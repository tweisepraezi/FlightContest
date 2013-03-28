import java.util.Date;
import java.util.Map;

class ContestController {
    
    def fcService

    def index = { redirect(action:start,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def start = {
        fcService.printstart "Start contest"
        
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
        if (session?.lastContest) {
            session.lastTaskResults = null
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
            // assign return action
            if (session.positionsReturnAction) {
                return [contestInstance:session.lastContest,positionsReturnAction:session.positionsReturnAction,positionsReturnController:session.positionsReturnController,positionsReturnID:session.positionsReturnID]
            }
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    def listteamresults = {
        if (session?.lastContest) {
            session.lastTaskResults = null
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

    def printtest = {
        if (session?.lastContest) {
            def contest = fcService.printtestContest(session.lastContest,false,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(controller:"contest",action:"listresults")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"test-normal")
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def printtest_landscape = {
        if (session?.lastContest) {
            def contest = fcService.printtestContest(session.lastContest,true,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(controller:"contest",action:"listresults")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"test-landscape")
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
            def contest = fcService.printresultsContest(session.lastContest,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(controller:"contest",action:"listresults")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"contestresults")
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
            def contest = fcService.printteamresultsContest(session.lastContest,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(action:"listteamresults")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"teamresults")
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
            def contest = fcService.printpointsContest(session.lastContest,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(action:"editpoints")
            } else if (contest.content) {
                fcService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"points")
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
    
    int create_test1(String testName, String printPrefix, boolean testExists) 
    {
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,false,2,ContestRules.R1,true,testExists)
        Map task1 = fcService.putTask(contest,"20. Februar ${Contest.DEMOCONTESTYEAR}","09:00",3,8,10,15,true,true,true,true,false, false,true, true,true,true, true,true,true,true, false)

		// Teams
		Map team1 = fcService.putTeam(contest,"Deutschland")
		Map team2 = fcService.putTeam(contest,"Schweiz")
		
        // Crews and Aircrafts
        Map crew1 = fcService.putCrew(contest,3,"Besatzung 3","Deutschland","K1","D-EAAA","","",85)
        Map crew2 = fcService.putCrew(contest,18,"Besatzung 18","Deutschland","K1","D-EAAD","","",80)
        Map crew3 = fcService.putCrew(contest,19,"Besatzung 19","Deutschland","K2","D-EAAE","","",80)
        Map crew4 = fcService.putCrew(contest,11,"Besatzung 11","Schweiz","K2","D-EAAB","","",70)
        Map crew5 = fcService.putCrew(contest,13,"Besatzung 13","Schweiz","K2","D-EAAC","","",70)
        
        // Route
        fcService.printstart "Route"
		Map route1 = fcService.importRoute(contest,"Strecke 1",SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK)
        fcService.printdone ""
        
        // Planning Test
        Map planningtest1 = fcService.putPlanningTest(task1,"")
        Map planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route1,130,20)
        fcService.putplanningtesttaskTask(task1,planningtesttask1)

        // Flight Test
        Map flighttest1 = fcService.putFlightTest(task1,"",route1)
        Map flighttestwind1 = fcService.putFlightTestWind(flighttest1,300,15)
        fcService.putflighttestwindTask(task1,flighttestwind1)
        
        // Planning
        fcService.putsequenceTask(task1,[crew1,crew4,crew5,crew3,crew2])
        fcService.puttimetableTask(task1,[[crew:crew1,starttime:'9:06'],
                                          [crew:crew4,starttime:'9:30'],
                                          [crew:crew5,starttime:'9:36'],
                                          [crew:crew3,starttime:'10:51'],
                                          [crew:crew2,starttime:'11:48']
                                         ])

        // Results
        fcService.putplanningresultsTask(task1,[[crew:crew1,
                                                 givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading: 98,legTime:"00:14:06"],
                                                              [trueHeading:205,legTime:"00:12:41"],
                                                              [trueHeading:154,legTime:"00:12:03"],
                                                              [trueHeading: 95,legTime:"00:09:56"],
                                                              [trueHeading:224,legTime:"00:07:43"],
                                                              [trueHeading:232,legTime:"00:04:25"]],
                                                 testComplete:true],
                                                [crew:crew4,
                                                 givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading: 99,legTime:"00:18:00"],
                                                              [trueHeading:203,legTime:"00:15:44"],
                                                              [trueHeading:153,legTime:"00:15:31"],
                                                              [trueHeading: 97,legTime:"00:12:42"],
                                                              [trueHeading:221,legTime:"00:09:21"],
                                                              [trueHeading:229,legTime:"00:05:20"]],
                                                 testComplete:true],
                                                [crew:crew5,
                                                 givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading:100,legTime:"00:18:03"],
                                                              [trueHeading:203,legTime:"00:15:37"],
                                                              [trueHeading:153,legTime:"00:15:35"],
                                                              [trueHeading: 98,legTime:"00:12:45"],
                                                              [trueHeading:221,legTime:"00:09:19"],
                                                              [trueHeading:229,legTime:"00:05:21"]],
                                                 testComplete:true],
                                                [crew:crew3,
                                                 givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading: 98,legTime:"00:15:11"],
                                                              [trueHeading:203,legTime:"00:13:35"],
                                                              [trueHeading:154,legTime:"00:13:04"],
                                                              [trueHeading:102,legTime:"00:10:57"],
                                                              [trueHeading:222,legTime:"00:08:19"],
                                                              [trueHeading:230,legTime:"00:04:42"]],
                                                 testComplete:true],
                                                [crew:crew2,
                                                 givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading: 98,legTime:"00:15:11"],
                                                              [trueHeading:204,legTime:"00:13:38"],
                                                              [trueHeading:153,legTime:"00:13:03"],
                                                              [trueHeading: 96,legTime:"00:10:43"],
                                                              [trueHeading:223,legTime:"00:08:10"],
                                                              [trueHeading:230,legTime:"00:04:44"]],
                                                 testComplete:true],
                                               ])
          
        fcService.importflightresultsTask(task1,[[crew:crew1,
                                                  startNum:3,
                                                  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
                                                  testComplete:true],
                                                 [crew:crew4,
                                                  startNum:11,
                                                  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
                                                  testComplete:true],
                                                 [crew:crew5,
                                                  startNum:13,
                                                  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
                                                  testComplete:true],
                                                 [crew:crew3,
                                                  startNum:19,
                                                  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
                                                  testComplete:true],
                                                 [crew:crew2,
                                                  startNum:18,
                                                  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
                                                  testComplete:true],
                                                ])
        fcService.putobservationresultsTask(task1, [
                                                    [crew:crew1,routePhotos:20,turnPointPhotos:0,groundTargets:0,testComplete:true],
                                                    [crew:crew2,routePhotos:0,turnPointPhotos:0,groundTargets:10,testComplete:true],
                                                    [crew:crew3,routePhotos:120,turnPointPhotos:0,groundTargets:10,testComplete:true],
                                                    [crew:crew4,routePhotos:0,turnPointPhotos:0,groundTargets:0,testComplete:true],
                                                    [crew:crew5,routePhotos:20,turnPointPhotos:0,groundTargets:0,testComplete:true],
                                                   ])
        fcService.putlandingresultsTask(task1, [
                                                [crew:crew1,
                                                 landingTest1Measure:'B',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:true,
                                                 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
                                                 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
                                                 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
                                                 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
                                                 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
                                                 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
                                                 landingTest4Measure:'0',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
                                                 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
                                                 landingTest4TouchingObstacle:false,
                                                 testComplete:true],
                                                [crew:crew2,
                                                 landingTest1Measure:'0',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
                                                 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
                                                 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
                                                 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
                                                 landingTest3Measure:'F',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
                                                 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
                                                 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
                                                 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
                                                 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
                                                 landingTest4TouchingObstacle:false,
                                                 testComplete:true],
                                                [crew:crew3,
                                                 landingTest1Measure:'B',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
                                                 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
                                                 landingTest2Measure:'A',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
                                                 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
                                                 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
                                                 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
                                                 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
                                                 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
                                                 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
                                                 landingTest4TouchingObstacle:false,
                                                 testComplete:true],
                                                [crew:crew4,
                                                 landingTest1Measure:'0',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
                                                 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
                                                 landingTest2Measure:'F',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
                                                 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
                                                 landingTest3Measure:'B',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
                                                 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
                                                 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
                                                 landingTest4Measure:'0',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
                                                 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
                                                 landingTest4TouchingObstacle:false,
                                                 testComplete:true],
                                                [crew:crew5,
                                                 landingTest1Measure:'E',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
                                                 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
                                                 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
                                                 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
                                                 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
                                                 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
                                                 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
                                                 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
                                                 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
                                                 landingTest4TouchingObstacle:false,
                                                 testComplete:true],
                                               ])
        fcService.runcalculatepositionsTask(task1)
        fcService.runcalculatecontestpositionsContest(contest,[],[task1],[team1,team2])
        fcService.runcalculateteampositionsContest(contest,[],[task1])

        fcService.printdone ""
        
		return contest.instance.id
    }
    
    int create_test2(String testName, String printPrefix, boolean testExists) 
    {
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,true,2,ContestRules.R1,true,testExists)
        Map task1 = fcService.putTask(contest,"20. Februar ${Contest.DEMOCONTESTYEAR}","09:00",3,8,10,15,true,true,true,true,false, false,true, true,true,true, false,false,false,false, false)

        // Classes with properties
        Map resultclass1 = fcService.putResultClass(contest,"Pr\u00E4zi","Pr\u00E4zisionsflugmeisterschaft",ContestRules.R1)
        Map resultclass2 = fcService.putResultClass(contest,"Tourist","",ContestRules.R1)
        
		// Teams
		Map team1 = fcService.putTeam(contest,"Deutschland")
		Map team2 = fcService.putTeam(contest,"Schweiz")
		
        // Crews with Teams, ResultClasses and Aircrafts
        Map crew1 = fcService.putCrew(contest,3,"Besatzung 3","Deutschland","Pr\u00E4zi","D-EAAA","","",85)
        Map crew2 = fcService.putCrew(contest,18,"Besatzung 18","Deutschland","Tourist","D-EAAD","","",80)
        Map crew3 = fcService.putCrew(contest,19,"Besatzung 19","Deutschland","Pr\u00E4zi","D-EAAE","","",80)
        Map crew4 = fcService.putCrew(contest,11,"Besatzung 11","Schweiz","Tourist","D-EAAB","","",70)
        Map crew5 = fcService.putCrew(contest,13,"Besatzung 13","Schweiz","Pr\u00E4zi","D-EAAC","","",70)
        
        // additional team
        Map team3 = fcService.putTeam(contest,'Polen')
        
        // TaskClass properties
        fcService.puttaskclassTask(task1,resultclass1,true,true,false,false,false, false,true, true,true,true, true,true,true,true)
        fcService.puttaskclassTask(task1,resultclass2,false,false,true,true,true, true,false, true,true,true, true,true,true,true)

        // additional class
        Map resultclass3 = fcService.putResultClass(contest,"Observer","",ContestRules.R1)
		fcService.puttaskclassTask(task1,resultclass3,true,true,true,true,false, false,true, true,true,true, false,false,false,false)
		
        // Route
        fcService.printstart "Route"
		Map route1 = fcService.importRoute(contest,"Strecke 1",SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK)
        fcService.printdone ""
        
        // Planning Test
        Map planningtest1 = fcService.putPlanningTest(task1,"")
        Map planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route1,130,20)
        fcService.putplanningtesttaskTask(task1,planningtesttask1)
        
        // Flight Test
        Map flighttest1 = fcService.putFlightTest(task1,"",route1)
        Map flighttestwind1 = fcService.putFlightTestWind(flighttest1,300,15)
        fcService.putflighttestwindTask(task1,flighttestwind1)
        
        // Planning
        fcService.putsequenceTask(task1,[crew1,crew4,crew5,crew3,crew2])
        fcService.puttimetableTask(task1,[[crew:crew1,starttime:'9:06'],
                                          [crew:crew4,starttime:'9:30'],
                                          [crew:crew5,starttime:'9:36'],
                                          [crew:crew3,starttime:'10:51'],
                                          [crew:crew2,starttime:'11:48']
                                         ])

        // Results
        fcService.putplanningresultsTask(task1,[[crew:crew1,
                                                 givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading: 98,legTime:"00:14:06"],
                                                              [trueHeading:205,legTime:"00:12:41"],
                                                              [trueHeading:154,legTime:"00:12:03"],
                                                              [trueHeading: 95,legTime:"00:09:56"],
                                                              [trueHeading:224,legTime:"00:07:43"],
                                                              [trueHeading:232,legTime:"00:04:25"]],
                                                 testComplete:true],
                                                [crew:crew4,
                                                 givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading: 99,legTime:"00:18:00"],
                                                              [trueHeading:203,legTime:"00:15:44"],
                                                              [trueHeading:153,legTime:"00:15:31"],
                                                              [trueHeading: 97,legTime:"00:12:42"],
                                                              [trueHeading:221,legTime:"00:09:21"],
                                                              [trueHeading:229,legTime:"00:05:20"]],
                                                 testComplete:true],
                                                [crew:crew5,
                                                 givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading:100,legTime:"00:18:03"],
                                                              [trueHeading:203,legTime:"00:15:37"],
                                                              [trueHeading:153,legTime:"00:15:35"],
                                                              [trueHeading: 98,legTime:"00:12:45"],
                                                              [trueHeading:221,legTime:"00:09:19"],
                                                              [trueHeading:229,legTime:"00:05:21"]],
                                                 testComplete:true],
                                                [crew:crew3,
                                                 givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading: 98,legTime:"00:15:11"],
                                                              [trueHeading:203,legTime:"00:13:35"],
                                                              [trueHeading:154,legTime:"00:13:04"],
                                                              [trueHeading:102,legTime:"00:10:57"],
                                                              [trueHeading:222,legTime:"00:08:19"],
                                                              [trueHeading:230,legTime:"00:04:42"]],
                                                 testComplete:true],
                                                [crew:crew2,
                                                 givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading: 98,legTime:"00:15:11"],
                                                              [trueHeading:204,legTime:"00:13:38"],
                                                              [trueHeading:153,legTime:"00:13:03"],
                                                              [trueHeading: 96,legTime:"00:10:43"],
                                                              [trueHeading:223,legTime:"00:08:10"],
                                                              [trueHeading:230,legTime:"00:04:44"]],
                                                 testComplete:true],
                                               ])
          
        fcService.importflightresultsTask(task1,[[crew:crew1,
                                                  startNum:3,
                                                  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
                                                  testComplete:true],
                                                 [crew:crew4,
                                                  startNum:11,
                                                  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
                                                  testComplete:true],
                                                 [crew:crew5,
                                                  startNum:13,
                                                  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
                                                  testComplete:true],
                                                 [crew:crew3,
                                                  startNum:19,
                                                  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
                                                  testComplete:true],
                                                 [crew:crew2,
                                                  startNum:18,
                                                  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
                                                  testComplete:true],
                                                ])
        fcService.putobservationresultsTask(task1, [
                                                    [crew:crew1,routePhotos:20,turnPointPhotos:0,groundTargets:0,testComplete:true],
                                                    [crew:crew2,routePhotos:0,turnPointPhotos:0,groundTargets:10,testComplete:true],
                                                    [crew:crew3,routePhotos:120,turnPointPhotos:0,groundTargets:10,testComplete:true],
                                                    [crew:crew4,routePhotos:0,turnPointPhotos:0,groundTargets:0,testComplete:true],
                                                    [crew:crew5,routePhotos:20,turnPointPhotos:0,groundTargets:0,testComplete:true],
                                                   ])
        fcService.putlandingresultsTask(task1, [
                                                [crew:crew1,
                                                 landingTest1Measure:'B',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:true,
                                                 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
                                                 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
                                                 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
                                                 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
                                                 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
                                                 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
                                                 landingTest4Measure:'0',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
                                                 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
                                                 landingTest4TouchingObstacle:false,
                                                 testComplete:true],
                                                [crew:crew2,
                                                 landingTest1Measure:'0',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
                                                 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
                                                 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
                                                 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
                                                 landingTest3Measure:'F',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
                                                 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
                                                 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
                                                 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
                                                 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
                                                 landingTest4TouchingObstacle:false,
                                                 testComplete:true],
                                                [crew:crew3,
                                                 landingTest1Measure:'B',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
                                                 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
                                                 landingTest2Measure:'A',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
                                                 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
                                                 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
                                                 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
                                                 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
                                                 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
                                                 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
                                                 landingTest4TouchingObstacle:false,
                                                 testComplete:true],
                                                [crew:crew4,
                                                 landingTest1Measure:'0',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
                                                 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
                                                 landingTest2Measure:'F',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
                                                 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
                                                 landingTest3Measure:'B',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
                                                 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
                                                 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
                                                 landingTest4Measure:'0',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
                                                 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
                                                 landingTest4TouchingObstacle:false,
                                                 testComplete:true],
                                                [crew:crew5,
                                                 landingTest1Measure:'E',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
                                                 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
                                                 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
                                                 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
                                                 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
                                                 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
                                                 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
                                                 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
                                                 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
                                                 landingTest4TouchingObstacle:false,
                                                 testComplete:true],
                                               ])
        fcService.runcalculatepositionsTask(task1)
        fcService.runcalculatepositionsResultClass(resultclass1,[task1],[team1,team2,team3])
        fcService.runcalculatepositionsResultClass(resultclass2,[task1],[team1,team2,team3])
        fcService.runcalculatepositionsResultClass(resultclass3,[task1],[team1,team2,team3])
		fcService.runcalculatecontestpositionsContest(contest,[resultclass1],[task1],[team1,team2,team3])
        fcService.runcalculateteampositionsContest(contest,[resultclass1,resultclass2,resultclass3],[task1])
        
        fcService.printdone ""
        
		return contest.instance.id
    }
    
    int create_test3(String testName, String printPrefix, boolean testExists) 
    {
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,true,0,ContestRules.R1,true,testExists) // 0 - keine Team-Auswertung
		
		// Route 1
		fcService.printstart "Route 1"
		Map route1 = fcService.importRoute(contest,"Strecke 1",SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK)
		fcService.printdone ""
		
		// Route 2
		fcService.printstart "Route 2"
		Map route2 = fcService.importRoute(contest,"Strecke 2",SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK)
		fcService.printdone ""
		
		// Teams
		Map team1 = fcService.putTeam(contest,"Deutschland")
		
		// Crews with Teams, ResultClasses and Aircrafts
		// DMM
		Map crew3 = fcService.putCrew(contest,3,"Besatzung 3","Deutschland","DMM","D-EAAA","","",85)
		Map crew18 = fcService.putCrew(contest,18,"Besatzung 18","Deutschland","DMM","D-EAAD","","",80)
		Map crew19 = fcService.putCrew(contest,19,"Besatzung 19","Deutschland","DMM","D-EAAE","","",80)
		Map crew11 = fcService.putCrew(contest,11,"Besatzung 11","Deutschland","DMM","D-EAAB","","",70)
		Map crew13 = fcService.putCrew(contest,13,"Besatzung 13","Deutschland","DMM","D-EAAC","","",70)
		// RuB-Wettbewerb
		Map crew1 = fcService.putCrew(contest,1,"Besatzung 1","Deutschland","RuB-Wettbewerb","D-EABA","","",85)
		Map crew2 = fcService.putCrew(contest,2,"Besatzung 2","Deutschland","RuB-Wettbewerb","D-EABB","","",70)
		Map crew4 = fcService.putCrew(contest,4,"Besatzung 4","Deutschland","RuB-Wettbewerb","D-EABC","","",90)
		Map crew5 = fcService.putCrew(contest,5,"Besatzung 5","Deutschland","RuB-Wettbewerb","D-EABD","","",90)
		Map crew7 = fcService.putCrew(contest,7,"Besatzung 7","Deutschland","RuB-Wettbewerb","D-EABE","","",80)
		// RuB-Tourist
		Map crew8 = fcService.putCrew(contest,8,"Besatzung 8","Deutschland","RuB-Tourist","D-EACA","","",70)
		Map crew9 = fcService.putCrew(contest,9,"Besatzung 9","Deutschland","RuB-Tourist","D-EACB","","",85)
		Map crew10 = fcService.putCrew(contest,10,"Besatzung 10","Deutschland","RuB-Tourist","D-EACC","","",75)
		Map crew12 = fcService.putCrew(contest,12,"Besatzung 12","Deutschland","RuB-Tourist","D-EACD","","",90)
		Map crew14 = fcService.putCrew(contest,14,"Besatzung 14","Deutschland","RuB-Tourist","D-EACE","","",100)
		
        // Classes with properties
        Map resultclass1 = fcService.putResultClass(contest,"DMM","Deutsche Motorflugmeisterschaft Navigationsflug",ContestRules.R1)
        Map resultclass2 = fcService.putResultClass(contest,"RuB-Wettbewerb","Rund um Berlin",ContestRules.R1)
		Map resultclass3 = fcService.putResultClass(contest,"RuB-Tourist","Rund um Berlin",ContestRules.R1)
		
		// Tasks - planningTestRun flightTestRun observationTestRun landingTestRun specialTestRun
		//         planningTestDistanceMeasure planningTestDirectionMeasure 
		//         landingTest1Run landingTest2Run landingTest3Run landingTest4Run
		
        // TaskClass properties
		
		// 1 - 23. August
        Map task1 = fcService.putTask(contest,"23. August ${Contest.DEMOCONTESTYEAR}","10:00",3,8,10,15,true,true,true,true,false,  false,true, true,false,false, true,true,true,true, false)
		
        fcService.puttaskclassTask(task1,resultclass1,true,true,true,true,false,      false,true, true,false,false, true,true,true,true)
        fcService.puttaskclassTask(task1,resultclass2,false,false,false,false,false,  false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task1,resultclass3,false,false,false,false,false,  false,true, true,false,false, false,false,false,false)
		
		Map planningtest1 = fcService.putPlanningTest(task1,"")
		Map planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route1,130,20)
		fcService.putplanningtesttaskcrewsTask(task1,planningtesttask1,[crew3,crew11,crew13,crew19,crew18])
		
		Map flighttest1 = fcService.putFlightTest(task1,"",route1)
		Map flighttestwind1 = fcService.putFlightTestWind(flighttest1,300,15)
		fcService.putflighttestwindcrewsTask(task1,flighttestwind1,[crew3,crew11,crew13,crew19,crew18])
		
		fcService.putsequenceTask(task1,[crew3,crew11,crew13,crew19,crew18])
		fcService.runcalculatetimetableTask(task1)
		
		// 2 - 24. August
		Map task2 = fcService.putTask(contest,"24. August ${Contest.DEMOCONTESTYEAR}","10:00",3,8,10,15,true,true,true,false,false, false,true, false,true,true, false,false,false,false, false)
		
        fcService.puttaskclassTask(task2,resultclass1,true,true,true,false,false,     false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task2,resultclass2,false,false,false,false,false,  false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task2,resultclass3,false,false,false,false,false,  false,true, true,false,false, false,false,false,false)
		
		Map planningtest2 = fcService.putPlanningTest(task2,"")
		Map planningtesttask2 = fcService.putPlanningTestTask(planningtest2,"",route2,130,20)
		fcService.putplanningtesttaskcrewsTask(task2,planningtesttask2,[crew3,crew11,crew13,crew19,crew18])
		
		Map flighttest2 = fcService.putFlightTest(task2,"",route2)
		Map flighttestwind2 = fcService.putFlightTestWind(flighttest2,300,15)
		fcService.putflighttestwindcrewsTask(task2,flighttestwind2,[crew3,crew11,crew13,crew19,crew18])
		
		fcService.putsequenceTask(task2,[crew3,crew11,crew13,crew19,crew18])
		fcService.runcalculatetimetableTask(task2)

		// 3 - 25. August
		Map task3 = fcService.putTask(contest,"25. August ${Contest.DEMOCONTESTYEAR}","10:00",3,8,10,15,true,true,true,true,false,  false,true, true,false,false, true,false,false,false, false)
		
		fcService.puttaskclassTask(task3,resultclass1,true,true,true,true,false,      false,true, true,false,false, true,false,false,false)
		fcService.puttaskclassTask(task3,resultclass2,true,true,true,true,false,      false,true, true,false,false, true,false,false,false)
		fcService.puttaskclassTask(task3,resultclass3,true,true,true,true,false,      false,true, true,false,false, true,false,false,false)
		
		Map planningtest3 = fcService.putPlanningTest(task3,"")
		Map planningtesttask3 = fcService.putPlanningTestTask(planningtest3,"",route1,130,20)
		fcService.putplanningtesttaskTask(task3,planningtesttask3)
		
		Map flighttest3 = fcService.putFlightTest(task3,"",route1)
		Map flighttestwind3 = fcService.putFlightTestWind(flighttest3,300,15)
		fcService.putflighttestwindTask(task3,flighttestwind3)
		
		fcService.putsequenceTask(task3,[crew14,crew4,crew5,crew12,crew3,crew1,crew9,crew18,crew19,crew7,crew10,crew11,crew13,crew2,crew8])
		fcService.runcalculatetimetableTask(task3)

        fcService.printdone ""
        
		return contest.instance.id
    }
    
    int create_test11(String testName, String printPrefix, boolean testExists) 
    {
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,false,2,ContestRules.R1,true,testExists)
        
        // Tasks
        Map task1 = fcService.putTask(contest,"Task-1","09:00",3,8,10,15,false,false, true,false,false, false,true, true,false,false, false,false,false,false, false)
        Map task2 = fcService.putTask(contest,"Task-2","09:00",3,8,10,15,false,false, false,true,false, false,true, true,false,false, false,false,false,false, false)

        // Crews with Teams, ResultClasses and Aircrafts
        Map crew11 = fcService.putCrew(contest,11,"Crew 1-1","Deutschland","","D-EAAA","","",85)
        Map crew12 = fcService.putCrew(contest,12,"Crew 1-2","Deutschland","","D-EAAE","","",80)
        Map crew13 = fcService.putCrew(contest,13,"Crew 1-3","Schweiz","","D-EAAC","","",70)
        Map crew21 = fcService.putCrew(contest,21,"Crew 2-1","Deutschland","","D-EAAD","","",80)
        Map crew22 = fcService.putCrew(contest,22,"Crew 2-2","Schweiz","","D-EAAB","","",70)
        Map crew31 = fcService.putCrew(contest,31,"Crew 3-1","Deutschland","","D-EAAF","","",80)
        Map crew32 = fcService.putCrew(contest,32,"Crew 3-2","Schweiz","","D-EAAG","","",70)

        fcService.putsequenceTask(task1,[crew11,crew12,crew13,crew21,crew22,crew31,crew32])
        fcService.putsequenceTask(task2,[crew11,crew12,crew13,crew21,crew22,crew31,crew32])
        
        fcService.putobservationresultsTask(task1, [
                                                    [crew:crew11,routePhotos: 20,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew12,routePhotos:  0,turnPointPhotos: 0,groundTargets:10,testComplete:true],
                                                    [crew:crew13,routePhotos:120,turnPointPhotos: 0,groundTargets:10,testComplete:true],
                                                    [crew:crew21,routePhotos:  0,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew22,routePhotos: 20,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew31,routePhotos: 40,turnPointPhotos:10,groundTargets:60,testComplete:true],
                                                    [crew:crew32,routePhotos: 30,turnPointPhotos:70,groundTargets:10,testComplete:true],
                                                   ])
        fcService.putobservationresultsTask(task2, [
                                                    [crew:crew11,routePhotos: 30,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew12,routePhotos:  0,turnPointPhotos: 0,groundTargets:90,testComplete:true],
                                                    [crew:crew13,routePhotos:130,turnPointPhotos: 0,groundTargets:80,testComplete:true],
                                                    [crew:crew21,routePhotos:  0,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew22,routePhotos: 30,turnPointPhotos: 0,groundTargets:60,testComplete:true],
                                                    [crew:crew31,routePhotos: 50,turnPointPhotos: 0,groundTargets:50,testComplete:true],
                                                    [crew:crew32,routePhotos: 40,turnPointPhotos:60,groundTargets: 0,testComplete:true],
                                                   ])
        fcService.putlandingresultsTask(task1, [
                                                [crew:crew11,landingPenalties:140,testComplete:true],
                                                [crew:crew12,landingPenalties:110,testComplete:true],
                                                [crew:crew13,landingPenalties: 80,testComplete:true],
                                                [crew:crew21,landingPenalties:130,testComplete:true],
                                                [crew:crew22,landingPenalties: 70,testComplete:true],
                                                [crew:crew31,landingPenalties: 90,testComplete:true],
                                                [crew:crew32,landingPenalties:170,testComplete:true],
                                               ])
        fcService.putlandingresultsTask(task2, [
                                                [crew:crew11,landingPenalties: 40,testComplete:true],
                                                [crew:crew12,landingPenalties: 10,testComplete:true],
                                                [crew:crew13,landingPenalties:180,testComplete:true],
                                                [crew:crew21,landingPenalties:230,testComplete:true],
                                                [crew:crew22,landingPenalties: 60,testComplete:true],
                                                [crew:crew31,landingPenalties:190,testComplete:true],
                                                [crew:crew32,landingPenalties:110,testComplete:true],
                                               ])
        
        fcService.putspecialresultsTask(task1, [
                                                [crew:crew11,specialPenalties: 80,testComplete:true],
                                                [crew:crew12,specialPenalties:150,testComplete:true],
                                                [crew:crew13,specialPenalties: 90,testComplete:true],
                                                [crew:crew21,specialPenalties:100,testComplete:true],
                                                [crew:crew22,specialPenalties:170,testComplete:true],
                                                [crew:crew31,specialPenalties: 50,testComplete:true],
                                                [crew:crew32,specialPenalties:120,testComplete:true],
                                               ])
        fcService.putspecialresultsTask(task2, [
                                                [crew:crew11,specialPenalties:280,testComplete:true],
                                                [crew:crew12,specialPenalties: 50,testComplete:true],
                                                [crew:crew13,specialPenalties: 30,testComplete:true],
                                                [crew:crew21,specialPenalties:120,testComplete:true],
                                                [crew:crew22,specialPenalties:220,testComplete:true],
                                                [crew:crew31,specialPenalties: 10,testComplete:true],
                                                [crew:crew32,specialPenalties: 90,testComplete:true],
                                               ])
        
		return contest.instance.id
    }
        
    int create_test12(String testName, String printPrefix, boolean testExists) 
    {
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,true,2,ContestRules.R1,true,testExists)
        
        // Tasks
        Map task1 = fcService.putTask(contest,"Task-1","09:00",3,8,10,15,true,true,true,true,false, false,true, true,false,false, false,false,false,false, false)
        Map task2 = fcService.putTask(contest,"Task-2","09:00",3,8,10,15,true,true,true,true,false, false,true, true,false,false, false,false,false,false, false)

        // Classes with properties
        Map resultclass1 = fcService.putResultClass(contest,"Class-1","Pr\u00E4zisionsflugmeisterschaft",ContestRules.R1)
        Map resultclass2 = fcService.putResultClass(contest,"Class-2","",ContestRules.R1)
        Map resultclass3 = fcService.putResultClass(contest,"Class-3","",ContestRules.R1)
        
        // Crews with Teams, ResultClasses and Aircrafts
        Map crew11 = fcService.putCrew(contest,11,"Crew 1-1","Deutschland","Class-1","D-EAAA","","",85)
        Map crew12 = fcService.putCrew(contest,12,"Crew 1-2","Deutschland","Class-1","D-EAAE","","",80)
        Map crew13 = fcService.putCrew(contest,13,"Crew 1-3","Schweiz","Class-1","D-EAAC","","",70)
        Map crew21 = fcService.putCrew(contest,21,"Crew 2-1","Deutschland","Class-2","D-EAAD","","",80)
        Map crew22 = fcService.putCrew(contest,22,"Crew 2-2","Schweiz","Class-2","D-EAAB","","",70)
        Map crew31 = fcService.putCrew(contest,31,"Crew 3-1","Deutschland","Class-3","D-EAAF","","",80)
        Map crew32 = fcService.putCrew(contest,32,"Crew 3-2","Schweiz","Class-3","D-EAAG","","",70)

        // TaskClass properties
        fcService.puttaskclassTask(task1,resultclass1,false,false, true,true,true, false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task1,resultclass2,false,false, true,true,true, false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task1,resultclass3,false,false, true,true,true, false,true, true,false,false, false,false,false,false)
        
        // TaskClass properties
        fcService.puttaskclassTask(task2,resultclass1,false,false, true,false,false, false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task2,resultclass2,false,false, false,true,false, false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task2,resultclass3,false,false, false,false,true, false,true, true,false,false, false,false,false,false)
        
        fcService.putsequenceTask(task1,[crew11,crew12,crew13,crew21,crew22,crew31,crew32])
        fcService.putsequenceTask(task2,[crew11,crew12,crew13,crew21,crew22,crew31,crew32])
        
        fcService.putobservationresultsTask(task1, [
                                                    [crew:crew11,routePhotos: 20,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew12,routePhotos:  0,turnPointPhotos: 0,groundTargets:10,testComplete:true],
                                                    [crew:crew13,routePhotos:120,turnPointPhotos: 0,groundTargets:10,testComplete:true],
                                                    [crew:crew21,routePhotos:  0,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew22,routePhotos: 20,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew31,routePhotos: 40,turnPointPhotos:10,groundTargets:60,testComplete:true],
                                                    [crew:crew32,routePhotos: 30,turnPointPhotos:70,groundTargets:10,testComplete:true],
                                                   ])
        fcService.putobservationresultsTask(task2, [
                                                    [crew:crew11,routePhotos: 30,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew12,routePhotos:  0,turnPointPhotos: 0,groundTargets:90,testComplete:true],
                                                    [crew:crew13,routePhotos:130,turnPointPhotos: 0,groundTargets:80,testComplete:true],
                                                    [crew:crew21,routePhotos:  0,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew22,routePhotos: 30,turnPointPhotos: 0,groundTargets:60,testComplete:true],
                                                    [crew:crew31,routePhotos: 50,turnPointPhotos: 0,groundTargets:50,testComplete:true],
                                                    [crew:crew32,routePhotos: 40,turnPointPhotos:60,groundTargets: 0,testComplete:true],
                                                   ])
        fcService.putlandingresultsTask(task1, [
                                                [crew:crew11,landingPenalties:140,testComplete:true],
                                                [crew:crew12,landingPenalties:110,testComplete:true],
                                                [crew:crew13,landingPenalties: 80,testComplete:true],
                                                [crew:crew21,landingPenalties:130,testComplete:true],
                                                [crew:crew22,landingPenalties: 70,testComplete:true],
                                                [crew:crew31,landingPenalties: 90,testComplete:true],
                                                [crew:crew32,landingPenalties:170,testComplete:true],
                                               ])
        fcService.putlandingresultsTask(task2, [
                                                [crew:crew11,landingPenalties: 40,testComplete:true],
                                                [crew:crew12,landingPenalties: 10,testComplete:true],
                                                [crew:crew13,landingPenalties:180,testComplete:true],
                                                [crew:crew21,landingPenalties:230,testComplete:true],
                                                [crew:crew22,landingPenalties: 60,testComplete:true],
                                                [crew:crew31,landingPenalties:190,testComplete:true],
                                                [crew:crew32,landingPenalties:110,testComplete:true],
                                               ])
        
        fcService.putspecialresultsTask(task1, [
                                                [crew:crew11,specialPenalties: 80,testComplete:true],
                                                [crew:crew12,specialPenalties:150,testComplete:true],
                                                [crew:crew13,specialPenalties: 90,testComplete:true],
                                                [crew:crew21,specialPenalties:100,testComplete:true],
                                                [crew:crew22,specialPenalties:170,testComplete:true],
                                                [crew:crew31,specialPenalties: 50,testComplete:true],
                                                [crew:crew32,specialPenalties:120,testComplete:true],
                                               ])
        fcService.putspecialresultsTask(task2, [
                                                [crew:crew11,specialPenalties:280,testComplete:true],
                                                [crew:crew12,specialPenalties: 50,testComplete:true],
                                                [crew:crew13,specialPenalties: 30,testComplete:true],
                                                [crew:crew21,specialPenalties:120,testComplete:true],
                                                [crew:crew22,specialPenalties:220,testComplete:true],
                                                [crew:crew31,specialPenalties: 10,testComplete:true],
                                                [crew:crew32,specialPenalties: 90,testComplete:true],
                                               ])
        
		return contest.instance.id
    }

    int create_test13(String testName, String printPrefix, boolean testExists) 
    {
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,false,0,ContestRules.R1,true,testExists)
        Map task1 = fcService.putTask(contest,"","11:00",3,10,10,15,true,true,true,true,false, false,true, true,false,false, false,false,false,false, false)
    
        // Crews and Aircrafts
        (1..100).each {
            fcService.putCrew(contest,it,"Name-${it.toString()}","Deutschland","","D-${it.toString()}","C172","rot",110)
        }
        
        fcService.printdone ""
        
		return contest.instance.id
    }

    int create_test14(String testName, String printPrefix, boolean testExists) 
    {
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,false,0,ContestRules.R1,true,testExists)
        Map task1 = fcService.putTask(contest,"","11:00",3,10,10,15,true,true,true,true,false, false,true, true,false,false, false,false,false,false, false)
    
        // Crews and Aircrafts
        (1..20).each {
            fcService.putCrew(contest,it,"Name-${it.toString()}","Deutschland","","D-${it.toString()}","C172","rot",110)
        }
        
        fcService.printdone ""
        
		return contest.instance.id
    }

    List test1Route() {
      [[title:"Strecke 1",mark:"Strecke 1"]]
    }
    List test1CoordRoute() {
      [[type:CoordType.TO,    mark:"T/O",
        latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        coordTrueTrack:0,coordMeasureDistance:0,
        measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0],
       [type:CoordType.SP,    mark:"SP",
        latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:0,coordMeasureDistance:0,
        measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0],
       [type:CoordType.SECRET,mark:"CP1",
        latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:88.8048175589,coordMeasureDistance:99.4440780950,
        measureTrueTrack:89.0,measureDistance:99.0,legMeasureDistance:99.0,legDistance:10.6911447084,secretLegRatio:0.6598765432],
       [type:CoordType.TP,    mark:"CP2",
        latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:88.8465166117,coordMeasureDistance:149.9578513483,
        measureTrueTrack:89.0,measureDistance:150.0,legMeasureDistance:51.0,legDistance:5.5075593952,secretLegRatio:0],
       [type:CoordType.SECRET,mark:"CP3",
        latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:219.3292360731,coordMeasureDistance:46.1613175609,
        measureTrueTrack:219.0,measureDistance:46.0,legMeasureDistance:46.0,legDistance:4.9676025918,secretLegRatio:0.2849770642],
       [type:CoordType.TP,    mark:"CP4",
        latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:219.2221790621,coordMeasureDistance:161.4135491992,
        measureTrueTrack:219.0,measureDistance:161.5,legMeasureDistance:115.5,legDistance:12.4730021598,secretLegRatio:0],
       [type:CoordType.SECRET,mark:"CP5",
        latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:160.8781319787,coordMeasureDistance:69.4482392126,
        measureTrueTrack:161.0,measureDistance:68.5,legMeasureDistance:68.5,legDistance:7.3974082073,secretLegRatio:0.5481481481],
       [type:CoordType.TP,    mark:"CP6",
        latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:160.9135697323,coordMeasureDistance:126.1284575235,
        measureTrueTrack:161.0,measureDistance:125.0,legMeasureDistance:56.5,legDistance:6.1015118790,secretLegRatio:0],
       [type:CoordType.SECRET,mark:"CP7",
        latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:86.3563657892,coordMeasureDistance:19.8166630762,
        measureTrueTrack:86.0,measureDistance:19.5,legMeasureDistance:19.5,legDistance:2.1058315335,secretLegRatio:0.1834782609],
       [type:CoordType.TP,    mark:"CP8",
        latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:86.5776194402,coordMeasureDistance:106.7215296566,
        measureTrueTrack:86.0,measureDistance:106.45,legMeasureDistance:86.95,legDistance:9.3898488121,secretLegRatio:0],
       [type:CoordType.SECRET,mark:"CP9",
        latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:237.2378249684,coordMeasureDistance:25.9240833138,
        measureTrueTrack:237.0,measureDistance:25.5,legMeasureDistance:25.5,legDistance:2.7537796976,secretLegRatio:0.2420774648],
       [type:CoordType.TP,    mark:"CP10",
        latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:237.1829094762,coordMeasureDistance:104.8407146901,
        measureTrueTrack:237.0,measureDistance:105.2,legMeasureDistance:79.7,legDistance:8.6069114471,secretLegRatio:0],
       [type:CoordType.FP,    mark:"FP",
        latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:242.9619395538,coordMeasureDistance:62.0480855033,
        measureTrueTrack:244.0,measureDistance:62.4,legMeasureDistance:62.4,legDistance:6.7386609071,secretLegRatio:0],
       [type:CoordType.LDG,   mark:"LDG",
        latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        coordTrueTrack:256.4547835436,coordMeasureDistance:33.4955075819,
        measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0]
      ]
    }
    List test1RouteLegCoord() {
      [[coordTrueTrack:48.2896815944,coordDistance:4.0974514721,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null],
       [coordTrueTrack:88.8048175589,coordDistance:10.7391013062,measureDistance:99.0,legMeasureDistance:99.0,legDistance:10.6911447084,measureTrueTrack:89.0],
       [coordTrueTrack:88.9286028152,coordDistance:5.4550359042,measureDistance:150.0,legMeasureDistance:51.0,legDistance:5.5075593952,measureTrueTrack:89.0],
       [coordTrueTrack:219.3292360731,coordDistance:4.9850234947,measureDistance:46.0,legMeasureDistance:46.0,legDistance:4.9676025918,measureTrueTrack:219.0],
       [coordTrueTrack:219.1791259770,coordDistance:12.4462266624,measureDistance:161.5,legMeasureDistance:115.5,legDistance:12.4730021598,measureTrueTrack:219.0],
       [coordTrueTrack:160.8781319787,coordDistance:7.4998098502,measureDistance:68.5,legMeasureDistance:68.5,legDistance:7.3974082073,measureTrueTrack:161.0],
       [coordTrueTrack:160.9571657647,coordDistance:6.1209706224,measureDistance:125.0,legMeasureDistance:56.5,legDistance:6.1015118790,measureTrueTrack:161.0],
       [coordTrueTrack:86.3563657892,coordDistance:2.1400284100,measureDistance:19.5,legMeasureDistance:19.5,legDistance:2.1058315335,measureTrueTrack:86.0],
       [coordTrueTrack:86.6280647396,coordDistance:9.3849768707,measureDistance:106.45,legMeasureDistance:86.95,legDistance:9.3898488121,measureTrueTrack:86.0],
       [coordTrueTrack:237.2378249684,coordDistance:2.7995770317,measureDistance:25.5,legMeasureDistance:25.5,legDistance:2.7537796976,measureTrueTrack:237.0],
       [coordTrueTrack:237.1648386550,coordDistance:8.5223089414,measureDistance:105.2,legMeasureDistance:79.7,legDistance:8.6069114471,measureTrueTrack:237.0],
       [coordTrueTrack:242.9619395538,coordDistance:6.7006571818,measureDistance:62.4,legMeasureDistance:62.4,legDistance:6.7386609071,measureTrueTrack:244.0],
       [coordTrueTrack:256.4547835436,coordDistance:3.6172254408,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null],
      ]
    }
    List test1RouteLegTest() {
      [[coordTrueTrack:88.8465166117,coordDistance:16.2,
        measureTrueTrack:89.0,measureDistance:150.0,legMeasureDistance:150.0,legDistance:16.1987041037],
       [coordTrueTrack:219.2221790621,coordDistance:17.44,
        measureTrueTrack:219.0,measureDistance:161.5,legMeasureDistance:161.5,legDistance:17.4406047516],
       [coordTrueTrack:160.9135697323,coordDistance:13.62,
        measureTrueTrack:161.0,measureDistance:125.0,legMeasureDistance:125.0,legDistance:13.4989200864],
       [coordTrueTrack:86.5776194402,coordDistance:11.52,
        measureTrueTrack:86.0,measureDistance:106.45,legMeasureDistance:106.45,legDistance:11.4956803456],
       [coordTrueTrack:237.1829094762,coordDistance:11.32,
        measureTrueTrack:237.0,measureDistance:105.2,legMeasureDistance:105.2,legDistance:11.3606911447],
       [coordTrueTrack:242.9619395538,coordDistance:6.7,
        measureTrueTrack:244.0,measureDistance:62.4,legMeasureDistance:62.4,legDistance:6.7386609071],
      ]
    }
    List test1Crew() {
       [[startNum:3, name:"Besatzung 3", mark:"3: Besatzung 3",  team:[name:"Deutschland"],tas:85,contestPenalties:215,contestPosition:3,noContestPosition:false,aircraft:[registration:"D-EAAA",type:"",colour:""]],
        [startNum:18,name:"Besatzung 18",mark:"18: Besatzung 18",team:[name:"Deutschland"],tas:80,contestPenalties:133,contestPosition:1,noContestPosition:false,aircraft:[registration:"D-EAAD",type:"",colour:""]],
        [startNum:19,name:"Besatzung 19",mark:"19: Besatzung 19",team:[name:"Deutschland"],tas:80,contestPenalties:568,contestPosition:5,noContestPosition:false,aircraft:[registration:"D-EAAE",type:"",colour:""]],
        [startNum:11,name:"Besatzung 11",mark:"11: Besatzung 11",team:[name:"Schweiz"],    tas:70,contestPenalties:383,contestPosition:4,noContestPosition:false,aircraft:[registration:"D-EAAB",type:"",colour:""]],
        [startNum:13,name:"Besatzung 13",mark:"13: Besatzung 13",team:[name:"Schweiz"],    tas:70,contestPenalties:135,contestPosition:2,noContestPosition:false,aircraft:[registration:"D-EAAC",type:"",colour:""]],
       ]
    }
    List test1Aircraft() {
       [[registration:"D-EAAA",type:"",colour:"",user1:[name:"Besatzung 3"],user2:null],
        [registration:"D-EAAD",type:"",colour:"",user1:[name:"Besatzung 18"],user2:null],
        [registration:"D-EAAE",type:"",colour:"",user1:[name:"Besatzung 19"],user2:null],
        [registration:"D-EAAB",type:"",colour:"",user1:[name:"Besatzung 11"],user2:null],
        [registration:"D-EAAC",type:"",colour:"",user1:[name:"Besatzung 13"],user2:null],
       ]
    }
    List test1Team() {
      [[name:"Deutschland",contestPenalties:348,contestPosition:1],
       [name:"Schweiz",    contestPenalties:518,contestPosition:2],
      ]
    }
    List test1Task() {
      [[title:"20. Februar ${Contest.DEMOCONTESTYEAR}",firstTime:"09:00",takeoffIntervalNormal:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
        preparationDuration:15,risingDuration:8,maxLandingDuration:10,parkingDuration:15,minNextFlightDuration:30,
        procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true]
      ]
    }
    List test1TestLegPlanning3() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:97.8800763462,planGroundSpeed:68.8869647355,planLegTime:0.2351678589,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.235,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:205.3931519488,planGroundSpeed:82.2652474538,planLegTime:0.2119971743,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:205,resultGroundSpeed:0,resultLegTime:0.2113888889,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:154.0394779952,planGroundSpeed:67.2301943440,planLegTime:0.2008026324,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:154,resultGroundSpeed:0,resultLegTime:0.2008333333,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:95.4071472127,planGroundSpeed:69.4701053153,planLegTime:0.1655388307,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:95,resultGroundSpeed:0,resultLegTime:0.1655555556,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:223.9963642250,planGroundSpeed:88.6676760978,planLegTime:0.1281188422,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:224,resultGroundSpeed:0,resultLegTime:0.1286111111,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:231.5872962201,planGroundSpeed:91.1478274836,planLegTime:0.0739458107,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:232,resultGroundSpeed:0,resultLegTime:0.0736111111,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
    List test1TestLegPlanning18() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:98.4400045050,planGroundSpeed:63.8224392169,planLegTime:0.2538292205,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.2530555556,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:204.5247409558,planGroundSpeed:77.1114052303,planLegTime:0.2261662843,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:204,resultGroundSpeed:0,resultLegTime:0.2272222222,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:153.6020844648,planGroundSpeed:62.1907217788,planLegTime:0.2170741811,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2175,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:96.0009550175,planGroundSpeed:64.3975926711,planLegTime:0.1785781040,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:96,resultGroundSpeed:0,resultLegTime:0.1786111111,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:223.1679772858,planGroundSpeed:83.5274990113,planLegTime:0.1360031144,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:223,resultGroundSpeed:0,resultLegTime:0.1361111111,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:230.7979111274,planGroundSpeed:86.0203790277,planLegTime:0.0783535259,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:230,resultGroundSpeed:0,resultLegTime:0.0788888889,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
    List test1TestLegPlanning19() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:98.4400045050,planGroundSpeed:63.8224392169,planLegTime:0.2538292205,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.2530555556,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:204.5247409558,planGroundSpeed:77.1114052303,planLegTime:0.2261662843,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:203,resultGroundSpeed:0,resultLegTime:0.2263888889,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:153.6020844648,planGroundSpeed:62.1907217788,planLegTime:0.2170741811,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:154,resultGroundSpeed:0,resultLegTime:0.2177777778,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:96.0009550175,planGroundSpeed:64.3975926711,planLegTime:0.1785781040,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:102,resultGroundSpeed:0,resultLegTime:0.1825,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:8,penaltyLegTime:9
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:223.1679772858,planGroundSpeed:83.5274990113,planLegTime:0.1360031144,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:222,resultGroundSpeed:0,resultLegTime:0.1386111111,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:4
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:230.7979111274,planGroundSpeed:86.0203790277,planLegTime:0.0783535259,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:230,resultGroundSpeed:0,resultLegTime:0.0783333333,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
    List test1TestLegPlanning11() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:99.8037402076,planGroundSpeed:53.6650595563,planLegTime:0.3018723940,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:99,resultGroundSpeed:0,resultLegTime:0.3,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:2
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:202.4010521006,planGroundSpeed:66.7338992916,planLegTime:0.2613364450,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:203,resultGroundSpeed:0,resultLegTime:0.2622222222,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:152.5379827855,planGroundSpeed:52.0946082636,planLegTime:0.2591439009,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2586111111,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:97.4477263633,planGroundSpeed:54.2206372592,planLegTime:0.2120963637,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:97,resultGroundSpeed:0,resultLegTime:0.2116666667,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:221.1434427012,planGroundSpeed:73.1838468701,planLegTime:0.1552255106,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:221,resultGroundSpeed:0,resultLegTime:0.1558333333,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:228.8698222549,planGroundSpeed:75.7082030901,planLegTime:0.0890260200,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:229,resultGroundSpeed:0,resultLegTime:0.0888888889,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }     
    List test1TestLegPlanning13() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:99.8037402076,planGroundSpeed:53.6650595563,planLegTime:0.3018723940,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:100,resultGroundSpeed:0,resultLegTime:0.3008333333,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:202.4010521006,planGroundSpeed:66.7338992916,planLegTime:0.2613364450,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:203,resultGroundSpeed:0,resultLegTime:0.2602777778,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:152.5379827855,planGroundSpeed:52.0946082636,planLegTime:0.2591439009,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2597222222,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:97.4477263633,planGroundSpeed:54.2206372592,planLegTime:0.2120963637,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.2125,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:221.1434427012,planGroundSpeed:73.1838468701,planLegTime:0.1552255106,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:221,resultGroundSpeed:0,resultLegTime:0.1552777778,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:228.8698222549,planGroundSpeed:75.7082030901,planLegTime:0.0890260200,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:229,resultGroundSpeed:0,resultLegTime:0.0891666667,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
    List test1TestLegFlight3() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:83.7852431943,planGroundSpeed:97.5056964422,planLegTime:0.1661441392,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:229.0378052961,planGroundSpeed:81.3523847013,planLegTime:0.2143760145,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:167.6483349505,planGroundSpeed:95.7490570122,planLegTime:0.1409935557,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:80.3367725496,planGroundSpeed:97.0206878228,planLegTime:0.1185314211,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:246.0465258632,planGroundSpeed:77.1328262775,planLegTime:0.1472784098,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:252.4126098880,planGroundSpeed:75.6975199169,planLegTime:0.0890385842,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
    List test1TestLegFlight18() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:83.4583310658,planGroundSpeed:92.4836079382,planLegTime:0.1751661766,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:229.6723011898,planGroundSpeed:76.2696781875,planLegTime:0.2286622995,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:168.0659119292,planGroundSpeed:90.7130676105,planLegTime:0.1488208960,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:79.9815521701,planGroundSpeed:91.9946186466,planLegTime:0.1250073120,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:246.6171413877,planGroundSpeed:72.0658305143,planLegTime:0.1576336513,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:252.9425792987,planGroundSpeed:70.6396760565,planLegTime:0.0954138011,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
    List test1TestLegFlight19() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:83.4583310658,planGroundSpeed:92.4836079382,planLegTime:0.1751661766,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:229.6723011898,planGroundSpeed:76.2696781875,planLegTime:0.2286622995,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:168.0659119292,planGroundSpeed:90.7130676105,planLegTime:0.1488208960,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:79.9815521701,planGroundSpeed:91.9946186466,planLegTime:0.1250073120,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:246.6171413877,planGroundSpeed:72.0658305143,planLegTime:0.1576336513,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:252.9425792987,planGroundSpeed:70.6396760565,planLegTime:0.0954138011,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
    List test1TestLegFlight11() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:82.6636259635,planGroundSpeed:82.4298858593,planLegTime:0.1965306616,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:231.2189175362,planGroundSpeed:66.0677077289,planLegTime:0.2639716224,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:169.0816457025,planGroundSpeed:80.6254557575,planLegTime:0.1674409140,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:79.1178787047,planGroundSpeed:81.9311982840,planLegTime:0.1403616722,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:248.0070611206,planGroundSpeed:61.9023987550,planLegTime:0.1835146978,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:254.2329616616,planGroundSpeed:60.4986563560,planLegTime:0.1114074329,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
    List test1TestLegFlight13() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:82.6636259635,planGroundSpeed:82.4298858593,planLegTime:0.1965306616,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:231.2189175362,planGroundSpeed:66.0677077289,planLegTime:0.2639716224,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:169.0816457025,planGroundSpeed:80.6254557575,planLegTime:0.1674409140,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:79.1178787047,planGroundSpeed:81.9311982840,planLegTime:0.1403616722,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:248.0070611206,planGroundSpeed:61.9023987550,planLegTime:0.1835146978,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:254.2329616616,planGroundSpeed:60.4986563560,planLegTime:0.1114074329,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
    List test1CoordResult3() {
      [[type:CoordType.TO,    mark:"T/O", latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","10:21:00"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 02,04630'",resultLongitude:"E 013\u00b0 43,84060'",resultAltitude:214,
        resultCpTime:Date.parse("HH:mm:ss","10:21:09"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","10:29:00"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 04,84870'",resultLongitude:"E 013\u00b0 49,21010'",resultAltitude:1375,
        resultCpTime:Date.parse("HH:mm:ss","10:29:03"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","10:35:35"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,29710'",resultLongitude:"E 014\u00b0 06,67800'",resultAltitude:1409,
        resultCpTime:Date.parse("HH:mm:ss","10:35:46"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:9
       ],
       [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","10:38:58"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,24970'",resultLongitude:"E 014\u00b0 15,53960'",resultAltitude:1609,
        resultCpTime:Date.parse("HH:mm:ss","10:38:56"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","10:43:38"),planProcedureTurn:true,
        resultLatitude:"N 052\u00b0 01,29270'",resultLongitude:"E 014\u00b0 10,54200'",resultAltitude:1399,
        resultCpTime:Date.parse("HH:mm:ss","10:43:39"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","10:52:50"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 51,69550'",resultLongitude:"E 013\u00b0 57,68880'",resultAltitude:1629,
        resultCpTime:Date.parse("HH:mm:ss","10:52:55"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:3
       ],
       [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","10:57:28"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 44,68110'",resultLongitude:"E 014\u00b0 01,81410'",resultAltitude:1496,
        resultCpTime:Date.parse("HH:mm:ss","10:57:41"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:11
       ],
       [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:01:18"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,88340'",resultLongitude:"E 014\u00b0 04,96680'",resultAltitude:1569,
        resultCpTime:Date.parse("HH:mm:ss","11:01:24"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:4
       ],
       [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:02:36"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,11140'",resultLongitude:"E 014\u00b0 08,27210'",resultAltitude:1707,
        resultCpTime:Date.parse("HH:mm:ss","11:02:55"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:17
       ],
       [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:08:25"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,52840'",resultLongitude:"E 014\u00b0 23,41510'",resultAltitude:1523,
        resultCpTime:Date.parse("HH:mm:ss","11:08:23"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:11:33"),planProcedureTurn:true,
        resultLatitude:"N 051\u00b0 37,97560'",resultLongitude:"E 014\u00b0 19,65030'",resultAltitude:1387,
        resultCpTime:Date.parse("HH:mm:ss","11:11:21"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:10
       ],
       [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:18:15"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 33,44220'",resultLongitude:"E 014\u00b0 08,05000'",resultAltitude:1615,
        resultCpTime:Date.parse("HH:mm:ss","11:18:17"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:23:36"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 30,34350'",resultLongitude:"E 013\u00b0 58,49930'",resultAltitude:1358,
        resultCpTime:Date.parse("HH:mm:ss","11:23:37"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.LDG,   mark:"LDG", latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:33:36"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 29,50800'",resultLongitude:"E 013\u00b0 52,83280'",resultAltitude:337,
        resultCpTime:Date.parse("HH:mm:ss","11:27:25"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
      ]
    }
    List test1CoordResult18() {
      [[type:CoordType.TO,    mark:"T/O", latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:03:00"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 02,00640'",resultLongitude:"E 013\u00b0 44,01730'",resultAltitude:216,
        resultCpTime:Date.parse("HH:mm:ss","13:03:08"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:11:00"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 04,83500'",resultLongitude:"E 013\u00b0 49,22100'",resultAltitude:1170,
        resultCpTime:Date.parse("HH:mm:ss","13:11:03"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:17:56"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,17800'",resultLongitude:"E 014\u00b0 06,68980'",resultAltitude:1236,
        resultCpTime:Date.parse("HH:mm:ss","13:17:55"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:21:31"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,13820'",resultLongitude:"E 014\u00b0 15,54840'",resultAltitude:1290,
        resultCpTime:Date.parse("HH:mm:ss","13:21:32"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:26:26"),planProcedureTurn:true,
        resultLatitude:"N 052\u00b0 01,27670'",resultLongitude:"E 014\u00b0 10,60120'",resultAltitude:1198,
        resultCpTime:Date.parse("HH:mm:ss","13:26:27"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:36:14"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 51,74450'",resultLongitude:"E 013\u00b0 57,59730'",resultAltitude:965,
        resultCpTime:Date.parse("HH:mm:ss","13:36:19"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:3
       ],
       [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:41:08"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 44,66360'",resultLongitude:"E 014\u00b0 01,74150'",resultAltitude:1225,
        resultCpTime:Date.parse("HH:mm:ss","13:41:07"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:45:10"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,83570'",resultLongitude:"E 014\u00b0 04,76570'",resultAltitude:1395,
        resultCpTime:Date.parse("HH:mm:ss","13:45:10"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:46:33"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,90780'",resultLongitude:"E 014\u00b0 08,31320'",resultAltitude:1405,
        resultCpTime:Date.parse("HH:mm:ss","13:46:41"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:6
       ],
       [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:52:40"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,52900'",resultLongitude:"E 014\u00b0 23,38740'",resultAltitude:2166,
        resultCpTime:Date.parse("HH:mm:ss","13:52:44"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:2
       ],
       [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:55:57"),planProcedureTurn:true,
        resultLatitude:"N 051\u00b0 38,01820'",resultLongitude:"E 014\u00b0 19,59790'",resultAltitude:1822,
        resultCpTime:Date.parse("HH:mm:ss","13:55:56"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","14:03:07"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 33,42810'",resultLongitude:"E 014\u00b0 08,03690'",resultAltitude:1384,
        resultCpTime:Date.parse("HH:mm:ss","14:03:09"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","14:08:50"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 30,43140'",resultLongitude:"E 013\u00b0 58,42820'",resultAltitude:1502,
        resultCpTime:Date.parse("HH:mm:ss","14:08:53"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.LDG,   mark:"LDG", latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","14:18:50"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 29,50580'",resultLongitude:"E 013\u00b0 52,83610'",resultAltitude:307,
        resultCpTime:Date.parse("HH:mm:ss","14:12:10"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
      ]
    }
    List test1CoordResult19() {
      [[type:CoordType.TO,    mark:"T/O", latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:06:00"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 02,01150'",resultLongitude:"E 013\u00b0 43,90980'",resultAltitude:188,
        resultCpTime:Date.parse("HH:mm:ss","12:06:06"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:14:00"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,00160'",resultLongitude:"E 013\u00b0 49,20850'",resultAltitude:1001,
        resultCpTime:Date.parse("HH:mm:ss","12:13:37"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:21
       ],
       [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:20:56"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,29600'",resultLongitude:"E 014\u00b0 06,67890'",resultAltitude:1490,
        resultCpTime:Date.parse("HH:mm:ss","12:20:40"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:14
       ],
       [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:24:31"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,37350'",resultLongitude:"E 014\u00b0 15,56770'",resultAltitude:1595,
        resultCpTime:Date.parse("HH:mm:ss","12:24:13"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:16
       ],
       [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:29:26"),planProcedureTurn:true,
        resultLatitude:"N 052\u00b0 01,12430'",resultLongitude:"E 014\u00b0 10,91000'",resultAltitude:1412,
        resultCpTime:Date.parse("HH:mm:ss","12:29:45"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:17
       ],
       [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:39:14"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 51,53060'",resultLongitude:"E 013\u00b0 58,04620'",resultAltitude:1435,
        resultCpTime:Date.parse("HH:mm:ss","12:39:32"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:16
       ],
       [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:44:08"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 44,66070'",resultLongitude:"E 014\u00b0 01,74090'",resultAltitude:1923,
        resultCpTime:Date.parse("HH:mm:ss","12:44:21"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:11
       ],
       [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:48:10"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,90240'",resultLongitude:"E 014\u00b0 05,07120'",resultAltitude:2058,
        resultCpTime:Date.parse("HH:mm:ss","12:48:05"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:3
       ],
       [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:49:33"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,64160'",resultLongitude:"E 014\u00b0 08,33550'",resultAltitude:1687,
        resultCpTime:Date.parse("HH:mm:ss","12:49:46"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:11
       ],
       [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:55:40"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,61940'",resultLongitude:"E 014\u00b0 23,36750'",resultAltitude:1774,
        resultCpTime:Date.parse("HH:mm:ss","12:55:56"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:14
       ],
       [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:58:57"),planProcedureTurn:true,
        resultLatitude:"N 051\u00b0 37,31980'",resultLongitude:"E 014\u00b0 20,31900'",resultAltitude:2359,
        resultCpTime:Date.parse("HH:mm:ss","12:58:58"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:06:07"),planProcedureTurn:false,
        resultLatitude:"",resultLongitude:"",resultAltitude:0,
        resultCpTime:Date.parse("HH:mm:ss","02:00:00"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:true,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:200
       ],
       [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:11:50"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 30,21180'",resultLongitude:"E 013\u00b0 58,60650'",resultAltitude:1291,
        resultCpTime:Date.parse("HH:mm:ss","13:12:06"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:14
       ],
       [type:CoordType.LDG,   mark:"LDG", latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","13:21:50"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 29,50790'",resultLongitude:"E 013\u00b0 52,83230'",resultAltitude:305,
        resultCpTime:Date.parse("HH:mm:ss","13:16:59"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
      ]
    }
    List test1CoordResult11() {
      [[type:CoordType.TO,    mark:"T/O", latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","10:45:00"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 02,01130'",resultLongitude:"E 013\u00b0 43,93320'",resultAltitude:200,
        resultCpTime:Date.parse("HH:mm:ss","10:45:18"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","10:53:00"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 04,66460'",resultLongitude:"E 013\u00b0 49,22740'",resultAltitude:1678,
        resultCpTime:Date.parse("HH:mm:ss","10:53:04"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:2
       ],
       [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:00:47"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 04,93310'",resultLongitude:"E 014\u00b0 06,67050'",resultAltitude:1816,
        resultCpTime:Date.parse("HH:mm:ss","11:00:57"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:8
       ],
       [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:04:48"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,15430'",resultLongitude:"E 014\u00b0 15,57610'",resultAltitude:1622,
        resultCpTime:Date.parse("HH:mm:ss","11:04:54"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:4
       ],
       [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:10:19"),planProcedureTurn:true,
        resultLatitude:"N 052\u00b0 01,22360'",resultLongitude:"E 014\u00b0 10,69030'",resultAltitude:1659,
        resultCpTime:Date.parse("HH:mm:ss","11:10:38"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:17
       ],
       [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:21:38"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 51,80050'",resultLongitude:"E 013\u00b0 57,51850'",resultAltitude:1571,
        resultCpTime:Date.parse("HH:mm:ss","11:21:46"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:6
       ],
       [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:27:08"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 44,63940'",resultLongitude:"E 014\u00b0 01,71880'",resultAltitude:1970,
        resultCpTime:Date.parse("HH:mm:ss","11:27:10"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:31:41"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,83070'",resultLongitude:"E 014\u00b0 04,75630'",resultAltitude:1827,
        resultCpTime:Date.parse("HH:mm:ss","11:31:46"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:3
       ],
       [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:33:14"),planProcedureTurn:false,
        resultLatitude:"",resultLongitude:"",resultAltitude:0,
        resultCpTime:Date.parse("HH:mm:ss","02:00:00"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:true,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:200
       ],
       [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:40:06"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,39500'",resultLongitude:"E 014\u00b0 23,40800'",resultAltitude:1839,
        resultCpTime:Date.parse("HH:mm:ss","11:40:09"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:43:46"),planProcedureTurn:true,
        resultLatitude:"N 051\u00b0 38,01920'",resultLongitude:"E 014\u00b0 19,61850'",resultAltitude:2007,
        resultCpTime:Date.parse("HH:mm:ss","11:43:44"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:52:07"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 33,47440'",resultLongitude:"E 014\u00b0 07,98900'",resultAltitude:1574,
        resultCpTime:Date.parse("HH:mm:ss","11:52:14"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:5
       ],
       [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:58:48"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 30,43450'",resultLongitude:"E 013\u00b0 58,43000'",resultAltitude:1644,
        resultCpTime:Date.parse("HH:mm:ss","11:58:55"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:5
       ],
       [type:CoordType.LDG,   mark:"LDG", latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:08:48"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 29,50910'",resultLongitude:"E 013\u00b0 52,83120'",resultAltitude:321,
        resultCpTime:Date.parse("HH:mm:ss","12:02:55"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
      ]
    }
    List test1CoordResult13() {
      [[type:CoordType.TO,    mark:"T/O", latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","10:51:00"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 02,01860'",resultLongitude:"E 013\u00b0 43,87860'",resultAltitude:211,
        resultCpTime:Date.parse("HH:mm:ss","10:51:07"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","10:59:00"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 04,81010'",resultLongitude:"E 013\u00b0 49,21410'",resultAltitude:985,
        resultCpTime:Date.parse("HH:mm:ss","10:58:59"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:06:47"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,00180'",resultLongitude:"E 014\u00b0 06,69910'",resultAltitude:1293,
        resultCpTime:Date.parse("HH:mm:ss","11:06:50"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:10:48"),planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,15960'",resultLongitude:"E 014\u00b0 15,55880'",resultAltitude:1240,
        resultCpTime:Date.parse("HH:mm:ss","11:10:53"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:3
       ],
       [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:16:19"),planProcedureTurn:true,
        resultLatitude:"N 052\u00b0 01,26100'",resultLongitude:"E 014\u00b0 10,64230'",resultAltitude:1281,
        resultCpTime:Date.parse("HH:mm:ss","11:16:09"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:8
       ],
       [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:27:38"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 51,74850'",resultLongitude:"E 013\u00b0 57,59490'",resultAltitude:1424,
        resultCpTime:Date.parse("HH:mm:ss","11:27:38"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:33:08"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 44,63130'",resultLongitude:"E 014\u00b0 01,65960'",resultAltitude:1307,
        resultCpTime:Date.parse("HH:mm:ss","11:32:51"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:15
       ],
       [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:37:41"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,85720'",resultLongitude:"E 014\u00b0 04,88720'",resultAltitude:1149,
        resultCpTime:Date.parse("HH:mm:ss","11:37:38"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:39:14"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,76200'",resultLongitude:"E 014\u00b0 08,31770'",resultAltitude:995,
        resultCpTime:Date.parse("HH:mm:ss","11:39:25"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:9
       ],
       [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:46:06"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,46530'",resultLongitude:"E 014\u00b0 23,41820'",resultAltitude:895,
        resultCpTime:Date.parse("HH:mm:ss","11:46:10"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:2
       ],
       [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:49:46"),planProcedureTurn:true,
        resultLatitude:"N 051\u00b0 37,98390'",resultLongitude:"E 014\u00b0 19,65190'",resultAltitude:1212,
        resultCpTime:Date.parse("HH:mm:ss","11:49:50"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:2
       ],
       [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","11:58:07"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 33,27590'",resultLongitude:"E 014\u00b0 08,20140'",resultAltitude:1389,
        resultCpTime:Date.parse("HH:mm:ss","11:58:13"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:4
       ],
       [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:04:48"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 30,42410'",resultLongitude:"E 013\u00b0 58,43490'",resultAltitude:1299,
        resultCpTime:Date.parse("HH:mm:ss","12:04:50"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.LDG,   mark:"LDG", latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:Date.parse("HH:mm:ss","12:14:48"),planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 29,50860'",resultLongitude:"E 013\u00b0 52,83220'",resultAltitude:320,
        resultCpTime:Date.parse("HH:mm:ss","12:12:41"),resultCpTimeInput:"00:00:00",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
      ]
    }
    List test1Test() {
      [[crew:[name:"Besatzung 3"],viewpos:0,taskTAS:85,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:Date.parse("HH:mm","09:06"),
        endTestingTime:Date.parse("HH:mm","10:06"),
        takeoffTime:Date.parse("HH:mm","10:21"),
        startTime:Date.parse("HH:mm","10:29"),
        finishTime:Date.parse("HH:mm:ss","11:23:36"),
        maxLandingTime:Date.parse("HH:mm:ss","11:33:36"),
        arrivalTime:Date.parse("HH:mm:ss","11:38:36"),
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:55,flightTestPenalties:55,
        observationTestRoutePhotoPenalties:20,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:20,landingTestPenalties:140,
        taskPenalties:215,taskPosition:3
       ],
       [crew:[name:"Besatzung 18"],viewpos:4,taskTAS:80,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:Date.parse("HH:mm","11:48"),
        endTestingTime:Date.parse("HH:mm","12:48"),
        takeoffTime:Date.parse("HH:mm","13:03"),
        startTime:Date.parse("HH:mm","13:11"),
        finishTime:Date.parse("HH:mm:ss","14:08:50"),
        maxLandingTime:Date.parse("HH:mm:ss","14:18:50"),
        arrivalTime:Date.parse("HH:mm:ss","14:23:50"),
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:13,flightTestPenalties:13,
        observationTestRoutePhotoPenalties:0,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:10,observationTestPenalties:10,landingTestPenalties:110,
        taskPenalties:133,taskPosition:1
       ],
       [crew:[name:"Besatzung 19"],viewpos:3,taskTAS:80,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:Date.parse("HH:mm","10:51"),
        endTestingTime:Date.parse("HH:mm","11:51"),
        takeoffTime:Date.parse("HH:mm","12:06"),
        startTime:Date.parse("HH:mm","12:14"),
        finishTime:Date.parse("HH:mm:ss","13:11:50"),
        maxLandingTime:Date.parse("HH:mm:ss","13:21:50"),
        arrivalTime:Date.parse("HH:mm:ss","13:26:50"),
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:21,planningTestPenalties:21,
        flightTestCheckPointPenalties:337,flightTestPenalties:337,
        observationTestRoutePhotoPenalties:120,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:10,observationTestPenalties:130,landingTestPenalties:80,
        taskPenalties:568,taskPosition:5
       ],
       [crew:[name:"Besatzung 11"],viewpos:1,taskTAS:70,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:Date.parse("HH:mm","09:30"),
        endTestingTime:Date.parse("HH:mm","10:30"),
        takeoffTime:Date.parse("HH:mm","10:45"),
        startTime:Date.parse("HH:mm","10:53"),
        finishTime:Date.parse("HH:mm:ss","11:58:48"),
        maxLandingTime:Date.parse("HH:mm:ss","12:08:48"),
        arrivalTime:Date.parse("HH:mm:ss","12:13:48"),
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:2,planningTestPenalties:2,
        flightTestCheckPointPenalties:251,flightTestPenalties:251,
        observationTestRoutePhotoPenalties:0,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:0,landingTestPenalties:130,
        taskPenalties:383,taskPosition:4
       ],
       [crew:[name:"Besatzung 13"],viewpos:2,taskTAS:70,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:Date.parse("HH:mm","09:36"),
        endTestingTime:Date.parse("HH:mm","10:36"),
        takeoffTime:Date.parse("HH:mm","10:51"),
        startTime:Date.parse("HH:mm","10:59"),
        finishTime:Date.parse("HH:mm:ss","12:04:48"),
        maxLandingTime:Date.parse("HH:mm:ss","12:14:48"),
        arrivalTime:Date.parse("HH:mm:ss","12:19:48"),
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:45,flightTestPenalties:45,
        observationTestRoutePhotoPenalties:20,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:20,landingTestPenalties:70,
        taskPenalties:135,taskPosition:2
       ],
      ]
    }
    
    void run_test1(String contestName)
    {
        if (session?.lastContest && session.lastContest.title == contestName) {
            fcService.printstart "runtest '$session.lastContest.title'"
            Map ret = fcService.testData(
               [[name:"Route",count:1,table:Route.findAllByContest(session.lastContest,[sort:"id"]),data:test1Route()],
                [name:"CoordRoute",count:14,table:CoordRoute.findAllByRoute(Route.findByContest(session.lastContest),[sort:"id"]),data:test1CoordRoute()],
                [name:"RouteLegCoord",count:13,table:RouteLegCoord.findAllByRoute(Route.findByContest(session.lastContest),[sort:"id"]),data:test1RouteLegCoord()],
                [name:"RouteLegTest",count:6,table:RouteLegTest.findAllByRoute(Route.findByContest(session.lastContest),[sort:"id"]),data:test1RouteLegTest()],
                [name:"Crew",count:5,table:Crew.findAllByContest(session.lastContest,[sort:"id"]),data:test1Crew()],
                [name:"Aircraft",count:5,table:Aircraft.findAllByContest(session.lastContest,[sort:"id"]),data:test1Aircraft()],
                [name:"Team",count:2,table:Team.findAllByContest(session.lastContest,[sort:"id"]),data:test1Team()],
                [name:"Task",count:1,table:Task.findAllByContest(session.lastContest,[sort:"id"]),data:test1Task()],
                [name:"TestLegPlanning 'Besatzung 3'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegPlanning3()],
                [name:"TestLegPlanning 'Besatzung 18'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 18")),[sort:"id"]),data:test1TestLegPlanning18()],
                [name:"TestLegPlanning 'Besatzung 19'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegPlanning19()],
                [name:"TestLegPlanning 'Besatzung 11'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 11")),[sort:"id"]),data:test1TestLegPlanning11()],
                [name:"TestLegPlanning 'Besatzung 13'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegPlanning13()],
                [name:"TestLegFlight 'Besatzung 3'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegFlight3()],
                [name:"TestLegFlight 'Besatzung 18'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 18")),[sort:"id"]),data:test1TestLegFlight18()],
                [name:"TestLegFlight 'Besatzung 19'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegFlight19()],
                [name:"TestLegFlight 'Besatzung 11'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 11")),[sort:"id"]),data:test1TestLegFlight11()],
                [name:"TestLegFlight 'Besatzung 13'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegFlight13()],
                [name:"CoordResult 'Besatzung 3'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 3")),[sort:"id"]),data:test1CoordResult3()],    
                [name:"CoordResult 'Besatzung 18'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 18")),[sort:"id"]),data:test1CoordResult18()],    
                [name:"CoordResult 'Besatzung 19'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 19")),[sort:"id"]),data:test1CoordResult19()],    
                [name:"CoordResult 'Besatzung 11'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 11")),[sort:"id"]),data:test1CoordResult11()],    
                [name:"CoordResult 'Besatzung 13'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 13")),[sort:"id"]),data:test1CoordResult13()],    
                [name:"Test",count:5,table:Test.findAllByTask(Task.findByContest(session.lastContest),[sort:"id"]),data:test1Test()],
                ]
            )
            fcService.printdone "Test '$session.lastContest.title'"
            flash.error = ret.error
            flash.message = ret.msgtext
        } else {
            flash.error = true
            flash.message = "No test found."
        }
    }

    List test2Crew() {
      [[startNum:3, name:"Besatzung 3", mark:"3: Besatzung 3",  team:[name:"Deutschland"],resultclass:[name:"Pr\u00E4zi",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],tas:85,contestPenalties:55, classPosition:2,noClassPosition:false,contestPosition:2,noContestPosition:false,aircraft:[registration:"D-EAAA",type:"",colour:""]],
       [startNum:18,name:"Besatzung 18",mark:"18: Besatzung 18",team:[name:"Deutschland"],resultclass:[name:"Tourist",   contestTitle:""],                                tas:80,contestPenalties:120,classPosition:1,noClassPosition:false,contestPosition:0,noContestPosition:true,aircraft:[registration:"D-EAAD",type:"",colour:""]],
       [startNum:19,name:"Besatzung 19",mark:"19: Besatzung 19",team:[name:"Deutschland"],resultclass:[name:"Pr\u00E4zi",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],tas:80,contestPenalties:358,classPosition:3,noClassPosition:false,contestPosition:3,noContestPosition:false,aircraft:[registration:"D-EAAE",type:"",colour:""]],
       [startNum:11,name:"Besatzung 11",mark:"11: Besatzung 11",team:[name:"Schweiz"],    resultclass:[name:"Tourist",   contestTitle:""],                                tas:70,contestPenalties:130,classPosition:2,noClassPosition:false,contestPosition:0,noContestPosition:true,aircraft:[registration:"D-EAAB",type:"",colour:""]],
       [startNum:13,name:"Besatzung 13",mark:"13: Besatzung 13",team:[name:"Schweiz"],    resultclass:[name:"Pr\u00E4zi",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],tas:70,contestPenalties:45, classPosition:1,noClassPosition:false,contestPosition:1,noContestPosition:false,aircraft:[registration:"D-EAAC",type:"",colour:""]],
      ]
    }
    List test2ResultClass() {
      [[name:"Pr\u00E4zi",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],
       [name:"Tourist",contestTitle:""],
       [name:"Observer",contestTitle:""],
      ]
    }
    List test2Team() {
      [[name:"Deutschland",contestPenalties:175,contestPosition:1],
       [name:"Schweiz",    contestPenalties:175,contestPosition:1],
       [name:'Polen',      contestPenalties:0,  contestPosition:0],
      ]    
	}
    List test2TaskClass() {
      [[resultclass:[name:"Pr\u00E4zi",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],planningTestRun:true,flightTestRun:true,observationTestRun:false,landingTestRun:false,specialTestRun:false,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
       [resultclass:[name:"Tourist",contestTitle:""],planningTestRun:false,flightTestRun:false,observationTestRun:true,landingTestRun:true,specialTestRun:true,planningTestDistanceMeasure:true,planningTestDirectionMeasure:false],
       [resultclass:[name:"Observer",contestTitle:""],planningTestRun:true,flightTestRun:true,observationTestRun:true,landingTestRun:true,specialTestRun:false,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
      ]
    }
    List test2TestLegPlanning18() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:98.4400045050,planGroundSpeed:63.8224392169,planLegTime:0.2538292205,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:89.0,resultTestDistance:0,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.2530555556,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:204.5247409558,planGroundSpeed:77.1114052303,planLegTime:0.2261662843,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:219.0,resultTestDistance:0,resultTrueHeading:204,resultGroundSpeed:0,resultLegTime:0.2272222222,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:153.6020844648,planGroundSpeed:62.1907217788,planLegTime:0.2170741811,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:161.0,resultTestDistance:0,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2175,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:96.0009550175,planGroundSpeed:64.3975926711,planLegTime:0.1785781040,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:86.0,resultTestDistance:0,resultTrueHeading:96,resultGroundSpeed:0,resultLegTime:0.1786111111,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:223.1679772858,planGroundSpeed:83.5274990113,planLegTime:0.1360031144,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:237.0,resultTestDistance:0,resultTrueHeading:223,resultGroundSpeed:0,resultLegTime:0.1361111111,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:230.7979111274,planGroundSpeed:86.0203790277,planLegTime:0.0783535259,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:244.0,resultTestDistance:0,resultTrueHeading:230,resultGroundSpeed:0,resultLegTime:0.0788888889,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
    List test2TestLegPlanning11() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:99.8037402076,planGroundSpeed:53.6650595563,planLegTime:0.3018723940,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:89.0,resultTestDistance:0,resultTrueHeading:99,resultGroundSpeed:0,resultLegTime:0.3,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:2
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:202.4010521006,planGroundSpeed:66.7338992916,planLegTime:0.2613364450,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:219.0,resultTestDistance:0,resultTrueHeading:203,resultGroundSpeed:0,resultLegTime:0.2622222222,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:152.5379827855,planGroundSpeed:52.0946082636,planLegTime:0.2591439009,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:161.0,resultTestDistance:0,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2586111111,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:97.4477263633,planGroundSpeed:54.2206372592,planLegTime:0.2120963637,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:86.0,resultTestDistance:0,resultTrueHeading:97,resultGroundSpeed:0,resultLegTime:0.2116666667,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:221.1434427012,planGroundSpeed:73.1838468701,planLegTime:0.1552255106,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:237.0,resultTestDistance:0,resultTrueHeading:221,resultGroundSpeed:0,resultLegTime:0.1558333333,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:228.8698222549,planGroundSpeed:75.7082030901,planLegTime:0.0890260200,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:244.0,resultTestDistance:0,resultTrueHeading:229,resultGroundSpeed:0,resultLegTime:0.0888888889,
        resultEntered:true,resultLegTimeInput:"00:00:00",
        penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
    List test2Test() {
      [[crew:[name:"Besatzung 3"],viewpos:0,taskTAS:85,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:Date.parse("HH:mm","09:06"),
        endTestingTime:Date.parse("HH:mm","10:06"),
        takeoffTime:Date.parse("HH:mm","10:21"),
        startTime:Date.parse("HH:mm","10:29"),
        finishTime:Date.parse("HH:mm:ss","11:23:36"),
        maxLandingTime:Date.parse("HH:mm:ss","11:33:36"),
        arrivalTime:Date.parse("HH:mm:ss","11:38:36"),
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:55,flightTestPenalties:55,
        observationTestRoutePhotoPenalties:20,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:20,landingTestPenalties:140,
        taskPenalties:55,taskPosition:2
       ],
       [crew:[name:"Besatzung 18"],viewpos:4,taskTAS:80,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:Date.parse("HH:mm","11:48"),
        endTestingTime:Date.parse("HH:mm","12:48"),
        takeoffTime:Date.parse("HH:mm","13:03"),
        startTime:Date.parse("HH:mm","13:11"),
        finishTime:Date.parse("HH:mm:ss","14:08:50"),
        maxLandingTime:Date.parse("HH:mm:ss","14:18:50"),
        arrivalTime:Date.parse("HH:mm:ss","14:23:50"),
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:13,flightTestPenalties:13,
        observationTestRoutePhotoPenalties:0,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:10,observationTestPenalties:10,landingTestPenalties:110,
        taskPenalties:120,taskPosition:1
       ],
       [crew:[name:"Besatzung 19"],viewpos:3,taskTAS:80,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:Date.parse("HH:mm","10:51"),
        endTestingTime:Date.parse("HH:mm","11:51"),
        takeoffTime:Date.parse("HH:mm","12:06"),
        startTime:Date.parse("HH:mm","12:14"),
        finishTime:Date.parse("HH:mm:ss","13:11:50"),
        maxLandingTime:Date.parse("HH:mm:ss","13:21:50"),
        arrivalTime:Date.parse("HH:mm:ss","13:26:50"),
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:21,planningTestPenalties:21,
        flightTestCheckPointPenalties:337,flightTestPenalties:337,
        observationTestRoutePhotoPenalties:120,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:10,observationTestPenalties:130,landingTestPenalties:80,
        taskPenalties:358,taskPosition:3
       ],
       [crew:[name:"Besatzung 11"],viewpos:1,taskTAS:70,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:Date.parse("HH:mm","09:30"),
        endTestingTime:Date.parse("HH:mm","10:30"),
        takeoffTime:Date.parse("HH:mm","10:45"),
        startTime:Date.parse("HH:mm","10:53"),
        finishTime:Date.parse("HH:mm:ss","11:58:48"),
        maxLandingTime:Date.parse("HH:mm:ss","12:08:48"),
        arrivalTime:Date.parse("HH:mm:ss","12:13:48"),
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:2,planningTestPenalties:2,
        flightTestCheckPointPenalties:251,flightTestPenalties:251,
        observationTestRoutePhotoPenalties:0,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:0,landingTestPenalties:130,
        taskPenalties:130,taskPosition:2
       ],
       [crew:[name:"Besatzung 13"],viewpos:2,taskTAS:70,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:Date.parse("HH:mm","09:36"),
        endTestingTime:Date.parse("HH:mm","10:36"),
        takeoffTime:Date.parse("HH:mm","10:51"),
        startTime:Date.parse("HH:mm","10:59"),
        finishTime:Date.parse("HH:mm:ss","12:04:48"),
        maxLandingTime:Date.parse("HH:mm:ss","12:14:48"),
        arrivalTime:Date.parse("HH:mm:ss","12:19:48"),
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:45,flightTestPenalties:45,
        observationTestRoutePhotoPenalties:20,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:20,landingTestPenalties:70,
        taskPenalties:45,taskPosition:1
       ],
      ]
    }
    
    void run_test2(String contestName)
    {
        if (session?.lastContest && session.lastContest.title == contestName) {
            fcService.printstart "runtest '$session.lastContest.title'"
            Map ret = fcService.testData(
               [[name:"Route",count:1,table:Route.findAllByContest(session.lastContest,[sort:"id"]),data:test1Route()],
                [name:"CoordRoute",count:14,table:CoordRoute.findAllByRoute(Route.findByContest(session.lastContest),[sort:"id"]),data:test1CoordRoute()],
                [name:"RouteLegCoord",count:13,table:RouteLegCoord.findAllByRoute(Route.findByContest(session.lastContest),[sort:"id"]),data:test1RouteLegCoord()],
                [name:"RouteLegTest",count:6,table:RouteLegTest.findAllByRoute(Route.findByContest(session.lastContest),[sort:"id"]),data:test1RouteLegTest()],
                [name:"Crew",count:5,table:Crew.findAllByContest(session.lastContest,[sort:"id"]),data:test2Crew()],
                [name:"Aircraft",count:5,table:Aircraft.findAllByContest(session.lastContest,[sort:"id"]),data:test1Aircraft()],
                [name:"ResultClass",count:3,table:ResultClass.findAllByContest(session.lastContest,[sort:"id"]),data:test2ResultClass()],
                [name:"Team",count:3,table:Team.findAllByContest(session.lastContest,[sort:"id"]),data:test2Team()],
                [name:"Task",count:1,table:Task.findAllByContest(session.lastContest,[sort:"id"]),data:test1Task()],
                [name:"TaskClass",count:3,table:TaskClass.findAllByTask(Task.findByContest(session.lastContest),[sort:"id"]),data:test2TaskClass()],
                [name:"TestLegPlanning 'Besatzung 3'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegPlanning3()],
                [name:"TestLegPlanning 'Besatzung 18'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 18")),[sort:"id"]),data:test2TestLegPlanning18()],
                [name:"TestLegPlanning 'Besatzung 19'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegPlanning19()],
                [name:"TestLegPlanning 'Besatzung 11'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 11")),[sort:"id"]),data:test2TestLegPlanning11()],
                [name:"TestLegPlanning 'Besatzung 13'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegPlanning13()],
                [name:"TestLegFlight 'Besatzung 3'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegFlight3()],
                [name:"TestLegFlight 'Besatzung 18'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 18")),[sort:"id"]),data:test1TestLegFlight18()],
                [name:"TestLegFlight 'Besatzung 19'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegFlight19()],
                [name:"TestLegFlight 'Besatzung 11'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 11")),[sort:"id"]),data:test1TestLegFlight11()],
                [name:"TestLegFlight 'Besatzung 13'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegFlight13()],
                [name:"CoordResult 'Besatzung 3'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 3")),[sort:"id"]),data:test1CoordResult3()],    
                [name:"CoordResult 'Besatzung 18'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 18")),[sort:"id"]),data:test1CoordResult18()],    
                [name:"CoordResult 'Besatzung 19'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 19")),[sort:"id"]),data:test1CoordResult19()],    
                [name:"CoordResult 'Besatzung 11'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 11")),[sort:"id"]),data:test1CoordResult11()],    
                [name:"CoordResult 'Besatzung 13'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 13")),[sort:"id"]),data:test1CoordResult13()],    
                [name:"Test",count:5,table:Test.findAllByTask(Task.findByContest(session.lastContest),[sort:"id"]),data:test2Test()],
                ]
            )
            fcService.printdone "Test '$session.lastContest.title'"
            flash.error = ret.error
            flash.message = ret.msgtext
        } else {
            flash.error = true
            flash.message = "No test found."
        }
    }

    void run_test3(String contestName)
    {
        if (session?.lastContest && session.lastContest.title == contestName) {
            fcService.printstart "runtest '$session.lastContest.title'"
            Map ret = fcService.testData(
               [[name:"Route",count:1,table:Route.findAllByContest(session.lastContest,[sort:"id"]),data:test1Route()],
                [name:"CoordRoute",count:14,table:CoordRoute.findAllByRoute(Route.findByContest(session.lastContest),[sort:"id"]),data:test1CoordRoute()],
                [name:"RouteLegCoord",count:13,table:RouteLegCoord.findAllByRoute(Route.findByContest(session.lastContest),[sort:"id"]),data:test1RouteLegCoord()],
                [name:"RouteLegTest",count:6,table:RouteLegTest.findAllByRoute(Route.findByContest(session.lastContest),[sort:"id"]),data:test1RouteLegTest()],
                [name:"Crew",count:5,table:Crew.findAllByContest(session.lastContest,[sort:"id"]),data:test2Crew()],
                [name:"Aircraft",count:5,table:Aircraft.findAllByContest(session.lastContest,[sort:"id"]),data:test1Aircraft()],
                [name:"ResultClass",count:2,table:ResultClass.findAllByContest(session.lastContest,[sort:"id"]),data:test3ResultClass()],
                [name:"Team",count:2,table:Team.findAllByContest(session.lastContest,[sort:"id"]),data:test3Team()],
                [name:"Task",count:1,table:Task.findAllByContest(session.lastContest,[sort:"id"]),data:test1Task()],
                [name:"TaskClass",count:2,table:TaskClass.findAllByTask(Task.findByContest(session.lastContest),[sort:"id"]),data:test3TaskClass()],
                [name:"TestLegPlanning 'Besatzung 3'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegPlanning3()],
                [name:"TestLegPlanning 'Besatzung 18'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 18")),[sort:"id"]),data:test2TestLegPlanning18()],
                [name:"TestLegPlanning 'Besatzung 19'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegPlanning19()],
                [name:"TestLegPlanning 'Besatzung 11'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 11")),[sort:"id"]),data:test2TestLegPlanning11()],
                [name:"TestLegPlanning 'Besatzung 13'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegPlanning13()],
                [name:"TestLegFlight 'Besatzung 3'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegFlight3()],
                [name:"TestLegFlight 'Besatzung 18'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 18")),[sort:"id"]),data:test1TestLegFlight18()],
                [name:"TestLegFlight 'Besatzung 19'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegFlight19()],
                [name:"TestLegFlight 'Besatzung 11'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 11")),[sort:"id"]),data:test1TestLegFlight11()],
                [name:"TestLegFlight 'Besatzung 13'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegFlight13()],
                [name:"CoordResult 'Besatzung 3'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 3")),[sort:"id"]),data:test1CoordResult3()],    
                [name:"CoordResult 'Besatzung 18'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 18")),[sort:"id"]),data:test1CoordResult18()],    
                [name:"CoordResult 'Besatzung 19'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 19")),[sort:"id"]),data:test1CoordResult19()],    
                [name:"CoordResult 'Besatzung 11'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 11")),[sort:"id"]),data:test1CoordResult11()],    
                [name:"CoordResult 'Besatzung 13'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 13")),[sort:"id"]),data:test1CoordResult13()],    
                [name:"Test",count:5,table:Test.findAllByTask(Task.findByContest(session.lastContest),[sort:"id"]),data:test2Test()],
                ]
            )
            fcService.printdone "Test '$session.lastContest.title'"
            flash.error = ret.error
            flash.message = ret.msgtext
        } else {
            flash.error = true
            flash.message = "No test found."
        }
    }

    List test3Team() {
      [[name:"Deutschland",contestPenalties:175,contestPosition:1],
       [name:"Schweiz",    contestPenalties:175,contestPosition:1],
      ]    
	}
    List test3TaskClass() {
      [[resultclass:[name:"Pr\u00E4zi",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],planningTestRun:true,flightTestRun:true,observationTestRun:false,landingTestRun:false,specialTestRun:false,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
       [resultclass:[name:"Tourist",contestTitle:""],planningTestRun:false,flightTestRun:false,observationTestRun:true,landingTestRun:true,specialTestRun:true,planningTestDistanceMeasure:true,planningTestDirectionMeasure:false],
      ]
    }
	List test3ResultClass() {
		[[name:"Pr\u00E4zi",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],
		 [name:"Tourist",contestTitle:""],
		]
    }
  
    def createtestquestion = {
            return []
    }
    
    def createtest = {
		int contest_id = 0
		switch (params.demoContest) {
			case '1': 
		        contest_id = create_test1("Demo Wettbewerb ${Contest.DEMOCONTESTYEAR}", "demo1", true)
				break
			case '2':
				contest_id = create_test2("Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (mit Klassen)", "demo2", true)
				break
			case '3':
				contest_id = create_test3("Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (kombinierter Wettbewerb)", "demo3", false)
				break
			case '11':
				contest_id = create_test11("Demo Wettbewerb Auswertung ohne Klassen", "demo11", false)
				break
			case '12':
				contest_id = create_test12("Demo Wettbewerb Auswertung mit Klassen", "demo12", false)
				break
			case '13': 
		        contest_id = create_test13("Demo Wettbewerb (100 Besatzungen)", "demo13", false)
				break
			case '14': 
		        contest_id = create_test14("Demo Wettbewerb (20 Besatzungen)", "demo14", false)
				break
		}
        
        redirect(controller:'contest',action:'activate',id:contest_id)
    }
            
    def runtest = {
        switch (session.lastContest.title) {
            case "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR}":
                run_test1 "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR}"
                break
            case "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (mit Klassen)":
                run_test2 "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (mit Klassen)"
                break
			case "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (kombinierter Wettbewerb)":
				run_test3 "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (kombinierter Wettbewerb)"
				break
        }
        redirect(controller:'contest',action:start)
    }

}
