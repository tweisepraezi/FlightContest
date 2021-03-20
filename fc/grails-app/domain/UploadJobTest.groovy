class UploadJobTest // DB-2.21
{
	UploadJobStatus uploadJobStatus
	String uploadJobLink = ""
	
	static belongsTo = [test:Test]

	static constraints = {
	}

}
