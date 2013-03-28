Deployment Notes
----------------
Thomas Weise
Deutscher Präzisionsflug-Verein e.V.


Supported platforms:
--------------------
Windows 7
Windows Vista ServicePack 1
Windows XP ServicePack 2


Build installer for Windows:
----------------------------
Windows installer tool:
	Inno Setup 5.4.3 (http://www.innosetup.com)
	  Register extension is5 for open with 'C:\Program Files\Inno Setup 5\Compil32.exe'

Additional sources for installer compilation:
	Java SDK 1.6 (32 Bit) in folder '..\..\..\Java\jdk1.6.0_30'
	Apache Tomcat 7.0 (32 Bit) in folder '..\..\..\Server\apache-tomcat-7.0.25-x86'
	Grails 2.0.1 in folder '..\..\..\Grails\grails-2.0.1'

Deployment sequence:
	1. Set version number in '..\fc\web-app\licenses\README.txt'
	2. Set version number in '..\fc\application.properties\app.version'
	3. Set version number in 'fc.is5'
	4. Update '..\readme.txt'
	5. Call _build.bat (generates ..\output\FCSetup.exe)


Application running details:
----------------------------
32 Bit

Using 4 database connections:
	1. in memory database H2
	2. 3 ODBC connections to Microsoft Access files (FC-AFLOS, FC-AFLOS-UPLOAD, FC-AFLOS-TEST, see odbc.vbs)
