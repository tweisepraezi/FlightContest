@echo off

call _setup_names.bat

set scr_fc1=K:\Projects\EPJ\PJ11\_Projekt\_save\%FCSETUP_NAME%
set scr_fc2=K:\Projects\GIT\FlightContest\readme.txt
set scr_fc3=K:\Projects\EPJ\PJ11\_Projekt\_save\%FCWAR_NAME%
set scr_fc4=K:\Projects\GIT\FlightContest\fc\web-app\docs\help.pdf
set scr_fc5=K:\Projects\GIT\FlightContest\fc\web-app\docs\help_fcmaps.pdf

if "%COMPUTERNAME%" =="TOM4" set dest_fc=H:\Data\Thomas\HiDrive\FlightContest\Flight Contest Download
::if "%COMPUTERNAME%" =="TOM4" set dest_war=H:\Data\Thomas\HiDrive\FlightContest\Flight Contest Download\Tomcat
if "%COMPUTERNAME%" =="TOM6" set dest_fc=C:\Users\tweis\HiDrive\FlightContest\Flight Contest Download
::if "%COMPUTERNAME%" =="TOM6" set dest_war=C:\Users\tweis\HiDrive\FlightContest\Flight Contest Download\Tomcat

if "%dest_fc%" == "" echo.
if "%dest_fc%" == "" echo Computer %COMPUTERNAME% not supported.
if "%dest_fc%" == "" echo.
if "%dest_fc%" == "" pause
if "%dest_fc%" == "" goto :eof

:: -------------------------------------------------------------------------------------------
echo.
echo Save FCSetup to Public Download
echo.
echo %scr_fc1% to '%dest_fc%
echo %scr_fc2% to '%dest_fc%
::echo %scr_fc3% to '%dest_war%
echo %scr_fc4% to '%dest_fc%
echo %scr_fc5% to '%dest_fc%

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
copy %scr_fc4% "%dest_fc%"
copy %scr_fc5% "%dest_fc%"
@echo off
echo.
pause