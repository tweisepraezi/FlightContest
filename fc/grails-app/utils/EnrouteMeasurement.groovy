enum EnrouteMeasurement // DB-2.13
{
    None ('fc.observation.enroute.none'),
    Unassigned ('fc.observation.enroute.notassigned'),
    Map ('fc.observation.enroute.map'),
    NMFromTP ('fc.observation.enroute.nmfromtp'),
    mmFromTP ('fc.observation.enroute.mmfromtp')
    //PosFromTP ('fc.observation.enroute.posfromtp')
    
    EnrouteMeasurement(String code)
    {
        this.code = code
    }
    
    boolean IsEnrouteMeasurement()
    {
        switch (this) {
            case EnrouteMeasurement.Map:
            case EnrouteMeasurement.NMFromTP:
            case EnrouteMeasurement.mmFromTP:
            //case EnrouteMeasurement.PosFromTP:
                return true
        }
        return false
    }
    
    boolean IsEnrouteMeasurementFromTP()
    {
        switch (this) {
            case EnrouteMeasurement.NMFromTP:
            case EnrouteMeasurement.mmFromTP:
            //case EnrouteMeasurement.PosFromTP:
                return true
        }
        return false
    }
    
    EnrouteRoute GetEnrouteRoute()
    {
        switch (this) {
            case EnrouteMeasurement.None:
                return EnrouteRoute.None
            case EnrouteMeasurement.Unassigned:
                return EnrouteRoute.Unassigned
            case EnrouteMeasurement.Map:
                return EnrouteRoute.InputName
            case EnrouteMeasurement.NMFromTP:
            case EnrouteMeasurement.mmFromTP:
            //case EnrouteMeasurement.PosFromTP:
                return EnrouteRoute.InputCoord
        }
        return false
    }
    
    EnrouteValueUnit GetEnrouteValueUnit()
    {
        switch (this) {
            case EnrouteMeasurement.NMFromTP:
                return EnrouteValueUnit.NM
            case EnrouteMeasurement.mmFromTP:
                return EnrouteValueUnit.mm
        }
        return null
    }
    
    final String code
}