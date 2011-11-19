class BootStrap {

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
		
		if ((global.versionMajor != global.VERSION_MAJOR) || (global.versionMinor != global.VERSION_MINOR)) {
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
			
			global.versionMajor = global.VERSION_MAJOR
			global.versionMinor = global.VERSION_MINOR
			global.save()
			println "DB ${global.versionMajor}.${global.versionMinor} upgraded."
		}
    }
    
    def destroy = {
    }
} 