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

set JAVA_HOME=%basedir%\Java\jdk8u442-b06
set ASCIIDOCTOR_HOME=%basedir%\AsciiDoctorJ\asciidoctorj-2.5.1
set GRAILS_HOME=%basedir%\Grails\Grails-2.5.6
set GRAILS_OPTS=-server -Xmx4096M -Xms768M -Dfile.encoding=UTF-8
set SETUPEXE=%ProgramFiles(x86)%\Inno Setup 6\iscc.exe
set TOUCHEXE=%basedir%\Touch\touch.exe
::set GDAL_BIN=%basedir%\gdal\gdal-3.2.1\bin
::set GDAL_BIN2=%basedir%\gdal\gdal-3.2.1\bin\gdal\java
::set PROJ_LIB=%basedir%\gdal\gdal-3.2.1\bin\proj7\share
set SAVEDIR=%startdir%\save

if not exist "%JAVA_HOME%" goto javaerror
if not exist %ASCIIDOCTOR_HOME% goto asciidoctorerror
if not exist "%GRAILS_HOME%" goto grailserror
if not exist "%SETUPEXE%" goto setuperror
if not exist "%TOUCHEXE%" goto setuperror
::if not exist "%GDAL_BIN%" goto setuperror
::if not exist "%GDAL_BIN2%" goto setuperror
::if not exist "%PROJ_LIB%" goto setuperror

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
echo ASCIIDOCTOR_HOME=%ASCIIDOCTOR_HOME%
echo GRAILS_HOME=%GRAILS_HOME%
echo GRAILS_OPTS=%GRAILS_OPTS%
echo SETUPEXE=%SETUPEXE%
echo TOUCHEXE=%TOUCHEXE%
::echo GDAL_BIN=%GDAL_BIN%
::echo GDAL_BIN2=%GDAL_BIN2%
::echo PROJ_LIB=%PROJ_LIB%
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
rmdir /Q /S web-app\gpxupload
md web-app\gpxupload
attrib +h web-app\gpxupload
rmdir /Q /S web-app\map
md web-app\map
attrib +h web-app\map
del /Q web-app\jobs\*
del /Q web-app\jobs\done\*
del /Q web-app\jobs\error\*
del /Q web-app\live\*

::-------------------------------------------------------------------
:createfolder
if not exist output md output
if not exist save md save

::-------------------------------------------------------------------
:buildhelp
call build_html_help.bat
call build_html_help_en.bat
call build_pdf_help.bat
call build_pdf_help_en.bat
call build_html_fcmaps.bat
call build_pdf_fcmaps.bat

::-------------------------------------------------------------------
:buildflightcontestmanager
call build_flightcontestmanager.bat

::-------------------------------------------------------------------
:setdeploytime1

echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- deploy\readme.txt
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- deploy\readme_en.txt
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- deploy\readme_tracking.txt
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- deploy\readme_maps.txt
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- deploy\FCSetup4-NewVersion.txt
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- deploy\FCSetup-NewVersion.txt
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
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- output\help_en.pdf
echo.
call %TOUCHEXE% -xamv -t %DEPLOY_TIME% -- output\help_fcmaps.pdf
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
:asciidoctorerror
echo.
echo '%ASCIIDOCTOR_HOME%' not found. 
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
if not exist "%SETUPEXE%" echo '%SETUPEXE%' not found.
if not exist "%TOUCHEXE%" echo '%TOUCHEXE%' not found.
if not exist "%GDAL_BIN%" echo '%GDAL_BIN%' not found.
if not exist "%GDAL_BIN2%" echo '%GDAL_BIN2%' not found.
::if not exist "%PROJ_LIB%" echo '%PROJ_LIB%' not found.
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
