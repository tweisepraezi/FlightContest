class Route 
{
    def grailsApplication
    
	String title
    int idTitle
    String mark = ""
    Boolean showAflosMark = false                          // DB-2.12
    
	static belongsTo = [contest:Contest]

	static hasMany = [coords:CoordRoute,routelegs:RouteLegCoord,testlegs:RouteLegTest]

	static constraints = {
		contest(nullable:false)
        
        // DB-2.12 compatibility
        showAflosMark(nullable:true)
	}

	static mapping = {
		coords sort:"id"
		routelegs sort:"id"
		testlegs sort:"id"
	}
	
	void CopyValues(Route routeInstance)
	{
		title = routeInstance.title
		idTitle = routeInstance.idTitle
		mark = routeInstance.mark
		
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
    
    boolean ExistAnyAflosRoute()
    {
        return AflosTools.ExistAnyAflosRoute(contest)
    }
    
    boolean IsAflosReferenceExportPossible()
    {
        if (mark) {
            return true
        }
        /*
        if (showAflosMark || AflosTools.GetAflosRouteName(contest, mark)) {
            return true
        }
        */
        return false
    }
}
