# GitHub MCP and pull requests

Owner: Engineering. Last verified: 2026-10-15. This is an informational tooling
guide; repository governance remains in [AGENTS.md](../.ai/core/AGENTS.md).

## Configuration

The repository's [Codex configuration](../../.codex/config.toml) registers the
official hosted GitHub MCP server. Codex loads project configuration for trusted
projects. The server handles GitHub repositories, issues and pull requests;
local Git commands continue to handle the working tree, commits and branches.

```toml
[mcp_servers.github]
url = "https://api.githubcopilot.com/mcp/"
bearer_token_env_var = "GITHUB_PAT_TOKEN"
```

For user-wide setup outside this checkout, use:

```sh
codex mcp add github --url https://api.githubcopilot.com/mcp/ --bearer-token-env-var GITHUB_PAT_TOKEN
```

This writes user configuration in `~/.codex/config.toml`. Check for an existing
entry first with `codex mcp get github`; avoid creating duplicate server names.

## Authentication

Supply `GITHUB_PAT_TOKEN` to the process launching Codex. Use a secret manager or
an interactive prompt rather than typing a token into shell history. For zsh:

```sh
read -rs 'GITHUB_PAT_TOKEN?GitHub PAT: '
export GITHUB_PAT_TOKEN
printf '\n'
codex
```

For this Mac's existing `codex-github-pat` Keychain item, launch a new Codex
CLI session from the repository root with:

```sh
./scripts/codex-with-github
```

The [launcher](../../scripts/codex-with-github) uses an existing nonempty
`GITHUB_PAT_TOKEN`, or loads the existing Keychain item for the current user,
then starts Codex with the token exported. It does not create credentials or
write tokens to disk. A missing or empty token stops startup.

A desktop client must inherit the variable too; setting it in an unrelated
terminal does not update an already running app. A `.env` file is not loaded
automatically by this configuration. Never commit tokens, put them in Markdown
or TOML, print credential-helper output, or paste credentials into chat.

Select the intended repository and resource owner when creating a fine-grained
PAT. Grant Pull requests read/write for PR creation and Contents read for
repository inspection; add Contents write if code writes are needed. Issues and
Actions permissions are needed only for the corresponding tasks. Token
permissions do not increase the account's repository privileges, and organization
approval or SSO requirements may also apply.

Three credential paths can coexist:

| Path | Credential source |
| --- | --- |
| Hosted GitHub MCP configured here | `GITHUB_PAT_TOKEN` in the Codex environment |
| Connected GitHub plugin | The plugin's own connection |
| Direct GitHub REST PR creation | Existing `GITHUB_PAT_TOKEN` supplied to the request process |
| Local Git | The configured Git credential helper |

Updating a saved Git token does not update the plugin or the MCP environment.
Do not assume these paths use the same token or have the same effective access.

## Verification

1. Run `codex mcp get github` and `codex mcp list`. Confirm the URL, enabled state
   and environment-variable name; these commands verify registration.
2. Start a new Codex session after configuration or environment changes. Inspect
   `/mcp` or the client's MCP panel and confirm that GitHub tools are available.
3. Request the authenticated identity and read
   `awesomehesman/eCom-Online-Store` and its open PRs. Successful initialization
   and tool discovery alone do not prove repository access or write permission.
4. When a PR is requested, create it once and verify the returned URL, head and
   base. Avoid creating throwaway PRs merely to test credentials.

On 2026-10-15, the existing user-level MCP registration initialized successfully
and listed 46 tools, including `create_pull_request`. Its token differed from
the saved Git credential. Tool counts can change. This check did not validate
MCP PR write access. A subsequent read-only `pull_request_read` call through the
hosted MCP server successfully retrieved open PR #207 from this repository.

## PR workflow and 403 troubleshooting

Review the diff and repository instructions, exclude local editor files, run
appropriate checks, push the intended branch and check for an existing PR for
that head. Target `develop` for normal integration. Include the purpose, scope,
governing references, validation, risks and outstanding work in the PR body.
Use the [PR description standard](pull-requests.md) and repository template.

For `403 Resource not accessible by integration`, first identify which credential
path made the request. Check its repository selection, token expiry, endpoint
permissions and applicable organization restrictions. Account-level admin or
push access does not prove that the credential can call the requested endpoint.
When available, inspect GitHub's `X-Accepted-GitHub-Permissions` response header.
Do not broaden unrelated permissions as a speculative fix.

For authorized PR creation, use the existing `GITHUB_PAT_TOKEN` directly with
GitHub REST when requested, including after a connected-plugin permission error.
Do not substitute the saved Git credential or assume the plugin uses this PAT.
The token must be available to the request process. If it is absent, load it
through the user's existing authorized secret-loading setup; do not create or
replace credentials or change authentication configuration. In the verified
local setup, the shell configuration loads the token from the existing secret
store; a non-interactive shell did not receive it automatically.

Read the token inside the request process and send it only in the HTTPS
Authorization header. Never print it, put it in command arguments or URLs, write
it to a temporary file, or enable shell tracing while loading it. Report a
missing token without exposing secret values.

Before POSTing to `/repos/{owner}/{repo}/pulls`, perform all
[governed-pr-creation preflight checks](../../.kiro/skills/governed-pr-creation/SKILL.md)
from the worktree containing the authorized committed amendment. Verify local
and remote commits, the target comparison, clean tracked state and the absence
of an open PR for the same head and base. An unrelated worktree's modifications
are not the cleanliness check for an isolated-worktree PR. After creation,
retrieve the PR and confirm its number, URL, title, head, base and state. If the
POST outcome is ambiguous, check for the PR before retrying. If the PAT request
fails, report the exact sanitized error and stop; do not rotate credentials or
broaden permissions automatically.

On 2026-10-15, the connected GitHub plugin returned
`403 Resource not accessible by integration` for this PR operation. Following
explicit user authorization, direct GitHub REST requests using the existing
`GITHUB_PAT_TOKEN` successfully created and retrieved
[PR #208](https://github.com/awesomehesman/eCom-Online-Store/pull/208), from
`docs/dec-0012-are-004-inventory-technical-disposition` to `develop`.
This verifies the PAT's ability to create that PR; it does not establish or
repair the separate plugin connection's write permissions.

## References

- [Codex MCP configuration](https://developers.openai.com/codex/mcp/)
- [GitHub MCP setup for Codex](https://github.com/github/github-mcp-server/blob/main/docs/installation-guides/install-codex.md)
- [GitHub REST authentication troubleshooting](https://docs.github.com/en/rest/using-the-rest-api/troubleshooting-the-rest-api)
- [Create a pull request: permissions and API](https://docs.github.com/en/rest/pulls/pulls#create-a-pull-request)

## Hosted MCP diagnostic result

On 2026-10-15, a direct protocol diagnostic against the configured hosted server
using the existing shell-loaded PAT successfully initialized
`github-mcp-server`, discovered 46 tools including `create_pull_request`, and
authenticated through `get_me` as `awesomehesman`. This verifies hosted MCP
authentication and discovery, not PR write access. No test PR was created.

The connected GitHub plugin that returned 403 is a separate connection; this
PAT configuration does not change that plugin. The current session did not
expose the hosted server as native tools. Start a new token-bearing Codex
session and confirm the hosted server in its MCP panel before using it.
