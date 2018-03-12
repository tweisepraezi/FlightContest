import java.math.BigDecimal;

import java.math.*
import java.text.*
import java.util.List
import java.util.Map
import java.awt.Graphics2D
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import org.springframework.web.context.request.RequestContextHolder

import groovy.xml.*

import org.apache.commons.net.ftp.FTPClient

class GpxService 
{
	def logService
    def messageSource
    def mailService
    def servletContext
    def grailsApplication
    
	final static BigDecimal ftPerMeter = 3.2808 // 1 meter = 3,2808 feet
    final static BigDecimal kmPerNM = 1.852f    // 1 NM = 1.852 km
    
    final static int MAP_WIDTH = 2000           // Karten-Breite in Pixel
    final static int MAP_HEIGHT = 2600          // Karten-Höhe in Pixel, > MAP_WIDTH
    final static String MAP_FONT_NAME = "Arial" // Schriftart zum Schreiben auf Karte
    final static int MAP_FONT_HEIGHT = 36       // Schrifthöhe zum Schreiben auf Karte
    final static BigDecimal MAP_MARGIN = 2.0    // Anzeigerand um Strecken-Karte in NM
    
    final static BigDecimal CONTESTMAP_CIRCLE_RADIUS = 0.5 // NM
    final static BigDecimal CONTESTMAP_PROCEDURETURN_DISTANCE = 1 // NM
    final static BigDecimal CONTESTMAP_TPNAME_DISTANCE = 1.5 // NM
    final static BigDecimal CONTESTMAP_TPNAME_PROCEDURETURN_DISTANCE = 2.5 // NM
    final static BigDecimal CONTESTMAP_MARGIN_DISTANCE = 5 // NM
    
	final static boolean WRLOG = false
    
    final static String XMLHEADER = "<?xml version='1.0' encoding='UTF-8'?>"
    
    final static String GPXVERSION = "1.1"
    final static String GPXCREATOR = "Flight Contest - flightcontest.de - Version 3"
    final static String GPXCREATOR_CONTESTMAP = "Flight Contest - flightcontest.de - Contest Map - Version 1"
    final static String GPXGACTRACKNAME = "GAC track"
    
    final static String COLOR_ERROR = "red"
    final static String COLOR_WARNING = "blue"
    
    final static String GPXDATA = "GPXDATA"
	
	//--------------------------------------------------------------------------
	boolean RepairGAC(String gacOriginalFileName, String gacRepairFileName, boolean repairTracks, boolean repairIdenticalTimes)
	{
		boolean repaired = true
		String err_msg = ""
		
		printstart "RepairGAC $gacOriginalFileName -> $gacRepairFileName"
		if (repairTracks) {
			println "Repair tracks"
		}
		if (repairIdenticalTimes) {
			println "Repair identical times"
		}
		
		File gac_original_file = new File(gacOriginalFileName)
		File gac_repair_file = new File(gacRepairFileName)
		String line
		
		LineNumberReader gac_original_reader = gac_original_file.newReader()
		BufferedWriter gac_repair_writer = gac_repair_file.newWriter()
		try {
			boolean first = true
			BigDecimal src_latitude = null
			BigDecimal src_longitude = null
			String next_time_utc = null
			String last_time_utc = null
			boolean valid_gac_format = false
			while (true) {
				line = gac_original_reader.readLine()
				if (line == null) {
					break
				}
				if (line) {
					if (line == GPX2GAC.GACFORMAT_DEF) {
						valid_gac_format = true
					}	
					String new_line = ""
					boolean ignore_line = false 
					if (line.startsWith("B")) {
						if (valid_gac_format) {

							// Repair DropOuts
							String time_utc = line.substring(1,7)
							if (!first) {
								if (repairIdenticalTimes) {
									if (last_time_utc == time_utc) { // Zeile mit doppelter Zeit entfernen
										ignore_line = true
										if (WRLOG) {
											println "Ignore '$line'"
										}
									} else { // Zeilen mit fehlenden Zeiten einfügen
										/*
										while (time_utc != next_time_utc) {
											String add_line = line.substring(0,1) + next_time_utc + line.substring(7)
											WriteLine(gac_repair_writer,add_line)
											next_time_utc = AddOneSecond(next_time_utc)
										}
										*/
									}
								}
							}
							
							String old_track = line.substring(39,42)
							String new_track = old_track
							
							// (Geographische Breite: -90 (S)... +90 Grad (N))
							String latitude_grad = line.substring(7,9)
							BigDecimal latitude_grad_math = latitude_grad.toBigDecimal()
							String latidude_minute = line.substring(9,11) + '.' + line.substring(11,14)
							BigDecimal latidude_minute_math = latidude_minute.toBigDecimal()
							boolean latitude_north = line.substring(14,15) == CoordPresentation.NORTH
							BigDecimal dest_latitude = latitude_grad_math + (latidude_minute_math / 60)
							if (!latitude_north) {
								dest_latitude *= -1
							}
							
							// Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
							String longitude_grad = line.substring(15,18)
							BigDecimal longitude_grad_math = longitude_grad.toBigDecimal()
							String longitude_minute = line.substring(18,20) + '.' + line.substring(20,23)
							BigDecimal longitude_minute_math = longitude_minute.toBigDecimal()
							boolean longitude_east = line.substring(23,24) == CoordPresentation.EAST
							BigDecimal dest_longitude = longitude_grad_math + (longitude_minute_math / 60)
							if (!longitude_east) {
								dest_longitude *= -1
							}
							
							if (!first) {
								if (repairTracks) {
									Map leg = AviationMath.calculateLeg(dest_latitude,dest_longitude,src_latitude,src_longitude)
									new_track = FcMath.GradStr(FcMath.RoundGrad(leg.dir))
									if (WRLOG) {
										println "$src_latitude, $src_longitude -> $dest_latitude, $dest_longitude (${line.substring(7,15)}, ${line.substring(15,24)})"
										if (old_track != new_track) {
											println "  $old_track -> $new_track"
										}
									}
								}
							}
							new_line = line.substring(0,39) + new_track + line.substring(42)
							first = false
							src_latitude = dest_latitude
							src_longitude = dest_longitude
							next_time_utc = AddOneSecond(time_utc)
							last_time_utc = time_utc
						} else {
							new_line = line
						}
					} else {
						new_line = line
					}
					if (!ignore_line) {
						WriteLine(gac_repair_writer,new_line)
					}
				}
			}
			if (!valid_gac_format) {
				repaired = false
				err_msg = "No supported gac format."
			}
		} catch (Exception e) {
			repaired = false
			err_msg = e.getMessage()
		}
		gac_repair_writer.close()
		gac_original_reader.close()
		
		if (repaired) {
			printdone ""
		} else {
			printerror err_msg
		}
		return repaired
	}
	
	//--------------------------------------------------------------------------
	String AddOneSecond(String timeUTC)
	{
		Date date_utc = Date.parse("HHmmss",timeUTC)
		GregorianCalendar calendar_utc = new GregorianCalendar()
		calendar_utc.setTime(date_utc)
		calendar_utc.add(Calendar.SECOND, 1) // + 1s
		return calendar_utc.getTime().format("HHmmss")
	}
	
    //--------------------------------------------------------------------------
    Map ConvertGAC2GPX(String gacFileName, String gpxFileName, Route routeInstance)
    {
        Map ret = [:]
        boolean converted = true
        String err_msg = ""
        
        printstart "ConvertGAC2GPX $gacFileName -> $gpxFileName"
        
        File gac_file = new File(gacFileName)
        File gpx_file = new File(gpxFileName)
        
        boolean repairIdenticalTimes = false
        String line = ""
        
        LineNumberReader gac_reader = gac_file.newReader()
        BufferedWriter gpx_writer = gpx_file.newWriter()
        MarkupBuilder xml = new MarkupBuilder(gpx_writer)
        gpx_writer.writeLine(XMLHEADER)
        xml.gpx(version:GPXVERSION, creator:GPXCREATOR) {
            xml.trk {
                xml.name GPXGACTRACKNAME
                xml.trkseg {
                    try {
                        boolean first = true
                        String last_time_utc = null
                        BigDecimal latitude = null
                        BigDecimal longitude = null
                        boolean valid_gac_format = false
                        String last_utc = FcTime.UTC_GPX_DATE
                        while (true) {
                            line = gac_reader.readLine()
                            if (line == null) {
                                break
                            }
                            if (line) {
                                if (line.startsWith("I")) { // if (line == GPX2GAC.GACFORMAT_DEF) {
                                    valid_gac_format = true
                                }   
                                boolean ignore_line = false 
                                if (line.startsWith("B")) {
                                    if (valid_gac_format) {
                                        
                                        // Repair DropOuts
                                        String time_utc = line.substring(1,7)
                                        if (!first) {
                                            if (repairIdenticalTimes) {
                                                if (last_time_utc == time_utc) { // Zeile mit doppelter Zeit entfernen
                                                    ignore_line = true
                                                    if (WRLOG) {
                                                        println "Ignore '$line'"
                                                    }
                                                }
                                            }
                                        }
                                        
                                        // UTC
                                        String utc_h = line.substring(1,3)
                                        String utc_min = line.substring(3,5)
                                        String utc_s = line.substring(5,7)
                                        String utc = FcTime.UTCGetNextDateTime(last_utc, "${utc_h}:${utc_min}:${utc_s}")
                                        
                                        // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                                        String latitude_grad = line.substring(7,9)
                                        BigDecimal latitude_grad_math = latitude_grad.toBigDecimal()
                                        String latidude_minute = line.substring(9,11) + '.' + line.substring(11,14)
                                        BigDecimal latidude_minute_math = latidude_minute.toBigDecimal()
                                        boolean latitude_north = line.substring(14,15) == CoordPresentation.NORTH
                                        latitude = latitude_grad_math + (latidude_minute_math / 60)
                                        if (!latitude_north) {
                                            latitude *= -1
                                        }
                                        
                                        // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                                        String longitude_grad = line.substring(15,18)
                                        BigDecimal longitude_grad_math = longitude_grad.toBigDecimal()
                                        String longitude_minute = line.substring(18,20) + '.' + line.substring(20,23)
                                        BigDecimal longitude_minute_math = longitude_minute.toBigDecimal()
                                        boolean longitude_east = line.substring(23,24) == CoordPresentation.EAST
                                        longitude = longitude_grad_math + (longitude_minute_math / 60)
                                        if (!longitude_east) {
                                            longitude *= -1
                                        }
                                        
                                        // Altitude (Höhe)
                                        String altitude_foot = line.substring(30,35)
                                        BigDecimal altitude_meter = altitude_foot.toLong() / ftPerMeter
                                        
                                        // write gpx tag
                                        if (!ignore_line) {
                                            // println "UTC: $utc, Latitude: $latitude, Longitude: $longitude, Altitude: ${altitude_meter}m"
                                            xml.trkpt(lat:latitude,lon:longitude) {
                                                xml.ele altitude_meter
                                                xml.time utc
                                            }
                                        }
                                        
                                        first = false
                                        last_time_utc = time_utc
                                        last_utc = utc
                                    }
                                }
                            }
                        }
                        if (!valid_gac_format) {
                            converted = false
                            err_msg = "No supported gac format."
                        }
                    } catch (Exception e) {
                        converted = false
                        err_msg = e.getMessage()
                    }
                }
            }
            if (routeInstance) {
                GPXRoute(routeInstance, null, false, false, xml) // false - no Print, false - no wrEnrouteSign
            }
        }
        gac_reader.close()
        gpx_writer.close()

        if (converted) {
            printdone ""
        } else {
            printerror err_msg
        }
        if (routeInstance) {
            println "Generate points for buttons (GetShowPointsRoute)"
            ret += [gpxShowPoints:RoutePointsTools.GetShowPointsRoute(routeInstance, null, false, messageSource)] // false - no showEnrouteSign
        }
        ret += [ok:converted]
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map ConvertIGC2GPX(String igcFileName, String gpxFileName, Route routeInstance)
    {
        Map ret = [:]
        boolean converted = true
        String err_msg = ""
        
        printstart "ConvertIGC2GPX $igcFileName -> $gpxFileName"
        
        File igc_file = new File(igcFileName)
        File gpx_file = new File(gpxFileName)
        
        boolean repairIdenticalTimes = false
        String line = ""
        
        LineNumberReader igc_reader = igc_file.newReader()
        BufferedWriter gpx_writer = gpx_file.newWriter()
        MarkupBuilder xml = new MarkupBuilder(gpx_writer)
        gpx_writer.writeLine(XMLHEADER)
        xml.gpx(version:GPXVERSION, creator:GPXCREATOR) {
            xml.trk {
                xml.name GPXGACTRACKNAME
                xml.trkseg {
                    try {
                        boolean first = true
                        String last_time_utc = null
                        BigDecimal latitude = null
                        BigDecimal longitude = null
                        boolean valid_gac_format = false
                        String last_utc = FcTime.UTC_GPX_DATE
                        while (true) {
                            line = igc_reader.readLine()
                            if (line == null) {
                                break
                            }
                            if (line) {
                                if (line.startsWith("I")) { // if (line == LoggerFileTools.IGCFORMAT_DEF) {
                                    valid_gac_format = true
                                }   
                                boolean ignore_line = false 
                                if (line.startsWith("B")) {
                                    if (valid_gac_format) {
                                        
                                        // Repair DropOuts
                                        String time_utc = line.substring(1,7)
                                        if (!first) {
                                            if (repairIdenticalTimes) {
                                                if (last_time_utc == time_utc) { // Zeile mit doppelter Zeit entfernen
                                                    ignore_line = true
                                                    if (WRLOG) {
                                                        println "Ignore '$line'"
                                                    }
                                                }
                                            }
                                        }
                                        
                                        // UTC
                                        String utc_h = line.substring(1,3)
                                        String utc_min = line.substring(3,5)
                                        String utc_s = line.substring(5,7)
                                        String utc = FcTime.UTCGetNextDateTime(last_utc, "${utc_h}:${utc_min}:${utc_s}")
                                        
                                        // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                                        String latitude_grad = line.substring(7,9)
                                        BigDecimal latitude_grad_math = latitude_grad.toBigDecimal()
                                        String latidude_minute = line.substring(9,11) + '.' + line.substring(11,14)
                                        BigDecimal latidude_minute_math = latidude_minute.toBigDecimal()
                                        boolean latitude_north = line.substring(14,15) == CoordPresentation.NORTH
                                        latitude = latitude_grad_math + (latidude_minute_math / 60)
                                        if (!latitude_north) {
                                            latitude *= -1
                                        }
                                        
                                        // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                                        String longitude_grad = line.substring(15,18)
                                        BigDecimal longitude_grad_math = longitude_grad.toBigDecimal()
                                        String longitude_minute = line.substring(18,20) + '.' + line.substring(20,23)
                                        BigDecimal longitude_minute_math = longitude_minute.toBigDecimal()
                                        boolean longitude_east = line.substring(23,24) == CoordPresentation.EAST
                                        longitude = longitude_grad_math + (longitude_minute_math / 60)
                                        if (!longitude_east) {
                                            longitude *= -1
                                        }
                                        
                                        // Altitude (Höhe)
                                        int altitude_meter = line.substring(30,35).toInteger()
                                        
                                        // write gpx tag
                                        if (!ignore_line) {
                                            // println "UTC: $utc, Latitude: $latitude, Longitude: $longitude, Altitude: ${altitude_meter}m"
                                            xml.trkpt(lat:latitude,lon:longitude) {
                                                xml.ele altitude_meter
                                                xml.time utc
                                            }
                                        }
                                        
                                        first = false
                                        last_time_utc = time_utc
                                        last_utc = utc
                                    }
                                }
                            }
                        }
                        if (!valid_gac_format) {
                            converted = false
                            err_msg = "No supported gac format."
                        }
                    } catch (Exception e) {
                        converted = false
                        err_msg = e.getMessage()
                    }
                }
            }
            if (routeInstance) {
                GPXRoute(routeInstance, null, false, false, xml) // false - no Print, false - no wrEnrouteSign
            }
        }
        igc_reader.close()
        gpx_writer.close()

        if (converted) {
            printdone ""
        } else {
            printerror err_msg
        }
        if (routeInstance) {
            println "Generate points for buttons (GetShowPointsRoute)"
            ret += [gpxShowPoints:RoutePointsTools.GetShowPointsRoute(routeInstance, null, false, messageSource)] // false - no showEnrouteSign
        }
        ret += [ok:converted]
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map ConvertRoute2GPX(Route routeInstance, String gpxFileName, boolean isPrint, boolean showPoints, boolean wrEnrouteSign, Map contestMap = [:])
    {
        printstart "ConvertRoute2GPX ${routeInstance.name()} -> ${gpxFileName}"
        
        printstart "Generate GPX"
        BufferedWriter gpx_writer = null
        CharArrayWriter gpx_data = null
        File gpx_file = null
        if (gpxFileName.startsWith(GPXDATA)) {
            gpx_data = new CharArrayWriter()
            gpx_writer = new BufferedWriter(gpx_data)
        } else {
            gpx_file = new File(gpxFileName)
            gpx_writer = gpx_file.newWriter()
        }
        MarkupBuilder xml = new MarkupBuilder(gpx_writer)
        gpx_writer.writeLine(XMLHEADER)
        if (contestMap) {
            xml.gpx(version:GPXVERSION, creator:GPXCREATOR_CONTESTMAP) {
                GPXRoute(routeInstance, null, isPrint, wrEnrouteSign, xml, contestMap)
            }
        } else {
            xml.gpx(version:GPXVERSION, creator:GPXCREATOR) {
                GPXRoute(routeInstance, null, isPrint, wrEnrouteSign, xml, [:])
            }
        }
        gpx_writer.close()
        if (gpxFileName.startsWith(GPXDATA)) {
            BootStrap.tempData.AddData(gpxFileName, gpx_data.toCharArray())
            printdone "${gpx_data.size()} bytes"
        } else {
            printdone "${gpx_file.size()} bytes"
        }

        List show_points = []
        if (showPoints) {
            println "Generate points for buttons (GetShowPointsRoute)"
            show_points = RoutePointsTools.GetShowPointsRoute(routeInstance, null, wrEnrouteSign, messageSource, contestMap)
        }
        
        printdone ""
        
        return [ok:true, gpxShowPoints:show_points]
    }
    
    //--------------------------------------------------------------------------
    Map AddRoute2GPX(Route routeInstance, String gpxFileName)
    {
        Map ret = [:]
        boolean converted = true
        String err_msg = ""
        
        printstart "AddRoute2GPX ${routeInstance.name()} -> ${gpxFileName}"

        File gpx_file = new File(gpxFileName)
        
        String new_gpx_filename = "${gpxFileName}.new"
        File new_gpx_file = new File(new_gpx_filename)
        BufferedWriter gpx_writer = new_gpx_file.newWriter()
        MarkupBuilder xml = new MarkupBuilder(gpx_writer)
        gpx_writer.writeLine(XMLHEADER)
        xml.gpx(version:GPXVERSION, creator:GPXCREATOR) {

            FileReader gpx_reader = new FileReader(gpx_file)
            def gpx = new XmlParser().parse(gpx_reader)
            if (gpx.trk.size() == 1) { // only 1 track allowed
                xml.trk {
                    name gpx.trk.name[0].text()
                    xml.trkseg {
                        gpx.trk.trkseg.trkpt.each { p ->
                            xml.trkpt(lat:p.'@lat', lon:p.'@lon') {
                                ele p.ele[0].text()
                                time p.time[0].text()
                            }
                        }
                    }
                }
                gpx.trk.text()
            }
            gpx_reader.close()
            
            GPXRoute(routeInstance, null, false, false, xml) // false - no Print, false - no wrEnrouteSign
        }
        gpx_writer.close()
        
        gpx_file.delete()
        new_gpx_file.renameTo(new File(gpxFileName))

        if (converted) {
            printdone ""
        } else {
            printerror err_msg
        }
        println "Generate points for buttons (GetShowPointsRoute)"
        ret += [gpxShowPoints:RoutePointsTools.GetShowPointsRoute(routeInstance, null, false, messageSource)] // false - no showEnrouteSign
        ret += [ok:converted]
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map ConvertTest2PrintMap(Test testInstance, String gpxFileName, String pngFileName) 
    {
        printstart "ConvertTest2PrintMap"
        Map ret = ConvertTest2GPX(testInstance,gpxFileName, true, false, false) // true - Print, false - no Points, false - no wrEnrouteSign
        if (ret.ok && ret.track) {
            printstart "Generate ${pngFileName} from ${gpxFileName}"
            
            Map xy = ConvertGPX2XY(gpxFileName, MAP_WIDTH, MAP_HEIGHT, false, true, "", "", "", "", testInstance.task.contest.timeZone, testInstance.task.contest.coordPresentation) // false - no forcePortrait, true - Print
            
            printstart "Generate image"
            BufferedImage img = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_RGB)
            Graphics2D g2 = img.createGraphics()
            g2.setColor(Color.WHITE)
            g2.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT)
            g2.setFont(new Font(MAP_FONT_NAME, Font.PLAIN, MAP_FONT_HEIGHT))
            g2.setColor(Color.BLACK)
            
            int last_x = 0
            int last_y = 0
            for (Map p in xy.points) {
                if (!p.start) {
                    if (p.source != "gatenotfound") {
                        g2.drawLine(last_x, last_y, p.x, p.y)
                    }
                }
                if (p.name) {
                    FontMetrics fm = g2.getFontMetrics()
                    int name_width = fm.stringWidth(p.name)
                    int name_x = p.x
                    int name_y = p.y
                    if (xy.portrait) {
                        if (name_x + name_width > MAP_WIDTH) {
                            name_x = MAP_WIDTH - name_width
                        }
                        if (p.name_down) {
                            name_y += MAP_FONT_HEIGHT
                        }
                        g2.drawString(p.name, name_x, name_y)
                    } else {
                        if (p.name_down) {
                            name_x += MAP_FONT_HEIGHT
                        }
                        if (name_y < name_width ) {
                            name_y = name_width
                        }
                        drawStringRotate(g2, name_x, name_y, -90, p.name)
                    }
                }
                if (p.source != "gatenotfound") {
                    last_x = p.x
                    last_y = p.y
                }
            }
            
            g2.dispose()
            img.flush()
            printdone ""
            
            printstart "Write image $pngFileName"
            try {
                def dest_file = new File(pngFileName).newOutputStream()
                boolean ok = ImageIO.write(img, "png", dest_file)
                dest_file.close()
                if (ok) {
                    printdone ""
                } else {
                    printerror ""
                }
            } catch (Exception e) {
                printerror e.getMessage()
            }
            
            printdone ""
        }
        printdone ""
        return ret
    }
    
    //--------------------------------------------------------------------------
    private void drawStringRotate(Graphics2D g2, int x, int y, int angle, String text) 
    {    
        g2.translate((float)x,(float)y)
        g2.rotate(Math.toRadians(angle))
        g2.drawString(text,0,0)
        g2.rotate(-Math.toRadians(angle))
        g2.translate(-(float)x,-(float)y)
    }
    
    //--------------------------------------------------------------------------
    Map ConvertGPX2XY(String gpxFileName, int maxX, int maxY, boolean forcePortrait, boolean isPrint, 
                      centerLat, centerLon, radiusValue, moveDir, String timeZone, CoordPresentation coordPresentation)
    {
        printstart "ConvertGPX2XY $gpxFileName"
        
        List points = []
        int points_max_x = 0
        int points_max_y = 0
        boolean portrait = false
        
        Reader gpx_reader = null
        if (gpxFileName.startsWith(GPXDATA)) {
            CharArrayReader gpx_data = new CharArrayReader(BootStrap.tempData.GetData(gpxFileName))
            gpx_reader = new BufferedReader(gpx_data)
        } else {
            String gpx_file_name = gpxFileName
            if (gpxFileName.startsWith(Defs.ROOT_FOLDER_GPXUPLOAD)) {
                gpx_file_name = servletContext.getRealPath("/") + gpxFileName
            }
            File gpx_file = new File(gpx_file_name)
            gpx_reader = new FileReader(gpx_file)
        }
        def gpx = new XmlParser().parse(gpx_reader)
        def track_points = null
        if (gpx.trk.size() == 1) { // only 1 track allowed
            def gpx_track_name = gpx.trk.name[0].text()
            track_points = gpx.trk.trkseg.trkpt
        }
        def routes = gpx.rte
        def way_points = gpx.wpt
        
        BigDecimal min_latitude_math = null
        BigDecimal min_longitude_math = null
        BigDecimal max_latitude_math = null
        BigDecimal max_longitude_math = null
        
        if (centerLat && centerLon && radiusValue) {
            min_latitude_math = centerLat.toBigDecimal()
            min_longitude_math = centerLon.toBigDecimal()
            if (moveDir) {
                BigDecimal move_dir = moveDir.toBigDecimal()
                BigDecimal move_value = radiusValue.toBigDecimal()
                if (move_dir == 0) { // down
                    min_latitude_math = AviationMath.getCoordinate(min_latitude_math, min_longitude_math, move_dir, move_value).lat
                } else if (move_dir == 180) { // up 
                    min_latitude_math = AviationMath.getCoordinate(min_latitude_math, min_longitude_math, move_dir, move_value).lat
                } else if (move_dir == 90) { // left
                    min_longitude_math = AviationMath.getCoordinate(min_latitude_math, min_longitude_math, move_dir, move_value).lon
                } else if (move_dir == 270) { // right
                    min_longitude_math = AviationMath.getCoordinate(min_latitude_math, min_longitude_math, move_dir, move_value).lon
                }
            }
            max_latitude_math = min_latitude_math
            max_longitude_math = min_longitude_math
            
            // add margin to min/max
            Map rect = AviationMath.getShowRect(min_latitude_math, max_latitude_math, min_longitude_math, max_longitude_math, 2*radiusValue.toBigDecimal())
            min_latitude_math = rect.latmin
            max_latitude_math = rect.latmax
            min_longitude_math = rect.lonmin
            max_longitude_math = rect.lonmax
        } else {
            
            // calculate min/max from flight
            track_points.each {
                BigDecimal latitude_math = it.'@lat'.toBigDecimal()
                BigDecimal longitude_math = it.'@lon'.toBigDecimal()
                
                if (!min_latitude_math) {
                    min_latitude_math = latitude_math
                } else if (latitude_math < min_latitude_math) {
                    min_latitude_math = latitude_math
                }
                if (!min_longitude_math) {
                    min_longitude_math = longitude_math
                } else if (longitude_math < min_longitude_math) {
                    min_longitude_math = longitude_math
                }
                if (!max_latitude_math) {
                    max_latitude_math = latitude_math
                } else if (latitude_math > max_latitude_math) {
                    max_latitude_math = latitude_math
                }
                if (!max_longitude_math) {
                    max_longitude_math = longitude_math
                } else if (longitude_math > max_longitude_math) {
                    max_longitude_math = longitude_math
                }
            }
            
            // calculate min/max from route points
            routes.each {
                def route_points = it.rtept
                route_points.each {
                    BigDecimal latitude_math = it.'@lat'.toBigDecimal()
                    BigDecimal longitude_math = it.'@lon'.toBigDecimal()
    
                    if (!min_latitude_math) {
                        min_latitude_math = latitude_math
                    } else if (latitude_math < min_latitude_math) {
                        min_latitude_math = latitude_math
                    }
                    if (!min_longitude_math) {
                        min_longitude_math = longitude_math
                    } else if (longitude_math < min_longitude_math) {
                        min_longitude_math = longitude_math
                    }
                    if (!max_latitude_math) {
                        max_latitude_math = latitude_math
                    } else if (latitude_math > max_latitude_math) {
                        max_latitude_math = latitude_math
                    }
                    if (!max_longitude_math) {
                        max_longitude_math = longitude_math
                    } else if (longitude_math > max_longitude_math) {
                        max_longitude_math = longitude_math
                    }
                }
            }
            
            // calculate min/max from way points
            way_points.each {
                BigDecimal latitude_math = it.'@lat'.toBigDecimal()
                BigDecimal longitude_math = it.'@lon'.toBigDecimal()

                if (!min_latitude_math) {
                    min_latitude_math = latitude_math
                } else if (latitude_math < min_latitude_math) {
                    min_latitude_math = latitude_math
                }
                if (!min_longitude_math) {
                    min_longitude_math = longitude_math
                } else if (longitude_math < min_longitude_math) {
                    min_longitude_math = longitude_math
                }
                if (!max_latitude_math) {
                    max_latitude_math = latitude_math
                } else if (latitude_math > max_latitude_math) {
                    max_latitude_math = latitude_math
                }
                if (!max_longitude_math) {
                    max_longitude_math = longitude_math
                } else if (longitude_math > max_longitude_math) {
                    max_longitude_math = longitude_math
                }
            }
            
            // add margin to min/max
            Map rect = AviationMath.getShowRect(min_latitude_math, max_latitude_math, min_longitude_math, max_longitude_math, MAP_MARGIN)
            min_latitude_math = rect.latmin
            max_latitude_math = rect.latmax
            min_longitude_math = rect.lonmin
            max_longitude_math = rect.lonmax
        }
        
        int max_x = maxX
        int max_y = maxY
        BigDecimal lat_nm = AviationMath.calculateLeg(min_latitude_math, min_longitude_math, max_latitude_math, min_longitude_math).dis
        BigDecimal lon_nm = AviationMath.calculateLeg(min_latitude_math, min_longitude_math, min_latitude_math, max_longitude_math).dis
        BigDecimal lon_lat_relation = lon_nm / lat_nm
        BigDecimal x_y_relation = maxX / maxY
        BigDecimal radius_value = 16;
        portrait = forcePortrait || (lon_nm < lat_nm)
        if (portrait) {
            if (lon_lat_relation < x_y_relation) {
                max_x = maxY * lon_lat_relation
            } else {
                max_y = maxX / lon_lat_relation
            }
            radius_value = lat_nm / 4
        } else {
            if (1 / lon_lat_relation < x_y_relation) {
                max_x = maxY / lon_lat_relation
            } else {
                max_y = maxX * lon_lat_relation
            }
            radius_value = lon_nm / 4
        }
        BigDecimal center_lat = (min_latitude_math + max_latitude_math) / 2
        BigDecimal center_lon = AviationMath.getCoordinate(center_lat, min_longitude_math, 90, lat_nm / 2).lon
        
        // calculate x/y from flight
        boolean start_value = true
        int last_x = -1
        int last_y = -1
        track_points.each {
            BigDecimal latitude_math = it.'@lat'.toBigDecimal()
            BigDecimal longitude_math = it.'@lon'.toBigDecimal()
            BigDecimal altitude = it.ele.text().toBigDecimal()
            String utc = it.time.text()
            int x = 0
            int y = 0
            if (portrait) {
                BigDecimal lon2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, latitude_math, min_longitude_math).dis
                x = (lon2_nm / lon_nm * max_x).toInteger()
                if (min_longitude_math > longitude_math) {
                    x *= -1
                }
                BigDecimal lat2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, max_latitude_math, longitude_math).dis
                y = (lat2_nm / lat_nm * max_y).toInteger()
                if (max_latitude_math < latitude_math) {
                    y *= -1
                }
            } else {
                BigDecimal lat2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, max_latitude_math, longitude_math).dis
                x = (lat2_nm / lat_nm * max_x).toInteger()
                BigDecimal lon2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, latitude_math, max_longitude_math).dis
                y = (lon2_nm / lon_nm * max_y).toInteger()
            }
            if ((last_x != x) || (last_y != y)) {
                points += [source:"flight", start:start_value, x:x, y:y, name:'', name_down:false, color:'', info:getPointInfo(latitude_math, longitude_math, altitude, utc, timeZone, coordPresentation, "")]
                if (x > points_max_x) {
                    points_max_x = x
                }
                if (y > points_max_y) {
                    points_max_y = y
                }
                start_value = false
            }
            if (it.extensions.flightcontest.badcourse) {
                boolean print_warning = false
                if (it.extensions.flightcontest.badcourse.text() == "yes") {
                    points[points.size()-1].color = COLOR_ERROR
                } else {
                    points[points.size()-1].color = COLOR_WARNING
                    print_warning = isPrint
                }
                String badcourse_duration = it.extensions.flightcontest.badcourse.'@duration'[0].toString()
                if (badcourse_duration != "null") {
                    points[points.size()-1].name += getMsg("fc.offlinemap.badcourse.seconds", [badcourse_duration], isPrint)
                    if (print_warning) {
                        points[points.size()-1].name += "*"
                    }
                }
            }
            if (it.extensions.flightcontest.badturn) {
                boolean print_warning = false
                if (it.extensions.flightcontest.badturn.text() == "yes") {
                    points[points.size()-1].color = COLOR_ERROR
                } else {
                    points[points.size()-1].color = COLOR_WARNING
                    print_warning = isPrint
                }
                if (points[points.size()-1].name) {
                    points[points.size()-1].name += ", "
                }
                points[points.size()-1].name += getMsg("fc.offlinemap.badprocedureturn",isPrint)
                if (print_warning) {
                    points[points.size()-1].name += "*"
                }
            }
            if (it.extensions.flightcontest.badgate) {
                boolean print_warning = false
                if (it.extensions.flightcontest.badgate.text() == "yes") {
                    points[points.size()-1].color = COLOR_ERROR
                } else {
                    points[points.size()-1].color = COLOR_WARNING
                    print_warning = isPrint
                }
                String badgate_name = it.extensions.flightcontest.badgate.'@name'[0]
                if (points[points.size()-1].name) {
                    points[points.size()-1].name += ", "
                }
                points[points.size()-1].name += getMsg("fc.offlinemap.badgate",[badgate_name],isPrint)
                if (print_warning) {
                    points[points.size()-1].name += "*"
                }
            }
            if (it.extensions.flightcontest.gatenotfound) {
                
                int x1 = 0
                int y1 = 0
                latitude_math = it.extensions.flightcontest.gatenotfound.'@lat'[0].toBigDecimal()
                longitude_math = it.extensions.flightcontest.gatenotfound.'@lon'[0].toBigDecimal()
                if (portrait) {
                    BigDecimal lon2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, latitude_math, min_longitude_math).dis
                    x1 = (lon2_nm / lon_nm * max_x).toInteger()
                    if (min_longitude_math > longitude_math) {
                        x1 *= -1
                    }
                    BigDecimal lat2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, max_latitude_math, longitude_math).dis
                    y1 = (lat2_nm / lat_nm * max_y).toInteger()
                    if (max_latitude_math < latitude_math) {
                        y1 *= -1
                    }
                } else {
                    BigDecimal lat2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, max_latitude_math, longitude_math).dis
                    x1 = (lat2_nm / lat_nm * max_x).toInteger()
                    BigDecimal lon2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, latitude_math, max_longitude_math).dis
                    y1 = (lon2_nm / lon_nm * max_y).toInteger()
                }
                
                boolean print_warning = false
                String badgate_color = ""
                if (it.extensions.flightcontest.gatenotfound.text() == "yes") {
                    badgate_color = COLOR_ERROR
                } else {
                    badgate_color = COLOR_WARNING
                    print_warning = isPrint
                }
                String badgate_name = getMsg("fc.offlinemap.badgate",[it.extensions.flightcontest.gatenotfound.'@name'[0]],isPrint)
                if (print_warning) {
                    badgate_name += "*"
                }
                points += [source:"gatenotfound", start:start_value, x:x1, y:y1, name:badgate_name, name_down:false, color:badgate_color, info:'']
            }
            last_x = x
            last_y = y
        }
        
        // calculate x/y from route points
        routes.each {
            def route_points = it.rtept
            String gate_name = ""
            boolean is_gate = it.rtept.size() == 2
            if (is_gate) {
                gate_name = it.name.text()
            }
            start_value = true
            last_x = -1
            last_y = -1
            route_points.each {
                BigDecimal latitude_math = it.'@lat'.toBigDecimal()
                BigDecimal longitude_math = it.'@lon'.toBigDecimal()
                BigDecimal altitude = 0.0
                if (it.ele) {
                    altitude = it.ele.text().toBigDecimal()
                }
                String point_name = it.name.text()
                int x = 0
                int y = 0
                if (portrait) {
                    BigDecimal lon2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, latitude_math, min_longitude_math).dis
                    x = (lon2_nm / lon_nm * max_x).toInteger()
                    if (min_longitude_math > longitude_math) {
                        x *= -1
                    }
                    BigDecimal lat2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, max_latitude_math, longitude_math).dis
                    y = (lat2_nm / lat_nm * max_y).toInteger()
                    if (max_latitude_math < latitude_math) {
                        y *= -1
                    }
                } else {
                    BigDecimal lat2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, max_latitude_math, longitude_math).dis
                    x = (lat2_nm / lat_nm * max_x).toInteger()
                    BigDecimal lon2_nm = AviationMath.calculateLeg(latitude_math, longitude_math, latitude_math, max_longitude_math).dis
                    y = (lon2_nm / lon_nm * max_y).toInteger()
                }
                if (start_value) {
                    points += [source:"route", start:start_value, x:x, y:y, name:gate_name, name_down:false, color:'', info:getPointInfo(latitude_math, longitude_math, altitude, "", timeZone, coordPresentation, gate_name)]
                } else {
                    if (portrait) {
                        if (is_gate && (y < last_y)) {
                            points[points.size()-1].name_down = true
                        }
                    } else {
                        if (is_gate && (x < last_x)) {
                            points[points.size()-1].name_down = true
                        }
                    }
                    points += [source:"route", start:start_value, x:x, y:y, name:'', name_down:false, color:'', info:getPointInfo(latitude_math, longitude_math, altitude, "", timeZone, coordPresentation, gate_name)]
                }
                if (x > points_max_x) {
                    points_max_x = x
                }
                if (y > points_max_y) {
                    points_max_y = y
                }
                start_value = false
                last_x = x
                last_y = y
            }
        }
        gpx_reader.close()
        if (points_max_x > maxX) {
            points_max_x = maxX
        }
        if (points_max_y > maxY) {
            points_max_y = maxY
        }
        /*
        points.each {
            println "$it"
        }
        */
        Map ret = [points: points, 
                   portrait: portrait, 
                   center_lat: center_lat, 
                   center_lon: center_lon, 
                   radius_value:radius_value,
                   max_x: points_max_x,
                   max_y: points_max_y
                  ]
        printdone ""
        return ret
    }
    
    //--------------------------------------------------------------------------
    private String getPointInfo(BigDecimal latitudeValue, BigDecimal longitudeValue, BigDecimal altitudeValue, 
                                String utcValue, String timeZone, CoordPresentation coordPresentation, String gateName)
    {
        String utc_or_gate = ""
        if (utcValue) {
            utc_or_gate = FcTime.UTCGetLocalTime(utcValue, timeZone)
            if (timeZone == "00:00") {
                utc_or_gate += " UTC"
            } else {
                utc_or_gate += " Local"
            }
        } else if (gateName) {
            utc_or_gate = gateName
        }
        
        Map lat_coord = CoordPresentation.GetDirectionGradDecimalMinute(latitudeValue,true)
        String lat = "${CoordPresentation.LAT} ${CoordPresentation.DecimalGradStr(latitudeValue)}${getMsg('fc.grad',false).encodeAsHTML()}" // CoordPresentation.DEGREE
        switch (coordPresentation) {
            case CoordPresentation.DEGREEMINUTE:
                lat = "${CoordPresentation.LAT} ${CoordPresentation.IntegerGradStr(lat_coord.grad,true)}${getMsg('fc.grad',false).encodeAsHTML()} ${CoordPresentation.DecimalMinuteStr(lat_coord.minute)}${getMsg('fc.min',false)} ${lat_coord.direction}"
                break
            case CoordPresentation.DEGREEMINUTESECOND:
                lat = "${CoordPresentation.LAT} ${CoordPresentation.IntegerGradStr(lat_coord.grad,true)}${getMsg('fc.grad',false).encodeAsHTML()} ${CoordPresentation.IntegerMinuteStr(lat_coord.minute)}${getMsg('fc.min',false)} ${CoordPresentation.DecimalSecondStr(lat_coord.minute)}${getMsg('fc.sec',false)} ${lat_coord.direction}"
                break
        }
        
        Map lon_coord = CoordPresentation.GetDirectionGradDecimalMinute(longitudeValue,false)
        String lon = "${CoordPresentation.LON} ${CoordPresentation.DecimalGradStr(longitudeValue)}${getMsg('fc.grad',false).encodeAsHTML()}" // CoordPresentation.DEGREE
        switch (coordPresentation) {
            case CoordPresentation.DEGREEMINUTE:
                lon = "${CoordPresentation.LON} ${CoordPresentation.IntegerGradStr(lon_coord.grad,false)}${getMsg('fc.grad',false).encodeAsHTML()} ${CoordPresentation.DecimalMinuteStr(lon_coord.minute)}${getMsg('fc.min',false)} ${lon_coord.direction}"
                break
            case CoordPresentation.DEGREEMINUTESECOND:
                lon = "${CoordPresentation.LON} ${CoordPresentation.IntegerGradStr(lon_coord.grad,false)}${getMsg('fc.grad',false).encodeAsHTML()} ${CoordPresentation.IntegerMinuteStr(lon_coord.minute)}${getMsg('fc.min',false)} ${CoordPresentation.DecimalSecondStr(lon_coord.minute)}${getMsg('fc.sec',false)} ${lon_coord.direction}"
                break
        }
        
        String alt = ""
        if (altitudeValue) {
            alt = "${FcMath.RoundAltitude(altitudeValue*ftPerMeter)}${getMsg('fc.foot',false)}"
        }
        
        String ret = ""
        if (utc_or_gate) {
            ret += "${utc_or_gate} - "
        }
        ret += "${lat}, ${lon}"
        if (alt) {
            ret += " - ${alt}"
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    String ConvertGPX2XYXML(String gpxFileName, int maxX, int maxY, centerLat, centerLon, radiusValue, moveDir, 
                            String timeZone, CoordPresentation coordPresentation)
    {
        printstart "ConvertGPX2XYXML $gpxFileName $maxX $maxY $centerLat $centerLon $radiusValue $moveDir"
        
        Map xy_values = ConvertGPX2XY(gpxFileName, maxX, maxY, true, false, centerLat, centerLon, radiusValue, moveDir, timeZone, coordPresentation) // true - forcePortrait, false - no Print
        
        println "MaxX = ${xy_values.max_x}, MaxY = ${xy_values.max_y}, Center-Lat = ${xy_values.center_lat}, Center-Lon: ${xy_values.center_lon}"
        
        StringWriter xml = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(xml)
        builder.points (portrait:     xy_values.portrait, 
                        center_lat:   xy_values.center_lat, 
                        center_lon:   xy_values.center_lon, 
                        radius_value: xy_values.radius_value,
                        max_x:        xy_values.max_x,
                        max_y:        xy_values.max_y
                       ) 
        {
            for (point in xy_values.points) {
                p (source:    point.source, 
                   start:     point.start, 
                   x:         point.x, 
                   y:         point.y, 
                   name:      point.name, 
                   name_down: point.name_down, 
                   color:     point.color,
                   info:      point.info
                  )
            }
        }
        
        printdone ""
        return xml.toString()
    }
    
    //--------------------------------------------------------------------------
    Map ConvertTest2GPX(Test testInstance, String gpxFileName, boolean isPrint, boolean showPoints, boolean wrEnrouteSign)
    {
        boolean found_track = false
        
        Route route_instance = testInstance.task.flighttest.route
        
        printstart "ConvertTest2GPX '${testInstance.aflosStartNum}:${testInstance.crew.name}' -> ${gpxFileName}"
        
        printstart "Generate GPX"
        BufferedWriter gpx_writer = null
        CharArrayWriter gpx_data = null
        File gpx_file = null
        if (gpxFileName.startsWith(GPXDATA)) {
            gpx_data = new CharArrayWriter()
            gpx_writer = new BufferedWriter(gpx_data)
        } else {
            gpx_file = new File(gpxFileName)
            gpx_writer = gpx_file.newWriter()
        }
        MarkupBuilder xml = new MarkupBuilder(gpx_writer)
        gpx_writer.writeLine(XMLHEADER)
        xml.gpx(version:GPXVERSION, creator:GPXCREATOR) {
            GPXRoute(route_instance, testInstance.flighttestwind, isPrint, wrEnrouteSign, xml)
            found_track = GPXTrack(testInstance, testInstance.aflosStartNum, isPrint, xml)
        }
        gpx_writer.close()
        if (gpxFileName.startsWith(GPXDATA)) {
            BootStrap.tempData.AddData(gpxFileName, gpx_data.toCharArray())
            printdone "${gpx_data.size()} bytes"
        } else {
            printdone "${gpx_file.size()} bytes"
        }
        
        List show_points = []
        if (showPoints) {
            if (testInstance.IsLoggerResultWithoutRunwayMissed()) {
                println "Generate points for buttons (GetShowPoints)"
                show_points = GetShowPoints(gpxFileName, wrEnrouteSign)
            } else {
                println "Generate points for buttons (GetShowPointsRoute)"
                show_points = RoutePointsTools.GetShowPointsRoute(route_instance, testInstance, wrEnrouteSign, messageSource)
            }
        }
        
        printdone ""
        
        return [ok:true, track:found_track, gpxShowPoints:show_points]
    }
    
    //--------------------------------------------------------------------------
    List GetShowPoints(String gpxFileName, boolean wrEnrouteSign)
    {
        List points = []
        printstart "GetShowPoints ${gpxFileName} wrEnrouteSign:$wrEnrouteSign"
        Reader gpx_reader = null
        if (gpxFileName.startsWith(GPXDATA)) {
            CharArrayReader gpx_data = new CharArrayReader(BootStrap.tempData.GetData(gpxFileName))
            gpx_reader = new BufferedReader(gpx_data)
        } else {
            String gpx_file_name = gpxFileName
            if (gpxFileName.startsWith(Defs.ROOT_FOLDER_GPXUPLOAD)) {
                gpx_file_name = servletContext.getRealPath("/") + gpxFileName
            }
            File gpx_file = new File(gpx_file_name)
            gpx_reader = new FileReader(gpx_file)
        }
        def gpx = new XmlParser().parse(gpx_reader)
        if (gpx.trk.size() == 1) { // only 1 track allowed
            gpx.trk.trkseg.trkpt.each { p ->
                if (p.extensions.flightcontest.badcourse && p.extensions.flightcontest.badcourse.'@duration'[0]) {
                    String duration = p.extensions.flightcontest.badcourse.'@duration'[0]
                    Map new_point = [name:getMsg("fc.offlinemap.badcourse.seconds", [duration], false)] // false - no Print
                    new_point += [latcenter:p.'@lat', loncenter:p.'@lon', radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_ERRORPOINT]
                    if (p.extensions.flightcontest.badcourse.text() == "yes") {
                        new_point += [error:true]
                    } else {
                        new_point += [warning:true]
                    }
                    points += new_point
                }
                if (p.extensions.flightcontest.badturn) {
                    Map new_point = [name:getMsg("fc.offlinemap.badprocedureturn",false)] // false - no Print
                    new_point += [latcenter:p.'@lat', loncenter:p.'@lon', radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_ERRORPOINT]
                    if (p.extensions.flightcontest.badturn.text() == "yes") {
                        new_point += [error:true]
                    } else {
                        new_point += [warning:true]
                    }
                    points += new_point
                }
                if (p.extensions.flightcontest.gate) {
                    Map new_point = [name:p.extensions.flightcontest.gate.'@name'[0]]
                    new_point += [latcenter:p.'@lat', loncenter:p.'@lon']
                    if (p.extensions.flightcontest.gate.'@runway'[0] == "yes") {
                        new_point += [radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_AIRFIELD]
                    } else {
                        new_point += [radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_CHECKPOINT]
                    }
                    points += new_point
                } 
                if (p.extensions.flightcontest.badgate) {
                    Map new_point = [name:p.extensions.flightcontest.badgate.'@name'[0]]
                    new_point += [latcenter:p.'@lat', loncenter:p.'@lon']
                    if (p.extensions.flightcontest.badgate.'@runway'[0] == "yes") {
                        new_point += [radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_AIRFIELD]
                    } else {
                        new_point += [radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_CHECKPOINT]
                    }
                    if (p.extensions.flightcontest.badgate.text() == "yes") {
                        new_point += [error:true]
                    } else {
                        new_point += [warning:true]
                    }
                    points += new_point
                } 
                if (p.extensions.flightcontest.gatenotfound) {
                    Map new_point = [name:p.extensions.flightcontest.gatenotfound.'@name'[0]]
                    new_point += [latcenter:p.extensions.flightcontest.gatenotfound.'@lat'[0], loncenter:p.extensions.flightcontest.gatenotfound.'@lon'[0]]
                    if (p.extensions.flightcontest.gatenotfound.'@runway'[0] == "yes") {
                        new_point += [radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_AIRFIELD]
                    } else {
                        new_point += [radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_CHECKPOINT]
                    }
                    if (p.extensions.flightcontest.gatenotfound.text() == "yes") {
                        new_point += [error:true]
                    } else {
                        new_point += [warning:true]
                    }
                    points += new_point
                }
                if (wrEnrouteSign) {
                    if (p.extensions.flightcontest.enroutephoto) {
                        Map new_point = [name:p.extensions.flightcontest.enroutephoto.'@name'[0], enroutephoto:true]
                        new_point += [latcenter:p.extensions.flightcontest.enroutephoto.'@lat'[0], loncenter:p.extensions.flightcontest.enroutephoto.'@lon'[0]]
                        new_point += [radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_ENROUTESIGN]
                        points += new_point
                    }
                    if (p.extensions.flightcontest.enroutecanvas) {
                        Map new_point = [name:p.extensions.flightcontest.enroutecanvas.'@name'[0], enroutecanvas:true]
                        new_point += [latcenter:p.extensions.flightcontest.enroutecanvas.'@lat'[0], loncenter:p.extensions.flightcontest.enroutecanvas.'@lon'[0]]
                        new_point += [radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_ENROUTESIGN]
                        points += new_point
                    }
                }
            }
        } else if (gpx.trk.size() == 0) { // no track
            gpx.rte.each { p ->
                if (p.extensions.flightcontest.gate) {
                    Map new_point = [name:p.name.text()]
                    new_point += [latcenter:p.extensions.flightcontest.gate.'@lat'[0], loncenter:p.extensions.flightcontest.gate.'@lon'[0]]
                    switch (p.extensions.flightcontest.gate.'@type'[0]) {
                        case "TO":
                        case "LDG":
                        case "iTO":
                        case "iLDG":
                            new_point += [radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_AIRFIELD]
                            break
                        default:
                            new_point += [radius:RoutePointsTools.GPXSHOWPPOINT_RADIUS_CHECKPOINT]
                            break
                    }
                    points += new_point
                }
            }
        }
        gpx_reader.close()

        printdone ""
        return points
    }
    
    //--------------------------------------------------------------------------
    private void GPXRoute(Route routeInstance, FlightTestWind flighttestwindInstance, boolean isPrint, boolean wrEnrouteSign, MarkupBuilder xml, Map contestMap = [:])
    {
        printstart "GPXRoute Print:$isPrint wrEnrouteSign:$wrEnrouteSign contestMap:$contestMap"
        
        boolean wr_enroutesign = wrEnrouteSign
        if (wrEnrouteSign && flighttestwindInstance) {
            wr_enroutesign = flighttestwindInstance.flighttest.IsObservationSignUsed()
        }
        
        // observation settings & enroute signs without position
        Map contest_map_rect = [:]
        BigDecimal center_latitude = null
        BigDecimal center_longitude = null
        if (wr_enroutesign || contestMap) {
            xml.extensions {
                xml.flightcontest {
                    if (wr_enroutesign) {
                        xml.observationsettings(
                            turnpoint: routeInstance.turnpointRoute,
                            turnpointmapmeasurement: getYesNo(routeInstance.turnpointMapMeasurement),
                            enroutephoto: routeInstance.enroutePhotoRoute,
                            enroutephotomeasurement: routeInstance.enroutePhotoMeasurement,
                            enroutecanvas: routeInstance.enrouteCanvasRoute,
                            enroutecanvasmeasurement: routeInstance.enrouteCanvasMeasurement
                        )
                        if (routeInstance.enroutePhotoRoute == EnrouteRoute.InputName) {
                            xml.enroutephotosigns {
                                for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                                    xml.enroutephotosign(
                                        photoname: coordenroutephoto_instance.enroutePhotoName,
                                        viewpos: coordenroutephoto_instance.enrouteViewPos
                                    )
                                }
                            }
                        }
                        if (routeInstance.enrouteCanvasRoute == EnrouteRoute.InputName) {
                            xml.enroutecanvassigns {
                                for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                                    xml.enroutecanvassign(
                                        canvasname: coordenroutecanvas_instance.enrouteCanvasSign.canvasName,
                                        viewpos: coordenroutecanvas_instance.enrouteViewPos
                                    )
                                }
                            }
                        }
                    }
                    if (contestMap) {
                        BigDecimal min_latitude = null
                        BigDecimal min_longitude = null
                        BigDecimal max_latitude = null
                        BigDecimal max_longitude = null
                        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                            if (coordroute_instance.type.IsContestMapCoord()) {
                                if (!contestMap.contestMapCenterPoints || contestMap.contestMapCenterPoints.contains(coordroute_instance.title()+',')) {
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
                        
                        contest_map_rect = AviationMath.getShowRect(min_latitude, max_latitude, min_longitude, max_longitude, CONTESTMAP_MARGIN_DISTANCE)
                        min_latitude = contest_map_rect.latmin
                        min_longitude = contest_map_rect.lonmin
                        max_latitude = contest_map_rect.latmax
                        max_longitude = contest_map_rect.lonmax
                        center_latitude = (max_latitude+min_latitude)/2
                        center_longitude = (max_longitude+min_longitude)/2
                        
                        xml.contestmap(
                            scale: routeInstance.contest.mapScale,
                            min_latitude: min_latitude,
                            min_longitude: min_longitude,
                            max_latitude: max_latitude,
                            max_longitude: max_longitude,
                            center_latitude: center_latitude,
                            center_longitude: center_longitude,
                            graticule: getYesNo(contestMap.contestMapGraticule),
                            center_graticule_latitude: GetRoundedDecimalGrad(center_latitude),
                            center_graticule_longitude: GetRoundedDecimalGrad(center_longitude),
                            contour_lines: getYesNo(contestMap.contestMapContourLines),
                            enroutephotos: getYesNo(contestMap.contestMapEnroutePhotos),
                            enroutecanvas: getYesNo(contestMap.contestMapEnrouteCanvas),
                            airfields: getYesNo(contestMap.contestMapAirfields),
                            churches: getYesNo(contestMap.contestMapChurches),
                            castles: getYesNo(contestMap.contestMapCastles),
                            chateaus: getYesNo(contestMap.contestMapChateaus),
                            windpowerstations: getYesNo(contestMap.contestMapWindpowerstations),
                            peaks: getYesNo(contestMap.contestMapPeaks),
                            additionals: getYesNo(contestMap.contestMapAdditionals),
                            specials: getYesNo(contestMap.contestMapSpecials),
                            airspaces: getYesNo(contestMap.contestMapAirspaces),
                            landscape: getYesNo(contestMap.contestMapPrintLandscape),
                            a3: getYesNo(contestMap.contestMapPrintA3),
                            scale_bar: getYesNo(contestMap.contestMapScaleBar),
                            no_color_change: getYesNo(contestMap.contestMapNoColorChange)
                        )
                    }
                }
            }
        }
        
        // tracks
        if (contestMap) {
            if (contestMap.contestMapLeg || contestMap.contestMapCurvedLeg) {
                CoordRoute last_coordroute_instance = null
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (coordroute_instance.type.IsContestMapCoord()) {
                        if (last_coordroute_instance) {
                            if (coordroute_instance.endCurved) {
                                if (contestMap.contestMapCurvedLeg) {
                                    xml.rte {
                                        xml.name "${last_coordroute_instance.titleShortMap(isPrint)} - ${coordroute_instance.titleShortMap(isPrint)}"
                                        boolean run = false
                                        CoordRoute last_coordroute_instance2 = null
                                        for (CoordRoute coordroute_instance2 in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                                            if (coordroute_instance2 == last_coordroute_instance) {
                                                run = true
                                            }
                                            if (run) {
                                                if (last_coordroute_instance2) {
                                                    if (last_coordroute_instance2 == last_coordroute_instance) { // first track
                                                        Map track_coords = AviationMath.getTrack2Circle(
                                                            last_coordroute_instance2.latMath(), last_coordroute_instance2.lonMath(),
                                                            coordroute_instance2.latMath(), coordroute_instance2.lonMath(),
                                                            CONTESTMAP_CIRCLE_RADIUS
                                                        )
                                                        xml.rtept(lat:track_coords.srcLat, lon:track_coords.srcLon)
                                                    } else if (coordroute_instance2 == coordroute_instance) { // last track
                                                        Map track_coords = AviationMath.getTrack2Circle(
                                                            last_coordroute_instance2.latMath(), last_coordroute_instance2.lonMath(),
                                                            coordroute_instance2.latMath(), coordroute_instance2.lonMath(),
                                                            CONTESTMAP_CIRCLE_RADIUS
                                                        )
                                                        xml.rtept(lat:track_coords.destLat, lon:track_coords.destLon)
                                                    } else { // middle track
                                                        xml.rtept(lat:coordroute_instance2.latMath(), lon:coordroute_instance2.lonMath())
                                                    }
                                                }
                                                last_coordroute_instance2 = coordroute_instance2
                                            }
                                            if (last_coordroute_instance2 == coordroute_instance) {
                                                run = false
                                            }
                                        }
                                    }
                                }
                            } else if (contestMap.contestMapLeg) {
                                if (contestMap.contestMapPrintPoints.contains(coordroute_instance.title()+',') && contestMap.contestMapPrintPoints.contains(last_coordroute_instance.title()+',')) {
                                    Map track_coords = AviationMath.getTrack2Circle(
                                        last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                        coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                        CONTESTMAP_CIRCLE_RADIUS
                                    )
                                    xml.rte {
                                        xml.name "${last_coordroute_instance.titleShortMap(isPrint)} - ${coordroute_instance.titleShortMap(isPrint)}"
                                        xml.rtept(lat:track_coords.srcLat, lon:track_coords.srcLon)
                                        xml.rtept(lat:track_coords.destLat, lon:track_coords.destLon)
                                    }
                                }
                            }
                        }
                        last_coordroute_instance = coordroute_instance
                    } else if (!coordroute_instance.type.IsCpCheckCoord()) {
                        last_coordroute_instance = null
                    }
                }
                if (contestMap.contestMapScaleBar) {
                    Map start_coord = AviationMath.getCoordinate(center_latitude, center_longitude, 0, 10 / kmPerNM) // 5cm von Mitte nach oben
                    start_coord = AviationMath.getCoordinate(start_coord.lat, start_coord.lon, 270, 10 / kmPerNM)    // 5cm von Mitte nach links
                    Map end_coord = AviationMath.getCoordinate(start_coord.lat, start_coord.lon, 90, 20 / kmPerNM)   // 10cm von Strat nach rechts
                    xml.rte {
                        xml.name "Waag. Balken 10cm"
                        xml.rtept(lat:start_coord.lat, lon:start_coord.lon)
                        xml.rtept(lat:start_coord.lat, lon:end_coord.lon)
                    }
                    end_coord = AviationMath.getCoordinate(start_coord.lat, start_coord.lon, 180, 20 / kmPerNM)
                    xml.rte {
                        xml.name "Senkr. Balken 10 cm"
                        xml.rtept(lat:start_coord.lat, lon:start_coord.lon)
                        xml.rtept(lat:end_coord.lat, lon:start_coord.lon)
                    }
                }
            }
        } else {
            long restart_id = 0
            xml.rte {
                xml.extensions {
                    xml.flightcontest {
                        xml.route(number:1)
                    }
                }
                xml.name routeInstance.name().encodeAsHTML()
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (coordroute_instance.type.IsCpCheckCoord()) {
                        BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                        xml.rtept(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                            xml.name coordroute_instance.titleShortMap(isPrint)
                            xml.ele altitude_meter
                        }
                    }
                    if (coordroute_instance.type == CoordType.iFP) {
                        restart_id = coordroute_instance.id
                        break
                    }
                }
            }
            if (restart_id != 0) {
                xml.rte {
                    xml.extensions {
                        xml.flightcontest {
                            xml.route(number:2)
                        }
                    }
                    xml.name routeInstance.name().encodeAsHTML()
                    boolean run = false
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                        if (run) {
                            if (coordroute_instance.type.IsCpCheckCoord()) {
                                BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                                xml.rtept(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                                    xml.name coordroute_instance.titleShortMap(isPrint)
                                    xml.ele altitude_meter
                                }
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
                }
            }
        }
        
        if (contestMap) {
            // circles
            if (contestMap.contestMapCircle) {
                CoordRoute last_coordroute_instance = null
                CoordRoute last_last_coordroute_instance = null
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (coordroute_instance.type.IsContestMapCoord()) {
                        if (contestMap.contestMapPrintPoints.contains(coordroute_instance.title()+',')) {
                            List circle_coords = AviationMath.getCircle(coordroute_instance.latMath(), coordroute_instance.lonMath(), CONTESTMAP_CIRCLE_RADIUS)
                            xml.rte {
                                // wrGate(coordroute_instance, xml)
                                // BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                                xml.name "Circle ${coordroute_instance.titleShortMap(isPrint)}"
                                for (Map circle_coord in circle_coords) {
                                    xml.rtept(lat:circle_coord.lat, lon:circle_coord.lon) {
                                        // xml.ele altitude_meter
                                    }
                                }
                            }
                        }
                    }
                    last_last_coordroute_instance = last_coordroute_instance 
                    last_coordroute_instance = coordroute_instance
                }
            }
            
            // procedure turns
            if (contestMap.contestMapProcedureTurn) {
                CoordRoute last_coordroute_instance = null
                CoordRoute last_last_coordroute_instance = null
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (coordroute_instance.planProcedureTurn && last_coordroute_instance && last_last_coordroute_instance && last_coordroute_instance.type.IsProcedureTurnCoord()) {
                        if (contestMap.contestMapPrintPoints.contains(last_coordroute_instance.title()+',')) {
                            List circle_coords = AviationMath.getProcedureTurnCircle(
                                last_last_coordroute_instance.latMath(), last_last_coordroute_instance.lonMath(),
                                last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                CONTESTMAP_CIRCLE_RADIUS,
                                CONTESTMAP_PROCEDURETURN_DISTANCE
                            )
                            xml.rte {
                                // wrGate(coordroute_instance, xml)
                                // BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                                xml.name "Procedure turn ${last_coordroute_instance.titleShortMap(isPrint)}"
                                for (Map circle_coord in circle_coords) {
                                    xml.rtept(lat:circle_coord.lat, lon:circle_coord.lon) {
                                        // xml.ele altitude_meter
                                    }
                                }
                            }
                        }
                    }
                    last_last_coordroute_instance = last_coordroute_instance 
                    last_coordroute_instance = coordroute_instance
                }
            }
            
            // tp names
            if (contestMap.contestMapTpName) {
                CoordRoute last_coordroute_instance = null
                CoordRoute last_last_coordroute_instance = null
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (coordroute_instance.type.IsContestMapCoord()) {
                        if (last_coordroute_instance && last_last_coordroute_instance) {
                            if (!coordroute_instance.type.IsEnrouteStartCoord()) {
                                if (last_coordroute_instance.type.IsEnrouteStartCoord()) { // iSP
                                    if (contestMap.contestMapPrintPoints.contains(coordroute_instance.title()+',')) {
                                        Map tp_coord = AviationMath.getOrthogonalTitlePoint(
                                            coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                            last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                            center_latitude, center_longitude,
                                            CONTESTMAP_TPNAME_DISTANCE
                                        )
                                        xml.wpt(lat:tp_coord.lat, lon:tp_coord.lon) {
                                            xml.name last_coordroute_instance.titleShortMap(isPrint)
                                            xml.sym "empty.png"
                                            xml.type ""
                                        }
                                    }
                                } else { // TP
                                    if (contestMap.contestMapPrintPoints.contains(last_coordroute_instance.title()+',')) {
                                        BigDecimal distancevalue_procedureturn = CONTESTMAP_TPNAME_PROCEDURETURN_DISTANCE
                                        if (coordroute_instance.endCurved) {
                                            distancevalue_procedureturn = CONTESTMAP_TPNAME_DISTANCE
                                        }
                                        Map tp_coord = AviationMath.getTitlePoint(
                                            last_last_coordroute_instance.latMath(), last_last_coordroute_instance.lonMath(),
                                            last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                            coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                            CONTESTMAP_TPNAME_DISTANCE, distancevalue_procedureturn
                                        )
                                        xml.wpt(lat:tp_coord.lat, lon:tp_coord.lon) {
                                            xml.name last_coordroute_instance.titleShortMap(isPrint)
                                            xml.sym "empty.png"
                                            xml.type ""
                                        }
                                    }
                                }
                            }
                            if (coordroute_instance.type.IsEnrouteFinishCoord()) { // FP, iFP
                                if (contestMap.contestMapPrintPoints.contains(coordroute_instance.title()+',')) {
                                    Map tp_coord = AviationMath.getOrthogonalTitlePoint(
                                        last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                        coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                        center_latitude, center_longitude,
                                        CONTESTMAP_TPNAME_DISTANCE
                                    )
                                    xml.wpt(lat:tp_coord.lat, lon:tp_coord.lon) {
                                        xml.name coordroute_instance.titleShortMap(isPrint)
                                        xml.sym "empty.png"
                                        xml.type ""
                                    }
                                }
                            }
                        } else if (last_coordroute_instance && last_coordroute_instance.type.IsEnrouteStartCoord()) { // SP
                            if (contestMap.contestMapPrintPoints.contains(last_coordroute_instance.title()+',')) {
                                Map tp_coord = AviationMath.getOrthogonalTitlePoint(
                                    coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                    last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                    center_latitude, center_longitude,
                                    CONTESTMAP_TPNAME_DISTANCE
                                )
                                xml.wpt(lat:tp_coord.lat, lon:tp_coord.lon) {
                                    xml.name last_coordroute_instance.titleShortMap(isPrint)
                                    xml.sym "empty.png"
                                    xml.type ""
                                }
                            }
                        }
                        last_last_coordroute_instance = last_coordroute_instance 
                        last_coordroute_instance = coordroute_instance
                    }
                }
            }
            
            // CoordEnroutePhoto
            if (contestMap.contestMapEnroutePhotos) {
                for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                    if (contestMap.contestMapPrintPoints.contains(coordenroutephoto_instance.title()+',')) {
                        Coord next_coord_instance = routeInstance.GetNextEnrouteSignCoord(coordenroutephoto_instance)
                        if (!next_coord_instance || contestMap.contestMapPrintPoints.contains(next_coord_instance.title()+',')) {
                            xml.wpt(lat:coordenroutephoto_instance.latMath(), lon:coordenroutephoto_instance.lonMath()) {
                                xml.name ""
                                xml.sym "fcphoto.png"
                                xml.type coordenroutephoto_instance.enroutePhotoName
                            }
                        }
                    }
                }
            }
            
            // CoordEnrouteCanvas
            if (contestMap.contestMapEnrouteCanvas) {
                for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                    if (contestMap.contestMapPrintPoints.contains(coordenroutecanvas_instance.title()+',')) {
                        Coord next_coord_instance = routeInstance.GetNextEnrouteSignCoord(coordenroutecanvas_instance)
                        if (!next_coord_instance || contestMap.contestMapPrintPoints.contains(next_coord_instance.title()+',')) {
                            xml.wpt(lat:coordenroutecanvas_instance.latMath(), lon:coordenroutecanvas_instance.lonMath()) {
                                xml.name ""
                                xml.sym "${coordenroutecanvas_instance.enrouteCanvasSign.toString().toLowerCase()}.png"
                                xml.type ""
                            }
                        }
                    }
                }
            }
            
            // Rahmen
            /*
            if (contestMap.contestMapFrame) {
                xml.rte {
                    xml.name "Graticule"
                    xml.rtept(lat:contest_map_rect.latmin, lon:contest_map_rect.lonmin)
                    xml.rtept(lat:contest_map_rect.latmin, lon:contest_map_rect.lonmax)
                    xml.rtept(lat:contest_map_rect.latmax, lon:contest_map_rect.lonmax)
                    xml.rtept(lat:contest_map_rect.latmax, lon:contest_map_rect.lonmin)
                    xml.rtept(lat:contest_map_rect.latmin, lon:contest_map_rect.lonmin)
                }
            }
            */
            
        } else {
            // gates
            CoordRoute last_coordroute_instance = null
            boolean first = true
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                boolean show_wpt = false
                switch (coordroute_instance.type) {
                    case CoordType.TO:
                        if (flighttestwindInstance) {
                            Map gate = AviationMath.getGate(
                                coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                flighttestwindInstance.TODirection, 
                                flighttestwindInstance.TOOffset,
                                flighttestwindInstance.TOOrthogonalOffset,
                                coordroute_instance.gatewidth2
                            )
                            xml.rte {
                                wrGate(coordroute_instance, xml, gate.coord.lat, gate.coord.lon)
                                xml.name coordroute_instance.titleShortMap(isPrint)
                                xml.rtept(lat:gate.coordRight.lat, lon:gate.coordRight.lon) {
                                    // xml.ele altitude_meter
                                }
                                xml.rtept(lat:gate.coordLeft.lat, lon:gate.coordLeft.lon) {
                                    // xml.ele altitude_meter
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
                            xml.rte {
                                wrGate(coordroute_instance, xml)
                                xml.name coordroute_instance.titleShortMap(isPrint)
                                xml.rtept(lat:gate.coordRight.lat, lon:gate.coordRight.lon) {
                                    // xml.ele altitude_meter
                                }
                                xml.rtept(lat:gate.coordLeft.lat, lon:gate.coordLeft.lon) {
                                    // xml.ele altitude_meter
                                }
                            }
                        }
                        break
                    case CoordType.LDG:
                        if (flighttestwindInstance) {
                            Map gate = AviationMath.getGate(
                                coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                flighttestwindInstance.LDGDirection, 
                                flighttestwindInstance.LDGOffset,
                                flighttestwindInstance.LDGOrthogonalOffset,
                                coordroute_instance.gatewidth2
                            )
                            xml.rte {
                                wrGate(coordroute_instance, xml, gate.coord.lat, gate.coord.lon)
                                xml.name coordroute_instance.titleShortMap(isPrint)
                                xml.rtept(lat:gate.coordRight.lat, lon:gate.coordRight.lon) {
                                    // xml.ele altitude_meter
                                }
                                xml.rtept(lat:gate.coordLeft.lat, lon:gate.coordLeft.lon) {
                                    // xml.ele altitude_meter
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
                            xml.rte {
                                wrGate(coordroute_instance, xml)
                                xml.name coordroute_instance.titleShortMap(isPrint)
                                xml.rtept(lat:gate.coordRight.lat, lon:gate.coordRight.lon) {
                                    // xml.ele altitude_meter
                                }
                                xml.rtept(lat:gate.coordLeft.lat, lon:gate.coordLeft.lon) {
                                    // xml.ele altitude_meter
                                }
                            }
                        }
                        break
                    case CoordType.iTO:
                    case CoordType.iLDG:
                        if (flighttestwindInstance) {
                            Map gate = AviationMath.getGate(
                                coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                flighttestwindInstance.iTOiLDGDirection, 
                                flighttestwindInstance.iTOiLDGOffset,
                                flighttestwindInstance.iTOiLDGOrthogonalOffset,
                                coordroute_instance.gatewidth2
                            )
                            xml.rte {
                                wrGate(coordroute_instance, xml, gate.coord.lat, gate.coord.lon)
                                xml.name coordroute_instance.titleShortMap(isPrint)
                                xml.rtept(lat:gate.coordRight.lat, lon:gate.coordRight.lon) {
                                    // xml.ele altitude_meter
                                }
                                xml.rtept(lat:gate.coordLeft.lat, lon:gate.coordLeft.lon) {
                                    // xml.ele altitude_meter
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
                            xml.rte {
                                wrGate(coordroute_instance, xml)
                                xml.name coordroute_instance.titleShortMap(isPrint)
                                xml.rtept(lat:gate.coordRight.lat, lon:gate.coordRight.lon) {
                                    // xml.ele altitude_meter
                                }
                                xml.rtept(lat:gate.coordLeft.lat, lon:gate.coordLeft.lon) {
                                    // xml.ele altitude_meter
                                }
                            }
                        }
                        break
                }
                if (show_wpt) {
                    xml.wpt(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                        xml.name coordroute_instance.titleShortMap(isPrint)
                    }
                }
                if (last_coordroute_instance && last_coordroute_instance.type.IsCpCheckCoord() && coordroute_instance.type.IsCpCheckCoord()) {
                    if (first) {
                        Map start_gate = AviationMath.getGate(
                            coordroute_instance.latMath(),coordroute_instance.lonMath(),
                            last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                            last_coordroute_instance.gatewidth2
                        )
                        xml.rte {
                            wrGate(last_coordroute_instance, xml)
                            //BigDecimal altitude_meter = last_coordroute_instance.altitude.toLong() / ftPerMeter
                            xml.name last_coordroute_instance.titleShortMap(isPrint)
                            xml.rtept(lat:start_gate.coordRight.lat, lon:start_gate.coordRight.lon) {
                                // xml.ele altitude_meter
                            }
                            xml.rtept(lat:start_gate.coordLeft.lat, lon:start_gate.coordLeft.lon) {
                                // xml.ele altitude_meter
                            }
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
                            Map gate = AviationMath.getGate(
                                last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                                coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                coordroute_instance.gatewidth2
                            )
                            xml.rte {
                                wrGate(coordroute_instance, xml)
                                // BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                                xml.name coordroute_instance.titleShortMap(isPrint)
                                xml.rtept(lat:gate.coordLeft.lat, lon:gate.coordLeft.lon) {
                                    // xml.ele altitude_meter
                                }
                                xml.rtept(lat:gate.coordRight.lat, lon:gate.coordRight.lon) {
                                    // xml.ele altitude_meter
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
        
        // Important points
        if (!contestMap) {
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                if (coordroute_instance.type.IsRunwayCoord()) {
                    xml.wpt(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                        xml.sym "airport"
                    }
                } else if (coordroute_instance.type.IsEnrouteStartCoord()) {
                    xml.wpt(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                        xml.sym "start"
                    }
                } else if (coordroute_instance.type.IsEnrouteFinishCoord()) {
                    xml.wpt(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                        xml.sym "finish"
                    }
                }
            }
        }
                        
        // CoordEnroutePhoto
        if (wr_enroutesign && routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
            for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                xml.wpt(lat:coordenroutephoto_instance.latMath(), lon:coordenroutephoto_instance.lonMath()) {
                    xml.extensions {
                        xml.flightcontest {
                            xml.enroutephotosign(
                                photoname:      coordenroutephoto_instance.enroutePhotoName,
                                viewpos:        coordenroutephoto_instance.enrouteViewPos,
                                type:           coordenroutephoto_instance.type,
                                number:         coordenroutephoto_instance.titleNumber,
                                dist:           coordenroutephoto_instance.enrouteDistance,
                                measuredist:    coordenroutephoto_instance.measureDistance,
                                orthogonaldist: coordenroutephoto_instance.enrouteOrthogonalDistance
                            )
                        }
                    }
                    xml.name coordenroutephoto_instance.enroutePhotoName
                    xml.sym "scenic area"
                }
            }
        }
        
        // CoordEnrouteCanvas
        if (wr_enroutesign && routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
            for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                xml.wpt(lat:coordenroutecanvas_instance.latMath(), lon:coordenroutecanvas_instance.lonMath()) {
                    xml.extensions {
                        xml.flightcontest {
                            xml.enroutecanvassign(
                                canvasname:     coordenroutecanvas_instance.enrouteCanvasSign.canvasName,
                                viewpos:        coordenroutecanvas_instance.enrouteViewPos,
                                type:           coordenroutecanvas_instance.type,
                                number:         coordenroutecanvas_instance.titleNumber,
                                dist:           coordenroutecanvas_instance.enrouteDistance,
                                measuredist:    coordenroutecanvas_instance.measureDistance,
                                orthogonaldist: coordenroutecanvas_instance.enrouteOrthogonalDistance
                            )
                        }
                    }
                    xml.name coordenroutecanvas_instance.enrouteCanvasSign.canvasName
                    xml.sym coordenroutecanvas_instance.enrouteCanvasSign.canvasName.toLowerCase()
                }
            }
        }
        
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    private BigDecimal GetRoundedDecimalGrad(BigDecimal decimalGrad)
    // Rundung auf ganze 10'
    {
        int grad_value = decimalGrad.toInteger()
        BigDecimal minute_value = 60 * (decimalGrad - grad_value)
        int minute_value2 = minute_value.toInteger()
        minute_value2 = (minute_value2.toBigDecimal() / 10).toInteger() * 10
        return minute_value2 / 60 + grad_value
    }
    
    //--------------------------------------------------------------------------
    private void wrGate(CoordRoute coordrouteInstance, MarkupBuilder xml, BigDecimal latValue = null, BigDecimal lonValue = null)
    {
        BigDecimal altitude_meter = coordrouteInstance.altitude.toLong() / ftPerMeter
        String dir = ""
        if (coordrouteInstance.type.IsRunwayCoord()) {
            dir = coordrouteInstance.gateDirection
        }
        if (latValue == null) {
            latValue = coordrouteInstance.latMath()
        }
        if (lonValue == null) {
            lonValue = coordrouteInstance.lonMath()
        }
        xml.extensions {
            xml.flightcontest {
                xml.gate(
                    type:           coordrouteInstance.type,
                    number:         coordrouteInstance.titleNumber,
                    lat:            latValue,
                    lon:            lonValue,
                    //ele:            altitude_meter,
                    alt:            coordrouteInstance.altitude,
                    width:          coordrouteInstance.gatewidth2,
                    dir:            dir,
                    notimecheck:    getYesNo(coordrouteInstance.noTimeCheck),
                    nogatecheck:    getYesNo(coordrouteInstance.noGateCheck),
                    noplanningtest: getYesNo(coordrouteInstance.noPlanningTest),
                    endcurved:      getYesNo(coordrouteInstance.endCurved),
                    dist:           coordrouteInstance.measureDistance,
                    track:          coordrouteInstance.measureTrueTrack,
                    duration:       coordrouteInstance.legDuration,
                    assignedsign:   coordrouteInstance.assignedSign,
                    correctsign:    coordrouteInstance.correctSign
                )
            }
        }
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
    private boolean GPXTrack(Test testInstance, int startNum, boolean isPrint, MarkupBuilder xml)
    // Return true: data found
    {
        printstart "GPXTrack"
        
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
            boolean show_curved_point = route_instance.contest.printStyle.contains('--route') && route_instance.contest.printStyle.contains('show-curved-points')
            List curved_point_titlecodes = route_instance.GetCurvedPointTitleCodes(isPrint)
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
            xml.trk {
                xml.name testInstance.crew.startNum
                xml.trkseg {
                    List enroute_points = []
                    int enroute_point_pos = 0
                    Map from_tp = [:]
                    for (Map p in track_points) {
                        
                        // <trkpt>
                        xml.trkpt(lat:p.latitude, lon:p.longitude) {
                            xml.ele p.altitude / GpxService.ftPerMeter
                            xml.time p.utc
                            
                            // add <extensions> for tp and flight errors
                            for (Map calc_result in calc_results) {
                                if (calc_result.utc == p.utc) {
                                    if (observationsign_used && !calc_result.badCourse && !calc_result.badTurn && !calc_result.gateMissed && !calc_result.gateNotFound && calc_result.coordType.IsEnrouteSignCoord()) { // cache enroute photo / canvas data
                                        enroute_points = RoutePointsTools.GetEnrouteSignShowPoints(route_instance,calc_result.coordType,calc_result.titleNumber)
                                        enroute_point_pos = 0
                                        from_tp = [titleCode:calc_result.titleCode, latitude:calc_result.latitude, longitude:calc_result.longitude]
                                    }
                                    xml.extensions {
                                        xml.flightcontest {
                                            if (calc_result.badCourse) {
                                                if (!calc_result.judgeDisabled && !calc_result.hide) {
                                                    String v = "yes"
                                                    if (calc_result.noBadCourse) {
                                                        v = "no"
                                                    }
                                                    if (calc_result.badCourseSeconds) {
                                                        xml.badcourse(duration:calc_result.badCourseSeconds, v)
                                                    } else {
                                                        xml.badcourse(v)
                                                    }
                                                }
                                            } else if (calc_result.badTurn) {
                                                if (!calc_result.judgeDisabled && !calc_result.hide) {
                                                    String v = "yes"
                                                    if (calc_result.noBadTurn) {
                                                        v = "no"
                                                    }
                                                    xml.badturn(v)
                                                }
                                            } else if (calc_result.gateMissed) {
                                                if (!calc_result.judgeDisabled && !calc_result.hide) {
                                                    String v = "yes"
                                                    if (calc_result.noGateMissed) {
                                                        v = "no"
                                                    }
                                                    xml.badgate(name:calc_result.titleCode, runway:getYesNo(calc_result.runway), v)
                                                }
                                            } else if (calc_result.gateNotFound) {
                                                if (!calc_result.judgeDisabled && !calc_result.hide) {
                                                    String v = "yes"
                                                    if (calc_result.noGateMissed) {
                                                        v = "no"
                                                    }
                                                    xml.gatenotfound(name:calc_result.titleCode, lat:calc_result.latitude, lon:calc_result.longitude, runway:getYesNo(calc_result.runway), v)
                                                }
                                            } else {
                                                if (show_curved_point || !(calc_result.titleCode in curved_point_titlecodes)) {
                                                    xml.gate(name:calc_result.titleCode, runway:getYesNo(calc_result.runway))
                                                }
                                            }
                                        } 
                                    }
                                    break
                                }
                                
                                // add <extensions> for enroute photo / canvas
                                if (observationsign_used && (enroute_point_pos < enroute_points.size())) {
                                    Map from_tp_leg = AviationMath.calculateLeg(p.latitude, p.longitude, from_tp.latitude, from_tp.longitude)
                                    if (enroute_points[enroute_point_pos].dist < from_tp_leg.dis) {
                                        xml.extensions {
                                            xml.flightcontest {
                                                if (enroute_points[enroute_point_pos].enroutePhoto) {
                                                    xml.enroutephoto(name:enroute_points[enroute_point_pos].name, lat:enroute_points[enroute_point_pos].latcenter, lon:enroute_points[enroute_point_pos].loncenter)
                                                } else {
                                                    xml.enroutecanvas(name:enroute_points[enroute_point_pos].name, lat:enroute_points[enroute_point_pos].latcenter, lon:enroute_points[enroute_point_pos].loncenter)
                                                }
                                            }
                                        }
                                        enroute_point_pos++
                                        break
                                    }
                                }
                            }
                        }
                        
                        found = true
                    }
                }
            }
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
    Map SendFTP(Object configFlightContest, String baseDir, String sourceURL, String destFileName)
    {
        printstart "SendFTP ${configFlightContest.ftp.host}:${configFlightContest.ftp.port}"
        Map ret = [:]
        try {
            String dest_file_name = "/${baseDir}/${destFileName}"
            println "${configFlightContest.ftp.host}:${configFlightContest.ftp.port}${dest_file_name}"
            new FTPClient().with {
                connect(configFlightContest.ftp.host, configFlightContest.ftp.port)
                enterLocalPassiveMode()
                if (login(configFlightContest.ftp.username, configFlightContest.ftp.password)) {
                    if (changeWorkingDirectory(baseDir) || makeDirectory(baseDir)) {
                        if (destFileName && sourceURL) {
                            def file = new URL(sourceURL).openStream()
                            if (storeFile(dest_file_name, file)) {
                                ret.message = getMsg('fc.net.ftp.filecopyok',[sourceURL,dest_file_name],false)
                            } else {
                                ret.message = getMsg('fc.net.ftp.filecopyerror',[sourceURL,dest_file_name],false)
                                ret.error = true
                            }
                            file.close()
                        }
                    } else {
                        ret.message = getMsg('fc.net.ftp.dircreateerror',[dir],false)
                        ret.error = true
                    }
                } else {
                    ret.message = getMsg('fc.net.ftp.loginerror',[configFlightContest.ftp.username],false)
                    ret.error = true
                }
                disconnect()
            }
            printdone ""
        } catch (Exception e) {
            ret.error = true
            ret.message = getMsg('fc.net.ftp.connecterror',["${configFlightContest.ftp.host}:${configFlightContest.ftp.port}"],false) + ": ${e.getMessage()}"
            printerror ret.message
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map SendFTP2(Object configFlightContest, String baseDir, String sourceURL, String destFileName) // TODO: getMsg
    {
        printstart "SendFTP2 ${configFlightContest.ftp.host}:${configFlightContest.ftp.port}"
        Map ret = [:]
        try {
            String dest_file_name = "/${baseDir}/${destFileName}"
            println "${configFlightContest.ftp.host}:${configFlightContest.ftp.port}${dest_file_name}"
            new FTPClient().with {
                connect(configFlightContest.ftp.host, configFlightContest.ftp.port)
                enterLocalPassiveMode()
                if (login(configFlightContest.ftp.username, configFlightContest.ftp.password)) {
                    if (changeWorkingDirectory(baseDir) || makeDirectory(baseDir)) {
                        if (destFileName && sourceURL) {
                            def file = new URL(sourceURL).openStream()
                            if (storeFile(dest_file_name, file)) {
                                ret.message = "getMsg('fc.net.ftp.filecopyok',[sourceURL,dest_file_name])"
                            } else {
                                ret.message = "getMsg('fc.net.ftp.filecopyerror',[sourceURL,dest_file_name])"
                                ret.error = true
                            }
                            file.close()
                        }
                    } else {
                        ret.message = "getMsg('fc.net.ftp.dircreateerror',[dir])"
                        ret.error = true
                    }
                } else {
                    ret.message = "getMsg('fc.net.ftp.loginerror',[configFlightContest.ftp.username])"
                    ret.error = true
                }
                disconnect()
            }
            printdone ""
        } catch (Exception e) {
            ret.error = true
            ret.message = """getMsg('fc.net.ftp.connecterror',["${configFlightContest.ftp.host}:${configFlightContest.ftp.port}"]) + ": ${e.getMessage()}" """
            printerror ret.message
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    public Map PublishLiveResults(long liveContestID, boolean uploadNoLiveResults)
    {
        Map ret = [error:false,failedDestinations:[]]
        if (BootStrap.global.IsLivePossible()) {
            printstart "PublishLiveResults ${new Date()}"
            long live_contest_id = liveContestID
            if (!live_contest_id) {
                live_contest_id = BootStrap.global.liveContestID
                println "Contest $live_contest_id (Live enabled)"
            } else {
                println "Contest $live_contest_id"
            }
            Contest.findAllById(live_contest_id).each { Contest contest_instance ->
                printstart "Contest '${contest_instance.title}'"
                
                String uuid = UUID.randomUUID().toString()
                String webroot_dir =  servletContext.getRealPath("/")
                String live_html_file_name = "${webroot_dir}${Defs.ROOT_FOLDER_GPXUPLOAD}/LIVE-${uuid}.htm"
                
                String live_url
                if (uploadNoLiveResults) {
                    live_url = "http://localhost:8080/fc/contest/listnoliveresults/${contest_instance.id}?lang=${BootStrap.global.liveLanguage}"
                } else {
                    live_url = "http://localhost:8080/fc/contest/listresultslive/${contest_instance.id}?lang=${BootStrap.global.liveLanguage}"
                }
                printstart "${live_url} -> ${live_html_file_name}"
                byte[] utf8_bom = new byte[3]
                utf8_bom[0] = (byte) 0xEF
                utf8_bom[1] = (byte) 0xBB
                utf8_bom[2] = (byte) 0xBF
                def live_html_file = new File(live_html_file_name).newOutputStream()
                live_html_file << new String(utf8_bom)
                live_html_file << new URL(live_url).openStream()
                live_html_file.close()
                printdone ""
                
                if (BootStrap.global.IsLiveFTPUploadPossible()) {
                    printstart "FTP-Upload"
                    String working_dir = grailsApplication.config.flightcontest.live.ftpupload.workingdir
                    if (!working_dir.startsWith("/")) {
                        working_dir = "/${working_dir}"
                    }
                    if (!working_dir.endsWith("/")) {
                        working_dir += "/"
                    }
                    Map ret_ftp = SendFTP2(
                        grailsApplication.config.flightcontest,
                        working_dir,
                        "file:${live_html_file_name}",
                        grailsApplication.config.flightcontest.live.ftpupload.name
                    )
                    if (ret_ftp.error) {
                        ret.error = true
                        ret.failedDestinations += grailsApplication.config.flightcontest.ftp.host
                        printerror ""
                    } else {
                        printdone ""
                    }
                }
                
                if (BootStrap.global.IsLiveCopyPossible()) {
                    printstart "Copy"
                    grailsApplication.config.flightcontest.live.copy.each { i, dest ->
                        String dest_file_name = dest.replaceAll('\\\\','/')
                        printstart "Copy to '$dest_file_name'"
                        def src_file = new File(live_html_file_name).newInputStream()
                        try {
                            def dest_file = new File(dest_file_name).newOutputStream()  
                            dest_file << src_file
                            dest_file.close()
                        } catch (Exception e) {
                            ret.error = true
                            ret.failedDestinations += dest_file_name
                            println "Error: ${e.getMessage()}"
                        }
                        src_file.close()
                        printdone ""
                    }
                    printdone ""
                }
                
                DeleteFile(live_html_file_name)
                
                printdone ""
            }
            printdone ""
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    public Map UploadStylesheet(String stylesheetName)
    {
        Map ret = [error:false,failedDestinations:[]]
        if (BootStrap.global.IsLivePossible()) {
            printstart "UploadStylesheet ${new Date()}"
            String webroot_dir =  servletContext.getRealPath("/")
            String stylesheet_file_name = "${webroot_dir}css/${stylesheetName}"
            
            if (BootStrap.global.IsLiveFTPUploadPossible()) {
                printstart "FTP-Upload"
                String working_dir = grailsApplication.config.flightcontest.live.ftpupload.workingdir
                if (!working_dir.startsWith("/")) {
                    working_dir = "/${working_dir}"
                }
                if (!working_dir.endsWith("/")) {
                    working_dir += "/"
                }
                Map ret_ftp = SendFTP2(
                    grailsApplication.config.flightcontest,
                    working_dir,
                    "file:${stylesheet_file_name}",
                    stylesheetName
                )
                if (ret_ftp.error) {
                    ret.error = true
                    ret.failedDestinations += grailsApplication.config.flightcontest.ftp.host
                    printerror ""
                } else {
                    printdone ""
                }
            }
            
            if (BootStrap.global.IsLiveCopyPossible()) {
                printstart "Copy"
                grailsApplication.config.flightcontest.live.copy.each { i, dest ->
                    String dest_file_name = dest.replaceAll('\\\\','/')
                    dest_file_name = "${dest_file_name.substring(0,dest_file_name.lastIndexOf('/')+1)}${stylesheetName}"
                    printstart "Copy to '$dest_file_name'"
                    def src_file = new File(stylesheet_file_name).newInputStream()
                    try {
                        def dest_file = new File(dest_file_name).newOutputStream()  
                        dest_file << src_file
                        dest_file.close()
                    } catch (Exception e) {
                        ret.error = true
                        ret.failedDestinations += dest_file_name
                        println "Error: ${e.getMessage()}"
                    }
                    src_file.close()
                    printdone ""
                }
                printdone ""
            }
            
            printdone ""
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    public void BackgroundUpload()
    {
        boolean found = false
        String webroot_dir = servletContext.getRealPath("/")
        String analyze_dir = webroot_dir + Defs.ROOT_FOLDER_JOBS
        File analyze_dir1 = new File(analyze_dir)
        analyze_dir1.eachFile() { File file ->
            if (file.isFile()) {
                if (!found) {
                    printstart "BackgroundUpload ${new Date()}"
                    found = true
                }
                
                printstart "Process '${file}'"
                
                LineNumberReader job_file_reader = file.newReader()
                String line = ""
                String ftp1_basedir
                String ftp1_sourceurl
                String ftp1_destfilename
                String ftp2_basedir
                String ftp2_sourceurl
                String ftp2_destfilename
                String email_to
                String email_subject
                String email_body
                String save_link
                String test_id
                String remove_file
                while (true) {
                    line = job_file_reader.readLine()
                    if (line == null) {
                        break
                    }
                    if (!ftp1_basedir) {
                        ftp1_basedir = line
                    } else if (!ftp1_sourceurl) {
                        ftp1_sourceurl = line
                    } else if (!ftp1_destfilename) {
                        ftp1_destfilename = line
                    } else if (!ftp2_basedir) {
                        ftp2_basedir = line
                    } else if (!ftp2_sourceurl) {
                        ftp2_sourceurl = line
                    } else if (!ftp2_destfilename) {
                        ftp2_destfilename = line
                    } else if (!email_to) {
                        email_to = line
                    } else if (!email_subject) {
                        email_subject = line
                    } else if (!email_body) {
                        email_body = line
                    } else if (!save_link) {
                        save_link = line
                    } else if (!test_id) {
                        test_id = line 
                    } else if (!remove_file) {
                        remove_file = line
                    }
                }
                job_file_reader.close()
                
                Map ret = [:]
                boolean test_instance_locked = false
                
                if (   ftp1_basedir && ftp1_sourceurl && ftp1_destfilename
                    && ftp2_basedir && ftp2_sourceurl && ftp2_destfilename
                    && email_to && email_subject && email_body && save_link && remove_file
                   ) 
                {
                    try {
                        Test test_instance = null
                        if (test_id && test_id.toLong()) {
                            test_instance = Test.get(test_id.toLong())
                            if (test_instance.isDirty()) {
                                println "Test instance locked: dirty"
                                test_instance_locked = true
                            }
                        }
                        if (!test_instance_locked) {
                        
                            // FTP upload gpx
                            ret = SendFTP2(
                                grailsApplication.config.flightcontest,ftp1_basedir,ftp1_sourceurl,ftp1_destfilename
                            )
            
                            // FTP upload html
                            if (!ret.error) {
                                ret = SendFTP2(
                                    grailsApplication.config.flightcontest,ftp2_basedir,ftp2_sourceurl,ftp2_destfilename
                                    
                                )
                            }
                            
                            if (!ret.error) {
                                try {
                                    // Send email
                                    mailService.sendMail {
                                        from grailsApplication.config.flightcontest.mail.from
                                        to NetTools.EMailList(email_to).toArray()
                                        if (grailsApplication.config.flightcontest.mail.cc) {
                                            List cc_list = NetTools.EMailReducedList(grailsApplication.config.flightcontest.mail.cc,email_to)
                                            if (cc_list) {
                                                cc cc_list.toArray()
                                            }
                                        }
                                        subject email_subject
                                        html email_body
                                        // TODO: body( view:"http://localhost:8080/fc/gpx/ftpgpxviewer", model:[fileName:GetEMailGpxURL(test.instance)])
                                    }
                                    
                                    // Save link
                                    if (test_instance) {
                                        println "Save link '$save_link'"
                                        test_instance.flightTestLink = save_link
                                        test_instance.save()
                                    }
                                    
                                    println "E-Mail send."
                                } catch (Exception e) {
                                    println "Error: ${e.getMessage()}" 
                                    ret.error = true
                                }
                            }
                            
                            if (ret.error) {
                                // Save link
                                if (test_instance) {
                                    println "Save link ''"
                                    test_instance.flightTestLink = ""
                                    test_instance.save()
                                }
                            }
                        }
                    } catch (Exception e) {
                        println "Test instance locked: ${e.getMessage()}" 
                        test_instance_locked = true
                    }
                } else {
                    println "No data."
                    ret.error = true
                }
                if (test_instance_locked) {
                    printdone ""
                } else if (!ret.error) {
                    DeleteFile(remove_file)
                    String new_file_name = webroot_dir + "${Defs.ROOT_FOLDER_JOBS_DONE}/" + file.name
                    file.renameTo(new File(new_file_name))
                    printdone new_file_name
                } else {
                    String new_file_name = webroot_dir + "${Defs.ROOT_FOLDER_JOBS_ERROR}/" + file.name
                    file.renameTo(new File(new_file_name))
                    printerror new_file_name
                }
            }
        }
        if (found) {
            printdone ""
        }
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
