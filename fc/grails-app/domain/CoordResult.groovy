class CoordResult extends Coord
{
	static belongsTo = [test:Test]
    
    boolean HideSecret(List curvedPointIds) {
        boolean id_found = false
        for (curved_point_id in curvedPointIds) {
            if (id == curved_point_id) {
                id_found = true
                break
            }
        }
        if (!id_found) {
            return false
        }
        if (type != CoordType.SECRET) {
            return false
        }
        if (penaltyCoord != 0) {
            return false
        }
        Route route_instance = test.flighttestwind.flighttest.route
        if (test.GetFlightTestBadCoursePoints() > 0) {
            if (resultEntered) {
                if (!DisabledCheckPointsTools.Uncompress(test.task.disabledCheckPointsBadCourse,route_instance).contains(title()+',')) {
                    if (resultBadCourseNum > 0) {
                        return false
                    }
                }
            }
        }
        if (test.GetFlightTestMinAltitudeMissedPoints() > 0) {
            if (!DisabledCheckPointsTools.Uncompress(test.task.disabledCheckPointsMinAltitude,route_instance).contains("${title()},")) {
                if (resultAltitude && resultMinAltitudeMissed) {
                    return false
                }
            }
        }
        return true
    }

    int GetBadCoursePenalties() {
        if (resultBadCourseNum) {
            int badcourse_penalties = resultBadCourseNum * test.GetFlightTestBadCoursePoints()
            int badcourse_maxpenalties = test.GetFlightTestBadCourseMaxPoints()
            if (badcourse_maxpenalties > 0 && badcourse_penalties > badcourse_maxpenalties) {
                badcourse_penalties = badcourse_maxpenalties
            }
            return badcourse_penalties
        }
        return 0
    }
    
    int GetOutsideCorridorPenalties() {
        if (resultOutsideCorridorSeconds) {
            int outsidecorridor_penalties = resultOutsideCorridorSeconds * test.GetFlightTestOutsideCorridorPointsPerSecond()
            return outsidecorridor_penalties
        }
        return 0
    }
    
    long GetNextCoordResultID()
    {
        boolean start_found = false
        for (CoordResult coordresult_instance in CoordResult.findAllByTest(this.test,[sort:"id"]) ) {
            if (true || coordresult_instance.type.IsTurnpointSignCoord()) {
                if (start_found) {
                    return coordresult_instance.id
                }
                if (coordresult_instance.id == this.id) {
                    start_found = true
                }
            }
        }
        return 0
    }
	
    long GetPrevCoordResultID()
    {
        boolean start_found = false
        for (CoordResult coordresult_instance in CoordResult.findAllByTest(this.test,[sort:"id", order:'desc']) ) {
            if (true || coordresult_instance.type.IsTurnpointSignCoord()) {
                if (start_found) {
                    return coordresult_instance.id
                }
                if (coordresult_instance.id == this.id) {
                    start_found = true
                }
            }
        }
        return 0
    }
    
    int GetCooordResultNo()
    {
        int coord_no = 0
        for (CoordResult coordresult_instance in CoordResult.findAllByTest(this.test,[sort:"id"]) ) {
            if (true || coordresult_instance.type.IsTurnpointSignCoord()) {
                if (coordresult_instance.planProcedureTurn) {
                    coord_no++
                }
                coord_no++
                if (coordresult_instance.id == this.id) {
                    return coord_no
                }
            }
        }
        return 0
    }
}
