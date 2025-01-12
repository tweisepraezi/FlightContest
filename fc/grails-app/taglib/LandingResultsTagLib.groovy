class LandingResultsTagLib 
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest1Complete = { attrs, body ->
		if (attrs.crewResults) {
			outln"""<tr>"""
			outln"""    <th colspan="4">${message(code:'fc.landingtest.landing1')}${if (attrs.t.IsPrecisionLanding()) " ("+message(code:attrs.t.GetPrecisionFlyingLandingText(attrs.t.task.landingTest1Points))+")" else ""}</th>"""
			outln"""</tr>"""
		} else {
        	outln"""<table>"""
			outln"""    <tbody>"""
		}
        outln"""        <tr>"""
        if (attrs.t.landingTest1Landing == 2) {
        	outln"""        <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>"""
        	outln"""        <td>${attrs.t.GetLandingTestNoLandingPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}</td>"""
        } else if (attrs.t.landingTest1Landing == 3) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestOutsideLandingPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}</td>"""
        } else if (!attrs.t.landingTest1Measure) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>"""
            outln"""        <td>- (${attrs.t.landingTest1MeasurePenalties} ${message(code:'fc.points')})</td>"""
        } else {
            outln"""        <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>"""
            outln"""        <td>${attrs.t.landingTest1Measure} (${attrs.t.landingTest1MeasurePenalties} ${message(code:'fc.points')})</td>"""
        }
        outln"""        </tr>"""
        if (attrs.t.landingTest1RollingOutside) {
            outln"""    <tr>"""
			outln"""        <td class="detailtitle">${message(code:'fc.landingtest.rollingoutside')}:</td>"""
			outln"""        <td>${attrs.t.GetLandingTestRollingOutsidePoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}</td>"""
			outln"""    </tr>"""
        }
        if (attrs.t.landingTest1PowerInBox) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestPowerInBoxPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest1GoAroundWithoutTouching) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestGoAroundWithoutTouchingPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest1GoAroundInsteadStop) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestGoAroundInsteadStopPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest1AbnormalLanding) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestAbnormalLandingPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest1NotAllowedAerodynamicAuxiliaries) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest1PowerInAir) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinair')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestPowerInAirPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest1FlapsInAir) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.flapsinair')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestFlapsInAirPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest1TouchingObstacle) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.touchingobstacle')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestTouchingObstaclePoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
		if (attrs.crewResults) {
			outln"""    <tr>"""
			outln"""        <td class="detailtitle">${message(code:'fc.test.results.summary')}:</td>"""
			outln"""        <td class="subpoints">${attrs.t.landingTest1Penalties} ${message(code:'fc.points')}</td>"""
			outln"""    </tr>"""
       		outln"""	<tr>"""
       		outln"""		<tf colspan="4"></tf>"""
       		outln"""	</tr>"""
		} else {
			outln"""    </tbody>"""
			outln"""</table>"""
		}
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest1Printable = { attrs, body ->
        outln"""<table class="landingresultlist">"""
        outln"""	<thead>"""
        outln"""        <tr class="name" id="1">"""
        outln"""       		<td>${message(code:'fc.landingtest.landing1')}${if (attrs.t.IsPrecisionLanding()) " ("+message(code:attrs.t.GetPrecisionFlyingLandingText(attrs.t.task.landingTest1Points))+")" else ""}</td>"""
        outln"""        </tr>"""
        outln"""	</thead>"""
        outln"""    <tbody>"""
        outln"""        <tr class="values" id="1">"""
        outln"""        	<td>"""
        if (attrs.t.landingTest1Landing == 2) {
            outln"""   		    ${message(code:'fc.landingtest.nolanding')}: ${attrs.t.GetLandingTestNoLandingPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}<br/>"""
        } else if (attrs.t.landingTest1Landing == 3) {
            outln"""         	${message(code:'fc.landingtest.outsidelanding')}: ${attrs.t.GetLandingTestOutsideLandingPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}<br/>"""
        } else if (!attrs.t.landingTest1Measure) {
            outln"""       		${message(code:'fc.landingresults.measure')}: - (${attrs.t.landingTest1MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        } else {
            outln"""       		${message(code:'fc.landingresults.measure')}: ${attrs.t.landingTest1Measure} (${attrs.t.landingTest1MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        }
        if (attrs.t.landingTest1RollingOutside) {
			outln"""            ${message(code:'fc.landingtest.rollingoutside')}: ${attrs.t.GetLandingTestRollingOutsidePoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest1PowerInBox) {
			outln"""            ${message(code:'fc.landingtest.powerinbox')}: ${attrs.t.GetLandingTestPowerInBoxPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest1GoAroundWithoutTouching) {
			outln"""            ${message(code:'fc.landingtest.goaroundwithouttouching')}: ${attrs.t.GetLandingTestGoAroundWithoutTouchingPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest1GoAroundInsteadStop) {
			outln"""            ${message(code:'fc.landingtest.goaroundinsteadstop')}: ${attrs.t.GetLandingTestGoAroundInsteadStopPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest1AbnormalLanding) {
			outln"""            ${message(code:'fc.landingtest.abnormallanding')}: ${attrs.t.GetLandingTestAbnormalLandingPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest1NotAllowedAerodynamicAuxiliaries) {
			outln"""            ${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}: ${attrs.t.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest1PowerInAir) {
            outln"""            ${message(code:'fc.landingtest.powerinair')}: ${attrs.t.GetLandingTestPowerInAirPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}<br/>"""
        }
        if (attrs.t.landingTest1FlapsInAir) {
            outln"""            ${message(code:'fc.landingtest.flapsinair')}: ${attrs.t.GetLandingTestFlapsInAirPoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}<br/>"""
        }
        if (attrs.t.landingTest1TouchingObstacle) {
            outln"""            ${message(code:'fc.landingtest.touchingobstacle')}: ${attrs.t.GetLandingTestTouchingObstaclePoints(attrs.t.task.landingTest1Points)} ${message(code:'fc.points')}<br/>"""
        }
        outln"""            </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr class="summary" id="1">"""
        outln"""            <td>${message(code:'fc.test.results.summary')}: ${attrs.t.landingTest1Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest2Complete = { attrs, body ->
		if (attrs.crewResults) {
			outln"""<tr>"""
			outln"""    <th colspan="4">${message(code:'fc.landingtest.landing2')}${if (attrs.t.IsPrecisionLanding()) " ("+message(code:attrs.t.GetPrecisionFlyingLandingText(attrs.t.task.landingTest2Points))+")" else ""}</th>"""
			outln"""</tr>"""
		} else {
	        outln"""<table>"""
	        outln"""    <tbody>"""
		}
        outln"""        <tr>"""
        if (attrs.t.landingTest2Landing == 2) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestNoLandingPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}</td>"""
        } else if (attrs.t.landingTest2Landing == 3) {
		    outln"""        <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestOutsideLandingPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}</td>"""
        } else if (!attrs.t.landingTest2Measure) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>"""
            outln"""        <td>- (${attrs.t.landingTest2MeasurePenalties} ${message(code:'fc.points')})</td>"""
        } else {
            outln"""        <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>"""
            outln"""        <td>${attrs.t.landingTest2Measure} (${attrs.t.landingTest2MeasurePenalties} ${message(code:'fc.points')})</td>"""
        }
        outln"""        </tr>"""
        if (attrs.t.landingTest2RollingOutside) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.rollingoutside')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestRollingOutsidePoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2PowerInBox) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestPowerInBoxPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2GoAroundWithoutTouching) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestGoAroundWithoutTouchingPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2GoAroundInsteadStop) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestGoAroundInsteadStopPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2AbnormalLanding) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestAbnormalLandingPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2NotAllowedAerodynamicAuxiliaries) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2PowerInAir) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinair')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestPowerInAirPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2FlapsInAir) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.flapsinair')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestFlapsInAirPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2TouchingObstacle) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.touchingobstacle')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestTouchingObstaclePoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
		if (attrs.crewResults) {
			outln"""    <tr>"""
			outln"""        <td class="detailtitle">${message(code:'fc.test.results.summary')}:</td>"""
			outln"""        <td class="subpoints">${attrs.t.landingTest2Penalties} ${message(code:'fc.points')}</td>"""
			outln"""    </tr>"""
       		outln"""	<tr>"""
       		outln"""		<tf colspan="4"></tf>"""
       		outln"""	</tr>"""
		} else {
        	outln"""    </tbody>"""
			outln"""</table>"""
		}
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest2Printable = { attrs, body ->
        outln"""<table class="landingresultlist">"""
        outln"""	<thead>"""
        outln"""        <tr class="name" id="2">"""
        outln"""       		<td>${message(code:'fc.landingtest.landing2')}${if (attrs.t.IsPrecisionLanding()) " ("+message(code:attrs.t.GetPrecisionFlyingLandingText(attrs.t.task.landingTest2Points))+")" else ""}</td>"""
        outln"""        </tr>"""
        outln"""	</thead>"""
        outln"""    <tbody>"""
        outln"""        <tr class="values" id="2">"""
        outln"""        	<td>"""
        if (attrs.t.landingTest2Landing == 2) {
            outln"""       		${message(code:'fc.landingtest.nolanding')}: ${attrs.t.GetLandingTestNoLandingPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}<br/>"""
        } else if (attrs.t.landingTest2Landing == 3) {
            outln"""       		${message(code:'fc.landingtest.outsidelanding')}: ${attrs.t.GetLandingTestOutsideLandingPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}<br/>"""
        } else if (!attrs.t.landingTest2Measure) {
            outln"""       		${message(code:'fc.landingresults.measure')}: - (${attrs.t.landingTest2MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        } else {
            outln"""       		${message(code:'fc.landingresults.measure')}: ${attrs.t.landingTest2Measure} (${attrs.t.landingTest2MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        }
        if (attrs.t.landingTest2RollingOutside) {
			outln"""            ${message(code:'fc.landingtest.rollingoutside')}: ${attrs.t.GetLandingTestRollingOutsidePoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest2PowerInBox) {
			outln"""            ${message(code:'fc.landingtest.powerinbox')}: ${attrs.t.GetLandingTestPowerInBoxPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest2GoAroundWithoutTouching) {
			outln"""            ${message(code:'fc.landingtest.goaroundwithouttouching')}: ${attrs.t.GetLandingTestGoAroundWithoutTouchingPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest2GoAroundInsteadStop) {
			outln"""            ${message(code:'fc.landingtest.goaroundinsteadstop')}: ${attrs.t.GetLandingTestGoAroundInsteadStopPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest2AbnormalLanding) {
			outln"""            ${message(code:'fc.landingtest.abnormallanding')}: ${attrs.t.GetLandingTestAbnormalLandingPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest2NotAllowedAerodynamicAuxiliaries) {
			outln"""            ${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}: ${attrs.t.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest2PowerInAir) {
			outln"""            ${message(code:'fc.landingtest.powerinair')}: ${attrs.t.GetLandingTestPowerInAirPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest2FlapsInAir) {
            outln"""            ${message(code:'fc.landingtest.flapsinair')}: ${attrs.t.GetLandingTestFlapsInAirPoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}<br/>"""
        }
        if (attrs.t.landingTest2TouchingObstacle) {
            outln"""            ${message(code:'fc.landingtest.touchingobstacle')}: ${attrs.t.GetLandingTestTouchingObstaclePoints(attrs.t.task.landingTest2Points)} ${message(code:'fc.points')}<br/>"""
        }
        outln"""            </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr class="summary" id="2">"""
        outln"""            <td>${message(code:'fc.test.results.summary')}: ${attrs.t.landingTest2Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest3Complete = { attrs, body ->
		if (attrs.crewResults) {
			outln"""<tr>"""
			outln"""    <th colspan="4">${message(code:'fc.landingtest.landing3')}${if (attrs.t.IsPrecisionLanding()) " ("+message(code:attrs.t.GetPrecisionFlyingLandingText(attrs.t.task.landingTest3Points))+")" else ""}</th>"""
			outln"""</tr>"""
		} else {
			outln"""<table>"""
			outln"""    <tbody>"""
		}
        outln"""        <tr>"""
        if (attrs.t.landingTest3Landing == 2) {
        	outln"""        <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestNoLandingPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}</td>"""
        } else if (attrs.t.landingTest3Landing == 3) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestOutsideLandingPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}</td>"""
        } else if (!attrs.t.landingTest3Measure) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>"""
            outln"""        <td>- (${attrs.t.landingTest3MeasurePenalties} ${message(code:'fc.points')})</td>"""
        } else {
            outln"""        <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>"""
            outln"""        <td>${attrs.t.landingTest3Measure} (${attrs.t.landingTest3MeasurePenalties} ${message(code:'fc.points')})</td>"""
        }
        outln"""        </tr>"""
        if (attrs.t.landingTest3RollingOutside) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.rollingoutside')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestRollingOutsidePoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3PowerInBox) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestPowerInBoxPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3GoAroundWithoutTouching) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestGoAroundWithoutTouchingPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3GoAroundInsteadStop) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestGoAroundInsteadStopPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3AbnormalLanding) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestAbnormalLandingPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3NotAllowedAerodynamicAuxiliaries) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3PowerInAir) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinair')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestPowerInAirPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3FlapsInAir) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.flapsinair')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestFlapsInAirPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3TouchingObstacle) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.touchingobstacle')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestTouchingObstaclePoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
		if (attrs.crewResults) {
			outln"""    <tr>"""
			outln"""        <td class="detailtitle">${message(code:'fc.test.results.summary')}:</td>"""
			outln"""        <td class="subpoints">${attrs.t.landingTest3Penalties} ${message(code:'fc.points')}</td>"""
			outln"""    </tr>"""
			outln"""	<tr>"""
       		outln"""		<tf colspan="4"></tf>"""
			outln"""	</tr>"""

		} else {
        	outln"""    </tbody>"""
			outln"""</table>"""
		}
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest3Printable = { attrs, body ->
        outln"""<table class="landingresultlist">"""
        outln"""	<thead>"""
        outln"""        <tr class="name" id="3">"""
        outln"""       		<td>${message(code:'fc.landingtest.landing3')}${if (attrs.t.IsPrecisionLanding()) " ("+message(code:attrs.t.GetPrecisionFlyingLandingText(attrs.t.task.landingTest3Points))+")" else ""}</td>"""
        outln"""        </tr>"""
        outln"""	</thead>"""
        outln"""    <tbody>"""
        outln"""        <tr class="values" id="3">"""
        outln"""        	<td>"""
        if (attrs.t.landingTest3Landing == 2) {
            outln"""       		${message(code:'fc.landingtest.nolanding')}: ${attrs.t.GetLandingTestNoLandingPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}<br/>"""
        } else if (attrs.t.landingTest3Landing == 3) {
            outln"""         	${message(code:'fc.landingtest.outsidelanding')}: ${attrs.t.GetLandingTestOutsideLandingPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}<br/>"""
        } else if (!attrs.t.landingTest3Measure) {
            outln"""         	${message(code:'fc.landingresults.measure')}: - (${attrs.t.landingTest3MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        } else {
            outln"""       		${message(code:'fc.landingresults.measure')}: ${attrs.t.landingTest3Measure} (${attrs.t.landingTest3MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        }
        if (attrs.t.landingTest3RollingOutside) {
			outln"""            ${message(code:'fc.landingtest.rollingoutside')}: ${attrs.t.GetLandingTestRollingOutsidePoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest3PowerInBox) {
			outln"""            ${message(code:'fc.landingtest.powerinbox')}: ${attrs.t.GetLandingTestPowerInBoxPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest3GoAroundWithoutTouching) {
			outln"""            ${message(code:'fc.landingtest.goaroundwithouttouching')}: ${attrs.t.GetLandingTestGoAroundWithoutTouchingPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest3GoAroundInsteadStop) {
			outln"""            ${message(code:'fc.landingtest.goaroundinsteadstop')}: ${attrs.t.GetLandingTestGoAroundInsteadStopPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest3AbnormalLanding) {
			outln"""            ${message(code:'fc.landingtest.abnormallanding')}: ${attrs.t.GetLandingTestAbnormalLandingPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest3NotAllowedAerodynamicAuxiliaries) {
			outln"""            ${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}: ${attrs.t.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest3PowerInAir) {
			outln"""            ${message(code:'fc.landingtest.powerinair')}: ${attrs.t.GetLandingTestPowerInAirPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest3FlapsInAir) {
			outln"""            ${message(code:'fc.landingtest.flapsinair')}: ${attrs.t.GetLandingTestFlapsInAirPoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}<br/>"""
        }
        if (attrs.t.landingTest3TouchingObstacle) {
            outln"""            ${message(code:'fc.landingtest.touchingobstacle')}: ${attrs.t.GetLandingTestTouchingObstaclePoints(attrs.t.task.landingTest3Points)} ${message(code:'fc.points')}<br/>"""
        }
        outln"""            </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr class="summary" id="3">"""
        outln"""            <td>${message(code:'fc.test.results.summary')}: ${attrs.t.landingTest3Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest4Complete = { attrs, body ->
		if (attrs.crewResults) {
			outln"""<tr>"""
			outln"""    <th colspan="4">${message(code:'fc.landingtest.landing4')}${if (attrs.t.IsPrecisionLanding()) " ("+message(code:attrs.t.GetPrecisionFlyingLandingText(attrs.t.task.landingTest4Points))+")" else ""}</th>"""
			outln"""</tr>"""
		} else {
			outln"""<table>"""
			outln"""    <tbody>"""
		}
        outln"""        <tr>"""
        if (attrs.t.landingTest4Landing == 2) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestNoLandingPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}</td>"""
        } else if (attrs.t.landingTest4Landing == 3) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestOutsideLandingPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}</td>"""
        } else if (!attrs.t.landingTest4Measure) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>"""
            outln"""        <td>- (${attrs.t.landingTest4MeasurePenalties} ${message(code:'fc.points')})</td>"""
        } else {
            outln"""        <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>"""
            outln"""        <td>${attrs.t.landingTest4Measure} (${attrs.t.landingTest4MeasurePenalties} ${message(code:'fc.points')})</td>"""
        }
        outln"""        </tr>"""
        if (attrs.t.landingTest4RollingOutside) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.rollingoutside')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestRollingOutsidePoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4PowerInBox) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestPowerInBoxPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4GoAroundWithoutTouching) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestGoAroundWithoutTouchingPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4GoAroundInsteadStop) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestGoAroundInsteadStopPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4AbnormalLanding) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestAbnormalLandingPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4NotAllowedAerodynamicAuxiliaries) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4PowerInAir) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinair')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestPowerInAirPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4FlapsInAir) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.flapsinair')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestFlapsInAirPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4TouchingObstacle) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.touchingobstacle')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTestTouchingObstaclePoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
		if (attrs.crewResults) {
			outln"""    <tr>"""
			outln"""        <td class="detailtitle">${message(code:'fc.test.results.summary')}:</td>"""
			outln"""        <td class="subpoints">${attrs.t.landingTest4Penalties} ${message(code:'fc.points')}</td>"""
			outln"""    </tr>"""
       		outln"""	<tr>"""
       		outln"""		<tf colspan="4"></tf>"""
       		outln"""	</tr>"""
		} else {
        	outln"""    </tbody>"""
			outln"""</table>"""
		}
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest4Printable = { attrs, body ->
        outln"""<table class="landingresultlist">"""
        outln"""	<thead>"""
        outln"""        <tr class="name" id="4">"""
        outln"""       		<td>${message(code:'fc.landingtest.landing4')}${if (attrs.t.IsPrecisionLanding()) " ("+message(code:attrs.t.GetPrecisionFlyingLandingText(attrs.t.task.landingTest4Points))+")" else ""}</td>"""
        outln"""        </tr>"""
        outln"""	</thead>"""
        outln"""    <tbody>"""
        outln"""        <tr class="values" id="4">"""
        outln"""        	<td>"""
        if (attrs.t.landingTest4Landing == 2) {
            outln"""       		${message(code:'fc.landingtest.nolanding')}: ${attrs.t.GetLandingTestNoLandingPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}<br/>"""
        } else if (attrs.t.landingTest4Landing == 3) {
            outln"""       		${message(code:'fc.landingtest.outsidelanding')}: ${attrs.t.GetLandingTestOutsideLandingPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}<br/>"""
        } else if (!attrs.t.landingTest4Measure) {
            outln"""       		${message(code:'fc.landingresults.measure')}: - (${attrs.t.landingTest4MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        } else {
            outln"""       		${message(code:'fc.landingresults.measure')}: ${attrs.t.landingTest4Measure} (${attrs.t.landingTest4MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        }
        if (attrs.t.landingTest4RollingOutside) {
			outln"""            ${message(code:'fc.landingtest.rollingoutside')}: ${attrs.t.GetLandingTestRollingOutsidePoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest4PowerInBox) {
			outln"""            ${message(code:'fc.landingtest.powerinbox')}: ${attrs.t.GetLandingTestPowerInBoxPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest4GoAroundWithoutTouching) {
			outln"""            ${message(code:'fc.landingtest.goaroundwithouttouching')}: ${attrs.t.GetLandingTestGoAroundWithoutTouchingPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}<br/>"""
        }
        if (attrs.t.landingTest4GoAroundInsteadStop) {
			outln"""            ${message(code:'fc.landingtest.goaroundinsteadstop')}: ${attrs.t.GetLandingTestGoAroundInsteadStopPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest4AbnormalLanding) {
			outln"""            ${message(code:'fc.landingtest.abnormallanding')}: ${attrs.t.GetLandingTestAbnormalLandingPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest4NotAllowedAerodynamicAuxiliaries) {
			outln"""            ${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}: ${attrs.t.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest4PowerInAir) {
            outln"""            ${message(code:'fc.landingtest.powerinair')}: ${attrs.t.GetLandingTestPowerInAirPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}<br/>"""
        }
        if (attrs.t.landingTest4FlapsInAir) {
            outln"""            ${message(code:'fc.landingtest.flapsinair')}: ${attrs.t.GetLandingTestFlapsInAirPoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}<br/>"""
        }
        if (attrs.t.landingTest4TouchingObstacle) {
			outln"""            ${message(code:'fc.landingtest.touchingobstacle')}: ${attrs.t.GetLandingTestTouchingObstaclePoints(attrs.t.task.landingTest4Points)} ${message(code:'fc.points')}<br/>"""
        }
        outln"""            </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr class="summary" id="4">"""
        outln"""            <td>${message(code:'fc.test.results.summary')}: ${attrs.t.landingTest4Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTestSummaryPrintable = { attrs, body ->
		outln"""<table class="summary">"""
		outln"""	<tfoot>"""
		if (attrs.t.IsLandingTestAnyRun()) {
			if (attrs.t.landingTestOtherPenalties != 0) {
				outln"""<tr class="otherpenalties">"""
				outln"""	<td>${message(code:'fc.landingtest.otherpenalties')}: ${attrs.t.landingTestOtherPenalties} ${message(code:'fc.points')}</td>"""
				outln"""</tr>"""
			}
		}
		outln"""		<tr class="penalties">"""
		outln"""			<td>${message(code:'fc.penalties')}: ${attrs.t.landingTestPenalties} ${message(code:'fc.points')}</td>"""
		outln"""		</tr>"""
		outln"""	</tfoot>"""
		outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
