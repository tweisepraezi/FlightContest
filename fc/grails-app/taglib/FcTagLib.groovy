class FcTagLib
{
    // ====================================================================================================================
    // <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
    def viewmsg = { p ->
        if (p.msg) {
            if (p.error) {
                out << """<h3 style="color: rgb(255,0,0); background: rgb(255,255,255) none repeat scroll 0% 0%; -moz-background-clip: border; -moz-background-origin: padding; -moz-background-inline-policy: continuous;" class="toggler atStart">${p.msg}</h3>"""
            } else {
                out << """<h3 style="color: rgb(0,0,255); background: rgb(255,255,255) none repeat scroll 0% 0%; -moz-background-clip: border; -moz-background-origin: padding; -moz-background-inline-policy: continuous;" class="toggler atStart">${p.msg}</h3>"""
            }
        } else {
            out << """<h3 style="color: rgb(255,255,255); background: rgb(255,255,255) none repeat scroll 0% 0%; -moz-background-clip: border; -moz-background-origin: padding; -moz-background-inline-policy: continuous;" class="toggler atStart">.</h3>"""
        }
        out << """<h3></h3>"""
    }
    
    // ====================================================================================================================
    // <g:viewbool value="${value}" tag="td" trueclass="c1" falseclass="c0" />
    def viewbool = { p ->
    	def attrib = ""
    	if (p.value) {
	        if (p.trueclass) {
	        	attrib = " class=${p.trueclass}"
	        }
    	} else {
            if (p.falseclass) {
                attrib = " class=${p.falseclass}"
            }
    	}
    	def starttag = ""
    	if (p.tag) {
    		starttag = "<${p.tag}${attrib}>"
    	}
    	def endtag = ""
        if (p.tag) {
            endtag = "</${p.tag}>"
        }
    	if (p.value) {
    		out << "${starttag}${message(code:'fc.yes')}${endtag}" 
    	} else {
    		out << "${starttag}${message(code:'fc.no')}${endtag}"
    	}
    }
    
    // ====================================================================================================================
    // <g:aflosroutename var="${aflosRouteDefsInstance.routename}" link="${createLink(controller:'aflosRouteNames',action:'show')}"/>
    def aflosroutename = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.name.encodeAsHTML()}</a>"""
        }
    }
    
    // ====================================================================================================================
    // <g:aircraft var="${aircraftInstance}" link="${createLink(controller:'aircraft',action:'edit')}"/>
    def aircraft = { p ->
        if (p.var) {
            if (p.next) {
                out << """<a href="${p.link}/${p.var.id}${p.next}">${p.var.registration.encodeAsHTML()}</a>"""
            } else {
                out << """<a href="${p.link}/${p.var.id}">${p.var.registration.encodeAsHTML()}</a>"""
            }
        }
    }
    
    // ====================================================================================================================
    // <g:team var="${teamInstance}" link="${createLink(controller:'team',action:'edit')}"/>
    def team = { p ->
        if (p.var) {
            if (p.next) {
                out << """<a href="${p.link}/${p.var.id}${p.next}">${p.var.name.encodeAsHTML()}</a>"""
            } else {
                out << """<a href="${p.link}/${p.var.id}">${p.var.name.encodeAsHTML()}</a>"""
            }
        }
    }
    
    // ====================================================================================================================
    // <g:resultclass var="${resultclassInstance}" link="${createLink(controller:'resultClass',action:'edit')}"/>
    def resultclass = { p ->
        if (p.var) {
            if (p.next) {
                out << """<a href="${p.link}/${p.var.id}${p.next}">${p.var.name.encodeAsHTML()}</a>"""
            } else {
                out << """<a href="${p.link}/${p.var.id}">${p.var.name.encodeAsHTML()}</a>"""
            }
        }
    }
    
    // ====================================================================================================================
    // <g:coordroute var="${coordRouteInstance}" link="${createLink(controller:'coordRoute',action:'show')}"/>
    def coordroute = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name()}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:contest var="${contestInstance}" link="${createLink(controller:'contest',action:'show')}"/>
    def contest = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }

    // ====================================================================================================================
    // <g:task var="${taskInstance}" link="${createLink(controller:'task',action:'edit')}"/>
    def task = { p ->
        String next_str = ""
        if (p.next) {
            next_str = p.next
        }
        if (p.link == "${createLinkTo(dir:'',file:'task/listplanning')}") {
       		out << """<a href="${p.link}/${p.var.id}${next_str}">${p.var.name().encodeAsHTML()} (${message(code:'fc.task.planning')})</a>"""
        } else if (p.link == "${createLinkTo(dir:'',file:'task/listresults')}") {
            out << """<a href="${p.link}/${p.var.id}${next_str}">${p.var.name().encodeAsHTML()} (${message(code:'fc.task.results')})</a>"""
        } else if (p.link == "${createLinkTo(dir:'',file:'task/disabledcheckpoints')}") {
            out << """<a href="${p.link}/${p.var.id}${next_str}">${message(code:'fc.task.disabledcheckpoints')}</a>"""
        } else if (p.link == "${createLinkTo(dir:'',file:'task/listdifferences')}") {
            out << """<a href="${p.link}/${p.var.id}${next_str}">${message(code:'fc.task.differences')}</a>"""
        } else if (p.link == "${createLinkTo(dir:'',file:'task/timetable')}") {
			out << """<a href="${p.link}/${p.var.id}${next_str}">${message(code:'fc.task.timetable')}</a>"""
        } else if (p.link == "${createLinkTo(dir:'',file:'task/timetablejudge')}") {
			out << """<a href="${p.link}/${p.var.id}${next_str}">${message(code:'fc.task.timetablejudge')}</a>"""
        } else if (p.link == "${createLinkTo(dir:'',file:'task/timetableoverview')}") {
            out << """<a href="${p.link}/${p.var.id}${next_str}">${message(code:'fc.task.timetableoverview')}</a>"""
        } else {
        	out << """<a href="${p.link}/${p.var.id}${next_str}">${p.var.bestOfName().encodeAsHTML()} (${message(code:'fc.task.settings')})</a>"""
        }
    }

    // ====================================================================================================================
    // <g:livetrackingtask var="${taskInstance}" link="${createLink(controller:'task',action:'edit')}"/>
    def livetrackingtask = { p ->
        String next_str = ""
        if (p.next) {
            next_str = p.next
        }
      	out << """<a href="${p.link}/${p.var.id}${next_str}">${p.var.bestOfName().encodeAsHTML()} (${message(code:'fc.livetracking.settings')})</a>"""
    }

    // ====================================================================================================================
    // <g:crew var="${crewInstance}" link="${createLink(controller:'crew',action:'edit')}"/>
    def crew = { p ->
        if (p.var) {
            if (p.next) {
                out << """<a href="${p.link}/${p.var.id}${p.next}">${p.var.name.encodeAsHTML()}</a>"""
            } else {
                out << """<a href="${p.link}/${p.var.id}">${p.var.name.encodeAsHTML()}</a>"""
            }
        }
    }
    
    // ====================================================================================================================
    // <g:flighttest var="${flightTestInstance}" link="${createLink(controller:'flightTest',action:'show')}"/>
    def flighttest = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }

    // ====================================================================================================================
    // <g:flighttestwind var="${flightTestWindInstance}" link="${createLink(controller:'flightTestWind',action:'show')}"/>
    def flighttestwind = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name()}</a>""" // .encodeAsHTML()
    }

    // ====================================================================================================================
    // <g:planningtest var="${planningTestInstance}" link="${createLink(controller:'planningTest',action:'show')}"/>
    def planningtest = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }

    // ====================================================================================================================
    // <g:planningtesttask var="${planningTestTaskInstance}" link="${createLink(controller:'planningTestTask',action:'show')}"/>
    def planningtesttask = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }

    // ====================================================================================================================
    // <g:route var="${routeInstance}" link="${createLink(controller:'route',action:'show')}"/>
    def route = { p ->
        String c = ""
        if (p.error) {
            c = "error"
        }
        if (p.next) {
            out << """<a class="$c" href="${p.link}/${p.var.id}${p.next}">${p.var.name().encodeAsHTML()}</a>"""
        } else {
            out << """<a class="$c" href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
        }
    }

    // ====================================================================================================================
    // <g:routetext var="${routeInstance}"/>
    def routetext = { p ->
        out << """${p.var.name().encodeAsHTML()}"""
    }

    // ====================================================================================================================
    // <g:routeleg var="${routeLegInstance}" link="${createLink(controller:'routeLeg',action:'show')}"/>
    def routeleg = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.testName()}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:routelegcoord var="${routeLegCoordInstance}" link="${createLink(controller:'routeLegCoord',action:'show')}"/>
    def routelegcoord = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.coordName()}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:routelegcoordnum var="${routeLegCoordInstance}" num="${i}" link="${createLink(controller:'routeLegCoord',action:'show')}"/>
    def routelegcoordnum = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.num}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:routelegtest var="${routeLegTestInstance}" link="${createLink(controller:'routeLegTest',action:'show')}"/>
    def routelegtest = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.testName()}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:routelegtestnum var="${routeLegTestInstance}" num="${i}" link="${createLink(controller:'routeLegTest',action:'show')}"/>
    def routelegtestnum = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.num}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:test var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/>
    def test = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.crew.startNum}: ${p.var.crew.name.encodeAsHTML()} (${p.var.task.name().encodeAsHTML()})</a>"""
        }
    }
    
    // ====================================================================================================================
    // <g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/>
    def testnum = { p ->
        if (p.var) {
			out << """<a href="${p.link}/${p.var.id}">${p.var.GetStartNum()}</a>"""
        }
    }
    
    // ====================================================================================================================
    // <g:testlegflight var="${testLegFlightInstance}" link="${createLink(controller:'testLegFlight',action:'show')}"/>
    def testlegflight = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name()}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:testlegplanning var="${testLegPlanningInstance}" link="${createLink(controller:'testLegPlanning',action:'show')}"/>
    def testlegplanning = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name()}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:wind var="${windInstance}" link="${createLink(controller:'wind',action:'show')}"/>
    def wind = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name()}</a>""" // .encodeAsHTML()
    }

    // ====================================================================================================================
    // <g:windtext var="${windInstance}" />
    def windtext = { p ->
        out << "${p.var.name()}" // .encodeAsHTML()
    }

    // ====================================================================================================================
    // <g:windtextprintable var="${windInstance}" />
    def windtextprintable = { p ->
        out << "${p.var.printName()}" // .encodeAsHTML()
    }

    // ====================================================================================================================
	// <g:wrpoints test="${testInstance.flightTestTakeoffMissed}" titlecode="fc.flighttest.takeoffmissed" points="${testInstance.GetFlightTestTakeoffMissedPoints()}" />
	def wrpoints = { p ->
		if (p.test) {
			String title_msg = "${message(code:p.titlecode)}"
			out << """<tr>"""
		    out << """<td>${title_msg}: ${p.points.toString()} ${message(code:'fc.points')}</td>"""
			out << """</tr>"""
		}
    }
										
    // --------------------------------------------------------------------------------------------------------------------
    void outln(str)
    {
        out << """$str
"""
    }

}
