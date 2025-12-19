import java.util.List;
import java.util.Map;

import org.springframework.web.context.request.RequestContextHolder

class EmailService
{
    static final int FTP_UPLOAD_MAXSHOWPOINTS = 34
    
    def domainService
    def gpxService
    def kmlService
    def logService
    def printService
    def grailsApplication
    def servletContext
    def messageSource
    
    //--------------------------------------------------------------------------
    Map emailRoute(Route routeInstance, String printLanguage, def grailsAttributes, def request)
    {
        String email_to = routeInstance.EMailAddress()
        printstart "Send mail of '${routeInstance.name()}' to '${email_to}'"
        
        String uuid = UUID.randomUUID().toString()
        String webroot_dir = servletContext.getRealPath("/")
        String upload_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-EMAIL-${uuid}.gpx"
        String upload_gpx4htm_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-EMAIL-HTM-${uuid}.gpx"
        String upload_kmz_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-EMAIL-${uuid}.kmz"
        String upload_pdf_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-EMAIL-${uuid}.pdf"
        Map gpx_converter = gpxService.ConvertRoute2GPX(routeInstance, webroot_dir + upload_gpx_file_name, [isPrint:true, showPoints:true, wrEnrouteSign:true, gpxExport:true, wrPhotoImage:true])
        Map gpx_view_converter = gpxService.ConvertRoute2GPX(routeInstance, webroot_dir + upload_gpx4htm_file_name, [isPrint:true, showPoints:true, wrEnrouteSign:true, gpxExport:false])
        Map kmz_converter = kmlService.ConvertRoute2KMZ(routeInstance, webroot_dir, upload_kmz_file_name, true, true) // true - Print, true - wrEnrouteSign
        Map pdf_converter = printService.ConvertRoute2PDF(routeInstance, webroot_dir + upload_pdf_file_name, false, false, GetPrintParams(routeInstance.contest, printLanguage, grailsAttributes, request)) 
        if (gpx_converter.ok && gpx_view_converter.ok && kmz_converter.ok && pdf_converter.ok) {
            String route_name = routeInstance.printName().replaceAll(' ','-')
            String gpx_file_name = "${route_name}.gpx"
            String gpx4htm_file_name = "${route_name}_htm.gpx"
            String route_title = routeInstance.GetEMailTitle()
            
            Map email = GetRouteEMailBody(routeInstance.contest.contestUUID, route_name)
            String ret_message = ""
            String job_file_name = "${Defs.ROOT_FOLDER_JOBS}/JOB-${uuid}.job"
            BufferedWriter job_writer = null
            try {
                String ftp_uploads = ""
                ftp_uploads += "file:${webroot_dir+upload_gpx_file_name}"
                ftp_uploads += Defs.BACKGROUNDUPLOAD_SRCDEST_SEPARATOR
                ftp_uploads += gpx_file_name
                ftp_uploads += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                ftp_uploads += "file:${webroot_dir+upload_gpx4htm_file_name}"
                ftp_uploads += Defs.BACKGROUNDUPLOAD_SRCDEST_SEPARATOR
                ftp_uploads += gpx4htm_file_name
                ftp_uploads += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                ftp_uploads += "file:${webroot_dir+upload_kmz_file_name}"
                ftp_uploads += Defs.BACKGROUNDUPLOAD_SRCDEST_SEPARATOR
                ftp_uploads += "${route_name}.kmz"
                ftp_uploads += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                ftp_uploads += "file:${webroot_dir+upload_pdf_file_name}"
                ftp_uploads += Defs.BACKGROUNDUPLOAD_SRCDEST_SEPARATOR
                ftp_uploads += "${route_name}.pdf"
                ftp_uploads += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                if (gpx_converter.gpxShowPoints.size() <= FTP_UPLOAD_MAXSHOWPOINTS) {
                    ftp_uploads += "http://localhost:8080/fc/gpx/startftpgpxviewer?fileName=${HTMLFilter.GetStr(gpx4htm_file_name)}&originalFilename=${HTMLFilter.GetStr(route_title)}&printLanguage=${printLanguage}&showProfiles=no&gpxShowPoints=${HTMLFilter.GetStr2(gpx_converter.gpxShowPoints)}"
                } else {
                    ftp_uploads += "http://localhost:8080/fc/gpx/startftpgpxviewer?fileName=${HTMLFilter.GetStr(gpx4htm_file_name)}&originalFilename=${HTMLFilter.GetStr(route_title)}&printLanguage=${printLanguage}&showProfiles=no&gpxShowPoints=${HTMLFilter.GetStr2([])}"
                }
                ftp_uploads += Defs.BACKGROUNDUPLOAD_SRCDEST_SEPARATOR
                ftp_uploads += "${route_name}.htm"
                
                String remove_files = ""
                remove_files += webroot_dir + upload_gpx_file_name
                remove_files += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                remove_files += webroot_dir + upload_gpx4htm_file_name
                remove_files += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                remove_files += webroot_dir + upload_kmz_file_name
                remove_files += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                remove_files += webroot_dir + upload_pdf_file_name

				// job status
				UploadJobRoute uploadjob_route = UploadJobRoute.findByRoute(routeInstance)
				if (uploadjob_route) {
					uploadjob_route.uploadJobStatus = UploadJobStatus.Waiting // testInstance.flightTestLink = Defs.EMAIL_SENDING
				} else {
					uploadjob_route = new UploadJobRoute(route:routeInstance, uploadJobStatus:UploadJobStatus.Waiting)
				}
				uploadjob_route.save(flush:true)
				
                // create email job
                File job_file = new File(webroot_dir + job_file_name)
                job_writer = job_file.newWriter("UTF-8")
                gpxService.WriteLine(job_writer,email_to) // 1
                gpxService.WriteLine(job_writer,routeInstance.GetEMailTitle()) // 2
                gpxService.WriteLine(job_writer,email.body) // 3
                gpxService.WriteLine(job_writer,routeInstance.contest.contestUUID) // 4
                gpxService.WriteLine(job_writer,ftp_uploads) // 5
                gpxService.WriteLine(job_writer,remove_files) // 6
                gpxService.WriteLine(job_writer,"UploadJobRoute${Defs.BACKGROUNDUPLOAD_IDLINK_SEPARATOR}${uploadjob_route.id}${Defs.BACKGROUNDUPLOAD_IDLINK_SEPARATOR}${email.link}") // 7
                job_writer.close()
                
                ret_message = getMsg('fc.net.mail.prepared',[email_to])
                println "Job '${job_file_name}' created." 
            } catch (Exception e) {
                println "Error: '${job_file_name}' could not be created ('${e.getMessage()}')"
            }
            
            printdone ""
            return [ok:true, message:ret_message]
        } else {
            String error_message = getMsg('fc.net.mail.convert.error',[routeInstance.name()])
            gpxService.DeleteFile(upload_gpx_file_name)
            gpxService.DeleteFile(upload_kmz_file_name)
            gpxService.DeleteFile(upload_pdf_file_name)
            printerror error_message
            return [ok:false, message:error_message]
        }
    }
    
    //--------------------------------------------------------------------------
    Map emailRouteMap(Route routeInstance, String routeMapFileName)
    {
        String email_to = routeInstance.EMailAddress()
        printstart "Send mail of '${routeInstance.name()}' to '${email_to}'"
        
        String uuid = UUID.randomUUID().toString()
        String webroot_dir = servletContext.getRealPath("/")
        
        String route_name = routeInstance.printName().replaceAll(' ','-')
        String pdf_file_name = "${route_name}-Map-${routeInstance.contestMapEdition}.pdf"
        String route_title = routeInstance.GetEMailTitle()
        
        Map email = GetRouteMapEMailBody(routeInstance.contest.contestUUID, pdf_file_name)
        String ret_message = ""
        String job_file_name = "${Defs.ROOT_FOLDER_JOBS}/JOB-${uuid}.job"
        BufferedWriter job_writer = null
        try {
            String ftp_uploads = ""
            ftp_uploads += "file:${routeMapFileName}"
            ftp_uploads += Defs.BACKGROUNDUPLOAD_SRCDEST_SEPARATOR
            ftp_uploads += pdf_file_name
            
            String remove_files = ""
            remove_files += routeMapFileName

            // job status
            UploadJobRouteMap uploadjob_routemap = UploadJobRouteMap.findByRouteAndUploadJobMapEdition(routeInstance, routeInstance.contestMapEdition)
            if (uploadjob_routemap) {
                uploadjob_routemap.uploadJobStatus = UploadJobStatus.Waiting
            } else {
                uploadjob_routemap = new UploadJobRouteMap(route:routeInstance, uploadJobMapEdition:routeInstance.contestMapEdition, uploadJobStatus:UploadJobStatus.Waiting)
            }
            uploadjob_routemap.save(flush:true)
            
            // create email job
            File job_file = new File(webroot_dir + job_file_name)
            job_writer = job_file.newWriter("UTF-8")
            gpxService.WriteLine(job_writer,email_to) // 1
            gpxService.WriteLine(job_writer,routeInstance.GetEMailTitle()) // 2
            gpxService.WriteLine(job_writer,email.body) // 3
            gpxService.WriteLine(job_writer,routeInstance.contest.contestUUID) // 4
            gpxService.WriteLine(job_writer,ftp_uploads) // 5
            gpxService.WriteLine(job_writer,remove_files) // 6
            gpxService.WriteLine(job_writer,"UploadJobRouteMap${Defs.BACKGROUNDUPLOAD_IDLINK_SEPARATOR}${uploadjob_routemap.id}${Defs.BACKGROUNDUPLOAD_IDLINK_SEPARATOR}${email.link}") // 7
            job_writer.close()
            
            ret_message = getMsg('fc.net.mail.prepared',[email_to])
            println "Job '${job_file_name}' created." 
        } catch (Exception e) {
            println "Error: '${job_file_name}' could not be created ('${e.getMessage()}')"
        }
        
        printdone ""
        return [ok:true, message:ret_message]
    }
    
    //--------------------------------------------------------------------------
    Map emailcrewresultsTask(Map params, String printLanguage, def grailsAttributes, def request, boolean allResults)
    {
        printstart "emailcrewresultsTask"
        Map task = domainService.GetTaskMap(params)
        if (!task.instance) {
            printerror "No task."
            return task
        }

        String webroot_dir = servletContext.getRealPath("/")
        String lock_file_name = webroot_dir + Defs.ROOT_FOLDER_JOBS_LOCK
        File lock_file = new File(lock_file_name)
        BufferedWriter lock_writer = lock_file.newWriter()
        lock_writer.close()
        
        // Send email of all navigationresults
        int email_num = 0
        int error_num = 0
        List ok_test_instances = []
        for ( Test test_instance in Test.findAllByTask(task.instance,[sort:"viewpos"])) {
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
				UploadJobStatus upload_job_status = test_instance.GetFlightTestUploadJobStatus()
                if (allResults || upload_job_status == UploadJobStatus.None || upload_job_status == UploadJobStatus.Error) {
					if (test_instance.IsSendEMailPossible()) {
						email_num++
						Map ret = SendEmailTest(test_instance, printLanguage, grailsAttributes, request)
						if (ret.ok) {
							ok_test_instances += test_instance
						} else {
							error_num++
						}
					}
                }
            }
        }
        
        gpxService.DeleteFile(lock_file_name)
        
        if (error_num) {
            task.message = getMsg('fc.net.mail.prepared.num.error',[email_num,error_num])
            task.error = true
        } else {
            task.message = getMsg('fc.net.mail.prepared.num',[email_num])
        }
        
        printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
    Map SendEmailTest(Test testInstance, String printLanguage, def grailsAttributes, def request)
    {
        String email_to = testInstance.EMailAddress()
        boolean is_flight_test = testInstance.IsFlightTestRun()
        printstart "Send mail of '${testInstance.crew.name}' to '${email_to}'"
        
        String uuid = UUID.randomUUID().toString()
        String webroot_dir = servletContext.getRealPath("/")
        String upload_pdf_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/FLIGHT-EMAIL-${uuid}.pdf"
        String upload_gpx_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/FLIGHT-EMAIL-${uuid}.gpx"
        String upload_kmz_file_name = "${Defs.ROOT_FOLDER_GPXUPLOAD}/FLIGHT-EMAIL-${uuid}.kmz"
        Map pdf_converter = printService.ConvertTest2PDF(testInstance, webroot_dir, upload_pdf_file_name, false, false, GetPrintParams(testInstance.task.contest, printLanguage, grailsAttributes, request))
        Map gpx_converter = [ok:true, track:true]
        Map kmz_converter = [ok:true, track:true]
        if (is_flight_test) {
            gpx_converter = gpxService.ConvertTest2GPX(testInstance, webroot_dir + upload_gpx_file_name, [isPrint:true, showPoints:true, wrEnrouteSign:true, gpxExport:false])
            kmz_converter = kmlService.ConvertTest2KMZ(testInstance, webroot_dir, upload_kmz_file_name, true, true) // true - Print, true - wrEnrouteSign
        }
        if (gpx_converter.ok && gpx_converter.track && kmz_converter.ok && kmz_converter.track) {
            String test_name = testInstance.GetFileName(ResultType.Crew)
            String gpx_file_name = "${test_name}.gpx"
            String flight_title = testInstance.GetEMailTitle(ResultType.Flight)
            String email_title = testInstance.GetEMailTitle(ResultType.Crew)
            
            Map email = GetTestEMailBody(testInstance.crew.uuid, test_name, testInstance.task.contest.printOrganizer, is_flight_test)
            String ret_message = ""
            String job_file_name = "${Defs.ROOT_FOLDER_JOBS}/JOB-${uuid}.job"
            try {
                String ftp_uploads = ""
                ftp_uploads += "file:${webroot_dir+upload_pdf_file_name}"
                ftp_uploads += Defs.BACKGROUNDUPLOAD_SRCDEST_SEPARATOR
                ftp_uploads += "${test_name}.pdf"
                if (is_flight_test) {
                    ftp_uploads += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                    ftp_uploads += "file:${webroot_dir+upload_gpx_file_name}"
                    ftp_uploads += Defs.BACKGROUNDUPLOAD_SRCDEST_SEPARATOR
                    ftp_uploads += gpx_file_name
                    ftp_uploads += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                    ftp_uploads += "file:${webroot_dir+upload_kmz_file_name}"
                    ftp_uploads += Defs.BACKGROUNDUPLOAD_SRCDEST_SEPARATOR
                    ftp_uploads += "${test_name}.kmz"
                    ftp_uploads += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                    if (gpx_converter.gpxShowPoints.size() <= FTP_UPLOAD_MAXSHOWPOINTS) {
                        ftp_uploads += "http://localhost:8080/fc/gpx/startftpgpxviewer?fileName=${gpx_file_name}&originalFilename=${HTMLFilter.GetStr(flight_title)}&printLanguage=${printLanguage}&showProfiles=yes&gpxShowPoints=${HTMLFilter.GetStr2(gpx_converter.gpxShowPoints)}"
                    } else {
                        ftp_uploads += "http://localhost:8080/fc/gpx/startftpgpxviewer?fileName=${gpx_file_name}&originalFilename=${HTMLFilter.GetStr(flight_title)}&printLanguage=${printLanguage}&showProfiles=yes&gpxShowPoints=${HTMLFilter.GetStr2([])}"
                    }
                    ftp_uploads += Defs.BACKGROUNDUPLOAD_SRCDEST_SEPARATOR
                    ftp_uploads += "${test_name}.htm"
                }
                
                String remove_files = ""
                remove_files += webroot_dir + upload_pdf_file_name
                if (is_flight_test) {
                    remove_files += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                    remove_files += webroot_dir + upload_gpx_file_name
                    remove_files += Defs.BACKGROUNDUPLOAD_OBJECT_SEPARATOR
                    remove_files += webroot_dir + upload_kmz_file_name
                }

				// job status
				UploadJobTest uploadjob_test = UploadJobTest.findByTest(testInstance)
				if (uploadjob_test) {
					uploadjob_test.uploadJobStatus = UploadJobStatus.Waiting // testInstance.flightTestLink = Defs.EMAIL_SENDING
				} else {
					uploadjob_test = new UploadJobTest(test:testInstance, uploadJobStatus:UploadJobStatus.Waiting)
				}
				uploadjob_test.save(flush:true)
				
                // create email job
                File job_file = new File(webroot_dir + job_file_name)
                BufferedWriter job_writer = job_file.newWriter("UTF-8")
                gpxService.WriteLine(job_writer,email_to) // 1
                gpxService.WriteLine(job_writer,email_title) // 2
                gpxService.WriteLine(job_writer,email.body) // 3
                gpxService.WriteLine(job_writer,testInstance.crew.uuid) // 4
                gpxService.WriteLine(job_writer,ftp_uploads) // 5
                gpxService.WriteLine(job_writer,remove_files) // 6
                gpxService.WriteLine(job_writer,"UploadJobTest${Defs.BACKGROUNDUPLOAD_IDLINK_SEPARATOR}${uploadjob_test.id}${Defs.BACKGROUNDUPLOAD_IDLINK_SEPARATOR}${email.link}") // 7
                job_writer.close()
                
                ret_message = getMsg('fc.net.mail.prepared',[email_to])
                println "Job '${job_file_name}' created."
            } catch (Exception e) {
                println "Error: '${job_file_name}' could not be created ('${e.getMessage()}')"
            }
                            
            printdone ""
            return [ok:true, message:ret_message]
        } else {
            String error_message = getMsg('fc.net.mail.convert.error',[testInstance.crew.name])
            gpxService.DeleteFile(upload_pdf_file_name)
            if (is_flight_test) {
                gpxService.DeleteFile(upload_gpx_file_name)
                gpxService.DeleteFile(upload_kmz_file_name)
            }
            printerror error_message
            return [ok:false, message:error_message]
        }
    }
    
    //--------------------------------------------------------------------------
    private Map GetRouteEMailBody(String contestUUID, String routeName)
    {
        Map ret = [:]
        String body = ""
        
        String contest_dir = "${grailsApplication.config.flightcontest.ftp.contesturl}/${contestUUID}"
        
        String kmz_url = "${contest_dir}/${routeName}.kmz"
        body += """<p>${getPrintMsg('fc.net.mail.route.body.kmz')}: <a href="${kmz_url}">${kmz_url}</a></p>"""
        
        String gpx_url = "${contest_dir}/${routeName}.gpx"
        body += """<p>${getPrintMsg('fc.net.mail.route.body.gpx')}: <a href="${gpx_url}">${gpx_url}</a></p>"""
        
        String view_url = "${contest_dir}/${routeName}.htm"
        body += """<p>${getPrintMsg('fc.net.mail.route.body.map')}: <a href="${view_url}">${view_url}</a></p>"""
        
        String pdf_url = "${contest_dir}/${routeName}.pdf"
        body += """<p>${getPrintMsg('fc.net.mail.route.body.pdf')}: <a href="${pdf_url}">${pdf_url}</a></p>"""
        
        ret += [body:body, link:view_url]
        
        return ret
    }

    //--------------------------------------------------------------------------
    private Map GetRouteMapEMailBody(String contestUUID, String mapFileName)
    {
        Map ret = [:]
        String body = ""
        
        String contest_dir = "${grailsApplication.config.flightcontest.ftp.contesturl}/${contestUUID}"
        
        String map_url = "${contest_dir}/${mapFileName}"
        body += """<p>${getPrintMsg('fc.net.mail.route.body.contestmap')}: <a href="${map_url}">${map_url}</a></p>"""
        
        ret += [body:body, link:map_url]
        
        return ret
    }

    //--------------------------------------------------------------------------
    String GetRouteLinks(Route routeInstance)
    {
        String links = ""
        if (routeInstance.GetUploadJobStatus() == UploadJobStatus.Done) {
            Map route_links = NetTools.EMailLinks(routeInstance.GetUploadLink())
            links += "${getPrintMsg('fc.net.mail.route.body.map')}: "
            links += route_links.map
            links += "\n"
            links += "${getPrintMsg('fc.net.mail.route.body.kmz')}: "
            links += route_links.kmz
            links += "\n"
            links += "${getPrintMsg('fc.net.mail.route.body.gpx')}: "
            links += route_links.gpx
            links += "\n"
            links += "${getPrintMsg('fc.net.mail.route.body.pdf')}: "
            links += route_links.pdf
        }
        for (Map map_link in routeInstance.GetMapUploadLinks()) {
            links += "\n"
            if (map_link.optiontitle) {
                if (map_link.noroute) {
                    links += "${map_link.optiontitle} (${getPrintMsg('fc.net.mail.route.body.noroute')}): " 
                } else if (map_link.allroutedetails) {
                    links += "${map_link.optiontitle} (${getPrintMsg('fc.net.mail.route.body.allroutedetails')}): " 
                } else {
                    links += "${map_link.optiontitle}: "
                }
            } else {
                if (map_link.noroute) {
                    links += "${map_link.optionnumber}. ${getPrintMsg('fc.net.mail.route.body.contestmap.noroute')}: "
                } else if (map_link.allroutedetails) {
                    links += "${map_link.optionnumber}. ${getPrintMsg('fc.net.mail.route.body.contestmap.allroutedetails')}: "
                } else {
                    links += "${map_link.optionnumber}. ${getPrintMsg('fc.net.mail.route.body.contestmap')}: "
                }
            }
            links += map_link.link
        }
        return links
    }
    
    //--------------------------------------------------------------------------
    private Map GetTestEMailBody(String crewUUID, String testName, String printOrganizer, boolean isFlightTest)
    {
        Map ret = [:]
        String body = "<p>${getPrintMsg('fc.net.mail.test.body.participant')}</p>"
        
        String crew_dir = "${grailsApplication.config.flightcontest.ftp.contesturl}/${crewUUID}"
        
        String results_url = "${crew_dir}/${testName}.pdf"
        body += """<p>${getPrintMsg('fc.net.mail.test.body.pdf')}: <a href="${results_url}">${results_url}</a></p>"""
        
        if (isFlightTest) {
            String kmz_url = "${crew_dir}/${testName}.kmz"
            body += """<p>${getPrintMsg('fc.net.mail.test.body.kmz')}: <a href="${kmz_url}">${kmz_url}</a></p>"""
            
            //String gpx_url = "${crew_dir}/${testName}.gpx"
            //body += """<p>${getPrintMsg('fc.net.mail.test.body.gpx')}: <a href="${gpx_url}">${gpx_url}</a></p>"""
            
            String view_url = "${crew_dir}/${testName}.htm"
            body += """<p>${getPrintMsg('fc.net.mail.test.body.map')}: <a href="${view_url}">${view_url}</a></p>"""
        }
        
        body += """<p>${printOrganizer}</p>"""
        
        ret += [body:body, link:results_url]
        
        return ret
    }

    //--------------------------------------------------------------------------
    void CreateUploadJobRouteMap(Route routeInstance, boolean noRoute, boolean allRouteDetails, int optionNumber, String optionTitle)
    {
        UploadJobRouteMap uploadjob_routemap = UploadJobRouteMap.findByRouteAndUploadJobMapEdition(routeInstance, routeInstance.contestMapEdition)
        if (!uploadjob_routemap) {
            uploadjob_routemap = new UploadJobRouteMap(
				route:routeInstance, uploadJobMapEdition:routeInstance.contestMapEdition, uploadJobStatus:UploadJobStatus.None, uploadJobNoRoute:noRoute, uploadJobAllRouteDetails: allRouteDetails,
				uploadJobOptionNumber:optionNumber, uploadJobOptionTitle:optionTitle
			)
            uploadjob_routemap.save(flush:true)
        }
    }
    
    //--------------------------------------------------------------------------
    private Map GetPrintParams(Contest contestInstance, String printLanguage, def grailsAttributes, def request)
    {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:contestInstance,
                lang:printLanguage
               ]
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
    void printstart(out)
    {
        logService.printstart out
    }

    //--------------------------------------------------------------------------
    void printerror(out)
    {
        if (out) {
            logService.printend "Error: $out"
        } else {
            logService.printend "Error."
        }
    }

    //--------------------------------------------------------------------------
    void printdone(out)
    {
        if (out) {
            logService.printend "Done: $out"
        } else {
            logService.printend "Done."
        }
    }

    //--------------------------------------------------------------------------
    void print(out)
    {
        logService.print out
    }

    //--------------------------------------------------------------------------
    void println(out)
    {
        logService.println out
    }
}
