docker container rm showdbs
call ..\_env.bat
docker run --name showdbs --env PGHOST=%PGHOST% --env PGPORT=%PGPORT% --env PGUSER=%PGUSER% --env PGPASSWORD=%PGPASSWORD% createdb:latest
pause
