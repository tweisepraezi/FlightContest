import java.util.Map;

class RouteFileTools
{
    final static String GPX_EXTENSION = ".gpx"
    final static String REF_EXTENSION = ".ref"
    final static String TXT_EXTENSION = ".txt"
    
    final static String ROUTE_EXTENSIONS = "${RouteFileTools.GPX_EXTENSION}, ${RouteFileTools.REF_EXTENSION}, ${RouteFileTools.TXT_EXTENSION}"
    final static String FC_ROUTE_EXTENSIONS = "${RouteFileTools.GPX_EXTENSION}"
    
    final static Float DEFAULT_GATEWIDTH_RUNWAY = 0.02f
    final static Float DEFAULT_GATEWIDTH_TP = 1.0f
    final static Float DEFAULT_GATEWIDTH_SC = 2.0f
    
    //--------------------------------------------------------------------------
    static Map ReadRouteFile(String fileExtension, Contest contestInstance, String routeFileName, String originalFileName, Map importParams)
    // Return: gatenum - Number of gates 
    //         valid   - true, if valid route file format
    //         errors  - <> ""
    {
        switch (fileExtension) {
            case GPX_EXTENSION:
                Map route_data = ReadGPXFile(contestInstance, routeFileName, originalFileName)
                return create_route(contestInstance, route_data, importParams)
            case REF_EXTENSION:
                Map route_data = ReadREFFile(contestInstance, routeFileName, originalFileName)
                return create_route(contestInstance, route_data, importParams)
            case TXT_EXTENSION:
                Map route_data = ReadTXTFile(contestInstance, routeFileName, originalFileName)
                return create_route(contestInstance, route_data, importParams)
        }
        return [gatenum: 0, valid: false, errors: ""]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadGPXFile(Contest contestInstance, String gpxFileName, String originalFileName)
    // Return: gates     - List of gates (lat, lon, alt)
    //         routename - Name of route
    //         valid     - true, if valid route file format
    //         errors    - <> ""
    {
        List gates = []
        String route_name = ""
        boolean valid_format = false
        String read_errors = ""
        
        File gpx_file = new File(gpxFileName)

        FileReader gpx_reader = new FileReader(gpx_file)
        try {
            def gpx = new XmlParser().parse(gpx_reader)
            if (gpx.rte.size() == 1) {
                
                route_name = gpx.rte[0].name[0].text()
                if (route_name) {
                    int num = 0
                    while (Route.findByContestAndTitle(contestInstance, route_name)) {
                        num++
                        route_name = "${gpx.rte[0].name[0].text()} ($num)"
                    }
                }
                
                BigDecimal last_latitude = null
                BigDecimal last_longitude = null
                def last_track = null
                for (rtept in gpx.rte[0].rtept) {
                    valid_format = true
                    
                    BigDecimal latitude = rtept.'@lat'.toBigDecimal()
                    BigDecimal longitude = rtept.'@lon'.toBigDecimal()
                    
                    Map lat = CoordPresentation.GetDirectionGradDecimalMinute(latitude, true)
                    Map lon = CoordPresentation.GetDirectionGradDecimalMinute(longitude, false)
                    Integer alt = (rtept.ele[0].text().toBigDecimal() * GpxService.ftPerMeter).toInteger()
                    
                    def track = null
                    if (last_latitude != null && last_longitude != null) {
                        if ((latitude == last_latitude) && (longitude == last_longitude)) {
                            track = last_track
                        } else {
                            Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                            track = leg.dir // FcMath.RoundGrad(leg.dir)
                        }
                    }
                    
                    Map gate = [lat:lat, lon:lon, alt:alt, track:track, next_track:null]
                    gates += gate
                    
                    last_latitude = latitude
                    last_longitude = longitude
                    last_track = track
                }
                
            }
        } catch (Exception e) {
            read_errors += e.getMessage()
        }
        gpx_reader.close()
        
        return [gates: gates, routename: route_name, valid: valid_format, errors: read_errors]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadREFFile(Contest contestInstance, String refFileName, String originalFileName)
    // Return: gates     - List of gates (lat, lon, alt)
    //         routename - Name of route
    //         valid     - true, if valid route file format
    //         errors    - <> ""
    {
        List gates = []
        String route_name = ""
        boolean valid_format = false
        String read_errors = ""
        
        File ref_file = new File(refFileName)
        LineNumberReader ref_reader = ref_file.newReader()
        
        try {
            String org_file_name = originalFileName.substring(0,originalFileName.lastIndexOf('.'))
            route_name = org_file_name
            if (route_name) {
                int num = 0
                while (Route.findByContestAndTitle(contestInstance, route_name)) {
                    num++
                    route_name = "${org_file_name} ($num)"
                }
            }
            
            BigDecimal last_latitude = null
            BigDecimal last_longitude = null
            def last_track = null
            while (true) {
                String line = ref_reader.readLine()
                if (line == null) {
                    break
                }
                if (line && line.startsWith('"') && line.contains(';')) {
                    valid_format = true
                    
                    if (line.contains("End Of File;")) {
                        break
                    }
                    
                    def line_values = line.split(';')
                    if (line_values.size() == 13) {
                        
                        // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                        String latitude_str = line_values[1].trim()
                        String latitude_grad = latitude_str.substring(2,5)
                        BigDecimal latitude_grad_math = latitude_grad.toBigDecimal()
                        String latidude_minute = latitude_str.substring(6,14)
                        BigDecimal latidude_minute_math = latidude_minute.toBigDecimal()
                        boolean latitude_north = latitude_str.substring(0,1) == 'N'
                        BigDecimal latitude = latitude_grad_math + (latidude_minute_math / 60)
                        if (!latitude_north) {
                            latitude *= -1
                        }
                        Map lat = CoordPresentation.GetDirectionGradDecimalMinute(latitude, true)
                        
                        // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                        String longitude_str = line_values[2].trim()
                        String longitude_grad = longitude_str.substring(2,5)
                        BigDecimal longitude_grad_math = longitude_grad.toBigDecimal()
                        String longitude_minute = longitude_str.substring(6,14)
                        BigDecimal longitude_minute_math = longitude_minute.toBigDecimal()
                        boolean longitude_east = longitude_str.substring(0,1) == 'E'
                        BigDecimal longitude = longitude_grad_math + (longitude_minute_math / 60)
                        if (!longitude_east) {
                            longitude *= -1
                        }
                        Map lon = CoordPresentation.GetDirectionGradDecimalMinute(longitude, false)
                        
                        // Altitude (Höhe) in ft
                        String altitude_foot_str = line_values[3].trim()
                        Integer alt = altitude_foot_str.toInteger()
                        
                        def track = null
                        if (last_latitude != null && last_longitude != null) {
                            if ((latitude == last_latitude) && (longitude == last_longitude)) {
                                track = last_track
                            } else {
                                Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                                track = leg.dir
                            }
                        }
                        
                        Map gate = [lat:lat, lon:lon, alt:alt, track:track, next_track:null]
                        gates += gate
                        
                        last_latitude = latitude
                        last_longitude = longitude
                        last_track = track
                    }
                }
            }
            
        } catch (Exception e) {
            read_errors += e.getMessage()
        }
        ref_reader.close()
        
        return [gates: gates, routename: route_name, valid: valid_format, errors: read_errors]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadTXTFile(Contest contestInstance, String txtFileName, String originalFileName)
    // Return: gates     - List of gates (lat, lon, alt)
    //         routename - Name of route
    //         valid     - true, if valid route file format
    //         errors    - <> ""
    {
        List gates = []
        String route_name = ""
        boolean valid_format = false
        String read_errors = ""
        
        File txt_file = new File(txtFileName)
        LineNumberReader txt_reader = txt_file.newReader()
        
        try {
            String org_file_name = originalFileName.substring(0,originalFileName.lastIndexOf('.'))
            route_name = org_file_name
            if (route_name) {
                int num = 0
                while (Route.findByContestAndTitle(contestInstance, route_name)) {
                    num++
                    route_name = "${org_file_name} ($num)"
                }
            }
            
            BigDecimal last_latitude = null
            BigDecimal last_longitude = null
            def last_track = null
            while (true) {
                String line = txt_reader.readLine()
                if (line == null) {
                    break
                }
                if (line && !line.startsWith('#')) {
                    valid_format = true
                    def line_values = line.split(',')
                    if (line_values.size() == 3) {
                        
                        BigDecimal latitude = line_values[0].trim().toBigDecimal()
                        BigDecimal longitude = line_values[1].trim().toBigDecimal()
                        
                        Map lat = CoordPresentation.GetDirectionGradDecimalMinute(latitude, true)
                        Map lon = CoordPresentation.GetDirectionGradDecimalMinute(longitude, false)
                        Integer alt = (line_values[2].trim().toBigDecimal() * GpxService.ftPerMeter).toInteger()
                        
                        def track = null
                        if (last_latitude != null && last_longitude != null) {
                            if ((latitude == last_latitude) && (longitude == last_longitude)) {
                                track = last_track
                            } else {
                                Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                                track = leg.dir
                            }
                        }
                        
                        Map gate = [lat:lat, lon:lon, alt:alt, track:track, next_track:null]
                        gates += gate
                        
                        last_latitude = latitude
                        last_longitude = longitude
                        last_track = track
                    }
                }
            }
            
        } catch (Exception e) {
            read_errors += e.getMessage()
        }
        txt_reader.close()
        
        return [gates: gates, routename: route_name, valid: valid_format, errors: read_errors]
    }
    
    //--------------------------------------------------------------------------
    private static Map create_route(Contest contestInstance, Map routeData, Map importParams)
    // Return: gatenum - Number of gates 
    //         valid   - true, if valid gpx format
    //         errors  - <> ""
    {
        int coord_num = routeData.gates.size()
        
        if (!routeData.valid || routeData.errors) {
            return [gatenum: coord_num, valid: routeData.valid, errors: routeData.errors]
        }
        
        def last_next_track = null
        if (importParams.autosecret) {
            for (gate in routeData.gates.reverse()) {
                if (last_next_track) {
                    gate.next_track = last_next_track
                }
                last_next_track = gate.track
            }
        }
        
        String read_errors = ""
        int gate_pos = 0
        int tp_pos = 1
        int sc_pos = 1
        
        int to_pos = 0
        if (importParams.firstcoordto) {
            to_pos =  1
        }
        
        int fp_pos = coord_num
        int ldg_pos = 0
        if (importParams.ldg == 0) { // lastcoord
            fp_pos = coord_num - 1
            ldg_pos = coord_num
        } else if (importParams.firstcoordto && importParams.ldg == 1) { // addto
            routeData.gates += routeData.gates[0]
            fp_pos = coord_num
            ldg_pos = coord_num + 1
        }
        
        // check pos (to_pos importParams.ildgpos importParams.curvedstartpos importParams.curvedendpos fp_pos ldg_pos)
        if (importParams.curved) {
            if (!importParams.curvedstartpos) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved leg missed."]
            }
            if (importParams.curvedstartpos <= 0) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved leg must be greater 0."]
            }
            if (!importParams.curvedendpos) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved leg missed."]
            }
            if (importParams.curvedendpos <= 0) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved leg  must be greater 0."]
            }
            if (importParams.curvedstartpos + 1 >= importParams.curvedendpos) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved leg  must be greater than start pos."]
            }
            if (to_pos + 2 > importParams.curvedstartpos) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved leg must be located after SP."]
            }
            if (importParams.curvedendpos >= fp_pos) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved leg must be located before FP."]
            }
        }
        if (importParams.ildg) {
            if (!importParams.ildgpos) {
                return [gatenum: coord_num, valid: true, errors: "iLDG pos missed."]
            }
            if (importParams.ildgpos <= 0) {
                return [gatenum: coord_num, valid: true, errors: "iLDG pos must be greater 0."]
            }
            if (to_pos + 3 > importParams.ildgpos) {
                return [gatenum: coord_num, valid: true, errors: "iFP/iLDG/iSP must be located after SP."]
            }
            if (importParams.ildgpos + 1 >= fp_pos) {
                return [gatenum: coord_num, valid: true, errors: "iFP/iLDG/iSP must be located before FP."]
            }
        }
        if (importParams.curved && importParams.ildg) {
            if (importParams.ildgpos + 1 < importParams.curvedstartpos) {
                // ok
            } else if (importParams.ildgpos > importParams.curvedendpos + 1) {
                // ok
            } else {
                return [gatenum: coord_num, valid: true, errors: "iFP/iLDG/iSP must not be located in curved leg."]
            }
        }
        
        // create route
        Route route_instance = new Route()
        route_instance.contest = contestInstance
        route_instance.idTitle = Route.countByContest(contestInstance) + 1
        route_instance.title = routeData.routename
        if (!route_instance.save()) {
            return [gatenum: coord_num, valid: false, errors: "Could not save route ${routeData.routename}"]
        }
        
        // add coords to route
        last_next_track = null
        for (gate in routeData.gates) {
            gate_pos++
            
            BigDecimal track_diff = null
            if (importParams.autosecret) {
                if (last_next_track != null && gate.next_track != null) {
                    track_diff = (gate.next_track - last_next_track).abs()
                }
            }
            
            CoordRoute coordroute_instance = new CoordRoute()
            coordroute_instance.route = route_instance
            
            // T/O
            if (gate_pos == to_pos) {
                coordroute_instance.type = CoordType.TO
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_RUNWAY
                coordroute_instance.gateDirection = importParams.todirection
            // SP
            } else if (gate_pos == to_pos + 1) {
                coordroute_instance.type = CoordType.SP
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP // TODO: Gatewidth from rule
            // FP
            } else if (gate_pos == fp_pos) {
                coordroute_instance.type = CoordType.FP
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
            // LDG
            } else if (gate_pos == ldg_pos) {
                coordroute_instance.type = CoordType.LDG
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_RUNWAY
                coordroute_instance.gateDirection = importParams.ldgdirection
            // iFP, iLDG, iSP
            } else if (importParams.ildg && gate_pos + 1 == importParams.ildgpos) {
                coordroute_instance.type = CoordType.iFP
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
            } else if (importParams.ildg && gate_pos == importParams.ildgpos) {
                coordroute_instance.type = CoordType.iLDG
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_RUNWAY
                coordroute_instance.gateDirection = importParams.ildgdirection
                coordroute_instance.noTimeCheck = true
                coordroute_instance.noGateCheck = true
                coordroute_instance.noPlanningTest = true
            } else if (importParams.ildg && gate_pos == importParams.ildgpos + 1) {
                coordroute_instance.type = CoordType.iSP
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                coordroute_instance.noPlanningTest = true
            // Curved (TP, SC..., TP)
            } else if (importParams.curved && gate_pos == importParams.curvedstartpos ) {
                coordroute_instance.type = CoordType.TP
                coordroute_instance.titleNumber = tp_pos
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                tp_pos++
            } else if (importParams.curved && gate_pos == importParams.curvedendpos) {
                coordroute_instance.type = CoordType.TP
                coordroute_instance.titleNumber = tp_pos
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                coordroute_instance.noPlanningTest = true
                coordroute_instance.endCurved = true
                tp_pos++
            } else if (importParams.curved && gate_pos > importParams.curvedstartpos && gate_pos < importParams.curvedendpos) {
                coordroute_instance.type = CoordType.SECRET
                coordroute_instance.titleNumber = sc_pos
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_SC // TODO: Gatewidth from rule
                coordroute_instance.noTimeCheck = true
                coordroute_instance.noGateCheck = true
                coordroute_instance.noPlanningTest = true
                sc_pos++
            // SC
            } else if (track_diff != null && track_diff < 1) {
                coordroute_instance.type = CoordType.SECRET
                coordroute_instance.titleNumber = sc_pos
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_SC // TODO: Gatewidth from rule
                sc_pos++
            // TP
            } else {
                coordroute_instance.type = CoordType.TP
                coordroute_instance.titleNumber = tp_pos
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                tp_pos++
            }
            
            coordroute_instance.latDirection = gate.lat.direction
            coordroute_instance.latGrad = gate.lat.grad
            coordroute_instance.latMinute = gate.lat.minute
            
            coordroute_instance.lonDirection = gate.lon.direction
            coordroute_instance.lonGrad = gate.lon.grad
            coordroute_instance.lonMinute = gate.lon.minute
            
            coordroute_instance.altitude = gate.alt
            
            coordroute_instance.measureEntered = true

            if (!coordroute_instance.save()) {
                read_errors = "Could not save ${coordroute_instance.title()}"
            }
            
            if (read_errors) {
                break
            }
            
            last_next_track = gate.next_track
        }

        return [gatenum: coord_num, valid: true, errors: read_errors, route: route_instance]
    }
            
    //--------------------------------------------------------------------------
    static Map ReadFcRouteFile(String fileExtension, Contest contestInstance, String routeFileName)
    // Return: gatenum - Number of gates 
    //         valid   - true, if valid FC gpx format
    //         errors  - <> ""
    {
        switch (fileExtension) {
            case GPX_EXTENSION:
                return ReadFCGPXFile(contestInstance, routeFileName)
                break
        }
        return [gatenum: 0, valid: false, errors: ""]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadFCGPXFile(Contest contestInstance, String gpxFileName)
    // Return: gatenum - Number of gates 
    //         valid   - true, if valid FC gpx format
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
