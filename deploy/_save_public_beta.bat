@echo off

call _setup_names.bat

set scr_fc1=K:\Projects\EPJ\PJ11\_Projekt\_save\%FCSETUP_NAME%
set scr_fc2=K:\Projects\GIT\FlightContest\readme.txt
set scr_fc3=K:\Projects\EPJ\PJ11\_Projekt\_save\%FCWAR_NAME%

if "%COMPUTERNAME%" =="TOM4" set dest_fc=H:\Data\Thomas\HiDrive\FlightContest\Flight Contest Beta
::if "%COMPUTERNAME%" =="TOM4" set dest_war=H:\Data\Thomas\HiDrive\FlightContest\Flight Contest Beta\Tomcat

if "%dest_fc%" == "" echo.
if "%dest_fc%" == "" echo Computer %COMPUTERNAME% not supported.
if "%dest_fc%" == "" echo.
if "%dest_fc%" == "" pause
if "%dest_fc%" == "" goto :eof

:: -------------------------------------------------------------------------------------------
echo.
echo Save FCSetup to Public Beta
echo.
echo %scr_fc1% to '%dest_fc%
echo %scr_fc2% to '%dest_fc%
::echo %scr_fc3% to '%dest_war%

if exist "%dest_fc%\%FCSETUP_NAME%" echo.
if exist "%dest_fc%\%FCSETUP_NAME%" echo "%dest_fc%\%FCSETUP_NAME%" already exists.
if exist "%dest_fc%\%FCSETUP_NAME%" echo.
if exist "%dest_fc%\%FCSETUP_NAME%" pause
if exist "%dest_fc%\%FCSETUP_NAME%" goto :eof

::if exist "%dest_war%\%FCWAR_NAME%" echo.
::if exist "%dest_war%\%FCWAR_NAME%" echo "%dest_war%\%FCWAR_NAME%" already exists.
::if exist "%dest_war%\%FCWAR_NAME%" echo.
::if exist "%dest_war%\%FCWAR_NAME%" pause
::if exist "%dest_war%\%FCWAR_NAME%" goto :eof

echo.
pause
echo. 
@echo on
copy %scr_fc1% "%dest_fc%"
copy %scr_fc2% "%dest_fc%"
::copy %scr_fc3% "%dest_war%"
@echo off
echo.
pause