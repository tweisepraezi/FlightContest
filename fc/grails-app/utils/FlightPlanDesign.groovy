enum FlightPlanDesign // DB-2.41
{
    TPList    ('fc.test.flightplan.anr.tplist'),
    OnlyTimes ('fc.test.flightplan.anr.onlytimes'),
    Map       ('fc.test.flightplan.anr.map')
    
    FlightPlanDesign(String code)
    {
        this.code = code
    }
    
    final String code
}