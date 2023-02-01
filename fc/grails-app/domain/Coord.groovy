import java.util.Map;

import java.math.BigDecimal;
import java.text.*

class Coord
{
	CoordType type = CoordType.TO
	int titleNumber = 1
	String mark
	
	// Latitude (Geographische Breite)
    String latDirection = CoordPresentation.NORTH
	int latGrad
	BigDecimal latMinute = 0.0
    BigDecimal latGradDecimal = 0.0
    Integer latMin = 0
    BigDecimal latSecondDecimal = 0.0

    // Longitude (Geographische Laenge)
    String lonDirection = CoordPresentation.EAST
    int lonGrad
    BigDecimal lonMinute = 0.0
    BigDecimal lonGradDecimal = 0.0
    Integer lonMin = 0
    BigDecimal lonSecondDecimal = 0.0

    int altitude = 500                   // Altitude (Höhe) des Bodens über Meeresspiegel in ft
    Integer minAltitudeAboveGround = 0   // DB-2.34, min. Altitude (Höhe) über Grund in ft, wenn > 0
    Integer maxAltitudeAboveGround = 0   // DB-2.34, max. Altitude (Höhe) über Grund in ft, wenn > 0
    int gatewidth = 1                    // UNUSED: Coord.gatewidth, migriert nach gatewidth2 (Float), DB-2.3
	Float gatewidth2 = 1.0f              // Gate-Breite (in NM) (Standard: 1NM, Secret: 2NM), DB-2.3
	Integer legDuration                  // duration of leg [min], DB-2.3
    Boolean endCurved = false            // End of curved, DB-2.8
	Boolean noTimeCheck = false          // No time check, DB-2.3
    Boolean noGateCheck = false          // No gate check, DB-2.8
	Boolean noPlanningTest = false       // No planning test, DB-2.6
    BigDecimal gateDirection = 270.0     // Richtung der Startbahn (T/O,LDG,iT/O,iLDG), Grad, DB-2.12
    Boolean circleCenter = false         // DB-2.26
    Boolean semiCircleInvert = false     // DB-2.27
    
    // Speicher für Eingabe der Landkarten-Messung
	boolean measureEntered = false       // UNUSED: Coord.measureEntered, ersetzt durch Is...Measure(), DB-2.13
	BigDecimal coordTrueTrack = 0        // Richting in Grad (aus Koordinaten berechnet)
	BigDecimal coordMeasureDistance = 0  // Entfernung vom letzten TP in mm (aus Koordinaten berechnet)
    BigDecimal measureTrueTrack          // Gemessene Richting in Grad
	BigDecimal measureDistance           // Gemessene Entfernung vom letzten TP in mm
    BigDecimal legMeasureDistance        // Entfernung vom letzten Punkt in mm (aus measureDistance berechnet) 
    BigDecimal legDistance               // Entfernung vom letzten Punkt in NM (aus measureDistance berechnet)
    
    // Relative Position eines Secret-Points im Leg 
    BigDecimal secretLegRatio = 0
    
    // TurnPoint signs
    TurnpointSign assignedSign = TurnpointSign.None            // DB-2.13
    TurnpointCorrect correctSign = TurnpointCorrect.Unassigned // DB-2.13
    
    // Enroute objects
    Integer enrouteViewPos = 0                // Photo-Position, DB-2.13 
    String enroutePhotoName = ""              // Photo-Name, DB-2.13
    EnrouteCanvasSign enrouteCanvasSign = EnrouteCanvasSign.None // DB-2.13
    BigDecimal enrouteDistance                // Gemessene Entfernung vom letzten TP in NM, DB-2.13
    Boolean enrouteDistanceOk = true          // Gemessene Entfernung im Leg, DB-2.13
    Integer enrouteOrthogonalDistance = 0     // Senkrechte Entfernung von der Strecke in m, < 0 links, > 0 rechts vom Kurs, berechnet, DB-2.13
    CoordTitle enrouteCoordTitle              // transienter Wert zur Auswahl
    String enrouteLastPhotoName = ""          // transienter Wert zur Eingabe eines Zaheln-Bereiches
    
    Integer observationPositionTop = 50       // DB-2.28, DB-2.29 percent
    Integer observationPositionLeft = 50      // DB-2.28, DB-2.29 percent
    Boolean observationNextPrintPage = false  // DB-2.28, Turnpoint
    Boolean observationNextPrintPageEnroute = false  // DB-2.30
    
	static hasOne = [imagecoord:ImageCoord]   // DB-2.28
    
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
	
    static transients = ['resultCpTimeInput','latGradDecimal','lonGradDecimal','latMin','lonMin','latSecondDecimal','lonSecondDecimal','enrouteCoordTitle','enrouteLastPhotoName']
    
    static constraints = {
		type()
        titleNumber(range:1..<100)
		mark(nullable:true)
        
        latDirection(inList:[CoordPresentation.NORTH,CoordPresentation.SOUTH])
        latGrad(range:0..90)
        latGradDecimal(range:-90..90, scale:5)
        latMinute(range:0..<60, scale:10, validator:{val,obj->
            if (val > 0 && obj.latGrad == 90 ) {
                return false
            }
        })
        latMin(range:0..<60)
        latSecondDecimal(range:0..<60, scale:4)
        
        lonDirection(inList:[CoordPresentation.EAST,CoordPresentation.WEST])
        lonGrad(range:0..180, validator:{val,obj->
            if (val == 180 && obj.lonDirection == CoordPresentation.WEST) {
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
        enrouteCoordTitle(nullable:true)
        enrouteLastPhotoName(nullable:true)
        
        altitude(range:0..100000)
        gatewidth()
		coordTrueTrack(scale:10)
		coordMeasureDistance(scale:10)
        measureTrueTrack(nullable:true,range:0..360,scale:10)
		measureDistance(nullable:true,scale:10)
        legMeasureDistance(nullable:true,scale:10)
        legDistance(nullable:true,scale:10)
        resultCpTimeInput(blank:false, validator:{ val, obj ->
        	String v = FcTime.GetInputTimeStr(val)
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
        gateDirection(nullable:true,range:0..360,scale:0)
        
        // DB-2.13 compatibility
        assignedSign(nullable:true)
        correctSign(nullable:true)
        enrouteViewPos(nullable:true)
        enroutePhotoName(nullable:true,size:0..5)
        enrouteCanvasSign(nullable:true)
        enrouteDistance(nullable:true,scale:10,min:0.0)
        enrouteDistanceOk(nullable:true)
        enrouteOrthogonalDistance(nullable:true)
        
        // DB-2.26 compatibility
        circleCenter(nullable:true)
        
        // DB-2.27 compatibility
        semiCircleInvert(nullable:true)
        
        // DB-2.28 compatibility
        observationPositionTop(nullable:true)
        observationPositionLeft(nullable:true)
        observationNextPrintPage(nullable:true)
        imagecoord(nullable:true)
        
        // DB-2.30 compatibility
        observationNextPrintPageEnroute(nullable:true)
        
        // DB-2.34 compatibility
        minAltitudeAboveGround(nullable:true, min:0)
        maxAltitudeAboveGround(nullable:true, min:0)
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
        minAltitudeAboveGround = coordInstance.minAltitudeAboveGround 
        maxAltitudeAboveGround = coordInstance.maxAltitudeAboveGround 
	    gatewidth2 = coordInstance.gatewidth2
		coordTrueTrack = coordInstance.coordTrueTrack
		coordMeasureDistance = coordInstance.coordMeasureDistance
	    measureTrueTrack = coordInstance.measureTrueTrack
		measureDistance = coordInstance.measureDistance
        enrouteViewPos = coordInstance.enrouteViewPos
        enroutePhotoName = coordInstance.enroutePhotoName
        enrouteCanvasSign = coordInstance.enrouteCanvasSign
        enrouteDistance = coordInstance.enrouteDistance
        enrouteDistanceOk = coordInstance.enrouteDistanceOk
        enrouteOrthogonalDistance = coordInstance.enrouteOrthogonalDistance
	    legMeasureDistance = coordInstance.legMeasureDistance
	    legDistance = coordInstance.legDistance
		legDuration = coordInstance.legDuration
        endCurved = coordInstance.endCurved
		noTimeCheck = coordInstance.noTimeCheck
        noGateCheck = coordInstance.noGateCheck
		noPlanningTest = coordInstance.noPlanningTest
	    secretLegRatio = coordInstance.secretLegRatio
        gateDirection = coordInstance.gateDirection
        circleCenter = coordInstance.circleCenter
        semiCircleInvert = coordInstance.semiCircleInvert
        assignedSign = coordInstance.assignedSign
        correctSign = coordInstance.correctSign
        observationPositionTop = coordInstance.observationPositionTop
        observationPositionLeft = coordInstance.observationPositionLeft
        observationNextPrintPage = coordInstance.observationNextPrintPage
        observationNextPrintPageEnroute = coordInstance.observationNextPrintPageEnroute
        // planCpTime = coordInstance.planCpTime
	    planProcedureTurn = coordInstance.planProcedureTurn
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
        
        if (coordInstance.imagecoord) {
            ImageCoord imagecoord_instance = new ImageCoord(imageData:coordInstance.imagecoord.imageData, coord:this)
            if (!imagecoord_instance.save()) {
                throw new Exception("Coord.CopyValues (ImageCoord) could not save")
            }
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
	
    String titleCode(boolean isPrint = false)
	{
        if (isPrint) {
            return titlePrintCode()
        }
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
	
    String titleTrackingCode()
	{
		switch (type) {
			case CoordType.TP:
            case CoordType.SECRET:
				return "${getTrackingMsg(type.code)}${titleNumber}"
			default:
				return getTrackingMsg(type.code)
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
                title = titleTrackingCode()
                break
        }
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
	
    String titleExport()
    {
        switch (type) {
            case CoordType.TP:
            case CoordType.SECRET:
                return "${type.export}${titleNumber}"
            default:
                return type.export
        }
    }
    
	BigDecimal latMath()
	{
		BigDecimal ret = latGrad + latMinute/60
		if (latDirection == CoordPresentation.NORTH) {
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
        return coordPresentation.GetCoordName(this, true)
    }

    String latPrintName()
    {
        return latPrintName(route.contest.coordPresentation)
    }

    String latPrintName(CoordPresentation coordPresentation)
    {
        return coordPresentation.GetCoordName(this, true)
    }

    String latExportName()
    {
        return latPrintName(route.contest.coordPresentation).replaceAll(',','.')
    }

	BigDecimal lonMath()
	{
		BigDecimal ret = lonGrad + lonMinute/60
		if (lonDirection == CoordPresentation.EAST) {
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
        return coordPresentation.GetCoordName(this, false)
    }

    String lonPrintName()
    {
        return lonPrintName(route.contest.coordPresentation)
    }
    
    String lonPrintName(CoordPresentation coordPresentation)
    {
        return coordPresentation.GetCoordName(this, false)
    }

    String lonExportName()
    {
        return lonPrintName(route.contest.coordPresentation).replaceAll(',','.')
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
	
	String coordMeasureDistanceName(boolean isPrint = false)
	{
		if (isPrint) {
			return "${FcMath.DistanceMeasureStr(coordMeasureDistance)}${getPrintMsg('fc.mm')}"
		}
		return "${FcMath.DistanceMeasureStr(coordMeasureDistance)}${getMsg('fc.mm')}"
	}
	
	String measureDistanceName(boolean isPrint = false)
    {
    	if (measureDistance != null) {
			if (isPrint) {
				return "${FcMath.DistanceMeasureStr(measureDistance)}${getPrintMsg('fc.mm')}"
			}
    		return "${FcMath.DistanceMeasureStr(measureDistance)}${getMsg('fc.mm')}"
    	}
    	return "-"
    }
    
	String measureDistanceDiffName(boolean isPrint = false)
    {
    	if (measureDistance != null) {
			if (isPrint) {
				return "${FcMath.DistanceMeasureStr(coordMeasureDistance-measureDistance)}${getPrintMsg('fc.mm')}"
			}
    		return "${FcMath.DistanceMeasureStr(coordMeasureDistance-measureDistance)}${getMsg('fc.mm')}"
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

    int GetMinAltitudeAboveGround(int routeAltitudeAboveGround)
    {
        if (minAltitudeAboveGround) {
            return altitude + minAltitudeAboveGround
        }
        return altitude + routeAltitudeAboveGround
    }

    int GetMaxAltitudeAboveGround()
    {
        if (maxAltitudeAboveGround) {
            return altitude + maxAltitudeAboveGround
        }
        return 0
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
    
    boolean IsRouteMeasure()
    {
        if (measureTrueTrack || measureDistance) {
            return true
        }
        return false
    }

    String GetTurnpointSign()
    {
        if (!type.IsTurnpointSignCoord()) {
            return "-"
        }
        switch (route.turnpointRoute) {
            case TurnpointRoute.AssignPhoto:
                return assignedSign.title
            case TurnpointRoute.AssignCanvas:
                if (assignedSign.canvas) {
                    return assignedSign.title
                }
                return "${assignedSign.title} !"
            case TurnpointRoute.TrueFalsePhoto:
                return getMsg(correctSign.code)
        }
        return ""
    }
    
    String GetPrintTurnpointSign()
    {
        if (!type.IsTurnpointSignCoord()) {
            return "-"
        }
        switch (route.turnpointRoute) {
            case TurnpointRoute.AssignPhoto:
                return assignedSign.title
            case TurnpointRoute.AssignCanvas:
                if (assignedSign.canvas) {
                    return assignedSign.title
                }
                return "${assignedSign.title} !"
            case TurnpointRoute.TrueFalsePhoto:
                return getPrintMsg(correctSign.code)
        }
        return ""
    }
    
    boolean IsTurnpointSign()
    {
        if (type.IsTurnpointSignCoord()) {
            switch (route.turnpointRoute) {
                case TurnpointRoute.AssignPhoto:
                    return assignedSign != TurnpointSign.None
                case TurnpointRoute.AssignCanvas:
                    if (assignedSign.canvas) {
                        return assignedSign != TurnpointSign.None
                    }
                    return false
                case TurnpointRoute.TrueFalsePhoto:
                    return correctSign != TurnpointCorrect.Unassigned
            }
        }
        return false
    }
    
    boolean IsEnroutePhotoMeasure()
    {
        if (route.enroutePhotoRoute == EnrouteRoute.InputCoordmm) {
            if (measureDistance) {
                return true
            }
        }
        return false
    }
    
    boolean IsEnrouteCanvasMeasure()
    {
        if (route.enrouteCanvasRoute == EnrouteRoute.InputCoordmm) {
            if (measureDistance) {
                return true
            }
        }
        return false
    }
    
    BigDecimal GetMeasureDistance()
    {
        if (measureDistance) {
            return measureDistance
        }
        return coordMeasureDistance
    }
    
    String GetEnrouteOrthogonalDistance()
    {
        String s = "${enrouteOrthogonalDistance.abs()}${getMsg('fc.m')}"
        if (enrouteOrthogonalDistance > 0) {
            s += " ${getMsg('fc.right')}"   
        } else if (enrouteOrthogonalDistance < 0) {
            s += " ${getMsg('fc.left')}"
        }
        return s
    }
    
    String GetPrintEnrouteOrthogonalDistance()
    {
        String s = "${enrouteOrthogonalDistance.abs()}${getPrintMsg('fc.m')}"
        if (enrouteOrthogonalDistance > 0) {
            s += " ${getPrintMsg('fc.right')}"   
        } else if (enrouteOrthogonalDistance < 0) {
            s += " ${getPrintMsg('fc.left')}"
        }
        return s
    }
    
    String GetExportRouteCoord(boolean kmzExport)
    {
        String s = "${titleExport()}"
        if (!kmzExport) {
            s += ", ${latExportName()}, ${lonExportName()}, ${RouteFileTools.ALT} ${altitude}${RouteFileTools.UNIT_ft}"
        }
        if (type.IsRunwayCoord()) {
            s += ", ${RouteFileTools.GATE} ${gateDirection}${RouteFileTools.UNIT_GRAD} ${gatewidth2}${RouteFileTools.UNIT_NM}"
        } else {
            s += ", ${RouteFileTools.GATE} ${gatewidth2}${RouteFileTools.UNIT_NM}"
        }
        if (kmzExport) {
            s += ", ${RouteFileTools.ALT} ${altitude}${RouteFileTools.UNIT_ft}"
        }
        if (minAltitudeAboveGround) {
            s += ", ${RouteFileTools.MINALT} ${minAltitudeAboveGround}${RouteFileTools.UNIT_ft}"
        }
        if (maxAltitudeAboveGround) {
            s += ", ${RouteFileTools.MAXALT} ${maxAltitudeAboveGround}${RouteFileTools.UNIT_ft}"
        }
        if (measureDistance) {
            s += ", ${RouteFileTools.DIST} ${FcMath.DistanceMeasureStr2(measureDistance)}${RouteFileTools.UNIT_mm}"
        }
        if (measureTrueTrack) {
            s += ", ${RouteFileTools.TRACK} ${FcMath.RouteGradStr2(measureTrueTrack)}${RouteFileTools.UNIT_GRAD}"
        }
        if (legDuration) {
            s += ", ${RouteFileTools.DURATION} ${legDuration}${RouteFileTools.UNIT_min}"
        }
        if (noTimeCheck) {
            s += ", ${RouteFileTools.UNIT_TPnotimecheck}"
        }
        if (noGateCheck) {
            s += ", ${RouteFileTools.UNIT_TPnogatecheck}"
        }
        if (noPlanningTest) {
            s += ", ${RouteFileTools.UNIT_TPnoplanningtest}"
        }
        if (endCurved) {
            s += ", ${RouteFileTools.UNIT_TPendcurved}"
        }
        if (circleCenter) {
            s += ", ${RouteFileTools.UNIT_TPcirclecenter}"
        }
        if (semiCircleInvert) {
            s += ", ${RouteFileTools.UNIT_TPsemicircleinvert}"
        }
        String tp_sign = GetExportTurnpointSign2()
        if (tp_sign != RouteFileTools.UNIT_TPnosign) {
            s += ", ${RouteFileTools.SIGN} ${GetExportTurnpointSign2()}"
        }
        return s
    }
    
    String GetExportTurnpointSign()
    {
        String tp_sign = GetExportTurnpointSign2()
        if (tp_sign != RouteFileTools.UNIT_TPnosign) {
            return "${titleExport()}, ${tp_sign}"
        }
        return tp_sign
    }
    
    private String GetExportTurnpointSign2()
    {
        if (type.IsTurnpointSignCoord()) {
            switch (route.turnpointRoute) {
                case TurnpointRoute.AssignPhoto:
                    if (assignedSign == TurnpointSign.None) {
                        return RouteFileTools.UNIT_TPnosign
                    }
                    return assignedSign.title
                case TurnpointRoute.AssignCanvas:
                    if (assignedSign == TurnpointSign.None) {
                        return RouteFileTools.UNIT_TPnosign
                    }
                    if (assignedSign.canvas) {
                        return assignedSign.title
                    }
                    return RouteFileTools.UNIT_TPnosign
                case TurnpointRoute.TrueFalsePhoto:
                    switch (correctSign) {
                        case TurnpointCorrect.Unassigned:
                            return RouteFileTools.UNIT_TPnosign
                        case TurnpointCorrect.True:
                            return RouteFileTools.UNIT_TPcorrect
                        case TurnpointCorrect.False:
                            return RouteFileTools.UNIT_TPincorrect
                    }
                    return RouteFileTools.UNIT_TPnosign
            }
        }
        return RouteFileTools.UNIT_TPnosign
    }
    
    String GetExportEnroute(boolean enroutePhoto)
    {
        if (enroutePhoto) {
            switch (route.enroutePhotoRoute) {
                case EnrouteRoute.InputName:
                    return enroutePhotoName
                case EnrouteRoute.InputCoord:
                    return "${enroutePhotoName}, ${latExportName()}, ${lonExportName()}"
                case EnrouteRoute.InputNMFromTP:
                    return "${enroutePhotoName}, ${titleExport()}, ${FcMath.DistanceStr2(enrouteDistance)}${RouteFileTools.UNIT_NM}"
                case EnrouteRoute.InputmmFromTP:
                    return "${enroutePhotoName}, ${titleExport()}, ${FcMath.DistanceMeasureStr2(GetMeasureDistance())}${RouteFileTools.UNIT_mm}"
                case EnrouteRoute.InputCoordmm:
                    return "${enroutePhotoName}, ${latExportName()}, ${lonExportName()}, ${FcMath.DistanceMeasureStr2(GetMeasureDistance())}${RouteFileTools.UNIT_mm}"
            }
        } else {
            switch (route.enrouteCanvasRoute) {
                case EnrouteRoute.InputName:
                    return enrouteCanvasSign.canvasName
                case EnrouteRoute.InputCoord:
                    return "${enrouteCanvasSign.canvasName}, ${latExportName()}, ${lonExportName()}"
                case EnrouteRoute.InputNMFromTP:
                    return "${enrouteCanvasSign.canvasName}, ${titleExport()}, ${FcMath.DistanceStr2(enrouteDistance)}${RouteFileTools.UNIT_NM}"
                case EnrouteRoute.InputmmFromTP:
                    return "${enrouteCanvasSign.canvasName}, ${titleExport()}, ${FcMath.DistanceMeasureStr2(GetMeasureDistance())}${RouteFileTools.UNIT_mm}"
                case EnrouteRoute.InputCoordmm:
                    return "${enrouteCanvasSign.canvasName}, ${latExportName()}, ${lonExportName()}, ${FcMath.DistanceMeasureStr2(GetMeasureDistance())}${RouteFileTools.UNIT_mm}"
            }
        }
    }

    String GetExportEnrouteKML(boolean enroutePhoto)
    {
        if (enroutePhoto) {
            switch (route.enroutePhotoRoute) {
                case EnrouteRoute.InputName:
                case EnrouteRoute.InputCoord:
                    return enroutePhotoName
                case EnrouteRoute.InputNMFromTP:
                    return "${enroutePhotoName}, ${titleExport()}, ${FcMath.DistanceStr2(enrouteDistance)}${RouteFileTools.UNIT_NM}"
                case EnrouteRoute.InputmmFromTP:
                    return "${enroutePhotoName}, ${titleExport()}, ${FcMath.DistanceMeasureStr2(GetMeasureDistance())}${RouteFileTools.UNIT_mm}"
                case EnrouteRoute.InputCoordmm:
                    return "${enroutePhotoName}, ${RouteFileTools.DIST} ${FcMath.DistanceMeasureStr2(GetMeasureDistance())}${RouteFileTools.UNIT_mm}"
            }
        } else {
            switch (route.enrouteCanvasRoute) {
                case EnrouteRoute.InputName:
                case EnrouteRoute.InputCoord:
                    return enrouteCanvasSign
                case EnrouteRoute.InputNMFromTP:
                    return "${enrouteCanvasSign}, ${titleExport()}, ${FcMath.DistanceStr2(enrouteDistance)}${RouteFileTools.UNIT_NM}"
                case EnrouteRoute.InputmmFromTP:
                    return "${enrouteCanvasSign}, ${titleExport()}, ${FcMath.DistanceMeasureStr2(GetMeasureDistance())}${RouteFileTools.UNIT_mm}"
                case EnrouteRoute.InputCoordmm:
                    return "${enrouteCanvasSign}, ${FcMath.DistanceMeasureStr2(GetMeasureDistance())}${RouteFileTools.UNIT_mm}"
            }
        }
    }

    String GetGeoDataRouteCoord()
    {
        return """"${route.printName()} - ${titleExport()}"|"${titlePrintCode()}"|POINT (${CoordPresentation.DecimalGradStr(lonMath()).replaceAll(',','.')} ${CoordPresentation.DecimalGradStr(latMath()).replaceAll(',','.')})"""
    }
    
    String GetGeoDataEnroute(boolean enroutePhoto)
    {
        if (enroutePhoto) {
            return """"${route.printName()} - ${enroutePhotoName}"|"${enroutePhotoName}"|POINT (${CoordPresentation.DecimalGradStr(lonMath()).replaceAll(',','.')} ${CoordPresentation.DecimalGradStr(latMath()).replaceAll(',','.')})"""
        } else {
            return """"${route.printName()} - ${enrouteCanvasSign.canvasName}"|"${enrouteCanvasSign.canvasName}"|POINT (${CoordPresentation.DecimalGradStr(lonMath()).replaceAll(',','.')} ${CoordPresentation.DecimalGradStr(latMath()).replaceAll(',','.')})"""
        }
    }

    void calculateCoordEnrouteValues(EnrouteRoute enrouteRoute)
    {
        switch (enrouteRoute) {
            case EnrouteRoute.InputCoord:
                measureDistance = null
                calculateCoordEnrouteFromTP()
                enrouteDistance = route.Convert_mm2NM(coordMeasureDistance)
                calculateCoordEnrouteOrthogonalDistance()
                break
            case EnrouteRoute.InputCoordmm:
                calculateCoordEnrouteFromTP()
                enrouteDistance = route.Convert_mm2NM(GetMeasureDistance())
                calculateCoordEnrouteOrthogonalDistance()
                break
            case EnrouteRoute.InputNMFromTP:
                coordMeasureDistance = route.Convert_NM2mm(enrouteDistance)
                measureDistance = null
                calculateCoordEnrouteCoordinate()
                enrouteOrthogonalDistance = 0
                break
            case EnrouteRoute.InputmmFromTP:
                coordMeasureDistance = measureDistance
                enrouteDistance = route.Convert_mm2NM(coordMeasureDistance)
                calculateCoordEnrouteCoordinate()
                enrouteOrthogonalDistance = 0
                break
        }
    }
    
    private void calculateCoordEnrouteCoordinate()
    // FromTP (type & titleNumber), enrouteDistance (NM) -> Koordinate
    {
        Map values = calculate_coord_enroute_coordinate()
        if (values.valid_coord) {
            latDirection = values.lat.direction
            latGrad = values.lat.grad
            latMinute = values.lat.minute
            lonDirection = values.lon.direction
            lonGrad = values.lon.grad
            lonMinute = values.lon.minute
        }
        if (values.valid_ok) {
            enrouteDistanceOk = values.enrouteDistanceOk
        }
    }
    
    private BigDecimal get_cp_distance(BigDecimal fromLegDistance, BigDecimal fromTrueTrack, BigDecimal enrouteDistance, BigDecimal trueTrack)
    {
        BigDecimal course_change = AviationMath.courseChange(fromTrueTrack, trueTrack)
        if (course_change.abs() < Defs.ENROUTE_CURVED_COURSE_DIFF) {
            return enrouteDistance - fromLegDistance
        }
        BigDecimal epsilon = 180 - course_change
        BigDecimal beta = Math.toDegrees(Math.asin(fromLegDistance/enrouteDistance*Math.sin(Math.toRadians(epsilon))))
        BigDecimal alpha = 180 - beta - epsilon
        BigDecimal from_cp_distance = enrouteDistance * Math.sin(Math.toRadians(alpha)) / Math.sin(Math.toRadians(epsilon))
        return from_cp_distance
    }
    
    private Map get_coordinate_from_coord(CoordType coordType, int titleNumber)
    {
        Map ret = [lat:null, lon:null]
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route,[sort:'id'])) {
            if ((coordroute_instance.type == coordType) && (coordroute_instance.titleNumber == titleNumber)) {
                ret.lat = coordroute_instance.latMath()
                ret.lon = coordroute_instance.lonMath()
                break
            }
        }
        return ret
    }
    
    private Map calculate_coord_enroute_coordinate()
    // FromTP (type & titleNumber), enrouteDistance (NM) -> Koordinate
    {
        BigDecimal true_track = null
        BigDecimal from_true_track = null
        BigDecimal leg_distance = null
        BigDecimal leg_direction = null
        BigDecimal from_leg_distance = 0
        boolean enroute_leg_found = false 
        CoordType from_type = type
        int from_titlenumber = titleNumber
        Map from_tp = [:]
        for (RouteLegCoord routeleg_instance in RouteLegCoord.findAllByRoute(route,[sort:'id'])) {
            if (enroute_leg_found) {
                Map sc = get_coordinate_from_coord(routeleg_instance.startTitle.type, routeleg_instance.startTitle.number)
                Map leg = AviationMath.calculateLeg(sc.lat, sc.lon, from_tp.lat, from_tp.lon)
                leg_distance = leg.dis
                leg_direction = leg.dir
                if (routeleg_instance.startTitle.type == CoordType.SECRET) {
                    from_true_track = leg.dir
                    if (leg_distance > enrouteDistance) {
                        break
                    }
                    from_type = routeleg_instance.startTitle.type
                    from_titlenumber = routeleg_instance.startTitle.number
                    true_track = routeleg_instance.coordTrueTrack
                    from_leg_distance = leg_distance
                } else {
                    break
                }
            }
            if ((routeleg_instance.startTitle.type == type) && (routeleg_instance.startTitle.number == titleNumber)) {
                enroute_leg_found = true
                from_type = routeleg_instance.startTitle.type
                from_titlenumber = routeleg_instance.startTitle.number
                true_track = routeleg_instance.coordTrueTrack
                leg_distance = routeleg_instance.coordDistance
                from_tp = get_coordinate_from_coord(from_type, from_titlenumber)
            }
        }
        if (true_track != null) {
            Map from_cp = get_coordinate_from_coord(from_type, from_titlenumber)
            if ((from_cp.lat != null) && (from_cp.lon != null)) {
                if (enrouteDistance != null) {
                    BigDecimal from_cp_distance = get_cp_distance(from_leg_distance, from_true_track, enrouteDistance, true_track)
                    Map enroute_coord = AviationMath.getCoordinate(from_cp.lat, from_cp.lon, true_track, from_cp_distance)
                    Map lat = CoordPresentation.GetDirectionGradDecimalMinute(enroute_coord.lat, true)
                    Map lon = CoordPresentation.GetDirectionGradDecimalMinute(enroute_coord.lon, false)
                    Map values = [valid_coord:true, valid_ok:true, lat:lat, lon:lon, true_track:true_track, from_type:from_type, from_titlenumber:from_titlenumber, from_cp:from_cp]
                    if (enrouteDistance >= leg_distance) {
                        values.enrouteDistanceOk = false
                    } else {
                        values.enrouteDistanceOk = true
                    }
                    return values
                } else {
                    return [valid_coord:false, valid_ok:true, enrouteDistanceOk:false]
                }
            }
        }
        return [valid_coord:false, valid_ok:false]
    }
    
    /* Entferung an der krummen Strecke entlang
    private void calculateCoordEnrouteCoordinate()
    // FromTP (type & titleNumber), enrouteDistance (NM) -> Koordinate
    {
        BigDecimal true_track = null
        BigDecimal leg_distance = null
        BigDecimal secret_legs_distance = 0
        boolean enroute_leg_found = false 
        CoordType from_type = type
        int from_titlenumber = titleNumber
        for (RouteLegCoord routeleg_instance in RouteLegCoord.findAllByRoute(route,[sort:'id'])) {
            if (enroute_leg_found) {
                if (routeleg_instance.startTitle.type == CoordType.SECRET) {
                    if (enrouteDistance < secret_legs_distance + leg_distance) {
                        break
                    }
                    secret_legs_distance += leg_distance
                    from_type = routeleg_instance.startTitle.type
                    from_titlenumber = routeleg_instance.startTitle.number
                    true_track = routeleg_instance.coordTrueTrack
                    leg_distance = routeleg_instance.coordDistance
                } else {
                    break
                }
            }
            if ((routeleg_instance.startTitle.type == type) && (routeleg_instance.startTitle.number == titleNumber)) {
                enroute_leg_found = true
                from_type = routeleg_instance.startTitle.type
                from_titlenumber = routeleg_instance.startTitle.number
                true_track = routeleg_instance.coordTrueTrack
                leg_distance = routeleg_instance.coordDistance
            }
        }
        if (true_track != null) {
            BigDecimal fromtp_lat = null
            BigDecimal fromtp_lon = null 
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route,[sort:'id'])) {
                if ((coordroute_instance.type == from_type) && (coordroute_instance.titleNumber == from_titlenumber)) {
                    fromtp_lat = coordroute_instance.latMath()
                    fromtp_lon = coordroute_instance.lonMath()
                    break
                }
            }
            if ((fromtp_lat != null) && (fromtp_lon != null)) {
                if (enrouteDistance != null) {
                    Map enroute_coord = AviationMath.getCoordinate(fromtp_lat, fromtp_lon, true_track, enrouteDistance - secret_legs_distance)
                    Map lat = CoordPresentation.GetDirectionGradDecimalMinute(enroute_coord.lat, true)
                    Map lon = CoordPresentation.GetDirectionGradDecimalMinute(enroute_coord.lon, false)
                    latDirection = lat.direction
                    latGrad = lat.grad
                    latMinute = lat.minute
                    lonDirection = lon.direction
                    lonGrad = lon.grad
                    lonMinute = lon.minute
                    if (enrouteDistance >= leg_distance + secret_legs_distance) {
                        enrouteDistanceOk = false
                    } else {
                        enrouteDistanceOk = true
                    }
                } else {
                    enrouteDistanceOk = false
                }
            }
        }
    }
    */
    
    private void calculateCoordEnrouteFromTP()
    // Koordinate -> FromTP (type & titleNumber), coordMeasureDistance (mm)
    {
        CoordType from_type = CoordType.UNKNOWN
        int from_titlenumber = 1
        BigDecimal from_distance = null
        CoordRoute from_coordroute_instance = null
        enrouteDistanceOk = false
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route,[sort:'id'])) {
            if (coordroute_instance.type.IsEnrouteCalculateSignCoord()) {
                if (coordroute_instance.type.IsEnrouteSignCoord()) {
                    from_type = coordroute_instance.type
                    from_titlenumber = coordroute_instance.titleNumber
                    from_distance = 0
                    from_coordroute_instance = coordroute_instance
                }
                Map enroute = AviationMath.calculateLeg(latMath(),lonMath(),coordroute_instance.latMath(),coordroute_instance.lonMath())
                BigDecimal leg_distance = null
                BigDecimal true_track = null
                for (RouteLegCoord routeleg_instance in RouteLegCoord.findAllByRoute(route,[sort:'id'])) {
                    if ((routeleg_instance.startTitle.type == coordroute_instance.type) && (routeleg_instance.startTitle.number == coordroute_instance.titleNumber)) {
                        leg_distance = routeleg_instance.coordDistance
                        true_track = routeleg_instance.coordTrueTrack
                        break
                    }
                }
                if (true_track != null) {
                    boolean fromtp_found = false
                    if (enroute.dis == 0) {
                        fromtp_found = true
                    } else if (enroute.dis < leg_distance) {
                        BigDecimal track_diff = AviationMath.courseChange(true_track,enroute.dir).abs()
                        if (enroute.dis > Defs.ENROUTE_MAX_DISTANCE) {
                            BigDecimal max_angle = Math.toDegrees(Math.asin(Defs.ENROUTE_MAX_DISTANCE / enroute.dis))
                            if (track_diff < max_angle) {
                                fromtp_found = true
                            }
                        } else {
                            fromtp_found = true
                        }
                    }
                    if (fromtp_found) {
                        type = from_type
                        titleNumber = from_titlenumber
                        // direkte Entfernung bei krummen Strecken
                        enroute = AviationMath.calculateLeg(latMath(),lonMath(),from_coordroute_instance.latMath(),from_coordroute_instance.lonMath())
                        coordMeasureDistance = route.Convert_NM2mm(enroute.dis)
                        // Entferung an der krummen Strecke entlang
                        // coordMeasureDistance = route.Convert_NM2mm(from_distance + enroute.dis)
                        enrouteDistanceOk = true
                        return
                    }
                    from_distance += leg_distance
                }
            }
        }
        type = CoordType.UNKNOWN
        titleNumber = 1
        coordMeasureDistance = 0
    }
    
    private void calculateCoordEnrouteOrthogonalDistance()
    // Koordinate, FromTP (type & titleNumber), enrouteDistance (NM) -> enrouteOrthogonalDistance (m)
    {
        enrouteOrthogonalDistance = 0

        Map values = calculate_coord_enroute_coordinate()
        if (values.valid_coord) {
            BigDecimal orthogonal_track = AviationMath.getOrthogonalTrackRight(values.true_track)
            Map line_coord = AviationMath.getCoordinate(values.from_cp.lat, values.from_cp.lon, values.true_track, get_from_dis(values))
            Map orthogonal = AviationMath.calculateLeg(latMath(), lonMath(), line_coord.lat, line_coord.lon)
            if (AviationMath.courseChange(orthogonal_track,orthogonal.dir).abs() < Defs.ENROUTE_MAX_COURSE_DIFF) {
                enrouteOrthogonalDistance = Route.Convert_NM2m(orthogonal.dis) // right
            } else {
                enrouteOrthogonalDistance = -Route.Convert_NM2m(orthogonal.dis) // left
            }
        }
    }
    
    private BigDecimal get_from_dis(Map values)
    {
        Map enroute_from_cp = AviationMath.calculateLeg(values.from_cp.lat, values.from_cp.lon, latMath(), lonMath())
        return enroute_from_cp.dis * Math.sin(Math.toRadians(90 - AviationMath.courseChange(values.true_track, AviationMath.getDiametricalTrack(enroute_from_cp.dir)).abs()))
    }
}