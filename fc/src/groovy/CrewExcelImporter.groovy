import org.grails.plugins.excelimport.AbstractExcelImporter
import org.grails.plugins.excelimport.*

class CrewExcelImporter extends AbstractExcelImporter 
{
	static int CONFIG_CREWS_COLUMN_NUM = 8
	
	static Map CONFIG_CREWS_COLUMN_MAP = [ 
		sheet:'Crews', 
		startRow: 7, 
		columnMap: [ 'A':'name', 'B':'name2', 'C':'teamname', 'D':'resultclassname', 'E':'tas', 'F':'registration', 'G':'type', 'H':'colour' ] 
	]

	public CrewExcelImporter(String fileName)
	{ 
		super(fileName) 
	}
}