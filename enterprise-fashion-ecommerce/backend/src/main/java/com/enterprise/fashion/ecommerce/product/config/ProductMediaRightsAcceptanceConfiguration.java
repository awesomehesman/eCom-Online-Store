package com.enterprise.fashion.ecommerce.product.config;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort;
import com.enterprise.fashion.ecommerce.product.application.usecase.RecordProductMediaRightsAcceptance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Configuration for media usage-rights acceptance recording components.
 * <p>
 * This configuration wires the use case with its recording port and transaction support.
 * It does NOT:
 * <ul>
 *   <li>Expose any HTTP endpoint, controller or public invocation path</li>
 *   <li>Configure an authoritative evidence source</li>
 *   <li>Change publication trust boundaries</li>
 *   <li>Enable Staff authentication or authorization</li>
 * </ul>
 * <p>
 * The existing fail-closed publication composition (UnavailableProductPublicationEvidenceAdapter
 * and reject-all policy) remains unchanged. Recording observations does not enable publication.
 * <p>
 * Media rights acceptance observations are distinct from content acceptance observations.
 * Both are required for publication readiness per DEC-0042 P2, but neither alone enables publication.
 */
@Configuration(proxyBeanMethods = false)
public class ProductMediaRightsAcceptanceConfiguration {

    /**
     * Creates the use case for recording media usage-rights acceptance observations.
     * <p>
     * The use case owns its transaction boundary with REPEATABLE_READ isolation,
     * matching the evidence store requirements.
     */
    @Bean
    RecordProductMediaRightsAcceptance recordProductMediaRightsAcceptance(
            ProductMediaRightsAcceptanceRecordingPort recordingPort,
            PlatformTransactionManager transactionManager) {
        TransactionTemplate writes = new TransactionTemplate(transactionManager);
        writes.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        writes.setReadOnly(false);
        return new RecordProductMediaRightsAcceptance(recordingPort, writes);
    }
}
