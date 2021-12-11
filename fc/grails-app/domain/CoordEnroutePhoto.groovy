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
}
