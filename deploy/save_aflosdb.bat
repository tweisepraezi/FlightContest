@echo off
setlocal
setlocal ENABLEEXTENSIONS
setlocal ENABLEDELAYEDEXPANSION

:: Thomas Weise
:: Version 1.0.0

:: Directory where AFLOS databases will be saved.
set dest_dir=C:\FCSave

:: Directory where AFLOS has been installed.
set aflos_dir=C:\AFLOS

:: ---------------------------------------------------------------------------------------------------------
:run
call :SETDATETIME

set aflosdb_dir=%aflos_dir%\AFLOS_System
set aflosdb_name=AFLOS.mdb
set aflosdb_lock_name=AFLOS.ldb

echo Save AFLOS database.
echo.

if not exist "%dest_dir%" goto dest_dir_error
if not exist "%aflosdb_dir%" goto aflos_dir_error
if not exist "%aflosdb_dir%\%aflosdb_name%" goto aflosdb_error
if exist "%aflosdb_dir%\%aflosdb_lock_name%" goto aflosdb_run_error

echo.
echo   Copy '%aflosdb_dir%\%aflosdb_name%' to '%dest_dir%'
copy "%aflosdb_dir%\%aflosdb_name%" "%dest_dir%"
ren "%dest_dir%\%aflosdb_name%" %cdate%-%ctime%-%aflosdb_name%

set aflosdb_saved_name=%dest_dir%\%cdate%-%ctime%-%aflosdb_name%
if not exist "%aflosdb_saved_name%" goto aflosdb_saved_error

echo.
echo Done.
echo.
echo Database saved successfully to '%aflosdb_saved_name%'.
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
:aflos_dir_error
echo.
echo ERROR: Directory '%aflos_dir%' does not exist.
echo.
echo Install AFLOS.
echo.
echo.
pause
goto :EOF

:: ---------------------------------------------------------------------------------------------------------
:aflosdb_error
echo.
echo ERROR: Database '%aflosdb_dir%\%aflosdb_name%' does not exist.
echo.
echo.
pause
goto :EOF

:: ---------------------------------------------------------------------------------------------------------
:aflosdb_run_error
echo.
echo ERROR: Database '%aflosdb_dir%\%aflosdb_name%' is in use.
echo.
echo Quiet AFLOS and try again.
echo.
echo.
pause
goto :EOF

:: ---------------------------------------------------------------------------------------------------------
:aflosdb_saved_error
echo.
echo ERROR: Database not saved.
echo.
echo '%aflosdb_saved_name%' not found.
echo.
echo.
pause
goto :EOF
