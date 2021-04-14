@echo off

set output_name=help_en.pdf

echo Show "%FC_OUTPUT%\%output_name%"

powershell -Command "Start %FC_OUTPUT%\%output_name%"
