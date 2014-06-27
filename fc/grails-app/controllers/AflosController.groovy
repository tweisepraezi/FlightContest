

class AflosController {

    def fcService
    
    def start = {
		fcService.printstart "Start AFLOS"
        def aflos = fcService.startAflos(params,session.lastAflosController)
        if (aflos.controller) {
			fcService.printdone "found"
            redirect(controller:aflos.controller,action:'list')
        } else {
			fcService.printdone ""
			redirect(controller:'contest',action:'start')
		}
    }

	def selectfilename = {
		[:]
    }
	
	def uploadaflosdb = {
		if (session?.lastContest) {
			session.lastContest.refresh()
			def file = request.getFile('uploadfile')
			if (file && !file.empty) {
				String file_name = file.getOriginalFilename()
				fcService.printstart "Upload '$file_name'"
				fcService.println file.getContentType() // "application/msaccess", "application/octet-stream" 
				if (file_name.toLowerCase().endsWith('.mdb')) {
					String upload_file_name = "AFLOS-UPLOAD.mdb"
					file.transferTo(new File(upload_file_name))
					fcService.setaflosuploadedContest(session.lastContest)
		            flash.message = message(code:'fc.uploaded',args:[file_name])
					fcService.printdone flash.message
				} else {
					flash.error = true
					flash.message = message(code:'fc.notuploaded.aflos',args:[file_name])
					fcService.printerror flash.message
				}
			}
	        def aflos = fcService.startAflos(params,session.lastAflosController)
	        if (aflos.controller) {
	            redirect(controller:aflos.controller,action:"list")
	        } else {
				redirect(controller:'contest',action:'start')
			}
		} else {
			redirect(controller:'contest',action:'start')
		}
	}
	
	def cancel = {
        def aflos = fcService.startAflos(params,session.lastAflosController)
        if (aflos.controller) {
            redirect(controller:aflos.controller,action:"list")
        } else {
			redirect(controller:'contest',action:'start')
		}
	}
}
