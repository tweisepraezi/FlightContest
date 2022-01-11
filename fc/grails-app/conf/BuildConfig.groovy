
grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6

grails.project.war.file = ".\\output\\fc.war"


grails.project.fork = [ 
	// configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required 
	// compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

	// configure settings for the test-app JVM, uses the daemon by default 
	test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true], 
	// configure settings for the run-app JVM
	run: [maxMemory: 4096, minMemory: 1024, debug: false, maxPerm: 256, forkReserve:false], 
        // FC: maxMemory: 4096, minMemory: 1024
	// configure settings for the run-war JVM 
	war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false], 
	// configure settings for the Console UI JVM 
	console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256] 
]


grails.dependency.cache.dir = "./dependency-cache"

grails.project.dependency.resolver = "maven" // or ivy

grails.project.dependency.resolution = {
	
	// inherit Grails' default dependencies
	inherits("global") {
		// specify dependency exclusions here; for example, uncomment this to disable ehcache:
		// excludes 'ehcache'
	}
	
	log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
	checksums true // Whether to verify checksums on resolve
	legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility
	
	repositories { 
		//inherits true // Whether to inherit repository definitions from plugins
        mavenRepo "https://repo1.maven.org/maven2" // mavenCentral()
        mavenRepo "https://repo.grails.org/grails/core" // Grails
        mavenRepo "https://repo.grails.org/grails/plugins" // Grails plugins
	}	
	
	dependencies { 
		// specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g. 
		//runtime "mysql:mysql-connector-java:5.1.24" 
		//compile "org.springframework.integration:spring-integration-core:2.2.5.RELEASE"
        
        compile "commons-net:commons-net:3.7.2" // https://repo1.maven.org/maven2/commons-net/commons-net
		
		compile "org.xhtmlrenderer:core-renderer:R8" // https://repo1.maven.org/maven2/org/xhtmlrenderer/core-renderer
        
        // excel import
        compile "org.apache.poi:poi:4.1.2" // https://repo1.maven.org/maven2/org/apache/poi/
        compile "org.apache.poi:poi-ooxml:4.1.2" // https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml/
        
		runtime "com.lowagie:itext:2.0.8" // https://repo1.maven.org/maven2/com/lowagie/itext, vorher 2.1.5
        
        compile "org.gdal:gdal:3.2.0" // https://repo1.maven.org/maven2/org/gdal/gdal
        //compile "org.python:jython:2.7.2" // https://repo1.maven.org/maven2/org/python/jython
        
        //test "org.grails:grails-datastore-test-support:1.0.2-grails-2.4"
        
        compile "com.fazecast:jSerialComm:2.8.0" // https://repo1.maven.org/maven2/com/fazecast/jSerialComm
	}
	 
	plugins {
        // plugins for the compile step
        //compile ":scaffolding:2.1.2"
        // compile ":cache:1.1.8"
        // asset-pipeline 2.0+ requires Java 7, use version 1.9.x with Java 6
        // compile ":asset-pipeline:2.5.7"
        
		compile "org.grails.plugins:joda-time:1.5" // https://repo.grails.org/grails/plugins/org/grails/plugins/joda-time/
        compile "org.grails.plugins:mail:1.0.7" // https://repo.grails.org/grails/plugins/org/grails/plugins/mail/
        compile "org.grails.plugins:quartz:1.0.2" // https://repo.grails.org/grails/plugins/org/grails/plugins/quartz/
		
		//compile "org.grails.plugins:class-diagram:0.5.2"
		//compile "org.grails.plugins:db-util:0.4"
		//compile "org.grails.plugins:excel-import:1.0.0"
        
        // plugins for the build system only
        //build "org.grails.plugins:tomcat:7.0.42"
        build "org.grails.plugins:tomcat:8.0.22" // https://repo.grails.org/grails/plugins/org/grails/plugins/tomcat/

		// plugins needed at runtime but not for compilation
		// runtime "org.grails.plugins:hibernate:3.6.10.2" 
        runtime "org.grails.plugins:hibernate4:4.3.10" // https://repo.grails.org/grails/plugins/org/grails/plugins/hibernate/
	}
}
