class RouteTools
{
    //--------------------------------------------------------------------------
    static List GetOkPlanningTestTaskRoutes(Contest contestInstance)
    {
        List ret_routes_used = []
        List ret_routes_unused = []
        for (Route route_instance in Route.findAllByContest(contestInstance,[sort:"idTitle"])) {
            Map route_status = GetRouteStatus(route_instance)
            if (route_status.routeUsuable) {
                if (PlanningTestTask.findByRoute(route_instance,[sort:"id"])) {
                    ret_routes_used += route_instance
                } else {
                    ret_routes_unused += route_instance
                }
            }
        }
        
        return ret_routes_unused + ret_routes_used
    }
    
    //--------------------------------------------------------------------------
    static List GetOkFlightTestRoutes(Contest contestInstance)
    {
        List ret_routes_used = []
        List ret_routes_unused = []
        for (Route route_instance in Route.findAllByContest(contestInstance,[sort:"idTitle"])) {
            Map route_status = GetRouteStatus(route_instance)
            if (route_status.routeUsuable) {
                if (FlightTest.findByRoute(route_instance,[sort:"id"])) {
                    ret_routes_used += route_instance
                } else {
                    ret_routes_unused += route_instance
                }
            }
        }
        
        return ret_routes_unused + ret_routes_used
    }
    
    //--------------------------------------------------------------------------
    static boolean IsObservationSignOk(Route routeInstance)
    {
        boolean is_observation_sign = false
        boolean sign_ok = true
        
        if (routeInstance) {
            Map route_status = GetRouteStatus(routeInstance)
            if (routeInstance.IsTurnpointSign()) {
                is_observation_sign = true
                if (!route_status.turnpointSignOk) {
                    sign_ok = false
                }
            }
            if (routeInstance.enroutePhotoRoute.IsEnrouteRouteInput()) {
                is_observation_sign = true
                if (!route_status.enrouteMeasurementPhotoOk || !route_status.enrouteSignPhotoOk) {
                    sign_ok = false
                }
            }
            if (routeInstance.enrouteCanvasRoute.IsEnrouteRouteInput()) {
                is_observation_sign = true
                if (!route_status.enrouteMeasurementCanvasOk || !route_status.enrouteSignCanvasOk) {
                    sign_ok = false
                }
            }
        }
        
        return is_observation_sign && sign_ok
    }
    
    //--------------------------------------------------------------------------
    static Map GetRouteStatus(Route routeInstance)
    {
        Map ret = [turnpointSignOk:false,            turnpointSignStatusInfo:"",
                   enrouteMeasurementPhotoOk:false,  enrouteMeasurementPhotoInfo:"",
                   enrouteMeasurementCanvasOk:false, enrouteMeasurementCanvasInfo:"",
                   enrouteSignPhotoOk:false,         enrouteSignPhotoStatusInfo:"",
                   enrouteSignCanvasOk:false,        enrouteSignCanvasStatusInfo:"",
                   routeUsuable:false,
                   routeOk:false,                    routeInfo:""
                  ]

        Map turnpoint_status = get_turnpoint_status(routeInstance)
        ret.turnpointSignOk = is_turnpoint_ok(routeInstance, turnpoint_status)
        ret.turnpointSignStatusInfo = get_turnpoint_status_info(routeInstance, turnpoint_status, ret.turnpointSignOk)

        Map photo_measurement_status = get_enroute_measurement_status(routeInstance, true)
        ret.enrouteMeasurementPhotoOk = is_enroute_measurement_ok(photo_measurement_status, true)
        ret.enrouteMeasurementPhotoInfo = get_enroute_measurement_status_info(routeInstance, photo_measurement_status, true, ret.enrouteMeasurementPhotoOk)

        Map canvas_measurement_status = get_enroute_measurement_status(routeInstance, false)
        ret.enrouteMeasurementCanvasOk = is_enroute_measurement_ok(canvas_measurement_status, false)
        ret.enrouteMeasurementCanvasInfo = get_enroute_measurement_status_info(routeInstance, canvas_measurement_status, false, ret.enrouteMeasurementCanvasOk)

        Map photo_status = get_enroute_photo_status(routeInstance)
        ret.enrouteSignPhotoOk = is_enroute_sign_ok(photo_status, true)
        ret.enrouteSignPhotoStatusInfo = get_enroute_sign_status_info(routeInstance, photo_status, true, ret.enrouteSignPhotoOk)

        Map canvas_status = get_enroute_canvas_status(routeInstance)
        ret.enrouteSignCanvasOk = is_enroute_sign_ok(canvas_status, false)
        ret.enrouteSignCanvasStatusInfo = get_enroute_sign_status_info(routeInstance, canvas_status, false, ret.enrouteSignCanvasOk)
        
        Map route_status = get_route_status(routeInstance, turnpoint_status, photo_status, canvas_status)
        ret.routeUsuable = is_route_usuable(route_status)
        ret.routeOk = is_route_ok(route_status)
        ret.routeInfo = get_route_status_info(routeInstance, route_status, ret.routeOk)
        
        return ret
    }
    
    // Turnpoint
    // =========
    
    //--------------------------------------------------------------------------
    private static Map get_turnpoint_status(Route routeInstance)
    {
        int missingsign_num = 0
        int doublesign_num = 0
        int invalidsign_num = 0
        int unassigned_num = 0
        int canvassign_num = 0
        if (routeInstance.IsTurnpointSign()) {
            switch (routeInstance.turnpointRoute) {
                case TurnpointRoute.AssignPhoto:
                    List signs = []
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                        if (coordroute_instance.type.IsTurnpointSignCoord()) {
                            if (coordroute_instance.assignedSign == TurnpointSign.None) {
                                missingsign_num++
                            } else if (coordroute_instance.assignedSign == TurnpointSign.NoSign) {
                                // nothing
                            } else if (coordroute_instance.assignedSign in signs) {
                                doublesign_num++
                            }
                            signs += coordroute_instance.assignedSign
                        }
                            
                    }
                    break
                case TurnpointRoute.AssignCanvas:
                    List signs = []
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                        if (coordroute_instance.type.IsTurnpointSignCoord()) {
                            if (coordroute_instance.assignedSign == TurnpointSign.None) {
                                missingsign_num++
                            } else if (!coordroute_instance.assignedSign.canvas) {
                                invalidsign_num++
                            } else if (coordroute_instance.assignedSign == TurnpointSign.NoSign) {
                                // nothing
                            } else if (coordroute_instance.assignedSign in signs) {
                                doublesign_num++
                                canvassign_num++
                            } else {
                                canvassign_num++
                            }
                            signs += coordroute_instance.assignedSign
                        }
                    }
                    break
                case TurnpointRoute.TrueFalsePhoto:
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                        if (coordroute_instance.type.IsTurnpointSignCoord()) {
                            if (coordroute_instance.correctSign == TurnpointCorrect.Unassigned) {
                                unassigned_num++
                            }
                        }
                    }
                    break
            }
        }
        return [missingsign_num:missingsign_num, doublesign_num:doublesign_num, invalidsign_num:invalidsign_num, unassigned_num:unassigned_num, canvassign_num:canvassign_num]
    }
    
    //--------------------------------------------------------------------------
    private static boolean is_turnpoint_ok(Route routeInstance, Map status)
    {
        if (status.missingsign_num > 0) {
            return false
        }
        if (status.doublesign_num > 0) {
            return false
        }
        if (status.invalidsign_num > 0) {
            return false
        }
        if (status.unassigned_num > 0) {
            return false
        }
        return true
    }
    
    //--------------------------------------------------------------------------
    private static String get_turnpoint_status_info(Route routeInstance, Map status, boolean turnpointSignOk)
    {
        String s = ""
        if (!turnpointSignOk) {
            if (status.missingsign_num > 0) {
                s += "${status.missingsign_num} ${routeInstance.getMsg('fc.observation.turnpoint.missingsigns')}"
            }
            if (status.doublesign_num > 0) {
                if (status.missingsign_num > 0) {
                    s += ", "
                }
                s += "${status.doublesign_num} ${routeInstance.getMsg('fc.observation.turnpoint.doublesigns')}"
            }
            if (status.invalidsign_num > 0) {
                if ((status.missingsign_num > 0) || (status.doublesign_num > 0)) {
                    s += ", "
                }
                s += "${status.invalidsign_num} ${routeInstance.getMsg('fc.observation.turnpoint.invalidsigns')}"
            }
            if (status.unassigned_num > 0) {
                if ((status.missingsign_num > 0) || (status.doublesign_num > 0) || (status.invalidsign_num > 0)) {
                    s += ", "
                }
                s += "${status.unassigned_num} ${routeInstance.getMsg('fc.observation.turnpoint.unassignedsigns')}"
            }
        }
        return s
    }
    
    // Enroute measurement
    // ===================
    
    //--------------------------------------------------------------------------
    private static Map get_enroute_measurement_status(Route routeInstance, boolean enroutePhoto)
    {
        boolean unassigned = false
        boolean noinput = false
        boolean noposition = false
        if (enroutePhoto) {
            switch (routeInstance.enroutePhotoMeasurement) {
                case EnrouteMeasurement.None:
                    // nothing
                    break
                case EnrouteMeasurement.Unassigned:
                    unassigned = true
                    break
                case EnrouteMeasurement.Map:
                    if (!routeInstance.enroutePhotoRoute.IsEnrouteRouteInput()) {
                        noinput = true
                    }
                    break
                case EnrouteMeasurement.NMFromTP:
                case EnrouteMeasurement.mmFromTP:
                //case EnrouteMeasurement.PosFromTP:
                    if (!routeInstance.enroutePhotoRoute.IsEnrouteRouteInput()) {
                        noinput = true
                    } else if (!routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
                        noposition = true
                    }
                    break
            }
        } else {
            switch (routeInstance.enrouteCanvasMeasurement) {
                case EnrouteMeasurement.None:
                    // nothing
                    break
                case EnrouteMeasurement.Unassigned:
                    unassigned = true
                    break
                case EnrouteMeasurement.Map:
                    if (!routeInstance.enrouteCanvasRoute.IsEnrouteRouteInput()) {
                        noinput = true
                    }
                    break
                case EnrouteMeasurement.NMFromTP:
                case EnrouteMeasurement.mmFromTP:
                //case EnrouteMeasurement.PosFromTP:
                    if (!routeInstance.enrouteCanvasRoute.IsEnrouteRouteInput()) {
                        noinput = true
                    } else if (!routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
                        noposition = true
                    }
                    break
            }
        }
        return [unassigned:unassigned, noinput:noinput, noposition:noposition]
    }
    
    //--------------------------------------------------------------------------
    private static boolean is_enroute_measurement_ok(Map status, boolean enroutePhoto)
    {
        if (status.unassigned || status.noinput || status.noposition) {
            return false
        }
        return true
    }
    
    //--------------------------------------------------------------------------
    private static String get_enroute_measurement_status_info(Route routeInstance, Map status, boolean enroutePhoto, boolean enrouteMeasurementOk)
    {
        String s = ""
        if (!enrouteMeasurementOk) {
            if (status.unassigned) {
                s += "${routeInstance.getMsg(get_name(enroutePhoto,'fc.observation.enroute.unassigned'))}"
            } else if (status.noinput) {
                s += "${routeInstance.getMsg(get_name(enroutePhoto,'fc.observation.enroute.noinput'))}"
            } else if (status.noposition) {
                s += "${routeInstance.getMsg(get_name(enroutePhoto,'fc.observation.enroute.noposition'))}"
            }
        }
        return s
    }
    
    // Enroute photos and canvas
    // =========================
    
    //--------------------------------------------------------------------------
    private static Map get_enroute_photo_status(Route routeInstance)
    {
        int doublesign_num = 0
        int unknowncoordtype_num = 0
        int distancetolong_num = 0
        int num = 0
        int min_num = 0
        int max_num = 0
        boolean min_error = false
        boolean max_error = false
        if (routeInstance.enroutePhotoRoute.IsEnrouteRouteInput()) {
            List enroutephoto_names = []
            for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                if (coordenroutephoto_instance.enroutePhotoName in enroutephoto_names) {
                    doublesign_num++
                }
                if (coordenroutephoto_instance.type == CoordType.UNKNOWN) {
                    if (routeInstance.enroutePhotoRoute != EnrouteRoute.InputName) {
                        unknowncoordtype_num++
                    }
                } else if (!coordenroutephoto_instance.enrouteDistanceOk) {
                    distancetolong_num++
                }
                enroutephoto_names += coordenroutephoto_instance.enroutePhotoName
            }
            num = CoordEnroutePhoto.countByRoute(routeInstance)
            min_num = routeInstance.contest.minEnroutePhotos
            if (num < min_num) {
                min_error = true
            }
            max_num = routeInstance.contest.maxEnroutePhotos
            if (num > max_num) {
                max_error = true
            }
        }
        return [doublesign_num:doublesign_num, unknowncoordtype_num:unknowncoordtype_num, distancetolong_num:distancetolong_num,
                num:num, min_num:min_num, max_num:max_num, min_error:min_error, max_error:max_error]
    }
    
    //--------------------------------------------------------------------------
    private static Map get_enroute_canvas_status(Route routeInstance)
    {
        int doublesign_num = 0
        int unknowncoordtype_num = 0
        int distancetolong_num = 0
        int num = 0
        int min_num = 0
        int max_num = 0
        boolean min_error = false
        boolean max_error = false
        if (routeInstance.enrouteCanvasRoute.IsEnrouteRouteInput()) {
            List signs = []
            for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                if (!routeInstance.contest.enrouteCanvasMultiple) {
                    if (coordenroutecanvas_instance.enrouteCanvasSign in signs) {
                        doublesign_num++
                    }
                }
                if (coordenroutecanvas_instance.type == CoordType.UNKNOWN) {
                    if (routeInstance.enrouteCanvasRoute != EnrouteRoute.InputName) {
                        unknowncoordtype_num++
                    }
                } else if (!coordenroutecanvas_instance.enrouteDistanceOk) {
                    distancetolong_num++
                }
                if (coordenroutecanvas_instance.enrouteCanvasSign != EnrouteCanvasSign.NoSign) {
                    signs += coordenroutecanvas_instance.enrouteCanvasSign
                }
            }
            num = signs.size()
        }
        min_num = routeInstance.contest.minEnrouteCanvas
        if (num < min_num) {
            min_error = true
        }
        max_num = routeInstance.contest.maxEnrouteCanvas
        if (num > max_num) {
            max_error = true
        }
        return [doublesign_num:doublesign_num, unknowncoordtype_num:unknowncoordtype_num, distancetolong_num:distancetolong_num,
                num:num, min_num:min_num, max_num:max_num, min_error:min_error, max_error:max_error]
    }
    
    //--------------------------------------------------------------------------
    private static boolean is_enroute_sign_ok(Map status, boolean enroutePhoto)
    {
        if ((status.doublesign_num > 0) || (status.unknowncoordtype_num > 0) || (status.distancetolong_num > 0) || status.min_error || status.max_error) {
            return false
        }
        return true
    }
    
    //--------------------------------------------------------------------------
    private static String get_enroute_sign_status_info(Route routeInstance, Map status, boolean enroutePhoto, boolean enrouteSignOk)
    {
        String s = ""
        if (!enrouteSignOk) {
            boolean wr_comma = false
            if (status.doublesign_num > 0) {
                if (wr_comma) {
                    s += ", "
                }
                s += "${status.doublesign_num} ${routeInstance.getMsg(get_name(enroutePhoto,'fc.observation.enroute.doublename'))}"
                wr_comma = true
            }
            if (status.unknowncoordtype_num > 0) {
                if (wr_comma) {
                    s += ", "
                }
                s += "${status.unknowncoordtype_num} ${routeInstance.getMsg(get_name(enroutePhoto,'fc.observation.enroute.unknowncoordtype'))}"
                wr_comma = true
            }
            if (status.distancetolong_num > 0) {
                if (wr_comma) {
                    s += ", "
                }
                s += "${status.distancetolong_num} ${routeInstance.getMsg(get_name(enroutePhoto,'fc.observation.enroute.distancetolong'))}"
                wr_comma = true
            }
            if (status.min_error) {
                if (wr_comma) {
                    s += ", "
                }
                s += routeInstance.getMsgArgs(get_name(enroutePhoto,'fc.observation.enroute.numerror.min'),[status.min_num, status.num])
                wr_comma = true
            }
            if (status.max_error) {
                if (wr_comma) {
                    s += ", "
                }
                s += routeInstance.getMsgArgs(get_name(enroutePhoto,'fc.observation.enroute.numerror.max'),[status.max_num, status.num])
                wr_comma = true
            }
        }
        return s
    }
    
    // Route
    // =====
    
    //--------------------------------------------------------------------------
    private static Map get_route_status(Route routeInstance, Map turnpointStatus, Map photoStatus, Map canvasStatus)
    {
        int target_num = 0
        int canvas_num = 0
        if (routeInstance.enroutePhotoRoute.IsEnrouteRouteInput()) {
            target_num += photoStatus.num
        }
        if (routeInstance.enrouteCanvasRoute.IsEnrouteRouteInput()) {
            target_num += canvasStatus.num
            canvas_num += canvasStatus.num
        }
        if (routeInstance.turnpointRoute == TurnpointRoute.AssignCanvas) {
            target_num += turnpointStatus.canvassign_num
            canvas_num += turnpointStatus.canvassign_num
        }
        
        boolean min_canvas_error = false
        int min_canvas_num = routeInstance.contest.minEnrouteCanvas
        if (routeInstance.enrouteCanvasRoute.IsEnrouteRouteInput() || routeInstance.turnpointRoute == TurnpointRoute.AssignCanvas) {
            if (canvas_num < min_canvas_num) {
                min_canvas_error = true
            }
        }
        
        boolean max_canvas_error = false
        int max_canvas_num = routeInstance.contest.maxEnrouteCanvas
        if (routeInstance.enrouteCanvasRoute.IsEnrouteRouteInput() || routeInstance.turnpointRoute == TurnpointRoute.AssignCanvas) {
            if (canvas_num > max_canvas_num) {
                max_canvas_error = true
            }
        }
        
        boolean min_target_error = false
        int min_target_num = routeInstance.contest.minEnrouteTargets
        if (routeInstance.enroutePhotoRoute.IsEnrouteRouteInput() || routeInstance.enrouteCanvasRoute.IsEnrouteRouteInput() || routeInstance.turnpointRoute == TurnpointRoute.AssignCanvas) {
            if (target_num < min_target_num) {
                min_target_error = true
            }
        }
        
        boolean max_target_error = false
        int max_target_num = routeInstance.contest.maxEnrouteTargets
        if (routeInstance.enroutePhotoRoute.IsEnrouteRouteInput() || routeInstance.enrouteCanvasRoute.IsEnrouteRouteInput() || routeInstance.turnpointRoute == TurnpointRoute.AssignCanvas) {
            if (target_num > max_target_num) {
                max_target_error = true
            }
        }
        
        boolean route_empty = routeInstance.IsRouteEmpty()
        boolean route_incomplete = false
        if (!route_empty) {
            route_incomplete = !routeInstance.IsRouteComplete()
        }
        
        int leg_num = RouteLegTest.countByRoute(routeInstance)
        
        boolean min_leg_error = false
        int min_leg_num = routeInstance.contest.minRouteLegs
        if (!route_empty && !route_incomplete) {
            if (leg_num < min_leg_num) {
                min_leg_error = true
            }
        }
        
        boolean max_leg_error = false
        int max_leg_num = routeInstance.contest.maxRouteLegs
        if (!route_empty && !route_incomplete) {
            if (leg_num > max_leg_num) {
                max_leg_error = true
            }
        }
        
        List measure_distance_difference = []
        for (RouteLegTest routelegtest_instance in RouteLegTest.findAllByRoute(routeInstance,[sort:'id'])) {
            CoordRoute coordroute_instance = CoordRoute.findByRouteAndTypeAndTitleNumber(routeInstance,routelegtest_instance.endTitle.type,routelegtest_instance.endTitle.number)
            if (coordroute_instance.measureDistance != routelegtest_instance.legMeasureDistance) {
                measure_distance_difference += routelegtest_instance.endTitle.titleCode()
            }
        }
        
        List procedureturn_difference = []
        RouteLegCoord last_routelegcoord_instance = null
        for (RouteLegCoord routelegcoord_instance in RouteLegCoord.findAllByRoute(routeInstance,[sort:'id'])) {
            if (routelegcoord_instance.IsProcedureTurn()) {
                BigDecimal course_change = AviationMath.courseChange(routelegcoord_instance.turnTrueTrack,routelegcoord_instance.testTrueTrack())
                if (course_change.abs() >= 90) {
                    CoordRoute coordroute_instance = CoordRoute.findByRouteAndTypeAndTitleNumber(routeInstance,routelegcoord_instance.endTitle.type,routelegcoord_instance.endTitle.number)
                    if (!coordroute_instance.planProcedureTurn) {
                        procedureturn_difference += last_routelegcoord_instance.endTitle.titleCode()
                    }
                }
            }
            last_routelegcoord_instance = routelegcoord_instance
        }

        int circlecenter_num = CoordRoute.countByRouteAndCircleCenter(routeInstance, true)
		
		List secret_measure_incomplete = []
		int secret_pos = 0
		BigDecimal measure_distance = 0
		boolean secret_error_found = false
        List corridor_tpflags_errors = []
        List gatewidth_errors = []
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:"id"])) {
			if (coordroute_instance.type == CoordType.SECRET) {
				secret_pos++
				if (!secret_error_found) {
					if (secret_pos > 1) {
						if (coordroute_instance.measureDistance) {
							if (!measure_distance) {
								secret_measure_incomplete += coordroute_instance.titleCode()
								secret_error_found = true
							} else if (measure_distance > coordroute_instance.measureDistance) {
								secret_measure_incomplete += coordroute_instance.titleCode()
								secret_error_found = true
							}
						} else {
							if (measure_distance) {
								secret_measure_incomplete += coordroute_instance.titleCode()
								secret_error_found = true
							}
						}
					}
					if (coordroute_instance.measureDistance) {
						measure_distance = coordroute_instance.measureDistance
					}
				}
			} else {
				secret_pos = 0
				measure_distance = 0
				secret_error_found = false
			}
            if (routeInstance.corridorWidth) {
                if (coordroute_instance.type.IsCorridorCoord()) {
                    if (coordroute_instance.type.IsCorridorNoCheckCoord()) {
                        if (!coordroute_instance.noTimeCheck || !coordroute_instance.noGateCheck) {
                            corridor_tpflags_errors += coordroute_instance.titleCode()
                        }
                    }
                }
            } else {
                if (!coordroute_instance.gatewidth2) {
                    gatewidth_errors += coordroute_instance.titleCode()
                }
            }
		}
            
        boolean corridor_error = false
        if (routeInstance.contest.anrFlying) {
            if (!routeInstance.corridorWidth) {
                corridor_error = true
            }
        } else {
            if (routeInstance.corridorWidth) {
                corridor_error = true
            }
        }
        
         
        return [target_num:                  target_num, 
                min_target_num:              min_target_num,
                min_target_error:            min_target_error,
                max_target_num:              max_target_num, 
                max_target_error:            max_target_error,
                canvas_num:                  canvas_num,
                min_canvas_num:              min_canvas_num,
                min_canvas_error:            min_canvas_error,
                max_canvas_num:              max_canvas_num,
                max_canvas_error:            max_canvas_error,
                leg_num:                     leg_num,
                min_leg_num:                 min_leg_num,
                min_leg_error:               min_leg_error,
                max_leg_num:                 max_leg_num, 
                max_leg_error:               max_leg_error,
                route_empty:                 route_empty,
                route_incomplete:            route_incomplete,
                measure_distance_difference: measure_distance_difference,
                procedureturn_difference:    procedureturn_difference,
                circlecenter_num:            circlecenter_num,
				secret_measure_incomplete:   secret_measure_incomplete,
                corridor_error:              corridor_error,
                corridor_tpflags_errors:     corridor_tpflags_errors,
                gatewidth_errors:            gatewidth_errors
               ]
    }
    
    //--------------------------------------------------------------------------
    private static boolean is_route_usuable(Map status)
    {
        if (status.route_empty || status.route_incomplete || status.measure_distance_difference || status.procedureturn_difference || status.circlecenter_num || status.secret_measure_incomplete || status.corridor_error || status.gatewidth_errors) {
            return false
        }
        return true
    }
    
    //--------------------------------------------------------------------------
    private static boolean is_route_ok(Map status)
    {
        if (!is_route_usuable(status)) {
            return false
        } else if (status.min_target_error || status.max_target_error || status.min_canvas_error || status.max_canvas_error || status.min_leg_error || status.max_leg_error || status.corridor_tpflags_errors) {
            return false
        }
        return true
    }
    
    //--------------------------------------------------------------------------
    private static String get_route_status_info(Route routeInstance, Map status, boolean routeOk)
    {
        String s = ""
        if (!routeOk) {
            boolean wr_comma = false
            if (status.circlecenter_num) {
                if (wr_comma) {
                    s += ", "
                }
                s += routeInstance.getMsgArgs('fc.circlecenter.error',[status.circlecenter_num])
                wr_comma = true
            } else {
                if (status.route_empty) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.route.empty',[])
                    wr_comma = true
                }
                if (status.route_incomplete) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.route.incomplete',[])
                    wr_comma = true
                }
                if (status.min_leg_error) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.routelegtest.numerror.min',[status.min_leg_num, status.leg_num])
                    wr_comma = true
                }
                if (status.max_leg_error) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.routelegtest.numerror.max',[status.max_leg_num, status.leg_num])
                    wr_comma = true
                }
                if (status.measure_distance_difference) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.routelegtest.legmeasuredistance.difference',[status.measure_distance_difference])
                    wr_comma = true
                }
                if (status.procedureturn_difference) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.routelegtest.procedureturn.difference',[status.procedureturn_difference])
                    wr_comma = true
                }
                if (status.min_target_error) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.observation.enroute.numerror.min',[status.min_target_num, status.target_num])
                    wr_comma = true
                }
                if (status.max_target_error) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.observation.enroute.numerror.max',[status.max_target_num, status.target_num])
                    wr_comma = true
                }
                if (status.min_canvas_error) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.observation.enroute.numerror.min.canvas',[status.min_canvas_num, status.canvas_num])
                    wr_comma = true
                }
                if (status.max_canvas_error) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.observation.enroute.numerror.max.canvas',[status.max_canvas_num, status.canvas_num])
                    wr_comma = true
                }
                if (status.secret_measure_incomplete) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.route.secretmeasureincomplete',[status.secret_measure_incomplete])
                    wr_comma = true
                }
                if (status.corridor_error) {
                    if (wr_comma) {
                        s += ", "
                    }
                    if (routeInstance.contest.anrFlying) {
                        s += routeInstance.getMsgArgs('fc.route.corridorwidthmissing',[])
                    } else {
                        s += routeInstance.getMsgArgs('fc.route.corridorwidtherror',[])
                    }
                    wr_comma = true
                }
                if (status.corridor_tpflags_errors) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.route.corridortpflagserrors',[status.corridor_tpflags_errors])
                    wr_comma = true
                }
                if (status.gatewidth_errors) {
                    if (wr_comma) {
                        s += ", "
                    }
                    s += routeInstance.getMsgArgs('fc.route.gatewidtherrors',[status.gatewidth_errors])
                    wr_comma = true
                }
            }
        }
        return s
    }
    
    //--------------------------------------------------------------------------
    private static String get_name(boolean enroutePhoto, String startName)
    {
        if (enroutePhoto) {
            return "${startName}.photo"
        } else {
            return "${startName}.canvas"
        }
    }
    
}
