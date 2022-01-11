import java.math.*
import java.util.List;

import org.springframework.web.context.request.RequestContextHolder

class RoutePointsTools
{
    final static Float GPXSHOWPPOINT_RADIUS_CHECKPOINT = 2.0f   // Anzeigeradius um Checkpunkt in NM
    final static Float GPXSHOWPPOINT_RADIUS_ERRORPOINT = 2.0f   // Anzeigeradius um Fehlerpunkt in NM
    final static Float GPXSHOWPPOINT_RADIUS_AIRFIELD = 0.5f   // Anzeigeradius um Flugplatz in NM
    final static Float GPXSHOWPPOINT_RADIUS_ENROUTESIGN = 0.5f   // Anzeigeradius um Strecken-Objekt in NM
    final static int GPXSHOWPPOINT_SCALE = 4                     // Nachkommastellen für Koordinaten
    
    //--------------------------------------------------------------------------
    static List GetShowPointsRoute(Route routeInstance, Test testInstance, def messageSource, Map params, Map contestMap = [:])
    {
        boolean add_image_coord = false
        if (params.addImageCoord) {
            add_image_coord = true
        }
        List points = []
        
        CoordRoute last_coordroute_instance = null
        CoordRoute lasttp_coordroute_instance = null
        List enroute_points = []
        int enroute_point_pos = 0
        boolean show_curved_point = params.showCurvedPoints || routeInstance.showCurvedPoints
        List curved_point_ids = routeInstance.GetCurvedPointIds()
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
            // add enroute points
            if (params.wrEnrouteSign && (enroute_point_pos < enroute_points.size())) {
                Map from_tp_leg = AviationMath.calculateLeg(coordroute_instance.latMath(), coordroute_instance.lonMath(), lasttp_coordroute_instance.latMath(), lasttp_coordroute_instance.lonMath())
                while ((enroute_point_pos < enroute_points.size()) && (enroute_points[enroute_point_pos].dist < from_tp_leg.dis)) {
                    points += enroute_points[enroute_point_pos]
                    enroute_point_pos++
                }
            }
            
            // add additional error points
            if (testInstance) {
                if (testInstance.IsLoggerResult()) {
                    points += GetErrorPoints(testInstance, coordroute_instance, last_coordroute_instance, messageSource)
                } else {
                    points += GetErrorPointsOld(testInstance, coordroute_instance, last_coordroute_instance, messageSource)
                }
            }
            
            // add regular point
            if (!contestMap || coordroute_instance.type.IsContestMapCoord()) {
                if (params.showCoord) {
                    if (params.showCoord == coordroute_instance.titleCode()) {
                        Map new_point = [name:coordroute_instance.titleCode(params.isPrint)]
                        new_point += GetPointCoords(coordroute_instance)
                        if (testInstance) {
                            new_point += GetPointGateMissed(testInstance, coordroute_instance)
                        }
                        points += new_point
                    }
                } else if (show_curved_point || !coordroute_instance.HideSecret(curved_point_ids)) {
                    if (!(params.noCircleCenterPoints && coordroute_instance.circleCenter)) {
                        Map new_point = [name:coordroute_instance.titleCode(params.isPrint)]
                        new_point += GetPointCoords(coordroute_instance)
                        if (testInstance) {
                            new_point += GetPointGateMissed(testInstance, coordroute_instance)
                        }
                        points += new_point
                    }
                }
            }
            
            // cache enroute points
            if (params.wrEnrouteSign) {
                if (coordroute_instance.type.IsEnrouteSignCoord()) {
                    enroute_points = GetEnrouteSignShowPoints(routeInstance,coordroute_instance.type,coordroute_instance.titleNumber, add_image_coord)
                    enroute_point_pos = 0
                    lasttp_coordroute_instance = coordroute_instance
                } else if (coordroute_instance.type != CoordType.SECRET) {
                    enroute_points = []
                    enroute_point_pos = 0
                }
            }
            
            last_coordroute_instance = coordroute_instance
        }
        
        // add unknown enroute points
        if (params.wrEnrouteSign) {
            enroute_points = GetEnrouteSignShowPoints(routeInstance, CoordType.UNKNOWN, 1, add_image_coord) // CoordType.UNKNOWN: 0 - Unevaluated, 1 - NotFound
            for (Map enroute_point in enroute_points) {
                Map new_point = enroute_point
                new_point += [error:true]
                points += new_point
            }
        }
        
        return points
    }
    
    //--------------------------------------------------------------------------
    private static List GetEnrouteSignShowPoints(Route routeInstance, CoordType coordType, int titleNumber, boolean addImageCoord)
    {
        List enroute_points = []
        
        // CoordEnroutePhoto
        if (routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
            for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRouteAndTypeAndTitleNumber(routeInstance,coordType,titleNumber,[sort:"enrouteViewPos"])) {
                Map new_point = [name:coordenroutephoto_instance.enroutePhotoName, dist:coordenroutephoto_instance.enrouteDistance, enroutePhoto:true, , enroutephoto:true]
                if (addImageCoord) {
                    new_point += [imagecoord:coordenroutephoto_instance.imagecoord]
                }
                new_point += GetPointEnrouteCoords(coordenroutephoto_instance)
                enroute_points += new_point
            }
        }
        
        // CoordEnrouteCanvas
        if (routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
            for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRouteAndTypeAndTitleNumber(routeInstance,coordType,titleNumber,[sort:"enrouteViewPos"])) {
                Map new_point = [name:coordenroutecanvas_instance.enrouteCanvasSign.canvasName, dist:coordenroutecanvas_instance.enrouteDistance, enroutePhoto:false, enroutecanvas:true]
                new_point += GetPointEnrouteCoords(coordenroutecanvas_instance)
                enroute_points += new_point
            }
        }
        
        // sort enroute points
        enroute_points.sort { p1, p2 ->
            p1.dist.compareTo(p2.dist)
        }

        return enroute_points
    }
    
    //--------------------------------------------------------------------------
    private static Map GetPointGateMissed(Test testInstance, CoordRoute coordrouteInstance)
    {
        Map error_point = [:]
        for (CoordResult coordresult_instance in CoordResult.findAllByTest(testInstance,[sort:"id"])) {
            if (coordrouteInstance.title() == coordresult_instance.title()) {
                if (coordresult_instance.resultEntered) {
                    if (coordresult_instance.resultCpNotFound) {
                        if ((coordresult_instance.type == CoordType.SECRET) && (!testInstance.IsFlightTestCheckSecretPoints())) {
                            error_point += [warning:true]
                        } else if (DisabledCheckPointsTools.Uncompress(testInstance.task.disabledCheckPointsNotFound).contains(coordresult_instance.title()+',')) {
                            error_point += [warning:true]
                        } else if (testInstance.GetFlightTestCpNotFoundPoints() == 0) {
                            error_point += [warning:true]
                        } else {
                            switch(coordresult_instance.type) {
                                case CoordType.TO:
                                    if (testInstance.IsFlightTestCheckTakeOff() || testInstance.GetFlightTestTakeoffCheckSeconds()) {
                                        error_point += [error:true]
                                    }
                                    break
                                case CoordType.LDG:
                                    if (testInstance.IsFlightTestCheckLanding()) {
                                        error_point += [error:true]
                                    }
                                    break
                                default:
                                    error_point += [error:true]
                                    break
                            }
                        }
                    }
                }
                break
            }
        }
        return error_point
    }
    
    //--------------------------------------------------------------------------
    static Map GetPointCoords(CoordRoute coordrouteInstance)
    {
        Map ret = [:]
        if (coordrouteInstance.type == CoordType.SECRET) {
            ret += [latcenter:coordrouteInstance.latMath(),loncenter:coordrouteInstance.lonMath(),radius:GPXSHOWPPOINT_RADIUS_CHECKPOINT]
        } else if (coordrouteInstance.type.IsCpCheckCoord()) {
            ret += [latcenter:coordrouteInstance.latMath(),loncenter:coordrouteInstance.lonMath(),radius:GPXSHOWPPOINT_RADIUS_CHECKPOINT]
        } else {
            ret += [latcenter:coordrouteInstance.latMath(),loncenter:coordrouteInstance.lonMath(),radius:GPXSHOWPPOINT_RADIUS_AIRFIELD]
        }
        ret.latcenter = ret.latcenter.setScale(GPXSHOWPPOINT_SCALE, RoundingMode.HALF_EVEN)
        ret.loncenter = ret.loncenter.setScale(GPXSHOWPPOINT_SCALE, RoundingMode.HALF_EVEN)
        if (coordrouteInstance.circleCenter) {
            ret += [circlecenter:true]
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private static List GetErrorPoints(Test testInstance, CoordRoute coordrouteInstance, CoordRoute lastCoordrouteInstance, def messageSource)
    {
        List error_points = []
        if (coordrouteInstance && lastCoordrouteInstance) {
            
            CoordResult coordresult_instance = GetCoordResult(testInstance, coordrouteInstance)
            CoordResult last_coordresult_instance = GetCoordResult(testInstance, lastCoordrouteInstance)
            String utc = ""
            String last_utc = ""
            for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'utc'])) {
                if (calcresult_instance.IsCoordTitleEqual(coordrouteInstance.type,coordrouteInstance.titleNumber)) {
                    utc = calcresult_instance.utc
                }
                if (calcresult_instance.IsCoordTitleEqual(lastCoordrouteInstance.type,lastCoordrouteInstance.titleNumber)) {
                    last_utc = calcresult_instance.utc
                }
                if (utc && last_utc) {
                    break
                }
            }
            
            if (utc && last_utc) {
                for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'utc'])) {
                    if ((last_utc < calcresult_instance.utc) && (calcresult_instance.utc < utc)) {
                        if (calcresult_instance.badTurn && !calcresult_instance.judgeDisabled) {
                            if (testInstance.task.procedureTurnDuration > 0) {
                                if (coordresult_instance.resultProcedureTurnEntered && coordresult_instance.planProcedureTurn) {
                                    if (coordresult_instance.resultProcedureTurnNotFlown) {
                                        Map new_point = [name:getMsg("fc.offlinemap.badprocedureturn",false,messageSource)] // false - no Print
                                        new_point += GetPointCoords2(calcresult_instance.latitude, calcresult_instance.longitude)
                                        if (DisabledCheckPointsTools.Uncompress(testInstance.task.disabledCheckPointsProcedureTurn).contains(last_coordresult_instance.title()+',')) {
                                            new_point += [warning:true] // noBadTurn
                                        } else if (testInstance.GetFlightTestProcedureTurnNotFlownPoints() == 0) {
                                            new_point += [warning:true] // noBadTurn
                                        } else {
                                            new_point += [error:true] // no noBadTurn
                                        }
                                        error_points += new_point
                                    }
                                }
                            }
                        }
                        if (calcresult_instance.badCourse && calcresult_instance.badCourseSeconds && !calcresult_instance.judgeDisabled) {
                           if (coordresult_instance.resultEntered && coordresult_instance.type.IsBadCourseCheckCoord()) {
                                Map new_point = [name:getMsg("fc.offlinemap.badcourse.seconds", [calcresult_instance.badCourseSeconds], false, messageSource)] // false - no Print
                                new_point += GetPointCoords2(calcresult_instance.latitude, calcresult_instance.longitude)
                                if (DisabledCheckPointsTools.Uncompress(testInstance.task.disabledCheckPointsBadCourse).contains(coordresult_instance.title()+',')) {
                                    new_point += [warning:true] // noBadCourse
                                } else if (testInstance.GetFlightTestBadCoursePoints() == 0) {
                                    new_point += [warning:true] // noBadCourse
                                } else if (calcresult_instance.badCourseSeconds <= testInstance.GetFlightTestBadCourseCorrectSecond()) {
                                    new_point += [warning:true] // noBadCourse
                                } else {
                                    new_point += [error:true] // no noBadCourse
                                }
                                error_points += new_point
                            }
                        }
                    }
                }
            }
            
        }
        return error_points
    }
    
    //--------------------------------------------------------------------------
    private static List GetErrorPointsOld(Test testInstance, CoordRoute coordrouteInstance, CoordRoute lastCoordrouteInstance, def messageSource)
    {
        List error_points = []
        CoordResult coordresult_instance = GetCoordResult(testInstance, coordrouteInstance)
        CoordResult last_coordresult_instance = GetCoordResult(testInstance, lastCoordrouteInstance)
        if (coordresult_instance) {
            
            // procedure turn not flown
            if ((testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (testInstance.task.procedureTurnDuration > 0)) {
                if (coordresult_instance.resultProcedureTurnEntered && coordresult_instance.planProcedureTurn) {
                    if (coordresult_instance.resultProcedureTurnNotFlown) {
                        Map new_point = [name:getMsg("fc.offlinemap.badprocedureturn",false, messageSource)] // false - no Print
                        new_point += GetPointCoords(lastCoordrouteInstance)
                        if (!DisabledCheckPointsTools.Uncompress(testInstance.task.disabledCheckPointsProcedureTurn).contains(last_coordresult_instance.title()+',')) {
                            new_point += [error:true]
                        } else {
                            new_point += [warning:true]
                        }
                        error_points += new_point
                    }
                }
            }
            
            // bad course
            if (testInstance.GetFlightTestBadCoursePoints() > 0) {
                if (coordresult_instance.resultEntered && coordresult_instance.type.IsBadCourseCheckCoord()) {
                    if (coordresult_instance.resultBadCourseNum > 0) {
                        Map new_point = [name:getMsg("fc.offlinemap.badcourse.number", [coordresult_instance.resultBadCourseNum], false, messageSource)] // false - no Print
                        
                        new_point += GetPointCoords(coordrouteInstance)
                        if (!DisabledCheckPointsTools.Uncompress(testInstance.task.disabledCheckPointsBadCourse).contains(coordresult_instance.title()+',')) {
                            new_point += [error:true]
                        } else {
                            new_point += [warning:true]
                        }
                        error_points += new_point
                    }
                }
            }
        }
        return error_points
    }
    
    //--------------------------------------------------------------------------
    private static Map GetPointCoords2(BigDecimal latValue, BigDecimal lonValue)
    {
        Map ret = [latcenter:latValue,loncenter:lonValue,radius:GPXSHOWPPOINT_RADIUS_ERRORPOINT] 
        ret.latcenter = ret.latcenter.setScale(GPXSHOWPPOINT_SCALE, RoundingMode.HALF_EVEN)
        ret.loncenter = ret.loncenter.setScale(GPXSHOWPPOINT_SCALE, RoundingMode.HALF_EVEN)
        return ret
    }
    
    //--------------------------------------------------------------------------
    private static Map GetPointEnrouteCoords(Coord coordInstance)
    {
        Map ret = [:]
        ret += [latcenter:coordInstance.latMath(),loncenter:coordInstance.lonMath(),radius:GPXSHOWPPOINT_RADIUS_ENROUTESIGN]
        ret.latcenter = ret.latcenter.setScale(GPXSHOWPPOINT_SCALE, RoundingMode.HALF_EVEN)
        ret.loncenter = ret.loncenter.setScale(GPXSHOWPPOINT_SCALE, RoundingMode.HALF_EVEN)
        return ret
    }
    
    //--------------------------------------------------------------------------
    private static CoordResult GetCoordResult(Test testInstance, CoordRoute coordrouteInstance)
    {
        if (coordrouteInstance) {
            for (CoordResult coordresult_instance in CoordResult.findAllByTest(testInstance,[sort:"id"])) {
                if (coordrouteInstance.title() == coordresult_instance.title()) {
                    return coordresult_instance
                }
            }
        }
        return null
    } 
    
    //--------------------------------------------------------------------------
    private static String getMsg(String code, List args, boolean isPrint, def messageSource)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        String lang = session_obj.showLanguage
        if (isPrint) {
            lang = session_obj.printLanguage
        }
        if (args) {
            return messageSource.getMessage(code, args.toArray(), new Locale(lang))
        } else {
            return messageSource.getMessage(code, null, new Locale(lang))
        }
    }

    //--------------------------------------------------------------------------
    private static String getMsg(String code, boolean isPrint, def messageSource)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        String lang = session_obj.showLanguage
        if (isPrint) {
            lang = session_obj.printLanguage
        }
        return messageSource.getMessage(code, null, new Locale(lang))
    }
}
