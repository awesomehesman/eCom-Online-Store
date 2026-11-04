# Initial Product Catalogue Contract

## 1. Purpose and Scope

This Product-owned human-readable Contract governs initial anonymous catalogue listing and individual Product evaluation. Version 1.0.0 is paired with [initial-product-catalogue.openapi.yaml](initial-product-catalogue.openapi.yaml), the authoritative executable HTTP wire Contract. This companion governs ownership, lifecycle, visibility, consistency, failure and integration boundaries; it does not compete with OpenAPI for routes, methods, parameters, schemas, headers or response codes. Conflicts are Contract defects. No implementation, release readiness or independent approval is claimed.

## 2. Governing Authority

This Contract is subordinate to [API Standards](../../../.ai/backend/API.md), [Architecture](../../../.ai/core/ARCHITECTURE.md), [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md), [Glossary](../../../.ai/core/GLOSSARY.md), the [Product Domain](../../domains/product/product-domain.md), [Product Backend](../../backend/product/product-backend.md) and [Accepted DEC-0019](../../decisions/DEC-0019-product-variant-attribute-standards.md). Applicable [Product Evaluation frontend requirements](../../frontend/storefront/product-evaluation.md) consume evidence without becoming Product authority.

DEC-0019 records outstanding canonical Product and Decision Index synchronization. This Contract does not perform that synchronization or declare all implementation prerequisites closed. Its representations preserve the Accepted decision's meanings and extension boundary.

## 3. Product Ownership

Product owns stable Product and Variant identity, membership, descriptive content, family context, Attributes, media associations, lifecycle and Product-owned visibility eligibility. Category owns taxonomy and classification. Search owns queries, facets, ranking and indexing. Pricing and Inventory retain commercial and stock authority. No response establishes final purchasability, Customer eligibility, Cart acceptance or Checkout readiness.

## 4. Public Access and Trust Boundaries

Only currently customer-visible Product information is public. Anonymous reads require no authenticated Session and must not create or renew one. This declaration does not open administrative routes or authorize mutation, publication, archival or access to protected content. Path identifiers, pagination and correlation inputs remain untrusted. Stable public identifiers are opaque references distinct from persistence keys and carry no authority.

## 5. Listing Semantics

Listing is a Product-owned catalogue read, not Search. It returns only evidence currently confirmed eligible by Product. No Category filters, search terms, ranking, arbitrary sorting or expansions are supported. Empty eligible results are a successful empty collection. Dependency failure must not be disguised as an empty catalogue; pagination must operate over eligible evidence rather than leak counts or positions of protected records.

## 6. Details Semantics

Details preserve Product identity and include only eligible Variant/media evidence associated with that Product. Unknown and authoritatively non-visible Products have the same safe external outcome; Draft, Archived, unpublished, withdrawn and invalid content is concealed. Authoritative unavailability is different from confirmed absence: it must withhold representation and use the failure mapping in OpenAPI, without disclosing protected state.

## 7. Publication and Visibility Eligibility

Published lifecycle state alone is insufficient. Before returning a Product, Product must confirm applicable identity, description, Attributes, media, lifecycle and validation requirements, including REQ-PRD-014–020, 021–024 and 025–029. Publication must have governed provenance and authorization. Incomplete, stale, contradictory or uncertain evidence cannot grant visibility. An unrelated Domain, cache, index, UI or projection cannot publish or reactivate Product truth.

Evaluate Variants and media independently. Exclude an ineligible child, but withhold the entire Product when that exclusion leaves an applicable Product completeness/publication requirement unsatisfied. This Contract sets no universal minimum media or Variant count: empty collections are permitted only when Product's applicable rules permit them. Omission must not conceal a mandatory missing requirement and falsely imply completeness.

## 8. Variant and Attribute Semantics

Variants belong to exactly one Product and retain stable identity independently of labels, values, Price and Inventory. Family context is Apparel, Footwear or Accessories under DEC-0019. All emitted values must belong to an applicable governed standard version.

Size and Colour define configurations where applicable; Fit and Material are initially descriptive and cannot distinguish Variants. The representation declares the Product's applicable defining concepts. Every emitted Variant supplies exactly those concepts; omission means confirmed non-applicability, never unknown data. Accessories need not have both Size and Colour. Applicability for other families remains Product-owned rather than invented by this Contract.

Apparel sizes, UK whole/half footwear sizes and One Size preserve DEC-0019 semantics. The initial Colour code BLACK has label Black; casing aliases do not create new configurations. Other colour/accessory-size meanings require explicit Product extension approval. Open wire representations for governed extensions are not permission to accept arbitrary values. Canonical defining values must be semantically unique within the Product, including concurrent accepted state. Labels, serialization and changed standard versions cannot reset uniqueness or replace identity.

## 9. Product Media Eligibility

Only Product-confirmed public media may be represented. Rights, security, quality, accuracy and accessibility checks remain necessary. Preserve Product/Variant association and governed order. Media must not reference a different Product's Variant. Required text alternatives must be meaningful; a generated filename is not sufficient. Private, Draft, failed, misleading, stale or unauthorized media is omitted or causes Product withholding where completeness requires it. Public references must contain no credentials or private storage identifiers. This Contract selects no storage provider, upload, transformation or delivery pipeline.

## 10. Pagination and Ordering

The paired OpenAPI defines bounded cursor pagination and its numeric limits as this capability's wire-level work bounds, not business assortment policy. Ordering uses stable public Product references; mutable names, Category and Search rank do not determine traversal. A continuation is untrusted navigation context, not a snapshot or visibility grant. It carries no protected-record data. Unsupported parameters and malformed continuation requests fail safely.

## 11. Consistency and Freshness

Each response must use a coherent Product-owned observation and confirm eligibility for all included content at the read decision. Revalidate on every request, including continuation requests. Do not combine conflicting Product/Variant/standard observations into fabricated current truth. Intervening withdrawal must invalidate positive reliance before response authorization or cause withholding.

Pagination is live, not a cross-request snapshot: newly eligible records before the cursor can be missed; changed content is current when read. Clients restart listing when a refreshed traversal is needed. Previously returned data is historical evidence, not perpetual visibility authority. No guarantee can retract bytes already delivered. Server/proxy caches must not override current visibility; the wire Contract requires no-store and supplies no conditional-response path.

## 12. Failure and Uncertainty

Distinguish malformed input, concealed absence/non-visibility, authoritative dependency unavailability and unexpected failure internally. Fail closed without partial success or silent omission of unresolved evidence. Do not return stale content as a fallback or report uncertain state as confirmed absence. Failure does not authorize data repair, mutation or downstream rollback. OpenAPI owns safe HTTP/RFC 9457 mappings.

## 13. Security and Privacy

Apply least privilege, input bounds, explicit DTO mappings and purpose limitation. Exclude internal keys, persistence structures, private media, audit details, Draft content, credentials, SQL/constraint names and stack traces. Unknown and non-visible details must not become existence oracles through errors or telemetry. Public media URLs and text content require safe output handling. No Idempotency Key is required for these safe reads.

## 14. Observability

Propagate safe bounded correlation; client correlation values never establish identity or authorization. Record templated routes, outcome category, duration and dependency degradation without raw cursors, private identifiers, media credentials or unnecessary content. Ordinary read logs are not automatically Audit Records. Existing proportional audit obligations for mutations/publication are unaffected.

## 15. Traceability

| Concern | Governing requirements | Contract treatment |
| --- | --- | --- |
| Identity and membership | REQ-PRD-003, 007–010; BPRD-REQ-012–016 | Stable public references, owning Product and eligible Variants |
| Attributes/evolution | REQ-PRD-009–013; BPRD-REQ-017–019; DEC-0019 | Canonical family/version-bound values, explicit applicability, semantic uniqueness |
| Media/publication | REQ-PRD-014–020, 025–029; BPRD-REQ-020–023, 027–030 | Positive Product-owned confirmation, independent child checks, completeness gate |
| Visibility/non-purchasability | REQ-PRD-021–024; BPRD-REQ-024, 031–036 | Fail-closed reads; no commerce or Search authority |
| Ports/Contracts | BPRD-REQ-008–010, 038–040, 045 | Owner boundary, DTOs, bounded wire Contract |
| Failure/security/testing | BPRD-REQ-042–046; API §§17, 36, 63–65 | Safe errors, current evidence, correlation and executable validation |
| Storefront consumption | REQ-FPE-005–010, 012–019, 025–028 | Non-authoritative presentation and safe degradation |

## 16. Explicit Non-Decisions

No Product creation/admin API, publication workflow, lifecycle transition graph, media technology, SKU/database identifier format, schema, migration, Category taxonomy, Search filter, Pricing/Inventory capability, market policy, review/rating capability, provider or infrastructure selection is introduced. Wire bounds are not new Product completeness thresholds. No new colour/size extension is approved by serialization support. Existing Contract and governing-source review requirements remain applicable before release.

## 17. Acceptance and Validation Criteria

1. Both operations are explicitly anonymous safe reads; protected routes remain protected.
2. Listing and details expose no Draft/Archived/withdrawn/ineligible data, even when lifecycle alone says Published.
3. Unknown and authoritatively non-visible details share concealment behavior; unavailable evidence never becomes absence or empty success.
4. Ineligible children are removed only when remaining Product completeness is still satisfied; cross-Product Variant/media substitution fails closed.
5. Applicable canonical Size/Colour values and standard versions obey DEC-0019; missing required values, unknown codes and duplicate configurations are rejected before positive reliance.
6. Cursor boundaries, deterministic order, page-size limits, malformed/unknown parameters and empty/end-of-list cases follow OpenAPI.
7. Withdrawal/concurrent changes and dependency failures cannot turn stale observations into current visibility.
8. Errors use RFC 9457, bounded correlation and no private implementation details; all responses follow freshness/cache rules.
9. No Pricing/Inventory fields, arbitrary filter language, Session requirement or Idempotency-Key requirement appears.
10. Schemas, examples and companion semantics remain synchronized; validate positive/negative/uncertainty, security, integration and compatibility scenarios against the executable Contract. Artifact creation alone claims no executed implementation tests or governing-source synchronization.
