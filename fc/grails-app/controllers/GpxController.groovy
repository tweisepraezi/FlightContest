class GpxController 
{

    def gpxService
    
	def selectgpxfilename = {
        if (session?.lastContest) {
            [contestInstance:session.lastContest]
        } else {
            [contestInstance:null]
        }
    }
	
    def selectgacfilename = {
        if (session?.lastContest) {
            [contestInstance:session.lastContest]
        } else {
            [contestInstance:null]
        }
    }
    
	def showmapgpx = {
		def file = request.getFile('loadgpxfile')
		if (file && !file.empty) {
			String original_filename = file.getOriginalFilename()
            if (params.offlinemap) {
                gpxService.printstart "Show offline map of '$original_filename'"
            } else {
                gpxService.printstart "Show map of '$original_filename'"
            }
			gpxService.println file.getContentType() // "text/xml" 
			if (original_filename.toLowerCase().endsWith('.gpx')) {
				String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
				String upload_filename = "gpxupload/GPX-${uuid}-UPLOAD.gpx"
                gpxService.printstart "Upload $original_filename -> $upload_filename"
                file.transferTo(new File(webroot_dir,upload_filename))
                gpxService.printdone ""
                
                session.gpxShowPoints = null
                Route route_instance = null
                if (params.routeid && params.routeid.isLong()) {
                    route_instance = Route.get(params.routeid)
                }
                if (route_instance) {
                    Map converter = gpxService.AddRoute2GPX(route_instance, webroot_dir + upload_filename)
                    session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                } else {
                    session.gpxShowPoints = HTMLFilter.GetStr(gpxService.GetShowPoints(webroot_dir + upload_filename))
                }

                gpxService.printdone ""
                if (params.offlinemap) {
                    redirect(action:'startofflineviewer',params:[uploadFilename:upload_filename,originalFilename:original_filename,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"yes",showProfiles:"yes",showZoom:"yes",showPoints:"yes"])
                } else {
                    redirect(action:'startgpxviewer',params:[uploadFilename:upload_filename,originalFilename:original_filename,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"yes",showProfiles:"yes"])
                }
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
            if (params.offlinemap) {
                gpxService.printstart "Show offline map of '$original_filename'"
            } else {
                gpxService.printstart "Show map of '$original_filename'"
            }
            gpxService.println file.getContentType() // "text/xml"
            if (original_filename.toLowerCase().endsWith('.gac')) {
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String upload_file_name = "GAC-${uuid}-UPLOAD.gac"
                String upload_gpx_file_name = "gpxupload/GPX-${uuid}-UPLOAD.gpx"
                
                gpxService.printstart "Upload $original_filename -> $upload_file_name"
                file.transferTo(new File(upload_file_name))
                gpxService.printdone ""
                
                Route route_instance = null
                if (params.routeid && params.routeid.isLong()) {
                    route_instance = Route.get(params.routeid)
                }

                Map converter = gpxService.ConvertGAC2GPX(upload_file_name, webroot_dir + upload_gpx_file_name, route_instance)
                if (converter.ok) {
                    gpxService.DeleteFile(upload_file_name)
                    gpxService.printdone ""
                    session.gpxShowPoints = null
                    if (route_instance) {
                        session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                    } else {
                        session.gpxShowPoints = HTMLFilter.GetStr(gpxService.GetShowPoints(webroot_dir + upload_filename))
                    }
                    if (params.offlinemap) {
                        redirect(action:'startofflineviewer',params:[uploadFilename:upload_gpx_file_name,originalFilename:original_filename,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"yes",showProfiles:"yes",showZoom:"yes",showPoints:"yes"])
                    } else {
                        redirect(action:'startgpxviewer',params:[uploadFilename:upload_gpx_file_name,originalFilename:original_filename,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"yes",showProfiles:"yes"])
                    }
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
	
    def startofflineviewer = {
        gpxService.printstart "startofflineviewer ($params.uploadFilename, $params.originalFilename, TestID:$params.testID, Lang:$params.showLanguage, Cancel:$params.showCancel, Profiles:$params.showProfiles, Zoom:$params.showZoom, Points:$params.showPoints)"
        if (session.gpxShowPoints) {
            render(view:"offlineviewer",model:[fileName:params.uploadFilename,originalFilename:params.originalFilename,testID:params.testID,showLanguage:params.showLanguage,showCancel:params.showCancel,showProfiles:params.showProfiles,showZoom:params.showZoom,showPoints:params.showPoints,gpxShowPoints:HTMLFilter.GetList(session.gpxShowPoints)])
        } else {
            render(view:"offlineviewer",model:[fileName:params.uploadFilename,originalFilename:params.originalFilename,testID:params.testID,showLanguage:params.showLanguage,showCancel:params.showCancel,showProfiles:params.showProfiles,showZoom:params.showZoom,showPoints:params.showPoints])
        }
        gpxService.printdone ""
    }
    
    def offlineviewer = {
        return [:]
    }
    
    def startgpxviewer = {
        gpxService.printstart "startgpxviewer ($params.uploadFilename, $params.originalFilename, TestID:$params.testID, Lang:$params.showLanguage, Cancel:$params.showCancel, Profiles:$params.showProfiles, Zoom:$params.showZoom, Points:$params.showPoints)"
        if (session.gpxShowPoints) {
            render(view:"gpxviewer",model:[fileName:params.uploadFilename,originalFilename:params.originalFilename,testID:params.testID,showLanguage:params.showLanguage,showCancel:params.showCancel,showProfiles:params.showProfiles,showZoom:params.showZoom,showPoints:params.showPoints,gpxShowPoints:HTMLFilter.GetList(session.gpxShowPoints)])
        } else {
            render(view:"gpxviewer",model:[fileName:params.uploadFilename,originalFilename:params.originalFilename,testID:params.testID,showLanguage:params.showLanguage,showCancel:params.showCancel,showProfiles:params.showProfiles,showZoom:params.showZoom,showPoints:params.showPoints])
        }
        gpxService.printdone ""
    }
    
    def gpxviewer = {
        return [:]
    }
    
    def startroutegpxviewer = {
        Route route_instance = Route.get(params.id)
        if (route_instance) {
            session.printLanguage = params.printLanguage
            String file_name = "${route_instance.GetFileName()}.gpx"
            String original_filename = route_instance.GetEMailTitle().encodeAsHTML()
            render(view:"ftpgpxviewer",model:[fileName:file_name,originalFilename:original_filename,printLanguage:params.printLanguage,lang:params.printLanguage,showProfiles:params.showProfiles,gpxShowPoints:HTMLFilter.GetList(params.gpxShowPoints)])
        }
        return [:]
    }

    def ftpgpxviewer = {
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

    def deletegpxfile = {
        String file_name = params.filename
        gpxService.printstart "deletegpxfile $file_name"
        gpxService.DeleteFile(file_name) 
        session.gpxShowPoints = null
        gpxService.printdone ""
    }
    
    def calculatexy = {
        gpxService.printstart "calculatexy ($params.gpxFileName, $params.maxX, $params.maxY, $params.centerLat, $params.centerLon, $params.radius, $params.moveDir)"
        response.contentType = "text/xml"
        response.outputStream << gpxService.ConvertGPX2XYXML(params.gpxFileName, params.maxX.toInteger(), params.maxY.toInteger(), params.centerLat, params.centerLon, params.radius, params.moveDir, session.lastContest.timeZone, session.lastContest.coordPresentation)
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
