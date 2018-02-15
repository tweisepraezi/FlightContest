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
}
