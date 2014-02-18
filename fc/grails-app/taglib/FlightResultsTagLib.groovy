class FlightResultsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
	def flightTestPrintable = { attrs, body ->
		if (CoordResult.countByTest(attrs.t)) {
			outln"""<table width="100%" border="1" cellspacing="0" cellpadding="2">"""
			outln"""	<thead>"""
			outln"""		<tr>"""
			outln"""			<th class="table-head">${message(code:'fc.title')}</th>"""
			outln"""			<th class="table-head">${message(code:'fc.aflos.checkpoint')}</th>"""
			outln"""			<th colspan="3" class="table-head">${message(code:'fc.cptime')}</th>"""
			if (attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) {
				outln"""		<th class="table-head">${message(code:'fc.procedureturn')}</th>"""
			}
			if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
				outln"""		<th class="table-head">${message(code:'fc.badcoursenum')}</th>"""
			}
			if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
				outln"""		<th class="table-head">${message(code:'fc.altitude')}</th>"""
			}
			outln"""		</tr>"""
			outln"""		<tr>"""
			outln"""			<th/>"""
			outln"""			<th/>"""
			outln"""			<th>${message(code:'fc.test.results.plan')}</th>"""
			outln"""			<th>${message(code:'fc.test.results.measured')}</th>"""
			outln"""			<th>${message(code:'fc.points')}</th>"""
			if (attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) {
			outln"""			<th/>"""
			}
			if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
			outln"""			<th/>"""
			}
			if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
			outln"""			<th/>"""
			}
			outln"""		</tr>"""
			outln"""	</thead>"""
			outln"""	<tbody>"""
			Integer penalty_coord_summary = 0
			Integer	penalty_procedureturn_summary = 0
			Integer	penalty_badcourse_summary = 0
			Integer penalty_altitude_summary = 0
			String disabled_checkpoints = attrs.t.task.disabledCheckPoints
			boolean check_secretpoints = attrs.t.IsFlightTestCheckSecretPoints()
			CoordResult last_coordresult_instance = null
			for (CoordResult coordresult_instance in CoordResult.findAllByTest(attrs.t,[sort:"id"])) {
				if (last_coordresult_instance) {
					if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
						penalty_coord_summary += last_coordresult_instance.penaltyCoord
					}
					outln"""<tr>"""
					outln"""	<td>${last_coordresult_instance.titlePrintCode()}</td>"""
					outln"""	<td>${last_coordresult_instance.mark}</td>"""
					outln"""	<td>${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
					if (last_coordresult_instance.resultCpNotFound) {
						outln"""<td>-</td>"""
					} else {
						outln"""<td>${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
					}
					if (disabled_checkpoints.contains(last_coordresult_instance.title()+',')) {
						outln"""<td>-</td>"""
					} else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
						outln"""<td>${last_coordresult_instance.penaltyCoord}</td>"""
					} else {
						outln"""<td>-</td>"""
					}
					if (attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) {
						if (coordresult_instance.planProcedureTurn) {
							if (coordresult_instance.resultProcedureTurnEntered) {
								if (disabled_checkpoints.contains(last_coordresult_instance.title()+',')) {
									outln"""<td>-</td>"""
								} else if (coordresult_instance.resultProcedureTurnNotFlown) {
									penalty_procedureturn_summary += attrs.t.GetFlightTestProcedureTurnNotFlownPoints()
									outln"""<td>${attrs.t.GetFlightTestProcedureTurnNotFlownPoints()}</td>"""
								} else {
									outln"""<td>0</td>"""
								}
							} else {
								outln"""<td/>"""
							}
						} else {
							outln"""<td/>"""
						}
					}
					if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
						if (last_coordresult_instance.resultEntered && last_coordresult_instance.type.IsBadCourseCheckCoord()) {
							penalty_badcourse_summary += last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()
							if (last_coordresult_instance.resultBadCourseNum > 0) {
								outln"""<td>${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()} (${last_coordresult_instance.resultBadCourseNum})</td>"""
							} else {
								outln"""<td>${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()}</td>"""
							}
						} else {
							outln"""<td/>"""
						}
					}
					if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
						if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
							if (last_coordresult_instance.resultAltitude && last_coordresult_instance.resultMinAltitudeMissed) {
								penalty_altitude_summary += attrs.t.GetFlightTestMinAltitudeMissedPoints()
								outln"""<td>${attrs.t.GetFlightTestMinAltitudeMissedPoints()} (${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')})</td>"""
							} else {
								if (last_coordresult_instance.resultCpNotFound) {
									outln"""<td>0 (-)</td>"""
								} else {
									outln"""<td>0 (${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')})</td>"""
								}
							}
						} else {
							outln"""<td/>"""
						}
					}
					outln"""</tr>"""
				}
				last_coordresult_instance = coordresult_instance
			}
			if (last_coordresult_instance) {
				if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
					penalty_coord_summary += last_coordresult_instance.penaltyCoord
				}
				outln"""    <tr>"""
				outln"""		<td>${last_coordresult_instance.titlePrintCode()}</td>"""
				outln"""		<td>${last_coordresult_instance.mark}</td>"""
				outln"""		<td>${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
				if (last_coordresult_instance.resultCpNotFound) {
					outln"""	<td>-</td>"""
				} else {
					outln"""	<td>${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
				}
				if (disabled_checkpoints.contains(last_coordresult_instance.title()+',')) {
					outln"""	<td>-</td>"""
				} else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
					outln"""	<td>${last_coordresult_instance.penaltyCoord}</td>"""
				} else {
					outln"""	<td>-</td>"""
				}
				if (attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) {
					outln"""	<td/>"""
				}
				if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
					if (last_coordresult_instance.resultEntered && last_coordresult_instance.type.IsBadCourseCheckCoord()) {
						penalty_badcourse_summary += last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()
						if (last_coordresult_instance.resultBadCourseNum > 0) {
							outln"""<td>${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()} (${last_coordresult_instance.resultBadCourseNum})</td>"""
						} else {
						   	outln"""<td>${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()}</td>"""
						}
					} else {
						outln"""<td/>"""
					}
				}
				if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
					if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
						if (last_coordresult_instance.resultAltitude && last_coordresult_instance.resultMinAltitudeMissed) {
							penalty_altitude_summary += attrs.t.GetFlightTestMinAltitudeMissedPoints()
							outln"""<td>${attrs.t.GetFlightTestMinAltitudeMissedPoints()} (${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')})</td>"""
						} else {
							if (last_coordresult_instance.resultCpNotFound) {
								outln"""<td>0 (-)</td>"""
							} else {
								outln"""<td>0 (${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')})</td>"""
							}
						}
					} else {
						outln"""<td/>"""
					}
				}
				outln"""</tr>"""
			}
			outln"""	</tbody>"""
			outln"""	<tfoot>"""
			outln"""		<tr>"""
			outln"""			<td/>"""
			outln"""			<td/>"""
			outln"""			<td/>"""
			outln"""			<td/>"""
			outln"""			<td>${penalty_coord_summary}</td>"""
			if (attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) {
				outln"""		<td>${penalty_procedureturn_summary}</td>"""
			}
			if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
				outln"""		<td>${penalty_badcourse_summary}</td>"""
			}
			if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
				outln"""		<td>${penalty_altitude_summary}</td>"""
			}
			outln"""		</tr>"""
			outln"""	</tfoot>"""
			outln"""</table>"""
			outln"""<br/>"""
		}
        outln"""<table>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:'fc.flightresults.checkpointpenalties')}: ${attrs.t.flightTestCheckPointPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        if (attrs.t.flightTestTakeoffMissed) {
			outln"""	<tr>"""
            outln"""       	<td>${message(code:'fc.flighttest.takeoffmissed')}: ${attrs.t.GetFlightTestTakeoffMissedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""   	</tr>"""
        }
        if (attrs.t.flightTestLandingTooLate) {
            outln"""	<tr>"""
            outln"""	    <td>${message(code:'fc.flighttest.landingtolate')}: ${attrs.t.GetFlightTestLandingToLatePoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestBadCourseStartLanding) {
        	outln"""	<tr>"""
         	outln"""		<td>${message(code:'fc.flighttest.badcoursestartlanding')}: ${attrs.t.GetFlightTestBadCourseStartLandingPoints()} ${message(code:'fc.points')}</td>"""
        	outln"""	</tr>"""
        }
        if (attrs.t.flightTestGivenTooLate) {
        	outln"""	<tr>"""
            outln"""		<td>${message(code:'fc.flighttest.giventolate')}: ${attrs.t.GetFlightTestGivenToLatePoints()} ${message(code:'fc.points')}</td>"""
        	outln"""	</tr>"""
        }
        if (attrs.t.flightTestSafetyAndRulesInfringement) {
            outln"""	<tr>"""
            outln"""	    <td>${message(code:'fc.flighttest.safetyandrulesinfringement')}: ${attrs.t.GetFlightTestSafetyAndRulesInfringementPoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestInstructionsNotFollowed) {
            outln"""	<tr>"""
            outln"""	    <td>${message(code:'fc.flighttest.instructionsnotfollowed')}: ${attrs.t.GetFlightTestInstructionsNotFollowedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestFalseEnvelopeOpened) {
            outln"""	<tr>"""
            outln"""	    <td>${message(code:'fc.flighttest.falseenvelopeopened')}: ${attrs.t.GetFlightTestFalseEnvelopeOpenedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestSafetyEnvelopeOpened) {
            outln"""	<tr>"""
            outln"""	    <td>${message(code:'fc.flighttest.safetyenvelopeopened')}: ${attrs.t.GetFlightTestSafetyEnvelopeOpenedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestFrequencyNotMonitored) {
            outln"""	<tr>"""
            outln"""	    <td>${message(code:'fc.flighttest.frequencynotmonitored')}: ${attrs.t.GetFlightTestFrequencyNotMonitoredPoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestOtherPenalties > 0) {
            outln"""	<tr>"""
            outln"""	    <td>${message(code:'fc.flighttest.otherpenalties')}: ${attrs.t.flightTestOtherPenalties} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        outln"""	    <tr>"""
        outln"""	      	<td> </td>"""
        outln"""	    </tr>"""
        outln"""	</tbody>"""
        outln"""	<tfoot>"""
        outln"""	    <tr>"""
        outln"""	    	<td>${message(code:'fc.penalties')}: ${attrs.t.flightTestPenalties} ${message(code:'fc.points')}</td>"""
        outln"""	    </tr>"""
        outln"""	</tfoot>"""
    	outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def flightTestAFLOSPrintable = { attrs, body ->
		if (CoordResult.countByTest(attrs.t)) {
			outln"""<br/>"""
			outln"""<table width="100%" border="1" cellspacing="0" cellpadding="2">"""
			outln"""	<thead>"""
			outln"""		<tr>"""
			outln"""			<th class="table-head">${message(code:'fc.title')}</th>"""
			outln"""			<th class="table-head">${message(code:'fc.aflos.checkpoint')}</th>"""
			outln"""			<th colspan="2" class="table-head">${message(code:'fc.cptime')}</th>"""
			if (attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) {
				outln"""		<th class="table-head">${message(code:'fc.procedureturn')}</th>"""
			}
			if (true || (attrs.t.GetFlightTestBadCoursePoints() > 0)) {
				outln"""		<th class="table-head">${message(code:'fc.badcoursenum')}</th>"""
			}
			if (true || (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0)) {
				outln"""		<th class="table-head">${message(code:'fc.altitude')}</th>"""
			}
			outln"""		</tr>"""
			outln"""		<tr>"""
			outln"""			<th/>"""
			outln"""			<th/>"""
			outln"""			<th>${message(code:'fc.test.results.plan')}</th>"""
			outln"""			<th>${message(code:'fc.test.results.measured')}</th>"""
			if (attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) {
				outln"""		<th/>"""
			}
			if (true || (attrs.t.GetFlightTestBadCoursePoints() > 0)) {
				outln"""		<th/>"""
			}
			if (true || (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0)) {
				outln"""		<th/>"""
			}
			outln"""		</tr>"""
			outln"""	</thead>"""
			outln"""	<tbody>"""
			CoordResult last_coordresult_instance = null
			for (CoordResult coordresult_instance in CoordResult.findAllByTest(attrs.t,[sort:"id"])) {
				if (last_coordresult_instance) {
					outln"""<tr>"""
					outln"""	<td>${last_coordresult_instance.titlePrintCode()}</td>"""
					outln"""	<td>${last_coordresult_instance.mark}</td>"""
					outln"""	<td>${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
					if (last_coordresult_instance.resultCpNotFound) {
						outln"""<td>-</td>"""
					} else {
						outln"""<td>${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
					}
					if (attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) {
						if (coordresult_instance.planProcedureTurn) {
							if (coordresult_instance.resultProcedureTurnEntered) {
								if (coordresult_instance.resultProcedureTurnNotFlown) {
									outln"""<td>${message(code:'fc.flighttest.procedureturnnotflown.short')}</td>"""
								} else {
									outln"""<td>${message(code:'fc.flighttest.procedureturnflown.short')}</td>"""
								}
							} else {
								outln"""<td/>"""
							}
						} else {
							outln"""<td/>"""
						}
					}
					if (true || (attrs.t.GetFlightTestBadCoursePoints() > 0)) {
						if (last_coordresult_instance.resultEntered) {
							if (last_coordresult_instance.type.IsBadCourseCheckCoord()) {
								outln"""<td>${last_coordresult_instance.resultBadCourseNum}</td>"""
							} else {
								outln"""<td/>"""
							}
						} else {
							outln"""<td/>"""
						}
					}
					if (true || (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0)) {
						if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
							if (last_coordresult_instance.resultCpNotFound) {
								outln"""<td>-</td>"""
							} else {
								outln"""<td>${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')}</td>"""
							}
						} else {
							outln"""<td/>"""
						}
					}
					outln"""</tr>"""
				}
				last_coordresult_instance = coordresult_instance
			}
			if (last_coordresult_instance) {
				outln"""	<tr>"""
				outln"""		<td>${last_coordresult_instance.titlePrintCode()}</td>"""
				outln"""		<td>${last_coordresult_instance.mark}</td>"""
				outln"""			<td>${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
				if (last_coordresult_instance.resultCpNotFound) {
					outln"""	<td>-</td>"""
				} else {
					outln"""	<td>${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
				}
				if (attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) {
					outln"""	<td/>"""
				}
				if (true || (attrs.t.GetFlightTestBadCoursePoints() > 0)) {
					if (last_coordresult_instance.resultEntered) {
						if (last_coordresult_instance.type.IsBadCourseCheckCoord()) {
							outln"""<td>${last_coordresult_instance.resultBadCourseNum}</td>"""
						} else {
						   	outln"""<td/>"""
						}
					} else {
					   	outln"""<td/>"""
					}
				}
				if (true || (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0)) {
					if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
						if (last_coordresult_instance.resultCpNotFound) {
							outln"""<td>-</td>"""
						} else {
							outln"""<td>${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')}</td>"""
						}
					} else {
						outln"""<td/>"""
					}
				}
				outln"""	</tr>"""
			}
			outln"""	</tbody>"""
			outln"""</table>"""
		}
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
