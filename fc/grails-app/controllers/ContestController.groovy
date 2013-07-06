import java.math.BigDecimal;
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

    int create_test21(String testName, String printPrefix, boolean testExists) 
    {
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,false,0,ContestRules.R1,true,testExists)
        
        // Routes
        fcService.printstart "Strecke 5"
		Map route5 = fcService.importRoute(contest,"Strecke 5",SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK)
        fcService.printdone ""
        
        fcService.printstart "Strecke 6"
		Map route6 = fcService.importRoute(contest,"Strecke 6",SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK)
        fcService.printdone ""
		
        fcService.printstart "Strecke 5m"
		Map route5m = fcService.putRoute(contest,"Strecke 5m", "Strecke 5m")
		create_route21(route5m, "Strecke 5m")
		fcService.printdone ""
		
        fcService.printstart "Strecke 6m"
		Map route6m = fcService.putRoute(contest,"Strecke 6m", "Strecke 6m")
		create_route21(route6m, "Strecke 6m")
		fcService.printdone ""
		
		// Crews and Aircrafts
		Map crew1 = fcService.putCrew(contest,1,"Besatzung 120","","","D-EAAA","","",120)
		Map crew2 = fcService.putCrew(contest,2,"Besatzung 60", "","","D-EAAB","","",60)

        // Flight Tests
        Map task5 = fcService.putTask(contest,"Strecke 5","09:00",2,10,10,15,false,true,false,false,false, false,true, true,true,true, false,false,false,false, false)
        Map flighttest5 = fcService.putFlightTest(task5,"",route5)
        Map flighttestwind5 = fcService.putFlightTestWind(flighttest5,0,0)
        fcService.putflighttestwindTask(task5,flighttestwind5)
		fcService.runcalculatetimetableTask(task5)
		
        Map task6 = fcService.putTask(contest,"Strecke 6","09:00",2,10,10,15,false,true,false,false,false, false,true, true,true,true, false,false,false,false, false)
        Map flighttest6 = fcService.putFlightTest(task6,"",route6)
        Map flighttestwind6 = fcService.putFlightTestWind(flighttest6,0,0)
        fcService.putflighttestwindTask(task6,flighttestwind6)
		fcService.runcalculatetimetableTask(task6)
		
        fcService.printdone ""
        
		return contest.instance.id
    }

	void create_route21(Map route, String strecke) {
		fcService.putCoordRoute(route,CoordType.TO,     0,"T/O", 'N',48,6.78612, 'E', 9, 45.6951, 1903, 1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SP,     0,"SP",  'N',48,7.46118, 'E',9, 41.34078,2524,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.TP,     1,"CP1", 'N',48,5.5845,  'E',9, 35.82642,2413,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.TP,     2,"CP2", 'N',48,13.21854,'E',9, 29.25354,2245,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.TP,     3,"CP3", 'N',48,14.97018,'E',9, 44.7258, 2131,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.TP,     4,"CP4", 'N',48,19.45464,'E',9, 43.81206,2226,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.TP,     5,"CP5", 'N',48,28.88394,'E',9, 54.42102,2554,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.TP,     6,"CP6", 'N',48,28.2984, 'E',10,4.75254, 2124,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.TP,     7,"CP7", 'N',48,21.54018,'E',10,9.18834, 2085,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.TP,     8,"CP8", 'N',48,14.57244,'E',10,12.99804,2209,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.TP,     9,"CP9", 'N',48,12.14622,'E',10,4.4004,  2193,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET, 1,"CP10",'N',48,13.19832,'E',9, 54.72876,500, 1.0, null, null)
		if (strecke == "Strecke 6m") {
			fcService.putCoordRoute(route,CoordType.TP,    10,"CP11",'N',48,12.25446,'E',9, 46.19184,2203,1.0, null, 132)
		} else {
			fcService.putCoordRoute(route,CoordType.TP,    10,"CP11",'N',48,12.25446,'E',9, 46.19184,2203,1.0, null, null)
		}
		fcService.putCoordRoute(route,CoordType.TP,    11,"CP12",'N',48,8.17152, 'E',9, 53.05554,2259,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.TP,    12,"CP13",'N',48,6.89436, 'E',9, 59.77026,2308,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET, 2,"CP14",'N',48,5.26938, 'E',9, 59.85162,2452,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET, 3,"CP15",'N',48,3.87882, 'E',9, 59.34978,2505,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET, 4,"CP16",'N',48,3.16764, 'E',9, 58.90962,2613,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET, 5,"CP17",'N',48,2.12592, 'E',9, 57.98466,2531,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET, 6,"CP18",'N',48,1.38588, 'E',9, 57.10206,2646,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET, 7,"CP19",'N',48,0.57762, 'E',9, 55.63968,2731,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET, 8,"CP20",'N',47,59.82114,'E',9, 53.72874,2669,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET, 9,"CP21",'N',47,59.47158,'E',9, 52.1712, 2724,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,10,"CP22",'N',47,59.28024,'E',9, 50.32314,2537,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,11,"CP23",'N',47,59.24022,'E',9, 49.1409, 2439,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,12,"CP24",'N',47,59.35428,'E',9, 47.49078,2423,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,13,"CP25",'N',47,59.91636,'E',9, 44.94666,2432,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,14,"CP26",'N',48,0.45312, 'E',9, 43.71606,2419,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,15,"CP27",'N',48,1.11954, 'E',9, 42.42648,2413,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,16,"CP28",'N',48,1.60434, 'E',9, 41.68098,2409,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,17,"CP29",'N',48,2.47332, 'E',9, 40.7391, 2446,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,18,"CP30",'N',48,3.26796, 'E',9, 40.04532,2432,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,19,"CP31",'N',48,4.42632, 'E',9, 39.41616,2478,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,20,"CP32",'N',48,5.61012, 'E',9, 39.06072,2406,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,21,"CP33",'N',48,6.55188, 'E',9, 39.03636,2537,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.SECRET,22,"CP34",'N',48,7.07484, 'E',9, 39.1155, 2455,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.TP,    13,"CP35",'N',48,8.02164, 'E',9, 39.15162,2491,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.FP,     0,"FP",  'N',48,9.02784, 'E',9, 43.24464,2347,1.0, null, null)
		fcService.putCoordRoute(route,CoordType.LDG,    0,"LDG", 'N',48,6.86658, 'E',9, 45.7728, 1903,1.0, null, null)
	}
    List test1Route() {
      [[title:"Strecke 1",mark:"Strecke 1"]]
    }
    List test1CoordRoute() {
      [[type:CoordType.TO,    mark:"T/O",
        latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        coordTrueTrack:0,coordMeasureDistance:0,
        measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SP,    mark:"SP",
        latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:0,coordMeasureDistance:0,
        measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SECRET,mark:"CP1",
        latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:88.8048175589,coordMeasureDistance:99.4440780950,
        measureTrueTrack:89.0,measureDistance:99.0,legMeasureDistance:99.0,legDistance:10.6911447084,secretLegRatio:0.6598765432,planProcedureTurn:false],
       [type:CoordType.TP,    mark:"CP2",
        latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:88.8465166117,coordMeasureDistance:149.9578513483,
        measureTrueTrack:89.0,measureDistance:150.0,legMeasureDistance:51.0,legDistance:5.5075593952,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SECRET,mark:"CP3",
        latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:219.3292360731,coordMeasureDistance:46.1613175609,
        measureTrueTrack:219.0,measureDistance:46.0,legMeasureDistance:46.0,legDistance:4.9676025918,secretLegRatio:0.2849770642,planProcedureTurn:true],
       [type:CoordType.TP,    mark:"CP4",
        latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:219.2221790621,coordMeasureDistance:161.4135491992,
        measureTrueTrack:219.0,measureDistance:161.5,legMeasureDistance:115.5,legDistance:12.4730021598,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SECRET,mark:"CP5",
        latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:160.8781319787,coordMeasureDistance:69.4482392126,
        measureTrueTrack:161.0,measureDistance:68.5,legMeasureDistance:68.5,legDistance:7.3974082073,secretLegRatio:0.5481481481,planProcedureTurn:false],
       [type:CoordType.TP,    mark:"CP6",
        latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:160.9135697323,coordMeasureDistance:126.1284575235,
        measureTrueTrack:161.0,measureDistance:125.0,legMeasureDistance:56.5,legDistance:6.1015118790,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SECRET,mark:"CP7",
        latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:86.3563657892,coordMeasureDistance:19.8166630762,
        measureTrueTrack:86.0,measureDistance:19.5,legMeasureDistance:19.5,legDistance:2.1058315335,secretLegRatio:0.1834782609,planProcedureTurn:false],
       [type:CoordType.TP,    mark:"CP8",
        latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:86.5776194402,coordMeasureDistance:106.7215296566,
        measureTrueTrack:86.0,measureDistance:106.45,legMeasureDistance:86.95,legDistance:9.3898488121,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SECRET,mark:"CP9",
        latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:237.2378249684,coordMeasureDistance:25.9240833138,
        measureTrueTrack:237.0,measureDistance:25.5,legMeasureDistance:25.5,legDistance:2.7537796976,secretLegRatio:0.2420774648,planProcedureTurn:true],
       [type:CoordType.TP,    mark:"CP10",
        latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:237.1829094762,coordMeasureDistance:104.8407146901,
        measureTrueTrack:237.0,measureDistance:105.2,legMeasureDistance:79.7,legDistance:8.6069114471,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.FP,    mark:"FP",
        latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:242.9619395538,coordMeasureDistance:62.0480855033,
        measureTrueTrack:244.0,measureDistance:62.4,legMeasureDistance:62.4,legDistance:6.7386609071,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.LDG,   mark:"LDG",
        latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        coordTrueTrack:256.4547835436,coordMeasureDistance:33.4955075819,
        measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false]
      ]
    }
    List test1RouteLegCoord() {
      [[coordTrueTrack:48.2896815944, coordDistance:4.0974514721, measureDistance:null,  legMeasureDistance:null,  legDistance:null,         measureTrueTrack:null, turnTrueTrack:null],
       [coordTrueTrack:88.8048175589, coordDistance:10.7391013062,measureDistance:99.0,  legMeasureDistance:99.0,  legDistance:10.6911447084,measureTrueTrack:89.0, turnTrueTrack:48],
       [coordTrueTrack:88.9286028152, coordDistance:5.4550359042, measureDistance:150.0, legMeasureDistance:51.0,  legDistance:5.5075593952, measureTrueTrack:89.0, turnTrueTrack:89],
       [coordTrueTrack:219.3292360731,coordDistance:4.9850234947, measureDistance:46.0,  legMeasureDistance:46.0,  legDistance:4.9676025918, measureTrueTrack:219.0,turnTrueTrack:89],
       [coordTrueTrack:219.1791259770,coordDistance:12.4462266624,measureDistance:161.5, legMeasureDistance:115.5, legDistance:12.4730021598,measureTrueTrack:219.0,turnTrueTrack:219],
       [coordTrueTrack:160.8781319787,coordDistance:7.4998098502, measureDistance:68.5,  legMeasureDistance:68.5,  legDistance:7.3974082073, measureTrueTrack:161.0,turnTrueTrack:219],
       [coordTrueTrack:160.9571657647,coordDistance:6.1209706224, measureDistance:125.0, legMeasureDistance:56.5,  legDistance:6.1015118790, measureTrueTrack:161.0,turnTrueTrack:161],
       [coordTrueTrack:86.3563657892, coordDistance:2.1400284100, measureDistance:19.5,  legMeasureDistance:19.5,  legDistance:2.1058315335, measureTrueTrack:86.0, turnTrueTrack:161],
       [coordTrueTrack:86.6280647396, coordDistance:9.3849768707, measureDistance:106.45,legMeasureDistance:86.95, legDistance:9.3898488121, measureTrueTrack:86.0, turnTrueTrack:86],
       [coordTrueTrack:237.2378249684,coordDistance:2.7995770317, measureDistance:25.5,  legMeasureDistance:25.5,  legDistance:2.7537796976, measureTrueTrack:237.0,turnTrueTrack:86],
       [coordTrueTrack:237.1648386550,coordDistance:8.5223089414, measureDistance:105.2, legMeasureDistance:79.7,  legDistance:8.6069114471, measureTrueTrack:237.0,turnTrueTrack:237],
       [coordTrueTrack:242.9619395538,coordDistance:6.7006571818, measureDistance:62.4,  legMeasureDistance:62.4,  legDistance:6.7386609071, measureTrueTrack:244.0,turnTrueTrack:237],
       [coordTrueTrack:256.4547835436,coordDistance:3.6172254408, measureDistance:null,  legMeasureDistance:null,  legDistance:null,         measureTrueTrack:null, turnTrueTrack:244],
      ]
    }
    List test1RouteLegTest() {
      [[coordTrueTrack:88.8048175589, coordDistance:16.2,         measureDistance:150.0, legMeasureDistance:150.0, legDistance:16.1987041037,measureTrueTrack:89.0, turnTrueTrack:null],
       [coordTrueTrack:219.3292360731,coordDistance:17.44,        measureDistance:161.5, legMeasureDistance:161.5, legDistance:17.4406047516,measureTrueTrack:219.0,turnTrueTrack:89],
       [coordTrueTrack:160.8781319787,coordDistance:13.62,        measureDistance:125.0, legMeasureDistance:125.0, legDistance:13.4989200864,measureTrueTrack:161.0,turnTrueTrack:219],
       [coordTrueTrack:86.3563657892, coordDistance:11.52,        measureDistance:106.45,legMeasureDistance:106.45,legDistance:11.4956803456,measureTrueTrack:86.0, turnTrueTrack:161],
       [coordTrueTrack:237.2378249684,coordDistance:11.32,        measureDistance:105.2, legMeasureDistance:105.2, legDistance:11.3606911447,measureTrueTrack:237.0,turnTrueTrack:86],
       [coordTrueTrack:242.9619395538,coordDistance:6.7,          measureDistance:62.4,  legMeasureDistance:62.4,  legDistance:6.7386609071, measureTrueTrack:244.0,turnTrueTrack:237],
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
      [[title:"20. Februar ${Contest.DEMOCONTESTYEAR}",firstTime:"09:00",takeoffIntervalNormal:3,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
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
        resultLatitude:"N 052\u00b0 02,17190'",resultLongitude:"E 013\u00b0 44,23030'",resultAltitude:237,
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
        resultLatitude:"N 052\u00b0 02,17410'",resultLongitude:"E 013\u00b0 44,23230'",resultAltitude:227,
        resultCpTime:Date.parse("HH:mm:ss","13:03:07"),resultCpTimeInput:"00:00:00",
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
        resultLatitude:"N 052\u00b0 02,17160'",resultLongitude:"E 013\u00b0 44,23470'",resultAltitude:205,
        resultCpTime:Date.parse("HH:mm:ss","12:06:05"),resultCpTimeInput:"00:00:00",
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
        resultLatitude:"N 052\u00b0 02,17200'",resultLongitude:"E 013\u00b0 44,23130'",resultAltitude:209,
        resultCpTime:Date.parse("HH:mm:ss","10:45:17"),resultCpTimeInput:"00:00:00",
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
        resultLatitude:"N 052\u00b0 02,17260'",resultLongitude:"E 013\u00b0 44,23400'",resultAltitude:214,
        resultCpTime:Date.parse("HH:mm:ss","10:51:06"),resultCpTimeInput:"00:00:00",
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
  
    void run_test21(String contestName)
    {
        if (session?.lastContest && session.lastContest.title == contestName) {
            fcService.printstart "runtest '$session.lastContest.title'"
			Route route5
			Route route6
			Route route5m
			Route route6m
			Route.findAllByContest(session.lastContest).eachWithIndex { Route route, int i ->
				switch (i) {
					case 0: 
						route5 = route
						break
					case 1:
						route6 = route
						break
					case 2:
						route5m = route
						break
					case 3:
						route6m = route
						break
				}
			}
			Task task5
			Task task6
			Task.findAllByContest(session.lastContest).eachWithIndex { Task task, int i ->
				switch (i) {
					case 0:
						task5 = task
						break
					case 1:
						task6 = task
						break
				}

			}
            Map ret = fcService.testData(
               [[name:"Route",count:4,table:Route.findAllByContest(session.lastContest,[sort:"id"]),data:test21Route()],
                [name:"CoordRoute 'Strecke 5'",count:39,table:CoordRoute.findAllByRoute(route5,[sort:"id"]),data:test21CoordRoute("Strecke 5")],
                [name:"CoordRoute 'Strecke 6'",count:39,table:CoordRoute.findAllByRoute(route6,[sort:"id"]),data:test21CoordRoute("Strecke 6")],
                [name:"CoordRoute 'Strecke 5m'",count:39,table:CoordRoute.findAllByRoute(route5m,[sort:"id"]),data:test21CoordRoute("Strecke 5m")],
                [name:"CoordRoute 'Strecke 6m'",count:39,table:CoordRoute.findAllByRoute(route6m,[sort:"id"]),data:test21CoordRoute("Strecke 6m")],
                [name:"RouteLegCoord 'Strecke 5'",count:38,table:RouteLegCoord.findAllByRoute(route5,[sort:"id"]),data:test21RouteLegCoord("Strecke 5")],
                [name:"RouteLegCoord 'Strecke 6'",count:38,table:RouteLegCoord.findAllByRoute(route6,[sort:"id"]),data:test21RouteLegCoord("Strecke 6")],
                [name:"RouteLegCoord 'Strecke 5m'",count:38,table:RouteLegCoord.findAllByRoute(route5m,[sort:"id"]),data:test21RouteLegCoord("Strecke 5m")],
                [name:"RouteLegCoord 'Strecke 6m'",count:38,table:RouteLegCoord.findAllByRoute(route6m,[sort:"id"]),data:test21RouteLegCoord("Strecke 6m")],
                [name:"RouteLegTest 'Strecke 5'",count:14,table:RouteLegTest.findAllByRoute(route5,[sort:"id"]),data:test21RouteLegTest("Strecke 5")],
                [name:"RouteLegTest 'Strecke 6'",count:14,table:RouteLegTest.findAllByRoute(route6,[sort:"id"]),data:test21RouteLegTest("Strecke 6")],
                [name:"RouteLegTest 'Strecke 5m'",count:14,table:RouteLegTest.findAllByRoute(route5m,[sort:"id"]),data:test21RouteLegTest("Strecke 5m")],
                [name:"RouteLegTest 'Strecke 6m'",count:14,table:RouteLegTest.findAllByRoute(route6m,[sort:"id"]),data:test21RouteLegTest("Strecke 6m")],
                [name:"Crew",count:2,table:Crew.findAllByContest(session.lastContest,[sort:"id"]),data:test21Crew()],
                [name:"Task",count:2,table:Task.findAllByContest(session.lastContest,[sort:"id"]),data:test21Task()],
                [name:"TestLegFlight 'Besatzung 120' 'Strecke 5'",count:14,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(task5,Crew.findByContestAndName(session.lastContest,"Besatzung 120")),[sort:"id"]),data:test21TestLegFlight120("Strecke 5")],
                [name:"TestLegFlight 'Besatzung 60' 'Strecke 5'", count:14,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(task5,Crew.findByContestAndName(session.lastContest,"Besatzung 60")), [sort:"id"]),data:test21TestLegFlight60("Strecke 5")],
                [name:"TestLegFlight 'Besatzung 120' 'Strecke 6'",count:14,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(task6,Crew.findByContestAndName(session.lastContest,"Besatzung 120")),[sort:"id"]),data:test21TestLegFlight120("Strecke 6")],
                [name:"TestLegFlight 'Besatzung 60' 'Strecke 6'", count:14,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(task6,Crew.findByContestAndName(session.lastContest,"Besatzung 60")), [sort:"id"]),data:test21TestLegFlight60("Strecke 6")],
                [name:"CoordResult 'Besatzung 120' 'Strecke 5'",  count:39,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 120")),[sort:"id"]),data:test21CoordResult120("Strecke 5")],    
                [name:"CoordResult 'Besatzung 60' 'Strecke 5'",   count:39,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 60")),[sort:"id"]),data:test21CoordResult60("Strecke 5")],    
                [name:"CoordResult 'Besatzung 120' 'Strecke 6'",  count:39,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 120")),[sort:"id"]),data:test21CoordResult120("Strecke 6")],    
                [name:"CoordResult 'Besatzung 60' 'Strecke 6'",   count:39,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Besatzung 60")),[sort:"id"]),data:test21CoordResult60("Strecke 6")],    
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

    List test21Route() {
      [[title:"Strecke 5",mark:"Strecke 5"],[title:"Strecke 6",mark:"Strecke 6"],[title:"Strecke 5m",mark:"Strecke 5m"],[title:"Strecke 6m",mark:"Strecke 6m"]]
    }
    List test21CoordRoute(String strecke) {
	  List l = [	
		  [mark:"T/O", type:CoordType.TO,    latDirection:'N',latGrad:48,latMinute:6.78612, lonDirection:'E',lonGrad:9, lonMinute:45.6951, altitude:1903,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"SP",  type:CoordType.SP,    latDirection:'N',latGrad:48,latMinute:7.46118, lonDirection:'E',lonGrad:9, lonMinute:41.34078,altitude:2524,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP1", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:5.5845,  lonDirection:'E',lonGrad:9, lonMinute:35.82642,altitude:2413,gatewidth2:1.0,coordTrueTrack:242.9927805108,coordMeasureDistance:38.2689956682,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP2", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:13.21854,lonDirection:'E',lonGrad:9, lonMinute:29.25354,altitude:2245,gatewidth2:1.0,coordTrueTrack:330.1283636740,coordMeasureDistance:81.5219365944,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP3", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:14.97018,lonDirection:'E',lonGrad:9, lonMinute:44.7258, altitude:2131,gatewidth2:1.0,coordTrueTrack:80.3537644360,coordMeasureDistance:96.7997427055,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
		  [mark:"CP4", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:19.45464,lonDirection:'E',lonGrad:9, lonMinute:43.81206,altitude:2226,gatewidth2:1.0,coordTrueTrack:352.2789081903,coordMeasureDistance:41.9060276350,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP5", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:28.88394,lonDirection:'E',lonGrad:9, lonMinute:54.42102,altitude:2554,gatewidth2:1.0,coordTrueTrack:36.7577376702,coordMeasureDistance:108.9843487966,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP6", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:28.2984, lonDirection:'E',lonGrad:10,lonMinute:4.75254, altitude:2124,gatewidth2:1.0,coordTrueTrack:94.8864626525,coordMeasureDistance:63.6534878383,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP7", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:21.54018,lonDirection:'E',lonGrad:10,lonMinute:9.18834, altitude:2085,gatewidth2:1.0,coordTrueTrack:156.4600910143,coordMeasureDistance:68.2617132154,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP8", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:14.57244,lonDirection:'E',lonGrad:10,lonMinute:12.99804,altitude:2209,gatewidth2:1.0,coordTrueTrack:160.0128420855,coordMeasureDistance:68.6565047302,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP9", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:12.14622,lonDirection:'E',lonGrad:10,lonMinute:4.4004,  altitude:2193,gatewidth2:1.0,coordTrueTrack:247.0441046808,coordMeasureDistance:57.6038587521,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP10",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:13.19832,lonDirection:'E',lonGrad:9, lonMinute:54.72876,altitude:500, gatewidth2:1.0,coordTrueTrack:279.2712735373,coordMeasureDistance:60.4711394976,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.5308943089,planProcedureTurn:false],
		  [mark:"CP11",type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:12.25446,lonDirection:'E',lonGrad:9, lonMinute:46.19184,altitude:2203,gatewidth2:1.0,coordTrueTrack:270.5110110558,coordMeasureDistance:112.3820277830,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP12",type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:8.17152, lonDirection:'E',lonGrad:9, lonMinute:53.05554,altitude:2259,gatewidth2:1.0,coordTrueTrack:131.7314005896,coordMeasureDistance:56.7995551950,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
		  [mark:"CP13",type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:6.89436, lonDirection:'E',lonGrad:9, lonMinute:59.77026,altitude:2308,gatewidth2:1.0,coordTrueTrack:105.9048009794,coordMeasureDistance:43.1561451728,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP14",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:5.26938, lonDirection:'E',lonGrad:9, lonMinute:59.85162,altitude:2452,gatewidth2:1.0,coordTrueTrack:178.0849512072,coordMeasureDistance:15.0557238248,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.0668032787,planProcedureTurn:false],
		  [mark:"CP15",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:3.87882, lonDirection:'E',lonGrad:9, lonMinute:59.34978,altitude:2505,gatewidth2:1.0,coordTrueTrack:185.3211522401,coordMeasureDistance:28.0447584647,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.1254098361,planProcedureTurn:false],
		  [mark:"CP16",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:3.16764, lonDirection:'E',lonGrad:9, lonMinute:58.90962,altitude:2613,gatewidth2:1.0,coordTrueTrack:188.7702270674,coordMeasureDistance:34.9176937824,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.1569672131,planProcedureTurn:false],
		  [mark:"CP17",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:2.12592, lonDirection:'E',lonGrad:9, lonMinute:57.98466,altitude:2531,gatewidth2:1.0,coordTrueTrack:194.0469601470,coordMeasureDistance:45.5168390213,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.2065573770,planProcedureTurn:false],
		  [mark:"CP18",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:1.38588, lonDirection:'E',lonGrad:9, lonMinute:57.10206,altitude:2646,gatewidth2:1.0,coordTrueTrack:197.9357318536,coordMeasureDistance:53.6140305352,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.2450819672,planProcedureTurn:false],
		  [mark:"CP19",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:0.57762, lonDirection:'E',lonGrad:9, lonMinute:55.63968,altitude:2731,gatewidth2:1.0,coordTrueTrack:203.6064288644,coordMeasureDistance:63.8348531790,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.2971311475,planProcedureTurn:false],
		  [mark:"CP20",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.82114,lonDirection:'E',lonGrad:9, lonMinute:53.72874,altitude:2669,gatewidth2:1.0,coordTrueTrack:209.7225191976,coordMeasureDistance:75.4205989397,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.3581967213,planProcedureTurn:false],
		  [mark:"CP21",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.47158,lonDirection:'E',lonGrad:9, lonMinute:52.1712, altitude:2724,gatewidth2:1.0,coordTrueTrack:214.3844904348,coordMeasureDistance:83.2881876036,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.4032786885,planProcedureTurn:false],
		  [mark:"CP22",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.28024,lonDirection:'E',lonGrad:9, lonMinute:50.32314,altitude:2537,gatewidth2:1.0,coordTrueTrack:219.6718599771,coordMeasureDistance:91.6013297583,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.4545081967,planProcedureTurn:false],
		  [mark:"CP23",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.24022,lonDirection:'E',lonGrad:9, lonMinute:49.1409, altitude:2439,gatewidth2:1.0,coordTrueTrack:222.8707169938,coordMeasureDistance:96.7092848996,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.4868852459,planProcedureTurn:false],
		  [mark:"CP24",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.35428,lonDirection:'E',lonGrad:9, lonMinute:47.49078,altitude:2423,gatewidth2:1.0,coordTrueTrack:227.4295887584,coordMeasureDistance:103.2100223775,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.5323770492,planProcedureTurn:false],
		  [mark:"CP25",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.91636,lonDirection:'E',lonGrad:9, lonMinute:44.94666,altitude:2432,gatewidth2:1.0,coordTrueTrack:234.8437733233,coordMeasureDistance:112.2184753721,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.6057377049,planProcedureTurn:false],
		  [mark:"CP26",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:0.45312, lonDirection:'E',lonGrad:9, lonMinute:43.71606,altitude:2419,gatewidth2:1.0,coordTrueTrack:239.0225833671,coordMeasureDistance:115.8847127820,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.6463114754,planProcedureTurn:false],
		  [mark:"CP27",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:1.11954, lonDirection:'E',lonGrad:9, lonMinute:42.42648,altitude:2413,gatewidth2:1.0,coordTrueTrack:243.5152534003,coordMeasureDistance:119.9096054241,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.6909836066,planProcedureTurn:false],
		  [mark:"CP28",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:1.60434, lonDirection:'E',lonGrad:9, lonMinute:41.68098,altitude:2409,gatewidth2:1.0,coordTrueTrack:246.3636439838,coordMeasureDistance:122.1797687138,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.7192622951,planProcedureTurn:false],
		  [mark:"CP29",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:2.47332, lonDirection:'E',lonGrad:9, lonMinute:40.7391, altitude:2446,gatewidth2:1.0,coordTrueTrack:250.8273686530,coordMeasureDistance:124.6555952652,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.7635245902,planProcedureTurn:false],
		  [mark:"CP30",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:3.26796, lonDirection:'E',lonGrad:9, lonMinute:40.04532,altitude:2432,gatewidth2:1.0,coordTrueTrack:254.6126009157,coordMeasureDistance:126.5544119144,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.8012295082,planProcedureTurn:false],
		  [mark:"CP31",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:4.42632, lonDirection:'E',lonGrad:9, lonMinute:39.41616,altitude:2478,gatewidth2:1.0,coordTrueTrack:259.7102918087,coordMeasureDistance:127.9439544592,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.8516393443,planProcedureTurn:false],
		  [mark:"CP32",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:5.61012, lonDirection:'E',lonGrad:9, lonMinute:39.06072,altitude:2406,gatewidth2:1.0,coordTrueTrack:264.6945459895,coordMeasureDistance:128.6109852327,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.9012295082,planProcedureTurn:false],
		  [mark:"CP33",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:6.55188, lonDirection:'E',lonGrad:9, lonMinute:39.03636,altitude:2537,gatewidth2:1.0,coordTrueTrack:268.5828281429,coordMeasureDistance:128.2302822153,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.9397540984,planProcedureTurn:false],
		  [mark:"CP34",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:7.07484, lonDirection:'E',lonGrad:9, lonMinute:39.1155, altitude:2455,gatewidth2:1.0,coordTrueTrack:270.7498559623,coordMeasureDistance:127.7018680468,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.9610655738,planProcedureTurn:false],
		  [mark:"CP35",type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:8.02164, lonDirection:'E',lonGrad:9, lonMinute:39.15162,altitude:2491,gatewidth2:1.0,coordTrueTrack:274.6823500101,coordMeasureDistance:127.8748284371,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"FP",  type:CoordType.FP,    latDirection:'N',latGrad:48,latMinute:9.02784, lonDirection:'E',lonGrad:9, lonMinute:43.24464,altitude:2347,gatewidth2:1.0,coordTrueTrack:69.7758183261,coordMeasureDistance:26.9527530644,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"LDG", type:CoordType.LDG,   latDirection:'N',latGrad:48,latMinute:6.86658, lonDirection:'E',lonGrad:9, lonMinute:45.7728, altitude:1903,gatewidth2:1.0,coordTrueTrack:142.0204175420,coordMeasureDistance:25.3901337778,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
      ]
	  if (strecke == "Strecke 6" || strecke == "Strecke 6m") {
		  // CP11
		  l[12].measureTrueTrack = 132
		  l[12].planProcedureTurn = true
		  // CP12
		  l[13].planProcedureTurn = false
	  }
	  return l
    }
    List test21RouteLegCoord(String strecke) {
	  List l = [	
		  [coordTrueTrack:283.0738921712,coordDistance:2.9842515197, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:null],
		  [coordTrueTrack:242.9927805108,coordDistance:4.1327209145, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:283],
		  [coordTrueTrack:330.1283636740,coordDistance:8.8036648590, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:243],
		  [coordTrueTrack:80.3537644360, coordDistance:10.4535359293,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:330],
		  [coordTrueTrack:352.2789081903,coordDistance:4.5254889455, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:80],
		  [coordTrueTrack:36.7577376702, coordDistance:11.7693681206,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:352],
		  [coordTrueTrack:94.8864626525, coordDistance:6.8740267644, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:37],
		  [coordTrueTrack:156.4600910143,coordDistance:7.3716752932, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:95],
		  [coordTrueTrack:160.0128420855,coordDistance:7.4143093661, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:156],
		  [coordTrueTrack:247.0441046808,coordDistance:6.2207190877, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:160],
		  [coordTrueTrack:279.2712735373,coordDistance:6.5303606369, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:247],
		  [coordTrueTrack:260.5795422792,coordDistance:5.7665585959, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:279],
		  [coordTrueTrack:131.7314005896,coordDistance:6.1338612522, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:261],
		  [coordTrueTrack:105.9048009794,coordDistance:4.6604908394, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:132],
		  [coordTrueTrack:178.0849512072,coordDistance:1.6258881020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:106],
		  [coordTrueTrack:193.5567120396,coordDistance:1.4304136455, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:178],
		  [coordTrueTrack:202.4730890648,coordDistance:0.7696260394, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:194],
		  [coordTrueTrack:210.6943702086,coordDistance:1.2114397640, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:202],
		  [coordTrueTrack:218.5750901922,coordDistance:0.9465949322, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:211],
		  [coordTrueTrack:230.4343741528,coordDistance:1.2689304742, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:219],
		  [coordTrueTrack:239.3891679625,coordDistance:1.4856124479, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:230],
		  [coordTrueTrack:251.4601796188,coordDistance:1.0993709100, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:239],
		  [coordTrueTrack:261.2060343691,coordDistance:1.2515554864, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:251],
		  [coordTrueTrack:267.1045938553,coordDistance:0.7922734187, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:261],
		  [coordTrueTrack:275.8964955151,coordDistance:1.1102707798, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:267],
		  [coordTrueTrack:288.2701365508,coordDistance:1.7929326260, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:276],
		  [coordTrueTrack:303.1001655100,coordDistance:0.9828890204, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:288],
		  [coordTrueTrack:307.6861309848,coordDistance:1.0901051900, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:303],
		  [coordTrueTrack:314.1950280940,coordDistance:0.6954497066, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:308],
		  [coordTrueTrack:324.0658841750,coordDistance:1.0732224391, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:314],
		  [coordTrueTrack:329.7296452017,coordDistance:0.9200880415, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:324],
		  [coordTrueTrack:340.0498880935,coordDistance:1.2323108963, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:330],
		  [coordTrueTrack:348.6579620642,coordDistance:1.2073793596, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:340],
		  [coordTrueTrack:359.0103704238,coordDistance:0.9419004961, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:349],
		  [coordTrueTrack:5.7694167765,  coordDistance:0.5256225364, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:359],
		  [coordTrueTrack:1.4587060220,  coordDistance:0.9471069275, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:6],
		  [coordTrueTrack:69.7758183261, coordDistance:2.9106644778, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:1],
		  [coordTrueTrack:142.0204175420,coordDistance:2.7419150948, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:70],
	  ]
	  if (strecke == "Strecke 6" || strecke == "Strecke 6m") {
		  l[11].measureTrueTrack = 132
		  l[12].turnTrueTrack = 132
	  }
      return l
    }
    List test21RouteLegTest(String strecke) {
	  List l = [
		  [coordTrueTrack:242.9927805108,coordDistance:4.13, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:null],
		  [coordTrueTrack:330.1283636740,coordDistance:8.8,  measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:243],
		  [coordTrueTrack:80.3537644360, coordDistance:10.45,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:330],
		  [coordTrueTrack:352.2789081903,coordDistance:4.53, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:80],
		  [coordTrueTrack:36.7577376702, coordDistance:11.77,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:352],
		  [coordTrueTrack:94.8864626525, coordDistance:6.87, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:37],
		  [coordTrueTrack:156.4600910143,coordDistance:7.37, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:95],
		  [coordTrueTrack:160.0128420855,coordDistance:7.41, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:156],
		  [coordTrueTrack:247.0441046808,coordDistance:6.22, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:160],
		  [coordTrueTrack:279.2712735373,coordDistance:12.3, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:247],
		  [coordTrueTrack:131.7314005896,coordDistance:6.13, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:261],
		  [coordTrueTrack:105.9048009794,coordDistance:4.66, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:132],
		  [coordTrueTrack:178.0849512072,coordDistance:24.41,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:106],
		  [coordTrueTrack:69.7758183261, coordDistance:2.91, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:1],
      ]
	  if (strecke == "Strecke 6" || strecke == "Strecke 6m") {
		  l[10].turnTrueTrack = 132
	  }
	  return l
    }
    List test21Crew() {
       [[startNum:1,name:"Besatzung 120",mark:"",team:null,tas:120,contestPenalties:0,contestPosition:0,noContestPosition:false,aircraft:[registration:"D-EAAA",type:"",colour:""]],
        [startNum:2,name:"Besatzung 60", mark:"",team:null,tas:60, contestPenalties:0,contestPosition:0,noContestPosition:false,aircraft:[registration:"D-EAAB",type:"",colour:""]],
       ]
    }
	List test21Task() {
		[[title:"Strecke 5",firstTime:"09:00",takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
    	  preparationDuration:15,risingDuration:10,maxLandingDuration:10,parkingDuration:15,minNextFlightDuration:30,
		  procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
	     [title:"Strecke 6",firstTime:"09:00",takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
		  preparationDuration:15,risingDuration:10,maxLandingDuration:10,parkingDuration:15,minNextFlightDuration:30,
		  procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true]
		]
	}
	List test21TestLegFlight120(String strecke) {
		List l = [
			[planTrueTrack:243.0,planTestDistance:4.13, planTrueHeading:243.0,planGroundSpeed:120.0,planLegTime:0.0344166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:330.0,planTestDistance:8.8,  planTrueHeading:330.0,planGroundSpeed:120.0,planLegTime:0.0733333333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:80.0, planTestDistance:10.45,planTrueHeading:80.0, planGroundSpeed:120.0,planLegTime:0.0870833333,planProcedureTurn:true, planProcedureTurnDuration:1],
			[planTrueTrack:352.0,planTestDistance:4.53, planTrueHeading:352.0,planGroundSpeed:120.0,planLegTime:0.03775,     planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:37.0, planTestDistance:11.77,planTrueHeading:37.0, planGroundSpeed:120.0,planLegTime:0.0980833333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:95.0, planTestDistance:6.87, planTrueHeading:95.0, planGroundSpeed:120.0,planLegTime:0.05725,     planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:156.0,planTestDistance:7.37, planTrueHeading:156.0,planGroundSpeed:120.0,planLegTime:0.0614166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:160.0,planTestDistance:7.41, planTrueHeading:160.0,planGroundSpeed:120.0,planLegTime:0.06175,     planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:247.0,planTestDistance:6.22, planTrueHeading:247.0,planGroundSpeed:120.0,planLegTime:0.0518333333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:279.0,planTestDistance:12.3, planTrueHeading:279.0,planGroundSpeed:120.0,planLegTime:0.1025000000,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:132.0,planTestDistance:6.13, planTrueHeading:132.0,planGroundSpeed:120.0,planLegTime:0.0510833333,planProcedureTurn:true, planProcedureTurnDuration:1],
			[planTrueTrack:106.0,planTestDistance:4.66, planTrueHeading:106.0,planGroundSpeed:120.0,planLegTime:0.0388333333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:178.0,planTestDistance:24.41,planTrueHeading:178.0,planGroundSpeed:120.0,planLegTime:0.2034166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:70.0, planTestDistance:2.91, planTrueHeading:70.0, planGroundSpeed:120.0,planLegTime:0.0242500000,planProcedureTurn:false,planProcedureTurnDuration:0],
		]
		if (strecke == "Strecke 6") {
			l[10].planProcedureTurn = false
			l[10].planProcedureTurnDuration = 0
		}
		return l
	}
	List test21TestLegFlight60(String strecke) {
		List l = test21TestLegFlight120(strecke)
		l.each {
			it.planGroundSpeed /= 2
			it.planLegTime *= 2
		}
		l[0].planLegTime -= 0.0000000001
		l[1].planLegTime += 0.0000000001
		l[2].planLegTime += 0.0000000001
		l[4].planLegTime += 0.0000000001
		l[6].planLegTime -= 0.0000000001
		l[8].planLegTime += 0.0000000001
		l[10].planLegTime += 0.0000000001
		l[11].planLegTime += 0.0000000001
		l[12].planLegTime -= 0.0000000001
		return l
	}
	List test21CoordResult120(String strecke) {
	    List l = test21CoordRoute(strecke)
		l.each {
			it.coordTrueTrack = 0
			it.coordMeasureDistance = 0
			it.secretLegRatio = 0
		}
		if (strecke == "Strecke 6") {
			l[12].measureTrueTrack = null
			l[12].planProcedureTurn = false
			l[13].planProcedureTurn = true
		}
		return l
	}
	List test21CoordResult60(String strecke) {
		List l = test21CoordResult120(strecke)
		return l
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
			case '21': 
		        contest_id = create_test21("Demo Wettbewerb (Strecken)", "demo21", true)
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
			case "Demo Wettbewerb (Strecken)":
				run_test21 "Demo Wettbewerb (Strecken)"
				break
        }
        redirect(controller:'contest',action:start)
    }

}
