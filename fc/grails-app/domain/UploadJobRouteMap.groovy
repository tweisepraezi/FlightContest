class UploadJobRouteMap // DB-2.22
{
	UploadJobStatus uploadJobStatus
    Integer uploadJobMapEdition
    Boolean uploadJobNoRoute
    Boolean uploadJobSecondOptions
	String uploadJobLink = ""
	
	static belongsTo = [route:Route]

	static constraints = {
	}

}
