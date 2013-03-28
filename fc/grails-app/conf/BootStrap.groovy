import java.util.List;

class BootStrap {

	def messageSource
	def grailsApplication
	
    static Global global = null 
    
    def init = { servletContext ->
		println "Init..."
				
		boolean db_loaded = false
		boolean db_upgrade = false
		boolean db_nodowngradedable = false
		boolean db_nocompatible = false
		
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
								contest_instance.contestPrintTaskDetails = true
								contest_instance.contestPrintTaskNamesInTitle = true
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
								resultclass_instance.contestPrintTaskDetails = true
								resultclass_instance.contestPrintTaskNamesInTitle = true
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
								task_instance.printTimetableJuryAircraftType = false
								task_instance.printTimetableJuryAircraftColour = false
								task_instance.printTimetableJuryTAS = true
								task_instance.printTimetableJuryTeam = false
								task_instance.printTimetableJuryPlanning = true
								task_instance.printTimetableJuryPlanningEnd = true
								task_instance.printTimetableJuryTakeoff = true
								task_instance.printTimetableJuryStartPoint = true
								task_instance.printTimetableJuryCheckPoints = ""
								task_instance.printTimetableJuryFinishPoint = true
								task_instance.printTimetableJuryLanding = true
								task_instance.printTimetableJuryArrival = true
								task_instance.printTimetableJuryEmptyColumn1 = false
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
		
		// add method getMsg to all domain classes
		grailsApplication.domainClasses.each { domain_class ->
			domain_class.metaClass.getMsg = {
	            return messageSource.getMessage(it, null, new Locale(global.showLanguage))
			}
			domain_class.metaClass.getPrintMsg = {
	            return messageSource.getMessage(it, null, new Locale(global.printLanguage))
			}
		}
		
		println "Init done."
    }
    
    def destroy = {
    }
} 