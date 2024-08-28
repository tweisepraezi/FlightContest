import java.awt.geom.Line2D;
import java.util.Map;

class RouteFileTools
{
    final static String GPX_EXTENSION = ".gpx"
    final static String KML_EXTENSION = ".kml"
    final static String KMZ_EXTENSION = ".kmz"
    final static String REF_EXTENSION = ".ref"
    final static String TXT_EXTENSION = ".txt"
    
    final static String ROUTE_EXTENSIONS = "${RouteFileTools.GPX_EXTENSION}, ${RouteFileTools.KML_EXTENSION}, ${RouteFileTools.KMZ_EXTENSION}, ${RouteFileTools.REF_EXTENSION}, ${RouteFileTools.TXT_EXTENSION}"
    final static String FC_ROUTE_EXTENSIONS = "${RouteFileTools.GPX_EXTENSION}, ${RouteFileTools.KML_EXTENSION}, ${RouteFileTools.KMZ_EXTENSION}"
    final static String ENROUTE_SIGN_EXTENSIONS = "${RouteFileTools.KML_EXTENSION}, ${RouteFileTools.KMZ_EXTENSION}, ${RouteFileTools.TXT_EXTENSION}"
    final static String TURNPOINT_EXTENSIONS = "${RouteFileTools.TXT_EXTENSION}"
    
    final static Float DEFAULT_GATEWIDTH_RUNWAY = 0.02f
    final static Float DEFAULT_GATEWIDTH_TP = 1.0f
    
    final static String ALT = "Alt"
    final static String MINALT = "MinAlt"
    final static String MAXALT = "MaxAlt"
    final static String GATE = "Gate"
    final static String DURATION = "Duration"
    final static String DIST = "Dist"
    final static String TRACK = "Track"
    final static String SIGN = "Sign"
    final static String UNIT_mm = "mm"
    final static String UNIT_m = "m"
    final static String UNIT_ft = "ft"
    final static String UNIT_NM = "NM"
    final static String UNIT_min = "min"
    final static String UNIT_GRAD = (char)176 // direction
    final static String UNIT_TPcorrect = "yes"
    final static String UNIT_TPincorrect = "no"
    final static String UNIT_TPnotimecheck = "notime"
    final static String UNIT_TPnogatecheck = "nogate"
    final static String UNIT_TPnoplanningtest = "noplan"
    final static String UNIT_TPendcurved = "endcurved"
    final static String UNIT_TPcirclecenter = "circlecenter"
    final static String UNIT_TPsemicircleinvert = "invert"
    final static String UNIT_TPignoregate = "ignoregate"
    final static String UNIT_TPnosign = "-"
    
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
            case KML_EXTENSION:
                Map route_data = ReadKMLFile(contestInstance, routeFileName, originalFileName, importParams.foldername, importParams.readplacemarks, false)
                return create_route(contestInstance, route_data, importParams)
            case KMZ_EXTENSION:
                Map route_data = ReadKMLFile(contestInstance, routeFileName, originalFileName, importParams.foldername, importParams.readplacemarks, true)
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
            String org_file_name = originalFileName.substring(0,originalFileName.lastIndexOf('.'))
            route_name = org_file_name
            if (route_name) {
                int num = 0
                while (Route.findByContestAndTitle(contestInstance, route_name)) {
                    num++
                    route_name = "${org_file_name} ($num)"
                }
            }
            
            def gpx = new XmlParser().parse(gpx_reader)
            if (gpx.rte.size() == 1) {
                
				/*
                route_name = gpx.rte[0].name[0].text()
                if (route_name) {
                    int num = 0
                    while (Route.findByContestAndTitle(contestInstance, route_name)) {
                        num++
                        route_name = "${gpx.rte[0].name[0].text()} ($num)"
                    }
                }
				*/
                
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
    private static Map ReadKMLFile(Contest contestInstance, String kmFileName, String originalFileName, String folderName, boolean readPlacemarks, boolean kmzFile)
    // Return: gates     - List of gates (lat, lon, alt)
    //         routename - Name of route
    //         valid     - true, if valid route file format
    //         errors    - <> ""
    {
        List gates = []
        String route_name = ""
        boolean valid_format = false
        String read_errors = ""
        
        File km_file = new File(kmFileName)
        def kmz_file = null
        def km_reader = null
        if (kmzFile) {
            kmz_file = new java.util.zip.ZipFile(km_file)
            kmz_file.entries().findAll { !it.directory }.each {
                if (!km_reader) {
                    km_reader = kmz_file.getInputStream(it)
                }
            }
        } else {
            //km_reader = new FileReader(km_file)
            km_reader = new InputStreamReader(new FileInputStream(km_file), "UTF-8")
        }

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
            
            def kml = new XmlParser().parse(km_reader)
            
            if (!readPlacemarks && kml.Document.Placemark.LineString.coordinates) {
                String coordinates = kml.Document.Placemark.LineString.coordinates.text()
                Map kml_coords = ReadKMLCoordinates(coordinates, null, null)
                gates += kml_coords.gates
                if (gates.size()) {
                    valid_format = true
                }
            } else {
                def folder = null
                boolean root_folder = false
                if (folderName) {
                    folder = search_folder_by_name(kml.Document, folderName)
                } else {
                    folder = kml.Document // root
                }
                if (folder && folder.Placemark) {
                    BigDecimal last_latitude = null
                    BigDecimal last_longitude = null
                    for (def pm in folder.Placemark) {
                        if (readPlacemarks) {
                            if (pm.Point.coordinates) {
                                String coordinates = pm.Point.coordinates.text()
                                Map kml_coords = ReadKMLCoordinates(coordinates, last_latitude, last_longitude)
                                gates += kml_coords.gates
                                last_latitude = kml_coords.lastlatitude
                                last_longitude = kml_coords.lastlongitude
                            }
                        } else {
                            if (pm.LineString.coordinates) {
                                String coordinates = pm.LineString.coordinates.text()
                                Map kml_coords = ReadKMLCoordinates(coordinates, last_latitude, last_longitude)
                                gates += kml_coords.gates
                                last_latitude = kml_coords.lastlatitude
                                last_longitude = kml_coords.lastlongitude
                            }
                        }
                    }
                    if (gates.size()) {
                        valid_format = true
                    }
                }
            }
        } catch (Exception e) {
            read_errors += e.getMessage()
        }
        if (km_reader) {
            km_reader.close()
        }
        if (kmz_file) {
            kmz_file.close()
        }

        return [gates: gates, routename: route_name, valid: valid_format, errors: read_errors]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadKMLCoordinates(String coordinateValues, BigDecimal lastLatitude, BigDecimal lastLongitude)
    // Return: gates                       - List of gates (lat, lon, alt)
    //         lastlatitude, lastlongitude - last coordinate
    {
        List gates = []
        
        String coordinate_values = coordinateValues.replaceAll("\n"," ").replaceAll("\r"," ").replaceAll("\t"," ")
        
        BigDecimal last_latitude = lastLatitude
        BigDecimal last_longitude = lastLongitude
        def last_track = null
        for (coordinate in coordinate_values.split(' ')) {
            if (coordinate) {
                List coord = coordinate.split(',')
                BigDecimal latitude = coord[1].toBigDecimal()
                BigDecimal longitude = coord[0].toBigDecimal()
                
                Map lat = CoordPresentation.GetDirectionGradDecimalMinute(latitude, true)
                Map lon = CoordPresentation.GetDirectionGradDecimalMinute(longitude, false)
                Integer alt = (coord[2].toBigDecimal() * GpxService.ftPerMeter).toInteger()
                
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

        return [gates: gates, lastlatitude: last_latitude, lastlongitude: last_longitude]
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
                    if (line_values.size() >= 4) {
                        
                        // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                        String latitude_str = line_values[1].trim()
                        String latitude_grad = latitude_str.substring(2,5)
                        BigDecimal latitude_grad_math = latitude_grad.toBigDecimal()
                        String latidude_minute = latitude_str.substring(6,14)
                        BigDecimal latidude_minute_math = latidude_minute.toBigDecimal()
                        boolean latitude_north = latitude_str.substring(0,1) == CoordPresentation.NORTH
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
                        boolean longitude_east = longitude_str.substring(0,1) == CoordPresentation.EAST
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
            int line_num = 0
            while (true) {
                String line = txt_reader.readLine()
                if (line == null) {
                    break
                }
                line_num++
                if (line && !line.startsWith(Defs.IGNORE_LINE)) {
                    valid_format = true
                    String line1 = line 
                    while (line1.contains('  ')) {
                        line1 = line1.replaceAll('  ', ' ')
                    }
                    def line_values = line1.split(',')
                    if (line_values.size() == 3) {
                        Map lat = [:]
                        Map lon = [:]
                        Map alt = [:]
                        boolean invalid = false
                        String s = ""
                        
                        s = line_values[0].trim()
                        lat = contestInstance.coordPresentation.GetDirectionGradDecimalMinute(s,true,true)
                        if (lat.invalid) {
                            invalid = true
                        }
                        s = line_values[1].trim()
                        lon = contestInstance.coordPresentation.GetDirectionGradDecimalMinute(s,true,false)
                        if (lon.invalid) {
                            invalid = true
                        }
                        s = line_values[2].trim()
                        alt = GetAltitude(s)
                        if (alt.invalid) {
                            invalid = true
                        }

                        if (!invalid) {
                            BigDecimal latitude = CoordPresentation.GetDecimalGrad(lat,true)
                            BigDecimal longitude = CoordPresentation.GetDecimalGrad(lon,false)
                            
                            def track = null
                            if (last_latitude != null && last_longitude != null) {
                                if ((latitude == last_latitude) && (longitude == last_longitude)) {
                                    track = last_track
                                } else {
                                    Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                                    track = leg.dir
                                }
                            }
                            
                            Map gate = [lat:lat, lon:lon, alt:alt.alt, track:track, next_track:null]
                            gates += gate
                        
                            last_latitude = latitude
                            last_longitude = longitude
                            last_track = track
                        } else {
                            if (read_errors) {
                                read_errors += ", "
                            }
                            read_errors += line_num.toString()
                        }
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
    private static Map GetAltitude(String altStr)
    {
        Map ret = [invalid:true, alt:0]
        
        if (altStr.startsWith(ALT) && (altStr.endsWith(UNIT_ft) || altStr.endsWith(UNIT_m))) {
            String s = altStr.substring(ALT.size()).trim()
            if (s.endsWith(UNIT_ft)) {
                s = s.substring(0,s.size()-UNIT_ft.size())
                if (s.isInteger()) {
                    ret.alt = s.toInteger()
                    ret.invalid = false
                }
            } else if (s.endsWith(UNIT_m)) {
                s = s.substring(0,s.size()-UNIT_m.size())
                if (s.isBigDecimal()) {
                    ret.alt = (s.toBigDecimal() * GpxService.ftPerMeter).toInteger()
                    ret.invalid = false
                }
            }
        }
        
        return ret
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
        int tp_num = 1
        int sc_num = 1
        
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
        
        // check pos (to_pos importParams.ildgpos importParams.curvedstartpos1 importParams.curvedendpos1 fp_pos ldg_pos)
        if (importParams.curved1) {
            if (!importParams.curvedstartpos1) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved1 leg missed."]
            }
            if (importParams.curvedstartpos1 <= 0) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved1 leg must be greater 0."]
            }
            if (!importParams.curvedendpos1) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved1 leg missed."]
            }
            if (importParams.curvedendpos1 <= 0) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved1 leg  must be greater 0."]
            }
            if (importParams.curvedstartpos1 + 1 >= importParams.curvedendpos1) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved1 leg  must be greater than start pos."]
            }
            if (to_pos + 2 > importParams.curvedstartpos1) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved1 leg must be located after SP."]
            }
            if (importParams.curvedendpos1 >= fp_pos) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved1 leg must be located before FP."]
            }
        }
        if (importParams.curved2) {
            if (!importParams.curvedstartpos2) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved2 leg missed."]
            }
            if (importParams.curvedstartpos2 <= 0) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved2 leg must be greater 0."]
            }
            if (!importParams.curvedendpos2) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved2 leg missed."]
            }
            if (importParams.curvedendpos2 <= 0) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved2 leg  must be greater 0."]
            }
            if (importParams.curvedstartpos2 + 1 >= importParams.curvedendpos2) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved2 leg  must be greater than start pos."]
            }
            if (to_pos + 2 > importParams.curvedstartpos2) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved2 leg must be located after SP."]
            }
            if (importParams.curvedendpos2 >= fp_pos) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved2 leg must be located before FP."]
            }
        }
        if (importParams.curved3) {
            if (!importParams.curvedstartpos3) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved3 leg missed."]
            }
            if (importParams.curvedstartpos3 <= 0) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved3 leg must be greater 0."]
            }
            if (!importParams.curvedendpos3) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved3 leg missed."]
            }
            if (importParams.curvedendpos3 <= 0) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved3 leg  must be greater 0."]
            }
            if (importParams.curvedstartpos3 + 1 >= importParams.curvedendpos3) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved3 leg  must be greater than start pos."]
            }
            if (to_pos + 2 > importParams.curvedstartpos3) {
                return [gatenum: coord_num, valid: true, errors: "Start pos of curved3 leg must be located after SP."]
            }
            if (importParams.curvedendpos3 >= fp_pos) {
                return [gatenum: coord_num, valid: true, errors: "End pos of curved3 leg must be located before FP."]
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
        if (importParams.curved1 && importParams.ildg) {
            if (importParams.ildgpos + 1 < importParams.curvedstartpos1) {
                // ok
            } else if (importParams.ildgpos > importParams.curvedendpos1 + 1) {
                // ok
            } else {
                return [gatenum: coord_num, valid: true, errors: "iFP/iLDG/iSP must not be located in curved1 leg."]
            }
        }
        if (importParams.curved2 && importParams.ildg) {
            if (importParams.ildgpos + 1 < importParams.curvedstartpos2) {
                // ok
            } else if (importParams.ildgpos > importParams.curvedendpos2 + 1) {
                // ok
            } else {
                return [gatenum: coord_num, valid: true, errors: "iFP/iLDG/iSP must not be located in curved2 leg."]
            }
        }
        if (importParams.curved3 && importParams.ildg) {
            if (importParams.ildgpos + 1 < importParams.curvedstartpos3) {
                // ok
            } else if (importParams.ildgpos > importParams.curvedendpos3 + 1) {
                // ok
            } else {
                return [gatenum: coord_num, valid: true, errors: "iFP/iLDG/iSP must not be located in curved3 leg."]
            }
        }

        // create route
        Route route_instance = new Route()
        route_instance.contest = contestInstance
        route_instance.idTitle = Route.countByContest(contestInstance) + 1
        route_instance.title = routeData.routename
        route_instance.turnpointRoute = contestInstance.turnpointRule.GetTurnpointRoute()
        route_instance.turnpointMapMeasurement = contestInstance.turnpointMapMeasurement
        route_instance.enroutePhotoMeasurement = contestInstance.enroutePhotoRule.GetEnrouteMeasurement()
        route_instance.enroutePhotoRoute = route_instance.enroutePhotoMeasurement.GetEnrouteRoute()
        route_instance.enrouteCanvasMeasurement = contestInstance.enrouteCanvasRule.GetEnrouteMeasurement()
        route_instance.enrouteCanvasRoute = route_instance.enrouteCanvasMeasurement.GetEnrouteRoute()
		route_instance.useProcedureTurns = contestInstance.useProcedureTurns
        route_instance.liveTrackingScorecard = contestInstance.liveTrackingScorecard
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
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
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
            // Curved (TP, SC..., TP) (1)
            } else if (importParams.curved1 && gate_pos == importParams.curvedstartpos1 ) {
                coordroute_instance.type = CoordType.TP
                coordroute_instance.titleNumber = tp_num
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                tp_num++
            } else if (importParams.curved1 && gate_pos == importParams.curvedendpos1) {
                coordroute_instance.type = CoordType.TP
                coordroute_instance.titleNumber = tp_num
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                coordroute_instance.noPlanningTest = true
                coordroute_instance.endCurved = true
                tp_num++
            } else if (importParams.curved1 && gate_pos > importParams.curvedstartpos1 && gate_pos < importParams.curvedendpos1) {
                coordroute_instance.type = CoordType.SECRET
                coordroute_instance.titleNumber = sc_num
                coordroute_instance.gatewidth2 = contestInstance.scGateWidth
                coordroute_instance.noTimeCheck = true
                coordroute_instance.noGateCheck = true
                coordroute_instance.noPlanningTest = true
                coordroute_instance.ignoreGate = true
                sc_num++
            // Curved (TP, SC..., TP) (2)
            } else if (importParams.curved2 && gate_pos == importParams.curvedstartpos2 ) {
                coordroute_instance.type = CoordType.TP
                coordroute_instance.titleNumber = tp_num
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                tp_num++
            } else if (importParams.curved2 && gate_pos == importParams.curvedendpos2) {
                coordroute_instance.type = CoordType.TP
                coordroute_instance.titleNumber = tp_num
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                coordroute_instance.noPlanningTest = true
                coordroute_instance.endCurved = true
                tp_num++
            } else if (importParams.curved2 && gate_pos > importParams.curvedstartpos2 && gate_pos < importParams.curvedendpos2) {
                coordroute_instance.type = CoordType.SECRET
                coordroute_instance.titleNumber = sc_num
                coordroute_instance.gatewidth2 = contestInstance.scGateWidth
                coordroute_instance.noTimeCheck = true
                coordroute_instance.noGateCheck = true
                coordroute_instance.noPlanningTest = true
                coordroute_instance.ignoreGate = true
                sc_num++
            // Curved (TP, SC..., TP) (3)
            } else if (importParams.curved3 && gate_pos == importParams.curvedstartpos3) {
                coordroute_instance.type = CoordType.TP
                coordroute_instance.titleNumber = tp_num
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                tp_num++
            } else if (importParams.curved3 && gate_pos == importParams.curvedendpos3) {
                coordroute_instance.type = CoordType.TP
                coordroute_instance.titleNumber = tp_num
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                coordroute_instance.noPlanningTest = true
                coordroute_instance.endCurved = true
                tp_num++
            } else if (importParams.curved3 && gate_pos > importParams.curvedstartpos3 && gate_pos < importParams.curvedendpos3) {
                coordroute_instance.type = CoordType.SECRET
                coordroute_instance.titleNumber = sc_num
                coordroute_instance.gatewidth2 = contestInstance.scGateWidth
                coordroute_instance.noTimeCheck = true
                coordroute_instance.noGateCheck = true
                coordroute_instance.noPlanningTest = true
                coordroute_instance.ignoreGate = true
                sc_num++
            // Semicircle (SC) (1)
            } else if (importParams.semicircle1 && gate_pos == importParams.semicirclepos1) {
                coordroute_instance.type = CoordType.SECRET
                coordroute_instance.titleNumber = sc_num
                coordroute_instance.gatewidth2 = contestInstance.scGateWidth
                coordroute_instance.noTimeCheck = true
                coordroute_instance.noGateCheck = true
                coordroute_instance.noPlanningTest = true
				coordroute_instance.circleCenter = true
                sc_num++
            // Semicircle (SC) (2)
            } else if (importParams.semicircle2 && gate_pos == importParams.semicirclepos2) {
                coordroute_instance.type = CoordType.SECRET
                coordroute_instance.titleNumber = sc_num
                coordroute_instance.gatewidth2 = contestInstance.scGateWidth
                coordroute_instance.noTimeCheck = true
                coordroute_instance.noGateCheck = true
                coordroute_instance.noPlanningTest = true
				coordroute_instance.circleCenter = true
                sc_num++
            // Semicircle (SC) (3)
            } else if (importParams.semicircle3 && gate_pos == importParams.semicirclepos3) {
                coordroute_instance.type = CoordType.SECRET
                coordroute_instance.titleNumber = sc_num
                coordroute_instance.gatewidth2 = contestInstance.scGateWidth
                coordroute_instance.noTimeCheck = true
                coordroute_instance.noGateCheck = true
                coordroute_instance.noPlanningTest = true
				coordroute_instance.circleCenter = true
                sc_num++
            // SC
            } else if (track_diff != null && track_diff < importParams.secretcoursechange) {
                coordroute_instance.type = CoordType.SECRET
                coordroute_instance.titleNumber = sc_num
                coordroute_instance.gatewidth2 = contestInstance.scGateWidth
                sc_num++
            // TP
            } else {
                coordroute_instance.type = CoordType.TP
                coordroute_instance.titleNumber = tp_num
                coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                tp_num++
            }
            
            coordroute_instance.latDirection = gate.lat.direction
            coordroute_instance.latGrad = gate.lat.grad
            coordroute_instance.latMinute = gate.lat.minute
            
            coordroute_instance.lonDirection = gate.lon.direction
            coordroute_instance.lonGrad = gate.lon.grad
            coordroute_instance.lonMinute = gate.lon.minute
            
            coordroute_instance.altitude = gate.alt
            
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
            case KML_EXTENSION:
                return ReadFCKMLFile(contestInstance, routeFileName, false)
            case KMZ_EXTENSION:
                return ReadFCKMLFile(contestInstance, routeFileName, true)
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
                String new_title2 = ""
                for (rte in gpx.rte) {
                    if (!new_title2 && rte.extensions.flightcontest.route) {
                        String new_title = rte.name[0].text()
                        new_title2 = new_title
                        if (new_title2) {
                            int found_num = 1
                            while (contestInstance.RouteTitleFound(new_title2)) {
                                found_num++
                                new_title2 = "${new_title} (${found_num})"
                            }
                        }
                    }
                    if (rte.extensions.flightcontest.gate) {
                        valid_format = true
                        gate_num++
                        if (first) {
                            route_instance = new Route()
                            route_instance.contest = contestInstance
                            route_instance.idTitle = Route.countByContest(contestInstance) + 1
                            route_instance.title = new_title2
                            route_instance.turnpointRoute = contestInstance.turnpointRule.GetTurnpointRoute()
                            route_instance.turnpointMapMeasurement = contestInstance.turnpointMapMeasurement
                            if (route_instance.turnpointRoute == TurnpointRoute.Unassigned) {
                                route_instance.turnpointRoute = TurnpointRoute.None
                                route_instance.turnpointMapMeasurement = false
                            }
                            route_instance.enroutePhotoRoute = EnrouteRoute.None
                            route_instance.enrouteCanvasRoute = EnrouteRoute.None
                            route_instance.enroutePhotoMeasurement = contestInstance.enroutePhotoRule.GetEnrouteMeasurement()
                            if (route_instance.enroutePhotoMeasurement == EnrouteMeasurement.Unassigned) {
                                route_instance.enroutePhotoMeasurement = EnrouteMeasurement.None
                            }
                            route_instance.enrouteCanvasMeasurement = contestInstance.enrouteCanvasRule.GetEnrouteMeasurement()
                            if (route_instance.enrouteCanvasMeasurement == EnrouteMeasurement.Unassigned) {
                                route_instance.enrouteCanvasMeasurement = EnrouteMeasurement.None
                            }
                            route_instance.liveTrackingScorecard = contestInstance.liveTrackingScorecard
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
                        String minalt = rte.extensions.flightcontest.gate.'@minalt'[0]
                        if (minalt) {
                            coordroute_instance.minAltitudeAboveGround = minalt.toInteger()
                        }
                        String maxalt = rte.extensions.flightcontest.gate.'@maxalt'[0]
                        if (maxalt) {
                            coordroute_instance.maxAltitudeAboveGround = maxalt.toInteger()
                        }
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
                        coordroute_instance.circleCenter = rte.extensions.flightcontest.gate.'@circlecenter'[0] == "yes"
						coordroute_instance.semiCircleInvert = rte.extensions.flightcontest.gate.'@invert'[0] == "yes"
						coordroute_instance.ignoreGate = rte.extensions.flightcontest.gate.'@ignoregate'[0] == "yes"
                        
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
                        String assignedsign = rte.extensions.flightcontest.gate.'@assignedsign'[0] // Version 3
                        if (assignedsign) {
                            coordroute_instance.assignedSign = TurnpointSign.(assignedsign.toString())
                        }
                        String correctsign = rte.extensions.flightcontest.gate.'@correctsign'[0] // Version 3
                        if (correctsign) {
                            coordroute_instance.correctSign = TurnpointCorrect.(correctsign.toString())
                        }
                        if (rte.extensions.flightcontest.photoimage) {
                            String observationpositiontop = rte.extensions.flightcontest.photoimage.'@observationpositiontop'[0]
                            if (observationpositiontop) {
                                coordroute_instance.observationPositionTop = observationpositiontop.toInteger()
                            }
                            String observationpositionleft = rte.extensions.flightcontest.photoimage.'@observationpositionleft'[0]
                            if (observationpositionleft) {
                                coordroute_instance.observationPositionLeft = observationpositionleft.toInteger()
                            }
                            String observationnextprintpageturnpoint = rte.extensions.flightcontest.photoimage.'@observationnextprintpageturnpoint'[0]
                            if (observationnextprintpageturnpoint) {
                                coordroute_instance.observationNextPrintPage = observationnextprintpageturnpoint == "yes"
                            }
                            String observationnextprintpageenroute = rte.extensions.flightcontest.photoimage.'@observationnextprintpageenroute'[0]
                            if (observationnextprintpageenroute) {
                                coordroute_instance.observationNextPrintPageEnroute = observationnextprintpageenroute == "yes"
                            }
                        }
                        if (!coordroute_instance.save()) {
                            read_errors = "Could not save ${coordroute_instance.title()}"
                        }
                        if (rte.extensions.flightcontest.photoimage && rte.extensions.flightcontest.photoimage.imagedata) {
                            byte[] image_data = Base64.getDecoder().decode(rte.extensions.flightcontest.photoimage.imagedata[0].text())
                            if (image_data) {
                                ImageCoord imagecoord_instance = new ImageCoord(imageData:image_data, coord:coordroute_instance)
                                imagecoord_instance.save()
                                coordroute_instance.imagecoord = imagecoord_instance
                            }
                        }
                    }
                    if (read_errors) {
                        break
                    }
                }
            }
            
            // Observations (Version 3)
            if (route_instance) {
                boolean save_route = false
                if (gpx.extensions.flightcontest.observationsettings) {
                    String turnpoint = gpx.extensions.flightcontest.observationsettings.'@turnpoint'[0]
                    if (turnpoint) {
                        route_instance.turnpointRoute = TurnpointRoute.(turnpoint.toString())
                        save_route = true
                    }
                    String turnpointmapmeasurement = gpx.extensions.flightcontest.observationsettings.'@turnpointmapmeasurement'[0]
                    if (turnpointmapmeasurement) {
                        route_instance.turnpointMapMeasurement = turnpointmapmeasurement == "yes"
                        save_route = true
                    }
                    String turnpointprintstyle = gpx.extensions.flightcontest.observationsettings.'@turnpointprintstyle'[0]
                    if (turnpointprintstyle) {
                        route_instance.turnpointPrintStyle = ObservationPrintStyle.(turnpointprintstyle.toString())
                        save_route = true
                    }
                    String turnpointprintpositionmaker = gpx.extensions.flightcontest.observationsettings.'@turnpointprintpositionmaker'[0]
                    if (turnpointprintpositionmaker) {
                        route_instance.turnpointPrintPositionMaker = turnpointprintpositionmaker == "yes"
                        save_route = true
                    }
                    String enroutephoto = gpx.extensions.flightcontest.observationsettings.'@enroutephoto'[0]
                    if (enroutephoto) {
                        route_instance.enroutePhotoRoute = EnrouteRoute.(enroutephoto.toString())
                        save_route = true
                    }
                    String enroutephotomeasurement = gpx.extensions.flightcontest.observationsettings.'@enroutephotomeasurement'[0]
                    if (enroutephotomeasurement) {
                        route_instance.enroutePhotoMeasurement = EnrouteMeasurement.(enroutephotomeasurement.toString())
                        save_route = true
                    }
                    String enroutephotoprintstyle = gpx.extensions.flightcontest.observationsettings.'@enroutephotoprintstyle'[0]
                    if (enroutephotoprintstyle) {
                        route_instance.enroutePhotoPrintStyle = ObservationPrintStyle.(enroutephotoprintstyle.toString())
                        save_route = true
                    }
                    String enroutephotoprintpositionmarker = gpx.extensions.flightcontest.observationsettings.'@enroutephotoprintpositionmarker'[0]
                    if (enroutephotoprintpositionmarker) {
                        route_instance.enroutePhotoPrintPositionMaker = enroutephotoprintpositionmarker == "yes"
                        save_route = true
                    }
                    String enroutecanvas = gpx.extensions.flightcontest.observationsettings.'@enroutecanvas'[0]
                    if (enroutecanvas) {
                        route_instance.enrouteCanvasRoute = EnrouteRoute.(enroutecanvas.toString())
                        save_route = true
                    }
                    String enroutecanvasmeasurement = gpx.extensions.flightcontest.observationsettings.'@enroutecanvasmeasurement'[0]
                    if (enroutecanvasmeasurement) {
                        route_instance.enrouteCanvasMeasurement = EnrouteMeasurement.(enroutecanvasmeasurement.toString())
                        save_route = true
                    }
                    String useprocedureturn = gpx.extensions.flightcontest.observationsettings.'@useprocedureturn'[0]
                    if (useprocedureturn) {
                        route_instance.useProcedureTurns = useprocedureturn == "yes"
                        save_route = true
                    }
                    String mapscale = gpx.extensions.flightcontest.observationsettings.'@mapscale'[0]
                    if (mapscale) {
                        route_instance.mapScale = mapscale.toInteger()
                        save_route = true
                    }
					route_instance.altitudeAboveGround = 0
                    String altitudeaboveground = gpx.extensions.flightcontest.observationsettings.'@altitudeaboveground'[0]
                    if (altitudeaboveground) {
                        route_instance.altitudeAboveGround = altitudeaboveground.toInteger()
                        save_route = true
                    }
                }
                if (gpx.extensions.flightcontest.mapsettings) {
                    String contestmapairfields = gpx.extensions.flightcontest.mapsettings.'@contestmapairfields'[0]
                    if (contestmapairfields) {
                        route_instance.contestMapAirfields = contestmapairfields
                        save_route = true
                    }
                    String contestmapcircle = gpx.extensions.flightcontest.mapsettings.'@contestmapcircle'[0]
                    if (contestmapcircle) {
                        route_instance.contestMapCircle = contestmapcircle == "yes"
                        save_route = true
                    }
                    String contestmapprocedureturn = gpx.extensions.flightcontest.mapsettings.'@contestmapprocedureturn'[0]
                    if (contestmapprocedureturn) {
                        route_instance.contestMapProcedureTurn = contestmapprocedureturn == "yes"
                        save_route = true
                    }
                    String contestmapleg = gpx.extensions.flightcontest.mapsettings.'@contestmapleg'[0]
                    if (contestmapleg) {
                        route_instance.contestMapLeg = contestmapleg == "yes"
                        save_route = true
                    }
                    String contestmapcurvedleg = gpx.extensions.flightcontest.mapsettings.'@contestmapcurvedleg'[0]
                    if (contestmapcurvedleg) {
                        route_instance.contestMapCurvedLeg = contestmapcurvedleg == "yes"
                        save_route = true
                    }
                    String contestmapcurvedlegpoints = gpx.extensions.flightcontest.mapsettings.'@contestmapcurvedlegpoints'[0]
                    if (contestmapcurvedlegpoints) {
                        route_instance.contestMapCurvedLegPoints = contestmapcurvedlegpoints
                        save_route = true
                    }
                    String contestmaptpname = gpx.extensions.flightcontest.mapsettings.'@contestmaptpname'[0]
                    if (contestmaptpname) {
                        route_instance.contestMapTpName = contestmaptpname == "yes"
                        save_route = true
                    }
                    String contestmapsecretgates = gpx.extensions.flightcontest.mapsettings.'@contestmapsecretgates'[0]
                    if (contestmapsecretgates) {
                        route_instance.contestMapSecretGates = contestmapsecretgates == "yes"
                        save_route = true
                    }
                    String contestmapenroutephotos = gpx.extensions.flightcontest.mapsettings.'@contestmapenroutephotos'[0]
                    if (contestmapenroutephotos) {
                        route_instance.contestMapEnroutePhotos = contestmapenroutephotos == "yes"
                        save_route = true
                    }
                    String contestmapenroutecanvas = gpx.extensions.flightcontest.mapsettings.'@contestmapenroutecanvas'[0]
                    if (contestmapenroutecanvas) {
                        route_instance.contestMapEnrouteCanvas = contestmapenroutecanvas == "yes"
                        save_route = true
                    }
                    String contestmapgraticule = gpx.extensions.flightcontest.mapsettings.'@contestmapgraticule'[0]
                    if (contestmapgraticule) {
                        route_instance.contestMapGraticule = contestmapgraticule == "yes"
                        save_route = true
                    }
                    String contestmapcontourlines = gpx.extensions.flightcontest.mapsettings.'@contestmapcontourlines'[0]
                    if (contestmapcontourlines) {
                        route_instance.contestMapContourLines = contestmapcontourlines.toInteger()
                        save_route = true
                    }
                    String contestmapmunicipalitynames = gpx.extensions.flightcontest.mapsettings.'@contestmapmunicipalitynames'[0]
                    if (contestmapmunicipalitynames) {
                        route_instance.contestMapMunicipalityNames = contestmapmunicipalitynames == "yes"
                        save_route = true
                    }
                    String contestmapchurches = gpx.extensions.flightcontest.mapsettings.'@contestmapchurches'[0]
                    if (contestmapchurches) {
                        route_instance.contestMapChurches = contestmapchurches == "yes"
                        save_route = true
                    }
                    String contestmapcastles = gpx.extensions.flightcontest.mapsettings.'@contestmapcastles'[0]
                    if (contestmapcastles) {
                        route_instance.contestMapCastles = contestmapcastles == "yes"
                        save_route = true
                    }
                    String contestmapchateaus = gpx.extensions.flightcontest.mapsettings.'@contestmapchateaus'[0]
                    if (contestmapchateaus) {
                        route_instance.contestMapChateaus = contestmapchateaus == "yes"
                        save_route = true
                    }
                    String contestmappowerlines = gpx.extensions.flightcontest.mapsettings.'@contestmappowerlines'[0]
                    if (contestmappowerlines) {
                        route_instance.contestMapPowerlines = contestmappowerlines == "yes"
                        save_route = true
                    }
                    String contestmapwindpowerstations = gpx.extensions.flightcontest.mapsettings.'@contestmapwindpowerstations'[0]
                    if (contestmapwindpowerstations) {
                        route_instance.contestMapWindpowerstations = contestmapwindpowerstations == "yes"
                        save_route = true
                    }
                    String contestmapsmallroads = gpx.extensions.flightcontest.mapsettings.'@contestmapsmallroads'[0]
                    if (contestmapsmallroads) {
                        route_instance.contestMapSmallRoads = contestmapsmallroads == "yes"
                        save_route = true
                    }
                    String contestmappeaks = gpx.extensions.flightcontest.mapsettings.'@contestmappeaks'[0]
                    if (contestmappeaks) {
                        route_instance.contestMapPeaks = contestmappeaks == "yes"
                        save_route = true
                    }
                    String contestmapdropshadow = gpx.extensions.flightcontest.mapsettings.'@contestmapdropshadow'[0]
                    if (contestmapdropshadow) {
                        route_instance.contestMapDropShadow = contestmapdropshadow == "yes"
                        save_route = true
                    }
                    String contestmapadditionals = gpx.extensions.flightcontest.mapsettings.'@contestmapadditionals'[0]
                    if (contestmapadditionals) {
                        route_instance.contestMapAdditionals = contestmapadditionals == "yes"
                        save_route = true
                    }
                    String contestmapspecials = gpx.extensions.flightcontest.mapsettings.'@contestmapspecials'[0]
                    if (contestmapspecials) {
                        route_instance.contestMapSpecials = contestmapspecials == "yes"
                        save_route = true
                    }
                    String contestmapairspaces = gpx.extensions.flightcontest.mapsettings.'@contestmapairspaces'[0]
                    if (contestmapairspaces) {
                        route_instance.contestMapAirspaces = contestmapairspaces == "yes"
                        save_route = true
                    }
                    String contestmapairspaceslayer = gpx.extensions.flightcontest.mapsettings.'@contestmapairspaceslayer'[0]
                    if (contestmapairspaceslayer) {
                        route_instance.contestMapAirspacesLayer2 = contestmapairspaceslayer.replace(',','\n')
                        save_route = true
                    }
                    String contestmapairspaceslayer2 = gpx.extensions.flightcontest.mapsettings.'@contestmapairspaceslayer2'[0]
                    if (contestmapairspaceslayer2) {
                        route_instance.contestMapAirspacesLayer2 = contestmapairspaceslayer2
                        save_route = true
                    }
                    String contestmapairspaceslowerlimit = gpx.extensions.flightcontest.mapsettings.'@contestmapairspaceslowerlimit'[0]
                    if (contestmapairspaceslowerlimit) {
                        route_instance.contestMapAirspacesLowerLimit = contestmapairspaceslowerlimit.toInteger()
                        save_route = true
                    }
                    String contestmapshowoptions1 = gpx.extensions.flightcontest.mapsettings.'@contestmapshowoptions1'[0]
                    if (contestmapshowoptions1) {
                        route_instance.contestMapShowFirstOptions = contestmapshowoptions1 == "yes"
                        save_route = true
                    }
                    String contestmaptitle1 = gpx.extensions.flightcontest.mapsettings.'@contestmaptitle1'[0]
                    if (contestmaptitle1) {
                        route_instance.contestMapFirstTitle = contestmaptitle1
                        save_route = true
                    }
                    String contestmapcenterverticalpos1 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterverticalpos1'[0]
                    if (contestmapcenterverticalpos1) {
                        route_instance.contestMapCenterVerticalPos = VerticalPos.(contestmapcenterverticalpos1.toString())
                        save_route = true
                    }
                    String contestmapcenterhorizontalpos1 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterhorizontalpos1'[0]
                    if (contestmapcenterhorizontalpos1) {
                        route_instance.contestMapCenterHorizontalPos = HorizontalPos.(contestmapcenterhorizontalpos1.toString())
                        save_route = true
                    }
                    String contestmapcenterpoints1 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterpoints1'[0]
                    if (contestmapcenterpoints1) {
                        route_instance.contestMapCenterPoints = contestmapcenterpoints1
                        save_route = true
                    }
                    String contestmapprintpoints1 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintpoints1'[0]
                    if (contestmapprintpoints1) {
                        route_instance.contestMapPrintPoints = contestmapprintpoints1
                        save_route = true
                    }
                    String contestmapprintlandscape1 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintlandscape1'[0]
                    if (contestmapprintlandscape1) {
                        route_instance.contestMapPrintLandscape = contestmapprintlandscape1 == "yes"
                        save_route = true
                    }
                    String contestmapprintsize1 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintsize1'[0]
                    if (contestmapprintsize1) {
                        route_instance.contestMapPrintSize = contestmapprintsize1
                        save_route = true
                    }
                    String contestmapcentermovex1 = gpx.extensions.flightcontest.mapsettings.'@contestmapcentermovex1'[0]
                    if (contestmapcentermovex1) {
                        route_instance.contestMapCenterMoveX = contestmapcentermovex1.toBigDecimal()
                        save_route = true
                    }
                    String contestmapcentermovey1 = gpx.extensions.flightcontest.mapsettings.'@contestmapcentermovey1'[0]
                    if (contestmapcentermovey1) {
                        route_instance.contestMapCenterMoveY = contestmapcentermovey1.toBigDecimal()
                        save_route = true
                    }
                    String contestmapshowoptions2 = gpx.extensions.flightcontest.mapsettings.'@contestmapshowoptions2'[0]
                    if (contestmapshowoptions2) {
                        route_instance.contestMapShowSecondOptions = contestmapshowoptions2 == "yes"
                        save_route = true
                    }
                    String contestmaptitle2 = gpx.extensions.flightcontest.mapsettings.'@contestmaptitle2'[0]
                    if (contestmaptitle2) {
                        route_instance.contestMapSecondTitle = contestmaptitle2
                        save_route = true
                    }
                    String contestmapcenterverticalpos2 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterverticalpos2'[0]
                    if (contestmapcenterverticalpos2) {
                        route_instance.contestMapCenterVerticalPos2 = VerticalPos.(contestmapcenterverticalpos2.toString())
                        save_route = true
                    }
                    String contestmapcenterhorizontalpos2 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterhorizontalpos2'[0]
                    if (contestmapcenterhorizontalpos2) {
                        route_instance.contestMapCenterHorizontalPos2 = HorizontalPos.(contestmapcenterhorizontalpos2.toString())
                        save_route = true
                    }
                    String contestmapcenterpoints2 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterpoints2'[0]
                    if (contestmapcenterpoints2) {
                        route_instance.contestMapCenterPoints2 = contestmapcenterpoints2
                        save_route = true
                    }
                    String contestmapprintpoints2 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintpoints2'[0]
                    if (contestmapprintpoints2) {
                        route_instance.contestMapPrintPoints2 = contestmapprintpoints2
                        save_route = true
                    }
                    String contestmapprintlandscape2 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintlandscape2'[0]
                    if (contestmapprintlandscape2) {
                        route_instance.contestMapPrintLandscape2 = contestmapprintlandscape2 == "yes"
                        save_route = true
                    }
                    String contestmapprintsize2 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintsize2'[0]
                    if (contestmapprintsize2) {
                        route_instance.contestMapPrintSize2 = contestmapprintsize2
                        save_route = true
                    }
                    String contestmapcentermovex2 = gpx.extensions.flightcontest.mapsettings.'@contestmapcentermovex2'[0]
                    if (contestmapcentermovex2) {
                        route_instance.contestMapCenterMoveX2 = contestmapcentermovex2.toBigDecimal()
                        save_route = true
                    }
                    String contestmapcentermovey2 = gpx.extensions.flightcontest.mapsettings.'@contestmapcentermovey2'[0]
                    if (contestmapcentermovey2) {
                        route_instance.contestMapCenterMoveY2 = contestmapcentermovey2.toBigDecimal()
                        save_route = true
                    }
                    String contestmapshowoptions3 = gpx.extensions.flightcontest.mapsettings.'@contestmapshowoptions3'[0]
                    if (contestmapshowoptions3) {
                        route_instance.contestMapShowThirdOptions = contestmapshowoptions3 == "yes"
                        save_route = true
                    }
                    String contestmaptitle3 = gpx.extensions.flightcontest.mapsettings.'@contestmaptitle3'[0]
                    if (contestmaptitle3) {
                        route_instance.contestMapThirdTitle = contestmaptitle3
                        save_route = true
                    }
                    String contestmapcenterverticalpos3 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterverticalpos3'[0]
                    if (contestmapcenterverticalpos3) {
                        route_instance.contestMapCenterVerticalPos3 = VerticalPos.(contestmapcenterverticalpos3.toString())
                        save_route = true
                    }
                    String contestmapcenterhorizontalpos3 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterhorizontalpos3'[0]
                    if (contestmapcenterhorizontalpos3) {
                        route_instance.contestMapCenterHorizontalPos3 = HorizontalPos.(contestmapcenterhorizontalpos3.toString())
                        save_route = true
                    }
                    String contestmapcenterpoints3 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterpoints3'[0]
                    if (contestmapcenterpoints3) {
                        route_instance.contestMapCenterPoints3 = contestmapcenterpoints3
                        save_route = true
                    }
                    String contestmapprintpoints3 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintpoints3'[0]
                    if (contestmapprintpoints3) {
                        route_instance.contestMapPrintPoints3 = contestmapprintpoints3
                        save_route = true
                    }
                    String contestmapprintlandscape3 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintlandscape3'[0]
                    if (contestmapprintlandscape3) {
                        route_instance.contestMapPrintLandscape3 = contestmapprintlandscape3 == "yes"
                        save_route = true
                    }
                    String contestmapprintsize3 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintsize3'[0]
                    if (contestmapprintsize3) {
                        route_instance.contestMapPrintSize3 = contestmapprintsize3
                        save_route = true
                    }
                    String contestmapcentermovex3 = gpx.extensions.flightcontest.mapsettings.'@contestmapcentermovex3'[0]
                    if (contestmapcentermovex3) {
                        route_instance.contestMapCenterMoveX3 = contestmapcentermovex3.toBigDecimal()
                        save_route = true
                    }
                    String contestmapcentermovey3 = gpx.extensions.flightcontest.mapsettings.'@contestmapcentermovey3'[0]
                    if (contestmapcentermovey3) {
                        route_instance.contestMapCenterMoveY3 = contestmapcentermovey3.toBigDecimal()
                        save_route = true
                    }
                    String contestmapshowoptions4 = gpx.extensions.flightcontest.mapsettings.'@contestmapshowoptions4'[0]
                    if (contestmapshowoptions4) {
                        route_instance.contestMapShowForthOptions = contestmapshowoptions4 == "yes"
                        save_route = true
                    }
                    String contestmaptitle4 = gpx.extensions.flightcontest.mapsettings.'@contestmaptitle4'[0]
                    if (contestmaptitle4) {
                        route_instance.contestMapForthTitle = contestmaptitle4
                        save_route = true
                    }
                    String contestmapcenterverticalpos4 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterverticalpos4'[0]
                    if (contestmapcenterverticalpos4) {
                        route_instance.contestMapCenterVerticalPos4 = VerticalPos.(contestmapcenterverticalpos4.toString())
                        save_route = true
                    }
                    String contestmapcenterhorizontalpos4 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterhorizontalpos4'[0]
                    if (contestmapcenterhorizontalpos4) {
                        route_instance.contestMapCenterHorizontalPos4 = HorizontalPos.(contestmapcenterhorizontalpos4.toString())
                        save_route = true
                    }
                    String contestmapcenterpoints4 = gpx.extensions.flightcontest.mapsettings.'@contestmapcenterpoints4'[0]
                    if (contestmapcenterpoints4) {
                        route_instance.contestMapCenterPoints4 = contestmapcenterpoints4
                        save_route = true
                    }
                    String contestmapprintpoints4 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintpoints4'[0]
                    if (contestmapprintpoints4) {
                        route_instance.contestMapPrintPoints4 = contestmapprintpoints4
                        save_route = true
                    }
                    String contestmapprintlandscape4 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintlandscape4'[0]
                    if (contestmapprintlandscape4) {
                        route_instance.contestMapPrintLandscape4 = contestmapprintlandscape4 == "yes"
                        save_route = true
                    }
                    String contestmapprintsize4 = gpx.extensions.flightcontest.mapsettings.'@contestmapprintsize4'[0]
                    if (contestmapprintsize4) {
                        route_instance.contestMapPrintSize4 = contestmapprintsize4
                        save_route = true
                    }
                    String contestmapcentermovex4 = gpx.extensions.flightcontest.mapsettings.'@contestmapcentermovex4'[0]
                    if (contestmapcentermovex4) {
                        route_instance.contestMapCenterMoveX4 = contestmapcentermovex4.toBigDecimal()
                        save_route = true
                    }
                    String contestmapcentermovey4 = gpx.extensions.flightcontest.mapsettings.'@contestmapcentermovey4'[0]
                    if (contestmapcentermovey4) {
                        route_instance.contestMapCenterMoveY4 = contestmapcentermovey4.toBigDecimal()
                        save_route = true
                    }
                }
                if (save_route) {
                    route_instance.save()
                }
                if (gpx.extensions.flightcontest.enroutephotosigns) {
                    for (enroutephotosign in gpx.extensions.flightcontest.enroutephotosigns.enroutephotosign) {
                        CoordEnroutePhoto coordenroutephoto_instance = new CoordEnroutePhoto()
                        coordenroutephoto_instance.route = route_instance
                        coordenroutephoto_instance.enroutePhotoName = enroutephotosign.'@photoname'
                        coordenroutephoto_instance.enrouteViewPos = enroutephotosign.'@viewpos'.toInteger()
                        coordenroutephoto_instance.save()
                    }
                }
                if (gpx.extensions.flightcontest.enroutecanvassigns) {
                    for (enroutecanvassign in gpx.extensions.flightcontest.enroutecanvassigns.enroutecanvassign) {
                        CoordEnrouteCanvas coordenroutecanvas_instance = new CoordEnrouteCanvas()
                        coordenroutecanvas_instance.route = route_instance
                        coordenroutecanvas_instance.enrouteCanvasSign = EnrouteCanvasSign.(enroutecanvassign.'@canvasname')
                        coordenroutecanvas_instance.enrouteViewPos = enroutecanvassign.'@viewpos'.toInteger()
                        coordenroutecanvas_instance.save()
                    }
                }
                if (gpx.wpt.size() > 0) {
                    for (wpt in gpx.wpt) {
                        if (wpt.extensions.flightcontest.enroutephotosign) {
                            CoordEnroutePhoto coordenroutephoto_instance = new CoordEnroutePhoto()
                            coordenroutephoto_instance.route = route_instance
                            
                            coordenroutephoto_instance.enroutePhotoName = wpt.extensions.flightcontest.enroutephotosign.'@photoname'[0]
                            coordenroutephoto_instance.enrouteViewPos = wpt.extensions.flightcontest.enroutephotosign.'@viewpos'[0].toInteger()
                            
                            Map lat = CoordPresentation.GetDirectionGradDecimalMinute(wpt.'@lat'.toBigDecimal(), true)
                            coordenroutephoto_instance.latDirection = lat.direction
                            coordenroutephoto_instance.latGrad = lat.grad
                            coordenroutephoto_instance.latMinute = lat.minute
                            
                            Map lon = CoordPresentation.GetDirectionGradDecimalMinute(wpt.'@lon'.toBigDecimal(), false)
                            coordenroutephoto_instance.lonDirection = lon.direction
                            coordenroutephoto_instance.lonGrad = lon.grad
                            coordenroutephoto_instance.lonMinute = lon.minute
                            
                            coordenroutephoto_instance.type = CoordType.(wpt.extensions.flightcontest.enroutephotosign.'@type'[0])
                            coordenroutephoto_instance.titleNumber = wpt.extensions.flightcontest.enroutephotosign.'@number'[0].toInteger()
                            
                            String dist = wpt.extensions.flightcontest.enroutephotosign.'@dist'[0]
                            if (dist) {
                                coordenroutephoto_instance.enrouteDistance = dist.toBigDecimal()
                            }
                            String measuredist = wpt.extensions.flightcontest.enroutephotosign.'@measuredist'[0]
                            if (measuredist) {
                                coordenroutephoto_instance.measureDistance = measuredist.toBigDecimal()
                            }
                            String orthogonaldist = wpt.extensions.flightcontest.enroutephotosign.'@orthogonaldist'[0]
                            if (orthogonaldist) {
                                coordenroutephoto_instance.enrouteOrthogonalDistance = orthogonaldist.toInteger()
                            }
                            
                            if (wpt.extensions.flightcontest.enroutephotoimage) {
                                String observationpositiontop = wpt.extensions.flightcontest.enroutephotoimage.'@observationpositiontop'[0]
                                if (observationpositiontop) {
                                    coordenroutephoto_instance.observationPositionTop = observationpositiontop.toInteger()
                                }
                                String observationpositionleft = wpt.extensions.flightcontest.enroutephotoimage.'@observationpositionleft'[0]
                                if (observationpositionleft) {
                                    coordenroutephoto_instance.observationPositionLeft = observationpositionleft.toInteger()
                                }
                            }
                            
                            //coordenroutephoto_instance.calculateCoordEnrouteValues(coordenroutephoto_instance.route.enroutePhotoRoute)
                            coordenroutephoto_instance.save()
                            
                            if (wpt.extensions.flightcontest.enroutephotoimage && wpt.extensions.flightcontest.enroutephotoimage.imagedata) {
                                byte[] image_data = Base64.getDecoder().decode(wpt.extensions.flightcontest.enroutephotoimage.imagedata[0].text())
                                if (image_data) {
                                    ImageCoord imagecoord_instance = new ImageCoord(imageData:image_data, coord:coordenroutephoto_instance)
                                    imagecoord_instance.save()
                                    coordenroutephoto_instance.imagecoord = imagecoord_instance
                                }
                            }
                        }
                        if (wpt.extensions.flightcontest.enroutecanvassign) {
							EnrouteCanvasSign enroute_canvassign = EnrouteCanvasSign.(wpt.extensions.flightcontest.enroutecanvassign.'@canvasname'[0])
							
                            CoordEnrouteCanvas coordenroutecanvas_instance = new CoordEnrouteCanvas()
                            coordenroutecanvas_instance.route = route_instance
                            
                            coordenroutecanvas_instance.enrouteCanvasSign = enroute_canvassign
                            coordenroutecanvas_instance.enrouteViewPos = wpt.extensions.flightcontest.enroutecanvassign.'@viewpos'[0].toInteger()
                            
							Map lat = CoordPresentation.GetDirectionGradDecimalMinute(wpt.'@lat'.toBigDecimal(), true)
							coordenroutecanvas_instance.latDirection = lat.direction
							coordenroutecanvas_instance.latGrad = lat.grad
							coordenroutecanvas_instance.latMinute = lat.minute
							
							Map lon = CoordPresentation.GetDirectionGradDecimalMinute(wpt.'@lon'.toBigDecimal(), false)
							coordenroutecanvas_instance.lonDirection = lon.direction
							coordenroutecanvas_instance.lonGrad = lon.grad
							coordenroutecanvas_instance.lonMinute = lon.minute

                            coordenroutecanvas_instance.type = CoordType.(wpt.extensions.flightcontest.enroutecanvassign.'@type'[0])
                            coordenroutecanvas_instance.titleNumber = wpt.extensions.flightcontest.enroutecanvassign.'@number'[0].toInteger()
                            
                            String dist = wpt.extensions.flightcontest.enroutecanvassign.'@dist'[0]
                            if (dist) {
                                coordenroutecanvas_instance.enrouteDistance = dist.toBigDecimal()
                            }
                            String measuredist = wpt.extensions.flightcontest.enroutecanvassign.'@measuredist'[0]
                            if (measuredist) { 
                                coordenroutecanvas_instance.measureDistance = measuredist.toBigDecimal()
                            }
                            String orthogonaldist = wpt.extensions.flightcontest.enroutecanvassign.'@orthogonaldist'[0]
                            if (orthogonaldist) {
                                coordenroutecanvas_instance.enrouteOrthogonalDistance = orthogonaldist.toInteger()
                            }
                            
                            //coordenroutecanvas_instance.calculateCoordEnrouteValues(coordenroutecanvas_instance.route.enrouteCanvasRoute)
                            coordenroutecanvas_instance.save()
                        }
                    }
                }
            }
        } catch (Exception e) {
            read_errors += e.getMessage()
        }
        gpx_reader.close()
        
        return [gatenum: gate_num, valid: valid_format, errors: read_errors, route: route_instance]
    }       
     
    //--------------------------------------------------------------------------
    private static Map ReadFCKMLFile(Contest contestInstance, String kmFileName, boolean kmzFile)
    // Return: gatenum - Number of gates 
    //         valid   - true, if valid FC kml/kmz format
    //         errors  - <> ""
    {
        int gate_num = 0
        boolean valid_format = false
        String read_errors = ""
        Route route_instance = null
        
        File km_file = new File(kmFileName)
        def kmz_file = null
        def km_reader = null
        if (kmzFile) {
            kmz_file = new java.util.zip.ZipFile(km_file)
            kmz_file.entries().findAll { !it.directory }.each {
                if (!km_reader) {
                    km_reader = kmz_file.getInputStream(it)
                }
            }
        } else {
            //km_reader = new FileReader(km_file)
            km_reader = new InputStreamReader(new FileInputStream(km_file), "UTF-8")
        }
        
        try {
            def kml = new XmlParser().parse(km_reader)
            
            def settings_folder = search_folder_by_name(kml.Document, Defs.ROUTEEXPORT_SETTINGS)
            def mapsettings_folder = search_folder_by_name(kml.Document, Defs.ROUTEEXPORT_MAPSETTINGS)
            def turnpoints_folder = search_folder_by_name(kml.Document, Defs.ROUTEEXPORT_TURNPOINTS)
            def photos_folder = search_folder_by_name(kml.Document, Defs.ROUTEEXPORT_PHOTOS)
            def canvas_folder = search_folder_by_name(kml.Document, Defs.ROUTEEXPORT_CANVAS)
            if (settings_folder && turnpoints_folder) {
                valid_format = true
                
                def settings_data = settings_folder.ExtendedData.Data
                
                // Create route
                route_instance = new Route()
                route_instance.contest = contestInstance
                route_instance.idTitle = Route.countByContest(contestInstance) + 1
                route_instance.turnpointRoute = contestInstance.turnpointRule.GetTurnpointRoute()
                route_instance.turnpointMapMeasurement = contestInstance.turnpointMapMeasurement
                if (route_instance.turnpointRoute == TurnpointRoute.Unassigned) {
                    route_instance.turnpointRoute = TurnpointRoute.None
                    route_instance.turnpointMapMeasurement = false
                }
                route_instance.enroutePhotoRoute = EnrouteRoute.None
                route_instance.enrouteCanvasRoute = EnrouteRoute.None
                route_instance.enroutePhotoMeasurement = contestInstance.enroutePhotoRule.GetEnrouteMeasurement()
                if (route_instance.enroutePhotoMeasurement == EnrouteMeasurement.Unassigned) {
                    route_instance.enroutePhotoMeasurement = EnrouteMeasurement.None
                }
                route_instance.enrouteCanvasMeasurement = contestInstance.enrouteCanvasRule.GetEnrouteMeasurement()
                if (route_instance.enrouteCanvasMeasurement == EnrouteMeasurement.Unassigned) {
                    route_instance.enrouteCanvasMeasurement = EnrouteMeasurement.None
                }
				route_instance.altitudeAboveGround = 0
                for (def d in settings_data) {
                    switch (d.'@name') {
                        case "routetitle":
                            String new_title = d.value.text()
                            String new_title2 = new_title
                            if (new_title2) { 
                                int found_num = 1
                                while (contestInstance.RouteTitleFound(new_title2)) {
                                    found_num++
                                    new_title2 = "${new_title} (${found_num})"
                                }
                            }
                            route_instance.title = new_title2
                            break
                        case "turnpoint":
                            route_instance.turnpointRoute = TurnpointRoute.(d.value.text())
                            break
                        case "turnpointmapmeasurement":
                            route_instance.turnpointMapMeasurement = d.value.text() == "yes"
                            break
                        case "turnpointprintstyle":
                            route_instance.turnpointPrintStyle = ObservationPrintStyle.(d.value.text())
                            break
                        case "turnpointprintpositionmaker":
                            route_instance.turnpointPrintPositionMaker = d.value.text() == "yes"
                            break
                        case "enroutephoto":
                            route_instance.enroutePhotoRoute = EnrouteRoute.(d.value.text())
                            break
                        case "enroutephotomeasurement":
                            route_instance.enroutePhotoMeasurement = EnrouteMeasurement.(d.value.text())
                            break
                        case "enroutephotoprintstyle":
                            route_instance.enroutePhotoPrintStyle = ObservationPrintStyle.(d.value.text())
                            break
                        case "enroutephotoprintpositionmarker":
                            route_instance.enroutePhotoPrintPositionMaker = d.value.text() == "yes"
                            break
                        case "enroutecanvas":
                            route_instance.enrouteCanvasRoute = EnrouteRoute.(d.value.text())
                            break
                        case "enroutecanvasmeasurement":
                            route_instance.enrouteCanvasMeasurement = EnrouteMeasurement.(d.value.text())
                            break
                        case "useprocedureturn":
                            route_instance.useProcedureTurns = d.value.text() == "yes"
                            break
						case "mapscale":
							route_instance.mapScale = d.value.text().toInteger()
							break
						case "altitudeaboveground":
							route_instance.altitudeAboveGround = d.value.text().toInteger()
							break
                    }
                }
                if (mapsettings_folder) {
                    def mapsettings_data = mapsettings_folder.ExtendedData.Data
                    for (def d in mapsettings_data) {
                        switch (d.'@name') {
                            case "contestmapairfields":
                                route_instance.contestMapAirfields = d.value.text()
                                break
                            case "contestmapcircle":
                                route_instance.contestMapCircle = d.value.text() == "yes"
                                break
                            case "contestmapprocedureturn":
                                route_instance.contestMapProcedureTurn = d.value.text() == "yes"
                                break
                            case "contestmapleg":
                                route_instance.contestMapLeg = d.value.text() == "yes"
                                break
                            case "contestmapcurvedleg":
                                route_instance.contestMapCurvedLeg = d.value.text() == "yes"
                                break
                            case "contestmapcurvedlegpoints":
                                route_instance.contestMapCurvedLegPoints = d.value.text()
                                break
                            case "contestmaptpname":
                                route_instance.contestMapTpName = d.value.text() == "yes"
                                break
                            case "contestmapsecretgates":
                                route_instance.contestMapSecretGates = d.value.text() == "yes"
                                break
                            case "contestmapenroutephotos":
                                route_instance.contestMapEnroutePhotos = d.value.text() == "yes"
                                break
                            case "contestmapgraticule":
                                route_instance.contestMapGraticule = d.value.text() == "yes"
                                break
                            case "contestmapcontourlines":
                                route_instance.contestMapContourLines = d.value.text().toInteger()
                                break
                            case "contestmapmunicipalitynames":
                                route_instance.contestMapMunicipalityNames = d.value.text() == "yes"
                                break
                            case "contestmapchurches":
                                route_instance.contestMapChurches = d.value.text() == "yes"
                                break
                            case "contestmapcastles":
                                route_instance.contestMapCastles = d.value.text() == "yes"
                                break
                            case "contestmapchateaus":
                                route_instance.contestMapChateaus = d.value.text() == "yes"
                                break
                            case "contestmappowerlines":
                                route_instance.contestMapPowerlines = d.value.text() == "yes"
                                break
                            case "contestmapwindpowerstations":
                                route_instance.contestMapWindpowerstations = d.value.text() == "yes"
                                break
                            case "contestmapsmallroads":
                                route_instance.contestMapSmallRoads = d.value.text() == "yes"
                                break
                            case "contestmappeaks":
                                route_instance.contestMapPeaks = d.value.text() == "yes"
                                break
                            case "contestmapdropshadow":
                                route_instance.contestMapDropShadow = d.value.text() == "yes"
                                break
                            case "contestmapadditionals":
                                route_instance.contestMapAdditionals = d.value.text() == "yes"
                                break
                            case "contestmapspecials":
                                route_instance.contestMapSpecials = d.value.text() == "yes"
                                break
                            case "contestmapairspaces":
                                route_instance.contestMapAirspaces = d.value.text() == "yes"
                                break
                            case "contestmapairspaceslayer":
                                route_instance.contestMapAirspacesLayer2 = d.value.text().replace(',','\n')
                                break
                            case "contestmapairspaceslayer2":
                                route_instance.contestMapAirspacesLayer2 = d.value.text()
                                break
                            case "contestmapairspaceslowerlimit":
                                route_instance.contestMapAirspacesLowerLimit = d.value.text().toInteger()
                                break
                            case "contestmapshowoptions1":
                                route_instance.contestMapShowFirstOptions = d.value.text() == "yes"
                                break
                            case "contestmaptitle1":
                                route_instance.contestMapFirstTitle = d.value.text()
                                break
                            case "contestmapcenterverticalpos1":
                                route_instance.contestMapCenterVerticalPos = VerticalPos.(d.value.text())
                                break
                            case "contestmapcenterhorizontalpos1":
                                route_instance.contestMapCenterHorizontalPos = HorizontalPos.(d.value.text())
                                break
                            case "contestmapcenterpoints1":
                                route_instance.contestMapCenterPoints = d.value.text()
                                break
                            case "contestmapprintpoints1":
                                route_instance.contestMapPrintPoints = d.value.text()
                                break
                            case "contestmapprintlandscape1":
                                route_instance.contestMapPrintLandscape = d.value.text() == "yes"
                                break
                            case "contestmapprintsize1":
                                route_instance.contestMapPrintSize = d.value.text()
                                break
                            case "contestmapcentermovex1":
                                route_instance.contestMapCenterMoveX = d.value.text().toBigDecimal()
                                break
                            case "contestmapcentermovey1":
                                route_instance.contestMapCenterMoveY = d.value.text().toBigDecimal()
                                break
                            case "contestmapshowoptions2":
                                route_instance.contestMapShowSecondOptions = d.value.text() == "yes"
                                break
                            case "contestmaptitle2":
                                route_instance.contestMapSecondTitle = d.value.text()
                                break
                            case "contestmapcenterverticalpos2":
                                route_instance.contestMapCenterVerticalPos2 = VerticalPos.(d.value.text())
                                break
                            case "contestmapcenterhorizontalpos2":
                                route_instance.contestMapCenterHorizontalPos2 = HorizontalPos.(d.value.text())
                                break
                            case "contestmapcenterpoints2":
                                route_instance.contestMapCenterPoints2 = d.value.text()
                                break
                            case "contestmapprintpoints2":
                                route_instance.contestMapPrintPoints2 = d.value.text()
                                break
                            case "contestmapprintlandscape2":
                                route_instance.contestMapPrintLandscape2 = d.value.text() == "yes"
                                break
                            case "contestmapprintsize2":
                                route_instance.contestMapPrintSize2 = d.value.text()
                                break
                            case "contestmapcentermovex2":
                                route_instance.contestMapCenterMoveX2 = d.value.text().toBigDecimal()
                                break
                            case "contestmapcentermovey2":
                                route_instance.contestMapCenterMoveY2 = d.value.text().toBigDecimal()
                                break
                            case "contestmapshowoptions3":
                                route_instance.contestMapShowThirdOptions = d.value.text() == "yes"
                                break
                            case "contestmaptitle3":
                                route_instance.contestMapThirdTitle = d.value.text()
                                break
                            case "contestmapcenterverticalpos3":
                                route_instance.contestMapCenterVerticalPos3 = VerticalPos.(d.value.text())
                                break
                            case "contestmapcenterhorizontalpos3":
                                route_instance.contestMapCenterHorizontalPos3 = HorizontalPos.(d.value.text())
                                break
                            case "contestmapcenterpoints3":
                                route_instance.contestMapCenterPoints3 = d.value.text()
                                break
                            case "contestmapprintpoints3":
                                route_instance.contestMapPrintPoints3 = d.value.text()
                                break
                            case "contestmapprintlandscape3":
                                route_instance.contestMapPrintLandscape3 = d.value.text() == "yes"
                                break
                            case "contestmapprintsize3":
                                route_instance.contestMapPrintSize3 = d.value.text()
                                break
                            case "contestmapcentermovex3":
                                route_instance.contestMapCenterMoveX3 = d.value.text().toBigDecimal()
                                break
                            case "contestmapcentermovey3":
                                route_instance.contestMapCenterMoveY3 = d.value.text().toBigDecimal()
                                break
                            case "contestmapshowoptions4":
                                route_instance.contestMapShowForthOptions = d.value.text() == "yes"
                                break
                            case "contestmaptitle4":
                                route_instance.contestMapForthTitle = d.value.text()
                                break
                            case "contestmapcenterverticalpos4":
                                route_instance.contestMapCenterVerticalPos4 = VerticalPos.(d.value.text())
                                break
                            case "contestmapcenterhorizontalpos4":
                                route_instance.contestMapCenterHorizontalPos4 = HorizontalPos.(d.value.text())
                                break
                            case "contestmapcenterpoints4":
                                route_instance.contestMapCenterPoints4 = d.value.text()
                                break
                            case "contestmapprintpoints4":
                                route_instance.contestMapPrintPoints4 = d.value.text()
                                break
                            case "contestmapprintlandscape4":
                                route_instance.contestMapPrintLandscape4 = d.value.text() == "yes"
                                break
                            case "contestmapprintsize4":
                                route_instance.contestMapPrintSize4 = d.value.text()
                                break
                            case "contestmapcentermovex4":
                                route_instance.contestMapCenterMoveX4 = d.value.text().toBigDecimal()
                                break
                            case "contestmapcentermovey4":
                                route_instance.contestMapCenterMoveY4 = d.value.text().toBigDecimal()
                                break
                        }
                    }
                }
                route_instance.liveTrackingScorecard = contestInstance.liveTrackingScorecard
                route_instance.save()
                
                // Add coordinates
                Map sign_data = read_import_sign(ImportSign.RouteCoord, turnpoints_folder, "", false, kmz_file)
                Map ret = add_sign_data(route_instance, ImportSign.RouteCoord, sign_data, false)
                
                // Add observations
                if (ret.valid && !ret.errors) {
                    gate_num = ret.importedsignnum
                    
                    if (route_instance.enroutePhotoRoute == EnrouteRoute.InputName) {
                        for (def d in settings_data) {
                            if (d.'@name'.startsWith("enroutephotosign:")) {
                                int view_pos = d.'@name'.substring(17).toInteger()
                                CoordEnroutePhoto coordenroutephoto_instance = new CoordEnroutePhoto()
                                coordenroutephoto_instance.route = route_instance
                                coordenroutephoto_instance.enroutePhotoName = d.value.text()
                                coordenroutephoto_instance.enrouteViewPos = view_pos
                                coordenroutephoto_instance.save()
                            }
                        }
                    } else if (photos_folder) {
						ImportSign import_sign = ImportSign.GetEnrouteSign(route_instance, true)
                        sign_data = read_import_sign(import_sign, photos_folder, "", false, kmz_file)
                        add_sign_data(route_instance, import_sign, sign_data, false)
                    }
                    
                    if (route_instance.enrouteCanvasRoute == EnrouteRoute.InputName) {
                        for (def d in settings_data) {
                            if (d.'@name'.startsWith("enroutecanvassign:")) {
                                int view_pos = d.'@name'.substring(18).toInteger()
                                CoordEnrouteCanvas coordenroutecanvas_instance = new CoordEnrouteCanvas()
                                coordenroutecanvas_instance.route = route_instance
                                coordenroutecanvas_instance.enrouteCanvasSign = EnrouteCanvasSign.(d.value.text())
                                coordenroutecanvas_instance.enrouteViewPos = view_pos
                                coordenroutecanvas_instance.save()
                            }
                        }
                    } else if (canvas_folder) {
						ImportSign import_sign = ImportSign.GetEnrouteSign(route_instance, false)
                        sign_data = read_import_sign(import_sign, canvas_folder, "", false, kmz_file)
                        add_sign_data(route_instance, import_sign, sign_data, false)
                    }
                }
                
                valid_format = ret.valid
                read_errors = ret.errors
            }
        } catch (Exception e) {
            read_errors += e.getMessage()
        }
        if (km_reader) {
            km_reader.close()
        }
        if (kmz_file) {
            kmz_file.close()
        }
        
        return [gatenum: gate_num, valid: valid_format, errors: read_errors, route: route_instance]
    }
    
    //--------------------------------------------------------------------------
    static Map ReadImportSignFile(String fileExtension, Route routeInstance, String routeFileName, String originalFileName, ImportSign importSign, String folderName, String namePrefix, boolean autoName)
    // Return: importedsignnum - Number of imported signs 
    //         filesignnum     - Number of signs in file 
    //         valid           - true, if valid route file format
    //         errors          - <> ""
    {
        switch (fileExtension) {
            case TXT_EXTENSION:
                Map sign_data = ReadImportSignTXTFile(routeInstance, routeFileName, importSign, namePrefix, autoName)
                return add_sign_data(routeInstance, importSign, sign_data, true)
            case KML_EXTENSION:
                Map sign_data = ReadImportSignKMLFile(routeInstance, routeFileName, importSign, folderName, namePrefix, autoName, false)
                return add_sign_data(routeInstance, importSign, sign_data, false)
            case KMZ_EXTENSION:
                Map sign_data = ReadImportSignKMLFile(routeInstance, routeFileName, importSign, folderName, namePrefix, autoName, true)
                return add_sign_data(routeInstance, importSign, sign_data, false)
        }
        return [importedsignnum: 0, filesignnum: 0, valid: false, errors: ""]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadImportSignTXTFile(Route routeInstance, String txtFileName, ImportSign importSign, String namePrefix, boolean autoName)
    // Return: import_signs - List of enroute signs
    //         valid        - true, if valid sign file format
    //         errors       - <> ""
    {
        List import_signs = []
        boolean valid_format = false
        int invalid_line_num = 0
        String read_errors = ""
        int line_pos = 0
        
        File txt_file = new File(txtFileName)
        LineNumberReader txt_reader = txt_file.newReader()
        
        try {
            int photo_num = 0
            while (true) {
                String line = txt_reader.readLine()
                if (line == null) {
                    break
                }
                line_pos++
                if (line && line.trim() && !line.startsWith(Defs.IGNORE_LINE)) {
                    line = change_comma2point(line)
                    line = change_problemchars(line)
                    valid_format = true
                    Map import_sign = importSign.GetLineValues(line)
                    import_sign.tpname = change_wp2tp(import_sign.tpname)
                    if (!import_sign.value_num_ok) {
                        invalid_line_num++
                        if (read_errors) {
                            read_errors += ", "
                        }
                        read_errors += line_pos.toString()
                    } else {
                        boolean invalid_line = false
                        boolean found = false
                        if (autoName) {
                            if (importSign.IsEnrouteCanvas()) {
                                import_sign.name = EnrouteCanvasSign.NoSign
                            } else if (importSign.IsEnroutePhoto()) {
                                photo_num++
                                import_sign.name = photo_num.toString()
                            }
                        }
                        if (namePrefix) {
                            if (import_sign.name.toLowerCase().startsWith(namePrefix.toLowerCase())) {
                                found = true
                            }
                        } else {
                            found = true
                        }
                        if (found) {
                            if (import_sign.name && !autoName) {
                                if (importSign.IsEnroutePhoto()) {
                                    // check non canvas name
                                    for (EnrouteCanvasSign sign in EnrouteCanvasSign.values()) {
                                        if (sign.canvasName == import_sign.name) {
                                            invalid_line = true
                                            break
                                        }
                                    }
                                    // check duplicate name
                                    if (!invalid_line) {
                                        if (CoordEnroutePhoto.findByEnroutePhotoNameAndRoute(import_sign.name,routeInstance)) {
                                            invalid_line = true
                                        }
                                    }
                                } else if (importSign.IsEnrouteCanvas()) { 
                                    // check canvas name
                                    boolean invalid = true
                                    for (EnrouteCanvasSign sign in EnrouteCanvasSign.values()) {
                                        if (sign.canvasName == import_sign.name) {
                                            invalid = false
                                            break
                                        }
                                    }
                                    if (invalid) {
                                        invalid_line = true
                                    }
                                    // check duplicate name
                                    if (!invalid_line && !routeInstance.contest.enrouteCanvasMultiple) {
                                        if (CoordEnrouteCanvas.findByEnrouteCanvasSignAndRoute(EnrouteCanvasSign.(import_sign.name),routeInstance)) {
                                            invalid_line = true
                                        }
                                    }
                                } else {
                                    invalid_line = true
                                }
                            } 
                            if (import_sign.lat) {
                                import_sign += [lat_value:routeInstance.contest.coordPresentation.GetDirectionGradDecimalMinute(import_sign.lat,true,true)]
                                if (import_sign.lat_value.invalid) {
                                    invalid_line = true
                                }
                            }
                            if (import_sign.lon) {
                                import_sign += [lon_value:routeInstance.contest.coordPresentation.GetDirectionGradDecimalMinute(import_sign.lon,true,false)]
                                if (import_sign.lon_value.invalid) {
                                    invalid_line = true
                                }
                            }
                            if (import_sign.alt) {
                                import_sign += [alt_value:GetAltitude(import_sign.alt)]
                                if (import_sign.alt_value.invalid) {
                                    invalid_line = true
                                }
                            }
                            if (import_sign.tpname) {
                                if (importSign.IsEnroutePhoto() || importSign.IsEnrouteCanvas()) { // check from turnpoints of enroute signs
                                    boolean invalid = true
                                    for (CoordTitle coord_title in routeInstance.GetEnrouteCoordTitles(false)) {
                                        if (import_sign.tpname == coord_title.titleExport()) {
                                            import_sign += [tptype:coord_title.type, tpnumber:coord_title.number]
                                            invalid = false
                                            break
                                        }
                                    }
                                    if (invalid) {
                                        invalid_line = true
                                    }
                                } else if (importSign.IsTurnpoint()) { // check turnpoint signs
                                    boolean invalid = true
                                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:"id"])) {
                                        if (coordroute_instance.type.IsTurnpointSignCoord()) {
                                            if (import_sign.tpname == coordroute_instance.titleExport()) {
                                                import_sign += [tptype:coordroute_instance.type, tpnumber:coordroute_instance.titleNumber]
                                                invalid = false
                                                break
                                            }
                                        }
                                    }
                                    if (invalid) {
                                        invalid_line = true
                                    }
                                } else if (importSign == ImportSign.RouteCoord) {
                                    boolean invalid = true
                                    for (CoordType coord_type in CoordType.values()) {
                                        if (import_sign.tpname.startsWith(coord_type.export)) {
                                            import_sign += [tptype:coord_type]
                                            invalid = false
                                            break
                                        }
                                    }
                                    if (invalid) {
                                        invalid_line = true
                                    }
                                } else {
                                    invalid_line = true
                                }
                            }
                            if (import_sign.nm) {
                                if (import_sign.nm.endsWith(UNIT_NM)) {
                                    String s = import_sign.nm.substring(0,import_sign.nm.size()-UNIT_NM.size())
                                    if (s.isBigDecimal()) {
                                        import_sign += [nm_value:s.toBigDecimal()]
                                    } else {
                                        invalid_line = true
                                    }
                                } else {
                                    invalid_line = true
                                }
                            }
                            if (import_sign.mm) {
                                if (import_sign.mm.endsWith(UNIT_mm)) {
                                    String s = import_sign.mm.substring(0,import_sign.mm.size()-UNIT_mm.size())
                                    if (s.isBigDecimal()) {
                                        import_sign += [mm_value:s.toBigDecimal()]
                                    } else {
                                        invalid_line = true
                                    }
    
                                } else {
                                    invalid_line = true
                                }
                            }
                            if (import_sign.tpsign) {
                                if (importSign == ImportSign.TurnpointPhoto) { // check for turnpoint photo signs
                                    boolean invalid = true
                                    for (TurnpointSign sign in TurnpointSign.GetTurnpointSigns(false)) {
                                        if (sign.title == import_sign.tpsign) {
                                            invalid = false
                                            break
                                        }
                                    }
                                    if (invalid) {
                                        invalid_line = true
                                    }
                                } else if (importSign == ImportSign.TurnpointCanvas) { // check for turnpoint canvas signs
                                    boolean invalid = true
                                    for (TurnpointSign sign in TurnpointSign.GetTurnpointSigns(true)) {
                                        if (sign.title == import_sign.tpsign) {
                                            invalid = false
                                            break
                                        }
                                    }
                                    if (invalid) {
                                        invalid_line = true
                                    }
                                } else {
                                    invalid_line = true
                                    }
                            }
                            if (import_sign.tpcorrect) {
                                if ((import_sign.tpcorrect == UNIT_TPcorrect) || (import_sign.tpcorrect == UNIT_TPincorrect)) {
                                    // nothing
                                } else {
                                    invalid_line = true
                                }
                            }
                            if (invalid_line) {
                                invalid_line_num++
                                if (read_errors) {
                                    read_errors += ", "
                                }
                                read_errors += line_pos.toString()
                            } else {
                                import_signs += import_sign
                            }
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            read_errors += e.getMessage()
        }
        txt_reader.close()
        
        return [import_signs: import_signs, valid: valid_format, invalidlinenum: invalid_line_num, errors: read_errors]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadImportSignKMLFile(Route routeInstance, String kmFileName, ImportSign importSign, String folderName, String namePrefix, boolean autoName, boolean kmzFile)
    // Return: import_signs - List of enroute signs
    //         valid        - true, if valid sign file format
    //         errors       - <> ""
    {
        Map ret = [import_signs: [], valid: false, invalidlinenum: 0, errors: ""]
        
        File km_file = new File(kmFileName)
        def kmz_file = null
        def km_reader = null
        if (kmzFile) {
            kmz_file = new java.util.zip.ZipFile(km_file)
            kmz_file.entries().findAll { !it.directory }.each {
                if (!km_reader) {
                    km_reader = kmz_file.getInputStream(it)
                }
            }
        } else {
            //km_reader = new FileReader(km_file)
            km_reader = new InputStreamReader(new FileInputStream(km_file), "UTF-8")
        }

        try {
            def kml = new XmlParser().parse(km_reader)
            def folder = null
            if (folderName) {
                folder = search_folder_by_name(kml.Document, folderName)
            } else {
                folder = kml.Document.Folder[0] // first folder
            }
            if (folder && folder.name) {
                ret = read_import_sign(importSign, folder, namePrefix, autoName, kmz_file)
            }
        } catch (Exception e) {
            ret.read_errors += e.getMessage()
        }
        if (km_reader) {
            km_reader.close()
        }
        if (kmz_file) {
            kmz_file.close()
        }
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    private static Map read_import_sign(ImportSign importSign, def readFolder, String namePrefix, boolean autoName, def kmzFile)
    {
        List import_signs = []
        boolean valid_format = false
        int invalid_line_num = 0
        String read_errors = ""
        
        if (readFolder.Placemark) {
            valid_format = true
            int photo_num = 0
            for (def pm in readFolder.Placemark) {
                if (pm.Point.coordinates) {
                    String sign_name = pm.name.text()
                    if (autoName) {
                        if (importSign.IsEnrouteCanvas()) {
                            sign_name = EnrouteCanvasSign.NoSign
                        } else if (importSign.IsEnroutePhoto()) {
                            photo_num++
                            sign_name = photo_num.toString()
                        }
                    }
                    
                    boolean found = false
                    if (namePrefix) {
                        if (sign_name.toLowerCase().startsWith(namePrefix.toLowerCase())) {
                            found = true
                        }
                    } else {
                        found = true
                    }
                    
                    // check canvas name
                    if (found) {
                        if (importSign.IsEnrouteCanvas() || importSign.IsEnroutePhoto()) {
                            boolean canvas_name = false
                            for (EnrouteCanvasSign sign in EnrouteCanvasSign.values()) {
                                if (sign_name.startsWith(sign.toString())) {
                                    canvas_name = true
                                    break
                                }
                            }
                            if (importSign.IsEnrouteCanvas()) {
                                if (!canvas_name) {
                                    found = false
                                }
                            } else if (importSign.IsEnroutePhoto()) {
                                if (canvas_name) {
                                    found = false
                                }
                            }
                        }
                    }

                    // add to import_signs
                    if (found) {
                        Map import_sign = importSign.GetKMLValues(sign_name)
                        
                        String coordinates = pm.Point.coordinates.text()
                        List coord = coordinates.split(',')
                        BigDecimal latitude = coord[1].toBigDecimal()
                        BigDecimal longitude = coord[0].toBigDecimal()
                        Integer altitude = (coord[2].toBigDecimal() * GpxService.ftPerMeter).toInteger()
                        import_sign += [lat_value:CoordPresentation.GetDirectionGradDecimalMinute(latitude, true)]
                        import_sign += [lon_value:CoordPresentation.GetDirectionGradDecimalMinute(longitude, false)]
                        import_sign += [alt_value:[invalid:false, alt:altitude]]
            
                        if (importSign == ImportSign.RouteCoord) {
                            CoordType tp_type = CoordType.UNKNOWN
                            for (CoordType coord_type in CoordType.values()) {
                                if (sign_name.startsWith(coord_type.export)) {
                                    tp_type = coord_type
                                    break
                                }
                            }
                            import_sign += [tptype:tp_type]
                        }
                        
                        if (pm.ExtendedData) {
                            for (def data in pm.ExtendedData.Data) {
                                if (data.'@name' == "observationpositiontop") {
                                    import_sign += [observationpositiontop:data.value.text().toInteger()]
                                }
                                if (data.'@name' == "observationpositionleft") {
                                    import_sign += [observationpositionleft:data.value.text().toInteger()]
                                }
                                if (data.'@name' == "observationnextprintpageturnpoint") {
                                    import_sign += [observationnextprintpageturnpoint:data.value.text()]
                                }
                                if (data.'@name' == "observationnextprintpageenroute") {
                                    import_sign += [observationnextprintpageenroute:data.value.text()]
                                }
                            }
                        }
                        
                        if (kmzFile) {
                            switch (importSign) {
                                case ImportSign.RouteCoord:
                                    if (import_sign.tpname) {
                                        import_sign += [imagedata: read_zipentry_data(kmzFile, "turnpointphotos/${import_sign.tpname}.jpg")]
                                    }
                                    break
                                case ImportSign.EnroutePhotoCoord:
                                    if (import_sign.name) {
                                        import_sign += [imagedata: read_zipentry_data(kmzFile, "photos/${import_sign.name}.jpg")]
                                    }
                                    break
                            }
                        }
                        
                        import_signs += import_sign
                    }
                }
            }
        }
        
        return [import_signs: import_signs, valid: valid_format, invalidlinenum: invalid_line_num, errors: read_errors]
    }
    
    //--------------------------------------------------------------------------
    private static byte[] read_zipentry_data(def kmzFile, String entryName)
    {
        java.util.zip.ZipEntry tp_entry = kmzFile.getEntry(entryName)
        if (tp_entry) {
            InputStream tp_stream = kmzFile.getInputStream(tp_entry)
            
            byte[] buff = new byte[8000]
            int bytes_read = 0
            ByteArrayOutputStream photo_stream = new ByteArrayOutputStream()
            while((bytes_read = tp_stream.read(buff)) != -1) {
                photo_stream.write(buff, 0, bytes_read)
            }
            
            return photo_stream.toByteArray()
        }
        return null
    }
    
    //--------------------------------------------------------------------------
    private static def search_folder_by_name(def startNode, String searchName)
    {
        def search_folder = startNode.Folder
        for (def f in search_folder) {
            // compare name
            if (f.name[0].text() == searchName) {
                return f
            }
            // search subfolder
            def ret = search_folder_by_name(f, searchName)
            if (ret) {
                return ret
            }
        }
        return null
    }

    //--------------------------------------------------------------------------
    private static String change_comma2point(String lineValue)
    {
        String ret = ""
        char last_ch = ' '
        char last_ch2 = ' '
        for (char ch in lineValue.getChars()) {
            if (last_ch == ',') {
                if (last_ch2.isDigit() && ch.isDigit()) {
                    ret += '.'
                } else {
                    ret += last_ch
                }
            }
            if (ch != ',') {
                ret += ch
            }
            last_ch2 = last_ch
            last_ch = ch
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private static String change_problemchars(String lineValue)
    {
        String s = lineValue.replaceAll('\t', ' ')
        s = s.replaceAll("\u2018", CoordPresentation.GRADMIN)
        s = s.replaceAll("\u2019", CoordPresentation.GRADMIN)
        s = s.replaceAll("\u201B", CoordPresentation.GRADMIN)
        s = s.replaceAll("\u2032", CoordPresentation.GRADMIN)
        s = s.replaceAll("\u201C", CoordPresentation.GRADSEC)
        s = s.replaceAll("\u201D", CoordPresentation.GRADSEC)
        s = s.replaceAll("\u201F", CoordPresentation.GRADSEC)
        s = s.replaceAll("\u2033", CoordPresentation.GRADSEC)
        s = s.replaceAll(CoordPresentation.GRAD, "${CoordPresentation.GRAD} ")
        s = s.replaceAll(CoordPresentation.GRADMIN, "${CoordPresentation.GRADMIN} ")
        s = s.replaceAll(CoordPresentation.GRADSEC, "${CoordPresentation.GRADSEC} ")
        return s
    }

    //--------------------------------------------------------------------------
    private static String change_wp2tp(String tpName)
    {
        if (tpName.startsWith('WP')) {
            return tpName.replaceFirst('WP', CoordType.TP.export)
        } else if (tpName.startsWith('CP')) {
            return tpName.replaceFirst('CP', CoordType.TP.export)
        } else if (tpName.startsWith('T/O')) {
            return tpName.replaceFirst('T/O', CoordType.TO.export)
        } else if (tpName.startsWith('UZK')) {
            return tpName.replaceFirst('UZK', CoordType.SECRET.export)
        }
        return tpName
    }
    
    //--------------------------------------------------------------------------
    private static Map add_sign_data(Route routeInstance, ImportSign importSign, Map signData, boolean txtFile)
    {
        if (!signData.valid || signData.invalidlinenum || signData.errors) {
            return [importedsignnum: 0, filesignnum: 0, valid: signData.valid, invalidlinenum: signData.invalidlinenum, errors: signData.errors]
        }
        
        int imported_sign_num = 0
        int invalid_line_num = 0
        String read_errors = ""
        int tp_num = 1
        int sc_num = 1
        for (Map import_sign in signData.import_signs) {
            if (importSign.IsEnroutePhoto()) {
                //if (CoordEnroutePhoto.countByRoute(routeInstance) < routeInstance.contest.maxEnroutePhotos) {
                    CoordEnroutePhoto coordenroutephoto_instance = new CoordEnroutePhoto()
                    coordenroutephoto_instance.route = routeInstance
                    coordenroutephoto_instance.enroutePhotoName = import_sign.name
                    coordenroutephoto_instance.enrouteViewPos = CoordEnroutePhoto.countByRoute(routeInstance) + 1
                    if (import_sign.lat || import_sign.lat_value) {
                        coordenroutephoto_instance.latDirection = import_sign.lat_value.direction
                        coordenroutephoto_instance.latGrad = import_sign.lat_value.grad
                        coordenroutephoto_instance.latMinute = import_sign.lat_value.minute
                    }
                    if (import_sign.lon || import_sign.lon_value) {
                        coordenroutephoto_instance.lonDirection = import_sign.lon_value.direction
                        coordenroutephoto_instance.lonGrad = import_sign.lon_value.grad
                        coordenroutephoto_instance.lonMinute = import_sign.lon_value.minute
                    }
                    if (import_sign.tpname) {
						if (!import_sign.tptype) {
							CoordTitle coord_title = CoordTitle.GetCoordTitle(import_sign.tpname)
							coordenroutephoto_instance.type = coord_title.type
							coordenroutephoto_instance.titleNumber = coord_title.number
						} else {
							coordenroutephoto_instance.type = import_sign.tptype
							coordenroutephoto_instance.titleNumber = import_sign.tpnumber
						}
                    }
                    if (import_sign.nm) {
						if (!import_sign.nm_value) {
							if (import_sign.nm.endsWith(UNIT_NM)) {
								String s = import_sign.nm.substring(0,import_sign.nm.size()-UNIT_NM.size())
								if (s.isBigDecimal()) {
									coordenroutephoto_instance.enrouteDistance = s.toBigDecimal()
								}
							}
						} else {
							coordenroutephoto_instance.enrouteDistance = import_sign.nm_value
						}
                    }
                    if (import_sign.mm) {
						if (!import_sign.mm_value) {
							if (import_sign.mm.endsWith(UNIT_mm)) {
								String s = import_sign.mm.substring(0,import_sign.mm.size()-UNIT_mm.size())
								if (s.isBigDecimal()) {
									coordenroutephoto_instance.measureDistance = s.toBigDecimal()
								}
							}
						} else {
							coordenroutephoto_instance.measureDistance = import_sign.mm_value
						}
                    }
                    coordenroutephoto_instance.calculateCoordEnrouteValues(routeInstance.enroutePhotoRoute)
                    if (import_sign.observationpositiontop) {
                        coordenroutephoto_instance.observationPositionTop = import_sign.observationpositiontop
                    }
                    if (import_sign.observationpositionleft) {
                        coordenroutephoto_instance.observationPositionLeft = import_sign.observationpositionleft
                    }
                    if (coordenroutephoto_instance.save()) {
                        if (import_sign.imagedata) {
                            ImageCoord imagecoord_instance = new ImageCoord(imageData:import_sign.imagedata, coord:coordenroutephoto_instance)
                            imagecoord_instance.save()
                            coordenroutephoto_instance.imagecoord = imagecoord_instance
                        }
                        routeInstance.calculateEnroutPhotoViewPos()
                        imported_sign_num++
                    } else {
                        invalid_line_num++
                        if (read_errors) {
                            read_errors += ", "
                        }
                        read_errors += import_sign.name
                    }
                //}
            } else if (importSign.IsEnrouteCanvas()) {
                //if (CoordEnrouteCanvas.countByRoute(routeInstance) < routeInstance.contest.maxEnrouteCanvas) {
					EnrouteCanvasSign enroute_canvassign = EnrouteCanvasSign.(import_sign.name)
                    CoordEnrouteCanvas coordenroutecanvas_instance = new CoordEnrouteCanvas()
                    coordenroutecanvas_instance.route = routeInstance
                    coordenroutecanvas_instance.enrouteCanvasSign = enroute_canvassign
                    coordenroutecanvas_instance.enrouteViewPos = CoordEnrouteCanvas.countByRoute(routeInstance) + 1
					if (import_sign.lat || import_sign.lat_value) {
						coordenroutecanvas_instance.latDirection = import_sign.lat_value.direction
						coordenroutecanvas_instance.latGrad = import_sign.lat_value.grad
						coordenroutecanvas_instance.latMinute = import_sign.lat_value.minute
					}
					if (import_sign.lon || import_sign.lon_value) {
						coordenroutecanvas_instance.lonDirection = import_sign.lon_value.direction
						coordenroutecanvas_instance.lonGrad = import_sign.lon_value.grad
						coordenroutecanvas_instance.lonMinute = import_sign.lon_value.minute
					}
                    if (import_sign.tpname) {
						if (!import_sign.tptype) {
							CoordTitle coord_title = CoordTitle.GetCoordTitle(import_sign.tpname)
							coordenroutecanvas_instance.type = coord_title.type
							coordenroutecanvas_instance.titleNumber = coord_title.number
						} else {
							coordenroutecanvas_instance.type = import_sign.tptype
							coordenroutecanvas_instance.titleNumber = import_sign.tpnumber
						}
                    }
                    if (import_sign.nm) {
						if (!import_sign.nm_value) {
							if (import_sign.nm.endsWith(UNIT_NM)) {
								String s = import_sign.nm.substring(0,import_sign.nm.size()-UNIT_NM.size())
								if (s.isBigDecimal()) {
									coordenroutecanvas_instance.enrouteDistance = s.toBigDecimal()
								}
							}
						} else {
							coordenroutecanvas_instance.enrouteDistance = import_sign.nm_value
						}
                    }
                    if (import_sign.mm) {
						if (!import_sign.nm_value) {
							if (import_sign.nm.endsWith(UNIT_NM)) {
								String s = import_sign.nm.substring(0,import_sign.nm.size()-UNIT_NM.size())
								if (s.isBigDecimal()) {
									coordenroutecanvas_instance.enrouteDistance = s.toBigDecimal()
								}
							}
						} else {
							coordenroutecanvas_instance.measureDistance = import_sign.mm_value
						}
					}
                    coordenroutecanvas_instance.calculateCoordEnrouteValues(routeInstance.enrouteCanvasRoute)
                    if (coordenroutecanvas_instance.save()) {
                        routeInstance.calculateEnroutCanvasViewPos()
                        imported_sign_num++
                    } else {
                        invalid_line_num++
                        if (read_errors) {
                            read_errors += ", "
                        }
                        read_errors += import_sign.name
                    }
                //}
            } else  if (importSign.IsTurnpoint()) {
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:"id"])) {
                    if ((import_sign.tptype == coordroute_instance.type) && (import_sign.tpnumber == coordroute_instance.titleNumber)) {
                        switch (importSign) {
                            case ImportSign.TurnpointPhoto:
                            case ImportSign.TurnpointCanvas:
                                coordroute_instance.assignedSign = TurnpointSign.GetTurnpointSign(import_sign.tpsign)
                                break
                            case ImportSign.TurnpointTrueFalse:
                                switch (import_sign.tpcorrect) {
                                    case RouteFileTools.UNIT_TPcorrect:
                                        coordroute_instance.correctSign = TurnpointCorrect.True
                                        break
                                    case RouteFileTools.UNIT_TPincorrect:
                                        coordroute_instance.correctSign = TurnpointCorrect.False
                                        break
                                }
                                break
                        }
                        if (coordroute_instance.save()) {
                            imported_sign_num++
                        } else {
                            invalid_line_num++
                            if (read_errors) {
                                read_errors += ", "
                            }
                            read_errors += import_sign.tpname
                        }
                        break
                    }
                }
            } else if (importSign == ImportSign.RouteCoord) {
                CoordRoute coordroute_instance = new CoordRoute()
                coordroute_instance.route = routeInstance
                
                coordroute_instance.type = import_sign.tptype
                if (import_sign.tptype == CoordType.SECRET) {
                    coordroute_instance.titleNumber = sc_num
                    sc_num++
                } else if (import_sign.tptype == CoordType.TP) {
                    coordroute_instance.titleNumber = tp_num
                    tp_num++
                }

                coordroute_instance.latDirection = import_sign.lat_value.direction
                coordroute_instance.latGrad = import_sign.lat_value.grad
                coordroute_instance.latMinute = import_sign.lat_value.minute
                
                coordroute_instance.lonDirection = import_sign.lon_value.direction
                coordroute_instance.lonGrad = import_sign.lon_value.grad
                coordroute_instance.lonMinute = import_sign.lon_value.minute
                
                coordroute_instance.altitude = import_sign.alt_value.alt
                
                if (import_sign.tptype.IsRunwayCoord()) {
                    coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_RUNWAY
                } else if (import_sign.tptype == CoordType.SECRET) {
                    coordroute_instance.gatewidth2 = routeInstance.contest.scGateWidth
                } else {
                    coordroute_instance.gatewidth2 = DEFAULT_GATEWIDTH_TP
                }
                
                if (import_sign.other) {
                    for (String o in import_sign.other) {
                        if (o.startsWith(GATE) && (o.endsWith(UNIT_GRAD) || o.endsWith(UNIT_NM))) {
                            String s = o.substring(GATE.size()).trim()
                            while (s.contains('  ')) {
                                s = s.replaceAll('  ', ' ')
                            }
                            def gate_values = s.split(' ')
                            for (String g in gate_values) {
                                if (g.endsWith(UNIT_GRAD)) {
                                    s = g.substring(0,g.size()-UNIT_GRAD.size())
                                    if (s.isBigDecimal()) {
                                        coordroute_instance.gateDirection = s.toBigDecimal()
                                    }
                                } else if (g.endsWith(UNIT_NM)) {
                                    s = g.substring(0,g.size()-UNIT_NM.size())
                                    if (s.isFloat()) {
                                        coordroute_instance.gatewidth2 = s.toFloat()
                                    }
                                }
                            }
                        }
                        if (o.startsWith(ALT) && o.endsWith(UNIT_ft)) {
                            String s = o.substring(ALT.size()).trim()
                            s = s.substring(0,s.size()-UNIT_ft.size())
                            if (s.isInteger()) {
                                coordroute_instance.altitude = s.toInteger()
                            }
                        }
                        if (o.startsWith(MINALT) && o.endsWith(UNIT_ft)) {
                            String s = o.substring(MINALT.size()).trim()
                            s = s.substring(0,s.size()-UNIT_ft.size())
                            if (s.isInteger()) {
                                coordroute_instance.minAltitudeAboveGround = s.toInteger()
                            }
                        }
                        if (o.startsWith(MAXALT) && o.endsWith(UNIT_ft)) {
                            String s = o.substring(MAXALT.size()).trim()
                            s = s.substring(0,s.size()-UNIT_ft.size())
                            if (s.isInteger()) {
                                coordroute_instance.maxAltitudeAboveGround = s.toInteger()
                            }
                        }
                        if (o.startsWith(DURATION) && o.endsWith(UNIT_min)) {
                            String s = o.substring(DURATION.size()).trim()
                            s = s.substring(0,s.size()-UNIT_min.size())
                            if (s.isInteger()) {
                                coordroute_instance.legDuration = s.toInteger()
                            }
                        }
                        if (o.startsWith(DIST) && o.endsWith(UNIT_mm)) {
                            String s = o.substring(DIST.size()).trim()
                            s = s.substring(0,s.size()-UNIT_mm.size())
                            if (s.isBigDecimal()) {
                                coordroute_instance.measureDistance = s.toBigDecimal()
                            }
                        }
                        if (o.startsWith(TRACK) && o.endsWith(UNIT_GRAD)) {
                            String s = o.substring(TRACK.size()).trim()
                            s = s.substring(0,s.size()-UNIT_GRAD.size())
                            if (s.isBigDecimal()) {
                                coordroute_instance.measureTrueTrack = s.toBigDecimal()
                            }
                        }
                        if (o == RouteFileTools.UNIT_TPnotimecheck) {
                            coordroute_instance.noTimeCheck = true
                        }
                        if (o == RouteFileTools.UNIT_TPnogatecheck) {
                            coordroute_instance.noGateCheck = true
                        }
                        if (o == RouteFileTools.UNIT_TPnoplanningtest) {
                            coordroute_instance.noPlanningTest = true
                        }
                        if (o == RouteFileTools.UNIT_TPendcurved) {
                            coordroute_instance.endCurved = true
                        }
                        if (o == RouteFileTools.UNIT_TPcirclecenter) {
                            coordroute_instance.circleCenter = true
                        }
                        if (o == RouteFileTools.UNIT_TPsemicircleinvert) {
                            coordroute_instance.semiCircleInvert = true
                        }
                        if (o == RouteFileTools.UNIT_TPignoregate) {
                            coordroute_instance.ignoreGate = true
                        }
                        if (o.startsWith(SIGN)) {
                            String s = o.substring(SIGN.size()).trim()
                            switch (routeInstance.turnpointRoute) {
                                case TurnpointRoute.AssignPhoto:
                                case TurnpointRoute.AssignCanvas:
                                    coordroute_instance.assignedSign = TurnpointSign.GetTurnpointSign(s)
                                    break
                                case TurnpointRoute.TrueFalsePhoto:
                                    switch (s) {
                                        case RouteFileTools.UNIT_TPcorrect:
                                            coordroute_instance.correctSign = TurnpointCorrect.True
                                            break
                                        case RouteFileTools.UNIT_TPincorrect:
                                            coordroute_instance.correctSign = TurnpointCorrect.False
                                            break
                                    }
                                    break
                            }
                        }
                    }
                }
                if (import_sign.observationpositiontop) {
                    coordroute_instance.observationPositionTop = import_sign.observationpositiontop
                }
                if (import_sign.observationpositionleft) {
                    coordroute_instance.observationPositionLeft = import_sign.observationpositionleft
                }
                if (import_sign.observationnextprintpageturnpoint) {
                    coordroute_instance.observationNextPrintPage = import_sign.observationnextprintpageturnpoint == "yes"
                }
                if (import_sign.observationnextprintpageenroute) {
                    coordroute_instance.observationNextPrintPageEnroute = import_sign.observationnextprintpageenroute == "yes"
                }
                
                if (coordroute_instance.save()) {
                    if (import_sign.imagedata) {
                        ImageCoord imagecoord_instance = new ImageCoord(imageData:import_sign.imagedata, coord:coordroute_instance)
                        imagecoord_instance.save()
                        coordroute_instance.imagecoord = imagecoord_instance
                    }
                    imported_sign_num++
                } else {
                    invalid_line_num++
                    if (read_errors) {
                        read_errors += ", "
                    }
                    read_errors += import_sign.tpname
                }
            }
        }
   
        return [importedsignnum: imported_sign_num, filesignnum: signData.import_signs.size(), valid: true, invalidlinenum: invalid_line_num, errors: read_errors]
    }
}
