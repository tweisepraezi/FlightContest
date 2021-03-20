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
	Inno Setup 5.5.9 Unicode (http://www.innosetup.com)
	  Register extension is5 for open with 'C:\Program Files (x86)\Inno Setup 5\Compil32.exe'
      by create key 'HKCR\.is5' with default REG_SZ value 'InnoSetupScriptFile5'

Additional sources for installer compilation:
	Java SDK 1.6 (32 Bit) in folder '..\..\..\Java\jdk1.6.0_30'
    Groovy 2.1.6 in folder '..\..\..\Groovy\Groovy-2.1.6'

Deployment sequence:
	1. Set version number in 'printlabel.is5'
	2  Groovy/Grails Tool Suite: 
	   Export "Runnable JAR file" from project 'printlabel' to '..\output\printlabel.jar'
	   with option 'Package required libraries into generated JAR'
	3. Inno Setup: 
	   Compile 'printlabel.is5' (generates '..\output\FCPrintLabelSetup.exe')
