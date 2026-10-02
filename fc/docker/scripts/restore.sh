#!/bin/sh
# Restore core application data only, into fresh stopped volumes.
set -eu
. "$(dirname -- "$0")/common.sh"
: "${1:?Specify a completed backup directory}"
test -f "$1/COMPLETE"
test -f "$1/application.tar.gz"
if [ -n "$(compose ps --status running -q flightcontest)" ]; then
    echo 'Stop the target application before restoring.' >&2
    exit 1
fi
compose run --rm --no-deps -T --entrypoint python3 flightcontest \
    /usr/local/lib/flightcontest/restore-volume.py < "$1/application.tar.gz"
echo 'Application data restored. Review the saved configuration before starting.'
