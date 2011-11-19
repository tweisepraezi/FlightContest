class FcTagLib
{
    // ====================================================================================================================
    // <g:mainnav link="${createLink(controller:'contest')}" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" newaction="${message(code:'fc.aircraft.new')}" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="contest" show="${message(code:'fc.contest.show')}" id="${contestInstance.id}" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="contestDayTask" contestdaytasknav="true" />
    def mainnav = { p -> 
        def c = ""
        boolean second_nav = false
    
        out << """<div class="clear"></div>
"""
        out << """<div class="grid">
"""
        out << """  <ul class="nav main">
"""
        c = ""
        if (p.controller == "contest") { c = "active" }
        out << """    <li> <a class="${c}" href="${p.link}/../../contest/start">${message(code:'fc.contest')}</a> </li>
"""
        c = ""
        if (p.controller == "crew") { c = "active" }
        out << """    <li> <a class="${c}" href="${p.link}/../../crew/list">${message(code:'fc.crew.list')}</a> </li>
"""
        c = ""
        if (p.controller == "aircraft") { c = "active" }
        out << """    <li> <a class="${c}" href="${p.link}/../../aircraft/list">${message(code:'fc.aircraft.list')}</a> </li>
"""
        c = ""
        if (p.controller == "route") { c = "active" }
        out << """    <li> <a class="${c}" href="${p.link}/../../route/list">${message(code:'fc.route.list')}</a> </li>
"""
        if (ContestDayTask.findByIdIsNotNull()) {
            c = ""
            if (p.controller == "contestDayTask") { c = "active" }
            out << """    <li> <a class="${c}" href="${p.link}/../../contestDayTask/start">${message(code:'fc.contestdaytask.list')}</a> </li>
"""
        }
        if (true) {
            c = ""
            if (p.controller == "root") { c = "active" }
            out << """    <li class="secondary"> <a class="${c}" href="${p.link}/../..">${message(code:'fc.internal')}</a> </li>
"""
        }
        out << """  </ul>
"""
        out << """</div>
"""

        if (p.newaction) {
            out << """<div class="clear"></div>
"""
            out << """<div class="grid">
"""
            out << """  <ul class="nav main">
"""
            out << """    <li> <a href="${p.link}/../../${p.controller}/create">${p.newaction}</a> </li>
"""
            out << """  </ul>
"""
            out << """</div>
"""
            second_nav = true
        } else if (p.show) {
            out << """<div class="clear"></div>
"""
            out << """<div class="grid">
"""
            out << """  <ul class="nav main">
"""
            out << """    <li> <a href="${p.link}/../../${p.controller}/show/${p.id}">${p.show}</a> </li>
"""
            out << """  </ul>
"""
            out << """</div>
"""
            second_nav = true
        }


        out << """<div class="clear"></div>
"""

        if (p.contestdaytasknav) {
            out << """<div class="grid">
"""
            out << """  <ul class="nav main">
"""
            def contestInstance = Contest.findByIdIsNotNull()
            ContestDay.list().each { contestDayInstance ->
                contestDayInstance.contestdaytasks.each { contestDayTaskInstance ->
                    c = ""
                    if (contestInstance.lastContestDayTask == contestDayTaskInstance.id ) { c = "active" }
                    out << """    <li> <a class="${c}" href="${p.link}/../../contestDayTask/listcrewtests/${contestDayTaskInstance.id}" >${contestDayTaskInstance.name()}</a> </li>
"""
                }        
            }
            out << """  </ul>
"""
            out << """</div>
"""            
            out << """<div class="clear"></div>
"""
            second_nav = true
        }

        if (!second_nav) {
            out << """<div class="grid">
"""
            out << """  <ul class="nav main">
"""
            out << """  </ul>
"""
            out << """</div>
"""            
            out << """<div class="clear"></div>
"""
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
        if (p.link == "/fc/contestDayTask/listcrewtests") {
        	out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()} (M)</a>"""
        } else {
        	out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
        }
    }

    // ====================================================================================================================
    // <g:crew var="${crewInstance}" link="${createLink(controller:'crew',action:'show')}"/>
    def crew = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
        }
    }
    
    // ====================================================================================================================
    // <g:crewtest var="${crewTestInstance}" link="${createLink(controller:'crewTest',action:'show')}"/>
    def crewtest = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.crew.name().encodeAsHTML()} (${p.var.contestdaytask.name().encodeAsHTML()})</a>"""
        }
    }
    
    // ====================================================================================================================
    // <g:crewtestnum var="${crewTestInstance}" link="${createLink(controller:'crewTest',action:'show')}"/>
    def crewtestnum = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.viewpos+1}</a>"""
        }
    }
    
    // ====================================================================================================================
    // <g:crewtestleg var="${crewTestLegInstance}" link="${createLink(controller:'crewTestLeg',action:'show')}"/>
    def crewtestleg = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name()}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:crewtestleg2 var="${crewTestLegInstance}" name="${leg}" link="${createLink(controller:'crewTestLeg',action:'show')}"/>
    def crewtestleg2 = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.name}</a>""" // .encodeAsHTML()
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
    // <g:navtest var="${navTestInstance}" link="${createLink(controller:'navTest',action:'show')}"/>
    def navtest = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }

    // ====================================================================================================================
    // <g:navtesttext var="${navTestInstance}"/>
    def navtesttext = { p ->
        out << """${p.var.name().encodeAsHTML()}"""
    }


    // ====================================================================================================================
    // <g:navtesttask var="${navTestTaskInstance}" link="${createLink(controller:'navTestTask',action:'show')}"/>
    def navtesttask = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }

    // ====================================================================================================================
    // <g:navtesttasktitle var="${navTestTaskInstance}"/>
    def navtesttasktitle = { p ->
         out << """<td valign="top" class="name">${message(code:'fc.title')}:</td>"""
        out << """<td valign="top" class="value">${p.var.name().encodeAsHTML()}</td>"""
    }

    // ====================================================================================================================
    // <g:navtesttasktext var="${navTestTaskInstance}"/>
    def navtesttasktext = { p ->
        out << """${p.var.name().encodeAsHTML()}"""
    }

    // ====================================================================================================================
    // <g:navtesttaskleg var="${navTestTaskLegInstance}" link="${createLink(controller:'navTestTaskLeg',action:'show')}"/>
    def navtesttaskleg = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name()}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:navtesttaskleg2 var="${navTestTaskLegInstance}" name="${leg}" link="${createLink(controller:'navTestTaskLeg',action:'show')}"/>
    def navtesttaskleg2 = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.name}</a>""" // .encodeAsHTML()
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
    // <g:routeleg var="${routeLegInstance}" link="${createLink(controller:'routeLeg',action:'show')}"/>
    def routeleg = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name()}</a>""" // .encodeAsHTML()
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
    // <g:wind var="${windInstance}" link="${createLink(controller:'wind',action:'show')}"/>
    def wind = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name()}</a>""" // .encodeAsHTML()
    }

    // ====================================================================================================================
    // <g:windtext var="${windInstance}" />
    def windtext = { p ->
        out << "${p.var.name()}" // .encodeAsHTML()
    }
}
