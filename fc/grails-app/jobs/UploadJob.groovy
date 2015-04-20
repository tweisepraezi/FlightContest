class UploadJob
{
    def gpxService
    
    static triggers = {
        simple name: 'uploadTrigger', startDelay: 10000, repeatInterval: 10000 // 10s , 10s
    }
    
    def group = "UploadGroup"
    def description = "Upload Trigger"
    def concurrent = false
    
    def execute() {
        gpxService.BackgroundUpload()
    }
}
