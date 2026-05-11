@echo off

set input_name=fc.adoc
set output_name=help_de.html

call %ASCIIDOCTOR_HOME%\bin\asciidoctorj.bat -D %FC_OUTPUT_DOCS% -o %output_name% "%FC_DOCS%\%input_name%"
echo Processing %FC_DOCS%\%input_name% to %FC_OUTPUT_DOCS%\%output_name% done.
