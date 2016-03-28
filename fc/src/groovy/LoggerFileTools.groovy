class LoggerFileTools
{
    final static String GAC_EXTENSION = ".gac"
    final static String GPX_EXTENSION = ".gpx"
    
    final static String LOGGER_EXTENSIONS = "${LoggerFileTools.GAC_EXTENSION}, ${LoggerFileTools.GPX_EXTENSION}"
    
    final static boolean GAC_REPAIRIDENTICLATIMES = false
    
    //--------------------------------------------------------------------------
    static Map ReadLoggerFile(String fileExtension, Test testInstance, String loggerFileName)
    // Return: trackpointnum - Number of track points 
    //         valid         - true, if valid logger format
    //         errors        - <> ""
    {
        switch (fileExtension) {
            case GAC_EXTENSION:
                return ReadGACFile(testInstance, loggerFileName)
                break
            case GPX_EXTENSION:
                return ReadGPXFile(testInstance, loggerFileName)
                break
        }
        return [trackpointnum: 0, valid: false, errors: ""]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadGACFile(Test testInstance, String gacFileName)
    // Return: trackpointnum - Number of track points 
    //         valid         - true, if valid gac format
    //         errors        - <> ""
    {
        int track_point_num = 0
        boolean valid_format = false
        String read_errors = ""
        
        File gac_file = new File(gacFileName)
        LineNumberReader gac_reader = gac_file.newReader()
        try {
            boolean first = true
            String last_time_utc = null
            BigDecimal last_latitude = null
            BigDecimal last_longitude = null
            String last_utc = FcTime.UTC_GPX_DATE
            while (true) {
                String line = gac_reader.readLine()
                if (line == null) {
                    break
                }
                if (line) {
                    if (line == GpxService.GACFORMAT_DEF) {
                        valid_format = true
                    }
                    boolean ignore_line = false
                    if (line.startsWith("B")) {
                        if (valid_format) {
                            
                            // remove old track points
                            if (first) {
                                if (testInstance.IsLoggerData()) {
                                    TrackPoint.findAllByLoggerdata(testInstance.loggerData,[sort:"id"]).each { TrackPoint trackpoint_instance ->
                                        trackpoint_instance.delete()
                                    }
                                }
                            }
                            
                            // Repair DropOuts
                            String time_utc = line.substring(1,7)
                            if (!first) {
                                if (GAC_REPAIRIDENTICLATIMES) {
                                    if (last_time_utc == time_utc) { // Zeile mit doppelter Zeit entfernen
                                        ignore_line = true
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
                            boolean latitude_north = line.substring(14,15) == 'N'
                            BigDecimal latitude = latitude_grad_math + (latidude_minute_math / 60)
                            if (!latitude_north) {
                                latitude *= -1
                            }
                            
                            // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                            String longitude_grad = line.substring(15,18)
                            BigDecimal longitude_grad_math = longitude_grad.toBigDecimal()
                            String longitude_minute = line.substring(18,20) + '.' + line.substring(20,23)
                            BigDecimal longitude_minute_math = longitude_minute.toBigDecimal()
                            boolean longitude_east = line.substring(23,24) == 'E'
                            BigDecimal longitude = longitude_grad_math + (longitude_minute_math / 60)
                            if (!longitude_east) {
                                longitude *= -1
                            }
                            
                            // Altitude (Höhe) in ft
                            String altitude_foot = line.substring(30,35)
                            int altitude = altitude_foot.toInteger()

                            // Track in Grad
                            def track = null
                            if (last_latitude != null && last_longitude != null) {
                                Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                                track = FcMath.RoundGrad(leg.dir)
                            }
                            
                            // save track point
                            if (!ignore_line) {
                                TrackPoint trackpoint_instance = new TrackPoint()
                                trackpoint_instance.loggerdata = testInstance.loggerData
                                trackpoint_instance.utc = utc
                                trackpoint_instance.latitude = latitude
                                trackpoint_instance.longitude = longitude
                                trackpoint_instance.altitude = altitude
                                trackpoint_instance.track = track
                                trackpoint_instance.save()
                                
                                track_point_num++
                            }
                            
                            first = false
                            last_time_utc = time_utc
                            last_utc = utc
                            last_latitude = latitude
                            last_longitude = longitude
                        }
                    }
                }
            }
        } catch (Exception e) {
            read_errors = e.getMessage()
        }
        gac_reader.close()
        
        return [trackpointnum: track_point_num, valid: valid_format, errors: read_errors]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadGPXFile(Test testInstance, String gpxFileName)
    // Return: trackpointnum - Number of track points 
    //         valid         - true, if valid gpx format
    //         errors        - <> ""
    {
        int track_point_num = 0
        boolean valid_format = false
        String read_errors = ""
        
        File gpx_file = new File(gpxFileName)

        FileReader gpx_reader = new FileReader(gpx_file)
        try {
            def gpx = new XmlParser().parse(gpx_reader)
            if (gpx.trk.size() == 1) { // only 1 track allowed
                valid_format = true
                
                //def gpx_track_name = gpx.trk.name[0].text()
                def track_points = gpx.trk.trkseg.trkpt
                
                // write track points
                boolean first = true
                BigDecimal last_latitude = null
                BigDecimal last_longitude = null
                String last_utc = FcTime.UTC_GPX_DATE
                track_points.each {
                    
                    // remove old track points
                    if (first) {
                        if (testInstance.IsLoggerData()) {
                            TrackPoint.findAllByLoggerdata(testInstance.loggerData,[sort:"id"]).each { TrackPoint trackpoint_instance ->
                                trackpoint_instance.delete()
                            }
                        }
                    }
                    
                    // UTC
                    String utc_time = FcTime.UTCGetTime(it.time[0].text())
                    String utc = FcTime.UTCGetNextDateTime(last_utc, utc_time)
                    
                    // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                    BigDecimal latitude = it.'@lat'.toBigDecimal()

                    // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                    BigDecimal longitude = it.'@lon'.toBigDecimal()

                    // Altitude (Höhe) in ft
                    BigDecimal altitude_meter = it.ele[0].text().toBigDecimal()
                    int altitude = FcMath.RoundAltitude(altitude_meter * GpxService.ftPerMeter)
                    
                    // Track in Grad
                    def track = null
                    if (last_latitude != null && last_longitude != null) {
                        Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                        track = FcMath.RoundGrad(leg.dir)
                    }
                    
                    // save track point
                    TrackPoint trackpoint_instance = new TrackPoint()
                    trackpoint_instance.loggerdata = testInstance.loggerData
                    trackpoint_instance.utc = utc
                    trackpoint_instance.latitude = latitude
                    trackpoint_instance.longitude = longitude
                    trackpoint_instance.altitude = altitude
                    trackpoint_instance.track = track
                    trackpoint_instance.save()
                    track_point_num++
                    
                    first = false
                    last_utc = utc
                    last_latitude = latitude
                    last_longitude = longitude
                }
            } else {
            }
        } catch (Exception e) {
            read_errors = e.getMessage()
        }
        gpx_reader.close()
        
        return [trackpointnum: track_point_num, valid: valid_format, errors: read_errors]
    }        
}
