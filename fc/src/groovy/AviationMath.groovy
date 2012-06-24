import java.util.Map;

class AviationMath 
{
	//--------------------------------------------------------------------------
	static Map calculateLeg(BigDecimal destLatitude, BigDecimal destLongitude,
		                    BigDecimal srcLatitude, BigDecimal srcLongitude)
	// Berechnet Kurs und Entfernung einer Etappe von Koordinate scr... zur Koordinate dest...
	//   Latitude (Geographische Breite: -90 ... +90 Grad)
	//   Longitude (Geographische Laenge: -179.999 ... +180 Grad)
	// Rückgabe: distance in NM
	//           direction in Grad (0...359.999) 
	{
		// calculate leg distance
		BigDecimal latitude_diff = destLatitude - srcLatitude
		BigDecimal longitude_diff = destLongitude - srcLongitude
		
		BigDecimal latitude_dist = 60 * latitude_diff
		BigDecimal longitude_dist = 60 * longitude_diff * Math.cos( Math.toRadians((destLatitude + srcLatitude)/2) )
		
		BigDecimal distance = Math.sqrt(latitude_dist*latitude_dist + longitude_dist*longitude_dist)
		
		// calculate leg direction
		BigDecimal direction = 0.0
		if (latitude_dist == 0 && longitude_dist > 0) {
			direction = 90.0
		} else if (latitude_dist == 0 && longitude_dist < 0) {
			direction = 270.0
		} else if (longitude_dist == 0 && latitude_dist > 0) {
			direction = 0.0
		} else if (longitude_dist == 0 && latitude_dist < 0) {
			direction = 180.0
		} else if (latitude_dist > 0 && longitude_dist > 0) {
			direction = Math.toDegrees(Math.atan(longitude_dist/latitude_dist))
		} else if (latitude_dist < 0 && longitude_dist > 0) {
			direction = 180.0 + Math.toDegrees(Math.atan(longitude_dist/latitude_dist))
		} else if (latitude_dist < 0 && longitude_dist < 0) {
			direction = 180.0 + Math.toDegrees(Math.atan(longitude_dist/latitude_dist))
		} else if (latitude_dist > 0 && longitude_dist < 0) {
			direction = 360.0 + Math.toDegrees(Math.atan(longitude_dist/latitude_dist))
		}
		
		// return Map
		return [dis:distance,dir:direction]
	}
	
	//--------------------------------------------------------------------------
	static Map calculateWind(BigDecimal windDirection, BigDecimal windSpeed, BigDecimal valueTAS, 
							 BigDecimal trueTrack, BigDecimal valueDistance)
	// Berechnet Vorhaltewinkel, Geschwindigkeit über Grund und Flugzeit des Flugzeuges
	// unter Berücksichtigung des Windes.
	// windDirection: Windrichtung 0 ... 359.999 Grad
	// windSpeed: Windgeschwindigkeit in kt
	// valueTAS: TAS des Flugzeuges in kt
	// trueTrack: Kurs des Flugzeuges über Grund in Grad
	// valueDistance: Entfernung der Etappe in NM
	// Rückgabe: trueheading
	//           groundspeed
	//           legtime
	{
       // calculate True Heading and Ground Speed 
	   BigDecimal trueheading
	   BigDecimal groundspeed
       if (windSpeed > 0) {
           BigDecimal wind_direction = windDirection - 180
           BigDecimal beta = wind_direction - trueTrack
           BigDecimal sin_beta = Math.sin(Math.toRadians(beta)) 
           BigDecimal drift_angle = 0
           if (beta != 0 && beta != 180 && beta != -180) {
               drift_angle = Math.toDegrees(Math.asin(sin_beta*windSpeed/valueTAS))  
           }
           trueheading = trueTrack - drift_angle 
           BigDecimal gamma = 180 + trueheading - wind_direction
           BigDecimal sin_gamma = Math.sin(Math.toRadians(gamma))
           if (beta == 0) {
        	   groundspeed = valueTAS + windSpeed
           } else if (beta == 180 || beta == -180) {
        	   groundspeed = valueTAS - windSpeed
           } else {
        	   groundspeed = valueTAS * sin_gamma / sin_beta
           }
           if (trueheading > 360) {
        	   trueheading -= 360
           } else if (trueheading < 0) {
        	   trueheading += 360
           }
       } else {
    	   trueheading = trueTrack
    	   groundspeed = valueTAS
       }
	   
	   // calculate time
       BigDecimal legtime = valueDistance / groundspeed 
	   
	   // return Map
	   return [trueheading:trueheading,groundspeed:groundspeed,legtime:legtime]
	}
	
	//--------------------------------------------------------------------------
	static BigDecimal courseChange(BigDecimal oldTrueTrack, BigDecimal newTrueTrack)
	// Berechnet Kursänderung in Grad.
	//   positiver Wert - Kursänderung im Uhrzeigersinn
	//   negativer Wert - Kursänderung entgegen dem Uhrzeigersinn
	// oldTrueTrack: alter Kurs
	// newTrueTrack: neuer Kurs
	{
		if ((oldTrueTrack == null) || (newTrueTrack == null)) {
			return 0
		}
		BigDecimal ret = 0
		if (newTrueTrack >= oldTrueTrack) {
			ret = newTrueTrack - oldTrueTrack
			if (ret > 180) {
				ret -= 360
			}
		} else {
			ret = oldTrueTrack - newTrueTrack
			if (ret > 180) {
				ret -= 360
			}
			ret = -ret
		}
		return ret
	}
}
