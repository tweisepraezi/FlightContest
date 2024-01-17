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

class TaskCreatorMapService
{
    def logService
    def messageSource
    def quartzScheduler
    def gpxService
	def openAIPService
    
    final static Map HEADER_CONTENTTYPE = [name:"Content-Type", value:"application/vnd.api+json; charset=utf-8"]
    final static Map HEADER_ACCEPT = [name:"Accept", value:"application/vnd.api+json; charset=utf-8"]
    
    final static String STYLE_FC = "fcmaps" // < 3.1.8 nutzt Stil opentopomap-fc 
    final static String STYLE_FC_DEV = "fcmaps-dev"
    final static String STYLE_PRINTMAPS_CARTO = "osm-carto"
    final static String STYLE_PRINTMAPS_CARTO_CONTOURLINES = "osm-carto-ele20"

    final static String HIDELAYERS_FC = "" // borders
    final static String HIDELAYERS_FC_CONTOURS20 = "contours20"
    final static String HIDELAYERS_FC_CONTOURS50 = "contours50"
    final static String HIDELAYERS_FC_CONTOURS100 = "contours100"
    final static String HIDELAYERS_FC_AIRFIELDSNAME = "airports-name"
    final static String HIDELAYERS_FC_AIRFIELDSICAO = "airports-icao"
    final static String HIDELAYERS_FC_CHURCHES = "symbols-poly-churches" // "symbols-poly-churches,symbols-point-churches"
    final static String HIDELAYERS_FC_CASTLES = "symbols-poly-castles" // "symbols-poly-castles,symbols-point-castles"
    final static String HIDELAYERS_FC_POWERLINES = "powerlines"
    final static String HIDELAYERS_FC_WINDPOWERSTATIONS = "symbols-point-windpowerstations,symbols-poly-windpowerstations"
    final static String HIDELAYERS_FC_SMALLROADS = "roads-small"
    final static String HIDELAYERS_PRINTMAPS_CARTO = "admin-low-zoom,admin-mid-zoom,admin-high-zoom,placenames-small,text-point,text-poly,text-poly-low-zoom,nature-reserve-boundaries,landuse-overlay,roads-text-name,roads-text-ref,roads-text-ref-low-zoom,amenity-points,amenity-points-poly,junctions,ferry-routes,stations,stations-poly,tourism-boundary,water-lines-text,bridge-text,railways-text-name"
    final static String HIDELAYERS_PRINTMAPS_CARTO_MUNICIPALITY = "placenames-medium"
    
    final static String ATTR_OUTPUT_PROJECTION = "4326" // EPSG-Nummer, WGS84 / Pseudo-Mercator, Google Maps, OpenStreetMap und andere Kartenanbieter im Netz
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
    
    final static int TP_FONT_SIZE = 24
    
    final static BigDecimal FRAME_STROKE_WIDTH = 1
    final static BigDecimal TRACK_STROKE_WIDTH = 0.75
    
    final static BigDecimal GRATICULE_SCALEBAR_LEN = 0.2 // NM
    final static int AIRFIELD_TEXT_FONT_SIZE = 10
    final static int PEAKS_TEXT_FONT_SIZE = 10
    final static int SPECIALS_TEXT_FONT_SIZE = 10
    final static String GEODATA_BUILDING_SCALE = "scale(0.75, 0.75)"
    final static String GEODATA_SYMBOL_SCALE = "scale(0.75, 0.75)"
    final static int SYMBOL_TEXT_FONT_SIZE = 10
    
    final static String AIRSPACE_PRAEFIX = "AIRSPACE:"
    final static String AIRSPACE_LAYER_STYLE_SEPARATOR = ","
    final static String AIRSPACE_LAYER_STYLE_KEY_VALUE_SEPARATOR = ":"
    final static BigDecimal AIRSPACE_STROKE_WIDTH = 0.75
    
    // 1:50000
    //final static int landuse_residential = 0xe0dfdf
    //final static int landuse_commercial = 0xf2dad9
    //final static int landuse_industrial = 0xebdbe8
    //final static int landuse_retail = 0xffd6d1 // Handel
    //final static int buildings = 0xd1c6bd
    //final static int buildings_major = 0xaf9c8d
    //final static List COLOR_CHANGES = [[NewRGB:0xf7b5bd, OldRGBs:[landuse_residential,landuse_commercial,landuse_industrial,landuse_retail,buildings,buildings_major]]]
    
    // 1:100000
    final static List COLOR_CHANGES = [[OldRGBs:[0xd3d3d3], NewRGB:0xf7b5bd], // Ortschaften, Industrie (grau -> rot)
                                       [OldRGBs:[0xfbeeda], NewRGB:0xffffff], // Felder (grün -> weiß)
                                       [OldRGBs:[0xdbf1c6], NewRGB:0xffffff], // Wiesen (grün -> weiß)
                                       [OldRGBs:[0xf2efe9], NewRGB:0xffffff], // Felder (grau -> weiß)
                                       [OldRGBs:[0xffffff], NewRGB:0xbbbbbb], // Straße (weiß -> grau)
                                       [OldRGBs:[0xe892a2], NewRGB:0xf85858], // Straße (rot -> rot)
                                       [OldRGBs:[0xfcd6a4], NewRGB:0xf85858], // Straße (orange -> rot)
                                       [OldRGBs:[0xf9b29c], NewRGB:0xf85858], // Straße (orange -> rot)
                                       [OldRGBs:[0xf7fabf], NewRGB:0xf8f858], // Straße (gelb -> gelb)
                                      ]
    
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
        
        String printjob_filename = contestMapParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB
        String printjobid_filename = contestMapParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOBID + contestMapParams.routeId + ".txt"
        String printfileid_filename = contestMapParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTFILEID + contestMapParams.routeId + ".txt"
        
        /*
        if (new File(printjob_filename).exists()) {
            ret.message = getMsg('fc.contestmap.previousjobrunningerror', false)
            return ret
        }
        */
        
        String style = ""
        String hide_layers = ""
        if (contestMapParams.contestMapFCStyle) {
            style = STYLE_FC
            hide_layers = HIDELAYERS_FC
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
            if (contestMapParams.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_GEODATA) {
                hide_layers += ",${HIDELAYERS_FC_AIRFIELDSNAME},${HIDELAYERS_FC_AIRFIELDSICAO}"
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
            if (!contestMapParams.contestMapSmallRoads) {
                hide_layers += ",${HIDELAYERS_FC_SMALLROADS}"
            }
            if (contestMapParams.contestMapDevStyle) {
                style = STYLE_FC_DEV
            }
        } else {
            if (contestMapParams.contestMapContourLines) {
                style = STYLE_PRINTMAPS_CARTO_CONTOURLINES
            } else {
                style = STYLE_PRINTMAPS_CARTO
            }
            hide_layers = HIDELAYERS_PRINTMAPS_CARTO
            if (!contestMapParams.contestMapMunicipalityNames) {
                hide_layers += ",${HIDELAYERS_PRINTMAPS_CARTO_MUNICIPALITY}"
            }
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
                if (contestMapParams.contestMapPrintLandscape) {
                    print_width = ANR_LONG
                    print_height = ANR_SHORT
                } else {
                    print_width = ANR_SHORT
                    print_height = ANR_LONG
                }
                break
            case Defs.CONTESTMAPPRINTSIZE_AIRPORTAREA:
                paper_size = "T/O"
                print_width = 2*AIRPORTAREA_DISTANCE
                print_height = 2*AIRPORTAREA_DISTANCE
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
        
        // reduce print_height
        if (ATTR_OUTPUT_PROJECTION == "4326") {
            BigDecimal latlon_relation = AviationMath.getLatLonDistanceRelation(contestMapParams.centerLatitude)
            print_height *= latlon_relation
        }
        
        //...........................................................................................
        String mapdata_date = ""
        printstart "Get service capabilities"
        Map status = CallPrintServer("/capabilities/service", [HEADER_ACCEPT], "GET", DataType.JSON, "")
        if (status.responseCode == 200) {
            for (def config_style in status.json.ConfigStyles) {
                if (config_style.Name == style) {
                    mapdata_date = config_style.Date
                }
            }
            printdone mapdata_date
        } else {
            printerror ""
        }
        
        //...........................................................................................
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
            "Style": "<MarkersSymbolizer file='[sym]' transform='${GEODATA_SYMBOL_SCALE}' placement='point' />",
            "SRS": "+init=${ATTR_INPUT_SRS}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "waypoints"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${SYMBOL_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dx='10' placement='point'>[type]</TextSymbolizer>",
            "SRS": "+init=${ATTR_INPUT_SRS}",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "waypoints"
        }"""
        
        String airfields_lines = ""
        String airfields_file_name = ""
        if (contestMapParams.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_GEODATA) {
            airfields_file_name = Defs.FCSAVE_FILE_GEODATA_AIRFIELDS
            String airfields_short_file_name = airfields_file_name.substring(airfields_file_name.lastIndexOf('/')+1)
            airfields_lines = """,{
                "Style": "<MarkersSymbolizer file='airfield.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' allow-overlap='true' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${airfields_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${AIRFIELD_TEXT_FONT_SIZE}' fill='black' halo-radius='1' halo-fill='white' allow-overlap='true' dy='10' placement='point'>[name]</TextSymbolizer>",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${airfields_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String churches_lines = ""
        String churches_file_name = ""
        if (false && contestMapParams.contestMapChurches) {
            churches_file_name = Defs.FCSAVE_FILE_GEODATA_CHURCHES
            String churches_short_file_name = churches_file_name.substring(churches_file_name.lastIndexOf('/')+1)
            churches_lines = """,{
                "Style": "<MarkersSymbolizer file='church.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${churches_short_file_name}",
                "Layer": ""
            }"""
        }

        String castles_lines = ""
        String castles_file_name = ""
        if (false && contestMapParams.contestMapCastles) {
            castles_file_name = Defs.FCSAVE_FILE_GEODATA_CASTLES
            String castles_short_file_name = castles_file_name.substring(castles_file_name.lastIndexOf('/')+1)
            castles_lines = """,{
                "Style": "<MarkersSymbolizer file='castle.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${castles_short_file_name}",
                "Layer": ""
            }"""
        }

        String chateaus_lines = ""
        String chateaus_file_name = ""
        if (false && contestMapParams.contestMapChateaus) {
            chateaus_file_name = Defs.FCSAVE_FILE_GEODATA_CHATEAUS
            String chateaus_short_file_name = chateaus_file_name.substring(chateaus_file_name.lastIndexOf('/')+1)
            chateaus_lines = """,{
                "Style": "<MarkersSymbolizer file='chateau.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${chateaus_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String windpowerstations_lines = ""
        String windpowerstations_file_name = ""
        if (false && contestMapParams.contestMapWindpowerstations) {
            windpowerstations_file_name = Defs.FCSAVE_FILE_GEODATA_WINDPOWERSTATIONS
            String windpowerstations_short_file_name = windpowerstations_file_name.substring(windpowerstations_file_name.lastIndexOf('/')+1)
            windpowerstations_lines = """,{
                "Style": "<MarkersSymbolizer file='windpowerstation.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${windpowerstations_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String peaks_lines = ""
        String peaks_file_name = ""
        if (false && contestMapParams.contestMapPeaks) {
            peaks_file_name = Defs.FCSAVE_FILE_GEODATA_PEAKS
            String peaks_short_file_name = peaks_file_name.substring(peaks_file_name.lastIndexOf('/')+1)
            peaks_lines = """,{
                "Style": "<MarkersSymbolizer file='peak.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${peaks_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${PEAKS_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dy='10' placement='point'>[altitude]</TextSymbolizer>",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${peaks_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String additionals_lines = ""
        String additionals_file_name = ""
        if (contestMapParams.contestMapAdditionals) {
            additionals_file_name = Defs.FCSAVE_FILE_GEODATA_ADDITIONALS
            String additionals_short_file_name = additionals_file_name.substring(additionals_file_name.lastIndexOf('/')+1)
            additionals_lines = """,{
                "Style": "<MarkersSymbolizer file='[symbol]' transform='${GEODATA_BUILDING_SCALE}' allow-overlap='true' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${additionals_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${AIRFIELD_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dy='10' placement='point'>[name]</TextSymbolizer>",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${additionals_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String specials_lines = ""
        String specials_file_name = ""
        if (contestMapParams.contestMapSpecials) {
            specials_file_name = Defs.FCSAVE_FILE_GEODATA_SPECIALS
            String specials_short_file_name = specials_file_name.substring(specials_file_name.lastIndexOf('/')+1)
            specials_lines = """,{
                "Style": "<MarkersSymbolizer file='special.png' transform='${GEODATA_BUILDING_SCALE}' allow-overlap='true' placement='point' />",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${specials_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${SPECIALS_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dx='[dx]' dy='[dy]' placement='point'>[name]</TextSymbolizer>",
                "SRS": "+init=${ATTR_INPUT_SRS}",
                "Type": "csv",
                "File": "${specials_short_file_name}",
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
				Map ret2 = openAIPService.WriteAirspaces2KMZ(route_instance, contestMapParams.webRootDir, file_name, false)
				if (!ret2.ok) {
                    ret.message = getMsg('fc.contestmap.contestmapairspaces.kmzexport.missedairspaces', [ret2.missingAirspaces], false)
                    return ret
				}
				airspaces_file_name = contestMapParams.webRootDir + "${Defs.ROOT_FOLDER_GPXUPLOAD}/AIRSPACES-${uuid}-UPLOAD.kmz"
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
                        String airspace_textspacing = '100'
                        String airspace_textcolor = 'black'
                        String airspace_fillcolor = 'steelblue'
                        String airspace_fillopacity = '0.2'
                        for (String airspace_style in Tools.Split(airspace_layer, AIRSPACE_LAYER_STYLE_SEPARATOR)) {
                            List airspace_style_values = Tools.Split(airspace_style.trim(), AIRSPACE_LAYER_STYLE_KEY_VALUE_SEPARATOR)
                            if (airspace_style_values.size() == 1) {
                                airspace_layer = airspace_style_values[0].trim()
                                airspace_text = airspace_layer
                            } else if (airspace_style_values.size() == 2) {
                                switch (airspace_style_values[0].trim()) {
                                    case 'file': 
                                        airspace_filename = airspace_style_values[1].trim()
                                        break
                                    case 'text': 
                                        airspace_text = airspace_style_values[1].trim()
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
                        }
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
                            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${airspace_textsize}' fill='${airspace_textcolor}' allow-overlap='false' placement='line' halo-radius='1' halo-fill='white' spacing='${airspace_textspacing}'>'${airspace_text}'</TextSymbolizer>",
                            "SRS": "+init=${ATTR_INPUT_SRS}",
                            "Type": "ogr",
                            "File": "${airspace_filename}",
                            "Layer": "${airspace_layer}"
                        }"""
                    }
                }
            }
        }
        
        printstart "print_osm Scale=1:${print_scale} Width=${print_width} Height=${print_height}"
        println "print_width: $print_width mm"
        println "print_height: $print_height mm"
    
        String printjob_id = ""
        boolean job_started = false
        boolean job_successfully = false
        int img_width = 0
        int img_height = 0
        
        //...........................................................................................
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
                    "Style": "${style}",
                    "Projection": "${ATTR_OUTPUT_PROJECTION}",
                    "HideLayers": "${hide_layers}",
                    "UserObjects": [
                        ${gpx_lines}
                        ${airfields_lines}
                        ${churches_lines}
                        ${castles_lines}
                        ${chateaus_lines}
                        ${windpowerstations_lines}
                        ${peaks_lines}
                        ${additionals_lines}
                        ${specials_lines}
                        ${airspaces_lines}
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
    
        //...........................................................................................
        if (printjob_id) {
            // route
            printstart "Upload gpx"
            FileUpload("/upload/${printjob_id}", gpx_file_name)
            printdone ""
            // symbols
            printstart "Upload airfield.png"
            FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + "images/map/airfield.png")
            printdone ""
            printstart "Upload church.png"
            FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + "images/map/church.png")
            printdone ""
            printstart "Upload castle.png"
            FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + "images/map/castle.png")
            printdone ""
            printstart "Upload castle_ruin.png"
            FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + "images/map/castle_ruin.png")
            printdone ""
            printstart "Upload chateau.png"
            FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + "images/map/chateau.png")
            printdone ""
            printstart "Upload windpowerstation.png"
            FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + "images/map/windpowerstation.png")
            printdone ""
            printstart "Upload peak.png"
            FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + "images/map/peak.png")
            printdone ""
            printstart "Upload special.png"
            FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + "images/map/special.png")
            printdone ""
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
            if (contestMapParams.contestMapEnroutePhotos) {
                printstart "Upload fcphoto.png"
                FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + "GM_Utils/Icons/fcphoto.png")
                printdone ""
            }
            if (contestMapParams.contestMapEnrouteCanvas) {
                EnrouteCanvasSign.each { enroute_canvas_sign ->
                    if (enroute_canvas_sign.imageName) {
                        printstart "Upload ${enroute_canvas_sign.imageName}"
                        FileUpload("/upload/${printjob_id}", contestMapParams.webRootDir + enroute_canvas_sign.imageName)
                        printdone ""
                    }
                }
            }
            if (airfields_file_name) {
                printstart "Upload airfields"
                FileUpload("/upload/${printjob_id}", airfields_file_name)
                printdone ""
            }
            if (churches_file_name) {
                printstart "Upload churches"
                FileUpload("/upload/${printjob_id}", churches_file_name)
                printdone ""
            }
            if (castles_file_name) {
                printstart "Upload castles"
                FileUpload("/upload/${printjob_id}", castles_file_name)
                printdone ""
            }
            if (chateaus_file_name) {
                printstart "Upload chateaus"
                FileUpload("/upload/${printjob_id}", chateaus_file_name)
                printdone ""
            }
            if (windpowerstations_file_name) {
                printstart "Upload windpowerstations"
                FileUpload("/upload/${printjob_id}", windpowerstations_file_name)
                printdone ""
            }
            if (peaks_file_name) {
                printstart "Upload peaks"
                FileUpload("/upload/${printjob_id}", peaks_file_name)
                printdone ""
            }
            if (additionals_file_name) {
                printstart "Upload additionals"
                FileUpload("/upload/${printjob_id}", additionals_file_name)
                printdone ""
            }
            if (specials_file_name) {
                printstart "Upload specials"
                FileUpload("/upload/${printjob_id}", specials_file_name)
                printdone ""
            }
            if (airspaces_file_name) {
                printstart "Upload airspaces"
                FileUpload("/upload/${printjob_id}", airspaces_file_name)
                printdone ""
				if (BootStrap.global.IsOpenAIP()) {
					gpxService.DeleteFile(airspaces_file_name)
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
                 (Defs.OSMPRINTMAP_PRINTPROJECTION):ATTR_OUTPUT_PROJECTION,
                 (Defs.OSMPRINTMAP_PRINTCOLORCHANGES):false // contestMapParams.printColorChanges
                ]
            )
            printdone ""
            
            ret.ok = true
        }
        
        return ret
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
        
        /*
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
        
        //String auth_str = "${LOGIN_NAME}:${LOGIN_PASSWORD}".getBytes().encodeBase64().toString()
        //connection.setRequestProperty( "Authorization", "Basic ${auth_str}" )
        try {
            switch (dataType) {
                case DataType.JSON:
                    String s = connection.content.text
                    s = new String(s.getBytes("ISO-8859-1"), "UTF-8")
                    ret.json = new JsonSlurper().parseText(s)
                    break
                case DataType.BINARY:
                    ret.binary = connection.content
                    break
            }
            ret.responseCode = connection.responseCode
            if (LOG_RESTAPI_RETURNS) {
                if (dataType == DataType.JSON) {
                    println "${ret.responseCode} ${ret.json}"
                } else {
                    println "${ret.responseCode}"
                }
            }
            return ret
        } catch (Exception e) {
            if (LOG_RESTAPI_EXCEPTIONS) {
                println "Exception: ${e.getMessage()} ${e}"
            }
        }
        */
        
        return ret
    }

    //--------------------------------------------------------------------------
    private void downscaleImage(String pngInputFileName, String pngResizedFileName, int imgWidth, int imgHeight, boolean isLandscape, boolean printColorChanges)
    {
        println "Width=${imgWidth}, Height=${imgHeight}, Landscape=${isLandscape}"
        try {
            File input_png_file = new File(pngInputFileName)
            BufferedImage input_img = ImageIO.read(input_png_file)
            int type = input_img.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : input_img.getType()
            
            BufferedImage resized_image = resizeImage(input_img, type, imgWidth, imgHeight, printColorChanges)
            
            def dest_file = new File(pngResizedFileName)
            savePNGImage(dest_file, resized_image, isLandscape)
            
        } catch(IOException e) {
            println(e.getMessage())
        }
    }
    
    //--------------------------------------------------------------------------
    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int imgWidth, int imgHeight, boolean printColorChanges)
    {
        BufferedImage resized_image = new BufferedImage(imgWidth, imgHeight, type)
        
        Graphics2D g = resized_image.createGraphics()
        g.drawImage(originalImage, 0, 0, imgWidth, imgHeight, null)
        g.dispose()
    
        if (printColorChanges) {
            BufferedImage recolored_image = new BufferedImage(imgWidth, imgHeight, type)
            for (int x = 0; x < imgWidth; x++) {
                for (int y = 0; y < imgHeight; y++) {
                    int pixel = resized_image.getRGB(x, y)
                    int alpha = (pixel & 0xff000000) >> 24
                    int rgb = (pixel & 0x00ffffff)
                    for (Map color_change in COLOR_CHANGES) {
                        for (int old_rgb in color_change.OldRGBs) {
                            if (rgb == old_rgb) {
                                pixel = (alpha << 24) + color_change.NewRGB
                            }
                        }
                    }
                    recolored_image.setRGB(x, y, pixel)
                }
            }
            return recolored_image
        }
        
        return resized_image
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
