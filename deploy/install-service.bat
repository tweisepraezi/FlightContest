@echo off

:: Thomas Weise
:: Version 1.0.2

set PRGDIR=%~dp0
set JAVA_HOME=
set JRE_HOME=%PRGDIR%java
set path=%JAVA_HOME%\bin;%path%
set CATALINA_HOME=%PRGDIR%tomcat
set WORKINGDIR=%PRGDIR%fc

:: echo JAVA_HOME=%JAVA_HOME%
:: echo CATALINA_HOME=%CATALINA_HOME%
:: echo.
:: pause

:: stop monitor
:: if "%1" == "remove" taskkill /F /IM tomcat7w.exe
:: stop service

if "%1" == "remove" net stop %2
:: pause

:: install/remove service
call "%PRGDIR%tomcat\bin\fc-service.bat" %1 %2
:: pause

:: start service
if "%1" == "install" net start %2
:: pause

:: start monitor
:: if "%1" == "install" start "%PRGDIR%tomcat\bin\tomcat7w.exe" //MS//FlightContest
:: pause

