import java.util.Map

import java.math.*
import java.text.*
import java.util.List

import org.springframework.web.context.request.RequestContextHolder

import groovy.xml.*

import org.apache.commons.net.ftp.FTPClient

class GpxService 
{
	def logService
    def messageSource
    
	final static BigDecimal ftPerMeter = 3.2808 // 1 meter = 3,2808 feet
	
	final static String GPXGACCONVERTER_VERSION = "1.0"
	
	final static String GACFORMAT_DEF = "I033639GSP4042TRT4346FXA"
    
    final static Float GPXSHOWPPOINT_GATEDISTANCE = 0.5f         // Größe der Anzeigeumgebung um Gate in NM
    final static Float GPXSHOWPPOINT_SECRETGATEDISTANCE = 1.0f   // Größe der Anzeigeumgebung um Secret Gate in NM
    final static Float GPXSHOWPPOINT_AIRFIELDDISTANCE = 2.0f     // Größe der Anzeigeumgebung um Flugplatz in NM
    final static int GPXSHOWPPOINT_SCALE = 4                     // Nachkommastellen für Koordinaten
	
	final static boolean WRLOG = false
    
    final static String XMLHEADER = "<?xml version='1.0' encoding='UTF-8'?>"
    
    final static String GPXVERSION = "1.1"
    final static String GPXCREATOR = "Flight Contest - flightcontest.de"
    final static String GPXGACTRACKNAME = "GAC track"
	
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
					if (line == GACFORMAT_DEF) {
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
							boolean latitude_north = line.substring(14,15) == 'N'
							BigDecimal dest_latitude = latitude_grad_math + (latidude_minute_math / 60)
							if (!latitude_north) {
								dest_latitude *= -1
							}
							
							// Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
							String longitude_grad = line.substring(15,18)
							BigDecimal longitude_grad_math = longitude_grad.toBigDecimal()
							String longitude_minute = line.substring(18,20) + '.' + line.substring(20,23)
							BigDecimal longitude_minute_math = longitude_minute.toBigDecimal()
							boolean longitude_east = line.substring(23,24) == 'E'
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
	boolean ConvertGPX2GAC(String gpxFileName, String gacFileName)
	{
		boolean converted = true
		String err_msg = ""
		
		printstart "ConvertGPX2GAC $gpxFileName -> $gacFileName"
		
		File gpx_file = new File(gpxFileName)
		File gac_file = new File(gacFileName)

		FileReader gpx_reader = new FileReader(gpx_file)
		BufferedWriter gac_writer = gac_file.newWriter()
		try {
			def gpx = new XmlParser().parse(gpx_reader)
			if (gpx.trk.size() == 1) { // only 1 track allowed
				def gpx_track_name = gpx.trk.name[0].text()
				def track_points = gpx.trk..trkpt
				
				// write header
				WriteLine(gac_writer,"AFCGPX:$GPXGACCONVERTER_VERSION")
				WriteLine(gac_writer,"HINFO1:Flight Contest GPX GAC Converter $GPXGACCONVERTER_VERSION")
				WriteLine(gac_writer,"HINFO2:Thomas Weise, Deutscher Praezisionsflug-Verein e.V.")
				WriteLine(gac_writer,"HGPXTRACKNAME:$gpx_track_name")
				WriteLine(gac_writer,GACFORMAT_DEF)
				
				// write track points
				boolean first = true
				BigDecimal last_latitude_math
				BigDecimal last_longitude_math
				track_points.each {
					String utc =  GACTimeStr(it.time[0].text())
					BigDecimal latitude_math = it.'@lat'.toBigDecimal()
					String latitude = GACLatitudeStr(latitude_math)
					BigDecimal longitude_math = it.'@lon'.toBigDecimal()
					String longitude = GACLongitudeStr(longitude_math)
					String valid = "A" // valid data, V = invalid data
					String pressure = "99999" // no barographic sensor
					BigDecimal altitude_meter = it.ele[0].text().toBigDecimal()
					String altitude = GACAltitudeStr(altitude_meter)
					BigDecimal groundspeed_math = 0
					String truetrack = "000"
					if (!first) {
						Map leg = AviationMath.calculateLeg(latitude_math,longitude_math,last_latitude_math,last_longitude_math)
						truetrack = FcMath.GradStr(FcMath.RoundGrad(leg.dir))
						if (WRLOG) {
							println "$last_latitude_math, $last_longitude_math -> $latitude_math, $longitude_math, $truetrack"
						}
						groundspeed_math = leg.dis * 3600
						if (groundspeed_math > 999.9) {
							groundspeed_math = 999.9
						}
					}
					String groundspeed = GACGroundSpeedStr(groundspeed_math)
					String accuracy = "9999" // not available
					WriteLine(gac_writer,"B${utc}${latitude}${longitude}${valid}${pressure}${altitude}${groundspeed}${truetrack}${accuracy}")
					if (WRLOG) {
						println "  ${latitude}, ${longitude}"
					}
					first = false
					last_latitude_math = latitude_math
					last_longitude_math = longitude_math
				}
			} else {
				converted = false
			}
		} catch (Exception e) {
			converted = false
			err_msg = e.getMessage()
		}
		gac_writer.close()
		gpx_reader.close()
		
		if (converted) {
			printdone ""
		} else {
			printerror err_msg
		}
		return converted
	}
	
    //--------------------------------------------------------------------------
    boolean ConvertGAC2GPX(String gacFileName, String gpxFileName)
    {
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
                        while (true) {
                            line = gac_reader.readLine()
                            if (line == null) {
                                break
                            }
                            if (line) {
                                if (line == GACFORMAT_DEF) {
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
                                        String utc = "2015-01-01T${utc_h}:${utc_min}:${utc_s}Z" // TODO: ConvertGAC2GPX UTC Date
                                        
                                        // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                                        String latitude_grad = line.substring(7,9)
                                        BigDecimal latitude_grad_math = latitude_grad.toBigDecimal()
                                        String latidude_minute = line.substring(9,11) + '.' + line.substring(11,14)
                                        BigDecimal latidude_minute_math = latidude_minute.toBigDecimal()
                                        boolean latitude_north = line.substring(14,15) == 'N'
                                        latitude = latitude_grad_math + (latidude_minute_math / 60)
                                        if (!latitude_north) {
                                            latitude *= -1
                                        }
                                        
                                        // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                                        String longitude_grad = line.substring(15,18)
                                        BigDecimal longitude_grad_math = longitude_grad.toBigDecimal()
                                        String longitude_minute = line.substring(18,20) + '.' + line.substring(20,23)
                                        BigDecimal longitude_minute_math = longitude_minute.toBigDecimal()
                                        boolean longitude_east = line.substring(23,24) == 'E'
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
        }
        gac_reader.close()
        gpx_writer.close()

        if (converted) {
            printdone ""
        } else {
            printerror err_msg
        }
        return converted
    }
    
    //--------------------------------------------------------------------------
    Map ConvertRoute2GPX(Route routeInstance, String gpxFileName)
    {
        Map ret = [:]
        boolean converted = true
        String err_msg = ""
        
        printstart "ConvertRoute2GPX ${routeInstance.name()} -> ${gpxFileName}"
        
        File gpx_file = new File(gpxFileName)
        BufferedWriter gpx_writer = gpx_file.newWriter()
        MarkupBuilder xml = new MarkupBuilder(gpx_writer)
        gpx_writer.writeLine(XMLHEADER)
        xml.gpx(version:GPXVERSION, creator:GPXCREATOR) {
            GPXRoute(routeInstance, xml)
        }
        gpx_writer.close()

        if (converted) {
            printdone ""
        } else {
            printerror err_msg
        }
        ret += [gpxShowPoints:GPXShowPoints(routeInstance,null)]
        ret += [ok:converted]
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map ConvertTest2GPX(Test testInstance, String gpxFileName)
    {
        Map ret = [:]
        boolean converted = true
        boolean found_track = false
        String err_msg = ""
        
        Route route_instance = testInstance.task.flighttest.route
        
        printstart "ConvertTest2GPX '${testInstance.aflosStartNum}:${testInstance.crew.name}' -> ${gpxFileName}"
        
        File gpx_file = new File(gpxFileName)
        BufferedWriter gpx_writer = gpx_file.newWriter()
        MarkupBuilder xml = new MarkupBuilder(gpx_writer)
        gpx_writer.writeLine(XMLHEADER)
        xml.gpx(version:GPXVERSION, creator:GPXCREATOR) {
            GPXRoute(route_instance, xml)
            found_track = GPXTrack(testInstance, testInstance.aflosStartNum, xml)
        }
        gpx_writer.close()

        if (converted) {
            printdone ""
        } else {
            printerror err_msg
        }
        ret += [gpxShowPoints:GPXShowPoints(route_instance,testInstance)]
        ret += [ok:converted,track:found_track]
        return ret
    }
    
    //--------------------------------------------------------------------------
    private List GPXShowPoints(Route routeInstance, Test testInstance)
    {
        List points = []
        
            
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
            //if (coordroute_instance.type.IsCpCheckCoord()) {
                Map new_point = [name:coordroute_instance.titleCode()]
                new_point += ErrorPoints(testInstance,coordroute_instance)
                if (coordroute_instance.type == CoordType.SECRET) {
                    new_point += AviationMath.getShowPoint(coordroute_instance.latMath(), coordroute_instance.lonMath(), GPXSHOWPPOINT_SECRETGATEDISTANCE)
                } else if (coordroute_instance.type.IsCpCheckCoord()) {
                    new_point += AviationMath.getShowPoint(coordroute_instance.latMath(), coordroute_instance.lonMath(), GPXSHOWPPOINT_GATEDISTANCE)
                } else {
                    new_point += AviationMath.getShowPoint(coordroute_instance.latMath(), coordroute_instance.lonMath(), GPXSHOWPPOINT_AIRFIELDDISTANCE)
                }
                new_point.latmin.setScale(GPXSHOWPPOINT_SCALE, RoundingMode.HALF_EVEN)
                new_point.latmax.setScale(GPXSHOWPPOINT_SCALE, RoundingMode.HALF_EVEN)
                new_point.lonmin.setScale(GPXSHOWPPOINT_SCALE, RoundingMode.HALF_EVEN)
                new_point.lonmax.setScale(GPXSHOWPPOINT_SCALE, RoundingMode.HALF_EVEN)
                points += new_point
            //}
        }
        return points
    }
    
    //--------------------------------------------------------------------------
    private Map ErrorPoints(Test testInstance, CoordRoute coordroute_instance)
    {
        Map ret = [:]
        CoordResult last_coordresult_instance = null
        for (CoordResult coordresult_instance in CoordResult.findAllByTest(testInstance,[sort:"id"])) {
            if (last_coordresult_instance) {
                if (coordroute_instance.title() == last_coordresult_instance.title()) {
                    boolean error = false
                    boolean points = false
                    
                    // gate missed
                    if (last_coordresult_instance.resultEntered && last_coordresult_instance.type.IsCpTimeCheckCoord()) {
                        if (last_coordresult_instance.resultCpNotFound) {
                            if ((last_coordresult_instance.type == CoordType.SECRET) && (!testInstance.IsFlightTestCheckSecretPoints())) {
                                ret += [hide:true]
                            } else {
                                error = true
                                ret += [cpnotfound:true]
                                if (!testInstance.task.disabledCheckPointsNotFound.contains(last_coordresult_instance.title()+',')) {
                                    points = true
                                }
                            }
                        }
                    }
                    
                    // procedure turn not flown
                    if ((testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (testInstance.task.procedureTurnDuration > 0)) {
                        if (coordresult_instance.resultProcedureTurnEntered && coordresult_instance.planProcedureTurn) {
                            if (coordresult_instance.resultProcedureTurnNotFlown) {
                                error = true
                                ret += [procedureturnnotflown:true]
                                if (!testInstance.task.disabledCheckPointsProcedureTurn.contains(last_coordresult_instance.title()+',')) {
                                    points = true
                                }
                            }
                        }
                    }
                    
                    // bad course
                    if (testInstance.GetFlightTestBadCoursePoints() > 0) {
                        if (last_coordresult_instance.resultEntered && last_coordresult_instance.type.IsBadCourseCheckCoord()) {
                            if (last_coordresult_instance.resultBadCourseNum > 0) {
                                error = true
                                ret += [badcoursenum:last_coordresult_instance.resultBadCourseNum]
                                if (!testInstance.task.disabledCheckPointsBadCourse.contains(last_coordresult_instance.title()+',')) {
                                    points = true
                                }
                            }
                        }
                    }
                    
                    if (points) {
                        ret += [points:true]
                    }
                    if (error) {
                        ret += [error:true]
                    }
                    break
                }
            }
            last_coordresult_instance = coordresult_instance
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private void GPXRoute(Route routeInstance, MarkupBuilder xml)
    {
        // tracks
        long restart_id = 0
        xml.rte {
            xml.name routeInstance.name().encodeAsHTML()
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                if (coordroute_instance.type.IsCpCheckCoord()) {
                    BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                    xml.rtept(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                        xml.name coordroute_instance.titleShortMap()
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
                xml.name routeInstance.name().encodeAsHTML()
                boolean run = false
                for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                    if (run) {
                        if (coordroute_instance.type.IsCpCheckCoord()) {
                            BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                            xml.rtept(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                                xml.name coordroute_instance.titleShortMap()
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
        
        // gates
        CoordRoute last_coordroute_instance = null
        boolean first = true
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
            switch (coordroute_instance.type) {
                case CoordType.TO:
                case CoordType.iTO:
                case CoordType.LDG:
                case CoordType.iLDG:
                    xml.wpt(lat:coordroute_instance.latMath(), lon:coordroute_instance.lonMath()) {
                        xml.name coordroute_instance.titleShortMap()
                    }
                    break
            }
            if (last_coordroute_instance && last_coordroute_instance.type.IsCpCheckCoord() && coordroute_instance.type.IsCpCheckCoord()) {
                if (first) {
                    Map start_gate = AviationMath.getGate(
                        coordroute_instance.latMath(),coordroute_instance.lonMath(),
                        last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                        last_coordroute_instance.gatewidth2
                    )
                    xml.rte {
                        //BigDecimal altitude_meter = last_coordroute_instance.altitude.toLong() / ftPerMeter
                        xml.name last_coordroute_instance.titleShortMap()
                        xml.rtept(lat:start_gate.coordLeft.lat, lon:start_gate.coordLeft.lon) {
                            // xml.ele altitude_meter
                        }
                        xml.rtept(lat:start_gate.coordRight.lat, lon:start_gate.coordRight.lon) {
                            // xml.ele altitude_meter
                        }
                    }
                }
                Map gate = AviationMath.getGate(
                    last_coordroute_instance.latMath(),last_coordroute_instance.lonMath(),
                    coordroute_instance.latMath(),coordroute_instance.lonMath(),
                    coordroute_instance.gatewidth2
                )
                xml.rte {
                    // BigDecimal altitude_meter = coordroute_instance.altitude.toLong() / ftPerMeter
                    xml.name coordroute_instance.titleShortMap()
                    xml.rtept(lat:gate.coordLeft.lat, lon:gate.coordLeft.lon) {
                        // xml.ele altitude_meter
                    }
                    xml.rtept(lat:gate.coordRight.lat, lon:gate.coordRight.lon) {
                        // xml.ele altitude_meter
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
    
    //--------------------------------------------------------------------------
    private boolean GPXTrack(Test testInstance, int startNum, MarkupBuilder xml)
    // Return true: data found
    {
        boolean found = false
        List aflos_crew_names = GetAflosCrewNames(testInstance.task.contest,startNum)
        if (aflos_crew_names) {
            xml.trk {
                xml.name testInstance.crew.name.encodeAsHTML()
                xml.trkseg {
                    for(AflosCrewNames afloscrewnames_instance in aflos_crew_names) {
                        for(int measure_point = 0; measure_point < afloscrewnames_instance.measurePointsNum; measure_point++) {
                            int[] measure_point_data = new int[16]
                            for (int j = 0; j < 16; j++) {
                                measure_point_data[j] =  afloscrewnames_instance.daten[64*measure_point+4*j+3] << 24 |
                                                        (afloscrewnames_instance.daten[64*measure_point+4*j+2] & 0xFF) << 16 |
                                                        (afloscrewnames_instance.daten[64*measure_point+4*j+1] & 0xFF) << 8 |
                                                        (afloscrewnames_instance.daten[64*measure_point+4*j+0] & 0xFF)
                            }
                            
                            // UTC
                            String utc_h = (measure_point_data[12] & 31).toString()
                            if (utc_h.size() == 1) {
                                utc_h = "0" + utc_h
                            }
                            String utc_min = (measure_point_data[14] & 63).toString()
                            if (utc_min.size() == 1) {
                                utc_min = "0" + utc_min
                            }
                            String utc_s = (measure_point_data[15] & 63).toString()
                            if (utc_s.size() == 1) {
                                utc_s = "0" + utc_s
                            }
                            String utc = "2015-01-01T${utc_h}:${utc_min}:${utc_s}Z" // TODO: GPXTrack UTC Date
                            
                            // Latitude
                            def latitude = measure_point_data[0] & 127
                            def b1 = measure_point_data[2] & 63
                            latitude = latitude + (b1 / 60)
                            b1 = measure_point_data[4] & 127
                            latitude = latitude + (b1 / 6000)
                            b1 = measure_point_data[6] & 127
                            latitude = latitude + (b1 / 600000)
                            if ((measure_point_data[0] & 128) == 128) {
                                latitude *= -1
                            }
                            
                            // Longitude
                            def longitude = measure_point_data[7]
                            b1 = measure_point_data[9] & 63
                            longitude = longitude + (b1 / 60)
                            b1 = measure_point_data[11] & 127
                            longitude = longitude + (b1 / 6000)
                            b1 = measure_point_data[13] & 127
                            longitude = longitude + (b1 / 600000)
                            if ((measure_point_data[13] & 128) == 128) {
                                longitude *= -1
                            }
                            
                            // Speed in kn
                            def Speed = (measure_point_data[9] & 192) * 4 + measure_point_data[10]
                            Speed += (measure_point_data[8] & 15) / 10
                            
                            // Altitude in ft
                            int altitude_foot = measure_point_data[1]
                            altitude_foot *= 256
                            altitude_foot += measure_point_data[3]
                            BigDecimal altitude_meter = altitude_foot / ftPerMeter
                            
                            //println "$utc $longitude $latitude $Speed $altitude_foot"
                            xml.trkpt(lat:latitude,lon:longitude) {
                                xml.ele altitude_meter
                                xml.time utc
                            }
                            
                            found = true
                        }
                    }
                }
            }
        }
        return found
    }
    
    //--------------------------------------------------------------------------
    private List GetAflosCrewNames(Contest contestInstance, int startNum)
    {
        if (startNum <= 0) {
            return []
        } else if (contestInstance.aflosTest) {
            return AflosCrewNames.aflostest.findAllByStartnum(startNum,[sort:'id'])
        } else if (contestInstance.aflosUpload) {
            return AflosCrewNames.aflosupload.findAllByStartnum(startNum,[sort:'id'])
        }
        return AflosCrewNames.aflos.findAllByStartnum(startNum,[sort:'id'])
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
	String GACTimeStr(String gpxTime)
	// gpxTime - yyyy-mm-ddThh:mm:ssZ
	// Return hhmmss (6)
	{
        Date gpx_time = Date.parse("HH:mm:ss",gpxTime.substring(11, 19))
		return gpx_time.format("HHmmss")
	}
	
	//--------------------------------------------------------------------------
	String GACLatitudeStr(BigDecimal latitudeMath)
	// Return GGMMXXXN (8)
	{
		boolean north = true
		if (latitudeMath < 0) {
			latitudeMath *= -1
			north = false
		}
		
		int grad_value = latitudeMath.toInteger()
		DecimalFormat df_grad = new DecimalFormat("00")
		String grad = df_grad.format(grad_value)
		
		BigDecimal minute_value = 60*(latitudeMath - grad_value)
		DecimalFormat df_minute = new DecimalFormat("00.000")
		String minute = df_minute.format(minute_value).replace(',', '')
		
		if (north) {
			return "${grad}${minute}N"
		} else {
			return "${grad}${minute}S"
		}
	}
	
	//--------------------------------------------------------------------------
	String GACLongitudeStr(BigDecimal longitudeMath)
	// Return GGGMMXXXE (9)
	{
		boolean east = true
		if (longitudeMath < 0) {
			longitudeMath *= -1
			east = false
		}
		
		int grad_value = longitudeMath.toInteger()
		DecimalFormat df_grad = new DecimalFormat("000")
		String grad = df_grad.format(grad_value)
		
		BigDecimal minute_value = 60*(longitudeMath - grad_value)
		DecimalFormat df_minute = new DecimalFormat("00.000")
		String minute = df_minute.format(minute_value).replace(',', '')
		
		if (east) {
			return "${grad}${minute}E"
		} else {
			return "${grad}${minute}W"
		}
	}
	
	//--------------------------------------------------------------------------
	String GACAltitudeStr(BigDecimal altitudeMeter)
	// Return xxxxx (5)
	{
		BigDecimal altitude = ftPerMeter * altitudeMeter
		DecimalFormat df = new DecimalFormat("00000")
		return df.format(altitude)
	}
	
	//--------------------------------------------------------------------------
	String GACGroundSpeedStr(BigDecimal groundSpeed)
	// Return xxxx (4) Zentel Knoten 
	{
		BigDecimal tenth_ground_speed = 10 * groundSpeed
		DecimalFormat df = new DecimalFormat("0000")
		return df.format(tenth_ground_speed)
	}
	
	//--------------------------------------------------------------------------
	void DeleteFile(String fileName)
	{
		print "Delete '$fileName'..."
		File file = new File(fileName)
		if (file.delete()) {
			println "Done."
		} else {
			println "Error."
		}
	}
	
    //--------------------------------------------------------------------------
    Map SendFTP(Object configFlightContest, String baseDir, String sourceURL, String destFileName)
    {
        printstart "SendFTP ${configFlightContest.ftp.host}:${configFlightContest.ftp.port}"
        Map ret = [:]
        try {
            String dest_file_name = "${configFlightContest.ftp.directory}/$baseDir/${destFileName}"
            println "${configFlightContest.ftp.host}:${configFlightContest.ftp.port}${dest_file_name}"
            new FTPClient().with {
                connect(configFlightContest.ftp.host, configFlightContest.ftp.port)
                enterLocalPassiveMode()
                if (login(configFlightContest.ftp.username, configFlightContest.ftp.password)) {
                    if (configFlightContest.ftp.directory) {
                        if (changeWorkingDirectory(configFlightContest.ftp.directory)) {
                            if (changeWorkingDirectory(baseDir) || makeDirectory(baseDir)) {
                                if (destFileName && sourceURL) {
                                    if (storeFile(dest_file_name, new URL(sourceURL).openStream())) {
                                        ret.message = getMsg('fc.net.ftp.filecopyok',[sourceURL,dest_file_name])
                                    } else {
                                        ret.message = getMsg('fc.net.ftp.filecopyerror',[sourceURL,dest_file_name])
                                        ret.error = true
                                    }
                                }
                            } else {
                                ret.message = getMsg('fc.net.ftp.dircreateerror',[dir])
                                ret.error = true
                            }
                        } else {
                            ret.message = getMsg('fc.net.ftp.nobasedestdirerror',[configFlightContest.ftp.directory])
                            ret.error = true
                        }
                    } else {
                        ret.message = getMsg('fc.net.ftp.nobasedirerror')
                        ret.error = true
                    }
                } else {
                    ret.message = getMsg('fc.net.ftp.loginerror',[configFlightContest.ftp.username])
                    ret.error = true
                }
                disconnect()
            }
            printdone ""
        } catch (Exception e) {
            ret.error = true
            ret.message = getMsg('fc.net.ftp.connecterror',["${configFlightContest.ftp.host}:${configFlightContest.ftp.port}"]) + ": ${e.getMessage()}"
            printerror ret.message
        }
        return ret
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
