class LiveJob
{
    def gpxService
    
    def group = "LiveGroup"
    def description = "Live Trigger"
    def concurrent = false
    
    def execute() {
        gpxService.PublishLiveResults(0)
    }
}
