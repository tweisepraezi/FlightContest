enum EnrouteRule // DB-2.13
{
    None ('fc.observation.enroute.none'),
    Map ('fc.observation.enroute.map'),
    NMFromTP ('fc.observation.enroute.nmfromtp'),
    mmFromTP ('fc.observation.enroute.mmfromtp'),
    //PosFromTP ('fc.observation.enroute.posfromtp'),
    MapOrMeasurement ('fc.observation.enroute.mapormeasurement')
    
    EnrouteRule(String code)
    {
        this.code = code
    }
    
    EnrouteMeasurement GetEnrouteMeasurement()
    {
        switch (this) {
            case EnrouteRule.None:
                return EnrouteMeasurement.None
            case EnrouteRule.Map:
                return EnrouteMeasurement.Map
            case EnrouteRule.NMFromTP:
                return EnrouteMeasurement.NMFromTP
            case EnrouteRule.mmFromTP:
                return EnrouteMeasurement.mmFromTP
            //case EnrouteRule.PosFromTP:
            //    return EnrouteMeasurement.PosFromTP
            case EnrouteRule.MapOrMeasurement:
                return EnrouteMeasurement.Unassigned
        }
    }
    
    final String code
}