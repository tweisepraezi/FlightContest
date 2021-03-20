@echo off

set input_name=fcmaps.adoc
set output_name=help_fcmaps.html

call %RUBY_HOME%\asciidoctor -D %FC_OUTPUT_DOCS% -o %output_name% "%FC_DOCS%\%input_name%"
echo Processing %FC_DOCS%\%input_name% to %FC_OUTPUT_DOCS%\%output_name% done.
