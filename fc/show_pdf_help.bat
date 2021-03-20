@echo off

set output_name=help.pdf

echo Show "%FC_OUTPUT%\%output_name%"

powershell -Command "Start %FC_OUTPUT%\%output_name%"
