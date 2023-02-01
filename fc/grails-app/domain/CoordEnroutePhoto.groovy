class CoordEnroutePhoto extends Coord
{
    static belongsTo = [route:Route]

    int GetObservationPositionTop()
    {
        return ((observationPositionTop * route.enroutePhotoPrintStyle.height) / 100).toInteger()
    }
    
    int GetObservationPositionLeft()
    {
        return ((observationPositionLeft * route.enroutePhotoPrintStyle.width) / 100).toInteger()
    }
    
    int GetObservationPositionPercentTop(int observationPositionTop)
    {
        return (100 * observationPositionTop / route.enroutePhotoPrintStyle.height).toInteger()
    }
    
    int GetObservationPositionPercentLeft(int observationPositionLeft)
    {
        return (100 * observationPositionLeft / route.enroutePhotoPrintStyle.width).toInteger()
    }
	
    long GetNextCoordEnroutePhotoID()
    {
        boolean start_found = false
        for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(this.route,[sort:"enrouteViewPos"]) ) {
            if (start_found) {
                return coordenroutephoto_instance.id
            }
			if (coordenroutephoto_instance.id == this.id) {
				start_found = true
			}
        }
        return 0
    }
	
    long GetPrevCoordEnroutePhotoID()
    {
        boolean start_found = false
        for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(this.route,[sort:"enrouteViewPos", order:'desc']) ) {
            if (start_found) {
                return coordenroutephoto_instance.id
            }
			if (coordenroutephoto_instance.id == this.id) {
				start_found = true
			}
        }
        return 0
    }
}
