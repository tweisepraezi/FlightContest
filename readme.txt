Flight Contest

Flight Contest plant den zeitlichen Wettbewerbsablauf eines Motorflug-Wettbewerbes
der General Aviation und wertet Ergebnisse für Planungstests, Navigationstests, 
Beobachtungen und/oder Landungen aus.

Flight Contest is a Tool to schedule general aviation power flying competitions 
and to evaluate planning, navigation, observation and/or landing test results.


Thomas Weise
Deutscher Präzisionsflug-Verein e.V.
06.04.2017

Downloads und Dokumentationen:
http://flightcontest.de

Diese Hinweise finden Sie nach der Installation im Startmenü unter
'Programme -> Flight Contest -> Readme' (deutsch)

Die Bedienungsanleitung finden Sie im Startmenü unter
'Programme -> Flight Contest -> Bedienungsanleitung' (deutsch)
'All Programs -> Flight Contest -> Manual' (english)

Weitere Hilfe finden Sie im Startmenü unter
'Programme -> Flight Contest -> Help' (deutsch)

Flight Contest ist eine moderne, auf Java-Techniken basierende, mehrsprachige
und netzwerkfähige Open-Source-Datenbank-Anwendung:
* Die Benutzer-Oberfläche läuft im Web-Browser (Firefox, Internet Explorer u.a.). 
* Sie kann auf mehreren vernetzten Computern gleichzeitig benutzt werden. 
* Jeder Benutzer kann dabei seine bevorzugte Bediensprache verwenden
  (Deutsch, Englisch, erweiterbar).
 
Flight Contest hat gegenüber bisherigen Programmen ein flexibles
Wettbewerbsmanagement. Dazu zählen die Unterstützung
* unterschiedlicher Wettbewerbsordnungen 
  (FAI Precision and Air Rally Flying, Deutschland Navigationsflug, 
   Schweiz Präzisionsflug, Österreich Präzisions- und Navigationsflug, 
   erweiterbar),
* von Wettbewerbsklassen 
  (unterschiedliche Auswertung von Teilnehmergruppen in einem Wettbewerb),
* paralleler Wettbewerbe 
  (gleichzeitig stattfindende Wettbewerbe mit unabhängigen Startfeldern),
* kombinierter Wettbewerbe 
  (gleichzeitig stattfindende Wettbewerbe mit einem gemeinsamen Startfeld).
 
Weitere nützliche Funktionen von Flight Contest:
* Direkte Strafpunkt-Berechnung beim Laden einer Logger-Datendatei
  (GAC, GPX und IGC).
* Sehr schnelle Logger-Daten-Nutzung des FAI-zertifizierten Loggers AFLOS 
  (ohne Umweg über Datei-Export und -Import).
* PDF-Erzeugung aller Formulare für Druck und Internet-Veröffentlichung.
* Online-Kartenanzeige für Strecken und Navigationsflüge.
  (Goggle Maps und Open Street Map)
* Offline-Anzeige für Strecken und Navigationsflüge.
* E-Mail-Versand der Navigationsflug-Karten an Teilnehmer.
* Live-Ergebnis-Anzeige (Live Scoring) zur Anzeige der vorläufigen
  Rangliste während eines laufenden Debriefings.


Erweiterungen in dieser Version (3.1):
--------------------------------------
- Neue Beobachtungs-Auswertung
    * Unterstützt Auswerteformular für Besatzungen und 
      Karten-Debriefing durch Schiedsrichter
    * Unterstützt Fotos und Bodenzeichen an Wendenpunkten und auf der Strecke
    * Unterstützt manuelle Eingabe der Beobachtungs-Objekte und Import 
      aus Text-Dateien
    * Beobachtungs-Objekte können einzeln von der Auswertung ausgeschlossen werden
      (-> 'Ergebnisse -> Check-Punkte deaktivieren')
    * Unterstützt die Erfassung gescannter Auswerteformulare
    -> 'Programme -> Flight Contest -> Anleitung Beobachtungen'
- Regelwerk-abhängige Wettbewerbs-Vorgaben hinzugefügt
    * Min. und max. Anzahl von Streckenabschnitten
    * Tor-Breite von UZK-Koordinaten
    * Typ der Wendepunkt-Beobachtungen
    * Typ der Strecken-Foto-Beobachtungen
    * Typ der Strecken-Bodenzeichen-Beobachtungen
    * Min. und max. Anzahl von Strecken-Fotos
    * Min. und max. Anzahl von Strecken-Bodenzeichen
    * Min. und max. Anzahl von Strecken-Fotos und -Bodenzeichen
    * Ungeeignete Start-Nummern
    -> 'Wettbewerb -> Vorgaben'
- Strecken-Import ('Strecken -> Import Strecke') erweitert
    * Import von KML- und KMZ-Dateien
    * TXT-Datei-Import untertützt nun verschiedene Koordinaten-Darstellungen
      (entsprechend der Wettbewerbseinstellung)
    -> 'Programme -> Flight Contest -> Help' -> 'Streckenplanung'
- Regelwerk-Wettbewerbspunkte erweitert
    * Allgemein -> Steigerungsfaktor für Besatzungen
    * Planungstest -> Verwendung elektronischer Rechenhilfen
    * Navigationstest -> Mitführen von verbotenem elektronischen Equipment
    -> 'Wettbewerb -> Punkte'
- Unterstützung für automatischer Aktionen hinzugefügt
   * Automatisches Öffnen von PDF im Acrobat Reader
   * Automatisches Laden gescannter Planungstest-Formulare
   * Automatisches Laden gescannter Beobachtungstest-Formulare
   * Automatisches Laden eingelesener Logger-Daten
   -> 'Programme -> Flight Contest -> Help' -> 'Automatische Aktionen'
- Aktualisierung folgender Regelwerke
    FAI Precision Flying - Edition 2017
    FAI Air Rally Flying - Edition 2017
    Wettkampfreglement Präzisionsflug-Schweizermeisterschaft - Ausgabe 2017
    Präzisionsflug-Reglement Österreich - Landefeld Typ 1 - Ausgabe 2017
    Präzisionsflug-Reglement Österreich - Landefeld Typ 2 - Ausgabe 2017
    Präzisionsflug-Reglement Österreich - Landefeld Typ 3 - Ausgabe 2017
    Navigationsflug-Reglement Österreich - Ausgabe 2017
- Englische Bediensanleitung Version 3.0 hinzugefügt
    -> 'Programme -> Flight Contest -> Manual'

Erweiterungen in Version 3.0:
-----------------------------
- Navigationsflug-Logger-Auswertung erweitert:
    * Mit 'Import Logger-Daten' werden Logger-Daten direkt aus einer
      ausgewählten GAC-, GPX- oder IGC-Datei geladen und daraus sofort
      Fehlerpunkte berechnet.
    * Mit 'Import AFLOS-Daten -> Kalkuliere AFLOS-Daten' werden 
      Logger-Daten direkt aus einer AFLOS-Datenbank gelesen
      und daraus sofort Fehlerpunkte berechnet,
      sofern der AFLOS-Name der 'Flight Contest'-Strecke, 
      die dem Navigationstest zugeordnet worden ist, 
      mit dem Namen einer existierenden AFLOS-Referenzstrecke,
      die keine Koordinaten beinhalten muss, übereinstimmt.
    * Das Laden in AFLOS kalkulierter AFLOS-Daten ist mit
      'Import AFLOS-Daten -> Import AFLOS-Kalkulation' weiter 
      erreichbar, sofern die AFLOS-Referenzstrecke in 'Flight Contest'
      als Strecke importiert und dem Navigationstest zugeordnet worden ist.
    * Aus den geladenen Logger-Daten können mit 'Neu kalkulieren' 
      Fehlerpunkte neu berechnet werden. 
      Dabei können die auszuwertenden Logger-Zeiten angepasst werden. 
      Anzuwenden, wenn T/O nicht erkannt wurde oder wenn Korrekturen
      an T/O-LDG-Richtung und/oder T/O-LDG-Gate notwendig waren.
- Anzeige von Logger-Aufzeichnungen ('Extras -> Logger-Daten-Anzeige')
    Zeigt Logger-Daten aus GAC-, GPX- oder IGC-Dateien
    mit Google/OSM- oder Offline-Karte an.
- Hilfe zu AFLOS-Themen ist unter 'Programme -> Flight Contest -> AFLOS Help'
  mit folgenden Themen zu finden:
    * Interaktion mit AFLOS
    * AFLOS-Kalkulation im Nachhinein anwenden
        Vorgehen, um die AFLOS-Kalkulation bei Versagen der integrierten
        Flight Contest-Kalkulation zu aktivieren
    * Streckenplanung mit AFLOS
    * Behebung von AFLOS-Problemen
    * Datensicherung der AFLOS-Datenbank während eines laufenden Wettbewerbes
- Offline-Anzeige der Navigationsflug-Karte
- Ausdruck der Navigationsflug-Karte mit der Ergebnisliste:
    Beim Ausdruck aller Besatzungsergebnisse kann die Navigationsflug-Karte
    separat abgewählt werden.
- Navigationsflug-Ergebnis-Export 
    Mit 'GPX-Export' wird das Ergebnis als GPX-Datei exportiert.
    Dieses kann mit 'Extras -> Logger-Daten-Anzeige' oder einem beliebigen anderen
    GPX-Viewer angezeigt werden.
- Koordinatendarstellung in den Contest-Einstellungen wählbar:
   Die Koordinatendarstellung kann zwischen 'Grad Dezimal', 'Grad/Minuten Dezimal'
   und 'Grad/Minuten/Sekunden' gewählt werden.
   Der Strecken-Druck (Strecke -> Druck) druckt alle 3 Darstellungen untereinander.
- Strecken-Export
    Mit 'GPX-Export' wird eine Strecke als GPX-Datei exportiert.
    Diese kann mit 'Extras -> Logger-Daten-Anzeige' oder einem beliebigen anderen
    GPX-Viewer angezeigt werden. 
- FC-Strecken-Import
    Mit 'Strecken -> Import FC-Strecke' kann eine Strecke aus einer GPX-Datei 
    importiert werden, die entweder mit dem Strecken-Export oder dem 
    Navigationsflug-Ergebnis-Export exportiert wurde.
    GPX-Strecken, die mit anderen Programmen erzeugt wurden,
    können mit 'Strecken-Import' importiert werden. 
- Strecken-Import
    Mit 'Strecken -> Import Strecke' können Strecken folgender Formate 
    importiert werden:
    * GPX-Datei, die von einem Fremd-Programm (z.B. 'Flight Planner') erzeugt wurde.
      Darf nur genau eine Strecke (<rte>...</ret>) beinhalten.
      Muss Höhenangaben beinhalten 
      (<rtept lat="49.118333" lon="9.784000"><ele>400.00</ele></rtept>).
    * REF-Datei, die aus einer AFLOS-Referenzstrecke erzeugt wurde. 
    * TXT-Datei, die pro Zeile eine Koordinate mit Höhenangabe enthält.
- Ausdrucke müssen beim Drucken nicht mehr eingepasst werden.
- Unbekannte Zeitkontrollen werden in Englisch mit 'SC' abgekürzt (vorher 'Secret').
- Aktualisierung folgender Regelwerke
    Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2016
    FAI Precision Flying - Edition 2016
    FAI Air Rally Flying - Edition 2016
- Regelwerke für Österreich hinzugefügt:
    Präzisionsflug-Reglement Österreich - Landefeld Typ 1 - Ausgabe 2016
    Präzisionsflug-Reglement Österreich - Landefeld Typ 2 - Ausgabe 2016
    Präzisionsflug-Reglement Österreich - Landefeld Typ 3 - Ausgabe 2016
    Navigationsflug-Reglement Österreich - Ausgabe 2016
  Einschränkungen:
    * "Mitführen von verbotenem elektronischen Equipment" kann nicht 
      angegebenen werden.
    * 20% Punkteaufschlag bei Präzisionsflug-Reglement kann nicht automatisch
      brechnet werden.
    Fügen Sie diese Punkte unter "Sonstige Strafpunkte" hinzu.
- Beachten Sie die erhöhten Anforderungen an die Leistungsfähigkeit
  Ihres Computers, wenn Sie diese Version einsetzen wollen:
    min. 8 GB RAM
    CPU Intel® Core™ i5 oder i7 (min. 2 Cores, min. 2 GHz)
- Eigenständige Installation für den GPX-GAC-Konverter
- Voreinstellwerte für Flugzeit-Berechnung der Starts und Landungen geändert:
    TO -> SP: wind+:3NM, iFP -> iLDG: wind+:2NM, iLDG -> iSP: wind+:3NM
- Besatzungs-Liste: Kommando 'Startnummern sortieren' eingeführt
- Planung: Kommando 'Zeitplan-Export' eingeführt
- Anwendung 'Print Label' für Label-Drucker Brother QL-570
    Druckt Label mit Daten des 'Zeitplan-Export'-Kommandos.
- Hilfe ('Programme -> Flight Contest -> Help') erweitert:
    'Zu erledigende Aufgaben' beschreibt Aufgaben 
    des Wettbewerbs-Ausrichters und des Auswerte-Schiedsrichters

Fehlerbehebungen und Erweiterungen in Version 2.3.7:
----------------------------------------------------
- Das Scheitern der erneuten Zuweisung von Planungsaufgaben wurde behoben.
- Beim Zuweisen von Planungsaufgaben erscheint jetzt ein zusätzliches
  Abfragefenster, was auf das Löschen bereits eingegebener Ergebnisse
  hinweist.

Fehlerbehebungen in Version 2.3.6:
----------------------------------
- Probleme von Ausdrucken mit Wettbewerbsnamen, die Umlaute enthalten,
  beseitigt.

Fehlerbehebungen und Erweiterungen in 2.3.5:
--------------------------------------------
- Anzeige und E-Mail-Versand der Navigationsflug-Karte wird jetzt 
  nur bei AFLOS-Datenbank mit zutreffender Referenzstrecke ermöglicht
  (für mehrtägige Wettbewerbe mit mehreren AFLOS-Datenbanken)
- Ausdrucke mit Wettbewerbsnamen, die das Zeichen & enthalten,
  sind jetzt möglich
- 'Ergebnisse -> Alle Navigationsflüge als E-Mail versenden' eingeführt
- 'Extras -> Live-Einstellungen -> Live-Ergebnis einmal berechnen' eingeführt
- Scripte zum Starten und Stoppen von Flight Contest eingeführt:
    Start -> Alle Programme -> Flight Contest -> Scripts
      -> Start Flight Contest
      -> Stop Flight Contest
- Flight Contest - Datenbanksicherung stoppt und startet Flight Contest
  jetzt automatisch
    Start -> Alle Programme -> Flight Contest -> Scripts
       -> Save contest database
- Die AFLOS-Datenbank-Tabellenanzeige für Entwicklungszwecke wurde entfernt.

Fehlerbehebungen und Erweiterungen in Version 2.3.4:
----------------------------------------------------
- E-Mail-Versand bei leerem Aufgaben-Namen funktionierte nicht.
- E-Mail-Versand einer Strecke an die Wettbewerbsleitung eingeführt. 
- Anzeige der Ziel-E-Mail-Adresse als Tool-Tipp über E-Mail-Button.

Fehlerbehebungen und Erweiterungen in Version 2.3.3:
----------------------------------------------------
- Kollisionen zwischen Live-Anzeige und Auswerter-Eingaben reduziert
- Strecken-Karte: Check-Punkt-Zoom-Faktoren berücksichtigen jetzt 
  konfigurierte Gate-Breiten
- Ein laufender E-Mail-Versand der Navigationsflug-Karte im Hintergrund 
  wird jetzt in Ergebnisliste mit einem Symbol angezeigt.

Fehlerbehebungen und Erweiterungen in Version 2.3.2:
----------------------------------------------------
- Fehlerbehebung GPX-GAC-Konverter
- E-Mail-Versand der Navigationsflug-Karte im Hintergrund
- Druck-Abstürze bei leerem Aufgaben-Namen beseitigt
- Der eingetragene Veranstalter bei einem neuem Wettbewerb wird nun übernommen.
- Meldungen beim Setzen von Planungsaufgaben und dem Zuweisen des Flugwinds.

Erweiterungen in Version 2.3.0:
-------------------------------
- Kurzer Klassenname eingeführt.
- Massenhaftes Löschen, Deaktivieren und Aktivieren von Teams und Besatzungen
  eingeführt.
- FP->LDG-Berechnungs-Voreinstellwert auf "wind+:6NM" geändert.
- Planungstest: Deaktivierte Punkte werden mit gedruckt, um die Anfänge zu
    berechnender Streckenabschnitte klar zu erkennen.
- Flugplan: Ausdruck der Gesamtflugzeit eingeführt.
- Separates Einstell-Formular für Besatzungs- und Schiedsrichter-Zeitplan-Druck
    Druck erfolgt aus Einstell-Formular heraus.
- Schiedsrichter-Zeitplan: 
    Einstellungen für Intermediate-Tower und -Landetest eingeführt.
- Übersichts-Zeitplan eingeführt:
    * Druckt Briefing-Zeit, wenn angegeben.
    * Druckt erste und letzte Zeiten für Planungstests, Takoffs und Landungen.
    * Druckt Takeoff-Abstand.
    * Druckt TAS-abhängige Gesamtflugzeiten (von Takeoff bis Landung).
- Besatzungs-Zeitplan: 
    TAS-abhängige Gesamtflugzeiten können zusätzlich mit gedruckt werden.
- Landemesswert-Eingabe um "out" und "no" erweitert:
    Als Landemesswert können die Werte "out" (Landung außerhalb der Landbox) 
    und "no" (keine Landung) eingegeben werden, um Mausbedienung bei der Eingabe
    zu vermeiden.
- AFLOS-Logger-Daten-Import: 
    * Referenzstrecken-Auswahl ergänzt
        Bei Abstürzen des AFLOS-Calculates kann eine andere (reduzierte) Strecke
        kalkuliert werden, die dann abweichend von der geplanten Strecke hier 
        auswählbar ist.
    * Option "Vorhandene Logger-Daten nicht löschen" ergänzt.
- Unterschriften-Druck eingeführt.
- Veranstalter-Druck in Fußzeile eingeführt.
- Druck eines freien Textes mit Wettbewerbstitel eingeführt.
- Wettbewerbs-Auswertung erweitert: 
    * Filter 'Alle Teams'
    * Option 'Aufgaben-Detail-Ergebnisse'
    * Die Plazierung gleicher Plätze kann mit +/- festgelegt werden.
- Check-Punkt-Deaktivierung erweitert:
    Überflugzeit, Tor vorbei, Procedure Turn, Bad Course und Höhe
    können jetzt je Check-Punkt separat deaktiviert werden.
- Live-Ergebnis-Anzeige erweitert:
    * Gleichzeitige Anzeige der Ergebnisse mehrerer Aufgaben (= Wettbewerbstage)
    * Konfigurierbare Live-Ergebnis-Positionsberechnung
- Live-Ergebnis-Anzeige-Konfiguration ('Extras -> Live-Einstellungen')
    Hier wird die Live-Anzeige konfiguriert sowie ein- und ausgeschaltet.
    'Programme -> Flight Contest -> Help' -> 'Live-Ergebnisanzeige'
- Team-Auswertung: 
    * Deaktivierung von ganzen Teams möglich.
    * Deaktivierung einzelner Besatzungen eines Teams von der Team-Auswertung 
      möglich.
- Hilfe ('Programme -> Flight Contest -> Help') erweitert:
    * Kapitel 'Planungstest und Flugzeugvorbereitung' hinzugefügt.
    * Kapitel 'Behebung von AFLOS-Problemen' überarbeitet.
    * Kapitel 'Druck-Stil beeinflussen' hinzugefügt:
        Beeinflussung des Druck-Stils mit CSS-Werten
- Einzelne Besatzungen können für eine Aufgabe deaktiviert werden.
    Wird diese Aufgabe bei einer Wettbewerbs- oder Team-Auswertung
    einbezogen, wird diese Besatzung automatisch weggelassen.
- Besatzungs-Druck:
    Die Reihenfolge des Ausdrucks kann durch eine Aufgabe festgelegt werden.
    Einzelne für die Aufgabe deaktivierte Besatzungen werden dann weggelassen.
- Wettbewerbs-/Klassen-Auswertung:
    Deaktivierung einzelner Besatzungen möglich.
- Wahl der Landepunkt-Wertung je Landung möglich.
    Bei Präzisionsflug-Landungen, wo unterschiedliche schwere Landungen 
    mit unterschiedlichen Strafpunkten bewertet werden, kann eine vom
    Standard abweichende Reihenfolge jetzt unter Aufgaben-Einstellungen 
    konfiguriert werden.
- 'Programme -> Flight Contest -> Help' -> 'Auswertungen' hinzugefügt
- Import Excel-Besatzungsliste erweitert:
    * Zusätzliche E-Mail-Spalte (mehrere E-Mails mit Komma trennen)
    * Option "Start-Nr. 13 weglassen"
- 'Programme -> Flight Contest -> Help' -> 
  'Installations-abhängige Konfiguration' hinzugefügt
- Neue Navigationsflug-Ergebnis-Anzeige
    * Navigationsflug-Karte mit Google
    * Checkpunkte können hier mit einem Klick angezeigt werden
    * E-Mail-Versand der Navigationsflug-Karte
        Neben der Besatzung wird diese E-Mail auch an den 
        Auswerte-Schiedsrichter versandt.
    * Anzeige des erfolgreichen E-Mail-Versands in der Ergebnisliste
- Abruf der E-Mails an den Auswerte-Schiedsrichter ('Extras -> Web-Mail')
- Aktualisierung der FAI Regelwerke
    FAI Precision Flying - Edition 2015
    FAI Air Rally Flying - Edition 2015
- Aufteilung des Regelwerkes "FAI Air Rally Flying" in 2 Regelwerke
    FAI Air Rally Flying - Unlimited - Edition 2015
    FAI Air Rally Flying - Advanced - Edition 2015
- Aktualisierung des deutschen Regelwerkes
    Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2015

Fehlerbehebungen in 2.2.6:
--------------------------
- Aktualisierungs-Probleme bei bestimmten Contest-Optionen beseitigt.

Fehlerbehebungen und Erweiterungen in Version 2.2.5:
----------------------------------------------------
- Höhenauswertung bei Zwischenlandung (iLDG und iT/O) entfernt.
- Von der Zeitauswertung deaktivierte und nicht korrekt geflogene Wendepunkte
  (am Wendepunkt-Tor vorbeigeflogen) werden jetzt bestraft.
- Aktualisierungs-Probleme bei Live-Ergebnisanzeige beseitigt.
- 'Programme -> Flight Contest -> Help' -> 'Behebung von AFLOS-Problemen'
  hinzugefügt
- Die empfohlene Konfiguration für eine 'Zwischenlandung mit Pause' wurde in den
  Streckenplanungsbeispielen korrigiert
  (siehe 'Programme -> Flight Contest -> Help' -> Abschnitt 'Streckenplanung')
- Eine AFLOS-Kurzanleitung wird mit installiert 
  (siehe 'Programme -> Flight Contest')

Erweiterungen in Version 2.2.4:
-------------------------------
- Live-Ergebnisanzeige
  Ermöglicht Anzeige eines vorläufigen Ergebnisses während des Debriefings.
  (siehe 'Programme -> Flight Contest -> Help' -> Abschnitt 'Live-Ergebnisanzeige')

Fehlerbehebungen in Version 2.2.3:
----------------------------------
- Unzulässige Wendeschleifen-Berechnungen bei SP,iFP,iLDG,iT/O,iSP und FP
  beseitigt. 
- Schiedsrichterzeitplan-Druck-Optionen: 
  Anwahl von Check-Punkten hat andere Check-Punkte mit angewählt.
- Wettbewerbs-Auswertungs-Einstellungen:
  Anwahl von 'Besatzungen, die zu keinem Team gehören' stürzte ab.
- Aufruf von Besatzungsergebnissen aus Wettbewerbs- und Klassen-Auswertung:
  'Abbrechen' kehrte nicht zu 'Auswertung' sondern zu 'Ergebnisse' zurück.
- Den Streckenplanungsbeispielen wurde die 'Zwischenlandung mit Pause' hinzugefügt
  (siehe 'Programme -> Flight Contest -> Help' -> Abschnitt 'Streckenplanung')

Fehlerbehebungen in Version 2.2.2:
----------------------------------
- Nach Strecken-Kopie konnte die Strecken-Liste nicht mehr betrachtet werden.

Fehlerbehebungen und Erweiterungen in Version 2.2.1:
----------------------------------------------------
- Unterstützung des Loggers DG-200
    Dieser Logger speichert Flug-Informationen in GPX-Dateien.
    Aus diesen GPX-Dateien müssen mit 'Extras -> GPX-GAC-Konverter'
    GAC-Dateien erzeugt werden, die in AFLOS zu importieren sind.
    Siehe 'Programme -> Flight Contest -> Help' -> 'Unterstützte Logger'.
- Fehler in Strecken-Kartenansicht (Strecken-Details -> 'Strecken-Karte') behoben
    Strecken-Punkte wurden manchmal nicht in der korrekten Reihenfolge 
    miteinander verbunden.

Erweiterungen in Version 2.2.0:
-------------------------------
- Unterstützung der AFLOS-Intermediate-Punkte iFP, iLDG, iT/O und iSP.
    Bei Strecken mit einer Touch&Go-Zwischenlandung sind iFP, iLDG und iSP
    in die Strecke einzubauen.
    Siehe 'Programme -> Flight Contest -> Help' -> Abschnitt 'Streckenplanung'.
- AFLOS-Strecken-Import-Markierungen erweitert.
    Check-Punkte in den AFLOS-Referenz-Daten können im Feld 'Mark' mit folgenden 
    weiteren Markierungen versehen werden, die beim Import der Strecke 
    verarbeitet werden:
    * $curved - Diesen Check-Punkt als Punkt einer krummen Strecke festlegen.
    * $noplanningtest - Diesen Check-Punkt nicht in Flugplanungstest aufnehmen.
    Bei einem krummen Streckenabschnitt sind alle Punkte innerhalb mit $curved
    zu markieren, der Punkt am Ende mit $noplanningtest.
    Details zur Anwendung dieser und aller anderen Markierungen sind in 
    'Programme -> Flight Contest -> Help' -> 'Streckenplanung' zu finden.
- Streckenplanung überarbeitet.
    Enthält jetzt Strecken-Beispiele für
    * Normale Strecken
    * Unbekannte Zeitkontrollen
    * Zwischenlandung (Touch&Go)
    * Krummer Streckenabschnitt
    * Präzisionsflugstrecke
    Siehe 'Programme -> Flight Contest -> Help' -> 'Streckenplanung'.
- Flugplanungstest:
    Einzelne Wendepunkte einer Strecke können für die Verwendung beim Flugplanungstest
    weggelassen werden. Dazu kann bei einer noch nicht verwendeten Strecke 
    bei einem Punkt die Eigenschaft "Kein Flugplanungstest" aktiviert werden.
    In einer AFLOS-Referenzstrecke kann dazu die Markierung $noplanningtest
    angegeben werden. 
- Wind- und entfernungsabhängige Zeitberechnung für SP, LDG, iLDG und iSP eingeführt.
    Bisher war für SP und LDG nur die Angabe einer festen Zeit für alle Besatzungen
    möglich. Details zur Anwendung sind im neuen Hilfe-Kapitel 
    'Programme -> Flight Contest -> Help -> 
    Flugzeit-Berechnung für Starts und Landungen' zu finden.
- Neuer Hauptmenüpunkt 'Ergebnisse'
    Hier erfolgt jetzt die Ergebnis-Eingabe der Aufgaben eines Wettbewerbstages
    (war vorher unter 'Auswertung' zu finden).
- Inhalt des Hauptmenüpunktes 'Auswertung' verändert 
    Die Ermittlung des Endergebisses bei klassenlosen Wettbewerben oder 
    bei zu mischenden Klassen erfolgt unter 'Wettbewerbs-Auswertung',
    ansonsten unter dem Klassen-Namen.
    Die Ermittlung des Team-Endergebisses erfolgt unter 'Team-Auswertung'.
- Strecken-Kartenansicht (Strecken-Details -> 'Strecken-Karte')
    Ermöglicht Ansicht einer importierten Strecke mit Google Maps in der
    Gelände-Ansicht.
    Zur Prüfung der Genauigkeit der verwendeten Koordinaten.
    Erfordert bei Aufruf Verbindung ins Internet.
- Bedienungsanleitung
    'Programme -> Flight Contest -> Manual'

Fehlerbehebungen in Version 2.1.3:
----------------------------------
- Unterstützung gebrochener Steuerkurse bei der Eingabe der Ergebnisse 
  eines Planungstests
- Schiedsrichter-Zeitplan: Druck-Voreinstellung für Tower hinzugefügt.
- Wettkampfreglement Präzisionsflug-Schweizermeisterschaft 
  an Ausgabe 2014 angepasst.
- Standard-Wettbewerbsablauf überarbeitet 
  ('Programme -> Flight Contest -> Help' -> Abschnitt 'Standard-Wettbewerbsablauf')
- Automatische Grafik-Vergrößerung beim A3-Portrait-Druck eingeführt
- Format-Angabe im Druckdateinamen eingeführt
- Neuer Browser-Titel bei Ergebnis-Eingaben:
  "Startnummer - Kennzeichen - Aufgabe - Detail (Version X)"
- Navigationstest-Ergebnisdruck: AFLOS-CPs werden mit gedruckt.

Fehlerbehebungen in Version 2.1.2:
----------------------------------
- Die Zeitauswertung enthielt unerwünschte 'Procedure Turn'-Einträge nach SP und FP.

Fehlerbehebungen in Version 2.1.1:
----------------------------------
- Bei nicht geflogenen Wendepunkten wurden davor beobachtete Kursabweichungen
  mit einer zu hohen Anzahl aus AFLOS-Loggerdaten gelesen.  

Erweiterungen in Version 2.1.0:
-------------------------------
- Verbesserungen für krumme Strecken-Abschnitte
    Vor oder nach einem krummen Strecken-Abschnitt sind auch Wendeschleifen 
    erlaubt. Diese können aber bei Bedarf durch eine abweichende 
    Richtungsangabe ($track) außer Kraft gesetzt werden. 
    Der Flugplan der Besatzung erhält als Richtung automatisch die Richtung
    auf den ersten Hilfspunkt als Einflug-Kurs auf die krumme Strecke.
    ('Programme -> Flight Contest -> Help' -> Abschnitt 'Streckenplanung')
- Strecken-Koordinaten-Ausdruck
    Ermöglicht Ausdruck der Koordinaten einer Strecke 
    mit und ohne unbekannte Zeitkontrollen.
- Planung: Takeoff-Abstand bei langsamerem Folgeflugzeug
    Ermöglicht es, vor jeder Geschwindigkeits-Gruppe einen zusätzlichen
    Zeitabstand einzustellen, um gleichzeitiges Kreisen vor dem Start-Punkt
    zu vermeiden. 
- AFLOS 2.13 für automatische Auswertung des Takeoff-Zeitfensters erforderlich
    Die AFLOS-Calculate-Funktion in älteren Versionen berechnet
    die Überflugzeit des T/O-Punktes nicht.
- Probleme bei der Umschaltung von manueller auf automatische Auswertung
  der Einhaltung des Takeoff-Zeitfensters und der spätesten Landezeit
  wurden beseitigt.

Erweiterungen in Version 2.0.0:
-------------------------------
- Tips wurde in Help ('Programme -> Flight Contest -> Help') umbenannt.
    Enthält folgende Kapitel:
      Bedien-Tipps
      Standard-Wettbewerbsablauf
      Änderungen bei Wettbewerbs-Durchführung (neu)
	  Besatzungsliste erstellen (neu)
      Streckenplanung (neu)
	  Landungen (neu)
	  Datensicherung eines laufenden Wettbewerbes (neu)
- Vereinfachte Ergebnis-Listen-Eingabe:
    Die Eingabe von Besatzungs-Ergebnissen kann nun mit 
	'Fertig und nächstes Ergebnis' beendet werden, um sofort 
    die Eingabe des nächsten Besatzungs-Ergebnisses vorzunehmen.
- Möglichkeit, die Zeitauswertung von UZK-Koordinaten zu deaktivieren:
	Durch Abwahl der Navigationstest-Aufgaben-Einstellung können
	unbekannte Zeitkontrollen (UZK) für eine Klasse aus der Zeit-
	auswertung genommen werden.
- Automatische Auswertung der Einhaltung des Takeoff-Zeitfensters und
  der spätesten Landezeit einer Aufgabe mit Hilfe der Logger-Daten
    Dazu müssen Takeoff- und Landungs-Tor in AFLOS geeignet konfiguriert werden.
    Details sind in 'Programme -> Flight Contest -> Help' im Abschnitt
    'Streckenplanung' zu finden.
    In den Navigationstest-Aufgaben-Einstellungen kann die automatische 
    Auswertung abgeschaltet werden. In diesem Fall müssen Schiedsrichter,
    die die Landebahn überwachen, tätig werden.
- Möglichkeit, nur die besten x Aufgaben einer Besatzung zu werten:
    Mit der Aufgaben-Einstellung 'Als beste Aufgabe auswerten' können
	die Aufgaben festgelegt werden, die dafür einzubeziehen sind.
	Mit der Wettbewerbs-Einstellung 'Anzahl der Aufgaben, von denen 
	das beste Ergebnis auszuwerten ist' wird die Anzahl x der Aufgaben
	festgelegt, deren geringste Punktezahl dafür zu summieren ist.
- Verbergen von Aufgaben
    Aufgaben können in der Anzeige von 'Planung' und 'Auswertung' verborgen 
    werden, um das versehentliche Ändern erledigter Wettbewerbstage zu
    vermeiden.
    Neue Besatzungen werden einer Aufgabe nicht hinzugefügt, wenn sie in der
    'Planung' nicht angezeigt werden (für kombinierte Wettbewerbe).
- Aufgaben-Kopie:
    Möglichkeit, eine neue Aufgabe mit den Einstellungen einer
	bestehenden Aufgabe anzulegen (Schalter 'Kopieren' in 'Aufgabe bearbeiten').
- Auswertung: 'Deaktivierte Check-Punkte' wurde in 
    'Zeitauswertung von Check-Punkten deaktivieren' umbenannt.
	Unterschreitung der Mindestflughöhe und festgestelltes Kreisen
	wird beim einem deaktivierten Check-Punkt weiter bestraft.
- Neue Auswertungseinstellungen:
    Angabe eines beliebigen Titels beim Ausdruck.
    Die Ausgabe von '[vorläufig]' kann forciert werden.
	Zusätzliche Filterung nach Teams bei Wettbewerbs- und Klassen-Auswertung.
- Ausdruck:
    PDF-Dateinamen beschreiben jetzt den enthaltenen Inhalt.
	PDF-Dateinamen wird ein Präfix vorangestellt, der mit 'fc-'
	beginnt, gefolgt von einer Abkürzung des Wettbewerbstitels
	oder einer eigenen Festlegung.
	Zeitplan-Dateinamen werden um Ausgabe-Nummer ergänzt.
    Wahl von Hoch- und Querausdruck
    Wahl von A3-Papier
- Ausdruck von anpassbaren Grafiken in der Kopfzeile
	Auswahl der Grafiken in 'Wettbewerbseinstellungen'.
    Unterstützung von 3 Bildern: am linken Rand, in der Mitte und am rechten Rand.
	Wird ein linkes und ein rechtes Bild zugeordnet, wird der Wettbewerbstitel 
	zentriert angeordnet, sonst linksbündig.
	Wird ein mittleres Bild zugeordnet, wird der Wettbewerbstitel weggelassen.
	Grafik- und Titel-Größe können eingestellt und mit einem Test-Ausdruck 
	in der Wirkung beurteilt werden.
	Max. Größe einer Grafik: 1 MB.
- Zeitplan/Flugplan:
    Anzeige der letzten Änderung.
- Ausdruck-Optionen bei Zeitplan
    Wahl auszudruckender Spalten
	Zusätzlicher Untertitel
	Änderungen der verschiedenen Ausgaben
- Ausdruck-Optionen bei Schiedsrichterzeitplan und Besatzungen
    Wahl auszudruckender Spalten
	Zusätzlicher Untertitel
	Bis zu 3 Leerspalten
- Anderer Test: 
    Festlegung eines beliebigen Titels (anstelle von 'Andere Ergebnisse')
- Flugzeug-Wechsel:
    Das Ändern des Flugzeuges einer Besatzung ändert erst einmal 
    nicht das Flugzeug, das in einer Aufgabe für Planung und Navigationsflug
    benutzt wurde. Dadurch ist es möglich, dass eine Besatzung von Aufgabe 
    zu Aufgabe jeweils ein anderes Flugzeug verwenden kann. 
    Derartige Abweichungen werden in der Planung mit einem ! markiert.
    Das Flugzeug der Aufgabe wird an die der Besatzungs-Festlegung
    angepasst, wenn die Reihenfolge innerhalb einer Aufgabe für diese
    Besatzung geändert wird.
    Frühere Nutzungen eines Flugzeuges werden unter 'Flugzeug bearbeiten'
    angezeigt.
- Navigationsflug-Messwert-Druck:
    Ausdruck der AFLOS-Messwerte Überflugzeit, Kursabweichungen (Kreisen)
	und Flughöhe ohne Strafpunkte.
- AFLOS-Strecken-Import erweitert:
  Check-Punkte in den AFLOS-Referenz-Daten können im Feld 'Mark' mit folgenden 
  weiteren Markierungen versehen werden, die beim Import der Strecke 
  verarbeitet werden:
    * $duration:10min - Feste Flugzeit vom vorangegangenen Check-Punkt in Minuten
    * $notimecheck    - Keine Zeitauswertung für diesen Check-Punkt
  Details zur Anwendung dieser und aller anderen Markierungen sind in 
  'Programme -> Flight Contest -> Help' im Abschnitt 'Streckenplanung' zu finden.
- Vollständige Unterstützung der Punkte-Auswertung für 'FAI Air Rally Flying'
    Punkte pro Sekunde Zeitüberschreitung des Takeoff-Zeitfensters wird ermittelt

Fehlerbehebungen in Version 1.2.4:
----------------------------------
- TrueTrack-Rundungs-Problem bei Leg-Berechnung beseitigt.
- Ausgabe der GroundSpeed im Flugplan mit 1 Nachkommastelle.
- 180-Grad-Eingabeproblem bei Planungstest-Auswertungs-Eingabe beseitigt.
- Takeoff-Abstands-Warnung: Warnung wird nur bei Unterschreitung des
  definierten Standard-Takoff-Abstandes ausgegeben, 
  auch bei schnellerem Folgeflugzeug.
- Probleme der nachträglichen Aktivierung der Klassenverwaltung 
  eines Wettbewerbes, bei dem schon Aufgaben geplant wurden, behoben.
- Sortierreihenfolge beim Löschen einer Besatzung bleibt nun erhalten.
- Besatzungsergebnis-Druck: Kopfzeile um Startnummer und Besatzungsname ergänzt.
- Fehlfunktion beim 'Wettbewerb kopieren' beseitigt.
- Beispiel eines Beobachtungsprotokolls hinzugefügt:
  'Programme -> Flight Contest -> Samples -> FC-ObservationLog-Sample.xls'

Fehlerbehebungen und Erweiterungen in Version 1.2.3:
----------------------------------------------------
- Anzeige der Flugzeug-Doppelnutzung in Zeitplan und Besatzungsliste

Fehlerbehebungen und Erweiterungen in Version 1.2.2:
----------------------------------------------------
- Planungsaufgabe: Fehlender Ausdruck des Windes wurde ergänzt.
- Zeitplan berechnen: Die Berechnung nicht berechneter Mannschaften
  orientiert sich jetzt an der Anfangszeit der vorherigen Besatzung.
- Planung: Das Verschieben der Besatzungs-Reihenfolge nach oben 
  löscht jetzt nur noch die Zeiten der verschobenen Besatzung(en) 
  und damit nicht mehr die Zeit der darüberliegenden Besatzung.
- TAS-Änderung: Das Ändern der TAS einer Besatzung ändert erst einmal 
  nicht die TAS, die in einer Aufgabe für Planung und Navigationsflug
  benutzt wurde. Dadurch ist es möglich, dass eine Besatzung von Aufgabe 
  zu Aufgabe jeweils eine andere TAS verwenden kann. 
  Derartige Abweichungen werden in der Planung jetzt mit einem ! markiert.
  Die TAS der Aufgabe wird allerdings an die der Besatzungs-Festlegung
  angepasst, wenn die Reihenfolge innerhalb einer Aufgabe für diese
  Besatzung geändert wird.
- Planung: Warnungen bei Nichteinhaltung der Takeoff-Zeit-Reihenfolge
  eingeführt. Diese berücksichtigt auch den anderen Takeoff-Abstand
  bei einem schnelleren Folgeflugzeug.  
- Planung: Beim Deaktivieren einer Besatzung werden Warnungen
  folgender Besatzungen nicht automatisch gelöscht. Die Neuberechnung
  von Warnungen erfolgt mit 'Zeitplan berechnen'.

Erweiterungen in Version 1.2.1:
-------------------------------
- Streckenaudruck verbessert
  * Der Koordinaten-Tabelle wurden Spalten für AFLOS-Check-Punkte, Höhe und
    Torbreite hinzugefügt.
  * Den Auswerte- und Test-Etappen-Tabellen wurde die Gesamtentfernung
    und die Anzeige von Kursänderungen >= 90 Grad hinzugefügt.

Erweiterungen in Version 1.2.0:
-------------------------------
- Einführung einer Start-Nummer für Besatzungen
  Diese ändert sich nicht, wenn die Reihenfolge der Besatzungen
  bei einer Aufgabe geändert wird.
  Diese Nummer entspricht der AFLOS-Competitor-Nummer.
  FC-CrewList-Sample.xls wurde entspr. erweitert.
- Bei Neuanlage einer Aufgabe werden jetzt alle Einstellungen
  im Eingabe-Formular angezeigt.
- Automatische Neuberechnung des Zeitplanes bei Änderungen bestimmter 
  Aufgaben-Einstellungen hinzugefügt.
  Die Auswirkung einer Einstellungsänderung wird im Eingabe-Formular erklärt.
- Soll eine Wendeschleife (Kursänderung am Wendepunkt >= 90 Grad) nicht
  geflogen werden, ist bei der Flugzeit der Wendeschleife 0 min anzugeben.
  Der Flugplan enthält dann keine Aufforderung zur Wendeschleife.
  Kursänderungen >= 90 Grad werden in der Koordinaten-Liste einer Strecke
  angezeigt (= Kursänderung nach Erreichen des vorangegangen Wendepunktes).
- Ausdruck der Navigationsflugergebnisse verbessert:
  * Nicht getroffene Wendepunkte drucken beim Messwert das Zeichen '-'.
  * Deaktivierte Wendepunkte drucken bei den Punkten das Zeichen '-'.
  * Wendeschleifen-Fehler werden bei deaktivierten Wendepunkten
    nicht bestraft und drucken bei den Punkten das Zeichen '-'.
  * Werden Auswerte-Punkte für Wendeschleifen-, Kursabweichungs- 
    oder Höhen-Fehler mit 0 festgelegt, werden Spalten für die Punkte
    dieser Werte beim Ausdruck weggelassen.
- AFLOS-Strecken-Import erweitert
  Check-Punkte in den AFLOS-Referenz-Daten können im Feld 'Mark' 
  mit folgenden Markierungen versehen werden, die beim Import
  der Strecke verarbeitet werden:
  * $secret - Check-Punkt als unbekannte Zeitkontrolle importieren,
    für unbekannte Zeitkontrollen mit von 2NM abweichender Torbreite
  * $ignore - Check-Punkt nicht importieren
  * $dist:26,5mm - Entfernung vom vorangegangenen Wendepunkt in mm
    (für eine Karte mit dem Wettbewerbs-Maßstab 1:200000)
  * $dist:12,3NM - Entfernung vom vorangegangenen Wendepunkt in NM
  * $dist:22,4km - Entfernung vom vorangegangenen Wendepunkt in km
  * $track:142 - Richtung vom vorangegangenen Check-Punkt in Grad
  Der vorangegangene Wendepunkt ist ein Check-Punkt, der importiert wird
  und keine unbekannte Zeitkontrolle ist.
  Beim Import können folgende Erkennungs-Verfahren für unbekannte
  Zeitkontrollen gewählt werden:
  * Keine Erkennung
  * Check-Punkte mit Torbreite 2NM
  * Check-Punkte mit Markierung $secret
  * Check-Punkte mit Torbreite 2NM oder Markierung $secret (Default)
- Standard-Wettbewerbsablauf überarbeitet
  * Neue Einteilung in 3 Wettbewerbsphasen
    A. Wettbewerbs-Vorbereitung
    B. Wettbewerbs-Beginn
    C. Wettbewerbs-Durchführung
  * Anpassungen bei Strecken-Vorbereitung
  * Anpassungen bei Aufgaben-Einstellungen
- Programm-Menü erweitert
  * Programme -> Flight Contest -> Usage:
      Vorbereitungs- und Nutzungshinweise
  * Programme -> Flight Contest -> Rules:
      alle unterstützten Wettbewerbsregeln
- Beschreibung
  'Wiederherstellung eines fehlgeschlagenen Flight Contest-Updates'
  hinzugefügt, siehe unten

Fehlerbehebung in Version 1.1.1:
--------------------------------
- Beim Anlegen von Klassen beim Import von Besatzungen aus Excel-Vorlage
  wurden die Auswertungs-Punkte der Klassen nicht korrekt eingestellt.
  Das Problem besteht nicht, wenn die Klassen vor dem Import manuell
  angelegt wurden. 
  Von der gewählten Wettbewerbsordnung abweichende Punkte werden in
  der Punkte-Anzeige mit einem ! markiert.
  Bei bestehenden Wettbewerben kann eine Punkteanweichung durch Wechsel
  zu einer anderen Wettbewerbsordnung, Speichern, Wechsel zurück zur
  gewünschten Wettbewerbsordnung und Speichern repariert werden.
- Beim Import von Besatzungen aus Excel-Vorlage werden zwischen
  Pilot und Copilot ein , eingefügt.

Erweiterungen in Version 1.1.0:
-------------------------------
- Unterstützung kombinierter Wettbewerbe
  * Wettbewerbs-Auswertung auch für klassenbasierte Wettbewerbe,
    wo nach Klassen gefiltert werden kann, eingeführt.
  * In der Wettbewerbs-Auswertung kann zusätzlich nach Aufgaben
    gefiltert werden.
  * Teilen Sie die Teilnehmer derart in Klassen auf,
    dass je Wettbewerb entsprechende Aufgaben und Klassen als
    Filter anwendbar sind. Sie benötigen jeweils eine Klasse für
    - Teilnehmer, die nur an einem Wettbewerb teilnehmen
    - Teilnehmer, die an mehreren Wettbewerben teilnehmen.
- Zeitplan-Berechnung auch bei unvollständiger Planung
  (kein Flugwind zugewiesen) möglich.

Fehlerbehebungen in Version 1.0.1:
----------------------------------
- Beschreibung zur Behebung von Server-Ausführungsproblemen 
  nach Update-Installation hinzugefügt (siehe unten).

Erweiterungen in Version 1.0:
-----------------------------
- Klassenverwaltung:
  * Ein Wettbewerb kann auf Klassen-Auswertung umgestellt werden.
    Die Auswertungsdetails werden dann in der Aufgabe pro Klasse definiert.
  * Besatzungen können Klassen zugeordnet werden.
  * Je Klasse existiert eine separate Ergebnis-Auswertung, die über ihren
    Klassen-Namen in 'Auswertung' erreichbar ist.
  * Mit 'Klassen-Ergebnis-Einstellungen' können verschiedene 
    Auswertungs-Details eingestellt werden.
- Teamverwaltung: 
  * Ein Wettbewerb kann für eine Team-Auswertung konfiguriert werden.
    Dazu wird im Wettbewerb die Mindestanzahl vorhandener Team-Mitglieder
    zur Team-Auswertung festgelegt.
  * Besatzungen können Teams zugeordnet werden.
  * In 'Auswertung' ist die Team-Auswertung durchführbar.
  * Mit 'Team-Auswertungs-Einstellungen' können verschiedene 
    Auswertungs-Details eingestellt werden.
- Erweiterte Wettbewerbs-Auswertung:
  * In 'Auswertung' ist die Wettbewerbs-Auswertung durchführbar.
  * Mit 'Wettbewerbs-Auswertungs-Einstellungen' können verschiedene 
    Auswertungs-Details eingestellt werden.
- Erweiterte Landungseingabe:
  * Es können bis zu 4 Landungen aktiviert werden.
  * Wird min. eine Landung aktiviert, wird die Eingabe der Gesamtstrafpunkte 
    aller Landungen durch eine Landemesswert-Eingabe und zusätzliche 
    Lande-Fehlerschalter je Landung ersetzt.
  * Die Strafpunkte einer Landung werden aus dem Landemesswert mit einer 
    konfigurierbaren Formel (siehe Help -> Landungen)
    und den Lande-Fehlerschaltern mit konfigurierbarer Strafpunktzahl berechnet.
- Auswahl der Wettbewerbsordnung
  * Die Wettbewerbsordnung wird beim Anlegen eines Wettbewerbs bzw.
    einer Klasse festgelegt. 
  * Einzelne Werte können in 'Punkte' geändert werden.
  * Die Wettbewerbsordnung kann je Wettbewerb bzw. je Klasse geändert werden.
    Eine Neuberechnung vorhandener Ergebnisse findet aber erst mit Aufruf von
    'Auswertungen neu berechnen' (in 'Punkte' zu finden) statt.
- Hochladen von AFLOS-Datenbanken:
    Es ist nun möglich, AFLOS auf einem anderen PC im Netzwerk zu betreiben.
    Nach dem Lesen von Logger-Daten ist dazu die AFLOS-Datenbank über
    'AFLOS -> Datenbank hochladen' auf den 'Flight Contest'-Server hochzuladen.
    Details siehe unten (Interaktion mit AFLOS)
- Der Begriff 'Mannschaft' wurde durch 'Besatzung' ersetzt.
- Der Begriff 'Protestprotokoll' wurde durch 'Besatzungsergebnis' ersetzt. 
- Beim Druck eines oder aller Besatzungsergebnisse können zu druckende Details
  (Planung, Navigationsflug, Beobachtungen, Landung, andere Ergebnisse)
  festgelegt werden.
- Vereinfachte Strecken-Eingabe:
    Die Eingabe von Strecken-Karten-Messwerten kann 
    nun mit 'Speichern und nächster Punkt' beendet werden, um sofort 
    die Eingabe des nächsten Karten-Messwertes vorzunehmen.
- Vereinfachte Auswertungs-Eingabe: 
    Die manuelle Eingabe von Check-Punkt-Werten bei 
    Planungs- und Navigationsflugergebnissen kann nun mit
    'Speichern und nächster Punkt' beendet werden, um sofort 
    die Eingabe des nächsten Check-Punkt-Wertes vorzunehmen.
- Verbesserte Tastatur-Bedienung:
    Bei der Eingabe von Werten bewirkt das Drücken der Tab-Taste
    die Fokussierung auf das erste interessante Text-Eingabeelement. 
    Die weiteren Text-Elemente können durch erneutes Drücken der Tab-Taste 
    erreicht werden. 
    Wird ein Schalter erreicht, kann dieser durch die Enter-Taste betätigt werden. 
    Mit Shift-Tab kann zurück fokussiert werden.
- Strecken-Kopie im Wettbewerb möglich.
- Einstellung einer von der Bediensprache unabhängigen Drucksprache möglich.
- Das Löschen auszuwählender Besatzungen in der Besatzungsliste ist möglich.
  Damit können alle oder einige Besatzungen in einem Schritt gelöscht werden.
- Planung: 'Bis Ende auswählen' (von letzter Markierung an) möglich.
- Planung: Der Ausdruck von Aufgaben und Flugplänen erfolgt jetzt für die 
  zuvor markierten Besatzungen. Damit können nun Aufgaben und Flugpläne
  unmittelbar vor deren Übergabe an die Besatzung gedruckt werden.
  Dadurch sind Anpassungen des Flugwindes im laufenden Wettbewerb möglich.
- Navigationsflug-Auswertung: Sind keine AFLOS-Daten vorhanden, können
  mit 'Keine Daten' alle Tore als 'nicht getroffen' und alle Wendeschleifen
  als 'nicht geflogen' markiert werden.
- Ausgabe von Warnungen '!' bei fehlerhaften Strecken-Karten-Messwerten:
  * abweichender Kurs zwischen unbekannten Zeitkontrollen und Wendepunkten
  * Entfernungsmessung kleiner als bei vorangegangener unbekannter Zeitkontrolle
- 2. optionale Namensspalte beim Import von Excel-Besatzungslisten
- Menüpunkt 'Demo-Wettbewerb' im Wettbewerbs-Menü:
    Ermöglicht das Anlegen verschiedener Demo-Wettbewerbe.
    Weitere Hinweise zum Anlegen eines Demo-Wettbewerbs siehe unten.

Erweiterungen in Version 0.6:
-----------------------------
- Wettbewerbsverwaltung
  * Neue Funktion 'Wettbewerb kopieren'
    Erlaubt es, von einem bestehenden Wettbewerb 
    Wettbewerbs-Einstellungen, Strecken, Besatzungen und/oder 
    Aufgaben-Einstellungen in einen neuen Wettbewerb zu kopieren.
    Damit kann eine Planung und Auswertung erneut begonnen werden
    (z.B. für unterschiedliche Wettbewerbsklassen).
- Streckenverwaltung
  * Streckenmesswerte sind jetzt vom letzten TP aus einzugeben.
  * Strecken-Druck erfolgt jetzt mit Karten-Messwerten. 
- Besatzungsverwaltung
  * Import von Excel-Besatzungslisten möglich.
    Verwenden Sie dabei immer folgende Vorlage:
    'Programme -> Flight Contest -> Samples -> FC-CrewList-Sample.xls'
  * Deaktivieren von Besatzungen möglich.
    Deaktivierte Besatzungen entfallen in Auswertungen und Ausdrucken.
- Aufgabeneinstellungen
  * Einstellbare Werte wurden nach Wichtigkeit sortiert.
  * Initialwert des Flugabstandes auf 3 Minuten geändert.
  * Auswertungsdetails einstellbar:
      Planungsergebnisse (Standard: ein) 
      Navigationsflugergebnisse (Standard: ein) 
      Beobachtungsergebnisse (Standard: ein)
      Landungsergebnisse (Standard: ein)
      Andere Ergebnisse (Standard: aus)
- Wettbewerbsplanung
  * Automatische Flugplan-Neuberechnung bei Flugwindänderung.
  * Zeitplan-Berechnung erfolgt nur noch für Besatzungen, 
    deren Zeitplan als 'Nicht berechnet' angezeigt wird.
    Zeitpläne werden beim Ändern der Reihenfolge als 'Nicht berechnet'
    markiert. 
  * Streckenberechnungen werden jetzt exakt wie bei PrecisWin gerundet.  
  * Zeit- und Flugpläne werden mit Ausgabe-Nummer gedruckt.
    Alle Zeitplan- oder Flugplan-Neuberechnungen erhöhen die Ausgabenummer
    beim nächsten Druck des Zeitplanes oder aller Fluppläne um 1.
  * Besatzungen können ans Ende verschoben werden.
    Beim Verschieben nach unten oder ans Ende werden nur die Planzeiten
    der Besatzungen zurückgesetzt, die angehakt sind.
    Beim Verschieben nach oben werden die Planzeiten der angehakten
    Besatzungen und die nach unten rückende Besatzung zurückgesetzt.
  * Druck eines Schiedsrichter-Zeitplanes mit mehr Details möglich.
  * Check-Punkt-Abkürzungen berücksichtigen nun die aktive Anzeige-Sprache. 
    Entsprechend der Wettbewerbsordnung Navigationsflug werden 
    Wendepunkte nun im Deutschen mit 'WP' abgekürzt (vorher 'TP').
    Unbekannte Zeitkontrollen werden mit 'UZK' abgekürzt (vorher 'Secret').
- Wettbewerbsauswertung
  * Ergebnisse werden bei Planungsänderung automatisch zurückgesetzt.
  * Punkte-Eingabe für Beobachtungen, Landungen und Anderes ergänzt.
  * Ausdruck des Besatzungsergebnisses
  * Wettbewerbs-Gesamtauswertung
    (Summe mehrerer Aufgaben/Wettbewerbstage) möglich.
  * Deaktivierung der Zeitauswertung von Check-Punkten
    Nach einer Änderung werden die Strafpunkte neu berechnet und die
    Platzierung auf 'n/a' (= nicht berechnet) gesetzt.
  * Beim Import von AFLOS-Logger-Daten können nun auch zutreffende 
    AFLOS-Fehlerpunkte angezeigt werden (Knopf 'AFLOS-Logger-Fehler').
- Bedienoberfläche
  * Blättern-Modus für Planung und Auswertung mit frei einstellbarer 
    Teilnehmer-Anzahl (Extras -> Einstellungen).
  * Stellt folgende Informationen beim Start wieder her:
      Letzter Wettbewerb
      Bediensprache
      Drucksprache
      Zeilenzahl beim Blättern
    Dazu werden werden auf dem Bedien-Computer Cookies abgespeichert.

Erweiterungen in Version 0.5:
-----------------------------
- Übernahme von Überflug-Daten aus AFLOS
- Neue Hauptmenüsortierung
- Ergebniseingabe bei Planungs- und Navigationstest wurde verbessert:
  * Ein Haken wird angezeigt, wenn ein Wert eingegeben wurde.
  * Die Werteeingabe kann zurückgesetzt werden.
  * Es werden Sollwerte bei der Istwerteingabe mit angezeigt.
  * Bei Zeiten ist auch die Eingabe hh.mm.ss erlaubt.
- Anzeige des Besatzungsergebnisses

Erweiterungen in Version 0.4:
-----------------------------
- Übernahme von Strecken aus AFLOS
- Unterstützung mehrerer Wettbewerbe
- Deutsche und englische Bediensprache
- Erweiterungen der Wettbewerbsverwaltung

Erweiterungen in Version 0.3:
-----------------------------
- HTML-Layout basierend auf Fluid 960 Grid System

Erweiterungen in Version 0.2:
-----------------------------
- 'Flight Contest' als Apache Tomcat Server-Anwendung

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


Mögliche Betriebssysteme der Server-Installation:
------------------------------------------------
Alle Windows-Versionen (ab Windows XP)


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
Mit Firefox oder Internet Explorer auf dem Server:
  'Programme -> Flight Contest -> Flight Contest'

Mit Firefox oder Internet Explorer 
auf einem beliebigen Rechner im Netzwerk aufrufen:
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
