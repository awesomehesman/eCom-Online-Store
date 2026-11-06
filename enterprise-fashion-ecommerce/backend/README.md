# Local backend development

This setup exercises the existing anonymous catalogue only. It grants no Staff,
publication or operational commerce authority. No Product data is seeded.

## Prerequisites

Java 21, the committed Gradle 8.14.5 wrapper, Docker Engine/Desktop with Compose v2,
network access to the approved dependency repositories and PostgreSQL image, and
free localhost ports 5432, 8080 and 4200. Frontend prerequisites are documented in
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
migration account and applies existing classpath migrations V1–V8 at startup;
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
