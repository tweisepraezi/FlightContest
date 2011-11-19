import java.util.List;

class BootStrap {

	def messageSource
	def grailsApplication
	
    static Global global = null 
    
    def init = { servletContext ->
		
		boolean db_loaded = false
		boolean db_upgrade = false
		
		if (Global.count() == 0) {
			global = new Global()
			global.save()
		} else {
			global = Global.findByVersionMajorIsNotNull()
			db_loaded = true
		}
		
		if ((global.versionMajor != global.DB_MAJOR) || (global.versionMinor != global.DB_MINOR)) {
			db_upgrade = true
		}
		
		if (db_loaded) {
			println "DB ${global.versionMajor}.${global.versionMinor} loaded."
		} else {
			println "DB ${global.versionMajor}.${global.versionMinor} created."
		}
		
		// DB upgrade
		if (db_upgrade) {
			println "Upgrade"
			
			global.versionMajor = global.DB_MAJOR
			global.versionMinor = global.DB_MINOR
			global.showLanguage = "de"
			global.save()
			println "DB ${global.versionMajor}.${global.versionMinor} upgraded."
		}
		
		// add method getMsg to all domain classes
		grailsApplication.domainClasses.each { domain_class ->
			domain_class.metaClass.getMsg = {
	            return messageSource.getMessage(it, null, new Locale(global.showLanguage))
			}
		}
    }
    
    def destroy = {
    }
} 