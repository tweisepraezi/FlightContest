class TempData
{
    private List data_list = []
    
    // --------------------------------------------------------------------------------------------------------------------
    void AddData(String dataName, dataValue)
    {
        Map data = [name:dataName, value:dataValue]
        data_list += data
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean RemoveData(String dataName)
    {
        for (int i = 0; i < data_list.size(); i++) {
            if (data_list[i].name == dataName) {
                data_list.remove(i)
                return true
            }
        }
        return false
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def GetData(String dataName)
    {
        for (int i = 0; i < data_list.size(); i++) {
            if (data_list[i].name == dataName) {
                return data_list[i].value
            }
        }
        return null
    }
}
