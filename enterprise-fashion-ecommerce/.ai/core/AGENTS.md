---
title: AGENTS
version: 1.1.0
status: Approved
owner: Engineering
last_updated: 2026-08-12
applies_to:
  - Codex
  - Kiro
  - GitHub Copilot
  - Claude Code
  - Human contributors
review_cycle: Monthly
source_of_truth: true
---

> **Release Status:** Version 1.0.0 established the foundational engineering constitution for this repository. This document remains the authoritative governance source for all AI agents and human contributors. Future revisions must remain backward compatible unless a breaking governance change is approved through repository governance, recorded in the applicable Decision Record, and applied to this document.

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
3. Determine whether an existing applicable Decision Record, including an ADR for an Architecture Decision, resolves it.
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
- Material Decisions are captured in the applicable Decision Record; Architecture Decisions use ADRs.
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
- `.ai/backend/` is the approved location for backend-specific standards for Java, Spring, APIs, events, databases, PostgreSQL, and Azure integrations.
- `.ai/frontend/` is the approved location for frontend-specific standards for Angular, UI, accessibility, performance, and Storybook.
- `.ai/prompts/` is the approved location for reusable task templates. Prompts must reference source-of-truth documents instead of duplicating their content.
- `.ai/agents/` is the approved location for tool-specific integration guidance. Tool-specific files must not override core engineering standards.

The existence of a path or placeholder file in these directories does not establish substantive, completed, or Approved guidance. Contributors must verify that a lower-level file contains current governed content before relying on it.

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
test(inventory): cover concurrent Stock Reservations
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
- Record a material Decision in the applicable Decision Record, using an ADR only for an Architecture Decision.

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

## 14. Documentation Standards

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
- Diagrams must use approved source-controlled formats such as Mermaid, PlantUML, or Draw.io, with rendered output committed only when it materially improves review or distribution.
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

## 15. API Standards

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

## 16. Database Standards

### Purpose

Ensure data integrity, consistency, scalability, and maintainability across database schemas and migrations.

### Principles

- Schema is a contract: changes must be controlled and backward compatible.
- Data integrity and normalization are default.
- Schema must be observable and auditable.

### Mandatory Standards

- Business entity primary keys must use UUIDs unless an Accepted Architecture Decision explicitly establishes another strategy; UUID generation strategy must be defined in the database standards and used consistently.
- Table names must be plural, snake_case (e.g., `order_items`).
- Column names must be snake_case, descriptive, and avoid reserved words.
- Normalize data to at least 3NF unless denormalization is justified.
- Indexes must be defined for all foreign keys, unique constraints, and common queries.
- All schema changes must use versioned Flyway migrations, as established by `ARCHITECTURE.md`.
- Foreign keys must enforce referential integrity.
- Soft deletion must be used selectively for recoverable mutable records; durable business facts such as orders, payments, inventory movements, and audit records must use lifecycle status or append-only history instead of ordinary deletion.
- Auditing: Use `created_at`, `updated_at`, `created_by`, `updated_by` fields.
- Optimistic locking: Use a version column for concurrent updates.
- Database Transactions: Define clear Database Transaction boundaries; avoid long-running Database Transactions.
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

## 17. Angular Standards

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

## 18. Java Standards

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
- Database Transaction management: Use `@Transactional` at the service layer, not the controller.
- Exception handling: Use `@ControllerAdvice` for API error responses.
- Logging: Use SLF4J; never log sensitive data.
- Configuration: Use `application.yaml` or `application.properties` with profiles.
- Testing: Unit, integration, and contract tests are required for all services and APIs.
- Dependency management: Use BOM and avoid unnecessary dependencies.

### Recommended Practices

- Use records for immutable DTOs under the repository's Java 21 LTS baseline.
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
- [ ] Is Database Transaction management and error handling proper?
- [ ] Is logging safe and configuration externalized?
- [ ] Are tests present and meaningful?

---

## 19. Testing Standards

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
- Use Testcontainers for integration with databases and external infrastructure dependencies where practical.
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

## 20. Security Standards

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
- Dependencies: Monitor and patch vulnerabilities using approved SCA/dependency scanning.
- Logging: Never log passwords, tokens, payment data, or PII unnecessarily.
- Privacy: Follow GDPR, CCPA, and other applicable regulations.
- Security review required for all new features and external integrations.

### Recommended Practices

- Use security headers (CSP, HSTS, etc.) in all responses.
- Use bounded token or Session lifetimes under the approved Identity and security architecture.
- Run SAST and applicable source, dependency, and static security checks as CI quality gates under `SECURITY-STANDARDS.md` and `TESTING-STANDARDS.md`.
- Perform DAST against a suitable deployed Environment on the cadence and at the depth governed by those standards. CI/CD may orchestrate DAST against that Environment; this does not imply that every pull request runs full DAST, and DAST must not be treated as penetration testing.
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

## 21. Accessibility Standards

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

## 22. Performance Standards

### Purpose

Deliver a fast, responsive experience for all users and efficient operation for the business.

### Principles

- Performance is a feature; regressions are bugs.
- Optimize for Core Web Vitals and backend response times.

### Mandatory Standards

- Core Web Vitals: LCP < 2.5s, INP < 200ms, CLS < 0.1.
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

## 23. Observability Standards

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

## 24. AI Behaviour Rules

### 24.1 Purpose

AI agents must behave as accountable engineering contributors rather than unreviewed text generators. They must use the repository context, reason within approved constraints, disclose uncertainty, verify their work, and preserve the integrity of the codebase.

### 24.2 Context acquisition

Before proposing or making changes, an AI agent must:

1. Read this constitution.
2. Read the most relevant files under `.ai/core/`.
3. Read the applicable technology-specific files under `.ai/backend/` and `.ai/frontend/`.
4. Read the relevant business and domain specifications.
5. Inspect the current implementation, tests, migrations, contracts, and related documentation.
6. Identify the affected domain owners and architectural boundaries.
7. Identify any assumptions, unresolved decisions, or contradictions.

The agent must not rely only on the immediate user request when the repository contains authoritative context that materially affects the work.

### 24.3 Requirement discipline

AI agents must distinguish between:

- Explicit requirements.
- Approved standards and decisions.
- Existing implementation behaviour.
- Reasonable implementation assumptions.
- Suggestions or possible enhancements.

An agent must never present an assumption, convention, or preferred design as an approved business requirement.

When a requirement is incomplete, the agent must proceed only when a safe and reversible assumption can be made without materially affecting business behaviour, security, data integrity, architecture, compliance, cost, or customer experience. Such assumptions must be stated clearly.

When ambiguity materially affects those areas, the agent must stop and request a decision.

### 24.4 Planning before modification

For non-trivial work, the agent must form a concise implementation plan before editing. The plan must identify:

- Files to inspect.
- Files likely to change.
- Requirements and standards being applied.
- Data, API, event, UI, security, testing, and operational impacts.
- Verification steps.

The plan must remain proportional to the task. It must not become a substitute for implementation.

### 24.5 Repository inspection

Before creating a new pattern, abstraction, component, service, endpoint, schema object, test fixture, or utility, the agent must search for an existing equivalent.

The agent must prefer:

1. Reusing an approved existing pattern.
2. Extending an existing abstraction when ownership remains clear.
3. Creating a new implementation only when the existing options do not satisfy the requirement.

The agent must not infer that a pattern is approved merely because it exists. Existing code must still be checked against current standards.

### 24.6 Scope control

AI agents must keep changes focused on the requested outcome.

They must not:

- Rewrite unrelated files.
- Perform broad formatting changes without approval.
- Upgrade dependencies opportunistically.
- Rename public contracts without necessity.
- Introduce additional features because they appear useful.
- Move files solely to satisfy personal preference.

Necessary supporting changes may be included when they are directly required for correctness and are explicitly described.

### 24.7 Safe editing

When modifying existing files, the agent must:

- Preserve unrelated behaviour.
- Preserve local style when it remains compliant.
- Avoid destructive replacements when targeted edits are possible.
- Review the final diff.
- Verify imports, references, paths, and generated artifacts.
- Avoid deleting data, migrations, tests, or documentation unless the removal is intentional and justified.

### 24.8 Truthfulness and verification

An AI agent must never claim that:

- Tests passed when they were not run.
- A build succeeded when it was not executed.
- A file was modified when no change was applied.
- A provider supports behaviour that was not verified.
- Documentation is complete when placeholders remain.
- A security, accessibility, or performance review occurred when it did not.

The agent must distinguish clearly between:

- Verified results.
- Static inspection.
- Reasoned inference.
- Proposed work.
- Known limitations.

### 24.9 Failure handling

When a command, test, build, migration, tool, or edit fails, the agent must:

1. Report the failure accurately.
2. Inspect the error before retrying.
3. Avoid repeated blind retries.
4. Correct the underlying issue where possible.
5. Preserve evidence needed for diagnosis.
6. State what remains unverified.

The agent must not hide a failure by disabling checks, weakening assertions, swallowing exceptions, or removing the affected test.

### 24.10 Generated content

Generated code, documentation, tests, SQL, OpenAPI, configuration, diagrams, and infrastructure definitions are subject to the same standards as human-authored work.

Before presenting generated output, the agent must check:

- Internal consistency.
- Compatibility with existing contracts.
- Correct file placement.
- Naming conventions.
- Security and privacy risks.
- Missing negative paths.
- Placeholder or fabricated content.
- Unsupported dependencies or commands.

### 24.11 Communication

AI responses must be direct, accurate, and proportionate to the task.

They must:

- State completed work clearly.
- State unresolved risks or decisions.
- Avoid unnecessary repetition.
- Avoid overstating quality or completeness.
- Use repository terminology from `GLOSSARY.md`.
- Include exact commands when user action is required.

### 24.12 Tool-specific guidance

Files under `.ai/agents/` may define how a specific tool discovers context or applies changes. Tool-specific guidance must not weaken or override this constitution.

If a tool limitation prevents compliance, the agent must disclose the limitation and choose the safest available workflow.

## 25. Forbidden Practices

The following practices are prohibited. A departure is permitted only when the governing Canonical Document explicitly allows it through the applicable Decision or formal Exception process. An ADR applies only to Architecture Decisions and cannot waive a mandatory requirement owned by another canonical standard.

### 25.1 Requirements and design

- Inventing business rules or acceptance criteria.
- Treating assumptions as approved requirements.
- Implementing materially ambiguous behaviour without escalation.
- Bypassing domain specifications.
- Introducing a major architecture pattern without an ADR.
- Designing for hypothetical scale at the expense of current clarity and delivery.

### 25.2 Code and architecture

- Business logic in controllers, UI components, repositories, mappers, or infrastructure adapters.
- Circular dependencies between modules.
- Shared “utility” modules that become uncontrolled dependency hubs.
- Copying business logic across frontend and backend.
- Hidden global mutable state.
- Unbounded retries, loops, queues, or caches.
- Silent exception handling.
- Catching generic exceptions without a defined recovery or translation strategy.
- Reflection-heavy or framework-specific magic without clear benefit.
- Premature microservice extraction.
- Direct cross-domain database access that bypasses domain ownership.

### 25.3 API and integration

- Returning persistence entities directly from APIs.
- Exposing stack traces or internal exception messages.
- Breaking public contracts without versioning and migration guidance.
- Trusting client-provided Price, Permission, Payment state, or Inventory state.
- Treating a Payment Redirect or client-reported Payment success as authoritative Payment proof.
- Changing provider-dependent authoritative Payment state without validated Payment Provider evidence.
- Processing duplicate-sensitive operations without idempotency controls.
- Calling external services without timeouts.
- Retrying non-idempotent operations blindly.
- Hard-coding provider-specific behaviour in core domain logic.

### 25.4 Database and data

- Manual production schema changes outside approved migrations.
- Editing an already-applied migration except through an approved repair procedure.
- Relying only on application validation for essential integrity rules.
- Using floating-point types for money.
- Storing raw payment-card data.
- Deleting financial, audit, or order history through ordinary CRUD operations.
- Logging or exposing unnecessary personal information.
- Creating indexes without considering write and storage cost.
- Large destructive data migrations without backup, rehearsal, and rollback planning.

### 25.5 Frontend

- Treating route guards or hidden controls as authorization enforcement.
- Storing secrets in browser code or frontend environment files.
- Persisting long-lived sensitive tokens in unsafe browser storage.
- Using non-semantic elements as interactive controls without correct behaviour.
- Removing visible focus indicators.
- Direct DOM manipulation when Angular mechanisms are sufficient.
- Subscribing without lifecycle management.
- Duplicating server-owned validation or pricing logic as authoritative frontend logic.
- Blocking rendering with avoidable synchronous work.

### 25.6 Testing and quality

- Disabling tests to make CI pass.
- Reducing assertions without resolving the defect.
- Mocking the unit under test.
- Tests that depend on execution order, wall-clock timing, public internet access, or shared mutable state.
- Snapshot tests used as a substitute for behavioural assertions.
- Inflating coverage with meaningless tests.
- Claiming release readiness with failing or unexecuted checks.

### 25.7 Security and operations

- Committing secrets, credentials, private keys, tokens, or production data.
- Logging passwords, session tokens, raw payment data, or sensitive identity documents.
- Disabling TLS, authorization, CSRF protection, or security headers for convenience.
- Granting wildcard cloud or database permissions without documented necessity.
- Running CI workflows with broader permissions than required.
- Ignoring known critical vulnerabilities without an accepted risk decision.
- Alerts without owners or actionable response guidance.
- Production changes without rollback or recovery consideration.

### 25.8 Documentation and AI output

- Presenting placeholders as complete documentation.
- Duplicating source-of-truth standards.
- Fabricating commands, APIs, libraries, provider capabilities, or test results.
- Creating large numbers of shallow files to imply completeness.
- Leaving unresolved `TODO`, `FIXME`, or sample values in production-ready work without tracking.
- Committing generated binaries or exports when reproducible source is sufficient.

## 26. Preferred Practices

Contributors should apply these practices unless a more specific approved standard requires otherwise.

### 26.1 Design and architecture

- Prefer explicit domain boundaries and named application use cases.
- Prefer modular monolith patterns for the initial platform.
- Prefer composition over inheritance.
- Prefer immutable value objects and DTOs.
- Prefer ports and adapters for external providers.
- Prefer small, cohesive modules with clear ownership.
- Prefer synchronous behaviour for simple workflows and asynchronous behaviour only where it provides a clear operational or scalability benefit.
- Prefer reversible decisions while requirements are still evolving.

### 26.2 Code

- Prefer descriptive names over comments that restate code.
- Prefer early validation and clear failure messages.
- Prefer pure functions for deterministic transformations.
- Prefer constructor injection.
- Prefer centralised policy objects for complex business decisions.
- Prefer typed contracts over maps or loosely structured data.
- Prefer framework-supported lifecycle management.
- Prefer deletion of obsolete code over indefinite compatibility layers after deprecation is complete.

### 26.3 APIs and events

- Prefer coarse-grained use-case endpoints over chatty APIs.
- Prefer idempotent command handling.
- Prefer explicit workflow transitions.
- Prefer stable machine-readable error codes.
- Prefer additive contract evolution.
- Prefer event names in past tense that describe completed business facts.
- Prefer the Outbox Pattern when reliable event publication is required by the approved Architecture.

### 26.4 Data

- Prefer database constraints for invariant protection.
- Prefer append-only movement or history tables for inventory, payment, order, and audit changes.
- Prefer UTC timestamps and explicit currency codes.
- Prefer optimistic locking for normal concurrent edits.
- Prefer dedicated read models or projections for reporting rather than distorting transactional models.
- Prefer lifecycle status over soft deletion when the record represents a durable business fact.

### 26.5 Frontend and UX

- Prefer mobile-first responsive design.
- Prefer semantic HTML before ARIA.
- Prefer Signals for local synchronous UI state and RxJS for asynchronous streams.
- Prefer route-level lazy loading.
- Prefer reusable design-system components.
- Prefer skeletons for predictable content loading and progress indicators for operations with measurable progress.
- Prefer preserving user input after recoverable errors.
- Prefer clear confirmation for destructive or irreversible actions.

### 26.6 Testing

- Prefer tests that read like business behaviour.
- Prefer test data builders over large inline object construction.
- Prefer real databases through Testcontainers for persistence integration tests.
- Prefer contract tests for external provider adapters.
- Prefer a small number of high-value end-to-end journeys.
- Prefer architecture tests that protect module boundaries.

### 26.7 Delivery and operations

- Prefer automated, repeatable deployments.
- Prefer feature flags for risky incremental rollout.
- Prefer backward-compatible database expansion before application migration and later cleanup.
- Prefer dashboards that combine technical and business signals.
- Prefer alerts on customer or business impact rather than low-level noise.
- Prefer documented rollback and reconciliation procedures for payment, inventory, and order workflows.

## 27. Quality Gates

Quality gates are mandatory controls that determine whether a change may progress to review, merge, release, or production.

### 27.1 Specification gate

Before implementation begins:

- Requirements and acceptance criteria are testable.
- Domain ownership is identified.
- Material architecture decisions are approved.
- Security, privacy, accessibility, performance, and operational impacts are considered.
- Open decisions that block correctness are resolved.

### 27.2 Local development gate

Before a branch is pushed:

- Formatting passes.
- Linting and static analysis pass.
- Type checking passes.
- Affected unit and integration tests pass.
- The project builds successfully.
- Migrations validate where applicable.
- The final diff has been reviewed.
- No secrets or unintended generated files are present.

### 27.3 Pull-request gate

Before review approval:

- Required PR content is complete.
- Required automated checks pass.
- Tests meaningfully cover changed behaviour.
- Documentation and contracts are current.
- Reviewers can reproduce or understand the evidence.
- No unresolved blocking discussion remains.
- Security, data, accessibility, and operational concerns are addressed.

### 27.4 Merge gate

Before merge:

- Required approvals are present.
- Branch protection requirements pass.
- The target branch relationship is valid.
- Breaking changes are approved and versioned.
- Migrations are safe for the intended deployment sequence.
- Rollout and rollback guidance exists where required.

### 27.5 Release gate

Before a release is promoted:

- Release scope is known.
- Build artifacts are reproducible and traceable to source.
- Integration and end-to-end checks pass in the release environment.
- Critical security scans pass, or an approved, unexpired Security Exception explicitly covers each blocking finding and release context.
- Observability, dashboards, alerts, and runbooks are ready.
- Backup, restore, and rollback requirements are satisfied.
- Release notes and operational communication are prepared.

### 27.6 Production gate

Before production enablement:

- Environment configuration is validated.
- Secrets and identities are correctly scoped.
- Database migrations have been rehearsed for high-risk changes.
- Health and readiness checks pass.
- Smoke Tests pass.
- Monitoring is active.
- A responsible owner is available during the agreed observation period.

### 27.7 Gate exceptions

A quality gate may be bypassed only through every applicable formal Exception process established by an Approved Canonical Document. A generic risk acceptance may govern a gate only when no more specific Approved Exception mechanism owns the affected Requirement, and it must be explicit, time-bound, documented, auditable, risk-aware, owned, and approved by an authorized person. It must state:

- Which Requirement and gate are bypassed.
- Why the bypass is necessary.
- Business and technical risk.
- Compensating controls.
- Accountable owner and authorized approver.
- Approval date and expiry date.
- Remediation plan and tracking reference.

A mandatory security Requirement may be waived only through the applicable Security Exception governed by `SECURITY-STANDARDS.md`. A mandatory testing Requirement may be waived only through the applicable Testing Exception governed by `TESTING-STANDARDS.md`. A mandatory coding Requirement must use the applicable Coding Exception where governed by `CODING-STANDARDS.md`, and a mandatory documentation Requirement must use the applicable Documentation Exception where governed by `DOCUMENTATION-STANDARDS.md`.

One Exception type must not silently waive a Requirement owned by another Canonical Document. When multiple standards govern a gate, every applicable formal Exception must be satisfied. An Exception or generic risk acceptance does not automatically satisfy a Human Approval Gate unless the governing workflow explicitly says it does and the required authorized human approval is recorded.

AI agents may not grant or imply approval for an Exception, risk acceptance, or Human Approval Gate.

## 28. Feature Development Lifecycle

### 28.1 Discovery

Define the customer or operational problem, intended outcome, actors, value, constraints, assumptions, dependencies, and success measures.

### 28.2 Specification

Create or update the applicable business, domain, frontend, backend, data, integration, and infrastructure specifications. Requirements must be traceable and testable.

### 28.3 Design

Define:

- Domain model and ownership.
- Application use cases.
- API and event contracts.
- Database changes.
- UI states and accessibility behaviour.
- Integration and failure handling.
- Security and privacy controls.
- Observability and operational support.

Material Architecture Decisions require an ADR. Other material Decisions follow the Decision Record governance in `DECISIONS.md`.

### 28.4 Planning

Split work into independently reviewable increments. Identify migration order, feature flags, compatibility requirements, test strategy, and rollout dependencies.

### 28.5 Implementation

Implement the smallest complete vertical slice that satisfies the approved scope. Keep business logic at the correct boundary and maintain documentation alongside code.

### 28.6 Verification

Run applicable unit, integration, contract, component, end-to-end, accessibility, security, and performance checks. Verify negative paths and recovery behaviour.

### 28.7 Review

Review requirements, architecture, security, data integrity, user experience, performance, tests, operations, and documentation. Resolve findings before merge.

### 28.8 Release

Deploy through the approved pipeline. Use feature flags, staged rollout, or controlled activation where risk requires it. Validate health, telemetry, and core journeys.

### 28.9 Observation

Monitor technical and business signals after release. Confirm that the feature meets the expected outcome and that no material regression exists.

### 28.10 Completion

Close the feature only when follow-up tasks, documentation, metrics, and operational ownership are clear. Deferred enhancements must be tracked separately and must not be implied as delivered.

## 29. Bug Fix Workflow

### 29.1 Triage

Capture:

- Observed behaviour.
- Expected behaviour.
- Affected users, environments, and domains.
- Severity and business impact.
- Reproduction evidence.
- First known occurrence.
- Workaround, if any.

### 29.2 Severity

Use the following default severity model:

- **Critical:** security exposure, data loss, payment/order corruption, or widespread inability to purchase.
- **High:** major customer journey or operational workflow unavailable with no reasonable workaround.
- **Medium:** material defect with limited scope or a practical workaround.
- **Low:** minor behaviour, content, visual, or maintainability issue with low business impact.

### 29.3 Reproduction

Before changing code, reproduce the issue where practical and identify the smallest failing scenario. Preserve relevant logs, correlation IDs, payloads, screenshots, and environment details without exposing secrets or personal information.

### 29.4 Root cause

Fixes must address the root cause, not only the visible symptom. The investigation should determine:

- Why the system allowed the defect.
- Why existing tests or controls did not detect it.
- Whether similar paths are affected.
- Whether data reconciliation or remediation is required.

### 29.5 Regression test

Add a failing automated test before or alongside the fix where practical. The test must prove the reported behaviour and prevent recurrence.

### 29.6 Implementation and verification

Keep the fix focused. Run affected and neighbouring tests. Verify successful behaviour, negative paths, and any data or operational remediation.

### 29.7 Production defects

For production defects, also consider:

- Feature disablement or rollback.
- Customer communication.
- Data correction.
- Payment or inventory reconciliation.
- Alert improvements.
- Incident review or postmortem.

### 29.8 Closure

A bug is complete when the root cause is corrected, regression protection exists, affected documentation is updated, and any remediation or monitoring action is owned.

## 30. Refactoring Guidelines

### 30.1 Purpose

Refactoring improves internal structure without intentionally changing externally observable behaviour.

### 30.2 Preconditions

Before refactoring:

- The current behaviour is understood.
- Relevant tests exist or are added.
- The refactoring objective is explicit.
- Affected public contracts and domain boundaries are identified.
- The expected benefit justifies the change.

### 30.3 Refactoring scope

Refactors should be incremental and reviewable. Separate behaviour changes from structural changes where practical.

### 30.4 Appropriate reasons

Valid reasons include:

- Reducing duplication.
- Restoring domain boundaries.
- Improving readability or testability.
- Removing obsolete code.
- Reducing coupling.
- Improving performance based on evidence.
- Preparing a safe, approved feature change.

“Personal preference” is not sufficient justification for broad refactoring.

### 30.5 Behaviour preservation

Existing public behaviour must remain unchanged unless the change is explicitly reclassified and reviewed as a feature or bug fix.

### 30.6 Data and API refactoring

Schema and contract refactors require compatibility planning. Use expand-migrate-contract sequencing where appropriate:

1. Add compatible structures.
2. Migrate producers and consumers.
3. Backfill or transform data.
4. Observe stability.
5. Remove deprecated structures in a later change.

### 30.7 Verification

Refactoring must pass the same or stronger tests as the original implementation. Architecture, performance, and maintainability improvements should be demonstrated where measurable.

### 30.8 Prohibited refactoring behaviour

- Large unreviewable rewrites without a migration strategy.
- Removing tests because the design changed.
- Combining unrelated cleanup.
- Introducing a new framework or dependency without approval.
- Rewriting working modules only to match agent preference.

## 31. Domain Ownership

Domain ownership determines where business rules, data, APIs, events, and operational responsibilities belong.

### 31.1 Product and Catalogue

Owns:

- Product definitions.
- Product Variants.
- Categories and collections.
- Product attributes.
- Catalogue publication state.
- Product media metadata.

Does not own authoritative inventory balance, customer-specific carts, completed order snapshots, or payment state.

### 31.2 Pricing and Promotions

Owns:

- Base and sale prices.
- Promotion definitions.
- Voucher eligibility.
- Discount calculation policy.
- Promotion usage constraints.

Orders retain immutable pricing snapshots after confirmation.

### 31.3 Inventory

Owns:

- Stock.
- Available-to-Sell quantity.
- Stock Reservations.
- Stock Adjustments and Stock Movements.
- Low-stock policy.

Inventory owns authoritative Stock and Available-to-Sell state. Stock Reservation and Stock changes must follow approved concurrency and overselling protections. Catalogue, client, search, reporting, and projection state may display or derive availability but must not become authoritative Inventory truth.

### 31.4 Customer and Identity

Identity owns credentials, authentication sessions, roles, and permissions.

Customer owns profiles, addresses, preferences, and consent records.

Order delivery addresses are immutable snapshots and are not changed when a customer edits a saved address.

### 31.5 Cart and Checkout

Cart owns active purchase intent and cart items.

Checkout coordinates validation of customer details, prices, promotions, inventory, shipping, and payment initiation. It does not become the source of truth for payment or completed orders.

### 31.6 Payments

Owns:

- Payment attempts.
- Provider references.
- Payment status.
- Callback validation.
- Refund records.
- Reconciliation.

Payments must not directly own product, inventory, or fulfilment rules.

Provider-dependent authoritative Payment state requires validated Payment Provider evidence. A Payment Redirect or client-reported Payment success is not authoritative proof, and retries must not create duplicate financial effects.

### 31.7 Orders

Owns:

- Confirmed commercial order records.
- Order item snapshots.
- Totals and currency snapshots.
- Order lifecycle.
- Status history.
- Cancellation policy coordination.

Orders must not rely on mutable catalogue records to represent historical purchases.

### 31.8 Shipping and Fulfilment

Owns:

- Shipping methods.
- Delivery quotations.
- Shipment records.
- Tracking references.
- Shipment lifecycle.
- Delivery events.

Shipping providers are external adapters behind project-owned contracts.

### 31.9 Notifications

Owns notification requests, templates, delivery attempts, retry state, and delivery outcome.

Notifications consume approved business events and must not mutate authoritative order, payment, or inventory state.

### 31.10 Content Management

Owns editorial pages, homepage content, banners, campaign content, and policy presentation content.

It must not own product pricing, stock, or order data.

### 31.11 Administration

Administration provides protected workflows over domain-owned application services. It must not bypass domain rules or write directly to another domain’s persistence layer.

### 31.12 Reporting

Reporting owns read models, exports, dashboards, and analytical projections. It does not own transactional state and must not mutate operational aggregates.

### 31.13 Cross-domain interaction

Domains should interact through:

- Application-service interfaces.
- Explicit queries.
- Approved APIs.
- Domain or integration events.

Direct access to another domain’s internal repository, entity, or table is prohibited unless an accepted ADR defines a controlled exception.

### 31.14 Ownership conflicts

When ownership is unclear:

1. Identify the business invariant.
2. Determine which domain is accountable for its correctness.
3. Check `ARCHITECTURE.md` and existing ADRs.
4. Record the decision if it is material.
5. Avoid shared ownership of mutable authoritative state.

## 32. Glossary Reference

The canonical project glossary is `.ai/core/GLOSSARY.md`.

Contributors must use canonical terms consistently in requirements, code, APIs, events, database objects, tests, user interfaces, logs, and documentation.

When introducing a new business or engineering term:

1. Confirm that an equivalent term does not already exist.
2. Define the term in `GLOSSARY.md`.
3. Identify ambiguous or prohibited synonyms where useful.
4. Use the term consistently in the same change.

Terms with materially different meanings must not be used interchangeably. In particular:

- **Product** and **Product Variant** are distinct.
- **Cart**, **checkout**, and **order** represent different lifecycle stages.
- **Payment**, **Payment Attempt**, and **Refund** are distinct records.
- **Stock**, **Stock Reservation**, and **Available-to-Sell** are distinct Inventory concepts.
- **Category** and **collection** are distinct merchandising concepts.
- **Authentication** and **Authorization** are distinct controls.
- **Audit log**, **application log**, **metric**, and **trace** are distinct observability artifacts.

When a provider uses terminology that conflicts with the internal model, the provider term must be translated within the adapter and must not redefine the project’s domain language.

## 33. Governance

### 33.1 Ownership

The Engineering function owns this document. Significant changes require architectural review and must be reflected in the corresponding standards or Architecture Decision Records where applicable.

### 33.2 Review Cycle

This constitution must be reviewed at least monthly and additionally after:

- Major architectural changes.
- Introduction of a new AI coding agent.
- Adoption of new engineering standards.
- Significant security or production incidents.
- Major product or platform evolution.

### 33.3 Backward Compatibility

Contributors should evolve this document incrementally. Existing repository standards must not be invalidated without a documented migration strategy.

### 33.4 Source of Truth

Where guidance conflicts, this document governs contributor behaviour. Technology-specific documents may extend but must not weaken or contradict this constitution.

## Revision History

| Version | Date       | Status   | Summary                                                                                                                                                                                          |
| ------- | ---------- | -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 0.1.0   | 2026-08-05 | Draft    | Established document authority, vision, engineering philosophy, decision hierarchy, contributor responsibilities, Definition of Done, and AI self-review requirements.                           |
| 0.2.0   | 2026-08-05 | Draft    | Defined repository ownership and file-placement rules, the standard development workflow, branch strategy, commit conventions, and pull-request requirements.                                    |
| 0.3.0   | 2026-08-05 | Draft    | Established engineering standards for documentation, APIs, databases, frontend, backend, testing, security, accessibility, performance, and observability.                                       |
| 0.4.0   | 2026-08-05 | Draft    | Defined AI behaviour, prohibited and preferred practices, quality gates, feature and defect lifecycles, refactoring rules, domain ownership, and glossary governance.                            |
| 1.0.0   | 2026-08-05 | Approved | Completed the foundational AI Engineering Constitution, including governance, engineering philosophy, repository standards, delivery lifecycle, quality gates, and contributor responsibilities. |
| 1.1.0   | 2026-08-12 | Approved | Clarified formal Exception precedence and DAST execution, aligned technology examples with Architecture, normalized Product Variant, Stock Reservation, Database Transaction, Smoke Test, and Outbox Pattern terminology, and corrected Decision Record scope after the core consistency audit. |
