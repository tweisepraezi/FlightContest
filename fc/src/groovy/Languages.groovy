enum Languages 
{
	de ('Deutsch'),
	en ('English')
	
	Languages(String title)
	{
		this.title = title
	}
	
	final String title
	
    //--------------------------------------------------------------------------
    static String GetLanguageDecimal(String showLanguage, String stringValue)
    {
        switch (showLanguage) {
            case Languages.de.toString():
                return stringValue.replace('.',',')
                break
            case Languages.en.toString():
                return stringValue.replace(',','.')
                break
        }
        return stringValue
    }

}