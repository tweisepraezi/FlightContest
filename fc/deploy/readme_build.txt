Build Flight Contest
====================
Thomas Weise
Deutscher Praezisionsflug-Verein e.V.

Supported platforms:
--------------------
Windows x64

Downloads:
----------
Open JDK 8: https://adoptium.net/de/temurin/releases/?os=windows&arch=x64&package=jdk&version=8 (Windows, x64, JDK, 8)
Grails 2.5.6: https://grails.org/download.html/
Apache Tomcat 9: https://tomcat.apache.org/download-90.cgi -> 64-bit Windows zip
AsciiDoctorJ 2.5.1: https://docs.asciidoctor.org/asciidoctorj/latest/distribution/ -> asciidoctorj
Inno Setup 6.1.2: http://www.innosetup.com/
Python 3.7.9: https://www.python.org/downloads/ (otional)
GDAL 3.2.1: https://www.gisinternals.com/release.php (otional)

Build for Windows:
------------------
Windows installer tool:
	Inno Setup 6.1.2
		Register extension is6 for open with 'C:\Program Files (x86)\Inno Setup 6\Compil32.exe'

Source folder:
	%PROJECT_ROOT%\EPJ\fcdev
		contains folder .git, fc, fcmaps, gpx2gac, printlabel, and files .gitignore and readme.txt
	
Additional sources for installer compilation (used by build.bat and fc.is6):
	Open JDK 8 (x64) in folder '%PROJECT_ROOT%\Java\jdk8u442-b06'
	Grails 2.5.6 in folder '%PROJECT_ROOT%\Grails\grails-2.5.6'
	Apache Tomcat 9 (x64) in folder '%PROJECT_ROOT%\Server\apache-tomcat-9.0.54-windows-x64'
	Apache Tomcat 9 (x64) in folder '%PROJECT_ROOT%\Server\apache-tomcat-9.0.13-windows-x64'
	AsciiDoctorJ 2.5.1 in folder '%PROJECT_ROOT%\AsciiDoctorJ\asciidoctorj-2.5.1
    Touch in folder '%PROJECT_ROOT%\Touch'

Build configuration:
    %PROJECT_ROOT%\EPJ\fcdev\fc\grails-app\conf\BuildConfig.groovy
		grails.dependency.cache.dir
			Modify dependency cache location here if you need
        grails.project.dependency.resolution -> repositories    
			Modify or add repositories here if you need

Development shell:
    %PROJECT_ROOT%\EPJ\fcdev\fc\start_development.bat
    
Deployment sequence:
	1. Set version number in '%PROJECT_ROOT%\EPJ\fcdev\fc\application.properties' -> app.version
	2. Set version number in '%PROJECT_ROOT%\EPJ\fcdev\fc\deploy\fc.is6'
    3. Set version number and date in '%PROJECT_ROOT%\EPJ\fcdev\fc\deploy\setup_names.bat'
    4. Set version number and date in '%PROJECT_ROOT%\EPJ\fcdev\fc\docs\fc.adoc'
    5. Set version number and date in '%PROJECT_ROOT%\EPJ\fcdev\fc\docs\fcmaps.adoc'
    6. Set version number and date in '%PROJECT_ROOT%\EPJ\fcdev\fc\docs\fc_en.adoc'
	7. Set version number in '%PROJECT_ROOT%\EPJ\fcdev\fc\web-app\licenses\README.txt'
	8. Call build.bat in development shell (generates %PROJECT_ROOT%\EPJ\fcdev\fc\output\FCSetup.exe)
    9. Call install.bat in development shell (installs Flight Contest on your computer)

Application running details:
    Using the in-memory database H2
    Data location: C:\Program Files\Flight Contest\fc\fcdb.h2.db
    Run as service 'FlightContest'
    
Python and GDAL installation for OSM tiles generation  (otional):
    1. python-3.7.9-amd64.exe (with 'Add Python to PATH')
        (-> start Python shell and check for '[MSC v.1900 64 bit (AMD64)] on win32')
    2. gdal-302-1900-x64-core.msi (Generic installer for the GDAL core components)
    3. GDAL-3.2.1.win-amd64-py3.7.msi (Installer for the GDAL python bindings)
    4. Repair GDAL
        Copy https://github.com/OSGeo/gdal/blob/release/3.1/gdal/swig/python/osgeo/osr.py to C:\Program Files\Python37\Lib\site-packages\osgeo
    5. Add 'C:\Program Files\GDAL' to system environment variable PATH
