enum RouteCoordType 
{
	UNKNOWN ('Unknown', '',    0, 'fc.routecoordtype.unknown'),
	TO      ('T/O',     'T/O', 0, 'fc.routecoordtype.to'),
	SP      ('SP',      'SP',  0, 'fc.routecoordtype.sp'),
    SECRET  ('Secret',  'CP',  2, 'fc.routecoordtype.secret'),
	TP      ('TP',      'CP',  0, 'fc.routecoordtype.tp'),
	FP      ('FP',      'FP',  0, 'fc.routecoordtype.fp'),
	LDG     ('LDG',     'LDG', 0, 'fc.routecoordtype.ldg')
	
	RouteCoordType(String title, String aflosMark, int aflosGateWidth, String code)
	{
		this.title = title
		this.aflosMark = aflosMark
		this.aflosGateWidth = aflosGateWidth
		this.code = code
	}
	
	final String title
	final String aflosMark
	final int aflosGateWidth
	final String code
	
	static List listNextValues(actValue)
	{
		List l = []
        switch(actValue) {
        	case RouteCoordType.TO:
        		l += RouteCoordType.TO
        		l += RouteCoordType.SP
        		break
        	case RouteCoordType.SP:
                l += RouteCoordType.SP
                l += RouteCoordType.LDG
                break
            case RouteCoordType.TP:
                l += RouteCoordType.TP
                l += RouteCoordType.FP
                break
            case RouteCoordType.LDG:
                l += RouteCoordType.LDG
                break
            case RouteCoordType.SECRET:
                l += RouteCoordType.SECRET
                break
		}
		return l
	}
}