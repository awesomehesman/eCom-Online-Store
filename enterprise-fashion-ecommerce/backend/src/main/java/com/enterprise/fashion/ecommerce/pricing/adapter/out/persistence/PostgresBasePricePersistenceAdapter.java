package com.enterprise.fashion.ecommerce.pricing.adapter.out.persistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import com.enterprise.fashion.ecommerce.pricing.application.port.out.BasePricePersistencePort;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Currency;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Money;
import com.enterprise.fashion.ecommerce.pricing.domain.model.SelectVariantBasePrice;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.State;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.Subject;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Bounded JdbcClient history/head mechanics under ADR-0018: immutable revision insertion
 * and conditional head mutation cannot use unrestricted aggregate save semantics.
 * Application owns transactions; this adapter only requires them. No Product table access.
 */
@Repository
public class PostgresBasePricePersistenceAdapter implements BasePricePersistencePort {
    private final JdbcClient jdbc;
    public PostgresBasePricePersistenceAdapter(JdbcClient jdbc) { this.jdbc = Objects.requireNonNull(jdbc); }

    @Override
    public void initialize(Subject subject, String revision, Money money) {
        validate(subject, revision, money);
        transaction(false);
        jdbc.sql("INSERT INTO pricing.base_price_subject(variant_reference, product_reference, head_revision) VALUES (?, ?, ?)")
                .params(subject.variantReference(), subject.productReference(), revision).update();
        insert(subject, null, revision, money, "ASSIGN");
    }

    @Override
    public void replace(Subject subject, String expectedRevision, String revision, Money money) {
        validate(subject, revision, money);
        transaction(false);
        lock(subject, expectedRevision);
        insert(subject, expectedRevision, revision, money, "ASSIGN");
        advance(subject, expectedRevision, revision);
    }

    @Override
    public void withdraw(Subject subject, String expectedRevision, String revision) {
        Objects.requireNonNull(subject);
        if (revision == null || revision.isBlank()) { throw new IllegalArgumentException("Revision required"); }
        transaction(false);
        lock(subject, expectedRevision);
        var prior = jdbc.sql("SELECT revision, previous_revision, kind, amount, currency FROM pricing.base_price_revision WHERE variant_reference = ? AND revision = ?")
                .params(subject.variantReference(), expectedRevision).query((rs, row) -> new Revision(rs.getString("revision"),
                        rs.getString("previous_revision"), rs.getString("kind"), new Money(rs.getBigDecimal("amount"), new Currency(rs.getString("currency"))))).single();
        if (!"ASSIGN".equals(prior.kind())) { throw new RevisionConflict(); }
        insert(subject, expectedRevision, revision, prior.money(), "WITHDRAW");
        advance(subject, expectedRevision, revision);
    }

    @Override
    public List<VariantBasePriceAssignment> load(Subject subject) {
        Objects.requireNonNull(subject);
        transaction(true);
        var heads = jdbc.sql("SELECT product_reference, head_revision FROM pricing.base_price_subject WHERE variant_reference = ?")
                .param(subject.variantReference()).query((rs, row) -> new Head(rs.getString(1), rs.getString(2))).list();
        if (heads.isEmpty()) { return List.of(); }
        Head head = heads.getFirst();
        if (!subject.productReference().equals(head.product())) { throw new IllegalStateException("Pricing subject binding mismatch"); }
        var rows = jdbc.sql("SELECT product_reference, revision, previous_revision, kind, amount, currency FROM pricing.base_price_revision WHERE variant_reference = ? ORDER BY revision COLLATE \"C\"")
                .param(subject.variantReference()).query((rs, row) -> {
                    if (!subject.productReference().equals(rs.getString("product_reference"))) {
                        throw new IllegalStateException("Pricing revision ownership mismatch");
                    }
                    return new Revision(rs.getString("revision"), rs.getString("previous_revision"), rs.getString("kind"),
                            new Money(rs.getBigDecimal("amount"), new Currency(rs.getString("currency"))));
                }).list();
        var byRevision = new HashMap<String, Revision>();
        for (Revision row : rows) {
            if (byRevision.put(row.revision(), row) != null || !("ASSIGN".equals(row.kind()) || "WITHDRAW".equals(row.kind()))) {
                throw new IllegalStateException("Invalid Pricing revision history");
            }
        }
        var visited = new HashSet<String>();
        String cursor = head.revision();
        while (cursor != null) {
            Revision row = byRevision.get(cursor);
            if (row == null || !visited.add(cursor)) { throw new IllegalStateException("Broken Pricing revision chain"); }
            cursor = row.previous();
        }
        if (visited.size() != rows.size() || rows.isEmpty()) { throw new IllegalStateException("Incomplete Pricing revision observation"); }
        var result = rows.stream().map(row -> new VariantBasePriceAssignment(subject, row.revision(), row.money(),
                "WITHDRAW".equals(row.kind()) ? State.WITHDRAWN : row.revision().equals(head.revision()) ? State.CURRENT : State.OBSOLETE)).toList();
        var selection = new SelectVariantBasePrice().select(subject, result);
        if (selection instanceof SelectVariantBasePrice.Invalid || selection instanceof SelectVariantBasePrice.Conflict) {
            throw new IllegalStateException("Inconsistent Pricing observation");
        }
        return result;
    }

    private void lock(Subject subject, String expected) {
        var rows = jdbc.sql("SELECT product_reference, head_revision FROM pricing.base_price_subject WHERE variant_reference = ? FOR UPDATE")
                .param(subject.variantReference()).query((rs, row) -> new Head(rs.getString(1), rs.getString(2))).list();
        if (rows.isEmpty() || !subject.productReference().equals(rows.getFirst().product()) || !Objects.equals(expected, rows.getFirst().revision())) {
            throw new RevisionConflict();
        }
    }
    private void insert(Subject subject, String previous, String revision, Money money, String kind) {
        jdbc.sql("INSERT INTO pricing.base_price_revision(variant_reference, product_reference, revision, previous_revision, kind, amount, currency) VALUES (?, ?, ?, ?, ?, ?, ?)")
                .param(subject.variantReference()).param(subject.productReference()).param(revision)
                .param(previous).param(kind).param(money.amount()).param(money.currency().code()).update();
    }
    private void advance(Subject subject, String expected, String revision) {
        int changed = jdbc.sql("UPDATE pricing.base_price_subject SET head_revision = ? WHERE variant_reference = ? AND product_reference = ? AND head_revision = ?")
                .params(revision, subject.variantReference(), subject.productReference(), expected).update();
        if (changed != 1) { throw new RevisionConflict(); }
    }
    private void transaction(boolean snapshot) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) { throw new IllegalStateException("Pricing transaction required"); }
        if (snapshot) {
            String isolation = jdbc.sql("SELECT current_setting('transaction_isolation')").query(String.class).single();
            if (!"repeatable read".equals(isolation) && !"serializable".equals(isolation)) {
                throw new IllegalStateException("Pricing snapshot transaction required");
            }
        } else if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            throw new IllegalStateException("Pricing write transaction required");
        }
    }
    private static void validate(Subject subject, String revision, Money money) {
        new VariantBasePriceAssignment(subject, revision, money, State.CURRENT);
    }
    private record Head(String product, String revision) { }
    private record Revision(String revision, String previous, String kind, Money money) { }
}
