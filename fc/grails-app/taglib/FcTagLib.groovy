class FcTagLib
{
    // ====================================================================================================================
    // <g:mainnav link="${createLink(controller:'contest')}" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" newaction="${message(code:'fc.aircraft.new')}" printaction="${message(code:'fc.aircraft.print')}" importaction="${message(code:'fc.route.import')}" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="contest" show="${message(code:'fc.contest.show')}" id="${contestInstance.id}" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="contest" edit="${message(code:'fc.contest.edit')}" id="${contestInstance.id}" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="contestDayTask" contestdaytaskplanning="true" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="contestDayTask" contestdaytaskresults="true" />
    def mainnav = { p -> 
    	def c = ""
        boolean second_nav = false
    
        // ---------------------------------------------------------------
        // ---------------------------------------------------------------
        outln """<div class="clear"></div>"""
        outln """<div class="grid">"""
        outln """  <ul class="nav main">"""
        if (session.lastContest) {
	        outln """    <li> <a class="${active(p.controller,'contest')}" href="${p.link}/../../contest/start">${message(code:'fc.contest')}</a> </li>"""
	        outln """    <li> <a class="${active(p.controller,'crew')}" href="${p.link}/../../crew/list">${message(code:'fc.crew.list')}</a> </li>"""
	        outln """    <li> <a class="${active(p.controller,'aircraft')}" href="${p.link}/../../aircraft/list">${message(code:'fc.aircraft.list')}</a> </li>"""
	        outln """    <li> <a class="${active(p.controller,'route')}" href="${p.link}/../../route/list">${message(code:'fc.route.list')}</a> </li>"""
            boolean foundAnyContestDaytask = false
	        ContestDay.findAllByContest(session.lastContest).each { contestDayInstance ->
            	ContestDayTask.findAllByContestday(contestDayInstance).each { contestDayTaskInstance ->
            		foundAnyContestDaytask = true
            	}
	        }
			if (foundAnyContestDaytask) {
				outln """    <li> <a class="${if (p.contestdaytaskplanning) active(p.controller,'contestDayTask')}" href="${p.link}/../../contestDayTask/startplanning">${message(code:'fc.contestdaytask.listplanning')}</a> </li>"""
           		outln """    <li> <a class="${if (p.contestdaytaskresults) active(p.controller,'contestDayTask')}" href="${p.link}/../../contestDayTask/startresults">${message(code:'fc.contestdaytask.listresults')}</a> </li>"""
			}
        } else {
        	if (Contest.findByIdIsNotNull()) {
        		Contest.list().each { contestInstance ->
        			outln """    <li> <a href="${p.link}/../../contest/activate/${contestInstance.id}">${contestInstance.name()}</a> </li>"""
        		}
        	} else {
        		outln """    <li> <a href="${p.link}/../../contest/create">${message(code:'fc.contest.new')}</a> </li>"""
        	}
        }
        outln """    <li class="secondary"> <a class="${active(p.controller,'global')}" href="${p.link}/../../global/list">${message(code:'fc.settings')}</a> </li>"""
        if (true) {
            outln """    <li class="secondary"> <a class="${if (isAflos(p.controller)) "active"}" href="${p.link}/../../aflos/start">${message(code:'fc.aflos')}</a> </li>"""
        }
        outln """  </ul>"""
        outln """</div>"""

        // ---------------------------------------------------------------
        // ---------------------------------------------------------------
        if (p.newaction || p.show || p.edit || p.printaction || p.importaction) {
            if (p.controller != "contest" || session.lastContest) {
	        	outln """<div class="clear"></div>"""
	            outln """<div class="grid">"""
	            outln """  <ul class="nav main">"""
		        if (p.newaction) {
		            outln """    <li> <a href="${p.link}/../../${p.controller}/create">${p.newaction}</a> </li>"""
		        } 
		        if (p.show) {
		            outln """    <li> <a href="${p.link}/../../${p.controller}/show/${p.id}">${p.show}</a> </li"""
		        }
		        if (p.edit) {
		        	outln """    <li> <a href="${p.link}/../../${p.controller}/edit/${p.id}">${p.edit}</a> </li"""
		        }
		        if (p.printaction) {
		            outln """    <li> <a href="${p.link}/../../${p.controller}/print">${p.printaction}</a> </li>"""
		        }
		        if (p.importaction) {
	                outln """    <li> <a href="${p.link}/../../${p.controller}/importroute">${p.importaction}</a> </li>"""
		        }
	            outln """  </ul>"""
	            outln """</div>"""
	            second_nav = true
            }
        } else if (p.contestdaytaskplanning || p.contestdaytaskresults) {
            outln """<div class="clear"></div>"""
            outln """<div class="grid">"""
            outln """  <ul class="nav main">"""
            ContestDay.findAllByContest(session.lastContest).each { contestDayInstance ->
                ContestDayTask.findAllByContestday(contestDayInstance).each { contestDayTaskInstance ->
                   	if (p.contestdaytaskplanning) {
                   		outln """    <li> <a class="${if (session.lastContestDayTaskPlanning == contestDayTaskInstance.id) "active"}" href="${p.link}/../../contestDayTask/listplanning/${contestDayTaskInstance.id}" >${contestDayTaskInstance.name()}</a> </li>"""
                   	} else if (p.contestdaytaskresults) {
                   		outln """    <li> <a class="${if (session.lastContestDayTaskResults == contestDayTaskInstance.id) "active"}" href="${p.link}/../../contestDayTask/listresults/${contestDayTaskInstance.id}" >${contestDayTaskInstance.name()}</a> </li>"""
                   	}
                }        
            }
            outln """  </ul>"""
            outln """</div>"""            
            outln """<div class="clear"></div>"""
            second_nav = true
        } else if (isAflos(p.controller)) {
            outln """<div class="clear"></div>"""
            outln """<div class="grid">"""
            outln """  <ul class="nav main">"""
            outln """    <li> <a class="${active(p.controller,'aflosRouteDefs')}" href="${p.link}/../../aflosRouteDefs/list" >${message(code:'fc.aflos.routedefs.list')}</a> </li>"""
            outln """    <li> <a class="${active(p.controller,'aflosCrewNames')}" href="${p.link}/../../aflosCrewNames/list" >${message(code:'fc.aflos.crewnames.list')}</a> </li>"""
            outln """  </ul>"""
			outln """</div>"""            
			outln """<div class="clear"></div>"""
            second_nav = true
        } else if (p.controller == "global") {
            outln """<div class="clear"></div>"""
            outln """<div class="grid">"""
            outln """  <ul class="nav main">"""
            outln """    <li> <a class="${active(p.controller,'global')}" href="${p.link}/../../global/list" >${message(code:'fc.internal')}</a> </li>"""
            outln """    <li> <a href="${p.link}/../../global/changelanguage">${message(code:'fc.changelanguage')}</a> </li>"""
            if (Contest.count() > 1) {
            	outln """    <li> <a href="${p.link}/../../global/changecontest">${message(code:'fc.changecontest')}</a> </li>"""
            }
            if (session.lastContest) {
                outln """    <li> <a href="${p.link}/../../contest/deletequestion">${message(code:'fc.contest.delete')}</a> </li>"""
            }
            if (!Contest.findByIdIsNotNull()) {
                outln """    <li> <a href="${p.link}/../../contest/createtest">${message(code:'fc.contest.new.test')}</a> </li>"""
            } else {
            	outln """    <li> <a href="${p.link}/../../contest/create">${message(code:'fc.contest.new')}</a> </li>"""
            }
            outln """  </ul>"""
            outln """</div>"""            
            outln """<div class="clear"></div>"""
            second_nav = true
        }
	
        if (!second_nav) {
        	outln """<div class="grid">"""
            outln """  <ul class="nav main">"""
            outln """  </ul>"""
            outln """</div>"""            
            outln """<div class="clear"></div>"""
        }
    }
    
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
    // <g:aircraft var="${aircraftInstance}" link="${createLink(controller:'aircraft',action:'show')}"/>
    def aircraft = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.registration.encodeAsHTML()}</a>"""
        }
    }
    
    // ====================================================================================================================
    // <g:contest var="${contestInstance}" link="${createLink(controller:'contest',action:'show')}"/>
    def contest = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }

    // ====================================================================================================================
    // <g:contestday var="${contestDayInstance}" link="${createLink(controller:'contestDay',action:'show')}"/>
    def contestday = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }

    // ====================================================================================================================
    // <g:contestdaytask var="${contestDayTaskInstance}" link="${createLink(controller:'contestDayTask',action:'show')}"/>
    def contestdaytask = { p ->
        if (p.link == "/fc/contestDayTask/listplanning") {
        	out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()} (${message(code:'fc.contestdaytask.planning')})</a>"""
        } else if (p.link == "/fc/contestDayTask/listresults") {
                out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()} (${message(code:'fc.contestdaytask.results')})</a>"""
        } else {
        	out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()} (${message(code:'fc.contestdaytask.settings')})</a>"""
        }
    }

    // ====================================================================================================================
    // <g:crew var="${crewInstance}" link="${createLink(controller:'crew',action:'show')}"/>
    def crew = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.name.encodeAsHTML()}</a>"""
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
        out << """<a href="${p.link}/${p.var.id}">${p.var.wind.name()}</a>""" // .encodeAsHTML()
    }

    // ====================================================================================================================
    // <g:landingtest var="${landingTestInstance}" link="${createLink(controller:'landingTest',action:'show')}"/>
    def landingtest = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }
    
    // ====================================================================================================================
    // <g:landingtesttask var="${landingTestTaskInstance}" link="${createLink(controller:'landingTestTask',action:'show')}"/>
    def landingtesttask = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
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
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }

    // ====================================================================================================================
    // <g:routetext var="${routeInstance}"/>
    def routetext = { p ->
        out << """${p.var.name().encodeAsHTML()}"""
    }

    // ====================================================================================================================
    // <g:routecoord var="${routeCoordInstance}" link="${createLink(controller:'routeCoord',action:'show')}"/>
    def routecoord = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name()}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:routecoordnum var="${routeCoordInstance}" num="${i}" link="${createLink(controller:'routeCoord',action:'show')}"/>
    def routecoordnum = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.num}</a>""" // .encodeAsHTML()
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
    // <g:specialtest var="${specialTestInstance}" link="${createLink(controller:'specialTest',action:'show')}"/>
    def specialtest = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }
    
    // ====================================================================================================================
    // <g:specialtesttask var="${specialTestTaskInstance}" link="${createLink(controller:'specialTestTask',action:'show')}"/>
    def specialtesttask = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }
    
    // ====================================================================================================================
    // <g:test var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/>
    def test = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.crew.name.encodeAsHTML()} (${p.var.contestdaytask.name().encodeAsHTML()})</a>"""
        }
    }
    
    // ====================================================================================================================
    // <g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/>
    def testnum = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.viewpos+1}</a>"""
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
    // <g:testlegplanning2 var="${testLegFlightInstance}" name="${testLeg}" link="${createLink(controller:'testLegFlight',action:'show')}"/>
    def testlegplanning2 = { p ->
        out << """<a href="${p.link}/${p.var.id}?name=${p.name}">${p.name}</a>""" // .encodeAsHTML()
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

    // --------------------------------------------------------------------------------------------------------------------
    void outln(str)
    {
        out << """$str
"""
    }

    // --------------------------------------------------------------------------------------------------------------------
    String active(controller, name)
    {
    	if (controller == name) { 
    		return "active" 
    	}
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    boolean isAflos(controller)
    {
        if (controller == "aflosRouteDefs" || controller == "aflosRouteNames" || controller == "aflosCrewNames") {
            return true
        }
    }
}
