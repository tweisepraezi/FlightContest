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
$str_fc = "Flight Contest 3.4.4"
$str_fc_url = "http://localhost:8080/fc/contest/start"
$str_homepage = "flightcontest.de"
$str_homepage_url = "https://flightcontest.de"
$str_fcsave = "C:\FCSave"
if ((Get-WinUserLanguageList)[0].EnglishName -eq "German") {
    $str_help = "Hilfe"
    $str_help_url = "http://localhost:8080/fc/docs/help_de.html"
    $str_areyousure = "Sind Sie sicher?"
    $str_menu_exit = "Beenden"
    $str_menu_restart = "Manager neu starten"
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
} else {
    $str_help = "Help"
    $str_help_url = "http://localhost:8080/fc/docs/help_en.html"
    $str_areyousure = "Are you sure?"
    $str_menu_exit = "Exit"
    $str_menu_restart = "Restart manager"
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
$menu_fc = New-Object System.Windows.Forms.MenuItem
$menu_fc.Text = $str_fc
$menu_fc.Add_Click({
    Start-Process $str_fc_url
})

$menu_homepage = New-Object System.Windows.Forms.MenuItem
$menu_homepage.Text = $str_homepage
$menu_homepage.Add_Click({
    Start-Process $str_homepage_url
})

$menu_help = New-Object System.Windows.Forms.MenuItem
$menu_help.Text = $str_help
$menu_help.Add_Click({
    Start-Process $str_help_url
})

$menu_fcsave = New-Object System.Windows.Forms.MenuItem
$menu_fcsave.Text = $str_fcsave
$menu_fcsave.Add_Click({
    Start-Process $str_fcsave
})

# Evaluation menu entries
# -----------------------

$menu_evaluation = New-Object System.Windows.Forms.MenuItem
$menu_evaluation.Text = $str_evaluation

$menu_evaluation_loadlogger = $menu_evaluation.MenuItems.Add($str_evaluation_loadlogger)
$menu_evaluation_loadlogger.Add_Click({ 
    Start-Process "C:\Program Files\Flight Contest\scripts\FCAutoLoad_Logger.vbs"
})

$menu_evaluation_loadobservations = $menu_evaluation.MenuItems.Add($str_evaluation_loadobservations)
$menu_evaluation_loadobservations.Add_Click({ 
    Start-Process "C:\Program Files\Flight Contest\scripts\FCAutoLoadScan_Observation.vbs"
})
 
$menu_evaluation_loadplanningtasks = $menu_evaluation.MenuItems.Add($str_evaluation_loadplanningtasks)
$menu_evaluation_loadplanningtasks.Add_Click({ 
    Start-Process "C:\Program Files\Flight Contest\scripts\FCAutoLoadScan_PlanningTask.vbs"
})

# Service menu entries
# --------------------
$menu_service = New-Object System.Windows.Forms.MenuItem
$menu_service.Text = $str_service

$menu_service_manager = $menu_service.MenuItems.Add($str_service_manager)
$menu_service_manager.Add_Click({
    Start-Process "C:\Program Files\Flight Contest\tomcat\bin\tomcat9w.exe" //MS//FlightContest
})

$menu_service_restart = $menu_service.MenuItems.Add($str_service_restart)
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

$menu_service_start = $menu_service.MenuItems.Add($str_service_start)
$menu_service_start.Add_Click({
    $service = Get-Service -name FlightContest
    if ($service.Status -eq "running") {
        [System.Windows.Forms.MessageBox]::Show($str_service_started, $str_fcmanager)
    } else {
        Start-Service FlightContest
        [System.Windows.Forms.MessageBox]::Show($str_service_starting, $str_fcmanager)
    }
})

$menu_service_stop = $menu_service.MenuItems.Add($str_service_stop)
$menu_service_stop.Add_Click({
    $service = Get-Service -name FlightContest
    if ($service.Status -eq "stopped") {
        [System.Windows.Forms.MessageBox]::Show($str_service_stopped, $str_fcmanager)
    } else {
        Stop-Service FlightContest
        [System.Windows.Forms.MessageBox]::Show($str_service_stopping, $str_fcmanager)
    }
})

$menu_service_savedb = $menu_service.MenuItems.Add($str_service_savedb)
$menu_service_savedb.Add_Click({
    Start-Process "C:\Program Files\Flight Contest\scripts\save_fcdb.bat"
})

# Restart menu entry
# ------------------
$menu_restart = New-Object System.Windows.Forms.MenuItem
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
$menu_exit = New-Object System.Windows.Forms.MenuItem
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
 
# Create taskbar menu
# -------------------
$taskbar_menu = New-Object System.Windows.Forms.ContextMenu
$taskbar_menu.MenuItems.AddRange($menu_fc)
$taskbar_menu.MenuItems.AddRange($menu_fcsave)
$taskbar_menu.MenuItems.AddRange($menu_help)
$taskbar_menu.MenuItems.AddRange($menu_homepage)
$taskbar_menu.MenuItems.AddRange($menu_evaluation)
$taskbar_menu.MenuItems.AddRange($menu_service)
$taskbar_menu.MenuItems.AddRange($menu_restart)
$taskbar_menu.MenuItems.AddRange($menu_exit)
$taskbar_icon.ContextMenu = $taskbar_menu
$taskbar_icon.Add_Click({
    If ($_.Button -eq [Windows.Forms.MouseButtons]::Left) {
        Start-Process "http://localhost:8080/fc/contest/start"
#        $window.Left = $([System.Windows.SystemParameters]::WorkArea.Width-$window.Width)
#        $window.Top = $([System.Windows.SystemParameters]::WorkArea.Height-$window.Height)
#        $window.Show()
#        $window.Activate() 
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
