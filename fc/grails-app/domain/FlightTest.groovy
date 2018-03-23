import java.math.BigDecimal;

class FlightTest 
{
	String title
	Route route

	// transient Wind values
	static transients = ['direction','speed','TODirection','LDGDirection','iTOiLDGDirection']
	BigDecimal direction = 0.0
	BigDecimal speed = 0.0
    BigDecimal TODirection = 0.0
    BigDecimal LDGDirection = 0.0
    BigDecimal iTOiLDGDirection = 0.0

	static belongsTo = [task:Task]

	static hasMany = [flighttestwinds:FlightTestWind]
	
	static constraints = {
		title()
		route(nullable:true)
        direction(range:0.0f..<360.0f,scale:10)
        speed(range:0.0f..<1000.0f,scale:10)
        TODirection(range:0..<360,scale:10)
        LDGDirection(range:0..<360,scale:10)
        iTOiLDGDirection(range:0..<360,scale:10)
		task(nullable:false)
	}

	static mapping = {
		flighttestwinds sort:"id"
	}

    String idName()
    {
		return "${getMsg('fc.flighttest')}-${task.idTitle}"
    }
    
	String name()
	{
		if(title) {
			return title
		} else {
            return idName()
		}
	}
    
    boolean IsObservationSignUsed()
    {
        Test test_instance1 = Test.findByTask(task)
        if (test_instance1) {
            if (test_instance1.IsObservationSignUsed()) {
                return true
            }
            for (Test test_instance in Test.findAllByTask(task)) {
                if (test_instance.IsObservationSignUsed()) {
                    return true
                }
            }
        }
        return false
    }
    
    boolean CanObservationsAdd()
    {
        if (route.IsObservationSignOk()) {
            if (!IsObservationSignUsed()) {
                if (!Test.findByTaskAndObservationTestComplete(task,true)) {
                    return true
                }
            }
        }
        return false
    }
    
    boolean CanObservationsRemove()
    {
        if (IsObservationSignUsed()) {
            if (!Test.findByTaskAndObservationTestComplete(task,true)) {
                return true
            }
        }
        return false
    }
}
