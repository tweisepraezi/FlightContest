#!/bin/bash

dropdb lowzoom2

# water
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_water;"


# landuse
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_landuse;"
	
	
# roads
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_roads;"


# borders
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_borders;"


# railways
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_railways;"
	
	
# cities and towns
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_cities;"


# water polygon labels
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_lakelabel;"
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_baylabel;"
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_straitplabel;"
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_straitllabel;"
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_glacierlabel;"


# natural area labels
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_natural_areas;"
psql -d gis2 -c "DROP VIEW IF EXISTS lowzoom_natural_lines;"

