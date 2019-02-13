call env.bat
call %ruby_home%\asciidoctor -D %docs% help_fcmaps.adoc
call wscript convert_utf8toansi.vbs //NoLogo "%docs%\help_fcmaps.html"
