import java.math.BigDecimal;
import java.util.List

import org.springframework.web.context.request.RequestContextHolder

class GeoDataService
{
    def logService
    def messageSource
    
    // Geo files
    final static String CSV_EXTENSION = ".csv"
    final static String DATA_EXTENSIONS = "${GeoDataService.CSV_EXTENSION}"
    
    final static String CSV_DELIMITER = ";"
    final static Map CSV_FIELDS = [DATUM:'DATUM', OBA:'OBA', OBA_WERT:'OBA_WERT', NAME:'NAME', BOX_GEO:'BOX_GEO', HOEHE:'HOEHE']
    final static Map CSV_FILTER = [Churches:[[OBA_WERT:'Kirche'],[OBA_WERT:'Kathedrale']], Castles:[[OBA_WERT:'Burg, Festung']], Chateaus:[[OBA_WERT:'Schloss']], Airfields:[[OBA_WERT:'Internationaler Flughafen'],[OBA_WERT:'Regionalflughafen'],[OBA_WERT:'Segelfluggelände'],[OBA_WERT:'Sonderflughafen'],[OBA_WERT:'Sonderlandeplatz'],[OBA_WERT:'Verkehrslandeplatz']], Windpowerstations:[[OBA_WERT:'Windrad']], Peaks:[[OBA:'Besonderer_Hoehenpunkt']]]
    final static List CSV_LOWERCASE_FIELDS = []

    
    //--------------------------------------------------------------------------
    Map LoadGeoDataFile(String fileExtension, String originalFileName, String geoDataFileName)
    // Return: ok  - true: Daten erfolgreich geladen
    {
        switch (fileExtension) {
            case CSV_EXTENSION:
                return ReadCSVFile(originalFileName, geoDataFileName)
        }
        return [ok: false, msg: getMsg("fc.contestmap.importgeodata.nodatafile", [originalFileName], false)]
    }
    
    //--------------------------------------------------------------------------
    private Map ReadCSVFile(String originalFileName, String geoDataFileName)
    // Return: ok  - true: Daten erfolgreich geladen
    {
        Map geo_data = CsvTools.LoadData(geoDataFileName, CSV_DELIMITER, CSV_FIELDS, CSV_FILTER, CSV_LOWERCASE_FIELDS)
        Map geo_info = [newestDate:'']
        boolean ok = true  // SaveCSVFile(Defs.FCSAVE_FILE_GEODATA_AIRFIELDS, geo_data.Airfields, false, geo_info)
        ok &= SaveCSVFile(Defs.FCSAVE_FILE_GEODATA_CHURCHES, geo_data.Churches, false, geo_info)
        ok &= SaveCSVFile(Defs.FCSAVE_FILE_GEODATA_CASTLES, geo_data.Castles, false, geo_info)
        ok &= SaveCSVFile(Defs.FCSAVE_FILE_GEODATA_CHATEAUS, geo_data.Chateaus, false, geo_info)
        ok &= SaveCSVFile(Defs.FCSAVE_FILE_GEODATA_WINDPOWERSTATIONS, geo_data.Windpowerstations, false, geo_info)
        ok &= SaveCSVFile(Defs.FCSAVE_FILE_GEODATA_PEAKS, geo_data.Peaks, true, geo_info)
        ok &= SaveTxtFile(Defs.FCSAVE_FILE_GEODATA_DATE, geo_info)
        if (!ok) {
            return [ok: false, msg: getMsg("fc.contestmap.importgeodata.notimported", [originalFileName], false)]
        }
        return [ok: true, msg: getMsg("fc.contestmap.importgeodata.imported", [originalFileName], false)]
    }

    //--------------------------------------------------------------------------
    private boolean SaveCSVFile(String geoFileName, List geoData, boolean wrPeak, Map geoInfo)
    {
        printstart "Write ${geoFileName} with ${geoData.size()} geo data. $geoData"
        
        File geo_file = new File(geoFileName)
        Writer geo_writer = geo_file.newWriter("UTF-8",false)
        
        geo_writer << "id${OsmPrintMapService.CSV_DELIMITER}datum${OsmPrintMapService.CSV_DELIMITER}name${OsmPrintMapService.CSV_DELIMITER}wkt"
        if (wrPeak) {
            geo_writer << "${OsmPrintMapService.CSV_DELIMITER}altitude"
        }
        
        int line_id = 1
        
        for (Map geo_datum in geoData) {
            String wkt = geo_datum.BOX_GEO
            String altitude = ""
            if (geo_datum.HOEHE.isInteger()) {
                altitude = (geo_datum.HOEHE.toInteger() * GpxService.ftPerMeter).toInteger().toString()
            }
            if (geo_datum.DATUM) {
                String[] datum = geo_datum.DATUM.split("\\x2e")
                if (datum.size() == 3) {
                    String datum2 = "${datum[2]}-${datum[1]}-${datum[0]}" 
                    if (datum2 > geoInfo.newestDate) {
                        geoInfo.newestDate = datum2
                    }
                }
            }
            geo_writer << """${OsmPrintMapService.CSV_LINESEPARATOR}${line_id}${OsmPrintMapService.CSV_DELIMITER}${geo_datum.DATUM}${OsmPrintMapService.CSV_DELIMITER}"${geo_datum.NAME}"${OsmPrintMapService.CSV_DELIMITER}${wkt}"""
            if (wrPeak) {
                geo_writer << "${OsmPrintMapService.CSV_DELIMITER}${altitude} ft"
            }
            line_id++
        }
        
        geo_writer.close()
        
        printdone ""
        return true
    }
    
    //--------------------------------------------------------------------------
    private boolean SaveTxtFile(String geoFileName, Map geoInfo)
    {
        printstart "Write ${geoFileName} with ${geoInfo}"
        
        File geo_file = new File(geoFileName)
        Writer geo_writer = geo_file.newWriter("UTF-8",false)
        
        geo_writer << geoInfo.newestDate
        geo_writer.close()
        
        printdone ""
        return true
    }
    
    //--------------------------------------------------------------------------
    static String ReadTxtFile(String geoFileName)
    {
        String ret = ""
        File file = new File(geoFileName)
        if (file.exists()) {
            file.eachLine {
                if (!ret) {
                    ret = it
                }
            }
        }
        return ret
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
