@echo off

set scr_aflos_name=AFLOS-DemoContest.mdb
set scr_crewlist_test_name=FC-CrewList-Test.xls

:: -------------------------------------------------------------------------------------------
set fc_samples=%ProgramFiles(x86)%\Flight Contest\samples
if "%ProgramFiles(x86)%" == "" set fc_samples=%ProgramFiles%\Flight Contest\samples
set aflos_db_dir=C:\AFLOS\Database
set aflos_system_dir=C:\AFLOS\AFLOS_System
set aflos_db_name=AFLOS.mdb

set src_aflos=%~dp0%scr_aflos_name%
set scr_crewlist_test=%~dp0%scr_crewlist_test_name%

set dest_aflos=%fc_samples%\%scr_aflos_name%
set dest_crewlist_test=%fc_samples%\%scr_crewlist_test_name%

:: -------------------------------------------------------------------------------------------
echo.
echo Copy test data
echo.
echo Remove:
echo   '%dest_aflos%'
echo   '%dest_crewlist_test%'
echo Copy actions:
echo   '%src_aflos%' to '%aflos_db_dir%'
echo   '%src_aflos%' to '%aflos_system_dir%\%aflos_db_name%'
echo   '%src_aflos%' to '%fc_samples%'
echo   '%scr_crewlist_test%' to '%fc_samples%'
echo.
pause
echo. 
@echo on
del "%dest_aflos%"
del "%dest_crewlist_test%"
copy %src_aflos% "%aflos_db_dir%"
copy %src_aflos% "%aflos_system_dir%\%aflos_db_name%"
copy %src_aflos% "%fc_samples%"
copy %scr_crewlist_test% "%fc_samples%"
@echo off
echo.
pause