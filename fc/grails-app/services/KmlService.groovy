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
    
    final static String STYLE_TRACK = "TrackStyle"
    final static String STYLE_TRACK_COLOR = "ddddddff"
    final static String STYLE_TRACK_WIDTH = "1"
    
    final static String STYLE_ENROUTE_PHOTO = "EnroutePhotoStyle"
    final static String STYLE_ENROUTE_CANVAS = "EnrouteCanvasStyle-"
    final static String STYLE_ENROUTE_LABELCOLOR = "ffffffff"
    final static String STYLE_ENROUTE_PHOTO_SCALE = "1.2"
    final static String STYLE_ENROUTE_CANVAS_SCALE = "0.7"
    final static String TILT_ENROUTE = "60" // Betrachtungswinkel in Grad
    final static String RANGE_ENROUTE = "1000" // Betrachtungsentfernung in m
    
    final static String GPXDATA = "GPXDATA"
	
    //--------------------------------------------------------------------------
    Map ConvertRoute2KMZ(Route routeInstance, String webRootDir, String kmzFileName, boolean isPrint, boolean wrEnrouteSign)
    {
        printstart "ConvertRoute2KMZ ${routeInstance.name()} -> ${webRootDir + kmzFileName}"
        
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
            kml_writer = kml_file.newWriter()
        }
        
        MarkupBuilder xml = new MarkupBuilder(kml_writer)
        kml_writer.writeLine(XMLHEADER)
        xml.kml(xmlns:NAMESPACE_XMLNS,'xmlns:gx':NAMESPACE_XMLNS_GX,'xmlns:kml':NAMESPACE_XMLNS_KML,'xmlns:atom':NAMESPACE_XMLNS_ATOM) {
            xml.Document {
                KMZStyles(xml)
                KMZRoute(routeInstance, null, isPrint, wrEnrouteSign, xml)
            }
        }
        kml_writer.close()
        
        if (kml_file_name.startsWith(GPXDATA)) {
            BootStrap.tempData.AddData(kml_file_name, kml_data.toCharArray())
            printdone "${kml_data.size()} bytes"
            // TODO: WriteKMZ
        } else {
            printdone "${kml_file.size()} bytes"
            WriteKMZ(webRootDir, kmzFileName, kml_file_name)
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
        
        printstart "ConvertTest2KMZ '${testInstance.aflosStartNum}:${testInstance.crew.name}' -> ${webRootDir + kmzFileName}"
        
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
            kml_writer = kml_file.newWriter()
        }
        
        MarkupBuilder xml = new MarkupBuilder(kml_writer)
        kml_writer.writeLine(XMLHEADER)
        xml.kml(xmlns:NAMESPACE_XMLNS,'xmlns:gx':NAMESPACE_XMLNS_GX,'xmlns:kml':NAMESPACE_XMLNS_KML,'xmlns:atom':NAMESPACE_XMLNS_ATOM) {
            xml.Document {
                KMZStyles(xml)
                KMZRoute(route_instance, testInstance, isPrint, wrEnrouteSign, xml)
                xml.Folder {
                    xml.name getMsg('fc.kmz.flights',isPrint)
                    found_track = KMZTrack(testInstance, testInstance.aflosStartNum, isPrint, xml)
                }
            }
        }
        kml_writer.close()
        
        if (kml_file_name.startsWith(GPXDATA)) {
            BootStrap.tempData.AddData(kml_file_name, kml_data.toCharArray())
            printdone "${kml_data.size()} bytes"
            // TODO: WriteKMZ
        } else {
            printdone "${kml_file.size()} bytes"
            WriteKMZ(webRootDir, kmzFileName, kml_file_name)
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
        
        printstart "ConvertTask2KMZ '${taskInstance.name()}' -> ${webRootDir + kmzFileName}"
        
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
            kml_writer = kml_file.newWriter()
        }
        
        MarkupBuilder xml = new MarkupBuilder(kml_writer)
        kml_writer.writeLine(XMLHEADER)
        xml.kml(xmlns:NAMESPACE_XMLNS,'xmlns:gx':NAMESPACE_XMLNS_GX,'xmlns:kml':NAMESPACE_XMLNS_KML,'xmlns:atom':NAMESPACE_XMLNS_ATOM) {
            xml.Document {
                KMZStyles(xml)
                KMZRoute(route_instance, null, isPrint, wrEnrouteSign, xml)
                xml.Folder {
                    xml.name getMsg('fc.kmz.flights',isPrint)
                    for (Test test_instance in Test.findAllByTask(taskInstance,[sort:"viewpos"])) {
                        if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                            if (test_instance.IsFlightTestRun()) {
                                if (KMZTrack(test_instance, test_instance.aflosStartNum, isPrint, xml)) {
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
            // TODO: WriteKMZ
        } else {
            printdone "${kml_file.size()} bytes"
            WriteKMZ(webRootDir, kmzFileName, kml_file_name)
            DeleteFile(webRootDir + kml_file_name)
        }
        
        printdone ""
        
        return [ok:true, track:found_track]
    }
    
    //--------------------------------------------------------------------------
    private void KMZStyles(MarkupBuilder xml)
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
    private void SetRouteProperties(MarkupBuilder xml)
    {
        xml.extrude "1"    // extends the line down to the ground
        xml.tessellate "1" // breaks the line up into smaller chunks
    }
    
    //--------------------------------------------------------------------------
    private void WriteKMZ(String webRootDir, String kmzFileName, String kmlFileName)
    {
        ZipOutputStream kmz_zip_output_stream = new ZipOutputStream(new FileOutputStream(webRootDir + kmzFileName))
        
        WriteFile2KMZ(kmz_zip_output_stream, "doc.kml", webRootDir, kmlFileName)
        kmz_zip_output_stream.putNextEntry(new ZipEntry("files/"))
        WriteFile2KMZ(kmz_zip_output_stream, "files/photo.png", webRootDir, "GM_Utils/Icons/photo.png")
        WriteFile2KMZ(kmz_zip_output_stream, "files/airport.png", webRootDir, "GM_Utils/Icons/airport.png")
        WriteFile2KMZ(kmz_zip_output_stream, "files/start.png", webRootDir, "GM_Utils/Icons/start.png")
        WriteFile2KMZ(kmz_zip_output_stream, "files/finish.png", webRootDir, "GM_Utils/Icons/finish.png")
        EnrouteCanvasSign.each { enroute_canvas_sign ->
            if (enroute_canvas_sign.imageName) {
                WriteFile2KMZ(kmz_zip_output_stream, "files/${enroute_canvas_sign.imageJpgShortName}", webRootDir, enroute_canvas_sign.imageJpgName)
            }
        }

        kmz_zip_output_stream.close()
    }
    
    //--------------------------------------------------------------------------
    private void WriteFile2KMZ(ZipOutputStream zipOutputStream, String zipFileName, String webRootDir, String fileName)
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
    private void KMZRoute(Route routeInstance, Test testInstance, boolean isPrint, boolean wrEnrouteSign, MarkupBuilder xml)
    {
        printstart "KMZRoute Print:$isPrint wrEnrouteSign:$wrEnrouteSign"
        
        Map contest_map_rect = [:]
        BigDecimal center_latitude = null
        BigDecimal center_longitude = null
        BigDecimal min_latitude = null
        BigDecimal min_longitude = null
        BigDecimal max_latitude = null
        BigDecimal max_longitude = null
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
            if (coordroute_instance.type.IsContestMapCoord()) {
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

        xml.Folder {
            xml.name "${getMsg('fc.kmz.route',isPrint)} '${routeInstance.name().encodeAsHTML()}'"
            
            // legs
            xml.Folder {
                xml.name getMsg('fc.kmz.legs',isPrint)
                long restart_id = 0
                xml.Placemark {
                    xml.styleUrl "#${STYLE_ROUTELEG}"
                    if (routeInstance.IsIntermediateSP()) {
                        xml.name "1"
                    } else {
                        xml.name routeInstance.name().encodeAsHTML()
                    }
                    xml.LineString {
                        SetRouteProperties(xml)
                        String route_coordinates = ""
                        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
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
                        xml.coordinates route_coordinates
                    }
                }
                if (restart_id != 0) {
                    xml.Placemark {
                        xml.styleUrl "#${STYLE_ROUTELEG}"
                        xml.name "2"
                        boolean run = false
                        xml.LineString {
                            SetRouteProperties(xml)
                            String route_coordinates = ""
                            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                                if (run) {
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
            for (RouteLegCoord routeleg_instance in RouteLegCoord.findAllByRoute(routeInstance,[sort:'id'])) {
                if (routeleg_instance.IsProcedureTurn()) {
                    BigDecimal course_change = AviationMath.courseChange(routeleg_instance.turnTrueTrack,routeleg_instance.testTrueTrack())
                    if (course_change.abs() >= 90) {
                        procedureturn_num++
                    }
                }
            }
            if (procedureturn_num > 0) {
                xml.Folder {
                    xml.name getMsg('fc.kmz.procedureturns',isPrint)
                    CoordRoute last_coordroute_instance = null
                    CoordRoute last_last_coordroute_instance = null
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                        if (coordroute_instance.planProcedureTurn && last_coordroute_instance && last_last_coordroute_instance && last_coordroute_instance.type.IsProcedureTurnCoord()) {
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
                                xml.name last_coordroute_instance.titleShortMap(isPrint)
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
                List enroute_points = RoutePointsTools.GetShowPointsRoute(routeInstance, null, true, messageSource) // true - showEnrouteSign
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
                                    xml.name coordroute_instance.titleShortMap(isPrint)
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
                                            SetRouteProperties(xml)
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
                                    xml.name coordroute_instance.titleShortMap(isPrint)
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
                                            SetRouteProperties(xml)
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
                                    xml.name coordroute_instance.titleShortMap(isPrint)
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
                                            SetRouteProperties(xml)
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
                                    xml.name coordroute_instance.titleShortMap(isPrint)
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
                                            SetRouteProperties(xml)
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
                                    xml.name coordroute_instance.titleShortMap(isPrint)
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
                                            SetRouteProperties(xml)
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
                                    xml.name coordroute_instance.titleShortMap(isPrint)
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
                                            SetRouteProperties(xml)
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
                                xml.name last_coordroute_instance.titleShortMap(isPrint)
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
                                        SetRouteProperties(xml)
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
                                Map gate = AviationMath.getGate(
                                    last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                                    enroute_points[route_point_pos].latcenter,enroute_points[route_point_pos].loncenter,
                                    last_coordroute_instance.gatewidth2
                                )
                                if (enroute_points[route_point_pos].enroutephoto) {
                                    xml.Placemark {
                                        xml.name enroute_points[route_point_pos].name
                                        xml.styleUrl "#${STYLE_ENROUTE_PHOTO}"
                                        xml.LookAt {
                                            xml.longitude enroute_points[route_point_pos].loncenter
                                            xml.latitude enroute_points[route_point_pos].latcenter
                                            xml.altitude "0"
                                            xml.heading "${gate.gateTrack}"
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
                                            xml.longitude enroute_points[route_point_pos].loncenter
                                            xml.latitude enroute_points[route_point_pos].latcenter
                                            xml.altitude "0"
                                            xml.heading "${gate.gateTrack}"
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
                            boolean show_curved_point = routeInstance.contest.printStyle.contains('--route') && routeInstance.contest.printStyle.contains('show-curved-points')
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
                                    } else {
                                        xml.styleUrl "#${STYLE_GATE}"
                                    }
                                    xml.name coordroute_instance.titleShortMap(isPrint)
                                    xml.LookAt {
                                        xml.longitude coordroute_instance.lonMath()
                                        xml.latitude coordroute_instance.latMath()
                                        xml.altitude "0"
                                        xml.heading "${gate.gateTrack}"
                                        xml.tilt TILT_GATE
                                        xml.range RANGE_GATE
                                    }
                                    xml.MultiGeometry {
                                        xml.LineString {
                                            SetRouteProperties(xml)
                                            xml.coordinates "${gate.coordLeft.lon},${gate.coordLeft.lat},${altitude_meter2} ${gate.coordRight.lon},${gate.coordRight.lat},${altitude_meter2}"
                                        }
                                        if (coordroute_instance.type.IsEnrouteFinishCoord()) {
                                            xml.Point {
                                                xml.coordinates "${coordroute_instance.lonMath()},${coordroute_instance.latMath()},${altitude_meter2}"
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
        
            // CoordEnroutePhoto
            /*
            if (wr_enroutesign && routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
                xml.Folder {
                    xml.name getMsg('fc.kmz.enroutephotos',isPrint)
                    for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                        xml.Placemark {
                            xml.name coordenroutephoto_instance.enroutePhotoName
                            xml.styleUrl "#${STYLE_ENROUTE_PHOTO}"
                            xml.LookAt {
                                xml.longitude coordenroutephoto_instance.lonMath()
                                xml.latitude coordenroutephoto_instance.latMath()
                                xml.altitude "0"
                                xml.heading "0"
                                xml.tilt TILT_ENROUTE
                                xml.range RANGE_ENROUTE
                            } 
                            xml.Point {
                                xml.coordinates "${coordenroutephoto_instance.lonMath()},${coordenroutephoto_instance.latMath()},0"
                            }
                            //xml.description "${getMsg('fc.kmz.distance.tp',[coordenroutephoto_instance.titleCode(isPrint)],isPrint)}: ${FcMath.DistanceStr(coordenroutephoto_instance.enrouteDistance)}${getMsg('fc.mile',isPrint)},\n ${getMsg('fc.kmz.distance.route',isPrint)}: ${coordenroutephoto_instance.GetPrintEnrouteOrthogonalDistance()}"
                        }
                    }
                }
            }
            
            // CoordEnrouteCanvas
            if (wr_enroutesign && routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
                xml.Folder {
                    xml.name getMsg('fc.kmz.enroutecanvas',isPrint)
                    for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                        xml.Placemark {
                            xml.name coordenroutecanvas_instance.enrouteCanvasSign.canvasName
                            xml.styleUrl "#${STYLE_ENROUTE_CANVAS}${coordenroutecanvas_instance.enrouteCanvasSign.canvasName}"
                            xml.LookAt {
                                xml.longitude coordenroutecanvas_instance.lonMath()
                                xml.latitude coordenroutecanvas_instance.latMath()
                                xml.altitude "0"
                                xml.heading "0"
                                xml.tilt TILT_ENROUTE
                                xml.range RANGE_ENROUTE
                            } 
                            xml.Point {
                                xml.coordinates "${coordenroutecanvas_instance.lonMath()},${coordenroutecanvas_instance.latMath()},0"
                            }
                            //xml.description "${getMsg('fc.kmz.distance.tp',[coordenroutecanvas_instance.titleCode(isPrint)],isPrint)}: ${FcMath.DistanceStr(coordenroutecanvas_instance.enrouteDistance)}${getMsg('fc.mile',isPrint)},\n ${getMsg('fc.kmz.distance.route',isPrint)}: ${coordenroutecanvas_instance.GetPrintEnrouteOrthogonalDistance()}"
                        }
                    }
                }
            }
            */
        }
        
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    private boolean KMZTrack(Test testInstance, int startNum, boolean isPrint, MarkupBuilder xml)
    // Return true: data found
    {
        printstart "KMZTrack"
        
        boolean found = false
        List track_points = []
        if (testInstance.IsLoggerData()) {
            println "Get track points from '${testInstance.loggerDataStartUtc}' to '${testInstance.loggerDataEndUtc}'"
            track_points = testInstance.GetTrackPoints(testInstance.loggerDataStartUtc, testInstance.loggerDataEndUtc) 
        } else {
            println "Get track points from AFLOS startnum $startNum"
            track_points = AflosTools.GetAflosCrewNamesTrackPoints(testInstance, startNum)
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
