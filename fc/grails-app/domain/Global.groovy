class Global 
{
    def grailsApplication
    
	// DB-2.10
	static int DB_MAJOR = 2
	static int DB_MINOR = 10
	
	int versionMajor = DB_MAJOR
	int versionMinor = DB_MINOR
	String showLanguage = "de"             // UNUSED: Global.showLanguage, DB-2.10
	String printLanguage = "de"            // Print language, DB-2.0, UNUSED: Global.printLanguage, DB-2.10
	String dbCompatibility = ""            // DB-2.0
    
    static constraints = {
    }
	
    // --------------------------------------------------------------------------------------------------------------------
	boolean IsDBNewer()
	{
		switch (dbCompatibility) {
			case "newerMajor":
			case "equalMajorNewerMinor":
				return true
		}
		return false
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	boolean IsDBOlder()
	{
		switch (dbCompatibility) {
			case "olderMajor":
				return true
		}
		return false
	}
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsEMailPossible()
    {
        if (grailsApplication.config.grails.mail.host 
         && grailsApplication.config.grails.mail.port
         && grailsApplication.config.grails.mail.username
         && grailsApplication.config.grails.mail.password
         && grailsApplication.config.flightcontest.mail.from
         && grailsApplication.config.flightcontest.mail.cc
        ) {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsTestEMailPossible()
    {
        if (IsEMailPossible()  
         && grailsApplication.config.flightcontest.testmail.to
         && grailsApplication.config.flightcontest.testmail.subject
         && grailsApplication.config.flightcontest.testmail.body
        ) {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsFTPPossible()
    {
        if (grailsApplication.config.flightcontest.ftp.host
         && grailsApplication.config.flightcontest.ftp.port
         && grailsApplication.config.flightcontest.ftp.username
         && grailsApplication.config.flightcontest.ftp.password
         && grailsApplication.config.flightcontest.ftp.directory
         && grailsApplication.config.flightcontest.ftp.publicurl
        ) {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsTestFTPPossible()
    {
        if (IsFTPPossible()
         && grailsApplication.config.flightcontest.ftp.testsourceurl
        ) {
            return true
        }
        return false
    }
}
