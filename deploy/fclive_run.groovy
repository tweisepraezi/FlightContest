// Thomas Weise
// Version 1.0.0

def CONTEST_ID = '1'  // ID des Wettbewerbes
def LANGUAGE = 'de'   // Ausgabe-Sprache (de oder en)

def Download = true

// Liste der FTP-Verbindungen, wohin Live-Ergebnisse geuploaded werden sollen
//   Bsp.: [server:"some-server.some-domain.com",login_name:"some-login-name",login_pwd:"some-password",working_dir:"/"],
def FTPConnections = [
	//[server:"some-server.some-domain.com",login_name:"some-login-name",login_pwd:"some-password",working_dir:"/"],
]

// Liste der Dateien, wohin Live-Ergebnisse kopiert werden sollen
//   Bsp. lokale Platte: [file:"c:\\_temp\\fclive1.htm"],
//   Bsp. Share:         [file:"\\\\some-server.some-domain.com\\some-share\\fclive1.htm"],
def CopyDestinations = [
	//[file:"c:\\_temp\\fclive1.htm"],
]

//---------------------------------------------------------------------------------------------------------------------------
import org.apache.commons.net.ftp.FTPClient

def LIVE_HTM_URL = "http://localhost:8080/fc/contest/listresultslive/${CONTEST_ID}?lang=${LANGUAGE}"
def TEMP_HTM_NAME = "fclive.htm"
def FTP_HTM_NAME = "fclive.htm"

byte[] utf8_bom = new byte[3]
utf8_bom[0] = (byte) 0xEF
utf8_bom[1] = (byte) 0xBB
utf8_bom[2] = (byte) 0xBF

byte[] utf16_bom = new byte[2]
utf16_bom[0] = (byte) 0xFF
utf16_bom[1] = (byte) 0xFE

// get html page
try {
	if (Download) {
		println "Download $TEMP_HTM_NAME..."
		def file = new File(TEMP_HTM_NAME).newOutputStream()  
		file << new String(utf8_bom)
		file << new URL(LIVE_HTM_URL).openStream()
		file.close()
		println "Done."
	}
} catch(Exception e) {
	println "Error: ${e.getMessage()}"
}

// ftp upload
if (FTPConnections) {
	FTPConnections.each { ftp_connection ->
		try {
			println "Upload $TEMP_HTM_NAME to $ftp_connection.server/${ftp_connection.working_dir}${FTP_HTM_NAME}..."
			new FTPClient().with {
				connect ftp_connection.server
				enterLocalPassiveMode()
				login ftp_connection.login_name, ftp_connection.login_pwd
				storeFile "${ftp_connection.working_dir}${FTP_HTM_NAME}", new File(TEMP_HTM_NAME).newInputStream()
				disconnect()
			}
			println "Done."
		} catch(Exception e) {
			println "Error: ${e.getMessage()}"
		}
	}
}

// copy to drive or share
if (CopyDestinations) {
	CopyDestinations.each { destination ->
		try {
			println "Copy $destination.file..."
			def file3 = new File(destination.file).newOutputStream()  
			file3 << new File(TEMP_HTM_NAME).newInputStream()
			file3.close()
			println "Done."
		} catch(Exception e) {
			println "Error: ${e.getMessage()}"
		}
	}
}
