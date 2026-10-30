package com.enterprise.fashion.ecommerce.product.adapter.in.web;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductIdPage;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@SpringBootTest(properties = "product.catalogue.cursor-secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
@AutoConfigureMockMvc
@Testcontainers
class ProductCatalogueHttpIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private ProductCatalogueCursorCodec cursors;
    @Autowired private MockMvc http;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private ObjectMapper json;
    @Autowired private JdbcIndexedSessionRepository sessions;
    @MockitoSpyBean private ProductPersistenceService products;

    @Test
    void authenticatedContinuationRevalidatesRatherThanReturningUnconfirmedProducts() throws Exception {
        // Synthetic protocol boundary only, not a production-issued eligible page.
        fixture("A", "DRAFT");
        fixture("B", "ARCHIVED");
        fixture("C", "PUBLISHED");
        String token = cursors.encode(new ProductIdPage.Cursor(new ProductId("A"), 1));
        problem(http.perform(get("/api/v1/products").param("limit", "1").param("cursor", token)).andReturn(), 503);
        verify(products).pageCatalogueCandidateIds(1, new ProductIdPage.Cursor(new ProductId("A"), 1));
        problem(http.perform(get("/api/v1/products").param("cursor", token)).andReturn(), 400);
        problem(http.perform(get("/api/v1/products").param("limit", "2").param("cursor", token)).andReturn(), 400);
        problem(http.perform(get("/api/v1/products").param("cursor", token, token)).andReturn(), 400);
    }

    @Test
    void continuationAtConfirmedEndReturnsNoCandidateCursor() throws Exception {
        fixture("B", "DRAFT");
        fixture("C", "ARCHIVED");
        String token = cursors.encode(new ProductIdPage.Cursor(new ProductId("A"), 20));
        var result = http.perform(get("/api/v1/products").param("cursor", token)).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(json.readTree(result.getResponse().getContentAsString()).get("nextCursor").isNull()).isTrue();
        assertThat(result.getResponse().getHeader("Cache-Control")).isEqualTo("no-store");
        assertThat(result.getResponse().getHeaders("Set-Cookie")).isEmpty();
        assertThat(result.getRequest().getSession(false)).isNull();
    }

    @Test
    void opaqueProtocolPreservesLiveExclusiveOrderingWithoutDuplicateCandidates() {
        fixture("A", "PUBLISHED");
        fixture("a", "PUBLISHED");
        fixture("z", "PUBLISHED");
        ProductIdPage first = products.pageCatalogueCandidateIds(1, null);
        assertThat(first.ids()).containsExactly(new ProductId("A"));
        String token = cursors.encode(first.nextCursor()); // Isolated protocol exercise, never HTTP issuance.
        fixture("0", "PUBLISHED"); // Earlier insertion must not replay the boundary.
        fixture("B", "PUBLISHED"); // Later insertion is eligible for live candidate traversal, not visibility.
        var second = products.pageCatalogueCandidateIds(1, cursors.decode(token, 1));
        var third = products.pageCatalogueCandidateIds(1, cursors.decode(cursors.encode(second.nextCursor()), 1));
        assertThat(second.ids()).containsExactly(new ProductId("B"));
        assertThat(third.ids()).containsExactly(new ProductId("a"));
        assertThat(java.util.stream.Stream.of(first, second, third).flatMap(page -> page.ids().stream()).toList())
                .doesNotHaveDuplicates();
    }

    @BeforeEach
    void clearSyntheticCatalogue() {
        jdbc.update("DELETE FROM product.product_media");
        jdbc.update("DELETE FROM product.product_variant");
        jdbc.update("DELETE FROM product.product");
    }

    private Product fixture(String id, String lifecycle) {
        Product product = new Product(new ProductId(id), ProductFamily.ACCESSORIES, ProductAttributeStandard.INITIAL, false, false)
                .withRepresentation(new ProductContent("PRIVATE PRODUCT NAME", "PRIVATE PRODUCT DESCRIPTION"), List.of());
        products.create(product);
        jdbc.update("UPDATE product.product SET lifecycle = ? WHERE product_id = ?", lifecycle, id);
        return product;
    }

    private void problem(MvcResult result, int expected) throws Exception {
        var response = result.getResponse();
        assertThat(response.getStatus()).isEqualTo(expected);
        assertThat(response.getContentType()).startsWith("application/problem+json");
        assertThat(response.getHeader("Cache-Control")).isEqualTo("no-store");
        String correlation = response.getHeader("X-Correlation-ID");
        assertThat(correlation).matches("[A-Za-z0-9._-]{1,128}");
        var body = json.readTree(response.getContentAsString());
        var fields = new ArrayList<String>();
        body.fieldNames().forEachRemaining(fields::add);
        assertThat(fields).containsExactlyInAnyOrder("type", "title", "status", "correlationId");
        assertThat(body.get("type").asText()).isEqualTo("about:blank");
        assertThat(body.get("status").asInt()).isEqualTo(expected);
        assertThat(body.get("correlationId").asText()).isEqualTo(correlation);
        assertThat(response.getContentAsString()).doesNotContain("PRIVATE", "representation_revision", "sourceActor", "SELECT", "Exception");
        assertThat(response.getHeader("Set-Cookie")).isNull();
        assertThat(result.getRequest().getSession(false)).isNull();
    }

    @Test
    void anonymousEmptyCatalogueHasExactSchemaAndNoSessionOrCache() throws Exception {
        var result = http.perform(get("/api/v1/products").header("X-Correlation-ID", "safe-correlation-1"))
                .andExpect(status().isOk()).andReturn();
        assertThat(json.readTree(result.getResponse().getContentAsString()))
                .isEqualTo(json.readTree("{\"items\":[],\"pageSize\":20,\"nextCursor\":null}"));
        assertThat(result.getResponse().getHeader("Cache-Control")).isEqualTo("no-store");
        assertThat(result.getResponse().getHeader("X-Correlation-ID")).isEqualTo("safe-correlation-1");
        assertThat(result.getResponse().getHeader("Set-Cookie")).isNull();
        assertThat(result.getRequest().getSession(false)).isNull();
        verify(products).pageCatalogueCandidateIds(20, null);
    }

    @Test
    void hiddenRecordsDoNotAffectPublicPageBoundariesOrExposeTheirIds() throws Exception {
        for (int i = 0; i < 25; i++) {
            fixture("hidden-%02d".formatted(i), i % 2 == 0 ? "DRAFT" : "ARCHIVED");
        }
        for (String limit : new String[]{"1", "20", "100", "00020"}) {
            var response = http.perform(get("/api/v1/products").param("limit", limit)).andExpect(status().isOk()).andReturn().getResponse();
            var body = json.readTree(response.getContentAsString());
            assertThat(body.get("items").isEmpty()).isTrue();
            assertThat(body.get("nextCursor").isNull()).isTrue();
            assertThat(response.getContentAsString()).doesNotContain("hidden", "PRIVATE");
        }
        fixture("zz-unconfirmed", "PUBLISHED");
        problem(http.perform(get("/api/v1/products").param("limit", "1")).andReturn(), 503);
    }

    @ParameterizedTest
    @ValueSource(strings = {"DRAFT", "ARCHIVED", "PUBLISHED"})
    void detailsConcealNonvisibleProductsAndWithholdUnconfirmedPublished(String lifecycle) throws Exception {
        Product product = fixture(UUID.randomUUID().toString(), lifecycle);
        var before = jdbc.queryForMap("SELECT * FROM product.product WHERE product_id = ?", product.id().value());
        problem(http.perform(get("/api/v1/products/" + product.id().value())).andReturn(), "PUBLISHED".equals(lifecycle) ? 503 : 404);
        assertThat(jdbc.queryForMap("SELECT * FROM product.product WHERE product_id = ?", product.id().value())).isEqualTo(before);
    }

    @Test
    void missingDetailsHaveSameSafeResponseAsDraft() throws Exception {
        fixture("draft", "DRAFT");
        var missing = http.perform(get("/api/v1/products/missing").header("X-Correlation-ID", "same-correlation")).andReturn();
        var draft = http.perform(get("/api/v1/products/draft").header("X-Correlation-ID", "same-correlation")).andReturn();
        problem(missing, 404);
        problem(draft, 404);
        assertThat(missing.getResponse().getContentAsString()).isEqualTo(draft.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "0", "-1", "101", "+1", "1.0", "1e2", "999999999999999999999"})
    void malformedLimitsAreSafeBadRequests(String limit) throws Exception {
        problem(http.perform(get("/api/v1/products").param("limit", limit)).andReturn(), 400);
    }

    @Test
    void rejectsDuplicateUnknownAndDetailQueryParameters() throws Exception {
        problem(http.perform(get("/api/v1/products").param("limit", "1", "2")).andReturn(), 400);
        problem(http.perform(get("/api/v1/products").param("search", "private-value")).andReturn(), 400);
        problem(http.perform(get("/api/v1/products/unknown").param("limit", "20")).andReturn(), 400);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "raw-product-id", "eyJpZCI6InByaXZhdGUifQ", "tampered-token", "bad+token"})
    void rejectsUnsupportedOrManipulatedPublicCursorsWithoutDecodingInternalIds(String cursor) throws Exception {
        problem(http.perform(get("/api/v1/products").param("cursor", cursor)).andReturn(), 400);
    }

    @Test
    void rejectsOverlongCursorAndMalformedProductIds() throws Exception {
        problem(http.perform(get("/api/v1/products").param("cursor", "a".repeat(2049))).andReturn(), 400);
        for (String id : new String[]{"bad.id", "!bad", "a".repeat(129)}) {
            problem(http.perform(get("/api/v1/products/" + id)).andReturn(), 400);
        }
    }

    @Test
    void invalidRepeatedOrMissingCorrelationIsSafelyReplaced() throws Exception {
        for (String invalid : new String[]{"", "bad~value", "a".repeat(129)}) {
            var result = http.perform(get("/api/v1/products/missing").header("X-Correlation-ID", invalid)).andReturn();
            problem(result, 404);
            assertThat(result.getResponse().getHeader("X-Correlation-ID")).isNotEqualTo(invalid);
        }
        problem(http.perform(get("/api/v1/products/missing").header("X-Correlation-ID", "first", "second")).andReturn(), 404);
        problem(http.perform(get("/api/v1/products/missing")).andReturn(), 404);
    }

    @Test
    void authorityUnavailabilityIs503AndUnexpectedDatabaseFailureIs500() throws Exception {
        ProductId id = new ProductId("unavailable");
        doThrow(new DataAccessResourceFailureException("PRIVATE connection diagnostic")).when(products).load(id);
        problem(http.perform(get("/api/v1/products/unavailable")).andReturn(), 503);
        doThrow(new DataAccessResourceFailureException("PRIVATE page diagnostic"))
                .when(products).pageCatalogueCandidateIds(eq(20), isNull());
        problem(http.perform(get("/api/v1/products")).andReturn(), 503);
    }

    @Test
    void realDatabaseFailureNeverBecomes404OrAnEmptyPage() throws Exception {
        jdbc.execute("ALTER TABLE product.product RENAME TO unavailable_product");
        try {
            problem(http.perform(get("/api/v1/products")).andReturn(), 500);
            problem(http.perform(get("/api/v1/products/missing")).andReturn(), 500);
        } finally {
            jdbc.execute("ALTER TABLE product.unavailable_product RENAME TO product");
        }
    }

    @Test
    void publicSecurityChainDoesNotOpenMutationsAdministrationOrHead() throws Exception {
        http.perform(post("/api/v1/products").with(csrf())).andExpect(status().is4xxClientError());
        http.perform(post("/api/v1/products/x/publish").with(csrf())).andExpect(status().is4xxClientError());
        http.perform(get("/api/v1/products/x/acceptance")).andExpect(status().is4xxClientError());
        http.perform(get("/api/v1/admin/products")).andExpect(status().is4xxClientError());
        http.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head("/api/v1/products"))
                .andExpect(status().is4xxClientError());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private org.springframework.session.SessionRepository<org.springframework.session.Session> sessionStore() {
        return (org.springframework.session.SessionRepository) sessions;
    }

    @Test
    void anonymousReadsDoNotRenewAnExistingSessionCookie() throws Exception {
        var store = sessionStore();
        org.springframework.session.Session session = store.createSession();
        session.setMaxInactiveInterval(Duration.ofHours(2));
        session.setLastAccessedTime(Instant.now().minusSeconds(30));
        store.save(session);
        Instant before = store.findById(session.getId()).getLastAccessedTime();
        String cookie = java.util.Base64.getEncoder().encodeToString(
                session.getId().getBytes(java.nio.charset.StandardCharsets.UTF_8));
        var result = http.perform(get("/api/v1/products/missing").cookie(new jakarta.servlet.http.Cookie("SESSION", cookie)))
                .andReturn();
        problem(result, 404);
        assertThat(store.findById(session.getId()).getLastAccessedTime()).isEqualTo(before);
    }

    @Test
    void candidateKeysetsKeepExactStableOrderWithoutProtectedPositions() throws Exception {
        fixture("A", "PUBLISHED");
        fixture("a", "PUBLISHED");
        fixture("-hidden", "DRAFT");
        fixture("Z-hidden", "ARCHIVED");
        ProductIdPage first = products.pageCatalogueCandidateIds(1, null);
        assertThat(first.ids()).containsExactly(new ProductId("A"));
        ProductIdPage second = products.pageCatalogueCandidateIds(1, first.nextCursor());
        assertThat(second.ids()).containsExactly(new ProductId("a"));
        assertThat(second.nextCursor()).isNull();
        problem(http.perform(get("/api/v1/products").param("limit", "1")).andReturn(), 503);
    }

    @Test
    void productErrorDispatchUsesSafeProblemWithoutExposingServletDiagnostics() throws Exception {
        var result = http.perform(get("/error").with(request -> {
                    request.setDispatcherType(jakarta.servlet.DispatcherType.ERROR);
                    return request;
                })
                .requestAttr(jakarta.servlet.RequestDispatcher.ERROR_REQUEST_URI, "/api/v1/products/private-product")
                .requestAttr(jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION, new IllegalStateException("PRIVATE diagnostic")))
                .andReturn();
        problem(result, 500);
        assertThat(result.getResponse().getContentAsString()).doesNotContain("private-product", "diagnostic");
    }

    @Test
    void conditionalRequestsStillRevalidateWithout304OrEtag() throws Exception {
        var empty = http.perform(get("/api/v1/products").header("If-None-Match", "*")).andExpect(status().isOk()).andReturn();
        assertThat(empty.getResponse().getHeader("ETag")).isNull();
        assertThat(empty.getResponse().getHeader("Cache-Control")).isEqualTo("no-store");
        fixture("unconfirmed", "PUBLISHED");
        var result = http.perform(get("/api/v1/products/unconfirmed").header("If-None-Match", "*"))
                .andExpect(status().isServiceUnavailable()).andReturn();
        assertThat(result.getResponse().getHeader("ETag")).isNull();
        problem(result, 503);
    }
}
