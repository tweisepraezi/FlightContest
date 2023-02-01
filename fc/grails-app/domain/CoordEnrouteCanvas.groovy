class CoordEnrouteCanvas extends Coord
{
    static belongsTo = [route:Route]

    long GetNextCoordEnrouteCanvasID()
    {
        boolean start_found = false
        for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(this.route,[sort:"enrouteViewPos"]) ) {
            if (start_found) {
                return coordenroutecanvas_instance.id
            }
			if (coordenroutecanvas_instance.id == this.id) {
				start_found = true
			}
        }
        return 0
    }
	
    long GetPrevCoordEnrouteCanvasID()
    {
        boolean start_found = false
        for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(this.route,[sort:"enrouteViewPos", order:'desc']) ) {
            if (start_found) {
                return coordenroutecanvas_instance.id
            }
			if (coordenroutecanvas_instance.id == this.id) {
				start_found = true
			}
        }
        return 0
    }
}
