
hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = true
	cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}

dataSource {
    properties {
        // https://tomcat.apache.org/tomcat-9.0-doc/jdbc-pool.html#Common_Attributes
        jmxEnabled = true
        initialSize = 10
        maxActive = 100
        minIdle = 10
        maxIdle = 100
        maxWait = 180000
        maxAge = 600000
        timeBetweenEvictionRunsMillis = 5000
        minEvictableIdleTimeMillis = 60000
        validationQuery = "SELECT 1"
        validationQueryTimeout = 10
        validationInterval = 15000
        testOnBorrow = true
        testWhileIdle = true
        testOnReturn = false
        jdbcInterceptors = 'ConnectionState'
        defaultTransactionIsolation = 1 
            // https://github.com/JetBrains/jdk8u_jdk/blob/master/src/share/classes/java/sql/Connection.java
            // 1 - TRANSACTION_READ_UNCOMMITTED, 2 - TRANSACTION_READ_COMMITTED, 4 - TRANSACTION_REPEATABLE_READ, 8 - TRANSACTION_SERIALIZABLE
    }
}

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
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:file:../fc/fcdb"
            pooled = true
            driverClassName = 'org.h2.Driver'
            username = 'sa'
            password = ''
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
