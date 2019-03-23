import org.quartz.JobKey

class OsmPrintMapJob
{
    def gpxService
    def osmPrintMapService
    def quartzScheduler
    
    def group = Defs.OSMPRINTMAP_GROUP
    def description = "OsmPrintMap Trigger"
    def concurrent = false
    
    def execute(context) {
        String action_name = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_ACTION)
        String job_filename = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_JOBFILENAME)
        String job_id = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_JOBID)
        String jobid_filename = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_JOBIDFILENAME)
        String fileid_filename = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_FILEIDFILENAME)
        String png_filename = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_PNGFILENAME)
        boolean print_landscape = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_PRINTLANDSCAPE)
        boolean print_colorchanges = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_PRINTCOLORCHANGES)
        try {
            osmPrintMapService.BackgroundJob(action_name, job_filename, job_id, jobid_filename, fileid_filename, png_filename, print_landscape, print_colorchanges)
        } catch (Exception e) {
             quartzScheduler.unscheduleJobs(quartzScheduler.getTriggersOfJob(new JobKey("OsmPrintMapJob",Defs.OSMPRINTMAP_GROUP))*.key)
             if (job_filename) {
                 gpxService.DeleteFile(job_filename)
             }
        }    
    }
}
