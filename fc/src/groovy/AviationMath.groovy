import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Map

class AviationMath 
{
	//--------------------------------------------------------------------------
	static Map calculateLeg(BigDecimal destLatitude, BigDecimal destLongitude,
		                    BigDecimal srcLatitude, BigDecimal srcLongitude)
	// Berechnet Kurs und Entfernung einer Etappe von Koordinate scr... zur Koordinate dest...
	//   Latitude: Geographische Breite (-90 ... +90 Grad)
	//   Longitude: Geographische Laenge (-179.999 ... +180 Grad)
	// Rückgabe: dis in NM
	//           dir in Grad (0...359.999) 
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
		return [dis:distance, dir:direction]
	}
	
	//--------------------------------------------------------------------------
	static Map calculateWind(BigDecimal windDirection, BigDecimal windSpeed, BigDecimal valueTAS, 
							 BigDecimal trueTrack, BigDecimal valueDistance)
	// Berechnet Vorhaltewinkel, Geschwindigkeit über Grund und Flugzeit des Flugzeuges
	// unter Berücksichtigung des Windes.
	//   windDirection: Windrichtung 0 ... 359.999 Grad
	//   windSpeed: Windgeschwindigkeit in kn
	//   valueTAS: TAS des Flugzeuges in kn
	//   trueTrack: Kurs des Flugzeuges über Grund in Grad
	//   valueDistance: Entfernung der Etappe in NM
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
	   return [trueheading:trueheading, groundspeed:groundspeed, legtime:legtime]
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
    
    //--------------------------------------------------------------------------
    static Map getCoordinate(BigDecimal startLatitude, BigDecimal startLongitude,
                             BigDecimal valueTrack, BigDecimal valueDistance)
    // Berechnet Koordinate aus Start-Koordinate, Kurs und Entfernung
    //   startLatitude: Geographische Breite (-90 ... +90 Grad)
    //   startLongitude: Geographische Laenge (-179.999 ... +180 Grad)
    //   valueTrack: Kurs in Grad (0 ... 359.999 Grad)
    //   valueDistance: Entfernung in NM
    // Rückgabe: lat, lon
    {
        BigDecimal latitude_dist = 0
        BigDecimal longitude_dist = 0
        if ((valueTrack >= 0) && (valueTrack < 90)) {
            latitude_dist = valueDistance * Math.cos(Math.toRadians(valueTrack))
            longitude_dist = valueDistance * Math.sin(Math.toRadians(valueTrack))
        } else if ((valueTrack >= 90) && (valueTrack < 180)) {
            BigDecimal track_value = valueTrack - 90
            latitude_dist = valueDistance * Math.sin(Math.toRadians(track_value))
            longitude_dist = valueDistance * Math.cos(Math.toRadians(track_value))
        } else if ((valueTrack >= 180) && (valueTrack < 270)) {
            BigDecimal track_value = valueTrack - 180
            latitude_dist = valueDistance * Math.cos(Math.toRadians(track_value))
            longitude_dist = valueDistance * Math.sin(Math.toRadians(track_value))
        } else if ((valueTrack >= 270) && (valueTrack < 360)) {
            BigDecimal track_value = valueTrack - 270
            latitude_dist = valueDistance * Math.sin(Math.toRadians(track_value))
            longitude_dist = valueDistance * Math.cos(Math.toRadians(track_value))
        } else {
            throw new Exception("Invalid track ${valueTrack}")
        }
        
        BigDecimal latitude_diff = latitude_dist / 60
        latitude_diff = latitude_diff.setScale(10, RoundingMode.HALF_EVEN)
        BigDecimal latitude
        if ((valueTrack >= 0) && (valueTrack < 90)) {
            latitude = startLatitude + latitude_diff
        } else if ((valueTrack >= 90) && (valueTrack < 180)) {
            latitude = startLatitude - latitude_diff
        } else if ((valueTrack >= 180) && (valueTrack < 270)) {
            latitude = startLatitude - latitude_diff
        } else if ((valueTrack >= 270) && (valueTrack < 360)) {
            latitude = startLatitude + latitude_diff
        }

        BigDecimal longitude_diff = longitude_dist / (60 * Math.cos( Math.toRadians((startLatitude + latitude)/2) ))
        longitude_diff = longitude_diff.setScale(10, RoundingMode.HALF_EVEN)
        BigDecimal longitude
        if ((valueTrack >= 0) && (valueTrack < 90)) {
            longitude = startLongitude + longitude_diff
        } else if ((valueTrack >= 90) && (valueTrack < 180)) {
            longitude = startLongitude + longitude_diff
        } else if ((valueTrack >= 180) && (valueTrack < 270)) {
            longitude = startLongitude - longitude_diff
        } else if ((valueTrack >= 270) && (valueTrack < 360)) {
            longitude = startLongitude - longitude_diff
        }

        // return Map
        return [lat:latitude, lon:longitude]
    }
    
    //--------------------------------------------------------------------------
    static BigDecimal getOrthogonalTrackRight(BigDecimal valueTrack)
    // Berechnet senkrechten Kurs in Uhrzeigersinn (+ 90 Grad)
    //   valueTrack: Kurs in Grad (0 ... 359.999 Grad)
    // Rückgabe: Kurs in Grad (0 ... 359.999 Grad)
    {
        BigDecimal track = valueTrack + 90
        if (track >= 360) {
            track -= 360
        }
        return track
    }
    
    //--------------------------------------------------------------------------
    static BigDecimal getOrthogonalTrackLeft(BigDecimal valueTrack)
    // Berechnet senkrechten Kurs gegen Uhrzeigersinn (- 90 Grad)
    //   valueTrack: Kurs in Grad (0 ... 359.999 Grad)
    // Rückgabe: Kurs in Grad (0 ... 359.999 Grad)
    {
        BigDecimal track = valueTrack - 90
        if (track < 0) {
            track += 360
        }
        return track
    }
    
    //--------------------------------------------------------------------------
    static BigDecimal getDiametricalTrack(BigDecimal valueTrack)
    // Berechnet Gegenkurs
    //   valueTrack: Kurs in Grad (0 ... 359.999 Grad)
    // Rückgabe: Kurs in Grad (0 ... 359.999 Grad)
    {
        BigDecimal track = valueTrack + 180
        if (track >= 360 ) {
            track -= 360
        }
        return track
    }
    
    //--------------------------------------------------------------------------
    static Map getGate(BigDecimal srcLatitude, BigDecimal srcLongitude,
                       BigDecimal destLatitude, BigDecimal destLongitude,
                       Float gateWidth)
    // Berechnet Koordinaten eines Gates an Koordinate dest... 
    // aus einer Etappe von der Koordinate scr... zur Koordinate dest...
    //   Latitude: Geographische Breite (-90 ... +90 Grad)
    //   Longitude: Geographische Laenge (-179.999 ... +180 Grad)
    //   gateWidth: Gate-Breite in NM
    // Rückgabe: coordLeft.lat, coordLeft.lon, coordRight.lat, coordRight.lon
    {
        BigDecimal gate_track = calculateLeg(destLatitude, destLongitude, srcLatitude, srcLongitude).dir
        
        BigDecimal left_track = getOrthogonalTrackLeft(gate_track)
        BigDecimal right_track = getOrthogonalTrackRight(gate_track)
        
        Map left_coord = getCoordinate(destLatitude, destLongitude, left_track, gateWidth/2)
        Map right_coord = getCoordinate(destLatitude, destLongitude, right_track, gateWidth/2)
        
        // return Map
        return [coordLeft:left_coord, coordRight:right_coord, gateTrack:gate_track]
    }

    //--------------------------------------------------------------------------
    static Map getGate(BigDecimal gateLatitude, BigDecimal gateLongitude,
                       BigDecimal gateTrack, BigDecimal gateOffset, 
                       BigDecimal gateOrthogonalOffset, Float gateWidth)
    // Berechnet Koordinaten eines Gates an Koordinate gateLatitude/gateLongitude
    //   Latitude: Geographische Breite (-90 ... +90 Grad)
    //   Longitude: Geographische Laenge (-179.999 ... +180 Grad)
    //   gateTrack: Gate-Richtung in Grad (0 ... 359.999 Grad)
    //   gateOffset: Positionsabweichung längs zur Startbahn [NM]
    //   gateOrthogonalOffset: Positionsabweichung quer zur Startbahn [NM]
    //   gateWidth: Gate-Breite in NM
    // Rückgabe: coordLeft.lat, coordLeft.lon, coordRight.lat, coordRight.lon
    {
        Map gate_coord = [lat:gateLatitude, lon:gateLongitude]
        
        BigDecimal left_track = getOrthogonalTrackLeft(gateTrack)
        BigDecimal right_track = getOrthogonalTrackRight(gateTrack)
        
        if (gateOffset) {
            gate_coord = getCoordinate(gate_coord.lat, gate_coord.lon, gateTrack, gateOffset)
        }
        if (gateOrthogonalOffset) {
            gate_coord = getCoordinate(gate_coord.lat, gate_coord.lon, left_track, gateOrthogonalOffset)
        }
        
        Map left_coord = getCoordinate(gate_coord.lat, gate_coord.lon, left_track, gateWidth/2)
        Map right_coord = getCoordinate(gate_coord.lat, gate_coord.lon, right_track, gateWidth/2)
        
        // return Map
        return [coord:gate_coord, coordLeft:left_coord, coordRight:right_coord, gateTrack:gateTrack]
    }

    //--------------------------------------------------------------------------
    static Map getGateAtDistance(BigDecimal srcLatitude, BigDecimal srcLongitude,
                                 BigDecimal destLatitude, BigDecimal destLongitude,
                                 BigDecimal gateDistance, Float gateWidth)
    // Berechnet Koordinaten eines Gates, welches in der Entfernung gateDistance
    // von der Koordinate srcLatitude/srcLongitude
    // in Richtung der Etappe von der Koordinate scr... zur Koordinate dest...  liegt
    //   Latitude: Geographische Breite (-90 ... +90 Grad)
    //   Longitude: Geographische Laenge (-179.999 ... +180 Grad)
    //   gateDistance: Entfernung des Gates von srcLatitude/srcLongitude
    //   gateWidth: Gate-Breite in NM
    // Rückgabe: coordLeft.lat, coordLeft.lon, coordRight.lat, coordRight.lon
    {
        Map leg = calculateLeg(destLatitude, destLongitude, srcLatitude, srcLongitude)
        if (gateDistance > leg.dis) {
            gateDistance = 0.8 * leg.dis
        }
        Map dest_coord = getCoordinate(srcLatitude, srcLongitude, leg.dir, gateDistance)
        
        BigDecimal left_track = getOrthogonalTrackLeft(leg.dir)
        BigDecimal right_track = getOrthogonalTrackRight(leg.dir)
        
        Map left_coord = getCoordinate(dest_coord.lat, dest_coord.lon, left_track, gateWidth/2)
        Map right_coord = getCoordinate(dest_coord.lat, dest_coord.lon, right_track, gateWidth/2)
        
        // return Map
        return [coordLeft:left_coord, coordRight:right_coord, gateTrack:leg.dir]
    }

    //--------------------------------------------------------------------------
    static Map getShowPoint(BigDecimal showLatitude, BigDecimal showLongitude, BigDecimal showDistance, BigDecimal scaleBarLen)
    // Berechnet Anzeige-Bereich um eine Koordinate
    //   showLatitude: Geographische Breite (-90 ... +90 Grad)
    //   showLongitude: Geographische Laenge (-179.999 ... +180 Grad)
    //   showDistance: Entfernung des Anzeige-Bereiches um Koorodinate in NM
    //   scaleBarDistance: Länge von Scalebar-Strichen
    // Rückgabe Anzeige-Bereich latmin, latmax, lonmin, lonmax
    // Rückgabe Scalbar: latmin2, latmax2, lonmin2, lonmax2
    {
        Map left_coord = getCoordinate(showLatitude, showLongitude, 270, showDistance)
        Map left_coord2 = getCoordinate(showLatitude, left_coord.lon, 90, scaleBarLen)
        Map right_coord = getCoordinate(showLatitude, showLongitude, 90, showDistance)
        Map right_coord2 = getCoordinate(showLatitude, right_coord.lon, 270, scaleBarLen)
        Map top_coord = getCoordinate(showLatitude, showLongitude, 0, showDistance)
        Map top_coord2 = getCoordinate(top_coord.lat, showLongitude, 180, scaleBarLen)
        Map bottom_coord = getCoordinate(showLatitude, showLongitude, 180, showDistance)
        Map bottom_coord2 = getCoordinate(bottom_coord.lat, showLongitude, 0, scaleBarLen)
        return [latmin:bottom_coord.lat, latmax:top_coord.lat, lonmin:left_coord.lon, lonmax:right_coord.lon, latmin2:top_coord2.lat, latmax2:bottom_coord2.lat, lonmin2:right_coord2.lon, lonmax2:left_coord2.lon]
    }
    
    //--------------------------------------------------------------------------
    static Map getShowRect(BigDecimal minLatitude, BigDecimal maxLatitude, BigDecimal minLongitude, BigDecimal maxLongitude, BigDecimal marginDistance)
    // Berechnet Anzeige-Bereich um eine Rechteck
    //   minLatitude, maxLatitude: Geographische Breite (-90 ... +90 Grad)
    //   minLongitude, maxLongitude : Geographische Laenge (-179.999 ... +180 Grad)
    //   showDistance: Anzeige-Bereiches um Rechteck in NM
    // Rückgabe: latmin, latmax, lonmin, lonmax
    {
        BigDecimal lat_average = (minLatitude + maxLatitude) / 2
        BigDecimal lon_average = (minLongitude + maxLongitude) / 2
        
        Map left_coord = getCoordinate(lat_average, minLongitude, 270, marginDistance)
        Map right_coord = getCoordinate(lat_average, maxLongitude, 90, marginDistance)
        Map top_coord = getCoordinate(maxLatitude, lon_average, 0, marginDistance)
        Map bottom_coord = getCoordinate(minLatitude, lon_average, 180, marginDistance)
        return [latmin:bottom_coord.lat,latmax:top_coord.lat,lonmin:left_coord.lon,lonmax:right_coord.lon]
    }

    //--------------------------------------------------------------------------
    static List getCircle(BigDecimal showLatitude, BigDecimal showLongitude, BigDecimal radiusValue)
    // Berechnet Kreis um eine Koordinate
    //   showLatitude: Geographische Breite (-90 ... +90 Grad)
    //   showLongitude: Geographische Laenge (-179.999 ... +180 Grad)
    //   radiusValue: Radius des Kreises in NM
    // Rückgabe: Liste von Koordinaten (lat, lon)
    {
        List circle_coords = []
        BigDecimal value_track = 0
        while (value_track < 360) {
            circle_coords += getCoordinate(showLatitude, showLongitude, value_track, radiusValue)
            value_track += 2
        }
        circle_coords += getCoordinate(showLatitude, showLongitude, 0, radiusValue)
        return circle_coords
    }
    
    //--------------------------------------------------------------------------
    static Map getTrack2Circle(BigDecimal srcLatitude, BigDecimal srcLongitude,
                               BigDecimal destLatitude, BigDecimal destLongitude,
                               BigDecimal radiusValue)
    // Berechnet Koordinaten eines  Tracks zum Kreis um Koordinaten
    // aus einer Etappe von der Koordinate scr... zur Koordinate dest...
    //   Latitude: Geographische Breite (-90 ... +90 Grad)
    //   Longitude: Geographische Laenge (-179.999 ... +180 Grad)
    //   radiusValue: Radius des Kreises in NM
    // Rückgabe: srcLat, srcLon, destLat, destLon
    {
        BigDecimal track_value = calculateLeg(destLatitude, destLongitude, srcLatitude, srcLongitude).dir

        Map src_coord = getCoordinate(srcLatitude, srcLongitude, track_value, radiusValue)
        Map dest_coord = getCoordinate(destLatitude, destLongitude, getDiametricalTrack(track_value), radiusValue)
        
        // return Map
        return [srcLat:src_coord.lat, srcLon:src_coord.lon, destLat:dest_coord.lat, destLon:dest_coord.lon]
    }

    //--------------------------------------------------------------------------
    static List getProcedureTurnCircle(BigDecimal srcLatitude, BigDecimal srcLongitude,
                                       BigDecimal ptLatitude, BigDecimal ptLongitude,
                                       BigDecimal destLatitude, BigDecimal destLongitude,
                                       BigDecimal tpRadiusValue,
                                       BigDecimal procedureTurnDistance)
    // Berechnet ProcedureTurn-Halbkreis um eine Koordinate
    // eingehende Etappe von der Koordinate scr... zur Koordinate pt...
    // ausgehende Etappe von der Koordinate pt... zur Koordinate dest...
    //   Latitude: Geographische Breite (-90 ... +90 Grad)
    //   Longitude: Geographische Laenge (-179.999 ... +180 Grad)
    //   tpRadiusValue: Radius des Kreises um Wendepunkt in NM
    //   procedureTurnDistance: Entfernung des ProcedurTurn-Kreises vom Wendepunkt in NM
    // Rückgabe: Liste von Koordinaten (lat, lon)
    {
        List circle_coords = []
        
        BigDecimal in_track = calculateLeg(ptLatitude, ptLongitude, srcLatitude, srcLongitude).dir
        BigDecimal out_track = calculateLeg(destLatitude, destLongitude, ptLatitude, ptLongitude).dir
        BigDecimal course_change = courseChange(in_track, out_track)
        
        BigDecimal track_change
        BigDecimal circle_in_track
        BigDecimal circle_out_track
        BigDecimal circle_out_track2
        BigDecimal circle_add_track
        BigDecimal procedureturn_track
        if (course_change < 0) {
            track_change = 180 + course_change
            circle_in_track = getOrthogonalTrackLeft(in_track)
            circle_out_track = getOrthogonalTrackLeft(out_track)
            circle_out_track2 = circle_out_track
            if (circle_out_track < circle_in_track) {
                circle_out_track2 += 360
            }
            circle_add_track = 2
            procedureturn_track = checkTrack(in_track + track_change/2)
        } else {
            track_change = 180 - course_change
            circle_in_track = getOrthogonalTrackRight(in_track)
            circle_out_track = getOrthogonalTrackRight(out_track)
            circle_out_track2 = circle_out_track
            if (circle_out_track > circle_in_track) {
                circle_out_track2 -= 360
            }
            circle_add_track = -2
            procedureturn_track = checkTrack(in_track - track_change/2)
        }
        
        BigDecimal r = procedureTurnDistance * Math.sin(Math.toRadians(track_change/2))
        BigDecimal d = Math.sqrt(procedureTurnDistance*procedureTurnDistance - r*r)
        
        // in        
        Map in_coord = getCoordinate(ptLatitude, ptLongitude, in_track, tpRadiusValue)
        circle_coords += in_coord
        circle_coords += getCoordinate(in_coord.lat, in_coord.lon, in_track, d - tpRadiusValue)
        
        // circle
        Map pt_circle_coord = getCoordinate(ptLatitude, ptLongitude, procedureturn_track, procedureTurnDistance)
        BigDecimal value_track = circle_in_track
        BigDecimal value_track2 = circle_in_track
        // println "XX1 in_track=${circle_in_track} circle_out_track=${circle_out_track} circle_out_track2=${circle_out_track2} circle_add_track=${circle_add_track}"
        if (course_change < 0) {
            while (value_track2 < circle_out_track2) {
                circle_coords += getCoordinate(pt_circle_coord.lat, pt_circle_coord.lon, value_track, r)
                value_track = checkTrack(value_track + circle_add_track)
                value_track2 += circle_add_track
                // println "XX21 value_track2=${value_track2}"
            }
        } else {
            while (value_track2 > circle_out_track2) {
                circle_coords += getCoordinate(pt_circle_coord.lat, pt_circle_coord.lon, value_track, r)
                value_track = checkTrack(value_track + circle_add_track)
                value_track2 += circle_add_track
                // println "XX22 value_track2=${value_track2}"
            }
        }
        
        // out
        Map out_coord = getCoordinate(ptLatitude, ptLongitude, getDiametricalTrack(out_track), d)
        circle_coords += out_coord
        circle_coords += getCoordinate(out_coord.lat, out_coord.lon, out_track, d - tpRadiusValue)

        return circle_coords
    }
    
    //--------------------------------------------------------------------------
    static BigDecimal checkTrack(BigDecimal valueTrack)
    {
        if (valueTrack < 0) {
            return valueTrack + 360
        } else if (valueTrack >= 360 ) {
            return valueTrack - 360
        }
        return valueTrack
    }
    
    //--------------------------------------------------------------------------
    static Map getTitlePoint(BigDecimal srcLatitude, BigDecimal srcLongitude,
                             BigDecimal tpLatitude, BigDecimal tpLongitude,
                             BigDecimal destLatitude, BigDecimal destLongitude,
                             BigDecimal distanceValue, BigDecimal distanceValueProcedureTurn)
    // Berechnet Koordinate in Entfernung von der Koordinate tp... an der abgewandten Seite
    // eingehende Etappe von der Koordinate scr... zur Koordinate tp...
    // ausgehende Etappe von der Koordinate tp... zur Koordinate dest...
    //   Latitude: Geographische Breite (-90 ... +90 Grad)
    //   Longitude: Geographische Laenge (-179.999 ... +180 Grad)
    //   distanceValue, distanceValueProcedureTurn: Entfernung in NM
    // Rückgabe: lat, lon
    {
        BigDecimal in_track = calculateLeg(tpLatitude, tpLongitude, srcLatitude, srcLongitude).dir
        BigDecimal out_track = calculateLeg(destLatitude, destLongitude, tpLatitude, tpLongitude).dir
        
        BigDecimal course_change = courseChange(in_track, out_track)
        
        BigDecimal point_track
        if (course_change < 0) {
            BigDecimal track_change = 180 + course_change
            point_track = checkTrack(in_track + track_change/2)
        } else {
            BigDecimal track_change = 180 - course_change
            point_track = checkTrack(in_track - track_change/2)
        }
        
        if (course_change.abs() > 90) {
            distanceValue = distanceValueProcedureTurn
        }

        return getCoordinate(tpLatitude, tpLongitude, point_track, distanceValue)
    }
    
    //--------------------------------------------------------------------------
    static Map getOrthogonalTitlePoint(BigDecimal srcLatitude, BigDecimal srcLongitude,
                                       BigDecimal tpLatitude, BigDecimal tpLongitude,
                                       BigDecimal centerLatitude, BigDecimal centerLongitude,
                                       BigDecimal distanceValue)
    // Berechnet Koordinate in Entfernung von der Koordinate tp... an der zur Kartenmitte abgewandten Seite
    // eingehende Etappe von der Koordinate scr... zur Koordinate tp...
    // Kartenmitte ist Koordinate center...
    //   Latitude: Geographische Breite (-90 ... +90 Grad)
    //   Longitude: Geographische Laenge (-179.999 ... +180 Grad)
    //   distanceValue: Entfernung in NM
    // Rückgabe: lat, lon
    {
        Map gate = AviationMath.getGate(
            srcLatitude, srcLongitude,
            tpLatitude, tpLongitude,
            distanceValue
        )
        Map center_left = AviationMath.calculateLeg(gate.coordLeft.lat, gate.coordLeft.lon, centerLatitude, centerLongitude)
        Map center_right = AviationMath.calculateLeg(gate.coordRight.lat, gate.coordRight.lon, centerLatitude, centerLongitude)
        if (center_left.dis > center_right.dis) {
            return AviationMath.getTitlePoint(
                gate.coordRight.lat, gate.coordRight.lon,
                tpLatitude, tpLongitude,
                gate.coordRight.lat, gate.coordRight.lon,
                distanceValue, distanceValue
            )
        }
        return AviationMath.getTitlePoint(
            gate.coordLeft.lat, gate.coordLeft.lon,
            tpLatitude, tpLongitude,
            gate.coordLeft.lat, gate.coordLeft.lon,
            distanceValue, distanceValue
        )
    }
}
