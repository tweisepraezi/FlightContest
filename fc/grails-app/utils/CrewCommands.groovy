enum CrewCommands 
{
    SELECTCOMMAND             ('fc.crew.selectcommand'),
    DISABLECONTESTCREWS       ('fc.crew.disablecontestcrews'),
    ENABLECONTESTCREWS        ('fc.crew.enablecontestcrews'),
    DISABLETEAMCREWS          ('fc.crew.disableteamcrews'),
    ENABLETEAMCREWS           ('fc.crew.enableteamcrews'),
    DISABLEINCREASECREWS      ('fc.crew.disableincreasecrews'),
    ENABLEINCREASECREWS       ('fc.crew.enableincreasecrews'),
    DISABLECREWS              ('fc.crew.disablecrews'),
    ENABLECREWS               ('fc.crew.enablecrews'),
    EXPORTCREWS               ('fc.crew.exportcrews'),
    EXPORTPILOTS              ('fc.crew.exportpilots'),
    EXPORTNAVIGATORS          ('fc.crew.exportnavigators')
    
	CrewCommands(String titleCode)
	{
		this.titleCode = titleCode
	}
	
    static List GetValues(boolean showIncrease, boolean isCrewPilotNavigatorDelimiter)
    {
        List ret = []
        for(v in values()) {
            switch (v) {
                case DISABLEINCREASECREWS:
                case ENABLEINCREASECREWS:
                    if (showIncrease) {
                        ret += v
                    }
                    break
                case EXPORTPILOTS:
                case EXPORTNAVIGATORS:
                    if (isCrewPilotNavigatorDelimiter) {
                        ret += v
                    }
                    break
                default:
                    ret += v
                    break
            }
        }
        return ret
    }
    
	final String titleCode
	
}