class Defs
{
    static final String GPX_VIEWER_VERSION                             = "GM_Utils-6.13"
 
    static final String FCSAVE_FOLDER                                  = "C:/FCSave"
    static final String FCSAVE_FOLDER_FC                               = "${FCSAVE_FOLDER}/.fc"
    static final String FCSAVE_FOLDER_GEODATA                          = "${FCSAVE_FOLDER}/.geodata"
    static final String FCSAVE_FOLDER_GEODATA_IMAGES                   = "${FCSAVE_FOLDER}/.geodata/images"
    
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
    
    static final String ROOT_FOLDER_GPXUPLOAD                          = "gpxupload" // alternate: GpxService.GPXDATA
    static final String ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB              = "gpxupload/OSMPRINT-JOB.txt"
    static final String ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOBID            = "gpxupload/OSMPRINT-JOBID-"
    static final String ROOT_FOLDER_GPXUPLOAD_OSMPRINTFILEID           = "gpxupload/OSMPRINT-FILEID-"
    static final String ROOT_FOLDER_JOBS                               = "jobs"
    static final String ROOT_FOLDER_JOBS_DONE                          = "jobs/done"
    static final String ROOT_FOLDER_JOBS_ERROR                         = "jobs/error"
    static final String ROOT_FOLDER_JOBS_LOCK                          = "jobs/.lock"
    static final String ROOT_FOLDER_FONTS                              = "fonts"
    static final String ROOT_FOLDER_LIVE                               = "live"
    
    static final String LIVE_STYLESHEET                                = "fclive.css"
    static final String LIVE_FILENAME                                  = "fclive.htm"
    static final int LIVE_UPLOADSECONDS                                = 60
    
    static final String EXE_GPSBABEL                                   = "C:/Program Files (x86)/GPSBabel/gpsbabel.exe"
    
    static final String LIVETRACKING_DISPLAY_TASK                      = "/display/task/"
    static final String LIVETRACKING_DISPLAY_MAP                       = "/map/"
    static final String LIVETRACKING_RESULTS_SERVICE                   = "/resultsservice/"
    static final String LIVETRACKING_RESULTS_TASKRESULTS               = "/taskresults/"
    
    static final String LIVETRACKING_TASKTEST_PLANNING                 = "Planning"
    static final String LIVETRACKING_TASKTEST_NAVIGATION               = "Navigation"
    static final String LIVETRACKING_TASKTEST_OBSERVATION              = "Observation"
    static final String LIVETRACKING_TASKTEST_LANDING                  = "Landing"
    static final String LIVETRACKING_TASKTEST_SPECIAL                  = "Other"
    
    static final String LIVETRACKING_VISIBILITY_PUBLIC                 = "public"
    static final String LIVETRACKING_VISIBILITY_PRIVATE                = "private"
    static final String LIVETRACKING_VISIBILITY_UNLISTED               = "unlisted"
    
    static final int OSMPRINTMAP_RUNSECONDS                            = 2
    static final String OSMPRINTMAP_GROUP                              = "OsmPrintMapGroup"
    static final String OSMPRINTMAP_ACTION                             = "Action"
    static final String OSMPRINTMAP_ACTION_CHECKJOB                    = "ActionCheckJob"
    static final String OSMPRINTMAP_JOBFILENAME                        = "JobFileName"
    static final String OSMPRINTMAP_JOBID                              = "JobId"
    static final String OSMPRINTMAP_JOBIDFILENAME                      = "JobIdFileName"
    static final String OSMPRINTMAP_FILEIDFILENAME                     = "FileIdFileName"
    static final String OSMPRINTMAP_PNGFILENAME                        = "PngFileName"
    static final String OSMPRINTMAP_PRINTLANDSCAPE                     = "PrintLandscape"
    static final String OSMPRINTMAP_PRINTCOLORCHANGES                  = "PrintColorChanges"
    static final String OSMPRINTMAP_ERR_EXTENSION                      = ".err"
    
    static final String TurnpointID_TimeCheck                          = "timecheck_"
    static final String TurnpointID_NotFound                           = "notfound_"
    static final String TurnpointID_ProcedureTurn                      = "procedureturn_"
    static final String TurnpointID_BadCourse                          = "badcourse_"
    static final String TurnpointID_MinAltitude                        = "minaltitude_"
    static final String TurnpointID_TurnpointObs                       = "turnpointobs_"
    static final String TurnpointID_ContestMapCenterPoints             = "contestmapcenterpoints_"
    static final String TurnpointID_ContestMapCenterPoints2            = "contestmapcenterpoints2_"
    static final String TurnpointID_ContestMapCenterPoints3            = "contestmapcenterpoints3_"
    static final String TurnpointID_ContestMapCenterPoints4            = "contestmapcenterpoints4_"
    static final String TurnpointID_ContestMapPrintPoints              = "contestmapprintpoints_"
    static final String TurnpointID_ContestMapPrintPoints2             = "contestmapprintpoints2_"
    static final String TurnpointID_ContestMapPrintPoints3             = "contestmapprintpoints3_"
    static final String TurnpointID_ContestMapPrintPoints4             = "contestmapprintpoints4_"
    static final String TurnpointID_ContestMapCurvedLegPoints          = "contestmapcurvedlegpoints_"
    
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
    
    static final String CONTESTMAPOUTPUT_EXPORTPRINTMAP                = "contestMapOutput_exportPrintMap"
    static final String CONTESTMAPOUTPUT_SHOWONLINEMAP                 = "contestMapOutput_showOnlineMap"
    static final String CONTESTMAPOUTPUT_EXPORTGPX                     = "contestMapOutput_exportGPX"
    static final String CONTESTMAPPRINT_PDFMAP                         = "contestMapPrint_PDFMap"
    static final String CONTESTMAPPRINT_PNGMAP                         = "contestMapPrint_PNGMap"
    static final String CONTESTMAPPRINT_PNGZIP                         = "contestMapPrint_PNGZip"
    static final String CONTESTMAPPRINT_TIFMAP                         = "contestMapPrint_TIFMAP"
    static final String CONTESTMAPPRINT_TILES                          = "contestMapPrint_TILES"
    
    static final int CONTESTMAPSCALE_200000                            = 200000
    static final int CONTESTMAPSCALE_250000                            = 250000
    
    static final int CONTESTMAPCONTOURLINES_NONE                       = 0
    static final int CONTESTMAPCONTOURLINES_20M                        = 20
    static final int CONTESTMAPCONTOURLINES_50M                        = 50
    static final int CONTESTMAPCONTOURLINES_100M                       = 100
    
    static final String CONTESTMAPAIRFIELDS_OSM_ICAO                   = "ICAO"    // saved in database. do not modify.
    static final String CONTESTMAPAIRFIELDS_OSM_NAME                   = "NAME"    // saved in database. do not modify.
    static final String CONTESTMAPAIRFIELDS_GEODATA                    = "GEODATA" // saved in database. do not modify.
    
    static final String CONTESTMAPPOINTS_INIT                          = "Init"    // saved in database. do not modify.
    
    static final String CONTESTMAPPRINTSIZE_A4                         = "A4"      // saved in database. do not modify.
    static final String CONTESTMAPPRINTSIZE_A3                         = "A3"      // saved in database. do not modify.
    static final String CONTESTMAPPRINTSIZE_A2                         = "A2"      // saved in database. do not modify.
    static final String CONTESTMAPPRINTSIZE_A1                         = "A1"      // saved in database. do not modify.
    static final String CONTESTMAPPRINTSIZE_ANR                        = "ANR"     // saved in database. do not modify.
    
    static final String BACKGROUNDUPLOAD_OBJECT_SEPARATOR              = ";"
    static final String BACKGROUNDUPLOAD_SRCDEST_SEPARATOR             = "!"
    static final String BACKGROUNDUPLOAD_IDLINK_SEPARATOR              = "§"
    
    static final String EMAIL_AT_CHAR                                  = "@"
    static final String EMAIL_NAME_SEPARATOR                           = "."
    static final String EMAIL_LIST_SEPARATOR                           = ","
    
    static final BigDecimal TRACKPOINT_INTERPOLATED_SPEED              = 20.0f // NM, min. Geschwindigkeit für Interpolation von Logger-Meßpunkten
    
    static final String ROUTEEXPORT_CREATOR                            = "Flight Contest - flightcontest.de - Version 8"
    static final String ROUTEEXPORT_SETTINGS                           = "settings"
    static final String ROUTEEXPORT_MAPSETTINGS                        = "mapsettings"
    static final String ROUTEEXPORT_TURNPOINTS                         = "turnpoints"
    static final String ROUTEEXPORT_PHOTOS                             = "photos"
    static final String ROUTEEXPORT_CANVAS                             = "canvas"
}
