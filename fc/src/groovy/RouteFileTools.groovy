class RouteFileTools
{
    final static String GPX_EXTENSION = ".gpx"
    
    final static String ROUTE_EXTENSIONS = "${RouteFileTools.GPX_EXTENSION}"
    
    //--------------------------------------------------------------------------
    static Map ReadRouteFile(String fileExtension, Contest contestInstance, String routeFileName)
    // Return: gatenum - Number of gates 
    //         valid   - true, if valid logger format
    //         errors  - <> ""
    {
        switch (fileExtension) {
            case GPX_EXTENSION:
                return ReadGPXFile(contestInstance, routeFileName)
                break
        }
        return [gatenum: 0, valid: false, errors: ""]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadGPXFile(Contest contestInstance, String gpxFileName)
    // Return: gatenum - Number of gates 
    //         valid   - true, if valid gpx format
    //         errors  - <> ""
    {
        int gate_num = 0
        boolean valid_format = false
        String read_errors = ""
        Route route_instance = null
        
        File gpx_file = new File(gpxFileName)

        FileReader gpx_reader = new FileReader(gpx_file)
        try {
            def gpx = new XmlParser().parse(gpx_reader)
            if (gpx.rte.size() > 0) {
                
                boolean first = true
                String route_name = ""
                for (rte in gpx.rte) {
                    if (!route_name && rte.extensions.flightcontest.route) {
                        route_name = rte.name[0].text()
                        int num = 0
                        while (Route.findByContestAndTitle(contestInstance, route_name)) {
                            num++
                            route_name = "${rte.name[0].text()} ($num)"
                        }
                    }
                    if (rte.extensions.flightcontest.gate) {
                        valid_format = true
                        gate_num++
                        if (first) {
                            route_instance = new Route()
                            route_instance.contest = contestInstance
                            route_instance.idTitle = Route.countByContest(contestInstance) + 1
                            route_instance.title = route_name
                            route_instance.save()
                            //println "Route saved."
                            first = false
                        }
                        
                        CoordRoute coordroute_instance = new CoordRoute()
                        coordroute_instance.route = route_instance
                        
                        coordroute_instance.type = CoordType.(rte.extensions.flightcontest.gate.'@type'[0])
                        coordroute_instance.titleNumber = rte.extensions.flightcontest.gate.'@number'[0].toInteger()
                        
                        Map lat = CoordPresentation.GetDirectionGradDecimalMinute(rte.extensions.flightcontest.gate.'@lat'[0].toBigDecimal(), true)
                        coordroute_instance.latDirection = lat.direction
                        coordroute_instance.latGrad = lat.grad
                        coordroute_instance.latMinute = lat.minute
                        
                        Map lon = CoordPresentation.GetDirectionGradDecimalMinute(rte.extensions.flightcontest.gate.'@lon'[0].toBigDecimal(), false)
                        coordroute_instance.lonDirection = lon.direction
                        coordroute_instance.lonGrad = lon.grad
                        coordroute_instance.lonMinute = lon.minute
                        
                        //coordroute_instance.altitude = rte.extensions.flightcontest.gate.'@ele'[0].toBigDecimal() * GpxService.ftPerMeter
                        coordroute_instance.altitude = rte.extensions.flightcontest.gate.'@alt'[0].toInteger()
                        coordroute_instance.gatewidth2 = rte.extensions.flightcontest.gate.'@width'[0].toFloat()
                        
                        if (coordroute_instance.type.IsRunwayCoord()) {
                            String dir = rte.extensions.flightcontest.gate.'@dir'[0]
                            if (dir) {
                                coordroute_instance.gateDirection = rte.extensions.flightcontest.gate.'@dir'[0].toBigDecimal()
                            }
                        }
                        coordroute_instance.noTimeCheck = rte.extensions.flightcontest.gate.'@notimecheck'[0] == "yes"
                        coordroute_instance.noGateCheck = rte.extensions.flightcontest.gate.'@nogatecheck'[0] == "yes"
                        coordroute_instance.noPlanningTest = rte.extensions.flightcontest.gate.'@noplanningtest'[0] == "yes"
                        coordroute_instance.endCurved = rte.extensions.flightcontest.gate.'@endcurved'[0] == "yes"
                        
                        String dist = rte.extensions.flightcontest.gate.'@dist'[0]
                        if (dist) {
                            coordroute_instance.measureDistance = dist.toBigDecimal()
                        }
                        String track = rte.extensions.flightcontest.gate.'@track'[0]
                        if (track) {
                            coordroute_instance.measureTrueTrack = track.toBigDecimal()
                        } 
                        String duration = rte.extensions.flightcontest.gate.'@duration'[0]
                        if (duration) {
                            coordroute_instance.legDuration = duration.toInteger()
                        }
                        coordroute_instance.measureEntered = true

                        if (!coordroute_instance.save()) {
                            read_errors = "Could not save ${coordroute_instance.title()}"
                        }
                    }
                    if (read_errors) {
                        break
                    }
                }
            }
        } catch (Exception e) {
            read_errors += e.getMessage()
        }
        gpx_reader.close()
        
        return [gatenum: gate_num, valid: valid_format, errors: read_errors, route: route_instance]
    }        
}
