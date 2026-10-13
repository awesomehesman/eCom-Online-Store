# DEC-0028 — Initial Payment Methods and Provider Policy

- **Identifier:** DEC-0028
- **Title:** Initial Payment Methods and Provider Policy
- **Type:** Product Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-10-13
- **Last Updated:** 2026-10-13
- **Owner:** Product
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context and Current Approved Baseline

[`PRODUCT.md` §24 item 6](../../.ai/core/PRODUCT.md) leaves initial payment methods and provider open. Its §16.3 requires authoritative Payment Provider evidence, distinguishable Payment concepts and uncertain outcomes, duplicate financial-effect prevention and reconciliation. The [Approved Payment Domain Specification](../domains/payment/payment-domain.md) REQ-PAY-006–024 and [Payment Backend Specification](../backend/payment/payment-backend.md) BPAY-REQ-019–035 establish Payment ownership, provider-neutral semantics and safety requirements. Neither selects a method, payment rail, provider, provider Contract or integration.

[`ARCHITECTURE.md` §34 item 5](../../.ai/core/ARCHITECTURE.md) separately leaves Payment provider selection open for Architecture decision-making. Accepted [ADR-0014](../adr/ADR-0014-post-payment-backend-specification.md) governs post-Payment backend sequencing; it does not select a provider. This Product record frames the customer-facing method offering and business constraints that any future provider choice must satisfy. It neither makes nor authorizes the Architecture provider decision. [SECURITY-STANDARDS.md §36](../../.ai/core/SECURITY-STANDARDS.md) and [`DECISIONS.md` §§29, 35 and 38](../../.ai/core/DECISIONS.md) retain their Payment, provider and review safeguards.

## Decision Questions and Status

What breadth of customer payment methods is appropriate for the initial Product? What attributable customer demand, accessibility and usability needs, Checkout compatibility, payment availability and reliability, commercial viability, fees and settlement implications, Refund and reconciliation support, Operations capacity, provider portability, Payment-data protection and launch-readiness evidence should constrain later Product policy and Architecture provider selection?

**NO OPTION SELECTED.** These questions do not identify a method or provider, establish financial terms, rank candidates, or close `PRODUCT.md` §24 item 6 or `ARCHITECTURE.md` §34 item 5.

## Alternatives Considered — Not Formally Compared

| Option | Initial Product policy candidate | Potential benefit | Limitation, risk and evidence needed |
| --- | --- | --- | --- |
| A — Minimal initial offering | Consider a bounded initial set of customer payment methods justified by launch evidence. | Could limit initial complexity and make customer guidance and support easier to validate. | May omit material customer needs or reduce accessibility; attributable demand, failure, commercial and coverage evidence is needed. No method is selected. |
| B — Broader initial offering | Consider a broader set of methods only if customer, operational, financial and security evidence supports the additional breadth. | Could serve more customer preferences and recovery situations. | More variation may increase failure, support, reconciliation, fee and PCI-scope complexity; no set or provider is selected. |
| C — Staged expansion | Consider a bounded initial offering with later methods added only after separate Product, Architecture, Security and Contract governance. | Could preserve reversibility and allow later evidence to inform expansion. | A limited first release may not meet demand, and expansion needs operational and compatibility planning; no initial set, schedule or addition is selected. |

All three alternatives remain **unselected, unranked and non-authoritative**. They concern method-offering breadth, not a particular method, rail, acquirer, processor, provider, SDK or technical integration. No provider is compared or preferred.

## Domain and Governance Boundaries

| Authority | Boundary preserved |
| --- | --- |
| Product | Owns the customer-facing payment-method policy, intended experience and commercial requirements. Product cannot select a provider-specific technical architecture through this record. |
| Payment | Owns Payment, Payment Attempt, provider-validated evidence, Payment Authorization, Capture, Void, Settlement, Payment Transaction, Refund execution where governed, uncertainty, reconciliation and permitted transitions under [REQ-PAY-006–024](../domains/payment/payment-domain.md) and [BPAY-REQ-019–035](../backend/payment/payment-backend.md). |
| Checkout | Coordinates the governed payment handoff and customer progression; it cannot independently declare Payment success. See the [Checkout Domain Specification](../domains/checkout/checkout-domain.md). |
| Order | Owns Order creation and lifecycle and consumes governed Payment outcomes without rewriting Payment truth. See the [Order Domain Specification](../domains/order/order-domain.md). |
| Finance | Reviews commercial cost, fee exposure, settlement, reconciliation and financial-operational implications without a fee or schedule being selected here. |
| Security and Privacy/Legal | Preserve hosted or tokenised handling, sensitive-data and Secret controls, qualified PCI-scope review, provider-evidence trust, contextual Authorization and applicable contractual or protected-data review. |
| Architecture | Owns the separate provider-selection ADR and material integration architecture under `ARCHITECTURE.md` §34 item 5. Product requirements inform that decision but do not substitute for it. |
| Engineering and Operations | Review feasibility, resilience, support, failure, recovery and migration without selecting an Adapter, Contract, provider or implementation through this record. |

Existing Proposed commerce decisions, including [DEC-0013](DEC-0013-guest-checkout-account-requirement.md), [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md) and [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md), are not Accepted policy and cannot establish guest Payment, cancellation, Return or Refund treatment here.

## Payment Safety and Future Evaluation Criteria

Any later choice must preserve provider-neutral Payment integration behind an Approved project-owned Contract under [REQ-PAY-010–012](../domains/payment/payment-domain.md) and [BPAY-REQ-021–023](../backend/payment/payment-backend.md). Hosted or tokenised sensitive-data handling and qualified PCI-scope review remain required. A browser return, client report or Payment Redirect is not authoritative Payment or Settlement proof. Provider-dependent truth requires validated server-side evidence; malformed, stale, replayed or unverifiable evidence cannot mutate trusted state. Payment Authorization, Capture, Void, Settlement and Refund outcomes remain distinct.

Duplicate requests, callbacks, retries or recovery must not create repeated financial effects. Unknown, pending, delayed, conflicting and uncertain outcomes remain explicit until reconciled with authorized evidence. Refund execution remains governed by applicable approved eligibility and amount; this record selects no Refund policy. Provider replacement must preserve project-owned business semantics, historical Payment meaning and safe migration rather than embed vendor terminology into core rules. No provider Contract, verification protocol, SDK, credential mechanism or retry implementation is defined.

A separately authorized comparison must consider attributable customer demand; accessibility and usability; Checkout conversion without sacrificing truthful outcomes; Payment success, decline and failure evidence; operational availability and reliability; commercial fees and total cost; Settlement and reconciliation; Refund compatibility; Security and PCI impact; provider portability, replacement and support; implementation readiness; reversibility; and future expansion. These are criteria, not scores or findings. No measurement, provider diligence, fee quote, Finance conclusion or stakeholder approval is asserted.

## Reviewers, Dependencies and Future Gates

Product leads the policy question. Payment, Checkout, Order, Finance, Security, Architecture, Engineering and Operations must review the aspects they own. Legal/Privacy review applies to provider contracts, customer rights and protected payment-data effects. Other owning Domains participate where their governed behavior is materially affected. Consultation is not an assertion that review occurred.

1. **Attributable Product and commercial evidence:** Establish intended customer journeys, demand and accessibility needs, expected method breadth, financial and operational constraints, and the evidence basis. This record claims none complete.
2. **Comparative Product evaluation:** Obtain separate authorization before comparing the three method-scope alternatives. This record does not conduct or authorize comparison.
3. **Product selection and acceptance:** Product and affected owners decide the intended offering through [`PRODUCT.md` §§25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17, 29 and 35](../../.ai/core/DECISIONS.md), with canonical Product synchronization where required. No alternative is Accepted here.
4. **Architecture provider selection:** Resolve `ARCHITECTURE.md` §34 item 5 through separately governed ADR authority and provider evidence. A Product method policy does not pick the provider or Adapter.
5. **Provider and control approval:** Obtain applicable provider Contract, Security, PCI-scope, Finance, Engineering, Operations and Legal/Privacy approvals before relying on an integration. No gate is presumed passed.
6. **Implementation and launch readiness:** Authorize implementation separately and verify Payment, Checkout, Order, financial-effect, failure, recovery, support and compatibility safeguards. This Proposed record grants no delivery authority.

## Validation Criteria

Review of this record passes only if: (1) it remains 0.1.0 Proposed, Product-owned and non-authoritative with `PRODUCT.md` §24 item 6 traceability; (2) exactly three policy-level alternatives remain unselected and unranked; (3) no method, rail, provider or provider ranking appears as a selection; (4) Product policy and Architecture provider-selection authority remain distinct; (5) Approved Payment ownership, validated evidence, hosted/tokenised handling, uncertainty, duplicate safety and reconciliation remain intact; (6) no commercial measurement or stakeholder approval is fabricated; (7) no Contract, API, Event, schema or mechanism is selected; and (8) evidence, Product evaluation and acceptance, Architecture selection, control approval and implementation remain separate gates.

## Decision Status and Explicit Non-Decisions

DEC-0028 supersedes nothing and is superseded by nothing. It remains **0.1.0 Proposed, Product-owned and non-authoritative**. It selects no payment method, rail, provider, acquirer, processor, provider ranking, Capture timing, Settlement schedule, fee or Pricing term, Refund policy, provider Contract, API, Event, schema, SDK, credential-handling mechanism, integration architecture or implementation. It does not compare providers, authorize Product comparative evaluation, accept Product policy, resolve the Architecture provider decision, change an Approved Specification or make an existing Proposed commerce decision authoritative. `PRODUCT.md` §24 item 6 and `ARCHITECTURE.md` §34 item 5 remain open.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§14.4–14.5, 16.3, 24 item 6, 25 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §34 item 5](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§29, 35 and 38](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md), and [SECURITY-STANDARDS.md §36](../../.ai/core/SECURITY-STANDARDS.md) — decision, Payment, provider and security boundaries.
- [Payment Domain REQ-PAY-006–024](../domains/payment/payment-domain.md) and [Backend BPAY-REQ-019–035](../backend/payment/payment-backend.md) — owner authority, provider neutrality, validated evidence, uncertainty and financial integrity.
- [Checkout Domain](../domains/checkout/checkout-domain.md) and [Order Domain](../domains/order/order-domain.md) — distinct coordination and commercial truth.
- [ADR-0014](../adr/ADR-0014-post-payment-backend-specification.md) — Accepted post-Payment backend sequencing, not provider selection.
- [DEC-0013](DEC-0013-guest-checkout-account-requirement.md), [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md) and [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md) — related Proposed, not Accepted, commerce policy.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed three unselected initial payment-method breadth alternatives and future provider business constraints while preserving Payment safety, Product and Architecture authority separation, unresolved Product item 6 and Architecture item 5, and distinct evidence, evaluation, acceptance, provider and implementation gates. |
