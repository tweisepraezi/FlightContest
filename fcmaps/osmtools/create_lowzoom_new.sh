#!/bin/bash

echo "Remove lowzoom_new..."
dropdb --if-exists lowzoom_new
echo "Create lowzoom_new..."
createdb lowzoom_new
echo "Create extension postgis in lowzoom_new..."
psql -d lowzoom_new -c "CREATE EXTENSION postgis;"
echo "Create extension dblink  in lowzoom_new..."
psql -d lowzoom_new -c "CREATE EXTENSION dblink;"
echo "Create extension dblink in gis_new..."
psql -d gis_new -c "CREATE EXTENSION dblink;"

# railways
echo "Simplifying railways..."
psql -d gis_new -c "CREATE VIEW lowzoom_railways AS SELECT ST_SimplifyPreserveTopology(way,50) AS way,railway,\"service\",tunnel FROM planet_osm_line WHERE (\"service\" IS NULL AND railway IN ('rail','light_rail'));"
echo "1 done"
psql -d lowzoom_new -c "CREATE TABLE railways (way geometry(LineString,3857), railway text, \"service\" text, tunnel text);"
echo "2 done"
psql -d lowzoom_new -c "INSERT INTO railways SELECT * FROM dblink('dbname=gis_new','SELECT * FROM lowzoom_railways') AS t(way geometry(LineString,3857), railway text, \"service\" text, tunnel text);"
echo "3 done"
psql -d lowzoom_new -c "CREATE INDEX railways_way_idx ON railways USING GIST (way);"
echo "4 done (last)"
	
echo "End."