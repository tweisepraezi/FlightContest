class CoordTitle // DB-2.5
{
	CoordType type
	int number
	
	static belongsTo = [RouteLeg,TestLegFlight,TestLegPlanning,CalcResult] // BUG: RouteLeg anstelle von RouteLegCoord & RouteLegTest angegeben, DB-2.12
	
	CoordTitle(String v)
	{
		this.type = CoordType.UNKNOWN
		this.number = 0
	}
	
	CoordTitle(CoordType type, int number)
	{
		this.type = type
		this.number = number
	}
	
	static CoordTitle GetCoordTitle(String coordTitleValue)
	{
		if (coordTitleValue.startsWith("T/O")) {
			return new CoordTitle(CoordType.TO,1)
		} else if (coordTitleValue.startsWith("TO")) {
			return new CoordTitle(CoordType.TO,1)
		} else if (coordTitleValue.startsWith("FTO")) {
			return new CoordTitle(CoordType.TO,1)
		} else if (coordTitleValue.startsWith("iTO")) {
			return new CoordTitle(CoordType.iTO,1)
		} else if (coordTitleValue.startsWith("SP")) {
			return new CoordTitle(CoordType.SP,1)
		} else if (coordTitleValue.startsWith("iSP")) {
			return new CoordTitle(CoordType.iSP,1)
		} else if (coordTitleValue.startsWith("ISP")) {
			return new CoordTitle(CoordType.iSP,1)
		} else if (coordTitleValue.startsWith("WP") || coordTitleValue.startsWith("TP")) {
			int tp_num = coordTitleValue.substring(2).toInteger()
			return new CoordTitle(CoordType.TP,tp_num)
        } else if (coordTitleValue.startsWith("SC")) {
            int sc_num = coordTitleValue.substring(2).toInteger()
            return new CoordTitle(CoordType.SECRET,sc_num)
		} else if (coordTitleValue.startsWith("UZK")) {
			int sc_num = coordTitleValue.substring(3).toInteger()
			return new CoordTitle(CoordType.SECRET,sc_num)
		} else if (coordTitleValue.startsWith("Secret")) {
			int sc_num = coordTitleValue.substring(6).toInteger()
			return new CoordTitle(CoordType.SECRET,sc_num)
		} else if (coordTitleValue.startsWith("FP")) {
			return new CoordTitle(CoordType.FP,1)
		} else if (coordTitleValue.startsWith("iFP")) {
			return new CoordTitle(CoordType.iFP,1)
		} else if (coordTitleValue.startsWith("IFP")) {
			return new CoordTitle(CoordType.iFP,1)
		} else if (coordTitleValue.startsWith("LDG")) {
			return new CoordTitle(CoordType.LDG,1)
		} else if (coordTitleValue.startsWith("iLDG")) {
			return new CoordTitle(CoordType.iLDG,1)
		}
		return new CoordTitle(CoordType.UNKNOWN,0)
	}

    String name()
	{
		switch (type) {
			case CoordType.TP:
            case CoordType.SECRET:
				return "${type}${number}"
			default:
				return "$type"
		}
	}
	
    String titleCode()
	{
		switch (type) {
			case CoordType.TP:
            case CoordType.SECRET:
				return "${getMsg(type.code)}${number}"
			default:
				return getMsg(type.code)
		}
	}
	
    String titleCode2(int addNumber)
    {
        switch (type) {
            case CoordType.TP:
            case CoordType.SECRET:
                return "${getMsg(type.code)}${number+addNumber}"
            default:
                return getMsg(type.code)
        }
    }
    
    String titlePrintCode()
	{
		switch (type) {
			case CoordType.TP:
            case CoordType.SECRET:
				return "${getPrintMsg(type.code)}${number}"
			default:
				return getPrintMsg(type.code)
		}
	}
    
    String titlePrintCode2(int addNumber)
    {
        switch (type) {
            case CoordType.TP:
            case CoordType.SECRET:
                return "${getPrintMsg(type.code)}${number+addNumber}"
            default:
                return getPrintMsg(type.code)
        }
    }
    
    String titleTrackingCode(boolean isANR)
	{
		switch (type) {
			case CoordType.TP:
                if (isANR) {
                    return "${getTrackingMsg(CoordType.SECRET.code)}${number}"
                }
                return "${getTrackingMsg(type.code)}${number}"
            case CoordType.SECRET:
                if (isANR) {
                    return "${getTrackingMsg(CoordType.SECRET.code)}${number+Defs.ANR_LIVETRACKING_SECRETPOINT_OFFSET}"
                }
                return "${getTrackingMsg(type.code)}${number}"
			default:
				return getTrackingMsg(type.code)
		}
	}
	
	String titleMediaCode(Media media)
	{
		String title = ""
        switch (media) {
            case Media.Screen:
                title = titleCode()
                break
            case Media.Print:
                title = titlePrintCode()
                break
            case Media.Tracking:
                title = titleTrackingCode(false)
                break
            case Media.TrackingANR:
                title = titleTrackingCode(true)
                break
        }
		return title
	}
	
    String titleEnrouteInput()
    {
        switch (type) {
            case CoordType.SP:
                return getMsg(type.code).substring(0,1)
            case CoordType.TP:
                return "${number}"
            case CoordType.iSP:
                return getMsg(type.code).substring(0,2)
            default:
                return "?"
        }
    }
    
    String titleExport()
    {
        switch (type) {
            case CoordType.TP:
            case CoordType.SECRET:
                return "${type.export}${number}"
            default:
                return "$type.export"
        }
    }
    
}
