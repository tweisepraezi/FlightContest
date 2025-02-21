# Part used for the restart button
Param (
 [String]$Restart
)
 
If ($Restart -ne "") {
  sleep 1
}

# Strings
# -------
$str_fcmanager = "Flight Contest Manager"
$str_fc_version = "4.0.2"
$str_fc = "Flight Contest " + $str_fc_version
$str_fc_url = "http://localhost:8080/fc/contest/start"
$str_homepage = "flightcontest.de"
$str_homepage_url = "https://flightcontest.de"
$str_fcsave = "C:\FCSave"
$str_firefox_url = "C:\Program Files\Mozilla Firefox\firefox.exe"
$str_new_version_url = "http://download.flightcontest.de/FCSetup4-NewVersion.txt"
$str_new_setup_url = "http://download.flightcontest.de"
$str_new_setup_path = "C:\FCSave\.fcsetups"
if ((Get-WinUserLanguageList)[0].EnglishName -eq "German") {
    $str_help = "Hilfe"
    $str_help_url = "http://localhost:8080/fc/docs/help_de.html"
    $str_areyousure = "Sind Sie sicher?"
    $str_menu_exit = "Beenden"
    $str_menu_restart = "Manager neu starten"
    $str_menu_fc_install = "Installiere Flight Contest"
    $str_evaluation = "Auswertungs-Kommandos"
    $str_evaluation_loadlogger = "Logger-Daten automatisch laden"
    $str_evaluation_loadobservations = "Beobachtungs-Formulare automatisch laden"
    $str_evaluation_loadplanningtasks = "Planungs-Aufgaben-Formulare automatisch laden"
    $str_service = "Dienst-Kommandos"
    $str_service_manager = "Dienst-Manager"
    $str_service_restart = "Neustart Flight Contest"
    $str_service_restarting = "Flight Contest startet neu."
    $str_service_start = "Start Flight Contest"
    $str_service_starting = "Flight Contest startet."
    $str_service_started = "Flight Contest ist bereits gestartet."
    $str_service_stop = "Stop Flight Contest"
    $str_service_stopping = "Flight Contest stoppt."
    $str_service_stopped = "Flight Contest ist bereits gestoppt."
    $str_service_savedb = "Datenbank sichern"
    $str_getclientid = "Client-ID ermitteln"
    $str_clientid = "Client-ID wurde in die Zwischenablage kopiert: "
} else {
    $str_help = "Help"
    $str_help_url = "http://localhost:8080/fc/docs/help_en.html"
    $str_areyousure = "Are you sure?"
    $str_menu_exit = "Exit"
    $str_menu_restart = "Restart manager"
    $str_menu_fc_install = "Install Flight Contest"
    $str_evaluation = "Evaluation commands"
    $str_evaluation_loadlogger = "Auto load logger data"
    $str_evaluation_loadobservations = "Auto load observation forms"
    $str_evaluation_loadplanningtasks = "Auto load planning task forms"
    $str_service = "Service commands"
    $str_service_manager = "Service Manager"
    $str_service_restart = "Restart Flight Contest"
    $str_service_restarting = "Flight Contest is restarting."
    $str_service_start = "Start Flight Contest"
    $str_service_starting = "Flight Contest is starting."
    $str_service_started = "Flight Contest is already running."
    $str_service_stop = "Stop Flight Contest"
    $str_service_stopping = "Flight Contest is stopping."
    $str_service_stopped = "Flight Contest is already stopped."
    $str_service_savedb = "Save database"
    $str_getclientid = "Get client-id"
    $str_clientid = "Client-id has been copied to the clipboard: "
}

# Load assemblies 
# ---------------
[System.Reflection.Assembly]::LoadWithPartialName('System.Windows.Forms')    | out-null
[System.Reflection.Assembly]::LoadWithPartialName('presentationframework')   | out-null
[System.Reflection.Assembly]::LoadWithPartialName('System.Drawing')          | out-null
[System.Reflection.Assembly]::LoadWithPartialName('WindowsFormsIntegration') | out-null
 
# GUI to display
# --------------
<#
[xml]$xaml = @"
<Window xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation" xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml" 
        WindowStyle="None" Height="600" Width="400" ResizeMode="NoResize" ShowInTaskbar="False" AllowsTransparency="True" Background="Transparent">
    <Border BorderBrush="Black" BorderThickness="1" Margin="10,10,10,10">
        <Grid Name="grid" Background="White">
            <StackPanel HorizontalAlignment="Center" VerticalAlignment="Center">
                <Button Name="close_button" Width="80" Height="20"></Button>
            </StackPanel>  
        </Grid> 
    </Border>
</Window>
"@

$window = [Windows.Markup.XamlReader]::Load((New-Object System.Xml.XmlNodeReader $xaml))

$window_close_button = $window.findname("close_button") 
$window_close_button.Content = "Close"
$window_close_button.Add_Click({ 
    $window.Close()
})
#>

# Functions
#----------
function start_flightcontest {
    if ([System.IO.File]::Exists($str_firefox_url)) {
        Start-Process $str_firefox_url $str_fc_url
    } else {
        Start-Process $str_fc_url
    }
}

function stop_manager {
    
    foreach ($proc in (Get-CimInstance Win32_Process -Filter "Name = 'powershell.exe'" | Where-Object -Property CommandLine -like '*fcmanager.ps1*' ))
    {
        Stop-Process $proc.ProcessId
    }
}

# Stop other running FCManager
# ----------------------------
foreach ($proc in (Get-CimInstance Win32_Process -Filter "Name = 'powershell.exe'" | Where-Object -Property CommandLine -like '*fcmanager.ps1*' ))
{
    if ($proc.ProcessId -ne $pid) {
        Stop-Process $proc.ProcessId
    }
}

# Taskbar icon
# ------------
$icon = New-Object System.Drawing.Icon "C:\Program Files\Flight Contest\fc.ico"
if (!$icon) {
    $icon = New-Object System.Drawing.Icon "..\web-app\images\fc.ico"
}
if (!$icon) {
    $icon = [System.Drawing.Icon]::ExtractAssociatedIcon("C:\Windows\System32\mmc.exe")
}
$taskbar_icon = New-Object System.Windows.Forms.NotifyIcon
$taskbar_icon.Text = $str_fcmanager
$taskbar_icon.Icon = $icon
$taskbar_icon.Visible = $true
 
# Flight Contest menu entries
# ---------------------------
$menu_fc = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_fc.Text = $str_fc
$menu_fc.Add_Click({
    start_flightcontest
})

$menu_homepage = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_homepage.Text = $str_homepage
$menu_homepage.Add_Click({
    Start-Process $str_homepage_url
})

$menu_help = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_help.Text = $str_help
$menu_help.Add_Click({
    Start-Process $str_help_url
})

$menu_fcsave = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_fcsave.Text = $str_fcsave
$menu_fcsave.Add_Click({
    Start-Process $str_fcsave
})

$menu_getclientid = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_getclientid.Text = $str_getclientid
$menu_getclientid.Add_Click({
    $client_id = (Get-ItemProperty -Path HKLM:\SOFTWARE\Microsoft\SQMClient -Name "MachineID").MachineID
    $client_id = $client_id.Replace('{','')
    $client_id = $client_id.Replace('}','')
    Set-Clipboard -Value $client_id
    [System.Windows.Forms.MessageBox]::Show($str_clientid + " " + $client_id, $str_fcmanager)
})

# Evaluation menu entries
# -----------------------

$menu_evaluation = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_evaluation.Text = $str_evaluation

$menu_evaluation_loadlogger = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_evaluation_loadlogger.Text = $str_evaluation_loadlogger
$menu_evaluation_loadlogger.Add_Click({ 
    Start-Process "C:\Program Files\Flight Contest\scripts\FCAutoLoad_Logger.vbs"
})
$menu_evaluation.DropDownItems.Add($menu_evaluation_loadlogger)

$menu_evaluation_loadobservations = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_evaluation_loadobservations.Text = $str_evaluation_loadobservations
$menu_evaluation_loadobservations.Add_Click({ 
    Start-Process "C:\Program Files\Flight Contest\scripts\FCAutoLoadScan_Observation.vbs"
})
$menu_evaluation.DropDownItems.Add($menu_evaluation_loadobservations)
 
$menu_evaluation_loadplanningtasks = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_evaluation_loadplanningtasks.Text = $str_evaluation_loadplanningtasks
$menu_evaluation_loadplanningtasks.Add_Click({ 
    Start-Process "C:\Program Files\Flight Contest\scripts\FCAutoLoadScan_PlanningTask.vbs"
})
$menu_evaluation.DropDownItems.Add($menu_evaluation_loadplanningtasks)

# Service menu entries
# --------------------
$menu_service = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_service.Text = $str_service

$menu_service_manager = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_service_manager.Text = $str_service_manager
$menu_service_manager.Add_Click({
    Start-Process "C:\Program Files\Flight Contest\tomcat\bin\tomcat9w.exe" //MS//FlightContest
})
$menu_service.DropDownItems.Add($menu_service_manager)

$menu_service_restart = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_service_restart.Text = $str_service_restart
$menu_service_restart.Add_Click({
    $service = Get-Service -name FlightContest
    if ($service.Status -eq "running") {
        Stop-Service FlightContest
        Start-Service FlightContest
        [System.Windows.Forms.MessageBox]::Show($str_service_restarting, $str_fcmanager)
    } else {
        if ($service.Status -eq "stopped") {
            Start-Service FlightContest
            [System.Windows.Forms.MessageBox]::Show($str_service_starting, $str_fcmanager)
        }
    }
})
$menu_service.DropDownItems.Add($menu_service_restart)

$menu_service_start = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_service_start.Text = $str_service_start
$menu_service_start.Add_Click({
    $service = Get-Service -name FlightContest
    if ($service.Status -eq "running") {
        [System.Windows.Forms.MessageBox]::Show($str_service_started, $str_fcmanager)
    } else {
        Start-Service FlightContest
        [System.Windows.Forms.MessageBox]::Show($str_service_starting, $str_fcmanager)
    }
})
$menu_service.DropDownItems.Add($menu_service_start)

$menu_service_stop = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_service_stop.Text = $str_service_stop
$menu_service_stop.Add_Click({
    $service = Get-Service -name FlightContest
    if ($service.Status -eq "stopped") {
        [System.Windows.Forms.MessageBox]::Show($str_service_stopped, $str_fcmanager)
    } else {
        Stop-Service FlightContest
        [System.Windows.Forms.MessageBox]::Show($str_service_stopping, $str_fcmanager)
    }
})
$menu_service.DropDownItems.Add($menu_service_stop)

$menu_service_savedb = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_service_savedb.Text = $str_service_savedb
$menu_service_savedb.Add_Click({
    Start-Process "C:\Program Files\Flight Contest\scripts\save_fcdb.bat"
})
$menu_service.DropDownItems.Add($menu_service_savedb)

# Download menu entry
# -------------------
$new_fc_version = Invoke-WebRequest -Uri $str_new_version_url
if ([String]$new_fc_version -ne "") {
    if ([String]$new_fc_version -ne $str_fc_version) {
        $newversion_icon = New-Object System.Drawing.Icon "C:\Program Files\Flight Contest\fcdownload.ico"
        if (!$newversion_icon) {
            $newversion_icon = New-Object System.Drawing.Icon "..\web-app\images\fcdownload.ico"
        }
        $taskbar_icon.Icon = $newversion_icon
        $fc_setup_name = "FCSetup-" + $new_fc_version + ".exe"
        $fc_setup_uri = $str_new_setup_url + "/" + $fc_setup_name
        $fc_setup_file = $str_new_setup_path + "\" + $fc_setup_name
        $menu_download = New-Object System.Windows.Forms.ToolStripMenuItem
        $menu_download.Text = $str_menu_fc_install + " " + $new_fc_version
        $menu_download.add_Click({
            if (![System.IO.File]::Exists($fc_setup_file)) {
                Invoke-WebRequest -Uri $fc_setup_uri -OutFile $fc_setup_file
            }
            if ([System.IO.File]::Exists($fc_setup_file)) {
                Start-Process $fc_setup_file
                stop_manager
            } else {
                [System.Windows.Forms.MessageBox]::Show($fc_setup_file + " not found", $str_fcmanager)
            }
        })
    }
}

# Restart menu entry
# ------------------
$menu_restart = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_restart.Text = $str_menu_restart
$menu_restart.add_Click({
    $Restart = "Yes"
    Start-Process -WindowStyle hidden powershell.exe ".\fcmanager.ps1 '$Restart'"  
 
    # $window.Close()
    Stop-Process $pid
  
    $Global:Timer_Status = $timer.Enabled
    If ($Timer_Status -eq $true) {
        $timer.Stop() 
    }  
})

# Exit menu entry
# ---------------
$menu_exit = New-Object System.Windows.Forms.ToolStripMenuItem
$menu_exit.Text = $str_menu_exit
$menu_exit.add_Click({
    $result = [System.Windows.Forms.MessageBox]::Show($str_menu_exit + ".`r`n`r`n" + $str_areyousure, $str_fcmanager, [System.Windows.Forms.MessageBoxButtons]::OKCancel, [System.Windows.Forms.MessageBoxIcon]::Question)
    If ($result -eq [System.Windows.Forms.DialogResult]::OK) {
        $taskbar_icon.Visible = $false
        # $window.Close()
        Stop-Process $pid
        $Global:Timer_Status = $timer.Enabled
        If ($Timer_Status -eq $true) {
            $timer.Stop() 
        } 
    }
})

# Separators
# ----------
$separator1 = New-Object System.Windows.Forms.ToolStripSeparator
$separator2 = New-Object System.Windows.Forms.ToolStripSeparator
$separator3 = New-Object System.Windows.Forms.ToolStripSeparator

# Create taskbar menu
# -------------------
$taskbar_menu_strip = New-Object System.Windows.Forms.ContextMenuStrip
$taskbar_menu_strip.Items.Add($menu_fc)
if ($menu_download) {
    $taskbar_menu_strip.Items.Add($menu_download)
}
$taskbar_menu_strip.Items.Add($separator1)
$taskbar_menu_strip.Items.Add($menu_fcsave)
$taskbar_menu_strip.Items.Add($menu_help)
$taskbar_menu_strip.Items.Add($menu_homepage)
$taskbar_menu_strip.Items.Add($separator2)
$taskbar_menu_strip.Items.Add($menu_evaluation)
$taskbar_menu_strip.Items.Add($menu_service)
$taskbar_menu_strip.Items.Add($separator3)
$taskbar_menu_strip.Items.Add($menu_getclientid)
$taskbar_menu_strip.Items.Add($menu_restart)
$taskbar_menu_strip.Items.Add($menu_exit);
$taskbar_icon.ContextMenuStrip = $taskbar_menu_strip
$taskbar_icon.Add_Click({
    If ($_.Button -eq [Windows.Forms.MouseButtons]::Left) {
        start_flightcontest
    }  
})
 
# Hide powershell
# ---------------
$windowcode = '[DllImport("user32.dll")] public static extern bool ShowWindowAsync(IntPtr hWnd, int nCmdShow);'
$asyncwindow = Add-Type -MemberDefinition $windowcode -name Win32ShowWindowAsync -namespace Win32Functions -PassThru
$null = $asyncwindow::ShowWindowAsync((Get-Process -PID $pid).MainWindowHandle, 0)
 
# Force garbage collection just to start slightly lower RAM usage.
[System.GC]::Collect()
 
# Create an application context for it to all run within.
# This helps with responsiveness, especially when clicking Exit.
$appContext = New-Object System.Windows.Forms.ApplicationContext
[void][System.Windows.Forms.Application]::Run($appContext)

