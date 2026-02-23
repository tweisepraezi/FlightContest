docker container rm datetime
call ..\_env.bat
docker run --name datetime --env PGHOST=%PGHOST% --env PGPORT=%PGPORT% --env PGUSER=%PGUSER% --env PGPASSWORD=%PGPASSWORD% --env dbid=%dbid% --env PBFTIME=%PBFTIME% --env PBFDATE=%PBFDATE% createdb:latest
pause