package com.enterprise.fashion.ecommerce.product.config;

import java.time.Clock;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageTechnicalInspector;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageSecurityCheckPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageMalwareObservationPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageMalwareScanner;
import com.enterprise.fashion.ecommerce.product.adapter.out.media.ClamAvProductImageMalwareScanner;
import com.enterprise.fashion.ecommerce.product.adapter.out.media.UnavailableProductImageMalwareScanner;
import com.enterprise.fashion.ecommerce.product.application.usecase.CheckQuarantinedProductImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
public class ProductMediaSecurityCheckingConfiguration {
    @Bean
    CheckQuarantinedProductImage checkQuarantinedProductImage(ProductImageSecurityCheckPort storage,
            ProductImageMalwareObservationPort observations, ProductImageMalwareScanner scanner,
            PlatformTransactionManager manager) {
        var reads = transaction(manager, true);
        var writes = transaction(manager, false);
        return new CheckQuarantinedProductImage(storage, observations, scanner, reads, writes,
                ProductImageTechnicalInspector.Limits.conservativeDefaults(), Clock.systemUTC(), UUID::randomUUID);
    }

    @Bean
    ProductImageMalwareScanner productImageMalwareScanner(
            @Value("${product.media.clamav.host:}") String host,
            @Value("${product.media.clamav.port:3310}") int port,
            @Value("${product.media.clamav.connect-timeout-millis:1000}") int connectTimeout,
            @Value("${product.media.clamav.read-timeout-millis:5000}") int readTimeout,
            @Value("${product.media.clamav.maximum-stream-bytes:8388608}") int maximumStreamBytes,
            @Value("${product.media.clamav.maximum-response-bytes:4096}") int maximumResponseBytes) {
        if (host == null || host.isBlank()) return new UnavailableProductImageMalwareScanner();
        return new ClamAvProductImageMalwareScanner(new ClamAvProductImageMalwareScanner.Configuration(
                host, port, connectTimeout, readTimeout, maximumStreamBytes, maximumResponseBytes));
    }

    private static TransactionTemplate transaction(PlatformTransactionManager manager, boolean readOnly) {
        var transaction = new TransactionTemplate(manager);
        transaction.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        transaction.setReadOnly(readOnly);
        return transaction;
    }
}
