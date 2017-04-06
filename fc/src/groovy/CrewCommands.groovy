enum CrewCommands 
{
    SELECTCOMMAND           ('fc.crew.selectcommand'),
    DISABLECONTESTCREWS     ('fc.crew.disablecontestcrews'),
    ENABLECONTESTCREWS      ('fc.crew.enablecontestcrews'),
    DISABLETEAMCREWS        ('fc.crew.disableteamcrews'),
    ENABLETEAMCREWS         ('fc.crew.enableteamcrews'),
    DISABLEINCREASECREWS    ('fc.crew.disableincreasecrews'),
    ENABLEINCREASECREWS     ('fc.crew.enableincreasecrews'),
    DISABLECREWS            ('fc.crew.disablecrews'),
    ENABLECREWS             ('fc.crew.enablecrews')
    
	CrewCommands(String titleCode)
	{
		this.titleCode = titleCode
	}
	
    static List GetValues(boolean showIncrease)
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
                default:
                    ret += v
                    break
            }
        }
        return ret
    }
    
	final String titleCode
	
}