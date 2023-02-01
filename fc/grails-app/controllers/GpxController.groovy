class GpxController 
{

    def gpxService
    
    def selectloggerfilename = {
        if (session?.lastContest) {
            [contestInstance:session.lastContest]
        } else {
            [contestInstance:null]
        }
    }
    
	def showmaploggerdata = {
		def file = request.getFile('loadloggerfile')
		if (file && !file.empty) {
			String original_filename = file.getOriginalFilename()
            String file_extension = original_filename.substring(original_filename.lastIndexOf('.')).toLowerCase()
            if (params.offlinemap) {
                gpxService.printstart "showmaploggerdata: Show offline map of '$original_filename'"
            } else if (params.gpxdownload) {
                gpxService.printstart "showmaploggerdata: Download gpx of '$original_filename'"
            } else { // params.onlinemap
                gpxService.printstart "showmaploggerdata: Show map of '$original_filename'"
            }
			gpxService.println file.getContentType() // "text/xml" 
			String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
			String upload_filename = "${Defs.ROOT_FOLDER_GPXUPLOAD}/LOGGERDATA-${uuid}-UPLOAD${file_extension}"
            String gpx_filename = "${Defs.ROOT_FOLDER_GPXUPLOAD}/LOGGERDATA-${uuid}-UPLOAD.gpx"
            
            gpxService.printstart "Upload $original_filename -> $upload_filename"
            file.transferTo(new File(webroot_dir,upload_filename))
            gpxService.printdone ""
            
            Route route_instance = null
            if (params.routeid && params.routeid.isLong()) {
                route_instance = Route.get(params.routeid)
            }
            
            String notconverted_message = ""
            Map converter = [:]
            switch (file_extension) {
                case LoggerFileTools.GPX_EXTENSION:
                    LoggerFileTools.RemoveExistingBOM(webroot_dir + gpx_filename)
                    if (route_instance) {
                        converter = gpxService.AddRoute2GPX(route_instance, webroot_dir + gpx_filename)
                    } else {
                        converter = [ok:true]
                    }
                    break
                case LoggerFileTools.KML_EXTENSION:
                    LoggerFileTools.RemoveExistingBOM(webroot_dir + upload_filename)
                    converter = gpxService.ConvertKML2GPX(webroot_dir + upload_filename, webroot_dir + gpx_filename, route_instance, false)
                    if (converter.ok) {
                        gpxService.DeleteFile(upload_filename)
                    }
                    notconverted_message = message(code:'fc.gpx.gacnotconverted',args:[original_filename])
                    break
                case LoggerFileTools.KMZ_EXTENSION:
                    converter = gpxService.ConvertKML2GPX(webroot_dir + upload_filename, webroot_dir + gpx_filename, route_instance, true)
                    if (converter.ok) {
                        gpxService.DeleteFile(upload_filename)
                    }
                    notconverted_message = message(code:'fc.gpx.gacnotconverted',args:[original_filename])
                    break
                case LoggerFileTools.GAC_EXTENSION:
                    converter = gpxService.ConvertGAC2GPX(webroot_dir + upload_filename, webroot_dir + gpx_filename, route_instance)
                    if (converter.ok) {
                        gpxService.DeleteFile(upload_filename)
                    }
                    notconverted_message = message(code:'fc.gpx.gacnotconverted',args:[original_filename])
                    break
                case LoggerFileTools.IGC_EXTENSION:
                    converter = gpxService.ConvertIGC2GPX(webroot_dir + upload_filename, webroot_dir + gpx_filename, route_instance)
                    if (converter.ok) {
                        gpxService.DeleteFile(upload_filename)
                    }
                    notconverted_message = message(code:'fc.gpx.gacnotconverted',args:[original_filename])
                    break
                case LoggerFileTools.NMEA_EXTENSION:
                    converter = gpxService.ConvertNMEA2GPX(webroot_dir + upload_filename, webroot_dir + gpx_filename, route_instance)
                    if (converter.ok) {
                        gpxService.DeleteFile(upload_filename)
                    }
                    notconverted_message = message(code:'fc.gpx.gacnotconverted',args:[original_filename])
                    break
                default:
                    notconverted_message = message(code:'fc.loggerdata.loggerfileerror',args:[original_filename])
                    break
            }
            
            if (converter.ok) {
                gpxService.printdone ""
                session.gpxShowPoints = null
                if (route_instance) {
                    session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                } else {
                    session.gpxShowPoints = HTMLFilter.GetStr(gpxService.GetShowPoints(webroot_dir + gpx_filename, false)) // false - no wrEnrouteSign
                }
                if (params.offlinemap) {
                    redirect(action:'startofflineviewer',params:[uploadFilename:gpx_filename,originalFilename:original_filename,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"yes",showProfiles:"yes",showZoom:"yes",showPoints:"yes"])
                } else if (params.gpxdownload) {
                    gpxService.printstart "Download GPX"
                    String gpx_filename2 = original_filename.replace(' ',"_")
                    if (!gpx_filename2.endsWith('.gpx')) {
                        gpx_filename2 += '.gpx'
                    }
                    response.setContentType("application/octet-stream")
                    response.setHeader("Content-Disposition", "Attachment;Filename=${gpx_filename2}")
                    gpxService.Download(webroot_dir + gpx_filename, webroot_dir + gpx_filename2, response.outputStream)
                    gpxService.printdone ""
                    gpxService.DeleteFile(gpx_filename)
                } else { // params.onlinemap
                    redirect(action:'startgpxviewer',params:[uploadFilename:gpx_filename,originalFilename:original_filename,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"yes",showProfiles:"yes",gmApiKey:BootStrap.global.GetGMApiKey()])
                }
            } else {
                flash.error = true
                flash.message = notconverted_message
                gpxService.DeleteFile(upload_filename)
                gpxService.DeleteFile(gpx_filename)
                gpxService.printdone flash.message
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
            } else { // params.onlinemap
                gpxService.printstart "Show map of '$original_filename'"
            }
            gpxService.println file.getContentType() // "text/xml"
            if (original_filename.toLowerCase().endsWith('.gac')) {
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String upload_file_name = "GAC-${uuid}-UPLOAD.gac"
                String upload_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GPX-${uuid}-UPLOAD.gpx"
                
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
                        session.gpxShowPoints = HTMLFilter.GetStr(gpxService.GetShowPoints(webroot_dir + upload_gpx_file_name, false)) // false - no wrEnrouteSign
                    }
                    if (params.offlinemap) {
                        redirect(action:'startofflineviewer',params:[uploadFilename:upload_gpx_file_name,originalFilename:original_filename,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"yes",showProfiles:"yes",showZoom:"yes",showPoints:"yes"])
                    } else { // params.onlinemap
                        redirect(action:'startgpxviewer',params:[uploadFilename:upload_gpx_file_name,originalFilename:original_filename,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"yes",showProfiles:"yes",gmApiKey:BootStrap.global.GetGMApiKey()])
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
        gpxService.printstart "startofflineviewer ($params.uploadFilename, $params.originalFilename, TestID:$params.testID, Lang:$params.showLanguage, Cancel:$params.showCancel, Profiles:$params.showProfiles, Zoom:$params.showZoom, Points:$params.showPoints, showCoord:$params.showCoord)"
        if (session.gpxShowPoints) {
            render(view:"offlineviewer",model:[fileName:params.uploadFilename,originalFilename:params.originalFilename,testID:params.testID,showLanguage:params.showLanguage,showCancel:params.showCancel,showProfiles:params.showProfiles,showZoom:params.showZoom,showPoints:params.showPoints,showCoord:params.showCoord,gpxShowPoints:HTMLFilter.GetList(session.gpxShowPoints)])
        } else {
            render(view:"offlineviewer",model:[fileName:params.uploadFilename,originalFilename:params.originalFilename,testID:params.testID,showLanguage:params.showLanguage,showCancel:params.showCancel,showProfiles:params.showProfiles,showZoom:params.showZoom,showPoints:params.showPoints])
        }
        gpxService.printdone ""
    }
    
    def offlineviewer = {
        return [:]
    }
    
    def startgpxviewer = {
        gpxService.printstart "startgpxviewer ($params.uploadFilename, $params.originalFilename, TestID:$params.testID, Lang:$params.showLanguage, Cancel:$params.showCancel, Profiles:$params.showProfiles, Zoom:$params.showZoom, Points:$params.showPoints, gmApiKey:$params.gmApiKey)"
        if (session.gpxShowPoints) {
            render(view:"gpxviewer",model:[fileName:params.uploadFilename,originalFilename:params.originalFilename,testID:params.testID,showLanguage:params.showLanguage,showCancel:params.showCancel,showProfiles:params.showProfiles,showZoom:params.showZoom,showPoints:params.showPoints,gmApiKey:params.gmApiKey,gpxShowPoints:HTMLFilter.GetList(session.gpxShowPoints)])
        } else {
            render(view:"gpxviewer",model:[fileName:params.uploadFilename,originalFilename:params.originalFilename,testID:params.testID,showLanguage:params.showLanguage,showCancel:params.showCancel,showProfiles:params.showProfiles,showZoom:params.showZoom,showPoints:params.showPoints,gmApiKey:params.gmApiKey])
        }
        gpxService.printdone ""
    }
    
    def gpxviewer = {
        return [:]
    }
    
    def ftpgpxviewer = {
        return [:]
    }

    def startftpgpxviewer = {
        session.printLanguage = params.printLanguage
        String original_filename= params.originalFilename.encodeAsHTML()
        render(view:"ftpgpxviewer",model:[fileName:params.fileName,originalFilename:original_filename,printLanguage:params.printLanguage,lang:params.printLanguage,showProfiles:params.showProfiles,gpxShowPoints:HTMLFilter.GetList(params.gpxShowPoints)])
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
        String time_zone = "00:00"
        CoordPresentation coord_presentation = CoordPresentation.DEGREEMINUTE
        if (session.lastContest) {
            time_zone = session.lastContest.timeZone
            coord_presentation = session.lastContest.coordPresentation
        }
        response.contentType = "text/xml"
        response.outputStream << gpxService.ConvertGPX2XYXML(params.gpxFileName, params.maxX.toInteger(), params.maxY.toInteger(), params.centerLat, params.centerLon, params.radius, params.moveDir, time_zone, coord_presentation)
        gpxService.printdone ""
    }
    
	def cancel = {
        if (session.gpxviewerReturnAction) {
            redirect(action:session.gpxviewerReturnAction,controller:session.gpxviewerReturnController,id:session.gpxviewerReturnID)
        } else {
		    redirect(controller:'global',action:'info')
        }
	}
}
