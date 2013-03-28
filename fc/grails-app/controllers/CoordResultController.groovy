class CoordResultController 
{
    
	def fcService
	
    def edit = {
        def coordresult = fcService.getCoordResult(params) 
        if (coordresult.instance) {
            return [coordResultInstance:coordresult.instance]
        } else {
            flash.message = coordresult.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def editprocedureturn = {
        def coordresult = fcService.getCoordResult(params) 
        if (coordresult.instance) {
            return [coordResultInstance:coordresult.instance]
        } else {
            flash.message = coordresult.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def updatereturn = {
        def coordresult = fcService.updateCoordResult(params) 
        if (coordresult.saved) {
            flash.message = coordresult.message
			int next_testid = GetNextTestID(params.testid)
			if (next_testid) {
				redirect(controller:"test",action:'flightresults',id:params.testid,params:[next:next_testid])
			} else {
				redirect(controller:"test",action:'flightresults',id:params.testid)
			}
        } else if (coordresult.instance) {
            if (coordresult.error) {
                flash.message = coordresult.message
                flash.error = true
            }
            render(view:'edit',model:[coordResultInstance:coordresult.instance])
        } else {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        }
    }

    def updatenext = {
        def coordresult = fcService.updateCoordResult(params) 
        if (coordresult.saved) {
            flash.message = coordresult.message

			// search next id
			boolean set_next = false
			CoordResult coordresult_nextinstance = null
			CoordResult coordresult_nextinstance2 = null
			int leg_no = 0
			for ( CoordResult coordresult_instance in CoordResult.findAllByTest(coordresult.instance.test,[sort:"id"]) ) {
				if (!coordresult_nextinstance) {
					if (coordresult_instance.planProcedureTurn) {
						leg_no++
					}
					leg_no++
				}
				if (set_next) {
					if (!coordresult_nextinstance) {
						coordresult_nextinstance = coordresult_instance
					} else if (!coordresult_nextinstance2) {
						coordresult_nextinstance2 = coordresult_instance
						break
					}
				}
				if (coordresult_instance == coordresult.instance) {
					set_next = true
				}
			}
			
			if (coordresult_nextinstance2) {
				redirect(action:'edit',id:coordresult_nextinstance.id,params:[name:leg_no,next:coordresult_nextinstance2.id])
			} else if (coordresult_nextinstance) {
				redirect(action:'edit',id:coordresult_nextinstance.id,params:[name:leg_no])
			} else {
				redirect(controller:"test",action:'flightresults',id:params.testid)
			}
        } else if (coordresult.instance) {
            if (coordresult.error) {
                flash.message = coordresult.message
                flash.error = true
            }
            render(view:'edit',model:[coordResultInstance:coordresult.instance])
        } else {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        }
    }
	
	def gotonext = {
		def coordresult = fcService.getCoordResult(params)
		if (coordresult.instance) {

			// search next id
			boolean set_next = false
			CoordResult coordresult_nextinstance = null
			CoordResult coordresult_nextinstance2 = null
			int leg_no = 0
			for ( CoordResult coordresult_instance in CoordResult.findAllByTest(coordresult.instance.test,[sort:"id"]) ) {
				if (!coordresult_nextinstance) {
					if (coordresult_instance.planProcedureTurn) {
						leg_no++
					}
					leg_no++
				}
				if (set_next) {
					if (!coordresult_nextinstance) {
						coordresult_nextinstance = coordresult_instance
					} else if (!coordresult_nextinstance2) {
						coordresult_nextinstance2 = coordresult_instance
						break
					}
				}
				if (coordresult_instance == coordresult.instance) {
					set_next = true
				}
			}
			
			if (coordresult_nextinstance2) {
				redirect(action:'edit',id:coordresult_nextinstance.id,params:[name:leg_no,next:coordresult_nextinstance2.id])
			} else if (coordresult_nextinstance) {
				redirect(action:'edit',id:coordresult_nextinstance.id,params:[name:leg_no])
			} else {
				redirect(controller:"test",action:'flightresults',id:params.testid)
			}
		}
	}

    def reset = {
        def coordresult = fcService.resetCoordResult(params) 
        if (coordresult.saved) {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        } else if (coordresult.instance) {
            if (coordresult.error) {
                flash.message = coordresult.message
                flash.error = true
            }
            render(view:'edit',model:[coordResultInstance:coordresult.instance])
        } else {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        }
    }

    def updateprocedureturn = {
        def coordresult = fcService.updateCoordResultProcedureTurn(params) 
        if (coordresult.saved) {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        } else if (coordresult.instance) {
            if (coordresult.error) {
                flash.message = coordresult.message
                flash.error = true
            }
            render(view:'edit',model:[coordResultInstance:coordresult.instance])
        } else {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        }
    }

    def cancel = {
        redirect(controller:"test",action:'flightresults',id:params.testid)
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
