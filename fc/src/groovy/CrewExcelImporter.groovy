import org.grails.plugins.excelimport.AbstractExcelImporter
import org.grails.plugins.excelimport.*

class CrewExcelImporter extends AbstractExcelImporter 
{
	static int CONFIG_CREWS_COLUMN_NUM = 10 // A...J
	
	static Map CONFIG_CREWS_COLUMN_MAP = [ 
		sheet:'Crews', 
		startRow: 7, 
		columnMap: [ 'A':'startNum', 
                     'B':'name', 
                     'C':'name2', 
                     'D':'email',
                     'E':'teamname', 
                     'F':'resultclassname', 
                     'G':'tas', 
                     'H':'registration', 
                     'I':'type', 
                     'J':'colour' 
                   ] 
	]

    static String CONFIG_CREWS_EMPTY_VALUE = "@"
    
    static Map CONFIG_CREWS_PROPERTY_CONFIGURATION_MAP = [
        startNum:        ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue:CONFIG_CREWS_EMPTY_VALUE]),
        name:            ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue:CONFIG_CREWS_EMPTY_VALUE]),
        name2:           ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue:CONFIG_CREWS_EMPTY_VALUE]),
        email:           ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue:CONFIG_CREWS_EMPTY_VALUE]),
        teamname:        ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue:CONFIG_CREWS_EMPTY_VALUE]),
        resultclassname: ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue:CONFIG_CREWS_EMPTY_VALUE]),
        tas:             ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue:CONFIG_CREWS_EMPTY_VALUE]),
        registration:    ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue:CONFIG_CREWS_EMPTY_VALUE]),
        type:            ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue:CONFIG_CREWS_EMPTY_VALUE]),
        colour:          ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue:CONFIG_CREWS_EMPTY_VALUE]),
    ]
    
    public CrewExcelImporter(String fileName)
	{ 
		super(fileName) 
	}
}