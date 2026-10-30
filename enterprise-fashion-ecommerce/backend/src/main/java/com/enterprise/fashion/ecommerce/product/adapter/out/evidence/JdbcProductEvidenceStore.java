package com.enterprise.fashion.ecommerce.product.adapter.out.evidence;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceEvent;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductEvidenceObservationPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Storage mechanics only. Append is package-private, has no application writer Port or command,
 * and cannot establish acceptance. Database recording attribution is not Identity evidence.
 */
@Repository
class JdbcProductEvidenceStore implements ProductEvidenceObservationPort {
    private final JdbcAggregateTemplate aggregates;
    private final JdbcClient jdbc;
    private final ProductPersistencePort products;

    JdbcProductEvidenceStore(JdbcAggregateTemplate aggregates, JdbcClient jdbc, ProductPersistencePort products) {
        this.aggregates = aggregates;
        this.jdbc = jdbc;
        this.products = products;
    }

    void append(EvidenceEventRow row) {
        Objects.requireNonNull(row);
        requireTransaction(true);
        ProductId owner = new ProductId(row.productId());
        ProductRevision revision = new ProductRevision(row.representationRevision());
        var purpose = ProductEvidenceEvent.Purpose.valueOf(row.purpose());
        var type = ProductEvidenceEvent.Type.valueOf(row.eventType());
        if (row.eventId() == null || row.sourceReference() == null || row.sourceReference().isBlank()
                || row.sourceActorClaim() == null || row.sourceActorClaim().isBlank()
                || row.supportingReferences() == null
                || Arrays.stream(row.supportingReferences()).anyMatch(v -> v == null || v.isBlank())
                || row.auditCorrelation() == null || !row.auditCorrelation().matches("[A-Za-z0-9._-]{1,128}")) {
            throw new IllegalArgumentException("Invalid evidence metadata");
        }
        var product = products.load(owner).orElseThrow(() -> new IllegalArgumentException("Evidence Product is absent"));
        if (type == ProductEvidenceEvent.Type.RECORD && !revision.equals(product.revision())) {
            throw new IllegalArgumentException("Evidence revision does not match Product");
        }
        // purpose/type parsing rejects unsupported purposes/outcomes; none is an acceptance.
        Objects.requireNonNull(purpose);
        aggregates.insert(row);
    }

    @Override
    public List<ProductEvidenceEvent> history(ProductId product, ProductEvidenceEvent.Purpose purpose) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(purpose);
        requireTransaction(false);
        try {
            return aggregates.findAll(Query.query(Criteria.where("product_id").is(product.value())
                    .and("purpose").is(purpose.name())).sort(Sort.by("recordedAt", "eventId")), EvidenceEventRow.class)
                    .stream().map(row -> new ProductEvidenceEvent(row.eventId(), new ProductId(row.productId()),
                            new ProductRevision(row.representationRevision()), ProductEvidenceEvent.Purpose.valueOf(row.purpose()),
                            ProductEvidenceEvent.Type.valueOf(row.eventType()), row.sourceReference(), row.sourceActorClaim(),
                            Arrays.asList(row.supportingReferences()), row.auditCorrelation(), row.recordedAt(), row.recordedBy(),
                            row.targetEventId(), row.replacementEventId())).toList();
        } catch (IllegalArgumentException | NullPointerException invalid) {
            throw new DataIntegrityViolationException("Invalid stored Product evidence history");
        }
    }

    private void requireTransaction(boolean writable) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Product evidence requires an application-owned transaction");
        }
        String isolation = jdbc.sql("SELECT current_setting('transaction_isolation')").query(String.class).single();
        if (!"repeatable read".equals(isolation) && !"serializable".equals(isolation)) {
            throw new IllegalStateException("Product evidence requires a consistent snapshot");
        }
        if (writable && !"off".equals(jdbc.sql("SELECT current_setting('transaction_read_only')").query(String.class).single())) {
            throw new IllegalStateException("Product evidence append requires a writable transaction");
        }
    }
}
