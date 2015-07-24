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
call :SETDATETIME

set fcdb_dir=%ProgramFiles%\Flight Contest\fc
if exist "%ProgramFiles(x86)%" set fcdb_dir=%ProgramFiles(x86)%\Flight Contest\fc

set fcdb_name=fcdb.h2.db
set fcdb_lock_name=fcdb.lock.db

echo Save Flight Contest database.
echo.

if not exist "%dest_dir%" goto dest_dir_error
if not exist "%fcdb_dir%\%fcdb_name%" goto fcdb_error

set stop_fcdb=0
if exist "%fcdb_dir%\%fcdb_lock_name%" set stop_fcdb=1

echo.
if "%stop_fcdb%" == "1" echo "Stop FlightContest"
if "%stop_fcdb%" == "1" net stop FlightContest
echo Copy '%fcdb_dir%\%fcdb_name%' to '%dest_dir%'
copy "%fcdb_dir%\%fcdb_name%" "%dest_dir%"
ren "%dest_dir%\%fcdb_name%" %cdate%-%ctime%-%fcdb_name%
if "%stop_fcdb%" == "1" echo.
if "%stop_fcdb%" == "1" echo "Start FlightContest"
if "%stop_fcdb%" == "1" net start FlightContest

set fcdb_saved_name=%dest_dir%\%cdate%-%ctime%-%fcdb_name%
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
::#### set the ISO conform date and time variables
:SETDATETIME
set T=
set US=
::## check for US date formating
for /f %%a in ('echo %DATE% ^| findstr /i "\."') do if "%%a"=="" (set US=1)
for /f "tokens=1,2,3,4 delims=./ " %%a in ("%DATE%") do (if "%US%"=="1" set CDATE=%%d%%b%%c) & (if "%US%"=="" set CDATE=%%c%%b%%a)
for /f "tokens=1,2,3 delims=:,. " %%a in ("%TIME%") do (set /a "T=100+%%a") & (set CTIME=!T:~1,2!%%b%%c)
set CDATE=%CDATE: =%
set CTIME=%CTIME: =%
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
