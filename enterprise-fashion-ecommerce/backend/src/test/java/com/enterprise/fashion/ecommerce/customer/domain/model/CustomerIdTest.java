package com.enterprise.fashion.ecommerce.customer.domain.model;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CustomerIdTest {
    @Test
    void preservesOpaqueIdentityAndRedactsItsStringRepresentation() {
        UUID value = UUID.fromString("d34b65a2-afca-4a78-9a71-204f1910bfae");
        var customer = new CustomerId(value);
        var account = new AccountId(value);

        assertThat(customer.value()).isEqualTo(value);
        assertThat(customer).isEqualTo(new CustomerId(value)).isNotEqualTo(account);
        assertThat(customer.toString()).doesNotContain(value.toString());
        assertThat(account.toString()).doesNotContain(value.toString());
    }

    @Test
    void requiresBothIdentifierTypesToHaveValues() {
        assertThatNullPointerException().isThrownBy(() -> new CustomerId(null));
        assertThatNullPointerException().isThrownBy(() -> new AccountId(null));
    }
}
