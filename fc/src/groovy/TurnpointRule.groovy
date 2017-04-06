enum TurnpointRule // DB-2.13
{
    None ('fc.observation.turnpoint.none'),
    AssignPhoto ('fc.observation.turnpoint.assign.photo'),
    AssignCanvas ('fc.observation.turnpoint.assign.canvas'),
    TrueFalsePhoto ('fc.observation.turnpoint.truefalse.photo'),
    AssignTrueFalse ('fc.observation.turnpoint.assigntruefalse')
    
    TurnpointRule(String code)
    {
        this.code = code
    }
    
    TurnpointRoute GetTurnpointRoute()
    {
        switch (this) {
            case TurnpointRule.None:
                return TurnpointRoute.None
            case TurnpointRule.AssignPhoto:
                return TurnpointRoute.AssignPhoto
            case TurnpointRule.AssignCanvas:
                return TurnpointRoute.AssignCanvas
            case TurnpointRule.TrueFalsePhoto:
                return TurnpointRoute.TrueFalsePhoto
            case TurnpointRule.AssignTrueFalse:
                return TurnpointRoute.Unassigned
        }
    }
    
    final String code
}