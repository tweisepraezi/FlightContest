import java.math.BigDecimal;

class TrackPoint // DB-2.12
{
    String utc                // UTCDateTime
    BigDecimal latitude       // Breitengrad
    BigDecimal longitude      // Längengrad
    Integer altitude          // ft
    Integer track             // Grad
    
    static belongsTo = [loggerdata:LoggerData]
    
    static constraints = {
        utc(size:0..30)
        latitude(range:-90..90, scale:10)
        longitude(range:-179.9999999999..180, scale:10)
        altitude()
        track(nullable:true,range:0..360)
    }
}
