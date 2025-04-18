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
	
# natural area labels
echo "Create lines for labels of natural areas..."
psql -d gis_new -c "DROP VIEW IF EXISTS lowzoom_natural_lines;"
echo "1 done"
psql -d gis_new -c "DROP VIEW IF EXISTS lowzoom_natural_areas;"
echo "2 done"
psql -d gis_new -c "CREATE VIEW lowzoom_natural_areas AS SELECT natural_arealabel(osm_id,way) as way,name,areatype,way_area,(hierarchicregions).nextregionsize AS nextregionsize,(hierarchicregions).subregionsize AS subregionsize FROM (SELECT osm_id,way,name,(CASE WHEN \"natural\" IS NOT NULL THEN \"natural\" ELSE \"region:type\" END) AS areatype, way_area, OTM_Next_Natural_Area_Size(osm_id,way_area,way) AS hierarchicregions FROM planet_osm_polygon WHERE (\"region:type\" IN ('natural_area','mountain_area') OR \"natural\" IN ('massif', 'mountain_range', 'valley','couloir','ridge','arete','gorge','canyon')) AND name IS NOT NULL) AS natural_areas;"
echo "3 done"
psql -d gis_new -c "CREATE VIEW lowzoom_natural_lines AS SELECT way,name,areatype,way_area,(hierarchicregions).nextregionsize AS nextregionsize,(hierarchicregions).subregionsize AS subregionsize FROM (SELECT osm_id,way,name,\"natural\" AS areatype,ST_Length(way)*ST_Length(way)/10 as way_area, OTM_Next_Natural_Area_Size(osm_id,0.0,way) AS hierarchicregions FROM planet_osm_line AS li WHERE \"natural\" IN ('massif', 'mountain_range', 'valley','couloir','ridge','arete','gorge','canyon') AND name IS NOT NULL AND NOT EXISTS (SELECT osm_id FROM planet_osm_polygon AS po WHERE po.osm_id=li.osm_id )) AS natural_lines;"
echo "4 done"
psql -d lowzoom_new -c "CREATE TABLE naturalarealabels (way geometry(LineString,3857), name text, areatype text, way_area real,nextregionsize real,subregionsize real);"
echo "5 done"
psql -d lowzoom_new -c "INSERT INTO naturalarealabels SELECT * FROM dblink('dbname=gis_new','SELECT * FROM lowzoom_natural_areas') AS t(way geometry(LineString,3857), name text, areatype text, way_area real,nextregionsize real,subregionsize real);"
echo "6 done"
psql -d lowzoom_new -c "INSERT INTO naturalarealabels SELECT * FROM dblink('dbname=gis_new','SELECT * FROM lowzoom_natural_lines') AS t(way geometry(LineString,3857), name text, areatype text, way_area real,nextregionsize real,subregionsize real);"
echo "7 done"
psql -d lowzoom_new -c "CREATE INDEX naturalarealabels_way_idx ON naturalarealabels USING GIST (way);"
echo "8 done (last)"
	
# water polygon labels
echo "Create lines for labels of water polygons..."
psql -d gis_new -c "CREATE VIEW lowzoom_lakelabel    AS SELECT arealabel(osm_id,way) AS way,name,'lakeaxis'::text    AS label,way_area FROM planet_osm_polygon WHERE (\"natural\" = 'water' OR water='lake' OR landuse IN ('basin','reservoir')) AND name IS NOT NULL;"
echo "1 done"
psql -d gis_new -c "CREATE VIEW lowzoom_baylabel     AS SELECT arealabel(osm_id,way) AS way,name,'bayaxis'::text     AS label,way_area FROM planet_osm_polygon WHERE  \"natural\" = 'bay' AND name IS NOT NULL;"
echo "2 done"
psql -d gis_new -c "CREATE VIEW lowzoom_straitplabel AS SELECT arealabel(osm_id,way) AS way,name,'straitaxis'::text  AS label,way_area FROM planet_osm_polygon WHERE  \"natural\" = 'strait' AND name IS NOT NULL;"
echo "3 done"
psql -d gis_new -c "CREATE VIEW lowzoom_straitllabel AS SELECT ST_LineMerge(longway) AS way,name,'straitaxis'::text AS label,len*len/10 as way_area FROM (SELECT ST_Collect(way) AS longway,SUM(ST_Length(way)) AS len,MAX(name) AS name FROM planet_osm_line WHERE \"natural\"='strait' AND name is NOT NULL GROUP BY osm_id) AS t;"
echo "4 done"
psql -d gis_new -c "CREATE VIEW lowzoom_glacierlabel AS SELECT arealabel(osm_id,way) AS way,name,'glacieraxis'::text AS label,way_area FROM planet_osm_polygon WHERE  \"natural\" = 'glacier' AND name IS NOT NULL;"
echo "5 done"
psql -d lowzoom_new -c "CREATE TABLE lakelabels (way geometry(LineString,3857), name text, label text, lake_area real);"
echo "6 done"
psql -d lowzoom_new -c "INSERT INTO lakelabels SELECT * FROM dblink('dbname=gis_new','SELECT * FROM lowzoom_lakelabel')    AS t(way geometry(LineString,3857), name text, label text, way_area real);"
echo "7 done"
psql -d lowzoom_new -c "INSERT INTO lakelabels SELECT * FROM dblink('dbname=gis_new','SELECT * FROM lowzoom_baylabel')     AS t(way geometry(LineString,3857), name text, label text, way_area real);"
echo "8 done"
psql -d lowzoom_new -c "INSERT INTO lakelabels SELECT * FROM dblink('dbname=gis_new','SELECT * FROM lowzoom_straitplabel') AS t(way geometry(LineString,3857), name text, label text, way_area real);"
echo "9 done"
psql -d lowzoom_new -c "INSERT INTO lakelabels SELECT * FROM dblink('dbname=gis_new','SELECT * FROM lowzoom_straitllabel') AS t(way geometry(LineString,3857), name text, label text, way_area real);"
echo "10 done"
psql -d lowzoom_new -c "INSERT INTO lakelabels SELECT * FROM dblink('dbname=gis_new','SELECT * FROM lowzoom_glacierlabel') AS t(way geometry(LineString,3857), name text, label text, way_area real);"
echo "11 done"
psql -d lowzoom_new -c "CREATE INDEX lakelabels_way_idx ON lakelabels USING GIST (way);"
echo "12 done (last)"

echo "End."