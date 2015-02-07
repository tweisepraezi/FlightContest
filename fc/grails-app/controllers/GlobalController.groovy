class GlobalController {

	def fcService
    def mailService
    def gpxService
    
    def index = { 
    	redirect(action:list,params:params) 
    }

    def info = {
        fcService.println "List info"
        // save return action
        session.crewReturnAction = actionName
        session.crewReturnController = controllerName
        session.crewReturnID = params.id
        session.aircraftReturnAction = actionName
        session.aircraftReturnController = controllerName
        session.aircraftReturnID = params.id
        session.teamReturnAction = actionName
        session.teamReturnController = controllerName
        session.teamReturnID = params.id
        session.gpxviewerReturnAction = actionName
        session.gpxviewerReturnController = controllerName
        session.gpxviewerReturnID = params.id
        [globalInstance:BootStrap.global,contestInstance:session.lastContest,url:params.url]
    }
    
    def list = {
		fcService.println "List extras"
		// save return action
		session.crewReturnAction = actionName 
		session.crewReturnController = controllerName
		session.crewReturnID = params.id
		session.aircraftReturnAction = actionName
		session.aircraftReturnController = controllerName
		session.aircraftReturnID = params.id
		session.teamReturnAction = actionName
		session.teamReturnController = controllerName
		session.teamReturnID = params.id
    	[globalInstance:BootStrap.global,contestInstance:session.lastContest]
    }

    def listall = {
		fcService.println "List extras (all)"
		// save return action
		session.crewReturnAction = actionName 
		session.crewReturnController = controllerName
		session.crewReturnID = params.id
		session.aircraftReturnAction = actionName
		session.aircraftReturnController = controllerName
		session.aircraftReturnID = params.id
		session.teamReturnAction = actionName
		session.teamReturnController = controllerName
		session.teamReturnID = params.id
        [globalInstance:BootStrap.global,contestInstance:session.lastContest]
    }

    def changeglobalsettings = {
        fcService.println "Change global settings"
		if (!session.showLanguage) {
			session.showLanguage = Languages.de.toString()
		}
		if (!session.printLanguage) {
			session.printLanguage = Languages.de.toString()
		}
		if (!session.showLimitCrewNum) {
			session.showLimitCrewNum = 10
		}
        File config_file = new File("${System.properties['user.home']}/.fc/config.groovy")
        if (config_file.exists()) {
            session.configText = config_file.text
        } else {
            session.configText = ""
        }
        [globalInstance:BootStrap.global]
    }

    def update = {
        fcService.printstart "Update global settings"
        
        fcService.println "Set showLanguage to '$params.showLanguage'"
		session.showLanguage = params.showLanguage
        
        fcService.println "Set printLanguage to '$params.printLanguage'"
        session.printLanguage = params.printLanguage
        
		session.showLimitCrewNum = params.showLimitCrewNum.toInteger()
		if (params.lastShowLimitCrewNum != params.showLimitCrewNum) {
			session.showLimitStartPos = 0
		}
        session.configText = params.configText
		
		fcService.SetCookie(response, "ShowLanguage",     params.showLanguage)
		fcService.SetCookie(response, "PrintLanguage",    params.printLanguage)
		fcService.SetCookie(response, "ShowLimitCrewNum", params.showLimitCrewNum)
        
        File config_dir = new File("${System.properties['user.home']}/.fc")
        if (!config_dir.exists()) {
            config_dir.mkdir()
        }

        File config_file = new File("${System.properties['user.home']}/.fc/config.groovy")
        BufferedWriter config_file_writer = config_file.newWriter()
        config_file_writer.write(session.configText)
        config_file_writer.close()
        
        fcService.printdone ""
        redirect(action:info,params:[lang:params.showLanguage])
    }
    
    def testmail = {
        try {
            mailService.sendMail {
                from grailsApplication.config.flightcontest.mail.from
                to NetTools.EMailList(grailsApplication.config.flightcontest.testmail.to).toArray()
                if (grailsApplication.config.flightcontest.mail.cc) {
                    cc NetTools.EMailList(grailsApplication.config.flightcontest.mail.cc).toArray()
                }
                subject grailsApplication.config.flightcontest.testmail.subject
                body grailsApplication.config.flightcontest.testmail.body
            }
            flash.message = message(code:'fc.net.mail.sent',args:[grailsApplication.config.flightcontest.testmail.to])
        } catch (Exception e) {
            flash.message = e.getMessage() 
            flash.error = true
        }
        redirect(action:info)
    }
    
    def testftp = {
        fcService.printstart "Test FTP"
        String test_filename = "${UUID.randomUUID().toString()}.txt"
        Map ret = gpxService.SendFTP(grailsApplication.config.flightcontest,"_ftptest", grailsApplication.config.flightcontest.ftp.testsourceurl, test_filename)
        flash.message = "${ret.message}" 
        flash.error = ret.error
        if (!ret.error) {
            String url = "${grailsApplication.config.flightcontest.ftp.publicurl}${grailsApplication.config.flightcontest.ftp.directory}/_ftptest/${test_filename}"
            fcService.printdone "'$url' uploaded."
            redirect(action:info,params:[url:url])
        } else {
            fcService.printerror ret.error
            redirect(action:info)
        }
    }
    
    def start_openurl = { // UNUSED: start_openurl
        fcService.println "Open '$params.url'"
        fcService.printdone ""
        render(view:"openurl", model:[url:params.url])
    }
    
    def openurl = { // UNUSED: openurl
        return [:]
    }

    def cancel = {
        redirect(action:info)
    }
}
