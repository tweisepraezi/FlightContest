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

    def updatereturn = {
        def testlegplanning = fcService.updateTestLegPlanningResult(params) 
        if (testlegplanning.saved) {
            flash.message = testlegplanning.message
			int next_testid = GetNextTestID(params.testid)
			if (next_testid) {
				redirect(controller:"test",action:'planningtaskresults',id:params.testid,params:[next:next_testid])
			} else {
				redirect(controller:"test",action:'planningtaskresults',id:params.testid)
			}
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

    def updatenext = {
        def testlegplanning = fcService.updateTestLegPlanningResult(params) 
        if (testlegplanning.saved) {
            flash.message = testlegplanning.message

			// search next id
			boolean set_next = false
			TestLegPlanning testlegplanning_nextinstance = null
			TestLegPlanning testlegplanning_nextinstance2 = null
			int leg_no = 0
			for ( TestLegPlanning testlegplanning_instance in TestLegPlanning.findAllByTest(testlegplanning.instance.test,[sort:"id"]) ) {
				if (!testlegplanning_nextinstance) {
					leg_no++
				}
				if (set_next) {
					if (!testlegplanning_nextinstance) {
						testlegplanning_nextinstance = testlegplanning_instance
					} else if (!testlegplanning_nextinstance2) {
						testlegplanning_nextinstance2 = testlegplanning_instance
						break
					}
				}
				if (testlegplanning_instance == testlegplanning.instance) {
					set_next = true
				}
			}
			
			if (testlegplanning_nextinstance2) {
				redirect(action:'edit',id:testlegplanning_nextinstance.id,params:[name:leg_no,next:testlegplanning_nextinstance2.id])
			} else if (testlegplanning_nextinstance) {
				redirect(action:'edit',id:testlegplanning_nextinstance.id,params:[name:leg_no])
			} else {
				redirect(controller:"test",action:'planningtaskresults',id:params.testid)
			}
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
	
	def gotonext = {
		def testlegplanning = fcService.getTestLegPlanningResult(params)
		if (testlegplanning.instance) {
			
			// search next id
			boolean set_next = false
			TestLegPlanning testlegplanning_nextinstance = null
			TestLegPlanning testlegplanning_nextinstance2 = null
			int leg_no = 0
			for ( TestLegPlanning testlegplanning_instance in TestLegPlanning.findAllByTest(testlegplanning.instance.test,[sort:"id"]) ) {
				if (!testlegplanning_nextinstance) {
					leg_no++
				}
				if (set_next) {
					if (!testlegplanning_nextinstance) {
						testlegplanning_nextinstance = testlegplanning_instance
					} else if (!testlegplanning_nextinstance2) {
						testlegplanning_nextinstance2 = testlegplanning_instance
						break
					}
				}
				if (testlegplanning_instance == testlegplanning.instance) {
					set_next = true
				}
			}
			
			if (testlegplanning_nextinstance2) {
				redirect(action:'edit',id:testlegplanning_nextinstance.id,params:[name:leg_no,next:testlegplanning_nextinstance2.id])
			} else if (testlegplanning_nextinstance) {
				redirect(action:'edit',id:testlegplanning_nextinstance.id,params:[name:leg_no])
			} else {
				redirect(controller:"test",action:'planningtaskresults',id:params.testid)
			}
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

	int GetNextTestID(String test_id)
	{
		int nexttest_id = 0
		if (test_id) {
			Test test = Test.get(test_id)
			if (test) {
				boolean set_next = false
				for (Test test_instance2 in Test.findAllByTask(test.task,[sort:'viewpos'])) {
					if (set_next) {
		                nexttest_id = test_instance2.id
						set_next = false
					}
		            if (test_instance2.id == test.id) { // BUG: direkter Klassen-Vergleich geht nicht 
						set_next = true
		            }
				}
			}
		}
		return nexttest_id
	}
}
