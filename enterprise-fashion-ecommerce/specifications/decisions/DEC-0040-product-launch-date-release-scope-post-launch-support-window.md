# DEC-0040 — Product Launch Date, Release Scope and Post-Launch Support Window

- **Identifier:** DEC-0040
- **Title:** Product Launch Date, Release Scope and Post-Launch Support Window
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

[`PRODUCT.md` §24 item 30](../../.ai/core/PRODUCT.md) leaves “Product launch date, release scope, and post-launch support window” unresolved. Product [§28.6](../../.ai/core/PRODUCT.md) describes Reporting and Launch Readiness work, including accessibility, performance, Security, backup/restore, runbooks, support and production controls; it selects no date or release baseline. Product [§35](../../.ai/core/PRODUCT.md) requires applicable problem, experience, business-rule, operational and launch gates before a capability is released. [§27.4](../../.ai/core/PRODUCT.md) prohibits trading away correctness, Security, accessibility or recoverability for feature volume. These Approved constraints are readiness requirements, not evidence that a release is ready.

Product [§34.4](../../.ai/core/PRODUCT.md) requires detailed support channels, hours, response targets, escalation levels and customer communication templates before launch. [DEC-0026](DEC-0026-customer-support-channels-service-expectations-policy.md) leaves the customer-facing support offer Proposed; [DEC-0027](DEC-0027-production-customer-service-operational-escalation-process.md) leaves internal service and escalation policy Proposed. Neither record establishes coverage, staffing or a production process. A proposed launch plan must not convert their alternatives into commitments.

The [AGENTS.md §§27.5–27.6](../../.ai/core/AGENTS.md) release and production gates, [ARCHITECTURE.md §§43 and 45](../../.ai/core/ARCHITECTURE.md) rollout and operational-readiness boundaries, [SECURITY-STANDARDS.md §40](../../.ai/core/SECURITY-STANDARDS.md) review gates, [TESTING-STANDARDS.md §§39–40](../../.ai/core/TESTING-STANDARDS.md) release and post-deployment verification, and [DOCUMENTATION-STANDARDS.md §§29–30](../../.ai/core/DOCUMENTATION-STANDARDS.md) runbook and incident-record expectations remain separate and binding. The Approved [Administration](../domains/admin/admin-domain.md), [CMS](../domains/cms/cms-domain.md) and [Reporting](../domains/reporting/reporting-domain.md) Domain Specifications support their own governed readiness and presentation responsibilities but select no launch date, scope or support window.

## Decision and Current Proposed Position

What Product policy should govern the initial launch date, release scope and post-launch support window, subject to independently verified readiness and release authority?

**NO ALTERNATIVE SELECTED.** This 0.1.0 Proposed, Product-owned record is non-authoritative. It frames three concerns without establishing a launch commitment. `PRODUCT.md` §24 item 30 remains open; no comparison, acceptance, release or implementation is authorized.

## Distinct Unresolved Decision Questions

1. **Product launch date:** Should a date eventually be fixed, conditional on verified gates, or deferred until evidence exists? What external communications or commitments would follow, who must approve them, and what conditions would require replanning? No calendar date, deadline or milestone is set.
2. **Release scope:** Should the release baseline eventually be fixed, phased, or determined by verified capability readiness? Which Product and operational capabilities, exclusions and deferrals would need explicit approval, and how would customer-facing implications be explained? No Version 1 feature commitment, entitlement or launch audience is selected.
3. **Post-launch support window:** Should a heightened observation or stabilization period be adopted, and if so how would it relate to monitoring, incident response, rollback, handover, customer support and operational escalation? No duration, coverage, staffing, on-call rota, service target, support channel or escalation owner is chosen.

Each concern may need distinct evidence and approval. A decision about one cannot silently resolve the others.

## Alternatives Considered — Not Formally Compared

| Option | Conditional planning approach | Potential benefit | Limitations and evidence needed |
| --- | --- | --- | --- |
| A — Fixed Launch Plan, Subject to Governance Gates | A date, approved scope and support window could be established through a separate Product process, while mandatory release and production gates still control execution. | Could give stakeholders a common planning reference if delivery evidence supports it. | A fixed plan could become misleading as readiness changes. Requires scope, capacity, dependency, contingency, customer-communication and gate evidence; a date cannot waive a failed gate. |
| B — Readiness-Gated Launch Plan | Date, scope and support arrangements could be determined against verified readiness criteria and separately approved Product commitments. | Could align commitments with demonstrated capability and operational evidence. | Timing and external communication may remain uncertain. Requires agreed evidence, accountable reviews, explicit release scope and a plan for unmet criteria or delay. |
| C — Phased or Incremental Launch Plan | Timing and scope could be structured into separately governed increments, each with suitable support and operational readiness. | Could limit exposure while learning from bounded release evidence. | Adds coordination, compatibility, customer-communication and repeated readiness demands. Each increment requires its own approved scope and applicable gates; no increment or rollout mechanism is selected. |

These three approaches are **illustrative, conditional, unselected, unranked and non-authoritative**. They are not an exhaustive or approved launch model, and none bypasses a mandatory gate.

## Evaluation Criteria and Evidence Required

A separately authorized comparison would need attributable evidence for Product value and commercial readiness; explicit capability scope and exclusions; dependencies on unresolved Product decisions; Engineering delivery confidence; test, quality, accessibility, Security and Privacy evidence; qualified Legal review where applicable; operational and support capacity; truthful customer and stakeholder communication; incident, rollback, recovery, backup and monitoring readiness; schedule uncertainty and reversibility; cost and support burden. Evidence must distinguish a forecast from a verified result and identify its source, limitations and owner. This record contains no scores, measurements, readiness claims or preferred approach.

## Consequences and Risks

An eventual governed launch policy could clarify sequencing, customer expectations and accountable release decisions. Premature date or scope claims could create unsupported commercial commitments, pressure to bypass gates, inaccessible or insecure journeys, unserviceable orders or uncertain financial outcomes. An unsupported post-launch support promise could mislead Customers and overwhelm Operations. Deferral or phasing could also affect business goals, cost and communication. These are conditional consequences to assess, not findings or accepted trade-offs.

## Security and Privacy Impact

The [Security Standards §40](../../.ai/core/SECURITY-STANDARDS.md) pre-production, production and high-risk release gates remain binding, including applicable approval, threat-model, artifact, access, monitoring and exception requirements. Product [§35.5](../../.ai/core/PRODUCT.md) calls for applicable Security, Privacy/Legal and accessibility review. No launch plan may infer approval from a Proposed decision, incomplete scan, unreviewed exception or unsupported customer-data practice. This record selects no risk acceptance, Security Exception, incident action or Privacy conclusion.

## Data, Compatibility and Migration Impact

No data migration, schema, Contract or provider is selected. An eventual scope and rollout plan must account for historical commercial data, compatible deployment, external side effects, backup/restore and recovery under [Architecture §43](../../.ai/core/ARCHITECTURE.md). A deployable artifact does not prove that rollback is safe. Applicable migration rehearsal, release evidence and post-deployment validation remain independently required.

## Operational and Support Impact

[Architecture §45](../../.ai/core/ARCHITECTURE.md) requires critical operational assets such as dashboards, alerts, runbooks, health indicators, ownership, escalation, reconciliation and recovery. [AGENTS.md §§27.5–27.6](../../.ai/core/AGENTS.md) additionally require known release scope, traceable artifacts, tests, observability, rollback, validated production configuration and an available responsible owner during an agreed observation period. [Testing Standards §§39–40](../../.ai/core/TESTING-STANDARDS.md) require release evidence and bounded post-deployment validation. This record supplies none of those assets or evidence and sets no observation period. Operations and Support review capacity and handover only within separately approved authority.

## Dependencies and Required Review

Product leads the item 30 policy question, with Engineering, Architecture, Operations, Support, Security, Privacy/Legal, Accessibility, Finance and other affected business and Domain owners reviewing their respective concerns. [DEC-0026](DEC-0026-customer-support-channels-service-expectations-policy.md) and [DEC-0027](DEC-0027-production-customer-service-operational-escalation-process.md) remain Proposed; support expectations under Product §34.4 require separate resolution before launch. Other unresolved Product release-capability decisions may affect scope and must be reconciled where included, rather than treated as accepted or universally blocking initial drafting. Administration may coordinate only governed operations; CMS may present approved launch content; Reporting may provide derived measures without setting launch truth. Listing reviewers or dependencies claims no review, approval, capability or evidence.

## Separate Governance Gates

1. **Initial Proposed drafting:** This record frames the three questions and illustrative approaches only.
2. **Evidence collection:** Requires separate authorization and attributable Product, delivery, test, Security, accessibility, commercial and operational evidence.
3. **Comparative evaluation:** Requires separate authorization; no comparison, ranking or recommendation occurs here.
4. **Product selection and acceptance:** Requires Product and affected-owner approval and any necessary synchronization of governing Product authority.
5. **Architecture and operational readiness:** Requires independent verification of compatible release structure, support, monitoring, incident response, recovery and rollback.
6. **Release and production approval:** Requires all applicable Product, AGENTS, Security, Testing and operational gates to pass or a valid governing exception where permitted.
7. **Deployment, enablement and post-launch execution:** Require separate authorization and verification; accepting a Product Decision alone never permits production launch.

Passing one gate does not pass the next.

## Governance Boundaries and Explicit Non-Decisions

DEC-0040 supersedes nothing and is superseded by nothing. It selects no launch date or deadline; release scope, feature commitment, launch geography, channel or audience; deployment or rollout mechanism, release train or cadence; production-readiness finding; support or stabilization window; staffing, on-call schedule, escalation ownership, SLA, SLO or service guarantee; support provider or customer-facing channel; incident or rollback authorization; Product policy acceptance; Architecture, Specification or Contract change; implementation, deployment or production enablement. It authorizes no comparative evaluation and cannot waive existing release or production gates.

## Validation Criteria

This initial proposal is valid only if it (1) traces exact `PRODUCT.md` §24 item 30 wording; (2) remains 0.1.0 Proposed, Product-owned and non-authoritative; (3) keeps date, scope and support window distinct and unresolved; (4) contains exactly three conditional, unselected, unranked illustrative approaches; (5) preserves DEC-0026, DEC-0027, Product §34.4 and all readiness gates without claiming evidence; (6) invents no date, scope, service commitment, staffing or launch approval; and (7) keeps evaluation, acceptance, Architecture, operational readiness, release authorization and execution separate.

## References

- [AGENTS.md §§27.5–27.6](../../.ai/core/AGENTS.md), [PRODUCT.md §§24 item 30, 27.4, 28.6, 34.4 and 35](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §§43 and 45](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [SECURITY-STANDARDS.md §40](../../.ai/core/SECURITY-STANDARDS.md), [TESTING-STANDARDS.md §§39–40](../../.ai/core/TESTING-STANDARDS.md) and [DOCUMENTATION-STANDARDS.md §§29–30](../../.ai/core/DOCUMENTATION-STANDARDS.md) — governing Product and release constraints.
- [Administration Domain](../domains/admin/admin-domain.md), [CMS Domain](../domains/cms/cms-domain.md) and [Reporting Domain](../domains/reporting/reporting-domain.md) — governed coordination, presentation and derived measures without launch-policy authority.
- [DEC-0026](DEC-0026-customer-support-channels-service-expectations-policy.md) and [DEC-0027](DEC-0027-production-customer-service-operational-escalation-process.md) — related Proposed, non-authoritative support-policy and operational-escalation decisions.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-14 | Proposed | Initially framed launch date, release scope and post-launch support window as distinct unresolved Product concerns with three conditional, unselected approaches; preserved support dependencies and separate readiness, evaluation, acceptance, release and implementation gates. |
