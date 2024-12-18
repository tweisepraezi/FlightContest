@echo off

set startdir=%~dp0
cd ..\..\..
for /f %%i in ('cd') do set basedir=%%i
cd %startdir%

set JAVA_HOME=%basedir%\Java\jdk8u422-b05
set ASCIIDOCTOR_HOME=%basedir%\AsciiDoctorJ\asciidoctorj-2.5.1
set GRAILS_HOME=%basedir%\Grails\Grails-2.5.6
set GRAILS_OPTS=-server -Xmx4096M -Xms768M -Dfile.encoding=UTF-8 -Dgrails.server.port.http=8080
set FC_DOCS=%startdir%docs
set FC_OUTPUT_DOCS=%startdir%web-app\docs
set FC_OUTPUT=%startdir%output
set GDAL_BIN=C:\Program Files\GDAL
::set GDAL_BIN=%basedir%\gdal\gdal-3.2.1\bin;%basedir%\gdal\gdal-3.2.1\bin\gdal\java
set PROJ_LIB=%ProgramFiles%\GDAL\projlib
::set USE_PATH_FOR_GDAL_PYTHON=YES

echo.
set PATH=%PATH%;%JAVA_HOME%\bin;%GRAILS_HOME%\bin;%GDAL_BIN%
echo.
echo JAVA_HOME=%JAVA_HOME%
echo ASCIIDOCTOR_HOME=%ASCIIDOCTOR_HOME%
echo GRAILS_HOME=%GRAILS_HOME%
echo GRAILS_OPTS=%GRAILS_OPTS%
echo FC_DOCS=%FC_DOCS%
echo FC_OUTPUT_DOCS=%FC_OUTPUT_DOCS%
echo FC_OUTPUT=%FC_OUTPUT%
echo GDAL_BIN=%GDAL_BIN%
echo PROJ_LIB=%PROJ_LIB%
echo.
cmd
