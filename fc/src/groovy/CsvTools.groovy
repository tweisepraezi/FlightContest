class CsvTools
{
    // Development options
    private final static boolean LOG_LOAD_DATA = false // false
    
    // ----------------------------------------------------------------------------------
    static Map LoadData(String fileName, String csvDelimiter, Map fieldValues, Map filterValues, List lowerCaseFields)
    // Erste Zeile: Feldnamen
    {
        Map data_list = [:]
        filterValues.each { data_key, filter_values ->
            data_list += [(data_key):[]]
        }
        String[] field_names
        
        File file = new File(fileName)
        if (file.exists()) {
            boolean first_line = true
            file.eachLine {
                if (first_line) {
                    field_names = it.split(csvDelimiter)
                    if (LOG_LOAD_DATA) {
                        println "CSV-Felder: ${field_names.size()} $field_names"
                    }
                    first_line = false
                } else {
                    if (it) {
                        String[] values = it.split(csvDelimiter)
                        if (LOG_LOAD_DATA) {
                            // println "${values.size()} $values"
                        }
                        
                        if (values.size() != field_names.size()) {
                            if (LOG_LOAD_DATA) {
                                println "Repair: $values"
                            }
                            String s = ""
                            for (String value in values) {
                                if (value.startsWith('"')) {
                                    if (s) {
                                        s += csvDelimiter
                                    }
                                    s += value.substring(1)
                                    s += ","
                                } else if (value.endsWith('"')) {
                                    s += value.substring(0,value.size()-1)
                                } else {
                                    if (s) {
                                        s += csvDelimiter
                                    }
                                    s += value
                                }
                            }
                            values = s.split(csvDelimiter)
                            if (LOG_LOAD_DATA) {
                                println "Done:   $values"
                            }
                        }
                        
                        Map datum = [:]
                        Map add_datum = [:]
                        filterValues.each { data_key, filter_values ->
                            datum += [(data_key):[:]]
                            add_datum += [(data_key):false]
                        }
                        if (LOG_LOAD_DATA) {
                            println "Init datum: $datum"
                            println "Init add_datum: $add_datum"
                        }
                        
                        int i = 0
                        for (String value in values) {
                            if (i < field_names.size()) {
                                filterValues.each { data_key, filter_values ->
                                    fieldValues.each { field_key, field_value ->
                                        if (field_names[i] == field_key) {
                                            if (field_value.toString() in lowerCaseFields) {
                                                datum.(data_key.toString()).(field_value.toString()) = value.toLowerCase()
                                            } else {
                                                datum.(data_key.toString()).(field_value.toString()) = value
                                            }
                                        }
                                    }
                                    filter_values.each { filter_values2 ->
                                        filter_values2.each { filter_key, filter_value ->
                                            if (field_names[i] == filter_key) {
                                                if (filter_value.toString() == value) {
                                                    add_datum.(data_key.toString()) = true
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (LOG_LOAD_DATA) {
                                    println "Problem1: $i ${values.size()} $values"
                                }
                            }
                            i++
                        }
                        
                        filterValues.each { data_key, filter_values ->
                            if (add_datum.(data_key.toString())) {
                                data_list.(data_key.toString()) += datum.(data_key.toString())
                                if (LOG_LOAD_DATA) {
                                    println "Add datum to ${data_key}: ${datum.(data_key.toString())}"
                                }
                            }
                        }
                    }
                }
            }
        }
        if (LOG_LOAD_DATA) {
            println "${data_list}."
        }
        return data_list
    }

}
