# Database Migration Governance

- Flyway is the sole database migration authority.
- Every migration must be attributable to its owning Module or Domain.
- Applied migrations are immutable. Corrections must use forward migrations.
- Runtime application startup or schema generation must not substitute for governed Flyway migrations.
- Concrete schemas and Domain migrations may be added only when the corresponding persistence owner and Aggregate or Domain implementation are authorized.
- Versioned migrations must use `V<version>__<description>.sql`.
- Migration descriptions must use clear lowercase `snake_case`.
- Migrations must not invent unauthorized schemas, tables, columns, indexes, constraints, sequences, extensions, or seed data.

This foundation increment intentionally contains no SQL migration.
