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

// Karten mit printmaps-osm.de erstellen
//   http://printmaps-osm.de:8080/de/server.html
//   https://github.com/printmaps/printmaps
//     Map generation: https://github.com/printmaps/printmaps/tree/master/Nik4 
//   http://www.gdal.org/
//     http://www.gdal.org/ogr_formats.html
//   https://forum.openstreetmap.org/viewtopic.php?id=57945&p=3
//   https://github.com/mapnik/mapnik/wiki/TextSymbolizer
//   https://github.com/mapnik/mapnik/wiki/LineSymbolizer
//   https://github.com/mapnik/mapnik/wiki/PolygonSymbolizer
//   https://github.com/mapnik/mapnik/wiki/MarkersSymbolizer
//   https://docs.oracle.com/javase/7/docs/api/java/net/URLConnection.html
//   https://stackoverflow.com/questions/321736/how-to-set-dpi-information-in-an-image
//   https://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/standard_metadata.html
//   https://de.wikipedia.org/wiki/European_Petroleum_Survey_Group_Geodesy   EPSG-Nummern

class OsmPrintMapService
{
    def logService
    def messageSource
    def quartzScheduler
    def gpxService
    
    final static Map HEADER_CONTENTTYPE = [name:"Content-Type", value:"application/vnd.api+json; charset=utf-8"]
    final static Map HEADER_ACCEPT = [name:"Accept", value:"application/vnd.api+json; charset=utf-8"]
    
    final static String STYLE_STANDARD = "osm-carto"
    final static String STYLE_CONTOURLINES = "osm-carto-ele20"
    final static String STYLE_OTM = "opentopomap-fc"
    
    final static String HIDELAYERS_CARTO = "admin-low-zoom,admin-mid-zoom,admin-high-zoom,placenames-small,text-point,text-poly,text-poly-low-zoom,nature-reserve-boundaries,landuse-overlay,roads-text-name,roads-text-ref,roads-text-ref-low-zoom,amenity-points,amenity-points-poly,junctions,ferry-routes,stations,stations-poly,tourism-boundary,water-lines-text,bridge-text,railways-text-name"
    final static String HIDELAYERS_CARTO_MUNICIPALITY = "placenames-medium"
    
    final static String HIDELAYERS_OTM = "" // borders
    final static String HIDELAYERS_OTM_CONTOURS = "contours"

    final static String ATTR_PROJECTION = "3857" // EPSG-Nummer, WGS 84 / Pseudo-Mercator, Google Maps, OpenStreetMap und andere Kartenanbieter im Netz
     
    // Formate
    //   DIN-A3 Hochformat = 297 x 420 mm, DIN-A3 Querformat = 420 x 297 mm
    //   DIN-A4 Hochformat = 210 x 297 mm, DIN-A4 Querformat = 297 x 210 mm
    
    final static int A3_SHORT = 297 // mm
    final static int A3_LONG = 420 // mm
    final static int A4_SHORT = 210 // mm
    final static int A4_LONG = 297 // mm
    final static int MARGIN = 10 // nicht bedruckbarer Rand [mm]

    final static int FACTOR = 2 // Maßstabsverkleinerung (1 = 1:200000, 2 =  1:100000, 4 = 1:50000)
    final static int FACTOR2 = 1 // Druckvergrößerung (1 = 1:100000, 2 = 1:50000)
    final static int DPI = 600 // Original: 300 DPI
    
    final static String SCALE_TITLE = "200000"
    
    final static int TEXT_XPOS_LEFT = 5  // Abstand vom linken Rand [mm]
    final static int TEXT_XPOS_RIGHT = 5 // Abstand vom rechten Rand [mm]
    
    final static int CONTEST_TITLE_FONT_SIZE = FACTOR*14
    final static int CONTEST_TITLE_YPOS_TOP = 4
     
    final static int ROUTE_TITLE_FONT_SIZE = FACTOR*10
    final static int ROUTE_TITLE_YPOS_CONTEST_TITLE = 4
    
    final static int BOTTOM_TEXT_FONT_SIZE = FACTOR*8
    final static int BOTTOM_TEXT_YPOS = FACTOR*2 // mm
    final static int BOTTOM_TEXT_YPOS2 = FACTOR*5 // mm
    
    final static int SCALEBAR_YPOS_TOP = 4
    final static BigDecimal SCALEBAR_X_DIFF = FACTOR*5*GpxService.kmPerNM // 1 NM (9.26mm)
    final static String SCALEBAR_TITLE = "5 NM"
    final static int SCALEBAR_TITLE_FONT_SIZE = FACTOR*8
    
    final static int TP_FONT_SIZE = FACTOR*24
    
    final static BigDecimal FRAME_STROKE_WIDTH = FACTOR*1
    final static BigDecimal TRACK_STROKE_WIDTH = FACTOR*0.75
    final static BigDecimal GRATICULE_STROKE_WIDTH = FACTOR*0.25
    final static BigDecimal SCALEBAR_STROKE_WIDTH = FACTOR*4
    
    final static int SCALE = 200000/FACTOR
    
    final static int GRATICULE_TEXT_FONT_SIZE = FACTOR*6
    final static int AIRFIELD_TEXT_FONT_SIZE = FACTOR*6
    final static int PEAKS_TEXT_FONT_SIZE = FACTOR*5
    final static int SPECIALS_TEXT_FONT_SIZE = FACTOR*5
    final static String GEODATA_BUILDING_SCALE = "scale(1.0, 1.0)"
    final static String GEODATA_SYMBOL_SCALE = "scale(1.0, 1.0)"
    final static int SYMBOL_TEXT_FONT_SIZE = FACTOR*10
    
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
    Map PrintOSM(Map printParams)
    {
        printstart "PrintOSM"
        println "Params: ${printParams}"
        
        Map ret = [ok:false, message:'']
        boolean graticule = false
        Map print_options = [centerLatitude: 0.0,
                             centerLongitude: 0.0,
                             centerGraticuleLatitude: 0.0,
                             centerGraticuleLongitude: 0.0,
                             printEnroutePhotos: false,
                             printEnrouteCanvas: false,
                             printContourLines: false,
                             printMunicipalityNames: false,
                             printAirfields: false,
                             printChurches: false,
                             printCastles: false,
                             printChateaus: false,
                             printWindpowerstations: false,
                             printPeaks: false,
                             printAdditionals: false,
                             printSpecials: false,
                             printAirspaces: false,
                             printLandscape: true,
                             printA3: true,
                             printColorChanges: false,
                             printOTM: false
                            ]
        
        File gpx_file = new File(printParams.gpxFileName)
        FileReader gpx_reader = new FileReader(gpx_file)
        try {
            def gpx = new XmlParser().parse(gpx_reader)
            def m = gpx.extensions.flightcontest.contestmap
            graticule = m.'@graticule'[0] == "yes"
            print_options.centerLatitude = m.'@center_latitude'[0].toBigDecimal()
            print_options.centerLongitude = m.'@center_longitude'[0].toBigDecimal()
            print_options.centerGraticuleLatitude = m.'@center_graticule_latitude'[0].toBigDecimal()
            print_options.centerGraticuleLongitude = m.'@center_graticule_longitude'[0].toBigDecimal()
            print_options.printEnroutePhotos = m.'@enroutephotos'[0] == "yes"
            print_options.printEnrouteCanvas = m.'@enroutecanvas'[0] == "yes"
            print_options.printContourLines = m.'@contour_lines'[0] == "yes"
            print_options.printMunicipalityNames = m.'@municipality_names'[0] == "yes"
            print_options.printAirfields = m.'@airfields'[0] == "yes"
            print_options.printChurches = m.'@churches'[0] == "yes"
            print_options.printCastles = m.'@castles'[0] == "yes"
            print_options.printChateaus = m.'@chateaus'[0] == "yes"
            print_options.printWindpowerstations = m.'@windpowerstations'[0] == "yes"
            print_options.printPeaks = m.'@peaks'[0] == "yes"
            print_options.printAdditionals = m.'@additionals'[0] == "yes"
            print_options.printSpecials = m.'@specials'[0] == "yes"
            print_options.printAirspaces = m.'@airspaces'[0] == "yes"
            print_options.printLandscape = m.'@landscape'[0] == "yes"
            print_options.printA3 = m.'@a3'[0] == "yes"
            print_options.printColorChanges = m.'@colorchanges'[0] == "yes"
            print_options.printOTM = m.'@otm'[0] == "yes"
            println "Options: ${print_options}"
            ret.ok = true
        } catch (Exception e) {
            println e.getMessage()
        }
        gpx_reader.close()

        if (ret.ok) {
            if (!graticule) {
                printParams.graticuleFileName = ""
            }
        }
        if (ret.ok) {
            ret = print_osm(printParams, print_options)
        }
        
        printdone ""
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map print_osm(Map printParams, Map printOptions)
    {
        Map ret = [ok:false, message:'']
        
        String printjob_filename = printParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB
        String printjobid_filename = printParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOBID + printParams.routeId + ".txt"
        String printfileid_filename = printParams.webRootDir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTFILEID + printParams.routeId + ".txt"
        
        /*
        if (new File(printjob_filename).exists()) {
            ret.message = getMsg('fc.contestmap.previousjobrunningerror', false)
            return ret
        }
        */
        
        String graticule_file_name = printParams.graticuleFileName.replaceAll('\\\\', '/')

        String style = ""
        String hide_layers = ""
        if (!printOptions.printOTM) {
            if (printOptions.printContourLines) {
                style = STYLE_CONTOURLINES
            } else {
                style = STYLE_STANDARD
            }
            hide_layers = HIDELAYERS_CARTO
            if (!printOptions.printMunicipalityNames) {
                hide_layers += ",${HIDELAYERS_CARTO_MUNICIPALITY}"
            }
        } else {
            style = STYLE_OTM
            hide_layers = HIDELAYERS_OTM
            if (!printOptions.printContourLines) {
                hide_layers += ",${HIDELAYERS_OTM_CONTOURS}"
            }
        }
        
        int print_width = 0 // mm
        int print_height = 0 // mm
        String paper_size = ""
        if (printOptions.printLandscape) {
            if (printOptions.printA3) {
                print_width = A3_LONG
                print_height = A3_SHORT
                paper_size = "A3"
            } else {
                print_width = A4_LONG
                print_height = A4_SHORT
                paper_size = "A4"
            }
        } else {
            if (printOptions.printA3) {
                print_width = A3_SHORT
                print_height = A3_LONG
                paper_size = "A3"
            } else {
                print_width = A4_SHORT
                print_height = A4_LONG
                paper_size = "A4"
            }
        }
        print_width -= 2*MARGIN
        print_height -= 2*MARGIN
        print_width *= FACTOR
        print_height *= FACTOR
        
        int contest_title_ypos = print_height - CONTEST_TITLE_YPOS_TOP*FACTOR
        int route_title_ypos = contest_title_ypos - ROUTE_TITLE_YPOS_CONTEST_TITLE*FACTOR
        int text_xpos_left = TEXT_XPOS_LEFT*FACTOR
        int text_xpos_right = print_width - TEXT_XPOS_RIGHT*FACTOR
        int scalebar_xpos = text_xpos_right - TEXT_XPOS_RIGHT*SCALEBAR_X_DIFF
        int scalebar_ypos = print_height - SCALEBAR_YPOS_TOP*FACTOR
        int scalbar_text_ypos = scalebar_ypos - 3*FACTOR   // 3mm nach unten
        
        String copyright_text = getMsg('fc.contestmap.copyright.osm',true)
        String copyright_date = GeoDataService.ReadTxtFile(Defs.FCSAVE_FILE_GEODATA_DATE)
        if (printOptions.printAirfields || printOptions.printChurches || printOptions.printCastles || printOptions.printChateaus || printOptions.printWindpowerstations || printOptions.printPeaks) {
            copyright_text += ", ${getMsg('fc.contestmap.copyright.bkg',[copyright_date],true)}"
        }
        if (printOptions.printOTM) {
            copyright_text += ", ${getMsg('fc.contestmap.copyright.srtm',[],true)}"
            copyright_text += ", ${getMsg('fc.contestmap.copyright.otm',[],true)}"
        }
        String user_text = """{
            "Style": "<LineSymbolizer stroke='black' stroke-width='${FRAME_STROKE_WIDTH}' stroke-linecap='butt' />",
            "WellKnownText": "LINESTRING(0.0 0.0, 0.0 ${print_height}, ${print_width} ${print_height}, ${print_width} 0.0, 0.0 0.0)"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-2' size='${CONTEST_TITLE_FONT_SIZE}' fill='black' horizontal-alignment='right' halo-radius='1' halo-fill='white' allow-overlap='true'>'${printParams.contestTitle}'</TextSymbolizer>",
            "WellKnownText": "POINT(${text_xpos_left} ${contest_title_ypos})"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-2' size='${ROUTE_TITLE_FONT_SIZE}' fill='black' horizontal-alignment='right' halo-radius='1' halo-fill='white' allow-overlap='true'>'${printParams.routeTitle}'</TextSymbolizer>",
            "WellKnownText": "POINT(${text_xpos_left} ${route_title_ypos})"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${BOTTOM_TEXT_FONT_SIZE}' fill='black' horizontal-alignment='right' halo-radius='1' halo-fill='white' allow-overlap='true'>'${getMsg('fc.contestmap.copyright',true)}'</TextSymbolizer>",
            "WellKnownText": "POINT(${text_xpos_left} ${BOTTOM_TEXT_YPOS2})"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${BOTTOM_TEXT_FONT_SIZE}' fill='black' horizontal-alignment='right' halo-radius='1' halo-fill='white' allow-overlap='true'>'${copyright_text}'</TextSymbolizer>",
            "WellKnownText": "POINT(${text_xpos_left} ${BOTTOM_TEXT_YPOS})"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${BOTTOM_TEXT_FONT_SIZE}' fill='black' horizontal-alignment='left' halo-radius='1' halo-fill='white' allow-overlap='true'>'${getMsg('fc.contestmap.scale',true)} 1:${SCALE_TITLE}, ${paper_size}'</TextSymbolizer>",
            "WellKnownText": "POINT(${text_xpos_right} ${BOTTOM_TEXT_YPOS})"
        },
        {
            "Style": "<LineSymbolizer stroke='black' stroke-width='${SCALEBAR_STROKE_WIDTH}' stroke-opacity='1' stroke-linecap='butt' />",
            "WellKnownText": "LINESTRING(${scalebar_xpos} ${scalebar_ypos}, ${scalebar_xpos + SCALEBAR_X_DIFF} ${scalebar_ypos})"
        },            
        {
            "Style": "<LineSymbolizer stroke='black' stroke-width='${SCALEBAR_STROKE_WIDTH}' stroke-opacity='0.2' stroke-linecap='butt' />",
            "WellKnownText": "LINESTRING(${scalebar_xpos + SCALEBAR_X_DIFF} ${scalebar_ypos}, ${scalebar_xpos + 2*SCALEBAR_X_DIFF} ${scalebar_ypos})"
        },            
        {
            "Style": "<LineSymbolizer stroke='black' stroke-width='${SCALEBAR_STROKE_WIDTH}' stroke-opacity='1' stroke-linecap='butt' />",
            "WellKnownText": "LINESTRING(${scalebar_xpos + 2*SCALEBAR_X_DIFF} ${scalebar_ypos}, ${scalebar_xpos + 3*SCALEBAR_X_DIFF} ${scalebar_ypos})"
        },            
        {
            "Style": "<LineSymbolizer stroke='black' stroke-width='${SCALEBAR_STROKE_WIDTH}' stroke-opacity='0.2' stroke-linecap='butt' />",
            "WellKnownText": "LINESTRING(${scalebar_xpos + 3*SCALEBAR_X_DIFF} ${scalebar_ypos}, ${scalebar_xpos + 4*SCALEBAR_X_DIFF} ${scalebar_ypos})"
        },            
        {
            "Style": "<LineSymbolizer stroke='black' stroke-width='${SCALEBAR_STROKE_WIDTH}' stroke-opacity='1' stroke-linecap='butt' />",
            "WellKnownText": "LINESTRING(${scalebar_xpos + 4*SCALEBAR_X_DIFF} ${scalebar_ypos}, ${scalebar_xpos + 5*SCALEBAR_X_DIFF} ${scalebar_ypos})"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${SCALEBAR_TITLE_FONT_SIZE}' fill='black' horizontal-alignment='left' halo-radius='1' halo-fill='white' allow-overlap='true'>'${SCALEBAR_TITLE}'</TextSymbolizer>",
            "WellKnownText": "POINT(${text_xpos_right} ${scalbar_text_ypos})"
        }"""

        String gpx_file_name = printParams.gpxFileName.replaceAll('\\\\', '/')
        String gpx_short_file_name = gpx_file_name.substring(gpx_file_name.lastIndexOf('/')+1)
        String gpx_lines = """,{
            "Style": "<LineSymbolizer stroke='black' stroke-width='${TRACK_STROKE_WIDTH}' stroke-linecap='round' />",
            "SRS": "+init=epsg:4326",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "routes"
        },
        {
            "Style": "<LineSymbolizer stroke='red' stroke-width='${TRACK_STROKE_WIDTH}' stroke-linecap='round' />",
            "SRS": "+init=epsg:4326",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "tracks"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-2' size='${TP_FONT_SIZE}' fill='black' halo-radius='1' halo-fill='white' allow-overlap='true'>[name]</TextSymbolizer>",
            "SRS": "+init=epsg:4326",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "waypoints"
        },
        {
            "Style": "<MarkersSymbolizer file='[sym]' transform='${GEODATA_SYMBOL_SCALE}' placement='point' />",
            "SRS": "+init=epsg:4326",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "waypoints"
        },
        {
            "Style": "<TextSymbolizer fontset-name='fontset-0' size='${SYMBOL_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dx='20' placement='point'>[type]</TextSymbolizer>",
            "SRS": "+init=epsg:4326",
            "Type": "ogr",
            "File": "${gpx_short_file_name}",
            "Layer": "waypoints"
        }"""
        
        String graticule_lines = ""
        if (graticule_file_name) {
            if (create_graticule_csv(graticule_file_name, printOptions.centerGraticuleLatitude, printOptions.centerGraticuleLongitude, printOptions.centerLatitude, printOptions.centerLongitude, print_width, print_height)) {
                String graticule_short_file_name = graticule_file_name.substring(graticule_file_name.lastIndexOf('/')+1)
                graticule_lines = """,{
                    "Style": "<LineSymbolizer stroke='black' stroke-width='${GRATICULE_STROKE_WIDTH}' stroke-linecap='round' />",
                    "SRS": "+init=epsg:4326",
                    "Type": "csv",
                    "File": "${graticule_short_file_name}",
                    "Layer": ""
                },
                {
                    "Style": "<TextSymbolizer fontset-name='fontset-0' size='${GRATICULE_TEXT_FONT_SIZE}' fill='black' horizontal-alignment='right' dx='5' dy='10' placement='vertex'>[name]</TextSymbolizer>",
                    "SRS": "+init=epsg:4326",
                    "Type": "csv",
                    "File": "${graticule_short_file_name}",
                    "Layer": ""
                }"""
            } else {
                graticule_file_name = ""
            }
        }
        
        String airfields_lines = ""
        String airfields_file_name = ""
        if (printOptions.printAirfields) {
            airfields_file_name = Defs.FCSAVE_FILE_GEODATA_AIRFIELDS
            String airfields_short_file_name = airfields_file_name.substring(airfields_file_name.lastIndexOf('/')+1)
            airfields_lines = """,{
                "Style": "<MarkersSymbolizer file='airfield.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${airfields_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${AIRFIELD_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dy='10' placement='point'>[name]</TextSymbolizer>",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${airfields_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String churches_lines = ""
        String churches_file_name = ""
        if (printOptions.printChurches) {
            churches_file_name = Defs.FCSAVE_FILE_GEODATA_CHURCHES
            String churches_short_file_name = churches_file_name.substring(churches_file_name.lastIndexOf('/')+1)
            churches_lines = """,{
                "Style": "<MarkersSymbolizer file='church.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${churches_short_file_name}",
                "Layer": ""
            }"""
        }

        String castles_lines = ""
        String castles_file_name = ""
        if (printOptions.printCastles) {
            castles_file_name = Defs.FCSAVE_FILE_GEODATA_CASTLES
            String castles_short_file_name = castles_file_name.substring(castles_file_name.lastIndexOf('/')+1)
            castles_lines = """,{
                "Style": "<MarkersSymbolizer file='castle.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${castles_short_file_name}",
                "Layer": ""
            }"""
        }

        String chateaus_lines = ""
        String chateaus_file_name = ""
        if (printOptions.printChateaus) {
            chateaus_file_name = Defs.FCSAVE_FILE_GEODATA_CHATEAUS
            String chateaus_short_file_name = chateaus_file_name.substring(chateaus_file_name.lastIndexOf('/')+1)
            chateaus_lines = """,{
                "Style": "<MarkersSymbolizer file='chateau.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${chateaus_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String windpowerstations_lines = ""
        String windpowerstations_file_name = ""
        if (printOptions.printWindpowerstations) {
            windpowerstations_file_name = Defs.FCSAVE_FILE_GEODATA_WINDPOWERSTATIONS
            String windpowerstations_short_file_name = windpowerstations_file_name.substring(windpowerstations_file_name.lastIndexOf('/')+1)
            windpowerstations_lines = """,{
                "Style": "<MarkersSymbolizer file='windpowerstation.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${windpowerstations_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String peaks_lines = ""
        String peaks_file_name = ""
        if (printOptions.printPeaks) {
            peaks_file_name = Defs.FCSAVE_FILE_GEODATA_PEAKS
            String peaks_short_file_name = peaks_file_name.substring(peaks_file_name.lastIndexOf('/')+1)
            peaks_lines = """,{
                "Style": "<MarkersSymbolizer file='peak.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${peaks_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${PEAKS_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dy='10' placement='point'>[altitude]</TextSymbolizer>",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${peaks_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String additionals_lines = ""
        String additionals_file_name = ""
        if (printOptions.printAdditionals) {
            additionals_file_name = Defs.FCSAVE_FILE_GEODATA_ADDITIONALS
            String additionals_short_file_name = additionals_file_name.substring(additionals_file_name.lastIndexOf('/')+1)
            additionals_lines = """,{
                "Style": "<MarkersSymbolizer file='[symbol]' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${additionals_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${AIRFIELD_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dy='10' placement='point'>[name]</TextSymbolizer>",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${additionals_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String specials_lines = ""
        String specials_file_name = ""
        if (printOptions.printSpecials) {
            specials_file_name = Defs.FCSAVE_FILE_GEODATA_SPECIALS
            String specials_short_file_name = specials_file_name.substring(specials_file_name.lastIndexOf('/')+1)
            specials_lines = """,{
                "Style": "<MarkersSymbolizer file='special.png' transform='${GEODATA_BUILDING_SCALE}' placement='point' />",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${specials_short_file_name}",
                "Layer": ""
            }
            ,{
                "Style": "<TextSymbolizer fontset-name='fontset-0' size='${SPECIALS_TEXT_FONT_SIZE}' fill='black' allow-overlap='true' dx='[dx]' dy='[dy]' placement='point'>[name]</TextSymbolizer>",
                "SRS": "+init=epsg:4326",
                "Type": "csv",
                "File": "${specials_short_file_name}",
                "Layer": ""
            }"""
        }
        
        String airspaces_lines = ""
        String airspaces_file_name = ""
        if (printOptions.printAirspaces && printParams.contestMapAirspacesLayer) {
            airspaces_file_name = Defs.FCSAVE_FILE_GEODATA_AIRSPACES
            String airspaces_short_file_name = airspaces_file_name.substring(airspaces_file_name.lastIndexOf('/')+1)
            for (String airspaces_layer in printParams.contestMapAirspacesLayer.split(",")) {
                airspaces_lines += """,{
                    "Style": "<PolygonSymbolizer fill-opacity='0.2' fill='steelblue' />",
                    "SRS": "+init=epsg:4326",
                    "Type": "ogr",
                    "File": "${airspaces_short_file_name}",
                    "Layer": "${airspaces_layer.trim()}"
                }"""
            }
        }
        
        printstart "print_osm Scale=1:${SCALE} Width=${print_width} Height=${print_height}"
        BigDecimal INCH_2_MM = 25.4
        BigDecimal PX_PER_MM = DPI / INCH_2_MM
        BigDecimal PXX = 6.75
        int print_width_px = (print_width * PX_PER_MM / FACTOR).toInteger()
        int print_height_px = (print_height * PX_PER_MM / FACTOR).toInteger()
        int print_width_pxx = (print_width_px / PXX).toInteger()
        int print_height_pxx = (print_height_px / PXX).toInteger()
        println "PX_PER_MM: $PX_PER_MM"
        println "print_width: $print_width mm, $print_width_px px, $print_width_pxx pxx"
        println "print_height: $print_height mm, $print_height_px px, $print_height_pxx pxx"
        

        //...........................................................................................
        printstart "Get service capabilities"
        Map status = CallPrintServer("/capabilities/service", [HEADER_ACCEPT], "GET", DataType.JSON, "")
        printdone ""
    
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
                    "Scale": ${SCALE},
                    "PrintWidth": ${print_width},
                    "PrintHeight": ${print_height},
                    "Latitude": ${printOptions.centerLatitude},
                    "Longitude": ${printOptions.centerLongitude},
                    "Style": "${style}",
                    "Projection": "${ATTR_PROJECTION}",
                    "HideLayers": "${hide_layers}",
                    "UserObjects": [
                        ${user_text}
                        ${gpx_lines}
                        ${graticule_lines}
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
            FileUpload("/upload/${printjob_id}", printParams.webRootDir + "images/map/airfield.png")
            printdone ""
            printstart "Upload church.png"
            FileUpload("/upload/${printjob_id}", printParams.webRootDir + "images/map/church.png")
            printdone ""
            printstart "Upload castle.png"
            FileUpload("/upload/${printjob_id}", printParams.webRootDir + "images/map/castle.png")
            printdone ""
            printstart "Upload chateau.png"
            FileUpload("/upload/${printjob_id}", printParams.webRootDir + "images/map/chateau.png")
            printdone ""
            printstart "Upload windpowerstation.png"
            FileUpload("/upload/${printjob_id}", printParams.webRootDir + "images/map/windpowerstation.png")
            printdone ""
            printstart "Upload peak.png"
            FileUpload("/upload/${printjob_id}", printParams.webRootDir + "images/map/peak.png")
            printdone ""
            printstart "Upload special.png"
            FileUpload("/upload/${printjob_id}", printParams.webRootDir + "images/map/special.png")
            printdone ""
            if (printOptions.printEnroutePhotos) {
                printstart "Upload fcphoto.png"
                FileUpload("/upload/${printjob_id}", printParams.webRootDir + "GM_Utils/Icons/fcphoto.png")
                printdone ""
            }
            if (printOptions.printEnrouteCanvas) {
                EnrouteCanvasSign.each { enroute_canvas_sign ->
                    if (enroute_canvas_sign.imageName) {
                        printstart "Upload ${enroute_canvas_sign.imageName}"
                        FileUpload("/upload/${printjob_id}", printParams.webRootDir + enroute_canvas_sign.imageName)
                        printdone ""
                    }
                }
            }
            if (graticule_file_name) {
                printstart "Upload graticule"
                FileUpload("/upload/${printjob_id}", graticule_file_name)
                printdone ""
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
            printjob_writer << printParams.routeId
            printjob_writer.close()
            printdone ""
            */
            
            // save printjob_id
            printstart "Generate ${printjobid_filename}"
            File printjobid_file = new File(printjobid_filename)
            BufferedWriter printjobid_writer = printjobid_file.newWriter()
            printjobid_writer << printjob_id
            printjobid_writer << "\n"
            printjobid_writer << printParams.pngFileName
            printjobid_writer << "\n"
            printjobid_writer << printOptions.printLandscape
            printjobid_writer << "\n"
            printjobid_writer << printOptions.printA3
            printjobid_writer << "\n"
            printjobid_writer << printOptions.printColorChanges
            printjobid_writer.close()
            printdone ""
            
            // save print file id
            printstart "Generate ${printfileid_filename}"
            File printfileid_file = new File(printfileid_filename)
            BufferedWriter printfileid_writer = printfileid_file.newWriter()
            printfileid_writer << printParams.pngFileName
            printfileid_writer << "\n"
            printfileid_writer << printOptions.printLandscape
            printfileid_writer << "\n"
            printfileid_writer << printOptions.printA3
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
                 (Defs.OSMPRINTMAP_PNGFILENAME):printParams.pngFileName,
                 (Defs.OSMPRINTMAP_PRINTLANDSCAPE):printOptions.printLandscape,
                 (Defs.OSMPRINTMAP_PRINTCOLORCHANGES):printOptions.printColorChanges
                ]
            )
            printdone ""
            
            ret.ok = true
        }
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    void BackgroundJob(String actionName, String jobFileName, String jobId, String jobIdFileName, String pngFileName, boolean printLandscape, boolean printColorChanges)
    {
        printstart "BackgroundJob ${actionName} ${jobId}"
        
        switch (actionName) {
            case Defs.OSMPRINTMAP_ACTION_CHECKJOB:
                boolean job_successfully = false
                int img_width = 0
                int img_height = 0
                
                //...........................................................................................
                printstart "Check job status"
                Map status = CallPrintServer("/mapstate/${jobId}", [HEADER_ACCEPT], "GET", DataType.JSON, "")
                if (status.responseCode == 200) {
                    if (status.json.Data.Attributes.MapBuildSuccessful) {
                        img_width = status.json.Data.Attributes.MapBuildBoxPixel.Width
                        img_height = status.json.Data.Attributes.MapBuildBoxPixel.Height
                        String s = status.json.Data.Attributes.MapBuildMessage
                        println "Successfully. Status='${status.json.Data.Attributes.MapBuildSuccessful}', Width=${img_width}, Height=${img_height}"
                        job_successfully = status.json.Data.Attributes.MapBuildSuccessful == "yes"
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
                        
                        printstart "Write ${download_zip_file_name}"
                        FileOutputStream zip_stream = new FileOutputStream(download_zip_file_name)
                        zip_stream << status.binary
                        zip_stream.flush()
                        zip_stream.close()
                        printdone ""
                        
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
                        
                        printstart "Delete ${download_zip_file_name}"
                        File download_zip_file = new File(download_zip_file_name)
                        if (download_zip_file.delete()) {
                            printdone ""
                        } else {
                            printerror ""
                        }
                        
                        printstart "Downscale ${unpacked_png_file_name} -> ${pngFileName}"
                        downscaleImage(unpacked_png_file_name, pngFileName, (img_width/FACTOR2).toInteger(), (img_height/FACTOR2).toInteger(), printLandscape, printColorChanges)
                        printdone ""
                        
                        printstart "Delete ${unpacked_png_file_name}"
                        File unpacked_png_file = new File(unpacked_png_file_name)
                        if (unpacked_png_file.delete()) {
                            printdone ""
                        } else {
                            printerror ""
                        }
                        
                        printstart "Delete ${jobIdFileName}"
                        File printjobid_file = new File(jobIdFileName)
                        if (printjobid_file.delete()) {
                            printdone ""
                        } else {
                            printerror ""
                        }
                    }
                    printdone "responseCode=${status.responseCode}, ${if(status.binary)'Data available.'else'No data.'}"
                }
                
                //...........................................................................................
                if (jobId) {
                    printstart "View job"
                    status = CallPrintServer("/metadata/${jobId}", [HEADER_CONTENTTYPE], "GET", DataType.JSON, null)
                    printdone "responseCode=${status.responseCode}"
                }
                
                //...........................................................................................
                if (jobId) {
                    printstart "Remove job"
                    status = CallPrintServer("/${jobId}", [HEADER_ACCEPT], "DELETE", DataType.NONE, null)
                    printdone "responseCode=${status.responseCode}"
                }
            
                //...........................................................................................
                printstart "Stop OsmPrintMapJob"
                quartzScheduler.unscheduleJobs(quartzScheduler.getTriggersOfJob(new JobKey("OsmPrintMapJob",Defs.OSMPRINTMAP_GROUP))*.key)
                gpxService.DeleteFile(jobFileName)
                printdone ""
                
                break
        }
        
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    private boolean create_graticule_csv(String graticuleFileName,
                                         BigDecimal centerGraticuleLatitude, BigDecimal centerGraticuleLongitude,
                                         BigDecimal centerLatitude, BigDecimal centerLongitude,
                                         int printWidth, int printHeight
                                        )
    {
        printstart "Write ${graticuleFileName}"
        println "centerGraticuleLatitude: ${centerGraticuleLatitude}"
        println "centerGraticuleLongitude: ${centerGraticuleLongitude}"
        
        BigDecimal print_width_nm = SCALE * printWidth / Contest.mmPerNM
        BigDecimal print_height_nm = SCALE * printHeight / Contest.mmPerNM
        Map rect_width = AviationMath.getShowPoint(centerLatitude, centerLongitude, print_width_nm / 2)
        Map rect_height = AviationMath.getShowPoint(centerLatitude, centerLongitude, print_height_nm / 2)
        println "Width:  ${printWidth} mm, ${print_width_nm} NM, ${rect_width}"
        println "Height: ${printHeight} mm, ${print_height_nm} NM, ${rect_height}"
        BigDecimal min_lat = rect_height.latmin
        BigDecimal max_lat = rect_height.latmax
        BigDecimal min_lon = rect_width.lonmin
        BigDecimal max_lon = rect_width.lonmax
        println "min_lat=${min_lat} max_lat=${max_lat} min_lon=${min_lon} max_lon=${max_lon}"
        
        File graticule_file = new File(graticuleFileName)
        Writer graticule_writer = graticule_file.newWriter("UTF-8",false)
        
        graticule_writer << "id${CSV_DELIMITER}name${CSV_DELIMITER}wkt"
        
        int line_id = 1
        int name_id = 1
        CoordPresentation coord_presentation = CoordPresentation.DEGREEMINUTE
        
        // vertical line
        BigDecimal start_lon = centerGraticuleLongitude
        while (start_lon > min_lon) {
            start_lon -= 10/60 // 10'
        }
        BigDecimal lon = start_lon
        while (lon < max_lon) {
            lon += 10/60 // 10'
            String wkt = "LINESTRING(${lon} ${min_lat}, ${lon} ${max_lat})"
            String name = coord_presentation.GetMapName(lon, false)
            graticule_writer << """${CSV_LINESEPARATOR}${line_id}${CSV_DELIMITER}"${name}"${CSV_DELIMITER}${wkt}"""
            line_id++
            name_id++
        }
        
        // horizontal line
        BigDecimal start_lat = centerGraticuleLatitude
        while (start_lat > min_lat) {
            start_lat -= 10/60 // 10'
        }
        BigDecimal lat = start_lat
        name_id = 1
        while (lat < max_lat) {
            lat += 10/60 // 10'
            String wkt = "LINESTRING(${min_lon} ${lat}, ${max_lon} ${lat})"
            String name = coord_presentation.GetMapName(lat, true)
            graticule_writer << """${CSV_LINESEPARATOR}${line_id}${CSV_DELIMITER}"${name}"${CSV_DELIMITER}${wkt}"""
            line_id++
            name_id++
        }

        graticule_writer.close()
        
        printdone ""
        return true
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
