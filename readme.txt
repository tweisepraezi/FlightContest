Flight Contest

Programm zur Planung und Auswertung von Navigationsflug-Wettbewerben
Program for planning and valuating of Air Rally and Precision Flying 

Thomas Weise
Deutscher Präzisionsflug-Verein e.V.
tweise.praeziflug@gmx.de
15.06.2012

Diese Hinweise finden Sie nach der Installation im Startmenü unter
'Programme -> Flight Contest -> Readme'.

Vorbereitungs- und Nutzungshinweise finden Sie im Startmenü unter
'Programme -> Flight Contest -> Usage'.

Standard-Wettbewerbsablauf siehe unten.


Erweiterungen in dieser Version (1.2.0):
-----------------------------------------
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
  * $ignore - Check-Punkt nicht importieren,
    zur Markierung von Hilfspunkten, die nicht ausgewertet werden sollen,
    z.B. für krumme Strecken
  * $dist:26,5mm - Entfernung vom vorangegangenen Wende-Punkt in mm
    (für eine Karte mit dem Wettbewerbs-Maßstab 1:200000)
  * $dist:12,3NM - Entfernung vom vorangegangenen Wende-Punkt in NM
  * $dist:22,4km - Entfernung vom vorangegangenen Wende-Punkt in km
  * $track:142 - Richtung vom vorangegangenen Wende-Punkt in Grad
  Der vorangegangene Wende-Punkt ist ein Check-Punkt, der importiert wird
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
- Wettkampfreglement Präzisionsflug-Schweizermeisterschaft - Ausgabe 2009
  hinzugefügt
- Programm-Menü erweitert
  * Programme -> Flight Contest -> Usage:
      Vorbereitungs- und Nutzungshinweise
  * Programme -> Flight Contest -> Rules:
      alle unterstützten Wettbewerbsregeln
- Beschreibung
  "Wiederherstellung eines fehlgeschlagenen 'Flight Contest'-Updates"
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
- Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2012
  Gegenüber der Ausgabe 2008 gab es keine Veränderungen in den 
  Punkten.
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
  * In 'Auswertung' ist das Team-Ergebnis abrufbar.
  * Mit 'Team-Ergebnis-Einstellungen' können verschiedene 
    Auswertungs-Details eingestellt werden.
- Erweiterte Wettbewerbs-Auswertung:
  * In 'Auswertung' ist das Wettbewerbs-Ergebnis abrufbar.
  * Mit 'Wettbewerbs-Ergebnis-Einstellungen' können verschiedene 
    Auswertungs-Details eingestellt werden.
- Erweiterte Landungseingabe:
  * Es können bis zu 4 Landungen aktiviert werden.
  * Wird min. eine Landung aktiviert, wird die Eingabe der Gesamtstrafpunkte 
    aller Landungen durch eine Landemesswert-Eingabe und zusätzliche 
    Lande-Fehlerschalter je Landung ersetzt.
  * Die Strafpunkte einer Landung werden aus dem Landemesswert mit einer 
    konfigurierbaren Formel (siehe Landefeld-Punkt-Berechnung)
    und den Lande-Fehlerschaltern mit konfigurierbarer Strafpunktzahl berechnet.
- Auswahl der Wettbewerbsordnung
  * Die Wettbewerbsordnung wird beim Anlegen eines Wettbewerbs bzw.
    einer Klasse festgelegt. 
  * Es stehen folgende Wettbewerbsordnungen zur Auswahl:
      Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2012
      Wettbewerbsordnung Präzisionsflug Deutschland - Ausgabe 2005
      FAI Air Rally Flying - Edition 2011
      FAI Precision Flying - Edition 2011
      Wettkampfreglement Präzisionsflug-Schweizermeisterschaft - Ausgabe 2009
  * Einzelne Werte können in "Punkte" geändert werden.
  * Die Wettbewerbsordnung kann je Wettbewerb bzw. je Klasse geändert werden.
    Eine Neuberechnung vorhandener Ergebnisse findet aber erst mit Aufruf von
    "Auswertungen neu berechnen" (in "Punkte" zu finden) statt.
- Hochladen von AFLOS-Datenbanken:
    Es ist nun möglich, AFLOS auf einem anderen PC im Netzwerk zu betreiben.
    Nach dem Lesen von Logger-Daten ist dazu die AFLOS-Datenbank über
    'AFLOS -> Datenbank hochladen' auf den 'Flight Contest'-Server hochzuladen.
    Details siehe unten (Interaktion mit AFLOS)
- Der Begriff "Mannschaft" wurde durch "Besatzung" ersetzt.
- Der Begriff "Protestprotokoll" wurde durch "Besatzungsergebnis" ersetzt. 
- Beim Druck eines oder aller Besatzungsergebnisse können zu druckende Details
  (Flugplanung, Navigationsflug, Beobachtungen, Landung, andere Ergebnisse)
  festgelegt werden.
- Vereinfachte Strecken-Eingabe:
    Die Eingabe von Strecken-Karten-Messwerten kann 
    nun mit "Speichern und nächster Punkt" beendet werden, um sofort 
    die Eingabe des nächsten Karten-Messwertes vorzunehmen.
- Vereinfachte Auswertungs-Eingabe: 
    Die manuelle Eingabe von Checkpunkt-Werten bei 
    Flugplanungs- und Navigationsflugergebnissen kann nun mit
    "Speichern und nächster Punkt" beendet werden, um sofort 
    die Eingabe des nächsten Checkpunkt-Wertes vorzunehmen.
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
- Planung: "Bis Ende auswählen" (von letzter Markierung an) möglich.
- Planung: Der Ausdruck von Aufgaben und Flugplänen erfolgt jetzt für die 
  zuvor markierten Besatzungen. Damit können nun Aufgaben und Flugpläne
  unmittelbar vor deren Übergabe an die Besatzung gedruckt werden.
  Dadurch sind Anpassungen des Flugwindes im laufenden Wettbewerb möglich.
- Navigationsflug-Auswertung: Sind keine AFLOS-Daten vorhanden, können
  mit "Keine Daten" alle Tore als "nicht getroffen" und alle Wendeschleifen
  als "nicht geflogen" markiert werden.
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
  * Initialwerte der Punkte an Wettbewerbsordnung Navigationsflug 2008
    angepasst.
  * Neue Funktion "Wettbewerb kopieren"
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
      Flugplanungsergebnisse (Standard: ein) 
      Navigationsflugergebnisse (Standard: ein) 
      Beobachtungsergebnisse (Standard: ein)
      Landungsergebnisse (Standard: ein)
      Andere Ergebnisse (Standard: aus)
- Wettbewerbsplanung
  * Automatische Flugplan-Neuberechnung bei Flugwindänderung.
  * Zeitplan-Berechnung erfolgt nur noch für Besatzungen, 
    deren Zeitplan als "Nicht berechnet" angezeigt wird.
    Zeitpläne werden beim Ändern der Reihenfolge als "Nicht berechnet"
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
  * Checkpunkt-Abkürzungen berücksichtigen nun die aktive Anzeige-Sprache. 
    Entsprechend der Wettbewerbsordnung Navigationsflug 2008 werden 
    Wendepunkte nun im Deutschen mit 'WP' abgekürzt (vorher 'TP').
    Unbekannte Zeitkontrollen werden mit 'UZK' abgekürzt (vorher 'Secret').
- Wettbewerbsauswertung
  * Ergebnisse werden bei Planungsänderung automatisch zurückgesetzt.
  * Punkte-Eingabe für Beobachtungen, Landungen und Anderes ergänzt.
  * Ausdruck des Besatzungsergebnisses
  * Wettbewerbs-Gesamtauswertung
    (Summe mehrerer Aufgaben/Wettbewerbstage) möglich.
  * Deaktivierung von Checkpunkten möglich (Deaktivierte Checkpunkte).
    Nach einer Änderung werden die Strafpunkte neu berechnet und die
    Platzierung auf 'n/a' (= nicht berechnet) gesetzt.
  * Beim Import von AFLOS-Logger-Daten können nun auch zutreffende 
    AFLOS-Fehlerpunkte angezeigt werden (Knopf "AFLOS-Logger-Fehler").
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
- Ergebniseingabe bei Flugplanungs- und Navigationstest wurde verbessert:
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
'Internet Explorer' ebenfalls möglich.


Interaktion mit AFLOS:
---------------------
Minimale empfohlene AFLOS Version: 2.10

'Flight Contest' kann AFLOS-Daten von 2 Quellen lesen:
1. direkt von 'AFLOS', wenn 'AFLOS' und 'Flight Contest' 
   auf dem selben PC installiert sind oder
2. aus hochgeladenen AFLOS-Datenbanken.

Fall 2 ist zu benutzen, wenn ein 'Flight Contest'-Server verwendet wird, 
auf dem kein 'AFLOS' vorhanden oder benutzt wird.

Das Hochladen von Datenbanken erfolgt mit 'AFLOS -> Datenbank hochladen'.
Dadurch wird beim aktiven Wettbewerb die Einstellung
"Hochgeladene AFLOS-Datenbank benutzen" aktiviert.

Installationshinweise, wenn 'AFLOS' und 'Flight Contest' auf dem selben
PC installiert sind und dort auch benutzt werden sollen:
Wird AFLOS nicht in dem Verzeichnis C:\AFLOS installiert,
führen Sie nach der ersten Benutzung von AFLOS den Punkt
'Programme -> Flight Contest -> Install AFLOS connection' aus
(in Windows Vista oder Windows 7 als Administrator).
Kann trotzdem nicht direkt auf AFLOS-Daten zugegriffen werden,
kann immer Fall 2 (Hochladen der AFLOS-Datenbank) angewendet werden.

'Flight Contest' importiert Strecken und Logger-Messwerte entweder direkt
aus der zuletzt oder gerade geöffneten AFLOS-Datenbank (Fall 1)
oder aus der zuletzt hochgeladenen AFLOS-Datenbank (Fall 2).

Import von Strecken (Strecken -> Import AFLOS-Strecke):
Stellen Sie zum Import sicher, dass Check-Punkte unbekannter Zeitkontrollen 
in AFLOS eine Torbreite von 2NM oder die Markierung $secret haben.

Import von Logger-Messwerten (Auswertung -> Navigationsflug -> Import AFLOS-Logger-Daten):
Stellen Sie zum Import sicher, dass die aus dem Logger eingelesenen Messwerte
für eine konkrete Besatzung (= Comp.-Nr.) und Strecke (= Ref.-Nr.) und der 
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
  Für eine AFLOS-Besatzung nicht vorhandener Eintrag
    (-> Import-Fehler "AFLOS-Logger-Daten von ... nicht komplett.")


Excel-Besatzungslisten
----------------------
Sie können Besatzungslisten mit Excel erstellen und in 'Flight Contest'
importieren.

Verwenden Sie dazu folgende Vorlage, deren Aufbau einen erfolgreichen
Import sicherstellt: 
'Programme -> Flight Contest -> Samples -> FC-CrewList-Sample.xls'

Importiert werden nur Besatzungsnamen, die noch nicht vorhanden sind.


Standard-Wettbewerbsablauf
--------------------------

A. Wettbewerbs-Vorbereitung

1. Strecken festlegen und in AFLOS eingeben
   - Bei Päzisionsflug-Wettbewerben Karten-Entfernungs-Messwerte 
     mit $dist:...mm im Feld 'Mark' eintragen.
2. Besatzunglisten in Excel-Tabelle eingeben
   - Vorlage verwenden:
     Programme -> Flight Contest -> Samples -> FC-CrewList-Sample.xls
   - Bei Wettbewerb mit Klassen-Auswertung Klassen-Name zuordnen.
   - Bei Wettbewerb mit Team-Auswertung Team-Name zuordnen.
   - Reihenfolge festlegen und Startnummern zuordnen.

B. Wettbewerbs-Beginn
   
3. Neuer Wettbewerb
   - Wettbewerbsname eintragen.
   - Ggf. Klassen-Verwaltung aktivieren.
   - Wettbewerbsordnung, Kartenmaßstab und Zeitzone einstellen.
   - Anzahl der zu wertenden Teams bei Team-Auswertung einstellen.
   - Ggf. Drucksprache umstellen (-> Extras -> Einstellungen).
   - Auswertungs-Einstellungen des Wettbewerbs anpassen
     (-> 'Wettbewerb -> Punkte' oder 'Klassen -> <Name> -> Punkte'):
     * Ggf. Punkte für nicht geflogene Wendescheifen mit 0 einstellen
     * Ggf. Punkte für Kursabweichungen > 90 Grad mit 0 einstellen
     * Ggf. Punkte für Unterschreitung der Mindestflughöhe mit 0 einstellen
4. Strecken -> Import AFLOS-Strecke
   - Vorbereitete Strecken aus AFLOS importieren.
5. Besatzungen -> Import Excel-Besatzungsliste
   - Vorbereitete Excel-Tabelle (FC-CrewList-Sample.xls) importieren.
   - Nicht eingetroffene Teilnehmer deaktivieren.
   
C. Wettbewerbs-Durchführung

6. Neue Aufgabe
   - Zeit-Einstellungen festlegen
     * TakeOff-Abstand
     * Zeit T/O -> SP
     * Vorhandene Wendeschleifen mit 1min fliegen (ja/nein)
     * Zeit FP -> Landung
     * Zeit FP -> Abstellen des Flugzeuges
   - Auswertungsdetails festlegen
     * Flugplanungstest (ja/nein)
     * Navigationstest (ja/nein)
     * Beobachtungstest (ja/nein)
     * Landetest (ja/nein, Anzahl der Landungen festlegen)
   - Flugplannungstest hinzufügen: Dabei eine erste Flugplannungsaufgabe
     mit Strecke und Wind angeben. 
   - Navigationstest hinzufügen: Dabei Strecke und Wind angeben. 
7. Planung
   - Allen Besatzungen eine Flugplannungsaufgabe zuweisen.
   - Ggf. Reihenfolge korrigieren.
   - Allen Besatzungen einen Flugwind zuweisen.
   - Zeitplan berechnen.
   - Ggf. einzelne Zeiten verschieben.
   - Aufgaben drucken.
   - Zeitplan drucken.
   - Schiedsrichter-Zeitplan drucken.
   - Flugpläne drucken.
8. Tagesablauf
   - Flugplanungstest durchführen.
   - Logger-Daten in AFLOS einlesen.
   - Beobachtungsergebnisse abfragen.
   - Landungen durchführen und vermessen.
9. Auswertung des Tagesablaufes
   - Flugplanungsergebnisse eintragen.
   - Navigationsflug-Ergebnisse aus AFLOS importieren.
     * Ggf. Wendepunkte für Zeitauswertung deaktivieren
   - Beobachtungsergebnisse eintragen.
   - Landungsergebnisse eintragen.
   - Besatzungsergebnisse drucken und Besatzungen übergeben.
   - Ergebnisse auf Grund stattgegebener Proteste korrigieren. 
   - Plätze berechnen.
   - Aufgaben-Ergebnis des Tages drucken.
10. Wettbewerbs- oder Klassen-Ergebnisse
   - Plätze berechnen.
   - Wettbewerbs- oder Klassen-Ergebnis drucken.
   - Team-Ergebnisse drucken

   
Formeln der Strafpunkt-Berechnung von Landungen
-----------------------------------------------
Berechungsformel für 'Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2012' für alle Landungen:
{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;default:return 200;}}

Berechungsformel für 'Wettbewerbsordnung Präzisionsflug Deutschland - Ausgabe 2005' für alle Landungen:
für Landung 1 und 4:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(7*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 175;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return 200;}}}
für Landung 2 und 3:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 100;case 'D':return 50;case 'E':return 60;case 'F':return 70;case 'G':return 80;case 'H':return 90;default:return 150;}}}

Berechungsformel für 'FAI Air Rally Flying - Edition 2011' für alle Landungen:
{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return 200;}}

Berechungsformel für 'FAI Precision Flying - Edition 2011':
für Landung 1 und 4:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 6*i}else{return -(14*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 350;case 'D':return 150;case 'E':return 180;case 'F':return 210;case 'G':return 240;case 'H':return 270;default:return 300;}}}
für Landung 2 und 3:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 4*i}else{return -(8*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 200;case 'D':return 100;case 'E':return 120;case 'F':return 140;case 'G':return 160;case 'H':return 180;default:return 200;}}}

Berechungsformel für 'Wettkampfreglement Präzisionsflug-Schweizermeisterschaft - Ausgabe 2009' für alle Landungen:
für Landung 1 und 4:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(7*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 175;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return 200;}}}
für Landung 2 und 3:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 100;case 'D':return 50;case 'E':return 60;case 'F':return 70;case 'G':return 80;case 'H':return 90;default:return 150;}}}


Mögliche Betriebssysteme der Server-Installation:
------------------------------------------------
Windows XP ServicePack 2
Windows Vista ServicePack 1
Windows 7 


Server starten:
---------------
Starten des Dienstemanagers 'Apache Tomcat FlightContest':
  'Programme -> Flight Contest -> Flight Contest Service Manager'
(in Windows Vista oder Windows 7 werden Administrator-Privilegien
 automatisch angefordert)

Auf Icon des Dienstemanagers 'Apache Tomcat FlightContest' ausführen:
  Kontextmenüpunkt 'Start Service' aufrufen


Server stoppen:
---------------
Auf Icon des Dienstemanagers 'Apache Tomcat FlightContest' ausführen:
  Kontextmenüpunkt 'Stop Service'
  
  
Behebung von Server-Ausführungsproblemen:
-----------------------------------------
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
 
    
Datensicherung:
---------------
Die Datenbank wird an folgendem Ort gespeichert:
  <Installationsverzeichnis>\fc
  (C:\Program Files\Flight Contest\fc)
  
Datenbankdatei: 
  fcdb.h2.db

Vorgehen zur Datensicherung:
  1. Server stoppen
  2. <Installationsverzeichnis>\fc\fcdb.h2.db
     an einen sicheren Ort kopieren
    
Vorgehen zur Datenwiederherstellung:
  1. Server stoppen
  2. Gesicherte fcdb.h2.db nach 
     <Installationsverzeichnis>\fc kopieren
  3. Server starten

Hinweis:
  <Installationsverzeichnis>\fc\AFLOS-UPLOAD.mdb
  darf nicht gelöscht werden.


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
  

Logs:
-----
  Sind hier zu finden:
    <Installationsverzeichnis>\fc\logs


Cookies:
--------
  Speichert folgende Cookies für 1 Jahr:
    LastContestID    - Letzter Wettbewerb 
    ShowLanguage     - Bediensprache
    PrintLanguage    - Drucksprache
    ShowLimitCrewNum - Zeilenzahl beim Blättern

