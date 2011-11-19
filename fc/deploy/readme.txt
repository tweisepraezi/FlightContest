Flight Contest

Thomas Weise
Deutscher Präzisionsflug-Verein e.V.
17.05.2011

Diese Hinweise finden Sie nach der Installation im Startmenü unter
'Programme -> Flight Contest -> Readme'.

Standard-Wettbewerbsablauf siehe unten.


Fehlerbehebungen und Erweiterungen in dieser Version (0.6.2):
-------------------------------------------------------------
- Nach dem Deaktivieren einer Mannschaft konnte der Zeitplan wegen
  Warnungen nicht gedruckt werden.
- Beim Import von AFLOS-Logger-Daten wurden Punkte nicht korrekt berechnet,
  wenn Soll- und Istzeit 12:00:00 überschritten.
- Neue Funktion "Wettbewerb kopieren"
  Erlaubt es, von einem bestehenden Wettbewerb 
  Wettbewerbs-Einstellungen, Strecken, Mannschaften und/oder 
  Aufgaben-Einstellungen in einen neuen Wettbewerb zu kopieren.
  Damit kann eine Planung und Auswertung erneut begonnen werden
  (z.B. für unterschiedliche Wettbewerbsklassen).
- Beim Import von AFLOS-Logger-Daten können nun auch zutreffende 
  AFLOS-Fehlerpunkte angezeigt werden (Knopf "AFLOS-Logger-Fehler").
  

Änderungen in Version 0.6.1:
----------------------------
- Beseitigung kleinerer Fehler
- AFLOS-Test-Datenbank für Test-Wettbewerb der Installation hinzugefügt:
    C:\Program Files\Flight Contest\samples\AFLOS-FCTest1.mdb
  Hinweise zum Anlegen des Test-Wettbewerbs siehe unten.


Erweiterungen in Version 0.6:
-----------------------------

1. Wettbewerbsverwaltung

- Initialwerte der Punkte an Wettbewerbsordnung Navigationsflug 2008
  angepasst.

2. Streckenverwaltung

- Streckenmesswerte sind jetzt vom letzten TP aus einzugeben.
- Strecken-Druck erfolgt jetzt mit Karten-Messwerten. 

3. Mannschaftsverwaltung

- Import von Excel-Mannschaftslisten möglich.
  Verwenden Sie dabei immer folgende Vorlage:
  'Programme -> Flight Contest -> Samples -> FC-CrewList-Sample.xls'
- Deaktivieren von Mannschaften möglich.
  Deaktivierte Mannschaften entfallen in Auswertungen und Ausdrucken.

4. Aufgabeneinstellungen

- Einstellbare Werte wurden nach Wichtigkeit sortiert.
- Initialwert des Flugabstandes auf 3 Minuten geändert.
- Auswertungsdetails einstellbar:
    * Flugplanungsergebnisse (Standard: ein) 
    * Navigationsflugergebnisse (Standard: ein) 
    * Beobachtungsergebnisse (Standard: ein)
    * Landungsergebnisse (Standard: ein)
    * Andere Ergebnisse (Standard: aus)

5. Wettbewerbsplanung

- Automatische Flugplan-Neuberechnung bei Flugwindänderung.
- Zeitplan-Berechnung erfolgt nur noch für Mannschaften, 
  deren Zeitplan als "Nicht berechnet" angezeigt wird.
  Zeitpläne werden beim Ändern der Reihenfolge als "Nicht berechnet"
  markiert. 
- Streckenberechnungen werden jetzt exakt wie bei PrecisWin gerundet.  
- Zeit- und Flugpläne werden mit Ausgabe-Nummer gedruckt.
  Alle Zeitplan- oder Flugplan-Neuberechnungen erhöhen die Ausgabenummer
  beim nächsten Druck des Zeitplanes oder aller Fluppläne um 1.

6. Wettbewerbsauswertung

- Ergebnisse werden bei Planungsänderung automatisch zurückgesetzt.
- Punkte-Eingabe für Beobachtungen, Landungen und Anderes ergänzt.
- Debriefing-Ausdruck für alle Mannschaften.
- Wettbewerbs-Gesamtauswertung (Summe mehrerer Aufgaben/Wettbewerbstage)
  möglich.

7. Bedienoberfläche

- Blättern-Modus für Planung und Auswertung mit frei einstellbarer 
  Teilnehmer-Anzahl (Extras -> Einstellungen).
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
  die Spalte "Debriefing"


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


Excel-Mannschaftslisten
-----------------------
Sie können Mannschaftslisten mit Excel erstellen und in 'Flight Contest'
importieren.

Verwenden Sie dazu folgende Vorlage, deren Aufbau einen erfolgreichen
Import sicherstellt: 
'Programme -> Flight Contest -> Samples -> FC-CrewList-Sample.xls'

Importiert werden nur Mannschaftsnamen, die noch nicht vorhanden sind.


Standard-Wettbewerbsablauf
--------------------------

A. Vorbereitung

1. Strecken festlegen und in AFLOS eingeben.
2. Mannschaftlisten in Excel-Tabelle (FC-CrewList-Sample.xls) eingeben. 
3. Neuer Wettbewerb
   - Wettbewerbsname, Kartenmaßstab und Zeitzone festlegen.
4. Strecken -> Import AFLOS-Strecke
   - Vorbereitete Strecke aus AFLOS importieren.
   - Karten-Messwerte für Check-Punkte eintragen.

B. Durchführung

5. Mannschaften -> Import Excel-Mannschaftsliste
   - Vorbereitete Excel-Tabelle (FC-CrewList-Sample.xls) importieren.
   - Reihenfolge berechnen und ggf. nachsortieren.
6. Neue Aufgabe
   - Datum, Anfangszeit, Takeoff-Abstand, Flugzeit bis SP,
     Planungstest- und Auswertungsdetails festlegen
   - Flugplannungstest hinzufügen: Dabei eine erste Flugplannungsaufgabe
     mit Strecke und Wind angeben. 
   - Navigationstest hinzufügen: Dabei Strecke und Wind angeben. 
7. Planung
   - Allen Mannschaften eine Flugplannungsaufgabe zuweisen.
   - GGf. Reihenfolge nachsortieren.
   - Allen Mannschaften einen Flugwind zuweisen.
   - Zeitplan berechnen.
   - GGf. einzelne Zeiten verschieben.
   - Aufgaben drucken.
   - Zeitplan drucken.
   - Flugpläne drucken.
8. Tagesablauf
   - Flugplanungstest durchführen.
   - Logger-Daten in AFLOS einlesen.
   - Beobachtungsergebnisse abfragen.
   - Landungen durchführen und vermessen.
9. Auswertung
   - Flugplanungsergebnisse eintragen.
   - Navigationsflug-Ergebnisse aus AFLOS importieren.
   - Beobachtungsergebnisse eintragen.
   - Landungsergebnisse eintragen.
   - Debriefing-Protokolle drucken und Mannschaften übergeben.
   - Ergebnisse auf Grund stattgegebener Proteste korrigieren. 
   - Plätze berechnen.
   - Ergebnisse drucken.
10. Auswertung -> Gesamtergebnis
   - Plätze berechnen.
   - Gesamtergebnis drucken. 


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
  

Test-Wettbewerb:
----------------
Ist kein Wettbewerb vorhanden, steht der Menüpunkt 'Test-Wettbewerb' zur Verfügung. 
Sonst kann der Test-Wettbewerb durch Aufruf von
  http://localhost:8080/fc/contest/createtest
auch später angelegt werden.

Vor Anlegen des Test-Wettbewerbs muss die Test-AFLOS-Datenbank aktiviert werden.
Die Installation von 'Flight Contest' installiert eine Test-AFLOS-Datenbank mit.
Haben Sie nach der 'Flight Contest'-Installation mit einer anderen AFLOS-Datenbank
gearbeitet und wollen Sie den Test-Wettbewerb anlegen, müssen Sie die 
Test-AFLOS-Datenbank wieder laden. Diese ist dazu hier zu finden:
  C:\Program Files\Flight Contest\samples\AFLOS-FCTest1.mdb
 
    
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

   