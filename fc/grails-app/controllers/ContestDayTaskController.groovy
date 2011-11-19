
import org.xhtmlrenderer.pdf.ITextRenderer


class ContestDayTaskController {

	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def contestdaytask = fcService.getContestDayTask(params) 
        if (contestdaytask.instance) {
        	return [contestDayTaskInstance:contestdaytask.instance]
        } else {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        }
    }

    def edit = {
        def contestdaytask = fcService.getContestDayTask(params) 
        if (contestdaytask.instance) {
        	return [contestDayTaskInstance:contestdaytask.instance]
        } else {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        }
    }

    def update = {
        def contestdaytask = fcService.updateContestDayTask(params) 
        if (contestdaytask.saved) {
        	flash.message = contestdaytask.message
        	redirect(action:show,id:contestdaytask.instance.id)
        } else if (contestdaytask.instance) {
        	render(view:'edit',model:[contestDayTaskInstance:contestdaytask.instance])
        } else {
        	flash.message = contestdaytask.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def contestdaytask = fcService.createContestDayTask(params) 
        return [contestDayTaskInstance:contestdaytask.instance]
    }

    def save = {
        def contestdaytask = fcService.saveContestDayTask(params) 
        if (contestdaytask.saved) {
        	flash.message = contestdaytask.message
        	if (contestdaytask.fromcontestday) {
        		redirect(controller:"contestDay",action:show,id:params.contestdayid)
        	} else {
        		redirect(controller:"contest",action:"start")
        	}
        } else {
            render(view:'create',model:[contestDayTaskInstance:contestdaytask.instance])
        }
    }
    
    def delete = {
        def contestdaytask = fcService.deleteContestDayTask(params)
        if (contestdaytask.deleted) {
        	flash.message = contestdaytask.message
        	redirect(controller:"contest",action:"start")
        } else if (contestdaytask.notdeleted) {
        	flash.message = contestdaytask.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = contestdaytask.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
	def cancel = {
        if (params.fromcontestday) {
            redirect(controller:"contestDay",action:show,id:params.contestdayid)
        } else {
            redirect(controller:"contest",action:"start")
        }
	}

	def createplanningtest = {
        def contestdaytask = fcService.getContestDayTask(params)
        redirect(controller:'planningTest',action:'create',params:['contestdaytask.id':contestdaytask.instance.id,'contestdaytaskid':contestdaytask.instance.id,'fromcontestdaytask':true])
	}
	
	def createflighttest = {
        def contestdaytask = fcService.getContestDayTask(params) 
        redirect(controller:'flightTest',action:'create',params:['contestdaytask.id':contestdaytask.instance.id,'contestdaytaskid':contestdaytask.instance.id,'fromcontestdaytask':true])
	}
	
	def createlandingtest = {
        def contestdaytask = fcService.getContestDayTask(params) 
        redirect(controller:'landingTest',action:'create',params:['contestdaytask.id':contestdaytask.instance.id,'contestdaytaskid':contestdaytask.instance.id,'fromcontestdaytask':true])
	}
	
	def createspecialtest = {
        def contestdaytask = fcService.getContestDayTask(params) 
        redirect(controller:'specialTest',action:'create',params:['contestdaytask.id':contestdaytask.instance.id,'contestdaytaskid':contestdaytask.instance.id,'fromcontestdaytask':true])
	}
	
    def startplanning = {
		def contestdaytask = fcService.startplanningContestDayTask(params,session.lastContest,session.lastContestDayTaskPlanning)
		if (contestdaytask.contestdaytaskid) {
   			params.id = contestdaytask.contestdaytaskid
   			redirect(action:listplanning,params:params)
   		}
    }

    def listplanning = {
        def contestdaytask = fcService.getContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else {
            session.lastContestDayTaskPlanning = contestdaytask.instance.id
        	return [contestDayTaskInstance:contestdaytask.instance]
        }
    }

    def startresults = {
        def contestdaytask = fcService.startresultsContestDayTask(params,session.lastContest,session.lastContestDayTaskResults)
        if (contestdaytask.contestdaytaskid) {
            params.id = contestdaytask.contestdaytaskid
            redirect(action:listresults,params:params)
        }
    }

    def listresults = {
        def contestdaytask = fcService.getContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else {
            session.lastContestDayTaskResults = contestdaytask.instance.id
            return [contestDayTaskInstance:contestdaytask.instance]
        }
    }

	def selectall = {
        def contestdaytask = fcService.selectallContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
            return
        } else {
        	flash.selectedTestIDs = contestdaytask.selectedtestids
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        }
    }

    def deselectall = {
        def contestdaytask = fcService.getContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else {
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        }
    }
    
    def assignplanningtesttask = {
        def contestdaytask = fcService.assignplanningtesttaskContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else if (contestdaytask.error) {
        	flash.message = contestdaytask.message
        	flash.error = true
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        } else if (contestdaytask.testinstanceids?.size > 1) {
        	flash.testInstanceIDs = contestdaytask.testinstanceids
        	redirect(action:selectplanningtesttask,id:contestdaytask.instance.id)
        } else {
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        }
    }
    
    def selectplanningtesttask = {
        def contestdaytask = fcService.getContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else {
        	contestdaytask.instance.properties = params
        	return ['contestDayTaskInstance':contestdaytask.instance,'testInstanceIDs':flash.testInstanceIDs]
        }
    }
    
    def setplanningtesttask = {
        def contestdaytask = fcService.setplanningtesttaskContestDayTask(params) 
        flash.message = contestdaytask.message
        if (!contestdaytask.instance) {
            redirect(controller:"contest",action:"start")
        } else {
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        }
    }
    
    def assignflighttestwind = {
        def contestdaytask = fcService.assignflighttestwindContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else if (contestdaytask.error) {
        	flash.message = contestdaytask.message
           	flash.error = true
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        } else if (contestdaytask.testinstanceids?.size > 1) {
        	flash.testInstanceIDs = contestdaytask.testinstanceids
        	redirect(action:selectflighttestwind,id:contestdaytask.instance.id)
        } else {
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        }
    }

    def selectflighttestwind = {
        def contestdaytask = fcService.getContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else {
        	contestdaytask.instance.properties = params
        	return ['contestDayTaskInstance':contestdaytask.instance,'testInstanceIDs':flash.testInstanceIDs]
        }
    }
    
    def setflighttestwind = {
        def contestdaytask = fcService.setflighttestwindContestDayTask(params) 
        flash.message = contestdaytask.message
        if (!contestdaytask.instance) {
            redirect(controller:"contest",action:"start")
        } else {
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        }
    }

	def calculatesequence = {
        def contestdaytask = fcService.calculatesequenceContestDayTask(params) 
        flash.message = contestdaytask.message
        if (!contestdaytask.instance) {
            redirect(controller:"contest",action:"start")
        } else {
            flash.error = contestdaytask.error
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        }
	}

	def moveup = {
        def contestdaytask = fcService.moveupContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else if (contestdaytask.borderreached) {
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        } else if (contestdaytask.error) {
       		flash.message = contestdaytask.message
            flash.error = true
            redirect(action:listplanning,id:contestdaytask.instance.id)
        } else { 
	        if (contestdaytask.selectedtestids) {
	    		flash.selectedTestIDs = contestdaytask.selectedtestids
	    	}
	        redirect(action:listplanning,id:contestdaytask.instance.id)
        }
	}
	
	def movedown = {
        def contestdaytask = fcService.movedownContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else if (contestdaytask.borderreached) {
            redirect(action:listplanning,id:contestdaytask.instance.id)
        } else if (contestdaytask.error) {
       		flash.message = contestdaytask.message
            flash.error = true
            redirect(action:listplanning,id:contestdaytask.instance.id)
        } else {
	    	if (contestdaytask.selectedtestids) {
	    		flash.selectedTestIDs = contestdaytask.selectedtestids
	    	}
	        redirect(action:listplanning,id:contestdaytask.instance.id)
        }
	}
	
	def calculatetimetable = {
        def contestdaytask = fcService.calculatetimetableContestDayTask(params) 
        flash.message = contestdaytask.message
        if (!contestdaytask.instance) {
            redirect(controller:"contest",action:"start")
        } else {
			if (contestdaytask.error) {
				flash.error = true
			}
        	redirect(action:listplanning,id:contestdaytask.instance.id)
		}
	}

	def timeadd = {
        def contestdaytask = fcService.timeaddContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else {
	   		flash.selectedTestIDs = contestdaytask.selectedtestids
	    	redirect(action:listplanning,id:contestdaytask.instance.id)
        }
	}

	def timesubtract = {
        def contestdaytask = fcService.timesubtractContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else {
	   		flash.selectedTestIDs = contestdaytask.selectedtestids
	        redirect(action:listplanning,id:contestdaytask.instance.id)
        }
	}

	def timetableprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def contestdaytask = fcService.getContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else {
        	return [contestDayTaskInstance:contestdaytask.instance ]
        }
    }

	def printtimetable = {
        def contestdaytask = fcService.printtimetableContestDayTask(params,GetPrintParams()) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else if (contestdaytask.error) {
        	flash.message = contestdaytask.message
           	flash.error = true
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        } else if (contestdaytask.content) {
        	fcService.WritePDF(response,contestdaytask.content)
	    } else {
        	redirect(action:listplanning,id:contestdaytask.instance.id)
	    }
	}
	
	def printflightplans = {
        def contestdaytask = fcService.printflightplansContestDayTask(params,GetPrintParams()) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else if (contestdaytask.error) {
        	flash.message = contestdaytask.message
           	flash.error = true
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        } else if (contestdaytask.content) {
        	fcService.WritePDF(response,contestdaytask.content)
	    } else {
        	redirect(action:listplanning,id:contestdaytask.instance.id)
	    }
	}

    def printplanningtesttask = {
        def contestdaytask = fcService.printplanningtasksContestDayTask(params,GetPrintParams()) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else if (contestdaytask.error) {
            flash.message = contestdaytask.message
        	flash.error = true
        	redirect(action:listplanning,id:contestdaytask.instance.id)
        } else if (contestdaytask.content) {
        	fcService.WritePDF(response,contestdaytask.content)
	    } else {
	        redirect(action:listplanning,id:contestdaytask.instance.id)
		}
	}

	def calculatepositions = {
        def contestdaytask = fcService.calculatepositionsContestDayTask(params) 
        flash.message = contestdaytask.message
        if (!contestdaytask.instance) {
            redirect(controller:"contest",action:"start")
        } else {
			if (contestdaytask.error) {
				flash.error = true
			}
        	redirect(action:listresults,id:contestdaytask.instance.id)
		}
	}

	def printresults = {
        def contestdaytask = fcService.printresultsContestDayTask(params,GetPrintParams()) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else if (contestdaytask.error) {
        	flash.message = contestdaytask.message
           	flash.error = true
        	redirect(action:listresults,id:contestdaytask.instance.id)
        } else if (contestdaytask.content) {
        	fcService.WritePDF(response,contestdaytask.content)
	    } else {
        	redirect(action:listresults,id:contestdaytask.instance.id)
	    }
	}

	def positionsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def contestdaytask = fcService.getContestDayTask(params) 
        if (!contestdaytask.instance) {
            flash.message = contestdaytask.message
            redirect(controller:"contest",action:"start")
        } else {
        	return [contestDayTaskInstance:contestdaytask.instance ]
        }
    }

	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'
               ]
    }
}

