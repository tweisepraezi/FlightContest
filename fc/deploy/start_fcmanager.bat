@echo off
setlocal
setlocal ENABLEEXTENSIONS
setlocal ENABLEDELAYEDEXPANSION

:: Thomas Weise
:: Version 1.0.0

:: ---------------------------------------------------------------------------------------------------------
:run

echo.
echo "Start FlightContest Manager"
cd %~dp0
powershell -WindowStyle hidden -ExecutionPolicy Unrestricted Start-Process -FilePath "powershell" -ArgumentList ".\fcmanager.ps1" -WindowStyle hidden

echo.
echo Done.
echo.
echo.
goto :EOF
