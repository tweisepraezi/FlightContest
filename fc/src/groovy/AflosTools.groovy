import java.math.*
import java.text.*
import java.util.List;
import java.util.Map;

class AflosTools
{
    private static final String PRAEFIX_CHECKPOINTS = "P"
    private static final String PRAEFIX_ERRORPOINTS = "-Bad "
    
    final static boolean REMOVE_IDENTICAL_TIMES = true
    
    //--------------------------------------------------------------------------
    static boolean ExistAnyAflosRoute(Contest contestInstance)
    {
        if (contestInstance.aflosTest) {
            if (AflosRouteNames.aflostest.count() < 2) {
                return false
            }
        } else if (contestInstance.aflosUpload) {
            if (AflosRouteNames.aflosupload.count() < 2) {
                return false
            }
        } else {
            if (AflosRouteNames.aflos.count() < 2) {
                return false
            }
        }
        return true
    }
    
    //--------------------------------------------------------------------------
    static AflosRouteNames GetAflosRouteName(Contest contestInstance, String aflosRouteName)
    {
        AflosRouteNames aflosroutenames_instance = null
        if (contestInstance.aflosTest) {
            aflosroutenames_instance = AflosRouteNames.aflostest.findByName(aflosRouteName)
        } else if (contestInstance.aflosUpload) {
            aflosroutenames_instance = AflosRouteNames.aflosupload.findByName(aflosRouteName)
        } else {
            aflosroutenames_instance = AflosRouteNames.aflos.findByName(aflosRouteName)
        }
        return aflosroutenames_instance
    }
    
    //--------------------------------------------------------------------------
    static AflosRouteNames GetAflosRouteName(Contest contestInstance, long aflosRouteId)
    {
        AflosRouteNames aflosroutenames_instance = null
        if (contestInstance.aflosTest) {
            aflosroutenames_instance = AflosRouteNames.aflostest.get(aflosRouteId)
        } else if (contestInstance.aflosUpload) {
            aflosroutenames_instance = AflosRouteNames.aflosupload.get(aflosRouteId)
        } else {
            aflosroutenames_instance = AflosRouteNames.aflos.get(aflosRouteId)
        }
        return aflosroutenames_instance
    }
    
    //--------------------------------------------------------------------------
    static List GetAflosRouteNames(Contest contestInstance)
    {
        if (contestInstance?.aflosTest) {
            return AflosRouteNames.aflostest.findAllByIdNotEqual(0,[sort:"id"])
        } else if (contestInstance?.aflosUpload) {
            return AflosRouteNames.aflosupload.findAllByIdNotEqual(0,[sort:"id"])
        }
        return AflosRouteNames.aflos.findAllByIdNotEqual(0,[sort:"id"])
    }

    //--------------------------------------------------------------------------
    static List GetAflosRouteNames2(Contest contestInstance)
    {
        if (contestInstance.aflosTest) {
            return AflosRouteNames.aflostest.findAllByNameIsNotNullAndIdNotEqual(0,[sort:"id"])
        } else if (contestInstance.aflosUpload) {
            return AflosRouteNames.aflosupload.findAllByNameIsNotNullAndIdNotEqual(0,[sort:"id"])
        }
        return AflosRouteNames.aflos.findAllByNameIsNotNullAndIdNotEqual(0,[sort:"id"])
    }

    //--------------------------------------------------------------------------
    static List GetAflosRouteDefs(Contest contestInstance, AflosRouteNames aflosRouteNamesInstance)
    {
        List aflosroutedefs_instances = null
        if (contestInstance.aflosTest) {
            aflosroutedefs_instances = AflosRouteDefs.aflostest.findAllByRoutename(aflosRouteNamesInstance,[sort:"id"])
        } else if (contestInstance.aflosUpload) {
            aflosroutedefs_instances = AflosRouteDefs.aflosupload.findAllByRoutename(aflosRouteNamesInstance,[sort:"id"])
        } else {
            aflosroutedefs_instances = AflosRouteDefs.aflos.findAllByRoutename(aflosRouteNamesInstance,[sort:"id"])
        }
        return aflosroutedefs_instances
    }
    
    //--------------------------------------------------------------------------
    static boolean ExistAnyAflosCrew(Contest contestInstance)
    {
        if (contestInstance.aflosTest) {
            if (AflosCrewNames.aflostest.countByPointsNotEqual(0) > 0) {
                return true
            }
        } else if (contestInstance.aflosUpload) {
            if (AflosCrewNames.aflosupload.countByPointsNotEqual(0) > 0) {
                return true
            }
        } else {
            if (AflosCrewNames.aflos.countByPointsNotEqual(0) > 0) {
                return true
            }
        }
        return false
    }
    
    //--------------------------------------------------------------------------
    static boolean ExistAnyAflosCheckPoints(Contest contestInstance, String aflosRouteName)
    {
        AflosRouteNames aflos_route_name = GetAflosRouteName(contestInstance, aflosRouteName)
        if (contestInstance.aflosTest) {
            if (AflosCheckPoints.aflostest.findByRoutename(aflos_route_name)) {
                return true
            }
        } else if (contestInstance.aflosUpload) {
            if (AflosCheckPoints.aflosupload.findByRoutename(aflos_route_name)) {
                return true
            }
        } else {
            if (AflosCheckPoints.aflos.findByRoutename(aflos_route_name)) {
                return true
            }
        }
        return false

    }
    
    //--------------------------------------------------------------------------
    static AflosCrewNames GetAflosCrewName(Contest contestInstance, int startNum)
    {
        AflosCrewNames afloscrewnames_instance = null
        if (contestInstance.aflosTest) {
            afloscrewnames_instance = AflosCrewNames.aflostest.findByStartnumAndPointsNotEqual(startNum,0)
        } else if (contestInstance.aflosUpload) {
            afloscrewnames_instance = AflosCrewNames.aflosupload.findByStartnumAndPointsNotEqual(startNum,0)
        } else {
            afloscrewnames_instance = AflosCrewNames.aflos.findByStartnumAndPointsNotEqual(startNum,0)
        }
        return afloscrewnames_instance
    }
    
    //--------------------------------------------------------------------------
    static List GetAflosCrewNames(Contest contestInstance, int startNum)
    {
        if (startNum <= 0) {
            return []
        } else if (contestInstance.aflosTest) {
            return AflosCrewNames.aflostest.findAllByStartnum(startNum,[sort:'id'])
        } else if (contestInstance.aflosUpload) {
            return AflosCrewNames.aflosupload.findAllByStartnum(startNum,[sort:'id'])
        }
        return AflosCrewNames.aflos.findAllByStartnum(startNum,[sort:'id'])
    }
    
    //--------------------------------------------------------------------------
    static List GetAflosCrewNames(Contest contestInstance)
    {
        if (contestInstance.aflosTest) {
            return AflosCrewNames.aflostest.findAllByPointsNotEqual(0,[sort:"id"])
        } else if (contestInstance.aflosUpload) {
            return AflosCrewNames.aflosupload.findAllByPointsNotEqual(0,[sort:"id"])
        }
        return AflosCrewNames.aflos.findAllByPointsNotEqual(0,[sort:"id"])
    }

    //--------------------------------------------------------------------------
    private static Map GetAflosCrewNamesPoint(String utcLastDateTime, AflosCrewNames afloscrewnamesInstance, int measurePoint, lastLatitude, lastLongitude)
    {
        int[] measure_point_data = new int[16]
        for (int j = 0; j < 16; j++) {
            measure_point_data[j] =  afloscrewnamesInstance.daten[64*measurePoint+4*j+3] << 24 |
                                    (afloscrewnamesInstance.daten[64*measurePoint+4*j+2] & 0xFF) << 16 |
                                    (afloscrewnamesInstance.daten[64*measurePoint+4*j+1] & 0xFF) << 8 |
                                    (afloscrewnamesInstance.daten[64*measurePoint+4*j+0] & 0xFF)
        }
        
        // UTC
        String utc_h = (measure_point_data[12] & 31).toString()
        if (utc_h.size() == 1) {
            utc_h = "0" + utc_h
        }
        String utc_min = (measure_point_data[14] & 63).toString()
        if (utc_min.size() == 1) {
            utc_min = "0" + utc_min
        }
        String utc_s = (measure_point_data[15] & 63).toString()
        if (utc_s.size() == 1) {
            utc_s = "0" + utc_s
        }
        String utc = FcTime.UTCGetNextDateTime(utcLastDateTime, "${utc_h}:${utc_min}:${utc_s}")
        
        // Latitude
        def latitude = measure_point_data[0] & 127
        def b1 = measure_point_data[2] & 63
        latitude = latitude + (b1 / 60)
        b1 = measure_point_data[4] & 127
        latitude = latitude + (b1 / 6000)
        b1 = measure_point_data[6] & 127
        latitude = latitude + (b1 / 600000)
        if ((measure_point_data[0] & 128) == 128) {
            latitude *= -1
        }
        
        // Longitude
        def longitude = measure_point_data[7]
        b1 = measure_point_data[9] & 63
        longitude = longitude + (b1 / 60)
        b1 = measure_point_data[11] & 127
        longitude = longitude + (b1 / 6000)
        b1 = measure_point_data[13] & 127
        longitude = longitude + (b1 / 600000)
        if ((measure_point_data[13] & 128) == 128) {
            longitude *= -1
        }
        
        // Speed in kn
        // def speed = (measure_point_data[9] & 192) * 4 + measure_point_data[10]
        // speed += (measure_point_data[8] & 15) / 10
        
        // Altitude in ft
        int altitude = measure_point_data[1]
        altitude *= 256
        altitude += measure_point_data[3]
        
        // Track in Grad
        def track = null
        if (lastLatitude != null && lastLongitude != null) {
            Map leg = AviationMath.calculateLeg(latitude,longitude,lastLatitude,lastLongitude)
            track = FcMath.RoundGrad(leg.dir)
        }

        return [utc:            utc,
                latitude:       latitude,
                longitude:      longitude,
                altitude:       altitude,
                altitude_meter: altitude / GpxService.ftPerMeter,
                track:          track
               ]
    }
    
    //--------------------------------------------------------------------------
    static List GetAflosCrewNamesTrackPoints(Test testInstance, int startNum)
    {
        List points = []
        List aflos_crew_names = AflosTools.GetAflosCrewNames(testInstance.task.contest,startNum)
        if (aflos_crew_names) {
            String last_utc = FcTime.UTC_GPX_DATE
            for (AflosCrewNames afloscrewnames_instance in aflos_crew_names) {
                Map last_point = [latitude:null, longitude:null]
                for (int measure_point = 0; measure_point < afloscrewnames_instance.measurePointsNum; measure_point++) {
                    Map new_point = GetAflosCrewNamesPoint(last_utc, afloscrewnames_instance, measure_point, last_point.latitude, last_point.longitude)
                    points += new_point
                    last_point = new_point
                    last_utc = new_point.utc
                }
            }
        }
        return points
    }
                        
    //--------------------------------------------------------------------------
    static boolean GetAflosCrewNamesLoggerData(Test testInstance, int startNum)
    // Return true: Track points found
    {
        boolean found = false
        
        // remove old track points
        if (testInstance.IsLoggerData()) {
            TrackPoint.findAllByLoggerdata(testInstance.loggerData,[sort:"id"]).each { TrackPoint trackpoint_instance ->
                trackpoint_instance.delete()
            }
        }
        
        List aflos_crew_names = AflosTools.GetAflosCrewNames(testInstance.task.contest,startNum)
        if (aflos_crew_names) {
            String last_utc = FcTime.UTC_GPX_DATE
            BigDecimal last_latitude = null
            BigDecimal last_longitude = null
            def last_track = null
            for (AflosCrewNames afloscrewnames_instance in aflos_crew_names) {
                Map last_point = [latitude:null, longitude:null]
                for (int measure_point = 0; measure_point < afloscrewnames_instance.measurePointsNum; measure_point++) {
                    boolean ignore_line = false
                    
                    Map new_point = GetAflosCrewNamesPoint(last_utc, afloscrewnames_instance, measure_point, last_point.latitude, last_point.longitude)
                    
                    if (REMOVE_IDENTICAL_TIMES) {
                        if (new_point.utc == last_utc) { // Zeile mit doppelter Zeit entfernen
                            ignore_line = true
                        }
                    }
                    
                    if (!ignore_line) {
                        if ((new_point.latitude == last_latitude) && (new_point.longitude == last_longitude)) {
                            new_point.track = last_track
                        }
                        
                        TrackPoint trackpoint_instance = new TrackPoint()
                        trackpoint_instance.loggerdata = testInstance.loggerData
                        trackpoint_instance.utc = new_point.utc
                        trackpoint_instance.latitude = new_point.latitude
                        trackpoint_instance.longitude = new_point.longitude
                        trackpoint_instance.altitude = new_point.altitude
                        trackpoint_instance.track = new_point.track
                        trackpoint_instance.save()
            
                        last_point = new_point
                        last_utc = new_point.utc
                        last_latitude = new_point.latitude
                        last_longitude = new_point.longitude
                        last_track = new_point.track
                        
                        found = true
                    }
                }
            }
        }
        
        return found
    }
                        
    //--------------------------------------------------------------------------
    static List GetAflosCheckPoints(Contest contestInstance, String aflosRouteName, int startNum)
    {
        List afloscheckpoints_instances = null
        AflosRouteNames aflosroutenames_instance = GetAflosRouteName(contestInstance,aflosRouteName)
        if (aflosroutenames_instance) {
            if (contestInstance.aflosTest) {
                afloscheckpoints_instances = AflosCheckPoints.aflostest.findAllByStartnumAndRoutename(startNum,aflosroutenames_instance,[sort:"id"])
            } else if (contestInstance.aflosUpload) {
                afloscheckpoints_instances = AflosCheckPoints.aflosupload.findAllByStartnumAndRoutename(startNum,aflosroutenames_instance,[sort:"id"])
            } else {
                afloscheckpoints_instances = AflosCheckPoints.aflos.findAllByStartnumAndRoutename(startNum,aflosroutenames_instance,[sort:"id"])
            }
        }
        return afloscheckpoints_instances
    }
    
    //--------------------------------------------------------------------------
    static void ImportAflosCalc(Test testInstance, int startNum, String alternateAflosRouteName)
    {
        // remove old calc results
        if (testInstance.IsLoggerResult()) {
            CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:"id"]).each { CalcResult calcresult_instance ->
                calcresult_instance.delete()
            }
        }
        
        Contest contest_instance = testInstance.crew.contest
        Route route_instance = testInstance.flighttestwind.flighttest.route
        String aflos_route_name = testInstance.flighttestwind.flighttest.route.mark
        if (alternateAflosRouteName) {
            aflos_route_name = alternateAflosRouteName
        }
        
        // add not missed check points to calc results
        String last_utc = FcTime.UTC_GPX_DATE
        GetAflosCheckPoints(contest_instance, aflos_route_name, startNum).each { AflosCheckPoints afloscheckpoints_instance ->
            CalcResult calc_result = new CalcResult()
            calc_result.loggerresult = testInstance.loggerResult
            
            calc_result.utc = ConvertAflosUTC(last_utc, afloscheckpoints_instance.utc)
            calc_result.latitude = ConvertAflosCoord(afloscheckpoints_instance.latitude)
            calc_result.longitude = ConvertAflosCoord(afloscheckpoints_instance.longitude)
            calc_result.altitude = ConvertAflosAltitude(afloscheckpoints_instance.altitude)
            calc_result.coordTitle = GetCoordTitleAflos(route_instance, afloscheckpoints_instance.mark, PRAEFIX_CHECKPOINTS)
            
            calc_result.save()
            
            last_utc = calc_result.utc
        }

        // add error points to calc results
        boolean bad_course = false
        boolean no_bad_course = false
        last_utc = FcTime.UTC_GPX_DATE
        GetAflosErrorPoints(contest_instance, aflos_route_name, startNum).each { AflosErrorPoints afloserrorpoints_instance ->
            boolean save_calc_result = false
            String error_info = afloserrorpoints_instance.mark.trim()
            
            CalcResult calc_result = new CalcResult()
            calc_result.loggerresult = testInstance.loggerResult
            
            calc_result.utc = ConvertAflosUTC(last_utc, afloserrorpoints_instance.utc)
            calc_result.latitude = ConvertAflosCoord(afloserrorpoints_instance.latitude)
            calc_result.longitude = ConvertAflosCoord(afloserrorpoints_instance.longitude)
            calc_result.altitude = ConvertAflosAltitude(afloserrorpoints_instance.altitude)
            
            // check Bad Course
            if (IsBadCourse(error_info)) {
                calc_result.badCourse = true
                calc_result.badCourseSeconds = GetBadCourseSeconds(error_info)
                if (calc_result.badCourseSeconds && (calc_result.badCourseSeconds <= testInstance.GetFlightTestBadCourseCorrectSecond())) {
                    calc_result.noBadCourse = true
                }
                save_calc_result = true
                if (calc_result.badCourseSeconds) {
                    bad_course = true
                    no_bad_course = calc_result.noBadCourse
                }
            }
            if (bad_course && !error_info) {
                calc_result.badCourse = true
                calc_result.noBadCourse = no_bad_course
                save_calc_result = true
            }
            
            // check Bad Turn
            if (IsBadTurn(error_info)) {
                calc_result.badTurn = true
                save_calc_result = true
                bad_course = false
                no_bad_course = false
            }
            
            // check Bad Height
            if (IsBadHeight(error_info)) {
                // no save
                bad_course = false
                no_bad_course = false
            }
            
            // check Bad CheckPoint
            if (IsBadTimeCheck(error_info)) {
                calc_result.gateMissed = true
                save_calc_result = true
                bad_course = false
                no_bad_course = false
                calc_result.coordTitle = GetCoordTitleAflos(route_instance, error_info, PRAEFIX_ERRORPOINTS)
            }
                
            if (save_calc_result) {
                calc_result.save()
            }
            
            last_utc = calc_result.utc
        }
    }

    //--------------------------------------------------------------------------
    private static List GetAflosErrorPoints(Contest contestInstance, String aflosRouteName, int startNum)
    {
        List afloserrorpoints_instances = null
        AflosRouteNames aflosroutenames_instance = GetAflosRouteName(contestInstance,aflosRouteName)
        if (aflosroutenames_instance) {
            if (contestInstance.aflosTest) {
                afloserrorpoints_instances = AflosErrorPoints.aflostest.findAllByStartnumAndRoutename(startNum,aflosroutenames_instance,[sort:"id"])
            } else if (contestInstance.aflosUpload) {
                afloserrorpoints_instances = AflosErrorPoints.aflosupload.findAllByStartnumAndRoutename(startNum,aflosroutenames_instance,[sort:"id"])
            } else {
                afloserrorpoints_instances = AflosErrorPoints.aflos.findAllByStartnumAndRoutename(startNum,aflosroutenames_instance,[sort:"id"])
            }
        }
        return afloserrorpoints_instances
    }
    
    //--------------------------------------------------------------------------
    private static CoordTitle GetCoordTitleAflos(Route routeInstance, String checkPointMark, String testPraefix)
    {
        if (checkPointMark.startsWith(testPraefix)) {
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:"id"])) {
                if (checkPointMark == "${testPraefix}${coordroute_instance.mark}") {
                    CoordTitle coord_title = new CoordTitle(coordroute_instance.type, coordroute_instance.titleNumber)
                    coord_title.save()
                    return coord_title
                }
            }
        }
        return null
    }
    
    //--------------------------------------------------------------------------
    private static boolean IsBadTimeCheck(String aflosErrorInfo)
    {
        if (aflosErrorInfo.contains("-Bad")) {
            String error_info_bad = aflosErrorInfo.substring(4).trim()
            for (CoordType coord_type in CoordType.values()) {
                if (coord_type.aflosMark && error_info_bad.startsWith(coord_type.aflosMark)) {
                    return true
                }
            }
        }
        return false
    }

    //--------------------------------------------------------------------------
    private static boolean IsBadCourse(String aflosErrorInfo)
    {
        return aflosErrorInfo.contains("-Bad Course")
    }
    
    //--------------------------------------------------------------------------
    private static Integer GetBadCourseSeconds(String aflosErrorInfo)
    {
        if (IsBadCourse(aflosErrorInfo)) {
            String error_info_badcourse = aflosErrorInfo.substring(11).trim()
            if (error_info_badcourse) {
                if (error_info_badcourse.startsWith("(") && error_info_badcourse.endsWith(")")) {
                    error_info_badcourse = error_info_badcourse.substring(1,error_info_badcourse.size()-1)
                    if (error_info_badcourse.isInteger()) {
                        return error_info_badcourse.toInteger()
                    }
                }
            }
        }
        return 0
    }
    
    //--------------------------------------------------------------------------
    private static boolean IsBadTurn(String aflosErrorInfo)
    {
        return aflosErrorInfo.contains("-Bad Turn")
    }
    
    //--------------------------------------------------------------------------
    private static boolean IsBadHeight(String aflosErrorInfo)
    {
        return aflosErrorInfo.contains("-Bad Height")
    }
    
    //--------------------------------------------------------------------------
    private static String ConvertAflosTime(String aflosTime)
    // "09h 36min 05,000sec" -> "hh:mm:ss"
    {
        String[] at = aflosTime.split()
        
        String aflos_hour = at[0].replaceFirst('h','')
        String aflos_minute = at[1].replaceFirst('min','')
        String aflos_seconds = at[2].replaceFirst('sec','')
        aflos_seconds = aflos_seconds.replaceFirst(',','.')
        BigDecimal seconds_decimal = aflos_seconds.toBigDecimal() 
        BigDecimal seconds_decimal_rounded = seconds_decimal.setScale(0, RoundingMode.HALF_EVEN)
        DecimalFormat df = new DecimalFormat("00")
        String seconds = df.format(seconds_decimal_rounded)

        return "$aflos_hour:$aflos_minute:$seconds"
    }
    
    //--------------------------------------------------------------------------
    private static String ConvertAflosUTC(String utcLastDateTime, String aflosTime)
    // aflosTime "09h 36min 05,000sec" -> return "yyyy-mm-ddThh:mm:ssZ"
    {
        return FcTime.UTCGetNextDateTime(utcLastDateTime, ConvertAflosTime(aflosTime))
    }
    
    //--------------------------------------------------------------------------
    private static String ConvertAflosCoordValue(String aflosCoordValue)
    // "51° 26,9035' N" -> "N 051° 26,90350'" 
    {
        String[] a = aflosCoordValue.split()
        
        String grad = a[0]
        while (grad.size() < 4) {
            grad = "0$grad"
        }
        grad = "${grad.substring(0,grad.size()-1)}\u00b0" // ° -> \u00b0 
        
        String minute = a[1]
        minute = "${minute.substring(0,minute.size()-1)}0'"
        
        return "${a[2]} $grad $minute"
    }

    //--------------------------------------------------------------------------
    private static BigDecimal ConvertAflosCoord(String aflosCoordValue)
    // "51° 26,9035' N"
    {
        String[] a = aflosCoordValue.split()
        
        String grad = a[0]
        grad =  grad.substring(0,grad.size()-1)
        
        String minute = a[1]
        minute =  minute.substring(0,minute.size()-1)
        minute = minute.replaceFirst(',','.')
        
        switch (a[2]) {
            case "S":
            case "W":
                return -(grad.toBigDecimal() + (minute.toBigDecimal() / 60))
        }
        return grad.toBigDecimal() + (minute.toBigDecimal() / 60)
    }
     
    //--------------------------------------------------------------------------
    private static Integer ConvertAflosAltitude(String aflosAltitude)
    // "01992 ft"
    {
        return aflosAltitude.split()[0].toInteger()
    }
    
    //--------------------------------------------------------------------------
    static Map ConvertRoute2REF(Route routeInstance, String refFileName)
    {
        BufferedWriter gpx_writer = null
        CharArrayWriter gpx_data = null
        File ref_file = new File(refFileName)
        gpx_writer = ref_file.newWriter()

        CoordRoute last_coordroute_instance = null
        boolean route_modified = false
        int num = 0
        int cp_num = 0
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
            
            String last_mark = coordroute_instance.mark
            switch (coordroute_instance.type) {
                case CoordType.TP:
                case CoordType.SECRET:
                    cp_num++
                    coordroute_instance.mark = "${coordroute_instance.type.aflosMark}${cp_num}"
                    break
                default:
                    coordroute_instance.mark = coordroute_instance.type.aflosMark
                    break
            }
            if (last_mark != coordroute_instance.mark) {
                coordroute_instance.save()
                route_modified = true
            }
            
            num++
            BigDecimal direction = 0.0
            BigDecimal distance = 0
            switch (coordroute_instance.type) {
                case CoordType.TO:
                    direction = coordroute_instance.gateDirection
                    break
                case CoordType.LDG:
                case CoordType.iTO:
                case CoordType.iLDG:
                    Map leg = AviationMath.calculateLeg(coordroute_instance.latMath(),coordroute_instance.lonMath(),last_coordroute_instance.latMath(),last_coordroute_instance.lonMath())
                    direction = coordroute_instance.gateDirection
                    distance = leg.dis
                    break
                default:
                    Map leg = AviationMath.calculateLeg(coordroute_instance.latMath(),coordroute_instance.lonMath(),last_coordroute_instance.latMath(),last_coordroute_instance.lonMath())
                    direction = leg.dir
                    distance = leg.dis
                    break
            }
            String mark = ""
            if (coordroute_instance.type == CoordType.SECRET) {
                if (mark) {
                    mark += ' '
                }
                mark += '$secret'
            }
            if (coordroute_instance.measureDistance) {
                if (mark) {
                    mark += ' '
                }
                mark += '$dist:' + DistanceMeasureStr(coordroute_instance.measureDistance) + 'mm'
            }
            if (coordroute_instance.measureTrueTrack) {
                if (mark) {
                    mark += ' '
                }
                mark += '$track:' + coordroute_instance.measureTrueTrack
            }
            if (coordroute_instance.legDuration) {
                if (mark) {
                    mark += ' '
                }
                mark += '$duration:' + coordroute_instance.legDuration + 'min'

            }
            if (coordroute_instance.noTimeCheck) {
                if (mark) {
                    mark += ' '
                }
                mark += '$notimecheck'
            }
            if (coordroute_instance.noGateCheck) {
                if (mark) {
                    mark += ' '
                }
                mark += '$nogatecheck'
            }
            if (coordroute_instance.noPlanningTest) {
                if (mark) {
                    mark += ' '
                }
                mark += '$noplanningtest'
            }
            Map wr = [name: coordroute_instance.mark,
                      lat:  "${coordroute_instance.latDirection} ${FcMath.GradStr(coordroute_instance.latGrad)} ${MinuteStr(coordroute_instance.latMinute)}",
                      lon:  "${coordroute_instance.lonDirection} ${FcMath.GradStr(coordroute_instance.lonGrad)} ${MinuteStr(coordroute_instance.lonMinute)}",
                      alt:  coordroute_instance.altitude,
                      dir:  "${DirectionStr(direction)}",
                      dis:  "${DirectionStr(distance)}",
                      gat:  "${GateWidthStr(coordroute_instance.gatewidth2)}",
                      latM: "${CoordStr(coordroute_instance.latMath())}",
                      lonM: "${CoordStr(coordroute_instance.lonMath())}",
                     ]
            gpx_writer.writeLine('"' + "${wr.name};${wr.lat};${wr.lon}; ${wr.alt}; ${wr.dir}; ${wr.dis}; ${wr.gat}; 0; ${num}; ${wr.lonM}; ${wr.latM};${mark};" + '"')
            
            last_coordroute_instance = coordroute_instance
        }
        gpx_writer.writeLine('"End Of File;"')
        gpx_writer.close()
        
        if (!routeInstance.showAflosMark) {
            routeInstance.showAflosMark = true
            routeInstance.save()
            route_modified = true
        }
        
        return [ok:true, modified:route_modified]
    }
    
    //--------------------------------------------------------------------------
    private static String MinuteStr(BigDecimal minuteValue)
    {
        if (minuteValue >= 0) {
            DecimalFormat df = new DecimalFormat("00.00000")
            return df.format(minuteValue).replaceAll(',','.')
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    private static String DirectionStr(BigDecimal dirValue)
    {
        if (dirValue >= 0) {
            DecimalFormat df = new DecimalFormat("0.##")
            return df.format(dirValue).replaceAll(',','.')
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    private static String GateWidthStr(Float gateWidth)
    {
        if (gateWidth >= 0) {
            if (gateWidth == gateWidth.toInteger()) {
                return gateWidth.toInteger().toString()
            } else {
                DecimalFormat df = new DecimalFormat("0.##")
                return df.format(gateWidth).replaceAll(',','.')
            }
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    private static String CoordStr(BigDecimal coordValue)
    {
        if (coordValue >= 0) {
            coordValue.setScale(13)
            DecimalFormat df = new DecimalFormat("0.#############")
            return df.format(coordValue).replaceAll(',','.')
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    private static String DistanceMeasureStr(BigDecimal distanceValue)
    {
        if (distanceValue >= 0) {
            DecimalFormat df = new DecimalFormat("0.0#")
            return df.format(distanceValue).replaceAll(',','.')
        }
        return ""
    }
}
