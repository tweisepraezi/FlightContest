import java.util.Map;

import org.quartz.JobKey

class GlobalController {

	def fcService
    def mailService
    def gpxService
    def geoDataService
    def quartzScheduler
    
    def index = { 
    	redirect(action:list,params:params) 
    }

    def info = {
        fcService.println "List info"
        if (session?.lastContest) {
            session.lastContest.refresh()
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
            return [globalInstance:BootStrap.global,contestInstance:session.lastContest,url:params.url]
        }
        return [globalInstance:BootStrap.global,url:params.url]
    }
    
    def list = {
		fcService.println "List extras"
        if (session?.lastContest) {
            session.lastContest.refresh()
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
        	return [globalInstance:BootStrap.global,contestInstance:session.lastContest]
        }
        redirect(controller:'contest',action:'start')
    }

    def listall = {
		fcService.println "List extras (all)"
        if (session?.lastContest) {
            session.lastContest.refresh()
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
            return [globalInstance:BootStrap.global,contestInstance:session.lastContest]
        }
        redirect(controller:'contest',action:'start')
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
        String config_file_name = Defs.FCSAVE_FILE_CONFIG
        fcService.println "Read $config_file_name"
        File config_file = new File(config_file_name)
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

        if (!Global.IsCloudFoundryEnvironment()) {
            CheckAndCreateDir(Defs.FCSAVE_FOLDER)
            CheckAndCreateDir(Defs.FCSAVE_FOLDER_FC)
            String config_file_name = Defs.FCSAVE_FILE_CONFIG
            fcService.println "Save $config_file_name"
            File config_file = new File(config_file_name)
            BufferedWriter config_file_writer = config_file.newWriter()
            config_file_writer.write(session.configText)
            config_file_writer.close()
        }
        
        fcService.printdone ""
        redirect(action:info,params:[lang:params.showLanguage])
    }
    
    def livesettings = {
        gpxService.println "Live settings"
        if (session?.lastContest) {
            session.lastContest.refresh()
            return [globalInstance:BootStrap.global,urlList:GetLiveUrlList(request),contestInstance:session.lastContest]
        } else {
            return [globalInstance:BootStrap.global,urlList:GetLiveUrlList(request),contestInstance:null]
        }
    }

    def savelivesettings = {
        long last_live_contest_id = BootStrap.global.liveContestID
        BootStrap.global.liveContestID = params.liveContestID.toLong()
        if (params.liveUploadSeconds) {
            BootStrap.global.liveUploadSeconds = params.liveUploadSeconds.toInteger()
        }
        if (params.liveLanguage) {
            BootStrap.global.liveLanguage = params.liveLanguage
        }
        if (BootStrap.global.hasErrors()) {
            BootStrap.global.liveContestID = 0
            BootStrap.global.save()
        } else {
            if (BootStrap.global.save()) {
                if (BootStrap.global.liveContestID != last_live_contest_id) {
                    if (BootStrap.global.liveContestID) {
                        if (BootStrap.global.liveUploadSeconds > 0) {
                            LiveJob.schedule(1000*BootStrap.global.liveUploadSeconds)
                        } else {
                            LiveJob.schedule(1000*Defs.LIVE_UPLOADSECONDS)
                        }
                        println "Live enabled."
                    } else {  
                        quartzScheduler.unscheduleJobs(quartzScheduler.getTriggersOfJob(new JobKey("LiveJob","LiveGroup"))*.key)
                        println "Live disabled."
                    }
                }
            }
        }
        redirect(action:livesettings,params:['newwindow':params.newwindow])
    }
    
    def disablelive = {
        quartzScheduler.unscheduleJobs(quartzScheduler.getTriggersOfJob(new JobKey("LiveJob","LiveGroup"))*.key)
        BootStrap.global.liveContestID = 0
        BootStrap.global.save()
        gpxService.println "Live disabled."
        redirect(action:livesettings,params:['newwindow':params.newwindow])
    }
    
    def enablelive = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            BootStrap.global.liveContestID = session.lastContest.id
            BootStrap.global.save()
            if (BootStrap.global.liveUploadSeconds > 0) {
                LiveJob.schedule(1000*BootStrap.global.liveUploadSeconds)
            } else {
                LiveJob.schedule(1000*Defs.LIVE_UPLOADSECONDS)
            }
            gpxService.println "Live enabled."
        }
        redirect(action:livesettings,params:['newwindow':params.newwindow])
    }
    
    def liverunonce = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            LiveJob.triggerNow([ContestID:session.lastContest.id])
            gpxService.println "Live run once."
        }
        redirect(action:livesettings,params:['newwindow':params.newwindow])
    }
    
    def uploadlivestylesheet = {
        String stylesheet_name = Defs.LIVE_STYLESHEET
        gpxService.printstart "Upload live stylesheet ${stylesheet_name}"
        Map ret = gpxService.UploadStylesheet(stylesheet_name)
        if (!ret.error) {
            flash.message = message(code:'fc.uploaded',args:[stylesheet_name])
            flash.error = false
        } else {
            flash.message = message(code:'fc.notuploaded.destinations',args:[stylesheet_name,ret.failedDestinations]) 
            flash.error = true
        }
        gpxService.printdone flash.message
        redirect(action:livesettings,params:['newwindow':params.newwindow])
    }
    
    def uploadnoliveresults = {
        gpxService.printstart "Upload no live results"
        long live_contest_id = params.liveContestID.toLong()
        if (live_contest_id) {
            Map ret = gpxService.PublishLiveResults(live_contest_id,true)
            if (!ret.error) {
                flash.message = message(code:'fc.uploaded.noliveresults')
                flash.error = false
            } else {
                flash.message = message(code:'fc.notuploaded.noliveresults',args:[ret.failedDestinations])
                flash.error = true
            }
        } else {
            flash.message = message(code:'fc.notuploaded.noliveresults.nocontest')
            flash.error = true
        }
        gpxService.printdone flash.message
        redirect(action:livesettings,params:['newwindow':params.newwindow])
    }
    
    def testmail = {
        try {
            String send_subject = grailsApplication.config.flightcontest.testmail.subject
            if (session?.lastContest) {
                send_subject += " (${session.lastContest.title})"
            }
            mailService.sendMail {
                from grailsApplication.config.flightcontest.mail.from
                to NetTools.EMailList(grailsApplication.config.flightcontest.testmail.to).toArray()
                if (grailsApplication.config.flightcontest.mail.cc) {
                    List cc_list = NetTools.EMailReducedList(grailsApplication.config.flightcontest.mail.cc,grailsApplication.config.flightcontest.testmail.to)
                    if (cc_list) {
                        cc cc_list.toArray()
                    }
                }
                subject send_subject
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
        gpxService.printstart "Test FTP"
        String test_filename = "${UUID.randomUUID().toString()}.txt"
        Map ret = gpxService.SendFTP(grailsApplication.config.flightcontest,"_ftptest", grailsApplication.config.flightcontest.ftp.testsourceurl, test_filename)
        flash.message = "${ret.message}" 
        flash.error = ret.error
        if (!ret.error) {
            String url = "${grailsApplication.config.flightcontest.ftp.contesturl}/_ftptest/${test_filename}"
            gpxService.printdone "'$url' uploaded."
            redirect(action:info,params:[url:url])
        } else {
            gpxService.printerror ret.error
            redirect(action:info)
        }
    }
    
    def start_webmail = {
        fcService.println "Open '${grailsApplication.config.flightcontest.webmail.url}' for '${grailsApplication.config.grails.mail.username}'"
        render(view:"webmail", model:[login_url:         grailsApplication.config.flightcontest.webmail.url,
                                      loginname_name:    grailsApplication.config.flightcontest.webmail.loginname_name,
                                      loginname:         grailsApplication.config.flightcontest.webmail.username,
                                      loginpassword_name:grailsApplication.config.flightcontest.webmail.loginpassword_name,
                                      loginpassword:     grailsApplication.config.flightcontest.webmail.password
                                     ])
    }
    
    def webmail = {
        return [:]
    }

    def selectgeodatafilename = {
        [:]
    }
    
    def loadgeodata = {
        if (!Global.IsCloudFoundryEnvironment()) {
            CheckAndCreateDir(Defs.FCSAVE_FOLDER)
            CheckAndCreateDir(Defs.FCSAVE_FOLDER_GEODATA)
        }
        def file = request.getFile('loadgeofile')
        if (file && !file.empty) {
            String original_filename = file.getOriginalFilename()
            String file_extension = original_filename.substring(original_filename.lastIndexOf('.')).toLowerCase()
            geoDataService.printstart "Import geo data of '$original_filename'"
            geoDataService.println file.getContentType() // "text/xml" 
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_filename = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GEODATA-${uuid}-UPLOAD${file_extension}"
            
            geoDataService.printstart "Upload $original_filename -> $upload_filename"
            file.transferTo(new File(webroot_dir,upload_filename))
            geoDataService.printdone ""

            Map ret = geoDataService.LoadGeoDataFile(file_extension, original_filename, webroot_dir + upload_filename)
            gpxService.DeleteFile(webroot_dir + upload_filename)
            if (ret.ok) {
                geoDataService.printdone ""
                flash.message = ret.msg
                redirect(controller:'global',action:'info')
            } else {
                flash.error = true
                flash.message = ret.msg
                geoDataService.printerror flash.message
                redirect(controller:'global',action:'info')
            }
        } else {
            redirect(controller:'global',action:'info')
        }
    }
    
    
    def cancel = {
        redirect(action:info)
    }

    private List GetLiveUrlList(def request)
    {
        List url_list = []
        url_list += "${ControllerTools.GetFcUrl(request)}/${Defs.ROOT_FOLDER_LIVE}/${Defs.LIVE_FILENAME}" 
        if (BootStrap.global.IsLiveFTPUploadPossible()) {
            url_list += BootStrap.global.GetLiveFTPURL()
        }
        if (BootStrap.global.IsLiveCopyPossible()) {
            grailsApplication.config.flightcontest.live.copy.each { i, url ->
                url_list += url
            }
        }
        return url_list
    }
    
    private void CheckAndCreateDir(String dirName)
    {
        File dir = new File(dirName)
        if (!dir.exists()) {
            dir.mkdir()
        }
    }

}
