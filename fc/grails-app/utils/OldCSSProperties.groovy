
class OldCSSProperties {

    // --------------------------------------------------------------------------------------------------------------------
    static String DeleteAllProperties(Contest contestInstance)
    {
        String print_style = ""
        contestInstance.printStyle.eachLine {
            if (it.contains("--route") ||
                it.contains("--disable-procedureturn") || 
                it.contains("--show-curved-points") ||
                it.contains("--class") ||
                it.contains("--secret-gatewidth") ||
                it.contains("--before-starttime") ||
                it.contains("--add-submission") ||
                it.contains('--flightplan') ||
                it.contains("--start-tp") ||
                it.contains("--add-num") ||
                it.contains("--submission") ||
                it.contains('--flightresults') ||
                it.contains("--landingresults")
               ) {
            } else {
                if (print_style) {
                    print_style += "\n"
                }
                print_style += it
            }
        }
        return print_style
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static boolean UseProcedureTurn(Route routeInstance)
    {
        boolean use_pt = true
        routeInstance.contest.printStyle.eachLine {
            if (it.contains("--route") && it.contains("--disable-procedureturn")) {
                String s = it.substring(it.indexOf("--route")+7).trim()
                if (s.startsWith(":")) {
                    s = s.substring(1).trim()
                    int i = s.indexOf(";")
                    if (i) {
                        s = s.substring(0,i).trim()
                        if (s == routeInstance.name()) {
                            use_pt = false
                        }
                    }
                }
            }
        }
        return use_pt
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static boolean ShowCurvedPoints(Route routeInstance)
    {
        boolean show_curved_points = false
        routeInstance.contest.printStyle.eachLine {
            if (it.contains("--route") && it.contains("--show-curved-points")) {
                String s = it.substring(it.indexOf("--route")+7).trim()
                if (s.startsWith(":")) {
                    s = s.substring(1).trim()
                    int i = s.indexOf(";")
                    if (i) {
                        s = s.substring(0,i).trim()
                        if (s == routeInstance.name()) {
                            show_curved_points = true
                        }
                    }
                }
            }
        }
        return show_curved_points
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static Float GetSecretGateWidth(ResultClass resultClassInstance)
    {
        Float ret = null
        resultClassInstance.contest.printStyle.eachLine {
            if (it.contains("--class") && it.contains("--secret-gatewidth")) {
                String s = it.substring(it.indexOf("--class")+7).trim()
                if (s.startsWith(":")) {
                    s = s.substring(1).trim()
                    int i = s.indexOf(";")
                    if (i) {
                        s = s.substring(0,i).trim()
                        if (s == resultClassInstance.name) {
                            s = it.substring(it.indexOf("--secret-gatewidth")+18).trim()
                            if (s.startsWith(":")) {
                                s = s.substring(1).trim()
                                i = s.indexOf(";")
                                if (i) {
                                    s = s.substring(0,i).trim()
                                    if (s.isNumber()) {
                                        ret = s.toFloat()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ret
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static Integer GetMinutesBeforeStartTime(ResultClass resultClassInstance)
    {
        Integer ret = null
        resultClassInstance.contest.printStyle.eachLine {
            if (it.contains("--class") && it.contains("--before-starttime")) {
                String s = it.substring(it.indexOf("--class")+7).trim()
                if (s.startsWith(":")) {
                    s = s.substring(1).trim()
                    int i = s.indexOf(";")
                    if (i) {
                        s = s.substring(0,i).trim()
                        if (s == resultClassInstance.name) {
                            s = it.substring(it.indexOf("--before-starttime")+18).trim()
                            if (s.startsWith(":")) {
                                s = s.substring(1).trim()
                                i = s.indexOf(";")
                                if (i) {
                                    s = s.substring(0,i).trim()
                                    if (s.isInteger()) {
                                        ret = s.toInteger()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ret
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static Integer GetMinutesAddSubmission(ResultClass resultClassInstance)
    {
        Integer ret = null
        resultClassInstance.contest.printStyle.eachLine {
            if (it.contains("--class") && it.contains("--add-submission")) {
                String s = it.substring(it.indexOf("--class")+7).trim()
                if (s.startsWith(":")) {
                    s = s.substring(1).trim()
                    int i = s.indexOf(";")
                    if (i) {
                        s = s.substring(0,i).trim()
                        if (s == resultClassInstance.name) {
                            s = it.substring(it.indexOf("--add-submission")+16).trim()
                            if (s.startsWith(":")) {
                                s = s.substring(1).trim()
                                i = s.indexOf(";")
                                if (i) {
                                    s = s.substring(0,i).trim()
                                    if (s.isInteger()) {
                                        ret = s.toInteger()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ret
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static Map GetTestLegStyle(Contest contestInstance)
    {
        Map ret = [showLegDistance:true, showTrueTrack:true, showTrueHeading:true, showGroundSpeed:true, showLocalTime:true, showElapsedTime:false]
        if (contestInstance.printStyle.contains('--flightplan')) {
            if (contestInstance.printStyle.contains('hide-distance')) {
                ret.showLegDistance = false
            }
            if (contestInstance.printStyle.contains('hide-truetrack')) {
                ret.showTrueTrack = false
            }
            if (contestInstance.printStyle.contains('hide-trueheading')) {
                ret.showTrueHeading = false
            }
            if (contestInstance.printStyle.contains('hide-groundspeed')) {
                ret.showGroundSpeed = false
            }
            if (contestInstance.printStyle.contains('disable-local-time')) {
                ret.showLocalTime = false
            }
            if (contestInstance.printStyle.contains('show-elapsed-time')) {
                ret.showElapsedTime = true
            }
	    }
        return ret
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static String GetAddTPNum(Route routeInstance)
    {
        String tp_values = ""
        String addnum_values = ""
        routeInstance.contest.printStyle.eachLine {
            if (it.contains("--route") && it.contains("--start-tp") && it.contains("--add-num")) {
                String s = it.substring(it.indexOf("--route")+7).trim()
                if (s.startsWith(":")) {
                    s = s.substring(1).trim()
                    int i = s.indexOf(";")
                    if (i) {
                        s = s.substring(0,i).trim()
                        if (s == routeInstance.name()) {
                            String s2 = it.substring(it.indexOf("--start-tp")+10).trim()
                            if (s2.startsWith(":")) {
                                s2 = s2.substring(1).trim()
                                int j = s2.indexOf(";")
                                if (j) {
                                    for (String s21 in s2.substring(0,j).trim().split(',')) {
                                        if (tp_values) {
                                            tp_values += ","
                                        }
                                        tp_values += s21.trim().toUpperCase()
                                    }
                                    String s3 = it.substring(it.indexOf("--add-num")+9).trim()
                                    if (s3.startsWith(":")) {
                                        s3 = s3.substring(1).trim()
                                        int k = s3.indexOf(";")
                                        if (k) {
                                            for (String s31 in s3.substring(0,k).trim().split(',')) {
                                                if (s31.isInteger()) {
                                                    if (addnum_values) {
                                                        addnum_values += ","
                                                    }
                                                    addnum_values += s31.toInteger()
                                                }
                                            }
                                        }
                                    }
    
                                }
                            }
                            
                        }
                    }
                }
            }
        }
        if (tp_values && addnum_values) {
            return "${tp_values}:${addnum_values}"
        }
        return ""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static Integer GetSubmissionMinutes(Contest contestInstance)
    {
        int add_minutes = 0
        contestInstance.printStyle.eachLine {
            if (it.contains("--submission")) {
                String s = it.substring(it.indexOf("--submission")+12).trim()
                if (s.startsWith(":")) {
                    s = s.substring(1).trim()
                    int i = s.indexOf(";")
                    if (i) {
                        s = s.substring(0,i).trim()
                        if (s.isInteger()) {
                            add_minutes = s.toInteger()
                        }
                    }
                }
            }
        }
        return add_minutes
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static Boolean ShowFlightResultsCurvedPoints(Contest contestInstance)
    {
        boolean show_curved_points = false
        contestInstance.printStyle.eachLine {
            if (it.contains('--flightresults') && it.contains('show-curved-points')) {
                show_curved_points = true
            }
        }
        return show_curved_points
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static BigDecimal GetLandingResultsFactor(Contest contestInstance)
    {
        BigDecimal landingresults_factor = null
        contestInstance.printStyle.eachLine {
            if (it.contains("--landingresults")) {
                String s = it.substring(it.indexOf("--landingresults")+16).trim()
                if (s.startsWith(":")) {
                    s = s.substring(1).trim()
                    int i = s.indexOf(";")
                    if (i) {
                        s = s.substring(0,i).trim()
                        if (s.isBigDecimal()) {
                            BigDecimal f = s.toBigDecimal()
                            if ((f >= 0.0f) && (f <= 1.0f)) {
                                landingresults_factor = f
                            }
                        }
                    }
                }
            }
        }
        return landingresults_factor
    }
}