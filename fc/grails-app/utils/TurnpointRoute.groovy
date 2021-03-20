enum TurnpointRoute // DB-2.13
{
    Unassigned ('fc.observation.turnpoint.notassigned'), 
    None ('fc.observation.turnpoint.none'),
    AssignPhoto ('fc.observation.turnpoint.assign.photo'),
    AssignCanvas ('fc.observation.turnpoint.assign.canvas'),
    TrueFalsePhoto ('fc.observation.turnpoint.truefalse.photo')
    
    TurnpointRoute(String code)
    {
        this.code = code
    }
    
    boolean IsTurnpointSign()
    {
        switch (this) {
            case TurnpointRoute.AssignPhoto:
            case TurnpointRoute.AssignCanvas:
            case TurnpointRoute.TrueFalsePhoto:
                return true
        }
        return false
    }
    
    final String code
}