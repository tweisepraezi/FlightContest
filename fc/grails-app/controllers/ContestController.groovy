

class ContestController {
    
	def fcService

	def index = { redirect(action:start,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def start = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ contestInstanceList: Contest.list( params ), contestInstanceTotal: Contest.count() ]
    }

    def show = {
        def contest = fcService.getContest(params) 
        if (contest.instance) {
        	return [contestInstance:contest.instance]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def edit = {
        def contest = fcService.getContest(params) 
        if (contest.instance) {
        	return [contestInstance:contest.instance]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def update = {
        def contest = fcService.updateContest(params) 
        if (contest.saved) {
        	flash.message = contest.message
        	redirect(action:start,id:contest.instance.id)
        } else if (contest.instance) {
        	render(view:'edit',model:[contestInstance:contest.instance])
        } else {
        	flash.message = contest.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def contest = fcService.createContest(params)
        if (contest.created) {
	        return [contestInstance:contest.instance]
    	} else {
    		flash.message = contest.message
    		redirect(action:start)
    	}
    }

    def save = {
        def contest = fcService.saveContest(params) 
        if (contest.saved) {
        	flash.message = contest.message
        	redirect(action:start,id:contest.instance.id)
        } else {
            render(view:'create',model:[contestInstance:contest.instance])
        }
    }

    def delete = {
        def contest = fcService.deleteContest(params)
        if (contest.deleted) {
        	flash.message = contest.message
            redirect(action:start)
        } else if (contest.notdeleted) {
        	flash.message = contest.message
            redirect(action:start,id:params.id)
        } else {
        	flash.message = contest.message
        	redirect(action:start)
        }
    }

	def cancel = {
        redirect(action:start)
	}
	
	def createday = {
		def contest = fcService.getContest(params)
		redirect(controller:'contestDay',action:'create',params:['contest.id':contest.instance.id,'contestid':contest.instance.id,'fromcontest':true])
	}

    def createtest = {
		if (Contest.count()) {
			redirect(uri:"")
			return
		}
		
        def contest = fcService.putContest("Test-Wettbewerb 2009")
        def contestday1 = fcService.putContestDay(contest,"")
        def contestdaytask1 = fcService.putContestDayTask(contestday1,"")

        def crew1 = fcService.putCrew("Becker","","Deutschland","D-EBEC","C172","rot",110.0f)
        def crew2 = fcService.putCrew("Meier","","Deutschland","D-EMEI","C172","blau",130.0f)
        def crew3 = fcService.putCrew("Schulze","","Deutschland","","","",140.0f)
        def crew4 = fcService.putCrew("Schmidt","","Deutschland","","","",140.0f)
        def crew5 = fcService.putCrew("Richter","","Deutschland","","","",140.0f)
        def crew6 = fcService.putCrew("Roethke","Weise","Deutschland","D-MWCB","","",60.0f)
        
        def aircraft3 = fcService.putAircraft("D-ESCH","C172","gelb",120.0f)
        
        fcService.setownaircraftCrew(crew3,aircraft3)
        
        fcService.setusedaircraftCrew(crew4,aircraft3.instance,115.0f)
        fcService.setusedaircraftCrew(crew5,crew1.instance.ownAircraft,120.0f)
        
        def route1 = fcService.putRoute("")
    	fcService.putRouteCoord(route1,52,00,'N',14,00,'E')
    	fcService.putRouteCoord(route1,52,20,'N',14,20,'E')
    	fcService.putRouteCoord(route1,52,35,'N',13,50,'E')
    	
        def route2 = fcService.putRoute("")
    	fcService.putRouteCoord(route2,54,00,'N',16,00,'E')
    	fcService.putRouteCoord(route2,53,30,'N',16,10,'E')
    	fcService.putRouteCoord(route2,52,55,'N',15,50,'E')

        def route3 = fcService.putRoute("Strecke-3 (LWB-2009)")
    	fcService.putRouteCoord(route3,51,26.7464,'N',13,51.6109,'E')
    	fcService.putRouteCoord(route3,51,23.3484,'N',14,5.3033,'E')
    	fcService.putRouteCoord(route3,51,18.2738,'N',14,25.1232,'E')
    	fcService.putRouteCoord(route3,51,26.7081,'N',14,8.17,'E')
    	fcService.putRouteCoord(route3,51,31.0965,'N',14,17.1596,'E')
    	fcService.putRouteCoord(route3,51,29.3749,'N',13,52.7632,'E')

    	def navtest1 = fcService.putNavTest(contestdaytask1,"")
    	def navtesttask1 = fcService.putNavTestTask(navtest1,"",route3,60.0f,90,10)
    	
    	def flighttest1 = fcService.putFlightTest(contestdaytask1,"",route2)
    	def flighttestwind1 = fcService.putFlightTestWind(flighttest1,234,8)

    	def landingtest1 = fcService.putLandingTest(contestdaytask1,"")
    	fcService.putLandingTestTask(landingtest1,"")
    	fcService.putLandingTestTask(landingtest1,"")
    	fcService.putLandingTestTask(landingtest1,"")
    	
    	def specialtest1 = fcService.putSpecialTest(contestdaytask1,"")
    	fcService.putSpecialTestTask(specialtest1,"")
    	fcService.putSpecialTestTask(specialtest1,"")
    	fcService.putSpecialTestTask(specialtest1,"")
    	
    	fcService.putnavtesttaskContestDayTask(contestdaytask1,navtesttask1)
    	fcService.putflighttestwindContestDayTask(contestdaytask1,flighttestwind1)
    	fcService.runcalculatesequenceContestDayTask(contestdaytask1)
    	fcService.runcalculatetimetableContestDayTask(contestdaytask1)
    	
    	fcService.putResults(contestdaytask1,crew1,170,190,90,60)
    	fcService.putResults(contestdaytask1,crew2,220,220,20,50)
    	fcService.putResults(contestdaytask1,crew3,180,210,60,40)
    	fcService.putResults(contestdaytask1,crew4,390,315,40,20)
    	fcService.putResults(contestdaytask1,crew5,140,175,70,80)
    	fcService.runcalculatepositionsContestDayTask(contestdaytask1)
    	
    	redirect(controller:'contestDayTask',action:start)
    	/*
        if (contest.saved) {
        	flash.message = contest.message
        	redirect(action:start,id:contest.instance.id)
        } else {
            render(view:'create',model:[contestInstance:contest.instance])
        }
        */
    }

}
