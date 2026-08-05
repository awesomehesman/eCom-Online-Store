---
title: AGENTS
version: 0.3.0
status: Draft
owner: Engineering
last_updated: 2026-08-05
applies_to:
  - Codex
  - Kiro
  - GitHub Copilot
  - Claude Code
  - Human contributors
review_cycle: Monthly
source_of_truth: true
---

# AGENTS.md

> Enterprise AI Engineering Constitution

## 1. Purpose and Authority

This document defines the mandatory engineering constitution for every AI coding agent and human contributor working in this repository. It governs how contributors must analyse requirements, make decisions, design solutions, modify files, generate code, write tests, update documentation, review changes, and prepare work for merge.

This file is not a collection of optional recommendations. Unless a rule is explicitly marked as guidance, its statements are normative and must be followed. The words **must**, **must not**, **required**, and **prohibited** indicate mandatory rules. The words **should** and **should not** indicate strong defaults that may be overridden only when a documented reason exists.

The purpose of this constitution is to ensure that contributions remain:

- Aligned with the product vision and business requirements.
- Consistent with the approved architecture.
- Secure, accessible, observable, and maintainable by default.
- Predictable across different AI tools and human contributors.
- Testable and traceable from requirement to implementation.
- Suitable for production rather than merely demonstrable in a prototype.

When an agent begins work, it must treat this document as the primary behavioural instruction for repository-level engineering. More specialised files define detailed standards for their respective areas, but they inherit the principles established here.

## 2. Vision

The repository exists to deliver a production-grade, premium fashion e-commerce platform that provides customers with a fast, trustworthy, accessible, and refined shopping experience while providing the business with reliable control over catalogue, inventory, pricing, customers, checkout, payments, orders, shipping, content, administration, reporting, and future growth.

The engineering system around the product must be equally intentional. A new contributor—human or AI—should be able to enter the repository, locate the governing context, understand the system boundaries, determine the applicable standards, and make a correct contribution without relying on undocumented tribal knowledge.

The repository therefore serves four purposes simultaneously:

1. **Product implementation:** the runnable frontend, backend, infrastructure, data, and automation required to operate the platform.
2. **Engineering source of truth:** the architecture, standards, domain specifications, decisions, and constraints that govern implementation.
3. **AI context system:** structured, tool-agnostic context that enables coding agents to work consistently and safely.
4. **Operational record:** traceable evidence of decisions, changes, tests, releases, and production practices.

Success means more than shipping features. The platform must remain understandable, supportable, secure, and adaptable as the product, team, integrations, and technology landscape evolve.

## 3. Product and Engineering Philosophy

### 3.1 Product quality over output volume

Contributors must optimise for useful, correct, production-ready outcomes rather than the number of generated files, lines of code, endpoints, components, or documents. A smaller complete solution is preferable to a large collection of placeholders.

### 3.2 Correctness over cleverness

Solutions must favour explicit, understandable behaviour over compact or novel implementations. Clever abstractions that obscure business rules, complicate debugging, or increase onboarding cost are prohibited unless their benefit is clear and documented.

### 3.3 Readability over brevity

Code and documentation must communicate intent. Names must be descriptive, control flow must be understandable, and important design decisions must be visible rather than hidden behind unexplained conventions or framework magic.

### 3.4 Customer trust is a functional requirement

Accuracy of price, stock, payment state, order status, delivery information, privacy choices, and transactional communication is part of the product—not an operational afterthought. The system must never knowingly present uncertain data as confirmed truth.

### 3.5 Security, privacy, and accessibility by design

Security, privacy, and accessibility must be considered during requirements and design, not added after implementation. A feature that functions but exposes avoidable risk or excludes users is incomplete.

### 3.6 Domain logic belongs in the domain

Business rules must not be scattered across controllers, Angular components, database scripts, provider adapters, or UI templates. Rules must have an intentional owner and be implemented at the correct architectural boundary.

### 3.7 Tests are executable specifications

Automated tests must prove important behaviour, not merely increase coverage statistics. Tests should describe business outcomes, protect domain rules, and prevent regression at system boundaries.

### 3.8 Documentation and implementation evolve together

Documentation must be updated in the same change as the implementation it governs. A code change that invalidates architecture, API, database, operational, or user-facing documentation is not complete until the affected documentation is corrected.

### 3.9 Observability is part of implementation

Critical business operations must be diagnosable. Logging, metrics, traces, audit records, and correlation identifiers must be designed alongside the workflow they support.

### 3.10 Build for the current product, preserve the future path

The project must avoid premature complexity, but it must not create preventable dead ends. Implement the simplest solution that satisfies current requirements while preserving clear domain boundaries, replaceable integrations, versionable APIs, and maintainable data ownership.

## 4. Guiding Principles

All contributors must apply the following principles:

- Product quality takes precedence over speed.
- Correctness takes precedence over cleverness.
- Readability takes precedence over brevity.
- Security, privacy, accessibility, maintainability, performance, and observability are first-class requirements.
- Business rules belong in the appropriate domain or application boundary.
- Every material change must be testable.
- Every material change must be explainable.
- Every externally visible behaviour must be traceable to a requirement, specification, or approved decision.
- Existing standards must be reused before creating new conventions.
- Duplication must not be introduced when an existing abstraction or shared rule already owns the behaviour.
- External integrations must be isolated behind project-owned contracts.
- Database integrity must not depend only on frontend or application validation.
- Client-side authorization must never be treated as a security boundary.
- Generated output must be reviewed before it is presented as complete.
- Placeholder content must never be described as implementation-ready documentation.

## 5. Decision Hierarchy

When instructions, files, or implementation options conflict, contributors must resolve the conflict using this hierarchy, from highest to lowest authority:

1. Applicable law, regulatory obligations, security requirements, and contractual constraints.
2. Approved business requirements and domain rules.
3. Accepted Architecture Decision Records.
4. `.ai/core/ARCHITECTURE.md` and approved architecture specifications.
5. Domain specifications under `specifications/domains/`.
6. Cross-cutting standards in `.ai/core/`.
7. Technology-specific standards in `.ai/backend/` and `.ai/frontend/`.
8. API, database, event, and integration contracts.
9. Feature-level specifications and acceptance criteria.
10. Existing local implementation patterns that remain consistent with higher-level guidance.
11. Agent or contributor preference.

A lower-authority source must never silently override a higher-authority source.

When two sources at the same authority level conflict, the contributor must:

1. Stop the affected implementation work.
2. Identify the exact conflict.
3. Determine whether an existing ADR resolves it.
4. Propose a resolution and its consequences.
5. Update the relevant source-of-truth documents after approval.
6. Continue only when the decision is explicit.

## 6. AI and Contributor Responsibilities

Before modifying the repository, every contributor must:

1. Read this file.
2. Identify the business domain and technical areas affected.
3. Read the applicable product, architecture, domain, security, testing, API, database, frontend, backend, and documentation standards.
4. Inspect relevant existing implementation before proposing a new pattern.
5. Identify assumptions, dependencies, risks, and open decisions.
6. Confirm whether the requested work is documentation, implementation, refactoring, investigation, review, or incident remediation.

During implementation, contributors must:

- Preserve domain boundaries.
- Reuse approved patterns and shared components.
- Avoid inventing business rules, provider behaviour, data fields, or requirements.
- Make assumptions explicit when progress is possible without clarification.
- Request clarification only when ambiguity would materially affect correctness, security, data integrity, customer experience, cost, or architecture.
- Keep changes focused on the requested scope.
- Avoid unrelated cleanup unless it is necessary for correctness or explicitly approved.
- Include appropriate validation, error handling, logging, tests, and documentation.
- Protect backward compatibility unless a breaking change is explicitly approved.
- Never conceal failed tests, incomplete work, uncertainty, or unsupported claims.

Before presenting work as complete, contributors must review the final diff and verify compliance with this constitution.

## 7. Definition of Done

A change is complete only when all applicable conditions below are satisfied.

### 7.1 Requirements and behaviour

- The implementation satisfies the approved requirements and acceptance criteria.
- Business rules are implemented once at the correct boundary.
- Expected success, validation, empty, loading, failure, retry, and permission states are handled.
- Relevant edge cases and concurrency concerns are addressed.
- No unapproved scope has been introduced.

### 7.2 Architecture and maintainability

- The change follows approved architecture and dependency rules.
- Domain ownership is preserved.
- Naming communicates intent.
- No unnecessary duplication, coupling, or abstraction is introduced.
- Public contracts remain backward compatible or the breaking change is approved and versioned.

### 7.3 Testing and quality

- Required unit, integration, contract, component, end-to-end, security, accessibility, or performance tests are implemented.
- Tests pass in the supported development environment and CI pipeline.
- Tests verify meaningful behaviour and failure conditions.
- Static analysis, formatting, linting, build, migration, and quality checks pass.

### 7.4 Security, privacy, and accessibility

- Authentication and authorization are enforced at trusted boundaries.
- Inputs, outputs, uploads, secrets, and personal information are handled safely.
- Logging excludes passwords, tokens, payment credentials, and unnecessary personal information.
- Accessibility requirements are met for affected user interfaces.
- Threats introduced by the change have been considered and mitigated.

### 7.5 Data and operations

- Database changes use approved migrations and constraints.
- Data compatibility, rollback, backup, and retention impacts are considered.
- Critical operations are observable through appropriate logs, metrics, traces, and audit records.
- Operational documentation and runbooks are updated where required.

### 7.6 Documentation

- Source-of-truth documentation is updated in the same change.
- API, event, schema, configuration, and environment changes are documented.
- New decisions are recorded in an ADR when required.
- Examples and commands are accurate and usable.
- No placeholder section remains for functionality represented as complete.

## 8. AI Self-Review Checklist

Before proposing or finalising a change, the agent must verify:

- [ ] I read the relevant governing context before making changes.
- [ ] The change satisfies a documented requirement or approved decision.
- [ ] Naming follows repository conventions and communicates intent.
- [ ] Domain boundaries and dependency rules are respected.
- [ ] Business logic is not duplicated or placed in the wrong layer.
- [ ] API and data contracts remain compatible or are explicitly versioned.
- [ ] Validation and error handling cover expected failure modes.
- [ ] Authentication, authorization, privacy, and secret handling were reviewed.
- [ ] Accessibility was considered for all affected user interactions.
- [ ] Performance and scalability implications were considered.
- [ ] Logs, metrics, traces, and audit events are appropriate and safe.
- [ ] Tests cover the main behaviour, negative paths, and edge cases.
- [ ] All relevant quality checks pass.
- [ ] Documentation and examples are synchronized with the implementation.
- [ ] The final diff contains no unrelated edits, secrets, generated noise, or placeholders presented as finished work.
- [ ] Any remaining uncertainty, risk, or follow-up work is stated clearly.

## 9. Repository Structure

The repository is organised so that product intent, engineering standards, implementation, infrastructure, and governance remain distinct but traceable. Contributors must place files according to ownership and purpose rather than convenience.

```text
enterprise-fashion-ecommerce/
├── .ai/
│   ├── core/
│   ├── backend/
│   ├── frontend/
│   ├── prompts/
│   └── agents/
├── specifications/
│   ├── business/
│   ├── domains/
│   ├── frontend/
│   ├── backend/
│   ├── infrastructure/
│   └── adr/
├── frontend/
├── backend/
├── infrastructure/
├── scripts/
├── tools/
├── docs/
├── .github/
├── README.md
├── CONTRIBUTING.md
├── ROADMAP.md
├── CHANGELOG.md
├── LICENSE
└── CODE_OF_CONDUCT.md
```

### 9.1 `.ai/`

The `.ai/` directory contains the governing context consumed by AI agents and human contributors.

- `.ai/core/` contains tool-agnostic product, architecture, engineering, documentation, testing, security, design-system, and glossary standards.
- `.ai/backend/` contains backend-specific standards for Java, Spring, APIs, events, databases, PostgreSQL, and Azure integrations.
- `.ai/frontend/` contains frontend-specific standards for Angular, UI, accessibility, performance, and Storybook.
- `.ai/prompts/` contains reusable task templates. Prompts must reference source-of-truth documents instead of duplicating their content.
- `.ai/agents/` contains tool-specific integration guidance. Tool-specific files must not override core engineering standards.

Contributors must not add implementation code, generated binaries, secrets, temporary notes, or feature-specific requirements to `.ai/`.

### 9.2 `specifications/`

The `specifications/` directory contains approved or draft requirements and implementation contracts.

- `specifications/business/` contains vision, personas, journeys, business requirements, success measures, assumptions, constraints, and policy decisions.
- `specifications/domains/` contains self-contained domain specifications such as product, category, customer, inventory, pricing, order, payment, shipping, and administration.
- `specifications/frontend/` contains page, component, interaction, state, analytics, accessibility, and responsive specifications.
- `specifications/backend/` contains application-service, integration, event, API, and backend workflow specifications.
- `specifications/infrastructure/` contains environments, deployment, networking, observability, backup, recovery, and operational specifications.
- `specifications/adr/` contains Architecture Decision Records. ADR filenames must use a zero-padded identifier and concise kebab-case title.

Feature implementation must not begin until the applicable specification is sufficiently complete to make the requested behaviour testable.

### 9.3 `frontend/`

The `frontend/` directory contains the Angular application and related client-side tooling.

It may include:

- Application source code.
- Design-system implementation.
- Storybook configuration and stories.
- Unit, component, accessibility, and end-to-end tests.
- Static assets owned by the frontend.
- Build and development configuration.

Frontend code must not duplicate backend-owned business rules or contain secrets.

### 9.4 `backend/`

The `backend/` directory contains the Java and Spring Boot application.

It may include:

- Domain modules.
- Application services.
- REST API adapters.
- Persistence adapters.
- External integration adapters.
- Database migrations.
- Unit, integration, contract, and architecture tests.

Backend modules must follow the approved domain boundaries and dependency rules defined in `ARCHITECTURE.md`.

### 9.5 `infrastructure/`

The `infrastructure/` directory contains version-controlled infrastructure definitions and deployment configuration.

It may include:

- Infrastructure as Code.
- Environment templates.
- Networking and identity configuration.
- Monitoring and alert definitions.
- Deployment manifests.
- Backup and recovery automation.

Secrets and environment-specific credentials must never be committed.

### 9.6 `scripts/` and `tools/`

- `scripts/` contains small, repository-owned automation used for builds, validation, generation, migration, setup, or maintenance.
- `tools/` contains larger internal utilities or pinned tool configuration.

Scripts must be deterministic, documented, safe by default, and fail clearly. Destructive scripts must require explicit confirmation or a clearly named force flag.

### 9.7 `docs/`

The `docs/` directory contains explanatory or operational documentation that does not belong in governing AI context or formal specifications.

Examples include:

- Onboarding guides.
- Local-development instructions.
- Operational runbooks.
- Troubleshooting guides.
- Release notes.
- Support procedures.

Source-of-truth standards must remain in `.ai/` or `specifications/`; `docs/` must link to them rather than duplicate them.

### 9.8 `.github/`

The `.github/` directory contains repository automation and collaboration controls, including:

- CI/CD workflows.
- Pull-request templates.
- Issue templates.
- CODEOWNERS.
- Dependency update configuration.
- Repository security guidance.

Workflow files must use least privilege, pin third-party actions to approved versions or commit SHAs, and avoid exposing secrets to untrusted code paths.

### 9.9 File-placement rules

Before creating a file, contributors must determine:

1. Who owns the content?
2. Is it a governing standard, formal specification, implementation file, operational guide, or generated artifact?
3. What existing file should link to it?
4. Whether a new file is necessary or an existing source of truth should be extended.

The following are prohibited:

- Duplicate standards in multiple folders.
- Untracked decision notes used as de facto requirements.
- Generic `misc`, `temp`, `new`, `final`, or `backup` folders.
- Files named with ambiguous suffixes such as `final-v2-latest`.
- Generated output committed without a documented reason.
- Empty folders represented only by meaningless placeholder files once real content exists.

## 10. Development Workflow

All work must follow a traceable, reviewable workflow from requirement to merge.

### 10.1 Work classification

Before making changes, classify the work as one of the following:

- Feature development.
- Bug fix.
- Security remediation.
- Refactoring.
- Documentation change.
- Infrastructure change.
- Dependency maintenance.
- Investigation or spike.
- Incident remediation.

The classification determines the required specification, tests, review depth, and release handling.

### 10.2 Standard workflow

The default workflow is:

1. Confirm the requested outcome and scope.
2. Identify the governing requirements and standards.
3. Inspect the current implementation and related tests.
4. Identify affected domains, contracts, data, security boundaries, and operational concerns.
5. Create or update the specification when behaviour is not already defined.
6. Record an ADR when the change introduces or modifies a material architecture decision.
7. Create a focused branch from the latest approved integration branch.
8. Implement the smallest complete change that satisfies the requirement.
9. Add or update tests at the appropriate levels.
10. Update documentation, contracts, migrations, examples, and runbooks in the same branch.
11. Run all applicable local quality checks.
12. Review the final diff against the AI Self-Review Checklist.
13. Open a pull request with sufficient context for independent review.
14. Resolve review findings without silently expanding scope.
15. Merge only after required checks and approvals pass.
16. Verify the merged result in the target environment when the change requires deployment validation.

### 10.3 Specification readiness

A feature is ready for implementation when the applicable specification defines:

- Business objective.
- In-scope and out-of-scope behaviour.
- Actors and permissions.
- Functional and non-functional requirements.
- Business and validation rules.
- Success, empty, loading, error, retry, and cancellation states.
- API, event, data, and integration impacts.
- Security, privacy, accessibility, performance, and observability expectations.
- Acceptance criteria.
- Known assumptions and open decisions.

When a task is intentionally exploratory, it must be labelled as a spike and must not be presented as production implementation.

### 10.4 Change scope

Branches and pull requests should have one primary purpose. Contributors must avoid bundling unrelated features, refactors, dependency upgrades, formatting changes, and documentation rewrites into one change.

A necessary supporting change may be included when it is directly required by the primary outcome and clearly described in the pull request.

### 10.5 Local verification

Before pushing, contributors must run all applicable commands defined by the affected project, including:

- Formatting.
- Linting.
- Type checking.
- Unit tests.
- Integration tests.
- Architecture tests.
- Database migration validation.
- Build.
- Component or Storybook checks.
- End-to-end tests when practical.
- Security or dependency scans when required.

An agent must not claim that checks passed unless it actually executed them and observed a successful result.

### 10.6 Incomplete work

Incomplete work must not be disguised as complete through placeholder implementations, disabled tests, broad `TODO` comments, hard-coded success responses, fake provider behaviour, or swallowed errors.

When partial work is intentionally committed:

- The limitation must be explicit.
- The code path must remain safe.
- The change must not be enabled in production unless approved.
- Follow-up work must be tracked.
- Documentation must reflect the actual state.

## 11. Branch Strategy

The repository uses a controlled integration workflow.

### 11.1 Long-lived branches

- `main` represents the stable, releasable production baseline.
- `develop` represents the approved integration baseline while the project uses a develop-based workflow.

Direct commits to protected long-lived branches are prohibited except for approved emergency procedures.

### 11.2 Short-lived branches

All normal work must occur on short-lived branches created from the latest target branch.

Approved branch prefixes:

- `feature/` for new product behaviour.
- `fix/` for defects.
- `security/` for security remediation.
- `refactor/` for behaviour-preserving structural improvements.
- `docs/` for documentation-only changes.
- `test/` for test-only changes.
- `chore/` for maintenance and tooling.
- `infra/` for infrastructure changes.
- `release/` for release preparation when used.
- `hotfix/` for approved urgent production fixes.

Branch names must be lowercase, concise, hyphen-separated, and describe the outcome.

Examples:

```text
feature/product-variant-selection
fix/payment-webhook-idempotency
security/admin-session-rotation
docs/agents-repository-workflow
infra/production-monitoring-baseline
```

Prohibited branch names include personal names, ticket-only identifiers without context, spaces, vague terms, and long natural-language sentences.

### 11.3 Branch freshness

Before creating a branch, contributors must update the target branch locally. Before opening or updating a pull request, contributors must ensure the branch can be integrated cleanly and must resolve conflicts intentionally rather than accepting one side wholesale.

### 11.4 Branch lifetime

Branches should be merged or closed promptly. Long-running work must be divided into safe, reviewable increments where possible. Branches must not become alternate integration environments with undocumented divergence.

### 11.5 Hotfixes

A hotfix is permitted only for urgent production risk such as:

- Security exposure.
- Payment or order corruption.
- Severe customer-facing outage.
- Data-loss risk.
- Regulatory or contractual breach.

Hotfixes must still include focused tests, review, documentation of risk, and back-merging into all affected long-lived branches.

## 12. Commit Standards

Commits form the permanent technical history of the repository. Each commit must be understandable without relying on private context.

### 12.1 Commit format

Use Conventional Commits:

```text
<type>(<scope>): <imperative summary>
```

Approved common types:

- `feat`
- `fix`
- `docs`
- `refactor`
- `test`
- `chore`
- `build`
- `ci`
- `perf`
- `security`
- `revert`

Examples:

```text
feat(catalogue): add product publication workflow
fix(payments): prevent duplicate webhook confirmation
docs(ai): define repository and branch standards
refactor(orders): extract fulfilment transition policy
test(inventory): cover concurrent stock reservations
```

### 12.2 Commit summary rules

The summary must:

- Use imperative mood.
- Start with a lowercase word after the colon.
- Describe the outcome, not the activity.
- Avoid a trailing period.
- Remain concise enough to scan in history.

Avoid summaries such as:

```text
updated files
changes
work in progress
fix stuff
final version
```

### 12.3 Commit body

Add a body when the reason, trade-off, migration impact, risk, or behaviour is not obvious from the summary.

A useful body explains:

- Why the change is necessary.
- What behaviour changed.
- Any migration or compatibility impact.
- Important alternatives rejected.

Do not use the body to repeat the diff line by line.

### 12.4 Atomic commits

A commit should represent one coherent logical change. It must be possible to review, revert, and understand the commit independently where practical.

Contributors must not mix:

- Unrelated formatting changes.
- Multiple independent features.
- Generated artifacts without source changes.
- Refactoring and behaviour changes that cannot be reviewed separately.
- Secret or local-environment files.

### 12.5 Signed and verified history

Where repository policy enables commit signing, contributors should use signed commits. Automated commits must identify the automation and must not impersonate a human contributor.

### 12.6 Fixup and temporary commits

Temporary `fixup`, `WIP`, or checkpoint commits may exist on a local or draft branch, but they must be squashed, renamed, or intentionally preserved before merge according to repository merge policy.

## 13. Pull Request Standards

A pull request is the primary review and integration unit. It must provide enough evidence for a reviewer to understand the intent, risks, behaviour, and verification without reconstructing the work from the diff alone.

### 13.1 Required pull-request content

Every pull request must include:

- Clear summary of the outcome.
- Business or technical reason.
- Scope and explicit exclusions.
- Related issue, requirement, specification, or ADR references.
- Description of significant design decisions.
- Security, privacy, accessibility, data, migration, and operational impact where applicable.
- Test evidence and commands executed.
- Screenshots or recordings for material UI changes.
- API examples for contract changes.
- Rollout, rollback, or feature-flag notes where required.
- Known limitations and follow-up work.

### 13.2 Reviewability

Pull requests must be small enough to review with reasonable confidence. When a change is large, contributors should separate it into ordered, independently safe pull requests such as:

1. Specification and ADR.
2. Schema or contract foundation.
3. Backend implementation.
4. Frontend implementation.
5. Operational enablement.

Reviewability must not be achieved by hiding complexity in generated output or excluding necessary tests and documentation.

### 13.3 Draft pull requests

Use a draft pull request for early integration feedback when the change is not ready to merge. Draft status does not excuse unsafe code, committed secrets, misleading documentation, or broken shared branches.

### 13.4 Review expectations

Reviewers must assess:

- Requirement correctness.
- Architecture and domain boundaries.
- Security and privacy.
- Data integrity and migration safety.
- API and event compatibility.
- Error handling and observability.
- Accessibility and user experience.
- Performance and scalability.
- Test quality.
- Documentation accuracy.
- Operational readiness.

Approval must not be based only on successful CI.

### 13.5 Responding to review

Contributors must address review comments through one of the following:

- Apply the requested correction.
- Explain why the existing approach is correct with evidence.
- Propose an alternative resolution.
- Record a larger decision in an ADR.

Comments must not be marked resolved while the underlying concern remains unaddressed.

### 13.6 Merge requirements

A pull request may be merged only when:

- Required approvals are present.
- Required automated checks pass.
- Blocking conversations are resolved.
- The branch is current according to repository policy.
- Documentation and migrations are complete.
- No unresolved high-severity security, correctness, or data-integrity concern remains.

### 13.7 Merge strategy

The repository should use a consistent merge strategy configured at repository level. Squash merging is preferred for short-lived branches when individual branch commits are not independently valuable. Rebase or merge commits may be used when preserving a structured series is materially useful.

The final merge commit or squash message must follow the commit standards in this document.

## Documentation Standards

### Purpose

Establish documentation as the authoritative source of product, architecture, engineering, and operational truth. Ensure documentation is accurate, useful, and synchronized with implementation.

### Principles

- Documentation is a first-class deliverable and must be maintained as carefully as code.
- Every significant change must update relevant documentation in the same change.
- Documentation must be owned, reviewed, and versioned.

### Mandatory Standards

- All specifications, standards, and contracts must be maintained in the repository, not in private or external systems.
- Documentation must be updated in the same pull request as the code, schema, or contract it governs.
- Documentation must follow the repository’s markdown conventions (see below).
- Each specification must include: Purpose, Scope, Actors, Requirements, Rules, Acceptance Criteria, API/Data Impacts, Security/Accessibility/Performance/Observability, and Open Decisions.
- Architecture Decision Records (ADRs) must use the approved template and reside in `specifications/adr/`.
- Diagrams must be SVG or PlantUML, committed as source and rendered output.
- Examples must be executable or copy-paste verifiable where practical.
- Versioning of documentation must match the implementation version.
- Documentation must be reviewed before merge using the criteria below.

### Recommended Practices

- Use diagrams and tables to clarify complex flows.
- Reference, rather than duplicate, content from other source-of-truth documents.
- Use code blocks, callouts, and cross-links for clarity.
- Provide before/after examples for major changes.
- Use inclusive, clear, and concise language.

### Anti-patterns

- Outdated, orphaned, or unowned documentation.
- Placeholder or “TBD” sections in master or release branches.
- Private notes or tribal knowledge that contradicts repository documentation.
- Duplicating standards in multiple locations.
- Screenshots of code or configuration (use text/code blocks).
- Unexplained acronyms or jargon.

### Review Checklist

- [ ] Is the documentation accurate, complete, and current?
- [ ] Does it follow the required structure and markdown conventions?
- [ ] Are diagrams and examples present and correct?
- [ ] Are changes versioned and linked to implementation?
- [ ] Are prohibited documentation practices avoided?
- [ ] Are all placeholder sections removed?

---

## API Standards

### Purpose

Ensure APIs are consistent, discoverable, secure, and maintainable. Enable client and backend teams to work independently and safely.

### Principles

- APIs are contracts: changes must be intentional and versioned.
- RESTful conventions and OpenAPI must be followed unless otherwise specified.
- APIs must be explicit, predictable, and safe by default.

### Mandatory Standards

- Use RESTful resource-oriented design. Each resource has a plural, kebab-case name (e.g., `/products`, `/order-items`).
- HTTP methods: `GET` (read), `POST` (create), `PUT` (replace), `PATCH` (partial update), `DELETE` (remove).
- Use standard HTTP status codes (2xx, 4xx, 5xx) appropriately.
- Pagination: Use `limit`/`offset` or `cursor` for large collections, with metadata in response.
- Filtering/sorting: Use query parameters (`?status=active&sort=-created_at`).
- Idempotency: All `PUT` and `DELETE` must be idempotent; `POST` must support idempotency keys where needed.
- Versioning: Use URI versioning (`/v1/`) for breaking changes.
- Validation: Validate all input, reject invalid requests with clear errors.
- Error responses: Use RFC7807 Problem Details (`application/problem+json`), with `type`, `title`, `status`, `detail`, and `instance`.
- Authentication: All APIs must require authentication unless explicitly public.
- Authorization: Enforce RBAC at the API boundary.
- Correlation ID: Accept and propagate a `X-Correlation-ID` header.
- OpenAPI: All APIs must have complete, up-to-date OpenAPI definitions.
- Backward compatibility: Breaking changes require new version and deprecation plan.
- Deprecation: Mark deprecated endpoints in OpenAPI and provide migration guidance.

### Recommended Practices

- Use resource nesting only when it reflects true ownership.
- Prefer explicit status transitions for workflows.
- Provide consistent error codes and messages.
- Document common query parameters and error scenarios.
- Use OpenAPI examples for all endpoints.

### Anti-patterns

- Ambiguous or inconsistent resource names.
- Business logic in controllers rather than services.
- Leaking internal errors or stack traces.
- Accepting or returning sensitive data unnecessarily.
- Breaking backward compatibility without versioning.

### Review Checklist

- [ ] Are resource names, methods, and status codes correct?
- [ ] Are pagination, filtering, and sorting implemented and documented?
- [ ] Are validation and error responses RFC7807-compliant?
- [ ] Is authentication and authorization enforced?
- [ ] Is the OpenAPI spec complete and accurate?
- [ ] Are breaking changes versioned and deprecated properly?

---

## Database Standards

### Purpose

Ensure data integrity, consistency, scalability, and maintainability across database schemas and migrations.

### Principles

- Schema is a contract: changes must be controlled and backward compatible.
- Data integrity and normalization are default.
- Schema must be observable and auditable.

### Mandatory Standards

- All primary keys must use UUIDv4 unless a documented exception exists.
- Table names must be plural, snake_case (e.g., `order_items`).
- Column names must be snake_case, descriptive, and avoid reserved words.
- Normalize data to at least 3NF unless denormalization is justified.
- Indexes must be defined for all foreign keys, unique constraints, and common queries.
- All schema changes must use versioned migrations (e.g., Flyway, Liquibase).
- Foreign keys must enforce referential integrity.
- Soft deletes: Use a `deleted_at` timestamp for logical deletion; never physically delete unless required.
- Auditing: Use `created_at`, `updated_at`, `created_by`, `updated_by` fields.
- Optimistic locking: Use a version column for concurrent updates.
- Transactions: Define clear transaction boundaries; avoid long-running transactions.
- Seed data: Only use for system-critical or test purposes, not for business data.
- Rollback: All migrations must be reversible where feasible.
- Performance: Analyze query plans for new or changed queries.

### Recommended Practices

- Use enum tables for controlled vocabularies.
- Use database constraints for business rules where appropriate.
- Document schema changes in migration files.
- Regularly review and prune unused indexes.

### Anti-patterns

- Using integer autoincrement for public IDs.
- Hard-coding production data in migrations.
- Relying on application logic for referential integrity.
- Massive tables without partitioning or archiving.

### Review Checklist

- [ ] Are naming, keys, and normalization correct?
- [ ] Are indexes and constraints appropriate?
- [ ] Are migrations versioned, reversible, and documented?
- [ ] Is auditing, soft delete, and optimistic locking implemented?
- [ ] Are performance and rollback considered?

---

## Angular Standards

### Purpose

Ensure Angular frontend is modular, maintainable, performant, and accessible.

### Principles

- Favor standalone components and signals-first state management.
- Separate smart (container) and presentational (dumb) components.
- Prioritize accessibility, performance, and testability.

### Mandatory Standards

- Use standalone components by default; avoid legacy NgModules for new code.
- Prefer Signals for state, minimize RxJS to async boundaries and effects.
- Use RxJS only for side effects, HTTP, or event streams; avoid overusing `Subject`.
- Folder structure: Group by feature/domain, not by technical type.
- Smart components handle data loading, routing, and state; presentational components are stateless and reusable.
- Use Angular Router with lazy loading for all major features.
- Global state must be managed with explicit, simple stores or signals; avoid over-complex libraries unless justified.
- Forms: Use Angular Reactive Forms for all forms.
- Accessibility: Use semantic HTML, ARIA roles, and test with screen readers.
- Styling: Use component-scoped styles, avoid global CSS.
- Performance: Use `OnPush` change detection, trackBy in `*ngFor`, and lazy load images/assets.
- Testing: Unit and component tests are required for all significant logic and UI.

### Recommended Practices

- Use feature modules for large features if needed for routing or separation.
- Use Storybook for reusable components.
- Use Angular CLI for scaffolding and building.
- Prefer composition over inheritance in components.

### Anti-patterns

- Fat components mixing data, state, and view logic.
- Deeply nested folder structures by technical type.
- Uncontrolled use of RxJS operators for local state.
- Global CSS or side effects in component constructors.
- Untested or unreviewed direct DOM manipulation.

### Review Checklist

- [ ] Are standalone components and signals used appropriately?
- [ ] Are smart/presentational boundaries respected?
- [ ] Is folder organization by feature?
- [ ] Are routing, lazy loading, and state management correct?
- [ ] Are accessibility and performance standards met?
- [ ] Are tests present and meaningful?

---

## Java Standards

### Purpose

Ensure backend code is modular, testable, maintainable, and secure using Spring Boot.

### Principles

- Follow layered, modular architecture with clear package boundaries.
- Use dependency injection and favor constructor injection.
- Separate domain, application, adapter, and configuration code.

### Mandatory Standards

- Use package-by-feature/domain, not by technical type.
- Constructor injection is required; avoid field or setter injection.
- Validation: Use Bean Validation (JSR-380) for all incoming data.
- Use DTOs for API boundaries; never expose entities directly.
- Mapping: Use MapStruct or explicit mappers for DTO/entity conversion.
- Transaction management: Use `@Transactional` at the service layer, not the controller.
- Exception handling: Use `@ControllerAdvice` for API error responses.
- Logging: Use SLF4J; never log sensitive data.
- Configuration: Use `application.yaml` or `application.properties` with profiles.
- Testing: Unit, integration, and contract tests are required for all services and APIs.
- Dependency management: Use BOM and avoid unnecessary dependencies.

### Recommended Practices

- Use records for immutable DTOs (Java 17+).
- Use sealed interfaces for domain hierarchies.
- Use Lombok only where it adds clarity and is approved.
- Document public APIs with Swagger/OpenAPI annotations.

### Anti-patterns

- Business logic in controllers or repositories.
- Static state or singletons outside Spring context.
- Exposing internal exceptions or stack traces to clients.
- Overusing reflection, dynamic proxies, or unchecked casts.
- Tightly coupled or circular dependencies.

### Review Checklist

- [ ] Are package and layer boundaries respected?
- [ ] Is constructor injection used everywhere?
- [ ] Are DTOs, validation, and mapping correct?
- [ ] Is transaction management and error handling proper?
- [ ] Is logging safe and configuration externalized?
- [ ] Are tests present and meaningful?

---

## Testing Standards

### Purpose

Ensure all important behaviour is verified, regressions are prevented, and releases are safe.

### Principles

- Follow the testing pyramid: more unit tests, fewer end-to-end.
- Tests must be deterministic, meaningful, and maintainable.
- Coverage is a means, not an end.

### Mandatory Standards

- Unit tests required for all business logic, with clear naming (e.g., `OrderServiceTest`).
- Integration tests required for data access, APIs, and critical workflows.
- Contract tests required for API and provider boundaries.
- Component tests for Angular components and services.
- End-to-end tests for major user journeys.
- Test names must describe behaviour, not implementation.
- Tests must be deterministic and independent.
- Use mocking only for external dependencies.
- CI must run all tests and block on failure.
- Coverage must be monitored; gaps in critical logic must be justified.

### Recommended Practices

- Use parameterized tests for variations.
- Use test data builders or fixtures for complex objects.
- Use testcontainers for integration with databases.
- Use accessibility and performance tests in CI.

### Anti-patterns

- Flaky, non-deterministic, or timing-dependent tests.
- Over-mocking or mocking internal logic.
- Asserting implementation details rather than behaviour.
- Ignoring test failures or disabling tests.

### Review Checklist

- [ ] Are all required test levels present?
- [ ] Are tests deterministic and meaningful?
- [ ] Are naming and coverage appropriate?
- [ ] Are mocks used only for external dependencies?
- [ ] Are tests run and passing in CI?

---

## Security Standards

### Purpose

Ensure the platform is secure by default, protects customer and business data, and meets regulatory and contractual obligations.

### Principles

- Secure-by-design: security is built in, not bolted on.
- Least privilege and defense in depth.
- Privacy and compliance are non-negotiable.

### Mandatory Standards

- All APIs and admin interfaces require authentication.
- RBAC: Enforce roles and permissions at all boundaries.
- Secrets: Use environment variables or secure vaults; never commit secrets.
- Encryption: Encrypt sensitive data at rest and in transit.
- Input validation: Validate and sanitize all external input.
- Output encoding: Prevent XSS and injection attacks.
- Dependencies: Monitor and patch vulnerabilities (OWASP Dependency-Check).
- Logging: Never log passwords, tokens, payment data, or PII unnecessarily.
- Privacy: Follow GDPR, CCPA, and other applicable regulations.
- Security review required for all new features and external integrations.

### Recommended Practices

- Use security headers (CSP, HSTS, etc.) in all responses.
- Use short-lived JWTs or session tokens.
- Regularly run SAST/DAST tools in CI.
- Use automated secrets scanning.
- Document threat models for major features.

### Anti-patterns

- Hard-coded secrets or credentials in code or config.
- Relying on client-side authorization.
- Disabling security checks for expediency.
- Ignoring known vulnerabilities in dependencies.
- Overbroad permissions or default admin access.

### Review Checklist

- [ ] Is authentication and RBAC enforced?
- [ ] Are secrets managed securely?
- [ ] Is input validated and output encoded?
- [ ] Are dependencies free of known vulnerabilities?
- [ ] Is logging restricted and privacy maintained?
- [ ] Has a security review been completed?

---

## Accessibility Standards

### Purpose

Ensure the platform is usable by people of all abilities, meeting WCAG 2.2 AA at minimum.

### Principles

- Accessibility is a requirement, not a feature.
- All users must be able to complete critical tasks.

### Mandatory Standards

- Use semantic HTML elements for all content and controls.
- All interactive elements must be accessible by keyboard.
- Manage focus for modals, dialogs, and navigation changes.
- Color contrast must meet WCAG 2.2 AA (minimum 4.5:1 for text).
- All images must have meaningful `alt` text.
- Forms must have labels, error messages, and ARIA attributes as needed.
- Support reduced motion preferences.
- Responsive layouts must not break accessibility.
- Accessibility acceptance criteria must be included in all UI work.

### Recommended Practices

- Test with screen readers (NVDA, VoiceOver).
- Use automated accessibility tools (axe, Lighthouse) in CI.
- Provide skip-to-content and landmark navigation.
- Document accessibility considerations in specifications.

### Anti-patterns

- Using divs/spans for buttons or links.
- Relying solely on color to convey information.
- Unlabeled form controls or icons.
- Keyboard traps or missing focus outlines.
- Animations that cannot be disabled.

### Review Checklist

- [ ] Are semantic HTML and ARIA used correctly?
- [ ] Is keyboard navigation and focus management correct?
- [ ] Is color contrast sufficient?
- [ ] Are forms and errors accessible?
- [ ] Are automated and manual accessibility checks passing?

---

## Performance Standards

### Purpose

Deliver a fast, responsive experience for all users and efficient operation for the business.

### Principles

- Performance is a feature; regressions are bugs.
- Optimize for Core Web Vitals and backend response times.

### Mandatory Standards

- Core Web Vitals: LCP < 2.5s, FID < 100ms, CLS < 0.1.
- Backend APIs must respond within 300ms p95 under normal load.
- Use HTTP caching for static assets and APIs where appropriate.
- Database: Use indexes, avoid N+1 queries, and batch operations where needed.
- Bundle optimization: Remove unused code, use tree-shaking, and lazy load modules.
- Images: Use modern formats (WebP/AVIF), responsive sizes, and lazy loading.
- Monitor performance in production and CI.
- Scalability: All new features must consider horizontal scaling.

### Recommended Practices

- Use CDN for static assets.
- Debounce or throttle expensive frontend operations.
- Use service workers for caching and offline support.
- Profile and optimize slow queries and endpoints.

### Anti-patterns

- Large, blocking JavaScript bundles.
- Synchronous/blocking backend operations.
- Unoptimized images or assets.
- Ignoring performance regressions in reviews.

### Review Checklist

- [ ] Are Core Web Vitals and backend response targets met?
- [ ] Is caching implemented where appropriate?
- [ ] Are bundle and image sizes optimized?
- [ ] Are database queries performant?
- [ ] Is monitoring in place and reviewed?

---

## Observability Standards

### Purpose

Enable effective monitoring, troubleshooting, and auditing of the platform in production.

### Principles

- Observability is required for all critical flows.
- Logs, metrics, and traces must be structured and actionable.

### Mandatory Standards

- Structured logging: Use JSON or key-value logs for all services.
- Metrics: Expose Prometheus-compatible metrics for APIs, jobs, and infrastructure.
- Tracing: Instrument distributed traces for all major workflows (OpenTelemetry).
- Correlation ID: Propagate through all requests, logs, and traces.
- Audit logging: Record all security-sensitive and business-critical actions.
- Dashboards: Maintain dashboards for health, errors, performance, and business KPIs.
- Alerts: Set up actionable alerts for errors, downtime, and SLO breaches.
- Health endpoints: All services must expose health and readiness endpoints.
- SLOs: Define and monitor Service Level Objectives for critical APIs.
- Incident support: Retain logs and traces for postmortem analysis.

### Recommended Practices

- Use log sampling for high-volume flows.
- Document observability for new features.
- Use synthetic monitoring for major user journeys.
- Regularly review alert noise and dashboard relevance.

### Anti-patterns

- Unstructured, free-text logs.
- Logging sensitive data or PII.
- Alert fatigue from unactionable or noisy alerts.
- Missing correlation between user, request, and system events.

### Review Checklist

- [ ] Are structured logs, metrics, and traces implemented?
- [ ] Is correlation ID propagated everywhere?
- [ ] Are dashboards and alerts in place for new/changed flows?
- [ ] Is audit logging present for sensitive actions?
- [ ] Are health endpoints and SLOs defined and monitored?

## AI Behaviour Rules

Specify behavioral rules and constraints for AI agents.

## Forbidden Practices

List practices and patterns that are prohibited in this repository.

## Preferred Practices

Highlight recommended and encouraged patterns and practices.

## Quality Gates

Describe automated and manual checks required before merging.

## Feature Development Lifecycle

Summarize the end-to-end process for delivering new features.

## Bug Fix Workflow

Describe the process for triaging, fixing, and verifying bugs.

## Refactoring Guidelines

Outline standards and best practices for refactoring code.

## Domain Ownership

Define responsibility and boundaries for different business domains.

## Glossary Reference

Provide definitions for key terms and acronyms used in this repository.

## Revision History

| Version | Date       | Status | Summary                                                                                                                                                                |
| ------- | ---------- | ------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 0.1.0   | 2026-08-05 | Draft  | Established document authority, vision, engineering philosophy, decision hierarchy, contributor responsibilities, Definition of Done, and AI self-review requirements. |
| 0.2.0   | 2026-08-05 | Draft  | Defined repository ownership and file-placement rules, the standard development workflow, branch strategy, commit conventions, and pull-request requirements.          |
| 0.3.0   | 2026-08-05 | Draft  | Established engineering standards for documentation, APIs, databases, frontend, backend, testing, security, accessibility, performance, and observability.             |
