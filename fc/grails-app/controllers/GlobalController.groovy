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

    def changelanguage = {
        [globalInstance:BootStrap.global]
    }

    def update = {
    	redirect(controller:'contest',action:'start',params:[lang:params.language])
    }
    
    def cancel = {
        redirect(action:list)
    }
}
