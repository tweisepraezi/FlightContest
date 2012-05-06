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
environments {
	development {
		dataSource {
			dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			//url = "jdbc:hsqldb:mem:devDB"
			//url = "jdbc:h2:mem:fcdb"
			url = "jdbc:h2:file:~/.grails/.fc/dev" // BUG: mem verursacht Fehler
		}
	}
	test {
		dataSource {
			dbCreate = "update"
			//url = "jdbc:hsqldb:file:fcdb;shutdown=true"
			//url = "jdbc:h2:mem:testDb"
			url = "jdbc:h2:file:~/.grails/.fc/test"
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			//url = "jdbc:hsqldb:file:${userHome}/.${appName}/data;shutdown=true"
			//url = "jdbc:hsqldb:file:d:/.${appName}/fcdb;shutdown=true"
			//url = "jdbc:hsqldb:file:fcdb;shutdown=true"
			//url = "jdbc:h2:prodDb"
			url = "jdbc:h2:file:fcdb"
		}
	}
}