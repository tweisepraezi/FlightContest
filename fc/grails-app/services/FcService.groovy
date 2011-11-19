import org.xhtmlrenderer.pdf.ITextRenderer

class FcService
{
    boolean transactional = true
    def messageSource
    
    static int mmPerNM = 1852000
    
    
    //--------------------------------------------------------------------------
    Map startAflos(params,lastAflosController)
    {
        if (lastAflosController) {
        	return [controller:lastAflosController]
        }
        return [controller:'aflosRouteDefs']
    }
    
    //--------------------------------------------------------------------------
    Map getAircraft(params)
    {
        def aircraftInstance = Aircraft.get(params.id)
        if (!aircraftInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.aircraft'),params.id])]
        }
        return ['instance':aircraftInstance]
    }

    //--------------------------------------------------------------------------
    Map updateAircraft(params)
    {
        def aircraftInstance = Aircraft.get(params.id)
        if(aircraftInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(aircraftInstance.version > version) {
                    aircraftInstance.errors.rejectValue("version", "aircraft.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':aircraftInstance]
                }
            }

            if (params.registration) {
                def aircraft2Instance = Aircraft.findByRegistrationAndContest(params.registration,aircraftInstance.contest)
                if (aircraft2Instance) {
                    return ['instance':aircraft2Instance,'error':true,'message':getMsg('fc.aircraft.registration.error',["${params.registration}"])]
                }
            }
            
            aircraftInstance.properties = params

            if(!aircraftInstance.hasErrors() && aircraftInstance.save()) {
                return ['instance':aircraftInstance,'saved':true,'message':getMsg('fc.updated',["${aircraftInstance.registration}"])]
            } else {
                return ['instance':aircraftInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.aircraft'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createAircraft(params)
    {
        def aircraftInstance = new Aircraft()
        aircraftInstance.properties = params
        return ['instance':aircraftInstance]
    }
    
    //--------------------------------------------------------------------------
    Map saveAircraft(params,contestInstance)
    {
        def aircraftInstance = new Aircraft(params)
        aircraftInstance.contest = contestInstance
        
        if (params.registration) {
            def aircraft2Instance = Aircraft.findByRegistrationAndContest(params.registration,contestInstance)
            if (aircraft2Instance) {
            	return ['instance':aircraft2Instance,'error':true,'message':getMsg('fc.aircraft.registration.error',["${params.registration}"])]
            }
        }
        
        if(!aircraftInstance.hasErrors() && aircraftInstance.save()) {
            return ['instance':aircraftInstance,'saved':true,'message':getMsg('fc.created',["${aircraftInstance.registration}"])]
        } else {
            return ['instance':aircraftInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteAircraft(params)
    {
        def aircraftInstance = Aircraft.get(params.id)
        if(aircraftInstance) {
            try {
                if (aircraftInstance.user1) {
                    def crewInstance = Crew.get( aircraftInstance.user1.id )
                    crewInstance.aircraft = null
                    crewInstance.save()
                    aircraftInstance.user1 = null 
                }
                if (aircraftInstance.user2) {
                    def crewInstance = Crew.get( aircraftInstance.user2.id )
                    crewInstance.aircraft = null
                    crewInstance.save()
                    aircraftInstance.user2 = null 
                }
                aircraftInstance.delete()
                return ['deleted':true,'message':getMsg('fc.deleted',["${aircraftInstance.registration}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.aircraft'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.aircraft'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map printAircrafts(params,printparams)
    {
        def aircrafts = [:]

        // Print aircrafts
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            def url = "${printparams.baseuri}/aircraft/listprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            aircrafts.content = content 
        }
        catch (Throwable e) {
            aircrafts.message = getMsg('fc.aircraft.printerror',["$e"])
            aircrafts.error = true
        }
        return aircrafts
    }
    
    //--------------------------------------------------------------------------
    Map updateContest(params)
    {
        def contestInstance = Contest.get(params.id)
        
        if (contestInstance) {
            
            if(params.version) {
                def version = params.version.toLong()
                if(contestInstance.version > version) {
                    contestInstance.errors.rejectValue("version", "contest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':contestInstance]
                }
            }
            contestInstance.properties = params
            if(!contestInstance.hasErrors() && contestInstance.save()) {
                return ['instance':contestInstance,'saved':true,'message':getMsg('fc.updated',["${contestInstance.title}"])]
            } else {
                return ['instance':contestInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createContest(params)
    {
        def contestInstance = new Contest()
        contestInstance.properties = params
        return ['instance':contestInstance,'created':true]
    }
    
    //--------------------------------------------------------------------------
    Map saveContest(params)
    {
        def contestInstance = new Contest(params)
        
        if(!contestInstance.hasErrors() && contestInstance.save()) {
            def taskInstance = new Task()
            taskInstance.title = params.taskTitle
            taskInstance.idTitle = 1
            taskInstance.contest = contestInstance
            taskInstance.save()
            return ['instance':contestInstance,'saved':true,'message':getMsg('fc.created',["${contestInstance.title}"])]
        } else {
            return ['instance':contestInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteContest(params)
    {
        def contestInstance = Contest.get(params.id)
        
        if (contestInstance) {
            try {
            	Task.findAllByContest(contestInstance).each { taskInstance ->
            		taskInstance.delete()
            	}
                contestInstance.delete()
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${contestInstance.title}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.contest'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getTask(params)
    {
        def taskInstance = Task.get(params.id)

        if (!taskInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
        
        return ['instance':taskInstance]
    }

    //--------------------------------------------------------------------------
    Map updateTask(params)
    {
        def taskInstance = Task.get(params.id)
        
        if (taskInstance) {
            
            if(params.version) {
                def version = params.version.toLong()
                if(taskInstance.version > version) {
                    taskInstance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':taskInstance]
                }
            }
            
            taskInstance.properties = params
            if(!taskInstance.hasErrors() && taskInstance.save()) {
                return ['instance':taskInstance,'saved':true,'message':getMsg('fc.updated',["${taskInstance.name()}"])]
            } else {
                return ['instance':taskInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createTask(params)
    {
        def taskInstance = new Task()
        taskInstance.properties = params
        return ['instance':taskInstance]
    }
    
    //--------------------------------------------------------------------------
    Map saveTask(params,contestInstance)
    {
    	def taskInstance = new Task(params)
        
        taskInstance.contest = contestInstance
        taskInstance.idTitle = Task.countByContest(contestInstance) + 1
        
        if(!taskInstance.hasErrors() && taskInstance.save()) {
            Crew.findAllByContest(taskInstance.contest).eachWithIndex { crewInstance, i ->
                def testInstance = new Test()
                testInstance.crew = crewInstance
                testInstance.viewpos = i
                testInstance.task = taskInstance
                testInstance.timeCalculated = false
                testInstance.save()
            }
            
            return ['instance':taskInstance,'saved':true,'fromcontestday':params.fromcontestday,'message':getMsg('fc.created',["${taskInstance.name()}"])]
        } else {
            return ['instance':taskInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteTask(params)
    {
        def taskInstance = Task.get(params.id)
        
        if (taskInstance) {
            try {
            	// remove Tests
            	Test.findAllByTask(taskInstance).each { testInstance ->
            		testInstance.delete()
            	}

                taskInstance.delete()
                
                // correct idTitle of other tasks
                Task.findAllByContest(taskInstance.contest).eachWithIndex { taskInstance2, index  -> 
                    taskInstance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${taskInstance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.task'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map startplanningTask(params,contestInstance,lastTaskPlanning)
    {
    	def taskInstance
        if (lastTaskPlanning) {
            taskInstance = Task.findById(lastTaskPlanning)
        }
        if (!taskInstance) {
        	Task.findAllByContest(contestInstance).each {
        		if (!taskInstance) {
        			taskInstance = it
        		}
        	}
        }
        if (taskInstance) {
            return ['taskid':taskInstance.id]
        }
    }
    
    //--------------------------------------------------------------------------
    Map startresultsTask(params,contestInstance,lastTaskResults)
    {
        def taskInstance
        if (lastTaskResults) {
            taskInstance = Task.findById(lastTaskResults)
        }
        if (!taskInstance) {
            Task.findAllByContest(contestInstance).each {
                if (!taskInstance) {
                    taskInstance = it
                }
            }
        }
        if (taskInstance) {
            return ['taskid':taskInstance.id]
        }
    }
    
    //--------------------------------------------------------------------------
    Map selectallTask(params)
    {
        def task = getTask(params) 
        if (task.instance) {
            def selectedTestIDs = [selectedTestID:""]
            Test.findAllByTask(task.instance).each { testInstance ->
                   selectedTestIDs["selectedTestID${testInstance.id}"] = "on"
            }
            task.selectedtestids = selectedTestIDs
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map assignplanningtesttaskTask(params)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // PlanningTest exists?
        if (!task.instance.planningtest) {
            task.message = getMsg('fc.planningtest.notfound')
            task.error = true
            return task
        }

        // PlanningTestTask exists?
        if (!PlanningTestTask.countByPlanningtest(task.instance.planningtest)) {
            task.message = getMsg('fc.planningtesttask.notfound')
            task.error = true
            return task
        }

        // Multiple PlanningTestTasks?  
        if (PlanningTestTask.countByPlanningtest(task.instance.planningtest) > 1) {
            def testInstanceIDs = ['']
            Test.findAllByTask(task.instance).each { testInstance ->
                if (params["selectedTestID${testInstance.id}"] == "on") {
                    testInstanceIDs += testInstance.id
                }
            }
            task.testinstanceids = testInstanceIDs
            return task
        }

        // set single PlanningTestTask to all selected Tests
        PlanningTestTask planningTestTaskInstance = PlanningTestTask.findByPlanningtest(task.instance.planningtest) 
        Test.findAllByTask(task.instance).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                testInstance.planningtesttask = planningTestTaskInstance
                calulateTestLegPlannings(testInstance, testInstance.planningtesttask.route)
                testInstance.save()
            }
        }
        
        return task
    }
    
    //--------------------------------------------------------------------------
    void putplanningtesttaskTask(task,planningtesttask)
    {
        Test.findAllByTask(task.instance).each { testInstance ->
            testInstance.planningtesttask = planningtesttask.instance
            calulateTestLegPlannings(testInstance, testInstance.planningtesttask.route)
            testInstance.save()
        }
    }
    
    //--------------------------------------------------------------------------
    void putplanningresultsTask(task,results)
    {
    	Test.findAllByTask(task.instance,[sort:"viewpos"]).eachWithIndex { testInstance, i ->
    		TestLegPlanning.findAllByTest(testInstance).eachWithIndex { testLegPlanningInstance, j  ->
        		if (results[i].givenValues[j]?.trueHeading && results[i].givenValues[j]?.legTime) {
	    			testLegPlanningInstance.resultTrueHeading = results[i].givenValues[j].trueHeading
	        		testLegPlanningInstance.resultLegTimeInput = results[i].givenValues[j].legTime
	        		calculateLegPlanningInstance(testLegPlanningInstance)
	        		testLegPlanningInstance.save()
        		}
        	}
            testInstance.planningTestGivenTooLate = results[i].givenTooLate
            testInstance.planningTestExitRoomTooLate = results[i].exitRoomTooLate
            calculateTestPenalties(testInstance)
           	testInstance.planningTestComplete = testInstance.planningTestLegComplete && results[i].testComplete
            calculateTestPenalties(testInstance)
    		testInstance.save()
        }
    }

    //--------------------------------------------------------------------------
    Map setplanningtesttaskTask(params)
    {
        def task = getTask(params) 
        if (task.instance) {
            def planningTestTaskInstance = PlanningTestTask.get(params.planningtesttask.id)
            params.testInstanceIDs.each { testID ->
                if (testID) {
                    def testInstance = Test.get(testID)
                    testInstance.planningtesttask = planningTestTaskInstance 
                    calulateTestLegPlannings(testInstance, testInstance.planningtesttask.route)
                    testInstance.save()
                }
            }
            task.message = getMsg('fc.task.selectplanningtesttask.assigned',[planningTestTaskInstance.name()])
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map assignflighttestwindTask(params)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
            task.error = true
            return task
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
            task.message = getMsg('fc.flighttestwind.notfound')
            task.error = true
            return task
        }

        // Multiple FlightTestWinds?  
        if (FlightTestWind.countByFlighttest(task.instance.flighttest) > 1) {
            def testInstanceIDs = ['']
            Test.findAllByTask(task.instance).each { testInstance ->
                if (params["selectedTestID${testInstance.id}"] == "on") {
                    testInstanceIDs += testInstance.id
                }
            }
            task.testinstanceids = testInstanceIDs
            return task
        }

        // set single FlightTestWind to all selected Tests
        def flightTestWindInstance = FlightTestWind.findByFlighttest(task.instance.flighttest)
        Test.findAllByTask(task.instance).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                testInstance.flighttestwind = flightTestWindInstance
                testInstance.save()
            }
        }

        return task
    }
    
    //--------------------------------------------------------------------------
    void putflighttestwindTask(task,flighttestwind)
    {
        Test.findAllByTask(task.instance).each { testInstance ->
            testInstance.flighttestwind = flighttestwind.instance
            testInstance.save()
        }
    }
    
    //--------------------------------------------------------------------------
    void putflightresultsTask(task,results)
    {
        Test.findAllByTask(task.instance,[sort:"viewpos"]).eachWithIndex { testInstance, i ->
        	CoordResult.findAllByTest(testInstance).eachWithIndex { coordResultInstance, j  ->
        		boolean calculate = false
        		if (results[i].givenValues[j]?.cpNotFound) {
        			coordResultInstance.resultCpNotFound = true
        			calculate = true
        		} else if (results[i].givenValues[j]?.cpTime) {
        			coordResultInstance.resultCpTimeInput =  results[i].givenValues[j].cpTime
                    calculate = true
        		}
                if (results[i].givenValues[j]?.procedureTurnNotFlown) {
                    calculate = true
                    coordResultInstance.resultProcedureTurnNotFlown = true
                }
                if (results[i].givenValues[j]?.altitude) {
                    calculate = true
                    coordResultInstance.resultAltitude = results[i].givenValues[j].altitude
                }
                if (calculate) {
                    calculateCoordResultInstance(coordResultInstance,false)
                    coordResultInstance.resultProcedureTurnEntered = true
                    coordResultInstance.save()
                }
            }
            testInstance.flightTestTakeoffMissed = results[i].takeoffMissed
            testInstance.flightTestBadCourseStartLanding = results[i].badCourseStartLanding
            testInstance.flightTestLandingTooLate = results[i].landingTooLate
            testInstance.flightTestGivenTooLate = results[i].givenTooLate
            calculateTestPenalties(testInstance)
            testInstance.flightTestComplete = testInstance.flightTestCheckPointsComplete && results[i].testComplete
            calculateTestPenalties(testInstance)
            testInstance.save()
        }
    }

    //--------------------------------------------------------------------------
    Map setflighttestwindTask(params)
    {
        def task = getTask(params) 
        if (task.instance) {
            def flightTestWindInstance = FlightTestWind.get(params.flighttestwind.id)
            params.testInstanceIDs.each { testID ->
                if (testID) {
                    def testInstance = Test.get(testID)
                    testInstance.flighttestwind = flightTestWindInstance
                    testInstance.timeCalculated = false
                    testInstance.save()
                }
            }
            task.message = getMsg('fc.task.selectflighttestwind.assigned',[flightTestWindInstance.wind.name()])
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map calculatesequenceTask(params)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        /*
        // Have all crews an aircraft?
        def call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (!testInstance.crew.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            return task
        }
        */

        calulateSequence(task.instance)
        
        task.message = getMsg('fc.test.sequence.calculated')        
        return task
    }
    
    //--------------------------------------------------------------------------
    Map moveupTask(params)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        def borderreached = false
        def notmovable = false
        def off2on = false
        def on2off = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                if (testInstance.viewpos == 0) {
                    borderreached = true
                }
                if (off2on && on2off) {
                    notmovable = true
                }
                off2on = true
            } else {
                if (off2on) {
                    on2off = true
                }
            }
        }
        if (borderreached) {
               //task.message = getMsg('fc.test.moveborderreached')
               task.borderreached = true
            return task
        }
        if (notmovable) {
            task.message = getMsg('fc.test.notmovable')
            task.error = true
            return task
        }
        
        def movenum = 0
        def movefirstpos = -1
        def selectedTestIDs = [selectedTestID:""]
        borderreached = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                testInstance.viewpos--
                testInstance.timeCalculated = false
                testInstance.save()
                selectedTestIDs["selectedTestID${testInstance.id}"] = "on"
                if (testInstance.viewpos == 0) {
                    borderreached = true
                }
                movenum++
                if (movefirstpos == -1 || testInstance.viewpos < movefirstpos) {
                    movefirstpos = testInstance.viewpos
                }
            }
        }
        Test.findAllByTask(task.instance).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] != "on") {
                if (testInstance.viewpos >= movefirstpos && testInstance.viewpos < movefirstpos + movenum) {
                    testInstance.viewpos += movenum
                    testInstance.timeCalculated = false
                    testInstance.save()
                }
            }
        }

        if (!borderreached) {
            task.selectedtestids = selectedTestIDs
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map movedownTask(params)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        def borderreached = false
        def notmovable = false
        def off2on = false
        def on2off = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                if (testInstance.viewpos + 1 == Crew.countByContest(task.instance.contest)) {
                    borderreached = true
                }
                if (off2on && on2off) {
                    notmovable = true
                }
                off2on = true
            } else {
                if (off2on) {
                    on2off = true
                }
            }
        }
        if (borderreached) {
               //task.message = getMsg('fc.test.moveborderreached')
            task.borderreached = true
            return task
        }
        if (notmovable) {
            task.message = getMsg('fc.test.notmovable')
            task.error = true
            return task
        }
        
        def movenum = 0
        def movefirstpos = -1
        def selectedTestIDs = [selectedTestID:""]
        borderreached = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                testInstance.viewpos++
                testInstance.timeCalculated = false
                testInstance.save()
                selectedTestIDs["selectedTestID${testInstance.id}"] = "on"
                   if (testInstance.viewpos + 1 == Crew.countByContest(task.instance.contest)) {
                       borderreached = true
                   }
                movenum++
                if (movefirstpos == -1 || testInstance.viewpos < movefirstpos) {
                    movefirstpos = testInstance.viewpos
                }
            }
        }
        Test.findAllByTask(task.instance).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] != "on") {
                if (testInstance.viewpos >= movefirstpos && testInstance.viewpos < movefirstpos + movenum) {
                    testInstance.viewpos -= movenum
                    testInstance.timeCalculated = false
                    testInstance.save()
                }
            }
        }

        if (!borderreached) {
            task.selectedtestids = selectedTestIDs
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map calculatetimetableTask(params)
    {
        println "calculatetimetableTask"
        
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
               task.error = true
            return task
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
            task.message = getMsg('fc.flighttestwind.notfound')
               task.error = true
            return task
        }
        
        // FlightTestWind assigned to all crews?
        def call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (!testInstance.flighttestwind) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.flighttestwind.notassigned')
            task.error = true
            return task
        }

        /*
        // Have all crews an aircraft?
        call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (!testInstance.crew.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            return task
        }
        */

        calulateTestLegFlights(task.instance)
        calulateTimetable(task.instance)
        calulateCoordResults(task.instance)
        
        task.message = getMsg('fc.test.timetable.calculated')        
        return task
    }
    
    //--------------------------------------------------------------------------
    Map timeaddTask(params)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        def selectedTestIDs = [selectedTestID:""]
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                addTestingTime(task.instance,testInstance)
                selectedTestIDs["selectedTestID${testInstance.id}"] = "on"
            }
        }
        task.selectedtestids = selectedTestIDs
        return task
    }
    
    //--------------------------------------------------------------------------
    Map timesubtractTask(params)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        def selectedTestIDs = [selectedTestID:""]
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                subtractTestingTime(task.instance,testInstance)
                selectedTestIDs["selectedTestID${testInstance.id}"] = "on"
            }
        }
        task.selectedtestids = selectedTestIDs
           return task
    }
    
    //--------------------------------------------------------------------------
    Map printtimetableTask(params,printparams)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
               task.error = true
            return task
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
            task.message = getMsg('fc.flighttestwind.notfound')
               task.error = true
            return task
        }
        
        // FlightTestWind assigned to all crews?
        def call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (!testInstance.flighttestwind) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.flighttestwind.notassigned')
            task.error = true
            return task
        }

        // Have all crews an aircraft?
        call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (!testInstance.crew.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            return task
        }

        // Timetable calculated?  
        call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (!testInstance.timeCalculated) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.test.timetable.newcalculate')
            task.error = true
            return task
        }        
        
        // Warnings?  
        call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (testInstance.arrivalTimeWarning || testInstance.takeoffTimeWarning) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.test.flightplan.resolvewarnings')
            task.error = true
            return task
        }        
        
        // Print timetable
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            def url = "${printparams.baseuri}/task/timetableprintable/${task.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            task.content = content 
        }
        catch (Throwable e) {
            task.message = getMsg('fc.test.timetable.printerror',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printflightplansTask(params,printparams)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
            task.error = true
            return task
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
            task.message = getMsg('fc.flighttestwind.notfound')
            task.error = true
            return task
        }
        
        // FlightTestWind assigned to all crews?
        def call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (!testInstance.flighttestwind) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.flighttestwind.notassigned')
            task.error = true
            return task
        }

        // Have all crews an aircraft?
        call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (!testInstance.crew.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            return task
        }

        // Timetable calculated?  
        call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (!testInstance.timeCalculated) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.test.timetable.newcalculate')
            task.error = true
            return task
        }        
        
        // Warnings?  
        call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (testInstance.arrivalTimeWarning || testInstance.takeoffTimeWarning) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.test.flightplan.resolvewarnings')
            task.error = true
            return task
        }        
        
        // Print flightplans
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { testInstance ->
                def url = "${printparams.baseuri}/test/flightplanprintable/${testInstance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
                println "Print: $url"
                renderer.setDocument(url)
                renderer.layout()
                if (first_pdf) {
                    renderer.createPDF(content,false)
                    first_pdf = false
                } else {
                    renderer.writeNextDocument(15)
                }
            }
            renderer.finishPDF()

            task.content = content 
        }
        catch (Throwable e) {
            task.message = getMsg('fc.test.flightplan.printerror',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printplanningtasksTask(params,printparams)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // PlanningTest exists?
        if (!task.instance.planningtest) {
            task.message = getMsg('fc.planningtest.notfound')
            task.error = true
            return task
        }

        // PlanningTestTask exists?
        if (!PlanningTestTask.countByPlanningtest(task.instance.planningtest)) {
            task.message = getMsg('fc.planningtesttask.notfound')
               task.error = true
            return task
        }

        // PlanningTestTask assigned to all crews?
        def call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (!testInstance.planningtesttask) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.planningtesttask.notassigned')
            task.error = true
            return task
        }

        // Print PlanningTasks
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { testInstance ->
                def url = "${printparams.baseuri}/test/planningtaskprintable/${testInstance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}&results=no"
                println "Print: $url"
                renderer.setDocument(url)
                renderer.layout()
                if (first_pdf) {
                    renderer.createPDF(content,false)
                    first_pdf = false
                } else {
                    renderer.writeNextDocument(15)
                }
            }
            renderer.finishPDF()
            task.content = content
        }
        catch (Throwable e) {
            task.message = getMsg('fc.planningtesttask.printerror',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map calculatepositionsTask(params)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // calulate position
        int actPenalty = -1
        int maxPosition = Test.countByTask(task.instance)
        for (int actPosition = 1; actPosition <= maxPosition; actPosition++) {
            
            // search lowest penalty
            int minPenalty = 100000
            Test.findAllByTask(task.instance).each { testInstance ->
                if (testInstance.testPenalties > actPenalty) {
                    if (testInstance.testPenalties < minPenalty) {
                        minPenalty = testInstance.testPenalties 
                    }
                }
            }
            actPenalty = minPenalty 
            
            // set position
            int setPositionNum = -1
            Test.findAllByTask(task.instance).each { testInstance ->
                if (testInstance.testPenalties == actPenalty) {
                    testInstance.positionContestDay = actPosition
                    testInstance.save()
                    setPositionNum++
                }
            }
            
            if (setPositionNum > 0) {
                actPosition += setPositionNum
            }
        }
        
        task.message = getMsg('fc.test.results.positions.calculated')        
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printresultsTask(params,printparams)
    {
        def task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // Positions calculated?  
        boolean call_return = false
        Test.findAllByTask(task.instance).each { testInstance ->
            if (!testInstance.positionContestDay) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.test.results.positions.newcalculate')
            task.error = true
            return task
        }        
        
        // Print positions
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            def url = "${printparams.baseuri}/task/positionsprintable/${task.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            task.content = content 
        }
        catch (Throwable e) {
            task.message = getMsg('fc.test.timetable.printerror',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map getRoute(params)
    {
        def routeInstance = Route.get(params.id)

        if(!routeInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
        
        return ['instance':routeInstance]
    }
    
    //--------------------------------------------------------------------------
    Map updateRoute(params)
    {
        def routeInstance = Route.get(params.id)
        
        if (routeInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(routeInstance.version > version) {
                    routeInstance.errors.rejectValue("version", "route.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':routeInstance]
                }
            }
            
            routeInstance.properties = params
            if(!routeInstance.hasErrors() && routeInstance.save()) {
                return ['instance':routeInstance,'saved':true,'message':getMsg('fc.updated',["${routeInstance.name()}"])]
            } else {
                return ['instance':routeInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createRoute(params)
    {
        def routeInstance = new Route()
        routeInstance.properties = params
        return ['instance':routeInstance]
    }
    
    //--------------------------------------------------------------------------
    Map saveRoute(params,contestInstance)
    {
        def routeInstance = new Route(params)
        
        routeInstance.contest = contestInstance
        routeInstance.idTitle = Route.countByContest(contestInstance) + 1
        
        if(!routeInstance.hasErrors() && routeInstance.save()) {
            return ['instance':routeInstance,'saved':true,'message':getMsg('fc.created',["${routeInstance.name()}"])]
        } else {
            return ['instance':routeInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteRoute(params)
    {
        def routeInstance = Route.get(params.id)
        def contestInstance = routeInstance.contest
        
        if (routeInstance) {
            try {
                routeInstance.delete()
                
                Route.findAllByContest(contestInstance).eachWithIndex { routeInstance2, index  -> 
                    routeInstance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${routeInstance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.route'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map printRoute(params,printparams)
    {
        def route = getRoute(params)
        if (!route.instance) {
            return route
        }
        
        // Print route
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            def url = "${printparams.baseuri}/route/showprintable/${route.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            route.content = content 
        }
        catch (Throwable e) {
            route.message = getMsg('fc.route.printerror',["$e"])
            route.error = true
        }
        return route
    }
    
    //--------------------------------------------------------------------------
    Map printRoutes(params,printparams)
    {
        def routes = [:]

        // Print routes
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Route.findAllByContest(printparams.contest).each { routeInstance ->
	            def url = "${printparams.baseuri}/route/showprintable/${routeInstance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
	            println "Print: $url"
	            renderer.setDocument(url)
	            renderer.layout()
                if (first_pdf) {
                	renderer.createPDF(content,false)
                    first_pdf = false
                } else {
                    renderer.writeNextDocument(15)
                }
	            routes.found = true
            }
            renderer.finishPDF()
            
            routes.content = content 
        }
        catch (Throwable e) {
        	routes.message = getMsg('fc.route.printerror',["$e"])
            routes.error = true
        }
        return routes
    }
    
    //--------------------------------------------------------------------------
    Map caculateroutelegsRoute(params)
    {
        def routeInstance = Route.get(params.id)
        
        if (routeInstance) {
        	calculateRouteLegs(routeInstance)
            return ['instance':routeInstance,'calculated':true,'message':getMsg('fc.routeleg.calculated')]
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map existAnyAflosRoute()
    {
    	if (AflosRouteNames.count() < 2) {
            return ['error':true,'message':getMsg('fc.route.import.notfound')]
    	}
    	return [:]
    }
    
    //--------------------------------------------------------------------------
    Map importAflosRoute(params,contestInstance)
    {
        def aflosRouteNamesInstance = AflosRouteNames.get(params.aflosroutenames.id)
    	if (aflosRouteNamesInstance) {
    	
    		def routeInstance = new Route()
	        
	        routeInstance.contest = contestInstance
	        routeInstance.idTitle = Route.countByContest(contestInstance) + 1
	        routeInstance.title = aflosRouteNamesInstance.name
	        routeInstance.mark = aflosRouteNamesInstance.name
	        
	        if(!routeInstance.hasErrors() && routeInstance.save()) {
                def aflosRouteDefsInstances = AflosRouteDefs.findAllByRoutename(aflosRouteNamesInstance)
                def lastCoordRouteInstance
                def lastCoordRouteTestInstance
                def tpNum = 0
                def tcNum = 0
                aflosRouteDefsInstances.each { aflosRouteDefsInstance ->
                	def coordRouteInstance = new CoordRoute()
                    
                    // set latitude
                    aflosRouteDefsInstance.latitude.split().eachWithIndex{ latValue, i ->
                		switch(i) {
                			case 0: 
                				coordRouteInstance.latDirection = latValue
                				break
                			case 1:
                				coordRouteInstance.latGrad = latValue.toInteger()
                				break
                			case 2:
                				coordRouteInstance.latMinute = latValue.replace(',','.').toBigDecimal()
                				break
                		}
                	}
                    
                    // set longitude
                    aflosRouteDefsInstance.longitude.split().eachWithIndex{ lonValue, i ->
                        switch(i) {
                            case 0: 
                                coordRouteInstance.lonDirection = lonValue
                                break
                            case 1:
                                coordRouteInstance.lonGrad = lonValue.toInteger()
                                break
                            case 2:
                                coordRouteInstance.lonMinute = lonValue.replace(',','.').toBigDecimal()
                                break
                        }
                    }
    
                    // set type and title
                    coordRouteInstance.type = CoordType.UNKNOWN
                    CoordType.each { type ->
                    	if (coordRouteInstance.type == CoordType.UNKNOWN) { // Typ der Koordinate noch nicht ermittelt
	                    	if (type != CoordType.UNKNOWN && aflosRouteDefsInstance.mark && aflosRouteDefsInstance.mark.startsWith(type.aflosMark)) {  
	                    		if (type.aflosGateWidth == 0 || (type.aflosGateWidth == aflosRouteDefsInstance.gatewidth)) { // Übereinstimmung AFLOS-Kooordinate mit Typ
	                    			switch (type) {
	                    				case CoordType.SECRET:
	                    					if (params.aflosroutenames.secretcoordrouteidentification == SecretCoordRouteIdentification.NONE.toString()) {
	                    						coordRouteInstance.type = CoordType.TP 
	                    					} else {
	                    						coordRouteInstance.type = CoordType.SECRET 
	                    					}
                                            break
	                    				case CoordType.TP:
                                            coordRouteInstance.type = CoordType.TP 
                                            break
                                        default:
                                        	coordRouteInstance.type = type
                                        	break
	                    			}

	                    			switch (coordRouteInstance.type) {
		                    			case CoordType.TP:
		                    				tpNum++
		                    				coordRouteInstance.titleNumber = tpNum
		                    				break
		                    			case CoordType.SECRET:
		                    				tcNum++
	                                        coordRouteInstance.titleNumber = tcNum
		                    				break
		                    		}
	                    		}
	                    	}
                    	}
                    }
                    
                    // set other
                    coordRouteInstance.altitude = aflosRouteDefsInstance.altitude.toInteger()
                    coordRouteInstance.mark = aflosRouteDefsInstance.mark
                    coordRouteInstance.gatewidth = aflosRouteDefsInstance.gatewidth
                    coordRouteInstance.route = routeInstance
                    
                    coordRouteInstance.save()
                    
                    newLeg(coordRouteInstance.route, coordRouteInstance, lastCoordRouteInstance, lastCoordRouteTestInstance, null, null)
                    lastCoordRouteInstance = coordRouteInstance
                    switch (coordRouteInstance.type) {
	                    case CoordType.SP:
	                    case CoordType.TP:
	                    case CoordType.FP:
	                        lastCoordRouteTestInstance = coordRouteInstance
	                        break
	                }
                }
                calculateSecretLegRatio(routeInstance)
                return ['instance':routeInstance,'saved':true,'message':getMsg('fc.imported',["${routeInstance.name()}"])]
	        } else {
	            return ['error':true,'message':getMsg('fc.notimported',["${aflosRouteNamesInstance.name}"])]
	        }
        } else {
            return ['error':true,'message':getMsg('fc.notimported',["${params.aflosroutenames.id}"])]
    	}
    }
    
    //--------------------------------------------------------------------------
    Map existAnyAflosCrew()
    {
    	if (AflosCrewNames.count() > 0) {
            return [:]
        }
        return ['error':true,'message':getMsg('fc.aflos.points.notfound')]
    }
    
    //--------------------------------------------------------------------------
    Map importAflosResults(params,contestInstance) // TODO
    {
        Test testInstance = Test.get(params.id)

        AflosCrewNames aflosCrewNamesInstance = AflosCrewNames.findByStartnumAndPointsNotEqual(params.afloscrewnames.startnum,0)
        if (!aflosCrewNamesInstance) {
        	return ['error':true,'message':getMsg('fc.aflos.points.crewnotfound',[params.afloscrewnames.startnum])]
        }
        AflosRouteNames aflosRouteNamesInstance = AflosRouteNames.findByName(testInstance.flighttestwind.flighttest.route.mark)
        if (!aflosRouteNamesInstance) {
        	return ['error':true,'message':getMsg('fc.aflos.points.routenotfound',[testInstance.flighttestwind.flighttest.route.mark])]
        }
        
        println "importAflosResults $testInstance.crew.name (AFLOS crew: ${aflosCrewNamesInstance.viewName()}, AFLOS route: $aflosRouteNamesInstance.name)"

        AflosErrors aflosError= AflosErrors.findByStartnumAndRoutename(aflosCrewNamesInstance.startnum,aflosRouteNamesInstance)
        if (!aflosError) {
        	return ['error':true,'message':getMsg('fc.aflos.points.notcomplete',[aflosCrewNamesInstance.viewName()])]
        }
        if (aflosError.mark.contains("Check Error")) {
        	return ['error':true,'message':getMsg('fc.aflos.points.errors',[aflosCrewNamesInstance.viewName()])]
        }

        try {
	        // Import AflosCheckPoints
            int checkPointErrors = 0
            int heightErrors = 0
	        CoordResult.findAllByTest(testInstance).each { coordResultInstance ->
	        	boolean found = false
	        	AflosCheckPoints.findAllByStartnumAndRoutename(aflosCrewNamesInstance.startnum,aflosRouteNamesInstance).each { aflosCheckPointsInstance ->
	        		if (aflosCheckPointsInstance.mark == "P${coordResultInstance.mark}") {
	        			// reset results
	        			coordResultInstance.ResetResults()
	        			
	        			// read utc '09h 36min 05,000sec'
	        			coordResultInstance.resultCpTimeInput = FcMath.ConvertAFLOSTime(aflosCheckPointsInstance.utc) 
	
	        			// read latitude '51° 26,9035' N'
	        			coordResultInstance.resultLatitude = FcMath.ConvertAFLOSCoordValue(aflosCheckPointsInstance.latitude)
 
	        			// read longitude '013° 51,7278' E'
	                    coordResultInstance.resultLongitude = FcMath.ConvertAFLOSCoordValue(aflosCheckPointsInstance.longitude)
 
	        			// read altitude '01992 ft'
	        			coordResultInstance.resultAltitude = aflosCheckPointsInstance.altitude.split()[0].toInteger()
	        			
	        			// calculate results
                        if (coordResultInstance.planProcedureTurn) {
                            coordResultInstance.resultProcedureTurnEntered = true
                        }
	        			calculateCoordResultInstance(coordResultInstance,true)
	        			
                        // calculate verify values 
                        if (coordResultInstance.resultMinAltitudeMissed) {
                            heightErrors++
                        }

	        			// save results
	                    coordResultInstance.save()
	                    found = true
	                    
	                    // log
	                    if (coordResultInstance.planProcedureTurn) {
	                        println "Procedure turn"
	                    }
	                    println "Found AflosCheckPoint $aflosCheckPointsInstance.mark UTC: $coordResultInstance.resultCpTimeInput Local: ${coordResultInstance.resultCpTime.format('HH:mm:ss')}"
	        		}
	        	}
	        	if (!found) {
	        		// reset results
                    coordResultInstance.ResetResults()
                    
	        		// calculate results
                    coordResultInstance.resultCpNotFound = true
                    if (coordResultInstance.planProcedureTurn) {
                        coordResultInstance.resultProcedureTurnEntered = true
                    }
                    calculateCoordResultInstance(coordResultInstance,true)
                    
                    // calculate verify values
                    checkPointErrors++
                    
                    // save results
	                coordResultInstance.save()
                    
	                // log
	                if (coordResultInstance.planProcedureTurn) {
                        println "Procedure turn"
                    }
                    println "Not found AflosCheckPoint $coordResultInstance.mark"
	        	}
	    	}
	    	
	    	// Import AflosErrorPoints
	    	int badCourseNum = 0
	    	int courseErrors = 0
	    	String badCourseStartTimeUTC
	    	AflosErrorPoints.findAllByStartnumAndRoutename(aflosCrewNamesInstance.startnum,aflosRouteNamesInstance).each { aflosErrorPointsInstance ->
	    		if (badCourseNum == 0) {
	    			if (aflosErrorPointsInstance.mark.contains("-Bad Course")) {
		    			badCourseNum = 1
		    			badCourseStartTimeUTC = aflosErrorPointsInstance.utc
		    		}
	    		} else {
	    			if (!aflosErrorPointsInstance.mark.trim()) {
	    				badCourseNum++
	    			} else {
	    				// process error point
	    				if (processAflosErrorPointBadCourse(testInstance,badCourseNum,badCourseStartTimeUTC)) {
	    					courseErrors++
	    				}
	    				
	    				// search for next error point 
	    				if (aflosErrorPointsInstance.mark.contains("-Bad Course")) {
	    					badCourseNum = 1
	                        badCourseStartTimeUTC = aflosErrorPointsInstance.utc
	    				} else {
	    					badCourseNum = 0
	    					badCourseStartTimeUTC = ""
	    				}
	    			}
	    		}
	    		if (aflosErrorPointsInstance.mark.contains("-Bad Turn")) {
	    			processAflosErrorPointBadTurn(testInstance,aflosErrorPointsInstance.utc)
	    		}
	    	}
	        if (badCourseNum > 0) {
	            // process error point
	        	if (processAflosErrorPointBadCourse(testInstance,badCourseNum,badCourseStartTimeUTC)) {
	        		courseErrors++
	        	}
	        }
	        
	    	// Penalties berechnen
	        calculateTestPenalties(testInstance)
	        testInstance.save()
	        
	        // save imported crew
	        testInstance.crew.mark = aflosCrewNamesInstance.viewName()
	        testInstance.crew.save()
	        
	        if (aflosError.mark == "Flight O.K.") {
	        	return ['saved':true,'message':getMsg('fc.aflos.points.imported',[aflosCrewNamesInstance.viewName()])]
	        } else {
	        	if (aflosError.dropOutErrors == 0 && 
	        		checkPointErrors == aflosError.checkPointErrors && 
	        		heightErrors == aflosError.heightErrors && 
	        		courseErrors == aflosError.courseErrors)
	        	{
	        		return ['saved':true,'message':getMsg('fc.aflos.points.imported.naverrors',[aflosCrewNamesInstance.viewName()])]
	        	} else {
	        		if (checkPointErrors != aflosError.checkPointErrors) {
	        			println "Evaluation error: $checkPointErrors bad checkpoints <> $aflosError.checkPointErrors bad AFLOS checkpoints"
	        		}
                    if (heightErrors != aflosError.heightErrors) {
                        println "Evaluation error: $heightErrors bad heights <> $aflosError.heightErrors bad AFLOS heights"
                    }
                    if (courseErrors != aflosError.courseErrors) {
                        println "Evaluation error: $courseErrors bad courses <> $aflosError.courseErrors bad AFLOS courses"
                    }
	        		return ['error':true,'saved':true,'message':getMsg('fc.aflos.points.imported.naverrors.differences',[aflosCrewNamesInstance.viewName()])]
	        	}
	        }
    	}
        catch (Exception e) {
        	return ['error':true,'message':getMsg('fc.notimported.msg',[aflosCrewNamesInstance.viewName(),e.getMessage()])]
        }
    }

    //--------------------------------------------------------------------------
    boolean processAflosErrorPointBadCourse(Test testInstance, int badCourseNum, String badCourseStartTimeUTC) // TODO
    {
    	boolean courseError = false
    	Contest contestInstance = testInstance.task.contest
    	
        Date badCourseStartTime = Date.parse("HH:mm:ss",FcMath.ConvertAFLOSTime(badCourseStartTimeUTC))
        GregorianCalendar badCourseStartCalendar = new GregorianCalendar()
        badCourseStartCalendar.setTime(badCourseStartTime)
        
        Date timeZoneDate = Date.parse("HH:mm",contestInstance.timeZone)
        GregorianCalendar timeZoneCalendar = new GregorianCalendar()
        timeZoneCalendar.setTime(timeZoneDate)
        
        badCourseStartCalendar.add(Calendar.HOUR, timeZoneCalendar.get(Calendar.HOUR))
        badCourseStartCalendar.add(Calendar.MINUTE, timeZoneCalendar.get(Calendar.MINUTE))
        
        GregorianCalendar badCourseEndCalendar = badCourseStartCalendar.clone()
        badCourseEndCalendar.add(Calendar.SECOND, badCourseNum)
            
        println "Found AflosErrorPointBadCourse ($badCourseNum, ${badCourseStartCalendar.getTime().format('HH:mm:ss')}...${badCourseEndCalendar.getTime().format('HH:mm:ss')}): "

        if (badCourseNum > contestInstance.flightTestBadCourseCorrectSecond) {
        	courseError = true
    		int lastIndex = 0
    		Date lastTime
    		boolean calculatePenalties = false
            CoordResult.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { coordResultInstance, i ->
            	if (lastIndex != 0 && badCourseEndCalendar.getTime() < coordResultInstance.resultCpTime) {
        			// process
        			lastIndex = i
        			
        			if (badCourseEndCalendar.getTime() > lastTime) {
        				coordResultInstance.resultBadCourseNum++
        				coordResultInstance.save()
        				calculatePenalties = true
        				println "  $coordResultInstance.mark relevant."
        			} else {
        				println "  $coordResultInstance.mark not relevant."
        			}
            	}

            	if (badCourseStartCalendar.getTime() > coordResultInstance.resultCpTime) {
                    lastIndex = i
                    lastTime = coordResultInstance.resultCpTime
                } else {
                    lastIndex = 0
                }
            }
    		
    	} else {
    		println "  Not relevant (number)."
    	}
        return courseError
    }
    
    //--------------------------------------------------------------------------
    void processAflosErrorPointBadTurn(Test testInstance, String badTurnTimeUTC) // TODO
    {
        Contest contestInstance = testInstance.task.contest
        
        Date badTurnTime = Date.parse("HH:mm:ss",FcMath.ConvertAFLOSTime(badTurnTimeUTC))
        GregorianCalendar badTurnCalendar = new GregorianCalendar()
        badTurnCalendar.setTime(badTurnTime)
        
        Date timeZoneDate = Date.parse("HH:mm",contestInstance.timeZone)
        GregorianCalendar timeZoneCalendar = new GregorianCalendar()
        timeZoneCalendar.setTime(timeZoneDate)
        
        badTurnCalendar.add(Calendar.HOUR, timeZoneCalendar.get(Calendar.HOUR))
        badTurnCalendar.add(Calendar.MINUTE, timeZoneCalendar.get(Calendar.MINUTE))
        
        print "Found AflosErrorPointBadTurn (${badTurnCalendar.getTime().format('HH:mm:ss')}): "

        int lastIndex = 0
        Date lastTime
        boolean calculatePenalties = false
        CoordResult.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { coordResultInstance, i ->
            if (lastIndex != 0 && coordResultInstance.planProcedureTurn && badTurnCalendar.getTime() < coordResultInstance.resultCpTime) {
                // process
                lastIndex = i
                
                if (badTurnCalendar.getTime() > lastTime) {
                    coordResultInstance.resultProcedureTurnNotFlown = true
                    coordResultInstance.resultProcedureTurnEntered = true
                    coordResultInstance.save()
                    calculatePenalties = true
                    println "  $coordResultInstance.mark relevant."
                } else {
                    println "  $coordResultInstance.mark not relevant."
                }
            }

            if (badTurnCalendar.getTime() > coordResultInstance.resultCpTime) {
                lastIndex = i
                lastTime = coordResultInstance.resultCpTime
            } else {
                lastIndex = 0
            }
            
        }
    }
    
    //--------------------------------------------------------------------------
    Map getCoordRoute(params)
    {
        def coordRouteInstance = CoordRoute.get(params.id)

        if (!coordRouteInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
        
        return ['instance':coordRouteInstance]
    }

    //--------------------------------------------------------------------------
    Map updateCoordRoute(params)
    {
        def coordRouteInstance = CoordRoute.get(params.id)
        
        if (coordRouteInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(coordRouteInstance.version > version) {
                    coordRouteInstance.errors.rejectValue("version", "coordRoute.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordRouteInstance]
                }
            }
            
            coordRouteInstance.properties = params
            
            if(!coordRouteInstance.hasErrors() && coordRouteInstance.save()) {
                calculateSecretLegRatio(coordRouteInstance.route)
                calculateRouteLegs(coordRouteInstance.route)
                return ['instance':coordRouteInstance,'saved':true,'message':getMsg('fc.updated',["${coordRouteInstance.name()}"])]
            } else {
                return ['instance':coordRouteInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createCoordRoute(params)
    {
    	CoordRoute lastCoordRouteInstance = CoordRoute.findByRoute(Route.get(params.routeid),[sort:"id", order:"desc"])
        CoordRoute coordRouteInstance = new CoordRoute()
    
        coordRouteInstance.properties = params
        coordRouteInstance.altitude = 1
    
        if (params.secret) {
            coordRouteInstance.type = CoordType.SECRET
            if (lastCoordRouteInstance) {
                switch (lastCoordRouteInstance.type) {
                	case CoordType.SP:
                    case CoordType.TP:
                    case CoordType.SECRET:
                    	coordRouteInstance.titleNumber = findNextTitleNumber(lastCoordRouteInstance.route,CoordType.SECRET)
                    	break
                    default:
                        return ['error':true,'message':getMsg('fc.coordroute.addsecret.notallowed')]
                }
            } else {
                return ['error':true,'message':getMsg('fc.coordroute.addsecret.notallowed')]
            }
        } else {
	        if (lastCoordRouteInstance) {
	        	switch (lastCoordRouteInstance.type) {
	        		case CoordType.TO:
	        			coordRouteInstance.type = CoordType.SP
	        			break
	        		case CoordType.SP:
	                    coordRouteInstance.type = CoordType.TP
	                    break
	        		case CoordType.TP:
                    case CoordType.SECRET:
	                    coordRouteInstance.type = CoordType.TP
	                    coordRouteInstance.titleNumber = findNextTitleNumber(lastCoordRouteInstance.route,CoordType.TP) 
	                    break
	                case CoordType.FP:
	                    coordRouteInstance.type = CoordType.LDG
	                    break
	                case CoordType.LDG:
	                	return ['error':true,'message':getMsg('fc.coordroute.add.notallowed')]
	        	}
	        }
        }
        
        if (lastCoordRouteInstance) {
            coordRouteInstance.latGrad = lastCoordRouteInstance.latGrad
            coordRouteInstance.latMinute = lastCoordRouteInstance.latMinute
            coordRouteInstance.latDirection = lastCoordRouteInstance.latDirection
            coordRouteInstance.lonGrad = lastCoordRouteInstance.lonGrad
            coordRouteInstance.lonMinute = lastCoordRouteInstance.lonMinute
            coordRouteInstance.lonDirection = lastCoordRouteInstance.lonDirection
        }
        
        return ['instance':coordRouteInstance]
    }

    
    //--------------------------------------------------------------------------
    int findNextTitleNumber(Route route, CoordType type)
    {
    	int titleNumber = 0
    	CoordRoute.findAllByRoute(route,[sort:"id", order:"desc"]).each { coordRouteInstance ->
        	if (!titleNumber) {
	        	if (coordRouteInstance.type == type) {
	        		titleNumber = coordRouteInstance.titleNumber
	        	}
        	}
        }
    	return titleNumber + 1
    }
    
    //--------------------------------------------------------------------------
    Map saveCoordRoute(params)
    {
    	def lastCoordRouteInstance = CoordRoute.findByRoute(Route.get(params.routeid),[sort:"id", order:"desc"])
    	BigDecimal lastMapMeasureDistance = null
    	BigDecimal lastMapMeasureTrueTrack = null
        
    	def lastCoordRouteTestInstance 
    	CoordRoute.findAllByRoute(Route.get(params.routeid),[sort:"id", order:"desc"]).each { coordRouteInstance ->
			if (!lastCoordRouteTestInstance) {
		        switch (coordRouteInstance.type) {
			        case CoordType.SP:
			        case CoordType.TP:
			        case CoordType.FP:
			        	lastCoordRouteTestInstance = coordRouteInstance
			            break
			        default:
		        		lastMapMeasureDistance = addMapDistance(lastMapMeasureDistance,coordRouteInstance.mapmeasuredistance)
		                break
			    }
        	}
        }
        
    	def coordRouteInstance = new CoordRoute(params)
        
        coordRouteInstance.route = Route.get(params.routeid)
        
        if(!coordRouteInstance.hasErrors() && coordRouteInstance.save()) {
            calculateSecretLegRatio(coordRouteInstance.route)
       		lastMapMeasureDistance = addMapDistance(lastMapMeasureDistance,coordRouteInstance.mapmeasuredistance)
        	lastMapMeasureTrueTrack = coordRouteInstance.mapmeasuretruetrack
        	newLeg(coordRouteInstance.route, coordRouteInstance, lastCoordRouteInstance, lastCoordRouteTestInstance, lastMapMeasureDistance, lastMapMeasureTrueTrack)
            return ['instance':coordRouteInstance,'saved':true,'message':getMsg('fc.created',["${coordRouteInstance.name()}"])]
        } else {
            return ['instance':coordRouteInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteCoordRoute(params)
    {
        def coordRouteInstance = CoordRoute.get(params.id)
        
        if (coordRouteInstance) {
            try {
                def routeInstance = coordRouteInstance.route
                removeAllRouteLegs(routeInstance)
                coordRouteInstance.delete()
                calculateRouteLegs(routeInstance)
                return ['deleted':true,'message':getMsg('fc.deleted',["${coordRouteInstance.name()}"]),'routeid':routeInstance.id]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.coordroute'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getRouteLegCoord(params)
    {
        def routeLegCoordInstance = RouteLegCoord.get(params.id)

        if (!routeLegCoordInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.routeleg'),params.id])]
        }
        
        return ['instance':routeLegCoordInstance]
    }

    //--------------------------------------------------------------------------
    Map updateRouteLegCoord(params)
    {
        def routeLegCoordInstance = RouteLegCoord.get(params.id)
        
        if (routeLegCoordInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(routeLegCoordInstance.version > version) {
                    routeLegCoordInstance.errors.rejectValue("version", "routeLegCoord.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':routeLegCoordInstance]
                }
            }
            
            routeLegCoordInstance.properties = params

            calculateRouteLegCoordMapDistances(routeLegCoordInstance)
            
            if(!routeLegCoordInstance.hasErrors() && routeLegCoordInstance.save()) {
                return ['instance':routeLegCoordInstance,'saved':true,'message':getMsg('fc.updated',["${routeLegCoordInstance.coordName()}"])]
            } else {
                return ['instance':routeLegCoordInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    void calculateRouteLegCoordMapDistances(routeLegCoordInstance)
    {
    	println "calculateRouteLegCoordMapDistances $routeLegCoordInstance"
    	
    	Contest contestInstance = routeLegCoordInstance.route.contest
    		
    	// calculate mapdistance
    	routeLegCoordInstance.mapdistance = calculateMapDistance(contestInstance,routeLegCoordInstance.mapmeasuredistance)
    	
    	// search coord for routeLegCoordInstance
    	def foundCoordRoute
    	RouteLegCoord.findAllByRoute(routeLegCoordInstance.route).eachWithIndex { routeLeg, i -> 
    		if (routeLeg == routeLegCoordInstance) {
    			CoordRoute.findAllByRoute(routeLegCoordInstance.route).eachWithIndex { coordRouteInstance, j ->  
    				if (i + 1 == j) {
    					foundCoordRoute = coordRouteInstance
    				}
    			}
    		}
    	}

        // save mapdistance to coord
    	if (foundCoordRoute) {
    		foundCoordRoute.mapmeasuredistance = routeLegCoordInstance.mapmeasuredistance
			foundCoordRoute.mapdistance = routeLegCoordInstance.mapdistance
			foundCoordRoute.mapmeasuretruetrack = routeLegCoordInstance.mapmeasuretruetrack
			foundCoordRoute.save()
    	}
        
        // calculate mapdistance in testlegs
        CoordRoute lastCoordRouteInstance
        CoordRoute lastCoordRouteTestInstance
        BigDecimal lastMapMeasureDistance = null
        BigDecimal lastMapMeasureTrueTrack = null
        int testlegpos = 0
        CoordRoute.findAllByRoute(routeLegCoordInstance.route).each { coordRouteInstance ->
       		lastMapMeasureDistance = addMapDistance(lastMapMeasureDistance,coordRouteInstance.mapmeasuredistance)
            lastMapMeasureTrueTrack = coordRouteInstance.mapmeasuretruetrack
            if (lastCoordRouteInstance && lastCoordRouteTestInstance) {
                if ( (lastCoordRouteTestInstance.type == CoordType.SP && coordRouteInstance.type == CoordType.TP) ||
                     (lastCoordRouteTestInstance.type == CoordType.SP && coordRouteInstance.type == CoordType.FP) ||
                     (lastCoordRouteTestInstance.type == CoordType.TP && coordRouteInstance.type == CoordType.TP) ||
                     (lastCoordRouteTestInstance.type == CoordType.TP && coordRouteInstance.type == CoordType.FP) ) 
                {
                	RouteLegTest.findAllByRoute(routeLegCoordInstance.route).eachWithIndex { testLeg, i ->
                		if (i == testlegpos) {
                			testLeg.mapmeasuredistance = lastMapMeasureDistance 
                			testLeg.mapdistance = calculateMapDistance(contestInstance,lastMapMeasureDistance)
                			testLeg.mapmeasuretruetrack = lastMapMeasureTrueTrack
                			testLeg.save()
                		}
                	}
                    testlegpos++
                }
            }
            lastCoordRouteInstance = coordRouteInstance
            switch (coordRouteInstance.type) {
                case CoordType.SP:
                case CoordType.TP:
                case CoordType.FP:
                    lastCoordRouteTestInstance = coordRouteInstance
                    lastMapMeasureDistance = null
                    break
            }
        }
        
    }

    //--------------------------------------------------------------------------
    Map getRouteLegTest(params)
    {
        def routeLegTestInstance = RouteLegTest.get(params.id)

        if (!routeLegTestInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.routeleg'),params.id])]
        }
        
        return ['instance':routeLegTestInstance]
    }

    //--------------------------------------------------------------------------
    Map getCrew(params)
    {
        def crewInstance = Crew.get(params.id)

        if (!crewInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.crew'),params.id])]
        }
        
        return ['instance':crewInstance]
    }

    //--------------------------------------------------------------------------
    Map updateCrew(params)
    {
        def crewInstance = Crew.get(params.id)
        if(crewInstance) {
            
            if (params.version) {
                def version = params.version.toLong()
                if(crewInstance.version > version) {
                    crewInstance.errors.rejectValue("version", "crew.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':crewInstance]
                }
            }

            if(!crewInstance.hasErrors()) {

            	def oldAircraftInstance = crewInstance.aircraft
	            crewInstance.properties = params
	            
	            if (oldAircraftInstance) {
	            	if (crewInstance == oldAircraftInstance.user2) {
	            		oldAircraftInstance.user2 = null
	            	} else if (crewInstance == oldAircraftInstance.user1) {
	            		oldAircraftInstance.user1 = oldAircraftInstance.user2
	            		oldAircraftInstance.user2 = null
	            	}
	                oldAircraftInstance.save()
	            }
	
                if (crewInstance.aircraft) {
	                if (!crewInstance.aircraft.user1) {
		            	crewInstance.aircraft.user1 = crewInstance
		                crewInstance.aircraft.save()
		            } else if (!crewInstance.aircraft.user2) {
		            	crewInstance.aircraft.user2 = crewInstance
		            	crewInstance.aircraft.save()
		            } else {
		            	crewInstance.aircraft = null
		            }
                }
	
                if (crewInstance.save()) {

	                Test.findAllByCrew(crewInstance).each { testInstance ->
	                	testInstance.timeCalculated = false
	                    testInstance.save()
	                }
	
	                return ['instance':crewInstance,'saved':true,'message':getMsg('fc.updated',["${crewInstance.name}"])]
                } else {
                    return ['instance':crewInstance]
                }
            } else {
                return ['instance':crewInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.crew'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createCrew(params)
    {
        def crewInstance = new Crew()
        crewInstance.properties = params
        return ['instance':crewInstance]
    }
    
    //--------------------------------------------------------------------------
    Map saveCrew(params,contestInstance)
    {
        def crewInstance = new Crew(params)
        crewInstance.contest = contestInstance
        if (!crewInstance.hasErrors() && crewInstance.save()) {
            
            if (params.registration) {
                def aircraftInstance = Aircraft.findByRegistrationAndContest(params.registration,contestInstance)
                if (!aircraftInstance) {
                    aircraftInstance = new Aircraft(params)
                    aircraftInstance.contest = crewInstance.contest 
                }
                if (!aircraftInstance.user1) {
                	aircraftInstance.user1 = crewInstance
                } else if (!aircraftInstance.user2) {
                    aircraftInstance.user2 = crewInstance
                } else {
                	aircraftInstance = null
                }
                if (aircraftInstance) {
                    if(!aircraftInstance.hasErrors() && aircraftInstance.save()) {
                        crewInstance.aircraft = aircraftInstance 
                        crewInstance.save()
                    }
                }
            }

            Task.findAllByContest(contestInstance).each { taskInstance ->
                def testInstance = new Test()
                testInstance.crew = crewInstance
                testInstance.viewpos = Crew.countByContest(contestInstance) - 1
                testInstance.task = taskInstance
                testInstance.timeCalculated = false
                testInstance.save()
            }
            String msg
            if (crewInstance.aircraft) {
            	msg = getMsg('fc.crew.withaircraft.created',["${crewInstance.name}", "${crewInstance.aircraft.registration}"])
            } else {
                msg = getMsg('fc.created',["${crewInstance.name}"])
            }

            return ['instance':crewInstance,'saved':true,'message':msg]
        } else {
            return ['instance':crewInstance]
        }
            
    }
    
    //--------------------------------------------------------------------------
    Map deleteCrew(params) 
    {
        def crewInstance = Crew.get(params.id)
        
        if(crewInstance) {
            try {
                
                if (crewInstance.aircraft) {
                	if (crewInstance == crewInstance.aircraft.user2) {
                		crewInstance.aircraft.user2 = null
                	} else {
                        crewInstance.aircraft.user1 = crewInstance.aircraft.user2
                        crewInstance.aircraft.user2 = null
                	}
                    crewInstance.aircraft.save()
                    
                    crewInstance.aircraft = null 
                }
                
                // remove Tests
                Test.findAllByCrew(crewInstance).each { testInstance ->
                    testInstance.delete()
                }
                
                crewInstance.delete()
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${crewInstance.name}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.crew'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.crew'),params.id])]
        }
    }

    //--------------------------------------------------------------------------
    Map setaircraftCrew(crew,aircraft)
    {
        crew.instance.aircraft = aircraft.instance
        def p = [:]
        p.id = crew.instance.id
        return updateCrew(p)
    }
    
    //--------------------------------------------------------------------------
    Map printCrews(params,printparams)
    {
        def crews = [:]

        // Print crews
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            def url = "${printparams.baseuri}/crew/listprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            crews.content = content 
        }
        catch (Throwable e) {
            crews.message = getMsg('fc.crew.printerror',["$e"])
            crews.error = true
        }
        return crews
    }
    
    //--------------------------------------------------------------------------
    Map getTest(params)
    {
        Test testInstance = Test.get(params.id)

        if (!testInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
        
        return ['instance':testInstance]
    }

    //--------------------------------------------------------------------------
    Map printflightplanTest(params,printparams)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print flightplan
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            def url = "${printparams.baseuri}/test/flightplanprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.test.timetable.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printplanningtaskTest(params,printparams,withResult)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print flightplan
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            def url = "${printparams.baseuri}/test/planningtaskprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}&results="
            if (withResult) {
            	url += "yes"
            } else {
                url += "no"
            }
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.test.timetable.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printresultsTest(params,printparams)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print crew results
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            def url = "${printparams.baseuri}/test/resultsprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.test.timetable.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map updateresultsTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
            	test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.properties = params
        
        calculateTestPenalties(test.instance)
        test.instance.positionContestDay = 0
            
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance]
        }
    }

    //--------------------------------------------------------------------------
    Map planningtaskresultscompleteTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.planningTestComplete = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map planningtaskresultsopenTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.planningTestComplete = false
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map planningtaskresultsgiventolateonTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.planningTestGivenTooLate = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map planningtaskresultsgiventolateoffTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.planningTestGivenTooLate = false
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map planningtaskresultsexitroomtolateonTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.planningTestExitRoomTooLate = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map planningtaskresultsexitroomtolateoffTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.planningTestExitRoomTooLate = false
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultscompleteTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.flightTestComplete = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultsopenTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.flightTestComplete = false
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultstakeoffmissedonTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.flightTestTakeoffMissed = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultstakeoffmissedoffTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.flightTestTakeoffMissed = false
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultsbadcoursestartlandingonTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.flightTestBadCourseStartLanding = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultsbadcoursestartlandingoffTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.flightTestBadCourseStartLanding = false
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultslandingtolateonTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.flightTestLandingTooLate = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultslandingtolateoffTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.flightTestLandingTooLate = false
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultsgiventolateonTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.flightTestGivenTooLate = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultsgiventolateoffTest(params)
    {
        def test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            def version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.flightTestGivenTooLate = false
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    void calculateTestPenalties(Test testInstance)
    {
    	print "calculateTestPenalties $testInstance.crew.name..."
    	
    	Contest contestInstance = testInstance.task.contest 
    	
    	// planningTestPenalties
    	testInstance.planningTestLegPenalties = 0
        testInstance.planningTestLegComplete = false
        if (TestLegPlanning.findByTest(testInstance)) {
        	testInstance.planningTestLegComplete = true
        }
    	TestLegPlanning.findAllByTest(testInstance).each { testLegPlanningInstance ->
    		if (testLegPlanningInstance.resultEntered) {
    			testInstance.planningTestLegPenalties += testLegPlanningInstance.penaltyTrueHeading
    			testInstance.planningTestLegPenalties += testLegPlanningInstance.penaltyLegTime
    		} else {
    			testInstance.planningTestLegComplete = false
    		}
    	}
        if (testInstance.planningTestLegPenalties > contestInstance.planningTestMaxPoints) {
            testInstance.planningTestLegPenalties = contestInstance.planningTestMaxPoints
        }
    	testInstance.planningTestPenalties = testInstance.planningTestLegPenalties
    	if (testInstance.planningTestGivenTooLate) {
    		testInstance.planningTestPenalties += contestInstance.planningTestPlanTooLatePoints
    	}
    	if (testInstance.planningTestExitRoomTooLate) {
    		testInstance.planningTestPenalties += contestInstance.planningTestExitRoomTooLatePoints
    	}
    	
    	// flightTestPenalties
    	testInstance.flightTestCheckPointPenalties = 0
    	testInstance.flightTestCheckPointsComplete = false
    	if (CoordResult.findByTest(testInstance)) {
    		testInstance.flightTestCheckPointsComplete = true
    	}
    	CoordResult.findAllByTest(testInstance).each { coordResultInstance ->
    		if (coordResultInstance.resultEntered) {
  				testInstance.flightTestCheckPointPenalties += coordResultInstance.penaltyCoord
    		} else {
    			testInstance.flightTestCheckPointsComplete = false
    		}
    		if (coordResultInstance.planProcedureTurn) {
	    		if (coordResultInstance.resultProcedureTurnEntered) {
		    		if (coordResultInstance.resultProcedureTurnNotFlown) {
		    			testInstance.flightTestCheckPointPenalties += contestInstance.flightTestProcedureTurnNotFlownPoints
		    		}
	    		} else {
	    			testInstance.flightTestCheckPointsComplete = false
	    		}
    		}
    		if (coordResultInstance.resultAltitude && coordResultInstance.resultMinAltitudeMissed) {
    			testInstance.flightTestCheckPointPenalties += contestInstance.flightTestMinAltitudeMissedPoints
    		}
    		if (coordResultInstance.resultBadCourseNum) {
    			testInstance.flightTestCheckPointPenalties += coordResultInstance.resultBadCourseNum * contestInstance.flightTestBadCoursePoints  
    		}
    	}
    	testInstance.flightTestPenalties = testInstance.flightTestCheckPointPenalties
        if (testInstance.flightTestTakeoffMissed) {
            testInstance.flightTestPenalties += contestInstance.flightTestTakeoffMissedPoints
        }
        if (testInstance.flightTestBadCourseStartLanding) {
            testInstance.flightTestPenalties += contestInstance.flightTestBadCourseStartLandingPoints
        }
        if (testInstance.flightTestLandingTooLate) {
            testInstance.flightTestPenalties += contestInstance.flightTestLandingToLatePoints
        }
        if (testInstance.flightTestGivenTooLate) {
            testInstance.flightTestPenalties += contestInstance.flightTestGivenToLatePoints
        }
    	
        // landingTestPenalties
        // TODO
        
        // specialTestPenalties
        // TODO
        
        // testPenalties
    	testInstance.testPenalties = 0
    	if (testInstance.planningTestComplete) {
    		testInstance.testPenalties += testInstance.planningTestPenalties
    	}
    	if (testInstance.flightTestComplete) {
    		testInstance.testPenalties += testInstance.flightTestPenalties
    	}
        if (testInstance.landingTestComplete) {
        	testInstance.testPenalties += testInstance.landingTestPenalties
        }
        if (testInstance.specialTestComplete) {
        	testInstance.testPenalties += testInstance.specialTestPenalties
        }

    	println "  Planning:$testInstance.planningTestPenalties Flight:$testInstance.flightTestPenalties Landing:$testInstance.landingTestPenalties Special:$testInstance.specialTestPenalties Summary:$testInstance.testPenalties"
    }

    //--------------------------------------------------------------------------
    Map getTestLegFlight(params)
    {
        def testLegFlightInstance = TestLegFlight.get(params.id)

        if (!testLegFlightInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegflight'),params.id])]
        }
        
        return ['instance':testLegFlightInstance]
    }

    //--------------------------------------------------------------------------
    Map getFlightTest(params)
    {
        def flightTestInstance = FlightTest.get(params.id)

        if (!flightTestInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
        }
        
        return ['instance':flightTestInstance]
    }

    //--------------------------------------------------------------------------
    Map updateFlightTest(params)
    {
        def flightTestInstance = FlightTest.get(params.id)
        
        if (flightTestInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(flightTestInstance.version > version) {
                    flightTestInstance.errors.rejectValue("version", "flightTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':flightTestInstance]
                }
            }
            
            flightTestInstance.properties = params
            
	        Test.findAllByTask(flightTestInstance.task).each { testInstance ->
	        	testInstance.timeCalculated = false
	            testInstance.save()
	        }
            
            if(!flightTestInstance.hasErrors() && flightTestInstance.save()) {
                return ['instance':flightTestInstance,'saved':true,'message':getMsg('fc.updated',["${flightTestInstance.name()}"])]
            } else {
                return ['instance':flightTestInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createFlightTest(params)
    {
        if (!Route.count()) {
            return ['message':getMsg('fc.route.notfound'),'error':true,
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':params.task.id]
        }
         
        def flightTestInstance = new FlightTest()
        flightTestInstance.properties = params
        return ['instance':flightTestInstance]
    }

    
    //--------------------------------------------------------------------------
    Map saveFlightTest(params)
    {
        def flightTestInstance = new FlightTest(params)
        
        flightTestInstance.task = Task.get( params.taskid )
        
        if (!flightTestInstance.direction) {
        	flightTestInstance.direction = 0
        }
        if (!flightTestInstance.speed) {
        	flightTestInstance.speed = 0
        }
        
        if(!flightTestInstance.hasErrors() && flightTestInstance.save()) {

            def taskInstance = Task.get( params.taskid )
            taskInstance.flighttest = flightTestInstance
            taskInstance.save()

            def windInstance = new Wind(direction:flightTestInstance.direction,speed:flightTestInstance.speed)
            windInstance.save()
            
            def flightTestWindInstance = new FlightTestWind(params)
            flightTestWindInstance.wind = windInstance
            flightTestWindInstance.flighttest = flightTestInstance
            flightTestWindInstance.save()

            return ['instance':flightTestInstance,'saved':true,'message':getMsg('fc.created',["${flightTestInstance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':taskInstance.id]
        } else {
            return ['instance':flightTestInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteFlightTest(params)
    {
        def flightTestInstance = FlightTest.get(params.id)
        
        if (flightTestInstance) {
            try {
                def taskInstance = Task.get( flightTestInstance.task.id )
                taskInstance.flighttest = null
                taskInstance.save()
                
                flightTestInstance.delete()
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${flightTestInstance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.flighttest'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getFlightTestWind(params)
    {
        def flightTestWindInstance = FlightTestWind.get(params.id)

        if (!flightTestWindInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttestwind'),params.id])]
        }
        
        flightTestWindInstance.direction = flightTestWindInstance.wind.direction
        flightTestWindInstance.speed = flightTestWindInstance.wind.speed
        
        return ['instance':flightTestWindInstance]
    }

    //--------------------------------------------------------------------------
    Map updateFlightTestWind(params)
    {
        def flightTestWindInstance = FlightTestWind.get(params.id)
        
        if (flightTestWindInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(flightTestWindInstance.version > version) {
                    flightTestWindInstance.errors.rejectValue("version", "flightTestWind.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':flightTestWindInstance]
                }
            }
            
            flightTestWindInstance.properties = params

            flightTestWindInstance.wind.direction = flightTestWindInstance.direction
            flightTestWindInstance.wind.speed = flightTestWindInstance.speed
            
            if(!flightTestWindInstance.hasErrors() && flightTestWindInstance.save()) {
                return ['instance':flightTestWindInstance,'saved':true,'message':getMsg('fc.updated',["${flightTestWindInstance.name()}"])]
            } else {
                return ['instance':flightTestWindInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttestwind'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createFlightTestWind(params)
    {
        def flightTestWindInstance = new FlightTestWind()
        flightTestWindInstance.properties = params
        return ['instance':flightTestWindInstance]
    }

    
    //--------------------------------------------------------------------------
    Map saveFlightTestWind(params)
    {
        def flightTestWindInstance = new FlightTestWind(params)
        
        flightTestWindInstance.flighttest = FlightTest.get(params.flighttestid)
        
        def windInstance = new Wind(params)
        if(!windInstance.hasErrors() && windInstance.save()) {
            flightTestWindInstance.wind = windInstance
        }
        
        if(!flightTestWindInstance.hasErrors() && flightTestWindInstance.save()) {
            return ['instance':flightTestWindInstance,'saved':true,'message':getMsg('fc.created',["${flightTestWindInstance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,
                    'taskid':flightTestWindInstance.flighttest.task.id,
                    'flighttestid':flightTestWindInstance.flighttest.id]
        } else {
            return ['instance':flightTestWindInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteFlightTestWind(params)
    {
        def flightTestWindInstance = FlightTestWind.get(params.id)
        
        if (flightTestWindInstance) {
            try {
                flightTestWindInstance.delete()
                return ['deleted':true,'message':getMsg('fc.deleted',["${flightTestWindInstance.name()}"]),
                        'flighttestid':flightTestWindInstance.flighttest.id]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.flighttestwind'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttestwind'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getLandingTest(params)
    {
        def landingTestInstance = LandingTest.get(params.id)

        if (!landingTestInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.landingtest'),params.id])]
        }
        
        return ['instance':landingTestInstance]
    }

    //--------------------------------------------------------------------------
    Map updateLandingTest(params)
    {
        def landingTestInstance = LandingTest.get(params.id)
        
        if (landingTestInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(landingTestInstance.version > version) {
                    landingTestInstance.errors.rejectValue("version", "landingTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':landingTestInstance]
                }
            }
            
            landingTestInstance.properties = params
            if(!landingTestInstance.hasErrors() && landingTestInstance.save()) {
                return ['instance':landingTestInstance,'saved':true,'message':getMsg('fc.updated',["${landingTestInstance.name()}"])]
            } else {
                return ['instance':landingTestInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.landingtest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createLandingTest(params)
    {
        def landingTestInstance = new LandingTest()
        landingTestInstance.properties = params
        return ['instance':landingTestInstance]
    }

    
    //--------------------------------------------------------------------------
    Map saveLandingTest(params)
    {
        def landingTestInstance = new LandingTest(params)
        
        landingTestInstance.task = Task.get( params.taskid )
        
        if(!landingTestInstance.hasErrors() && landingTestInstance.save()) {

            def taskInstance = Task.get( params.taskid )
            taskInstance.landingtest = landingTestInstance
            taskInstance.save()
            
            return ['instance':landingTestInstance,'saved':true,'message':getMsg('fc.created',["${landingTestInstance.name()}"]),
                    'fromtask':params.fromtask,'taskid':taskInstance.id]
        } else {
            return ['instance':landingTestInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteLandingTest(params)
    {
        def landingTestInstance = LandingTest.get(params.id)
        
        if (landingTestInstance) {
            try {
                def taskInstance = Task.get( landingTestInstance.task.id )
                taskInstance.landingtest = null
                taskInstance.save()
                
                landingTestInstance.delete()
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${landingTestInstance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.landingtest'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.landingtest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getLandingTestTask(params)
    {
        def landingTestTaskInstance = LandingTestTask.get(params.id)

        if (!landingTestTaskInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.landingtesttask'),params.id])]
        }
        
        return ['instance':landingTestTaskInstance]
    }

    //--------------------------------------------------------------------------
    Map updateLandingTestTask(params)
    {
        def landingTestTaskInstance = LandingTestTask.get(params.id)
        
        if (landingTestTaskInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(landingTestTaskInstance.version > version) {
                    landingTestTaskInstance.errors.rejectValue("version", "landingTestTask.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':landingTestTaskInstance]
                }
            }
            
            landingTestTaskInstance.properties = params
            if(!landingTestTaskInstance.hasErrors() && landingTestTaskInstance.save()) {
                return ['instance':landingTestTaskInstance,'saved':true,'message':getMsg('fc.updated',["${landingTestTaskInstance.name()}"])]
            } else {
                return ['instance':landingTestTaskInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.landingtesttask'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createLandingTestTask(params)
    {
        def landingTestTaskInstance = new LandingTestTask()
        landingTestTaskInstance.properties = params
        return ['instance':landingTestTaskInstance]
    }

    
    //--------------------------------------------------------------------------
    Map saveLandingTestTask(params)
    {
        def landingTestTaskInstance = new LandingTestTask(params)
        
        landingTestTaskInstance.landingtest = LandingTest.get(params.landingtestid)
        landingTestTaskInstance.idTitle = LandingTestTask.countByLandingtest(landingTestTaskInstance.landingtest) + 1
        
        if(!landingTestTaskInstance.hasErrors() && landingTestTaskInstance.save()) {
            return ['instance':landingTestTaskInstance,'saved':true,'message':getMsg('fc.created',["${landingTestTaskInstance.name()}"]),
                    'landingtestid':landingTestTaskInstance.landingtest.id]
        } else {
            return ['instance':landingTestTaskInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteLandingTestTask(params)
    {
        def landingTestTaskInstance = LandingTestTask.get(params.id)
        
        if (landingTestTaskInstance) {
            try {
                def landingTestInstance = landingTestTaskInstance.landingtest 
                    
                landingTestTaskInstance.delete()

                LandingTestTask.findAllByLandingtest(landingTestInstance).eachWithIndex { landingTestTaskInstance2, index  -> 
                    landingTestTaskInstance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${landingTestTaskInstance.name()}"]),
                        'landingtestid':landingTestTaskInstance.landingtest.id]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.landingtesttask'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.landingtesttask'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getPlanningTest(params)
    {
        def planningTestInstance = PlanningTest.get(params.id)

        if (!planningTestInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtest'),params.id])]
        }
        
        return ['instance':planningTestInstance]
    }

    //--------------------------------------------------------------------------
    Map updatePlanningTest(params)
    {
        def planningTestInstance = PlanningTest.get(params.id)
        
        if (planningTestInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(planningTestInstance.version > version) {
                    planningTestInstance.errors.rejectValue("version", "planningTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':planningTestInstance]
                }
            }
            
            planningTestInstance.properties = params
            if(!planningTestInstance.hasErrors() && planningTestInstance.save()) {
                return ['instance':planningTestInstance,'saved':true,'message':getMsg('fc.updated',["${planningTestInstance.name()}"])]
            } else {
                return ['instance':planningTestInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createPlanningTest(params)
    {
        if (!Route.count()) {
            return ['message':getMsg('fc.route.notfound'),'error':true,
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':params.task.id]
        }
         
        def planningTestInstance = new PlanningTest()
        planningTestInstance.properties = params
        return ['instance':planningTestInstance]
    }

    
    //--------------------------------------------------------------------------
    Map savePlanningTest(params)
    {
        def planningTestInstance = new PlanningTest(params)
        
        planningTestInstance.task = Task.get( params.taskid )
        
        if (!planningTestInstance.direction) {
            planningTestInstance.direction = 0
        }
        if (!planningTestInstance.speed) {
            planningTestInstance.speed = 0
        }
        
        if(!planningTestInstance.hasErrors() && planningTestInstance.save()) {

            if (params.route) {
                def windInstance = new Wind(direction:planningTestInstance.direction,speed:planningTestInstance.speed)
                windInstance.save()
                
                def planningTestTaskInstance = new PlanningTestTask(params)
                planningTestTaskInstance.planningtest = planningTestInstance
                planningTestTaskInstance.title = params.taskTitle
                planningTestTaskInstance.idTitle = 1
                planningTestTaskInstance.wind = windInstance
                if (planningTestTaskInstance.hasErrors() || !planningTestTaskInstance.save()) {
                    planningTestInstance.delete()
                    return ['instance':planningTestInstance]
                }
            }
            
            def taskInstance = Task.get( params.taskid )
            taskInstance.planningtest = planningTestInstance
            taskInstance.save()
            
            return ['instance':planningTestInstance,'saved':true,'message':getMsg('fc.created',["${planningTestInstance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':taskInstance.id]
        } else {
            return ['instance':planningTestInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deletePlanningTest(params)
    {
        def planningTestInstance = PlanningTest.get(params.id)
        
        if (planningTestInstance) {
            try {
                def taskInstance = Task.get( planningTestInstance.task.id )
                taskInstance.planningtest = null
                taskInstance.save()
                
                planningTestInstance.delete()
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${planningTestInstance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.landingtest'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getPlanningTestTask(params)
    {
        def planningTestTaskInstance = PlanningTestTask.get(params.id)

        if (!planningTestTaskInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtesttask'),params.id])]
        }
        
        planningTestTaskInstance.direction = planningTestTaskInstance.wind.direction
        planningTestTaskInstance.speed = planningTestTaskInstance.wind.speed
        
        return ['instance':planningTestTaskInstance]
    }

    //--------------------------------------------------------------------------
    Map updatePlanningTestTask(params)
    {
    	def planningTestTaskInstance = PlanningTestTask.get(params.id)
        
        if (planningTestTaskInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(planningTestTaskInstance.version > version) {
                    planningTestTaskInstance.errors.rejectValue("version", "planningTestTask.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':planningTestTaskInstance]
                }
            }
            
            planningTestTaskInstance.properties = params
            
            if (!planningTestTaskInstance.direction) {
            	planningTestTaskInstance.direction = 0
            }
            if (!planningTestTaskInstance.speed) {
            	planningTestTaskInstance.speed = 0
            }
            
            planningTestTaskInstance.wind.direction = planningTestTaskInstance.direction
            planningTestTaskInstance.wind.speed = planningTestTaskInstance.speed

            Test.findAllByTask(planningTestTaskInstance.planningtest.task).each { testInstance ->
                calulateTestLegPlannings(testInstance, planningTestTaskInstance.route)
            }

            if(!planningTestTaskInstance.hasErrors() && planningTestTaskInstance.save()) {
                return ['instance':planningTestTaskInstance,'saved':true,'message':getMsg('fc.updated',["${planningTestTaskInstance.name()}"])]
            } else {
                return ['instance':planningTestTaskInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtesttask'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createPlanningTestTask(params)
    {
        if (!Route.count()) {
            return ['message':getMsg('fc.route.notfound'),'error':true,'planningtestid':params.planningtestid]
        }
         
        def planningTestTaskInstance = new PlanningTestTask()
        planningTestTaskInstance.properties = params
        return ['instance':planningTestTaskInstance]
    }

    
    //--------------------------------------------------------------------------
    Map savePlanningTestTask(params)
    {
        def planningTestTaskInstance = new PlanningTestTask(params)

        planningTestTaskInstance.planningtest = PlanningTest.get(params.planningtestid)
        planningTestTaskInstance.idTitle = PlanningTestTask.countByPlanningtest(planningTestTaskInstance.planningtest) + 1
        
        if (!planningTestTaskInstance.direction) {
            planningTestTaskInstance.direction = 0
        }
        if (!planningTestTaskInstance.speed) {
            planningTestTaskInstance.speed = 0
        }
        
        def windInstance = new Wind(direction:planningTestTaskInstance.direction,speed:planningTestTaskInstance.speed)
        if(!windInstance.hasErrors() && windInstance.save()) {
            planningTestTaskInstance.wind = windInstance
        }
        
        if(!planningTestTaskInstance.hasErrors() && planningTestTaskInstance.save()) {
            return ['instance':planningTestTaskInstance,'saved':true,'message':getMsg('fc.created',["${planningTestTaskInstance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,
                    'taskid':planningTestTaskInstance.planningtest.task.id,
                    'planningtestid':planningTestTaskInstance.planningtest.id]
        } else {
            return ['instance':planningTestTaskInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deletePlanningTestTask(params)
    {
        def planningTestTaskInstance = PlanningTestTask.get(params.id)
        
        if (planningTestTaskInstance) {
            try {
                def planningTestInstance = planningTestTaskInstance.planningtest 
                    
                planningTestTaskInstance.delete()

                PlanningTestTask.findAllByPlanningtest(planningTestInstance).eachWithIndex { planningTestTaskInstance2, index  -> 
                    planningTestTaskInstance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${planningTestTaskInstance.name()}"]),
                        'planningtestid':planningTestTaskInstance.planningtest.id]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.planningtesttask'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtesttask'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getTestLegPlanning(params)
    {
        def testLegPlanningInstance = TestLegPlanning.get(params.id)

        if (!testLegPlanningInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanning'),params.id])]
        }
        
        return ['instance':testLegPlanningInstance]
    }

    //--------------------------------------------------------------------------
    Map getTestLegPlanningResult(params)
    {
        def testLegPlanningInstance = TestLegPlanning.get(params.id)

        if (!testLegPlanningInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
        
        // calculate resultLegTimeInput
        int seconds = (3600 * testLegPlanningInstance.resultLegTime).toDouble().round().toInteger()
        GregorianCalendar time = new GregorianCalendar()
        time.set(Calendar.HOUR_OF_DAY, 0)
        time.set(Calendar.MINUTE, 0)
        time.set(Calendar.SECOND, seconds)
        testLegPlanningInstance.resultLegTimeInput = time.getTime().format("HH:mm:ss")

		return ['instance':testLegPlanningInstance]
    }

    //--------------------------------------------------------------------------
    Map updateTestLegPlanningResult(params)
    {
    	TestLegPlanning testLegPlanningInstance = TestLegPlanning.get(params.id)
        if(testLegPlanningInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(testLegPlanningInstance.version > version) {
                    testLegPlanningInstance.errors.rejectValue("version", "testLegPlanning.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':testLegPlanningInstance]
                }
            }

            testLegPlanningInstance.properties = params
            
            Map ret = calculateLegPlanningInstance(testLegPlanningInstance)
            if (ret)
            {
            	return ret
            }
            
            calculateTestPenalties(testLegPlanningInstance.test)
            
            if(!testLegPlanningInstance.hasErrors() && testLegPlanningInstance.save()) {
                return ['instance':testLegPlanningInstance,'saved':true,'message':getMsg('fc.updated',["${testLegPlanningInstance.name()}"])]
            } else {
            	return ['instance':testLegPlanningInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetTestLegPlanningResult(params)
    {
        TestLegPlanning testLegPlanningInstance = TestLegPlanning.get(params.id)
        if(testLegPlanningInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(testLegPlanningInstance.version > version) {
                    testLegPlanningInstance.errors.rejectValue("version", "testLegPlanning.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':testLegPlanningInstance]
                }
            }

            // reset results
		    testLegPlanningInstance.ResetResults()
            
            calculateTestPenalties(testLegPlanningInstance.test)
            
            if(!testLegPlanningInstance.hasErrors() && testLegPlanningInstance.save()) {
                return ['instance':testLegPlanningInstance,'saved':true,'message':getMsg('fc.updated',["${testLegPlanningInstance.name()}"])]
            } else {
                return ['instance':testLegPlanningInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map calculateLegPlanningInstance(testLegPlanningInstance)
    {
        // calculate resultLegTime
        try {
            String input_time = testLegPlanningInstance.resultLegTimeInput.replace('.',':')
            switch (input_time.size()) {
                case 1:
                    Date date = Date.parse("s", input_time)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                case 2:
                    Date date = Date.parse("ss", input_time)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                case 4:
                    Date date = Date.parse("m:ss", input_time)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                case 5:
                    Date date = Date.parse("mm:ss", input_time)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                case 7:
                    Date date = Date.parse("H:mm:ss", input_time)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                case 8:
                    Date date = Date.parse("HH:mm:ss", input_time)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                default:
                    return ['instance':testLegPlanningInstance,'error':true,'message':getMsg('fc.testlegplanningresult.legtime.error')]
            }
        } catch (Exception e) {
            return ['instance':testLegPlanningInstance,'error':true,'message':getMsg('fc.testlegplanningresult.value.error')]
        }
        
        Contest contestInstance = testLegPlanningInstance.test.task.contest
        
        // calculate penaltyTrueHeading
        int planTrueHeading = testLegPlanningInstance.planTrueHeading.toDouble().round().toInteger()
        if (planTrueHeading > 180) {
            planTrueHeading -= 360
        }
        int resultTrueHeading = testLegPlanningInstance.resultTrueHeading
        if (resultTrueHeading > 180) {
            resultTrueHeading -= 360
        }
        int diffTrueHeading =  Math.abs(planTrueHeading - resultTrueHeading)
        if (diffTrueHeading > contestInstance.planningTestDirectionCorrectGrad) {
            testLegPlanningInstance.penaltyTrueHeading = contestInstance.planningTestDirectionPointsPerGrad * (diffTrueHeading - contestInstance.planningTestDirectionCorrectGrad)
        } else {
            testLegPlanningInstance.penaltyTrueHeading = 0
        }
        
        // calculate penaltyLegTime
        int planLegTimeSeconds = (3600 * testLegPlanningInstance.planLegTime).toDouble().round().toInteger()
        int resultLegTimeSeconds = (3600 * testLegPlanningInstance.resultLegTime).toDouble().round().toInteger()

        int diffLegTime =  Math.abs(planLegTimeSeconds - resultLegTimeSeconds)
        if (diffLegTime > contestInstance.planningTestTimeCorrectSecond) {
            testLegPlanningInstance.penaltyLegTime = contestInstance.planningTestTimePointsPerSecond * (diffLegTime - contestInstance.planningTestTimeCorrectSecond)
        } else {
            testLegPlanningInstance.penaltyLegTime = 0
        }
        
        testLegPlanningInstance.resultEntered = true
        
        return [:]
    }
    
    //--------------------------------------------------------------------------
    Map getCoordResult(params)
    {
        def coordResultInstance = CoordResult.get(params.id)

        if (!coordResultInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordresult'),params.id])]
        }
        
        // calculate resultCpTimeInput
        coordResultInstance.resultCpTimeInput = coordResultInstance.resultCpTime.format("HH:mm:ss")

        return ['instance':coordResultInstance]
    }

    //--------------------------------------------------------------------------
    Map updateCoordResult(params)
    {
        CoordResult coordResultInstance = CoordResult.get(params.id)
        if(coordResultInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(coordResultInstance.version > version) {
                    coordResultInstance.errors.rejectValue("version", "coordResult.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordResultInstance]
                }
            }

            coordResultInstance.properties = params
            
            Map ret = calculateCoordResultInstance(coordResultInstance,false)
            if (ret)
            {
                return ret
            }
            
            calculateTestPenalties(coordResultInstance.test)
            
            if(!coordResultInstance.hasErrors() && coordResultInstance.save()) {
                return ['instance':coordResultInstance,'saved':true,'message':getMsg('fc.updated',["${coordResultInstance.name()}"])]
            } else {
                return ['instance':coordResultInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map updateCoordResultProcedureTurn(params)
    {
        CoordResult coordResultInstance = CoordResult.get(params.id)
        if(coordResultInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(coordResultInstance.version > version) {
                    coordResultInstance.errors.rejectValue("version", "coordResult.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordResultInstance]
                }
            }

            coordResultInstance.properties = params
            coordResultInstance.resultProcedureTurnEntered = true
            
            calculateTestPenalties(coordResultInstance.test)
            
            if(!coordResultInstance.hasErrors() && coordResultInstance.save()) {
                return ['instance':coordResultInstance,'saved':true,'message':getMsg('fc.updated',["${coordResultInstance.name()}"])]
            } else {
                return ['instance':coordResultInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetCoordResult(params)
    {
        CoordResult coordResultInstance = CoordResult.get(params.id)
        if(coordResultInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(coordResultInstance.version > version) {
                    coordResultInstance.errors.rejectValue("version", "coordResult.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordResultInstance]
                }
            }

            // reset results
            coordResultInstance.ResetResults()

            calculateTestPenalties(coordResultInstance.test)
            
            if(!coordResultInstance.hasErrors() && coordResultInstance.save()) {
                return ['instance':coordResultInstance,'saved':true,'message':getMsg('fc.updated',["${coordResultInstance.name()}"])]
            } else {
                return ['instance':coordResultInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map calculateCoordResultInstance(coordResultInstance,boolean calculateUTC)
    {
    	// calculate resultCpTime
        try {
            String input_time = coordResultInstance.resultCpTimeInput.replace('.',':')
            switch (input_time.size()) {
                case 1:
                    coordResultInstance.resultCpTime = Date.parse("H", input_time)
                    break
                case 2:
                	coordResultInstance.resultCpTime = Date.parse("HH", input_time)
                    break
                case 4:
                	coordResultInstance.resultCpTime = Date.parse("H:mm", input_time)
                    break
                case 5:
                	coordResultInstance.resultCpTime = Date.parse("HH:mm", input_time)
                    break
                case 7:
                	coordResultInstance.resultCpTime = Date.parse("H:mm:ss", input_time)
                    break
                case 8:
                	coordResultInstance.resultCpTime = Date.parse("HH:mm:ss", input_time)
                    break
                default:
                    return ['instance':coordResultInstance,'error':true,'message':getMsg('fc.coordresult.cptime.error')]
            }
        } catch (Exception e) {
            return ['instance':coordResultInstance,'error':true,'message':getMsg('fc.testlegplanningresult.value.error')]
        }
        
        Contest contestInstance = coordResultInstance.test.task.contest
        
        // calculate penaltyCoord
        if (coordResultInstance.resultCpNotFound) {
        	coordResultInstance.penaltyCoord = contestInstance.flightTestCpNotFoundPoints
        } else {
	        GregorianCalendar planCpTime = new GregorianCalendar() 
	        planCpTime.setTime(coordResultInstance.planCpTime)
	        int planCpTimeSeconds = planCpTime.get(Calendar.SECOND) + 60 * planCpTime.get(Calendar.MINUTE) + 3600 * planCpTime.get(Calendar.HOUR)
	        	
	        GregorianCalendar resultCpTime = new GregorianCalendar() 
	        resultCpTime.setTime(coordResultInstance.resultCpTime)

	        if (calculateUTC) {
	        	Date timeZoneDate = Date.parse("HH:mm",contestInstance.timeZone)
	        	GregorianCalendar timeZoneCalendar = new GregorianCalendar()
	        	timeZoneCalendar.setTime(timeZoneDate)
	        	resultCpTime.add(Calendar.HOUR, timeZoneCalendar.get(Calendar.HOUR))
	        	resultCpTime.add(Calendar.MINUTE, timeZoneCalendar.get(Calendar.MINUTE))
	        	coordResultInstance.resultCpTime = resultCpTime.getTime()
	        }
	        int resultCpTimeSeconds = resultCpTime.get(Calendar.SECOND) + 60 * resultCpTime.get(Calendar.MINUTE) + 3600 * resultCpTime.get(Calendar.HOUR)
	        
	        int diffCpTime =  Math.abs(planCpTimeSeconds - resultCpTimeSeconds)
	        if (diffCpTime > contestInstance.flightTestCptimeCorrectSecond) {
	            coordResultInstance.penaltyCoord = contestInstance.flightTestCptimePointsPerSecond * (diffCpTime - contestInstance.flightTestCptimeCorrectSecond)
	        } else {
	            coordResultInstance.penaltyCoord = 0
	        }
	        if (coordResultInstance.penaltyCoord > contestInstance.flightTestCptimeMaxPoints) {
	        	coordResultInstance.penaltyCoord = contestInstance.flightTestCptimeMaxPoints
	        }
        }
        coordResultInstance.resultEntered = true
        
        // calculate resultMinAltitudeMissed
        if (coordResultInstance.resultAltitude && coordResultInstance.resultAltitude < coordResultInstance.altitude) {
        	coordResultInstance.resultMinAltitudeMissed = true
        }
        
        return [:]
    }
    
    //--------------------------------------------------------------------------
    Map getSpecialTest(params)
    {
        def specialTestInstance = SpecialTest.get(params.id)

        if (!specialTestInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.specialtest'),params.id])]
        }
        
        return ['instance':specialTestInstance]
    }

    //--------------------------------------------------------------------------
    Map updateSpecialTest(params)
    {
        def specialTestInstance = SpecialTest.get(params.id)
        
        if (specialTestInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(specialTestInstance.version > version) {
                    specialTestInstance.errors.rejectValue("version", "specialTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':specialTestInstance]
                }
            }
            
            specialTestInstance.properties = params
            if(!specialTestInstance.hasErrors() && specialTestInstance.save()) {
                return ['instance':specialTestInstance,'saved':true,'message':getMsg('fc.updated',["${specialTestInstance.name()}"])]
            } else {
                return ['instance':specialTestInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.specialtest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createSpecialTest(params)
    {
        def specialTestInstance = new SpecialTest()
        specialTestInstance.properties = params
        return ['instance':specialTestInstance]
    }

    
    //--------------------------------------------------------------------------
    Map saveSpecialTest(params)
    {
        def specialTestInstance = new SpecialTest(params)
        
        specialTestInstance.task = Task.get( params.taskid )
        
        if(!specialTestInstance.hasErrors() && specialTestInstance.save()) {

            def taskInstance = Task.get( params.taskid )
            taskInstance.specialtest = specialTestInstance
            taskInstance.save()
            
            return ['instance':specialTestInstance,'saved':true,'message':getMsg('fc.created',["${specialTestInstance.name()}"]),
                    'fromtask':params.fromtask,'taskid':taskInstance.id]
        } else {
            return ['instance':specialTestInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteSpecialTest(params)
    {
        def specialTestInstance = SpecialTest.get(params.id)
        
        if (specialTestInstance) {
            try {
                def taskInstance = Task.get( specialTestInstance.task.id )
                taskInstance.specialtest = null
                taskInstance.save()
                
                specialTestInstance.delete()
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${specialTestInstance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.landingtest'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.specialtest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getSpecialTestTask(params)
    {
        def specialTestTaskInstance = SpecialTestTask.get(params.id)

        if (!specialTestTaskInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.specialtesttask'),params.id])]
        }
        
        return ['instance':specialTestTaskInstance]
    }

    //--------------------------------------------------------------------------
    Map updateSpecialTestTask(params)
    {
        def specialTestTaskInstance = SpecialTestTask.get(params.id)
        
        if (specialTestTaskInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(specialTestTaskInstance.version > version) {
                    specialTestTaskInstance.errors.rejectValue("version", "specialTestTask.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':specialTestTaskInstance]
                }
            }
            
            specialTestTaskInstance.properties = params
            if(!specialTestTaskInstance.hasErrors() && specialTestTaskInstance.save()) {
                return ['instance':specialTestTaskInstance,'saved':true,'message':getMsg('fc.updated',["${specialTestTaskInstance.name()}"])]
            } else {
                return ['instance':specialTestTaskInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.specialtesttask'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createSpecialTestTask(params)
    {
        def specialTestTaskInstance = new SpecialTestTask()
        specialTestTaskInstance.properties = params
        return ['instance':specialTestTaskInstance]
    }

    
    //--------------------------------------------------------------------------
    Map saveSpecialTestTask(params)
    {
        def specialTestTaskInstance = new SpecialTestTask(params)
        
        specialTestTaskInstance.specialtest = SpecialTest.get(params.specialtestid)
        specialTestTaskInstance.idTitle = SpecialTestTask.countBySpecialtest(specialTestTaskInstance.specialtest) + 1
        
        if(!specialTestTaskInstance.hasErrors() && specialTestTaskInstance.save()) {
            return ['instance':specialTestTaskInstance,'saved':true,'message':getMsg('fc.created',["${specialTestTaskInstance.name()}"]),
                    'specialtestid':specialTestTaskInstance.specialtest.id]
        } else {
            return ['instance':specialTestTaskInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteSpecialTestTask(params)
    {
        def specialTestTaskInstance = SpecialTestTask.get(params.id)
        
        if (specialTestTaskInstance) {
            try {
                def specialTestInstance = specialTestTaskInstance.specialtest
                
                specialTestTaskInstance.delete()
                
                SpecialTestTask.findAllBySpecialtest(specialTestInstance).eachWithIndex { specialTestTaskInstance2, index  -> 
                    specialTestTaskInstance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${specialTestTaskInstance.name()}"]),
                        'specialtestid':specialTestTaskInstance.specialtest.id]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.specialtesttask'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.specialtesttask'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getWind(params)
    {
        def windInstance = Wind.get(params.id)

        if(!windInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.wind'),params.id])]
        }
        
        return ['instance':windInstance]
    }
    
    //--------------------------------------------------------------------------
    private Map calculateLegData(CoordRoute newCoordRouteInstance, CoordRoute lastCoordRouteInstance)
    {
        // calculate leg distance
        def newLatMath = newCoordRouteInstance.latMath()
        def lastLatMath = lastCoordRouteInstance.latMath()
            
        def latDiff = newLatMath - lastLatMath 
        def lonDiff = newCoordRouteInstance.lonMath() - lastCoordRouteInstance.lonMath()
        
        def latDis = 60 * latDiff
        def lonDis = 60 * lonDiff * Math.cos( Math.toRadians((newLatMath + lastLatMath)/2) )
        
        def dis = Math.sqrt(latDis*latDis + lonDis*lonDis)
        
        // calculate leg direction
        def dir = 0.0
        if (latDis == 0 && lonDis > 0) {
            dir = 90.0
        } else if (latDis == 0 && lonDis < 0) {
            dir = 270.0
        } else if (lonDis == 0 && latDis > 0) {
            dir = 0.0
        } else if (lonDis == 0 && latDis < 0) {
            dir = 180.0
        } else if (latDis > 0 && lonDis > 0) {
            dir = Math.toDegrees(Math.atan(lonDis/latDis))
        } else if (latDis < 0 && lonDis > 0) {
            dir = 180.0 + Math.toDegrees(Math.atan(lonDis/latDis))
        } else if (latDis < 0 && lonDis < 0) {
            dir = 180.0 + Math.toDegrees(Math.atan(lonDis/latDis))
        } else if (latDis > 0 && lonDis < 0) {
            dir = 360.0 + Math.toDegrees(Math.atan(lonDis/latDis))
        }
        
    	return [dis:dis,dir:dir]
    }
    
    //--------------------------------------------------------------------------
    private void newLeg(Route route, CoordRoute newCoordRouteInstance, CoordRoute lastCoordRouteInstance, CoordRoute lastCoordRouteTestInstance, BigDecimal lastMapMeasureDistance, BigDecimal lastMapMeasureTrueTrack) 
    {
    	println "newLeg: ${route.name()} ${newCoordRouteInstance.title()}"
    	
        // create routeLegCoordInstance
    	if (lastCoordRouteInstance) {
    		Map legDataCoord = calculateLegData(newCoordRouteInstance, lastCoordRouteInstance)
	
	        def titleCoord = "${lastCoordRouteInstance.title()} -> ${newCoordRouteInstance.title()}"
	        
	        def routeLegCoordInstance = new RouteLegCoord([trueTrack:legDataCoord.dir,
	                                                       coordDistance:legDataCoord.dis,
	                                                       route:route,
	                                                       title:titleCoord,
	                                                       mapmeasuredistance:newCoordRouteInstance.mapmeasuredistance,
	                                                       mapdistance:newCoordRouteInstance.mapdistance,
	                                                       mapmeasuretruetrack:newCoordRouteInstance.mapmeasuretruetrack
	                                                      ]) 
	        routeLegCoordInstance.save()
        }

        // create routeLegTestInstance
        if (lastCoordRouteInstance && lastCoordRouteTestInstance) {
            Map legDataTest = calculateLegData(newCoordRouteInstance, lastCoordRouteTestInstance)
            
	        def titleTest = "${lastCoordRouteTestInstance.title()} -> ${newCoordRouteInstance.title()}"
            
	        if ( (lastCoordRouteTestInstance.type == CoordType.SP && newCoordRouteInstance.type == CoordType.TP) ||
	        	 (lastCoordRouteTestInstance.type == CoordType.SP && newCoordRouteInstance.type == CoordType.FP) ||
	        	 (lastCoordRouteTestInstance.type == CoordType.TP && newCoordRouteInstance.type == CoordType.TP) ||
	        	 (lastCoordRouteTestInstance.type == CoordType.TP && newCoordRouteInstance.type == CoordType.FP) )
	        {
	        	def routeLegTestInstance = new RouteLegTest([trueTrack:legDataTest.dir,
	        	                                             coordDistance:legDataTest.dis,
	        	                                             route:route,
	        	                                             title:titleTest,
	                                                         mapmeasuredistance:lastMapMeasureDistance,
	                                                         mapdistance:calculateMapDistance(route.contest,lastMapMeasureDistance),
	                                                         mapmeasuretruetrack:lastMapMeasureTrueTrack
	                                                        ])
	        	routeLegTestInstance.save()
	        }
        }
    }
    
    //--------------------------------------------------------------------------
    BigDecimal calculateMapDistance(Contest contestInstance, BigDecimal mapMeasureDistance)
    {
    	if (mapMeasureDistance == null) {
    		return null
    	}
  		return contestInstance.mapScale * mapMeasureDistance / mmPerNM
    }

    //--------------------------------------------------------------------------
    private void removeAllRouteLegs(Route routeInstance) 
    {
    	println "removeAllRouteLegs: ${routeInstance.name()}"
    	
        RouteLegCoord.findAllByRoute(routeInstance).each { routeLegInstance ->
        	routeLegInstance.delete()
        }
        RouteLegTest.findAllByRoute(routeInstance).each { routeLegInstance ->
        	routeLegInstance.delete()
        }
    }
    
    //--------------------------------------------------------------------------
    private void calculateRouteLegs(Route routeInstance) 
    {
        println "calculateRouteLegs: ${routeInstance.name()}"
        
        // remove all legs
    	removeAllRouteLegs(routeInstance)
        
        // calculate new legs
        CoordRoute lastCoordRouteInstance
        CoordRoute lastCoordRouteTestInstance
        BigDecimal lastMapMeasureDistance = null
        BigDecimal lastMapMeasureTrueTrack = null
        CoordRoute.findAllByRoute(routeInstance).each { coordRouteInstance ->
      		lastMapMeasureDistance = addMapDistance(lastMapMeasureDistance,coordRouteInstance.mapmeasuredistance)
        	lastMapMeasureTrueTrack = coordRouteInstance.mapmeasuretruetrack
            newLeg(routeInstance, coordRouteInstance, lastCoordRouteInstance, lastCoordRouteTestInstance, lastMapMeasureDistance, lastMapMeasureTrueTrack)
            lastCoordRouteInstance = coordRouteInstance
            switch (coordRouteInstance.type) {
	            case CoordType.SP:
	            case CoordType.TP:
	            case CoordType.FP:
	            	lastCoordRouteTestInstance = coordRouteInstance
	            	lastMapMeasureDistance = null
	            	break
            }
        }
    }

    //--------------------------------------------------------------------------
    private void calculateSecretLegRatio(Route routeInstance)
    {
        println "calculateSecretLegRatio: ${routeInstance.name()}"
        
        def startCoordRouteInstance
        def startCoordRouteInstance2
        def lastType
        def lastLegDirection = null
        
        CoordRoute.findAllByRoute(routeInstance).each { coordRouteInstance ->
        	switch (coordRouteInstance.type) {
        		case CoordType.SP:
        		case CoordType.TP:
        			startCoordRouteInstance = coordRouteInstance
        			break
        		case CoordType.SECRET:
	        		
        			// search endCoordRouteInstance
	        		def endCoordRouteInstance
	        		boolean found = false
	        		CoordRoute.findAllByRoute(routeInstance).each { coordRouteInstance2 ->
	                    if (found) {
	                        switch (coordRouteInstance2.type) {
	                        	case CoordType.TP:
                                case CoordType.FP:
                                	endCoordRouteInstance = coordRouteInstance2
                                	found = false
                                	break
	                        }
	                    }
	        			if (coordRouteInstance2 == startCoordRouteInstance) {
	        				found = true
	        			}
	        		}

	        		if (endCoordRouteInstance && !coordRouteInstance.secretLegRatio) {
		        		// get leg data
		                Map legData = calculateLegData(endCoordRouteInstance, startCoordRouteInstance)
		                
		        		// get secret leg data
		        		Map legDataSecret = calculateLegData(coordRouteInstance, startCoordRouteInstance)
		        		
		        		// calculate secretLegRatio
		        		if (legData.dis > 0) {
		        			coordRouteInstance.secretLegRatio = legDataSecret.dis / legData.dis
		        			coordRouteInstance.save()
		        		}

	        		}
	        		break
        	}

        	// calculate planProcedureTurn
        	if (lastType == CoordType.TP) {
                Map legData = calculateLegData(coordRouteInstance, startCoordRouteInstance2)
                if (lastLegDirection != null) {
                    def diffTrack = legData.dir - lastLegDirection
                    if (diffTrack < 0) {
                        diffTrack += 360
                    }
                    if (diffTrack >= 90 && diffTrack < 270) {
                        coordRouteInstance.planProcedureTurn = true
                        coordRouteInstance.save()
                    }
                }
                lastLegDirection = legData.dir
        	}
            switch (coordRouteInstance.type) {
            	case CoordType.SP:
            		startCoordRouteInstance2 = coordRouteInstance 
            		break
                case CoordType.TP:
                case CoordType.FP:
                    startCoordRouteInstance2 = coordRouteInstance 
		            break
            }
        	lastType = coordRouteInstance.type
        }
    }
    
    //--------------------------------------------------------------------------
    BigDecimal addMapDistance(lastMapMeasureDistance, addmeasuredistance)
    {
	    if (addmeasuredistance != null) {
	    	if (lastMapMeasureDistance != null) {
	    		lastMapMeasureDistance += addmeasuredistance
	    	} else {
	    		lastMapMeasureDistance = addmeasuredistance
	    	}
	    }
    }
    
    //--------------------------------------------------------------------------
    private String getMsg(String code, List args)
    {
        if (args) {
            return messageSource.getMessage(code, args.toArray(), null)
        } else {
            return messageSource.getMessage(code, null, null)
        }
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code)
    {
           return messageSource.getMessage(code, null, null)
    }
    
    //--------------------------------------------------------------------------
    void calulateSequence(Task taskInstance)
    {
    	// set viewpos for aircraft of user1 
        Test.findAllByTask(taskInstance).each { testInstance ->
        	if (testInstance.crew.aircraft) {
        		if (testInstance.crew.aircraft.user1 == testInstance.crew) {
        			testInstance.viewpos = 2000+testInstance.crew.tas
        		}
        	}
        }

        // set viewpos for aircraft of user2 
        Test.findAllByTask(taskInstance).each { testInstance ->
            if (testInstance.crew.aircraft) {
                if (testInstance.crew.aircraft.user2 == testInstance.crew) {
                    testInstance.viewpos = 1000+testInstance.crew.tas
                }
            }
        }

        // set viewpos for user without aircraft 
        Test.findAllByTask(taskInstance).each { testInstance ->
            if (!testInstance.crew.aircraft) {
                testInstance.viewpos = 0
            }
        }

        // set viewpos
        Test.findAllByTask(taskInstance,[sort:"viewpos",order:"desc"]).eachWithIndex { testInstance, i ->
            testInstance.viewpos = i
            testInstance.timeCalculated = false
            testInstance.save()
        }
    }
 
    //--------------------------------------------------------------------------
    void addTestingTime(Task taskInstance, Test testInstance)
    {
        if (testInstance.timeCalculated) {
            
            GregorianCalendar time = new GregorianCalendar() 
            time.setTime(testInstance.testingTime)
            
            // add testingTime
            time.add(Calendar.MINUTE, taskInstance.addTimeValue)
            testInstance.testingTime = time.getTime()
            
            // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
            calculateTimes(time, taskInstance, testInstance)
            testInstance.save()
            
            calulateCoordResult(testInstance)
            calulateTimetableWarnings(taskInstance)
        }
    }
  
    //--------------------------------------------------------------------------
    void subtractTestingTime(Task taskInstance, Test testInstance)
    {
        if (testInstance.timeCalculated) {
            
            GregorianCalendar time = new GregorianCalendar() 
            time.setTime(testInstance.testingTime)
            
            // subtract testingTime
            time.add(Calendar.MINUTE, -taskInstance.addTimeValue)
            testInstance.testingTime = time.getTime()
            
            // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
            calculateTimes(time, taskInstance, testInstance)
            testInstance.save()
            
            calulateCoordResult(testInstance)
            calulateTimetableWarnings(taskInstance)
        }
    }

    //--------------------------------------------------------------------------
    private void calulateTimetableWarnings(Task taskInstance)
    {
        Date first_date = Date.parse("HH:mm",taskInstance.firstTime)
        Date lastArrivalTime = first_date
        
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { testInstance ->
            
            // calculate arrivalTimeWarning by arrivalTime
            testInstance.arrivalTimeWarning = false
            if (lastArrivalTime > testInstance.arrivalTime) {
                testInstance.arrivalTimeWarning = true
            }
            lastArrivalTime = testInstance.arrivalTime
            
            // calculate takeoffTimeWarning by aircraft
            testInstance.takeoffTimeWarning = false
            boolean foundAircraft = false
            GregorianCalendar lastTakeoffTime = null
            Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { testInstance2 ->
                if (testInstance.crew.aircraft == testInstance2.crew.aircraft) {
                	if (testInstance == testInstance2) {
                		foundAircraft = true
                	}
					if (!foundAircraft) {
	                	lastTakeoffTime = new GregorianCalendar()
	                    lastTakeoffTime.setTime(testInstance2.arrivalTime)
	                    lastTakeoffTime.add(Calendar.MINUTE, taskInstance.minNextFlightDuration)
	                    lastTakeoffTime.add(Calendar.MINUTE, taskInstance.preparationDuration)
	                }
                }
            }
            if (lastTakeoffTime) {
                if (testInstance.takeoffTime < lastTakeoffTime.getTime()) {
                    testInstance.takeoffTimeWarning = true
                }
            }
            
            testInstance.save()
        }

    }
 
    //--------------------------------------------------------------------------
    void calulateTimetable(Task taskInstance)
    {
        println "calulateTimetable: ${taskInstance.name()}"
        
        Date first_date = Date.parse("HH:mm",taskInstance.firstTime)
        GregorianCalendar first_time = new GregorianCalendar() 
        first_time.setTime(first_date)

        GregorianCalendar start_time = new GregorianCalendar()
        start_time.set(Calendar.HOUR_OF_DAY, first_time.get(Calendar.HOUR_OF_DAY))
        start_time.set(Calendar.MINUTE,      first_time.get(Calendar.MINUTE))
        start_time.set(Calendar.SECOND,      0)

        BigDecimal lastTAS = 9000.0
        Date lastArrivalTime = first_date
        
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { testInstance ->
        	print "  ${testInstance.crew.name}..."
        	
            if (testInstance.crew.tas > lastTAS) { // faster aircraft
            	start_time.add(Calendar.MINUTE, taskInstance.takeoffIntervalFasterAircraft - taskInstance.takeoffIntervalNormal)
            }
            
            // calculate testingTime
            GregorianCalendar time = start_time.clone()
            testInstance.testingTime = time.getTime()
            
            // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
            calculateTimes(time, taskInstance, testInstance)
            
            // calculate arrivalTimeWarning by arrivalTime
            testInstance.arrivalTimeWarning = false
            if (lastArrivalTime > testInstance.arrivalTime) {
                testInstance.arrivalTimeWarning = true
            }
            lastArrivalTime = testInstance.arrivalTime
            
            // calculate takeoffTimeWarning by aircraft
            testInstance.takeoffTimeWarning = false
            boolean foundAircraft = false
            GregorianCalendar lastTakeoffTime = null
            Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { testInstance2 ->
	            if (testInstance.crew.aircraft == testInstance2.crew.aircraft) {
	                if (testInstance == testInstance2) {
	                    foundAircraft = true
	                }
	                if (!foundAircraft) {
	                    lastTakeoffTime = new GregorianCalendar()
	                    lastTakeoffTime.setTime(testInstance2.arrivalTime)
	                    lastTakeoffTime.add(Calendar.MINUTE, taskInstance.minNextFlightDuration)
	                    lastTakeoffTime.add(Calendar.MINUTE, taskInstance.preparationDuration)
	                }
	            }
            }
            if (lastTakeoffTime) {
                if (testInstance.takeoffTime < lastTakeoffTime.getTime()) {
                    testInstance.takeoffTimeWarning = true
                }
            }

            // next 
            start_time.add(Calendar.MINUTE, taskInstance.takeoffIntervalNormal)
            lastTAS = testInstance.crew.tas
            
            testInstance.timeCalculated = true
            testInstance.save()
            println "  Done."
        }

    }
 
    //--------------------------------------------------------------------------
    private void calculateTimes(GregorianCalendar time, Task taskInstance, Test testInstance) 
    {
        // calulate endTestingTime
        time.add(Calendar.MINUTE, taskInstance.planningTestDuration)
        testInstance.endTestingTime = time.getTime()
            
        // calulate takeoffTime
        time.add(Calendar.MINUTE, taskInstance.preparationDuration)
        testInstance.takeoffTime = time.getTime()
        
        // calulate startTime
        time.add(Calendar.MINUTE, taskInstance.risingDuration)
        testInstance.startTime = time.getTime()
        
        // calculate finishTime
        time.add(Calendar.SECOND, (3600 * getLegsPlanTime(testInstance)).toInteger() )
        testInstance.finishTime = time.getTime()
        GregorianCalendar finish_time = time.clone()
        
        // calculate maxLandingTime
        finish_time.add(Calendar.MINUTE, taskInstance.maxLandingDuration)
        testInstance.maxLandingTime = finish_time.getTime() 

        // calculate arrivalTime
        time.add(Calendar.MINUTE, taskInstance.parkingDuration)
        testInstance.arrivalTime = time.getTime()
        
    }
    
    //--------------------------------------------------------------------------
    private BigDecimal getLegsPlanTime(Test testInstance)
    {
        def legsTime = 0
        TestLegFlight.findAllByTest(testInstance).each { testLegFlightInstance ->
            legsTime += testLegFlightInstance.planLegTime
            if (testLegFlightInstance.planProcedureTurn) {
            	legsTime += testLegFlightInstance.planProcedureTurnDuration / 60
            }
        }
        return legsTime
    }

    //--------------------------------------------------------------------------
    void calulateTestLegFlights(Task taskInstance)
    {
        println "calulateTestLegFlights: ${taskInstance.name()}"
        
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { testInstance ->
            // remove all TestLegFlights
            TestLegFlight.findAllByTest(testInstance).each { testLegFlightInstance ->
                testLegFlightInstance.delete(flush:true)
            }

            // calculate TestLegFlights
            Integer lastTrueTrack = null
            RouteLegTest.findAllByRoute(testInstance?.flighttestwind?.flighttest?.route).each { routeLegTestInstance ->
                
                def testLegFlightInstance = new TestLegFlight()
                calculateLeg(testLegFlightInstance, routeLegTestInstance, testInstance.flighttestwind.wind, testInstance.crew.tas, testInstance.task.procedureTurnDuration, lastTrueTrack)
                testLegFlightInstance.test = testInstance
                testLegFlightInstance.save()

                lastTrueTrack = testLegFlightInstance.planTrueTrack
            }
        }
    }

    //--------------------------------------------------------------------------
    void calulateCoordResults(Task taskInstance)
    {
        println "calulateCoordResults: ${taskInstance.name()}"
        
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test testInstance ->
        	calulateCoordResult(testInstance)
        }
    }

    //--------------------------------------------------------------------------
    private void calulateCoordResult(Test testInstance)
    {
        // remove all coordResultInstances
        CoordResult.findAllByTest(testInstance).each { coordResultInstance ->
            coordResultInstance.delete(flush:true)
        }

        // create coordResultInstances
        int coordIndex = 0
        CoordRoute.findAllByRoute(testInstance?.flighttestwind?.flighttest?.route).each { coordRouteInstance ->
            def coordResultInstance = new CoordResult()
            coordResultInstance.type = coordRouteInstance.type
            coordResultInstance.titleNumber = coordRouteInstance.titleNumber
            coordResultInstance.mark = coordRouteInstance.mark
            coordResultInstance.latGrad = coordRouteInstance.latGrad
            coordResultInstance.latMinute = coordRouteInstance.latMinute
            coordResultInstance.latDirection = coordRouteInstance.latDirection
            coordResultInstance.lonGrad = coordRouteInstance.lonGrad
            coordResultInstance.lonMinute = coordRouteInstance.lonMinute
            coordResultInstance.lonDirection = coordRouteInstance.lonDirection
            coordResultInstance.altitude = coordRouteInstance.altitude
            coordResultInstance.gatewidth = coordRouteInstance.gatewidth
            coordResultInstance.planProcedureTurn = coordRouteInstance.planProcedureTurn
            switch (coordRouteInstance.type) {
                case CoordType.TO:
                    coordResultInstance.planCpTime = testInstance.takeoffTime
                    break
                case CoordType.SP:
                    coordResultInstance.planCpTime = testInstance.startTime
                    break
                case CoordType.LDG:
                    coordResultInstance.planCpTime = testInstance.maxLandingTime
                    break
                case CoordType.TP:
                case CoordType.FP:
                    coordIndex++
                    Date cpTime = testInstance.startTime
                    TestLegFlight.findAllByTest(testInstance).eachWithIndex { testLegFlightInstance, legIndex ->
                        cpTime = testLegFlightInstance.AddPlanLegTime(cpTime)
                        if (coordIndex == legIndex + 1) {
                            coordResultInstance.planCpTime = cpTime
                        }
                    }
                    break
                case CoordType.SECRET:
                    Date cpTime = testInstance.startTime
                    TestLegFlight.findAllByTest(testInstance).eachWithIndex { testLegFlightInstance, legIndex ->
                        if (coordIndex == legIndex) {
                            cpTime = testLegFlightInstance.AddPlanLegTime(cpTime,coordRouteInstance.secretLegRatio)
                            coordResultInstance.planCpTime = cpTime
                        } else {
                            cpTime = testLegFlightInstance.AddPlanLegTime(cpTime)
                        }
                    }
                    break
            }
            coordResultInstance.test = testInstance
            switch (coordRouteInstance.type) {
                case CoordType.TO:
                case CoordType.LDG:
                    // ignore Takeoff and Landing
                    break
                default:
                    coordResultInstance.save()
                    break
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private void calulateTestLegPlannings(Test testInstance, Route route)
    {
        println "calulateTestLegPlannings: ${testInstance.crew.name}"
        
        // remove all TestLegPlannings
        TestLegPlanning.findAllByTest(testInstance).each { testLegPlanningInstance ->
            testLegPlanningInstance.delete()
        }
        
        // calculate TestLegPlannings with results 
        Integer lastTrueTrack = null
        RouteLegTest.findAllByRoute(route).each { routeLegTestInstance ->
            def testLegPlanningInstance = new TestLegPlanning()
            calculateLeg(testLegPlanningInstance, routeLegTestInstance, testInstance.planningtesttask.wind, testInstance.crew.tas, testInstance.planningtesttask.planningtest.task.procedureTurnDuration, lastTrueTrack)
            testLegPlanningInstance.test = testInstance
            if (!testInstance.task.planningTestDistanceMeasure) {
                testLegPlanningInstance.resultTestDistance = testLegPlanningInstance.planTestDistance
            }
            if (!testInstance.task.planningTestDirectionMeasure) {
                testLegPlanningInstance.resultTrueTrack = testLegPlanningInstance.planTrueTrack
            }
            testLegPlanningInstance.save()
            lastTrueTrack = testLegPlanningInstance.planTrueTrack
        }
    }

    //--------------------------------------------------------------------------
    private void calculateLeg(TestLeg testLeg, RouteLeg routeLegInstance, Wind windInstance, BigDecimal valueTAS, int procedureTurnDuration, Integer lastTrueTrack) 
    {
       // save route data
       testLeg.planTestDistance = routeLegInstance.testDistance()
       testLeg.planTrueTrack = routeLegInstance.testTrueTrack()
       
       // calculate wind
       if (windInstance.speed) {
           def windDirection = windInstance.direction - 180
           def beta = windDirection - routeLegInstance.trueTrack
           def sinBeta = Math.sin(Math.toRadians(beta)) 
           def driftAngle = 0
           if (beta != 0 && beta != 180 && beta != -180) {
               driftAngle = Math.toDegrees(Math.asin(sinBeta*windInstance.speed/valueTAS))  
           }
           testLeg.planTrueHeading = routeLegInstance.trueTrack - driftAngle 
           def gamma = 180 + testLeg.planTrueHeading - windDirection
           def sinGamma = Math.sin(Math.toRadians(gamma))
           //println "Beta: $beta, sinBeta: $sinBeta, Gamma: $gamma, sinGamma: $sinGamma"
           if (beta == 0) {
        	   testLeg.planGroundSpeed = valueTAS + windInstance.speed
           } else if (beta == 180 || beta == -180) {
        	   testLeg.planGroundSpeed = valueTAS - windInstance.speed
           } else {
        	   testLeg.planGroundSpeed = valueTAS * sinGamma / sinBeta
           }
           if (testLeg.planTrueHeading < 0) {
        	   testLeg.planTrueHeading += 360
           }
       } else {
    	   testLeg.planTrueHeading = routeLegInstance.trueTrack
    	   testLeg.planGroundSpeed = valueTAS
       }
       testLeg.planLegTime = testLeg.planTestDistance / testLeg.planGroundSpeed 
       
       // calculate Procedure Turn
       testLeg.planProcedureTurn = false
       testLeg.planProcedureTurnDuration = 0
       if (lastTrueTrack != null) {
           def diffTrack = testLeg.planTrueTrack - lastTrueTrack
           if (diffTrack < 0) {
               diffTrack += 360
           }
           if (diffTrack >= 90 && diffTrack < 270) {
        	   testLeg.planProcedureTurn = true
        	   testLeg.planProcedureTurnDuration = procedureTurnDuration
           }
       }
    }

    //--------------------------------------------------------------------------
    void WritePDF(response,content) {
        byte[] b = content.toByteArray()
        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "attachment; filename=print.pdf")
        response.setContentLength(b.length)
        response.getOutputStream().write(b)
    }
    
    //--------------------------------------------------------------------------
    Map putAircraft(contest,registration,type,colour)
    {
        def p = [:]
        p.registration = registration
        p.type = type
        p.colour = colour
        return saveAircraft(p,contest.instance)
    }
    
    //--------------------------------------------------------------------------
    Map putContest(title,mapScale)
    {
        def p = [:]
        p.title = title
        p.mapScale = mapScale
        return saveContest(p)
    }
    
    //--------------------------------------------------------------------------
    Map putTask(contest,title)
    {
        def p = [:]
        p.title = title
        return saveTask(p,contest.instance)
    }
    
    //--------------------------------------------------------------------------
    Map putRoute(contest,title,mark)
    {
        def p = [:]
        p.title = title
        p.mark = mark
        return saveRoute(p,contest.instance)
    }

    //--------------------------------------------------------------------------
    Map putCoordRoute(route,type,titleNumber,mark,latDirection,latGrad,latMinute,lonDirection,lonGrad,lonMinute,altitude,gatewidth,mapmeasuredistance,mapmeasuretruetrack)
    {
        def p = [:]
        p.routeid = route.instance.id
        p.type = type
        if (titleNumber > 0) {
        	p.titleNumber = titleNumber
        }
        p.mark = mark
        p.latGrad = latGrad
        p.latMinute = latMinute
        p.latDirection = latDirection
        p.lonGrad = lonGrad
        p.lonMinute = lonMinute
        p.lonDirection = lonDirection
        p.altitude = altitude 
        p.gatewidth = gatewidth
        p.mapmeasuredistance = mapmeasuredistance
        p.mapdistance = calculateMapDistance(route.instance.contest,mapmeasuredistance)
        p.mapmeasuretruetrack = mapmeasuretruetrack 
        return saveCoordRoute(p)
    }
    
    //--------------------------------------------------------------------------
    Map putCrew(contest,name,country,registration,type,colour,tas)
    {
        def p = [:]
        p.name = name
        p.country = country
        p.registration = registration
        p.type = type
        p.colour = colour
        p.tas = tas
        return saveCrew(p,contest.instance)
    }
    
    //--------------------------------------------------------------------------
    Map putFlightTest(task,title,route)
    {
        def p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.route = route.instance
        p.direction = 0
        p.speed = 0
        return saveFlightTest(p)
    }
    
    //--------------------------------------------------------------------------
    Map putFlightTest(task,title,route,direction,speed)
    {
        def p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.route = route.instance
        p.direction = direction
        p.speed = speed
        return saveFlightTest(p)
    }
    
    //--------------------------------------------------------------------------
    Map putFlightTestWind(flighttest,direction,speed)
    {
        def p = [:]
        p.flighttestid = flighttest.instance.id
        p.direction = direction
        p.speed = speed
        return saveFlightTestWind(p)
    }

    //--------------------------------------------------------------------------
    Map putLandingTest(task,title)
    {
        def p = [:]
        p.taskid = task.instance.id
        p.title = title
        return saveLandingTest(p)
    }
    
    //--------------------------------------------------------------------------
    Map putLandingTestTask(landingtest,title)
    {
        def p = [:]
        p.landingtestid = landingtest.instance.id
        p.title = title
        return saveLandingTestTask(p)
    }
    
    //--------------------------------------------------------------------------
    Map putPlanningTest(task,title)
    {
        def p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.direction = 0
        p.speed = 0
        return savePlanningTest(p)
    }

    //--------------------------------------------------------------------------
    Map putPlanningTest(task,title,taskTitle,route,direction,speed)
    {
        def p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.taskTitle = taskTitle
        p.route = route.instance
        p.direction = direction
        p.speed = speed
        return savePlanningTest(p)
    }

    //--------------------------------------------------------------------------
    Map putPlanningTestTask(planningtest,title,route,direction,speed)
    {
        def p = [:]
        p.planningtestid = planningtest.instance.id
        p.title = title
        p.route = route.instance
        p.direction = direction
        p.speed = speed
        return savePlanningTestTask(p)
    }
    
    //--------------------------------------------------------------------------
    Map putSpecialTest(task,title)
    {
        def p = [:]
        p.taskid = task.instance.id
        p.title = title
        return saveSpecialTest(p)
    }
    
    //--------------------------------------------------------------------------
    Map putSpecialTestTask(specialtest,title)
    {
        def p = [:]
        p.specialtestid = specialtest.instance.id
        p.title = title
        return saveSpecialTestTask(p)
    }
    
    //--------------------------------------------------------------------------
    Map putResults(task,crew,flightTestPenalties,landingTestPenalties,specialTestPenalties)
    {
        Test testInstance = Test.findByTaskAndCrew(task.instance,crew.instance)
        def p = [:]
        p.id = testInstance.id
        p.flightTestPenalties = flightTestPenalties
        p.landingTestPenalties = landingTestPenalties
        p.specialTestPenalties = specialTestPenalties
        return updateresultsTest(p)
    }
    
    //--------------------------------------------------------------------------
    Map runcalculatesequenceTask(task)
    {
        def p = [:]
        p.id = task.instance.id 
        return calculatesequenceTask(p)
    }
    
    //--------------------------------------------------------------------------
    Map runcalculatetimetableTask(task)
    {
        def p = [:]
        p.id = task.instance.id 
        return calculatetimetableTask(p)
    }
    
    //--------------------------------------------------------------------------
    Map runcalculatepositionsTask(task)
    {
        def p = [:]
        p.id = task.instance.id 
        return calculatepositionsTask(p)
    }
}
