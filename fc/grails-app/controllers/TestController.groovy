import java.util.Map;

class TestController 
{
    def domainService
    def printService
    def imageService
	def fcService
    def gpxService
    def kmlService
    def calcService
    def emailService
    def trackerService

    def show = {
        Map test = domainService.GetTest(params) 
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
        Map test = domainService.GetTest(params) 
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
        Map test = printService.printflightplanTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        } else if (test.error) {
        	flash.message = test.message
        	flash.error = true
        	redirect(action:show,id:test.instance.id)
        } else if (test.content) {
        	printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"flightplan-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.timetableVersion}",true,false,false)
        } else {
        	redirect(action:show,id:test.instance.id)
        }
    }

	def flightplangotonext = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
			if (nexttest_id) {
				redirect(action:flightplan,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def flightplangotoprev = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Flight,session)
			if (prevtest_id) {
				redirect(action:flightplan,id:prevtest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def planningtask = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
            session.routeReturnAction = actionName
            session.routeReturnController = controllerName
            session.routeReturnID = params.id
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
        Map test = domainService.GetTest(params) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        }
    }

    def printplanningtask = {
        Map test = printService.printplanningtaskTest(params,false,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"planningtask-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def printplanningtaskwithresults = {
        Map test = printService.printplanningtaskTest(params,false,false,true,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"planningtaskwithresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

	def planningtaskgotonext = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
			if (nexttest_id) {
				redirect(action:planningtask,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def planningtaskgotoprev = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Flight,session)
			if (prevtest_id) {
				redirect(action:planningtask,id:prevtest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def planningtaskresults = {
        Map test = domainService.GetTest(params) 
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
            trackerService.updateTestResult(params, ResultType.Planningtask)
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
            trackerService.updateTestResult(params, ResultType.Planningtask)
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Planningtask,session)
			if (nexttest_id) {
				redirect(action:planningtaskresults,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def planningtaskresultsgotonext = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Planningtask,session)
			if (nexttest_id) {
				redirect(action:planningtaskresults,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def planningtaskresultsgotoprev = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Planningtask,session)
			if (prevtest_id) {
				redirect(action:planningtaskresults,id:prevtest_id)
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
			long nexttest_id = test.instance.GetNextTestID(ResultType.Planningtask,session)
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
			long nexttest_id = test.instance.GetNextTestID(ResultType.Planningtask,session)
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
        Map test = printService.printplanningtaskresultsTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"planningtaskresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetPlanningTestVersion()}",true,false,false)
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
	
    def planningtaskformimportcrew = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            redirect(action:planningtaskformimport,id:params.id)
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def planningtaskformimport = {
        Map test = domainService.GetTest(params)
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
    
    def planningtaskformimportimagefile = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
            def file = request.getFile('imagefile')
            Map img = imageService.LoadImage(ImageService.JPG_EXTENSION, file, test.instance, "scannedPlanningTest", Test.SCANNEDIMAGEMAXSIZE)
            if (!img.found) {
                img = imageService.LoadImage("", file, test.instance, "scannedPlanningTest", Test.SCANNEDIMAGEMAXSIZE)
            }
            flash.error = img.error
            flash.message = img.message
            long nexttest_id = test.instance.GetNextTestID(ResultType.Planningtask,session)
            if (nexttest_id) {
                redirect(action:planningtaskresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:planningtaskresults,id:params.id)
            }
        } else {
            flash.message = test.message
            redirect(controller:"contest",action:"tasks")
        }
    }
        
    def planningtaskformdeleteimagefile = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
            delete_imagefile(['testInstance':test.instance,'imageField':'scannedPlanningTest'])
            flash.message = message(code:'fc.planningtesttask.deleteform.deleted')
            long nexttest_id = test.instance.GetNextTestID(ResultType.Planningtask,session)
            if (nexttest_id) {
                redirect(action:planningtaskresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:planningtaskresults,id:params.id)
            }
        } else {
            flash.message = test.message
            redirect(controller:"contest",action:"tasks")
        }
    }
    
    def planningtaskformimage = {
        if (params.testid) {
            Test test = Test.get(params.testid)
            response.outputStream << test.scannedPlanningTest
        }
    }
    
    def planningtaskformimportcancel = {
        Map test = domainService.GetTest(params)
        if (test.instance) {
            long nexttest_id = test.instance.GetNextTestID(ResultType.Planningtask,session)
            if (nexttest_id) {
                redirect(action:planningtaskresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:planningtaskresults,id:params.id)
            }
        } else {
            redirect(action:planningtaskresults,id:params.id)
        }
    }
        
    def flightresults = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
			session.aircraftReturnAction = actionName
			session.aircraftReturnController = controllerName
			session.aircraftReturnID = params.id
            session.flighttestwindReturnAction = actionName
            session.flighttestwindReturnController = controllerName
            session.flighttestwindReturnID = params.id
            session.routeReturnAction = actionName
            session.routeReturnController = controllerName
            session.routeReturnID = params.id
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
            trackerService.updateTestResult(params, ResultType.Flight)
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
            trackerService.updateTestResult(params, ResultType.Flight)
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
			if (nexttest_id) {
				redirect(action:flightresults,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def flightresultsgotonext = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
			if (nexttest_id) {
				redirect(action:flightresults,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def flightresultsgotoprev = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Flight,session)
			if (prevtest_id) {
				redirect(action:flightresults,id:prevtest_id)
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
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
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
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
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
        String webroot_dir = servletContext.getRealPath("/")
        Map test = printService.printflightresultsTest(params,false,false,webroot_dir,GetPrintParams()) 
        if (test.instance) {
            if (test.error) {
                flash.message = test.message
                flash.error = true
                redirect(action:show,id:test.instance.id)
            } else if (test.content) {
                printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"navigationresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetFlightTestVersion()}",true,false,false)
            } else {
                redirect(action:show,id:test.instance.id)
            }
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def printmeasureflightresults = {
        String webroot_dir = servletContext.getRealPath("/")
        Map test = printService.printmeasureflightresultsTest(params,false,false,webroot_dir,GetPrintParams()) 
        if (test.instance) {
            if (test.error) {
                flash.message = test.message
                flash.error = true
                redirect(action:show,id:test.instance.id)
            } else if (test.content) {
                printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"navigationmeasure-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetFlightTestVersion()}",true,false,false)
            } else {
                redirect(action:show,id:test.instance.id)
            }
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def printloggerdata = {
        String webroot_dir = servletContext.getRealPath("/")
        Map test = printService.printloggerdataTest(params,false,false,webroot_dir,GetPrintParams()) 
        if (test.instance) {
            if (test.error) {
                flash.message = test.message
                flash.error = true
                redirect(action:show,id:test.instance.id)
            } else if (test.content) {
                printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"loggerdata-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetFlightTestVersion()}",true,false,false)
            } else {
                redirect(action:show,id:test.instance.id)
            }
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def flightresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getflightresultsprintableTest(params) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance,flightMapFileName:params.flightMapFileName]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def flightresultsmeasureprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getflightresultsprintableTest(params) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance,flightMapFileName:params.flightMapFileName]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def loggerdataprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getflightresultsprintableTest(params)
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance,flightMapFileName:params.flightMapFileName]
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
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
			if (nexttest_id) {
				redirect(action:flightresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:flightresults,id:params.id)
			}
        } else {
            redirect(action:flightresults,id:params.id)
        }
    }
	 
    def observationprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        Map test = domainService.GetTest(params) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startplanning")
        }
    }

    def importtracker = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            redirect(action:importtrackercrew,id:params.id)
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def importtrackercrew = {
        Map test = domainService.GetTest(params)
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
    
    def importlogger = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            redirect(action:importloggercrew,id:params.id)
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def importloggercrew = {
        Map test = domainService.GetTest(params)
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
    
    def recalculateresults = {
        def ret = fcService.recalculateResultsTest(params)
        if (ret.saved) {
            flash.message = ret.message
            if (ret.error) {
                flash.error = ret.error
            }
            Map test = domainService.GetTest(params)
            long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
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
    
    def importtrackerdata = {
        def ret = trackerService.importTrackerPointsAndCalcTest(params) 
        if (ret.saved) {
            flash.message = ret.message
            if (ret.error) {
            	flash.error = ret.error
            }
			Map test = domainService.GetTest(params)
			long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
			if (nexttest_id) {
				redirect(action:flightresults,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:flightresults,id:params.id)
			}
        } else if (ret.error) {
            flash.error = ret.error
            flash.message = ret.message
            Map test = domainService.GetTest(params)
            long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
            if (nexttest_id) {
                redirect(action:flightresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:flightresults,id:params.id)
            }
        } else {
			redirect(action:flightresults,id:params.id)
        }
    }
    
    def importloggerfile = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
            def file = request.getFile('loggerfile')
            boolean interpolate_missing_data = params?.interpolate_missing_data == "on"
            int correct_seconds = 0
            if (params?.correct_seconds && params.correct_seconds.isInteger()) {
                correct_seconds = params.correct_seconds.toInteger()
            }
            Map calc = fcService.calculateLoggerResultTest(LoggerFileTools.GAC_EXTENSION, test.instance, file, false, interpolate_missing_data, correct_seconds)
            if (!calc.found) {
                calc = fcService.calculateLoggerResultTest(LoggerFileTools.IGC_EXTENSION, test.instance, file, false, interpolate_missing_data, correct_seconds)
            }
            if (!calc.found) {
                calc = fcService.calculateLoggerResultTest(LoggerFileTools.GPX_EXTENSION, test.instance, file, false, interpolate_missing_data, correct_seconds)
            }
            if (!calc.found) {
                calc = fcService.calculateLoggerResultTest(LoggerFileTools.KML_EXTENSION, test.instance, file, false, interpolate_missing_data, correct_seconds)
            }
            if (!calc.found) {
                calc = fcService.calculateLoggerResultTest(LoggerFileTools.KMZ_EXTENSION, test.instance, file, false, interpolate_missing_data, correct_seconds)
            }
            if (!calc.found) {
                calc = fcService.calculateLoggerResultTest(LoggerFileTools.NMEA_EXTENSION, test.instance, file, false, interpolate_missing_data, correct_seconds)
            }
            if (!calc.found) {
                calc = fcService.calculateLoggerResultTest("", test.instance, file, false, interpolate_missing_data, correct_seconds)
            }
            flash.error = calc.error
            flash.message = calc.message
            long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
            if (nexttest_id) {
                redirect(action:flightresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:flightresults,id:params.id)
            }
        } else {
            flash.message = test.message
            redirect(controller:"contest",action:"tasks")
        }
    }
	    
    def recalculatecrew = {
        Map test = domainService.GetTest(params) 
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
    
    def judgeenablecalcresult = {
        def ret = fcService.changeCalcResultTest(params, true)
        if (ret.saved) {
            flash.message = ret.message
            if (ret.error) {
                flash.error = ret.error
            }
            long nexttest_id = ret.instance.GetNextTestID(ResultType.Flight,session)
            if (nexttest_id) {
                redirect(action:flightresults,id:ret.instance.id,params:[next:nexttest_id])
            } else {
                redirect(action:flightresults,id:ret.instance.id)
            }
        } else if (ret.error) {
            flash.error = ret.error
            flash.message = ret.message
            redirect(action:flightresults,id:ret.instance.id)
        } else {
            redirect(action:flightresults,id:ret.instance.id)
        }
    }
    
    def judgedisablecalcresult = {
        def ret = fcService.changeCalcResultTest(params, false)
        if (ret.saved) {
            flash.message = ret.message
            if (ret.error) {
                flash.error = ret.error
            }
            long nexttest_id = ret.instance.GetNextTestID(ResultType.Flight,session)
            if (nexttest_id) {
                redirect(action:flightresults,id:ret.instance.id,params:[next:nexttest_id])
            } else {
                redirect(action:flightresults,id:ret.instance.id)
            }
        } else if (ret.error) {
            flash.error = ret.error
            flash.message = ret.message
            redirect(action:flightresults,id:ret.instance.id)
        } else {
            redirect(action:flightresults,id:ret.instance.id)
        }
    }
    
    def observationresults = {
        Map test = domainService.GetTest(params) 
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
            long nexttest_id = test.instance.GetNextTestID(ResultType.Observation,session)
            if (nexttest_id) {
                redirect(action:observationresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:observationresults,id:params.id)
            }
        } else {
            trackerService.updateTestResult(params, ResultType.Observation)
            flash.message = test.message
            redirect(controller:"task",action:"startresults",params:[message:test.message])
        }
    }
    
    def observationresultsreadynext = {
        def test = fcService.readyobservationresultsTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            long nexttest_id = test.instance.GetNextTestID(ResultType.Observation,session)
            if (nexttest_id) {
                redirect(action:observationresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:observationresults,id:params.id)
            }
        } else if (test.instance) {
            trackerService.updateTestResult(params, ResultType.Observation)
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Observation,session)
			if (nexttest_id) {
				redirect(action:observationresults,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def observationresultsgotonext = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Observation,session)
			if (nexttest_id) {
				redirect(action:observationresults,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def observationresultsgotoprev = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Observation,session)
			if (prevtest_id) {
				redirect(action:observationresults,id:prevtest_id)
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
            long nexttest_id = test.instance.GetNextTestID(ResultType.Observation,session)
            if (nexttest_id) {
                redirect(action:observationresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:observationresults,id:params.id)
            }
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Observation,session)
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
			long nexttest_id = test.instance.GetNextTestID(ResultType.Observation,session)
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
        Map test = printService.printobservationresultsTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"observationresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetObservationTestVersion()}",true,false,false)
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
	
    def observationformimportcrew = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            redirect(action:observationformimport,id:params.id)
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def observationformimport = {
        Map test = domainService.GetTest(params)
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
    
    def observationformimportimagefile = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
            def file = request.getFile('imagefile')
            Map img = imageService.LoadImage(ImageService.JPG_EXTENSION, file, test.instance, "scannedObservationTest", Test.SCANNEDIMAGEMAXSIZE)
            if (!img.found) {
                img = imageService.LoadImage("", file, test.instance, "scannedObservationTest", Test.SCANNEDIMAGEMAXSIZE)
            }
            flash.error = img.error
            flash.message = img.message
            long nexttest_id = test.instance.GetNextTestID(ResultType.Observation,session)
            if (nexttest_id) {
                redirect(action:observationresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:observationresults,id:params.id)
            }
        } else {
            flash.message = test.message
            redirect(controller:"contest",action:"tasks")
        }
    }
        
    def observationformdeleteimagefile = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
            delete_imagefile(['testInstance':test.instance,'imageField':'scannedObservationTest'])
            flash.message = message(code:'fc.observation.deleteform.deleted')
            long nexttest_id = test.instance.GetNextTestID(ResultType.Observation,session)
            if (nexttest_id) {
                redirect(action:observationresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:observationresults,id:params.id)
            }
        } else {
            flash.message = test.message
            redirect(controller:"contest",action:"tasks")
        }
    }
    
    def observationformimage = {
        if (params.testid) {
            Test test = Test.get(params.testid)
            try {
                response.outputStream << test.scannedObservationTest
            } catch (Exception e) {
            }
        }
    }
    
    def observationformimportcancel = {
        Map test = domainService.GetTest(params)
        if (test.instance) {
            long nexttest_id = test.instance.GetNextTestID(ResultType.Observation,session)
            if (nexttest_id) {
                redirect(action:observationresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:observationresults,id:params.id)
            }
        } else {
            redirect(action:observationresults,id:params.id)
        }
    }
        
    private void delete_imagefile(Map params)
    {
        fcService.printstart "Delete '$params.imageField'"
        params.testInstance.(params.imageField) = null
        params.testInstance.save()
        fcService.printdone ""
    }   
    
    def landingresults = {
        Map test = domainService.GetTest(params) 
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
        def test = fcService.readylandingresultsTest(params, ResultType.Landing)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing)
	            redirect(controller:"task",action:"startresults",params:[message:test.message])
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultsreadynext = {
        def test = fcService.readylandingresultsTest(params, ResultType.Landing)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing)
				if (nexttest_id) {
					redirect(action:landingresults,id:nexttest_id)
				} else {
					redirect(controller:"task",action:"startresults")
				}
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def landingresultsgotonext = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing,session)
			if (nexttest_id) {
				redirect(action:landingresults,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def landingresultsgotoprev = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Landing,session)
			if (prevtest_id) {
				redirect(action:landingresults,id:prevtest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def landingresultssavenext = {
        def test = fcService.savelandingresultsTest(params, ResultType.Landing)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing)
				if (nexttest_id) {
					redirect(action:landingresults,id:nexttest_id)
				} else {
					redirect(controller:"task",action:"startresults")
				}
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultssave = {
        def test = fcService.savelandingresultsTest(params, ResultType.Landing)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing,session)
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
        def test = fcService.openlandingresultsTest(params, ResultType.Landing) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:landingresults,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing,session)
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
        Map test = printService.printlandingresultsTest(params, false, false, GetPrintParams(), ResultType.Landing) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"landingresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetLandingTestVersion()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def landingresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getlandingresultsprintableTest(params, ResultType.Landing) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def landingresults1 = {
        Map test = domainService.GetTest(params) 
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

    def landingresultsready1 = {
        def test = fcService.readylandingresultsTest(params, ResultType.Landing1)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing1,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults1,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing1)
	            redirect(controller:"task",action:"startresults",params:[message:test.message])
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultsreadynext1 = {
        def test = fcService.readylandingresultsTest(params, ResultType.Landing1)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing1,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults1,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing1)
				if (nexttest_id) {
					redirect(action:landingresults1,id:nexttest_id)
				} else {
					redirect(controller:"task",action:"startresults")
				}
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def landingresultsgotonext1 = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing1,session)
			if (nexttest_id) {
				redirect(action:landingresults1,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def landingresultsgotoprev1 = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Landing1,session)
			if (prevtest_id) {
				redirect(action:landingresults1,id:prevtest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def landingresultssavenext1 = {
        def test = fcService.savelandingresultsTest(params, ResultType.Landing1)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing1,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults1,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing1)
				if (nexttest_id) {
					redirect(action:landingresults1,id:nexttest_id)
				} else {
					redirect(controller:"task",action:"startresults")
				}
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultssave1 = {
        def test = fcService.savelandingresultsTest(params, ResultType.Landing1)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing1,session)
	        if (test.error) {
	            flash.error = true
	        }
			if (nexttest_id) {
				redirect(action:landingresults1,id:params.id,params:[next:nexttest_id])
			} else {
            	redirect(action:landingresults1,id:params.id)
			}
        } else {
            redirect(action:landingresults1,id:params.id)
        }
    }
    
    def landingresultsreopen1 = {
        def test = fcService.openlandingresultsTest(params, ResultType.Landing1)
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:landingresults1,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing1,session)
			if (nexttest_id) {
				redirect(action:landingresults1,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:landingresults1,id:params.id)
			}
        } else {
            flash.message = test.message
            redirect(action:landingresults1,id:params.id)
        }
    }
	
    def printlandingresults1 = {
        Map test = printService.printlandingresultsTest(params, false, false, GetPrintParams(), ResultType.Landing1) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"landingresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetLandingTest1Version()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def landingresultsprintable1 = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getlandingresultsprintableTest(params, ResultType.Landing1) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def landingresults2 = {
        Map test = domainService.GetTest(params) 
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

    def landingresultsready2 = {
        def test = fcService.readylandingresultsTest(params, ResultType.Landing2)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing2,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults2,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing2)
	            redirect(controller:"task",action:"startresults",params:[message:test.message])
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultsreadynext2 = {
        def test = fcService.readylandingresultsTest(params, ResultType.Landing2)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing2,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults2,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing2)
				if (nexttest_id) {
					redirect(action:landingresults2,id:nexttest_id)
				} else {
					redirect(controller:"task",action:"startresults")
				}
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def landingresultsgotonext2 = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing2,session)
			if (nexttest_id) {
				redirect(action:landingresults2,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def landingresultsgotoprev2 = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Landing2,session)
			if (prevtest_id) {
				redirect(action:landingresults2,id:prevtest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def landingresultssavenext2 = {
        def test = fcService.savelandingresultsTest(params, ResultType.Landing2)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing2,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults2,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing2)
				if (nexttest_id) {
					redirect(action:landingresults2,id:nexttest_id)
				} else {
					redirect(controller:"task",action:"startresults")
				}
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultssave2 = {
        def test = fcService.savelandingresultsTest(params, ResultType.Landing2)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing2,session)
	        if (test.error) {
	            flash.error = true
	        }
			if (nexttest_id) {
				redirect(action:landingresults2,id:params.id,params:[next:nexttest_id])
			} else {
            	redirect(action:landingresults2,id:params.id)
			}
        } else {
            redirect(action:landingresults2,id:params.id)
        }
    }
    
    def landingresultsreopen2 = {
        def test = fcService.openlandingresultsTest(params, ResultType.Landing2)
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:landingresults2,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing2,session)
			if (nexttest_id) {
				redirect(action:landingresults2,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:landingresults2,id:params.id)
			}
        } else {
            flash.message = test.message
            redirect(action:landingresults2,id:params.id)
        }
    }
	
    def printlandingresults2 = {
        Map test = printService.printlandingresultsTest(params, false, false, GetPrintParams(), ResultType.Landing2) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"landingresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetLandingTest2Version()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def landingresultsprintable2 = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getlandingresultsprintableTest(params, ResultType.Landing2) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def landingresults3 = {
        Map test = domainService.GetTest(params) 
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

    def landingresultsready3 = {
        def test = fcService.readylandingresultsTest(params, ResultType.Landing3)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing3,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults3,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing3)
	            redirect(controller:"task",action:"startresults",params:[message:test.message])
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultsreadynext3 = {
        def test = fcService.readylandingresultsTest(params, ResultType.Landing3)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing3,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults3,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing3)
				if (nexttest_id) {
					redirect(action:landingresults3,id:nexttest_id)
				} else {
					redirect(controller:"task",action:"startresults")
				}
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def landingresultsgotonext3 = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing3,session)
			if (nexttest_id) {
				redirect(action:landingresults3,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def landingresultsgotoprev3 = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Landing3,session)
			if (prevtest_id) {
				redirect(action:landingresults3,id:prevtest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def landingresultssavenext3 = {
        def test = fcService.savelandingresultsTest(params, ResultType.Landing3)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing3,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults3,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing3)
				if (nexttest_id) {
					redirect(action:landingresults3,id:nexttest_id)
				} else {
					redirect(controller:"task",action:"startresults")
				}
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultssave3 = {
        def test = fcService.savelandingresultsTest(params, ResultType.Landing3)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing3,session)
	        if (test.error) {
	            flash.error = true
	        }
			if (nexttest_id) {
				redirect(action:landingresults3,id:params.id,params:[next:nexttest_id])
			} else {
            	redirect(action:landingresults3,id:params.id)
			}
        } else {
            redirect(action:landingresults3,id:params.id)
        }
    }
    
    def landingresultsreopen3 = {
        def test = fcService.openlandingresultsTest(params, ResultType.Landing3) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:landingresults3,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing3,session)
			if (nexttest_id) {
				redirect(action:landingresults3,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:landingresults3,id:params.id)
			}
        } else {
            flash.message = test.message
            redirect(action:landingresults3,id:params.id)
        }
    }

    def printlandingresults3 = {
        Map test = printService.printlandingresultsTest(params, false, false, GetPrintParams(), ResultType.Landing3) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"landingresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetLandingTest3Version()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def landingresultsprintable3 = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getlandingresultsprintableTest(params, ResultType.Landing3) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def landingresults4 = {
        Map test = domainService.GetTest(params) 
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

    def landingresultsready4 = {
        def test = fcService.readylandingresultsTest(params, ResultType.Landing4)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing4,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults4,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing4)
	            redirect(controller:"task",action:"startresults",params:[message:test.message])
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultsreadynext4 = {
        def test = fcService.readylandingresultsTest(params, ResultType.Landing4)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing4,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults4,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing4)
				if (nexttest_id) {
					redirect(action:landingresults4,id:nexttest_id)
				} else {
					redirect(controller:"task",action:"startresults")
				}
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def landingresultsgotonext4 = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing4,session)
			if (nexttest_id) {
				redirect(action:landingresults4,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def landingresultsgotoprev4 = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Landing4,session)
			if (prevtest_id) {
				redirect(action:landingresults4,id:prevtest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def landingresultssavenext4 = {
        def test = fcService.savelandingresultsTest(params, ResultType.Landing4)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing4,session)
	        if (test.error) {
	            flash.error = true
            	redirect(action:landingresults4,id:params.id)
	        } else {
                trackerService.updateTestResult(params, ResultType.Landing4)
				if (nexttest_id) {
					redirect(action:landingresults4,id:nexttest_id)
				} else {
					redirect(controller:"task",action:"startresults")
				}
	        }
        } else {
            redirect(controller:"task",action:"startresults")
        }
    }
    
    def landingresultssave4 = {
        def test = fcService.savelandingresultsTest(params, ResultType.Landing4)
        flash.message = test.message
		if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing4,session)
	        if (test.error) {
	            flash.error = true
	        }
			if (nexttest_id) {
				redirect(action:landingresults4,id:params.id,params:[next:nexttest_id])
			} else {
            	redirect(action:landingresults4,id:params.id)
			}
        } else {
            redirect(action:landingresults4,id:params.id)
        }
    }
    
    def landingresultsreopen4 = {
        def test = fcService.openlandingresultsTest(params, ResultType.Landing4) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:landingresults4,id:params.id)
        } else if (test.instance) {
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Landing4,session)
			if (nexttest_id) {
				redirect(action:landingresults4,id:params.id,params:[next:nexttest_id])
			} else {
				redirect(action:landingresults4,id:params.id)
			}
        } else {
            flash.message = test.message
            redirect(action:landingresults4,id:params.id)
        }
    }

    def printlandingresults4 = {
        Map test = printService.printlandingresultsTest(params, false, false, GetPrintParams(), ResultType.Landing4) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"landingresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetLandingTest4Version()}",true,false,false)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

    def landingresultsprintable4 = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getlandingresultsprintableTest(params, ResultType.Landing4) 
        if (test.instance) {
            return [contestInstance:session.lastContest,testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def specialresults = {
        Map test = domainService.GetTest(params) 
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
            trackerService.updateTestResult(params, ResultType.Special)
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
            trackerService.updateTestResult(params, ResultType.Special)
            flash.message = test.message
			long nexttest_id = test.instance.GetNextTestID(ResultType.Special,session)
			if (nexttest_id) {
				redirect(action:specialresults,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }
    
	def specialresultsgotonext = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Special,session)
			if (nexttest_id) {
				redirect(action:specialresults,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def specialresultsgotoprev = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Special,session)
			if (prevtest_id) {
				redirect(action:specialresults,id:prevtest_id)
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
			long nexttest_id = test.instance.GetNextTestID(ResultType.Special,session)
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
			long nexttest_id = test.instance.GetNextTestID(ResultType.Special,session)
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
        Map test = printService.printspecialresultsTest(params,false,false,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"specialresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetSpecialTestVersion()}",true,false,false)
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
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long nexttest_id = test.instance.GetNextTestID(ResultType.Crew,session)
			if (nexttest_id) {
				redirect(action:crewresults,id:nexttest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def crewresultsgotoprev = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			long prevtest_id = test.instance.GetPrevTestID(ResultType.Crew,session)
			if (prevtest_id) {
				redirect(action:crewresults,id:prevtest_id)
			} else {
				redirect(controller:"task",action:"startresults")
			}
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
	def crewresultsprintquestion = {
        Map test = domainService.GetTest(params) 
        if (test.instance) {
			// set return action
            if (params.fromTask) {
                test.instance.printPlanningResults = false
                test.instance.printPlanningResultsScan = false
                test.instance.printFlightResults = false
                test.instance.printFlightMap = false
                test.instance.printObservationResults = false
                test.instance.printObservationResultsScan = false
                test.instance.printLandingResults = false
                test.instance.printSpecialResults = false
                return [testInstance:test.instance,taskInstance:test.instance.task,crewresultsprintquestionReturnAction:"startresults",crewresultsprintquestionReturnController:"task",crewresultsprintquestionReturnID:params.id]
            } else {
                return [testInstance:test.instance,crewresultsprintquestionReturnAction:"crewresults",crewresultsprintquestionReturnController:controllerName,crewresultsprintquestionReturnID:params.id]
            }
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def crewresults = {
        Map test = domainService.GetTest(params) 
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
        String webroot_dir = servletContext.getRealPath("/")
        Map test = printService.printcrewresultsTest(params,false,false,webroot_dir,GetPrintParams()) 
        if (test.instance) {
            if (test.error) {
                flash.message = test.message
                flash.error = true
                redirect(action:show,id:test.instance.id)
            } else if (test.content) {
                printService.WritePDF(response,test.content,session.lastContest.GetPrintPrefix(),"crewresults-task${test.instance.task.idTitle}-crew${test.instance.crew.startNum}-${test.instance.GetCrewResultsVersion()}",true,false,false)
            } else {
                redirect(action:show,id:test.instance.id)
            }
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def crewresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        def test = fcService.getresultsprintableTest(params) 
        if (test.instance) {
            test.instance.printSummaryResults = params.printSummaryResults == "true"
			test.instance.printPlanningResults = params.printPlanningResults == "true"
            test.instance.printPlanningResultsScan = params.printPlanningResultsScan == "true"
			test.instance.printFlightResults = params.printFlightResults == "true"
            test.instance.printFlightMap = params.printFlightMap == "true"
			test.instance.printObservationResults = params.printObservationResults == "true"
            test.instance.printObservationResultsScan = params.printObservationResultsScan == "true"
			test.instance.printLandingResults = params.printLandingResults == "true"
			test.instance.printSpecialResults = params.printSpecialResults == "true"
			test.instance.printProvisionalResults = params.printProvisionalResults == "true"
            return [contestInstance:session.lastContest,testInstance:test.instance,flightMapFileName:params.flightMapFileName]
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
	}
	
    def showofflinemap_test = {
        Map test = domainService.GetTest(params)
        if (test.instance) {
            gpxService.printstart "showofflinemap_test: Show map of '${test.instance.crew.name}' $params"
            String uuid = UUID.randomUUID().toString()
            String upload_gpx_file_name = "${GpxService.GPXDATA}-${uuid}"
            Map converter = [:]
            if (params.showCoord) {
                String show_utc = params.showUtc
                if (params.addShowTimeValue && params.addShowTimeValue.isInteger()) {
                    show_utc = FcTime.UTCAddSeconds(show_utc, 60*params.addShowTimeValue.toInteger())
                }
                converter = gpxService.ConvertTest2GPX(test.instance, upload_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:false, gpxExport:false, showCoord:params.showCoord, showUtc:show_utc, lastUtc:params.lastUtc])
            } else {
                converter = gpxService.ConvertTest2GPX(test.instance, upload_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:true, gpxExport:false])
            }
            if (converter.ok && converter.track) {
                gpxService.printdone ""
                session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                redirect(controller:'gpx',action:'startofflineviewer',params:[uploadFilename:upload_gpx_file_name,originalFilename:test.instance.GetTitle(ResultType.Flight).encodeAsHTML(),testID:test.instance.id,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"no",showZoom:"yes",showPoints:"yes",showCoord:params.showCoord])
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
    
    def showmap_test = {
        Map test = domainService.GetTest(params)
        if (test.instance) {
            gpxService.printstart "showmap_test: Show map of '${test.instance.crew.name}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GPX-${uuid}-UPLOAD.gpx"
            Map converter = gpxService.ConvertTest2GPX(test.instance, webroot_dir + upload_gpx_file_name, [isPrint:false, showPoints:true, wrEnrouteSign:true, gpxExport:false])
            if (converter.ok && converter.track) {
                gpxService.printdone ""
                session.gpxShowPoints = HTMLFilter.GetStr(converter.gpxShowPoints)
                redirect(controller:'gpx',action:'startgpxviewer',params:[defaultOnlineMap:test.instance.task.flighttest.route.defaultOnlineMap,uploadFilename:upload_gpx_file_name,originalFilename:test.instance.GetTitle(ResultType.Flight).encodeAsHTML(),testID:test.instance.id,showLanguage:session.showLanguage,lang:session.showLanguage,showCancel:"no",showProfiles:"yes",gmApiKey:BootStrap.global.GetGMApiKey()])
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
    
    def gpxexport_test = {
        Map test = domainService.GetTest(params)
        if (test.instance) {
            gpxService.printstart "gpxexport_test: Export logger data of '${test.instance.crew.name}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/GPX-${uuid}-UPLOAD.gpx"
            Map converter = gpxService.ConvertTest2GPX(test.instance, webroot_dir + upload_gpx_file_name, [isPrint:false, showPoints:false, wrEnrouteSign:true, gpxExport:true, wrPhotoImage:true])
            if (converter.ok && converter.track) {
                String logger_file_name = (test.instance.GetTitle(ResultType.Flight) + '.gpx').replace(' ',"_")
                response.setContentType("application/octet-stream")
                response.setHeader("Content-Disposition", "Attachment;Filename=${logger_file_name}")
                gpxService.Download(webroot_dir + upload_gpx_file_name, logger_file_name, response.outputStream)
                gpxService.DeleteFile(upload_gpx_file_name)
                gpxService.printdone ""
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
     
    def kmzexport_test = {
        Map test = domainService.GetTest(params)
        if (test.instance) {
            kmlService.printstart "kmzexport_test: Export logger data of crew '${test.instance.crew.name}'"
            String uuid = UUID.randomUUID().toString()
            String webroot_dir = servletContext.getRealPath("/")
            String upload_kmz_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/KMZ-${uuid}-UPLOAD.kmz"
            Map converter = kmlService.ConvertTest2KMZ(test.instance, webroot_dir, upload_kmz_file_name, false, true) // false - no Print, true - wrEnrouteSign
            if (converter.ok && converter.track) {
                String logger_file_name = (test.instance.GetTitle(ResultType.Flight) + '.kmz').replace(' ',"_")
                response.setContentType("application/octet-stream")
                response.setHeader("Content-Disposition", "Attachment;Filename=${logger_file_name}")
                kmlService.Download(webroot_dir + upload_kmz_file_name, logger_file_name, response.outputStream)
                kmlService.DeleteFile(upload_kmz_file_name)
                kmlService.printdone ""
            } else {
                flash.error = true
                if (converter.ok && !converter.track) {
                    flash.message = message(code:'fc.kmz.noflight',args:[test.instance.crew.name])
                } else {
                    flash.message = message(code:'fc.kmz.notexported',args:[test.instance.crew.name])
                }
                kmlService.DeleteFile(upload_kmz_file_name)
                kmlService.printerror flash.message
                redirect(action:'flightresults',id:params.id)
            }
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }

    }
     
    def sendmail = {
        Map test = domainService.GetTest(params)
        if (test.instance) {
            Map ret = emailService.SendEmailTest(test.instance, session.printLanguage, grailsAttributes, request)
            flash.message = ret.message
            if (!ret.ok) {
                flash.error = true
            }
            redirect(controller:"task",action:"startresults",params:[message:ret.message])
        } else {
            flash.message = test.message
            redirect(controller:"task",action:"startresults")
        }
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
	
    def cancelimportcrew = {
        Map test = domainService.GetTest(params)
        if (test.instance) {
            long nexttest_id = test.instance.GetNextTestID(ResultType.Flight,session)
            if (nexttest_id) {
                redirect(action:flightresults,id:params.id,params:[next:nexttest_id])
            } else {
                redirect(action:flightresults,id:params.id)
            }
        } else {
            redirect(action:flightresults,id:params.id)
        }
    }
        
	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
}
