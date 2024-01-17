import java.util.zip.*
import org.springframework.web.multipart.MultipartFile

class MapController {
    def fcService
    def printService
    def gpxService
    
    def list = {
		fcService.printstart "List maps"
        if (session?.lastContest) {
			session.lastContest.refresh()
            
            def map_list = []
            String map_folder_name = "${servletContext.getRealPath('/')}${Defs.ROOT_FOLDER_MAP}/${session.lastContest.contestUUID}/"
            File map_folder = new File(map_folder_name)
            if (map_folder.exists()) {
                map_folder.traverse { File file ->
                    if (file.name.endsWith('.png') && !file.name.endsWith('.warped.png')) {
                        Map map_entry = [name:"",title:"", localref:"",top:0,bottom:0,right:0,left:0,projection:"",landscape:false,size:"",tif:false]
                        map_entry.name = file.name
                        map_entry.title = file.name.substring(0,file.name.size()-4)
                        //map_entry.localref = "http://localhost:8080/fc/map/${session.lastContest.contestUUID}/${map_entry.title}.warped.png"
                        map_entry.localref = "http://localhost:8080/fc/map/${session.lastContest.contestUUID}/${map_entry.title}.png"
                        File map_tif = new File("${map_folder_name}${map_entry.title}.tif")
                        if (map_tif.exists()) {
                            map_entry.tif = true
                        }
                        File map_info = new File("${map_folder_name}${file.name}info")
                        if (map_info.exists()) {
                            LineNumberReader map_info_reader = map_info.newReader()
                            while (true) {
                                String value = map_info_reader.readLine()
                                if (value) {
                                    List value_list = value.split(':')
                                    if (value_list.size() == 2) {
                                        String v = value_list[1].trim()
                                        if (value.startsWith("Top(Lat):") && v.isBigDecimal()) {
                                            map_entry.top = v.toBigDecimal()
                                        } else if (value.startsWith("Bottom(Lat):") && v.isBigDecimal()) {
                                            map_entry.bottom = v.toBigDecimal()
                                        } else if (value.startsWith("Right(Lon):") && v.isBigDecimal()) {
                                            map_entry.right = v.toBigDecimal()
                                        } else if (value.startsWith("Left(Lon):") && v.isBigDecimal()) {
                                            map_entry.left = v.toBigDecimal()
                                        } else if (value.startsWith("Projection:")) {
                                            map_entry.projection = v
                                        } else if (value.startsWith("Landscape:")) {
                                            if (v == "true") {
                                                map_entry.landscape = true
                                            }
                                        } else if (value.startsWith("Size:")) {
                                            map_entry.size = v
                                        }
                                    }
                                } else {
                                    break
                                }
                            }
                            map_info_reader.close()
                        }
                        map_list += map_entry
                    }
                }
            }
			fcService.printdone "last contest"
            session.planningtesttaskReturnAction = actionName
            session.planningtesttaskReturnController = controllerName
            session.planningtesttaskReturnID = params.id
            session.flighttestReturnAction = actionName
            session.flighttestReturnController = controllerName
            session.flighttestReturnID = params.id
            return [mapList:map_list]
        }
		fcService.printdone ""
        redirect(controller:'contest',action:'start')
    }

    def start_taskcreator = {
        String task_creator_url = "http://localhost:8080/fc/taskcreator/run.html?lang=en&admin" // local task creator
        if (BootStrap.global.IsTaskCreator()) {
            task_creator_url = grailsApplication.config.flightcontest.taskcreator.url
        }
        fcService.println "Task creator: $task_creator_url"
        
        if (params.localref && params.top && params.bottom && params.right && params.left) {
            String login_url = "${task_creator_url}&loadurlmap=%22${params.localref}%22,${params.top},${params.bottom},${params.right},${params.left}"
            fcService.println "Open $login_url"
            render(view:"taskcreator", model:[login_url:login_url])
        } else {
            render(view:"taskcreator", model:[login_url:task_creator_url])
        }
    }
    
    def taskcreator = {
        return [:]
    }

    def download_png = {
        if (session?.lastContest) {
            String map_png_file_name = "${servletContext.getRealPath('/')}${Defs.ROOT_FOLDER_MAP}/${session.lastContest.contestUUID}/${params.name}"
            gpxService.printstart "Download PNG"
            String map_file_name = (params.title + '.png').replace(' ',"_")
            response.setContentType("application/octet-stream")
            response.setHeader("Content-Disposition", "Attachment;Filename=${map_file_name}")
            gpxService.Download(map_png_file_name, map_file_name, response.outputStream)
            gpxService.printdone ""
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
    def download_pngzip = {
        if (session?.lastContest) {
            String map_png_file_name = "${servletContext.getRealPath('/')}${Defs.ROOT_FOLDER_MAP}/${session.lastContest.contestUUID}/${params.name}"
            String world_file_name = "${map_png_file_name}w"
            String info_file_name = "${map_png_file_name}info"
            String png_file_name = "Map.png"
            String map_zip_file_name = map_png_file_name + ".zip"
            gpxService.printstart "Write ${map_zip_file_name}"
            ZipOutputStream zip_stream = new ZipOutputStream(new FileOutputStream(map_zip_file_name))
            write_file_to_zip(zip_stream, png_file_name, map_png_file_name)
            write_file_to_zip(zip_stream, png_file_name + "w", world_file_name)
            write_file_to_zip(zip_stream, png_file_name + "info", info_file_name)
            zip_stream.close()
            gpxService.printdone ""
            gpxService.printstart "Download PNGZIP"
            String map_file_name = (params.title + '.zip') // .replace(' ',"_")
            response.setContentType("application/octet-stream")
            response.setHeader("Content-Disposition", "Attachment;Filename=${map_file_name}")
            gpxService.Download(map_zip_file_name, map_file_name, response.outputStream)
            gpxService.DeleteFile(map_zip_file_name)
            gpxService.printdone ""
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
    def download_pdf = {
        if (session?.lastContest) {
            String map_png_file_name = "${servletContext.getRealPath('/')}${Defs.ROOT_FOLDER_MAP}/${session.lastContest.contestUUID}/${params.name}"
            String info_file_name = "${map_png_file_name}info"
            boolean print_landscape = params.landscape == "true"
            String print_size = params.size
            gpxService.printstart "Generate PDF (Landscape: $print_landscape, Size:$print_size)"
            Map ret = printService.printmapRoute(print_size, print_landscape, map_png_file_name, GetPrintParams())
            gpxService.printdone ""
            if (ret.error) {
                flash.message = ret.message
                flash.error = true
                redirect(action:'show',id:params.id)
            } else if (ret.content) {
                String map_file_name = (params.title + '.pdf').replace(' ',"_")
                printService.WritePDF3(response, ret.content, map_file_name)
            } else {
                redirect(action:'show',id:params.id)
            }
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
    def rename_question = {
        if (session?.lastContest) {
            String map_png_file_name = "${servletContext.getRealPath('/')}${Defs.ROOT_FOLDER_MAP}/${session.lastContest.contestUUID}/${params.name}"
        } else {
            redirect(action:'list')
            redirect(controller:'contest',action:'start')
        }
    }
    
    def rename = {
        if (session?.lastContest) {
            if (params.MapRenameNewName && params.name != params.MapRenameNewName) {
                String map_png_file_name = "${servletContext.getRealPath('/')}${Defs.ROOT_FOLDER_MAP}/${session.lastContest.contestUUID}/${params.name}.png"
                String world_file_name = "${map_png_file_name}w"
                String info_file_name = "${map_png_file_name}info"
                String new_map_png_file_name = "${servletContext.getRealPath('/')}${Defs.ROOT_FOLDER_MAP}/${session.lastContest.contestUUID}/${params.MapRenameNewName}.png"
                String new_world_file_name = "${new_map_png_file_name}w"
                String new_info_file_name = "${new_map_png_file_name}info"
                if (new File(new_map_png_file_name).exists()) {
                    flash.message = message(code:'fc.map.rename.error', args:[params.MapRenameNewName])
                    flash.error = true
                } else {
                    gpxService.println "Rename $map_png_file_name to $new_map_png_file_name"
                    new File(map_png_file_name).renameTo(new File(new_map_png_file_name))
                    new File(world_file_name).renameTo(new File(new_world_file_name))
                    new File(info_file_name).renameTo(new File(new_info_file_name))
                    flash.message = message(code:'fc.map.rename.done', args:[params.MapRenameNewName])
                }
            }
            redirect(action:'list')
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
    def delete = {
        if (session?.lastContest) {
            String map_png_file_name = "${servletContext.getRealPath('/')}${Defs.ROOT_FOLDER_MAP}/${session.lastContest.contestUUID}/${params.name}"
            String world_file_name = "${map_png_file_name}w"
            String info_file_name = "${map_png_file_name}info"
            gpxService.DeleteFile(map_png_file_name)
            gpxService.DeleteFile(world_file_name)
            gpxService.DeleteFile(info_file_name)
            gpxService.println "Delete $map_png_file_name"
            flash.message = message(code:'fc.map.deleted', args:[params.title])
            redirect(action:'list')
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
    def selectfilename = {
        [:]
    }
    
    def importfilename = {
        if (session?.lastContest) {
            MultipartFile zip_file = request.getFile("zipfile")
            if (zip_file && !zip_file.isEmpty()) {
                String map_dir_name = "${servletContext.getRealPath('/')}${Defs.ROOT_FOLDER_MAP}/${session.lastContest.contestUUID}"
                String new_name = zip_file.getOriginalFilename().substring(0, zip_file.getOriginalFilename().lastIndexOf('.'))
                String modify_name = ""
                String modify_num = 0
                while (new File("${map_dir_name}/${new_name}${modify_name}.png").exists()) {
                    modify_num++
                    modify_name = " (${modify_num})"
                }
                new_name += modify_name
                gpxService.println "Import map '${new_name}'"
                Map ret = fcService.importMap(zip_file, map_dir_name, new_name)
                if (ret.importOk) {
                    flash.message = message(code:'fc.map.import.done',args:[new_name])
                } else {
                    flash.message = message(code:'fc.map.import.error',args:[new_name])
                    flash.error = true
                }
            }
            redirect(action:'list')
        } else {
            redirect(controller:'contest',action:'start')
        }
    }
    
	def cancel = {
        redirect(action:'list')
	}
	
    private void write_file_to_zip(ZipOutputStream zipOutputStream, String zipFileName, String fileName)
    {
        byte[] buffer = new byte[1024]
        FileInputStream file_input_stream = new FileInputStream(fileName)
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName))
        int length
        while ((length = file_input_stream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length)
        }
        zipOutputStream.closeEntry()
        file_input_stream.close()
    }
    
	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
}
