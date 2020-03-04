
grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6

grails.project.war.file = "..\\output\\fc.war"


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


grails.dependency.cache.dir = "K:/Projects/Grails/Dependency-Cache"

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

		grailsHome() 
		grailsPlugins() 
		//mavenLocal() // ~/.m2/repository
		grailsCentral() 
		mavenCentral() 
		// uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories 
		//mavenRepo "http://repository.codehaus.org" 
		//mavenRepo "http://download.java.net/maven/2/" }

	}	
	
	dependencies { 
		// specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g. 
		//runtime 'mysql:mysql-connector-java:5.1.24' 
		//compile 'org.springframework.integration:spring-integration-core:2.2.5.RELEASE'
		
		compile 'org.xhtmlrenderer:core-renderer:R8'
		
		runtime 'com.lowagie:itext:2.0.8' // vorher 2.1.5 
	}
	 
	plugins {
		compile "org.grails.plugins:excel-import:1.0.0"
		compile "org.grails.plugins:joda-time:1.4"
        compile "org.grails.plugins:mail:1.0.7"
        compile "org.grails.plugins:quartz:1.0.2"
		//compile ":poi:3.7"
		compile "org.grails.plugins:class-diagram:0.5.2"
		compile "org.grails.plugins:db-util:0.4"
        
        // plugins for the build system only
        build "org.grails.plugins:tomcat:7.0.42"

		// plugins needed at runtime but not for compilation
		runtime "org.grails.plugins:hibernate:3.6.10.2" 
	}
}
