class TestController 
{
	def fcService

    def show = {
        def test = fcService.getTest(params) 
        if (test.instance) {
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
			// assign return action
			if (session.positionsReturnAction) {
				return [testInstance:test.instance,positionsReturnAction:session.positionsReturnAction,positionsReturnController:session.positionsReturnController,positionsReturnID:session.positionsReturnID]
			} else {
            	return [testInstance:test.instance]
			}
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"contest",action:"tasks")
        }
    }

    def flightplan = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
			session.aircraftReturnAction = actionName
			session.aircraftReturnController = controllerName
			session.aircraftReturnID = params.id
			// assign return action
			if (session.positionsReturnAction) {
				return [testInstance:test.instance,positionsReturnAction:session.positionsReturnAction,positionsReturnController:session.positionsReturnController,positionsReturnID:session.positionsReturnID]
			} else {
            	return [testInstance:test.instance]
			}
        	return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        }
    }

    def flightplanprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getTest(params) 
        if (test.instance) {
        	return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        }
    }

    def printflightplan = {
        def test = fcService.printflightplanTest(params,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        } else if (test.error) {
        	flash.message = test.message
        	flash.error = true
        	redirect(action:show,id:test.instance.id)
        } else if (test.content) {
        	fcService.WritePDF(response,test.content)
        } else {
        	redirect(action:show,id:test.instance.id)
        }
    }

    def planningtask = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
			// assign return action
			if (session.positionsReturnAction) {
				return [testInstance:test.instance,positionsReturnAction:session.positionsReturnAction,positionsReturnController:session.positionsReturnController,positionsReturnID:session.positionsReturnID]
			} else {
            	return [testInstance:test.instance]
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        }
    }

    def planningtaskprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getTest(params) 
        if (test.instance) {
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        }
    }

    def printplanningtask = {
        def test = fcService.printplanningtaskTest(params,GetPrintParams(),false) // false - without result 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

	def printplanningtaskresult = {
        def test = fcService.printplanningtaskTest(params,GetPrintParams(),true) // true - with result
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content)
        } else {
            redirect(action:show,id:test.instance.id)
        }
	}
	
    def planningtaskresults = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
			session.aircraftReturnAction = actionName
			session.aircraftReturnController = controllerName
			session.aircraftReturnID = params.id
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }

	def planningtaskresultscomplete = {
        def test = fcService.planningtaskresultscompleteTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"listresults",id:test.instance.task.id)
        }
	}
	
    def planningtaskresultssave = {
        def test = fcService.planningtaskresultssaveTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        }
    }
    
    def planningtaskresultsreopen = {
        def test = fcService.planningtaskresultsopenTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        }
	}
	
    def flightresults = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
			session.aircraftReturnAction = actionName
			session.aircraftReturnController = controllerName
			session.aircraftReturnID = params.id
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def flightresultscomplete = {
        def test = fcService.flightresultscompleteTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"listresults",id:test.instance.task.id)
        }
    }
    
    def flightresultssave = {
        def test = fcService.flightresultssaveTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        }
    }
    
    def flightresultsreopen = {
        def test = fcService.flightresultsreopenTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        }
    }
    
    def importresults = {
        def route = fcService.existAnyAflosCrew()
        if (route.error) {
            flash.error = route.error
            flash.message = route.message
            redirect(action:flightresults,id:params.id)
        } else {
            redirect(action:selectafloscrew,id:params.id)
        }
    }
	    
    def selectafloscrew = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"contest",action:"tasks")
        }
    }
	    
    def importaflosresults = {
        def route = fcService.importAflosResults(params) 
        if (route.saved) {
            flash.message = route.message
            if (route.error) {
            	flash.error = route.error
            }
        } else if (route.error) {
            flash.error = route.error
            flash.message = route.message
        }
        redirect(action:flightresults,id:params.id)
    }
	    
    def observationresults = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
        	return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def observationresultscomplete = {
        def test = fcService.observationresultscompleteTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"listresults",id:test.instance.task.id)
        }
    }
    
    def observationresultsreopen = {
        def test = fcService.observationresultsopenTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        }
    }
    
    def observationresultssave = {
        def test = fcService.observationresultssaveTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        }
    }
    
    def landingresults = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
        	return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def landingresultscomplete = {
        def test = fcService.landingresultscompleteTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:landingresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"listresults",id:test.instance.task.id)
        }
    }
    
    def landingresultsreopen = {
        def test = fcService.landingresultsopenTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:landingresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:landingresults,id:params.id)
        }
    }
    
    def landingresultssave = {
        def test = fcService.landingresultssaveTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:landingresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:landingresults,id:params.id)
        }
    }
    
    def specialresults = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
        	return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def specialresultscomplete = {
        def test = fcService.specialresultscompleteTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"listresults",id:test.instance.task.id)
        }
    }
    
    def specialresultsreopen = {
        def test = fcService.specialresultsopenTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        }
    }
    
    def specialresultssave = {
        def test = fcService.specialresultssaveTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        }
    }
    
    def debriefing = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
			session.aircraftReturnAction = actionName
			session.aircraftReturnController = controllerName
			session.aircraftReturnID = params.id
			// assign return action
			if (session.positionsReturnAction) {
				return [testInstance:test.instance,positionsReturnAction:session.positionsReturnAction,positionsReturnController:session.positionsReturnController,positionsReturnID:session.positionsReturnID]
			} else {
            	return [testInstance:test.instance]
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def printdebriefing = {
        def test = fcService.printdebriefingTest(params,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def debriefingprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getTest(params) 
        if (test.instance) {
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def cancel = {
		// process return action
		if (params.positionsReturnAction) {
			redirect(action:params.positionsReturnAction,controller:params.positionsReturnController,id:params.positionsReturnID)
		} else {
        	redirect(controller:"task",action:'startresults')
		}
    }
	
	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'
               ]
    }
}
