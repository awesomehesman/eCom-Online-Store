package com.enterprise.fashion.ecommerce.customer.domain.model;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CustomerAccountRelationshipTest {
    private final CustomerId customer = new CustomerId(new UUID(0, 1));
    private final AccountId account = new AccountId(new UUID(0, 2));

    @Test
    void retainsTheExactTypedReferences() {
        var relationship = new CustomerAccountRelationship(customer, account);

        assertThat(relationship.customerId()).isEqualTo(customer);
        assertThat(relationship.accountId()).isEqualTo(account);
        assertThat(relationship).isNotEqualTo(new CustomerAccountRelationship(customer, new AccountId(new UUID(0, 3))));
    }

    @Test
    void requiresBothReferences() {
        assertThatNullPointerException().isThrownBy(() -> new CustomerAccountRelationship(null, account));
        assertThatNullPointerException().isThrownBy(() -> new CustomerAccountRelationship(customer, null));
    }
}
