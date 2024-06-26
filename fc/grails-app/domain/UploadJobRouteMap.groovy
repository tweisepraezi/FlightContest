class UploadJobRouteMap // DB-2.22
{
	UploadJobStatus uploadJobStatus
    Integer uploadJobMapEdition
    Boolean uploadJobNoRoute
    Boolean uploadJobAllRouteDetails         // DB-2.33
    Boolean uploadJobSecondOptions = false   // UNUSED, since DB-2.29
	Integer uploadJobOptionNumber = 0        // DB-2.29
    String uploadJobOptionTitle = ""         // DB-2.29
	String uploadJobLink = ""
	
	static belongsTo = [route:Route]

	static constraints = {
		// DB-2.29 compatibility
		uploadJobOptionNumber(nullable:true)
        uploadJobOptionTitle(nullable:true)
        
        // DB-2.33 compatibility
        uploadJobAllRouteDetails(nullable:true)
	}

    String GetTitle() {
        String show_route = "o"
        if (uploadJobNoRoute) {
            show_route = "x"
        } else if (uploadJobAllRouteDetails) {
            show_route = "+"
        }
        if (uploadJobOptionTitle) {
            return "${uploadJobOptionTitle} ${show_route} (${uploadJobMapEdition})"
        }
        if (uploadJobOptionNumber) {
            return "${uploadJobOptionNumber} ${show_route} (${uploadJobMapEdition})"
        }
        return "${show_route} ${uploadJobMapEdition}"
    }
}
