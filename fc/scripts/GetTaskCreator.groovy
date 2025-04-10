ScriptLogo    = "GetTaskCreator"
ScriptVersion = "1.0.0"
// Thomas Weise 
// 19.04.2024

// Read task creator files

import groovy.time.*

// ----------------------------------------------------------------------------------
// your script constants

RUN_GET_HTML = true
RUN_GET_FILES = true
CHECK_LOCALE_DE = true

TASK_CREATOR_URL = "https://www.airrats.cl/taskcreator"

MODIFY_DATA = [
    [Old:"""key=AIzaSyDFCicW-Cz5hVwe3sBG5qyWdyzBfWiRIY0""", ReplaceWith:"""key=AIzaSyAzxxtg-0BdE8Xi-QvDCx5G1JnTyDC8d-w"""],
    [JSExtern:true, Old:"""<script src="js/airrats.min.js"></script>""", New:"""		<script src="${TASK_CREATOR_URL}/js/airrats.min.js"></script>"""],
    [Old:"""<!-- Global site tag (gtag.js) - Google Analytics -->""", RemoveTo:"""<!-- end google analytics -->"""],
    [Old:"""<body>""", New:"""	<body onmousemove="get_taskname()" onload="clear_addons()">"""],
    [Old:"""<div id="left_panel">""",
     New:"""		<div id="left_panel">
			<div style="display:block;">
                <span id="titulo1" style="font-size: 15px;">Carlos Rocca's</span>
			</div>"""
    ],
    [Old:"""<div id="separador_carta" class="separador"><hr></div>""",
     New:"""			<div style="display:block;">
                <span id="titulo1" style="font-size: 12px;">{FCGUI}</span>
			</div>
			<div id="separador_load" class="separador"><hr></div>
			<div id="seccion_load">
                <label for="task_name" style="float: left; margin: 4px 5px 0 0;">{TASKNAME}:</label>
                <div style="display: inline-block; vertical-align: bottom;">
                    <input type="text" id="task_name1" class="task_name" style="width:200px" onchange="set_taskname()" >
                </div>
                <div style="display: none;">
                    <input type="text" id="task_name" class="task_name">
                </div>
                <div style="margin-top:4px;">
                    <button id="save_as_csv" title="{SAVETOOLTIP}">{SAVE}</button>
                    <button id="btn_delete_task" title="{CLEARTOOLTIP}" onclick="clear_taskname()" style="margin-left:10px;">{CLEAR}</button>
                    <button id="btn_csv" title="{LOADTOOLTIP}">{LOAD}</button>
                    <button title="{SYNCMAPURLTOOLTIP}" onclick="sync_map_url()" style="margin-right:5px;">{SYNCMAPURL}</button>
                    <input type="file" name="File Upload" id="load_csv" accept=".csv" style="display:none;" />
                    <button id="btn_save_as_fc" style="margin-left:5px;" title="{EXPORTFCKMLTOOLTIP}">{EXPORTFCKML}</button>
                </div>
                <script>
                    function set_taskname() {
                        var v = \$("#task_name1").val();
                        \$('#task_name').val(v);
                    }
                    function get_taskname() {
                        var v = \$("#task_name").val();
                        if (v) {
                            \$('#task_name1').val(v);
                        }
                    }
                    function clear_taskname() {
                        \$('#task_name1').val('');
                    }
                    function clear_addons() {
                        \$('#btn_delete_suas').trigger("click");
                        \$('#btn_delete_map_addons').trigger("click");
                    }
                    function get_map_params(params, searchParam) {
                        params = params.substr(1);
                        var params_array = params.split('&');
                        for (let i in params_array) {
                            if (params_array[i].startsWith(searchParam)) {
                                var data = params_array[i].substr(searchParam.length+1);
                                var data_array = data.split(',');
                                return data_array;
                            }
                        }
                        return null;
                    }
                    function sync_map_url() {
                        if (\$("#map_url").val()) {
                            var map_data = get_map_params(window.location.search, 'baseurl');
                            if (map_data) {
                                var new_map_url = map_data[0].replaceAll('%22','') + \$("#map_url").val().substr(\$("#map_url").val().lastIndexOf('/')+1)
                                \$("#map_url").val(new_map_url)
                            }
                        }
                    }
                </script>
            </div>
            <div id="separador_carta" class="separador"><hr></div>"""
    ],
    [Old:"""<label for="map_name" """, RemoveTo:"""<div id="separador_mapa_usuario" """],
    [Old:"""<label for="task_name" """, RemoveTo:"""<button id="btn_save_as_fc" """],
    [Old:"""<label for="chk_private" """, RemoveTo:"""<span><input id="chk_private" """],
]

TRANSLATIONS = [
    [key:"{FCGUI}", en:"Flight Contest User Interface", de:"Flight Contest Benutzeroberfläche", es:"Flight Contest interfaz de usuario"],
    [key:"{TASKNAME}", en:"Task name", de:"Task-Name", es:"Nombre de la prueba"],
    [key:"{LOAD}", en:"Load", de:"Laden", es:"Cargar"],
    [key:"{LOADTOOLTIP}", en:"Load task from CSV file.", de:"Task von einer CSV-Datei laden.", es:"Cargar tarea desde un archivo CSV."],
    [key:"{SYNCMAPURL}", en:"Sync map url", de:"Sync Karten-Url", es:"Sincronizar Url Carta"],
    [key:"{SYNCMAPURLTOOLTIP}", en:"Synchronize map url with the actual contest.", de:"Karten-Url mit dem aktuellen Wettbewerb synchronisieren.", es:"Sincronizar la url del mapa con la competición actual."],
    [key:"{SAVE}", en:"Save", de:"Speichern", es:"Guardar"],
    [key:"{SAVETOOLTIP}", en:"Save task to CSV file.", de:"Task in eine CSV-Datei speichern.", es:"Guardar la tarea en un archivo CSV."],
    [key:"{CLEAR}", en:"Clear", de:"Löschen", es:"Borrar"],
    [key:"{CLEARTOOLTIP}", en:"Clear task", de:"Task löschen.", es:"Borrar tarea."],
    [key:"{EXPORTFCKML}", en:"Export FC kml", de:"Export FC kml", es:"Exportar FC kml"],
    [key:"{EXPORTFCKMLTOOLTIP}", en:"Save FC route file (kml). Import with FC route import.", de:"FC-Strechen-Datei (kml) speichern. Mit FC-Strecken-Import importieren.", es:"Guardar archivo de ruta FC (kml). Importar con la importación de rutas FC."],
    //[key:"{}", en:"", de:"", es:""],
]

LOG_TIME_FORMAT = "HH:mm:ss"

// ----------------------------------------------------------------------------------
// common script constants

LogOn               = true   // True - Log aufzeichnen (Logs werden je Scriptaufruf oben eingef�gt, Logname: <ScriptLogo>.log)
LogFileNameWithDate = false   // True - Unterschiedliche Logfiles je Scriptaufruf, False - Ein Logfile
LogDir              = "scripts"     // Leer - Log im aktuellen Verzeichnis schreiben, Sonst - Log in angegebenen Verzeichnis schreiben
LogStdOutput        = true   // True - Log in Console ausgeben 
LogMaxLines         = 10   // Maximalanzahl der Log-Zeilen über mehrere Script-Aufrufe hinweg

// ----------------------------------------------------------------------------------
// your global variables

// ----------------------------------------------------------------------------------
void run_main() 
{
	log.println "Run at host $hostname"
	log.println "Write temporary log to '$lognewfilename'"
	log.println ""

    if (RUN_GET_HTML) {
        get_taskcreator_html("en", "web-app/taskcreator")
        get_taskcreator_html("de", "web-app/taskcreator")
        get_taskcreator_html("es", "web-app/taskcreator")
    }
    if (RUN_GET_FILES) {
        get_file("locale/en.json", "web-app/taskcreator")
        get_file("locale/de.json", "web-app/taskcreator")
        get_file("js/airrats.min.js", "web-app/taskcreator")
        get_file("js/jquery-3.6.0.min.js", "web-app/taskcreator")
        get_file("js/jquery-ui.min.js", "web-app/taskcreator")
        get_file("js/jquery.ajax-combobox/btn.png", "web-app/taskcreator")
        get_file("js/jquery.ajax-combobox/js/jquery.ajax-combobox.js", "web-app/taskcreator")
        get_file("js/jquery.ajax-combobox/css/jquery.ajax-combobox.css", "web-app/taskcreator")
        get_file("images/scale_200K_272,355mm_en.png", "web-app/taskcreator")
        get_file("images/icon_blue_dot.png", "web-app/taskcreator")
        get_file("css/airrats.css", "web-app/taskcreator")
    }
    if (CHECK_LOCALE_DE) {
        check_locale("locale/en.json", "locale/de.json", "locale/de_new.json", "web-app/taskcreator")
    }
    
	end_time = new Date()
	TimeDuration duration = TimeCategory.minus( end_time, nowtime )
	log.println ""
	log.println "${nowtime.format(LOG_TIME_FORMAT)} - ${end_time.format(LOG_TIME_FORMAT)} (${duration.minutes}min ${duration.seconds}s)"
	
}

// ----------------------------------------------------------------------------------
void get_taskcreator_html(String lang, String destDir)
{
    String url = TASK_CREATOR_URL + "?lang=${lang}"
    def connection = url.toURL().openConnection()
    String data = connection.content.text
    //data = new String(data.getBytes("ISO-8859-1"), "UTF-8")
    
    String std_filename = "${destDir}/run_${lang}.html"
    String jsextern_filename = "${destDir}/run_jsextern_${lang}.html"
    String org_filename = "${destDir}/org_${lang}.html"
    
    File std_file = new File(std_filename)
    std_file.delete()
    
    File jsextern_file = new File(jsextern_filename)
    jsextern_file.delete()
    
    File org_file = new File(org_filename)
    org_file.delete()
    
    List std_replace_lines = []
    List jsextern_replace_lines = []
    int last_replaced_pos = 0
    int start_remove_to = 0
    data.eachLine { line, number ->
        org_file << line
        org_file << "\r\n"
        
        String std_line = line
        String jsextern_line = line
        int replace_pos = 0
        for (Map modify_date in MODIFY_DATA) {
            replace_pos++
            if (last_replaced_pos < replace_pos) {
                if (start_remove_to) {
                    std_line = ""
                    jsextern_line = ""
                    if (line.contains(modify_date.RemoveTo)) {
                        std_replace_lines += "r${start_remove_to}-${number + 1}"
                        last_replaced_pos = replace_pos
                        start_remove_to = 0
                    }
                }
                if (line.contains(modify_date.Old)) {
                    if (modify_date.ReplaceWith) {
                        std_line = line.replace(modify_date.Old, modify_date.ReplaceWith)
                        last_replaced_pos = replace_pos
                    } else if (!modify_date.RemoveTo) {
                        if (!modify_date.JSExtern) {
                            std_line = modify_date.New
                            if (std_line) {
                                std_replace_lines += "m${number + 1}"
                            } else {
                                std_replace_lines += "r${number + 1}"
                            }
                            jsextern_line = modify_date.New
                            last_replaced_pos = replace_pos
                        } else {
                            jsextern_line = modify_date.New
                            if (jsextern_line) {
                                jsextern_replace_lines += "m${number + 1}"
                            } else {
                                jsextern_replace_lines += "r${number + 1}"
                            }
                            last_replaced_pos = replace_pos
                        }
                    } else {
                        std_line = ""
                        jsextern_line = ""
                        start_remove_to = number + 1
                    }
                }
                break
            }
        }
        if (std_line) {
            std_file << translate(lang, std_line)
            std_file << "\r\n"
        }
        if (jsextern_line) {
            jsextern_file << translate(lang, jsextern_line)
            jsextern_file << "\r\n"
        }
    }
    
    log.println "org_${lang}.html: ${MODIFY_DATA.size()} instructions. ${last_replaced_pos} modified/removed: Std ${std_replace_lines}, JsExtern ${jsextern_replace_lines}"
}

// ----------------------------------------------------------------------------------
String translate(String lang, String str)
{
    for (Map translation in TRANSLATIONS) {
        if (str.contains(translation.key)) {
            str = str.replace(translation.key, translation[(lang)])
        }
    }
    return str
}

// ----------------------------------------------------------------------------------
void get_file(String fileName, String destDir)
{
    if (fileName) {
        log.print fileName
        
        String dest_filename = "${destDir}/${fileName}"
        
        String url = TASK_CREATOR_URL + "/" + fileName
        def connection = url.toURL().openConnection()
        
        if (connection) {
            File dest_file = new File(dest_filename)
            if (dest_file.exists()) {
                dest_file.delete()
            }
            
            if (connection.contentLength > 0) {
                def data = connection.getInputStream().getBytes()
                dest_file << data
                log.println " Done (${data.size()} bytes)."
            } else {
                log.println " Error: Not found."
            }
        } else {
            log.println " Error: No connection."
        }
    }
}

// ----------------------------------------------------------------------------------
void check_locale(String srcFileName, String destFileName, String newFileName, String destDir)
{
    log.print destFileName
    
    int src_string_num = 0
    int new_string_num = 0
    
    String new_filename = "${destDir}/${newFileName}"
    File new_file = new File(new_filename)
    new_file.delete()
    BufferedWriter new_writer = new_file.newWriter("UTF-8")
    
    String dest_filename = "${destDir}/${destFileName}"
    File dest_file = new File(dest_filename)
    
    String src_filename = "${destDir}/${srcFileName}"
    File src_file = new File(src_filename)
    String src_line = ""
    src_file.withReader { src_reader ->
        while ((src_line = src_reader.readLine()) != null) {
            src_string_num++
            def src_line_array = src_line.split(':')
            boolean dest_line_found = false
            String dest_line = ""
            dest_file.withReader { dest_reader ->
                while ((dest_line = dest_reader.readLine()) != null) {
                    def dest_line_array = dest_line.split(':')
                    if (src_line_array[0].trim() == dest_line_array[0].trim()) {
                        new_writer.writeLine dest_line
                        dest_line_found = true
                        break
                    }
                }
            }
            
            if (!dest_line_found) {
                new_writer.writeLine "NEW " + src_line
                new_string_num++
            }
        }
    }    

    new_writer.close()
    
    log.println " Done (${src_string_num} strings, ${new_string_num} new strings)"
}


// =================================================================================================
// internal routines
// DO NOT MODIFY
// if modified add char at version number

// Thomas Weise
// Version 4.2.0
// 28.03.2024

// ----------------------------------------------------------------------------------
// internal constants

// log constants
NEWEXTENSION         = ".new"
LOGEXTENSION         = ".log"


// =================================================================================================
class Log
{
	/* Anwendung:
	log.printstart("printstart51")
	log.print("print1")
	log.println(" println1")
	log.printend("printend51")
	*/

	//--------------------------------------------------------------------------
	Log(boolean printSystem, File logFile = null, boolean logAppend = false)
	{
		this.printSystem = printSystem
		if (logFile) {
			logFileWriter = logFile.newWriter(logAppend)
		}
	}
	
	//--------------------------------------------------------------------------
	int MaxLogChars = 0            // Umbruch bei Ausgabezeichenanzahl�berschreitung, 0 - deaktiviert
	String LineSeparator = "\r\n"
	String SpaceFill = "  "
	String Points = "..."
	String NextLine = "_"
	String Trenner = " "
	
	//--------------------------------------------------------------------------
	private boolean printSystem
	private Writer logWriter = new StringWriter()
	private Writer logFileWriter

	private int startNum = 0
	private int newLineNum = 0
	private boolean openLine
	private int writtenChars = 0
	private int writtenLines = 0
	
	
	//--------------------------------------------------------------------------
	void printstart(out)
	{
		wrnext()
		wr(out)
		wr(Points)
		
		startNum++
	}
	
	//--------------------------------------------------------------------------
	void print(out)
	{
		wrnext()
		wr(out)
		
		openLine = true
	}
	
	//--------------------------------------------------------------------------
	void println(out)
	{
		wrnext()
		wrln(out)
		
		openLine = false
	}
	
	//--------------------------------------------------------------------------
	void printend(out)
	{
		if (MaxLogChars) {
			if (writtenChars + out.size() > MaxLogChars) {
				wrnext(false) // false - ohne Spaces
			}
		}
		if (openLine) {
			wrln("")
			openLine = false
		}
		if (newLineNum == startNum) {
			for (int i = 0; i < startNum-1; i++) {
				wr(SpaceFill)
			}
		} else {
			wr(Trenner)
		}
		wrln(out)
		
		startNum--
		if (newLineNum > startNum) {
			newLineNum = startNum
		}
	}
	
	//--------------------------------------------------------------------------
	void printreset()
	{
		if (openLine || startNum) {
			wrln("/")
		}
		startNum = 0
		newLineNum = 0
		openLine = false
		writtenChars = 0
	}
	
	//--------------------------------------------------------------------------
	private void wrnext(boolean wrSpaces = true)
	{
		if (newLineNum < startNum) {
			wrln(NextLine)
			newLineNum = startNum
		}
		
		if (wrSpaces) {
			for (int i = 0; i < startNum; i++) {
				wr(SpaceFill)
			}
		}
	}
	
	//--------------------------------------------------------------------------
	private void wr(out)
	{
		if (printSystem) {
			System.out.print(out)
		}
		logWriter << out
		if (logFileWriter) {
			logFileWriter << out
			logFileWriter.flush()
		}
		writtenChars += out.size()
	}

	//--------------------------------------------------------------------------
	private void wrln(out)
	{
		if (printSystem) {
			System.out.println(out)
		}
		logWriter << out
		logWriter << LineSeparator
		if (logFileWriter) {
			logFileWriter << out
			logFileWriter.flush()
			logFileWriter << LineSeparator
			logFileWriter.flush()
		}
		writtenLines++
		writtenChars = 0
	}
	
	//--------------------------------------------------------------------------
	String toString()
	{
		return logWriter.toString()
	}
	
	//--------------------------------------------------------------------------
	int getLines()
	{
		return writtenLines
	}
	
	//--------------------------------------------------------------------------
	void close() 
	{
		if (logFileWriter) {
			logFileWriter.close()
		}
	}

}

//=================================================================================================
class Logger
{
	//--------------------------------------------------------------------------
	Logger(String logfilename, String lognewfilename, boolean logon = true, boolean logstdoutput = true, int logmaxlines = 1000)
	{
		logFileName = logfilename
		logNewFileName = lognewfilename
		logOn = logon
		logStdOutput = logstdoutput
		logMaxLines = logmaxlines
	}
	
	//--------------------------------------------------------------------------
	Map Params
	
	//--------------------------------------------------------------------------
	private Log log
	private String logFileName
	private String logNewFileName
	private boolean logOn
	private boolean logStdOutput
	private int logMaxLines
	private File newLogFile
	
	//--------------------------------------------------------------------------
	void open()
	{
		if (logOn) {
			newLogFile = new File(logNewFileName)
			boolean logfileexists = newLogFile.exists()
			log = new Log(logStdOutput,newLogFile,logfileexists)
			if (logfileexists) {
				Params?.RestartWriteStr.each {
					log.println(it)
				}
			}
			Params?.OpenWriteStr.each {
				log.println(it)
			}
		}
	}
	
	//--------------------------------------------------------------------------
	void close()
	{
		if (logOn) {
			Params?.CloseWriteStr.each {
				log.println(it)
			}

			File logfile = new File(logFileName)
			if (logfile.exists()) {
				boolean cut = false
				logfile.eachLine {
					if (!cut) {
						if (log.lines >= logMaxLines) {
							Params?.CutWriteStr.each {
								log.println(it)
							}
							cut = true
							return
						}
						log.println(it)
					}
				}
				logfile.delete()
			}
			log.close()
			
			newLogFile.renameTo(logfile)
		}
	}

	//--------------------------------------------------------------------------
	void printstart(out)
	{
		if (logOn) {
			log.printstart(out)
		}
	}
	
	//--------------------------------------------------------------------------
	void print(out)
	{
		if (logOn) {
			log.print(out)
		}
	}
	
	//--------------------------------------------------------------------------
	void println(out)
	{
		if (logOn) {
			log.println(out)
		}
	}
	
	//--------------------------------------------------------------------------
	void printend(out)
	{
		if (logOn) {
			log.printend(out)
		}
	}
	
	//--------------------------------------------------------------------------
	void printreset()
	{
	}
}

// =================================================================================================
// main program and global variables

// global variables
hostname = System.getenv().COMPUTERNAME
nowtime = new Date()
nowtimefile = nowtime.format("yyyyMMdd-HHmmss")
nowtimelog = nowtime.format("dd.MM.yyyy HH:mm:ss")
if (!LogDir) {
    LogDir = System.getProperty("user.dir") + System.getProperty("file.separator")
}
if (!LogDir.endsWith(System.getProperty("file.separator"))) {
    LogDir += System.getProperty("file.separator")
}
if (LogFileNameWithDate) {
	logfilename = "${LogDir}${ScriptLogo}-${nowtimefile}${LOGEXTENSION}"
} else {
	logfilename = "${LogDir}${ScriptLogo}${LOGEXTENSION}"
}
lognewfilename = "${LogDir}${ScriptLogo}${LOGEXTENSION}${NEWEXTENSION}"

// open logfile
log = new Logger(logfilename, lognewfilename, LogOn, LogStdOutput, LogMaxLines)
log.Params = [OpenWriteStr:["${ScriptLogo} ${ScriptVersion}","${nowtimefile} (${nowtimelog})",""],
              RestartWriteStr:["","------------------------------------------------------------------------------","Restart"],
              CloseWriteStr:["","=========================================================================================================",""],
              CutWriteStr:["Lines have been cut off."]]
log.open()

// run
run_main()

// close logfile
log.close()
