@echo off
setlocal
setlocal ENABLEEXTENSIONS
setlocal ENABLEDELAYEDEXPANSION

:: Thomas Weise
:: Version 1.0.0

:: ---------------------------------------------------------------------------------------------------------
:run
echo.
echo Read logger GlobalSat DG-200.
echo.

for /f %%a in ('powershell -Command "Get-Date -format yyyyMMdd"') do set date=%%a

set gpsbabel=%ProgramFiles%\GPSBabel\gpsbabel.exe
if not exist "%gpsbabel%" goto gpsbabel_error

:: port
echo Available ports:
powershell -Command "Get-PnpDevice -Class 'Ports' -PresentOnly"
set port=COM3
set /p port=Enter com port (Default %port%): 

:: start number
set start_num=1
set /p start_num=Enter start number (Default %start_num%): 

:: date
set /p date=Date %date%: 

set start_time=%date%020000
set end_time=%date%220000
set filename=C:\FCSave\.logger\%start_num%.gpx
set gpsbabel_call="%gpsbabel%" -t -i dg-200 -f %port% -x track,start=%start_time%,stop=%end_time% -o gpx -F %filename%

echo.
echo Port: %port%
echo Start time: %start_time%
echo End time: %end_time%
echo.

echo Read logger
%gpsbabel_call%
echo.
echo Saved logger data: %filename%
echo.
echo Done.
echo.
pause
goto :EOF

:: ---------------------------------------------------------------------------------------------------------
:gpsbabel_error
echo.
echo ERROR: Application '%gpsbabel%' does not exist.
echo.
pause
goto :EOF
