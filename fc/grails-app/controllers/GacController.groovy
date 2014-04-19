class GacController 
{

    def gacService
    
	def selectgacfilename = {
		[:]
    }
	
	def loadgac = {
		def file = request.getFile('loadgacfile')
		if (file && !file.empty) {
			String file_name = file.getOriginalFilename()
			gacService.printstart "Process '$file_name'"
			gacService.println file.getContentType() // "application/msaccess", "application/octet-stream" 
			if (file_name.toLowerCase().endsWith('.gac')) {
				String upload_file_name = "GAC-UPLOAD.gac"
				String download_file_name = "GAC-DOWNLOAD.gac"
				
				gacService.printstart "Upload $file_name -> $upload_file_name"
				file.transferTo(new File(upload_file_name))
				gacService.printdone ""
				
				if (gacService.RepairTrack(upload_file_name, download_file_name)) {
					String repair_file_name = file_name.substring(0, file_name.length()-4) + '-repaired.gac'
					response.setContentType("application/octet-stream")
					response.setHeader("Content-Disposition", "Attachment;Filename=${repair_file_name}")
					
					gacService.Download(download_file_name, repair_file_name, response.outputStream)
					flash.message = message(code:'fc.gac.repaired',args:[repair_file_name])
					gacService.printdone flash.message
				} else {
					flash.error = true
					flash.message = message(code:'fc.gac.notrepaired',args:[file_name])
					gacService.printdone flash.message
					redirect(controller:'global',action:'list')
				}
			} else {
				flash.error = true
				flash.message = message(code:'fc.gac.gacfileerror',args:[file_name])
				gacService.printerror flash.message
				redirect(controller:'global',action:'list')
			}
		} else {
			redirect(controller:'global',action:'list')
		}
	}
	
	def selectgpxfilename = {
		[:]
    }
	
	def loadgpx = {
		def file = request.getFile('loadgpxfile')
		if (file && !file.empty) {
			String file_name = file.getOriginalFilename()
			gacService.printstart "Process '$file_name'"
			gacService.println file.getContentType() // "application/msaccess", "application/octet-stream" 
			if (file_name.toLowerCase().endsWith('.gpx')) {
				String upload_file_name = "GPX-UPLOAD.gpx"
				String download_file_name = "GAC-DOWNLOAD.gac"
				
				gacService.printstart "Upload $file_name -> $upload_file_name"
				file.transferTo(new File(upload_file_name))
				gacService.printdone ""
				
				if (gacService.ConvertGPX2GAC(upload_file_name, download_file_name)) {
					String gac_file_name = file_name.substring(0, file_name.length()-4) + '-converted.gac'
					response.setContentType("application/octet-stream")
					response.setHeader("Content-Disposition", "Attachment;Filename=${gac_file_name}")
					
					gacService.Download(download_file_name, gac_file_name, response.outputStream)
					flash.message = message(code:'fc.gac.gpxconverted',args:[gac_file_name])
					gacService.printdone flash.message
				} else {
					flash.error = true
					flash.message = message(code:'fc.gac.gpxnotconverted',args:[file_name])
					gacService.printdone flash.message
					redirect(controller:'global',action:'list')
				}
			} else {
				flash.error = true
				flash.message = message(code:'fc.gac.gpxfileerror',args:[file_name])
				gacService.printerror flash.message
				redirect(controller:'global',action:'list')
			}
		} else {
			redirect(controller:'global',action:'list')
		}
	}
	
	def cancel = {
		redirect(controller:'global',action:'list')
	}
}
