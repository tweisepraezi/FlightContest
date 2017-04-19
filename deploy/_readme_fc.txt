Deployment Notes
----------------
Thomas Weise
Deutscher Präzisionsflug-Verein e.V.


Supported platforms:
--------------------
Windows
Pivotal Cloud Foundry


Deployment to Windows:
----------------------
Windows installer tool:
	Inno Setup 5.5.9 Unicode (http://www.innosetup.com)
	  Register extension is5 for open with 'C:\Program Files (x86)\Inno Setup 5\Compil32.exe'
      by create key 'HKCR\.is5' with default REG_SZ value 'InnoSetupScriptFile5'

Additional sources for installer compilation:
	Java SDK 1.6 (32 Bit) in folder '..\..\..\Java\jdk1.6.0_30'
	Apache Tomcat 7.0 (32 Bit) in folder '..\..\..\Server\apache-tomcat-7.0.25-x86'
	Grails 2.3.6 in folder '..\..\..\Grails\grails-2.3.6'

Deployment sequence:
	1. Set version number in '..\fc\web-app\licenses\README.txt'
	2. Set version number in '..\fc\application.properties\app.version'
	3. Set version number in 'fc.is5'
	4. Update '..\readme.txt'
	5. Call _build_fc.bat (generates ..\output\FCSetup.exe)

Application running details:
    32 Bit
    Using 4 database connections:
    	1. in memory database H2
    	2. 3 ODBC connections to Microsoft Access files (FC-AFLOS, FC-AFLOS-UPLOAD, FC-AFLOS-TEST, see odbc.vbs)


Deployment to Pivotal Cloud Foundry:
------------------------------------
Pivotal Cloud Foundry deployment tool:
    Cloud Foundry Command Line Interface (cf CLI)
        https://pivotal.io/platform/pcf-tutorials/getting-started-with-pivotal-cloud-foundry/install-the-cf-cli

Deployment sequence:
    1. Build war: Call grails command "-Dgrails.env=cloudfoundry war"
    2. Upload to Pivotal Cloud Foundry: 
        cf login -a api.run.pivotal.io
        cf push fc-demo -i 1 -m 2G -k 1G -p ..\output\fc.war --random-route
    3. Ausführen: fc-demo-<random>.cfapps.io

Application running details:
    Using 1 database connection (in memory database H2)


Development details:
--------------------
You have to install Flight Contest before you can start development 
because you need ODBC connections, test database and test files in installation directory 'C:\Program Files (x86)\Flight Contest'.
