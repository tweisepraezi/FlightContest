import java.math.BigDecimal;
import java.text.*

class Coord
{
	CoordType type = CoordType.TO
	int titleNumber = 1
	String mark
	
	// Latitude (Geographische Breite)
    String latDirection = 'N'
	int latGrad
	BigDecimal latMinute = 0.0
    BigDecimal latGradDecimal = 0.0
    Integer latMin = 0
    BigDecimal latSecondDecimal = 0.0

    // Longitude (Geographische Laenge)
    String lonDirection = 'E'
    int lonGrad
    BigDecimal lonMinute = 0.0
    BigDecimal lonGradDecimal = 0.0
    Integer lonMin = 0
    BigDecimal lonSecondDecimal = 0.0

    int altitude = 500                   // Altitude (Höhe) in ft 
    int gatewidth = 1                    // UNUSED: gatewidth, migriert nach gatewidth2 (Float), DB-2.3
	Float gatewidth2 = 1.0f              // Gate-Breite (in NM) (Standard: 1NM, Secret: 2NM), DB-2.3
	Integer legDuration                  // duration of leg [min], DB-2.3
    Boolean endCurved = false            // End of curved, DB-2.8
	Boolean noTimeCheck = false          // No time check, DB-2.3
    Boolean noGateCheck = false          // No gate check, DB-2.8
	Boolean noPlanningTest = false       // No planning test, DB-2.6
    BigDecimal gateDirection = 270.0     // Richtung der Startbahn (T/O,LDG,iT/O,iLDG), Grad, DB-2.12
    
    // Speicher für Eingabe der Landkarten-Messung
	boolean measureEntered = false
	BigDecimal coordTrueTrack = 0        // Grad
	BigDecimal coordMeasureDistance = 0  // mm, Entfernung bis zum letzten TP/SP
    BigDecimal measureTrueTrack          // Grad
	BigDecimal measureDistance           // mm, Entfernung bis zum letzten TP/SP
    BigDecimal legMeasureDistance        // mm
    BigDecimal legDistance               // NM
    
    // Relative Position eines Secret-Points im Leg 
    BigDecimal secretLegRatio = 0
    
    // plan, results, penalties
    Date planCpTime                     = Date.parse("HH:mm","00:00")
    boolean planProcedureTurn           = false
    String resultLatitude               = ""
    String resultLongitude              = ""
    int resultAltitude                  = 0
    Date resultCpTime                   = Date.parse("HH:mm","00:00")
    String resultCpTimeInput            = "00:00:00"  // transient
    boolean resultCpNotFound            = false
    boolean resultEntered               = false
    boolean resultProcedureTurnNotFlown = false
    boolean resultProcedureTurnEntered  = false
    boolean resultMinAltitudeMissed     = false
    int resultBadCourseNum              = 0
    int penaltyCoord                    = 0           // Points
	
    static transients = ['resultCpTimeInput','latGradDecimal','lonGradDecimal','latMin','lonMin','latSecondDecimal','lonSecondDecimal']
    
    static constraints = {
		type()
        titleNumber(range:1..<100)
		mark(nullable:true)
        
        latDirection(inList:['N','S'])
        latGrad(range:0..90)
        latGradDecimal(range:-90..90, scale:5)
        latMinute(range:0..<60, scale:10, validator:{val,obj->
            if (val > 0 && obj.latGrad == 90 ) {
                return false
            }
        })
        latMin(range:0..<60)
        latSecondDecimal(range:0..<60, scale:4)
        
        lonDirection(inList:['E','W'])
        lonGrad(range:0..180, validator:{val,obj->
            if (val == 180 && obj.lonDirection == 'W') {
                return false
            }
        })
        lonGradDecimal(range:-180..180, scale:5, validator:{val,obj->
            if (val == -180) {
                return false
            }
        })
        lonMinute(range:0..<60, scale:10, validator:{val,obj->
            if (val > 0 && obj.lonGrad == 180 ) {
                return false
            }
        })
        lonMin(range:0..<60)
        lonSecondDecimal(range:0..<60, scale:4)
        
        altitude(range:0..100000)
        gatewidth()
		coordTrueTrack(scale:10)
		coordMeasureDistance(scale:10)
        measureTrueTrack(nullable:true,range:0..<360,scale:10)
		measureDistance(nullable:true,scale:10)
        legMeasureDistance(nullable:true,scale:10)
        legDistance(nullable:true,scale:10)
        resultCpTimeInput(blank:false, validator:{ val, obj ->
        	String v = val.replace('.',':')
	        switch(v.size()) {
	            case 1:
	                try {
	                    Date t = Date.parse("s",v)
	                } catch(Exception e) {
	                    return false
	                }
	                return true
	            case 2:
	                try {
	                    Date t = Date.parse("ss",v)
	                } catch(Exception e) {
	                    return false
	                }
	                return true
	            case 4:
	                try {
	                    Date t = Date.parse("m:ss",v)
	                } catch(Exception e) {
	                    return false
	                }
	                return true
	            case 5:
	                try {
	                    Date t = Date.parse("mm:ss",v)
	                } catch(Exception e) {
	                    return false
	                }
	                return true
	            case 7:
	                try {
	                    Date t = Date.parse("H:mm:ss",v)
	                } catch(Exception e) {
	                    return false
	                }
	                return true
	            case 8:
	                try {
	                    Date t = Date.parse("HH:mm:ss",v)
	                } catch(Exception e) {
	                    return false
	                }
	                return true
	        }
	        return false
	    })
	    resultAltitude(min:0)
	    resultBadCourseNum(min:0)
		secretLegRatio(scale:10)
		
		// DB-2.3 compatibility
		gatewidth2(nullable:true,scale:10,min:0.0f)
		legDuration(nullable:true,min:1)
		noTimeCheck(nullable:true)
		
		// DB-2.6 compatibility
		noPlanningTest(nullable:true)
        
        // DB-2.8 compatibility
        noGateCheck(nullable:true)
        endCurved(nullable:true)
        
        // DB-2.12 compatibility
        gateDirection(nullable:true,range:0..<360,scale:0)
    }

	void ResetResults(boolean resetProcedureTurn)
	{
	    resultLatitude              = ""
	    resultLongitude             = ""
	    resultAltitude              = 0
	    resultCpTime                = Date.parse("HH:mm","00:00")
	    resultCpTimeInput           = "00:00:00"
	    resultCpNotFound            = false
	    resultEntered               = false
		if (resetProcedureTurn) {
			resultProcedureTurnNotFlown = false
			resultProcedureTurnEntered  = false
		}
	    resultMinAltitudeMissed     = false
	    resultBadCourseNum          = 0
	    penaltyCoord                = 0
	}

	void ResetResultsProcedureTurn()
	{
	    resultProcedureTurnNotFlown = false
	    resultProcedureTurnEntered  = false
	}

	void CopyValues(Coord coordInstance)
	{
		type = coordInstance.type
		titleNumber = coordInstance.titleNumber
		mark = coordInstance.mark
		latGrad = coordInstance.latGrad
		latMinute = coordInstance.latMinute
		latDirection = coordInstance.latDirection
	    lonGrad = coordInstance.lonGrad
	    lonMinute = coordInstance.lonMinute
	    lonDirection = coordInstance.lonDirection
	    altitude = coordInstance.altitude 
	    gatewidth2 = coordInstance.gatewidth2
		measureEntered = coordInstance.measureEntered
		coordTrueTrack = coordInstance.coordTrueTrack
		coordMeasureDistance = coordInstance.coordMeasureDistance
	    measureTrueTrack = coordInstance.measureTrueTrack
		measureDistance = coordInstance.measureDistance
	    legMeasureDistance = coordInstance.legMeasureDistance
	    legDistance = coordInstance.legDistance
		legDuration = coordInstance.legDuration
        endCurved = coordInstance.endCurved
		noTimeCheck = coordInstance.noTimeCheck
        noGateCheck = coordInstance.noGateCheck
		noPlanningTest = coordInstance.noPlanningTest
	    secretLegRatio = coordInstance.secretLegRatio
        gateDirection = coordInstance.gateDirection
	    // planCpTime = coordInstance.planCpTime
	    // planProcedureTurn = coordInstance.planProcedureTurn
	    // resultLatitude = coordInstance.resultLatitude
	    // resultLongitude = coordInstance.resultLongitude
	    // resultAltitude = coordInstance.resultAltitude
	    // resultCpTime = coordInstance.resultCpTime
	    // resultCpNotFound = coordInstance.resultCpNotFound
	    // resultEntered = coordInstance.resultEntered
	    // resultProcedureTurnNotFlown = coordInstance.resultProcedureTurnNotFlown
	    // resultProcedureTurnEntered = coordInstance.resultProcedureTurnEntered
	    // resultMinAltitudeMissed = coordInstance.resultMinAltitudeMissed
	    // resultBadCourseNum = coordInstance.resultBadCourseNum
	    // penaltyCoord = coordInstance.penaltyCoord
		
		if (!this.save()) {
			throw new Exception("Coord.CopyValues could not save")
		}
	}
	
    String title()
	{
		switch (type) {
			case CoordType.TP:
            case CoordType.SECRET:
				return "${type.title}${titleNumber}"
			default:
				return type.title
		}
	}
	
    String titleCode()
	{
		switch (type) {
			case CoordType.TP:
            case CoordType.SECRET:
				return "${getMsg(type.code)}${titleNumber}"
			default:
				return getMsg(type.code)
		}
	}
	
    String titlePrintCode()
	{
		switch (type) {
			case CoordType.TP:
            case CoordType.SECRET:
				return "${getPrintMsg(type.code)}${titleNumber}"
			default:
				return getPrintMsg(type.code)
		}
	}
	
    String titleWithRatio()
    {
        switch (type) {
            case CoordType.TP:
                return "${getMsg(type.code)}${titleNumber}"
            case CoordType.SECRET:
                if (secretLegRatio > 0) {
                    return " ${getMsg(type.code)}${titleNumber} (${FcMath.RatioStr(100*secretLegRatio)}${getMsg('fc.percent')})"
                }
                return "${getMsg(type.code)}${titleNumber}"
            default:
                return getMsg(type.code)
        }
    }
    
	String titleShortMap(boolean isPrint)
	{
		String title
        if (isPrint) {
            title = titlePrintCode()
        }  else {
            title = titleCode()
        }
        /*
		switch (type) {
			case CoordType.TP:
			case CoordType.SECRET:
				title += " (${mark})"
		}
		*/
		return title
	}
	
	String titleMap()
	{
		String title = titleCode()
		switch (type) {
			case CoordType.TP:
			case CoordType.SECRET:
				title += " (${mark})"
		}
		title += " - ${name()}"
		return title
	}
	
	BigDecimal latMath()
	{
		BigDecimal ret = latGrad + latMinute/60
		if (latDirection == 'N') {
			return ret
		} else {
			return -ret
		}
	}
	
    String latName()
    {
        return latName(route.contest.coordPresentation)
    }
    
    String latName(CoordPresentation coordPresentation)
    {
        switch (coordPresentation) {
            case CoordPresentation.DEGREEMINUTE:
                return "${CoordPresentation.LAT} ${CoordPresentation.IntegerGradStr(latGrad,true)}${getMsg('fc.grad')} ${CoordPresentation.DecimalMinuteStr(latMinute)}${getMsg('fc.min')} ${latDirection}"
            case CoordPresentation.DEGREEMINUTESECOND:
                return "${CoordPresentation.LAT} ${CoordPresentation.IntegerGradStr(latGrad,true)}${getMsg('fc.grad')} ${CoordPresentation.IntegerMinuteStr(latMinute)}${getMsg('fc.min')} ${CoordPresentation.DecimalSecondStr(latMinute)}${getMsg('fc.sec')} ${latDirection}"
        }
        return "${CoordPresentation.LAT} ${CoordPresentation.DecimalGradStr(latMath())}${getMsg('fc.grad')}" // CoordPresentation.DEGREE
    }

    String latPrintName()
    {
        return latPrintName(route.contest.coordPresentation)
    }

    String latPrintName(CoordPresentation coordPresentation)
    {
        switch (coordPresentation) {
            case CoordPresentation.DEGREEMINUTE:
                return "${CoordPresentation.LAT} ${CoordPresentation.IntegerGradStr(latGrad,true)}${getPrintMsg('fc.grad')} ${CoordPresentation.DecimalMinuteStr(latMinute)}${getPrintMsg('fc.min')} ${latDirection}"
            case CoordPresentation.DEGREEMINUTESECOND:
                return "${CoordPresentation.LAT} ${CoordPresentation.IntegerGradStr(latGrad,true)}${getPrintMsg('fc.grad')} ${CoordPresentation.IntegerMinuteStr(latMinute)}${getPrintMsg('fc.min')} ${CoordPresentation.DecimalSecondStr(latMinute)}${getPrintMsg('fc.sec')} ${latDirection}"
        }
        return "${CoordPresentation.LAT} ${CoordPresentation.DecimalGradStr(latMath())}${getPrintMsg('fc.grad')}" // CoordPresentation.DEGREE
    }

	BigDecimal lonMath()
	{
		BigDecimal ret = lonGrad + lonMinute/60
		if (lonDirection == 'E') {
			return ret
		} else {
			return -ret
		}
	}
	
    String lonName()
    {
        return lonName(route.contest.coordPresentation)
    }
    
    String lonName(CoordPresentation coordPresentation)
    {
        switch (coordPresentation) {
            case CoordPresentation.DEGREEMINUTE:
                return "${CoordPresentation.LON} ${CoordPresentation.IntegerGradStr(lonGrad,false)}${getMsg('fc.grad')} ${CoordPresentation.DecimalMinuteStr(lonMinute)}${getMsg('fc.min')} ${lonDirection}"
            case CoordPresentation.DEGREEMINUTESECOND:
                return "${CoordPresentation.LON} ${CoordPresentation.IntegerGradStr(lonGrad,false)}${getMsg('fc.grad')} ${CoordPresentation.IntegerMinuteStr(lonMinute)}${getMsg('fc.min')} ${CoordPresentation.DecimalSecondStr(lonMinute)}${getMsg('fc.sec')} ${lonDirection}"
        }
        return "${CoordPresentation.LON} ${CoordPresentation.DecimalGradStr(lonMath())}${getMsg('fc.grad')}" // CoordPresentation.DEGREE
    }

    String lonPrintName()
    {
        return lonPrintName(route.contest.coordPresentation)
    }
    
    String lonPrintName(CoordPresentation coordPresentation)
    {
        switch (coordPresentation) {
            case CoordPresentation.DEGREEMINUTE:
                return "${CoordPresentation.LON} ${CoordPresentation.IntegerGradStr(lonGrad,false)}${getPrintMsg('fc.grad')} ${CoordPresentation.DecimalMinuteStr(lonMinute)}${getPrintMsg('fc.min')} ${lonDirection}"
            case CoordPresentation.DEGREEMINUTESECOND:
                return "${CoordPresentation.LON} ${CoordPresentation.IntegerGradStr(lonGrad,false)}${getPrintMsg('fc.grad')} ${CoordPresentation.IntegerMinuteStr(lonMinute)}${getPrintMsg('fc.min')} ${CoordPresentation.DecimalSecondStr(lonMinute)}${getPrintMsg('fc.sec')} ${lonDirection}"
        }
        return "${CoordPresentation.LON} ${CoordPresentation.DecimalGradStr(lonMath())}${getPrintMsg('fc.grad')}" // CoordPresentation.DEGREE
    }

	String name()
	{
		return "${latName()} ${lonName()}"
	}

	String givenName()
	{
		String s = "${getMsg('fc.test.results.given')} ${titleCode()} ${mark}:"
		if (resultCpNotFound) {
			s += " ${GetCpNotFoundName()}" 
		}
		s += " ${FcMath.TimeStr(resultCpTime)}"
		s += " ${resultAltitude}${getMsg('fc.foot')}" 
		if (type.IsBadCourseCheckCoord()) {
			s += " ${resultBadCourseNum} ${getMsg('fc.badcoursenum')}"
		}
		s += "."
		return s
	}
	
	String givenProcedureTurn()
	{
		String s = "${getMsg('fc.test.results.given')}: "
		if (resultProcedureTurnNotFlown) {
			s += getMsg('fc.flighttest.procedureturnnotflown')
		} else {
			s += getMsg('fc.flighttest.procedureturnflown')
		}
		s += "."
		return s
	}
	
	String namePrintable(boolean printSettings, boolean showAllCoordPresentations)
	{
		String print_name = ""
        if (showAllCoordPresentations) {
            print_name = "${latPrintName(CoordPresentation.DEGREE)}, ${lonPrintName(CoordPresentation.DEGREE)}"
        } else {
            print_name = "${latPrintName()}, ${lonPrintName()}"
        }
		if (printSettings) {
	    	if (measureDistance != null || measureTrueTrack != null || legDuration != null || noTimeCheck || noGateCheck || noPlanningTest) {
				print_name += " ("
			}
	    	if (measureTrueTrack != null) {
	    		print_name += "${FcMath.RouteGradStr(measureTrueTrack)}${getPrintMsg('fc.grad')}"
				if (measureDistance != null || legDuration != null || noTimeCheck || noGateCheck || noPlanningTest) {
					print_name += "; "
				}
	    	}
	    	if (measureDistance != null) {
	    		print_name += "${FcMath.DistanceMeasureStr(measureDistance)}${getPrintMsg('fc.mm')}"
				if (legDuration != null || noTimeCheck || noGateCheck || noPlanningTest) {
					print_name += "; "
				}
	    	}
			if (legDuration != null) {
				print_name += "${legDuration}${getPrintMsg('fc.time.min')}"
				if (noTimeCheck || noGateCheck || noPlanningTest) {
					print_name += "; "
				}
			}
			if (noTimeCheck) {
				print_name += "${getPrintMsg('fc.notimecheck.print')}"
				if (noGateCheck || noPlanningTest) {
					print_name += "; "
				}
			}
            if (noGateCheck) {
                print_name += "${getPrintMsg('fc.nogatecheck.print')}"
                if (noPlanningTest) {
                    print_name += "; "
                }
            }
			if (noPlanningTest) {
				print_name += "${getPrintMsg('fc.noplanningtest.print')}"
			}
	    	if (measureDistance != null || measureTrueTrack != null || legDuration != null || noTimeCheck || noGateCheck || noPlanningTest) {
				print_name += ")"
			}
		}
        if (showAllCoordPresentations) {
            print_name += "<br/>"
            print_name += "${latPrintName(CoordPresentation.DEGREEMINUTE)}, ${lonPrintName(CoordPresentation.DEGREEMINUTE)}"
            print_name += "<br/>"
            print_name += "${latPrintName(CoordPresentation.DEGREEMINUTESECOND)}, ${lonPrintName(CoordPresentation.DEGREEMINUTESECOND)}"
        }
		return print_name
	}

	String coordTrueTrackName()
	{
		return "${FcMath.RouteGradStr(coordTrueTrack)}${getMsg('fc.grad')}"
	}
	
	String coordMeasureDistanceName()
	{
		return  "${FcMath.DistanceMeasureStr(coordMeasureDistance)}${getMsg('fc.mm')}"
	}
	
	String measureDistanceName()
    {
    	if (measureDistance != null) {
    		return  "${FcMath.DistanceMeasureStr(measureDistance)}${getMsg('fc.mm')}"
    	}
    	return "-"
    }
    
    String measureTrueTrackName()
    {
    	if (measureTrueTrack != null) {
    		return  "${FcMath.RouteGradStr(measureTrueTrack)}${getMsg('fc.grad')}"
    	}
    	return "-"
    }
	
	String legDurationName()
	{
		if (legDuration != null) {
			return  "${legDuration}${getMsg('fc.time.min')}"
		}
		return "-"

	}
	
	String GetCpNotFoundName()
	{
		switch(type) {
			case CoordType.TO:
				return getMsg('fc.flighttest.takeoffnotfound')
			case CoordType.LDG:
				return getMsg('fc.flighttest.landingnotfound')
		}
		return getMsg('fc.flighttest.cpnotfound')
	}

	String GetCpNotFoundShortName()
	{
		switch(type) {
			case CoordType.TO:
				return getMsg('fc.flighttest.takeoffnotfound.short')
			case CoordType.LDG:
				return getMsg('fc.flighttest.landingnotfound.short')
		}
		return getMsg('fc.flighttest.cpnotfound.short')
	}
    
    String GetResultLocalTime()
    {
        return FcMath.TimeStr(resultCpTime)
    }
}