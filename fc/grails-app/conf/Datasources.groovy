datasources = {

	datasource(name: 'aflos') {
		domainClasses([AflosRouteNames,AflosRouteDefs,AflosCrewNames,AflosErrors,AflosCheckPoints,AflosErrorPoints])
		readOnly(true)
		
		driverClassName('sun.jdbc.odbc.JdbcOdbcDriver')
        dialect(org.hibernate.dialect.SQLServerDialect) // https://www.hibernate.org/hib_docs/v3/api/org/hibernate/dialect/package-summary.html

        url('jdbc:odbc:FC-AFLOS')
        username('')
		password('')

		//dbCreate('')
		//logSql(true)

        hibernate {
			cache {
				use_second_level_cache(false)
				use_query_cache(false)
			}
		}
	}
}
