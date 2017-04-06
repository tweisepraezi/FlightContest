enum EnrouteRoute // DB-2.13
{
    None ('fc.observation.enroute.input.none'),
    Unassigned ('fc.observation.enroute.input.notassigned'),
    InputName ('fc.observation.enroute.input.name'),
    InputCoord ('fc.observation.enroute.input.coord'),
    InputNMFromTP ('fc.observation.enroute.input.nmfromtp'),
    InputmmFromTP ('fc.observation.enroute.input.mmfromtp'),
    InputCoordmm ('fc.observation.enroute.input.coordmm')
    
    EnrouteRoute(String code)
    {
        this.code = code
    }
    
    boolean IsEnrouteRouteInput()
    {
        switch (this) {
            case EnrouteRoute.InputName:
            case EnrouteRoute.InputCoord:
            case EnrouteRoute.InputNMFromTP:
            case EnrouteRoute.InputmmFromTP:
            case EnrouteRoute.InputCoordmm:
                return true
        }
        return false
    }
    
    boolean IsEnrouteRouteInputCoord()
    {
        switch (this) {
            case EnrouteRoute.InputCoord:
            case EnrouteRoute.InputCoordmm:
                return true
        }
        return false
    }
    
    boolean IsEnrouteRouteInputDistanceFromTP()
    {
        switch (this) {
            case EnrouteRoute.InputNMFromTP:
            case EnrouteRoute.InputmmFromTP:
                return true
        }
        return false
    }
    
    boolean IsEnrouteRouteInputPosition()
    {
        switch (this) {
            case EnrouteRoute.InputCoord:
            case EnrouteRoute.InputCoordmm:
            case EnrouteRoute.InputNMFromTP:
            case EnrouteRoute.InputmmFromTP:
                return true
        }
        return false
    }
    
    EnrouteMeasurement GetEnrouteResultMeasurement()
    {
        switch (this) {
            case EnrouteRoute.InputNMFromTP:
            case EnrouteRoute.InputCoord:
                return EnrouteMeasurement.NMFromTP
            case EnrouteRoute.InputmmFromTP:
            case EnrouteRoute.InputCoordmm:
                return EnrouteMeasurement.mmFromTP
        }
        return EnrouteMeasurement.None
    }
    
    final String code
}