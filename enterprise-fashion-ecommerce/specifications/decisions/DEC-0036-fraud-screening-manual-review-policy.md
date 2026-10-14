# DEC-0036 — Fraud Screening and Manual Review Policy

- **Identifier:** DEC-0036
- **Title:** Fraud Screening and Manual Review Policy
- **Type:** Product Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-10-14
- **Last Updated:** 2026-10-14
- **Owner:** Product
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context and Current Approved Baseline

[`PRODUCT.md` §24 item 26](../../.ai/core/PRODUCT.md) leaves “Fraud-screening approach and manual-review workflow” unresolved. Product §16.12 requires protection against foreseeable abuse in registration, authentication, voucher, promotion, Checkout, Payment, Refund and administrative journeys. High-risk actions may require additional verification or review, but no approach, journey classification or initial-release scope is selected. Fraud controls must not silently alter authoritative Order or Payment state; signals and decisions must preserve privacy, security, audit and explainability requirements.

The [Security Standards §§9–12, 27–28 and 35](../../.ai/core/SECURITY-STANDARDS.md) establish risk review, identity and access controls, monitoring, proportional audit and Sensitive Data safeguards. Approved Identity and Customer requirements govern abuse resistance and false-positive recovery without choosing screening thresholds or a workflow. Checkout, Order and Payment may consume governed outcomes within their own authority, but a fraud signal is not Payment evidence, Order truth or permission to change a lifecycle. Administration may coordinate only an authorized review. No dedicated screening capability, staffed manual-review operation, fraud provider or customer notification rule is evidenced by these requirements.

## Decision Question and Status

What fraud-screening approach and manual-review policy, if any, should govern the initial offering's registration, authentication, voucher, promotion, Checkout, Payment, Refund and administrative journeys, while preserving existing domain, Security, Privacy and operational authority?

This question does not presume that every journey needs the same control. **NO ALTERNATIVE SELECTED.** This 0.1.0 Proposed record is non-authoritative, grants no comparative evaluation or implementation authority, and leaves `PRODUCT.md` §24 item 26 open.

## Alternatives Considered — Not Formally Compared

| Option | Conditional policy approach | Possible benefit | Limitations, risks and evidence required |
| --- | --- | --- | --- |
| A — Existing-Control-Led Policy | Consider relying on existing Approved safeguards for the initial offering, without separately committing a dedicated fraud-screening or manual-review capability unless later justified. | Could avoid an unsupported new workflow while retaining mandatory Security and Domain controls. | Existing controls might not address material journey-specific exposure. Requires actual threat and abuse evidence, customer and business impact, Security review and launch-scope validation; this option cannot waive mandatory controls. |
| B — Risk-Tiered Review Policy | Consider additional governed verification or human review for defined higher-risk journeys only after eligibility, evidence, authority and operations are separately approved. | Could focus intervention where evidence shows proportionate need. | False positives, friction, staffing and inconsistent outcomes are possible. Requires journey classification, signal provenance and freshness, authorized review, recovery, customer communication and operational feasibility evidence; no threshold or intervention is set here. |
| C — Broader Coordinated Screening Policy | Consider coordinated screening and review policy across multiple affected journeys, subject to proportionality and separate Domain, Security, Privacy and Architecture governance. | Could improve consistency across related abuse contexts if supported by evidence. | Greater privacy, integration, support, accountability and provider-dependency risk. Requires cross-domain ownership and compatibility evidence, false-positive recovery, access and audit controls, affected-owner review and cost assessment; no central fraud authority is created. |

The three approaches are **unselected, unranked and non-authoritative**. They are policy categories, not scored options, implementation designs or claims about current operations. None authorizes bypass of existing Security safeguards.

## Ownership and Approved Authority Boundaries

| Function | Boundary preserved |
| --- | --- |
| Product | Owns unresolved customer-facing and commercial screening policy; does not select a mechanism through this record. |
| Security | Retains mandatory controls, threat assessment, risk review and security approval within existing authority. |
| Identity | Retains Authentication, Authorization and identity-related evidence and controls; no fraud score becomes Identity truth. |
| Customer | Retains Customer and Account data authority and applicable false-positive recovery; fraud representations do not redefine Customer truth. |
| Checkout | Retains current purchase validation and progression; a signal does not independently establish purchase eligibility or a restriction. |
| Order | Retains Order lifecycle and confirmed commercial evidence; no hold state or silent state change is introduced. |
| Payment | Retains Payment and Refund transaction truth and validated Payment Provider evidence; fraud advice never becomes Payment proof. |
| Administration | Coordinates only governed Staff User review within current Authorization; no Role, Permission, blocking outcome or approval chain is granted. |
| Notifications | Orchestrates only approved communications and delivery outcomes; it does not choose fraud policy or customer messages. |
| Reporting | Consumes derived, provenance-preserving evidence and audit outcomes without becoming source or transactional authority. |
| Operations | Reviews operational readiness, support and escalation within separately approved authority; no staffed process is asserted. |
| Architecture | Owns material technical structure, integration and cross-domain ownership decisions separately from Product policy. |

No Fraud Domain is created, fraud-scoring or fraud-data authority assigned, Staff Role or Permission granted, Order or Payment hold introduced, or Domain ownership transferred. Missing, stale or uncertain signals must not be treated as confirmed outcomes or transactional proof.

## Related Proposed Decisions and Scope Dependencies

[DEC-0012](DEC-0012-administrative-role-permission-matrix.md) leaves Staff access policy Proposed; it grants no review Permission. [DEC-0013](DEC-0013-guest-checkout-account-requirement.md) leaves guest-checkout policy Proposed and does not establish identity or fraud proof. [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md) and [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md) leave cancellation and Return/Refund policy Proposed; fraud advice cannot independently deny or alter those outcomes. [DEC-0027](DEC-0027-production-customer-service-operational-escalation-process.md) leaves operational escalation Proposed and establishes no fraud staffing or route. [DEC-0028](DEC-0028-initial-payment-methods-provider-policy.md) leaves Payment methods and provider policy Proposed and selects no screening provider. These records are non-authoritative and do not complete any prerequisite for policy acceptance.

Accepted [ADR-0014](../adr/ADR-0014-post-payment-backend-specification.md) preserves item 26 as open and selects no screening, review or intervention policy. Existing Architecture and Domain boundaries remain in force; no new Architecture decision is made here.

## Evidence Required Before Evaluation or Selection

Product, Security and affected owners must identify actual fraud and abuse exposure; intended initial journeys; customer and business impact; false-positive handling and recovery; journey-specific eligibility; screening-signal provenance, freshness, uncertainty and permitted explainability; privacy and Sensitive Data scope; Security controls; authorized Staff review; proportional Audit Records; customer communication; operational ownership and escalation; Finance, Legal and regulatory implications where applicable; provider and third-party signal dependencies; cross-domain compatibility; and initial-release feasibility. A risk list is not evidence that a measured fraud problem, staff capacity, provider Contract, qualified legal obligation or approved review process exists. Evidence and reviewer confirmations must be attributable before any comparison or selection.

## Security, Privacy, Consequences and Operational Impact

A later evidence-backed policy might reduce fraud loss or improve intervention consistency. It could also harm legitimate Customers through false positives, delay Checkout or Payment, expose Sensitive Data, increase Staff workload and operational cost, create unclear accountability, or encourage incorrect transaction-state intervention. Third-party signals could introduce privacy, provenance, reliability and provider-lock-in risks. Inconsistent cross-domain outcomes or weak audit and explainability could make recovery difficult. These are conditional trade-offs, not measured findings or a selected operating model.

Any later design must preserve server-side Authorization, least privilege, default denial, Customer isolation, Payment evidence integrity, Order lifecycle authority, proportionate audit, protected data, explicit uncertainty and safe recovery. Risk signals cannot silently change authoritative state or override owning-Domain validation. No retention period, scoring method, challenge, review queue, threshold, provider, communication or intervention is defined.

## Reviewers and Future Governance Gates

Product leads the policy question. Security, Privacy/Legal, Identity, Customer, Checkout, Order, Payment, Administration, Notifications, Reporting, Operations, Finance where affected, Architecture and Engineering review their respective boundaries. Listing a reviewer does not imply review or approval.

1. **Initial Proposed drafting:** This record frames the open question and three approaches only.
2. **Comparative evaluation:** Obtain separate explicit authorization and attributable journey, risk, customer, operational and technical evidence before comparing approaches.
3. **Security and Privacy/Legal review:** Assess threats, false positives, personal data, access, explainability, audit, lawful processing and third-party risk as applicable.
4. **Product policy acceptance:** Obtain affected-owner review, resolve intended scope and formally accept a Product choice; Proposed text remains non-authoritative.
5. **Architecture and affected-owner review:** Govern any new cross-domain ownership, lifecycle or integration design separately.
6. **Provider or integration selection:** Use separate diligence and governance if a provider or third-party signal is proposed.
7. **Specification and Contract changes:** Govern any lifecycle, API, Event, Role, Permission or other affected-source changes through their own approvals.
8. **Implementation and operational rollout:** Verify operational ownership, recovery, customer impact, testing and release readiness under resulting authority.

Initial drafting authorizes none of the downstream gates.

## Validation Criteria

This record remains a valid initial proposal only if: (1) it traces the exact `PRODUCT.md` §24 item 26; (2) it remains 0.1.0 Proposed, Product-owned and non-authoritative; (3) exactly three approaches remain unselected and unranked; (4) mandatory Security and Domain safeguards remain binding; (5) no Fraud Domain, scoring owner, Staff grant, Order or Payment hold, provider or workflow is created; (6) no fraud signal becomes transaction proof; and (7) evaluation, review, acceptance, source changes and implementation remain separate gates.

## Decision Status and Explicit Non-Decisions

DEC-0036 supersedes nothing and is superseded by nothing. It selects no fraud-scoring model, numerical threshold, rule, screening provider, third-party signal integration, fraud-data owner, Customer verification challenge, customer notification rule, manual-review queue or workflow, review owner or staffing model, Staff Role or Permission, Order or Payment hold or lifecycle change, Refund restriction, API or Event Contract, database schema, Architecture mechanism, implementation or deployment. It creates no Fraud Domain, approves no comparative evaluation, and changes no Approved Product, Security, Domain or Backend authority.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§16.12 and 24 item 26](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) and [SECURITY-STANDARDS.md §§9–12, 27–28 and 35](../../.ai/core/SECURITY-STANDARDS.md) — Product question and mandatory governance.
- [Identity REQ-IDN-011](../domains/identity/identity-domain.md) and [BIDN-REQ-015](../backend/identity/identity-backend.md); [Customer REQ-CUS-042](../domains/customer/customer-domain.md) and [BCUS-REQ-038](../backend/customer/customer-backend.md) — abuse resistance, false-positive and data boundaries.
- [Checkout REQ-CHK-036](../domains/checkout/checkout-domain.md) and [BCHK-REQ-036](../backend/checkout/checkout-backend.md); [Order REQ-ORD-044](../domains/order/order-domain.md) and [BORD-REQ-044](../backend/order/order-backend.md) — progression and lifecycle non-authority.
- [Payment REQ-PAY-033](../domains/payment/payment-domain.md) and [BPAY-REQ-041](../backend/payment/payment-backend.md); [Administration REQ-ADM-012, 014–015 and 034](../domains/admin/admin-domain.md) and [BADM-REQ-034](../backend/admin/admin-backend.md) — Payment evidence and governed review coordination.
- [Notifications REQ-NTF-049](../domains/notifications/notifications-domain.md) and [Notifications Backend](../backend/notifications/notifications-backend.md); [Reporting REQ-RPT-008, 013–018 and 047](../domains/reporting/reporting-domain.md) and [Reporting Backend](../backend/reporting/reporting-backend.md) — communication, provenance, uncertainty and audit boundaries.
- [DEC-0012](DEC-0012-administrative-role-permission-matrix.md), [DEC-0013](DEC-0013-guest-checkout-account-requirement.md), [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md), [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md), [DEC-0027](DEC-0027-production-customer-service-operational-escalation-process.md), [DEC-0028](DEC-0028-initial-payment-methods-provider-policy.md) and [ADR-0014](../adr/ADR-0014-post-payment-backend-specification.md) — related unresolved Product policy and preserved backend-roadmap scope.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-14 | Proposed | Initial Proposed Product Decision Record for fraud screening and manual review policy, preserving unselected alternatives, existing security safeguards, domain authority, operational dependencies and separate governance gates. |
