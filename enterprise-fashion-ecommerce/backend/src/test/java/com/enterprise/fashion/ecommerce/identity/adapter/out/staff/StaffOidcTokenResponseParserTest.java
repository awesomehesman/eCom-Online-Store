package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.*;

class StaffOidcTokenResponseParserTest {
    private static final String CONTENT_TYPE = "application/json";
    private static final String ID_TOKEN = "private-id-token";
    private static final String ACCESS_TOKEN = "private-access-token";
    private static final String VALID = """
            {"access_token":"private-access-token","token_type":"Bearer","id_token":"private-id-token"}
            """;
    private final StaffOidcTokenResponseParser parser = new StaffOidcTokenResponseParser(4096);

    @Test void parsesValidSuccessAndRetainsOnlyTheIdToken() {
        var result = parse(200, CONTENT_TYPE, VALID);

        assertThat(result.parsed()).isTrue();
        assertThat(result.failure()).isNull();
        assertThat(result.tokenResponse().idToken()).isEqualTo(ID_TOKEN);
        assertThat(StaffOidcTokenResponse.class.getRecordComponents())
                .extracting(component -> component.getName()).containsExactly("idToken");
        assertThat(Arrays.stream(StaffOidcTokenResponse.class.getDeclaredFields())
                .allMatch(field -> Modifier.isFinal(field.getModifiers()))).isTrue();
    }

    @Test void acceptsBearerCaseInsensitivelyAndUtf8ContentType() {
        assertThat(parse(200, "Application/JSON; charset=\"UTF-8\"",
                VALID.replace("Bearer", "bEaReR")).parsed()).isTrue();
    }

    @Test void rejectsMissingBlankAndNonStringIdToken() {
        assertInvalidSuccess("{\"access_token\":\"a\",\"token_type\":\"Bearer\"}");
        assertInvalidSuccess("{\"access_token\":\"a\",\"token_type\":\"Bearer\",\"id_token\":\" \"}");
        assertInvalidSuccess("{\"access_token\":\"a\",\"token_type\":\"Bearer\",\"id_token\":7}");
    }

    @Test void rejectsMissingBlankAndNonStringAccessToken() {
        assertInvalidSuccess("{\"token_type\":\"Bearer\",\"id_token\":\"i\"}");
        assertInvalidSuccess("{\"access_token\":\" \",\"token_type\":\"Bearer\",\"id_token\":\"i\"}");
        assertInvalidSuccess("{\"access_token\":true,\"token_type\":\"Bearer\",\"id_token\":\"i\"}");
    }

    @Test void rejectsMissingBlankAndUnsupportedTokenType() {
        assertInvalidSuccess("{\"access_token\":\"a\",\"id_token\":\"i\"}");
        assertInvalidSuccess("{\"access_token\":\"a\",\"token_type\":\" \",\"id_token\":\"i\"}");
        assertInvalidSuccess("{\"access_token\":\"a\",\"token_type\":\"MAC\",\"id_token\":\"i\"}");
    }

    @Test void acceptsOnlyNonnegativeIntegerExpiresIn() {
        assertThat(parse(200, CONTENT_TYPE, VALID.replace("}", ",\"expires_in\":0}")).parsed()).isTrue();
        assertThat(parse(200, CONTENT_TYPE, VALID.replace("}", ",\"expires_in\":3600}")).parsed()).isTrue();
        assertInvalidSuccess(VALID.replace("}", ",\"expires_in\":-1}"));
        assertInvalidSuccess(VALID.replace("}", ",\"expires_in\":1.5}"));
        assertInvalidSuccess(VALID.replace("}", ",\"expires_in\":\"3600\"}"));
    }

    @Test void rejectsDuplicateFields() {
        assertInvalidSuccess("""
                {"access_token":"a","token_type":"Bearer","id_token":"one","id_token":"two"}
                """);
    }

    @Test void rejectsNullEmptyMalformedAndNonObjectBodies() {
        assertThat(parser.parse(200, CONTENT_TYPE, null).failure()).isEqualTo(invalidToken());
        assertThat(parser.parse(200, CONTENT_TYPE, new byte[0]).failure()).isEqualTo(invalidToken());
        assertInvalidSuccess("{");
        assertInvalidSuccess("null");
        assertInvalidSuccess("[]");
        assertInvalidSuccess("\"value\"");
        assertThat(parser.parse(200, CONTENT_TYPE, new byte[] {'{', '"', 'x', '"', ':', '"',
                (byte) 0xc3, (byte) 0x28, '"', '}'}).failure()).isEqualTo(invalidToken());
    }

    @Test void rejectsTrailingJsonValuesAndNonWhitespaceContent() {
        assertInvalidSuccess(VALID + "{}");
        assertInvalidSuccess(VALID + " private");
        assertInvalidSuccess("{\"access_token\":\"a\",\"token_type\":\"Bearer\",\"id_token\":\"i\"");
        assertErrorRejected("{\"error\":\"invalid_grant\"");
    }

    @Test void rejectsMixedSuccessAndErrorFields() {
        assertInvalidSuccess(VALID.replace("}", ",\"error\":\"invalid_grant\"}"));
        assertThat(parse(400, CONTENT_TYPE,
                "{\"error\":\"invalid_grant\",\"id_token\":\"private\"}").failure())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED);
    }

    @Test void rejectsUnknownFieldsAndNestedValues() {
        assertInvalidSuccess(VALID.replace("}", ",\"extension\":\"value\"}"));
        assertInvalidSuccess(VALID.replace("\"private-id-token\"", "{}"));
        assertInvalidSuccess(VALID.replace("\"private-id-token\"", "[]"));
    }

    @Test void rejectsOversizedBodiesBeforeJsonParsing() {
        byte[] body = "x".repeat(4097).getBytes(StandardCharsets.UTF_8);
        assertThat(parser.parse(200, CONTENT_TYPE, body).failure()).isEqualTo(invalidToken());
        assertThatThrownBy(() -> new StaffOidcTokenResponseParser(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Maximum response size is invalid");
    }

    @Test void rejectsMissingUnsupportedAndAmbiguousContentTypes() {
        assertThat(parser.parse(200, null, bytes(VALID)).failure()).isEqualTo(invalidToken());
        assertThat(parser.parse(200, " ", bytes(VALID)).failure()).isEqualTo(invalidToken());
        assertThat(parser.parse(200, "text/json", bytes(VALID)).failure()).isEqualTo(invalidToken());
        assertThat(parser.parse(200, "application/json; charset=iso-8859-1", bytes(VALID)).failure())
                .isEqualTo(invalidToken());
        assertThat(parser.parse(200, "application/json; profile=x", bytes(VALID)).failure())
                .isEqualTo(invalidToken());
        assertThat(parser.parse(200, "application/json; charset=utf-8; charset=utf-8", bytes(VALID)).failure())
                .isEqualTo(invalidToken());
    }

    @Test void parsesValidOAuthErrorsIntoNegativeOutcomesWithoutDetail() {
        var rejected = parse(400, CONTENT_TYPE, """
                {"error":"invalid_grant","error_description":"private detail","error_uri":"https://private"}
                """);
        assertThat(rejected.parsed()).isFalse();
        assertThat(rejected.failure()).isEqualTo(StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED);
        assertThat(rejected.toString()).doesNotContain("private", "invalid_grant");

        assertThat(parse(503, CONTENT_TYPE, "{\"error\":\"temporarily_unavailable\"}").failure())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.PROVIDER_UNAVAILABLE);
        assertThat(parse(429, CONTENT_TYPE, "{\"error\":\"temporarily_unavailable\"}").failure())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.PROVIDER_UNAVAILABLE);
    }

    @Test void providerUnavailableStatusOverridesEveryResponseBodyCondition() {
        byte[][] bodies = {
                null,
                new byte[0],
                bytes("{"),
                bytes("x".repeat(4097)),
                bytes("{\"error\":\"one\",\"error\":\"two\"}"),
                bytes(VALID),
                bytes("{\"error_description\":\"private-provider-detail\"}")
        };
        for (int status : new int[] {429, 500, 503, 599}) {
            for (byte[] body : bodies) {
                var result = parser.parse(status, CONTENT_TYPE, body);
                assertThat(result.parsed()).isFalse();
                assertThat(result.failure())
                        .isEqualTo(StaffOidcAuthenticationVerification.Outcome.PROVIDER_UNAVAILABLE);
                assertThat(result.toString()).doesNotContain("private-provider-detail");
            }
            assertThat(parser.parse(status, "text/plain", bytes("private-provider-detail")).failure())
                    .isEqualTo(StaffOidcAuthenticationVerification.Outcome.PROVIDER_UNAVAILABLE);
        }
    }

    @Test void otherNonSuccessStatusesRemainExchangeRejectedForInvalidContent() {
        for (int status : new int[] {300, 400, 401, 403, 499}) {
            assertThat(parser.parse(status, null, null).failure())
                    .isEqualTo(StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED);
            assertThat(parser.parse(status, "text/plain", bytes("not-json")).failure())
                    .isEqualTo(StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED);
            assertThat(parser.parse(status, CONTENT_TYPE, bytes("{")).failure())
                    .isEqualTo(StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED);
        }
    }

    @Test void rejectsInvalidOAuthErrorResponses() {
        assertErrorRejected("{}");
        assertErrorRejected("{\"error\":\" \"}");
        assertErrorRejected("{\"error\":7}");
        assertErrorRejected("{\"error\":\"invalid_grant\",\"unknown\":\"value\"}");
        assertErrorRejected("{\"error\":\"invalid_grant\",\"error\":\"invalid_client\"}");
        assertErrorRejected("{\"error\":\"invalid_grant\",\"error_description\":{}}");
    }

    @Test void rejectsRefreshTokensRatherThanRetainingThem() {
        assertInvalidSuccess(VALID.replace("}", ",\"refresh_token\":\"private-refresh\"}"));
        assertErrorRejected("{\"error\":\"invalid_grant\",\"refresh_token\":\"private-refresh\"}");
    }

    @Test void sensitiveValuesAreRedactedFromInternalDiagnostics() {
        var result = parse(200, CONTENT_TYPE, VALID);
        assertThat(result.toString()).doesNotContain(ID_TOKEN, ACCESS_TOKEN);
        assertThat(result.tokenResponse().toString()).doesNotContain(ID_TOKEN, ACCESS_TOKEN);
        assertThatThrownBy(() -> new StaffOidcTokenResponse(" "))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("ID token is invalid");
    }

    @Test void parsingDoesNotMutateAuthenticationOrCreateASessionOrAuthority() {
        var original = new TestingAuthenticationToken("customer", "redacted", "CUSTOMER");
        SecurityContextHolder.getContext().setAuthentication(original);
        try {
            assertThat(parse(200, CONTENT_TYPE, VALID).parsed()).isTrue();
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(original);
            assertThat(original.getAuthorities()).extracting(Object::toString).containsExactly("CUSTOMER");
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private StaffOidcTokenResponseParser.Result parse(int status, String contentType, String body) {
        return parser.parse(status, contentType, bytes(body));
    }

    private void assertInvalidSuccess(String body) {
        assertThat(parse(200, CONTENT_TYPE, body).failure()).isEqualTo(invalidToken());
    }

    private void assertErrorRejected(String body) {
        assertThat(parse(400, CONTENT_TYPE, body).failure())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED);
    }

    private static byte[] bytes(String value) { return value.getBytes(StandardCharsets.UTF_8); }

    private static StaffOidcAuthenticationVerification.Outcome invalidToken() {
        return StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN;
    }
}
