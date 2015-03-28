class GpxTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def gpxViewerHead = { attrs, body ->
        outln"""<style type="text/css">"""
        outln"""    .text { font-size:1.5em; margin:0; }"""
        outln"""    .error { color:red; }"""
        outln"""    #map_profiles { display:inline; }"""
        // outln"""    #map_hp { height:300px; }"""
        // outln"""    #map_vp { height:300px; }"""
        outln"""</style>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def gpxViewerForm = { attrs, body ->
        int tab_index = 1
        
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
        outln"""    function resize_map() {"""
        outln"""        var map = document.getElementById("map").getBoundingClientRect()"""
        outln"""        var max_height = window.innerHeight - map.top - 20;"""
        if (attrs.showProfiles == "yes") {
            outln"""    document.getElementById("map").style.height = (3*max_height/4 ) + "px";"""
            outln"""    document.getElementById("map_hp").style.height = (max_height/8 ) + "px";"""
            outln"""    document.getElementById("map_vp").style.height = (max_height/8 ) + "px";"""
        } else {
            outln"""    document.getElementById("map").style.height = (max_height ) + "px";"""
        }
        outln"""        """
        outln"""        """
        outln"""        """
        outln"""    }"""
        outln"""    """
        outln"""    window.addEventListener("resize", resize_map);"""
        outln"""</script>"""
        
        outln"""<div class="text" id="point">${attrs.infoText}</div>"""
        outln""""""
        outln"""<button type="button" id="0" onmousedown="showPos=0;wr_pos(0);return true;" class="gpxview:map:skaliere" tabIndex="${tab_index}">${message(code:'fc.gpx.overview')}</button>"""
        tab_index++
        if (attrs.gpxShowPoints) {
            int i = 1
            for(Map p in attrs.gpxShowPoints) {
                String button_text = p.name
                String button_class = ""
                if (p.error) {
                    button_class = "error"
                    if (p.badcoursenum) {
                        button_text += " (${p.badcoursenum})"
                    }
                    if (p.points) {
                        button_class += " points"
                    }
                }
                if (!p.hide) {
                    outln"""<button type="button" id="${i}" onmousedown="showPos=${i};wr_pos(${i});return true;" class="${button_class} gpxview:map:skaliere:${p.latcenter},${p.loncenter},${p.radius}" tabIndex="${tab_index}">${button_text}</button>"""
                    tab_index++
                    i++
                }
            }
            outln"""<script>var maxPos = ${i-1};</script>"""
            outln"""<button type="button" onclick="if(showPos>0)showPos--;document.getElementById(showPos).click();wr_pos(showPos);return true;" tabIndex="${tab_index}">${message(code:'fc.gpx.beforepoint')}</button>"""
            tab_index++
            outln"""<button type="button" onclick="if(showPos<maxPos)showPos++;document.getElementById(showPos).click();wr_pos(showPos);return true;" tabIndex="${tab_index}">${message(code:'fc.gpx.nextpoint')}</button>"""
            tab_index++
        }
        if (attrs.showCancel == "yes") {
            outln"""<button type="submit" name="_action_cancel" tabIndex="${tab_index}">${message(code:'fc.cancel')}</button>"""
            tab_index++
        }
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
        outln"""    var Fullscreenbutton = true;"""
        outln"""    var Unit = 'air';"""
        outln"""    var Overviewmapcontrol = true;"""
        outln"""    var Shwpshadow = false;"""
        outln"""    var Legende_rr = false;"""
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
    private void outln(str)
    {
        out << """$str
"""
    }

}
