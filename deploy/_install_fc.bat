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
echo Uninstall Flight Contest...
"%ProgramFiles(x86)%\Flight Contest\unins000.exe" /silent

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
set post_install="C:\FCSave\.fc\install_setup.bat"
if exist %post_install% call %post_install%
echo.
echo Done.
pause
goto :eof


