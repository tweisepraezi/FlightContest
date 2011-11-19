

class AflosController {

    def fcService
    
    def start = {
        def aflos = fcService.startAflos(params,session.lastAflosController)
        if (aflos.controller) {
            redirect(controller:aflos.controller,action:"list")
        }
    }

}
