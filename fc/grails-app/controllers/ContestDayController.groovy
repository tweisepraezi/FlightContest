

class ContestDayController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def contestday = fcService.getContestDay(params) 
        if (contestday.instance) {
        	return [contestDayInstance:contestday.instance]
        } else {
            flash.message = contestday.message
            redirect(controller:"contest",action:"start")
        }
    }

    def edit = {
        def contestday = fcService.getContestDay(params) 
        if (contestday.instance) {
        	return [contestDayInstance:contestday.instance]
        } else {
            flash.message = contestday.message
            redirect(controller:"contest",action:"start")
        }
    }

    def update = {
        def contestday = fcService.updateContestDay(params) 
        if (contestday.saved) {
        	flash.message = contestday.message
        	redirect(action:show,id:contestday.instance.id)
        } else if (contestday.instance) {
        	render(view:'edit',model:[contestDayInstance:contestday.instance])
        } else {
        	flash.message = contestday.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def contestday = fcService.createContestDay(params)
        return [contestDayInstance:contestday.instance]
    }

    def save = {
        def contestday = fcService.saveContestDay(params) 
        if (contestday.saved) {
        	flash.message = contestday.message
        	if (contestday.fromcontest) {
        		redirect(controller:"contest",action:show,id:params.contestid)
        	} else {
        		redirect(controller:"contest",action:"start")
        	}
        } else {
            render(view:'create',model:[contestDayInstance:contestday.instance])
        }
    }

    def delete = {
        def contestday = fcService.deleteContestDay(params)
        if (contestday.deleted) {
        	flash.message = contestday.message
        	redirect(controller:"contest",action:"start")
        } else if (contestday.notdeleted) {
        	flash.message = contestday.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = contestday.message
        	redirect(controller:"contest",action:"start")
        }
    }

	def cancel = {
        if (params.fromcontest) {
            redirect(controller:"contest",action:show,id:params.contestid)
        } else {
            redirect(controller:"contest",action:"start")
        }
	}
	
	def createdaytask = {
        def contestday = fcService.getContestDay(params) 
        redirect(controller:'contestDayTask',action:'create',params:['contestday.id':contestday.instance.id,'contestdayid':contestday.instance.id,'fromcontestday':true])
	}
}
