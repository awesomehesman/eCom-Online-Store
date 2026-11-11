# Local backend development

This setup exercises the existing anonymous catalogue only. It grants no Staff,
publication or operational commerce authority. No Product data is seeded.

## Prerequisites

Java 21, the committed Gradle 8.14.5 wrapper, Docker Engine/Desktop with Compose v2,
network access to the approved dependency repositories and PostgreSQL image, and
free localhost ports 3310, 5432, 8080 and 4200. Frontend prerequisites are documented in
`../frontend/README.md`. Use the existing npm lockfile/dependencies.

From this `backend/` directory, generate development-only secrets in one terminal:

```sh
export LOCAL_DB_OWNER_PASSWORD="$(openssl rand -hex 32)"
export LOCAL_DB_PASSWORD="$(openssl rand -hex 32)"
export PRODUCT_CATALOGUE_CURSOR_SECRET="$(openssl rand -base64 32)"
# On macOS, if Java 21 is not already selected:
export JAVA_HOME="$(/usr/libexec/java_home -v 21)"
docker compose -f compose.local.yaml config --quiet
docker compose -f compose.local.yaml up -d --wait
./gradlew bootRun --dependency-verification=strict --args='--spring.profiles.active=local'
```

Keep these values private and stable for the lifetime of this local database.
Do not print resolved Compose configuration, commit an environment file, or use
real production credentials. A new terminal must receive the same database
passwords through your local secret-handling process. Regenerating shell values
does not change passwords inside an existing volume.

PostgreSQL 18 matches existing Testcontainers expectations. Its named volume is
mounted at `/var/lib/postgresql`. The SQL bootstrap runs only on a fresh
volume: it creates a non-superuser runtime account and default grants for objects
created by the migration owner. It does not create Domain tables or insert data.
The bootstrap owner is local-only; it is not a production credential design.

The explicit `local` profile binds HTTP to 127.0.0.1:8080. Flyway uses the separate
migration account and applies existing classpath migrations V1–V12 at startup;
normal runtime connections lack schema-creation privileges and access to the
`public` schema containing Flyway history. Domain schemas remain accessible via
the migration owner's default grants. Flyway clean is
disabled. Spring Session retains `identity.spring_session` and automatic Session
schema initialization remains disabled. Never alter applied migrations. Review
later migrations for compatibility with the local runtime grants.

The cursor secret must be standard Base64 of exactly 32 random bytes. It is not a
Staff credential or publication grant. Changing it invalidates existing cursors;
there is no fallback key or weakened validation.

## Anonymous smoke test

In a second terminal, with the backend running:

```sh
curl --silent --show-error --include http://127.0.0.1:8080/api/v1/products
cd ../frontend
npm start
```

In another terminal:

```sh
curl --silent --show-error --include http://127.0.0.1:4200/api/v1/products
```

Open `http://127.0.0.1:4200/`. The existing Angular development-only proxy forwards
catalogue requests to port 8080. No production routing or hosting is selected.
Both requests are anonymous; do not attach cookies or authorization headers.

- **200** with `items: []`, `pageSize: 20`, `nextCursor: null`: confirmed empty
  eligible catalogue. The storefront says no products are currently available.
- **503** Problem Details: unavailable/uncertain authority, not confirmed empty.
  The storefront says catalogue availability could not be confirmed.
- Connection refusal, startup failure, proxy error or invalid response: transport
  or runtime failure; never evidence of an empty catalogue.

Expect `Cache-Control: no-store` and `X-Correlation-ID` on catalogue responses.
No Session cookie should be created. A fresh database permits empty observation;
existing unconfirmed evidence may instead yield 503. Do not create or publish
Products to force a result. Rechecking issues another actual read.

## Stop and retain local data

Stop Angular and Gradle with Ctrl+C in their terminals, then:

```sh
docker compose -f compose.local.yaml stop
```

`docker compose -f compose.local.yaml down` removes containers/network but retains
the named database volume. To remove local data **only when explicitly intended**:

```sh
docker compose -f compose.local.yaml down --volumes
```

This deletes this Compose project's local database. Never run it against data you
need to retain. No release approval, manual accessibility sign-off or production
readiness follows from a successful local smoke test.

## Local verification evidence — 2026-11-06

Verified on Java 21.0.6, Docker 28.0.4 / Compose 2.34.0 and PostgreSQL 18
(server 18.6). Compose configuration validation passed. Spring Boot 3.5.16
started with strict Gradle verification; Flyway history recorded successful V1–V8.
Read-only queries confirmed zero Product and Session rows and a runtime role
without superuser, database-creation or role-creation privileges.

Direct port 8080 and proxied port 4200 listing requests returned HTTP 200 with
`{"items":[],"pageSize":20,"nextCursor":null}`, JSON content type, no-store and
correlation headers, and no Set-Cookie. Real Chrome verified confirmed-empty
presentation, a single main landmark and keyboard recheck/focus. This smoke test
used the actual database/backend/proxy, not synthetic responses. A real 503/outage
scenario and manual accessibility were not exercised.

The initial shell bootstrap failed on Docker Desktop bind-mount execution. The
final configuration uses plain SQL initialization; the already-created empty
verification volume was recovered by applying that SQL once before Flyway startup.
No Product records were inserted. Subsequent uniquely named fresh-volume tests
verified automatic SQL initialization, successful V1–V8 and catalogue HTTP 200.
The first fresh test exposed runtime access to Flyway history through broad table
grants. The SQL bootstrap now revokes public-schema access; the repeated fresh test
checks that history reads/writes fail while Domain access remains available.
Temporary verification services and volumes were removed; the original retained
volume was untouched. Bootstrap changes do not retrofit existing volumes. A volume
initialized with the earlier grants needs a separately reviewed permission update
or explicitly intended reset before treating its runtime isolation as equivalent.

The observed original failure was execution of the bind-mounted shell initializer:
Docker reported `/bin/sh: bad interpreter: Permission denied`. The exact host/mount
policy causing that denial was not established. Plain `.sql` initialization avoids
executing a mounted shell file; it is processed by the image's `psql` initdb path.

## Development-only Staff OIDC provider evaluation

`compose.staff-identity.local.yaml` runs Keycloak 26.8.2 with a separate PostgreSQL
18 database for local Staff OIDC evaluation. It imports the reproducible
`fashion-staff-local` realm and the confidential
`fashion-staff-backend-local` client. The client permits authorization code flow,
requires PKCE S256 and disables implicit, direct-grant, service-account, device and
CIBA grants. It creates no users, Staff associations, application roles,
permissions or automatic provisioning.

This stack is not connected to the application. It does not activate Staff login,
issue an application Session or authorize Product administration. `start-dev`,
plain HTTP, a bootstrap administrator and a single database/container topology are
development-only limitations. The imported realm does not establish the mandatory
production assurance mapping, phishing-resistant MFA policy, recovery, audit,
revocation, backup, availability, secret-management or operational controls from
ADR-0023 and the Security Standards.

From `backend/`, generate fresh local-only secrets in the current shell. Keep the
same values while retaining the Compose volume; never commit or print them:

```sh
export KEYCLOAK_DB_PASSWORD="$(openssl rand -hex 32)"
export KEYCLOAK_BOOTSTRAP_ADMIN_USERNAME="local-keycloak-admin"
export KEYCLOAK_BOOTSTRAP_ADMIN_PASSWORD="$(openssl rand -hex 32)"
export STAFF_OIDC_CLIENT_SECRET="$(openssl rand -hex 32)"
docker compose -f compose.staff-identity.local.yaml config --quiet
docker compose -f compose.staff-identity.local.yaml up -d --wait
```

The service is loopback-only at `http://127.0.0.1:8180`. Verify its governed local
realm metadata without exposing credentials:

```sh
curl --fail --silent --show-error --retry 30 --retry-delay 1 --retry-all-errors \
  http://127.0.0.1:8180/realms/fashion-staff-local/.well-known/openid-configuration
```

The retry is intentional: container health can become positive briefly before the
imported realm's HTTP endpoint accepts connections.

The discovery document must report this exact issuer and corresponding endpoints:

```text
issuer:                 http://127.0.0.1:8180/realms/fashion-staff-local
authorization_endpoint: http://127.0.0.1:8180/realms/fashion-staff-local/protocol/openid-connect/auth
token_endpoint:         http://127.0.0.1:8180/realms/fashion-staff-local/protocol/openid-connect/token
jwks_uri:               http://127.0.0.1:8180/realms/fashion-staff-local/protocol/openid-connect/certs
```

Use the local administration console only to inspect configuration. Do not create
an association or treat a local account as application Staff authority. The realm
import resolves the committed client-secret placeholder from the shell environment;
the secret itself is not committed.

Stop containers while retaining the local Identity-provider database:

```sh
docker compose -f compose.staff-identity.local.yaml stop
```

Restart with the same exported values using `up -d --wait`. Keycloak skips the
startup import when the realm already exists, so the PostgreSQL volume remains the
source of retained local state. To reset only this development stack when deletion
is explicitly intended:

```sh
docker compose -f compose.staff-identity.local.yaml down --volumes
```

That command permanently removes this Compose project's local Keycloak database.
Run it only from this repository with the exact Compose file after confirming the
project name `fashion-staff-identity-local`. It does not remove the separate
`fashion-local` application PostgreSQL volume.

## Local Product image malware scanning

The `compose.local.yaml` development environment includes the official ClamAV
`clamd` 1.4.6 image. It persists signature definitions and exposes port 3310 on
`127.0.0.1` only. The local Spring profile streams the original private
quarantine bytes with `INSTREAM`; it never supplies filesystem paths or public
URLs. The application upload limit remains 8 MiB. The development `clamd`
configuration sets `StreamMaxLength` to 9 MiB so protocol framing and the exact
8 MiB application maximum remain below the scanner's stream ceiling; it does
not increase the Product upload limit.

Compose inherits the health check declared by the official ClamAV image, which
performs the image-owned clamd PING/PONG probe. It deliberately does not define
a separate `nc`-based command or assume that extra shell utilities are present.
The exact `clamav/clamav:1.4.6` image and its effective runtime configuration
must still be verified in each development environment after the image can be
retrieved.

Start or stop the local dependencies with the existing Compose workflow:

```bash
docker compose -f compose.local.yaml up -d
docker compose -f compose.local.yaml ps
docker compose -f compose.local.yaml down
```

The first ClamAV startup can take several minutes while signature databases are
downloaded and loaded. Before each `INSTREAM` request, the application queries
`VERSION`. The recorded engine and signature-database metadata describes the
scanner state observed by that preceding query; it is not atomically bound to
the subsequent scan. The application treats an unconfigured, unhealthy,
timed-out, malformed or unavailable scanner as `INDETERMINATE`. A `CLEAN`
result is a technical observation only, not authoritative Security acceptance.
Product publication and customer visibility remain blocked.
