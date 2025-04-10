import grails.util.Environment
import org.gdal.gdal.gdalJNI

class Global 
{
    def grailsApplication
    
	// Actual database version: DB-2.42
	static int DB_MAJOR = 2
	static int DB_MINOR = 42
	
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
	
    static String ClientID = ""
    static String ConfigServer = ""
    static String FCMapServer = ""
    static String OpenAIPServer = ""
    static String OpenAIPAPIKey = ""
    static String OpenAIPIgnoreAirspacesStartsWith = ""
    static String OwnerEMail = ""
    static String OwnerClub = ""
    static Integer FCMapCounter = 0
    
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
    String GetClientID()
    {
        String client_id = ""
        def process = ['powershell', '-command', '(get-itemproperty -path HKLM:\\SOFTWARE\\Microsoft\\SQMClient -Name MachineID).MachineID'].execute()
        client_id = process.text
        if (client_id) {
            client_id = client_id.trim()
        }
        if (client_id) {
            client_id = client_id.replace('{','')
        }
        if (client_id) {
            client_id = client_id.replace('}','')
        }
        return client_id
    }

    // --------------------------------------------------------------------------------------------------------------------
    String GetConfigServer()
    {
        if (grailsApplication.config.flightcontest.config.server) {
            return grailsApplication.config.flightcontest.config.server
        }
        return Defs.DEFAULT_CONFIG_SERVER
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean LoadClientConfig()
    {
        ClientID = GetClientID()
        ConfigServer = GetConfigServer()
        FCMapServer = ""
        OpenAIPServer = ""
        OpenAIPAPIKey = ""
        OpenAIPIgnoreAirspacesStartsWith = ""
        OwnerEMail = ""
        OwnerClub = ""
        
        // read config server config
        boolean config_found = false
        if (ClientID && ConfigServer) {
            String config_url = ConfigServer + "/" + ClientID + "/" + Defs.CONFIG_NAME
            def connection = config_url.toURL().openConnection()
            connection.requestMethod = "GET"
            connection.doInput = true
            int response_code = 503
            try {
                response_code = connection.responseCode
            } catch (Exception e) {
            }
            if (response_code == 200) {
                InputStream inputstream_instance = connection.getInputStream()
                BufferedReader input_reader = inputstream_instance.newReader("UTF-8")
                String input_data = ""
                while (true) {
                    String line = input_reader.readLine()
                    if (line == null) {
                        break
                    }
                    input_data += line
                }
                input_reader.close()
                inputstream_instance.close()
                def loaded_data = Eval.me(input_data)
                if (loaded_data && loaded_data instanceof Map) {
                    if (loaded_data.fcmap.server) {
                        FCMapServer = loaded_data.fcmap.server
                    }
                    if (loaded_data.openaip.server) {
                        OpenAIPServer = loaded_data.openaip.server
                    }
                    if (loaded_data.openaip.apikey) {
                        OpenAIPAPIKey = loaded_data.openaip.apikey
                    }
                    if (loaded_data.openaip.ignoreAirspacesStartsWith) {
                        OpenAIPIgnoreAirspacesStartsWith = loaded_data.openaip.ignoreAirspacesStartsWith
                    }
                    if (loaded_data.owner.email) {
                        OwnerEMail = loaded_data.owner.email
                    }
                    if (loaded_data.owner.club) {
                        OwnerClub = loaded_data.owner.club
                    }
                    config_found = true
                }
            }
        }
        
        // read local config
        if (grailsApplication.config.flightcontest.maps.fcmap.server) {
            FCMapServer = grailsApplication.config.flightcontest.maps.fcmap.server
        }
        if (grailsApplication.config.flightcontest.maps.openaip.server) {
            OpenAIPServer = grailsApplication.config.flightcontest.maps.openaip.server
        }
        if (grailsApplication.config.flightcontest.maps.openaip.apikey) {
            OpenAIPAPIKey = grailsApplication.config.flightcontest.maps.openaip.apikey
        }
        if (grailsApplication.config.flightcontest.maps.openaip.ignoreAirspacesStartsWith) {
            OpenAIPIgnoreAirspacesStartsWith = grailsApplication.config.flightcontest.maps.openaip.ignoreAirspacesStartsWith
        }
        
        return config_found
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    void CountFCMap()
    {
        // read config server fcmap counter
        if (ClientID && ConfigServer) {
            String config_url = ConfigServer + "/" + ClientID + "/" + Defs.FCMAP_COUNTER_NAME
            def connection = config_url.toURL().openConnection()
            connection.requestMethod = "GET"
            connection.doInput = true
            int response_code = 503
            try {
                response_code = connection.responseCode
            } catch (Exception e) {
            }
            if (response_code == 200) {
                FCMapCounter++
            }
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetPrintServerAPI()
    {
        return FCMapServer
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsContestMapDevOptions()
    {
        if (IsDevelopmentEnvironment()) {
            return true
        }
        if (grailsApplication.config.flightcontest.maps.fcmap.devoptions) {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsOpenAIP()
    {
        if (OpenAIPServer && OpenAIPAPIKey) {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetOpenAIPAPI()
    {
        return OpenAIPServer
    }
    
    // --------------------------------------------------------------------------------------------------------------------
	String GetOpenAIPToken()
	{
        return OpenAIPAPIKey
	}
	
    // --------------------------------------------------------------------------------------------------------------------
    String GetOpenAIPIgnoreAirspacesStartsWith()
    {
        return OpenAIPIgnoreAirspacesStartsWith
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetGMApiKey()
    {
        if (   grailsApplication.config.flightcontest.onlinemap
            && grailsApplication.config.flightcontest.onlinemap.gm_api_key
           )
        {
            return grailsApplication.config.flightcontest.onlinemap.gm_api_key
        }
        return ""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetMapTilesServer()
    {
        if (   grailsApplication.config.flightcontest.onlinemap
            && grailsApplication.config.flightcontest.onlinemap.tiles_server
           )
        {
            return grailsApplication.config.flightcontest.onlinemap.tiles_server
        }
        return "https://tiles.flightcontest.de"
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean GetMapTilesTMS()
    {
        if (   grailsApplication.config.flightcontest.onlinemap
            && grailsApplication.config.flightcontest.onlinemap.tiles_xyz
           )
        {
            return false
        }
        return true
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
        return true
        /*
        if (   grailsApplication.config.flightcontest.taskcreator
            && grailsApplication.config.flightcontest.taskcreator.url
        ) {
            return true
        }
        return false
        */
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean IsTaskCreatorJSExtern()
    {
        if (grailsApplication.config.flightcontest.taskcreator.jsextern) {
            return true
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    String GetTaskCreatorExternURL()
    {
        if (grailsApplication.config.flightcontest.taskcreator.url) {
            return grailsApplication.config.flightcontest.taskcreator.url
        }
        return Defs.DEFAULT_EXTERNAL_TASKCREATOR
    }
}
