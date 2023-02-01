import java.util.List;

class CoordRoute extends Coord
{
    static belongsTo = [route:Route]
    
    boolean HideSecret(List curvedPointIds)
    {
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
        return true
    }
    
    int GetObservationPositionTop()
    {
        return ((observationPositionTop * route.turnpointPrintStyle.height) / 100).toInteger()
    }
    
    int GetObservationPositionLeft()
    {
        return ((observationPositionLeft * route.turnpointPrintStyle.width) / 100).toInteger()
    }
    
    int GetObservationPositionPercentTop(int observationPositionTop)
    {
        return (100 * observationPositionTop / route.turnpointPrintStyle.height).toInteger()
    }
    
    int GetObservationPositionPercentLeft(int observationPositionLeft)
    {
        return (100 * observationPositionLeft / route.turnpointPrintStyle.width).toInteger()
    }
	
    long GetNextCoordRouteID(boolean searchTurnpointSign)
    {
        boolean start_found = false
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this.route,[sort:"id"]) ) {
            if (!searchTurnpointSign || coordroute_instance.type.IsTurnpointSignCoord()) {
                if (start_found) {
                    return coordroute_instance.id
                }
                if (coordroute_instance.id == this.id) {
                    start_found = true
                }
            }
        }
        return 0
    }
	
    long GetPrevCoordRouteID(boolean searchTurnpointSign)
    {
        boolean start_found = false
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this.route,[sort:"id", order:'desc']) ) {
            if (!searchTurnpointSign || coordroute_instance.type.IsTurnpointSignCoord()) {
                if (start_found) {
                    return coordroute_instance.id
                }
                if (coordroute_instance.id == this.id) {
                    start_found = true
                }
            }
        }
        return 0
    }
}
