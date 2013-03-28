@echo off
setlocal
setlocal ENABLEEXTENSIONS
setlocal ENABLEDELAYEDEXPANSION

:: Thomas Weise
:: Version 1.0.0

:: Directory where prints will be saved.
set dest_dir=C:\FCSave

:: Directory where downloaded prints has been saved.
set print_dir=%TEMP%

:: Prefix and suffix of Flight Contest prints.
set print_prefix=fc-
set print_suffix=*.pdf

:: ---------------------------------------------------------------------------------------------------------
:run
call :SETDATETIME

echo Save new Flight Contest prints.
echo.

if not exist "%dest_dir%" goto dest_dir_error
if not exist "%print_dir%" goto print_dir_error

:: /D:m-d-y  Copies files changed on or after the specified date.
::           If no date is given, copies only those files whose
::           source time is newer than the destination time.
:: /C        Continues copying even if errors occur.
:: /F        Displays full source and destination file names while copying.
:: /L        Displays files that would be copied.

set flag1=/L /D /C /F
set flag2=   /D /C /F

xcopy "%print_dir%\%print_prefix%%print_suffix%" "%dest_dir%" %flag2%

echo.
echo Done.
echo.
echo Prints saved successfully to '%dest_dir%'.
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
:print_dir_error
echo.
echo ERROR: Directory '%print_dir%' does not exist.
echo.
echo Copy this batch to desktop and change variable 'print_dir'.
echo.
echo.
pause
goto :EOF
