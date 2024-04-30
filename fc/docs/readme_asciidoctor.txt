Home-Page:
http://asciidoctor.org/

AsciiDoctorJ:
https://docs.asciidoctor.org/asciidoctorj/latest/

AsciiDoctorJ Command Line Interface:
https://docs.asciidoctor.org/asciidoctorj/latest/cli/

AsciiDoctor-Kommando-Doc:
http://asciidoctor.org/man/asciidoctor/

AsciiDoc-Syntax-Doc:
http://asciidoctor.org/docs/asciidoc-syntax-quick-reference/
http://asciidoctor.org/docs/user-manual/#introduction-to-asciidoctor
http://asciidoctor.org/docs/convert-asciidoc-to-pdf/


Tomcat HTML-Ausgabeproblem
--------------------------
ISO-8859-1

server.xml:
 <Connector port="80" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443"
               URIEncoding="UTF-8" />

catalina.sh:
JAVA_OPTS="-Djava.awt.headless=true -Dfile.encoding=UTF-8
-server -Xms2048m -Xmx2048m
-XX:NewSize=256m -XX:MaxNewSize=256m -XX:PermSize=256m
-XX:MaxPermSize=256m -XX:+DisableExplicitGC"

http://stackoverflow.com/questions/34326993/utf-8-format-not-working-in-servlet-for-tomcat-server

http://stackoverflow.com/questions/8391675/utf-8-encoding-a-servlet-form-submission-with-tomcat?rq=1

Ä      &#196;
Ö      &#214;
Ü      &#220;
ä      &#228
ö      &#246;
ü      &#252;
ß      &#223;
