# Pull-request descriptions

Owner: Engineering. Updated: 2026-10-15.

Use the following structure for future PRs, following the description supplied
for [PR #207](https://github.com/awesomehesman/eCom-Online-Store/pull/207).
Apply the existing [AGENTS.md §13](../.ai/core/AGENTS.md) requirements; this guide
explains the description format without changing approval or merge authority.
The [repository template](../../.github/pull_request_template.md) supplies these
headings automatically for new PRs where GitHub loads that template.

For the controlled Kiro workflow, use the
[governed-pr-creation skill](../../.kiro/skills/governed-pr-creation/SKILL.md).
ChatGPT validates scope and authorizes PR creation; Kiro inspects committed,
pushed changes and creates the PR; the user stages, commits, pushes, reviews and
merges. Kiro must stop on failed preflight or GitHub permission errors and must
not modify files, branches or authentication, or switch to unauthorized
credentials. The skill's invocation example is a template, not live authorization.

## Summary

Lead with the concrete outcome and why it matters. Identify the affected
requirement, specification, Decision Record or evidence item. Keep it concise.

## Changes

Use specific bullets describing the final diff. Include affected document
versions, statuses, confirmation dates, sections and revision-history entries
when relevant. Explain ownership and Authorization boundaries, safeguards,
scope exclusions and unresolved policy where the change concerns them.

## Governance

Describe what the change establishes and which reviews or decisions remain
outstanding. For evidence changes:

- Identify the attributable source, date, scope and confirming capacity.
- Distinguish accountable business-owner evidence from independent technical,
  Security and Identity approval. Claim only approvals actually recorded.
- Preserve the precise status of affected requirements, Decision Records,
  unresolved policy, gate conditions and comparison eligibility.
- State whether any Role, Permission, API, Contract, workflow or implementation
  authority is established. Do not imply authority from a business confirmation.

For implementation changes, describe applicable security, privacy,
accessibility, data, migration and operational impact, significant design
decisions, rollout or rollback needs and known limitations. Scale detail to the
change and satisfy the repository constitution's required PR content.

## Validation

List verification actually performed and the results. Include exact commands
where applicable, metadata/version checks, provenance preservation and checks
of whether Approved specifications or implementation files changed. State any
relevant checks not run and the reason.

Validate file counts and scope against the committed PR diff, not uncommitted
workspace changes. For example, PR #207's statement that exactly one
documentation file changed applies to its published diff; later local tooling
and documentation edits are separate work until committed and pushed.

Do not copy PR #207's version, Pending reviews, gate results or validation
claims into unrelated PRs. Recheck every assertion against the actual change.
Use clear bullets and preserve canonical terminology and exact status values.

## Outstanding Items

Identify relevant unresolved dependencies without inventing blockers or
resolving existing decisions implicitly. Use this section when applicable.

## Review Notes

Highlight specific reviewer considerations such as authority boundaries,
evidence provenance, version consistency, decision lifecycle, Security
constraints and cross-domain dependencies. Omit genuinely inapplicable sections
for non-governance changes, but retain Governance for governance changes.
