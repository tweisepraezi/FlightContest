# Run the `fc` web application in Docker

This deployment builds only from `fc/` and runs one web-app container with Grails 2.5.6, Java 8, Tomcat 9, and the existing H2 competition database. Map services, PostGIS, region imports, GDAL/tile generation, and the other repository components are deferred. See [the deployment plan](../docs/DOCKER_DEPLOYMENT_PLAN.md) and [recorded validation](../docs/DOCKER_VALIDATION.md) for scope and test results.

## Start the application

Install Docker Engine with Compose v2, or Docker Desktop configured for Linux containers. From the `fc` folder:

```sh
cd docker
docker compose up --build -d
docker compose ps
docker compose logs --tail=100 flightcontest
```

Open **http://localhost:8080/fc** when the container is healthy. The first build downloads Grails and its dependencies and compiles the production WAR. It needs Internet access; normal core operation does not. The image runs as UID/GID 10001. Keep one application instance per H2 database.

```sh
docker compose stop
docker compose start
```

Ordinary container replacement retains the named volumes. `docker compose down` retains them too; `docker compose down --volumes` deletes the installation's stored data. Keep the same Compose project name when restarting an installation. Set `COMPOSE_PROJECT_NAME` to isolate a test installation and use another host port if running both.

## Configuration

Copy `.env.example` to `.env` to change deployment settings. `.env` is ignored by Git. For LAN access, set `FC_BIND_ADDRESS=0.0.0.0` and `FC_PUBLIC_BASE_URL=http://YOUR_HOST:8080/fc`, then recreate the container. The host port is configurable with `FC_PORT`; internal PDF requests continue to use port 8080 inside the container.

| Setting | Default / behavior |
| --- | --- |
| `FC_BIND_ADDRESS`, `FC_PORT` | `127.0.0.1`, `8080`. |
| `TZ` | `Europe/Warsaw`; configure for the installation. Contest timezone settings still apply. |
| `FC_CLIENT_ID` | Empty generates and persists an ID. Set the original Windows installation ID when moving an existing installation that uses remote configuration or map counters. |
| `FC_REMOTE_CONFIG` | `false` prevents startup requests to the remote configuration provider. Set `true` only when using that provider. Requests have connection/read timeouts. |
| `FC_PUBLIC_BASE_URL` | Empty derives browser links from the request. Set explicitly when accessed through a proxy or a fixed LAN address. Include `/fc`. |
| `FC_MAP_MODE` | Fixed to `disabled` in Compose for this web-app release. |
| `CATALINA_OPTS` | 256 MB initial / 2 GB maximum heap, UTF-8, headless mode, German JVM locale. Preserve the locale options unless intentionally changing legacy numeric formatting. Browser language is chosen independently in the application. |

The image also supports `FC_SAVE_DIR`, `FC_DB_PATH`, `FC_CONFIG_FILE`, and `FC_INTERNAL_BASE_URL`; image defaults and Compose mounts align these paths with persistent storage. If overriding them, update the storage mounts and backup/restore scripts accordingly.

The checked-in `config.groovy` enables application logs. To supply private settings for existing integrations, copy it to an ignored `config.local.groovy` and create `compose.override.yaml`:

```yaml
services:
  flightcontest:
    volumes:
      - ./config.local.groovy:/var/lib/flightcontest/save/.fc/config.groovy:ro
```

Standard `docker compose` loads this override automatically. Commands with explicit `-f` flags must include it explicitly. The backup/restore scripts include `compose.override.yaml` automatically when present. Keep credentials out of tracked files and images. Groovy configuration files are executable application configuration and must come from trusted operators.

The application retains its existing access model. Start with local or trusted-LAN use; containerization does not add Internet-facing authentication.

## Stored data and moving an existing installation

| Volume | Contents |
| --- | --- |
| `state` | H2 in `db/`, `FCSave` equivalent in `save/`, persistent client ID in `save/.fc/client-id`, working files in `work/`. |
| `uploads`, `jobs` | Logger uploads and existing job files, including completion/error directories. |
| `maps`, `live`, `live2` | Application map assets and generated live output. |

The H2 basename is `/var/lib/flightcontest/db/fcdb`. Keep the original compatible database files and driver; changing to Docker does not require converting them to PostgreSQL. Application startup may still apply the existing Flight Contest schema/data upgrades.

For a Windows installation, first stop it and take a consistent copy of its H2 database, `C:/FCSave`, and referenced upload/map/job/live directories. Import into a fresh isolated Compose project, preserving these directory mappings and UID 10001 ownership. Copy the existing installation ID into `FC_CLIENT_ID`. Do not point a development Grails run at that database: the repository's development profile uses `create-drop`. Verify the copied installation before switching operators to it.

Old absolute file references or custom configuration may need adjustment. No operator database is bundled, and verification with a real restored Windows database remains a deployment acceptance step.

## Backup and restore

Run from `fc/docker`. Backups contain application data and potentially secret configuration; store them privately.

```sh
sh scripts/backup.sh /absolute/path/to/new-backup-directory
```

The script stops the app, archives its state and mutable web directories, saves the effective Compose settings and mounted configuration, and restarts services that were previously running. A `COMPLETE` file marks successful completion. Keep the matching application image; the backup records image information but does not export images.

Restore into a **new Compose project with fresh volumes**, with the same image version:

```sh
COMPOSE_PROJECT_NAME=flightcontest-restored sh scripts/restore.sh /absolute/path/to/backup
```

Restoration refuses a running app, nonempty application storage, unsafe archive paths, or symbolic/hard links. Review the saved configuration, restore required private settings, then start the new project with an available host port. Do not paste an old resolved Compose file blindly: it may contain absolute paths specific to the original computer.

Use a restored test project to verify reports and contest data before relying on a backup. For rollback after an update, restore the prior image **and matching pre-update data** if startup changed database contents. Preserve the backup until the updated installation has passed its workflow checks.

## Validation and limitations

For an isolated running test installation, from the `fc` folder:

```sh
python3 docker/tests/smoke.py http://localhost:8080/fc --demo 99
```

This creates the built-in demo contests, checks their expected scoring results, runs module checks, downloads a crew-list PDF, and uploads/converts a logger file. Omit `--demo` to avoid creating contests. `--contest-id NUMBER` checks a retained/restored contest and its report. Results are saved under `/tmp/flightcontest-smoke` unless `--output` is supplied.

Map rendering, GDAL conversions, and tile generation are deferred. Direct USB/GPS access, Brother COM label printing, Windows tray tools, and opening desktop viewers also remain outside the container scope. Use logger-file upload and browser/PDF output. Real-event data restoration, exact report layout comparison, external-provider credentials, and Windows-host testing must be verified for the intended installation. See the validation record for what was actually run.
