import org.grails.plugins.excelimport.AbstractExcelImporter
import org.grails.plugins.excelimport.*

class CrewExcelImporter extends AbstractExcelImporter 
{
	static int CONFIG_CREWS_COLUMN_NUM = 9 // A...I
	
	static Map CONFIG_CREWS_COLUMN_MAP = [ 
		sheet:'Crews', 
		startRow: 7, 
		columnMap: [ 'A':'startNum', 'B':'name', 'C':'name2', 'D':'teamname', 'E':'resultclassname', 'F':'tas', 'G':'registration', 'H':'type', 'I':'colour' ] 
	]

	public CrewExcelImporter(String fileName)
	{ 
		super(fileName) 
	}
}