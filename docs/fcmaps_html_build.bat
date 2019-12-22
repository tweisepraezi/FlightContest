call env.bat
call %ruby_home%\asciidoctor -D %docs% -o help_fcmaps.html fcmaps.adoc
call wscript convert_utf8toansi.vbs //NoLogo "%docs%\help_fcmaps.html"
