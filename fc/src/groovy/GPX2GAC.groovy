import java.io.BufferedWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat

class GPX2GAC
{
    //--------------------------------------------------------------------------
    final static String GPXGACCONVERTER_VERSION = "1.0"
    final static String GACFORMAT_DEF = "I033639GSP4042TRT4346FXA"
    final static BigDecimal ftPerMeter = 3.2808 // 1 meter = 3,2808 feet
    final static boolean WRLOG = false
    
    //--------------------------------------------------------------------------
    static Map Convert(String gpxFileName, String gacFileName)
    {
        Map ret = [converted:true, errmsg:'', onetrack:true]
        
        if (WRLOG) {
            println "GPX2GAC.Convert $gpxFileName -> $gacFileName"
        }
        
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
                ret.converted = false
                ret.onetrack = false
            }
        } catch (Exception e) {
            ret.converted = false
            ret.errmsg = e.getMessage()
        }
        gac_writer.close()
        gpx_reader.close()
        
        if (WRLOG) {
            if (ret.converted) {
                println ""
            } else {
                println "Error: $ret.errmsg"
            }
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private static String GACTimeStr(String gpxTime)
    // gpxTime - yyyy-mm-ddThh:mm:ssZ
    // Return hhmmss (6)
    {
        String ret = ""
        ret += gpxTime.substring(11, 13)
        ret += gpxTime.substring(14, 16)
        ret += gpxTime.substring(17, 19)
        return ret
    }
    
    //--------------------------------------------------------------------------
    private static String GACLatitudeStr(BigDecimal latitudeMath)
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
    private static String GACLongitudeStr(BigDecimal longitudeMath)
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
    private static String GACAltitudeStr(BigDecimal altitudeMeter)
    // Return xxxxx (5)
    {
        BigDecimal altitude = ftPerMeter * altitudeMeter
        DecimalFormat df = new DecimalFormat("00000")
        return df.format(altitude)
    }
    
    //--------------------------------------------------------------------------
    private static String GACGroundSpeedStr(BigDecimal groundSpeed)
    // Return xxxx (4) Zentel Knoten 
    {
        BigDecimal tenth_ground_speed = 10 * groundSpeed
        DecimalFormat df = new DecimalFormat("0000")
        return df.format(tenth_ground_speed)
    }
    
    //--------------------------------------------------------------------------
    private static void WriteLine(BufferedWriter fileWriter, String wrLine)
    {
        fileWriter.write(wrLine)
        fileWriter.newLine()
    }
}
