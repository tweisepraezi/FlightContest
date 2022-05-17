import java.math.*
import java.text.DecimalFormat
import java.util.List;

import org.springframework.web.context.request.RequestContextHolder

class CalcService
{
    def messageSource
    def logService
    
    final static Float ADVANCED_GATE_WIDTH     = 4.0  // NM
    final static BigDecimal NEXTGATE_DISTANCE  = 1.0  // NM
    final static int NEXTGATE_SECONDS          = 30   // Sekunden; Zeit vor dem Hilfsgate, wo neuer Kurs nicht als Kursabweichung bestraft wird (bei Kurswechsel vor Gate)
    final static int LASTGATE_SECONDS          = 45   // Sekunden; Zeit nach dem Gate-Durchflug, wo alter Kurs nicht als Kursabweichung bestraft wird
    final static int PROCEDURETURN_SECONDS     = 180  // Sekunden; Zeit nach dem Gate-Durchflug, wo ein Procedure Turn erwartet wird
    final static int PROCEDURETURN_MAXDIFFGRAD = 60   // Grad; max. Abweichung der Kursänderung beim Procedure Turn
                                                      //   bei Änderung testCalcService_isBadProcedureTurn anpassen
    final static int TO_GATE_SECONDS           = 1800 // Sekunden; Zeit nach dem T/O-Durchflug erwartet wird (30 min)
    
    final static int COORDINATE_SCALE          = 10   // Nachkommastellen für Koordinaten
    
    //--------------------------------------------------------------------------
    Map CalculateLoggerFile(String fileExtension, Test testInstance, String loggerFileName, boolean interpolateMissingData, int correctSeconds)
    {
        printstart "CalculateLoggerFile: extension '$fileExtension', crew '$testInstance.crew.name', file '$loggerFileName'"
        
        printstart "Import track points from logger file"
        Map reader = LoggerFileTools.ReadLoggerFile(fileExtension, testInstance, loggerFileName, interpolateMissingData, correctSeconds)
        if (reader.errors) {
            printerror reader.errors
        } else {
            printdone "${reader.trackpointnum} track points found."
        }
        
        if (!reader.errors && reader.trackpointnum) {
            Calculate(testInstance, "", "")
        }
        
        printdone ""
        
        return reader
    }
    
    //--------------------------------------------------------------------------
    void Calculate(Test testInstance, String loggerDataStartUtc, String loggerDataEndUtc)
    {
        printstart "Calculate '$testInstance.crew.name', startutc '$loggerDataStartUtc', endutc '$loggerDataEndUtc'"
        
        println "Remove old calc results"
        if (testInstance.IsLoggerResult()) {
            CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:"id"]).each { CalcResult calcresult_instance ->
                calcresult_instance.delete()
            }
        }
        
        List track_points = testInstance.GetTrackPoints(loggerDataStartUtc, loggerDataEndUtc).trackPoints
        if (!track_points) {
            printdone "No track points."
            return
        }
        
        List gate_list = getGates(testInstance)
        if (!gate_list) {
            printdone "No gates."
            return
        }

        /*
        printstart "Gates"
        for (Map g in gate_list) {
            println g
        }
        printdone ""

        printstart "Trackpoints"
        for (Map p in track_points) {
            println p
        }
        printdone ""
        */
        
        // calculate gate crossings
        printstart "Calculate gate crossings"
        int first_gate_pos = 0
        Map analyze_gates = getAnalyzeGates(gate_list, first_gate_pos)
        boolean analyze_restarted = false
        boolean analyze_restart = false
        int analyze_num = 0
        String last_valid_gate_utc = ""
        while (true) {
            Map last_p = null
            for (Map p in track_points) {
                if (!analyze_gates) {
                    break
                }
                if (last_p != null) {
                    /*
                    if (p.utc == "2015-01-01T08:38:06Z") {
                        int i = 0
                    }
                    */
                    if (!last_valid_gate_utc || (p.utc > last_valid_gate_utc)) {
                        Map result = calculateGateCrossings(last_p, p, analyze_gates.gates)
                        if (result.gateFound || result.additionalGateFound) {
                            if (result.additionalGateFound) {
                                println "  Additional gate found"
                            }
                            boolean gate_missed = saveCalcResultGate(testInstance, result, last_p, p)
                            
                            first_gate_pos = analyze_gates.lastGatePos + 1
                            analyze_gates = getAnalyzeGates(gate_list, first_gate_pos)
                            
                            if (!gate_missed || result.onAdvancedGateLine) {
                                last_valid_gate_utc = p.utc
                            }
                        } else if (analyze_gates.gates) {
                            Map first_gate = analyze_gates.gates[0]
                            if (first_gate.coordType == CoordType.TO) {
                                String to_utc = FcTime.UTCGetDateTime(p.utc, testInstance.takeoffTime, testInstance.task.contest.timeZone)
                                int first_seconds = FcTime.UTCTimeDiffSeconds(to_utc, p.utc)
                                if (first_seconds > TO_GATE_SECONDS) {
                                    saveCalcResultGateNotFound(testInstance, first_gate, to_utc)
                                    println "  T/O-Timeout (${TO_GATE_SECONDS}s)"
                                    analyze_restart = true
                                    break
                                }
                            }
                        }
                    }
                }
                last_p = p
            }
            if (analyze_restart) {
                println "  Restart"
                first_gate_pos = analyze_gates.lastGatePos + 1
                analyze_gates = getAnalyzeGates(gate_list, first_gate_pos)
                analyze_restart = false
            } else if (first_gate_pos < gate_list.size()) {
                analyze_num++
                if (!analyze_restarted) {
                    println "  Gate not found. Analyze again: $last_valid_gate_utc"
                    analyze_restarted = true
                } else {
                    saveCalcResultGateNotFound(testInstance, analyze_gates.gates[0], last_p.utc)
                    println "  Gate not found. Analyze with next gate: $last_valid_gate_utc"
                    first_gate_pos = analyze_gates.lastGatePos + 1
                    analyze_gates = getAnalyzeGates(gate_list, first_gate_pos)
                    analyze_restarted = false
                }
            } else {
                println "All ${gate_list.size()} gates processed."
                break
            }
        }
        if (analyze_num > 0) {
            printstart "Repair time inconsistencies of not found or missed gates"
            String last_utc = ""
            for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'coordTitle']).reverse()) {
                if (last_utc) {
                    if (calcresult_instance.utc >= last_utc) {
                        if (calcresult_instance.gateNotFound || calcresult_instance.gateMissed) {
                            String new_utc = FcTime.UTCAddSeconds(last_utc, -2) 
                            for (Map p in track_points.reverse()) {
                                if (p.utc <= new_utc) {
                                    new_utc = p.utc
                                    break
                                }
                            }
                            println "${calcresult_instance.coordTitle.name()}: Set ${calcresult_instance.utc} -> ${new_utc}"
                            calcresult_instance.utc = new_utc
                            calcresult_instance.save()
                        } else {
                            println "${calcresult_instance.coordTitle.name()}: Invalid ${calcresult_instance.utc}"
                        }
                    }
                }
                last_utc = calcresult_instance.utc
            }
            printdone ""
        }
        printdone ""

        // calculate bad course
        printstart "Calculate bad course"
        List coursecheck_gate_list = getCourseCheckGates(testInstance, gate_list)
        /*
        printstart "Bad course gates"
        for (Map g in coursecheck_gate_list) {
            println g
        }
        printdone ""
        */
        if (coursecheck_gate_list.size() > 0 ) {
            int  gate_pos = 0
            boolean gate_pos_changed = true
            Map gate = coursecheck_gate_list[gate_pos]
            Map last_gate = null
            BigDecimal last_track = null
            Map last_p = null
            boolean check_course_nextgate = false
            List badcourse_points = []
            boolean check_procedureturn = false
            boolean check_procedureturn_nextgate = false
            BigDecimal procedureturn_track_changes = 0
            for (Map p in track_points) {
                /*
                if (p.utc == "2015-01-01T10:22:56Z") {
                    int i = 0
                }
                */
                if (p.track != null) {
                    if (p.utc >= gate.gateUtc) {
                        if (check_course_nextgate) {
                            if (badcourse_points.size() > 0) {
                                saveCalcResultBadCourse(testInstance, badcourse_points)
                                badcourse_points.clear()
                            }
                            check_course_nextgate = false
                        }
                        if (check_procedureturn_nextgate) {
                            boolean is_bad_procedureturn = isBadProcedureTurn(procedureturn_track_changes, last_gate.gateTrack, gate.gateTrack)
                            println "Procedure Turn (2) ${gate.coordType}${gate.coordTypeNumber}: BT:${is_bad_procedureturn}, Changes:${procedureturn_track_changes} Grad"
                            if (is_bad_procedureturn) {
                                saveCalcResultProcedureTurn(testInstance, last_p)
                            }
                            check_procedureturn_nextgate = false
                        }
                        gate_pos++
                        gate_pos_changed = true
                        procedureturn_track_changes = 0
                    }
                    
                    if (gate_pos >= coursecheck_gate_list.size()) {
                        break
                    }
                    
                    gate = coursecheck_gate_list[gate_pos]
                    last_gate = coursecheck_gate_list[gate_pos-1]
                    Map next_gate = null
                    if (gate_pos + 1 < coursecheck_gate_list.size()) {
                        next_gate = coursecheck_gate_list[gate_pos+1]
                    }
                    if (gate_pos_changed) {
                        check_procedureturn = gate.procedureTurn
                        println "Analyze ${gate.coordType}${gate.coordTypeNumber} ($gate_pos): ${gate.gateUtc}, ${gate.gateTrack} Grad, PT: ${check_procedureturn}"
                        gate_pos_changed = false
                    }
                        
                    if (gate.coordType.IsBadCourseCheckCoord()) {
                        int after_lastgate_seconds = FcTime.UTCTimeDiffSeconds(last_gate.gateUtc, p.utc)
                        //println "Analyze ${gate.coordType}${gate.coordTypeNumber}, From last gate:${after_lastgate_seconds}s, PT:$check_procedureturn"
                        if (after_lastgate_seconds > 0) {
                            if (check_procedureturn) { // check bad turn 
                                if ((after_lastgate_seconds < PROCEDURETURN_SECONDS) && !isGateTrackReached(last_track, gate.gateTrack, p.track, true)) {
                                    BigDecimal change_track = FcMath.GradDiff(last_track, p.track)
                                    procedureturn_track_changes += change_track
                                    check_procedureturn_nextgate = true
                                    //println "Procedure Turn (1) ${gate.coordType}${gate.coordTypeNumber} ${gate.gateTrack} Grad ${after_lastgate_seconds}s, Kurs ${p.track} Grad, Change ${change_track} Grad, Summary ${procedureturn_track_changes} Grad"
                                } else {
                                    BigDecimal change_track = FcMath.GradDiff(last_track, p.track)
                                    procedureturn_track_changes += change_track
                                    boolean is_bad_procedureturn = isBadProcedureTurn(procedureturn_track_changes, last_gate.gateTrack, gate.gateTrack)
                                    println "Procedure Turn (1) ${gate.coordType}${gate.coordTypeNumber}: BT:${is_bad_procedureturn}, Changes:${procedureturn_track_changes} Grad"
                                    if (is_bad_procedureturn) {
                                        saveCalcResultProcedureTurn(testInstance, p)
                                    }
                                    procedureturn_track_changes = 0
                                    check_procedureturn = false
                                    check_procedureturn_nextgate = false
                                }
                            } else { // check bad course
                                boolean is_bad_course = false
                                
                                if (!isCourseOk(gate.gateTrack, p.track)) {
                                    is_bad_course = true
                                    //println "After Gate ${last_gate.coordType}${last_gate.coordTypeNumber} ${last_gate.gateTrack}: ${after_lastgate_seconds}s, To Next Gate ${gate.coordType}${gate.coordTypeNumber} ${gate.gateTrack}"
                                }
                                
                                if (!gate.procedureTurn && is_bad_course) {
                                    int to_nextgate_seconds = FcTime.UTCTimeDiffSeconds(p.utc, gate.gateUtc)
                                    if (!last_gate.gateFlyBy && (after_lastgate_seconds < LASTGATE_SECONDS)) {
                                            // tolerate last track after successful gate
                                        is_bad_course = false
                                        if (!isCourseOk(last_gate.gateTrack, p.track)) {
                                            is_bad_course = true
                                        }
                                        // println "  Calculate From Gate $is_bad_course"
                                    } else if (gate.gateFlyBy && (next_gate != null) && (to_nextgate_seconds < NEXTGATE_SECONDS)) {
                                            // tolerate new track before additional gate after failed gate
                                        is_bad_course = false
                                        if (!isCourseOk(next_gate.gateTrack, p.track)) {
                                            is_bad_course = true
                                        }
                                        // println "  Calculate To Next Gate $is_bad_course"
                                    }
                                }
                                
                                // save bad course
                                if (is_bad_course) {
                                    badcourse_points += p
                                    check_course_nextgate = true
                                } else if (badcourse_points.size() > 0) {
                                    saveCalcResultBadCourse(testInstance, badcourse_points)
                                    badcourse_points.clear()
                                    check_course_nextgate = false
                                }
                            }
                        }
                    }
                    last_track = p.track
                }
                last_p = p
            }
        } else {
            println "No bad course gates found."
        }
        printdone ""
        
        printdone ""
    }

    //--------------------------------------------------------------------------
    private Map calculateGateCrossings(Map trackPoint1, Map trackPoint2, List gateList)
    //   y = Latitude: Geographische Breite (-90 ... +90 Grad)
    //   x = Longitude: Geographische Laenge (-179.999 ... +180 Grad)
    {
        Map ret = [gateFound:false, additionalGateFound:false, onGateLine:false, onAdvancedGateLine:false, lastPointNearer:false]
        
        boolean analyze_default_gate = true
        boolean run_exception = false
        for (Map g in gateList) {
            if (isGateTrackOk(g.gateTrack, trackPoint2.track)) {
                Map track_line = [x1:trackPoint1.longitude, y1:trackPoint1.latitude, x2:trackPoint2.longitude, y2:trackPoint2.latitude, track:trackPoint2.track, utc:trackPoint2.utc]
                Map gate_line = [x1:g.coordLeft.lon, y1:g.coordLeft.lat, x2:g.coordRight.lon, y2:g.coordRight.lat, track:g.gateTrack, type:g.coordType, nr:g.coordTypeNumber]
                Map advanced_gate_line = [x1:g.advancedCoordLeft.lon, y1:g.advancedCoordLeft.lat, x2:g.advancedCoordRight.lon, y2:g.advancedCoordRight.lat, track:g.gateTrack, type:g.coordType, nr:g.coordTypeNumber]
                /*
                if (analyze_default_gate) {
                    println "  Analyze Track (default gate): $track_line $gate_line"
                } else {
                    println "  Analyze Track (additional gate): $track_line $gate_line"
                }
                */
                Map line_crossed = isLineCrossed(track_line, gate_line, advanced_gate_line)
                if (line_crossed.runException) {
                    run_exception = true
                }
                boolean is_gate_line_crossed = false
                switch (g.coordType) {
                    case CoordType.TO:
                    case CoordType.LDG:
                    case CoordType.iTO:
                    case CoordType.iLDG:
                        is_gate_line_crossed = line_crossed.onTrackLine && line_crossed.onGateLine
                        break
                    case CoordType.SP:
                    case CoordType.iSP:
                        is_gate_line_crossed = line_crossed.onTrackLine && line_crossed.onAdvancedGateLine
                        break
                    default:
                        if (analyze_default_gate) {
                            is_gate_line_crossed = line_crossed.onTrackLine
                        } else {
                            is_gate_line_crossed = line_crossed.onTrackLine && line_crossed.onAdvancedGateLine
                        }
                        break
                }
                if (is_gate_line_crossed) {
                    ret.coordType = g.coordType
                    ret.coordTypeNumber  = g.coordTypeNumber
                    if (analyze_default_gate) {
                        ret.gateFound = true
                    } else {
                        ret.additionalGateFound = true
                    }
                    ret.onGateLine = line_crossed.onGateLine
                    ret.onAdvancedGateLine = line_crossed.onAdvancedGateLine
                    ret.lastPointNearer = line_crossed.lastPointNearer
                    break
                }
            }
            analyze_default_gate = false
            if (run_exception) {
                //println "  Exception at $trackPoint1 $trackPoint2"
            }
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map isLineCrossed(Map trackLine, Map gateLine, Map advancedGateLine)
    {
        Map ret = [onTrackLine:false, onGateLine:false, onAdvancedGateLine:false, lastPointNearer:false, runException:false]
        
        try {
            Map cross_point = getCrossPoint(gateLine, trackLine)
        
            ret.onTrackLine = isPointOnLine(cross_point, trackLine, "Track")
            ret.onGateLine = isPointOnLine(cross_point, gateLine, "Gate")
            ret.onAdvancedGateLine = isPointOnLine(cross_point, advancedGateLine, "AdvancedGate")
                        
            if (ret.onTrackLine) {
                BigDecimal dist_1 = getPointDistance(cross_point.x, cross_point.y, trackLine.x1, trackLine.y1)
                BigDecimal dist_2 = getPointDistance(cross_point.x, cross_point.y, trackLine.x2, trackLine.y2)
                if (dist_1 < dist_2) {
                    ret.lastPointNearer = true
                }
            }
            
            /*
            if (ret.onTrackLine) {
                println "isLineCrossed: onGateLine: $ret.onGateLine, ${RouteGradStr2(cross_point.x)}, ${RouteGradStr2(cross_point.y)}"
            } else {
                println "Not isLineCrossed: ${RouteGradStr2(cross_point.x)}, ${RouteGradStr2(cross_point.y)}"
            }
            */
        } catch (Exception e) {
            ret.runException = true
        }
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    private boolean isPointOnLine(Map pointValue, Map lineValue, String s)
    {
        boolean on_gate_x = false
        if (lineValue.x2 > lineValue.x1) {
            on_gate_x = (lineValue.x1 <= pointValue.x) && (pointValue.x <= lineValue.x2)
        } else { // lineValue.x1 > lineValue.x2
            on_gate_x = (lineValue.x2 <= pointValue.x) && (pointValue.x <= lineValue.x1)
        }
        
        boolean on_gate_y = false
        if (lineValue.y2 > lineValue.y1) {
            on_gate_y = (lineValue.y1 <= pointValue.y) && (pointValue.y <= lineValue.y2)
        } else { // lineValue.y1 > lineValue.y2
            on_gate_y = (lineValue.y2 <= pointValue.y) && (pointValue.y <= lineValue.y1)
        }

        /*        
        if (s == "Track") {
            println "isPointOnLine $s, ${on_gate_x && on_gate_y}, x:$on_gate_x, y:$on_gate_y, Point: ${RouteGradStr2(pointValue.x)}, ${RouteGradStr2(pointValue.y)}, Line: ${RouteGradStr2(lineValue.x1)}, ${RouteGradStr2(lineValue.y1)}, ${RouteGradStr2(lineValue.x2)}, ${RouteGradStr2(lineValue.y2)},   $pointValue.y, $lineValue.y1, $lineValue.y2"
        }
        */
        
        return on_gate_x && on_gate_y
    }
    
    //--------------------------------------------------------------------------
    private boolean isBadProcedureTurn(BigDecimal trackChanges, BigDecimal lastGateTrack, BigDecimal gateTrack)
    {
        boolean is_bad_procedureturn = false
        BigDecimal gate_track_change = FcMath.GradDiff(lastGateTrack, gateTrack)
        BigDecimal gate_track_change1 = gate_track_change
        if (gate_track_change > 0) {
            gate_track_change -= 360
        } else {
            gate_track_change += 360
        }
        BigDecimal diff_gate_track = gate_track_change - trackChanges
        //println "XX $lastGateTrack $gateTrack $gate_track_change $gate_track_change1 - $trackChanges - $diff_gate_track"
        if (diff_gate_track < -PROCEDURETURN_MAXDIFFGRAD || diff_gate_track > PROCEDURETURN_MAXDIFFGRAD) {
            is_bad_procedureturn = true
        }
        return is_bad_procedureturn
    }
    
    //--------------------------------------------------------------------------
    private boolean isGateTrackReached(BigDecimal lastTrack, BigDecimal gateTrack, BigDecimal nextTrack, boolean logOn)
    // 
    {
        boolean track_reached = false
        BigDecimal last_gate_diff = FcMath.GradDiff(lastTrack, gateTrack)
        BigDecimal gate_next_diff = FcMath.GradDiff(gateTrack, nextTrack)
        BigDecimal track_diff = FcMath.GradDiff(lastTrack, nextTrack)
        if (last_gate_diff > 0 && last_gate_diff < 30 && gate_next_diff > 0 && gate_next_diff < 30) {
            track_reached = true
        } else if (last_gate_diff < 0 && last_gate_diff > -30 && gate_next_diff < 0 && gate_next_diff > -30) {
            track_reached = true
        } else if (last_gate_diff == 0 || gate_next_diff == 0) {
            track_reached = true
        }
        //println "  XX $last_gate_diff $gate_next_diff $track_diff"
        if (logOn && track_reached) {
            println "  isGateTrackReached: Last:$lastTrack, Gate:$gateTrack, Next:$nextTrack, "
        }
        return track_reached
    }
    
    //--------------------------------------------------------------------------
    private boolean saveCalcResultGate(Test testInstance, Map result, Map last_p, Map p)
    // Return: true, if gate missed
    {
        CalcResult calc_result = new CalcResult()
        calc_result.loggerresult = testInstance.loggerResult
        if (result.lastPointNearer) {
            calc_result.utc = last_p.utc
            calc_result.latitude = last_p.latitude
            calc_result.longitude = last_p.longitude
            calc_result.altitude = last_p.altitude
        } else {
            calc_result.utc = p.utc
            calc_result.latitude = p.latitude
            calc_result.longitude = p.longitude
            calc_result.altitude = p.altitude
        }
        calc_result.coordTitle = new CoordTitle(result.coordType, result.coordTypeNumber)
        if (result.additionalGateFound) {
            calc_result.gateMissed = true
            calc_result.gateFlyBy = true
        } else {
            calc_result.gateMissed = !result.onGateLine
        }
        if (calc_result.gateMissed) {
            println "  Gate missed: ${calc_result.utc}"
        }
        if (calc_result.gateFlyBy) {
            println "  Gate fly-by: ${calc_result.utc}"
        }
        calc_result.save()
        return calc_result.gateMissed
    }
    
    //--------------------------------------------------------------------------
    private void saveCalcResultGateNotFound(Test testInstance, Map gateInstance, String utcValue)
    {
        CalcResult calc_result = new CalcResult()
        calc_result.loggerresult = testInstance.loggerResult
        calc_result.coordTitle = new CoordTitle(gateInstance.coordType, gateInstance.coordTypeNumber)
        calc_result.utc = utcValue
        calc_result.latitude = gateInstance.gateLatitude
        calc_result.longitude = gateInstance.gateLongitude
        calc_result.altitude = gateInstance.gateAltitude
        calc_result.gateNotFound = true
        println "  Gate not found: ${utcValue}"
        calc_result.save()
    }
    
    //--------------------------------------------------------------------------
    private void saveCalcResultProcedureTurn(Test testInstance, Map p)
    {
        CalcResult calc_result = new CalcResult()
        calc_result.loggerresult = testInstance.loggerResult
        calc_result.utc = p.utc
        calc_result.latitude = p.latitude
        calc_result.longitude = p.longitude
        calc_result.altitude = p.altitude
        calc_result.badTurn = true
        println "  Bad turn: ${p.utc}"
        calc_result.save()
    }
    
    //--------------------------------------------------------------------------
    private void saveCalcResultBadCourse(Test testInstance, List badCoursePoints)
    {
        boolean first = true
        boolean no_bad_course = false
        String 
        for (Map badcourse_point in badCoursePoints) {
            CalcResult calc_result = new CalcResult()
            calc_result.loggerresult = testInstance.loggerResult
            calc_result.utc = badcourse_point.utc
            calc_result.latitude = badcourse_point.latitude
            calc_result.longitude = badcourse_point.longitude
            calc_result.altitude = badcourse_point.altitude
            calc_result.badCourse = true
            if (first) {
                calc_result.badCourseSeconds = badCoursePoints.size()
                if (calc_result.badCourseSeconds <= testInstance.GetFlightTestBadCourseCorrectSecond()) {
                    no_bad_course = true
                }
                if (no_bad_course) {
                    println "  Bad course (No): ${calc_result.utc} (${calc_result.badCourseSeconds}s)"
                } else {
                    println "  Bad course (Yes): ${calc_result.utc} (${calc_result.badCourseSeconds}s)"
                }
            }
            calc_result.noBadCourse = no_bad_course
            calc_result.save()
            first = false
        }
    }
    
    //--------------------------------------------------------------------------
    String RouteGradStr(BigDecimal gradValue)
    {
        DecimalFormat df = new DecimalFormat("0.000")
        return df.format(gradValue)
    }
    
    //--------------------------------------------------------------------------
    String RouteGradStr2(BigDecimal gradValue)
    {
        DecimalFormat df = new DecimalFormat("0.00000000")
        return df.format(gradValue)
    }
    
    //--------------------------------------------------------------------------
    private Map getLineParams(Map line)
    {
        Map ret = [:]
        if (line.x1 == line.x2) {
            ret.A = 1
            ret.B = 0
            ret.C = -1 * line.x1
        } else if (line.y1 == line.y2) {
            ret.A = 0
            ret.B = 1
            ret.C = -1 * line.y1
        } else {
            ret.A = -1 * ((line.y2 - line.y1) / (line.x2 - line.x1))
            ret.B = 1
            ret.C = (line.x1 * (line.y2 - line.y1) / (line.x2 - line.x1)) - line.y1
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map getCrossPoint(Map gateLine, Map trackLine)
    {
        Map l1 = getLineParams(gateLine)
        Map l2 = getLineParams(trackLine)
        
        BigDecimal x = getDeterminate(l1.B, l1.C, l2.B, l2.C) / getDeterminate(l1.A, l1.B, l2.A, l2.B)
        BigDecimal y = getDeterminate(l1.C, l1.A, l2.C, l2.A) / getDeterminate(l1.A, l1.B, l2.A, l2.B)

        BigDecimal x1 = x.setScale(COORDINATE_SCALE, RoundingMode.HALF_EVEN) 
        BigDecimal y1 = y.setScale(COORDINATE_SCALE, RoundingMode.HALF_EVEN)
        
        return [x:x1, y:y1]
    }
    
    //--------------------------------------------------------------------------
    private BigDecimal getDeterminate(BigDecimal a11, BigDecimal a12, BigDecimal a21, BigDecimal a22)
    {
        return (a11 * a22) - (a21 * a12)
    }
    
    //--------------------------------------------------------------------------
    private BigDecimal getPointDistance(BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2)
    {
        BigDecimal xd = x2 - x1
        BigDecimal yd = y2 - y1
        return Math.sqrt( xd * xd + yd * yd)
    }
    
    //--------------------------------------------------------------------------
    private boolean isGateTrackOk(BigDecimal gateTrack, BigDecimal flightTrack)
    {
        boolean ok = false
        if (flightTrack >= 0 && flightTrack < 360) {
            if (gateTrack >= 0 && gateTrack < 90) { 
                 if (flightTrack > gateTrack + 270) {
                     ok = true
                 } else if (flightTrack < gateTrack + 90) {
                     ok = true
                 }
            } else if (gateTrack >= 90 && gateTrack < 270) {
                if (gateTrack - 90 < flightTrack && flightTrack < gateTrack + 90) {
                    ok = true
                }
            } else if (gateTrack >= 270 && gateTrack < 360) {
                if (gateTrack - 90 < flightTrack) {
                    ok = true
                } else if (flightTrack < gateTrack - 270) {
                    ok = true
                }
            }
        }
        return ok
    }
    
    //--------------------------------------------------------------------------
    private boolean isCourseOk(BigDecimal gateTrack, BigDecimal flightTrack)
    {
        boolean ok = false
        if (flightTrack >= 0 && flightTrack < 360) {
            if (gateTrack >= 0 && gateTrack < 90) { 
                 if (flightTrack >= gateTrack + 270) {
                     ok = true
                 } else if (flightTrack <= gateTrack + 90) {
                     ok = true
                 }
            } else if (gateTrack >= 90 && gateTrack < 270) {
                if (gateTrack - 90 <= flightTrack && flightTrack <= gateTrack + 90) {
                    ok = true
                }
            } else if (gateTrack >= 270 && gateTrack < 360) {
                if (gateTrack - 90 <= flightTrack) {
                    ok = true
                } else if (flightTrack <= gateTrack - 270) {
                    ok = true
                }
            }
        }
        return ok
    }
    
    //--------------------------------------------------------------------------
    private Map getAnalyzeGates(List gateList, int firstGatePos)
    {
        Map ret = [gates:[], lastGatePos:firstGatePos]
        
        if (firstGatePos < gateList.size()) {
            int add_gate_num = 1
            for (int i = firstGatePos; i < gateList.size(); i++) {
                if (add_gate_num > 1) {
                    if (!gateList[i].additionalGate) {
                        break
                    }
                }
                ret.gates += gateList[i]
                ret.lastGatePos = i
                add_gate_num++
            }
        }
        
        println "Analyze gates: $ret"
        
        return ret 
    }
    
    //--------------------------------------------------------------------------
    private List getGates(Test testInstance)
    {
        List gates = []
        
        FlightTestWind flighttestwind_instance = testInstance.flighttestwind
        FlightTest flighttest_instance = flighttestwind_instance.flighttest
        Route route_instance = flighttest_instance.route
        
        CoordRoute last_coordroute_instance = null
        boolean add_sp_gate = true
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route_instance,[sort:'id'])) {
            switch (coordroute_instance.type) {
                case CoordType.TO:
                    Map gate = AviationMath.getGate(
                        coordroute_instance.latMath(),coordroute_instance.lonMath(),
                        flighttestwind_instance.TODirection,
                        flighttestwind_instance.TOOffset,
                        flighttestwind_instance.TOOrthogonalOffset,
                        coordroute_instance.gatewidth2
                    )
                    Map advanced_gate = AviationMath.getGate(
                        coordroute_instance.latMath(),coordroute_instance.lonMath(),
                        flighttestwind_instance.TODirection,
                        flighttestwind_instance.TOOffset,
                        flighttestwind_instance.TOOrthogonalOffset,
                        ADVANCED_GATE_WIDTH
                    )
                    Map new_gate = [coordType:coordroute_instance.type, 
                                    coordTypeNumber:coordroute_instance.titleNumber,
                                    coordLeft:gate.coordLeft,
                                    coordRight:gate.coordRight,
                                    advancedCoordLeft:advanced_gate.coordLeft,
                                    advancedCoordRight:advanced_gate.coordRight,
                                    gateTrack:FcMath.RoundGrad(gate.gateTrack),
                                    gateWidth:coordroute_instance.gatewidth2,
                                    gateLatitude:gate.coord.lat,
                                    gateLongitude:gate.coord.lon,
                                    gateAltitude:coordroute_instance.GetMinAltitudeAboveGround(route_instance.altitudeAboveGround),
                                    procedureTurn:false,
                                    additionalGate:false]
                    gates += new_gate
                    break
                case CoordType.LDG:
                    Map gate = AviationMath.getGate(
                        coordroute_instance.latMath(),coordroute_instance.lonMath(),
                        flighttestwind_instance.LDGDirection,
                        flighttestwind_instance.LDGOffset,
                        flighttestwind_instance.LDGOrthogonalOffset,
                        coordroute_instance.gatewidth2
                    )
                    Map advanced_gate = AviationMath.getGate(
                        coordroute_instance.latMath(),coordroute_instance.lonMath(),
                        flighttestwind_instance.LDGDirection,
                        flighttestwind_instance.LDGOffset,
                        flighttestwind_instance.LDGOrthogonalOffset,
                        ADVANCED_GATE_WIDTH
                    )
                    Map new_gate = [coordType:coordroute_instance.type,
                                    coordTypeNumber:coordroute_instance.titleNumber,
                                    coordLeft:gate.coordLeft,
                                    coordRight:gate.coordRight,
                                    advancedCoordLeft:advanced_gate.coordLeft,
                                    advancedCoordRight:advanced_gate.coordRight,
                                    gateTrack:FcMath.RoundGrad(gate.gateTrack),
                                    gateWidth:coordroute_instance.gatewidth2,
                                    gateLatitude:gate.coord.lat,
                                    gateLongitude:gate.coord.lon,
                                    gateAltitude:coordroute_instance.GetMinAltitudeAboveGround(route_instance.altitudeAboveGround),
                                    procedureTurn:false,
                                    additionalGate:false]
                    gates += new_gate
                    break
                case CoordType.iTO:
                case CoordType.iLDG:
                    Map gate = AviationMath.getGate(
                        coordroute_instance.latMath(),coordroute_instance.lonMath(),
                        flighttestwind_instance.iTOiLDGDirection,
                        flighttestwind_instance.iTOiLDGOffset,
                        flighttestwind_instance.iTOiLDGOrthogonalOffset,
                        coordroute_instance.gatewidth2
                    )
                    Map advanced_gate = AviationMath.getGate(
                        coordroute_instance.latMath(),coordroute_instance.lonMath(),
                        flighttestwind_instance.iTOiLDGDirection,
                        flighttestwind_instance.iTOiLDGOffset,
                        flighttestwind_instance.iTOiLDGOrthogonalOffset,
                        ADVANCED_GATE_WIDTH
                    )
                    Map new_gate = [coordType:coordroute_instance.type,
                                    coordTypeNumber:coordroute_instance.titleNumber,
                                    coordLeft:gate.coordLeft, 
                                    coordRight:gate.coordRight,
                                    advancedCoordLeft:advanced_gate.coordLeft, 
                                    advancedCoordRight:advanced_gate.coordRight,
                                    gateTrack:FcMath.RoundGrad(gate.gateTrack),
                                    gateWidth:coordroute_instance.gatewidth2,
                                    gateLatitude:gate.coord.lat,
                                    gateLongitude:gate.coord.lon,
                                    gateAltitude:coordroute_instance.GetMinAltitudeAboveGround(route_instance.altitudeAboveGround),
                                    procedureTurn:false,
                                    additionalGate:false]
                    gates += new_gate
                    break
            }
            if (last_coordroute_instance && last_coordroute_instance.type.IsCpCheckCoord() && coordroute_instance.type.IsCpCheckCoord()) {
                if (add_sp_gate) {
                    Map start_gate = AviationMath.getGate(
                        coordroute_instance.latMath(),coordroute_instance.lonMath(),
                        last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                        last_coordroute_instance.gatewidth2
                    )
                    Map advanced_start_gate = AviationMath.getGate(
                        coordroute_instance.latMath(),coordroute_instance.lonMath(),
                        last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                        ADVANCED_GATE_WIDTH
                    )
                    Map new_gate = [coordType:last_coordroute_instance.type, 
                                    coordTypeNumber:last_coordroute_instance.titleNumber, 
                                    coordLeft:start_gate.coordLeft, 
                                    coordRight:start_gate.coordRight, 
                                    advancedCoordLeft:advanced_start_gate.coordLeft, 
                                    advancedCoordRight:advanced_start_gate.coordRight, 
                                    gateTrack:FcMath.RoundGrad(AviationMath.getDiametricalTrack(start_gate.gateTrack)),
                                    gateWidth:last_coordroute_instance.gatewidth2,
                                    gateLatitude:last_coordroute_instance.latMath(),
                                    gateLongitude:last_coordroute_instance.lonMath(),
                                    gateAltitude:last_coordroute_instance.GetMinAltitudeAboveGround(route_instance.altitudeAboveGround),
                                    procedureTurn:false,
                                    additionalGate:false
                                   ]
                    gates += new_gate
                }
                
                // additional gates
                switch (last_coordroute_instance.type) {
                    case CoordType.SP:
                    case CoordType.iSP:
                        BigDecimal gate_distance = AviationMath.calculateLeg(
                            last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                            coordroute_instance.latMath(),coordroute_instance.lonMath()
                        ).dis
                        if (gate_distance > NEXTGATE_DISTANCE) {
                            gates += getAddionalGate(last_coordroute_instance, coordroute_instance, NEXTGATE_DISTANCE)
                        }
                        if (gate_distance / 4 > NEXTGATE_DISTANCE) {
                            gates += getAddionalGate(last_coordroute_instance, coordroute_instance, gate_distance / 4)
                        }
                        if (gate_distance / 2 > NEXTGATE_DISTANCE) {
                            gates += getAddionalGate(last_coordroute_instance, coordroute_instance, gate_distance / 2)
                        }
                        break
                    case CoordType.TP:
                        if (route_instance.useProcedureTurns && coordroute_instance.planProcedureTurn) {
                            BigDecimal gate_distance = AviationMath.calculateLeg(
                                last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                                coordroute_instance.latMath(),coordroute_instance.lonMath()
                            ).dis
                            if (gate_distance / 2 > NEXTGATE_DISTANCE) {
                                gates += getAddionalGate(last_coordroute_instance, coordroute_instance, gate_distance / 2)
                            }
                        } else {
                            gates += getAddionalGate(last_coordroute_instance, coordroute_instance, NEXTGATE_DISTANCE)
                        }
                        break
                }
                
                if ((coordroute_instance.type == CoordType.iSP) && (last_coordroute_instance.type == CoordType.iFP)) {
                    // no standard gate
                } else {
                    // standard gate
                    Float gate_width = null
                    if (testInstance && coordroute_instance.type == CoordType.SECRET) {
                        gate_width = testInstance.GetSecretGateWidth()
                    }
                    if (!gate_width) {
                        gate_width = coordroute_instance.gatewidth2
                    }
                    Map gate = AviationMath.getGate(
                        last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                        coordroute_instance.latMath(),coordroute_instance.lonMath(),
                        gate_width
                    )
                    Map advanced_gate = AviationMath.getGate(
                        last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                        coordroute_instance.latMath(),coordroute_instance.lonMath(),
                        ADVANCED_GATE_WIDTH
                    )
                    boolean procedure_turn = route_instance.useProcedureTurns && coordroute_instance.planProcedureTurn && (testInstance.task.procedureTurnDuration > 0)
                    Map new_gate = [coordType:coordroute_instance.type, 
                                    coordTypeNumber:coordroute_instance.titleNumber,
                                    coordLeft:gate.coordLeft,
                                    coordRight:gate.coordRight,
                                    advancedCoordLeft:advanced_gate.coordLeft,
                                    advancedCoordRight:advanced_gate.coordRight,
                                    gateTrack:FcMath.RoundGrad(gate.gateTrack),
                                    gateWidth:gate_width,
                                    gateLatitude:coordroute_instance.latMath(),
                                    gateLongitude:coordroute_instance.lonMath(),
                                    gateAltitude:coordroute_instance.GetMinAltitudeAboveGround(route_instance.altitudeAboveGround),
                                    procedureTurn:procedure_turn,
                                    additionalGate:false
                                   ]
                    gates += new_gate
                }
                
                add_sp_gate = false
            }
            if (coordroute_instance.type == CoordType.iSP) {
                add_sp_gate = true
            }
            last_coordroute_instance = coordroute_instance
        }
        
        return gates
    }
    
    //--------------------------------------------------------------------------
    Map getAddionalGate(CoordRoute lastCoordRouteInstance, CoordRoute coordRouteInstance, BigDecimal nextGateDistance)
    {        
        Map add_gate = AviationMath.getGateAtDistance(
            lastCoordRouteInstance.latMath(),lastCoordRouteInstance.lonMath(),
            coordRouteInstance.latMath(),coordRouteInstance.lonMath(),
            nextGateDistance,
            lastCoordRouteInstance.gatewidth2
        )
        Map advanced_add_gate = AviationMath.getGateAtDistance(
            lastCoordRouteInstance.latMath(),lastCoordRouteInstance.lonMath(),
            coordRouteInstance.latMath(),coordRouteInstance.lonMath(),
            nextGateDistance,
            ADVANCED_GATE_WIDTH
        )
        Map new_add_gate = [coordType:lastCoordRouteInstance.type, 
                            coordTypeNumber:lastCoordRouteInstance.titleNumber,
                            coordLeft:add_gate.coordLeft,
                            coordRight:add_gate.coordRight,
                            advancedCoordLeft:advanced_add_gate.coordLeft,
                            advancedCoordRight:advanced_add_gate.coordRight,
                            gateTrack:FcMath.RoundGrad(add_gate.gateTrack),
                            gateWidth:lastCoordRouteInstance.gatewidth2,
                            gateLatitude:lastCoordRouteInstance.latMath(),
                            gateLongitude:lastCoordRouteInstance.lonMath(),
                            gateAltitude:lastCoordRouteInstance.GetMinAltitudeAboveGround(lastCoordRouteInstance.route.altitudeAboveGround),
                            procedureTurn:false,
                            additionalGate:true
                           ]
        return new_add_gate
    }
    
    //--------------------------------------------------------------------------
    private List getCourseCheckGates(Test testInstance, List gateList)
    {
        List gates = []
        for (Map g in gateList) {
            if (!g.additionalGate && g.coordType.IsCpTimeCheckCoord()) {
                String gate_utc = ""
                boolean gate_flyby = false
                for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresultAndGateNotFound(testInstance.loggerResult,false,[sort:'utc'])) {
                    if (calcresult_instance.IsCoordTitleEqual(g.coordType, g.coordTypeNumber)) {
                        gate_utc = calcresult_instance.utc
                        gate_flyby = calcresult_instance.gateFlyBy
                        break
                    }
                }
                if (gate_utc) {
                    Map new_gate = [coordType:g.coordType,
                                    coordTypeNumber:g.coordTypeNumber,
                                    gateTrack:g.gateTrack,
                                    gateUtc:gate_utc,
                                    gateFlyBy:gate_flyby,
                                    procedureTurn:g.procedureTurn
                                   ]
                    gates += new_gate
                } else {
                    println "getCourseCheckGates: Gate ${g.coordType} ${g.coordTypeNumber} not found."
                }
            }
        }
        return gates
    }
    
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
