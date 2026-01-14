docker container rm printmaps
call ..\_env.bat
docker run --name printmaps --env dbid=%dbid% --env PGHOST=%PGHOST% --env PGPORT=%PGPORT% --env PGUSER=%PGUSER% --env PGPASSWORD=%PGPASSWORD% -p 127.0.0.1:8181:8181 printmaps:latest
::pause

:: C:\FCSave\.fc\config.groovy
::  maps {
::    fcmap {
::       server = "http://localhost:8181/api/beta2/maps" // Print server address
::    }
::  }

:: https://docs.docker.com/reference/cli/docker/container/run/