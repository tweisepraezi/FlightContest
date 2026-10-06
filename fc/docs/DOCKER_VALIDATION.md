# Web-app Docker validation

Date: 2 October 2026. Scope: only `fc/`, one application container, no mapping stack. This record describes checks actually performed on the narrowed implementation.

## Runtime and build

| Component | Recorded value |
| --- | --- |
| Application | Flight Contest 4.2.13 |
| Grails | 2.5.6; archive checksum recorded in `docker/Dockerfile` |
| Java | Temurin 1.8.0_504-b01 |
| Tomcat | 9.0.122; base image digest recorded in `docker/Dockerfile` |
| H2 | Bundled `h2-1.3.176.jar` |
| Tested image architecture | Linux ARM64 on this Docker host |
| Image | `flightcontest:4.2.13-docker` |
| Built image digest | `sha256:fd7377221dbaabef88463f353b588a31adf035a5dc9ace3d7a2c7533e3818476` |

From the repository root, `docker compose -f fc/docker/compose.yaml build` succeeded. It compiled the production WAR from the `fc/` build context, including 177 sources and 193 application GSPs. Legacy dependency caches were available on this host; a build with empty caches on another host remains unverified.

The runtime contains Python for application backup restoration. GDAL packages and Linux map conversion commands have been removed. The application retains its existing Java dependency declarations, including GDAL and PostgreSQL jars; those do not provision native map tooling or a database service.

## Checks performed

- Compose configuration validates and lists exactly one service: `flightcontest`.
- Shell scripts and Python files pass syntax checks.
- Git whitespace verification passes with `core.whitespace=cr-at-eol`, preserving legacy CRLF source files.
- An isolated project starts with an empty H2 database and becomes healthy on `http://localhost:18080/fc`. Bootstrap completes without PowerShell or map services.
- Readiness and built-in application module checks pass.
- All built-in demo scenarios selected by `--demo 99` complete with expected scoring results.
- Crew listing, PDF download, and logger upload/conversion to GPX pass.
- The generated test installation ID is `03da7b1d-bf88-42d9-bfc7-cdb0322152fe`. This belongs to the isolated test project; a fresh project generates its own ID unless `FC_CLIENT_ID` is set.

Startup command used:

```sh
FC_PORT=18080 docker compose -p flightcontest-web-check -f fc/docker/compose.yaml up --no-build -d --wait --wait-timeout 180
```

Smoke command used against the isolated test database:

```sh
python3 fc/docker/tests/smoke.py http://localhost:18080/fc --demo 99 --output /private/tmp/flightcontest-web-only-smoke
```

## Remaining acceptance work

A restored real Windows installation, exact PDF layout/font comparison, spreadsheet round trips, concurrent operators, interrupted background jobs, operation without Internet, a clean-cache build, and other host platforms have not been verified in this check. Backup/restore and an image-update rollback rehearsal require separate verification before operator deployment.

Map generation, region/contour import, PostGIS, GDAL/tile generation, remote integrations, and desktop/hardware tools are deferred. No compatibility claim is made for those components.

## Writable settings configuration fix — 2 October 2026

The original Compose mount placed `config.groovy` read-only at the path written by `GlobalController.update`. Updating language settings therefore returned HTTP 500. The seed configuration now mounts at `/etc/flightcontest/config.groovy`; startup copies it into the writable state volume only when no saved configuration exists, including migration of the empty root-owned placeholder left by the old mount. Existing saved configuration is retained.

The rebuilt image is `sha256:3bf6f7be2625e2dfe842a7eb1b8b87d9f5be674d106925d3fb0671a0d97a4de6`. The running project was recreated and is healthy. Its saved configuration is owned by the application user, mode `0600`, and verified readable/writable. The operator reported the settings update working after deployment. ClientID remains `8D266B31-672C-4D68-A1BC-2BD72C938F00`.

Backup now includes the writable configuration in the application archive, and restore accepts that configuration entry. Shell/Python syntax, Compose configuration, and Git whitespace checks pass. Backup/restore and replacement after a settings edit have not been exercised for this fix.

## Remote client configuration — 2 October 2026

The operator requested the existing Extras → Get ClientID → Load Configuration workflow. The installation's private `.env` now sets `FC_REMOTE_CONFIG=true`; the default for new installations remains `false`. The old value disabled both startup loading and the manual action.

The container reached the provider's configuration endpoint with HTTP 200. After recreation, the application is healthy. An HTTP POST to `/global/loadconfig` succeeded and displayed the requested ClientID, registered owner/club, configured map server, and configured OpenAIP server. Provider credentials were not printed or written into repository files. This verifies configuration loading only; rendering and other external provider operations remain outside this check.

The displayed generated-map count is `Global.FCMapCounter`, an in-memory value incremented by this application instance. Loading client configuration does not retrieve the count from a Windows instance.

## Operator H2 database restore — 2 October 2026

Restored the supplied `docker/backup/fcdb.h2.db` into the existing project's state volume after stopping the app. Read-only inspection with H2 1.3.176 confirmed schema version 2.49, 4 contests, 2 crews, and 16 routes. The copied database matched the supplied file before startup and was assigned to application UID/GID 10001.

The previous database directory, including diagnostic logs, is preserved under `docker/backups/before-db-restore-20261002/db/`. The supplied backup files were retained unchanged. `fcdb.trace.db` is a diagnostic log and was not needed for the restored database.

The app restarted and became healthy. The start page lists all four restored contests: `Trasy rekreacyjne.`, `Precyzyjne`, `Rajdowe`, and `Precyzyjne (stopnie, minuty, sekundy)`. Existing configuration, ClientID override, and other application volumes were retained. No separate saved-file or map-asset backup was supplied, so this operation restores the H2 database only.

The input `docker/backup/` directory is now ignored by Git and excluded from the Docker build context.
