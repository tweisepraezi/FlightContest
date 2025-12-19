class DisabledCheckPointsTools
{
    
    final static String COMPRESS_IDENTIFIER = "C1:"
    
    static String Compress(String umcompressedValue, Route routeInstance)
    {
        String compressed_str = "${COMPRESS_IDENTIFIER}${routeInstance.id}:" 
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:"id"])) {
            boolean found = false
            for (String v in umcompressedValue.split(',')) {
                if (v == coordroute_instance.title()) {
                    found = true
                }
            }
            if (found) {
                compressed_str += "1"
            } else {
                compressed_str += "0"
            }
        }
        return compressed_str // Defs.ROUTE_MAX_POINTS
    }
    
    static String Compress2(String umcompressedValue, List gateTitles)
    {
        String compressed_str = "${COMPRESS_IDENTIFIER}0:" 
        for (String gate_title in gateTitles) {
            boolean found = false
            for (String v in umcompressedValue.split(',')) {
                if (v == gate_title) {
                    found = true
                }
            }
            if (found) {
                compressed_str += "1"
            } else {
                compressed_str += "0"
            }
        }
        return compressed_str
    }
    
    static String Uncompress(String compressedValue, Route routeInstance = null)
    {
        if (!compressedValue.startsWith(COMPRESS_IDENTIFIER)) {
            return compressedValue
        }
        
        String[] c = compressedValue.split(':')
        long route_id = c[1].toLong()
        if (!routeInstance) {
            routeInstance = Route.get(route_id)
        }
        
        String uncompressed_str = ""
        int i = 0
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:"id"])) {
            if (i < c[2].size()) {
                if (c[2].substring(i,i+1) == "1") {
                    uncompressed_str += "${coordroute_instance.title()},"
                }
            }
            i++
        }
        return uncompressed_str
    }
    
    static boolean Contains(String compressedValue, Route routeInstance, String titleValue, boolean retCorridorNoCheckCoord = true)
    {
        if (routeInstance.corridorWidth) {
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:"id"])) {
                if (coordroute_instance.type.IsCorridorNoCheckCoord()) {
                    if (titleValue == "${coordroute_instance.title()},") {
                        return retCorridorNoCheckCoord
                    }
                }
            }
        } else {
            if (Uncompress(compressedValue, routeInstance).contains(titleValue)) {
                return true
            }
        }
        return false
    }
}
