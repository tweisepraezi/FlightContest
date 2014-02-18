@echo off

:: Thomas Weise
:: tweise.praeziflug@gmx.de

echo.
echo Build Flight Contest installer for Windows

set startdir=%~dp0
cd ..\..\..
for /f %%i in ('cd') do set basedir=%%i
cd %startdir%

set JAVA_HOME=%basedir%\Java\jdk1.6.0_30
set GRAILS_HOME=%basedir%\Grails\grails-2.3.4
set SETUPEXE=%ProgramFiles(x86)%\Inno Setup 5\iscc.exe
if not exist "%SETUPEXE%" set SETUPEXE=%ProgramFiles%\Inno Setup 5\iscc.exe

if not exist "%JAVA_HOME%" goto javaerror
if not exist "%GRAILS_HOME%" goto grailserror
if not exist "%SETUPEXE%" goto setuperror

set PATH=%PATH%;%JAVA_HOME%\bin;%GRAILS_HOME%\bin

echo.
echo JAVA_HOME=%JAVA_HOME%
echo GRAILS_HOME=%GRAILS_HOME%
echo SETUPEXE=%SETUPEXE%
echo.
pause

::-------------------------------------------------------------------
:grails
echo.
echo Build fc.war...
cd ..\fc
echo.
call grails war
goto innosetup

::-------------------------------------------------------------------
:innosetup
echo.
echo Build FCSetup.exe...
cd ..\deploy
"%SETUPEXE%" fc.is5
goto done

::-------------------------------------------------------------------
:javaerror
echo.
echo '%JAVA_HOME%' not found. 
echo Exit.
echo.
pause
goto :eof

::-------------------------------------------------------------------
:grailserror
echo.
echo '%GRAILS_HOME%' not found. 
echo Exit.
echo.
pause
goto :eof

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
echo.
echo Done.
pause
goto :eof
