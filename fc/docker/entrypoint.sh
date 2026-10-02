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

if [ -n "${FC_CONFIG_FILE:-}" ]; then
    # Docker left an empty root-owned file beneath the old read-only bind mount.
    # Replace that placeholder, but retain saved configuration (including empty files).
    if [ ! -e "$FC_CONFIG_FILE" ] || {
        [ -f "$FC_CONFIG_FILE" ] && [ ! -s "$FC_CONFIG_FILE" ] && [ ! -w "$FC_CONFIG_FILE" ];
    }; then
        : "${FC_CONFIG_SEED_FILE:?Set a readable initial configuration file}"
        mkdir -p "$(dirname "$FC_CONFIG_FILE")"
        (umask 077
         cp "$FC_CONFIG_SEED_FILE" "$FC_CONFIG_FILE.tmp"
         mv "$FC_CONFIG_FILE.tmp" "$FC_CONFIG_FILE")
    fi
    if [ ! -f "$FC_CONFIG_FILE" ] || [ ! -r "$FC_CONFIG_FILE" ] || [ ! -w "$FC_CONFIG_FILE" ]; then
        echo "FC_CONFIG_FILE must be readable and writable: $FC_CONFIG_FILE. Store it in the state volume; mount initial configuration at FC_CONFIG_SEED_FILE." >&2
        exit 1
    fi
fi

exec "$@"
