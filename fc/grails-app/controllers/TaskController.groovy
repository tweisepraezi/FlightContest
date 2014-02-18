
import org.xhtmlrenderer.pdf.ITextRenderer


class TaskController {

	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def edit = {
        def task = fcService.getTask(params) 
        if (task.instance) {
			// assign return action
			if (session.taskReturnAction) {
				return [taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID]
			}
			return [taskInstance:task.instance]
        } else {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        }
    }

	def editprintsettings = {
		def task = fcService.getTask(params)
		if (task.instance) {
			// assign return action
			if (session.taskReturnAction) {
				return [taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID]
			}
			return [taskInstance:task.instance]
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
    def update = {
        def task = fcService.updateTask(params) 
        if (task.saved) {
        	flash.message = task.message
			// process return action
			if (params.taskReturnAction) {
				redirect(action:params.taskReturnAction,controller:params.taskReturnController,id:params.taskReturnID)
			} else {
        		redirect(controller:"contest",action:"tasks")
			}
        } else if (task.instance) {
			render(view:'edit',model:[taskInstance:task.instance])
        } else {
			flash.message = task.message
			redirect(action:edit,id:params.id)
        }
    }

    def updateprintsettings = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.Modified,session.printLanguage) 
        if (task.saved) {
        	flash.message = task.message
			// process return action
			if (params.taskReturnAction) {
				redirect(action:params.taskReturnAction,controller:params.taskReturnController,id:params.taskReturnID)
			} else {
        		redirect(controller:"contest",action:"tasks")
			}
        } else if (task.instance) {
			render(view:'edit',model:[taskInstance:task.instance])
        } else {
			flash.message = task.message
			redirect(action:edit,id:params.id)
        }
    }

	def updateprintsettingsstandard = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.Standard,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updateprintsettingsnone = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.None,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updateprintsettingsall = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.All,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updateprintsettingstower = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.Tower,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updateprintsettingsplanning = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.Planning,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updateprintsettingstakeoff = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.Takeoff,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updateprintsettingslanding = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.Landing,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updateprintsettingsparking = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.Parking,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updateprintsettingstimetablestandard = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableStandard,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updateprintsettingstimetablenone = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableNone,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updateprintsettingstimetableall = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableAll,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def savechangeprintsettingstimetable = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableSaveChange,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def removechangeprintsettingstimetable = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableRemoveChange,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def addchangeprintsettingstimetable = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableAddChange,session.printLanguage) 
        if (task.saved) {
			render(view:'editprintsettings',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
    def create = {
        def task = fcService.createTask(params) 
        return [taskInstance:task.instance,contestInstance:session.lastContest]
    }
	
	def copy = {
		def new_task = fcService.copyTask(params,session.lastContest)
        return [taskInstance:new_task.instance,contestInstance:session.lastContest,taskClassSettings:new_task.taskclass_settings]
	}

    def save = {
        def task = fcService.saveTask(params,session.lastContest) 
        if (task.saved) {
        	flash.message = task.message
     		redirect(controller:"contest",action:"tasks")
        } else {
            render(view:'create',model:[taskInstance:task.instance])
        }
    }
    
    def delete = {
        def task = fcService.deleteTask(params)
        if (task.deleted) {
        	flash.message = task.message
        	redirect(controller:"contest",action:"tasks")
        } else if (task.notdeleted) {
        	flash.message = task.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = task.message
        	redirect(controller:"contest",action:"tasks")
        }
    }
	
	def disabledcheckpoints = {
        def task = fcService.getTask(params) 
        if (task.instance) {
			// assign return action
			if (session.taskReturnAction) {
				return [taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID]
			}
			return [taskInstance:task.instance]
        } else {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        }
	}
	
    def savedisabledcheckpoints = {
        def task = fcService.savedisabledcheckpointsTask(params) 
        if (task.saved) {
        	flash.message = task.message
			// process return action
			if (params.taskReturnAction) {
				redirect(action:params.taskReturnAction,controller:params.taskReturnController,id:params.taskReturnID)
			} else {
        		redirect(controller:"contest",action:"tasks")
			}
        } else if (task.instance) {
			render(view:'edit',model:[taskInstance:task.instance])
        } else {
			flash.message = task.message
			redirect(action:edit,id:params.id)
        }
    }

	def resetdisabledcheckpoints = {
        def task = fcService.resetdisabledcheckpointsTask(params) 
        if (task.saved) {
        	flash.message = task.message
        }
        redirect(action:disabledcheckpoints,id:params.id)
	}
	
	def cancel = {
		// process return action
		if (params.taskReturnAction) {
			redirect(action:params.taskReturnAction,controller:params.taskReturnController,id:params.taskReturnID)
		} else if (params.crewresultsprintquestionReturnAction) {
			redirect(action:params.crewresultsprintquestionReturnAction,controller:params.crewresultsprintquestionReturnController,id:params.crewresultsprintquestionReturnID)
		} else if (params.listresultsprintquestionReturnAction) {
			redirect(action:params.listresultsprintquestionReturnAction,controller:params.listresultsprintquestionReturnController,id:params.listresultsprintquestionReturnID)
		} else {
			redirect(controller:"contest",action:"tasks")
		}
	}

	def createplanningtest = {
        def task = fcService.getTask(params)
        redirect(controller:'planningTest',action:'create',params:['task.id':task.instance.id,'taskid':task.instance.id,'fromtask':true])
	}
	
	def createflighttest = {
        def task = fcService.getTask(params) 
        redirect(controller:'flightTest',action:'create',params:['task.id':task.instance.id,'taskid':task.instance.id,'fromtask':true])
	}
	
    def startplanning = {
		fcService.printstart "Start planning"
		if (session?.lastContest) {
			def task = fcService.startplanningTask(params, session.lastContest, session.lastTaskPlanning)
			if (task.taskid) {
	   			params.id = task.taskid
				fcService.println "last planning task $task.taskid"
	   			redirect(action:listplanning,params:params)
	   		}
			fcService.printdone "last contest"
		} else {
			fcService.printdone ""
			redirect(controller:'contest',action:'start')
		}
    }

    def listplanning = {
		fcService.printstart "List planning"
		if (session?.lastContest) {
			def task = fcService.getTask(params) 
	        if (!task.instance) {
	            flash.message = task.message
				fcService.printdone task.message
	            redirect(controller:"contest",action:"tasks")
	        } else if (task.instance.hidePlanning) {
				session.lastTaskPlanning = null
				fcService.printdone "Hide task."
				redirect(controller:"contest",action:"tasks")
	        } else {
				SetLimit()
	            session.lastTaskPlanning = task.instance.id
				// save return action
				session.taskReturnAction = actionName
				session.taskReturnController = controllerName
				session.taskReturnID = params.id
				session.crewReturnAction = actionName
				session.crewReturnController = controllerName
				session.crewReturnID = params.id
				session.teamReturnAction = actionName
				session.teamReturnController = controllerName
				session.teamReturnID = params.id
				session.aircraftReturnAction = actionName
				session.aircraftReturnController = controllerName
				session.aircraftReturnID = params.id
				session.resultclassReturnAction = actionName
				session.resultclassReturnController = controllerName
				session.resultclassReturnID = params.id
				session.planningtestReturnAction = actionName
				session.planningtestReturnController = controllerName
				session.planningtestReturnID = params.id
				session.planningtesttaskReturnAction = actionName
				session.planningtesttaskReturnController = controllerName
				session.planningtesttaskReturnID = params.id
				session.flighttestReturnAction = actionName
				session.flighttestReturnController = controllerName
				session.flighttestReturnID = params.id
				session.flighttestwindReturnAction = actionName
				session.flighttestwindReturnController = controllerName
				session.flighttestwindReturnID = params.id
				session.showReturnAction = actionName
				session.showReturnController = controllerName
				session.showReturnID = params.id
				fcService.printdone ""
	        	return [taskInstance:task.instance]
	        }
		} else {
			fcService.printdone ""
			redirect(controller:'contest',action:'start')
		}
    }

    def startresults = {
		fcService.printstart "Start results"
		if (session?.lastContest) {
			if (session?.lastResultClassResults) {
		        def resultclass = fcService.startresultsResultClass(params, session.lastContest, session.lastResultClassResults)
		        if (resultclass.resultclassid) {
		            params.id = resultclass.resultclassid
					fcService.println "last results resultclass $resultclass.resultclassid"
		            redirect(controller:'resultClass',action:'listresults',params:params)
		        }
			} else if (session?.lastContestResults) {
				fcService.println "last results contest"
	            redirect(controller:'contest',action:'listresults',params:params)
			} else if (session?.lastTeamResults) {
				fcService.println "last results contest"
	            redirect(controller:'contest',action:'listteamresults',params:params)
			} else {
		        def task = fcService.startresultsTask(params, session.lastContest, session.lastTaskResults)
		        if (task.taskid) {
		            params.id = task.taskid
					fcService.println "last results task $task.taskid"
		            redirect(action:listresults,params:params)
		        }
			}
			fcService.printdone "last contest"
		} else {
			fcService.printdone ""
			redirect(controller:'contest',action:'start')
		}
    }

    def listresults = {
		fcService.printstart "List results"
		if (session?.lastContest) {
	        def task = fcService.getTask(params) 
	        if (!task.instance) {
	            flash.message = task.message
				fcService.printdone task.message
	            redirect(controller:"contest",action:"tasks")
	        } else if (task.instance.hideResults) {
				session.lastResultClassResults = null
				session.lastContestResults = true
				session.lastTeamResults = null
				session.lastTaskResults = null
				fcService.printdone "Hide task."
				redirect(controller:"contest",action:"listresults")
	        } else {
				SetLimit()
				session.lastResultClassResults = null
				session.lastContestResults = null
				session.lastTeamResults = null
	            session.lastTaskResults = task.instance.id
				// save return action
				session.taskReturnAction = actionName 
				session.taskReturnController = controllerName
				session.taskReturnID = params.id
				session.crewReturnAction = actionName
				session.crewReturnController = controllerName
				session.crewReturnID = params.id
				session.teamReturnAction = actionName
				session.teamReturnController = controllerName
				session.teamReturnID = params.id
				session.aircraftReturnAction = actionName
				session.aircraftReturnController = controllerName
				session.aircraftReturnID = params.id
				session.resultclassReturnAction = actionName
				session.resultclassReturnController = controllerName
				session.resultclassReturnID = params.id
				session.positionsReturnAction = actionName 
				session.positionsReturnController = controllerName
				session.positionsReturnID = params.id
				session.showReturnAction = actionName
				session.showReturnController = controllerName
				session.showReturnID = params.id
				fcService.printdone ""
	            return [taskInstance:task.instance]
	        }
		} else {
			fcService.printdone ""
			redirect(controller:'contest',action:'start')
		}
    }

	def selectall = {
        def task = fcService.selectallTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
            return
        } else {
        	flash.selectedTestIDs = task.selectedtestids
        	redirect(action:listplanning,id:task.instance.id)
        }
    }

	def selectend = {
        def task = fcService.selectendTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
            return
        } else {
        	flash.selectedTestIDs = task.selectedtestids
        	redirect(action:listplanning,id:task.instance.id)
        }
    }

    def deselectall = {
        def task = fcService.getTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
        	redirect(action:listplanning,id:task.instance.id)
        }
    }
    
    def assignplanningtesttask = {
        def task = fcService.assignplanningtesttaskTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
        	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.testinstanceids?.size > 1) {
        	redirect(action:selectplanningtesttask,id:task.instance.id,params:[testInstanceIDs:task.testinstanceids])
        } else {
        	redirect(action:listplanning,id:task.instance.id)
        }
    }
    
    def selectplanningtesttask = {
        def task = fcService.getTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
        	task.instance.properties = params
        	return ['taskInstance':task.instance,'testInstanceIDs':params.testInstanceIDs]
        }
    }
    
    def setplanningtesttask = {
        def task = fcService.setplanningtesttaskTask(params) 
        flash.message = task.message
        if (!task.instance) {
            redirect(controller:"contest",action:"tasks")
        } else {
        	redirect(action:listplanning,id:task.instance.id)
        }
    }
    
    def assignflighttestwind = {
        def task = fcService.assignflighttestwindTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.testinstanceids?.size > 1) {
        	redirect(action:selectflighttestwind,id:task.instance.id,params:[testInstanceIDs:task.testinstanceids])
        } else {
        	redirect(action:listplanning,id:task.instance.id)
        }
    }

    def selectflighttestwind = {
        def task = fcService.getTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
        	task.instance.properties = params
        	return ['taskInstance':task.instance,'testInstanceIDs':params.testInstanceIDs]
        }
    }
    
    def setflighttestwind = {
        def task = fcService.setflighttestwindTask(params) 
        flash.message = task.message
        if (!task.instance) {
            redirect(controller:"contest",action:"tasks")
        } else {
        	redirect(action:listplanning,id:task.instance.id)
        }
    }

	def calculatesequence = {
        def task = fcService.calculatesequenceTask(params) 
        flash.message = task.message
        if (!task.instance) {
            redirect(controller:"contest",action:"tasks")
        } else {
            flash.error = task.error
        	redirect(action:listplanning,id:task.instance.id)
        }
	}

	def resetsequence = {
        def task = fcService.resetsequenceTask(params) 
        flash.message = task.message
        if (!task.instance) {
            redirect(controller:"contest",action:"tasks")
        } else {
            flash.error = task.error
        	redirect(action:listplanning,id:task.instance.id)
        }
	}

	def moveup = {
        def task = fcService.moveupTask(params,session) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.borderreached) {
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.error) {
       		flash.message = task.message
            flash.error = true
            redirect(action:listplanning,id:task.instance.id)
        } else { 
	        if (task.selectedtestids) {
	    		flash.selectedTestIDs = task.selectedtestids
	    	}
	        redirect(action:listplanning,id:task.instance.id)
        }
	}
	
	def movedown = {
        def task = fcService.movedownTask(params,session) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.borderreached) {
            redirect(action:listplanning,id:task.instance.id)
        } else if (task.error) {
       		flash.message = task.message
            flash.error = true
            redirect(action:listplanning,id:task.instance.id)
        } else {
	    	if (task.selectedtestids) {
	    		flash.selectedTestIDs = task.selectedtestids
	    	}
	        redirect(action:listplanning,id:task.instance.id)
        }
	}
	
	def moveend = {
        def task = fcService.moveendTask(params,session) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.borderreached) {
            redirect(action:listplanning,id:task.instance.id)
        } else if (task.error) {
       		flash.message = task.message
            flash.error = true
            redirect(action:listplanning,id:task.instance.id)
        } else {
	    	if (task.selectedtestids) {
	    		flash.selectedTestIDs = task.selectedtestids
	    	}
	        redirect(action:listplanning,id:task.instance.id)
        }
	}
	
	def calculatetimetable = {
        def task = fcService.calculatetimetableTask(params) 
        flash.message = task.message
        if (!task.instance) {
            redirect(controller:"contest",action:"tasks")
        } else {
			if (task.error) {
				flash.error = true
			}
        	redirect(action:listplanning,id:task.instance.id)
		}
	}

	def timeadd = {
        def task = fcService.timeaddTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
	   		flash.selectedTestIDs = task.selectedtestids
	    	redirect(action:listplanning,id:task.instance.id)
        }
	}

	def timesubtract = {
        def task = fcService.timesubtractTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
	   		flash.selectedTestIDs = task.selectedtestids
	        redirect(action:listplanning,id:task.instance.id)
        }
	}

	def timetableprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def task = fcService.gettimetableprintableTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
        	return [taskInstance:task.instance ]
        }
    }

	def timetablejuryprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def task = fcService.gettimetableprintableTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
        	return [taskInstance:task.instance ]
        }
    }

	def printtimetable = {
        def task = fcService.printtimetableTask(params,GetPrintParams(),false) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	fcService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"timetable-task${task.instance.idTitle}-${task.instance.GetTimeTableVersion()}",true,task.instance.printTimetableA3,task.instance.printTimetableLandscape)
	    } else {
        	redirect(action:listplanning,id:task.instance.id)
	    }
	}
	
	def printtimetablejury = {
        def task = fcService.printtimetableTask(params,GetPrintParams(),true) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	fcService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"jurytimetable-task${task.instance.idTitle}-${task.instance.GetTimeTableVersion()}",true,task.instance.printTimetableJuryA3,task.instance.printTimetableJuryLandscape)
	    } else {
        	redirect(action:listplanning,id:task.instance.id)
	    }
	}
	
	def printflightplans = {
        def task = fcService.printflightplansTask(params,false,false,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	fcService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"flightplans-task${task.instance.idTitle}-${task.instance.GetTimeTableVersion()}",true,false,false)
	    } else {
        	redirect(action:listplanning,id:task.instance.id)
	    }
	}

    def printplanningtesttask = {
        def task = fcService.printplanningtasksTask(params,false,false,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
            flash.message = task.message
        	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	fcService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"planningtasks-task${task.instance.idTitle}",true,false,false)
	    } else {
	        redirect(action:listplanning,id:task.instance.id)
		}
	}

	def calculatepositions = {
        def task = fcService.calculatepositionsTask(params) 
        flash.message = task.message
        if (!task.instance) {
            redirect(controller:"contest",action:"tasks")
        } else {
			if (task.error) {
				flash.error = true
			}
        	redirect(action:listresults,id:task.instance.id)
		}
	}

	def printresults = {
        def task = fcService.positionscalculatedTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listresults,id:task.instance.id)
        } else {
			redirect(action:listresultsprintquestion,id:task.instance.id)
        }
	}

	def printtaskresults = {
		def task = fcService.positionscalculatedTask(params)
		if (!task.instance) {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		} else if (task.error) {
			flash.message = task.message
			   flash.error = true
			redirect(action:listresults,id:task.instance.id)
		} else {
			task = fcService.printresultsTask(params,false,true,GetPrintParams())
			if (!task.instance) {
				flash.message = task.message
				redirect(controller:"contest",action:"tasks")
			} else if (task.error) {
				flash.message = task.message
			    flash.error = true
				redirect(action:listresults,id:task.instance.id)
			} else if (task.content) {
				fcService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"results-task${task.instance.idTitle}",true,false,true)
			} else {
				redirect(action:listresults,id:task.instance.id)
			}
		}
	}
	
    def listresultsprintquestion = {
        def task = fcService.getTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
        	task.instance.properties = params
        	return ['taskInstance':task.instance,listresultsprintquestionReturnAction:"startresults",listresultsprintquestionReturnController:controllerName,listresultsprintquestionReturnID:params.id]
        }
    }
	
	def printresultclassresults = {
		def task = fcService.printresultclassresultsTask(params,false,true,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listresults,id:task.instance.id)
        } else if (task.content) {
        	fcService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"classresults-task${task.instance.idTitle}",true,false,true)
	    } else {
        	redirect(action:listresults,id:task.instance.id)
	    }
	}
    
	def listresultsprintable = {
        def task = fcService.getTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
			task.instance.printProvisionalResults = params.printProvisionalResults == "true"
	        if (params.contestid) {
	            session.lastContest = Contest.get(params.contestid)
	        }
	        if (params.resultclassid) {
				ResultClass resultclass_instance = ResultClass.get(params.resultclassid)
				session.contestTitle = resultclass_instance.GetPrintContestTitle()
				return [taskInstance:task.instance,resultclassInstance:resultclass_instance]
			}
        	return [taskInstance:task.instance]
        }
    }

	def crewresultsprintquestion = {
        def task = fcService.getTask(params) 
        if (task.instance) {
			// set return action
           	return [taskInstance:task.instance,crewresultsprintquestionReturnAction:"startresults",crewresultsprintquestionReturnController:controllerName,crewresultsprintquestionReturnID:params.id]
        } else {
            flash.message = task.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def printcrewresults = {
        def task = fcService.printcrewresultsTask(params,false,false,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listresults,id:task.instance.id)
        } else if (task.content) {
        	fcService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"crewresults-task${task.instance.idTitle}",true,false,false)
	    } else {
        	redirect(action:listresults,id:task.instance.id)
	    }
	}

	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
	
	void SetLimit() {
		if (params.showlimit == "on") {
			session.showLimit = true
			if (!session?.showLimitStartPos) {
				session.showLimitStartPos = 0
			}
		} else if (params.showlimit == "off") {
			session.showLimit = false
		} else if (params.startpos) {
			session.showLimitStartPos = params.startpos.toInteger()
		}
	}
}

