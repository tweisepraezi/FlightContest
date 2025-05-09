import java.util.Map;

class Contest 
{
	static final int PRINTSTYLESIZE = 10000             // DB-2.8
    static final int PRINTFREETEXTSIZE = 10000          // DB-2.8
    static final String DEFAULT_FREETEXTSTYLE = "td { font-size: 1000%; padding-top:1cm; }\ntd#1 { padding-top:2cm; }" // DB-2.8
	static final int IMAGEMAXSIZE = 1048576             // 1MB, DB-2.3
	static final int IMAGEHEIGHT = 40                   // DB-2.3
	static final int IMAGEBOTTOMHEIGHT = 40             // DB-2.8
	static final String TITLESIZE = "2.0em"             // DB-2.3
	static final String IMAGEBOTTOMTEXTSIZE = "16"      // DB-2.8
	static final BigDecimal A3PORTRAITFACTOR = 1.414    // DB-2.8
	static final BigDecimal A4LANDSCAPEFACTOR = 1       // DB-2.8
	static final BigDecimal A3LANDSCAPEFACTOR = 1       // DB-2.8
	
	static final String LANDING_NO = "NO"               // DB-2.8
	static final String LANDING_OUT = "OUT"             // DB-2.8
    
	String title = ""
	int mapScale = 200000                               // UNUSED, since DB-2.21
    CoordPresentation coordPresentation = CoordPresentation.DEGREEMINUTE // DB-2.12 
    TimeZone timeZone2 = TimeZone.getDefault()          // DB-2.21
    String timeZone = "02:00"                           // Difference between UTC and local time [hh:mm]
	boolean resultClasses = false          	            // Klassen, DB-2.0
	ContestRules contestRule = ContestRules.Empty       // Wettbewerbsordnung, DB-2.0, empty DB-2.41
    Boolean contestRuleForEachClass = false             // Eigene Wettbewerbsordnung für jede Klasse, DB-2.8 
    String ruleTitle = ""                               // DB-2.21
	Boolean precisionFlying = false                     // DB-2.3
    Boolean useProcedureTurns = false                   // DB-2.18
    Integer increaseFactor = 0                          // DB-2.13
	boolean testExists = false                          // Integrierter Test vorhanden, DB-2.0
	boolean aflosTest = false                           // Nutzung der AFLOS-Test-Datenbank, DB-2.0
	boolean aflosUpload = false                         // Nutzung einer geuploadeten AFLOS-Datenbank, DB-2.0 
	Integer bestOfAnalysisTaskNum = 0                   // Anzahl der Aufgaben, aus denen das beste Ergebnis zu nehmen ist, DB-2.3
	String printPrefix = ""                             // Prefix for print, DB-2.3
	String printOrganizer = ""                          // DB-2.8, empty DB-2.41
    String contestUUID = UUID.randomUUID().toString()   // DB-2.10

	// Wettbewerbs-Auswertung
	String contestClassResults = ""		                // Zu berücksichtigende Klassen, DB-2.1
	String contestTaskResults = ""                      // Zu berücksichtigende Aufgaben, DB-2.1
	String contestTeamResults = ""                      // Zu berücksichtigende Teams, DB-2.3
	boolean contestPlanningResults = true               // Planungstest berüchsichtigen, DB-2.0
	boolean contestFlightResults = true                 // Navigationstest berüchsichtigen, DB-2.0
	boolean contestObservationResults = true            // Beobachtungstest berüchsichtigen, DB-2.0
	boolean contestLandingResults = true                // Landetest berüchsichtigen, DB-2.0
    BigDecimal contestLandingResultsFactor              // DB-2.20
	boolean contestSpecialResults = false               // Sondertest berücksichtigen, DB-2.0
	Integer contestContestTitle = 0            	        // Wettbewerbstitel beim Ausdruck, DB-2.1
	String contestPrintTitle = ""                       // Ausdruck-Titel, DB-2.3
	String contestPrintSubtitle = ""                    // Ausdruck-Untertitel, DB-2.3
	Boolean contestPrintLandscape = true                // Ausdruck quer, DB-2.1
	Boolean contestPrintTaskDetails = false             // Ausdruck der Aufgabensummen in Liste, DB-2.1
    String contestPrintTaskTestDetails = ""             // Ausdruck der Aufgabendetails in Liste, DB-2.8
    Boolean contestPrintObservationDetails = false      // Ausdruck der Beobachtungsdetails in Liste, DB-2.13
    Boolean contestPrintLandingDetails = false          // Ausdruck der Landedetails in Liste, DB-2.8
	Boolean contestPrintTaskNamesInTitle = false        // Ausdruck der Aufgabennamen im Title, DB-2.1
	Boolean contestPrintAircraft = true                 // Ausdruck des Flugzeuges in Liste, DB-2.8
	Boolean contestPrintTeam = false                    // Ausdruck des Teams in Liste, DB-2.8
	Boolean contestPrintClass = false                   // Ausdruck der Klasse in Liste, DB-2.8
	Boolean contestPrintShortClass = false              // Ausdruck des kurzen Klassennamens in Liste, DB-2.8
	Boolean contestPrintProvisional = false             // Ausdruck "vorläufig", DB-2.3
	Boolean contestPrintA3 = false                      // Ausdruck A3, DB-2.3
    Boolean contestPrintEqualPositions = false          // Ausdruck gleicher Positionen, DB-2.8
	String contestPrintFooter = ""                      // Ausdruck Fußzeilen, DB-2.35
    
	// Team-Auswertung
	int teamCrewNum = 0                    	            // Anzahl von Besatzungen, DB-2.0
	String teamClassResults = ""		                // Zu berücksichtigende Klassen, DB-2.0
	String teamTaskResults = ""                         // Zu berücksichtigende Aufgaben, DB-2.1
	boolean teamPlanningResults = true     	            // Planungstest berüchsichtigen, DB-2.0
	boolean teamFlightResults = true       	            // Navigationstest berüchsichtigen, DB-2.0
	boolean teamObservationResults = true  	            // Beobachtungstest berüchsichtigen, DB-2.0
	boolean teamLandingResults = true      	            // Landetest berüchsichtigen, DB-2.0
	boolean teamSpecialResults = false     	            // Sondertest berücksichtigen, DB-2.0
	int teamContestTitle = 0               	            // Wettbewerbstitel beim Ausdruck, DB-2.0
	String teamPrintTitle = ""                          // Ausdruck-Titel, DB-2.3
	String teamPrintSubtitle = ""                       // Ausdruck-Untertitel, DB-2.3
	Boolean teamPrintLandscape = true                   // Ausdruck quer, DB-2.1
	Boolean teamPrintProvisional = false                // Ausdruck "vorläufig", DB-2.3
	Boolean teamPrintA3 = false                         // Ausdruck A3, DB-2.3
    Boolean teamPrintEqualPositions = false             // Ausdruck gleicher Positionen, DB-2.8
	String teamPrintFooter = ""                         // Ausdruck Fußzeilen, DB-2.35
	
    // Live-Auswertung
    Integer liveRefreshSeconds = Defs.LIVE_REFRESHSECONDS  // Live-Anzeige-Refresh-Zeit in Sekunden, DB-2.8
    String liveStylesheet = Defs.LIVE_STYLESHEET        // Live-Anzeige-Stylesheet, DB-2.8
    Integer livePositionCalculation = 0                 // Live-Positions-Berechnung (0 = Contest, sonst Task), DB-2.8
    Boolean liveShowSummary = true                      // Live-Summary-Anzeige, DB-2.8
    Integer liveShowSize = Defs.LIVE_SHOWSIZE           // Live-Anzeige-Crew-Anzahl, DB-2.36
    Integer liveNewestShowSize = Defs.LIVE_NEWESTSHOWSIZE  // Live-Anzeige-Neueste-Crew-Anzahl, DB-2.36
    
	// PlanningTest
	int planningTestDirectionCorrectGrad = 2
	int planningTestDirectionPointsPerGrad = 2
	int planningTestTimeCorrectSecond = 5
	int planningTestTimePointsPerSecond = 1
	int planningTestMaxPoints = 350
	int planningTestPlanTooLatePoints = 50
	int planningTestExitRoomTooLatePoints = 100
    Integer planningTestForbiddenCalculatorsPoints = 0  // DB-2.13

	// FlightTest
	int flightTestTakeoffMissedPoints = 200
	Integer flightTestTakeoffCorrectSecond = 60         // DB-2.3
	Boolean flightTestTakeoffCheckSeconds = false       // DB-2.3
	Integer flightTestTakeoffPointsPerSecond = 3        // DB-2.3
	int flightTestCptimeCorrectSecond = 2
	int flightTestCptimePointsPerSecond = 1
	int flightTestCptimeMaxPoints = 200
	int flightTestCpNotFoundPoints = 200
	int flightTestProcedureTurnNotFlownPoints = 200
	int flightTestMinAltitudeMissedPoints = 500
	int flightTestBadCourseCorrectSecond = 5
	int flightTestBadCoursePoints = 200
    Integer flightTestBadCourseMaxPoints = 1000         // DB-2.17
	int flightTestBadCourseStartLandingPoints = 500
    Boolean flightTestBadCourseStartLandingSeparatePoints = false // DB-2.41
	int flightTestLandingToLatePoints = 200
	int flightTestGivenToLatePoints = 100
	Integer flightTestSafetyAndRulesInfringementPoints = 0 // DB-2.3
	Integer flightTestInstructionsNotFollowedPoints = 0 // DB-2.3
	Integer flightTestFalseEnvelopeOpenedPoints = 0     // DB-2.3
	Integer flightTestSafetyEnvelopeOpenedPoints = 0    // DB-2.3
	Integer flightTestFrequencyNotMonitoredPoints = 0   // DB-2.3
    Integer flightTestForbiddenEquipmentPoints = 0      // DB-2.13
    Integer flightTestExitRoomTooLatePoints = 0         // DB-2.41
    Integer flightTestSubmissionMinutes                 // DB-2.20
	Integer flightTestOutsideCorridorCorrectSecond = 0  // DB-2.41
    Integer flightTestOutsideCorridorPointsPerSecond = 0 // DB-2.41
    
    // ObservationTest
    EnrouteValueUnit observationTestEnrouteValueUnit = EnrouteValueUnit.mm // DB-2.13
    Float observationTestEnrouteCorrectValue = 5.0f     // DB-2.13
    Float observationTestEnrouteInexactValue = 10.0f    // DB-2.13
    Integer observationTestEnrouteInexactPoints = 10    // DB-2.13
    Integer observationTestEnrouteNotFoundPoints = 20   // DB-2.13
    Integer observationTestEnrouteFalsePoints = 40      // DB-2.13
    Integer observationTestTurnpointNotFoundPoints = 40 // DB-2.13
    Integer observationTestTurnpointFalsePoints = 80    // DB-2.13
            
	// LandingTest
    String landingTest1LongRuleTitle = ""               // DB-2.42
    String landingTest1ShortRuleTitle = ""              // DB-2.42
    String landingTest1AirfieldImageNames = ""          // DB-2.42
	int landingTest1MaxPoints = 300                     // DB-2.0
	int landingTest1NoLandingPoints = 300               // DB-2.0
	int landingTest1OutsideLandingPoints = 200          // DB-2.0
	int landingTest1RollingOutsidePoints = 200          // DB-2.0
	int landingTest1PowerInBoxPoints = 50               // DB-2.0
	int landingTest1GoAroundWithoutTouchingPoints = 200 // DB-2.0
	int landingTest1GoAroundInsteadStopPoints = 200     // DB-2.0
	int landingTest1AbnormalLandingPoints = 150         // DB-2.0
	Integer landingTest1NotAllowedAerodynamicAuxiliariesPoints = 0// DB-2.7
	String landingTest1PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}" // DB-2.0
    String landingTest1PrintCalculatorValues = ""       // DB-2.42

    String landingTest2LongRuleTitle = ""               // DB-2.42
    String landingTest2ShortRuleTitle = ""              // DB-2.42
    String landingTest2AirfieldImageNames = ""          // DB-2.42
	int landingTest2MaxPoints = 300                     // DB-2.0
	int landingTest2NoLandingPoints = 300               // DB-2.0
	int landingTest2OutsideLandingPoints = 200          // DB-2.0
	int landingTest2RollingOutsidePoints = 200          // DB-2.0
	int landingTest2PowerInBoxPoints = 50               // DB-2.0
	int landingTest2GoAroundWithoutTouchingPoints = 200 // DB-2.0
	int landingTest2GoAroundInsteadStopPoints = 200     // DB-2.0
	int landingTest2AbnormalLandingPoints = 150         // DB-2.0
	Integer landingTest2NotAllowedAerodynamicAuxiliariesPoints = 0// DB-2.7
	int landingTest2PowerInAirPoints = 200              // DB-2.0
	String landingTest2PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}" // DB-2.0
    String landingTest2PrintCalculatorValues = ""       // DB-2.42
	
    String landingTest3LongRuleTitle = ""               // DB-2.42
    String landingTest3ShortRuleTitle = ""              // DB-2.42
    String landingTest3AirfieldImageNames = ""          // DB-2.42
	int landingTest3MaxPoints = 300                     // DB-2.0
	int landingTest3NoLandingPoints = 300               // DB-2.0
	int landingTest3OutsideLandingPoints = 200          // DB-2.0
	int landingTest3RollingOutsidePoints = 200          // DB-2.0
	int landingTest3PowerInBoxPoints = 50               // DB-2.0
	int landingTest3GoAroundWithoutTouchingPoints = 200 // DB-2.0
	int landingTest3GoAroundInsteadStopPoints = 200     // DB-2.0
	int landingTest3AbnormalLandingPoints = 150         // DB-2.0
	Integer landingTest3NotAllowedAerodynamicAuxiliariesPoints = 0// DB-2.7
	int landingTest3PowerInAirPoints = 200              // DB-2.0
	int landingTest3FlapsInAirPoints = 200              // DB-2.0
	String landingTest3PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}" // DB-2.0
    String landingTest3PrintCalculatorValues = ""       // DB-2.42
	
    String landingTest4LongRuleTitle = ""               // DB-2.42
    String landingTest4ShortRuleTitle = ""              // DB-2.42
    String landingTest4AirfieldImageNames = ""          // DB-2.42
	int landingTest4MaxPoints = 300                     // DB-2.0
	int landingTest4NoLandingPoints = 300               // DB-2.0
	int landingTest4OutsideLandingPoints = 200          // DB-2.0
	int landingTest4RollingOutsidePoints = 200          // DB-2.0
	int landingTest4PowerInBoxPoints = 50               // DB-2.0
	int landingTest4GoAroundWithoutTouchingPoints = 200 // DB-2.0
	int landingTest4GoAroundInsteadStopPoints = 200     // DB-2.0
	int landingTest4AbnormalLandingPoints = 150         // DB-2.0
	Integer landingTest4NotAllowedAerodynamicAuxiliariesPoints = 0// DB-2.7
	int landingTest4TouchingObstaclePoints = 400        // DB-2.0
	String landingTest4PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}" // DB-2.0
    String landingTest4PrintCalculatorValues = ""       // DB-2.42
	
    // Defaults
    Integer minRouteLegs = 0                            // DB-2.13
    Integer maxRouteLegs = 8                            // DB-2.13
    Float scGateWidth = 2.0f                            // DB-2.13
    String unsuitableStartNum = "13"                    // DB-2.13
    TurnpointRule turnpointRule = TurnpointRule.None    // DB-2.13
    Boolean turnpointMapMeasurement = false             // DB-2.13
    EnrouteRule enroutePhotoRule = EnrouteRule.None     // DB-2.13
    EnrouteRule enrouteCanvasRule = EnrouteRule.None    // DB-2.13
    Boolean enrouteCanvasMultiple = false               // DB-2.13
    Integer minEnroutePhotos = 0                        // DB-2.13
    Integer maxEnroutePhotos = 0                        // DB-2.13
    Integer minEnrouteCanvas = 0                        // DB-2.13
    Integer maxEnrouteCanvas = 0                        // DB-2.13
    Integer minEnrouteTargets = 0                       // DB-2.13
    Integer maxEnrouteTargets = 0                       // DB-2.13
    Boolean anrFlying = false                           // DB-2.41
    Integer flightTestLastGateNoBadCourseSeconds = 0    // DB-2.41
    Boolean showPlanningTest = false                    // DB-2.41
    Boolean activateFlightTestCheckLanding = false      // DB-2.41
    Boolean showObservationTest = false                 // DB-2.41
    
	// Crew print settings
	String printCrewPrintTitle = ""                     // DB-2.3
	Boolean printCrewNumber = true                      // DB-2.3
	Boolean printCrewName = true                        // DB-2.3
    Boolean printCrewEmail = false                      // DB-2.10
	Boolean printCrewTeam = true                        // DB-2.3
	Boolean printCrewClass = true                       // DB-2.3
	Boolean printCrewShortClass = false                 // DB-2.8
	Boolean printCrewAircraft = true                    // DB-2.3
	Boolean printCrewAircraftType = false               // DB-2.7
	Boolean printCrewAircraftColour = false             // DB-2.7
	Boolean printCrewTAS = true                         // DB-2.3
    Boolean printCrewTrackerID = false                  // DB-2.16
    Boolean printCrewUUID = false                       // DB-2.10
    Boolean printCrewSortHelp = false                   // DB-2.39
	Boolean printCrewEmptyColumn1 = false               // DB-2.3
	String printCrewEmptyTitle1 = ""                    // DB-2.3
	Boolean printCrewEmptyColumn2 = false               // DB-2.3
	String printCrewEmptyTitle2 = ""                    // DB-2.3
	Boolean printCrewEmptyColumn3 = false               // DB-2.3
	String printCrewEmptyTitle3 = ""                    // DB-2.3
	Boolean printCrewEmptyColumn4 = false               // DB-2.8
	String printCrewEmptyTitle4 = ""                    // DB-2.8
	Boolean printCrewLandscape = false                  // DB-2.3
	Boolean printCrewA3 = false                         // DB-2.3
    Integer printCrewOrder = 0                          // DB-2.9
	Boolean printTeams = false                          // DB-2.35
	Boolean printTeamLandscape = false                  // DB-2.35
	Boolean printAircraft = false                       // DB-2.35
	Boolean printAircraftLandscape = false              // DB-2.35
    
	// Points print settings
	String printPointsPrintTitle = ""                   // DB-2.3
    Boolean printPointsGeneral = true                   // DB-2.13
	Boolean printPointsPlanningTest = true              // DB-2.3
	Boolean printPointsFlightTest = true                // DB-2.3
    Boolean printPointsObservationTest = true           // DB-2.13
	Boolean printPointsLandingTest1 = true              // DB-2.3
	Boolean printPointsLandingTest2 = true              // DB-2.3
	Boolean printPointsLandingTest3 = true              // DB-2.3
	Boolean printPointsLandingTest4 = true              // DB-2.3
    Boolean printPointsLandingField = true              // DB-2.13
    String landingFieldImageName = ""                   // DB-2.13, UNUSED, since DB-2.42
    Boolean printPointsTurnpointSign = false            // DB-2.13
    Boolean printPointsEnrouteCanvas = false            // DB-2.13
    String printLandingCalculatorValues = ""            // DB-2.8, UNUSED, since DB-2.42
	Boolean printPointsZero = false                     // DB-2.3
	Boolean printPointsLandscape = false                // DB-2.3
	Boolean printPointsA3 = false                       // DB-2.3

    // FreeText
    String printFreeText = ""                           // DB-2.8
    String printFreeTextTitle = ""                      // DB-2.8
    Boolean printFreeTextLandscape = true               // DB-2.8
    Boolean printFreeTextA3 = false                     // DB-2.8
    String printFreeTextStyle = DEFAULT_FREETEXTSTYLE   // DB-2.8
    
    // print styles
	String printStyle = ""                              // DB-2.8
    Boolean flightPlanShowLegDistance = true            // DB-2.20
    Boolean flightPlanShowTrueTrack = true              // DB-2.20
    Boolean flightPlanShowTrueHeading = true            // DB-2.20
    Boolean flightPlanShowGroundSpeed = true            // DB-2.20
    Boolean flightPlanShowLocalTime = true              // DB-2.20
    Boolean flightPlanShowElapsedTime = false           // DB-2.20
    
	// Images
	byte[] imageLeft = null                             // DB-2.3
	Integer imageLeftHeight = IMAGEHEIGHT               // DB-2.3
	byte[] imageCenter = null                           // DB-2.3
	Integer imageCenterHeight = IMAGEHEIGHT             // DB-2.3
	byte[] imageRight = null                            // DB-2.3
	Integer imageRightHeight = IMAGEHEIGHT              // DB-2.3
	String titleSize = TITLESIZE                        // DB-2.3
	BigDecimal a3PortraitFactor = A3PORTRAITFACTOR      // DB-2.7
	BigDecimal a4LandscapeFactor = A4LANDSCAPEFACTOR    // DB-2.7
	BigDecimal a3LandscapeFactor = A3LANDSCAPEFACTOR    // DB-2.7
	Boolean imageBottomOn = false                       // DB-2.8
	byte[] imageBottomLeft = null                       // DB-2.8
	Integer imageBottomLeftHeight = IMAGEBOTTOMHEIGHT   // DB-2.8
	String imageBottomLeftText = ""                     // DB-2.8
	byte[] imageBottomRight = null                      // DB-2.8
	Integer imageBottomRightHeight = IMAGEBOTTOMHEIGHT  // DB-2.8
	String imageBottomRightText = ""                    // DB-2.8
	String imageBottomTextSize = IMAGEBOTTOMTEXTSIZE    // DB-2.8
    
    // Crew management
    String crewPilotNavigatorDelimiter = ","            // DB-2.16
    String crewSurnameForenameDelimiter = ""            // DB-2.16
    
    String reserve = ""                                 // DB-2.12
	
    // Live-Tracking
    Integer liveTrackingContestID = 0                   // DB-2.14
    String liveTrackingContestDate = ""                 // DB-2.15
    Boolean liveTrackingManagedCrews = false            // DB-2.15, UNUSED, since DB-2.24
    String liveTrackingScorecard = ""                   // DB-2.17
    String liveTrackingContestVisibility = Defs.LIVETRACKING_VISIBILITY_PRIVATE // DB-2.25
    
	// transient values
	static transients = ['copyContestSettings','copyRoutes','copyCrews','copyTaskSettings']
	boolean copyContestSettings = true
	boolean copyRoutes = true
	boolean copyCrews = true
	boolean copyTaskSettings = true
	
	static hasMany = [routes:Route, tasks:Task, crews:Crew, aircrafts:Aircraft, teams:Team, resultclasses:ResultClass, contestproperties:ContestProperty]
	
	static constraints = {
		title(blank:false)
		mapScale(blank:false, range:1..1000000000)
        timeZone(blank:false, validator:{ val, obj ->
			if (val.startsWith("-")) {
				if (val.size() > 6) {
					return false
				}
	            try {
	                Date t = Date.parse("HH:mm",val.substring(1))
	            } catch(Exception e) {
	                return false
	            }
			} else {
	            if (val.size() > 5) {
	                return false
	            }
	            try {
	                Date t = Date.parse("HH:mm",val)
	            } catch(Exception e) {
	                return false
	            }
			}
            return true
        })
		teamCrewNum(blank:false, min:0)
		
		planningTestDirectionCorrectGrad(blank:false, min:0)
		planningTestDirectionPointsPerGrad(blank:false, min:0)
		planningTestTimeCorrectSecond(blank:false, min:0)
		planningTestTimePointsPerSecond(blank:false, min:0)
		planningTestMaxPoints(blank:false, min:0)
		planningTestPlanTooLatePoints(blank:false, min:0)
		planningTestExitRoomTooLatePoints(blank:false, min:0)
		
	    flightTestTakeoffMissedPoints(blank:false, min:0)
	    flightTestCptimeCorrectSecond(blank:false, min:0)
	    flightTestCptimePointsPerSecond(blank:false, min:0)
	    flightTestCptimeMaxPoints(blank:false, min:0)
	    flightTestCpNotFoundPoints(blank:false, min:0)
	    flightTestProcedureTurnNotFlownPoints(blank:false, min:0)
	    flightTestMinAltitudeMissedPoints(blank:false, min:0)
	    flightTestBadCourseCorrectSecond(blank:false, min:0)
	    flightTestBadCoursePoints(blank:false, min:0)
	    flightTestBadCourseStartLandingPoints(blank:false, min:0)
	    flightTestLandingToLatePoints(blank:false, min:0)
	    flightTestGivenToLatePoints(blank:false, min:0)
		
		landingTest1MaxPoints(blank:false, min:0)
		landingTest1NoLandingPoints(blank:false, min:0)
		landingTest1OutsideLandingPoints(blank:false, min:0)
		landingTest1RollingOutsidePoints(blank:false, min:0)
		landingTest1PowerInBoxPoints(blank:false, min:0)
		landingTest1GoAroundWithoutTouchingPoints(blank:false, min:0)
		landingTest1GoAroundInsteadStopPoints(blank:false, min:0)
		landingTest1AbnormalLandingPoints(blank:false, min:0)
		landingTest1PenaltyCalculator(blank:false, size:0..4096)
		
		landingTest2MaxPoints(blank:false, min:0)
		landingTest2NoLandingPoints(blank:false, min:0)
		landingTest2OutsideLandingPoints(blank:false, min:0)
		landingTest2RollingOutsidePoints(blank:false, min:0)
		landingTest2PowerInBoxPoints(blank:false, min:0)
		landingTest2GoAroundWithoutTouchingPoints(blank:false, min:0)
		landingTest2GoAroundInsteadStopPoints(blank:false, min:0)
		landingTest2AbnormalLandingPoints(blank:false, min:0)
		landingTest2PowerInAirPoints(blank:false, min:0)
		landingTest2PenaltyCalculator(blank:false, size:0..4096)
		
		landingTest3MaxPoints(blank:false, min:0)
		landingTest3NoLandingPoints(blank:false, min:0)
		landingTest3OutsideLandingPoints(blank:false, min:0)
		landingTest3RollingOutsidePoints(blank:false, min:0)
		landingTest3PowerInBoxPoints(blank:false, min:0)
		landingTest3GoAroundWithoutTouchingPoints(blank:false, min:0)
		landingTest3GoAroundInsteadStopPoints(blank:false, min:0)
		landingTest3AbnormalLandingPoints(blank:false, min:0)
		landingTest3PowerInAirPoints(blank:false, min:0)
		landingTest3FlapsInAirPoints(blank:false, min:0)
		landingTest3PenaltyCalculator(blank:false, size:0..4096)
		
		landingTest4MaxPoints(blank:false, min:0)
		landingTest4NoLandingPoints(blank:false, min:0)
		landingTest4OutsideLandingPoints(blank:false, min:0)
		landingTest4RollingOutsidePoints(blank:false, min:0)
		landingTest4PowerInBoxPoints(blank:false, min:0)
		landingTest4GoAroundWithoutTouchingPoints(blank:false, min:0)
		landingTest4GoAroundInsteadStopPoints(blank:false, min:0)
		landingTest4AbnormalLandingPoints(blank:false, min:0)
		landingTest4TouchingObstaclePoints(blank:false, min:0)
		landingTest4PenaltyCalculator(blank:false, size:0..4096)
		
		// DB-2.1 compatibility
		contestClassResults(nullable:true)
		contestTaskResults(nullable:true)
		contestContestTitle(nullable:true)
		contestPrintLandscape(nullable:true)
		contestPrintTaskDetails(nullable:true)
		contestPrintTaskNamesInTitle(nullable:true)
		teamTaskResults(nullable:true)
		teamPrintLandscape(nullable:true)
		
		// DB-2.3 compatibility
		precisionFlying(nullable:true)
		bestOfAnalysisTaskNum(nullable:true,blank:false, min:0)
		contestPrintTitle(nullable:true)
		contestPrintSubtitle(nullable:true)
		contestPrintProvisional(nullable:true)
		contestPrintA3(nullable:true)
		teamPrintTitle(nullable:true)
		teamPrintSubtitle(nullable:true)
		teamPrintProvisional(nullable:true)
		teamPrintA3(nullable:true)
		printPrefix(nullable:true)
		contestTeamResults(nullable:true)
		printCrewPrintTitle(nullable:true)
		printCrewNumber(nullable:true)
		printCrewName(nullable:true)
		printCrewTeam(nullable:true)
		printCrewClass(nullable:true)
		printCrewAircraft(nullable:true)
		printCrewTAS(nullable:true)
		printCrewEmptyColumn1(nullable:true)
		printCrewEmptyTitle1(nullable:true)
		printCrewEmptyColumn2(nullable:true)
		printCrewEmptyTitle2(nullable:true)
		printCrewEmptyColumn3(nullable:true)
		printCrewEmptyTitle3(nullable:true)
		printCrewLandscape(nullable:true)
		printCrewA3(nullable:true)
		printPointsPrintTitle(nullable:true)
		printPointsPlanningTest(nullable:true)
		printPointsFlightTest(nullable:true)
		printPointsLandingTest1(nullable:true)
		printPointsLandingTest2(nullable:true)
		printPointsLandingTest3(nullable:true)
		printPointsLandingTest4(nullable:true)
		printPointsZero(nullable:true)
		printPointsLandscape(nullable:true)
		printPointsA3(nullable:true)
		imageLeft(nullable:true,maxSize:IMAGEMAXSIZE)
		imageLeftHeight(nullable:true)
		imageCenter(nullable:true,maxSize:IMAGEMAXSIZE)
		imageCenterHeight(nullable:true)
		imageRight(nullable:true,maxSize:IMAGEMAXSIZE)
		imageRightHeight(nullable:true)
		titleSize(nullable:true)
		flightTestTakeoffCorrectSecond(nullable:true, min:0)
		flightTestTakeoffCheckSeconds(nullable:true)
		flightTestTakeoffPointsPerSecond(nullable:true, min:0)
		flightTestSafetyAndRulesInfringementPoints(nullable:true, min:0)
		flightTestInstructionsNotFollowedPoints(nullable:true, min:0)
		flightTestFalseEnvelopeOpenedPoints(nullable:true, min:0)
		flightTestSafetyEnvelopeOpenedPoints(nullable:true, min:0)
		flightTestFrequencyNotMonitoredPoints(nullable:true, min:0)
		
		// DB-2.7 compatibility
		landingTest1NotAllowedAerodynamicAuxiliariesPoints(nullable:true, min:0)
		landingTest2NotAllowedAerodynamicAuxiliariesPoints(nullable:true, min:0)
		landingTest3NotAllowedAerodynamicAuxiliariesPoints(nullable:true, min:0)
		landingTest4NotAllowedAerodynamicAuxiliariesPoints(nullable:true, min:0)
		printCrewAircraftType(nullable:true)
		printCrewAircraftColour(nullable:true)
		a3PortraitFactor(nullable:true)
		a4LandscapeFactor(nullable:true)
		a3LandscapeFactor(nullable:true)
		
		// DB-2.8 compatibility
		printStyle(nullable:true,maxSize:PRINTSTYLESIZE)
		printCrewShortClass(nullable:true)
		imageBottomOn(nullable:true)
		imageBottomLeft(nullable:true,maxSize:IMAGEMAXSIZE)
		imageBottomLeftHeight(nullable:true)
		imageBottomLeftText(nullable:true)
		imageBottomRight(nullable:true,maxSize:IMAGEMAXSIZE)
		imageBottomRightHeight(nullable:true)
		imageBottomRightText(nullable:true)
		imageBottomTextSize(nullable:true)
		printOrganizer(blank:false,nullable:true)
		contestPrintAircraft(nullable:true)
		contestPrintTeam(nullable:true)
		contestPrintClass(nullable:true)
		contestPrintShortClass(nullable:true)
		printCrewEmptyColumn4(nullable:true)
		printCrewEmptyTitle4(nullable:true)
        contestPrintTaskTestDetails(nullable:true)
        contestPrintLandingDetails(nullable:true)
        liveRefreshSeconds(nullable:true,blank:false, min:0)
        liveStylesheet(nullable:true,blank:true)
        livePositionCalculation(nullable:true)
        liveShowSummary(nullable:true)
        contestPrintEqualPositions(nullable:true)
        teamPrintEqualPositions(nullable:true)
        printFreeText(nullable:true,maxSize:PRINTFREETEXTSIZE)
        printFreeTextTitle(nullable:true)
        printFreeTextLandscape(nullable:true)
        printFreeTextA3(nullable:true)
        printFreeTextStyle(nullable:true,maxSize:PRINTSTYLESIZE)
        contestRuleForEachClass(nullable:true)
        printLandingCalculatorValues(nullable:true)
        
        // DB-2.9 compatibility
        printCrewOrder(nullable:true)
        
        // DB-2.10 compatibility
        contestUUID(nullable:true)
        printCrewEmail(nullable:true)
        printCrewUUID(nullable:true)
        
        // DB-2.12 compatibility
        coordPresentation(nullable:true)
        reserve(nullable:true)
        
        // DB-2.13 compatibility
        planningTestForbiddenCalculatorsPoints(nullable:true, min:0)
        flightTestForbiddenEquipmentPoints(nullable:true, min:0)
        observationTestEnrouteValueUnit(nullable:true)
        observationTestEnrouteCorrectValue(nullable:true, min:0.0f)
        observationTestEnrouteInexactValue(nullable:true, min:0.0f)
        observationTestEnrouteInexactPoints(nullable:true, min:0)
        observationTestEnrouteNotFoundPoints(nullable:true, min:0)
        observationTestEnrouteFalsePoints(nullable:true, min:0)
        observationTestTurnpointNotFoundPoints(nullable:true, min:0)
        observationTestTurnpointFalsePoints(nullable:true, min:0)
        increaseFactor(nullable:true, min:0, max:1000)
        minRouteLegs(nullable:true, min:0, max:100)
        maxRouteLegs(nullable:true, min:0, max:100)
        scGateWidth(nullable:true, min:0.0f, max:100.0f)
        unsuitableStartNum(nullable:true)
        turnpointRule(nullable:true)
        turnpointMapMeasurement(nullable:true)
        enroutePhotoRule(nullable:true)
        enrouteCanvasRule(nullable:true)
        enrouteCanvasMultiple(nullable:true)
        minEnroutePhotos(nullable:true, min:0, max:100)
        maxEnroutePhotos(nullable:true, min:0, max:100)
        minEnrouteCanvas(nullable:true, min:0, max:100)
        maxEnrouteCanvas(nullable:true, min:0, max:100)
        minEnrouteTargets(nullable:true, min:0, max:100)
        maxEnrouteTargets(nullable:true, min:0, max:100)
        printPointsGeneral(nullable:true)
        printPointsObservationTest(nullable:true)
        contestPrintObservationDetails(nullable:true)
        printPointsLandingField(nullable:true)
        landingFieldImageName(nullable:true)
        printPointsTurnpointSign(nullable:true)
        printPointsEnrouteCanvas(nullable:true)
        
        // DB-2.14 compatibility
        liveTrackingContestID(nullable:true)
		
		// DB-2.15 compatibility
		liveTrackingManagedCrews(nullable:true)
        liveTrackingContestDate(nullable:true, validator:{ val, obj ->
            if (val && val.size()) {
                if (val.size() != 10) {
                    return false
                }
                try {
                    Date t = Date.parse("yyyy-MM-dd",val)
                } catch(Exception e) {
                    return false
                }
			}
			return true
		})
        
		// DB-2.16 compatibility
        printCrewTrackerID(nullable:true)
        crewPilotNavigatorDelimiter(nullable:true, size:0..1)
        crewSurnameForenameDelimiter(nullable:true, size:0..1)
        
        // DB-2.17 compatibility
        liveTrackingScorecard(nullable:true)
        flightTestBadCourseMaxPoints(nullable:true, blank:false, min:0)
        
        // DB-2.18 compatibility
        useProcedureTurns(nullable:true)
        
        // DB-2.20 compatibility
        flightPlanShowLegDistance(nullable:true)
        flightPlanShowTrueTrack(nullable:true)
        flightPlanShowTrueHeading(nullable:true)
        flightPlanShowGroundSpeed(nullable:true)
        flightPlanShowLocalTime(nullable:true)
        flightPlanShowElapsedTime(nullable:true)
        flightTestSubmissionMinutes(nullable:true, min:0)
        contestLandingResultsFactor(nullable:true, min:0.0, max:1.0)
        
        // DB-2.21 compatibility
        ruleTitle(nullable:true)
        timeZone2(nullable:true)
        
        // DB-2.25 compatibility
        liveTrackingContestVisibility(nullable:true)

		// DB-2.35 compatibility
		contestPrintFooter(nullable:true)
		teamPrintFooter(nullable:true)
		printTeams(nullable:true)
		printTeamLandscape(nullable:true)
		printAircraft(nullable:true)
		printAircraftLandscape(nullable:true)
        
        // DB-2.36 compatibility
        liveShowSize(nullable:true,min:1)
        liveNewestShowSize(nullable:true,min:0)

        // DB-2.39 compatibility
        printCrewSortHelp(nullable:true)
        
        // DB-2.41 compatibility
        anrFlying(nullable:true)
        flightTestBadCourseStartLandingSeparatePoints(nullable:true)
        flightTestOutsideCorridorCorrectSecond(nullable:true,min:0)
        flightTestOutsideCorridorPointsPerSecond(nullable:true,min:0)
        flightTestExitRoomTooLatePoints(nullable:true,min:0)
        flightTestLastGateNoBadCourseSeconds(nullable:true,min:0)
        showPlanningTest(nullable:true)
        activateFlightTestCheckLanding(nullable:true)
        showObservationTest(nullable:true)

        // DB-2.42 compatibility
        landingTest1LongRuleTitle(nullable:true)
        landingTest1ShortRuleTitle(nullable:true)
        landingTest1AirfieldImageNames(nullable:true)
        landingTest1PrintCalculatorValues(nullable:true)
        landingTest2LongRuleTitle(nullable:true)
        landingTest2ShortRuleTitle(nullable:true)
        landingTest2AirfieldImageNames(nullable:true)
        landingTest2PrintCalculatorValues(nullable:true)
        landingTest3LongRuleTitle(nullable:true)
        landingTest3ShortRuleTitle(nullable:true)
        landingTest3AirfieldImageNames(nullable:true)
        landingTest3PrintCalculatorValues(nullable:true)
        landingTest4LongRuleTitle(nullable:true)
        landingTest4ShortRuleTitle(nullable:true)
        landingTest4AirfieldImageNames(nullable:true)
        landingTest4PrintCalculatorValues(nullable:true)
    }

    static mapping = {
        routes sort:"id"
		tasks sort:"id"
		crews sort:"id"
		aircrafts sort:"id"
		teams sort:"id"
		resultclasses sort:"id"
        contestproperties sort:"id"
	}

	void CopyValues(Contest contestInstance)
	{
		if (contestInstance) {
			if (copyContestSettings) {
				mapScale = contestInstance.mapScale
                timeZone2 = contestInstance.timeZone2
                liveTrackingContestDate = contestInstance.liveTrackingContestDate
				timeZone = contestInstance.timeZone
				resultClasses = contestInstance.resultClasses
                teamCrewNum = contestInstance.teamCrewNum
                bestOfAnalysisTaskNum = contestInstance.bestOfAnalysisTaskNum

                contestRule = contestInstance.contestRule
                contestRuleForEachClass = contestInstance.contestRuleForEachClass
                ruleTitle = contestInstance.ruleTitle
                printOrganizer = contestInstance.printOrganizer
                
				precisionFlying = contestInstance.precisionFlying
                increaseFactor = contestInstance.increaseFactor
				
				planningTestDirectionCorrectGrad = contestInstance.planningTestDirectionCorrectGrad 
				planningTestDirectionPointsPerGrad = contestInstance.planningTestDirectionPointsPerGrad
				planningTestTimeCorrectSecond = contestInstance.planningTestTimeCorrectSecond
				planningTestTimePointsPerSecond = contestInstance.planningTestTimePointsPerSecond
				planningTestMaxPoints = contestInstance.planningTestMaxPoints
				planningTestPlanTooLatePoints = contestInstance.planningTestPlanTooLatePoints
				planningTestExitRoomTooLatePoints = contestInstance.planningTestExitRoomTooLatePoints
                planningTestForbiddenCalculatorsPoints = contestInstance.planningTestForbiddenCalculatorsPoints
                
				flightTestTakeoffMissedPoints = contestInstance.flightTestTakeoffMissedPoints
				flightTestTakeoffCorrectSecond = contestInstance.flightTestTakeoffCorrectSecond
				flightTestTakeoffCheckSeconds = contestInstance.flightTestTakeoffCheckSeconds
				flightTestTakeoffPointsPerSecond = contestInstance.flightTestTakeoffPointsPerSecond
				flightTestCptimeCorrectSecond = contestInstance.flightTestCptimeCorrectSecond
				flightTestCptimePointsPerSecond = contestInstance.flightTestCptimePointsPerSecond
				flightTestCptimeMaxPoints = contestInstance.flightTestCptimeMaxPoints
				flightTestCpNotFoundPoints = contestInstance.flightTestCpNotFoundPoints
				flightTestProcedureTurnNotFlownPoints = contestInstance.flightTestProcedureTurnNotFlownPoints
				flightTestMinAltitudeMissedPoints = contestInstance.flightTestMinAltitudeMissedPoints
				flightTestBadCourseCorrectSecond = contestInstance.flightTestBadCourseCorrectSecond
				flightTestBadCoursePoints = contestInstance.flightTestBadCoursePoints
                flightTestBadCourseMaxPoints = contestInstance.flightTestBadCourseMaxPoints
				flightTestBadCourseStartLandingPoints = contestInstance.flightTestBadCourseStartLandingPoints
                flightTestBadCourseStartLandingSeparatePoints = contestInstance.flightTestBadCourseStartLandingSeparatePoints
				flightTestLandingToLatePoints = contestInstance.flightTestLandingToLatePoints
				flightTestGivenToLatePoints = contestInstance.flightTestGivenToLatePoints
				flightTestSafetyAndRulesInfringementPoints = contestInstance.flightTestSafetyAndRulesInfringementPoints
				flightTestInstructionsNotFollowedPoints = contestInstance.flightTestInstructionsNotFollowedPoints
				flightTestFalseEnvelopeOpenedPoints = contestInstance.flightTestFalseEnvelopeOpenedPoints
				flightTestSafetyEnvelopeOpenedPoints = contestInstance.flightTestSafetyEnvelopeOpenedPoints
				flightTestFrequencyNotMonitoredPoints = contestInstance.flightTestFrequencyNotMonitoredPoints
                flightTestForbiddenEquipmentPoints = contestInstance.flightTestForbiddenEquipmentPoints
                flightTestExitRoomTooLatePoints = contestInstance.flightTestExitRoomTooLatePoints
                flightTestOutsideCorridorCorrectSecond = contestInstance.flightTestOutsideCorridorCorrectSecond
                flightTestOutsideCorridorPointsPerSecond = contestInstance.flightTestOutsideCorridorPointsPerSecond
                
                observationTestEnrouteValueUnit = contestInstance.observationTestEnrouteValueUnit
                observationTestEnrouteCorrectValue = contestInstance.observationTestEnrouteCorrectValue
                observationTestEnrouteInexactValue = contestInstance.observationTestEnrouteInexactValue
                observationTestEnrouteInexactPoints = contestInstance.observationTestEnrouteInexactPoints
                observationTestEnrouteNotFoundPoints = contestInstance.observationTestEnrouteNotFoundPoints
                observationTestEnrouteFalsePoints = contestInstance.observationTestEnrouteFalsePoints
                observationTestTurnpointNotFoundPoints = contestInstance.observationTestTurnpointNotFoundPoints
                observationTestTurnpointFalsePoints = contestInstance.observationTestTurnpointFalsePoints
				
                landingTest1LongRuleTitle = contestInstance.landingTest1LongRuleTitle
                landingTest1ShortRuleTitle = contestInstance.landingTest1ShortRuleTitle
                landingTest1AirfieldImageNames = contestInstance.landingTest1AirfieldImageNames
				landingTest1MaxPoints = contestInstance.landingTest1MaxPoints
				landingTest1NoLandingPoints = contestInstance.landingTest1NoLandingPoints
				landingTest1OutsideLandingPoints = contestInstance.landingTest1OutsideLandingPoints
				landingTest1RollingOutsidePoints = contestInstance.landingTest1RollingOutsidePoints
				landingTest1PowerInBoxPoints = contestInstance.landingTest1PowerInBoxPoints
				landingTest1GoAroundWithoutTouchingPoints = contestInstance.landingTest1GoAroundWithoutTouchingPoints
				landingTest1GoAroundInsteadStopPoints = contestInstance.landingTest1GoAroundInsteadStopPoints
				landingTest1AbnormalLandingPoints = contestInstance.landingTest1AbnormalLandingPoints
				landingTest1NotAllowedAerodynamicAuxiliariesPoints = contestInstance.landingTest1NotAllowedAerodynamicAuxiliariesPoints
				landingTest1PenaltyCalculator = contestInstance.landingTest1PenaltyCalculator
                landingTest1PrintCalculatorValues = contestInstance.landingTest1PrintCalculatorValues
				
                landingTest2LongRuleTitle = contestInstance.landingTest2LongRuleTitle
                landingTest2ShortRuleTitle = contestInstance.landingTest2ShortRuleTitle
                landingTest2AirfieldImageNames = contestInstance.landingTest2AirfieldImageNames
				landingTest2MaxPoints = contestInstance.landingTest2MaxPoints
				landingTest2NoLandingPoints = contestInstance.landingTest2NoLandingPoints
				landingTest2OutsideLandingPoints = contestInstance.landingTest2OutsideLandingPoints
				landingTest2RollingOutsidePoints = contestInstance.landingTest2RollingOutsidePoints
				landingTest2PowerInBoxPoints = contestInstance.landingTest2PowerInBoxPoints
				landingTest2GoAroundWithoutTouchingPoints = contestInstance.landingTest2GoAroundWithoutTouchingPoints
				landingTest2GoAroundInsteadStopPoints = contestInstance.landingTest2GoAroundInsteadStopPoints
				landingTest2AbnormalLandingPoints = contestInstance.landingTest2AbnormalLandingPoints
				landingTest2NotAllowedAerodynamicAuxiliariesPoints = contestInstance.landingTest2NotAllowedAerodynamicAuxiliariesPoints
				landingTest2PowerInAirPoints = contestInstance.landingTest2PowerInAirPoints
				landingTest2PenaltyCalculator = contestInstance.landingTest2PenaltyCalculator
                landingTest2PrintCalculatorValues = contestInstance.landingTest2PrintCalculatorValues
				
                landingTest3LongRuleTitle = contestInstance.landingTest3LongRuleTitle
                landingTest3ShortRuleTitle = contestInstance.landingTest3ShortRuleTitle
                landingTest3AirfieldImageNames = contestInstance.landingTest3AirfieldImageNames
				landingTest3MaxPoints = contestInstance.landingTest3MaxPoints
				landingTest3NoLandingPoints = contestInstance.landingTest3NoLandingPoints
				landingTest3OutsideLandingPoints = contestInstance.landingTest3OutsideLandingPoints
				landingTest3RollingOutsidePoints = contestInstance.landingTest3RollingOutsidePoints
				landingTest3PowerInBoxPoints = contestInstance.landingTest3PowerInBoxPoints
				landingTest3GoAroundWithoutTouchingPoints = contestInstance.landingTest3GoAroundWithoutTouchingPoints
				landingTest3GoAroundInsteadStopPoints = contestInstance.landingTest3GoAroundInsteadStopPoints
				landingTest3AbnormalLandingPoints = contestInstance.landingTest3AbnormalLandingPoints
				landingTest3NotAllowedAerodynamicAuxiliariesPoints = contestInstance.landingTest3NotAllowedAerodynamicAuxiliariesPoints
				landingTest3PowerInAirPoints = contestInstance.landingTest3PowerInAirPoints
				landingTest3FlapsInAirPoints = contestInstance.landingTest3FlapsInAirPoints
				landingTest3PenaltyCalculator = contestInstance.landingTest3PenaltyCalculator
                landingTest3PrintCalculatorValues = contestInstance.landingTest3PrintCalculatorValues
				
                landingTest4LongRuleTitle = contestInstance.landingTest4LongRuleTitle
                landingTest4ShortRuleTitle = contestInstance.landingTest4ShortRuleTitle
                landingTest4AirfieldImageNames = contestInstance.landingTest4AirfieldImageNames
				landingTest4MaxPoints = contestInstance.landingTest4MaxPoints
				landingTest4NoLandingPoints = contestInstance.landingTest4NoLandingPoints
				landingTest4OutsideLandingPoints = contestInstance.landingTest4OutsideLandingPoints
				landingTest4RollingOutsidePoints = contestInstance.landingTest4RollingOutsidePoints
				landingTest4PowerInBoxPoints = contestInstance.landingTest4PowerInBoxPoints
				landingTest4GoAroundWithoutTouchingPoints = contestInstance.landingTest4GoAroundWithoutTouchingPoints
				landingTest4GoAroundInsteadStopPoints = contestInstance.landingTest4GoAroundInsteadStopPoints
				landingTest4AbnormalLandingPoints = contestInstance.landingTest4AbnormalLandingPoints
				landingTest4NotAllowedAerodynamicAuxiliariesPoints = contestInstance.landingTest4NotAllowedAerodynamicAuxiliariesPoints
				landingTest4TouchingObstaclePoints = contestInstance.landingTest4TouchingObstaclePoints
				landingTest4PenaltyCalculator = contestInstance.landingTest4PenaltyCalculator
                landingTest4PrintCalculatorValues = contestInstance.landingTest4PrintCalculatorValues
                
                minRouteLegs = contestInstance.minRouteLegs
                maxRouteLegs = contestInstance.maxRouteLegs
                scGateWidth = contestInstance.scGateWidth
                unsuitableStartNum = contestInstance.unsuitableStartNum
                turnpointRule = contestInstance.turnpointRule
                turnpointMapMeasurement = contestInstance.turnpointMapMeasurement
                enroutePhotoRule = contestInstance.enroutePhotoRule
                enrouteCanvasRule = contestInstance.enrouteCanvasRule
                enrouteCanvasMultiple = contestInstance.enrouteCanvasMultiple
                minEnroutePhotos = contestInstance.minEnroutePhotos
                maxEnroutePhotos = contestInstance.maxEnroutePhotos
                minEnrouteCanvas = contestInstance.minEnrouteCanvas
                maxEnrouteCanvas = contestInstance.maxEnrouteCanvas
                minEnrouteTargets = contestInstance.minEnrouteTargets
                maxEnrouteTargets = contestInstance.maxEnrouteTargets
                useProcedureTurns = contestInstance.useProcedureTurns
                liveTrackingScorecard = contestInstance.liveTrackingScorecard

				anrFlying = contestInstance.anrFlying
                flightTestLastGateNoBadCourseSeconds = contestInstance.flightTestLastGateNoBadCourseSeconds
                showPlanningTest = contestInstance.showPlanningTest
                activateFlightTestCheckLanding = contestInstance.activateFlightTestCheckLanding
                showObservationTest = contestInstance.showObservationTest
                
                if (copyCrews) {
                    crewPilotNavigatorDelimiter = contestInstance.crewPilotNavigatorDelimiter
                    crewSurnameForenameDelimiter = contestInstance.crewSurnameForenameDelimiter
                }
			} 
			
			if (!this.save()) {
				throw new Exception("Contest.CopyValues could not save")
			}
			
			if (copyContestSettings) { // contestproperties:ContestProperty
				ContestProperty.findAllByContest(contestInstance,[sort:"id"]).each { ContestProperty contest_property_instance ->
					ContestProperty new_contest_property_instance = new ContestProperty()
					new_contest_property_instance.contest = this
					new_contest_property_instance.CopyValues(contest_property_instance)
					new_contest_property_instance.save()
				}
            }
            
			if (copyRoutes) { // routes:Route
				Route.findAllByContest(contestInstance,[sort:"idTitle"]).each { Route route_instance ->
					Route new_route_instance = new Route()
					new_route_instance.contest = this
					new_route_instance.CopyValues(route_instance)
					new_route_instance.save()
				}
			}
			
			if (copyCrews) {
				// teams:Team
				Team.findAllByContest(contestInstance,[sort:"id"]).each { Team team_instance ->
					Team new_team_instance = new Team()
					new_team_instance.contest = this
					new_team_instance.CopyValues(team_instance)
					new_team_instance.save()
				}
			
				// resultclasses:ResultClass
				ResultClass.findAllByContest(contestInstance,[sort:"id"]).each { ResultClass resultclass_instance ->
					ResultClass new_resultclass_instance = new ResultClass()
					new_resultclass_instance.contest = this
					new_resultclass_instance.CopyValues(resultclass_instance)
					new_resultclass_instance.save()
				}

				// aircrafts:Aircraft
				Aircraft.findAllByContest(contestInstance,[sort:"id"]).each { Aircraft aircraft_instance ->
					Aircraft new_aircraft_instance = new Aircraft()
					new_aircraft_instance.contest = this
					new_aircraft_instance.CopyValues(aircraft_instance)
					new_aircraft_instance.save()
				}
				
				// crews:Crew
				Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
					Crew new_crew_instance = new Crew()
					new_crew_instance.contest = this
					new_crew_instance.CopyValues(crew_instance)
					new_crew_instance.save()
				}
			}
			
			if (copyTaskSettings) { // tasks:Task
				Task.findAllByContest(contestInstance,[sort:"idTitle"]).each { Task task_instance ->
					Task new_task_instance = new Task()
					new_task_instance.contest = this
					new_task_instance.CopyValues(task_instance)
					new_task_instance.save()
				}
			}
		}
	}
	
	String idName()
	{
		return "${getMsg('fc.contest')}-${id}"
	}
	
	String name()
	{
		if(title) {
			return title
		} else {
            return idName()
		}
	}
		
	boolean IsPlanningTestRun()
	{
		for (Task task_instance in Task.findAllByContest(this,[sort:"idTitle"])) {
			if (task_instance.IsPlanningTestRun()) {
				return true
			}
		}
		return false
	}
	
	boolean IsFlightTestRun()
	{
		for (Task task_instance in Task.findAllByContest(this,[sort:"idTitle"])) {
			if (task_instance.IsFlightTestRun()) {
				return true
			}
		}
		return false
	}
	
	boolean IsObservationTestRun()
	{
		for (Task task_instance in Task.findAllByContest(this,[sort:"idTitle"])) {
			if (task_instance.IsObservationTestRun()) {
				return true
			}
		}
		return false
	}
	
	boolean IsLandingTestRun()
	{
		for (Task task_instance in Task.findAllByContest(this,[sort:"idTitle"])) {
			if (task_instance.IsLandingTestRun()) {
				return true
			}
		}
		return false
	}
	
	boolean IsSpecialTestRun()
	{
		for (Task task_instance in Task.findAllByContest(this,[sort:"idTitle"])) {
			if (task_instance.IsSpecialTestRun()) {
				return true
			}
		}
		return false
	}
	
	String GetPrintContestTitle(ResultFilter resultFilter)
	{
		switch (resultFilter) {
			case ResultFilter.Contest:
				if (contestContestTitle > 1) {
					int i = 1
					for (ResultClass resultclass_instance in ResultClass.findAllByContest(this,[sort:"id"])) {
						if (resultclass_instance.contestTitle) {
							i++
							if (i == contestContestTitle) {
								return resultclass_instance.contestTitle
							}
						}
					}
					i++
					if (i == contestContestTitle) {
						if (contestPrintTitle) {
							return contestPrintTitle
						} else {
							return "-"
						}
					}
				}
				return title
			case ResultFilter.Team:
				if (teamContestTitle > 1) {
					int i = 1
					for (ResultClass resultclass_instance in ResultClass.findAllByContest(this,[sort:"id"])) {
						if (resultclass_instance.contestTitle) {
							i++
							if (i == teamContestTitle) {
								return resultclass_instance.contestTitle
							}
						}
					}
					i++
					if (i == teamContestTitle) {
						if (teamPrintTitle) {
							return teamPrintTitle
						} else {
							return "-"
						}
					}
				}
				return title
		}
	}
	
	String GetListTitle(ResultFilter resultFilter, String msgID)
	{
		switch (resultFilter) {
			case ResultFilter.Contest:
				if (contestContestTitle > 1) {
					int i = 1
					for (ResultClass resultclass_instance in ResultClass.findAllByContest(this,[sort:"id"])) {
						if (resultclass_instance.contestTitle) {
							i++
							if (i == contestContestTitle) {
								return "${getMsg(msgID)} - ${resultclass_instance.contestTitle}"
							}
						}
					}
					i++
					if (i == contestContestTitle) {
						return "${getMsg(msgID)} - ${contestPrintTitle}"
					}
				}
				return "${getMsg(msgID)} - ${title}"
			case ResultFilter.Team:
				if (teamContestTitle > 1) {
					int i = 1
					for (ResultClass resultclass_instance in ResultClass.findAllByContest(this,[sort:"id"])) {
						if (resultclass_instance.contestTitle) {
							i++
							if (i == teamContestTitle) {
								return "${getMsg(msgID)} - ${resultclass_instance.contestTitle}"
							}
						}
					}
					i++
					if (i == teamContestTitle) {
						return "${getMsg(msgID)} - ${teamPrintTitle}"
					}
				}
				return "${getMsg(msgID)} - ${title}"
		}
	}
	
	String GetResultTitle(Map resultSettings, boolean isPrint)
	{
		String ret = ""
		if (resultSettings["Planning"]) {
			if (isPrint) {
				ret += getPrintMsg('fc.planningresults.planning')
			} else {
				ret += getMsg('fc.planningresults.planning')
			}
		}
		if (resultSettings["Flight"]) {
			if (ret) {
				ret += ", "
			} 
			if (isPrint) {
				ret += getPrintMsg('fc.flightresults.flight')
			} else {
				ret += getMsg('fc.flightresults.flight')
			}
		}
		if (resultSettings["Observation"]) {
			if (ret) {
				ret += ", "
			} 
			if (isPrint) {
				ret += getPrintMsg('fc.observationresults.observations')
			} else {
				ret += getMsg('fc.observationresults.observations')
			}
		}
		if (resultSettings["Landing"]) {
			if (ret) {
				ret += ", "
			} 
			if (isPrint) {
				ret += getPrintMsg('fc.landingresults.landing')
			} else {
				ret += getMsg('fc.landingresults.landing')
			}
		}
		if (resultSettings["Special"]) {
			if (ret) {
				ret += ", "
			} 
			if (isPrint) {
				ret += getPrintMsg('fc.specialresults.other')
			} else {
				ret += getMsg('fc.specialresults.other')
			}
		}
		if (!ret) {
			if (isPrint) {
				ret = getPrintMsg('fc.results.none')
			} else {
				ret = getMsg('fc.results.none')
			}
		}
		if (resultSettings["Tasks"]) {
			ret = "${resultSettings["Tasks"]} - $ret"
		}
		return ret
	}
	
	boolean IsTeamResultsProvisional(Map resultSettings, String resultTaskIDs)
	{
		if (teamPrintProvisional) {
			return true
		}
        for (Team team_instance in Team.findAllByContest(this,[sort:'contestPosition'])) {
			if (team_instance.IsActiveTeam()) {
                int crew_num = 0
            	for ( Crew crew_instance in Crew.findAllByTeamAndDisabledAndDisabledTeam(team_instance,false,false,[sort:'teamPenalties'])) {
            		if (crew_instance.IsActiveCrew(ResultFilter.Team) && (crew_num < teamCrewNum)) {
            			crew_num++
                        for ( Task task_instance in GetResultTasks(resultTaskIDs)) {
                        	Test test_instance = Test.findByCrewAndTask(crew_instance,task_instance)
							if (test_instance) {
	                        	if (test_instance.IsTestResultsProvisional(resultSettings)) {
									return true
	                        	}
							}
                        }
            		}
            	}
			}
        }
		return false
	}
	
	Map GetTeamResultSettings()
	{
		Map ret = [:]
		if (teamPlanningResults && IsPlanningTestRun()) {
			ret += [Planning:true]
		}
		if (teamFlightResults && IsFlightTestRun()) {
			ret += [Flight:true]
		}
		if (teamObservationResults && IsObservationTestRun()) {
			ret += [Observation:true]
		}
		if (teamLandingResults && IsLandingTestRun()) {
			ret += [Landing:true]
		}
		if (teamSpecialResults && IsSpecialTestRun()) {
			ret += [Special:true]
		}
		return ret
	}
	
	boolean IsContestResultsProvisional(Map resultSettings, String resultTaskIDs)
	{
		if (contestPrintProvisional) {
			return true
		}
	    for (Crew crew_instance in Crew.findAllByContestAndDisabled(this,false,[sort:'contestPosition'])) {
			if (!crew_instance.disabled && !crew_instance.noContestPosition) {
	            for (Task task_instance in GetResultTasks(resultTaskIDs)) {
	            	Test test_instance = Test.findByCrewAndTask(crew_instance,task_instance)
					if (test_instance && !test_instance.disabledCrew) {
		            	if (test_instance.IsTestResultsProvisional(resultSettings)) {
							return true
						}
					}
	            }
			}
	    }
		return false
	}
	
	Map GetResultSettings(boolean isPrint = false)
	{
		Map ret = [:]
		if (contestPrintTaskNamesInTitle) {
			String task_names = ""
			for (Task task_instance in GetResultTasks(contestTaskResults)) {
				if (task_names) {
					task_names += ", "
				}
				if (isPrint) {
					task_names += task_instance.printName()
				} else {
					task_names += task_instance.name()
				}
			}
			if (task_names) {
				ret += [Tasks:task_names]
			}
		}
		if (contestPlanningResults && IsPlanningTestRun()) {
			ret += [Planning:true]
		}
		if (contestFlightResults && IsFlightTestRun()) {
			ret += [Flight:true]
		}
		if (contestObservationResults && IsObservationTestRun()) {
			ret += [Observation:true]
		}
		if (contestLandingResults && IsLandingTestRun()) {
			ret += [Landing:true]
		}
		if (contestSpecialResults && IsSpecialTestRun()) {
			ret += [Special:true]
		}
		return ret
	}
	
    List GetResultClasses(String resultClassIDs)
    {
        List ret = []
        if (resultClasses) {
            String resultclass_ids = "$resultClassIDs,"
            for (ResultClass resultclass_instance in ResultClass.findAllByContest(this,[sort:"id"])) {
                if (resultclass_ids.contains("resultclass_${resultclass_instance.id},")) {
                    ret += resultclass_instance
                }
            }
        }
        return ret
    }
    
	List GetResultTasks(String resultTaskIDs)
	{
		List ret = []
		String task_ids = "$resultTaskIDs,"
		for (Task task_instance in Task.findAllByContest(this,[sort:"idTitle"])) {
			if (task_ids.contains("task_${task_instance.id},")) {
				ret += task_instance
			}
		}
		return ret
	}
	
    List GetResultTaskIDs(String resultTaskIDs)
    {
        List ret = []
        String task_ids = "$resultTaskIDs,"
        for (Task task_instance in Task.findAllByContest(this,[sort:"idTitle"])) {
            if (task_ids.contains("task_${task_instance.id},")) {
                ret += task_instance.id
            }
        }
        return ret
    }
    
    List GetTestDetailsTasks(String testDetailsTaskIDs)
    {
        List ret = []
        String task_ids = "$testDetailsTaskIDs,"
        for (Task task_instance in Task.findAllByContest(this,[sort:"idTitle"])) {
            if (task_ids.contains("tasktestdetails_${task_instance.id},")) {
                ret += task_instance
            }
        }
        return ret
    }
    
    List GetTestDetailsTasksIDs(String testDetailsTaskIDs)
    {
        List ret = []
        String task_ids = "$testDetailsTaskIDs,"
        for (Task task_instance in Task.findAllByContest(this,[sort:"idTitle"])) {
            if (task_ids.contains("tasktestdetails_${task_instance.id},")) {
                ret += task_instance.id
            }
        }
        return ret
    }
    
	List GetResultTeams(String resultTeamIDs)
	{
		List ret = []
        String team_ids = "$resultTeamIDs,"
        if (team_ids.contains("team_all_teams,")) {
            for (Team team_instance in Team.findAllByContest(this,[sort:"id"])) {
                ret += team_instance
            }
        } else {
    		for (Team team_instance in Team.findAllByContest(this,[sort:"id"])) {
    			if (team_ids.contains("team_${team_instance.id},")) {
    				ret += team_instance
    			}
    		}
        }
        if (team_ids.contains("team_no_team_crew,")) {
            ret += null
        }
		return ret
	}
	
    List GetResultTeamIDs(String resultTeamIDs)
    {
        List ret = []
        String team_ids = "$resultTeamIDs,"
        if (team_ids.contains("team_all_teams,")) {
            for (Team team_instance in Team.findAllByContest(this,[sort:"id"])) {
                ret += team_instance.id
            }
        } else {
            for (Team team_instance in Team.findAllByContest(this,[sort:"id"])) {
                if (team_ids.contains("team_${team_instance.id},")) {
                    ret += team_instance.id
                }
            }
        }
        if (team_ids.contains("team_no_team_crew,")) {
            ret += null
        }
        return ret
    }
    
    String GetResultClassNames(String resultClassIDs)
    {
        String s = ""
        if (resultClasses) {
            String resultclass_ids = "$resultClassIDs,"
            for (ResultClass resultclass_instance in ResultClass.findAllByContest(this,[sort:"id"])) {
                if (resultclass_ids.contains("resultclass_${resultclass_instance.id},")) {
                    if (s) {
                        s += ","
                    }
                    s += resultclass_instance.name
                }
            }
        }
        return s
    }
    
    String GetResultTaskNames(String resultTaskIDs)
    {
        String s = ""
        String task_ids = "$resultTaskIDs,"
        for (Task task_instance in Task.findAllByContest(this,[sort:"idTitle"])) {
            if (task_ids.contains("task_${task_instance.id},")) {
                if (s) {
                    s += ","
                }
                s += task_instance.name()
            }
        }
        return s
    }
    
    String GetResultTeamNames(String resultTeamIDs)
    {
        String s = ""
        String team_ids = "$resultTeamIDs,"
        for (Team team_instance in Team.findAllByContest(this,[sort:"id"])) {
            if (team_ids.contains("team_${team_instance.id},")) {
                if (s) {
                    s += ","
                }
                s += team_instance.name
            } 
        }
        if (team_ids.contains("team_no_team_crew,")) {
            if (s) {
                s += ","
            }
            s += "<Kein Team>"
        }
        if (team_ids.contains("team_all_teams,")) {
            if (s) {
                s += ","
            }
            s += "<Alle Teams>"
        }
        return s
    }
    
	String GetPrintPrefix()
	{
		String prefix = "" 
		if (printPrefix) {
			prefix = printPrefix.replaceAll(' ', '')
		} else {
			String title2 = title.replaceAll('[\\p{Punct}]','') // Punkt u.a. Satzzeichen entfernen
			if (title2.isInteger()) {
				prefix = title2
			} else {
				for (String s in title2.split()) {
					if (s) {
						if (s.isInteger()) {
							if (!title2.startsWith(s)) {
								prefix += s
							}
						} else {
							prefix += s.substring(0,1)
						}
					}
				}
			}
		}
		if (prefix) {
			prefix += '-'
		}
		return prefix.toLowerCase()
	}
	
    String GetPrintFreeTextTitle()
    {
        if (printFreeTextTitle) {
            return printFreeTextTitle
        }
        return getPrintMsg('fc.info')
    }
    
    String GetIncreaseValues()
    {
        if (resultClasses && contestRuleForEachClass) {
            String increase_values = ""
            for (ResultClass resultclass_instance in ResultClass.findAllByContest(this,[sort:"id"])) {
                if (resultclass_instance.increaseFactor > 0) {
                    if (increase_values) {
                        increase_values += ","
                    }
                    increase_values += resultclass_instance.increaseFactor.toString() + "%"
                }
            }
            return increase_values
        }
        if (increaseFactor > 0) {
            return increaseFactor.toString() + "%"
        }
        return ""
    }
    
    boolean IsUnsuitableStartNum(int crewNum)
    {
        if (unsuitableStartNum) {
            for (String s in unsuitableStartNum.split(',')) {
                if (s == crewNum.toString()) {
                    return true
                }
            }
        }
        return false
    }

    boolean RouteTitleFound(String newTitle)
    {
        for (Route route_instance in Route.findAllByContest(this,[sort:"idTitle"])) {
           if (route_instance.title == newTitle) {
               return true
           }
        }
        return false
    }
    
    String GetLiveTrackingVisibility()
    {
        return "${liveTrackingContestVisibility.substring(0,1).toUpperCase()}${liveTrackingContestVisibility.substring(1).toLowerCase()}"
    }
}
