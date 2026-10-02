#!/bin/sh
# Usage: COMPOSE_PROJECT_NAME=flightcontest sh scripts/backup.sh /path/new-backup
set -eu
umask 077
. "$(dirname -- "$0")/common.sh"
: "${1:?Specify a new backup directory}"
mkdir "$1"
backup_dir=$(CDPATH= cd -- "$1" && pwd)
running=$(compose ps --status running --services)
resume() {
    # Restart only services this script stopped, including when archiving fails.
    for service in flightcontest; do
        if printf '%s\n' "$running" | grep -qx "$service"; then
            compose start "$service" >&2 || true
        fi
    done
}
trap resume EXIT
trap 'exit 1' HUP INT TERM
compose stop flightcontest >&2
compose config > "$backup_dir/compose.resolved.yaml"
compose run --rm --no-deps -T --entrypoint cat flightcontest \
    /var/lib/flightcontest/save/.fc/config.groovy > "$backup_dir/config.groovy"
compose run --rm --no-deps -T --entrypoint tar flightcontest \
    -C / -czf - \
    var/lib/flightcontest \
    usr/local/tomcat/webapps/fc/gpxupload usr/local/tomcat/webapps/fc/jobs \
    usr/local/tomcat/webapps/fc/map usr/local/tomcat/webapps/fc/live \
    usr/local/tomcat/webapps/fc/live2 > "$backup_dir/application.tar.gz"
compose images --format json > "$backup_dir/images.json"
printf 'Backup completed. Configuration files may contain credentials.\n' > "$backup_dir/COMPLETE"
echo "Backup saved to $backup_dir"
