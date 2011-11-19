

class SpecialTestController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def specialtest = fcService.getSpecialTest(params) 
        if (specialtest.instance) {
        	return [specialTestInstance:specialtest.instance]
        } else {
            flash.message = specialtest.message
            redirect(controller:"contest",action:"start")
        }
    }

    def edit = {
        def specialtest = fcService.getSpecialTest(params) 
        if (specialtest.instance) {
        	return [specialTestInstance:specialtest.instance]
        } else {
            flash.message = specialtest.message
            redirect(controller:"contest",action:"start")
        }
    }

    def update = {
        def specialtest = fcService.updateSpecialTest(params) 
        if (specialtest.saved) {
        	flash.message = specialtest.message
        	redirect(action:show,id:specialtest.instance.id)
        } else if (specialtest.instance) {
        	render(view:'edit',model:[specialTestInstance:specialtest.instance])
        } else {
        	flash.message = specialtest.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def specialtest = fcService.createSpecialTest(params)
       	return [specialTestInstance:specialtest.instance]
    }

    def save = {
        def specialtest = fcService.saveSpecialTest(params) 
        if (specialtest.saved) {
        	flash.message = specialtest.message
            if (specialtest.fromcontestdaytask) {
            	redirect(controller:"contestDayTask",action:show,id:specialtest.contestdaytaskid)
            } else {
            	redirect(controller:"contest",action:"start")
            }
        } else {
            render(view:'create',model:[specialTestInstance:specialtest.instance])
        }
    }

    def delete = {
        def specialtest = fcService.deleteSpecialTest(params)
        if (specialtest.deleted) {
        	flash.message = specialtest.message
        	redirect(controller:"contest",action:"start")
        } else if (specialtest.notdeleted) {
        	flash.message = specialtest.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = specialtest.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
	def cancel = {
        if (params.fromcontestdaytask) {
            redirect(controller:"contestDayTask",action:show,id:params.contestdaytaskid)
        } else {
            redirect(controller:"contest",action:"start")
        }
	}
	
	def createspecialtesttask = {
        def specialtest = fcService.getSpecialTest(params) 
        redirect(controller:'specialTestTask',action:'create',params:['specialTest.id':specialtest.instance.id,'specialtestid':specialtest.instance.id])
	}
}
