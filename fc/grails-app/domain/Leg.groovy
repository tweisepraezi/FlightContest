import java.text.*

class Leg
{
	BigDecimal trueTrack    // Grad
	BigDecimal distance     // NM 
	
	BigDecimal trueHeading  // Grad
	BigDecimal groundSpeed  // NM
	BigDecimal legTime      // h

	boolean procedureTurn
	
	def messageSource
	
	String name()
	{
		GregorianCalendar time = new GregorianCalendar()
		time.set(Calendar.HOUR_OF_DAY, 0)
		time.set(Calendar.MINUTE, 0)
		time.set(Calendar.SECOND, (3600 * legTime).toInteger())
		String time_str = time.getTime().format("HH:mm:ss")
		
		return "${trueHeadingFormat()}${messageSource.getMessage('fc.grad', null, null)} ${groundSpeedFormat()}${messageSource.getMessage('fc.mile', null, null)} ${time_str}"
	}
	
	String trueTrackFormat()
	{
		DecimalFormat df = new DecimalFormat("000")
		return df.format(trueTrack)
	}
	
	String distanceFormat()
	{
		DecimalFormat df = new DecimalFormat("#0.00")
		return df.format(distance)
	}
	
	String trueHeadingFormat()
	{
		DecimalFormat df = new DecimalFormat("000")
		return df.format(trueHeading)
	}
	
	String groundSpeedFormat()
	{
		DecimalFormat df = new DecimalFormat("#0.0")
		return df.format(groundSpeed)
	}
	
	String legTimeFormat()
	{
		GregorianCalendar time = new GregorianCalendar()
		time.set(Calendar.HOUR_OF_DAY, 0)
		time.set(Calendar.MINUTE, 0)
		time.set(Calendar.SECOND, (3600 * legTime).toInteger())
		return time.getTime().format("HH:mm:ss")
	}
	
	Date addtime(Date initTime)
	{
		GregorianCalendar time = new GregorianCalendar() 
		time.setTime(initTime)
		
		time.add(Calendar.SECOND, (3600 * legTime).toInteger() )

		return time.getTime()
	}
	
}
