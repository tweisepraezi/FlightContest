class HealthController {
    def ready = {
        response.setHeader('Cache-Control', 'no-store')
        try {
            if (BootStrap.ready && BootStrap.global && !BootStrap.global.IsDBOlder() && !BootStrap.global.IsDBNewer() && Global.count() > 0) {
                render(status: 200, contentType: 'text/plain', text: 'ready\n')
                return
            }
        } catch (Exception ignored) {
            // Readiness includes the database; do not expose exception details.
        }
        render(status: 503, contentType: 'text/plain', text: 'not ready\n')
    }
}
