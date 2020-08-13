import java.math.BigDecimal;

class FlightTestWind
{
	Wind wind
    
    BigDecimal TODirection = 0.0                           // DB-2.12, Richtung der Startbahn, Grad
    BigDecimal TOOffset = 0.0                              // DB-2.12, Positionsabweichung längs zur Startbahn, NM
    BigDecimal TOOrthogonalOffset = 0.0                    // DB-2.12, Positionsabweichung quer zur Startbahn, NM
    
    BigDecimal LDGDirection = 0.0                          // DB-2.12, Richtung der Startbahn, Grad
    BigDecimal LDGOffset = 0.0                             // DB-2.12, Positionsabweichung längs zur Startbahn, NM
    BigDecimal LDGOrthogonalOffset = 0.0                   // DB-2.12, Positionsabweichung quer zur Startbahn, NM

    BigDecimal iTOiLDGDirection = 0.0                      // DB-2.12, Richtung der Startbahn, Grad
    BigDecimal iTOiLDGOffset = 0.0                         // DB-2.12, Positionsabweichung längs zur Startbahn, NM
    BigDecimal iTOiLDGOrthogonalOffset = 0.0               // DB-2.12, Positionsabweichung quer zur Startbahn, NM

    Integer idTitle = 0                                    // DB-2.12
	
	// transient Wind values
	static transients = ['direction','speed']
	BigDecimal direction = 0.0
	BigDecimal speed = 0.0
	
	static belongsTo = [flighttest:FlightTest]

	static constraints = {
		direction(range:0..360,scale:10)
		speed(range:0.0f..<1000.0f,scale:10)

        // DB-2.12 compatibility
        TODirection(nullable:true,range:0..360,scale:10)
        TOOffset(nullable:true,range:-2..2,scale:3)
        TOOrthogonalOffset(nullable:true,range:-1..1,scale:3)
        LDGDirection(nullable:true,range:0..360,scale:10)
        LDGOffset(nullable:true,range:-2..2,scale:3)
        LDGOrthogonalOffset(nullable:true,range:-1..1,scale:3)
        iTOiLDGDirection(nullable:true,range:0..360,scale:10)
        iTOiLDGOffset(nullable:true,range:-2..2,scale:3)
        iTOiLDGOrthogonalOffset(nullable:true,range:-1..1,scale:3)
        idTitle(nullable:true)
	}
	
	String name()
	{
		return "${wind.name()} (${idTitle})"
	}
    
    boolean Used()
    {
        Test test_instance1 = Test.findByTask(flighttest.task,[sort:"id"])
        if (!test_instance1.crew.disabled && !test_instance1.disabledCrew && (test_instance1.flighttestwind == this) && test_instance1.timeCalculated) {
            return true
        }
        for (Test test_instance in Test.findAllByTask(flighttest.task,[sort:"id"])) {
            if (!test_instance.crew.disabled && !test_instance.disabledCrew && (test_instance.flighttestwind == this) && test_instance.timeCalculated) {
                return true
            }
        }
        return false
    }
}
