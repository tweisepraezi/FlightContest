@echo off
setlocal
setlocal ENABLEEXTENSIONS
setlocal ENABLEDELAYEDEXPANSION

:: Thomas Weise
:: Version 1..0.0

:: ---------------------------------------------------------------------------------------------------------
:run

echo.
echo "Stop FlightContest"
net stop FlightContest

echo.
echo Done.
echo.
echo.
pause
goto :EOF
