Flight Contest

Programm zur Planung und Auswertung von Navigationsflug-Wettbewerben
Program for planning and valuating of Air Rally and Precision Flying 

Thomas Weise
Deutscher Pr�zisionsflug-Verein e.V.
10.03.2013

Downloads:
https://www.dropbox.com/sh/7iiyj608t3icgab/L3GRISYuvM

Diese Hinweise finden Sie nach der Installation im Startmen� unter
'Programme -> Flight Contest -> Readme'.

Vorbereitungs- und Nutzungshinweise finden Sie im Startmen� unter
'Programme -> Flight Contest -> Usage'.

Bedientipps und Standard-Wettbewerbsablauf finden Sie im Startmen� unter
'Programme -> Flight Contest -> Tips'


Flight Contest ist eine moderne, auf Java-Techniken basierende, mehrsprachige
und netzwerkf�hige Open-Source-Datenbank-Anwendung:
* Die Benutzer-Oberfl�che l�uft im Web-Browser (Firefox, Internet Explorer u.a.). 
* Sie kann auf mehreren vernetzten Computern gleichzeitig benutzt werden. 
* Jeder Benutzer kann dabei seine bevorzugte Bediensprache verwenden
  (Deutsch, Englisch, erweiterbar).
 
Flight Contest hat gegen�ber bisherigen Programmen ein flexibles
Wettbewerbsmanagement. Dazu z�hlen die Unterst�tzung
* unterschiedlicher Wettbewerbsordnungen 
  (FAI, Deutschland, Schweiz, erweiterbar),
* von Wettbewerbsklassen 
  (unterschiedliche Auswertung von Teilnehmergruppen in einem Wettbewerb),
* paralleler Wettbewerbe 
  (gleichzeitig stattfindende Wettbewerbe mit unabh�ngigen Startfeldern),
* kombinierter Wettbewerbe 
  (gleichzeitig stattfindende Wettbewerbe mit einem gemeinsamen Startfeld).
 
Weitere n�tzliche Funktionen von Flight Contest:
* Sehr schnelle Logger-Daten-Nutzung des FAI-zertifizierten Loggers AFLOS 
  (ohne Umweg �ber Datei-Export und -Import).
* PDF-Erzeugung aller Formulare f�r Druck und Internet-Ver�ffentlichung.


Fehlerbehebungen in dieser Version (1.2.4):
-------------------------------------------
- TrueTrack-Rundungs-Problem bei Leg-Berechnung beseitigt.
- Ausgabe der GroundSpeed im Flugplan mit 1 Nachkommastelle.
- 180-Grad-Eingabeproblem bei Planungstest-Auswertungs-Eingabe beseitigt.
- Takeoff-Abstands-Warnung: Warnung wird nur bei Unterschreitung des
  definierten Standard-Takoff-Abstandes ausgegeben, 
  auch bei schnellerem Folgeflugzeug.
- Probleme der nachtr�glichen Aktivierung der Klassenverwaltung 
  eines Wettbewerbes, bei dem schon Aufgaben geplant wurden, behoben.
- Sortierreihenfolge beim L�schen einer Besatzung bleibt nun erhalten.
- Besatzungsergebnis-Druck: Kopfzeile um Startnummer und Besatzungsname erg�nzt.
- Installation der korrekten Fassung der 
  'Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2012'.
- Fehlfunktion beim 'Wettbewerb kopieren' beseitigt.
- Beispiel eines Beobachtungsprotokolls hinzugef�gt:
  'Programme -> Flight Contest -> Samples -> FC-ObservationLog-Sample.xls'

Fehlerbehebungen und Erweiterungen in Version 1.2.3:
----------------------------------------------------
- Aktualisierung der FAI Regelwerke
      FAI Air Rally Flying - Edition 2012
      FAI Precision Flying - Edition 2012
- Anzeige der Flugzeug-Doppelnutzung in Zeitplan und Besatzungsliste

Fehlerbehebungen und Erweiterungen in Version 1.2.2:
----------------------------------------------------
- Flugplanungsaufgabe: Fehlender Ausdruck des Windes wurde erg�nzt.
- Zeitplan berechnen: Die Berechnung nicht berechneter Mannschaften
  orientiert sich jetzt an der Anfangszeit der vorherigen Besatzung.
- Planung: Das Verschieben der Besatzungs-Reihenfolge nach oben 
  l�scht jetzt nur noch die Zeiten der verschobenen Besatzung(en) 
  und damit nicht mehr die Zeit der dar�berliegenden Besatzung.
- TAS-�nderung: Das �ndern der TAS einer Besatzung �ndert erst einmal 
  nicht die TAS, die in einer Aufgabe f�r Flugplanung und Navigationsflug
  benutzt wurde. Dadurch ist es m�glich, dass eine Besatzung von Aufgabe 
  zu Aufgabe jeweils eine andere TAS verwenden kann. 
  Derartige Abweichungen werden in der Planung jetzt mit einem ! markiert.
  Die TAS der Aufgabe wird allerdings an die der Besatzungs-Festlegung
  angepasst, wenn die Reihenfolge innerhalb einer Aufgabe f�r diese
  Besatzung ge�ndert wird.
- Planung: Warnungen bei Nichteinhaltung der Takeoff-Zeit-Reihenfolge
  eingef�hrt. Diese ber�cksichtigt auch den anderen Takeoff-Abstand
  bei einem schnelleren Folgeflugzeug.  
- Planung: Beim Deaktivieren einer Besatzung werden Warnungen
  folgender Besatzungen nicht automatisch gel�scht. Die Neuberechnung
  von Warnungen erfolgt mit 'Zeitplan berechnen'.

Erweiterungen in Version 1.2.1:
-------------------------------
- Streckenaudruck verbessert
  * Der Koordinaten-Tabelle wurden Spalten f�r AFLOS-CheckPunkte, H�he und
    Torbreite hinzugef�gt.
  * Den Auswerte- und Test-Etappen-Tabellen wurde die Gesamtentfernung
    und die Anzeige von Kurs�nderungen >= 90 Grad hinzugef�gt.
- Hinweis zur Verwendung der AFLOS-Koordinaten-Markierung $ignore:
  Ein dertiger Punkt wird von 'Flight Contest' beim Import vollst�ndig
  ignoriert, d.h., es werden keine Daten dieses Punktes verarbeitet.
  Bei Verwendung als Hilfspunkt f�r krumme Strecken ist deshalb dem
  ersten folgenden Wendepunkt die Entfernung und Richtung mitzuteilen, 
  die f�r den krummen Streckenabschnitt verwendet werden soll.
  Daf�r sind zweckm�ssigerweise folgende Werte anzugeben:
    $dist  - Summe der Entfernung der vorangegangen $ignore-Punkte
    $track - Richtung des ersten $ignore-Punktes
  F�r diesen Einsatzfall wird in einer zuk�nftigen 'Flight Contest'-
  Version eine eigene Markierungsm�glichkeit eingef�hrt.
- Tipps 'Programme -> Flight Contest -> Tips' hinzugef�gt.
  Darin ist jetzt der Standard-Wettbewerbsablauf zu finden.

Erweiterungen in Version 1.2.0:
-------------------------------
- Einf�hrung einer Start-Nummer f�r Besatzungen
  Diese �ndert sich nicht, wenn die Reihenfolge der Besatzungen
  bei einer Aufgabe ge�ndert wird.
  Diese Nummer entspricht der AFLOS-Competitor-Nummer.
  FC-CrewList-Sample.xls wurde entspr. erweitert.
- Bei Neuanlage einer Aufgabe werden jetzt alle Einstellungen
  im Eingabe-Formular angezeigt.
- Automatische Neuberechnung des Zeitplanes bei �nderungen bestimmter 
  Aufgaben-Einstellungen hinzugef�gt.
  Die Auswirkung einer Einstellungs�nderung wird im Eingabe-Formular erkl�rt.
- Soll eine Wendeschleife (Kurs�nderung am Wendepunkt >= 90 Grad) nicht
  geflogen werden, ist bei der Flugzeit der Wendeschleife 0 min anzugeben.
  Der Flugplan enth�lt dann keine Aufforderung zur Wendeschleife.
  Kurs�nderungen >= 90 Grad werden in der Koordinaten-Liste einer Strecke
  angezeigt (= Kurs�nderung nach Erreichen des vorangegangen Wendepunktes).
- Ausdruck der Navigationsflugergebnisse verbessert:
  * Nicht getroffene Wendepunkte drucken beim Messwert das Zeichen '-'.
  * Deaktivierte Wendepunkte drucken bei den Punkten das Zeichen '-'.
  * Wendeschleifen-Fehler werden bei deaktivierten Wendepunkten
    nicht bestraft und drucken bei den Punkten das Zeichen '-'.
  * Werden Auswerte-Punkte f�r Wendeschleifen-, Kursabweichungs- 
    oder H�hen-Fehler mit 0 festgelegt, werden Spalten f�r die Punkte
    dieser Werte beim Ausdruck weggelassen.
- AFLOS-Strecken-Import erweitert
  Check-Punkte in den AFLOS-Referenz-Daten k�nnen im Feld 'Mark' 
  mit folgenden Markierungen versehen werden, die beim Import
  der Strecke verarbeitet werden:
  * $secret - Check-Punkt als unbekannte Zeitkontrolle importieren,
    f�r unbekannte Zeitkontrollen mit von 2NM abweichender Torbreite
  * $ignore - Check-Punkt nicht importieren
  * $dist:26,5mm - Entfernung vom vorangegangenen Wende-Punkt in mm
    (f�r eine Karte mit dem Wettbewerbs-Ma�stab 1:200000)
  * $dist:12,3NM - Entfernung vom vorangegangenen Wende-Punkt in NM
  * $dist:22,4km - Entfernung vom vorangegangenen Wende-Punkt in km
  * $track:142 - Richtung vom vorangegangenen Wende-Punkt in Grad
  Der vorangegangene Wende-Punkt ist ein Check-Punkt, der importiert wird
  und keine unbekannte Zeitkontrolle ist.
  Beim Import k�nnen folgende Erkennungs-Verfahren f�r unbekannte
  Zeitkontrollen gew�hlt werden:
  * Keine Erkennung
  * Check-Punkte mit Torbreite 2NM
  * Check-Punkte mit Markierung $secret
  * Check-Punkte mit Torbreite 2NM oder Markierung $secret (Default)
- Standard-Wettbewerbsablauf �berarbeitet
  * Neue Einteilung in 3 Wettbewerbsphasen
    A. Wettbewerbs-Vorbereitung
    B. Wettbewerbs-Beginn
    C. Wettbewerbs-Durchf�hrung
  * Anpassungen bei Strecken-Vorbereitung
  * Anpassungen bei Aufgaben-Einstellungen
- Wettkampfreglement Pr�zisionsflug-Schweizermeisterschaft - Ausgabe 2009
  hinzugef�gt
- Programm-Men� erweitert
  * Programme -> Flight Contest -> Usage:
      Vorbereitungs- und Nutzungshinweise
  * Programme -> Flight Contest -> Rules:
      alle unterst�tzten Wettbewerbsregeln
- Beschreibung
  "Wiederherstellung eines fehlgeschlagenen 'Flight Contest'-Updates"
  hinzugef�gt, siehe unten

Fehlerbehebung in Version 1.1.1:
--------------------------------
- Beim Anlegen von Klassen beim Import von Besatzungen aus Excel-Vorlage
  wurden die Auswertungs-Punkte der Klassen nicht korrekt eingestellt.
  Das Problem besteht nicht, wenn die Klassen vor dem Import manuell
  angelegt wurden. 
  Von der gew�hlten Wettbewerbsordnung abweichende Punkte werden in
  der Punkte-Anzeige mit einem ! markiert.
  Bei bestehenden Wettbewerben kann eine Punkteanweichung durch Wechsel
  zu einer anderen Wettbewerbsordnung, Speichern, Wechsel zur�ck zur
  gew�nschten Wettbewerbsordnung und Speichern repariert werden.
- Beim Import von Besatzungen aus Excel-Vorlage werden zwischen
  Pilot und Copilot ein , eingef�gt.

Erweiterungen in Version 1.1.0:
-------------------------------
- Unterst�tzung kombinierter Wettbewerbe
  * Wettbewerbs-Auswertung auch f�r klassenbasierte Wettbewerbe,
    wo nach Klassen gefiltert werden kann, eingef�hrt.
  * In der Wettbewerbs-Auswertung kann zus�tzlich nach Aufgaben
    gefiltert werden.
  * Teilen Sie die Teilnehmer derart in Klassen auf,
    dass je Wettbewerb entsprechende Aufgaben und Klassen als
    Filter anwendbar sind. Sie ben�tigen jeweils eine Klasse f�r
    - Teilnehmer, die nur an einem Wettbewerb teilnehmen
    - Teilnehmer, die an mehreren Wettbewerben teilnehmen.
- Zeitplan-Berechnung auch bei unvollst�ndiger Planung
  (kein Flugwind zugewiesen) m�glich.

Fehlerbehebungen in Version 1.0.1:
----------------------------------
- Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2012
  Gegen�ber der Ausgabe 2008 gab es keine Ver�nderungen in den 
  Punkten.
- Beschreibung zur Behebung von Server-Ausf�hrungsproblemen 
  nach Update-Installation hinzugef�gt (siehe unten).

Erweiterungen in Version 1.0:
-----------------------------
- Klassenverwaltung:
  * Ein Wettbewerb kann auf Klassen-Auswertung umgestellt werden.
    Die Auswertungsdetails werden dann in der Aufgabe pro Klasse definiert.
  * Besatzungen k�nnen Klassen zugeordnet werden.
  * Je Klasse existiert eine separate Ergebnis-Auswertung, die �ber ihren
    Klassen-Namen in 'Auswertung' erreichbar ist.
  * Mit 'Klassen-Ergebnis-Einstellungen' k�nnen verschiedene 
    Auswertungs-Details eingestellt werden.
- Teamverwaltung: 
  * Ein Wettbewerb kann f�r eine Team-Auswertung konfiguriert werden.
    Dazu wird im Wettbewerb die Mindestanzahl vorhandener Team-Mitglieder
    zur Team-Auswertung festgelegt.
  * Besatzungen k�nnen Teams zugeordnet werden.
  * In 'Auswertung' ist das Team-Ergebnis abrufbar.
  * Mit 'Team-Ergebnis-Einstellungen' k�nnen verschiedene 
    Auswertungs-Details eingestellt werden.
- Erweiterte Wettbewerbs-Auswertung:
  * In 'Auswertung' ist das Wettbewerbs-Ergebnis abrufbar.
  * Mit 'Wettbewerbs-Ergebnis-Einstellungen' k�nnen verschiedene 
    Auswertungs-Details eingestellt werden.
- Erweiterte Landungseingabe:
  * Es k�nnen bis zu 4 Landungen aktiviert werden.
  * Wird min. eine Landung aktiviert, wird die Eingabe der Gesamtstrafpunkte 
    aller Landungen durch eine Landemesswert-Eingabe und zus�tzliche 
    Lande-Fehlerschalter je Landung ersetzt.
  * Die Strafpunkte einer Landung werden aus dem Landemesswert mit einer 
    konfigurierbaren Formel (siehe Landefeld-Punkt-Berechnung)
    und den Lande-Fehlerschaltern mit konfigurierbarer Strafpunktzahl berechnet.
- Auswahl der Wettbewerbsordnung
  * Die Wettbewerbsordnung wird beim Anlegen eines Wettbewerbs bzw.
    einer Klasse festgelegt. 
  * Es stehen folgende Wettbewerbsordnungen zur Auswahl:
      Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2012
      Wettbewerbsordnung Pr�zisionsflug Deutschland - Ausgabe 2005
      FAI Air Rally Flying - Edition 2012
      FAI Precision Flying - Edition 201
      Wettkampfreglement Pr�zisionsflug-Schweizermeisterschaft - Ausgabe 2009
  * Einzelne Werte k�nnen in "Punkte" ge�ndert werden.
  * Die Wettbewerbsordnung kann je Wettbewerb bzw. je Klasse ge�ndert werden.
    Eine Neuberechnung vorhandener Ergebnisse findet aber erst mit Aufruf von
    "Auswertungen neu berechnen" (in "Punkte" zu finden) statt.
- Hochladen von AFLOS-Datenbanken:
    Es ist nun m�glich, AFLOS auf einem anderen PC im Netzwerk zu betreiben.
    Nach dem Lesen von Logger-Daten ist dazu die AFLOS-Datenbank �ber
    'AFLOS -> Datenbank hochladen' auf den 'Flight Contest'-Server hochzuladen.
    Details siehe unten (Interaktion mit AFLOS)
- Der Begriff "Mannschaft" wurde durch "Besatzung" ersetzt.
- Der Begriff "Protestprotokoll" wurde durch "Besatzungsergebnis" ersetzt. 
- Beim Druck eines oder aller Besatzungsergebnisse k�nnen zu druckende Details
  (Flugplanung, Navigationsflug, Beobachtungen, Landung, andere Ergebnisse)
  festgelegt werden.
- Vereinfachte Strecken-Eingabe:
    Die Eingabe von Strecken-Karten-Messwerten kann 
    nun mit "Speichern und n�chster Punkt" beendet werden, um sofort 
    die Eingabe des n�chsten Karten-Messwertes vorzunehmen.
- Vereinfachte Auswertungs-Eingabe: 
    Die manuelle Eingabe von Checkpunkt-Werten bei 
    Flugplanungs- und Navigationsflugergebnissen kann nun mit
    "Speichern und n�chster Punkt" beendet werden, um sofort 
    die Eingabe des n�chsten Checkpunkt-Wertes vorzunehmen.
- Verbesserte Tastatur-Bedienung:
    Bei der Eingabe von Werten bewirkt das Dr�cken der Tab-Taste
    die Fokussierung auf das erste interessante Text-Eingabeelement. 
    Die weiteren Text-Elemente k�nnen durch erneutes Dr�cken der Tab-Taste 
    erreicht werden. 
    Wird ein Schalter erreicht, kann dieser durch die Enter-Taste bet�tigt werden. 
    Mit Shift-Tab kann zur�ck fokussiert werden.
- Strecken-Kopie im Wettbewerb m�glich.
- Einstellung einer von der Bediensprache unabh�ngigen Drucksprache m�glich.
- Das L�schen auszuw�hlender Besatzungen in der Besatzungsliste ist m�glich.
  Damit k�nnen alle oder einige Besatzungen in einem Schritt gel�scht werden.
- Planung: "Bis Ende ausw�hlen" (von letzter Markierung an) m�glich.
- Planung: Der Ausdruck von Aufgaben und Flugpl�nen erfolgt jetzt f�r die 
  zuvor markierten Besatzungen. Damit k�nnen nun Aufgaben und Flugpl�ne
  unmittelbar vor deren �bergabe an die Besatzung gedruckt werden.
  Dadurch sind Anpassungen des Flugwindes im laufenden Wettbewerb m�glich.
- Navigationsflug-Auswertung: Sind keine AFLOS-Daten vorhanden, k�nnen
  mit "Keine Daten" alle Tore als "nicht getroffen" und alle Wendeschleifen
  als "nicht geflogen" markiert werden.
- Ausgabe von Warnungen '!' bei fehlerhaften Strecken-Karten-Messwerten:
  * abweichender Kurs zwischen unbekannten Zeitkontrollen und Wendepunkten
  * Entfernungsmessung kleiner als bei vorangegangener unbekannter Zeitkontrolle
- 2. optionale Namensspalte beim Import von Excel-Besatzungslisten
- Men�punkt 'Demo-Wettbewerb' im Wettbewerbs-Men�:
    Erm�glicht das Anlegen verschiedener Demo-Wettbewerbe.
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
    (z.B. f�r unterschiedliche Wettbewerbsklassen).
- Streckenverwaltung
  * Streckenmesswerte sind jetzt vom letzten TP aus einzugeben.
  * Strecken-Druck erfolgt jetzt mit Karten-Messwerten. 
- Besatzungsverwaltung
  * Import von Excel-Besatzungslisten m�glich.
    Verwenden Sie dabei immer folgende Vorlage:
    'Programme -> Flight Contest -> Samples -> FC-CrewList-Sample.xls'
  * Deaktivieren von Besatzungen m�glich.
    Deaktivierte Besatzungen entfallen in Auswertungen und Ausdrucken.
- Aufgabeneinstellungen
  * Einstellbare Werte wurden nach Wichtigkeit sortiert.
  * Initialwert des Flugabstandes auf 3 Minuten ge�ndert.
  * Auswertungsdetails einstellbar:
      Flugplanungsergebnisse (Standard: ein) 
      Navigationsflugergebnisse (Standard: ein) 
      Beobachtungsergebnisse (Standard: ein)
      Landungsergebnisse (Standard: ein)
      Andere Ergebnisse (Standard: aus)
- Wettbewerbsplanung
  * Automatische Flugplan-Neuberechnung bei Flugwind�nderung.
  * Zeitplan-Berechnung erfolgt nur noch f�r Besatzungen, 
    deren Zeitplan als "Nicht berechnet" angezeigt wird.
    Zeitpl�ne werden beim �ndern der Reihenfolge als "Nicht berechnet"
    markiert. 
  * Streckenberechnungen werden jetzt exakt wie bei PrecisWin gerundet.  
  * Zeit- und Flugpl�ne werden mit Ausgabe-Nummer gedruckt.
    Alle Zeitplan- oder Flugplan-Neuberechnungen erh�hen die Ausgabenummer
    beim n�chsten Druck des Zeitplanes oder aller Fluppl�ne um 1.
  * Besatzungen k�nnen ans Ende verschoben werden.
    Beim Verschieben nach unten oder ans Ende werden nur die Planzeiten
    der Besatzungen zur�ckgesetzt, die angehakt sind.
    Beim Verschieben nach oben werden die Planzeiten der angehakten
    Besatzungen und die nach unten r�ckende Besatzung zur�ckgesetzt.
  * Druck eines Schiedsrichter-Zeitplanes mit mehr Details m�glich.
  * Checkpunkt-Abk�rzungen ber�cksichtigen nun die aktive Anzeige-Sprache. 
    Entsprechend der Wettbewerbsordnung Navigationsflug 2008 werden 
    Wendepunkte nun im Deutschen mit 'WP' abgek�rzt (vorher 'TP').
    Unbekannte Zeitkontrollen werden mit 'UZK' abgek�rzt (vorher 'Secret').
- Wettbewerbsauswertung
  * Ergebnisse werden bei Planungs�nderung automatisch zur�ckgesetzt.
  * Punkte-Eingabe f�r Beobachtungen, Landungen und Anderes erg�nzt.
  * Ausdruck des Besatzungsergebnisses
  * Wettbewerbs-Gesamtauswertung
    (Summe mehrerer Aufgaben/Wettbewerbstage) m�glich.
  * Deaktivierung von Checkpunkten m�glich (Deaktivierte Checkpunkte).
    Nach einer �nderung werden die Strafpunkte neu berechnet und die
    Platzierung auf 'n/a' (= nicht berechnet) gesetzt.
  * Beim Import von AFLOS-Logger-Daten k�nnen nun auch zutreffende 
    AFLOS-Fehlerpunkte angezeigt werden (Knopf "AFLOS-Logger-Fehler").
- Bedienoberfl�che
  * Bl�ttern-Modus f�r Planung und Auswertung mit frei einstellbarer 
    Teilnehmer-Anzahl (Extras -> Einstellungen).
  * Stellt folgende Informationen beim Start wieder her:
      Letzter Wettbewerb
      Bediensprache
      Drucksprache
      Zeilenzahl beim Bl�ttern
    Dazu werden werden auf dem Bedien-Computer Cookies abgespeichert.

Erweiterungen in Version 0.5:
-----------------------------
- �bernahme von �berflug-Daten aus AFLOS
- Neue Hauptmen�sortierung
- Ergebniseingabe bei Flugplanungs- und Navigationstest wurde verbessert:
  * Ein Haken wird angezeigt, wenn ein Wert eingegeben wurde.
  * Die Werteeingabe kann zur�ckgesetzt werden.
  * Es werden Sollwerte bei der Istwerteingabe mit angezeigt.
  * Bei Zeiten ist auch die Eingabe hh.mm.ss erlaubt.
- Anzeige des Besatzungsergebnisses

Erweiterungen in Version 0.4:
-----------------------------
- �bernahme von Strecken aus AFLOS
- Unterst�tzung mehrerer Wettbewerbe
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
Ein Server stellt eine Datenbank im Netzwerk zur Verf�gung.
Auf einem oder mehreren Clients l�uft 'Flight Context' im Web-Browser.
Server und Client k�nnen auf einem Windows-Rechner liegen.

Beste Darstellung im Client mit dem Web-Browser 'Firefox'.
'Internet Explorer' ebenfalls m�glich.


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
f�hren Sie nach der ersten Benutzung von AFLOS den Punkt
'Programme -> Flight Contest -> Install AFLOS connection' aus
(in Windows Vista oder Windows 7 als Administrator).
Kann trotzdem nicht direkt auf AFLOS-Daten zugegriffen werden,
kann immer Fall 2 (Hochladen der AFLOS-Datenbank) angewendet werden.

'Flight Contest' importiert Strecken und Logger-Messwerte entweder direkt
aus der zuletzt oder gerade ge�ffneten AFLOS-Datenbank (Fall 1)
oder aus der zuletzt hochgeladenen AFLOS-Datenbank (Fall 2).

Import von Strecken (Strecken -> Import AFLOS-Strecke):
Stellen Sie zum Import sicher, dass Check-Punkte unbekannter Zeitkontrollen 
in AFLOS eine Torbreite von 2NM oder die Markierung $secret haben.

Import von Logger-Messwerten (Auswertung -> Navigationsflug -> Import AFLOS-Logger-Daten):
Stellen Sie zum Import sicher, dass die aus dem Logger eingelesenen Messwerte
f�r eine konkrete Besatzung (= Comp.-Nr.) und Strecke (= Ref.-Nr.) und der 
aktivierten Option 'Procedure Turn" in AFLOS mit 'Check-Data -> Calculate' 
berechnet wurden.
Fehlerhaft errechnete Kursabweichungen k�nnen nach dem Import manuell korrigiert werden
(Klick auf Zahl in Nr.-Spalte f�r den gew�nschten Check-Punkt).
Nicht auswertbare Logger-Messwerte k�nnen nicht importiert werden. 
"Flight Contest -> AFLOS -> Erfasste Fehlerstati"
oder "AFLOS -> Check -Overview" zeigen Status von AFLOS-Messungen an:
  Flight O.K.      Auswertbarer Flug ohne Flugfehler
  Flight not O.K.  Auswertbarer Flug mit Flugfehlern
  Check Error !    Nicht auswertbarer Flug
    (-> Import-Fehler "AFLOS-Logger-Daten von ... enthalten Fehler.")
  F�r eine AFLOS-Besatzung nicht vorhandener Eintrag
    (-> Import-Fehler "AFLOS-Logger-Daten von ... nicht komplett.")


Excel-Besatzungslisten
----------------------
Sie k�nnen Besatzungslisten mit Excel erstellen und in 'Flight Contest'
importieren.

Verwenden Sie dazu folgende Vorlage, deren Aufbau einen erfolgreichen
Import sicherstellt: 
'Programme -> Flight Contest -> Samples -> FC-CrewList-Sample.xls'

Importiert werden nur Besatzungsnamen, die noch nicht vorhanden sind.


Formeln der Strafpunkt-Berechnung von Landungen
-----------------------------------------------
Berechungsformel f�r 'Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2012' f�r alle Landungen:
{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;default:return 200;}}

Berechungsformel f�r 'Wettbewerbsordnung Pr�zisionsflug Deutschland - Ausgabe 2005' f�r alle Landungen:
f�r Landung 1 und 4:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(7*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 175;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return 200;}}}
f�r Landung 2 und 3:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 100;case 'D':return 50;case 'E':return 60;case 'F':return 70;case 'G':return 80;case 'H':return 90;default:return 150;}}}

Berechungsformel f�r 'FAI Air Rally Flying - Edition 2012' f�r alle Landungen:
{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return 200;}}

Berechungsformel f�r 'FAI Precision Flying - Edition 2012':
f�r Landung 1 und 4:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 5*i}else{return -(10*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 250;case 'D':return 125;case 'E':return 150;case 'F':return 175;case 'G':return 200;case 'H':return 225;default:return 300;}}}
f�r Landung 2 und 3:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(6*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 150;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return 200;}}}

Berechungsformel f�r 'Wettkampfreglement Pr�zisionsflug-Schweizermeisterschaft - Ausgabe 2009' f�r alle Landungen:
f�r Landung 1 und 4:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(7*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 175;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return 200;}}}
f�r Landung 2 und 3:
{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 100;case 'D':return 50;case 'E':return 60;case 'F':return 70;case 'G':return 80;case 'H':return 90;default:return 150;}}}


M�gliche Betriebssysteme der Server-Installation:
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

Auf Icon des Dienstemanagers 'Apache Tomcat FlightContest' ausf�hren:
  Kontextmen�punkt 'Start Service' aufrufen


Server stoppen:
---------------
Auf Icon des Dienstemanagers 'Apache Tomcat FlightContest' ausf�hren:
  Kontextmen�punkt 'Stop Service'
  
  
Behebung von Server-Ausf�hrungsproblemen:
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
Im Wettbewerbs-Men� steht der Men�punkt 'Demo-Wettbewerb' zur Verf�gung,
mit dem verschiedene Demo-Wettbewerbe angelegt werden k�nnen.

Beim Laden eines Demo-Wettbewerbs wird automatisch die 
Demo-AFLOS-Datenbank aktiviert. Zuvor in AFLOS geladene Datenbanken oder
hochgeladene Datenbanken bleiben davon unber�hrt und stehen bei Wechsel
zu einem echten Wettbewerb automatisch wieder zur Verf�gung. 

Das gleichzeitige Benutzen von Demo-Wettbewerb und echtem Wettbewerb
im Netzwerk ist ebenfalls m�glich.
 
    
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
  darf nicht gel�scht werden.


Wiederherstellung eines fehlgeschlagenen 'Flight Contest'-Updates:
------------------------------------------------------------------
Das Verzeichnis <Installationsverzeichnis>\fc wird bei 
Update-Installation mit dem Namen 
<Installationsverzeichnis>\fc-backup-<Datum> automatisch gesichert. 

Sollte das 'Flight Contest'-Update nicht starten, kann folgendermassen 
zum letzten funktionst�chtigen 'Flight Contest' zur�ckgekehrt werden:
  1. Server stoppen
  2. 'Flight Contest' deinstallieren
  3. <Installationsverzeichnis>\fc l�schen
  4. <Installationsverzeichnis>\fc-backup-<Datum> in
     <Installationsverzeichnis>\fc umbenennen
  5. Letztes funktionst�chtiges 'Flight Contest' installieren
  

Logs:
-----
  Sind hier zu finden:
    <Installationsverzeichnis>\fc\logs


Cookies:
--------
  Speichert folgende Cookies f�r 1 Jahr:
    LastContestID    - Letzter Wettbewerb 
    ShowLanguage     - Bediensprache
    PrintLanguage    - Drucksprache
    ShowLimitCrewNum - Zeilenzahl beim Bl�ttern

