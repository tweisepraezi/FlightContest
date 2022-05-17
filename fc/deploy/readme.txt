Flight Contest
==============

Änderungen 3.2.3
----------------
- Für jeden einzelnen Check-Punkt einer Strecke kann jetzt auch eine separate Mindestflughöhe über Grund angegeben werden
  (abweichend von der Festlegung in den Stecken-Einstellungen, die sonst für alle Check-Punkte gilt).
- Für jeden einzelnen Check-Punkt einer Strecke kann jetzt auch eine Maximalflughöhe über Grund angegeben werden,
  deren Überschreiung dann auch zu einer Bestrafung wegen Verletzung der korrekten Flughöhe führt.
- Beachten Sie bei der Festlung derartiger Höhenangaben, dass die barometrische Höhenmessung während der Fluges eine andere Höhe
  ergibt als die GPS-Messung, die mit diesen Festlegungen ausgewertet wird.
  Reduzieren Sie also die tatsächlich auszuwertende Minimalhöhe bzw. erhöhen Sie die tatsächlich auszuwertende Maximalhöhe 
  um eine Toleranz-Wert (z.B. 200ft), um Fälle im Grenzbereich nicht zu bestrafen.

Änderungen 3.2.2.2
------------------
- Bug "Höhenauswertung liefert Höhenfehler bei Vorbeiflügen" behoben

Änderungen 3.2.2.1
------------------
- Update auf GPSBabel 1.8.0

Änderungen 3.2.2
----------------
- Das Speichern von Links auf hochgeladene OSM-Karten wurde um "Alle Strecken-Details" erweitert.

Änderungen 3.2.1.1
------------------
- Anpassungen an Live-Tracking-API-Änderungen (Übernahme von Punkte-Änderungen)

Änderungen 3.2.1
----------------
- OSM-Wettbewerbs-Karte:
    Individuelle Konfiguration des Aussehens jedes Luftraums hinzugefügt
    Horizontale und vertikale Verschiebung des Kartenzentrums hinzugefügt

- Offline-Karte für jeden einzelnen Wendepunkt:
    Angabe eines Zeitoffsets zur Verschiebung des gezeigten Logger-Aufzeichnungs-Ausschnittes hinzugefügt
    
- Anpassungen an Live-Tracking-API-Änderungen

Änderungen 3.2.0
----------------
- Umstellung der Laufzeitumgebung
    Plattform: x64
    Laufzeitumgebung: OpenJDK 8, Tomcat 9
    Framework: Grails 2.5.6
    Service wird mit Automatik-Start installiert
    AFLOS-Anschluss nicht mehr enthalten
    Erfordert die manuelle Deinstallation einer älteren Version (3.x).
    Datenübernahme von einer älteren Version (3.x) nach Installation:
      1. Stop Flight Contest (Startmenü -> Flight Contest -> Stop Flight Contest)
      2. Kopiere 'C:\Program Files (x86)\Flight Contest\fc\fcdb.h2.db' nach 'C:\Program Files\Flight Contest\fc\fcdb.h2.db'
      3. Start Flight Contest (Startmenü -> Flight Contest -> Start Flight Contest)

- Wettbewerbs-Datum und Zeitzone hinzugefügt
    Dient zur Ermittlung der Differenz zwischen Ortszeit und UTC.
    Wird für Live-Tracking benötigt.

- Aufgaben-Datum hinzugefügt
    Wird für Live-Tracking benötigt.

- Live-Tracking-Unterstützung hinzugefügt
  -> Hilfe -> Live-Tracking
  -> Hilfe -> Live-Tracking konfigurieren

- Besondere CSS-Eigenschaften in Dialoge überführt
  --route --disable-procedureturn     -> Strecken -> <Strecken-Name> -> Strecken-Einstellungen -> Wendeschleifen verwenden
  --route --show-curved-points        -> Strecken -> <Strecken-Name> -> Strecken-Einstellungen -> UZKs der krummen Strecke in Strecken-Karten dieser Strecke anzeigen
  --class --secret-gatewidth          -> Klassen -> <Klassen-Name> -> Abweichende Tor-Breite von UZK-Koordinaten für diese Klasse
  --class --before-starttime          -> Klassen -> <Klassen-Name> -> Vorverlegung der Planungs-Anfangs-Zeit vor der regulären Planungszeit für diese Klasse
  --class --add-submission            -> Klassen -> <Klassen-Name> -> Verlängerung der spätesten Abgabezeit des Lösungsbogens für diese Klasse
  --flightplan hide-distance          -> Aufgaben -> Navigationstest -> <Navigationstest-Name> -> Bearbeiten -> Werte in Flugplan-Spalte 'Entfernung' anzeigen
  --flightplan hide-truetrack         -> Aufgaben -> Navigationstest -> <Navigationstest-Name> -> Bearbeiten -> Werte in Flugplan-Spalte 'Rechtweisender Kurs' anzeigen
  --flightplan hide-trueheading       -> Aufgaben -> Navigationstest -> <Navigationstest-Name> -> Bearbeiten -> Werte in Flugplan-Spalte 'Rechtweisender Steuerkurs' anzeigen
  --flightplan hide-groundspeed       -> Aufgaben -> Navigationstest -> <Navigationstest-Name> -> Bearbeiten -> Werte in Flugplan-Spalte 'Geschwindigkeit über Grund' anzeigen
  --flightplan disable-local-time     -> Aufgaben -> Navigationstest -> <Navigationstest-Name> -> Bearbeiten -> Flugplan-Spalte 'Ortszeit' anzeigen
  --flightplan show-elapsed-time      -> Aufgaben -> Navigationstest -> <Navigationstest-Name> -> Bearbeiten -> Zeitverlauf in Flugplan-Spalte "Flugzeit" anzeigen
  --submission                        -> Aufgaben -> Navigationstest -> <Navigationstest-Name> -> Bearbeiten -> Späteste Abgabezeit des Lösungsbogens nach Erreichen des FP
  --route --start-tp --add-num        -> Aufgaben -> Navigationstest -> <Navigationstest-Name> -> Bearbeiten -> Increase turnpoint numbers by the specified value from the specified turnpoint in the flight plans for crews
  --flightresults show-curved-points  -> Aufgaben -> Navigationstest -> <Navigationstest-Name> -> Bearbeiten -> UZKs der krummen Strecke im Navigationsflug-Ergebnis immer anzeigen
  --landingresults                    -> Auswertung -> Wettbewerbs-Auswertung -> Wettbewerbs-Auswertungs-Einstellungen -> Lande-Strafpunkte um angegebenen Faktor reduzieren
  -> Hilfe -> Ersetzte CSS-Eigenschaften

- Der Kartenmaßstab wird jetzt in den Strecken-Einstellungen festgelegt.

- Die Mindestflughöhe über Grund wird jetzt in den Strecken-Einstellungen festgelegt. Bei den einzelnen Koordinaten ist demzufolge die Geländehöhe über Normal-Null einzugeben.
  Die Mindestflughöhe über Normal-Null ist damit die Summe aus Mindestflughöhe über Grund und der Geländehöhe.

- OSM-Wettbewerbs-Karte:
    Alle Optionen werden in der Datenbank gespeichert.
    Krumme Etappen können separat deaktiviert werden.
    Direktes Erzeugen ohne Streckendetails hinzugefügt.
	Direktes Erzeugen mit allen Streckendetails hinzugefügt.
    3 weitere Sätze Erzeugungs-Optionen hinzugefügt.
    E-Mail-Versand von Wettbewerbskarten hinzugefügt.

- Schiedsrichter-Zeitplan:
    Druck-Option 'Späteste Abgabezeit' hinzugefügt
    Druck-Option 'Landefeld' hinzugefügt
    Konfigurationswert 'flightcontest.landing.info' hinzugefügt

- Neue Vorlage für die Besatzungsliste
  -> Hilfe -> Besatzungsliste erstellen -> FC-CrewList-Sample.xlsx

- Englische Hilfe hinzugefügt

- OSM-Online-Karten-Layer 'Flight Contest' hinzugefügt.
  Ermöglicht die Anzeige von Strecke und Navigationsflügen interaktiv auf der OSM-Wettbewerbs-Karte.

- Streckenplanung: 'Halbkreis durch Angabe eines Kreismittelpunktes erzeugen' hinzugefügt.
  -> Hilfe -> Streckenplanung -> Wettbewerbsstrecke mit Halbkreisen erzeugen

- Foto-Verwaltung und -Druck hinzugefügt
    Strecken-Foto-Namen können alphabetisch oder zufällig vergeben werden. Das erste Bild bestimmt, ob dabei Buchstaben oder Zahlen verwendet werden.
      -> Strecken -> <Strecken-Name> -> Strecken-Foto-Namen alphabetisch vergeben
      -> Strecken -> <Strecken-Name> -> Strecken-Foto-Namen zufällig vergeben
    Wendepunkt- und Strecken-Bilder können einzeln importiert werden.
      -> Strecken -> <Strecken-Name> -> Wendepunkt-Beobachtungen -> <Nr.> -> Wendepunkt-Foto-Bild importieren
      -> Strecken -> <Strecken-Name> -> Strecken-Fotos -> <Nr.> -> Strecken-Foto-Bild importieren
    Wendepunkt- und Strecken-Bilder können in einer ZIP-Datei importiert werden.
      -> Strecken -> <Strecken-Name> -> Wendepunkt-Foto-Bilder importieren (SP.jpg, TP1.jpg, ..., FP.jpg, Name muss mit Check-Punkt übereinstimmen)
      -> Strecken -> <Strecken-Name> -> Strecken-Foto-Bilder importieren (1.jpg, 2.jpg, ..., Name muss mit Foto-Name übereinstiimen)
    Foto-Markierung kann mit einem Klick verschoben werden. Bei Bedarf kann bei Wendepunkt-Beobachtungen für den Druck auch ein Seitenwechsel eingefügt werden.
      -> Strecken -> <Strecken-Name> -> Wendepunkt-Beobachtungen -> <Nr.>
      -> Strecken -> <Strecken-Name> -> Strecken-Fotos -> <Nr.>
    Wendepunkt- und Strecken-Bilder können alphabetisch oder nach ihrem Auftreten auf der Strecke sortiert gedruckt werden.
      -> Strecken -> <Strecken-Name> -> Wendepunkt-Foto-Druck
      -> Strecken -> <Strecken-Name> -> Wendepunkt-Foto-Druck (alphabetisch)
      -> Strecken -> <Strecken-Name> -> Wendepunkt-Foto-Druck (Strecken-Verlauf)
      -> Strecken -> <Strecken-Name> -> Strecken-Foto-Druck (alphabetisch)
      -> Strecken -> <Strecken-Name> -> Strecken-Foto-Druck (Strecken-Verlauf)
    Bild-Erstellungs-Hinweise:
      Ertellen Sie alle Bilder als JPG-Datei im 4:3-Format.
      Wendepunkt-Bilder mit groß geschriebenen englischen Check-Punkt-Bezeichnungen benennen und in eine ZIP-Datei packen (z.B. SP.jpg, TP1.jpg, ..., FP.jpg).
      Strecken-Bilder in der Reihenfolge ihres Auftretens mit Zahlen benennen und in eine ZIP-Datei packen (z.B. 1.jpg, 2.jpg, ...).
      Strecken-Bilder-Koordinaten in eine UTF-8-Textdatei auflisten, z.B.:
        1, Lat 52° 12.10000' N, Lon 016° 45.90000' E
        2, Lat 52° 16.80000' N, Lon 017° 23.10000' E
        ...
      Diese Wendepunkt-Bilder können auch in den Ordner "turnpointphotos" einer bereits erstellten kmz-Streckendatei hinzugefügt werden.
      Diese Strecken-Bilder können auch in den Ordner "photos" einer bereits erstellten kmz-Streckendatei hinzugefügt werden.

- Logger-Daten-Anzeige um GPX-Download erweitert

- Aktualisierung folgender Regelwerke
    FAI Precision Flying - Edition 2022
    FAI Air Rally Flying - Edition 2022

- Logger-Auswertung: In der Liste berechneter Punkte steht die Offline-Karte jetzt schnell für jeden einzelnen Wendepunkt zur Verfügung.

- Das Auslesen der Logger 'Renkforce GT-730FL-S', 'GlobalSat DG-100' und 'GlobalSat DG-200' kann jetzt direkt aus Flight Contest heraus erledigt werden.
  Vorausetzung: Installation von GPSBabel (http://www.gpsbabel.org/download.html)
    -> Hilfe -> Logger auslesen

      
Allgemeines:
------------
'Flight Contest' ist eine Netzwerk-Anwendung.
Ein Server stellt eine Datenbank im Netzwerk zur Verfügung.
Auf einem oder mehreren Clients läuft 'Flight Context' im Web-Browser.
Server und Client können auf einem Windows-Rechner liegen.

Beste Darstellung im Client mit dem Web-Browser 'Firefox'.

Server starten:
---------------
Starten des Dienstemanagers 'Apache Tomcat FlightContest':
  'Programme -> Flight Contest -> Flight Contest Service Manager'

Auf Icon des Dienstemanagers 'Apache Tomcat FlightContest' ausführen:
  Kontextmenüpunkt 'Start Service' aufrufen


Server stoppen:
---------------
Auf Icon des Dienstemanagers 'Apache Tomcat FlightContest' ausführen:
  Kontextmenüpunkt 'Stop Service'
  
  
Behebung von Server-Ausführungsproblemen nach Update:
-----------------------------------------------------
Gelegentlich kann der Dienst 'Apache Tomcat FlightContest' 
nach Update-Installation nicht gestartet werden. Behebung:
  1. 'Flight Contest' deinstallieren
     (-> Programme -> Flight Contest -> Uninstall Flight Contest)
  2. Booten
  3. 'Flight Contest' erneut installieren


Arbeit mit 'Flight Contest':
----------------------------
Mit Firefox auf dem Server:
  'Programme -> Flight Contest -> Flight Contest'

Mit Firefox auf einem beliebigen Rechner im Netzwerk aufrufen:
  http://SERVER-COMPUTERNAME:8080/fc/contest/start
  

Demo-Wettbewerb:
----------------
Im Wettbewerbs-Menü steht der Menüpunkt 'Demo-Wettbewerb' zur Verfügung,
mit dem verschiedene Demo-Wettbewerbe angelegt werden können.

Beim Laden eines Demo-Wettbewerbs wird automatisch die 
Demo-AFLOS-Datenbank aktiviert. Zuvor in AFLOS geladene Datenbanken oder
hochgeladene Datenbanken bleiben davon unberührt und stehen bei Wechsel
zu einem echten Wettbewerb automatisch wieder zur Verfügung. 

Das gleichzeitige Benutzen von Demo-Wettbewerb und echtem Wettbewerb
im Netzwerk ist ebenfalls möglich.
 
    
Wiederherstellung eines fehlgeschlagenen 'Flight Contest'-Updates:
------------------------------------------------------------------
Das Verzeichnis <Installationsverzeichnis>\fc wird bei 
Update-Installation mit dem Namen 
<Installationsverzeichnis>\fc-backup-<Datum> automatisch gesichert. 

Sollte das 'Flight Contest'-Update nicht starten, kann folgendermassen 
zum letzten funktionstüchtigen 'Flight Contest' zurückgekehrt werden:
  1. Server stoppen
  2. 'Flight Contest' deinstallieren
  3. <Installationsverzeichnis>\fc löschen
  4. <Installationsverzeichnis>\fc-backup-<Datum> in
     <Installationsverzeichnis>\fc umbenennen
  5. Letztes funktionstüchtiges 'Flight Contest' installieren


Cookies:
--------
  Speichert folgende Cookies für 1 Jahr:
    LastContestID    - Letzter Wettbewerb 
    ShowLanguage     - Bediensprache
    PrintLanguage    - Drucksprache
    ShowLimitCrewNum - Zeilenzahl beim Blättern
