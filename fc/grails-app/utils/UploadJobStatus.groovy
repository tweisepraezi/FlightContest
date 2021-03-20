enum UploadJobStatus
{
	None,
	Waiting,
	Sending,
	Done,
	Error
	
	boolean InWork()
	{
		switch (this) {
			case Waiting:
			case Sending:
				return true
		}
		return false
	}
}