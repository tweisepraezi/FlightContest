class OsmPrintMapJob
{
    def osmPrintMapService
    
    def group = Defs.OSMPRINTMAP_GROUP
    def description = "OsmPrintMap Trigger"
    def concurrent = false
    
    def execute(context) {
        String action_name = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_ACTION)
        String job_filename = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_JOBFILENAME)
        String job_id = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_JOBID)
        String jobid_filename = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_JOBIDFILENAME)
        String png_filename = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_PNGFILENAME)
        boolean print_landscape = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_PRINTLANDSCAPE)
        boolean print_colorchanges = context.mergedJobDataMap.get(Defs.OSMPRINTMAP_PRINTCOLORCHANGES)
        osmPrintMapService.BackgroundJob(action_name, job_filename, job_id, jobid_filename, png_filename, print_landscape, print_colorchanges)
    }
}
