package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.StreamReadFeature;

/** Strict byte-local OAuth/OIDC token-response parsing. Performs no HTTP or authentication work. */
final class StaffOidcTokenResponseParser {
    record Result(StaffOidcTokenResponse tokenResponse,
            StaffOidcAuthenticationVerification.Outcome failure) {
        Result {
            if ((tokenResponse == null) == (failure == null)) {
                throw new IllegalArgumentException("Exactly one token-response result is required");
            }
        }
        static Result parsed(StaffOidcTokenResponse response) { return new Result(response, null); }
        static Result rejected(StaffOidcAuthenticationVerification.Outcome failure) {
            return new Result(null, Objects.requireNonNull(failure));
        }
        boolean parsed() { return tokenResponse != null; }
        @Override public String toString() {
            return parsed() ? "StaffOidcTokenResponseParsing[internal-success]"
                    : "StaffOidcTokenResponseParsing[" + failure + "]";
        }
    }

    private static final int MAXIMUM_FIELD_NAME_LENGTH = 64;
    private static final int MAXIMUM_PROTOCOL_VALUE_LENGTH = 128;
    private static final int MAXIMUM_ERROR_DETAIL_LENGTH = 1024;
    private static final Set<String> SUCCESS_FIELDS = Set.of(
            "access_token", "token_type", "expires_in", "id_token", "refresh_token");
    private static final Set<String> ERROR_FIELDS = Set.of("error", "error_description", "error_uri");
    private static final JsonFactory JSON = JsonFactory.builder()
            .enable(StreamReadFeature.STRICT_DUPLICATE_DETECTION).build();

    private final int maximumResponseSize;

    StaffOidcTokenResponseParser(int maximumResponseSize) {
        if (maximumResponseSize <= 0) {
            throw new IllegalArgumentException("Maximum response size is invalid");
        }
        this.maximumResponseSize = maximumResponseSize;
    }

    Result parse(int statusCode, String contentType, byte[] body) {
        if (statusCode < 100 || statusCode > 599) return rejected(
                StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        if (statusCode == 429 || statusCode >= 500) return rejected(
                StaffOidcAuthenticationVerification.Outcome.PROVIDER_UNAVAILABLE);
        StaffOidcAuthenticationVerification.Outcome invalid = statusCode == 200
                ? StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN
                : StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED;
        if (!supportedJson(contentType) || body == null || body.length == 0
                || body.length > maximumResponseSize) return rejected(invalid);
        try (JsonParser parser = JSON.createParser(body)) {
            if (parser.nextToken() != JsonToken.START_OBJECT) return rejected(invalid);
            ParsedFields fields = readFields(parser);
            if (fields == null || parser.nextToken() != null) return rejected(invalid);
            return statusCode == 200 ? success(fields) : error(fields);
        } catch (IOException | RuntimeException malformed) {
            return rejected(invalid);
        }
    }

    private Result success(ParsedFields fields) {
        if (fields.errorPresent || fields.errorDescriptionPresent || fields.errorUriPresent
                || fields.refreshTokenPresent || !fields.successFieldsComplete()
                || !"bearer".equals(fields.tokenType.toLowerCase(Locale.ROOT))) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        }
        return Result.parsed(new StaffOidcTokenResponse(fields.idToken));
    }

    private Result error(ParsedFields fields) {
        if (!fields.errorPresent || fields.anySuccessFieldPresent() || fields.refreshTokenPresent) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED);
        }
        return rejected(StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED);
    }

    private ParsedFields readFields(JsonParser parser) throws IOException {
        ParsedFields fields = new ParsedFields();
        Set<String> seen = new HashSet<>();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            if (parser.currentToken() != JsonToken.FIELD_NAME) return null;
            String name = parser.currentName();
            if (!bounded(name, MAXIMUM_FIELD_NAME_LENGTH) || !seen.add(name)
                    || !SUCCESS_FIELDS.contains(name) && !ERROR_FIELDS.contains(name)) return null;
            JsonToken valueToken = parser.nextToken();
            if (valueToken == null || valueToken == JsonToken.START_OBJECT || valueToken == JsonToken.START_ARRAY) {
                return null;
            }
            if (!readField(fields, name, parser, valueToken)) return null;
        }
        return fields;
    }

    private boolean readField(ParsedFields fields, String name, JsonParser parser, JsonToken token)
            throws IOException {
        return switch (name) {
            case "access_token" -> fields.accessTokenPresent = boundedString(parser, token, maximumResponseSize);
            case "id_token" -> {
                fields.idToken = string(parser, token, maximumResponseSize);
                yield fields.idToken != null;
            }
            case "token_type" -> {
                fields.tokenType = string(parser, token, MAXIMUM_PROTOCOL_VALUE_LENGTH);
                yield fields.tokenType != null;
            }
            case "expires_in" -> fields.expiresInPresent = nonNegativeInteger(parser, token);
            case "refresh_token" -> fields.refreshTokenPresent = true;
            case "error" -> fields.errorPresent = boundedString(parser, token, MAXIMUM_PROTOCOL_VALUE_LENGTH);
            case "error_description" -> fields.errorDescriptionPresent =
                    boundedString(parser, token, MAXIMUM_ERROR_DETAIL_LENGTH);
            case "error_uri" -> fields.errorUriPresent = boundedString(parser, token, MAXIMUM_ERROR_DETAIL_LENGTH);
            default -> false;
        };
    }

    private static boolean nonNegativeInteger(JsonParser parser, JsonToken token) throws IOException {
        if (token != JsonToken.VALUE_NUMBER_INT) return false;
        try {
            return parser.getLongValue() >= 0;
        } catch (RuntimeException invalid) {
            return false;
        }
    }

    private static boolean boundedString(JsonParser parser, JsonToken token, int maximum) throws IOException {
        return string(parser, token, maximum) != null;
    }

    private static String string(JsonParser parser, JsonToken token, int maximum) throws IOException {
        if (token != JsonToken.VALUE_STRING) return null;
        String value = parser.getText();
        return bounded(value, maximum) ? value : null;
    }

    private static boolean supportedJson(String contentType) {
        if (contentType == null || contentType.isBlank()) return false;
        String[] parts = contentType.split(";", -1);
        if (!"application/json".equalsIgnoreCase(parts[0].trim())) return false;
        boolean charsetSeen = false;
        for (int index = 1; index < parts.length; index++) {
            String parameter = parts[index].trim();
            int equals = parameter.indexOf('=');
            if (equals <= 0 || !"charset".equalsIgnoreCase(parameter.substring(0, equals).trim())
                    || charsetSeen) return false;
            String charset = parameter.substring(equals + 1).trim();
            if (charset.length() >= 2 && charset.startsWith("\"") && charset.endsWith("\"")) {
                charset = charset.substring(1, charset.length() - 1);
            }
            if (!"utf-8".equalsIgnoreCase(charset)) return false;
            charsetSeen = true;
        }
        return true;
    }

    private static boolean bounded(String value, int maximum) {
        return value != null && !value.isBlank() && value.length() <= maximum
                && value.codePoints().noneMatch(Character::isISOControl);
    }

    private static Result rejected(StaffOidcAuthenticationVerification.Outcome outcome) {
        return Result.rejected(outcome);
    }

    private static final class ParsedFields {
        private boolean accessTokenPresent;
        private String idToken;
        private String tokenType;
        private boolean expiresInPresent;
        private boolean refreshTokenPresent;
        private boolean errorPresent;
        private boolean errorDescriptionPresent;
        private boolean errorUriPresent;

        private boolean successFieldsComplete() {
            return accessTokenPresent && idToken != null && tokenType != null;
        }

        private boolean anySuccessFieldPresent() {
            return accessTokenPresent || idToken != null || tokenType != null || expiresInPresent;
        }
    }
}
