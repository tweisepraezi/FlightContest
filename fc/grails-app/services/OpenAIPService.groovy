import java.util.Map
import groovy.json.JsonSlurper
import org.springframework.web.context.request.RequestContextHolder
import groovy.xml.*
import java.util.zip.*
import java.net.URLEncoder

// https://docs.oracle.com/javase/8/docs/api/java/net/HttpURLConnection.html
// https://docs.openaip.net/

class OpenAIPService
{
    def messageSource
    def logService
    
    final static String ALL_DATA = "*"

	final static String XMLHEADER = "<?xml version='1.0' encoding='UTF-8'?>"

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
    Map WriteAirspaces2KMZ(Route routeInstance, String webRootDir, String kmzFileName, boolean isPrint)
    {
        printstart "WriteAirspaces2KMZ ${routeInstance.GetName(isPrint)} -> ${webRootDir + kmzFileName}"
        
        String kml_file_name = kmzFileName + ".kml"
        
        printstart "Generate '${webRootDir + kml_file_name}'"
        
		boolean all_airspaces_written = true
		String missing_airspaces = ""
		
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
						xml.color "ffff00ff"
						xml.width 1.5
					}
					xml.PolyStyle {
						xml.color "40ff00ff"
					}
				}
				for (String layer in routeInstance.contestMapAirspacesLayer2.split("\n")) {
					if (layer && layer.trim()) {
						String airspace_name = layer.trim()
                        if (!airspace_name.startsWith(Defs.IGNORE_LINE)) {
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
                            Map ret = [:]
                            if (airspace_name.startsWith(OsmPrintMapService.AIRSPACE_LAYER_ID_PREAFIX)) {
                                String search_id = airspace_name.substring(3)
                                ret = call_rest("airspaces?id=${search_id}", "GET", 200, "", "items")
                            } else {
                                String search_name = URLEncoder.encode(airspace_name, "UTF-8")
                                ret = call_rest("airspaces?search=${search_name}", "GET", 200, "", "items")
                            }
                            if (ret.ok && ret.data) {
                                String airspace_coordinates = ret.data.geometry.coordinates.toString()
                                airspace_coordinates = airspace_coordinates.replace(', ',',').replace('],[',' ').replace('[','').replace(']]]]','')
                                write_airspace(xml, airspace_name, airspace_text, "airspace", airspace_coordinates)
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
        kml_writer.close()
        
		printdone "${kml_file.size()} bytes"
		write_kmz(webRootDir, kmzFileName, kml_file_name)
		DeleteFile(webRootDir + kml_file_name)

        printdone ""
        
        return [ok:all_airspaces_written, missingAirspaces:missing_airspaces]
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
    Map GetAirspacesAirportarea(Route routeInstance, String contestMapCenterPoints)
    {
        printstart "GetAirspacesAirportarea ${routeInstance.name()} ${routeInstance.contestMapAirspacesLowerLimit}ft"
        
        Map airportarea = get_airportarea(routeInstance, contestMapCenterPoints)
        println "Center (Lat Lon): ${airportarea.centerLatitude} ${airportarea.centerLongitude}, Distance ${airportarea.airspaceDistance}m"
        
        boolean ok = false
        int airspaces_num = 0
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
        return false
        
        if (d.icaoClass == 2 && d.type == 10) { // FIS
            return true
        }
        if (d.icaoClass == 2 && d.type == 0) { // um Großflughafen, näher untersuchen
            return true
        }
        if (d.icaoClass == 8 && d.type == 28) { // PARA
            return true
        }
        return false
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
    Map WriteAirports2CSV(Route routeInstance, String webRootDir, String csvFileName, boolean isPrint, String contestMapCenterPoints)
    {
        printstart "WriteAirports2CSV ${routeInstance.GetName(isPrint)} -> ${webRootDir + csvFileName}"
        
        Map airportarea = get_airportarea(routeInstance, contestMapCenterPoints)
        println "Center (Lat Lon): ${airportarea.centerLatitude} ${airportarea.centerLongitude}, Distance ${airportarea.airspaceDistance}m"
        
        boolean ok = false
        int airports_num = 0
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
        
        // airports
        Map ret = call_rest("airports?pos=${airportarea.centerLatitude},${airportarea.centerLongitude}&dist=${airportarea.airspaceDistance}", "GET", 200, "", "items")
        if (ret.ok && ret.data) {
            ok = true
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
                        airports_num++
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
        
        // reporting-points
        ret = call_rest("reporting-points?pos=${airportarea.centerLatitude},${airportarea.centerLongitude}&dist=${airportarea.airspaceDistance}", "GET", 200, "", "items")
        if (ret.ok && ret.data) {
            ok = true
            for (Map d in ret.data) {
                if (d.geometry.type == "Point") {
                    String point_symbol = ""
                    if (d.compulsory) {
                        point_symbol = "reporting_point_compulsory-small.svg"
                    } else {
                        point_symbol = "reporting_point_request-small.svg"
                    }
                    airports_num++
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
        
        // obstacles
        /*
        ret = call_rest("obstacles?pos=${airportarea.centerLatitude},${airportarea.centerLongitude}&dist=${airportarea.airspaceDistance}", "GET", 200, "", "items")
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
                        airports_num++
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
        */
        
        csv_writer.close()

        if (ok) {
            printdone "${airports_num} airports found."
        } else {
            printerror ""
        }
        
        return [ok:ok, airportsnum:airports_num]
    }
        
    //--------------------------------------------------------------------------
    private Map get_airportarea(Route routeInstance, String contestMapCenterPoints)
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
        
        return [centerLatitude:   (max_latitude+min_latitude)/2,
                centerLongitude:  (max_longitude+min_longitude)/2,
                airspaceDistance: (OsmPrintMapService.AIRPORTAREA_DISTANCE * routeInstance.mapScale * 1.414 / 1000).toInteger() // m
               ]
    }
    
    //--------------------------------------------------------------------------
    private Map call_rest(String funcURL, String requestMethod, int successfulResponseCode, String outputData, String retDataKey)
    {
        Map ret = [responseCode:null, data:null, ok:false, errorMsg:""]
        
        boolean show_log = false
        int max_output_size = 0 // 2000
        
        String url_path = "${BootStrap.global.GetOpenAIPAPI()}/${funcURL}"
		
		if (funcURL.contains('?')) {
			url_path += "&"
		} else {
			url_path += "?"
		}
		url_path += "apiKey=${BootStrap.global.GetOpenAIPToken()}"
        
        if (show_log) {
            printstart "${requestMethod} ${url_path}"
        }
            
        try {
            def connection = url_path.toURL().openConnection()
            connection.requestMethod = requestMethod
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
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
