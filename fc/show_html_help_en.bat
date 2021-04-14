@echo off

set output_name=help_en.html

echo Show "%FC_OUTPUT_DOCS%\%output_name%"

powershell -Command "Start %FC_OUTPUT_DOCS%\%output_name%"
