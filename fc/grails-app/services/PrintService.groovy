import java.util.List;
import org.xhtmlrenderer.extend.FontResolver
import org.xhtmlrenderer.pdf.ITextRenderer
import org.springframework.web.context.request.RequestContextHolder
import com.lowagie.text.pdf.BaseFont
import java.net.URLEncoder

class PrintService
{
    def logService
    def domainService
    def servletContext
    def gpxService
    def messageSource
    
    //--------------------------------------------------------------------------
    Map printAircrafts(Map params, boolean a3, boolean landscape, printparams)
    {
        Map aircrafts = [:]

        // Print aircrafts
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/aircraft/listprintable?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            aircrafts.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            aircrafts.message = getMsg('fc.aircraft.printerror',["$e"])
            aircrafts.error = true
        }
        return aircrafts
    }
    
    //--------------------------------------------------------------------------
    Map printtestContest(Contest contestInstance, boolean a3, boolean landscape, printparams)
    {
        Map contest = [:]
        
        // Print test
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/contest/listtestprintable?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            contest.content = content.toByteArray() 
            content.close()
        }
        catch (Throwable e) {
            contest.message = getMsg('fc.print.error',["$e"])
            contest.error = true
        }
        return contest
    }
    
    //--------------------------------------------------------------------------
    Map printresultsContest(Contest contestInstance,boolean a3,boolean landscape,printparams)
    {
        Map contest = [:]
        
        // Positions calculated?
        boolean no_position_error = false
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
            if (crew_instance.disabled || (crew_instance.contestPenalties == -1) || crew_instance.noContestPosition) {
                if (crew_instance.contestPosition) {
                    no_position_error = true
                }
            } else {
                if (!crew_instance.contestPosition) {
                    no_position_error = true
                }
            }
        }
        if (no_position_error) {
            contest.message = getMsg('fc.results.positions2calculate')
            contest.error = true
            return contest
        }
        
        // No equal positions?
        boolean equal_position_error = false
        if (!contestInstance.contestPrintEqualPositions) {
            Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
                if (crew_instance.contestEqualPosition) {
                   equal_position_error = true
                }
            }
        }
        if (equal_position_error) {
            contest.message = getMsg('fc.results.positions2set')
            contest.error = true
            return contest
        }
        
        // Print positions
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/contest/listresultsprintable?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            contest.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            contest.message = getMsg('fc.print.error',["$e"])
            contest.error = true
        }
        return contest
    }
    
    //--------------------------------------------------------------------------
    Map printteamresultsContest(Contest contestInstance, boolean a3, boolean landscape, printparams)
    {
        Map contest = [:]
        
        // Positions calculated?
        boolean no_position_error = false
        for (Team team_instance in Team.findAllByContest(contestInstance,[sort:"id"])) {
            if (team_instance.IsActiveTeam()) {
                if (!team_instance.contestPosition) {
                    no_position_error = true
                }
            }
        }
        if (no_position_error) {
            contest.message = getMsg('fc.results.positions2calculate')
            contest.error = true
            return contest
        }
        
        // No equal positions?
        boolean equal_position_error = false
        if (!contestInstance.teamPrintEqualPositions) {
            for (Team team_instance in Team.findAllByContest(contestInstance,[sort:"id"])) {
                if (team_instance.IsActiveTeam()) {
                    if (team_instance.contestEqualPosition) {
                        equal_position_error = true
                    }
                }
            }
        }
        if (equal_position_error) {
            contest.message = getMsg('fc.results.positions2set')
            contest.error = true
            return contest
        }
        
        // Print positions
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/contest/listteamresultsprintable?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            contest.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            contest.message = getMsg('fc.print.error',["$e"])
            contest.error = true
        }
        return contest
    }
    
    //--------------------------------------------------------------------------
    Map printpointsContest(Contest contestInstance, boolean a3, boolean landscape, printparams)
    {
        Map contest = [:]
        
        // Print points
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/contest/pointsprintable?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            contest.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            contest.message = getMsg('fc.print.error',["$e"])
            contest.error = true
        }
        return contest
    }
    
    //--------------------------------------------------------------------------
    Map printfreetextContest(Contest contestInstance, boolean a3, boolean landscape, printparams)
    {
        Map contest = [:]
        
        // Print points
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/contest/freetextprintable?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            contest.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            contest.message = getMsg('fc.print.error',["$e"])
            contest.error = true
        }
        return contest
    }
    
    //--------------------------------------------------------------------------
    Map printCoord(Map params,boolean a3, boolean landscape,printparams,String detail)
    {
        Map route = domainService.GetRoute(params)
        if (!route.instance) {
            return route
        }
        
        // Print coordinates of route
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/route/showcoord${detail}printable/${route.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            route.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            route.message = getMsg('fc.route.printerror',["$e"])
            route.error = true
        }
        return route
    }
    
    //--------------------------------------------------------------------------
    Map printEnroutePhoto(Map params, boolean alphabetical, printparams)
    {
        Map route = domainService.GetRoute(params)
        if (!route.instance) {
            return route
        }
        
        // Print enroute photos of route
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/route/showenroutephotoprintable/${route.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&alphabetical=${alphabetical}&a3=${route.instance.enroutePhotoPrintStyle.a3}&landscape=${route.instance.enroutePhotoPrintStyle.landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            route.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            route.message = getMsg('fc.route.printerror',["$e"])
            route.error = true
        }
        return route
    }
    
    //--------------------------------------------------------------------------
    Map printTurnpointPhoto(Map params, boolean alphabetical, printparams)
    {
        Map route = domainService.GetRoute(params)
        if (!route.instance) {
            return route
        }
        
        // Print turnpoint photos of route
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/route/showturnpointphotoprintable/${route.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&alphabetical=${alphabetical}&a3=${route.instance.turnpointPrintStyle.a3}&landscape=${route.instance.turnpointPrintStyle.landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            route.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            route.message = getMsg('fc.route.printerror',["$e"])
            route.error = true
        }
        return route
    }
    
    //--------------------------------------------------------------------------
    Map printCrews(Map params, boolean a3, boolean landscape, printparams)
    {
        Map crews = [:]

        // Print crews
        try {
            ITextRenderer renderer = new ITextRenderer()
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/crew/listprintable?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)

			if (printparams.contest.printTeams) {
				url = "${printparams.baseuri}/team/listprintable?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${printparams.contest.printTeamLandscape}"
				println "Print: $url"
				renderer.setDocument(url)
				renderer.layout()
				renderer.writeNextDocument(1)
			}
			
			if (printparams.contest.printAircraft) {
				url = "${printparams.baseuri}/aircraft/listprintable?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${printparams.contest.printAircraftLandscape}"
				println "Print: $url"
				renderer.setDocument(url)
				renderer.layout()
				renderer.writeNextDocument(1)
			}
			
            renderer.finishPDF()
            crews.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            crews.message = getMsg('fc.crew.printerror',["$e"])
            crews.error = true
        }
        return crews
    }
    
    //--------------------------------------------------------------------------
    Map printResultClasses(Map params, boolean a3, boolean landscape, printparams)
    {
        Map resultclasses = [:]

        // Print resultclasses
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/resultClass/listprintable?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            resultclasses.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            resultclasses.message = getMsg('fc.resultclass.printerror',["$e"])
            resultclasses.error = true
        }
        return resultclasses
    }
    
    //--------------------------------------------------------------------------
    Map printresultsResultClass(ResultClass resultclassInstance, boolean a3, boolean landscape, printparams)
    {
        println "printresultsResultClass"
        
        Map resultclass = [:]
        
        // Positions calculated? 
        boolean no_position_error = false
        Crew.findAllByResultclass(resultclassInstance,[sort:"id"]).each { Crew crew_instance ->
            if (crew_instance.disabled || (crew_instance.contestPenalties == -1) || crew_instance.noClassPosition) {
                if (crew_instance.classPosition) {
                    no_position_error = true
                }
            } else if (crew_instance.resultclass.id == resultclassInstance.id) {
                if (!crew_instance.classPosition) {
                    no_position_error = true
                }
            }
        }
        if (no_position_error) {
            resultclass.message = getMsg('fc.results.positions2calculate')
            resultclass.error = true
            return resultclass
        }
        
        // No equal positions?
        boolean equal_position_error = false
        if (!resultclassInstance.contestPrintEqualPositions) {
            Crew.findAllByResultclass(resultclassInstance,[sort:"id"]).each { Crew crew_instance ->
                if (crew_instance.classEqualPosition) {
                    equal_position_error = true
                }
            }
        }
        if (equal_position_error) {
            resultclass.message = getMsg('fc.results.positions2set')
            resultclass.error = true
            return resultclass
        }
        
        // Print positions
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/resultClass/listresultsprintable/${resultclassInstance.id}?print=1&lang=${printparams.lang}&resultclassid=${resultclassInstance.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            resultclass.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            resultclass.message = getMsg('fc.print.error',["$e"])
            resultclass.error = true
        }
        return resultclass
    }
    
    //--------------------------------------------------------------------------
    Map printpointsResultClass(ResultClass resultclassInstance, boolean a3, boolean landscape, printparams)
    {
        println "printpointsResultClass"
        
        Map resultclass = [:]
        
        // Print points
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/resultClass/pointsprintable/${resultclassInstance.id}?print=1&lang=${printparams.lang}&resultclassid=${resultclassInstance.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            resultclass.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            resultclass.message = getMsg('fc.print.error',["$e"])
            resultclass.error = true
        }
        return resultclass
    }
    
    //--------------------------------------------------------------------------
    Map printRoute(Map params, boolean a3, boolean landscape, printparams)
    {
        Map route = domainService.GetRoute(params)
        if (!route.instance) {
            return route
        }
        
        // Print route
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/route/showprintable/${route.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            route.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            route.message = getMsg('fc.route.printerror',["$e"])
            route.error = true
        }
        return route
    }
    
    //--------------------------------------------------------------------------
    Map ConvertRoute2PDF(Route routeInstance, String pdfFileName, boolean a3, boolean landscape, printparams)
    {
        printstart "ConvertRoute2PDF ${routeInstance.name()} -> ${pdfFileName}"
        Map route = printRoute([id:routeInstance.id], a3, landscape, printparams)
        if (route.error) {
            printerror ""
            return [ok:false]
        }
        File pdf_file = new File(pdfFileName)
        DataOutputStream pdf_writer = pdf_file.newDataOutputStream()
        pdf_writer.write(route.content)
        pdf_writer.close()
        printdone ""
        return [ok:true]
    }
    
    //--------------------------------------------------------------------------
    Map printRoutes(Map params, boolean a3, boolean landscape, printparams)
    {
        Map routes = [:]

        // Print routes
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Route.findAllByContest(printparams.contest,[sort:"idTitle"]).each { Route route_instance ->
                String url = "${printparams.baseuri}/route/showprintable/${route_instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
                println "Print: $url"
                renderer.setDocument(url)
                renderer.layout()
                if (first_pdf) {
                    renderer.createPDF(content,false)
                    first_pdf = false
                } else {
                    renderer.writeNextDocument(1)
                }
                routes.found = true
            }
            renderer.finishPDF()
            routes.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            routes.message = getMsg('fc.route.printerror',["$e"])
            routes.error = true
        }
        return routes
    }
    
    //--------------------------------------------------------------------------
    Map printmapRoute(String printSize, boolean landscape, String mapFileName, printparams)
    {
        Map ret = [:]
        
        // Print contest map
        try {
            String print_action = "mapprintable"
            if (printSize == Defs.CONTESTMAPPRINTSIZE_ANR) {
                print_action = "mapanrprintable"
            }
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/route/${print_action}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&printSize=${printSize}&landscape=${landscape}"
            String map_file_name = ("file:///" + URLEncoder.encode(mapFileName, "UTF-8")).replaceAll('\\\\','/')
            url += "&mapFileName=${map_file_name}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            ret.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            ret.message = getMsg('fc.contestmap.printerror',["$e"])
            ret.error = true
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map printtimetableTask(Map params, printparams, boolean isJury)
    {
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            return task
        }

        boolean a3 = false
        boolean landscape = false
        if (isJury) {
            a3 = task.instance.printTimetableJuryA3
            landscape = task.instance.printTimetableJuryLandscape
            println "printtimetableTask Judge (${task.instance.name()})"
        } else {
            a3 = task.instance.printTimetableA3
            landscape = task.instance.printTimetableLandscape
            println "printtimetableTask Public (${task.instance.name()})"
        }
        
        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
               task.error = true
            return task
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
            task.message = getMsg('fc.flighttestwind.notfound')
            task.error = true
            return task
        }
        
        // FlightTestWind assigned to all crews?
        boolean call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (!test_instance.flighttestwind) {
                    call_return = true
                }
            }
        }
        if (call_return) {
            task.message = getMsg('fc.flighttestwind.notassigned')
            task.error = true
            return task
        }

        // Have all crews an aircraft?
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.taskAircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            return task
        }

        // Timetable calculated?  
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (!test_instance.timeCalculated) {
                    call_return = true
                }
            }
        }
        if (call_return) {
            task.message = getMsg('fc.test.timetable.newcalculate')
            task.error = true
            return task
        }        
        
        // Warnings?  
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (test_instance.arrivalTimeWarning || test_instance.takeoffTimeWarning) {
                    call_return = true
                }
            }
        }
        if (call_return) {
            task.message = getMsg('fc.test.flightplan.resolvewarnings')
            task.error = true
            return task
        }        
        
        // Print timetable
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = ""
            if (isJury) {
                url = "${printparams.baseuri}/task/timetablejudgeprintable/${task.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            } else {
                url = "${printparams.baseuri}/task/timetableprintable/${task.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            }
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            task.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            task.message = getMsg('fc.print.error',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printtimetableoverviewTask(Map params, printparams)
    {
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            return task
        }

        boolean a3 = false
        boolean landscape = false
        a3 = task.instance.printTimetableOverviewA3
        landscape = task.instance.printTimetableOverviewLandscape
        println "printtimetableoverviewTask (${task.instance.name()})"

        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
            task.error = true
            return task
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
            task.message = getMsg('fc.flighttestwind.notfound')
            task.error = true
            return task
        }
        
        // FlightTestWind assigned to all crews?
        boolean call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (!test_instance.flighttestwind) {
                    call_return = true
                }
            }
        }
        if (call_return) {
            task.message = getMsg('fc.flighttestwind.notassigned')
            task.error = true
            return task
        }

        // Have all crews an aircraft?
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.taskAircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            return task
        }

        // Timetable calculated?  
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (!test_instance.timeCalculated) {
                    call_return = true
                }
            }
        }
        if (call_return) {
            task.message = getMsg('fc.test.timetable.newcalculate')
            task.error = true
            return task
        }        
        
        // Warnings?  
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (test_instance.arrivalTimeWarning || test_instance.takeoffTimeWarning) {
                    call_return = true
                }
            }
        }
        if (call_return) {
            task.message = getMsg('fc.test.flightplan.resolvewarnings')
            task.error = true
            return task
        }        
        
        // Print overview timetable
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = ""
            url = "${printparams.baseuri}/task/timetableoverviewprintable/${task.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            task.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            task.message = getMsg('fc.print.error',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printlandingstartlistTask(Map params, printparams)
    {
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            return task
        }

        boolean a3 = false
        boolean landscape = false
        a3 = task.instance.printLandingStartlistA3
        landscape = task.instance.printLandingStartlistLandscape
        println "printlandingstartlistTask Judge (${task.instance.name()})"
        
        // Have all crews an aircraft?
        boolean call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.taskAircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            return task
        }

        // Print landing start list
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = ""
			url = "${printparams.baseuri}/task/landingstartlistprintable/${task.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            task.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            task.message = getMsg('fc.print.error',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printflightplansTask(Map params, boolean a3, boolean landscape, printparams)
    {
        printstart "printflightplansTask"
        
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            printerror ""
            return task
        }

        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
            task.error = true
            printerror task.message
            return task
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
            task.message = getMsg('fc.flighttestwind.notfound')
            task.error = true
            printerror task.message
            return task
        }
        
        // FlightTestWind assigned to all crews?
        boolean call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (!test_instance.flighttestwind) {
                    call_return = true
                }
            }
        }
        if (call_return) {
            task.message = getMsg('fc.flighttestwind.notassigned')
            task.error = true
            printerror task.message
            return task
        }

        // Have all crews an aircraft?
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.taskAircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            printerror task.message
            return task
        }

        // Timetable calculated?  
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (!test_instance.timeCalculated) {
                    call_return = true
                }
            }
        }
        if (call_return) {
            task.message = getMsg('fc.test.timetable.newcalculate')
            task.error = true
            printerror task.message
            return task
        }        
        
        // Warnings?  
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (test_instance.arrivalTimeWarning || test_instance.takeoffTimeWarning) {
                    call_return = true
                }
            }
        }
        if (call_return) {
            task.message = getMsg('fc.test.flightplan.resolvewarnings')
            task.error = true
            printerror task.message
            return task
        }        
        
        // Someone selected?
        boolean someone_selected = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                    someone_selected = true
                }
            }
        }
        if (!someone_selected) {
            task.message = getMsg('fc.test.flightplan.someonemustselected')
            task.error = true
            printerror task.message
            return task
        }
        
        // Print flightplans
        try {
            String print_action = "flightplanprintable"
            String print_params = ""
            if (task.instance.flighttest.route.corridorWidth) {
                switch (task.instance.flighttest.flightPlanDesign) {
                    case FlightPlanDesign.TPList:
                        print_action = "flightplananrprintable"
                        print_params = "&tplist=${true}"
                        break
                    case FlightPlanDesign.OnlyTimes:
                        print_action = "flightplananrprintable"
                        break
                    case FlightPlanDesign.Map:
                        print_action = "flightplanmapanrprintable"
                        print_params = "&crews=${true}"
                        break
                }
            }
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
                if (params["selectedTestID${test_instance.id}"] == "on") {
                    if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                        String url = "${printparams.baseuri}/test/${print_action}/${test_instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}${print_params}"
                        println "Print: $url"
                        renderer.setDocument(url)
                        renderer.layout()
                        if (first_pdf) {
                            renderer.createPDF(content,false)
                            first_pdf = false
                        } else {
                            renderer.writeNextDocument(1)
                        }
                    }
                }
            }
            renderer.finishPDF()
            task.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            task.message = getMsg('fc.test.flightplan.printerror',["$e"])
            task.error = true
            printerror task.message
        }
        printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printplanningtasksTask(Map params, boolean a3, boolean landscape, printparams)
    {
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            return task
        }

        // PlanningTest exists?
        if (!task.instance.planningtest) {
            task.message = getMsg('fc.planningtest.notfound')
            task.error = true
            return task
        }

        // PlanningTestTask exists?
        if (!PlanningTestTask.countByPlanningtest(task.instance.planningtest)) {
            task.message = getMsg('fc.planningtesttask.notfound')
               task.error = true
            return task
        }

        // PlanningTestTask assigned to all crews?
        boolean call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (!test_instance.planningtesttask) {
                    call_return = true
                }
            }
        }
        if (call_return) {
            task.message = getMsg('fc.planningtesttask.notassigned')
            task.error = true
            return task
        }

        // Someone selected?
        boolean someone_selected = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                    someone_selected = true
                }
            }
        }
        if (!someone_selected) {
            task.message = getMsg('fc.planningtesttask.someonemustselected.print')
            task.error = true
            printerror task.message
            return task
        }
        
        // Print PlanningTasks
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
                if (params["selectedTestID${test_instance.id}"] == "on") {
                    if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                        String url = "${printparams.baseuri}/test/planningtaskprintable/${test_instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}&results=no"
                        println "Print: $url"
                        renderer.setDocument(url)
                        renderer.layout()
                        if (first_pdf) {
                            renderer.createPDF(content,false)
                            first_pdf = false
                        } else {
                            renderer.writeNextDocument(1)
                        }
                    }
                }
            }
            renderer.finishPDF()
            task.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            task.message = getMsg('fc.planningtesttask.printerror',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printobservationTask(Map params, boolean a3, boolean landscape, printparams)
    {
        printstart "printobservationTask"
        
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            printerror ""
            return task
        }

        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
            task.error = true
            printerror task.message
            return task
        }
        
        List test_instance_ids = [""]
        int assign_num = 0
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                test_instance_ids += test_instance.id.toString()
                assign_num++
            }
        }
        task.testinstanceids = test_instance_ids
        if (!assign_num) {
            task.message = getMsg('fc.observation.print.someonemustselected')
            task.error = true
            printerror task.message
            return task
        }
        
        // Print observations
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
                if (params["selectedTestID${test_instance.id}"] == "on") {
                    if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                        String url = "${printparams.baseuri}/test/observationprintable/${test_instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
                        println "Print: $url"
                        renderer.setDocument(url)
                        renderer.layout()
                        if (first_pdf) {
                            renderer.createPDF(content,false)
                            first_pdf = false
                        } else {
                            renderer.writeNextDocument(1)
                        }
                    }
                }
            }
            renderer.finishPDF()
            task.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            task.message = getMsg('fc.observation.print.error',["$e"])
            task.error = true
        }
        
        printdone ""
        return task
        
    }
    
    //--------------------------------------------------------------------------
    Map printneutralobservationFlightTest(boolean printResults, Map params, boolean a3, boolean landscape, printparams)
    {
        printstart "printneutralobservationFlightTest results=${printResults}"
        
        FlightTest flighttest_instance = FlightTest.get(params.id)
        
        if (flighttest_instance) {
            Map flighttest = ['instance':flighttest_instance]
            List result_class_ids = flighttest_instance.task.GetObservationResultClassIDs(printResults)
            if (result_class_ids) {
                // Print neutral or result observation formular for each resultclass
                try {
                    ITextRenderer renderer = new ITextRenderer();
                    addFonts(renderer)
                    ByteArrayOutputStream content = new ByteArrayOutputStream()
                    boolean first_pdf = true
                    for (long result_class_id in result_class_ids) {
                        String url = "${printparams.baseuri}/task/observationprintable/${flighttest_instance.task.id}?print=1&results=${printResults}&resultclassid=${result_class_id}&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
                        println "Print: $url"
                        renderer.setDocument(url)
                        renderer.layout()
                        if (first_pdf) {
                            renderer.createPDF(content,false)
                            first_pdf = false
                        } else {
                            renderer.writeNextDocument(1)
                        }
                    }
                    renderer.finishPDF()
                    flighttest.content = content.toByteArray()
                    content.close()
                }
                catch (Throwable e) {
                    flighttest.message = getMsg('fc.observation.print.error',["$e"])
                    flighttest.error = true
                }
            } else {
                // Print neutral or result observation formular
                try {
                    ITextRenderer renderer = new ITextRenderer();
                    addFonts(renderer)
                    ByteArrayOutputStream content = new ByteArrayOutputStream()
                    String url = "${printparams.baseuri}/task/observationprintable/${flighttest_instance.task.id}?print=1&results=${printResults}&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
                    println "Print: $url"
                    renderer.setDocument(url)
                    renderer.layout()
                    renderer.createPDF(content,false)
                    renderer.finishPDF()
                    flighttest.content = content.toByteArray()
                    content.close()
                }
                catch (Throwable e) {
                    flighttest.message = getMsg('fc.observation.print.error',["$e"])
                    flighttest.error = true
                }
            }
            printdone ""
            return flighttest
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
            printerror ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map printneutralanrplanFlightTest(boolean printResults, Map params, boolean a3, boolean landscape, printparams)
    {
        printstart "printneutralanrplanFlightTest results=${printResults}"
        
        FlightTest flighttest_instance = FlightTest.get(params.id)
        if (flighttest_instance) {
            Map flighttest = ['instance':flighttest_instance]
            if (printResults) {
                List test_instances = []
                BigDecimal last_tas = null
                for (Test test_instance in flighttest_instance.GetPrintableANRPlanTests()) {
                    if (last_tas != test_instance.taskTAS) {
                        test_instances += test_instance
                    }
                    last_tas = test_instance.taskTAS
                }
                if (test_instances) {
                    ITextRenderer renderer = new ITextRenderer();
                    addFonts(renderer)
                    ByteArrayOutputStream content = new ByteArrayOutputStream()
                    boolean first_pdf = true
                    int page = 0
                    for (Test test_instance in test_instances) {
                        page++
                        String url = "${printparams.baseuri}/test/flightplanmapanrprintable/${test_instance.id}?print=1&results=${printResults}&page=${page}&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
                        println "Print: $url"
                        renderer.setDocument(url)
                        renderer.layout()
                        if (first_pdf) {
                            renderer.createPDF(content,false)
                            first_pdf = false
                        } else {
                            renderer.writeNextDocument(1)
                        }
                    }
                    renderer.finishPDF()
                    flighttest.content = content.toByteArray()
                    content.close()
                }
            } else {
                Test test_instance = Test.findByTask(flighttest_instance.task)
                if (test_instance) {
                    try {
                        ITextRenderer renderer = new ITextRenderer();
                        addFonts(renderer)
                        ByteArrayOutputStream content = new ByteArrayOutputStream()
                        String url = "${printparams.baseuri}/test/flightplanmapanrprintable/${test_instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
                        println "Print: $url"
                        renderer.setDocument(url)
                        renderer.layout()
                        renderer.createPDF(content,false)
                        renderer.finishPDF()
                        flighttest.content = content.toByteArray()
                        content.close()
                    }
                    catch (Throwable e) {
                        flighttest.message = getMsg('fc.observation.print.error',["$e"])
                        flighttest.error = true
                    }
                } else {
                    Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
                    printerror ret.message
                    return ret
                }
            }
            printdone ""
            return flighttest
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
            printerror ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map printresultclassresultsTask(Map params, boolean a3, boolean landscape, printparams)
    {
        printstart "printresultclassresultsTask"

        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            printerror ""
            return task
        }

        task.instance.printAircraft = params.printAircraft == "on"
        task.instance.printTeam = params.printTeam == "on"
        task.instance.printClass = params.printClass == "on"
        task.instance.printShortClass = params.printShortClass == "on"
        task.instance.printProvisionalResults = params.printProvisionalResults == "on"
        
        // Print task results of selected result classes
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            for (ResultClass resultclass_instance in ResultClass.findAllByContest(task.instance.contest,[sort:"id"])) {
                if (params["resultclass_${resultclass_instance.id}"] == "on") {
                    String url = "${printparams.baseuri}/task/listresultsprintable/${task.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&resultclassid=${resultclass_instance.id}&a3=${a3}&landscape=${landscape}&printAircraft=${task.instance.printAircraft}&printTeam=${task.instance.printTeam}&printClass=${task.instance.printClass}&printShortClass=${task.instance.printShortClass}&printProvisionalResults=${task.instance.printProvisionalResults}"
                    println "Print: $url"
                    renderer.setDocument(url)
                    renderer.layout()
                    if (first_pdf) {
                        renderer.createPDF(content,false)
                        first_pdf = false
                    } else {
                        renderer.writeNextDocument(1)
                    }
                    task.found = true
                }
            }
            renderer.finishPDF()
            task.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            task.message = getMsg('fc.print.error',["$e"])
            task.error = true
        }
        if (task.error) {
            printerror task.message
        } else {
            printdone ""
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printcrewresultsTask(Map params, boolean a3, boolean landscape, String webRootDir, printparams)
    {
        printstart "printcrewresultsTask"
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            printerror "No task."
            return task
        }

        task.instance.printSummaryResults = params.printSummaryResults == "on"
        task.instance.printPlanningResults = params.printPlanningResults == "on"
        task.instance.printPlanningResultsScan = params.printPlanningResultsScan == "on"
        task.instance.printFlightResults = params.printFlightResults == "on"
        task.instance.printFlightMap = params.printFlightMap == "on"
        task.instance.printObservationResults = params.printObservationResults == "on"
        task.instance.printObservationResultsScan = params.printObservationResultsScan == "on"
        task.instance.printLandingResults = params.printLandingResults == "on"
        task.instance.printSpecialResults = params.printSpecialResults == "on"
        task.instance.printModifiedResults = params.printResults == "ModifiedResults"
        task.instance.printCompletedResults = params.printResults == "CompletedResults"
        task.instance.printProvisionalResults = params.printProvisionalResults == "on"
        
        // Print all crewresults
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            for ( Test test_instance in Test.findAllByTask(task.instance,[sort:"viewpos"])) {
                if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                    if (isprintcrewresult(params,test_instance)) {
                        boolean print_crew_results = true
                        if (task.instance.printModifiedResults && !test_instance.crewResultsModified) {
                            print_crew_results = false
                        }
                        if (task.instance.printCompletedResults && test_instance.IsTestResultsProvisional(test_instance.GetResultSettings())) {
                            print_crew_results = false
                        }
                        if (print_crew_results) {
                            printstart "Print $test_instance.crew.name"
                            String url = "${printparams.baseuri}/test/crewresultsprintable/${test_instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}&printSummaryResults=${task.instance.printSummaryResults}&printPlanningResults=${task.instance.printPlanningResults}&printPlanningResultsScan=${task.instance.printPlanningResultsScan}&printFlightResults=${task.instance.printFlightResults}&printFlightMap=${task.instance.printFlightMap}&printObservationResults=${task.instance.printObservationResults}&printObservationResultsScan=${task.instance.printObservationResultsScan}&printLandingResults=${task.instance.printLandingResults}&printSpecialResults=${task.instance.printSpecialResults}&printProvisionalResults=${task.instance.printProvisionalResults}"
                            if (!task.instance.GetDetailNum()) {
                                url += "&disabletitle=yes"
                            }
                            String uuid = UUID.randomUUID().toString()
                            String flight_gpx_file_name = "${GpxService.GPXDATA}-${uuid}"
                            String flight_map_file_name = "${webRootDir}${Defs.ROOT_FOLDER_GPXUPLOAD}/PNG-${uuid}-FLIGHT.png"
                            boolean is_flight = false
                            if (task.instance.printFlightMap && test_instance.IsFlightTestRun() && test_instance.IsShowMapPossible()) {
                                Map converter = gpxService.ConvertTest2PrintMap(test_instance, flight_gpx_file_name, flight_map_file_name)
                                if (converter.ok && converter.track) {
                                    String flight_map_file_name2 = ("file:///" + HTMLFilter.GetStr(flight_map_file_name)).replaceAll('\\\\','/')
                                    url += "&flightMapFileName=${flight_map_file_name2}"
                                }
                                is_flight = true
                            }
                            println "Print: $url"
                            renderer.setDocument(url)
                            renderer.layout()
                            if (first_pdf) {
                                renderer.createPDF(content,false)
                                first_pdf = false
                            } else {
                                renderer.writeNextDocument(1)
                            }
                            if (is_flight) {
                                gpxService.DeleteFile(flight_gpx_file_name)
                                gpxService.DeleteFile(flight_map_file_name)
                            }
                            printdone ""
                        }
                    }
                }
            }
            renderer.finishPDF()
            task.content = content.toByteArray()
            content.close()
            printdone ""
        }
        catch (Throwable e) {
            task.message = getMsg('fc.crewresults.printerror',["$e"])
            task.error = true
            printerror task.message 
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    private boolean isprintcrewresult(Map params, Test testInstance) 
    {
        if (testInstance.task.contest.resultClasses) {
            for (ResultClass resultclass_instance in ResultClass.findAllByContest(testInstance.task.contest,[sort:"id"])) {
                if (params["resultclass_${resultclass_instance.id}"] == "on") {
                    if (testInstance.crew.resultclass) {
                        if (testInstance.crew.resultclass.id == resultclass_instance.id) {
                            return true
                        }
                    }
                }
            }
            return false
        }
        return true
    }
    
    //--------------------------------------------------------------------------
    Map printresultsTask(Map params,boolean a3, boolean landscape,printparams)
    {
        printstart "printresultsTask"
        
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            printerror ""
            return task
        }

        task.instance.printAircraft = params.printAircraft == "on"
        task.instance.printTeam = params.printTeam == "on"
        task.instance.printClass = params.printClass == "on"
        task.instance.printShortClass = params.printShortClass == "on"
        task.instance.printProvisionalResults = params.printProvisionalResults == "on"
        
        // Print task results
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/task/listresultsprintable/${task.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}&printAircraft=${task.instance.printAircraft}&printTeam=${task.instance.printTeam}&printClass=${task.instance.printClass}&printShortClass=${task.instance.printShortClass}&printProvisionalResults=${task.instance.printProvisionalResults}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            task.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            task.message = getMsg('fc.print.error',["$e"])
            task.error = true
        }
        if (task.error) {
            printerror task.message
        } else {
            printdone ""
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printTeams(Map params, boolean a3, boolean landscape, printparams)
    {
        Map teams = [:]

        // Print teams
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/team/listprintable?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            teams.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            teams.message = getMsg('fc.team.printerror',["$e"])
            teams.error = true
        }
        return teams
    }
    
    //--------------------------------------------------------------------------
    Map printflightresultsTest(Map params, boolean a3, boolean landscape, String webRootDir, printparams)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print flight test results
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/flightresultsprintable/${test.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            String uuid = UUID.randomUUID().toString()
            String flight_gpx_file_name = "${GpxService.GPXDATA}-${uuid}"
            String flight_map_file_name = "${webRootDir}${Defs.ROOT_FOLDER_GPXUPLOAD}/PNG-${uuid}-FLIGHT.png"
            if (test.instance.IsShowMapPossible()) {
                Map converter = gpxService.ConvertTest2PrintMap(test.instance, flight_gpx_file_name, flight_map_file_name)
                if (converter.ok && converter.track) {
                    String flight_map_file_name2 = ("file:///" + HTMLFilter.GetStr(flight_map_file_name)).replaceAll('\\\\','/')
                    url += "&flightMapFileName=${flight_map_file_name2}"
                }
            }
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content.toByteArray()
            content.close()
            if (test.instance.IsShowMapPossible()) {
                gpxService.DeleteFile(flight_gpx_file_name)
                gpxService.DeleteFile(flight_map_file_name)
            }
        }
        catch (Throwable e) {
            test.message = getMsg('fc.flightresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printmeasureflightresultsTest(Map params, boolean a3, boolean landscape, String webRootDir, printparams)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print flight test results
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/flightresultsmeasureprintable/${test.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            String uuid = UUID.randomUUID().toString()
            String flight_gpx_file_name = "${GpxService.GPXDATA}-${uuid}"
            String flight_map_file_name = "${webRootDir}${Defs.ROOT_FOLDER_GPXUPLOAD}/PNG-${uuid}-FLIGHT.png"
            if (test.instance.IsShowMapPossible()) {
                Map converter = gpxService.ConvertTest2PrintMap(test.instance, flight_gpx_file_name, flight_map_file_name)
                if (converter.ok && converter.track) {
                    String flight_map_file_name2 = ("file:///" + HTMLFilter.GetStr(flight_map_file_name)).replaceAll('\\\\','/')
                    url += "&flightMapFileName=${flight_map_file_name2}"
                }
            }
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content.toByteArray()
            content.close()
            if (test.instance.IsShowMapPossible()) {
                gpxService.DeleteFile(flight_gpx_file_name)
                gpxService.DeleteFile(flight_map_file_name)
            }
        }
        catch (Throwable e) {
            test.message = getMsg('fc.flightresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printloggerdataTest(Map params, boolean a3, boolean landscape, String webRootDir, printparams)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print logger data
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/loggerdataprintable/${test.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            test.message = getMsg('fc.flightresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printcrewresultsTest(Map params, boolean a3, boolean landscape, String webRootDir, printparams)
    {
        printstart "printcrewresultsTest"
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            printerror "No test."
            return test
        }
        
        if (params.allResults) {
            test.instance.printSummaryResults = true
            test.instance.printPlanningResults = test.instance.IsPlanningTestRun()
            test.instance.printPlanningResultsScan = test.instance.printPlanningResults
            test.instance.printFlightResults = test.instance.IsFlightTestRun()
            test.instance.printFlightMap = test.instance.printFlightResults
            test.instance.printObservationResults = test.instance.IsObservationTestRun()
            test.instance.printObservationResultsScan = test.instance.printObservationResults
            test.instance.printLandingResults = test.instance.IsLandingTestRun()
            test.instance.printSpecialResults = test.instance.IsSpecialTestRun()
        } else {
            test.instance.printSummaryResults = params.printSummaryResults == "on"
            test.instance.printPlanningResults = params.printPlanningResults == "on"
            test.instance.printPlanningResultsScan = params.printPlanningResultsScan == "on"
            test.instance.printFlightResults = params.printFlightResults == "on"
            test.instance.printFlightMap = params.printFlightMap == "on"
            test.instance.printObservationResults = params.printObservationResults == "on"
            test.instance.printObservationResultsScan = params.printObservationResultsScan == "on"
            test.instance.printLandingResults = params.printLandingResults == "on"
            test.instance.printSpecialResults = params.printSpecialResults == "on"
            test.instance.printProvisionalResults = params.printProvisionalResults == "on"
        }
        
        // Print crewresults
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/crewresultsprintable/${test.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}&printSummaryResults=${test.instance.printSummaryResults}&printPlanningResults=${test.instance.printPlanningResults}&printPlanningResultsScan=${test.instance.printPlanningResultsScan}&printFlightResults=${test.instance.printFlightResults}&printFlightMap=${test.instance.printFlightMap}&printObservationResults=${test.instance.printObservationResults}&printObservationResultsScan=${test.instance.printObservationResultsScan}&printLandingResults=${test.instance.printLandingResults}&printSpecialResults=${test.instance.printSpecialResults}&printProvisionalResults=${test.instance.printProvisionalResults}"
            if (!(test.instance.GetDetailNum() || test.instance.printSummaryResults)) {
                url += "&disabletitle=yes"
            }
            String uuid = UUID.randomUUID().toString()
            String flight_gpx_file_name = "${GpxService.GPXDATA}-${uuid}"
            String flight_map_file_name = "${webRootDir}${Defs.ROOT_FOLDER_GPXUPLOAD}/PNG-${uuid}-FLIGHT.png"
            if (test.instance.printFlightMap && test.instance.IsShowMapPossible()) {
                Map converter = gpxService.ConvertTest2PrintMap(test.instance, flight_gpx_file_name, flight_map_file_name)
                if (converter.ok && converter.track) {
                    String flight_map_file_name2 = ("file:///" + HTMLFilter.GetStr(flight_map_file_name)).replaceAll('\\\\','/')
                    url += "&flightMapFileName=${flight_map_file_name2}"
                }
            }
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content.toByteArray()
            content.close()
            if (test.instance.IsShowMapPossible()) {
                gpxService.DeleteFile(flight_gpx_file_name)
                gpxService.DeleteFile(flight_map_file_name)
            }
            printdone ""
        }
        catch (Throwable e) {
            test.message = getMsg('fc.crewresults.printerror',["$e"])
            test.error = true
            printerror test.message 
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map ConvertTest2PDF(Test testInstance, String webRootDir, String pdfFileName, boolean a3, boolean landscape, printparams)
    {
        printstart "ConvertTest2PDF ${testInstance.name()} -> ${pdfFileName}"
        Map test = printcrewresultsTest([id:testInstance.id, allResults:true], a3, landscape, webRootDir, printparams)
        if (test.error) {
            printerror ""
            return [ok:false]
        }
        File pdf_file = new File(webRootDir + pdfFileName)
        DataOutputStream pdf_writer = pdf_file.newDataOutputStream()
        pdf_writer.write(test.content)
        pdf_writer.close()
        printdone ""
        return [ok:true]
    }
    
    //--------------------------------------------------------------------------
    Map printflightplanTest(Map params, boolean a3, boolean landscape, printparams)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print flightplan
        try {
            String print_action = "flightplanprintable"
            String print_params = ""
            if (test.instance.flighttestwind.flighttest.route.corridorWidth) {
                switch (test.instance.flighttestwind.flighttest.flightPlanDesign) {
                    case FlightPlanDesign.TPList:
                        print_action = "flightplananrprintable"
                        print_params = "&tplist=${true}"
                        break
                    case FlightPlanDesign.OnlyTimes:
                        print_action = "flightplananrprintable"
                        break
                    case FlightPlanDesign.Map:
                        print_action = "flightplanmapanrprintable"
                        print_params = "&crews=${true}"
                        break
                }
            }
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/${print_action}/${test.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}${print_params}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            test.message = getMsg('fc.print.error',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printplanningtaskTest(Map params, boolean a3, boolean landscape, boolean withResults, printparams)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print planningtask
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = ""
            if (withResults) {
                url = "${printparams.baseuri}/test/planningtaskprintable/${test.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}&results=yes"
            } else {
                url = "${printparams.baseuri}/test/planningtaskprintable/${test.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}&results=no"
            }
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            test.message = getMsg('fc.print.error',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printplanningtaskresultsTest(Map params, boolean a3, boolean landscape, printparams)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print planning test results
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/planningtaskresultsprintable/${test.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            test.message = getMsg('fc.planningresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printobservationresultsTest(Map params, boolean a3, boolean landscape, printparams)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print observation test results
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/observationresultsprintable/${test.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            test.message = getMsg('fc.observationresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printlandingresultsTest(Map params, boolean a3, boolean landscape, printparams, ResultType resultType)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }
		
		String print_action = ""
		switch (resultType) {
			case ResultType.Landing:
				print_action = "landingresultsprintable"
				break
			case ResultType.Landing1:
				print_action = "landingresultsprintable1"
				break
			case ResultType.Landing2:
				print_action = "landingresultsprintable2"
				break
			case ResultType.Landing3:
				print_action = "landingresultsprintable3"
				break
			case ResultType.Landing4:
				print_action = "landingresultsprintable4"
				break
		}
        
        // Print landing test results
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/${print_action}/${test.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            test.message = getMsg('fc.landingresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printspecialresultsTest(Map params, boolean a3, boolean landscape, printparams)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print special test results
        try {
            ITextRenderer renderer = new ITextRenderer();
            addFonts(renderer)
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/specialresultsprintable/${test.instance.id}?print=1&lang=${printparams.lang}&contestid=${printparams.contest.id}&a3=${a3}&landscape=${landscape}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content.toByteArray()
            content.close()
        }
        catch (Throwable e) {
            test.message = getMsg('fc.specialresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    public boolean WritePDF(response, byte[] contentByteArray, String prefix, String suffix, boolean showSize, boolean isA3, boolean isLandscape)
    {
        boolean ok = false
        String size_str = ""
        if (showSize) {
            size_str += "-"
            if (isA3) {
                size_str += "a3"
            } else {
                size_str += "a4"
            }
            if (isLandscape) {
                size_str += "l"
            }
        }
        String file_name = "fc-${prefix}${suffix}${size_str}.pdf"
        printstart "WritePDF '$file_name'"
        try {
            println "${contentByteArray.length} bytes."
            response.setContentType("application/pdf")
            response.setHeader("Content-disposition", "attachment; filename=$file_name")
            //response.setContentLength(contentByteArray.length)
            response.getOutputStream().write(contentByteArray)
            ok = true
            printdone ""
        } catch (Throwable e) {
            printerror "Throwable ${e}"
        } catch (Exception e) {
            printerror "Exception ${e}"
        }
        return ok
    }
    
    //--------------------------------------------------------------------------
    public boolean WritePDF(response, byte[] contentByteArray, String prefix, String suffix, boolean showSize, String printSize, boolean isLandscape)
    {
        boolean ok = false
        String size_str = ""
        if (showSize) {
            size_str += "-"
            size_str += printSize.toLowerCase()
            if (isLandscape) {
                size_str += "l"
            }
        }
        String file_name = "fc-${prefix}${suffix}${size_str}.pdf"
        printstart "WritePDF '$file_name'"
        try {
            println "${contentByteArray.length} bytes."
            response.setContentType("application/pdf")
            response.setHeader("Content-disposition", "attachment; filename=$file_name")
            //response.setContentLength(contentByteArray.length)
            response.getOutputStream().write(contentByteArray)
            ok = true
            printdone ""
        } catch (Throwable e) {
            printerror "Throwable ${e}"
        } catch (Exception e) {
            printerror "Exception ${e}"
        }
        return ok
    }
    
    //--------------------------------------------------------------------------
    public boolean WritePDF3(response, byte[] contentByteArray, String fileName)
    {
        boolean ok = false
        printstart "WritePDF '$fileName'"
        try {
            println "${contentByteArray.length} bytes."
            response.setContentType("application/pdf")
            response.setHeader("Content-disposition", "attachment; filename=$fileName")
            //response.setContentLength(contentByteArray.length)
            response.getOutputStream().write(contentByteArray)
            ok = true
            printdone ""
        } catch (Throwable e) {
            printerror "Throwable ${e}"
        } catch (Exception e) {
            printerror "Exception ${e}"
        }
        return ok
    }
    
    //--------------------------------------------------------------------------
    private void addFonts(ITextRenderer renderer)
    {
        String webroot_dir = servletContext.getRealPath("/")
        String font_path = "${webroot_dir}/${Defs.ROOT_FOLDER_FONTS}"
        FontResolver resolver = renderer.getFontResolver()
        resolver.addFont("${font_path}/${Defs.FONT_NOTOSANS_REGULAR}", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED)
        resolver.addFont("${font_path}/${Defs.FONT_NOTOSANS_BOLD}", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED)
        resolver.addFont("${font_path}/${Defs.FONT_NOTOSANS_ITALIC}", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED)
        resolver.addFont("${font_path}/${Defs.FONT_NOTOSANS_BOLDITALIC}", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED)
    }
    
    //--------------------------------------------------------------------------
    private String getMsg(String code, List args)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        if (args) {
            return messageSource.getMessage(code, args.toArray(), new Locale(session_obj.showLanguage))
        } else {
            return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
        }
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
    }
    
    //--------------------------------------------------------------------------
    private String getPrintMsg(String code)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        return messageSource.getMessage(code, null, new Locale(session_obj.printLanguage))
    }
    
    //--------------------------------------------------------------------------
    private void printstart(out)
    {
        logService.printstart out
    }

    //--------------------------------------------------------------------------
    private void printerror(out)
    {
        if (out) {
            logService.printend "Error: $out"
        } else {
            logService.printend "Error."
        }
    }

    //--------------------------------------------------------------------------
    private void printdone(out)
    {
        if (out) {
            logService.printend "Done: $out"
        } else {
            logService.printend "Done."
        }
    }

    //--------------------------------------------------------------------------
    private void print(out)
    {
        logService.print out
    }

    //--------------------------------------------------------------------------
    private void println(out)
    {
        logService.println out
    }
}
