import grails.util.Environment
import groovy.text.SimpleTemplateEngine

class Tools {

    // --------------------------------------------------------------------------------------------------------------------
    static boolean IsDevelopmentEnvironment()
    {
        if (Environment.currentEnvironment == Environment.DEVELOPMENT || Environment.currentEnvironment == Environment.TEST) { // || grails.util.GrailsUtil.getEnvironment().equals("lastdb")
            return true
        }
    }

	// --------------------------------------------------------------------------------------------------------------------
	static Map GetMap(String mapAsString) {
		//  {p1=Wert 1, p2=Wert 2, p3=Wert 3, p4=Wert 4} -> Map
		Map map = [:]
		for (def v in mapAsString[1..-2].split(', ')) {
			def pair = v.split('=')
			map += [(pair.first()): pair.last()]
		}
		return map
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static List Split(String inputStr, String delimiterValue) {
		// Split inputStr with delimiterValue to a list of strings 
		// Empty values will generate empty list enries.
		List ret = []
		if (inputStr) {
			if (!inputStr.contains(delimiterValue)) {
				ret += inputStr
			} else {
				int start_pos = 0
				while (true) {
					int pos = inputStr.indexOf(delimiterValue, start_pos)
					if (pos < 0) {
						ret += inputStr.substring(start_pos)
						break
					}
					ret += inputStr.substring(start_pos, pos)
					pos += delimiterValue.size()
					start_pos = pos
				}
			}
		}
		return ret
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static boolean IsAllowedChars(String strValue, String allowedChars) {
		boolean is_allowed = true
		strValue.each { String ch ->
			boolean found = false
			allowedChars.each {
				if (ch.toLowerCase() == it.toLowerCase()) {
					found = true
				}
			}
			if (!found) {
				is_allowed = false
			}
		}
		return is_allowed
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static boolean IsAllowedFirstChar(String strValue, String allowedChars) {
		if (strValue) {
			return IsAllowedChars(strValue.substring(0,1), allowedChars)
		}
		return true
	}

	// --------------------------------------------------------------------------------------------------------------------
	static String GetAllowedChars(String strValue, Map keyDef, boolean withKeyDelimiter = false) {
		String str_value = ""
		if (strValue.size() > keyDef.maxSize) {
			strValue = strValue.substring(0, keyDef.maxSize)
		}
		boolean first = true
		strValue.each { String ch ->
			boolean allowed = false
			String allowed_chars = keyDef.allowedChars
			if (withKeyDelimiter) {
				allowed_chars += Defs.KEY_DELIMITER
			}
			allowed_chars.each {
				if (!allowed) {
					if (ch.toLowerCase() == it.toLowerCase()) {
						allowed = true
					}
				}
			}
			if (first) {
				if (allowed) {
					allowed = false
					keyDef.allowedFirstChars.each {
						if (!allowed) {
							if (ch.toLowerCase() == it.toLowerCase()) {
								allowed = true
							}
						}
					}
				}
				if (allowed) {
					first = false
				}
			}
			if (allowed) {
				str_value += ch
			} else if (strValue.size() == 1) {
				str_value += (int)ch
			}
		}
		return str_value
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static String RemoveUnallowedChars(String strValue, String unallowedChars) {
		String str_value = ""
		strValue.each { String ch ->
			boolean allowed = true
			unallowedChars.each {
				if (allowed) {
					if (ch == it) {
						allowed = false
					}
				}
			}
			if (allowed) {
				str_value += ch
			}
		}
		return str_value
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static String RemoveFirstCharsUntilAllowedFirstChar(String strValue, String allowedFirstChars) {
		String str_value = ""
		boolean first = true
		strValue.each { String ch ->
			if (first) {
				allowedFirstChars.each {
					if (ch.toLowerCase() == it.toLowerCase()) {
						first = false
					}
				}
			}
			if (!first) {
				str_value += ch
			}
		}
		return str_value
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static boolean IsValidMultipleMail(String mailValue) {
		for (String mail_value in Split(mailValue, Defs.MAIL_MULTIPLE_DELIMITER)) {
			if (!IsValidMail(mail_value)) {
				return false
			}
		}
		return true
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static boolean IsValidMail(String mailValue) {
		if (!mailValue.contains(Defs.MAIL_AT_CHAR)) {
			return false
		}
		if (mailValue.indexOf(Defs.MAIL_AT_CHAR) != mailValue.lastIndexOf(Defs.MAIL_AT_CHAR)) {
			return false
		}
		if (!IsAllowedChars(mailValue, Defs.MAIL_ALLOWED_CHARS)) {
			return false
		}
		if (!IsAllowedFirstChar(mailValue, Defs.MAIL_ALLOWED_FIRSTCHAR)) {
			return false
		}
		return true
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static String ReplaceVariable(String value, Map param) {
		def engine = new SimpleTemplateEngine()
		def template = engine.createTemplate(value)
		return template.make(param)
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static String GetGroovyString(String strValue) {
		if (strValue) {
			if (strValue.contains('"')) {
				strValue = strValue.replace('"',"'")
			}
			if (strValue.contains('\\')) {
				strValue = strValue.replace('\\','\\\\')
			}
			if (strValue.contains('$')) {
				strValue = strValue.replace('$','\\$')
			}
			return strValue
		}
		return ""
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static String CorrectMailLineSize(String mailBody) {
		String ret = ""
		mailBody.eachLine { String line ->
			if (ret) {
				ret += "\n"
			}
			if (line.size() < Defs.MAIL_LINE_SIZE) {
				ret += line
			} else {
				String x = ""
				for (String s in line.split(' ')) {
					String xo = x
					if (x) {
						x += ' '
					}
					x += s
					if (x.size() > Defs.MAIL_LINE_SIZE) {
						if (xo) {
							ret += xo + '\n'
							x = s
						} else {
							ret += s
							x = ""
						}
					}
				}
				if (x) {
					ret += x
				}
			}
		}
		return ret
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static List CloneListOfMap(List list) {
		return list.collect { list_value ->
			return list_value.collectEntries { map_value -> return map_value }
		}
	}

	// --------------------------------------------------------------------------------------------------------------------
	static String GetShortenString(String strValue, boolean toShorten = true, int shortenSize = 20) {
		if (toShorten && shortenSize > 0) {
			String continue_str = "..."
			if (strValue.size() < shortenSize) {
				shortenSize = strValue.size()
				continue_str = ""
			}
			return "${strValue.substring(0, shortenSize)}${continue_str} (${strValue.size()} bytes)"
		}
		return strValue
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static String GetStrWithSize(String inputValue, int withSize, String fillChar) {
		while (inputValue.size() < withSize) {
			inputValue = "${fillChar}${inputValue}"
		}
		return inputValue
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static String GetFileName(String pathValue) {
		if (pathValue) {
			if (pathValue.contains('\\')) {
				return pathValue.substring(pathValue.lastIndexOf('\\')+1)
			}
			return pathValue
		}
		return ""
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	static String GetMapnikString(String strValue) {
		if (strValue) {
			if (strValue.contains('"')) {
				strValue = strValue.replace('"',"")
			}
			if (strValue.contains("'")) {
				strValue = strValue.replace("'","")
			}
			return strValue
		}
		return ""
	}
}