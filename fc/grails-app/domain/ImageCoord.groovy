class ImageCoord // DB-2.28
{
	byte[] imageData = null
    
	static belongsTo = [coord:Coord]

	static constraints = {
		imageData(nullable:true)
	}

	static mapping = {
		imageData sqlType: "varbinary(max)"
    }
}
