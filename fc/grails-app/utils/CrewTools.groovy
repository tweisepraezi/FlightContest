import org.apache.poi.hssf.usermodel.*
import org.apache.poi.xssf.usermodel.*

class CrewTools
{
    static String CREWS_SHEET = "Crews"
    static int HEADER_ROW = 7
    static int MAX_COLS = 50
    static List COLUMN_NAMES = ['startNum', 'name', 'name2', 'email', 'pilotFirstName', 'pilotLastName', 'pilotEmail', 'navFirstName', 'navLastName', 'navEmail', 'teamname', 'resultclassname', 'tas', 'registration', 'type', 'colour', 'trackerID'] 
    static List COLUMN_NAMES_1 = ['startNum', 'name', 'name2', 'email', 'teamname', 'resultclassname', 'tas', 'registration', 'type', 'colour'] 
    static List COLUMN_NAMES_2 = ['startNum', 'pilotFirstName', 'pilotLastName', 'pilotEmail', 'navFirstName', 'navLastName', 'navEmail', 'teamname', 'resultclassname', 'tas', 'registration', 'type', 'colour', 'trackerID'] 
    
    static Map ImportExcelCrews(String excelFileName, Contest contestInstance)
    {
        Map ret = [validFile:true, crewList:[]]
        
        FileInputStream fileinput_stream = new FileInputStream(excelFileName)
        def workbook = null
        if (excelFileName.toLowerCase().endsWith(".xls")) {
            workbook = new HSSFWorkbook(fileinput_stream)
        } else if (excelFileName.toLowerCase().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(fileinput_stream)
        }
        if (workbook) {
            def sheet = workbook.getSheet(CREWS_SHEET)
            if (sheet) {
                List column_list = check_header(sheet)
                if (column_list) {
                    int row_index = HEADER_ROW + 1
                    while (true) {
                        // get row
                        def row = sheet.getRow(row_index)
                        if (!row) {
                            break
                        }
                        
                        // read data
                        int col_index = 0
                        boolean empty_row = true
                        Map crew_entry = [:]
                        for (Map column in column_list) {
                            def cell = row.getCell(column.colIndex)
                            if (cell) {
                                empty_row = false
                                crew_entry[(column.colName)] = cell.toString().trim()
                            } else {
                                crew_entry[(column.colName)] = ""
                            }
                        }
                        add_crew(crew_entry, ret, contestInstance)
                        row_index++
                        if (empty_row) { // empty row
                            break
                        }    
                    }
                }
            }
        }
        fileinput_stream.close()
        
        return ret
    }
    
    private static List check_header(def sheet)
    {
        def header_row = sheet.getRow(HEADER_ROW)
        int col_index = 0
        List column_list = []
        while (true) {
            def cell = header_row.getCell(col_index)
            if (cell) {
                String col_name = cell.toString()
                if (col_name in COLUMN_NAMES) {
                    column_list += [colName:col_name, colIndex:col_index]
                }
            }
            col_index++
            if (col_index > MAX_COLS - 1) {
                break
            }
        }
        return column_list
    }
    
    private static void add_crew(Map crewEntry, Map ret, Contest contestInstance)
    {
        if (crewEntry) {
            Map crew_entry = [:]
            
            String pilot_name = ""
            if (crewEntry.name) {
                pilot_name = crewEntry.name
            } else if (crewEntry.pilotFirstName && crewEntry.pilotLastName) {
                if (contestInstance.crewSurnameForenameDelimiter) {
                    pilot_name = crewEntry.pilotLastName
                    pilot_name += contestInstance.crewSurnameForenameDelimiter
                    pilot_name += " "
                    pilot_name += crewEntry.pilotFirstName
                } else {
                    pilot_name = crewEntry.pilotFirstName
                    pilot_name += " "
                    pilot_name += crewEntry.pilotLastName
                }
            }
            if (!pilot_name) {
                return
            }
            
            String nav_name = ""
            if (crewEntry.name2) {
                nav_name = crewEntry.name2
            } else if (crewEntry.navFirstName && crewEntry.navLastName) {
                if (contestInstance.crewSurnameForenameDelimiter) {
                    nav_name = crewEntry.navLastName
                    nav_name += contestInstance.crewSurnameForenameDelimiter
                    nav_name += " "
                    nav_name += crewEntry.navFirstName
                } else {
                    nav_name = crewEntry.navFirstName
                    nav_name += " "
                    nav_name += crewEntry.navLastName
                }
            }
            
            crew_entry.name = pilot_name
            if (nav_name) {
                crew_entry.name += contestInstance.crewPilotNavigatorDelimiter
                crew_entry.name += " "
                crew_entry.name += nav_name
            }
            
            crew_entry.email = crewEntry.pilotEmail
            if (crewEntry.navEmail) {
                if (crew_entry.email) {
                    crew_entry.email += Defs.EMAIL_LIST_SEPARATOR
                }
                crew_entry.email += crewEntry.navEmail
            }

            crew_entry.startNum = crewEntry.startNum
            crew_entry.teamname = crewEntry.teamname
            crew_entry.resultclassname = crewEntry.resultclassname
            crew_entry.tas = crewEntry.tas
            crew_entry.registration = crewEntry.registration
            crew_entry.type = crewEntry.type
            crew_entry.colour = crewEntry.colour
            crew_entry.trackerID = crewEntry.trackerID
            
            ret.crewList += crew_entry
        }
    }
}