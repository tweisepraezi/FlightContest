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
    final static Map HEADER_ACCEPT = [name:"Accept", value:"application/vnd.api+json; charset=utf-8"]
    
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
    final static String ATTR_INPUT_SRS = "epsg:4326"
     
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
    final static int AIRPORTAREA_DISTANCE = 420 // mm
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
    
    final static int SCALEBAR_YPOS_TOP = 6
    final static String SCALEBAR_TITLE = "5 NM"
    final static int SCALEBAR_TITLE_FONT_SIZE = 8
    
    final static int TP_FONT_SIZE = 16
    
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
    
    // CSV files
    final static String CSV_DELIMITER = "|"
    final static String CSV_LINESEPARATOR = "\r\n"
    
    // Development options
    private final static boolean LOG_RESTAPI_CALLS = true        // true
    private final static boolean LOG_RESTAPI_OUTPUTDATA = false  // false
    private final static boolean LOG_RESTAPI_RETURNS = true      // true
    private final static boolean LOG_RESTAPI_EXCEPTIONS = true   // true
        
    //--------------------------------------------------------------------------
    private enum DataType {
        NONE,
        JSON,
        BINARY
    }
    
    //--------------------------------------------------------------------------
    Map PrintOSM(Map contestMapParams)
    {
        printstart "PrintOSM"
        
        Map ret = [ok:false, message:'']
        
        Map add_params = [centerLatitude: 0.0, centerLongitude: 0.0, centerGraticuleLatitude: 0.0, centerGraticuleLongitude: 0.0]
        File gpx_file = new File(contestMapParams.gpxFileName)
        FileReader gpx_reader = new FileReader(gpx_file)
        try {
            def gpx = new XmlParser().parse(gpx_reader)
            def m = gpx.extensions.flightcontest.contestmap
            add_params.centerLatitude = m.'@center_latitude'[0].toBigDecimal()
            add_params.centerLongitude = m.'@center_longitude'[0].toBigDecimal()
            add_params.centerGraticuleLatitude = m.'@center_graticule_latitude'[0].toBigDecimal()
            add_params.centerGraticuleLongitude = m.'@center_graticule_longitude'[0].toBigDecimal()
            ret.ok = true
        } catch (Exception e) {
            println e.getMessage()
        }
        gpx_reader.close()
        contestMapParams += add_params
        println "Params: ${contestMapParams}"
        
        if (ret.ok) {
            if (!contestMapParams.contestMapGraticule) {
                contestMapParams.graticuleFileName = ""
            }
        }
        if (ret.ok) {
            ret = print_osm(contestMapParams)
        }
        
        printdone ""
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map print_osm(Map contestMapParams)
    {
        Map ret = [ok:false, message:'']
        
        String map_projection = ATTR_OUTPUT_PROJECTION_PRINTPDF
        if (contestMapParams.taskCreator) {
            map_projection = ATTR_OUTPUT_PROJECTION_TASKCREATOR
        }
        
        String printjob_filename = contestMapParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB
        String printjobid_filename = contestMapParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOBID + contestMapParams.routeId + ".txt"
        String printfileid_filename = contestMapParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTFILEID + contestMapParams.routeId + ".txt"
        
        /*
        if (new File(printjob_filename).exists()) {
            ret.message = getMsg('fc.contestmap.previousjobrunningerror', false)
            return ret
        }
        */
        
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
        
        int map_scale = contestMapParams.mapScale
        int print_scale = map_scale
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
                paper_size = "T/O"
                print_width = 2*AIRPORTAREA_DISTANCE
                print_height = 2*AIRPORTAREA_DISTANCE
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
        
        if (contestMapParams.contestMapCenterHorizontalPos != HorizontalPos.Center || contestMapParams.contestMapCenterVerticalPos != VerticalPos.Center) {
            BigDecimal print_width_nm = print_scale * print_width / Route.mmPerNM
            BigDecimal print_height_nm = print_scale * print_height / Route.mmPerNM
            Map rect_width = AviationMath.getShowPoint(contestMapParams.centerLatitude, contestMapParams.centerLongitude, print_width_nm / 2 - GpxService.CONTESTMAP_RUNWAY_FRAME_DISTANCE, GRATICULE_SCALEBAR_LEN)
            Map rect_height = AviationMath.getShowPoint(contestMapParams.centerLatitude, contestMapParams.centerLongitude, print_height_nm / 2 - GpxService.CONTESTMAP_RUNWAY_FRAME_DISTANCE, GRATICULE_SCALEBAR_LEN)
            switch (contestMapParams.contestMapCenterHorizontalPos) {
                case HorizontalPos.Left:
                    contestMapParams.centerLongitude = rect_width.lonmax
                    break
                case HorizontalPos.Right:
                    contestMapParams.centerLongitude = rect_width.lonmin
                    break
            }
            switch (contestMapParams.contestMapCenterVerticalPos) {
                case VerticalPos.Top:
                    contestMapParams.centerLatitude = rect_height.latmin
                    break
                case VerticalPos.Bottom:
                    contestMapParams.centerLatitude = rect_height.latmax
                    break
            }
        }
        
        BigDecimal latlon_relation = 1
        if (contestMapParams.taskCreator) {
            latlon_relation = AviationMath.getLatLonDistanceRelation(contestMapParams.centerLatitude)
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
        String map_region = ""
        String mapdata_date = ""
        printstart "Get service capabilities"
        Map status = CallPrintServer("/capabilities/service", [HEADER_ACCEPT], "GET", DataType.JSON, "")
        if (status.responseCode == 200) {
            for (def config_style in status.json.ConfigStyles) {
                if (config_style.Name.startsWith('region-')) {
                    if (!contestMapParams.contestMapDevStyle || config_style.Name.endsWith('-dev')) {
                        BigDecimal lat_bottom = get_value(config_style.LongDescription, 'Bottom(Lat):')
                        BigDecimal lat_top = get_value(config_style.LongDescription, 'Top(Lat):')
                        BigDecimal lon_left = get_value(config_style.LongDescription, 'Left(Lon):')
                        BigDecimal lon_right = get_value(config_style.LongDescription, 'Right(Lon):')
                        if (contestMapParams.centerLatitude >= lat_bottom && contestMapParams.centerLatitude <= lat_top && contestMapParams.centerLongitude >= lon_left && contestMapParams.centerLongitude <= lon_right) {
                            map_region = config_style.Name
                            mapdata_date = config_style.Date
                            break
                        }
                    }
                }
            }
            if (map_region) {
                printdone "${map_region} ${mapdata_date}"
            } else {
                printerror ""
                ret.message = getMsg('fc.contestmap.noregionerror', [contestMapParams.centerLatitude, contestMapParams.centerLongitude], false)
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
        if (BootStrap.global.IsOpenAIP() && ((contestMapParams.contestMapAirspaces && contestMapParams.contestMapAirspacesLayer2) || openaip_airfields)) {
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
            "SRS": "+init=${ATTR_INPUT_SRS}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "routes"
        },
        {
            "Style": "<LineSymbolizer stroke='red' stroke-width='${TRACK_STROKE_WIDTH}' stroke-linecap='round' />",
            "SRS": "+init=${ATTR_INPUT_SRS}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "tracks"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-2' size='${TP_FONT_SIZE}' fill='black' halo-radius='1' halo-fill='white' allow-overlap='true'>[name]</TextSymbolizer>",
            "SRS": "+init=${ATTR_INPUT_SRS}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "waypoints"
        },
        {
            "Style": "<MarkersSymbolizer file='[sym]' transform='${GEODATA_SYMBOL_SCALE}' placement='point' allow-overlap='true'/>",
            "SRS": "+init=${ATTR_INPUT_SRS}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "waypoints"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-2' size='${SYMBOL_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dx='10' placement='point'>[type]</TextSymbolizer>",
            "SRS": "+init=${ATTR_INPUT_SRS}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "waypoints"
        }"""
        
        String graticule_lines = ""
        if (!contestMapParams.taskCreator && graticule_file_name) {
            if (create_graticule_csv(graticule_file_name, contestMapParams.centerGraticuleLatitude, contestMapParams.centerGraticuleLongitude, contestMapParams.centerLatitude, contestMapParams.centerLongitude, print_scale, print_width, print_height, min_print_height, alternate_pos)) {
                String graticule_short_file_name = graticule_file_name.substring(graticule_file_name.lastIndexOf('/')+1)
                graticule_lines = """,{
                    "Style": "<PolygonSymbolizer fill='lightgrey' />",
                    "SRS": "+init=${ATTR_INPUT_SRS}",
                    "Type": "csv",
                    "File": "${graticule_short_file_name}",
                    "Layer": ""
                },
                {
                    "Style": "<LineSymbolizer stroke='black' stroke-width='${GRATICULE_STROKE_WIDTH}' stroke-linecap='round' />",
                    "SRS": "+init=${ATTR_INPUT_SRS}",
                    "Type": "csv",
                    "File": "${graticule_short_file_name}",
                    "Layer": ""
                },
                {
                    "Style": "<TextSymbolizer fontset-name='fontset-0' size='${GRATICULE_TEXT_FONT_SIZE}' fill='black' halo-radius='1' halo-fill='white' allow-overlap='true' horizontal-alignment='right' dx='5' dy='8' placement='vertex'>[name]</TextSymbolizer>",
                    "SRS": "+init=${ATTR_INPUT_SRS}",
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
            Route route_instance = Route.get(contestMapParams.routeId)
            String file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/MAPOBJECTS-${uuid}-UPLOAD.csv"
            String symbol_praefix = "SYMBOL-${uuid}-"
            mapobjects_symbol_filenames = create_mapobjects_csv(route_instance, contestMapParams.webRootDir, file_name, false, symbol_praefix)
            mapobjects_file_name = contestMapParams.webRootDir + file_name
            String mapobjects_short_file_name = mapobjects_file_name.substring(mapobjects_file_name.lastIndexOf('/')+1) // transform='scale(0.5, 0.5)'
            mapobjects_lines = """,{
                "Style": "<MarkersSymbolizer file='[symbol]' transform='${GEODATA_SYMBOL_SCALE}' allow-overlap='true' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${mapobjects_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${OBJECT_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' horizontal-alignment='[horizontal-alignment]' vertical-alignment='[vertical-alignment]' dy='[dy]' placement='point'>[name]</TextSymbolizer>",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${mapobjects_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String airspaces_lines = ""
        String airspaces_file_name = "" // will be uploaded
        if (contestMapParams.contestMapAirspaces && contestMapParams.contestMapAirspacesLayer2) {
			if (BootStrap.global.IsOpenAIP()) {
				String uuid = UUID.randomUUID().toString()
				Route route_instance = Route.get(contestMapParams.routeId)
				String file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/AIRSPACES-${uuid}-UPLOAD.kmz"
				Map ret2 = openAIPService.WriteAirspaces2KMZ(route_instance, contestMapParams.webRootDir, file_name, false, false)
				if (!ret2.ok) {
                    ret.message = getMsg('fc.contestmap.contestmapairspaces.kmzexport.missedairspaces', [ret2.missingAirspaces], false)
                    return ret
				}
				airspaces_file_name = contestMapParams.webRootDir + file_name
			} else {
				airspaces_file_name = Defs.FCSAVE_FILE_GEODATA_AIRSPACES
			}
            String airspaces_short_file_name = airspaces_file_name.substring(airspaces_file_name.lastIndexOf('/')+1)
            for (String layer in contestMapParams.contestMapAirspacesLayer2.split("\n")) {
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
                        airspaces_lines += """,{
                            "Style": "<PolygonSymbolizer fill-opacity='${airspace_fillopacity}' fill='${airspace_fillcolor}' />",
                            "SRS": "+init=${ATTR_INPUT_SRS}",
                            "Type": "ogr",
                            "File": "${airspace_filename}",
                            "Layer": "${airspace_layer}"
                        }
                        ,{
                            "Style": "<LineSymbolizer stroke='${airspace_fillcolor}' stroke-width='${AIRSPACE_STROKE_WIDTH}' stroke-linecap='round' />",
                            "SRS": "+init=${ATTR_INPUT_SRS}",
                            "Type": "ogr",
                            "File": "${airspace_filename}",
                            "Layer": "${airspace_layer}"
                        }
                        ,{
                            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${airspace_textsize}' fill='${airspace_textcolor}' allow-overlap='false' placement='line' halo-radius='1' halo-fill='white' spacing='${airspace_textspacing}' placement-type='simple' placements='X,12,10,8,6,4,2'>'${airspace_text}'</TextSymbolizer>",
                            "SRS": "+init=${ATTR_INPUT_SRS}",
                            "Type": "ogr",
                            "File": "${airspace_filename}",
                            "Layer": "${airspace_layer}"
                        }"""
                    }
                }
            }
        }
        
        String airports_lines = ""
        String airports_file_name = ""
        String uuid = UUID.randomUUID().toString()
        Route route_instance = Route.get(contestMapParams.routeId)
        String file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/AIRPORTS-${uuid}-UPLOAD.csv"
        openAIPService.WriteAirports2CSV(route_instance, contestMapParams.webRootDir, file_name, false, false, ",${CoordType.TO.title},${CoordType.LDG.title},${CoordType.iTO.title},${CoordType.iLDG.title},", openaip_airfields, contestMapParams.contestMapAdditionals)
        airports_file_name = contestMapParams.webRootDir + file_name
        String airports_short_file_name = airports_file_name.substring(airports_file_name.lastIndexOf('/')+1) // transform='scale(0.5, 0.5)'
        if (contestMapParams.taskCreator) {
            airports_lines = """,{
                "Style": "<MarkersSymbolizer file='[${OpenAIPService.CSV_AIRPORT_RUNWAY}]' transform='scale(0.6, ${latlon_relation*0.6}),rotate([${OpenAIPService.CSV_AIRPORT_HEADING}],0,0)' allow-overlap='true' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${airports_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<MarkersSymbolizer file='[${OpenAIPService.CSV_AIRPORT_TYPE}]' transform='scale(0.6, ${latlon_relation*0.6})' allow-overlap='true' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${airports_short_file_name}",
                "Layer": ""
            }"""
        } else {
            airports_lines = """,{
                "Style": "<MarkersSymbolizer file='[${OpenAIPService.CSV_AIRPORT_RUNWAY}]' transform='rotate([${OpenAIPService.CSV_AIRPORT_HEADING}],0,0),scale(0.6, 0.6)' allow-overlap='true' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${airports_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<MarkersSymbolizer file='[${OpenAIPService.CSV_AIRPORT_TYPE}]' transform='scale(0.6, 0.6)' allow-overlap='true' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${airports_short_file_name}",
                "Layer": ""
            }"""
        }
        airports_lines += """,{
            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${OBJECT_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dy='${OBJECT_TEXT_LINE1_DY}' placement='point'>[${OpenAIPService.CSV_AIRPORT_NAME}]</TextSymbolizer>",
            "SRS": "+init=${ATTR_INPUT_SRS}",
            "Type": "csv",
            "File": "${airports_short_file_name}",
            "Layer": ""
        }
        ,{
            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${OBJECT_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dy='${OBJECT_TEXT_LINE2_DY}' placement='point'>[${OpenAIPService.CSV_AIRPORT_ICAO}]</TextSymbolizer>",
            "SRS": "+init=${ATTR_INPUT_SRS}",
            "Type": "csv",
            "File": "${airports_short_file_name}",
            "Layer": ""
        }"""
        
        printstart "print_osm Scale=1:${print_scale} Width=${print_width} Height=${print_height}"
        println "print_width: $print_width mm"
        println "print_height: $print_height mm"
    
        String printjob_id = ""
        boolean job_started = false
        boolean job_successfully = false
        int img_width = 0
        int img_height = 0
        
        //...........................................................................................
        if (map_region) {
            printstart "Create job"
            status = CallPrintServer("/metadata", [HEADER_CONTENTTYPE,HEADER_ACCEPT], "POST", DataType.JSON,
            """{
                "Data": {
                    "Type": "maps",
                    "ID": "",
                    "Attributes": {
                        "Fileformat": "png",
                        "Scale": ${print_scale},
                        "PrintWidth": ${print_width},
                        "PrintHeight": ${print_height},
                        "Latitude": ${contestMapParams.centerLatitude},
                        "Longitude": ${contestMapParams.centerLongitude},
                        "Style": "${map_region}",
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
				gpxService.DeleteFile(mapobjects_file_name)
            }
            if (mapobjects_symbol_filenames) {
                for (String symbol_filename in mapobjects_symbol_filenames) {
                    printstart "Upload symbols"
                    FileUpload("/upload/${printjob_id}", symbol_filename)
                    printdone ""
                    gpxService.DeleteFile(symbol_filename)
                }
            }
            if (airspaces_file_name) {
                printstart "Upload airspaces"
                FileUpload("/upload/${printjob_id}", airspaces_file_name)
                printdone ""
				if (BootStrap.global.IsOpenAIP()) {
					gpxService.DeleteFile(airspaces_file_name)
				}
            }
            if (airports_file_name) {
                printstart "Upload airports"
                FileUpload("/upload/${printjob_id}", airports_file_name)
                printdone ""
				if (BootStrap.global.IsOpenAIP()) {
					gpxService.DeleteFile(airports_file_name)
				}
            }
        }
        
        //...........................................................................................
        if (printjob_id) {
            printstart "Start job"
            status = CallPrintServer("/mapfile", [HEADER_CONTENTTYPE,HEADER_ACCEPT], "POST", DataType.JSON, """
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
                 (Defs.OSMPRINTMAP_PRINTCOLORCHANGES):false // contestMapParams.printColorChanges
                ]
            )
            printdone ""
            
            ret.ok = true
        }
        
        return ret
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
    void BackgroundJob(String actionName, String jobFileName, String jobId, String jobIdFileName, String fileIdFileName, String pngFileName, boolean printLandscape, String printSize, String printProjection, boolean printColorChanges)
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
                Map status = CallPrintServer("/mapstate/${jobId}", [HEADER_ACCEPT], "GET", DataType.JSON, "")
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
                    status = CallPrintServer("/mapfile/${jobId}", [HEADER_ACCEPT], "GET", DataType.BINARY, null)
                    if (status.responseCode == 200) {
                    
                        String download_zip_file_name = "${pngFileName}.zip"
                        String unpacked_png_file_name = "${pngFileName}.png"
                        String world_file_name = "${pngFileName}w"
                        String info_file_name = "${pngFileName}info"
                        String tif_file_name = "${pngFileName.substring(0,pngFileName.lastIndexOf('.'))}.tif"
                        String warped_tif_file_name = "${pngFileName.substring(0,pngFileName.lastIndexOf('.'))}.warped.tif"
                        String warped_png_file_name = "${pngFileName}.warped.png"
                        
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
                status = CallPrintServer("/metadata/${jobId}", [HEADER_CONTENTTYPE], "GET", DataType.JSON, null)
                printdone "responseCode=${status.responseCode}"
                
                //...........................................................................................
                printstart "Remove job"
                status = CallPrintServer("/${jobId}", [HEADER_ACCEPT], "DELETE", DataType.NONE, null)
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
    private boolean create_graticule_csv(String graticuleFileName,
                                         BigDecimal centerGraticuleLatitude, BigDecimal centerGraticuleLongitude,
                                         BigDecimal centerLatitude, BigDecimal centerLongitude,
                                         int printScale, int printWidth, int printHeight, int minPrintHeight, boolean alternatePos
                                        )
    {
        printstart "Write ${graticuleFileName}"
        println "centerGraticuleLatitude: ${centerGraticuleLatitude}"
        println "centerGraticuleLongitude: ${centerGraticuleLongitude}"
        
        BigDecimal print_width_nm = printScale * printWidth / Route.mmPerNM
        BigDecimal print_height_nm = printScale * printHeight / Route.mmPerNM
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
        BigDecimal start_lon = centerGraticuleLongitude
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
        BigDecimal start_lat = centerGraticuleLatitude
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
            if (coordmapobject_instance.mapObjectType != MapObjectType.Airfield) {
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
    private Map CallPrintServer(String funcURL, List headerList, String requestMethod, DataType dataType, def outputData)
    {
        Map ret = [responseCode:null, json:null, binary:null]
        
        String url_path = BootStrap.global.GetPrintServerAPI() + funcURL
        if (LOG_RESTAPI_CALLS) {
            println url_path
        }
        if (LOG_RESTAPI_OUTPUTDATA) {
            println outputData
        }
        def connection = url_path.toURL().openConnection()
        
        connection.requestMethod = requestMethod
        
        if (headerList) {
            headerList.each {
                connection.setRequestProperty( it.name, it.value )
            }
        }
        
        //String auth_str = "${LOGIN_NAME}:${LOGIN_PASSWORD}".getBytes().encodeBase64().toString()
        //connection.setRequestProperty( "Authorization", "Basic ${auth_str}" )
        try {
            if (outputData) {
                connection.doOutput = true
                switch (dataType) {
                    case DataType.JSON:
                        byte[] output_bytes = outputData.getBytes("UTF-8")
                        OutputStream os = connection.getOutputStream()
                        os.write(output_bytes)
                        os.close()
                        break
                    case DataType.BINARY:
                        OutputStream os = connection.getOutputStream()
                        os << outputData
                        os.close()
                        break
                }
            }
            switch (dataType) {
                case DataType.JSON:
                    try {
                        String s = connection.content.text
                        s = new String(s.getBytes("ISO-8859-1"), "UTF-8")
                        ret.json = new JsonSlurper().parseText(s)
                    } catch (Exception e) {
                        println "Exception (1): ${e.getMessage()} ${e}"
                    }
                    break
                case DataType.BINARY:
                    ret.binary = connection.content
                    break
            }
            ret.responseCode = connection.responseCode
            if (LOG_RESTAPI_RETURNS) {
                switch (dataType) {
                    case DataType.JSON:
                        println "responseCode=${ret.responseCode}, data=${ret.json}"
                        break
                    case DataType.BINARY:
                        println "responseCode=${ret.responseCode}"
                        //println "${ret.responseCode} ${ret.binary}"
                        break
                    default:
                        println "responseCode=${ret.responseCode}"
                        break
                }
            }
            return ret
        } catch (Exception e) {
            if (LOG_RESTAPI_EXCEPTIONS) {
                println "Exception (2): ${e.getMessage()} ${e}"
            }
        }
        
        return ret
    }

    //--------------------------------------------------------------------------
    private void FileUpload(String funcURL, String FilePath)
    {
        String url_path = BootStrap.global.GetPrintServerAPI() + funcURL
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
        
        String url_path = BootStrap.global.GetPrintServerAPI() + funcURL
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
