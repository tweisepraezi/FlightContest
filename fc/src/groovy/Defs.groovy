class Defs
{
    static final String GPX_VIEWER_VERSION                             = "GM_Utils-5.21"
 
    static final String FCSAVE_FOLDER                                  = "C:/FCSave"
    static final String FCSAVE_FOLDER_FC                               = "${FCSAVE_FOLDER}/.fc"
    static final String FCSAVE_FOLDER_GEODATA                          = "${FCSAVE_FOLDER}/.geodata"
    
    static final String FCSAVE_FILE_CONFIG                             = "${Defs.FCSAVE_FOLDER_FC}/config.groovy"
    static final String FCSAVE_FILE_GEODATA_DATE                       = "${FCSAVE_FOLDER_GEODATA}/date.txt"
    static final String FCSAVE_FILE_GEODATA_AIRFIELDS                  = "${FCSAVE_FOLDER_GEODATA}/airfields.csv"
    static final String FCSAVE_FILE_GEODATA_CHURCHES                   = "${FCSAVE_FOLDER_GEODATA}/churches.csv"
    static final String FCSAVE_FILE_GEODATA_CASTLES                    = "${FCSAVE_FOLDER_GEODATA}/castles.csv"
    static final String FCSAVE_FILE_GEODATA_CHATEAUS                   = "${FCSAVE_FOLDER_GEODATA}/chateaus.csv"
    static final String FCSAVE_FILE_GEODATA_WINDPOWERSTATIONS          = "${FCSAVE_FOLDER_GEODATA}/windpowerstations.csv"
    static final String FCSAVE_FILE_GEODATA_PEAKS                      = "${FCSAVE_FOLDER_GEODATA}/peaks.csv"
    static final String FCSAVE_FILE_GEODATA_ADDITIONALS                = "${FCSAVE_FOLDER_GEODATA}/additionals.csv"
    static final String FCSAVE_FILE_GEODATA_SPECIALS                   = "${FCSAVE_FOLDER_GEODATA}/specials.csv"
    static final String FCSAVE_FILE_GEODATA_AIRSPACES                  = "${FCSAVE_FOLDER_GEODATA}/airspaces.kmz"
    
    static final String ROOT_FOLDER_GPXUPLOAD                          = "gpxupload"
    static final String ROOT_FOLDER_JOBS                               = "jobs"
    static final String ROOT_FOLDER_JOBS_DONE                          = "jobs/done"
    static final String ROOT_FOLDER_JOBS_ERROR                         = "jobs/error"
    static final String ROOT_FOLDER_JOBS_LOCK                          = "jobs/.lock"
    static final String ROOT_FOLDER_FONTS                              = "fonts"
    static final String ROOT_FOLDER_LIVE                               = "live"
    
    static final String EMAIL_SENDING                                  = "_email_sending"
    static final String EMAIL_ERROR                                    = "_email_error"
    
    static final String LIVE_STYLESHEET                                = "fclive.css"
    static final String LIVE_FILENAME                                  = "fclive.htm"
    static final int LIVE_UPLOADSECONDS                                = 60

    static final String TurnpointID_TimeCheck                          = "timecheck_"
    static final String TurnpointID_NotFound                           = "notfound_"
    static final String TurnpointID_ProcedureTurn                      = "procedureturn_"
    static final String TurnpointID_BadCourse                          = "badcourse_"
    static final String TurnpointID_MinAltitude                        = "minaltitude_"
    static final String TurnpointID_TurnpointObs                       = "turnpointobs_"
    static final String TurnpointID_ContestMapCenterPoints             = "contestmapcenterpoints_"
    static final String TurnpointID_ContestMapPrintPoints              = "contestmapprintpoints_"
    
    static final String EnrouteID_PhotoObs                             = "photoobs_"
    static final String EnrouteID_CanvasObs                            = "canvasobs_"
    static final String EnrouteID_PhotoCoordTitle                      = "enroutephotodataevaluationcoordtitle_"
    static final String EnrouteID_PhotoEvaluationValue                 = "enroutephotodataevaluationvalue_"
    static final String EnrouteID_CanvasCoordTitle                     = "enroutecanavasdataevaluationcoordtitle_"
    static final String EnrouteID_CanvasEvaluationValue                = "enroutecanavasdataevaluationvalue_"

    static final String EnrouteValue_Unevaluated                       = "unevaluated"
    static final String EnrouteValue_NotFound                          = "not_found"
    
    static final String TaskClassID                                    = "taskclass_"
    static final String TaskClassSubID_PlanningTestRun                 = "_planningTestRun"
    static final String TaskClassSubID_PlanningTestDistanceMeasure     = "_planningTestDistanceMeasure"
    static final String TaskClassSubID_PlanningTestDirectionMeasure    = "_planningTestDirectionMeasure"
    static final String TaskClassSubID_FlightTestRun                   = "_flightTestRun"
    static final String TaskClassSubID_FlightTestCheckSecretPoints     = "_flightTestCheckSecretPoints"
    static final String TaskClassSubID_FlightTestCheckTakeOff          = "_flightTestCheckTakeOff"
    static final String TaskClassSubID_FlightTestCheckLanding          = "_flightTestCheckLanding"
    static final String TaskClassSubID_ObservationTestRun              = "_observationTestRun"
    static final String TaskClassSubID_ObservationTestTurnpointRun     = "_observationTestTurnpointRun"
    static final String TaskClassSubID_ObservationTestEnroutePhotoRun  = "_observationTestEnroutePhotoRun"
    static final String TaskClassSubID_ObservationTestEnrouteCanvasRun = "_observationTestEnrouteCanvasRun"
    static final String TaskClassSubID_LandingTestRun                  = "_landingTestRun"
    static final String TaskClassSubID_LandingTest1Run                 = "_landingTest1Run"
    static final String TaskClassSubID_LandingTest2Run                 = "_landingTest2Run"
    static final String TaskClassSubID_LandingTest3Run                 = "_landingTest3Run"
    static final String TaskClassSubID_LandingTest4Run                 = "_landingTest4Run"
    static final String TaskClassSubID_SpecialTestRun                  = "_specialTestRun"
    static final String TaskClassSubID_SpecialTestTitle                = "_specialTestTitle"

    static final BigDecimal ENROUTE_MAX_DISTANCE                       = 0.5f  // NM, max. Entfernung eines Strecken-Objektes
    static final int ENROUTE_MAX_COURSE_DIFF                           = 90 // max. Kursabweichung in Grad
    static final int ENROUTE_CURVED_COURSE_DIFF                        = 1 // Kursabweichung in Grad, ab der krumme Strecke identifiziert wird
    
    static final String FONT_NOTOSANS_REGULAR                          = "NotoSans-Regular.ttf"
    static final String FONT_NOTOSANS_BOLD                             = "NotoSans-Bold.ttf"
    static final String FONT_NOTOSANS_ITALIC                           = "NotoSans-Italic.ttf"
    static final String FONT_NOTOSANS_BOLDITALIC                       = "NotoSans-BoldItalic.ttf"
    
    static final String CONTESTMAPOUTPUT_EXPORTPDFMAP                  = "contestMapOutput_exportPDFMap"
    static final String CONTESTMAPOUTPUT_EXPORTPNGMAP                  = "contestMapOutput_exportPNGMap"
    static final String CONTESTMAPOUTPUT_SHOWONLINEMAP                 = "contestMapOutput_showOnlineMap"
    static final String CONTESTMAPOUTPUT_EXPORTGPX                     = "contestMapOutput_exportGPX"
    
    static final String BACKGROUNDUPLOAD_OBJECT_SEPARATOR              = ";"
    static final String BACKGROUNDUPLOAD_SRCDEST_SEPARATOR             = "!"
    static final String BACKGROUNDUPLOAD_IDLINK_SEPARATOR              = "§"
}
