# Flight Contest: Docker deployment plan for the `fc` web application

Updated: 2 October 2026. Application version: `4.2.13`.

Current scope: deploy only the existing web application in `fc/`. The Docker build context, deployment files, and application changes stay inside that folder. Mapping services and the other repository components are deferred to a later project.

Status: the web-app deployment files and Linux portability changes are present. Acceptance requires the build, startup, workflow, persistence, and restore checks below. See [the runbook](../docker/README.md) for commands and [the validation record](DOCKER_VALIDATION.md) for actual results; the existence of deployment files alone is not proof of compatibility.

## 1. Objective and why this is simpler

Run the existing Grails web application at `/fc` in one Linux Docker container, retaining its Groovy code, GSP interface, competition calculations, and H2 competition database.

This is a smaller first release than deploying the complete stack. It has one application service and its persistent files. It avoids PostgreSQL/PostGIS provisioning, map-region imports, coastline downloads, renderer supervision, contest/database selection, and GDAL/native-library compatibility work. The remaining work is the legacy WAR build, Linux startup, persistent storage, web workflows, and operational verification.

Keep Grails 2.5.6, Java 8, Tomcat 9, the current compatible H2 driver, and one application instance. The [existing deployment notes](../deploy/readme_en.txt) describe the runtime family. A C# rewrite, framework upgrade, and database-engine conversion are separate projects. The [C# migration plan](CSHARP_DOCKER_MIGRATION_PLAN.md) remains a future alternative.

## 2. Scope for this release

| Area | Current treatment |
| --- | --- |
| Contest setup, crews, routes/tasks, schedules, scoring, rankings | Keep existing web workflows; verify representative scenarios. |
| Logger-file uploads, spreadsheet imports/exports | Keep browser-based file exchange; verify supported formats. |
| Reports and PDFs | Keep existing report generation and bundled assets; verify values, fonts, and printable layout. |
| Competition database | Keep H2 in persistent storage; run the production profile. |
| Saved files, uploads, jobs, existing map assets, live output | Persist mutable application directories across container replacement. Storing existing map assets does not enable map rendering. |
| Background jobs | Keep existing application jobs in the single web-app instance. |
| Local and remote map generation, GDAL conversions, tile generation | Disabled/unavailable in this Docker release; deferred. |
| Email, FTP, tracking, external configuration providers | No new integration work in this release. Remote startup configuration is off by default. Validate separately before using existing integrations. |
| USB/GPS devices, Brother COM printing, tray utilities, desktop viewers | Deferred; use browser downloads, PDF output, and logger-file upload. |
| `fcmapsdocker/`, `fcmapsserver/`, `gpx2gac/`, `printlabel/`, `dotnet/` | No changes or build/runtime dependencies for this deployment. |

There is no mapping Compose profile, map database service, renderer image, or region-import service in the current deliverables. Map-specific work from the broader implementation is rolled back; the original components remain available for later work.

## 3. Deployment and invocation

```text
Browser --> host port 8080 --> flightcontest: Java 8 + Tomcat 9 + fc.war
                                  |-- H2 competition database
                                  |-- saved files and installation ID
                                  |-- uploads, jobs, existing assets, live output
                                  +-- existing application background jobs
```

Use `fc/` as the entire build context. From that folder:

```sh
cd docker
docker compose up --build -d
docker compose ps
docker compose logs --tail=100 flightcontest
```

Open `http://localhost:8080/fc` after the application is healthy. The first image build requires Internet access for Grails, dependencies, and system packages. The web app runs as UID/GID 10001. Bind to loopback initially; configure the bind address, port, and public URL explicitly for LAN use.

Keep `/fc` as the context path. Configure the internal application URL separately so server-side PDF requests reach the same Tomcat even when the browser connects through a different host port or address.

## 4. Required application changes

| Finding | Change retained for the web app |
| --- | --- |
| Startup invokes PowerShell to obtain a registry machine ID | [FlightContestRuntime.groovy](../grails-app/utils/FlightContestRuntime.groovy) accepts `FC_CLIENT_ID`, or generates and persists an identity on Linux. Keep the existing Windows fallback. |
| Configuration and save paths use `C:/FCSave` | [Config.groovy](../grails-app/conf/Config.groovy) and [Defs.groovy](../grails-app/utils/Defs.groovy) accept configured container paths. Preserve Windows defaults when no override is supplied. |
| Production H2 uses a relative path | [DataSource.groovy](../grails-app/conf/DataSource.groovy) accepts `FC_DB_PATH`; use an absolute persistent basename. Never use the development `create-drop` profile with operator data. |
| WAR output uses Windows backslashes | [BuildConfig.groovy](../grails-app/conf/BuildConfig.groovy) uses a portable output path. Build with Grails/JDK inside Docker without invoking Windows installer tools. |
| Some sources use legacy Windows encoding | Normalize Groovy sources in the disposable build copy with `prepare-source.py`. Preserve repository source encoding. |
| Mutable files are beneath the deployed application | Explode the WAR in the image and mount only mutable directories. Preserve static-file serving and `servletContext.getRealPath()` behavior. |
| PDFs use a browser-visible host for internal requests | Retain internal URL overrides in print parameters. Retain public URL handling for existing web links/assets. |
| Restored configuration could enable unavailable map services | Set `FC_MAP_MODE=disabled` and retain small guards that skip map-provider/local-map checks. Skip GDAL availability on Linux. No Linux map conversion implementation is included. |
| Tomcat responding does not prove the application is initialized | [HealthController.groovy](../grails-app/controllers/HealthController.groovy) checks completed bootstrap, database version compatibility, and database access. |

Keep these changes separate from competition calculations. Roll back map metadata JDBC rewrites, externally managed map lifecycle/UI changes, map-specific environment settings, Linux GDAL commands, and changes to the map Dockerfiles/import/renderer scripts.

## 5. Storage and configuration

| Volume | Contents |
| --- | --- |
| `state` | `/var/lib/flightcontest`: H2 in `db/`, saved files and `.fc/client-id` in `save/`, working files in `work/`. |
| `uploads` | `/usr/local/tomcat/webapps/fc/gpxupload`. |
| `jobs` | `/usr/local/tomcat/webapps/fc/jobs`, including `done` and `error`. |
| `maps` | `/usr/local/tomcat/webapps/fc/map`: existing application assets only. |
| `live`, `live2` | Corresponding application output directories. |

Persist data, not the entire `webapps` tree, so replacing an image replaces application code. Keep one process per H2 file store. `docker compose down` retains named volumes; `down --volumes` deletes them. Keep the same Compose project name for the installation.

| Setting | Purpose/default |
| --- | --- |
| `FC_BIND_ADDRESS`, `FC_PORT` | Host binding; defaults `127.0.0.1`, `8080`. |
| `TZ`, `CATALINA_OPTS` | Timezone, memory, UTF-8, headless operation, and existing numeric-formatting locale. |
| `FC_SAVE_DIR` | Image default `/var/lib/flightcontest/save`. |
| `FC_DB_PATH` | Image default `/var/lib/flightcontest/db/fcdb`, without a database extension. |
| `FC_CONFIG_FILE` | Writable Groovy configuration at `save/.fc/config.groovy` in the state volume. The application settings page saves changes here. |
| `FC_CONFIG_SEED_FILE` | Read-only initial configuration at `/etc/flightcontest/config.groovy`; copied into writable storage only when uninitialized. |
| `FC_CLIENT_ID` | Optional original installation ID; otherwise persist a generated ID. |
| `FC_REMOTE_CONFIG` | Defaults `false`; remote configuration is outside first-release acceptance. |
| `FC_PUBLIC_BASE_URL` | Browser URL including `/fc`; derive from requests when empty. |
| `FC_INTERNAL_BASE_URL` | Image default `http://127.0.0.1:8080/fc` for application self-calls. |
| `FC_MAP_MODE` | Fixed to `disabled` by the current Compose deployment. |

Keep secrets, operator databases, and private configuration outside the build context and Git. To move an installation, stop it, take a consistent copy of H2 and referenced files, preserve its client ID, and restore into an isolated project first. Startup may apply existing Flight Contest schema/data upgrades even though the database engine is unchanged.

## 6. Implementation and acceptance sequence

1. **Build the web app.** Build the production WAR from `fc/` with the recorded Grails/JDK/runtime versions and existing H2 driver. Check required fonts, templates, help, and browser assets. Resolve legacy dependencies without silently upgrading the framework.
2. **Verify startup.** Start an empty installation without Windows tools or map services. Confirm `/fc/health/ready`, usable application pages, clean shutdown, and stable identity. Separately verify a copied reference database.
3. **Verify core workflows.** Exercise contest setup, routes/tasks, crews, schedules, logger upload/conversion, scoring, corrections, rankings, and spreadsheet exchange. Run meaningful built-in module/demo checks and compare expected results.
4. **Verify reports.** Download representative PDFs and compare values, page breaks, paper sizes, fonts, images, and non-ASCII names. Exercise a different host port/LAN address to confirm internal report URLs work.
5. **Verify persistence.** Create data and upload files, replace the container, then verify database records, files, and identity. Check jobs and interrupted-work recovery; verify core workflows with optional services unavailable.
6. **Verify operation.** Back up with the application stopped, restore into fresh volumes, and verify data, identity, files, and reports. Rehearse an update and rollback using the previous image plus matching pre-update data. Write commands and actual results in the validation record.

A real Windows installation restore, exact report comparison, and any claimed host/CPU support need their own evidence. Do not mark unperformed checks as passed.

## 7. Deliverables inside `fc/`

```text
fc/
  .dockerignore
  docker/
    Dockerfile
    compose.yaml              # One flightcontest service
    .env.example
    .gitignore
    config.groovy
    entrypoint.sh
    prepare-source.py
    README.md
    scripts/
      common.sh
      backup.sh
      restore.sh
      restore-volume.py
    tests/
      smoke.py
      runtime.groovy
  docs/
    DOCKER_DEPLOYMENT_PLAN.md
    DOCKER_VALIDATION.md
```

All build inputs come from `fc/`; no parent-folder context or sibling component is required. Backup/restore covers the web application's stored files and H2 only.

## 8. Release checklist

- [ ] Production WAR and image build from the `fc/` context.
- [ ] Exactly one web-app service starts and becomes ready without Windows or map tooling.
- [ ] Empty and restored H2 databases work with the recorded compatible driver.
- [ ] Core contest/import/scoring workflows match the reference scenarios.
- [ ] PDFs contain correct values and usable print layout/fonts.
- [ ] Data, files, and installation identity survive container replacement.
- [ ] Background jobs and concurrent operators work with one application instance.
- [ ] Core operation works without optional providers.
- [ ] Backup/restore into fresh volumes and update/rollback checks pass.
- [ ] Another operator can use the runbook and actual validation record.

## 9. Later work

After accepting the web-app deployment, plan map rendering, PostGIS, region/contour import, GDAL/tile generation, map lifecycle and contest selection, and mapping backups separately. Then evaluate other repository tools, desktop/hardware integration, framework upgrades, or the C# migration according to need.

None of those projects is a prerequisite for this release. The remaining uncertainties here are legacy dependency retrieval, copied database compatibility, generated web assets, filesystem assumptions, and workflow/report parity.
