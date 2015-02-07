class AflosCrewNames implements Serializable 
{
	// listed in Datasources.groovy
	
    long lineid
	int startnum
	int points            // Gesamtzahl der Messpunkte der Start-Nummer beim letzten Datenbankeintrag, sonst 0
    int measurePointsNum  // Anzahl der Messpunkte des Datenbankeintrages für eine Startnummer, max. 3600
	String name
    byte[] daten
    byte[] vertices
    byte[] satellit

	static mapping = {
		datasources(['aflos','aflostest','aflosupload'])
		table 'PLN_Data'
		
        lineid column: 'PLINEID'
		startnum column: 'Start_Nr'
		points column: 'Points'
        measurePointsNum column: 'NUMVERT'
		name column: 'MARK'
        daten column: 'Daten'
        vertices column: 'VERTICES'
        satellit column: 'Satellit'

        id name: 'lineid'
		version false
	}
	
	String viewName()
	{
		if (name) {
			return "$startnum: $name"
		}
		return "$startnum"
	}
}
