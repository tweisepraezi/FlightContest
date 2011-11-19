import org.xhtmlrenderer.pdf.ITextRenderer

class FcService
{
    boolean transactional = true
	def messageSource
	
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

            def oldOwner = aircraftInstance.owner 
            aircraftInstance.properties = params

            if (oldOwner) {
	            def crewInstance = Crew.get( oldOwner.id )
	        	crewInstance.ownAircraft = null
	        	try {
		        	crewInstance.save(flush:true)
	        	} catch (Exception e) {
		        	crewInstance.save()
	        	}
	        	
	        	CrewTest.findAllByCrew(crewInstance).each { crewTestInstance -> 
	        		calculateAircraft(null, crewTestInstance)
	        		crewTestInstance.save()
	        	}
            }

            if (aircraftInstance.owner) {
	            def crewInstance = Crew.get( aircraftInstance.owner.id )
	        	crewInstance.ownAircraft = aircraftInstance
	        	crewInstance.save()

	        	CrewTest.findAllByCrew(crewInstance).each { crewTestInstance -> 
	        		calculateAircraft(crewInstance, crewTestInstance)
	        		crewTestInstance.save()
	            }
            } 
            
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
	Map saveAircraft(params)
    {
        def aircraftInstance = new Aircraft(params)
        
        aircraftInstance.contest = Contest.findByIdIsNotNull() // TODO Contest.get(params.contestid)
        
        if(!aircraftInstance.hasErrors() && aircraftInstance.save()) {
        	
        	if (aircraftInstance.owner) {
                def crewInstance = Crew.get( params.owner.id )
            	crewInstance.ownAircraft = aircraftInstance
            	crewInstance.save()

            	CrewTest.findAllByCrew(crewInstance).each { crewTestInstance -> 
        			calculateAircraft(crewInstance, crewTestInstance)
        			crewTestInstance.save()
                }
            }
        	
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
                if (aircraftInstance.owner) {
    	            def crewInstance = Crew.get( aircraftInstance.owner.id )
    	        	crewInstance.ownAircraft = null
    	        	if(!crewInstance.hasErrors() && crewInstance.save()) {
    	        		aircraftInstance.owner = null 
    	        	}
                }
                
                CrewTest.list().each { crewTestInstance ->
	        		if (crewTestInstance.aircraft == aircraftInstance) {
	        			crewTestInstance.delete()
	        		}
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
	Map getContest(params)
    {
    	def contestInstance = Contest.get(params.id)

    	if (!contestInstance) {
    		return ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
    	}
    	
    	return ['instance':contestInstance]
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
    	if (!Contest.count()) {
    		def contestInstance = new Contest()
    		contestInstance.properties = params
    		return ['instance':contestInstance,'created':true]
    	}
    	return ['message':getMsg('fc.contest.exist')]
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
        contestDayInstance.idTitle = ContestDay.count() + 1
        
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
        
        if (contestDayInstance) {
            try {
                contestDayInstance.delete()
                
                ContestDay.getAll().eachWithIndex { contestDayInstance2, index  -> 
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
    	def contestDayTask = ContestDayTask.get(params.id)

    	if (!contestDayTask) {
    		return ['message':getMsg('fc.notfound',[getMsg('fc.contestdaytask'),params.id])]
    	}
    	
    	return ['instance':contestDayTask]
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
        	Crew.list().eachWithIndex { crewInstance, i ->
	        	def crewTestInstance = new CrewTest()
	        	crewTestInstance.crew = crewInstance
	        	crewTestInstance.viewpos = i
	        	crewTestInstance.contestdaytask = contestDayTaskInstance
           		calculateAircraft(crewInstance, crewTestInstance)
	            crewTestInstance.save()
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
            	def contestDayInstance = contestDayTaskInstance.contestday
            	
            	CrewTest.list().each { crewTestInstance ->
	        		if (crewTestInstance.contestdaytask == contestDayTaskInstance) {
	        			crewTestInstance.delete()
	        		}
	        	}
            	
                contestDayTaskInstance.delete()
                
                ContestDayTask.findAllByContestday(contestDayInstance).eachWithIndex { contestDayTaskInstance2, index  -> 
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
	Map startContestDayTask(params)
	{
		def contestInstance = Contest.findByIdIsNotNull()
		if (contestInstance.lastContestDayTask == 0) {
			def contestDayTaskInstance = ContestDayTask.findByIdIsNotNull()
			if (contestDayTaskInstance) {
				return ['contestdaytaskid':contestDayTaskInstance.id]
			}
		} else {
			def contestDayTaskInstance = ContestDayTask.findById(contestInstance.lastContestDayTask)
			if (!contestDayTaskInstance) {
				contestDayTaskInstance = ContestDayTask.findByIdIsNotNull()
			}
			if (contestDayTaskInstance) {
				return ['contestdaytaskid':contestDayTaskInstance.id]
			}
		}
	}
	
	//--------------------------------------------------------------------------
	Map listcrewtestsContestDayTask(params)
	{
        def contestdaytask = getContestDayTask(params) 
        if (contestdaytask.instance) {
	    	def contestInstance = Contest.findByIdIsNotNull()
	    	contestInstance.lastContestDayTask = contestdaytask.instance.id
	        contestInstance.save()
        }
        return contestdaytask
	}
	
	//--------------------------------------------------------------------------
	Map selectallContestDayTask(params)
	{
        def contestdaytask = getContestDayTask(params) 
        if (contestdaytask.instance) {
	        def selectedCrewTestIDs = [selectedCrewTestID:""]
	    	CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
	   			selectedCrewTestIDs["selectedCrewTestID${crewTestInstance.id}"] = "on"
	    	}
	        contestdaytask.selectedcrewtestids = selectedCrewTestIDs
        }
        return contestdaytask
	}
	
	//--------------------------------------------------------------------------
	Map assignnavtesttaskContestDayTask(params)
	{
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
        	return contestdaytask
        }

        // NavTest exists?
        if (!contestdaytask.instance.navtest) {
        	contestdaytask.message = getMsg('fc.navtest.notfound')
        	contestdaytask.error = true
        	return contestdaytask
        }

        // NavTestTask exists?
        if (!NavTestTask.countByNavtest(contestdaytask.instance.navtest)) {
        	contestdaytask.message = getMsg('fc.navtesttask.notfound')
        	contestdaytask.error = true
        	return contestdaytask
        }

        // Multiple NavTestTasks?  
        if (NavTestTask.countByNavtest(contestdaytask.instance.navtest) > 1) {
        	def crewTestInstanceIds = ['']
        	CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
	    		if (params["selectedCrewTestID${crewTestInstance.id}"] == "on") {
    				crewTestInstanceIds += crewTestInstance.id
	    		}
	    	}
        	contestdaytask.crewtestinstanceids = crewTestInstanceIds
        	return contestdaytask
        }

        // set single NavTestTask to all selected CrewTests
        NavTestTask navTestTaskInstance = NavTestTask.findByNavtest(contestdaytask.instance.navtest) 
    	CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
    		if (params["selectedCrewTestID${crewTestInstance.id}"] == "on") {
    			crewTestInstance.navtesttask = navTestTaskInstance
    			crewTestInstance.save()
    		}
    	}
        
    	return contestdaytask
	}
	
	//--------------------------------------------------------------------------
	void putnavtesttaskContestDayTask(contestdaytask,navtesttask)
	{
		CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
			crewTestInstance.navtesttask = navtesttask.instance
			crewTestInstance.save()
		}
	}
	
	//--------------------------------------------------------------------------
	Map setnavtesttaskContestDayTask(params)
	{
        def contestdaytask = getContestDayTask(params) 
        if (contestdaytask.instance) {
	        def navTestTaskInstance = NavTestTask.get(params.navtesttask.id)
	        params.crewTestInstanceIds.each { crewTestId ->
	        	if (crewTestId) {
	        		def crewTestInstance = CrewTest.get(crewTestId)
	        		crewTestInstance.navtesttask = navTestTaskInstance 
	        		crewTestInstance.save()
	        	}
	        }
	        contestdaytask.message = getMsg('fc.contestdaytask.selectnavtesttask.assigned',[navTestTaskInstance.name()])
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
        	def crewTestInstanceIds = ['']
        	CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
	    		if (params["selectedCrewTestID${crewTestInstance.id}"] == "on") {
    				crewTestInstanceIds += crewTestInstance.id
	    		}
	    	}
        	contestdaytask.crewtestinstanceids = crewTestInstanceIds
        	return contestdaytask
        }

        // set single FlightTestWind to all selected CrewTests
        def flightTestWindInstance = FlightTestWind.findByFlighttest(contestdaytask.instance.flighttest)
    	CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
    		if (params["selectedCrewTestID${crewTestInstance.id}"] == "on") {
    			crewTestInstance.flighttestwind = flightTestWindInstance
    			crewTestInstance.save()
    		}
    	}

        return contestdaytask
	}
	
	//--------------------------------------------------------------------------
	void putflighttestwindContestDayTask(contestdaytask,flighttestwind)
	{
		CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
			crewTestInstance.flighttestwind = flighttestwind.instance
			crewTestInstance.save()
		}
	}
	
	//--------------------------------------------------------------------------
	Map setflighttestwindContestDayTask(params)
	{
        def contestdaytask = getContestDayTask(params) 
        if (contestdaytask.instance) {
	        def flightTestWindInstance = FlightTestWind.get(params.flighttestwind.id)
	        params.crewTestInstanceIds.each { crewTestId ->
	        	if (crewTestId) {
	        		def crewTestInstance = CrewTest.get(crewTestId)
	        		crewTestInstance.flighttestwind = flightTestWindInstance
	        		crewTestInstance.timeCalculated = false
	        		crewTestInstance.save()
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

        // Have all crews an aircraft?
        def call_return = false
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (!crewTestInstance.aircraft) {
        		call_return = true
        	}
        }
        if (call_return) {
        	contestdaytask.message = getMsg('fc.aircraft.notassigned')
            contestdaytask.error = true
            return contestdaytask
        }

        calulateSequence(contestdaytask.instance)
        
        contestdaytask.message = getMsg('fc.crewtest.sequence.calculated')        
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
    	CrewTest.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { crewTestInstance ->
    		if (params["selectedCrewTestID${crewTestInstance.id}"] == "on") {
    			if (crewTestInstance.viewpos == 0) {
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
       		//contestdaytask.message = getMsg('fc.crewtest.moveborderreached')
       		contestdaytask.borderreached = true
        	return contestdaytask
        }
        if (notmovable) {
        	contestdaytask.message = getMsg('fc.crewtest.notmovable')
            contestdaytask.error = true
        	return contestdaytask
        }
    	
        def movenum = 0
        def movefirstpos = -1
        def selectedCrewTestIDs = [selectedCrewTestID:""]
        borderreached = false
    	CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
    		if (params["selectedCrewTestID${crewTestInstance.id}"] == "on") {
    			crewTestInstance.viewpos--
    			crewTestInstance.timeCalculated = false
    			crewTestInstance.save()
    			selectedCrewTestIDs["selectedCrewTestID${crewTestInstance.id}"] = "on"
    			if (crewTestInstance.viewpos == 0) {
    				borderreached = true
    			}
    			movenum++
    			if (movefirstpos == -1 || crewTestInstance.viewpos < movefirstpos) {
    				movefirstpos = crewTestInstance.viewpos
    			}
    		}
    	}
    	CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
			if (params["selectedCrewTestID${crewTestInstance.id}"] != "on") {
				if (crewTestInstance.viewpos >= movefirstpos && crewTestInstance.viewpos < movefirstpos + movenum) {
	    			crewTestInstance.viewpos += movenum
	    			crewTestInstance.timeCalculated = false
	    			crewTestInstance.save()
				}
			}
		}

    	if (!borderreached) {
    		contestdaytask.selectedcrewtestids = selectedCrewTestIDs
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
    	CrewTest.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { crewTestInstance ->
    		if (params["selectedCrewTestID${crewTestInstance.id}"] == "on") {
    			if (crewTestInstance.viewpos + 1 == Crew.count()) {
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
       		//contestdaytask.message = getMsg('fc.crewtest.moveborderreached')
            contestdaytask.borderreached = true
        	return contestdaytask
        }
        if (notmovable) {
        	contestdaytask.message = getMsg('fc.crewtest.notmovable')
            contestdaytask.error = true
        	return contestdaytask
        }
    	
        def movenum = 0
        def movefirstpos = -1
        def selectedCrewTestIDs = [selectedCrewTestID:""]
        borderreached = false
    	CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
    		if (params["selectedCrewTestID${crewTestInstance.id}"] == "on") {
    			crewTestInstance.viewpos++
    			crewTestInstance.timeCalculated = false
    			crewTestInstance.save()
    			selectedCrewTestIDs["selectedCrewTestID${crewTestInstance.id}"] = "on"
       			if (crewTestInstance.viewpos + 1 == Crew.count()) {
       				borderreached = true
       			}
    			movenum++
    			if (movefirstpos == -1 || crewTestInstance.viewpos < movefirstpos) {
    				movefirstpos = crewTestInstance.viewpos
    			}
    		}
    	}
    	CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
			if (params["selectedCrewTestID${crewTestInstance.id}"] != "on") {
				if (crewTestInstance.viewpos >= movefirstpos && crewTestInstance.viewpos < movefirstpos + movenum) {
	    			crewTestInstance.viewpos -= movenum
	    			crewTestInstance.timeCalculated = false
	    			crewTestInstance.save()
				}
			}
    	}

    	if (!borderreached) {
    		contestdaytask.selectedcrewtestids = selectedCrewTestIDs
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
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (!crewTestInstance.flighttestwind) {
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
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (!crewTestInstance.aircraft) {
        		call_return = true
        	}
        }
        if (call_return) {
        	contestdaytask.message = getMsg('fc.aircraft.notassigned')
            contestdaytask.error = true
        	return contestdaytask
        }

        calulateCrewTestLegs(contestdaytask.instance)
        calulateTimetable(contestdaytask.instance)
        
        contestdaytask.message = getMsg('fc.crewtest.timetable.calculated')        
        return contestdaytask
	}
	
	//--------------------------------------------------------------------------
	Map timeaddContestDayTask(params)
	{
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        def selectedCrewTestIDs = [selectedCrewTestID:""]
        CrewTest.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { crewTestInstance ->
    		if (params["selectedCrewTestID${crewTestInstance.id}"] == "on") {
    			addTestingTime(contestdaytask.instance,crewTestInstance)
    			selectedCrewTestIDs["selectedCrewTestID${crewTestInstance.id}"] = "on"
    		}
    	}
        contestdaytask.selectedcrewtestids = selectedCrewTestIDs
    	return contestdaytask
	}
	
	//--------------------------------------------------------------------------
	Map timesubtractContestDayTask(params)
	{
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        def selectedCrewTestIDs = [selectedCrewTestID:""]
        CrewTest.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { crewTestInstance ->
    		if (params["selectedCrewTestID${crewTestInstance.id}"] == "on") {
    			subtractTestingTime(contestdaytask.instance,crewTestInstance)
    			selectedCrewTestIDs["selectedCrewTestID${crewTestInstance.id}"] = "on"
    		}
    	}
        contestdaytask.selectedcrewtestids = selectedCrewTestIDs
   		return contestdaytask
	}
	
	//--------------------------------------------------------------------------
	Map printtimetableContestDayTask(params,baseuri)
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
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (!crewTestInstance.flighttestwind) {
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
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (!crewTestInstance.aircraft) {
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
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (!crewTestInstance.timeCalculated) {
        		call_return = true
        	}
        }
        if (call_return) {
        	contestdaytask.message = getMsg('fc.crewtest.timetable.newcalculate')
            contestdaytask.error = true
        	return contestdaytask
        }        
        
        // Warnings?  
        call_return = false
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (crewTestInstance.arrivalTimeWarning || crewTestInstance.takeoffTimeWarning) {
        		call_return = true
        	}
        }
        if (call_return) {
        	contestdaytask.message = getMsg('fc.crewtest.flightplan.resolvewarnings')
            contestdaytask.error = true
        	return contestdaytask
        }        
        
        // Print timetable
        try {
	        ITextRenderer renderer = new ITextRenderer();
	        ByteArrayOutputStream content = new ByteArrayOutputStream()
	        boolean first_pdf = true
	   		def url = baseuri + "/contestDayTask/timetableprintable/${contestdaytask.instance.id}"
	   		println "Print: $url"
	        renderer.setDocument(url)
	        renderer.layout()
	   		renderer.createPDF(content,false)
	        renderer.finishPDF()
	        contestdaytask.content = content 
	    }
		catch (Throwable e) {
			contestdaytask.message = getMsg('fc.crewtest.timetable.printerror',["$e"])
			contestdaytask.error = true
		}
    	return contestdaytask
	}
	
	//--------------------------------------------------------------------------
	Map printflightplansContestDayTask(params,baseuri)
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
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (!crewTestInstance.flighttestwind) {
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
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (!crewTestInstance.aircraft) {
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
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (!crewTestInstance.timeCalculated) {
        		call_return = true
        	}
        }
        if (call_return) {
        	contestdaytask.message = getMsg('fc.crewtest.timetable.newcalculate')
        	contestdaytask.error = true
        	return contestdaytask
        }        
        
        // Warnings?  
        call_return = false
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (crewTestInstance.arrivalTimeWarning || crewTestInstance.takeoffTimeWarning) {
        		call_return = true
        	}
        }
        if (call_return) {
        	contestdaytask.message = getMsg('fc.crewtest.flightplan.resolvewarnings')
        	contestdaytask.error = true
        	return contestdaytask
        }        
        
        // Print flightplans
        try {
	        ITextRenderer renderer = new ITextRenderer();
	        ByteArrayOutputStream content = new ByteArrayOutputStream()
	        boolean first_pdf = true
	    	CrewTest.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { crewTestInstance ->
	    		def url = baseuri + "/crewTest/flightplanprintable/${crewTestInstance.id}"
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
			contestdaytask.message = getMsg('fc.crewtest.flightplan.printerror',["$e"])
			contestdaytask.error = true
		}
        return contestdaytask
	}
	
	//--------------------------------------------------------------------------
	Map printnavtesttaskContestDayTask(params,baseuri)
	{
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        // NavTest exists?
        if (!contestdaytask.instance.navtest) {
        	contestdaytask.message = getMsg('fc.navtest.notfound')
        	contestdaytask.error = true
        	return contestdaytask
        }

        // NavTestTask exists?
        if (!NavTestTask.countByNavtest(contestdaytask.instance.navtest)) {
        	contestdaytask.message = getMsg('fc.navtesttask.notfound')
           	contestdaytask.error = true
        	return contestdaytask
        }

        // NavTestTask assigned to all crews?
        def call_return = false
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (!crewTestInstance.navtesttask) {
        		call_return = true
        	}
        }
        if (call_return) {
        	contestdaytask.message = getMsg('fc.navtesttask.notassigned')
            contestdaytask.error = true
        	return contestdaytask
        }

        // Print navtesttasks
        try {
	        ITextRenderer renderer = new ITextRenderer();
	        ByteArrayOutputStream content = new ByteArrayOutputStream()
	        boolean first_pdf = true
	    	CrewTest.findAllByContestdaytask(contestdaytask.instance,[sort:"viewpos"]).each { crewTestInstance ->
	    		def url = baseuri + "/navTestTask/showprintable/${crewTestInstance.navtesttask.id}?crewTest=${crewTestInstance.id}"
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
			contestdaytask.message = getMsg('fc.navtesttask.printerror',["$e"])
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
        int maxPosition = CrewTest.countByContestdaytask(contestdaytask.instance)
        for (int actPosition = 1; actPosition <= maxPosition; actPosition++) {
        	
        	// search lowest penalty
        	int minPenalty = 100000
	        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
	        	if (crewTestInstance.penaltySummary > actPenalty) {
	        		if (crewTestInstance.penaltySummary < minPenalty) {
	        			minPenalty = crewTestInstance.penaltySummary 
	        		}
	        	}
	        }
        	actPenalty = minPenalty 
        	
        	// set position
        	int setPositionNum = -1
	        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
	        	if (crewTestInstance.penaltySummary == actPenalty) {
	        		crewTestInstance.positionContestDay = actPosition
	        		crewTestInstance.save()
	        		setPositionNum++
	        	}
	        }
        	
        	if (setPositionNum > 0) {
        		actPosition += setPositionNum
        	}
        }
        
        contestdaytask.message = getMsg('fc.crewtest.results.positions.calculated')        
        return contestdaytask
	}
	
	//--------------------------------------------------------------------------
	Map printresultsContestDayTask(params,baseuri)
	{
        def contestdaytask = getContestDayTask(params) 
        if (!contestdaytask.instance) {
            return contestdaytask
        }

        // Positions calculated?  
        boolean call_return = false
        CrewTest.findAllByContestdaytask(contestdaytask.instance).each { crewTestInstance ->
        	if (!crewTestInstance.positionContestDay) {
        		call_return = true
        	}
        }
        if (call_return) {
        	contestdaytask.message = getMsg('fc.crewtest.results.positions.newcalculate')
        	contestdaytask.error = true
        	return contestdaytask
        }        
        
        // Print positions
        try {
	        ITextRenderer renderer = new ITextRenderer();
	        ByteArrayOutputStream content = new ByteArrayOutputStream()
	        boolean first_pdf = true
	   		def url = baseuri + "/contestDayTask/positionsprintable/${contestdaytask.instance.id}"
	   		println "Print: $url"
	        renderer.setDocument(url)
	        renderer.layout()
	   		renderer.createPDF(content,false)
	        renderer.finishPDF()
	        contestdaytask.content = content 
	    }
		catch (Throwable e) {
			contestdaytask.message = getMsg('fc.crewtest.timetable.printerror',["$e"])
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
    Map saveRoute(params)
    {
        def routeInstance = new Route(params)
        
        routeInstance.contest = Contest.findByIdIsNotNull() // TODO Contest.get(params.contestid)
        routeInstance.idTitle = Route.count() + 1
        
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
        
        if (routeInstance) {
            try {
                routeInstance.delete()
                
                Route.getAll().eachWithIndex { routeInstance2, index  -> 
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
    Map saveRouteCoord(params)
    {
    	def lastRouteCoordInstance = RouteCoord.findByRoute(Route.get(params.routeid),[sort:"id", order:"desc"])
        def routeCoordInstance = new RouteCoord(params)
    	
    	routeCoordInstance.route = Route.get(params.routeid)
        
        if(!routeCoordInstance.hasErrors() && routeCoordInstance.save()) {
        	addLeg(routeCoordInstance.route,lastRouteCoordInstance,routeCoordInstance)
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
            	removeRouteLegs(routeInstance)
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
	Map getRouteLeg(params)
    {
    	def routeLegInstance = RouteLeg.get(params.id)

    	if (!routeLegInstance) {
    		return ['message':getMsg('fc.notfound',[getMsg('fc.routeleg'),params.id])]
    	}
    	
    	return ['instance':routeLegInstance]
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

            def oldOwnAircraft = crewInstance.ownAircraft
            crewInstance.properties = params
            
        	if (oldOwnAircraft) {
        		def oldAircraftInstance = Aircraft.get( oldOwnAircraft.id )
	        	oldAircraftInstance.owner = null
	        	try {
		        	oldAircraftInstance.save(flush:true)
	        	} catch(Exception e) {
		        	oldAircraftInstance.save()
	        	}
            }

            if (crewInstance.ownAircraft) {
            	def aircraftInstance = Aircraft.get( crewInstance.ownAircraft.id )
	        	aircraftInstance.owner = crewInstance
	        	aircraftInstance.save()
            }

            if(!crewInstance.hasErrors() && crewInstance.save()) {
               	CrewTest.findAllByCrew(crewInstance).each { crewTestInstance ->
               		calculateAircraft(crewInstance, crewTestInstance)
	        		crewTestInstance.save()
                }

               	return ['instance':crewInstance,'saved':true,'message':getMsg('fc.updated',["${crewInstance.name()}"])]
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
    Map saveCrew(params)
    {
        def crewInstance = new Crew(params)
        
        crewInstance.contest = Contest.findByIdIsNotNull() // TODO Contest.get(params.contestid)
        
        if (!crewInstance.hasErrors() && crewInstance.save()) {
        	
            if (params.registration) {
    	        def aircraftInstance = new Aircraft(params)
    	        if (aircraftInstance) {
    		    	aircraftInstance.owner = crewInstance
    		    	aircraftInstance.contest = crewInstance.contest 
    		    	if(!aircraftInstance.hasErrors() && aircraftInstance.save()) {
    		    		crewInstance.ownAircraft = aircraftInstance 
    		    	}
    	        }
            }

        	ContestDayTask.list().each { contestDayTaskInstance ->
	        	def crewTestInstance = new CrewTest()
	        	crewTestInstance.crew = crewInstance
	        	crewTestInstance.viewpos = Crew.count() - 1
	        	crewTestInstance.contestdaytask = contestDayTaskInstance
           		calculateAircraft(crewInstance, crewTestInstance)
	            crewTestInstance.save()
        	}
        
    		String msg
        	if (crewInstance.ownAircraft) {
               	msg = getMsg('fc.crew.created.ownaircraft',["${crewInstance.name()}", "${crewInstance.ownAircraft.registration}"])
        	} else {
               	msg = getMsg('fc.created',["${crewInstance.name()}"])
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
                
            	if (crewInstance.ownAircraft) {
    	            def aircraftInstance = Aircraft.get( crewInstance.ownAircraft.id )
    	        	aircraftInstance.owner = null
    	        	if(!aircraftInstance.hasErrors() && aircraftInstance.save()) {
    	        		crewInstance.ownAircraft = null 
    	        	}
                }
            	
                CrewTest.list().each { crewTestInstance ->
            		if (crewTestInstance.crew == crewInstance) {
            			crewTestInstance.delete()
            		}
	        	}
                
            	crewInstance.delete()
                
            	return ['deleted':true,'message':getMsg('fc.deleted',["${crewInstance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.crew'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.crew'),params.id])]
        }
    }

	//--------------------------------------------------------------------------
	Map setownaircraftCrew(crew,aircraft)
	{
	    crew.instance.ownAircraft = aircraft.instance
		def p = [:]
	    p.id = crew.instance.id
	    return updateCrew(p)
	}
    
	//--------------------------------------------------------------------------
	Map setusedaircraftCrew(crew,aircraftInstance,usedTAS)
	{
        crew.instance.usedAircraft = aircraftInstance
        crew.instance.usedTAS = usedTAS
		def p = [:]
        p.id = crew.instance.id
        return updateCrew(p)
	}

	//--------------------------------------------------------------------------
	Map getCrewTest(params)
    {
    	def crewTestInstance = CrewTest.get(params.id)

    	if (!crewTestInstance) {
    		return ['message':getMsg('fc.notfound',[getMsg('fc.crewtest'),params.id])]
    	}
    	
    	return ['instance':crewTestInstance]
    }

	//--------------------------------------------------------------------------
	Map printflightplanCrewTest(params,baseuri)
    {
		def crewtest = getCrewTest(params)
		if (!crewtest.instance) {
			return crewtest
		}
		
        // Print flightplan
        try {
	        ITextRenderer renderer = new ITextRenderer();
	        ByteArrayOutputStream content = new ByteArrayOutputStream()
	   		def url = baseuri + "/crewTest/flightplanprintable/${crewtest.instance.id}"
	   		println "Print: $url"
	        renderer.setDocument(url)
	        renderer.layout()
	   		renderer.createPDF(content,false)
	        renderer.finishPDF()
	        crewtest.content = content
	    }
		catch (Throwable e) {
			crewtest.message = getMsg('fc.crewtest.timetable.printerror',["$e"])
			crewtest.error = true
		}
		return crewtest
    }
	
	//--------------------------------------------------------------------------
	Map printresultsCrewTest(params,baseuri)
    {
		def crewtest = getCrewTest(params)
		if (!crewtest.instance) {
			return crewtest
		}
		
        // Print crew results
        try {
	        ITextRenderer renderer = new ITextRenderer();
	        ByteArrayOutputStream content = new ByteArrayOutputStream()
	   		def url = baseuri + "/crewTest/resultsprintable/${crewtest.instance.id}"
	   		println "Print: $url"
	        renderer.setDocument(url)
	        renderer.layout()
	   		renderer.createPDF(content,false)
	        renderer.finishPDF()
	        crewtest.content = content
	    }
		catch (Throwable e) {
			crewtest.message = getMsg('fc.crewtest.timetable.printerror',["$e"])
			crewtest.error = true
		}
		return crewtest
    }
	
	//--------------------------------------------------------------------------
	Map updateresultsCrewTest(params)
	{
        def crewTestInstance = CrewTest.get(params.id)
        
        if (crewTestInstance) {
            
        	if(params.version) {
                def version = params.version.toLong()
                if(crewTestInstance.version > version) {
                    crewTestInstance.errors.rejectValue("version", "crewTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':crewTestInstance]
                }
            }
        	
            crewTestInstance.properties = params
            
            crewTestInstance.penaltySummary = crewTestInstance.penaltyNavTest + crewTestInstance.penaltyFlightTest + crewTestInstance.penaltyLandingTest + crewTestInstance.penaltySpecialTest
            crewTestInstance.positionContestDay = 0
            	
            if(!crewTestInstance.hasErrors() && crewTestInstance.save()) {
            	return ['instance':crewTestInstance,'saved':true,'message':getMsg('fc.updated',["${crewTestInstance.crew.name()}"])]
            } else {
            	return ['instance':crewTestInstance]
            }
        } else {
        	return ['message':getMsg('fc.notfound',[getMsg('fc.contestdaytask'),params.id])]
        }
	}
	
	//--------------------------------------------------------------------------
	Map getCrewTestLeg(params)
    {
    	def crewTestLegInstance = CrewTestLeg.get(params.id)

    	if (!crewTestLegInstance) {
    		return ['message':getMsg('fc.notfound',[getMsg('fc.crewtestleg'),params.id])]
    	}
    	
    	return ['instance':crewTestLegInstance]
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
    		        'fromlistcrewtests':params.fromlistcrewtests,'fromcontestdaytask':params.fromcontestdaytask,
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
        
        if(!flightTestInstance.hasErrors() && flightTestInstance.save()) {

            def contestDayTaskInstance = ContestDayTask.get( params.contestdaytaskid )
            contestDayTaskInstance.flighttest = flightTestInstance
            contestDayTaskInstance.save()

            if (params.direction) {
	        	def windInstance = new Wind(params)
	            windInstance.save()
	            
	            def flightTestWindInstance = new FlightTestWind(params)
	            flightTestWindInstance.wind = windInstance
	            flightTestWindInstance.flighttest = flightTestInstance
	            flightTestWindInstance.save()
            }

        	return ['instance':flightTestInstance,'saved':true,'message':getMsg('fc.created',["${flightTestInstance.name()}"]),
        	        'fromlistcrewtests':params.fromlistcrewtests,'fromcontestdaytask':params.fromcontestdaytask,
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
        	        'fromlistcrewtests':params.fromlistcrewtests,
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
	Map getNavTest(params)
    {
    	def navTestInstance = NavTest.get(params.id)

    	if (!navTestInstance) {
    		return ['message':getMsg('fc.notfound',[getMsg('fc.navtest'),params.id])]
    	}
    	
    	return ['instance':navTestInstance]
    }

	//--------------------------------------------------------------------------
    Map updateNavTest(params)
    {
        def navTestInstance = NavTest.get(params.id)
        
        if (navTestInstance) {

        	if(params.version) {
                def version = params.version.toLong()
                if(navTestInstance.version > version) {
                    navTestInstance.errors.rejectValue("version", "navTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':navTestInstance]
                }
            }
        	
            navTestInstance.properties = params
            if(!navTestInstance.hasErrors() && navTestInstance.save()) {
            	return ['instance':navTestInstance,'saved':true,'message':getMsg('fc.updated',["${navTestInstance.name()}"])]
            } else {
            	return ['instance':navTestInstance]
            }
        } else {
        	return ['message':getMsg('fc.notfound',[getMsg('fc.navtest'),params.id])]
        }
    }
	
	//--------------------------------------------------------------------------
    Map createNavTest(params)
    {
    	if (!Route.count()) {
    		return ['message':getMsg('fc.route.notfound'),'error':true,
    		        'fromlistcrewtests':params.fromlistcrewtests,'fromcontestdaytask':params.fromcontestdaytask,
    		        'contestdaytaskid':params.contestdaytask.id]
    	}
		 
    	def navTestInstance = new NavTest()
		navTestInstance.properties = params
		return ['instance':navTestInstance]
    }

    
	//--------------------------------------------------------------------------
    Map saveNavTest(params)
    {
        def navTestInstance = new NavTest(params)
    	
        navTestInstance.contestdaytask = ContestDayTask.get( params.contestdaytaskid )
        
        if(!navTestInstance.hasErrors() && navTestInstance.save()) {

        	def contestDayTaskInstance = ContestDayTask.get( params.contestdaytaskid )
            contestDayTaskInstance.navtest = navTestInstance
            contestDayTaskInstance.save()

            if (params.route) {
	            def windInstance = new Wind(params)
	        	windInstance.save()
	            
	        	def navTestTaskInstance = new NavTestTask(params)
	            navTestTaskInstance.navtest = navTestInstance
	            navTestTaskInstance.title = params.taskTitle
	            navTestTaskInstance.idTitle = 1
	            navTestTaskInstance.wind = windInstance
	            navTestTaskInstance.save()
	            
	            calulateNavTestTaskLegs(navTestTaskInstance)
            }
            
            return ['instance':navTestInstance,'saved':true,'message':getMsg('fc.created',["${navTestInstance.name()}"]),
        	        'fromlistcrewtests':params.fromlistcrewtests,'fromcontestdaytask':params.fromcontestdaytask,
        	        'contestdaytaskid':contestDayTaskInstance.id]
        } else {
        	return ['instance':navTestInstance]
        }
    }
	
	//--------------------------------------------------------------------------
    Map deleteNavTest(params)
    {
        def navTestInstance = NavTest.get(params.id)
        
        if (navTestInstance) {
            try {
                def contestDayTaskInstance = ContestDayTask.get( navTestInstance.contestdaytask.id )
                contestDayTaskInstance.navtest = null
                contestDayTaskInstance.save()
                
                navTestInstance.delete()
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${navTestInstance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
            	return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.landingtest'),params.id])]
            }
        } else {
        	return ['message':getMsg('fc.notfound',[getMsg('fc.navtest'),params.id])]
        }
    }
	
	//--------------------------------------------------------------------------
	Map getNavTestTask(params)
    {
    	def navTestTaskInstance = NavTestTask.get(params.id)

    	if (!navTestTaskInstance) {
    		return ['message':getMsg('fc.notfound',[getMsg('fc.navtesttask'),params.id])]
    	}
    	
    	navTestTaskInstance.direction = navTestTaskInstance.wind.direction
    	navTestTaskInstance.speed = navTestTaskInstance.wind.speed
    	
    	return ['instance':navTestTaskInstance]
    }

	//--------------------------------------------------------------------------
    Map updateNavTestTask(params)
    {
        def navTestTaskInstance = NavTestTask.get(params.id)
        
        if (navTestTaskInstance) {

        	if(params.version) {
                def version = params.version.toLong()
                if(navTestTaskInstance.version > version) {
                    navTestTaskInstance.errors.rejectValue("version", "navTestTask.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':navTestTaskInstance]
                }
            }
        	
            navTestTaskInstance.properties = params
            
            navTestTaskInstance.wind.direction = navTestTaskInstance.direction
        	navTestTaskInstance.wind.speed = navTestTaskInstance.speed

        	calulateNavTestTaskLegs(navTestTaskInstance)

            if(!navTestTaskInstance.hasErrors() && navTestTaskInstance.save()) {
            	return ['instance':navTestTaskInstance,'saved':true,'message':getMsg('fc.updated',["${navTestTaskInstance.name()}"])]
            } else {
            	return ['instance':navTestTaskInstance]
            }
        } else {
        	return ['message':getMsg('fc.notfound',[getMsg('fc.navtesttask'),params.id])]
        }
    }
	
	//--------------------------------------------------------------------------
    Map createNavTestTask(params)
    {
    	if (!Route.count()) {
    		return ['message':getMsg('fc.route.notfound'),'error':true,'navtestid':params.navTest.id]
    	}
		 
    	def navTestTaskInstance = new NavTestTask()
		navTestTaskInstance.properties = params
		return ['instance':navTestTaskInstance]
    }

    
	//--------------------------------------------------------------------------
    Map saveNavTestTask(params)
    {
        def navTestTaskInstance = new NavTestTask(params)
    	
        navTestTaskInstance.navtest = NavTest.get(params.navtestid)
        navTestTaskInstance.idTitle = NavTestTask.countByNavtest(navTestTaskInstance.navtest) + 1
        
        def windInstance = new Wind(params)
    	if(!windInstance.hasErrors() && windInstance.save()) {
    		navTestTaskInstance.wind = windInstance
    	}
        
        if(!navTestTaskInstance.hasErrors() && navTestTaskInstance.save()) {
        	calulateNavTestTaskLegs(navTestTaskInstance)
        	return ['instance':navTestTaskInstance,'saved':true,'message':getMsg('fc.created',["${navTestTaskInstance.name()}"]),
        	        'fromlistcrewtests':params.fromlistcrewtests,
        	        'contestdaytaskid':navTestTaskInstance.navtest.contestdaytask.id,
        	        'navtestid':navTestTaskInstance.navtest.id]
        } else {
        	return ['instance':navTestTaskInstance]
        }
    }
	
	//--------------------------------------------------------------------------
    Map deleteNavTestTask(params)
    {
        def navTestTaskInstance = NavTestTask.get(params.id)
        
        if (navTestTaskInstance) {
            try {
            	def navTestInstance = navTestTaskInstance.navtest 
            		
                navTestTaskInstance.delete()

                NavTestTask.findAllByNavtest(navTestInstance).eachWithIndex { navTestTaskInstance2, index  -> 
                	navTestTaskInstance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${navTestTaskInstance.name()}"]),
                        'navtestid':navTestTaskInstance.navtest.id]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
            	return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.navtesttask'),params.id])]
            }
        } else {
        	return ['message':getMsg('fc.notfound',[getMsg('fc.navtesttask'),params.id])]
        }
    }
	
	//--------------------------------------------------------------------------
	Map printNavTestTask(params,baseuri)
    {
        def navtesttask = getNavTestTask(params) 
        if (!navtesttask.instance) {
        	return navtesttask
        }
		
        // Print navtesttask
        try {
	        ITextRenderer renderer = new ITextRenderer();
	        ByteArrayOutputStream content = new ByteArrayOutputStream()
	   		def url = baseuri + "/navTestTask/showprintable/${navtesttask.instance.id}"
	   		println "Print: $url"
	        renderer.setDocument(url)
	        renderer.layout()
	   		renderer.createPDF(content,false)
	        renderer.finishPDF()
	        navtesttask.content = content
	    }
		catch (Throwable e) {
			navtesttask.message = getMsg('fc.navtesttask.printerror2',["$e"])
			navtesttask.error = true
		}
		return navtesttask
    }
	
	//--------------------------------------------------------------------------
	Map getNavTestTaskLeg(params)
    {
    	def navTestTaskLegInstance = NavTestTaskLeg.get(params.id)

    	if (!navTestTaskLegInstance) {
    		return ['message':getMsg('fc.notfound',[getMsg('fc.navtesttaskleg'),params.id])]
    	}
    	
    	return ['instance':navTestTaskLegInstance]
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
    private void calculateAircraft(Crew crewInstance, CrewTest crewTestInstance) 
    {
		if (crewInstance?.usedAircraft) {
			crewTestInstance.aircraft = crewInstance.usedAircraft
			crewTestInstance.TAS = crewInstance.usedTAS
		} else if (crewInstance?.ownAircraft) {
			crewTestInstance.aircraft = crewInstance.ownAircraft
			crewTestInstance.TAS = crewInstance.ownAircraft.defaultTAS
	    } else {
	    	crewTestInstance.aircraft = null
	    	crewTestInstance.TAS = 0
	    }
		crewTestInstance.timeCalculated = false
    }
    
    //--------------------------------------------------------------------------
    private RouteLeg newLeg(Route route, RouteCoord lastRouteCoordInstance, RouteCoord newRouteCoordInstance) 
    {
    	// calculate distance
    	def newLatMath = newRouteCoordInstance.latMath()
    	def lastLatMath = lastRouteCoordInstance.latMath()
    		
    	def latDiff = newLatMath - lastLatMath 
    	def lonDiff = newRouteCoordInstance.lonMath() - lastRouteCoordInstance.lonMath()
    	
    	def latDis = 60 * latDiff
    	def lonDis = 60 * lonDiff * Math.cos( Math.toRadians((newLatMath + lastLatMath)/2) )
    	
    	def dis = Math.sqrt(latDis*latDis + lonDis*lonDis)
    	
    	// calculate trueTrack
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
    	
		return new RouteLeg([trueTrack:dir,distance:dis,route:route])
    }
    
    //--------------------------------------------------------------------------
    private void addLeg(Route route, RouteCoord lastRouteCoordInstance, RouteCoord newRouteCoordInstance) 
    {
    	if (lastRouteCoordInstance) {
    		// claculate new leg
    		def routeLegInstance = newLeg(route, lastRouteCoordInstance, newRouteCoordInstance)
    		routeLegInstance.save()
    	}
    }
    
    //--------------------------------------------------------------------------
    private void removeRouteLegs(Route routeInstance) 
    {
    	// remove all legs
    	RouteLeg.findAllByRoute(routeInstance).each {
    		it.delete()
    	}
    }
    
    //--------------------------------------------------------------------------
    private void calculateRouteLegs(Route routeInstance) 
    {
    	// remove all legs
    	RouteLeg.findAllByRoute(routeInstance).each { routeLegInstance ->
    		routeLegInstance.delete()
    	}
    	
    	// calculate new legs
    	RouteCoord lastRouteCoordInstance
    	RouteCoord.findAllByRoute(routeInstance).each { routeCoordInstance -> 
    		if (lastRouteCoordInstance) {
        		def routeLegInstance = newLeg(routeInstance, lastRouteCoordInstance, routeCoordInstance)
        		routeLegInstance.save()
    		}
    		lastRouteCoordInstance = routeCoordInstance
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
    	// calculate "viewpos": 0..Crew.count()-1
    	
    	// get maxGroups
    	def maxGroups = 0
    	def actGroup = 0
    	def lastAircraft = ""
    	CrewTest.findAllByContestdaytask(contestDayTaskInstance,[sort:"aircraft"]).each { crewTestInstance ->
    		if (crewTestInstance.aircraft.registration != lastAircraft) {
    			lastAircraft = crewTestInstance.aircraft.registration
    			actGroup = 1
    		} else {
    			actGroup++
    		}
			if (actGroup > maxGroups) {
				maxGroups = actGroup 
			}
    	}
    
    	// calculate viewpos with aircraft sort
    	lastAircraft = ""
    	def aircraftIndex = 0
    	CrewTest.findAllByContestdaytask(contestDayTaskInstance,[sort:"aircraft"]).each { crewTestInstance ->
    		if (crewTestInstance.aircraft.registration != lastAircraft) {
    			lastAircraft = crewTestInstance.aircraft.registration
    			
    			def ownAircraftExists = false 
    			CrewTest.findAllByContestdaytaskAndAircraft(contestDayTaskInstance,crewTestInstance.aircraft).each { crewTestInstance2 ->
    				if (crewTestInstance2.aircraft == crewTestInstance2.crew.ownAircraft) {
    					ownAircraftExists = true
    				}
    			}
    			
    			if (ownAircraftExists) {
    				aircraftIndex = maxGroups - 1
    			} else {
    				aircraftIndex = maxGroups
    			}
    		}
    			
			if (crewTestInstance.aircraft == crewTestInstance.crew.ownAircraft) {
				crewTestInstance.viewpos = 1000 * maxGroups 
			} else {
				crewTestInstance.viewpos = 1000 * aircraftIndex
				aircraftIndex--
			}
    	}
    
    	// add TAS to viewpos
    	CrewTest.findAllByContestdaytask(contestDayTaskInstance,[sort:"TAS",order:"desc"]).each { crewTestInstance ->
    		crewTestInstance.viewpos += crewTestInstance.TAS
    	}

    	// set viewpos
    	CrewTest.findAllByContestdaytask(contestDayTaskInstance,[sort:"viewpos",order:"desc"]).eachWithIndex { crewTestInstance, i ->
    		crewTestInstance.viewpos = i
    		crewTestInstance.timeCalculated = false
    	}
    }
 
	//--------------------------------------------------------------------------
    void addTestingTime(ContestDayTask contestDayTaskInstance, CrewTest crewTestInstance)
    {
		if (crewTestInstance.timeCalculated) {
	    	
			GregorianCalendar time = new GregorianCalendar() 
			time.setTime(crewTestInstance.testingTime)
			
			// add testingTime
			time.add(Calendar.MINUTE, contestDayTaskInstance.addTimeValue)
			crewTestInstance.testingTime = time.getTime()
			
			// calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
			calculateTimes(time, contestDayTaskInstance, crewTestInstance)
			
    		calulateTimetableWarnings(contestDayTaskInstance)
		}
    }
  
	//--------------------------------------------------------------------------
    void subtractTestingTime(ContestDayTask contestDayTaskInstance, CrewTest crewTestInstance)
    {
		if (crewTestInstance.timeCalculated) {
	    	
			GregorianCalendar time = new GregorianCalendar() 
			time.setTime(crewTestInstance.testingTime)
			
			// subtract testingTime
			time.add(Calendar.MINUTE, -contestDayTaskInstance.addTimeValue)
			crewTestInstance.testingTime = time.getTime()
			
			// calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
			calculateTimes(time, contestDayTaskInstance, crewTestInstance)
			
    		calulateTimetableWarnings(contestDayTaskInstance)
		}
    }

	//--------------------------------------------------------------------------
    private void calulateTimetableWarnings(ContestDayTask contestDayTaskInstance)
    {
    	Date first_date = Date.parse("HH:mm",contestDayTaskInstance.firstTime)
    	Date lastArrivalTime = first_date
    	
    	CrewTest.findAllByContestdaytask(contestDayTaskInstance,[sort:"viewpos"]).each { crewTestInstance ->
    		
    		// calculate arrivalTimeWarning by arrivalTime
    		crewTestInstance.arrivalTimeWarning = false
    		if (lastArrivalTime > crewTestInstance.arrivalTime) {
    			crewTestInstance.arrivalTimeWarning = true
    		}
    		lastArrivalTime = crewTestInstance.arrivalTime
    		
    		// calculate takeoffTimeWarning by aircraft
    		crewTestInstance.takeoffTimeWarning = false
    		boolean foundAircraft = false
    		GregorianCalendar lastTakeoffTime = null
    		CrewTest.findAllByContestdaytaskAndAircraft(contestDayTaskInstance,crewTestInstance.aircraft,[sort:"viewpos"]).each { crewTestInstance2 ->
    			if (crewTestInstance == crewTestInstance2) {
    				foundAircraft = true
    			}
    			if (!foundAircraft) {
    				lastTakeoffTime = new GregorianCalendar()
    				lastTakeoffTime.setTime(crewTestInstance2.arrivalTime)
    				lastTakeoffTime.add(Calendar.MINUTE, contestDayTaskInstance.minNextFlightDuration)
    				lastTakeoffTime.add(Calendar.MINUTE, contestDayTaskInstance.preparationDuration)
    			}
    		}
    		if (lastTakeoffTime) {
    			if (crewTestInstance.takeoffTime < lastTakeoffTime.getTime()) {
    				crewTestInstance.takeoffTimeWarning = true
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

    	float lastTAS = 9000.0
    	Date lastArrivalTime = first_date
    	
    	CrewTest.findAllByContestdaytask(contestDayTaskInstance,[sort:"viewpos"]).each { crewTestInstance ->
    		
   			if (crewTestInstance.TAS > lastTAS) { // faster aircraft
   				start_time.add(Calendar.MINUTE, contestDayTaskInstance.takeoffIntervalFasterAircraft - contestDayTaskInstance.takeoffIntervalNormal)
   			}
    		
   			// calculate testingTime
    		GregorianCalendar time = start_time.clone()
    		crewTestInstance.testingTime = time.getTime()
    		
			// calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
			calculateTimes(time, contestDayTaskInstance, crewTestInstance)
			
    		// calculate arrivalTimeWarning by arrivalTime
    		crewTestInstance.arrivalTimeWarning = false
    		if (lastArrivalTime > crewTestInstance.arrivalTime) {
    			crewTestInstance.arrivalTimeWarning = true
    		}
    		lastArrivalTime = crewTestInstance.arrivalTime
    		
    		// calculate takeoffTimeWarning by aircraft
    		crewTestInstance.takeoffTimeWarning = false
    		boolean foundAircraft = false
    		GregorianCalendar lastTakeoffTime = null
    		CrewTest.findAllByContestdaytaskAndAircraft(contestDayTaskInstance,crewTestInstance.aircraft,[sort:"viewpos"]).each { crewTestInstance2 ->
    			if (crewTestInstance == crewTestInstance2) {
    				foundAircraft = true
    			}
    			if (!foundAircraft) {
    				lastTakeoffTime = new GregorianCalendar()
    				lastTakeoffTime.setTime(crewTestInstance2.arrivalTime)
    				lastTakeoffTime.add(Calendar.MINUTE, contestDayTaskInstance.minNextFlightDuration)
    				lastTakeoffTime.add(Calendar.MINUTE, contestDayTaskInstance.preparationDuration)
    			}
    		}
    		if (lastTakeoffTime) {
    			if (crewTestInstance.takeoffTime < lastTakeoffTime.getTime()) {
    				crewTestInstance.takeoffTimeWarning = true
    			}
    		}

    		// next 
    		start_time.add(Calendar.MINUTE, contestDayTaskInstance.takeoffIntervalNormal)
    		lastTAS = crewTestInstance.TAS
    		
    		crewTestInstance.timeCalculated = true
    	}

    }
 
	//--------------------------------------------------------------------------
    private void calculateTimes(GregorianCalendar time, ContestDayTask contestDayTaskInstance, CrewTest crewTestInstance) 
    {
		// calulate endTestingTime
		time.add(Calendar.MINUTE, contestDayTaskInstance.navTestDuration)
		crewTestInstance.endTestingTime = time.getTime()
			
		// calulate takeoffTime
		time.add(Calendar.MINUTE, contestDayTaskInstance.preparationDuration)
		crewTestInstance.takeoffTime = time.getTime()
		
		// calulate startTime
		time.add(Calendar.MINUTE, contestDayTaskInstance.risingDuration)
		crewTestInstance.startTime = time.getTime()
		
		// calculate finishTime
		time.add(Calendar.SECOND, (3600 * getLegsTime(crewTestInstance)).toInteger() )
		crewTestInstance.finishTime = time.getTime()
		
		// calculate arrivalTime
		time.add(Calendar.MINUTE, contestDayTaskInstance.landingDuration)
		crewTestInstance.arrivalTime = time.getTime()
    }
    
	//--------------------------------------------------------------------------
    private BigDecimal getLegsTime(CrewTest crewTestInstance)
    {
    	def legsTime = 0
    	CrewTestLeg.findAllByCrewtest(crewTestInstance).each { crewTestLegInstance ->
    		legsTime += crewTestLegInstance.legTime
    	}
    	return legsTime
    }

	//--------------------------------------------------------------------------
    void calulateCrewTestLegs(ContestDayTask contestDayTaskInstance)
    {
    	println "calulateCrewTestLegs"
    	
    	CrewTest.findAllByContestdaytask(contestDayTaskInstance,[sort:"viewpos"]).each { crewTestInstance ->
    		// remove all crewTestLegs
	    	CrewTestLeg.findAllByCrewtest(crewTestInstance).each { crewTestLegInstance ->
	    		crewTestLegInstance.delete(flush:true)
	    	}
	    	
	    	// calculate crewTestLegs
	    	Integer lastTrueTrack = null
	    	RouteLeg.findAllByRoute(crewTestInstance?.flighttestwind?.flighttest?.route).each { routeLegInstance ->
	    		
	    		def crewTestLegInstance = new CrewTestLeg()
	    		calculateLeg(crewTestLegInstance, routeLegInstance, crewTestInstance.flighttestwind.wind, crewTestInstance.TAS, crewTestInstance.contestdaytask.procedureTurnDuration, lastTrueTrack)
	    		crewTestLegInstance.crewtest = crewTestInstance
	    		crewTestLegInstance.save()
	   			
	   			lastTrueTrack = crewTestLegInstance.trueTrack
	    	}
    	}
    }

	//--------------------------------------------------------------------------
    private void calulateNavTestTaskLegs(NavTestTask navTestTaskInstance)
    {
    	println "calulateNavTestTaskLegs"
    	
    	// remove all navTestTaskLegs
    	NavTestTaskLeg.findAllByNavtesttask(navTestTaskInstance).each { navTestTaskLegInstance ->
    		navTestTaskLegInstance.delete(flush:true)
    	}
    	
    	// calculate navTestTaskLegs
    	Integer lastTrueTrack = null
    	RouteLeg.findAllByRoute(navTestTaskInstance.route).each { routeLegInstance ->
    		
    		def navTestTaskLegInstance = new NavTestTaskLeg()
    		calculateLeg(navTestTaskLegInstance, routeLegInstance, navTestTaskInstance.wind, navTestTaskInstance.TAS, navTestTaskInstance.navtest.contestdaytask.procedureTurnDuration, lastTrueTrack)
    		navTestTaskLegInstance.navtesttask = navTestTaskInstance
    		navTestTaskLegInstance.save()
   			
   			lastTrueTrack = navTestTaskLegInstance.trueTrack
    	}
    }

	//--------------------------------------------------------------------------
    private void calculateLeg(Leg leg, RouteLeg routeLegInstance, Wind windInstance, float TAS, int procedureTurnDuration, Integer lastTrueTrack) 
    {
   		// save route data
   		leg.distance = routeLegInstance.distance
   		leg.trueTrack = routeLegInstance.trueTrack
   		
   		// calculate wind
   		if (windInstance.speed) {
	   		def windDirection = windInstance.direction - 180
	   		def beta = windDirection - routeLegInstance.trueTrack
	   		def sinBeta = Math.sin(Math.toRadians(beta)) 
	   		def driftAngle = 0
	   		if (beta != 0 && beta != 180 && beta != -180) {
	   			driftAngle = Math.toDegrees(Math.asin(sinBeta*windInstance.speed/TAS))  
	   		}
	   		leg.trueHeading = routeLegInstance.trueTrack - driftAngle 
	   		def gamma = 180 + leg.trueHeading - windDirection
	   		def sinGamma = Math.sin(Math.toRadians(gamma))
	   		println "Beta: $beta, sinBeta: $sinBeta, Gamma: $gamma, sinGamma: $sinGamma"
   			if (beta == 0) {
   				leg.groundSpeed = TAS + windInstance.speed
   			} else if (beta == 180 || beta == -180) {
	   			leg.groundSpeed = TAS - windInstance.speed
	   		} else {
	   			leg.groundSpeed = TAS * sinGamma / sinBeta
	   		}
	   		if (leg.trueHeading < 0) {
	   			leg.trueHeading += 360
	   		}
   		} else {
   			leg.trueHeading = routeLegInstance.trueTrack
   			leg.groundSpeed = TAS
   		}
   		leg.legTime = leg.distance / leg.groundSpeed 
   		
   		// calculate Procedure Turn
   		leg.procedureTurn = false
   		if (lastTrueTrack != null) {
   			def diffTrack = leg.trueTrack - lastTrueTrack
   			if (diffTrack < 0) {
   				diffTrack += 360
   			}
   			if (diffTrack >= 90 && diffTrack < 270) {
   				leg.procedureTurn = true
   				leg.legTime += procedureTurnDuration / 60
   			}
   		}
    }

	void WritePDF(response,content) {
	    byte[] b = content.toByteArray()
	    response.setContentType("application/pdf")
	    response.setHeader("Content-disposition", "attachment; filename=print.pdf")
	    response.setContentLength(b.length)
	    response.getOutputStream().write(b)
	}
	
	//--------------------------------------------------------------------------
	Map putAircraft(registration,type,colour,defaultTAS)
	{
		def p = [:]
        p.registration = registration
        p.type = type
        p.colour = colour
        p.defaultTAS = defaultTAS
        return saveAircraft(p)
	}
	
	//--------------------------------------------------------------------------
	Map putContest(title)
	{
		def p = [:]
		p.title = title
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
	Map putRoute(title)
	{
		def p = [:]
        p.title = title
        return saveRoute(p)
	}

	//--------------------------------------------------------------------------
	Map putRouteCoord(route,latGrad,latMinute,latDirection,lonGrad,lonMinute,lonDirection)
	{
		def p = [:]
    	p.routeid = route.instance.id 
    	p.latGrad = latGrad
    	p.latMinute = latMinute
    	p.latDirection = latDirection
    	p.lonGrad = lonGrad
    	p.lonMinute = lonMinute
    	p.lonDirection = lonDirection
    	return saveRouteCoord(p)
	}
	
	//--------------------------------------------------------------------------
	Map putCrew(name1,name2,country,registration,type,colour,defaultTAS)
	{
		def p = [:]
        p.name1 = name1
        p.name2 = name2
        p.country = country
        p.registration = registration
        p.type = type
        p.colour = colour
        p.defaultTAS = defaultTAS
        return saveCrew(p)
	}
	
	//--------------------------------------------------------------------------
	Map putFlightTest(contestdaytask,title,route)
	{
		def p = [:]
		p.contestdaytaskid = contestdaytask.instance.id
		p.title = title
		p.route = route.instance
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
	Map putNavTest(contestdaytask,title)
	{
		def p = [:]
		p.contestdaytaskid = contestdaytask.instance.id
		p.title = title
		return saveNavTest(p)
	}

	//--------------------------------------------------------------------------
	Map putNavTest(contestdaytask,title,taskTitle,route,TAS,direction,speed)
	{
		def p = [:]
		p.contestdaytaskid = contestdaytask.instance.id
		p.title = title
		p.taskTitle = taskTitle
		p.route = route.instance
		p.TAS = TAS
		p.direction = direction
		p.speed = speed
		return saveNavTest(p)
	}

	//--------------------------------------------------------------------------
    Map putNavTestTask(navtest,title,route,TAS,direction,speed)
    {
		def p = [:]
		p.navtestid = navtest.instance.id
		p.title = title
		p.route = route.instance
		p.TAS = TAS
		p.direction = direction
		p.speed = speed
		return saveNavTestTask(p)
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
	Map putResults(contestdaytask,crew,penaltyNavTest,penaltyFlightTest,penaltyLandingTest,penaltySpecialTest)
	{
		CrewTest crewTestInstance = CrewTest.findByContestdaytaskAndCrew(contestdaytask.instance,crew.instance)
		def p = [:]
		p.id = crewTestInstance.id
		p.penaltyNavTest = penaltyNavTest
		p.penaltyFlightTest = penaltyFlightTest
		p.penaltyLandingTest = penaltyLandingTest
		p.penaltySpecialTest = penaltySpecialTest
		return updateresultsCrewTest(p)
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
