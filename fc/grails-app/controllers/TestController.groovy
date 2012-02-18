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
			session.teamReturnAction = actionName
			session.teamReturnController = controllerName
			session.teamReturnID = params.id
			session.resultclassReturnAction = actionName
			session.resultclassReturnController = controllerName
			session.resultclassReturnID = params.id
        	return [testInstance:test.instance,flightplanReturnAction:"startplanning",flightplanReturnController:"task",flightplanReturnID:test.instance.task.id]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        }
    }

    def flightplanprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getflightplanprintableTest(params) 
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
           	return [testInstance:test.instance,planningtaskReturnAction:"startplanning",planningtaskReturnController:"task",planningtaskReturnID:test.instance.task.id]
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
        def test = fcService.completeplanningtaskresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def planningtaskresultssave = {
        def test = fcService.saveplanningtaskresultsTest(params) 
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
        def test = fcService.openplanningtaskresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        }
	}
	
    def printplanningtaskresults = {
        def test = fcService.printplanningtaskresultsTest(params,GetPrintParams()) 
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

    def planningtaskresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getplanningtaskresultsprintableTest(params) 
        if (test.instance) {
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
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
        def test = fcService.completeflightresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def flightresultssave = {
        def test = fcService.saveflightresultsTest(params) 
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
        def test = fcService.openflightresultsreTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        }
    }
    
    def printflightresults = {
        def test = fcService.printflightresultsTest(params,GetPrintParams()) 
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

    def flightresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getflightresultsprintableTest(params) 
        if (test.instance) {
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def setnoflightresults = {
        def test = fcService.setnoflightresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        } else {
            redirect(action:flightresults,id:params.id)
        }
    }
	 
    def importresults = {
		if (session?.lastContest) {
	        def ret = fcService.existAnyAflosCrew(session.lastContest)
	        if (ret.error) {
	            flash.error = ret.error
	            flash.message = ret.message
	            redirect(action:flightresults,id:params.id)
	        } else {
	            redirect(action:selectafloscrew,id:params.id)
	        }
		} else {
            redirect(controller:"task",action:"startresults")
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
        def ret = fcService.importAflosResults(params) 
        if (ret.saved) {
            flash.message = ret.message
            if (ret.error) {
            	flash.error = ret.error
            }
        } else if (ret.error) {
            flash.error = ret.error
            flash.message = ret.message
        }
        redirect(action:flightresults,id:params.id)
    }
	    
	def viewimporterrors = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			if (test.instance.crew.mark) {
				String startnum = test.instance.crew.mark.substring(test.instance.crew.mark.lastIndexOf('(')+1, test.instance.crew.mark.lastIndexOf(')') )
				redirect(controller:"aflosErrorPoints",action:"crew",params:[startnum:startnum,routename:test.instance.flighttestwind.flighttest.route.mark])
			} else {
				redirect(action:flightresults,id:params.id)
			}
        } else {
			redirect(action:flightresults,id:params.id)
		}
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
        def test = fcService.completeobservationresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def observationresultsreopen = {
        def test = fcService.openobservationresultsTest(params) 
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
        def test = fcService.saveobservationresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        }
    }
    
    def printobservationresults = {
        def test = fcService.printobservationresultsTest(params,GetPrintParams()) 
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

    def observationresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getobservationresultsprintableTest(params) 
        if (test.instance) {
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
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
        def test = fcService.completelandingresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:landingresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultsreopen = {
        def test = fcService.openlandingresultsTest(params) 
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
        def test = fcService.savelandingresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:landingresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:landingresults,id:params.id)
        }
    }
    
    def printlandingresults = {
        def test = fcService.printlandingresultsTest(params,GetPrintParams()) 
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

    def landingresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getlandingresultsprintableTest(params) 
        if (test.instance) {
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
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
        def test = fcService.completespecialresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def specialresultsreopen = {
        def test = fcService.openspecialresultsTest(params) 
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
        def test = fcService.savespecialresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        }
    }
    
    def printspecialresults = {
        def test = fcService.printspecialresultsTest(params,GetPrintParams()) 
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

    def specialresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getspecialresultsprintableTest(params) 
        if (test.instance) {
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def crewresultsprintquestion = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			// set return action
           	return [testInstance:test.instance,crewresultsprintquestionReturnAction:"crewresults",crewresultsprintquestionReturnController:controllerName,crewresultsprintquestionReturnID:params.id]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def crewresults = {
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

    def printcrewresults = {
        def test = fcService.printcrewresultsTest(params,GetPrintParams()) 
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

    def crewresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getresultsprintableTest(params) 
        if (test.instance) {
			test.instance.printPlanningResults = params.printPlanningResults == "true"
			test.instance.printFlightResults = params.printFlightResults == "true"
			test.instance.printObservationResults = params.printObservationResults == "true"
			test.instance.printLandingResults = params.printLandingResults == "true"
			test.instance.printSpecialResults = params.printSpecialResults == "true"
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def cancel = {
		// process return action
		if (params.crewresultsprintquestionReturnAction) {
			redirect(action:params.crewresultsprintquestionReturnAction,controller:params.crewresultsprintquestionReturnController,id:params.crewresultsprintquestionReturnID)
		} else if (params.flightplanReturnAction) {
			redirect(action:params.flightplanReturnAction,controller:params.flightplanReturnController,id:params.flightplanReturnID)
		} else if (params.planningtaskReturnAction) {
			redirect(action:params.planningtaskReturnAction,controller:params.planningtaskReturnController,id:params.planningtaskReturnID)
		} else {
       		redirect(controller:"task",action:'startresults')
		}
    }
	
	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
}
