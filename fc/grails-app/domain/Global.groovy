class Global 
{
	static int DB_MAJOR = 2 
	static int DB_MINOR = 2
	
	int versionMajor = DB_MAJOR
	int versionMinor = DB_MINOR
	String showLanguage = "de"
	String printLanguage = "de"            // Print language, DB-2.0
	String dbCompatibility = ""            // DB-2.0
	
    static constraints = {
    }
	
	boolean IsDBNewer()
	{
		switch (dbCompatibility) {
			case "newerMajor":
			case "equalMajorNewerMinor":
				return true
		}
		return false
	}
	
	boolean IsDBOlder()
	{
		switch (dbCompatibility) {
			case "olderMajor":
				return true
		}
		return false
	}
}
