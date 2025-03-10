class GpxTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def offlineViewerHead = { attrs, body ->
        outln"""<style type="text/css">"""
        outln"""    .text { font-size:1.5em; margin:0; }"""
        outln"""    .error { color:red; }"""
        outln"""    .enrouteerror { background-color:yellow; }"""
        outln"""    .warning { color:blue; }"""
        outln"""</style>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def offlineViewerForm = { attrs, body ->
        wrbuttons("xskaliere", attrs)
        
        outln"""<script>"""
        outln"""    var write_map = true;"""
        outln"""    var map_resized = false;"""
        outln"""    var center_lat = "";"""
        outln"""    var center_lon = "";"""
        outln"""    var radius_value = "";"""
        outln"""    var max_x = 0;"""
        outln"""    var max_y = 0;"""
        outln"""    var infos = [];"""
        outln"""    var lats = [];"""
        outln"""    var lons = [];"""
        outln"""    var act_lat = null;"""
        outln"""    var act_lon = null;"""
        outln"""    var act_x = null;"""
        outln"""    var act_y = null;"""
        outln"""    var last_lat = null;"""
        outln"""    var last_lon = null;"""
        outln"""    var last_distance = null;"""
        outln"""    var last_x = null;"""
        outln"""    var last_y = null;"""
        outln"""    var move_dir = "";"""
        outln"""    var show_points = false;"""
        outln"""    function resize_map() {"""
        outln"""        map_resized = true;"""
        outln"""        var map = document.getElementById("map");"""
        outln"""        var map_margin = document.getElementById("map_margin");"""
                        // 1. calculation of map size
        outln"""        var map_rect = map.getBoundingClientRect();"""
        outln"""        var map_margin_rect = map_margin.getBoundingClientRect();"""
        outln"""        map.height = window.innerHeight - map_rect.top - 20;"""
        outln"""        map.width = map_margin_rect.right - map_margin_rect.left - 2;"""
                        // 2. calculation of map size
        outln"""        map_rect = map.getBoundingClientRect();"""
        outln"""        map_margin_rect = map_margin.getBoundingClientRect();"""
        outln"""        map.height = window.innerHeight - map_rect.top - 20;"""
        outln"""        map.width = map_margin_rect.right - map_margin_rect.left - 2;"""
                        // write map content
        outln"""        var xhttp = new XMLHttpRequest();"""
        outln"""        xhttp.onreadystatechange = function() {"""
        outln"""            if (xhttp.readyState == 4 && xhttp.status == 200 && xhttp.responseXML !== "null" && xhttp.responseXML !== "undefined" && xhttp.responseXML.getElementsByTagName('points') !== "null") {"""
        outln"""                var map = document.getElementById("map");"""
        outln"""                if (!map_resized) {"""
                                    // 
        outln"""                    p = xhttp.responseXML.getElementsByTagName('points')[0];"""
        outln"""                    center_lat = parseFloat(p.getAttribute('center_lat'));"""
        outln"""                    center_lon = parseFloat(p.getAttribute('center_lon'));"""
        outln"""                    if (radius_value == "") {"""
        outln"""                        radius_value = parseFloat(p.getAttribute('radius_value'));"""
        outln"""                    }"""
        outln"""                    max_x = parseInt(p.getAttribute('max_x'));"""
        outln"""                    max_y = parseInt(p.getAttribute('max_y'));"""
        outln"""                    infos = [];"""
        outln"""                    lats = [];"""
        outln"""                    lons = [];"""
        outln"""                    for (y1 = 0; y1 < max_y; y1++) {"""
        outln"""                        for (x1 = 0; x1 < max_x; x1++) {"""
        outln"""                            infos.push("");"""
        outln"""                            lats.push(null);"""
        outln"""                            lons.push(null);"""
        outln"""                        }"""
        outln"""                    }"""
                                    //
        outln"""                    var ctx = map.getContext("2d");"""
        outln"""                    ctx.beginPath();"""
        outln"""                    ctx.rect(0,0,map.width,map.height);"""
        outln"""                    ctx.fillStyle = "white";"""
        outln"""                    ctx.fill();"""
        outln"""                    ctx.stroke();"""
        outln"""                    v = xhttp.responseXML.getElementsByTagName('p');"""
        outln"""                    var last_x = -1;"""
        outln"""                    var last_y = -1;"""
        outln"""                    var last_source = "";"""
        outln"""                    for (i = 0; i < v.length; i++) {"""
        outln"""                        var x = parseInt(v[i].getAttribute('x'));"""
        outln"""                        var y = parseInt(v[i].getAttribute('y'));"""
        outln"""                        var source = v[i].getAttribute('source');"""
        outln"""                        var color = v[i].getAttribute('color');"""
        outln"""                        var info = v[i].getAttribute('info');"""
        outln"""                        if (info != "") {"""
        outln"""                            infos[y*max_x+x] = info;"""
        outln"""                            lats[y*max_x+x] = parseFloat(v[i].getAttribute('lat'));"""
        outln"""                            lons[y*max_x+x] = parseFloat(v[i].getAttribute('lon'));"""
        outln"""                        }"""
                                        // write line/point
        outln"""                        if (source != "gatenotfound") {"""
        outln"""                            ctx.beginPath();"""
        outln"""                            if (color != '') {"""
        outln"""                                ctx.strokeStyle = color;"""
        outln"""                            } else {"""
        outln"""                                ctx.strokeStyle = "black";"""
        outln"""                            }"""
        outln"""                            if (show_points && (source == "flight")) {"""
        outln"""                                ctx.moveTo(x,y);"""
        outln"""                                ctx.lineTo(x+1,y+1);"""
        outln"""                            } else {"""
        outln"""                                if (v[i].getAttribute('start') == 'true') {"""
        outln"""                                    last_x = x;"""
        outln"""                                    last_x = y;"""
        outln"""                                } else {"""
        outln"""                                    ctx.moveTo(last_x,last_y);"""
        outln"""                                    ctx.lineTo(x,y);"""
        outln"""                                }"""
        outln"""                            }"""
        outln"""                            last_x = x;"""
        outln"""                            last_y = y;"""
        outln"""                            last_source = source;"""
        outln"""                            ctx.stroke();"""
        outln"""                        }"""
                                        // write text
        outln"""                        var text_x = x;"""
        outln"""                        var text_y = y;"""
        outln"""                        if (v[i].getAttribute('name') && (text_x < map.width)) {"""
        outln"""                            ctx.font = "normal 12px Arial";"""
        outln"""                            name_width = ctx.measureText(v[i].getAttribute('name')).width;"""
        outln"""                            if (text_x + name_width > map.width) {"""
        outln"""                                text_x = map.width - name_width;"""
        outln"""                            }"""
        outln"""                            if (v[i].getAttribute('name_down') == 'true') {"""
        outln"""                                text_y += 12;"""
        outln"""                            } else {"""
        outln"""                                text_y -= 6;"""
        outln"""                            }"""
        outln"""                            if (color != '') {"""
        outln"""                                ctx.strokeStyle = color;"""
        outln"""                            } else {"""
        outln"""                                ctx.strokeStyle = "black";"""
        outln"""                            }"""
        outln"""                            ctx.strokeText(v[i].getAttribute('name'),text_x,text_y);"""
        outln"""                        }"""
        outln"""                    }"""
        outln"""                    write_map = true;"""
        outln"""                }"""
        outln"""                if (map_resized) {"""
        outln"""                    map_resized = false;"""
        outln"""                    xhttp.open("GET", "${createLinkTo(dir:'gpx/calculatexy',file:'')}?gpxFileName=${attrs.gpxFileName}&maxX=" + map.width + "&maxY=" + map.height + "&centerLat=" + center_lat + "&centerLon=" + center_lon + "&radius=" + radius_value + "&moveDir=" + move_dir, true);"""
        outln"""                    xhttp.send();"""
        outln"""                    write_map = false;"""
        outln"""                }"""
        outln"""            }"""
        outln"""        }"""
        outln"""        if (write_map) {"""
        outln"""            xhttp.open("GET", "${createLinkTo(dir:'gpx/calculatexy',file:'')}?gpxFileName=${attrs.gpxFileName}&maxX=" + map.width + "&maxY=" + map.height + "&centerLat=" + center_lat + "&centerLon=" + center_lon + "&radius=" + radius_value + "&moveDir=" + move_dir, true);"""
        outln"""            xhttp.send();"""
        outln"""            write_map = false;"""
        outln"""        }"""
        outln"""    }"""
        outln"""    function write_info(e) {"""
        outln"""        var map_rect = document.getElementById("map").getBoundingClientRect();"""
        outln"""        var x = e.clientX - parseInt(map_rect.left.toString());"""
        outln"""        var y = e.clientY - parseInt(map_rect.top.toString());"""
        outln"""        var written = write_info_xy(x, y);"""
                        // first round
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x, y-1);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x, y+1);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x-1, y);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x-1, y-1);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x-1, y+1);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x+1, y);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x+1, y-1);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x+1, y+1);"""
        outln"""        }"""
                        // second round
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x, y-2);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x, y+2);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x-1, y-2);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x-1, y+2);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x+1, y-2);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x+1, y+2);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x-2, y);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x-2, y-1);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x-2, y+1);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x-2, y-2);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x-2, y+2);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x+2, y);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x+2, y-1);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x+2, y+1);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x+2, y-2);"""
        outln"""        }"""
        outln"""        if (!written) {"""
        outln"""            written = write_info_xy(x+2, y+2);"""
        outln"""        }"""
        outln"""    }"""
        outln"""    function clear_info() {"""
        outln"""        document.getElementById("info").innerHTML = "";"""
        outln"""    }"""
        outln"""    function write_info_xy(x, y) {"""
        outln"""        if (x >= 0 && x < max_x && y >= 0 && y < max_y) {"""
        outln"""            if (infos[y*max_x+x] != "") {"""
        outln"""                var distance_info = " - ${message(code:'fc.gpx.distancemeasurement.start')}";"""
        outln"""                if (last_lat != null && last_lon != null) {"""
        outln"""                    last_distance = get_distance(last_lat,lats[y*max_x+x],last_lon,lons[y*max_x+x]);"""
        outln"""                    distance_info = " - " + last_distance.toString() + "${message(code:'fc.gpx.distancemeasurement.stop')}";"""
        outln"""                }"""
        outln"""                document.getElementById("info").innerHTML = infos[y*max_x+x] + distance_info;"""
        outln"""                act_lat = lats[y*max_x+x];"""
        outln"""                act_lon = lons[y*max_x+x];"""
        outln"""                act_x = x;"""
        outln"""                act_y = y;"""
        outln"""                return true;"""
        outln"""            } else {"""
        outln"""                document.getElementById("info").innerHTML = "";"""
        outln"""                act_lat = null;"""
        outln"""                act_lon = null;"""
        outln"""                act_x = null;"""
        outln"""                act_y = null;"""
        outln"""            }"""
        outln"""        } else {"""
        outln"""            document.getElementById("info").innerHTML = "";"""
        outln"""            act_lat = null;"""
        outln"""            act_lon = null;"""
        outln"""            act_x = null;"""
        outln"""            act_y = null;"""
        outln"""        }"""
        outln"""        return false;"""
        outln"""    }"""
        outln"""    function get_distance(srcLatitude, destLatitude, srcLongitude, destLongitude) {"""
        outln"""        var latitude_diff = destLatitude - srcLatitude;"""
		outln"""        var longitude_diff = destLongitude - srcLongitude;"""
		outln"""        var latitude_dist = 60 * latitude_diff;"""
		outln"""        var longitude_dist = 60 * longitude_diff * Math.cos( ((destLatitude + srcLatitude)/2)*Math.PI/180 );"""
		outln"""        var distance = Math.sqrt(latitude_dist*latitude_dist + longitude_dist*longitude_dist);"""
        outln"""        return distance.toFixed(2);"""
        outln"""    }"""
        outln"""    function mouse_down(e) {"""
        outln"""        if (act_lat != null && act_lon != null && act_x != null && act_y != null) {"""
        outln"""            if (e.button == 0) {""" // Left-Mouse
        outln"""                if (e.shiftKey) {""" // Shift
        outln"""                    if (last_lat == null) {""" // start
        outln"""                        last_lat = act_lat;"""
        outln"""                        last_lon = act_lon;"""
        outln"""                        last_distance = 0;"""
        outln"""                        last_x = act_x;"""
        outln"""                        last_y = act_y;"""
        outln"""                        write_info(e);"""
        outln"""                    } else {""" // write line
        outln"""                        var ctx = map.getContext("2d");"""
        outln"""                        ctx.beginPath();"""
        outln"""                        ctx.strokeStyle = "blue";"""
        outln"""                        ctx.moveTo(last_x,last_y);"""
        outln"""                        ctx.lineTo(act_x,act_y);"""
        outln"""                        ctx.stroke();"""
        outln"""                        ctx.font = "14px Arial";"""
        outln"""                        ctx.fillStyle = "blue";"""
        outln"""                        ctx.fillText(last_distance.toString() + '${message(code:'fc.mile')}', act_x+2, act_y+2);"""
        outln"""                        last_lat = null;"""
        outln"""                        last_lon = null;"""
        outln"""                        last_distance = null;"""
        outln"""                        last_x = null;"""
        outln"""                        last_y = null;"""
        outln"""                    }"""
        outln"""                } else {"""
        outln"""                    last_lat = null;"""
        outln"""                    last_lon = null;"""
        outln"""                    last_distance = null;"""
        outln"""                    last_x = null;"""
        outln"""                    last_y = null;"""
        outln"""                }"""
        outln"""            }"""
        outln"""        } else {"""
        outln"""            if (e.button == 0) {""" // Left-Mouse
        outln"""                last_lat = null;"""
        outln"""                last_lon = null;"""
        outln"""                last_distance = null;"""
        outln"""                last_x = null;"""
        outln"""                last_y = null;"""
        outln"""            }"""
        outln"""        }"""
        outln"""    }"""
        outln"""    window.addEventListener("resize", resize_map);"""
        outln"""</script>"""
        
        outln"""<div id="map_margin">"""
        outln"""  <canvas id="map" style="border:1px solid #000000;" onmousemove="write_info(event)" onmouseout="clear_info()" onmousedown="mouse_down(event)" />"""
        outln"""</div>"""

        outln"""<div>"""
        outln"""  <p id="info"/>"""
        outln"""</div>"""
        
        outln"""<script>"""
        outln"""    resize_map();"""
                    // rescale
        outln"""    function rescale(centerLat, centerLon, radiusValue, moveDir) {"""
        outln"""        center_lat = centerLat;"""
        outln"""        center_lon = centerLon;"""
        outln"""        radius_value = radiusValue;"""
        outln"""        move_dir = moveDir;"""
        outln"""        resize_map();"""
        outln"""    }"""
                    // buttons
        outln"""    var buttons = document.getElementsByTagName("button");"""
        outln"""    for(var i=0; i<buttons.length; i++) {"""
        outln"""        var button = buttons[i];"""
        if (attrs.showCoord) {
            outln"""    var rescale_first_button = true;"""
        } else {
            outln"""    var rescale_first_button = false;"""
        }
        outln"""        if (button.className) {"""
        outln"""            var class_name = button.className;"""
        outln"""            var cn = class_name.search("xskaliere");"""
        outln"""            if (cn > -1) {"""
        outln"""                var cmd = class_name.substring(cn).split()[0];"""
        outln"""                cmd = cmd.split(":");"""
        outln"""                if (cmd.length > 0) {"""
        outln"""                    if (cmd[0] == "xskaliere") {"""
        outln"""                        ( function() {"""
        outln"""                            if(cmd.length == 1) {"""
        outln"""                                button.onclick = function(){rescale("","","","")};"""
        outln"""                            } else if(cmd.length == 2) {"""
        outln"""                                var pars = cmd[1].split(",");"""
        outln"""                                button.onclick = function(){rescale(pars[0],pars[1],pars[2],"")};"""
        outln"""                                if (rescale_first_button && (button.innerHTML == '${attrs.showCoord}')) {"""
        outln"""                                    rescale(pars[0],pars[1],pars[2],"");"""
        outln"""                                    rescale_first_button = false;"""
        outln"""                                }"""
        outln"""                            }"""
        outln"""                        } )();"""
        outln"""                    }"""
        outln"""                }"""
        outln"""            }"""
        outln"""            else if (class_name == "zoomout") {""" // -
        outln"""                button.onclick = function(){rescale(center_lat,center_lon,radius_value * 2,"")};"""
        outln"""            }"""
        outln"""            else if (class_name == "zoomin") {""" // +
        outln"""                button.onclick = function(){rescale(center_lat,center_lon,radius_value / 2,"")};"""
        outln"""            }"""
        outln"""            else if (class_name == "moveleft") {""" // left
        outln"""                button.onclick = function(){rescale(center_lat,center_lon,radius_value,90)};"""
        outln"""            }"""
        outln"""            else if (class_name == "moveright") {""" // right
        outln"""                button.onclick = function(){rescale(center_lat,center_lon,radius_value,270)};"""
        outln"""            }"""
        outln"""            else if (class_name == "moveup") {""" // up
        outln"""                button.onclick = function(){rescale(center_lat,center_lon,radius_value,180)};"""
        outln"""            }"""
        outln"""            else if (class_name == "movedown") {""" // down
        outln"""                button.onclick = function(){rescale(center_lat,center_lon,radius_value,0)};"""
        outln"""            }"""
        outln"""            else if (class_name == "refresh") {""" // refresh
        outln"""                button.onclick = function(){rescale(center_lat,center_lon,radius_value,"")};"""
        outln"""            }"""
        outln"""        }"""
        outln"""    }"""
                    // input
        outln"""    var inputs = document.getElementsByTagName("input");"""
        outln"""    for(var i=0; i<inputs.length; i++) {"""
        outln"""        var input = inputs[i];"""
        outln"""        if (input.className) {"""
        outln"""            var class_name = input.className;"""
        outln"""            if (class_name == "showpoints") {""" // points
        outln"""                input.onchange = function() {"""
        outln"""                    show_points = !show_points;"""
        outln"""                    rescale(center_lat,center_lon,radius_value,"");"""
        outln"""                };"""
        outln"""            }"""
        outln"""        }"""
        outln"""    }"""
                    // keys
        outln"""    function key_down(e) {"""
        outln"""        if (e.keyCode == '38' || e.keyCode == '104') {""" // up
        outln"""            rescale(center_lat,center_lon,radius_value,180);"""
        outln"""        }"""
        outln"""        else if (e.keyCode == '40' || e.keyCode == '98') {""" // down
        outln"""            rescale(center_lat,center_lon,radius_value,0);"""
        outln"""        }"""
        outln"""        else if (e.keyCode == '37' || e.keyCode == '100') {""" // left
        outln"""            rescale(center_lat,center_lon,radius_value,90);"""
        outln"""        }"""
        outln"""        else if (e.keyCode == '39' || e.keyCode == '102') {""" // right
        outln"""            rescale(center_lat,center_lon,radius_value,270);"""
        outln"""        }"""
        outln"""        else if (e.keyCode == '189' || e.keyCode == '109') {""" // -
        outln"""            rescale(center_lat,center_lon,radius_value * 2,"");"""
        outln"""        }"""
        outln"""        else if (e.keyCode == '187' || e.keyCode == '107') {""" // +
        outln"""            rescale(center_lat,center_lon,radius_value / 2,"");"""
        outln"""        }"""
        outln"""    }"""
        outln"""    document.onkeydown = key_down;"""
        outln"""</script>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def offlineViewerFooter = { attrs, body ->
        outln"""<footer"""
        //outln"""   <p>${message(code:'fc.gpx.gpxviewer')}</p>"""
        outln"""</footer>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def gpxViewerHead = { attrs, body ->
        outln"""<style type="text/css">"""
        outln"""    .text { font-size:1.5em; margin:0; }"""
        outln"""    .error { color:red; }"""
        outln"""    .enrouteerror { background-color:yellow; }"""
        outln"""    .warning { color:blue; }"""
        outln"""    #map_profiles { display:inline; }"""
        // outln"""    #map_hp { height:300px; }"""
        // outln"""    #map_vp { height:300px; }"""
        outln"""</style>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def gpxViewerForm = { attrs, body ->
        outln"""<script>"""
        outln"""    var gmApiKey = "${attrs.gmApiKey}";"""
        outln"""</script>"""
        
        // FC OnlineMap
        String map_folder_name = "${servletContext.getRealPath('/')}${Defs.ROOT_FOLDER_MAP}/${attrs.contestUUID}/"
        File map_folder = new File(map_folder_name)
        String onlinemap_names = ""
        int onlinemap_index = 1
        int i = 0
        if (map_folder.exists()) {
            for (Map map_entry in MapTools.GetMapList(servletContext, session)) {
                if (map_entry.projection == "3857") {
                    if (onlinemap_names) {
                        onlinemap_names += ","
                    }
                    onlinemap_names += map_entry.localref
                    i++
                    if (map_entry.title == attrs.defaultOnlineMap) {
                        onlinemap_index = i
                    }
                }
            }
        }
        outln"""<script>"""
        outln"""    var onlineMapUrls = [];"""
        outln"""    var onlineMapBounds = [];"""
        outln"""    var onlineMapIndex = 0;"""
        if (onlinemap_names) {
            outln"""const xhttp = new XMLHttpRequest();"""
            outln"""var onlinemap_names_array = "${onlinemap_names}".split(",");"""
            outln"""for (let i in onlinemap_names_array) {"""
            outln"""    var onlinemap_name = onlinemap_names_array[i];"""
            outln"""    xhttp.onload = function() {load_onlinemap_info(this);}"""
            outln"""    xhttp.open("GET", onlinemap_name + "${Defs.MAP_PNG_INFO_FILE_SUFFIX}", false);"""
            outln"""    xhttp.send();"""
            outln"""    function load_onlinemap_info(infoData) {"""
            outln"""        var top = 0;"""
            outln"""        var bottom = 0;"""
            outln"""        var right = 0;"""
            outln"""        var left = 0;"""
            outln"""        var projection = '';"""
            outln"""        var found = false;"""
            outln"""        var lines = infoData.responseText.split('\\n');"""
            outln"""        for (var line = 0; line < lines.length; line++) {"""
            outln"""            var key_value = lines[line].split(':');"""
            outln"""            if (key_value) {"""
            outln"""                switch(key_value[0]) {"""
            outln"""                    case 'Top(Lat)':"""
            outln"""                        top = Number(key_value[1].trim());"""
            outln"""                        found = true;"""
            outln"""                        break;"""
            outln"""                    case 'Bottom(Lat)':"""
            outln"""                        bottom = Number(key_value[1].trim());"""
            outln"""                        found = true;"""
            outln"""                        break;"""
            outln"""                    case 'Right(Lon)':"""
            outln"""                        right = Number(key_value[1].trim());"""
            outln"""                        found = true;"""
            outln"""                        break;"""
            outln"""                    case 'Left(Lon)':"""
            outln"""                        left = Number(key_value[1].trim());"""
            outln"""                        found = true;"""
            outln"""                        break;"""
            outln"""                    case 'Projection':"""
            outln"""                        projection = key_value[1].trim();"""
            outln"""                        found = true;"""
            outln"""                        break;"""
            outln"""                }"""
            outln"""            }"""
            outln"""        }"""
            outln"""        if (found && projection == '3857') {"""
            outln"""            onlineMapUrls.push(onlinemap_name);"""
            outln"""            onlineMapBounds.push([[top, left], [bottom, right]]);"""
            outln"""            onlineMapIndex = ${onlinemap_index};"""
            outln"""        }"""
            outln"""    }"""
            outln"""}"""
        }
        outln"""</script>"""
        
        wrbuttons("gpxview:map:skaliere", attrs)
        
        // FC OnlineMap
        outln"""<input type="range" id="onlinemap_opacity_id" value="1.0" min="0.0" max="1.0" step="0.01" style="display:none; vertical-align:middle; width:60px;"></input>"""
        outln"""<select id="onlinemap_route_id" style="display:none; vertical-align:middle;"></select>"""
        
        outln"""<script>"""
        outln"""    function resize_map() {"""
        outln"""        var map = document.getElementById("map").getBoundingClientRect()"""
        outln"""        var max_height = window.innerHeight - map.top - 20;"""
        if (attrs.showProfiles == "yes") {
            outln"""    document.getElementById("map").style.height = (3*max_height/4 ) + "px";"""
            outln"""    document.getElementById("map_hp").style.height = (max_height/8 ) + "px";"""
            outln"""    document.getElementById("map_vp").style.height = (max_height/8 ) + "px";"""
        } else {
            outln"""    document.getElementById("map").style.height = (0.95*max_height) + "px";"""
        }
        outln"""    }"""
        outln"""    window.addEventListener("resize", resize_map);"""
        outln"""</script>"""
        
        outln"""<div id="map" class="map gpxview:${attrs.gpxFileName}:Oberflaeche" >"""
        outln"""    <noscript><p>${message(code:'fc.needjavascript')}</p></noscript>"""
        outln"""</div>"""
        if (attrs.showProfiles == "yes") {
            outln"""<div id="map_profiles">"""
            outln"""    <div id="map_hp">"""
            outln"""        <noscript><p>${message(code:'fc.needjavascript')}</p></noscript>"""
            outln"""    </div>"""
            outln"""    <div id="map_vp">"""
            outln"""        <noscript><p>${message(code:'fc.needjavascript')}</p></noscript>"""
            outln"""    </div>"""
            outln"""</div>"""
        }
        if (attrs.showLanguage != Languages.de.toString()) {
            outln"""<script>"""
            outln"""    var Doclang = 'en';"""
            outln"""</script>"""
        }
        outln"""<script>"""
        outln"""    var Legende_fnm = false;"""
        outln"""    var Fullscreenbutton = false;""" // BUG: Aufruf scheitert
        outln"""    var Unit = 'air';"""
        outln"""    var Overviewmapcontrol = true;"""
        outln"""    var Shwpshadow = false;"""
        outln"""    var Legende_rr = false;"""
        outln"""    var Arrowtrack = true;"""
        outln"""    """
        outln"""    resize_map();"""
        outln"""</script>"""
    }

    // --------------------------------------------------------------------------------------------------------------------
    def gpxViewerFooter = { attrs, body ->
        outln"""<footer"""
        outln"""   <p>${message(code:'fc.gpx.gpxviewer')}</p>"""
        outln"""</footer>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void wrbuttons( String skaliereFunc, attrs )
    {
        outln"""<script>"""
        outln"""    var showPos = 0;"""
        outln"""    """
        outln"""    function wr_pos(id) {"""
        outln"""        if (id==0) {"""
        outln"""            document.getElementById("point").innerHTML="${attrs.infoText}";"""
        outln"""        } else {"""
        outln"""            document.getElementById("point").innerHTML="${attrs.infoText}" + " - " + document.getElementById(id).textContent;"""
        outln"""        }"""
        outln"""    }"""
        outln"""    """
        outln"""</script>"""
        outln""""""
        outln"""<div class="text" id="point">${attrs.infoText}</div>"""
        outln""""""
        outln"""<button type="button" id="0" onmousedown="showPos=0;wr_pos(0);return true;" class="${skaliereFunc}" tabIndex="${attrs.ti[0]++}">${message(code:'fc.gpx.overview')}</button>"""
        if (attrs.gpxShowPoints) {
            int i = 1
            for(Map p in attrs.gpxShowPoints) {
                String button_text = p.name
                String button_class = ""
                if (p.error) {
                    if (p.enroutephoto || p.enroutecanvas) {
                        button_class = "enrouteerror"
                    } else {
                        button_class = "error"
                    }
                } else if (p.warning) {
                    button_class = "warning"
                }
                if (p.enroutephoto) {
                    button_text = """<img src="${attrs.gpxViewerSrc}/Icons/fcphoto.png" style="height:12px;"/> ${p.name}"""
                } else if (p.enroutecanvas) {
                    button_text = """<img src="${attrs.gpxViewerSrc}/Icons/${p.name.toLowerCase()}.png" style="height:12px;"/>"""
                } else if (p.circlecenter) {
                    button_text = """<img src="${attrs.gpxViewerSrc}/Icons/circle_red.svg" style="height:12px;"/> ${p.name}"""
                }
                outln"""<button type="button" id="${i}" onmousedown="showPos=${i};wr_pos(${i});return true;" class="${button_class} ${skaliereFunc}:${p.latcenter},${p.loncenter},${p.radius}" tabIndex="${attrs.ti[0]++}">${button_text}</button>"""
                i++
            }
            outln"""<script>var maxPos = ${i-1};</script>"""
            outln"""<button type="button" onclick="if(showPos>0)showPos--;document.getElementById(showPos).click();wr_pos(showPos);return true;" tabIndex="${attrs.ti[0]++}">${message(code:'fc.gpx.beforepoint')}</button>"""
            outln"""<button type="button" onclick="if(showPos<maxPos)showPos++;document.getElementById(showPos).click();wr_pos(showPos);return true;" tabIndex="${attrs.ti[0]++}">${message(code:'fc.gpx.nextpoint')}</button>"""
        }
        if (attrs.showZoom == "yes") {
            outln"""<button type="button" class="zoomout" tabIndex="${attrs.ti[0]++}">${message(code:'fc.gpx.zoomout')}</button>"""
            outln"""<button type="button" class="zoomin" tabIndex="${attrs.ti[0]++}">${message(code:'fc.gpx.zoomin')}</button>"""
            outln"""<button type="button" class="moveleft" tabIndex="${attrs.ti[0]++}">${message(code:'fc.gpx.moveleft')}</button>"""
            outln"""<button type="button" class="moveright" tabIndex="${attrs.ti[0]++}">${message(code:'fc.gpx.moveright')}</button>"""
            outln"""<button type="button" class="moveup" tabIndex="${attrs.ti[0]++}">${message(code:'fc.gpx.moveup')}</button>"""
            outln"""<button type="button" class="movedown" tabIndex="${attrs.ti[0]++}">${message(code:'fc.gpx.movedown')}</button>"""
            outln"""<button type="button" class="refresh" tabIndex="${attrs.ti[0]++}">${message(code:'fc.gpx.refresh')}</button>"""
        }
        if (attrs.showPoints == "yes") {
            outln"""<input type="checkbox" class="showpoints">${message(code:'fc.gpx.points')}</input>"""
        }
        if (attrs.showCancel == "yes") {
            outln"""<button type="submit" name="_action_cancel" tabIndex="${attrs.ti[0]++}">${message(code:'fc.cancel')}</button>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
