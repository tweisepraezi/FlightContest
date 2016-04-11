Deployment Notes
----------------
Thomas Weise
Deutscher Präzisionsflug-Verein e.V.


Supported platforms:
--------------------
Windows


Build installer for Windows:
----------------------------
Windows installer tool:
	Inno Setup 5.4.3 (http://www.innosetup.com)
	  Register extension is5 for open with 'C:\Program Files (x86)\Inno Setup 5\Compil32.exe'
      by create key 'HKCR\.is5' with default REG_SZ value 'is5_auto_file'

Additional sources for installer compilation:
	Java SDK 1.6 (32 Bit) in folder '..\..\..\Java\jdk1.6.0_30'

Deployment sequence:
	1. Set version number in 'gpx2gac.is5'
	2  Groovy/Grails Tool Suite: 
	   Export "Runnable JAR file" from project 'gpx2jar' to '..\output\gpx2gac.jar'
	   with option 'Package required libraries into generated JAR'
	3. Inno Setup: 
	   Compile 'gpx2gac.is5' (generates '..\output\FCGpx2GacSetup.exe')
