import org.grails.plugins.excelimport.AbstractExcelImporter
import org.grails.plugins.excelimport.*

class CrewExcelImporter extends AbstractExcelImporter 
{
	static int CONFIG_CREWS_COLUMN_NUM = 6
	
	static Map CONFIG_CREWS_COLUMN_MAP = [ 
		sheet:'Crews', 
		startRow: 7, 
		columnMap: [ 'A':'name', 'B':'country', 'C':'tas', 'D':'registration', 'E':'type', 'F':'colour' ] 
	]

	public CrewExcelImporter(String fileName)
	{ 
		super(fileName) 
	}

	List getCrews() 
	{
		List ret_list = []
		int i = 0
		List crew_list = ExcelImportUtils.convertColumnMapConfigManyRows(workbook, CONFIG_CREWS_COLUMN_MAP)
		for (Map crew_entry in crew_list) {
			if (i == 0) {
				int col_num = 0
				crew_entry.each { key, value ->
					if (key == value) {
						col_num++
					}
				}
				if (col_num != CONFIG_CREWS_COLUMN_NUM) {
					return []
				}
			} else {
				Map new_entry = [:]
				CONFIG_CREWS_COLUMN_MAP.columnMap.each { key, value ->
					new_entry += [(value):""]
				}
				crew_entry.each { key, value ->
					if (value) {
						new_entry[(key)] = value.toString()
					}
				}
				ret_list += new_entry
			}
			i++
		}
		return ret_list
	}

}