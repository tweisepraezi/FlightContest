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
        if (test.GetFlightTestBadCoursePoints() > 0) {
            if (resultEntered) {
                if (!DisabledCheckPointsTools.Uncompress(test.task.disabledCheckPointsBadCourse).contains(title()+',')) {
                    if (resultBadCourseNum > 0) {
                        return false
                    }
                }
            }
        }
        if (test.GetFlightTestMinAltitudeMissedPoints() > 0) {
            if (!DisabledCheckPointsTools.Uncompress(test.task.disabledCheckPointsMinAltitude).contains("${title()},")) {
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
}
