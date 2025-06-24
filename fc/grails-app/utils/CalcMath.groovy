import java.math.*
import java.math.RoundingMode
import java.util.Map

class CalcMath 
{
    final static int COORDINATE_SCALE          = 10   // Nachkommastellen für Koordinaten
    
    //--------------------------------------------------------------------------
    static Map isLineCrossed(Map trackLine, Map gateLine, Map advancedGateLine)
    {
        Map ret = [onTrackLine:false, onGateLine:false, onAdvancedGateLine:false, lastPointNearer:false, runException:false]
        
        try {
            Map cross_point = getCrossPoint(gateLine, trackLine)
        
            ret.onTrackLine = isPointOnLine(cross_point, trackLine, "Track")
            ret.onGateLine = isPointOnLine(cross_point, gateLine, "Gate")
            ret.onAdvancedGateLine = isPointOnLine(cross_point, advancedGateLine, "AdvancedGate")
                        
            if (ret.onTrackLine) {
                BigDecimal dist_1 = getPointDistance(cross_point.x, cross_point.y, trackLine.x1, trackLine.y1)
                BigDecimal dist_2 = getPointDistance(cross_point.x, cross_point.y, trackLine.x2, trackLine.y2)
                if (dist_1 < dist_2) {
                    ret.lastPointNearer = true
                }
            }
            
            /*
            if (ret.onTrackLine) {
                println "isLineCrossed: onGateLine: $ret.onGateLine, ${RouteGradStr2(cross_point.x)}, ${RouteGradStr2(cross_point.y)}"
            } else {
                println "Not isLineCrossed: ${RouteGradStr2(cross_point.x)}, ${RouteGradStr2(cross_point.y)}"
            }
            */
        } catch (Exception e) {
            ret.runException = true
        }
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    private static Map getCrossPoint(Map gateLine, Map trackLine)
    {
        Map l1 = getLineParams(gateLine)
        Map l2 = getLineParams(trackLine)
        
        BigDecimal x = getDeterminate(l1.B, l1.C, l2.B, l2.C) / getDeterminate(l1.A, l1.B, l2.A, l2.B)
        BigDecimal y = getDeterminate(l1.C, l1.A, l2.C, l2.A) / getDeterminate(l1.A, l1.B, l2.A, l2.B)

        BigDecimal x1 = x.setScale(COORDINATE_SCALE, RoundingMode.HALF_EVEN) 
        BigDecimal y1 = y.setScale(COORDINATE_SCALE, RoundingMode.HALF_EVEN)
        
        return [x:x1, y:y1]
    }
    
    //--------------------------------------------------------------------------
    private static Map getLineParams(Map line)
    {
        Map ret = [:]
        if (line.x1 == line.x2) {
            ret.A = 1
            ret.B = 0
            ret.C = -1 * line.x1
        } else if (line.y1 == line.y2) {
            ret.A = 0
            ret.B = 1
            ret.C = -1 * line.y1
        } else {
            ret.A = -1 * ((line.y2 - line.y1) / (line.x2 - line.x1))
            ret.B = 1
            ret.C = (line.x1 * (line.y2 - line.y1) / (line.x2 - line.x1)) - line.y1
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private static BigDecimal getDeterminate(BigDecimal a11, BigDecimal a12, BigDecimal a21, BigDecimal a22)
    {
        return (a11 * a22) - (a21 * a12)
    }
 
    //--------------------------------------------------------------------------
    private static boolean isPointOnLine(Map pointValue, Map lineValue, String s)
    {
        boolean on_gate_x = false
        if (lineValue.x2 > lineValue.x1) {
            on_gate_x = (lineValue.x1 <= pointValue.x) && (pointValue.x <= lineValue.x2)
        } else { // lineValue.x1 > lineValue.x2
            on_gate_x = (lineValue.x2 <= pointValue.x) && (pointValue.x <= lineValue.x1)
        }
        
        boolean on_gate_y = false
        if (lineValue.y2 > lineValue.y1) {
            on_gate_y = (lineValue.y1 <= pointValue.y) && (pointValue.y <= lineValue.y2)
        } else { // lineValue.y1 > lineValue.y2
            on_gate_y = (lineValue.y2 <= pointValue.y) && (pointValue.y <= lineValue.y1)
        }

        /*        
        if (s == "Track") {
            println "isPointOnLine $s, ${on_gate_x && on_gate_y}, x:$on_gate_x, y:$on_gate_y, Point: ${RouteGradStr2(pointValue.x)}, ${RouteGradStr2(pointValue.y)}, Line: ${RouteGradStr2(lineValue.x1)}, ${RouteGradStr2(lineValue.y1)}, ${RouteGradStr2(lineValue.x2)}, ${RouteGradStr2(lineValue.y2)},   $pointValue.y, $lineValue.y1, $lineValue.y2"
        }
        */
        
        return on_gate_x && on_gate_y
    }
    
    //--------------------------------------------------------------------------
    private static BigDecimal getPointDistance(BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2)
    {
        BigDecimal xd = x2 - x1
        BigDecimal yd = y2 - y1
        return Math.sqrt( xd * xd + yd * yd)
    }
     
 }
