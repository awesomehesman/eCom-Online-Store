CREATE SCHEMA customer;

-- The exact pair is unique. Neither identifier alone determines relationship cardinality.
CREATE TABLE customer.account_relationship (
    customer_id UUID NOT NULL,
    account_id UUID NOT NULL,
    PRIMARY KEY (customer_id, account_id)
);
