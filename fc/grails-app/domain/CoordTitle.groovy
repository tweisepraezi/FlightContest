class CoordTitle // DB-2.5
{
	CoordType type
	int number
	
	static belongsTo = [RouteLegCoord,RouteLegTest]
	
	CoordTitle(CoordType type, int number)
	{
		this.type = type
		this.number = number
	}
	
	static CoordTitle GetCoordTitle(String coordTitleValue)
	{
		if (coordTitleValue.startsWith("T/O")) {
			return new CoordTitle(CoordType.TO, 0)
		} else if (coordTitleValue.startsWith("SP")) {
			return new CoordTitle(CoordType.SP,0)
		} else if (coordTitleValue.startsWith("WP") || coordTitleValue.startsWith("TP")) {
			int tp_num = coordTitleValue.substring(2).toInteger()
			return new CoordTitle(CoordType.TP,tp_num)
		} else if (coordTitleValue.startsWith("UZK")) {
			int secret_num = coordTitleValue.substring(3).toInteger()
			return new CoordTitle(CoordType.SECRET,secret_num)
		} else if (coordTitleValue.startsWith("Secret")) {
			int secret_num = coordTitleValue.substring(6).toInteger()
			return new CoordTitle(CoordType.SECRET,secret_num)
		} else if (coordTitleValue.startsWith("FP")) {
			return new CoordTitle(CoordType.FP,0)
		} else if (coordTitleValue.startsWith("LDG")) {
			return new CoordTitle(CoordType.LDG,9)
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
	
    String titlePrintCode()
	{
		switch (type) {
			case CoordType.TP:
            case CoordType.SECRET:
				return "${getPrintMsg(type.code)}${number}"
			default:
				return getMsg(type.code)
		}
	}
}
