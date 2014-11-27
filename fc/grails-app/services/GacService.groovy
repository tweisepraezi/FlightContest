import java.text.*

class GacService 
{
	def logService

	final static BigDecimal ftPerMeter = 3.2808 // 1 meter = 3,2808 feet
	
	final static String GPXGACCONVERTER_VERSION = "1.0"
	
	final static String GACFORMAT_DEF = "I033639GSP4042TRT4346FXA"
	
	final static boolean WRLOG = false
	
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
				def track_points = gpx.trk.trkseg.trkpt
				
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
