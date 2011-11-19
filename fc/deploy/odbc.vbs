On Error Resume Next

Set shell = WScript.CreateObject("WScript.Shell")

aflosPath = shell.RegRead("HKCU\Software\ODBC\ODBC.INI\AFLOS\DBQ")
Err.Clear
If aflosPath = "" Then
	uninstallAflosPath = shell.RegRead("HKLM\Software\Microsoft\Windows\CurrentVersion\Uninstall\AFLOS\UninstallString")
	Err.Clear
	If uninstallAflosPath <> "" Then
		aflosPath = Left(uninstallAflosPath,2) + "\AFLOS\AFLOS_System\AFLOS.mdb"
	Else
		aflosPath = "C:\AFLOS\AFLOS_System\AFLOS.mdb"
	End If
End If

driverPath = shell.RegRead("HKLM\Software\ODBC\ODBCINST.INI\Driver do Microsoft Access (*.mdb)\Driver")
driverName = "Driver do Microsoft Access (*.mdb)"
Err.Clear
If driverPath = "" Then
	driverPath = shell.RegRead("HKLM\Software\ODBC\ODBCINST.INI\Microsoft Access Driver (*.mdb)\Driver")
	driverName = "Microsoft Access Driver (*.mdb)"
	Err.Clear
End If
If driverPath = "" Then
	driverPath = shell.RegRead("HKLM\Software\ODBC\ODBCINST.INI\Microsoft Access-Treiber (*.mdb)\Driver")
	driverName = "Microsoft Access-Treiber (*.mdb)"
	Err.Clear
End If
If driverPath <> "" Then
	DeleteKeys
	WriteValue "Software\ODBC\ODBC.INI\FC-AFLOS\DBQ", "REG_SZ", aflosPath
	WriteValue "Software\ODBC\ODBC.INI\FC-AFLOS\Driver", "REG_SZ", driverPath
	WriteValue "Software\ODBC\ODBC.INI\FC-AFLOS\DriverId", "REG_DWORD", 25
	WriteValue "Software\ODBC\ODBC.INI\FC-AFLOS\SafeTransactions", "REG_DWORD", 0
	WriteValue "Software\ODBC\ODBC.INI\FC-AFLOS\UID", "REG_SZ", ""
	WriteValue "Software\ODBC\ODBC.INI\FC-AFLOS\Engines\Jet\ImplicitCommitSync", "REG_SZ", ""
	WriteValue "Software\ODBC\ODBC.INI\FC-AFLOS\Engines\Jet\Threads", "REG_DWORD", 3
	WriteValue "Software\ODBC\ODBC.INI\FC-AFLOS\Engines\Jet\UserCommitSync", "REG_SZ", "Yes"
	WriteValue "Software\ODBC\ODBC.INI\ODBC Data Sources\FC-AFLOS", "REG_SZ", driverName
End If

Sub WriteValue(valueName, valueType, valueValue)
    shell.RegWrite "HKLM\" + valueName, valueValue, valueType
End Sub

Sub DeleteKeys()
    On Error Resume Next
    shell.RegDelete "HKLM\Software\ODBC\ODBC.INI\FC-AFLOS\Engines\Jet\"
    Err.Clear
    shell.RegDelete "HKLM\Software\ODBC\ODBC.INI\FC-AFLOS\Engines\"
    Err.Clear
    shell.RegDelete "HKLM\Software\ODBC\ODBC.INI\FC-AFLOS\"
    Err.Clear
End Sub
