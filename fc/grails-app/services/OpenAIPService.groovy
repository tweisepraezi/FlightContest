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

	
    //--------------------------------------------------------------------------
    Map test(Map params)
    {
		//printstart "getairports"
        //Map ret = call_rest("airports?pos=45.243432%2C9.23423&dist=50000", "GET", 200, "", ALL_DATA)
		printstart "getairspaces"
		Map ret = call_rest("airspaces?search=CTR%20Schleswig", "GET", 200, "", ALL_DATA) // searchOptLwc=true&
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
                            for (String airspace_style in Tools.Split(airspace_name, OsmPrintMapService.AIRSPACE_LAYER_STYLE_SEPARATOR)) {
                                List airspace_style_values = Tools.Split(airspace_style.trim(), OsmPrintMapService.AIRSPACE_LAYER_STYLE_KEY_VALUE_SEPARATOR)
                                if (airspace_style_values.size() == 1) {
                                    airspace_name = airspace_style_values[0].trim()
                                }
                            }
                            String search_name = URLEncoder.encode(airspace_name, "UTF-8")
                            Map ret = call_rest("airspaces?search=${search_name}", "GET", 200, "", "items") // searchOptLwc=true&
                            if (ret.ok && ret.data) {
                                String airspace_coordinates = ret.data.geometry.coordinates.toString()
                                airspace_coordinates = airspace_coordinates.replace(', ',',').replace('],[',' ').replace('[','').replace(']]]]','')
                                write_airspace(xml, airspace_name, "airspace", airspace_coordinates)
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
    private void write_airspace(MarkupBuilder xml, String airspaceName, String airspaceStyle, String airspaceCoordinates)
    {
		xml.Folder {
			xml.name airspaceName
			xml.Placemark {
				xml.name airspaceName
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
