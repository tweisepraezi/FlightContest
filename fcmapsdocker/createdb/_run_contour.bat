docker container rm contour
call ..\_env.bat
docker run --name contour --env dbid=%dbid% --env PGHOST=%PGHOST% --env PGPORT=%PGPORT% --env PGUSER=%PGUSER% --env PGPASSWORD=%PGPASSWORD% --env MINLON=6.24522242525 --env MINLAT=48.9348891599 --env MAXLON=7.83147757475 --env MAXLAT=49.9660696535 --env CONTOURSOURCES=%CONTOURSOURCES% --env SRTMUSER=%SRTMUSER% --env SRTMPASSWORD=%SRTMPASSWORD% createdb:latest
pause
