class MapTools
{
    // ----------------------------------------------------------------------------------
    static List GetMapList(def servletContext, def session)
    {
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
        return map_list
    }

    // ----------------------------------------------------------------------------------
    static Map GetMap(String mapName, def servletContext, def session)
    {
        for (Map map_entry in GetMapList(servletContext, session)) {
            if (map_entry.title == mapName) {
                return map_entry
            }
        }
        return [:]
    }
}
