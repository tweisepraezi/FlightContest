import java.util.Map
import java.nio.charset.StandardCharsets

class LoggerFileTools
{
    final static String GAC_EXTENSION = ".gac"
    final static String IGC_EXTENSION = ".igc"
    final static String GPX_EXTENSION = ".gpx"
    final static String KML_EXTENSION = ".kml"
    final static String KMZ_EXTENSION = ".kmz"
    final static String NMEA_EXTENSION = ".nmea"
    
    final static String LOGGER_EXTENSIONS = "${LoggerFileTools.GAC_EXTENSION}, ${LoggerFileTools.IGC_EXTENSION}, ${LoggerFileTools.GPX_EXTENSION}, ${LoggerFileTools.KML_EXTENSION}, ${LoggerFileTools.KMZ_EXTENSION}, ${LoggerFileTools.NMEA_EXTENSION}"
    
    final static boolean REMOVE_IDENTICAL_TIMES = true
    
    final static String IGCFORMAT_DEF = "I033638FXA3940SIU4143ENL"
    
    //--------------------------------------------------------------------------
    static Map ReadLoggerFile(String fileExtension, Test testInstance, String loggerFileName)
    // Return: trackpointnum - Number of track points 
    //         valid         - true, if valid logger format
    //         errors        - <> ""
    {
        switch (fileExtension) {
            case GAC_EXTENSION:
                return ReadGACFile(testInstance, loggerFileName)
            case IGC_EXTENSION:
                return ReadIGCFile(testInstance, loggerFileName)
            case GPX_EXTENSION:
                RemoveExistingBOM(loggerFileName)
                return ReadGPXFile(testInstance, loggerFileName)
            case KML_EXTENSION:
                RemoveExistingBOM(loggerFileName)
                return ReadKMLFile(testInstance, loggerFileName, false)
            case KMZ_EXTENSION:
                return ReadKMLFile(testInstance, loggerFileName, true)
            case NMEA_EXTENSION:
                return ReadNMEAFile(testInstance, loggerFileName)
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
            BigDecimal last_latitude = null
            BigDecimal last_longitude = null
            def last_track = null
            String last_utc = FcTime.UTC_GPX_DATE
            while (true) {
                String line = gac_reader.readLine()
                if (line == null) {
                    break
                }
                if (line) {
                    if (line.startsWith("I")) { //if (line == GPX2GAC.GACFORMAT_DEF) {
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
                            
                            // UTC
                            String utc_h = line.substring(1,3)
                            String utc_min = line.substring(3,5)
                            String utc_s = line.substring(5,7)
                            String utc = FcTime.UTCGetNextDateTime(last_utc, "${utc_h}:${utc_min}:${utc_s}")
                            
                            /*
                            if (utc == "2015-01-01T12:19:06Z") {
                                int j = 0
                            }
                            */
                            
                            // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                            String latitude_grad = line.substring(7,9)
                            BigDecimal latitude_grad_math = latitude_grad.toBigDecimal()
                            String latidude_minute = line.substring(9,11) + '.' + line.substring(11,14)
                            BigDecimal latidude_minute_math = latidude_minute.toBigDecimal()
                            boolean latitude_north = line.substring(14,15) == CoordPresentation.NORTH
                            BigDecimal latitude = latitude_grad_math + (latidude_minute_math / 60)
                            if (!latitude_north) {
                                latitude *= -1
                            }
                            
                            // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                            String longitude_grad = line.substring(15,18)
                            BigDecimal longitude_grad_math = longitude_grad.toBigDecimal()
                            String longitude_minute = line.substring(18,20) + '.' + line.substring(20,23)
                            BigDecimal longitude_minute_math = longitude_minute.toBigDecimal()
                            boolean longitude_east = line.substring(23,24) == CoordPresentation.EAST
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
                                if ((latitude == last_latitude) && (longitude == last_longitude)) {
                                    track = last_track
                                } else {
                                    Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                                    track = FcMath.RoundGrad(leg.dir)
                                }
                            }
                            
                            if (REMOVE_IDENTICAL_TIMES) {
                                if (utc == last_utc) {
                                    ignore_line = true
                                }
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
                                
                                last_utc = utc
                                last_latitude = latitude
                                last_longitude = longitude
                                last_track = track
                            }
                            
                            first = false
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
    private static Map ReadIGCFile(Test testInstance, String igcFileName)
    // Return: trackpointnum - Number of track points 
    //         valid         - true, if valid igc format
    //         errors        - <> ""
    {
        int track_point_num = 0
        boolean valid_format = false
        String read_errors = ""
        
        File igc_file = new File(igcFileName)
        LineNumberReader igc_reader = igc_file.newReader()
        try {
            boolean first = true
            BigDecimal last_latitude = null
            BigDecimal last_longitude = null
            def last_track = null
            String last_utc = FcTime.UTC_GPX_DATE
            while (true) {
                String line = igc_reader.readLine()
                if (line == null) {
                    break
                }
                if (line) {
                    if (line.startsWith("I")) { //if (line == IGCFORMAT_DEF) {
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
                            
                            // UTC
                            String utc_h = line.substring(1,3)
                            String utc_min = line.substring(3,5)
                            String utc_s = line.substring(5,7)
                            String utc = FcTime.UTCGetNextDateTime(last_utc, "${utc_h}:${utc_min}:${utc_s}")
                            
                            /*
                            if (utc == "2015-01-01T12:19:06Z") {
                                int j = 0
                            }
                            */
                            
                            // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                            String latitude_grad = line.substring(7,9)
                            BigDecimal latitude_grad_math = latitude_grad.toBigDecimal()
                            String latidude_minute = line.substring(9,11) + '.' + line.substring(11,14)
                            BigDecimal latidude_minute_math = latidude_minute.toBigDecimal()
                            boolean latitude_north = line.substring(14,15) == CoordPresentation.NORTH
                            BigDecimal latitude = latitude_grad_math + (latidude_minute_math / 60)
                            if (!latitude_north) {
                                latitude *= -1
                            }
                            
                            // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                            String longitude_grad = line.substring(15,18)
                            BigDecimal longitude_grad_math = longitude_grad.toBigDecimal()
                            String longitude_minute = line.substring(18,20) + '.' + line.substring(20,23)
                            BigDecimal longitude_minute_math = longitude_minute.toBigDecimal()
                            boolean longitude_east = line.substring(23,24) == CoordPresentation.EAST
                            BigDecimal longitude = longitude_grad_math + (longitude_minute_math / 60)
                            if (!longitude_east) {
                                longitude *= -1
                            }
                            
                            // Altitude (Höhe) in ft
                            int altitude_meter = line.substring(30,35).toInteger()
                            int altitude = FcMath.RoundAltitude(altitude_meter * GpxService.ftPerMeter)

                            // Track in Grad
                            def track = null
                            if (last_latitude != null && last_longitude != null) {
                                if ((latitude == last_latitude) && (longitude == last_longitude)) {
                                    track = last_track
                                } else {
                                    Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                                    track = FcMath.RoundGrad(leg.dir)
                                }
                            }
                            
                            if (REMOVE_IDENTICAL_TIMES) {
                                if (utc == last_utc) {
                                    ignore_line = true
                                }
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
                                
                                last_utc = utc
                                last_latitude = latitude
                                last_longitude = longitude
                                last_track = track
                            }
                            
                            first = false
                        }
                    }
                }
            }
        } catch (Exception e) {
            read_errors = e.getMessage()
        }
        igc_reader.close()
        
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
                def last_track = null
                String last_utc = FcTime.UTC_GPX_DATE
                track_points.each {
                    
                    boolean ignore_line = false
                    
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
                    
                    /*
                    if (utc == "2015-01-01T12:19:06Z") {
                        int j = 0
                    }
                    */
                    
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
                        if ((latitude == last_latitude) && (longitude == last_longitude)) {
                            track = last_track
                        } else {
                            Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                            track = FcMath.RoundGrad(leg.dir)
                        }
                    }
                    
                    if (REMOVE_IDENTICAL_TIMES) {
                        if (utc == last_utc) {
                            ignore_line = true
                        }
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
                        
                        last_utc = utc
                        last_latitude = latitude
                        last_longitude = longitude
                        last_track = track
                    }
                    
                    first = false
                }
            } else {
            }
        } catch (Exception e) {
            read_errors = e.getMessage()
        }
        gpx_reader.close()
        
        return [trackpointnum: track_point_num, valid: valid_format, errors: read_errors]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadKMLFile(Test testInstance, String kmFileName, boolean kmzFile)
    // Return: trackpointnum - Number of track points 
    //         valid         - true, if valid gpx format
    //         errors        - <> ""
    {
        int track_point_num = 0
        boolean valid_format = false
        String read_errors = ""
        
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

        try {
            def kml = new XmlParser().parse(km_reader)
            def gx_track = kml.Document.Placemark.'gx:Track'
                        
            if (gx_track && gx_track.when && gx_track.'gx:coord' && gx_track.when.size() == gx_track.'gx:coord'.size()) {
                valid_format = true
                
                boolean first = true
                BigDecimal last_latitude = null
                BigDecimal last_longitude = null
                def last_track = null
                String last_utc = FcTime.UTC_GPX_DATE
                
                int i = 0
                while (i < gx_track.when.size()) {
                    
                    String time_utc = gx_track.when[i].text()
                    String coord = gx_track.'gx:coord'[i].text()
                    def coord_values = coord.split(' ')
                    boolean ignore_line = false
                    
                    // remove old track points
                    if (first) {
                        if (testInstance.IsLoggerData()) {
                            TrackPoint.findAllByLoggerdata(testInstance.loggerData,[sort:"id"]).each { TrackPoint trackpoint_instance ->
                                trackpoint_instance.delete()
                            }
                        }
                    }
                    
                    // UTC
                    String utc = FcTime.UTCGetNextDateTime(last_utc, "${time_utc.substring(11,19)}")
                                
                    // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                    BigDecimal latitude = coord_values[1].toBigDecimal() 
                                
                    // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                    BigDecimal longitude = coord_values[0].toBigDecimal()
                                
                    // Altitude (Höhe)
                    BigDecimal altitude_meter = coord_values[2].toBigDecimal()
                    int altitude = FcMath.RoundAltitude(altitude_meter * GpxService.ftPerMeter)
                    
                    // Track in Grad
                    def track = null
                    if (last_latitude != null && last_longitude != null) {
                        if ((latitude == last_latitude) && (longitude == last_longitude)) {
                            track = last_track
                        } else {
                            Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                            track = FcMath.RoundGrad(leg.dir)
                        }
                    }
                    
                    if (REMOVE_IDENTICAL_TIMES) {
                        if (utc == last_utc) {
                            ignore_line = true
                        }
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
                        
                        last_utc = utc
                        last_latitude = latitude
                        last_longitude = longitude
                        last_track = track
                    }
                    
                    first = false
                    i++
                }
            } else {
            }
        } catch (Exception e) {
            read_errors = e.getMessage()
        }
        if (km_reader) {
            km_reader.close()
        }
        if (kmz_file) {
            kmz_file.close()
        }

        return [trackpointnum: track_point_num, valid: valid_format, errors: read_errors]
    }
    
    //--------------------------------------------------------------------------
    private static Map ReadNMEAFile(Test testInstance, String nmeaFileName)
    // Return: trackpointnum - Number of track points 
    //         valid         - true, if valid nmea format
    //         errors        - <> ""
    {
        int track_point_num = 0
        boolean valid_format = false
        String read_errors = ""
        
        File nmea_file = new File(nmeaFileName)
        LineNumberReader igc_reader = nmea_file.newReader()
        try {
            boolean first = true
            BigDecimal last_latitude = null
            BigDecimal last_longitude = null
            def last_track = null
            String last_utc = FcTime.UTC_GPX_DATE
            while (true) {
                String line = igc_reader.readLine()
                if (line == null) {
                    break
                }
                if (line) {
                    if (line.startsWith('$GPGGA')) {
                        valid_format = true
                    }
                    boolean ignore_line = false
                    if (line.startsWith('$GPGGA')) {
                        if (valid_format) {
                            
                            // remove old track points
                            if (first) {
                                if (testInstance.IsLoggerData()) {
                                    TrackPoint.findAllByLoggerdata(testInstance.loggerData,[sort:"id"]).each { TrackPoint trackpoint_instance ->
                                        trackpoint_instance.delete()
                                    }
                                }
                            }
                            
                            String[] line_values = line.split(",")
                            
                            // UTC
                            String utc_h = line_values[1].substring(0,2)
                            String utc_min = line_values[1].substring(2,4)
                            String utc_s = line_values[1].substring(4,6)
                            String utc = FcTime.UTCGetNextDateTime(last_utc, "${utc_h}:${utc_min}:${utc_s}")
                            
                            /*
                            if (utc == "2015-01-01T12:19:06Z") {
                                int j = 0
                            }
                            */
                            
                            // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                            String latitude_grad = line_values[2].substring(0,2)
                            BigDecimal latitude_grad_math = latitude_grad.toBigDecimal()
                            String latidude_minute = line_values[2].substring(2)
                            BigDecimal latidude_minute_math = latidude_minute.toBigDecimal()
                            boolean latitude_north = line_values[3] == CoordPresentation.NORTH
                            BigDecimal latitude = latitude_grad_math + (latidude_minute_math / 60)
                            if (!latitude_north) {
                                latitude *= -1
                            }
                            
                            // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                            String longitude_grad = line_values[4].substring(0,3)
                            BigDecimal longitude_grad_math = longitude_grad.toBigDecimal()
                            String longitude_minute = line_values[4].substring(4)
                            BigDecimal longitude_minute_math = longitude_minute.toBigDecimal()
                            boolean longitude_east = line_values[5] == CoordPresentation.EAST
                            BigDecimal longitude = longitude_grad_math + (longitude_minute_math / 60)
                            if (!longitude_east) {
                                longitude *= -1
                            }
                            
                            // Altitude (Höhe) in ft
                            String altitude_meter = "0.0"
                            if (line_values[10] == 'M') {
                                altitude_meter = line_values[9]
                            }
                            BigDecimal altitude_meter2 = altitude_meter.toBigDecimal()
                            int altitude = FcMath.RoundAltitude(altitude_meter2 * GpxService.ftPerMeter)

                            // Track in Grad
                            def track = null
                            if (last_latitude != null && last_longitude != null) {
                                if ((latitude == last_latitude) && (longitude == last_longitude)) {
                                    track = last_track
                                } else {
                                    Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                                    track = FcMath.RoundGrad(leg.dir)
                                }
                            }
                            
                            if (REMOVE_IDENTICAL_TIMES) {
                                if (utc == last_utc) {
                                    ignore_line = true
                                }
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
                                
                                last_utc = utc
                                last_latitude = latitude
                                last_longitude = longitude
                                last_track = track
                            }
                            
                            first = false
                        }
                    }
                }
            }
        } catch (Exception e) {
            read_errors = e.getMessage()
        }
        igc_reader.close()
        
        return [trackpointnum: track_point_num, valid: valid_format, errors: read_errors]
    }
    
    //--------------------------------------------------------------------------
    static void RemoveExistingBOM(String fileName)
    {
        File file = new File(fileName)
        BufferedReader reader = file.newReader()
        
        String new_filename = "${fileName}.new"
        File new_file = new File(new_filename)
        BufferedWriter new_writer = new_file.newWriter()
        
        while (true) {
            String line = reader.readLine()
            if (line == null) {
                break
            }
            new_writer.writeLine(line)
        }

        reader.close()
        
        new_writer.close()
        file.delete()
        new_file.renameTo(new File(fileName))
    }
    
}
