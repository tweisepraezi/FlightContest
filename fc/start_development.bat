@echo off

set startdir=%~dp0
cd ..\..\..
for /f %%i in ('cd') do set basedir=%%i
cd %startdir%

set JAVA_HOME=%basedir%\Java\openjdk-1.8.0.275x64
set RUBY_HOME=%basedir%\Ruby\ruby-2.3.3-x64-mingw32\bin
set GRAILS_HOME=%basedir%\Grails\Grails-2.5.6
set GRAILS_OPTS=-server -Xmx4096M -Xms768M -Dfile.encoding=UTF-8
set FC_DOCS=%startdir%docs
set FC_OUTPUT_DOCS=%startdir%web-app\docs
set FC_OUTPUT=%startdir%output
::set GDAL_BIN=%basedir%\gdal\gdal-3.2.1\bin;%basedir%\gdal\gdal-3.2.1\bin\gdal\java
set PROJ_LIB=%ProgramFiles%\GDAL\projlib
::set USE_PATH_FOR_GDAL_PYTHON=YES

echo.
set PATH=%PATH%;%JAVA_HOME%\bin;%GRAILS_HOME%\bin;%GDAL_BIN%
echo.
echo JAVA_HOME=%JAVA_HOME%
echo RUBY_HOME=%RUBY_HOME%
echo GRAILS_HOME=%GRAILS_HOME%
echo GRAILS_OPTS=%GRAILS_OPTS%
echo FC_DOCS=%FC_DOCS%
echo FC_OUTPUT_DOCS=%FC_OUTPUT_DOCS%
echo FC_OUTPUT=%FC_OUTPUT%
echo GDAL_BIN=%GDAL_BIN%
echo PROJ_LIB=%PROJ_LIB%
echo.
cmd
