
class HTMLFilter {
	
	static String NoWrapStr(String sourceScr)
	{
		return sourceScr.replace(' ','&#160;') // = 00A0 - Dauerleerzeichen
	}
	
}
