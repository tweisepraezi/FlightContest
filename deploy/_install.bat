@echo off

:: Thomas Weise
:: tweise.praeziflug@gmx.de

echo.
echo Install Flight Contest on Windows

set SETUPEXE=..\output\FCSetup.exe
if not exist "%SETUPEXE%" goto setuperror

echo.
pause

::-------------------------------------------------------------------
:fcsetup
echo.
echo Install Flight Contest...
%SETUPEXE% /silent
goto done

::-------------------------------------------------------------------
:setuperror
echo.
echo '%SETUPEXE%' not found.
echo Exit.
echo.
pause
goto :eof

::-------------------------------------------------------------------
:done
if exist "%USERPROFILE%\.grails\install.bat" call "%USERPROFILE%\.grails\install.bat"
echo.
echo Done.
pause
goto :eof


