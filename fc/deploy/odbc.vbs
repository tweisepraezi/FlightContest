On Error Resume Next

Set shell = WScript.CreateObject("WScript.Shell")

aflosPath = shell.RegRead("HKCU\Software\ODBC\ODBC.INI\AFLOS\DBQ")
Err.Clear
If aflosPath <> "" Then
    DeleteKeys
    CopyValue "Software\ODBC\ODBC.INI\AFLOS\DBQ", "REG_SZ"
    CopyValue "Software\ODBC\ODBC.INI\AFLOS\Driver", "REG_SZ"
    CopyValue "Software\ODBC\ODBC.INI\AFLOS\DriverId", "REG_DWORD"
    CopyValue "Software\ODBC\ODBC.INI\AFLOS\SafeTransactions", "REG_DWORD"
    CopyValue "Software\ODBC\ODBC.INI\AFLOS\UID", "REG_SZ"
    CopyValue "Software\ODBC\ODBC.INI\AFLOS\Engines\Jet\ImplicitCommitSync", "REG_SZ"
    CopyValue "Software\ODBC\ODBC.INI\AFLOS\Engines\Jet\Threads", "REG_DWORD"
    CopyValue "Software\ODBC\ODBC.INI\AFLOS\Engines\Jet\UserCommitSync", "REG_SZ"
Else
    aflosPath = "C:\AFLOS\AFLOS_System\AFLOS.mdb"
    uninstallAflosPath = shell.RegRead("HKLM\Software\Microsoft\Windows\CurrentVersion\Uninstall\AFLOS\UninstallString")
    Err.Clear
    If uninstallAflosPath <> "" Then
        aflosPath = Left(uninstallAflosPath,2) + "\AFLOS\AFLOS_System\AFLOS.mdb"
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
        WriteValue "Software\ODBC\ODBC.INI\AFLOS\DBQ", "REG_SZ", aflosPath
        WriteValue "Software\ODBC\ODBC.INI\AFLOS\Driver", "REG_SZ", driverPath
        WriteValue "Software\ODBC\ODBC.INI\AFLOS\DriverId", "REG_DWORD", 25
        WriteValue "Software\ODBC\ODBC.INI\AFLOS\SafeTransactions", "REG_DWORD", 0
        WriteValue "Software\ODBC\ODBC.INI\AFLOS\UID", "REG_SZ", ""
        WriteValue "Software\ODBC\ODBC.INI\AFLOS\Engines\Jet\ImplicitCommitSync", "REG_SZ", ""
        WriteValue "Software\ODBC\ODBC.INI\AFLOS\Engines\Jet\Threads", "REG_DWORD", 3
        WriteValue "Software\ODBC\ODBC.INI\AFLOS\Engines\Jet\UserCommitSync", "REG_SZ", "Yes"
        WriteValue "Software\ODBC\ODBC.INI\ODBC Data Sources\AFLOS", "REG_SZ", driverName
    End If
End If

Sub CopyValue(valueName, valueType)
    value = shell.RegRead("HKCU\" + valueName)
    shell.RegWrite "HKLM\" + valueName, value, valueType
End Sub

Sub WriteValue(valueName, valueType, valueValue)
    shell.RegWrite "HKLM\" + valueName, valueValue, valueType
End Sub

Sub DeleteKeys()
    On Error Resume Next
    shell.RegDelete "HKLM\Software\ODBC\ODBC.INI\AFLOS\Engines\Jet\"
    Err.Clear
    shell.RegDelete "HKLM\Software\ODBC\ODBC.INI\AFLOS\Engines\"
    Err.Clear
    shell.RegDelete "HKLM\Software\ODBC\ODBC.INI\AFLOS\"
    Err.Clear
End Sub
