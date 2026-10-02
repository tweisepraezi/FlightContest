// Run inside the built image with WEB-INF/classes and WEB-INF/lib/* on the classpath.
String first = FlightContestRuntime.clientId()
assert first == FlightContestRuntime.clientId()
assert new File(FlightContestRuntime.saveDirectory(), '.fc/client-id').text.trim() == first
assert FlightContestRuntime.internalBaseUrl('http://invalid/') == 'http://127.0.0.1:8080/fc'
assert new File(Defs.FCSAVE_FOLDER).isAbsolute()

println 'Runtime identity, persistent save directory, and internal URL checks passed.'
