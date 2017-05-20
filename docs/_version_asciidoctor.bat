@echo off

call env.bat

echo.
echo Home: %ruby_home%
echo.
call %ruby_home%\ruby --version
echo.
call %ruby_home%\asciidoctor --version
echo.
call %ruby_home%\asciidoctor-pdf --version
echo.
pause