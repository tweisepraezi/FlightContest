# Flight Contest: C# and Docker migration plan

Analysis date: 21 September 2026. Repository revision: `ffc9e47`. Application version: `4.2.13`.

Implementation update: the initial .NET solution is being introduced on `feature/dotnet-migration-step1`; see the [Rider setup guide](../../dotnet/README.md) and [step 1 handoff](../../dotnet/docs/MIGRATION_STEP_1.md). The architecture and estimates below describe the broader migration, not features already implemented.

Current scope decision: direct GPS device access, Brother printer integration, and the Windows device helper are deferred. References to those components below remain as future compatibility notes, not first-release requirements. Logger-file upload and browser/PDF reporting remain in scope. PostgreSQL integration and background workers will be introduced with their first working feature; the initial scaffold runs without a database.

## 1. Recommendation and feasibility

**Yes: the Flight Contest application can be rewritten in C# and run in Docker.** The recommended target is **ASP.NET Core MVC on .NET 10, using Linux containers**, PostgreSQL, and the existing map-rendering service. Windows users would continue using a browser. A Windows computer could also host the containers through Docker Desktop and WSL 2; alternatively, a Linux server could host them for Windows clients.

“MDV framework” is interpreted here as **MVC, Model–View–Controller**. This is an explicit, unconfirmed assumption. If a specific framework called MDV was intended, revisit the presentation-layer choice before implementation. Choose modern ASP.NET Core MVC, rather than the older ASP.NET MVC built on .NET Framework. Microsoft documents MVC's controllers, Razor views, and web API support in its [MVC overview](https://learn.microsoft.com/en-us/aspnet/core/mvc/overview?view=aspnetcore-10.0). .NET 10 is the recommended starting point because it is an active LTS release, with support scheduled through November 2028; verify supported versions again when implementation starts. [Microsoft support policy](https://dotnet.microsoft.com/en-us/platform/support/policy/dotnet-core).

There are three distinct scopes:

| Objective | Feasibility and implications |
| --- | --- |
| Remove the main application's Windows dependency | Plausible without a complete rewrite: retain Grails temporarily and replace OS-specific integrations. This needs a separate build and runtime proof. |
| Rewrite Flight Contest's application code in C# and deploy with Docker | Recommended if C# maintainability is a project objective. This includes business logic, persistence, MVC pages, jobs, import/export, and integration adapters. |
| Rewrite every component and run every existing workflow inside containers | A much larger scope. Browser JavaScript, Mapnik/GDAL/PostGIS, bundled map-service executables, and hardware drivers are separate dependencies. Desktop interaction and Windows COM printing need replacement or a helper outside Docker. |

A rewrite is therefore **not required simply to use Docker**. It is reasonable as a technology migration, but it should proceed in verified stages. The hard work is reproducing years of competition rules and operational behavior, not translating Groovy syntax.

This document is based on static source inspection. No legacy build, container, hardware workflow, live database migration, or performance benchmark was run. The plan distinguishes repository facts from proposed architecture; it does not claim verified runtime portability.

## 2. What is in this repository

The working directory is `fc/`, but its parent Git repository also contains `fcmapsdocker/`, `fcmapsserver/`, `gpx2gac/`, and `printlabel/`. Those adjacent components matter when interpreting “everything.”

### Application inventory

Counts below are physical file/line counts, including comments and blank lines. They are sizing indicators, not estimates of development effort. Vendor JavaScript, translation text, documentation, and adjacent projects are excluded from the line totals.

| Area in `fc/` | Observed size | Migration significance |
| --- | --- | --- |
| `grails-app/controllers/` | 26 Groovy files; 15,308 lines | Request handling includes substantial workflow logic. |
| `grails-app/domain/` | 42 Groovy files; 13,312 lines | GORM entities also contain calculations and presentation methods. |
| `grails-app/services/` | 23 Groovy files; 43,869 lines | Scoring, imports, reporting, tracking, maps, demo data, and built-in checks. |
| `grails-app/taglib/` | 22 Groovy files; 12,765 lines | Custom HTML generation must be accounted for alongside views. |
| `grails-app/utils/` | 51 Groovy files; 13,101 lines | Rules, aviation mathematics, file formats, time, and integration helpers. |
| `grails-app/views/` | 193 GSP files; 22,837 lines | Server-rendered UI maps naturally to MVC/Razor. |
| `grails-app/jobs/` | 3 Groovy files | Upload, live results, and map processing. |
| `grails-app/i18n/` | 11 properties files | Preserve language keys and audit translation completeness. |
| `test/unit/` | 11 Groovy files | Ten placeholder tests; the aviation-math test's assertion is commented out. |
| `src/csharp/` | One 56-line launcher | Launches scripts through `cmd.exe`; not an existing C# application foundation. |

There are 100,409 lines of Groovy under `grails-app/`; adding GSP views, unit tests, and the C# launcher gives 123,529 lines. In particular, `FcService.groovy` is 15,949 lines, `RouteController.groovy` is 4,998, and `Test.groovy` is 3,163. A direct one-class-for-one-class translation would preserve difficult coupling.

### Existing technology and behavior

| Finding | Repository evidence |
| --- | --- |
| Grails 2.5.6 / Groovy application, packaged as a WAR | [application.properties](../application.properties), [BuildConfig.groovy](../grails-app/conf/BuildConfig.groovy). Build metadata targets Java 1.6 bytecode; deployment instructions specify JDK 8 and Tomcat 9, while the development build plugin references Tomcat 8. |
| H2 is the default competition database | [DataSource.groovy](../grails-app/conf/DataSource.groovy): production uses `jdbc:h2:file:../fc/fcdb`. External configuration can override defaults. |
| PostgreSQL is already used for local mapping | [PrintMapTools.groovy](../grails-app/utils/PrintMapTools.groovy), [map installation notes](../../fcmapsdocker/readme_install.txt). This is distinct from the default H2 competition store. |
| Database upgrades include application-specific data transformations | [BootStrap.groovy](../grails-app/conf/BootStrap.groovy), [Global.groovy](../grails-app/domain/Global.groovy). Current declared database version is `2.49`. |
| Precision flying, rally flying, ANR, classes, teams, routes, schedules, planning/navigation/observation/landing scoring | [project overview](../../readme.txt), [ContestRules.groovy](../grails-app/utils/ContestRules.groovy), domain classes, [FcService.groovy](../grails-app/services/FcService.groovy), [EvaluationService.groovy](../grails-app/services/EvaluationService.groovy). |
| PDF reports are generated from printable HTML | [PrintService.groovy](../grails-app/services/PrintService.groovy) uses Flying Saucer / iText and fetches application print URLs. |
| Logger imports support GAC, IGC, GPX, KML, KMZ, and NMEA | [LoggerFileTools.groovy](../grails-app/utils/LoggerFileTools.groovy); route formats and FC-specific extensions are in [RouteFileTools.groovy](../grails-app/utils/RouteFileTools.groovy). |
| Spreadsheet import uses Apache POI, including old `.xls` files | [BuildConfig.groovy](../grails-app/conf/BuildConfig.groovy), [CrewTools.groovy](../grails-app/utils/CrewTools.groovy), `FcService.groovy`. |
| External integrations include tracking, OpenAIP, email, FTP publishing, and map services | [TrackerService.groovy](../grails-app/services/TrackerService.groovy), [OpenAIPService.groovy](../grails-app/services/OpenAIPService.groovy), [EmailService.groovy](../grails-app/services/EmailService.groovy), [GpxService.groovy](../grails-app/services/GpxService.groovy). |
| Browser-side mapping and Task Creator are separate assets | [web-app/GM_Utils](../web-app/GM_Utils/), [web-app/taskcreator](../web-app/taskcreator/), [GetTaskCreator.groovy](../scripts/GetTaskCreator.groovy). They do not need a C# rewrite to work with MVC. |

The standard unit-test folder is insufficient as a migration safety net. However, [TestService.groovy](../grails-app/services/TestService.groovy) contains meaningful boundary checks, and the `DemoContest*Service.groovy` files contain scenarios and expected results. Extract those before redesigning calculations.

## 3. Windows dependencies and their replacements

| Current dependency | Evidence | Required change |
| --- | --- | --- |
| Windows installation, Tomcat service control, batch builds | `build.bat`, `deploy/fc.is6`, `deploy/install-service.bat`, `deploy/start_fc.bat` | Build .NET artifacts and images in CI; supply Compose configuration and an operator runbook. Replace service start/stop with deployment operations. |
| `C:/FCSave`, installed-program paths, filesystem configuration scripts | `Defs.groovy`, `Config.groovy` | Configure storage roots and service URLs; mount persistent directories. Translate settings into typed configuration and secrets, rather than executing old Groovy configuration. |
| Windows registry machine identifier | `Global.GetClientID()` invokes PowerShell against `HKLM` | Introduce a persisted installation ID. Support importing the existing ID because it is used to fetch remote configuration and map-counter information. Coordinate any provider-side registration change. |
| GPS device enumeration and GPSBabel executable | `ReadLoggerTagLib.groovy`, `TaskController.groovy` around `readlogger`, `deploy/read_*_logger.bat` | Browser file upload for exported tracks; optional local C# device helper for direct serial/USB acquisition. Keep device selection separate from scoring. |
| Automatic file import and opening PDFs | `deploy/FCAutoLoad_Logger.vbs`, `FCAutoLoadScan_*.vbs`, `FCAutoOpenPDF.vbs` | Server-side ingestion for mounted input folders, or a local helper that uploads files and opens approved results. Container processes cannot open a user's desktop browser or PDF viewer. |
| Tray manager / WPF / Windows Forms | `deploy/fcmanager.ps1`, `src/csharp/FlightContestManager.cs` | Move application status and job controls into the web UI. Provide an optional desktop tray/helper only if users need it. |
| Hard-coded Python/GDAL locations | `GpxService.Gdal2Tiles()` uses `C:/Program Files/Python37/python.exe` and GDAL paths | Retain GDAL in a Linux worker/container; invoke configured executables with explicit argument lists, timeouts, and captured failures. Validate projection and raster output. |
| Windows `psql.exe` and Docker CLI calls from the web application | `PrintMapTools.groovy`, `OsmPrintMapService.groovy` around lines 1836–1905 | Use database/API clients and an explicit map-job interface. Let deployment tooling own containers; do not make the MVC application manage the Docker daemon. |
| Host-local URLs embedded in maps, email, and live output | `MapController.groovy`, `MapTools.groovy`, `GpxService.groovy`, `Defs.groovy` | Separate public application URLs from internal container service addresses. Replace assumptions that `localhost:8080` and `localhost:8181` refer to the host. |
| Brother label printing through COM | Adjacent [printlabel/MainWindow.groovy](../../printlabel/src/MainWindow.groovy) instantiates `bpac.Document` | Preserve through a Windows C# helper and validated vendor SDK integration, or replace with PDF labels and normal printing after checking layout fidelity. |
| Separate Groovy/Swing GPX-to-GAC utility | Adjacent [gpx2gac/MainWindow.groovy](../../gpx2gac/src/MainWindow.groovy) | Move conversion into a shared C# library and expose it in the web UI or a small CLI; compare output with existing examples. |

Docker Desktop does not provide direct USB passthrough; its documented USB/IP workaround has device limitations. Treat a Windows hardware helper as the default design for device parity, with passthrough as an optional, separately tested configuration. [Docker USB guidance](https://docs.docker.com/desktop/troubleshoot-and-support/faqs/general/), [USB/IP details](https://docs.docker.com/desktop/features/usbip/).

If **everything must run inside containers**, standardize input on uploaded logger files, scanned files, and browser-downloadable PDFs, and replace any COM-dependent label workflow. That changes the operator experience and must be an explicit product decision.

## 4. Proposed architecture

Use one modular application with a web host and a worker host. Share the domain and application libraries; keep mapping infrastructure separate because it already has its own runtime and data requirements.

```text
Windows / macOS / Linux browser
              |
        ASP.NET Core MVC -------- PostgreSQL: competition data
              |                         |
       persistent files           durable job records
              |                         |
              +---------------- .NET worker
                                        |
                            existing map service / GDAL
                                        |
                               PostGIS: mapping data

Optional Windows device helper --authenticated uploads--> MVC API
  GPS/serial, scan folders, desktop opening, Brother labels
```

### Project layout to create

```text
dotnet/
  FlightContest.sln
  src/
    FlightContest.Domain/          # Rules, calculations, entities, value types
    FlightContest.Application/     # Use cases, contracts, job definitions
    FlightContest.Infrastructure/  # EF Core, files, external adapters
    FlightContest.Web/             # MVC controllers, Razor views, assets, API
    FlightContest.Worker/          # Imports, reports, publication, map jobs
    FlightContest.Migration/       # Validated legacy export import tool
    FlightContest.DeviceAgent/     # Optional Windows host integration
  tests/
    FlightContest.Domain.Tests/
    FlightContest.Parity.Tests/
    FlightContest.Integration.Tests/
    FlightContest.Browser.Tests/
  deploy/
    compose.yaml
    Dockerfile.web
    Dockerfile.worker
```

The names and layout are proposals; no application scaffolding is created by this analysis.

### Technology choices

| Area | Proposed implementation | Decision to prove early |
| --- | --- | --- |
| Web application | ASP.NET Core MVC, Razor views, existing browser assets where suitable | Port one complete contest workflow and preserve form behavior and keyboard use. |
| Competition persistence | EF Core 10 with Npgsql and PostgreSQL | Explicit mapping of GORM relationships, ordering, deletion rules, precision, and concurrency. [Npgsql EF Core 10](https://www.npgsql.org/efcore/release-notes/10.0.html). |
| Background work | .NET worker with a database-backed job queue and explicit retry/lease handling | Preserve current jobs' non-overlap behavior; prevent duplicate publication after restarts. A scheduler library can be selected if scheduling needs warrant it. |
| Reports | Prototype HTML-to-PDF using Playwright .NET and Chromium | Test actual A3/A4 layouts, fonts, page breaks, headers, maps, and scanned-form alignment. Browser dependencies must be installed and version matched. [PDF API](https://playwright.dev/dotnet/docs/api/class-page#page-pdf), [container guidance](https://playwright.dev/dotnet/docs/docker). |
| Spreadsheet interchange | Evaluate NPOI for both `.xls` and `.xlsx` | Validate real crew/observation templates and formulas; do not assume an `.xlsx`-only replacement covers the current workflow. [NPOI project](https://github.com/nissl-lab/npoi). |
| Mapping | Retain the existing service, Mapnik, GDAL, PostGIS, and styles behind interfaces | Golden map outputs and offline operation; coordinate-system and rendering compatibility. |
| Images | Select a maintained Linux-compatible image library during the report prototype | Avoid `System.Drawing.Common` in the Linux server; Microsoft removed its Unix support switch. [Compatibility documentation](https://learn.microsoft.com/en-us/dotnet/core/compatibility/core-libraries/7.0/system-drawing). |
| Localization | Convert properties resources to .NET resources and use request-local culture | Preserve escaped Unicode, formatting arguments, fallback languages, and print language independent of UI language. |

Keep PostgreSQL for competition records logically separate from the map databases. Existing mapping scripts create `gis_*`, `running_*`, and `ready_*` databases. Their lifecycle and privileges should not control competition data. Separate database services are preferable for isolation; shared infrastructure is an operational option with separate roles and backups.

Start with one web instance and one worker instance. The current process-global state (`BootStrap.global`, `TempData`) and session-held contest objects are unsuitable as the basis for adding replicas. Store IDs in sessions, load contest-scoped data explicitly, and persist operational job state.

## 5. Step-by-step implementation plan

### Step 1 — Agree on the compatibility baseline

1. Confirm MVC terminology and supported host scenarios: Windows desktop with Docker Desktop, Linux server, and whether Windows Server hosting is required.
2. Create a feature matrix from controllers, GSPs, tag libraries, `docs/fc_en.adoc`, and real operator workflows. Include precision/rally/ANR variants, combined and parallel contests, manual corrections, backups, offline maps, and label printing.
3. Assign every feature a disposition: preserve, replace, or explicitly retire. Track the adjacent utilities and map stack as well as `fc/`.
4. Inventory real GPS devices, scanners, printers, database versions, deployment sizes, languages, and external-service credentials/configuration. Do not put credentials in the feature matrix.
5. Choose a small initial workflow for the pilot, while retaining the full matrix as the definition of completion.

**Deliverable / exit gate:** an agreed scope and acceptance matrix. “MVP complete” must not be confused with full replacement.

### Step 2 — Preserve and run a reference installation

1. Record the working installer, JDK, Tomcat, H2 driver, dependencies, settings, and database version. The source tree mixes historical build and deployment versions; confirm the actual installed combination.
2. Take a consistent H2 backup and copy all referenced files, including `C:/FCSave`, maps, images, uploads, custom configuration, and report assets. Use a stopped instance or verified database backup procedure; do not copy a live database file casually.
3. Restore to an isolated reference installation. Do not run the development environment against production data: `DataSource.groovy` uses `create-drop` in development.
4. Run built-in module checks and demo contest checks. Record current failures rather than treating the old program as infallible.
5. Capture representative inputs, parsed tracks, intermediate calculations, penalties, rankings, generated files, and report PDFs. Remove personal data where it is unnecessary for testing.

**Deliverable / exit gate:** a reproducible reference installation, a tested restore, and a versioned comparison corpus.

### Step 3 — Prove the difficult boundaries before broad implementation

1. Build a small MVC application in a Linux container with a PostgreSQL connection, persistent file upload, and a basic health endpoint.
2. Port one meaningful calculation from `CalcService` / `AviationMath` and its boundary cases from `TestService`.
3. Parse one real logger file and compare its normalized points with the old application.
4. Generate one demanding printable form and one result report with the proposed PDF engine; inspect physical scale and page layout.
5. Call the existing map-service API from the container and fetch a finished map.
6. On an actual Windows workstation, prove one required GPS or printer workflow through the proposed helper, if that workflow is in scope.

**Deliverable / exit gate:** evidence that calculations, native dependencies, printing, and Windows hardware can be supported. Resolve a failed boundary before committing to bulk translation.

### Step 4 — Establish the C# solution and deployment foundation

1. Create the proposed solution with nullable reference types, explicit dependency direction, centralized dependency versions, and reproducible builds.
2. Add contest-scoped application services; keep HTTP/session access out of the domain. Separate the large `FcService` into operations such as contest management, scheduling, route editing, logger import, and evaluation.
3. Define configuration for data roots, internal service endpoints, public base URL/path, installation ID, mail, tracking, and maps. Read secrets from deployment configuration or secret files.
4. Introduce operator/admin/read-only access policies appropriate for LAN use; allow separately scoped public results. Preserve contest isolation in every lookup. Implement validation, request authenticity checks for mutations, and safe upload limits as part of the replacement endpoints.
5. Add Linux CI builds, meaningful domain/integration checks, and image builds. Add Windows checks for the optional helper.

**Deliverable / exit gate:** a deployable application shell with persistent storage, usable configuration, and CI validation.

### Step 5 — Implement the database model and migration tooling

1. Inventory all 42 domain classes and their effective H2 schema. Record `hasMany`, `belongsTo`, inheritance, ordered collections, `mapping`, constraints, defaults, transient fields, enum encodings, and binary data.
2. Separate stored state from computed properties. Preserve old identifiers in migration metadata or retain IDs deliberately; account for references embedded in strings such as `task_123` and `resultclass_456`.
3. Define explicit PostgreSQL precision/scale, nullability, foreign keys, indexes, uniqueness, and concurrency tokens. Preserve empty-string versus null distinctions: the old configuration disables conversion of empty strings to null.
4. Read historical upgrade logic in `BootStrap.groovy`. Normalize a restored copy through a supported legacy upgrade path before export, or implement documented version-specific transformations. Never advance an old database version without applying its data changes.
5. Build a temporary Java/Groovy exporter using the compatible H2 runtime or legacy application. Export a versioned JSON/CSV manifest, all relationships, and binary assets with checksums. Do not assume EF Core can read the existing H2 file directly or PostgreSQL can execute an H2 SQL dump unchanged.
6. Build the C# importer with dry-run validation, deterministic ID mapping, relationship checks, and an import report. Use a fresh destination or a transaction/staging design that prevents partial imports from becoming active.
7. Compare row counts, identifiers, relationships, blob hashes, rule settings, stored results, and recomputed rankings. Check timezone data, legacy sentinels, deleted/disabled flags, and image/file references.
8. Use explicit, reviewed EF Core migrations for the new schema. Run deployment migrations once, separately from normal web startup, and rehearse restoration if a migration fails.

**Deliverable / exit gate:** repeatable migration of representative historical and current databases with a reconciliation report and tested rollback. The one-time exporter may remain Java even when the production application is entirely .NET.

### Step 6 — Port calculation and scoring behavior

1. Start with `FcMath`, `FcTime`, `AviationMath`, `CalcMath`, and the computational parts of `CalcService`.
2. Encode units and conversions explicitly: nautical miles/metres, knots, headings, coordinates, elapsed durations, UTC timestamps, and competition-local times.
3. Preserve rounding at the same stages. `FcMath` uses both significant-digit rounding (`MathContext(4)`) and fixed-scale `HALF_EVEN`; C# `decimal` is not an unlimited-precision replacement for Java `BigDecimal`. Choose `decimal`/`double` by operation and validate conversion boundaries.
4. Port rule definitions, per-class overrides, planning penalties, navigation checks, observation scoring, landing scoring, special results, exclusions, tie handling, and team/overall rankings.
5. Cover gate boundaries, missed gates, procedure turns, curved legs, corridors, interpolation, duplicate timestamps, clock corrections, midnight rollover, and daylight-saving behavior with comparisons against the corpus.
6. Convert `TestService` and demo expectations into automatically failing tests. Add explicit cases where existing unit tests only print differences.
7. Version rule configurations so a later rule change does not silently recalculate a historical competition differently.

**Deliverable / exit gate:** exact agreement for discrete penalties, rankings, and specified rounded outputs. Define tolerances only for continuous values; those tolerances must never hide a change in a scoring decision. Record any intentional legacy-bug correction separately for domain review.

### Step 7 — Port competition workflows and the MVC interface

1. Implement contest selection/settings, crews, aircraft, teams, and result classes.
2. Add route creation/editing, checkpoints, wind/planning data, task creation, schedules, start lists, and relevant copy operations.
3. Add logger evaluation, manual result entry and correction, observation/landing forms, final rankings, and publication controls.
4. Translate GSP views into typed Razor views. Replace custom tag libraries with view models, partials, view components, or tag helpers according to responsibility. Move calculations out of rendering code.
5. Preserve or deliberately redirect legacy `/fc` URLs used by helpers, printable pages, bookmarks, and embedded assets. Rewrite generated URLs through one configured route/public-address mechanism.
6. Port localization and validate German/English flows plus every remaining catalog's fallback behavior. Audit current file encodings during conversion.
7. Reuse mapping and Task Creator browser assets initially where licensing and integration permit; change their endpoints and configuration. Keep browser JavaScript where the existing interaction depends on it.

**Deliverable / exit gate:** a browser user can complete the pilot contest end to end, with multiple simultaneous operators and no cross-contest state leakage. Expand until every preserved workflow in Step 1 is covered.

### Step 8 — Complete import/export, reports, and map integration

1. Port every supported logger format and route format, including FC-specific GPX/KML extensions and supported TXT/REF paths. Preserve validation, interpolation, duplicate removal, encoding, and timestamp corrections.
2. Support real `.xls` and `.xlsx` crew/observation files, scan workflows, ZIP/KMZ assets, and GPX-to-GAC conversion. Add export/reimport checks where formats support a round trip.
3. Port all printable views, certificates/forms, result reports, and associated settings. Bundle required fonts and compare extracted values as well as rendered pages. Specifically verify A3/A4 size, orientation, scale, margins, and form alignment.
4. Adapt the print-map API, OpenAIP integration, map overlays, route/image projection, and GDAL tile generation. Record map-data versions in job metadata.
5. Retain the adjacent mapping stack initially. Its `printmaps_webservice` and `printmaps_buildservice` files are bundled **Linux x86-64 Go executables**; their source is not present alongside those files. Establish reproducible upstream builds or verified binary provenance. Do not promise ARM64 support until these dependencies are rebuilt or validated under emulation.
6. Replace startup-time fixed map selection with a documented per-contest strategy. The current renderer is configured using `dbid`, and the old application starts/stops a single named container. Initially serialize map work and selection, or extend the service contract to isolate contest-specific configurations.
7. Provide a deployment-owned map-data import job. The current `createdb` container performs database creation and downloads; converting web Docker commands to plain HTTP calls alone does not replace this workflow. Choose an operator-triggered job initially, or a restricted management worker if in-UI region creation must be retained.

**Deliverable / exit gate:** supported formats, reports, and maps pass the compatibility matrix, including offline use with preloaded resources.

### Step 9 — Replace background jobs and external integration behavior

1. Port `UploadJob`, `LiveJob`, and `OsmPrintMapJob` into durable work with explicit status, retries, timeout, cancellation, and error details.
2. Preserve non-concurrent execution where required. Use database leases or equivalent ownership, and idempotency keys for imports, publication, and map requests.
3. Persist job state independently of sessions and process memory. Ensure a restart cannot lose an accepted import or silently duplicate a publication.
4. Port SMTP/email, FTP publishing where still required, tracking, and remote configuration through narrow adapters. Validate actual upstream contracts with test accounts or captured fixtures before enabling outgoing writes.
5. Make disconnected operation explicit: local scoring and local reports continue; external publication queues and clearly reports its status.
6. Replace folder watchers with ingestion that waits for a stable/completed file and deduplicates input. A local helper should keep a retry queue for temporary network outages.

**Deliverable / exit gate:** restart and network-loss exercises preserve accepted work and produce understandable job status.

### Step 10 — Complete the optional Windows helper

1. Implement supported GPS acquisition and upload using explicit device IDs and supported commands. Keep scoring on the server.
2. Add scan-folder ingestion and approved desktop actions where required. If using a Windows service, keep interactive tray/browser actions in a separate user-session process.
3. Integrate Brother b-PAC/COM printing only on Windows and test the actual printer, SDK bitness, and `.lbx` layouts. Alternatively, validate a PDF-label replacement with operators.
4. Authenticate the helper to the server with a scoped, revocable credential. Transfer bytes and metadata, not arbitrary client filesystem paths or remote shell commands.
5. Package, update, and diagnose the helper independently of the Linux containers.

**Deliverable / exit gate:** required hardware workflows work on the actual supported Windows machines, including reconnection and offline retries. Skip this project only if the accepted scope uses files and browser printing throughout.

### Step 11 — Produce a complete Docker deployment

1. Create multistage .NET builds and pin supported base-image/dependency versions. Use non-root runtime users and give each process write access only to its data directories.
2. Supply Compose services for `web`, `worker`, competition PostgreSQL, and an optional local map stack with PostGIS and map-data initialization. Separate map features into an optional profile if remote rendering is sufficient.
3. Persist competition data, uploads/maps, job artifacts, and ASP.NET data-protection keys. Bundle fonts and static browser assets. Treat containers as replaceable; do not store competition state in the image or writable container layer.
4. Use service DNS names such as `db` and `maps` for internal traffic. Use the configured public URL for browser/email links. Publish only the necessary application endpoint; keep databases and map administration internal.
5. Include readiness checks and bounded retry behavior. Startup order alone does not establish database or map readiness. Run database migrations as an explicit deployment step.
6. Replace the mapping scripts' fixed host IP and host PostgreSQL assumptions with container networking. Review their subprocess lifecycle and shutdown handling; the existing scripts background services and keep containers alive using `tail`.
7. Provide backup and restore commands for the competition database **and** related files/configuration, with a consistency point. Keep regenerable map caches distinct from irreplaceable contest assets.
8. Validate supported Windows Docker Desktop/WSL 2 installation, file permissions, LAN access, reboot recovery, resource limits, and no-internet startup after preload. Docker Desktop is not supported on Windows Server; for that host scenario, use a Linux VM running the container stack or make a separate supported hosting decision. [Docker Windows installation requirements](https://docs.docker.com/desktop/setup/install/windows-install/).
9. Package offline installation materials if required: images, application assets/fonts, map resources, configuration examples, and an operator recovery guide. Measure storage/RAM needs with representative contests and map regions before specifying minimum hardware.

**Deliverable / exit gate:** a fresh supported host can install, start, use, back up, restore, and upgrade the application following the runbook, without a legacy Windows service installation.

### Step 12 — Run in parallel, cut over, and retire the legacy application

1. Run several representative historical contests through both implementations, covering every retained ruleset and feature family. Have experienced scorers review differences.
2. Run a rehearsal with concurrent operators, real devices, large logger files, report batches, temporary network loss, and a host reboot. Record responsiveness and recovery time.
3. For a pilot, keep the legacy system as the authoritative writer and import snapshots into the new system for comparison. Avoid simultaneous writes to a shared competition database.
4. Before cutover, stop legacy writes, take a final consistent backup/export, migrate once, and reconcile the complete contest. Enable writes in the new application only after acceptance.
5. Define rollback limits explicitly. Restoring the old backup loses any subsequent new-system changes unless those changes have a verified replay/export path. Preserve an audit/export of post-cutover work and rehearse the chosen recovery procedure.
6. Retain a recoverable legacy installation and backups through the agreed acceptance period. Retire it only when all retained features, integrations, and operational procedures pass the matrix.

**Deliverable / exit gate:** the replacement runs a real competition successfully, with accepted results, recoverable data, and no untracked legacy dependency.

## 6. Delivery order and effort

The critical sequence is **reference corpus → difficult-boundary prototype → schema/import + calculations → complete workflow → full feature parity → deployment rehearsal → cutover**. UI/report work can proceed alongside domain work once contracts and representative data exist.

An initial milestone should demonstrate: create/import contest, crews and a route; import one logger; calculate checked results; generate a report; restart containers; and restore the same contest from backup. This is a useful pilot slice, not a complete replacement for the product.

The following are planning allowances, not a quotation or a measured forecast. They assume experienced C# developers, regular access to a competition-domain expert, usable legacy data, and reuse of the map renderer/browser assets.

| Work package | Initial allowance, engineering person-weeks |
| --- | --- |
| Reference setup, compatibility corpus, technical prototypes | 4–8 |
| Application foundation, schema, migration tooling | 6–12 |
| Mathematics, scoring, scheduling, and parity validation | 12–24 |
| MVC workflows, views, localization | 12–24 |
| Formats, reports, maps, jobs, external integrations | 12–24 |
| Windows helper, packaging, operational checks, pilot/cutover | 6–12 |
| **Total before contingency** | **52–104** |

For two full-time engineers, that is roughly 6–12 months of nominal capacity before coordination, external dependencies, and contingency; use approximately **8–16 months as an initial calendar planning range**, then replace it with evidence after Steps 1–3. A narrower MVP may arrive much sooner. A literal rewrite of the GIS renderer and all third-party browser tools is outside these allowances and needs separate sizing.

## 7. Principal risks and completion criteria

| Risk supported by this inspection | Required control |
| --- | --- |
| Scoring logic distributed across large services, entities, and tag libraries | Characterize observable behavior, extract calculations, and compare intermediate as well as final results. |
| Weak conventional unit tests | Convert built-in checks and demos into automated regression tests before broad changes. |
| Historical schema upgrades and hidden data encodings | Versioned export/import, explicit transformations, relationship reconciliation, and tested backups. |
| Report/layout differences | Visual and physical-scale checks on representative printable forms, not only successful PDF generation. |
| Windows hardware and desktop assumptions | Tested host helper or an explicitly accepted replacement workflow. |
| Map service host coupling and x86-64 binaries | API/job boundary, container networking, contest isolation, and verified platform support. |
| Process-local state and non-concurrent jobs | Persistent job state, contest-scoped operations, concurrency checks, and restart tests. |
| Mixed bundled asset licensing and provenance | Inventory source, binary, font, map-data, and Task Creator notices before reuse/distribution; record component-specific obligations and obtain review where unclear. The repository's [license inventory](../web-app/licenses/README.txt) identifies GPLv3 for Flight Contest and separate terms for bundled components; it also lists a noncommercial Creative Commons license for GPX Viewer. A C# rewrite does not establish new rights to reused assets. |

Full completion requires all of the following:

- Every retained Step 1 feature is implemented and accepted, including the adjacent utilities where in scope.
- Required penalties, ranks, rounded outputs, import semantics, and historical rule behavior pass the comparison corpus.
- Historical/current data migration and database-plus-files restore pass reconciliation.
- Required reports and maps pass content and layout checks.
- Multiple users can work without lost edits or contest-state mixing.
- The supported Windows host/browser and actual hardware workflows pass rehearsal.
- Core local operation works without internet after required resources are preloaded.
- Container replacement/restart preserves data and accepted work; installation and upgrades follow a tested runbook.
- A cutover and rollback procedure has been rehearsed, including handling changes made after cutover.

The recommended endpoint is a C# application that runs independently of Windows, while Windows remains a supported client and optional container host. Preserve specialist GIS dependencies and isolate hardware access until there is a concrete reason and separate budget to replace them.
