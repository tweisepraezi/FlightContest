class PlanningtaskResultsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
	// --------------------------------------------------------------------------------------------------------------------
	def planningtaskTestPrintable = { attrs, body ->
        if (TestLegPlanning.countByTest(attrs.t)) {
	        outln"""<table width="100%" border="1" cellspacing="0" cellpadding="2">"""
	        outln"""    <thead>"""
	        outln"""        <tr>"""
	        outln"""            <th class="table-head">${message(code:'fc.title')}</th>"""
	        outln"""            <th colspan="3" class="table-head">${message(code:'fc.trueheading')}</th>"""
	        outln"""            <th colspan="3" class="table-head">${message(code:'fc.legtime')}</th>"""
	        outln"""        </tr>"""
	        outln"""        <tr>"""
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
	            if (last_testlegplanning_instance) {
	                penalty_trueheading_summary += last_testlegplanning_instance.penaltyTrueHeading
	                penalty_legtime_summary += last_testlegplanning_instance.penaltyLegTime
	                outln"""<tr>"""
	                outln"""    <td>${last_testlegplanning_instance.coordTitle.titlePrintCode()}</td>"""
	                outln"""    <td>${FcMath.GradStr(last_testlegplanning_instance.planTrueHeading)}${message(code:'fc.grad')}</td>"""
	                outln"""    <td>${FcMath.GradStrComma(last_testlegplanning_instance.resultTrueHeading)}${message(code:'fc.grad')}</td>"""
	                outln"""    <td>${last_testlegplanning_instance.penaltyTrueHeading}</td>"""
	                outln"""    <td>${FcMath.TimeStr(last_testlegplanning_instance.planLegTime)}</td>"""
	                outln"""    <td>${FcMath.TimeStr(last_testlegplanning_instance.resultLegTime)}</td>"""
	                outln"""    <td>${last_testlegplanning_instance.penaltyLegTime}</td>"""
	                outln"""</tr>"""
	            }
	            last_testlegplanning_instance = test_legplanning_instance
	        }
	        if (last_testlegplanning_instance) {
	            penalty_trueheading_summary += last_testlegplanning_instance.penaltyTrueHeading
	            penalty_legtime_summary += last_testlegplanning_instance.penaltyLegTime
	            outln"""	<tr>"""
	            outln"""	    <td>${last_testlegplanning_instance.coordTitle.titlePrintCode()}</td>"""
	            outln"""	    <td>${FcMath.GradStr(last_testlegplanning_instance.planTrueHeading)}${message(code:'fc.grad')}</td>"""
	            outln"""	    <td>${FcMath.GradStrComma(last_testlegplanning_instance.resultTrueHeading)}${message(code:'fc.grad')}</td>"""
	            outln"""	    <td>${last_testlegplanning_instance.penaltyTrueHeading}</td>"""
	            outln"""	    <td>${FcMath.TimeStr(last_testlegplanning_instance.planLegTime)}</td>"""
	            outln"""	    <td>${FcMath.TimeStr(last_testlegplanning_instance.resultLegTime)}</td>"""
	            outln"""	    <td>${last_testlegplanning_instance.penaltyLegTime}</td>"""
	            outln"""	</tr>"""
	        }
	        outln"""	</tbody>"""
	        outln"""	<tfoot>"""
	        outln"""		<tr>"""
	        outln"""			<td/>"""
	        outln"""			<td/>"""
	        outln"""			<td/>"""
	        outln"""			<td>${penalty_trueheading_summary}</td>"""
	        outln"""			<td/>"""
	        outln"""			<td/>"""
			outln"""			<td>${penalty_legtime_summary}</td>"""
	        outln"""		</tr>"""
	        outln"""	</tfoot>"""
	        outln"""</table>"""
            outln"""<br/>"""
        }
        outln"""<table>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:'fc.planningresults.legpenalties')}: ${attrs.t.planningTestLegPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        if (attrs.t.planningTestGivenTooLate) {
			outln"""    <tr>"""
			outln"""    	<td>${message(code:'fc.planningtest.giventolate')}: ${attrs.t.GetPlanningTestPlanTooLatePoints()} ${message(code:'fc.points')}</td>"""
			outln"""    </tr>"""
        }
        if (attrs.t.planningTestExitRoomTooLate) {
        	outln"""	<tr>"""
         	outln"""		<td>${message(code:'fc.planningtest.exitroomtolate')}: ${attrs.t.GetPlanningTestExitRoomTooLatePoints()} ${message(code:'fc.points')}</td>"""
        	outln"""	</tr>"""
        }
        if (attrs.t.planningTestOtherPenalties > 0) {
            outln"""	<tr>"""
            outln"""	    <td>${message(code:'fc.planningtest.otherpenalties')}: ${attrs.t.planningTestOtherPenalties} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        outln"""        <tr>"""
        outln"""        	<td> </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:'fc.penalties')}: ${attrs.t.planningTestPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
