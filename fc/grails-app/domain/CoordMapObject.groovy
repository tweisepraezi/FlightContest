class CoordMapObject extends Coord
{
    static belongsTo = [route:Route]

    long GetNextCoordMapObjectID()
    {
        boolean start_found = false
        for (CoordMapObject coordmapobject_instance in CoordMapObject.findAllByRoute(this.route,[sort:"enrouteViewPos"]) ) {
            if (start_found) {
                return coordmapobject_instance.id
            }
			if (coordmapobject_instance.id == this.id) {
				start_found = true
			}
        }
        return 0
    }
	
    long GetPrevCoordMapObjectID()
    {
        boolean start_found = false
        for (CoordMapObject coordmapobject_instance in CoordMapObject.findAllByRoute(this.route,[sort:"enrouteViewPos", order:'desc']) ) {
            if (start_found) {
                return coordmapobject_instance.id
            }
			if (coordmapobject_instance.id == this.id) {
				start_found = true
			}
        }
        return 0
    }
}
