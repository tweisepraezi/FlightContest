class TestLegFlightController 
{
    
	def fcService
	
    def show = {
        def testlegflight = fcService.getTestLegFlight(params) 
        if (testlegflight.instance) {
        	return [testLegFlightInstance:testlegflight.instance]
        } else {
            flash.message = testlegflight.message
            redirect(controller:"contestDayTask",action:"startplanning")
        }
    }

}
