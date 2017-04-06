enum TurnpointCorrect // DB-2.13
{
    Unassigned ('fc.observation.turnpoint.notassigned'), 
    True ('fc.observation.turnpoint.true'),
    False ('fc.observation.turnpoint.false')
    
    TurnpointCorrect(String code)
    {
        this.code = code
    }
    
    final String code
}