import java.math.BigDecimal;
import java.text.DecimalFormat
import java.util.Map;
import java.util.List;

import groovy.json.JsonSlurper

import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.web.context.request.RequestContextHolder

import java.awt.Graphics2D
import java.awt.image.BufferedImage

import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageTypeSpecifier
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import javax.imageio.metadata.IIOInvalidTreeException
import javax.imageio.metadata.IIOMetadata
import javax.imageio.metadata.IIOMetadataNode
import javax.imageio.stream.ImageOutputStream

import org.quartz.JobKey

import org.gdal.gdal.Dataset
import org.gdal.gdal.TranslateOptions
import org.gdal.gdal.WarpOptions
import org.gdal.gdal.gdal
import org.gdal.gdalconst.gdalconstConstants

class OsmPrintMapService
{
    def logService
    def messageSource
    def quartzScheduler
    def gpxService
	def openAIPService
    
    final static Map HEADER_CONTENTTYPE = [name:"Content-Type", value:"application/vnd.api+json; charset=utf-8"]
    
    final static String HIDELAYERS_FC                   = ""
    final static String HIDELAYERS_FC_CONTOURS20        = "contours20"
    final static String HIDELAYERS_FC_CONTOURS50        = "contours50"
    final static String HIDELAYERS_FC_CONTOURS100       = "contours100"
    final static String HIDELAYERS_FC_AIRFIELDSNAME     = "airports-name"
    final static String HIDELAYERS_FC_AIRFIELDSICAO     = "airports-icao"
    final static String HIDELAYERS_FC_CHURCHES          = "symbols-churches"
    final static String HIDELAYERS_FC_CASTLES           = "symbols-castles"
    final static String HIDELAYERS_FC_POWERLINES        = "powerlines"
    final static String HIDELAYERS_FC_WINDPOWERSTATIONS = "symbols-point-windpowerstations,symbols-poly-windpowerstations"
    final static String HIDELAYERS_FC_SMALLROADS_1      = "roads-small-grade1"
    final static String HIDELAYERS_FC_SMALLROADS_2      = "roads-small-grade2"
    final static String HIDELAYERS_FC_SMALLROADS_3      = "roads-small-grade3"
    final static String HIDELAYERS_FC_SMALLROADS_4      = "roads-small-grade4"
    final static String HIDELAYERS_FC_SMALLROADS_5      = "roads-small-grade5"
    
    final static String ATTR_OUTPUT_PROJECTION_PRINTPDF    = "3857" // Web Mercator projection, used by Google Maps, OpenStreetMap, OpenTopoMap and others
    final static String ATTR_OUTPUT_PROJECTION_TASKCREATOR = "4326" // linear lat lon projection
    final static String ATTR_INPUT_SRS_OLD = "+init=epsg:4326"
    final static String ATTR_INPUT_SRS_NEW = "epsg:4326"
     
    // Formate
    //   DIN-A1 Hochformat = 594 x 841 mm, DIN-A1 Querformat = 841 x 594 mm 
    //   DIN-A2 Hochformat = 420 x 594 mm, DIN-A2 Querformat = 594 x 420 mm
    //   DIN-A3 Hochformat = 297 x 420 mm, DIN-A3 Querformat = 420 x 297 mm
    //   DIN-A4 Hochformat = 210 x 297 mm, DIN-A4 Querformat = 297 x 210 mm
    
    final static int A1_SHORT = 594 // mm
    final static int A1_LONG = 841 // mm
    final static int A2_SHORT = 420 // mm
    final static int A2_LONG = 594 // mm
    final static int A3_SHORT = 297 // mm
    final static int A3_LONG = 420 // mm
    final static int A4_SHORT = 210 // mm
    final static int A4_LONG = 297 // mm
    final static int ANR_SHORT = 172 // mm
    final static int ANR_LONG = 198 // mm
    final static int AIRPORTAREA_A3_DISTANCE = A3_LONG // mm
    final static int AIRPORTAREA_A4_DISTANCE = A4_LONG // mm
    final static int MARGIN = 10 // nicht bedruckbarer Rand [mm]

    final static int DPI = 600 // Original: 300 DPI
    
    final static int TEXT_XPOS_LEFT = 5  // Abstand vom linken Rand [mm]
    final static int TEXT_XPOS_RIGHT = 5 // Abstand vom rechten Rand [mm]
    
    final static int CONTEST_TITLE_FONT_SIZE = 14
    final static int CONTEST_TITLE_YPOS_TOP = 6
     
    final static int ROUTE_TITLE_FONT_SIZE = 10
    final static int ROUTE_TITLE_YPOS_CONTEST_TITLE = 4
    
    final static int EDITION_TITLE_FONT_SIZE = 8
    final static int EDITION_TITLE_YPOS_CONTEST_TITLE = 3
    
    final static int BOTTOM_TEXT_FONT_SIZE = 8
    final static int BOTTOM_TEXT_YPOS = 2 // mm
    final static int BOTTOM_TEXT_YPOS2 = 5 // mm
    final static int BOTTOM_TEXT_YPOS3 = 8 // mm
    
    final static int SCALEBAR_YPOS_TOP = 6
    final static String SCALEBAR_TITLE = "5 NM"
    final static int SCALEBAR_TITLE_FONT_SIZE = 8
    
    final static BigDecimal FRAME_STROKE_WIDTH = 1
    final static BigDecimal TRACK_STROKE_WIDTH = 0.75
    final static BigDecimal GRATICULE_STROKE_WIDTH = 0.5
    final static BigDecimal SCALEBAR_STROKE_WIDTH = 4
    
    final static int GRATICULE_TEXT_FONT_SIZE = 8
    final static BigDecimal GRATICULE_SCALEBAR_LEN = 0.2 // NM
    final static String GEODATA_SYMBOL_SCALE = "scale(0.75, 0.75)"
    final static int SYMBOL_TEXT_FONT_SIZE = 12
    
    final static int OBJECT_TEXT_FONT_SIZE = 6
    final static int OBJECT_TEXT_LINE1_DY = 8
    final static int OBJECT_TEXT_LINE2_DY = 14
    
    final static String AIRSPACE_LAYER_STYLE_SEPARATOR = ","
    final static String AIRSPACE_LAYER_STYLE_KEY_VALUE_SEPARATOR = ":"
    final static String AIRSPACE_LAYER_ID_PREAFIX = "id_"
    final static BigDecimal AIRSPACE_STROKE_WIDTH = 0.75

    final static String ADDITIONAL_AIRSPACE_NAME = "coord"
    final static String ADDITIONAL_AIRSPACE_LONLAT_SEPARATOR = ";"
    final static String ADDITIONAL_AIRSPACE_LONLATALT_KML_SEPARATOR = ","
    final static String ADDITIONAL_AIRSPACE_COORD_SEPARATOR = " "

    final static BigDecimal AIRPORTAREA_ADDITIONAL_LEN = 2.0 // NM
    
    // CSV files
    final static String CSV_DELIMITER = "|"
    final static String CSV_LINESEPARATOR = "\r\n"
    
    // Development options
    private final static boolean LOG_RESTAPI_CALLS = true        // true
        
    //--------------------------------------------------------------------------
    Map PrintOSM(Map contestMapParams)
    {
        printstart "PrintOSM"
        
        Map ret = [ok:false, message:'']
        
        Route route_instance = Route.get(contestMapParams.routeId)
        Route route_instance2 = null
        if (route_instance.contestMapShowMapObjectsFromRouteID) {
            route_instance2 = Route.get(route_instance.contestMapShowMapObjectsFromRouteID)
        }
        
        String map_projection = ATTR_OUTPUT_PROJECTION_PRINTPDF
        if (contestMapParams.taskCreator) {
            map_projection = ATTR_OUTPUT_PROJECTION_TASKCREATOR
        }
        
        String input_srs = ATTR_INPUT_SRS_OLD
        if (BootStrap.global.IsNewSRSSystax()) {
            input_srs = ATTR_INPUT_SRS_NEW
        }
        
        String printjob_filename = contestMapParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB
        String printjobid_filename = contestMapParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOBID + contestMapParams.routeId + ".txt"
        String printfileid_filename = contestMapParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTFILEID + contestMapParams.routeId + ".txt"
        
        /*
        if (new File(printjob_filename).exists()) {
            ret.message = getMsg('fc.contestmap.previousjobrunningerror', false)
            printerror ret.message
            return ret
        }
        */
        
        if (!contestMapParams.contestMapGraticule) {
            contestMapParams.graticuleFileName = ""
        }
        String graticule_file_name = contestMapParams.graticuleFileName.replaceAll('\\\\', '/')

        String hide_layers = HIDELAYERS_FC
        boolean openaip_airfields = false
        switch (contestMapParams.contestMapContourLines) {
            case Defs.CONTESTMAPCONTOURLINES_20M:
                hide_layers += ",${HIDELAYERS_FC_CONTOURS50},${HIDELAYERS_FC_CONTOURS100}"
                break
            case Defs.CONTESTMAPCONTOURLINES_50M:
                hide_layers += ",${HIDELAYERS_FC_CONTOURS20},${HIDELAYERS_FC_CONTOURS100}"
                break
            case Defs.CONTESTMAPCONTOURLINES_100M:
                hide_layers += ",${HIDELAYERS_FC_CONTOURS20},${HIDELAYERS_FC_CONTOURS50}"
                break
            default:
                hide_layers += ",${HIDELAYERS_FC_CONTOURS20},${HIDELAYERS_FC_CONTOURS50},${HIDELAYERS_FC_CONTOURS100}"
                break
        }
        if (contestMapParams.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_AUTO || contestMapParams.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_OPENAIP) {
            if (BootStrap.global.IsOpenAIP()) {
                openaip_airfields = true
                hide_layers += ",${HIDELAYERS_FC_AIRFIELDSNAME},${HIDELAYERS_FC_AIRFIELDSICAO}"
            } else { // ICAO
                hide_layers += ",${HIDELAYERS_FC_AIRFIELDSNAME}"
            }
        } else if (contestMapParams.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_OSM_ICAO) {
            hide_layers += ",${HIDELAYERS_FC_AIRFIELDSNAME}"
        } else {
            hide_layers += ",${HIDELAYERS_FC_AIRFIELDSICAO}"
        }
        if (!contestMapParams.contestMapChurches) {
            hide_layers += ",${HIDELAYERS_FC_CHURCHES}"
        }
        if (!contestMapParams.contestMapCastles) {
            hide_layers += ",${HIDELAYERS_FC_CASTLES}"
        }
        if (!contestMapParams.contestMapPowerlines) {
            hide_layers += ",${HIDELAYERS_FC_POWERLINES}"
        }
        if (!contestMapParams.contestMapWindpowerstations) {
            hide_layers += ",${HIDELAYERS_FC_WINDPOWERSTATIONS}"
        }
        switch (contestMapParams.contestMapSmallRoadsGrade) {
            case Defs.CONTESTMAPSMALLROADSGRADE_1:
                hide_layers += ",${HIDELAYERS_FC_SMALLROADS_2},${HIDELAYERS_FC_SMALLROADS_3},${HIDELAYERS_FC_SMALLROADS_4},${HIDELAYERS_FC_SMALLROADS_5}"
                break
            case Defs.CONTESTMAPSMALLROADSGRADE_2:
                hide_layers += ",${HIDELAYERS_FC_SMALLROADS_3},${HIDELAYERS_FC_SMALLROADS_4},${HIDELAYERS_FC_SMALLROADS_5}"
                break
            case Defs.CONTESTMAPSMALLROADSGRADE_3:
                hide_layers += ",${HIDELAYERS_FC_SMALLROADS_4},${HIDELAYERS_FC_SMALLROADS_5}"
                break
            case Defs.CONTESTMAPSMALLROADSGRADE_4:
                hide_layers += ",${HIDELAYERS_FC_SMALLROADS_5}"
                break
            case Defs.CONTESTMAPSMALLROADSGRADE_5:
                hide_layers += ""
                break
            default: // non small roads
                hide_layers += ",${HIDELAYERS_FC_SMALLROADS_1},${HIDELAYERS_FC_SMALLROADS_2},${HIDELAYERS_FC_SMALLROADS_3},${HIDELAYERS_FC_SMALLROADS_4},${HIDELAYERS_FC_SMALLROADS_5}"
                break
        }
        if (hide_layers.startsWith(',')) {
            hide_layers = hide_layers.substring(1)
        }
        
        Map center_route = route_instance.GetCenter(contestMapParams)
        BigDecimal center_latitude = center_route.centerLatitude
        BigDecimal center_longitude = center_route.centerLongitude
        
        int map_scale = route_instance.mapScale
        BigDecimal scalebar_x_diff = 5*Defs.CONTESTMAPSCALE_200000/map_scale*GpxService.kmPerNM // 1 NM (9.26mm)
        
        int print_width = 0 // mm
        int print_height = 0 // mm
        int min_print_height = 0 // mm
        boolean alternate_pos = false
        String paper_size = ""
        switch (contestMapParams.contestMapPrintSize) {
            case Defs.CONTESTMAPPRINTSIZE_A4:
                paper_size = "A4"
                if (contestMapParams.contestMapPrintLandscape) {
                    print_width = A4_LONG
                    print_height = A4_SHORT
                } else {
                    print_width = A4_SHORT
                    print_height = A4_LONG
                }
                break
            case Defs.CONTESTMAPPRINTSIZE_A3:
                paper_size = "A3"
                if (contestMapParams.contestMapPrintLandscape) {
                    print_width = A3_LONG
                    print_height = A3_SHORT
                } else {
                    print_width = A3_SHORT
                    print_height = A3_LONG
                }
                break
            case Defs.CONTESTMAPPRINTSIZE_A2:
                paper_size = "A2"
                if (contestMapParams.contestMapPrintLandscape) {
                    print_width = A2_LONG
                    print_height = A2_SHORT
                } else {
                    print_width = A2_SHORT
                    print_height = A2_LONG
                }
                break
            case Defs.CONTESTMAPPRINTSIZE_A1:
                paper_size = "A1"
                if (contestMapParams.contestMapPrintLandscape) {
                    print_width = A1_LONG
                    print_height = A1_SHORT
                } else {
                    print_width = A1_SHORT
                    print_height = A1_LONG
                    alternate_pos = true
                }
                break
            case Defs.CONTESTMAPPRINTSIZE_ANR:
                paper_size = "ANR"
                print_width = ANR_LONG
                print_height = ANR_SHORT
                break
            case Defs.CONTESTMAPPRINTSIZE_AIRPORTAREA:
                print_width = 2*AIRPORTAREA_A3_DISTANCE
                print_height = 2*AIRPORTAREA_A3_DISTANCE
                paper_size = "${print_width}x${print_height}mm"
                alternate_pos = true
                break
            case Defs.CONTESTMAPPRINTSIZE_ANRAIRPORTAREA:
                print_width = 2*AIRPORTAREA_A4_DISTANCE
                print_height = 2*AIRPORTAREA_A4_DISTANCE
                paper_size = "${print_width}x${print_height}mm"
                alternate_pos = true
                break
        }
        if (contestMapParams.contestMapPrintLandscape) {
            min_print_height = A4_SHORT
        } else {
            min_print_height = A4_LONG
        }
        print_width -= 2*MARGIN
        print_height -= 2*MARGIN
        BigDecimal print_width_nm = map_scale * print_width / Defs.mmPerNM
        BigDecimal print_height_nm = map_scale * print_height / Defs.mmPerNM
        Map rect_width = AviationMath.getShowPoint(center_latitude, center_longitude, print_width_nm / 2 - GpxService.CONTESTMAP_RUNWAY_FRAME_DISTANCE, GRATICULE_SCALEBAR_LEN)
        Map rect_height = AviationMath.getShowPoint(center_latitude, center_longitude, print_height_nm / 2 - GpxService.CONTESTMAP_RUNWAY_FRAME_DISTANCE, GRATICULE_SCALEBAR_LEN)
        
        if (contestMapParams.contestMapCenterHorizontalPos != HorizontalPos.Center || contestMapParams.contestMapCenterVerticalPos != VerticalPos.Center) {
            boolean correct_rect = false
            switch (contestMapParams.contestMapCenterHorizontalPos) {
                case HorizontalPos.Left:
                    center_longitude = rect_width.lonmax
                    correct_rect = true
                    break
                case HorizontalPos.Right:
                    center_longitude = rect_width.lonmin
                    correct_rect = true
                    break
            }
            switch (contestMapParams.contestMapCenterVerticalPos) {
                case VerticalPos.Top:
                    center_latitude = rect_height.latmin
                    correct_rect = true
                    break
                case VerticalPos.Bottom:
                    center_latitude = rect_height.latmax
                    correct_rect = true
                    break
            }
            if (correct_rect) {
            }
        }

        rect_width = AviationMath.getShowPoint(center_latitude, center_longitude, print_width_nm / 2, GRATICULE_SCALEBAR_LEN)        
        rect_height = AviationMath.getShowPoint(center_latitude, center_longitude, print_height_nm / 2, GRATICULE_SCALEBAR_LEN)
        println "center_latitude=${center_latitude}, center_longitude=${center_longitude}"
        println "Width:  ${print_width} mm, ${print_width_nm} NM, ${rect_width}"
        println "Height: ${print_height} mm, ${print_height_nm} NM, ${rect_height}"
        
        BigDecimal latlon_relation = 1
        if (contestMapParams.taskCreator) {
            latlon_relation = AviationMath.getLatLonDistanceRelation(center_latitude)
            print_height *= latlon_relation
        }
        int contest_title_ypos = print_height - CONTEST_TITLE_YPOS_TOP
        int route_title_ypos = contest_title_ypos - ROUTE_TITLE_YPOS_CONTEST_TITLE
        int edition_title_ypos = route_title_ypos - EDITION_TITLE_YPOS_CONTEST_TITLE
        int text_xpos_left = TEXT_XPOS_LEFT
        int text_xpos_right = print_width - TEXT_XPOS_RIGHT
        int scalebar_xpos = text_xpos_right - TEXT_XPOS_RIGHT*scalebar_x_diff
        int scalebar_ypos = print_height - SCALEBAR_YPOS_TOP
        int scalbar_text_ypos = scalebar_ypos - 3   // 3mm nach unten
        
        //...........................................................................................
        String region_style = "" // printmaps_webservice_capabilities.json -> ConfigStyles -> Name, printmaps_buildservice.yaml -> styles -> name
        String mapdata_date = ""
        printstart "Get service capabilities"
        Map status = CallPrintServer("/capabilities/service", [PrintMapTools.HEADER_ACCEPT], "GET", PrintMapTools.DataType.JSON, "")
        if (status.responseCode == 200) {

            // check full page
            println "Full page $rect_height.latmin $rect_height.latmax $rect_width.lonmin $rect_width.lonmax"
            for (def config_style in status.json.ConfigStyles) {
                if (config_style.Name.startsWith('region-')) {
                    if (contestMapParams.contestMapDevStyle) {
                        if (config_style.Name == 'region-dev') {
                            BigDecimal lat_bottom = get_value(config_style.LongDescription, 'Bottom(Lat):')
                            BigDecimal lat_top = get_value(config_style.LongDescription, 'Top(Lat):')
                            BigDecimal lon_left = get_value(config_style.LongDescription, 'Left(Lon):')
                            BigDecimal lon_right = get_value(config_style.LongDescription, 'Right(Lon):')
                            print "Check full page for $config_style.Name ($lat_bottom $lat_top $lon_left $lon_right)"
                            if (rect_height.latmin >= lat_bottom && rect_height.latmax <= lat_top && rect_width.lonmin >= lon_left && rect_width.lonmax <= lon_right) {
                                region_style = config_style.Name
                                mapdata_date = config_style.Date
                                println " found."
                                break
                            }
                            println ""
                        }
                    } else {
                        if (config_style.Name != 'region-dev') {
                            BigDecimal lat_bottom = get_value(config_style.LongDescription, 'Bottom(Lat):')
                            BigDecimal lat_top = get_value(config_style.LongDescription, 'Top(Lat):')
                            BigDecimal lon_left = get_value(config_style.LongDescription, 'Left(Lon):')
                            BigDecimal lon_right = get_value(config_style.LongDescription, 'Right(Lon):')
                            print "Check full page for $config_style.Name ($lat_bottom $lat_top $lon_left $lon_right)"
                            if (rect_height.latmin >= lat_bottom && rect_height.latmax <= lat_top && rect_width.lonmin >= lon_left && rect_width.lonmax <= lon_right) {
                                region_style = config_style.Name
                                mapdata_date = config_style.Date
                                println " found."
                                break
                            }
                            println ""
                        }
                    }
                }
            }
            
            // check center page
            if (!region_style) {
                for (def config_style in status.json.ConfigStyles) {
                    if (config_style.Name.startsWith('region-')) {
                        if (contestMapParams.contestMapDevStyle) {
                            if (config_style.Name == 'region-dev') {
                                BigDecimal lat_bottom = get_value(config_style.LongDescription, 'Bottom(Lat):')
                                BigDecimal lat_top = get_value(config_style.LongDescription, 'Top(Lat):')
                                BigDecimal lon_left = get_value(config_style.LongDescription, 'Left(Lon):')
                                BigDecimal lon_right = get_value(config_style.LongDescription, 'Right(Lon):')
                                print "Check center for $config_style.Name ($lat_bottom $lat_top $lon_left $lon_right)"
                                if (center_latitude >= lat_bottom && center_latitude <= lat_top && center_longitude >= lon_left && center_longitude <= lon_right) {
                                    region_style = config_style.Name
                                    mapdata_date = config_style.Date
                                    println " found."
                                    break
                                }
                                println ""
                            }
                        } else {
                            if (config_style.Name != 'region-dev') {
                                BigDecimal lat_bottom = get_value(config_style.LongDescription, 'Bottom(Lat):')
                                BigDecimal lat_top = get_value(config_style.LongDescription, 'Top(Lat):')
                                BigDecimal lon_left = get_value(config_style.LongDescription, 'Left(Lon):')
                                BigDecimal lon_right = get_value(config_style.LongDescription, 'Right(Lon):')
                                print "Check center for $config_style.Name ($lat_bottom $lat_top $lon_left $lon_right)"
                                if (center_latitude >= lat_bottom && center_latitude <= lat_top && center_longitude >= lon_left && center_longitude <= lon_right) {
                                    region_style = config_style.Name
                                    mapdata_date = config_style.Date
                                    println " found."
                                    break
                                }
                                println ""
                            }
                        }
                    }
                }
            }
            
            if (PrintMapTools.IsLocalPrintmapsRunning()) {
                mapdata_date = PrintMapTools.GetLocalPrintmapsDate(route_instance.contest)
            }
            
            if (region_style) {
                printdone "${region_style} ${mapdata_date}"
            } else {
                printerror "No region found"
                ret.message = getMsg('fc.contestmap.noregionerror', [center_latitude, center_longitude], false)
                return ret
            }
        } else {
            printerror status.responseCode
        }
        
        //...........................................................................................
        String edition_text = "${getMsg('fc.contestmap.edition',true)} ${contestMapParams.contestMapEdition}"
        String generator_date = new Date().format("dd.MM.yyyy HH:mm")
        String generator_text = getMsg('fc.contestmap.generator.flightcontest',[generator_date],true)
        String copyright_text = getMsg('fc.contestmap.copyright.osm',true)
        if (mapdata_date) {
            copyright_text += " (${getMsg('fc.contestmap.mapdata.date',[mapdata_date],true)})"
        }
        String copyright_date = GeoDataService.ReadTxtFile(Defs.FCSAVE_FILE_GEODATA_DATE)
        if (contestMapParams.contestMapChateaus || contestMapParams.contestMapPeaks) {
            copyright_text += ", ${getMsg('fc.contestmap.copyright.bkg',[copyright_date],true)}"
        }
        copyright_text += ", ${getMsg('fc.contestmap.copyright.srtm',[],true)}"
        // copyright_text += ", ${getMsg('fc.contestmap.copyright.otm',[],true)}"
        if (BootStrap.global.IsOpenAIP() && ((contestMapParams.contestMapAirspaces && (route_instance.contestMapAirspacesLayer2 || route_instance2?.contestMapAirspacesLayer2)) || (openaip_airfields && (route_instance.contestMapAirfieldsData || route_instance2?.contestMapAirfieldsData)))) {
            copyright_text += ", ${getMsg('fc.contestmap.copyright.openaip',[],true)}"
        }
        String user_text = ""
        if (!contestMapParams.taskCreator) {
            user_text = """{
                "Style": "<LineSymbolizer stroke='black' stroke-width='${FRAME_STROKE_WIDTH}' stroke-linecap='butt' />",
                "WellKnownText": "LINESTRING(0.0 0.0, 0.0 ${print_height}, ${print_width} ${print_height}, ${print_width} 0.0, 0.0 0.0)"
            },
            {
                "Style": "<TextSymbolizer fontset-name='fontset-2' size='${CONTEST_TITLE_FONT_SIZE}' fill='black' horizontal-alignment='right' halo-radius='1' halo-fill='white' allow-overlap='true'>'${contestMapParams.contestTitle}'</TextSymbolizer>",
                "WellKnownText": "POINT(${text_xpos_left} ${contest_title_ypos})"
            },
            {
                "Style": "<TextSymbolizer fontset-name='fontset-2' size='${ROUTE_TITLE_FONT_SIZE}' fill='black' horizontal-alignment='right' halo-radius='1' halo-fill='white' allow-overlap='true'>'${contestMapParams.routeTitle}'</TextSymbolizer>",
                "WellKnownText": "POINT(${text_xpos_left} ${route_title_ypos})"
            },
            {
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${EDITION_TITLE_FONT_SIZE}' fill='black' horizontal-alignment='right' halo-radius='1' halo-fill='white' allow-overlap='true'>'${edition_text}'</TextSymbolizer>",
                "WellKnownText": "POINT(${text_xpos_left} ${edition_title_ypos})"
            },
            {
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${BOTTOM_TEXT_FONT_SIZE}' fill='black' horizontal-alignment='right' halo-radius='1' halo-fill='white' allow-overlap='true'>'${generator_text}'</TextSymbolizer>",
                "WellKnownText": "POINT(${text_xpos_left} ${BOTTOM_TEXT_YPOS2})"
            },
            {
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${BOTTOM_TEXT_FONT_SIZE}' fill='black' horizontal-alignment='right' halo-radius='1' halo-fill='white' allow-overlap='true'>'${copyright_text}'</TextSymbolizer>",
                "WellKnownText": "POINT(${text_xpos_left} ${BOTTOM_TEXT_YPOS})"
            },
            {
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${BOTTOM_TEXT_FONT_SIZE}' fill='black' horizontal-alignment='left' halo-radius='1' halo-fill='white' allow-overlap='true'>'${getMsg('fc.contestmap.scale',true)} 1:${map_scale}, ${paper_size}'</TextSymbolizer>",
                "WellKnownText": "POINT(${text_xpos_right} ${BOTTOM_TEXT_YPOS})"
            },
            {
                "Style": "<LineSymbolizer stroke='black' stroke-width='${SCALEBAR_STROKE_WIDTH}' stroke-opacity='1' stroke-linecap='butt' />",
                "WellKnownText": "LINESTRING(${scalebar_xpos} ${scalebar_ypos}, ${scalebar_xpos + scalebar_x_diff} ${scalebar_ypos})"
            },            
            {
                "Style": "<LineSymbolizer stroke='black' stroke-width='${SCALEBAR_STROKE_WIDTH}' stroke-opacity='0.2' stroke-linecap='butt' />",
                "WellKnownText": "LINESTRING(${scalebar_xpos + scalebar_x_diff} ${scalebar_ypos}, ${scalebar_xpos + 2*scalebar_x_diff} ${scalebar_ypos})"
            },            
            {
                "Style": "<LineSymbolizer stroke='black' stroke-width='${SCALEBAR_STROKE_WIDTH}' stroke-opacity='1' stroke-linecap='butt' />",
                "WellKnownText": "LINESTRING(${scalebar_xpos + 2*scalebar_x_diff} ${scalebar_ypos}, ${scalebar_xpos + 3*scalebar_x_diff} ${scalebar_ypos})"
            },            
            {
                "Style": "<LineSymbolizer stroke='black' stroke-width='${SCALEBAR_STROKE_WIDTH}' stroke-opacity='0.2' stroke-linecap='butt' />",
                "WellKnownText": "LINESTRING(${scalebar_xpos + 3*scalebar_x_diff} ${scalebar_ypos}, ${scalebar_xpos + 4*scalebar_x_diff} ${scalebar_ypos})"
            },            
            {
                "Style": "<LineSymbolizer stroke='black' stroke-width='${SCALEBAR_STROKE_WIDTH}' stroke-opacity='1' stroke-linecap='butt' />",
                "WellKnownText": "LINESTRING(${scalebar_xpos + 4*scalebar_x_diff} ${scalebar_ypos}, ${scalebar_xpos + 5*scalebar_x_diff} ${scalebar_ypos})"
            },
            {
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${SCALEBAR_TITLE_FONT_SIZE}' fill='black' horizontal-alignment='left' halo-radius='1' halo-fill='white' allow-overlap='true'>'${SCALEBAR_TITLE}'</TextSymbolizer>",
                "WellKnownText": "POINT(${text_xpos_right} ${scalbar_text_ypos})"
            },"""
            int ypos_corridor_width = BOTTOM_TEXT_YPOS3
            if (!contestMapParams.contestMapContourLines) {
                ypos_corridor_width = BOTTOM_TEXT_YPOS2
            }
            if (contestMapParams.contestMapCorridorWidth) {
                user_text += """{
                    "Style": "<TextSymbolizer fontset-name='fontset-0' size='${BOTTOM_TEXT_FONT_SIZE}' fill='black' horizontal-alignment='left' halo-radius='1' halo-fill='white' allow-overlap='true'>'${getMsg('fc.corridorwidth',true)} ${FcMath.DistanceStr(contestMapParams.contestMapCorridorWidth)}${getMsg('fc.mile',true)}'</TextSymbolizer>",
                    "WellKnownText": "POINT(${text_xpos_right} ${ypos_corridor_width})"
                },"""
            }
            if (contestMapParams.contestMapContourLines) {
                user_text += """{
                    "Style": "<TextSymbolizer fontset-name='fontset-0' size='${BOTTOM_TEXT_FONT_SIZE}' fill='black' horizontal-alignment='left' halo-radius='1' halo-fill='white' allow-overlap='true'>'${getMsg('fc.contestmap.contestmapcontourlines',true)} ${contestMapParams.contestMapContourLines}${getMsg('fc.contestmap.contestmapcontourlines.unit',true)}'</TextSymbolizer>",
                    "WellKnownText": "POINT(${text_xpos_right} ${BOTTOM_TEXT_YPOS2})"
                },"""
            }
        }
        
        String gpx_file_name = contestMapParams.gpxFileName.replaceAll('\\\\', '/')
        String gpx_short_file_name = gpx_file_name.substring(gpx_file_name.lastIndexOf('/')+1)
        String gpx_lines = """{
            "Style": "<LineSymbolizer stroke='black' stroke-width='${TRACK_STROKE_WIDTH}' stroke-linecap='round' />",
            "SRS": "${input_srs}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "routes"
        },
        {
            "Style": "<LineSymbolizer stroke='red' stroke-width='${TRACK_STROKE_WIDTH}' stroke-linecap='round' />",
            "SRS": "${input_srs}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "tracks"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-2' size='[src]' fill='black' halo-radius='1' halo-fill='white' allow-overlap='true'>[name]</TextSymbolizer>",
            "SRS": "${input_srs}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "waypoints"
        },
        {
            "Style": "<MarkersSymbolizer file='[sym]' transform='${GEODATA_SYMBOL_SCALE}' placement='point' allow-overlap='true'/>",
            "SRS": "${input_srs}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "waypoints"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-2' size='${SYMBOL_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dx='10' placement='point'>[type]</TextSymbolizer>",
            "SRS": "${input_srs}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "waypoints"
        }"""
        
        String graticule_lines = ""
        if (!contestMapParams.taskCreator && graticule_file_name) {
            if (create_graticule_csv(graticule_file_name, center_latitude, center_longitude, map_scale, print_width, print_height, min_print_height, alternate_pos)) {
                String graticule_short_file_name = graticule_file_name.substring(graticule_file_name.lastIndexOf('/')+1)
                graticule_lines = """,{
                    "Style": "<PolygonSymbolizer fill='lightgrey' />",
                    "SRS": "${input_srs}",
                    "Type": "csv",
                    "File": "${graticule_short_file_name}",
                    "Layer": ""
                },
                {
                    "Style": "<LineSymbolizer stroke='black' stroke-width='${GRATICULE_STROKE_WIDTH}' stroke-linecap='round' />",
                    "SRS": "${input_srs}",
                    "Type": "csv",
                    "File": "${graticule_short_file_name}",
                    "Layer": ""
                },
                {
                    "Style": "<TextSymbolizer fontset-name='fontset-0' size='${GRATICULE_TEXT_FONT_SIZE}' fill='black' halo-radius='1' halo-fill='white' allow-overlap='true' horizontal-alignment='right' dx='5' dy='8' placement='vertex'>[name]</TextSymbolizer>",
                    "SRS": "${input_srs}",
                    "Type": "csv",
                    "File": "${graticule_short_file_name}",
                    "Layer": ""
                }"""
            } else {
                graticule_file_name = ""
            }
        }
        
        String mapobjects_lines = ""
        String mapobjects_file_name = ""
        List mapobjects_symbol_filenames = []
        if (contestMapParams.contestMapAdditionals) {
            String uuid = UUID.randomUUID().toString()
            String file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/MAPOBJECTS-${uuid}-UPLOAD.csv"
            String symbol_praefix = "SYMBOL-${uuid}-"
            mapobjects_symbol_filenames = create_mapobjects_csv(route_instance, contestMapParams.webRootDir, file_name, false, symbol_praefix)
            mapobjects_file_name = contestMapParams.webRootDir + file_name
            String mapobjects_short_file_name = mapobjects_file_name.substring(mapobjects_file_name.lastIndexOf('/')+1) // transform='scale(0.5, 0.5)'
            mapobjects_lines = """,{
                "Style": "<MarkersSymbolizer file='[symbol]' transform='${GEODATA_SYMBOL_SCALE}' allow-overlap='true' placement='point' />",
                "SRS": "${input_srs}",
                "Type": "csv",
                "File": "${mapobjects_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${OBJECT_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' horizontal-alignment='[horizontal-alignment]' vertical-alignment='[vertical-alignment]' dy='[dy]' placement='point'>[name]</TextSymbolizer>",
                "SRS": "${input_srs}",
                "Type": "csv",
                "File": "${mapobjects_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String airspaces_lines = ""
        String airspaces_file_name = "" // will be uploaded
        if (contestMapParams.contestMapAirspaces) {
			if (BootStrap.global.IsOpenAIP()) {
				String uuid = UUID.randomUUID().toString()
				String file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/AIRSPACES-${uuid}-UPLOAD.kmz"
				Map ret2 = openAIPService.WriteAirspaces2KMZ(route_instance, contestMapParams.webRootDir, file_name, false, false)
				if (!ret2.ok) {
                    ret.message = getMsg('fc.contestmap.contestmapairspaces.kmzexport.missedairspaces', [ret2.missingAirspaces], false)
                    printerror ret.message
                    return ret
				}
				airspaces_file_name = contestMapParams.webRootDir + file_name
			} else {
				airspaces_file_name = Defs.FCSAVE_FILE_GEODATA_AIRSPACES
			}
            airspaces_lines += get_airspaces_lines(route_instance.contestMapAirspacesLayer2, airspaces_file_name, input_srs)
            if (route_instance.contestMapShowMapObjectsFromRouteID) {
                if (route_instance2) {
                    airspaces_lines += get_airspaces_lines(route_instance2.contestMapAirspacesLayer2, airspaces_file_name, input_srs)
                }
            }
        }
        
        String airports_lines = ""
        String airports_file_name = ""
        String uuid = UUID.randomUUID().toString()
        String file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/AIRPORTS-${uuid}-UPLOAD.csv"
        openAIPService.WriteAirfields2CSV(route_instance, contestMapParams.webRootDir, file_name, false, Defs.CONTESTMAPPOINTS_AIRFIELDS, openaip_airfields, contestMapParams.contestMapAdditionals)
        airports_file_name = contestMapParams.webRootDir + file_name
        String airports_short_file_name = airports_file_name.substring(airports_file_name.lastIndexOf('/')+1) // transform='scale(0.5, 0.5)'
        if (contestMapParams.taskCreator) {
            airports_lines = """,{
                "Style": "<MarkersSymbolizer file='[${OpenAIPService.CSV_AIRPORT_RUNWAY}]' transform='scale(0.6, ${latlon_relation*0.6}),rotate([${OpenAIPService.CSV_AIRPORT_HEADING}],0,0)' allow-overlap='true' placement='point' />",
                "SRS": "${input_srs}",
                "Type": "csv",
                "File": "${airports_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<MarkersSymbolizer file='[${OpenAIPService.CSV_AIRPORT_TYPE}]' transform='scale(0.6, ${latlon_relation*0.6})' allow-overlap='true' placement='point' />",
                "SRS": "${input_srs}",
                "Type": "csv",
                "File": "${airports_short_file_name}",
                "Layer": ""
            }"""
        } else {
            airports_lines = """,{
                "Style": "<MarkersSymbolizer file='[${OpenAIPService.CSV_AIRPORT_RUNWAY}]' transform='rotate([${OpenAIPService.CSV_AIRPORT_HEADING}],0,0),scale(0.6, 0.6)' allow-overlap='true' placement='point' />",
                "SRS": "${input_srs}",
                "Type": "csv",
                "File": "${airports_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<MarkersSymbolizer file='[${OpenAIPService.CSV_AIRPORT_TYPE}]' transform='scale(0.6, 0.6)' allow-overlap='true' placement='point' />",
                "SRS": "${input_srs}",
                "Type": "csv",
                "File": "${airports_short_file_name}",
                "Layer": ""
            }"""
        }
        airports_lines += """,{
            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${OBJECT_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dy='${OBJECT_TEXT_LINE1_DY}' placement='point'>[${OpenAIPService.CSV_AIRPORT_NAME}]</TextSymbolizer>",
            "SRS": "${input_srs}",
            "Type": "csv",
            "File": "${airports_short_file_name}",
            "Layer": ""
        }
        ,{
            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${OBJECT_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dy='${OBJECT_TEXT_LINE2_DY}' placement='point'>[${OpenAIPService.CSV_AIRPORT_ICAO}]</TextSymbolizer>",
            "SRS": "${input_srs}",
            "Type": "csv",
            "File": "${airports_short_file_name}",
            "Layer": ""
        }"""
        
        printstart "Run job"
        println "map_scale: 1:${map_scale}"
        println "print_width: $print_width mm"
        println "print_height: $print_height mm"
    
        String printjob_id = ""
        boolean job_started = false
        boolean job_successfully = false
        int img_width = 0
        int img_height = 0
        
        //...........................................................................................
        if (region_style) {
            printstart "Create job"
            status = CallPrintServer("/metadata", [HEADER_CONTENTTYPE,PrintMapTools.HEADER_ACCEPT], "POST", PrintMapTools.DataType.JSON,
            """{
                "Data": {
                    "Type": "maps",
                    "ID": "",
                    "Attributes": {
                        "Fileformat": "png",
                        "Scale": ${map_scale},
                        "PrintWidth": ${print_width},
                        "PrintHeight": ${print_height},
                        "Latitude": ${center_latitude},
                        "Longitude": ${center_longitude},
                        "Style": "${region_style}",
                        "Projection": "${map_projection}",
                        "HideLayers": "${hide_layers}",
                        "UserObjects": [
                            ${user_text}
                            ${gpx_lines}
                            ${graticule_lines}
                            ${mapobjects_lines}
                            ${airspaces_lines}
                            ${airports_lines}
                        ]
                    }
                }
            }""")
            
            if (status.responseCode) {
                if (status.json) {
                    printdone "responseCode=${status.responseCode}, printjob_id=${status.json.Data.ID}"
                } else {
                    printerror "responseCode=${status.responseCode}"
                }
            } else {
                printerror ""
            }
            if (status.responseCode == 201) {
                printjob_id = status.json.Data.ID
            } else {
                ret.message = getMsg('fc.contestmap.invalidprintjobdata', [], false)
            }
        }
    
        //...........................................................................................
        if (printjob_id) {
            // route
            printstart "Upload gpx"
            FileUpload("/upload/${printjob_id}", gpx_file_name)
            printdone ""
            // symbols
            File map_images_dir = new File(contestMapParams.webRootDir + "images/map")
			if (map_images_dir.exists()) {
				printstart "Upload all images of ${map_images_dir.name}"
				map_images_dir.eachFile() { File map_image ->
					if (map_image.isFile()) {
						println "Upload ${map_image.name}"
						FileUpload("/upload/${printjob_id}", map_image.canonicalPath.replace("\\", "/"))
					}
				}
				printdone ""
			}
            // additional images
            File geodata_images_dir = new File(Defs.FCSAVE_FOLDER_GEODATA_IMAGES)
			if (geodata_images_dir.exists()) {
				printstart "Upload all images of ${Defs.FCSAVE_FOLDER_GEODATA_IMAGES}"
				geodata_images_dir.eachFile() { File geodata_image ->
					if (geodata_image.isFile()) {
						println "Upload ${geodata_image.name}"
						FileUpload("/upload/${printjob_id}", geodata_image.canonicalPath.replace("\\", "/"))
					}
				}
				printdone ""
			}
            // photo image
            if (contestMapParams.contestMapEnroutePhotos) {
                printstart "Upload fcphoto.png"
                FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + "GM_Utils/Icons/fcphoto.png")
                printdone ""
            }
            // canvas images
            if (contestMapParams.contestMapEnrouteCanvas) {
                EnrouteCanvasSign.each { enroute_canvas_sign ->
                    if (enroute_canvas_sign.imageName) {
                        printstart "Upload ${enroute_canvas_sign.imageName}"
                        FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + enroute_canvas_sign.imageName)
                        printdone ""
                    }
                }
            }
            // turnpoint images
            if (contestMapParams.contestMapTurnpointSign) {
                TurnpointSign.each { turnpoint_sign ->
                    if (turnpoint_sign.imagePngName) {
                        printstart "Upload ${turnpoint_sign.imagePngName}"
                        FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + turnpoint_sign.imagePngName)
                        printdone ""
                    }
                }
            }
            if (!contestMapParams.taskCreator && graticule_file_name) {
                printstart "Upload graticule"
                FileUpload("/upload/${printjob_id}", graticule_file_name)
                printdone ""
            }
            if (mapobjects_file_name) {
                printstart "Upload mapobjects"
                FileUpload("/upload/${printjob_id}", mapobjects_file_name)
                printdone ""
            }
            if (mapobjects_symbol_filenames) {
                for (String symbol_filename in mapobjects_symbol_filenames) {
                    printstart "Upload symbols"
                    FileUpload("/upload/${printjob_id}", symbol_filename)
                    printdone ""
                }
            }
            if (airspaces_file_name) {
                printstart "Upload airspaces"
                FileUpload("/upload/${printjob_id}", airspaces_file_name)
                printdone ""
            }
            if (airports_file_name) {
                printstart "Upload airports"
                FileUpload("/upload/${printjob_id}", airports_file_name)
                printdone ""
            }
        }
        
        if (mapobjects_file_name) {
            gpxService.DeleteFile(mapobjects_file_name)
        }
        if (mapobjects_symbol_filenames) {
            for (String symbol_filename in mapobjects_symbol_filenames) {
                gpxService.DeleteFile(symbol_filename)
            }
        }
        if (airspaces_file_name) {
            if (BootStrap.global.IsOpenAIP()) {
                gpxService.DeleteFile(airspaces_file_name)
            }
        }
        if (airports_file_name) {
            if (BootStrap.global.IsOpenAIP()) {
                gpxService.DeleteFile(airports_file_name)
            }
        }
        
        //...........................................................................................
        if (printjob_id) {
            printstart "Start job"
            status = CallPrintServer("/mapfile", [HEADER_CONTENTTYPE,PrintMapTools.HEADER_ACCEPT], "POST", PrintMapTools.DataType.JSON, """
            {
                "Data": {
                    "Type": "maps",
                    "ID": "${printjob_id}"
                }
            }"""
            )
            if (status.responseCode == 202) {
                job_started = true
                BootStrap.global.CountFCMap()
            } else {
                ret.message = getMsg('fc.contestmap.printjobnotstarted', [status.responseCode], false)
            }
            printdone "responseCode=${status.responseCode}"
        }
        
        //...........................................................................................
        if (job_started) {
            // save printjob
            /*
            printstart "Generate ${printjob_filename}"
            File printjob_file = new File(printjob_filename)
            BufferedWriter printjob_writer = printjob_file.newWriter()
            printjob_writer << contestMapParams.routeId
            printjob_writer.close()
            printdone ""
            */
            
            // save printjob_id
            printstart "Generate ${printjobid_filename}"
            File printjobid_file = new File(printjobid_filename)
            BufferedWriter printjobid_writer = printjobid_file.newWriter()
            printjobid_writer << printjob_id
            printjobid_writer << "\n"
            printjobid_writer << contestMapParams.pngFileName
            printjobid_writer << "\n"
            printjobid_writer << contestMapParams.contestMapPrintLandscape
            printjobid_writer << "\n"
            printjobid_writer << contestMapParams.contestMapPrintSize
            printjobid_writer << "\n"
            printjobid_writer << false // contestMapParams.printColorChanges
            printjobid_writer << "\n"
            printjobid_writer << map_projection
            printjobid_writer << "\n"
            printjobid_writer << FcMath.EnrouteExportDistanceStr(contestMapParams.contestMapCorridorWidth)
            printjobid_writer.close()
            printdone ""
            
            // save print file id
            printstart "Generate ${printfileid_filename}"
            File printfileid_file = new File(printfileid_filename)
            BufferedWriter printfileid_writer = printfileid_file.newWriter()
            printfileid_writer << contestMapParams.pngFileName
            printfileid_writer << "\n"
            printfileid_writer << contestMapParams.contestMapPrintLandscape
            printfileid_writer << "\n"
            printfileid_writer << contestMapParams.contestMapPrintSize
            printfileid_writer << "\n"
            printfileid_writer << contestMapParams.routeTitle
            printfileid_writer << "\n"
            printfileid_writer << map_projection
            printfileid_writer.close()
            printdone ""
            
            // start job
            printstart "Start OsmPrintMapJob"
            OsmPrintMapJob.schedule(
                1000*Defs.OSMPRINTMAP_RUNSECONDS,
                -1,
                [(Defs.OSMPRINTMAP_ACTION):Defs.OSMPRINTMAP_ACTION_CHECKJOB, 
                 (Defs.OSMPRINTMAP_JOBFILENAME):printjob_filename,
                 (Defs.OSMPRINTMAP_JOBID):printjob_id,
                 (Defs.OSMPRINTMAP_JOBIDFILENAME):printjobid_filename,
                 (Defs.OSMPRINTMAP_FILEIDFILENAME):printfileid_filename,
                 (Defs.OSMPRINTMAP_PNGFILENAME):contestMapParams.pngFileName,
                 (Defs.OSMPRINTMAP_PRINTLANDSCAPE):contestMapParams.contestMapPrintLandscape,
                 (Defs.OSMPRINTMAP_PRINTSIZE):contestMapParams.contestMapPrintSize,
                 (Defs.OSMPRINTMAP_PRINTPROJECTION):map_projection,
                 (Defs.OSMPRINTMAP_PRINTCORRIDORWIDTH):FcMath.EnrouteExportDistanceStr(contestMapParams.contestMapCorridorWidth),
                 (Defs.OSMPRINTMAP_PRINTCOLORCHANGES):false // contestMapParams.printColorChanges
                ]
            )
            printdone ""
            
            ret.ok = true
        }
        
        printdone ""
        return ret
    }
    
    //--------------------------------------------------------------------------
    private String get_airspaces_lines(String airspacesLayer, String airspacesFileName, String inputSRS)
    {
        String airspaces_lines = ""
        String airspaces_short_file_name = airspacesFileName.substring(airspacesFileName.lastIndexOf('/')+1)
        int additional_airspace_no = 0
        for (String layer in airspacesLayer.split("\n")) {
            if (layer && layer.trim()) {
                String airspace_layer = layer.trim()
                if (!airspace_layer.startsWith(Defs.IGNORE_LINE)) {
                    String airspace_filename = airspaces_short_file_name
                    String airspace_text = airspace_layer
                    String airspace_textsize= '10'
                    String airspace_textspacing = '1'
                    String airspace_textcolor = 'black'
                    String airspace_fillcolor = 'steelblue'
                    String airspace_fillopacity = '0.2'
                    boolean first_style = true
                    for (String airspace_style in Tools.Split(airspace_layer, AIRSPACE_LAYER_STYLE_SEPARATOR)) {
                        List airspace_style_values = Tools.Split(airspace_style.trim(), AIRSPACE_LAYER_STYLE_KEY_VALUE_SEPARATOR)
                        if (airspace_style_values.size() == 1) {
                            if (first_style) {
                                airspace_layer = airspace_style_values[0].trim()
                                if (airspace_layer == OsmPrintMapService.ADDITIONAL_AIRSPACE_NAME) {
                                    additional_airspace_no++
                                    airspace_layer += "_${additional_airspace_no}"
                                }
                                airspace_text = airspace_layer
                            } else if (airspace_text) {
                                airspace_text += OsmPrintMapService.AIRSPACE_LAYER_STYLE_SEPARATOR + airspace_style
                            }
                        } else if (airspace_style_values.size() == 2) {
                            switch (airspace_style_values[0].trim()) {
                                case 'file': 
                                    airspace_filename = airspace_style_values[1].trim()
                                    break
                                case 'text': 
                                    airspace_text = airspace_style_values[1]
                                    break
                                case 'textsize': 
                                    airspace_textsize = airspace_style_values[1].trim()
                                    break
                                case 'textspacing': 
                                    airspace_textspacing = airspace_style_values[1].trim()
                                    break
                                case 'textcolor': 
                                    airspace_textcolor = airspace_style_values[1].trim()
                                    break
                                case 'fillcolor': 
                                    airspace_fillcolor = airspace_style_values[1].trim()
                                    break
                                case 'fillopacity': 
                                    airspace_fillopacity = airspace_style_values[1].trim()
                                    break
                            }
                        }
                        first_style = false
                    }
                    airspace_text = airspace_text.trim()
                    airspace_text = Tools.GetMapnikString(airspace_text)
                    airspaces_lines += """,{
                        "Style": "<PolygonSymbolizer fill-opacity='${airspace_fillopacity}' fill='${airspace_fillcolor}' />",
                        "SRS": "${inputSRS}",
                        "Type": "ogr",
                        "File": "${airspace_filename}",
                        "Layer": "${airspace_layer}"
                    }
                    ,{
                        "Style": "<LineSymbolizer stroke='${airspace_fillcolor}' stroke-width='${AIRSPACE_STROKE_WIDTH}' stroke-linecap='round' />",
                        "SRS": "${inputSRS}",
                        "Type": "ogr",
                        "File": "${airspace_filename}",
                        "Layer": "${airspace_layer}"
                    }
                    ,{
                        "Style": "<TextSymbolizer fontset-name='fontset-0' size='${airspace_textsize}' fill='${airspace_textcolor}' allow-overlap='false' placement='line' halo-radius='1' halo-fill='white' spacing='${airspace_textspacing}' placement-type='simple' placements='X,12,10,8,6,4,2'>'${airspace_text}'</TextSymbolizer>",
                        "SRS": "${inputSRS}",
                        "Type": "ogr",
                        "File": "${airspace_filename}",
                        "Layer": "${airspace_layer}"
                    }"""
                }
            }
        }
        return airspaces_lines
    }
    
    //--------------------------------------------------------------------------
    private BigDecimal get_value(String valueStr, String searchStr)
    {
        if (valueStr.contains(searchStr)) {
            String s = valueStr.substring(valueStr.indexOf(searchStr)+searchStr.size()).trim()
            int end_pos = s.indexOf(';')
            if (end_pos > 0) {
                s = s.substring(0, end_pos)
                if (s.isBigDecimal()) {
                    return s.toBigDecimal()
                }
            }
        }
        return 0
    }

    //--------------------------------------------------------------------------
    void BackgroundJob(String actionName, String jobFileName, String jobId, String jobIdFileName, String fileIdFileName, String pngFileName, boolean printLandscape, String printSize, String printProjection, String corridorWidth, boolean printColorChanges)
    {
        printstart "BackgroundJob ${actionName} ${jobId}"
        
        switch (actionName) {
            case Defs.OSMPRINTMAP_ACTION_CHECKJOB:
                boolean job_successfully = false
                int img_width = 0
                int img_height = 0
                BigDecimal lon_min = 0.0
                BigDecimal lon_max = 0.0
                BigDecimal lat_min = 0.0
                BigDecimal lat_max = 0.0
                BigDecimal pix_width = 0.0
                BigDecimal pix_height = 0.0

                //...........................................................................................
                printstart "Check job status"
                Map status = CallPrintServer("/mapstate/${jobId}", [PrintMapTools.HEADER_ACCEPT], "GET", PrintMapTools.DataType.JSON, "")
                if (status.responseCode == 200) {
                    if (status.json.Data.Attributes.MapBuildSuccessful) {
                        job_successfully = status.json.Data.Attributes.MapBuildSuccessful == "yes"
                        if (job_successfully) {
                            img_width = status.json.Data.Attributes.MapBuildBoxPixel.Width
                            img_height = status.json.Data.Attributes.MapBuildBoxPixel.Height
                            lon_min = status.json.Data.Attributes.MapBuildBoxWGS84.LonMin.toBigDecimal()
                            lon_max = status.json.Data.Attributes.MapBuildBoxWGS84.LonMax.toBigDecimal()
                            lat_min = status.json.Data.Attributes.MapBuildBoxWGS84.LatMin.toBigDecimal()
                            lat_max = status.json.Data.Attributes.MapBuildBoxWGS84.LatMax.toBigDecimal()
                            pix_width = (lon_max - lon_min) / img_height
                            pix_height = (lat_max - lat_min) / img_width
                            println "Successfully. Status='${status.json.Data.Attributes.MapBuildSuccessful}', Width=${img_width}, Height=${img_height}, LonDiff=${lon_max-lon_min}, LatDiff=${lat_max-lat_min}, PixWidth=${pix_width}, PixHeight=${pix_height}"
                        } else {
                            println "Not successfully. Status='${status.json.Data.Attributes.MapBuildSuccessful}"
                        }
                    } else {
                        printdone "Wait '${status.json.Data.Attributes.MapBuildStarted}'"
                        break
                    }
                } else {
                    // printdone "Wait responseCode=${status.responseCode}"
                    printstart "Stop OsmPrintMapJob"
                    quartzScheduler.unscheduleJobs(quartzScheduler.getTriggersOfJob(new JobKey("OsmPrintMapJob",Defs.OSMPRINTMAP_GROUP))*.key)
                    gpxService.DeleteFile(jobFileName)
                    printdone ""
                    
                    printdone ""
                    break
                }
                printdone ""
                
                //...........................................................................................
                if (job_successfully) {
                    printstart "Download map"
                    status = CallPrintServer("/mapfile/${jobId}", [PrintMapTools.HEADER_ACCEPT], "GET", PrintMapTools.DataType.BINARY, null)
                    if (status.responseCode == 200) {
                    
                        String download_zip_file_name = "${pngFileName}.zip"
                        String unpacked_png_file_name = "${pngFileName}.png"
                        String world_file_name = "${pngFileName}${Defs.MAP_PNG_WORLD_FILE_SUFFIX}"
                        String info_file_name = "${pngFileName}${Defs.MAP_PNG_INFO_FILE_SUFFIX}"
                        String tif_file_name = "${pngFileName.substring(0,pngFileName.lastIndexOf('.'))}.tif"
                        String warped_tif_file_name = "${pngFileName.substring(0,pngFileName.lastIndexOf('.'))}.warped.tif"
                        String warped_png_file_name = "${pngFileName}${Defs.MAP_PNG_WARP_FILE_SUFFIX}"
                        
                        // Write download_zip_file_name
                        printstart "Write ${download_zip_file_name}"
                        FileOutputStream zip_stream = new FileOutputStream(download_zip_file_name)
                        zip_stream << status.binary
                        zip_stream.flush()
                        zip_stream.close()
                        printdone ""
                        
                        // Write unpacked_png_file_name
                        def km_reader = null
                        def kmz_file = new java.util.zip.ZipFile(download_zip_file_name)
                        kmz_file.entries().findAll { !it.directory }.each {
                            if (!km_reader) {
                                km_reader = kmz_file.getInputStream(it)
                            }
                        }
                        if (km_reader) {
                            printstart "Write ${unpacked_png_file_name}"
                            FileOutputStream png_stream = new FileOutputStream(unpacked_png_file_name)
                            png_stream << km_reader
                            png_stream.flush()
                            png_stream.close()
                            printdone ""
                            km_reader.close()
                        }
                        kmz_file.close()
                        
                        // Generate world_file_name
                        printstart "Generate ${world_file_name}"
                        try {
                            File world_file = new File(world_file_name)
                            BufferedWriter world_file_writer = world_file.newWriter()
                            world_file_writer << "${(lon_max-lon_min)/img_width}\n" // 1
                            world_file_writer << "0\n" // 2
                            world_file_writer << "0\n" // 3
                            world_file_writer << "${(lat_min-lat_max)/img_height}\n" // 4
                            world_file_writer << "${lon_min}\n" // 5
                            world_file_writer << "${lat_max}" // 6
                            world_file_writer.close()
                        } catch (Exception e) {
                            println e.getMessage()
                        }
                        printdone ""
                        
                        // Generate info_file_name
                        printstart "Generate ${info_file_name}"
                        try {
                            File info_file = new File(info_file_name)
                            BufferedWriter info_file_writer = info_file.newWriter()
                            info_file_writer << "Top(Lat):    ${lat_max}\n"
                            info_file_writer << "Center(Lat): ${(lat_max-lat_min)/2+lat_min}\n"
                            info_file_writer << "Bottom(Lat): ${lat_min}\n"
                            info_file_writer << "Right(Lon):  ${lon_max}\n"
                            info_file_writer << "Center(Lon): ${(lon_max-lon_min)/2+lon_min}\n"
                            info_file_writer << "Left(Lon):   ${lon_min}\n"
                            info_file_writer << "Projection:  ${printProjection}\n"
                            info_file_writer << "Height:      ${img_height}px\n"
                            info_file_writer << "Width:       ${img_width}px\n"
                            info_file_writer << "Landscape:   ${printLandscape}\n"
                            info_file_writer << "Size:        ${printSize}\n"
                            if (corridorWidth != '0.0') {
                                info_file_writer << "Corridor:    ${corridorWidth}NM\n"
                            }
                            info_file_writer.close()
                        } catch (Exception e) {
                            println e.getMessage()
                        }
                        printdone ""
                        
                        // Delete download_zip_file_name
                        printstart "Delete ${download_zip_file_name}"
                        File download_zip_file = new File(download_zip_file_name)
                        if (download_zip_file.delete()) {
                            printdone ""
                        } else {
                            printerror ""
                        }
                        
                        // Generate pngFileName
                        printstart "Copy ${unpacked_png_file_name} -> ${pngFileName}"
                        def src_file = new File(unpacked_png_file_name).newInputStream()
                        try {
                            def dest_file = new File(pngFileName).newOutputStream()
                            dest_file << src_file
                            dest_file.close()
                            printdone ""
                        } catch (Exception e) {
                            printerror e.getMessage()
                        }
                        src_file.close()

                        /*
                        if (BootStrap.global.IsGDALAvailable()) {
                            gdal.AllRegister()
                            
                            // Generate GeoTIFF
                            printstart "Generate ${tif_file_name}"
                            try {
                                Vector options = new Vector()
                                options.add("-of")
                                options.add("GTiff")
                                options.add("-a_srs")
                                options.add("EPSG:3857")
                                options.add("-a_ullr") // upper left, lower right
                                options.add(lon_min.toString())
                                options.add(lat_max.toString())
                                options.add(lon_max.toString())
                                options.add(lat_min.toString())
                                options.add("-co")
                                options.add("COMPRESS=JPEG") // compress
                                options.add("-co")
                                options.add("TILED=YES") // creation of tiled TIFF files
                                println options.toString()
                                TranslateOptions translate_options = new TranslateOptions(options)
                                Dataset png_data = gdal.Open(pngFileName, gdalconstConstants.GA_ReadOnly)
                                if (png_data) {
                                    Dataset ds = gdal.Translate(tif_file_name, png_data, translate_options)
                                    if (ds) {
                                        ds.delete()
                                        printdone ""
                                    } else {
                                        printerror "No tif generated."
                                    }
                                    png_data.delete()
                                } else {
                                    printerror "No png loaded."
                                }
                                translate_options.delete()
                            } catch (Exception e) {
                                printerror e.getMessage()
                            }
                            
                            // Generate warped PNG
                            printstart "Warp to ${warped_png_file_name}"
                            try {
                                Vector options = new Vector()
                                options.add("-of")
                                options.add("PNG")
                                options.add("-s_srs")
                                options.add("EPSG:3857")
                                options.add("-t_srs")
                                options.add("EPSG:3857")
                                println options.toString()
                                WarpOptions warp_options = new WarpOptions(options)
                                Dataset tif_data = gdal.Open(tif_file_name, gdalconstConstants.GA_ReadOnly)
                                if (tif_data) {
                                    Dataset[] tif_data2 = [tif_data]
                                    Dataset ds = gdal.Warp(warped_png_file_name, tif_data2, warp_options);
                                    if (ds) {
                                        ds.delete()
                                        printdone ""
                                    } else {
                                        printerror "No png generated."
                                    }
                                    tif_data.delete()
                                } else {
                                    printerror "No tif loaded."
                                }
                                warp_options.delete()
                            } catch (Exception e) {
                                printerror e.getMessage()
                            }
                        }
                        */

                        // Delete unpacked_png_file_name
                        printstart "Delete ${unpacked_png_file_name}"
                        File unpacked_png_file = new File(unpacked_png_file_name)
                        if (unpacked_png_file.delete()) {
                            printdone ""
                        } else {
                            printerror ""
                        }
                    }
                    printdone "responseCode=${status.responseCode}, ${if(status.binary)'Data available.'else'No data.'}"
                }
                
                // Test graphic processing
                boolean test_on = false
                if (test_on && job_successfully) {
                    printdone ""
                    throw new Exception()
                }
                
                //...........................................................................................
                printstart "Delete ${jobIdFileName}"
                File printjobid_file = new File(jobIdFileName)
                if (printjobid_file.delete()) {
                    printdone ""
                } else {
                    printerror ""
                }

                //...........................................................................................
                printstart "View job"
                status = CallPrintServer("/metadata/${jobId}", [HEADER_CONTENTTYPE], "GET", PrintMapTools.DataType.JSON, null)
                printdone "responseCode=${status.responseCode}"
                
                //...........................................................................................
                printstart "Remove job"
                status = CallPrintServer("/${jobId}", [PrintMapTools.HEADER_ACCEPT], "DELETE", PrintMapTools.DataType.NONE, null)
                printdone "responseCode=${status.responseCode}"
            
                //...........................................................................................
                printstart "Stop OsmPrintMapJob"
                quartzScheduler.unscheduleJobs(quartzScheduler.getTriggersOfJob(new JobKey("OsmPrintMapJob",Defs.OSMPRINTMAP_GROUP))*.key)
                gpxService.DeleteFile(jobFileName)
                printdone ""
                
                //...........................................................................................
                if (!job_successfully) {
                    String printfileid_errorfilename = fileIdFileName + Defs.OSMPRINTMAP_ERR_EXTENSION
                    printstart "Renmae ${fileIdFileName} to ${printfileid_errorfilename}"
                    File printfileid_file = new File(fileIdFileName)
                    if (printfileid_file.renameTo(printfileid_errorfilename)) {
                        printdone ""
                    } else {
                        printerror ""
                    }
                }
                
                break
        }
        
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    private boolean create_graticule_csv(String graticuleFileName, BigDecimal centerLatitude, BigDecimal centerLongitude,
                                         int printScale, int printWidth, int printHeight, int minPrintHeight, boolean alternatePos
                                        )
    {
        BigDecimal center_graticule_latitude = get_rounded_decimal_grad(centerLatitude)
        BigDecimal center_graticule_longitude = get_rounded_decimal_grad(centerLongitude)

        printstart "Write ${graticuleFileName}"
        println "center_graticule_latitude=${center_graticule_latitude}, center_graticule_longitude=${center_graticule_longitude}"
        
        BigDecimal print_width_nm = printScale * printWidth / Defs.mmPerNM
        BigDecimal print_height_nm = printScale * printHeight / Defs.mmPerNM
        Map rect_width = AviationMath.getShowPoint(centerLatitude, centerLongitude, print_width_nm / 2, GRATICULE_SCALEBAR_LEN)
        Map rect_height = AviationMath.getShowPoint(centerLatitude, centerLongitude, print_height_nm / 2, GRATICULE_SCALEBAR_LEN*printHeight/minPrintHeight) 
        println "Width:  ${printWidth} mm, ${print_width_nm} NM, ${rect_width}"
        println "Height: ${printHeight} mm, ${print_height_nm} NM, ${rect_height}"
        
        File graticule_file = new File(graticuleFileName)
        Writer graticule_writer = graticule_file.newWriter("UTF-8",false)
        
        graticule_writer << "id${CSV_DELIMITER}name${CSV_DELIMITER}wkt"
        
        int line_id = 1
        CoordPresentation coord_presentation = CoordPresentation.DEGREEMINUTE
        
        // vertical line
        BigDecimal start_lon = center_graticule_longitude
        while (start_lon > rect_width.lonmin) {
            start_lon -= 10/60 // 10'
        }
        BigDecimal lon = start_lon
        BigDecimal mid_lon = start_lon + (rect_width.lonmax - start_lon) / 2
        while (lon < rect_width.lonmax) {
            if ((lon < mid_lon) && (lon + 10/60 > mid_lon)) {
                BigDecimal lon2 = lon
                while (lon2 < lon + 8.5/60) {
                    lon2 += 1/60 // 1'
                    // write lineal
                    String wkt = "LINESTRING(${lon2} ${rect_height.latmin2}, ${lon2} ${rect_height.latmax})"
                    String name = ""
                    graticule_writer << """${CSV_LINESEPARATOR}${line_id}${CSV_DELIMITER}"${name}"${CSV_DELIMITER}${wkt}"""
                    line_id++
                }
                lon2 += 1/60 // 1'
                String wkt = "POLYGON((${lon} ${rect_height.latmax}, ${lon2} ${rect_height.latmax}, ${lon2} ${rect_height.latmin2}, ${lon} ${rect_height.latmin2}, ${lon} ${rect_height.latmax}))"
                String name = ""
                graticule_writer << """${CSV_LINESEPARATOR}${line_id}${CSV_DELIMITER}"${name}"${CSV_DELIMITER}${wkt}"""
                line_id++
            }
            lon += 10/60 // 10'
            // write line
            String wkt = "LINESTRING(${lon} ${rect_height.latmin}, ${lon} ${rect_height.latmax})"
            String name = ""
            graticule_writer << """${CSV_LINESEPARATOR}${line_id}${CSV_DELIMITER}"${name}"${CSV_DELIMITER}${wkt}"""
            // write longitude value
            if (alternatePos) {
                wkt = "LINESTRING(${lon} ${rect_height.latmin2}, ${lon} ${rect_height.latmin2})"
            } else {
                wkt = "LINESTRING(${lon} ${rect_height.latmax}, ${lon} ${rect_height.latmax})"
            }
            name = coord_presentation.GetMapName(lon, false)
            graticule_writer << """${CSV_LINESEPARATOR}${line_id}${CSV_DELIMITER}"${name}"${CSV_DELIMITER}${wkt}"""
            line_id++
        }
        
        // horizontal line
        BigDecimal start_lat = center_graticule_latitude
        while (start_lat > rect_height.latmin) {
            start_lat -= 10/60 // 10'
        }
        BigDecimal lat = start_lat
        BigDecimal mid_lat = start_lat + (rect_height.latmax - start_lat) / 2
        while (lat < rect_height.latmax) {
            if ((lat < mid_lat) && (lat + 10/60 > mid_lat)) {
                BigDecimal lat2 = lat
                while (lat2 < lat + 8.5/60) {
                    lat2 += 1/60 // 1'
                    // write lineal
                    String wkt = "LINESTRING(${rect_width.lonmin} ${lat2}, ${rect_width.lonmax2} ${lat2})"
                    String name = ""
                    graticule_writer << """${CSV_LINESEPARATOR}${line_id}${CSV_DELIMITER}"${name}"${CSV_DELIMITER}${wkt}"""
                    line_id++
                }
                lat2 += 1/60 // 1'
                String wkt = "POLYGON((${rect_width.lonmin} ${lat}, ${rect_width.lonmax2} ${lat}, ${rect_width.lonmax2} ${lat2}, ${rect_width.lonmin} ${lat2}, ${rect_width.lonmin} ${lat}))"
                String name = ""
                graticule_writer << """${CSV_LINESEPARATOR}${line_id}${CSV_DELIMITER}"${name}"${CSV_DELIMITER}${wkt}"""
                line_id++
            }
            lat += 10/60 // 10'
            // write line
            String wkt = "LINESTRING(${rect_width.lonmin} ${lat}, ${rect_width.lonmax} ${lat})"
            String name = ""
            graticule_writer << """${CSV_LINESEPARATOR}${line_id}${CSV_DELIMITER}"${name}"${CSV_DELIMITER}${wkt}"""
            // write latitude value
            wkt = "LINESTRING(${rect_width.lonmin} ${lat}, ${rect_width.lonmin} ${lat})"
            name = coord_presentation.GetMapName(lat, true)
            graticule_writer << """${CSV_LINESEPARATOR}${line_id}${CSV_DELIMITER}"${name}"${CSV_DELIMITER}${wkt}"""
            line_id++
        }

        graticule_writer.close()
        
        printdone ""
        return true
    }
    
    //--------------------------------------------------------------------------
    private BigDecimal get_rounded_decimal_grad(BigDecimal decimalGrad)
    // Rundung auf ganze 10'
    {
        int grad_value = decimalGrad.toInteger()
        BigDecimal minute_value = 60 * (decimalGrad - grad_value)
        int minute_value2 = minute_value.toInteger()
        minute_value2 = (minute_value2.toBigDecimal() / 10).toInteger() * 10
        return minute_value2 / 60 + grad_value
    }
    
    //--------------------------------------------------------------------------
    private List create_mapobjects_csv(Route routeInstance, String webRootDir, String csvFileName, boolean isPrint, String symbolPraefix)
    {
        println "create_mapobjects_csv ${routeInstance.GetName(isPrint)} -> ${webRootDir + csvFileName}"
        
        List mapobjects_symbol_filenames = []
        int[] line_id = [] + 1
        
        File csv_file = new File(webRootDir + csvFileName)
        Writer csv_writer = csv_file.newWriter("UTF-8",false)
        csv_writer << "id${CSV_DELIMITER}symbol${CSV_DELIMITER}name${CSV_DELIMITER}vertical-alignment${CSV_DELIMITER}horizontal-alignment${CSV_DELIMITER}dy${CSV_DELIMITER}wkt"
        
        mapobjects_symbol_filenames += create_mapobjects_csv2(routeInstance, webRootDir, symbolPraefix, line_id, csv_writer)
        if (routeInstance.contestMapShowMapObjectsFromRouteID) {
            Route route_instance = Route.get(routeInstance.contestMapShowMapObjectsFromRouteID)
            if (route_instance) {
                mapobjects_symbol_filenames += create_mapobjects_csv2(route_instance, webRootDir, symbolPraefix, line_id, csv_writer)
            }
        }
        
        csv_writer.close()
        return mapobjects_symbol_filenames
    }

    //--------------------------------------------------------------------------
    private List create_mapobjects_csv2(Route routeInstance, String webRootDir, String symbolPraefix, int[] lineID, Writer csvWriter)
    {
        List mapobjects_symbol_filenames = []
        
        for (CoordMapObject coordmapobject_instance in CoordMapObject.findAllByRoute(routeInstance, [sort:"enrouteViewPos"])) {
            if (!coordmapobject_instance.mapObjectText.startsWith(Defs.IGNORE_LINE) && coordmapobject_instance.mapObjectType != MapObjectType.Airfield) {
                csvWriter << CSV_LINESEPARATOR + lineID[0]
                if (coordmapobject_instance.mapObjectType == MapObjectType.Symbol) {
                    String symbol_name = symbolPraefix + coordmapobject_instance.id + '.png'
                    csvWriter << CSV_DELIMITER + symbol_name
                    String symbol_filename = webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD + "/" + symbol_name
                    mapobjects_symbol_filenames += symbol_filename
                    File symbol_file = new File(symbol_filename)
                    FileOutputStream symbol_stream = new FileOutputStream(symbol_file)
                    if (coordmapobject_instance.imagecoord) {
                        symbol_stream << coordmapobject_instance.imagecoord.imageData
                    }
                    symbol_stream.close()
                } else {
                    csvWriter << CSV_DELIMITER + coordmapobject_instance.mapObjectType.imageShortName
                }
                csvWriter << CSV_DELIMITER + '"' + coordmapobject_instance.mapObjectText + '"'
                if (false) { // if (coordmapobject_instance.mapObjectType == MapObjectType.Subtitle) {
                    csvWriter << CSV_DELIMITER + "bottom"
                    csvWriter << CSV_DELIMITER + "right"
                    csvWriter << CSV_DELIMITER + 0
                } else {
                    csvWriter << CSV_DELIMITER + "auto"
                    csvWriter << CSV_DELIMITER + "auto"
                    csvWriter << CSV_DELIMITER + OBJECT_TEXT_LINE1_DY
                }
                csvWriter << CSV_DELIMITER + "POINT(${coordmapobject_instance.lonMath()} ${coordmapobject_instance.latMath()})"
                lineID[0]++
            }
        }
        return mapobjects_symbol_filenames
    }
    
    //--------------------------------------------------------------------------
    private Map CallPrintServer(String funcURL, List headerList, String requestMethod, PrintMapTools.DataType dataType, def outputData)
    {
        String url_path = PrintMapTools.GetPrintServerAPI() + funcURL       
        return PrintMapTools.CallPrintServer(url_path, headerList, requestMethod, dataType, outputData)        
    }

    //--------------------------------------------------------------------------
    private void FileUpload(String funcURL, String FilePath)
    {
        String url_path = PrintMapTools.GetPrintServerAPI() + funcURL
        if (LOG_RESTAPI_CALLS) {
            println url_path
        }
        
        HttpURLConnection conn = null
        DataOutputStream dos = null
        DataInputStream inStream = null
        String lineEnd = "\r\n"
        String twoHyphens = "--"
        String boundary =  "*****"
        int bytesRead, bytesAvailable, bufferSize
        byte[] buffer
        int maxBufferSize = 1*1024*1024
        try{
            //------------------ CLIENT REQUEST
            FileInputStream fileInputStream = new FileInputStream(FilePath)
            // open a URL connection to the Servlet
            URL url = new URL(url_path)
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection()
            // Allow Inputs
            conn.setDoInput(true)
            // Allow Outputs
            conn.setDoOutput(true)
            // Don't use a cached copy.
            conn.setUseCaches(false)
            // Use a post method.
            conn.setRequestMethod("POST")
            //conn.setRequestProperty("Accept", "application/vnd.api+json; charset=utf-8")
            conn.setRequestProperty("Connection", "keep-alive")
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary)
            
            dos = new DataOutputStream( conn.getOutputStream() )
            dos.writeBytes(twoHyphens + boundary + lineEnd)
            //dos.writeBytes("Content-Disposition: form-data; name=\"upload\";" + " filename=\"" + FilePath +"\"" + lineEnd)
            dos.writeBytes("Content-Disposition: form-data; name=\"file\";" + " filename=\"" + FilePath +"\"" + lineEnd)
            dos.writeBytes(lineEnd)
            
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available()
            println "${bytesAvailable} bytes available."
            bufferSize = Math.min(bytesAvailable, maxBufferSize)
            buffer = new byte[bufferSize]
            
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            while (bytesRead > 0) {
                 dos.write(buffer, 0, bufferSize)
                 println "${bytesRead} bytes written."
                 bytesAvailable = fileInputStream.available()
                 bufferSize = Math.min(bytesAvailable, maxBufferSize)
                 bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                 println "${bytesRead} bytes available."
             }
            
             // send multipart form data necesssary after file data...
             dos.writeBytes(lineEnd)
             dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
             
             // close streams
             fileInputStream.close()
             dos.flush()
             dos.close()
             
             println "${conn.responseCode} ${FilePath} uploaded to ${url_path}"
        } catch (MalformedURLException ex) {
            println "From ServletCom CLIENT REQUEST:" + ex
        } catch (IOException ioe) {
            println "From ServletCom CLIENT REQUEST:" + ioe
        }
        //------------------ read the SERVER RESPONSE
        try {
            inStream = new DataInputStream ( conn.getInputStream() )
            String str
            while (( str = inStream.readLine()) != null){
                println("Server response is: " + str)
            }
            inStream.close()
        }catch (IOException ioex){
            println("From (ServerResponse): " + ioex)
        }
    }

    //--------------------------------------------------------------------------
    private Map CallPrintServer_Upload(String funcURL, List headerList, String requestMethod, String fileName)
    {
        Map ret = [responseCode:null, json:null, binary:null]
        
        String url_path = PrintMapTools.GetPrintServerAPI() + funcURL
        if (LOG_RESTAPI_CALLS) {
            println url_path
        }
        def connection = url_path.toURL().openConnection()
        
        connection.requestMethod = requestMethod
        
        if (headerList) {
            headerList.each {
                connection.setRequestProperty( it.name, it.value )
            }
        }
        
        connection.doOutput = true
        connection.useCaches = false
        
        return ret
    }

    //--------------------------------------------------------------------------
    private void savePNGImage(File output, BufferedImage bufferedImage, boolean isLandscape) throws IOException 
    {
        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName("png"); iw.hasNext();) {
           ImageWriter writer = iw.next()
           
           ImageWriteParam write_param = writer.getDefaultWriteParam()
           ImageTypeSpecifier type_specifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB)
           IIOMetadata metadata = writer.getDefaultImageMetadata(type_specifier, write_param)
           if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
               continue
           }
    
           setDPI(metadata, isLandscape)
    
           ImageOutputStream stream = ImageIO.createImageOutputStream(output)
           try {
              writer.setOutput(stream)
              writer.write(metadata, new IIOImage(bufferedImage, null, metadata), write_param)
              // println "YY1 ${metadata.getStandardTree()}"
           } finally {
              stream.close()
           }
           
           break
        }
    }

    //--------------------------------------------------------------------------
    private void setDPI(IIOMetadata metadata, boolean isLandscape) throws IIOInvalidTreeException
    {
        double INCH_2_CM = 2.54
        double dots_per_mm = 1.0 * DPI / 10 / INCH_2_CM
        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0")
        IIOMetadataNode orientation = new IIOMetadataNode("ImageOrientation")
        if (isLandscape) {
            orientation.setAttribute("value", "Rotate90")
        } else {
            orientation.setAttribute("value", "Normal")
        }
        IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize")
        horiz.setAttribute("value", dots_per_mm.toString())
        IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize")
        vert.setAttribute("value", dots_per_mm.toString())
        IIOMetadataNode dim = new IIOMetadataNode("Dimension")
        dim.appendChild(orientation)
        dim.appendChild(horiz)
        dim.appendChild(vert)
        root.appendChild(dim)
        metadata.mergeTree("javax_imageio_1.0", root)
    }
    
    //--------------------------------------------------------------------------
    void InitLocalPrintmaps(Route routeInstance)
    {
        printstart "InitLocalPrintmaps"
        
        boolean get_osm_links = true
        boolean get_contour_data = true
        boolean get_osm_data = true
        boolean call_docker = true
        
        Map center_route = routeInstance.GetCenter([contestMapCenterPoints:Defs.CONTESTMAPPOINTS_AIRFIELDS])
        BigDecimal center_latitude = center_route.centerLatitude
        BigDecimal center_longitude = center_route.centerLongitude
        int print_width = 0 // mm
        int print_height = 0 // mm
        String print_size = Defs.CONTESTMAPPRINTSIZE_AIRPORTAREA
        if (routeInstance.contest.anrFlying) {
            print_size = Defs.CONTESTMAPPRINTSIZE_ANRAIRPORTAREA
        }
        switch (print_size) {
            case Defs.CONTESTMAPPRINTSIZE_AIRPORTAREA:
                print_width = 2*AIRPORTAREA_A3_DISTANCE
                print_height = 2*AIRPORTAREA_A3_DISTANCE
                break
            case Defs.CONTESTMAPPRINTSIZE_ANRAIRPORTAREA:
                print_width = 2*AIRPORTAREA_A4_DISTANCE
                print_height = 2*AIRPORTAREA_A4_DISTANCE
                break
        }
        //print_width -= 2*MARGIN
        //print_height -= 2*MARGIN
        BigDecimal print_width_nm = routeInstance.mapScale * print_width / Defs.mmPerNM
        BigDecimal print_height_nm = routeInstance.mapScale * print_height / Defs.mmPerNM
        Map rect_width = AviationMath.getShowPoint(center_latitude, center_longitude, print_width_nm / 2 + AIRPORTAREA_ADDITIONAL_LEN, GRATICULE_SCALEBAR_LEN)
        Map rect_height = AviationMath.getShowPoint(center_latitude, center_longitude, print_height_nm / 2 + AIRPORTAREA_ADDITIONAL_LEN, GRATICULE_SCALEBAR_LEN)
        
        println "center_latitude=${center_latitude}, center_longitude=${center_longitude}"
        println "Width:  ${print_width} mm, ${print_width_nm} NM, ${rect_width}"
        println "Height: ${print_height} mm, ${print_height_nm} NM, ${rect_height}"

        String pbf_links = ""
        String pbf_time = Defs.PBFTIME_UNKNOWN
        String pbf_date = Defs.PBFTIME_UNKNOWN
        if (get_osm_links) {
            String pbf_firsttime = Defs.PBFTIME_UNKNOWN
            String pbf_lasttime = Defs.PBFTIME_UNKNOWN
            for (Map pbf_data in get_pbf_list(rect_width, rect_height)) {
                if (pbf_links) {
                    pbf_links += " "
                }
                pbf_links += pbf_data.pbflink
                if (pbf_data.pbftime != Defs.PBFTIME_UNKNOWN) {
                    if (pbf_firsttime == Defs.PBFTIME_UNKNOWN || pbf_data.pbftime < pbf_firsttime) {
                        pbf_firsttime = pbf_data.pbftime
                    }
                    if (pbf_lasttime == Defs.PBFTIME_UNKNOWN || pbf_data.pbftime > pbf_lasttime) {
                        pbf_lasttime = pbf_data.pbftime
                    }
                }
            }
            if (pbf_firsttime != Defs.PBFTIME_UNKNOWN && pbf_lasttime != Defs.PBFTIME_UNKNOWN) {
                if (pbf_firsttime == pbf_lasttime) {
                    pbf_time = pbf_firsttime
                    pbf_date = pbf_time.substring(0,10)
                } else {
                    pbf_time = "${pbf_firsttime}..${pbf_lasttime}"
                    pbf_date = "${pbf_firsttime.substring(0,10)}..${pbf_lasttime.substring(0,10)}"
                }
            }
            println "Links: ${pbf_links}"
            println "Time: ${pbf_time}, Date: ${pbf_date}"
        }

        String docker_call = "docker rm initprintmaps"
        println "Excecute $docker_call"
        if (call_docker) {
            docker_call.execute().waitFor()
        }
                
        docker_call = "docker"
        docker_call += " run --detach --name initprintmaps"
        docker_call += " --env dbid=${routeInstance.contest.contestUUID}"
        docker_call += " --env PGHOST=${PrintMapTools.GetSQLHost()}"
        docker_call += " --env PGPORT=${PrintMapTools.GetSQLPort()}"
        docker_call += " --env PGUSER=${PrintMapTools.GetSQLUserName()}" 
        docker_call += " --env PGPASSWORD=${PrintMapTools.GetSQLPassword()}" 
        docker_call += " --env MINLON=${rect_width.lonmin}"
        docker_call += " --env MINLAT=${rect_height.latmin}"
        docker_call += " --env MAXLON=${rect_width.lonmax}"
        docker_call += " --env MAXLAT=${rect_height.latmax}"
        docker_call += " --env PBFTIME=${pbf_time}"
        docker_call += " --env PBFDATE=${pbf_date} "
        if (get_contour_data) {
            docker_call += " --env CONTOURSOURCES=${PrintMapTools.GetContourSources()}"
            docker_call += " --env SRTMUSER=${BootStrap.global.GetSRTMUsername()}" // neccessary for contour db
            docker_call += " --env SRTMPASSWORD=${BootStrap.global.GetSRTMPassword()}"
        }
        if (get_osm_data) {
            docker_call += """ --env PBFLINKS="${pbf_links}" """ // neccessary for osm db
        }
        docker_call += " createdb:latest"
        println "Excecute ${remove_password(docker_call, ['--env PGPASSWORD=', '--env SRTMPASSWORD='])}"
        if (call_docker) {
            docker_call.execute().waitFor()
        }
        
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    void StartLocalPrintmaps(Contest contestInstance)
    {
        printstart "StartLocalPrintmaps"

        String docker_call = "docker rm runprintmaps"
        println "Excecute $docker_call"
        docker_call.execute().waitFor()
            
        docker_call = "docker"
        docker_call += " run --detach --name runprintmaps"
        docker_call += " --env dbid=${contestInstance.contestUUID}"
        docker_call += " --env PGHOST=${PrintMapTools.GetSQLHost()}"
        docker_call += " --env PGPORT=${PrintMapTools.GetSQLPort()}"
        docker_call += " --env PGUSER=${PrintMapTools.GetSQLUserName()}" 
        docker_call += " --env PGPASSWORD=${PrintMapTools.GetSQLPassword()}" 
        docker_call += " -p 127.0.0.1:8181:8181"
        docker_call += " printmaps:latest"
        println "Excecute ${remove_password(docker_call, ['--env PGPASSWORD='])}"
        docker_call.execute().waitFor()
            
        sleep(5000)
            
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    void StopLocalPrintmaps(Contest contestInstance)
    {
        printstart "StopLocalPrintmaps"

        String docker_call = "docker stop runprintmaps"
        println "Excecute $docker_call"
        docker_call.execute().waitFor()
            
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    private String remove_password(String logStr, List passwordPraefixes)
    {
        for (String password_praefix in passwordPraefixes) {
            if (logStr.contains(password_praefix)) {
                int start_pos = logStr.indexOf(password_praefix)
                int end_pos = logStr.indexOf(" ", start_pos + password_praefix.size())
                logStr = logStr.substring(0, start_pos) + logStr.substring(end_pos + 1)
            }
        }
        return logStr
    }
    
    //--------------------------------------------------------------------------
    List get_pbf_list(Map rectWidth, Map rectHeight)
    {
        println "get_pbf_list ${rectWidth.lonmin} ${rectHeight.latmin} ${rectWidth.lonmax} ${rectHeight.latmax}"
        
        List pbf_list = []
        InputStream inputstream_instance = new URL(Defs.GEOFABRIK_INDEX).openStream()
        BufferedReader input_reader = inputstream_instance.newReader("UTF-8")
        def input_data = new JsonSlurper().parse(input_reader)
        if (input_data) {
            List parent_list = []
            for (def feature in input_data.features) {
                parent_list += feature.properties.parent
            }
            for (def feature in input_data.features) {
                if (!(feature.properties.id in parent_list || feature.properties.id in Defs.GEOFABRIK_IGNORE_FEATURES)) {
                    boolean point_inside = false
                    boolean points_around = false
                    for (def coordinate_area in feature.geometry.coordinates) {
                        boolean not_found = false
                        boolean found_topleft = false
                        boolean found_top = false
                        boolean found_topright = false
                        boolean found_left = false
                        boolean found_right = false
                        boolean found_bottomleft = false
                        boolean found_bottom = false
                        boolean found_bottomright = false
                        for (def coordinates2 in coordinate_area) {
                            for (def coordinate in coordinates2) {
                                BigDecimal lon = coordinate[0].toBigDecimal()
                                BigDecimal lat = coordinate[1].toBigDecimal()
                                if ( lon > rectWidth.lonmin && lon < rectWidth.lonmax && lat < rectHeight.latmax && lat > rectHeight.latmin) { // Point inside
                                    point_inside = true
                                    break
                                } else if (lon < rectWidth.lonmin && lat > rectHeight.latmax) { // oben links
                                    found_topleft = true
                                } else if (lon > rectWidth.lonmin && lon < rectWidth.lonmax && lat > rectHeight.latmax) { // oben Mitte
                                    found_top = true
                                } else if (lon > rectWidth.lonmax && lat > rectHeight.latmax) { // oben rechts
                                    found_topright = true
                                } else if (lon < rectWidth.lonmin && lat < rectHeight.latmax && lat > rectHeight.latmin) { // links
                                    found_left = true
                                } else if (lon > rectWidth.lonmax && lat < rectHeight.latmax && lat > rectHeight.latmin) { // rechts
                                    found_right = true
                                } else if (lon < rectWidth.lonmin && lat < rectHeight.latmin) { // unten links
                                    found_bottomleft = true
                                } else if (lon > rectWidth.lonmin && lon < rectWidth.lonmax && lat < rectHeight.latmin) { // unten Mitte
                                    found_bottom = true
                                } else if (lon > rectWidth.lonmax && lat < rectHeight.latmin) { // unten rechts
                                    found_bottomright = true
                                } else {
                                    not_found = true
                                    break
                                }
                            }
                            if (point_inside || not_found) {
                                break
                            }
                            if (found_topleft && found_top && found_topright && found_left && found_right && found_bottomleft && found_bottom && found_bottomright) {
                                points_around = true
                                break
                            }
                        }
                        if (point_inside || points_around) {
                            break
                        }
                    }
                    if (point_inside || points_around) {
                        String pbf_time = Defs.PBFTIME_UNKNOWN
                        String state_file_url = feature.properties.urls.updates + "/" + Defs.GEOFABRIK_OSMSTATE_FILENAME
                        try {
                            InputStream state_inputstream_instance = new URL(state_file_url).openStream()
                            BufferedReader state_input_reader = state_inputstream_instance.newReader("UTF-8")
                            while (true) {
                                String line = state_input_reader.readLine()
                                if (line == null) {
                                    break
                                }
                                if (line.startsWith("timestamp=")) {
                                    pbf_time = line.substring(10).replace('\\:',':')
                                    break
                                }
                            }
                            state_input_reader.close()
                            state_inputstream_instance.close()
                        } catch (Exception e) {
                        }
                        
                        println "Inside=${point_inside} Around=${points_around} ${feature.properties.urls.pbf} Time=${pbf_time}"
                        pbf_list += [pbflink:feature.properties.urls.pbf, pbftime:pbf_time]
                    }
                }
            }
        }
        input_reader.close()
        inputstream_instance.close()
        return pbf_list
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code, List args, boolean isPrint)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        String lang = session_obj.showLanguage
        if (isPrint) {
            lang = session_obj.printLanguage
        }
        if (args) {
            return messageSource.getMessage(code, args.toArray(), new Locale(lang))
        } else {
            return messageSource.getMessage(code, null, new Locale(lang))
        }
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code, boolean isPrint)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        String lang = session_obj.showLanguage
        if (isPrint) {
            lang = session_obj.printLanguage
        }
        return messageSource.getMessage(code, null, new Locale(lang))
    }
    
    //--------------------------------------------------------------------------
    void printstart(out)
    {
        logService.printstart out
    }

    //--------------------------------------------------------------------------
    void printerror(out)
    {
        if (out) {
            logService.printend "Error: $out"
        } else {
            logService.printend "Error."
        }
    }

    //--------------------------------------------------------------------------
    void printdone(out)
    {
        if (out) {
            logService.printend "Done: $out"
        } else {
            logService.printend "Done."
        }
    }

    //--------------------------------------------------------------------------
    void print(out)
    {
        logService.print out
    }

    //--------------------------------------------------------------------------
    void println(out)
    {
        logService.println out
    }
}
