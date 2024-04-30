import org.quartz.JobKey

class MainTagLib 
{
    // --------------------------------------------------------------------------------------------------------------------
	// <g:mainnav link="${createLink(controller:'contest')}" />
	// <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" />
	// <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" newaction="${message(code:'fc.aircraft.new')}" printaction="${message(code:'fc.aircraft.print')}" />
	// <g:mainnav link="${createLink(controller:'contest')}" controller="contest" show="${message(code:'fc.contest.show')}" id="${contestInstance.id}" />
	// <g:mainnav link="${createLink(controller:'contest')}" controller="contest" id="${contestInstance.id}" conteststart="true" />
	// <g:mainnav link="${createLink(controller:'contest')}" controller="contest" id="${contestInstance.id}" contesttasks="true" />
	// <g:mainnav link="${createLink(controller:'contest')}" controller="task" taskplanning="true" />
	// <g:mainnav link="${createLink(controller:'contest')}" controller="task" taskresults="true" />
	// <g:mainnav link="${createLink(controller:'contest')}" controller="task" contestevaluation="true" />
    
    def quartzScheduler
    
	def mainnav = { p ->
		def c = ""
		boolean second_nav = false
	
		// ---------------------------------------------------------------
		// 1. menu line
		// ---------------------------------------------------------------
		outln """<div class="clear"></div>"""
        outln """<div class="grid">"""
		outln """  <ul class="nav main">"""
        if (session?.lastContest) {
			session.lastContest.refresh()
	        outln """    <li> <a class="${if (p.conteststart) active(p.controller,'contest')}" href="${p.link}/../../contest/start">${message(code:'fc.contest')}</a> </li>"""
			outln """    <li> <a class="${active(p.controller,'route')}" href="${p.link}/../../route/list">${message(code:'fc.route.list')}</a> </li>"""
			outln """    <li> <a class="${active(p.controller,'map')}" href="${p.link}/../../map/list">${message(code:'fc.map.list')}</a> </li>"""
	        outln """    <li> <a class="${active(p.controller,'crew')}" href="${p.link}/../../crew/list">${message(code:'fc.crew.list')}</a> </li>"""
			outln """    <li> <a class="${active(p.controller,'team')}" href="${p.link}/../../team/list">${message(code:'fc.team.list')}</a> </li>"""
			if (session?.lastContest.resultClasses) {
				outln """    <li> <a class="${active(p.controller,'resultClass')}" href="${p.link}/../../resultClass/list">${message(code:'fc.resultclass.list')}</a> </li>"""
			}
			outln """    <li> <a class="${active(p.controller,'aircraft')}" href="${p.link}/../../aircraft/list">${message(code:'fc.aircraft.list')}</a> </li>"""
            outln """    <li> <a class="${if (p.contesttasks) active(p.controller,'contest')}" href="${p.link}/../../contest/tasks">${message(code:'fc.task.tasks')}</a> </li>"""
			if (Task.findByContestAndHidePlanning(session.lastContest,false)) {
				outln """    <li> <a class="${if (p.taskplanning) active(p.controller,'task')}" href="${p.link}/../../task/startplanning">${message(code:'fc.task.listplanning')}</a> </li>"""
			}
			if (Task.findByContestAndHideResults(session.lastContest,false)) {
				outln """    <li> <a class="${if (p.taskresults) active(p.controller,'task')}" href="${p.link}/../../task/startresults">${message(code:'fc.task.listresults')}</a> </li>"""
			}
			if (Task.findByContest(session.lastContest)) {
				outln """    <li> <a class="${if (p.contestevaluation) active(p.controller,'task')}" href="${p.link}/../../task/startevaluation">${message(code:'fc.contest.evaluation')}</a> </li>"""
			}
		} else {
			if (Contest.findByIdIsNotNull([sort:"id"])) {
				Contest.list().each { contestInstance ->
					outln """    <li> <a href="${p.link}/../../contest/activate/${contestInstance.id}">${contestInstance.name()}</a> </li>"""
        		}
        	} else {
        		outln """    <li> <a href="${p.link}/../../contest/create">${message(code:'fc.contest.new')}</a> </li>"""
				outln """    <li> <a href="${p.link}/../../contest/createtestquestion">${message(code:'fc.contest.new.test')}</a> </li>"""
        	}
        }
        
        outln """        <li class="secondary"> <a name="start" class="${active(p.controller,'flightcontest')}" href="https://flightcontest.de" target="_blank">flightcontest.de</a> </li>"""
        outln """        <li class="secondary"> <a href="/fc/docs/help_${session.showLanguage}.html" target="_blank">${message(code:'fc.help')}</a></li>"""
        outln """        <li class="secondary"> <a class="${active(p.controller,'global')}" href="${p.link}/../../global/info">${message(code:'fc.extras')}</a> </li>"""
        if (Global.IsDevelopmentEnvironment()) {
            outln """    <li class="secondary"> <a href="${p.link}/../../dbconsole" target="_blank">${message(code:'fc.dbconsole')}</a> </li>"""
            outln """    <li class="secondary"> <a href="${p.link}/../../contest/runmodultests">${message(code:'fc.contest.modultests')}</a> </li>"""
        }
        if (BootStrap.global.IsLivePossible()) {
            if (BootStrap.global.liveContestID == session?.lastContest?.id) {
                outln """<li class="secondary"> <a class="${active(p.controller,'flightcontest')}" href="${p.link}/../../global/livesettings?newwindow=true" target="_blank">${message(code:'fc.livesettings.short.on')}</a> </li>"""
            } else {
                outln """<li class="secondary"> <a class="${active(p.controller,'flightcontest')}" href="${p.link}/../../global/livesettings?newwindow=true" target="_blank">${message(code:'fc.livesettings.short')}</a> </li>"""
            }
        }
        //String webroot_dir = servletContext.getRealPath("/")
        //String printjob_filename = webroot_dir + Defs.ROOT_FOLDER_GPXUPLOAD_OSMPRINTJOB
        //if (new File(printjob_filename).exists()) {
        def job_key = new JobKey("OsmPrintMapJob",Defs.OSMPRINTMAP_GROUP)
        if (job_key) {
            if (quartzScheduler.getTriggersOfJob(job_key)*.key) {
                outln """<li class="secondary"><a>${message(code:'fc.contestmap.job.running.short')}</a> </li>"""
            }
        }
        outln """  </ul>"""
		outln """</div>"""

        // ---------------------------------------------------------------
		// 2. menu line
        // ---------------------------------------------------------------
        if (p.newaction || p.show || p.edit || p.printaction || p.printsettings || p.importaction || p.importaction2) {
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
                        outln """    <li> <a href="${p.link}/../../${p.controller}/importfileroute">${message(code:'fc.route.fileimport')}</a> </li>"""
                        outln """    <li> <a href="${p.link}/../../${p.controller}/importfcroute">${message(code:'fc.route.fcfileimport')}</a> </li>"""
					} else if (p.controller == "crew") {
						outln """    <li> <a href="${p.link}/../../${p.controller}/selectfilename">${p.importaction}</a> </li>"""
					} else if (p.controller == "map") {
						outln """    <li> <a href="${p.link}/../../${p.controller}/selectfilename">${p.importaction}</a> </li>"""
                        if (BootStrap.global.IsTaskCreatorExtern()) {
                            outln """    <li> <a href="${p.link}/../../${p.controller}/start_taskcreator_intern" target="_blank">${message(code:'fc.map.taskcreator')} (${message(code:'fc.map.taskcreator.intern')})</a> </li>"""
                            outln """    <li> <a href="${p.link}/../../${p.controller}/start_taskcreator_extern" target="_blank">${message(code:'fc.map.taskcreator')} (${message(code:'fc.map.taskcreator.extern')})</a> </li>"""
                            //outln """    <li> <a href="${p.link}/../../${p.controller}/start_taskcreator_load" target="_blank">${message(code:'fc.map.taskcreator')} (${message(code:'fc.map.taskcreator.load')})</a> </li>"""
                        } else {
                            outln """    <li> <a href="${p.link}/../../${p.controller}/start_taskcreator_intern" target="_blank">${message(code:'fc.map.taskcreator')}</a> </li>"""
                        }
                        outln """    <li> <a href="${p.link}/../../${p.controller}/download_allpngzip">${p.downloadallzipaction}</a> </li>"""
					}
				}
				if (p.importaction2) {
					if (p.controller == "crew") {
						outln """    <li> <a href="${p.link}/../../contest/livetracking_teamsimport/${session.lastContest.id}">${p.importaction2}</a> </li>"""
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
					if (p.controller == "route") {
						if (BootStrap.global.IsDevelopmentEnvironment() && BootStrap.global.IsOpenAIP()) {
							outln """    <li> <a href="${p.link}/../../${p.controller}/openaip_test">OpenAIP Test</a> </li>"""
						}
					}
		        }
				if (p.printsettings) {
					outln """    <li> <a href="${p.link}/../../${p.controller}/editprintsettings">${p.printsettings}</a> </li>"""
				}
				if (p.controller == "contest" && session.lastContest && !p.contesttasks) {
					if (!session.lastContest.resultClasses || !session.lastContest.contestRuleForEachClass) {
						outln """<li> <a href="${p.link}/../../contest/editpoints">${message(code:'fc.contestrule.points')}</a> </li>"""
					}
                     outln """   <li> <a href="${p.link}/../../contest/editdefaults">${message(code:'fc.contestrule.defaults')}</a> </li>"""
	                if (Contest.count() > 1) {
	                    outln """<li> <a href="${p.link}/../../contest/change">${message(code:'fc.contest.change')}</a> </li>"""
					}
					outln """    <li> <a href="${p.link}/../../contest/create">${message(code:'fc.contest.new')}</a> </li>"""
                    if (BootStrap.global.liveContestID != session.lastContest.id) {
                        outln """    <li> <a href="${p.link}/../../contest/deletequestion">${message(code:'fc.contest.delete')}</a> </li>"""
                        outln """    <li> <a href="${p.link}/../../contest/anonymizequestion">${message(code:'fc.contest.anonymize')}</a> </li>"""
                    }
					outln """    <li> <a href="${p.link}/../../contest/copyquestion">${message(code:'fc.contest.copy')}</a> </li>"""
					outln """    <li> <a href="${p.link}/../../contest/createtestquestion">${message(code:'fc.contest.new.test')}</a> </li>"""
                    outln """    <li> <a href="${p.link}/../../contest/editfreetext">${message(code:'fc.contest.printfreetext')}</a> </li>"""
					if (session.lastContest.testExists) {
						outln """<li> <a href="${p.link}/../../contest/runtest">${message(code:'fc.contest.runtest')}</a> </li>"""
					}
	            }
	            outln """  </ul>"""
				outln """</div>"""
	            second_nav = true
            }
        } else if (p.taskplanning || p.taskresults || p.contestevaluation) {
            outln """<div class="clear"></div>"""
			outln """<div class="grid">"""
            outln """  <ul class="nav main">"""
			for (Task task_instance in Task.findAllByContest(session.lastContest,[sort:"idTitle"])) {
				if (p.taskplanning) {
					if (!task_instance.hidePlanning) {
						outln """    <li> <a class="${if (session.lastTaskPlanning == task_instance.id) "active"}" href="${p.link}/../../task/listplanning/${task_instance.id}" >${task_instance.name()}</a> </li>"""
					}
               	} else if (p.taskresults) {
					if (!task_instance.hideResults) {
						outln """    <li> <a class="${if (session.lastTaskResults == task_instance.id) "active"}" href="${p.link}/../../task/listresults/${task_instance.id}" >${task_instance.name()}</a> </li>"""
					}
				}
			}
			if (p.contestevaluation) {
				if (session.lastContest.resultClasses) {
					for (ResultClass resultclass_instance in ResultClass.findAllByContest(session.lastContest,[sort:"id"])) {
						outln """    <li> <a class="${if (session.lastResultClassResults == resultclass_instance.id) "active"}" href="${p.link}/../../resultClass/listresults/${resultclass_instance.id}">${resultclass_instance.name}</a> </li>"""
					}
				}
				outln """    <li> <a class="${if (session.lastContestResults) "active"}" href="${p.link}/../../contest/listresults">${message(code:'fc.contest.listresults')}</a> </li>"""
				if (session.lastContest.teamCrewNum > 0) {
					outln """    <li> <a class="${if (session.lastTeamResults) "active"}" href="${p.link}/../../contest/listteamresults">${message(code:'fc.contest.listteamresults')}</a> </li>"""
				}
			}
			
			if (p.taskplanning || p.taskresults) {
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
				} else if (session.showPage) {
					outln """    <li class="secondary"> <a href="?showpage=off">${message(code:'fc.showlimitall')}</a> </li>"""
					for (int page_pos = session.showPageNum; page_pos > 0; page_pos--) {
						if (session.showPagePos == page_pos) {
							outln """<li class="secondary"> <a class="active" href="?showpagepos=${page_pos}">${page_pos}</a> </li>"""
						} else {
							outln """<li class="secondary"> <a href="?showpagepos=${page_pos}">${page_pos}</a> </li>"""
						}
					}
				} else {
					if (session.showLimitCrewNum) {
						outln """    <li class="secondary"> <a href="?showlimit=on">${message(code:'fc.showlimit',args:[session.showLimitCrewNum])}</a> </li>"""
					}
					outln """    <li class="secondary"> <a href="?showpage=on">${message(code:'fc.showpage')}</a> </li>"""
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
            outln """    <li> <a class="${active(p.controller,'global')}" href="${p.link}/../../global/info" >${message(code:'fc.info')}</a> </li>"""
            outln """    <li> <a href="${p.link}/../../global/changeglobalsettings">${message(code:'fc.changeglobalsettings')}</a> </li>"""
            if (BootStrap.global.IsLivePossible()) {
                outln """    <li> <a href="${p.link}/../../global/livesettings">${message(code:'fc.livesettings')}</a> </li>"""
            }
            if (BootStrap.global.IsWebMailPossible()) {
                outln """    <li> <a href="${p.link}/../../global/start_webmail" target="_blank">${message(code:'fc.net.webmail')}</a> </li>"""
            }
            if (BootStrap.global.IsTestEMailPossible()) {
                outln """    <li title="${BootStrap.global.TestEMailAddress()}"> <a href="${p.link}/../../global/testmail">${message(code:'fc.net.mail.test')}</a> </li>"""
            }
            if (BootStrap.global.IsTestFTPPossible()) {
                outln """    <li> <a href="${p.link}/../../global/testftp">${message(code:'fc.net.ftp.test')}</a> </li>"""
            }
            outln """    <li> <a href="${p.link}/../../gpx/selectloggerfilename">${message(code:'fc.loggerdata.show')}</a> </li>"""
            outln """    <li> <a href="${p.link}/../../gac/selectgacfilename">${message(code:'fc.gac.repair')}</a> </li>"""
            outln """    <li> <a href="${p.link}/../../gac/selectgpxfilename">${message(code:'fc.gac.convert.gpx')}</a> </li>"""
            if (!Global.IsCloudFoundryEnvironment() && !BootStrap.global.IsCloudDemo()) {
                outln """    <li> <a href="${p.link}/../../global/selectgeodatafilename">${message(code:'fc.contestmap.importgeodata')}</a> </li>"""
            }
			if (Global.IsDevelopmentEnvironment()) {
                outln """    <li> <a href="${p.link}/../../global/list" >${message(code:'fc.internal')}</a> </li>"""
				outln """    <li> <a href="${p.link}/../../classDiagram" target="_blank">${message(code:'fc.classdiagram')}</a> </li>"""
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
    
    // --------------------------------------------------------------------------------------------------------------------
    String active(controller, name)
    {
    	if (controller == name) { 
    		return "active" 
    	}
		return ""
    }
    
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
