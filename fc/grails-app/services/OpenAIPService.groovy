import java.util.Map
import groovy.json.JsonSlurper
import org.springframework.web.context.request.RequestContextHolder
import groovy.xml.*
import java.util.zip.*
import java.net.URLEncoder
import java.math.*

// https://docs.oracle.com/javase/8/docs/api/java/net/HttpURLConnection.html
// https://docs.openaip.net/

class OpenAIPService
{
    def messageSource
    def logService
    
    final static String ALL_DATA = "*"

	final static String XMLHEADER = "<?xml version='1.0' encoding='UTF-8'?>"
    
    // Airfields
    final static String AIRFIELD_ID_PREAFIX = "id_"
    final static String AIRFIELD_NAME = "name"
    final static String AIRFIELD_ICAO = "icao"
    final static String AIRFIELD_TYPE = "type"
    final static String AIRFIELD_TYPE_GLIDING_SMALL = "gliding-small"
    final static String AIRFIELD_TYPE_AF_CIVIL_SMALL = "af-civil-small"
    final static String AIRFIELD_TYPE_APT_MIL_CIVIL_SMALL = "apt-mil-civil-small"
    final static String AIRFIELD_TYPE_AD_MIL_SMALL = "ad-mil-small"
    final static String AIRFIELD_TYPE_LIGHT_AIRCRAFT_SMALL = "light-aircraft-small"
    final static String AIRFIELD_TYPE_AD_CLOSED_SMALL = "ad-closed-small"
    final static String AIRFIELD_TYPE_APT_SMALL = "apt-small"
    final static String AIRFIELD_TYPE_AF_WATER_LARGE = "af-water-large"
    final static String AIRFIELD_TYPE_REPORTING_POINT_COMPULSORY_SMALL = "reporting-point-compulsory-small"
    final static String AIRFIELD_TYPE_REPORTING_POINT_REQUEST_SMALL = "reporting-point-request-small"
    final static String AIRFIELD_TYPE_OTHER = "other-"
    final static String AIRFIELD_RUNWAY = "runway"
    final static String AIRFIELD_RUNWAY_PAVED_SMALL = "paved-small"
    final static String AIRFIELD_RUNWAY_UNPAVED_SMALL = "unpaved-small"
    final static String AIRFIELD_RUNWAY_OTHER = "other-"
    final static String AIRFIELD_HEADING = "heading"
    final static String AIRFIELD_WKT = "wkt"
    final static String AIRFIELD_DATA_SEPARATOR = ","
    final static String AIRFIELD_DATA_KEY_VALUE_SEPARATOR = ":"

    // CSV files
    final static String CSV_DELIMITER = ";"
    final static String CSV_LINESEPARATOR = "\r\n"
    final static String CSV_AIRPORT_TYPE = "type"
    final static String CSV_AIRPORT_RUNWAY = "runway"
    final static String CSV_AIRPORT_HEADING = "heading"
    final static String CSV_AIRPORT_NAME = "name"
    final static String CSV_AIRPORT_ICAO = "icao"
    final static String CSV_AIRPORT_WKT = "wkt"
	
    //--------------------------------------------------------------------------
    Map test(Map params)
    {
		//printstart "getairports"
        //Map ret = call_rest("airports?pos=45.243432%2C9.23423&dist=50000", "GET", 200, "", ALL_DATA)
		printstart "getairspaces"
		Map ret = call_rest("airspaces?search=CTR%20Schleswig", "GET", 200, "", ALL_DATA)
		if (ret.ok && ret.data) {
			Map ret2 = ['message':'done']
			printdone ret2
			return ret2
		}
		Map ret3 = [error:true, 'message':ret.errorMsg]
		printerror ret3
		return ret3
    }
    

    //--------------------------------------------------------------------------
    Map WriteAirspaces2KMZ(Route routeInstance, String webRootDir, String kmzFileName, boolean isPrint, boolean isHidden)
    {
        printstart "WriteAirspaces2KMZ ${routeInstance.GetName(isPrint)} -> ${webRootDir + kmzFileName}"
        
        String map_folder_name = webRootDir + Defs.ROOT_FOLDER_MAP + "\\" + routeInstance.contest.contestUUID
        File map_folder = new File(map_folder_name)
        if (!map_folder.exists()) {
            map_folder.mkdir()
        }
        
        String airspaces_filename = map_folder_name + "\\" + Defs.MAP_AIRSPACES_FILE
        String kml_file_name = kmzFileName + ".kml"
        
        printstart "Generate '${webRootDir + kml_file_name}'"
        
		boolean all_airspaces_written = true
		String missing_airspaces = ""
        List new_airspaces = []
		
        BufferedWriter kml_writer = null
        CharArrayWriter kml_data = null
        File kml_file = null
		kml_file = new File(webRootDir + kml_file_name)
		kml_writer = kml_file.newWriter("UTF-8")
        
        MarkupBuilder xml = new MarkupBuilder(kml_writer)
        kml_writer.writeLine(XMLHEADER)
		xml.kml(xmlns:"http://www.opengis.net/kml/2.2",'xmlns:gx':"http://www.google.com/kml/ext/2.2",'xmlns:kml':"http://www.opengis.net/kml/2.2",'xmlns:atom':"http://www.w3.org/2005/Atom") {
            xml.Document {
				xml.name "Airspaces"
				xml.Style(id:"airspace") {
					xml.IconStyle {
						xml.scale 4.24403e-38
					}
					xml.LineStyle {
                        if (isHidden) {
                            xml.color "ffff7777"
                        } else {
                            xml.color "ffff00ff"
                        }
						xml.width 1.5
					}
					xml.PolyStyle {
                        if (isHidden) {
                            xml.color "40ff7777"
                        } else {
                            xml.color "40ff00ff"
                        }
					}
				}
				for (String layer in routeInstance.contestMapAirspacesLayer2.split("\n")) {
					if (layer && layer.trim()) {
						String airspace_name = layer.trim()
                        boolean ignore_line = false
                        if (isHidden) {
                            ignore_line = true
                            if (airspace_name.startsWith(Defs.IGNORE_LINE)) {
                                ignore_line = false
                            }
                            airspace_name = airspace_name.substring(Defs.IGNORE_LINE.size()).trim()
                        } else {
                            if (airspace_name.startsWith(Defs.IGNORE_LINE)) {
                                ignore_line = true
                            }
                        }
                        if (!ignore_line) {
                            String airspace_text = ""
                            boolean first_style = true
                            for (String airspace_style in Tools.Split(airspace_name, OsmPrintMapService.AIRSPACE_LAYER_STYLE_SEPARATOR)) {
                                List airspace_style_values = Tools.Split(airspace_style.trim(), OsmPrintMapService.AIRSPACE_LAYER_STYLE_KEY_VALUE_SEPARATOR)
                                if (airspace_style_values.size() == 1) {
                                    if (first_style) {
                                        airspace_name = airspace_style_values[0].trim()
                                    } else if (airspace_text) {
                                        airspace_text += OsmPrintMapService.AIRSPACE_LAYER_STYLE_SEPARATOR + airspace_style
                                    }
                                } else if (airspace_style_values.size() == 2) {
                                    switch (airspace_style_values[0].trim()) {
                                        case 'text': 
                                            airspace_text = airspace_style_values[1]
                                            break
                                    }
                                }
                                first_style = false
                            }
                            if (!airspace_text) {
                                airspace_text = airspace_name
                            }   
                            airspace_text = airspace_text.trim()
                            
                            // get airspace from cache
                            Map ret = get_airspace_from_cache(airspaces_filename, airspace_name) // get airspace from cache
                            if (ret.ok && ret.coordinates) {
                                write_airspace(xml, airspace_name, airspace_text, "airspace", ret.coordinates)
                            } else {
                                // get airspace from OpenAIP
                                ret = [:]
                                if (airspace_name.startsWith(OsmPrintMapService.AIRSPACE_LAYER_ID_PREAFIX)) {
                                    String search_id = airspace_name.substring(3)
                                    println "Get airspace $search_id from OpenAIP"
                                    ret = call_rest("airspaces?id=${search_id}", "GET", 200, "", "items")
                                } else {
                                    String search_name = URLEncoder.encode(airspace_name, "UTF-8")
                                    println "Get airspace '$search_name' from OpenAIP"
                                    ret = call_rest("airspaces?search=${search_name}", "GET", 200, "", "items")
                                }
                                if (ret.ok && ret.data) {
                                    String airspace_coordinates = ret.data.geometry.coordinates.toString()
                                    airspace_coordinates = airspace_coordinates.replace(', ',',').replace('],[',' ').replace('[','').replace(']]]]','')
                                    write_airspace(xml, airspace_name, airspace_text, "airspace", airspace_coordinates)
                                    new_airspaces += [airspaceName:airspace_name, airspaceText:airspace_text, airspaceStyle:"airspace", airspaceCoordinates:airspace_coordinates]
                                } else {
                                    all_airspaces_written = false
                                    if (missing_airspaces) {
                                        missing_airspaces += ", "
                                    }
                                    missing_airspaces += airspace_name
                                }
                            }
                        }
					}
				}
			}
		}
        kml_writer.close()
        
		printdone "${kml_file.size()} bytes"
		write_kmz(webRootDir, kmzFileName, kml_file_name)
		DeleteFile(webRootDir + kml_file_name)
        
        if (new_airspaces) {
            write_airspaces_to_cache(airspaces_filename, new_airspaces)
        }

        printdone ""
        
        return [ok:all_airspaces_written, missingAirspaces:missing_airspaces]
    }

    //--------------------------------------------------------------------------
    private void write_airspaces_to_cache(String airspacesFilename, List newAirspaces) // TODO
    {
        String new_filename = airspacesFilename + ".new.kml"
        
		def kml_file = new File(new_filename)
		def kml_writer = kml_file.newWriter("UTF-8")
        
        MarkupBuilder xml = new MarkupBuilder(kml_writer)
        kml_writer.writeLine(XMLHEADER)
		xml.kml(xmlns:"http://www.opengis.net/kml/2.2",'xmlns:gx':"http://www.google.com/kml/ext/2.2",'xmlns:kml':"http://www.opengis.net/kml/2.2",'xmlns:atom':"http://www.w3.org/2005/Atom") {
            xml.Document {
				xml.name "Airspaces"
				xml.Style(id:"airspace") {
					xml.IconStyle {
						xml.scale 4.24403e-38
					}
                    boolean isHidden = false
					xml.LineStyle {
                        if (isHidden) {
                            xml.color "ffff7777"
                        } else {
                            xml.color "ffff00ff"
                        }
						xml.width 1.5
					}
					xml.PolyStyle {
                        if (isHidden) {
                            xml.color "40ff7777"
                        } else {
                            xml.color "40ff00ff"
                        }
					}
				}
                
                File kml_read_file = new File(airspacesFilename)
                if (kml_read_file.exists()) {
                    def kml_reader = new FileReader(kml_read_file)
                    def read_kml = new XmlParser().parse(kml_reader)
                    def folders = read_kml.Document.Folder
                    for (folder in folders) {
                        write_airspace(xml, folder.name[0].text(), folder.description[0].text(), "airspace", folder.Placemark.Polygon.outerBoundaryIs.LinearRing.coordinates[0].text())
                    }
                    kml_reader.close()
                }
                
                for (Map new_airspace in newAirspaces) {
                    write_airspace(xml, new_airspace.airspaceName, new_airspace.airspaceText, new_airspace.airspaceStyle, new_airspace.airspaceCoordinates)
                }
			}
		}
        kml_writer.close()
        
        DeleteFile(airspacesFilename)
        kml_file.renameTo(new File(airspacesFilename))
    }
    
    //--------------------------------------------------------------------------
    private void write_airspace(MarkupBuilder xml, String airspaceName, String airspaceText, String airspaceStyle, String airspaceCoordinates)
    {
		xml.Folder {
			xml.name airspaceName
            xml.description airspaceText
			xml.Placemark {
				xml.name airspaceName
                xml.description airspaceText
				xml.styleUrl "#${airspaceStyle}"
				xml.Polygon {
					xml.extrude 1
					xml.tessellate 1
					xml.altitudeMode "clampToGround" // "absolute"
					xml.outerBoundaryIs {
						xml.LinearRing {
							xml.coordinates airspaceCoordinates
						}
					}
				}
			}
		}
	}
	
    //--------------------------------------------------------------------------
    private void write_kmz(String webRootDir, String kmzFileName, String kmlFileName)
    {
        ZipOutputStream kmz_zip_output_stream = new ZipOutputStream(new FileOutputStream(webRootDir + kmzFileName))
        
        write_file_to_kmz(kmz_zip_output_stream, "doc.kml", webRootDir, kmlFileName)
        kmz_zip_output_stream.putNextEntry(new ZipEntry("files/"))

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
    private Map get_airspace_from_cache(String airspacesFilename, String airspaceName)
    {
        File kml_file = new File(airspacesFilename)
        if (kml_file.exists()) {
            def kml_reader = new FileReader(kml_file)

            def kml = new XmlParser().parse(kml_reader)
            def folders = kml.Document.Folder
            
            for (folder in folders) {
                if (folder.name[0].text() == airspaceName) {
                    return [ok:true, coordinates:folder.Placemark.Polygon.outerBoundaryIs.LinearRing.coordinates[0].text()]
                }
            }
            kml_reader.close()
        }
        return [ok:false, coordinates:""]
    }
    
    //--------------------------------------------------------------------------
    Map GetAirspacesAirportarea(Route routeInstance, String contestMapCenterPoints)
    {
        printstart "GetAirspacesAirportarea ${routeInstance.name()} ${routeInstance.contestMapAirspacesLowerLimit}ft"

        boolean ok = false
        int airspaces_num = 0
        
        Map airportarea = get_airportarea(routeInstance, contestMapCenterPoints, false)
        if (airportarea) {
            println "Center (Lat Lon): ${airportarea.centerLatitude} ${airportarea.centerLongitude}, Distance ${airportarea.airspaceDistance}m"
            
            Map ret = call_rest("airspaces?pos=${airportarea.centerLatitude},${airportarea.centerLongitude}&dist=${airportarea.airspaceDistance}", "GET", 200, "", "items")
            if (ret.ok && ret.data) {
                ok = true
                String airspaces = ""
                for (Map d in ret.data) {
                    if (is_airspace(d, routeInstance.contestMapAirspacesLowerLimit)) {
                        if (airspaces) {
                            airspaces += "\n"
                        }
                        if (is_hidden_airspace(d)) {
                            airspaces += "#"
                        }
                        airspaces += "${OsmPrintMapService.AIRSPACE_LAYER_ID_PREAFIX}${d._id}"
                        airspaces += get_airspace_details(d)
                        airspaces_num++
                    }
                }
                routeInstance.contestMapAirspacesLayer2 = airspaces
                routeInstance.save()
                printdone ""
            } else {
                printerror ""
            }
        } else {
            printerror ""
        }
        
        return [ok:ok, airspacesnum:airspaces_num]
    }
    
    //--------------------------------------------------------------------------
    private boolean is_airspace(Map d, int contestMapAirspacesLowerLimit)
    {
        if (d.lowerLimit.value == 0) {
            return true
        }
        if (d.lowerLimit.unit == 1 && d.lowerLimit.value < contestMapAirspacesLowerLimit) {
            return true
        }
        return false
    }
    
    //--------------------------------------------------------------------------
    private boolean is_hidden_airspace(Map d)
    {
        String ignore_airspaces = BootStrap.global.GetOpenAIPIgnoreAirspacesStartsWith()
        if (ignore_airspaces) {
            for (String ignore_airspace in ignore_airspaces.split(',')) {
                String ignore_airspace2 = ignore_airspace.trim()
                if (d.name.startsWith(ignore_airspace2)) {
                    return true
                }
            }
        }
        
        boolean is_hidden = false
        if (d.icaoClass == 4 && d.type == 0) { // AREA (de)
            is_hidden = true
        }
        if (d.icaoClass == 6 && d.type == 10) { // FIR (it)
            is_hidden = true
        }
        if (d.icaoClass == 8 && d.type == 5) { // FMC (it)
            is_hidden = true
        }
        if (d.icaoClass == 8 && d.type == 10) { // FIR (fr)
            is_hidden = true
        }
        if (d.icaoClass == 8 && d.type == 33) { // FIS (de), SIV (fr)
            is_hidden = true
        }
        println "is_hidden_airspace $is_hidden $d.icaoClass $d.type $d.name"
        
        return is_hidden
    }
    
    //--------------------------------------------------------------------------
    private String get_airspace_details(Map d)
    {
        String name = d.name.replace("'","")
        String s = ",text:${name} ${get_airspace_limit(d.lowerLimit,d.upperLimit)}"
        if (false) { // Test
            s += " (icaoClass=${d.icaoClass} type=${d.type})"
        }
        if (d.icaoClass == 8 && d.type == 1) { // ED-R
            s += ",fillcolor:red,textcolor:red"
        } else if (d.icaoClass == 8 && d.type == 6) { // RMZ
            s += ",fillcolor:steelblue,textcolor:black"
        } else if (d.icaoClass == 3 && d.type == 4) { // CTR
            // nothing
        } else {
            s += ",fillcolor:gray,textcolor:black"
        }
        return s
    }
    
    //--------------------------------------------------------------------------
    private String get_airspace_limit(Map lowerLimit, Map upperLimit)
    {
        return "${get_airspace_limit(lowerLimit)}-${get_airspace_limit(upperLimit)}"
    }
    
    //--------------------------------------------------------------------------
    private String get_airspace_limit(Map limit)
    {
        switch (limit.unit) {
            case 1:
                if (limit.value == 0) {
                    return "GND"
                } else {
                    return "${limit.value}ft"
                }
            case 6:
                return "FL${limit.value}"
        }
        return "${limit.value}(${limit.unit})"
    }
    
    //--------------------------------------------------------------------------
    Map GetAirfieldsAirportarea(Route routeInstance, String contestMapCenterPoints)
    {
        printstart "GetAirfieldsAirportarea ${routeInstance.name()}"

        boolean ok = false
        int airfields_num = 0
        
        Map airportarea = get_airportarea(routeInstance, contestMapCenterPoints, false)
        if (airportarea) {
            println "Center (Lat Lon): ${airportarea.centerLatitude} ${airportarea.centerLongitude}, Distance ${airportarea.airspaceDistance}m"

            String airfields = ""
            
            // Airfields
            Map ret = call_rest("airports?pos=${airportarea.centerLatitude},${airportarea.centerLongitude}&dist=${airportarea.airspaceDistance}", "GET", 200, "", "items")
            if (ret.ok && ret.data) {
                ok = true
                
                for (Map d in ret.data) {
                    if (d.geometry.type == "Point") {
                        String airfield_type = ""
                        String runway_type = ""
                        String runway_heading = ""
                        if (d.runways) {
                            for (Map r in d.runways) {
                                if (r.mainRunway) {
                                    switch (d.type) {
                                        case 1:
                                            airfield_type = AIRFIELD_TYPE_GLIDING_SMALL
                                            break
                                        case 2:
                                            airfield_type = AIRFIELD_TYPE_AF_CIVIL_SMALL
                                            break
                                        case 3:
                                            airfield_type = AIRFIELD_TYPE_APT_MIL_CIVIL_SMALL
                                            break
                                        case 5:
                                            airfield_type = AIRFIELD_TYPE_AD_MIL_SMALL
                                            break
                                        case 6:
                                            airfield_type = AIRFIELD_TYPE_LIGHT_AIRCRAFT_SMALL
                                            break
                                        case 8:
                                            airfield_type = AIRFIELD_TYPE_AD_CLOSED_SMALL
                                            break
                                        case 9:
                                            airfield_type = AIRFIELD_TYPE_APT_SMALL
                                            break
                                        case 10:
                                            airfield_type = AIRFIELD_TYPE_AF_WATER_LARGE
                                            break
                                        default:
                                            airfield_type = AIRFIELD_TYPE_OTHER + d.type + '-' + r.surface.mainComposite
                                            break
                                    }
                                    switch (r.surface.mainComposite) { 
                                        case 0:
                                        case 1:
                                            runway_type = AIRFIELD_RUNWAY_PAVED_SMALL
                                            break
                                        case 2:
                                            runway_type = AIRFIELD_RUNWAY_UNPAVED_SMALL
                                            break
                                        default:    
                                            runway_type = AIRFIELD_RUNWAY_OTHER + r.surface.mainComposite
                                            break
                                    }
                                    runway_heading = r.trueHeading
                                    break
                                }
                            }
                        }
                        if (airfield_type) {
                            airfields_num++
                            if (airfields) {
                                airfields += "\n"
                            }
                            airfields += "${AIRFIELD_ID_PREAFIX}${d._id}"
                            airfields += ",${AIRFIELD_NAME}:${d.name}"
                            if (d.icaoCode) {
                                airfields += ",${AIRFIELD_ICAO}:${d.icaoCode}"
                            }
                            airfields += ",${AIRFIELD_TYPE}:${airfield_type}"
                            airfields += ",${AIRFIELD_RUNWAY}:${runway_type}"
                            airfields += ",${AIRFIELD_HEADING}:${runway_heading}"
                            airfields += ",${AIRFIELD_WKT}:POINT(${d.geometry.coordinates[0]} ${d.geometry.coordinates[1]})"
                        }
                    }
                }
            }
            
            // Reporting points
            ret = call_rest("reporting-points?pos=${airportarea.centerLatitude},${airportarea.centerLongitude}&dist=${airportarea.airspaceDistance}", "GET", 200, "", "items")
            if (ret.ok && ret.data) {
                for (Map d in ret.data) {
                    if (d.geometry.type == "Point") {
                        String point_type = ""
                        if (d.compulsory) {
                            point_type = AIRFIELD_TYPE_REPORTING_POINT_COMPULSORY_SMALL
                        } else {
                            point_type = AIRFIELD_TYPE_REPORTING_POINT_REQUEST_SMALL
                        }
                        if (point_type) {
                            airfields_num++
                            if (airfields) {
                                airfields += "\n"
                            }
                            airfields += "${AIRFIELD_ID_PREAFIX}${d._id}"
                            airfields += ",${AIRFIELD_NAME}:${d.name}"
                            airfields += ",${AIRFIELD_TYPE}:${point_type}"
                            airfields += ",${AIRFIELD_WKT}:POINT(${d.geometry.coordinates[0]} ${d.geometry.coordinates[1]})"
                        }
                    }
                }
            }
                
            if (airfields) {
                routeInstance.contestMapAirfieldsData = airfields
                routeInstance.save()
            }
            
            if (ok) {
                printdone "$airfields_num airfields found."
            } else {
                printerror ""
            }
            
        } else {
            printerror ""
        }
        
        return [ok:ok, airfieldsnum:airfields_num]
    }
    
    //--------------------------------------------------------------------------
    Map WriteAirfields2CSV(Route routeInstance, String webRootDir, String csvFileName, boolean isPrint, String contestMapCenterPoints, boolean openaipAirfields, boolean additionalAirfields)
    {
        printstart "WriteAirfields2CSV ${routeInstance.GetName(isPrint)} -> ${webRootDir + csvFileName}"
        
        boolean ok = false
        int airfields_num = 0

        Map airportarea = get_airportarea(routeInstance, contestMapCenterPoints, false)
        if (airportarea) {
            println "Center (Lat Lon): ${airportarea.centerLatitude} ${airportarea.centerLongitude}, Distance ${airportarea.airspaceDistance}m"
        }
            
        int line_id = 1
        
        File csv_file = new File(webRootDir + csvFileName)
        Writer csv_writer = csv_file.newWriter("UTF-8",false)
        csv_writer << "id"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_TYPE}"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_RUNWAY}"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_HEADING}"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_ICAO}"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_NAME}"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_WKT}"
        
        if (airportarea) {
            
            // Airfields and reporting points
            if (openaipAirfields) {
                for (String airfield_date in routeInstance.contestMapAirfieldsData.split("\n")) {
                    if (airfield_date && airfield_date.trim()) {
                        String airfield_date2 = airfield_date.trim()
                        if (!airfield_date2.startsWith(Defs.IGNORE_LINE)) {
                            String airfield_id = ""
                            String airfield_name = ""
                            String airfield_icao = ""
                            String airfield_symbol = ""
                            String runway_symbol = ""
                            String runway_heading = ""
                            String airfield_wkt = ""
                            for (String airfield_date3 in Tools.Split(airfield_date2, AIRFIELD_DATA_SEPARATOR)) {
                                List airfield_values = Tools.Split(airfield_date3.trim(), AIRFIELD_DATA_KEY_VALUE_SEPARATOR)
                                if (airfield_values.size() == 1) {
                                    airfield_id = airfield_values[0].trim().substring(AIRFIELD_ID_PREAFIX.size())
                                } else if (airfield_values.size() == 2) {
                                    switch (airfield_values[0].trim()) {
                                        case AIRFIELD_NAME:
                                            airfield_name = airfield_values[1].trim()
                                            break
                                        case AIRFIELD_ICAO:
                                            airfield_icao = airfield_values[1].trim()
                                            break
                                        case AIRFIELD_WKT:
                                            airfield_wkt = airfield_values[1].trim()
                                            break
                                        case AIRFIELD_HEADING:
                                            runway_heading = airfield_values[1].trim()
                                            break
                                        case AIRFIELD_TYPE:
                                            switch (airfield_values[1].trim()) {
                                                case AIRFIELD_TYPE_GLIDING_SMALL:
                                                    airfield_symbol = "gliding-small.svg"
                                                    break
                                                case AIRFIELD_TYPE_AF_CIVIL_SMALL:
                                                    airfield_symbol = "af_civil-small.svg"
                                                    break
                                                case AIRFIELD_TYPE_APT_MIL_CIVIL_SMALL:
                                                    airfield_symbol = "apt_mil_civil-small.svg"
                                                    break
                                                case AIRFIELD_TYPE_AD_MIL_SMALL:
                                                    airfield_symbol = "ad_mil-small.svg"
                                                    break
                                                case AIRFIELD_TYPE_LIGHT_AIRCRAFT_SMALL:
                                                    airfield_symbol = "light_aircraft-small.svg"
                                                    break
                                                case AIRFIELD_TYPE_AD_CLOSED_SMALL:
                                                    airfield_symbol = "ad_closed-small.svg"
                                                    break
                                                case AIRFIELD_TYPE_APT_SMALL:
                                                    airfield_symbol = "apt-small.svg"
                                                    break
                                                case AIRFIELD_TYPE_AF_WATER_LARGE:
                                                    airfield_symbol = "af_water-large.svg"
                                                    break
                                                case AIRFIELD_TYPE_REPORTING_POINT_COMPULSORY_SMALL:
                                                    airfield_symbol = "reporting_point_compulsory-small.svg"
                                                    break
                                                case AIRFIELD_TYPE_REPORTING_POINT_REQUEST_SMALL:
                                                    airfield_symbol = "reporting_point_request-small.svg"
                                                    break
                                                default:
                                                    airfield_symbol = airfield_values[1].trim()
                                                    break
                                            }
                                            break
                                        case AIRFIELD_RUNWAY:
                                            switch (airfield_values[1].trim()) {
                                                case AIRFIELD_RUNWAY_PAVED_SMALL:
                                                    runway_symbol = "runway_paved-small.svg"
                                                    break
                                                case AIRFIELD_RUNWAY_UNPAVED_SMALL:
                                                    runway_symbol = "runway_unpaved-small.svg"
                                                    break
                                                default:
                                                    runway_symbol = airfield_values[1].trim()
                                                    break
                                            }
                                            break
                                    }
                                }
                            }
                            if (airfield_id) {
                                ok = true
                                airfields_num++
                                csv_writer << CSV_LINESEPARATOR + airfield_id
                                csv_writer << CSV_DELIMITER + airfield_symbol
                                csv_writer << CSV_DELIMITER + runway_symbol
                                csv_writer << CSV_DELIMITER + runway_heading
                                csv_writer << CSV_DELIMITER + airfield_icao
                                csv_writer << CSV_DELIMITER + '"' + airfield_name + '"'
                                csv_writer << CSV_DELIMITER + airfield_wkt
                            }
                        }
                    }
                }
            }
            
            // Additional airfields
            if (additionalAirfields) {
                for (CoordMapObject coordmapobject_instance in CoordMapObject.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                    if (coordmapobject_instance.mapObjectType == MapObjectType.Airfield) {
                        String airport_symbol = "af_civil-small.svg"
                        if (coordmapobject_instance.mapObjectGliderAirfield) {
                            airport_symbol = "gliding-small.svg"
                        }
                        String runway_symbol = "runway_unpaved-small.svg"
                        if (coordmapobject_instance.mapObjectPavedAirfield) {
                            runway_symbol = "runway_paved-small.svg"
                        }
                        String runway_heading = coordmapobject_instance.gateDirection
                        ok = true
                        airfields_num++
                        csv_writer << CSV_LINESEPARATOR + line_id
                        csv_writer << CSV_DELIMITER + airport_symbol
                        csv_writer << CSV_DELIMITER + runway_symbol
                        csv_writer << CSV_DELIMITER + runway_heading
                        csv_writer << CSV_DELIMITER
                        csv_writer << CSV_DELIMITER + '"' + coordmapobject_instance.mapObjectText + '"'
                        csv_writer << ""
                        csv_writer << CSV_DELIMITER + "POINT(${coordmapobject_instance.lonMath()} ${coordmapobject_instance.latMath()})"
                        line_id++
                    }
                }
            }
        }
        
        csv_writer.close()

        if (ok) {
            printdone "${airfields_num} airports found."
        } else {
            printerror ""
        }
        
        return [ok:ok, airfieldsnum:airfields_num]
    }
        
    //--------------------------------------------------------------------------
    Map WriteAirfieldsOld2CSV(Route routeInstance, String webRootDir, String csvFileName, boolean isPrint, boolean checkAirport, String contestMapCenterPoints, boolean openaipAirfields, boolean additionalAirfields)
    {
        printstart "WriteAirfieldsOld2CSV ${routeInstance.GetName(isPrint)} -> ${webRootDir + csvFileName}"
        
        boolean ok = false
        int airfields_num = 0

        Map airportarea = get_airportarea(routeInstance, contestMapCenterPoints, checkAirport)
        if (airportarea) {
            println "Center (Lat Lon): ${airportarea.centerLatitude} ${airportarea.centerLongitude}, Distance ${airportarea.airspaceDistance}m"
        }
            
        int line_id = 1
        
        File csv_file = new File(webRootDir + csvFileName)
        Writer csv_writer = csv_file.newWriter("UTF-8",false)
        csv_writer << "id"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_TYPE}"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_RUNWAY}"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_HEADING}"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_ICAO}"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_NAME}"
        csv_writer << "${CSV_DELIMITER}${CSV_AIRPORT_WKT}"
        
        if (airportarea) {                        ok = true

            
            // airports
            if (openaipAirfields) {
                Map ret = call_rest("airports?pos=${airportarea.centerLatitude},${airportarea.centerLongitude}&dist=${airportarea.airspaceDistance}", "GET", 200, "", "items")
                if (ret.ok && ret.data) {
                    if (checkAirport) {
                        ok = true
                        for (Map d in ret.data) {
                            airfields_num++
                            csv_writer << CSV_LINESEPARATOR + line_id
                            csv_writer << CSV_DELIMITER
                            csv_writer << d
                            line_id++
                        }
                    } else {
                        for (Map d in ret.data) {
                            if (d.geometry.type == "Point") {
                                String airport_symbol = ""
                                String runway_symbol = ""
                                String runway_heading = ""
                                if (d.runways) {
                                    for (Map r in d.runways) {
                                        if (r.mainRunway) {
                                            switch (d.type) {
                                                case 1:
                                                    airport_symbol = "gliding-small.svg"
                                                    break
                                                case 2:
                                                    airport_symbol = "af_civil-small.svg"
                                                    break
                                                case 3:
                                                    airport_symbol = "apt_mil_civil-small.svg"
                                                    break
                                                case 4:
                                                    airport_symbol = "other-4" + '-' + r.surface.mainComposite
                                                    break
                                                case 5:
                                                    airport_symbol = "ad_mil-small.svg"
                                                    break
                                                case 6:
                                                    airport_symbol = "light_aircraft-small.svg"
                                                    break
                                                case 7:
                                                    airport_symbol = "other-7" + '-' + r.surface.mainComposite
                                                    break
                                                case 8:
                                                    airport_symbol = "ad_closed-small.svg"
                                                    break
                                                case 9:
                                                    airport_symbol = "apt-small.svg"
                                                    break
                                                case 10:
                                                    airport_symbol = "af_water-large.svg"
                                                    break
                                                default:
                                                    airport_symbol = "other-" + d.type + '-' + r.surface.mainComposite
                                                    break
                                            }
                                            switch (r.surface.mainComposite) { 
                                                case 0:
                                                case 1:
                                                    runway_symbol = "runway_paved-small.svg"
                                                    break
                                                case 2:
                                                    runway_symbol = "runway_unpaved-small.svg"
                                                    break
                                                default:    
                                                    runway_symbol = "other-" + r.surface.mainComposite
                                                    break
                                            }
                                            runway_heading = r.trueHeading
                                            break
                                        }
                                    }
                                }
                                if (airport_symbol) {
                                    airfields_num++
                                    csv_writer << CSV_LINESEPARATOR + line_id
                                    csv_writer << CSV_DELIMITER + airport_symbol
                                    csv_writer << CSV_DELIMITER + runway_symbol
                                    csv_writer << CSV_DELIMITER + runway_heading
                                    csv_writer << CSV_DELIMITER
                                    if (d.icaoCode) {
                                        csv_writer << d.icaoCode
                                    }
                                    csv_writer << CSV_DELIMITER + '"' + d.name + '"'
                                    csv_writer << CSV_DELIMITER + "POINT(${d.geometry.coordinates[0]} ${d.geometry.coordinates[1]})"
                                    line_id++
                                }
                            }
                        }
                    }
                }
            }
            
            // additional airfields
            if (additionalAirfields) {
                for (CoordMapObject coordmapobject_instance in CoordMapObject.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                    if (coordmapobject_instance.mapObjectType == MapObjectType.Airfield) {
                        String airport_symbol = "af_civil-small.svg"
                        if (coordmapobject_instance.mapObjectGliderAirfield) {
                            airport_symbol = "gliding-small.svg"
                        }
                        String runway_symbol = "runway_unpaved-small.svg"
                        if (coordmapobject_instance.mapObjectPavedAirfield) {
                            runway_symbol = "runway_paved-small.svg"
                        }
                        String runway_heading = coordmapobject_instance.gateDirection
                        airfields_num++
                        csv_writer << CSV_LINESEPARATOR + line_id
                        csv_writer << CSV_DELIMITER + airport_symbol
                        csv_writer << CSV_DELIMITER + runway_symbol
                        csv_writer << CSV_DELIMITER + runway_heading
                        csv_writer << CSV_DELIMITER
                        csv_writer << CSV_DELIMITER + '"' + coordmapobject_instance.mapObjectText + '"'
                        csv_writer << ""
                        csv_writer << CSV_DELIMITER + "POINT(${coordmapobject_instance.lonMath()} ${coordmapobject_instance.latMath()})"
                        line_id++
                    }
                }
            }
            
            // reporting-points
            if (openaipAirfields) {
                Map ret = call_rest("reporting-points?pos=${airportarea.centerLatitude},${airportarea.centerLongitude}&dist=${airportarea.airspaceDistance}", "GET", 200, "", "items")
                if (ret.ok && ret.data) {
                    if (checkAirport) {
                        ok = true
                        for (Map d in ret.data) {
                            airfields_num++
                            csv_writer << CSV_LINESEPARATOR + line_id
                            csv_writer << CSV_DELIMITER
                            csv_writer << d
                            line_id++
                        }
                    } else {
                        ok = true
                        for (Map d in ret.data) {
                            if (d.geometry.type == "Point") {
                                String point_symbol = ""
                                if (d.compulsory) {
                                    point_symbol = "reporting_point_compulsory-small.svg"
                                } else {
                                    point_symbol = "reporting_point_request-small.svg"
                                }
                                airfields_num++
                                csv_writer << CSV_LINESEPARATOR + line_id
                                csv_writer << CSV_DELIMITER + point_symbol
                                csv_writer << CSV_DELIMITER
                                csv_writer << CSV_DELIMITER
                                csv_writer << CSV_DELIMITER
                                csv_writer << CSV_DELIMITER + '"' + d.name + '"'
                                csv_writer << CSV_DELIMITER + "POINT(${d.geometry.coordinates[0]} ${d.geometry.coordinates[1]})"
                                line_id++
                            }
                        }
                    }
                }
            }
            
            // obstacles
            /*
            if (openaipAirfields) {
                Map ret = call_rest("obstacles?pos=${airportarea.centerLatitude},${airportarea.centerLongitude}&dist=${airportarea.airspaceDistance}", "GET", 200, "", "items")
                if (ret.ok && ret.data) {
                    for (Map d in ret.data) {
                        if (d.geometry.type == "Point") {
                            String point_symbol = ""
                            if (d.osmTags.'generator:method' == 'wind_turbine') {
                            } else if (d.osmTags.'communication:radio' == 'yes') {
                            } else {
                                println "XX1 ${d.osmTags}"
                            }
                            if (point_symbol) {
                                airfields_num++
                                csv_writer << CSV_LINESEPARATOR + line_id
                                csv_writer << CSV_DELIMITER + point_symbol
                                csv_writer << CSV_DELIMITER
                                csv_writer << CSV_DELIMITER
                                csv_writer << CSV_DELIMITER
                                csv_writer << CSV_DELIMITER + '"' + d.name + '"'
                                csv_writer << CSV_DELIMITER + "POINT(${d.geometry.coordinates[0]} ${d.geometry.coordinates[1]})"
                                line_id++
                            }
                        }
                    }
                }
            }
            */
        }
        
        csv_writer.close()

        if (ok) {
            printdone "${airfields_num} airports found."
        } else {
            printerror ""
        }
        
        return [ok:ok, airfieldsnum:airfields_num]
    }
        
    //--------------------------------------------------------------------------
    private Map get_airportarea(Route routeInstance, String contestMapCenterPoints, boolean checkAirport)
    {
        BigDecimal min_latitude = null
        BigDecimal min_longitude = null
        BigDecimal max_latitude = null
        BigDecimal max_longitude = null
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
            if (coordroute_instance.type.IsContestMapQuestionCoord()) {
                if (!contestMapCenterPoints || DisabledCheckPointsTools.Uncompress(contestMapCenterPoints).contains(coordroute_instance.title()+',')) {
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
        }
        
        if (!min_latitude) {
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
        }
        
        if (!min_latitude) {
            return [:]
        }
        
        int airspace_distance = (OsmPrintMapService.AIRPORTAREA_DISTANCE * routeInstance.mapScale * 1.414 / 1000).toInteger() // m
        if (checkAirport) {
            airspace_distance = 5000 // m
        }
        
        BigDecimal center_lat = ((max_latitude+min_latitude)/2).setScale(3, RoundingMode.HALF_EVEN)
        BigDecimal center_lon = ((max_longitude+min_longitude)/2).setScale(3, RoundingMode.HALF_EVEN)
        
        return [centerLatitude: center_lat, centerLongitude: center_lon, airspaceDistance: airspace_distance]
    }
    
    //--------------------------------------------------------------------------
    private Map call_rest(String funcURL, String requestMethod, int successfulResponseCode, String outputData, String retDataKey)
    {
        String openaip_url_path = "${BootStrap.global.GetOpenAIPAPI()}/${funcURL}"
        if (funcURL.contains('?')) {
            openaip_url_path += "&"
        } else {
            openaip_url_path += "?"
        }
        openaip_url_path += "apiKey=${BootStrap.global.GetOpenAIPToken()}"
        
        return call_rest2(openaip_url_path, "", requestMethod, successfulResponseCode, outputData, retDataKey)
    }
        
    //--------------------------------------------------------------------------
    private Map call_rest_firebase(String funcURL, String requestMethod, int successfulResponseCode, String outputData, String retDataKey)
    {
        boolean show_log = false
        
        String firebase_url_path = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=API_KEY"
        String firebase_output_data = """{"email":"EMAIL","password":"PASSWORD","returnSecureToken":true}"""
        
        Map firebase_ret = call_rest2(firebase_url_path, "", "POST", 200, firebase_output_data, ALL_DATA)
        if (show_log) {
            println firebase_ret
            println ""
        }
        if (firebase_ret.responseCode == 200) {
            String firebase_id_token = firebase_ret.data.idToken
            
            String openaip_url_path = "${BootStrap.global.GetOpenAIPAPI()}/${funcURL}"
            if (funcURL.contains('?')) {
                openaip_url_path += "&"
            } else {
                openaip_url_path += "?"
            }
            openaip_url_path += "apiKey=${BootStrap.global.GetOpenAIPToken()}"
            
            return call_rest2(openaip_url_path, firebase_id_token, requestMethod, successfulResponseCode, outputData, retDataKey)
        }
        return [responseCode:firebase_ret.responseCode, data:null, ok:false, errorMsg:""]
    }
        
    //--------------------------------------------------------------------------
    private Map call_rest2(String urlPath, String bearerToken, String requestMethod, int successfulResponseCode, String outputData, String retDataKey)
    {
        Map ret = [responseCode:null, data:null, ok:false, errorMsg:""]
        
        boolean show_log = false
        
        if (show_log) {
            printstart "${requestMethod} ${urlPath}"
        }
            
        int max_output_size = 0 // 2000
        try {
            def connection = urlPath.toURL().openConnection()
            connection.requestMethod = requestMethod
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("User-Agent", "Application")
            if (bearerToken) {
                connection.setRequestProperty("Authorization", "Bearer ${bearerToken}")
                if (show_log) {
                    println "Bearer ${bearerToken}"
                }
            }
            connection.doOutput = true
            connection.doInput = true
    
            if (outputData) {
                if (show_log) {
                    if (max_output_size && outputData.size() > max_output_size) {
                        println "${outputData.substring(0, max_output_size)}..."
                    } else {
                        println outputData
                    }
                }
                byte[] output_bytes = outputData.getBytes("UTF-8")
                OutputStream os = connection.getOutputStream()
                os.write(output_bytes)
                os.close()
            }
            
            if (show_log) {
                if (connection.responseCode == successfulResponseCode) {
                    println "responseCode=${connection.responseCode} Ok."
                } else {
                    println "responseCode=${connection.responseCode} Error."
                }
            }
            ret.responseCode = connection.responseCode
            
            if (ret.responseCode == successfulResponseCode) {
                ret.ok = true
                if (retDataKey) {
                    InputStream inputstream_instance = connection.getInputStream()
                    BufferedReader input_reader = inputstream_instance.newReader("UTF-8")
                    def input_data = new JsonSlurper().parse(input_reader)
                    if (input_data) {
                        if (show_log) {
                            println "Json data: ${input_data}"
                        }
                        if (retDataKey == ALL_DATA) {
                            ret.data = input_data
                        } else {
                            ret.data = input_data.(retDataKey.toString())
                        }
                    }
                    input_reader.close()
                    inputstream_instance.close()
                    if (show_log) {
                        if (connection.responseCode == successfulResponseCode) {
                            println "responseCode2=${ret.responseCode} ${ret.data} Ok."
                        } else {
                            println "responseCode2=${ret.responseCode} ${ret.data} Error."
                        }
                    }
                }
            } else {
                ret.errorMsg = "Response code ${ret.responseCode}, "
                ret.errorMsg += "${connection.getResponseMessage()}, "
                InputStream inputstream_instance = connection.getErrorStream()
				if (inputstream_instance) {
					BufferedReader input_reader = inputstream_instance.newReader("UTF-8")
					while (true) {
						String line = input_reader.readLine()
						if (line == null) {
							break
						}
						ret.errorMsg += line
					}
					input_reader.close()
					inputstream_instance.close()
				}
            }
            
            if (show_log) {
                printdone ""
            }
        } catch (Exception e) {
            ret.errorMsg += "Exception ${e.message}"
            if (show_log) {
                printerror "Exception ${e.message}"
            }
        }
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    void DeleteFile(String fileName)
    {
        printstart "Delete '$fileName'"
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
    
    //--------------------------------------------------------------------------
    private String getMsg(String code, List args)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        if (args) {
            return messageSource.getMessage(code, args.toArray(), new Locale(session_obj.showLanguage))
        } else {
            return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
        }
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
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
