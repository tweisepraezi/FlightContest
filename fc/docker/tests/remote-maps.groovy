// Run in the application image with FC_MAP_MODE=remote and WEB-INF on the classpath.
import com.sun.net.httpserver.HttpServer
import com.sun.net.httpserver.HttpHandler

assert System.getenv('FC_MAP_MODE') == 'remote'
BootStrap.global = new Global()
Global.FCMapServer = 'http://remote-map-provider.invalid/api'
Global.PostgreSQLPassword = 'local-credentials-must-not-select-local-maps'
assert !BootStrap.global.IsLocalPrintmaps()
assert !PrintMapTools.IsLocalPrintmapsRunning()
assert PrintMapTools.GetPrintServerAPI() == Global.FCMapServer

HttpServer server = HttpServer.create(new InetSocketAddress('127.0.0.1', 0), 0)
['ok': [200, '{"ready":true}'],
 'accepted': [202, '{"job":"example"}'],
 'failed': [503, 'unavailable'],
 'invalid': [200, 'invalid-json']].each { path, result ->
    server.createContext('/' + path, { exchange ->
        byte[] body = result[1].getBytes('UTF-8')
        exchange.responseHeaders.set('Content-Type', 'application/json; charset=utf-8')
        exchange.sendResponseHeaders(result[0], body.length)
        exchange.responseBody.withCloseable { it.write(body) }
    } as HttpHandler)
}
server.start()
try {
    String base = "http://127.0.0.1:${server.address.port}"
    Map ok = PrintMapTools.CallPrintServer(base + '/ok', [], 'GET', PrintMapTools.DataType.JSON, null)
    assert ok.responseCode == 200 && ok.json?.ready && !ok.error
    Map accepted = PrintMapTools.CallPrintServer(base + '/accepted', [], 'POST', PrintMapTools.DataType.JSON, '{}')
    assert accepted.responseCode == 202 && accepted.json.job == 'example'
    Map failed = PrintMapTools.CallPrintServer(base + '/failed', [], 'GET', PrintMapTools.DataType.JSON, null)
    assert failed.responseCode == 503 && failed.error == 'HTTP 503' && failed.json == null
    Map invalid = PrintMapTools.CallPrintServer(base + '/invalid', [], 'GET', PrintMapTools.DataType.JSON, null)
    assert invalid.responseCode == 200 && invalid.error && invalid.json == null
    Map badUrl = PrintMapTools.CallPrintServer('not-a-url', [], 'GET', PrintMapTools.DataType.JSON, null)
    assert badUrl.error && badUrl.responseCode == null
} finally {
    server.stop(0)
}
println 'Remote selection, successful requests, and server failure checks passed.'
