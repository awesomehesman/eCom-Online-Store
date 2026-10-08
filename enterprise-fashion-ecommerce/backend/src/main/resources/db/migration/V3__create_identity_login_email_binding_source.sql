-- Identity-owned dormant binding facts. No producer, issuer, verifier or accepted default exists.
-- A scope row serializes publication and observation for one stable Identity.
CREATE TABLE identity.login_email_binding_scope (
    subject UUID PRIMARY KEY
);

CREATE TABLE identity.login_email_binding (
    fact UUID PRIMARY KEY,
    subject UUID NOT NULL REFERENCES identity.login_email_binding_scope(subject),
    email TEXT NOT NULL CHECK (length(email) > 0),
    supersedes UUID,
    UNIQUE (fact, subject),
    FOREIGN KEY (supersedes, subject)
        REFERENCES identity.login_email_binding(fact, subject),
    CHECK (supersedes IS NULL OR supersedes <> fact)
);
CREATE INDEX idx_login_email_binding_subject
    ON identity.login_email_binding(subject);
CREATE INDEX idx_login_email_binding_supersedes
    ON identity.login_email_binding(supersedes) WHERE supersedes IS NOT NULL;
