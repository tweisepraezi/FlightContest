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
