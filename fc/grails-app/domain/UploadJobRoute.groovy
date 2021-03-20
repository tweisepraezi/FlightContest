class UploadJobRoute // DB-2.21
{
	UploadJobStatus uploadJobStatus
	String uploadJobLink = ""
	
	static belongsTo = [route:Route]

	static constraints = {
	}

}
