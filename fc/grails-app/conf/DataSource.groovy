dataSource {
	pooled = true
	//driverClassName = "org.hsqldb.jdbcDriver"
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
	//logSql = true
	properties {
	}
}

dataSource_aflostest {
	pooled = false
	dialect = org.hibernate.dialect.SQLServerDialect	
	driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
	username = ''
	password = ''
	url = 'jdbc:odbc:FC-AFLOS-TEST'
	readOnly = true
	//logSql = true
	properties {
	}
}

dataSource_aflosupload {
	pooled = false
	dialect = org.hibernate.dialect.SQLServerDialect	
	driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver"
	username = ''
	password = ''
	url = 'jdbc:odbc:FC-AFLOS-UPLOAD'
	readOnly = true
	//logSql = true
	properties {
	}
}

hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = true
	cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}

// environment specific settings
//   dbCreate: enable one of 'create', 'create-drop','update'
environments {
	development {
		dataSource {
			dbCreate = "create-drop" // empty database 
			//dbCreate = "update" // last database
			url = "jdbc:h2:file:~/.grails/.fc-dev/fcdb" // BUG: mem verursacht Fehler
			//url = "jdbc:hsqldb:mem:devDB"
			//url = "jdbc:h2:mem:fcdb"
		}
	}
	test {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:file:~/.grails/.fc-test/fcdb"
			//url = "jdbc:hsqldb:file:fcdb;shutdown=true"
			//url = "jdbc:h2:mem:testDb"
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:file:fcdb"
			//url = "jdbc:hsqldb:file:${userHome}/.${appName}/data;shutdown=true"
			//url = "jdbc:hsqldb:file:d:/.${appName}/fcdb;shutdown=true"
			//url = "jdbc:hsqldb:file:fcdb;shutdown=true"
			//url = "jdbc:h2:prodDb"
		}
	}
}