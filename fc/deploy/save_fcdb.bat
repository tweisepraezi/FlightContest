@echo off
setlocal
setlocal ENABLEEXTENSIONS
setlocal ENABLEDELAYEDEXPANSION

:: Thomas Weise
:: Version 1.1.0

:: Directory where Flight Contest databases will be saved.
set dest_dir=C:\FCSave

:: ---------------------------------------------------------------------------------------------------------
:run
echo Save Flight Contest database.
echo.

for /f %%a in ('powershell -Command "Get-Date -format yyyy-MM-dd--HH-mm-ss"') do set DATETIME=%%a

set fcdb_dir=%ProgramFiles%\Flight Contest\fc

set fcdb_name=fcdb.h2.db
set fcdb_lock_name=fcdb.lock.db

if not exist "%dest_dir%" goto dest_dir_error
if not exist "%fcdb_dir%\%fcdb_name%" goto fcdb_error

set stop_fcdb=0
if exist "%fcdb_dir%\%fcdb_lock_name%" set stop_fcdb=1

echo.
if "%stop_fcdb%" == "1" echo "Stop FlightContest"
if "%stop_fcdb%" == "1" net stop FlightContest
echo Copy '%fcdb_dir%\%fcdb_name%' to '%dest_dir%'
copy "%fcdb_dir%\%fcdb_name%" "%dest_dir%"
ren "%dest_dir%\%fcdb_name%" %DATETIME%--%fcdb_name%
if "%stop_fcdb%" == "1" echo.
if "%stop_fcdb%" == "1" echo "Start FlightContest"
if "%stop_fcdb%" == "1" net start FlightContest

set fcdb_saved_name=%dest_dir%\%DATETIME%--%fcdb_name%
if not exist "%fcdb_saved_name%" goto fcdb_saved_error

echo.
echo Done.
echo.
echo Database saved successfully to '%fcdb_saved_name%'.
echo.
echo.
pause
goto :EOF

:: ---------------------------------------------------------------------------------------------------------
:dest_dir_error
echo.
echo ERROR: Directory '%dest_dir%' does not exist.
echo.
echo Copy this batch to desktop and change variable 'dest_dir'.
echo.
echo.
pause
goto :EOF

:: ---------------------------------------------------------------------------------------------------------
:fcdb_error
echo.
echo ERROR: Database '%fcdb_dir%\%fcdb_name%' does not exist.
echo.
echo.
pause
goto :EOF

:: ---------------------------------------------------------------------------------------------------------
:fcdb_run_error
echo.
echo ERROR: Database '%fcdb_dir%\%fcdb_name%' is in use.
echo.
echo Stop 'Apache Tomcat FlightContest' and try again.
echo.
echo.
pause
goto :EOF

:: ---------------------------------------------------------------------------------------------------------
:fcdb_saved_error
echo.
echo ERROR: Database not saved.
echo.
echo '%fcdb_saved_name%' not found.
echo.
echo.
pause
goto :EOF
