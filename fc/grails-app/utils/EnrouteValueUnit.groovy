enum EnrouteValueUnit 
{
    mm ('fc.mm'),
    NM ('fc.mile')
    
    EnrouteValueUnit(String code)
    {
        this.code = code
    }
    
    final String code
    
    EnrouteMeasurement GetEnrouteMeasurement()
    {
        switch (this) {
            case EnrouteValueUnit.NM:
                return EnrouteMeasurement.NMFromTP
            case EnrouteValueUnit.mm:
                return EnrouteMeasurement.mmFromTP
        }
        return EnrouteMeasurement.None
    }
}