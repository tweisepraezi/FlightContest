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
            def contestDayInstance = new ContestDay()
            contestDayInstance.title = params.contestDayTitle
            contestDayInstance.idTitle = 1
            contestDayInstance.contest = contestInstance
            contestDayInstance.save()
            
            def contestDayTaskInstance = new ContestDayTask()
            contestDayTaskInstance.title = params.contestDayTaskTitle
            contestDayTaskInstance.idTitle = 1
            contestDayTaskInstance.contestday = contestDayInstance
            contestDayTaskInstance.save()
            
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
            	ContestDay.findAllByContest(contestInstance).each { contestDayInstance ->
            		contestDayInstance.delete()
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
    Map getContestDay(params)
    {
        def contestDayInstance = ContestDay.get(params.id)

        if (!contestDayInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contestday'),params.id])]
        }
        
        return ['instance':contestDayInstance]
    }

    //--------------------------------------------------------------------------
    Map updateContestDay(params)
    {
        def contestDayInstance = ContestDay.get(params.id)
        
        if (contestDayInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(contestDayInstance.version > version) {
                    contestDayInstance.errors.rejectValue("version", "contestDay.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':contestDayInstance]
                }
            }
            
            contestDayInstance.properties = params
            if(!contestDayInstance.hasErrors() && contestDayInstance.save()) {
                return ['instance':contestDayInstance,'saved':true,'message':getMsg('fc.updated',["${contestDayInstance.name()}"])]
            } else {
                return ['instance':contestDayInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contestday'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createContestDay(params)
    {
        def contestDayInstance = new ContestDay()
        contestDayInstance.properties = params
        return ['instance':contestDayInstance]
    }
    
    //--------------------------------------------------------------------------
    Map saveContestDay(params)
    {
        def contestDayInstance = new ContestDay(params)
        
        contestDayInstance.contest = Contest.get(params.contestid)
        contestDayInstance.idTitle = ContestDay.countByContest(contestDayInstance.contest) + 1
        
        if(!contestDayInstance.hasErrors() && contestDayInstance.save()) {
            return ['instance':contestDayInstance,'saved':true,'fromcontest':params.fromcontest,'message':getMsg('fc.created',["${contestDayInstance.name()}"])]
        } else {
            return ['instance':contestDayInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteContestDay(params)
    {
        def contestDayInstance = ContestDay.get(params.id)
        def contestInstance = contestDayInstance.contest
        
        if (contestDayInstance) {
            try {
                contestDayInstance.delete()
                
                ContestDay.findAllByContest(contestInstance).eachWithIndex { contestDayInstance2, index  -> 
                    contestDayInstance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${contestDayInstance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.contestday'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contestday'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getContestDayTask(params)
    {
        def contestDayTaskInstance = ContestDayTask.get(params.id)

        if (!contestDayTaskInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contestdaytask'),params.id])]
        }
        
        return ['instance':contestDayTaskInstance]
    }

    //--------------------------------------------------------------------------
    Map updateContestDayTask(params)
    {
        def contestDayTaskInstance = ContestDayTask.get(params.id)
        
        if (contestDayTaskInstance) {
            
            if(params.version) {
                def version = params.version.toLong()
                if(contestDayTaskInstance.version > version) {
                    contestDayTaskInstance.errors.rejectValue("version", "contestDayTask.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':contestDayTaskInstance]
                }
            }
            
            contestDayTaskInstance.properties = params
            if(!contestDayTaskInstance.hasErrors() && contestDayTaskInstance.save()) {
                return ['instance':contestDayTaskInstance,'saved':true,'message':getMsg('fc.updated',["${contestDayTaskInstance.name()}"])]
            } else {
                return ['instance':contestDayTaskInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contestdaytask'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createContestDayTask(params)
    {
        def contestDayTaskInstance = new ContestDayTask()
        contestDayTaskInstance.properties = params
        return ['instance':contestDayTaskInstance]
    }
    
    //--------------------------------------------------------------------------
    Map saveContestDayTask(params)
    {
        def contestDayTaskInstance = new ContestDayTask(params)
        
        contestDayTaskInstance.contestday = ContestDay.get(params.contestdayid)
        contestDayTaskInstance.idTitle = ContestDayTask.countByContestday(contestDayTaskInstance.contestday) + 1
        
        if(!contestDayTaskInstance.hasErrors() && contestDayTaskInstance.save()) {
            Crew.findAllByContest(contestDayTaskInstance.contestday.contest).eachWithIndex { crewInstance, i ->
                def testInstance = new Test()
                testInstance.crew = crewInstance
                testInstance.viewpos = i
                testInstance.contestdaytask = contestDayTaskInstance
                testInstance.timeCalculated = false
                testInstance.save()
            }
            
            return ['instance':contestDayTaskInstance,'saved':true,'fromcontestday':params.fromcontestday,'message':getMsg('fc.created',["${contestDayTaskInstance.name()}"])]
        } else {
            return ['instance':contestDayTaskInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteContestDayTask(params)
    {
        def contestDayTaskInstance = ContestDayTask.get(params.id)
        
        if (contestDayTaskInstance) {
            try {
            	// remove Tests
            	Test.findAllByContestdaytask(contestDayTaskInstance).each { testInstance ->
            		testInstance.delete()
            	}

                contestDayTaskInstance.delete()
                
                // correct idTitle of other contestdaytasks
                ContestDayTask.findAllByContestday(contestDayTaskInstance.contestday).eachWithIndex { contestDayTaskInstance2, index  -> 
                    contestDayTaskInstance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${contestDayTaskInstance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.contestdaytask'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contestdaytask'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map startplanningContestDayTask(params,contestInstance,lastContestDayTaskPlanning)
    {
    	def contestDayTaskInstance
        if (lastContestDayTaskPlanning) {
            contestDayTaskInstance = ContestDayTask.findById(lastContestDayTaskPlanning)
        }
        if (!contestDayTaskInstance) {
            ContestDay.findAllByContest(contestInstance).each { contestDayInstance ->
            	ContestDayTask.findAllByContestday(contestDayInstance).each {
            		if (!contestDayTaskInstance) {
            			contestDayTaskInstance = it
            		}
            	}
            }
        }
        if (contestDayTaskInstance) {
            return ['contestdaytaskid':contestDayTaskInstance.id]
        }
    }
    
    //--------------------------------------------------------------------------
    Map startresultsContestDayTask(params,contestInstance,lastContestDayTaskResults)
    {
        def contestDayTaskInstance
        if (lastContestDayTaskResults) {
            contestDayTaskInstance = ContestDayTask.findById(lastContestDayTaskResults)
        }
        if (!contestDayTaskInstance) {
            ContestDay.findAllByContest(contestInstance).each { contestDayInstance ->
                ContestDayTask.findAllByContestday(contestDayInstance).each {
                    if (!contestDayTaskInstance) {
                        contestDayTaskInstance = it
                    }
                }
            }
        }
        if (contestDayTaskInstance) {
            return ['contestdaytaskid':contestDayTaskInstance.id]
        }
    }
    
    //--------------------------------------------------------------------------
    Map selectallContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (contestdaytask.instance) {
            def selectedTestIDs = [selectedTestID:""]
            Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
                   selectedTestIDs["selectedTestID${testInstance.id}"] = "on"
            }
            contestdaytask.selectedtestids = selectedTestIDs
        }
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map assignplanningtesttaskContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        // PlanningTest exists?
        if (!contestdaytask.instance.planningtest) {
            contestdaytask.message = getMsg('fc.planningtest.notfound')
            contestdaytask.error = true
            return contestdaytask
        }

        // PlanningTestTask exists?
        if (!PlanningTestTask.countByPlanningtest(contestdaytask.instance.planningtest)) {
            contestdaytask.message = getMsg('fc.planningtesttask.notfound')
            contestdaytask.error = true
            return contestdaytask
        }

        // Multiple PlanningTestTasks?  
        if (PlanningTestTask.countByPlanningtest(contestdaytask.instance.planningtest) > 1) {
            def testInstanceIDs = ['']
            Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
                if (params["selectedTestID${testInstance.id}"] == "on") {
                    testInstanceIDs += testInstance.id
                }
            }
            contestdaytask.testinstanceids = testInstanceIDs
            return contestdaytask
        }

        // set single PlanningTestTask to all selected Tests
        PlanningTestTask planningTestTaskInstance = PlanningTestTask.findByPlanningtest(contestdaytask.instance.planningtest) 
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                testInstance.planningtesttask = planningTestTaskInstance
                calulateTestLegPlannings(testInstance)
                testInstance.save()
            }
        }
        
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    void putplanningtesttaskContestDayTask(contestdaytask,planningtesttask)
    {
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            testInstance.planningtesttask = planningtesttask.instance
            calulateTestLegPlannings(testInstance)
            testInstance.save()
        }
    }
    
    //--------------------------------------------------------------------------
    void putplanningresultsContestDayTask(contestdaytask,results)
    {
    	Test.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).eachWithIndex { testInstance, i ->
    		TestLegPlanning.findAllByTest(testInstance).eachWithIndex { testLegPlanningInstance, j  ->
        		testLegPlanningInstance.resultTrueHeading = results[i].givenValues[j].trueHeading
        		testLegPlanningInstance.resultLegTimeInput = results[i].givenValues[j].legTime
        		calculateLegPlanningInstance(testLegPlanningInstance)
        		testLegPlanningInstance.save()
        	}
            testInstance.planningTestTooLate = results[i].givenTooLate
            testInstance.planningTestExitRoomTooLate = results[i].exitRoomTooLate
    		calculateTestPenalties(testInstance)
           	testInstance.planningTestComplete = testInstance.planningTestLegComplete && results[i].testComplete
    		testInstance.save()
        }
    }

    //--------------------------------------------------------------------------
    Map setplanningtesttaskContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (contestdaytask.instance) {
            def planningTestTaskInstance = PlanningTestTask.get(params.planningtesttask.id)
            params.testInstanceIDs.each { testID ->
                if (testID) {
                    def testInstance = Test.get(testID)
                    testInstance.planningtesttask = planningTestTaskInstance 
                    calulateTestLegPlannings(testInstance)
                    testInstance.save()
                }
            }
            contestdaytask.message = getMsg('fc.contestdaytask.selectplanningtesttask.assigned',[planningTestTaskInstance.name()])
        }
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map assignflighttestwindContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        // FlightTest exists?
        if (!contestdaytask.instance.flighttest) {
            contestdaytask.message = getMsg('fc.flighttest.notfound')
            contestdaytask.error = true
            return contestdaytask
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(contestdaytask.instance.flighttest)) {
            contestdaytask.message = getMsg('fc.flighttestwind.notfound')
            contestdaytask.error = true
            return contestdaytask
        }

        // Multiple FlightTestWinds?  
        if (FlightTestWind.countByFlighttest(contestdaytask.instance.flighttest) > 1) {
            def testInstanceIDs = ['']
            Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
                if (params["selectedTestID${testInstance.id}"] == "on") {
                    testInstanceIDs += testInstance.id
                }
            }
            contestdaytask.testinstanceids = testInstanceIDs
            return contestdaytask
        }

        // set single FlightTestWind to all selected Tests
        def flightTestWindInstance = FlightTestWind.findByFlighttest(contestdaytask.instance.flighttest)
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                testInstance.flighttestwind = flightTestWindInstance
                testInstance.save()
            }
        }

        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    void putflighttestwindContestDayTask(contestdaytask,flighttestwind)
    {
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            testInstance.flighttestwind = flighttestwind.instance
            testInstance.save()
        }
    }
    
    //--------------------------------------------------------------------------
    Map setflighttestwindContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (contestdaytask.instance) {
            def flightTestWindInstance = FlightTestWind.get(params.flighttestwind.id)
            params.testInstanceIDs.each { testID ->
                if (testID) {
                    def testInstance = Test.get(testID)
                    testInstance.flighttestwind = flightTestWindInstance
                    testInstance.timeCalculated = false
                    testInstance.save()
                }
            }
            contestdaytask.message = getMsg('fc.contestdaytask.selectflighttestwind.assigned',[flightTestWindInstance.wind.name()])
        }
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map calculatesequenceContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        /*
        // Have all crews an aircraft?
        def call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (!testInstance.crew.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.aircraft.notassigned')
            contestdaytask.error = true
            return contestdaytask
        }
        */

        calulateSequence(contestdaytask.instance)
        
        contestdaytask.message = getMsg('fc.test.sequence.calculated')        
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map moveupContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        def borderreached = false
        def notmovable = false
        def off2on = false
        def on2off = false
        Test.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { testInstance ->
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
               //contestdaytask.message = getMsg('fc.test.moveborderreached')
               contestdaytask.borderreached = true
            return contestdaytask
        }
        if (notmovable) {
            contestdaytask.message = getMsg('fc.test.notmovable')
            contestdaytask.error = true
            return contestdaytask
        }
        
        def movenum = 0
        def movefirstpos = -1
        def selectedTestIDs = [selectedTestID:""]
        borderreached = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
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
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] != "on") {
                if (testInstance.viewpos >= movefirstpos && testInstance.viewpos < movefirstpos + movenum) {
                    testInstance.viewpos += movenum
                    testInstance.timeCalculated = false
                    testInstance.save()
                }
            }
        }

        if (!borderreached) {
            contestdaytask.selectedtestids = selectedTestIDs
        }
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map movedownContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        def borderreached = false
        def notmovable = false
        def off2on = false
        def on2off = false
        Test.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                if (testInstance.viewpos + 1 == Crew.countByContest(contestdaytask.instance.contest)) {
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
               //contestdaytask.message = getMsg('fc.test.moveborderreached')
            contestdaytask.borderreached = true
            return contestdaytask
        }
        if (notmovable) {
            contestdaytask.message = getMsg('fc.test.notmovable')
            contestdaytask.error = true
            return contestdaytask
        }
        
        def movenum = 0
        def movefirstpos = -1
        def selectedTestIDs = [selectedTestID:""]
        borderreached = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                testInstance.viewpos++
                testInstance.timeCalculated = false
                testInstance.save()
                selectedTestIDs["selectedTestID${testInstance.id}"] = "on"
                   if (testInstance.viewpos + 1 == Crew.countByContest(contestdaytask.instance.contest)) {
                       borderreached = true
                   }
                movenum++
                if (movefirstpos == -1 || testInstance.viewpos < movefirstpos) {
                    movefirstpos = testInstance.viewpos
                }
            }
        }
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] != "on") {
                if (testInstance.viewpos >= movefirstpos && testInstance.viewpos < movefirstpos + movenum) {
                    testInstance.viewpos -= movenum
                    testInstance.timeCalculated = false
                    testInstance.save()
                }
            }
        }

        if (!borderreached) {
            contestdaytask.selectedtestids = selectedTestIDs
        }
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map calculatetimetableContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        // FlightTest exists?
        if (!contestdaytask.instance.flighttest) {
            contestdaytask.message = getMsg('fc.flighttest.notfound')
               contestdaytask.error = true
            return contestdaytask
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(contestdaytask.instance.flighttest)) {
            contestdaytask.message = getMsg('fc.flighttestwind.notfound')
               contestdaytask.error = true
            return contestdaytask
        }
        
        // FlightTestWind assigned to all crews?
        def call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (!testInstance.flighttestwind) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.flighttestwind.notassigned')
            contestdaytask.error = true
            return contestdaytask
        }

        /*
        // Have all crews an aircraft?
        call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (!testInstance.crew.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.aircraft.notassigned')
            contestdaytask.error = true
            return contestdaytask
        }
        */

        calulateTestLegFlights(contestdaytask.instance)
        calulateTimetable(contestdaytask.instance)
        
        contestdaytask.message = getMsg('fc.test.timetable.calculated')        
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map timeaddContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        def selectedTestIDs = [selectedTestID:""]
        Test.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                addTestingTime(contestdaytask.instance,testInstance)
                selectedTestIDs["selectedTestID${testInstance.id}"] = "on"
            }
        }
        contestdaytask.selectedtestids = selectedTestIDs
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map timesubtractContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        def selectedTestIDs = [selectedTestID:""]
        Test.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { testInstance ->
            if (params["selectedTestID${testInstance.id}"] == "on") {
                subtractTestingTime(contestdaytask.instance,testInstance)
                selectedTestIDs["selectedTestID${testInstance.id}"] = "on"
            }
        }
        contestdaytask.selectedtestids = selectedTestIDs
           return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map printtimetableContestDayTask(params,printparams)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        // FlightTest exists?
        if (!contestdaytask.instance.flighttest) {
            contestdaytask.message = getMsg('fc.flighttest.notfound')
               contestdaytask.error = true
            return contestdaytask
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(contestdaytask.instance.flighttest)) {
            contestdaytask.message = getMsg('fc.flighttestwind.notfound')
               contestdaytask.error = true
            return contestdaytask
        }
        
        // FlightTestWind assigned to all crews?
        def call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (!testInstance.flighttestwind) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.flighttestwind.notassigned')
            contestdaytask.error = true
            return contestdaytask
        }

        // Have all crews an aircraft?
        call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (!testInstance.crew.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.aircraft.notassigned')
            contestdaytask.error = true
            return contestdaytask
        }

        // Timetable calculated?  
        call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (!testInstance.timeCalculated) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.test.timetable.newcalculate')
            contestdaytask.error = true
            return contestdaytask
        }        
        
        // Warnings?  
        call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (testInstance.arrivalTimeWarning || testInstance.takeoffTimeWarning) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.test.flightplan.resolvewarnings')
            contestdaytask.error = true
            return contestdaytask
        }        
        
        // Print timetable
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            def url = "${printparams.baseuri}/contestDayTask/timetableprintable/${contestdaytask.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            contestdaytask.content = content 
        }
        catch (Throwable e) {
            contestdaytask.message = getMsg('fc.test.timetable.printerror',["$e"])
            contestdaytask.error = true
        }
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map printflightplansContestDayTask(params,printparams)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        // FlightTest exists?
        if (!contestdaytask.instance.flighttest) {
            contestdaytask.message = getMsg('fc.flighttest.notfound')
            contestdaytask.error = true
            return contestdaytask
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(contestdaytask.instance.flighttest)) {
            contestdaytask.message = getMsg('fc.flighttestwind.notfound')
            contestdaytask.error = true
            return contestdaytask
        }
        
        // FlightTestWind assigned to all crews?
        def call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (!testInstance.flighttestwind) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.flighttestwind.notassigned')
            contestdaytask.error = true
            return contestdaytask
        }

        // Have all crews an aircraft?
        call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (!testInstance.crew.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.aircraft.notassigned')
            contestdaytask.error = true
            return contestdaytask
        }

        // Timetable calculated?  
        call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (!testInstance.timeCalculated) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.test.timetable.newcalculate')
            contestdaytask.error = true
            return contestdaytask
        }        
        
        // Warnings?  
        call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (testInstance.arrivalTimeWarning || testInstance.takeoffTimeWarning) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.test.flightplan.resolvewarnings')
            contestdaytask.error = true
            return contestdaytask
        }        
        
        // Print flightplans
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Test.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { testInstance ->
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

            contestdaytask.content = content 
        }
        catch (Throwable e) {
            contestdaytask.message = getMsg('fc.test.flightplan.printerror',["$e"])
            contestdaytask.error = true
        }
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map printplanningtasksContestDayTask(params,printparams)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        // PlanningTest exists?
        if (!contestdaytask.instance.planningtest) {
            contestdaytask.message = getMsg('fc.planningtest.notfound')
            contestdaytask.error = true
            return contestdaytask
        }

        // PlanningTestTask exists?
        if (!PlanningTestTask.countByPlanningtest(contestdaytask.instance.planningtest)) {
            contestdaytask.message = getMsg('fc.planningtesttask.notfound')
               contestdaytask.error = true
            return contestdaytask
        }

        // PlanningTestTask assigned to all crews?
        def call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (!testInstance.planningtesttask) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.planningtesttask.notassigned')
            contestdaytask.error = true
            return contestdaytask
        }

        // Print PlanningTasks
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Test.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { testInstance ->
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
            contestdaytask.content = content
        }
        catch (Throwable e) {
            contestdaytask.message = getMsg('fc.planningtesttask.printerror',["$e"])
            contestdaytask.error = true
        }
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map calculatepositionsContestDayTask(params)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        // calulate position
        int actPenalty = -1
        int maxPosition = Test.countByContestdaytask(contestdaytask.instance)
        for (int actPosition = 1; actPosition <= maxPosition; actPosition++) {
            
            // search lowest penalty
            int minPenalty = 100000
            Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
                if (testInstance.testPenalties > actPenalty) {
                    if (testInstance.testPenalties < minPenalty) {
                        minPenalty = testInstance.testPenalties 
                    }
                }
            }
            actPenalty = minPenalty 
            
            // set position
            int setPositionNum = -1
            Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
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
        
        contestdaytask.message = getMsg('fc.test.results.positions.calculated')        
        return contestdaytask
    }
    
    //--------------------------------------------------------------------------
    Map printresultsContestDayTask(params,printparams)
    {
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        // Positions calculated?  
        boolean call_return = false
        Test.findAllByContestdaytask(contestdaytask.instance).each { testInstance ->
            if (!testInstance.positionContestDay) {
                call_return = true
            }
        }
        if (call_return) {
            contestdaytask.message = getMsg('fc.test.results.positions.newcalculate')
            contestdaytask.error = true
            return contestdaytask
        }        
        
        // Print positions
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            def url = "${printparams.baseuri}/contestDayTask/positionsprintable/${contestdaytask.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            contestdaytask.content = content 
        }
        catch (Throwable e) {
            contestdaytask.message = getMsg('fc.test.timetable.printerror',["$e"])
            contestdaytask.error = true
        }
        return contestdaytask
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
	        
	        if(!routeInstance.hasErrors() && routeInstance.save()) {
                def aflosRouteDefsInstances = AflosRouteDefs.findAllByRoutename(aflosRouteNamesInstance)
                def lastRouteCoordInstance
                def lastRouteCoordTestInstance
                def tpNum = 0
                def tcNum = 0
                aflosRouteDefsInstances.each { aflosRouteDefsInstance ->
                	def routeCoordInstance = new RouteCoord()
                    
                    // set latitude
                    aflosRouteDefsInstance.latitude.split().eachWithIndex{ latValue, i ->
                		switch(i) {
                			case 0: 
                				routeCoordInstance.latDirection = latValue
                				break
                			case 1:
                				routeCoordInstance.latGrad = latValue.toInteger()
                				break
                			case 2:
                				routeCoordInstance.latMinute = latValue.replace(',','.').toBigDecimal()
                				break
                		}
                	}
                    
                    // set longitude
                    aflosRouteDefsInstance.longitude.split().eachWithIndex{ lonValue, i ->
                        switch(i) {
                            case 0: 
                                routeCoordInstance.lonDirection = lonValue
                                break
                            case 1:
                                routeCoordInstance.lonGrad = lonValue.toInteger()
                                break
                            case 2:
                                routeCoordInstance.lonMinute = lonValue.replace(',','.').toBigDecimal()
                                break
                        }
                    }
    
                    // set type and title
                    routeCoordInstance.type = RouteCoordType.UNKNOWN
                    RouteCoordType.each { type ->
                    	if (routeCoordInstance.type == RouteCoordType.UNKNOWN) {
	                    	if (aflosRouteDefsInstance.mark.startsWith(type.aflosMark)) {
	                    		if (type.aflosGateWidth == 0 || type.aflosGateWidth == aflosRouteDefsInstance.gatewidth) {
		                    		routeCoordInstance.type = type
		                    		switch (type) {
		                    			case RouteCoordType.TP:
		                    				tpNum++
		                    				routeCoordInstance.titleNumber = tpNum
		                    				break
		                    			case RouteCoordType.SECRET:
		                    				tcNum++
	                                        routeCoordInstance.titleNumber = tcNum
		                    				break
		                    		}
	                    		}
	                    	}
                    	}
                    }
                    
                    // set other
                    routeCoordInstance.altitude = aflosRouteDefsInstance.altitude.toInteger()
                    routeCoordInstance.mark = aflosRouteDefsInstance.mark
                    routeCoordInstance.gatewidth = aflosRouteDefsInstance.gatewidth
                    routeCoordInstance.route = routeInstance
                    
                    routeCoordInstance.save()
                    
                    newLeg(routeCoordInstance.route, routeCoordInstance, lastRouteCoordInstance, lastRouteCoordTestInstance, 0)
                    lastRouteCoordInstance = routeCoordInstance
                    switch (routeCoordInstance.type) {
	                    case RouteCoordType.SP:
	                    case RouteCoordType.TP:
	                    case RouteCoordType.FP:
	                        lastRouteCoordTestInstance = routeCoordInstance
	                        break
	                }
                }
                return ['instance':routeInstance,'saved':true,'message':getMsg('fc.imported',["${routeInstance.name()}"])]
	        } else {
	            return ['error':true,'message':getMsg('fc.notimported',["${aflosRouteNamesInstance.name}"])]
	        }
        } else {
            return ['error':true,'message':getMsg('fc.notimported',["${params.aflosroutenames.id}"])]
    	}
    }
    
    //--------------------------------------------------------------------------
    Map getRouteCoord(params)
    {
        def routeCoordInstance = RouteCoord.get(params.id)

        if (!routeCoordInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.routecoord'),params.id])]
        }
        
        return ['instance':routeCoordInstance]
    }

    //--------------------------------------------------------------------------
    Map updateRouteCoord(params)
    {
        def routeCoordInstance = RouteCoord.get(params.id)
        
        if (routeCoordInstance) {

            if(params.version) {
                def version = params.version.toLong()
                if(routeCoordInstance.version > version) {
                    routeCoordInstance.errors.rejectValue("version", "routeCoord.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':routeCoordInstance]
                }
            }
            
            routeCoordInstance.properties = params
            
            if(!routeCoordInstance.hasErrors() && routeCoordInstance.save()) {
                calculateRouteLegs(routeCoordInstance.route)
                return ['instance':routeCoordInstance,'saved':true,'message':getMsg('fc.updated',["${routeCoordInstance.name()}"])]
            } else {
                return ['instance':routeCoordInstance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.routecoord'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createRouteCoord(params)
    {
        def lastRouteCoordInstance = RouteCoord.findByRoute(Route.get(params.routeid),[sort:"id", order:"desc"])
        def routeCoordInstance = new RouteCoord()
    
        routeCoordInstance.properties = params
    
        if (params.secret) {
            routeCoordInstance.type = RouteCoordType.SECRET
            if (lastRouteCoordInstance) {
                switch (lastRouteCoordInstance.type) {
                	case RouteCoordType.SP:
                    case RouteCoordType.TP:
                    case RouteCoordType.SECRET:
                    	routeCoordInstance.titleNumber = findNextTitleNumber(lastRouteCoordInstance.route,RouteCoordType.SECRET)
                    	break
                    default:
                        return ['error':true,'message':getMsg('fc.routecoord.addsecret.notallowed')]
                }
            } else {
                return ['error':true,'message':getMsg('fc.routecoord.addsecret.notallowed')]
            }
        } else {
	        if (lastRouteCoordInstance) {
	        	switch (lastRouteCoordInstance.type) {
	        		case RouteCoordType.TO:
	        			routeCoordInstance.type = RouteCoordType.SP
	        			break
	        		case RouteCoordType.SP:
	                    routeCoordInstance.type = RouteCoordType.TP
	                    break
	        		case RouteCoordType.TP:
                    case RouteCoordType.SECRET:
	                    routeCoordInstance.type = RouteCoordType.TP
	                    routeCoordInstance.titleNumber = findNextTitleNumber(lastRouteCoordInstance.route,RouteCoordType.TP) 
	                    break
	                case RouteCoordType.FP:
	                    routeCoordInstance.type = RouteCoordType.LDG
	                    break
	                case RouteCoordType.LDG:
	                	return ['error':true,'message':getMsg('fc.routecoord.add.notallowed')]
	        	}
	        }
        }
        
        if (lastRouteCoordInstance) {
            routeCoordInstance.latGrad = lastRouteCoordInstance.latGrad
            routeCoordInstance.latMinute = lastRouteCoordInstance.latMinute
            routeCoordInstance.latDirection = lastRouteCoordInstance.latDirection
            routeCoordInstance.lonGrad = lastRouteCoordInstance.lonGrad
            routeCoordInstance.lonMinute = lastRouteCoordInstance.lonMinute
            routeCoordInstance.lonDirection = lastRouteCoordInstance.lonDirection
        }
        
        return ['instance':routeCoordInstance]
    }

    
    //--------------------------------------------------------------------------
    int findNextTitleNumber(Route route, RouteCoordType type)
    {
    	int titleNumber = 0
        RouteCoord.findAllByRoute(route,[sort:"id", order:"desc"]).each { routeCoordInstance ->
        	if (!titleNumber) {
	        	if (routeCoordInstance.type == type) {
	        		titleNumber = routeCoordInstance.titleNumber
	        	}
        	}
        }
    	return titleNumber + 1
    }
    
    //--------------------------------------------------------------------------
    Map saveRouteCoord(params)
    {
    	def lastRouteCoordInstance = RouteCoord.findByRoute(Route.get(params.routeid),[sort:"id", order:"desc"])
    	BigDecimal lastMapMeasureDistance = 0
        
    	def lastRouteCoordTestInstance 
        RouteCoord.findAllByRoute(Route.get(params.routeid),[sort:"id", order:"desc"]).each { routeCoordInstance ->
			if (!lastRouteCoordTestInstance) {
		        switch (routeCoordInstance.type) {
			        case RouteCoordType.SP:
			        case RouteCoordType.TP:
			        case RouteCoordType.FP:
			        	lastRouteCoordTestInstance = routeCoordInstance
			            break
			        default:
		                lastMapMeasureDistance += routeCoordInstance.mapmeasuredistance
		                break
			    }
        	}
        }
        
    	def routeCoordInstance = new RouteCoord(params)
        
        routeCoordInstance.route = Route.get(params.routeid)
        
        if(!routeCoordInstance.hasErrors() && routeCoordInstance.save()) {
        	lastMapMeasureDistance += routeCoordInstance.mapmeasuredistance 
        	newLeg(routeCoordInstance.route, routeCoordInstance, lastRouteCoordInstance, lastRouteCoordTestInstance, lastMapMeasureDistance)
            return ['instance':routeCoordInstance,'saved':true,'message':getMsg('fc.created',["${routeCoordInstance.name()}"])]
        } else {
            return ['instance':routeCoordInstance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteRouteCoord(params)
    {
        def routeCoordInstance = RouteCoord.get(params.id)
        
        if (routeCoordInstance) {
            try {
                def routeInstance = routeCoordInstance.route
                removeAllRouteLegs(routeInstance)
                routeCoordInstance.delete()
                calculateRouteLegs(routeInstance)
                return ['deleted':true,'message':getMsg('fc.deleted',["${routeCoordInstance.name()}"]),'routeid':routeInstance.id]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.routecoord'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.routecoord'),params.id])]
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
            return ['message':getMsg('fc.notfound',[getMsg('fc.routecoord'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    void calculateRouteLegCoordMapDistances(routeLegCoordInstance)
    {
    	println "calculateRouteLegCoordMapDistances $routeLegCoordInstance"
    	
    	Contest contestInstance = routeLegCoordInstance.route.contest
    		
    	// calculate mapdistance
    	if (!routeLegCoordInstance.mapmeasuredistance) {
            routeLegCoordInstance.mapmeasuredistance = 0
    	}
    	routeLegCoordInstance.mapdistance = calculateMapDistance(contestInstance,routeLegCoordInstance.mapmeasuredistance)
    	
    	// search routeCoord for routeLegCoordInstance
    	def foundRouteCoord
    	RouteLegCoord.findAllByRoute(routeLegCoordInstance.route).eachWithIndex { routeLeg, i -> 
    		if (routeLeg == routeLegCoordInstance) {
    			RouteCoord.findAllByRoute(routeLegCoordInstance.route).eachWithIndex { routeCoord, j ->  
    				if (i + 1 == j) {
    					foundRouteCoord = routeCoord
    				}
    			}
    		}
    	}

        // save mapdistance to routeCoord
    	if (foundRouteCoord) {
    		foundRouteCoord.mapmeasuredistance = routeLegCoordInstance.mapmeasuredistance
			foundRouteCoord.mapdistance = routeLegCoordInstance.mapdistance
			foundRouteCoord.save()
    	}
        
        // calculate mapdistance in testlegs
        RouteCoord lastRouteCoordInstance
        RouteCoord lastRouteCoordTestInstance
        BigDecimal lastMapMeasureDistance = 0
        int testlegpos = 0
        RouteCoord.findAllByRoute(routeLegCoordInstance.route).each { routeCoordInstance -> 
            lastMapMeasureDistance += routeCoordInstance.mapmeasuredistance
            if (lastRouteCoordInstance && lastRouteCoordTestInstance) {
                if ( (lastRouteCoordTestInstance.type == RouteCoordType.SP && routeCoordInstance.type == RouteCoordType.TP) ||
                     (lastRouteCoordTestInstance.type == RouteCoordType.TP && routeCoordInstance.type == RouteCoordType.TP) ||
                     (lastRouteCoordTestInstance.type == RouteCoordType.TP && routeCoordInstance.type == RouteCoordType.FP) ) 
                {
                	RouteLegTest.findAllByRoute(routeLegCoordInstance.route).eachWithIndex { testLeg, i ->
                		if (i == testlegpos) {
                			testLeg.mapmeasuredistance = lastMapMeasureDistance 
                			testLeg.mapdistance = calculateMapDistance(contestInstance,lastMapMeasureDistance)
                			testLeg.save()
                		}
                	}
                    testlegpos++
                }
            }
            lastRouteCoordInstance = routeCoordInstance
            switch (routeCoordInstance.type) {
                case RouteCoordType.SP:
                case RouteCoordType.TP:
                case RouteCoordType.FP:
                    lastRouteCoordTestInstance = routeCoordInstance
                    lastMapMeasureDistance = 0
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

            ContestDay.findAllByContest(contestInstance).each { contestDayInstance ->
	            ContestDayTask.findAllByContestday(contestDayInstance).each { contestDayTaskInstance ->
	                def testInstance = new Test()
	                testInstance.crew = crewInstance
	                testInstance.viewpos = Crew.countByContest(contestInstance) - 1
	                testInstance.contestdaytask = contestDayTaskInstance
	                testInstance.timeCalculated = false
	                testInstance.save()
	            }
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

        test.instance.planningTestTooLate = true
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

        test.instance.planningTestTooLate = false
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
    void calculateTestPenalties(Test testInstance)
    {
    	print "calculateTestPenalties $testInstance.crew.name..."
    	
    	Contest contestInstance = testInstance.contestdaytask.contestday.contest 
    	
    	testInstance.planningTestLegPenalties = 0
        testInstance.planningTestLegComplete = false
        if (TestLegPlanning.findByTest(testInstance)) {
        	testInstance.planningTestLegComplete = true
        }
    	TestLegPlanning.findAllByTest(testInstance).each { testLegPlanning ->
    		if (testLegPlanning.resultEntered) {
    			testInstance.planningTestLegPenalties += testLegPlanning.penaltyTrueHeading
    			testInstance.planningTestLegPenalties += testLegPlanning.penaltyLegTime
    		} else {
    			testInstance.planningTestLegComplete = false
    		}
    	}
        if (testInstance.planningTestLegPenalties > contestInstance.planningTestMaxPoints) {
            testInstance.planningTestLegPenalties = contestInstance.planningTestMaxPoints
        }
    	testInstance.planningTestPenalties = testInstance.planningTestLegPenalties
    	if (testInstance.planningTestTooLate) {
    		testInstance.planningTestPenalties += contestInstance.planningTestPlanTooLatePoints
    	}
    	if (testInstance.planningTestExitRoomTooLate) {
    		testInstance.planningTestPenalties += contestInstance.planningTestExitRoomTooLatePoints
    	}
    	
    	testInstance.testPenalties = testInstance.planningTestPenalties +
    	                             testInstance.flightTestPenalties +
    	                             testInstance.landingTestPenalties +
    	                             testInstance.specialTestPenalties

    	println "  $testInstance.testPenalties"
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
            
	        Test.findAllByContestdaytask(flightTestInstance.contestdaytask).each { testInstance ->
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
                    'fromlistplanning':params.fromlistplanning,'fromcontestdaytask':params.fromcontestdaytask,
                    'contestdaytaskid':params.contestdaytask.id]
        }
         
        def flightTestInstance = new FlightTest()
        flightTestInstance.properties = params
        return ['instance':flightTestInstance]
    }

    
    //--------------------------------------------------------------------------
    Map saveFlightTest(params)
    {
        def flightTestInstance = new FlightTest(params)
        
        flightTestInstance.contestdaytask = ContestDayTask.get( params.contestdaytaskid )
        
        if (!flightTestInstance.direction) {
        	flightTestInstance.direction = 0
        }
        if (!flightTestInstance.speed) {
        	flightTestInstance.speed = 0
        }
        
        if(!flightTestInstance.hasErrors() && flightTestInstance.save()) {

            def contestDayTaskInstance = ContestDayTask.get( params.contestdaytaskid )
            contestDayTaskInstance.flighttest = flightTestInstance
            contestDayTaskInstance.save()

            def windInstance = new Wind(direction:flightTestInstance.direction,speed:flightTestInstance.speed)
            windInstance.save()
            
            def flightTestWindInstance = new FlightTestWind(params)
            flightTestWindInstance.wind = windInstance
            flightTestWindInstance.flighttest = flightTestInstance
            flightTestWindInstance.save()

            return ['instance':flightTestInstance,'saved':true,'message':getMsg('fc.created',["${flightTestInstance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,'fromcontestdaytask':params.fromcontestdaytask,
                    'contestdaytaskid':contestDayTaskInstance.id]
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
                def contestDayTaskInstance = ContestDayTask.get( flightTestInstance.contestdaytask.id )
                contestDayTaskInstance.flighttest = null
                contestDayTaskInstance.save()
                
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
                    'contestdaytaskid':flightTestWindInstance.flighttest.contestdaytask.id,
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
        
        landingTestInstance.contestdaytask = ContestDayTask.get( params.contestdaytaskid )
        
        if(!landingTestInstance.hasErrors() && landingTestInstance.save()) {

            def contestDayTaskInstance = ContestDayTask.get( params.contestdaytaskid )
            contestDayTaskInstance.landingtest = landingTestInstance
            contestDayTaskInstance.save()
            
            return ['instance':landingTestInstance,'saved':true,'message':getMsg('fc.created',["${landingTestInstance.name()}"]),
                    'fromcontestdaytask':params.fromcontestdaytask,'contestdaytaskid':contestDayTaskInstance.id]
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
                def contestDayTaskInstance = ContestDayTask.get( landingTestInstance.contestdaytask.id )
                contestDayTaskInstance.landingtest = null
                contestDayTaskInstance.save()
                
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
                    'fromlistplanning':params.fromlistplanning,'fromcontestdaytask':params.fromcontestdaytask,
                    'contestdaytaskid':params.contestdaytask.id]
        }
         
        def planningTestInstance = new PlanningTest()
        planningTestInstance.properties = params
        return ['instance':planningTestInstance]
    }

    
    //--------------------------------------------------------------------------
    Map savePlanningTest(params)
    {
        def planningTestInstance = new PlanningTest(params)
        
        planningTestInstance.contestdaytask = ContestDayTask.get( params.contestdaytaskid )
        
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
            
            def contestDayTaskInstance = ContestDayTask.get( params.contestdaytaskid )
            contestDayTaskInstance.planningtest = planningTestInstance
            contestDayTaskInstance.save()
            
            return ['instance':planningTestInstance,'saved':true,'message':getMsg('fc.created',["${planningTestInstance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,'fromcontestdaytask':params.fromcontestdaytask,
                    'contestdaytaskid':contestDayTaskInstance.id]
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
                def contestDayTaskInstance = ContestDayTask.get( planningTestInstance.contestdaytask.id )
                contestDayTaskInstance.planningtest = null
                contestDayTaskInstance.save()
                
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

            Test.findAllByContestdaytask(planningTestTaskInstance.planningtest.contestdaytask).each { testInstance ->
                calulateTestLegPlannings(testInstance)
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
                    'contestdaytaskid':planningTestTaskInstance.planningtest.contestdaytask.id,
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
    Map calculateLegPlanningInstance(testLegPlanningInstance)
    {
        // calculate planLegTime
        try {
            switch (testLegPlanningInstance.resultLegTimeInput.size()) {
                case 1:
                    Date date = Date.parse("s", testLegPlanningInstance.resultLegTimeInput)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                case 2:
                    Date date = Date.parse("ss", testLegPlanningInstance.resultLegTimeInput)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                case 4:
                    Date date = Date.parse("m:ss", testLegPlanningInstance.resultLegTimeInput)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                case 5:
                    Date date = Date.parse("mm:ss", testLegPlanningInstance.resultLegTimeInput)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                case 7:
                    Date date = Date.parse("H:mm:ss", testLegPlanningInstance.resultLegTimeInput)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                case 8:
                    Date date = Date.parse("HH:mm:ss", testLegPlanningInstance.resultLegTimeInput)
                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
                    break
                default:
                    return ['instance':testLegPlanningInstance,'error':true,'message':getMsg('fc.testlegplanningresult.legtime.error')]
            }
        } catch (Exception e) {
            return ['instance':testLegPlanningInstance,'error':true,'message':getMsg('fc.testlegplanningresult.value.error')]
        }
        
        Contest contestInstance = testLegPlanningInstance.test.contestdaytask.contestday.contest
        
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
        
        specialTestInstance.contestdaytask = ContestDayTask.get( params.contestdaytaskid )
        
        if(!specialTestInstance.hasErrors() && specialTestInstance.save()) {

            def contestDayTaskInstance = ContestDayTask.get( params.contestdaytaskid )
            contestDayTaskInstance.specialtest = specialTestInstance
            contestDayTaskInstance.save()
            
            return ['instance':specialTestInstance,'saved':true,'message':getMsg('fc.created',["${specialTestInstance.name()}"]),
                    'fromcontestdaytask':params.fromcontestdaytask,'contestdaytaskid':contestDayTaskInstance.id]
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
                def contestDayTaskInstance = ContestDayTask.get( specialTestInstance.contestdaytask.id )
                contestDayTaskInstance.specialtest = null
                contestDayTaskInstance.save()
                
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
    private Map calculateLegData(RouteCoord newRouteCoordInstance, RouteCoord lastRouteCoordInstance)
    {
        // calculate leg distance
        def newLatMath = newRouteCoordInstance.latMath()
        def lastLatMath = lastRouteCoordInstance.latMath()
            
        def latDiff = newLatMath - lastLatMath 
        def lonDiff = newRouteCoordInstance.lonMath() - lastRouteCoordInstance.lonMath()
        
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
    private void newLeg(Route route, RouteCoord newRouteCoordInstance, RouteCoord lastRouteCoordInstance, RouteCoord lastRouteCoordTestInstance, BigDecimal lastMapMeasureDistance) 
    {
    	println "newLeg: ${route.name()} ${newRouteCoordInstance.title()}"
    	
        // create RouteLegCoord
    	if (lastRouteCoordInstance) {
    		Map legDataCoord = calculateLegData(newRouteCoordInstance, lastRouteCoordInstance)
	
	        def titleCoord = "${lastRouteCoordInstance.title()} -> ${newRouteCoordInstance.title()}"
	        
	        def routeLegCoordInstance = new RouteLegCoord([trueTrack:legDataCoord.dir,
	                                                       coordDistance:legDataCoord.dis,
	                                                       route:route,
	                                                       title:titleCoord,
	                                                       mapmeasuredistance:newRouteCoordInstance.mapmeasuredistance,
	                                                       mapdistance:newRouteCoordInstance.mapdistance
	                                                      ]) 
	        routeLegCoordInstance.save()
        }

        // create RouteCoordType
        if (lastRouteCoordInstance && lastRouteCoordTestInstance) {
            Map legDataTest = calculateLegData(newRouteCoordInstance, lastRouteCoordTestInstance)
            
	        def titleTest = "${lastRouteCoordTestInstance.title()} -> ${newRouteCoordInstance.title()}"
            
	        if ( (lastRouteCoordTestInstance.type == RouteCoordType.SP && newRouteCoordInstance.type == RouteCoordType.TP) ||
	        	 (lastRouteCoordTestInstance.type == RouteCoordType.TP && newRouteCoordInstance.type == RouteCoordType.TP) ||
	        	 (lastRouteCoordTestInstance.type == RouteCoordType.TP && newRouteCoordInstance.type == RouteCoordType.FP) )
	        {
	        	def routeLegTestInstance = new RouteLegTest([trueTrack:legDataTest.dir,
	        	                                             coordDistance:legDataTest.dis,
	        	                                             route:route,
	        	                                             title:titleTest,
	                                                         mapmeasuredistance:lastMapMeasureDistance,
	                                                         mapdistance:calculateMapDistance(route.contest,lastMapMeasureDistance)
	                                                        ])
	        	routeLegTestInstance.save()
	        }
        }
    }
    
    //--------------------------------------------------------------------------
    BigDecimal calculateMapDistance(Contest contestInstance, BigDecimal mapMeasureDistance)
    {
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
        // remove all legs
    	removeAllRouteLegs(routeInstance)
        
        // calculate new legs
        RouteCoord lastRouteCoordInstance
        RouteCoord lastRouteCoordTestInstance
        BigDecimal lastMapMeasureDistance = 0
        RouteCoord.findAllByRoute(routeInstance).each { routeCoordInstance -> 
        	lastMapMeasureDistance += routeCoordInstance.mapmeasuredistance
            newLeg(routeInstance, routeCoordInstance, lastRouteCoordInstance, lastRouteCoordTestInstance, lastMapMeasureDistance)
            lastRouteCoordInstance = routeCoordInstance
            switch (routeCoordInstance.type) {
	            case RouteCoordType.SP:
	            case RouteCoordType.TP:
	            case RouteCoordType.FP:
	            	lastRouteCoordTestInstance = routeCoordInstance
	            	lastMapMeasureDistance = 0
	            	break
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
    void calulateSequence(ContestDayTask contestDayTaskInstance)
    {
    	// set viewpos for aircraft of user1 
        Test.findAllByContestdaytask(contestDayTaskInstance).each { testInstance ->
        	if (testInstance.crew.aircraft) {
        		if (testInstance.crew.aircraft.user1 == testInstance.crew) {
        			testInstance.viewpos = 2000+testInstance.crew.tas
        		}
        	}
        }

        // set viewpos for aircraft of user2 
        Test.findAllByContestdaytask(contestDayTaskInstance).each { testInstance ->
            if (testInstance.crew.aircraft) {
                if (testInstance.crew.aircraft.user2 == testInstance.crew) {
                    testInstance.viewpos = 1000+testInstance.crew.tas
                }
            }
        }

        // set viewpos for user without aircraft 
        Test.findAllByContestdaytask(contestDayTaskInstance).each { testInstance ->
            if (!testInstance.crew.aircraft) {
                testInstance.viewpos = 0
            }
        }

        // set viewpos
        Test.findAllByContestdaytask(contestDayTaskInstance,[sort:"viewpos",order:"desc"]).eachWithIndex { testInstance, i ->
            testInstance.viewpos = i
            testInstance.timeCalculated = false
            testInstance.save()
        }
    }
 
    //--------------------------------------------------------------------------
    void addTestingTime(ContestDayTask contestDayTaskInstance, Test testInstance)
    {
        if (testInstance.timeCalculated) {
            
            GregorianCalendar time = new GregorianCalendar() 
            time.setTime(testInstance.testingTime)
            
            // add testingTime
            time.add(Calendar.MINUTE, contestDayTaskInstance.addTimeValue)
            testInstance.testingTime = time.getTime()
            
            // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
            calculateTimes(time, contestDayTaskInstance, testInstance)
            
            calulateTimetableWarnings(contestDayTaskInstance)
        }
    }
  
    //--------------------------------------------------------------------------
    void subtractTestingTime(ContestDayTask contestDayTaskInstance, Test testInstance)
    {
        if (testInstance.timeCalculated) {
            
            GregorianCalendar time = new GregorianCalendar() 
            time.setTime(testInstance.testingTime)
            
            // subtract testingTime
            time.add(Calendar.MINUTE, -contestDayTaskInstance.addTimeValue)
            testInstance.testingTime = time.getTime()
            
            // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
            calculateTimes(time, contestDayTaskInstance, testInstance)
            
            calulateTimetableWarnings(contestDayTaskInstance)
        }
    }

    //--------------------------------------------------------------------------
    private void calulateTimetableWarnings(ContestDayTask contestDayTaskInstance)
    {
        Date first_date = Date.parse("HH:mm",contestDayTaskInstance.firstTime)
        Date lastArrivalTime = first_date
        
        Test.findAllByContestdaytask(contestDayTaskInstance,[sort:"viewpos"]).each { testInstance ->
            
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
            Test.findAllByContestdaytask(contestDayTaskInstance,[sort:"viewpos"]).each { testInstance2 ->
                if (testInstance.crew.aircraft == testInstance2.crew.aircraft) {
                	if (testInstance == testInstance2) {
                		foundAircraft = true
                	}
					if (!foundAircraft) {
	                	lastTakeoffTime = new GregorianCalendar()
	                    lastTakeoffTime.setTime(testInstance2.arrivalTime)
	                    lastTakeoffTime.add(Calendar.MINUTE, contestDayTaskInstance.minNextFlightDuration)
	                    lastTakeoffTime.add(Calendar.MINUTE, contestDayTaskInstance.preparationDuration)
	                }
                }
            }
            if (lastTakeoffTime) {
                if (testInstance.takeoffTime < lastTakeoffTime.getTime()) {
                    testInstance.takeoffTimeWarning = true
                }
            }
        }

    }
 
    //--------------------------------------------------------------------------
    void calulateTimetable(ContestDayTask contestDayTaskInstance)
    {
        Date first_date = Date.parse("HH:mm",contestDayTaskInstance.firstTime)
        GregorianCalendar first_time = new GregorianCalendar() 
        first_time.setTime(first_date)

        GregorianCalendar start_time = new GregorianCalendar()
        start_time.set(Calendar.HOUR_OF_DAY, first_time.get(Calendar.HOUR_OF_DAY))
        start_time.set(Calendar.MINUTE,      first_time.get(Calendar.MINUTE))
        start_time.set(Calendar.SECOND,      0)

        BigDecimal lastTAS = 9000.0
        Date lastArrivalTime = first_date
        
        Test.findAllByContestdaytask(contestDayTaskInstance,[sort:"viewpos"]).each { testInstance ->
            
            if (testInstance.crew.tas > lastTAS) { // faster aircraft
            	start_time.add(Calendar.MINUTE, contestDayTaskInstance.takeoffIntervalFasterAircraft - contestDayTaskInstance.takeoffIntervalNormal)
            }
            
            // calculate testingTime
            GregorianCalendar time = start_time.clone()
            testInstance.testingTime = time.getTime()
            
            // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
            calculateTimes(time, contestDayTaskInstance, testInstance)
            
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
            Test.findAllByContestdaytask(contestDayTaskInstance,[sort:"viewpos"]).each { testInstance2 ->
	            if (testInstance.crew.aircraft == testInstance2.crew.aircraft) {
	                if (testInstance == testInstance2) {
	                    foundAircraft = true
	                }
	                if (!foundAircraft) {
	                    lastTakeoffTime = new GregorianCalendar()
	                    lastTakeoffTime.setTime(testInstance2.arrivalTime)
	                    lastTakeoffTime.add(Calendar.MINUTE, contestDayTaskInstance.minNextFlightDuration)
	                    lastTakeoffTime.add(Calendar.MINUTE, contestDayTaskInstance.preparationDuration)
	                }
	            }
            }
            if (lastTakeoffTime) {
                if (testInstance.takeoffTime < lastTakeoffTime.getTime()) {
                    testInstance.takeoffTimeWarning = true
                }
            }

            // next 
            start_time.add(Calendar.MINUTE, contestDayTaskInstance.takeoffIntervalNormal)
            lastTAS = testInstance.crew.tas
            
            testInstance.timeCalculated = true
        }

    }
 
    //--------------------------------------------------------------------------
    private void calculateTimes(GregorianCalendar time, ContestDayTask contestDayTaskInstance, Test testInstance) 
    {
        // calulate endTestingTime
        time.add(Calendar.MINUTE, contestDayTaskInstance.planningTestDuration)
        testInstance.endTestingTime = time.getTime()
            
        // calulate takeoffTime
        time.add(Calendar.MINUTE, contestDayTaskInstance.preparationDuration)
        testInstance.takeoffTime = time.getTime()
        
        // calulate startTime
        time.add(Calendar.MINUTE, contestDayTaskInstance.risingDuration)
        testInstance.startTime = time.getTime()
        
        // calculate finishTime
        time.add(Calendar.SECOND, (3600 * getLegsPlanTime(testInstance)).toInteger() )
        testInstance.finishTime = time.getTime()
        
        // calculate arrivalTime
        time.add(Calendar.MINUTE, contestDayTaskInstance.landingDuration)
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
    void calulateTestLegFlights(ContestDayTask contestDayTaskInstance)
    {
        println "calulateTestLegFlights: ${contestDayTaskInstance.name()}"
        
        Test.findAllByContestdaytask(contestDayTaskInstance,[sort:"viewpos"]).each { testInstance ->
            // remove all TestLegFlights
            TestLegFlight.findAllByTest(testInstance).each { testLegFlightInstance ->
                testLegFlightInstance.delete(flush:true)
            }

            // calculate TestLegFlights
            Integer lastTrueTrack = null
            RouteLegTest.findAllByRoute(testInstance?.flighttestwind?.flighttest?.route).each { routeLegTestInstance ->
                
                def testLegFlightInstance = new TestLegFlight()
                calculateLeg(testLegFlightInstance, routeLegTestInstance, testInstance.flighttestwind.wind, testInstance.crew.tas, testInstance.contestdaytask.procedureTurnDuration, lastTrueTrack)
                testLegFlightInstance.test = testInstance
                testLegFlightInstance.save()

                lastTrueTrack = testLegFlightInstance.planTrueTrack
            }
        }
    }

    //--------------------------------------------------------------------------
    private void calulateTestLegPlannings(Test testInstance)
    {
        println "calulateTestLegPlannings: ${testInstance.crew.name}"
        
        // remove all TestLegPlannings
        TestLegPlanning.findAllByTest(testInstance).each { testLegPlanningInstance ->
            testLegPlanningInstance.delete()
        }
        
        // calculate TestLegPlannings with results 
        Integer lastTrueTrack = null
        RouteLegTest.findAllByRoute(testInstance.planningtesttask.route).each { routeLegTestInstance ->
            def testLegPlanningInstance = new TestLegPlanning()
            calculateLeg(testLegPlanningInstance, routeLegTestInstance, testInstance.planningtesttask.wind, testInstance.crew.tas, testInstance.planningtesttask.planningtest.contestdaytask.procedureTurnDuration, lastTrueTrack)
            testLegPlanningInstance.test = testInstance
            if (!testInstance.contestdaytask.planningTestDistanceMeasure) {
                testLegPlanningInstance.resultTestDistance = testLegPlanningInstance.planTestDistance
            }
            if (!testInstance.contestdaytask.planningTestDirectionMeasure) {
                testLegPlanningInstance.resultTrueTrack = testLegPlanningInstance.planTrueTrack
            }
            testLegPlanningInstance.save()
            lastTrueTrack = testLegPlanningInstance.planTrueTrack
        }
    }

    //--------------------------------------------------------------------------
    private void calculateLeg(TestLeg testLeg, RouteLegTest routeLegTestInstance, Wind windInstance, BigDecimal valueTAS, int procedureTurnDuration, Integer lastTrueTrack) 
    {
       // save route data
       testLeg.planTestDistance = routeLegTestInstance.testDistance()
       testLeg.planTrueTrack = routeLegTestInstance.trueTrack
       
       // calculate wind
       if (windInstance.speed) {
           def windDirection = windInstance.direction - 180
           def beta = windDirection - routeLegTestInstance.trueTrack
           def sinBeta = Math.sin(Math.toRadians(beta)) 
           def driftAngle = 0
           if (beta != 0 && beta != 180 && beta != -180) {
               driftAngle = Math.toDegrees(Math.asin(sinBeta*windInstance.speed/valueTAS))  
           }
           testLeg.planTrueHeading = routeLegTestInstance.trueTrack - driftAngle 
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
    	   testLeg.planTrueHeading = routeLegTestInstance.trueTrack
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
    Map putContest(title,contestDayTitle,contestDayTaskTitle)
    {
        def p = [:]
        p.title = title
        p.contestDayTitle = contestDayTitle
        p.contestDayTaskTitle = contestDayTaskTitle
        return saveContest(p)
    }
    
    //--------------------------------------------------------------------------
    Map putContestDay(contest,title)
    {
        def p = [:]
        p.contestid = contest.instance.id
        p.title = title
        return saveContestDay(p)
    }
    
    //--------------------------------------------------------------------------
    Map putContestDayTask(contestday,title)
    {
        def p = [:]
        p.contestdayid = contestday.instance.id 
        p.title = title
        return saveContestDayTask(p)
    }
    
    //--------------------------------------------------------------------------
    Map putRoute(contest,title)
    {
        def p = [:]
        p.title = title
        return saveRoute(p,contest.instance)
    }

    //--------------------------------------------------------------------------
    Map putRouteCoord(route,type,titleNumber,latDirection,latGrad,latMinute,lonDirection,lonGrad,lonMinute,altitude,gatewidth,mapmeasuredistance)
    {
        def p = [:]
        p.routeid = route.instance.id
        p.type = type
        if (titleNumber > 0) {
        	p.titleNumber = titleNumber
        }
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
        return saveRouteCoord(p)
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
    Map putFlightTest(contestdaytask,title,route)
    {
        def p = [:]
        p.contestdaytaskid = contestdaytask.instance.id
        p.title = title
        p.route = route.instance
        p.direction = 0
        p.speed = 0
        return saveFlightTest(p)
    }
    
    //--------------------------------------------------------------------------
    Map putFlightTest(contestdaytask,title,route,direction,speed)
    {
        def p = [:]
        p.contestdaytaskid = contestdaytask.instance.id
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
    Map putLandingTest(contestdaytask,title)
    {
        def p = [:]
        p.contestdaytaskid = contestdaytask.instance.id
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
    Map putPlanningTest(contestdaytask,title)
    {
        def p = [:]
        p.contestdaytaskid = contestdaytask.instance.id
        p.title = title
        p.direction = 0
        p.speed = 0
        return savePlanningTest(p)
    }

    //--------------------------------------------------------------------------
    Map putPlanningTest(contestdaytask,title,taskTitle,route,direction,speed)
    {
        def p = [:]
        p.contestdaytaskid = contestdaytask.instance.id
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
    Map putSpecialTest(contestdaytask,title)
    {
        def p = [:]
        p.contestdaytaskid = contestdaytask.instance.id
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
    Map putResults(contestdaytask,crew,flightTestPenalties,landingTestPenalties,specialTestPenalties)
    {
        Test testInstance = Test.findByContestdaytaskAndCrew(contestdaytask.instance,crew.instance)
        def p = [:]
        p.id = testInstance.id
        p.flightTestPenalties = flightTestPenalties
        p.landingTestPenalties = landingTestPenalties
        p.specialTestPenalties = specialTestPenalties
        return updateresultsTest(p)
    }
    
    //--------------------------------------------------------------------------
    Map runcalculatesequenceContestDayTask(contestdaytask)
    {
        def p = [:]
        p.id = contestdaytask.instance.id 
        return calculatesequenceContestDayTask(p)
    }
    
    //--------------------------------------------------------------------------
    Map runcalculatetimetableContestDayTask(contestdaytask)
    {
        def p = [:]
        p.id = contestdaytask.instance.id 
        return calculatetimetableContestDayTask(p)
    }
    
    //--------------------------------------------------------------------------
    Map runcalculatepositionsContestDayTask(contestdaytask)
    {
        def p = [:]
        p.id = contestdaytask.instance.id 
        return calculatepositionsContestDayTask(p)
    }
}
