class TestController 
{
	def fcService

    def show = {
        def test = fcService.getTest(params) 
        if (test.instance) {
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"contest",action:"start")
        }
    }

    def flightplan = {
        def test = fcService.getTest(params) 
        if (test.instance) {
        	return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startplanning")
        }
    }

    def flightplanprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getTest(params) 
        if (test.instance) {
        	return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startplanning")
        }
    }

    def printflightplan = {
        def test = fcService.printflightplanTest(params,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startplanning")
        } else if (test.error) {
        	flash.message = test.message
        	flash.error = true
        	redirect(action:show,id:test.instance.id)
        } else if (test.content) {
        	fcService.WritePDF(response,test.content)
        } else {
        	redirect(action:show,id:test.instance.id)
        }
    }

    def planningtask = {
        def test = fcService.getTest(params) 
        if (test.instance) {
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startplanning")
        }
    }

    def planningtaskprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getTest(params) 
        if (test.instance) {
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startplanning")
        }
    }

    def printplanningtask = {
        def test = fcService.printplanningtaskTest(params,GetPrintParams(),false) // false - without result 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startplanning")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content)
        } else {
            redirect(action:show,id:test.instance.id)
        }
    }

	def printplanningtaskresult = {
        def test = fcService.printplanningtaskTest(params,GetPrintParams(),true) // true - with result
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startplanning")
        } else if (test.error) {
            flash.message = test.message
            flash.error = true
            redirect(action:show,id:test.instance.id)
        } else if (test.content) {
            fcService.WritePDF(response,test.content)
        } else {
            redirect(action:show,id:test.instance.id)
        }
	}
	
    def planningtaskresults = {
        def test = fcService.getTest(params) 
        if (test.instance) {
            return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startresults")
        }
    }

    def results = {
        def test = fcService.getTest(params) 
        if (test.instance) {
        	return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startresults")
        }
    }

    def resultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def test = fcService.getTest(params) 
        if (test.instance) {
        	return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startresults")
        }
    }

    def printresults = {
        def test = fcService.printresultsTest(params,GetPrintParams()) 
        if (!test.instance) {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startresults")
        } else if (test.error) {
        	flash.message = test.message
        	flash.error = true
        	redirect(action:show,id:test.instance.id)
        } else if (test.content) {
        	fcService.WritePDF(response,test.content)
        } else {
        	redirect(action:show,id:test.instance.id)
        }
    }

    def editresults = {
        def test = fcService.getTest(params) 
        if (test.instance) {
        	return [testInstance:test.instance]
        } else {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"startresults")
        }
    }

    def updateresults = {
        def test = fcService.updateresultsTest(params) 
        if (test.saved) {
        	flash.message = test.message
        	redirect(action:results,id:test.instance.id)
        } else if (test.instance) {
        	render(view:'editresults',model:[testInstance:test.instance])
        } else {
        	flash.message = test.message
            redirect(action:editresults,id:params.id)
        }
    }

	def planningtaskresultscomplete = {
        def test = fcService.planningtaskresultscompleteTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(controller:"contestDayTask",action:"listresults",id:test.instance.contestdaytask.id)
        }
	}
	
    def planningtaskresultsreopen = {
        def test = fcService.planningtaskresultsopenTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        }
	}
	
	def planningtaskresultsgiventolateon = {
        def test = fcService.planningtaskresultsgiventolateonTest(params) 
        if (test.error) {
        	flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        }
    }
	
	def planningtaskresultsgiventolateoff = {
        def test = fcService.planningtaskresultsgiventolateoffTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        }
	}
	
	def planningtaskresultsexitroomtolateon = {
        def test = fcService.planningtaskresultsexitroomtolateonTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        }
	}
	
	def planningtaskresultsexitroomtolateoff = {
        def test = fcService.planningtaskresultsexitroomtolateoffTest(params) 
        if (test.error) {
            flash.error = true
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        } else {
            flash.message = test.message
            redirect(action:planningtaskresults,id:params.id)
        }
	}
	    
    Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'
               ]
    }
}
