import org.springframework.web.context.request.RequestContextHolder

/** OS boundaries shared by the legacy application and its container deployment. */
class FlightContestRuntime {
    static String setting(String name, String fallback = '') {
        String value = System.getenv(name)
        return value?.trim() ?: fallback
    }

    static boolean isWindows() {
        return System.getProperty('os.name').toLowerCase(Locale.ROOT).startsWith('windows')
    }

    static String saveDirectory() {
        return setting('FC_SAVE_DIR', isWindows() ? 'C:/FCSave' : new File(System.getProperty('user.home'), '.flightcontest').path)
    }

    static synchronized String clientId() {
        String configured = setting('FC_CLIENT_ID')
        if (configured) {
            return checkedClientId(configured)
        }
        if (isWindows()) {
            def process = ['powershell', '-command', '(get-itemproperty -path HKLM:\\SOFTWARE\\Microsoft\\SQMClient -Name MachineID).MachineID'].execute()
            String machineId = process.text.trim()
            return machineId ? checkedClientId(machineId) : ''
        }
        File identity = new File(saveDirectory(), '.fc/client-id')
        if (identity.exists()) {
            return checkedClientId(identity.getText('UTF-8'))
        }
        if (!identity.parentFile.isDirectory() && !identity.parentFile.mkdirs()) {
            throw new IOException('Cannot create installation identity directory')
        }
        String generated = UUID.randomUUID().toString()
        File temporary = new File(identity.parentFile, 'client-id.tmp')
        temporary.setText(generated + '\n', 'UTF-8')
        if (!temporary.renameTo(identity)) {
            throw new IOException('Cannot persist installation identity')
        }
        return generated
    }

    private static String checkedClientId(String value) {
        String identity = value.trim().replace('{', '').replace('}', '')
        if (!(identity ==~ /[A-Za-z0-9_-]{1,128}/)) {
            throw new IllegalArgumentException('Invalid Flight Contest client ID')
        }
        return identity
    }

    static String internalBaseUrl(String fallback) {
        return setting('FC_INTERNAL_BASE_URL', fallback).replaceAll('/+$', '')
    }

    static String publicBaseUrl() {
        String configured = setting('FC_PUBLIC_BASE_URL')
        if (configured) {
            return configured.replaceAll('/+$', '')
        }
        def attributes = RequestContextHolder.getRequestAttributes()
        if (attributes) {
            def request = attributes.request
            return "${request.scheme}://${request.serverName}:${request.serverPort}${request.contextPath}"
        }
        return 'http://localhost:8080/fc'
    }

}
