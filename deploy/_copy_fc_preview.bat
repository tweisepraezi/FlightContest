@echo off

set scr_fc="K:\Projects\GIT\FlightContest\output\FCSetup.exe"

if "%COMPUTERNAME%" =="TOM4" set dest_fc=D:\Data\Thomas\HiDrive\FlightContest\Preview

:: -------------------------------------------------------------------------------------------
echo.
echo Copy FCSetup preview
echo.
echo Copy actions:
echo   '%scr_fc%' to '%dest_fc%'
echo.
pause
echo. 
@echo on
copy %scr_fc% "%dest_fc%"
@echo off
echo.
pause