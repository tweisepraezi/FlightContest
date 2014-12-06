enum CrewCommands 
{
    SELECTCOMMAND           ('fc.crew.selectcommand'),
    DISABLECONTESTCREWS     ('fc.crew.disablecontestcrews'),
    ENABLECONTESTCREWS      ('fc.crew.enablecontestcrews'),
    DISABLETEAMCREWS        ('fc.crew.disableteamcrews'),
    ENABLETEAMCREWS         ('fc.crew.enableteamcrews'),
    DISABLECREWS            ('fc.crew.disablecrews'),
    ENABLECREWS             ('fc.crew.enablecrews')
    
	CrewCommands(String titleCode)
	{
		this.titleCode = titleCode
	}
	
	final String titleCode
	
}