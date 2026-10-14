# DEC-0038 — Customer Data Requests and Account Closure Policy

- **Identifier:** DEC-0038
- **Title:** Customer Data Requests and Account Closure Policy
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

[`PRODUCT.md` §24 item 28](../../.ai/core/PRODUCT.md) leaves “Customer data export, correction, deletion, and account-closure workflow” unresolved. Product §16.7 requires purpose-limited collection, separation of Consent from transactional necessity, permission-controlled and auditable Customer-data access, and preservation of historical Order snapshots when an Account changes. The [Security Standards §§10–12, 27 and 35](../../.ai/core/SECURITY-STANDARDS.md) require trusted identity, server-side contextual Authorization, default denial, data minimization, protected and expiring bulk exports, proportional Audit Records, and privacy-by-design consideration of retention, backup expiry and data-subject requests. These obligations do not select an end-to-end request or closure workflow.

The [Customer Domain REQ-CUS-024–029 and 039–041](../domains/customer/customer-domain.md) and [Customer Backend BCUS-REQ-030–033](../backend/customer/customer-backend.md) preserve Customer-owned data, Account business semantics, governed historical access, correction, closure and data-request boundaries. Identity retains credentials, Sessions, Authentication and trusted identity evidence. Order, Payment, Return, Shipping, Notifications and other owners retain their own historical and transactional records; a Customer-data request cannot silently rewrite those facts. Account closure is not automatically Identity deletion, erasure of retained commercial records, or safe reuse of a login identifier. No current Approved source supplies a complete Customer request workflow, retention schedule, deletion method or legal conclusion.

## Decision Question and Status

What Customer data export, correction, deletion/anonymization and Account-closure workflow requirements, if any, should be adopted within Approved Domain, Security, Privacy and legal constraints?

The four concerns may be assessed independently where their purposes, owners or obligations differ. **NO ALTERNATIVE SELECTED.** This 0.1.0 Proposed record is non-authoritative, leaves `PRODUCT.md` §24 item 28 open, and authorizes neither comparative evaluation nor implementation.

## Alternatives Considered — Not Formally Compared

| Option | Conditional policy approach | Potential benefit | Limitations, dependencies and evidence needed |
| --- | --- | --- | --- |
| A — Owner-Coordinated Requests | Consider requests coordinated through authorized responsible owners and established controls, with each owning Domain validating any effect on its data. | Could keep authority and evidence close to the owning records while the request mix is established. | Coordination may be slow or inconsistent. Requires qualified obligations, requester verification, accountable request ownership, capacity, access, customer communication, per-owner evidence and recovery planning. It cannot defer or weaken an applicable mandatory right. |
| B — Bounded Customer-Initiated Requests | Consider allowing Customers to initiate specified request types, followed by verified identity and owning-Domain validation before any change or disclosure. | Could make supported requests easier to initiate and track. | Initiation is not self-service completion or automatic deletion. Requires evidence for Customer journeys, accessibility, impersonation resistance, requested data scope, historical exceptions, per-owner outcomes and operational follow-through. |
| C — Broader Coordinated Cross-Domain Request Policy | Consider a more integrated policy for requests affecting several owning Domains while each retains authority over its own data, history and permitted effects. | Could provide a more coherent Customer experience and consistent request traceability if supported by evidence. | Increases privacy, Security, operational and cross-domain reconciliation complexity. Requires qualified scope, lawful handling, source inventory, ownership, correction propagation, retention, uncertain outcomes and Architecture review; no central Data Rights authority is created. |

The three approaches remain **conditional, unselected, unranked and non-authoritative**. None may defer, bypass or weaken an already-applicable mandatory legal, Privacy or Security obligation. No approach presumes an existing channel, workflow, staffing model or technical capability.

## Approved Authority Boundaries

| Owner or function | Boundary preserved |
| --- | --- |
| Customer | [REQ-CUS-024–029 and 039–041](../domains/customer/customer-domain.md) and [BCUS-REQ-030–033](../backend/customer/customer-backend.md) govern Customer-owned data, Account business state, correction, authorized requests and historical non-rewriting; exact workflow and retention remain open. |
| Identity | [REQ-IDN-003, 007 and 028](../domains/identity/identity-domain.md) and [BIDN-REQ-004](../backend/identity/identity-backend.md) preserve credentials, Sessions, Authentication, trusted identity evidence and owning-Domain Authorization. Customer closure does not independently delete Identity or choose a revocation mechanism. |
| Order | [REQ-ORD-020](../domains/order/order-domain.md) and [BORD-REQ-020](../backend/order/order-backend.md) preserve confirmed commercial history and append or associate governed correction or privacy evidence without fabricating prior truth. |
| Payment | [Payment Domain](../domains/payment/payment-domain.md) and [BPAY-REQ-007](../backend/payment/payment-backend.md) retain Payment, Refund and provider-evidence history and financial transaction truth; a Customer request cannot rewrite those records. |
| Return | [REQ-RET-007](../domains/return/return-domain.md) and [BRET-REQ-007](../backend/return/return-backend.md) preserve Return lifecycle and required historical Order evidence. |
| Shipping | [REQ-SHP-010](../domains/shipping/shipping-domain.md) and [BSHP-REQ-020](../backend/shipping/shipping-backend.md) distinguish current Customer Address from historical delivery context. |
| Notifications | [Notifications Domain](../domains/notifications/notifications-domain.md) and [BNTF-REQ-050](../backend/notifications/notifications-backend.md) retain governed delivery and required evidence without defining retention, correction or deletion policy. |
| Reporting | [REQ-RPT-024 and 041](../domains/reporting/reporting-domain.md) and [BRPT-REQ-024](../backend/reporting/reporting-backend.md) preserve source-governed correction and purpose-bound derived exports, not authoritative Customer truth. |
| Administration | [REQ-ADM-035 and 049](../domains/admin/admin-domain.md) and [BADM-REQ-049](../backend/admin/admin-backend.md) permit only authorized, minimized coordination and owner-invoked retention or deletion actions; no Staff Permission or Customer-data ownership is granted. |
| Security and Privacy | Existing verification, least privilege, default denial, minimization, encryption, bounded export, audit, lawful handling and retention safeguards remain binding; qualified Privacy/Legal review is required before production reliance. |
| Architecture and Operations | [Architecture](../../.ai/core/ARCHITECTURE.md) governs material technical structure and cross-domain mechanisms. Operations reviews request handling, failure, recovery, backup effects and readiness only within approved authority. |

No Data Rights Domain, centralized authority over owning-Domain records, Account lifecycle state or cross-domain deletion command is created.

## Related Decisions, Contracts and Dependencies

Accepted [DEC-0005](DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md) preserves retained login-email association semantics and expressly leaves post-deletion reuse and the wider item 28 workflow unresolved. The approved [Customer registration Contract](../contracts/customer/initial-customer-registration-contract.md) and [Identity login Contract](../contracts/identity/initial-customer-login-contract.md) likewise do not approve post-deletion reuse or a data-request API.

[DEC-0012](DEC-0012-administrative-role-permission-matrix.md) leaves Staff access policy Proposed; [DEC-0013](DEC-0013-guest-checkout-account-requirement.md) leaves guest checkout and Account policy Proposed; [DEC-0022](DEC-0022-reporting-exports-policy.md) governs unresolved reporting exports, not a Customer-data export entitlement; [DEC-0025](DEC-0025-marketing-consent-communication-preferences-policy.md) leaves marketing Consent and Preferences Proposed; and [DEC-0027](DEC-0027-production-customer-service-operational-escalation-process.md) leaves support escalation Proposed. [DEC-0037](DEC-0037-product-data-import-export-migration-requirements.md) concerns Product-owned catalogue data, not Customer-data requests. These are conditional dependencies or shared evidence needs, not prerequisites to framing this Product question. Later policy acceptance must reconcile any affected access, guest, Consent, reporting or support behavior without treating a Proposed record as authority.

## Evidence Required Before Evaluation or Selection

Attributable evidence must identify request types and expected volumes; Customer journeys and accessibility; requester identity verification and fraud resistance; authoritative data inventory and owners; export scope and exclusions; correction validation and propagation; deletion, anonymization and retention constraints; historical commercial, financial, delivery and Audit records; Account closure and access revocation; post-closure and post-deletion Identity effects, including unresolved login-email reuse; backup, cache and derived-data treatment; evidence preservation; operational ownership and capacity; Customer communication; Security and Privacy risks; and qualified Privacy/Legal assessment. These are evaluation questions, not measured findings or selected requirements. No statutory deadline, retention period, legal conclusion or stakeholder approval is asserted.

## Security, Data, Consequences and Operational Impact

A later governed policy could improve Customer clarity and accountable handling of valid requests. It could also enable unauthorized disclosure, fraudulent requests, impersonation, overbroad export, incorrect or irreversible deletion, conflicting corrections, premature access removal, historical-record corruption or inconsistent cross-domain outcomes. Bulk exports need the already binding encryption, expiry, authorization, minimization and audit safeguards. Retained evidence, backup expiry, recovery and uncertain partial effects require qualified review; Customer closure cannot be equated with deletion of Identity or other owners' records. These are conditional risks and potential benefits, not an implementation design or legal finding.

## Reviewers and Future Governance Gates

Product leads the policy question. Customer, Identity, Order, Payment, Return, Shipping, Notifications, Reporting and Administration owners; Security, Privacy/Legal, Architecture, Engineering and Operations review their respective boundaries. Listing a reviewer does not claim review or approval.

1. **Initial Proposed drafting:** This record frames the question and three conditional approaches only.
2. **Evidence sufficiency and scope review:** Establish attributable request, data-inventory, owner, historical-integrity and operational evidence.
3. **Qualified Privacy/Legal review:** Validate applicable obligations, lawful handling, retention, deletion, disclosure and cross-border considerations without inferring conclusions from this proposal.
4. **Security and affected-owner review:** Assess verification, Authorization, access revocation, fraud, minimization, audit and each Domain's permitted effects.
5. **Comparative evaluation:** Obtain separate explicit authorization before comparing approaches.
6. **Product policy selection and DEC acceptance:** Select only through approved Product and affected-owner governance and synchronize changed canonical Product authority.
7. **Architecture, Specification, Contract and implementation governance:** Approve any material cross-domain mechanism, Contract, lifecycle or technical change separately before rollout.

Completing one gate does not authorize the next.

## Validation Criteria

This record is valid as an initial proposal only if: (1) it traces exact `PRODUCT.md` §24 item 28 wording; (2) it remains 0.1.0 Proposed, Product-owned and non-authoritative; (3) exactly three approaches remain unselected and unranked; (4) Customer Account semantics and Identity authority remain separate; (5) Order and Payment history and other owning-Domain records remain protected; (6) existing Security and Privacy controls remain binding without invented legal interpretation or retention/deletion policy; (7) no Staff Permission, new Domain or Contract is created; and (8) evaluation, approval, source change and implementation gates remain separate.

## Decision Status and Explicit Non-Decisions

DEC-0038 supersedes nothing and is superseded by nothing. It selects no Customer request channel, requester-verification mechanism, export scope or file format, correction workflow or propagation mechanism, deletion or anonymization method, retention period, Account-closure state or timing, Identity deletion or Session-revocation mechanism, post-deletion login-email reuse rule, Staff Role or Permission, provider or external service, API, Event or Message Contract, Architecture or technical implementation, rollout or operational service commitment. It creates no Data Rights Domain, claims no legal compliance, grants no comparative evaluation, and authorizes no implementation.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§16.7 and 24 item 28](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) and [SECURITY-STANDARDS.md §§10–12, 27 and 35](../../.ai/core/SECURITY-STANDARDS.md) — Product question and governing controls.
- [Customer Domain REQ-CUS-024–029 and 039–041](../domains/customer/customer-domain.md), [Customer Backend BCUS-REQ-030–033](../backend/customer/customer-backend.md), [Identity Domain REQ-IDN-003, 007 and 028](../domains/identity/identity-domain.md) and [Identity Backend BIDN-REQ-004](../backend/identity/identity-backend.md) — Customer data, Account and Identity boundaries.
- [Order Domain REQ-ORD-020](../domains/order/order-domain.md), [Order Backend BORD-REQ-020](../backend/order/order-backend.md), [Payment Domain](../domains/payment/payment-domain.md), [Payment Backend BPAY-REQ-007](../backend/payment/payment-backend.md), [Return Domain REQ-RET-007](../domains/return/return-domain.md), [Return Backend BRET-REQ-007](../backend/return/return-backend.md), [Shipping Domain REQ-SHP-010](../domains/shipping/shipping-domain.md) and [Shipping Backend BSHP-REQ-020](../backend/shipping/shipping-backend.md) — historical transaction and delivery authority.
- [Notifications Domain](../domains/notifications/notifications-domain.md), [Notifications Backend BNTF-REQ-050](../backend/notifications/notifications-backend.md), [Reporting Domain REQ-RPT-024 and 041](../domains/reporting/reporting-domain.md), [Reporting Backend BRPT-REQ-024](../backend/reporting/reporting-backend.md), [Administration Domain REQ-ADM-035 and 049](../domains/admin/admin-domain.md) and [Administration Backend BADM-REQ-049](../backend/admin/admin-backend.md) — delivery, derived-export and coordination boundaries.
- [DEC-0005](DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md), [Customer registration Contract](../contracts/customer/initial-customer-registration-contract.md) and [Identity login Contract](../contracts/identity/initial-customer-login-contract.md) — retained login association and unresolved post-deletion reuse.
- [DEC-0012](DEC-0012-administrative-role-permission-matrix.md), [DEC-0013](DEC-0013-guest-checkout-account-requirement.md), [DEC-0022](DEC-0022-reporting-exports-policy.md), [DEC-0025](DEC-0025-marketing-consent-communication-preferences-policy.md), [DEC-0027](DEC-0027-production-customer-service-operational-escalation-process.md) and [DEC-0037](DEC-0037-product-data-import-export-migration-requirements.md) — related Proposed, non-authoritative decisions.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-14 | Proposed | Initially framed Customer data requests and Account closure with three unselected approaches, preserved Customer and Identity separation and historical Domain authority, and left legal review, evaluation, acceptance and implementation as separate gates. |
