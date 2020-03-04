import java.util.Map;

import org.quartz.JobKey

import java.util.zip.*

class RouteController {
    
    def domainService
    def printService
	def fcService
    def gpxService
    def kmlService
    def osmPrintMapService
    def emailService
    def quartzScheduler
    
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
            long next_routeid = route.instance.GetNextID()
            if (next_routeid) {
                redirect(action:show,id:route.instance.id,params:[next:next_routeid])
            } else {
        	    redirect(action:show,id:route.instance.id)
            }
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
        if (params.mapexportquestionReturnAction) {
            redirect(action:params.mapexportquestionReturnAction,controller:params.mapexportquestionReturnController,id:params.mapexportquestionReturnID)
        } else if (session.routeReturnAction) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                long next_routeid = route.instance.GetNextID()
                if (next_routeid) {
                    redirect(action:session.routeReturnAction,controller:session.routeReturnController,id:session.routeReturnID,params:[next:next_routeid])
                } else {
                    redirect(action:session.routeReturnAction,controller:session.routeReturnController,id:session.routeReturnID)
                }
            } else {
                redirect(action:session.routeReturnAction,controller:session.routeReturnController,id:session.routeReturnID)
            }
        } else {
       	    redirect(action:list)
        }
	}
	
    def gotonext = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            long next_id = route.instance.GetNextID()
            long next_id2 = Route.GetNextID2(next_id)
            if (next_id) {
                if (next_id2) {
                    redirect(action:show,id:next_id,params:[next:next_id2])
                } else {
                    redirect(action:show,id:next_id)
                }
            } else {
                redirect(controller:"route",action:"list")
            }
        } else {
            flash.message = test.message
            redirect(controller:"route",action:"list")
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
            import_route = fcService.importFcRoute(RouteFileTools.KML_EXTENSION, session.lastContest, file)
        }
        if (!import_route.found) {
            import_route = fcService.importFcRoute(RouteFileTools.KMZ_EXTENSION, session.lastContest, file)
        }
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
        Map import_params = [foldername:params?.foldername,
                             firstcoordto:params?.firstcoordto == 'on',
                             todirection:params?.todirection.isBigDecimal()?params?.todirection.toBigDecimal():0.0,
                             curved1:params?.curved1 == 'on',
                             curvedstartpos1:params?.curvedstartpos1.isInteger()?params?.curvedstartpos1.toInteger():null,
                             curvedendpos1:params?.curvedendpos1.isInteger()?params?.curvedendpos1.toInteger():null,
                             curved2:params?.curved2 == 'on',
                             curvedstartpos2:params?.curvedstartpos2.isInteger()?params?.curvedstartpos2.toInteger():null,
                             curvedendpos2:params?.curvedendpos2.isInteger()?params?.curvedendpos2.toInteger():null,
                             curved3:params?.curved3 == 'on',
                             curvedstartpos3:params?.curvedstartpos3.isInteger()?params?.curvedstartpos3.toInteger():null,
                             curvedendpos3:params?.curvedendpos3.isInteger()?params?.curvedendpos3.toInteger():null,
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
        redirect(action:selectimporttxt, params:[titlecode:'fc.coordroute.import', routeid:params.id, importSign:turnpoint_sign_data.importsign, lineContent:turnpoint_sign_data.linecontent, importEnrouteData: true])
    }
    
    def importturnpointsign = {
        session.routeReturnAction = "show"
        session.routeReturnController = controllerName
        session.routeReturnID = params.id
        Route route_instance = Route.get(params.id)
        Map turnpoint_sign_data = ImportSign.GetTurnpointSignData(route_instance)
        redirect(action:selectimporttxt, params:[titlecode:turnpoint_sign_data.titlecode, routeid:params.id, importSign:turnpoint_sign_data.importsign, lineContent:turnpoint_sign_data.linecontent, importEnrouteData: false])
    }
    
    def importenroutephoto = {
        session.routeReturnAction = "show"
        session.routeReturnController = controllerName
        session.routeReturnID = params.id
        Route route_instance = Route.get(params.id)
        Map enroute_sign_data = ImportSign.GetEnrouteSignData(route_instance, true)
        redirect(action:selectimporttxt, params:[titlecode:'fc.coordroute.photo.import', routeid:params.id, importSign:enroute_sign_data.importsign, lineContent:enroute_sign_data.linecontent, importEnrouteData: true])
    }
    
    def importenroutecanvas = {
        session.routeReturnAction = "show"
        session.routeReturnController = controllerName
        session.routeReturnID = params.id
        Route route_instance = Route.get(params.id)
        Map enroute_sign_data = ImportSign.GetEnrouteSignData(route_instance, false)
        redirect(action:selectimporttxt, params:[titlecode:'fc.coordroute.canvas.import', routeid:params.id, importSign:enroute_sign_data.importsign, lineContent:enroute_sign_data.linecontent, importEnrouteData: true])
    }
    
    def importenroutesign = {
        Route route_instance = Route.get(params.routeid)
        def file = request.getFile('txtfile')
        Map import_txt = fcService.importSignFile(RouteFileTools.TXT_EXTENSION, route_instance, file, ImportSign.(params.importSign), params.foldername, params.namepraefix)
        if (!import_txt.found) {
            import_txt = fcService.importSignFile(RouteFileTools.KML_EXTENSION, route_instance, file, ImportSign.(params.importSign), params.foldername, params.namepraefix)
        }
        if (!import_txt.found) {
            import_txt = fcService.importSignFile(RouteFileTools.KMZ_EXTENSION, route_instance, file, ImportSign.(params.importSign), params.foldername, params.namepraefix)
        }
        if (!import_txt.found) {
            import_txt = fcService.importSignFile("", route_instance, file, ImportSign.(params.importSign), "", "")
        }
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
    
    def disableprocedureturn = {
        Route route_instance = Route.get(params.id)
        route_instance.useProcedureTurn = false
        route_instance.DisableProcedureTurn()
        redirect(action:"show",controller:"route",id:route_instance.id)
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
            Map converter = gpxService.ConvertRoute2GPX(route.instance, upload_gpx_file_name, false, true, false, false) // false - no Print, true - Points, false - no wrEnrouteSign, false - no gpxExport
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
            Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + upload_gpx_file_name, false, true, true, false) // false - no Print, true - Points, true - wrEnrouteSign, false - no gpxExport
            if (converter.ok) {
                gpxService.printdone ""
                session.gpxviewerReturnAction = 'show'
                session.gpxviewerReturnController = controllerName
                session.gpxviewerReturnID = params.id
                session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                redirect(controller:'gpx',action:'startgpxviewer',params:[uploadFilename:upload_gpx_file_name,originalFilename:route.instance.name(),showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"no",showProfiles:"no",gmApiKey:BootStrap.global.GetGMApiKey()])
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
            Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + upload_gpx_file_name, false, false, true, true) // false - no Print, false - no Points, true - wrEnrouteSign, true - gpxExport
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

    def kmzexport = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            kmlService.printstart "Export '${route.instance.name()}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_kmz_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/KMZ-${uuid}-UPLOAD.kmz"
            Map converter = kmlService.ConvertRoute2KMZ(route.instance, webroot_dir, upload_kmz_file_name, false, true) // false - no Print, true - wrEnrouteSign
            if (converter.ok) {
                String route_file_name = (route.instance.name() + '.kmz').replace(' ',"_")
                response.setContentType("application/octet-stream")
                response.setHeader("Content-Disposition", "Attachment;Filename=${route_file_name}")
                kmlService.Download(webroot_dir + upload_kmz_file_name, route_file_name, response.outputStream)
                kmlService.DeleteFile(upload_kmz_file_name)
                kmlService.printdone ""
            } else {
                flash.error = true
                flash.message = message(code:'fc.kmz.notexported',args:[route.instance.name()])
                kmlService.DeleteFile(upload_kmz_file_name)
                kmlService.printerror flash.message
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

    def mapexportquestion = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            // set all route points
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route.instance,[sort:"id"])) {
                if (route.instance.contestMapCenterPoints) {
                    route.instance.contestMapCenterPoints += ",${coordroute_instance.title()}"
                } else {
                    route.instance.contestMapCenterPoints = coordroute_instance.title() 
                }
                if (!coordroute_instance.type.IsRunwayCoord()) {
                    if (route.instance.contestMapPrintPoints) {
                        route.instance.contestMapPrintPoints += ",${coordroute_instance.title()}"
                    } else {
                        route.instance.contestMapPrintPoints = coordroute_instance.title() 
                    }
                }
            }
            route.instance.contestMapCenterPoints += ","
            route.instance.contestMapPrintPoints += ","
            
            if (BootStrap.global.IsDevelopmentEnvironment()) {
                route.instance.contestMapDevStyle = true
            }
            
            // calculate show_mode
            boolean new_osm_map = true
            boolean break_button = false // unterbrechen
            boolean fetch_button = false // abrufen
            boolean print_button = false // drucken
            boolean discard_button = false // verwerfen
            String webroot_dir = servletContext.getRealPath("/")
            String printjobid_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOBID + route.instance.id + ".txt"
            String printfileid_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTFILEID + route.instance.id + ".txt"
            String printfileid_errorfilename = printfileid_filename + Defs.OSMPRINTMAP_ERR_EXTENSION
            String route_id = ""
            String printjob_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB
            File printjob_file = new File(printjob_filename)
            if (printjob_file.exists()) {
                LineNumberReader printjob_reader = printjob_file.newReader()
                route_id = printjob_reader.readLine()
                printjob_reader.close()
            }
            if (route_id && route_id == route.instance.id.toString()) { // Scheduler job of this route is running
                new_osm_map = false
                break_button = true
                discard_button = true
                def job_key = new JobKey("OsmPrintMapJob",Defs.OSMPRINTMAP_GROUP)
                if (job_key) {
                    if (!quartzScheduler.getTriggersOfJob(job_key)*.key) {
                        gpxService.println "Restart OsmPrintMapJob"
                        redirect(action:'mapfetch',id:params.id)
                    }
                }
            } else if (route_id && route_id != route.instance.id.toString()) { // Scheduler job of another route is running
                new_osm_map = false
            } else if (!route_id && new File(printjobid_filename).exists()) { // No scheduler job is running & Print job  of this route exists
                new_osm_map = false
                fetch_button = true
                discard_button = true
            } else if (new File(printfileid_filename).exists()) { // Result png is ready for pdf generation
                new_osm_map = false
                print_button = true
                discard_button = true
            } else if (new File(printfileid_errorfilename).exists()) { // Job ends not successful
                flash.message = message(code:'fc.contestmap.job.error')
                flash.error = true
                gpxService.DeleteFile(printfileid_errorfilename)
            }
            // quartzScheduler.getTriggersOfJob(new JobKey("OsmPrintMapJob",Defs.OSMPRINTMAP_GROUP))*.key)
            
            // set return action
            return [routeInstance:route.instance, mapexportquestionReturnAction:"show", mapexportquestionReturnController:controllerName, mapexportquestionReturnID:params.id, BreakButton:break_button, FetchButton:fetch_button, PrintButton:print_button, DiscardButton:discard_button, NewOSMMap:new_osm_map, PrintMapsOSM:BootStrap.global.GetPrintServerAPI()==Global.PRINTSERVER_API]
        } else {
            flash.message = task.message
            redirect(action:'show',id:params.id)
        }
    }
    
    def mapexportquestion2 = {
        redirect(action:'mapexportquestion',id:params.id)
    }
    
    def mapgenerate = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapOutput = params.contestMapOutput
                route.instance.contestMapCircle = params.contestMapCircle == "on"
                route.instance.contestMapProcedureTurn = params.contestMapProcedureTurn == "on"
                route.instance.contestMapLeg = params.contestMapLeg == "on"
                route.instance.contestMapCurvedLeg = params.contestMapCurvedLeg == "on"
                route.instance.contestMapTpName = params.contestMapTpName == "on"
                route.instance.contestMapSecretGates = params.contestMapSecretGates == "on"
                route.instance.contestMapEnroutePhotos = params.contestMapEnroutePhotos == "on"
                route.instance.contestMapEnrouteCanvas = params.contestMapEnrouteCanvas == "on"
                route.instance.contestMapScale = params.contestMapScale.toInteger()
                route.instance.contestMapGraticule = params.contestMapGraticule == "on"
                route.instance.contestMapContourLines = params.contestMapContourLines.toInteger()
                route.instance.contestMapMunicipalityNames = params.contestMapMunicipalityNames == "on"
                route.instance.contestMapAirfields = params.contestMapAirfields
                route.instance.contestMapChurches = params.contestMapChurches == "on"
                route.instance.contestMapCastles = params.contestMapCastles == "on"
                route.instance.contestMapChateaus = params.contestMapChateaus == "on"
                route.instance.contestMapPowerlines = params.contestMapPowerlines == "on"
                route.instance.contestMapWindpowerstations = params.contestMapWindpowerstations == "on"
                route.instance.contestMapSmallRoads = params.contestMapSmallRoads == "on"
                route.instance.contestMapPeaks = params.contestMapPeaks == "on"
                route.instance.contestMapAdditionals = params.contestMapAdditionals == "on"
                route.instance.contestMapSpecials = params.contestMapSpecials == "on"
                route.instance.contestMapAirspaces = params.contestMapAirspaces == "on"
                String contestmap_airspaceslayer = params.contestMapAirspacesLayer
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route.instance,[sort:"id"])) {
                    if (params.("${Defs.TurnpointID_ContestMapCenterPoints}${coordroute_instance.title()}") == "on") {
                        if (route.instance.contestMapCenterPoints) {
                            route.instance.contestMapCenterPoints += ",${coordroute_instance.title()}"
                        } else {
                            route.instance.contestMapCenterPoints = coordroute_instance.title()
                        }
                    }
                    if (params.("${Defs.TurnpointID_ContestMapPrintPoints}${coordroute_instance.title()}") == "on") {
                        if (route.instance.contestMapPrintPoints) {
                            route.instance.contestMapPrintPoints += ",${coordroute_instance.title()}"
                        } else {
                            route.instance.contestMapPrintPoints = coordroute_instance.title()
                        }
                    }
                }
                if (route.instance.contestMapCenterPoints) {
                    route.instance.contestMapCenterPoints += ","
                }
                if (route.instance.contestMapPrintPoints) {
                    route.instance.contestMapPrintPoints += ","
                }
                route.instance.contestMapCenterHorizontalPos = params.contestMapCenterHorizontalPos
                route.instance.contestMapCenterVerticalPos = params.contestMapCenterVerticalPos
                route.instance.contestMapPrintLandscape = params.contestMapPrintLandscape == "on"
                route.instance.contestMapPrintSize = params.contestMapPrintSize
                route.instance.contestMapColorChanges = params.contestMapColorChanges == "on"
                route.instance.contestMapDevStyle = params.contestMapDevStyle == "on"
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map converter = gpxService.ConvertRoute2GPX(
                    route.instance, webroot_dir + route_gpx_file_name, false, true, false, false,
                    [contestMapCircle:route.instance.contestMapCircle,
                     contestMapProcedureTurn:route.instance.contestMapProcedureTurn,
                     contestMapLeg:route.instance.contestMapLeg,
                     contestMapCurvedLeg:route.instance.contestMapCurvedLeg,
                     contestMapTpName:route.instance.contestMapTpName,
                     contestMapSecretGates:route.instance.contestMapSecretGates,
                     contestMapEnroutePhotos:route.instance.contestMapEnroutePhotos,
                     contestMapEnrouteCanvas:route.instance.contestMapEnrouteCanvas,
                     contestMapScale:route.instance.contestMapScale,
                     contestMapGraticule:route.instance.contestMapGraticule,
                     contestMapContourLines:route.instance.contestMapContourLines,
                     contestMapMunicipalityNames:route.instance.contestMapMunicipalityNames,
                     contestMapAirfields:route.instance.contestMapAirfields,
                     contestMapChurches:route.instance.contestMapChurches,
                     contestMapCastles:route.instance.contestMapCastles,
                     contestMapChateaus:route.instance.contestMapChateaus,
                     contestMapPowerlines:route.instance.contestMapPowerlines,
                     contestMapWindpowerstations:route.instance.contestMapWindpowerstations,
                     contestMapSmallRoads:route.instance.contestMapSmallRoads,
                     contestMapPeaks:route.instance.contestMapPeaks,
                     contestMapAdditionals:route.instance.contestMapAdditionals,
                     contestMapSpecials:route.instance.contestMapSpecials,
                     contestMapAirspaces:route.instance.contestMapAirspaces,
                     contestMapCenterPoints:route.instance.contestMapCenterPoints,
                     contestMapPrintPoints:route.instance.contestMapPrintPoints,
                     contestMapPrintLandscape:route.instance.contestMapPrintLandscape,
                     contestMapPrintSize:route.instance.contestMapPrintSize,
                     contestMapColorChanges:route.instance.contestMapColorChanges,
                     contestMapDevStyle:route.instance.contestMapDevStyle,
                     contestMapFCStyle:true,
                    ]
                ) // false - no Print, false - no Points, false - no wrEnrouteSign
                if (converter.ok) {
                    if (route.instance.contestMapOutput == Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP) {
                        gpxService.printdone ""
                        session.gpxviewerReturnAction = 'show'
                        session.gpxviewerReturnController = controllerName
                        session.gpxviewerReturnID = params.id
                        session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                        redirect(controller:'gpx',action:'startgpxviewer',params:[uploadFilename:route_gpx_file_name,originalFilename:route.instance.name(),showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"no",showProfiles:"no",gmApiKey:BootStrap.global.GetGMApiKey()])
                    } else if (route.instance.contestMapOutput == Defs.CONTESTMAPOUTPUT_EXPORTGPX) {
                        String route_file_name = (route.instance.name() + '.gpx').replace(' ',"_")
                        response.setContentType("application/octet-stream")
                        response.setHeader("Content-Disposition", "Attachment;Filename=${route_file_name}")
                        gpxService.Download(webroot_dir + route_gpx_file_name, route_file_name, response.outputStream)
                        gpxService.DeleteFile(route_gpx_file_name)
                        gpxService.printdone ""
                    } else if (route.instance.contestMapOutput == Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP) {
                        String printjob_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB
                        File printjob_file = new File(printjob_filename)
                        if (printjob_file.exists()) {
                            flash.message = message(code:'fc.contestmap.previousjobrunningerror',args:[])
                            redirect(action:'mapexportquestion', id:params.id)
                        } else {
                            BufferedWriter printjob_writer = printjob_file.newWriter()
                            printjob_writer << route.instance.id
                            printjob_writer.close()
                            String map_png_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/MAP-${uuid}.png"
                            String world_file_name = "${map_png_file_name}w"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.name(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer: contestmap_airspaceslayer
                                                                ])
                            if (r.ok) {
                                gpxService.printdone ""
                                flash.message = message(code:'fc.contestmap.job.started',args:[])
                                redirect(action:'mapexportquestion', id:params.id)
                            } else {
                                gpxService.DeleteFile(printjob_filename)
                                if (r.message) {
                                    flash.message = r.message
                                } else {
                                    flash.message = message(code:'fc.contestmap.connectionerror',args:[BootStrap.global.GetPrintServerAPI()])
                                }
                                gpxService.printdone ""
                                flash.error = true
                                redirect(action:'show',id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(map_graticule_file_name)
                        }
                        gpxService.DeleteFile(route_gpx_file_name)
                        gpxService.printdone ""
                    }
                } else {
                    flash.error = true
                    flash.message = message(code:'fc.gpx.gacnotconverted',args:[route.instance.name()])
                    gpxService.DeleteFile(route_gpx_file_name)
                    gpxService.printerror flash.message
                    redirect(action:'show',id:params.id)
                }
            } else {
                flash.message = route.message
                redirect(action:list)
            }
        }
    }
    
    def mapbreak = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            String webroot_dir = servletContext.getRealPath("/")
            String route_id = ""
            String printjob_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB
            File printjob_file = new File(printjob_filename)
            if (printjob_file.exists()) {
                LineNumberReader printjob_reader = printjob_file.newReader()
                route_id = printjob_reader.readLine()
                printjob_reader.close()
            }
            if (route_id && route_id == route.instance.id.toString()) { // Scheduler job of this route is running
                quartzScheduler.unscheduleJobs(quartzScheduler.getTriggersOfJob(new JobKey("OsmPrintMapJob",Defs.OSMPRINTMAP_GROUP))*.key)
                gpxService.DeleteFile(printjob_filename)
            }
            redirect(action:'mapexportquestion',id:params.id)
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }
    
    def mapfetch = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            String webroot_dir = servletContext.getRealPath("/")
            String printjob_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB
            String printjobid_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOBID + route.instance.id + ".txt"
            String printfileid_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTFILEID + route.instance.id + ".txt"
            File printjobid_file = new File(printjobid_filename)
            if (printjobid_file.exists()) {
                LineNumberReader printjobid_file_reader = printjobid_file.newReader()
                String printjob_id = printjobid_file_reader.readLine()
                String png_file_name = printjobid_file_reader.readLine()
                boolean print_landscape = printjobid_file_reader.readLine() == 'true'
                boolean print_a3 = printjobid_file_reader.readLine() == 'true'
                boolean print_colorchanges = printjobid_file_reader.readLine() == 'true'
                printjobid_file_reader.close()
                OsmPrintMapJob.schedule(
                    1000*Defs.OSMPRINTMAP_RUNSECONDS,
                    -1, 
                    [(Defs.OSMPRINTMAP_ACTION):Defs.OSMPRINTMAP_ACTION_CHECKJOB, 
                     (Defs.OSMPRINTMAP_JOBFILENAME):printjob_filename,
                     (Defs.OSMPRINTMAP_JOBID):printjob_id,
                     (Defs.OSMPRINTMAP_JOBIDFILENAME):printjobid_filename,
                     (Defs.OSMPRINTMAP_FILEIDFILENAME):printfileid_filename, 
                     (Defs.OSMPRINTMAP_PNGFILENAME):png_file_name,
                     (Defs.OSMPRINTMAP_PRINTLANDSCAPE):print_landscape,
                     (Defs.OSMPRINTMAP_PRINTCOLORCHANGES):print_colorchanges
                    ]
                )
                File printjob_file = new File(printjob_filename)
                BufferedWriter printjob_writer = printjob_file.newWriter()
                printjob_writer << route.instance.id
                printjob_writer.close()
            }
            redirect(action:'mapexportquestion',id:params.id)
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }
    
    def mapdiscard = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            String webroot_dir = servletContext.getRealPath("/")
            String printjob_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB
            String printjobid_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOBID + route.instance.id + ".txt"
            if (new File(printjobid_filename).exists()) {
                quartzScheduler.unscheduleJobs(quartzScheduler.getTriggersOfJob(new JobKey("OsmPrintMapJob",Defs.OSMPRINTMAP_GROUP))*.key)
                gpxService.DeleteFile(printjobid_filename)
                gpxService.DeleteFile(printjob_filename)
            }
            
            String printfileid_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTFILEID + route.instance.id + ".txt"
            File printfileid_file = new File(printfileid_filename)
            if (printfileid_file.exists()) {
                LineNumberReader printfileid_reader = printfileid_file.newReader()
                String map_png_file_name = printfileid_reader.readLine()
                printfileid_reader.close()
                String unpacked_png_file_name = "${map_png_file_name}.png"
                String world_file_name = "${map_png_file_name}w"
                gpxService.DeleteFile(map_png_file_name)
                gpxService.DeleteFile(unpacked_png_file_name)
                gpxService.DeleteFile(world_file_name)
                gpxService.DeleteFile(printfileid_filename)
            }
            
            redirect(action:'show', id:params.id)
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }
    
    def maprefresh = {
        redirect(action:'mapexportquestion',id:params.id)
    }

    def mapprint = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapPrint = params.contestMapPrint
                String webroot_dir = servletContext.getRealPath("/")
                String printfileid_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTFILEID + route.instance.id + ".txt"
                File printfileid_file = new File(printfileid_filename)
                if (printfileid_file.exists()) {
                    LineNumberReader printfileid_file_reader = printfileid_file.newReader()
                    String map_png_file_name = printfileid_file_reader.readLine()
                    String world_file_name = "${map_png_file_name}w"
                    boolean print_landscape = printfileid_file_reader.readLine() == 'true'
                    String print_size = printfileid_file_reader.readLine()
                    printfileid_file_reader.close()
                    if (route.instance.contestMapPrint == Defs.CONTESTMAPPRINT_PNGMAP) {
                        gpxService.printstart "Download PNG"
                        String map_file_name = (route.instance.name() + '.png').replace(' ',"_")
                        response.setContentType("application/octet-stream")
                        response.setHeader("Content-Disposition", "Attachment;Filename=${map_file_name}")
                        gpxService.Download(map_png_file_name, map_file_name, response.outputStream)
                        gpxService.printdone ""
                        //gpxService.DeleteFile(map_png_file_name)
                        //gpxService.DeleteFile(world_file_name)
                        //gpxService.DeleteFile(printfileid_filename)
                    } else if (route.instance.contestMapPrint == Defs.CONTESTMAPPRINT_PNGZIP) {
                        String png_file_name = "Map.png"
                        String map_zip_file_name = map_png_file_name + ".zip"
                        gpxService.printstart "Write ${map_zip_file_name}"
                        ZipOutputStream zip_stream = new ZipOutputStream(new FileOutputStream(map_zip_file_name))
                        write_file_to_zip(zip_stream, png_file_name, map_png_file_name)
                        write_file_to_zip(zip_stream, png_file_name + "w", world_file_name)
                        zip_stream.close()
                        gpxService.printdone ""
                        gpxService.printstart "Download PNGZIP"
                        String map_file_name = (route.instance.name() + '.zip').replace(' ',"_")
                        response.setContentType("application/octet-stream")
                        response.setHeader("Content-Disposition", "Attachment;Filename=${map_file_name}")
                        gpxService.Download(map_zip_file_name, map_file_name, response.outputStream)
                        gpxService.printdone ""
                        //gpxService.DeleteFile(map_zip_file_name)
                        //gpxService.DeleteFile(map_png_file_name)
                        //gpxService.DeleteFile(world_file_name)
                        //gpxService.DeleteFile(printfileid_filename)
                    } else if (route.instance.contestMapPrint == Defs.CONTESTMAPPRINT_PDFMAP) {
                        gpxService.printstart "Generate PDF"
                        Map ret = printService.printmapRoute(print_size, print_landscape, map_png_file_name, GetPrintParams())
                        gpxService.printdone ""
                        if (ret.error) {
                            flash.message = ret.message
                            flash.error = true
                            redirect(action:'show',id:params.id)
                        } else if (ret.content) {
                            if (printService.WritePDF(response, ret.content, session.lastContest.GetPrintPrefix(), "map-${route.instance.idTitle}", true, print_size, print_landscape)) {
                                //gpxService.DeleteFile(map_png_file_name)
                                //gpxService.DeleteFile(world_file_name)
                                //gpxService.DeleteFile(printfileid_filename)
                            }
                        } else {
                            redirect(action:'show',id:params.id)
                        }
                    }
                } else {
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
    
    private void write_file_to_zip(ZipOutputStream zipOutputStream, String zipFileName, String fileName)
    {
        byte[] buffer = new byte[1024]
        FileInputStream kml_file_input_stream = new FileInputStream(fileName)
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName))
        int length
        while ((length = kml_file_input_stream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length)
        }
        zipOutputStream.closeEntry()
        kml_file_input_stream.close()
    }
    
    def mapprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        return [contestInstance:session.lastContest, mapFileName:params.mapFileName]
    }
    
    def sendmail = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            Map route = domainService.GetRoute(params)
            if (route.instance) {
                Map ret = emailService.emailRoute(route.instance, session.printLanguage, grailsAttributes, request)
                flash.message = ret.message
                if (!ret.ok) {
                    flash.error = true
                }
                redirect(action:'show',id:params.id)
            } else {
                flash.message = route.message
                redirect(action:list)
            }
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
}
