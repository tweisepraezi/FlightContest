class PlanningtaskResultsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
    def planningtaskTestResults = { attrs, body ->
        if (TestLegPlanning.countByTest(attrs.t)) {
            outln"""<div>"""
            outln"""    <table>"""
            outln"""        <thead>"""
            outln"""            <tr>"""
            outln"""                <th colspan="8" class="table-head">${message(code:'fc.planningresults.legresultlist')}</th>"""
            outln"""            </tr>"""
            outln"""            <tr>"""
            outln"""                <th>${message(code:'fc.number')}</th>"""
            outln"""                <th>${message(code:'fc.title')}</th>"""
            outln"""                <th/>"""
            outln"""                <th>${message(code:'fc.distance')}</th>"""
            outln"""                <th>${message(code:'fc.truetrack')}</th>"""
            outln"""                <th>${message(code:'fc.trueheading')}</th>"""
            outln"""                <th>${message(code:'fc.groundspeed')}</th>"""
            outln"""                <th>${message(code:'fc.legtime')}</th>"""
            outln"""            </tr>"""
            outln"""        </thead>"""
            outln"""        <tbody>"""
            int leg_no = 0
            Integer penalty_trueheading_summary = 0
            Integer penalty_legtime_summary = 0
            for (TestLegPlanning testlegplanning_instance in TestLegPlanning.findAllByTest(attrs.t,[sort:"id"])) {
                if (!testlegplanning_instance.noPlanningTest) {
                    leg_no++
                    outln"""        <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">"""
                    long next_id = 0
                    boolean set_next = false
                    for (TestLegPlanning testlegplanning_instance2 in TestLegPlanning.findAllByTest(attrs.t,[sort:"id"])) {
                        if (!testlegplanning_instance2.noPlanningTest) {
                            if (set_next) {
                                next_id = testlegplanning_instance2.id
                                set_next = false
                            }
                            if (testlegplanning_instance2 == testlegplanning_instance) {
                                set_next = true
                            }
                        }
                    }
                    outln"""            <td>${testlegplanning_link(testlegplanning_instance, leg_no.toString(), createLink(controller:'testLegPlanning',action:'edit'), next_id)}</td>"""
                    outln"""            <td>${testlegplanning_instance.coordTitle.titleCode()}</td>"""
                    outln"""            <td>${message(code:'fc.test.results.plan')}</td>"""
                    outln"""            <td>${FcMath.DistanceStr(testlegplanning_instance.planTestDistance)}${message(code:'fc.mile')}</td>"""
                    outln"""            <td>${FcMath.GradStr(testlegplanning_instance.planTrueTrack)}${message(code:'fc.grad')}</td>"""
                    outln"""            <td>${FcMath.GradStr(testlegplanning_instance.planTrueHeading)}${message(code:'fc.grad')}</td>"""
                    outln"""            <td>${FcMath.SpeedStr_Planning(testlegplanning_instance.planGroundSpeed)}${message(code:'fc.knot')}</td>"""
                    outln"""            <td>${testlegplanning_instance.planLegTimeStr()}${message(code:'fc.time.h')}</td>"""
                    outln"""        </tr>"""
                    outln"""        <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">"""
                    outln"""            <td/>"""
                    outln"""            <td/>"""
                    outln"""            <td>${message(code:'fc.test.results.given')}</td>"""
                    outln"""            <td/>"""
                    outln"""            <td/>"""
                    if (testlegplanning_instance.resultEntered) {
                        outln"""        <td>${FcMath.GradStrComma(testlegplanning_instance.resultTrueHeading)}${message(code:'fc.grad')}</td>"""
                        outln"""        <td/>"""
                        outln"""        <td>${testlegplanning_instance.resultLegTimeStr()}${message(code:'fc.time.h')}</td>"""
                    } else {
                        if (testlegplanning_instance.test.task.planningTestDirectionMeasure) {
                            outln"""    <td>${message(code:'fc.unknown')}</td>"""
                        } else {
                            outln"""    <td/>"""
                        }
                        outln"""        <td/>"""
                        outln"""        <td>${message(code:'fc.unknown')}</td>"""
                    }
                    outln"""        </tr>"""
                    outln"""        <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">"""
                    outln"""            <td/>"""
                    outln"""            <td/>"""
                    outln"""            <td>${message(code:'fc.test.results.penalty')}</td>"""
                    if (testlegplanning_instance.resultEntered) {
                        outln"""        <td/>"""
                        outln"""        <td/>"""
                        String points_class = "points"
                        if (!testlegplanning_instance.penaltyTrueHeading) {
                            points_class = "zeropoints"
                        }
                        outln"""        <td class="${points_class}">${testlegplanning_instance.penaltyTrueHeading} ${message(code:'fc.points')}</td>"""
                        outln"""        <td/>"""
                        points_class = "points"
                        if (!testlegplanning_instance.penaltyLegTime) {
                            points_class = "zeropoints"
                        }
                        outln"""        <td class="${points_class}">${testlegplanning_instance.penaltyLegTime} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""        <td/>"""
                        outln"""        <td/>"""
                        outln"""        <td>${message(code:'fc.unknown')}</td>"""
                        outln"""        <td/>"""
                        outln"""        <td>${message(code:'fc.unknown')}</td>"""
                    }
                    outln"""        </tr>"""
                    penalty_trueheading_summary += testlegplanning_instance.penaltyTrueHeading
                    penalty_legtime_summary += testlegplanning_instance.penaltyLegTime
                }
            }
            outln"""        </tbody>"""
            outln"""        <tfoot>"""
            outln"""            <tr>"""
            outln"""                <td/>"""
            outln"""                <td/>"""
            outln"""                <td>${message(code:'fc.test.results.summary')}</td>"""
            outln"""                <td/>"""
            outln"""                <td/>"""
            outln"""                <td>${penalty_trueheading_summary} ${message(code:'fc.points')}</td>"""
            outln"""                <td/>"""
            outln"""                <td>${penalty_legtime_summary} ${message(code:'fc.points')}</td>"""
            outln"""            </tr>"""
            outln"""        </tfoot>"""
            outln"""    </table>"""
            outln"""</div>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def planningtaskTestCrewResults = { attrs, body ->
        outln"""<table>"""
        outln"""    <thead>"""
        outln"""        <tr>"""
        if (attrs.t.planningTestComplete) {
            outln"""        <th colspan="7" class="table-head">${message(code:'fc.planningresults')} (${message(code:'fc.version')} ${attrs.t.GetPlanningTestVersion()})</th>"""
        } else {
            outln"""        <th colspan="7" class="table-head">${message(code:'fc.planningresults')} (${message(code:'fc.version')} ${attrs.t.GetPlanningTestVersion()}) [${message(code:'fc.provisional')}]</th>"""
        }
        outln"""        </tr>"""
        outln"""    </thead>"""
        if (TestLegPlanning.countByTest(attrs.t)) {
            outln"""<thead>"""
            outln"""    <tr>"""
            outln"""        <th>${message(code:'fc.title')}</th>"""
            outln"""        <th colspan="3">${message(code:'fc.trueheading')}</th>"""
            outln"""        <th colspan="3">${message(code:'fc.legtime')}</th>"""
            outln"""    </tr>"""
            outln"""    <tr>"""
            outln"""        <th/>"""
            outln"""        <th>${message(code:'fc.test.results.plan')}</th>"""
            outln"""        <th>${message(code:'fc.test.results.given')}</th>"""
            outln"""        <th>${message(code:'fc.points')}</th>"""
            outln"""        <th>${message(code:'fc.test.results.plan')}</th>"""
            outln"""        <th>${message(code:'fc.test.results.given')}</th>"""
            outln"""        <th>${message(code:'fc.points')}</th>"""
            outln"""    </tr>"""
            outln"""</thead>"""
            outln"""<tbody>"""
            Integer penalty_trueheading_summary = 0
            Integer penalty_legtime_summary = 0
            TestLegPlanning last_testlegplanning_instance = null
            for(TestLegPlanning testlegplanning_instance in TestLegPlanning.findAllByTest(attrs.t,[sort:"id"])) {
                if (last_testlegplanning_instance && !last_testlegplanning_instance.noPlanningTest) {
                    penalty_trueheading_summary += last_testlegplanning_instance.penaltyTrueHeading
                    penalty_legtime_summary += last_testlegplanning_instance.penaltyLegTime
                    outln"""<tr>"""
                    outln"""    <td>${last_testlegplanning_instance.coordTitle.titleCode()}</td>"""
                    outln"""    <td>${FcMath.GradStr(last_testlegplanning_instance.planTrueHeading)}${message(code:'fc.grad')}</td>"""
                    outln"""    <td>${FcMath.GradStrComma(last_testlegplanning_instance.resultTrueHeading)}${message(code:'fc.grad')}</td>"""
                    outln"""    <td>${last_testlegplanning_instance.penaltyTrueHeading}</td>"""
                    outln"""    <td>${FcMath.TimeStr(last_testlegplanning_instance.planLegTime)}</td>"""
                    outln"""    <td>${FcMath.TimeStr(last_testlegplanning_instance.resultLegTime)}</td>"""
                    outln"""    <td>${last_testlegplanning_instance.penaltyLegTime}</td>"""
                    outln"""</tr>"""
                }
                last_testlegplanning_instance = testlegplanning_instance
            }
            if (last_testlegplanning_instance && !last_testlegplanning_instance.noPlanningTest) {
                penalty_trueheading_summary += last_testlegplanning_instance.penaltyTrueHeading
                penalty_legtime_summary += last_testlegplanning_instance.penaltyLegTime
                outln"""<tr>"""
                outln"""    <td>${last_testlegplanning_instance.coordTitle.titleCode()}</td>"""
                outln"""    <td>${FcMath.GradStr(last_testlegplanning_instance.planTrueHeading)}${message(code:'fc.grad')}</td>"""
                outln"""    <td>${FcMath.GradStrComma(last_testlegplanning_instance.resultTrueHeading)}${message(code:'fc.grad')}</td>"""
                outln"""    <td>${last_testlegplanning_instance.penaltyTrueHeading}</td>"""
                outln"""    <td>${FcMath.TimeStr(last_testlegplanning_instance.planLegTime)}</td>"""
                outln"""    <td>${FcMath.TimeStr(last_testlegplanning_instance.resultLegTime)}</td>"""
                outln"""    <td>${last_testlegplanning_instance.penaltyLegTime}</td>"""
                outln"""</tr>"""
            }
            outln"""</tbody>"""
            outln"""<tfoot>"""
            outln"""    <tr>"""
            outln"""        <td/>"""
            outln"""        <td/>"""
            outln"""        <td/>"""
            outln"""        <td>${penalty_trueheading_summary}</td>"""
            outln"""        <td/>"""
            outln"""        <td/>"""
            outln"""        <td>${penalty_legtime_summary}</td>"""
            outln"""    </tr>"""
            outln"""</tfoot>"""
        }
        outln"""</table>"""
        outln"""<table>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.planningresults.legpenalties')}:</td>"""
        outln"""            <td>${attrs.t.planningTestLegPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        if (attrs.t.planningTestGivenTooLate) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.planningtest.giventolate')}:</td>"""
            outln"""        <td>${attrs.t.GetPlanningTestPlanTooLatePoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.planningTestExitRoomTooLate) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.planningtest.exitroomtolate')}:</td>"""
            outln"""        <td>${attrs.t.GetPlanningTestExitRoomTooLatePoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.planningTestForbiddenCalculators) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.planningtest.forbiddencalculators')}:</td>"""
            outln"""        <td>${attrs.t.GetPlanningTestForbiddenCalculatorsPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.planningTestOtherPenalties != 0) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.planningtest.otherpenalties')}:</td>"""
            outln"""        <td>${attrs.t.planningTestOtherPenalties} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.penalties')}:</td>"""
        String points_class = "points"
        if (!attrs.t.planningTestPenalties) {
            points_class = "zeropoints"
        }
        outln"""            <td class="${points_class}">${attrs.t.planningTestPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
    }
    
	// --------------------------------------------------------------------------------------------------------------------
	def planningtaskTestPrintable = { attrs, body ->
        if (TestLegPlanning.countByTest(attrs.t)) {
	        outln"""<table class="planningtaskresultlist">"""
	        outln"""    <thead>"""
	        outln"""        <tr class="name1">"""
	        outln"""            <th>${message(code:'fc.title')}</th>"""
	        outln"""            <th colspan="3">${message(code:'fc.trueheading')}</th>"""
	        outln"""            <th colspan="3">${message(code:'fc.legtime')}</th>"""
	        outln"""        </tr>"""
	        outln"""        <tr class="name2">"""
	        outln"""        	<th/>"""
	        outln"""            <th>${message(code:'fc.test.results.plan')}</th>"""
	        outln"""            <th>${message(code:'fc.test.results.given')}</th>"""
	        outln"""            <th>${message(code:'fc.points')}</th>"""
	        outln"""            <th>${message(code:'fc.test.results.plan')}</th>"""
	        outln"""            <th>${message(code:'fc.test.results.given')}</th>"""
	        outln"""            <th>${message(code:'fc.points')}</th>"""
	        outln"""        </tr>"""
	        outln"""    </thead>"""
	        outln"""    <tbody>"""
	        Integer penalty_trueheading_summary = 0
	        Integer penalty_legtime_summary = 0
			TestLegPlanning last_testlegplanning_instance = null
	        for (TestLegPlanning test_legplanning_instance in TestLegPlanning.findAllByTest(attrs.t,[sort:"id"])) {
	            if (last_testlegplanning_instance && !last_testlegplanning_instance.noPlanningTest) {
	                penalty_trueheading_summary += last_testlegplanning_instance.penaltyTrueHeading
	                penalty_legtime_summary += last_testlegplanning_instance.penaltyLegTime
	                outln"""<tr class="value" id="${last_testlegplanning_instance.coordTitle.titlePrintCode()}">"""
	                outln"""    <td class="tpname">${last_testlegplanning_instance.coordTitle.titlePrintCode()}</td>"""
	                outln"""    <td class="plantrueheading">${FcMath.GradStr(last_testlegplanning_instance.planTrueHeading)}${message(code:'fc.grad')}</td>"""
	                outln"""    <td class="trueheading">${FcMath.GradStrComma(last_testlegplanning_instance.resultTrueHeading)}${message(code:'fc.grad')}</td>"""
	                outln"""    <td class="penaltytrueheading">${last_testlegplanning_instance.penaltyTrueHeading}</td>"""
	                outln"""    <td class="planlegtime">${FcMath.TimeStr(last_testlegplanning_instance.planLegTime)}</td>"""
	                outln"""    <td class="legtime">${FcMath.TimeStr(last_testlegplanning_instance.resultLegTime)}</td>"""
	                outln"""    <td class="penaltylegtime">${last_testlegplanning_instance.penaltyLegTime}</td>"""
	                outln"""</tr>"""
	            }
	            last_testlegplanning_instance = test_legplanning_instance
	        }
	        if (last_testlegplanning_instance && !last_testlegplanning_instance.noPlanningTest) {
	            penalty_trueheading_summary += last_testlegplanning_instance.penaltyTrueHeading
	            penalty_legtime_summary += last_testlegplanning_instance.penaltyLegTime
	            outln"""	<tr class="value" id="${last_testlegplanning_instance.coordTitle.titlePrintCode()}">"""
	            outln"""	    <td class="tpname">${last_testlegplanning_instance.coordTitle.titlePrintCode()}</td>"""
	            outln"""	    <td class="plantrueheading">${FcMath.GradStr(last_testlegplanning_instance.planTrueHeading)}${message(code:'fc.grad')}</td>"""
	            outln"""	    <td class="trueheading">${FcMath.GradStrComma(last_testlegplanning_instance.resultTrueHeading)}${message(code:'fc.grad')}</td>"""
	            outln"""	    <td class="penaltytrueheading">${last_testlegplanning_instance.penaltyTrueHeading}</td>"""
	            outln"""	    <td class="planlegtime">${FcMath.TimeStr(last_testlegplanning_instance.planLegTime)}</td>"""
	            outln"""	    <td class="legtime">${FcMath.TimeStr(last_testlegplanning_instance.resultLegTime)}</td>"""
	            outln"""	    <td class="penaltylegtime">${last_testlegplanning_instance.penaltyLegTime}</td>"""
	            outln"""	</tr>"""
	        }
	        outln"""	</tbody>"""
	        outln"""	<tfoot>"""
	        outln"""		<tr class="summary">"""
	        outln"""			<td class="tpname"/>"""
	        outln"""			<td class="plantrueheading"/>"""
	        outln"""			<td class="trueheading"/>"""
	        outln"""			<td class="penaltytrueheading">${penalty_trueheading_summary}</td>"""
	        outln"""			<td class="planlegtime"/>"""
	        outln"""			<td class="legtime"/>"""
			outln"""			<td class="penaltylegtime">${penalty_legtime_summary}</td>"""
	        outln"""		</tr>"""
	        outln"""	</tfoot>"""
	        outln"""</table>"""
            outln"""<br/>"""
        }
        outln"""<table class="summary">"""
        outln"""    <tbody>"""
        outln"""        <tr class="legpenalties">"""
        outln"""            <td>${message(code:'fc.planningresults.legpenalties')}: ${attrs.t.planningTestLegPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        if (attrs.t.planningTestGivenTooLate) {
			outln"""    <tr class="giventolate">"""
			outln"""    	<td>${message(code:'fc.planningtest.giventolate')}: ${attrs.t.GetPlanningTestPlanTooLatePoints()} ${message(code:'fc.points')}</td>"""
			outln"""    </tr>"""
        }
        if (attrs.t.planningTestExitRoomTooLate) {
        	outln"""	<tr class="exitroomtolate">"""
         	outln"""		<td>${message(code:'fc.planningtest.exitroomtolate')}: ${attrs.t.GetPlanningTestExitRoomTooLatePoints()} ${message(code:'fc.points')}</td>"""
        	outln"""	</tr>"""
        }
        if (attrs.t.planningTestForbiddenCalculators) {
            outln"""    <tr class="exitroomtolate">"""
            outln"""        <td>${message(code:'fc.planningtest.forbiddencalculators')}: ${attrs.t.GetPlanningTestForbiddenCalculatorsPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.planningTestOtherPenalties != 0) {
            outln"""	<tr class="otherpenalties">"""
            outln"""	    <td>${message(code:'fc.planningtest.otherpenalties')}: ${attrs.t.planningTestOtherPenalties} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        outln"""        <tr>"""
        outln"""        	<td> </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr class="penalties">"""
        outln"""            <td>${message(code:'fc.penalties')}: ${attrs.t.planningTestPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
    private String testlegplanning_link(TestLegPlanning testLegPlanningInstance, String name, String link, long next_id)
    {
        String t = name
        if (testLegPlanningInstance.resultEntered) {
            t += """ <img src="/fc/images/skin/ok.png"/>"""
        } else {
            t += " ..."
        }
        if (next_id) {
            return """<a href="${link}/${testLegPlanningInstance.id}?name=${name}&next=${next_id}">${t}</a>"""
        }
        return """<a href="${link}/${testLegPlanningInstance.id}?name=${name}">${t}</a>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def planningtaskTestScannedPrintable = { attrs, body ->
        if (attrs.t.scannedPlanningTest) {
            outln"""<br/>"""
            outln"""<img class="scannedplanningtest" src="${createLink(controller:'test',action:'planningtaskformimage',params:[testid:attrs.t.id])}" />"""
        }
    } // def planningtaskTestScannedPrintable
    
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
