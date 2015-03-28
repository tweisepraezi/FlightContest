// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// FC: User specific configuration (Mail, Upload, etc.)
grails.config.locations = [ "file:C:/FCSave/.fc/config.groovy",
                            "file:C:/FCSave/.fc/migrate_db.groovy"
                          ]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// FC: Leere Strings nicht null setzen (Default: true)
grails.databinding.convertEmptyStringsToNull = false

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "" // must be empty, for use from external pc, old was: http://localhost:8080/${appName}
    }
    development {
        grails.serverURL = "" // must be empty, for use from external pc, old was: http://localhost:8080/${appName}
    }
    test {
        grails.serverURL = "" // must be empty, for use from external pc, old was: http://localhost:8080/${appName}
    }
	lastdb {
        grails.serverURL = "" // must be empty, for use from external pc, old was: http://localhost:8080/${appName}
	}
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}
	
	// disable stacktrace.log
	appenders {
		'null' name:'stacktrace'
    }
	
    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
	       'org.codehaus.groovy.grails.web.pages', //  GSP
	       'org.codehaus.groovy.grails.web.sitemesh', //  layouts
	       'org.codehaus.groovy.grails."web.mapping.filter', // URL mapping
	       'org.codehaus.groovy.grails."web.mapping', // URL mapping
	       'org.codehaus.groovy.grails.commons', // core / classloading
	       'org.codehaus.groovy.grails.plugins', // plugins
	       'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
	       'org.springframework',
	       'org.hibernate',
		   'net.sf.ehcache.hibernate'
		   
    warn   'org.mortbay.log'
}

// class-diagrm configuration
graphviz {
	dot.executable = "dot" // include full file path if not on path
}

classDiagram {
	preferences {
		defaults {
			skin = "regular"
			outputFormat = "png"    // Should be an image format from http://www.graphviz.org/doc/info/output.html
			showProperties = true
			showMethods = true
			showAssociationNames = true
			showMethodReturnType = true
			showMethodSignature = false
			showPropertyType = true
			showEmbeddedAsProperty = false
			showPackages = false
			autoUpdate = true
			classSelection = "<all>"
			classSelectionIsRegexp = false
			graphOrientation = "LR" 
			  // http://www.graphviz.org/doc/info/attrs.html#k:rankdir
 			  // "TB", "LR", "BT", "RL", corresponding to directed graphs drawn from top to bottom, from left to right, from bottom to top, and from right to left, respectively.
			fontsize = 9
		}
	}
	associations {
		arrows {
			// See http://www.graphviz.org/doc/info/arrows.html for available arrowheads and their visual appearance
			references = "open"
			belongsTo = "odiamond"
			embedded = "diamond"
			inherits = "onormal"
			none = "none"
		}
		decorators {
			// plain text to be shown on edge ends
			hasOne = "1"
			hasMany = "*"
			none = ""
		}
	}
	skins {
		// See http://graphviz.org/doc/info/attrs.html for available properties on graph, node and edge.
		// You may use any property except the 'shape' property (it is set internally to [shape:'record']).
		// Also, if you use the fontsize property, the gui size slider will not be able to override it.
		classic {
			name = "Classic"
			graphStyle = [bgcolor:"white"]
			nodeStyle = [style:"rounded,filled", color:"blue", fillcolor:"azure2", fontname:"Verdana"]
			edgeStyle = [color:"gray40", fontname:"Verdana"]
			packageStyle = [style:"rounded,filled", color:"gray95", fontname:"Verdana"]
		}
		classicSpaced {
			name = "Classic Spaced"
			graphStyle = [bgcolor:"white", mclimit:100, nodesep:'0.75 equally', ranksep:'0.75 equally']
			nodeStyle = [style:"rounded,filled", color:"blue", fillcolor:"azure2", fontname:"Verdana", fontsize:18]
			edgeStyle = [color:"gray40", fontname:"Verdana", fontsize:18, labelfontsize:20, labeldistance:3.5]
			packageStyle = [style:"rounded,filled", color:"gray95"]
		}
		regular {
			name = "Regular"
			graphStyle = [bgcolor:"white"]
			nodeStyle = [style:"filled", color:"lightyellow3", fillcolor:"lightyellow", fontname:"Verdana"]
			edgeStyle = [color:"gray40", fontname:"Verdana"]
			packageStyle = [style:"filled", color:"gray95", fontname:"Verdana"]
		}
		white {
			name = "White on Gray"
			graphStyle = [bgcolor:"gray90"]
			nodeStyle = [style:"filled", color:"gray40", fillcolor:"white", fontname:"Verdana"]
			edgeStyle = [color:"gray40", fontname:"Verdana"]
			packageStyle = [style:"", color:"gray40", fontname:"Verdana"]
		}
		gray {
			name = "Gray"
			graphStyle = [bgcolor:"white"]
			nodeStyle = [style:"filled", color:"gray40", fillcolor:"gray90", fontname:"Verdana"]
			edgeStyle = [color:"gray40", fontname:"Verdana"]
			packageStyle = [style:"filled", color:"gray95", fontname:"Verdana"]
		}
	}
	legend {
		style {
			graphStyle = [bgcolor:"gray94", margin:"0,0", size:"7,7"]
			nodeStyle = [style:"filled", color:"gray50", fillcolor:"white", margin:"0,0", fontsize:15, fontname:"Verdana"]
			edgeStyle = [color:"gray50", fontsize:15, fontname:"Verdana"]
			packageStyle = [style:"filled", color:"gray94", fontname:"Verdana"]
		}
	}
}

     
// Uncomment and edit the following lines to start using Grails encoding & escaping improvements

/* remove this line 
// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside null
                scriptlet = 'none' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}
remove this line */
