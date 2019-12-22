call env.bat
call %ruby_home%\asciidoctor  -D %docs% -o help_aflos.html aflos.adoc
call wscript convert_utf8toansi.vbs //NoLogo "%docs%\help_aflos.html"
