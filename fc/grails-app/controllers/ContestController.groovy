import java.util.Date;
import java.util.Map;

class ContestController {
    
    def fcService
	def demoContestService

    def index = { redirect(action:start,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def start = {
        fcService.printstart "Start contest"
        
		session.contestTitle = ""
        boolean restart = false
        
        // check ShowLanguage
        String show_language = fcService.GetCookie("ShowLanguage",Languages.de.toString())
        if (show_language) {
            if (session?.showLanguage != show_language) {
                restart = true
            }
            session.showLanguage = show_language
            BootStrap.global.showLanguage = show_language
        }

        if (restart) {
            fcService.printdone "Restart contest with language $show_language"
			redirect(controller:'contest',action:'start',params:[lang:show_language])
        } else {
            // PrintLanguage
            String print_language = fcService.GetCookie("PrintLanguage",Languages.de.toString())
            if (print_language) {
                session.printLanguage = print_language
                BootStrap.global.printLanguage = print_language
            }
            
            // ShowLimitCrewNum
            String showLimitCrewNum = fcService.GetCookie("ShowLimitCrewNum","10")
            if (showLimitCrewNum) {
                session.showLimitCrewNum = showLimitCrewNum.toInteger() 
            }
            
            if (BootStrap.global.IsDBOlder()) {
                fcService.printdone "db older"
                redirect(controller:'contest',action:'nostart',params:[reason:"older"])
            } else if (BootStrap.global.IsDBNewer()) {
                fcService.printdone "db newer"
                redirect(controller:'contest',action:'nostart',params:[reason:"newer"])
            } else {
                // LastContestID
                String lastContestID = fcService.GetCookie("LastContestID","")
                if (lastContestID) {
                    Contest last_contest = Contest.findById(lastContestID.toInteger())
                    if (last_contest) {
                        session.lastContest = last_contest
                    }
                }
                if (session?.lastContest) {
                    // save return action
                    session.contestReturnAction = actionName 
                    session.contestReturnController = controllerName
                    session.contestReturnID = params.id
                    fcService.printdone "last contest"
                    return [contestInstance:session.lastContest]
                }
                fcService.printdone ""
                return [:]
            }
        }
    }

    def nostart = {
        fcService.println "No Start."
        return [reason:params.reason]
    }
    
    def show = {
        if (session?.lastContest) {
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def edit = {
        if (session?.lastContest) {
            // assign return action
            if (session.contestReturnAction) {
                return [contestInstance:session.lastContest,contestReturnAction:session.contestReturnAction,contestReturnController:session.contestReturnController,contestReturnID:session.contestReturnID]
            }
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def editpoints = {
        if (session?.lastContest) {
            // assign return action
            if (session.contestReturnAction) {
                return [contestInstance:session.lastContest,contestReturnAction:session.contestReturnAction,contestReturnController:session.contestReturnController,contestReturnID:session.contestReturnID]
            }
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def update = {
        def contest = fcService.updateContest(params)
        if (contest.saved) {
            flash.message = contest.message
            session.lastContest = contest.instance
            if (!session.lastContest.teamCrewNum) {
                session.lastTeamResults = null
            }
            fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
            // process return action
            if (params.contestReturnAction) {
                redirect(action:params.contestReturnAction,controller:params.contestReturnController,id:params.contestReturnID)
            } else if (params.editteamresultsettingsReturnAction) {
                redirect(action:params.editteamresultsettingsReturnAction,controller:params.editteamresultsettingsReturnController,id:params.editteamresultsettingsReturnID)
            } else if (params.editresultsettingsReturnAction) {
                redirect(action:params.editresultsettingsReturnAction,controller:params.editresultsettingsReturnController,id:params.editresultsettingsReturnID)
            } else {
                redirect(action:start,id:contest.instance.id)
            }
        } else if (contest.instance) {
            render(view:'edit',model:[contestInstance:contest.instance])
        } else {
            flash.message = contest.message
            redirect(action:edit,id:params.id)
        }
    }

    def savepoints = {
        def contest = fcService.updateContest(params)
        if (contest.saved) {
            flash.message = contest.message
            session.lastContest = contest.instance
               redirect(action:"editpoints",id:contest.instance.id)
        } else {
            flash.message = contest.message
            redirect(action:editpoints,id:params.id)
        }
    }

    def calculatepoints = {
        def contest = fcService.calculatepointsContest(params)
          flash.message = contest.message
        redirect(action:editpoints,id:params.id)
    }

    def create = {
        def contest = fcService.createContest(params)
        if (contest.created) {
            return [contestInstance:contest.instance]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def save = {
        def contest = fcService.saveContest(params) 
        if (contest.saved) {
            session.lastContest = contest.instance
            fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
            session.showLimit = false
            session.showLimitStartPos = 0
            session.lastTaskPlanning = null
            session.lastTaskResults = null
            session.lastResultClassResults = null
            session.lastContestResults = null
            session.lastTeamResults = null
            flash.message = contest.message
            redirect(action:start,id:contest.instance.id)
        } else {
            render(view:'create',model:[contestInstance:contest.instance])
        }
    }

    def copyquestion = {
        if (session?.lastContest) {
            Map new_params = params + [title:fcService.getContestCopyTitle(session.lastContest)]
            def contest = fcService.createContest(new_params)
            if (contest.created) {
                return [contestInstance:contest.instance]
            } else {
                flash.message = contest.message
                redirect(action:start)
            }
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }
    
    def copy = {
        def contest = fcService.copyContest(params,session.lastContest) 
        if (contest.saved) {
            session.lastContest = contest.instance
            fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
            session.showLimit = false
            session.showLimitStartPos = 0
            session.lastTaskPlanning = null
            session.lastTaskResults = null
            session.lastResultClassResults = null
            session.lastContestResults = null
            session.lastTeamResults = null
            flash.message = contest.message
            redirect(action:start,id:contest.instance.id)
        } else {
            render(view:'create',model:[contestInstance:contest.instance])
        }
    }

    def tasks = {
        fcService.printstart "List tasks"
        if (session?.lastContest) {
            def contestTasksList = Task.findAllByContest(session.lastContest,[sort:"id"])
            // save return action
            session.taskReturnAction = actionName 
            session.taskReturnController = controllerName
            session.taskReturnID = params.id
            session.planningtestReturnAction = actionName
            session.planningtestReturnController = controllerName
            session.planningtestReturnID = params.id
            session.flighttestReturnAction = actionName
            session.flighttestReturnController = controllerName
            session.flighttestReturnID = params.id
            fcService.printdone "last contest"
            return [contestInstance:session.lastContest,contestTasks:contestTasksList]
        }
        fcService.printdone ""
        redirect(controller:'contest',action:'start')
    }

    def activate = {
        session.lastContest = Contest.get(params.id)
        fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
        session.showLimit = false
        session.showLimitStartPos = 0
        session.lastTaskPlanning = null
        session.lastTaskResults = null
        session.lastResultClassResults = null
        session.lastContestResults = null
        session.lastTeamResults = null
        redirect(action:'start')
    }

    def deletequestion = {
        if (session?.lastContest) {
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }
    
    def delete = {
        def contest = fcService.deleteContest(params)
        if (contest.deleted) {
            if (Contest.count() == 1) {
                session.lastContest = Contest.findByIdIsNotNull([sort:"id",order:"desc"])
                fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
                // session.lastContest = null
                // fcService.SetCookie(response, "LastContestID",  "")
                session.showLimit = false
                session.showLimitStartPos = 0
                session.lastTaskPlanning = null
                session.lastTaskResults = null
                session.lastResultClassResults = null
                session.lastContestResults = null
                session.lastTeamResults = null
                flash.message = contest.message
                redirect(action:start)
            } else {
                session.lastContest = null
                fcService.SetCookie(response, "LastContestID",  "")
                session.showLimit = false
                session.showLimitStartPos = 0
                session.lastTaskPlanning = null
                session.lastTaskResults = null
                session.lastResultClassResults = null
                session.lastContestResults = null
                session.lastTeamResults = null
                flash.message = contest.message
                redirect(controller:'global')
            }
        } else if (contest.notdeleted) {
            flash.message = contest.message
            redirect(action:start,id:params.id)
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def change = {
        session.lastContest = null
        fcService.SetCookie(response, "LastContestID",  "")
        redirect(action:start)
    }

    def updatecontest = {
        session.lastContest = Contest.get(params.contestid)
        fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
        session.showLimit = false
        session.showLimitStartPos = 0
        session.lastTaskPlanning = null
        session.lastTaskResults = null
        session.lastResultClassResults = null
        session.lastContestResults = null
        session.lastTeamResults = null
        redirect(action:start)
    }

	def selectfilename_imageleft = {
		redirect(action:selectimagefilename,params:['imageField':'imageLeft'])
	}
	
	def selectfilename_imagecenter = {
		redirect(action:selectimagefilename,params:['imageField':'imageCenter'])
	}
	
	def selectfilename_imageright = {
		redirect(action:selectimagefilename,params:['imageField':'imageRight'])
	}
	
	def selectimagefilename = {
		[:]
    }
	
	def loadimage = {
		load_image(params) 
		redirect(action:edit)
	}
	
	void load_image(Map params) 
	{
		def file = request.getFile('imagefile')
		if (file && !file.empty) {
			String file_name = file.getOriginalFilename()
			fcService.printstart "Upload '$file_name' to '$params.imageField'"
			if (file.size <= Contest.IMAGEMAXSIZE) {
				fcService.println file.getContentType() // "image/jpeg"
				if (file_name.toLowerCase().endsWith('.jpg')) {
					session.lastContest.(params.imageField) = file.bytes
					session.lastContest.save()
					fcService.printdone ""
				} else {
					flash.error = true
					flash.message = message(code:'fc.notimported.image',args:[file_name])
					fcService.printerror flash.message
				}
			} else {
				flash.error = true
				flash.message = message(code:'fc.notimported.image.size',args:[file_name,file.size,Contest.IMAGEMAXSIZE])
				fcService.printerror flash.message
			}
		}
	}
	
	def deleteimage_imageleft = {
		delete_image(['imageField':'imageLeft','imageFieldHeight':'imageLeftHeight'])
		redirect(action:edit)
	}
	
	def deleteimage_imagecenter = {
		delete_image(['imageField':'imageCenter','imageFieldHeight':'imageCenterHeight'])
		redirect(action:edit)
	}
	
	def deleteimage_imageright = {
		delete_image(['imageField':'imageRight','imageFieldHeight':'imageRightHeight'])
		redirect(action:edit)
	}
	
	void delete_image(Map params)
	{
		fcService.printstart "Delete '$params.imageField'"
		session.lastContest.(params.imageField) = null
		session.lastContest.(params.imageFieldHeight) = Contest.IMAGEHEIGHT
		session.lastContest.save()
		fcService.printdone ""
	}   
	
	def actualimage_imageleft = {
		actual_image(['imageFieldHeight':'imageLeftHeight','imageHeight':params.imageLeftHeight])
		redirect(action:edit)
	}
	
	def actualimage_imagecenter = {
		actual_image(['imageFieldHeight':'imageCenterHeight','imageHeight':params.imageCenterHeight])
		redirect(action:edit)
	}
	
	def actualimage_imageright = {
		actual_image(['imageFieldHeight':'imageRightHeight','imageHeight':params.imageRightHeight])
		redirect(action:edit)
	}
	
	void actual_image(Map params)
	{
		fcService.printstart "Actual '$params.imageFieldHeight'"
		if (params.imageHeight.isInteger()) {
			session.lastContest.(params.imageFieldHeight) = params.imageHeight.toInteger()
		}
		session.lastContest.save()
		fcService.printdone ""
	}   
	
	def actual_titelsize = {
		fcService.printstart "Actual 'titleSize'"
		session.lastContest.titleSize = params.titleSize
		session.lastContest.save()
		fcService.printdone ""
		redirect(action:edit)
	}
	
	def reset_titlesize = {
		fcService.printstart "Reset 'titleSize'"
		session.lastContest.titleSize = Contest.TITLESIZE
		session.lastContest.save()
		fcService.printdone ""
		redirect(action:edit)
	}
	
	def view_image_left = {
		if (params.contestid) {
	        Contest contest = Contest.get(params.contestid)
			response.outputStream << contest.imageLeft
		}
	}
	     
	def view_image_center = {
		if (params.contestid) {
	        Contest contest = Contest.get(params.contestid)
			response.outputStream << contest.imageCenter
		}
	}
	     
	def view_image_right = {
		if (params.contestid) {
	        Contest contest = Contest.get(params.contestid)
			response.outputStream << contest.imageRight
		}
	}
	     
    def cancel = {
        // process return action
        if (params.positionsReturnAction) {
            redirect(action:params.positionsReturnAction,controller:params.positionsReturnController,id:params.positionsReturnID)
        } else if (params.contestReturnAction) {
            redirect(action:params.contestReturnAction,controller:params.contestReturnController,id:params.contestReturnID)
        } else if (params.editteamresultsettingsReturnAction) {
            redirect(action:params.editteamresultsettingsReturnAction,controller:params.editteamresultsettingsReturnController,id:params.editteamresultsettingsReturnID)
        } else if (params.editresultsettingsReturnAction) {
            redirect(action:params.editresultsettingsReturnAction,controller:params.editresultsettingsReturnController,id:params.editresultsettingsReturnID)
        } else {
            redirect(action:start)
        }
    }
    
    def createtask = {
        redirect(controller:'task',action:'create',params:['contest.id':session.lastContest.id,'contestid':session.lastContest.id,'fromcontest':true])
    }

    def listresults = {
		session.contestTitle = ""
        if (session?.lastContest) {
            session.lastResultClassResults = null
            session.lastContestResults = true
            session.lastTeamResults = null
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
            session.crewresultsReturnAction = actionName
            session.crewresultsReturnController = controllerName
            session.crewresultsReturnID = params.id
            // assign return action
            if (session.positionsReturnAction) {
                return [contestInstance:session.lastContest,positionsReturnAction:session.positionsReturnAction,positionsReturnController:session.positionsReturnController,positionsReturnID:session.positionsReturnID]
            }
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    def listresultslive = {
		if (params.id) {
			session.lastContest = Contest.get(params.id)
		}
		if (session?.lastContest) {
			def contest = fcService.calculatecontestpositionsContest(session.lastContest,[],[],[])
            session.contestTitle = session.lastContest.GetPrintContestTitle(ResultFilter.Contest)
			return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
		}
	}
	
	def liveview = {
		if (session?.lastContest) {
			redirect(action:listresultslive)
        } else {
            redirect(action:start)
		}
	}
	
    def listteamresults = {
        if (session?.lastContest) {
            session.lastResultClassResults = null
            session.lastContestResults = null
            session.lastTeamResults = true
            // save return action
            session.crewReturnAction = actionName
            session.crewReturnController = controllerName
            session.crewReturnID = params.id
            session.teamReturnAction = actionName
            session.teamReturnController = controllerName
            session.teamReturnID = params.id
            // assign return action
            if (session.positionsReturnAction) {
                return [contestInstance:session.lastContest,positionsReturnAction:session.positionsReturnAction,positionsReturnController:session.positionsReturnController,positionsReturnID:session.positionsReturnID]
            }
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    def calculatepositions = {
        def contest = fcService.calculatecontestpositionsContest(session.lastContest,[],[],[])
        flash.message = contest.message
        if (contest.error) {
            flash.error = true
        }
        redirect(action:"listresults")
    }

    def calculateteampositions = {
        def contest = fcService.calculateteampositionsContest(session.lastContest,[],[]) 
        flash.message = contest.message
        if (contest.error) {
            flash.error = true
        }
        redirect(action:"listteamresults")
    }

    def printtest_a4_portrait = {
        if (session?.lastContest) {
            def contest = fcService.printtestContest(session.lastContest,false,false,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(controller:"contest",action:"listresults")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"test",true,false,false)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def printtest_a4_landscape = {
		fcService.printstart "Actual 'a4LandscapeFactor'"
		session.lastContest.a4LandscapeFactor = params.a4LandscapeFactor.replace(',','.').toBigDecimal()
		session.lastContest.save()
		fcService.printdone ""
        if (session?.lastContest) {
            def contest = fcService.printtestContest(session.lastContest,false,true,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(controller:"contest",action:"listresults")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"test",true,false,true)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def printtest_a3_portrait = {
        if (session?.lastContest) {
			fcService.printstart "Actual 'a3PortraitFactor'"
			session.lastContest.a3PortraitFactor = params.a3PortraitFactor.replace(',','.').toBigDecimal()
			session.lastContest.save()
			fcService.printdone ""
            def contest = fcService.printtestContest(session.lastContest,true,false,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(controller:"contest",action:"listresults")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"test",true,true,false)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def printtest_a3_landscape = {
        if (session?.lastContest) {
			fcService.printstart "Actual 'a3LandscapeFactor'"
			session.lastContest.a3LandscapeFactor = params.a3LandscapeFactor.replace(',','.').toBigDecimal()
			session.lastContest.save()
			fcService.printdone ""
            def contest = fcService.printtestContest(session.lastContest,true,true,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(controller:"contest",action:"listresults")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"test",true,true,true)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def listtestprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.contestTitle = session.lastContest.GetPrintContestTitle(ResultFilter.Contest)
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    def printresults = {
        if (session?.lastContest) {
            def contest = fcService.printresultsContest(session.lastContest,session.lastContest.contestPrintA3,session.lastContest.contestPrintLandscape,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(controller:"contest",action:"listresults")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"contestresults",true,session.lastContest.contestPrintA3,session.lastContest.contestPrintLandscape)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def listresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.contestTitle = session.lastContest.GetPrintContestTitle(ResultFilter.Contest)
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    def printteamresults = {
        if (session?.lastContest) {
            def contest = fcService.printteamresultsContest(session.lastContest,session.lastContest.teamPrintA3,session.lastContest.teamPrintLandscape,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(action:"listteamresults")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"teamresults",true,session.lastContest.teamPrintA3,session.lastContest.teamPrintLandscape)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def listteamresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.contestTitle = session.lastContest.GetPrintContestTitle(ResultFilter.Team)
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    def editresultsettings = {
        if (session?.lastContest) {
            // set return action
               return [contestInstance:session.lastContest,resultfilter:ResultFilter.Contest,editresultsettingsReturnAction:"listresults",editresultsettingsReturnController:controllerName,editresultsettingsReturnID:params.id]
        } else {
            redirect(action:start)
        }
    }
    
    def editteamresultsettings = {
        if (session?.lastContest) {
            // set return action
               return [contestInstance:session.lastContest,resultfilter:ResultFilter.Team,editteamresultsettingsReturnAction:"listteamresults",editteamresultsettingsReturnController:controllerName,editteamresultsettingsReturnID:params.id]
        } else {
            redirect(action:start)
        }
    }
    
    def printpoints = {
        if (session?.lastContest) {
            def contest = fcService.printpointsContest(session.lastContest,session.lastContest.printPointsA3,session.lastContest.printPointsLandscape,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(action:"editpoints")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"points",true,session.lastContest.printPointsA3,session.lastContest.printPointsLandscape)
            } else {
                redirect(action:"editpoints")
            }
        } else {
            redirect(action:start)
        }
    }
    
    def pointsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
    
    def createtestquestion = {
        return []
    }
    
    def createtest = {
		int contest_id = demoContestService.CreateTest(params.demoContest)
        redirect(controller:'contest',action:'activate',id:contest_id)
    }
            
    def runtest = {
        Map ret = demoContestService.RunTest(session.lastContest)
		flash.error = ret.error
		flash.message = ret.message
        redirect(controller:'contest',action:start)
    }

}
