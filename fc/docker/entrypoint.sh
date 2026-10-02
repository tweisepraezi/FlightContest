#!/bin/sh
set -eu

for directory in "$FC_SAVE_DIR/.fc" "$(dirname "$FC_DB_PATH")" \
    /var/lib/flightcontest/work \
    "$CATALINA_HOME/webapps/fc/gpxupload" "$CATALINA_HOME/webapps/fc/jobs/done" \
    "$CATALINA_HOME/webapps/fc/jobs/error" "$CATALINA_HOME/webapps/fc/map" \
    "$CATALINA_HOME/webapps/fc/live" "$CATALINA_HOME/webapps/fc/live2"; do
    mkdir -p "$directory"
    if [ ! -w "$directory" ]; then
        echo "Flight Contest needs write access to $directory (UID $(id -u))." >&2
        exit 1
    fi
done

if [ -n "${FC_CONFIG_FILE:-}" ] && [ ! -r "$FC_CONFIG_FILE" ]; then
    echo "FC_CONFIG_FILE is not readable: $FC_CONFIG_FILE" >&2
    exit 1
fi

exec "$@"
