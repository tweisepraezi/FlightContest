import org.xhtmlrenderer.pdf.ITextRenderer

class TaskController {

    def domainService
    def imageService
    def kmlService
    def printService
	def fcService
    def evaluationService
    
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def edit = {
        Map task = domainService.GetTask(params) 
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

	def timetable = {
		Map task = domainService.GetTask(params)
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
	
    def timetablejudge = {
        Map task = domainService.GetTask(params)
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
    
    def timetableoverview = {
        Map task = domainService.GetTask(params)
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
			if (task.error) {
				flash.error = true
				flash.message = task.message
			}
			render(view:'edit',model:[taskInstance:task.instance])
        } else {
			flash.message = task.message
			redirect(action:edit,id:params.id)
        }
    }

    def savetimetablejudgesettings = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryModified)
		if (task.instance) {
			flash.message = task.message
			render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
        } else {
			flash.message = task.message
			redirect(action:edit,id:params.id)
        }
    }

	def updatetimetablejudgesettingsstandard = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryStandard) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updatetimetablejudgesettingsnone = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryNone) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updatetimetablejudgesettingsall = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryAll) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updatetimetablejudgesettingstower = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryTower) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updatetimetablejudgesettingsintermediatetower = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryIntermediateTower) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updatetimetablejudgesettingsplanning = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryPlanning) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updatetimetablejudgesettingstakeoff = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryTakeoff) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updatetimetablejudgesettingslanding = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryLanding) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updatetimetablejudgesettingsintermediatelanding = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryIntermediateLanding) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updatetimetablejudgesettingsarrival = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryArrival) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
    def updatetimetablejudgesettingsdebriefing = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableJuryDebriefing) 
        if (task.instance) {
            flash.message = task.message
            render(view:'timetablejudge',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
        } else {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        }
    }
    
    def savetimetableoverviewsettings = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableOverviewModified)
        if (task.instance) {
            flash.message = task.message
            render(view:'timetableoverview',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
        } else {
            flash.message = task.message
            redirect(action:edit,id:params.id)
        }
    }

    def savetimetablesettings = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableModified)
		if (task.instance) {
			flash.message = task.message
			render(view:'timetable',,model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
        } else {
			flash.message = task.message
			redirect(action:edit,id:params.id)
        }
    }

	def updatetimetablesettingsstandard = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableStandard) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetable',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updatetimetablesettingsnone = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableNone) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetable',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def updatetimetablesettingsall = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableAll) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetable',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def removechangetimetablesettings = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableRemoveChange) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetable',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
	def addchangetimetablesettings = {
        def task = fcService.updateprintsettingsTask(params,PrintSettings.TimetableAddChange) 
        if (task.instance) {
			flash.message = task.message
			render(view:'timetable',model:[taskInstance:task.instance,taskReturnAction:session.taskReturnAction,taskReturnController:session.taskReturnController,taskReturnID:session.taskReturnID])
		} else {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		}
	}
	
    def create = {
        def task = fcService.createTask(params,session.lastContest) 
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
            render(view:'create',model:[taskInstance:task.instance,contestInstance:session.lastContest])
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
        Map task = domainService.GetTask(params) 
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
        Map task = domainService.GetTask(params)
        redirect(controller:'planningTest',action:'create',params:['task.id':task.instance.id,'taskid':task.instance.id,'fromtask':true])
	}
	
	def createflighttest = {
        Map task = domainService.GetTask(params) 
        redirect(controller:'flightTest',action:'create',params:['task.id':task.instance.id,'taskid':task.instance.id,'fromtask':true])
	}
	
    def startplanning = {
		fcService.printstart "Start planning"
		if (session?.lastContest) {
			session.lastContest.refresh()
			def task = fcService.startplanningTask(params, session.lastContest, session.lastTaskPlanning)
			if (task.taskid) {
	   			params.id = task.taskid
				fcService.println "last planning task $task.taskid"
	   			redirect(action:listplanning,params:params)
	   		} else {
               redirect(controller:'contest',action:'start')
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
			session.lastContest.refresh()
			Map task = domainService.GetTask(params) 
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
			session.lastContest.refresh()
	        def task = fcService.startresultsTask(params, session.lastContest, session.lastTaskResults)
	        if (task.taskid) {
	            params.id = task.taskid
				fcService.println "last results task $task.taskid"
				if (params.message) {
					flash.message = params.message
				}
	            redirect(action:listresults,params:params)
	        } else {
                redirect(controller:'contest',action:'start')
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
			session.lastContest.refresh()
	        Map task = domainService.GetTask(params) 
	        if (!task.instance) {
	            flash.message = task.message
				fcService.printdone task.message
	            redirect(controller:"contest",action:"tasks")
	        } else if (task.instance.hideResults) {
				session.lastTaskResults = null
				fcService.printdone "Hide task."
				redirect(controller:"contest",action:"listresults")
	        } else {
				SetLimit()
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
				session.crewresultsReturnAction = actionName
				session.crewresultsReturnController = controllerName
				session.crewresultsReturnID = params.id
				fcService.printdone ""
	            return [taskInstance:task.instance]
	        }
		} else {
			fcService.printdone ""
			redirect(controller:'contest',action:'start')
		}
    }

	def startevaluation = {
		fcService.printstart "Start evaluation"
		if (session?.lastContest) {
			session.lastContest.refresh()
			boolean last_found = false
			if (session?.lastContestResults) {
				last_found = true
				fcService.println "last results contest"
				redirect(controller:'contest',action:'listresults',params:params)
			} else if (session?.lastTeamResults) {
				last_found = true
				fcService.println "last results contest"
				redirect(controller:'contest',action:'listteamresults',params:params)
			} else if (session?.lastResultClassResults) {
				def resultclass = fcService.startresultsResultClass(params, session.lastContest, session.lastResultClassResults)
				if (resultclass.resultclassid) {
					last_found = true
					params.id = resultclass.resultclassid
					fcService.println "last results resultclass $resultclass.resultclassid"
					redirect(controller:'resultClass',action:'listresults',params:params)
				}
			} else {
				if (session.lastContest.resultClasses) {
					ResultClass first_resultclass_instance = ResultClass.findByContest(session.lastContest,[sort:"id"])
					if (first_resultclass_instance) {
						session.lastResultClassResults = first_resultclass_instance.id
						def resultclass = fcService.startresultsResultClass(params, session.lastContest, session.lastResultClassResults)
						if (resultclass.resultclassid) {
							last_found = true
							params.id = resultclass.resultclassid
							fcService.println "last results resultclass $resultclass.resultclassid"
							redirect(controller:'resultClass',action:'listresults',params:params)
						}
					}
				}
			}
			if (!last_found) {
				session.lastContestResults = true
				redirect(controller:'contest',action:'listresults',params:params)
			}
			fcService.printdone "last contest"
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
        Map task = domainService.GetTask(params) 
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
            flash.message = task.message
            redirect(action:listplanning,id:task.instance.id)
        }
    }
    
    def selectplanningtesttask = {
        Map task = domainService.GetTask(params) 
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
        Map task = domainService.GetTask(params) 
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

    def printobservation = {
        def task = printService.printobservationTask(params,false,false,GetPrintParams())
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
            flash.message = task.message
            flash.error = true
            redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
            printService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"observation-task${task.instance.idTitle}",true,false,false)
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
	
    def disablecrew = {
        def task = fcService.disableCrewsTask(params,session)
        if (task.instance) {
            flash.message = task.message
            if (task.error) {
                flash.error = true
            }
            redirect(action:listplanning,id:task.instance.id)
        } else {
            redirect(controller:"contest",action:"tasks")
        }
    }
    
    def enablecrew = {
        def task = fcService.enableCrewsTask(params,session)
        if (task.instance) {
            flash.message = task.message
            if (task.error) {
                flash.error = true
            }
            redirect(action:listplanning,id:task.instance.id)
        } else {
            redirect(controller:"contest",action:"tasks")
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
			if (task.error) {
				flash.error = true
				flash.message = task.message
			} else {
	   			flash.selectedTestIDs = task.selectedtestids
			}
	    	redirect(action:listplanning,id:task.instance.id)
        }
	}

	def timesubtract = {
        def task = fcService.timesubtractTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
			if (task.error) {
				flash.error = true
				flash.message = task.message
			} else {
				flash.selectedTestIDs = task.selectedtestids
			}
	        redirect(action:listplanning,id:task.instance.id)
        }
	}

    def exporttimetable = {
        String uuid = UUID.randomUUID().toString()
        String webroot_dir = servletContext.getRealPath("/")
        String upload_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/TXT-${uuid}-UPLOAD.txt"
        def task = fcService.exporttimetableTask(params, webroot_dir + upload_file_name)
        if (task.instance) {
            if (!task.error) {
                String timetable_file_name = (task.instance.name() + '.txt').replace(' ',"_")
                response.setContentType("application/octet-stream")
                response.setHeader("Content-Disposition", "Attachment;Filename=${timetable_file_name}")
                fcService.Download(webroot_dir + upload_file_name, timetable_file_name, response.outputStream)
                fcService.DeleteFile(webroot_dir + upload_file_name)
            }
            flash.message = task.message
            if (task.error) {
                flash.error = true
            }
            redirect(action:listplanning,id:task.instance.id)
        } else {
            redirect(controller:"contest",action:"tasks")
        }
    }

	def timetableprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def task = fcService.gettimetableprintableTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
        	return [contestInstance:session.lastContest,taskInstance:task.instance ]
        }
    }

	def timetablejudgeprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def task = fcService.gettimetableprintableTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
        	return [contestInstance:session.lastContest,taskInstance:task.instance ]
        }
    }

    def timetableoverviewprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def task = fcService.gettimetableprintableTask(params)
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
            return [contestInstance:session.lastContest,taskInstance:task.instance ]
        }
    }

	def printtimetable = {
        Map task = printService.printtimetableTask(params,GetPrintParams(),false) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	printService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"timetable-task${task.instance.idTitle}-${task.instance.GetTimeTableVersion()}",true,task.instance.printTimetableA3,task.instance.printTimetableLandscape)
	    } else {
        	redirect(action:listplanning,id:task.instance.id)
	    }
	}
	
	def printtimetablejudge = {
        Map task = printService.printtimetableTask(params,GetPrintParams(),true) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	printService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"jurytimetable-task${task.instance.idTitle}-${task.instance.GetTimeTableVersion()}",true,task.instance.printTimetableJuryA3,task.instance.printTimetableJuryLandscape)
	    } else {
        	redirect(action:listplanning,id:task.instance.id)
	    }
	}
	
    def printtimetableoverview = {
        Map task = printService.printtimetableoverviewTask(params,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
            flash.message = task.message
            flash.error = true
            redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
            printService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"overviewtimetable-task${task.instance.idTitle}-${task.instance.GetTimeTableVersion()}",true,task.instance.printTimetableOverviewA3,task.instance.printTimetableOverviewLandscape)
        } else {
            redirect(action:listplanning,id:task.instance.id)
        }
    }
    
	def printflightplans = {
        Map task = printService.printflightplansTask(params,false,false,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	printService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"flightplans-task${task.instance.idTitle}-${task.instance.GetTimeTableVersion()}",true,false,false)
	    } else {
        	redirect(action:listplanning,id:task.instance.id)
	    }
	}

    def printplanningtesttask = {
        Map task = printService.printplanningtasksTask(params,false,false,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
            flash.message = task.message
        	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	printService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"planningtasks-task${task.instance.idTitle}",true,false,false)
	    } else {
	        redirect(action:listplanning,id:task.instance.id)
		}
	}

	def calculatepositions = {
        def task = evaluationService.calculatepositionsTask(params) 
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
		Map task = fcService.positionscalculatedTask(params)
		if (!task.instance) {
			flash.message = task.message
			redirect(controller:"contest",action:"tasks")
		} else if (task.error) {
			flash.message = task.message
			   flash.error = true
			redirect(action:listresults,id:task.instance.id)
		} else {
			task = printService.printresultsTask(params,false,true,GetPrintParams())
			if (!task.instance) {
				flash.message = task.message
				redirect(controller:"contest",action:"tasks")
			} else if (task.error) {
				flash.message = task.message
			    flash.error = true
				redirect(action:listresults,id:task.instance.id)
			} else if (task.content) {
				printService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"results-task${task.instance.idTitle}",true,false,true)
			} else {
				redirect(action:listresults,id:task.instance.id)
			}
		}
	}
	
    def listresultsprintquestion = {
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
        	task.instance.properties = params
        	return ['taskInstance':task.instance,listresultsprintquestionReturnAction:"startresults",listresultsprintquestionReturnController:controllerName,listresultsprintquestionReturnID:params.id]
        }
    }
	
	def printresultclassresults = {
		Map task = printService.printresultclassresultsTask(params,false,true,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listresults,id:task.instance.id)
        } else if (task.content) {
        	printService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"classresults-task${task.instance.idTitle}",true,false,true)
	    } else {
        	redirect(action:listresults,id:task.instance.id)
	    }
	}
    
	def listresultsprintable = {
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
			task.instance.printAircraft = params.printAircraft == "true"
			task.instance.printTeam = params.printTeam == "true"
			task.instance.printClass = params.printClass == "true"
			task.instance.printShortClass = params.printShortClass == "true"
            task.instance.printModifiedResults = params.printModifiedResults == "true"
            task.instance.printCompletedResults = params.printCompletedResults == "true"
			task.instance.printProvisionalResults = params.printProvisionalResults == "true"
	        if (params.contestid) {
	            session.lastContest = Contest.get(params.contestid)
                session.printLanguage = params.lang
	        }
	        if (params.resultclassid) {
				ResultClass resultclass_instance = ResultClass.get(params.resultclassid)
				session.contestTitle = resultclass_instance.GetPrintContestTitle()
				return [contestInstance:session.lastContest,taskInstance:task.instance,resultclassInstance:resultclass_instance]
			}
        	return [contestInstance:session.lastContest,taskInstance:task.instance]
        }
    }

	def crewresultsprintquestion = {
        Map task = domainService.GetTask(params) 
        if (task.instance) {
			// set return action
           	return [taskInstance:task.instance,crewresultsprintquestionReturnAction:"startresults",crewresultsprintquestionReturnController:controllerName,crewresultsprintquestionReturnID:params.id]
        } else {
            flash.message = task.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def printcrewresults = {
        String webroot_dir = servletContext.getRealPath("/")
        Map task = printService.printcrewresultsTask(params,false,false,webroot_dir,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listresults,id:task.instance.id)
        } else if (task.content) {
        	printService.WritePDF(response,task.content,session.lastContest.GetPrintPrefix(),"crewresults-task${task.instance.idTitle}",true,false,false)
	    } else {
        	redirect(action:listresults,id:task.instance.id)
	    }
	}
    
    def kmzexport = {
        Map task = domainService.GetTask(params)
        if (task.instance) {
            kmlService.printstart "Export '${task.instance.name()}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_kmz_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/KMZ-${uuid}-UPLOAD.gpx"
            Map converter = kmlService.ConvertTask2KMZ(task.instance, webroot_dir, upload_kmz_file_name, false, true) // false - no Print, true - wrEnrouteSign
            if (converter.ok && converter.track) {
                String task_kmz_file_name = (task.instance.name() + '.kmz').replace(' ',"_")
                response.setContentType("application/octet-stream")
                response.setHeader("Content-Disposition", "Attachment;Filename=${task_kmz_file_name}")
                kmlService.Download(webroot_dir + upload_kmz_file_name, task_kmz_file_name, response.outputStream)
                kmlService.DeleteFile(task_kmz_file_name)
                kmlService.printdone ""
            } else {
                flash.error = true
                if (converter.ok && !converter.track) {
                    flash.message = message(code:'fc.kmz.noflight',args:[task.instance.name()])
                } else {
                    flash.message = message(code:'fc.kmz.notexported',args:[task.instance.name()])
                }
                kmlService.DeleteFile(upload_kmz_file_name)
                kmlService.printerror flash.message
                redirect(action:'listresults',id:params.id)
            }
        } else {
            flash.message = task.message
            redirect(action:listresults)
        }
    }

    def emailnavigationresults = {
        def task = fcService.emailnavigationresultsTask(params,session.printLanguage)
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
            flash.message = task.message
            flash.error = true
            redirect(action:listresults,id:task.instance.id)
        } else {
            redirect(action:listresults,id:task.instance.id)
        }

    }

    def planningtaskformimportextern = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            def task = fcService.startresultsTask(params, session.lastContest, session.lastTaskResults)
            if (task.taskid) {
                redirect(action:planningtaskformimport,id:task.taskid,params:['imagefile':params.imagefile])
            } else {
                redirect(controller:"task",action:"startresults")
            }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def planningtaskformimport = {
        Map task = domainService.GetTask(params) 
        if (task.instance) {
            File image_file = new File(params.imagefile)
            Map img = imageService.LoadImage2(ImageService.JPG_EXTENSION, image_file, Test.SCANNEDIMAGEMAXSIZE)
            if (!img.found) {
                img = imageService.LoadImage2("", image_file, Test.SCANNEDIMAGEMAXSIZE)
            }
            if (img.found) {
                BootStrap.tempData.AddData(params.imagefile, img.bytes)
            }
            if (img.error) {
                flash.error = img.error
                flash.message = img.message
            }
            return [taskInstance:task.instance,imagefile:params.imagefile]
        } else {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        }
    }
    
    def planningtaskformimportsave = {
        if (params.testid) {
            Test test_instance = Test.get(params.testid)
            test_instance.scannedPlanningTest = BootStrap.tempData.GetData(params.imagefile)
            test_instance.save()
        }
        BootStrap.tempData.RemoveData(params.imagefile)
        redirect(controller:"task",action:"startresults")
    }

    def loggerformimportextern = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            def task = fcService.startresultsTask(params, session.lastContest, session.lastTaskResults)
            if (task.taskid) {
                redirect(action:loggerformimport,id:task.taskid,params:['loggerfile':params.loggerfile])
            } else {
                redirect(controller:"task",action:"startresults")
            }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def loggerformimport = {
        Map task = domainService.GetTask(params) 
        if (task.instance) {
            return [taskInstance:task.instance,loggerfile:params.loggerfile]
        } else {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        }
    }
    
    def loggerformimportsave = {
        if (params.testid) {
            Test test_instance = Test.get(params.testid)
            Map calc = fcService.calculateLoggerResultExternTest(LoggerFileTools.GAC_EXTENSION, test_instance, params.loggerfile)
            if (!calc.found) {
                calc = fcService.calculateLoggerResultExternTest(LoggerFileTools.GPX_EXTENSION, test_instance, params.loggerfile)
            }
            if (!calc.found) {
                calc = fcService.calculateLoggerResultExternTest(LoggerFileTools.IGC_EXTENSION, test_instance, params.loggerfile)
            }
            if (!calc.found) {
                calc = fcService.calculateLoggerResultExternTest(LoggerFileTools.NMEA_EXTENSION, test_instance, params.loggerfile)
            }
            if (!calc.found) {
                calc = fcService.calculateLoggerResultExternTest("", test_instance, params.loggerfile, false)
            }
            flash.error = calc.error
            flash.message = calc.message
            long nexttest_id = test_instance.GetNextTestID(ResultType.Flight)
            if (nexttest_id) {
                redirect(controller:'test',action:'flightresults',id:test_instance.id,params:[next:nexttest_id])
            } else {
                redirect(controller:'test',action:'flightresults',id:test_instance.id)
            }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def loggerimportcancel = {
        redirect(controller:"task",action:"startresults")
    }
    
    def observationprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        Map task = domainService.GetTask(params) 
        if (task.instance) {
            return [contestInstance:session.lastContest,taskInstance:task.instance]
        } else {
            flash.message = task.message
            redirect(controller:"task",action:"startplanning")
        }
    }

    def observationformimportextern = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            def task = fcService.startresultsTask(params, session.lastContest, session.lastTaskResults)
            if (task.taskid) {
                redirect(action:observationformimport,id:task.taskid,params:['imagefile':params.imagefile])
            } else {
                redirect(controller:"task",action:"startresults")
            }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def observationformimport = {
        Map task = domainService.GetTask(params) 
        if (task.instance) {
            File image_file = new File(params.imagefile)
            Map img = imageService.LoadImage2(ImageService.JPG_EXTENSION, image_file, Test.SCANNEDIMAGEMAXSIZE)
            if (!img.found) {
                img = imageService.LoadImage2("", image_file, Test.SCANNEDIMAGEMAXSIZE)
            }
            if (img.found) {
                BootStrap.tempData.AddData(params.imagefile, img.bytes)
            }
            if (img.error) {
                flash.error = img.error
                flash.message = img.message
            }
            return [taskInstance:task.instance,imagefile:params.imagefile]
        } else {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        }
    }
    
    def observationformimportsave = {
        if (params.testid) {
            Test test_instance = Test.get(params.testid)
            test_instance.scannedObservationTest = BootStrap.tempData.GetData(params.imagefile)
            test_instance.save()
        }
        BootStrap.tempData.RemoveData(params.imagefile)
        redirect(controller:"task",action:"startresults")
    }
    
    def formimage = {
        if (params.taskid) {
            Task task = Task.get(params.taskid)
            response.outputStream << BootStrap.tempData.GetData(params.imagefile)
        }
    }
    
    def formimportcancel = {
        BootStrap.tempData.RemoveData(params.imagefile)
        redirect(controller:"task",action:"startresults")
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

