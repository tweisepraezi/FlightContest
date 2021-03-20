' --------------------------------------------------------------------------------
'   FCAutoLoad_Logger.vbs
'   Thomas Weise, Deutscher Präzisionsflug-Verein e.V.
'   23.03.2018
'   Version 1.0.1
' --------------------------------------------------------------------------------

' Neu gesicherte Dateien automatisch in Browser laden

Const SEARCH_FOLDER = "C:\FCSave\.logger"
Const SEARCH_FILTER = "*.gac,*.gpx,*.igc,*.nmea"  ' mehrere Filter mit Komma trennen, Start und Ende mit * trennen

Const CHECK_TIME = 500 ' ms
Const OPEN_TIME = 500 ' ms

Const OTHERSCRIPTNAME = ""

Const TITLE = "Flight Contest"

Const URL = "http://localhost:8080/fc/task/loggerformimportextern?loggerfile="

' start system objects
set fso = CreateObject("Scripting.FileSystemObject")
set shell = CreateObject("WScript.Shell")
set wmi = GetObject("winmgmts:{impersonationLevel=impersonate}!\\.\root\cimv2")

LastFiles = ""

If Not IsRun(TITLE) Then
	Run()
End If

' ----------------------------------------------------------------------------------
Sub Run()
	'
	If fso.FolderExists(SEARCH_FOLDER) Then
		
		MsgBox WScript.ScriptName & " will be start.", 0, TITLE
		
		' search existing files
		LastFiles = GetFiles(SEARCH_FOLDER)

		Do While True
			WScript.Sleep CHECK_TIME
			act_files = GetFiles(SEARCH_FOLDER)
			If act_files <> LastFiles Then
				added_files = GetAddedFiles(LastFiles, act_files)
				If added_files <> "" Then
					WScript.Sleep OPEN_TIME
					If CanOpenFiles(added_files) Then
						OpenFiles added_files
						LastFiles = act_files
					End If
				End If
			End If
		Loop

	End If
	'
End Sub

' ----------------------------------------------------------------------------------
Function GetFiles(folderName)
	'
	files = ""
	Set dir = fso.GetFolder(folderName)
	For Each file In dir.Files
		name = LCase(file.name)
		If IsFileSearch(name, SEARCH_FILTER) Then
			If files <> "" Then
				files = files & ","
			End If
			files = files & name
		End If
	Next
	GetFiles = files
	'
End Function

' ----------------------------------------------------------------------------------
Function IsFileSearch(fileName, searchFilter)
	'
	For Each f In Split(searchFilter, ",")
		filter1 = Split(f, "*")
		If IsFileFiltered(fileName, filter1(0), filter1(1)) Then
			IsFileSearch = True
			Exit Function
		End If
	Next
	'
End Function

' ----------------------------------------------------------------------------------
Function IsFileFiltered(fileName, filterStart, filterEnd)
	'
	If InStr(fileName, filterStart) = 1 And Right(fileName, Len(filterEnd)) = filterEnd Then
		IsFileFiltered = True
	End If
	'
End Function

' ----------------------------------------------------------------------------------
Function GetAddedFiles(lastFiles, actFiles)
	'
	added_files = ""
	For Each act_file In Split(actFiles, ",")
		found = False
		For Each last_file In Split(lastFiles, ",")
			If act_file = last_file Then
				found = True
				Exit For
			End If
		Next
		If Not found Then
			If added_files <> "" Then
				added_files = added_files & ","
			End If
			added_files = added_files & act_file
		End If
	Next
	GetAddedFiles = added_files
	'
End Function

' ----------------------------------------------------------------------------------
Function OpenFiles(fileList)
	'
	For Each file_name In Split(fileList, ",")
		OpenFile file_name
	Next
	'
End Function

' ----------------------------------------------------------------------------------
Function OpenFile(fileName)
	'
	cmd = URL & SEARCH_FOLDER & "\" & fileName
	shell.Run cmd, 1, False ' 0 - unsichtbar, True - Wait for return
	'
End Function

' ----------------------------------------------------------------------------------
Function CanOpenFiles(fileList)
	'
	can_open_files = True
	On Error Resume Next
	For Each file_name In Split(fileList, ",")
		Set file = fso.OpenTextFile(SEARCH_FOLDER & "\" & file_name, 8, False) ' 8 - Append
		If Err.Number = 0 Then
			file.Close
		Else
			can_open_files = False
			Exit For
		End If
	Next
	CanOpenFiles = can_open_files
	'
End Function

' ----------------------------------------------------------------------------------
Function IsRun(titleStr)
	'
	script_num = 0
	Dim terminate_process
	On Error Resume Next
	Set processes = WMI.ExecQuery("Select * from Win32_Process")
	If Err.Number = 0 Then
		For Each p In processes
			If InStr(LCase(p.CommandLine), LCase(WScript.Fullname)) > 0 And InStr(LCase(p.CommandLine), LCase(WScript.ScriptName)) > 0 Then
				script_num = script_num + 1
				If script_num = 1 Then
					Set terminate_process = p
				End If
			End If
			If OTHERSCRIPTNAME <> "" And InStr(LCase(p.CommandLine), LCase(OTHERSCRIPTNAME)) > 0 Then
				MsgBox OTHERSCRIPTNAME & " is running.", 0, titleStr
				IsRun = True
				Exit Function
			End If
			If script_num > 1 Then
				ret = MsgBox (WScript.ScriptName & " is running. Do you want to stop it?", 4 + 32, titleStr)
				If ret = 6 Then
					terminate_process.Terminate()
					MsgBox WScript.ScriptName & " has been stopped.", 0, titleStr
				End If
				IsRun = True
				Exit Function
			End If
		Next
	End If
	'
End Function

