@echo off

:: Thomas Weise
:: Version 3.2

:: Environment for Tomcat9

set TOMCATW=tomcat9w.exe
set PRGDIR=%~dp0
set JAVA_HOME=
set JRE_HOME=%PRGDIR%java
set PATH=%JRE_HOME%\bin;%PATH%
set CATALINA_HOME=%PRGDIR%tomcat

:: gdal binaries
set JvmArgs=-Djava.library.path=%ProgramFiles%\GDAL
set PR_ENVIRONMENT=PROJ_LIB=%ProgramFiles%\GDAL\projlib

:: set service display name
set PR_DISPLAYNAME=%2

:: set JvmMs=128
set JvmMx=2048

:: set SERVICE_STARTUP_MODE=manual
set SERVICE_STARTUP_MODE=auto

:: stop tomcat monitor
if "%1" == "remove" taskkill /F /IM %TOMCATW%
:: pause

:: stop service
if "%1" == "remove" net stop %2
:: pause

:: install/remove service
call "%PRGDIR%tomcat\bin\service.bat" %1 %2
:: pause

:: start service
if "%1" == "install" net start %2
:: pause

:: start tomcat monitor
:: if "%1" == "install" start "%PRGDIR%tomcat\bin\%TOMCATW%" //MS//%2
:: does not work
:: pause
