enum CoordType 
{
	UNKNOWN ('Unknown', '',     0.0, '',        '',        'fc.coordtype.unknown'),
	TO      ('T/O',     'T/O',  0.0, '',        '',        'fc.coordtype.to'     ),
	SP      ('SP',      'SP',   0.0, '',        '',        'fc.coordtype.sp'     ),
    SECRET  ('Secret',  'CP',   2.0, '$secret', '$curved', 'fc.coordtype.secret' ),
	TP      ('TP',      'CP',   0.0, '',        '',        'fc.coordtype.tp'     ),
	FP      ('FP',      'FP',   0.0, '',        '',        'fc.coordtype.fp'     ),
	LDG     ('LDG',     'LDG',  0.0, '',        '',        'fc.coordtype.ldg'    ),
	iFP     ('iFPi',    'iFP',  0.0, '',        '',        'fc.coordtype.ifp'    ),
	iLDG    ('iLDGi',   'iLDG', 0.0, '',        '',        'fc.coordtype.ildg'   ),
	iTO     ('iT/Oi',   'iT/O', 0.0, '',        '',        'fc.coordtype.ito'    ),
	iSP     ('iSPi',    'iSP',  0.0, '',        '',        'fc.coordtype.isp'    )

	// "$title," muss eindeutig sein, da Werte in Task.disabledCheckPoints gespeichert werden! 
	 
	CoordType(String title, String aflosMark, Float aflosGateWidth, String secretMark,  String secretMark2, String code)
	{
		this.title = title
		this.aflosMark = aflosMark
		this.aflosGateWidth = aflosGateWidth
		this.secretMark = secretMark
		this.secretMark2 = secretMark2
		this.code = code
	}
	
	final String title
	final String aflosMark
	final Float aflosGateWidth
	final String secretMark
	final String secretMark2
	final String code
	
	CoordType GetNextValue()
	{
		switch (this) {
			case CoordType.TO:
				return CoordType.SP
			case CoordType.SP:
			case CoordType.TP:
			case CoordType.SECRET:
			case CoordType.iSP:
				return CoordType.TP
			case CoordType.FP:
				return CoordType.LDG
			case CoordType.iFP:
				return CoordType.iLDG
			case CoordType.iLDG:
				return CoordType.iTO
			case CoordType.iTO:
				return CoordType.iSP
		}
		return null
	}
	
	List ListNextValues(existiFP)
	{
		List l = []
        switch(this) {
        	case CoordType.TO:
        		l += CoordType.TO
        		l += CoordType.SP
        		break
        	case CoordType.SP:
                l += CoordType.SP
                l += CoordType.LDG
                break
            case CoordType.TP:
                l += CoordType.TP
                l += CoordType.FP
				if (!existiFP) {
					l += CoordType.iFP
				}
                break
            case CoordType.LDG:
                l += CoordType.LDG
                break
            case CoordType.SECRET:
                l += CoordType.SECRET
                break
			case CoordType.iFP:
				l += CoordType.iFP
                break
			case CoordType.iLDG:
                l += CoordType.iLDG
                l += CoordType.iTO
                l += CoordType.iSP
                break
			case CoordType.iTO:
                l += CoordType.iTO
                l += CoordType.iSP
                break
			case CoordType.iSP:
                l += CoordType.iSP
                break
		}
		return l
	}
	
	boolean IsTypeAllowed(CoordType lastType)
	{
		switch (lastType) {
			case CoordType.UNKNOWN:
				switch(this) {
					case CoordType.TO:
						return true
				}
				break
			case CoordType.TO:
				switch(this) {
					case CoordType.SP:
						return true
				}
				break
			case CoordType.SP:
			case CoordType.SECRET:
			case CoordType.TP:
				switch(this) {
					case CoordType.SECRET:
					case CoordType.TP:
					case CoordType.iFP:
					case CoordType.FP:
						return true
				}
				break
			case CoordType.iFP:
				switch(this) {
					case CoordType.iLDG:
					case CoordType.iTO:
					case CoordType.iSP:
						return true
				}
				break
			case CoordType.iLDG:
				switch(this) {
					case CoordType.iTO:
					case CoordType.iSP:
						return true
				}
				break
			case CoordType.iTO:
				switch(this) {
					case CoordType.iSP:
						return true
				}
				break
			case CoordType.iSP:
				switch(this) {
					case CoordType.SECRET:
					case CoordType.TP:
					case CoordType.FP:
						return true
				}
				break
			case CoordType.FP:
				switch(this) {
					case CoordType.LDG:
						return true
				}
				break
		}
		return false
	}
	
	boolean IsSecretAllowedCoord()
	{
		switch(this) {
			case CoordType.SP:
			case CoordType.TP:
			case CoordType.iSP:
			case CoordType.SECRET:
				return true
		}
		return false
	}
	
	boolean IsBadCourseCheckCoord()
	{
		switch(this) {
			case CoordType.TP:
			case CoordType.SECRET:
			case CoordType.FP:
			case CoordType.iFP:
				return true
		}
		return false
	}
	
	boolean IsCpTimeCheckCoord()
	{
		switch(this) {
			case CoordType.SP:
			case CoordType.iSP:
			case CoordType.TP:
			case CoordType.SECRET:
			case CoordType.FP:
			case CoordType.iFP:
				return true
		}
		return false
	}
	
	boolean IsAltitudeCheckCoord()
	{
		switch(this) {
			case CoordType.SP:
			case CoordType.iSP:
			case CoordType.TP:
			case CoordType.SECRET:
			case CoordType.FP:
			case CoordType.iFP:
				return true
		}
		return false
	}
	
	boolean IsProcedureTurnCoord()
	{
		switch(this) {
			case CoordType.TP:
				return true
		}
		return false
	}
    
    boolean IsCpCheckCoord()
    {
        if (IsCpTimeCheckCoord() || IsProcedureTurnCoord() || IsBadCourseCheckCoord() || IsAltitudeCheckCoord()) {
            return true
        }
        return false
    }
}