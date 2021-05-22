import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import org.springframework.web.context.request.RequestContextHolder

import groovy.xml.*

import java.util.zip.*

// https://support.google.com/earth/answer/7365595?hl=de&ref_topic=7364446
// https://developers.google.com/kml/documentation/kmzarchives
// https://developers.google.com/kml/documentation/kmlreference
// https://developers.google.com/kml/documentation/kml_tut
// https://developers.google.com/kml/documentation/kml_tut#placemarks
// https://developers.google.com/kml/documentation/kml_tut#paths
// https://developers.google.com/kml/documentation/kml_tut#ground_overlays

class KmlService 
{
	def logService
    def messageSource
    def servletContext
    def grailsApplication
    
	final static BigDecimal ftPerMeter = 3.2808 // 1 meter = 3,2808 feet
    
    final static BigDecimal PROCEDURETURN_DISTANCE = 0.5 // NM
    final static BigDecimal MARGIN_DISTANCE = 5 // NM
    
    final static String XMLHEADER = "<?xml version='1.0' encoding='UTF-8'?>"
    
    final static String NAMESPACE_XMLNS = "http://www.opengis.net/kml/2.2"
    final static String NAMESPACE_XMLNS_GX = "http://www.google.com/kml/ext/2.2"
    final static String NAMESPACE_XMLNS_KML = "http://www.opengis.net/kml/2.2"
    final static String NAMESPACE_XMLNS_ATOM = "http://www.w3.org/2005/Atom"
     
    final static String KMLCREATOR = "Flight Contest - flightcontest.de - Version 1"
    
    final static String STYLE_ROUTELEG = "RouteLegStyle"
    final static String STYLE_ROUTELEG_COLOR = "ff00ffff"
    final static String STYLE_ROUTELEG_WIDTH = "2"
    
    final static String STYLE_GATE = "GateStyle"
    final static String STYLE_GATE_LABELCOLOR = "ffffffff"
    final static String STYLE_GATE_SCALE = "1.6"
    final static String TILT_GATE = "60" // Betrachtungswinkel in Grad
    final static String RANGE_GATE = "2000" // Betrachtungsentfernung in m
    final static String TILT_TO = "80" // Betrachtungswinkel in Grad
    final static String RANGE_TO = "1500" // Betrachtungsentfernung in m
    final static String TILT_LDG = "45" // Betrachtungswinkel in Grad
    final static String RANGE_LDG = "1500" // Betrachtungsentfernung in m

    final static String STYLE_AIRPORT = "AirportStyle"
    final static String STYLE_START = "StartStyle"
    final static String STYLE_FINISH = "FinishStyle"
    final static String STYLE_CIRCLECENTER = "CircleCenterStyle"
    
    final static String STYLE_TRACK = "TrackStyle"
    final static String STYLE_TRACK_COLOR = "ddddddff"
    final static String STYLE_TRACK_WIDTH = "1"
    
    final static String STYLE_ENROUTE_PHOTO = "EnroutePhotoStyle"
    final static String STYLE_ENROUTE_CANVAS = "EnrouteCanvasStyle-"
    final static String STYLE_ENROUTE_LABELCOLOR = "ffffffff"
    final static String STYLE_ENROUTE_PHOTO_SCALE = "1.2"
    final static String STYLE_ENROUTE_CANVAS_SCALE = "0.7"
    final static String TILT_ENROUTE = "60" // Betrachtungswinkel in Grad
    final static String RANGE_ENROUTE = "500" // Betrachtungsentfernung in m
    
    final static String GPXDATA = "GPXDATA"
	
    //--------------------------------------------------------------------------
    Map ConvertRoute2KMZ(Route routeInstance, String webRootDir, String kmzFileName, boolean isPrint, boolean wrEnrouteSign)
    {
        printstart "ConvertRoute2KMZ ${routeInstance.GetName(isPrint)} -> ${webRootDir + kmzFileName}"
        
        String kml_file_name = kmzFileName + ".kml"
        
        printstart "Generate '${webRootDir + kml_file_name}'"
        
        BufferedWriter kml_writer = null
        CharArrayWriter kml_data = null
        File kml_file = null
        if (kml_file_name.startsWith(GPXDATA)) {
            kml_data = new CharArrayWriter()
            kml_writer = new BufferedWriter(kml_data)
        } else {
            kml_file = new File(webRootDir + kml_file_name)
            kml_writer = kml_file.newWriter("UTF-8")
        }
        
        MarkupBuilder xml = new MarkupBuilder(kml_writer)
        kml_writer.writeLine(XMLHEADER)
        xml.kml(xmlns:NAMESPACE_XMLNS,'xmlns:gx':NAMESPACE_XMLNS_GX,'xmlns:kml':NAMESPACE_XMLNS_KML,'xmlns:atom':NAMESPACE_XMLNS_ATOM) {
            xml.Document {
                kmz_styles(xml)
                kmz_route(routeInstance, null, isPrint, wrEnrouteSign, xml)
            }
        }
        kml_writer.close()
        
        if (kml_file_name.startsWith(GPXDATA)) {
            BootStrap.tempData.AddData(kml_file_name, kml_data.toCharArray())
            printdone "${kml_data.size()} bytes"
            // TODO: write_kmz
        } else {
            printdone "${kml_file.size()} bytes"
            write_kmz(webRootDir, kmzFileName, kml_file_name)
            DeleteFile(webRootDir + kml_file_name)
        }

        printdone ""
        
        return [ok:true]
    }
    
    //--------------------------------------------------------------------------
    Map ConvertTest2KMZ(Test testInstance, String webRootDir, String kmzFileName, boolean isPrint, boolean wrEnrouteSign)
    {
        boolean found_track = false
        
        Route route_instance = testInstance.task.flighttest.route
        
        printstart "ConvertTest2KMZ '${testInstance.crew.name}' -> ${webRootDir + kmzFileName}"
        
        String kml_file_name = kmzFileName + ".kml"
        
        printstart "Generate '${webRootDir + kml_file_name}'"
        
        BufferedWriter kml_writer = null
        CharArrayWriter kml_data = null
        File kml_file = null
        if (kml_file_name.startsWith(GPXDATA)) {
            kml_data = new CharArrayWriter()
            kml_writer = new BufferedWriter(kml_data)
        } else {
            kml_file = new File(webRootDir + kml_file_name)
            kml_writer = kml_file.newWriter("UTF-8")
        }
        
        MarkupBuilder xml = new MarkupBuilder(kml_writer)
        kml_writer.writeLine(XMLHEADER)
        xml.kml(xmlns:NAMESPACE_XMLNS,'xmlns:gx':NAMESPACE_XMLNS_GX,'xmlns:kml':NAMESPACE_XMLNS_KML,'xmlns:atom':NAMESPACE_XMLNS_ATOM) {
            xml.Document {
                kmz_styles(xml)
                kmz_route(route_instance, testInstance, isPrint, wrEnrouteSign, xml)
                xml.Folder {
                    xml.name getMsg('fc.kmz.flights',isPrint)
                    found_track = kmz_track(testInstance, isPrint, xml)
                }
            }
        }
        kml_writer.close()
        
        if (kml_file_name.startsWith(GPXDATA)) {
            BootStrap.tempData.AddData(kml_file_name, kml_data.toCharArray())
            printdone "${kml_data.size()} bytes"
            // TODO: write_kmz
        } else {
            printdone "${kml_file.size()} bytes"
            write_kmz(webRootDir, kmzFileName, kml_file_name)
            DeleteFile(webRootDir + kml_file_name)
        }
        
        printdone ""
        
        return [ok:true, track:found_track]
    }
    
    //--------------------------------------------------------------------------
    Map ConvertTask2KMZ(Task taskInstance, String webRootDir, String kmzFileName, boolean isPrint, boolean wrEnrouteSign)
    {
        boolean found_track = false
        
        Route route_instance = taskInstance.flighttest.route
        
        printstart "ConvertTask2KMZ '${taskInstance.GetName(isPrint)}' -> ${webRootDir + kmzFileName}"
        
        String kml_file_name = kmzFileName + ".kml"
        
        printstart "Generate '${webRootDir + kml_file_name}'"
        
        BufferedWriter kml_writer = null
        CharArrayWriter kml_data = null
        File kml_file = null
        if (kml_file_name.startsWith(GPXDATA)) {
            kml_data = new CharArrayWriter()
            kml_writer = new BufferedWriter(kml_data)
        } else {
            kml_file = new File(webRootDir + kml_file_name)
            kml_writer = kml_file.newWriter("UTF-8")
        }
        
        MarkupBuilder xml = new MarkupBuilder(kml_writer)
        kml_writer.writeLine(XMLHEADER)
        xml.kml(xmlns:NAMESPACE_XMLNS,'xmlns:gx':NAMESPACE_XMLNS_GX,'xmlns:kml':NAMESPACE_XMLNS_KML,'xmlns:atom':NAMESPACE_XMLNS_ATOM) {
            xml.Document {
                kmz_styles(xml)
                kmz_route(route_instance, null, isPrint, wrEnrouteSign, xml)
                xml.Folder {
                    xml.name getMsg('fc.kmz.flights',isPrint)
                    for (Test test_instance in Test.findAllByTask(taskInstance,[sort:"viewpos"])) {
                        if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                            if (test_instance.IsFlightTestRun()) {
                                if (kmz_track(test_instance, isPrint, xml)) {
                                    found_track = true
                                }
                            }
                        }
                    }
                }
            }
        }
        kml_writer.close()
        
        if (kml_file_name.startsWith(GPXDATA)) {
            BootStrap.tempData.AddData(kml_file_name, kml_data.toCharArray())
            printdone "${kml_data.size()} bytes"
            // TODO: write_kmz
        } else {
            printdone "${kml_file.size()} bytes"
            write_kmz(webRootDir, kmzFileName, kml_file_name)
            DeleteFile(webRootDir + kml_file_name)
        }
        
        printdone ""
        
        return [ok:true, track:found_track]
    }
    
    //--------------------------------------------------------------------------
    private void kmz_styles(MarkupBuilder xml)
    {
        xml.Style(id:STYLE_ROUTELEG) {
            xml.LineStyle {
                xml.color STYLE_ROUTELEG_COLOR
                xml.width STYLE_ROUTELEG_WIDTH
            }
        }
        xml.Style(id:STYLE_GATE) {
            /*
            xml.IconStyle {
                xml.Icon {
                    xml.href "files/empty.png"
                }
                xml.hotSpot(x:"0.5", y:"0.5", xunits:"fraction", yunits:"fraction")
            }
            */
            xml.LabelStyle {
                xml.color STYLE_GATE_LABELCOLOR
                xml.scale STYLE_GATE_SCALE
            }
            xml.LineStyle {
                xml.color STYLE_ROUTELEG_COLOR
                xml.width STYLE_ROUTELEG_WIDTH
                xml.'gx:labelVisibility' "1"
            }
        }
        xml.Style(id:STYLE_AIRPORT) {
            xml.IconStyle {
                xml.Icon {
                    xml.href "files/airport.png"
                }
                xml.hotSpot(x:"0.5", y:"0.5", xunits:"fraction", yunits:"fraction")
            }
            xml.LabelStyle {
                xml.color STYLE_GATE_LABELCOLOR
                xml.scale STYLE_GATE_SCALE
            }
            xml.LineStyle {
                xml.color STYLE_ROUTELEG_COLOR
                xml.width STYLE_ROUTELEG_WIDTH
                //xml.'gx:labelVisibility' "1"
            }
        }
        xml.Style(id:STYLE_START) {
            xml.IconStyle {
                xml.Icon {
                    xml.href "files/start.png"
                }
                xml.hotSpot(x:"0.5", y:"0.5", xunits:"fraction", yunits:"fraction")
            }
            xml.LabelStyle {
                xml.color STYLE_GATE_LABELCOLOR
                xml.scale STYLE_GATE_SCALE
            }
            xml.LineStyle {
                xml.color STYLE_ROUTELEG_COLOR
                xml.width STYLE_ROUTELEG_WIDTH
                //xml.'gx:labelVisibility' "1"
            }
        }
        xml.Style(id:STYLE_FINISH) {
            xml.IconStyle {
                xml.Icon {
                    xml.href "files/finish.png"
                }
                xml.hotSpot(x:"0.5", y:"0.5", xunits:"fraction", yunits:"fraction")
            }
            xml.LabelStyle {
                xml.color STYLE_GATE_LABELCOLOR
                xml.scale STYLE_GATE_SCALE
            }
            xml.LineStyle {
                xml.color STYLE_ROUTELEG_COLOR
                xml.width STYLE_ROUTELEG_WIDTH
                //xml.'gx:labelVisibility' "1"
            }
        }
        xml.Style(id:STYLE_CIRCLECENTER) {
            xml.IconStyle {
                xml.Icon {
                    xml.href "files/cluster.png"
                }
                xml.hotSpot(x:"0.0", y:"0.0", xunits:"fraction", yunits:"fraction")
            }
            xml.LabelStyle {
                xml.color STYLE_GATE_LABELCOLOR
                xml.scale STYLE_GATE_SCALE
            }
            xml.LineStyle {
                xml.color STYLE_ROUTELEG_COLOR
                xml.width STYLE_ROUTELEG_WIDTH
                xml.'gx:labelVisibility' "1"
            }
        }
        xml.Style(id:STYLE_TRACK) {
            xml.LineStyle {
                xml.color STYLE_TRACK_COLOR
                xml.width STYLE_TRACK_WIDTH
            }
        }
        xml.Style(id:STYLE_ENROUTE_PHOTO) {
            xml.IconStyle {
                xml.Icon {
                    xml.href "files/photo.png"
                }
                xml.hotSpot(x:"0.5", y:"0", xunits:"fraction", yunits:"fraction")
            }
            xml.LabelStyle {
                xml.color STYLE_ENROUTE_LABELCOLOR
                xml.scale STYLE_ENROUTE_PHOTO_SCALE
            }
        }
        EnrouteCanvasSign.each { enroute_canvas_sign ->
            if (enroute_canvas_sign.imageName) {
                xml.Style(id:STYLE_ENROUTE_CANVAS + enroute_canvas_sign.canvasName) {
                    xml.IconStyle {
                        xml.color STYLE_ENROUTE_LABELCOLOR
                        xml.scale STYLE_ENROUTE_CANVAS_SCALE
                        xml.Icon {
                            xml.href "files/${enroute_canvas_sign.imageJpgShortName}"
                        }
                        xml.hotSpot(x:"0.5", y:"0.5", xunits:"fraction", yunits:"fraction")
                    }
                    xml.LabelStyle {
                        xml.scale "0"
                    }
                }
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private void set_route_properties(MarkupBuilder xml)
    {
        xml.extrude "1"    // extends the line down to the ground
        xml.tessellate "1" // breaks the line up into smaller chunks
    }
    
    //--------------------------------------------------------------------------
    private void write_kmz(String webRootDir, String kmzFileName, String kmlFileName)
    {
        ZipOutputStream kmz_zip_output_stream = new ZipOutputStream(new FileOutputStream(webRootDir + kmzFileName))
        
        write_file_to_kmz(kmz_zip_output_stream, "doc.kml", webRootDir, kmlFileName)
        kmz_zip_output_stream.putNextEntry(new ZipEntry("files/"))
        write_file_to_kmz(kmz_zip_output_stream, "files/photo.png", webRootDir, "GM_Utils/Icons/photo.png")
        write_file_to_kmz(kmz_zip_output_stream, "files/airport.png", webRootDir, "GM_Utils/Icons/airport.png")
        write_file_to_kmz(kmz_zip_output_stream, "files/start.png", webRootDir, "GM_Utils/Icons/start.png")
        write_file_to_kmz(kmz_zip_output_stream, "files/finish.png", webRootDir, "GM_Utils/Icons/finish.png")
        write_file_to_kmz(kmz_zip_output_stream, "files/cluster.png", webRootDir, "GM_Utils/Icons/cluster.png")
        EnrouteCanvasSign.each { enroute_canvas_sign ->
            if (enroute_canvas_sign.imageName) {
                write_file_to_kmz(kmz_zip_output_stream, "files/${enroute_canvas_sign.imageJpgShortName}", webRootDir, enroute_canvas_sign.imageJpgName)
            }
        }

        kmz_zip_output_stream.close()
    }
    
    //--------------------------------------------------------------------------
    private void write_file_to_kmz(ZipOutputStream zipOutputStream, String zipFileName, String webRootDir, String fileName)
    {
        byte[] buffer = new byte[1024]
        FileInputStream kml_file_input_stream = new FileInputStream(webRootDir + fileName)
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName))
        int length
        while ((length = kml_file_input_stream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length)
        }
        zipOutputStream.closeEntry()
        kml_file_input_stream.close()
    }
    
    //--------------------------------------------------------------------------
    private void kmz_route(Route routeInstance, Test testInstance, boolean isPrint, boolean wrEnrouteSign, MarkupBuilder xml)
    {
        printstart "kmz_route Print:$isPrint wrEnrouteSign:$wrEnrouteSign"
        
        Media media = Media.Screen
        if (isPrint) {
            media = Media.Print
        }
        
        Map contest_map_rect = [:]
        BigDecimal center_latitude = null
        BigDecimal center_longitude = null
        BigDecimal min_latitude = null
        BigDecimal min_longitude = null
        BigDecimal max_latitude = null
        BigDecimal max_longitude = null
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
            if (coordroute_instance.type.IsContestMapQuestionCoord()) {
                BigDecimal lat = coordroute_instance.latMath()
                BigDecimal lon = coordroute_instance.lonMath()
                if (min_latitude == null || lat < min_latitude) {
                    min_latitude = lat
                }
                if (min_longitude == null || lon < min_longitude) {
                    min_longitude = lon
                }
                if (max_latitude == null || lat > max_latitude) {
                    max_latitude = lat
                }
                if (max_longitude == null || lon > max_longitude) {
                    max_longitude = lon
                }
            }
        }
        
        contest_map_rect = AviationMath.getShowRect(min_latitude, max_latitude, min_longitude, max_longitude, MARGIN_DISTANCE)
        min_latitude = contest_map_rect.latmin
        min_longitude = contest_map_rect.lonmin
        max_latitude = contest_map_rect.latmax
        max_longitude = contest_map_rect.lonmax
        center_latitude = (max_latitude+min_latitude)/2
        center_longitude = (max_longitude+min_longitude)/2
        
        String route_name = routeInstance.GetName(isPrint)

        xml.Folder {
            xml.name "${getMsg('fc.kmz.route',isPrint)} '${route_name.encodeAsHTML()}'"
            
            // legs
            xml.Folder {
                xml.name getMsg('fc.kmz.legs',isPrint)
                long restart_id = 0
                xml.Placemark {
                    xml.styleUrl "#${STYLE_ROUTELEG}"
                    if (routeInstance.IsIntermediateSP()) {
                        xml.name "1"
                    } else {
                        xml.name route_name.encodeAsHTML()
                    }
                    xml.LineString {
                        set_route_properties(xml)
                        String route_coordinates = ""
                        CoordRoute last_coordroute_instance = null
                        CoordRoute start_coordroute_instance = null
                        CoordRoute center_coordroute_instance = null
                        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                            if (center_coordroute_instance) { // semicircle
                                List semicircle_coords = AviationMath.getSemicircle(
                                    center_coordroute_instance.latMath(), center_coordroute_instance.lonMath(),
                                    start_coordroute_instance.latMath(), start_coordroute_instance.lonMath(),
                                    coordroute_instance.latMath(), coordroute_instance.lonMath(), center_coordroute_instance.semiCircleInvert
                                )
                                for (Map semicircle_coord in semicircle_coords) {
                                    BigDecimal altitude_meter = center_coordroute_instance.altitude.toLong() / ftPerMeter
                                    if (route_coordinates) {
                                        route_coordinates += " "
                                    }
                                    route_coordinates += "${semicircle_coord.lon},${semicircle_coord.lat},${altitude_meter}"
                                }
                                start_coordroute_instance = null
                                center_coordroute_instance = null
                            } else if (coordroute_instance.circleCenter) {
                                start_coordroute_instance = last_coordroute_instance
                                center_coordroute_instance = coordroute_instance
                            }
                            if (!coordroute_instance.circleCenter) {
                                if (coordroute_instance.type.IsCpCheckCoord()) {
                                    BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                                    if (route_coordinates) {
                                        route_coordinates += " "
                                    }
                                    route_coordinates += "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter}"
                                }
                                if (coordroute_instance.type == CoordType.iFP) {
                                    restart_id = coordroute_instance.id
                                    break
                                }
                            }
                            last_coordroute_instance = coordroute_instance
                        }
                        xml.coordinates route_coordinates
                    }
                }
                if (restart_id != 0) {
                    xml.Placemark {
                        xml.styleUrl "#${STYLE_ROUTELEG}"
                        xml.name "2"
                        boolean run = false
                        xml.LineString {
                            set_route_properties(xml)
                            String route_coordinates = ""
                            CoordRoute last_coordroute_instance = null
                            CoordRoute start_coordroute_instance = null
                            CoordRoute center_coordroute_instance = null
                            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                                if (run) {
                                    if (center_coordroute_instance) { // semicircle
                                        List semicircle_coords = AviationMath.getSemicircle(
                                            center_coordroute_instance.latMath(), center_coordroute_instance.lonMath(),
                                            start_coordroute_instance.latMath(), start_coordroute_instance.lonMath(),
                                            coordroute_instance.latMath(), coordroute_instance.lonMath(), center_coordroute_instance.semiCircleInvert
                                        )
                                        for (Map semicircle_coord in semicircle_coords) {
                                            BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                                            if (route_coordinates) {
                                                route_coordinates += " "
                                            }
                                            route_coordinates += "${semicircle_coord.lon},${semicircle_coord.lat},${altitude_meter}"
                                        }
                                        start_coordroute_instance = null
                                        center_coordroute_instance = null
                                    } else if (coordroute_instance.circleCenter) {
                                        start_coordroute_instance = last_coordroute_instance
                                        center_coordroute_instance = coordroute_instance
                                    } 
                                    if (!coordroute_instance.circleCenter) {
                                        if (coordroute_instance.type.IsCpCheckCoord()) {
                                            BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                                            if (route_coordinates) {
                                                route_coordinates += " "
                                            }
                                            route_coordinates += "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter}"
                                        }
                                        if (coordroute_instance.type == CoordType.iFP) {
                                            restart_coordroute_instance = coordroute_instance
                                            break
                                        }
                                    }
                                    last_coordroute_instance = coordroute_instance
                                }
                                if (restart_id == coordroute_instance.id) {
                                    run = true
                                }
                            }
                            xml.coordinates route_coordinates
                        }
                    }
                }
            }
            
            // procedure turns
            int procedureturn_num = 0
            if ((!testInstance || testInstance.task.procedureTurnDuration > 0) && routeInstance.useProcedureTurns) {
                for (RouteLegCoord routeleg_instance in RouteLegCoord.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (routeleg_instance.IsProcedureTurn()) {
                        BigDecimal course_change = AviationMath.courseChange(routeleg_instance.turnTrueTrack,routeleg_instance.testTrueTrack())
                        if (course_change.abs() >= 90) {
                            procedureturn_num++
                        }
                    }
                }
            }
            if (procedureturn_num > 0) {
                xml.Folder {
                    xml.name getMsg('fc.kmz.procedureturns',isPrint)
                    CoordRoute last_coordroute_instance = null
                    CoordRoute last_last_coordroute_instance = null
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                        if (routeInstance.useProcedureTurns && coordroute_instance.planProcedureTurn && last_coordroute_instance && last_last_coordroute_instance && last_coordroute_instance.type.IsProcedureTurnCoord()) {
                            List circle_coords = AviationMath.getProcedureTurnCircle(
                                last_last_coordroute_instance.latMath(), last_last_coordroute_instance.lonMath(),
                                last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                0,
                                PROCEDURETURN_DISTANCE
                            )
                            Map gate = AviationMath.getGate(
                                last_last_coordroute_instance.latMath(),last_last_coordroute_instance.lonMath(),
                                last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                                last_coordroute_instance.gatewidth2
                            )
                            xml.Placemark {
                                xml.styleUrl "#${STYLE_ROUTELEG}"
                                xml.name last_coordroute_instance.titleMediaCode(media)
                                String route_coordinates = ""
                                for (Map circle_coord in circle_coords) {
                                    if (route_coordinates) {
                                        route_coordinates += " "
                                    }
                                    route_coordinates += "${circle_coord.lon},${circle_coord.lat},0"
                                }
                                xml.LookAt {
                                    xml.longitude last_coordroute_instance.lonMath()
                                    xml.latitude last_coordroute_instance.latMath()
                                    xml.altitude "0"
                                    xml.heading "${gate.gateTrack}"
                                    xml.tilt TILT_GATE
                                    xml.range RANGE_GATE
                                } 
                                xml.LineString {
                                    xml.coordinates route_coordinates
                                }
                            }
                        }
                        last_last_coordroute_instance = last_coordroute_instance 
                        last_coordroute_instance = coordroute_instance
                    }
                }
            }

            // points
            xml.Folder {
                xml.name getMsg('fc.kmz.points',isPrint)
                List enroute_points = RoutePointsTools.GetShowPointsRoute(routeInstance, null, messageSource, [isPrint:isPrint, wrEnrouteSign:true, showCurvedPoints:true])
                int route_point_pos = 0
                CoordRoute last_coordroute_instance = null
                boolean first = true
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    
                    boolean wr_enroute_points = false
                    if (last_coordroute_instance && (enroute_points[route_point_pos].name == last_coordroute_instance.titleCode())) {
                        route_point_pos++
                        wr_enroute_points = true
                    }
                    
                    // gates
                    BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                    switch (coordroute_instance.type) {
                        case CoordType.TO:
                            if (testInstance) {
                                Map gate = AviationMath.getGate(
                                    coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                    testInstance.flighttestwind.TODirection, 
                                    testInstance.flighttestwind.TOOffset,
                                    testInstance.flighttestwind.TOOrthogonalOffset,
                                    coordroute_instance.gatewidth2
                                )
                                xml.Placemark {
                                    xml.styleUrl "#${STYLE_AIRPORT}"
                                    xml.name coordroute_instance.titleMediaCode(media)
                                    xml.LookAt {
                                        xml.longitude coordroute_instance.lonMath()
                                        xml.latitude coordroute_instance.latMath()
                                        xml.altitude "0"
                                        xml.heading "${testInstance.flighttestwind.TODirection}"
                                        xml.tilt TILT_TO
                                        xml.range RANGE_TO
                                    } 
                                    xml.MultiGeometry {
                                        xml.LineString {
                                            set_route_properties(xml)
                                            xml.coordinates "${gate.coordRight.lon},${gate.coordRight.lat},${altitude_meter} ${gate.coordLeft.lon},${gate.coordLeft.lat},${altitude_meter}"
                                        }
                                        xml.Point {
                                            xml.coordinates "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter}"
                                        }
                                    }
                                }
                            } else {
                                Map gate = AviationMath.getGate(
                                    coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                    coordroute_instance.gateDirection, 
                                    0,
                                    0,
                                    coordroute_instance.gatewidth2
                                )
                                xml.Placemark {
                                    xml.styleUrl "#${STYLE_AIRPORT}"
                                    xml.name coordroute_instance.titleMediaCode(media)
                                    xml.LookAt {
                                        xml.longitude coordroute_instance.lonMath()
                                        xml.latitude coordroute_instance.latMath()
                                        xml.altitude "0"
                                        xml.heading "${coordroute_instance.gateDirection}"
                                        xml.tilt TILT_TO
                                        xml.range RANGE_TO
                                    } 
                                    xml.MultiGeometry {
                                        xml.LineString {
                                            set_route_properties(xml)
                                            xml.coordinates "${gate.coordRight.lon},${gate.coordRight.lat},${altitude_meter} ${gate.coordLeft.lon},${gate.coordLeft.lat},${altitude_meter}"
                                        }
                                        xml.Point {
                                            xml.coordinates "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter}"
                                        }
                                    }
                                }
                            }
                            break
                        case CoordType.LDG:
                            if (testInstance) {
                                Map gate = AviationMath.getGate(
                                    coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                    testInstance.flighttestwind.LDGDirection, 
                                    testInstance.flighttestwind.LDGOffset,
                                    testInstance.flighttestwind.LDGOrthogonalOffset,
                                    coordroute_instance.gatewidth2
                                )
                                xml.Placemark {
                                    xml.styleUrl "#${STYLE_AIRPORT}"
                                    xml.name coordroute_instance.titleMediaCode(media)
                                    xml.LookAt {
                                        xml.longitude coordroute_instance.lonMath()
                                        xml.latitude coordroute_instance.latMath()
                                        xml.altitude "0"
                                        xml.heading "${testInstance.flighttestwind.LDGDirection}"
                                        xml.tilt TILT_LDG
                                        xml.range RANGE_LDG
                                    } 
                                    xml.MultiGeometry {
                                        xml.LineString {
                                            set_route_properties(xml)
                                            xml.coordinates "${gate.coordRight.lon},${gate.coordRight.lat},${altitude_meter} ${gate.coordLeft.lon},${gate.coordLeft.lat},${altitude_meter}"
                                        }
                                        xml.Point {
                                            xml.coordinates "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter}"
                                        }
                                    }
                                }
                            } else {
                                Map gate = AviationMath.getGate(
                                    coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                    coordroute_instance.gateDirection, 
                                    0,
                                    0,
                                    coordroute_instance.gatewidth2
                                )
                                xml.Placemark {
                                    xml.styleUrl "#${STYLE_AIRPORT}"
                                    xml.name coordroute_instance.titleMediaCode(media)
                                    xml.LookAt {
                                        xml.longitude coordroute_instance.lonMath()
                                        xml.latitude coordroute_instance.latMath()
                                        xml.altitude "0"
                                        xml.heading "${coordroute_instance.gateDirection}"
                                        xml.tilt TILT_LDG
                                        xml.range RANGE_LDG
                                    } 
                                    xml.MultiGeometry {
                                        xml.LineString {
                                            set_route_properties(xml)
                                            xml.coordinates "${gate.coordRight.lon},${gate.coordRight.lat},${altitude_meter} ${gate.coordLeft.lon},${gate.coordLeft.lat},${altitude_meter}"
                                        }
                                        xml.Point {
                                            xml.coordinates "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter}"
                                        }
                                    }
                                }
                            }
                            break
                        case CoordType.iTO:
                        case CoordType.iLDG:
                            if (testInstance) {
                                Map gate = AviationMath.getGate(
                                    coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                    testInstance.flighttestwind.iTOiLDGDirection, 
                                    testInstance.flighttestwind.iTOiLDGOffset,
                                    testInstance.flighttestwind.iTOiLDGOrthogonalOffset,
                                    coordroute_instance.gatewidth2
                                )
                                xml.Placemark {
                                    xml.styleUrl "#${STYLE_AIRPORT}"
                                    xml.name coordroute_instance.titleMediaCode(media)
                                    xml.LookAt {
                                        xml.longitude coordroute_instance.lonMath()
                                        xml.latitude coordroute_instance.latMath()
                                        xml.altitude "0"
                                        xml.heading "${testInstance.flighttestwind.iTOiLDGDirection}"
                                        xml.tilt TILT_LDG
                                        xml.range RANGE_LDG
                                    } 
                                    xml.MultiGeometry {
                                        xml.LineString {
                                            set_route_properties(xml)
                                            xml.coordinates "${gate.coordRight.lon},${gate.coordRight.lat},${altitude_meter} ${gate.coordLeft.lon},${gate.coordLeft.lat},${altitude_meter}"
                                        }
                                        xml.Point {
                                            xml.coordinates "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter}"
                                        }
                                    }
                                }
                            } else {
                                Map gate = AviationMath.getGate(
                                    coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                    coordroute_instance.gateDirection, 
                                    0,
                                    0,
                                    coordroute_instance.gatewidth2
                                )
                                xml.Placemark {
                                    xml.styleUrl "#${STYLE_AIRPORT}"
                                    xml.name coordroute_instance.titleMediaCode(media)
                                    xml.LookAt {
                                        xml.longitude coordroute_instance.lonMath()
                                        xml.latitude coordroute_instance.latMath()
                                        xml.altitude "0"
                                        xml.heading "${coordroute_instance.gateDirection}"
                                        xml.tilt TILT_LDG
                                        xml.range RANGE_LDG
                                    } 
                                    xml.MultiGeometry {
                                        xml.LineString {
                                            set_route_properties(xml)
                                            xml.coordinates "${gate.coordRight.lon},${gate.coordRight.lat},${altitude_meter} ${gate.coordLeft.lon},${gate.coordLeft.lat},${altitude_meter}"
                                        }
                                        xml.Point {
                                            xml.coordinates "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter}"
                                        }
                                    }
                                }
                            }
                            break
                    }
                    
                    if (last_coordroute_instance && last_coordroute_instance.type.IsCpCheckCoord() && coordroute_instance.type.IsCpCheckCoord()) {
                        if (first) {
                            // SP, iSP
                            Map start_gate = AviationMath.getGate(
                                coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                                last_coordroute_instance.gatewidth2
                            )
                            xml.Placemark {
                                BigDecimal altitude_meter2 = last_coordroute_instance.altitude.toLong() / ftPerMeter
                                xml.styleUrl "#${STYLE_START}"
                                xml.name last_coordroute_instance.titleMediaCode(media)
                                xml.LookAt {
                                    xml.longitude last_coordroute_instance.lonMath()
                                    xml.latitude last_coordroute_instance.latMath()
                                    xml.altitude "0"
                                    xml.heading "${AviationMath.getDiametricalTrack(start_gate.gateTrack)}"
                                    xml.tilt TILT_GATE
                                    xml.range RANGE_GATE
                                }
                                xml.MultiGeometry {
                                    xml.LineString {
                                        set_route_properties(xml)
                                        xml.coordinates "${start_gate.coordRight.lon},${start_gate.coordRight.lat},${altitude_meter2} ${start_gate.coordLeft.lon},${start_gate.coordLeft.lat},${altitude_meter2}"
                                    }
                                    xml.Point {
                                        xml.coordinates "${last_coordroute_instance.lonMath()},${last_coordroute_instance.latMath()},${altitude_meter2}"
                                    }
                                }
                            }
                        }
                        
                        // enroute photos and canvas
                        if (wr_enroute_points) {
                            while (enroute_points[route_point_pos].enroutephoto || enroute_points[route_point_pos].enroutecanvas) {
                                Map leg_track = AviationMath.calculateLeg(
                                    coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                    last_coordroute_instance.latMath(),last_coordroute_instance.lonMath()
                                )
                                Map leg_enroute = AviationMath.calculateLeg(
                                    enroute_points[route_point_pos].latcenter,enroute_points[route_point_pos].loncenter,
                                    last_coordroute_instance.latMath(),last_coordroute_instance.lonMath()
                                )
                                Map coord_on_track = AviationMath.getCoordinate(last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),leg_track.dir,leg_enroute.dis)
                                if (enroute_points[route_point_pos].enroutephoto) {
                                    xml.Placemark {
                                        xml.name enroute_points[route_point_pos].name
                                        xml.styleUrl "#${STYLE_ENROUTE_PHOTO}"
                                        xml.LookAt {
                                            xml.longitude coord_on_track.lon
                                            xml.latitude coord_on_track.lat
                                            xml.altitude "0"
                                            xml.heading "${leg_track.dir}"
                                            xml.tilt TILT_ENROUTE
                                            xml.range RANGE_ENROUTE
                                        }
                                        xml.Point {
                                            xml.coordinates "${enroute_points[route_point_pos].loncenter},${enroute_points[route_point_pos].latcenter},0"
                                        }
                                    }
                                } else if (enroute_points[route_point_pos].enroutecanvas) {
                                    xml.Placemark {
                                        xml.name enroute_points[route_point_pos].name
                                        xml.styleUrl "#${STYLE_ENROUTE_CANVAS}${enroute_points[route_point_pos].name}"
                                        xml.LookAt {
                                            xml.longitude coord_on_track.lon
                                            xml.latitude coord_on_track.lat
                                            xml.altitude "0"
                                            xml.heading "${leg_track.dir}"
                                            xml.tilt TILT_ENROUTE
                                            xml.range RANGE_ENROUTE
                                        } 
                                        xml.Point {
                                            xml.coordinates "${enroute_points[route_point_pos].loncenter},${enroute_points[route_point_pos].latcenter},0"
                                        }
                                    }
                                }
                                route_point_pos++
                            }
                        }
                        
                        if ((coordroute_instance.type == CoordType.iSP) && (last_coordroute_instance.type == CoordType.iFP)) {
                            // no standard gate
                        } else {
                            // standard gate
                            boolean wr_gate = false
                            boolean show_curved_point = routeInstance.showCurvedPoints
                            List curved_point_ids = routeInstance.GetCurvedPointIds()
                            if (show_curved_point || !coordroute_instance.HideSecret(curved_point_ids)) {
                                wr_gate = true
                            }
                            if (wr_gate) {
                                Float gate_width = null
                                if (testInstance && coordroute_instance.type == CoordType.SECRET) {
                                    gate_width = testInstance.GetSecretGateWidth()
                                }
                                if (!gate_width) {
                                    gate_width = coordroute_instance.gatewidth2
                                }
                                Map gate = AviationMath.getGate(
                                    last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                                    coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                    gate_width
                                )
                                xml.Placemark {
                                    BigDecimal altitude_meter2 = coordroute_instance.altitude.toLong() / ftPerMeter
                                    if (coordroute_instance.type.IsEnrouteFinishCoord()) {
                                        xml.styleUrl "#${STYLE_FINISH}"
                                    } else if (coordroute_instance.circleCenter) {
                                        xml.styleUrl "#${STYLE_CIRCLECENTER}"
                                    } else {
                                        xml.styleUrl "#${STYLE_GATE}"
                                    }
                                    xml.name coordroute_instance.titleMediaCode(media)
                                    if (!coordroute_instance.circleCenter) {
                                        xml.LookAt {
                                            xml.longitude coordroute_instance.lonMath()
                                            xml.latitude coordroute_instance.latMath()
                                            xml.altitude "0"
                                            xml.heading "${gate.gateTrack}"
                                            xml.tilt TILT_GATE
                                            xml.range RANGE_GATE
                                        }
                                    }
                                    xml.MultiGeometry {
                                        if (coordroute_instance.type.IsEnrouteFinishCoord()) {
                                            xml.Point {
                                                xml.coordinates "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter2}"
                                            }
                                        } else if (coordroute_instance.circleCenter) {
                                            xml.Point {
                                                xml.coordinates "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter2}"
                                            }
                                        }
                                        if (!coordroute_instance.circleCenter) {
                                            xml.LineString {
                                                set_route_properties(xml)
                                                xml.coordinates "${gate.coordLeft.lon},${gate.coordLeft.lat},${altitude_meter2} ${gate.coordRight.lon},${gate.coordRight.lat},${altitude_meter2}"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        first = false
                    }
                    if (coordroute_instance.type == CoordType.iSP) {
                        first = true
                    }
                    last_coordroute_instance = coordroute_instance
                }
            }
        
            boolean wr_enroutesign = wrEnrouteSign
            if (wrEnrouteSign && testInstance) {
                wr_enroutesign = testInstance.flighttestwind.flighttest.IsObservationSignUsed()
            }
            
            // export
            xml.Folder {
                xml.name getMsg('fc.coordroute.export',isPrint)
                xml.visibility "0"
                
                if (wr_enroutesign) {
                    xml.Folder {
                        xml.name Defs.ROUTEEXPORT_SETTINGS
                        xml.visibility "0"
                        xml.ExtendedData {
                            xml.Data(name:"creator") {
                                xml.value Defs.ROUTEEXPORT_CREATOR
                            }
                            xml.Data(name:"routetitle") {
                                xml.value routeInstance.title
                            }
                            xml.Data(name:"turnpoint") {
                                xml.value routeInstance.turnpointRoute
                            }
                            xml.Data(name:"turnpointmapmeasurement") {
                                xml.value getYesNo(routeInstance.turnpointMapMeasurement)
                            }
                            xml.Data(name:"enroutephoto") {
                                xml.value routeInstance.enroutePhotoRoute
                            }
                            xml.Data(name:"enroutephotomeasurement") {
                                xml.value routeInstance.enroutePhotoMeasurement
                            }
                            xml.Data(name:"enroutecanvas") {
                                xml.value routeInstance.enrouteCanvasRoute
                            }
                            xml.Data(name:"enroutecanvasmeasurement") {
                                xml.value routeInstance.enrouteCanvasMeasurement
                            }
                            xml.Data(name:"useprocedureturn") {
                                xml.value getYesNo(routeInstance.useProcedureTurns)
                            }
                            if (routeInstance.enroutePhotoRoute == EnrouteRoute.InputName) {
                                for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                                    xml.Data(name:"enroutephotosign:${coordenroutephoto_instance.enrouteViewPos}") {
                                        xml.value coordenroutephoto_instance.enroutePhotoName
                                    }
                                }
                            }
                            if (routeInstance.enrouteCanvasRoute == EnrouteRoute.InputName) {
                                for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                                    xml.Data(name:"enroutecanvassign:${coordenroutecanvas_instance.enrouteViewPos}") {
                                        xml.value coordenroutecanvas_instance.enrouteCanvasSign.canvasName
                                    }
                                }
                            }
                        }
                    }
                }

                xml.Folder {
                    xml.name Defs.ROUTEEXPORT_TURNPOINTS
                    xml.visibility "0"
                    CoordRoute last_coordroute_instance = null
                    boolean first = true
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                        
                        // gates
                        BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                        switch (coordroute_instance.type) {
                            case CoordType.TO:
                            case CoordType.LDG:
                            case CoordType.iTO:
                            case CoordType.iLDG:
                                xml.Placemark {
                                    xml.styleUrl "#${STYLE_AIRPORT}"
                                    xml.name coordroute_instance.GetExportRouteCoord(true)
                                    xml.visibility "0"
                                    xml.Point {
                                        xml.coordinates "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter}"
                                    }
                                }
                                break
                        }
                        
                        if (last_coordroute_instance && last_coordroute_instance.type.IsCpCheckCoord() && coordroute_instance.type.IsCpCheckCoord()) {
                            if (first) {
                                // SP, iSP
                                xml.Placemark {
                                    BigDecimal altitude_meter2 = last_coordroute_instance.altitude.toLong() / ftPerMeter
                                    xml.styleUrl "#${STYLE_START}"
                                    xml.name last_coordroute_instance.GetExportRouteCoord(true)
                                    xml.visibility "0"
                                    xml.Point {
                                        xml.coordinates "${last_coordroute_instance.lonMath()},${last_coordroute_instance.latMath()},${altitude_meter2}"
                                    }
                                }
                            }
                            
                            if ((coordroute_instance.type == CoordType.iSP) && (last_coordroute_instance.type == CoordType.iFP)) {
                                // no standard gate
                            } else {
                                // standard gate
                                xml.Placemark {
                                    if (coordroute_instance.type.IsEnrouteFinishCoord()) {
                                        xml.styleUrl "#${STYLE_FINISH}"
                                    } else if (coordroute_instance.circleCenter) {
                                        xml.styleUrl "#${STYLE_CIRCLECENTER}"
                                    } else {
                                        xml.styleUrl "#${STYLE_GATE}"
                                    }
                                    xml.name coordroute_instance.GetExportRouteCoord(true)
                                    xml.visibility "0"
                                    xml.Point {
                                        xml.coordinates "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter}"
                                    }
                                }
                            }
                            first = false
                        }
                        if (coordroute_instance.type == CoordType.iSP) {
                            first = true
                        }
                        last_coordroute_instance = coordroute_instance
                    }
                }
                if (routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
                    List coordenroutephoto_instances = CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])
                    if (coordenroutephoto_instances) {
                        xml.Folder {
                            xml.name Defs.ROUTEEXPORT_PHOTOS
                            xml.visibility "0"
                            for (CoordEnroutePhoto coordenroutephoto_instance in coordenroutephoto_instances) {
                                xml.Placemark {
                                    xml.name coordenroutephoto_instance.enroutePhotoName
                                    xml.visibility "0"
                                    xml.styleUrl "#${STYLE_ENROUTE_PHOTO}"
                                    xml.Point {
                                        xml.coordinates "${coordenroutephoto_instance.lonMath()},${coordenroutephoto_instance.latMath()},0"
                                    }
                                }
                            }
                        }
                    }
                }
                if (routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
                    List coordenroutecanvas_instances = CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])
                    if (coordenroutecanvas_instances) {
                        xml.Folder {
                            xml.name Defs.ROUTEEXPORT_CANVAS
                            xml.visibility "0"
                            for (CoordEnrouteCanvas coordenroutecanvas_instance in coordenroutecanvas_instances) {
                                xml.Placemark {
                                    xml.name coordenroutecanvas_instance.enrouteCanvasSign.canvasName
                                    xml.visibility "0"
                                    xml.styleUrl "#${STYLE_ENROUTE_CANVAS}${coordenroutecanvas_instance.enrouteCanvasSign.canvasName}"
                                    xml.Point {
                                        xml.coordinates "${coordenroutecanvas_instance.lonMath()},${coordenroutecanvas_instance.latMath()},0"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    private String getYesNo(boolean boolValue)
    {
        if (boolValue) {
            return "yes"
        }
        return "no"
    }
    
    //--------------------------------------------------------------------------
    private boolean kmz_track(Test testInstance, boolean isPrint, MarkupBuilder xml)
    // Return true: data found
    {
        printstart "kmz_track"
        
        boolean found = false
        List track_points = []
        if (testInstance.IsLoggerData()) {
            println "Get track points from '${testInstance.loggerDataStartUtc}' to '${testInstance.loggerDataEndUtc}'"
            track_points = testInstance.GetTrackPoints(testInstance.loggerDataStartUtc, testInstance.loggerDataEndUtc).trackPoints 
        }
        if (track_points) {
            println "Write track points"
            Route route_instance = testInstance.task.flighttest.route
            boolean observationsign_used = testInstance.task.flighttest.IsObservationSignUsed()
            // cache calc results
            List calc_results = []
            if (testInstance.IsLoggerResult()) {
                for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'utc'])) {
                    String title_code = ""
                    CoordType coord_type = CoordType.UNKNOWN
                    int title_number = 1
                    boolean is_runway = false
                    if (calcresult_instance.coordTitle) {
                        if (isPrint) {
                            title_code = calcresult_instance.coordTitle.titlePrintCode()
                        } else {
                            title_code = calcresult_instance.coordTitle.titleCode()
                        }
                        coord_type = calcresult_instance.coordTitle.type
                        title_number = calcresult_instance.coordTitle.number
                        is_runway = calcresult_instance.coordTitle.type.IsRunwayCoord()
                    }
                    Map new_calc_result = [utc: calcresult_instance.utc, 
                                           latitude: calcresult_instance.latitude,
                                           longitude: calcresult_instance.longitude,
                                           altitude: calcresult_instance.altitude,
                                           titleCode: title_code,
                                           coordType: coord_type,
                                           titleNumber: title_number,
                                           gateNotFound: calcresult_instance.gateNotFound,
                                           gateMissed: calcresult_instance.gateMissed,
                                           gateFlyBy: calcresult_instance.gateFlyBy,
                                           noGateMissed: calcresult_instance.noGateMissed,
                                           badTurn: calcresult_instance.badTurn,
                                           noBadTurn: calcresult_instance.noBadTurn,
                                           badCourse: calcresult_instance.badCourse,
                                           badCourseSeconds: calcresult_instance.badCourseSeconds,
                                           noBadCourse: calcresult_instance.noBadCourse,
                                           judgeDisabled: calcresult_instance.judgeDisabled,
                                           hide: calcresult_instance.hide,
                                           runway: is_runway
                                          ]
                    calc_results += new_calc_result
                }
            }
            
            // write xml
            xml.Placemark {
                xml.styleUrl "#${STYLE_TRACK}"
                xml.name "${getMsg('fc.kmz.crew',isPrint)} ${testInstance.crew.startNum}"
                String track_coordinates = ""
                for (Map p in track_points) {
                    if (track_coordinates) {
                        track_coordinates += " "
                    }
                    track_coordinates += "${p.longitude},${p.latitude},${p.altitude / KmlService.ftPerMeter}"
                }
                xml.LineString {
                    xml.coordinates track_coordinates
                }
            }
                   
            found = true
        }
        
        printdone ""
        return found
    }
    
	//--------------------------------------------------------------------------
	void WriteLine(BufferedWriter fileWriter, String wrLine)
	{
		fileWriter.write(wrLine)
		fileWriter.newLine()
	}
	
	//--------------------------------------------------------------------------
	boolean Download(String downloadFileName, String returnFileName, OutputStream outputStream)
	{
		printstart "Download $downloadFileName -> $returnFileName"
		try {
			File download_file = new File(downloadFileName)
			outputStream.write(download_file.getBytes())
			outputStream.flush()
		} catch (Exception e) {
			printerror e.getMessage()
			return false
		}
		printdone ""
		return true
	}
	
    //--------------------------------------------------------------------------
    void DeleteFile(String fileName)
    {
        printstart "Delete '$fileName'"
        if (fileName.startsWith(GPXDATA)) {
            if (BootStrap.tempData.RemoveData(fileName)) {
                printdone ""
            } else {
                printerror ""
            }
        } else {
            String file_name = fileName
            if (fileName.startsWith(Defs.ROOT_FOLDER_GPXUPLOAD)) {
                file_name = servletContext.getRealPath("/") + fileName
            }
            File file = new File(file_name)
            if (file.delete()) {
                printdone ""
            } else {
                printerror ""
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private String getMsg(String code, List args, boolean isPrint)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        String lang = session_obj.showLanguage
        if (isPrint) {
            lang = session_obj.printLanguage
        }
        if (args) {
            return messageSource.getMessage(code, args.toArray(), new Locale(lang))
        } else {
            return messageSource.getMessage(code, null, new Locale(lang))
        }
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code, boolean isPrint)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        String lang = session_obj.showLanguage
        if (isPrint) {
            lang = session_obj.printLanguage
        }
        return messageSource.getMessage(code, null, new Locale(lang))
    }
    
	//--------------------------------------------------------------------------
	void printstart(out)
	{
		logService.printstart out
	}

	//--------------------------------------------------------------------------
	void printerror(out)
	{
		if (out) {
			logService.printend "Error: $out"
		} else {
			logService.printend "Error."
		}
	}

	//--------------------------------------------------------------------------
	void printdone(out)
	{
		if (out) {
			logService.printend "Done: $out"
		} else {
			logService.printend "Done."
		}
	}

	//--------------------------------------------------------------------------
	void print(out)
	{
		logService.print out
	}

	//--------------------------------------------------------------------------
	void println(out)
	{
		logService.println out
	}
}
