package com.enterprise.fashion.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EnterpriseFashionEcommerceApplicationTest {

    @Test
    void applicationIsAnnotatedForSpringBoot() {
        assertTrue(
                EnterpriseFashionEcommerceApplication.class.isAnnotationPresent(SpringBootApplication.class));
    }
}
