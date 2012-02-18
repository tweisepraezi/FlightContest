import grails.util.Environment

class FcTagLib
{
    // ====================================================================================================================
    // <g:mainnav link="${createLink(controller:'contest')}" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" newaction="${message(code:'fc.aircraft.new')}" printaction="${message(code:'fc.aircraft.print')}" importaction="${message(code:'fc.route.import')}" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="contest" show="${message(code:'fc.contest.show')}" id="${contestInstance.id}" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="contest" id="${contestInstance.id}" conteststart="true" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="contest" id="${contestInstance.id}" contesttasks="true" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="task" taskplanning="true" />
    // <g:mainnav link="${createLink(controller:'contest')}" controller="task" taskresults="true" />
    def mainnav = { p -> 
    	def c = ""
        boolean second_nav = false
    
        // ---------------------------------------------------------------
        // ---------------------------------------------------------------
        outln """<div class="clear"></div>"""
        outln """<div class="grid">"""
        outln """  <ul class="nav main">"""
        if (session?.lastContest) {
	        outln """    <li> <a class="${if (p.conteststart) active(p.controller,'contest')}" href="${p.link}/../../contest/start">${message(code:'fc.contest')}</a> </li>"""
            outln """    <li> <a class="${active(p.controller,'route')}" href="${p.link}/../../route/list">${message(code:'fc.route.list')}</a> </li>"""
	        outln """    <li> <a class="${active(p.controller,'crew')}" href="${p.link}/../../crew/list">${message(code:'fc.crew.list')}</a> </li>"""
	        outln """    <li> <a class="${active(p.controller,'team')}" href="${p.link}/../../team/list">${message(code:'fc.team.list')}</a> </li>"""
			if (session?.lastContest.resultClasses) {
				outln """    <li> <a class="${active(p.controller,'resultClass')}" href="${p.link}/../../resultClass/list">${message(code:'fc.resultclass.list')}</a> </li>"""
			}
	        outln """    <li> <a class="${active(p.controller,'aircraft')}" href="${p.link}/../../aircraft/list">${message(code:'fc.aircraft.list')}</a> </li>"""
            outln """    <li> <a class="${if (p.contesttasks) active(p.controller,'contest')}" href="${p.link}/../../contest/tasks">${message(code:'fc.contest.tasks')}</a> </li>"""
            boolean foundAnyTask = Task.findByContest(session.lastContest)
			if (foundAnyTask) {
				outln """    <li> <a class="${if (p.taskplanning) active(p.controller,'task')}" href="${p.link}/../../task/startplanning">${message(code:'fc.task.listplanning')}</a> </li>"""
           		outln """    <li> <a class="${if (p.taskresults) active(p.controller,'task')}" href="${p.link}/../../task/startresults">${message(code:'fc.task.listresults')}</a> </li>"""
			}
        } else {
        	if (Contest.findByIdIsNotNull()) {
        		Contest.list().each { contestInstance ->
        			outln """    <li> <a href="${p.link}/../../contest/activate/${contestInstance.id}">${contestInstance.name()}</a> </li>"""
        		}
        	} else {
        		outln """    <li> <a href="${p.link}/../../contest/create">${message(code:'fc.contest.new')}</a> </li>"""
				outln """    <li> <a href="${p.link}/../../contest/createtestquestion">${message(code:'fc.contest.new.test')}</a> </li>"""
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
		        	if (p.contesttasks) {
		        		outln """    <li> <a href="${p.link}/../../task/create">${p.newaction}</a> </li>"""
		        	} else {
		        		outln """    <li> <a href="${p.link}/../../${p.controller}/create">${p.newaction}</a> </li>"""
		        	}
		        } 
		        if (p.importaction) {
					if (p.controller == "route") {
						outln """    <li> <a href="${p.link}/../../${p.controller}/importroute">${p.importaction}</a> </li>"""
					} else if (p.controller == "crew") {
						outln """    <li> <a href="${p.link}/../../${p.controller}/selectfilename">${p.importaction}</a> </li>"""
					}
		        }
		        if (p.show) {
		            outln """    <li> <a href="${p.link}/../../${p.controller}/show/${p.id}">${p.show}</a> </li>"""
		        }
		        if (p.edit) {
		        	outln """    <li> <a href="${p.link}/../../${p.controller}/edit/${p.id}">${p.edit}</a> </li>"""
		        }
		        if (p.printaction) {
		            outln """    <li> <a href="${p.link}/../../${p.controller}/print">${p.printaction}</a> </li>"""
		        }
	            if (p.controller == "contest" && session.lastContest && !p.contesttasks) {
					if (!session.lastContest.resultClasses) {
	                    outln """    <li> <a href="${p.link}/../../contest/editpoints">${message(code:'fc.contestrule.points')}</a> </li>"""
					}
	                if (Contest.count() > 1) {
	                    outln """    <li> <a href="${p.link}/../../contest/change">${message(code:'fc.contest.change')}</a> </li>"""
	                }
                    outln """    <li> <a href="${p.link}/../../contest/create">${message(code:'fc.contest.new')}</a> </li>"""
	                outln """    <li> <a href="${p.link}/../../contest/deletequestion">${message(code:'fc.contest.delete')}</a> </li>"""
	                outln """    <li> <a href="${p.link}/../../contest/copyquestion">${message(code:'fc.contest.copy')}</a> </li>"""
					outln """    <li> <a href="${p.link}/../../contest/createtestquestion">${message(code:'fc.contest.new.test')}</a> </li>"""
					if (session.lastContest.testExists) {
						outln """    <li> <a href="${p.link}/../../contest/runtest">${message(code:'fc.contest.runtest')}</a> </li>"""
					}
	            }
	            outln """  </ul>"""
	            outln """</div>"""
	            second_nav = true
            }
        } else if (p.taskplanning || p.taskresults) {
            outln """<div class="clear"></div>"""
            outln """<div class="grid">"""
            outln """  <ul class="nav main">"""
            for (Task task_instance in Task.findAllByContest(session.lastContest)) {
               	if (p.taskplanning) {
               		outln """    <li> <a class="${if (session.lastTaskPlanning == task_instance.id) "active"}" href="${p.link}/../../task/listplanning/${task_instance.id}" >${task_instance.name()}</a> </li>"""
               	} else if (p.taskresults) {
               		outln """    <li> <a class="${if (session.lastTaskResults == task_instance.id) "active"}" href="${p.link}/../../task/listresults/${task_instance.id}" >${task_instance.name()}</a> </li>"""
               	}
            }
			if (p.taskresults) {
				if (session.lastContest.resultClasses) {
					for (ResultClass resultclass_instance in ResultClass.findAllByContest(session.lastContest)) {
						outln """    <li> <a class="${if (session.lastResultClassResults == resultclass_instance.id) "active"}" href="${p.link}/../../resultClass/listresults/${resultclass_instance.id}">${resultclass_instance.name}</a> </li>"""
					}
				} else {
					outln """    <li> <a class="${if (session.lastContestResults) "active"}" href="${p.link}/../../contest/listresults">${message(code:'fc.contest.listresults')}</a> </li>"""
				}
				if (session.lastContest.teamCrewNum > 0) {
					outln """    <li> <a class="${if (session.lastTeamResults) "active"}" href="${p.link}/../../contest/listteamresults">${message(code:'fc.contest.listteamresults')}</a> </li>"""
				}
			}
			
			if (!session.lastResultClassResults && !session.lastTeamResults && !session.lastContestResults) {
				if (session.showLimit) {
					int crew_num = Crew.countByContest(session.lastContest)
					int next_start = 0
					if (session.showLimitStartPos) {
						next_start = session.showLimitStartPos
					} 
					if (next_start + session.showLimitCrewNum < crew_num) {
						next_start += session.showLimitCrewNum
					}
					int before_start = 0
					if (session.showLimitStartPos) {
						before_start = session.showLimitStartPos
					} 
					if (before_start > session.showLimitCrewNum) {
						before_start -= session.showLimitCrewNum
					} else {
						before_start = 0
					}
					int last_start = 0
					if (crew_num > session.showLimitStartPos + session.showLimitCrewNum) {
						last_start = crew_num - session.showLimitCrewNum
					}
					outln """    <li class="secondary"> <a href="?showlimit=off">${message(code:'fc.showlimitall')}</a> </li>"""
					//if (last_start) {
						outln """    <li class="secondary"> <a href="?startpos=${last_start}">&gt;&#124;</a> </li>"""
						outln """    <li class="secondary"> <a href="?startpos=${next_start}">&gt;&gt;</a> </li>"""
					//}
					//if (session.showLimitStartPos > 0) {
						outln """    <li class="secondary"> <a href="?startpos=${before_start}">&lt;&lt;</a> </li>"""
						outln """    <li class="secondary"> <a href="?startpos=0">&#124;&lt;</a> </li>"""
					//}
				} else {
					outln """    <li class="secondary"> <a href="?showlimit=on">${message(code:'fc.showlimit',args:[session.showLimitCrewNum])}</a> </li>"""
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
            outln """    <li> <a class="${active(p.controller,'aflosErrors')}" href="${p.link}/../../aflosErrors/list" >${message(code:'fc.aflos.errors.list')}</a> </li>"""
            outln """    <li> <a class="${active(p.controller,'aflosCheckPoints')}" href="${p.link}/../../aflosCheckPoints/list" >${message(code:'fc.aflos.checkpoints.list')}</a> </li>"""
            outln """    <li> <a class="${active(p.controller,'aflosErrorPoints')}" href="${p.link}/../../aflosErrorPoints/list" >${message(code:'fc.aflos.errorpoints.list')}</a> </li>"""
			if (session?.lastContest) {
				if (!session.lastContest.aflosTest) {
					outln """    <li> <a href="${p.link}/../../aflos/selectfilename" >${message(code:'fc.aflos.upload')}</a> </li>"""
				}
			}
            outln """  </ul>"""
			outln """</div>"""            
			outln """<div class="clear"></div>"""
            second_nav = true
        } else if (p.controller == "global") {
            outln """<div class="clear"></div>"""
            outln """<div class="grid">"""
            outln """  <ul class="nav main">"""
            outln """    <li> <a href="${p.link}/../../global/changeglobalsettings">${message(code:'fc.changeglobalsettings')}</a> </li>"""
			if (Environment.currentEnvironment == Environment.DEVELOPMENT ) {
				outln """    <li> <a href="${p.link}/../../classDiagram" target="_blank">${message(code:'fc.classdiagram')}</a> </li>"""
				outln """    <li> <a href="${p.link}/../../dbUtil" target="_blank">${message(code:'fc.dbutil')}</a> </li>"""
			}
            outln """    <li> <a class="${active(p.controller,'global')}" href="${p.link}/../../global/list" >${message(code:'fc.internal')}</a> </li>"""
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
    // <g:aircraft var="${aircraftInstance}" link="${createLink(controller:'aircraft',action:'edit')}"/>
    def aircraft = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.registration.encodeAsHTML()}</a>"""
        }
    }
    
    // ====================================================================================================================
    // <g:team var="${teamInstance}" link="${createLink(controller:'team',action:'edit')}"/>
    def team = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.name.encodeAsHTML()}</a>"""
        }
    }
    
    // ====================================================================================================================
    // <g:resultclass var="${resultclassInstance}" link="${createLink(controller:'resultClass',action:'edit')}"/>
    def resultclass = { p ->
        if (p.var) {
            out << """<a href="${p.link}/${p.var.id}">${p.var.name.encodeAsHTML()}</a>"""
        }
    }
    
    // ====================================================================================================================
    // <g:coordresult var="${coordResultInstance}" name="${legNo}" procedureTurn="false" next="${next}" link="${createLink(controller:'coordResult',action:'edit')}"/></td>
    def coordresult = { p ->
	    String t = p.name
		if (p.procedureTurn) {
		    if (p.var.resultProcedureTurnEntered) {
		      t += """ <img src="/fc/images/skin/ok.png"/>"""
		    } else {
		        t += " ..."
		    }
		} else {
		    if (p.var.resultEntered) {
		      t += """ <img src="/fc/images/skin/ok.png"/>"""
		    } else {
		        t += " ..."
		    }
		}
		if (p.next) {
			out << """<a href="${p.link}/${p.var.id}?name=${p.name}&next=${p.next}">${t}</a>""" // .encodeAsHTML()
		} else {
			out << """<a href="${p.link}/${p.var.id}?name=${p.name}">${t}</a>""" // .encodeAsHTML()
		}
    }
    
    // ====================================================================================================================
    // <g:coordroute var="${coordRouteInstance}" link="${createLink(controller:'coordRoute',action:'show')}"/>
    def coordroute = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name()}</a>""" // .encodeAsHTML()
    }
    
    // ====================================================================================================================
    // <g:coordroutenum var="${coordRouteInstance}" num="${i}" next="${next}" link="${createLink(controller:'coordRoute',action:'show')}"/>
   def coordroutenum = { p ->
    	String t = p.num.toString()
        if (p.var.measureEntered) {
			t += """ <img src="/fc/images/skin/ok.png"/>"""
		} else {
			t += " ..."
		}
		if (p.next) {
			out << """<a href="${p.link}/${p.var.id}?next=${p.next}">${t}</a>""" // .encodeAsHTML()
		} else {
			out << """<a href="${p.link}/${p.var.id}">${t}</a>""" // .encodeAsHTML()
		}
    }
    
    // ====================================================================================================================
    // <g:contest var="${contestInstance}" link="${createLink(controller:'contest',action:'show')}"/>
    def contest = { p ->
        out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()}</a>"""
    }

    // ====================================================================================================================
    // <g:task var="${taskInstance}" link="${createLink(controller:'task',action:'edit')}"/>
    def task = { p ->
        if (p.link == "/fc/task/listplanning") {
        	out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()} (${message(code:'fc.task.planning')})</a>"""
        } else if (p.link == "/fc/task/listresults") {
            out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()} (${message(code:'fc.task.results')})</a>"""
        } else if (p.link == "/fc/task/disabledcheckpoints") {
            //out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()} (${message(code:'fc.task.disabledcheckpoints')})</a>"""
            out << """<a href="${p.link}/${p.var.id}">${message(code:'fc.task.disabledcheckpoints')}</a>"""
        } else {
        	out << """<a href="${p.link}/${p.var.id}">${p.var.name().encodeAsHTML()} (${message(code:'fc.task.settings')})</a>"""
        }
    }

    // ====================================================================================================================
    // <g:crew var="${crewInstance}" link="${createLink(controller:'crew',action:'edit')}"/>
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
            out << """<a href="${p.link}/${p.var.id}">${p.var.crew.name.encodeAsHTML()} (${p.var.task.name().encodeAsHTML()})</a>"""
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
    // <g:testlegplanning2 var="${testLegFlightInstance}" name="${testLeg}" next="${next}" link="${createLink(controller:'testLegFlight',action:'show')}"/>
    def testlegplanning2 = { p ->
    	String t = p.name
        if (p.var.resultEntered) {
			t += """ <img src="/fc/images/skin/ok.png"/>"""
        } else {
        	t += " ..."
        }
		if (p.next) {
			out << """<a href="${p.link}/${p.var.id}?name=${p.name}&next=${p.next}">${t}</a>""" // .encodeAsHTML()
		} else {
			out << """<a href="${p.link}/${p.var.id}?name=${p.name}">${t}</a>""" // .encodeAsHTML()
		}
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
    	switch (controller) {
        	case "aflosRouteDefs":
        	case "aflosRouteNames":
        	case "aflosCrewNames":
        	case "aflosErrors":
        	case "aflosCheckPoints":
        	case "aflosErrorPoints":
        		return true
        }
    }
}
