# DEC-0025 — Marketing Consent and Communication Preferences Policy

- **Identifier:** DEC-0025
- **Title:** Marketing Consent and Communication Preferences Policy
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

[`PRODUCT.md` §24 item 19](../../.ai/core/PRODUCT.md) leaves the “Marketing-consent and communication-preference model” unresolved. Its §16.7 requires a defined purpose, separates Consent from transactional necessity, and forbids inferring marketing Consent from purchase. A Product decision is needed to define the intended customer-facing choices for optional marketing communications and the relationship between those choices and communication Preferences. Existing Approved Requirements establish protective boundaries but do not select a marketing model.

[Customer Domain REQ-CUS-018–021](../domains/customer/customer-domain.md) and [Customer Backend BCUS-REQ-026–028](../backend/customer/customer-backend.md) preserve Customer ownership of supported Preferences and purpose-bound Consent, including current outcome, change or withdrawal evidence. They leave categories, defaults, lawful basis, wording, retention, evidence representation and communication rules unresolved. [Notifications Domain REQ-NTF-012–015](../domains/notifications/notifications-domain.md) and [Notifications Backend BNTF-REQ-012–014](../backend/notifications/notifications-backend.md) require current governed recipient and permission context, distinguish transactional from marketing communication, and preserve missing, stale, conflicting or unresolved classification and permission as uncertainty rather than silently sending.

## Decision Question and Policy Scope

What initial Product policy should govern optional marketing communication choices and Customer communication Preferences, while preserving purpose-bound permission, withdrawal, transactional/service communications, Customer source authority, qualified lawful-basis review and safe Notification delivery?

**NO OPTION SELECTED.** Marketing Consent concerns permission for a defined purpose under applicable governance; a Preference is a Customer-specific choice influencing experience, and a Notification Preference may concern an eligible non-mandatory topic or channel. Neither a Preference nor channel selection automatically establishes Consent. Lawful basis and processing permission require qualified Privacy/Legal review. Message purpose and transactional-versus-marketing classification are separate from Authentication, Principal identity, contextual Authorization and the mechanism that attempts delivery. This record establishes no equivalence among those concerns and no legal conclusion.

## Existing Authority and Ownership

| Authority | Approved boundary preserved |
| --- | --- |
| Product | Owns the unresolved customer experience and optional-marketing policy choice under `PRODUCT.md` §§16.7 and 24 item 19, subject to applicable approval. |
| Customer | Owns Customer, Account, contact, Consent, Preference and Notification Preference truth under [REQ-CUS-018–021](../domains/customer/customer-domain.md) and [BCUS-REQ-026–028](../backend/customer/customer-backend.md). A change, withdrawal or restriction must not be inferred from unrelated activity or overwritten by a consumer. |
| Notifications | Owns request, template, attempt, retry and delivery outcomes under [REQ-NTF-002–004, 012–015](../domains/notifications/notifications-domain.md) and [BNTF-REQ-008, 012–014](../backend/notifications/notifications-backend.md); it consumes governed Customer permission context but does not own Consent, Preference or marketing policy. Delivery and provider acceptance do not prove permission. |
| Identity and owning-domain Authorization | Identity owns Authentication, Principal and access-security evidence; Customer and Notifications retain their own resource/action authorization under [Identity REQ-IDN-007 and 028](../domains/identity/identity-domain.md) and the [Identity Backend Specification](../backend/identity/identity-backend.md). A contact address, Account, Session, Role label or client assertion is not a Consent or access grant. |
| CMS | May present governed policy and editorial content but cannot decide marketing policy or own Consent or Preference truth under [CMS REQ-CMS-025–026](../domains/cms/cms-domain.md) and the [CMS Backend Specification](../backend/cms/cms-backend.md). |
| Security and Privacy/Legal | Preserve purpose limitation, minimization, required Consent or other lawful basis where applicable, protected access, retention and data-subject handling under [SECURITY-STANDARDS.md §35](../../.ai/core/SECURITY-STANDARDS.md). This Product record does not determine legal sufficiency. |

## Alternatives Considered — Not Formally Compared

| Option | Initial policy candidate | Potential benefit | Limitation or trade-off requiring evidence |
| --- | --- | --- | --- |
| A — Narrowly scoped optional-marketing choice | Consider one bounded customer-facing choice for an expressly approved optional-marketing purpose. | May be easier for customers to understand and for owners to govern initially. | A broad choice could obscure materially different purposes; its actual purpose, scope and lawful basis remain unselected. |
| B — Topic-specific optional-marketing choices | Consider separate choices for distinct optional-marketing topics where Product and Privacy/Legal evidence justifies each distinction. | May better reflect purpose specificity and customer control. | More choices can increase comprehension, evidence and suppression complexity; no topic or category is selected. |
| C — Topic choices with separately governed channel Preferences | Consider topic-specific optional-marketing choices alongside distinct choices for eligible non-mandatory communication channels. | May allow more precise customer communication choices if lawful and operationally supportable. | Topic permission and channel Preference can be confused, become inconsistent or create delivery complexity; no channel, fallback or permission inheritance is selected. |

All three alternatives are **unselected, unranked, non-authoritative** candidates for later separately authorized evaluation and approval. None automatically supplies a lawful basis, marketing permission, an opt-in or opt-out default, a channel entitlement, or permission to send.

## Mandatory Safeguards and Communication Separation

Marketing Consent must not be inferred from purchase, Account creation, Wishlist activity, a Preference, inactivity, prior delivery or an available contact value. Optional marketing remains distinct from necessary transactional or service communication under [REQ-NTF-013–015](../domains/notifications/notifications-domain.md). Transactional classification cannot bypass purpose limitation, recipient isolation or other governing restrictions; a marketing label cannot bypass applicable permission. No universal rule that Consent is the sole lawful basis is selected: the applicable lawful basis and required customer choice must be assessed by qualified Privacy/Legal reviewers before acceptance.

Any later Approved policy must preserve purpose-bound Customer authority and reliable evidence of the applicable permission, current outcome, change and withdrawal. Withdrawal and governed suppression must be effective for later decisions about optional communication; a stale or conflicting copy must not restore permission. When purpose, classification, Consent, Preference, suppression or channel eligibility is missing, stale, conflicting or uncertain, Notifications must retain the uncertainty and must not silently authorize marketing delivery. Customer-owned truth cannot be replaced by CMS content, a Notification attempt, a provider response or a projection.

Collection and use of personal data remain minimized, protected and limited to an approved purpose. Protected Consent and Preference access or mutation requires current trusted identity context and owning-domain Authorization, with appropriate isolation and proportional Audit Records. Evidence and auditability must support accountable review without exposing unnecessary personal data. No particular evidence format, suppression mechanism, retention period or legal conclusion is prescribed here.

## Future Evaluation Considerations and Evidence

A future Product-led comparison must examine customer comprehension, purpose specificity, withdrawal and suppression, attributable evidence and auditability, privacy and security, operational complexity, cross-channel consistency, accessibility, qualified regulatory and legal review, and existing Customer, Identity, CMS and Notifications boundaries. It must distinguish a Product-intended experience from actual customer permission and from delivery capability. No comparison, ranking, measurement, customer research, stakeholder approval or legal finding is claimed by this record.

Evidence needed before comparison includes attributable Product purposes and customer scenarios; Customer ownership and currentness cases for permission, change and withdrawal; Notifications classification, uncertainty, suppression and failure cases; accessible customer-choice review; and Security, Privacy/Legal and Operations assessments of exposure, lawful processing, accountability and support. Where a communication owner proposes a service message or marketing campaign, that owner must provide its purpose and classification evidence without creating policy through a template or send request. Provider and external-system implications require later separate review if a candidate makes them relevant.

[DEC-0022](DEC-0022-reporting-exports-policy.md) remains Proposed and supplies no permission to reuse Customer data for reports or exports; [DEC-0023](DEC-0023-wishlist-behaviour-policy.md) remains Proposed and supplies no marketing Consent, Wishlist-alert right or Notification policy. Neither is an acceptance prerequisite for drafting this question, and neither is treated as Accepted authority.

## Review, Decision and Implementation Gates

Product owns the eventual policy choice. Customer and Notifications must review source and delivery boundaries; Identity and Engineering must review trusted access and feasible separation; CMS must review presentation without policy ownership. Security and Privacy/Legal must review purpose, lawful basis, notice, customer choice, minimization and applicable processing constraints. Operations and relevant communication owners must review support, classification, withdrawal and suppression consequences. Consultation is not an assertion that any reviewer has approved a policy.

1. **Evidence gathering:** Assemble attributable Product, affected-domain, customer-experience, Security and Privacy/Legal evidence. This record claims no evidence gate satisfied.
2. **Comparative evaluation:** Obtain separate authorization before formally comparing the three candidates. This Proposed record does not authorize that evaluation.
3. **Policy selection and acceptance:** A Product selection, affected-owner review and applicable consent/privacy approval must follow [`PRODUCT.md` §§25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md). Accepted policy must be synchronized into canonical Product guidance where required.
4. **Specification and implementation changes:** An Accepted policy alone would not authorize delivery. Applicable Customer, Notifications, Identity, CMS, Security, Contract, Architecture where material, provider and Engineering changes require separate governance.

## Validation Criteria

Review of this record passes only if: (1) its metadata remains 0.1.0 Proposed, Product-owned and non-authoritative; (2) exactly three policy alternatives remain unselected and unranked; (3) Consent, Preference, Notification Preference, lawful basis, message classification, Identity/Authorization and delivery mechanism remain distinct; (4) no purchase, Account, Wishlist, contact value, selected channel or delivery is treated as marketing permission; (5) Customer, Notifications, Identity and CMS authority remains with its Approved owner; (6) withdrawal, suppression, uncertainty, privacy, security and audit safeguards remain visible; (7) Product item 19 remains open; and (8) evidence, comparative evaluation, acceptance and implementation remain separate gates without a selected mechanism or legal conclusion.

## Decision Status and Explicit Non-Decisions

DEC-0025 supersedes nothing and is superseded by nothing. It remains **0.1.0 Proposed, Product-owned and non-authoritative**. No alternative is selected, ranked, formally compared or accepted. `PRODUCT.md` §24 item 19 remains unresolved; no Approved Domain or Backend Specification changes. This record selects no marketing category, communication topic, channel availability, Consent default, opt-in or opt-out mechanism, checkbox or customer-facing wording, lawful basis, evidence format, retention period, suppression implementation, provider, API or Event Contract, data schema, delivery fallback, send rule, technical Architecture or implementation. It creates no new approval, marketing delivery, access grant or evaluation authorization.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§16.7, 24 item 19, 25 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [GLOSSARY.md §§7 and 25](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md), and [SECURITY-STANDARDS.md §35](../../.ai/core/SECURITY-STANDARDS.md) — governance, ownership, terminology and privacy.
- [Customer Domain REQ-CUS-018–021](../domains/customer/customer-domain.md) and [Backend BCUS-REQ-026–028](../backend/customer/customer-backend.md) — Customer-owned Preference and Consent boundaries.
- [Notifications Domain REQ-NTF-012–015](../domains/notifications/notifications-domain.md) and [Backend BNTF-REQ-012–014](../backend/notifications/notifications-backend.md) — delivery, recipient, permission and classification boundaries.
- [Identity Domain REQ-IDN-007 and 028](../domains/identity/identity-domain.md) and [Backend](../backend/identity/identity-backend.md); [CMS Domain REQ-CMS-025–026](../domains/cms/cms-domain.md) and [Backend](../backend/cms/cms-backend.md) — access security and policy-presentation boundaries.
- [DEC-0022](DEC-0022-reporting-exports-policy.md) and [DEC-0023](DEC-0023-wishlist-behaviour-policy.md) — related Proposed decisions, not accepted permission or policy.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed three unselected Marketing Consent and communication-Preference policy alternatives while preserving Customer source authority, Notifications delivery boundaries, transactional separation, purpose and privacy safeguards, unresolved Product item 19, and separate evidence, evaluation, acceptance and implementation gates. |
