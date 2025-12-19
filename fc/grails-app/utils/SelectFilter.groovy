enum SelectFilter
{
    SelectNone     ('fc.select.none'   ),
    SelectClass1   ('fc.select.class'  ),
    SelectClass2   ('fc.select.class'  ),
    SelectClass3   ('fc.select.class'  ),
    SelectClass4   ('fc.select.class'  ),
    SelectPos1Of2  ('fc.select.pos1of2'),
    SelectPos2Of2  ('fc.select.pos2of2'),
    SelectPos1Of3  ('fc.select.pos1of3'),
    SelectPos2Of3  ('fc.select.pos2of3'),
    SelectPos3Of3  ('fc.select.pos3of3'),
    SelectPos1Of4  ('fc.select.pos1of4'),
    SelectPos2Of4  ('fc.select.pos2of4'),
    SelectPos3Of4  ('fc.select.pos3of4'),
    SelectPos4Of4  ('fc.select.pos4of4')
    
	SelectFilter(String titleCode)
	{
		this.titleCode = titleCode
	}
    
    static List GetValues(List resultClasses)
    {
        List ret = []
        for(v in values()) {
            switch (v) {
                case SelectClass1:
                    if (resultClasses.size() > 0) {
                        ret += v
                    }
                    break
                case SelectClass2:
                    if (resultClasses.size() > 1) {
                        ret += v
                    }
                    break
                case SelectClass3:
                    if (resultClasses.size() > 2) {
                        ret += v
                    }
                    break
                case SelectClass4:
                    if (resultClasses.size() > 3) {
                        ret += v
                    }
                    break
                default:
                    ret += v
                    break
            }
        }
        return ret
    }
    
    static String GetStr(SelectFilter selectFilter, List resultClasses)
    {
        switch (selectFilter) {
            case SelectFilter.SelectClass1:
                if (resultClasses.size() > 0) {
                    return resultClasses[0].name
                }
                break
            case SelectFilter.SelectClass2:
                if (resultClasses.size() > 1) {
                    return resultClasses[1].name
                }
                break
            case SelectFilter.SelectClass3:
                if (resultClasses.size() > 2) {
                    return resultClasses[2].name
                }
                break
            case SelectFilter.SelectClass4:
                if (resultClasses.size() > 3) {
                    return resultClasses[3].name
                }
                break
        }
        return ""
    }
	
	final String titleCode
}