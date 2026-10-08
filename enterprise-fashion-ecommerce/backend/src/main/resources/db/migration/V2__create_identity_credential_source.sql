-- Fixed internal context; slots serialize source operations, not credential cardinality.
-- No source population, acceptance default, runtime grants or lifecycle operation is supplied.
CREATE TABLE identity.credential_source_scope (
    subject UUID NOT NULL,
    context VARCHAR(32) NOT NULL CHECK (context = 'LOCAL_CUSTOMER_PASSWORD'),
    PRIMARY KEY (subject, context)
);

CREATE TABLE identity.credential_publication (
    fact UUID PRIMARY KEY,
    subject UUID NOT NULL,
    context VARCHAR(32) NOT NULL,
    verifier VARCHAR(128) NOT NULL CHECK (length(trim(verifier)) > 0),
    supersedes UUID,
    UNIQUE (fact, subject, context),
    FOREIGN KEY (subject, context)
        REFERENCES identity.credential_source_scope(subject, context),
    FOREIGN KEY (supersedes, subject, context)
        REFERENCES identity.credential_publication(fact, subject, context),
    CHECK (supersedes IS NULL OR supersedes <> fact)
);
CREATE INDEX idx_credential_publication_subject
    ON identity.credential_publication(subject, context);
CREATE INDEX idx_credential_publication_supersedes
    ON identity.credential_publication(supersedes) WHERE supersedes IS NOT NULL;
