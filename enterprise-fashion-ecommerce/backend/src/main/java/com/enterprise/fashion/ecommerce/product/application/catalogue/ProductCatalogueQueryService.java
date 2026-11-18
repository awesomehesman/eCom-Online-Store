package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.catalogue.ProjectProductCatalogueRepresentation.EligibleProductRepresentation;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.CannotCreateTransactionException;

/** Product-backed public Catalogue orchestration. Only VISIBLE exact aggregates are projected. */
public final class ProductCatalogueQueryService {
    public sealed interface ListingResult permits Page, AuthorityUnavailable, QueryError { }

    public record Page(List<ProductCatalogueRepresentation> items, int pageSize,
            ProductIdPage.Cursor nextCursor) implements ListingResult {
        public Page {
            ProductIdPage.validateSize(pageSize);
            items = List.copyOf(items);
            if (items.size() > pageSize || (nextCursor != null && (items.isEmpty()
                    || nextCursor.pageSize() != pageSize
                    || !nextCursor.afterExclusive().value().equals(items.getLast().productId())))) {
                throw new IllegalArgumentException("Invalid Product catalogue page");
            }
        }
    }

    public record AuthorityUnavailable() implements ListingResult { }
    public record QueryError() implements ListingResult { }

    public sealed interface DetailResult permits Found, NotFound, NotVisible,
            DetailAuthorityUnavailable, DetailError { }

    public record Found(ProductCatalogueRepresentation product) implements DetailResult {
        public Found { Objects.requireNonNull(product); }
    }
    public record NotFound() implements DetailResult { }
    public record NotVisible() implements DetailResult { }
    public record DetailAuthorityUnavailable() implements DetailResult { }
    public record DetailError() implements DetailResult { }

    private final ProductPersistenceService products;
    private final AssessProductVisibilityReadiness readiness;
    private final ProjectProductCatalogueRepresentation projector;

    public ProductCatalogueQueryService(ProductPersistenceService products,
            AssessProductVisibilityReadiness readiness, ProjectProductCatalogueRepresentation projector) {
        this.products = Objects.requireNonNull(products);
        this.readiness = Objects.requireNonNull(readiness);
        this.projector = Objects.requireNonNull(projector);
    }

    public DetailResult detail(ProductId id) {
        Objects.requireNonNull(id);
        try {
            Product product = products.load(id).orElse(null);
            if (product == null) return new NotFound();
            return switch (readiness.assess(product)) {
                case VISIBLE -> new Found(project(product));
                case NOT_FOUND, NOT_VISIBLE -> new NotVisible();
                case EVIDENCE_UNCONFIRMED, AUTHORITY_UNAVAILABLE -> new DetailAuthorityUnavailable();
                case ERROR -> new DetailError();
            };
        } catch (DataAccessResourceFailureException | TransientDataAccessResourceException
                | QueryTimeoutException | CannotCreateTransactionException unavailable) {
            return new DetailAuthorityUnavailable();
        } catch (RuntimeException unexpected) {
            return new DetailError();
        }
    }

    public ListingResult list(int size) { return list(size, null); }

    public ListingResult list(int size, ProductIdPage.Cursor cursor) {
        ProductIdPage.validateSize(size);
        if (cursor != null && cursor.pageSize() != size) {
            throw new IllegalArgumentException("Incompatible continuation");
        }
        try {
            ProductCatalogueCandidatePage candidates = products.pageCatalogueCandidates(size, cursor);
            var visible = new ArrayList<ProductCatalogueRepresentation>(candidates.products().size());
            for (Product product : candidates.products()) {
                switch (readiness.assess(product)) {
                    case VISIBLE -> visible.add(project(product));
                    case NOT_FOUND, NOT_VISIBLE, EVIDENCE_UNCONFIRMED, AUTHORITY_UNAVAILABLE -> {
                        return new AuthorityUnavailable();
                    }
                    case ERROR -> { return new QueryError(); }
                }
            }
            return new Page(visible, size, candidates.nextCursor());
        } catch (DataAccessResourceFailureException | TransientDataAccessResourceException
                | QueryTimeoutException | CannotCreateTransactionException unavailable) {
            return new AuthorityUnavailable();
        } catch (RuntimeException unexpected) {
            return new QueryError();
        }
    }

    private ProductCatalogueRepresentation project(Product product) {
        return projector.project(new EligibleProductRepresentation(product.id(), product.content(), product.family(),
                product.standard(), product.sizeApplicable(), product.colourApplicable(),
                product.variants(), product.media()));
    }
}
