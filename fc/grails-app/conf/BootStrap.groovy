import java.util.List;

import org.springframework.web.context.request.RequestContextHolder

class BootStrap {

	def messageSource
	def grailsApplication
	
    static Global global = null 
    static TempData tempData = null
    
    def init = { servletContext ->
		println "Init..."
				
        tempData = new TempData()
        
        boolean db_loaded = false
		boolean db_upgrade = false
		boolean db_nodowngradedable = false
		boolean db_nocompatible = false
        boolean db_migrate = false
		
		if (Global.count() == 0) {
			global = new Global()
			global.save()
		} else {
			global = Global.findByVersionMajorIsNotNull()
			db_loaded = true
		}
		
		if (global.versionMajor < global.DB_MAJOR) {
			global.dbCompatibility = "olderMajor"
		} else if (global.versionMajor > global.DB_MAJOR) {
			global.dbCompatibility = "newerMajor"
		} else {
			if (global.versionMinor < global.DB_MINOR) {
				global.dbCompatibility = "upgrade"
			} else if (global.versionMinor > global.DB_MINOR) {
				global.dbCompatibility = "equalMajorNewerMinor" 
			} else {
				global.dbCompatibility = ""
			}
		}

		if (db_loaded) {
			println "  DB ${global.versionMajor}.${global.versionMinor} loaded."
		} else {
			println "  DB ${global.versionMajor}.${global.versionMinor} created."
		}
		
        mk_root_dir(servletContext, Defs.ROOT_FOLDER_GPXUPLOAD)
        mk_root_dir(servletContext, Defs.ROOT_FOLDER_JOBS)
        mk_root_dir(servletContext, Defs.ROOT_FOLDER_JOBS_DONE)
        mk_root_dir(servletContext, Defs.ROOT_FOLDER_JOBS_ERROR)
        mk_root_dir(servletContext, Defs.ROOT_FOLDER_LIVE)
        
		switch (global.dbCompatibility) {
			case "upgrade":
				println "  Upgrade database..."
				switch (global.versionMajor) {
					case 2:
						if (global.versionMinor < 1) { // DB-2.1 compatibility
							print "    2.1 modifications"
							Contest.findAll().each { Contest contest_instance ->
								contest_instance.contestClassResults = ""
								contest_instance.contestTaskResults = ""
								for (Task task_instance in contest_instance.tasks) {
									if (contest_instance.contestTaskResults) {
										contest_instance.contestTaskResults += ","
									}
									contest_instance.contestTaskResults += "task_${task_instance.id}"
								}
								contest_instance.contestContestTitle = 0
								contest_instance.contestPrintLandscape = true
								contest_instance.contestPrintTaskDetails = false
								contest_instance.contestPrintTaskNamesInTitle = false
								contest_instance.teamTaskResults = ""
								for (Task task_instance in contest_instance.tasks) {
									if (contest_instance.teamTaskResults) {
										contest_instance.teamTaskResults += ","
									}
									contest_instance.teamTaskResults += "task_${task_instance.id}"
								}
								contest_instance.teamPrintLandscape = true
								contest_instance.save()
							}
							Crew.findAll().each { Crew crew_instance ->
								crew_instance.noContestPosition = false
								if (crew_instance.contest.resultClasses) {
									crew_instance.classPosition = crew_instance.contestPosition
									crew_instance.contestPosition = 0
								} else {
									crew_instance.classPosition = 0
								}
								crew_instance.save()
							}
							ResultClass.findAll().each { ResultClass resultclass_instance -> 
								resultclass_instance.contestTaskResults = ""
								for (Task task_instance in resultclass_instance.contest.tasks) {
									if (resultclass_instance.contestTaskResults) {
										resultclass_instance.contestTaskResults += ","
									}
									resultclass_instance.contestTaskResults += "task_${task_instance.id}"
								}
								resultclass_instance.contestPrintLandscape = true
								resultclass_instance.contestPrintTaskDetails = false
								resultclass_instance.contestPrintTaskNamesInTitle = false
								resultclass_instance.save()
							}
							println " done."
						}
						if (global.versionMinor < 2) { // DB-2.2 compatibility
							print "    2.2 modifications"
							Crew.findAll().each { Crew crew_instance ->
								crew_instance.startNum = crew_instance.viewpos + 1
								crew_instance.mark = ""
								crew_instance.save()
							}
							println " done."
						}
						if (global.versionMinor < 3) { // DB-2.3 compatibility
							print "    2.3 modifications"
							Contest.findAll().each { Contest contest_instance ->
								contest_instance.precisionFlying = contest_instance.contestRule.ruleValues.precisionFlying
								contest_instance.bestOfAnalysisTaskNum = 0
								contest_instance.contestPrintTitle = ""
								contest_instance.contestPrintSubtitle = ""
								contest_instance.contestPrintProvisional = false
								contest_instance.contestPrintA3 = false
								contest_instance.teamPrintTitle = ""
								contest_instance.teamPrintSubtitle = ""
								contest_instance.teamPrintProvisional = false
								contest_instance.teamPrintA3 = false
								contest_instance.printPrefix = ""
								contest_instance.contestTeamResults = ""
								for (Team team_instance in contest_instance.teams) {
									if (contest_instance.contestTeamResults) {
										contest_instance.contestTeamResults += ","
									}
									contest_instance.contestTeamResults += "team_${team_instance.id}"
								}
								if (Crew.findByContestAndTeamIsNull(contest_instance)) {
									contest_instance.contestTeamResults += "team_no_team_crew"
								}
								contest_instance.printCrewPrintTitle = ""
								contest_instance.printCrewNumber = true
								contest_instance.printCrewName = true
								contest_instance.printCrewTeam = true
								contest_instance.printCrewClass = true
								contest_instance.printCrewAircraft = true
								contest_instance.printCrewTAS = true
								contest_instance.printCrewEmptyColumn1 = false
								contest_instance.printCrewEmptyTitle1 = ""
								contest_instance.printCrewEmptyColumn2 = false
								contest_instance.printCrewEmptyTitle2 = ""
								contest_instance.printCrewEmptyColumn3 = false
								contest_instance.printCrewEmptyTitle3 = ""
								contest_instance.printCrewLandscape = false
								contest_instance.printCrewA3 = false
								contest_instance.printPointsPrintTitle = ""
								contest_instance.printPointsPlanningTest = true
								contest_instance.printPointsFlightTest = true
								contest_instance.printPointsLandingTest1 = true
								contest_instance.printPointsLandingTest2 = true
								contest_instance.printPointsLandingTest3 = true
								contest_instance.printPointsLandingTest4 = true
								contest_instance.printPointsZero = false
								contest_instance.printPointsLandscape = false
								contest_instance.printPointsA3 = false
								contest_instance.imageLeft = null
								contest_instance.imageLeftHeight = Contest.IMAGEHEIGHT
								contest_instance.imageCenter = null
								contest_instance.imageCenterHeight = Contest.IMAGEHEIGHT
								contest_instance.imageRight = null
								contest_instance.imageRightHeight = Contest.IMAGEHEIGHT
								contest_instance.titleSize = Contest.TITLESIZE
								contest_instance.flightTestTakeoffCorrectSecond = 60
								contest_instance.flightTestTakeoffCheckSeconds = false
								contest_instance.flightTestTakeoffPointsPerSecond = 0
								contest_instance.flightTestSafetyAndRulesInfringementPoints = 0
								contest_instance.flightTestInstructionsNotFollowedPoints = 0
								contest_instance.flightTestFalseEnvelopeOpenedPoints = 0
								contest_instance.flightTestSafetyEnvelopeOpenedPoints = 0
								contest_instance.flightTestFrequencyNotMonitoredPoints = 0
								contest_instance.save()
							}
							ResultClass.findAll().each { ResultClass resultclass_instance ->
								resultclass_instance.precisionFlying = resultclass_instance.contestRule.ruleValues.precisionFlying
								resultclass_instance.contestPrintSubtitle = ""
								resultclass_instance.contestPrintProvisional = false
								resultclass_instance.contestPrintA3 = false
								resultclass_instance.contestTeamResults = ""
								for (Team team_instance in resultclass_instance.contest.teams) {
									if (resultclass_instance.contestTeamResults) {
										resultclass_instance.contestTeamResults += ","
									}
									resultclass_instance.contestTeamResults += "team_${team_instance.id}"
								}
								if (Crew.findByContestAndTeamIsNull(resultclass_instance.contest)) {
									resultclass_instance.contestTeamResults += "team_no_team_crew"
								}
								resultclass_instance.flightTestTakeoffCorrectSecond = 60
								resultclass_instance.flightTestTakeoffCheckSeconds = false
								resultclass_instance.flightTestTakeoffPointsPerSecond = 0
								resultclass_instance.flightTestSafetyAndRulesInfringementPoints = 0
								resultclass_instance.flightTestInstructionsNotFollowedPoints = 0
								resultclass_instance.flightTestFalseEnvelopeOpenedPoints = 0
								resultclass_instance.flightTestSafetyEnvelopeOpenedPoints = 0
								resultclass_instance.flightTestFrequencyNotMonitoredPoints = 0
								resultclass_instance.printPointsPrintTitle = ""
								resultclass_instance.printPointsPlanningTest = true
								resultclass_instance.printPointsFlightTest = true
								resultclass_instance.printPointsLandingTest1 = true
								resultclass_instance.printPointsLandingTest2 = true
								resultclass_instance.printPointsLandingTest3 = true
								resultclass_instance.printPointsLandingTest4 = true
								resultclass_instance.printPointsZero = false
								resultclass_instance.printPointsLandscape = false
								resultclass_instance.printPointsA3 = false
								resultclass_instance.save()
							}
							Task.findAll().each { Task task_instance ->
								task_instance.flightTestCheckSecretPoints = true
								task_instance.flightTestCheckTakeOff = false
								task_instance.flightTestCheckLanding = false
								task_instance.bestOfAnalysis = false
								task_instance.printTimetableJuryPrintTitle = ""
								task_instance.printTimetableJuryNumber = true
								task_instance.printTimetableJuryCrew = true
								task_instance.printTimetableJuryAircraft = true
								task_instance.printTimetableJuryAircraftType = true
								task_instance.printTimetableJuryAircraftColour = false
								task_instance.printTimetableJuryTAS = false
								task_instance.printTimetableJuryTeam = false
								task_instance.printTimetableJuryPlanning = true
								task_instance.printTimetableJuryPlanningEnd = true
								task_instance.printTimetableJuryTakeoff = true
								task_instance.printTimetableJuryStartPoint = false
								task_instance.printTimetableJuryCheckPoints = ""
								task_instance.printTimetableJuryFinishPoint = false
								task_instance.printTimetableJuryLanding = true
								task_instance.printTimetableJuryArrival = true
								task_instance.printTimetableJuryEmptyColumn1 = true
								task_instance.printTimetableJuryEmptyTitle1 = ""
								task_instance.printTimetableJuryEmptyColumn2 = false
								task_instance.printTimetableJuryEmptyTitle2 = ""
								task_instance.printTimetableJuryEmptyColumn3 = false
								task_instance.printTimetableJuryEmptyTitle3 = ""
								task_instance.printTimetableJuryLandscape = true
								task_instance.printTimetableJuryA3 = false
								task_instance.printTimetablePrintTitle = ""
								task_instance.printTimetableNumber = true
								task_instance.printTimetableCrew = true
								task_instance.printTimetableAircraft = true
								task_instance.printTimetableTAS = true
								task_instance.printTimetableTeam = false
								task_instance.printTimetablePlanning = true
								task_instance.printTimetableTakeoff = true
								task_instance.printTimetableVersion = true
								task_instance.printTimetableChange = ""
								task_instance.printTimetableLandscape = false
								task_instance.printTimetableA3 = false
								task_instance.specialTestTitle = ""
								task_instance.hidePlanning = false
								task_instance.hideResults = false
								task_instance.save()
							}
							TaskClass.findAll().each { TaskClass taskclass_instance ->
								taskclass_instance.flightTestCheckSecretPoints = true
								taskclass_instance.flightTestCheckTakeOff = false
								taskclass_instance.flightTestCheckLanding = false
								taskclass_instance.specialTestTitle = ""
								taskclass_instance.save()
							}
							Test.findAll().each { Test test_instance ->
								test_instance.taskAircraft = test_instance.crew.aircraft
								test_instance.flightTestSafetyAndRulesInfringement = false
								test_instance.flightTestInstructionsNotFollowed = false
								test_instance.flightTestFalseEnvelopeOpened = false
								test_instance.flightTestSafetyEnvelopeOpened = false
								test_instance.flightTestFrequencyNotMonitored = false
								test_instance.save()
							}
							Coord.findAll().each { Coord coord_instance ->
								coord_instance.gatewidth2 = coord_instance.gatewidth
								coord_instance.legDuration = null
								coord_instance.noTimeCheck = false
								coord_instance.save()
							}
							Crew.findAll().each { Crew crew_instance ->
								crew_instance.noClassPosition = false
								crew_instance.save()
							}
							println " done."
						}
						if (global.versionMinor < 4) { // DB-2.4 compatibility
							print "    2.4 modifications"
							Route.findAll().each { Route route_instance ->
								BigDecimal next_turntruetrack = null
								RouteLegCoord.findAllByRoute(route_instance,[sort:"id"]).each { RouteLegCoord routelegcoord_instance ->
									routelegcoord_instance.turnTrueTrack = next_turntruetrack
									routelegcoord_instance.save()
									next_turntruetrack = routelegcoord_instance.testTrueTrack()
								}
								next_turntruetrack = null
								RouteLegTest.findAllByRoute(route_instance,[sort:"id"]).each { RouteLegTest routelegtest_instance ->
									routelegtest_instance.turnTrueTrack = next_turntruetrack
									routelegtest_instance.save()
									next_turntruetrack = routelegtest_instance.testTrueTrack()
								}
							}
							Task.findAll().each { Task task_instance ->
								task_instance.takeoffIntervalSlowerAircraft = task_instance.takeoffIntervalNormal
								task_instance.save()
							}
							println " done."
						}
						if (global.versionMinor < 5) { // DB-2.5 compatibility
							print "    2.5 modifications"
							RouteLeg.findAll().each { RouteLeg routeleg_instance ->
								String[] title_values = routeleg_instance.title.split(' ') 
								routeleg_instance.startTitle = CoordTitle.GetCoordTitle(title_values[0])
								routeleg_instance.endTitle = CoordTitle.GetCoordTitle(title_values[2])
								routeleg_instance.startTitle.save()
								routeleg_instance.endTitle.save()
								routeleg_instance.title = ""
								routeleg_instance.save()
							}
							Test.findAll().each { Test test_instance ->
								int tp_num = 0
								int leg_num = TestLegPlanning.countByTest(test_instance)
								for (TestLegPlanning testlegplanning_instance in TestLegPlanning.findAllByTest(test_instance,[sort:"id"])) {
									tp_num++
									if (tp_num == leg_num) {
										testlegplanning_instance.coordTitle = new CoordTitle(CoordType.FP,1)
									} else {
										testlegplanning_instance.coordTitle = new CoordTitle(CoordType.TP,tp_num)
									}
									testlegplanning_instance.coordTitle.save()
									testlegplanning_instance.save()
								}
								tp_num = 0
								leg_num = TestLegFlight.countByTest(test_instance)
								for (TestLegFlight testlegflight_instance in TestLegFlight.findAllByTest(test_instance,[sort:"id"])) {
									tp_num++
									if (tp_num == leg_num) {
										testlegflight_instance.coordTitle = new CoordTitle(CoordType.FP,1)
									} else {
										testlegflight_instance.coordTitle = new CoordTitle(CoordType.TP,tp_num)
									}
									testlegflight_instance.coordTitle.save()
									testlegflight_instance.save()
								}
							}
							println " done."
						}
						if (global.versionMinor < 6) { // DB-2.6 compatibility
							print "    2.6 modifications"
							Coord.findAll().each { Coord coord_instance ->
								coord_instance.noPlanningTest = false
								coord_instance.save()
							}
							println " done."
						}
						if (global.versionMinor < 7) { // DB-2.7 compatibility
							print "    2.7 modifications"
							Contest.findAll().each { Contest contest_instance ->
								contest_instance.landingTest1NotAllowedAerodynamicAuxiliariesPoints = 0
								contest_instance.landingTest2NotAllowedAerodynamicAuxiliariesPoints = 0
								contest_instance.landingTest3NotAllowedAerodynamicAuxiliariesPoints = 0
								contest_instance.landingTest4NotAllowedAerodynamicAuxiliariesPoints = 0
								contest_instance.printCrewAircraftType = false
								contest_instance.printCrewAircraftColour = false
								contest_instance.a3PortraitFactor = 1.414
								contest_instance.a4LandscapeFactor = 1
								contest_instance.a3LandscapeFactor = 1
								contest_instance.save()
							}
							ResultClass.findAll().each { ResultClass resultclass_instance ->
								resultclass_instance.landingTest1NotAllowedAerodynamicAuxiliariesPoints = 0
								resultclass_instance.landingTest2NotAllowedAerodynamicAuxiliariesPoints = 0
								resultclass_instance.landingTest3NotAllowedAerodynamicAuxiliariesPoints = 0
								resultclass_instance.landingTest4NotAllowedAerodynamicAuxiliariesPoints = 0
								resultclass_instance.save()
							}
							Task.findAll().each { Task task_instance ->
								task_instance.risingDurationFormula = "time+:${task_instance.risingDuration}min"
								task_instance.maxLandingDurationFormula =  "time:${task_instance.maxLandingDuration}min"
								task_instance.parkingDuration -= task_instance.maxLandingDuration
								task_instance.iLandingDurationFormula = "wind:1"
								task_instance.iRisingDurationFormula = "wind:1"
								task_instance.save()
							}
							TestLeg.findAll().each { TestLeg testleg_instance ->
								testleg_instance.planFullMinute = false
								testleg_instance.save()
							}
							Test.findAll().each { Test test_instance ->
								test_instance.landingTest1NotAllowedAerodynamicAuxiliaries = false
								test_instance.landingTest2NotAllowedAerodynamicAuxiliaries = false
								test_instance.landingTest3NotAllowedAerodynamicAuxiliaries = false
								test_instance.landingTest4NotAllowedAerodynamicAuxiliaries = false
								test_instance.save()
							}
							println " done."
						}
						if (global.versionMinor < 8) { // DB-2.8 compatibility
							print "    2.8 modifications"
							Contest.findAll().each { Contest contest_instance ->
								contest_instance.printStyle = ""
								contest_instance.printCrewShortClass = false
								contest_instance.imageBottomOn = false
								contest_instance.imageBottomLeft = null
								contest_instance.imageBottomLeftHeight = Contest.IMAGEBOTTOMHEIGHT
								contest_instance.imageBottomLeftText = ""
								contest_instance.imageBottomRight = null
								contest_instance.imageBottomRightHeight = Contest.IMAGEBOTTOMHEIGHT
								contest_instance.imageBottomRightText = ""
								contest_instance.imageBottomTextSize = Contest.IMAGEBOTTOMTEXTSIZE
								contest_instance.printOrganizer = Contest.DEFAULT_ORGANIZER
								contest_instance.contestPrintAircraft = true
								contest_instance.contestPrintTeam = false
								contest_instance.contestPrintClass = false
								contest_instance.contestPrintShortClass = false
								contest_instance.printCrewEmptyColumn4 = false
								contest_instance.printCrewEmptyTitle4 = ""
                                contest_instance.contestPrintTaskTestDetails = ""
                                contest_instance.contestPrintLandingDetails = false
                                contest_instance.liveRefreshSeconds = Contest.LIVE_REFRESHSECONDS
                                contest_instance.liveStylesheet = Defs.LIVE_STYLESHEET
                                contest_instance.livePositionCalculation = 0
                                contest_instance.liveShowSummary = true
                                contest_instance.contestPrintEqualPositions = false
                                contest_instance.teamPrintEqualPositions = false
                                contest_instance.printFreeText = ""
                                contest_instance.printFreeTextTitle = ""
                                contest_instance.printFreeTextLandscape = true
                                contest_instance.printFreeTextA3 = false
                                contest_instance.printFreeTextStyle = Contest.DEFAULT_FREETEXTSTYLE
                                contest_instance.contestRuleForEachClass = true
                                contest_instance.printLandingCalculatorValues = contest_instance.contestRule.ruleValues.printLandingCalculatorValues
								contest_instance.save()
							}
							ResultClass.findAll().each { ResultClass resultclass_instance ->
								resultclass_instance.shortName = resultclass_instance.GetDefaultShortName()
								resultclass_instance.contestPrintAircraft = true
								resultclass_instance.contestPrintTeam = false
								resultclass_instance.contestPrintClass = false
								resultclass_instance.contestPrintShortClass = false
                                resultclass_instance.contestPrintTaskTestDetails = ""
                                resultclass_instance.contestPrintLandingDetails = false
                                resultclass_instance.contestPrintEqualPositions = false
                                resultclass_instance.printLandingCalculatorValues = resultclass_instance.contestRule.ruleValues.printLandingCalculatorValues
								resultclass_instance.save()
							}
							Task.findAll().each { Task task_instance ->
								task_instance.printTimetableClass = false
								task_instance.printTimetableShortClass = false
								task_instance.printTimetableJuryClass = false
								task_instance.printTimetableJuryShortClass = true
								task_instance.printTimetableJuryEmptyColumn4 = false
								task_instance.printTimetableJuryEmptyTitle4 = ""
                                if (task_instance.disabledCheckPoints) {
                                    task_instance.disabledCheckPoints += ","
                                }
                                task_instance.disabledCheckPointsNotFound = task_instance.disabledCheckPoints
                                task_instance.disabledCheckPointsMinAltitude = task_instance.disabledCheckPoints
                                task_instance.disabledCheckPointsProcedureTurn = task_instance.disabledCheckPoints
                                task_instance.disabledCheckPointsBadCourse = ""
                                task_instance.printTimetableLegTimes = false
                                task_instance.briefingTime = ""
                                task_instance.printTimetableOverviewLegTimes = true
                                task_instance.printTimetableOverviewLandscape = false
                                task_instance.printTimetableOverviewA3 = false
								task_instance.save()
							}
                            Team.findAll().each { Team team_instance ->
                                team_instance.disabled = false
                                team_instance.contestEqualPosition = false
                                team_instance.contestAddPosition = 0
                                team_instance.save()
                            }
                            Crew.findAll().each { Crew crew_instance ->
                                crew_instance.contestEqualPosition = false
                                crew_instance.contestAddPosition = 0
                                crew_instance.classEqualPosition = false
                                crew_instance.classAddPosition = 0
                                crew_instance.disabledTeam = false
                                crew_instance.save()
                            }
                            Coord.findAll().each { Coord coord_instance ->
                                coord_instance.noGateCheck = false
                                coord_instance.endCurved = false
                                coord_instance.save()
                            }
                            RouteLeg.findAll().each { RouteLeg routeleg_instance ->
                                routeleg_instance.endCurved = false
                                routeleg_instance.save()
                            }
                            TestLeg.findAll().each { TestLeg testleg_instance ->
                                testleg_instance.noPlanningTest = false
                                testleg_instance.endCurved = false
                                testleg_instance.save()
                            }
							println " done."
						}
                        if (global.versionMinor < 9) { // DB-2.9 compatibility (in FC 2.3 wurde auf < 8 getestet) 
                            print "    2.9 modifications"
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.printCrewOrder = 0
                                contest_instance.save()
                            }
                            Crew.findAll().each { Crew crew_instance ->
                                crew_instance.disabledContest = false
                                crew_instance.save()
                            }
                            Task.findAll().each { Task task_instance ->
                                task_instance.landingTest1Points = 1
                                task_instance.landingTest2Points = 2
                                task_instance.landingTest3Points = 3
                                task_instance.landingTest4Points = 4
                                task_instance.save()
                            }
                            Test.findAll().each { Test test_instance ->
                                test_instance.disabledCrew = false
                                test_instance.landingTest1PowerInAir = false
                                test_instance.landingTest1FlapsInAir = false
                                test_instance.landingTest1TouchingObstacle = false
                                test_instance.landingTest2FlapsInAir = false
                                test_instance.landingTest2TouchingObstacle = false
                                test_instance.landingTest3TouchingObstacle = false
                                test_instance.landingTest4PowerInAir = false
                                test_instance.landingTest4FlapsInAir = false
                                test_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 10) { // DB-2.10 compatibility (in FC 2.3 wurde auf < 9 getestet)
                            print "    2.10 modifications"
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.contestUUID = UUID.randomUUID().toString()
                                contest_instance.printCrewEmail = false
                                contest_instance.printCrewUUID = false
                                contest_instance.save()
                            }
                            Crew.findAll().each { Crew crew_instance ->
                                crew_instance.uuid = UUID.randomUUID().toString()
                                crew_instance.email = ""
                                crew_instance.save()
                            }
                            Test.findAll().each { Test test_instance ->
                                test_instance.aflosStartNum = 0
                                test_instance.flightTestLink = ""
                                test_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 11) { // DB-2.11 compatibility (in FC 2.3 wurde auf < 10 getestet)
                            print "    2.11 modifications"
                            global.liveContestID = 0
                            global.liveUploadSeconds = Defs.LIVE_UPLOADSECONDS
                            global.liveLanguage = "de"
                            println " done."
                        }
                        if (global.versionMinor < 12) { // DB-2.12 compatibility
                            print "    2.12 modifications"
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.coordPresentation = CoordPresentation.DEGREEMINUTE
                                contest_instance.reserve = ""
                                contest_instance.save()
                            }
                            Test.findAll().each { Test test_instance ->
                                test_instance.loggerData = new LoggerDataTest()
                                test_instance.loggerDataStartUtc = ""
                                test_instance.loggerDataEndUtc = ""
                                test_instance.loggerResult = new LoggerResult()
                                test_instance.showAflosMark = false
                                test_instance.reserve = ""
                                test_instance.save()
                            }
                            int id_title = 0
                            BigDecimal wind_direction = 270.0
                            for (FlightTestWind flighttestwind_instance in FlightTestWind.findAll()) {
                                id_title++
                                wind_direction = flighttestwind_instance.wind.direction
                                flighttestwind_instance.TODirection = wind_direction
                                flighttestwind_instance.TOOffset = 0.0
                                flighttestwind_instance.TOOrthogonalOffset = 0.0
                                flighttestwind_instance.LDGDirection = wind_direction
                                flighttestwind_instance.LDGOffset = 0.0
                                flighttestwind_instance.LDGOrthogonalOffset = 0.0
                                flighttestwind_instance.iTOiLDGDirection = wind_direction
                                flighttestwind_instance.iTOiLDGOffset = 0.0
                                flighttestwind_instance.iTOiLDGOrthogonalOffset = 0.0
                                flighttestwind_instance.idTitle = id_title
                                flighttestwind_instance.save()
                            }
                            Route.findAll().each { Route route_instance ->
                                route_instance.showAflosMark = false
                                route_instance.save()
                            }
                            Coord.findAll().each { Coord coord_instance ->
                                switch (coord_instance.type) {
                                    case CoordType.TO:
                                    case CoordType.iTO:
                                        coord_instance.gateDirection = wind_direction
                                        coord_instance.save()
                                        break
                                    case CoordType.LDG:
                                    case CoordType.iLDG:
                                        coord_instance.gateDirection = wind_direction
                                        coord_instance.save()
                                        break
                                }
                            }
                            Task.findAll().each { Task task_instance ->
                                task_instance.reserve = ""
                                task_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 13) { // DB-2.13 compatibility
                            print "    2.13 modifications"
                            CoordTitle.findAll().each { CoordTitle coordtitle_instance ->
                                switch (coordtitle_instance.type) {
                                    case CoordType.UNKNOWN:
                                    case CoordType.TP:
                                    case CoordType.SECRET:
                                        break
                                    default:
                                        if (coordtitle_instance.number == 0) {
                                            coordtitle_instance.number = 1
                                            coordtitle_instance.save()
                                        }
                                        break
                                }
                            }
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.planningTestForbiddenCalculatorsPoints = contest_instance.contestRule.ruleValues.planningTestForbiddenCalculatorsPoints
                                contest_instance.flightTestForbiddenEquipmentPoints = contest_instance.contestRule.ruleValues.flightTestForbiddenEquipmentPoints
                                contest_instance.observationTestEnrouteValueUnit = contest_instance.contestRule.ruleValues.observationTestEnrouteValueUnit
                                contest_instance.observationTestEnrouteCorrectValue = contest_instance.contestRule.ruleValues.observationTestEnrouteCorrectValue
                                contest_instance.observationTestEnrouteInexactValue = contest_instance.contestRule.ruleValues.observationTestEnrouteInexactValue
                                contest_instance.observationTestEnrouteInexactPoints = contest_instance.contestRule.ruleValues.observationTestEnrouteInexactPoints
                                contest_instance.observationTestEnrouteNotFoundPoints = contest_instance.contestRule.ruleValues.observationTestEnrouteNotFoundPoints
                                contest_instance.observationTestEnrouteFalsePoints = contest_instance.contestRule.ruleValues.observationTestEnrouteFalsePoints
                                contest_instance.observationTestTurnpointNotFoundPoints = contest_instance.contestRule.ruleValues.observationTestTurnpointNotFoundPoints
                                contest_instance.observationTestTurnpointFalsePoints = contest_instance.contestRule.ruleValues.observationTestTurnpointFalsePoints
                                contest_instance.increaseFactor = contest_instance.contestRule.ruleValues.increaseFactor
                                contest_instance.minRouteLegs = contest_instance.contestRule.ruleValues.minRouteLegs
                                contest_instance.maxRouteLegs = contest_instance.contestRule.ruleValues.maxRouteLegs
                                contest_instance.scGateWidth = contest_instance.contestRule.ruleValues.scGateWidth
                                contest_instance.unsuitableStartNum = contest_instance.contestRule.ruleValues.unsuitableStartNum
                                contest_instance.turnpointRule = contest_instance.contestRule.ruleValues.turnpointRule
                                contest_instance.turnpointMapMeasurement = contest_instance.contestRule.ruleValues.turnpointMapMeasurement
                                contest_instance.enroutePhotoRule = contest_instance.contestRule.ruleValues.enroutePhotoRule
                                contest_instance.enrouteCanvasRule = contest_instance.contestRule.ruleValues.enrouteCanvasRule
                                contest_instance.enrouteCanvasMultiple = contest_instance.contestRule.ruleValues.enrouteCanvasMultiple
                                contest_instance.minEnroutePhotos = contest_instance.contestRule.ruleValues.minEnroutePhotos
                                contest_instance.maxEnroutePhotos = contest_instance.contestRule.ruleValues.maxEnroutePhotos
                                contest_instance.minEnrouteCanvas = contest_instance.contestRule.ruleValues.minEnrouteCanvas
                                contest_instance.maxEnrouteCanvas = contest_instance.contestRule.ruleValues.maxEnrouteCanvas
                                contest_instance.minEnrouteTargets = contest_instance.contestRule.ruleValues.minEnrouteTargets
                                contest_instance.maxEnrouteTargets = contest_instance.contestRule.ruleValues.maxEnrouteTargets
                                contest_instance.printPointsGeneral = contest_instance.contestRule.ruleValues.printPointsGeneral
                                contest_instance.printPointsObservationTest = contest_instance.contestRule.ruleValues.printPointsObservationTest
                                contest_instance.contestPrintObservationDetails = contest_instance.contestRule.ruleValues.contestPrintObservationDetails
                                contest_instance.printPointsLandingField = contest_instance.contestRule.ruleValues.printPointsLandingField
                                contest_instance.landingFieldImageName = contest_instance.contestRule.ruleValues.landingFieldImageName
                                contest_instance.printPointsTurnpointSign = contest_instance.contestRule.ruleValues.printPointsTurnpointSign
                                contest_instance.printPointsEnrouteCanvas = contest_instance.contestRule.ruleValues.printPointsEnrouteCanvas
                                contest_instance.save()
                            }
                            ResultClass.findAll().each { ResultClass resultclass_instance ->
                                resultclass_instance.planningTestForbiddenCalculatorsPoints = resultclass_instance.contestRule.ruleValues.planningTestForbiddenCalculatorsPoints
                                resultclass_instance.flightTestForbiddenEquipmentPoints = resultclass_instance.contestRule.ruleValues.flightTestForbiddenEquipmentPoints
                                resultclass_instance.observationTestEnrouteValueUnit = resultclass_instance.contestRule.ruleValues.observationTestEnrouteValueUnit
                                resultclass_instance.observationTestEnrouteCorrectValue = resultclass_instance.contestRule.ruleValues.observationTestEnrouteCorrectValue
                                resultclass_instance.observationTestEnrouteInexactValue = resultclass_instance.contestRule.ruleValues.observationTestEnrouteInexactValue
                                resultclass_instance.observationTestEnrouteInexactPoints = resultclass_instance.contestRule.ruleValues.observationTestEnrouteInexactPoints
                                resultclass_instance.observationTestEnrouteNotFoundPoints = resultclass_instance.contestRule.ruleValues.observationTestEnrouteNotFoundPoints
                                resultclass_instance.observationTestEnrouteFalsePoints = resultclass_instance.contestRule.ruleValues.observationTestEnrouteFalsePoints
                                resultclass_instance.observationTestTurnpointNotFoundPoints = resultclass_instance.contestRule.ruleValues.observationTestTurnpointNotFoundPoints
                                resultclass_instance.observationTestTurnpointFalsePoints = resultclass_instance.contestRule.ruleValues.observationTestTurnpointFalsePoints
                                resultclass_instance.increaseFactor = resultclass_instance.contestRule.ruleValues.increaseFactor
                                resultclass_instance.printPointsGeneral = resultclass_instance.contestRule.ruleValues.printPointsGeneral
                                resultclass_instance.printPointsObservationTest = resultclass_instance.contestRule.ruleValues.printPointsObservationTest
                                resultclass_instance.contestPrintObservationDetails = resultclass_instance.contestRule.ruleValues.contestPrintObservationDetails
                                resultclass_instance.printPointsLandingField = resultclass_instance.contestRule.ruleValues.printPointsLandingField
                                resultclass_instance.landingFieldImageName = resultclass_instance.contestRule.ruleValues.landingFieldImageName
                                resultclass_instance.printPointsTurnpointSign = resultclass_instance.contestRule.ruleValues.printPointsTurnpointSign
                                resultclass_instance.printPointsEnrouteCanvas = resultclass_instance.contestRule.ruleValues.printPointsEnrouteCanvas
                                resultclass_instance.save()
                            }
                            Crew.findAll().each { Crew crew_instance ->
                                crew_instance.increaseEnabled = false
                                crew_instance.save()
                            }
                            Route.findAll().each { Route route_instance ->
                                route_instance.turnpointRoute = TurnpointRoute.None
                                route_instance.turnpointMapMeasurement = false
                                route_instance.enroutePhotoRoute = EnrouteRoute.None
                                route_instance.enrouteCanvasRoute = EnrouteRoute.None
                                route_instance.enroutePhotoMeasurement = EnrouteMeasurement.None
                                route_instance.enrouteCanvasMeasurement = EnrouteMeasurement.None
                                route_instance.save()
                            }
                            Coord.findAll().each { Coord coord_instance ->
                                coord_instance.assignedSign = TurnpointSign.None
                                coord_instance.correctSign = TurnpointCorrect.Unassigned
                                coord_instance.enroutePhotoName = ""
                                coord_instance.enrouteCanvasSign = EnrouteCanvasSign.None
                                coord_instance.enrouteViewPos = 0
                                coord_instance.enrouteDistance = null
                                coord_instance.enrouteDistanceOk = true
                                coord_instance.enrouteOrthogonalDistance = 0
                                coord_instance.save()
                            }
                            Task.findAll().each { Task task_instance ->
                                task_instance.increaseEnabled = true
                                task_instance.disabledCheckPointsTurnpointObs = ""
                                task_instance.disabledEnroutePhotoObs = ""
                                task_instance.disabledEnrouteCanvasObs = ""
                                task_instance.observationTestTurnpointRun = true
                                task_instance.observationTestEnroutePhotoRun = true
                                task_instance.observationTestEnrouteCanvasRun = true
                                task_instance.save()
                            }
                            TaskClass.findAll().each { TaskClass taskclass_instance ->
                                taskclass_instance.observationTestTurnpointRun = true
                                taskclass_instance.observationTestEnroutePhotoRun = true
                                taskclass_instance.observationTestEnrouteCanvasRun = true
                                taskclass_instance.save()
                            }
                            Test.findAll().each { Test test_instance ->
                                test_instance.planningTestForbiddenCalculators = false
                                test_instance.flightTestForbiddenEquipment = false
                                test_instance.observationTestEnroutePhotoValueUnit = null
                                test_instance.observationTestEnrouteCanvasValueUnit = null
                                test_instance.observationTestOtherPenalties = 0
                                test_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 14) { // DB-2.14 compatibility
                            print "    2.14 modifications"
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.liveTrackingContestID = 0
                                contest_instance.save()
                            }
                            Task.findAll().each { Task task_instance ->
                                task_instance.liveTrackingNavigationTaskID = 0
                                task_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 15) { // DB-2.15 compatibility
                            print "    2.15 modifications"
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.liveTrackingContestDate = ""
                                contest_instance.save()
                            }
                            Crew.findAll().each { Crew crew_instance ->
                                crew_instance.liveTrackingTeamID = 0
								crew_instance.trackerID = ""
                                crew_instance.save()
                            }
                            Task.findAll().each { Task task_instance ->
                                task_instance.liveTrackingNavigationTaskDate = ""
                                task_instance.liveTrackingTracksAvailable = false
                                task_instance.save()
                            }
                            Test.findAll().each { Test test_instance ->
                                test_instance.taskTrackerID = ""
                                test_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 16) { // DB-2.16 compatibility
                            print "    2.16 modifications"
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.printCrewTrackerID = false
                                contest_instance.crewPilotNavigatorDelimiter = ","
                                contest_instance.crewSurnameForenameDelimiter = ""
                                contest_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 17) { // DB-2.17 compatibility
                            print "    2.17 modifications"
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.liveTrackingScorecard = contest_instance.contestRule.ruleValues.liveTrackingScorecard
                                contest_instance.flightTestBadCourseMaxPoints = contest_instance.contestRule.ruleValues.flightTestBadCourseMaxPoints
                                contest_instance.save()
                            }
							ResultClass.findAll().each { ResultClass resultclass_instance ->
                                resultclass_instance.flightTestBadCourseMaxPoints = resultclass_instance.contestRule.ruleValues.flightTestBadCourseMaxPoints
								resultclass_instance.save()
							}
                            println " done."
                        }
                        if (global.versionMinor < 18) { // DB-2.18 compatibility
                            print "    2.18 modifications"
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.useProcedureTurns = contest_instance.contestRule.ruleValues.useProcedureTurns
                                contest_instance.save()
                            }
                            Route.findAll().each { Route route_instance ->
                                route_instance.useProcedureTurns = route_instance.contest.contestRule.ruleValues.useProcedureTurns && OldCSSProperties.UseProcedureTurn(route_instance)
                                route_instance.liveTrackingScorecard = route_instance.contest.contestRule.ruleValues.liveTrackingScorecard
                                route_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 19) { // DB-2.19 compatibility
                            print "    2.19 modifications"
                            ResultClass.findAll().each { ResultClass resultclass_instance ->
                                resultclass_instance.secretGateWidth = OldCSSProperties.GetSecretGateWidth(resultclass_instance)
                                resultclass_instance.minutesBeforeStartTime = OldCSSProperties.GetMinutesBeforeStartTime(resultclass_instance)
                                resultclass_instance.minutesAddSubmission = OldCSSProperties.GetMinutesAddSubmission(resultclass_instance)
                                resultclass_instance.save()
                            }
                            Crew.findAll().each { Crew crew_instance ->
                                if (crew_instance.trackerID.startsWith("tracker-") && crew_instance.trackerID.contains("-${crew_instance.startNum}-")) {
                                    crew_instance.trackerID = ""
                                }
                                crew_instance.save()
                            }
                            Test.findAll().each { Test test_instance ->
                                if (test_instance.taskTrackerID.startsWith("tracker-") && test_instance.taskTrackerID.contains("-${test_instance.crew.startNum}-")) {
                                    test_instance.taskTrackerID = ""
                                }
                                test_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 20) { // DB-2.20 compatibility
                            print "    2.20 modifications"
                            FlightTest.findAll().each { FlightTest flighttest_instance ->
                                Map flighttest_style = OldCSSProperties.GetTestLegStyle(flighttest_instance.task.contest)
                                flighttest_instance.flightPlanShowLegDistance = flighttest_style.showLegDistance
                                flighttest_instance.flightPlanShowTrueTrack = flighttest_style.showTrueTrack
                                flighttest_instance.flightPlanShowTrueHeading = flighttest_style.showTrueHeading
                                flighttest_instance.flightPlanShowGroundSpeed = flighttest_style.showGroundSpeed
                                flighttest_instance.flightPlanShowLocalTime = flighttest_style.showLocalTime
                                flighttest_instance.flightPlanShowElapsedTime = flighttest_style.showElapsedTime
                                flighttest_instance.submissionMinutes = OldCSSProperties.GetSubmissionMinutes(flighttest_instance.task.contest)
                                flighttest_instance.flightPlanAddTPNum = OldCSSProperties.GetAddTPNum(flighttest_instance.route)
                                flighttest_instance.flightResultsShowCurvedPoints = OldCSSProperties.ShowFlightResultsCurvedPoints(flighttest_instance.task.contest)
                                flighttest_instance.save()
                            }
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.flightPlanShowLegDistance = contest_instance.contestRule.ruleValues.flightPlanShowLegDistance
                                contest_instance.flightPlanShowTrueTrack = contest_instance.contestRule.ruleValues.flightPlanShowTrueTrack
                                contest_instance.flightPlanShowTrueHeading = contest_instance.contestRule.ruleValues.flightPlanShowTrueHeading
                                contest_instance.flightPlanShowGroundSpeed = contest_instance.contestRule.ruleValues.flightPlanShowGroundSpeed
                                contest_instance.flightPlanShowLocalTime = contest_instance.contestRule.ruleValues.flightPlanShowLocalTime
                                contest_instance.flightPlanShowElapsedTime = contest_instance.contestRule.ruleValues.flightPlanShowElapsedTime
                                contest_instance.flightTestSubmissionMinutes = contest_instance.contestRule.ruleValues.flightTestSubmissionMinutes
                                contest_instance.contestLandingResultsFactor = OldCSSProperties.GetLandingResultsFactor(contest_instance)
                                contest_instance.save()
                            }
                            Route.findAll().each { Route route_instance ->
                                route_instance.showCurvedPoints = OldCSSProperties.ShowCurvedPoints(route_instance)
                                route_instance.save()
                            }
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.printStyle = OldCSSProperties.DeleteAllProperties(contest_instance)
                                contest_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 21) { // DB-2.21 compatibility
                            print "    2.21 modifications"
                            Route.findAll().each { Route route_instance ->
                                route_instance.mapScale = route_instance.contest.mapScale
                                route_instance.contestMapAirfields = Defs.CONTESTMAPAIRFIELDS_OSM_ICAO
                                route_instance.contestMapCircle = true
                                route_instance.contestMapProcedureTurn = true
                                route_instance.contestMapLeg = true
                                route_instance.contestMapCurvedLeg = true
                                route_instance.contestMapCurvedLegPoints = Defs.CONTESTMAPPOINTS_INIT
                                route_instance.contestMapTpName = true
                                route_instance.contestMapSecretGates = false
                                route_instance.contestMapEnroutePhotos = false
                                route_instance.contestMapEnrouteCanvas = false
                                route_instance.contestMapGraticule = true
                                route_instance.contestMapContourLines = Defs.CONTESTMAPCONTOURLINES_100M
                                route_instance.contestMapMunicipalityNames = true
                                route_instance.contestMapChurches = true
                                route_instance.contestMapCastles = true
                                route_instance.contestMapChateaus = true
                                route_instance.contestMapPowerlines = true
                                route_instance.contestMapWindpowerstations = true
                                route_instance.contestMapSmallRoads = false
                                route_instance.contestMapPeaks = true
                                route_instance.contestMapDropShadow = false
                                route_instance.contestMapAdditionals = true
                                route_instance.contestMapSpecials = false
                                route_instance.contestMapAirspaces = false
                                route_instance.contestMapAirspacesLayer = ""
                                route_instance.contestMapCenterHorizontalPos = HorizontalPos.Center
                                route_instance.contestMapCenterVerticalPos = VerticalPos.Center
                                route_instance.SetAllContestMapPoints()
                                route_instance.contestMapPrintLandscape = true
                                route_instance.contestMapPrintSize = Defs.CONTESTMAPPRINTSIZE_A3
                                route_instance.save()
                            }
                            Task.findAll().each { Task task_instance ->
                                task_instance.printTimetableJurySubmission = true
                                task_instance.printTimetableJuryLandingField = false
                                task_instance.save()
                            }
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.ruleTitle = contest_instance.contestRule.ruleValues.ruleTitle
                                contest_instance.save()
                            }
							ResultClass.findAll().each { ResultClass resultclass_instance ->
                                resultclass_instance.ruleTitle = resultclass_instance.contestRule.ruleValues.ruleTitle
								resultclass_instance.save()
							}
                            println " done."
                        }
                        if (global.versionMinor < 22) { // DB-2.22 compatibility
                            print "    2.22 modifications"
                            Route.findAll().each { Route route_instance ->
                                route_instance.contestMapCenterHorizontalPos2 = HorizontalPos.Center
                                route_instance.contestMapShowSecondOptions = false
                                route_instance.contestMapCenterVerticalPos2 = VerticalPos.Center
                                route_instance.SetAllContestMapPoints()
                                route_instance.contestMapPrintLandscape2 = true
                                route_instance.contestMapPrintSize2 = Defs.CONTESTMAPPRINTSIZE_A3
                                route_instance.contestMapEdition = 0
                                route_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 23) { // DB-2.23 compatibility
                            print "    2.23 modifications"
                            Task.findAll().each { Task task_instance ->
                                task_instance.liveTrackingResultsTaskID = 0
                                task_instance.liveTrackingResultsPlanningID = 0
                                task_instance.liveTrackingResultsFlightID = 0
                                task_instance.liveTrackingResultsFlightOn = false
                                task_instance.liveTrackingResultsObservationID = 0
                                task_instance.liveTrackingResultsLandingID = 0
                                task_instance.liveTrackingResultsSpecialID = 0
                                task_instance.liveTrackingResultsPublishImmediately = false
                                task_instance.save()
                            }
                            Test.findAll().each { Test test_instance ->
                                test_instance.planningTestLiveTrackingResultOk = false
                                test_instance.planningTestLiveTrackingResultError = false
                                test_instance.flightTestLiveTrackingResultOk = false
                                test_instance.flightTestLiveTrackingResultError = false
                                test_instance.observationTestLiveTrackingResultOk = false
                                test_instance.observationTestLiveTrackingResultError = false
                                test_instance.landingTestLiveTrackingResultOk = false
                                test_instance.landingTestLiveTrackingResultError = false
                                test_instance.specialTestLiveTrackingResultOk = false
                                test_instance.specialTestLiveTrackingResultError = false
                                test_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 24) { // DB-2.24 compatibility
                            print "    2.24 modifications"
                            Crew.findAll().each { Crew crew_instance ->
                                crew_instance.liveTrackingContestTeamID = 0
                                crew_instance.liveTrackingDifferences = false
                                crew_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < 25) { // DB-2.25 compatibility
                            print "    2.25 modifications"
                            Contest.findAll().each { Contest contest_instance ->
                                contest_instance.liveTrackingContestVisibility = Defs.LIVETRACKING_VISIBILITY_PRIVATE
                                contest_instance.save()
                            }
                            Task.findAll().each { Task task_instance ->
                                task_instance.liveTrackingNavigationTaskVisibility = Defs.LIVETRACKING_VISIBILITY_PRIVATE
                                task_instance.save()
                            }
                            Test.findAll().each { Test test_instance ->
                                test_instance.taskLiveTrackingTeamID = 0
                                test_instance.save()
                            }
                            println " done."
                        }
                        if (global.versionMinor < global.DB_MINOR) {
                            db_migrate = true
                        }
						break
				}
				global.versionMajor = global.DB_MAJOR
				global.versionMinor = global.DB_MINOR
				global.save()
				println "  DB ${global.versionMajor}.${global.versionMinor} upgraded."
				break
			case "olderMajor":
				global.save()
				println "  You are using database of older Flight Contest. Flight Contest cannot be started."
				break
			case "newerMajor":
			case "equalMajorNewerMinor":
				global.save()
				println "  You are using database of newer Flight Contest. Flight Contest cannot be started."
				break
		}
		
        if (global.liveContestID) {
            global.liveContestID = 0
            global.save()
            println "  Live disabled."
        }
        
        if (grailsApplication.config.flightcontest.migrate_force) {
            db_migrate = true
            println "  Force table migration"
        }
        if (db_migrate) {
            if (grailsApplication.config.flightcontest.migrate_tables) {
                println "  Run table migration..."
                try {
                    grailsApplication.config.flightcontest.migrate_tables.each { table, value  ->
                        println "    Table ${table}"
                        Class table_clazz = grailsApplication.getDomainClass(table).clazz
                        value.data.each { data_id, fields ->
                            println "      Search ${value.key} == ${data_id}"
                            if (value.type == "int") {
                                data_id = data_id.toInteger()
                            } else if (value.key == "id") {
                                data_id = data_id.toLong()
                            }
                            def table_obj = table_clazz.findWhere((value.key):data_id)
                            if (table_obj) {
                                println "        Row found."
                                fields.each { field_name, field_value ->
                                    String old_field_value = table_obj."${field_name}"
                                    table_obj."${field_name}" = field_value
                                    println "        Field ${field_name}: '${old_field_value}' -> '${field_value}'"
                                }
                                table_obj.save()
                            }
                        }
                    }
                    println "  Done"
                } catch (Exception e) {
                    println "  Exception ${e.getMessage()}"
                }
            }
        }
        
		// add method getMsg to all domain classes
		grailsApplication.domainClasses.each { domain_class ->
			domain_class.metaClass.getMsg = { code ->
                def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
	            return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
			}
            domain_class.metaClass.getMsgArgs = { code, args ->
                def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
                return messageSource.getMessage(code, args.toArray(), new Locale(session_obj.showLanguage))
            }
			domain_class.metaClass.getPrintMsg = { code ->
                def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
	            return messageSource.getMessage(code, null, new Locale(session_obj.printLanguage))
			}
            domain_class.metaClass.getPrintMsgArgs = { code, args ->
                def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
                return messageSource.getMessage(code, args.toArray(), new Locale(session_obj.printLanguage))
            }
			domain_class.metaClass.getTrackingMsg = { code ->
	            return messageSource.getMessage(code, null, new Locale("en"))
			}
            domain_class.metaClass.getTrackingMsgArgs = { code, args ->
                return messageSource.getMessage(code, args.toArray(), new Locale("en"))
            }
		}
        
        if (!global.IsLogPossible()) {
            println "  No log."
        }
		
		println "Init done."
    }
    
    def destroy = {
    }
    
    private void mk_root_dir(def actServletContext, String dirName)
    {
        String webroot_dir =  actServletContext.getRealPath("/")
        File root_dir = new File("${webroot_dir}${dirName}")
        if (!root_dir.exists()) {
            println "'${root_dir}' created."
            root_dir.mkdir()
        }
    }
} 