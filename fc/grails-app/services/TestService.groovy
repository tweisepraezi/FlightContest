import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


class TestService
{
    def messageSource
    def logService
    def calcService
    
    //--------------------------------------------------------------------------
    Map RunTest()
    {
        Map ret_test = [:]
        int error_num = 0
        printstart "Module tests"
        error_num += testCalcService_isGateTrackOk()
        error_num += testCalcService_isCourseOk()
        error_num += testCalcService_isBadProcedureTurn()
        error_num += testCalcService_isGateTrackReached()
        error_num += testCalcService_GradDiff()
        error_num += testCalcService_GetNextUTC()
        error_num += testCalcService_ConvertAflosUTC()
        error_num += testCalcService_IntegerMinuteStr()
        error_num += testCalcService_DecimalSecondStr()
        error_num += testCalcService_GetDirectionGradDecimalMinute()
        if (error_num) {
            ret_test.message = "$error_num Fehler."
            ret_test.error = true
            printerror ret_test.message
        } else {
            ret_test.message = "Modultests ohne Fehler."
            printdone ret_test.message
        }
        return ret_test
    }
    
    //--------------------------------------------------------------------------
    int testCalcService_isGateTrackOk()
    // return error num
    {
        printstart "testCalcService_isGateTrackOk"
        int error_num = 0

        // gateTrack = 0, flightTrack ok: > 270 ... < 90
        if ( calcService.isGateTrackOk(   0, 270    )) {error_num++; println "1-1"}
        if (!calcService.isGateTrackOk(   0, 270.001)) {error_num++; println "1-2"}
        if (!calcService.isGateTrackOk(   0, 359.999)) {error_num++; println "1-3"}
        if (!calcService.isGateTrackOk(   0,   0    )) {error_num++; println "1-4"}
        if (!calcService.isGateTrackOk(   0,  89.999)) {error_num++; println "1-5"}
        if ( calcService.isGateTrackOk(   0,  90    )) {error_num++; println "1-6"}
        if ( calcService.isGateTrackOk(   0, 180    )) {error_num++; println "1-7"}
        
        // gateTrack = 10, flightTrack ok: > 280 ... < 100
        if (!calcService.isGateTrackOk(  10,   0    )) {error_num++; println "2-1"}
        if (!calcService.isGateTrackOk(  10,  10    )) {error_num++; println "2-2"}
        if (!calcService.isGateTrackOk(  10,  99.999)) {error_num++; println "2-3"}
        if ( calcService.isGateTrackOk(  10, 100    )) {error_num++; println "2-4"}
        if ( calcService.isGateTrackOk(  10, 280    )) {error_num++; println "2-5"}
        if (!calcService.isGateTrackOk(  10, 280.001)) {error_num++; println "2-6"}
        if (!calcService.isGateTrackOk(  10, 359.999)) {error_num++; println "2-7"}
        
        // gateTrack = 90, flightTrack ok: > 0 ... < 180
        if ( calcService.isGateTrackOk(  90,   0    )) {error_num++; println "3-1"}
        if (!calcService.isGateTrackOk(  90,   0.001)) {error_num++; println "3-2"}
        if (!calcService.isGateTrackOk(  90,  90    )) {error_num++; println "3-3"}
        if (!calcService.isGateTrackOk(  90, 179.999)) {error_num++; println "3-4"}
        if ( calcService.isGateTrackOk(  90, 180    )) {error_num++; println "3-5"}
        if ( calcService.isGateTrackOk(  90, 270    )) {error_num++; println "3-6"}
        if ( calcService.isGateTrackOk(  90, 359.999)) {error_num++; println "3-7"}
        
        // gateTrack = 180, flightTrack ok: > 90 ... < 270
        if ( calcService.isGateTrackOk( 180,   0    )) {error_num++; println "4-1"}
        if ( calcService.isGateTrackOk( 180,  90    )) {error_num++; println "4-2"}
        if (!calcService.isGateTrackOk( 180,  90.001)) {error_num++; println "4-3"}
        if (!calcService.isGateTrackOk( 180, 180    )) {error_num++; println "4-4"}
        if (!calcService.isGateTrackOk( 180, 269.999)) {error_num++; println "4-5"}
        if ( calcService.isGateTrackOk( 180, 270    )) {error_num++; println "4-6"}
        if ( calcService.isGateTrackOk( 180, 359.999)) {error_num++; println "4-7"}
        
        // gateTrack = 270, flightTrack ok: > 180 ... < 0
        if ( calcService.isGateTrackOk( 270,   0    )) {error_num++; println "5-1"}
        if ( calcService.isGateTrackOk( 270,  90    )) {error_num++; println "5-2"}
        if ( calcService.isGateTrackOk( 270, 180    )) {error_num++; println "5-3"}
        if (!calcService.isGateTrackOk( 270, 180.001)) {error_num++; println "5-4"}
        if (!calcService.isGateTrackOk( 270, 270    )) {error_num++; println "5-5"}
        if (!calcService.isGateTrackOk( 270, 359.999)) {error_num++; println "5-6"}
        
        // gateTrack = 359, flightTrack ok: > 269 ... < 89
        if (!calcService.isGateTrackOk( 359,   0    )) {error_num++; println "6-1"}
        if (!calcService.isGateTrackOk( 359,  88.999)) {error_num++; println "6-2"}
        if ( calcService.isGateTrackOk( 359,  89    )) {error_num++; println "6-3"}
        if ( calcService.isGateTrackOk( 359, 269    )) {error_num++; println "6-4"}
        if (!calcService.isGateTrackOk( 359, 269.001)) {error_num++; println "6-5"}
        if (!calcService.isGateTrackOk( 359, 359    )) {error_num++; println "6-6"}
        if (!calcService.isGateTrackOk( 359, 359.999)) {error_num++; println "6-7"}
        
        if (error_num) {
            printerror "$error_num tests failed."
        } else {
            printdone ""
        }
        return error_num
    }
    
    //--------------------------------------------------------------------------
    int testCalcService_isCourseOk()
    // return error num
    {
        printstart "testCalcService_isCourseOk"
        int error_num = 0

        // gateTrack = 0, flightTrack ok: >= 270 ... <= 90
        if ( calcService.isCourseOk(   0, 269.999)) {error_num++; println "1-1"}
        if (!calcService.isCourseOk(   0, 270    )) {error_num++; println "1-2"}
        if (!calcService.isCourseOk(   0, 359.999)) {error_num++; println "1-3"}
        if (!calcService.isCourseOk(   0,   0    )) {error_num++; println "1-4"}
        if (!calcService.isCourseOk(   0,  90    )) {error_num++; println "1-5"}
        if ( calcService.isCourseOk(   0,  90.001)) {error_num++; println "1-6"}
        if ( calcService.isCourseOk(   0, 180    )) {error_num++; println "1-7"}
        
        // gateTrack = 10, flightTrack ok: >= 280 ... <= 100
        if (!calcService.isCourseOk(  10,   0    )) {error_num++; println "2-1"}
        if (!calcService.isCourseOk(  10,  10    )) {error_num++; println "2-2"}
        if (!calcService.isCourseOk(  10, 100    )) {error_num++; println "2-3"}
        if ( calcService.isCourseOk(  10, 100.001)) {error_num++; println "2-4"}
        if ( calcService.isCourseOk(  10, 279.999)) {error_num++; println "2-5"}
        if (!calcService.isCourseOk(  10, 280    )) {error_num++; println "2-6"}
        if (!calcService.isCourseOk(  10, 359.999)) {error_num++; println "2-7"}
        
        // gateTrack = 90, flightTrack ok: >= 0 ... <= 180
        if (!calcService.isCourseOk(  90,   0    )) {error_num++; println "3-2"}
        if (!calcService.isCourseOk(  90,  90    )) {error_num++; println "3-3"}
        if (!calcService.isCourseOk(  90, 180    )) {error_num++; println "3-4"}
        if ( calcService.isCourseOk(  90, 180.001)) {error_num++; println "3-5"}
        if ( calcService.isCourseOk(  90, 270    )) {error_num++; println "3-6"}
        if ( calcService.isCourseOk(  90, 359.999)) {error_num++; println "3-7"}
        
        // gateTrack = 180, flightTrack ok: >= 90 ... <= 270
        if ( calcService.isCourseOk( 180,   0    )) {error_num++; println "4-1"}
        if ( calcService.isCourseOk( 180,  89.999)) {error_num++; println "4-2"}
        if (!calcService.isCourseOk( 180,  90    )) {error_num++; println "4-3"}
        if (!calcService.isCourseOk( 180, 180    )) {error_num++; println "4-4"}
        if (!calcService.isCourseOk( 180, 270    )) {error_num++; println "4-5"}
        if ( calcService.isCourseOk( 180, 270.001)) {error_num++; println "4-6"}
        if ( calcService.isCourseOk( 180, 359.999)) {error_num++; println "4-7"}
        
        // gateTrack = 270, flightTrack ok: >= 180 ... <= 0
        if (!calcService.isCourseOk( 270,   0    )) {error_num++; println "5-1"}
        if ( calcService.isCourseOk( 270,   0.001)) {error_num++; println "5-2"}
        if ( calcService.isCourseOk( 270,  90    )) {error_num++; println "5-3"}
        if ( calcService.isCourseOk( 270, 179.000)) {error_num++; println "5-4"}
        if (!calcService.isCourseOk( 270, 180    )) {error_num++; println "5-5"}
        if (!calcService.isCourseOk( 270, 270    )) {error_num++; println "5-6"}
        if (!calcService.isCourseOk( 270, 359.999)) {error_num++; println "5-7"}
        
        // gateTrack = 359, flightTrack ok: >= 269 ... <= 89
        if (!calcService.isCourseOk( 359,   0    )) {error_num++; println "6-1"}
        if (!calcService.isCourseOk( 359,  89    )) {error_num++; println "6-2"}
        if ( calcService.isCourseOk( 359,  89.001)) {error_num++; println "6-3"}
        if ( calcService.isCourseOk( 359, 268.999)) {error_num++; println "6-4"}
        if (!calcService.isCourseOk( 359, 269    )) {error_num++; println "6-5"}
        if (!calcService.isCourseOk( 359, 359    )) {error_num++; println "6-6"}
        if (!calcService.isCourseOk( 359, 359.999)) {error_num++; println "6-7"}
        
        if (error_num) {
            printerror "$error_num tests failed."
        } else {
            printdone ""
        }
        return error_num
    }
    
    //--------------------------------------------------------------------------
    int testCalcService_isBadProcedureTurn()
    // return error num
    {
        printstart "testCalcService_isBadProcedureTurn"
        int error_num = 0

        // links rum - oben rechts
        if (!calcService.isBadProcedureTurn(  359,  90, 225)) {error_num++; println "1-1"}
        if (!calcService.isBadProcedureTurn(  300,  90, 225)) {error_num++; println "1-2"}
        if (!calcService.isBadProcedureTurn(   90,  90, 225)) {error_num++; println "1-3"}
        if (!calcService.isBadProcedureTurn(    0,  90, 225)) {error_num++; println "1-4"}
        if (!calcService.isBadProcedureTurn(  -90,  90, 225)) {error_num++; println "1-5"}
        if (!calcService.isBadProcedureTurn( -164,  90, 225)) {error_num++; println "1-6"}
        if ( calcService.isBadProcedureTurn( -165,  90, 225)) {error_num++; println "1-7"}
        if ( calcService.isBadProcedureTurn( -225,  90, 225)) {error_num++; println "1-8"}
        if ( calcService.isBadProcedureTurn( -285,  90, 225)) {error_num++; println "1-9"}
        if (!calcService.isBadProcedureTurn( -286,  90, 225)) {error_num++; println "1-10"}
        if (!calcService.isBadProcedureTurn( -359,  90, 225)) {error_num++; println "1-11"}
        
        // links rum - unten rechts
        if (!calcService.isBadProcedureTurn( -164, 180, 315)) {error_num++; println "2-1"}
        if ( calcService.isBadProcedureTurn( -165, 180, 315)) {error_num++; println "2-2"}
        if ( calcService.isBadProcedureTurn( -225, 180, 315)) {error_num++; println "2-3"}
        if ( calcService.isBadProcedureTurn( -285, 180, 315)) {error_num++; println "2-4"}
        if (!calcService.isBadProcedureTurn( -286, 180, 315)) {error_num++; println "2-5"}
        
        // links rum - unten links
        if (!calcService.isBadProcedureTurn( -164, 270,  45)) {error_num++; println "3-1"}
        if ( calcService.isBadProcedureTurn( -165, 270,  45)) {error_num++; println "3-2"}
        if ( calcService.isBadProcedureTurn( -225, 270,  45)) {error_num++; println "3-3"}
        if ( calcService.isBadProcedureTurn( -285, 270,  45)) {error_num++; println "3-4"}
        if (!calcService.isBadProcedureTurn( -286, 270,  45)) {error_num++; println "3-5"}

        // links rum - oben links
        if (!calcService.isBadProcedureTurn( -164,   0, 135)) {error_num++; println "4-1"}
        if ( calcService.isBadProcedureTurn( -165,   0, 135)) {error_num++; println "4-2"}
        if ( calcService.isBadProcedureTurn( -225,   0, 135)) {error_num++; println "4-3"}
        if ( calcService.isBadProcedureTurn( -285,   0, 135)) {error_num++; println "4-4"}
        if (!calcService.isBadProcedureTurn( -286,   0, 135)) {error_num++; println "4-5"}

        // rechts rum - oben rechts
        if (!calcService.isBadProcedureTurn(  164, 225,  90)) {error_num++; println "5-1"}
        if ( calcService.isBadProcedureTurn(  165, 225,  90)) {error_num++; println "5-2"}
        if ( calcService.isBadProcedureTurn(  225, 225,  90)) {error_num++; println "5-3"}
        if ( calcService.isBadProcedureTurn(  285, 225,  90)) {error_num++; println "5-4"}
        if (!calcService.isBadProcedureTurn(  286, 225,  90)) {error_num++; println "5-5"}

        // rechts rum - unten rechts
        if (!calcService.isBadProcedureTurn(  164, 315, 180)) {error_num++; println "6-1"}
        if ( calcService.isBadProcedureTurn(  165, 315, 180)) {error_num++; println "6-2"}
        if ( calcService.isBadProcedureTurn(  225, 315, 180)) {error_num++; println "6-3"}
        if ( calcService.isBadProcedureTurn(  285, 315, 180)) {error_num++; println "6-4"}
        if (!calcService.isBadProcedureTurn(  286, 315, 180)) {error_num++; println "6-5"}
        
        // rechts rum - unten links
        if (!calcService.isBadProcedureTurn(  164,  45, 270)) {error_num++; println "7-1"}
        if ( calcService.isBadProcedureTurn(  165,  45, 270)) {error_num++; println "7-2"}
        if ( calcService.isBadProcedureTurn(  225,  45, 270)) {error_num++; println "7-3"}
        if ( calcService.isBadProcedureTurn(  285,  45, 270)) {error_num++; println "7-4"}
        if (!calcService.isBadProcedureTurn(  286,  45, 270)) {error_num++; println "7-5"}

        // rechts rum - oben links
        if (!calcService.isBadProcedureTurn(  164, 135,   0)) {error_num++; println "8-1"}
        if ( calcService.isBadProcedureTurn(  165, 135,   0)) {error_num++; println "8-2"}
        if ( calcService.isBadProcedureTurn(  225, 135,   0)) {error_num++; println "8-3"}
        if ( calcService.isBadProcedureTurn(  285, 135,   0)) {error_num++; println "8-4"}
        if (!calcService.isBadProcedureTurn(  286, 135,   0)) {error_num++; println "8-5"}
        
        if (error_num) {
            printerror "$error_num tests failed."
        } else {
            printdone ""
        }
        return error_num
    }
    
    //--------------------------------------------------------------------------
    int testCalcService_isGateTrackReached()
    {
        printstart "testCalcService_isGateTrackReached"
        int error_num = 0

        // rechts rum
        if ( calcService.isGateTrackReached(350, 358, 357, false)) {error_num++; println "1-1"}
        if (!calcService.isGateTrackReached(357, 358, 358, false)) {error_num++; println "1-2"}
        if (!calcService.isGateTrackReached(358, 358, 358, false)) {error_num++; println "1-3"}
        
        if (!calcService.isGateTrackReached(350, 358, 358, false)) {error_num++; println "1-4"}
        if (!calcService.isGateTrackReached(350, 358,   4, false)) {error_num++; println "1-5"}
        if (!calcService.isGateTrackReached(358, 358,   4, false)) {error_num++; println "1-6"}
        
        if (!calcService.isGateTrackReached(350,   2,   4, false)) {error_num++; println "1-7"}
        if (!calcService.isGateTrackReached(  2,   3,   5, false)) {error_num++; println "1-8"}
        
        if ( calcService.isGateTrackReached(296, 118, 303, false)) {error_num++; println "1-9"}
        
        // links rum
        if ( calcService.isGateTrackReached(357, 358, 350, false)) {error_num++; println "2-1"}
        if (!calcService.isGateTrackReached(358, 358, 357, false)) {error_num++; println "2-2"}

        if (!calcService.isGateTrackReached(358, 358, 350, false)) {error_num++; println "2-4"}
        if (!calcService.isGateTrackReached(  4, 358, 350, false)) {error_num++; println "2-5"}
        if (!calcService.isGateTrackReached(  4, 358, 358, false)) {error_num++; println "2-6"}

        if (!calcService.isGateTrackReached(  4,   2, 350, false)) {error_num++; println "2-7"}
        if (!calcService.isGateTrackReached(  5,   3,   2, false)) {error_num++; println "2-8"}
        
        if (error_num) {
            printerror "$error_num tests failed."
        } else {
            printdone ""
        }
        return error_num
    }
    
    //--------------------------------------------------------------------------
    int testCalcService_GradDiff()
    // return error num
    {
        printstart "testCalcService_GradDiff"
        int error_num = 0
    
        // positiv
        if (FcMath.GradDiff(0,    1) !=  1) {error_num++; println "1-1"}
        if (FcMath.GradDiff(0,   10) != 10) {error_num++; println "1-1"}
        if (FcMath.GradDiff(10,  11) !=  1) {error_num++; println "1-3"}
        if (FcMath.GradDiff(359,  1) !=  2) {error_num++; println "1-4"}
        if (FcMath.GradDiff(350, 10) != 20) {error_num++; println "1-5"}
        
        // negativ
        if (FcMath.GradDiff( 1,   0) !=  -1) {error_num++; println "2-1"}
        if (FcMath.GradDiff(10,   0) != -10) {error_num++; println "2-2"}
        if (FcMath.GradDiff(11,  10) !=  -1) {error_num++; println "2-3"}
        if (FcMath.GradDiff( 1, 359) !=  -2) {error_num++; println "2-4"}
        if (FcMath.GradDiff(10, 350) != -20) {error_num++; println "2-5"}

        if (error_num) {
            printerror "$error_num tests failed."
        } else {
            printdone ""
        }
        return error_num
    }
    
    //--------------------------------------------------------------------------
    int testCalcService_GetNextUTC()
    // return error num
    {
        printstart "testCalcService_GetNextUTC"
        int error_num = 0
    
        // same day
        if (FcTime.UTCGetNextDateTime("2015-01-01T12:00:00Z","12:00:01") != "2015-01-01T12:00:01Z") {error_num++; println "1-1"}
        if (FcTime.UTCGetNextDateTime("2015-01-01T00:00:01Z","23:59:59") != "2015-01-01T23:59:59Z") {error_num++; println "1-2"}
        if (FcTime.UTCGetNextDateTime("2015-01-02T10:00:00Z","12:00:00") != "2015-01-02T12:00:00Z") {error_num++; println "1-3"}
        if (FcTime.UTCGetNextDateTime("2015-01-03T00:00:00Z","00:00:00") != "2015-01-03T00:00:00Z") {error_num++; println "1-4"}
        if (FcTime.UTCGetNextDateTime("2015-01-03T06:00:00Z","06:00:00") != "2015-01-03T06:00:00Z") {error_num++; println "1-5"}
        
        // next day
        if (FcTime.UTCGetNextDateTime("2015-01-01T23:59:59Z","00:00:00") != "2015-01-02T00:00:00Z") {error_num++; println "2-1"}
        if (FcTime.UTCGetNextDateTime("2015-01-02T10:00:00Z","02:00:00") != "2015-01-03T02:00:00Z") {error_num++; println "2-2"}
        if (FcTime.UTCGetNextDateTime("2015-01-03T12:00:00Z","00:00:00") != "2015-01-04T00:00:00Z") {error_num++; println "2-3"}
        if (FcTime.UTCGetNextDateTime("2015-01-04T06:00:00Z","05:59:00") != "2015-01-05T05:59:00Z") {error_num++; println "2-4"}
        
        if (error_num) {
            printerror "$error_num tests failed."
        } else {
            printdone ""
        }
        return error_num
    }
    
    //--------------------------------------------------------------------------
    int testCalcService_ConvertAflosUTC()
    // return error num
    {
        printstart "testCalcService_ConvertAflosUTC"
        int error_num = 0
    
        // same day
        if (AflosTools.ConvertAflosUTC("2015-01-01T00:00:00Z", "09h 36min 05,000sec") != "2015-01-01T09:36:05Z") {error_num++; println "1-1"}
        if (AflosTools.ConvertAflosUTC("2015-01-01T09:36:05Z", "09h 36min 06,000sec") != "2015-01-01T09:36:06Z") {error_num++; println "1-2"}
        if (AflosTools.ConvertAflosUTC("2015-01-01T09:36:06Z", "09h 36min 12,456sec") != "2015-01-01T09:36:12Z") {error_num++; println "1-3"}
        if (AflosTools.ConvertAflosUTC("2015-01-01T09:36:06Z", "09h 36min 12,500sec") != "2015-01-01T09:36:12Z") {error_num++; println "1-4"}
        if (AflosTools.ConvertAflosUTC("2015-01-01T09:36:06Z", "09h 36min 12,501sec") != "2015-01-01T09:36:13Z") {error_num++; println "1-5"}
        if (AflosTools.ConvertAflosUTC("2015-01-01T09:36:06Z", "09h 36min 13,499sec") != "2015-01-01T09:36:13Z") {error_num++; println "1-6"}
        if (AflosTools.ConvertAflosUTC("2015-01-01T09:36:06Z", "09h 36min 13,500sec") != "2015-01-01T09:36:14Z") {error_num++; println "1-7"}
        if (AflosTools.ConvertAflosUTC("2015-01-01T09:36:06Z", "09h 36min 13,501sec") != "2015-01-01T09:36:14Z") {error_num++; println "1-8"}
        
        // next day
        if (AflosTools.ConvertAflosUTC("2015-01-01T09:36:06Z", "07h 01min 20,000sec") != "2015-01-02T07:01:20Z") {error_num++; println "2-1"}
        
        if (error_num) {
            printerror "$error_num tests failed."
        } else {
            printdone ""
        }
        return error_num
    }

    //--------------------------------------------------------------------------
    int testCalcService_IntegerMinuteStr()
    // return error num
    {
        printstart "testCalcService_IntegerMinuteStr"
        int error_num = 0
    
        if (CoordPresentation.IntegerMinuteStr(12.0)    != "12") {error_num++; println "1-1"}
        if (CoordPresentation.IntegerMinuteStr(12.1)    != "12") {error_num++; println "1-2"}
        if (CoordPresentation.IntegerMinuteStr(12.5)    != "12") {error_num++; println "1-3"}
        if (CoordPresentation.IntegerMinuteStr(12.6667) != "12") {error_num++; println "1-4"}
        if (CoordPresentation.IntegerMinuteStr(12.9134) != "12") {error_num++; println "1-5"}
        if (CoordPresentation.IntegerMinuteStr(12.9999) != "12") {error_num++; println "1-6"}

        if (error_num) {
            printerror "$error_num tests failed."
        } else {
            printdone ""
        }
        return error_num
    }
    
    //--------------------------------------------------------------------------
    int testCalcService_DecimalSecondStr()
    // return error num
    {
        printstart "testCalcService_DecimalSecondStr"
        int error_num = 0
    
        if (CoordPresentation.DecimalSecondStr(12.00) !=    "00,0000") {error_num++; println "1-1"}
        if (CoordPresentation.DecimalSecondStr(12.25) !=    "15,0000") {error_num++; println "1-2"}
        if (CoordPresentation.DecimalSecondStr(12.50) !=    "30,0000") {error_num++; println "1-3"}
        if (CoordPresentation.DecimalSecondStr(12.75) !=    "45,0000") {error_num++; println "1-4"}
        if (CoordPresentation.DecimalSecondStr(12.90) !=    "54,0000") {error_num++; println "1-5"}
        if (CoordPresentation.DecimalSecondStr(12.99) !=    "59,4000") {error_num++; println "1-6"}
        if (CoordPresentation.DecimalSecondStr(12.99999) != "59,9994") {error_num++; println "1-7"}
        
        if (error_num) {
            printerror "$error_num tests failed."
        } else {
            printdone ""
        }
        return error_num
    }
    
    //--------------------------------------------------------------------------
    int testCalcService_GetDirectionGradDecimalMinute()
    // return error num
    {
        printstart "testCalcService_GetDirectionGradDecimalMinute"
        int error_num = 0
    
        // N
        if (CoordPresentation.GetDirectionGradDecimalMinute(   0.0,true ) != [direction:'N',grad:0,minute:  0]) {error_num++; println "1-1"}
        if (CoordPresentation.GetDirectionGradDecimalMinute(  10.5,true ) != [direction:'N',grad:10,minute:30]) {error_num++; println "1-2"}
        if (CoordPresentation.GetDirectionGradDecimalMinute(  52.0,true ) != [direction:'N',grad:52,minute: 0]) {error_num++; println "1-3"}
        if (CoordPresentation.GetDirectionGradDecimalMinute(  90.0,true ) != [direction:'N',grad:90,minute: 0]) {error_num++; println "1-4"}
        
        // S
        if (CoordPresentation.GetDirectionGradDecimalMinute( -10.5,true ) != [direction:'S',grad:10,minute:30]) {error_num++; println "2-1"}
        if (CoordPresentation.GetDirectionGradDecimalMinute( -52.0,true ) != [direction:'S',grad:52,minute: 0]) {error_num++; println "2-2"}
        if (CoordPresentation.GetDirectionGradDecimalMinute( -90.0,true ) != [direction:'S',grad:90,minute: 0]) {error_num++; println "2-3"}
        
        // E
        if (CoordPresentation.GetDirectionGradDecimalMinute(   0.0,false) != [direction:'E',grad:  0,minute: 0]) {error_num++; println "3-1"}
        if (CoordPresentation.GetDirectionGradDecimalMinute(   1.0,false) != [direction:'E',grad:  1,minute: 0]) {error_num++; println "3-2"}
        if (CoordPresentation.GetDirectionGradDecimalMinute(  16.0,false) != [direction:'E',grad: 16,minute: 0]) {error_num++; println "3-3"}
        if (CoordPresentation.GetDirectionGradDecimalMinute(  16.5,false) != [direction:'E',grad: 16,minute:30]) {error_num++; println "3-4"}
        if (CoordPresentation.GetDirectionGradDecimalMinute( 179.0,false) != [direction:'E',grad:179,minute: 0]) {error_num++; println "3-5"}
        if (CoordPresentation.GetDirectionGradDecimalMinute( 180.0,false) != [direction:'E',grad:180,minute: 0]) {error_num++; println "3-6"}
        
        // W
        if (CoordPresentation.GetDirectionGradDecimalMinute(  -1.0,false) != [direction:'W',grad:  1,minute: 0]) {error_num++; println "4-1"}
        if (CoordPresentation.GetDirectionGradDecimalMinute( -16.0,false) != [direction:'W',grad: 16,minute: 0]) {error_num++; println "4-2"}
        if (CoordPresentation.GetDirectionGradDecimalMinute( -16.5,false) != [direction:'W',grad: 16,minute:30]) {error_num++; println "4-3"}
        if (CoordPresentation.GetDirectionGradDecimalMinute(-179.0,false) != [direction:'W',grad:179,minute: 0]) {error_num++; println "4-4"}
        if (CoordPresentation.GetDirectionGradDecimalMinute(-180.0,false) != [direction:'W',grad:180,minute: 0]) {error_num++; println "4-5"}
        
        if (error_num) {
            printerror "$error_num tests failed."
        } else {
            printdone ""
        }
        return error_num
    }
    /*
    //--------------------------------------------------------------------------
    int testCalcService_New()
    // return error num
    {
        printstart "testCalcService_New"
        int error_num = 0
    
        if (1 != 2) {error_num++; println "1-1"}

        if (error_num) {
            printerror "$error_num tests failed."
        } else {
            printdone ""
        }
        return error_num
    }
    */
    
    //--------------------------------------------------------------------------
    private String getMsg(String code, List args)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        if (args) {
            return messageSource.getMessage(code, args.toArray(), new Locale(session_obj.showLanguage))
        } else {
            return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
        }
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
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
