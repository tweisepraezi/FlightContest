
//=================================================================================================
class LogService
{
	/* Anwendung:
	log.printstart("printstart51")
	log.print("print")
	log.println(" println")
	log.printend("printend51")
	*/

	//--------------------------------------------------------------------------
	LogService(boolean printSystem, File logFile = null, boolean logAppend = false)
	{
		this.printSystem = printSystem
		if (logFile) {
			logFileWriter = logFile.newWriter(logAppend)
		}
	}
	
	//--------------------------------------------------------------------------
	LogService()
	{
		this.printSystem = true
	}
	
	//--------------------------------------------------------------------------
	int MaxLogChars = 0            // Umbruch bei Ausgabezeichenanzahlüberschreitung, 0 - deaktiviert
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
	
	private boolean cachePrintWriteOn = true  // BUG: Print.write fügt ungewollt Zeilenwechsel ein
	private String cachePrintWrite = ""
	
	
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
			//print(out)
			if (cachePrintWriteOn) {
				cachePrintWrite += out
			} else {
				Print.write(out)
			}
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
			//println(out)
			if (cachePrintWriteOn) {
				cachePrintWrite += out
				Print.writeln(cachePrintWrite)
				cachePrintWrite = ""
			} else {
				Print.writeln(out)
			}
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

class Print
{
	static void write(out)
	{
		print out
	}
	
	static void writeln(out)
	{
		println out
	}
}

/*
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
	Logger(boolean printSystem, File logFile = null, boolean logAppend = false)
	{
		logOn = true
		log = new Log(printSystem,logFile,logAppend)
	}
	
	//--------------------------------------------------------------------------
	Logger()
	{
		logOn = true
		log = new Log(true,null,false)
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
						if (log.getLines() >= logMaxLines) {
							Params?.CutWriteStr.each {
								log.println(it)
							}
							cut = true
						}
					}
					if (!cut) {
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
*/

