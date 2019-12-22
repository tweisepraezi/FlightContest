call env.bat
call %ruby_home%\asciidoctor -D %docs% -o help.html fc.adoc
call wscript convert_utf8toansi.vbs //NoLogo "%docs%\help.html"

