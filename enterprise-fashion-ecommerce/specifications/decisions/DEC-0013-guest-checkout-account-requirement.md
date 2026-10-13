# DEC-0013 — Guest Checkout and Account Requirements

- **Identifier:** DEC-0013
- **Title:** Guest Checkout and Account Requirements
- **Type:** Product Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-10-13
- **Last Updated:** 2026-10-13
- **Owner:** Product
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

[`PRODUCT.md` §24 item 4](../../.ai/core/PRODUCT.md) leaves guest Checkout versus a mandatory Account open. Its Visitor description permits beginning guest Checkout **where enabled**, while its Registered Customer capabilities preserve an Account path; neither settles purchase eligibility. The [Approved Checkout Domain Specification](../domains/checkout/checkout-domain.md) distinguishes policy-permitted authenticated Customer and unauthenticated Visitor contexts without deciding which may complete Checkout. The [Approved Customer Domain Specification](../domains/customer/customer-domain.md) does not make Account creation a Checkout precondition. These are existing boundaries, not an accepted guest-purchase policy.

The Product sponsor has explicitly directed support for both guest and registered-customer Checkout, without mandatory Account creation for purchase; guest Registration remains optional. This is attributable **Product direction**, not an independently reviewed or Accepted decision. The repository supplies ownership, security and failure constraints, but has not established guest-order identification, access verification, post-purchase association, contact handling or a production guest Checkout Contract. Product, Checkout, Customer, Identity, Order, Payment, Security, Privacy and affected owners must review applicable consequences before acceptance.

## Decision Question

Should initial purchase policy allow both an unauthenticated Visitor Checkout path and an authenticated Registered Customer Checkout path without requiring Account creation for purchase, and what separately governed conditions are necessary to protect each path and its later Order access?

## Decision — Proposed Product Direction Only

**Option C is Product-preferred, not Accepted.** The Product sponsor prefers both guest and registered-customer Checkout, no mandatory Account creation to complete a purchase, optional guest Registration, and preservation of Registered Customer Account capabilities. This record captures that direction for review. It does not close `PRODUCT.md` §24 item 4, enable either production path, or authorize implementation. Comparative evaluation, affected-owner and Security/Privacy review, and formal Product acceptance have not occurred.

Any eventual policy must distinguish a Visitor purchase context from a registered Customer's governed Identity, Customer and Account context. A guest purchase must not be represented as completed Registration, Authentication, an Account, or protected Account access. Optional later Registration must follow its own Approved authority and cannot retroactively establish Order ownership or silently link a guest Order to an Account.

## Alternatives Considered — Not Formally Compared

| Option | Policy-level alternative | Potential benefit | Unresolved cost or question |
| --- | --- | --- | --- |
| A | Require an Account before Checkout completion. | A registered context may simplify some Account-based journeys. | Creates a purchase prerequisite contrary to the confirmed Product preference; registration and verification dependencies remain unresolved. No comparative finding selects or formally rejects it. |
| B | Allow guest Checkout without a registered-customer Checkout path. | Limits initial actor contexts. | Does not preserve the requested Registered Customer purchase path or its Account capabilities. No comparative finding selects or formally rejects it. |
| C | Support both guest and registered-customer Checkout without mandatory Account creation. | Matches the Product sponsor's stated preference while retaining Account capabilities. | Guest-order access, privacy, support, linking, Contract, abuse and operational consequences require evidence and owner review. Preferred for later formal evaluation only. |

No alternative has been formally compared, Accepted, or authorized for implementation. The preference is not evidence that another alternative is technically infeasible.

## Scope and Authority Boundaries

| Concern | Existing authority preserved; no mechanism selected |
| --- | --- |
| Product policy | Product owns the guest-versus-Account requirement. This Proposed record captures preference but cannot update canonical `PRODUCT.md` until Accepted and synchronized. |
| Identity | Identity owns Identity, Authentication, credential, Principal and Session security authority. It does not own Customer or Account business truth. Guest Checkout does not establish Authentication or resolve Registration, current-email verification, DEC-0009, DEC-0010 or DEC-0011. |
| Customer and Account | Customer owns Customer profiles, Preferences, Account business truth and governed associations. A Visitor or guest Order is not automatically a registered Customer profile or Account. |
| Checkout | Checkout owns purchase orchestration and must distinguish only policy-permitted Visitor and authenticated Customer contexts, validate current inputs and coordinate owning-Domain outcomes. |
| Order | Order owns creation, association, history and access decisions for Order records. A supplied Order Number, contact address or later Account assertion is not ownership proof. |
| Payment | Payment owns Payment Attempts, Transactions and authoritative Payment outcomes; Checkout or browser state cannot manufacture Payment success. This proposal changes no Payment policy. |
| Security and Privacy | Guest-order access needs governed identification and verification, current server-side contextual Authorization, customer-data isolation, data minimization, abuse resistance and auditable outcomes. No guest verification or Session mechanism is chosen. |

The [Approved Checkout](../domains/checkout/checkout-domain.md), [Customer](../domains/customer/customer-domain.md), [Identity](../domains/identity/identity-domain.md), [Order](../domains/order/order-domain.md) and [Payment](../domains/payment/payment-domain.md) Domain Specifications, their applicable Approved backend specializations and existing Contracts retain their authority. None is amended by this Proposed record.

## Evidence and Review Required Before Acceptance

- **Product and Customer:** confirm the intended guest and Registered Customer journeys, optional Registration wording, Account-feature boundaries, post-purchase support expectations and acceptance/denial cases without inventing a guest profile or automatic Account linkage.
- **Checkout and Order:** review Visitor and Customer purchase contexts, authoritative Order association, guest tracking and later Order access, duplicate and uncertain outcomes, and the boundary between a purchase request and confirmed Order creation.
- **Identity and Security:** review protected guest-order access, abuse and enumeration risks, object-level Authorization, credential and verification non-authority, and positive and negative cases. Do not reuse the unresolved DEC-0009, DEC-0010 or DEC-0011 paths as guest proof.
- **Privacy/Legal and affected owners:** review guest contact information, purpose, minimization, retention and disclosure; support, cancellation, Return and Refund access where separately eligible; and whether any proposed later association needs separate Product, Security, Domain or Contract governance.
- **Architecture and Contracts:** identify any material Architecture decision or affected executable Contract only after the policy and authority boundaries are reviewed. No route, payload, token, schema, provider or deployment approach is inferred here.

An acceptance review must distinguish binding repository requirements from this Product preference, record applicable owner and Security/Privacy conclusions, and leave unsupported mechanics unresolved. Passing review would still require a governed Accepted revision and canonical Product synchronization; it would not itself complete implementation prerequisites.

## Open Questions

1. What authoritative context identifies a guest Order, and how would a later requester prove entitlement to view or act on that exact Order without exposing its existence to others?
2. Which guest contact information is necessary for purchase, notification, tracking and support, and what purposes, minimization and privacy controls govern it?
3. How may a guest track an Order or obtain support, and what positive, denied, ambiguous, stale and unavailable authorization outcomes must be distinguished?
4. When cancellation, Return or Refund is separately eligible, how may the guest request it without treating contact details, an Order Number, or a payment artifact as authorization?
5. If a guest later chooses Registration, which separately governed prerequisites apply, and what—if any—association with an earlier Order is permitted? Automatic linking is not assumed.
6. Which Checkout, Identity, Customer, Order and Payment Contracts would need review, and would a material Architecture change require an ADR?
7. Which Security and Privacy reviewers, threat and abuse cases, audit boundaries and operational support evidence are required before acceptance?

## Consequences and Dependencies

If Option C is later Accepted, Product guidance must state both permitted purchase contexts and the non-mandatory Account rule. Checkout, Customer, Order, Identity, Payment, Security and Privacy reviews must determine the separate semantics needed for safe guest purchase and protected post-purchase access. Supporting two contexts may increase authorization, support and testing complexity; preserving registered-customer capabilities avoids making guest Checkout the sole path. No cost, conversion or fraud benefit is asserted without evidence.

DEC-0009 remains Proposed with no selected strict IDNA2008 implementation; DEC-0010 remains Proposed with no selected blocklist strategy; DEC-0011 remains Proposed with no selected current-email verification mechanism. Their exhausted evaluation authorizations are not reopened. These unresolved paths continue to block the affected Registration and Login implementation work. The Product preference does not make a guest path a bypass of any applicable Security, Checkout, Order, Payment or Privacy prerequisite. Product item 4 remains open until an Accepted Product decision is synchronized into [`PRODUCT.md`](../../.ai/core/PRODUCT.md). Applicable Specification, Contract and material Architecture changes require their own governed review.

## Security Impact

Guest-order lookup and action may expose Customer data or permit cross-Order access if untrusted identifiers, contact details or client state are treated as proof. Existing default denial, contextual server-side Authorization, object isolation, abuse resistance and safe failure requirements remain binding. No guest-order access or verification mechanism is selected.

## Data Impact

The proposal creates no Customer profile, Account, guest Identity, association or schema by itself. Order retains its governed Customer-or-Visitor creation association and history; later Customer, Account, contact or Session changes cannot silently rewrite Order truth. Contact-data purpose and privacy treatment remain open for review.

## Compatibility and Migration Impact

No existing Contract, OpenAPI description, Account, Order or Payment state changes. Any later guest-to-Account association or behavior for existing Orders needs separate compatibility, historical-integrity and Authorization review; no migration is selected.

## Operational Impact

Guest tracking, support, cancellation, Return and Refund handling require separately governed eligibility and access paths. This record sets no support channel, escalation, verification process, retention period, numerical abuse limit or operational workflow.

## Explicit Non-Authorizations

This Proposed record authorizes no implementation; API endpoint, method, DTO or payload; database schema or persistence change; Session or token mechanism; guest-order access or identity-verification mechanism; automatic guest-to-Account linking; provider selection; Payment-policy change; or alteration of an Approved Specification, Contract or OpenAPI file. It does not complete Registration, grant Authentication or Account access, claim a production guest Checkout path, reopen DEC-0009/0010/0011 evaluation, resolve another Product or Architecture decision, or close `PRODUCT.md` §24 item 4.

## References

- [AGENTS.md §§5–6](../../.ai/core/AGENTS.md), [DECISIONS.md §§8–17 and 25](../../.ai/core/DECISIONS.md), and [DOCUMENTATION-STANDARDS.md §§5–9](../../.ai/core/DOCUMENTATION-STANDARDS.md) — authority, Proposed status and record governance.
- [PRODUCT.md §§7.1–7.2, 12.3, 14.4, 16.7 and 24 item 4](../../.ai/core/PRODUCT.md) — Visitor and Registered Customer capabilities, purchase context, privacy and the open Product question.
- [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [SECURITY-STANDARDS.md §§7, 12 and 20](../../.ai/core/SECURITY-STANDARDS.md), and [GLOSSARY.md §§18 and 22](../../.ai/core/GLOSSARY.md) — ownership, trust, authorization and terminology.
- [Checkout Domain REQ-CHK-010, 012 and 023](../domains/checkout/checkout-domain.md), [Customer Domain REQ-CUS-004, 008, 033 and 039](../domains/customer/customer-domain.md), and [Identity Domain REQ-IDN-008 and 028](../domains/identity/identity-domain.md).
- [Order Domain REQ-ORD-007, 021 and 042](../domains/order/order-domain.md) and [Payment Domain REQ-PAY-026 and 030](../domains/payment/payment-domain.md).
- [Approved Checkout backend](../backend/checkout/checkout-backend.md), [Customer backend](../backend/customer/customer-backend.md), [Identity backend](../backend/identity/identity-backend.md), [Order backend](../backend/order/order-backend.md), and [Payment backend](../backend/payment/payment-backend.md) — bounded implementation-neutral specializations.
- [Initial Customer Registration Contract](../contracts/customer/initial-customer-registration-contract.md) and [Initial Customer Login Contract](../contracts/identity/initial-customer-login-contract.md) — existing registered-Customer flows, not guest Checkout Contracts.
- [DEC-0009](DEC-0009-customer-login-idna2008-implementation-strategy.md), [DEC-0010](DEC-0010-customer-password-blocklist-strategy.md), and [DEC-0011](DEC-0011-initial-customer-email-verification-mechanism.md) — unresolved Proposed decisions; no evaluation is reopened.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Recorded the Product sponsor's preference for both guest and registered-customer Checkout without mandatory Account creation, framed alternatives and review dependencies, and preserved existing authority without accepting a policy or authorizing implementation. |
