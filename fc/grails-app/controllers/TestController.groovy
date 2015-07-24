class TestController 
{
	def fcService
    def gpxService
    def mailService

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
            return [testInstance:test.instance,showReturnAction:session.showReturnAction,showReturnController:session.showReturnController,showReturnID:session.showReturnID]
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
            session.printLanguage = params.lang
        }
        def test = fcService.getflightplanprintableTest(params) 
        if (test.instance) {
        	return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        }
    }

    def printflightplan = {
        def test = fcService.printflightplanTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        } else if (test.error) {
        	flash.message = test.message
        	flash.error = true
        	redirect(action:show,id:test.instance.id)
        } else if (test.content) {
        	fcService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"flightplan-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.timetableVersion}",true,false,false)
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
            session.printLanguage = params.lang
        }
        def test = fcService.getTest(params) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        }
    }

    def printplanningtask = {
        def test = fcService.printplanningtaskTest(params,false,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"planningtask-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def printplanningtaskwithresults = {
        def test = fcService.printplanningtaskTest(params,false,false,true,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"planningtaskwithresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}",true,false,false)
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

	def planningtaskresultsready = {
        def test = fcService.readyplanningtaskresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults",params:[message:test.message])
        }
	}
	
	def planningtaskresultsreadynext = {
        def test = fcService.readyplanningtaskresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Planningtask)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Planningtask)
			if (nexttest_id) {
				if (next2test_id) {
					redirect(action:planningtaskresults,id:nexttest_id,params:[next:next2test_id])
				} else {
					redirect(action:planningtaskresults,id:nexttest_id)
				}
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def planningtaskresultsgotonext = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Planningtask)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Planningtask)
			if (nexttest_id) {
				if (next2test_id) {
					redirect(action:planningtaskresults,id:nexttest_id,params:[next:next2test_id])
				} else {
					redirect(action:planningtaskresults,id:nexttest_id)
				}
			} else {
				redirect(controller:"task",action:"startresults")
			}
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
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Planningtask)
			if (nexttest_id) {
				redirect(action:planningtaskresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:planningtaskresults,id:params.id)
			}
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
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Planningtask)
			if (nexttest_id) {
				redirect(action:planningtaskresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:planningtaskresults,id:params.id)
			}
        } else {
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        }
	}
	
    def printplanningtaskresults = {
        def test = fcService.printplanningtaskresultsTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"planningtaskresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetPlanningTestVersion()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def planningtaskresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getplanningtaskresultsprintableTest(params) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
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

    def flightresultsready = {
        def test = fcService.readyflightresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults",params:[message:test.message])
        }
    }
    
    def flightresultsreadynext = {
        def test = fcService.readyflightresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Flight)
			if (nexttest_id) {
				if (next2test_id) {
					redirect(action:flightresults,id:nexttest_id,params:[next:next2test_id])
				} else {
					redirect(action:flightresults,id:nexttest_id)
				}
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def flightresultsgotonext = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Flight)
			if (nexttest_id) {
				if (next2test_id) {
					redirect(action:flightresults,id:nexttest_id,params:[next:next2test_id])
				} else {
					redirect(action:flightresults,id:nexttest_id)
				}
			} else {
				redirect(controller:"task",action:"startresults")
			}
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
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight)
			if (nexttest_id) {
				redirect(action:flightresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:flightresults,id:params.id)
			}
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
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight)
			if (nexttest_id) {
				redirect(action:flightresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:flightresults,id:params.id)
			}
        } else {
            flash.message = test.message
            redirect(action:flightresults,id:params.id)
        }
    }
    
    def printflightresults = {
        def test = fcService.printflightresultsTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"navigationresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetFlightTestVersion()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def printaflosflightresults = {
        def test = fcService.printaflosflightresultsTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"navigationmeasure-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetFlightTestVersion()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def flightresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getflightresultsprintableTest(params) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def flightresultsaflosprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getflightresultsprintableTest(params) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
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
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight)
			if (nexttest_id) {
				redirect(action:flightresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:flightresults,id:params.id)
			}
        } else {
            redirect(action:flightresults,id:params.id)
        }
    }
	 
    def importresults = {
		if (session?.lastContest) {
			session.lastContest.refresh()
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
        def ret = fcService.importAflosResultsTest(params) 
        if (ret.saved) {
            flash.message = ret.message
            if (ret.error) {
            	flash.error = ret.error
            }
			def test = fcService.getTest(params)
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight)
			if (nexttest_id) {
				redirect(action:flightresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:flightresults,id:params.id)
			}
        } else if (ret.error) {
            flash.error = ret.error
            flash.message = ret.message
			redirect(action:flightresults,id:params.id)
        } else {
			redirect(action:flightresults,id:params.id)
        }
    }
	    
    def cancelaflosresults = {
		def test = fcService.getTest(params)
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight)
			if (nexttest_id) {
				redirect(action:flightresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:flightresults,id:params.id)
			}
        } else {
			redirect(action:flightresults,id:params.id)
        }
    }
	    
	def viewimporterrors = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			int start_num = test.instance.GetAFLOSStartNum()
			redirect(controller:"aflosErrorPoints",action:"crew",params:[startnum:start_num,routename:test.instance.flighttestwind.flighttest.route.mark])
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

    def observationresultsready = {
        def test = fcService.readyobservationresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults",params:[message:test.message])
        }
    }
    
    def observationresultsreadynext = {
        def test = fcService.readyobservationresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Observation)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Observation)
			if (nexttest_id) {
				if (next2test_id) {
					redirect(action:observationresults,id:nexttest_id,params:[next:next2test_id])
				} else {
					redirect(action:observationresults,id:nexttest_id)
				}
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def observationresultsgotonext = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Observation)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Observation)
			if (nexttest_id) {
				if (next2test_id) {
					redirect(action:observationresults,id:nexttest_id,params:[next:next2test_id])
				} else {
					redirect(action:observationresults,id:nexttest_id)
				}
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def observationresultssave = {
        def test = fcService.saveobservationresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Observation)
			if (nexttest_id) {
				redirect(action:observationresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:observationresults,id:params.id)
			}
		} else {
            flash.message = test.message
			redirect(action:observationresults,id:params.id)
		}
    }
	
    def observationresultsreopen = {
        def test = fcService.openobservationresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Observation)
			if (nexttest_id) {
				redirect(action:observationresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:observationresults,id:params.id)
			}
        } else {
            flash.message = test.message
            redirect(action:observationresults,id:params.id)
        }
    }
    
    def printobservationresults = {
        def test = fcService.printobservationresultsTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"observationresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetObservationTestVersion()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def observationresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getobservationresultsprintableTest(params) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
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

    def landingresultsready = {
        def test = fcService.readylandingresultsTest(params)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Landing)
	        if (test.error) {
	            flash.error = true
				if (nexttest_id && next2test_id) {
					redirect(action:landingresults,id:params.id,params:[next:next2test_id])
				} else {
	            	redirect(action:landingresults,id:params.id)
				}
	        } else {
	            redirect(controller:"task",action:"startresults",params:[message:test.message])
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultsreadynext = {
        def test = fcService.readylandingresultsTest(params)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Landing)
	        if (test.error) {
	            flash.error = true
				if (nexttest_id && next2test_id) {
					redirect(action:landingresults,id:params.id,params:[next:next2test_id])
				} else {
	            	redirect(action:landingresults,id:params.id)
				}
	        } else {
				if (nexttest_id) {
					if (next2test_id) {
						redirect(action:landingresults,id:nexttest_id,params:[next:next2test_id])
					} else {
						redirect(action:landingresults,id:nexttest_id)
					}
				} else {
					redirect(action:landingresults,id:nexttest_id)
				}
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def landingresultsgotonext = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Landing)
			if (nexttest_id) {
				if (next2test_id) {
					redirect(action:landingresults,id:nexttest_id,params:[next:next2test_id])
				} else {
					redirect(action:landingresults,id:nexttest_id)
				}
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def landingresultssave = {
        def test = fcService.savelandingresultsTest(params)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing)
	        if (test.error) {
	            flash.error = true
	        }
			if (nexttest_id) {
				redirect(action:landingresults,id:params.id,params:[next:nexttest_id])
			} else {
            	redirect(action:landingresults,id:params.id)
			}
        } else {
            redirect(action:landingresults,id:params.id)
        }
    }
    
    def landingresultsreopen = {
        def test = fcService.openlandingresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:landingresults,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing)
			if (nexttest_id) {
				redirect(action:landingresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:landingresults,id:params.id)
			}
        } else {
            flash.message = test.message
            redirect(action:landingresults,id:params.id)
        }
    }
    
    def printlandingresults = {
        def test = fcService.printlandingresultsTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"landingresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetLandingTestVersion()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def landingresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getlandingresultsprintableTest(params) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
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

    def specialresultsready = {
        def test = fcService.readyspecialresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults",params:[message:test.message])
        }
    }
    
    def specialresultsreadynext = {
        def test = fcService.readyspecialresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Special)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Special)
			if (nexttest_id) {
				if (next2test_id) {
					redirect(action:specialresults,id:nexttest_id,params:[next:next2test_id])
				} else {
					redirect(action:specialresults,id:nexttest_id)
				}
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def specialresultsgotonext = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Special)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Special)
			if (nexttest_id) {
				if (next2test_id) {
					redirect(action:specialresults,id:nexttest_id,params:[next:next2test_id])
				} else {
					redirect(action:specialresults,id:nexttest_id)
				}
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def specialresultssave = {
        def test = fcService.savespecialresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Special)
			if (nexttest_id) {
				redirect(action:specialresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:specialresults,id:params.id)
			}
        } else {
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        }
    }
    
    def specialresultsreopen = {
        def test = fcService.openspecialresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Special)
			if (nexttest_id) {
				redirect(action:specialresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:specialresults,id:params.id)
			}
        } else {
            flash.message = test.message
            redirect(action:specialresults,id:params.id)
        }
    }
    
    def printspecialresults = {
        def test = fcService.printspecialresultsTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"specialresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetSpecialTestVersion()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def specialresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getspecialresultsprintableTest(params) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def crewresultsgotonext = {
        def test = fcService.getTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Crew)
			long next2test_id = Test.GetNext2TestID(nexttest_id,ResultType.Crew)
			if (nexttest_id) {
				if (next2test_id) {
					redirect(action:crewresults,id:nexttest_id,params:[next:next2test_id])
				} else {
					redirect(action:crewresults,id:nexttest_id)
				}
			} else {
				redirect(controller:"task",action:"startresults")
			}
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
			// assign return action
			if (session.crewresultsReturnAction) {
				return [testInstance:test.instance,crewresultsReturnAction:session.crewresultsReturnAction,crewresultsReturnController:session.crewresultsReturnController,crewresultsReturnID:session.crewresultsReturnID]
			}
           	return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def printcrewresults = {
        def test = fcService.printcrewresultsTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"crewresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetCrewResultsVersion()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def crewresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getresultsprintableTest(params) 
        if (test.instance) {
			test.instance.printPlanningResults = params.printPlanningResults == "true"
			test.instance.printFlightResults = params.printFlightResults == "true"
			test.instance.printObservationResults = params.printObservationResults == "true"
			test.instance.printLandingResults = params.printLandingResults == "true"
			test.instance.printSpecialResults = params.printSpecialResults == "true"
			test.instance.printProvisionalResults = params.printProvisionalResults == "true"
            return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def showmap = {
        def test = fcService.getTest(params)
        if (test.instance) {
            gpxService.printstart "Show map of '${test.instance.crew.name}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_gpx_file_name = "gpxupload/GPX-${uuid}-UPLOAD.gpx"
            Map converter = gpxService.ConvertTest2GPX(test.instance, webroot_dir + upload_gpx_file_name)
            if (converter.ok && converter.track) {
                gpxService.printdone ""
                //session.gpxviewerReturnAction = 'flightresults'
                //session.gpxviewerReturnController = controllerName
                //session.gpxviewerReturnID = params.id
                session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                redirect(controller:'gpx',action:'startgpxviewer',params:[uploadFilename:upload_gpx_file_name,originalFilename:test.instance.GetTitle(ResultType.Flight).encodeAsHTML(),testID:test.instance.id,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"no",showProfiles:"yes"])
            } else {
                flash.error = true
                if (converter.ok && !converter.track) {
                    flash.message = message(code:'fc.gpx.noflight',args:[test.instance.crew.name])
                } else {
                    flash.message = message(code:'fc.gpx.gacnotconverted',args:[test.instance.crew.name])
                }
                gpxService.DeleteFile(upload_gpx_file_name)
                gpxService.printerror flash.message
                redirect(action:'flightresults',id:params.id)
            }
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def sendmail = {
        def test = fcService.getTest(params)
        if (test.instance) {
            String email_to = test.instance.EMailAddress()
            gpxService.printstart "Send mail of '${test.instance.crew.name}' to '${email_to}'"
            
            // Calculate flight test version
            if (test.instance.flightTestModified) {
                test.instance.flightTestVersion++
                test.instance.flightTestModified = false
                test.instance.save()
                gpxService.println "flightTestVersion $test.instance.flightTestVersion of '$test.instance.crew.name' saved."
            }
            
            long nexttest_id = test.instance.GetNextTestID(ResultType.Flight)
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_gpx_file_name = "gpxupload/GPX-${uuid}-EMAIL.gpx"
            Map converter = gpxService.ConvertTest2GPX(test.instance, webroot_dir + upload_gpx_file_name)
            if (converter.ok && converter.track) {
                
                Map email = test.instance.GetEMailBody()
                String job_file_name = "jobs/JOB-${uuid}.job"
                try {
                    // create email job
                    File job_file = new File(webroot_dir + job_file_name)
                    BufferedWriter job_writer = job_file.newWriter()
                    gpxService.WriteLine(job_writer,test.instance.crew.uuid) // 1
                    gpxService.WriteLine(job_writer,"file:${webroot_dir + upload_gpx_file_name}") // 2
                    gpxService.WriteLine(job_writer,"${test.instance.GetFileName(ResultType.Flight)}.gpx") // 3
                    gpxService.WriteLine(job_writer,test.instance.crew.uuid) // 4
                    gpxService.WriteLine(job_writer,"http://localhost:8080/fc/gpx/startftpgpxviewer?id=${test.instance.id}&printLanguage=${session.printLanguage}&lang=${session.printLanguage}&showProfiles=yes&gpxShowPoints=${HTMLFilter.GetStr(converter.gpxShowPoints)}") // 5
                    gpxService.WriteLine(job_writer,"${test.instance.GetFileName(ResultType.Flight)}.htm") // 6
                    gpxService.WriteLine(job_writer,email_to) // 7
                    gpxService.WriteLine(job_writer,test.instance.GetEMailTitle(ResultType.Flight)) // 8
                    gpxService.WriteLine(job_writer,email.body) // 9
                    gpxService.WriteLine(job_writer,email.link) // 10
                    gpxService.WriteLine(job_writer,test.instance.id.toString()) // 11
                    gpxService.WriteLine(job_writer,webroot_dir + upload_gpx_file_name) // 12
                    job_writer.close()
                    
                    // set sending link
                    test.instance.flightTestLink = Global.EMAIL_SENDING
                    test.instance.save()
                    
                    flash.message = message(code:'fc.net.mail.prepared',args:[email_to])
                    gpxService.println "Job '${job_file_name}' created." 
                } catch (Exception e) {
                    gpxService.println "Error: '${job_file_name}' could not be created ('${e.getMessage()}')" 
                }
                                
                gpxService.printdone ""
                if (test.instance.flightTestComplete) {
                    redirect(controller:"task",action:"startresults",params:[message:flash.message])
                } else {
                    if (nexttest_id) {
                        redirect(action:'flightresults',id:params.id,params:[next:nexttest_id])
                    } else {
                        redirect(action:'flightresults',id:params.id)
                    }
                }
            } else {
                flash.error = true
                if (converter.ok && !converter.track) {
                    flash.message = message(code:'fc.gpx.noflight',args:[test.instance.crew.name])
                } else {
                    flash.message = message(code:'fc.gpx.gacnotconverted',args:[test.instance.crew.name])
                }
                gpxService.DeleteFile(upload_gpx_file_name)
                gpxService.printerror flash.message
                if (nexttest_id) {
                    redirect(action:'flightresults',id:params.id,params:[next:nexttest_id])
                } else {
                    redirect(action:'flightresults',id:params.id)
                }
            }
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
    private String GetEMailGpxURL(Test testInstance)
    {
        String crew_dir = "${grailsApplication.config.flightcontest.ftp.contesturl}/${testInstance.crew.uuid}"
        String gpx_url = "${crew_dir}/${testInstance.GetFileName(ResultType.Flight)}.gpx"
        return gpx_url
    }
    
    def cancel = {
		// process return action
		if (params.crewresultsReturnAction) {
			redirect(action:params.crewresultsReturnAction,controller:params.crewresultsReturnController,id:params.crewresultsReturnID)
		} else if (params.crewresultsprintquestionReturnAction) {
			redirect(action:params.crewresultsprintquestionReturnAction,controller:params.crewresultsprintquestionReturnController,id:params.crewresultsprintquestionReturnID)
		} else if (params.flightplanReturnAction) {
			redirect(action:params.flightplanReturnAction,controller:params.flightplanReturnController,id:params.flightplanReturnID)
		} else if (params.planningtaskReturnAction) {
			redirect(action:params.planningtaskReturnAction,controller:params.planningtaskReturnController,id:params.planningtaskReturnID)
		} else if (params.showReturnAction) {
			redirect(action:params.showReturnAction,controller:params.showReturnController,id:params.showReturnID)
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
