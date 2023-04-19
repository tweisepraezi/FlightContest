import java.util.List;

class Route 
{
    def grailsApplication
    
	String title
    int idTitle
    String mark = ""
    Boolean showAflosMark = false                                                // DB-2.12
    Boolean useProcedureTurns = false                                            // DB-2.18
    String liveTrackingScorecard = ""                                            // DB-2.18
    
    TurnpointRoute turnpointRoute = TurnpointRoute.Unassigned                    // DB-2.13
    Boolean turnpointMapMeasurement = false                                      // DB-2.13
    ObservationPrintStyle turnpointPrintStyle = ObservationPrintStyle.Portrait2x4  // DB-2.28
    Boolean turnpointPrintPositionMaker = false                                  // DB-2.30
    
    EnrouteRoute enroutePhotoRoute = EnrouteRoute.Unassigned                     // DB-2.13
    EnrouteRoute enrouteCanvasRoute = EnrouteRoute.Unassigned                    // DB-2.13
    EnrouteMeasurement enroutePhotoMeasurement = EnrouteMeasurement.Unassigned   // DB-2.13
    EnrouteMeasurement enrouteCanvasMeasurement = EnrouteMeasurement.Unassigned  // DB-2.13
    ObservationPrintStyle enroutePhotoPrintStyle = ObservationPrintStyle.Portrait2x4  // DB-2.28
    Boolean enroutePhotoPrintPositionMaker = false                               // DB-2.30
    
    Boolean showCurvedPoints = false                                             // DB-2.20
	Integer mapScale = 200000                                                    // DB-2.21
	Integer altitudeAboveGround = 300                                            // DB-2.31, min. Altitude (Höhe) über Grund in ft
    Boolean exportSemicircleGates = false                                        // DB-2.26
    
    Boolean showCoords = true                                                    // DB-2.30
    Boolean showCoordObservations = false                                        // DB-2.30
    Boolean showResultLegs = false                                               // DB-2.30
    Boolean showTestLegs = false                                                 // DB-2.30
    Boolean showEnroutePhotos = false                                            // DB-2.30
    Boolean showEnrouteCanvas = false                                            // DB-2.30
    
    static int mmPerNM = 1852000
    static int mmPerkm = 1000000
    
    // transient values 
    static transients = ['contestMapOutput','contestMapPrint','contestMapDevStyle']
            
    String contestMapOutput = Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP
    String contestMapPrint = Defs.CONTESTMAPPRINT_PDFMAP
    boolean contestMapDevStyle = false
    
    String contestMapAirfields = Defs.CONTESTMAPAIRFIELDS_OSM_ICAO               // DB-2.21
    Boolean contestMapCircle = true                                              // DB-2.21
    Boolean contestMapProcedureTurn = true                                       // DB-2.21
    Boolean contestMapLeg = true                                                 // DB-2.21
    Boolean contestMapCurvedLeg = true                                           // DB-2.21
    String contestMapCurvedLegPoints = Defs.CONTESTMAPPOINTS_INIT                // DB-2.21, list of turn points after curved legs
    Boolean contestMapTpName = true                                              // DB-2.21
    Boolean contestMapSecretGates = false                                        // DB-2.21
    Boolean contestMapEnroutePhotos = false                                      // DB-2.21
    Boolean contestMapEnrouteCanvas = false                                      // DB-2.21
    Boolean contestMapGraticule = true                                           // DB-2.21
    Integer contestMapContourLines = Defs.CONTESTMAPCONTOURLINES_100M            // DB-2.21
    Boolean contestMapMunicipalityNames = true                                   // DB-2.21
    Boolean contestMapChurches = true                                            // DB-2.21
    Boolean contestMapCastles = true                                             // DB-2.21
    Boolean contestMapChateaus = true                                            // DB-2.21
    Boolean contestMapPowerlines = false                                         // DB-2.21
    Boolean contestMapWindpowerstations = true                                   // DB-2.21
    Boolean contestMapSmallRoads = false                                         // DB-2.21
    Boolean contestMapPeaks = true                                               // DB-2.21
    Boolean contestMapDropShadow = false                                         // DB-2.21
    Boolean contestMapAdditionals = true                                         // DB-2.21
    Boolean contestMapSpecials = false                                           // DB-2.21
    Boolean contestMapAirspaces = false                                          // DB-2.21
    String contestMapAirspacesLayer = ""                                         // DB-2.21, UNUSED, since DB-2.32
    String contestMapAirspacesLayer2 = ""                                        // DB-2.32
    Boolean contestMapShowFirstOptions = true                                    // DB-2.29
    String contestMapFirstTitle = ""                                             // DB-2.29
    VerticalPos contestMapCenterVerticalPos = VerticalPos.Center                 // DB-2.21
    HorizontalPos contestMapCenterHorizontalPos = HorizontalPos.Center           // DB-2.21
    String contestMapCenterPoints = Defs.CONTESTMAPPOINTS_INIT                   // DB-2.21, list of turn points arranged in map center
    String contestMapPrintPoints = Defs.CONTESTMAPPOINTS_INIT                    // DB-2.21, list of turn points for printing
    Boolean contestMapPrintLandscape = true                                      // DB-2.21
    String contestMapPrintSize = Defs.CONTESTMAPPRINTSIZE_A3                     // DB-2.21
    BigDecimal contestMapCenterMoveX = 0.0                                       // DB-2.32, NM
    BigDecimal contestMapCenterMoveY = 0.0                                       // DB-2.32, NM
    Boolean contestMapReserve1                                                   // DB-2.21
    Boolean contestMapReserve2                                                   // DB-2.21
    Boolean contestMapReserve3                                                   // DB-2.21
    String contestMapReserve4                                                    // DB-2.21
    String contestMapReserve5                                                    // DB-2.21
    Integer contestMapEdition = 0                                                // DB-2.22
    Boolean contestMapShowSecondOptions = false                                  // DB-2.22
    String contestMapSecondTitle = ""                                            // DB-2.29
    VerticalPos contestMapCenterVerticalPos2 = VerticalPos.Center                // DB-2.22
    HorizontalPos contestMapCenterHorizontalPos2 = HorizontalPos.Center          // DB-2.22
    String contestMapCenterPoints2 = Defs.CONTESTMAPPOINTS_INIT                  // DB-2.22, list of turn points arranged in map center
    String contestMapPrintPoints2 = Defs.CONTESTMAPPOINTS_INIT                   // DB-2.22, list of turn points for printing
    Boolean contestMapPrintLandscape2 = true                                     // DB-2.22
    String contestMapPrintSize2 = Defs.CONTESTMAPPRINTSIZE_A3                    // DB-2.22
    BigDecimal contestMapCenterMoveX2 = 0.0                                      // DB-2.32, NM
    BigDecimal contestMapCenterMoveY2 = 0.0                                      // DB-2.32, NM
    Boolean contestMapShowThirdOptions = false                                   // DB-2.29
    String contestMapThirdTitle = ""                                             // DB-2.29
    VerticalPos contestMapCenterVerticalPos3 = VerticalPos.Center                // DB-2.29
    HorizontalPos contestMapCenterHorizontalPos3 = HorizontalPos.Center          // DB-2.29
    String contestMapCenterPoints3 = Defs.CONTESTMAPPOINTS_INIT                  // DB-2.29, list of turn points arranged in map center
    String contestMapPrintPoints3 = Defs.CONTESTMAPPOINTS_INIT                   // DB-2.29, list of turn points for printing
    Boolean contestMapPrintLandscape3 = true                                     // DB-2.29
    String contestMapPrintSize3 = Defs.CONTESTMAPPRINTSIZE_A3                    // DB-2.29
    BigDecimal contestMapCenterMoveX3 = 0.0                                      // DB-2.32, NM
    BigDecimal contestMapCenterMoveY3 = 0.0                                      // DB-2.32, NM
    Boolean contestMapShowForthOptions = false                                   // DB-2.29
    String contestMapForthTitle = ""                                             // DB-2.29
    VerticalPos contestMapCenterVerticalPos4 = VerticalPos.Center                // DB-2.29
    HorizontalPos contestMapCenterHorizontalPos4 = HorizontalPos.Center          // DB-2.29
    String contestMapCenterPoints4 = Defs.CONTESTMAPPOINTS_INIT                  // DB-2.29, list of turn points arranged in map center
    String contestMapPrintPoints4 = Defs.CONTESTMAPPOINTS_INIT                   // DB-2.29, list of turn points for printing
    Boolean contestMapPrintLandscape4 = true                                     // DB-2.29
    String contestMapPrintSize4 = Defs.CONTESTMAPPRINTSIZE_A3                    // DB-2.29
    BigDecimal contestMapCenterMoveX4 = 0.0                                      // DB-2.32, NM
    BigDecimal contestMapCenterMoveY4 = 0.0                                      // DB-2.32, NM
	
    
	static belongsTo = [contest:Contest]

	static hasMany = [coords:CoordRoute, routelegs:RouteLegCoord, testlegs:RouteLegTest, enroutephotos:CoordEnroutePhoto, enroutecanvas:CoordEnrouteCanvas, uploadjobroutemaps:UploadJobRouteMap]

	static hasOne = [uploadjobroute:UploadJobRoute]                              // DB-2.21
	
	static constraints = {
		contest(nullable:false)
        
        // DB-2.12 compatibility
        showAflosMark(nullable:true)
        
        // DB-2.13 compatibility
        turnpointRoute(nullable:true)
        turnpointMapMeasurement(nullable:true)
        enroutePhotoRoute(nullable:true)
        enrouteCanvasRoute(nullable:true)
        enroutePhotoMeasurement(nullable:true)
        enrouteCanvasMeasurement(nullable:true)
        
        // DB-2.18 compatibility
        useProcedureTurns(nullable:true)
        liveTrackingScorecard(nullable:true)
        
        // DB-2.20 compatibility
        showCurvedPoints(nullable:true)
        
        // DB-2.21 compatibility
        mapScale(nullable:true, range:1..1000000000)
        contestMapAirfields(nullable:true)
        contestMapCircle(nullable:true)
        contestMapProcedureTurn(nullable:true)
        contestMapLeg(nullable:true)
        contestMapCurvedLeg(nullable:true)
        contestMapCurvedLegPoints(nullable:true)
        contestMapTpName(nullable:true)
        contestMapSecretGates(nullable:true)
        contestMapEnroutePhotos(nullable:true)
        contestMapEnrouteCanvas(nullable:true)
        contestMapGraticule(nullable:true)
        contestMapContourLines(nullable:true)
        contestMapMunicipalityNames(nullable:true)
        contestMapChurches(nullable:true)
        contestMapCastles(nullable:true)
        contestMapChateaus(nullable:true)
        contestMapPowerlines(nullable:true)
        contestMapWindpowerstations(nullable:true)
        contestMapSmallRoads(nullable:true)
        contestMapPeaks(nullable:true)
        contestMapDropShadow(nullable:true)
        contestMapAdditionals(nullable:true)
        contestMapSpecials(nullable:true)
        contestMapAirspaces(nullable:true)
        contestMapAirspacesLayer(nullable:true)
        contestMapCenterVerticalPos(nullable:true)
        contestMapCenterHorizontalPos(nullable:true)
        contestMapCenterPoints(nullable:true)
        contestMapPrintPoints(nullable:true)
        contestMapPrintLandscape(nullable:true)
        contestMapPrintSize(nullable:true)
        contestMapReserve1(nullable:true)
        contestMapReserve2(nullable:true)
        contestMapReserve3(nullable:true)
        contestMapReserve4(nullable:true)
        contestMapReserve5(nullable:true)
		uploadjobroute(nullable:true)
        
        // DB-2.22 compatibility
        contestMapEdition(nullable:true)
        contestMapShowSecondOptions(nullable:true)
        contestMapCenterVerticalPos2(nullable:true)
        contestMapCenterHorizontalPos2(nullable:true)
        contestMapCenterPoints2(nullable:true)
        contestMapPrintPoints2(nullable:true)
        contestMapPrintLandscape2(nullable:true)
        contestMapPrintSize2(nullable:true)
        
        // DB-2.26 compatibility
        exportSemicircleGates(nullable:true)
        
        // DB-2.28 compatibility
        enroutePhotoPrintStyle(nullable:true)
        turnpointPrintStyle(nullable:true)
		
		// DB-2.29 compatibility
		contestMapShowFirstOptions(nullable:true)
        contestMapFirstTitle(nullable:true)
        contestMapSecondTitle(nullable:true)
        contestMapShowThirdOptions(nullable:true)
        contestMapThirdTitle(nullable:true)
        contestMapCenterVerticalPos3(nullable:true)
        contestMapCenterHorizontalPos3(nullable:true)
        contestMapCenterPoints3(nullable:true)
        contestMapPrintPoints3(nullable:true)
        contestMapPrintLandscape3(nullable:true)
        contestMapPrintSize3(nullable:true)
        contestMapShowForthOptions(nullable:true)
        contestMapForthTitle(nullable:true)
        contestMapCenterVerticalPos4(nullable:true)
        contestMapCenterHorizontalPos4(nullable:true)
        contestMapCenterPoints4(nullable:true)
        contestMapPrintPoints4(nullable:true)
        contestMapPrintLandscape4(nullable:true)
        contestMapPrintSize4(nullable:true)
        
        // DB-2.30 compatibility
        turnpointPrintPositionMaker(nullable:true)
        enroutePhotoPrintPositionMaker(nullable:true)
        showCoords(nullable:true)
        showCoordObservations(nullable:true)
        showResultLegs(nullable:true)
        showTestLegs(nullable:true)
        showEnroutePhotos(nullable:true)
        showEnrouteCanvas(nullable:true)
		
		// DB-2.31 compatibility
		altitudeAboveGround(nullable:true)
        
		// DB-2.32 compatibility
        contestMapAirspacesLayer2(nullable:true)
        contestMapCenterMoveX(nullable:true)
        contestMapCenterMoveY(nullable:true)
        contestMapCenterMoveX2(nullable:true)
        contestMapCenterMoveY2(nullable:true)
        contestMapCenterMoveX3(nullable:true)
        contestMapCenterMoveY3(nullable:true)
        contestMapCenterMoveX4(nullable:true)
        contestMapCenterMoveY4(nullable:true)
	}

	static mapping = {
		coords sort:"id"
		routelegs sort:"id"
		testlegs sort:"id"
        enroutephotos sort:"id"
        enroutecanvas sort:"id"
        contestMapAirspacesLayer2 sqlType: "nvarchar(max)"
	}
	
	void CopyValues(Route routeInstance)
	{
		title = routeInstance.title
		idTitle = routeInstance.idTitle
		mark = routeInstance.mark
        useProcedureTurns = routeInstance.useProcedureTurns
        turnpointRoute = routeInstance.turnpointRoute
        turnpointMapMeasurement = routeInstance.turnpointMapMeasurement
        turnpointPrintStyle = routeInstance.turnpointPrintStyle
        turnpointPrintPositionMaker = routeInstance.turnpointPrintPositionMaker
        enroutePhotoRoute = routeInstance.enroutePhotoRoute
        enroutePhotoMeasurement = routeInstance.enroutePhotoMeasurement
        enroutePhotoPrintStyle = routeInstance.enroutePhotoPrintStyle
        enroutePhotoPrintPositionMaker = routeInstance.enroutePhotoPrintPositionMaker
        enrouteCanvasRoute = routeInstance.enrouteCanvasRoute
        enrouteCanvasMeasurement = routeInstance.enrouteCanvasMeasurement
        liveTrackingScorecard = routeInstance.liveTrackingScorecard
        mapScale = routeInstance.mapScale
		altitudeAboveGround = routeInstance.altitudeAboveGround
        showCoords = routeInstance.showCoords
        showCoordObservations = routeInstance.showCoordObservations
        showResultLegs = routeInstance.showResultLegs
        showTestLegs = routeInstance.showTestLegs
        showEnroutePhotos = routeInstance.showEnroutePhotos
        showEnrouteCanvas = routeInstance.showEnrouteCanvas

        contestMapAirfields = routeInstance.contestMapAirfields
        contestMapCircle = routeInstance.contestMapCircle
        contestMapProcedureTurn = routeInstance.contestMapProcedureTurn
        contestMapLeg = routeInstance.contestMapLeg
        contestMapCurvedLeg = routeInstance.contestMapCurvedLeg
        contestMapCurvedLegPoints = routeInstance.contestMapCurvedLegPoints
        contestMapTpName = routeInstance.contestMapTpName
        contestMapSecretGates = routeInstance.contestMapSecretGates
        contestMapEnroutePhotos = routeInstance.contestMapEnroutePhotos
        contestMapEnrouteCanvas = routeInstance.contestMapEnrouteCanvas
        contestMapGraticule = routeInstance.contestMapGraticule
        contestMapContourLines = routeInstance.contestMapContourLines
        contestMapMunicipalityNames = routeInstance.contestMapMunicipalityNames
        contestMapChurches = routeInstance.contestMapChurches
        contestMapCastles = routeInstance.contestMapCastles
        contestMapChateaus = routeInstance.contestMapChateaus
        contestMapPowerlines = routeInstance.contestMapPowerlines
        contestMapWindpowerstations = routeInstance.contestMapWindpowerstations
        contestMapSmallRoads = routeInstance.contestMapSmallRoads
        contestMapPeaks = routeInstance.contestMapPeaks
        contestMapDropShadow = routeInstance.contestMapDropShadow
        contestMapAdditionals = routeInstance.contestMapAdditionals
        contestMapSpecials = routeInstance.contestMapSpecials
        contestMapAirspaces = routeInstance.contestMapAirspaces
        contestMapAirspacesLayer2 = routeInstance.contestMapAirspacesLayer2
		contestMapShowFirstOptions = routeInstance.contestMapShowFirstOptions
        contestMapFirstTitle = routeInstance.contestMapFirstTitle
        contestMapCenterVerticalPos = routeInstance.contestMapCenterVerticalPos
        contestMapCenterHorizontalPos = routeInstance.contestMapCenterHorizontalPos
        contestMapCenterPoints = routeInstance.contestMapCenterPoints
        contestMapPrintPoints = routeInstance.contestMapPrintPoints
        contestMapPrintLandscape = routeInstance.contestMapPrintLandscape
        contestMapPrintSize = routeInstance.contestMapPrintSize
        contestMapCenterMoveX = routeInstance.contestMapCenterMoveX
        contestMapCenterMoveY = routeInstance.contestMapCenterMoveY
		contestMapShowSecondOptions = routeInstance.contestMapShowSecondOptions
        contestMapSecondTitle = routeInstance.contestMapSecondTitle
        contestMapCenterVerticalPos2 = routeInstance.contestMapCenterVerticalPos2
        contestMapCenterHorizontalPos2 = routeInstance.contestMapCenterHorizontalPos2
        contestMapCenterPoints2 = routeInstance.contestMapCenterPoints2
        contestMapPrintPoints2 = routeInstance.contestMapPrintPoints2
        contestMapPrintLandscape2 = routeInstance.contestMapPrintLandscape2
        contestMapPrintSize2 = routeInstance.contestMapPrintSize2
        contestMapCenterMoveX2 = routeInstance.contestMapCenterMoveX2
        contestMapCenterMoveY2 = routeInstance.contestMapCenterMoveY2
		contestMapShowThirdOptions = routeInstance.contestMapShowThirdOptions
        contestMapThirdTitle = routeInstance.contestMapThirdTitle
        contestMapCenterVerticalPos3 = routeInstance.contestMapCenterVerticalPos3
        contestMapCenterHorizontalPos3 = routeInstance.contestMapCenterHorizontalPos3
        contestMapCenterPoints3 = routeInstance.contestMapCenterPoints3
        contestMapPrintPoints3 = routeInstance.contestMapPrintPoints3
        contestMapPrintLandscape3 = routeInstance.contestMapPrintLandscape3
        contestMapPrintSize3 = routeInstance.contestMapPrintSize3
        contestMapCenterMoveX3 = routeInstance.contestMapCenterMoveX3
        contestMapCenterMoveY3 = routeInstance.contestMapCenterMoveY3
		contestMapShowForthOptions = routeInstance.contestMapShowForthOptions
        contestMapForthTitle = routeInstance.contestMapForthTitle
        contestMapCenterVerticalPos4 = routeInstance.contestMapCenterVerticalPos4
        contestMapCenterHorizontalPos4 = routeInstance.contestMapCenterHorizontalPos4
        contestMapCenterPoints4 = routeInstance.contestMapCenterPoints4
        contestMapPrintPoints4 = routeInstance.contestMapPrintPoints4
        contestMapPrintLandscape4 = routeInstance.contestMapPrintLandscape4
        contestMapPrintSize4 = routeInstance.contestMapPrintSize4
        contestMapCenterMoveX4 = routeInstance.contestMapCenterMoveX4
        contestMapCenterMoveY4 = routeInstance.contestMapCenterMoveY4
        
		if (!this.save()) {
			throw new Exception("Route.CopyValues could not save")
		}
		
		// coords:CoordRoute
		CoordRoute.findAllByRoute(routeInstance,[sort:"id"]).each { CoordRoute coordroute_instance ->
			CoordRoute new_coordroute_instance = new CoordRoute()
			new_coordroute_instance.route = this
			new_coordroute_instance.CopyValues(coordroute_instance)
			new_coordroute_instance.save()
		}
		
		// routelegs:RouteLegCoord
		RouteLegCoord.findAllByRoute(routeInstance,[sort:"id"]).each { RouteLegCoord routelegcoord_instance ->
			RouteLegCoord new_routelegcoord_instance = new RouteLegCoord()
			new_routelegcoord_instance.route = this
			new_routelegcoord_instance.CopyValues(routelegcoord_instance)
			new_routelegcoord_instance.save()
		}

		// testlegs:RouteLegTest
		RouteLegTest.findAllByRoute(routeInstance,[sort:"id"]).each { RouteLegTest routelegtest_instance ->
			RouteLegTest new_routelegtest_instance = new RouteLegTest()
			new_routelegtest_instance.route = this
			new_routelegtest_instance.CopyValues(routelegtest_instance)
			new_routelegtest_instance.save()
		}
        
        // enroutephotos:CoordEnroutePhoto
        CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"id"]).each { CoordEnroutePhoto coordenroute_instance ->
            CoordEnroutePhoto new_coordenroute_instance = new CoordEnroutePhoto()
            new_coordenroute_instance.route = this
            new_coordenroute_instance.CopyValues(coordenroute_instance)
            new_coordenroute_instance.save()
        }
        
        // enroutecanvas:CoordEnrouteCanvas
        CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"id"]).each { CoordEnrouteCanvas coordenroute_instance ->
            CoordEnrouteCanvas new_coordenroute_instance = new CoordEnrouteCanvas()
            new_coordenroute_instance.route = this
            new_coordenroute_instance.CopyValues(coordenroute_instance)
            new_coordenroute_instance.save()
        }
	}
	
	BigDecimal CenterLatitudeMath()
	{
		try {
			BigDecimal min_latitude = null
			BigDecimal max_latitude = null
			CoordRoute.findAllByRoute(this,[sort:"id"]).each { CoordRoute coordroute_instance ->
				BigDecimal latitude = coordroute_instance.latMath()
				if (min_latitude == null) {
					min_latitude = latitude
				} else if (latitude < min_latitude) {
					min_latitude = latitude
				}
				if (max_latitude == null) {
					max_latitude = latitude
				} else if (latitude > max_latitude) {
					max_latitude = latitude
				}
			}
			return (max_latitude + min_latitude)/2
		} catch (Exception e) {
			return 0
		}
	}
	
	BigDecimal CenterLongitudeMath()
	{
		try {
			BigDecimal min_longitude = null
			BigDecimal max_longitude = null
			CoordRoute.findAllByRoute(this,[sort:"id"]).each { CoordRoute coordroute_instance ->
				BigDecimal longitude = coordroute_instance.lonMath()
				if (min_longitude == null) {
					min_longitude = longitude
				} else if (longitude < min_longitude) {
					min_longitude = longitude
				}
				if (max_longitude == null) {
					max_longitude = longitude
				} else if (longitude > max_longitude) {
					max_longitude = longitude
				}
			}
			return (max_longitude + min_longitude)/2
		} catch (Exception e) {
			return 0
		}
	}
	
    String idName()
    {
		return "${getMsg('fc.route')}-${idTitle}"
    }
    
    String idNamePrintable()
    {
        return "${getPrintMsg('fc.route')}-${idTitle}"
    }

	String name()
	{
		if(title) {
			return title
		} else {
            return idName()
		}
	}
	
    String printName()
    {
        if(title) {
            return title
        } else {
            return idNamePrintable()
        }
    }
    
    String GetName(boolean isPrint)
    {
        if (isPrint) {
            return name()
        } else {
            return printName()
        }
    }

    String GetOSMRouteName1()
    {
        if (contestMapFirstTitle) {
            return "${name()} - ${contestMapFirstTitle}"
        }
        return name()
    }
    
    String GetOSMRouteName2()
    {
        if (contestMapSecondTitle) {
            return "${name()} - ${contestMapSecondTitle}"
        }
        return name()
    }
    
    String GetOSMRouteName3()
    {
        if (contestMapThirdTitle) {
            return "${name()} - ${contestMapThirdTitle}"
        }
        return name()
    }
    
    String GetOSMRouteName4()
    {
        if (contestMapForthTitle) {
            return "${name()} - ${contestMapForthTitle}"
        }
        return name()
    }
    
	boolean Used()
	{
		if (PlanningTestTask.findByRoute(this) || FlightTest.findByRoute(this)) {
			return true
		}
		return false
	}
    
    String GetFileName()
    {
        return "route-${idTitle}"
    }
    
    String GetEMailTitle()
    {
        return "${contest.title}: ${printName()}" 
    }
    
    boolean IsEMailPossible()
    {
        if (BootStrap.global.IsEMailPossible() && BootStrap.global.IsFTPPossible()) {
            return true
        }
        return false
    }
    
    String EMailAddress()
    {
        return grailsApplication.config.flightcontest.mail.cc
    }
    
    boolean IsSendEMailPossible()
    {
        return IsEMailPossible() && !GetUploadJobStatus().InWork()
    }
    
	UploadJobStatus GetUploadJobStatus()
	{
		UploadJobRoute uploadjob_route = UploadJobRoute.findByRoute(this)
		if (uploadjob_route) {
			return uploadjob_route.uploadJobStatus
		}
		return UploadJobStatus.None
	}
	
	String GetUploadLink()
	{
		UploadJobRoute uploadjob_route = UploadJobRoute.findByRoute(this)
		if (uploadjob_route) {
			if (uploadjob_route.uploadJobStatus == UploadJobStatus.Done) {
				return uploadjob_route.uploadJobLink
			}
		}
		return ""
	}

    boolean IsMapSendEMailPossible()
    {
        return IsEMailPossible() && !GetMapUploadJobStatus().InWork()
    }
    
	UploadJobStatus GetMapUploadJobStatus()
	{
		UploadJobRouteMap mapuploadjob_route = UploadJobRouteMap.findByRouteAndUploadJobMapEdition(this, contestMapEdition)
		if (mapuploadjob_route) {
			return mapuploadjob_route.uploadJobStatus
		}
		return UploadJobStatus.None
	}
	
	String GetMapUploadLink()
	{
		UploadJobRouteMap mapuploadjob_route = UploadJobRouteMap.findByRouteAndUploadJobMapEdition(this, contestMapEdition)
		if (mapuploadjob_route) {
			if (mapuploadjob_route.uploadJobStatus == UploadJobStatus.Done) {
				return mapuploadjob_route.uploadJobLink
			}
		}
		return ""
	}
    
    List GetMapUploadLinks()
    {
        List upload_links = []
		for (UploadJobRouteMap mapuploadjob_route in UploadJobRouteMap.findAllByRoute(this)) {
			if (mapuploadjob_route.uploadJobStatus == UploadJobStatus.Done) {
                upload_links += [edition:mapuploadjob_route.uploadJobMapEdition, link:mapuploadjob_route.uploadJobLink,
                                 noroute:mapuploadjob_route.uploadJobNoRoute, allroutedetails:mapuploadjob_route.uploadJobAllRouteDetails,
                                 optionnumber:mapuploadjob_route.uploadJobOptionNumber, optiontitle:mapuploadjob_route.uploadJobOptionTitle, title:mapuploadjob_route.GetTitle()
                                ]
            }
        }
        return upload_links
    }

    boolean IsIntermediateRunway()
    {
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,,[sort:"id"])) {
            switch (coordroute_instance.type) {
                case CoordType.iLDG:
                case CoordType.iTO:
                    return true
            }
        }
        return false
    }
    
    boolean IsIntermediateSP()
    {
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,,[sort:"id"])) {
            switch (coordroute_instance.type) {
                case CoordType.iSP:
                    return true
            }
        }
        return false
    }
    
    boolean IsRouteComplete()
    {
        boolean sp_found = false
        boolean fp_found = false
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:"id"])) {
            switch (coordroute_instance.type) {
                case CoordType.SP:
                    sp_found = true
                    break
                case CoordType.FP:
                    fp_found = true
                    break
            }
        }
        return sp_found && fp_found
    }
    
    boolean IsRouteEmpty()
    {
        if (!CoordRoute.findByRoute(this)) {
            return true
        }
        return false
    }

    Map GetRunways()
    {
        Map ret = [onlyRunways:true, runwayList:[], runwayPoints:""]
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:"id"])) {
            if (coordroute_instance.type.IsRunwayCoord()) {
                ret.runwayList += coordroute_instance.type
                if (ret.runwayPoints) {
                    ret.runwayPoints += ",${coordroute_instance.title()}"
                } else {
                    ret.runwayPoints = coordroute_instance.title()
                }
            } else {
                ret.onlyRunways = false
            }
        }
        return ret
    }
    
    boolean IsTurnpointSign()
    {
        turnpointRoute.IsTurnpointSign()
    }
    
    boolean IsTurnpointPhoto()
    {
        turnpointRoute.IsTurnpointPhoto()
    }
    
    private Map TurnpointSignStatus()
    {
        int missingsign_num = 0
        int doublesign_num = 0
        int invalidsign_num = 0
        int unassigned_num = 0
        if (IsTurnpointSign()) {
            switch (turnpointRoute) {
                case TurnpointRoute.AssignPhoto:
                    List signs = []
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:'id'])) {
                        if (coordroute_instance.type.IsTurnpointSignCoord()) {
                            if (coordroute_instance.assignedSign == TurnpointSign.None) {
                                missingsign_num++
                            } else if (coordroute_instance.assignedSign == TurnpointSign.NoSign) {
                                // nothing
                            } else if (coordroute_instance.assignedSign in signs) {
                                doublesign_num++
                            }
                            signs += coordroute_instance.assignedSign
                        }
                            
                    }
                    break
                case TurnpointRoute.AssignCanvas:
                    List signs = []
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:'id'])) {
                        if (coordroute_instance.type.IsTurnpointSignCoord()) {
                            if (coordroute_instance.assignedSign == TurnpointSign.None) {
                                missingsign_num++
                            } else if (!coordroute_instance.assignedSign.canvas) {
                                invalidsign_num++
                            } else if (coordroute_instance.assignedSign == TurnpointSign.NoSign) {
                                // nothing
                            } else if (coordroute_instance.assignedSign in signs) {
                                doublesign_num++
                            }
                            signs += coordroute_instance.assignedSign
                        }
                    }
                    break
                case TurnpointRoute.TrueFalsePhoto:
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:'id'])) {
                        if (coordroute_instance.type.IsTurnpointSignCoord()) {
                            if (coordroute_instance.correctSign == TurnpointCorrect.Unassigned) {
                                unassigned_num++
                            }
                        }
                    }
                    break
            }
        }
        return [missingsign_num:missingsign_num, doublesign_num:doublesign_num, invalidsign_num:invalidsign_num, unassigned_num:unassigned_num]
    }
    
    boolean AllTurnpointPhotoUploaded()
    {
        if (IsTurnpointSign()) {
            switch (turnpointRoute) {
                case TurnpointRoute.AssignPhoto:
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:'id'])) {
                        if (coordroute_instance.type.IsTurnpointSignCoord()) {
                            if (coordroute_instance.assignedSign == TurnpointSign.NoSign) {
                                // nothing
                            } else if (!coordroute_instance.imagecoord) {
                                return false
                            }
                        }
                    }
                    return true
                case TurnpointRoute.TrueFalsePhoto:
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:'id'])) {
                        if (coordroute_instance.type.IsTurnpointSignCoord()) {
                            if (!coordroute_instance.imagecoord) {
                                return false
                            }
                        }
                    }
                    return true
            }
        }
        return false
    }
    
    boolean IsTurnpointSignOk()
    {
        Map turnpointsign_status = TurnpointSignStatus()
        if (turnpointsign_status.missingsign_num > 0) {
            if (true) {
                return false
            }
        }
        if (turnpointsign_status.doublesign_num > 0) {
            return false
        }
        if (turnpointsign_status.invalidsign_num > 0) {
            return false
        }
        if (turnpointsign_status.unassigned_num > 0) {
            if (true) {
                return false
            }
        }
        return true
    }
    
    String GetTurnpointSignStatusInfo()
    {
        String s = ""
        if (!IsTurnpointSignOk()) {
            Map turnpointsign_status = TurnpointSignStatus()
            if (turnpointsign_status.missingsign_num > 0) {
                s += "${turnpointsign_status.missingsign_num} ${getMsg('fc.observation.turnpoint.missingsigns')}"
            }
            if (turnpointsign_status.doublesign_num > 0) {
                if (turnpointsign_status.missingsign_num > 0) {
                    s += ", "
                }
                s += "${turnpointsign_status.doublesign_num} ${getMsg('fc.observation.turnpoint.doublesigns')}"
            }
            if (turnpointsign_status.invalidsign_num > 0) {
                if ((turnpointsign_status.missingsign_num > 0) || (turnpointsign_status.doublesign_num > 0)) {
                    s += ", "
                }
                s += "${turnpointsign_status.invalidsign_num} ${getMsg('fc.observation.turnpoint.invalidsigns')}"
            }
            if (turnpointsign_status.unassigned_num > 0) {
                if ((turnpointsign_status.missingsign_num > 0) || (turnpointsign_status.doublesign_num > 0) || (turnpointsign_status.invalidsign_num > 0)) {
                    s += ", "
                }
                s += "${turnpointsign_status.unassigned_num} ${getMsg('fc.observation.turnpoint.unassignedsigns')}"
            }
        }
        return s
    }
    
    boolean IsTurnpointSignUsed()
    {
        if (TurnpointData.findByRoute(this)) {
            return true
        }
        return false
    }
    
    boolean CanTurnpointSignModify()
    {
        if (turnpointRoute.IsTurnpointSign() && !IsTurnpointSignUsed()) {
            return true
        }
        return false
    }
    
    boolean IsEnrouteSign(boolean enroutePhoto)
    {
        if (enroutePhoto) {
            if (enroutePhotoRoute.IsEnrouteRouteInput() && enroutePhotoMeasurement.IsEnrouteMeasurement()) {
                return true
            }
        } else {
            if (enrouteCanvasRoute.IsEnrouteRouteInput() && enrouteCanvasMeasurement.IsEnrouteMeasurement()) {
                return true
            }
        }
        return false
    }
     
    boolean CanEnrouteSignModify(boolean enroutePhoto)
    {
        if (enroutePhoto) {
            if (enroutePhotoRoute.IsEnrouteRouteInput() && IsRouteComplete() && !IsEnrouteSignUsed(enroutePhoto)) {
                return true
            }
        } else {
            if (enrouteCanvasRoute.IsEnrouteRouteInput() && IsRouteComplete() && !IsEnrouteSignUsed(enroutePhoto)) {
                return true
            }
        }
        return false
    }
    
    private boolean IsEnrouteSignInput(boolean enroutePhoto)
    {
        if (enroutePhoto) {
            if (enroutePhotoRoute.IsEnrouteRouteInput()) {
                return true
            }
        } else {
            if (enrouteCanvasRoute.IsEnrouteRouteInput()) {
                return true
            }
        }
        return false
    }
    
    boolean IsAnyEnroutePhoto()
    {
        if (CoordEnroutePhoto.countByRoute(this,[sort:"enrouteViewPos"])) {
            return true
        }
        return false
    }
    
    boolean AllEnroutePhotoUploaded()
    {
        for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(this,[sort:"enrouteViewPos"])) {
            if (!coordenroutephoto_instance.imagecoord) {
                return false
            }
        }
        return true
    }
    
    boolean AnyEnroutePhotoUploaded()
    {
        for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(this,[sort:"enrouteViewPos"])) {
            if (coordenroutephoto_instance.imagecoord) {
                return true
            }
        }
        return false
    }
    
    private Map EnrouteSignStatus(boolean enroutePhoto)
    {
        int doublesign_num = 0
        int unknowncoordtype_num = 0
        int distancetolong_num = 0
        int num = 0
        int min_num = 0
        int max_num = 0
        boolean min_error = false
        boolean max_error = false
        if (enroutePhoto) {
            if (IsEnrouteSignInput(true)) {
                List enroutephoto_names = []
                for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(this,[sort:"enrouteViewPos"])) {
                    if (coordenroutephoto_instance.enroutePhotoName in enroutephoto_names) {
                        doublesign_num++
                    }
                    if (coordenroutephoto_instance.type == CoordType.UNKNOWN) {
                        if (enroutePhotoRoute != EnrouteRoute.InputName) {
                            unknowncoordtype_num++
                        }
                    } else if (!coordenroutephoto_instance.enrouteDistanceOk) {
                        distancetolong_num++
                    }
                    enroutephoto_names += coordenroutephoto_instance.enroutePhotoName
                }
                num = CoordEnroutePhoto.countByRoute(this)
                min_num = contest.minEnroutePhotos
                if (num < min_num) {
                    min_error = true
                }
                max_num = contest.maxEnroutePhotos
                if (num > max_num) {
                    max_error = true
                }
            }
        } else {
            if (IsEnrouteSignInput(false)) {
                List signs = []
                for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(this,[sort:"enrouteViewPos"])) {
                    if (!contest.enrouteCanvasMultiple) {
                        if (coordenroutecanvas_instance.enrouteCanvasSign in signs) {
                            doublesign_num++
                        }
                    }
                    if (coordenroutecanvas_instance.type == CoordType.UNKNOWN) {
                        if (enrouteCanvasRoute != EnrouteRoute.InputName) {
                            unknowncoordtype_num++
                        }
                    } else if (!coordenroutecanvas_instance.enrouteDistanceOk) {
                        distancetolong_num++
                    }
					if (coordenroutecanvas_instance.enrouteCanvasSign != EnrouteCanvasSign.NoSign) {
						signs += coordenroutecanvas_instance.enrouteCanvasSign
					}
                }
                num = CoordEnrouteCanvas.countByRoute(this)
                min_num = contest.minEnrouteCanvas
                if (num < min_num) {
                    min_error = true
                }
                max_num = contest.maxEnrouteCanvas
                if (num > max_num) {
                    max_error = true
                }
            }
        }
        return [doublesign_num:doublesign_num, unknowncoordtype_num:unknowncoordtype_num, distancetolong_num:distancetolong_num,
                num:num, min_num:min_num, max_num:max_num, min_error:min_error, max_error:max_error]
    }
    
    boolean IsEnrouteSignOk(boolean enroutePhoto)
    {
        Map status = EnrouteSignStatus(enroutePhoto)
        if ((status.doublesign_num > 0) || (status.unknowncoordtype_num > 0) || (status.distancetolong_num > 0) || status.min_error || status.max_error) {
            return false
        }
        return true
    }
    
    String GetEnrouteSignStatusInfo(boolean enroutePhoto)
    {
        Map status = EnrouteSignStatus(enroutePhoto)
        String s = ""
        boolean wr_comma = false
        if (status.doublesign_num > 0) {
            if (wr_comma) {
                s += ", "
            }
            s += "${status.doublesign_num} ${getMsg(get_name(enroutePhoto,'fc.observation.enroute.doublename'))}"
            wr_comma = true
        }
        if (status.unknowncoordtype_num > 0) {
            if (wr_comma) {
                s += ", "
            }
            s += "${status.unknowncoordtype_num} ${getMsg(get_name(enroutePhoto,'fc.observation.enroute.unknowncoordtype'))}"
            wr_comma = true
        }
        if (status.distancetolong_num > 0) {
            if (wr_comma) {
                s += ", "
            }
            s += "${status.distancetolong_num} ${getMsg(get_name(enroutePhoto,'fc.observation.enroute.distancetolong'))}"
            wr_comma = true
        }
        if (status.min_error) {
            if (wr_comma) {
                s += ", "
            }
            s += getMsgArgs(get_name(enroutePhoto,'fc.observation.enroute.numerror.min'),[status.min_num])
            wr_comma = true
        }
        if (status.max_error) {
            if (wr_comma) {
                s += ", "
            }
            s += getMsgArgs(get_name(enroutePhoto,'fc.observation.enroute.numerror.max'),[status.max_num])
            wr_comma = true
        }
        return s
    }
    
    private Map EnrouteMeasurementStatus(boolean enroutePhoto)
    {
        boolean unassigned = false
        boolean noinput = false
        boolean noposition = false
        if (enroutePhoto) {
            switch (enroutePhotoMeasurement) {
                case EnrouteMeasurement.None:
                    // nothing
                    break
                case EnrouteMeasurement.Unassigned:
                    unassigned = true
                    break
                case EnrouteMeasurement.Map:
                    if (!enroutePhotoRoute.IsEnrouteRouteInput()) {
                        noinput = true
                    }
                    break
                case EnrouteMeasurement.NMFromTP:
                case EnrouteMeasurement.mmFromTP:
                //case EnrouteMeasurement.PosFromTP:
                    if (!enroutePhotoRoute.IsEnrouteRouteInput()) {
                        noinput = true
                    } else if (!enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
                        noposition = true
                    }
                    break
            }
        } else {
            switch (enrouteCanvasMeasurement) {
                case EnrouteMeasurement.None:
                    // nothing
                    break
                case EnrouteMeasurement.Unassigned:
                    unassigned = true
                    break
                case EnrouteMeasurement.Map:
                    if (!enrouteCanvasRoute.IsEnrouteRouteInput()) {
                        noinput = true
                    }
                    break
                case EnrouteMeasurement.NMFromTP:
                case EnrouteMeasurement.mmFromTP:
                //case EnrouteMeasurement.PosFromTP:
                    if (!enrouteCanvasRoute.IsEnrouteRouteInput()) {
                        noinput = true
                    } else if (!enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
                        noposition = true
                    }
                    break
            }
        }
        return [unassigned:unassigned, noinput:noinput, noposition:noposition]
    }
    
    boolean IsEnrouteMeasurementOk(boolean enroutePhoto)
    {
        Map status = EnrouteMeasurementStatus(enroutePhoto)
        if (status.unassigned || status.noinput || status.noposition) {
            return false
        }
        return true
    }
    
    String GetEnrouteMeasurementStatusInfo(boolean enroutePhoto)
    {
        String s = ""
        if (!IsEnrouteMeasurementOk(enroutePhoto)) {
            Map status = EnrouteMeasurementStatus(enroutePhoto)
            if (status.unassigned) {
                s += "${getMsg(get_name(enroutePhoto,'fc.observation.enroute.unassigned'))}"
            } else if (status.noinput) {
                s += "${getMsg(get_name(enroutePhoto,'fc.observation.enroute.noinput'))}"
            } else if (status.noposition) {
                s += "${getMsg(get_name(enroutePhoto,'fc.observation.enroute.noposition'))}"
            }
        }
        return s
    }
    
    private String get_name(boolean enroutePhoto, String startName)
    {
        if (enroutePhoto) {
            return "${startName}.photo"
        } else {
            return "${startName}.canvas"
        }
    }
    
    List GetEnrouteCoordTitles(boolean addUnknown)
    {
        List enroute_titles = []
        if (addUnknown) {
            enroute_titles += new CoordTitle(CoordType.UNKNOWN,0)
            enroute_titles += new CoordTitle(CoordType.UNKNOWN,1)
        }
        for(RouteLegTest routelegtest_instance in RouteLegTest.findAllByRoute(this,[sort:"id"])) {
            if (routelegtest_instance.startTitle.type.IsEnrouteSignCoord()) {
                enroute_titles += routelegtest_instance.startTitle
            }
        }
        return enroute_titles
    }
    
    boolean IsEnrouteCanvasSignUsed(EnrouteCanvasSign enrouteCanvasSign)
    {
		if (enrouteCanvasSign == EnrouteCanvasSign.NoSign) {
			return false
		}
        if (CoordEnrouteCanvas.findByEnrouteCanvasSignAndRoute(enrouteCanvasSign,this,[sort:"enrouteViewPos"])) {
            return true
        }
        return false
    }
    
    boolean IsEnrouteSignUsed(boolean enroutePhoto)
    {
        if (enroutePhoto) {
            if (EnroutePhotoData.findByRoute(this)) {
                return true
            }
        } else {
            if (EnrouteCanvasData.findByRoute(this)) {
                return true
            }
        }
        return false
    }
    
    boolean IsObservationSignOk()
    {
        boolean is_observation_sign = false
        boolean sign_ok = true
        
        if (IsTurnpointSign()) {
            is_observation_sign = true
            if (!IsTurnpointSignOk()) {
                sign_ok = false
            }
        }
        
        if (IsEnrouteSignInput(true)) {
            is_observation_sign = true
            if (!IsEnrouteSignOk(true)) {
                sign_ok = false
            }
            if (!IsEnrouteMeasurementOk(true)) {
                sign_ok = false
            }
        }
        
        if (IsEnrouteSignInput(false)) {
            is_observation_sign = true
            if (!IsEnrouteSignOk(false)) {
                sign_ok = false
            }
            if (!IsEnrouteMeasurementOk(false)) {
                sign_ok = false
            }
        }
        
        return is_observation_sign && sign_ok
    }
    
    boolean IsObservationSignUsed()
    {
        if (IsTurnpointSignUsed() || IsEnrouteSignUsed(true) || IsEnrouteSignUsed(false)) {
            return true
        }
        return false
    }
    
    private Map RouteStatus()
    {
        int photo_num = CoordEnroutePhoto.countByRoute(this)
        int canavs_num = CoordEnrouteCanvas.countByRoute(this)
        int leg_num = RouteLegTest.countByRoute(this)
        
        List measure_distance_difference = []
        for (RouteLegTest routelegtest_instance in RouteLegTest.findAllByRoute(this,[sort:'id'])) {
            CoordRoute coordroute_instance = CoordRoute.findByRouteAndTypeAndTitleNumber(this,routelegtest_instance.endTitle.type,routelegtest_instance.endTitle.number)
            if (coordroute_instance.measureDistance != routelegtest_instance.legMeasureDistance) {
                measure_distance_difference += routelegtest_instance.endTitle.titleCode()
            }
        }
        
        List procedureturn_difference = []
        RouteLegCoord last_routelegcoord_instance = null
        for (RouteLegCoord routelegcoord_instance in RouteLegCoord.findAllByRoute(this,[sort:'id'])) {
            if (routelegcoord_instance.IsProcedureTurn()) {
                BigDecimal course_change = AviationMath.courseChange(routelegcoord_instance.turnTrueTrack,routelegcoord_instance.testTrueTrack())
                if (course_change.abs() >= 90) {
                    CoordRoute coordroute_instance = CoordRoute.findByRouteAndTypeAndTitleNumber(this,routelegcoord_instance.endTitle.type,routelegcoord_instance.endTitle.number)
                    if (!coordroute_instance.planProcedureTurn) {
                        procedureturn_difference += last_routelegcoord_instance.endTitle.titleCode()
                    }
                }
            }
            last_routelegcoord_instance = routelegcoord_instance
        }
        
        boolean min_target_error = false
        int min_target_num = contest.minEnrouteTargets
        if (IsEnrouteSignInput(true) || IsEnrouteSignInput(false)) {
            if (photo_num + canavs_num < min_target_num) {
                min_target_error = true
            }
        }
        
        boolean max_target_error = false
        int max_target_num = contest.maxEnrouteTargets
        if (IsEnrouteSignInput(true) || IsEnrouteSignInput(false)) {
            if (photo_num + canavs_num > max_target_num) {
                max_target_error = true
            }
        }
        
        boolean min_leg_error = false
        int min_leg_num = contest.minRouteLegs
        if (leg_num < min_leg_num) {
            min_leg_error = true
        }
        
        boolean max_leg_error = false
        int max_leg_num = contest.maxRouteLegs
        if (leg_num > max_leg_num) {
            max_leg_error = true
        }
        
        int circlecenter_num = CoordRoute.countByRouteAndCircleCenter(this, true)
		
		List secret_measure_incomplete = []
		int secret_pos = 0
		BigDecimal measure_distance = 0
		boolean secret_error_found = false
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:"id"])) {
			if (coordroute_instance.type == CoordType.SECRET) {
				secret_pos++
				if (!secret_error_found) {
					if (secret_pos > 1) {
						if (coordroute_instance.measureDistance) {
							if (!measure_distance) {
								secret_measure_incomplete += coordroute_instance.titleCode()
								secret_error_found = true
							} else if (measure_distance > coordroute_instance.measureDistance) {
								secret_measure_incomplete += coordroute_instance.titleCode()
								secret_error_found = true
							}
						} else {
							if (measure_distance) {
								secret_measure_incomplete += coordroute_instance.titleCode()
								secret_error_found = true
							}
						}
					}
					if (coordroute_instance.measureDistance) {
						measure_distance = coordroute_instance.measureDistance
					}
				}
			} else {
				secret_pos = 0
				measure_distance = 0
				secret_error_found = false
			}
		}
         
        return [min_target_num:min_target_num, max_target_num:max_target_num, min_target_error:min_target_error, max_target_error:max_target_error,
                min_leg_num:min_leg_num, max_leg_num:max_leg_num, min_leg_error:min_leg_error, max_leg_error:max_leg_error,
                measure_distance_difference:measure_distance_difference, procedureturn_difference:procedureturn_difference, circlecenter_num:circlecenter_num,
				secret_measure_incomplete:secret_measure_incomplete]
    }
    
    boolean IsRouteOk()
    {
        Map status = RouteStatus()
        if (status.min_target_error || status.max_target_error || status.min_leg_error || status.max_leg_error || status.measure_distance_difference || 
		    status.procedureturn_difference || status.circlecenter_num || status.secret_measure_incomplete) {
            return false
        }
        return true
    }
    
    String GetRouteStatusInfo()
    {
        Map status = RouteStatus()
        String s = ""
        boolean wr_comma = false
        if (status.min_leg_error) {
            if (wr_comma) {
                s += ", "
            }
            s += getMsgArgs('fc.routelegtest.numerror.min',[status.min_leg_num])
            wr_comma = true
        }
        if (status.max_leg_error) {
            if (wr_comma) {
                s += ", "
            }
            s += getMsgArgs('fc.routelegtest.numerror.max',[status.max_leg_num])
            wr_comma = true
        }
        if (status.measure_distance_difference) {
            if (wr_comma) {
                s += ", "
            }
            s += getMsgArgs('fc.routelegtest.legmeasuredistance.difference',[status.measure_distance_difference])
            wr_comma = true
        }
        if (status.procedureturn_difference) {
            if (wr_comma) {
                s += ", "
            }
            s += getMsgArgs('fc.routelegtest.procedureturn.difference',[status.procedureturn_difference])
            wr_comma = true
        }
        if (status.min_target_error) {
            if (wr_comma) {
                s += ", "
            }
            s += getMsgArgs('fc.observation.enroute.numerror.min',[status.min_target_num])
            wr_comma = true
        }
        if (status.max_target_error) {
            if (wr_comma) {
                s += ", "
            }
            s += getMsgArgs('fc.observation.enroute.numerror.max',[status.max_target_num])
            wr_comma = true
        }
        if (status.circlecenter_num) {
            if (wr_comma) {
                s += ", "
            }
            s += getMsgArgs('fc.circlecenter.error',[status.circlecenter_num])
            wr_comma = true
        }
        if (status.secret_measure_incomplete) {
            if (wr_comma) {
                s += ", "
            }
            s += getMsgArgs('fc.route.secretmeasureincomplete',[status.secret_measure_incomplete])
            wr_comma = true
        }
        return s
    }
    
    static List GetOkPlanningTestTaskRoutes(Contest contestInstance)
    {
        List ok_routes = []
        for (Route route_instance in Route.findAllByContest(contestInstance,[sort:"idTitle"])) {
            if (route_instance.IsRouteOk()) {
                ok_routes += route_instance
            }
        }
        List ret_routes = []
        // add unused routes
        for (Route route_instance in ok_routes) {
            if (!PlanningTestTask.findByRoute(route_instance,[sort:"id"])) {
                ret_routes += route_instance
            }
        }
        // add used routes
        for (Route route_instance in ok_routes) {
            if (PlanningTestTask.findByRoute(route_instance,[sort:"id"])) {
                ret_routes += route_instance
            }
        }
        return ret_routes
    }
    
	String GetPlanningTestTaskRouteName()
	{
        String ret_name = ""
		if (title) {
			ret_name = title
		} else {
            ret_name = idName()
		}
        int used_num = PlanningTestTask.findAllByRoute(this,[sort:"id"]).size()
        if (used_num) {
            ret_name += " [${getMsgArgs('fc.route.usednum',[used_num])}]"
        }
        return ret_name
	}
	
    static List GetOkFlightTestRoutes(Contest contestInstance)
    {
        List ok_routes = []
        for (Route route_instance in Route.findAllByContest(contestInstance,[sort:"idTitle"])) {
            if (route_instance.IsRouteOk()) {
                ok_routes += route_instance
            }
        }
        List ret_routes = []
        // add unused routes
        for (Route route_instance in ok_routes) {
            if (!FlightTest.findByRoute(route_instance,[sort:"id"])) {
                ret_routes += route_instance
            }
        }
        // add used routes
        for (Route route_instance in ok_routes) {
            if (FlightTest.findByRoute(route_instance,[sort:"id"])) {
                ret_routes += route_instance
            }
        }
        return ret_routes
    }
    
	String GetFlightTestRouteName()
	{
        String ret_name = ""
		if (title) {
			ret_name = title
		} else {
            ret_name = idName()
		}
        int used_num = FlightTest.findAllByRoute(this,[sort:"id"]).size()
        if (used_num) {
            ret_name += " [${getMsgArgs('fc.route.usednum',[used_num])}]"
        }
        return ret_name
	}
	
    void calculateEnroutPhotoViewPos()
    {
        if (enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
            int pos = 0
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:'id'])) {
                if (coordroute_instance.type.IsEnrouteSignCoord()) {
                    for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRouteAndTypeAndTitleNumber(this,coordroute_instance.type,coordroute_instance.titleNumber,[sort:'enrouteDistance'])) {
                        pos++
                        if (coordenroutephoto_instance.enrouteViewPos != pos) {
                            coordenroutephoto_instance.enrouteViewPos = pos
                            coordenroutephoto_instance.save()
                            //println "1. Set enrouteViewPos from '${coordenroutephoto_instance.enroutePhotoName}' to ${pos}."
                        }
                    }
                }
            }
            for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRouteAndTypeAndTitleNumber(this,CoordType.UNKNOWN,1,[sort:'enrouteViewPos'])) {
                pos++
                if (coordenroutephoto_instance.enrouteViewPos != pos) {
                    coordenroutephoto_instance.enrouteViewPos = pos
                    coordenroutephoto_instance.save()
                    //println "2. Set enrouteViewPos from '${coordenroutephoto_instance.enroutePhotoName}' to ${pos}."
                }
            }
        }
    }
    
    void calculateEnroutCanvasViewPos()
    {
        if (enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
            int pos = 0
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:'id'])) {
                if (coordroute_instance.type.IsEnrouteSignCoord()) {
                    for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRouteAndTypeAndTitleNumber(this,coordroute_instance.type,coordroute_instance.titleNumber,[sort:'enrouteDistance'])) {
                        pos++
                        if (coordenroutecanvas_instance.enrouteViewPos != pos) {
                            // println "1. Set enrouteViewPos from '${coordenroutecanvas_instance.enrouteCanvasSign}' to ${pos}."
                            coordenroutecanvas_instance.enrouteViewPos = pos
                            coordenroutecanvas_instance.save()
                        }
                    }
                }
            }
            for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRouteAndTypeAndTitleNumber(this,CoordType.FP,1,[sort:'enrouteViewPos'])) {
                pos++
                if (coordenroutecanvas_instance.enrouteViewPos != pos) {
                    // println "2. Set enrouteViewPos from '${coordenroutecanvas_instance.enrouteCanvasSign}' to ${pos}."
                    coordenroutecanvas_instance.enrouteViewPos = pos
                    coordenroutecanvas_instance.save()
                }
            }
            for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRouteAndTypeAndTitleNumber(this,CoordType.UNKNOWN,1,[sort:'enrouteViewPos'])) {
                pos++
                if (coordenroutecanvas_instance.enrouteViewPos != pos) {
                    // println "2. Set enrouteViewPos from '${coordenroutecanvas_instance.enrouteCanvasSign}' to ${pos}."
                    coordenroutecanvas_instance.enrouteViewPos = pos
                    coordenroutecanvas_instance.save()
                }
            }
        }
    }
    
    Coord GetNextEnrouteSignCoord(Coord coordInstance)
    {
        boolean found = false
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:"id"])) {
            if (found) {
                if (coordroute_instance.type.IsEnrouteSignCoord() || coordroute_instance.type.IsEnrouteFinishCoord()) {
                    return coordroute_instance
                }
            }
            if (coordroute_instance.title() == coordInstance.title()) {
                found = true
            }
        }
        return null
    }
    
    List GetCurvedPointIds()
    {
        List curved_point_ids = []
        boolean curved_point = false
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:"id",order:"desc"])) {
            if (curved_point) {
                if (coordroute_instance.type != CoordType.SECRET) {
                    curved_point = false
                } else {
                    curved_point_ids += coordroute_instance.id
                }
            } 
            if (coordroute_instance.endCurved) {
                curved_point = true
            }
        }
        return curved_point_ids
    }
    
    List GetCurvedPointTitleCodes(Media media)
    {
        List curved_point_titlecodes = []
        boolean curved_point = false
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:"id",order:"desc"])) {
            if (curved_point) {
                if (coordroute_instance.type != CoordType.SECRET) {
                    curved_point = false
                } else {
                    curved_point_titlecodes += coordroute_instance.titleMediaCode(media)
                }
            } else if (coordroute_instance.endCurved) {
                curved_point = true
            }
        }
        return curved_point_titlecodes
    }
    
    long GetNextRouteID()
    {
        boolean start_found = false
        for (Route route_instance in Route.findAllByContest(this.contest,[sort:'idTitle'])) {
            if (start_found) {
                return route_instance.id
            }
            if (route_instance.id == this.id) {
                start_found = true
            }
        }
        return 0
    }
    
    long GetPrevRouteID()
    {
        boolean start_found = false
        for (Route route_instance in Route.findAllByContest(this.contest,[sort:'idTitle', order:'desc'])) {
            if (start_found) {
                return route_instance.id
            }
            if (route_instance.id == this.id) {
                start_found = true
            }
        }
        return 0
    }
    
    Map GetRouteData()
    {
        BigDecimal distance_to2ldg = 0.0
        BigDecimal distance_sp2fp = 0.0
        int procedureturn_num = 0
        int secret_num = 0
        int curved_num = 0
        int secret_num_curved = 0
        for (RouteLegCoord routelegcoord_instance in RouteLegCoord.findAllByRoute(this,[sort:'id'])) {
            distance_to2ldg = FcMath.AddDistance(distance_to2ldg, routelegcoord_instance.testDistance())
            if (routelegcoord_instance.IsProcedureTurn()) {
                BigDecimal course_change = AviationMath.courseChange(routelegcoord_instance.turnTrueTrack, routelegcoord_instance.testTrueTrack())
                if (course_change.abs() >= 90) {
                    procedureturn_num++
                }
            }
            if (routelegcoord_instance.startTitle.type == CoordType.SECRET) {
                secret_num++
                secret_num_curved++
            } else {
                secret_num_curved = 0
            }
            if (routelegcoord_instance.endCurved) {
                curved_num++
                secret_num -=  secret_num_curved
                secret_num_curved = 0
            }
        }
        for (RouteLegTest routelegtest_instance in RouteLegTest.findAllByRoute(this,[sort:'id'])) {
            distance_sp2fp = FcMath.AddDistance(distance_sp2fp, routelegtest_instance.testDistance())
        }
        return [distance_to2ldg:distance_to2ldg, distance_sp2fp:distance_sp2fp, procedureturn_num:procedureturn_num, secret_num:secret_num, curved_num:curved_num]
    }
    
    void SetAllContestMapPoints()
    {
        boolean save_points = false
        if (!contestMapCenterPoints || contestMapCenterPoints == Defs.CONTESTMAPPOINTS_INIT) {
            save_points = true
        }
        if (!contestMapCenterPoints2 || contestMapCenterPoints2 == Defs.CONTESTMAPPOINTS_INIT) {
            save_points = true
        }
        if (!contestMapCenterPoints3 || contestMapCenterPoints3 == Defs.CONTESTMAPPOINTS_INIT) {
            save_points = true
        }
        if (!contestMapCenterPoints4 || contestMapCenterPoints4 == Defs.CONTESTMAPPOINTS_INIT) {
            save_points = true
        }
        if (!contestMapPrintPoints || contestMapPrintPoints == Defs.CONTESTMAPPOINTS_INIT) {
            save_points = true
        }
        if (!contestMapPrintPoints2 || contestMapPrintPoints2 == Defs.CONTESTMAPPOINTS_INIT) {
            save_points = true
        }
        if (!contestMapPrintPoints3 || contestMapPrintPoints3 == Defs.CONTESTMAPPOINTS_INIT) {
            save_points = true
        }
        if (!contestMapPrintPoints4 || contestMapPrintPoints4 == Defs.CONTESTMAPPOINTS_INIT) {
            save_points = true
        }
        if (!contestMapCurvedLegPoints || contestMapCurvedLegPoints == Defs.CONTESTMAPPOINTS_INIT) {
            save_points = true
        }
        if (save_points) {
            String center_points = ""
            String print_points = ""
            String curvedleg_points = ""
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(this,[sort:"id"])) {
                if (coordroute_instance.type.IsContestMapQuestionCoord()) {
                    if (center_points) {
                        center_points += ",${coordroute_instance.title()}"
                    } else {
                        center_points = coordroute_instance.title() 
                    }
                    if (!coordroute_instance.type.IsRunwayCoord() || coordroute_instance.type == CoordType.TO) {
                        if (print_points) {
                            print_points += ",${coordroute_instance.title()}"
                        } else {
                            print_points = coordroute_instance.title() 
                        }
                    }
                    if (coordroute_instance.endCurved) {
                        if (curvedleg_points) {
                            curvedleg_points += ",${coordroute_instance.title()}"
                        } else {
                            curvedleg_points = coordroute_instance.title() 
                        }
                    }
                }
            }
            center_points += ","
            print_points += ","
            curvedleg_points += ","
            if (!contestMapCenterPoints || contestMapCenterPoints == Defs.CONTESTMAPPOINTS_INIT) {
                contestMapCenterPoints = DisabledCheckPointsTools.Compress(center_points, this)
            }
            if (!contestMapCenterPoints2 || contestMapCenterPoints2 == Defs.CONTESTMAPPOINTS_INIT) {
                contestMapCenterPoints2 = DisabledCheckPointsTools.Compress(center_points, this)
            }
            if (!contestMapCenterPoints3 || contestMapCenterPoints3 == Defs.CONTESTMAPPOINTS_INIT) {
                contestMapCenterPoints3 = DisabledCheckPointsTools.Compress(center_points, this)
            }
            if (!contestMapCenterPoints4 || contestMapCenterPoints4 == Defs.CONTESTMAPPOINTS_INIT) {
                contestMapCenterPoints4 = DisabledCheckPointsTools.Compress(center_points, this)
            }
            if (!contestMapPrintPoints || contestMapPrintPoints == Defs.CONTESTMAPPOINTS_INIT) {
                contestMapPrintPoints = DisabledCheckPointsTools.Compress(print_points, this)
            }
            if (!contestMapPrintPoints2 || contestMapPrintPoints2 == Defs.CONTESTMAPPOINTS_INIT) {
                contestMapPrintPoints2 = DisabledCheckPointsTools.Compress(print_points, this)
            }
            if (!contestMapPrintPoints3 || contestMapPrintPoints3 == Defs.CONTESTMAPPOINTS_INIT) {
                contestMapPrintPoints3 = DisabledCheckPointsTools.Compress(print_points, this)
            }
            if (!contestMapPrintPoints4 || contestMapPrintPoints4 == Defs.CONTESTMAPPOINTS_INIT) {
                contestMapPrintPoints4 = DisabledCheckPointsTools.Compress(print_points, this)
            }
            if (!contestMapCurvedLegPoints || contestMapCurvedLegPoints == Defs.CONTESTMAPPOINTS_INIT) {
                contestMapCurvedLegPoints = DisabledCheckPointsTools.Compress(curvedleg_points, this)
            }
        }
    }
    
    BigDecimal Convert_mm2NM(BigDecimal measureValue, boolean round = false)
    {
        if (measureValue == null) {
            return null
        }
        BigDecimal nm_value = mapScale * measureValue / mmPerNM
        if (round) {
            return FcMath.RoundEnrouteDistance(nm_value)
        }
        return nm_value
    }

    BigDecimal Convert_NM2mm(BigDecimal distanceValue, boolean round = false)
    {
        if (distanceValue == null) {
            return null
        }
        BigDecimal mm_value = distanceValue * mmPerNM / mapScale
        if (round) {
            return FcMath.RoundMeasureDistance(mm_value)
        }
        return mm_value
    }

    static BigDecimal Convert_NM2m(BigDecimal distanceValue)
    {
        if (distanceValue == null) {
            return null
        }
        return distanceValue * mmPerNM / 1000 
    }

    BigDecimal Convert_mm2km(BigDecimal measureValue)
    {
        if (measureValue == null) {
            return null
        }
        return mapScale * measureValue / mmPerkm
    }

    BigDecimal Convert_km2mm(Contest contestInstance, BigDecimal distanceValue)
    {
        if (distanceValue == null) {
            return null
        }
        return distanceValue * mmPerkm / mapScale 
    }
    
    Map GetFlightTestWindDirection()
    {
        Map ret = [TODirection:0.0, LDGDirection:0.0, iTOiLDGDirection:0.0]
        CoordRoute.findAllByRoute(this,[sort:"id"]).each { CoordRoute coordroute_instance ->
            switch (coordroute_instance.type) {
                case CoordType.TO:
                    ret.TODirection = coordroute_instance.gateDirection
                    break
                case CoordType.LDG:
                    ret.LDGDirection = coordroute_instance.gateDirection
                    break
                case CoordType.iTO:
                case CoordType.iLDG:
                    ret.iTOiLDGDirection = coordroute_instance.gateDirection
                    break
            }
        }
        return ret
    }
}
