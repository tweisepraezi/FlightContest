docker container rm osm
call ..\_env.bat
docker run --name osm --env dbid=%dbid% --env PGHOST=%PGHOST% --env PGPORT=%PGPORT% --env PGUSER=%PGUSER% --env PGPASSWORD=%PGPASSWORD% --env MINLON=6.24522242525 --env MINLAT=48.9348891599 --env MAXLON=7.83147757475 --env MAXLAT=49.9660696535 --env PBFLINKS="https://download.geofabrik.de/europe/germany/saarland-latest.osm.pbf" createdb:latest
pause