// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }
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
// The default codec used to encode data with ${}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
grails.converters.encoding="UTF-8"

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

// set per-environment serverURL stem for creating absolute links
environments {
    production {
		
    	// disable stacktrace.log
    	/*
    	log4j = {
			appenders {
				null name:'stacktrace'
            }
        }
    	*/
    	
		grails.serverURL = "http://www.changeme.com"
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

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
	       'org.codehaus.groovy.grails.web.pages', //  GSP
	       'org.codehaus.groovy.grails.web.sitemesh', //  layouts
	       'org.codehaus.groovy.grails."web.mapping.filter', // URL mapping
	       'org.codehaus.groovy.grails."web.mapping', // URL mapping
	       'org.codehaus.groovy.grails.commons', // core / classloading
	       'org.codehaus.groovy.grails.plugins', // plugins
	       'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
	       'org.springframework',
	       'org.hibernate'

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

     