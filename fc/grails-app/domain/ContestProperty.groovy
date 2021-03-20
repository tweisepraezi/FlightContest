class ContestProperty // DB-2.21
{
	String key
	String value

	static belongsTo = [contest:Contest]

	static constraints = {
		key(blank:false)
		value()
		contest(nullable:false)
	}

	void CopyValues(ContestProperty contestPropertyInstance)
	{
		key = contestPropertyInstance.key
		value = contestPropertyInstance.value
	
		if (!this.save()) {
			throw new Exception("ContestProperty.CopyValues could not save")
		}
	}
}
