
hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = true
	cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}

// environment specific settings
//   dbCreate: enable one of 'create', 'create-drop','update'
environments {
	lastdb {
		dataSource {
			dbCreate = "update" // last database
			url = "jdbc:h2:file:~/.grails/.fc-dev/fcdb" // BUG: mem verursacht Fehler
            pooled = true
            driverClassName = 'org.h2.Driver'
            username = 'sa'
            password = ''
		}
        dataSource_aflos {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS'
            readOnly = true
        }
        
        dataSource_aflostest {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS-TEST'
            readOnly = true
        }
        
        dataSource_aflosupload {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS-UPLOAD'
            readOnly = true
        }
	}
	development {
		dataSource {
			dbCreate = "create-drop" // empty database 
			url = "jdbc:h2:file:~/.grails/.fc-dev/fcdb" // BUG: mem verursacht Fehler
            pooled = true
            driverClassName = 'org.h2.Driver'
            username = 'sa'
            password = ''
		}
        dataSource_aflos {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS'
            readOnly = true
        }
        
        dataSource_aflostest {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS-TEST'
            readOnly = true
        }
        
        dataSource_aflosupload {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS-UPLOAD'
            readOnly = true
        }
	}
	test {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:file:~/.grails/.fc-test/fcdb"
            pooled = true
            driverClassName = 'org.h2.Driver'
            username = 'sa'
            password = ''
		}
        dataSource_aflos {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS'
            readOnly = true
        }
        
        dataSource_aflostest {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS-TEST'
            readOnly = true
        }
        
        dataSource_aflosupload {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS-UPLOAD'
            readOnly = true
        }
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:file:fcdb"
            pooled = true
            driverClassName = 'org.h2.Driver'
            username = 'sa'
            password = ''
		}
        dataSource_aflos {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS'
            readOnly = true
        }
        
        dataSource_aflostest {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS-TEST'
            readOnly = true
        }
        
        dataSource_aflosupload {
            pooled = false
            dialect = org.hibernate.dialect.SQLServerDialect    
            driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
            username = ''
            password = ''
            url = 'jdbc:odbc:FC-AFLOS-UPLOAD'
            readOnly = true
        }
	}
    cloudfoundry {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:file:fcdb"
            pooled = true
            driverClassName = 'org.h2.Driver'
            username = 'sa'
            password = ''
        }
    }
}
