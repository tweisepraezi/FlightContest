import java.util.Map;

class RouteController {
    
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
            return [routeInstanceList:routeList]
        }
		fcService.printdone ""
        redirect(controller:'contest',action:'start')
    }

    def show = {
        def route = fcService.getRoute(params) 
        if (route.instance) {
        	return [routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def edit = {
        def route = fcService.getRoute(params) 
        if (route.instance) {
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
		def route = fcService.createRoute(params)
        return [routeInstance:route.instance]
    }

    def save = {
        def route = fcService.saveRoute(params,session.lastContest) 
        if (route.saved) {
        	flash.message = route.message
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
        def route = fcService.getRoute(params) 
        if (route.instance) {
            redirect(action:show,id:route.instance.id)
        } else {
        	redirect(action:list)
        }
	}
	
	def createcoordroutes = {
        def route = fcService.getRoute(params) 
        redirect(controller:'coordRoute',action:'create',params:['route.id':route.instance.id,'routeid':route.instance.id])
	}

    def createsecretcoordroutes = {
        def route = fcService.getRoute(params) 
        redirect(controller:'coordRoute',action:'create',params:['secret':true,'route.id':route.instance.id,'routeid':route.instance.id])
    }

	def selectaflosroute = {
		[contestInstance:session.lastContest]
    }
	
	def importroute = {
		def route = fcService.existAnyAflosRoute(session.lastContest)
		if (route.error) {
			flash.error = route.error
            flash.message = route.message
	        redirect(action:list)
		} else {
	        redirect(action:selectaflosroute)
		}
	}
    
	def importaflosroute = {
        def route = fcService.importAflosRoute(params,session.lastContest,"",false,[]) // false - $curved wird nicht ignoriert 
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
        def routes = fcService.printRoutes(params,false,false,GetPrintParams()) 
        if (routes.error) {
            flash.message = routes.message
            flash.error = true
            redirect(action:list)
        } else if (routes.found && routes.content) {
            fcService.WritePDF(response,routes.content,session.lastContest.GetPrintPrefix(),"routes",true,false,false)
        } else {
            redirect(action:list)
        }
    }
    
    def printroute = {
        def route = fcService.printRoute(params,false,false,GetPrintParams()) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:list)
        } else if (route.content) {
            fcService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}",true,false,false)
        } else {
            redirect(action:list)
        }
	}
	
    def printcoordall = {
        def route = fcService.printCoord(params,false,false,GetPrintParams(),"all") 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:list)
        } else if (route.content) {
            fcService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-allpoints",true,false,false)
        } else {
            redirect(action:list)
        }
	}
	
    def printcoordtp = {
        def route = fcService.printCoord(params,false,false,GetPrintParams(),"tp") 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:list)
        } else if (route.content) {
            fcService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-tppoints",true,false,false)
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
        def route = fcService.getRoute(params) 
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
        def route = fcService.getRoute(params) 
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
        def route = fcService.getRoute(params) 
        if (route.instance) {
            return [contestInstance:session.lastContest,routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def showmapold = {
        def route = fcService.getRoute(params) 
        if (route.instance) {
        	return [routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }
    
    def showmap = {
        def route = fcService.getRoute(params) 
        if (route.instance) {
            gpxService.printstart "Process '${route.instance.name()}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_gpx_file_name = "gpxupload/GPX-${uuid}-UPLOAD.gpx"
            Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + upload_gpx_file_name)
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

    def sendmail = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            def route = fcService.getRoute(params)
            if (route.instance) {
                String email_to = route.instance.EMailAddress()
                gpxService.printstart "Send mail of '${route.instance.name()}' to '${email_to}'"
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String upload_gpx_file_name = "gpxupload/GPX-${uuid}-UPLOAD.gpx"
                Map converter = gpxService.ConvertRoute2GPX(route.instance, webroot_dir + upload_gpx_file_name)
                if (converter.ok) {
                    Map email = GetEMailBody(session.lastContest, route.instance)
                    
                    String job_file_name = "jobs/JOB-${uuid}.job"
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
