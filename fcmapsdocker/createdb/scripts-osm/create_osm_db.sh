#!/bin/bash

# https://www.postgresql.org/docs/17/app-psql.html

echo "Remove database gis_$dbid..."
dropdb gis_$dbid
echo "Remove database lowzoom_$dbid..."
dropdb lowzoom_$dbid

echo "Create database gis_$dbid..."
createdb gis_$dbid
echo "Create extension postgis..."
psql -d gis_$dbid -c 'CREATE EXTENSION postgis;'

echo "Get OSM data..."
for pbflink in $PBFLINKS; do echo $pbflink;wget --quiet "$pbflink"; done
#   https://download.geofabrik.de

echo "Start osm2pgsql (gis_$dbid)..."
osm2pgsql -d gis_$dbid --slim --drop --bbox $MINLON,$MINLAT,$MAXLON,$MAXLAT --flat-nodes=osmnodes.dat --number-processes 10 --output pgsql --style fcmaps.style *.osm.pbf 
#   https://osm2pgsql.org/doc/man/osm2pgsql-2-2-0.html

echo "Start arealabel..."
psql gis_$dbid < arealabel.sql
echo "Start stationdirection..."
psql gis_$dbid < stationdirection.sql
echo "Start viewpointdirection..."
psql gis_$dbid < viewpointdirection.sql
echo "Start pitchicon..."
psql gis_$dbid < pitchicon.sql
echo "Start otm_isolation..."
psql -d gis_$dbid -c 'ALTER TABLE planet_osm_point ADD COLUMN otm_isolation text;'

echo "Create lowzoom_$dbid..."
createdb lowzoom_$dbid
echo "Create extension postgis in lowzoom_$dbid..."
psql -d lowzoom_$dbid -c "CREATE EXTENSION postgis;"
echo "Create extension dblink  in lowzoom_$dbid..."
psql -d lowzoom_$dbid -c "CREATE EXTENSION dblink;"
#echo "Create extension dblink in gis_$dbid..."
#psql -d gis_$dbid -c "CREATE EXTENSION dblink;"

# railways
echo "Simplifying railways..."
psql -d gis_$dbid -c "CREATE VIEW lowzoom_railways AS SELECT ST_SimplifyPreserveTopology(way,50) AS way,railway,\"service\",tunnel FROM planet_osm_line WHERE (\"service\" IS NULL AND railway IN ('rail','light_rail'));"
echo "1 done"
psql -d lowzoom_$dbid -c "CREATE TABLE railways (way geometry(LineString,3857), railway text, \"service\" text, tunnel text);"
echo "2 done"
psql -d lowzoom_$dbid -c "INSERT INTO railways SELECT * FROM dblink('host=$PGHOST port=$PGPORT dbname=gis_$dbid user=$PGUSER password=$PGPASSWORD','SELECT * FROM lowzoom_railways') AS t(way geometry(LineString,3857), railway text, \"service\" text, tunnel text);"
echo "3 done"
psql -d lowzoom_$dbid -c "CREATE INDEX railways_way_idx ON railways USING GIST (way);"
echo "4 done (last)"
