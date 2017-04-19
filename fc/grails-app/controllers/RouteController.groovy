import java.util.Map;

class RouteController {
    
    def domainService
    def printService
	def fcService
    def gpxService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
		fcService.printstart "List routes"
        if (session?.lastContest) {
			session.lastContest.refresh()
            def routeList = Route.findAllByContest(session.lastContest,[sort:"id"])
			fcService.printdone "last contest"
            session.planningtesttaskReturnAction = actionName
            session.planningtesttaskReturnController = controllerName
            session.planningtesttaskReturnID = params.id
            session.flighttestReturnAction = actionName
            session.flighttestReturnController = controllerName
            session.flighttestReturnID = params.id
            //session.routeReturnAction = actionName
            //session.routeReturnController = controllerName
            //session.routeReturnID = params.id
            return [routeInstanceList:routeList]
        }
		fcService.printdone ""
        redirect(controller:'contest',action:'start')
    }

    def show = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            session.routeReturnAction = 'list'
            session.routeReturnController = controllerName
            session.routeReturnID = params.id
        	return [routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def edit = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            session.routeReturnAction = 'show'
            session.routeReturnController = controllerName
            session.routeReturnID = params.id
        	return [routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def update = {
        def route = fcService.updateRoute(params) 
        if (route.saved) {
        	flash.message = route.message
        	redirect(action:show,id:route.instance.id)
        } else if (route.instance) {
        	render(view:'edit',model:[routeInstance:route.instance])
        } else {
        	flash.message = route.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
		def route = fcService.createRoute(params,session.lastContest)
        return [routeInstance:route.instance]
    }

    def save = {
        def route = fcService.saveRoute(params,session.lastContest) 
        flash.message = route.message
        if (route.error) {
            flash.error = true
        }
        if (route.saved) {
        	redirect(action:show,id:route.instance.id)
        } else {
            render(view:'create',model:[routeInstance:route.instance])
        }
    }

    def delete = {
        def route = fcService.deleteRoute(params)
        if (route.deleted) {
        	flash.message = route.message
        	redirect(action:list)
        } else if (route.notdeleted) {
        	flash.message = route.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = route.message
        	redirect(action:list)
        }
    }
	
	def cancel = {
        // process return action
        if (session.routeReturnAction) {
            redirect(action:session.routeReturnAction,controller:session.routeReturnController,id:session.routeReturnID)
        } else {
       	    redirect(action:list)
        }
	}
	
	def createcoordroutes = {
        Map route = domainService.GetRoute(params) 
        redirect(controller:'coordRoute',action:'create',params:['route.id':route.instance.id,'routeid':route.instance.id])
	}

    def createsecretcoordroutes = {
        Map route = domainService.GetRoute(params) 
        redirect(controller:'coordRoute',action:'create',params:['secret':true,'route.id':route.instance.id,'routeid':route.instance.id])
    }
    
    def createenroutephoto = {
        Map route = domainService.GetRoute(params)
        redirect(controller:'coordEnroutePhoto',action:'create',params:['route.id':route.instance.id,'routeid':route.instance.id])
    }
    
    def removeallenroutephoto = {
        Map route = domainService.GetRoute(params)
        redirect(controller:'coordEnroutePhoto',action:'removeall',params:['route.id':route.instance.id,'routeid':route.instance.id])
    }

    def createenroutecanvas = {
        Map route = domainService.GetRoute(params)
        redirect(controller:'coordEnrouteCanvas',action:'create',params:['route.id':route.instance.id,'routeid':route.instance.id])
    }
    
    def removeallenroutecanvas = {
        Map route = domainService.GetRoute(params)
        redirect(controller:'coordEnrouteCanvas',action:'removeall',params:['route.id':route.instance.id,'routeid':route.instance.id])
    }

	def selectaflosroute = {
		[contestInstance:session.lastContest]
    }
	
	def importaflosroute = {
		def route = fcService.existAnyAflosRoute(session.lastContest)
		if (route.error) {
			flash.error = route.error
            flash.message = route.message
	        redirect(action:list)
		} else {
	        redirect(action:selectaflosroute)
		}
	}
    
	def importaflosroute2 = {
        def route = fcService.importAflosRoute2(params,session.lastContest,"",false,[]) // false - $curved wird nicht ignoriert 
        if (route.saved) {
            flash.message = route.message
			if (route.error) {
				flash.error = route.error
			}
        } else if (route.error) {
        	flash.error = route.error
            flash.message = route.message
        }
        redirect(action:list)
	}
	
    def selectfcroute = {
        return [:]
    }
    
    def importfcroute = {
        redirect(action:selectfcroute)
    }
    
    def importfcroute2 = {
        def file = request.getFile('routefile')
        Map import_route = fcService.importFcRoute(RouteFileTools.GPX_EXTENSION, session.lastContest, file)
        if (!import_route.found) {
            import_route = fcService.importFcRoute("", session.lastContest, file)
        }
        flash.error = import_route.error
        flash.message = import_route.message
        redirect(action:list)
    }
    
    def selectfileroute = {
        return [:]
    }
    
    def importfileroute = {
        if (session?.lastContest) {
            String line_content = ImportSign.GetImportTxtLineContent(session.lastContest)
            redirect(action:selectfileroute, params:[lineContent:line_content])
        }
    }
    
    def importfileroute2 = {
        def file = request.getFile('routefile')
        Map import_params = [firstcoordto:params?.firstcoordto == 'on',
                             todirection:params?.todirection.isBigDecimal()?params?.todirection.toBigDecimal():0.0,
                             curved:params?.curved == 'on',
                             curvedstartpos:params?.curvedstartpos.isInteger()?params?.curvedstartpos.toInteger():null,
                             curvedendpos:params?.curvedendpos.isInteger()?params?.curvedendpos.toInteger():null,
                             ildg:params?.ildg == 'on', 
                             ildgpos:params?.ildgpos.isInteger()?params?.ildgpos.toInteger():null, 
                             ildgdirection:params?.ildgdirection.isBigDecimal()?params?.ildgdirection.toBigDecimal():0.0,
                             ldg:params?.ldg.toInteger(),
                             ldgdirection:params?.ldgdirection.isBigDecimal()?params?.ldgdirection.toBigDecimal():0.0,
                             autosecret:params?.autosecret == 'on' 
                            ]
        Map import_route = fcService.importFileRoute(RouteFileTools.GPX_EXTENSION, session.lastContest, file, import_params)
        if (!import_route.found) {
            import_route = fcService.importFileRoute(RouteFileTools.KML_EXTENSION, session.lastContest, file, import_params)
        }
        if (!import_route.found) {
            import_route = fcService.importFileRoute(RouteFileTools.KMZ_EXTENSION, session.lastContest, file, import_params)
        }
        if (!import_route.found) {
            import_route = fcService.importFileRoute(RouteFileTools.REF_EXTENSION, session.lastContest, file, import_params)
        }
        if (!import_route.found) {
            import_route = fcService.importFileRoute(RouteFileTools.TXT_EXTENSION, session.lastContest, file, import_params)
        }
        if (!import_route.found) {
            import_route = fcService.importFileRoute("", session.lastContest, file, import_params)
        }
        flash.error = import_route.error
        flash.message = import_route.message
        redirect(action:list)
    }
    
    def selectimporttxt = {
        return [:]
    }
    
    def importcoord = {
        session.routeReturnAction = "show"
        session.routeReturnController = controllerName
        session.routeReturnID = params.id
        Route route_instance = Route.get(params.id)
        Map turnpoint_sign_data = ImportSign.GetRouteCoordData(route_instance)
        redirect(action:selectimporttxt, params:[titlecode:'fc.coordroute.import',routeid:params.id,importSign:turnpoint_sign_data.importsign,lineContent:turnpoint_sign_data.linecontent])
    }
    
    def importturnpointsign = {
        session.routeReturnAction = "show"
        session.routeReturnController = controllerName
        session.routeReturnID = params.id
        Route route_instance = Route.get(params.id)
        Map turnpoint_sign_data = ImportSign.GetTurnpointSignData(route_instance)
        redirect(action:selectimporttxt, params:[titlecode:turnpoint_sign_data.titlecode,routeid:params.id,importSign:turnpoint_sign_data.importsign,lineContent:turnpoint_sign_data.linecontent])
    }
    
    def importenroutephoto = {
        session.routeReturnAction = "show"
        session.routeReturnController = controllerName
        session.routeReturnID = params.id
        Route route_instance = Route.get(params.id)
        Map enroute_sign_data = ImportSign.GetEnrouteSignData(route_instance, true)
        redirect(action:selectimporttxt, params:[titlecode:'fc.coordroute.photo.import',routeid:params.id,importSign:enroute_sign_data.importsign,lineContent:enroute_sign_data.linecontent])
    }
    
    def importenroutecanvas = {
        session.routeReturnAction = "show"
        session.routeReturnController = controllerName
        session.routeReturnID = params.id
        Route route_instance = Route.get(params.id)
        Map enroute_sign_data = ImportSign.GetEnrouteSignData(route_instance, false)
        redirect(action:selectimporttxt, params:[titlecode:'fc.coordroute.canvas.import',routeid:params.id,importSign:enroute_sign_data.importsign,lineContent:enroute_sign_data.linecontent])
    }
    
    def importenroutesign = {
        Route route_instance = Route.get(params.routeid)
        def file = request.getFile('txtfile')
        Map import_txt = fcService.importSignFile(RouteFileTools.TXT_EXTENSION, route_instance, file, ImportSign.(params.importSign))
        flash.error = import_txt.error
        flash.message = import_txt.message
        redirect(action:"show",controller:"route",id:route_instance.id)
    }
    
	def calculateroutelegs = {
        def route = fcService.caculateroutelegsRoute(params) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:show,id:route.instance.id)
        } else if (route.instance) {
            flash.message = route.message
            redirect(action:show,id:route.instance.id)
        } else {
            flash.message = route.message
            redirect(action:list)
        }
	}
    
    def print = {
        Map routes = printService.printRoutes(params,false,false,GetPrintParams()) 
        if (routes.error) {
            flash.message = routes.message
            flash.error = true
            redirect(action:list)
        } else if (routes.found && routes.content) {
            printService.WritePDF(response,routes.content,session.lastContest.GetPrintPrefix(),"routes",true,false,false)
        } else {
            redirect(action:list)
        }
    }
    
    def printroute = {
        Map route = printService.printRoute(params,false,false,GetPrintParams()) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:list)
        } else if (route.content) {
            printService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}",true,false,false)
        } else {
            redirect(action:list)
        }
	}
	
    def printcoordall = {
        Map route = printService.printCoord(params,false,false,GetPrintParams(),"all") 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:list)
        } else if (route.content) {
            printService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-allpoints",true,false,false)
        } else {
            redirect(action:list)
        }
	}
	
    def printcoordtp = {
        Map route = printService.printCoord(params,false,false,GetPrintParams(),"tp") 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:list)
        } else if (route.content) {
            printService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-tppoints",true,false,false)
        } else {
            redirect(action:list)
        }
	}
	
    def copyroute = {
        def route = fcService.copyRoute(params) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:list)
        } else {
            redirect(action:list)
        }
	}
	
    def showprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            return [contestInstance:session.lastContest,routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def showcoordallprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            return [contestInstance:session.lastContest,routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def showcoordtpprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            return [contestInstance:session.lastContest,routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def showofflinemap = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            gpxService.printstart "Show offline map of '${route.instance.name()}'"
            String uuid = UUID.randomUUID().toString()
            String upload_gpx_file_name = "${GpxService.GPXDATA}-${uuid}"
            Map converter = gpxService.ConvertRoute2GPX(route.instance, upload_gpx_file_name, false, true, false) // false - no Print, true - Points, false - no wrEnrouteSign
            if (converter.ok) {
                gpxService.printdone ""
                session.gpxviewerReturnAction = 'show'
                session.gpxviewerReturnController = controllerName
                session.gpxviewerReturnID = params.id
                session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                redirect(controller:'gpx',action:'startofflineviewer',params:[uploadFilename:upload_gpx_file_name,originalFilename:route.instance.name(),showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"no",showProfiles:"no",showZoom:"yes"])
            } else {
                flash.error = true
                flash.message = message(code:'fc.gpx.gacnotconverted',args:[route.instance.name()])
                gpxService.DeleteFile(upload_gpx_file_name)
                gpxService.printerror flash.message
                redirect(action:'show',id:params.id)
            }
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def showmap = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            gpxService.printstart "Show map of '${route.instance.name()}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GPX-${uuid}-UPLOAD.gpx"
            Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + upload_gpx_file_name, false, true, true) // false - no Print, true - Points, true - wrEnrouteSign
            if (converter.ok) {
                gpxService.printdone ""
                session.gpxviewerReturnAction = 'show'
                session.gpxviewerReturnController = controllerName
                session.gpxviewerReturnID = params.id
                session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                redirect(controller:'gpx',action:'startgpxviewer',params:[uploadFilename:upload_gpx_file_name,originalFilename:route.instance.name(),showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"no",showProfiles:"no"])
            } else {
                flash.error = true
                flash.message = message(code:'fc.gpx.gacnotconverted',args:[route.instance.name()])
                gpxService.DeleteFile(upload_gpx_file_name)
                gpxService.printerror flash.message
                redirect(action:'show',id:params.id)
            }
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def gpxexport = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            gpxService.printstart "Export '${route.instance.name()}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GPX-${uuid}-UPLOAD.gpx"
            Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + upload_gpx_file_name, false, false, true) // false - no Print, false - no Points, true - wrEnrouteSign
            if (converter.ok) {
                String route_file_name = (route.instance.name() + '.gpx').replace(' ',"_")
                response.setContentType("application/octet-stream")
                response.setHeader("Content-Disposition", "Attachment;Filename=${route_file_name}")
                gpxService.Download(webroot_dir + upload_gpx_file_name, route_file_name, response.outputStream)
                gpxService.DeleteFile(upload_gpx_file_name)
                gpxService.printdone ""
            } else {
                flash.error = true
                flash.message = message(code:'fc.gpx.gacnotconverted',args:[route.instance.name()])
                gpxService.DeleteFile(upload_gpx_file_name)
                gpxService.printerror flash.message
                redirect(action:'show',id:params.id)
            }
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def aflosrefexport = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            gpxService.printstart "Export '${route.instance.name()}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_ref_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/REF-${uuid}-UPLOAD.ref"
            Map converter = AflosTools.ConvertRoute2REF(route.instance, webroot_dir + upload_ref_file_name)
            if (converter.ok) {
                String route_file_name = (route.instance.mark + '.ref').replace(' ',"_")
                response.setContentType("application/octet-stream")
                response.setHeader("Content-Disposition", "Attachment;Filename=${route_file_name}")
                gpxService.Download(webroot_dir + upload_ref_file_name, route_file_name, response.outputStream)
                gpxService.DeleteFile(upload_ref_file_name)
                if (converter.modified) {
                    gpxService.printdone "Modified."
                } else {
                    gpxService.printdone ""
                }
            } else {
                flash.error = true
                flash.message = message(code:'fc.gpx.gacnotconverted',args:[route.instance.name()])
                gpxService.DeleteFile(upload_ref_file_name)
                if (converter.modified) {
                    gpxService.printerror "${flash.message}. Modified."
                } else{
                    gpxService.printerror flash.message
                }
                redirect(action:'show',id:params.id)
            }
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def sendmail = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            Map route = domainService.GetRoute(params)
            if (route.instance) {
                String email_to = route.instance.EMailAddress()
                gpxService.printstart "Send mail of '${route.instance.name()}' to '${email_to}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String upload_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GPX-${uuid}-UPLOAD.gpx"
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + upload_gpx_file_name, true, true, true) // true - Print, true - Points, true - wrEnrouteSign
                if (converter.ok) {
                    Map email = GetEMailBody(session.lastContest, route.instance)
                    
                    String job_file_name = "${Defs.ROOT_FOLDER_JOBS}/JOB-${uuid}.job"
                    BufferedWriter job_writer = null
                    try {
                        // create email job
                        File job_file = new File(webroot_dir + job_file_name)
                        job_writer = job_file.newWriter()
                        gpxService.WriteLine(job_writer,session.lastContest.contestUUID) // 1
                        gpxService.WriteLine(job_writer,"file:${webroot_dir + upload_gpx_file_name}") // 2
                        gpxService.WriteLine(job_writer,"${route.instance.GetFileName()}.gpx") // 3
                        gpxService.WriteLine(job_writer,session.lastContest.contestUUID) // 4
                        gpxService.WriteLine(job_writer,"http://localhost:8080/fc/gpx/startroutegpxviewer?id=${route.instance.id}&printLanguage=${session.printLanguage}&lang=${session.printLanguage}&showProfiles=yes&gpxShowPoints=${HTMLFilter.GetStr(converter.gpxShowPoints)}") // 5
                        gpxService.WriteLine(job_writer,"${route.instance.GetFileName()}.htm") // 6
                        gpxService.WriteLine(job_writer,email_to) // 7
                        gpxService.WriteLine(job_writer,route.instance.GetEMailTitle()) // 8
                        gpxService.WriteLine(job_writer,email.body) // 9
                        gpxService.WriteLine(job_writer,email.link) // 10
                        gpxService.WriteLine(job_writer,"0") // 11
                        gpxService.WriteLine(job_writer,webroot_dir + upload_gpx_file_name) // 12
                        job_writer.close()
                        job_writer = null
                        
                        // set sending link
                        //session.lastContest.routeLink = Global.EMAIL_SENDING
                        //session.lastContest.save()
                        
                        flash.message = message(code:'fc.net.mail.prepared',args:[email_to])
                        gpxService.println "Job '${job_file_name}' created." 
                    } catch (Exception e) {
                        gpxService.println "Error: '${job_file_name}' could not be created ('${e.getMessage()}')"
                        if (job_writer) {
                            job_writer.close()
                        } 
                    }
                    gpxService.printdone ""
                    redirect(action:'show',id:params.id)
                } else {
                    flash.error = true
                    flash.message = message(code:'fc.gpx.gacnotconverted',args:[route.instance.name()])
                    gpxService.DeleteFile(upload_gpx_file_name)
                    gpxService.printerror flash.message
                    redirect(action:'show',id:params.id)
                }

            } else {
                flash.message = route.message
                redirect(action:list)
            }
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
    private Map GetEMailBody(Contest contestInstance, Route routeInstance)
    {
        Map ret = [:]
        String s = ""
        
        String contest_dir = "${grailsApplication.config.flightcontest.ftp.contesturl}/${contestInstance.contestUUID}"
        
        String view_url = "${contest_dir}/${routeInstance.GetFileName()}.htm"
        s += """<p>Strecke (Web-Browser): <a href="${view_url}">${view_url}</a></p>"""
        
        String gpx_url = "${contest_dir}/${routeInstance.GetFileName()}.gpx"
        s += """<p>Strecke (GPX Viewer): <a href="${gpx_url}">${gpx_url}</a></p>"""
        
        ret += [body:s,link:view_url]
        
        return ret
    }

	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
}
