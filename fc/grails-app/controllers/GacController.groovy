class GacController 
{

    def gpxService
    
	def selectgacfilename = {
		[repairtracks:false,identicaltimes:false]
    }
	
	def loadgac = {
		def file = request.getFile('loadgacfile')
		if (file && !file.empty) {
			String file_name = file.getOriginalFilename()
			gpxService.printstart "Process '$file_name'"
			gpxService.println file.getContentType() // "application/msaccess", "application/octet-stream" 
			if (file_name.toLowerCase().endsWith('.gac')) {
				String uuid = UUID.randomUUID().toString()
				String upload_file_name = "GAC-${uuid}-UPLOAD.gac"
				String download_file_name = "GAC-${uuid}-DOWNLOAD.gac"
				
				gpxService.printstart "Upload $file_name -> $upload_file_name"
				file.transferTo(new File(upload_file_name))
				gpxService.printdone ""
				
				boolean repair_tracks = params?.repairtracks == 'on'
				boolean repair_identicaltimes = params?.identicaltimes == 'on'
				if (gpxService.RepairGAC(upload_file_name, download_file_name, repair_tracks, repair_identicaltimes)) {
					String repair_file_name = file_name.substring(0, file_name.length()-4) + '-repaired.gac'
					response.setContentType("application/octet-stream")
					response.setHeader("Content-Disposition", "Attachment;Filename=${repair_file_name}")
					
					if (gpxService.Download(download_file_name, repair_file_name, response.outputStream)) {
						flash.message = message(code:'fc.gac.repaired',args:[repair_file_name])
					} else {
						flash.message = message(code:'fc.gac.notrepaired',args:[repair_file_name])
					}
					gpxService.DeleteFile(upload_file_name)
					gpxService.DeleteFile(download_file_name)
					gpxService.printdone flash.message
				} else {
					gpxService.DeleteFile(upload_file_name)
					gpxService.DeleteFile(download_file_name)
					flash.error = true
					flash.message = message(code:'fc.gac.notrepaired',args:[file_name])
					gpxService.printdone flash.message
					redirect(controller:'global',action:'info')
				}
			} else {
				flash.error = true
				flash.message = message(code:'fc.gac.gacfileerror',args:[file_name])
				gpxService.printerror flash.message
				redirect(controller:'global',action:'info')
			}
		} else {
			redirect(controller:'global',action:'info')
		}
	}
	
	def selectgpxfilename = {
		[:]
    }
	
	def loadgpx = {
		def file = request.getFile('loadgpxfile')
		if (file && !file.empty) {
			String file_name = file.getOriginalFilename()
			gpxService.printstart "Process '$file_name'"
			gpxService.println file.getContentType() // "application/msaccess", "application/octet-stream" 
			if (file_name.toLowerCase().endsWith('.gpx')) {
				String uuid = UUID.randomUUID().toString()
				String upload_file_name = "GPX-${uuid}-UPLOAD.gpx"
				String download_file_name = "GPX-${uuid}-DOWNLOAD.gac"
				
				gpxService.printstart "Upload $file_name -> $upload_file_name"
				file.transferTo(new File(upload_file_name))
				gpxService.printdone ""
				
                Map convert_ret = GPX2GAC.Convert(upload_file_name, download_file_name)
				if (convert_ret.converted) {
					String gac_file_name = file_name.substring(0, file_name.length()-4) + '-converted.gac'
					response.setContentType("application/octet-stream")
					response.setHeader("Content-Disposition", "Attachment;Filename=${gac_file_name}")
					
					if (gpxService.Download(download_file_name, gac_file_name, response.outputStream)) {
						flash.message = message(code:'fc.gac.gpxconverted',args:[gac_file_name])
					} else {
						flash.message = message(code:'fc.gac.gpxnotconverted',args:[gac_file_name])
					}
					gpxService.DeleteFile(upload_file_name)
					gpxService.DeleteFile(download_file_name)
					gpxService.printdone flash.message
				} else {
					flash.error = true
                    if (!convert_ret.onetrack) {
                        flash.message = message(code:'fc.gac.gpxnotconverted.multipletracks',args:[file_name])
                    } else {
                        flash.message = message(code:'fc.gac.gpxnotconverted',args:[file_name,convert_ret.errmsg])
                    }
					gpxService.DeleteFile(upload_file_name)
					gpxService.DeleteFile(download_file_name)
					gpxService.printdone flash.message
					redirect(controller:'global',action:'info')
				}
			} else {
				flash.error = true
				flash.message = message(code:'fc.gac.gpxfileerror',args:[file_name])
				gpxService.printerror flash.message
				redirect(controller:'global',action:'info')
			}
		} else {
			redirect(controller:'global',action:'info')
		}
	}
	
	def cancel = {
		redirect(controller:'global',action:'info')
	}
}
