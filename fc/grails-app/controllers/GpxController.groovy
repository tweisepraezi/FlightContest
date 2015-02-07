class GpxController 
{

    def gpxService
    
	def selectgpxfilename = {
		[:]
    }
	
    def selectgacfilename = {
        [:]
    }
    
	def showmapgpx = {
		def file = request.getFile('loadgpxfile')
		if (file && !file.empty) {
			String original_filename = file.getOriginalFilename()
			gpxService.printstart "Process '$original_filename'"
			gpxService.println file.getContentType() // "text/xml" 
			if (original_filename.toLowerCase().endsWith('.gpx')) {
				String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
				String upload_filename = "gpxupload/GPX-${uuid}-UPLOAD.gpx"
                gpxService.printstart "Upload $original_filename -> $upload_filename"
                file.transferTo(new File(webroot_dir,upload_filename))
                gpxService.printdone ""
                gpxService.printdone ""
                session.gpxShowPoints = null
                redirect(action:'startgpxviewer',params:[uploadFilename:upload_filename,originalFilename:original_filename,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"yes",showProfiles:"yes"])
			} else {
				flash.error = true
				flash.message = message(code:'fc.gac.gpxfileerror',args:[original_filename])
				gpxService.printerror flash.message
				redirect(controller:'global',action:'info')
			}
		} else {
			redirect(controller:'global',action:'info')
		}
	}
    
    def showmapgac = {
        def file = request.getFile('loadgacfile')
        if (file && !file.empty) {
            String original_filename = file.getOriginalFilename()
            gpxService.printstart "Process '$original_filename'"
            gpxService.println file.getContentType() // "text/xml"
            if (original_filename.toLowerCase().endsWith('.gac')) {
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String upload_file_name = "GAC-${uuid}-UPLOAD.gac"
                String upload_gpx_file_name = "gpxupload/GPX-${uuid}-UPLOAD.gpx"
                
                gpxService.printstart "Upload $original_filename -> $upload_file_name"
                file.transferTo(new File(upload_file_name))
                gpxService.printdone ""

                if (gpxService.ConvertGAC2GPX(upload_file_name, webroot_dir + upload_gpx_file_name)) {
                    gpxService.DeleteFile(upload_file_name)
                    gpxService.printdone ""
                    session.gpxShowPoints = null
                    redirect(action:'startgpxviewer',params:[uploadFilename:upload_gpx_file_name,originalFilename:original_filename,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"yes",showProfiles:"yes"])
                } else {
                    flash.error = true
                    flash.message = message(code:'fc.gpx.gacnotconverted',args:[upload_file_name])
                    gpxService.DeleteFile(upload_file_name)
                    gpxService.DeleteFile(upload_gpx_file_name)
                    gpxService.printdone flash.message
                    redirect(controller:'global',action:'info')
                }
            } else {
                flash.error = true
                flash.message = message(code:'fc.gac.gacfileerror',args:[original_filename])
                gpxService.printerror flash.message
                redirect(controller:'global',action:'info')
            }
        } else {
            redirect(controller:'global',action:'info')
        }
    }
	
    def startgpxviewer = {
        if (session.gpxShowPoints) {
            render(view:"gpxviewer",model:[fileName:params.uploadFilename,originalFilename:params.originalFilename,testID:params.testID,showLanguage:params.showLanguage,showCancel:params.showCancel,showProfiles:params.showProfiles,gpxShowPoints:HTMLFilter.GetList(session.gpxShowPoints)])
        } else {
            render(view:"gpxviewer",model:[fileName:params.uploadFilename,originalFilename:params.originalFilename,testID:params.testID,showLanguage:params.showLanguage,showCancel:params.showCancel,showProfiles:params.showProfiles])
        }
    }
    
    def gpxviewer = {
        return [:]
    }
    
    def startftpgpxviewer = {
        Test test_instance = Test.get(params.id)
        if (test_instance) {
            session.printLanguage = params.printLanguage
            String file_name = "${test_instance.GetFileName(ResultType.Flight)}.gpx"
            String original_filename = test_instance.GetEMailTitle(ResultType.Flight).encodeAsHTML()
            render(view:"ftpgpxviewer",model:[fileName:file_name,originalFilename:original_filename,printLanguage:params.printLanguage,lang:params.printLanguage,showProfiles:params.showProfiles,gpxShowPoints:HTMLFilter.GetList(params.gpxShowPoints)])
        }
        return [:]
    }

    def ftpgpxviewer = {
        return [:]
    }

    def deletegpxfile = {
        String file_name = servletContext.getRealPath("/") + params.filename
        gpxService.printstart "deletegpxfile"
        gpxService.DeleteFile(file_name)
        session.gpxShowPoints = null
        gpxService.printdone ""
    }
    
	def cancel = {
        if (session.gpxviewerReturnAction) {
            long next_testid = 0
            if (params.testid) {
                next_testid = Test.GetNext2TestID(params.testid.toLong(),ResultType.Flight)
            }
            if (next_testid) {
                redirect(action:session.gpxviewerReturnAction,controller:session.gpxviewerReturnController,id:session.gpxviewerReturnID,params:[next:next_testid])
            } else {
                redirect(action:session.gpxviewerReturnAction,controller:session.gpxviewerReturnController,id:session.gpxviewerReturnID)
            }
        } else {
		    redirect(controller:'global',action:'info')
        }
	}
}
