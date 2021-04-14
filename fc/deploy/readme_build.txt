Build Flight Contest
====================
Thomas Weise
Deutscher Praezisionsflug-Verein e.V.

Supported platforms:
--------------------
Windows x64

Downloads:
----------
Open JDK 8: https://github.com/ojdkbuild/ojdkbuild/
Grails 2.5.6: https://grails.org/download.html/
Apache Tomcat 9: https://tomcat.apache.org/download-90.cgi
Ruby 2.3.3: https://www.ruby-lang.org/de/downloads/
Inno Setup 6.1.2: http://www.innosetup.com/
Python 3.7.9: https://www.python.org/downloads/
GDAL 3.2.1: https://www.gisinternals.com/release.php

Build for Windows:
------------------
Windows installer tool:
	Inno Setup 6.1.2
		Register extension is6 for open with 'C:\Program Files (x86)\Inno Setup 6\Compil32.exe'
		by create key 'HKCR\.is6' with default REG_SZ value 'InnoSetupScriptFile6'

Source folder:
	%PROJECT_ROOT%\GIT\FlightContest
		contains folder .git, concepts, deploy, docs, fc, fcmaps, gpx2gac, output printlabel and readme.txt
	
Additional sources for installer compilation:
	Open JDK 8 (x64) in folder '%PROJECT_ROOT%\Java\openjdk-1.8.0.275x64'
	Grails 2.5.6 in folder '%PROJECT_ROOT%\Grails\grails-2.5.6'
	Apache Tomcat 9 (x64) in folder '%PROJECT_ROOT%\Server\apache-tomcat-9.0.41-windows-x64'
        Modify 'bin\service.bat'
            Set --DisplayName to "%SERVICE_NAME%"
        Repair 'bin\tomcat9.exe' and 'bin\tomcat9w.exe'
            Replace with exe from version 9.0.13
	Ruby 2.3.3 (x64) in folder '%PROJECT_ROOT%\Ruby\ruby-2.3.3-x64-mingw32'
    Touch in folder '%PROJECT_ROOT%\Touch'

Build configuration:
    %PROJECT_ROOT%\GIT\FlightContest\fc\grails-app\conf\BuildConfig.groovy
        Modify dependency cache location
            grails.dependency.cache.dir=%PROJECT_ROOT%/Grails/Dependency-Cache
        Modify or add dependency repositories if you need
            grails.project.dependency.resolution.repositories

Development shell:
    %PROJECT_ROOT%\GIT\FlightContest\fc\start_development.bat
    
Deployment sequence:
	1. Set version number and date in '%PROJECT_ROOT%\GIT\FlightContest\fc\deploy\readme.txt'
    2. Set version number and date in '%PROJECT_ROOT%\GIT\FlightContest\fc\deploy\setup_names.bat'
	3. Set version number in '%PROJECT_ROOT%\GIT\FlightContest\fc\web-app\licenses\README.txt'
	4. Set version number in '%PROJECT_ROOT%\GIT\FlightContest\fc\application.properties' -> app.version
	5. Set version number in '%PROJECT_ROOT%\GIT\FlightContest\fc\deploy\fc.is6'
	6. Call build.bat in development shell (generates %PROJECT_ROOT%\GIT\FlightContest\fc\output\FCSetup.exe)
    7. Call install.bat in development shell (installs Flight Contest an your computer)

Application running details:
    Using the in memory database H2
    Data location: C:\Program Files\Flight Contest\fc\fcdb.h2.db
    
GDAL installation for advanced OSM map generation:
    1. python-3.7.9-amd64.exe (with 'Add Python to PATH')
        (-> start Python shell and check for '[MSC v.1900 64 bit (AMD64)] on win32')
    2. gdal-302-1900-x64-core.msi (Generic installer for the GDAL core components)
    3. GDAL-3.2.1.win-amd64-py3.7.msi (Installer for the GDAL python bindings)
    4. Repair GDAL
        Copy https://github.com/OSGeo/gdal/blob/release/3.1/gdal/swig/python/osgeo/osr.py to C:\Program Files\Python37\Lib\site-packages\osgeo
    5. Add 'C:\Program Files\GDAL' to system environment variable PATH
