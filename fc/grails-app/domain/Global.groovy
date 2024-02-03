import grails.util.Environment
import org.gdal.gdal.gdalJNI

class Global 
{
    def grailsApplication
    
	// Actual database version: DB-2.38
	static int DB_MAJOR = 2
	static int DB_MINOR = 38
	
	int versionMajor = DB_MAJOR
	int versionMinor = DB_MINOR
	String showLanguage = "de"                           // UNUSED: Global.showLanguage, DB-2.10
	String printLanguage = "de"                          // Print language, DB-2.0, UNUSED: Global.printLanguage, DB-2.10
	String dbCompatibility = ""                          // DB-2.0
    
    Long liveContestID = 0                               // Live-Anzeige: Gewählter Contest, DB-2.11
    Integer liveUploadSeconds = Defs.LIVE_UPLOADSECONDS  // Live-Anzeige: Upload-Zeit in Sekunden, DB-2.11
    String liveLanguage = "de"                           // Live-Anzeige: Sprache, DB-2.11
    
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
    static boolean IsDevelopmentEnvironment() 
    {
        if (Environment.currentEnvironment == Environment.DEVELOPMENT || grails.util.GrailsUtil.getEnvironment().equals("lastdb")) {
            return true
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static boolean IsCloudFoundryEnvironment() 
    {
        if (grails.util.GrailsUtil.getEnvironment().equals("cloudfoundry")) {
            return true
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsCloudDemo()
    {
        if (grailsApplication.config.flightcontest.clouddemo
         && grailsApplication.config.flightcontest.clouddemo.enable) {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean ShowObservationButtons()
    {
        if (grailsApplication.config.flightcontest.observation
         && grailsApplication.config.flightcontest.observation.nobuttons) {
            return false
        }
        return true
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
    String TestEMailAddress()
    {
        return grailsApplication.config.flightcontest.testmail.to
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
    String GetLiveFTPURL()
    {
        return "${grailsApplication.config.flightcontest.ftp.contesturl}${grailsApplication.config.flightcontest.live.ftpupload.workingdir}${grailsApplication.config.flightcontest.live.ftpupload.name}"
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
        //if (IsLiveFTPUploadPossible() || IsLiveCopyPossible()) {
        if (grailsApplication.config.flightcontest.live) {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsLiveTrackingPossible()
    {
        if (   grailsApplication.config.flightcontest.livetracking
            && grailsApplication.config.flightcontest.livetracking.server
            && grailsApplication.config.flightcontest.livetracking.api
            && grailsApplication.config.flightcontest.livetracking.token
        ) {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetLiveTrackingAPI()
    {
        if (   grailsApplication.config.flightcontest.livetracking
            && grailsApplication.config.flightcontest.livetracking.server
            && grailsApplication.config.flightcontest.livetracking.api
        ) {
            return grailsApplication.config.flightcontest.livetracking.server + grailsApplication.config.flightcontest.livetracking.api
        }
        return ""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetLiveTrackingToken()
    {
        if (   grailsApplication.config.flightcontest.livetracking
            && grailsApplication.config.flightcontest.livetracking.token
        ) {
            return grailsApplication.config.flightcontest.livetracking.token
        }
        return ""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean ShowLiveTrackingIDs()
    {
        if (IsDevelopmentEnvironment()) {
            return true
        }
        if (   grailsApplication.config.flightcontest.livetracking
            && grailsApplication.config.flightcontest.livetracking.showids
        ) {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetLiveTrackingMap(Integer navigationTaskID)
    {
        if (   grailsApplication.config.flightcontest.livetracking
            && grailsApplication.config.flightcontest.livetracking.server
        ) {
            return "${grailsApplication.config.flightcontest.livetracking.server}${Defs.LIVETRACKING_DISPLAY_TASK}${navigationTaskID.toString()}${Defs.LIVETRACKING_DISPLAY_MAP}"
        }
        return ""
	}
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetLiveTrackingResults(Integer contestID, Integer navigationTaskID)
    {
        if (   grailsApplication.config.flightcontest.livetracking
            && grailsApplication.config.flightcontest.livetracking.server
        ) {
            return "${grailsApplication.config.flightcontest.livetracking.server}${Defs.LIVETRACKING_RESULTS_SERVICE}${contestID.toString()}${Defs.LIVETRACKING_RESULTS_TASKRESULTS}${navigationTaskID}/"
        }
        return ""
	}
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsLiveTrackingContestDeletePossible()
    {
        if (   grailsApplication.config.flightcontest.livetracking
            && grailsApplication.config.flightcontest.livetracking.contest
            && grailsApplication.config.flightcontest.livetracking.contest.showDelete
        ) {
            return true
        }
        return false
    }
	
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsLiveTrackingNavigationTaskDeletePossible()
    {
        if (   grailsApplication.config.flightcontest.livetracking
            && grailsApplication.config.flightcontest.livetracking.navigationtask
            && grailsApplication.config.flightcontest.livetracking.navigationtask.showDelete
        ) {
            return true
        }
        return false
    }
	
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsLogPossible()
    {
        if (IsDevelopmentEnvironment()) {
            if (    grailsApplication.config.flightcontest.wrlog
                && !grailsApplication.config.flightcontest.wrlog.enable
               )
            {
                return false
            }
            return true
        }
        if (IsCloudFoundryEnvironment()) {
            return true
        }
        if (   grailsApplication.config.flightcontest.wrlog
            && grailsApplication.config.flightcontest.wrlog.enable
           )
        {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsContestMapDevOptions()
    {
        if (IsDevelopmentEnvironment()) {
            return true
        }
        if (   grailsApplication.config.flightcontest.contestmap
            && grailsApplication.config.flightcontest.contestmap.devoptions
           )
        {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetPrintServerAPI()
    {
        if (   grailsApplication.config.flightcontest.contestmap
            && grailsApplication.config.flightcontest.contestmap.printserverapi
           )
        {
            return grailsApplication.config.flightcontest.contestmap.printserverapi
        }
        return ""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean GetPrintServerFCStyle()
    {
        if (   grailsApplication.config.flightcontest.contestmap
            && grailsApplication.config.flightcontest.contestmap.osmcartostyle
           )
        {
            return false
        }
        return true
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetGMApiKey()
    {
        if (   grailsApplication.config.flightcontest.maps
            && grailsApplication.config.flightcontest.maps.gm_api_key
           )
        {
            return grailsApplication.config.flightcontest.maps.gm_api_key
        }
        return ""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetMapTilesServer()
    {
        if (   grailsApplication.config.flightcontest.maps
            && grailsApplication.config.flightcontest.maps.tiles_server
           )
        {
            return grailsApplication.config.flightcontest.maps.tiles_server
        }
        return "https://tiles.flightcontest.de"
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean GetMapTilesTMS()
    {
        if (   grailsApplication.config.flightcontest.maps
            && grailsApplication.config.flightcontest.maps.tiles_xyz
           )
        {
            return false
        }
        return true
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsOpenAIP()
    {
        if (   grailsApplication.config.flightcontest.openaip
            && grailsApplication.config.flightcontest.openaip.server
            && grailsApplication.config.flightcontest.openaip.api
            && grailsApplication.config.flightcontest.openaip.token
        ) {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetOpenAIPAPI()
    {
        if (   grailsApplication.config.flightcontest.openaip
            && grailsApplication.config.flightcontest.openaip.server
            && grailsApplication.config.flightcontest.openaip.api
        ) {
            return grailsApplication.config.flightcontest.openaip.server + grailsApplication.config.flightcontest.openaip.api
        }
        return ""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
	String GetOpenAIPToken()
	{
        if (   grailsApplication.config.flightcontest.openaip
            && grailsApplication.config.flightcontest.openaip.token
           )
        {
            return grailsApplication.config.flightcontest.openaip.token
        }
        return ""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsGDALAvailable() {
        return gdalJNI.isAvailable()
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsTitlesUploadAvailable()
    {
        if (   grailsApplication.config.flightcontest.ftptiles
            && grailsApplication.config.flightcontest.ftptiles.host
            && grailsApplication.config.flightcontest.ftptiles.port
            && grailsApplication.config.flightcontest.ftptiles.username
            && grailsApplication.config.flightcontest.ftptiles.password
            && grailsApplication.config.flightcontest.ftptiles.basedir
            && gdalJNI.isAvailable()
           )
        {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetTitlesUploadHost()
    {
        if (IsTitlesUploadAvailable()) {
            return grailsApplication.config.flightcontest.ftptiles.host
        }
        return ""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetLandingInfo()
    {
        if (   grailsApplication.config.flightcontest.landing
            && grailsApplication.config.flightcontest.landing.info
           )
        {
            return grailsApplication.config.flightcontest.landing.info
        }
        return ""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsTaskCreatorExtern()
    {
        if (   grailsApplication.config.flightcontest.taskcreator
            && grailsApplication.config.flightcontest.taskcreator.url
        ) {
            return true
        }
        return false
    }
    
    
}
