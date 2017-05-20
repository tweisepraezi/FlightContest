' --------------------------------------------------------------------------------
'   convert_utf8toansi.vbs
'   Thomas Weise, Deutscher Präzisionsflug-Verein e.V.
'   19.05.2017
'   Version 1.1
' --------------------------------------------------------------------------------

' Transform UTF-8 to ASCII with "&#...;"

' fso constants
Const ForReading          = 1
Const ForWriting          = 2
Const ForAppending        = 8

Const TristateFalse       = 0
Const TristateTrue        = -1  ' Unicode
Const TristateUseDefault  = -2

Const adReadAll 	      = -1
Const adReadLine 	      = -2

Set fso = CreateObject("Scripting.FileSystemObject")

Set stream = CreateObject("ADODB.Stream")

If WScript.Arguments.Count = 1 Then
	'
	scr_name = WScript.Arguments(0)
	new_name = scr_name + ".new"
	'
	If fso.FileExists(scr_name) Then
		'
		stream.CharSet = "utf-8"
		stream.Open
		stream.LoadFromFile(scr_name)
		Set new_file = fso.OpenTextFile( new_name, ForWriting, True, TristateFalse )
		'
		Do While Not stream.EOS
			line = stream.ReadText(adReadLine)
			If Not IgnoreLine(line) Then
				new_file.WriteLine FilterLine(line)
			End If
		Loop
		'
		'
		new_file.Close
		If Not en_exists Then
			fso.DeleteFile scr_name
			fso.MoveFile new_name, scr_name
		Else
			fso.DeleteFile new_name
		End If
	End If
	'
End If

Function FilterLine(lineStr)
	'
	ret = ""
	For i = 1 To Len(lineStr)
		ch = Mid(lineStr,i,1)
		If Asc(ch) > 128 Then
			ret = ret & "&#" & Asc(ch) & ";"
		Else
			ret = ret & ch
		End If
	Next
	FilterLine = ret
	'
End Function

Function IgnoreLine(lineStr)
	'
	If InStr(lineStr,"<link") = 1 Then
		IgnoreLine = True
	End If
	'
End Function
