import groovy.swing.*
import javax.swing.*
import javax.swing.WindowConstants as WC
import javax.swing.SwingConstants as SC
import javax.swing.filechooser.FileFilter
import java.awt.*
import java.awt.BorderLayout as BL
import org.codehaus.groovy.scriptom.*

/*
http://groovy.jmiguel.eu/groovy.codehaus.org/SwingBuilder.fileChooser.html
http://groovy.jmiguel.eu/groovy.codehaus.org/SwingBuilder.frame.html
http://groovy.jmiguel.eu/groovy.codehaus.org/SwingBuilder.panel.html
http://groovy.jmiguel.eu/groovy.codehaus.org/SwingBuilder.label.html
*/

class MainWindow
{
    //--------------------------------------------------------------------------
    def swing = new SwingBuilder()
    def frame
    def info_text
    def print_button
    String selected_file = ''
    String current_directory = '.'
    
    static final int SIZE_X = 450
    static final int SIZE_Y = 160
    
    //--------------------------------------------------------------------------
    public static void main(String[] args)
    {
        new MainWindow(args)
    }
    
    //--------------------------------------------------------------------------
    MainWindow(String[] args)
    {
        def image_icon = null
        if (new File('src/printlabel.png').exists()) {
            image_icon = swing.imageIcon('src/printlabel.png').image
        } else { // from jar
            image_icon = swing.imageIcon('/printlabel.png').image
        }
        frame = swing.frame(title: 'Flight Contest Print Label 1.0',
                            size: [SIZE_X,SIZE_Y],
                            minimumSize: [SIZE_X,SIZE_Y],
                            locationRelativeTo: null,
                            defaultCloseOperation: WC.EXIT_ON_CLOSE,
                            iconImage: image_icon,
                            //pack: true,
                            show: true) 
        {
            borderLayout(vgap: 3)
            panel(constraints: BorderLayout.NORTH) {
                info_text = label(text:'<html><br/><br/><br/></html>')
            }
            panel(constraints: BorderLayout.CENTER) {
                print_button = button text: 'Print label...', actionPerformed: {
                    def file_dlg = fileChooser(dialogTitle: 'Choose a TXT file',
                                               id: 'openTxtDialog',
                                               currentDirectory: new File(current_directory),
                                               fileSelectionMode: JFileChooser.FILES_ONLY,
                                               fileFilter: [getDescription: {-> '*.txt'}, accept:{file-> file ==~ /.*?\.txt/ || file.isDirectory() }] as FileFilter ) {}
                    if (file_dlg.showOpenDialog() != JFileChooser.APPROVE_OPTION) {
                        info_text.text = "<html><br/><br/><br/></html>"
                        return
                    }
                    
                    // get file name
                    selected_file = file_dlg.selectedFile.getAbsolutePath()
                    current_directory = selected_file.substring(0, selected_file.lastIndexOf('\\'))
                    
                    // print
                    info_text.text = "<html>Printing '${selected_file}'...</html>"
                    info_text.foreground = java.awt.Color.BLUE
                    print_button.visible = false
                    
                    
                    doOutside {
                        Map print_ret = [printed:true, errmsg:'']
                        
                        try {
                            def printer_obj = new ActiveXObject("bpac.Document")
                            if (printer_obj) {
                                String label_template = "src/FCLabel.lbx"
                                if (!(new File(label_template).exists())) { // from jar
                                    String current_dir = args[0]
                                    label_template = "$current_dir/FCLabel.lbx"
                                }
                                if (new File(label_template).exists()) {
                                    boolean printer_open = printer_obj.invokeMethod("Open",label_template)
                                    if (printer_open) {
                                        
                                        File read_file = new File(selected_file)
                                        BufferedReader file_reader = read_file.newReader("UTF-8")
                                        String contest = ""
                                        String task = ""
                                        String start_num = ''
                                        String crew = ''
                                        String aircraft = ''
                                        String result_class = ''
                                        String tas = ''
                                        String planning_time = ''
                                        String takeoff_time = ''
                                        while (true) {
                                            String line = file_reader.readLine()
                                            if (line) {
                                                if (line.startsWith('CONTEST:')) {
                                                    contest = line.substring(8)
                                                }
                                                if (line.startsWith('TASK:')) {
                                                    task = line.substring(5)
                                                }
                                                if (line.startsWith('STARTNUM:')) {
                                                    start_num = line.substring(9)
                                                    crew = ''
                                                    aircraft = ''
                                                    result_class = ''
                                                    tas = ''
                                                    planning_time = ''
                                                    takeoff_time = ''
                                                }
                                                if (line.startsWith('CREW:')) {
                                                    crew = line.substring(5)
                                                }
                                                if (line.startsWith('AIRCRAFT:')) {
                                                    aircraft = line.substring(9)
                                                }
                                                if (line.startsWith('CLASS:')) {
                                                    result_class = line.substring(6)
                                                }
                                                if (line.startsWith('TAS:')) {
                                                    tas = line.substring(4)
                                                }
                                                if (line.startsWith('PLANNINGTIME:')) {
                                                    planning_time = line.substring(13)
                                                }
                                                if (line.startsWith('TAKEOFFTIME:')) {
                                                    takeoff_time = line.substring(12)
                                                }
                                            }
                                            if (!line && start_num) {
                                                println "$contest,$task,$start_num,$crew,$aircraft,$result_class,$tas,$planning_time,$takeoff_time"
                                                
                                                SetPrinterData(printer_obj, "contest", contest)
                                                SetPrinterData(printer_obj, "task", task)
                                                SetPrinterData(printer_obj, "start_num", start_num)
                                                SetPrinterData(printer_obj, "crew", crew)
                                                SetPrinterData(printer_obj, "aircraft", aircraft)
                                                SetPrinterData(printer_obj, "class", result_class)
                                                SetPrinterData(printer_obj, "tas", tas)
                                                SetPrinterData(printer_obj, "planning_time", planning_time)
                                                SetPrinterData(printer_obj, "takeoff_time", takeoff_time)
                                                
                                                printer_obj.invokeMethod("StartPrint",["", 0].toArray())
                                                printer_obj.invokeMethod("PrintOut",[1, 0].toArray())
                                                printer_obj.invokeMethod("EndPrint",null)
                                                
                                                start_num = ''
                                            }
                                            if (line == null) {
                                                break
                                            }
                                        }
                                        file_reader.close()
                
                                        printer_obj.invokeMethod("Close",null)
                                    } else {
                                        print_ret.printed = false
                                        print_ret.errmsg = "'$label_template' not found. (2)"
                                    }
                                } else {
                                    print_ret.printed = false
                                    print_ret.errmsg = "'$label_template' not found. (1)"
                                }
                            }
                        }
                        catch (Exception e) {
                            print_ret.printed = false
                            print_ret.errmsg = e.getMessage()
                        }
                        
                        if (print_ret.printed) {
                            info_text.text = "<html>Done: '${selected_file}'<br/>has been successfully printed.<br/></html>"
                            info_text.foreground = java.awt.Color.BLACK
                        } else {
                            info_text.text = "<html>Error: '${selected_file}'<br/>could not be printed (${print_ret.errmsg}).<br/>"
                            info_text.foreground = java.awt.Color.RED
                            println info_text.text
                        }
                        print_button.visible = true
                    }
                }
                button text: 'Exit', actionPerformed: {
                    System.exit(0)
                }
            }
            panel(constraints: BorderLayout.SOUTH) {
                label(text:'(c) 2016 Thomas Weise, License: GPL v3, flightcontest.de')
            }
        }
    }
    
    //--------------------------------------------------------------------------
    void SetPrinterData(printerObj, String valueName, valueData)
    {
        def value_obj = printerObj.invokeMethod("GetObject",valueName)
        if (value_obj) {
            boolean exist_value = false
            try {
                def value = value_obj.getProperty("Text")
                exist_value = true
            } catch (Exception e) {
                println "  $valueName: Does not exist."
            }
            if (exist_value) {
                value_obj.setProperty("Text", valueData)
                println "  $valueName: '$valueData' has been set."
            }
        }
    }
    
}
