---
title: AGENTS
version: 0.1.0
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

## Repository Structure

Outline the organization of code, documentation, and supporting assets.

## Development Workflow

Summarize the process for feature development, review, and deployment.

## Branch Strategy

Explain the branching model and policies for main, feature, and release branches.

## Commit Standards

Define the required format, content, and practices for commit messages.

## Pull Request Standards

Describe expectations for pull request content, review, and approval.

## Documentation Standards

Specify requirements for code, API, and user documentation.

## API Standards

Detail conventions and requirements for designing and documenting APIs.

## Database Standards

Outline best practices and requirements for database schema, migrations, and access.

## Angular Standards

Summarize guidelines specific to Angular development in this repository.

## Java Standards

Summarize guidelines specific to Java development in this repository.

## Testing Standards

Describe required testing approaches, coverage, and tools.

## Security Standards

Summarize mandatory security practices and requirements.

## Accessibility Standards

Outline accessibility requirements and evaluation practices.

## Performance Standards

Describe performance expectations and optimization guidelines.

## Observability Standards

Define required logging, monitoring, and tracing practices.

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
