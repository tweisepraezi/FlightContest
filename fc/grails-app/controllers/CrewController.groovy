class CrewController {
    
    def printService
	def fcService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
		fcService.printstart "List crews"
        if (session?.lastContest) {
			session.lastContest.refresh()
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
			session.resultclassReturnAction = actionName
			session.resultclassReturnController = controllerName
			session.resultclassReturnID = params.id
            def crew_list = Crew.findAllByContest(session.lastContest, [sort:'viewpos'])
			def active_crew_list = Crew.findAllByContestAndDisabled(session.lastContest, false, [sort:'viewpos'])
			fcService.printdone "last contest"
            return [crewList:crew_list,activeCrewList:active_crew_list,resultClasses:session.lastContest.resultClasses]
        }
		fcService.printdone ""
        redirect(controller:'contest',action:'start')
    }

    def edit = {
        def crew = fcService.getCrew(params) 
        if (crew.instance) {
			// assign return action
			if (session.crewReturnAction) {
				return [crewInstance:crew.instance,crewReturnAction:session.crewReturnAction,crewReturnController:session.crewReturnController,crewReturnID:session.crewReturnID]
			}
        	return [crewInstance:crew.instance]
        } else {
            flash.message = crew.message
            redirect(action:list)
        }
    }

	def editprintsettings = {
		if (session?.lastContest) {
			session.lastContest.refresh()
			return [contestInstance:session.lastContest]
        } else {
            redirect(action:list)
        }
	}
	
    def update = {
        def crew = fcService.updateCrew(params) 
        if (crew.saved) {
        	flash.message = crew.message
			// process return action
			if (params.crewReturnAction) {
				redirect(action:params.crewReturnAction,controller:params.crewReturnController,id:params.crewReturnID)
			} else {
				redirect(action:list)
			}
        } else if (crew.instance) {
        	render(view:'edit',model:[crewInstance:crew.instance])
        } else {
        	flash.message = crew.message
            redirect(action:edit,id:params.id)
        }
    }

    def saveprintsettings = {
		session.lastContest.refresh()
        def contest = fcService.updatecrewprintsettingsContest(session.lastContest,params,PrintSettings.CrewModified)
		if (contest.instance) {
			flash.message = contest.message
			render(view:'editprintsettings',model:[contestInstance:contest.instance,crewReturnAction:session.crewReturnAction,crewReturnController:session.crewReturnController,crewReturnID:session.crewReturnID])
		} else {
			flash.message = contest.message
			redirect(controller:"contest",action:"crews")
        }
    }

	def updateprintsettingsstandard = {
		session.lastContest.refresh()
        def contest = fcService.updatecrewprintsettingsContest(session.lastContest,params,PrintSettings.CrewStandard) 
        if (contest.instance) {
			flash.message = contest.message
			render(view:'editprintsettings',model:[contestInstance:contest.instance,crewReturnAction:session.crewReturnAction,crewReturnController:session.crewReturnController,crewReturnID:session.crewReturnID])
		} else {
			flash.message = contest.message
			redirect(controller:"contest",action:"crews")
		}
	}
	
	def updateprintsettingsnone = {
		session.lastContest.refresh()
        def contest = fcService.updatecrewprintsettingsContest(session.lastContest,params,PrintSettings.CrewNone) 
        if (contest.instance) {
			flash.message = contest.message
			render(view:'editprintsettings',model:[contestInstance:contest.instance,crewReturnAction:session.crewReturnAction,crewReturnController:session.crewReturnController,crewReturnID:session.crewReturnID])
		} else {
			flash.message = contest.message
			redirect(controller:"contest",action:"crews")
		}
	}
	
	def updateprintsettingsall = {
		session.lastContest.refresh()
        def contest = fcService.updatecrewprintsettingsContest(session.lastContest,params,PrintSettings.CrewAll) 
        if (contest.instance) {
			flash.message = contest.message
			render(view:'editprintsettings',model:[contestInstance:contest.instance,crewReturnAction:session.crewReturnAction,crewReturnController:session.crewReturnController,crewReturnID:session.crewReturnID])
		} else {
			flash.message = contest.message
			redirect(controller:"contest",action:"crews")
		}
	}
	
    def create = {
        def crew = fcService.createCrew(params)
        crew.instance.startNum = 1
        for(Crew crew_instance in Crew.findAllByContest(session.lastContest)) {
            if (crew_instance.startNum >= crew.instance.startNum) {
                crew.instance.startNum = crew_instance.startNum + 1
            }
        }
        return [crewInstance:crew.instance,resultClasses:session.lastContest.resultClasses]
    }

    def save = {
        def crew = fcService.saveCrew(params,session.lastContest) 
        if (crew.saved) {
        	flash.message = crew.message
        	redirect(action:list)
        } else {
            render(view:'create',model:[crewInstance:crew.instance])
        }
    }

	def delete = {
        def crew = fcService.deleteCrew(params)
        if (crew.deleted) {
        	flash.message = crew.message
        	redirect(action:list)
        } else if (crew.notdeleted) {
        	flash.message = crew.message
            redirect(action:edit,id:params.id)
        } else {
        	flash.message = crew.message
        	redirect(action:list)
        }
    }

	def cancel = {
		// process return action
		if (params.crewReturnAction) {
			redirect(action:params.crewReturnAction,controller:params.crewReturnController,id:params.crewReturnID)
		} else {
			redirect(action:list)
		}
	}

	def selectfilename = {
		[noStartnum13:false]
    }
	
	def importcrews = {
		def file = request.getFile('loadfile')
		if (file && !file.empty) {
			String file_name = file.getOriginalFilename()
			fcService.printstart "Upload '$file_name'"
			fcService.println file.getContentType() // "application/vnd.ms-excel", "application/octet-stream" 
			if (file_name.toLowerCase().endsWith('.xls')) {
                boolean no_startnum_13 = params?.noStartnum13 == 'on'
				String uuid = UUID.randomUUID().toString()
				String load_file_name = "CREWLIST-${uuid}-UPLOAD.xls"
				file.transferTo(new File(load_file_name))
		        def crews = fcService.importCrews(file_name, load_file_name, no_startnum_13, session.lastContest) 
		        if (crews.saved) {
		            flash.message = crews.message
					fcService.DeleteFile(load_file_name)
					fcService.printdone flash.message
		        } else if (crews.error) {
		        	flash.error = crews.error
		            flash.message = crews.message
					fcService.DeleteFile(load_file_name)
					fcService.printerror flash.message
		        }
			} else {
				flash.error = true
				flash.message = message(code:'fc.notimported.excel',args:[file_name])
				fcService.printerror flash.message
			}
		}
        redirect(action:list)
	}
	
    def listprintable = {
		if (params.contestid) {
			session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
		}
		if (session?.lastContest) {
			session.lastContest.refresh()
            
            Task task_instance = null
            if (session.lastContest.printCrewOrder > 0) {
                task_instance = Task.get(session.lastContest.printCrewOrder)
                if (task_instance) {
                    if (task_instance.contest != session.lastContest) {
                        task_instance = null
                    }
                }
            }
            
            if (task_instance) {
                def test_list = Test.findAllByTask(task_instance,[sort:'viewpos'])
                int crew_num = 0
                for (Test test_instance in test_list) {
                    if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                        crew_num++
                    }
                }
                return [testList:test_list,crewNum:crew_num,contestInstance:session.lastContest]
            } else {
                def crew_list = Crew.findAllByContestAndDisabled(session.lastContest,false,[sort:"viewpos"])
                int crew_num = crew_list.size()
                return [crewList:crew_list,crewNum:crew_num,contestInstance:session.lastContest]
            }
        }
        return [:]
    }

	def print = {
        Map crews = printService.printCrews(params,session.lastContest.printCrewA3,session.lastContest.printCrewLandscape,GetPrintParams()) 
        if (crews.error) {
            flash.message = crews.message
            flash.error = true
            redirect(action:list)
        } else if (crews.content) {
            printService.WritePDF(response,crews.content,session.lastContest.GetPrintPrefix(),"crews",true,session.lastContest.printCrewA3,session.lastContest.printCrewLandscape)
        } else {
            redirect(action:list)
        }
    }
    
	def selectall = {
        def crew = fcService.selectallCrew(session.lastContest) 
       	flash.selectedCrewIDs = crew.selectedcrewids
       	redirect(action:list)
    }

    def deselectall = {
       	redirect(action:list)
    }
    
	def calculatesequence = {
        def crew = fcService.calculatesequenceCrew(session.lastContest,params) 
        flash.error = crew.error
        flash.message = crew.message
       	redirect(action:list)
	}

	def moveup = {
        def crew = fcService.moveupCrew(session.lastContest,params,session) 
        if (crew.borderreached) {
        	redirect(action:list)
        } else if (crew.error) {
       		flash.message = crew.message
            flash.error = true
        	redirect(action:list)
        } else { 
	        if (crew.selectedcrewids) {
	    		flash.selectedCrewIDs = crew.selectedcrewids
	    	}
        	redirect(action:list)
        }
	}
	
	def movedown = {
        def crew = fcService.movedownCrew(session.lastContest,params,session) 
        if (crew.borderreached) {
        	redirect(action:list)
        } else if (crew.error) {
       		flash.message = crew.message
            flash.error = true
        	redirect(action:list)
        } else {
	    	if (crew.selectedcrewids) {
	    		flash.selectedCrewIDs = crew.selectedcrewids
	    	}
        	redirect(action:list)
        }
	}
	
    def deletecrews = {
        def ret = fcService.deleteCrews(session.lastContest,params,session)
        flash.message = ret.message
        redirect(action:list)
    }
    
    def sortstartnum = {
        [noStartnum13:false]
    }
    
    def sortstartnumrun = {
        boolean no_startnum_13 = params?.noStartnum13 == 'on'
        def ret = fcService.sortStartNumCrews(session.lastContest,params,session,no_startnum_13)
        flash.message = ret.message
        redirect(action:list)
    }
    
    def runcommand = {
        switch (params.crewcommand) {
            case CrewCommands.SELECTCOMMAND.toString():
                flash.message = message(code:'fc.crew.selectcommand.noselection')
                flash.error = true
                break
            case CrewCommands.DISABLECREWS.toString():
                def ret = fcService.disableCrews(session.lastContest,params,session)
                flash.message = ret.message
                break
            case CrewCommands.ENABLECREWS.toString():
                def ret = fcService.enableCrews(session.lastContest,params,session)
                flash.message = ret.message
                break
            case CrewCommands.DISABLETEAMCREWS.toString():
                def ret = fcService.disableTeamCrews(session.lastContest,params,session)
                flash.message = ret.message
                break
            case CrewCommands.ENABLETEAMCREWS.toString():
                def ret = fcService.enableTeamCrews(session.lastContest,params,session)
                flash.message = ret.message
                break
            case CrewCommands.DISABLECONTESTCREWS.toString():
                def ret = fcService.disableContestCrews(session.lastContest,params,session)
                flash.message = ret.message
                break
            case CrewCommands.ENABLECONTESTCREWS.toString():
                def ret = fcService.enableContestCrews(session.lastContest,params,session)
                flash.message = ret.message
                break
        }
        redirect(action:list)
    }
    
	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
}
