class LandingResultsTagLib 
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest1Complete = { attrs, body ->
		if (attrs.crewResults) {
			outln"""<tr>"""
			outln"""    <th colspan="4">${message(code:'fc.landingtest.landing1')}${if (attrs.t.IsPrecisionFlying()) " ("+message(code:'fc.landingtest.landing1.precision')+")" else ""}</th>"""
			outln"""</tr>"""
		} else {
        	outln"""<table>"""
			outln"""    <tbody>"""
		}
        outln"""        <tr>"""
        if (attrs.t.landingTest1Landing == 2) {
        	outln"""        <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>"""
        	outln"""        <td>${attrs.t.GetLandingTest1NoLandingPoints()} ${message(code:'fc.points')}</td>"""
        } else if (attrs.t.landingTest1Landing == 3) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest1OutsideLandingPoints()} ${message(code:'fc.points')}</td>"""
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
			outln"""        <td>${attrs.t.GetLandingTest1RollingOutsidePoints()} ${message(code:'fc.points')}</td>"""
			outln"""    </tr>"""
        }
        if (attrs.t.landingTest1PowerInBox) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest1PowerInBoxPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest1GoAroundWithoutTouching) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest1GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest1GoAroundInsteadStop) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest1GoAroundInsteadStopPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest1AbnormalLanding) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest1AbnormalLandingPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest1NotAllowedAerodynamicAuxiliaries) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest1NotAllowedAerodynamicAuxiliariesPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.landingresults.landing')}:</td>"""
        outln"""            <td class="subpoints">${attrs.t.landingTest1Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
		if (attrs.crewResults) {
       		outln"""<tr>"""
       		outln"""	<tf colspan="4"></tf>"""
       		outln"""</tr>"""
		} else {
			outln"""    </tbody>"""
			outln"""</table>"""
		}
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest1Printable = { attrs, body ->
        outln"""<table width="100%" border="1" cellspacing="0" cellpadding="2">"""
        outln"""	<thead>"""
        outln"""        <tr>"""
        outln"""       		<td>${message(code:'fc.landingtest.landing1')}${if (attrs.t.IsPrecisionFlying()) " ("+message(code:'fc.landingtest.landing1.precision')+")" else ""}</td>"""
        outln"""        </tr>"""
        outln"""	</thead>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""        	<td>"""
        if (attrs.t.landingTest1Landing == 2) {
            outln"""   		    ${message(code:'fc.landingtest.nolanding')}: ${attrs.t.GetLandingTest1NoLandingPoints()} ${message(code:'fc.points')}<br/>"""
        } else if (attrs.t.landingTest1Landing == 3) {
            outln"""         	${message(code:'fc.landingtest.outsidelanding')}: ${attrs.t.GetLandingTest1OutsideLandingPoints()} ${message(code:'fc.points')}<br/>"""
        } else if (!attrs.t.landingTest1Measure) {
            outln"""       		${message(code:'fc.landingresults.measure')}: - (${attrs.t.landingTest1MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        } else {
            outln"""       		${message(code:'fc.landingresults.measure')}: ${attrs.t.landingTest1Measure} (${attrs.t.landingTest1MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        }
        if (attrs.t.landingTest1RollingOutside) {
			outln"""            ${message(code:'fc.landingtest.rollingoutside')}: ${attrs.t.GetLandingTest1RollingOutsidePoints()} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest1PowerInBox) {
			outln"""            ${message(code:'fc.landingtest.powerinbox')}: ${attrs.t.GetLandingTest1PowerInBoxPoints()} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest1GoAroundWithoutTouching) {
			outln"""            ${message(code:'fc.landingtest.goaroundwithouttouching')}: ${attrs.t.GetLandingTest1GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest1GoAroundInsteadStop) {
			outln"""            ${message(code:'fc.landingtest.goaroundinsteadstop')}: ${attrs.t.GetLandingTest1GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest1AbnormalLanding) {
			outln"""            ${message(code:'fc.landingtest.abnormallanding')}: ${attrs.t.GetLandingTest1AbnormalLandingPoints()} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest1NotAllowedAerodynamicAuxiliaries) {
			outln"""            ${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}: ${attrs.t.GetLandingTest1NotAllowedAerodynamicAuxiliariesPoints()} ${message(code:'fc.points')}<br/>"""
		}
        outln"""            </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:'fc.landingresults.landing')}: ${attrs.t.landingTest1Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest2Complete = { attrs, body ->
		if (attrs.crewResults) {
			outln"""<tr>"""
			outln"""    <th colspan="4">${message(code:'fc.landingtest.landing2')}${if (attrs.t.IsPrecisionFlying()) " ("+message(code:'fc.landingtest.landing2.precision')+")" else ""}</th>"""
			outln"""</tr>"""
		} else {
	        outln"""<table>"""
	        outln"""    <tbody>"""
		}
        outln"""        <tr>"""
        if (attrs.t.landingTest2Landing == 2) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest2NoLandingPoints()} ${message(code:'fc.points')}</td>"""
        } else if (attrs.t.landingTest2Landing == 3) {
		    outln"""        <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest2OutsideLandingPoints()} ${message(code:'fc.points')}</td>"""
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
            outln"""        <td>${attrs.t.GetLandingTest2RollingOutsidePoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2PowerInBox) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest2PowerInBoxPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2GoAroundWithoutTouching) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest2GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2GoAroundInsteadStop) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest2GoAroundInsteadStopPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2AbnormalLanding) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest2AbnormalLandingPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2NotAllowedAerodynamicAuxiliaries) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest2NotAllowedAerodynamicAuxiliariesPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest2PowerInAir) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinair')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest2PowerInAirPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.landingresults.landing')}:</td>"""
        outln"""            <td class="subpoints">${attrs.t.landingTest2Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
		if (attrs.crewResults) {
       		outln"""<tr>"""
       		outln"""	<tf colspan="4"></tf>"""
       		outln"""</tr>"""
		} else {
        	outln"""    </tbody>"""
			outln"""</table>"""
		}
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest2Printable = { attrs, body ->
        outln"""<table width="100%" border="1" cellspacing="0" cellpadding="2">"""
        outln"""	<thead>"""
        outln"""        <tr>"""
        outln"""       		<td>${message(code:'fc.landingtest.landing2')}${if (attrs.t.IsPrecisionFlying()) " ("+message(code:'fc.landingtest.landing2.precision')+")" else ""}</td>"""
        outln"""        </tr>"""
        outln"""	</thead>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""        	<td>"""
        if (attrs.t.landingTest2Landing == 2) {
            outln"""       		${message(code:'fc.landingtest.nolanding')}: ${attrs.t.GetLandingTest2NoLandingPoints()} ${message(code:'fc.points')}<br/>"""
        } else if (attrs.t.landingTest2Landing == 3) {
            outln"""       		${message(code:'fc.landingtest.outsidelanding')}: ${attrs.t.GetLandingTest2OutsideLandingPoints()} ${message(code:'fc.points')}<br/>"""
        } else if (!attrs.t.landingTest2Measure) {
            outln"""       		${message(code:'fc.landingresults.measure')}: - (${attrs.t.landingTest2MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        } else {
            outln"""       		${message(code:'fc.landingresults.measure')}: ${attrs.t.landingTest2Measure} (${attrs.t.landingTest2MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        }
        if (attrs.t.landingTest2RollingOutside) {
			outln"""            ${message(code:'fc.landingtest.rollingoutside')}: ${attrs.t.GetLandingTest2RollingOutsidePoints()} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest2PowerInBox) {
			outln"""            ${message(code:'fc.landingtest.powerinbox')}: ${attrs.t.GetLandingTest2PowerInBoxPoints()} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest2GoAroundWithoutTouching) {
			outln"""            ${message(code:'fc.landingtest.goaroundwithouttouching')}: ${attrs.t.GetLandingTest2GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest2GoAroundInsteadStop) {
			outln"""            ${message(code:'fc.landingtest.goaroundinsteadstop')}: ${attrs.t.GetLandingTest2GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest2AbnormalLanding) {
			outln"""            ${message(code:'fc.landingtest.abnormallanding')}: ${attrs.t.GetLandingTest2AbnormalLandingPoints()} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest2NotAllowedAerodynamicAuxiliaries) {
			outln"""            ${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}: ${attrs.t.GetLandingTest2NotAllowedAerodynamicAuxiliariesPoints()} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest2PowerInAir) {
			outln"""            ${message(code:'fc.landingtest.powerinair')}: ${attrs.t.GetLandingTest2PowerInAirPoints()} ${message(code:'fc.points')}<br/>"""
		}
        outln"""            </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:'fc.landingresults.landing')}: ${attrs.t.landingTest2Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest3Complete = { attrs, body ->
		if (attrs.crewResults) {
			outln"""<tr>"""
			outln"""    <th colspan="4">${message(code:'fc.landingtest.landing3')}${if (attrs.t.IsPrecisionFlying()) " ("+message(code:'fc.landingtest.landing3.precision')+")" else ""}</th>"""
			outln"""</tr>"""
		} else {
			outln"""<table>"""
			outln"""    <tbody>"""
		}
        outln"""        <tr>"""
        if (attrs.t.landingTest3Landing == 2) {
        	outln"""        <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest3NoLandingPoints()} ${message(code:'fc.points')}</td>"""
        } else if (attrs.t.landingTest3Landing == 3) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest3OutsideLandingPoints()} ${message(code:'fc.points')}</td>"""
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
            outln"""        <td>${attrs.t.GetLandingTest3RollingOutsidePoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3PowerInBox) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest3PowerInBoxPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3GoAroundWithoutTouching) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest3GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3GoAroundInsteadStop) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest3GoAroundInsteadStopPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3AbnormalLanding) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest3AbnormalLandingPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3NotAllowedAerodynamicAuxiliaries) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest3NotAllowedAerodynamicAuxiliariesPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3PowerInAir) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinair')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest3PowerInAirPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest3FlapsInAir) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.flapsinair')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest3FlapsInAirPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.landingresults.landing')}:</td>"""
        outln"""            <td class="subpoints">${attrs.t.landingTest3Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
		if (attrs.crewResults) {
			outln"""<tr>"""
       		outln"""	<tf colspan="4"></tf>"""
			outln"""</tr>"""

		} else {
        	outln"""    </tbody>"""
			outln"""</table>"""
		}
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest3Printable = { attrs, body ->
        outln"""<table width="100%" border="1" cellspacing="0" cellpadding="2">"""
        outln"""	<thead>"""
        outln"""        <tr>"""
        outln"""       		<td>${message(code:'fc.landingtest.landing3')}${if (attrs.t.IsPrecisionFlying()) " ("+message(code:'fc.landingtest.landing3.precision')+")" else ""}</td>"""
        outln"""        </tr>"""
        outln"""	</thead>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""        	<td>"""
        if (attrs.t.landingTest3Landing == 2) {
            outln"""       		${message(code:'fc.landingtest.nolanding')}: ${attrs.t.GetLandingTest3NoLandingPoints()} ${message(code:'fc.points')}<br/>"""
        } else if (attrs.t.landingTest3Landing == 3) {
            outln"""         	${message(code:'fc.landingtest.outsidelanding')}: ${attrs.t.GetLandingTest3OutsideLandingPoints()} ${message(code:'fc.points')}<br/>"""
        } else if (!attrs.t.landingTest3Measure) {
            outln"""         	${message(code:'fc.landingresults.measure')}: - (${attrs.t.landingTest3MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        } else {
            outln"""       		${message(code:'fc.landingresults.measure')}: ${attrs.t.landingTest3Measure} (${attrs.t.landingTest3MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        }
        if (attrs.t.landingTest3RollingOutside) {
			outln"""            ${message(code:'fc.landingtest.rollingoutside')}: ${attrs.t.GetLandingTest3RollingOutsidePoints()} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest3PowerInBox) {
			outln"""            ${message(code:'fc.landingtest.powerinbox')}: ${attrs.t.GetLandingTest3PowerInBoxPoints()} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest3GoAroundWithoutTouching) {
			outln"""            ${message(code:'fc.landingtest.goaroundwithouttouching')}: ${attrs.t.GetLandingTest3GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest3GoAroundInsteadStop) {
			outln"""            ${message(code:'fc.landingtest.goaroundinsteadstop')}: ${attrs.t.GetLandingTest3GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest3AbnormalLanding) {
			outln"""            ${message(code:'fc.landingtest.abnormallanding')}: ${attrs.t.GetLandingTest3AbnormalLandingPoints()} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest3NotAllowedAerodynamicAuxiliaries) {
			outln"""            ${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}: ${attrs.t.GetLandingTest3NotAllowedAerodynamicAuxiliariesPoints()} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest3PowerInAir) {
			outln"""            ${message(code:'fc.landingtest.powerinair')}: ${attrs.t.GetLandingTest3PowerInAirPoints()} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest3FlapsInAir) {
			outln"""            ${message(code:'fc.landingtest.flapsinair')}: ${attrs.t.GetLandingTest3FlapsInAirPoints()} ${message(code:'fc.points')}<br/>"""
        }
        outln"""            </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:'fc.landingresults.landing')}: ${attrs.t.landingTest3Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest4Complete = { attrs, body ->
		if (attrs.crewResults) {
			outln"""<tr>"""
			outln"""    <th colspan="4">${message(code:'fc.landingtest.landing4')}${if (attrs.t.IsPrecisionFlying()) " ("+message(code:'fc.landingtest.landing4.precision')+")" else ""}</th>"""
			outln"""</tr>"""
		} else {
			outln"""<table>"""
			outln"""    <tbody>"""
		}
        outln"""        <tr>"""
        if (attrs.t.landingTest4Landing == 2) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest4NoLandingPoints()} ${message(code:'fc.points')}</td>"""
        } else if (attrs.t.landingTest4Landing == 3) {
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest4OutsideLandingPoints()} ${message(code:'fc.points')}</td>"""
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
            outln"""        <td>${attrs.t.GetLandingTest4RollingOutsidePoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4PowerInBox) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest4PowerInBoxPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4GoAroundWithoutTouching) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest4GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4GoAroundInsteadStop) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest4GoAroundInsteadStopPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4AbnormalLanding) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest4AbnormalLandingPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4NotAllowedAerodynamicAuxiliaries) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest4NotAllowedAerodynamicAuxiliariesPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.landingTest4TouchingObstacle) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.landingtest.touchingobstacle')}:</td>"""
            outln"""        <td>${attrs.t.GetLandingTest4TouchingObstaclePoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.landingresults.landing')}:</td>"""
        outln"""            <td class="subpoints">${attrs.t.landingTest4Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
		if (attrs.crewResults) {
       		outln"""<tr>"""
       		outln"""	<tf colspan="4"></tf>"""
       		outln"""</tr>"""
		} else {
        	outln"""    </tbody>"""
			outln"""</table>"""
		}
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTest4Printable = { attrs, body ->
        outln"""<table width="100%" border="1" cellspacing="0" cellpadding="2">"""
        outln"""	<thead>"""
        outln"""        <tr>"""
        outln"""       		<td>${message(code:'fc.landingtest.landing4')}${if (attrs.t.IsPrecisionFlying()) " ("+message(code:'fc.landingtest.landing4.precision')+")" else ""}</td>"""
        outln"""        </tr>"""
        outln"""	</thead>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""        	<td>"""
        if (attrs.t.landingTest4Landing == 2) {
            outln"""       		${message(code:'fc.landingtest.nolanding')}: ${attrs.t.GetLandingTest4NoLandingPoints()} ${message(code:'fc.points')}<br/>"""
        } else if (attrs.t.landingTest4Landing == 3) {
            outln"""       		${message(code:'fc.landingtest.outsidelanding')}: ${attrs.t.GetLandingTest4OutsideLandingPoints()} ${message(code:'fc.points')}<br/>"""
        } else if (!attrs.t.landingTest4Measure) {
            outln"""       		${message(code:'fc.landingresults.measure')}: - (${attrs.t.landingTest4MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        } else {
            outln"""       		${message(code:'fc.landingresults.measure')}: ${attrs.t.landingTest4Measure} (${attrs.t.landingTest4MeasurePenalties} ${message(code:'fc.points')})<br/>"""
        }
        if (attrs.t.landingTest4RollingOutside) {
			outln"""            ${message(code:'fc.landingtest.rollingoutside')}: ${attrs.t.GetLandingTest4RollingOutsidePoints()} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest4PowerInBox) {
			outln"""            ${message(code:'fc.landingtest.powerinbox')}: ${attrs.t.GetLandingTest4PowerInBoxPoints()} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest4GoAroundWithoutTouching) {
			outln"""            ${message(code:'fc.landingtest.goaroundwithouttouching')}: ${attrs.t.GetLandingTest4GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/>"""
        }
        if (attrs.t.landingTest4GoAroundInsteadStop) {
			outln"""            ${message(code:'fc.landingtest.goaroundinsteadstop')}: ${attrs.t.GetLandingTest4GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/>"""
        }
		if (attrs.t.landingTest4AbnormalLanding) {
			outln"""            ${message(code:'fc.landingtest.abnormallanding')}: ${attrs.t.GetLandingTest4AbnormalLandingPoints()} ${message(code:'fc.points')}<br/>"""
		}
		if (attrs.t.landingTest4NotAllowedAerodynamicAuxiliaries) {
			outln"""            ${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}: ${attrs.t.GetLandingTest4NotAllowedAerodynamicAuxiliariesPoints()} ${message(code:'fc.points')}<br/>"""
		}
        if (attrs.t.landingTest4TouchingObstacle) {
			outln"""            ${message(code:'fc.landingtest.touchingobstacle')}: ${attrs.t.GetLandingTest4TouchingObstaclePoints()} ${message(code:'fc.points')}<br/>"""
        }
        outln"""            </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:'fc.landingresults.landing')}: ${attrs.t.landingTest4Penalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def landingTestSummaryPrintable = { attrs, body ->
		outln"""<table>"""
		outln"""	<tfoot>"""
		if (attrs.t.IsLandingTestAnyRun()) {
			if (attrs.t.landingTestOtherPenalties > 0) {
				outln"""<tr>"""
				outln"""	<td>${message(code:'fc.landingtest.otherpenalties')}: ${attrs.t.landingTestOtherPenalties} ${message(code:'fc.points')}</td>"""
				outln"""</tr>"""
			}
		}
		outln"""		<tr>"""
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
