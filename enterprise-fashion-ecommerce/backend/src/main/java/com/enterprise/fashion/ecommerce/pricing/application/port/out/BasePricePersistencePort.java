package com.enterprise.fashion.ecommerce.pricing.application.port.out;

import java.util.List;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Money;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.Subject;

/** Internal storage mechanics only; references and committed rows do not establish commercial authority. */
public interface BasePricePersistencePort {
    void initialize(Subject subject, String revision, Money money);
    void replace(Subject subject, String expectedRevision, String revision, Money money);
    void withdraw(Subject subject, String expectedRevision, String revision);
    List<VariantBasePriceAssignment> load(Subject subject);

    final class RevisionConflict extends RuntimeException {
        public RevisionConflict() { super("Base-price revision conflict"); }
    }
}
