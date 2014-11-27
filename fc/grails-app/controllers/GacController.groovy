class GacController 
{

    def gacService
    
	def selectgacfilename = {
		[repairtracks:false,identicaltimes:false]
    }
	
	def loadgac = {
		def file = request.getFile('loadgacfile')
		if (file && !file.empty) {
			String file_name = file.getOriginalFilename()
			gacService.printstart "Process '$file_name'"
			gacService.println file.getContentType() // "application/msaccess", "application/octet-stream" 
			if (file_name.toLowerCase().endsWith('.gac')) {
				String uuid = UUID.randomUUID().toString()
				String upload_file_name = "GAC-${uuid}-UPLOAD.gac"
				String download_file_name = "GAC-${uuid}-DOWNLOAD.gac"
				
				gacService.printstart "Upload $file_name -> $upload_file_name"
				file.transferTo(new File(upload_file_name))
				gacService.printdone ""
				
				boolean repair_tracks = params?.repairtracks == 'on'
				boolean repair_identicaltimes = params?.identicaltimes == 'on'
				if (gacService.RepairGAC(upload_file_name, download_file_name, repair_tracks, repair_identicaltimes)) {
					String repair_file_name = file_name.substring(0, file_name.length()-4) + '-repaired.gac'
					response.setContentType("application/octet-stream")
					response.setHeader("Content-Disposition", "Attachment;Filename=${repair_file_name}")
					
					if (gacService.Download(download_file_name, repair_file_name, response.outputStream)) {
						flash.message = message(code:'fc.gac.repaired',args:[repair_file_name])
					} else {
						flash.message = message(code:'fc.gac.notrepaired',args:[repair_file_name])
					}
					gacService.DeleteFile(upload_file_name)
					gacService.DeleteFile(download_file_name)
					gacService.printdone flash.message
				} else {
					gacService.DeleteFile(upload_file_name)
					gacService.DeleteFile(download_file_name)
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
				String uuid = UUID.randomUUID().toString()
				String upload_file_name = "GPX-${uuid}-UPLOAD.gpx"
				String download_file_name = "GPX-${uuid}-DOWNLOAD.gac"
				
				gacService.printstart "Upload $file_name -> $upload_file_name"
				file.transferTo(new File(upload_file_name))
				gacService.printdone ""
				
				if (gacService.ConvertGPX2GAC(upload_file_name, download_file_name)) {
					String gac_file_name = file_name.substring(0, file_name.length()-4) + '-converted.gac'
					response.setContentType("application/octet-stream")
					response.setHeader("Content-Disposition", "Attachment;Filename=${gac_file_name}")
					
					if (gacService.Download(download_file_name, gac_file_name, response.outputStream)) {
						flash.message = message(code:'fc.gac.gpxconverted',args:[gac_file_name])
					} else {
						flash.message = message(code:'fc.gac.gpxnotconverted',args:[gac_file_name])
					}
					gacService.DeleteFile(upload_file_name)
					gacService.DeleteFile(download_file_name)
					gacService.printdone flash.message
				} else {
					flash.error = true
					flash.message = message(code:'fc.gac.gpxnotconverted',args:[file_name])
					gacService.DeleteFile(upload_file_name)
					gacService.DeleteFile(download_file_name)
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
