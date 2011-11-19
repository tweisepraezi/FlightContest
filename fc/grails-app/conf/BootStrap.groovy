class BootStrap {

    static Global global = null 
    
    def init = { servletContext ->
     	global = new Global()
    }
    
    def destroy = {
    }
} 