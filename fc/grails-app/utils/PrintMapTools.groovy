import groovy.json.JsonSlurper
import groovy.sql.*

class PrintMapTools
{
    //--------------------------------------------------------------------------
    enum DataType {
        NONE,
        JSON,
        BINARY
    }

    //--------------------------------------------------------------------------
    final static Map HEADER_ACCEPT = [name:"Accept", value:"application/vnd.api+json; charset=utf-8"]
    
    //--------------------------------------------------------------------------
    // Development options
    private final static boolean LOG_RESTAPI_CALLS = true        // true
    private final static boolean LOG_RESTAPI_OUTPUTDATA = false  // false
    private final static boolean LOG_RESTAPI_RETURNS = true      // true
    private final static boolean LOG_RESTAPI_EXCEPTIONS = true   // true

    //--------------------------------------------------------------------------
    static Map CallPrintServer(String urlPath, List headerList, String requestMethod, DataType dataType, def outputData)
    {
        Map ret = [responseCode:null, json:null, binary:null]
        if (LOG_RESTAPI_CALLS) {
            println urlPath
        }
        if (LOG_RESTAPI_OUTPUTDATA) {
            println outputData
        }
        def connection = urlPath.toURL().openConnection()
        
        connection.requestMethod = requestMethod
        
        if (headerList) {
            headerList.each {
                connection.setRequestProperty( it.name, it.value )
            }
        }
        
        //String auth_str = "${LOGIN_NAME}:${LOGIN_PASSWORD}".getBytes().encodeBase64().toString()
        //connection.setRequestProperty( "Authorization", "Basic ${auth_str}" )
        try {
            if (outputData) {
                connection.doOutput = true
                switch (dataType) {
                    case DataType.JSON:
                        byte[] output_bytes = outputData.getBytes("UTF-8")
                        OutputStream os = connection.getOutputStream()
                        os.write(output_bytes)
                        os.close()
                        break
                    case DataType.BINARY:
                        OutputStream os = connection.getOutputStream()
                        os << outputData
                        os.close()
                        break
                }
            }
            switch (dataType) {
                case DataType.JSON:
                    try {
                        String s = connection.content.text
                        s = new String(s.getBytes("ISO-8859-1"), "UTF-8")
                        ret.json = new JsonSlurper().parseText(s)
                    } catch (Exception e) {
                        println "Exception (1): ${e.getMessage()} ${e}"
                    }
                    break
                case DataType.BINARY:
                    ret.binary = connection.content
                    break
            }
            ret.responseCode = connection.responseCode
            if (LOG_RESTAPI_RETURNS) {
                switch (dataType) {
                    case DataType.JSON:
                        println "responseCode=${ret.responseCode}, data=${ret.json}"
                        break
                    case DataType.BINARY:
                        println "responseCode=${ret.responseCode}"
                        //println "${ret.responseCode} ${ret.binary}"
                        break
                    default:
                        println "responseCode=${ret.responseCode}"
                        break
                }
            }
            return ret
        } catch (Exception e) {
            if (LOG_RESTAPI_EXCEPTIONS) {
                println "Exception (2): ${e.getMessage()} ${e}"
            }
        }
        
        return ret
    }

    //--------------------------------------------------------------------------
    static boolean IsLocalPrintmapsRunning()
    {
        String url_path = Defs.PRINTMAPS_INTERN_LINK + "/capabilities/service" 
        Map status = CallPrintServer(url_path, [HEADER_ACCEPT], "GET", DataType.JSON, "")
        if (status.responseCode == 200) {
            return true
        }
        return false
    }
    
    //--------------------------------------------------------------------------
    static String GetPrintServerAPI()
    {
        if (BootStrap.global.IsLocalPrintmaps() && IsLocalPrintmapsRunning()) {
            return Defs.PRINTMAPS_INTERN_LINK
        }
        return BootStrap.global.GetPrintServerAPI()
    }
    
    //--------------------------------------------------------------------------
    static String GetPsqlExe()
    {
        String psql_exe = BootStrap.global.GetPostgrePsqlExe()
        if (!psql_exe) {
            psql_exe = Defs.EXE_PSQL
        }
        return psql_exe
    }
    
    //--------------------------------------------------------------------------
    static String GetSQLHost()
    {
        String sql_host = BootStrap.global.GetPostgreSQLHost()
        if (!sql_host) {
            sql_host = Inet4Address.getLocalHost().getHostAddress()
        }
        return sql_host
    }
    
    //--------------------------------------------------------------------------
    static String GetSQLPort()
    {
        String sql_port = BootStrap.global.GetPostgreSQLPort()
        if (!sql_port) {
            String sql_host = GetSQLHost()
            if (is_sqlport_available(sql_host, 5432)) {
                return "5432"
            }
            if (is_sqlport_available(sql_host, 5433)) {
                return "5433"
            }
            return ""
        }
        return sql_port
    }
    
    //--------------------------------------------------------------------------
    static private boolean is_sqlport_available(String sqlHost, int sqlPort)
    {
        Socket socket = null;
        try {
            socket = new Socket(sqlHost, sqlPort)
            return true
        } catch (IOException e) {
        } finally {
            if (socket != null) {
                try {
                    socket.close()
                } catch (IOException e) {
                }
            }
        }            
        return false
    }
    
    //--------------------------------------------------------------------------
    static String GetSQLUserName()
    {
        String sql_username = BootStrap.global.GetPostgreSQLUsername()
        if (!sql_username) {
            sql_username = "postgres"
        }
        return sql_username
    }
    
    //--------------------------------------------------------------------------
    static String GetSQLPassword()
    {
        return BootStrap.global.GetPostgreSQLPassword()
    }
    
    //--------------------------------------------------------------------------
    static String GetContourSources()
    {
        String contour_sources = BootStrap.global.GetContourSources()
        if (!contour_sources) {
            contour_sources = "view3"
        }
        return contour_sources
    }
    
    //--------------------------------------------------------------------------
    static boolean IsLocalPrintmapsStartable(Contest contestInstance)
    {
        println "IsLocalPrintmapsStartable"
        
        String gis_database = "gis_${contestInstance.contestUUID}"
        String psql_call = """ "${GetPsqlExe()}" postgresql://${GetSQLUserName()}:${GetSQLPassword()}@${GetSQLHost()}:${GetSQLPort()}/${gis_database}"""
        psql_call += """ -c "SELECT osm_id from public.planet_osm_line limit 1;" """
        println "  Excecute $psql_call"
        def p = psql_call.execute()
        p.waitFor()
        println "  Done: ${p.exitValue()}."
        if (p.exitValue() == 0) {
            println "Yes"
            return true
        }
        println "No"
        return false
    }
    
    //--------------------------------------------------------------------------
    static String GetLocalPrintmapsDate(Contest contestInstance)
    {
        println "GetLocalPrintmapsDate"
        
        String ready_database = "ready_${contestInstance.contestUUID}"
        String psql_call = """ "${GetPsqlExe()}" postgresql://${GetSQLUserName()}:${GetSQLPassword()}@${GetSQLHost()}:${GetSQLPort()}/${ready_database}"""
        psql_call += """ -c "SELECT PBFDATE from public.Info;" """
        println "  Excecute $psql_call"
        def p = psql_call.execute()
        p.waitFor()
        println "  Done: ${p.exitValue()}."
        if (p.exitValue() == 0) {
            int i = 0
            for (String s in p.getText().split('\n')) {
                i++
                if (i == 3) { // Date in line 3
                    return s.trim()
                }
            }
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    static void Sql(String dbName)
    {
        String connection_str = "jdbc:postgresql://${GetSQLHost()}:${GetSQLPort()}/${dbName}"
        println "Sql '${connection_str}'"
        try {
            groovy.sql.Sql.loadDriver("org.postgresql.Driver")
            def sql = groovy.sql.Sql.newInstance(connection_str, GetSQLUserName(), GetSQLPassword(), "org.postgresql.Driver") // "java.sql.Driver")
            def query = "SELECT osm_id from public.planet_osm_line limit 1;"
            Map ret = [:]
            GroovyRowResult result = sql.firstRow(query)
            Set keys = result.keySet()
            for (int i = 0; i < result.size(); i++) {
                println "YY ${result[i]}"
            }
            sql.close()
        } catch (Exception e) {
            println "Exception ${e.getMessage()}"
        }
    }

}
