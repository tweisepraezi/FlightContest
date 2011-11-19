
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

    def create = {
        def task = fcService.createTask(params) 
        return [taskInstance:task.instance]
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
	
	def cancel = {
		// process return action
		if (params.taskReturnAction) {
			redirect(action:params.taskReturnAction,controller:params.taskReturnController,id:params.taskReturnID)
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
		def task = fcService.startplanningTask(params,session.lastContest,session.lastTaskPlanning)
		if (task.taskid) {
   			params.id = task.taskid
   			redirect(action:listplanning,params:params)
   		}
    }

    def listplanning = {
		def task = fcService.getTask(params) 
        if (!task.instance) {
            flash.message = task.message
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
			session.aircraftReturnAction = actionName
			session.aircraftReturnController = controllerName
			session.aircraftReturnID = params.id
			session.positionsReturnAction = actionName 
			session.positionsReturnController = controllerName
			session.positionsReturnID = params.id
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
        	return [taskInstance:task.instance]
        }
    }

    def startresults = {
        def task = fcService.startresultsTask(params,session.lastContest,session.lastTaskResults)
        if (task.taskid) {
            params.id = task.taskid
            redirect(action:listresults,params:params)
        }
    }

    def listresults = {
        def task = fcService.getTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
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
			session.aircraftReturnAction = actionName
			session.aircraftReturnController = controllerName
			session.aircraftReturnID = params.id
			session.positionsReturnAction = actionName 
			session.positionsReturnController = controllerName
			session.positionsReturnID = params.id
            return [taskInstance:task.instance]
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
        } else if (task.testinstanceids?.size > 0) {
			long i = 0
			task.testinstanceids.each {
				i += it
			}
        	redirect(action:selectplanningtesttask,id:task.instance.id,params:[testInstanceIDs:task.testinstanceids,x:i]) // BUG: Map testInstanceIDs in Map params wird nicht ohne x ausgetauscht
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
        } else if (task.testinstanceids?.size > 0) {
			long i = 0
			task.testinstanceids.each {
				i += it
			}
        	redirect(action:selectflighttestwind,id:task.instance.id,params:[testInstanceIDs:task.testinstanceids,x:i]) // BUG: Map testInstanceIDs in Map params wird nicht ohne x ausgetauscht
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
        def task = fcService.getTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
        	return [taskInstance:task.instance ]
        }
    }

	def printtimetable = {
        def task = fcService.printtimetableTask(params,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	fcService.WritePDF(response,task.content)
	    } else {
        	redirect(action:listplanning,id:task.instance.id)
	    }
	}
	
	def printflightplans = {
        def task = fcService.printflightplansTask(params,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	fcService.WritePDF(response,task.content)
	    } else {
        	redirect(action:listplanning,id:task.instance.id)
	    }
	}

    def printplanningtesttask = {
        def task = fcService.printplanningtasksTask(params,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
            flash.message = task.message
        	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	fcService.WritePDF(response,task.content)
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
        def task = fcService.printresultsTask(params,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listresults,id:task.instance.id)
        } else if (task.content) {
        	fcService.WritePDF(response,task.content)
	    } else {
        	redirect(action:listresults,id:task.instance.id)
	    }
	}

	def positionsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def task = fcService.getTask(params) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else {
        	return [taskInstance:task.instance ]
        }
    }

	def printdebriefings = {
        def task = fcService.printdebriefingsTask(params,GetPrintParams()) 
        if (!task.instance) {
            flash.message = task.message
            redirect(controller:"contest",action:"tasks")
        } else if (task.error) {
        	flash.message = task.message
           	flash.error = true
        	redirect(action:listplanning,id:task.instance.id)
        } else if (task.content) {
        	fcService.WritePDF(response,task.content)
	    } else {
        	redirect(action:listplanning,id:task.instance.id)
	    }
	}

	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'
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

