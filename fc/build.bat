@echo off

:: Thomas Weise
:: tweise.praeziflug@gmx.de

echo.
echo Build Flight Contest installer for Windows

call deploy\setup_names.bat

set startdir=%~dp0
cd ..\..\..
for /f %%i in ('cd') do set basedir=%%i
cd %startdir%

for /f %%a in ('powershell -Command "Get-Date -format yyyy-MM-ddTHH-mm-ss"') do set BUILD_TIME=%%a

set JAVA_HOME=%basedir%\Java\openjdk-1.8.0.275x64
set RUBY_HOME=%basedir%\Ruby\ruby-2.3.3-x64-mingw32\bin
set GRAILS_HOME=%basedir%\Grails\Grails-2.5.6
set GRAILS_OPTS=-server -Xmx4096M -Xms768M -Dfile.encoding=UTF-8
set SETUPEXE=%ProgramFiles(x86)%\Inno Setup 6\iscc.exe
set TOUCHEXE=%basedir%\Touch\touch.exe
set SAVEDIR=%basedir%\EPJ\PJ11\_Projekt\_save

if not exist "%JAVA_HOME%" goto javaerror
if not exist "%RUBY_HOME%" goto rubyerror
if not exist "%GRAILS_HOME%" goto grailserror
if not exist "%SETUPEXE%" goto setuperror
if not exist "%TOUCHEXE%" goto setuperror

set PATH=%PATH%;%JAVA_HOME%\bin;%GRAILS_HOME%\bin

set FC_DOCS=%startdir%docs
set FC_OUTPUT_DOCS=%startdir%web-app\docs
set FC_OUTPUT=%startdir%output

echo.
echo BUILD_TIME=%BUILD_TIME%
echo DEPLOY_TIME=%DEPLOY_TIME%
echo FCSETUP_NAME=%FCSETUP_NAME%
echo FCWAR_NAME=%FCWAR_NAME%
echo SAVEDIR=%SAVEDIR%
echo.
echo JAVA_HOME=%JAVA_HOME%
echo RUBY_HOME=%RUBY_HOME%
echo GRAILS_HOME=%GRAILS_HOME%
echo GRAILS_OPTS=%GRAILS_OPTS%
echo SETUPEXE=%SETUPEXE%
echo TOUCHEXE=%TOUCHEXE%
echo FC_DOCS=%FC_DOCS%
echo FC_OUTPUT_DOCS=%FC_OUTPUT_DOCS%
echo FC_OUTPUT=%FC_OUTPUT%
if "%1" == "-nowar" echo war generation disabled.
echo.
pause

if "%1" == "-nowar" goto innosetup
if "%1" == "-deploy" goto deploy

::-------------------------------------------------------------------
:removefiles
echo.
echo Remove files...
echo.
del /Q web-app\gpxupload\*
del /Q web-app\jobs\*
del /Q web-app\jobs\done\*
del /Q web-app\jobs\error\*
del /Q web-app\live\*

::-------------------------------------------------------------------
:buildhelp
call build_html_help.bat
call build_pdf_help.bat
call build_html_fcmaps.bat
call build_pdf_fcmaps.bat

::-------------------------------------------------------------------
:setdeploytime1

echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- deploy\readme.txt
echo.

::-------------------------------------------------------------------
:grails
echo.
echo Build fc.war...
echo.
call grails prod clean
call grails prod war

::-------------------------------------------------------------------
:innosetup
echo.
echo Build FCSetup.exe...
cd deploy
"%SETUPEXE%" fc.is6
cd %startdir%

::-------------------------------------------------------------------
:setdeploytime2
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- output\FCSetup.exe
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- output\fc.war
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- output\help.pdf
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- output\help_fcmaps.pdf
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- deploy\readme_tracking.txt
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- deploy\readme_en.txt
echo.

::-------------------------------------------------------------------
:deploy
copy output\FCSetup.exe %SAVEDIR%\%FCSETUP_NAME%.%BUILD_TIME%
copy output\fc.war %SAVEDIR%\%FCWAR_NAME%.%BUILD_TIME%
goto done

::-------------------------------------------------------------------
:javaerror
echo.
echo '%JAVA_HOME%' not found. 
echo Exit.
echo.
::pause
goto :eof

::-------------------------------------------------------------------
:rubyerror
echo.
echo '%RUBY_HOME%' not found. 
echo Exit.
echo.
::pause
goto :eof

::-------------------------------------------------------------------
:grailserror
echo.
echo '%GRAILS_HOME%' not found. 
echo Exit.
echo.
::pause
goto :eof

::-------------------------------------------------------------------
:setuperror
echo.
echo '%SETUPEXE%' not found.
echo Exit.
echo.
::pause
goto :eof

::-------------------------------------------------------------------
:done
echo.
echo Done.
::pause
goto :eof
