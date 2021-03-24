
class HTMLFilter {
	
	static String NoWrapStr(String sourceScr)
	{
		return sourceScr.replace(' ','&#160;') // = 00A0 - Dauerleerzeichen
	}
	
    static String GetStr(String sourceStr)
    {
        String s = sourceStr
        s = s.replace(' ',"%20")
        s = s.replace('+',"%2b")
        return s
    }
    
    static String GetStr(List sourceList)
    {
        return sourceList.inspect().replace(' ',"%20")
    }
    
    static String GetStr2(List sourceList)
    {
        return sourceList.inspect().replace(' ',"%20").replace('/',"%2f").replace(':',"%3a").replace(',',"%2c").replace('[',"%5b").replace(']',"%5d")
    }
    
    static List GetList(String sourceStr)
    {
        return Eval.me(sourceStr.replace("%20",' '))
    }
    
    static String EncodeAsHTML(String sourceScr)
    // repair iText problems
    {
        String s = sourceScr
        s = s.replace('&','&amp;')
        return s
    }
    
    static String FilterParam(String paramStr)
    // repair params problem with GRAD
    {
        return paramStr.replaceAll("\u00c2", "") // Zeichen vor Grad entfernen
    }
}
