

class AflosController {

    def fcService
    
    def start = {
		fcService.printstart "Start AFLOS"
        def aflos = fcService.startAflos(params,session.lastAflosController)
        if (aflos.controller) {
			fcService.printdone "found"
            redirect(controller:aflos.controller,action:"list")
        } else {
			fcService.printdone ""
			redirect(controller:'contest',action:'start')
		}
    }

}
