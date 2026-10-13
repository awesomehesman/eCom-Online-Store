# DEC-0026 — Customer-Support Channels and Service Expectations Policy

- **Identifier:** DEC-0026
- **Title:** Customer-Support Channels and Service Expectations Policy
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

[`PRODUCT.md` §24 item 18](../../.ai/core/PRODUCT.md) leaves “Customer-support channels and service expectations” open. Its §34.4 requires detailed support channels, hours, response targets, escalation levels, and customer communication templates to be defined before launch; that future readiness obligation is not a statement that any value or production capability already exists. Customer-facing support needs include understanding how to seek help, when a response may be expected, and how an unresolved issue is communicated. Product must make those choices through governed policy, informed by operational evidence, without converting a candidate service expectation into a guarantee.

The Approved [Administration Domain Specification](../domains/admin/admin-domain.md) REQ-ADM-013 and 041 and [Administration Backend Specification](../backend/admin/admin-backend.md) BADM-REQ-013 and 041 allow only governed, purpose-bound support and escalation coordination. Administration cannot redefine Customer, Order, Payment, Shipment, Return, Refund or Notification truth, or select channels, levels, targets, staffing or resolution policy. [Customer Domain](../domains/customer/customer-domain.md) remains authoritative for Customer information; [Notifications Domain](../domains/notifications/notifications-domain.md) owns governed communication attempts and delivery outcomes, not support policy. A customer-visible message or provider acknowledgment is not proof that a support issue was resolved.

This record concerns the initial customer-facing service offer. [`PRODUCT.md` §24 item 24](../../.ai/core/PRODUCT.md) separately leaves the production customer-service and operational escalation process open. A future item 18 policy may state customer-facing expectations, but cannot silently select internal escalation ownership, routes, levels, approval chains, staffing or repair workflows for item 24. Neither decision may take another Domain's source authority.

## Decision Question and Status

Which customer-support channels, service availability and response expectations should the initial Product offer and communicate, and what customer-facing commitments are appropriate? Which dependencies on escalation require separately governed operational policy rather than being assumed by a channel choice?

**NO OPTION SELECTED.** The questions do not approve a channel, operating model, response time, resolution guarantee, contact detail or launch service commitment. `PRODUCT.md` §24 items 18 and 24 remain open.

## Alternatives Considered — Not Formally Compared

| Option | Initial policy candidate | Potential benefit | Limitation and evidence required |
| --- | --- | --- | --- |
| A — Single primary channel | Consider one customer-facing support channel with a separately defined, evidence-backed availability and response expectation. | Could simplify customer guidance and initial operational coordination. | May exclude customers who cannot use the channel or needs that it handles poorly; accessibility, demand and coverage evidence are needed. No channel is chosen. |
| B — Multiple channels, common expectation | Consider multiple customer-facing channels governed by one coherent service-expectation policy. | Could offer access paths suited to different customer needs while reducing conflicting promises. | Channel capabilities, staffing and consistency may differ; evidence is needed before any common target or availability claim. No channel or target is chosen. |
| C — Phased bounded offer | Consider an initial limited channel offer with later expansion only after separately governed evidence and approval. | Could allow a manageable initial service scope and reversible growth. | Customers may face a limited first-release choice; phase criteria, continuity and communication need review. No launch phase, date, channel or expansion promise is chosen. |

All three alternatives are **unselected, unranked and non-authoritative**. These are policy questions for later Product evaluation, not current support capabilities, approved staffing plans or implementation designs.

## Authority and Mandatory Safeguards

Product owns the support-service policy and customer experience intent. Administration may coordinate only approved Domain actions and investigations; [REQ-ADM-013 and 041](../domains/admin/admin-domain.md) preserve purpose limitation, least privilege, attribution and the separate escalation boundary. Customer remains authoritative for Customer data under [REQ-CUS-039–041](../domains/customer/customer-domain.md), while each affected commerce Domain retains its own Order, Payment, Shipping, Return and other facts. Support access to those facts requires current owning-Domain authorization; an issue identifier, customer contact or administrative screen does not confer access or permit arbitrary state manipulation.

Notifications owns governed request, attempt and delivery outcomes under [REQ-NTF-002–004, 012–015 and 037](../domains/notifications/notifications-domain.md); it cannot set support hours or response targets, prove issue resolution or change Customer truth. [Identity REQ-IDN-028](../domains/identity/identity-domain.md) and [SECURITY-STANDARDS.md §§12 and 35](../../.ai/core/SECURITY-STANDARDS.md) preserve trusted contextual Authorization, privacy, minimization, protected contact data and auditable high-risk actions. [CMS REQ-CMS-025](../domains/cms/cms-domain.md) may present approved support policy but cannot create its substance. Support communication must be accurate and accessible; a candidate option must not imply unsupported availability, response or resolution guarantees.

Necessary service communication remains distinct from optional marketing under [Notifications REQ-NTF-013–015](../domains/notifications/notifications-domain.md) and [`PRODUCT.md` §16.7](../../.ai/core/PRODUCT.md). Seeking support, making a purchase, supplying a contact address or receiving a service message does not infer marketing Consent. No support choice changes Consent, lawful-basis, contact-data processing or Notification channel policy. Existing privacy and Security requirements remain binding even where a later support channel is approved.

## Future Evaluation Criteria and Evidence

A separately authorized comparison must consider customer accessibility and usability; attributable customer needs and likely support demand; proposed coverage and availability; clear, supportable response expectations; operational feasibility and staffing capacity; cost and sustainability; consistency across channels; privacy and contact-data handling; Security, identity assurance and owning-Domain Authorization; accurate customer communication; interaction with Customer, Administration and Notifications responsibilities; incident and escalation dependencies; launch readiness; and reversibility. These are criteria, not findings or scores.

Product needs attributable customer journeys and customer-facing intent. Operations and Administration need to examine actual or credibly estimated workload, capacity, issue categories and support handoffs without inventing a production process. Customer and Notifications must review protected data and communication boundaries. UX/accessibility, Engineering and Security must review access, accessibility, system and abuse implications; Privacy/Legal participates where contact data, customer rights or communication duties are materially affected. No measurement, interview, staffing commitment, reviewer approval or service-level evidence is claimed in this record.

## Dependencies, Review and Future Gates

Product leads any later choice. Customer, Administration, Notifications, Operations, UX/accessibility, Engineering and Security are required reviewers; Privacy/Legal and relevant owning Domains participate where the candidate affects their information or customer obligations. Their review is not presumed complete. Item 24 remains a separate unresolved prerequisite for any production escalation process, though it does not prevent framing item 18. Proposed [DEC-0012](DEC-0012-administrative-role-permission-matrix.md) grants no Staff Role or Permission matrix; any protected support action still needs its existing Authorization.

1. **Evidence gathering:** Collect attributable customer, operational, accessibility, security and affected-owner evidence before asserting a feasible service offer.
2. **Comparative evaluation:** Obtain separate authorization before comparing the three candidates. This record does not conduct or authorize the comparison.
3. **Policy selection and acceptance:** Product and applicable affected owners must review a proposed customer-facing policy under [`PRODUCT.md` §§25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md). An Accepted choice must be synchronized into canonical Product guidance where required.
4. **Operational and implementation authority:** Item 24's process, actual staffing, runbooks, providers, Contracts, Architecture where material, Security, Engineering and launch-readiness work require separate governance. An Accepted item 18 policy alone does not implement support.

## Validation Criteria

Review of this Proposed record passes only if: (1) it traces exactly to `PRODUCT.md` §24 item 18 and remains 0.1.0 Proposed, Product-owned and non-authoritative; (2) exactly three alternatives remain unselected and unranked; (3) no current channel, hours, target, guarantee, staffing or platform is claimed; (4) Product, Administration, Customer, Notifications, Identity and owning-Domain authority remains separate; (5) customer communication, privacy, Security, accessibility, Authorization and non-inference of marketing Consent remain explicit; (6) item 24 is still open and no escalation process is selected; (7) required reviewers and evidence needs are stated without fabricated approval; and (8) evaluation, acceptance and implementation retain separate gates.

## Decision Status and Explicit Non-Decisions

DEC-0026 supersedes nothing and is superseded by nothing. It remains **0.1.0 Proposed, Product-owned and non-authoritative**. It selects no support channel, contact address or phone number, operating hours, response-time target, resolution-time guarantee, staffing level or model, escalation route or level, support platform or provider, Notification provider, Customer contact-data processing rule, Consent or lawful basis, Role or Permission, API or Event Contract, database schema, Architecture mechanism, service-level agreement, operational runbook or implementation. It establishes no production support arrangement, comparative result, policy acceptance, new Domain ownership, or authority to access protected Customer, Order, Payment or other records. `PRODUCT.md` §24 items 18 and 24 remain unresolved; no Approved Specification is changed.

## References

- [AGENTS.md §5](../../.ai/core/AGENTS.md), [PRODUCT.md §§16.7, 24 items 18 and 24, 25, 34.3–34.5 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md), and [SECURITY-STANDARDS.md §§12 and 35](../../.ai/core/SECURITY-STANDARDS.md) — decision authority, support need, ownership, terminology and protection.
- [Administration Domain REQ-ADM-013 and 041](../domains/admin/admin-domain.md) and [Backend BADM-REQ-013 and 041](../backend/admin/admin-backend.md) — bounded support and escalation coordination.
- [Customer Domain](../domains/customer/customer-domain.md) and [Notifications Domain REQ-NTF-002–004, 012–015 and 037](../domains/notifications/notifications-domain.md) — Customer source authority and governed communication outcomes.
- [Identity Domain REQ-IDN-028](../domains/identity/identity-domain.md) and [CMS Domain REQ-CMS-025](../domains/cms/cms-domain.md) — access security and approved-policy presentation boundaries.
- [DEC-0012](DEC-0012-administrative-role-permission-matrix.md) — Proposed administrative Role and Permission policy; no support access grant.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed three unselected Customer-support channel and service-expectation alternatives without selecting a channel, operating model or target; preserved Product, Administration, Customer, Notifications and Security boundaries, separately open Product item 24, and distinct evidence, evaluation, acceptance and implementation gates. |
