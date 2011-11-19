Flight Contest

Thomas Weise
Deutscher Präzisionsflug-Verein e.V.
04.12.2009

Installationshinweise
---------------------
1. Deinstallieren Sie FlightContest, 
   bevor Sie dieses Setup ausführen.
2. Hatten Sie zuvor FlightContest 0.2 installiert,
   löschen Sie zusätzlich das Datenbank-Verzeichnis 
   C:\Programme\FlightContest\fc, da dieses mit dieser
   Version nicht kompatibel ist.


Neu in dieser Version (0.3.0):
------------------------------
- Neues Layout, basierend auf Fluid 960 Grid System

Neu in dieser Version (0.2.0):
------------------------------
- 'Flight Contest' als Apache Tomcat Anwendung


Allgemeines:
------------
'Flight Contest' ist eine Netzwerk-Anwendung.
Ein Server stellt eine Datenbank im Netzwerk zur Verfügung.
Auf einem oder mehreren Clients läuft 'Flight Context' 
im Web-Browser Firefox.

Server und Client können auf einem Windows-Rechner liegen.


Server-Installations-Anforderungen:
-----------------------------------
  'Windows XP ServicePack 2' oder 'Windows Vista ServicePack 1' 
  
  
Client-Anforderungen:
---------------------
  Firefox 3.0 als Standard-Web-Browser 




Server starten:
---------------
Starten des Dienstemanagers 'Apache Tomcat FlightContest':
  'Start -> Programme -> Flight Contest -> Flight Contest Service Manager'
    (unter Windows Vista als Adminstrator starten)

Auf Icon des Dienstemanagers 'Apache Tomcat FlightContest' ausführen:
  Kontextmenüpunkt 'Start Service' aufrufen


Server stoppen:
---------------
Auf Icon des Dienstemanagers 'Apache Tomcat FlightContest' ausführen:
  Kontextmenüpunkt 'Stop Service'
  
  
Arbeit mit 'Flight Contest':
----------------------------
Mit Firefox auf dem Server:
  'Start -> Programme -> Flight Contest -> Flight Contest'

Mit Firefox auf einem beliebigen Rechner im Netzwerk aufrufen:
  http://SERVER-COMPUTERNAME:8080/fc/contest/start
  

Datensicherung:
---------------
Die Datenbank wird an folgendem Ort gespeichert:
  <Installationsverzeichnis>\fc

Vorgehen zur Datensicherung:

  Server stoppen

  Folgende Dateien an einen sicheren Ort kopieren:
    data.properties
    data.script
    
Vorgehen zur Datenwiederherstellung:

  Server stoppen

  Inhalt des folgenden Verzeichnisses löschen:
    <Installationsverzeichnis>\fc

  Folgende Dateien aus der Datensicherung wiederherstellen:
  	<Installationsverzeichnis>\fc\data.properties
  	<Installationsverzeichnis>\fc\data.script
  	
  Server starten
