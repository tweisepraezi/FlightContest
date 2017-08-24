@echo off

call _setup_names.bat

set scr_fc=K:\Projects\GIT\FlightContest\output\FCSetup.exe
set dest_fc=K:\Projects\EPJ\PJ11\_Projekt\_save\%FCSETUP_NAME%

:: -------------------------------------------------------------------------------------------
echo.
echo Save FCSetup
echo.
echo %scr_fc% to '%dest_fc%
if exist "%dest_fc%" echo.
if exist "%dest_fc%" echo Already exists.
if exist "%dest_fc%" echo.
if exist "%dest_fc%" pause
if exist "%dest_fc%" goto :eof
echo.
pause
echo. 
@echo on
copy %scr_fc% "%dest_fc%"
@echo off
echo.
pause