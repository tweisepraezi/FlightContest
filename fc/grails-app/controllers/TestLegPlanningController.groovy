class TestLegPlanningController 
{
    
	def fcService
	
    def show = {
        def testlegplanning = fcService.getTestLegPlanning(params) 
        if (testlegplanning.instance) {
        	return [testLegPlanningInstance:testlegplanning.instance]
        } else {
            flash.message = testlegplanning.message
            redirect(controller:"task",action:"startplanning")
        }
    }

    def edit = {
        def testlegplanning = fcService.getTestLegPlanningResult(params) 
        if (testlegplanning.instance) {
            return [testLegPlanningInstance:testlegplanning.instance]
        } else {
            flash.message = testlegplanning.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def update = {
        def testlegplanning = fcService.updateTestLegPlanningResult(params) 
        if (testlegplanning.saved) {
            flash.message = testlegplanning.message
            redirect(controller:"test",action:'planningtaskresults',id:params.testid)
        } else if (testlegplanning.instance) {
            if (testlegplanning.error) {
                flash.message = testlegplanning.message
                flash.error = true
            }
            render(view:'edit',model:[testLegPlanningInstance:testlegplanning.instance])
        } else {
            flash.message = testlegplanning.message
            redirect(controller:"test",action:'planningtaskresults',id:params.testid)
        }
    }

    def reset = {
        def testlegplanning = fcService.resetTestLegPlanningResult(params) 
        if (testlegplanning.saved) {
            flash.message = testlegplanning.message
            redirect(controller:"test",action:'planningtaskresults',id:params.testid)
        } else if (testlegplanning.instance) {
            if (testlegplanning.error) {
                flash.message = testlegplanning.message
                flash.error = true
            }
            render(view:'edit',model:[testLegPlanningInstance:testlegplanning.instance])
        } else {
            flash.message = testlegplanning.message
            redirect(controller:"test",action:'planningtaskresults',id:params.testid)
        }
    }

    def cancel = {
        redirect(controller:"test",action:'planningtaskresults',id:params.testid)
    }
}
