import java.util.Map
import org.quartz.JobKey
import java.util.zip.*
import org.springframework.web.multipart.MultipartFile

class RouteController {
    
    def domainService
    def printService
	def fcService
    def gpxService
    def kmlService
    def osmPrintMapService
    def taskCreatorMapService
    def emailService
    def quartzScheduler
	def openAIPService
    
    def index = { redirect(action:"list",params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
		fcService.printstart "List routes"
        if (session?.lastContest) {
			session.lastContest.refresh()
            def route_list = Route.findAllByContest(session.lastContest,[sort:"idTitle"])
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
            return [routeInstanceList:route_list]
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
            redirect(action:"list")
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
            redirect(action:"list")
        }
    }

    def update = {
        def route = fcService.updateRoute(params) 
        if (route.saved) {
        	flash.message = route.message
            long next_routeid = route.instance.GetNextRouteID()
            if (next_routeid) {
                redirect(action:"show",id:route.instance.id,params:[next:next_routeid])
            } else {
        	    redirect(action:"show",id:route.instance.id)
            }
        } else if (route.instance) {
        	render(view:'edit',model:[routeInstance:route.instance])
        } else {
        	flash.message = route.message
            redirect(action:"edit",id:params.id)
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
        	redirect(action:"show",id:route.instance.id)
        } else {
            render(view:'create',model:[routeInstance:route.instance])
        }
    }

    def delete = {
        def route = fcService.deleteRoute(params)
        if (route.deleted) {
        	flash.message = route.message
        	redirect(action:"list")
        } else if (route.notdeleted) {
        	flash.message = route.message
            redirect(action:"show",id:params.id)
        } else {
        	flash.message = route.message
        	redirect(action:"list")
        }
    }
	
	def cancel = {
        // process return action
        if (params.mapexportquestionReturnAction) {
            redirect(action:params.mapexportquestionReturnAction,controller:params.mapexportquestionReturnController,id:params.mapexportquestionReturnID)
        } else if (session.routeReturnAction) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                long next_routeid = route.instance.GetNextRouteID()
                if (next_routeid) {
                    redirect(action:session.routeReturnAction,controller:session.routeReturnController,id:session.routeReturnID,params:[next:next_routeid])
                } else {
                    redirect(action:session.routeReturnAction,controller:session.routeReturnController,id:session.routeReturnID)
                }
            } else {
                redirect(action:session.routeReturnAction,controller:session.routeReturnController,id:session.routeReturnID)
            }
        } else {
       	    redirect(action:"list")
        }
	}
	
    def gotonext = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            long next_id = route.instance.GetNextRouteID()
            if (next_id) {
                redirect(action:"show",id:next_id)
            } else {
                redirect(controller:"route",action:"list")
            }
        } else {
            flash.message = test.message
            redirect(controller:"route",action:"list")
        }
    }
    
    def gotoprev = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            long next_id = route.instance.GetPrevRouteID()
            if (next_id) {
                redirect(action:"show",id:next_id)
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
        redirect(action:"list")
    }
    
    def selectfileroute = {
        if (session?.lastContest) {
			return [contestInstance:session.lastContest]
		} else {
			return [:]
		}
    }
    
    def importfileroute = {
        if (session?.lastContest) {
            String line_content = ImportSign.GetImportTxtLineContent(session.lastContest)
            redirect(action:selectfileroute, params:[lineContent:line_content])
        }
    }
    
    def importfileroute2 = {
        def file = request.getFile('routefile')
		String secretcoursechange = params?.secretcoursechange.replace(',','.')
        Map import_params = [foldername:params?.foldername,
                             readplacemarks:params?.readplacemarks == 'on',
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
                             semicircle1:params?.semicircle1 == 'on',
                             semicirclepos1:params?.semicirclepos1.isInteger()?params?.semicirclepos1.toInteger():null,
                             semicircle2:params?.semicircle2 == 'on',
                             semicirclepos2:params?.semicirclepos2.isInteger()?params?.semicirclepos2.toInteger():null,
                             semicircle3:params?.semicircle3 == 'on',
                             semicirclepos3:params?.semicirclepos3.isInteger()?params?.semicirclepos3.toInteger():null,
                             ildg:params?.ildg == 'on', 
                             ildgpos:params?.ildgpos.isInteger()?params?.ildgpos.toInteger():null, 
                             ildgdirection:params?.ildgdirection.isBigDecimal()?params?.ildgdirection.toBigDecimal():0.0,
                             ldg:params?.ldg.toInteger(),
                             ldgdirection:params?.ldgdirection.isBigDecimal()?params?.ldgdirection.toBigDecimal():0.0,
                             autosecret:params?.autosecret == 'on',
							 secretcoursechange:secretcoursechange.isBigDecimal()?secretcoursechange.toBigDecimal():1.5,
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
        redirect(action:"list")
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
    
    def selectturnpointphotos = {
        [:]
    }
    
	def selectturnpointphotos_cancel = {
        redirect(action:"show", id:params.id)
    }
    
	def loadturnpointphotos = {
		MultipartFile zip_file = request.getFile("zipfile")
		if (zip_file && !zip_file.isEmpty()) {
            Map ret = fcService.importTurnpointPhotos(params, zip_file)
            flash.message = message(code:'fc.coordroute.turnpointphoto.images.import.done', args:[ret.importNum])
        }
        redirect(action:"show", id:params.id)
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
    
    def selectenroutephotos = {
        [:]
    }
    
	def selectenroutephotos_cancel = {
        redirect(action:"show", id:params.id)
    }
    
	def loadselectenroutephotos = {
		MultipartFile zip_file = request.getFile("zipfile")
		if (zip_file && !zip_file.isEmpty()) {
            Map ret = fcService.importEnroutePhotos(params, zip_file)
            flash.message = message(code:'fc.coordroute.photo.images.import.done', args:[ret.importNum])
        }
        redirect(action:"show", id:params.id)
    }
    
	def calculateroutelegs = {
        def route = fcService.caculateroutelegsRoute(params) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:"show",id:route.instance.id)
        } else if (route.instance) {
            flash.message = route.message
            redirect(action:"show",id:route.instance.id)
        } else {
            flash.message = route.message
            redirect(action:"list")
        }
	}
    
    def print = {
        Map routes = printService.printRoutes(params,false,false,GetPrintParams()) 
        if (routes.error) {
            flash.message = routes.message
            flash.error = true
            redirect(action:"list")
        } else if (routes.found && routes.content) {
            printService.WritePDF(response,routes.content,session.lastContest.GetPrintPrefix(),"routes",true,false,false)
        } else {
            redirect(action:"list")
        }
    }
    
    def printroute = {
        Map route = printService.printRoute(params,false,false,GetPrintParams()) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:"list")
        } else if (route.content) {
            printService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}",true,false,false)
        } else {
            redirect(action:"list")
        }
	}
	
    def printcoordall = {
        Map route = printService.printCoord(params,false,false,GetPrintParams(),"all") 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:"list")
        } else if (route.content) {
            printService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-allpoints",true,false,false)
        } else {
            redirect(action:"list")
        }
	}
	
    def printcoordtp = {
        Map route = printService.printCoord(params,false,false,GetPrintParams(),"tp") 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:"list")
        } else if (route.content) {
            printService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-tppoints",true,false,false)
        } else {
            redirect(action:"list")
        }
	}
    
    def assignnamealphabetical_enroutephoto = {
        Map route = domainService.GetRoute(params)
        redirect(controller:'coordEnroutePhoto',action:'assignnamealphabetical',params:['route.id':route.instance.id,'routeid':route.instance.id])
    }
    
    def assignnamerandomly_enroutephoto = {
        Map route = domainService.GetRoute(params)
        redirect(controller:'coordEnroutePhoto',action:'assignnamerandomly',params:['route.id':route.instance.id,'routeid':route.instance.id])
    }
	
	def print_enroutephoto_alphabetical = {
        Map route = printService.printEnroutePhoto(params,true,GetPrintParams()) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:"list")
        } else if (route.content) {
            printService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-tppoints",true,false,false)
        } else {
            redirect(action:"list")
        }
	}
    
    def print_enroutephoto_route = {
        Map route = printService.printEnroutePhoto(params,false,GetPrintParams()) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:"list")
        } else if (route.content) {
            printService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-tppoints",true,false,false)
        } else {
            redirect(action:"list")
        }
    }
    
    def delete_enroutephoto_route = {
        fcService.deleteEnroutePhotos(params) 
        flash.message = message(code:'fc.coordroute.photo.deleted')
        redirect(action:"show",id:params.id)
    }
	
	def print_turnpointphoto_alphabetical = {
        Map route = printService.printTurnpointPhoto(params,true,GetPrintParams()) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:"list")
        } else if (route.content) {
            printService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-tppoints",true,false,false)
        } else {
            redirect(action:"list")
        }
	}
    
    def print_turnpointphoto_route = {
        Map route = printService.printTurnpointPhoto(params,false,GetPrintParams()) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:"list")
        } else if (route.content) {
            printService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-tppoints",true,false,false)
        } else {
            redirect(action:"list")
        }
    }
    
    def delete_turnpointphoto_route = {
        fcService.deleteTurnpointPhotos(params) 
        flash.message = message(code:'fc.coordroute.turnpointphoto.delete.done')
        redirect(action:"show",id:params.id)
    }
	
    def copyroute = {
        def route = fcService.copyRoute(params) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:"list")
        } else {
            redirect(action:"list")
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
            redirect(action:"list")
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
            redirect(action:"list")
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
            redirect(action:"list")
        }
    }

    def showturnpointphotoprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            return [contestInstance:session.lastContest,routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:"list")
        }
    }

	def get_turnpoint_photo = {
        CoordRoute coordroute_instance = CoordRoute.get(params.id)
        if (coordroute_instance && coordroute_instance.imagecoord) {
            response.setContentType("application/octet-stream")
            response.outputStream << coordroute_instance.imagecoord.imageData
		}
	}
    
    def showenroutephotoprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            return [contestInstance:session.lastContest,routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:"list")
        }
    }

	def get_enroute_photo = {
        CoordEnroutePhoto coordenroutephoto_instance = CoordEnroutePhoto.get(params.id)
        if (coordenroutephoto_instance && coordenroutephoto_instance.imagecoord) {
            response.setContentType("application/octet-stream")
            response.outputStream << coordenroutephoto_instance.imagecoord.imageData
		}
	}
    
    def showofflinemap_route = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            gpxService.printstart "showofflinemap_route: Show map of '${route.instance.name()}'"
            String uuid = UUID.randomUUID().toString()
            String upload_gpx_file_name = "${GpxService.GPXDATA}-${uuid}"
            Map converter = gpxService.ConvertRoute2GPX(route.instance, upload_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false, noCircleCenterPoints:true])
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
            redirect(action:"list")
        }
    }

    def showmap_route = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            gpxService.printstart "showmap_route: Show map of '${route.instance.name()}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GPX-${uuid}-UPLOAD.gpx"
            Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + upload_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:true, gpxExport:false])
            if (converter.ok) {
                gpxService.printdone ""
                session.gpxviewerReturnAction = 'show'
                session.gpxviewerReturnController = controllerName
                session.gpxviewerReturnID = params.id
                session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                redirect(controller:'gpx',action:'startgpxviewer',params:[defaultOnlineMap:route.instance.defaultOnlineMap,uploadFilename:upload_gpx_file_name,originalFilename:route.instance.name(),showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"no",showProfiles:"no",gmApiKey:BootStrap.global.GetGMApiKey()])
            } else {
                flash.error = true
                flash.message = message(code:'fc.gpx.gacnotconverted',args:[route.instance.name()])
                gpxService.DeleteFile(upload_gpx_file_name)
                gpxService.printerror flash.message
                redirect(action:'show',id:params.id)
            }
        } else {
            flash.message = route.message
            redirect(action:"list")
        }
    }

    def gpxexport_route = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            gpxService.printstart "gpxexport_route: Export route '${route.instance.name()}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GPX-${uuid}-UPLOAD.gpx"
            Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + upload_gpx_file_name, [isPrint:false, showPoints:false, wrEnrouteSign:true, gpxExport:true, wrPhotoImage:true])
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
            redirect(action:"list")
        }
    }

    def kmzexport_route = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            kmlService.printstart "kmzexport_route: Export route '${route.instance.name()}'"
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
            redirect(action:"list")
        }
    }
	
	def kmzexportairspaces_route = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            save_map_settings(route.instance, params)
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_kmz_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/AIRSPACES-${uuid}-UPLOAD.kmz"
			Map ret = openAIPService.WriteAirspaces2KMZ(route.instance, webroot_dir, upload_kmz_file_name, false)
            if (ret.ok) {
                String route_file_name = ("${Defs.NAME_AIRPORTAREA} ${route.instance.name()} Airspaces.kmz") // .replace(' ',"_")
                response.setContentType("application/octet-stream")
                response.setHeader("Content-Disposition", "Attachment;Filename=${route_file_name}")
                kmlService.Download(webroot_dir + upload_kmz_file_name, route_file_name, response.outputStream)
                kmlService.DeleteFile(upload_kmz_file_name)
                kmlService.printdone ""
            } else {
                flash.error = true
                flash.message = message(code:'fc.contestmap.contestmapairspaces.kmzexport.missedairspaces',args:[ret.missingAirspaces])
                kmlService.DeleteFile(upload_kmz_file_name)
                kmlService.printerror flash.message
				redirect(action:'mapexportquestion',id:params.id)
            }
			
        } else {
            flash.message = route.message
            redirect(action:"list")
        }
	}

    def getairspaces_airportarea_route = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            save_map_settings(route.instance, params)
			Map ret = openAIPService.GetAirspacesAirportarea(route.instance, ",${CoordType.TO.title},${CoordType.LDG.title},${CoordType.iTO.title},${CoordType.iLDG.title},")
            if (ret.ok) {
                flash.message = message(code:'fc.contestmap.contestmapgetairspaces.airportarea.done',args:[ret.airspacesnum])
            } else {
                flash.error = true
                flash.message = message(code:'fc.contestmap.contestmapgetairspaces.airportarea.notfound',args:[])
            }
            redirect(action:'mapexportquestion',id:params.id)
        } else {
            flash.message = route.message
            redirect(action:"list")
        }
    }
    
	def csvexportairports_route = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            save_map_settings(route.instance, params)
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_csv_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/AIRPORTS-${uuid}-UPLOAD.csv"
			Map ret = openAIPService.WriteAirports2CSV(route.instance, webroot_dir, upload_csv_file_name, false, ",${CoordType.TO.title},${CoordType.LDG.title},${CoordType.iTO.title},${CoordType.iLDG.title},")
            if (ret.ok) {
                String route_file_name = "${Defs.NAME_AIRPORTAREA} ${route.instance.name()} Airports.csv" // .replace(' ',"_")
                response.setContentType("application/octet-stream")
                response.setHeader("Content-Disposition", "Attachment;Filename=${route_file_name}")
                kmlService.Download(webroot_dir + upload_csv_file_name, route_file_name, response.outputStream)
                kmlService.DeleteFile(upload_csv_file_name)
                kmlService.printdone ""
            } else {
                flash.error = true
                flash.message = message(code:'fc.contestmap.contestmapairports.csvexport.notfound')
                kmlService.DeleteFile(upload_csv_file_name)
                kmlService.printerror flash.message
				redirect(action:'mapexportquestion',id:params.id)
            }
			
        } else {
            flash.message = route.message
            redirect(action:"list")
        }
	}

    def saveshow_ajax = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            if (params.showCoords) {
                route.instance.showCoords = params.showCoords == "true"
            }
            if (params.showCoordObservations) {
                route.instance.showCoordObservations = params.showCoordObservations == "true"
            }
            if (params.showResultLegs) {
                route.instance.showResultLegs = params.showResultLegs == "true"
            }
            if (params.showTestLegs) {
                route.instance.showTestLegs = params.showTestLegs == "true"
            }
            if (params.showEnroutePhotos) {
                route.instance.showEnroutePhotos = params.showEnroutePhotos == "true"
            }
            if (params.showEnrouteCanvas) {
                route.instance.showEnrouteCanvas = params.showEnrouteCanvas == "true"
            }
            route.instance.save()
        }
        render(text: "")
    }
    
    def mapexportquestion = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            route.instance.SetAllContestMapPoints()
            
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
                    if (!quartzScheduler.getTriggersOfJob(job_key)*.key) { // Scheduler job of this route is not running
                        //gpxService.println "Restart OsmPrintMapJob"
                        //redirect(action:'mapfetch',id:params.id)
                        break_button = false
                        fetch_button = true
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
                LineNumberReader printfileid_reader = new File(printfileid_filename).newReader()
                printfileid_reader.readLine()
                printfileid_reader.readLine()
                printfileid_reader.readLine()
                route.instance.contestMapPrintName = printfileid_reader.readLine()
                printfileid_reader.close()
            } else if (new File(printfileid_errorfilename).exists()) { // Job ends not successful
                flash.message = message(code:'fc.contestmap.job.error')
                flash.error = true
                gpxService.DeleteFile(printfileid_errorfilename)
            }
            // quartzScheduler.getTriggersOfJob(new JobKey("OsmPrintMapJob",Defs.OSMPRINTMAP_GROUP))*.key)
            
            // set return action
            return [routeInstance:route.instance, mapexportquestionReturnAction:"show", mapexportquestionReturnController:controllerName, mapexportquestionReturnID:params.id, BreakButton:break_button, FetchButton:fetch_button, PrintButton:print_button, DiscardButton:discard_button, NewOSMMap:new_osm_map]
        } else {
            flash.message = task.message
            redirect(action:'show',id:params.id)
        }
    }
    
    def mapexportquestion2 = {
        redirect(action:'mapexportquestion',id:params.id)
    }
    
    def mapsavesettings = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            save_map_settings(route.instance, params)
        }
        redirect(action:'mapexportquestion',id:params.id)
    }
    
    def mapgenerate = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:route.instance.contestMapCircle,
                                         contestMapProcedureTurn:route.instance.contestMapProcedureTurn,
                                         contestMapLeg:route.instance.contestMapLeg,
                                         contestMapCurvedLeg:route.instance.contestMapCurvedLeg,
                                         contestMapCurvedLegPoints:route.instance.contestMapCurvedLegPoints,
                                         contestMapTpName:route.instance.contestMapTpName,
                                         contestMapSecretGates:route.instance.contestMapSecretGates,
                                         contestMapEnroutePhotos:route.instance.contestMapEnroutePhotos,
                                         contestMapEnrouteCanvas:route.instance.contestMapEnrouteCanvas,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape,
                                         contestMapPrintSize:route.instance.contestMapPrintSize,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName1(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, false, false, 1, route.instance.contestMapFirstTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate2 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:route.instance.contestMapCircle,
                                         contestMapProcedureTurn:route.instance.contestMapProcedureTurn,
                                         contestMapLeg:route.instance.contestMapLeg,
                                         contestMapCurvedLeg:route.instance.contestMapCurvedLeg,
                                         contestMapCurvedLegPoints:route.instance.contestMapCurvedLegPoints,
                                         contestMapTpName:route.instance.contestMapTpName,
                                         contestMapSecretGates:route.instance.contestMapSecretGates,
                                         contestMapEnroutePhotos:route.instance.contestMapEnroutePhotos,
                                         contestMapEnrouteCanvas:route.instance.contestMapEnrouteCanvas,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints2,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints2,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape2,
                                         contestMapPrintSize:route.instance.contestMapPrintSize2,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX2,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY2,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName2(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos2,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos2
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, false, false, 2, route.instance.contestMapSecondTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate3 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:route.instance.contestMapCircle,
                                         contestMapProcedureTurn:route.instance.contestMapProcedureTurn,
                                         contestMapLeg:route.instance.contestMapLeg,
                                         contestMapCurvedLeg:route.instance.contestMapCurvedLeg,
                                         contestMapCurvedLegPoints:route.instance.contestMapCurvedLegPoints,
                                         contestMapTpName:route.instance.contestMapTpName,
                                         contestMapSecretGates:route.instance.contestMapSecretGates,
                                         contestMapEnroutePhotos:route.instance.contestMapEnroutePhotos,
                                         contestMapEnrouteCanvas:route.instance.contestMapEnrouteCanvas,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints3,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints3,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape3,
                                         contestMapPrintSize:route.instance.contestMapPrintSize3,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX3,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY3,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName3(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos3,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos3
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, false, false, 3, route.instance.contestMapThirdTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate4 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:route.instance.contestMapCircle,
                                         contestMapProcedureTurn:route.instance.contestMapProcedureTurn,
                                         contestMapLeg:route.instance.contestMapLeg,
                                         contestMapCurvedLeg:route.instance.contestMapCurvedLeg,
                                         contestMapCurvedLegPoints:route.instance.contestMapCurvedLegPoints,
                                         contestMapTpName:route.instance.contestMapTpName,
                                         contestMapSecretGates:route.instance.contestMapSecretGates,
                                         contestMapEnroutePhotos:route.instance.contestMapEnroutePhotos,
                                         contestMapEnrouteCanvas:route.instance.contestMapEnrouteCanvas,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints4,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints4,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape4,
                                         contestMapPrintSize:route.instance.contestMapPrintSize4,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX4,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY4,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName4(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos4,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos4
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, false, false, 4, route.instance.contestMapForthTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_noroute = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:false,
                                         contestMapProcedureTurn:false,
                                         contestMapLeg:false,
                                         contestMapCurvedLeg:false,
                                         contestMapCurvedLegPoints:false,
                                         contestMapTpName:false,
                                         contestMapSecretGates:false,
                                         contestMapEnroutePhotos:false,
                                         contestMapEnrouteCanvas:false,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape,
                                         contestMapPrintSize:route.instance.contestMapPrintSize,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}l"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName1(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, true, false, 1, route.instance.contestMapFirstTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_taskcreator = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:false,
                                         contestMapProcedureTurn:false,
                                         contestMapLeg:false,
                                         contestMapCurvedLeg:false,
                                         contestMapCurvedLegPoints:false,
                                         contestMapTpName:true,
                                         contestMapSecretGates:false,
                                         contestMapEnroutePhotos:false,
                                         contestMapEnrouteCanvas:false,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints,
                                         contestMapPrintPoints:",${CoordType.TO.title},${CoordType.LDG.title},${CoordType.iTO.title},${CoordType.iLDG.title},",
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape,
                                         contestMapPrintSize:route.instance.contestMapPrintSize,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}l"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = taskCreatorMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                    routeTitle: route.instance.GetOSMRouteName1() + " (Task Creator)",
                                                                    routeId: route.instance.id,
                                                                    webRootDir: webroot_dir,
                                                                    gpxFileName: webroot_dir + route_gpx_file_name,
                                                                    pngFileName: webroot_dir + map_png_file_name,
                                                                    graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                    contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                    mapScale: route.instance.mapScale,
                                                                    contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos,
                                                                    contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos
                                                                   ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, true, false, 1, route.instance.contestMapFirstTitle + " (Task Creator)")
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_noroute2 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:false,
                                         contestMapProcedureTurn:false,
                                         contestMapLeg:false,
                                         contestMapCurvedLeg:false,
                                         contestMapCurvedLegPoints:false,
                                         contestMapTpName:false,
                                         contestMapSecretGates:false,
                                         contestMapEnroutePhotos:false,
                                         contestMapEnrouteCanvas:false,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints2,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints2,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape2,
                                         contestMapPrintSize:route.instance.contestMapPrintSize2,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX2,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY2,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}l"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName2(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos2,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos2
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, true, false, 2, route.instance.contestMapSecondTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_taskcreator2 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:false,
                                         contestMapProcedureTurn:false,
                                         contestMapLeg:false,
                                         contestMapCurvedLeg:false,
                                         contestMapCurvedLegPoints:false,
                                         contestMapTpName:true,
                                         contestMapSecretGates:false,
                                         contestMapEnroutePhotos:false,
                                         contestMapEnrouteCanvas:false,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints2,
                                         contestMapPrintPoints:",${CoordType.TO.title},${CoordType.LDG.title},${CoordType.iTO.title},${CoordType.iLDG.title},",
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape2,
                                         contestMapPrintSize:route.instance.contestMapPrintSize2,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX2,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY2,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}l"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = taskCreatorMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                    routeTitle: route.instance.GetOSMRouteName2() + " (Task Creator)",
                                                                    routeId: route.instance.id,
                                                                    webRootDir: webroot_dir,
                                                                    gpxFileName: webroot_dir + route_gpx_file_name,
                                                                    pngFileName: webroot_dir + map_png_file_name,
                                                                    graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                    contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                    mapScale: route.instance.mapScale,
                                                                    contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos2,
                                                                    contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos2
                                                                   ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, true, false, 2, route.instance.contestMapSecondTitle + " (Task Creator)")
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_noroute3 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:false,
                                         contestMapProcedureTurn:false,
                                         contestMapLeg:false,
                                         contestMapCurvedLeg:false,
                                         contestMapCurvedLegPoints:false,
                                         contestMapTpName:false,
                                         contestMapSecretGates:false,
                                         contestMapEnroutePhotos:false,
                                         contestMapEnrouteCanvas:false,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints3,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints3,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape3,
                                         contestMapPrintSize:route.instance.contestMapPrintSize3,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX3,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY3,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}l"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName3(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos3,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos3
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, true, false, 3, route.instance.contestMapThirdTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_taskcreator3 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:false,
                                         contestMapProcedureTurn:false,
                                         contestMapLeg:false,
                                         contestMapCurvedLeg:false,
                                         contestMapCurvedLegPoints:false,
                                         contestMapTpName:true,
                                         contestMapSecretGates:false,
                                         contestMapEnroutePhotos:false,
                                         contestMapEnrouteCanvas:false,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints3,
                                         contestMapPrintPoints:",${CoordType.TO.title},${CoordType.LDG.title},${CoordType.iTO.title},${CoordType.iLDG.title},",
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape3,
                                         contestMapPrintSize:route.instance.contestMapPrintSize3,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX3,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY3,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}l"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = taskCreatorMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                    routeTitle: route.instance.GetOSMRouteName3() + " (Task Creator)",
                                                                    routeId: route.instance.id,
                                                                    webRootDir: webroot_dir,
                                                                    gpxFileName: webroot_dir + route_gpx_file_name,
                                                                    pngFileName: webroot_dir + map_png_file_name,
                                                                    graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                    contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                    mapScale: route.instance.mapScale,
                                                                    contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos3,
                                                                    contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos3
                                                                   ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, true, false, 3, route.instance.contestMapThirdTitle + " (Task Creator)")
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_noroute4 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:false,
                                         contestMapProcedureTurn:false,
                                         contestMapLeg:false,
                                         contestMapCurvedLeg:false,
                                         contestMapCurvedLegPoints:false,
                                         contestMapTpName:false,
                                         contestMapSecretGates:false,
                                         contestMapEnroutePhotos:false,
                                         contestMapEnrouteCanvas:false,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints4,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints4,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape4,
                                         contestMapPrintSize:route.instance.contestMapPrintSize4,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX4,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY4,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName4(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos4,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos4
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, true, false, 4, route.instance.contestMapForthTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_taskcreator4 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:false,
                                         contestMapProcedureTurn:false,
                                         contestMapLeg:false,
                                         contestMapCurvedLeg:false,
                                         contestMapCurvedLegPoints:false,
                                         contestMapTpName:true,
                                         contestMapSecretGates:false,
                                         contestMapEnroutePhotos:false,
                                         contestMapEnrouteCanvas:false,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints4,
                                         contestMapPrintPoints:",${CoordType.TO.title},${CoordType.LDG.title},${CoordType.iTO.title},${CoordType.iLDG.title},",
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape4,
                                         contestMapPrintSize:route.instance.contestMapPrintSize4,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX4,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY4,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = taskCreatorMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                    routeTitle: route.instance.GetOSMRouteName4() + " (Task Creator)",
                                                                    routeId: route.instance.id,
                                                                    webRootDir: webroot_dir,
                                                                    gpxFileName: webroot_dir + route_gpx_file_name,
                                                                    pngFileName: webroot_dir + map_png_file_name,
                                                                    graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                    contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                    mapScale: route.instance.mapScale,
                                                                    contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos4,
                                                                    contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos4
                                                                   ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, true, false, 4, route.instance.contestMapForthTitle + " (Task Creator)")
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_allroutedetails = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
				String curvedleg_points = ""
				for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route.instance,[sort:"id"])) {
                    if (coordroute_instance.endCurved) {
						if (curvedleg_points) {
							curvedleg_points += ",${coordroute_instance.title()}"
						} else {
							curvedleg_points = coordroute_instance.title()
						}
					}
				}
				if (curvedleg_points) {
					curvedleg_points += ","
				}
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:true,
                                         contestMapProcedureTurn:true,
                                         contestMapLeg:true,
                                         contestMapCurvedLeg:true,
                                         contestMapCurvedLegPoints:curvedleg_points,
                                         contestMapTpName:true,
                                         contestMapSecretGates:true,
                                         contestMapEnroutePhotos:true,
                                         contestMapEnrouteCanvas:true,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape,
                                         contestMapPrintSize:route.instance.contestMapPrintSize,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName1(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, false, true, 1, route.instance.contestMapFirstTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_allroutedetails2 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
				String curvedleg_points = ""
				for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route.instance,[sort:"id"])) {
                    if (coordroute_instance.endCurved) {
						if (curvedleg_points) {
							curvedleg_points += ",${coordroute_instance.title()}"
						} else {
							curvedleg_points = coordroute_instance.title()
						}
					}
				}
				if (curvedleg_points) {
					curvedleg_points += ","
				}
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:true,
                                         contestMapProcedureTurn:true,
                                         contestMapLeg:true,
                                         contestMapCurvedLeg:true,
                                         contestMapCurvedLegPoints:curvedleg_points,
                                         contestMapTpName:true,
                                         contestMapSecretGates:true,
                                         contestMapEnroutePhotos:true,
                                         contestMapEnrouteCanvas:true,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints2,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints2,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape2,
                                         contestMapPrintSize:route.instance.contestMapPrintSize2,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX2,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY2,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName2(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos2,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos2
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, false, true, 2, route.instance.contestMapSecondTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_allroutedetails3 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
				String curvedleg_points = ""
				for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route.instance,[sort:"id"])) {
                    if (coordroute_instance.endCurved) {
						if (curvedleg_points) {
							curvedleg_points += ",${coordroute_instance.title()}"
						} else {
							curvedleg_points = coordroute_instance.title()
						}
					}
				}
				if (curvedleg_points) {
					curvedleg_points += ","
				}
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:true,
                                         contestMapProcedureTurn:true,
                                         contestMapLeg:true,
                                         contestMapCurvedLeg:true,
                                         contestMapCurvedLegPoints:curvedleg_points,
                                         contestMapTpName:true,
                                         contestMapSecretGates:true,
                                         contestMapEnroutePhotos:true,
                                         contestMapEnrouteCanvas:true,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints3,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints3,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape3,
                                         contestMapPrintSize:route.instance.contestMapPrintSize3,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX3,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY3,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName3(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos3,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos3
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, false, true, 3, route.instance.contestMapThirdTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_allroutedetails4 = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
				String curvedleg_points = ""
				for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route.instance,[sort:"id"])) {
                    if (coordroute_instance.endCurved) {
						if (curvedleg_points) {
							curvedleg_points += ",${coordroute_instance.title()}"
						} else {
							curvedleg_points = coordroute_instance.title()
						}
					}
				}
				if (curvedleg_points) {
					curvedleg_points += ","
				}
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:true,
                                         contestMapProcedureTurn:true,
                                         contestMapLeg:true,
                                         contestMapCurvedLeg:true,
                                         contestMapCurvedLegPoints:curvedleg_points,
                                         contestMapTpName:true,
                                         contestMapSecretGates:true,
                                         contestMapEnroutePhotos:true,
                                         contestMapEnrouteCanvas:true,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:route.instance.contestMapCenterPoints4,
                                         contestMapPrintPoints:route.instance.contestMapPrintPoints4,
                                         contestMapPrintLandscape:route.instance.contestMapPrintLandscape4,
                                         contestMapPrintSize:route.instance.contestMapPrintSize4,
                                         contestMapCenterMoveX:route.instance.contestMapCenterMoveX4,
                                         contestMapCenterMoveY:route.instance.contestMapCenterMoveY4,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route.instance.GetOSMRouteName4(),
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: route.instance.contestMapCenterHorizontalPos4,
                                                                 contestMapCenterVerticalPos: route.instance.contestMapCenterVerticalPos4
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, false, true, 4, route.instance.contestMapForthTitle)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_airportarea = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:false,
                                         contestMapProcedureTurn:false,
                                         contestMapLeg:false,
                                         contestMapCurvedLeg:false,
                                         contestMapCurvedLegPoints:false,
                                         contestMapTpName:true,
                                         contestMapSecretGates:false,
                                         contestMapEnroutePhotos:false,
                                         contestMapEnrouteCanvas:false,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:",${CoordType.TO.title},${CoordType.LDG.title},${CoordType.iTO.title},${CoordType.iLDG.title},",
                                         contestMapPrintPoints:",${CoordType.TO.title},${CoordType.LDG.title},${CoordType.iTO.title},${CoordType.iLDG.title},",
                                         contestMapPrintLandscape:true,
                                         contestMapPrintSize:Defs.CONTESTMAPPRINTSIZE_AIRPORTAREA,
                                         contestMapCenterMoveX:0.0,
                                         contestMapCenterMoveY:0.0,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            String route_title = "${Defs.NAME_AIRPORTAREA} ${route.instance.name()} (Online Map)"
                            Map r = osmPrintMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                 routeTitle: route_title,
                                                                 routeId: route.instance.id,
                                                                 webRootDir: webroot_dir,
                                                                 gpxFileName: webroot_dir + route_gpx_file_name,
                                                                 pngFileName: webroot_dir + map_png_file_name,
                                                                 graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                 contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                 mapScale: route.instance.mapScale,
                                                                 contestMapCenterHorizontalPos: HorizontalPos.Center,
                                                                 contestMapCenterVerticalPos: VerticalPos.Center
                                                                ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, true, false, 0, route_title)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    def mapgenerate_airportarea_taskcreator = {
        if (session?.lastContest) {
            Map route = domainService.GetRoute(params) 
            if (route.instance) {
                route.instance.contestMapEdition++
                save_map_settings(route.instance, params)
                gpxService.printstart "Export map '${route.instance.name()}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String route_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}.gpx"
                Map contestmap_params = [contestMapEdition:route.instance.contestMapEdition,
                                         contestMapCircle:false,
                                         contestMapProcedureTurn:false,
                                         contestMapLeg:false,
                                         contestMapCurvedLeg:false,
                                         contestMapCurvedLegPoints:false,
                                         contestMapTpName:true,
                                         contestMapSecretGates:false,
                                         contestMapEnroutePhotos:false,
                                         contestMapEnrouteCanvas:false,
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
                                         contestMapDropShadow:route.instance.contestMapDropShadow,
                                         contestMapAdditionals:route.instance.contestMapAdditionals,
                                         contestMapSpecials:route.instance.contestMapSpecials,
                                         contestMapAirspaces:route.instance.contestMapAirspaces,
                                         contestMapCenterPoints:",${CoordType.TO.title},${CoordType.LDG.title},${CoordType.iTO.title},${CoordType.iLDG.title},",
                                         contestMapPrintPoints:",${CoordType.TO.title},${CoordType.LDG.title},${CoordType.iTO.title},${CoordType.iLDG.title},",
                                         contestMapPrintLandscape:true,
                                         contestMapPrintSize:Defs.CONTESTMAPPRINTSIZE_AIRPORTAREA,
                                         contestMapCenterMoveX:0.0,
                                         contestMapCenterMoveY:0.0,
                                         contestMapDevStyle:route.instance.contestMapDevStyle,
                                         contestMapFCStyle:BootStrap.global.GetPrintServerFCStyle(),
                                        ]
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + route_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false], contestmap_params)
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
                            String info_file_name = "${map_png_file_name}info"
                            String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                            String vrt_file_name = "${tif_file_name}.vrt"
                            String map_graticule_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GRATICULE-${uuid}.csv"
                            String route_title = "${Defs.NAME_AIRPORTAREA} ${route.instance.name()} (Task Creator)"
                            Map r = taskCreatorMapService.PrintOSM([contestTitle: session.lastContest.title,
                                                                    routeTitle: route_title,
                                                                    routeId: route.instance.id,
                                                                    webRootDir: webroot_dir,
                                                                    gpxFileName: webroot_dir + route_gpx_file_name,
                                                                    pngFileName: webroot_dir + map_png_file_name,
                                                                    graticuleFileName: webroot_dir + map_graticule_file_name,
                                                                    contestMapAirspacesLayer2: route.instance.contestMapAirspacesLayer2,
                                                                    mapScale: route.instance.mapScale,
                                                                    contestMapCenterHorizontalPos: HorizontalPos.Center,
                                                                    contestMapCenterVerticalPos: VerticalPos.Center
                                                                   ] + contestmap_params)
                            if (r.ok) {
                                emailService.CreateUploadJobRouteMap(route.instance, true, false, 0, route_title)
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
                                redirect(action:'mapexportquestion', id:params.id)
                            }
                            gpxService.DeleteFile(map_png_file_name)
                            gpxService.DeleteFile(world_file_name)
                            gpxService.DeleteFile(info_file_name)
                            gpxService.DeleteFile(tif_file_name)
                            gpxService.DeleteFile(vrt_file_name)
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
                redirect(action:"list")
            }
        }
    }
    
    private void save_map_settings(Route routeInstance, def params)
    {
        routeInstance.contestMapOutput = params.contestMapOutput
        routeInstance.contestMapCircle = params.contestMapCircle == "on"
        routeInstance.contestMapProcedureTurn = params.contestMapProcedureTurn == "on"
        routeInstance.contestMapLeg = params.contestMapLeg == "on"
        routeInstance.contestMapCurvedLeg = params.contestMapCurvedLeg == "on"
        routeInstance.contestMapTpName = params.contestMapTpName == "on"
        routeInstance.contestMapSecretGates = params.contestMapSecretGates == "on"
        routeInstance.contestMapEnroutePhotos = params.contestMapEnroutePhotos == "on"
        routeInstance.contestMapEnrouteCanvas = params.contestMapEnrouteCanvas == "on"
        routeInstance.contestMapGraticule = params.contestMapGraticule == "on"
        routeInstance.contestMapContourLines = params.contestMapContourLines.toInteger()
        routeInstance.contestMapMunicipalityNames = params.contestMapMunicipalityNames == "on"
        routeInstance.contestMapAirfields = params.contestMapAirfields
        routeInstance.contestMapChurches = params.contestMapChurches == "on"
        routeInstance.contestMapCastles = params.contestMapCastles == "on"
        routeInstance.contestMapChateaus = params.contestMapChateaus == "on"
        routeInstance.contestMapPowerlines = params.contestMapPowerlines == "on"
        routeInstance.contestMapWindpowerstations = params.contestMapWindpowerstations == "on"
        routeInstance.contestMapSmallRoads = params.contestMapSmallRoads == "on"
        routeInstance.contestMapPeaks = params.contestMapPeaks == "on"
        routeInstance.contestMapDropShadow = params.contestMapDropShadow == "on"
        routeInstance.contestMapAdditionals = params.contestMapAdditionals == "on"
        routeInstance.contestMapSpecials = params.contestMapSpecials == "on"
        routeInstance.contestMapAirspaces = params.contestMapAirspaces == "on"
        routeInstance.contestMapAirspacesLayer2 = params.contestMapAirspacesLayer2
        if (params.contestMapAirspacesLowerLimit && params.contestMapAirspacesLowerLimit.isInteger()) {
            routeInstance.contestMapAirspacesLowerLimit = params.contestMapAirspacesLowerLimit.toInteger()
        }
        String center_points = ""
        String center_points2 = ""
        String center_points3 = ""
        String center_points4 = ""
        String print_points = ""
        String print_points2 = ""
        String print_points3 = ""
        String print_points4 = ""
        String curvedleg_points = ""
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:"id"])) {
            if (params.("${Defs.TurnpointID_ContestMapCenterPoints}${coordroute_instance.title()}") == "on") {
                if (center_points) {
                    center_points += ",${coordroute_instance.title()}"
                } else {
                    center_points = coordroute_instance.title()
                }
            }
            if (params.("${Defs.TurnpointID_ContestMapCenterPoints2}${coordroute_instance.title()}") == "on") {
                if (center_points2) {
                    center_points2 += ",${coordroute_instance.title()}"
                } else {
                    center_points2 = coordroute_instance.title()
                }
            }
            if (params.("${Defs.TurnpointID_ContestMapCenterPoints3}${coordroute_instance.title()}") == "on") {
                if (center_points3) {
                    center_points3 += ",${coordroute_instance.title()}"
                } else {
                    center_points3 = coordroute_instance.title()
                }
            }
            if (params.("${Defs.TurnpointID_ContestMapCenterPoints4}${coordroute_instance.title()}") == "on") {
                if (center_points4) {
                    center_points4 += ",${coordroute_instance.title()}"
                } else {
                    center_points4 = coordroute_instance.title()
                }
            }
            if (params.("${Defs.TurnpointID_ContestMapPrintPoints}${coordroute_instance.title()}") == "on") {
                if (print_points) {
                    print_points += ",${coordroute_instance.title()}"
                } else {
                    print_points = coordroute_instance.title()
                }
            }
            if (params.("${Defs.TurnpointID_ContestMapPrintPoints2}${coordroute_instance.title()}") == "on") {
                if (print_points2) {
                    print_points2 += ",${coordroute_instance.title()}"
                } else {
                    print_points2 = coordroute_instance.title()
                }
            }
            if (params.("${Defs.TurnpointID_ContestMapPrintPoints3}${coordroute_instance.title()}") == "on") {
                if (print_points3) {
                    print_points3 += ",${coordroute_instance.title()}"
                } else {
                    print_points3 = coordroute_instance.title()
                }
            }
            if (params.("${Defs.TurnpointID_ContestMapPrintPoints4}${coordroute_instance.title()}") == "on") {
                if (print_points4) {
                    print_points4 += ",${coordroute_instance.title()}"
                } else {
                    print_points4 = coordroute_instance.title()
                }
            }
            if (params.("${Defs.TurnpointID_ContestMapCurvedLegPoints}${coordroute_instance.title()}") == "on") {
                if (curvedleg_points) {
                    curvedleg_points += ",${coordroute_instance.title()}"
                } else {
                    curvedleg_points = coordroute_instance.title()
                }
            }
        }
        if (center_points) {
            center_points += ","
        }
        if (print_points) {
            print_points += ","
        }
        if (curvedleg_points) {
            curvedleg_points += ","
        }
        routeInstance.contestMapCurvedLegPoints = DisabledCheckPointsTools.Compress(curvedleg_points, routeInstance)
        routeInstance.contestMapShowFirstOptions = params.contestMapShowFirstOptions == "on"
        routeInstance.contestMapFirstTitle = params.contestMapFirstTitle
        routeInstance.contestMapCenterVerticalPos = params.contestMapCenterVerticalPos
        routeInstance.contestMapCenterHorizontalPos = params.contestMapCenterHorizontalPos
        routeInstance.contestMapCenterPoints = DisabledCheckPointsTools.Compress(center_points, routeInstance)
        routeInstance.contestMapPrintPoints = DisabledCheckPointsTools.Compress(print_points, routeInstance)
        routeInstance.contestMapPrintLandscape = params.contestMapPrintLandscape == "on"
        routeInstance.contestMapPrintSize = params.contestMapPrintSize
        if (params.contestMapCenterMoveX && params.contestMapCenterMoveX.replace(',','.').isBigDecimal()) {
            routeInstance.contestMapCenterMoveX = params.contestMapCenterMoveX.replace(',','.').toBigDecimal()
        }
        if (params.contestMapCenterMoveY && params.contestMapCenterMoveY.replace(',','.').isBigDecimal()) {
            routeInstance.contestMapCenterMoveY = params.contestMapCenterMoveY.replace(',','.').toBigDecimal()
        }
        routeInstance.contestMapShowSecondOptions = params.contestMapShowSecondOptions == "on"
        routeInstance.contestMapSecondTitle = params.contestMapSecondTitle
        routeInstance.contestMapCenterVerticalPos2 = params.contestMapCenterVerticalPos2
        routeInstance.contestMapCenterHorizontalPos2 = params.contestMapCenterHorizontalPos2
        routeInstance.contestMapCenterPoints2 = DisabledCheckPointsTools.Compress(center_points2, routeInstance)
        routeInstance.contestMapPrintPoints2 = DisabledCheckPointsTools.Compress(print_points2, routeInstance)
        routeInstance.contestMapPrintLandscape2 = params.contestMapPrintLandscape2 == "on"
        routeInstance.contestMapPrintSize2 = params.contestMapPrintSize2
        if (params.contestMapCenterMoveX2 && params.contestMapCenterMoveX2.replace(',','.').isBigDecimal()) {
            routeInstance.contestMapCenterMoveX2 = params.contestMapCenterMoveX2.replace(',','.').toBigDecimal()
        }
        if (params.contestMapCenterMoveY2 && params.contestMapCenterMoveY2.replace(',','.').isBigDecimal()) {
            routeInstance.contestMapCenterMoveY2 = params.contestMapCenterMoveY2.replace(',','.').toBigDecimal()
        }
        routeInstance.contestMapShowThirdOptions = params.contestMapShowThirdOptions == "on"
        routeInstance.contestMapThirdTitle = params.contestMapThirdTitle
        routeInstance.contestMapCenterVerticalPos3 = params.contestMapCenterVerticalPos3
        routeInstance.contestMapCenterHorizontalPos3 = params.contestMapCenterHorizontalPos3
        routeInstance.contestMapCenterPoints3 = DisabledCheckPointsTools.Compress(center_points3, routeInstance)
        routeInstance.contestMapPrintPoints3 = DisabledCheckPointsTools.Compress(print_points3, routeInstance)
        routeInstance.contestMapPrintLandscape3 = params.contestMapPrintLandscape3 == "on"
        routeInstance.contestMapPrintSize3 = params.contestMapPrintSize3
        if (params.contestMapCenterMoveX3 && params.contestMapCenterMoveX3.replace(',','.').isBigDecimal()) {
            routeInstance.contestMapCenterMoveX3 = params.contestMapCenterMoveX3.replace(',','.').toBigDecimal()
        }
        if (params.contestMapCenterMoveY3 && params.contestMapCenterMoveY3.replace(',','.').isBigDecimal()) {
            routeInstance.contestMapCenterMoveY3 = params.contestMapCenterMoveY3.replace(',','.').toBigDecimal()
        }
        routeInstance.contestMapShowForthOptions = params.contestMapShowForthOptions == "on"
        routeInstance.contestMapForthTitle = params.contestMapForthTitle
        routeInstance.contestMapCenterVerticalPos4 = params.contestMapCenterVerticalPos4
        routeInstance.contestMapCenterHorizontalPos4 = params.contestMapCenterHorizontalPos4
        routeInstance.contestMapCenterPoints4 = DisabledCheckPointsTools.Compress(center_points4, routeInstance)
        routeInstance.contestMapPrintPoints4 = DisabledCheckPointsTools.Compress(print_points4, routeInstance)
        routeInstance.contestMapPrintLandscape4 = params.contestMapPrintLandscape4 == "on"
        routeInstance.contestMapPrintSize4 = params.contestMapPrintSize4
        if (params.contestMapCenterMoveX4 && params.contestMapCenterMoveX4.replace(',','.').isBigDecimal()) {
            routeInstance.contestMapCenterMoveX4 = params.contestMapCenterMoveX4.replace(',','.').toBigDecimal()
        }
        if (params.contestMapCenterMoveY4 && params.contestMapCenterMoveY4.replace(',','.').isBigDecimal()) {
            routeInstance.contestMapCenterMoveY4 = params.contestMapCenterMoveY4.replace(',','.').toBigDecimal()
        }
        routeInstance.contestMapDevStyle = params.contestMapDevStyle == "on"
        routeInstance.save()
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
            redirect(action:"list")
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
                String print_size = printjobid_file_reader.readLine()
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
                     (Defs.OSMPRINTMAP_PRINTSIZE):print_size,
                     (Defs.OSMPRINTMAP_PRINTPROJECTION):"",
                     (Defs.OSMPRINTMAP_PRINTCOLORCHANGES):print_colorchanges
                    ]
                )
                File printjob_file = new File(printjob_filename)
                BufferedWriter printjob_writer = printjob_file.newWriter()
                printjob_writer << route.instance.id
                printjob_writer.close()
            } else {
                flash.error = true
                flash.message = message(code:'fc.contestmap.job.continue.error')
            }
            redirect(action:'mapexportquestion',id:params.id)
        } else {
            flash.message = route.message
            redirect(action:"list")
        }
    }
    
    def mapdiscard = {
        Map route = domainService.GetRoute(params) 
        if (route.instance) {
            String webroot_dir = servletContext.getRealPath("/")
            String printjobid_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOBID + route.instance.id + ".txt"
            if (new File(printjobid_filename).exists()) {
                quartzScheduler.unscheduleJobs(quartzScheduler.getTriggersOfJob(new JobKey("OsmPrintMapJob",Defs.OSMPRINTMAP_GROUP))*.key)
                gpxService.DeleteFile(printjobid_filename)
            }
            String printjob_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB
            if (new File(printjob_filename).exists()) {
                gpxService.DeleteFile(printjob_filename)
            }
            
            String printfileid_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTFILEID + route.instance.id + ".txt"
            File printfileid_file = new File(printfileid_filename)
            if (printfileid_file.exists()) {
                LineNumberReader printfileid_reader = printfileid_file.newReader()
                String map_png_file_name = printfileid_reader.readLine()
                printfileid_reader.close()
                String unpacked_png_file_name = "${map_png_file_name}.png"
                String warped_png_file_name = "${map_png_file_name}.warped.png"
                String warped_png_file_name_xml = "${map_png_file_name}.warped.png.aux.xml"
                String world_file_name = "${map_png_file_name}w"
                String info_file_name = "${map_png_file_name}info"
                String map_zip_file_name = map_png_file_name + ".zip"
                String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                String warped_tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.warped.tif"
                String vrt_file_name = "${tif_file_name}.vrt"
                String tiles_zip_file_name = tif_file_name + ".zip"
                gpxService.DeleteFile(map_png_file_name)
                gpxService.DeleteFile(warped_png_file_name)
                gpxService.DeleteFile(warped_png_file_name_xml)
                gpxService.DeleteFile(unpacked_png_file_name)
                gpxService.DeleteFile(world_file_name)
                gpxService.DeleteFile(info_file_name)
                gpxService.DeleteFile(map_zip_file_name)
                gpxService.DeleteFile(tif_file_name)
                gpxService.DeleteFile(warped_tif_file_name)
                gpxService.DeleteFile(vrt_file_name)
                gpxService.DeleteFile(tiles_zip_file_name)
                gpxService.DeleteFile(printfileid_filename)
            }
            if (params.gotoMapList) {
                redirect(controller:"map",action:'list')
            } else {
                redirect(action:'mapexportquestion',id:params.id)
            }
        } else {
            flash.message = route.message
            redirect(action:"list")
        }
    }
    
    def maprefresh = {
        redirect(action:'mapexportquestion',id:params.id)
    }

    def mapsave_gotomap = {
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
                    String info_file_name = "${map_png_file_name}info"
                    String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                    String vrt_file_name = "${tif_file_name}.vrt"
                    boolean print_landscape = printfileid_file_reader.readLine() == 'true'
                    String print_size = printfileid_file_reader.readLine()
                    printfileid_file_reader.close()
                    if (params.contestMapPrintName) {
                        String png_file_name = "${params.contestMapPrintName}.png"
                        String warped_png_file_name = "${params.contestMapPrintName}.warped.png"
                        String tif_file_name2 = "${params.contestMapPrintName}.tif"
                        String map_folder_name = "${webroot_dir}${Defs.ROOT_FOLDER_MAP}/${route.instance.contest.contestUUID}/"
                        File map_folder = new File(map_folder_name)
                        if (!map_folder.exists()) {
                            map_folder.mkdir()
                        }
                        if (!new File(map_folder_name + png_file_name).exists() || params.contestMapAllowOverwrite) {
                            gpxService.printstart "Copy ${png_file_name} to ${map_folder_name}"
                            copy_file_to_folder(map_folder_name, png_file_name, map_png_file_name)
                            copy_file_to_folder(map_folder_name, warped_png_file_name, map_png_file_name + ".warped.png")
                            copy_file_to_folder(map_folder_name, png_file_name + "w", world_file_name)
                            copy_file_to_folder(map_folder_name, png_file_name + "info", info_file_name)
                            copy_file_to_folder(map_folder_name, tif_file_name2, tif_file_name)
                            gpxService.printdone ""
                            flash.message = message(code:'fc.contestmap.savemap.done',args:[png_file_name])
                            redirect(action:'mapdiscard',id:params.id,params:[gotoMapList:true])
                        } else {
                            flash.message = message(code:'fc.contestmap.savemap.exists',args:[png_file_name])
                            flash.error = true
                            redirect(action:'mapexportquestion',id:params.id)
                        }
                    }
                } else {
                    redirect(action:'show',id:params.id)
                }
            } else {
                flash.message = route.message
                redirect(action:"list")
            }
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
    def mapsave = {
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
                    String info_file_name = "${map_png_file_name}info"
                    String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                    String vrt_file_name = "${tif_file_name}.vrt"
                    boolean print_landscape = printfileid_file_reader.readLine() == 'true'
                    String print_size = printfileid_file_reader.readLine()
                    printfileid_file_reader.close()
                    if (params.contestMapPrintName) {
                        String png_file_name = "${params.contestMapPrintName}.png"
                        String warped_png_file_name = "${params.contestMapPrintName}.warped.png"
                        String tif_file_name2 = "${params.contestMapPrintName}.tif"
                        String map_folder_name = "${webroot_dir}${Defs.ROOT_FOLDER_MAP}/${route.instance.contest.contestUUID}/"
                        File map_folder = new File(map_folder_name)
                        if (!map_folder.exists()) {
                            map_folder.mkdir()
                        }
                        if (!new File(map_folder_name + png_file_name).exists() || params.contestMapAllowOverwrite) {
                            gpxService.printstart "Copy ${png_file_name} to ${map_folder_name}"
                            copy_file_to_folder(map_folder_name, png_file_name, map_png_file_name)
                            copy_file_to_folder(map_folder_name, warped_png_file_name, map_png_file_name + ".warped.png")
                            copy_file_to_folder(map_folder_name, png_file_name + "w", world_file_name)
                            copy_file_to_folder(map_folder_name, png_file_name + "info", info_file_name)
                            copy_file_to_folder(map_folder_name, tif_file_name2, tif_file_name)
                            gpxService.printdone ""
                            flash.message = message(code:'fc.contestmap.savemap.done',args:[png_file_name])
                            redirect(action:'mapdiscard',id:params.id)
                        } else {
                            flash.message = message(code:'fc.contestmap.savemap.exists',args:[png_file_name])
                            flash.error = true
                            redirect(action:'mapexportquestion',id:params.id)
                        }
                    }
                } else {
                    redirect(action:'show',id:params.id)
                }
            } else {
                flash.message = route.message
                redirect(action:"list")
            }
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
    /*
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
                    String info_file_name = "${map_png_file_name}info"
                    String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                    String vrt_file_name = "${tif_file_name}.vrt"
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
                    } else if (route.instance.contestMapPrint == Defs.CONTESTMAPPRINT_PNGZIP) {
                        String png_file_name = "Map.png"
                        String map_zip_file_name = map_png_file_name + ".zip"
                        gpxService.printstart "Write ${map_zip_file_name}"
                        ZipOutputStream zip_stream = new ZipOutputStream(new FileOutputStream(map_zip_file_name))
                        write_file_to_zip(zip_stream, png_file_name, map_png_file_name)
                        write_file_to_zip(zip_stream, png_file_name + "w", world_file_name)
                        write_file_to_zip(zip_stream, png_file_name + "info", info_file_name)
                        zip_stream.close()
                        gpxService.printdone ""
                        gpxService.printstart "Download PNGZIP"
                        String map_file_name = (route.instance.name() + '.zip').replace(' ',"_")
                        response.setContentType("application/octet-stream")
                        response.setHeader("Content-Disposition", "Attachment;Filename=${map_file_name}")
                        gpxService.Download(map_zip_file_name, map_file_name, response.outputStream)
                        gpxService.printdone ""
                    } else if (route.instance.contestMapPrint == Defs.CONTESTMAPPRINT_ONLINEMAP) { // FC OnlineMap
                        if (params.contestMapPrintName) {
                            String png_file_name = "${params.contestMapPrintName}.png"
                            String warped_png_file_name = "${params.contestMapPrintName}.warped.png"
                            String tif_file_name2 = "${params.contestMapPrintName}.tif"
                            String map_folder_name = "${webroot_dir}${Defs.ROOT_FOLDER_MAP}/${route.instance.contest.contestUUID}/"
                            File map_folder = new File(map_folder_name)
                            if (!map_folder.exists()) {
                                map_folder.mkdir()
                            }
                            if (!new File(map_folder_name + png_file_name).exists() || params.contestMapAllowOverwrite) {
                                gpxService.printstart "Copy ${png_file_name} to ${map_folder_name}"
                                copy_file_to_folder(map_folder_name, png_file_name, map_png_file_name)
                                copy_file_to_folder(map_folder_name, warped_png_file_name, map_png_file_name + ".warped.png")
                                copy_file_to_folder(map_folder_name, png_file_name + "w", world_file_name)
                                copy_file_to_folder(map_folder_name, png_file_name + "info", info_file_name)
                                copy_file_to_folder(map_folder_name, tif_file_name2, tif_file_name)
                                gpxService.printdone ""
                                flash.message = message(code:'fc.contestmap.savemap.done',args:[png_file_name])
                            } else {
                                flash.message = message(code:'fc.contestmap.savemap.exists',args:[png_file_name])
                                flash.error = true
                            }
                        }
                        redirect(action:'mapexportquestion',id:params.id)
                    } else if (route.instance.contestMapPrint == Defs.CONTESTMAPPRINT_PDFMAP) {
                        gpxService.printstart "Generate PDF"
                        Map ret = printService.printmapRoute(print_size, print_landscape, map_png_file_name, GetPrintParams())
                        gpxService.printdone ""
                        if (ret.error) {
                            flash.message = ret.message
                            flash.error = true
                            redirect(action:'show',id:params.id)
                        } else if (ret.content) {
                            //printService.WritePDF(response, ret.content, session.lastContest.GetPrintPrefix(), "map-${route.instance.idTitle}", true, print_size, print_landscape)
                            String map_file_name = (route.instance.name() + '.pdf').replace(' ',"_")
                            printService.WritePDF3(response, ret.content, map_file_name)
                        } else {
                            redirect(action:'show',id:params.id)
                        }
                    } else if (route.instance.contestMapPrint == Defs.CONTESTMAPPRINT_TIFMAP) {
                        gpxService.printstart "Download TIF"
                        String map_file_name = (route.instance.name() + '.tif').replace(' ',"_")
                        response.setContentType("application/octet-stream")
                        response.setHeader("Content-Disposition", "Attachment;Filename=${map_file_name}")
                        gpxService.Download(tif_file_name, map_file_name, response.outputStream)
                        gpxService.printdone ""
                    } else if (route.instance.contestMapPrint == Defs.CONTESTMAPPRINT_TILES) {
                        String tiles_zip_file_name = tif_file_name + ".zip"
                        String tiles_dir_name = tif_file_name.substring(0,tif_file_name.lastIndexOf('/')) + "/tiles-" + route.instance.id
                        gpxService.printstart "Write ${tiles_zip_file_name}"
                        gpxService.DeleteDir(tiles_dir_name)
                        //gpxService.BuildVRT(tif_file_name, vrt_file_name)
                        gpxService.println "Generate tiles $tiles_dir_name"
                        //gpxService.Gdal2Tiles(webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD, vrt_file_name, tiles_dir_name)
                        gpxService.Gdal2Tiles(webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD, tif_file_name, tiles_dir_name)
                        Map ret = [message:'Tiles generated']
                        if (!(params.contestMapNoTilesUpload == 'on')) {
                            ret = gpxService.UploadTiles(tiles_dir_name)
                        }
                        //ZipOutputStream zip_stream = new ZipOutputStream(new FileOutputStream(tiles_zip_file_name))
                        //write_folder_to_zip(zip_stream, tiles_dir_name.substring(tiles_dir_name.lastIndexOf('/')+1), tiles_dir_name)
                        //zip_stream.close()
                        if (!(params.contestMapNoTilesUpload == 'on')) {
                            gpxService.DeleteDir(tiles_dir_name)
                        }
                        gpxService.printdone ""
                        //gpxService.printstart "Download TILES"
                        //String map_file_name = (route.instance.name() + '-Tiles.zip').replace(' ',"_")
                        //response.setContentType("application/octet-stream")
                        //response.setHeader("Content-Disposition", "Attachment;Filename=${map_file_name}")
                        //gpxService.Download(tiles_zip_file_name, map_file_name, response.outputStream)
                        //gpxService.printdone ""
                        flash.message = ret.message
                        if (ret.error) {
                            flash.error = true
                        }
                        redirect(action:'mapexportquestion',id:params.id)
                    }
                } else {
                    redirect(action:'show',id:params.id)
                }
            } else {
                flash.message = route.message
                redirect(action:"list")
            }
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    */
    
    def mapsendmail = {
        if (session?.lastContest) {
            session.lastContest.refresh()
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
                    String info_file_name = "${map_png_file_name}info"
                    String tif_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.tif"
                    String vrt_file_name = "${tif_file_name}.vrt"
                    boolean print_landscape = printfileid_file_reader.readLine() == 'true'
                    String print_size = printfileid_file_reader.readLine()
                    printfileid_file_reader.close()
                        
                    gpxService.printstart "Generate PDF"
                    Map ret = printService.printmapRoute(print_size, print_landscape, map_png_file_name, GetPrintParams())
                    gpxService.printdone ""
                
                    if (ret.error) {
                        flash.message = ret.message
                        flash.error = true
                        redirect(action:'mapexportquestion',id:params.id)
                    } else if (ret.content) {
                        String map_file_name = "${map_png_file_name.substring(0,map_png_file_name.lastIndexOf('.'))}.pdf"
                        
                        def map_file_stream = new File(map_file_name).newOutputStream()
                        map_file_stream.write(ret.content)
                        map_file_stream.close()

                        Map ret2 = emailService.emailRouteMap(route.instance, map_file_name)
                        flash.message = ret2.message
                        if (!ret2.ok) {
                            flash.error = true
                        }
                        redirect(action:'mapexportquestion',id:params.id)
                    } else {
                        redirect(action:'mapexportquestion',id:params.id)
                    }
                } else {
                    redirect(action:'mapexportquestion',id:params.id)
                }
            } else {
                flash.message = route.message
                redirect(action:"list")
            }
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
    private void copy_file_to_folder(String folderName, String fileName, String srcFileName)
    {
        def src_file = new File(srcFileName)
        if (src_file.exists()) {
            def src_stream = src_file.newInputStream()
            def dest_stream = new File(folderName+fileName).newOutputStream()  
            dest_stream << src_stream
            dest_stream.close()
            src_stream.close()
        }
    }
    
    private void write_file_to_zip(ZipOutputStream zipOutputStream, String zipFileName, String fileName)
    {
        byte[] buffer = new byte[1024]
        FileInputStream file_input_stream = new FileInputStream(fileName)
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName))
        int length
        while ((length = file_input_stream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length)
        }
        zipOutputStream.closeEntry()
        file_input_stream.close()
    }
    
    private void write_folder_to_zip(ZipOutputStream zipOutputStream, String zipFolderName, String folderName)
    {
        // https://stackoverflow.com/questions/740375/directories-in-a-zip-file-when-using-java-util-zip-zipoutputstream
        byte[] buffer = new byte[1024]
        //FileInputStream file_input_stream = new FileInputStream(fileName)
        zipOutputStream.putNextEntry(new ZipEntry(zipFolderName))
        /*
        int length
        while ((length = file_input_stream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length)
        }
        */
        zipOutputStream.closeEntry()
        //file_input_stream.close()
    }
    
    def mapprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        return [contestInstance:session.lastContest, mapFileName:params.mapFileName]
    }
    
    def mapdeletelink = {
        if (params.edition) {
            Route route_instance = Route.get(params.id)
            if (route_instance) {
                Long edition = params.edition.toLong()
                UploadJobRouteMap uploadjob_routemap = UploadJobRouteMap.findByRouteAndUploadJobMapEdition(route_instance, edition)
                if (uploadjob_routemap) {
                    uploadjob_routemap.delete()
                }
            }
        }
        redirect(action:"show",id:params.id)
    }
    
    def routelinks = {
        Route route_instance = Route.get(params.id)
        String route_links = emailService.GetRouteLinks(route_instance)
        response.setContentType("application/octet-stream")   
        response.setHeader("Content-Disposition", "Attachment;Filename=Links.txt")        
        response.outputStream << route_links
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
                redirect(action:"list")
            }
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
    def addviewposition_route = {
        fcService.changeviewpositionRoute(params, true)
        redirect(action:"list")
    }

    def subviewposition_route = {
        fcService.changeviewpositionRoute(params, false)
        redirect(action:"list")
    }

    def openaip_test = {
        def ret = openAIPService.test(params)
        flash.message = ret.message
		if (ret.error) {
			flash.error = true
		}
		redirect(action:"list")
    }
    
	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
}
