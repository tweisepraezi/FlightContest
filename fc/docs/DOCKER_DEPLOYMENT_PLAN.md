# Flight Contest: Docker deployment plan for the existing application

Prepared: 1 October 2026. Source revision reviewed: `5f86b31`. Application version: `4.2.13`.

Status: implementation plan based on source inspection. No legacy build, container startup, database restore, or workflow test has been performed for this plan. All proposed configuration names and deployment files below still need implementation.

## 1. Objective and recommendation

Run the existing Flight Contest Grails application in Linux Docker containers while retaining its Groovy code, GSP interface, competition calculations, and H2 competition database. A C# rewrite and a PostgreSQL conversion of competition data are not prerequisites.

This is the containerization option identified at the beginning of the [C# and Docker migration plan](CSHARP_DOCKER_MIGRATION_PLAN.md). The plans are alternatives for the main application; this work does not depend on completing the .NET migration.

The recommended starting point is Grails 2.5.6, a Java 8 runtime, Tomcat 9, and one application instance. The repository documents this runtime family in [the deployment notes](../deploy/readme_en.txt). Grails 2.5.6 supports WAR packaging and JDK 8, and Tomcat 9 requires Java 8 or later. These framework capabilities support the approach but do not establish compatibility of every Flight Contest integration. [Grails documentation](https://grails.apache.org/docs/2.5.6/guide/gettingStarted.html), [Tomcat documentation](https://tomcat.apache.org/migration-9).

Docker deployment requires targeted changes to Windows paths, startup identification, file storage, native tools, and map-service integration. The existing application cannot be expected to start unchanged in a normal Linux container.

## 2. Scope and milestones

| Area | Planned treatment |
| --- | --- |
| Contest setup, crews, routes, schedules, scoring, rankings | Retain the existing implementation and verify representative workflows. |
| Logger-file imports and spreadsheet interchange | Preserve browser-based imports and existing supported formats. |
| Reports | Retain Flying Saucer / iText and bundled fonts; verify PDF layout and downloads. |
| Competition database | Retain H2 with its compatible driver and persistent storage. No database-engine conversion. |
| Background jobs | Keep the existing Grails/Quartz jobs in the single application instance. |
| Local map rendering and data import | Reuse and adapt `fcmapsdocker/`; introduce a separate PostGIS service for mapping data. |
| Remote services | Preserve existing configuration and validate the integrations selected for deployment. |
| Direct GPS/USB access, Brother COM printing, tray tools, desktop auto-open | Deferred as in the existing migration plan. Use logger-file upload and browser/PDF output for the initial container release. |
| C# rewrite, Grails major-version upgrade, competition PostgreSQL conversion, multiple app replicas | Separate workstreams; excluded from this deployment plan. |

**Milestone A — core application:** reproducible image; startup; contest, import, scoring, and reporting workflows; persistent data; tested backup and restore. It must work without a local map stack. Existing remote map integration may be exercised separately when configured.

**Milestone B — local mapping:** map database provisioning, rendering, tile generation, contest selection, and restart behavior verified with the core application. Milestone A alone does not establish local-map or full application compatibility.

Start with Linux x86-64 as the deployment baseline. Windows hosting can use Docker Desktop with Linux containers. The bundled map-service executables are Linux x86-64 binaries; ARM64 hosts, including Apple Silicon Macs, need a separately verified emulation setup or rebuilt binaries before claiming support for the complete stack.

## 3. Proposed deployment

```text
Browser
   |
   | /fc
   v
flightcontest: Java 8 + Tomcat 9 + fc.war
   |-- H2 competition database on persistent storage
   |-- saved files, uploads, jobs, maps, and live output on persistent storage
   |-- existing background jobs
   |
   +-- HTTP --> printmaps: existing rendering services
   |                  |
   |                  +--> mapdb: PostgreSQL + PostGIS
   |
   +-- JDBC --> mapdb: map availability/metadata checks

Operator --> map-import: one-off existing createdb workflow --> mapdb
```

Compose should own service startup and networking. Use service names such as `printmaps` and `mapdb` for connections between containers, rather than host IP discovery. Container addresses can change while Compose service names remain stable. [Docker Compose networking](https://docs.docker.com/compose/how-tos/networking/).

Keep `/fc` as the application context. Separate the browser-visible base URL from the application's internal URL for report generation and the renderer's internal API URL. Audit each `localhost` reference: a call to the same Tomcat instance may remain local; a call to a separate map container must use its service address.

Expose the application on a configurable host port, initially bound to loopback. Document an explicit LAN binding for operators using other computers. Map databases and renderer endpoints should use the internal Compose network unless a specific client workflow requires direct access.

## 4. Required source and packaging changes

| Finding | Repository evidence | Planned change and verification |
| --- | --- | --- |
| Startup calls Windows PowerShell to read a registry machine ID | [BootStrap.groovy](../grails-app/conf/BootStrap.groovy), [Global.groovy](../grails-app/domain/Global.groovy), `LoadClientConfig()` / `GetClientID()` | Allow an explicit client ID or a generated ID saved once on persistent storage. Preserve the original installation ID when moving an existing installation because remote configuration depends on it. Keep Windows fallback behavior for existing Windows installs. Verify Linux startup without PowerShell. |
| Configuration is loaded from `C:/FCSave/.fc` | [Config.groovy](../grails-app/conf/Config.groovy) | Add an explicit external configuration location available before Grails configuration is loaded. Define precedence and preserve the Windows default when no override is supplied. |
| Save paths assume `C:/FCSave` | [Defs.groovy](../grails-app/utils/Defs.groovy) | Make the save root configurable. Audit derived paths, path separators, case sensitivity, file URLs, and command arguments. Verify uploads, exports, and filenames with spaces/non-ASCII characters. |
| Production H2 path is relative; development uses `create-drop` | [DataSource.groovy](../grails-app/conf/DataSource.groovy) | Use an absolute configurable H2 path in the container and package/run in the production environment. Use restored copies for testing. Keep the compatible H2 driver; do not upgrade it as part of the first container build. |
| Mutable files live beneath the deployed web application | [BootStrap.groovy](../grails-app/conf/BootStrap.groovy), [Defs.groovy](../grails-app/utils/Defs.groovy) | Persist each mutable directory and audit additional writes throughout controllers/services. Preserve static-file serving and `servletContext.getRealPath()` behavior. Verify persistence after container replacement, not just restart. |
| Build runs through Windows installer tooling; WAR output uses backslashes | [build.bat](../build.bat), [BuildConfig.groovy](../grails-app/conf/BuildConfig.groovy) | Create a standalone Linux WAR build using Grails 2.5.6 and JDK 8. Use a portable WAR output path. Package required browser assets, fonts, templates, and help files without invoking the Windows installer or launcher build. |
| Local map API and SQL defaults assume the host environment | [PrintMapTools.groovy](../grails-app/utils/PrintMapTools.groovy), [Defs.groovy](../grails-app/utils/Defs.groovy) | Configure the map API and database host/port explicitly. Replace the simple `psql.exe` metadata queries with JDBC using the existing PostgreSQL driver. Remove credential-bearing command logging in those paths. |
| Application starts/stops named Docker containers | [OsmPrintMapService.groovy](../grails-app/services/OsmPrintMapService.groovy) | Add an externally managed map-service mode. In this mode, replace start/stop/create-region actions with accurate status and operator instructions. Compose and the operator run map services/imports. Retain existing behavior for Windows installations. |
| Tile generation assumes Windows Python/GDAL locations | [GpxService.groovy](../grails-app/services/GpxService.groovy), `Gdal2Tiles()` | Configure Linux Python/GDAL commands using argument lists. Install and verify compatible GDAL Java bindings, JNI libraries, and native runtime where those methods execute. The Java dependency is currently GDAL 3.2.0; installing an arbitrary distro GDAL version is not sufficient proof of compatibility. |
| Renderer/import entry scripts are non-executable in Git | [printmaps Dockerfile](../../fcmapsdocker/printmaps/Dockerfile), [createdb Dockerfile](../../fcmapsdocker/createdb/Dockerfile), their `start.sh` files | Both Dockerfiles use `CMD ["/start.sh"]`, but both scripts have Git mode `100644`. Set executable permissions during the image build or invoke Bash explicitly; preserve LF line endings. |
| Renderer startup backgrounds two processes and tails a log | [printmaps/start.sh](../../fcmapsdocker/printmaps/start.sh) | Add process supervision or split the services with shared job storage. Ensure service failure makes the container unhealthy or exits it; forward termination signals and verify shutdown of both processes. |

Keep portability edits separate from changes to competition rules. Extend existing configuration facilities where practical rather than introducing a new configuration framework.

## 5. Storage and configuration contract

### Persistent storage

| State | Proposed container location or treatment |
| --- | --- |
| H2 competition database | `/var/lib/flightcontest/db/fcdb`, with JDBC URL `jdbc:h2:file:/var/lib/flightcontest/db/fcdb`. Mount the containing directory. |
| Existing `FCSave` contents and persisted installation ID | `/var/lib/flightcontest/save`; preserve `.fc`, `.geodata`, and any other referenced assets. |
| Application uploads | Persistent `gpxupload` directory beneath the exploded `/fc` application, initially preserving existing paths. |
| Application jobs | Persistent `jobs` tree including `done`, `error`, and any lock/status files. Define stale-lock handling after interrupted work. |
| Maps and live publication files | Persistent `map`, `live`, and `live2` directories where used; account for the existing relative map-backup path. |
| Renderer jobs and results | Persistent `/fcmaps/jobs`; retain active/completed job state across renderer replacement. |
| Mapping databases | Dedicated PostGIS data volume; independent of the H2 volume. |
| Temporary files | Explicit writable work/temp directories; classify remaining files as disposable or persistent after auditing writes. |

Use named volumes or documented bind mounts. Named volumes persist independently of container replacement; deleting a volume is a separate operation. [Docker volumes](https://docs.docker.com/engine/storage/volumes/).

For the first implementation, explode the WAR during image construction and mount only its mutable subdirectories. Do not persist the entire Tomcat `webapps` directory: that could retain old application code across image updates. Document exact mount targets once the runtime image is selected, including directory ownership for a non-root runtime user. Avoid startup redeployment that wipes or conflicts with mounted directories.

Run one application process against an H2 database. Do not share its file store between replicas. A restored file database with the same application/driver avoids an engine conversion, but application startup can still apply the existing database upgrades in `BootStrap.groovy`.

### Proposed settings

These names are a proposed interface, not existing supported environment variables. Implement and document how they map to Grails configuration before publishing Compose examples.

| Setting | Purpose |
| --- | --- |
| `FC_CONFIG_FILE` | Explicit external Groovy configuration file loaded at startup. |
| `FC_SAVE_DIR` | Replacement for the hard-coded `C:/FCSave` root. |
| `FC_DB_PATH` | Absolute H2 database basename, without a driver-specific file extension. |
| `FC_CLIENT_ID` | Optional imported installation identity; otherwise use the persisted generated identity. |
| `FC_PUBLIC_BASE_URL` | Browser-visible URL including `/fc`, where an absolute public URL is required. |
| `FC_INTERNAL_BASE_URL` | Application self-call URL, such as `http://127.0.0.1:8080/fc`, for verified server-side requests. |
| `FC_MAP_API_URL` | Internal renderer endpoint, such as `http://printmaps:8181/api/beta2/maps`. |
| `FC_MAP_MODE` | Explicit disabled, remote, or externally managed local map mode. |
| Existing PostgreSQL/map credentials and GDAL executable settings | Retain existing keys where possible; configure through the external file or mounted secrets. |

Set and record JVM memory, UTF-8 encoding, and runtime timezone; test them against the competition's own time settings. Keep passwords, real databases, and operator-specific configuration outside image build contexts and Git. The local machine ID, remote configuration, and map counters must behave consistently after a restart or restore. Bound remote configuration requests with timeouts so an unavailable provider does not indefinitely block startup.

## 6. Implementation sequence

### Step 1 — Establish a reproducible reference

1. Record the working application version, JDK, Tomcat, H2 driver, Grails distribution, plugins, and native libraries. Source files contain historical versions; distinguish those from the actually working installation.
2. Stop a reference installation or use its verified backup procedure. Copy the H2 database and referenced file trees as one consistent snapshot. Work only on copies.
3. Record representative precision, rally, and ANR contests, logger inputs, spreadsheet imports, scoring outputs, report PDFs, and map outputs.
4. Run meaningful built-in checks from [TestService.groovy](../grails-app/services/TestService.groovy) and the `DemoContest*Service` scenarios. Record baseline failures. The mostly placeholder unit-test directory is not sufficient evidence of behavior parity.
5. Record which remote integrations and hardware workflows are actually needed for the deployment; use the scope in section 2 as the starting assumption.

**Exit criterion:** recoverable reference installation and a comparison set with expected results.

### Step 2 — Prove the legacy build

1. Build with Grails 2.5.6 and JDK 8 in an isolated builder. Verify old plugin/dependency repositories and required artifacts are still retrievable. Cache verified artifacts where necessary; do not silently replace framework versions to make resolution succeed.
2. Produce a production `fc.war` without Windows-only build steps. Audit resources generated by `build.bat` and assets fetched by [GetTaskCreator.groovy](../scripts/GetTaskCreator.groovy); include what the application requires.
3. Record resolved dependency versions/checksums and the H2 driver. Select and pin exact tested Java 8 and Tomcat 9 runtime versions; do not use an unqualified `latest` image.
4. Use a known working WAR, if available, only for an initial packaging experiment. Completion of this step still requires a rebuild from source so portability changes can be shipped reproducibly.

**Exit criterion:** reproducible production WAR and documented dependency inputs. A dependency-resolution failure is a build blocker to solve before expanding implementation.

### Step 3 — Implement Linux startup and persistence

1. Add the configuration, client-ID, save-root, and database-path changes from sections 4–5 while preserving default Windows behavior.
2. Create the runtime image and core-only Compose configuration. Keep maps and optional external integrations explicitly disabled for the first startup.
3. Prepare persistent directories with the required permissions. Start Tomcat in the foreground and collect application logs through the container runtime.
4. Add a non-mutating readiness check that confirms the `/fc` application and its database initialization are ready. A reachable Tomcat landing page is insufficient. Optional map-provider availability must not block core readiness.
5. Start with an empty database, then separately with a restored reference copy. Verify startup without PowerShell, stable installation identity, and clean shutdown.
6. Create a contest and upload files; recreate the container from its image and confirm the records and files remain.

**Exit criterion:** repeatable Linux startup and verified persistence across container replacement.

### Step 4 — Verify core workflows and reports

1. Complete contest setup, crew entry/import, route/task preparation, scheduling, logger-file import, scoring, correction, and result publication using representative data.
2. Compare penalties, rankings, and other discrete outputs exactly with the reference. Use documented tolerances only for continuous values where the existing behavior justifies them.
3. Verify supported import/export formats, including legacy `.xls` and `.xlsx`, and export/reimport behavior where applicable.
4. Compare PDFs visually and by extracted values: A3/A4 layouts, page breaks, fonts, image paths, scales, and non-ASCII names. Test downloads from a browser on another machine as well as the host.
5. Check upload/live/map jobs, concurrent operator sessions, background-job overlap, recovery after interruption, and core operation without Internet connectivity.
6. Exercise the selected email, FTP, tracking, and remote-map integrations using isolated test configuration; prevent restored publication jobs from targeting live destinations during verification.

**Exit criterion:** Milestone A core workflow checks pass, with remaining deferred features explicitly recorded. Complete its backup/restore and update/rollback checks in Step 6; that step can run before local mapping when delivering the core application first.

### Step 5 — Integrate local maps and GDAL

1. Add a mapping profile with `mapdb`, `printmaps`, and a manually invoked `map-import` service. Pin tested PostgreSQL/PostGIS versions compatible with the existing import scripts and rendering queries.
2. Fix image entrypoint permissions and renderer process lifecycle. Persist renderer jobs/results and configure database/service addresses using Compose networking.
3. Replace map metadata CLI queries with JDBC, configure the API URL, and implement externally managed local-map mode. Do not mount the Docker socket into the web application.
4. Preserve the existing contest-to-database mapping: imports use `gis_*`, `running_*`, and `ready_*` databases; the renderer selects its database using `dbid`. Initially allow one selected contest for the renderer and serialize map processing. Document the operator action to switch contests, and reject or explain mismatched requests rather than rendering the wrong region.
5. Make the one-off importer report failure accurately. The existing script can proceed to create a `ready_*` marker after earlier commands fail; fix exit-status handling and mark data ready only after successful import validation. Provision its database creation/removal permissions separately from renderer access.
6. Test a small region import and verify data readiness, contour data if used, map submission, output retrieval, and switching to another contest. Test renderer failure and restart with an in-progress job.
7. Verify GDAL Java/native compatibility in the application container and Linux tile commands. Compare rendered maps and generated tiles with reference outputs, including projections, extent, and scale.
8. Build/download map assets before an offline event and verify the running stack does not require new downloads for the selected region. Keep the initial supported platform at `linux/amd64` until other architectures pass equivalent checks.

**Exit criterion:** Milestone B passes, including map import, rendering, tile generation, contest selection, and failure recovery.

### Step 6 — Document operation and rehearse deployment

1. Write operator instructions for build, configuration, start/stop, logs, health checks, region provisioning, contest selection, backup, restore, and version changes.
2. Implement a backup procedure that stops the application cleanly and captures H2, saved files, uploads, map assets, required job state, and configuration together. Document map database backup or reproducible re-import separately.
3. Restore into a fresh Compose project with fresh volumes, and verify contest data, installation ID, files, PDFs, and map availability. A successful backup command alone is insufficient.
4. Rehearse an image update with retained volumes. Preserve the prior image and a matching pre-update snapshot. Rollback must restore the matching database/files if startup changed the schema or data; replacing the image alone may not suffice.
5. Validate the documented Linux deployment and, if required, Windows Docker Desktop deployment. Record measured memory/disk use and startup/map-render times from representative workloads.

**Exit criterion:** another operator can deploy and restore the application using only the runbook and supplied artifacts.

## 7. Proposed repository deliverables

```text
fc/
  docker/
    Dockerfile                 # Pinned builder and Tomcat runtime stages
    compose.yaml               # Core app; optional local-mapping profile
    .env.example               # Documented non-secret settings
    config.groovy.example      # External application configuration
    entrypoint.sh              # Directory/config checks and foreground startup
    README.md                  # Operator runbook
    scripts/
      backup.sh                # Consistent backup procedure
      restore.sh               # Restore into stopped/fresh storage
  docs/
    DOCKER_DEPLOYMENT_PLAN.md   # This plan
    DOCKER_VALIDATION.md        # Actual commands, versions, results, limitations
  .dockerignore                # For the proposed fc/ application build context
fcmapsdocker/
  printmaps/                   # Existing image, adapted and verified
  createdb/                    # Existing one-off importer, adapted and verified
```

Compose should use `fc/` as the application build context and separate contexts for the two existing map images. Record the intended invocation directory and resolve relative configuration/bind paths accordingly. Exclude local databases, build caches, secrets, and unrelated artifacts from each build context.

These are planned deliverables. Creating this Markdown file does not create an executable Docker deployment.

## 8. Acceptance checklist

### Milestone A: core application

- [ ] A clean checkout can build the production WAR and pinned application image.
- [ ] The container starts without Windows tooling and serves `/fc` after database initialization.
- [ ] Empty and restored H2 databases both work with the recorded driver version.
- [ ] Installation identity survives restart, replacement, and restoration.
- [ ] Contest setup, imports, scoring, corrections, and rankings match the reference scenarios.
- [ ] Reports contain the expected values and preserve printable layout and fonts.
- [ ] Application data and referenced files survive container replacement.
- [ ] Background jobs and concurrent operator sessions behave correctly in one application instance.
- [ ] Core workflows remain usable with optional external services unavailable.
- [ ] Backup restoration into fresh volumes and an update/rollback rehearsal pass.

### Milestone B: local mapping

- [ ] Renderer and import images start reliably; process failures are visible.
- [ ] Region import succeeds and failures cannot leave false ready markers.
- [ ] Correct contest/database selection is enforced, including contest switching.
- [ ] Maps, GDAL conversions, and tiles match reference projections, scale, and content.
- [ ] In-progress job handling, renderer restart, and map-state persistence are verified.
- [ ] Prepared regions render without Internet access during event operation.
- [ ] Each claimed host/CPU platform has passed the documented workflow checks.

## 9. Remaining uncertainties and implementation order

The main unknowns are legacy dependency availability, the exact working H2/native-library combination, untracked generated assets, filesystem assumptions outside the known paths, and behavior after interrupted jobs. Resolve these through the staged proofs rather than assuming that a successful image build establishes readiness.

Container packaging retains the legacy framework and libraries. Record their maintenance status and required updates as follow-up work; Docker itself does not modernize those dependencies. Keep any necessary dependency update explicit and validate it against the same reference scenarios.

Implement Steps 1–4 first to establish a usable core deployment. Then complete local mapping and operational verification in Steps 5–6. Estimate delivery dates after the build and startup proofs identify the actual compatibility work; full desktop/hardware parity is outside these milestones.
