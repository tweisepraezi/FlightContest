import java.util.Date;

import org.xhtmlrenderer.pdf.ITextRenderer
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.context.request.RequestContextHolder
import javax.servlet.http.Cookie

class FcService
{
    boolean transactional = true
    def messageSource
	def logService
    
    static int mmPerNM = 1852000
	static int maxCookieAge = 31536000 // seconds (1 Jahr)
    
    
    //--------------------------------------------------------------------------
    Map startAflos(Map params,lastAflosController)
    {
        if (lastAflosController) {
        	return [controller:lastAflosController]
        }
        return [controller:'aflosRouteDefs']
    }
    
    //--------------------------------------------------------------------------
    Map getAircraft(Map params)
    {
        Aircraft aircraft_instance = Aircraft.get(params.id)
        if (!aircraft_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.aircraft'),params.id])]
        }
        return ['instance':aircraft_instance]
    }

    //--------------------------------------------------------------------------
    Map updateAircraft(Map params)
    {
        Aircraft aircraft_instance = Aircraft.get(params.id)
        if(aircraft_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(aircraft_instance.version > version) {
                    aircraft_instance.errors.rejectValue("version", "aircraft.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':aircraft_instance]
                }
            }

            if (params.registration != aircraft_instance.registration) {
                Aircraft aircraft2Instance = Aircraft.findByRegistrationAndContest(params.registration,aircraft_instance.contest)
                if (aircraft2Instance) {
                    return ['instance':aircraft2Instance,'error':true,'message':getMsg('fc.aircraft.registration.error',["${params.registration}"])]
                }
            }
            
            aircraft_instance.properties = params

            if(!aircraft_instance.hasErrors() && aircraft_instance.save()) {
                return ['instance':aircraft_instance,'saved':true,'message':getMsg('fc.updated',["${aircraft_instance.registration}"])]
            } else {
                return ['instance':aircraft_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.aircraft'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createAircraft(Map params)
    {
        Aircraft aircraft_instance = new Aircraft()
        aircraft_instance.properties = params
        return ['instance':aircraft_instance]
    }
    
    //--------------------------------------------------------------------------
    Map saveAircraft(Map params,Contest contestInstance)
    {
        Aircraft aircraft_instance = new Aircraft(params)
        aircraft_instance.contest = contestInstance
        
        if (params.registration) {
            Aircraft aircraft2Instance = Aircraft.findByRegistrationAndContest(params.registration,contestInstance)
            if (aircraft2Instance) {
            	return ['instance':aircraft2Instance,'error':true,'message':getMsg('fc.aircraft.registration.error',["${params.registration}"])]
            }
        }
        
        if(!aircraft_instance.hasErrors() && aircraft_instance.save()) {
            return ['instance':aircraft_instance,'saved':true,'message':getMsg('fc.created',["${aircraft_instance.registration}"])]
        } else {
            return ['instance':aircraft_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteAircraft(Map params)
    {
        Aircraft aircraft_instance = Aircraft.get(params.id)
        if(aircraft_instance) {
            try {
                if (aircraft_instance.user1) {
                    Crew crew_instance = Crew.get( aircraft_instance.user1.id )
                    crew_instance.aircraft = null
                    crew_instance.save()
                    aircraft_instance.user1 = null 
                }
                if (aircraft_instance.user2) {
                    Crew crew_instance = Crew.get( aircraft_instance.user2.id )
                    crew_instance.aircraft = null
                    crew_instance.save()
                    aircraft_instance.user2 = null 
                }
                aircraft_instance.delete()
                return ['deleted':true,'message':getMsg('fc.deleted',["${aircraft_instance.registration}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.aircraft'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.aircraft'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map printAircrafts(Map params,printparams)
    {
        Map aircrafts = [:]

        // Print aircrafts
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/aircraft/listprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
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
    Map updateContest(Map params)
    {
        Contest contest_instance = Contest.get(params.id)
        
        if (contest_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(contest_instance.version > version) {
                    contest_instance.errors.rejectValue("version", "contest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':contest_instance]
                }
            }
            contest_instance.properties = params
            if(!contest_instance.hasErrors() && contest_instance.save()) {
                return ['instance':contest_instance,'saved':true,'message':getMsg('fc.updated',["${contest_instance.title}"])]
            } else {
                return ['instance':contest_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createContest(Map params)
    {
        Contest contest_instance = new Contest()
        contest_instance.properties = params
        return ['instance':contest_instance,'created':true]
    }
    
    //--------------------------------------------------------------------------
	String getContestCopyTitle(Contest sourceContestInstance)
	{
		String new_title = sourceContestInstance.title
		if ( new_title.lastIndexOf(')') + 1 == new_title.length())
		{
			int i1 = new_title.lastIndexOf('(')
			if (i1 > 0) {
				new_title = new_title.substring(0,i1 - 1)
			}
		}
		new_title = getMsg('fc.contest.copytitle',["$new_title"])
		
		String new_title2 = new_title 
		int found_num = 1
		while (ContestTitleFound(new_title2)) {
			found_num++
			new_title2 = "$new_title ($found_num)"
		}
		return new_title2
	}
	
    //--------------------------------------------------------------------------
	private boolean ContestTitleFound(String newTitle)
	{
      	if (Contest.findByIdIsNotNull()) {
			for(Contest contest_instance in Contest.list()) {
			   if (contest_instance.title == newTitle) {
				   return true
			   }
			}
      	}
		return false
	}
	
    //--------------------------------------------------------------------------
    Map saveContest(Map params)
    {
        Contest contest_instance = new Contest(params)
        
        if(!contest_instance.hasErrors() && contest_instance.save()) {
            return ['instance':contest_instance,'saved':true,'message':getMsg('fc.created',["${contest_instance.title}"])]
        } else {
            return ['instance':contest_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map copyContest(Map params, Contest lastContestInstance)
    {
        Contest contest_instance = new Contest(params)
		contest_instance.CopyValues(lastContestInstance)
        if(!contest_instance.hasErrors() && contest_instance.save()) {
            return ['instance':contest_instance,'saved':true,'message':getMsg('fc.created',["${contest_instance.title}"])]
        } else {
            return ['instance':contest_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteContest(Map params)
    {
        Contest contest_instance = Contest.get(params.id)
        
        if (contest_instance) {
            try {
            	Task.findAllByContest(contest_instance).each { Task task_instance ->
            		task_instance.delete()
            	}
                contest_instance.delete()
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${contest_instance.title}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.contest'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map calculatepositionsContest(Contest contestInstance)
    {
		printstart "calculatepositionsContest"
		
		Map contest = [:]
		
        // calulate penalties
		for (Crew crew_instance in Crew.findAllByContest(contestInstance)) {
			crew_instance.contestPenalties = 0
			for (Task task_instance in Task.findAllByContest(contestInstance)) {
				crew_instance.contestPenalties += Test.findByCrewAndTask(crew_instance,task_instance).taskPenalties
			}
			crew_instance.save()
		}
		
        // calulate positions
        int act_penalty = -1
        int max_position = Crew.countByContest(contestInstance)
        for (int act_position = 1; act_position <= max_position; act_position++) {
            
            // search lowest penalty
            int min_penalty = 100000
            Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
				if (!crew_instance.disabled) {
	                if (crew_instance.contestPenalties > act_penalty) {
	                    if (crew_instance.contestPenalties < min_penalty) {
	                        min_penalty = crew_instance.contestPenalties 
	                    }
	                }
				}
            }
            act_penalty = min_penalty 
            
            // set position
            int set_position = -1
            Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
				if (crew_instance.disabled) {
					crew_instance.contestPosition = 0
					crew_instance.save()
				} else {
	                if (crew_instance.contestPenalties == act_penalty) {
	                    crew_instance.contestPosition = act_position
	                    crew_instance.save()
	                    set_position++
	                }
				}
            }
            
            if (set_position > 0) {
                act_position += set_position
            }
        }
        
        contest.message = getMsg('fc.results.positionscalculated')
		printdone contest.message      
        return contest
    }
    
    //--------------------------------------------------------------------------
    Map printresultsContest(Contest contestInstance, printparams)
    {
		Map contest = [:]
		
        // Positions calculated?
        boolean call_return = false
        Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
            if (crew_instance.disabled) {
				if (crew_instance.contestPosition) {
					call_return = true
				}
            } else {
				if (!crew_instance.contestPosition) {
					call_return = true
				}
            }
        }
        if (call_return) {
            contest.message = getMsg('fc.results.positions2calculate')
            contest.error = true
            return contest
        }
        
        // Print positions
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/contest/positionsprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            contest.content = content 
        }
        catch (Throwable e) {
            contest.message = getMsg('fc.print.error',["$e"])
            contest.error = true
        }
        return contest
    }
    
    //--------------------------------------------------------------------------
    Map getTask(Map params)
    {
        Task task_instance = Task.get(params.id)

        if (!task_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
        
        return ['instance':task_instance]
    }

    //--------------------------------------------------------------------------
    Map updateTask(Map params)
    {
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(task_instance.version > version) {
                    task_instance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':task_instance]
                }
            }
            
            task_instance.properties = params
			
			boolean modify_test = (task_instance.planningTestRun.toString() != params.planningTestRun) ||
							      (task_instance.flightTestRun.toString() != params.flightTestRun) ||
								  (task_instance.observationTestRun.toString() != params.observationTestRun) ||
								  (task_instance.landingTestRun.toString() != params.landingTestRun) ||
								  (task_instance.specialTestRun.toString() != params.specialTestRun)
			if (modify_test) {
		        Test.findAllByTask(task_instance).each { Test test_instance ->
					test_instance.CalculateTestPenalties()
		            test_instance.save()
		        }
			}	
			
            if(!task_instance.hasErrors() && task_instance.save()) {
                return ['instance':task_instance,'saved':true,'message':getMsg('fc.updated',["${task_instance.name()}"])]
            } else {
                return ['instance':task_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createTask(Map params)
    {
        Task task_instance = new Task()
        task_instance.properties = params
        return ['instance':task_instance]
    }
    
    //--------------------------------------------------------------------------
    Map saveTask(Map params,Contest contestInstance)
    {
    	Task task_instance = new Task(params)
        
        task_instance.contest = contestInstance
        task_instance.idTitle = Task.countByContest(contestInstance) + 1
        
        if(!task_instance.hasErrors() && task_instance.save()) {
            Crew.findAllByContest(task_instance.contest,[sort:"viewpos"]).eachWithIndex { Crew crew_instance, int i ->
                Test test_instance = new Test()
                test_instance.crew = crew_instance
				test_instance.taskTAS = crew_instance.tas
                test_instance.viewpos = i
                test_instance.task = task_instance
                test_instance.timeCalculated = false
                test_instance.save()
            }
            
            return ['instance':task_instance,'saved':true,'fromcontestday':params.fromcontestday,'message':getMsg('fc.created',["${task_instance.name()}"])]
        } else {
            return ['instance':task_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteTask(Map params)
    {
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            try {
            	// remove Tests
            	Test.findAllByTask(task_instance).each { Test test_instance ->
            		test_instance.delete()
            	}

                task_instance.delete()
                
                // correct idTitle of other tasks
                Task.findAllByContest(task_instance.contest).eachWithIndex { Task task_instance2, int index -> 
                    task_instance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${task_instance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.task'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map savedisabledcheckpointsTask(Map params)
    {
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(task_instance.version > version) {
                    task_instance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':task_instance]
                }
            }
            
			String last_disabledcheckpoints = task_instance.disabledCheckPoints
            task_instance.properties = params
			
			task_instance.disabledCheckPoints = ""
			CoordRoute.findAllByRoute(task_instance.flighttest.route).each { CoordRoute coordroute_instance ->
				if (params.(coordroute_instance.title()) == "on") {
					if (task_instance.disabledCheckPoints) {
						task_instance.disabledCheckPoints += ",${coordroute_instance.title()}"
					} else {
						task_instance.disabledCheckPoints = coordroute_instance.title() 
					}
				}
			}
			
			boolean modify_flighttest_results = last_disabledcheckpoints != task_instance.disabledCheckPoints
			if (modify_flighttest_results) {
		        Test.findAllByTask(task_instance).each { Test test_instance ->
					CoordResult.findAllByTest(test_instance).each { CoordResult coordresult_instance ->
						calculateCoordResultInstancePenaltyCoord(coordresult_instance)
						coordresult_instance.save()
					}
					calculateTestPenalties(test_instance)
		            test_instance.save()
		        }
			}	
			
            if(!task_instance.hasErrors() && task_instance.save()) {
                return ['instance':task_instance,'saved':true,'message':getMsg('fc.updated',["${task_instance.name()}"])]
            } else {
                return ['instance':task_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetdisabledcheckpointsTask(Map params)
    {
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(task_instance.version > version) {
                    task_instance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':task_instance]
                }
            }
            
			String last_disabledcheckpoints = task_instance.disabledCheckPoints
            task_instance.disabledCheckPoints = ""
			
			boolean modify_flighttest_results = last_disabledcheckpoints != task_instance.disabledCheckPoints
			if (modify_flighttest_results) {
		        Test.findAllByTask(task_instance).each { Test test_instance ->
					CoordResult.findAllByTest(test_instance).each { CoordResult coordresult_instance ->
						calculateCoordResultInstancePenaltyCoord(coordresult_instance)
						coordresult_instance.save()
					}
					calculateTestPenalties(test_instance)
		            test_instance.save()
		        }
			}	
			
            if(!task_instance.hasErrors() && task_instance.save()) {
                return ['instance':task_instance,'saved':true,'message':getMsg('fc.updated',["${task_instance.name()}"])]
            } else {
                return ['instance':task_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map startplanningTask(Map params,contestInstance,lastTaskPlanning)
    {
    	Task task_instance = null
        if (lastTaskPlanning) {
            task_instance = Task.findById(lastTaskPlanning)
        }
        if (!task_instance) {
			if (contestInstance) {
	        	Task.findAllByContest(contestInstance).each {
	        		if (!task_instance) {
	        			task_instance = it
	        		}
	        	}
			}
        }
        if (task_instance) {
            return ['taskid':task_instance.id]
        }
    }
    
    //--------------------------------------------------------------------------
    Map startresultsTask(Map params,contestInstance,lastTaskResults)
    {
        Task task_instance = null
        if (lastTaskResults) {
            task_instance = Task.findById(lastTaskResults)
        }
        if (!task_instance) {
			if (contestInstance) {
	            Task.findAllByContest(contestInstance).each {
	                if (!task_instance) {
	                    task_instance = it
	                }
	            }
			}
        }
        if (task_instance) {
            return ['taskid':task_instance.id]
        }
    }
    
    //--------------------------------------------------------------------------
    Map selectallTask(Map params)
    {
        Map task = getTask(params) 
        if (task.instance) {
            Map selected_testids = [selectedTestID:""]
            Test.findAllByTask(task.instance).each { Test test_instance ->
				selected_testids["selectedTestID${test_instance.id}"] = "on"
            }
            task.selectedtestids = selected_testids
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map assignplanningtesttaskTask(Map params)
    {
		printstart "assignplanningtesttaskTask"
		
        Map task = getTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

        // PlanningTest exists?
        if (!task.instance.planningtest) {
            task.message = getMsg('fc.planningtest.notfound')
            task.error = true
			printerror task.message
            return task
        }

        // PlanningTestTask exists?
        if (!PlanningTestTask.countByPlanningtest(task.instance.planningtest)) {
            task.message = getMsg('fc.planningtesttask.notfound')
            task.error = true
			printerror task.message
            return task
        }

        // Multiple PlanningTestTasks?  
        if (PlanningTestTask.countByPlanningtest(task.instance.planningtest) > 1) {
            List test_instance_ids = [""]
            Test.findAllByTask(task.instance).each { Test test_instance ->
                if (params["selectedTestID${test_instance.id}"] == "on") {
                    test_instance_ids += test_instance.id.toString()
                }
            }
            task.testinstanceids = test_instance_ids
			printdone ""
            return task
        }

        // set single PlanningTestTask to all selected Tests
        PlanningTestTask planningtesttask_instance = PlanningTestTask.findByPlanningtest(task.instance.planningtest) 
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
				if (!test_instance.crew.disabled) {
	                test_instance.planningtesttask = planningtesttask_instance
	                calulateTestLegPlannings(test_instance)
					test_instance.ResetPlanningTestResults()
					test_instance.CalculateTestPenalties()
	                test_instance.save()
				}
            }
        }
        
		printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
    Map setplanningtesttaskTask(Map params)
    {
        Map task = getTask(params) 
        if (task.instance) {
            PlanningTestTask planningtesttask_instance = PlanningTestTask.get(params.planningtesttask.id)
            params.testInstanceIDs.each { String test_id ->
                if (test_id) {
                    Test test_instance = Test.get(test_id)
					if (!test_instance.crew.disabled) {
	                    test_instance.planningtesttask = planningtesttask_instance 
	                    calulateTestLegPlannings(test_instance)
						test_instance.ResetPlanningTestResults()
						test_instance.CalculateTestPenalties()
	                    test_instance.save()
					}
                }
            }
            task.message = getMsg('fc.task.selectplanningtesttask.assigned',[planningtesttask_instance.name()])
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map assignflighttestwindTask(Map params)
    {
		printstart "assignflighttestwindTask"
		
        Map task = getTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
            task.error = true
			printerror task.message
            return task
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
            task.message = getMsg('fc.flighttestwind.notfound')
            task.error = true
			printerror task.message
            return task
        }

        // Multiple FlightTestWinds?  
        if (FlightTestWind.countByFlighttest(task.instance.flighttest) > 1) {
            List test_instance_ids = [""]
            Test.findAllByTask(task.instance).each { Test test_instance ->
                if (params["selectedTestID${test_instance.id}"] == "on") {
                    test_instance_ids += test_instance.id.toString()
                }
            }
            task.testinstanceids = test_instance_ids
			printdone ""
            return task
        }

        // set single FlightTestWind to all selected Tests
        FlightTestWind flighttestwind_instance = FlightTestWind.findByFlighttest(task.instance.flighttest)
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
				if (!test_instance.crew.disabled) {
					setflighttestwindTest(test_instance, task.instance, flighttestwind_instance)
				}
            }
        }

		printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
    Map setflighttestwindTask(Map params)
    {
		printstart "setflighttestwindTask"
        Map task = getTask(params) 
        if (task.instance) {
            FlightTestWind flighttestwind_instance = FlightTestWind.get(params.flighttestwind.id)
            params.testInstanceIDs.each { String test_id ->
                if (test_id) {
                    Test test_instance = Test.get(test_id)
					if (!test_instance.crew.disabled) {
						setflighttestwindTest(test_instance, task.instance, flighttestwind_instance)
					}
                }
            }
            task.message = getMsg('fc.task.selectflighttestwind.assigned',[flighttestwind_instance.wind.name()])
        }
		printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
	private void setflighttestwindTest(Test testInstance, Task taskInstance, FlightTestWind flightTestWind)
	{
		printstart "setflighttestwindTest: ${testInstance.crew.name}"
        testInstance.flighttestwind = flightTestWind
		calulateTestLegFlight(testInstance)
        if (testInstance.timeCalculated) {
			GregorianCalendar testing_time = new GregorianCalendar()
			testing_time.setTime(testInstance.testingTime)
			calculateTime(testInstance, taskInstance, testing_time, null)
			calulateCoordResult(testInstance)
			taskInstance.timetableModified = true
			taskInstance.save()
        }
        testInstance.save()
		printdone ""
	}
	
    //--------------------------------------------------------------------------
    Map calculatesequenceTask(Map params)
    {
		printstart "calculatesequenceTask"
		
        Map task = getTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

        /*
        // Have all crews an aircraft?
        boolean call_return = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (!test_instance.crew.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            return task
        }
        */

    	// set viewpos for aircraft of user1 
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	        	if (test_instance.crew.aircraft) {
	        		if (test_instance.crew.aircraft.user1 == test_instance.crew) {
	        			test_instance.viewpos = 4000+test_instance.taskTAS
	        		}
	        	}
			}
        }

        // set viewpos for aircraft of user2 
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (test_instance.crew.aircraft) {
	                if (test_instance.crew.aircraft.user2 == test_instance.crew) {
	                    test_instance.viewpos = 3000+test_instance.taskTAS
	                }
	            }
			}
        }

        // set viewpos for user without aircraft 
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (!test_instance.crew.aircraft) {
	                test_instance.viewpos = 2000+test_instance.taskTAS
	            }
			}
        }

        // set viewpos for disabled user 
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (test_instance.crew.disabled) {
                test_instance.viewpos = 1000+test_instance.taskTAS
			}
        }

        // set viewpos
        Test.findAllByTask(task.instance,[sort:"viewpos",order:"desc"]).eachWithIndex { Test test_instance, int i ->
            test_instance.viewpos = i
            test_instance.timeCalculated = false
			test_instance.ResetFlightTestResults()
			test_instance.CalculateTestPenalties()
            test_instance.save()
        }
        
        task.message = getMsg('fc.test.sequence.calculated')    
		printdone task.message    
        return task
    }
    
    //--------------------------------------------------------------------------
    Map resetsequenceTask(Map params)
    {
		printstart "resetsequenceTask"
		
        Map task = getTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

    	// set viewpos with crew viewpos
        Test.findAllByTask(task.instance).each { Test test_instance ->
   			test_instance.viewpos = test_instance.crew.viewpos
            test_instance.timeCalculated = false
			test_instance.ResetFlightTestResults()
			test_instance.CalculateTestPenalties()
            test_instance.save()
        }

        task.message = getMsg('fc.test.sequence.reset')    
		printdone task.message    
        return task
    }
    
    //--------------------------------------------------------------------------
    Map moveupTask(Map params,session)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

		// moveable? (not top & connected selection)
        boolean borderreached = false
        boolean notmovable = false
        boolean off2on = false
        boolean on2off = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (test_instance.viewpos == 0) {
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
        
		// move tasks up
		int movenum = 0
        int movefirstpos = -1
        Map selected_testids = [selectedTestID:""]
        borderreached = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                test_instance.viewpos--
                test_instance.timeCalculated = false
                test_instance.save()
                selected_testids["selectedTestID${test_instance.id}"] = "on"
                if (test_instance.viewpos == 0) {
                    borderreached = true
                }
                movenum++
                if (movefirstpos == -1 || test_instance.viewpos < movefirstpos) {
                    movefirstpos = test_instance.viewpos
                }
            }
        }
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] != "on") {
                if (test_instance.viewpos >= movefirstpos && test_instance.viewpos < movefirstpos + movenum) {
                    test_instance.viewpos += movenum
                    test_instance.timeCalculated = false
                    test_instance.save()
                }
            }
        }

		// modify showLimitStartPos
		if (session.showLimit) {
			if (movefirstpos < session.showLimitStartPos) {
				if (movenum == 1) {
					if (session.showLimitStartPos > session.showLimitCrewNum) {
						session.showLimitStartPos -= session.showLimitCrewNum
					} else {
						session.showLimitStartPos = 0
					}
				} else {
					session.showLimitStartPos--
				}
			}
		}
		
		// restore selection if not top
        if (!borderreached) {
            task.selectedtestids = selected_testids
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map movedownTask(Map params,session)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

		// moveable? (not bottom & connected selection)
        boolean borderreached = false
        boolean notmovable = false
        boolean off2on = false
        boolean on2off = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (test_instance.viewpos + 1 == Crew.countByContest(task.instance.contest)) {
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
        
		// move tasks down
        int movenum = 0
        int movefirstpos = -1
        Map selected_testids = [selectedTestID:""]
        borderreached = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                test_instance.viewpos++
                test_instance.timeCalculated = false
                test_instance.save()
                selected_testids["selectedTestID${test_instance.id}"] = "on"
                if (test_instance.viewpos + 1 == Crew.countByContest(task.instance.contest)) {
					borderreached = true
                }
                movenum++
                if (movefirstpos == -1 || test_instance.viewpos < movefirstpos) {
                    movefirstpos = test_instance.viewpos
                }
            }
        }
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] != "on") {
                if (test_instance.viewpos >= movefirstpos && test_instance.viewpos < movefirstpos + movenum) {
                    test_instance.viewpos -= movenum
                    test_instance.save()
                }
            }
        }

		// modify showLimitStartPos
		if (session.showLimit) {
			if (movefirstpos + movenum > session.showLimitStartPos + session.showLimitCrewNum) {
				if (movenum == 1) {
					int crew_num = Crew.countByContest(session.lastContest)
					if (session.showLimitStartPos + session.showLimitCrewNum < crew_num) {
						session.showLimitStartPos += session.showLimitCrewNum
					}
				} else {
					session.showLimitStartPos++
				}
			}
		}
		
		// restore selection if not bottom
        if (!borderreached) {
            task.selectedtestids = selected_testids
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map moveendTask(Map params,session)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

		// moveable? (not bottom & connected selection)
        boolean borderreached = false
        boolean notmovable = false
        boolean off2on = false
        boolean on2off = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (test_instance.viewpos + 1 == Crew.countByContest(task.instance.contest)) {
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
        
		// move tasks to end
        int movenum = 0
        int movefirstpos = -1
        Map selected_testids = [selectedTestID:""]
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (movefirstpos == -1) {
                    movefirstpos = test_instance.viewpos
                }
                test_instance.viewpos = Crew.countByContest(task.instance.contest) + movenum
                test_instance.timeCalculated = false
                test_instance.save()
                selected_testids["selectedTestID${test_instance.id}"] = "on"
                movenum++
            }
        }
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (test_instance.viewpos >= movefirstpos) {
                test_instance.viewpos -= movenum
                test_instance.save()
            }
        }

		// modify showLimitStartPos
		if (session.showLimit) {
			if (movefirstpos + movenum > session.showLimitStartPos + session.showLimitCrewNum) {
				if (movenum == 1) {
					int crew_num = Crew.countByContest(session.lastContest)
					if (session.showLimitStartPos + session.showLimitCrewNum < crew_num) {
						session.showLimitStartPos += session.showLimitCrewNum
					}
				} else {
					session.showLimitStartPos++
				}
			}
		}
		
        return task
    }
    
    //--------------------------------------------------------------------------
    Map calculatetimetableTask(Map params)
    {
        printstart "calculatetimetableTask"
		
        Map task = getTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
            task.error = true
			printerror task.message
            return task
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
            task.message = getMsg('fc.flighttestwind.notfound')
            task.error = true
			printerror task.message
            return task
        }
        
        // FlightTestWind assigned to all crews?
        boolean call_return = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (!test_instance.flighttestwind) {
	                call_return = true
	            }
			}
        }
        if (call_return) {
            task.message = getMsg('fc.flighttestwind.notassigned')
            task.error = true
			printerror task.message
            return task
        }

        /*
        // Have all crews an aircraft?
        call_return = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (!test_instance.crew.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            printerror task.message
            return task
        }
        */

        calulateTestLegFlights(task.instance)
        int crew_num = calulateTimetable(task.instance)
        
        task.message = getMsg('fc.test.timetable.calculated',[crew_num])   
		printdone task.message    
        return task
    }
    
    //--------------------------------------------------------------------------
    Map timeaddTask(Map params)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

        Map selected_testids = [selectedTestID:""]
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                addTestingTime(task.instance,test_instance)
                selected_testids["selectedTestID${test_instance.id}"] = "on"
            }
        }
        task.selectedtestids = selected_testids
        return task
    }
    
    //--------------------------------------------------------------------------
    Map timesubtractTask(Map params)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

        Map selected_testids = [selectedTestID:""]
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                subtractTestingTime(task.instance,test_instance)
                selected_testids["selectedTestID${test_instance.id}"] = "on"
            }
        }
        task.selectedtestids = selected_testids
           return task
    }
    
    //--------------------------------------------------------------------------
    Map printtimetableTask(Map params,printparams, boolean isJury)
    {
        Map task = getTask(params) 
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
        boolean call_return = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (!test_instance.flighttestwind) {
	                call_return = true
	            }
			}
        }
        if (call_return) {
            task.message = getMsg('fc.flighttestwind.notassigned')
            task.error = true
            return task
        }

        // Have all crews an aircraft?
        call_return = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (!test_instance.crew.aircraft) {
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
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (!test_instance.timeCalculated) {
	                call_return = true
	            }
			}
        }
        if (call_return) {
            task.message = getMsg('fc.test.timetable.newcalculate')
            task.error = true
            return task
        }        
        
        // Warnings?  
        call_return = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (test_instance.arrivalTimeWarning || test_instance.takeoffTimeWarning) {
	                call_return = true
	            }
			}
        }
        if (call_return) {
            task.message = getMsg('fc.test.flightplan.resolvewarnings')
            task.error = true
            return task
        }        
        
		// Calculate timetable version
		if (task.instance.timetableModified) {
			task.instance.timetableVersion++
			task.instance.timetableModified = false
			task.instance.save(flush:true)
		}
		
        // Print timetable
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
			String url = ""
			if (isJury) {
				url = "${printparams.baseuri}/task/timetablejuryprintable/${task.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
			} else {
				url = "${printparams.baseuri}/task/timetableprintable/${task.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
			}
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            task.content = content 
        }
        catch (Throwable e) {
            task.message = getMsg('fc.print.error',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printflightplansTask(Map params,printparams)
    {
        Map task = getTask(params) 
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
        boolean call_return = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (!test_instance.flighttestwind) {
	                call_return = true
	            }
			}
        }
        if (call_return) {
            task.message = getMsg('fc.flighttestwind.notassigned')
            task.error = true
            return task
        }

        // Have all crews an aircraft?
        call_return = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (!test_instance.crew.aircraft) {
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
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (!test_instance.timeCalculated) {
	                call_return = true
	            }
			}
        }
        if (call_return) {
            task.message = getMsg('fc.test.timetable.newcalculate')
            task.error = true
            return task
        }        
        
        // Warnings?  
        call_return = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (test_instance.arrivalTimeWarning || test_instance.takeoffTimeWarning) {
	                call_return = true
	            }
			}
        }
        if (call_return) {
            task.message = getMsg('fc.test.flightplan.resolvewarnings')
            task.error = true
            return task
        }        
        
		// Calculate timetable version
		if (task.instance.timetableModified) {
			task.instance.timetableVersion++
			task.instance.timetableModified = false
			task.instance.save(flush:true)
		}
		
        // Print flightplans
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
				if (!test_instance.crew.disabled) {
					test_instance.timetableVersion = task.instance.timetableVersion
					test_instance.save(flush:true)
	                String url = "${printparams.baseuri}/test/flightplanprintable/${test_instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
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
    Map printplanningtasksTask(Map params,printparams)
    {
        Map task = getTask(params) 
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
        boolean call_return = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (!test_instance.planningtesttask) {
	                call_return = true
	            }
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
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
				if (!test_instance.crew.disabled) {
	                String url = "${printparams.baseuri}/test/planningtaskprintable/${test_instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}&results=no"
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
    Map calculatepositionsTask(Map params)
    {
		printstart "calculatepositionsTask"
		
        Map task = getTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

        // calulate position
        int act_penalty = -1
        int max_position = Test.countByTask(task.instance)
        for (int act_position = 1; act_position <= max_position; act_position++) {
            
            // search lowest penalty
            int min_penalty = 100000
            Test.findAllByTask(task.instance).each { Test test_instance ->
				if (!test_instance.crew.disabled) {
	                if (test_instance.taskPenalties > act_penalty) {
	                    if (test_instance.taskPenalties < min_penalty) {
	                        min_penalty = test_instance.taskPenalties 
	                    }
	                }
				}
            }
            act_penalty = min_penalty 
            
            // set position
            int set_position = -1
            Test.findAllByTask(task.instance).each { Test test_instance ->
				if (test_instance.crew.disabled) {
                    test_instance.taskPosition = 0
                    test_instance.save()
				} else {
	                if (test_instance.taskPenalties == act_penalty) {
	                    test_instance.taskPosition = act_position
	                    test_instance.save()
	                    set_position++
	                }
				}
            }
            
            if (set_position > 0) {
                act_position += set_position
            }
        }
        
        task.message = getMsg('fc.results.positionscalculated')  
		printdone task.message      
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printresultsTask(Map params,printparams)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // Positions calculated?  
        boolean call_return = false
        Test.findAllByTask(task.instance).each { Test test_instance ->
            if (test_instance.crew.disabled) {
				if (test_instance.taskPosition) {
					call_return = true
				}
            } else {
				if (!test_instance.taskPosition) {
					call_return = true
				}
            }
        }
        if (call_return) {
            task.message = getMsg('fc.results.positions2calculate')
            task.error = true
            return task
        }        
        
        // Print positions
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/task/positionsprintable/${task.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            task.content = content 
        }
        catch (Throwable e) {
            task.message = getMsg('fc.print.error',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map getRoute(Map params)
    {
        Route route_instance = Route.get(params.id)

        if(!route_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
        
        return ['instance':route_instance]
    }
    
    //--------------------------------------------------------------------------
    Map updateRoute(Map params)
    {
        Route route_instance = Route.get(params.id)
        
        if (route_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(route_instance.version > version) {
                    route_instance.errors.rejectValue("version", "route.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':route_instance]
                }
            }
            
            route_instance.properties = params
            if(!route_instance.hasErrors() && route_instance.save()) {
                return ['instance':route_instance,'saved':true,'message':getMsg('fc.updated',["${route_instance.name()}"])]
            } else {
                return ['instance':route_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createRoute(Map params)
    {
        Route route_instance = new Route()
        route_instance.properties = params
        return ['instance':route_instance]
    }
    
    //--------------------------------------------------------------------------
    Map saveRoute(Map params,Contest contestInstance)
    {
        Route route_instance = new Route(params)
        
        route_instance.contest = contestInstance
        route_instance.idTitle = Route.countByContest(contestInstance) + 1
        
        if(!route_instance.hasErrors() && route_instance.save()) {
            return ['instance':route_instance,'saved':true,'message':getMsg('fc.created',["${route_instance.name()}"])]
        } else {
            return ['instance':route_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteRoute(Map params)
    {
        Route route_instance = Route.get(params.id)
        Contest contest_instance = route_instance.contest
        
        if (route_instance) {
            try {
                route_instance.delete()
                
                Route.findAllByContest(contest_instance).eachWithIndex { Route route_instance2, int index -> 
                    route_instance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${route_instance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.route'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map printRoute(Map params,printparams)
    {
        Map route = getRoute(params)
        if (!route.instance) {
            return route
        }
        
        // Print route
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/route/showprintable/${route.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
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
    Map printRoutes(Map params,printparams)
    {
        Map routes = [:]

        // Print routes
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Route.findAllByContest(printparams.contest).each { Route route_instance ->
	            String url = "${printparams.baseuri}/route/showprintable/${route_instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
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
    Map caculateroutelegsRoute(Map params)
    {
        Route route_instance = Route.get(params.id)
        
        if (route_instance) {
        	calculateRouteLegs(route_instance)
            return ['instance':route_instance,'calculated':true,'message':getMsg('fc.routeleg.calculated')]
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
    Map importAflosRoute(Map params, Contest contestInstance)
    {
		printstart "importAflosRoute"
		
        AflosRouteNames aflosroutenames_instance = AflosRouteNames.get(params.aflosroutenames.id)
    	if (aflosroutenames_instance) {
    	
    		Route route_instance = new Route()
	        
	        route_instance.contest = contestInstance
	        route_instance.idTitle = Route.countByContest(contestInstance) + 1
	        route_instance.title = aflosroutenames_instance.name
	        route_instance.mark = aflosroutenames_instance.name
	        
	        if(!route_instance.hasErrors() && route_instance.save()) {
                List aflosroutedefs_instances = AflosRouteDefs.findAllByRoutename(aflosroutenames_instance)
                CoordRoute last_coordroute_instance
                CoordRoute last_coordroute_test_instance
                int tpNum = 0
                int tcNum = 0
                aflosroutedefs_instances.each { AflosRouteDefs aflosroutedefs_instance ->
                	CoordRoute coordroute_instance = new CoordRoute()
                    
                    // set latitude
                    aflosroutedefs_instance.latitude.split().eachWithIndex{ String latValue, int i ->
                		switch(i) {
                			case 0: 
                				coordroute_instance.latDirection = latValue
                				break
                			case 1:
                				coordroute_instance.latGrad = latValue.toInteger()
                				break
                			case 2:
                				coordroute_instance.latMinute = latValue.replace(',','.').toBigDecimal()
                				break
                		}
                	}
                    
                    // set longitude
                    aflosroutedefs_instance.longitude.split().eachWithIndex{ String lonValue, int i ->
                        switch(i) {
                            case 0: 
                                coordroute_instance.lonDirection = lonValue
                                break
                            case 1:
                                coordroute_instance.lonGrad = lonValue.toInteger()
                                break
                            case 2:
                                coordroute_instance.lonMinute = lonValue.replace(',','.').toBigDecimal()
                                break
                        }
                    }
    
                    // set type and title
                    coordroute_instance.type = CoordType.UNKNOWN
                    CoordType.each { CoordType type ->
                    	if (coordroute_instance.type == CoordType.UNKNOWN) { // Typ der Koordinate noch nicht ermittelt
	                    	if (type != CoordType.UNKNOWN && aflosroutedefs_instance.mark && aflosroutedefs_instance.mark.startsWith(type.aflosMark)) {  
	                    		if (type.aflosGateWidth == 0 || (type.aflosGateWidth == aflosroutedefs_instance.gatewidth)) { // Übereinstimmung AFLOS-Kooordinate mit Typ
	                    			switch (type) {
	                    				case CoordType.SECRET:
	                    					if (params.aflosroutenames.secretcoordrouteidentification == SecretCoordRouteIdentification.NONE.toString()) {
	                    						coordroute_instance.type = CoordType.TP 
	                    					} else {
	                    						coordroute_instance.type = CoordType.SECRET 
	                    					}
                                            break
	                    				case CoordType.TP:
                                            coordroute_instance.type = CoordType.TP 
                                            break
                                        default:
                                        	coordroute_instance.type = type
                                        	break
	                    			}

	                    			switch (coordroute_instance.type) {
		                    			case CoordType.TP:
		                    				tpNum++
		                    				coordroute_instance.titleNumber = tpNum
		                    				break
		                    			case CoordType.SECRET:
		                    				tcNum++
	                                        coordroute_instance.titleNumber = tcNum
		                    				break
		                    		}
	                    		}
	                    	}
                    	}
                    }
                    
                    // set other
                    coordroute_instance.altitude = aflosroutedefs_instance.altitude.toInteger()
                    coordroute_instance.mark = aflosroutedefs_instance.mark
                    coordroute_instance.gatewidth = aflosroutedefs_instance.gatewidth
                    coordroute_instance.route = route_instance
                    
					// calculate coordTrueTrack/coordMeasureDistance
					if (last_coordroute_test_instance) {
						Map legdata_coord = calculateLegData(coordroute_instance, last_coordroute_test_instance)
						coordroute_instance.coordTrueTrack = legdata_coord.dir
						coordroute_instance.coordMeasureDistance = calculateMapMeasure(coordroute_instance.route.contest,legdata_coord.dis)
					}
					 
                    coordroute_instance.save()
                    
                    newLeg(coordroute_instance.route, coordroute_instance, last_coordroute_instance, last_coordroute_test_instance, null, null, null)
                    last_coordroute_instance = coordroute_instance
                    switch (coordroute_instance.type) {
	                    case CoordType.SP:
	                    case CoordType.TP:
	                    case CoordType.FP:
	                        last_coordroute_test_instance = coordroute_instance
	                        break
	                }
                }
                calculateSecretLegRatio(route_instance)
                Map ret = ['instance':route_instance,'saved':true,'message':getMsg('fc.imported',["${route_instance.name()}"])]
				printdone ret
				return ret
	        } else {
	            Map ret = ['error':true,'message':getMsg('fc.notimported',["${aflosroutenames_instance.name}"])]
				printerror ret.message
				return ret
	        }
        } else {
            Map ret = ['error':true,'message':getMsg('fc.notimported',["${params.aflosroutenames.id}"])]
			printerror ret.message
			return ret
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
    Map importAflosResults(Map params)
    {
		Test test_instance = Test.get(params.id)
		int start_num = params.afloscrewnames.startnum.toInteger()
		return importAflosResults(test_instance, start_num)
	}
	
    //--------------------------------------------------------------------------
    private Map importAflosResults(Test testInstance, int startNum)
    {
        printstart "importAflosResults: crew '$testInstance.crew.name', startnum $startNum"
		
        AflosCrewNames afloscrewnames_instance = AflosCrewNames.findByStartnumAndPointsNotEqual(startNum,0)
        if (!afloscrewnames_instance) {
        	Map ret = ['error':true,'message':getMsg('fc.aflos.points.crewnotfound',[startNum])]
			printerror ret.message
			return ret
        }
        AflosRouteNames aflosroutenames_instance = AflosRouteNames.findByName(testInstance.flighttestwind.flighttest.route.mark)
        if (!aflosroutenames_instance) {
        	Map ret = ['error':true,'message':getMsg('fc.aflos.points.routenotfound',[testInstance.flighttestwind.flighttest.route.mark])]
			printerror ret.message
			return ret
        }
        
        println "AFLOS crew: '${afloscrewnames_instance.viewName()}', AFLOS route: '$aflosroutenames_instance.name'"

        AflosErrors aflos_error = AflosErrors.findByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance)
        if (!aflos_error) {
			Map ret = ['error':true,'message':getMsg('fc.aflos.points.notcomplete',[afloscrewnames_instance.viewName()])]
			printerror ret.message
			return ret
        }
		/*
        if (aflos_error.mark.contains("Check Error")) {
        	Map ret = ['error':true,'message':getMsg('fc.aflos.points.errors',[afloscrewnames_instance.viewName()])]
			printerror ret.message
			return ret
        }
        */

        try {
	        // Import AflosCheckPoints
            int checkpoint_errors = 0
            int height_errors = 0
	        CoordResult.findAllByTest(testInstance).each { CoordResult coordresult_instance ->
	        	boolean found = false
	        	AflosCheckPoints.findAllByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance).each { AflosCheckPoints afloscheckpoints_instance ->
	        		if (afloscheckpoints_instance.mark == "P${coordresult_instance.mark}") {
	        			// reset results
	        			coordresult_instance.ResetResults()
	        			
	        			// read utc '09h 36min 05,000sec'
	        			coordresult_instance.resultCpTimeInput = FcMath.ConvertAFLOSTime(afloscheckpoints_instance.utc) 
	
	        			// read latitude '51° 26,9035' N'
	        			coordresult_instance.resultLatitude = FcMath.ConvertAFLOSCoordValue(afloscheckpoints_instance.latitude)
 
	        			// read longitude '013° 51,7278' E'
	                    coordresult_instance.resultLongitude = FcMath.ConvertAFLOSCoordValue(afloscheckpoints_instance.longitude)
 
	        			// read altitude '01992 ft'
	        			coordresult_instance.resultAltitude = afloscheckpoints_instance.altitude.split()[0].toInteger()
	        			
	        			// calculate results
                        if (coordresult_instance.planProcedureTurn) {
                            coordresult_instance.resultProcedureTurnEntered = true
                        }
	        			calculateCoordResultInstance(coordresult_instance,true)
	        			
                        // calculate verify values 
                        if (coordresult_instance.resultMinAltitudeMissed) {
                            height_errors++
                        }

	        			// save results
	                    coordresult_instance.save()
	                    found = true
	                    
	                    // log
	                    if (coordresult_instance.planProcedureTurn) {
	                        println "Procedure turn"
	                    }
	                    println "Found AflosCheckPoint $afloscheckpoints_instance.mark UTC: $coordresult_instance.resultCpTimeInput Local: ${FcMath.TimeStr(coordresult_instance.resultCpTime)}"
	        		}
	        	}
	        	if (!found) {
	        		// reset results
                    coordresult_instance.ResetResults()
                    
	        		// calculate results
                    coordresult_instance.resultCpNotFound = true
                    if (coordresult_instance.planProcedureTurn) {
                        coordresult_instance.resultProcedureTurnEntered = true
                    }
                    calculateCoordResultInstance(coordresult_instance,true)
                    
                    // calculate verify values
                    checkpoint_errors++
                    
                    // save results
	                coordresult_instance.save()
                    
	                // log
	                if (coordresult_instance.planProcedureTurn) {
                        println "Procedure turn"
                    }
                    println "Not found AflosCheckPoint $coordresult_instance.mark"
	        	}
	    	}
	    	
	    	// Import AflosErrorPoints
	    	int badcourse_num = 0
	    	int course_errors = 0
	    	String badcourse_starttimeutc
	    	AflosErrorPoints.findAllByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance).each { AflosErrorPoints afloserrorpoints_instance ->
	    		if (badcourse_num == 0) {
	    			if (afloserrorpoints_instance.mark.contains("-Bad Course")) {
		    			badcourse_num = 1
		    			badcourse_starttimeutc = afloserrorpoints_instance.utc
		    		}
	    		} else {
	    			if (!afloserrorpoints_instance.mark.trim()) {
	    				badcourse_num++
	    			} else {
	    				// process error point
	    				if (processAflosErrorPointBadCourse(testInstance, badcourse_num, badcourse_starttimeutc)) {
	    					course_errors++
	    				}
	    				
	    				// search for next error point 
	    				if (afloserrorpoints_instance.mark.contains("-Bad Course")) {
	    					badcourse_num = 1
	                        badcourse_starttimeutc = afloserrorpoints_instance.utc
	    				} else {
	    					badcourse_num = 0
	    					badcourse_starttimeutc = ""
	    				}
	    			}
	    		}
	    		if (afloserrorpoints_instance.mark.contains("-Bad Turn")) {
	    			processAflosErrorPointBadTurn(testInstance, afloserrorpoints_instance.utc)
	    		}
	    	}
	        if (badcourse_num > 0) {
	            // process error point
	        	if (processAflosErrorPointBadCourse(testInstance, badcourse_num, badcourse_starttimeutc)) {
	        		course_errors++
	        	}
	        }
	        
	    	// Penalties berechnen
	        calculateTestPenalties(testInstance)
	        testInstance.save()
	        
	        // save imported crew
	        testInstance.crew.mark = afloscrewnames_instance.viewName()
	        testInstance.crew.save()
	        
	        if (aflos_error.mark == "Flight O.K.") {
	        	Map ret = ['saved':true,'message':getMsg('fc.aflos.points.imported',[afloscrewnames_instance.viewName()])]
				printdone ret.message
				return ret
	        } else {
	        	if (aflos_error.dropOutErrors == 0 && 
	        		checkpoint_errors == aflos_error.checkPointErrors && 
	        		height_errors == aflos_error.heightErrors && 
	        		course_errors == aflos_error.courseErrors)
	        	{
	        		Map ret = ['saved':true,'message':getMsg('fc.aflos.points.imported.naverrors',[afloscrewnames_instance.viewName()])]
					printdone ret.message
					return ret
	        	} else {
	        		if (checkpoint_errors != aflos_error.checkPointErrors) {
	        			println "Evaluation error: $checkpoint_errors bad checkpoints <> $aflos_error.checkPointErrors bad AFLOS checkpoints"
	        		}
                    if (height_errors != aflos_error.heightErrors) {
                        println "Evaluation error: $height_errors bad heights <> $aflos_error.heightErrors bad AFLOS heights"
                    }
                    if (course_errors != aflos_error.courseErrors) {
                        println "Evaluation error: $course_errors bad courses <> $aflos_error.courseErrors bad AFLOS courses"
                    }
	        		Map ret = ['error':true,'saved':true,'message':getMsg('fc.aflos.points.imported.naverrors.differences',[afloscrewnames_instance.viewName()])]
					printerror ret.message
					return ret
	        	}
	        }
    	}
        catch (Exception e) {
        	Map ret = ['error':true,'message':getMsg('fc.notimported.msg',[afloscrewnames_instance.viewName(),e.getMessage()])]
			printerror ret.message
			return ret
        }
    }

    //--------------------------------------------------------------------------
    private boolean processAflosErrorPointBadCourse(Test testInstance, int badCourseNum, String badCourseStartTimeUTC)
    {
    	boolean course_error = false
    	Contest contest_instance = testInstance.task.contest
    	
        Date badCourseStartTime = Date.parse("HH:mm:ss",FcMath.ConvertAFLOSTime(badCourseStartTimeUTC))
        GregorianCalendar badCourseStartCalendar = new GregorianCalendar()
        badCourseStartCalendar.setTime(badCourseStartTime)
        
        Date timezone_date = Date.parse("HH:mm",contest_instance.timeZone)
        GregorianCalendar timezone_calendar = new GregorianCalendar()
        timezone_calendar.setTime(timezone_date)
        
        badCourseStartCalendar.add(Calendar.HOUR_OF_DAY, timezone_calendar.get(Calendar.HOUR_OF_DAY))
        badCourseStartCalendar.add(Calendar.MINUTE, timezone_calendar.get(Calendar.MINUTE))
        
        GregorianCalendar badCourseEndCalendar = badCourseStartCalendar.clone()
        badCourseEndCalendar.add(Calendar.SECOND, badCourseNum)
            
        println "Found AflosErrorPointBadCourse ($badCourseNum, ${FcMath.TimeStr(badCourseStartCalendar.getTime())}...${FcMath.TimeStr(badCourseEndCalendar.getTime())}): "

        if (badCourseNum > contest_instance.flightTestBadCourseCorrectSecond) {
        	course_error = true
    		int last_index = 0
    		Date last_time
    		boolean calculatePenalties = false
            CoordResult.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { CoordResult coordresult_instance, int i ->
            	if (last_index != 0 && badCourseEndCalendar.getTime() < coordresult_instance.resultCpTime) {
        			// process
        			last_index = i
        			
        			if (badCourseEndCalendar.getTime() > last_time) {
        				coordresult_instance.resultBadCourseNum++
        				coordresult_instance.save()
        				calculatePenalties = true
        				println "  $coordresult_instance.mark relevant."
        			} else {
        				println "  $coordresult_instance.mark not relevant."
        			}
            	}

            	if (badCourseStartCalendar.getTime() > coordresult_instance.resultCpTime) {
                    last_index = i
                    last_time = coordresult_instance.resultCpTime
                } else {
                    last_index = 0
                }
            }
    		
    	} else {
    		println "  Not relevant (number)."
    	}
        return course_error
    }
    
    //--------------------------------------------------------------------------
    private void processAflosErrorPointBadTurn(Test testInstance, String badTurnTimeUTC)
    {
        Contest contest_instance = testInstance.task.contest
        
        Date badturn_time = Date.parse("HH:mm:ss",FcMath.ConvertAFLOSTime(badTurnTimeUTC))
        GregorianCalendar badturn_calendar = new GregorianCalendar()
        badturn_calendar.setTime(badturn_time)
        
        Date timezone_date = Date.parse("HH:mm",contest_instance.timeZone)
        GregorianCalendar timezone_calendar = new GregorianCalendar()
        timezone_calendar.setTime(timezone_date)
        
        badturn_calendar.add(Calendar.HOUR_OF_DAY, timezone_calendar.get(Calendar.HOUR_OF_DAY))
        badturn_calendar.add(Calendar.MINUTE, timezone_calendar.get(Calendar.MINUTE))
        
        print "Found AflosErrorPointBadTurn (${FcMath.TimeStr(badturn_calendar.getTime())}): "

        int last_index = 0
        Date last_time
        boolean calculatePenalties = false
        CoordResult.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { CoordResult coordresult_instance, int i ->
            if (last_index != 0 && coordresult_instance.planProcedureTurn && badturn_calendar.getTime() < coordresult_instance.resultCpTime) {
                // process
                last_index = i
                
                if (badturn_calendar.getTime() > last_time) {
                    coordresult_instance.resultProcedureTurnNotFlown = true
                    coordresult_instance.resultProcedureTurnEntered = true
                    coordresult_instance.save()
                    calculatePenalties = true
                    println "  $coordresult_instance.mark relevant."
                } else {
                    println "  $coordresult_instance.mark not relevant."
                }
            }

            if (badturn_calendar.getTime() > coordresult_instance.resultCpTime) {
                last_index = i
                last_time = coordresult_instance.resultCpTime
            } else {
                last_index = 0
            }
            
        }
    }
    
    //--------------------------------------------------------------------------
    Map getCoordRoute(Map params)
    {
        CoordRoute coordroute_instance = CoordRoute.get(params.id)

        if (!coordroute_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
        
        return ['instance':coordroute_instance]
    }

    //--------------------------------------------------------------------------
    Map updateCoordRoute(Map params)
    {
        CoordRoute coordroute_instance = CoordRoute.get(params.id)
        
        if (coordroute_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordroute_instance.version > version) {
                    coordroute_instance.errors.rejectValue("version", "coordRoute.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordroute_instance]
                }
            }
            
            coordroute_instance.properties = params
			calculateCoordMapDistance(coordroute_instance, false)
			if (coordroute_instance.measureTrueTrack || coordroute_instance.measureDistance) {
				coordroute_instance.measureEntered = true
			}

            if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
                calculateSecretLegRatio(coordroute_instance.route)
                calculateRouteLegs(coordroute_instance.route)
                return ['instance':coordroute_instance,'saved':true,'message':getMsg('fc.updated',["${coordroute_instance.name()}"])]
            } else {
                return ['instance':coordroute_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
	Map resetmeasureCoordRoute(Map params)
	{
        CoordRoute coordroute_instance = CoordRoute.get(params.id)
        
        if (coordroute_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordroute_instance.version > version) {
                    coordroute_instance.errors.rejectValue("version", "coordRoute.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordroute_instance]
                }
            }
            
			coordroute_instance.measureEntered = false
			coordroute_instance.measureTrueTrack = null
            coordroute_instance.measureDistance = null
			coordroute_instance.legMeasureDistance = null
			coordroute_instance.legDistance = null
			
            if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
                calculateSecretLegRatio(coordroute_instance.route)
                calculateRouteLegs(coordroute_instance.route)
                return ['instance':coordroute_instance,'saved':true,'message':getMsg('fc.updated',["${coordroute_instance.name()}"])]
            } else {
                return ['instance':coordroute_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
	}
	
    //--------------------------------------------------------------------------
    Map createCoordRoute(Map params)
    {
    	CoordRoute last_coordroute_instance = CoordRoute.findByRoute(Route.get(params.routeid),[sort:"id", order:"desc"])
        CoordRoute coordroute_instance = new CoordRoute()
    
        coordroute_instance.properties = params
        coordroute_instance.altitude = 1
    
        if (params.secret) {
            coordroute_instance.type = CoordType.SECRET
            if (last_coordroute_instance) {
                switch (last_coordroute_instance.type) {
                	case CoordType.SP:
                    case CoordType.TP:
                    case CoordType.SECRET:
                    	coordroute_instance.titleNumber = findNextTitleNumber(last_coordroute_instance.route,CoordType.SECRET)
                    	break
                    default:
                        return ['error':true,'message':getMsg('fc.coordroute.addsecret.notallowed')]
                }
            } else {
                return ['error':true,'message':getMsg('fc.coordroute.addsecret.notallowed')]
            }
        } else {
	        if (last_coordroute_instance) {
	        	switch (last_coordroute_instance.type) {
	        		case CoordType.TO:
	        			coordroute_instance.type = CoordType.SP
	        			break
	        		case CoordType.SP:
	                    coordroute_instance.type = CoordType.TP
	                    break
	        		case CoordType.TP:
                    case CoordType.SECRET:
	                    coordroute_instance.type = CoordType.TP
	                    coordroute_instance.titleNumber = findNextTitleNumber(last_coordroute_instance.route,CoordType.TP) 
	                    break
	                case CoordType.FP:
	                    coordroute_instance.type = CoordType.LDG
	                    break
	                case CoordType.LDG:
	                	return ['error':true,'message':getMsg('fc.coordroute.add.notallowed')]
	        	}
	        }
        }
        
        if (last_coordroute_instance) {
            coordroute_instance.latGrad = last_coordroute_instance.latGrad
            coordroute_instance.latMinute = last_coordroute_instance.latMinute
            coordroute_instance.latDirection = last_coordroute_instance.latDirection
            coordroute_instance.lonGrad = last_coordroute_instance.lonGrad
            coordroute_instance.lonMinute = last_coordroute_instance.lonMinute
            coordroute_instance.lonDirection = last_coordroute_instance.lonDirection
        }
        
        return ['instance':coordroute_instance]
    }

    
    //--------------------------------------------------------------------------
    int findNextTitleNumber(Route route, CoordType type)
    {
    	int titleNumber = 0
    	CoordRoute.findAllByRoute(route,[sort:"id", order:"desc"]).each { CoordRoute coordroute_instance ->
        	if (!titleNumber) {
	        	if (coordroute_instance.type == type) {
	        		titleNumber = coordroute_instance.titleNumber
	        	}
        	}
        }
    	return titleNumber + 1
    }
    
    //--------------------------------------------------------------------------
    Map saveCoordRoute(Map params)
    {
    	CoordRoute last_coordroute_instance = CoordRoute.findByRoute(Route.get(params.routeid),[sort:"id", order:"desc"])

		// Summe der Distanzen vorangegangener Secrets-Points berechnen  
		BigDecimal lastMapMeasureDistance = null
        CoordRoute last_coordroute_test_instance 
    	CoordRoute.findAllByRoute(Route.get(params.routeid),[sort:"id", order:"desc"]).each { CoordRoute coordroute_instance ->
			if (!last_coordroute_test_instance) {
		        switch (coordroute_instance.type) {
			        case CoordType.SP:
			        case CoordType.TP:
			        case CoordType.FP:
			        	last_coordroute_test_instance = coordroute_instance // exit
			            break
			        default:
		        		lastMapMeasureDistance = addMapDistance(lastMapMeasureDistance,coordroute_instance.legMeasureDistance)
		                break
			    }
        	}
        }
        
    	CoordRoute coordroute_instance = new CoordRoute(params)
        
        coordroute_instance.route = Route.get(params.routeid)
		calculateCoordMapDistance(coordroute_instance, true)
		if (coordroute_instance.measureTrueTrack || coordroute_instance.measureDistance) {
			coordroute_instance.measureEntered = true
		}
		
		// calculate coordTrueTrack/coordMeasureDistance
		if (last_coordroute_test_instance) {
			Map legdata_coord = calculateLegData(coordroute_instance, last_coordroute_test_instance)
			coordroute_instance.coordTrueTrack = legdata_coord.dir
			coordroute_instance.coordMeasureDistance = calculateMapMeasure(coordroute_instance.route.contest,legdata_coord.dis)
		} 

        if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
            calculateSecretLegRatio(coordroute_instance.route)
       		lastMapMeasureDistance = addMapDistance(lastMapMeasureDistance,coordroute_instance.legMeasureDistance)
        	newLeg(
				coordroute_instance.route, 
				coordroute_instance, 
				last_coordroute_instance, 
				last_coordroute_test_instance, 
				coordroute_instance.measureDistance, 
				lastMapMeasureDistance, 
				coordroute_instance.measureTrueTrack
			)
            return ['instance':coordroute_instance,'saved':true,'message':getMsg('fc.created',["${coordroute_instance.name()}"])]
        } else {
            return ['instance':coordroute_instance]
        }
    }
    
    //--------------------------------------------------------------------------
	private void calculateCoordMapDistance(CoordRoute coordRouteInstance, boolean isNew)
	{
		if (coordRouteInstance.measureDistance) {
			Route route_instance = coordRouteInstance.route
			coordRouteInstance.legMeasureDistance = coordRouteInstance.measureDistance
			boolean exit = false
			boolean check = isNew  
			CoordRoute.findAllByRoute(route_instance,[sort:"id", order:"desc"]).each { CoordRoute coordroute_instance ->
				if (!exit) {
					if (check) {
						if (coordroute_instance.type == CoordType.SECRET) {
							if (coordroute_instance.legMeasureDistance) {
								coordRouteInstance.legMeasureDistance -= coordroute_instance.legMeasureDistance
							}
						} else {
							exit = true
						}
					} else {
						if (coordRouteInstance == coordroute_instance) {
							check = true
						}
					}
				}
			}
			coordRouteInstance.legDistance = calculateMapDistance(route_instance.contest, coordRouteInstance.legMeasureDistance)
		}
	}
	
    //--------------------------------------------------------------------------
    Map deleteCoordRoute(Map params)
    {
        CoordRoute coordroute_instance = CoordRoute.get(params.id)
        
        if (coordroute_instance) {
            try {
                Route route_instance = coordroute_instance.route
                removeAllRouteLegs(route_instance)
                coordroute_instance.delete()
                calculateRouteLegs(route_instance)
                return ['deleted':true,'message':getMsg('fc.deleted',["${coordroute_instance.name()}"]),'routeid':route_instance.id]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.coordroute'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getRouteLegCoord(Map params)
    {
        RouteLegCoord routelegcoord_instance = RouteLegCoord.get(params.id)

        if (!routelegcoord_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.routeleg'),params.id])]
        }
        
        return ['instance':routelegcoord_instance]
    }

    //--------------------------------------------------------------------------
    Map updateRouteLegCoord(Map params)
    {
        RouteLegCoord routelegcoord_instance = RouteLegCoord.get(params.id)
        
        if (routelegcoord_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(routelegcoord_instance.version > version) {
                    routelegcoord_instance.errors.rejectValue("version", "routeLegCoord.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':routelegcoord_instance]
                }
            }
            
            routelegcoord_instance.properties = params

            calculateRouteLegCoordMapDistances(routelegcoord_instance)
			calculateSecretLegRatio(routelegcoord_instance.route)
			
            if(!routelegcoord_instance.hasErrors() && routelegcoord_instance.save()) {
                return ['instance':routelegcoord_instance,'saved':true,'message':getMsg('fc.updated',["${routelegcoord_instance.coordName()}"])]
            } else {
                return ['instance':routelegcoord_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    private void calculateRouteLegCoordMapDistances(routeLegCoordInstance)
    {
    	printstart "calculateRouteLegCoordMapDistances $routeLegCoordInstance"
    	
    	Contest contest_instance = routeLegCoordInstance.route.contest
		BigDecimal secrets_distance = 0
    		
    	// search coord for routeLegCoordInstance
    	CoordRoute found_coordroute_instance
    	RouteLegCoord.findAllByRoute(routeLegCoordInstance.route).eachWithIndex { RouteLegCoord routelegcoord_instance, int i -> 
    		if (routelegcoord_instance == routeLegCoordInstance) {
    			CoordRoute.findAllByRoute(routeLegCoordInstance.route).eachWithIndex { CoordRoute coordroute_instance, int j ->
    				if (i + 1 == j) {
    					found_coordroute_instance = coordroute_instance
    				}
					if (!found_coordroute_instance) {
						if (coordroute_instance.type == CoordType.SECRET) {
							if (coordroute_instance.legMeasureDistance) {
								secrets_distance += coordroute_instance.legMeasureDistance
							}
						} else {
							secrets_distance = 0
						}
					}
    			}
    		}
    	}

    	// calculate legDistance
		routeLegCoordInstance.legMeasureDistance = routeLegCoordInstance.measureDistance - secrets_distance 
    	routeLegCoordInstance.legDistance = calculateMapDistance(contest_instance,routeLegCoordInstance.legMeasureDistance)
    	
        // save legDistance to coord
    	if (found_coordroute_instance) {
			found_coordroute_instance.measureDistance = routeLegCoordInstance.measureDistance
    		found_coordroute_instance.legMeasureDistance = routeLegCoordInstance.legMeasureDistance
			found_coordroute_instance.legDistance = routeLegCoordInstance.legDistance
			found_coordroute_instance.measureTrueTrack = routeLegCoordInstance.measureTrueTrack
			found_coordroute_instance.save()
    	}
        
        // calculate legDistance in testlegs
        CoordRoute last_coordroute_instance
        CoordRoute last_coordroute_test_instance
        BigDecimal lastMapMeasureDistance = null
        BigDecimal lastMapMeasureTrueTrack = null
        int testlegpos = 0
        CoordRoute.findAllByRoute(routeLegCoordInstance.route).each { CoordRoute coordroute_instance ->
       		lastMapMeasureDistance = addMapDistance(lastMapMeasureDistance,coordroute_instance.legMeasureDistance)
            lastMapMeasureTrueTrack = coordroute_instance.measureTrueTrack
            if (last_coordroute_instance && last_coordroute_test_instance) {
                if ( (last_coordroute_test_instance.type == CoordType.SP && coordroute_instance.type == CoordType.TP) ||
                     (last_coordroute_test_instance.type == CoordType.SP && coordroute_instance.type == CoordType.FP) ||
                     (last_coordroute_test_instance.type == CoordType.TP && coordroute_instance.type == CoordType.TP) ||
                     (last_coordroute_test_instance.type == CoordType.TP && coordroute_instance.type == CoordType.FP) ) 
                {
                	RouteLegTest.findAllByRoute(routeLegCoordInstance.route).eachWithIndex { RouteLegTest routelegtest_instance, int i ->
                		if (i == testlegpos) {
                			routelegtest_instance.legMeasureDistance = lastMapMeasureDistance 
                			routelegtest_instance.legDistance = calculateMapDistance(contest_instance,lastMapMeasureDistance)
                			routelegtest_instance.measureTrueTrack = lastMapMeasureTrueTrack
                			routelegtest_instance.save()
                		}
                	}
                    testlegpos++
                }
            }
            last_coordroute_instance = coordroute_instance
            switch (coordroute_instance.type) {
                case CoordType.SP:
                case CoordType.TP:
                case CoordType.FP:
                    last_coordroute_test_instance = coordroute_instance
                    lastMapMeasureDistance = null
                    break
            }
        }
        printdone ""
    }

    //--------------------------------------------------------------------------
    Map getRouteLegTest(Map params)
    {
        RouteLegTest routelegtest_instance = RouteLegTest.get(params.id)

        if (!routelegtest_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.routeleg'),params.id])]
        }
        
        return ['instance':routelegtest_instance]
    }

    //--------------------------------------------------------------------------
    Map getCrew(Map params)
    {
        Crew crew_instance = Crew.get(params.id)

        if (!crew_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.crew'),params.id])]
        }
        
        return ['instance':crew_instance]
    }

    //--------------------------------------------------------------------------
    Map updateCrew(Map params)
    {
		printstart "updateCrew: $params.name"
        Crew crew_instance = Crew.get(params.id)
        if (crew_instance) {

            if (params.version) {
                long version = params.version.toLong()
                if(crew_instance.version > version) {
                    crew_instance.errors.rejectValue("version", "crew.optimistic.locking.failure", getMsg('fc.notupdated'))
					printerror ""
                    return ['instance':crew_instance]
                }
            }

            if (!crew_instance.hasErrors()) {

				boolean modify_tas = crew_instance.tas.toString() != params.tas
            	Aircraft old_aircraft_instance = crew_instance.aircraft
	            crew_instance.properties = params
	            
	            if (old_aircraft_instance) {
	            	if (crew_instance == old_aircraft_instance.user2) {
	            		old_aircraft_instance.user2 = null
	            	} else if (crew_instance == old_aircraft_instance.user1) {
	            		old_aircraft_instance.user1 = old_aircraft_instance.user2
	            		old_aircraft_instance.user2 = null
	            	}
	                old_aircraft_instance.save()
	            }
	
                if (crew_instance.aircraft) {
	                if (!crew_instance.aircraft.user1) {
		            	crew_instance.aircraft.user1 = crew_instance
		                crew_instance.aircraft.save()
		            } else if (!crew_instance.aircraft.user2) {
		            	crew_instance.aircraft.user2 = crew_instance
		            	crew_instance.aircraft.save()
		            } else {
		            	crew_instance.aircraft = null
		            }
                }
	
                if (crew_instance.save()) {

					int no_modify_tas_num = 0
					if (modify_tas) {
						println "TAS modified."
		                Test.findAllByCrew(crew_instance).each { Test test_instance ->
							if (test_instance.planningtesttask || test_instance.timeCalculated) {
								println "'${test_instance.task.name()}' '$test_instance.crew.name': do nothing."
								no_modify_tas_num++
							} else {
								test_instance.taskTAS = crew_instance.tas 
								calulateTestLegPlannings(test_instance)
								println "Reset results"
								test_instance.ResetPlanningTestResults()
			                	test_instance.timeCalculated = false
								test_instance.ResetFlightTestResults()
								test_instance.CalculateTestPenalties()
			                    test_instance.save()
							}
		                }
					}
	
					printdone ""
					if (no_modify_tas_num) {
						return ['instance':crew_instance,'saved':true,'message':getMsg('fc.crew.updated.tasnomodify',["${crew_instance.name}",no_modify_tas_num.toString()])]
					} else {
						return ['instance':crew_instance,'saved':true,'message':getMsg('fc.updated',["${crew_instance.name}"])]
					}
                } else {
					printerror ""
                    return ['instance':crew_instance]
                }
            } else {
				printerror ""
                return ['instance':crew_instance]
            }
        } else {
			printerror ""
            return ['message':getMsg('fc.notfound',[getMsg('fc.crew'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createCrew(Map params)
    {
        Crew crew_instance = new Crew()
        crew_instance.properties = params
        return ['instance':crew_instance]
    }
    
    //--------------------------------------------------------------------------
    Map saveCrew(Map params, Contest contestInstance)
    {
        Crew crew_instance = new Crew(params)
        crew_instance.contest = contestInstance
		crew_instance.viewpos = Crew.countByContest(contestInstance)
        if (!crew_instance.hasErrors() && crew_instance.save()) {
            
            if (params.registration) {
                Aircraft aircraft_instance = Aircraft.findByRegistrationAndContest(params.registration,contestInstance)
                if (!aircraft_instance) {
                    aircraft_instance = new Aircraft(params)
                    aircraft_instance.contest = crew_instance.contest 
                }
                if (!aircraft_instance.user1) {
                	aircraft_instance.user1 = crew_instance
                } else if (!aircraft_instance.user2) {
                    aircraft_instance.user2 = crew_instance
                } else {
                	aircraft_instance = null
                }
                if (aircraft_instance) {
                    if(!aircraft_instance.hasErrors() && aircraft_instance.save()) {
                        crew_instance.aircraft = aircraft_instance 
                        crew_instance.save()
                    }
                }
            }

            Task.findAllByContest(contestInstance).each { Task task_instance ->
                Test test_instance = new Test()
                test_instance.crew = crew_instance
				test_instance.taskTAS = crew_instance.tas
                test_instance.viewpos = Crew.countByContest(contestInstance) - 1
                test_instance.task = task_instance
                test_instance.timeCalculated = false
                test_instance.save()
            }
            String msg
            if (crew_instance.aircraft) {
            	msg = getMsg('fc.crew.withaircraft.created',["${crew_instance.name}", "${crew_instance.aircraft.registration}"])
            } else {
                msg = getMsg('fc.created',["${crew_instance.name}"])
            }

            return ['instance':crew_instance,'saved':true,'message':msg]
        } else {
            return ['instance':crew_instance]
        }
            
    }
    
    //--------------------------------------------------------------------------
    Map deleteCrew(Map params) 
    {
        Crew crew_instance = Crew.get(params.id)
        
        if(crew_instance) {
            try {
                
                if (crew_instance.aircraft) {
                	if (crew_instance == crew_instance.aircraft.user2) {
                		crew_instance.aircraft.user2 = null
                	} else {
                        crew_instance.aircraft.user1 = crew_instance.aircraft.user2
                        crew_instance.aircraft.user2 = null
                	}
                    crew_instance.aircraft.save()
                    
                    crew_instance.aircraft = null 
                }
                
                // remove crew tests
                Test.findAllByCrew(crew_instance).each { Test test_instance ->
                    test_instance.delete()
                }
                
                // correct all test's viewpos
                Task.findAllByContest(crew_instance.contest).each { Task task_instance ->
                	Test.findAllByTask(task_instance,[sort:"viewpos"]).eachWithIndex { Test test_instance, int i ->
                		test_instance.viewpos = i
                		test_instance.save()
                	}
                }

                crew_instance.delete()
                
                // correct all crew's viewpos
				Crew.findAllByContest(crew_instance.contest).eachWithIndex { Crew crew_instance2, int i ->
					crew_instance2.viewpos = i
					crew_instance2.save()
				}
				
                return ['deleted':true,'message':getMsg('fc.deleted',["${crew_instance.name}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.crew'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.crew'),params.id])]
        }
    }

    //--------------------------------------------------------------------------
    Map setaircraftCrew(Map crew,Map aircraft)
    {
		printstart "setaircraftCrew: $aircraft.instance.registration $crew.instance.name"
        crew.instance.aircraft = aircraft.instance
        Map p = [:]
        p.id = crew.instance.id
		p.name = crew.instance.name
        Map ret = updateCrew(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map selectallCrew(Contest contestInstance)
    {
        Map crew = [:] 
        Map selected_crewids = [selectedCrewID:""]
        Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
			selected_crewids["selectedCrewID${crew_instance.id}"] = "on"
        }
        crew.selectedcrewids = selected_crewids
        return crew
    }
    
    //--------------------------------------------------------------------------
    Map calculatesequenceCrew(Contest contestInstance, Map params)
    {
		printstart "calculatesequenceCrew"
		
        Map crew = [:] 

		/*
        // Have all crews an aircraft?
        boolean call_return = false
        Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
            if (!crew_instance.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            crew.message = getMsg('fc.aircraft.notassigned')
            crew.error = true
            return crew
        }
        */

    	// set viewpos for aircraft of user1 
        Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
			if (!crew_instance.disabled) {
	        	if (crew_instance.aircraft) {
	        		if (crew_instance.aircraft.user1 == crew_instance) {
	        			crew_instance.viewpos = 4000+crew_instance.tas
	        		}
	        	}
			}
        }

        // set viewpos for aircraft of user2 
        Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
			if (!crew_instance.disabled) {
	            if (crew_instance.aircraft) {
	                if (crew_instance.aircraft.user2 == crew_instance) {
	                    crew_instance.viewpos = 3000+crew_instance.tas
	                }
	            }
			}
        }

        // set viewpos for user without aircraft 
        Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
			if (!crew_instance.disabled) {
	            if (!crew_instance.aircraft) {
	                crew_instance.viewpos = 2000+crew_instance.tas
	            }
			}
        }

        // set viewpos for disabled user 
        Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
			if (crew_instance.disabled) {
                crew_instance.viewpos = 1000+crew_instance.tas
			}
        }

        // set viewpos
        Crew.findAllByContest(contestInstance,[sort:"viewpos",order:"desc"]).eachWithIndex { Crew crew_instance, int i ->
            crew_instance.viewpos = i
            crew_instance.save()
        }
        
        crew.message = getMsg('fc.test.sequence.calculated')    
		printdone crew.message    
        return crew
    }
    
    //--------------------------------------------------------------------------
    Map moveupCrew(Contest contestInstance, Map params, session)
    {
		Map crew = [:]
		
		// moveable? (not top & connected selection)
        boolean borderreached = false
        boolean notmovable = false
        boolean off2on = false
        boolean on2off = false
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (crew_instance.viewpos == 0) {
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
            crew.borderreached = true
            return crew
        }
        if (notmovable) {
            crew.message = getMsg('fc.test.notmovable')
            crew.error = true
            return crew
        }
        
		// move tasks
		int movenum = 0
        int movefirstpos = -1
        Map selected_crewids = [selectedCrewID:""]
        borderreached = false
        Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                crew_instance.viewpos--
                crew_instance.save()
                selected_crewids["selectedCrewID${crew_instance.id}"] = "on"
                if (crew_instance.viewpos == 0) {
                    borderreached = true
                }
                movenum++
                if (movefirstpos == -1 || crew_instance.viewpos < movefirstpos) {
                    movefirstpos = crew_instance.viewpos
                }
            }
        }
        Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] != "on") {
                if (crew_instance.viewpos >= movefirstpos && crew_instance.viewpos < movefirstpos + movenum) {
                    crew_instance.viewpos += movenum
                    crew_instance.save()
                }
            }
        }

		// modify showLimitStartPos
		if (session.showLimit) {
			if (movefirstpos < session.showLimitStartPos) {
				if (movenum == 1) {
					if (session.showLimitStartPos > session.showLimitCrewNum) {
						session.showLimitStartPos -= session.showLimitCrewNum
					} else {
						session.showLimitStartPos = 0
					}
				} else {
					session.showLimitStartPos--
				}
			}
		}
		
		// restore selection if not top
        if (!borderreached) {
            crew.selectedcrewids = selected_crewids
        }
        return crew
    }
    
    //--------------------------------------------------------------------------
    Map movedownCrew(Contest contestInstance, Map params, session)
    {
        Map crew = [:] 

		// moveable? (not bottom & connected selection)
        boolean borderreached = false
        boolean notmovable = false
        boolean off2on = false
        boolean on2off = false
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (crew_instance.viewpos + 1 == Crew.countByContest(contestInstance)) {
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
            crew.borderreached = true
            return crew
        }
        if (notmovable) {
            crew.message = getMsg('fc.test.notmovable')
            crew.error = true
            return crew
        }
        
		// move tasks
        int movenum = 0
        int movefirstpos = -1
        Map selected_crewids = [selectedCrewID:""]
        borderreached = false
        Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                crew_instance.viewpos++
                crew_instance.save()
                selected_crewids["selectedCrewID${crew_instance.id}"] = "on"
                if (crew_instance.viewpos + 1 == Crew.countByContest(contestInstance)) {
					borderreached = true
                }
                movenum++
                if (movefirstpos == -1 || crew_instance.viewpos < movefirstpos) {
                    movefirstpos = crew_instance.viewpos
                }
            }
        }
        Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] != "on") {
                if (crew_instance.viewpos >= movefirstpos && crew_instance.viewpos < movefirstpos + movenum) {
                    crew_instance.viewpos -= movenum
                    crew_instance.save()
                }
            }
        }

		// modify showLimitStartPos
		if (session.showLimit) {
			if (movefirstpos + movenum > session.showLimitStartPos + session.showLimitCrewNum) {
				if (movenum == 1) {
					int crew_num = Crew.countByContest(session.lastContest)
					if (session.showLimitStartPos + session.showLimitCrewNum < crew_num) {
						session.showLimitStartPos += session.showLimitCrewNum
					}
				} else {
					session.showLimitStartPos++
				}
			}
		}
		
		// restore selection if not bottom
        if (!borderreached) {
            crew.selectedcrewids = selected_crewids
        }
        return crew
    }
    
    //--------------------------------------------------------------------------
    Map printCrews(Map params,printparams)
    {
        Map crews = [:]

        // Print crews
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/crew/listprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
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
    Map importCrews(String fileName, String loadFileName, Contest contestInstance)
    {
		printstart "importCrews $fileName [$loadFileName]"
		int new_crew_num = 0
		int new_crew_error_num = 0
		int exist_crew_num = 0
		try {
			CrewExcelImporter crew_importer = new CrewExcelImporter(loadFileName)
			
			List crews = crew_importer.getCrews()
			crews.each { Map crew_entry ->
				printstart crew_entry.name
				Crew crew = Crew.findByNameAndContest(crew_entry.name, contestInstance)
				if (crew) {
					printdone "Crew already exists."
					exist_crew_num++
				} else {
					Map ret = saveCrew(crew_entry,contestInstance)
					printdone "Created $ret"
					if (ret.saved) {
						new_crew_num++
					} else {
						new_crew_error_num++
					}
				}
			}
		} catch (Exception e) {
			Map ret = ['error':true,'message':getMsg('fc.notimported.msg',[fileName,e.getMessage()])]
			printerror ret
			return ret
		}
		
		Map ret
		if (new_crew_num) {
			ret = ['saved':true,'message':getMsg('fc.imported.crews',[fileName,new_crew_num])]
		} else if (new_crew_error_num) {
			ret = ['error':true,'message':getMsg('fc.notimported.crews.error',[fileName,new_crew_error_num])]
		} else {
			ret = ['error':true,'message':getMsg('fc.notimported.crews',[fileName])]
		}
		printdone ret
		return ret
	}
	
    //--------------------------------------------------------------------------
    Map getTest(Map params)
    {
        Test test_instance = Test.get(params.id)

        if (!test_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
        
        return ['instance':test_instance]
    }

    //--------------------------------------------------------------------------
    Map printflightplanTest(Map params,printparams)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }
        
		// Calculate timetable version
		Task task_instance = test.instance.task
		if (task_instance.timetableModified) {
			task_instance.timetableVersion++
			task_instance.timetableModified = false
			task_instance.save(flush:true)
		}
		
        // Print flightplan
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/flightplanprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.print.error',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printplanningtaskTest(Map params,printparams,withResult)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print flightplan
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/planningtaskprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}&results="
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
            test.message = getMsg('fc.print.error',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printdebriefingTest(Map params,printparams)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print debriefing
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/debriefingprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.test.debriefing.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printdebriefingsTask(Map params,printparams)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // Print debriefings
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
				if (!test_instance.crew.disabled) {
	                String url = "${printparams.baseuri}/test/debriefingprintable/${test_instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
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
            }
            renderer.finishPDF()

            task.content = content 
        }
        catch (Throwable e) {
            task.message = getMsg('fc.test.debriefing.printerror',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map planningtaskresultscompleteTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }
		
		test.instance.properties = params

        test.instance.planningTestComplete = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.planningresults.points',["${test.instance.planningTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
	Map planningtaskresultssaveTest(Map params)
	{
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

		test.instance.properties = params
		
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.planningresults.points',["${test.instance.planningTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
	}
	
    //--------------------------------------------------------------------------
    Map planningtaskresultsopenTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
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
    Map flightresultscompleteTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

		test.instance.properties = params

        test.instance.flightTestComplete = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.flightresults.points',["${test.instance.flightTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultssaveTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

		test.instance.properties = params

        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.flightresults.points',["${test.instance.flightTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map flightresultsreopenTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
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
    Map observationresultscompleteTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.properties = params
		
        test.instance.observationTestComplete = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.observationresults.points',["${test.instance.observationTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map observationresultsopenTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.observationTestComplete = false
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map observationresultssaveTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.properties = params
		
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.observationresults.points',["${test.instance.observationTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map landingresultscompleteTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.properties = params
		
        test.instance.landingTestComplete = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.landingresults.points',["${test.instance.landingTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map landingresultsopenTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.landingTestComplete = false
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map landingresultssaveTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.properties = params
		
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.landingresults.points',["${test.instance.landingTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map specialresultscompleteTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.properties = params
		
        test.instance.specialTestComplete = true
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.specialresults.points',["${test.instance.specialTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map specialresultsopenTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.specialTestComplete = false
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map specialresultssaveTest(Map params)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.properties = params
		
        calculateTestPenalties(test.instance)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.specialresults.points',["${test.instance.specialTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    private void calculateTestPenalties(Test testInstance)
    {
    	printstart "calculateTestPenalties: $testInstance.crew.name"
    	
    	Contest contest_instance = testInstance.task.contest 
    	
    	// planningTestPenalties
    	testInstance.planningTestLegPenalties = 0
        testInstance.planningTestLegComplete = false
        if (TestLegPlanning.findByTest(testInstance)) {
        	testInstance.planningTestLegComplete = true
        }
    	TestLegPlanning.findAllByTest(testInstance).each { TestLegPlanning testlegplanning_instance ->
    		if (testlegplanning_instance.resultEntered) {
    			testInstance.planningTestLegPenalties += testlegplanning_instance.penaltyTrueHeading
    			testInstance.planningTestLegPenalties += testlegplanning_instance.penaltyLegTime
    		} else {
    			testInstance.planningTestLegComplete = false
    		}
    	}
        if (testInstance.planningTestLegPenalties > contest_instance.planningTestMaxPoints) {
            testInstance.planningTestLegPenalties = contest_instance.planningTestMaxPoints
        }
    	testInstance.planningTestPenalties = testInstance.planningTestLegPenalties
    	if (testInstance.planningTestGivenTooLate) {
    		testInstance.planningTestPenalties += contest_instance.planningTestPlanTooLatePoints
    	}
    	if (testInstance.planningTestExitRoomTooLate) {
    		testInstance.planningTestPenalties += contest_instance.planningTestExitRoomTooLatePoints
    	}
    	
    	// flightTestPenalties
    	testInstance.flightTestCheckPointPenalties = 0
    	testInstance.flightTestCheckPointsComplete = false
    	if (CoordResult.findByTest(testInstance)) {
    		testInstance.flightTestCheckPointsComplete = true
    	}
    	CoordResult.findAllByTest(testInstance).each { CoordResult coordresult_instance ->
    		if (coordresult_instance.resultEntered) {
  				testInstance.flightTestCheckPointPenalties += coordresult_instance.penaltyCoord
    		} else {
    			testInstance.flightTestCheckPointsComplete = false
    		}
    		if (coordresult_instance.planProcedureTurn) {
	    		if (coordresult_instance.resultProcedureTurnEntered) {
		    		if (coordresult_instance.resultProcedureTurnNotFlown) {
		    			testInstance.flightTestCheckPointPenalties += contest_instance.flightTestProcedureTurnNotFlownPoints
		    		}
	    		} else {
	    			testInstance.flightTestCheckPointsComplete = false
	    		}
    		}
    		if (coordresult_instance.resultAltitude && coordresult_instance.resultMinAltitudeMissed) {
    			testInstance.flightTestCheckPointPenalties += contest_instance.flightTestMinAltitudeMissedPoints
    		}
    		if (coordresult_instance.resultBadCourseNum) {
    			testInstance.flightTestCheckPointPenalties += coordresult_instance.resultBadCourseNum * contest_instance.flightTestBadCoursePoints  
    		}
    	}
    	testInstance.flightTestPenalties = testInstance.flightTestCheckPointPenalties
        if (testInstance.flightTestTakeoffMissed) {
            testInstance.flightTestPenalties += contest_instance.flightTestTakeoffMissedPoints
        }
        if (testInstance.flightTestBadCourseStartLanding) {
            testInstance.flightTestPenalties += contest_instance.flightTestBadCourseStartLandingPoints
        }
        if (testInstance.flightTestLandingTooLate) {
            testInstance.flightTestPenalties += contest_instance.flightTestLandingToLatePoints
        }
        if (testInstance.flightTestGivenTooLate) {
            testInstance.flightTestPenalties += contest_instance.flightTestGivenToLatePoints
        }
    	
		// observationTestPenalties
		testInstance.observationTestPenalties = testInstance.observationTestRoutePhotoPenalties +
		                                        testInstance.observationTestTurnPointPhotoPenalties +
												testInstance.observationTestGroundTargetPenalties
		
        // landingTestPenalties
        // testInstance.landingTestPenalties = ... keine Detailpunkte
        
        // taskPenalties
    	testInstance.CalculateTestPenalties()

    	printdone "Planning:$testInstance.planningTestPenalties Flight:$testInstance.flightTestPenalties Observation:$testInstance.observationTestPenalties Landing:$testInstance.landingTestPenalties Summary:$testInstance.taskPenalties"
    }

    //--------------------------------------------------------------------------
    Map getTestLegFlight(Map params)
    {
        TestLegFlight testlegflight_instance = TestLegFlight.get(params.id)

        if (!testlegflight_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegflight'),params.id])]
        }
        
        return ['instance':testlegflight_instance]
    }

    //--------------------------------------------------------------------------
    Map getFlightTest(Map params)
    {
        FlightTest flighttest_instance = FlightTest.get(params.id)

        if (!flighttest_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
        }
        
        return ['instance':flighttest_instance]
    }

    //--------------------------------------------------------------------------
    Map updateFlightTest(Map params)
    {
		printstart "updateFlightTest"
		
        FlightTest flighttest_instance = FlightTest.get(params.id)
        
        if (flighttest_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(flighttest_instance.version > version) {
                    flighttest_instance.errors.rejectValue("version", "flightTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':flighttest_instance]
                }
            }

			Route old_route = flighttest_instance.route             
            flighttest_instance.properties = params

			if (old_route != flighttest_instance.route) {
		        Test.findAllByTask(flighttest_instance.task).each { Test test_instance ->
		        	test_instance.timeCalculated = false
					test_instance.ResetFlightTestResults()
					test_instance.CalculateTestPenalties()
		            test_instance.save()
		        }
				println "Calculated times have been reset." 
			}
            
            if(!flighttest_instance.hasErrors() && flighttest_instance.save()) {
                Map ret = ['instance':flighttest_instance,'saved':true,'message':getMsg('fc.updated',["${flighttest_instance.name()}"])]
				printdone ret.message
				return ret
            } else {
				printerror ""
                return ['instance':flighttest_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
			printerror ret.message
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map createFlightTest(Map params, Contest contestInstance)
    {
        if (!Route.countByContest(contestInstance)) {
            return ['message':getMsg('fc.route.notfound'),'error':true,
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':params.task.id]
        }
         
        FlightTest flighttest_instance = new FlightTest()
        flighttest_instance.properties = params
        return ['instance':flighttest_instance]
    }

    
    //--------------------------------------------------------------------------
    Map saveFlightTest(Map params)
    {
        FlightTest flighttest_instance = new FlightTest(params)
        
        flighttest_instance.task = Task.get( params.taskid )
        
        if (!flighttest_instance.direction) {
        	flighttest_instance.direction = 0
        }
        if (!flighttest_instance.speed) {
        	flighttest_instance.speed = 0
        }
        
        if(!flighttest_instance.hasErrors() && flighttest_instance.save()) {

            Task task_instance = Task.get( params.taskid )
            task_instance.flighttest = flighttest_instance
            task_instance.save()

            Wind windInstance = new Wind(direction:flighttest_instance.direction,speed:flighttest_instance.speed)
            windInstance.save()
            
            FlightTestWind flighttestwind_instance = new FlightTestWind(params)
            flighttestwind_instance.wind = windInstance
            flighttestwind_instance.flighttest = flighttest_instance
            flighttestwind_instance.save()

            return ['instance':flighttest_instance,'saved':true,'message':getMsg('fc.created',["${flighttest_instance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':task_instance.id]
        } else {
            return ['instance':flighttest_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteFlightTest(Map params)
    {
        FlightTest flighttest_instance = FlightTest.get(params.id)
        
        if (flighttest_instance) {
			boolean flighttest_used = false
			for (FlightTestWind fighttestwind_instance in flighttest_instance.flighttestwinds) {
				if (Test.findByFlighttestwind(fighttestwind_instance)) {
					flighttest_used = true
					break
				}
			}
			if (!flighttest_used) {
	            try {
	                Task task_instance = Task.get( flighttest_instance.task.id )
	                task_instance.flighttest = null
	                task_instance.save()
	                
	                flighttest_instance.delete()
	                
	                return ['deleted':true,'message':getMsg('fc.deleted',["${flighttest_instance.name()}"])]
	            }
	            catch(org.springframework.dao.DataIntegrityViolationException e) {
	                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.flighttest'),params.id])]
	            }
			} else {
				return ['notdeleted':true,'message':getMsg('fc.flighttest.notdeleted')]
			}
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getFlightTestWind(Map params)
    {
        FlightTestWind flighttestwind_instance = FlightTestWind.get(params.id)

        if (!flighttestwind_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttestwind'),params.id])]
        }
        
        flighttestwind_instance.direction = flighttestwind_instance.wind.direction
        flighttestwind_instance.speed = flighttestwind_instance.wind.speed
        
        return ['instance':flighttestwind_instance]
    }

    //--------------------------------------------------------------------------
    Map updateFlightTestWind(Map params)
    {
		printstart "updateFlightTestWind"
		
        FlightTestWind flighttestwind_instance = FlightTestWind.get(params.id)
        
        if (flighttestwind_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(flighttestwind_instance.version > version) {
                    flighttestwind_instance.errors.rejectValue("version", "flightTestWind.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':flighttestwind_instance]
                }
            }
            
			BigDecimal old_direction = flighttestwind_instance.wind.direction
			BigDecimal old_speed = flighttestwind_instance.wind.speed  
            flighttestwind_instance.properties = params

            flighttestwind_instance.wind.direction = flighttestwind_instance.direction
            flighttestwind_instance.wind.speed = flighttestwind_instance.speed
            
			if (old_direction != flighttestwind_instance.wind.direction || old_speed != flighttestwind_instance.wind.speed) {
		        Test.findAllByTask(flighttestwind_instance.flighttest.task).each { Test test_instance ->
		        	test_instance.timeCalculated = false
					test_instance.ResetFlightTestResults()
					test_instance.CalculateTestPenalties()
		            test_instance.save()
		        }
				println "Calculated times have been reset." 
			}
			
            if(!flighttestwind_instance.hasErrors() && flighttestwind_instance.save()) {
                Map ret = ['instance':flighttestwind_instance,'saved':true,'message':getMsg('fc.updated',["${flighttestwind_instance.name()}"]),
						   'flighttestid':flighttestwind_instance.flighttest.id]
				printdone ret.message
				return ret
            } else {
				printerror ""
                return ['instance':flighttestwind_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.flighttestwind'),params.id])]
			printerror ret.message
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map createFlightTestWind(Map params)
    {
        FlightTestWind flighttestwind_instance = new FlightTestWind()
        flighttestwind_instance.properties = params
        return ['instance':flighttestwind_instance]
    }

    
    //--------------------------------------------------------------------------
    Map saveFlightTestWind(Map params)
    {
        FlightTestWind flighttestwind_instance = new FlightTestWind(params)
        
        flighttestwind_instance.flighttest = FlightTest.get(params.flighttestid)
        
        Wind windInstance = new Wind(params)
        if(!windInstance.hasErrors() && windInstance.save()) {
            flighttestwind_instance.wind = windInstance
        }
        
        if(!flighttestwind_instance.hasErrors() && flighttestwind_instance.save()) {
            return ['instance':flighttestwind_instance,'saved':true,'message':getMsg('fc.created',["${flighttestwind_instance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,
                    'taskid':flighttestwind_instance.flighttest.task.id,
                    'flighttestid':flighttestwind_instance.flighttest.id]
        } else {
            return ['instance':flighttestwind_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteFlightTestWind(Map params)
    {
        FlightTestWind flighttestwind_instance = FlightTestWind.get(params.id)
        
        if (flighttestwind_instance) {
			if (!Test.findByFlighttestwind(flighttestwind_instance)) {
	            try {
	                flighttestwind_instance.delete()
	                return ['deleted':true,'message':getMsg('fc.deleted',["${flighttestwind_instance.name()}"]),
	                        'flighttestid':flighttestwind_instance.flighttest.id]
	            }
	            catch(org.springframework.dao.DataIntegrityViolationException e) {
	                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.flighttestwind'),params.id])]
	            }
			} else {
				return ['notdeleted':true,'message':getMsg('fc.flighttestwind.notdeleted')]
			}
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttestwind'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getPlanningTest(Map params)
    {
        PlanningTest planningtest_instance = PlanningTest.get(params.id)

        if (!planningtest_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtest'),params.id])]
        }
        
        return ['instance':planningtest_instance]
    }

    //--------------------------------------------------------------------------
    Map updatePlanningTest(Map params)
    {
		printstart "updatePlanningTest"
		
        PlanningTest planningtest_instance = PlanningTest.get(params.id)
        
        if (planningtest_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(planningtest_instance.version > version) {
                    planningtest_instance.errors.rejectValue("version", "planningTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':planningtest_instance]
                }
            }
            
            planningtest_instance.properties = params
            if(!planningtest_instance.hasErrors() && planningtest_instance.save()) {
                Map ret = ['instance':planningtest_instance,'saved':true,'message':getMsg('fc.updated',["${planningtest_instance.name()}"])]
				printdone ret.message
				return ret
            } else {
				printerror ""
                return ['instance':planningtest_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.planningtest'),params.id])]
			printerror ret.message
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map createPlanningTest(Map params, Contest contestInstance)
    {
        if (!Route.countByContest(contestInstance)) {
            return ['message':getMsg('fc.route.notfound'),'error':true,
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':params.task.id]
        }
         
        PlanningTest planningtest_instance = new PlanningTest()
        planningtest_instance.properties = params
        return ['instance':planningtest_instance]
    }

    
    //--------------------------------------------------------------------------
    Map savePlanningTest(Map params)
    {
        PlanningTest planningtest_instance = new PlanningTest(params)
        
        planningtest_instance.task = Task.get( params.taskid )
        
        if (!planningtest_instance.direction) {
            planningtest_instance.direction = 0
        }
        if (!planningtest_instance.speed) {
            planningtest_instance.speed = 0
        }
        
        if(!planningtest_instance.hasErrors() && planningtest_instance.save()) {

            if (params.route) {
                Wind windInstance = new Wind(direction:planningtest_instance.direction,speed:planningtest_instance.speed)
                windInstance.save()
                
                PlanningTestTask planningtesttask_instance = new PlanningTestTask(params)
                planningtesttask_instance.planningtest = planningtest_instance
                planningtesttask_instance.title = params.taskTitle
                planningtesttask_instance.idTitle = 1
                planningtesttask_instance.wind = windInstance
                if (planningtesttask_instance.hasErrors() || !planningtesttask_instance.save()) {
                    planningtest_instance.delete()
                    return ['instance':planningtest_instance]
                }
            }
            
            Task task_instance = Task.get( params.taskid )
            task_instance.planningtest = planningtest_instance
            task_instance.save()
            
            return ['instance':planningtest_instance,'saved':true,'message':getMsg('fc.created',["${planningtest_instance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':task_instance.id]
        } else {
            return ['instance':planningtest_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deletePlanningTest(Map params)
    {
        PlanningTest planningtest_instance = PlanningTest.get(params.id)
        
        if (planningtest_instance) {
			boolean planningtest_used = false
			for (PlanningTestTask planningtesttask_instance in planningtest_instance.planningtesttasks) {
				if (Test.findByPlanningtesttask(planningtesttask_instance)) {
					planningtest_used = true
					break
				}
			}
			if (!planningtest_used) {			
	            try {
	                Task task_instance = Task.get( planningtest_instance.task.id )
	                task_instance.planningtest = null
	                task_instance.save()
	                
	                planningtest_instance.delete()
	                
	                return ['deleted':true,'message':getMsg('fc.deleted',["${planningtest_instance.name()}"])]
	            }
	            catch(org.springframework.dao.DataIntegrityViolationException e) {
	                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.planningtest'),params.id])]
	            }
			} else {
				return ['notdeleted':true,'message':getMsg('fc.planningtest.notdeleted')]
			}
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getPlanningTestTask(Map params)
    {
        PlanningTestTask planningtesttask_instance = PlanningTestTask.get(params.id)

        if (!planningtesttask_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtesttask'),params.id])]
        }
        
        planningtesttask_instance.direction = planningtesttask_instance.wind.direction
        planningtesttask_instance.speed = planningtesttask_instance.wind.speed
        
        return ['instance':planningtesttask_instance]
    }

    //--------------------------------------------------------------------------
    Map updatePlanningTestTask(Map params)
    {
		printstart "updatePlanningTestTask"
		
    	PlanningTestTask planningtesttask_instance = PlanningTestTask.get(params.id)
        
        if (planningtesttask_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(planningtesttask_instance.version > version) {
                    planningtesttask_instance.errors.rejectValue("version", "planningTestTask.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':planningtesttask_instance]
                }
            }
            
			Route old_route = planningtesttask_instance.route
			BigDecimal old_direction = planningtesttask_instance.wind.direction 
			BigDecimal old_speed = planningtesttask_instance.wind.speed
            planningtesttask_instance.properties = params
            
            if (!planningtesttask_instance.direction) {
            	planningtesttask_instance.direction = 0
            }
            if (!planningtesttask_instance.speed) {
            	planningtesttask_instance.speed = 0
            }
            
            planningtesttask_instance.wind.direction = planningtesttask_instance.direction
            planningtesttask_instance.wind.speed = planningtesttask_instance.speed

			if (old_route != planningtesttask_instance.route || old_direction != planningtesttask_instance.wind.direction || old_speed != planningtesttask_instance.wind.speed) {
	            Test.findAllByTask(planningtesttask_instance.planningtest.task).each { Test test_instance ->
	                calulateTestLegPlannings(test_instance)
					test_instance.ResetPlanningTestResults()
					test_instance.CalculateTestPenalties()
	                test_instance.save()
	            }
				println "TestLegPlannings have been recalculated." 
			}

            if(!planningtesttask_instance.hasErrors() && planningtesttask_instance.save()) {
                Map ret = ['instance':planningtesttask_instance,'saved':true,'message':getMsg('fc.updated',["${planningtesttask_instance.name()}"]),
					       'planningtestid':planningtesttask_instance.planningtest.id]
				printdone ret.message
				return ret
            } else {
				printerror ""
                return ['instance':planningtesttask_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.planningtesttask'),params.id])]
			printerror ret.message
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map createPlanningTestTask(Map params, Contest contestInstance)
    {
        if (!Route.countByContest(contestInstance)) {
            return ['message':getMsg('fc.route.notfound'),'error':true,'planningtestid':params.planningtestid]
        }
         
        PlanningTestTask planningtesttask_instance = new PlanningTestTask()
        planningtesttask_instance.properties = params
        return ['instance':planningtesttask_instance]
    }

    
    //--------------------------------------------------------------------------
    Map savePlanningTestTask(Map params)
    {
        PlanningTestTask planningtesttask_instance = new PlanningTestTask(params)

        planningtesttask_instance.planningtest = PlanningTest.get(params.planningtestid)
        planningtesttask_instance.idTitle = PlanningTestTask.countByPlanningtest(planningtesttask_instance.planningtest) + 1
        
        if (!planningtesttask_instance.direction) {
            planningtesttask_instance.direction = 0
        }
        if (!planningtesttask_instance.speed) {
            planningtesttask_instance.speed = 0
        }
        
        Wind windInstance = new Wind(direction:planningtesttask_instance.direction,speed:planningtesttask_instance.speed)
        if(!windInstance.hasErrors() && windInstance.save()) {
            planningtesttask_instance.wind = windInstance
        }
        
        if(!planningtesttask_instance.hasErrors() && planningtesttask_instance.save()) {
            return ['instance':planningtesttask_instance,'saved':true,'message':getMsg('fc.created',["${planningtesttask_instance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,
                    'taskid':planningtesttask_instance.planningtest.task.id,
                    'planningtestid':planningtesttask_instance.planningtest.id]
        } else {
            return ['instance':planningtesttask_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deletePlanningTestTask(Map params)
    {
        PlanningTestTask planningtesttask_instance = PlanningTestTask.get(params.id)
        
        if (planningtesttask_instance) {
			if (!Test.findByPlanningtesttask(planningtesttask_instance)) {
				try {
	                PlanningTest planningtest_instance = planningtesttask_instance.planningtest 
	                    
	                planningtesttask_instance.delete()
	
	                PlanningTestTask.findAllByPlanningtest(planningtest_instance).eachWithIndex { PlanningTestTask planningtesttask_instance2, int index  -> 
	                    planningtesttask_instance2.idTitle = index + 1
	                }
	                
	                return ['deleted':true,'message':getMsg('fc.deleted',["${planningtesttask_instance.name()}"]),
	                        'planningtestid':planningtesttask_instance.planningtest.id]
	            }
	            catch(org.springframework.dao.DataIntegrityViolationException e) {
	                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.planningtesttask'),params.id])]
	            }
			} else {
				return ['notdeleted':true,'message':getMsg('fc.planningtesttask.notdeleted')]
			}
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtesttask'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getTestLegPlanning(Map params)
    {
        TestLegPlanning testlegplanning_instance = TestLegPlanning.get(params.id)

        if (!testlegplanning_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanning'),params.id])]
        }
        
        return ['instance':testlegplanning_instance]
    }

    //--------------------------------------------------------------------------
    Map getTestLegPlanningResult(Map params)
    {
        TestLegPlanning testlegplanning_instance = TestLegPlanning.get(params.id)

        if (!testlegplanning_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
        
        // calculate resultLegTimeInput
        GregorianCalendar time = new GregorianCalendar()
        time.set(Calendar.HOUR_OF_DAY, 0)
        time.set(Calendar.MINUTE, 0)
        time.set(Calendar.SECOND, FcMath.Seconds(testlegplanning_instance.resultLegTime))
        testlegplanning_instance.resultLegTimeInput = FcMath.TimeStr(time.getTime())

		return ['instance':testlegplanning_instance]
    }

    //--------------------------------------------------------------------------
    Map updateTestLegPlanningResult(Map params)
    {
    	TestLegPlanning testlegplanning_instance = TestLegPlanning.get(params.id)
        if(testlegplanning_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(testlegplanning_instance.version > version) {
                    testlegplanning_instance.errors.rejectValue("version", "testLegPlanning.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':testlegplanning_instance]
                }
            }

            testlegplanning_instance.properties = params
            
            Map ret = calculateLegPlanningInstance(testlegplanning_instance)
            if (ret)
            {
            	return ret
            }
            
            calculateTestPenalties(testlegplanning_instance.test)
            
            if(!testlegplanning_instance.hasErrors() && testlegplanning_instance.save()) {
				String msg = "${getMsg('fc.updated',["${testlegplanning_instance.name()}"])} ${getMsg('fc.testlegplanning.points',["${testlegplanning_instance.penaltyTrueHeading}","${testlegplanning_instance.penaltyLegTime}"])}"
                return ['instance':testlegplanning_instance,'saved':true,'message':msg]
            } else {
            	return ['instance':testlegplanning_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetTestLegPlanningResult(Map params)
    {
        TestLegPlanning testlegplanning_instance = TestLegPlanning.get(params.id)
        if(testlegplanning_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(testlegplanning_instance.version > version) {
                    testlegplanning_instance.errors.rejectValue("version", "testLegPlanning.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':testlegplanning_instance]
                }
            }

            // reset results
		    testlegplanning_instance.ResetResults()
            
            calculateTestPenalties(testlegplanning_instance.test)
            
            if(!testlegplanning_instance.hasErrors() && testlegplanning_instance.save()) {
                return ['instance':testlegplanning_instance,'saved':true,'message':getMsg('fc.updated',["${testlegplanning_instance.name()}"])]
            } else {
                return ['instance':testlegplanning_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    private Map calculateLegPlanningInstance(TestLegPlanning testLegPlanningInstance)
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
        
        Contest contest_instance = testLegPlanningInstance.test.task.contest
        
        // calculate penaltyTrueHeading
        int planTrueHeading = FcMath.Grads(testLegPlanningInstance.planTrueHeading)
        if (planTrueHeading > 180) {
            planTrueHeading -= 360
        }
        int resultTrueHeading = testLegPlanningInstance.resultTrueHeading
        if (resultTrueHeading > 180) {
            resultTrueHeading -= 360
        }
        int diffTrueHeading =  Math.abs(planTrueHeading - resultTrueHeading)
        if (diffTrueHeading > contest_instance.planningTestDirectionCorrectGrad) {
            testLegPlanningInstance.penaltyTrueHeading = contest_instance.planningTestDirectionPointsPerGrad * (diffTrueHeading - contest_instance.planningTestDirectionCorrectGrad)
        } else {
            testLegPlanningInstance.penaltyTrueHeading = 0
        }
        
        // calculate penaltyLegTime
        int planLegTimeSeconds = FcMath.Seconds(testLegPlanningInstance.planLegTime)
        int resultLegTimeSeconds = FcMath.Seconds(testLegPlanningInstance.resultLegTime)

        int diffLegTime =  Math.abs(planLegTimeSeconds - resultLegTimeSeconds)
        if (diffLegTime > contest_instance.planningTestTimeCorrectSecond) {
            testLegPlanningInstance.penaltyLegTime = contest_instance.planningTestTimePointsPerSecond * (diffLegTime - contest_instance.planningTestTimeCorrectSecond)
        } else {
            testLegPlanningInstance.penaltyLegTime = 0
        }
        
        testLegPlanningInstance.resultEntered = true
        
        return [:]
    }
    
    //--------------------------------------------------------------------------
    Map getCoordResult(Map params)
    {
        CoordResult coordresult_instance = CoordResult.get(params.id)

        if (!coordresult_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordresult'),params.id])]
        }
        
        // calculate resultCpTimeInput
        coordresult_instance.resultCpTimeInput = FcMath.TimeStr(coordresult_instance.resultCpTime)

        return ['instance':coordresult_instance]
    }

    //--------------------------------------------------------------------------
    Map updateCoordResult(Map params)
    {
        CoordResult coordresult_instance = CoordResult.get(params.id)
        if(coordresult_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(coordresult_instance.version > version) {
                    coordresult_instance.errors.rejectValue("version", "coordResult.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordresult_instance]
                }
            }

            coordresult_instance.properties = params
            
            Map ret = calculateCoordResultInstance(coordresult_instance,false)
            if (ret)
            {
                return ret
            }
            
            calculateTestPenalties(coordresult_instance.test)
            
            if(!coordresult_instance.hasErrors() && coordresult_instance.save()) {
				String altitude_points = "0"
				if (coordresult_instance.resultAltitude && coordresult_instance.resultMinAltitudeMissed) {
					altitude_points = coordresult_instance.test.crew.contest.flightTestMinAltitudeMissedPoints.toString()
				}
				String msg = "${getMsg('fc.updated',["${coordresult_instance.name()}"])} ${getMsg('fc.coordresult.points',[altitude_points,"${coordresult_instance.penaltyCoord}","${coordresult_instance.resultBadCourseNum * coordresult_instance.test.crew.contest.flightTestBadCoursePoints}"])}"
                return ['instance':coordresult_instance,'saved':true,'message':msg]
            } else {
                return ['instance':coordresult_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map updateCoordResultProcedureTurn(Map params)
    {
        CoordResult coordresult_instance = CoordResult.get(params.id)
        if(coordresult_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(coordresult_instance.version > version) {
                    coordresult_instance.errors.rejectValue("version", "coordResult.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordresult_instance]
                }
            }

            coordresult_instance.properties = params
            coordresult_instance.resultProcedureTurnEntered = true
            
            calculateTestPenalties(coordresult_instance.test)
            
            if(!coordresult_instance.hasErrors() && coordresult_instance.save()) {
                return ['instance':coordresult_instance,'saved':true,'message':getMsg('fc.updated',["${coordresult_instance.name()}"])]
            } else {
                return ['instance':coordresult_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetCoordResult(Map params)
    {
        CoordResult coordresult_instance = CoordResult.get(params.id)
        if(coordresult_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(coordresult_instance.version > version) {
                    coordresult_instance.errors.rejectValue("version", "coordResult.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordresult_instance]
                }
            }

            // reset results
            coordresult_instance.ResetResults()

            calculateTestPenalties(coordresult_instance.test)
            
            if(!coordresult_instance.hasErrors() && coordresult_instance.save()) {
                return ['instance':coordresult_instance,'saved':true,'message':getMsg('fc.updated',["${coordresult_instance.name()}"])]
            } else {
                return ['instance':coordresult_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    private Map calculateCoordResultInstance(CoordResult coordResultInstance, boolean calculateUTC)
    {
		println "calculateCoordResultInstance '${coordResultInstance.title()}' (${coordResultInstance.mark}) $coordResultInstance.resultCpTimeInput"
		
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
        Contest contest_instance = coordResultInstance.test.task.contest
        if (calculateUTC) {
			GregorianCalendar result_cptime = new GregorianCalendar() 
			result_cptime.setTime(coordResultInstance.resultCpTime)
        	Date timezone_date = Date.parse("HH:mm",contest_instance.timeZone)
        	GregorianCalendar timezone_calendar = new GregorianCalendar()
        	timezone_calendar.setTime(timezone_date)
        	result_cptime.add(Calendar.HOUR_OF_DAY, timezone_calendar.get(Calendar.HOUR_OF_DAY))
        	result_cptime.add(Calendar.MINUTE, timezone_calendar.get(Calendar.MINUTE))
        	coordResultInstance.resultCpTime = result_cptime.getTime()
        }
        
        // calculate penaltyCoord
		String cp = "${coordResultInstance.test.task.disabledCheckPoints},"
        if (cp.contains("${coordResultInstance.title()},")) {
			coordResultInstance.penaltyCoord = 0
        } else if (coordResultInstance.resultCpNotFound) {
        	coordResultInstance.penaltyCoord = contest_instance.flightTestCpNotFoundPoints
        } else {
	        int plancptime_seconds = FcMath.Seconds(coordResultInstance.planCpTime)
	        int resultcptime_seconds = FcMath.Seconds(coordResultInstance.resultCpTime)
	        
	        int diffCpTime =  Math.abs(plancptime_seconds - resultcptime_seconds)
	        if (diffCpTime > contest_instance.flightTestCptimeCorrectSecond) {
	            coordResultInstance.penaltyCoord = contest_instance.flightTestCptimePointsPerSecond * (diffCpTime - contest_instance.flightTestCptimeCorrectSecond)
	        } else {
	            coordResultInstance.penaltyCoord = 0
	        }
	        if (coordResultInstance.penaltyCoord > contest_instance.flightTestCptimeMaxPoints) {
	        	coordResultInstance.penaltyCoord = contest_instance.flightTestCptimeMaxPoints
	        }
        }
        coordResultInstance.resultEntered = true
        
        // calculate resultMinAltitudeMissed
        if (coordResultInstance.resultAltitude) {
        	coordResultInstance.resultMinAltitudeMissed = coordResultInstance.resultAltitude < coordResultInstance.altitude
        }
        
        return [:]
    }
    
    //--------------------------------------------------------------------------
    private void calculateCoordResultInstancePenaltyCoord(CoordResult coordResultInstance)
    {
        Contest contest_instance = coordResultInstance.test.task.contest
		String cp = "${coordResultInstance.test.task.disabledCheckPoints},"
		
        if (cp.contains("${coordResultInstance.title()},")) {
			coordResultInstance.penaltyCoord = 0
        } else if (coordResultInstance.resultCpNotFound) {
        	coordResultInstance.penaltyCoord = contest_instance.flightTestCpNotFoundPoints
        } else {
	        int plancptime_seconds = FcMath.Seconds(coordResultInstance.planCpTime)
	        int resultcptime_seconds = FcMath.Seconds(coordResultInstance.resultCpTime)
	        
	        int diffCpTime =  Math.abs(plancptime_seconds - resultcptime_seconds)
	        if (diffCpTime > contest_instance.flightTestCptimeCorrectSecond) {
	            coordResultInstance.penaltyCoord = contest_instance.flightTestCptimePointsPerSecond * (diffCpTime - contest_instance.flightTestCptimeCorrectSecond)
	        } else {
	            coordResultInstance.penaltyCoord = 0
	        }
	        if (coordResultInstance.penaltyCoord > contest_instance.flightTestCptimeMaxPoints) {
	        	coordResultInstance.penaltyCoord = contest_instance.flightTestCptimeMaxPoints
	        }
        }
    }
    
    //--------------------------------------------------------------------------
    Map getWind(Map params)
    {
        Wind windInstance = Wind.get(params.id)

        if(!windInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.wind'),params.id])]
        }
        
        return ['instance':windInstance]
    }
    
    //--------------------------------------------------------------------------
    private Map calculateLegData(CoordRoute newCoordRouteInstance, CoordRoute lastCoordRouteInstance)
    {
		return AviationMath.calculateLeg(newCoordRouteInstance.latMath(),
			      				         newCoordRouteInstance.lonMath(),
										 lastCoordRouteInstance.latMath(),
										 lastCoordRouteInstance.lonMath())
    }
    
    //--------------------------------------------------------------------------
    private void newLeg(Route route, CoordRoute newCoordRouteInstance, CoordRoute lastCoordRouteInstance, CoordRoute lastCoordRouteTestInstance, BigDecimal measureDistance, BigDecimal lastMapMeasureDistance, BigDecimal lastMapMeasureTrueTrack) 
    {
    	printstart "newLeg: ${route.name()} ${newCoordRouteInstance.title()}"
    	
        // create routelegcoord_instance
    	if (lastCoordRouteInstance) {
    		Map legDataCoord = calculateLegData(newCoordRouteInstance, lastCoordRouteInstance)
	
	        String title_coord = "${lastCoordRouteInstance.titleCode()} -> ${newCoordRouteInstance.titleCode()}"
	        
	        RouteLegCoord routelegcoord_instance = new RouteLegCoord([coordTrueTrack:legDataCoord.dir,
	                                                                  coordDistance:legDataCoord.dis,
	                                                                  route:route,
	                                                                  title:title_coord,
																	  measureDistance:measureDistance,
	                                                                  legMeasureDistance:newCoordRouteInstance.legMeasureDistance,
	                                                                  legDistance:newCoordRouteInstance.legDistance,
	                                                                  measureTrueTrack:newCoordRouteInstance.measureTrueTrack
	                                                      ]) 
	        routelegcoord_instance.save()
        }

        // create routelegtest_instance
        if (lastCoordRouteInstance && lastCoordRouteTestInstance) {
            Map legDataTest = calculateLegData(newCoordRouteInstance, lastCoordRouteTestInstance)
            
	        String title_test = "${lastCoordRouteTestInstance.titleCode()} -> ${newCoordRouteInstance.titleCode()}"
            
	        if ( (lastCoordRouteTestInstance.type == CoordType.SP && newCoordRouteInstance.type == CoordType.TP) ||
	        	 (lastCoordRouteTestInstance.type == CoordType.SP && newCoordRouteInstance.type == CoordType.FP) ||
	        	 (lastCoordRouteTestInstance.type == CoordType.TP && newCoordRouteInstance.type == CoordType.TP) ||
	        	 (lastCoordRouteTestInstance.type == CoordType.TP && newCoordRouteInstance.type == CoordType.FP) )
	        {
	        	RouteLegTest routelegtest_instance = new RouteLegTest([coordTrueTrack:legDataTest.dir,
	        	                                                       coordDistance:legDataTest.dis,
	        	                                                       route:route,
	        	                                                       title:title_test,
																	   measureDistance:measureDistance,
	                                                                   legMeasureDistance:lastMapMeasureDistance,
	                                                                   legDistance:calculateMapDistance(route.contest,lastMapMeasureDistance),
	                                                                   measureTrueTrack:lastMapMeasureTrueTrack
	                                                        ])
	        	routelegtest_instance.save()
	        }
        }
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    private BigDecimal calculateMapDistance(Contest contestInstance, BigDecimal mapMeasureDistance)
    {
    	if (mapMeasureDistance == null) {
    		return null
    	}
  		return contestInstance.mapScale * mapMeasureDistance / mmPerNM
    }

    //--------------------------------------------------------------------------
    private BigDecimal calculateMapMeasure(Contest contestInstance, BigDecimal distanceValue)
    {
    	if (distanceValue == null) {
    		return null
    	}
  		return distanceValue * mmPerNM / contestInstance.mapScale 
    }

    //--------------------------------------------------------------------------
    private void removeAllRouteLegs(Route routeInstance) 
    {
    	printstart "removeAllRouteLegs: ${routeInstance.name()}"
    	
        RouteLegCoord.findAllByRoute(routeInstance).each { RouteLegCoord routelegcoord_instance ->
        	routelegcoord_instance.delete()
        }
        RouteLegTest.findAllByRoute(routeInstance).each { RouteLegTest routelegtest_instance ->
        	routelegtest_instance.delete()
        }
		
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    private void calculateRouteLegs(Route routeInstance) 
    {
        printstart "calculateRouteLegs: ${routeInstance.name()}"
        
        // remove all legs
    	removeAllRouteLegs(routeInstance)
        
        // calculate new legs
        CoordRoute last_coordroute_instance
        CoordRoute last_coordroute_test_instance
        BigDecimal last_mapmeasuredistance = null
        CoordRoute.findAllByRoute(routeInstance).each { CoordRoute coordroute_instance ->
      		last_mapmeasuredistance = addMapDistance(last_mapmeasuredistance,coordroute_instance.legMeasureDistance)
            newLeg(
				routeInstance, 
				coordroute_instance, 
				last_coordroute_instance, 
				last_coordroute_test_instance, 
				coordroute_instance.measureDistance, 
				last_mapmeasuredistance, 
				coordroute_instance.measureTrueTrack
			)
            last_coordroute_instance = coordroute_instance
            switch (coordroute_instance.type) {
	            case CoordType.SP:
	            case CoordType.TP:
	            case CoordType.FP:
	            	last_coordroute_test_instance = coordroute_instance
	            	last_mapmeasuredistance = null
	            	break
            }
        }
		printdone ""
    }

    //--------------------------------------------------------------------------
    private void calculateSecretLegRatio(Route routeInstance)
    {
        printstart "calculateSecretLegRatio: ${routeInstance.name()}"
        
        CoordRoute start_coordroute_instance
        CoordRoute start_coordroute_instance2
        CoordType last_coordtype
        BigDecimal last_legdirection = null
        
        CoordRoute.findAllByRoute(routeInstance).each { CoordRoute coordroute_instance ->
        	switch (coordroute_instance.type) {
        		case CoordType.SP:
        		case CoordType.TP:
        			start_coordroute_instance = coordroute_instance
        			break
        		case CoordType.SECRET:
	        		
        			// search end_coordroute_instance
	        		CoordRoute end_coordroute_instance
	        		boolean found = false
	        		CoordRoute.findAllByRoute(routeInstance).each { CoordRoute coordroute_instance2 ->
	                    if (found) {
	                        switch (coordroute_instance2.type) {
	                        	case CoordType.TP:
                                case CoordType.FP:
                                	end_coordroute_instance = coordroute_instance2
                                	found = false
                                	break
	                        }
	                    }
	        			if (coordroute_instance2 == start_coordroute_instance) {
	        				found = true
	        			}
	        		}

	        		if (end_coordroute_instance) { // && !coordroute_instance.secretLegRatio) {
						if (coordroute_instance.legDistance && end_coordroute_instance.legDistance) { // calculate from measure
							coordroute_instance.secretLegRatio = FcMath.RoundDistance(coordroute_instance.legDistance) / (FcMath.RoundDistance(coordroute_instance.legDistance) + FcMath.RoundDistance(end_coordroute_instance.legDistance))
							coordroute_instance.save()
						} else { // calulate from coordinates
			        		// get leg data
			                Map legData = calculateLegData(end_coordroute_instance, start_coordroute_instance)
			                
			        		// get secret leg data
			        		Map legDataSecret = calculateLegData(coordroute_instance, start_coordroute_instance)
			        		
			        		// calculate secretLegRatio
			        		if (legData.dis > 0) {
			        			coordroute_instance.secretLegRatio = FcMath.RoundDistance(legDataSecret.dis) / FcMath.RoundDistance(legData.dis)
			        			coordroute_instance.save()
			        		}
						}
	        		}
	        		break
        	}

        	// calculate planProcedureTurn
        	if (last_coordtype == CoordType.TP) {
                Map legData = calculateLegData(coordroute_instance, start_coordroute_instance2)
                if (last_legdirection != null) {
                    BigDecimal diffTrack = legData.dir - last_legdirection
                    if (diffTrack < 0) {
                        diffTrack += 360
                    }
                    if (diffTrack >= 90 && diffTrack < 270) {
                        coordroute_instance.planProcedureTurn = true
                        coordroute_instance.save()
                    }
                }
                last_legdirection = legData.dir
        	}
            switch (coordroute_instance.type) {
            	case CoordType.SP:
            		start_coordroute_instance2 = coordroute_instance 
            		break
                case CoordType.TP:
                case CoordType.FP:
                    start_coordroute_instance2 = coordroute_instance 
		            break
            }
        	last_coordtype = coordroute_instance.type
        }
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    private BigDecimal addMapDistance(lastMapMeasureDistance, addmeasuredistance)
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
    private void addTestingTime(Task taskInstance, Test testInstance)
    {
        if (testInstance.timeCalculated) {
            
            GregorianCalendar time = new GregorianCalendar() 
            time.setTime(testInstance.testingTime)
            
            // add testingTime
            time.add(Calendar.MINUTE, taskInstance.addTimeValue)
            testInstance.testingTime = time.getTime()
            
            // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
            calculateTimes(time, taskInstance, testInstance)
			testInstance.ResetFlightTestResults()
			testInstance.CalculateTestPenalties()
            testInstance.save()
            
            calulateCoordResult(testInstance)
            calulateTimetableWarnings(taskInstance)
			
			taskInstance.timetableModified = true
			taskInstance.save()
			testInstance.timetableVersion = taskInstance.timetableVersion + 1 
        }
    }
  
    //--------------------------------------------------------------------------
    private void subtractTestingTime(Task taskInstance, Test testInstance)
    {
        if (testInstance.timeCalculated) {
            
            GregorianCalendar time = new GregorianCalendar() 
            time.setTime(testInstance.testingTime)
            
            // subtract testingTime
            time.add(Calendar.MINUTE, -taskInstance.addTimeValue)
            testInstance.testingTime = time.getTime()
            
            // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
            calculateTimes(time, taskInstance, testInstance)
			testInstance.ResetFlightTestResults()
			testInstance.CalculateTestPenalties()
            testInstance.save()
            
            calulateCoordResult(testInstance)
            calulateTimetableWarnings(taskInstance)

			taskInstance.timetableModified = true
			taskInstance.save()
			testInstance.timetableVersion = taskInstance.timetableVersion + 1 
		}
    }

    //--------------------------------------------------------------------------
    private void calulateTimetableWarnings(Task taskInstance)
    {
        Date first_date = Date.parse("HH:mm",taskInstance.firstTime)
        Date last_arrival_time = first_date
        
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance ->
            
            // calculate arrivalTimeWarning by arrivalTime
            test_instance.arrivalTimeWarning = false
            if (last_arrival_time > test_instance.arrivalTime) {
                test_instance.arrivalTimeWarning = true
            }
            last_arrival_time = test_instance.arrivalTime
            
            // calculate takeoffTimeWarning by aircraft
            test_instance.takeoffTimeWarning = false
            boolean found_aircraft = false
            GregorianCalendar last_takeoff_time = null
            Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance2 ->
                if (test_instance.crew.aircraft == test_instance2.crew.aircraft) {
                	if (test_instance == test_instance2) {
                		found_aircraft = true
                	}
					if (!found_aircraft) {
	                	last_takeoff_time = new GregorianCalendar()
	                    last_takeoff_time.setTime(test_instance2.arrivalTime)
	                    last_takeoff_time.add(Calendar.MINUTE, taskInstance.minNextFlightDuration)
	                    last_takeoff_time.add(Calendar.MINUTE, taskInstance.preparationDuration)
	                }
                }
            }
            if (last_takeoff_time) {
                if (test_instance.takeoffTime < last_takeoff_time.getTime()) {
                    test_instance.takeoffTimeWarning = true
                }
            }
            
            test_instance.save()
        }

    }
 
    //--------------------------------------------------------------------------
    private int calulateTimetable(Task taskInstance)
    {
        printstart "calulateTimetable: ${taskInstance.name()}"
        
		int calculated_crew_num = 0
		
        Date first_date = Date.parse("HH:mm",taskInstance.firstTime)
        GregorianCalendar first_time = new GregorianCalendar() 
        first_time.setTime(first_date)

        GregorianCalendar start_time = new GregorianCalendar()
        start_time.set(Calendar.HOUR_OF_DAY, first_time.get(Calendar.HOUR_OF_DAY))
        start_time.set(Calendar.MINUTE,      first_time.get(Calendar.MINUTE))
        start_time.set(Calendar.SECOND,      0)

        BigDecimal last_task_tas = 9000.0
        Date last_arrival_time = first_date
        
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
		        if (test_instance.taskTAS > last_task_tas) { // faster aircraft
		        	start_time.add(Calendar.MINUTE, taskInstance.takeoffIntervalFasterAircraft - taskInstance.takeoffIntervalNormal)
		        }
		        
				if (!test_instance.timeCalculated) {
					calculateTime(test_instance, taskInstance, start_time, last_arrival_time)
					calulateCoordResult(test_instance)
					calculated_crew_num++
				}
		
				// next 
		        start_time.add(Calendar.MINUTE, taskInstance.takeoffIntervalNormal)
		        last_task_tas = test_instance.taskTAS
	            last_arrival_time = test_instance.arrivalTime
			}
        }

		if (calculated_crew_num) {
			taskInstance.timetableModified = true
			taskInstance.save()
			printdone ""
		} else {
			printdone "Nothing to calculate."
		}
		return calculated_crew_num
    }
 
    //--------------------------------------------------------------------------
	private void calculateTime(Test testInstance, Task taskInstance, GregorianCalendar startTime, Date lastArrivalTime)
	{
    	printstart "calculateTime: ${testInstance.crew.name}"
    	
        // calculate testingTime
        GregorianCalendar time = startTime.clone()
        testInstance.testingTime = time.getTime()
        
        // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
        calculateTimes(time, taskInstance, testInstance)
        
        // calculate arrivalTimeWarning by arrivalTime
        testInstance.arrivalTimeWarning = false
		if (lastArrivalTime) {
	        if (lastArrivalTime > testInstance.arrivalTime) {
	            testInstance.arrivalTimeWarning = true
	        }
		}
        
        // calculate takeoffTimeWarning by aircraft
        testInstance.takeoffTimeWarning = false
        boolean found_aircraft = false
        GregorianCalendar last_takeoff_time = null
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance2 ->
            if (testInstance.crew.aircraft == test_instance2.crew.aircraft) {
                if (testInstance == test_instance2) {
                    found_aircraft = true
                }
                if (!found_aircraft) {
                    last_takeoff_time = new GregorianCalendar()
                    last_takeoff_time.setTime(test_instance2.arrivalTime)
                    last_takeoff_time.add(Calendar.MINUTE, taskInstance.minNextFlightDuration)
                    last_takeoff_time.add(Calendar.MINUTE, taskInstance.preparationDuration)
                }
            }
        }
        if (last_takeoff_time) {
            if (testInstance.takeoffTime < last_takeoff_time.getTime()) {
                testInstance.takeoffTimeWarning = true
            }
        }

        testInstance.timeCalculated = true
		testInstance.timetableVersion = taskInstance.timetableVersion + 1 
		testInstance.ResetFlightTestResults()
		testInstance.CalculateTestPenalties()
        testInstance.save()
		
		printdone ""
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
        time.add(Calendar.SECOND, getLegsPlanTimeSeconds(testInstance))
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
    private int getLegsPlanTimeSeconds(Test testInstance)
    {
		Date legs_time = Date.parse("HH:mm","00:00")
        TestLegFlight.findAllByTest(testInstance).each { TestLegFlight testlegflight_instance ->
			legs_time = testlegflight_instance.AddPlanLegTime(legs_time)
        }
        return FcMath.Seconds(legs_time)
    }

    //--------------------------------------------------------------------------
    private void calulateTestLegFlights(Task taskInstance)
    {
        printstart "calulateTestLegFlights: ${taskInstance.name()}"
		boolean something_calculated = false
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
				if (!test_instance.timeCalculated) {
					calulateTestLegFlight(test_instance)
					something_calculated = true
				}
			}
        }
		if (something_calculated) {
			printdone ""
		} else {
			printdone "Nothing to calculate."
		}
    }

    //--------------------------------------------------------------------------
    private void calulateTestLegFlight(Test testInstance)
    {
        printstart "calulateTestLegFlight: ${testInstance.crew.name}"
        
        // remove all TestLegFlights
        TestLegFlight.findAllByTest(testInstance).each { TestLegFlight testlegflight_instance ->
            testlegflight_instance.delete(flush:true)
        }

        // calculate TestLegFlights
        BigDecimal last_truetrack = null
        RouteLegTest.findAllByRoute(testInstance?.flighttestwind?.flighttest?.route).each { RouteLegTest routelegtest_instance ->
            
            TestLegFlight testlegflight_instance = new TestLegFlight()
            calculateLeg(testlegflight_instance, routelegtest_instance, testInstance.flighttestwind.wind, testInstance.taskTAS, testInstance.task.procedureTurnDuration, last_truetrack)
            testlegflight_instance.test = testInstance
            testlegflight_instance.save()

            last_truetrack = testlegflight_instance.planTrueTrack
        }
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    private void calulateCoordResult(Test testInstance)
    {
		printstart "calulateCoordResult: ${testInstance.crew.name}"
		
        // remove all coordResultInstances
        CoordResult.findAllByTest(testInstance).each { CoordResult coordresult_instance ->
            coordresult_instance.delete(flush:true)
        }

        // create coordResultInstances
        int coord_index = 0
        CoordRoute.findAllByRoute(testInstance?.flighttestwind?.flighttest?.route).each { CoordRoute coordroute_instance ->
            CoordResult coordresult_instance = new CoordResult()
            coordresult_instance.type = coordroute_instance.type
            coordresult_instance.titleNumber = coordroute_instance.titleNumber
            coordresult_instance.mark = coordroute_instance.mark
            coordresult_instance.latGrad = coordroute_instance.latGrad
            coordresult_instance.latMinute = coordroute_instance.latMinute
            coordresult_instance.latDirection = coordroute_instance.latDirection
            coordresult_instance.lonGrad = coordroute_instance.lonGrad
            coordresult_instance.lonMinute = coordroute_instance.lonMinute
            coordresult_instance.lonDirection = coordroute_instance.lonDirection
            coordresult_instance.altitude = coordroute_instance.altitude
            coordresult_instance.gatewidth = coordroute_instance.gatewidth
            coordresult_instance.planProcedureTurn = coordroute_instance.planProcedureTurn
            switch (coordroute_instance.type) {
                case CoordType.TO:
                    coordresult_instance.planCpTime = testInstance.takeoffTime
                    break
                case CoordType.SP:
                    coordresult_instance.planCpTime = testInstance.startTime
                    break
                case CoordType.LDG:
                    coordresult_instance.planCpTime = testInstance.maxLandingTime
                    break
                case CoordType.TP:
                case CoordType.FP:
                    coord_index++
                    Date cp_time = testInstance.startTime
                    TestLegFlight.findAllByTest(testInstance).eachWithIndex { TestLegFlight testlegflight_instance, int leg_index ->
                        cp_time = testlegflight_instance.AddPlanLegTime(cp_time)
                        if (coord_index == leg_index + 1) {
                            coordresult_instance.planCpTime = cp_time
                        }
                    }
                    break
                case CoordType.SECRET:
                    Date cp_time = testInstance.startTime
                    TestLegFlight.findAllByTest(testInstance).eachWithIndex { TestLegFlight testlegflight_instance, int leg_index ->
                        if (coord_index == leg_index) {
                            cp_time = testlegflight_instance.AddPlanLegTime(cp_time,coordroute_instance.secretLegRatio)
                            coordresult_instance.planCpTime = cp_time
                        } else {
                            cp_time = testlegflight_instance.AddPlanLegTime(cp_time)
                        }
                    }
                    break
            }
            coordresult_instance.test = testInstance
            switch (coordroute_instance.type) {
                case CoordType.TO:
                case CoordType.LDG:
                    // ignore Takeoff and Landing
                    break
                default:
                    coordresult_instance.save()
                    break
            }
        }
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    private void calulateTestLegPlannings(Test testInstance)
    {
        printstart "calulateTestLegPlannings: ${testInstance.crew.name}"
        
		if (!testInstance.planningtesttask) {
			printdone "No PlanningTestTask"
			return
		}
		
		Route route_instance = testInstance.planningtesttask.route
		
        // remove all TestLegPlannings
        TestLegPlanning.findAllByTest(testInstance).each { TestLegPlanning testlegplanning_instance ->
            testlegplanning_instance.delete()
        }
        
        // calculate TestLegPlannings with results 
        BigDecimal last_truetrack = null
        RouteLegTest.findAllByRoute(route_instance).each { RouteLegTest routelegtest_instance ->
            TestLegPlanning testlegplanning_instance = new TestLegPlanning()
            calculateLeg(testlegplanning_instance, routelegtest_instance, testInstance.planningtesttask.wind, testInstance.taskTAS, testInstance.planningtesttask.planningtest.task.procedureTurnDuration, last_truetrack)
            testlegplanning_instance.test = testInstance
            if (!testInstance.task.planningTestDistanceMeasure) {
                testlegplanning_instance.resultTestDistance = testlegplanning_instance.planTestDistance
            }
            if (!testInstance.task.planningTestDirectionMeasure) {
                testlegplanning_instance.resultTrueTrack = testlegplanning_instance.planTrueTrack
            }
            testlegplanning_instance.save()
            last_truetrack = testlegplanning_instance.planTrueTrack
        }
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    private void calculateLeg(TestLeg testLeg, RouteLeg routeLegInstance, Wind windInstance, BigDecimal valueTAS, int procedureTurnDuration, BigDecimal lastTrueTrack) 
    {
        // save route data
        testLeg.planTestDistance = routeLegInstance.testDistance()
        testLeg.planTrueTrack = routeLegInstance.testTrueTrack()

        // calculate wind
	    Map ret = AviationMath.calculateWind(windInstance.direction, windInstance.speed, valueTAS,
		                                     testLeg.planTrueTrack, testLeg.planTestDistance)
	    testLeg.planTrueHeading = ret.trueheading
	    testLeg.planGroundSpeed = ret.groundspeed
	    testLeg.planLegTime = ret.legtime 
	   
        // calculate procedure turn
        testLeg.planProcedureTurn = false
        testLeg.planProcedureTurnDuration = 0
        if (lastTrueTrack != null) {
            BigDecimal diffTrack = testLeg.planTrueTrack - lastTrueTrack
            if (diffTrack < 0) {
                diffTrack += 360
            }
            if (diffTrack >= 90 && diffTrack < 270) {
        	    testLeg.planProcedureTurn = true
        	    testLeg.planProcedureTurnDuration = procedureTurnDuration
            }
        }

		println "calculateLeg: ${routeLegInstance.testDistance()}, ${routeLegInstance.testTrueTrack()} -> ${testLeg.planTrueHeading}, ${testLeg.planGroundSpeed}, ${FcMath.TimeStr(testLeg.planLegTime)}"
    }

    //--------------------------------------------------------------------------
    Map putAircraft(Map contest, String registration, String type, String colour)
    {
		printstart "putAircraft"
        Map p = [:]
        p.registration = registration
        p.type = type
        p.colour = colour
        Map ret = saveAircraft(p,contest.instance)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putContest(String title, int mapScale)
    {
		printstart "putContest"
        Map p = [:]
        p.title = title
        p.mapScale = mapScale
        Map ret = saveContest(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putTask(Map contest, String title, String firstTime, int takeoffIntervalNormal, int risingDuration)
    {
		printstart "putTask"
        Map p = [:]
        p.title = title
		p.firstTime = firstTime
		p.takeoffIntervalNormal = takeoffIntervalNormal
		p.risingDuration = risingDuration
        Map ret = saveTask(p,contest.instance)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putRoute(Map contest, String title, String mark)
    {
		printstart "putRoute"
        Map p = [:]
        p.title = title
        p.mark = mark
        Map ret = saveRoute(p,contest.instance)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map putCoordRoute(Map route, CoordType type, int titleNumber, String mark, String latDirection, int latGrad, BigDecimal latMinute, String lonDirection, int lonGrad, BigDecimal lonMinute, int altitude, int gatewidth, BigDecimal measureDistance, BigDecimal measureTrueTrack)
    {
		printstart "putCoordRoute"
        Map p = [:]
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
		p.measureDistance = measureDistance
        p.measureTrueTrack = measureTrueTrack 
        Map ret = saveCoordRoute(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putCrew(Map contest, String name, String country, String registration, String type, String colour, BigDecimal tas)
    {
		printstart "putCrew"
        Map p = [:]
        p.name = name
        p.country = country
        p.registration = registration
        p.type = type
        p.colour = colour
        p.tas = tas
        Map ret = saveCrew(p,contest.instance)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putFlightTest(Map task, String title, Map route)
    {
		printstart "putFlightTest"
        Map p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.route = route.instance
        p.direction = 0
        p.speed = 0
        Map ret = saveFlightTest(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putFlightTest(Map task, String title, Map route, BigDecimal direction, BigDecimal speed)
    {
		printstart "putFlightTest"
        Map p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.route = route.instance
        p.direction = direction
        p.speed = speed
        Map ret = saveFlightTest(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putFlightTestWind(Map flighttest, BigDecimal direction, BigDecimal speed)
    {
		printstart "putFlightTestWind"
        Map p = [:]
        p.flighttestid = flighttest.instance.id
        p.direction = direction
        p.speed = speed
        Map ret = saveFlightTestWind(p)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map putPlanningTest(Map task, String title)
    {
		printstart "putPlanningTest"
        Map p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.direction = 0
        p.speed = 0
        Map ret = savePlanningTest(p)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map putPlanningTest(Map task, String title, String taskTitle, Map route, BigDecimal direction, BigDecimal speed)
    {
		printstart "putPlanningTest"
        Map p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.taskTitle = taskTitle
        p.route = route.instance
        p.direction = direction
        p.speed = speed
        Map ret = savePlanningTest(p)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map putPlanningTestTask(Map planningtest, String title, Map route, BigDecimal direction, BigDecimal speed)
    {
		printstart "putPlanningTestTask" 
        Map p = [:]
        p.planningtestid = planningtest.instance.id
        p.title = title
        p.route = route.instance
        p.direction = direction
        p.speed = speed
        Map ret = savePlanningTestTask(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    void putplanningtesttaskTask(Map task, Map planningtesttask)
    {
		printstart "putplanningtesttaskTask"
        Test.findAllByTask(task.instance).each { Test test_instance ->
            test_instance.planningtesttask = planningtesttask.instance
            calulateTestLegPlannings(test_instance)
            test_instance.save()
        }
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    void putplanningresultsTask(Map task, List crewResults)
    {
		printstart "putplanningresultsTask"
		Task task_instance = task.instance
		
		crewResults.each { Map crew_result ->
	    	Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
		    		TestLegPlanning.findAllByTest(test_instance).eachWithIndex { TestLegPlanning testlegplanning_instance, int j  ->
		        		if (crew_result.givenValues[j]?.trueHeading && crew_result.givenValues[j]?.legTime) {
			    			testlegplanning_instance.resultTrueHeading = crew_result.givenValues[j].trueHeading
			        		testlegplanning_instance.resultLegTimeInput = crew_result.givenValues[j].legTime
			        		calculateLegPlanningInstance(testlegplanning_instance)
			        		testlegplanning_instance.save()
		        		}
		        	}
		            test_instance.planningTestGivenTooLate = crew_result.givenTooLate
		            test_instance.planningTestExitRoomTooLate = crew_result.exitRoomTooLate
		            calculateTestPenalties(test_instance)
		           	test_instance.planningTestComplete = test_instance.planningTestLegComplete && crew_result.testComplete
		            calculateTestPenalties(test_instance)
		    		test_instance.save()
				}
	        }
		}
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    void putflighttestwindTask(Map task, Map flighttestwind)
    {
		printstart "putflighttestwindTask"
        Test.findAllByTask(task.instance).each { Test test_instance ->
            test_instance.flighttestwind = flighttestwind.instance
            test_instance.save()
        }
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    void putflightresultsTask(Map task, List crewResults)
    {
		printstart "putflightresultsTask"
		Task task_instance = task.instance
		
		crewResults.each { Map crew_result ->
	        Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
					putflightresults(test_instance, crew_result)
				}
	        }
        }
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    void importflightresultsTask(Map task, List crewResults)
    {
		printstart "importflightresultsTask"
		Task task_instance = task.instance

		crewResults.each { Map crew_result ->
	        Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
					Map ret = importAflosResults(test_instance, crew_result.startNum)
					if (!ret.error) {
						putflightresults(test_instance, crew_result)
					}
				}
	        }
		}
		
		printdone ""
	}
	
    //--------------------------------------------------------------------------
	private void putflightresults(Test testInstance, Map crewResult)
	{
		if (crewResult.givenValues) {
			CoordResult.findAllByTest(testInstance).eachWithIndex { CoordResult coordresult_instance, int j  ->
				boolean calculate = false
	    		if (crewResult.givenValues[j]?.cpNotFound) {
	    			coordresult_instance.resultCpNotFound = true
	    			calculate = true
	    		} else if (crewResult.givenValues[j]?.cpTime) {
	    			coordresult_instance.resultCpTimeInput =  crewResult.givenValues[j].cpTime
	                calculate = true
	    		}
	            if (crewResult.givenValues[j]?.procedureTurnNotFlown) {
	                calculate = true
	                coordresult_instance.resultProcedureTurnNotFlown = true
	            }
	            if (crewResult.givenValues[j]?.altitude) {
	                calculate = true
	                coordresult_instance.resultAltitude = crewResult.givenValues[j].altitude
	            }
	            if (crewResult.givenValues[j]?.badCourseNum) {
	                calculate = true
	                coordresult_instance.resultBadCourseNum = crewResult.givenValues[j].badCourseNum
	            }
	            if (calculate) {
	                calculateCoordResultInstance(coordresult_instance,false)
	                coordresult_instance.resultProcedureTurnEntered = true
	                coordresult_instance.save()
	            }
			}
        }
		if (crewResult.correctionValues) {
			crewResult.correctionValues.each { Map correction_value ->
				CoordResult.findAllByTest(testInstance).each { CoordResult coordresult_instance ->
					if (correction_value.mark == coordresult_instance.mark) {
						boolean calculate = false
			    		if (correction_value?.cpNotFound) {
			    			coordresult_instance.resultCpNotFound = true
			    			calculate = true
			    		} else if (correction_value?.cpFound) {
			    			coordresult_instance.resultCpNotFound = false
			    			if (correction_value?.cpTime) {
								coordresult_instance.resultCpTimeInput =  correction_value.cpTime
			    			}
			                calculate = true
			    		}
			            if (correction_value?.procedureTurnNotFlown) {
			                calculate = true
			                coordresult_instance.resultProcedureTurnNotFlown = true
			            } else if (correction_value?.procedureTurnFlown) {
			                calculate = true
			                coordresult_instance.resultProcedureTurnNotFlown = false
			            }
			            if (correction_value?.altitude) {
			                calculate = true
			                coordresult_instance.resultAltitude = correction_value.altitude
			            }
			            if (correction_value?.badCourseNum) {
			                calculate = true
			                coordresult_instance.resultBadCourseNum = correction_value.badCourseNum
			            }
			            if (calculate) {
			                calculateCoordResultInstance(coordresult_instance,false)
			                coordresult_instance.save()
			            }
					}
				}
			}
		}
        testInstance.flightTestTakeoffMissed = crewResult.takeoffMissed
        testInstance.flightTestBadCourseStartLanding = crewResult.badCourseStartLanding
        testInstance.flightTestLandingTooLate = crewResult.landingTooLate
        testInstance.flightTestGivenTooLate = crewResult.givenTooLate
        calculateTestPenalties(testInstance)
        testInstance.flightTestComplete = testInstance.flightTestCheckPointsComplete && crewResult.testComplete
        calculateTestPenalties(testInstance)
        testInstance.save()
	}
	
    //--------------------------------------------------------------------------
    void putobservationresultsTask(Map task, List crewResults)
    {
		printstart "putobservationresultsTask"
		Task task_instance = task.instance
		
		crewResults.each { Map crew_result ->
	    	Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
					test_instance.observationTestRoutePhotoPenalties = crew_result.routePhotos
					test_instance.observationTestTurnPointPhotoPenalties = crew_result.turnPointPhotos
					test_instance.observationTestGroundTargetPenalties = crew_result.groundTargets
		            calculateTestPenalties(test_instance)
					test_instance.observationTestComplete = crew_result.testComplete
		            calculateTestPenalties(test_instance)
		    		test_instance.save()
				}
	        }
		}
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    void putlandingresultsTask(Map task, List crewResults)
    {
		printstart "putlandingresultsTask"
		Task task_instance = task.instance
		
		crewResults.each { Map crew_result ->
	    	Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
					test_instance.landingTestPenalties = crew_result.landingPenalties
		            calculateTestPenalties(test_instance)
					test_instance.landingTestComplete = crew_result.testComplete
		            calculateTestPenalties(test_instance)
		    		test_instance.save()
				}
	        }
		}
		
		printdone ""
    }

    //--------------------------------------------------------------------------
	void putsequenceTask(Map task, List crewSequence)
	{
		printstart "putsequenceTask"
		int view_pos = 0
		crewSequence.each { Map crew ->
			Test.findAllByTask(task.instance).each { Test test_instance ->
				if (test_instance.crew == crew.instance) {
					test_instance.viewpos = view_pos
					test_instance.timeCalculated = false
					test_instance.ResetFlightTestResults()
					test_instance.CalculateTestPenalties()
					test_instance.save()
					view_pos++
				}
			}
		}
		printdone ""
	}

    //--------------------------------------------------------------------------
    Map runcalculatesequenceTask(Map task)
    {
		printstart "runcalculatesequenceTask"
        Map p = [:]
        p.id = task.instance.id 
        Map ret = calculatesequenceTask(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
	void puttimetableTask(Map task, List crewStartTimes)
	{
		printstart "puttimetableTask"
		Task task_instance = task.instance
		Date first_date = Date.parse("HH:mm",task_instance.firstTime)

		calulateTestLegFlights(task_instance)
		
		crewStartTimes.each { Map crew_start_time ->
			printstart "$crew_start_time.crew.instance.name $crew_start_time.starttime"
			Date last_arrival_time = first_date
			
	        Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_start_time.crew.instance) {
					Date start_date = Date.parse("HH:mm",crew_start_time.starttime)
					GregorianCalendar start_time = new GregorianCalendar() 
					start_time.setTime(start_date)
					
					if (!test_instance.timeCalculated) {
						calculateTime(test_instance, task_instance, start_time, last_arrival_time)
						calulateCoordResult(test_instance)
						last_arrival_time = test_instance.arrivalTime
					}
				}
	        }
			
			printdone ""
		}
		
		printdone ""
	}
	
    //--------------------------------------------------------------------------
    Map runcalculatetimetableTask(Map task)
    {
		printstart "runcalculatetimetableTask"
        Map p = [:]
        p.id = task.instance.id 
        Map ret = calculatetimetableTask(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map runcalculatepositionsTask(Map task)
    {
		printstart "runcalculatepositionsTask"
        Map p = [:]
        p.id = task.instance.id 
        Map ret = calculatepositionsTask(p)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map runcalculatepositionsContest(Map contest)
    {
		printstart "runcalculatepositionsContest"
        Map ret = calculatepositionsContest(contest.instance)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
	Map testData(List testData)
	{
		printstart "testData"
		int error_num = 0
		testData.each { Map table_test_data ->
			int table_error_num = 0
			
			// check count
			int table_count = table_test_data.table.size()
			String table_name = "$table_test_data.name [$table_count]"
			if (table_count != table_test_data.count) {
				table_error_num = add_error_num(table_error_num, table_name)
				println "Table count $table_test_data.count expected."
			}
			
			// check data num
			if (table_test_data.data.size() != table_count) {
				table_error_num = add_error_num(table_error_num, table_name)
				println "Different testdata count ${table_test_data.data.size()}."
			}
			
			// check data
			table_test_data.data.eachWithIndex { Map test_datum, int test_datum_index ->
				def table_datum = table_test_data.table[test_datum_index]
				table_error_num = test_data(test_datum, test_datum_index, table_error_num, table_datum, table_name, table_datum)
			}
			 
			if (table_error_num > 0) {
				printerror "$table_error_num errors."
			} else {
				println "$table_name Ok."
			}
			error_num += table_error_num
		}
		Map ret = [error:false,msgtext:""]
		if (error_num) {
			ret.error = true
			ret.msgtext = "$error_num errors." 
			printerror "Summary: $ret.msgtext"
		} else {
			ret.msgtext = "NO errors."
			printdone "Summary: $ret.msgtext"
		}
		return ret
	}
	
    //--------------------------------------------------------------------------
	private int test_data(Map testData, int testDataIndex, int tableErrorNum, tableDatum, tableName, recurseValue)
	{
		testData.each { key, value ->
			def value1 = recurseValue.(key.toString())
			if (value == null) {
				if (value1 != value) {
					tableErrorNum = add_error_num(tableErrorNum, tableName)
					println "${testDataIndex+1}: field '$key' has different value '$value1' ['$value' expected]."
				} 
			} else if (!value?.class) {
				tableErrorNum = test_data(value, testDataIndex, tableErrorNum, tableDatum, tableName, value1)
			} else if (value1 != value) {
				tableErrorNum = add_error_num(tableErrorNum, tableName)
				println "${testDataIndex+1}: field '$key' has different value '$value1' ['$value' expected]."
			} 
		}
		return tableErrorNum
	}
	
    //--------------------------------------------------------------------------
	private int add_error_num(int errorNum, String printMessage)
	{
		errorNum++
		if (errorNum == 1) {
			printstart printMessage
		}
		return errorNum
	}
	
    //--------------------------------------------------------------------------
    void WritePDF(response,content) {
        byte[] b = content.toByteArray()
        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "attachment; filename=print.pdf")
        response.setContentLength(b.length)
        response.getOutputStream().write(b)
		println "WritePDF"
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
	void SetCookie(response, String cookieName, String cookieValue)
	{
		Cookie cookie = new Cookie(cookieName, cookieValue)
        cookie.setMaxAge(maxCookieAge)
		cookie.setPath("/fc")
        response.addCookie(cookie)
		println "Set cookie '$cookieName' with '$cookieValue'."
    }
	
    //--------------------------------------------------------------------------
	private Cookie getCookie(String cookieName)
	{
        def cookies = RequestContextHolder.currentRequestAttributes().request.getCookies()
        if (cookies == null || cookieName == null || cookieName.length() == 0) {
            return null
        }
        // Otherwise, we have to do a linear scan for the cookie.
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(cookieName)) {
                return cookies[i]
            }
        }
        return null;
    }

    //--------------------------------------------------------------------------
	String GetCookie(String cookieName, String initValue)
	{
		Cookie cookie = getCookie(cookieName)
		if ( cookie ) {
			String value = cookie.getValue()
			println "Found cookie '$cookieName' with '$value'."
            return value
		} else {
			println "Cookie '$cookieName' not found."
			if (initValue) {
				return initValue
			}
			return ""
		}
    }
	
	//--------------------------------------------------------------------------
	void printstart(out)
	{
		logService.printstart out
	}

	//--------------------------------------------------------------------------
	void printerror(out)
	{
		if (out) {
			logService.printend "Error: $out"
		} else {
			logService.printend "Error."
		}
	}

	//--------------------------------------------------------------------------
	void printdone(out)
	{
		if (out) {
			logService.printend "Done: $out"
		} else {
			logService.printend "Done."
		}
	}

	//--------------------------------------------------------------------------
	void print(out)
	{
		logService.print out
	}

	//--------------------------------------------------------------------------
	void println(out)
	{
		logService.println out
	}

}
