@echo off

rem Thomas Weise
rem Version 0.2.0
rem 19.08.2009

set PRGDIR=%~dp0

set JAVA_HOME=%PRGDIR%java
set path=%JAVA_HOME%\bin;%path%

set CATALINA_HOME=%PRGDIR%tomcat

set WORKINGDIR=%PRGDIR%fc

rem echo JAVA_HOME=%JAVA_HOME%
rem echo CATALINA_HOME=%CATALINA_HOME%
rem echo.
rem pause

rem install/remove service
call "%PRGDIR%tomcat\bin\service.bat" %1 %2
rem pause

rem stop monitor
rem if "%1" == "remove" taskkill /F /IM tomcat6w.exe

rem start/stop service
if "%1" == "install" net start %2
if "%1" == "remove" net stop %2
rem pause

rem start monitor
rem if "%1" == "install" start "%PRGDIR%tomcat\bin\tomcat6w.exe" //MS//FlightContest
rem pause

