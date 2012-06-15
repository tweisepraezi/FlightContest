class ResultClassController {
	
		def fcService
		
		def index = { redirect(action:list,params:params) }
	
		// the delete, save and update actions only accept POST requests
		static allowedMethods = [delete:'POST', save:'POST', update:'POST']
	
		def list = {
			fcService.printstart "List resultclasses"
			if (session?.lastContest) {
				// save return action
				session.crewReturnAction = actionName
				session.crewReturnController = controllerName
				session.crewReturnID = params.id
				session.aircraftReturnAction = actionName
				session.aircraftReturnController = controllerName
				session.aircraftReturnID = params.id
				session.resultclassReturnAction = actionName
				session.resultclassReturnController = controllerName
				session.resultclassReturnID = params.id
				def resultclassList = ResultClass.findAllByContest(session.lastContest, [sort:'name'])
				fcService.printdone "last contest"
				return [resultclassInstanceList:resultclassList]
			}
			fcService.printdone ""
			redirect(controller:'contest',action:'start')
		}
	
		def edit = {
			def resultclass = fcService.getResultClass(params)
			if (resultclass.instance) {
				// assign return action
				if (session.resultclassReturnAction) {
					return [resultclassInstance:resultclass.instance,resultclassReturnAction:session.resultclassReturnAction,resultclassReturnController:session.resultclassReturnController,resultclassReturnID:session.resultclassReturnID]
				}
				return [resultclassInstance:resultclass.instance]
			} else {
				flash.message = resultclass.message
				redirect(action:list)
			}
		}
	
		def editpoints = {
			def resultclass = fcService.getResultClass(params)
			if (resultclass.instance) {
				// assign return action
				return [resultclassInstance:resultclass.instance,editpointsReturnAction:"edit",editpointsReturnController:controllerName,editpointsReturnID:params.id]
			} else {
				flash.message = resultclass.message
				redirect(action:list)
			}
		}
	
		def update = {
			def resultclass = fcService.updateResultClass(params)
			if (resultclass.saved) {
				flash.message = resultclass.message
				// process return action
				if (params.editpointsReturnAction) {
					redirect(action:params.editpointsReturnAction,controller:params.editpointsReturnController,id:params.editpointsReturnID)
				} else if (params.resultclassReturnAction) {
					redirect(action:params.resultclassReturnAction,controller:params.resultclassReturnController,id:params.resultclassReturnID)
				} else if (params.editresultsettingsReturnAction) {
					redirect(action:params.editresultsettingsReturnAction,controller:params.editresultsettingsReturnController,id:params.editresultsettingsReturnID)
				} else {
					redirect(action:list)
				}
			} else if (resultclass.error) {
				flash.message = resultclass.message
				flash.error = true
				render(view:'edit',model:[resultclassInstance:resultclass.instance])
			} else if (resultclass.instance) {
				render(view:'edit',model:[resultclassInstance:resultclass.instance])
			} else {
				flash.message = resultclass.message
				redirect(action:edit,id:params.id)
			}
		}
	
		def savesettings = {
			def resultclass = fcService.updateResultClass(params)
			if (resultclass.saved) {
				flash.message = resultclass.message
				redirect(action:edit,id:params.id)
			} else if (resultclass.error) {
				flash.message = resultclass.message
				flash.error = true
				render(view:'edit',model:[resultclassInstance:resultclass.instance])
			} else if (resultclass.instance) {
				render(view:'edit',model:[resultclassInstance:resultclass.instance])
			} else {
				flash.message = resultclass.message
				redirect(action:edit,id:params.id)
			}
		}
	
		def savepoints = {
			def resultclass = fcService.updateResultClass(params)
			if (resultclass.saved) {
				flash.message = resultclass.message
				redirect(action:editpoints,id:params.id)
			} else if (resultclass.error) {
				flash.message = resultclass.message
				flash.error = true
				render(view:'editpoints',model:[resultclassInstance:resultclass.instance])
			} else if (resultclass.instance) {
				render(view:'editpoints',model:[resultclassInstance:resultclass.instance])
			} else {
				flash.message = resultclass.message
				redirect(action:editpoints,id:params.id)
			}
		}
	
		def calculatepoints = {
			def resultclass = fcService.calculatepointsResultClass(params)
			flash.message = resultclass.message
			redirect(action:editpoints,id:params.id)
		}
	
		def create = {
			if (session?.lastContest) {
				def resultclass = fcService.createResultClass(params,session.lastContest)
				return [contestInstance:session.lastContest,resultclassInstance:resultclass.instance]
			} else {
				redirect(controller:'contest',action:'start')
			}
		}
	
		def save = {
			def resultclass = fcService.saveResultClass(params,session.lastContest)
			if (resultclass.saved) {
				flash.message = resultclass.message
				redirect(action:list)
			} else if (resultclass.error) {
				flash.message = resultclass.message
				flash.error = true
				render(view:'create',model:[contestInstance:session.lastContest,resultclassInstance:resultclass.instance])
			} else {
				render(view:'create',model:[contestInstance:session.lastContest,resultclassInstance:resultclass.instance])
			}
		}
	
		def delete = {
			def resultclass = fcService.deleteResultClass(params)
			if (resultclass.deleted) {
				flash.message = resultclass.message
				redirect(action:list)
			} else if (resultclass.notdeleted) {
				flash.message = resultclass.message
				redirect(action:edit,id:params.id)
			} else {
				flash.message = resultclass.message
				redirect(action:list)
			}
		}
	
		def cancel = {
			if (params.editpointsReturnAction) {
				redirect(action:params.editpointsReturnAction,controller:params.editpointsReturnController,id:params.editpointsReturnID)
			} else if (params.positionsReturnAction) {
				redirect(action:params.positionsReturnAction,controller:params.positionsReturnController,id:params.positionsReturnID)
			} else if (params.resultclassReturnAction) {
				redirect(action:params.resultclassReturnAction,controller:params.resultclassReturnController,id:params.resultclassReturnID)
			} else if (params.editresultsettingsReturnAction) {
				redirect(action:params.editresultsettingsReturnAction,controller:params.editresultsettingsReturnController,id:params.editresultsettingsReturnID)
			} else {
				redirect(action:list)
			}
		}
	
		def listprintable = {
			if (params.contestid) {
				session.lastContest = Contest.get(params.contestid)
			}
			if (session?.lastContest) {
				def resultclassList = ResultClass.findAllByContest(session.lastContest,[sort:"name"])
				return [resultclassInstanceList:resultclassList]
			}
			return [:]
		}
	
		def print = {
			def resultclasses = fcService.printResultClasses(params,GetPrintParams())
			if (resultclasses.error) {
				flash.message = resultclasses.message
				flash.error = true
				redirect(action:list)
			} else if (resultclasses.content) {
				fcService.WritePDF(response,resultclasses.content)
			} else {
				redirect(action:list)
			}
		}
		
		def listresults = {
			def resultclass = fcService.getResultClass(params)
			if (resultclass.instance) {
	            session.lastTaskResults = null
				session.lastResultClassResults = resultclass.instance.id
				session.lastContestResults = null
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
				// assign return action
				if (session.positionsReturnAction) {
					return [resultclassInstance:resultclass.instance,,positionsReturnAction:session.positionsReturnAction,positionsReturnController:session.positionsReturnController,positionsReturnID:session.positionsReturnID]
				}
	        	return [resultclassInstance:resultclass.instance]
	        } else {
	            redirect(action:"listresults")
	        }
	    }
	
		def calculatepositions = {
			ResultClass resultclass_instance = ResultClass.get(params.id)
	        def resultclass = fcService.calculatepositionsResultClass(resultclass_instance,[]) 
	        flash.message = resultclass.message
			if (resultclass.error) {
				flash.error = true
			}
	        redirect(action:"listresults",id:params.id)
		}
	
		def printresults = {
			def resultclass = fcService.getResultClass(params)
			if (resultclass.instance) {
		        def resultclass_print = fcService.printresultsResultClass(resultclass.instance,GetPrintParams()) 
		        if (resultclass_print.error) {
		        	flash.message = resultclass_print.message
		           	flash.error = true
					redirect(action:"listresults",id:params.id)
		        } else if (resultclass_print.content) {
		        	fcService.WritePDF(response,resultclass_print.content)
			    } else {
					redirect(action:"listresults",id:params.id)
			    }
	        } else {
	            redirect(action:"listresults",id:params.id)
	        }
		}
		
		def listresultsprintable = {
	        if (params.resultclassid) {
	            ResultClass resultclass_instance = ResultClass.get(params.resultclassid)
				session.lastContest = resultclass_instance.contest
				session.contestTitle = resultclass_instance.GetPrintContestTitle()
	        	return [resultclassInstance:resultclass_instance]
	        } else {
	            redirect(action:"listresults",id:params.resultclassid)
	        }
	    }
	
		def editresultsettings = {
			def resultclass = fcService.getResultClass(params)
			if (resultclass.instance) {
				// set return action
	           	return [resultclassInstance:resultclass.instance,resultfilter:ResultFilter.ResultClass,editresultsettingsReturnAction:"listresults",editresultsettingsReturnController:controllerName,editresultsettingsReturnID:params.id]
	        } else {
	            redirect(action:start)
	        }
		}
		
		def printpoints = {
			def resultclass = fcService.getResultClass(params)
			if (resultclass.instance) {
		        def resultclass_print = fcService.printpointsResultClass(resultclass.instance,GetPrintParams()) 
		        if (resultclass_print.error) {
		        	flash.message = resultclass_print.message
		           	flash.error = true
					redirect(action:"editpoints",id:params.id)
		        } else if (resultclass_print.content) {
		        	fcService.WritePDF(response,resultclass_print.content)
			    } else {
					redirect(action:"editpoints",id:params.id)
			    }
	        } else {
	            redirect(action:"editpoints",id:params.id)
	        }
		}
		
		def pointsprintable = {
	        if (params.resultclassid) {
	            ResultClass resultclass_instance = ResultClass.get(params.resultclassid)
				session.lastContest = resultclass_instance.contest
				session.contestTitle = resultclass_instance.GetPrintContestTitle()
	        	return [resultclassInstance:resultclass_instance]
	        } else {
	            redirect(action:"listresults",id:params.resultclassid)
	        }
	    }
	
		Map GetPrintParams() {
			return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
					contest:session.lastContest,
					lang:session.printLanguage
				   ]
		}
	}
	