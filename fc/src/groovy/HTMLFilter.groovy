
class HTMLFilter {
	
	static String NoWrapStr(String sourceScr)
	{
		return sourceScr.replace(' ','&#160;') // = 00A0 - Dauerleerzeichen
	}
	
    static String GetStr(List sourceList)
    {
        return sourceList.inspect().replace(' ',"%20")
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
}
