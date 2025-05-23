import java.math.BigDecimal;

class FlightTestWind
{
	Wind wind
    
    BigDecimal TODirection = 0.0                           // DB-2.12, Richtung der Startbahn, Grad
    BigDecimal TOOffset = 0.0                              // DB-2.12, Positionsabweichung l�ngs zur Startbahn, NM
    BigDecimal TOOrthogonalOffset = 0.0                    // DB-2.12, Positionsabweichung quer zur Startbahn, NM
    
    BigDecimal LDGDirection = 0.0                          // DB-2.12, Richtung der Startbahn, Grad
    BigDecimal LDGOffset = -0.03                           // DB-2.12, Positionsabweichung l�ngs zur Startbahn, NM
    BigDecimal LDGOrthogonalOffset = 0.0                   // DB-2.12, Positionsabweichung quer zur Startbahn, NM

    BigDecimal iTOiLDGDirection = 0.0                      // DB-2.12, Richtung der Startbahn, Grad
    BigDecimal iTOiLDGOffset = 0.0                         // DB-2.12, Positionsabweichung l�ngs zur Startbahn, NM
    BigDecimal iTOiLDGOrthogonalOffset = 0.0               // DB-2.12, Positionsabweichung quer zur Startbahn, NM

	String TODurationFormula = "wind+:3NM"                 // DB-2.39
	String LDGDurationFormula = "wind+:6NM"                // DB-2.39
	String iLDGDurationFormula = "wind+:2NM"               // DB-2.39
	String iTODurationFormula = "wind+:3NM"                // DB-2.39
    
    BigDecimal corridorWidthWind = 0.0                     // DB-2.43, NM

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
        
        // DB-2.39 compatibility
        TODurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
        LDGDurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
        iLDGDurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
        iTODurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
        
        // DB-2.43 compatibility
        corridorWidthWind(nullable:true)
	}
	
	String name()
	{
        if (corridorWidthWind) {
            return "${wind.name()} (${idTitle}) [${FcMath.DistanceStr(corridorWidthWind)}${getMsgArgs('fc.mile',[])}]"
        }
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
    
	static boolean DurationValid(val, obj)
	{
		if (val) {
			if (val.startsWith('time+:')) {
				if (val.endsWith('min')) {
					String f = val.substring(6,val.size()-3).replace(',','.')
					if (f.isInteger()) {
						return true
					}
				}
			} else if (val.startsWith('time:')) {
				if (val.endsWith('min')) {
					String f = val.substring(5,val.size()-3).replace(',','.')
					if (f.isInteger()) {
						return true
					}
				}
			} else if (val.startsWith('wind+:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(6,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(6,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('wind:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(5,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(5,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('nowind+:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(8,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(8,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('nowind:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(7,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(7,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('func+:')) {
				String f = val.substring(6,val.size())
				if (f) {
					return true
				}
			} else if (val.startsWith('func:')) {
				String f = val.substring(5,val.size())
				if (f) {
					return true
				}
			}
		}
		return false
	}
}
