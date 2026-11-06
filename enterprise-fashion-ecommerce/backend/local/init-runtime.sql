-- Local access bootstrap only; no application data.
\getenv runtime_password LOCAL_DB_PASSWORD
CREATE ROLE local_app LOGIN PASSWORD :'runtime_password' NOSUPERUSER NOCREATEDB NOCREATEROLE;
-- Keep Flyway history in public inaccessible to the runtime account.
REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE CREATE ON DATABASE fashion_local FROM PUBLIC;
ALTER DEFAULT PRIVILEGES FOR ROLE local_migrator GRANT USAGE ON SCHEMAS TO local_app;
ALTER DEFAULT PRIVILEGES FOR ROLE local_migrator GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO local_app;
ALTER DEFAULT PRIVILEGES FOR ROLE local_migrator GRANT USAGE, SELECT ON SEQUENCES TO local_app;
