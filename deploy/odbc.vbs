' Thomas Weise
' Version 2.0.0

On Error Resume Next

Set shell = WScript.CreateObject("WScript.Shell")

aflos_db_file = shell.RegRead("HKCU\Software\ODBC\ODBC.INI\AFLOS\DBQ")
Err.Clear
If aflos_db_file = "" Then
	uninstallAflosPath = shell.RegRead("HKLM\Software\Microsoft\Windows\CurrentVersion\Uninstall\AFLOS\UninstallString")
	Err.Clear
	If uninstallAflosPath <> "" Then
		aflos_db_file = Left(uninstallAflosPath,2) + "\AFLOS\AFLOS_System\AFLOS.mdb"
	Else
		aflos_db_file = "C:\AFLOS\AFLOS_System\AFLOS.mdb"
	End If
End If
SetAflosPath "FC-AFLOS", aflos_db_file

If WScript.Arguments.Count > 0 Then
	fc_path = WScript.Arguments(0)
	SetAflosPath "FC-AFLOS-UPLOAD", fc_path & "\fc\AFLOS-UPLOAD.mdb"
	SetAflosPath "FC-AFLOS-TEST", fc_path & "\samples\AFLOS-DemoContest.mdb"
End If

Sub SetAflosPath(aflosKey, aflosDbFile)
	On Error Resume Next
	driver_dll = shell.RegRead("HKLM\Software\ODBC\ODBCINST.INI\Driver do Microsoft Access (*.mdb)\Driver")
	driver_name = "Driver do Microsoft Access (*.mdb)"
	Err.Clear
	If driver_dll = "" Then
		driver_dll = shell.RegRead("HKLM\Software\ODBC\ODBCINST.INI\Microsoft Access Driver (*.mdb)\Driver")
		driver_name = "Microsoft Access Driver (*.mdb)"
		Err.Clear
	End If
	If driver_dll = "" Then
		driver_dll = shell.RegRead("HKLM\Software\ODBC\ODBCINST.INI\Microsoft Access-Treiber (*.mdb)\Driver")
		driver_name = "Microsoft Access-Treiber (*.mdb)"
		Err.Clear
	End If
	If driver_dll <> "" Then
		DeleteKeys aflosKey
		WriteValue "Software\ODBC\ODBC.INI\" & aflosKey & "\DBQ", "REG_SZ", aflosDbFile
		WriteValue "Software\ODBC\ODBC.INI\" & aflosKey & "\Driver", "REG_SZ", driver_dll
		WriteValue "Software\ODBC\ODBC.INI\" & aflosKey & "\DriverId", "REG_DWORD", 25
		WriteValue "Software\ODBC\ODBC.INI\" & aflosKey & "\SafeTransactions", "REG_DWORD", 0
		WriteValue "Software\ODBC\ODBC.INI\" & aflosKey & "\UID", "REG_SZ", ""
		WriteValue "Software\ODBC\ODBC.INI\" & aflosKey & "\Engines\Jet\ImplicitCommitSync", "REG_SZ", ""
		WriteValue "Software\ODBC\ODBC.INI\" & aflosKey & "\Engines\Jet\Threads", "REG_DWORD", 3
		WriteValue "Software\ODBC\ODBC.INI\" & aflosKey & "\Engines\Jet\UserCommitSync", "REG_SZ", "Yes"
		WriteValue "Software\ODBC\ODBC.INI\ODBC Data Sources\" & aflosKey & "", "REG_SZ", driver_name
	End If
End Sub

Sub WriteValue(valueName, valueType, valueValue)
    On Error Resume Next
    shell.RegWrite "HKLM\" + valueName, valueValue, valueType
    Err.Clear
End Sub

Sub DeleteKeys(aflosKey)
    On Error Resume Next
    shell.RegDelete "HKLM\Software\ODBC\ODBC.INI\" & aflosKey & "\Engines\Jet\"
    Err.Clear
    shell.RegDelete "HKLM\Software\ODBC\ODBC.INI\" & aflosKey & "\Engines\"
    Err.Clear
    shell.RegDelete "HKLM\Software\ODBC\ODBC.INI\" & aflosKey & "\"
    Err.Clear
End Sub
