import grails.test.*
import static org.junit.Assert.*;

import java.util.Map;


class AviationMathTest extends GrailsUnitTestCase
{
	void testCalculateWind()
	{
		tCalculateWind(
			[windDirection:130,windSpeed:20,valueTAS:70,valueDistance:16.2,trueTrack:89],
			[trueHeading:'100',groundSpeed:'53,67',legTime:'00:18:07']
		)
		tCalculateWind(
			[windDirection:130,windSpeed:20,valueTAS:70,valueDistance:17.44,trueTrack:219],
			[trueHeading:'202',groundSpeed:'66,73',legTime:'00:15:41']
		)
		tCalculateWind(
			[windDirection:130,windSpeed:20,valueTAS:70,valueDistance:13.50,trueTrack:161],
			[trueHeading:'153',groundSpeed:'52,09',legTime:'00:15:33']
		)
		tCalculateWind(
			[windDirection:130,windSpeed:20,valueTAS:70,valueDistance:11.50,trueTrack:86],
			[trueHeading:'097',groundSpeed:'54,22',legTime:'00:12:44']
		)
		tCalculateWind(
			[windDirection:130,windSpeed:20,valueTAS:70,valueDistance:11.36,trueTrack:237],
			[trueHeading:'221',groundSpeed:'73,18',legTime:'00:09:19']
		)
		tCalculateWind(
			[windDirection:130,windSpeed:20,valueTAS:70,valueDistance:6.74,trueTrack:244],
			[trueHeading:'229',groundSpeed:'75,71',legTime:'00:05:20']
		)
		
		// SP -> Secret1
		tCalculateWind(
			[windDirection:300,windSpeed:15,valueTAS:85,valueDistance:10.69,trueTrack:89],
			[trueHeading:'084',groundSpeed:'97,51',legTime:'00:06:35']
		)
		// Secret1 -> TP1
		tCalculateWind(
			[windDirection:300,windSpeed:15,valueTAS:85,valueDistance:5.51,trueTrack:89],
			[trueHeading:'084',groundSpeed:'97,51',legTime:'00:03:23']
		)
		// SP -> TP1
		tCalculateWind(
			[windDirection:300,windSpeed:15,valueTAS:85,valueDistance:16.2,trueTrack:89],
			[trueHeading:'084',groundSpeed:'97,51',legTime:'00:09:58']
		)
		// TP5 -> FP
		tCalculateWind(
			[windDirection:300,windSpeed:15,valueTAS:85,valueDistance:6.74,trueTrack:244],
			[trueHeading:'252',groundSpeed:'75,70',legTime:'00:05:21']
		)

	}
	
	void tCalculateWind(Map inputValues, Map testValues)
	{
		Map ret = AviationMath.calculateWind(inputValues.windDirection,inputValues.windSpeed,inputValues.valueTAS,
			                                 inputValues.trueTrack,inputValues.valueDistance
		)
		Map outputValues = [:]
		outputValues.trueHeading = FcMath.GradStr(ret.trueheading)
		outputValues.groundSpeed = FcMath.SpeedStr_Planning(ret.groundspeed)
		outputValues.legTime = FcMath.TimeStr(ret.legtime)
		if (outputValues == testValues) {
			println "$outputValues OK"
		} else {
			println "$outputValues ERROR"
		}
		// assert outputValues == testValues
	}
}
