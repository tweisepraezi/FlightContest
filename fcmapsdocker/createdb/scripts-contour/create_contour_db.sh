#!/bin/bash

echo "Remove database contours_$dbid..."
dropdb contours_$dbid

echo "Create database contours_$dbid..."
createdb contours_$dbid
echo "Create extension postgis..."
psql -d contours_$dbid -c 'CREATE EXTENSION postgis;'

echo "Start pyhgtmap..."
pyhgtmap --area=$MINLON:$MINLAT:$MAXLON:$MAXLAT --step=10 --start-node-id=1000000000 --start-way-id=1000000000 --max-nodes-per-tile=0 --no-zero-contour --pbf --srtm-user=$SRTMUSER --srtm-password=$SRTMPASSWORD

echo "Start osm2pgsql (contours_$dbid)..."
osm2pgsql -d contours_$dbid --slim --drop --flat-nodes=contournodes.dat --number-processes 10 --output pgsql --style contours.style lon*.pbf
