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

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient

import org.gdal.gdal.BuildVRTOptions
import org.gdal.gdal.Dataset
import org.gdal.gdal.gdal

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
    
    final static BigDecimal CONTESTMAP_TPCIRCLE_RADIUS = 0.5 // NM
    final static BigDecimal CONTESTMAP_CIRCLECENTER_RADIUS = 0.15 // NM
    final static BigDecimal CONTESTMAP_PROCEDURETURN_DISTANCE = 1 // NM
    final static BigDecimal CONTESTMAP_TPNAME_DISTANCE = 1.5 // NM
    final static BigDecimal CONTESTMAP_TPNAME_RUNWAY_DISTANCE = 1 // NM
    final static BigDecimal CONTESTMAP_TPNAME_RUNWAY_DIRECTION = 0 // Grad
    final static BigDecimal CONTESTMAP_TPNAME_NOPROCEDURETURN_DISTANCE = 2.0 // NM
    final static BigDecimal CONTESTMAP_TPNAME_PROCEDURETURN_DISTANCE = 3.0 // NM
    final static BigDecimal CONTESTMAP_MARGIN_DISTANCE = 5 // NM
    final static BigDecimal CONTESTMAP_RUNWAY_FRAME_DISTANCE = 2 // NM  
    
	final static boolean WRLOG = false
    
    final static String XMLHEADER = "<?xml version='1.0' encoding='UTF-8'?>"
    
    final static String GPXVERSION = "1.1"
    final static String GPXCREATOR_CONTESTMAP = "Flight Contest - flightcontest.de - Contest Map - Version 1"
    final static String GAC_TRACKNAME = "GAC track"
    final static String IGC_TRACKNAME = "IGC track"
    final static String KML_TRACKNAME = "KML track"
    final static String NMEA_TRACKNAME = "NMEA track"
    
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
    Map ConvertKML2GPX(String kmFileName, String gpxFileName, Route routeInstance, boolean kmzFile)
    {
        Map ret = [:]
        boolean converted = true
        String err_msg = ""
        
        printstart "ConvertKML2GPX $kmFileName -> $gpxFileName"
        
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
            km_reader = new FileReader(km_file)
        }

        File gpx_file = new File(gpxFileName)
        
        boolean repairIdenticalTimes = false
        String line = ""
        
        BufferedWriter gpx_writer = gpx_file.newWriter()
        MarkupBuilder xml = new MarkupBuilder(gpx_writer)
        gpx_writer.writeLine(XMLHEADER)
        xml.gpx(version:GPXVERSION, creator:Defs.ROUTEEXPORT_CREATOR) {
            xml.trk {
                xml.name KML_TRACKNAME
                xml.trkseg {
                    try {
                        boolean valid_kml_format = false
                        
                        def kml = new XmlParser().parse(km_reader)
                        def track = kml.Document.Placemark.'gx:Track'
                        
                        if (track && track.when && track.'gx:coord' && track.when.size() == track.'gx:coord'.size()) {
                            
                            boolean first = true
                            String last_time_utc = null
                            BigDecimal latitude = null
                            BigDecimal longitude = null
                            String last_utc = FcTime.UTC_GPX_DATE
                            
                            int i = 0
                            while (i < track.when.size()) {
                                valid_kml_format = true
                                
                                String time_utc = track.when[i].text()
                                String coord = track.'gx:coord'[i].text()
                                def coord_values = coord.split(' ')
                                
                                // Repair DropOuts
                                boolean ignore_value = false 
                                if (!first) {
                                    if (repairIdenticalTimes) {
                                        if (last_time_utc == time_utc) { // Zeile mit doppelter Zeit entfernen
                                            ignore_value = true
                                            if (WRLOG) {
                                                println "Ignore '$time_utc' '$coord'"
                                            }
                                        }
                                    }
                                }
                                
                                // UTC
                                String utc = FcTime.UTCGetNextDateTime(last_utc, "${time_utc.substring(11,19)}")
                                
                                // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                                latitude = coord_values[1].toBigDecimal() 
                                
                                // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                                longitude = coord_values[0].toBigDecimal()
                                
                                // Altitude (Höhe)
                                BigDecimal altitude_meter = coord_values[2].toBigDecimal()
                                
                                // write gpx tag
                                if (!ignore_value) {
                                    // println "UTC: $utc, Latitude: $latitude, Longitude: $longitude, Altitude: ${altitude_meter}m"
                                    xml.trkpt(lat:latitude,lon:longitude) {
                                        xml.ele altitude_meter
                                        xml.time utc
                                    }
                                }
                                
                                first = false
                                last_time_utc = time_utc
                                last_utc = utc
                                
                                i++
                            }
                        }
                            
                        if (!valid_kml_format) {
                            converted = false
                            err_msg = "No supported kml format."
                        }
                    } catch (Exception e) {
                        converted = false
                        err_msg = e.getMessage()
                    }
                }
            }
            if (routeInstance) {
                gpx_route(routeInstance, null, xml, [isPrint:false, wrEnrouteSign:false, gpxExport:false])
            }
        }
        gpx_writer.close()
        if (km_reader) {
            km_reader.close()
        }
        if (kmz_file) {
            kmz_file.close()
        }

        if (converted) {
            printdone ""
        } else {
            printerror err_msg
        }
        if (routeInstance) {
            println "Generate points for buttons (GetShowPointsRoute)"
            ret += [gpxShowPoints:RoutePointsTools.GetShowPointsRoute(routeInstance, null, messageSource, [isPrint:false, wrEnrouteSign:false, showCurvedPoints:false])]
        }
        ret += [ok:converted]
        return ret
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
        xml.gpx(version:GPXVERSION, creator:Defs.ROUTEEXPORT_CREATOR) {
            xml.trk {
                xml.name GAC_TRACKNAME
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
                gpx_route(routeInstance, null, xml, [isPrint:false, wrEnrouteSign:false, gpxExport:false])
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
            ret += [gpxShowPoints:RoutePointsTools.GetShowPointsRoute(routeInstance, null, messageSource, [isPrint:false, wrEnrouteSign:false, showCurvedPoints:false])]
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
        xml.gpx(version:GPXVERSION, creator:Defs.ROUTEEXPORT_CREATOR) {
            xml.trk {
                xml.name IGC_TRACKNAME
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
                            err_msg = "No supported igc format."
                        }
                    } catch (Exception e) {
                        converted = false
                        err_msg = e.getMessage()
                    }
                }
            }
            if (routeInstance) {
                gpx_route(routeInstance, null, xml, [isPrint:false, wrEnrouteSign:false, gpxExport:false])
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
            ret += [gpxShowPoints:RoutePointsTools.GetShowPointsRoute(routeInstance, null, messageSource, [isPrint:false, wrEnrouteSign:false, showCurvedPoints:false])]
        }
        ret += [ok:converted]
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map ConvertNMEA2GPX(String nmeaFileName, String gpxFileName, Route routeInstance)
    {
        Map ret = [:]
        boolean converted = true
        String err_msg = ""
        
        printstart "ConvertNMEA2GPX $nmeaFileName -> $gpxFileName"
        
        File nmea_file = new File(nmeaFileName)
        File gpx_file = new File(gpxFileName)
        
        boolean repairIdenticalTimes = false
        String line = ""
        
        LineNumberReader nmea_reader = nmea_file.newReader()
        BufferedWriter gpx_writer = gpx_file.newWriter()
        MarkupBuilder xml = new MarkupBuilder(gpx_writer)
        gpx_writer.writeLine(XMLHEADER)
        xml.gpx(version:GPXVERSION, creator:Defs.ROUTEEXPORT_CREATOR) {
            xml.trk {
                xml.name NMEA_TRACKNAME
                xml.trkseg {
                    try {
                        boolean first = true
                        String last_time_utc = null
                        BigDecimal latitude = null
                        BigDecimal longitude = null
                        boolean valid_nmea_format = false
                        String last_utc = FcTime.UTC_GPX_DATE
                        while (true) {
                            line = nmea_reader.readLine()
                            if (line == null) {
                                break
                            }
                            if (line) {
                                if (line.startsWith('$GPGGA')) {
                                    valid_nmea_format = true
                                }   
                                boolean ignore_line = false 
                                if (line.startsWith('$GPGGA')) {
                                    if (valid_nmea_format) {
                                        
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
                                        
                                        String[] line_values = line.split(",")
                                         
                                        // UTC
                                        String utc_h = line_values[1].substring(0,2)
                                        String utc_min = line_values[1].substring(2,4)
                                        String utc_s = line_values[1].substring(4,6)
                                        String utc = FcTime.UTCGetNextDateTime(last_utc, "${utc_h}:${utc_min}:${utc_s}")
                                        
                                        // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                                        String latitude_grad = line_values[2].substring(0,2)
                                        BigDecimal latitude_grad_math = latitude_grad.toBigDecimal()
                                        String latidude_minute = line_values[2].substring(2)
                                        BigDecimal latidude_minute_math = latidude_minute.toBigDecimal()
                                        boolean latitude_north = line_values[3] == CoordPresentation.NORTH
                                        latitude = latitude_grad_math + (latidude_minute_math / 60)
                                        if (!latitude_north) {
                                            latitude *= -1
                                        }
                                        
                                        // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                                        String longitude_grad = line_values[4].substring(0,3)
                                        BigDecimal longitude_grad_math = longitude_grad.toBigDecimal()
                                        String longitude_minute = line_values[4].substring(3)
                                        BigDecimal longitude_minute_math = longitude_minute.toBigDecimal()
                                        boolean longitude_east = line_values[5] == CoordPresentation.EAST
                                        longitude = longitude_grad_math + (longitude_minute_math / 60)
                                        if (!longitude_east) {
                                            longitude *= -1
                                        }
                                        
                                        // Altitude (Höhe)
                                        String altitude_meter = "0.0"
                                        if (line_values[10] == 'M') {
                                            altitude_meter = line_values[9]
                                        }
                                        
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
                        if (!valid_nmea_format) {
                            converted = false
                            err_msg = "No supported nmea format."
                        }
                    } catch (Exception e) {
                        converted = false
                        err_msg = e.getMessage()
                    }
                }
            }
            if (routeInstance) {
                gpx_route(routeInstance, null, xml, [isPrint:false, wrEnrouteSign:false, gpxExport:false])
            }
        }
        nmea_reader.close()
        gpx_writer.close()

        if (converted) {
            printdone ""
        } else {
            printerror err_msg
        }
        if (routeInstance) {
            println "Generate points for buttons (GetShowPointsRoute)"
            ret += [gpxShowPoints:RoutePointsTools.GetShowPointsRoute(routeInstance, null, messageSource, [isPrint:false, wrEnrouteSign:false, showCurvedPoints:false])]
        }
        ret += [ok:converted]
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map ConvertRoute2GPX(Route routeInstance, String gpxFileName, Map params, Map contestMapParams = [:])
    {
        printstart "ConvertRoute2GPX ${routeInstance.GetName(params.isPrint)} -> ${gpxFileName}"
        
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
        if (contestMapParams) {
            xml.gpx(version:GPXVERSION, creator:GPXCREATOR_CONTESTMAP) {
                gpx_route(routeInstance, null, xml, params, contestMapParams)
            }
        } else {
            xml.gpx(version:GPXVERSION, creator:Defs.ROUTEEXPORT_CREATOR) {
                gpx_route(routeInstance, null, xml, params, [:])
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
        if (params.showPoints) {
            println "Generate points for buttons (GetShowPointsRoute)"
            show_points = RoutePointsTools.GetShowPointsRoute(routeInstance, null, messageSource, [isPrint:params.isPrint, wrEnrouteSign:params.wrEnrouteSign, showCurvedPoints:false, noCircleCenterPoints:params.noCircleCenterPoints], contestMapParams)
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
        xml.gpx(version:GPXVERSION, creator:Defs.ROUTEEXPORT_CREATOR) {

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
            
            gpx_route(routeInstance, null, xml, [isPrint:false, wrEnrouteSign:false, gpxExport:false])
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
        ret += [gpxShowPoints:RoutePointsTools.GetShowPointsRoute(routeInstance, null, messageSource, [isPrint:false, wrEnrouteSign:false, showCurvedPoints:false])]
        ret += [ok:converted]
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map ConvertTest2PrintMap(Test testInstance, String gpxFileName, String pngFileName) 
    {
        printstart "ConvertTest2PrintMap"
        Map ret = ConvertTest2GPX(testInstance,gpxFileName, [isPrint:true, showPoints:false, wrEnrouteSign:false, gpxExport:false])
        if (ret.ok && ret.track) {
            printstart "Generate ${pngFileName} from ${gpxFileName}"
            
            Map xy = convert_gpx_2_xy(gpxFileName, MAP_WIDTH, MAP_HEIGHT, false, true, "", "", "", "", testInstance.task.contest.timeZone, testInstance.task.contest.coordPresentation) // false - no forcePortrait, true - Print
            
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
    private Map convert_gpx_2_xy(String gpxFileName, int maxX, int maxY, boolean forcePortrait, boolean isPrint, 
                                 centerLat, centerLon, radiusValue, moveDir, String timeZone, CoordPresentation coordPresentation)
    {
        printstart "convert_gpx_2_xy $gpxFileName"
        
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
        
        Map xy_values = convert_gpx_2_xy(gpxFileName, maxX, maxY, true, false, centerLat, centerLon, radiusValue, moveDir, timeZone, coordPresentation) // true - forcePortrait, false - no Print
        
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
    Map ConvertTest2GPX(Test testInstance, String gpxFileName, Map params)
    {
        boolean found_track = false
        
        Route route_instance = testInstance.task.flighttest.route
        
        printstart "ConvertTest2GPX '${testInstance.crew.name}' -> ${gpxFileName} [${params}]"
        
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
        xml.gpx(version:GPXVERSION, creator:Defs.ROUTEEXPORT_CREATOR) {
            gpx_route(route_instance, testInstance, xml, params)
            found_track = gpx_track(testInstance, xml, params)
        }
        gpx_writer.close()
        if (gpxFileName.startsWith(GPXDATA)) {
            BootStrap.tempData.AddData(gpxFileName, gpx_data.toCharArray())
            printdone "${gpx_data.size()} bytes"
        } else {
            printdone "${gpx_file.size()} bytes"
        }
        
        List show_points = []
        if (params.showPoints) {
            if (testInstance.IsLoggerResultWithoutRunwayMissed()) {
                println "Generate points for buttons (GetShowPoints)"
                show_points = GetShowPoints(gpxFileName, params.wrEnrouteSign)
            } else {
                println "Generate points for buttons (GetShowPointsRoute)"
                show_points = RoutePointsTools.GetShowPointsRoute(route_instance, testInstance, messageSource, [isPrint:params.isPrint, wrEnrouteSign:params.wrEnrouteSign, showCurvedPoints:false])
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
    private void gpx_route(Route routeInstance, Test testInstance, MarkupBuilder xml, Map params, Map contestMapParams = [:])
    {
        printstart "gpx_route $params $contestMapParams"
        
        boolean wr_enroutesign = params.wrEnrouteSign
        if (params.wrEnrouteSign && testInstance) {
            wr_enroutesign = testInstance.flighttestwind.flighttest.IsObservationSignUsed()
        }
        boolean wr_photoimage = false
        if (params.wrPhotoImage) {
            wr_photoimage = true
        }
        List curved_point_ids = routeInstance.GetCurvedPointIds()
        Media media = Media.Screen
        boolean is_print = false
        if (params.isTracking) {
            media = Media.Tracking
            is_print = true
        } else if (params.isPrint) {
            media = Media.Print
            is_print = true
        }
        Task task_instance = null
        if (params.taskInstance) {
            task_instance = params.taskInstance
        }

        // observation settings & enroute signs without position
        Map contest_map_rect = [:]
        BigDecimal center_latitude = null
        BigDecimal center_longitude = null
        boolean show_runway = false
        if (wr_enroutesign || contestMapParams) {
            xml.extensions {
                xml.flightcontest {
                    if (wr_enroutesign) {
                        xml.observationsettings(
                            turnpoint: routeInstance.turnpointRoute,
                            turnpointmapmeasurement: getYesNo(routeInstance.turnpointMapMeasurement),
                            turnpointprintstyle: routeInstance.turnpointPrintStyle,
                            turnpointprintpositionmaker: getYesNo(routeInstance.turnpointPrintPositionMaker),
                            enroutephoto: routeInstance.enroutePhotoRoute,
                            enroutephotomeasurement: routeInstance.enroutePhotoMeasurement,
                            enroutephotoprintstyle: routeInstance.enroutePhotoPrintStyle,
                            enroutephotoprintpositionmarker: getYesNo(routeInstance.enroutePhotoPrintPositionMaker),
                            enroutecanvas: routeInstance.enrouteCanvasRoute,
                            enroutecanvasmeasurement: routeInstance.enrouteCanvasMeasurement,
                            useprocedureturn: getYesNo(routeInstance.useProcedureTurns),
							mapscale: routeInstance.mapScale,
							altitudeaboveground: routeInstance.altitudeAboveGround
                        )
                        xml.mapsettings(
                            contestmapairfields: routeInstance.contestMapAirfields,
                            contestmapcircle: getYesNo(routeInstance.contestMapCircle),
                            contestmapprocedureturn: getYesNo(routeInstance.contestMapProcedureTurn),
                            contestmapleg: getYesNo(routeInstance.contestMapLeg),
                            contestmapcurvedleg: getYesNo(routeInstance.contestMapCurvedLeg),
                            contestmapcurvedlegpoints: routeInstance.contestMapCurvedLegPoints,
                            contestmaptpname: getYesNo(routeInstance.contestMapTpName),
                            contestmapsecretgates: getYesNo(routeInstance.contestMapSecretGates),
                            contestmapenroutephotos: getYesNo(routeInstance.contestMapEnroutePhotos),
                            contestmapenroutecanvas: getYesNo(routeInstance.contestMapEnrouteCanvas),
                            contestmapgraticule: getYesNo(routeInstance.contestMapGraticule),
                            contestmapcontourlines: routeInstance.contestMapContourLines,
                            contestmapmunicipalitynames: getYesNo(routeInstance.contestMapMunicipalityNames),
                            contestmapchurches: getYesNo(routeInstance.contestMapChurches),
                            contestmapcastles: getYesNo(routeInstance.contestMapCastles),
                            contestmapchateaus: getYesNo(routeInstance.contestMapChateaus),
                            contestmappowerlines: getYesNo(routeInstance.contestMapPowerlines),
                            contestmapwindpowerstations: getYesNo(routeInstance.contestMapWindpowerstations),
                            contestmapsmallroads: getYesNo(routeInstance.contestMapSmallRoads),
                            contestmappeaks: getYesNo(routeInstance.contestMapPeaks),
                            contestmapdropshadow: getYesNo(routeInstance.contestMapDropShadow),
                            contestmapadditionals: getYesNo(routeInstance.contestMapAdditionals),
                            contestmapspecials: getYesNo(routeInstance.contestMapSpecials),
                            contestmapairspaces: getYesNo(routeInstance.contestMapAirspaces),
                            contestmapairspaceslayer: routeInstance.contestMapAirspacesLayer,
                            contestmapshowoptions1: getYesNo(routeInstance.contestMapShowFirstOptions),
                            contestmaptitle1: routeInstance.contestMapFirstTitle,
                            contestmapcenterverticalpos1: routeInstance.contestMapCenterVerticalPos,
                            contestmapcenterhorizontalpos1: routeInstance.contestMapCenterHorizontalPos,
                            contestmapcenterpoints1: routeInstance.contestMapCenterPoints,
                            contestmapprintpoints1: routeInstance.contestMapPrintPoints,
                            contestmapprintlandscape1: getYesNo(routeInstance.contestMapPrintLandscape),
                            contestmapprintsize1: routeInstance.contestMapPrintSize,
                            contestmapshowoptions2: getYesNo(routeInstance.contestMapShowSecondOptions),
                            contestmaptitle2: routeInstance.contestMapSecondTitle,
                            contestmapcenterverticalpos2: routeInstance.contestMapCenterVerticalPos2,
                            contestmapcenterhorizontalpos2: routeInstance.contestMapCenterHorizontalPos2,
                            contestmapcenterpoints2: routeInstance.contestMapCenterPoints2,
                            contestmapprintpoints2: routeInstance.contestMapPrintPoints2,
                            contestmapprintlandscape2: getYesNo(routeInstance.contestMapPrintLandscape2),
                            contestmapprintsize2: routeInstance.contestMapPrintSize2,
                            contestmapshowoptions3: getYesNo(routeInstance.contestMapShowThirdOptions),
                            contestmaptitle3: routeInstance.contestMapThirdTitle,
                            contestmapcenterverticalpos3: routeInstance.contestMapCenterVerticalPos3,
                            contestmapcenterhorizontalpos3: routeInstance.contestMapCenterHorizontalPos3,
                            contestmapcenterpoints3: routeInstance.contestMapCenterPoints3,
                            contestmapprintpoints3: routeInstance.contestMapPrintPoints3,
                            contestmapprintlandscape3: getYesNo(routeInstance.contestMapPrintLandscape3),
                            contestmapprintsize3: routeInstance.contestMapPrintSize3,
                            contestmapshowoptions4: getYesNo(routeInstance.contestMapShowForthOptions),
                            contestmaptitle4: routeInstance.contestMapForthTitle,
                            contestmapcenterverticalpos4: routeInstance.contestMapCenterVerticalPos4,
                            contestmapcenterhorizontalpos4: routeInstance.contestMapCenterHorizontalPos4,
                            contestmapcenterpoints4: routeInstance.contestMapCenterPoints4,
                            contestmapprintpoints4: routeInstance.contestMapPrintPoints4,
                            contestmapprintlandscape4: getYesNo(routeInstance.contestMapPrintLandscape4),
                            contestmapprintsize4: routeInstance.contestMapPrintSize4
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
                    if (contestMapParams) {
                        BigDecimal min_latitude = null
                        BigDecimal min_longitude = null
                        BigDecimal max_latitude = null
                        BigDecimal max_longitude = null
                        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                            if (coordroute_instance.type.IsContestMapQuestionCoord()) {
                                if (!contestMapParams.contestMapCenterPoints || DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapCenterPoints).contains(coordroute_instance.title()+',')) {
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
                            min_latitude: min_latitude,
                            min_longitude: min_longitude,
                            max_latitude: max_latitude,
                            max_longitude: max_longitude,
                            center_latitude: center_latitude,
                            center_longitude: center_longitude,
                            center_graticule_latitude: GetRoundedDecimalGrad(center_latitude),
                            center_graticule_longitude: GetRoundedDecimalGrad(center_longitude)
                        )
                    }
                }
            }
        }
        
        // tracks
        if (contestMapParams) {
            if (show_runway) {
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (coordroute_instance.type.IsRunwayCoord()) {
                        xml.wpt(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                            xml.name "${coordroute_instance.titleMediaCode(media)}"
                            xml.sym "airport"
                            xml.type ""
                        }
                    }
                }
            } else if (contestMapParams.contestMapLeg || contestMapParams.contestMapCurvedLeg) {
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
                        if (semicircle_coords.size() > 2) {
                            xml.rte {
                                xml.name "${start_coordroute_instance.titleMediaCode(media)} - ${coordroute_instance.titleMediaCode(media)}"
                                int semicircle_coord_pos = 0
                                for (Map semicircle_coord in semicircle_coords) {
                                    if (semicircle_coord_pos == 1) {
                                        Map track_coords = AviationMath.getTrack2Circle(
                                            semicircle_coords[0].lat, semicircle_coords[0].lon,
                                            semicircle_coords[1].lat, semicircle_coords[1].lon,
                                            CONTESTMAP_TPCIRCLE_RADIUS
                                        )
                                        xml.rtept(lat:track_coords.srcLat, lon:track_coords.srcLon)
                                        //xml.rtept(lat:track_coords.destLat, lon:track_coords.destLon)
                                    }
                                    if (semicircle_coord_pos > 0) {
                                        xml.rtept(lat:semicircle_coord.lat, lon:semicircle_coord.lon) {
                                            // xml.ele altitude_meter
                                        }
                                    }
                                    semicircle_coord_pos++
                                }
                                Map track_coords = AviationMath.getTrack2Circle(
                                    semicircle_coords[semicircle_coord_pos-1].lat, semicircle_coords[semicircle_coord_pos-1].lon,
                                    coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                    CONTESTMAP_TPCIRCLE_RADIUS
                                )
                                //xml.rtept(lat:track_coords.srcLat, lon:track_coords.srcLon)
                                xml.rtept(lat:track_coords.destLat, lon:track_coords.destLon)
                            }
                        }
                        start_coordroute_instance = null
                        center_coordroute_instance = null
                        last_coordroute_instance = null // no leg line
                    } else if (coordroute_instance.circleCenter) {
                        start_coordroute_instance = last_coordroute_instance
                        center_coordroute_instance = coordroute_instance
                    }
                    if (coordroute_instance.type.IsContestMapCoord()) { // no secret and runway points
                        if (last_coordroute_instance) {
                            if (coordroute_instance.endCurved) {
                                if (contestMapParams.contestMapCurvedLeg && DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapCurvedLegPoints).contains(coordroute_instance.title()+',') && DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(coordroute_instance.title()+',') && DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(last_coordroute_instance.title()+',')) {
                                    xml.rte {
                                        xml.name "${last_coordroute_instance.titleMediaCode(media)} - ${coordroute_instance.titleMediaCode(media)}"
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
                                                            CONTESTMAP_TPCIRCLE_RADIUS
                                                        )
                                                        xml.rtept(lat:track_coords.srcLat, lon:track_coords.srcLon)
                                                    } else if (coordroute_instance2 == coordroute_instance) { // last track
                                                        Map track_coords = AviationMath.getTrack2Circle(
                                                            last_coordroute_instance2.latMath(), last_coordroute_instance2.lonMath(),
                                                            coordroute_instance2.latMath(), coordroute_instance2.lonMath(),
                                                            CONTESTMAP_TPCIRCLE_RADIUS
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
                            } else if (contestMapParams.contestMapLeg) {
                                if (DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(coordroute_instance.title()+',') && DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(last_coordroute_instance.title()+',')) {
                                    Map track_coords = AviationMath.getTrack2Circle(
                                        last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                        coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                        CONTESTMAP_TPCIRCLE_RADIUS
                                    )
                                    xml.rte {
                                        xml.name "${last_coordroute_instance.titleMediaCode(media)} - ${coordroute_instance.titleMediaCode(media)}"
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
            }
        } else {
            long restart_id = 0
            xml.rte {
                xml.extensions {
                    xml.flightcontest {
                        xml.route(number:1)
                    }
                }
                xml.name routeInstance.title.encodeAsHTML()
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
                            xml.rtept(lat:semicircle_coord.lat, lon:semicircle_coord.lon) {
                                // xml.ele altitude_meter
                            }
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
                            xml.rtept(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                                xml.name coordroute_instance.titleMediaCode(media)
                                xml.ele altitude_meter
                            }
                        }
                        if (coordroute_instance.type == CoordType.iFP) {
                            restart_id = coordroute_instance.id
                            break
                        }
                    }
                    last_coordroute_instance = coordroute_instance
                }
            }
            if (restart_id != 0) {
                xml.rte {
                    xml.extensions {
                        xml.flightcontest {
                            xml.route(number:2)
                        }
                    }
                    xml.name routeInstance.title.encodeAsHTML()
                    boolean run = false
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
                                    xml.rtept(lat:semicircle_coord.lat, lon:semicircle_coord.lon) {
                                        // xml.ele altitude_meter
                                    }
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
                                    xml.rtept(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                                        xml.name coordroute_instance.titleMediaCode(media)
                                        xml.ele altitude_meter
                                    }
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
                }
            }
        }
        
        if (contestMapParams) {
            // circles
            if (contestMapParams.contestMapCircle) {
                CoordRoute last_coordroute_instance = null
                CoordRoute last_last_coordroute_instance = null
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (coordroute_instance.type.IsContestMapCoord()) {
                        if (DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(coordroute_instance.title()+',')) {
                            List circle_coords = AviationMath.getCircle(coordroute_instance.latMath(), coordroute_instance.lonMath(), CONTESTMAP_TPCIRCLE_RADIUS)
                            xml.rte {
                                // BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                                xml.name "Circle ${coordroute_instance.titleMediaCode(media)}"
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
            if (contestMapParams.contestMapProcedureTurn && routeInstance.useProcedureTurns) {
                CoordRoute last_coordroute_instance = null
                CoordRoute last_coordroute_instance2 = null
                CoordRoute last_last_coordroute_instance = null
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (coordroute_instance.planProcedureTurn && last_coordroute_instance && last_last_coordroute_instance && last_coordroute_instance.type.IsProcedureTurnCoord()) {
                        if (DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(last_coordroute_instance.title()+',')) {
                            List circle_coords = AviationMath.getProcedureTurnCircle(
                                last_last_coordroute_instance.latMath(), last_last_coordroute_instance.lonMath(),
                                last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                CONTESTMAP_TPCIRCLE_RADIUS,
                                CONTESTMAP_PROCEDURETURN_DISTANCE
                            )
                            xml.rte {
                                // BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                                xml.name "Procedure turn ${last_coordroute_instance.titleMediaCode(media)}"
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
            
            // TP names
            if (contestMapParams.contestMapTpName) {
                CoordRoute last_coordroute_instance = null
                CoordRoute last_coordroute_instance2 = null
                CoordRoute last_last_coordroute_instance = null
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (coordroute_instance.type.IsContestMapCoord()) {
                        if (last_coordroute_instance && last_last_coordroute_instance) {
                            if (!coordroute_instance.type.IsEnrouteStartCoord()) {
                                if (last_coordroute_instance.type.IsEnrouteStartCoord()) { // iSP
                                    if (DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(coordroute_instance.title()+',')) {
                                        Map tp_coord = AviationMath.getOrthogonalTitlePoint(
                                            coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                            last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                            center_latitude, center_longitude,
                                            CONTESTMAP_TPNAME_DISTANCE
                                        )
                                        xml.wpt(lat:tp_coord.lat, lon:tp_coord.lon) {
                                            xml.name last_coordroute_instance.titleMediaCode(media)
                                            xml.sym "empty.png"
                                            xml.type ""
                                        }
                                    }
                                } else { // TP
                                    if (DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(last_coordroute_instance.title()+',')) {
                                        BigDecimal distancevalue_procedureturn = CONTESTMAP_TPNAME_PROCEDURETURN_DISTANCE
                                        if (coordroute_instance.endCurved) {
                                            distancevalue_procedureturn = CONTESTMAP_TPNAME_DISTANCE
                                        } else if (!routeInstance.useProcedureTurns) {
                                            distancevalue_procedureturn = CONTESTMAP_TPNAME_NOPROCEDURETURN_DISTANCE
                                        }
                                        Map tp_coord = AviationMath.getTitlePoint(
                                            last_last_coordroute_instance.latMath(), last_last_coordroute_instance.lonMath(),
                                            last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                            coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                            CONTESTMAP_TPNAME_DISTANCE, distancevalue_procedureturn
                                        )
                                        xml.wpt(lat:tp_coord.lat, lon:tp_coord.lon) {
                                            xml.name last_coordroute_instance.titleMediaCode(media)
                                            xml.sym "empty.png"
                                            xml.type ""
                                        }
                                    }
                                }
                            }
                            if (coordroute_instance.type.IsEnrouteFinishCoord()) { // FP, iFP
                                if (DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(coordroute_instance.title()+',')) {
                                    Map tp_coord = AviationMath.getOrthogonalTitlePoint(
                                        last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                        coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                        center_latitude, center_longitude,
                                        CONTESTMAP_TPNAME_DISTANCE
                                    )
                                    xml.wpt(lat:tp_coord.lat, lon:tp_coord.lon) {
                                        xml.name coordroute_instance.titleMediaCode(media)
                                        xml.sym "empty.png"
                                        xml.type ""
                                    }
                                }
                            }
                        } else if (last_coordroute_instance && last_coordroute_instance.type.IsEnrouteStartCoord()) { // SP
                            if (DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(last_coordroute_instance.title()+',')) {
                                Map tp_coord = AviationMath.getOrthogonalTitlePoint(
                                    coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                    last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                                    center_latitude, center_longitude,
                                    CONTESTMAP_TPNAME_DISTANCE
                                )
                                xml.wpt(lat:tp_coord.lat, lon:tp_coord.lon) {
                                    xml.name last_coordroute_instance.titleMediaCode(media)
                                    xml.sym "empty.png"
                                    xml.type ""
                                }
                            }
                        }
                        last_last_coordroute_instance = last_coordroute_instance 
                        last_coordroute_instance = coordroute_instance
                    } else if (coordroute_instance.type.IsRunwayCoord()) {
                        if (DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(coordroute_instance.title()+',')) {
                            Map tp_coord = AviationMath.getCoordinate(
                                coordroute_instance.latMath(), coordroute_instance.lonMath(),
                                CONTESTMAP_TPNAME_RUNWAY_DIRECTION,
                                CONTESTMAP_TPNAME_RUNWAY_DISTANCE
                            )
                            xml.wpt(lat:tp_coord.lat, lon:tp_coord.lon) {
                                xml.name coordroute_instance.titleMediaCode(media)
                                xml.sym "empty.png"
                                xml.type ""
                            }
                        }
                    } else if (contestMapParams.contestMapSecretGates && coordroute_instance.type == CoordType.SECRET && !coordroute_instance.HideSecret(curved_point_ids)) {
                        BigDecimal distance_value = coordroute_instance.gatewidth2
                        if (distance_value < CONTESTMAP_TPNAME_DISTANCE) {
                            distance_value = CONTESTMAP_TPNAME_DISTANCE
                        }
                        Map tp_coord = AviationMath.getOrthogonalTitlePoint(
                            last_coordroute_instance2.latMath(), last_coordroute_instance2.lonMath(),
                            coordroute_instance.latMath(), coordroute_instance.lonMath(),
                            center_latitude, center_longitude,
                            distance_value
                        )
                        xml.wpt(lat:tp_coord.lat, lon:tp_coord.lon) {
                            xml.name coordroute_instance.titleMediaCode(media)
                            xml.sym "empty.png"
                            xml.type ""
                        }
                    }
                    last_coordroute_instance2 = coordroute_instance
                }
            }
            
            // Gates unbekannter Zeitkontrollen
            if (contestMapParams.contestMapSecretGates) {
                CoordRoute last_coordroute_instance = null
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (coordroute_instance.type == CoordType.SECRET && !coordroute_instance.HideSecret(curved_point_ids)) {
                        Float gate_width = null
                        if (testInstance) {
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
                        xml.rte {
                            wr_gate(coordroute_instance, gate_width, xml, task_instance, wr_photoimage)
                            // BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                            xml.name coordroute_instance.titleMediaCode(media)
                            xml.rtept(lat:gate.coordLeft.lat, lon:gate.coordLeft.lon) {
                                // xml.ele altitude_meter
                            }
                            xml.rtept(lat:gate.coordRight.lat, lon:gate.coordRight.lon) {
                                // xml.ele altitude_meter
                            }
                        }
                    }
                    last_coordroute_instance = coordroute_instance
                }
            }
            
            // Circle center
			if (contestMapParams.contestMapCircle) {
				for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
					if (coordroute_instance.circleCenter) {
						List circle_coords = AviationMath.getCircle(coordroute_instance.latMath(), coordroute_instance.lonMath(), CONTESTMAP_CIRCLECENTER_RADIUS)
						xml.rte {
							// BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
							xml.name "Circle ${coordroute_instance.titleMediaCode(media)}"
							for (Map circle_coord in circle_coords) {
								xml.rtept(lat:circle_coord.lat, lon:circle_coord.lon) {
									// xml.ele altitude_meter
								}
							}
						}
					}
				}
			}
            
            // CoordEnroutePhoto
            if (contestMapParams.contestMapEnroutePhotos) {
                for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                    if (DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(coordenroutephoto_instance.title()+',')) {
                        Coord next_coord_instance = routeInstance.GetNextEnrouteSignCoord(coordenroutephoto_instance)
                        if (!next_coord_instance || DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(next_coord_instance.title()+',')) {
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
            if (contestMapParams.contestMapEnrouteCanvas) {
                for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                    if (DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(coordenroutecanvas_instance.title()+',')) {
                        Coord next_coord_instance = routeInstance.GetNextEnrouteSignCoord(coordenroutecanvas_instance)
                        if (!next_coord_instance || DisabledCheckPointsTools.Uncompress(contestMapParams.contestMapPrintPoints).contains(next_coord_instance.title()+',')) {
                            xml.wpt(lat:coordenroutecanvas_instance.latMath(), lon:coordenroutecanvas_instance.lonMath()) {
                                xml.name ""
                                xml.sym "${coordenroutecanvas_instance.enrouteCanvasSign.toString().toLowerCase()}.png"
                                xml.type ""
                            }
                        }
                    }
                }
            }
            
        } else {
            // gates
            CoordRoute last_coordroute_instance = null
            CoordRoute start_coordroute_instance = null
            CoordRoute center_coordroute_instance = null
            boolean set_endcurved = false
            boolean first = true
            int semicircle_gate_num = 0
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                if (routeInstance.exportSemicircleGates && params.gpxExport) {
                    if (center_coordroute_instance) { // semicircle
                        boolean write_gate = false
                        boolean show_curved_point = params.gpxExport || routeInstance.showCurvedPoints
                        if (show_curved_point) {
                            write_gate = true
                        }
                        if (write_gate) {
                            List semicircle_coords = AviationMath.getSemicircle(
                                center_coordroute_instance.latMath(), center_coordroute_instance.lonMath(),
                                start_coordroute_instance.latMath(), start_coordroute_instance.lonMath(),
                                coordroute_instance.latMath(), coordroute_instance.lonMath(), center_coordroute_instance.semiCircleInvert
                            )
                            Float gate_width = null
                            if (testInstance) {
                                gate_width = testInstance.GetSecretGateWidth()
                            }
                            if (!gate_width) {
                                gate_width = center_coordroute_instance.gatewidth2
                            }
                            Map last_semicircle_coord = null
                            for (Map semicircle_coord in semicircle_coords) {
                                if (last_semicircle_coord) {
                                    semicircle_gate_num++
                                    Map gate = AviationMath.getGate(
                                        last_semicircle_coord.lat, last_semicircle_coord.lon,
                                        semicircle_coord.lat, semicircle_coord.lon,
                                        gate_width
                                    )
                                    xml.rte {
                                        wr_gate_semicircle(semicircle_coord.lat, semicircle_coord.lon, center_coordroute_instance.altitude, gate_width, semicircle_gate_num, xml, task_instance)
                                        //BigDecimal altitude_meter = center_coordroute_instance.altitude.toLong() / ftPerMeter
                                        xml.name "${getMsg(CoordType.SECRET.code, is_print)}${semicircle_gate_num}"
                                        xml.rtept(lat:gate.coordLeft.lat, lon:gate.coordLeft.lon) {
                                            //xml.ele altitude_meter
                                        }
                                        xml.rtept(lat:gate.coordRight.lat, lon:gate.coordRight.lon) {
                                            //xml.ele altitude_meter
                                        }
                                    }
                                }
                                last_semicircle_coord = semicircle_coord
                            }
                        }
                        start_coordroute_instance = null
                        center_coordroute_instance = null
                        set_endcurved = true
                    } else if (coordroute_instance.circleCenter) {
                        start_coordroute_instance = last_coordroute_instance
                        center_coordroute_instance = coordroute_instance
                    }
                }
                if (!coordroute_instance.circleCenter || (!routeInstance.exportSemicircleGates && params.gpxExport)) {
                    boolean show_wpt = false
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
                                xml.rte {
                                    wr_gate(coordroute_instance, coordroute_instance.gatewidth2, xml, task_instance, wr_photoimage, gate.coord.lat, gate.coord.lon)
                                    xml.name coordroute_instance.titleMediaCode(media)
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
                                    wr_gate(coordroute_instance, coordroute_instance.gatewidth2, xml, task_instance, wr_photoimage)
                                    xml.name coordroute_instance.titleMediaCode(media)
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
                            if (testInstance) {
                                Map gate = AviationMath.getGate(
                                    coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                    testInstance.flighttestwind.LDGDirection, 
                                    testInstance.flighttestwind.LDGOffset,
                                    testInstance.flighttestwind.LDGOrthogonalOffset,
                                    coordroute_instance.gatewidth2
                                )
                                xml.rte {
                                    wr_gate(coordroute_instance, coordroute_instance.gatewidth2, xml, task_instance, wr_photoimage, gate.coord.lat, gate.coord.lon)
                                    xml.name coordroute_instance.titleMediaCode(media)
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
                                    wr_gate(coordroute_instance, coordroute_instance.gatewidth2, xml, task_instance, wr_photoimage)
                                    xml.name coordroute_instance.titleMediaCode(media)
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
                            if (testInstance) {
                                Map gate = AviationMath.getGate(
                                    coordroute_instance.latMath(),coordroute_instance.lonMath(),
                                    testInstance.flighttestwind.iTOiLDGDirection, 
                                    testInstance.flighttestwind.iTOiLDGOffset,
                                    testInstance.flighttestwind.iTOiLDGOrthogonalOffset,
                                    coordroute_instance.gatewidth2
                                )
                                xml.rte {
                                    wr_gate(coordroute_instance, coordroute_instance.gatewidth2, xml, task_instance, wr_photoimage, gate.coord.lat, gate.coord.lon)
                                    xml.name coordroute_instance.titleMediaCode(media)
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
                                    wr_gate(coordroute_instance, coordroute_instance.gatewidth2, xml, task_instance, wr_photoimage)
                                    xml.name coordroute_instance.titleMediaCode(media)
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
                            xml.name coordroute_instance.titleMediaCode(media)
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
                                wr_gate(last_coordroute_instance, last_coordroute_instance.gatewidth2, xml, task_instance, wr_photoimage)
                                //BigDecimal altitude_meter = last_coordroute_instance.altitude.toLong() / ftPerMeter
                                xml.name last_coordroute_instance.titleMediaCode(media)
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
                            boolean write_gate = false
                            boolean show_curved_point = params.gpxExport || routeInstance.showCurvedPoints
                            if (show_curved_point || !coordroute_instance.HideSecret(curved_point_ids)) {
                                write_gate = true
                            }
                            if (write_gate) {
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
                                xml.rte {
                                    wr_gate(coordroute_instance, gate_width, xml, task_instance, wr_photoimage, null, null, set_endcurved)
                                    // BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                                    xml.name coordroute_instance.titleMediaCode(media)
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
                    set_endcurved = false
                }
            }
            
            // procedure turns
            if ((!testInstance || testInstance.task.procedureTurnDuration > 0) && routeInstance.useProcedureTurns) {
                last_coordroute_instance = null
                CoordRoute last_last_coordroute_instance = null
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (coordroute_instance.planProcedureTurn && last_coordroute_instance && last_last_coordroute_instance && last_coordroute_instance.type.IsProcedureTurnCoord()) {
                        List circle_coords = AviationMath.getProcedureTurnCircle(
                            last_last_coordroute_instance.latMath(), last_last_coordroute_instance.lonMath(),
                            last_coordroute_instance.latMath(), last_coordroute_instance.lonMath(),
                            coordroute_instance.latMath(), coordroute_instance.lonMath(),
                            0,
                            CONTESTMAP_PROCEDURETURN_DISTANCE
                        )
                        xml.rte {
                            // BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                            xml.name "Procedure turn ${last_coordroute_instance.titleMediaCode(media)}"
                            for (Map circle_coord in circle_coords) {
                                xml.rtept(lat:circle_coord.lat, lon:circle_coord.lon) {
                                    // xml.ele altitude_meter
                                }
                            }
                        }
                    }
                    last_last_coordroute_instance = last_coordroute_instance 
                    last_coordroute_instance = coordroute_instance
                }
            }
            
        }
        
        // Important points
        if (!contestMapParams) {
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
                } else if (coordroute_instance.circleCenter) {
                    xml.wpt(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                        xml.sym "circle_red"
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
                            if (wr_photoimage && coordenroutephoto_instance.imagecoord) {
                                xml.enroutephotoimage(observationpositiontop: coordenroutephoto_instance.observationPositionTop, observationpositionleft: coordenroutephoto_instance.observationPositionLeft) {
                                    xml.imagedata Base64.getEncoder().encodeToString(coordenroutephoto_instance.imagecoord.imageData)
                                }
                            }
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
    private void wr_gate(CoordRoute coordrouteInstance, Float gateWidth, MarkupBuilder xml, Task taskInstance, boolean wrPhotoImage, BigDecimal latValue = null, BigDecimal lonValue = null, boolean setEndCurved = false)
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
        boolean no_timecheck = coordrouteInstance.noTimeCheck
        if (taskInstance) {
            no_timecheck = DisabledCheckPointsTools.Uncompress(taskInstance.disabledCheckPoints).contains("${coordrouteInstance.title()},")
        }
        boolean no_gatecheck = coordrouteInstance.noGateCheck
        if (taskInstance) {
            no_gatecheck = DisabledCheckPointsTools.Uncompress(taskInstance.disabledCheckPointsNotFound).contains("${coordrouteInstance.title()},")
        }
        boolean end_curved = coordrouteInstance.endCurved
        if (setEndCurved) {
            end_curved = true
        }
        if (coordrouteInstance.type == CoordType.SECRET) {
            end_curved = false
        }
        xml.extensions {
            xml.flightcontest {
                xml.gate(
                    type:           coordrouteInstance.type,
                    number:         coordrouteInstance.titleNumber,
                    lat:            latValue,
                    lon:            lonValue,
                    alt:            coordrouteInstance.altitude,
                    width:          gateWidth,
                    dir:            dir,
                    notimecheck:    getYesNo(no_timecheck),
                    nogatecheck:    getYesNo(no_gatecheck),
                    noplanningtest: getYesNo(coordrouteInstance.noPlanningTest),
                    endcurved:      getYesNo(end_curved),
                    circlecenter:   getYesNo(coordrouteInstance.circleCenter),
                    invert:         getYesNo(coordrouteInstance.semiCircleInvert),
                    dist:           coordrouteInstance.measureDistance,
                    track:          coordrouteInstance.measureTrueTrack,
                    duration:       coordrouteInstance.legDuration,
                    assignedsign:   coordrouteInstance.assignedSign,
                    correctsign:    coordrouteInstance.correctSign
                )
                if (wrPhotoImage && coordrouteInstance.imagecoord) {
                    xml.photoimage(
                        observationpositiontop: coordrouteInstance.observationPositionTop,
                        observationpositionleft: coordrouteInstance.observationPositionLeft,
                        observationnextprintpageturnpoint: getYesNo(coordrouteInstance.observationNextPrintPage),
                        observationnextprintpageenroute: getYesNo(coordrouteInstance.observationNextPrintPageEnroute)
                    ) {
                        xml.imagedata Base64.getEncoder().encodeToString(coordrouteInstance.imagecoord.imageData)
                    }
                }
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private void wr_gate_semicircle(BigDecimal latValue, BigDecimal lonValue, int altValue, Float gateWidth, int gateNumber, MarkupBuilder xml, Task taskInstance)
    {
        BigDecimal altitude_meter = altValue.toLong() / ftPerMeter
        String dir = ""
        xml.extensions {
            xml.flightcontest {
                xml.gate(
                    type:           CoordType.SECRET,
                    number:         gateNumber,
                    lat:            latValue,
                    lon:            lonValue,
                    alt:            altValue,
                    width:          gateWidth,
                    dir:            dir,
                    notimecheck:    getYesNo(true),
                    nogatecheck:    getYesNo(true),
                    noplanningtest: getYesNo(false),
                    endcurved:      getYesNo(false),
                    dist:           null,
                    track:          null,
                    duration:       null,
                    assignedsign:   TurnpointSign.None,
                    correctsign:    TurnpointCorrect.Unassigned
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
    private boolean gpx_track(Test testInstance, MarkupBuilder xml, Map params)
    // Return true: data found
    {
        printstart "gpx_track"
        
        boolean found = false
        List track_points = []
        if (testInstance.IsLoggerData()) {
            println "Get track points from '${testInstance.loggerDataStartUtc}' to '${testInstance.loggerDataEndUtc}'"
            track_points = testInstance.GetTrackPoints(testInstance.loggerDataStartUtc, testInstance.loggerDataEndUtc).trackPoints 
        }
        if (track_points) {
            println "Write track points"
            Media media = Media.Screen
            if (params.isTracking) {
                media = Media.Tracking
            } else if (params.isPrint) {
                media = Media.Print
            }
            Route route_instance = testInstance.task.flighttest.route
            boolean observationsign_used = testInstance.task.flighttest.IsObservationSignUsed()
            boolean show_curved_point = route_instance.showCurvedPoints
            List curved_point_titlecodes = route_instance.GetCurvedPointTitleCodes(media)
            // cache calc results
            List calc_results = []
            if (testInstance.IsLoggerResult()) {
                for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'utc'])) {
                    String title_code = ""
                    CoordType coord_type = CoordType.UNKNOWN
                    int title_number = 1
                    boolean is_runway = false
                    if (calcresult_instance.coordTitle) {
                        title_code = calcresult_instance.coordTitle.titleMediaCode(media)
                        coord_type = calcresult_instance.coordTitle.type
                        title_number = calcresult_instance.coordTitle.number
                        is_runway = calcresult_instance.coordTitle.type.IsRunwayCoord()
                    }
                    String utc = calcresult_instance.utc
                    if (params.gpxDate) {
                        utc = FcTime.UTCReplaceDate(calcresult_instance.utc, params.gpxDate)
                    }
                    Map new_calc_result = [utc: utc, 
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
                        
                        String utc = p.utc
                        if (params.gpxDate) {
                            utc = FcTime.UTCReplaceDate(p.utc, params.gpxDate)
                            if (params.gpxEndTime) {
                                if (utc > params.gpxEndTime) {
                                    println "Track points cut off: ${utc}"
                                    break
                                }
                            }
                        }
                        
                        // <trkpt>
                        xml.trkpt(lat:p.latitude, lon:p.longitude) {
                            xml.ele p.altitude / GpxService.ftPerMeter
                            xml.time utc
                            
                            // add <extensions> for tp and flight errors
                            for (Map calc_result in calc_results) {
                                if (calc_result.utc == utc) {
                                    if (observationsign_used && !calc_result.badCourse && !calc_result.badTurn && !calc_result.gateMissed && !calc_result.gateNotFound && calc_result.coordType.IsEnrouteSignCoord()) { // cache enroute photo / canvas data
                                        enroute_points = RoutePointsTools.GetEnrouteSignShowPoints(route_instance,calc_result.coordType,calc_result.titleNumber, false)
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
        Map ret = [error:false, message:'']
        try {
            String dest_file_name = "/${baseDir}/${destFileName}"
            println "${configFlightContest.ftp.host}:${configFlightContest.ftp.port}${dest_file_name}"
            new FTPClient().with {
                connect(configFlightContest.ftp.host, configFlightContest.ftp.port)
                enterLocalPassiveMode()
                if (login(configFlightContest.ftp.username, configFlightContest.ftp.password)) {
                    if (changeWorkingDirectory(baseDir) || makeDirectory(baseDir)) {
                        if (destFileName && sourceURL) {
                            setFileType(BINARY_FILE_TYPE)
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
        printstart "SendFTP2 ${configFlightContest.ftp.host}:${configFlightContest.ftp.port}:${sourceURL}"
        Map ret = [error:false, message:'']
        try {
            String dest_file_name = "/${baseDir}/${destFileName}"
            println "${configFlightContest.ftp.host}:${configFlightContest.ftp.port}${dest_file_name}"
            new FTPClient().with {
                connect(configFlightContest.ftp.host, configFlightContest.ftp.port)
                enterLocalPassiveMode()
                if (login(configFlightContest.ftp.username, configFlightContest.ftp.password)) {
                    if (changeWorkingDirectory(baseDir) || makeDirectory(baseDir)) {
                        if (destFileName && sourceURL) {
                            setFileType(BINARY_FILE_TYPE)
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
            ret.message = """getMsg('fc.net.ftp.connecterror',["${configFlightContest.ftp.host}:${configFlightContest.ftp.port}:${sourceURL}"]) + ": ${e.getMessage()}" """
            printerror ret.message
        }
        return ret
    }
    
    // ----------------------------------------------------------------------------------
    void BuildVRT(String tifFileName, String vrtFileName)
    {
        println "BuildVRT $tifFileName -> $vrtFileName"
        gdal.AllRegister()
        Vector source_filenames = new Vector()
        source_filenames.add(tifFileName)
        Vector built_vrt_options = new Vector()
        Dataset vrt_data = gdal.BuildVRT(vrtFileName, source_filenames, new BuildVRTOptions(built_vrt_options))
        if (vrt_data) {
            vrt_data.delete()
        }
    }

    // ----------------------------------------------------------------------------------
    void Gdal2Tiles(String workingDir, String inputFileName, String tilesDirName)
    {
        inputFileName = inputFileName.replace('\\','/')
        println "Gdal2Tiles '${inputFileName}' -> '${tilesDirName}'"
        
        String output_profile = "--profile=mercator"
        //String output_profile = "--profile=geodetic"
        //String output_profile = "--profile=raster"
        String input_srs = "--s_srs=EPSG:4326"
        //String input_srs = "--s_srs=EPSG:3857"
        
        List<String> command = ["C:/Program Files/Python37/python.exe", "C:/Program Files/GDAL/gdal2tiles.py", input_srs, output_profile, "--zoom=5-13", "--tilesize=256", "--webviewer=leaflet", "--exclude", inputFileName, tilesDirName]
        ProcessBuilder process_builder = new ProcessBuilder(command)
        Map<String, String> process_env = process_builder.environment()
        process_env.put("PROJ_LIB", "C:\\Program Files\\GDAL\\projlib")
        process_builder = process_builder.directory(new File(workingDir))
        process_builder.redirectErrorStream(true)
        Process process = process_builder.start()
        
        InputStream inputstream_instance = process.getInputStream()
        BufferedReader input_reader = inputstream_instance.newReader("UTF-8")
        while (true) {
            String line = input_reader.readLine()
            if (line == null) {
                break
            }
            println line
        }
        input_reader.close()
        inputstream_instance.close()
    }
    
    //--------------------------------------------------------------------------
    Map UploadTiles(String tilesDirName)
    {
        Map ret = [error:false, message:'', errornum:0]
        printstart "UploadTiles $tilesDirName"
        FTPClient ftp_client = new FTPClient()
        ftp_client.with {
            connect(grailsApplication.config.flightcontest.ftptiles.host, grailsApplication.config.flightcontest.ftptiles.port)
            enterLocalPassiveMode()
            if (login(grailsApplication.config.flightcontest.ftptiles.username, grailsApplication.config.flightcontest.ftptiles.password)) {
                String base_dir = grailsApplication.config.flightcontest.ftptiles.basedir
                if (changeWorkingDirectory(base_dir) || makeDirectory(base_dir)) {
                    println "upload $tilesDirName -> $base_dir"
                    Map ret1 = upload_dir(ftp_client, tilesDirName, tilesDirName)
                    ret.message = getMsg('fc.contestmap.exportmap.tiles.done', [ret1.done_num, ret1.error_num], false)
                    if (ret1.error_num) {
                        ret.error = true
                    }
                } else {
                    ret.message = getMsg('fc.net.ftp.dircreateerror', [base_dir], false)
                    ret.error = true
                }
            } else {
                ret.message = getMsg('fc.net.ftp.loginerror', [grailsApplication.config.flightcontest.ftptiles.username], false)
                ret.error = true
            }
            disconnect()
        }
        if (ret.error) {
            printerror ret.message
        } else {
            printdone ret.message
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map upload_dir(FTPClient ftpClient, String startSourceDirName, String sourceDirName)
    {
        Map ret = [error_num:0, done_num:0]
        
        File source_dir = new File(sourceDirName)
        
        // create sub directories or upload maps
        source_dir.eachFile() { File source_file ->
            String dest_file_name = source_file.canonicalPath.substring(source_file.canonicalPath.lastIndexOf('\\')+1)
            if (source_file.isFile()) {
                printstart "Upload $source_file.canonicalPath"
                try {
                    def stream = new FileInputStream(source_file)
                    ftpClient.deleteFile(dest_file_name)
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
                    boolean done = ftpClient.storeFile(dest_file_name, stream)
                    stream.close()
                    if (done) {
                        ret.done_num++
                        printdone ""
                    } else {
                        ret.error_num++
                        printerror ""
                    }
                } catch (Exception e) {
                    ret.error_num++
                    printerror e.getMessage()
                }
            } else {
                printstart "Create $dest_file_name"
                try {
                    ftpClient.makeDirectory(dest_file_name)
                    printdone ""
                } catch (Exception e) {
                    ret.error_num++
                    printerror e.getMessage()
                }
            }
        }
        
        // process sub directories
        source_dir.eachFile() { File source_file ->
            String dest_file_name = source_file.canonicalPath.substring(startSourceDirName.size()).replace('\\','/')
            if (!source_file.isFile()) {
                if (ftpClient.changeWorkingDirectory(dest_file_name)) {
                    Map ret1 = upload_dir(ftpClient, startSourceDirName, source_file.canonicalPath)
                    ret.error_num += ret1.error_num
                    ret.done_num += ret1.done_num
                }
            }
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
                
                String live_file_name ="${webroot_dir}${Defs.ROOT_FOLDER_LIVE}/${Defs.LIVE_FILENAME}"
                printstart "Copy to '$live_file_name'"
                def live_src_file = new File(live_html_file_name).newInputStream()
                try {
                    def dest_file = new File(live_file_name).newOutputStream()  
                    dest_file << live_src_file
                    dest_file.close()
                } catch (Exception e) {
                    ret.error = true
                    ret.failedDestinations += live_file_name
                    println "Error: ${e.getMessage()}"
                }
                live_src_file.close()
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
            
            String live_file_name ="${webroot_dir}${Defs.ROOT_FOLDER_LIVE}/${Defs.LIVE_FILENAME}"
            live_file_name = "${live_file_name.substring(0,live_file_name.lastIndexOf('/')+1)}${stylesheetName}"
            printstart "Copy to '$live_file_name'"
            def live_src_file = new File(stylesheet_file_name).newInputStream()
            try {
                def dest_file = new File(live_file_name).newOutputStream()  
                dest_file << live_src_file
                dest_file.close()
            } catch (Exception e) {
                ret.error = true
                ret.failedDestinations += live_file_name
                println "Error: ${e.getMessage()}"
            }
            live_src_file.close()
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
        File lock_file = new File(webroot_dir + Defs.ROOT_FOLDER_JOBS_LOCK)
        if (!lock_file.exists()) {
            List set_links = []
            File analyze_dir1 = new File(webroot_dir + Defs.ROOT_FOLDER_JOBS)
            analyze_dir1.eachFile() { File file ->
                if (file.isFile()) {
                    if (!found) {
                        printstart "BackgroundUpload ${new Date()}"
                        found = true
                    }
                    
                    printstart "Process '${file}'"
                    
                    BufferedReader job_file_reader = file.newReader("UTF-8")
                    String line = ""
                    int line_nr = 0
                    
                    String email_to // 1
                    String email_subject // 2
                    String email_body // 3
                    String ftp_basedir // 4
                    String ftp_uploads // 5
                    String remove_files // 6
                    String save_links // 7
                    
                    while (true) {
                        line = job_file_reader.readLine()
                        if (line == null) {
                            break
                        }
                        line_nr++
                        switch (line_nr) {
                            case 1: 
                                email_to = line
                                break
                            case 2: 
                                email_subject = line
                                break
                            case 3:
                                email_body = line
                                break
                            case 4:
                                ftp_basedir = line
                                break
                            case 5:
                                ftp_uploads = line
                                break
                            case 6:
                                remove_files = line
                                break
                            case 7:
                                save_links = line
                                break 
                        }
                    }
                    job_file_reader.close()
                    
                    // Save links
                    if (save_links) {
                        for (String save_link in save_links.split(Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR)) {
                            Map set_link = [save_link:save_link, error:false]
                            set_links += set_link
                        }
                    }
                        
					printstart "Process links"
					for (Map set_link in set_links) {
						String[] l = set_link.save_link.split(Defs.BACKGROUNDUPLOAD_IDLINK_SEPARATOR)
						if (l[0] == "UploadJobTest") {
							try {
								UploadJobTest uploadjob_test = UploadJobTest.get(l[1])
								println "Test ${uploadjob_test.test.name()}: Upload sending"
								uploadjob_test.uploadJobStatus = UploadJobStatus.Sending
								uploadjob_test.uploadJobLink = ""
								uploadjob_test.save(flush:true)
							} catch (Exception e) {
								println "UploadJobTest ${l[1]} locked (1): ${e.getMessage()}"
							}
						} else if (l[0] == "UploadJobRoute") {
							try {
								UploadJobRoute uploadjob_route = UploadJobRoute.get(l[1])
								println "Route ${uploadjob_route.route.name()}: Upload sending"
								uploadjob_route.uploadJobStatus = UploadJobStatus.Sending
								uploadjob_route.uploadJobLink = ""
								uploadjob_route.save(flush:true)
							} catch (Exception e) {
								println "UploadJobRoute ${l[1]} locked (1): ${e.getMessage()}"
							}
						} else if (l[0] == "UploadJobRouteMap") {
							try {
								UploadJobRouteMap uploadjob_routemap = UploadJobRouteMap.get(l[1])
								println "Route ${uploadjob_routemap.route.name()}: Upload sending"
								uploadjob_routemap.uploadJobStatus = UploadJobStatus.Sending
								uploadjob_routemap.uploadJobLink = ""
								uploadjob_routemap.save(flush:true)
							} catch (Exception e) {
								println "UploadJobRouteMap ${l[1]} locked (1): ${e.getMessage()}"
							}
						}
					}
					printdone ""
					
                    boolean error = false
                    
                    // FTP uploads
                    if (ftp_basedir && ftp_uploads) {
                        for (String ftp_upload in ftp_uploads.split(Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR)) {
                            String[] f = ftp_upload.split(Defs.BACKGROUNDUPLOAD_SRCDEST_SEPARATOR)
                            Map ret = SendFTP2(
                                grailsApplication.config.flightcontest, ftp_basedir, f[0], f[1]
                            )
                            if (ret.error) {
                                error = true
                                break
                            }
                        }
                    }
                    
                    // Send email
                    if (email_to && email_subject && email_body && !error) {
                        try {
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
                            }
                            println "E-Mail to '$email_to' send."
                        } catch (Exception e) {
                            println "Error: ${e.getMessage()}" 
                            error = true
                        }
                    }
                        
                    // Remove files
                    if (remove_files) {
                        for (String remove_file in remove_files.split(Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR)) {
                            DeleteFile(remove_file)
                        }
                    }
                    
                    // Modify links
                    if (set_links && error) {
                        for (Map set_link in set_links) {
                            set_link.error = true
                        }
                    }
                        
                    if (!error) {
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
            
            if (set_links) {
                printstart "Process links"
                for (Map set_link in set_links) {
                    String[] l = set_link.save_link.split(Defs.BACKGROUNDUPLOAD_IDLINK_SEPARATOR)
                    if (l[0] == "UploadJobTest") {
                        try {
                            UploadJobTest uploadjob_test = UploadJobTest.get(l[1])
							if (set_link.error) {
								println "Test ${uploadjob_test.test.name()}: Set E-Mail-Error"
								uploadjob_test.uploadJobStatus = UploadJobStatus.Error // test_instance.flightTestLink = Defs.EMAIL_ERROR
								uploadjob_test.uploadJobLink = ""
								
							} else {
								println "Test ${uploadjob_test.test.name()}: Save link '${l[2]}'"
								uploadjob_test.uploadJobStatus = UploadJobStatus.Done // test_instance.flightTestLink = l[2]
								uploadjob_test.uploadJobLink = l[2]
							}
							uploadjob_test.save(flush:true)
                        } catch (Exception e) {
                            println "UploadJobTest ${l[1]} locked (2): ${e.getMessage()}"
                        }
                    } else if (l[0] == "UploadJobRoute") {
                        try {
                            UploadJobRoute uploadjob_route = UploadJobRoute.get(l[1])
							if (set_link.error) {
								println "Route ${uploadjob_route.route.name()}: Set E-Mail-Error"
								uploadjob_route.uploadJobStatus = UploadJobStatus.Error
								uploadjob_route.uploadJobLink = ""
								
							} else {
								println "Route ${uploadjob_route.route.name()}: Save link '${l[2]}'"
								uploadjob_route.uploadJobStatus = UploadJobStatus.Done
								uploadjob_route.uploadJobLink = l[2]
							}
							uploadjob_route.save(flush:true)
                        } catch (Exception e) {
                            println "UploadJobRoute ${l[1]} locked (2): ${e.getMessage()}"
                        }
                    } else if (l[0] == "UploadJobRouteMap") {
                        try {
                            UploadJobRouteMap uploadjob_routemap = UploadJobRouteMap.get(l[1])
							if (set_link.error) {
								println "Route ${uploadjob_routemap.route.name()}: Set E-Mail-Error"
								uploadjob_routemap.uploadJobStatus = UploadJobStatus.Error
								uploadjob_routemap.uploadJobLink = ""
								
							} else {
								println "Route ${uploadjob_routemap.route.name()}: Save link '${l[2]}'"
								uploadjob_routemap.uploadJobStatus = UploadJobStatus.Done
								uploadjob_routemap.uploadJobLink = l[2]
							}
							uploadjob_routemap.save(flush:true)
                        } catch (Exception e) {
                            println "UploadJobRouteMap ${l[1]} locked (2): ${e.getMessage()}"
                        }
                    }
                }
                printdone ""
            }
        }
        if (found) {
            printdone ""
        }
    }
    
    //--------------------------------------------------------------------------
    void DeleteFile(String fileName)
    {
        printstart "DeleteFile '$fileName'"
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
    void DeleteDir(String fileName)
    {
        printstart "DeleteDir '$fileName'"
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
            if (file.deleteDir()) {
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
