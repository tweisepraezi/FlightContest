@echo off

set input_name=fc_en.adoc
set output_name=help_en.pdf

call %RUBY_HOME%\asciidoctor-pdf -D %FC_OUTPUT% -o %output_name% "%FC_DOCS%\%input_name%"

echo Processing %FC_DOCS%\%input_name% to %FC_OUTPUT%\%output_name% done.
