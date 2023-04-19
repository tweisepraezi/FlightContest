import java.util.Date;
import java.util.Map;

class ContestController {
    
    def printService
    def imageService
    def fcService
    def evaluationService
	def demoContestService
    def testService
    def trackerService
    def gpxService

    def index = { redirect(action:start,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def start = {
        fcService.printstart "Start contest"
        
		session.contestTitle = ""
        boolean restart = false
        
        session.maxInactiveInterval = 60*240 // 4h
        fcService.println "Session timeout ${session.maxInactiveInterval/60} minutes."
        
        // check ShowLanguage
        String show_language = ""
        if (params.lang) {
            show_language = params.lang
            if (show_language) {
                if (session?.showLanguage != show_language) {
                    restart = true
                }
                fcService.println "Set showLanguage to '$show_language' (param)"
                session.showLanguage = show_language
            }
        }
        if (!session?.showLanguage) {
            show_language = fcService.GetCookie("ShowLanguage",Languages.de.toString())
            if (show_language) {
                if (session?.showLanguage != show_language) {
                    restart = true
                }
                fcService.println "Set showLanguage to '$show_language' (cookie)"
                session.showLanguage = show_language
            }
        }
        
        if (restart) {
            fcService.printdone "Restart contest with language $show_language"
			redirect(controller:'contest',action:'start',params:[lang:show_language])
        } else {
            // PrintLanguage
            if (!session?.printLanguage) {
                String print_language = fcService.GetCookie("PrintLanguage",Languages.de.toString())
                if (print_language) {
                    fcService.println "Set printLanguage to '$print_language'"
                    session.printLanguage = print_language
                }
            }
            
            // ShowLimitCrewNum
            if (!session?.showLimitCrewNum) {
                String showLimitCrewNum = fcService.GetCookie("ShowLimitTestNum","0")
                if (showLimitCrewNum) {
                    fcService.println "Set showLimitCrewNum to '$showLimitCrewNum'"
                    session.showLimitCrewNum = showLimitCrewNum.toInteger() 
                }
            }
            
            if (BootStrap.global.IsDBOlder()) {
                fcService.printdone "db older"
                redirect(controller:'contest',action:'nostart',params:[reason:"older"])
            } else if (BootStrap.global.IsDBNewer()) {
                fcService.printdone "db newer"
                redirect(controller:'contest',action:'nostart',params:[reason:"newer"])
            } else {
                // LastContestID
                if (!session?.lastContest) {
                    String lastContestID = fcService.GetCookie("LastContestID","")
                    if (lastContestID) {
                        Contest last_contest = Contest.findById(lastContestID.toInteger())
                        if (last_contest) {
                            fcService.println "Set lastContest to '$lastContestID'"
                            session.lastContest = last_contest
                        }
                    }
                }
                if (session?.lastContest) {
                    // save return action
                    session.contestReturnAction = actionName 
                    session.contestReturnController = controllerName
                    session.contestReturnID = params.id
                    fcService.printdone "last contest"
                    return [contestInstance:session.lastContest]
                }
                fcService.printdone ""
                return [:]
            }
        }
    }

    def nostart = {
        fcService.println "No Start."
        return [reason:params.reason]
    }
    
    def show = {
        if (session?.lastContest) {
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def edit = {
        if (session?.lastContest) {
            // assign return action
            if (session.contestReturnAction) {
                return [contestInstance:session.lastContest,contestReturnAction:session.contestReturnAction,contestReturnController:session.contestReturnController,contestReturnID:session.contestReturnID]
            }
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def editfreetext = {
        if (session?.lastContest) {
            // assign return action
            if (session.contestReturnAction) {
                return [contestInstance:session.lastContest,contestReturnAction:session.contestReturnAction,contestReturnController:session.contestReturnController,contestReturnID:session.contestReturnID]
            }
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def editpoints = {
        if (session?.lastContest) {
			session.lastContest.refresh()
            // assign return action
            if (session.contestReturnAction) {
                return [contestInstance:session.lastContest,contestReturnAction:session.contestReturnAction,contestReturnController:session.contestReturnController,contestReturnID:session.contestReturnID]
            }
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def editdefaults = {
        if (session?.lastContest) {
            session.lastContest.refresh()
            // assign return action
            if (session.contestReturnAction) {
                return [contestInstance:session.lastContest,contestReturnAction:session.contestReturnAction,contestReturnController:session.contestReturnController,contestReturnID:session.contestReturnID]
            }
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def update = {
        def contest = fcService.updateContest(session.showLanguage, params)
        if (contest.saved) {
            flash.message = contest.message
            session.lastContest = contest.instance
            if (!session.lastContest.teamCrewNum) {
                session.lastTeamResults = null
            }
            fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
            // process return action
            if (params.contestReturnAction) {
                redirect(action:params.contestReturnAction,controller:params.contestReturnController,id:params.contestReturnID)
            } else if (params.editteamresultsettingsReturnAction) {
                redirect(action:params.editteamresultsettingsReturnAction,controller:params.editteamresultsettingsReturnController,id:params.editteamresultsettingsReturnID)
            } else if (params.editresultsettingsReturnAction) {
                redirect(action:params.editresultsettingsReturnAction,controller:params.editresultsettingsReturnController,id:params.editresultsettingsReturnID)
            } else {
                redirect(action:start,id:contest.instance.id)
            }
        } else if (contest.instance) {
            flash.message = contest.message
            flash.error = true
            render(view:'edit',model:[contestInstance:contest.instance])
        } else {
            flash.message = contest.message
            redirect(action:edit,id:params.id)
        }
    }

    def savecontest = {
        def contest = fcService.updateContest(session.showLanguage, params)
        if (contest.saved) {
            flash.message = contest.message
            session.lastContest = contest.instance
            redirect(action:edit,id:contest.instance.id)
        } else if (contest.instance) {
            render(view:'edit',model:[contestInstance:contest.instance])
        } else {
            flash.message = contest.message
            redirect(action:edit,id:params.id)
        }
    }

    def savepoints = {
        def contest = fcService.updateContest(session.showLanguage, params)
        if (contest.saved) {
            flash.message = contest.message
            session.lastContest = contest.instance
            redirect(action:editpoints,id:contest.instance.id)
        } else {
            flash.message = contest.message
            redirect(action:editpoints,id:params.id)
        }
    }

    def savedefaults = {
        def contest = fcService.updateContest(session.showLanguage, params)
        if (contest.saved) {
            flash.message = contest.message
            session.lastContest = contest.instance
            redirect(action:editdefaults,id:contest.instance.id)
        } else {
            flash.message = contest.message
            redirect(action:editdefaults,id:params.id)
        }
    }

    def savefreetext = {
        def contest = fcService.updateContest(session.showLanguage, params)
        if (contest.saved) {
            flash.message = contest.message
            session.lastContest = contest.instance
            redirect(action:editfreetext,id:contest.instance.id)
        } else if (contest.instance) {
            render(view:'editfreetext',model:[contestInstance:contest.instance])
        } else {
            flash.message = contest.message
            redirect(action:editfreetext,id:params.id)
        }
    }

    def calculatepoints = {
        def contest = fcService.calculatepointsContest(params)
          flash.message = contest.message
        redirect(action:editpoints,id:params.id)
    }

    def create = {
        def contest = fcService.createContest(params)
        if (contest.created) {
            return [contestInstance:contest.instance]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def save = {
        def contest = fcService.saveContest(params) 
        if (contest.saved) {
            session.lastContest = contest.instance
            fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
            session.showLimit = false
            session.showLimitStartPos = 0
			session.showPage = false
			session.showPagePos = 1
			session.showPageNum = 1
            session.lastTaskPlanning = null
            session.lastTaskResults = null
            session.lastResultClassResults = null
            session.lastContestResults = null
            session.lastTeamResults = null
            flash.message = contest.message
            redirect(action:start,id:contest.instance.id)
        } else {
            flash.message = contest.message
            flash.error = true
            render(view:'create',model:[contestInstance:contest.instance])
        }
    }

    def copyquestion = {
        if (session?.lastContest) {
            Map new_params = params + [title:fcService.getContestCopyTitle(session.lastContest)]
            def contest = fcService.createContest(new_params)
            if (contest.created) {
                return [contestInstance:contest.instance]
            } else {
                flash.message = contest.message
                redirect(action:start)
            }
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }
    
    def copy = {
        def contest = fcService.copyContest(params,session.lastContest) 
        if (contest.saved) {
            session.lastContest = contest.instance
            fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
            session.showLimit = false
            session.showLimitStartPos = 0
			session.showPage = false
			session.showPagePos = 1
			session.showPageNum = 1
            session.lastTaskPlanning = null
            session.lastTaskResults = null
            session.lastResultClassResults = null
            session.lastContestResults = null
            session.lastTeamResults = null
            flash.message = contest.message
            redirect(action:start,id:contest.instance.id)
        } else {
            render(view:'create',model:[contestInstance:contest.instance])
        }
    }

    def tasks = {
        fcService.printstart "List tasks"
        if (session?.lastContest) {
            def task_list = Task.findAllByContest(session.lastContest,[sort:"idTitle"])
            // save return action
            session.taskReturnAction = actionName 
            session.taskReturnController = controllerName
            session.taskReturnID = params.id
            session.planningtestReturnAction = actionName
            session.planningtestReturnController = controllerName
            session.planningtestReturnID = params.id
            session.flighttestReturnAction = actionName
            session.flighttestReturnController = controllerName
            session.flighttestReturnID = params.id
            fcService.printdone "last contest"
            return [contestInstance:session.lastContest,contestTasks:task_list]
        }
        fcService.printdone ""
        redirect(controller:'contest',action:'start')
    }

    def activate = {
        session.lastContest = Contest.get(params.id)
        fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
        session.showLimit = false
        session.showLimitStartPos = 0
		session.showPage = false
		session.showPagePos = 1
		session.showPageNum = 1
        session.lastTaskPlanning = null
        session.lastTaskResults = null
        session.lastResultClassResults = null
        session.lastContestResults = null
        session.lastTeamResults = null
		if (params.flashmessage) {
			flash.message = params.flashmessage
		}
		if (params.flasherror == "true") {
			flash.error = true
		}
        redirect(action:'start')
    }

    def deletequestion = {
        if (session?.lastContest) {
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }
    
    def delete = {
        def contest = fcService.deleteContest(params)
        if (contest.deleted) {
            if (Contest.count() == 1) {
                session.lastContest = Contest.findByIdIsNotNull([sort:"id",order:"desc"])
                fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
                // session.lastContest = null
                // fcService.SetCookie(response, "LastContestID",  "")
                session.showLimit = false
                session.showLimitStartPos = 0
				session.showPage = false
				session.showPagePos = 1
				session.showPageNum = 1
                session.lastTaskPlanning = null
                session.lastTaskResults = null
                session.lastResultClassResults = null
                session.lastContestResults = null
                session.lastTeamResults = null
                flash.message = contest.message
                redirect(action:start)
            } else {
                session.lastContest = null
                fcService.SetCookie(response, "LastContestID",  "")
                session.showLimit = false
                session.showLimitStartPos = 0
				session.showPage = false
				session.showPagePos = 1
				session.showPageNum = 1
                session.lastTaskPlanning = null
                session.lastTaskResults = null
                session.lastResultClassResults = null
                session.lastContestResults = null
                session.lastTeamResults = null
                flash.message = contest.message
                redirect(controller:'global')
            }
        } else if (contest.notdeleted) {
            flash.message = contest.message
            flash.error = true
            redirect(action:start,id:params.id)
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }
    
    def anonymizequestion = {
        if (session?.lastContest) {
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def anonymize = {
        def contest = fcService.anonymizeContest(params)
        flash.message = contest.message
        redirect(action:start)
    }

    def change = {
		println "change"
        session.lastContest = null
        fcService.SetCookie(response, "LastContestID",  "")
        redirect(action:start)
    }

	def selectfilename_imageleft = {
		redirect(action:selectimagefilename,params:['imageField':'imageLeft'])
	}
	
	def selectfilename_imagecenter = {
		redirect(action:selectimagefilename,params:['imageField':'imageCenter'])
	}
	
	def selectfilename_imageright = {
		redirect(action:selectimagefilename,params:['imageField':'imageRight'])
	}
	
	def selectfilename_imagebottomleft = {
		redirect(action:selectimagefilename,params:['imageField':'imageBottomLeft'])
	}
	
	def selectfilename_imagebottomright = {
		redirect(action:selectimagefilename,params:['imageField':'imageBottomRight'])
	}
	
	def selectimagefilename = {
		[:]
    }
	
	def loadimage = {
        session.lastContest.refresh()
        def file = request.getFile('imagefile')
        Map img = imageService.LoadImage(ImageService.JPG_EXTENSION, file, session.lastContest, params.imageField, Contest.IMAGEMAXSIZE)
        if (!img.found) {
            img = imageService.LoadImage("", file, session.lastContest, params.imageField, Contest.IMAGEMAXSIZE)
        }
        flash.error = img.error
        flash.message = img.message
		redirect(action:edit)
	}
	
	def deleteimage_imageleft = {
		delete_image(['imageField':'imageLeft','imageFieldHeight':'imageLeftHeight'])
		redirect(action:edit)
	}
	
	def deleteimage_imagecenter = {
		delete_image(['imageField':'imageCenter','imageFieldHeight':'imageCenterHeight'])
		redirect(action:edit)
	}
	
	def deleteimage_imageright = {
		delete_image(['imageField':'imageRight','imageFieldHeight':'imageRightHeight'])
		redirect(action:edit)
	}
	
	def deleteimage_imagebottomleft = {
		delete_bottom_image(['imageField':'imageBottomLeft','imageFieldHeight':'imageBottomLeftHeight'])
		redirect(action:edit)
	}
	
	def deleteimage_imagebottomright = {
		delete_bottom_image(['imageField':'imageBottomRight','imageFieldHeight':'imageBottomRightHeight'])
		redirect(action:edit)
	}
	
	void delete_image(Map params)
	{
		fcService.printstart "Delete '$params.imageField'"
		session.lastContest.refresh()
		session.lastContest.(params.imageField) = null
		session.lastContest.(params.imageFieldHeight) = Contest.IMAGEHEIGHT
		session.lastContest.save()
		fcService.printdone ""
	}   
	
	void delete_bottom_image(Map params)
	{
		fcService.printstart "Delete '$params.imageField'"
		session.lastContest.refresh()
		session.lastContest.(params.imageField) = null
		session.lastContest.(params.imageFieldHeight) = Contest.IMAGEBOTTOMHEIGHT
		session.lastContest.save()
		fcService.printdone ""
	}   
	
	def actualimage_imageleft = {
		actual_image(['imageFieldHeight':'imageLeftHeight','imageHeight':params.imageLeftHeight])
		redirect(action:edit)
	}
	
	def actualimage_imagecenter = {
		actual_image(['imageFieldHeight':'imageCenterHeight','imageHeight':params.imageCenterHeight])
		redirect(action:edit)
	}
	
	def actualimage_imageright = {
		actual_image(['imageFieldHeight':'imageRightHeight','imageHeight':params.imageRightHeight])
		redirect(action:edit)
	}
	
	void actual_image(Map params)
	{
		fcService.printstart "Actual '$params.imageFieldHeight'"
		session.lastContest.refresh()
		if (params.imageHeight.isInteger()) {
			session.lastContest.(params.imageFieldHeight) = params.imageHeight.toInteger()
		}
		session.lastContest.save()
		fcService.printdone ""
	}   
	
	def actual_titelsize = {
		fcService.printstart "Actual 'titleSize'"
		session.lastContest.refresh()
		session.lastContest.titleSize = params.titleSize
		session.lastContest.save()
		fcService.printdone ""
		redirect(action:edit)
	}
	
	def reset_titlesize = {
		fcService.printstart "Reset 'titleSize'"
		session.lastContest.refresh()
		session.lastContest.titleSize = Contest.TITLESIZE
		session.lastContest.save()
		fcService.printdone ""
		redirect(action:edit)
	}
	
	def reset_a3portraitfactor = {
		fcService.printstart "Reset 'a3PortraitFactor'"
		session.lastContest.refresh()
		session.lastContest.a3PortraitFactor = Contest.A3PORTRAITFACTOR
		session.lastContest.save()
		fcService.printdone ""
		redirect(action:edit)
	}
	
	def reset_a4landscapefactor = {
		fcService.printstart "Reset 'a4LandscapeFactor'"
		session.lastContest.refresh()
		session.lastContest.a4LandscapeFactor = Contest.A4LANDSCAPEFACTOR
		session.lastContest.save()
		fcService.printdone ""
		redirect(action:edit)
	}
	
	def reset_a3landscapefactor = {
		fcService.printstart "Reset 'a3LandscapeFactor'"
		session.lastContest.refresh()
		session.lastContest.a3LandscapeFactor = Contest.A3LANDSCAPEFACTOR
		session.lastContest.save()
		fcService.printdone ""
		redirect(action:edit)
	}
	
	def reset_imagebottomsize = {
		fcService.printstart "Reset 'imageBottomTextSize'"
		session.lastContest.refresh()
		session.lastContest.imageBottomTextSize = Contest.IMAGEBOTTOMTEXTSIZE
		session.lastContest.save()
		fcService.printdone ""
		redirect(action:edit)
	}
	
	def view_image_left = {
		if (params.contestid) {
            try {
                Contest contest = Contest.get(params.contestid)
                response.outputStream << contest.imageLeft
            } catch (Exception e) {
            }
		}
	}
	     
	def view_image_center = {
		if (params.contestid) {
            try {
                Contest contest = Contest.get(params.contestid)
                response.outputStream << contest.imageCenter
            } catch (Exception e) {
            }
		}
	}
	     
	def view_image_right = {
		if (params.contestid) {
            try {
                Contest contest = Contest.get(params.contestid)
                response.outputStream << contest.imageRight
            } catch (Exception e) {
            }
		}
	}
	     
	def view_image_bottom_left = {
		if (params.contestid) {
            try {
                Contest contest = Contest.get(params.contestid)
                response.outputStream << contest.imageBottomLeft
            } catch (Exception e) {
            }
		}
	}
	     
	def view_image_bottom_right = {
		if (params.contestid) {
            try {
                Contest contest = Contest.get(params.contestid)
                response.outputStream << contest.imageBottomRight
            } catch (Exception e) {
            }
		}
	}

    def delete_contestproperty = {
        fcService.println "delete_contestproperty $params"
        if (params.contestPropertyId) {
            ContestProperty contest_property_instance = ContestProperty.get(params.contestPropertyId)
            if (contest_property_instance) {
                flash.message = message(code:'fc.contest.properties.deleted', args:[contest_property_instance.key, contest_property_instance.value])
                contest_property_instance.delete()
            }
        }
		redirect(action:edit)
    }
    
    def add_contestproperty = {
        fcService.println "add_contestproperty $params"
        if (params.id && params.key && params.value) {
	        Contest contest_instance = Contest.get(params.id)
            if (contest_instance) {
                ContestProperty contest_property_instance = new ContestProperty(key:params.key, value:params.value, contest:contest_instance)
                if (contest_property_instance.save()) {
                    flash.message = message(code:'fc.contest.properties.saved', args:[params.key, params.value])
                } else {
                    flash.message = message(code:'fc.contest.properties.notsaved', args:[params.key, params.value])
                    flash.error = true
                }
            }
        }
		redirect(action:edit)
    }
    
    def cancel = {
        // process return action
        if (params.positionsReturnAction) {
            redirect(action:params.positionsReturnAction,controller:params.positionsReturnController,id:params.positionsReturnID)
        } else if (params.contestReturnAction) {
            redirect(action:params.contestReturnAction,controller:params.contestReturnController,id:params.contestReturnID)
        } else if (params.editteamresultsettingsReturnAction) {
            redirect(action:params.editteamresultsettingsReturnAction,controller:params.editteamresultsettingsReturnController,id:params.editteamresultsettingsReturnID)
        } else if (params.editresultsettingsReturnAction) {
            redirect(action:params.editresultsettingsReturnAction,controller:params.editresultsettingsReturnController,id:params.editresultsettingsReturnID)
        } else {
            redirect(action:start)
        }
    }
    
    def createtask = {
        redirect(controller:'task',action:'create',params:['contest.id':session.lastContest.id,'contestid':session.lastContest.id,'fromcontest':true])
    }

    def listresults = {
		session.contestTitle = ""
        if (session?.lastContest) {
            session.lastResultClassResults = null
            session.lastContestResults = true
            session.lastTeamResults = null
            // save return action
            session.crewReturnAction = actionName
            session.crewReturnController = controllerName
            session.crewReturnID = params.id
            session.aircraftReturnAction = actionName
            session.aircraftReturnController = controllerName
            session.aircraftReturnID = params.id
            session.teamReturnAction = actionName
            session.teamReturnController = controllerName
            session.teamReturnID = params.id
            session.crewresultsReturnAction = actionName
            session.crewresultsReturnController = controllerName
            session.crewresultsReturnID = params.id
            // assign return action
            if (session.positionsReturnAction) {
                return [contestInstance:session.lastContest,positionsReturnAction:session.positionsReturnAction,positionsReturnController:session.positionsReturnController,positionsReturnID:session.positionsReturnID,resultClasses:session.lastContest.resultClasses]
            }
            return [contestInstance:session.lastContest,resultClasses:session.lastContest.resultClasses]
        } else {
            redirect(action:start)
        }
    }

    def listresultslive = {
        evaluationService.printstart "listresultslive"
		if (params.id && params.id.isLong()) {
            evaluationService.print "Get contest with id '${params.id}'"
			session.lastContest = Contest.get(params.id)
            evaluationService.println "Done."
		}
        if (params.lang) {
            evaluationService.println "Set showLanguage to '${params.lang}'"
            session.showLanguage = params.lang
        }
        boolean show_intern = false
        if (params.showIntern == 'yes') {
            show_intern = true
        }
		if (session?.lastContest) {
			session.lastContest.refresh()
			def contest = evaluationService.calculatelivepositionsContest(session.lastContest)
            session.contestTitle = session.lastContest.GetPrintContestTitle(ResultFilter.Contest)
            gpxService.LiveCrews = contest.livecrews.size()
            evaluationService.printdone "Show live html."
			return [contestInstance:session.lastContest, liveTest:false, params:null, liveContest:contest.livecontest, liveCrews:contest.livecrews, newestLiveCrews:contest.newestlivecrews, showIntern:show_intern, LiveStartPos:params.livestartpos.toInteger(), LiveShowSize:params.liveshowsize.toInteger(), LiveNewestShowSize:params.livenewestshowsize.toInteger()]
        } else {
            evaluationService.printdone "Start."
            redirect(action:start)
		}
	}
	
    def listnoliveresults = {
        fcService.printstart "listnoliveresults"
        if (params.id && params.id.isLong()) {
            fcService.print "Get contest with id '${params.id}'"
            session.lastContest = Contest.get(params.id)
            fcService.println "Done."
        }
        if (params.lang) {
            fcService.println "Set showLanguage to '${params.lang}'"
            session.showLanguage = params.lang
        }
        if (session?.lastContest) {
            session.lastContest.refresh()
            session.contestTitle = session.lastContest.GetPrintContestTitle(ResultFilter.Contest)
            fcService.printdone "Show live html."
            return [contestInstance:session.lastContest,liveTest:false,params:null]
        } else {
            fcService.printdone "Start."
            redirect(action:start)
        }
    }
    
	def liveview = {
		if (session?.lastContest) {
			redirect(action:listresultslive,params:[showIntern:'yes',livestartpos:1,liveshowsize:9999,livenewestshowsize:0])
        } else {
            redirect(action:start)
		}
	}
	
    def listteamresults = {
        if (session?.lastContest) {
            session.lastResultClassResults = null
            session.lastContestResults = null
            session.lastTeamResults = true
            // save return action
            session.crewReturnAction = actionName
            session.crewReturnController = controllerName
            session.crewReturnID = params.id
            session.teamReturnAction = actionName
            session.teamReturnController = controllerName
            session.teamReturnID = params.id
            // assign return action
            if (session.positionsReturnAction) {
                return [contestInstance:session.lastContest,positionsReturnAction:session.positionsReturnAction,positionsReturnController:session.positionsReturnController,positionsReturnID:session.positionsReturnID]
            }
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    def calculatepositions = {
        def contest = evaluationService.calculatecontestpositionsContest(session.lastContest,[],[],[])
        flash.message = contest.message
        if (contest.error) {
            flash.error = true
        }
        redirect(action:"listresults")
    }
    
    def addposition = {
        def contest = evaluationService.addcontestpositionContest(session.lastContest,params.crewid.toLong())
        flash.message = contest.message
        if (contest.error) {
            flash.error = true
        }
        redirect(action:"listresults")
    }

    def subposition = {
        def contest = evaluationService.subcontestpositionContest(session.lastContest,params.crewid.toLong())
        flash.message = contest.message
        if (contest.error) {
            flash.error = true
        }
        redirect(action:"listresults")
    }

    def calculateteampositions = {
        def contest = evaluationService.calculateteampositionsContest(session.lastContest,[],[]) 
        flash.message = contest.message
        if (contest.error) {
            flash.error = true
        }
        redirect(action:"listteamresults")
    }

    def addteamposition = {
        def contest = evaluationService.addteampositionContest(session.lastContest,params.teamid.toLong())
        flash.message = contest.message
        if (contest.error) {
            flash.error = true
        }
        redirect(action:"listteamresults")
    }

    def subteamposition = {
        def contest = evaluationService.subteampositionContest(session.lastContest,params.teamid.toLong())
        flash.message = contest.message
        if (contest.error) {
            flash.error = true
        }
        redirect(action:"listteamresults")
    }

    def printtest_a4_portrait = {
        if (session?.lastContest) {
			session.lastContest.refresh()
            Map contest = printService.printtestContest(session.lastContest,false,false,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                flash.error = true
                redirect(controller:"contest",action:"edit")
            } else if (contest.content) {
                printService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"test",true,false,false)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def printtest_a4_landscape = {
        if (session?.lastContest) {
			session.lastContest.refresh()
			fcService.printstart "Actual 'a4LandscapeFactor'"
			session.lastContest.a4LandscapeFactor = FcMath.toBigDecimal(params.a4LandscapeFactor)
			session.lastContest.save()
			fcService.printdone ""
            Map contest = printService.printtestContest(session.lastContest,false,true,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                flash.error = true
                redirect(controller:"contest",action:"edit")
            } else if (contest.content) {
                printService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"test",true,false,true)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def printtest_a3_portrait = {
        if (session?.lastContest) {
			session.lastContest.refresh()
			fcService.printstart "Actual 'a3PortraitFactor'"
			session.lastContest.a3PortraitFactor = FcMath.toBigDecimal(params.a3PortraitFactor)
			session.lastContest.save()
			fcService.printdone ""
            Map contest = printService.printtestContest(session.lastContest,true,false,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                flash.error = true
                redirect(controller:"contest",action:"edit")
            } else if (contest.content) {
                printService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"test",true,true,false)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def printtest_a3_landscape = {
        if (session?.lastContest) {
			session.lastContest.refresh()
			fcService.printstart "Actual 'a3LandscapeFactor'"
			session.lastContest.a3LandscapeFactor = FcMath.toBigDecimal(params.a3LandscapeFactor)
			session.lastContest.save()
			fcService.printdone ""
            Map contest = printService.printtestContest(session.lastContest,true,true,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                flash.error = true
                redirect(controller:"contest",action:"edit")
            } else if (contest.content) {
                printService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"test",true,true,true)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def listtestprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
            session.contestTitle = session.lastContest.GetPrintContestTitle(ResultFilter.Contest)
			def crew_list = Crew.findAllByContestAndDisabled(session.lastContest,false,[sort:"viewpos"])
            return [crewList:crew_list,contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    def printresults = {
        if (session?.lastContest) {
            Map contest = printService.printresultsContest(session.lastContest,session.lastContest.contestPrintA3,session.lastContest.contestPrintLandscape,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(controller:"contest",action:"listresults")
            } else if (contest.content) {
                printService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"contestresults",true,session.lastContest.contestPrintA3,session.lastContest.contestPrintLandscape)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def listresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
            session.contestTitle = session.lastContest.GetPrintContestTitle(ResultFilter.Contest)
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    def printteamresults = {
        if (session?.lastContest) {
            Map contest = printService.printteamresultsContest(session.lastContest,session.lastContest.teamPrintA3,session.lastContest.teamPrintLandscape,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                   flash.error = true
                redirect(action:"listteamresults")
            } else if (contest.content) {
                printService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"teamresults",true,session.lastContest.teamPrintA3,session.lastContest.teamPrintLandscape)
            } else {
                redirect(action:start)
            }
        } else {
            redirect(action:start)
        }
    }
    
    def listteamresultsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
            session.contestTitle = session.lastContest.GetPrintContestTitle(ResultFilter.Team)
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    def editresultsettings = {
        if (session?.lastContest) {
			session.lastContest.refresh()
            // set return action
            return [contestInstance:session.lastContest,resultfilter:ResultFilter.Contest,editresultsettingsReturnAction:"listresults",editresultsettingsReturnController:controllerName,editresultsettingsReturnID:params.id]
        } else {
            redirect(action:start)
        }
    }
    
    def editteamresultsettings = {
        if (session?.lastContest) {
			session.lastContest.refresh()
            // set return action
            return [contestInstance:session.lastContest,resultfilter:ResultFilter.Team,editteamresultsettingsReturnAction:"listteamresults",editteamresultsettingsReturnController:controllerName,editteamresultsettingsReturnID:params.id]
        } else {
            redirect(action:start)
        }
    }
    
    def standardpoints = {
        def contest = fcService.standardpointsContest(params)
        if (contest.saved) {
            flash.message = contest.message
            session.lastContest = contest.instance
               redirect(action:editpoints,id:contest.instance.id)
        } else {
            flash.message = contest.message
            redirect(action:editpoints,id:params.id)
        }
    }

    def standarddefaults = {
        def contest = fcService.standarddefaultsContest(params)
        if (contest.saved) {
            flash.message = contest.message
            session.lastContest = contest.instance
               redirect(action:editdefaults,id:contest.instance.id)
        } else {
            flash.message = contest.message
            redirect(action:editdefaults,id:params.id)
        }
    }

    def printpoints = {
        if (session?.lastContest) {
            Map contest = printService.printpointsContest(session.lastContest,session.lastContest.printPointsA3,session.lastContest.printPointsLandscape,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                flash.error = true
                redirect(action:"editpoints")
            } else if (contest.content) {
                printService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"points",true,session.lastContest.printPointsA3,session.lastContest.printPointsLandscape)
            } else {
                redirect(action:"editpoints")
            }
        } else {
            redirect(action:start)
        }
    }
    
    def pointsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    def printfreetext = {
        if (session?.lastContest) {
            Map contest = printService.printfreetextContest(session.lastContest,session.lastContest.printFreeTextA3,session.lastContest.printFreeTextLandscape,GetPrintParams()) 
            if (contest.error) {
                flash.message = contest.message
                flash.error = true
                redirect(action:"editfreetext")
            } else if (contest.content) {
                printService.WritePDF(response,contest.content,session.lastContest.GetPrintPrefix(),"freetext",true,session.lastContest.printFreeTextA3,session.lastContest.printFreeTextLandscape)
            } else {
                redirect(action:"editfreetext")
            }
        } else {
            redirect(action:start)
        }
    }
    
    def freetextprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
            return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

    Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
    
    def createtestquestion = {
        return []
    }
    
    def createtest = {
		Map ret = demoContestService.CreateTest(params.demoContest, session.showLanguage)
        redirect(controller:'contest',action:'activate',id:ret.contestid,params:[flashmessage:ret.message,flasherror:ret.error])
    }
            
    def runtest = {
        Map ret = demoContestService.RunTest(session.lastContest)
		flash.error = ret.error
		flash.message = ret.message
        redirect(controller:'contest',action:start)
    }

    def runmodultests = {
        Map ret = testService.RunTest()
        flash.error = ret.error
        flash.message = ret.message
        redirect(controller:'contest',action:start)
    }
    
    def livetracking_contestcreate = {
        def ret = trackerService.createContest(params)
        flash.message = ret.message
        if (!ret.created) {
            flash.error = true
        }
        render(view:'edit',model:[contestInstance:ret.instance])
    }
    
    def livetracking_contestconnect = {
        def ret = trackerService.connectContest(params)
        flash.message = ret.message
        if (!ret.connected) {
            flash.error = true
        }
        render(view:'edit',model:[contestInstance:ret.instance])
    }
    
    def livetracking_contestdelete = {
        def ret = trackerService.deleteContest(params)
        flash.message = ret.message
        if (!ret.deleted) {
            flash.error = true
        }
        render(view:'edit',model:[contestInstance:ret.instance])
    }
	
    def livetracking_contestdisconnect = {
        def ret = trackerService.disconnectContest(params)
        flash.message = ret.message
        if (!ret.connected) {
            flash.error = true
        }
        render(view:'edit',model:[contestInstance:ret.instance])
    }
    
    def livetracking_contestvisibility_setpublic = {
        def ret = trackerService.setContestVisibility(params, Defs.LIVETRACKING_VISIBILITY_PUBLIC)
        flash.message = ret.message
        if (ret.error) {
            flash.error = true
        }
        render(view:'edit',model:[contestInstance:ret.instance])
    }
    
    def livetracking_contestvisibility_setprivate = {
        def ret = trackerService.setContestVisibility(params, Defs.LIVETRACKING_VISIBILITY_PRIVATE)
        flash.message = ret.message
        if (ret.error) {
            flash.error = true
        }
        render(view:'edit',model:[contestInstance:ret.instance])
    }
    
    def livetracking_contestvisibility_setunlisted = {
        def ret = trackerService.setContestVisibility(params, Defs.LIVETRACKING_VISIBILITY_UNLISTED)
        flash.message = ret.message
        if (ret.error) {
            flash.error = true
        }
        render(view:'edit',model:[contestInstance:ret.instance])
    }
    
    def livetracking_teamsimport = {
        def ret = trackerService.importTeams(params, true)
        flash.message = ret.message
        if (ret.different) {
            flash.error = true
        }
        redirect(controller:"crew", action:"list") // ,model:[contestInstance:ret.instance])
    }
    
}
