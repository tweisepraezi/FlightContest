import java.util.Map;

class RouteController {
    
    def domainService
    def printService
	def fcService
    def gpxService
    def kmlService
    def osmPrintMapService
    
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

    def kmzexport = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            kmlService.printstart "Export '${route.instance.name()}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_kmz_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/KMZ-${uuid}-UPLOAD.gpx"
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
                if (route.instance.contestMapPrintPoints) {
                    route.instance.contestMapPrintPoints += ",${coordroute_instance.title()}"
                } else {
                    route.instance.contestMapPrintPoints = coordroute_instance.title() 
                }
            }
            // set return action
            return [routeInstance:route.instance,mapexportquestionReturnAction:"show",mapexportquestionReturnController:controllerName,mapexportquestionReturnID:params.id]
        } else {
            flash.message = task.message
            redirect(action:'show',id:params.id)
        }
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
                route.instance.contestMapEnroutePhotos = params.contestMapEnroutePhotos == "on"
                route.instance.contestMapEnrouteCanvas = params.contestMapEnrouteCanvas == "on"
                route.instance.contestMapGraticule = params.contestMapGraticule == "on"
                route.instance.contestMapContourLines = params.contestMapContourLines == "on"
                route.instance.contestMapAirfields = params.contestMapAirfields == "on"
                route.instance.contestMapChurches = params.contestMapChurches == "on"
                route.instance.contestMapCastles = params.contestMapCastles == "on"
                route.instance.contestMapChateaus = params.contestMapChateaus == "on"
                route.instance.contestMapWindpowerstations = params.contestMapWindpowerstations == "on"
                route.instance.contestMapPeaks = params.contestMapPeaks == "on"
                route.instance.contestMapAdditionals = params.contestMapAdditionals == "on"
                route.instance.contestMapSpecials = params.contestMapSpecials == "on"
                route.instance.contestMapAirspaces = params.contestMapAirspaces == "on"
                route.instance.contestMapScaleBar = params.contestMapScaleBar == "on"
                route.instance.contestMapNoColorChange = params.contestMapNoColorChange == "on"
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
                route.instance.contestMapPrintLandscape = params.contestMapPrintLandscape == "on"
                route.instance.contestMapPrintA3 = params.contestMapPrintA3 == "on"
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map converter = gpxService.ConvertRoute2GPX(
                    route.instance, webroot_dir + route_gpx_file_name, false, true, false, 
                    [contestMapCircle:route.instance.contestMapCircle,
                     contestMapProcedureTurn:route.instance.contestMapProcedureTurn,
                     contestMapLeg:route.instance.contestMapLeg,
                     contestMapCurvedLeg:route.instance.contestMapCurvedLeg,
                     contestMapTpName:route.instance.contestMapTpName,
                     contestMapEnroutePhotos:route.instance.contestMapEnroutePhotos,
                     contestMapEnrouteCanvas:route.instance.contestMapEnrouteCanvas,
                     contestMapGraticule:route.instance.contestMapGraticule,
                     contestMapContourLines:route.instance.contestMapContourLines,
                     contestMapAirfields:route.instance.contestMapAirfields,
                     contestMapChurches:route.instance.contestMapChurches,
                     contestMapCastles:route.instance.contestMapCastles,
                     contestMapChateaus:route.instance.contestMapChateaus,
                     contestMapWindpowerstations:route.instance.contestMapWindpowerstations,
                     contestMapPeaks:route.instance.contestMapPeaks,
                     contestMapAdditionals:route.instance.contestMapAdditionals,
                     contestMapSpecials:route.instance.contestMapSpecials,
                     contestMapAirspaces:route.instance.contestMapAirspaces,
                     contestMapCenterPoints:route.instance.contestMapCenterPoints,
                     contestMapPrintPoints:route.instance.contestMapPrintPoints,
                     contestMapPrintLandscape:route.instance.contestMapPrintLandscape,
                     contestMapPrintA3:route.instance.contestMapPrintA3,
                     contestMapScaleBar:route.instance.contestMapScaleBar,
                     contestMapNoColorChange:route.instance.contestMapNoColorChange
                    ]
                ) // false - no Print, false - no Points, false - no wrEnrouteSign
                if (converter.ok) {
                    if (route.instance.contestMapOutput == Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP) {
                        gpxService.printdone ""
                        session.gpxviewerReturnAction = 'show'
                        session.gpxviewerReturnController = controllerName
                        session.gpxviewerReturnID = params.id
                        session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                        redirect(controller:'gpx',action:'startgpxviewer',params:[uploadFilename:route_gpx_file_name,originalFilename:route.instance.name(),showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"no",showProfiles:"no"])
                    } else if (route.instance.contestMapOutput == Defs.CONTESTMAPOUTPUT_EXPORTGPX) {
                        String route_file_name = (route.instance.name() + '.gpx').replace(' ',"_")
                        response.setContentType("application/octet-stream")
                        response.setHeader("Content-Disposition", "Attachment;Filename=${route_file_name}")
                        gpxService.Download(webroot_dir + route_gpx_file_name, route_file_name, response.outputStream)
                        gpxService.DeleteFile(route_gpx_file_name)
                        gpxService.printdone ""
                    } else if (route.instance.contestMapOutput == Defs.CONTESTMAPOUTPUT_EXPORTPNGMAP) {
                        String map_png_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/MAP-${uuid}.png"
                        String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                        Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                             routeTitle: route.instance.name(),
                                                             webRootDir: webroot_dir,
                                                             gpxFileName: webroot_dir + route_gpx_file_name,
                                                             pngFileName: webroot_dir + map_png_file_name,
                                                             graticuleFileName: webroot_dir + map_graticule_file_name,
                                                             contestMapAirspacesLayer: contestmap_airspaceslayer
                                                            ])
                        if (r.ok) {
                            String map_file_name = (route.instance.name() + '.png').replace(' ',"_")
                            response.setContentType("application/octet-stream")
                            response.setHeader("Content-Disposition", "Attachment;Filename=${map_file_name}")
                            gpxService.Download(webroot_dir + map_png_file_name, map_file_name, response.outputStream)
                        } else {
                            if (r.message) {
                                flash.message = r.message
                            } else {
                                flash.message = message(code:'fc.contestmap.connectionerror',args:[BootStrap.global.GetPrintServerAPI()])
                            }
                            flash.error = true
                            redirect(action:'show',id:params.id)
                        }
                        gpxService.DeleteFile(route_gpx_file_name)
                        gpxService.DeleteFile(map_png_file_name)
                        gpxService.DeleteFile(map_graticule_file_name)
                        gpxService.printdone ""
                    } else if (route.instance.contestMapOutput == Defs.CONTESTMAPOUTPUT_EXPORTPDFMAP) {
                        String map_png_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/MAP-${uuid}.png"
                        String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                        Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                             routeTitle: route.instance.name(),
                                                             webRootDir: webroot_dir,
                                                             gpxFileName: webroot_dir + route_gpx_file_name,
                                                             pngFileName: webroot_dir + map_png_file_name,
                                                             graticuleFileName: webroot_dir + map_graticule_file_name,
                                                             contestMapAirspacesLayer: contestmap_airspaceslayer
                                                            ])
                        if (r.ok) {
                            gpxService.printstart "Generate PDF"
                            Map ret = printService.printmapRoute(route.instance.contestMapPrintA3, route.instance.contestMapPrintLandscape, webroot_dir + map_png_file_name, GetPrintParams())
                            gpxService.printdone ""
                            if (ret.error) {
                                flash.message = ret.message
                                flash.error = true
                                redirect(action:'show',id:params.id)
                            } else if (ret.content) {
                                printService.WritePDF(response,ret.content,session.lastContest.GetPrintPrefix(),"map-${route.instance.idTitle}",true,route.instance.contestMapPrintA3,route.instance.contestMapPrintLandscape)
                            } else {
                                redirect(action:'show',id:params.id)
                            }
                        } else {
                            if (r.message) {
                                flash.message = r.message
                            } else {
                                flash.message = message(code:'fc.contestmap.connectionerror',args:[BootStrap.global.GetPrintServerAPI()])
                            }
                            flash.error = true
                            redirect(action:'show',id:params.id)
                        }
                        gpxService.DeleteFile(route_gpx_file_name)
                        gpxService.DeleteFile(map_png_file_name)
                        gpxService.DeleteFile(map_graticule_file_name)
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
