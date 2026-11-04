package com.enterprise.fashion.ecommerce.product.adapter.in.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductCatalogueQueryService;
import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductIdPage;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/** Explicit HTTP mappings only. No aggregate, revision, evidence or Staff data is serialized. */
@RestController
public final class ProductCatalogueController {
    private final ProductCatalogueQueryService queries;

    private final ProductCatalogueCursorCodec cursors;

    public ProductCatalogueController(ProductCatalogueQueryService queries, ProductCatalogueCursorCodec cursors) {
        this.queries = queries;
        this.cursors = cursors;
    }

    @GetMapping("/api/v1/products")
    public ResponseEntity<?> list(HttpServletRequest request) {
        String correlation = correlation(request);
        final int size;
        final ProductIdPage.Cursor cursor;
        try {
            Map<String, String[]> params = request.getParameterMap();
            if (params.keySet().stream().anyMatch(key -> !"limit".equals(key) && !"cursor".equals(key))) {
                return problem(400, correlation);
            }
            String[] limits = params.get("limit");
            if (limits == null) {
                size = ProductIdPage.DEFAULT_SIZE;
            } else {
                if (limits.length != 1 || !limits[0].matches("[0-9]+")) {
                    return problem(400, correlation);
                }
                String digits = limits[0].replaceFirst("^0+(?!$)", "");
                if (digits.length() > 3) {
                    return problem(400, correlation);
                }
                size = Integer.parseInt(digits);
                ProductIdPage.validateSize(size);
            }
            String[] tokens = params.get("cursor");
            if (tokens != null && tokens.length != 1) {
                return problem(400, correlation);
            }
            cursor = tokens == null ? null : cursors.decode(tokens[0], size);
            cursors.requireAvailable();
        } catch (ProductCatalogueCursorCodec.Unavailable unavailable) {
            return problem(503, correlation);
        } catch (IllegalArgumentException malformed) {
            return problem(400, correlation);
        } catch (RuntimeException unexpected) {
            return problem(500, correlation);
        }
        try {
            return switch (queries.list(size, cursor)) {
                case CONFIRMED_EMPTY -> ResponseEntity.ok().headers(headers(correlation))
                        .contentType(MediaType.APPLICATION_JSON).body(new EmptyPage(List.of(), size, null));
                case AUTHORITY_UNAVAILABLE -> problem(503, correlation);
                case ERROR -> problem(500, correlation);
            };
        } catch (RuntimeException unexpected) {
            return problem(500, correlation);
        }
    }

    @GetMapping("/api/v1/products/{productId}")
    public ResponseEntity<?> detail(@PathVariable("productId") String productId, HttpServletRequest request) {
        String correlation = correlation(request);
        final ProductId id;
        try {
            if (!request.getParameterMap().isEmpty()) {
                return problem(400, correlation);
            }
            id = new ProductId(productId);
        } catch (IllegalArgumentException malformed) {
            return problem(400, correlation);
        } catch (RuntimeException unexpected) {
            return problem(500, correlation);
        }
        try {
            return switch (queries.detail(id)) {
                case NOT_FOUND, NOT_VISIBLE -> problem(404, correlation);
                case EVIDENCE_UNCONFIRMED, AUTHORITY_UNAVAILABLE -> problem(503, correlation);
                case ERROR -> problem(500, correlation);
            };
        } catch (RuntimeException unexpected) {
            return problem(500, correlation);
        }
    }

    static String correlation(HttpServletRequest request) {
        var values = Collections.list(request.getHeaders("X-Correlation-ID"));
        return values.size() == 1 && values.getFirst().length() <= 128
                && values.getFirst().matches("[A-Za-z0-9._-]{1,128}")
                ? values.getFirst() : UUID.randomUUID().toString();
    }

    private static HttpHeaders headers(String correlation) {
        var headers = new HttpHeaders();
        headers.setCacheControl("no-store");
        headers.set("X-Correlation-ID", correlation);
        return headers;
    }

    static ResponseEntity<?> problem(int status, String correlation) {
        String title = switch (status) {
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 503 -> "Service Unavailable";
            default -> "Internal Server Error";
        };
        return ResponseEntity.status(status).headers(headers(correlation))
                .contentType(MediaType.APPLICATION_PROBLEM_JSON).body(new Problem("about:blank", title, status, correlation));
    }

    // Only the currently reachable successful representation is implemented. A Product DTO
    // requires a real positive visibility producer; serializing domain objects is never fallback.
    private record EmptyPage(List<Object> items, int pageSize, String nextCursor) { }
    private record Problem(String type, String title, int status, String correlationId) { }
}
