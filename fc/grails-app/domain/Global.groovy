class Global 
{
    def grailsApplication
    
	// DB-2.11
	static int DB_MAJOR = 2
	static int DB_MINOR = 11
	
    static final int LIVE_UPLOADSECONDS = 60
    static final String LIVE_STYLESHEET = "fclive.css"
    static final String EMAIL_SENDING   = "_email_sending"
    
	int versionMajor = DB_MAJOR
	int versionMinor = DB_MINOR
	String showLanguage = "de"                         // UNUSED: Global.showLanguage, DB-2.10
	String printLanguage = "de"                        // Print language, DB-2.0, UNUSED: Global.printLanguage, DB-2.10
	String dbCompatibility = ""                        // DB-2.0
    
    Long liveContestID = 0                             // Live-Anzeige: Gewählter Contest, DB-2.11
    Integer liveUploadSeconds = LIVE_UPLOADSECONDS     // Live-Anzeige: Upload-Zeit in Sekunden, DB-2.11
    String liveLanguage = "de"                         // Live-Anzeige: Sprache, DB-2.11
    
    static constraints = {
        // DB-2.11 compatibility
        liveContestID(nullable:true)
        liveUploadSeconds(nullable:true,blank:false, min:1)
        liveLanguage(nullable:true)
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
    boolean IsWebMailPossible()
    {
        if (grailsApplication.config.flightcontest.webmail.url
         && grailsApplication.config.flightcontest.webmail.loginname_name
         && grailsApplication.config.flightcontest.webmail.loginpassword_name
         && grailsApplication.config.flightcontest.webmail.username
         && grailsApplication.config.flightcontest.webmail.password
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
         && grailsApplication.config.flightcontest.ftp.contesturl
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

    // --------------------------------------------------------------------------------------------------------------------
    boolean IsLiveFTPUploadPossible()
    {
        if (IsFTPPossible()
            && grailsApplication.config.flightcontest.live
            && grailsApplication.config.flightcontest.live.ftpupload.name
            && grailsApplication.config.flightcontest.live.ftpupload.workingdir
           ) {
               return true
           }
           return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsLiveCopyPossible()
    {
        if (   grailsApplication.config.flightcontest.live
            && grailsApplication.config.flightcontest.live.copy
            && grailsApplication.config.flightcontest.live.copy.size()
           ) {
               return true
           }
           return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsLivePossible()
    {
        if (IsLiveFTPUploadPossible() || IsLiveCopyPossible()) {
            return true
        }
        return false
    }
}
