Flight Contest

Thomas Weise
Deutscher Präzisionsflug-Verein e.V.
28.11.2010

Diese Hinweise finden Sie nach der Installation im Startmenü unter
'Programme -> Flight Contest -> Readme'.


Erweiterungen in dieser Version (0.5.6):
----------------------------------------
- Blättern-Modus für Planung und Auswertung 
  mit frei einstellbarer Teilnehmer-Anzahl
  (Extras -> Einstellungen)
- Stellt folgende Informationen beim Start wieder her:
    * Letzter Wettbewerb
    * Bediensprache
    * Zeilenzahl beim Blättern
  Dazu werden werden auf dem Bedien-Computer Cookies abgespeichert.


Erweiterungen und Fehlerbehebung in Version 0.5.5:
--------------------------------------------------
- Navigationsflug-Auswertung:
  "Start verpasst" wurde in "Takeoff verpasst" umbenannt
- Einführung eines Debriefing-Formulars für Proteste,
  erreichbar für jede Mannschaft in der Auswertung über 
  die Spalte "Befragung"


Fehlerbehebung in Version 0.5.4:
--------------------------------
- Bei manueller Korrektur der Flughöhe wurden Strafpunkte 
  nicht neu berechnet.
- Das nachträgliche Eintragen des Flugzeugtyps funktioniert nun.
- Beim Flugplan wurden einzelne Streckenabschnitte nicht berechnet.
- Beim Löschen von Teilnehmern wurde laufende Nummerierung 
  in Planung und Auswertung nicht aktualisiert.


Erweiterungen und Fehlerbehebung in Version 0.5.3:
--------------------------------------------------
- Ergebniseingabe bei Flugplanungs- und Navigationstest wurde verbessert:
    * Ein Haken wird angezeigt, wenn ein Wert eingegeben wurde.
    * Die Werteeingabe kann zurückgesetzt werden.
    * Es werden Sollwerte bei der Istwerteingabe mit angezeigt.
    * Bei Zeiten ist auch die Eingabe hh.mm.ss erlaubt.
- Abstürze beim Import nicht auswertbarer AFLOS-Logger-Messwerte beseitigt.
    "Flight Contest -> AFLOS -> Erfasste Fehlerstati"
    oder "AFLOS -> Check -Overview" zeigen Status von AFLOS-Messungen an:
        Flight O.K.      Auswertbarer Flug ohne Flugfehler
        Flight not O.K.  Auswertbarer Flug mit Flugfehlern
        Check Error !    Nicht auswertbarer Flug
          (-> Import-Fehler "AFLOS-Logger-Daten von ... enthalten Fehler.")
        Für eine AFLOS-Mannschaft nicht vorhandener Eintrag:
          (-> Import-Fehler "AFLOS-Logger-Daten von ... nicht komplett.")


Fehlerbehebung in Version 0.5.2:
--------------------------------
- Punkte des Lande- und des Sondertests wurden nicht in der Summe
  einberechnet.
- Bei Änderung des Zeitplanes mit "Zeit +" oder "Zeit -" wurden
  die geänderten Zeiten nicht in die Navigationsflug-Auswertung
  übernommen.
- Die Ausgabe der Ankunfts-Zeit wurde im Flugplan entfernt,
  um Verwechselungen mit der spätestens Lndezeit zu vermeiden.
 
  
Fehlerbehebung in Version 0.5.1:
--------------------------------
- "Planung -> Zeitplan berechnen" zeigte für einzelne Mannschaften 
  "Nicht berechnet" an. 
  
Neu in Version 0.5:
-------------------
- Übernahme von Überflug-Daten aus AFLOS
- Neue Hauptmenüsortierung

Neu in Version 0.4:
-------------------
- Übernahme von Strecken aus AFLOS
- Unterstützung mehrerer Wettbewerbe
- Deutsche und englische Bediensprache
- Erweiterungen der Wettbewerbsverwaltung

Neu in Version 0.3:
-------------------
- Neues Layout, basierend auf Fluid 960 Grid System

Neu in Version 0.2:
-------------------
- 'Flight Contest' als Apache Tomcat Anwendung

Neu in Version 0.1.1:
---------------------
- Unterstützung von 'Windows Vista'

Version 0.1:
------------
- Erster Prototyp


Allgemeines:
------------
'Flight Contest' ist eine Netzwerk-Anwendung.
Ein Server stellt eine Datenbank im Netzwerk zur Verfügung.
Auf einem oder mehreren Clients läuft 'Flight Context' im Web-Browser.
Server und Client können auf einem Windows-Rechner liegen.

Beste Darstellung im Client mit dem Web-Browser 'Firefox'.
'Internet Explorer' ebenfalls möglich.

Auf einem Rechner ist die gleichzeite Anzeige
des Wettbewerbes in der anderen Bediensprache oder eines 2. Wettbewerbes   
mit einem 2. Internet-Explorer-Fenster möglich, 
nicht jedoch mit einem 2. Firefox-Fenster.


Interaktion mit AFLOS:
---------------------
Minimale empfohlene AFLOS Version: 2.10

Installationshinweise:
AFLOS und 'Flight Contest' müssen auf dem selben PC installiert sein.
Wird AFLOS nicht in dem Verzeichnis C:\AFLOS installiert,
führen Sie nach der ersten Benutzung von AFLOS den Punkt
'Programme -> Flight Contest -> Install AFLOS connection' aus
(in Windows Vista oder Windows 7 als Administrator).

'Flight Contest' importiert Strecken und Logger-Messwerte direkt
aus der zuletzt oder gerade geöffneten AFLOS-Datenbank.

Import von Strecken (Strecken -> Import AFLOS-Strecke):
Stellen Sie zum Import sicher, dass Check-Punkte unbekannter Zeitkontrollen 
in AFLOS eine Torbreite von 2NM, alle anderen Check-Punkte eine andere Torbreite haben.

Import von Logger-Messwerten (Auswertung -> Navigationsflug -> Import AFLOS-Logger-Daten):
Stellen Sie zum Import sicher, dass die aus dem Logger eingelesenen Messwerte
für eine konkrete Mannschaft (= Comp.-Nr.) und Strecke (= Ref.-Nr.) und der 
aktivierten Option 'Procedure Turn" in AFLOS mit 'Check-Data -> Calculate' 
berechnet wurden.
Fehlerhaft errechnete Kursabweichungen können nach dem Import manuell korrigiert werden
(Klick auf Zahl in Nr.-Spalte für den gewünschten Check-Punkt).
Nicht auswertbare Logger-Messwerte können nicht importiert werden. 
"Flight Contest -> AFLOS -> Erfasste Fehlerstati"
oder "AFLOS -> Check -Overview" zeigen Status von AFLOS-Messungen an:
  Flight O.K.      Auswertbarer Flug ohne Flugfehler
  Flight not O.K.  Auswertbarer Flug mit Flugfehlern
  Check Error !    Nicht auswertbarer Flug
    (-> Import-Fehler "AFLOS-Logger-Daten von ... enthalten Fehler.")
  Für eine AFLOS-Mannschaft nicht vorhandener Eintrag
    (-> Import-Fehler "AFLOS-Logger-Daten von ... nicht komplett.")


Mögliche Betriebssysteme der Server-Installation:
------------------------------------------------
Windows XP ServicePack 2
Windows Vista ServicePack 1
Windows 7 


Server starten:
---------------
Starten des Dienstemanagers 'Apache Tomcat FlightContest':
  'Programme -> Flight Contest -> Flight Contest Service Manager'
(in Windows Vista oder Windows 7 als Administrator)

Auf Icon des Dienstemanagers 'Apache Tomcat FlightContest' ausführen:
  Kontextmenüpunkt 'Start Service' aufrufen


Server stoppen:
---------------
Auf Icon des Dienstemanagers 'Apache Tomcat FlightContest' ausführen:
  Kontextmenüpunkt 'Stop Service'
  
  
Arbeit mit 'Flight Contest':
----------------------------
Mit Firefox oder Internet Explorer auf dem Server:
  'Programme -> Flight Contest -> Flight Contest'

Mit Firefox oder Internet Explorer 
auf einem beliebigen Rechner im Netzwerk aufrufen:
  http://SERVER-COMPUTERNAME:8080/fc/contest/start
  

Datensicherung:
---------------
Die Datenbank wird an folgendem Ort gespeichert:
  <Installationsverzeichnis>\fc
  (C:\Programme\Flight Contest\fc)

Vorgehen zur Datensicherung:

  Server stoppen

  Folgende Dateien an einen sicheren Ort kopieren:
    fcdb.properties
    fcdb.script
    
Vorgehen zur Datenwiederherstellung:

  Server stoppen

  Inhalt des folgenden Verzeichnisses löschen:
    <Installationsverzeichnis>\fc
    (C:\Programme\Flight Contest\fc)

  Folgende Dateien aus der Datensicherung wiederherstellen:
  	<Installationsverzeichnis>\fc\fcdb.properties
  	<Installationsverzeichnis>\fc\fcdb.script
  	(C:\Programme\Flight Contest\fc)
  	
  Server starten

Logs:
-----
  Sind hier zu finden:
    <Installationsverzeichnis>\tomcat\logs\stdout_<Datum>.log"

Cookies:
--------
  Speichert folgende Cookies für 1 Jahr:
    LastContestID    - Letzter Wettbewerb 
    ShowLanguage     - Bediensprache
    ShowLimitCrewNum - Zeilenzahl beim Blättern

   