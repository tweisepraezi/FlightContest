@echo off
setlocal
setlocal ENABLEEXTENSIONS
setlocal ENABLEDELAYEDEXPANSION

:: Thomas Weise
:: Version 1..0.0

:: ---------------------------------------------------------------------------------------------------------
:run

echo.
echo "Start FlightContest"
net start FlightContest

echo.
echo Done.
echo.
echo.
pause
goto :EOF
