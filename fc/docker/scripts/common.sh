#!/bin/sh
set -eu
docker_dir=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
compose() {
    if [ -f "$docker_dir/compose.override.yaml" ]; then
        set -- -f "$docker_dir/compose.override.yaml" "$@"
    fi
    docker compose --project-directory "$docker_dir" -f "$docker_dir/compose.yaml" "$@"
}
