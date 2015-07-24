class LiveJob
{
    def gpxService
    
    def group = "LiveGroup"
    def description = "Live Trigger"
    def concurrent = false
    
    def execute(context) {
        String contest_id = context.mergedJobDataMap.get('ContestID')
        long live_contest_id = 0
        if (contest_id) {
            live_contest_id = contest_id.toLong()
        }
        gpxService.PublishLiveResults(live_contest_id,false)
    }
}
