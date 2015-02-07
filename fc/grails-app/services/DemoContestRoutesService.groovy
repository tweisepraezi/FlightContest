class DemoContestRoutesService
{
	def fcService
	
	static final String ROUTE_5 = "Strecke 5"
	static final String ROUTE_5M = "Strecke 5m"
	static final String ROUTE_6 = "Strecke 6"
	static final String ROUTE_6M = "Strecke 6m"
	static final String ROUTE_7 = "Strecke 7"
	static final String ROUTE_7M = "Strecke 7m"
	
    long CreateTest(String testName, String printPrefix, boolean testExists) 
    {
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,false,0,ContestRules.R1,true,testExists)
        
        // Routes
        fcService.printstart ROUTE_5
		Map route5 = fcService.importRoute(contest,ROUTE_5,ROUTE_5,SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK,false,[])
        fcService.printdone ""
        
        fcService.printstart ROUTE_6
		Map route6 = fcService.importRoute(contest,ROUTE_6,ROUTE_6,SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK,false,[])
        fcService.printdone ""
		
        fcService.printstart ROUTE_7
		Map route7 = fcService.importRoute(contest,ROUTE_7,ROUTE_7,SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK,false,[])
        fcService.printdone ""
		
        fcService.printstart ROUTE_5M
		Map route5m = fcService.putRoute(contest,ROUTE_5M, ROUTE_5M)
		create_route(route5m, ROUTE_5M)
		fcService.printdone ""
		
        fcService.printstart ROUTE_6M
		Map route6m = fcService.putRoute(contest,ROUTE_6M, ROUTE_6M)
		create_route(route6m, ROUTE_6M)
		fcService.printdone ""
		
        fcService.printstart ROUTE_7M
		Map route7m = fcService.putRoute(contest,ROUTE_7M, ROUTE_7M)
		create_route(route7m, ROUTE_7M)
		fcService.printdone ""
		
		// Crews and Aircrafts
		Map crew1 = fcService.putCrew(contest,1,"Besatzung 120","","","","D-EAAA","","",120)
		Map crew2 = fcService.putCrew(contest,2,"Besatzung 60", "","","","D-EAAB","","",60)

        // Flight Tests
        Map task5 = fcService.putTask(contest,ROUTE_5,"09:00",2,"time:10min","time:10min",5,"wind:1","wind:1",false,true,false,false,false, false,true, true,true,true, false,false,false,false, false)
        Map flighttest5 = fcService.putFlightTest(task5,"",route5)
        Map flighttestwind5 = fcService.putFlightTestWind(flighttest5,0,0)
        fcService.putflighttestwindTask(task5,flighttestwind5)
		fcService.runcalculatetimetableTask(task5)
		
        Map task6 = fcService.putTask(contest,ROUTE_6,"09:00",2,"time:10min","time:10min",5,"wind:1","wind:1",false,true,false,false,false, false,true, true,true,true, false,false,false,false, false)
        Map flighttest6 = fcService.putFlightTest(task6,"",route6)
        Map flighttestwind6 = fcService.putFlightTestWind(flighttest6,0,0)
        fcService.putflighttestwindTask(task6,flighttestwind6)
		fcService.runcalculatetimetableTask(task6)
		
        Map task7 = fcService.putTask(contest,ROUTE_7,"09:00",2,"time:10min","time:10min",5,"wind:1","wind:1",false,true,false,false,false, false,true, true,true,true, false,false,false,false, false)
        Map flighttest7 = fcService.putFlightTest(task7,"",route7)
        Map flighttestwind7 = fcService.putFlightTestWind(flighttest7,0,0)
        fcService.putflighttestwindTask(task7,flighttestwind7)
		fcService.runcalculatetimetableTask(task7)
		
        fcService.printdone ""
        
		return contest.instance.id
    }

    Map RunTest(Contest lastContest, String contestName)
    {
		Map ret_test = [:]
        if (lastContest && lastContest.title == contestName) {
            fcService.printstart "runtest '$lastContest.title'"
			Route route5
			Route route6
			Route route7
			Route route5m
			Route route6m
			Route route7m
			Route.findAllByContest(lastContest).eachWithIndex { Route route, int i ->
				switch (i) {
					case 0: 
						route5 = route
						break
					case 1:
						route6 = route
						break
					case 2:
						route7 = route
						break
					case 3:
						route5m = route
						break
					case 4:
						route6m = route
						break
					case 5:
						route7m = route
						break
				}
			}
			Task task5
			Task task6
			Task task7
			Task.findAllByContest(lastContest).eachWithIndex { Task task, int i ->
				switch (i) {
					case 0:
						task5 = task
						break
					case 1:
						task6 = task
						break
					case 2:
						task7 = task
						break
				}

			}
            Map ret = fcService.testData(
               [[name:"Route",count:6,table:Route.findAllByContest(lastContest,[sort:"id"]),data:testRoute()],
                [name:"CoordRoute '$ROUTE_5'",count:39,table:CoordRoute.findAllByRoute(route5,[sort:"id"]),data:testCoordRoute(ROUTE_5)],
                [name:"CoordRoute '$ROUTE_6'",count:39,table:CoordRoute.findAllByRoute(route6,[sort:"id"]),data:testCoordRoute(ROUTE_6)],
                [name:"CoordRoute '$ROUTE_7'",count:39,table:CoordRoute.findAllByRoute(route7,[sort:"id"]),data:testCoordRoute(ROUTE_7)],
                [name:"CoordRoute '$ROUTE_5M'",count:39,table:CoordRoute.findAllByRoute(route5m,[sort:"id"]),data:testCoordRoute(ROUTE_5M)],
                [name:"CoordRoute '$ROUTE_6M'",count:39,table:CoordRoute.findAllByRoute(route6m,[sort:"id"]),data:testCoordRoute(ROUTE_6M)],
                [name:"CoordRoute '$ROUTE_7M'",count:39,table:CoordRoute.findAllByRoute(route7m,[sort:"id"]),data:testCoordRoute(ROUTE_7M)],
                [name:"RouteLegCoord '$ROUTE_5'",count:38,table:RouteLegCoord.findAllByRoute(route5,[sort:"id"]),data:testRouteLegCoord(ROUTE_5)],
                [name:"RouteLegCoord '$ROUTE_6'",count:38,table:RouteLegCoord.findAllByRoute(route6,[sort:"id"]),data:testRouteLegCoord(ROUTE_6)],
                [name:"RouteLegCoord '$ROUTE_7'",count:38,table:RouteLegCoord.findAllByRoute(route7,[sort:"id"]),data:testRouteLegCoord(ROUTE_7)],
                [name:"RouteLegCoord '$ROUTE_5M'",count:38,table:RouteLegCoord.findAllByRoute(route5m,[sort:"id"]),data:testRouteLegCoord(ROUTE_5M)],
                [name:"RouteLegCoord '$ROUTE_6M'",count:38,table:RouteLegCoord.findAllByRoute(route6m,[sort:"id"]),data:testRouteLegCoord(ROUTE_6M)],
                [name:"RouteLegCoord '$ROUTE_7M'",count:38,table:RouteLegCoord.findAllByRoute(route7m,[sort:"id"]),data:testRouteLegCoord(ROUTE_7M)],
                [name:"RouteLegTest '$ROUTE_5'",count:14,table:RouteLegTest.findAllByRoute(route5,[sort:"id"]),data:testRouteLegTest(ROUTE_5)],
                [name:"RouteLegTest '$ROUTE_6'",count:14,table:RouteLegTest.findAllByRoute(route6,[sort:"id"]),data:testRouteLegTest(ROUTE_6)],
                [name:"RouteLegTest '$ROUTE_7'",count:15,table:RouteLegTest.findAllByRoute(route7,[sort:"id"]),data:testRouteLegTest(ROUTE_7)],
                [name:"RouteLegTest '$ROUTE_5M'",count:14,table:RouteLegTest.findAllByRoute(route5m,[sort:"id"]),data:testRouteLegTest(ROUTE_5M)],
                [name:"RouteLegTest '$ROUTE_6M'",count:14,table:RouteLegTest.findAllByRoute(route6m,[sort:"id"]),data:testRouteLegTest(ROUTE_6M)],
                [name:"RouteLegTest '$ROUTE_7M'",count:15,table:RouteLegTest.findAllByRoute(route7m,[sort:"id"]),data:testRouteLegTest(ROUTE_7M)],
                [name:"Crew",count:2,table:Crew.findAllByContest(lastContest,[sort:"id"]),data:testCrew()],
                [name:"Task",count:3,table:Task.findAllByContest(lastContest,[sort:"id"]),data:testTask()],
                [name:"TestLegFlight 'Besatzung 120' '$ROUTE_5'",count:36,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(task5,Crew.findByContestAndName(lastContest,"Besatzung 120")),[sort:"id"]),data:testTestLegFlight120(ROUTE_5)],
                [name:"TestLegFlight 'Besatzung 60' '$ROUTE_5'", count:36,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(task5,Crew.findByContestAndName(lastContest,"Besatzung 60")), [sort:"id"]),data:testTestLegFlight60(ROUTE_5)],
                [name:"TestLegFlight 'Besatzung 120' '$ROUTE_6'",count:36,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(task6,Crew.findByContestAndName(lastContest,"Besatzung 120")),[sort:"id"]),data:testTestLegFlight120(ROUTE_6)],
                [name:"TestLegFlight 'Besatzung 60' '$ROUTE_6'", count:36,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(task6,Crew.findByContestAndName(lastContest,"Besatzung 60")), [sort:"id"]),data:testTestLegFlight60(ROUTE_6)],
                [name:"TestLegFlight 'Besatzung 120' '$ROUTE_7'",count:36,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(task7,Crew.findByContestAndName(lastContest,"Besatzung 120")),[sort:"id"]),data:testTestLegFlight120(ROUTE_7)],
                [name:"TestLegFlight 'Besatzung 60' '$ROUTE_7'", count:36,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(task7,Crew.findByContestAndName(lastContest,"Besatzung 60")), [sort:"id"]),data:testTestLegFlight60(ROUTE_7)],
                [name:"CoordResult 'Besatzung 120' '$ROUTE_5'",  count:39,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(task5,Crew.findByContestAndName(lastContest,"Besatzung 120")),[sort:"id"]),data:testCoordResult120(ROUTE_5)],    
                [name:"CoordResult 'Besatzung 60' '$ROUTE_5'",   count:39,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(task5,Crew.findByContestAndName(lastContest,"Besatzung 60")),[sort:"id"]),data:testCoordResult60(ROUTE_5)],    
                [name:"CoordResult 'Besatzung 120' '$ROUTE_6'",  count:39,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(task6,Crew.findByContestAndName(lastContest,"Besatzung 120")),[sort:"id"]),data:testCoordResult120(ROUTE_6)],    
                [name:"CoordResult 'Besatzung 60' '$ROUTE_6'",   count:39,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(task6,Crew.findByContestAndName(lastContest,"Besatzung 60")),[sort:"id"]),data:testCoordResult60(ROUTE_6)],    
                [name:"CoordResult 'Besatzung 120' '$ROUTE_7'",  count:39,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(task7,Crew.findByContestAndName(lastContest,"Besatzung 120")),[sort:"id"]),data:testCoordResult120(ROUTE_7)],    
                [name:"CoordResult 'Besatzung 60' '$ROUTE_7'",   count:39,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(task7,Crew.findByContestAndName(lastContest,"Besatzung 60")),[sort:"id"]),data:testCoordResult60(ROUTE_7)],    
               ]
            )
            fcService.printdone "Test '$lastContest.title'"
            ret_test.error = ret.error
            ret_test.message = ret.msgtext
        } else {
            ret_test.error = true
            ret_test.message = "No test found."
        }
		return ret_test
    }
	
	void create_route(Map route, String strecke) {
		if (strecke == "Strecke 7m") {
			fcService.putCoordRoute(route,CoordType.TO,     0,"T/O", 'N',48,6.78612, 'E', 9, 45.6951, 1903, 1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SP,     0,"SP",  'N',48,7.46118, 'E',9, 41.34078,2524,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     1,"CP1", 'N',48,5.5845,  'E',9, 35.82642,2413,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     2,"CP2", 'N',48,13.21854,'E',9, 29.25354,2245,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     3,"CP3", 'N',48,14.97018,'E',9, 44.7258, 2131,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     4,"CP4", 'N',48,19.45464,'E',9, 43.81206,2226,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     5,"CP5", 'N',48,28.88394,'E',9, 54.42102,2554,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     6,"CP6", 'N',48,28.2984, 'E',10,4.75254, 2124,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     7,"CP7", 'N',48,21.54018,'E',10,9.18834, 2085,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     8,"CP8", 'N',48,14.57244,'E',10,12.99804,2209,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.iFP,    0,"iFP", 'N',48,12.14622,'E',10,4.4004,  2193,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.iLDG,   0,"iLDG",'N',48,13.19832,'E',9, 54.72876,500, 1.0, null, null)
			fcService.putCoordRoute(route,CoordType.iSP,    0,"iSP", 'N',48,12.25446,'E',9, 46.19184,2203,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     9,"CP9", 'N',48,8.17152, 'E',9, 53.05554,2259,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,    10,"CP10",'N',48,6.89436, 'E',9, 59.77026,2308,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 1,"CP11",'N',48,5.26938, 'E',9, 59.85162,2452,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 2,"CP12",'N',48,3.87882, 'E',9, 59.34978,2505,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 3,"CP13",'N',48,3.16764, 'E',9, 58.90962,2613,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 4,"CP14",'N',48,2.12592, 'E',9, 57.98466,2531,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 5,"CP15",'N',48,1.38588, 'E',9, 57.10206,2646,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 6,"CP16",'N',48,0.57762, 'E',9, 55.63968,2731,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 7,"CP17",'N',47,59.82114,'E',9, 53.72874,2669,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 8,"CP18",'N',47,59.47158,'E',9, 52.1712, 2724,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 9,"CP19",'N',47,59.28024,'E',9, 50.32314,2537,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,10,"CP20",'N',47,59.24022,'E',9, 49.1409, 2439,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,11,"CP21",'N',47,59.35428,'E',9, 47.49078,2423,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,12,"CP22",'N',47,59.91636,'E',9, 44.94666,2432,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,13,"CP23",'N',48,0.45312, 'E',9, 43.71606,2419,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,14,"CP24",'N',48,1.11954, 'E',9, 42.42648,2413,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,15,"CP25",'N',48,1.60434, 'E',9, 41.68098,2409,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,16,"CP26",'N',48,2.47332, 'E',9, 40.7391, 2446,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,17,"CP27",'N',48,3.26796, 'E',9, 40.04532,2432,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,18,"CP28",'N',48,4.42632, 'E',9, 39.41616,2478,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,19,"CP29",'N',48,5.61012, 'E',9, 39.06072,2406,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,20,"CP30",'N',48,6.55188, 'E',9, 39.03636,2537,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,21,"CP31",'N',48,7.07484, 'E',9, 39.1155, 2455,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,    11,"CP32",'N',48,8.02164, 'E',9, 39.15162,2491,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.FP,     0,"FP",  'N',48,9.02784, 'E',9, 43.24464,2347,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.LDG,    0,"LDG", 'N',48,6.86658, 'E',9, 45.7728, 1903,1.0, null, null)

		} else {
			fcService.putCoordRoute(route,CoordType.TO,     0,"T/O", 'N',48,6.78612, 'E', 9, 45.6951, 1903, 1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SP,     0,"SP",  'N',48,7.46118, 'E',9, 41.34078,2524,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     1,"CP1", 'N',48,5.5845,  'E',9, 35.82642,2413,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     2,"CP2", 'N',48,13.21854,'E',9, 29.25354,2245,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     3,"CP3", 'N',48,14.97018,'E',9, 44.7258, 2131,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     4,"CP4", 'N',48,19.45464,'E',9, 43.81206,2226,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     5,"CP5", 'N',48,28.88394,'E',9, 54.42102,2554,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     6,"CP6", 'N',48,28.2984, 'E',10,4.75254, 2124,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     7,"CP7", 'N',48,21.54018,'E',10,9.18834, 2085,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     8,"CP8", 'N',48,14.57244,'E',10,12.99804,2209,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,     9,"CP9", 'N',48,12.14622,'E',10,4.4004,  2193,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 1,"CP10",'N',48,13.19832,'E',9, 54.72876,500, 1.0, null, null)
			if (strecke == "Strecke 6m") {
				fcService.putCoordRoute(route,CoordType.TP,    10,"CP11",'N',48,12.25446,'E',9, 46.19184,2203,1.0, null, 132)
			} else {
				fcService.putCoordRoute(route,CoordType.TP,    10,"CP11",'N',48,12.25446,'E',9, 46.19184,2203,1.0, null, null)
			}
			fcService.putCoordRoute(route,CoordType.TP,    11,"CP12",'N',48,8.17152, 'E',9, 53.05554,2259,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,    12,"CP13",'N',48,6.89436, 'E',9, 59.77026,2308,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 2,"CP14",'N',48,5.26938, 'E',9, 59.85162,2452,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 3,"CP15",'N',48,3.87882, 'E',9, 59.34978,2505,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 4,"CP16",'N',48,3.16764, 'E',9, 58.90962,2613,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 5,"CP17",'N',48,2.12592, 'E',9, 57.98466,2531,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 6,"CP18",'N',48,1.38588, 'E',9, 57.10206,2646,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 7,"CP19",'N',48,0.57762, 'E',9, 55.63968,2731,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 8,"CP20",'N',47,59.82114,'E',9, 53.72874,2669,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET, 9,"CP21",'N',47,59.47158,'E',9, 52.1712, 2724,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,10,"CP22",'N',47,59.28024,'E',9, 50.32314,2537,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,11,"CP23",'N',47,59.24022,'E',9, 49.1409, 2439,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,12,"CP24",'N',47,59.35428,'E',9, 47.49078,2423,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,13,"CP25",'N',47,59.91636,'E',9, 44.94666,2432,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,14,"CP26",'N',48,0.45312, 'E',9, 43.71606,2419,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,15,"CP27",'N',48,1.11954, 'E',9, 42.42648,2413,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,16,"CP28",'N',48,1.60434, 'E',9, 41.68098,2409,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,17,"CP29",'N',48,2.47332, 'E',9, 40.7391, 2446,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,18,"CP30",'N',48,3.26796, 'E',9, 40.04532,2432,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,19,"CP31",'N',48,4.42632, 'E',9, 39.41616,2478,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,20,"CP32",'N',48,5.61012, 'E',9, 39.06072,2406,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,21,"CP33",'N',48,6.55188, 'E',9, 39.03636,2537,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.SECRET,22,"CP34",'N',48,7.07484, 'E',9, 39.1155, 2455,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.TP,    13,"CP35",'N',48,8.02164, 'E',9, 39.15162,2491,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.FP,     0,"FP",  'N',48,9.02784, 'E',9, 43.24464,2347,1.0, null, null)
			fcService.putCoordRoute(route,CoordType.LDG,    0,"LDG", 'N',48,6.86658, 'E',9, 45.7728, 1903,1.0, null, null)
		}
	}
	
    List testRoute() {
      [[title:ROUTE_5,mark:ROUTE_5],[title:ROUTE_6,mark:ROUTE_6],[title:ROUTE_7,mark:ROUTE_7],[title:ROUTE_5M,mark:ROUTE_5M],[title:ROUTE_6M,mark:ROUTE_6M],[title:ROUTE_7M,mark:ROUTE_7M]]
    }
	
    List testCoordRoute(String strecke) {
	  List l = [	
		  [mark:"T/O", type:CoordType.TO,    latDirection:'N',latGrad:48,latMinute:6.78612, lonDirection:'E',lonGrad:9, lonMinute:45.6951, altitude:1903,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"SP",  type:CoordType.SP,    latDirection:'N',latGrad:48,latMinute:7.46118, lonDirection:'E',lonGrad:9, lonMinute:41.34078,altitude:2524,gatewidth2:1.0,coordTrueTrack:283.0738921712,coordMeasureDistance:27.6341690720,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP1", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:5.5845,  lonDirection:'E',lonGrad:9, lonMinute:35.82642,altitude:2413,gatewidth2:1.0,coordTrueTrack:242.9927805108,coordMeasureDistance:38.2689956682,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP2", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:13.21854,lonDirection:'E',lonGrad:9, lonMinute:29.25354,altitude:2245,gatewidth2:1.0,coordTrueTrack:330.1283636740,coordMeasureDistance:81.5219365944,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP3", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:14.97018,lonDirection:'E',lonGrad:9, lonMinute:44.7258, altitude:2131,gatewidth2:1.0,coordTrueTrack:80.3537644360,coordMeasureDistance:96.7997427055,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
		  [mark:"CP4", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:19.45464,lonDirection:'E',lonGrad:9, lonMinute:43.81206,altitude:2226,gatewidth2:1.0,coordTrueTrack:352.2789081903,coordMeasureDistance:41.9060276350,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP5", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:28.88394,lonDirection:'E',lonGrad:9, lonMinute:54.42102,altitude:2554,gatewidth2:1.0,coordTrueTrack:36.7577376702,coordMeasureDistance:108.9843487966,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP6", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:28.2984, lonDirection:'E',lonGrad:10,lonMinute:4.75254, altitude:2124,gatewidth2:1.0,coordTrueTrack:94.8864626525,coordMeasureDistance:63.6534878383,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP7", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:21.54018,lonDirection:'E',lonGrad:10,lonMinute:9.18834, altitude:2085,gatewidth2:1.0,coordTrueTrack:156.4600910143,coordMeasureDistance:68.2617132154,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP8", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:14.57244,lonDirection:'E',lonGrad:10,lonMinute:12.99804,altitude:2209,gatewidth2:1.0,coordTrueTrack:160.0128420855,coordMeasureDistance:68.6565047302,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP9", type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:12.14622,lonDirection:'E',lonGrad:10,lonMinute:4.4004,  altitude:2193,gatewidth2:1.0,coordTrueTrack:247.0441046808,coordMeasureDistance:57.6038587521,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP10",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:13.19832,lonDirection:'E',lonGrad:9, lonMinute:54.72876,altitude:500, gatewidth2:1.0,coordTrueTrack:279.2712735373,coordMeasureDistance:60.4711394976,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.5308943089,planProcedureTurn:false],
		  [mark:"CP11",type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:12.25446,lonDirection:'E',lonGrad:9, lonMinute:46.19184,altitude:2203,gatewidth2:1.0,coordTrueTrack:260.5795422792,coordMeasureDistance:113.8694720953,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP12",type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:8.17152, lonDirection:'E',lonGrad:9, lonMinute:53.05554,altitude:2259,gatewidth2:1.0,coordTrueTrack:131.7314005896,coordMeasureDistance:56.7995551950,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
		  [mark:"CP13",type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:6.89436, lonDirection:'E',lonGrad:9, lonMinute:59.77026,altitude:2308,gatewidth2:1.0,coordTrueTrack:105.9048009794,coordMeasureDistance:43.1561451728,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"CP14",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:5.26938, lonDirection:'E',lonGrad:9, lonMinute:59.85162,altitude:2452,gatewidth2:1.0,coordTrueTrack:178.0849512072,coordMeasureDistance:15.0557238248,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.0668032787,planProcedureTurn:false],
		  [mark:"CP15",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:3.87882, lonDirection:'E',lonGrad:9, lonMinute:59.34978,altitude:2505,gatewidth2:1.0,coordTrueTrack:193.5567120396,coordMeasureDistance:28.3013541826,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.1254098361,planProcedureTurn:false],
		  [mark:"CP16",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:3.16764, lonDirection:'E',lonGrad:9, lonMinute:58.90962,altitude:2613,gatewidth2:1.0,coordTrueTrack:202.4730890648,coordMeasureDistance:35.4280913076,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.1569672131,planProcedureTurn:false],
		  [mark:"CP17",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:2.12592, lonDirection:'E',lonGrad:9, lonMinute:57.98466,altitude:2531,gatewidth2:1.0,coordTrueTrack:210.6943702086,coordMeasureDistance:46.6460235224,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.2065573770,planProcedureTurn:false],
		  [mark:"CP18",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:1.38588, lonDirection:'E',lonGrad:9, lonMinute:57.10206,altitude:2646,gatewidth2:1.0,coordTrueTrack:218.5750901922,coordMeasureDistance:55.4114925945,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.2450819672,planProcedureTurn:false],
		  [mark:"CP19",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:0.57762, lonDirection:'E',lonGrad:9, lonMinute:55.63968,altitude:2731,gatewidth2:1.0,coordTrueTrack:230.4343741528,coordMeasureDistance:67.1617887859,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.2971311475,planProcedureTurn:false],
		  [mark:"CP20",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.82114,lonDirection:'E',lonGrad:9, lonMinute:53.72874,altitude:2669,gatewidth2:1.0,coordTrueTrack:239.3891679625,coordMeasureDistance:80.9185600538,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.3581967213,planProcedureTurn:false],
		  [mark:"CP21",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.47158,lonDirection:'E',lonGrad:9, lonMinute:52.1712, altitude:2724,gatewidth2:1.0,coordTrueTrack:251.4601796188,coordMeasureDistance:91.0987346807,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.4032786885,planProcedureTurn:false],
		  [mark:"CP22",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.28024,lonDirection:'E',lonGrad:9, lonMinute:50.32314,altitude:2537,gatewidth2:1.0,coordTrueTrack:261.2060343691,coordMeasureDistance:102.6881384846,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.4545081967,planProcedureTurn:false],
		  [mark:"CP23",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.24022,lonDirection:'E',lonGrad:9, lonMinute:49.1409, altitude:2439,gatewidth2:1.0,coordTrueTrack:267.1045938553,coordMeasureDistance:110.0245903421,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.4868852459,planProcedureTurn:false],
		  [mark:"CP24",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.35428,lonDirection:'E',lonGrad:9, lonMinute:47.49078,altitude:2423,gatewidth2:1.0,coordTrueTrack:275.8964955151,coordMeasureDistance:120.3056977629,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.5323770492,planProcedureTurn:false],
		  [mark:"CP25",type:CoordType.SECRET,latDirection:'N',latGrad:47,latMinute:59.91636,lonDirection:'E',lonGrad:9, lonMinute:44.94666,altitude:2432,gatewidth2:1.0,coordTrueTrack:288.2701365508,coordMeasureDistance:136.9082538797,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.6057377049,planProcedureTurn:false],
		  [mark:"CP26",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:0.45312, lonDirection:'E',lonGrad:9, lonMinute:43.71606,altitude:2419,gatewidth2:1.0,coordTrueTrack:303.1001655100,coordMeasureDistance:146.0098062085,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.6463114754,planProcedureTurn:false],
		  [mark:"CP27",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:1.11954, lonDirection:'E',lonGrad:9, lonMinute:42.42648,altitude:2413,gatewidth2:1.0,coordTrueTrack:307.6861309848,coordMeasureDistance:156.1041802682,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.6909836066,planProcedureTurn:false],
		  [mark:"CP28",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:1.60434, lonDirection:'E',lonGrad:9, lonMinute:41.68098,altitude:2409,gatewidth2:1.0,coordTrueTrack:314.1950280940,coordMeasureDistance:162.5440445516,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.7192622951,planProcedureTurn:false],
		  [mark:"CP29",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:2.47332, lonDirection:'E',lonGrad:9, lonMinute:40.7391, altitude:2446,gatewidth2:1.0,coordTrueTrack:324.0658841750,coordMeasureDistance:172.4820843375,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.7635245902,planProcedureTurn:false],
		  [mark:"CP30",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:3.26796, lonDirection:'E',lonGrad:9, lonMinute:40.04532,altitude:2432,gatewidth2:1.0,coordTrueTrack:329.7296452017,coordMeasureDistance:181.0020996020,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.8012295082,planProcedureTurn:false],
		  [mark:"CP31",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:4.42632, lonDirection:'E',lonGrad:9, lonMinute:39.41616,altitude:2478,gatewidth2:1.0,coordTrueTrack:340.0498880935,coordMeasureDistance:192.4132985016,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.8516393443,planProcedureTurn:false],
		  [mark:"CP32",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:5.61012, lonDirection:'E',lonGrad:9, lonMinute:39.06072,altitude:2406,gatewidth2:1.0,coordTrueTrack:348.6579620642,coordMeasureDistance:203.5936313718,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.9012295082,planProcedureTurn:false],
		  [mark:"CP33",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:6.55188, lonDirection:'E',lonGrad:9, lonMinute:39.03636,altitude:2537,gatewidth2:1.0,coordTrueTrack:359.0103704238,coordMeasureDistance:212.3156299657,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.9397540984,planProcedureTurn:false],
		  [mark:"CP34",type:CoordType.SECRET,latDirection:'N',latGrad:48,latMinute:7.07484, lonDirection:'E',lonGrad:9, lonMinute:39.1155, altitude:2455,gatewidth2:1.0,coordTrueTrack:5.7694167765,  coordMeasureDistance:217.1828946526,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.9610655738,planProcedureTurn:false],
		  [mark:"CP35",type:CoordType.TP,    latDirection:'N',latGrad:48,latMinute:8.02164, lonDirection:'E',lonGrad:9, lonMinute:39.15162,altitude:2491,gatewidth2:1.0,coordTrueTrack:1.4587060220,  coordMeasureDistance:225.9531048015,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"FP",  type:CoordType.FP,    latDirection:'N',latGrad:48,latMinute:9.02784, lonDirection:'E',lonGrad:9, lonMinute:43.24464,altitude:2347,gatewidth2:1.0,coordTrueTrack:69.7758183261,coordMeasureDistance:26.9527530644,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
		  [mark:"LDG", type:CoordType.LDG,   latDirection:'N',latGrad:48,latMinute:6.86658, lonDirection:'E',lonGrad:9, lonMinute:45.7728, altitude:1903,gatewidth2:1.0,coordTrueTrack:142.0204175420,coordMeasureDistance:25.3901337778,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
      ]
	  if (strecke == ROUTE_6 || strecke == ROUTE_6M) {
		  // CP11
		  l[12].measureTrueTrack = 132
		  l[12].planProcedureTurn = true
		  // CP12
		  l[13].planProcedureTurn = false
	  }
	  if (strecke == ROUTE_7 || strecke == ROUTE_7M) {
		  // CP9
		  l[10].mark = "iFP"
		  l[10].type = CoordType.iFP
		  // CP10
		  l[11].mark = "iLDG"
		  l[11].type = CoordType.iLDG
		  l[11].secretLegRatio = 0
		  // CP11
		  l[12].mark = "iSP"
		  l[12].type = CoordType.iSP
		  l[12].coordTrueTrack = 260.5795422792
		  l[12].coordMeasureDistance = 53.3983325977
		  // CP12...
		  l[13].mark = "CP9"
		  l[14].mark = "CP10"
		  l[15].mark = "CP11"
		  l[16].mark = "CP12"
		  l[17].mark = "CP13"
		  l[18].mark = "CP14"
		  l[19].mark = "CP15"
		  l[20].mark = "CP16"
		  l[21].mark = "CP17"
		  l[22].mark = "CP18"
		  l[23].mark = "CP19"
		  l[24].mark = "CP20"
		  l[25].mark = "CP21"
		  l[26].mark = "CP22"
		  l[27].mark = "CP23"
		  l[28].mark = "CP24"
		  l[29].mark = "CP25"
		  l[30].mark = "CP26"
		  l[31].mark = "CP27"
		  l[32].mark = "CP28"
		  l[33].mark = "CP29"
		  l[34].mark = "CP30"
		  l[35].mark = "CP31"
		  l[36].mark = "CP32"
	  }
	  return l
    }
	
    List testRouteLegCoord(String strecke) {
	  List l = [	
		  [coordTrueTrack:283.0738921712,coordDistance:2.9842515197, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:null],
		  [coordTrueTrack:242.9927805108,coordDistance:4.1327209145, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:283],
		  [coordTrueTrack:330.1283636740,coordDistance:8.8036648590, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:243],
		  [coordTrueTrack:80.3537644360, coordDistance:10.4535359293,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:330],
		  [coordTrueTrack:352.2789081903,coordDistance:4.5254889455, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:80],
		  [coordTrueTrack:36.7577376702, coordDistance:11.7693681206,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:352],
		  [coordTrueTrack:94.8864626525, coordDistance:6.8740267644, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:37],
		  [coordTrueTrack:156.4600910143,coordDistance:7.3716752932, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:95],
		  [coordTrueTrack:160.0128420855,coordDistance:7.4143093661, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:156],
		  [coordTrueTrack:247.0441046808,coordDistance:6.2207190877, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:160],
		  [coordTrueTrack:279.2712735373,coordDistance:6.5303606369, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:247],
		  [coordTrueTrack:260.5795422792,coordDistance:5.7665585959, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:279],
		  [coordTrueTrack:131.7314005896,coordDistance:6.1338612522, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:261],
		  [coordTrueTrack:105.9048009794,coordDistance:4.6604908394, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:132],
		  [coordTrueTrack:178.0849512072,coordDistance:1.6258881020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:106],
		  [coordTrueTrack:193.5567120396,coordDistance:1.4304136455, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:178],
		  [coordTrueTrack:202.4730890648,coordDistance:0.7696260394, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:194],
		  [coordTrueTrack:210.6943702086,coordDistance:1.2114397640, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:202],
		  [coordTrueTrack:218.5750901922,coordDistance:0.9465949322, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:211],
		  [coordTrueTrack:230.4343741528,coordDistance:1.2689304742, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:219],
		  [coordTrueTrack:239.3891679625,coordDistance:1.4856124479, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:230],
		  [coordTrueTrack:251.4601796188,coordDistance:1.0993709100, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:239],
		  [coordTrueTrack:261.2060343691,coordDistance:1.2515554864, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:251],
		  [coordTrueTrack:267.1045938553,coordDistance:0.7922734187, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:261],
		  [coordTrueTrack:275.8964955151,coordDistance:1.1102707798, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:267],
		  [coordTrueTrack:288.2701365508,coordDistance:1.7929326260, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:276],
		  [coordTrueTrack:303.1001655100,coordDistance:0.9828890204, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:288],
		  [coordTrueTrack:307.6861309848,coordDistance:1.0901051900, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:303],
		  [coordTrueTrack:314.1950280940,coordDistance:0.6954497066, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:308],
		  [coordTrueTrack:324.0658841750,coordDistance:1.0732224391, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:314],
		  [coordTrueTrack:329.7296452017,coordDistance:0.9200880415, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:324],
		  [coordTrueTrack:340.0498880935,coordDistance:1.2323108963, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:330],
		  [coordTrueTrack:348.6579620642,coordDistance:1.2073793596, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:340],
		  [coordTrueTrack:359.0103704238,coordDistance:0.9419004961, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:349],
		  [coordTrueTrack:5.7694167765,  coordDistance:0.5256225364, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:359],
		  [coordTrueTrack:1.4587060220,  coordDistance:0.9471069275, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:6],
		  [coordTrueTrack:69.7758183261, coordDistance:2.9106644778, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:1],
		  [coordTrueTrack:142.0204175420,coordDistance:2.7419150948, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:70],
	  ]
	  if (strecke == ROUTE_6 || strecke == ROUTE_6M) {
		  l[11].measureTrueTrack = 132
		  l[12].turnTrueTrack = 132
	  }
      return l
    }
	
    List testRouteLegTest(String strecke) {
	  if (strecke == ROUTE_7 || strecke == ROUTE_7M) {
		  List l = [
			  [coordTrueTrack:242.9927805108,coordDistance:4.13, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:null],
			  [coordTrueTrack:330.1283636740,coordDistance:8.8,  measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:243],
			  [coordTrueTrack:80.3537644360, coordDistance:10.45,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:330],
			  [coordTrueTrack:352.2789081903,coordDistance:4.53, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:80],
			  [coordTrueTrack:36.7577376702, coordDistance:11.77,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:352],
			  [coordTrueTrack:94.8864626525, coordDistance:6.87, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:37],
			  [coordTrueTrack:156.4600910143,coordDistance:7.37, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:95],
			  [coordTrueTrack:160.0128420855,coordDistance:7.41, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:156],
			  [coordTrueTrack:247.0441046808,coordDistance:6.22, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:160],
			  [coordTrueTrack:279.2712735373,coordDistance:6.53, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:247],
			  [coordTrueTrack:260.5795422792,coordDistance:5.77, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:279],
			  [coordTrueTrack:131.7314005896,coordDistance:6.13, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:261],
			  [coordTrueTrack:105.9048009794,coordDistance:4.66, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:132],
			  [coordTrueTrack:178.0849512072,coordDistance:24.41,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:106],
			  [coordTrueTrack:69.7758183261, coordDistance:2.91, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:1],
	      ]
		  return l
	  } else {
		  List l = [
			  [coordTrueTrack:242.9927805108,coordDistance:4.13, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:null],
			  [coordTrueTrack:330.1283636740,coordDistance:8.8,  measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:243],
			  [coordTrueTrack:80.3537644360, coordDistance:10.45,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:330],
			  [coordTrueTrack:352.2789081903,coordDistance:4.53, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:80],
			  [coordTrueTrack:36.7577376702, coordDistance:11.77,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:352],
			  [coordTrueTrack:94.8864626525, coordDistance:6.87, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:37],
			  [coordTrueTrack:156.4600910143,coordDistance:7.37, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:95],
			  [coordTrueTrack:160.0128420855,coordDistance:7.41, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:156],
			  [coordTrueTrack:247.0441046808,coordDistance:6.22, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:160],
			  [coordTrueTrack:279.2712735373,coordDistance:12.3, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:247],
			  [coordTrueTrack:131.7314005896,coordDistance:6.13, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:261],
			  [coordTrueTrack:105.9048009794,coordDistance:4.66, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:132],
			  [coordTrueTrack:178.0849512072,coordDistance:24.41,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:106],
			  [coordTrueTrack:69.7758183261, coordDistance:2.91, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:1],
	      ]
		  if (strecke == ROUTE_6 || strecke == ROUTE_6M) {
			  l[10].turnTrueTrack = 132
		  }
		  return l
	  }
    }
	
    List testCrew() {
       [[startNum:1,name:"Besatzung 120",mark:"",team:null,tas:120,contestPenalties:0,contestPosition:0,noContestPosition:false,aircraft:[registration:"D-EAAA",type:"",colour:""]],
        [startNum:2,name:"Besatzung 60", mark:"",team:null,tas:60, contestPenalties:0,contestPosition:0,noContestPosition:false,aircraft:[registration:"D-EAAB",type:"",colour:""]],
       ]
    }
	
	List testTask() {
		[[title:ROUTE_5,firstTime:"09:00",takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
    	  preparationDuration:15,risingDurationFormula:"time:10min",maxLandingDurationFormula:"time:10min",parkingDuration:5,minNextFlightDuration:30,
		  procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
	     [title:ROUTE_6,firstTime:"09:00",takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
		  preparationDuration:15,risingDurationFormula:"time:10min",maxLandingDurationFormula:"time:10min",parkingDuration:5,minNextFlightDuration:30,
		  procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
	     [title:ROUTE_7,firstTime:"09:00",takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
		  preparationDuration:15,risingDurationFormula:"time:10min",maxLandingDurationFormula:"time:10min",parkingDuration:5,minNextFlightDuration:30,
		  procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true]
		]
	}
	
	List testTestLegFlight120(String strecke) {
		List l = [
			[planTrueTrack:243.0,planTestDistance:4.13, planTrueHeading:243.0,planGroundSpeed:120.0,planLegTime:0.0344166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:330.0,planTestDistance:8.8,  planTrueHeading:330.0,planGroundSpeed:120.0,planLegTime:0.0733333333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:80.0, planTestDistance:10.45,planTrueHeading:80.0, planGroundSpeed:120.0,planLegTime:0.0870833333,planProcedureTurn:true, planProcedureTurnDuration:1],
			[planTrueTrack:352.0,planTestDistance:4.53, planTrueHeading:352.0,planGroundSpeed:120.0,planLegTime:0.03775,     planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:37.0, planTestDistance:11.77,planTrueHeading:37.0, planGroundSpeed:120.0,planLegTime:0.0980833333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:95.0, planTestDistance:6.87, planTrueHeading:95.0, planGroundSpeed:120.0,planLegTime:0.05725,     planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:156.0,planTestDistance:7.37, planTrueHeading:156.0,planGroundSpeed:120.0,planLegTime:0.0614166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:160.0,planTestDistance:7.41, planTrueHeading:160.0,planGroundSpeed:120.0,planLegTime:0.06175,     planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:247.0,planTestDistance:6.22, planTrueHeading:247.0,planGroundSpeed:120.0,planLegTime:0.0518333333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:279.0,planTestDistance:6.53, planTrueHeading:279.0,planGroundSpeed:120.0,planLegTime:0.0544166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:261.0,planTestDistance:5.77, planTrueHeading:261.0,planGroundSpeed:120.0,planLegTime:0.0480833333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:132.0,planTestDistance:6.13, planTrueHeading:132.0,planGroundSpeed:120.0,planLegTime:0.0510833333,planProcedureTurn:true, planProcedureTurnDuration:1],
			[planTrueTrack:106.0,planTestDistance:4.66, planTrueHeading:106.0,planGroundSpeed:120.0,planLegTime:0.0388333333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:178.0,planTestDistance:1.63, planTrueHeading:178.0,planGroundSpeed:120.0,planLegTime:0.0135833333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:194.0,planTestDistance:1.43, planTrueHeading:194.0,planGroundSpeed:120.0,planLegTime:0.0119166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:202.0,planTestDistance:0.77, planTrueHeading:202.0,planGroundSpeed:120.0,planLegTime:0.0064166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:211.0,planTestDistance:1.21, planTrueHeading:211.0,planGroundSpeed:120.0,planLegTime:0.0100833333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:219.0,planTestDistance:0.95, planTrueHeading:219.0,planGroundSpeed:120.0,planLegTime:0.0079166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:230.0,planTestDistance:1.27, planTrueHeading:230.0,planGroundSpeed:120.0,planLegTime:0.0105833333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:239.0,planTestDistance:1.49, planTrueHeading:239.0,planGroundSpeed:120.0,planLegTime:0.0124166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:251.0,planTestDistance:1.10, planTrueHeading:251.0,planGroundSpeed:120.0,planLegTime:0.0091666667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:261.0,planTestDistance:1.25, planTrueHeading:261.0,planGroundSpeed:120.0,planLegTime:0.0104166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:267.0,planTestDistance:0.79, planTrueHeading:267.0,planGroundSpeed:120.0,planLegTime:0.0065833333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:276.0,planTestDistance:1.11, planTrueHeading:276.0,planGroundSpeed:120.0,planLegTime:0.0092500000,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:288.0,planTestDistance:1.79, planTrueHeading:288.0,planGroundSpeed:120.0,planLegTime:0.0149166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:303.0,planTestDistance:0.98, planTrueHeading:303.0,planGroundSpeed:120.0,planLegTime:0.0081666667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:308.0,planTestDistance:1.09, planTrueHeading:308.0,planGroundSpeed:120.0,planLegTime:0.0090833333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:314.0,planTestDistance:0.70, planTrueHeading:314.0,planGroundSpeed:120.0,planLegTime:0.0058333333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:324.0,planTestDistance:1.07, planTrueHeading:324.0,planGroundSpeed:120.0,planLegTime:0.0089166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:330.0,planTestDistance:0.92, planTrueHeading:330.0,planGroundSpeed:120.0,planLegTime:0.0076666667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:340.0,planTestDistance:1.23, planTrueHeading:340.0,planGroundSpeed:120.0,planLegTime:0.0102500000,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:349.0,planTestDistance:1.21, planTrueHeading:349.0,planGroundSpeed:120.0,planLegTime:0.0100833333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:359.0,planTestDistance:0.94, planTrueHeading:359.0,planGroundSpeed:120.0,planLegTime:0.0078333333,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:6.0,  planTestDistance:0.53, planTrueHeading:6.0,  planGroundSpeed:120.0,planLegTime:0.0044166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:1.0,  planTestDistance:0.95, planTrueHeading:1.0,  planGroundSpeed:120.0,planLegTime:0.0079166667,planProcedureTurn:false,planProcedureTurnDuration:0],
			[planTrueTrack:70.0, planTestDistance:2.91, planTrueHeading:70.0, planGroundSpeed:120.0,planLegTime:0.0242500000,planProcedureTurn:false,planProcedureTurnDuration:0],
		]
		if (strecke == ROUTE_6) {
			l[10].planTrueTrack = 132.0
			l[10].planTrueHeading = 132.0
			l[11].planProcedureTurn = false
			l[11].planProcedureTurnDuration = 0
		} else if (strecke == ROUTE_7) {
			l[11].planProcedureTurn = false
			l[11].planProcedureTurnDuration = 0
		}
		return l
	}
	
	List testTestLegFlight60(String strecke) {
		List l = testTestLegFlight120(strecke)
		l.each {
			it.planGroundSpeed /= 2
			it.planLegTime *= 2
		}
		l[0].planLegTime -= 0.0000000001
		l[1].planLegTime += 0.0000000001
		l[2].planLegTime += 0.0000000001
		l[4].planLegTime += 0.0000000001
		l[6].planLegTime -= 0.0000000001
		l[8].planLegTime += 0.0000000001
		l[9].planLegTime -= 0.0000000001
		l[10].planLegTime += 0.0000000001
		l[11].planLegTime += 0.0000000001
		l[12].planLegTime += 0.0000000001
		l[13].planLegTime += 0.0000000001
		l[14].planLegTime -= 0.0000000001
		l[15].planLegTime -= 0.0000000001
		l[16].planLegTime += 0.0000000001
		l[17].planLegTime -= 0.0000000001
		l[18].planLegTime += 0.0000000001
		l[19].planLegTime -= 0.0000000001
		l[20].planLegTime -= 0.0000000001
		l[21].planLegTime -= 0.0000000001
		l[22].planLegTime += 0.0000000001
		l[24].planLegTime -= 0.0000000001
		l[25].planLegTime -= 0.0000000001
		l[26].planLegTime += 0.0000000001
		l[27].planLegTime += 0.0000000001
		l[28].planLegTime -= 0.0000000001
		l[29].planLegTime -= 0.0000000001
		l[31].planLegTime += 0.0000000001
		l[32].planLegTime += 0.0000000001
		l[33].planLegTime -= 0.0000000001
		l[34].planLegTime -= 0.0000000001
		return l
	}
	
	List testCoordResult120(String strecke) {
	    List l = testCoordRoute(strecke)
		l.each {
			it.coordTrueTrack = 0
			it.coordMeasureDistance = 0
			it.secretLegRatio = 0
		}
		if (strecke == ROUTE_6) {
			l[12].measureTrueTrack = null
			l[12].planProcedureTurn = false
			l[13].planProcedureTurn = false
		} else if (strecke == ROUTE_7) {
			l[13].planProcedureTurn = false
		}
		return l
	}
	
	List testCoordResult60(String strecke) {
		List l = testCoordResult120(strecke)
		return l
	}
}
