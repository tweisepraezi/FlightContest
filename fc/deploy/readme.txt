Flight Contest
==============

Änderungen 4.1.5
----------------
- Der Flughafen-Bereich beim Suchen von Flupplätzen und Lufträumen und beim Erzeugen der Karten um den Flugplatz
  wurde für ANR-Wettbewerbe auf 297mm Randabstand reduziert. Das ermöglich vom TO aus A4-Karten in alle Richtungen.
  Für Rallye- und Präzisionsflug wird ein Randabstand von 420mm verwendet. Das ermöglich vom TO aus A3-Karten in alle Richtungen.
- Hilfe zu "Planung und Auswertung von ANR-Wettbewerben" überarbeitet
- Hilfe zu "Wettbewerbsstrecken mit Task-Creator erstellen" überarbeitet
- Hilfe zu "Wettbewerbsstrecken mit Google Earth Pro erstellen" überarbeitet
- Bug "Task-Creator-Karten werden einer Strecke als Default-Online-Karte zugeordnet" behoben

Änderungen 4.1.4
----------------
- OSM-Wettbewerbs-Karte für ANR-Strecken: Erzeugung von Karten mit abweichender Korridor-Breite für eine Strecke hinzugefügt
    Bis zu 3 abweichende Korridor-Breiten können in den 2., 3. und 4. Einstellungen konfiguriert werden.
- ANR Live Tracking bei Strecken mit landschaftlichen Abschnitten oder Halbkreisen hinzugefügt
    Bei Besatzungen mit abweichender Korridor-Breite werden Strafpunkte beim Live-Tracking gegen die Korridor-Breite der Strecke ermittelt.
- Strecken -> Import Strecke:
    Verfügbare Optionen werden jetzt nach der Wettbewerbsart gefiltert.
      So steht z.B. die Option 'UZK-Koordinaten automatisch ermitteln' nur bei Präzisionsflug-Wettbewerben zur Verfügung.
    'Verzeichnisname in kml/kmz-Datei' wird jetzt mit 'turnpoints' voreingestellt.
      Dieses Verzeichnis wird in FC-kmz-Dateien und in Task-Creator-Export-kml-Dateien zur Speicherung der Strecken-Koordinaten verwendet.
    Nutzen Sie dieses Kommando bei Präzisionsflug-Wettbewerben, um im Task-Creator konstruktierte Strecken, 
    wo Wendepunkte für unbekannte Zeitkontrollen eingefügt wurden, automatisch in unbekannte Zeitkontrollen umzuwandeln.
- Bug "'Ergebnisse -> Abweichungen' stürzt bei gelöschten Beobachtungsformularen ab" behoben

Änderungen 4.1.3
----------------
- ANR-Besatzungs-Zeitplan: Druck der Korridor-Breite hinzugefügt
- ANR-Schiedsrichter-Zeitplan: Druck der Korridor-Breite hinzugefügt
- Strecken-Kommando 'GPX-Export (mit Halbkreis-Tore)' wird jetzt automatisch angeboten, sobald eine Strecke mit Kreismittelpunkt erkannt wird
- Bug "ANR-Flugplan wird nicht mit abweichender Korridor-Breiten-Info gedruckt" behoben
- Bug "OSM-Wettbewerbs-Karte: ANR-Karten-Druck ohne Strecken-Details druckt immer SP- und FP-Tore" behoben
- Bug "Karten, deren Namen ein Komma enthält, kann im Task-Creator nicht geladen werden" behoben
- Bug "OSM-Wettbewerbs-Karte: 'Karten-Objekte importieren' stürzt ab" behoben
- Bug "OSM-Wettbewerbs-Karte: Fehlende Wendepunkt-Festlegungen zum Karten-Zentrum und zur Druck-Festlegung nach 'Import FC-Strecke'" behoben
- Aktualisierung folgender Regelwerke
    FAI Precision Flying - Edition 2025
    FAI Air Rally Flying - Edition 2025
    GAC Landing appendix - Edition 2025

Änderungen 4.1.2
----------------
- ANR-Planung: Abweichende Korridor-Breiten können hinzugefügt werden (-> Navigationstest-Details -> Flugtestwind hinzufügen)
    Dann können die abweichenden Korridor-Breiten mit 'Wind zuweisen' ausgewählten Besatzungen zugewiesen werden. 
- Bug "ANR-Loggerauswertung: 'Berechnete Punkte' zeigt in Spalte 'Außerhalb Korridor' eine Sekunde zuviel an" behoben
- Bug "ANR-Karten-Namen mit Leerzeichen verhindert Druck des ANR-Karten-Flugplanes" behoben
- ANR Live Tracking funktioniert nicht bei folgenden Bedingungen:
    * ANR-Strecke enthält Halbkreise oder landschaftliche Abschnitte
    * Besatzungen mit abweichender Korridor-Breite

Änderungen 4.1.1
----------------
- Aktualisierung folgender Regelwerke
    Wettbewerbsordnung Rallyeflug Deutschland 2025
    Wettbewerbsordnung Air Navigation Race Deutschland 2025
    Regelwerk Landewertung Deutschland 2025
- OSM-Wettbewerbs-Karte: Jetzt werden alle Lufträume, wo das untere Höhenlimit kleiner oder gleich als die eingestellte Höhe ist, berücksichtigt.
- ANR-Korridor-Auswertung: Nur außerhalb des Korridors liegende Sekunden-Abschnitte von Logger-Meßpunkten werden zur Berechnung der Strafpunkte berücksichtigt.
- Bug "Erstellte Live-Tracking-ANR-Navigation-Tasks funktionieren nicht" behoben

Änderungen 4.1.0
----------------
- Wettbewerbspunkte bearbeiten: Möglichkeit der Übernahme der Landepunkte einer anderen Wettbewerbsordung hinzugefügt
- Wettbewerbspunkte bearbeiten: "Alle Ergebnisse neu berechnen" hinzugefügt
    Änderungen der Strafpunkte führen jetzt nicht mehr dazu, dass die Strafpunkte aller Ergebnisse sofort neu berechnet werden.
    Dazu ist diese Funktion explizit aufzurufen.
- OSM-Wettbewerbs-Karte: Flugplatz-OpenAIP-Abfragen werden jetzt in Strecke gespeichert.
    Mit "Flugplätze um Flughafen suchen" sind die Flugplätze vor dem Druck zu ermitteln.
    Mit # beginnende Flugplätze werden bei der Kartenerzeugung ignoriert.
- OSM-Wettbewerbs-Karte: "Erzeugen (nur T/O)" hinzugefügt.
    Druckt Karte mit T/O-Punkt aber ohne weitere Wendepunkte.
- Interner Task-Creator: Seiten-Leitfaden hinzugefügt.
    Die ausgewählte Seitengröße (A3, A4, ...)  und Orientierung (Hochformat oder Querformat W) 
    wird um den Mittelpunkt aller vorhandenen Check- und Plotting-Punkte angezeigt. 
    Um eine Seite vor der Streckeneingabe festzulegen, zuerst den Takeoff- und einen Plotting-Punkt eingeben.
- Wählbarer Aufruf des internen und externen Task-Creators im Karten-Menü hinzugefügt.
- Bug "OSM-Wettbewerbs-Karte kann bei ANR-Regelwerk nicht gedruckt werden, wenn nur der Punkt T/O vorhanden ist" behoben
- Bug "Google-Earth-Darstellung im internen Task-Creator funktioniert nicht mehr" behoben
- Bug "Live-Tracking-Contest kann nicht angelegt werden" behoben
- Bug "OpenAIP-Layer in OSM-Online-Karte funktioniert nicht mehr" behoben
- Hilfe-Kapitel "Durch Installation beschädigte 'Flight Contest'-Datenbank wiederherstellen" hinzugefügt

Änderungen 4.0.2
----------------
- OSM-Wettbewerbs-Karte: Luftraum-OpenAIP-Abfragen werden jetzt im Wettbewerb gespeichert.
    Das beschleunigt den Start der Kartenerzeugung für Folge-Aufträge erheblich.
    Die Grenzen abgerufener Lufträume können im Karten-Menü unter dem Eintrag 'Lufträume' zur Anzeige gebracht werden.    
- Logger-Auswertung: Bei der Anpassung der Startbahn mit "Wind (Startbahn verschieben)" stehen dort jetzt folgende Aktionen zur Verfügung:
    "Offline-Karte (T/O)" - Logger-Daten mit Strecke am T/O-Punkt anzeigen
    "Offline-Karte (LDG)" - Logger-Daten mit Strecke am LDG-Punkt anzeigen
    "Logger-Daten erneut kalkulieren" - berechnet sofort die Strafpunkte mit der verschobenen Startbahn
    Die Positionsabweichung des LDG-Punktes längs zur Startbahn wird nun mit -0.03NM voreingestellt, damit eine identische Position mit T/O-Punkt vermieden wird.
- Bug "Strafpunkte für Landungen sind im deutschen ANR-Regelwerk nicht korrekt konfiguriert" behoben
- Bug "Tor-Breite von T/O und LDG einer verwendeten Strecke bei ANR-Regelwerk nicht änderbar" behoben
- Bug "Bei überlappenden Karten-Regionen wird die OSM-Wettbewerbs-Karte nicht immer vollständig gedruckt" behoben
- Bug "Bei der Strecken-Default-Druck-Karte können Nicht-ANR-Formate ausgewählt werden" behoben
- Bug "OSM-Wettbewerbs-Karte kann nicht gedruckt werden, wenn Luftraum-Name ein Anführungszeichen enthält" behoben

Änderungen 4.0.1
----------------
- ANR-Wettbewerbs-Karte: Verwendete Korridor-Breite in NM wird jetzt unten rechts gedruckt.
- OSM-Wettbewerbs-Karte: Wendepunkt-Kreise werden jetzt mit dem Durchmesser der Tor-Breite gedruckt (vorher 1 NM).
- Offline-Viewer: Entfernungsmessung hinzugefügt. Start erfolgt mit Shift-Klick auf einem Tor.
    Mit einem folgendem Shift-Klick kann die gemessene Entfernung mit einer Linie in die Darstellung gezeichnet werden.
- Bug "Punkteberechnung im landschaftlichen Streckenabschnitt fehlerhaft" behoben

Änderungen 4.0.0
----------------
- Planung und Auswertung von ANR-Wettbewerben hinzugefügt

Änderungen 3.6.0
----------------
- Konfigurations-Management für Maps- und OpenAIP-Server-Einstellungen hinzugefügt
    Ein zentraler Server nimmt alle für die Karten-Erzeugung notwendigen Einstellungen vor.
      Dazu muss die Client-ID der Flight Contest-Installation registriert werden.
      Die Client-ID wird in "Extras -> Client-ID ermitteln" angezeigt. Hier wird auch das erfolgreiche Laden der Konfiguration angezeigt.
      Die Client-ID kann auch mit dem Flight Contest Manager ermittelt werden.
      Registriere deinen Client hier: https://flightcontest.de/register-client.
      Die Konfiguration wird beim Start des Flight Contest-Dienstes geladen. Sollte zu diesem Zeitpunkt keine Internet verfügbar gewesen sein,
      kann mit "Extras -> Client-ID ermitteln -> Konfiguration laden" die Konfiguration nach Herstellen der Internet-Verbindung geladen werden.
    Die Nutzung der Karten-Erzeugung wird auf dem zentralen Server geloggt.
      Das umfasst IP-Adresse und Zeitpunkt des Aufrufes für eine Client-ID.
      Diese Informationen werden 400 Tage lang gespeichert, um eine jährliche Abrechnung der Servernutzung zu ermöglichen.
    Bisher verwendete Konfigurations-Einstellungen in config.groovy sind nicht mehr wirksam.
      Für die Nutzung eines eigenen OpenAIP-API-Keys sind folgende neue Konfiguationseinstellungen zu verwenden:
        flightcontest {
          maps {
            openaip {
              server = "https://api.core.openaip.net/api"
              apikey = "TODO"
              ignoreAirspacesStartsWith = ""
            }  
          }
        }
- Bug "Bei Unterbrechung der Kartenerzeugung geht Karten-Projektion verloren" behoben
- Aktualisierung folgender Regelwerke
    FAI Precision Flying - Edition 2024

Änderungen 3.5.0
----------------
- OSM-Wettbewerbs-Karte: Integrierte Bearbeitung zusätzlicher Karten-Objekte hinzugefügt
    Kirchen, Burgen/Schlösser, Ruinen, Landhäuser, Berggipfel, Türme, Fernmeldetürme, Leuchttürme, Windkraftanlagen,
    Kreismittelpunkte, Fluplätze und eigene Symbole können zum Zeichen auf die Karte hinzugefügt werden.
    Jedes Objekt kann mit einem Untertitel versehen werden.
    Die zusätzlichen Karten-Objekte sind bei einem gpx- und kml-Export mit enthalten.
    Die Karten-Objekte einer Strecke kann in anderen Strecken mit benutzt werden.
    Bei einem GPX-Export mit Halbkreis-Toren werden die Halbkreis-Mittelpunkte als Karten-Objekt hinzugefügt.
- OSM-Wettbewerbs-Karte: Stil-Veränderungen
    Straßen-Brücken und Waldwege wurden entfernt
    Feldwege können jetzt in 5 Abstufungen (1 = wenig bis 5 = viele) hinzugefügt werden.
- OSM-Wettbewerbs-Karte: Unterstützung für zusätzliche Karten-Regionen hinzugefügt
    Zusätzlich zu den mitteleuropäischen Ländern können nun auch Karten von Zentral-Chile erstellt werden.
- OSM-Wettbewerbs-Karte: Option 'Verschiebung des Kartenzentrums' wurde in 'Verschiebung des Kartenrandes' umbenannt
- OSM-Wettbewerbs-Karte: 'KMZ-Export-Lufträume (verborgen)' hinzugefügt
- Strecken: iTO wurde entfernt. Bei Zwischenlandungen ist nur iLDG zu verwenden.
- Logger mit mehreren Meßwerten je Sekunde werden akzeptiert.
- Die Wettbewerbs-Vorgabe "Mehrfachverwendung von Strecken-Bodenzeichen erlauben" ist jetzt standardmäßig aktiviert.
- Strecken: Die Berechung von Strecken-Fehlern wurde überarbeitet.
    Die Anzeige-Spalte 'Nutzbar' zeigt jetzt mit 'Ja' an, dass eine Strecke für Navigationsflug oder Planungstest verwendet werden kann.
    Rote Markierungen zeigen an, dass Fehler wegen Nichteinhaltung des Regelwerkes bestehen.
    Auch bei Über- oder Unterschreiten der Anzahl von Bildern, Bodenzeichen oder Strecken-Abschnitte kann eine Strecke verwendet werden.
    Die Bodenzeichen-Anzahl-Überprüfung schliesst jetzt Wendepunkt-Bodenzeichen mit ein.
- OSM-Wettbewerbs-Karte: Option 'Wendepunkt-Bodenzeichen' hinzugefügt. Druckt das konfigurierte Wendepunkt-Bodenzeichen rechts neben den Wendepunkt-Kreis.
- Aktualisierung folgender Regelwerke
    FAI Air Rally Flying - Edition 2024
    GAC Landing appendix - Edition 2024

Änderungen 3.4.8
----------------
- Hilfe "Fotos und Bodenzeichen verwalten" hinzugefügt
- Hilfe für das Karten-Menü hinzugefügt
- Strecken-Fotos importieren: Option "Namen automatisch festlegen" hinzugefügt. Nummeriert die Fotos beginnend mit 1.
- Strecken-Bodenzeichen importieren: Option "Namen automatisch festlegen" hinzugefügt. Fügt Bodenzeichen-Platzhalter * mit angegebener Koordinate hinzu.
  Der Bodenzeichen-Platzhalter * muss vor Nutzung in einem Wettbewerb durch das gewünschte Bodenzeichen ersetzt werden.
- Neue Strecke: Schaltfläche "Ohne Beobachtungen erstellen" hinzugefügt
- Übersichts-Zeitplan: Bei Seitenumbrüchen zur Bildung von Gruppen werden Planungs-, Takeoff- und Lande-Zeiten je Gruppe gedruckt.
- Interner Task-Creator: Bug "Bei der magnetische Deklination wurde Ost mit West vertauscht" behoben.

Änderungen 3.4.7
----------------
- OpenAIP-Zugang wiederhergestellt
- Strecken-Foto/Bodenzeichen-Import: Die Anzahl importierbarer Fotos und Bodenzeichen wird nicht mehr beschränkt.
- OSM-Wettbewerbs-Karten-Verbesserungen:
    Wendepunkt-Namen wurden in der Größe reduziert (16 Punkte) und näher am Wendepunkt geschrieben (1.1 NM).
    Strecken-Foto-Name wird größer gedruckt.
    Bug "Strecken-Foto/Bodenzeichen-Symbole fehlten machmal" behoben
    Lufträume: Areas und FIS werden standardmäßig ignoriert. Mit der Konfiguration flightcontest.openaip.ignoreAirspacesStartsWith
      können weitere zu ignorierende Lufträume hinzugefügt werden.
- Bug "Besatzungslistendruck stürzt ab, wenn Besatzung ohne Team vorhanden ist" behoben
- Bug "Übersichts-Zeitplan: Planausgabezeitbereich bei Einsteigern fehlerhaft" behoben
- Bug "Halbkreis-Berechnung: Torrichtung am Ende manchmal fehlerhaft" behoben 
- Bug "Halbkreis-Tore aus Kreismittelpunkten exportieren: Halbkreis nach iSP fehlerhaft" behoben
- Bug "Wind-Zuweisung hat Zeitplan-Warnungen nicht neu berechnet" behoben

Änderungen 3.4.6
----------------
- Interner Task-Creator: Deutsche und spanische Benutzeroberfläche hinzugefügt.
  Unter "Extras -> Einstellungen" kann die gewünschte Sprache eingestellt werden.
- Flight Contest Manager: Flight Contest startet mit Firefox, wenn dieser vorhanden ist, auch wenn er nicht der Standard-Browser ist.
- Bug "PDF-Wettbewerbs-Karte kann nicht erzeugt werden, wenn der Karten-Titel Umlaute enthält" behoben

Änderungen 3.4.5
----------------
- Karten-Menüpunkt "Alle Wettbewerbs-Karten exportieren" hinzugefügt
- Karten-Import zeigt jetzt eine Fehlermeldung an, wenn die zu importierende(n) Karte(n) bereits im Karten-Menü vorhanden sind.
    Eine bestehende Karte muss erst gelöscht werden, bevor sie neu importiert werden kann.
- Interner Task-Creator: 'Sync map url'-Schaltfläche hinzugefügt.
    Nach dem Laden eines Task, der auf einem anderen Computer erstellt worden ist, zeigt die Map-URL auf Karten eines anderen Wettbewerbs.
    Mit dieser Schaltfläche wird die Map-URL auf den aktuellen Wettbewerb angepasst.
- Die zu berücksichtigende Luftraum-Höhe wird jetzt beim Strecken-Export/Import und Strecken-Kopie mit transportiert.
- Der in der Aufgabe definierte Titel für "Anderer Test" wird jetzt in der Auswertung anstelle von "And." angezeigt.
- Bug "Besatzungsliste stürzt ab, wenn Besatzung ohne Team eingegeben wird" behoben
- Bug "Umbenennen von Flugzeugen stürzt ab, wenn ein bereits existerendes Kennzeichen verwendet wird" behoben
- Bug "Strecken-Einstellung 'Default-Online-Karte' zeigt zu viele Auswahlwerte an" behoben
- Bug "OSM-Online-Karte hat nicht die in Strecken-Einstellung 'Default-Online-Karte' eingestellte Karte angezeigt" behoben
- Bug "OSM-Wettbewerbs-Karte verwendet nicht die eingestellte Druck-Sprache" behoben
- Bug "KMZ-Export verwendet nicht die eingestellte Druck-Sprache" behoben

Änderungen 3.4.4
----------------
- "Flight Contest Manager" hinzugefügt.
  Startet bei der Anmeldung als Taskleistensymbol.
  Enthält folgende Kontextmenü-Kommandos, die bisher nur als Windows-Startmenü-Kommandos zur Verfügung standen:
    Auswertungs-Kommandos -> Logger-Daten automatisch laden: Startet Hintergrund-Programm zum Importieren neu gespeicherter Logger-Daten.
    Auswertungs-Kommandos -> Beobachtungs-Formulare automatisch laden: Startet Hintergrund-Programm zum Importieren gescannter Beobachtungs-Formulare
    Auswertungs-Kommandos -> Planungs-Aufgaben-Formulare automatisch laden: Startet Hintergrund-Programm zum Importieren gescannter Planungs-Test-Formulare
    Dienst-Kommandos -> Dienst-Manager: Startet Flight Contest-Dienst-Manager, wo z.B. der Java-Speicher vergrößert werden kann (Java -> Maximum memory pool)
    Dienst-Kommandos -> Neustart Flight Contest: Stoppt und startet Flight Contest-Dienst
    Dienst-Kommandos -> Start Flight Contest: Startet Flight Contest-Dienst
    Dienst-Kommandos -> Stop Flight Contest: Stoppt Flight Contest-Dienst
    Dienst-Kommandos -> Datenbank sichern: Speichert Datenbank in C:\FCSave
    Beenden: "Flight Contest Manager" beenden. Kann mit Windows-Startmenü-Kommando wieder gestartet werden.
  Ein Klick auf das Taskleistensymbol öffnen die Flight-Contest-Bedienoberfläche im Browser.

Änderungen 3.4.3
----------------
- Online/Offline-Viewer im Navigationsflug-Ergebnis wurde um Buttons "T/O-SP" und "FP-LDG" erweitert.
  Zoomt zu den Logger-Daten des angeklickten Bereiches, wobei die beiden Punkte die Auflösung bestimmen.
- Die Einstellungen für die Flugzeit-Berechnung von Starts und Landungen wurden von den Aufgaben- zu den Wind-Einstellungen verlegt.
  Damit wird es möglich, dass bei Richtungswechsel der Startbahn andere Werte verwendet werden können,
  so dass abweichende An- bzw. Abflugentfernungen besser berücksichtigt werden können.
- Strecken-Beobachtungen: Auswahl des Beobachtungswertes ??? hinzugefügt.
  Dieses ist auszuwählen, wenn z.B. ein unbekanntes Bodenzeichen im Antwort-Bogen angegeben wurde. Wird mit "Falsch" bestraft.
- Planung: Anordnung der Aktions-Buttons wurde übersichtlicher organisert.
- Planung: Die Eingabe von Navigationstest-Details wurde vereinfacht.
- Besatzungs-Druck: Option "Sortierungshilfe" hinzugefügt.
    Zeigt eine zusätzliche Spalte mit der Start-Nr. des anderen Flugzeuges an.
    Zeigt eine zusätzliche Spalte mit der Positionsdifferenz zur Startliste des anderen Flugzeuges an.
    Markiert TAS mit !, die höher als vom Vorgänger sind.
    Markiert Teams mit !!, die gleich zum Vorgänger sind.
    Markiert Teams mit !, die gleich zum Vorgänger des Vorgängers sind.
- Bug "Tor-Richtung am Halbkreis-Ende im Konstruktions-Modus fehlerhaft" behoben
- Spracheinstellung für Task-Creator in "Extras -> Einstellungen" hinzugefügt (Deutsch, Englisch, Spanisch)
    
Änderungen 3.4.2
----------------
- Bei Konfiguration des externen Task-Creators werden zusätzliche Links neben den Links zum internen Task-Creator angezeigt.
- Interner Task-Creator: 'Addons RL' und 'SUAs Url' hinzugefügt
- OSM-Wettbewerbs-Karte: 'Lufträume um Flughafen ermitteln' hinzugefügt
    Dabei werden standardmäßig Lufträume, die unter 4000ft liegen, berücksichtigt (anpassbar).
    'KMZ-Export-Lufträume' enthält jetzt auch die Namen der Lufträume.
- OSM-Wettbewerbs-Karte: Flugplätze aus OpenAIP-Daten hinzugefügt
    Diese werden wie in ICAO-Karten dargestellt.
    Mit 'CSV-Export-Flughäfen' können die Flupplatz-Koordinaten abgerufen werden.

Änderungen 3.4.1
----------------
- Interner Task-Creator: Fehlermeldungen bei Eingabe des Task-Namens beseitigt
- Strecken: Verbesserte Fehlermeldung bei Halbkreis-Mittelpunkten einschl. Hilfetext
- Kartenerzeugung: AirportArea-Name um Strecken-Titel erweitert
- Karten: Bug "Import einer Karten-zip stürzt ab" beseitigt

Änderungen 3.4.0
----------------
- Menüpunkt "Karten" hinzugefügt
    Listet alle lokal gespeicherten Karten des Wettbewerbes auf.
    Hier stehen Kommandos zur PDF- und PNG-Erzeugung, zum Export, zum Umbenennen, zum Löschen und zum Aufruf des Task-Creators zur Verfügung.
    PDF-Karten sind zum Druck vorgesehen.
    Neue Karten zur PDF/PNG-Erzeugung oder zur Verwendung im Task-Creator werden mit "OSM-Wettbewerbs-Karte" einer Strecke erzeugt.
    Dabei muss für PDF/PNG oder für den Task-Creator jeweils eine eigene Karte erzeugt werden.
    Mit "Karte importieren" kann ein Karten-Export auf einem anderen Notebook oder in einem anderen Wettbewerb importiert werden.
- OSM-Wettbewerbs-Karte erweitert:
    1. Schaltfläche "Online-Karte um Flughafen erzeugen" hinzugefügt,
       welche eine Karte mit T/O in der Mitte mit 420mm Abstand zum Rand für OSM-Online-Anzeige erzeugt.
    2. Schaltfläche "Task-Creator-Karte um Flughafen erzeugen" hinzugefügt,
       welche eine Karte mit T/O in der Mitte mit 420mm Abstand zum Rand für Verwendung im Task-Creator erzeugt.
    3. Schaltfläche "Erzeugen (für Task-Creator)" jeweils in den 1./2./3./4. Einstellungen hinzugefügt,
       welche Karten mit T/O und ggf. LDG aber ohne weitere Streckendetails zur Verwendung im Task-Creator erzeugt.
    4. Erzeugte OSM-Karten werden jetzt immer lokal gespeichert und sind dann über den Menüpunkt "Karten" erreichbar,
       wo Weiterverarbeitungs-Kommandos zur Verfügung stehen.
- OSM-Online-Karte: Anzeige lokal gespeicherter Karten hinzugefügt
    Hierbei kann fließend zwischen Online-Karte und lokaler Karte umgeschaltet werden.
    Sind mehrere lokale Karten vorhanden, kann zwischen diesen umgeschaltet werden.
    Die Default-Karte kann in den 'Strecken-Einstellungen' festgelegt werden.
    Für den Task-Creator erzeugte Karten können hier nicht angezeigt werden, da diese Karten eine andere Karten-Projektion nutzen.
- Interner Task-Creator zur Strecken-Konstruktion hinzugefügt
    Basiert auf https://www.airrats.cl/taskcreator?lang=en von Carlos Rocca.
    Start des Task-Creators über "..." einer gepeicherten Karte für eine neue Aufgabe:
      Hier ist die "Map Url" im Task-Creator voreingestellt und kann mit "Load" geladen werden.
      Nach Aktivieren von "Turn Points -> Edit" können mit Doppelklick Wendepunkte auf der Karte hinzugefügt werden.
      Mit "Save task data" kann die Aufgabe als CSV-Datei im Download-Ordner des Computers abgespeichert werden.
    Start über Menüpunkt "Karten -> Task-Creator" zum Laden bereits gespeicherter Aufgaben:
      Mit "Load task data" kann die gespeicherte CSV-Datei einer Aufgabe geladen werden. 
      Eine voreingestellte "Map Url" muss mit "Load" geladen werden.
      Nach Aktivieren von "Turn Points -> Edit" ist eine Weiterbearbeitung der Aufgabe möglich.
      Mit "Export FC kml" kann eine Strecke exportiert werden, die mit "Strecken -> Import FC-Strecke" zur Planung und Auswertung genutzt werden kann.
- Halbkreis-Tore aus Kreismittelpunkten werden jetzt mit 5° Kursänderung erzeugt.
    Das kann mit der Strecken-Einstellung "Kurs-Änderung je Halbkreis-Tor" verändert werden.
- Die Begrenzung der Anzahl von Strecken-Punkten wurde aufgehoben.
- Strecken-Koordinate: Option "Check-Punkt ignorieren" für landschaftlichen Streckenabschnitt hinzugefügt
    Bewirkt, dass diese Koordinate nicht ausgewertet wird und nicht in Online/Offline-Viewer und verschiedenen Eingabe-Masken zu sehen ist.
- Ergebnisse: Druck-Optionen bei anstehendem Besatzungs-Ergebnisdruck über ... mit einem Klick erreichbar.
    Das ermöglicht es, bei Änderungen an einem Besatzungs-Ergebnis dessen Korrektur-Druck schneller durchzuführen.

Änderungen 3.3.6
----------------
- Bug "UZKs des krummen Streckenabschnittes im Navigationsflug-Ergebnis werden bei hintereinander liegenden krummen Streckenabschnitten immer gedruckt" behoben
- Ergebnis-Druck: Seiteanordnung zur Verringerung von Druckseiten verbessert 
- OSM-Wettbewerbs-Karte: ANR-Format an ANR 2.2.0 angepasst
- Bug "Import FC-Strecke: Strecken-Fotos und -Bodenzeichen vom iSP werden nicht importiert" behoben
- OSM-Wettbewerbs-Karte: Ortsstraßen sowie Wald- und Feldwege erweitert
- OSM-Wettbewerbs-Karte: Hilfe verbessert

Änderungen 3.3.5
----------------
- Strecken-Import: Option "Lese Ortsmarkierungen anstelle von Pfaden bei kml/kmz-Dateien" hinzugefügt
  Durch die Voreinstellung "Ein" werden französische kml/kmz-Dateien automatisch unterstützt.
- Bug "Beim Export von Halbkreis-Toren aus Kreismittelpunkten werden krumme Streckenabschnitte fehlerhaft exportiert" behoben

Änderungen 3.3.4
----------------
- OSM-Wettbewerbs-Karte/PNG-Wettbewerbs-Karte mit World-File erzeugen: pnginfo-Datei hinzugefügt, die detailierte Informationen zur erzeugten Karte enthält
- Bug "Import von Koordinaten aus kml-Dateien funktioniert nicht" behoben
- Bug "Import der Grad-Zahl von TO/LDG-Kooordinaten aus kml-Dateien funktioniert nicht" behoben

Änderungen 3.3.3
----------------
- Aktualisierung folgender Regelwerke
    FAI Precision Flying - Edition 2023
    FAI Air Rally Flying - Edition 2023
    GAC Landing appendix - Edition 2023
    Motorflug-Wettbewerbsordnung Österreich 2023
- Die Mindestflughöhe wird jetzt bei neuen Strecken mit 400ft (500ft - 100ft Toleranz) initialisiert.
    Grund: Das Regelwerk 'FAI Precision Flying - Edition 2023' definiert einen Puffer von 100ft für die Höhenmessung.

Änderungen 3.3.2
----------------
- Genauigkeit von Strecken-Fotos und Bodenzeichen reduziert (ein Zehntel NM, ganze mm)
- Wettbewerbs-Auswertungs-Einstellungen: Option "Live-Ergebnis-Besatzungs-Anzahl" hinzugefügt.
    Definiert die max. Anzahl der Besatzungen, die in der Live-Ergebnis-Anzeige auf einer Seite angezeigt werden.
    Bei Überschreiten des eingestellten Wertes (Default: 30) werden sich abwechselnde Seiteninhalte erzeugt.
- Wettbewerbs-Auswertungs-Einstellungen: Option "Live-Ergebnis-Neueste-Besatzungs-Anzahl" hinzugefügt.
    Definiert die Anzahl der Besatzungen, die in der Live-Ergebnis-Anzeige als neueste fertiggestellte Auswertung am Anfang der Seite angezeigt werden.
    Default: 5 Besatzungen
- OSM-Online-Karte: Karte "TopPlusOpen" vom Bundesamt für Kartographie und Geodäsie hinzugefügt.
- OSM-Wettbewerbs-Karte:
    Erzeugen: Spezifische Fehlermeldung bei fehlerhaften Lufträumen hinzugefügt.
    Deaktivierung von Einträgen in der Luftraum-Liste hinzugefügt: Mit # beginnde Einträge werden jetzt bei der Kartenerzeugung ignoriert.
- Bug "Datenbank wird bei Flight Contest-Installation durch einen anderen Benutzer geleert" behoben.
  Die Datenbank kann aus der Installations-Sicherungs-Kopie wiederhergestellt werden.
    1. Stop Flight Contest
    2. Kopie C:\Program Files\Flight Contest\fc-<Datum>-<Zeit>-<FC-Version>\fcdb.h2.db -> C:\Program Files\Flight Contest\fc
       oder
       Kopie C:\Program Files\Flight Contest\fc-backup-<Datum>-<Zeit>-<DB-Version>\fcdb.h2.db -> C:\Program Files\Flight Contest\fc
    3. Start Flight Contest

Änderungen 3.3.1.1
------------------
- OSM-Online-Karte: Karte von Michelin hinzugefügt

Änderungen 3.3.1
----------------
- OpenAIP-Integration für Luftraum-Anzeige und -Kartendruck hinzugefügt
    OSM-Wettbewerbs-Karte: Luftraum-Koordinaten werden jetzt aus OpenAIP abgerufen.
    OSM-Online-Karte: OpenAIP-Layer hinzugefügt. Zeigt Lufträume und Flugplätze auf der Karte an.
    OpenAIP-Konfiguration erforderlich, siehe 'Hilfe -> OpenAIP für Luftraum-Anzeige und -Kartendruck konfigurieren'

Änderungen 3.3.0
----------------
- Strecken-Liste und Strecken-Details zeigen jetzt alle Aufgaben-Verwendungen an.
- Die Mindestflughöhe wird jetzt bei neuen Strecken mit 300ft (500ft - 200ft Toleranz) initialisiert.
- Höhen-Angaben in Dialogen und beim Strecken-Druck zeigen jetzt Höhe des Geländes (GND:), die Mindestflughöhe (>) und die Maximalflughöhe (<).
- Bug "Strecken-Höhen-Anpassungen werden bei der nächsten Logger-Auswertung nicht übernommen" behoben
- Unterstützung von Zahlen-Tastaturen hinzugefügt
	Bei der Bearbeitung des Planungs-Abgabewertes kann zur Navigation zum nächsten Eingabefeld die Enter-Taste benutzt werden.
	Bei der Bearbeitung von Check-Wert-Meßpunkten kann zur Navigation zum nächsten Eingabefeld die Enter-Taste benutzt werden.
	Bei der Eingabe von Entfernungen in Beobachtungsergebnissen kann zur Navigation zum nächsten Eingabefeld die Enter-Taste benutzt werden.
	Bei der Eingabe von Zeiten wird anstelle des ':' u.a. auch ein ',' akzeptiert.	
- Offline-Karte für Check-Punkte zeigt nun Flug vom vorhergehenden Check-Punkt aus.
- Druckfußzeile für Auswertungs-Drucke hinzugefügt.
- Aufgaben-Einstellungen: Option "Planung sperren" hinzugefügt. Deaktiviert alle Kommandos, die die Planung verändern können.
- Planung: Zeitplan-Export (Start-Liste) hinzugefügt. Für die Startliste von FFA_SkyTraq.
- Strecken-Druck: Übersicht der Koordinaten-Karten-Entfernungen (in mm) hinzugefügt.
- Besatzungs-Druck: Team-Druck und Flugzeug-Druck auf Folge-Seiten hinzugefügt.
- Landungs-Ergebnis-Eingabe: Schaltfläche 'Speichern und nächstes Ergebnis' hinzugefügt.
- Ergebnisse: Die Ergebnis-Liste enthält nun für jede Landung eine separate Spalte (max. 4: Ldg1, Ldg2, Ldg3, Ldg4).
- Ergebnis-Eingabe: Schaltfläche 'Vorheriges Ergebnis' hinzugefügt.
- Beobachtungs-Ergebnis-Eingabe wurde für Touchscreen-Nutzung optimiert:
    Strecken-Foto-Eingabe wurde von Auswahlliste auf Schaltfläche umgestellt.
	Radio-Buttons wurden vergrößert und mit Rahmen versehen.
- Alle Listen-Details: Schaltfläche 'Vorheriges Element' hinzugefügt.
- Planung: Landetest-Startliste hinzugefügt. Erlaubt Ausdruck einer Startliste, ohne einen Zeitplan festzulegen.
  Ausdruck mit Gruppierung.
- Logger auslesen: Option 'Logger eines neuen Ports sofort auslesen' hinzugefügt.
- Planung, Ergebnisse und Besatzungen: Zur Bildung von Gruppen können Seitenumbrüche hinzugefügt werden.
    Seitenumbrüche werden beim Drucken vor der konfigurierten Besatzung eingefügt.
	Die aktivierte Option "Zeige Seiten einzeln" reduziert die Besatzungs-Anzeige auf die gewählte Seite
	und begrenzt die Navigation zur nächsten oder vorherigen Besatzung.
- Strecken-Import erweitert:
	Bis zu 3 Koordinaten können als Halbkreis-Mittelpunkte deklariert werden.
	Die auszuwertende Kursänderung zur automatischen Ermittlung von UZK-Koordinaten kann angepasst werden (Standard: 1,5°).
- Leeres Strecken-Bodenzeichen hinzugefügt.
    Die Eingabe erfolgt durch Auswahl des *. Bewirkt, dass zusätzliche Zeilen im Auswerteformular gedruckt werden.
	Ein korrektes Ergebnis besteht dann darin, dass in diesen Zeilen keine Bodenzeichen gefunden wurden.

Änderungen 3.2.4
----------------
- Zeitplan-Export (Daten) hinzugefügt.
  Liefert eine JSON-Datei, die die Startzeit und alle Wendepunkt-Überflug-Zeiten für alle Besatzungen beinhaltet.
- Löschen von Wendepunkt-Foto-Bildern hinzugefügt
- Löschen von Strecken-Foto-Bildern hinzugefügt

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
