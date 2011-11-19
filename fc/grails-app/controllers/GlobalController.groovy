class GlobalController {

    def index = { 
    	redirect(action:list,params:params) 
    }

    def list = {
    	[globalInstance:BootStrap.global,contestInstance:session.lastContest]
    }

    def listall = {
            [globalInstance:BootStrap.global,contestInstance:session.lastContest]
        }

    def changecontest = {
    	[globalInstance:BootStrap.global]
    }

    def changelanguage = {
        [globalInstance:BootStrap.global]
    }

    def update = {
    	redirect(controller:'contest',action:'start',params:[lang:params.language])
    }
    
    def updatecontest = {
    	session.lastContest = Contest.get(params.contestid)
    	session.lastContestDayTaskPlanning = null
    	session.lastContestDayTaskResults = null
        redirect(controller:'contest',action:'start')
    }
        
    def cancel = {
        redirect(action:list)
    }
}
