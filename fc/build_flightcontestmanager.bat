@echo off

set input_name=.\src\csharp\FlightContestManager.cs
set output_name=.\output\FlightContestManager.exe

call "C:\Windows\Microsoft.NET\Framework\v4.0.30319\csc.exe" /platform:x64 /target:winexe /win32icon:.\web-app\images\fc.ico /out:%output_name% %input_name%
echo Processing %input_name% to %output_name% done.


