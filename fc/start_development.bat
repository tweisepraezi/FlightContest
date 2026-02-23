@echo off

set startdir=%~dp0
cd ..\..\..
for /f %%i in ('cd') do set basedir=%%i
cd %startdir%

call _env.bat
::@echo off
::set PGPASSWORD=...

set JAVA_HOME=%basedir%\Java\jdk8u442-b06
set ASCIIDOCTOR_HOME=%basedir%\AsciiDoctorJ\asciidoctorj-2.5.1
set GRAILS_HOME=%basedir%\Grails\Grails-2.5.6
set GRAILS_OPTS=-server -Xmx4096M -Xms768M -Dfile.encoding=UTF-8 -Dgrails.server.port.http=8080
set FC_DOCS=%startdir%docs
set FC_OUTPUT_DOCS=%startdir%web-app\docs
set FC_OUTPUT=%startdir%output
set POSTGRESQL_HOME=C:\Program Files\PostgreSQL\17

echo.
set PATH=%JAVA_HOME%\bin;%GRAILS_HOME%\bin;%POSTGRESQL_HOME%\bin;%PATH%
echo.
echo JAVA_HOME=%JAVA_HOME%
echo ASCIIDOCTOR_HOME=%ASCIIDOCTOR_HOME%
echo GRAILS_HOME=%GRAILS_HOME%
echo GRAILS_OPTS=%GRAILS_OPTS%
echo FC_DOCS=%FC_DOCS%
echo FC_OUTPUT_DOCS=%FC_OUTPUT_DOCS%
echo FC_OUTPUT=%FC_OUTPUT%
echo POSTGRESQL_HOME=%POSTGRESQL_HOME%
echo.
cmd
