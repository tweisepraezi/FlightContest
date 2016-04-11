import groovy.swing.*
import javax.swing.*
import javax.swing.WindowConstants as WC
import javax.swing.SwingConstants as SC
import javax.swing.filechooser.FileFilter
import java.awt.*
import java.awt.BorderLayout as BL

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
    def convert_button
    String selected_file = ''
    String converted_file = ''
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
        if (new File('src/gpx2gac.png').exists()) {
            image_icon = swing.imageIcon('src/gpx2gac.png').image
        } else { // from jar
            image_icon = swing.imageIcon('/gpx2gac.png').image
        }
        frame = swing.frame(title: 'Flight Contest GPX GAC Converter 1.0',
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
                convert_button = button text: 'Convert GPX...', actionPerformed: {
                    def file_dlg = fileChooser(dialogTitle: 'Choose a GPX file',
                                               id: 'openGpxDialog',
                                               currentDirectory: new File(current_directory),
                                               fileSelectionMode: JFileChooser.FILES_ONLY,
                                               fileFilter: [getDescription: {-> '*.gpx'}, accept:{file-> file ==~ /.*?\.gpx/ || file.isDirectory() }] as FileFilter ) {}
                    if (file_dlg.showOpenDialog() != JFileChooser.APPROVE_OPTION) {
                        info_text.text = "<html><br/><br/><br/></html>"
                        return
                    }
                    
                    // get file names
                    selected_file = file_dlg.selectedFile.getAbsolutePath()
                    converted_file = selected_file.replace('.gpx','.gac')
                    int converted_file_num = 0
                    String converted_file_base = converted_file.replace('.gac', '')
                    while (new File(converted_file).exists()) {
                        converted_file_num++
                        converted_file = "${converted_file_base}_${converted_file_num}.gac"
                    }
                    current_directory = selected_file.substring(0, selected_file.lastIndexOf('\\'))
                    
                    // convert
                    info_text.text = "<html>Converting '${selected_file}'<br/>-> '${converted_file}'...</html>"
                    info_text.foreground = java.awt.Color.BLUE
                    convert_button.visible = false
                    
                    doOutside {
                        Map convert_ret = GPX2GAC.Convert(selected_file, converted_file)
                        if (convert_ret.converted) {
                            info_text.text = "<html>Done: '${selected_file}'<br/>-> '${converted_file}'<br/>has been successfully converted.<br/></html>"
                            info_text.foreground = java.awt.Color.BLACK
                        } else {
                            if (!convert_ret.onetrack) {
                                info_text.text = "<html>Error: '${selected_file}'<br/>-> '${converted_file}'<br/>could not be converted: More than one track.<br/>"
                            } else {
                                info_text.text = "<html>Error: '${selected_file}'<br/>-> '${converted_file}'<br/>could not be converted (${convert_ret.errmsg}).<br/>"
                            }
                            info_text.foreground = java.awt.Color.RED
                            new File(converted_file).delete()
                        }
                        convert_button.visible = true
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
}
