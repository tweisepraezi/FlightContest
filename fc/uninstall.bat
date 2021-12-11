@echo off

:: Thomas Weise
:: tweise.praeziflug@gmx.de

echo.
echo Uninstall Flight Contest on Windows

set UNINSTALLEXE="C:\Program Files\Flight Contest\unins000.exe"
if not exist "%UNINSTALLEXE%" goto setuperror

echo.
::pause

::-------------------------------------------------------------------
:fcsetup

::echo.
::echo Uninstall Flight Contest...
::"%ProgramFiles%\Flight Contest\unins000.exe" /silent

echo.
echo Uninstall Flight Contest...
%UNINSTALLEXE% /silent
goto done

::-------------------------------------------------------------------
:setuperror
echo.
echo '%UNINSTALLEXE%' not found.
echo Exit.
echo.
::pause
goto :eof

::-------------------------------------------------------------------
:done
set post_install="C:\FCSave\.fc\install_setup.bat"
if exist %post_install% call %post_install%
echo.
echo Done.
::pause
goto :eof


