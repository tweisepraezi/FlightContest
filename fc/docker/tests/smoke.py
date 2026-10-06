"""HTTP checks for an isolated Flight Contest installation (Python standard library).

--demo creates competition data; use it only with a disposable test database.
"""
import argparse
import http.cookiejar
from pathlib import Path
import urllib.parse
import urllib.request
import uuid

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('base_url', help='Application URL including /fc')
parser.add_argument('--demo', choices=['1', '2', '21', '22', '23', '24', '99'])
parser.add_argument('--contest-id', help='Activate an existing contest after restore/recreation')
parser.add_argument('--output', type=Path, default=Path('/tmp/flightcontest-smoke'))
args = parser.parse_args()
args.output.mkdir(parents=True, exist_ok=True)
client = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()))

def request(path, form=None):
    data = urllib.parse.urlencode(form).encode() if form is not None else None
    with client.open(args.base_url.rstrip('/') + path, data, timeout=900) as response:
        return response.read()

assert request('/health/ready') == b'ready\n'
request('/contest/start?lang=en')
modules = request('/contest/runmodultests')
(args.output / 'modules.html').write_bytes(modules)
assert b'Modultests ohne Fehler.' in modules, 'Built-in module checks failed; inspect modules.html'
print('Readiness and built-in module checks passed.', flush=True)

if args.demo:
    demo = request('/contest/createtest', {'demoContest': args.demo})
    (args.output / 'demo.html').write_bytes(demo)
    assert b'NO errors.' in demo or b'Keine Fehler.' in demo, 'Demo scoring checks failed; inspect demo.html'
    print('Demo creation and expected scoring checks passed.', flush=True)
if args.contest_id:
    request('/contest/activate/' + args.contest_id)
if args.demo or args.contest_id:
    crews = request('/crew/list')
    assert b'Besatzung' in crews or b'Crew' in crews, 'Expected crew list is missing'
    report = request('/crew/print')
    (args.output / 'crews.pdf').write_bytes(report)
    assert report.startswith(b'%PDF-') and b'%%EOF' in report[-100:], 'PDF generation failed'
    print('Crew listing and PDF generation passed.', flush=True)

# Upload a repository logger file and request its normalized GPX download.
fixture = Path(__file__).resolve().parents[2] / 'web-app/testdata/Crew_3.gac'
boundary = 'flightcontest-' + uuid.uuid4().hex
body = (
    f'--{boundary}\r\nContent-Disposition: form-data; name="gpxdownload"\r\n\r\ntrue\r\n'
    f'--{boundary}\r\nContent-Disposition: form-data; name="loadloggerfile"; filename="Crew 3.gac"\r\n'
    'Content-Type: application/octet-stream\r\n\r\n'
).encode() + fixture.read_bytes() + f'\r\n--{boundary}--\r\n'.encode()
upload = urllib.request.Request(args.base_url.rstrip('/') + '/gpx/showmaploggerdata', data=body,
                               headers={'Content-Type': 'multipart/form-data; boundary=' + boundary})
with client.open(upload, timeout=120) as response:
    gpx = response.read()
(args.output / 'logger.gpx').write_bytes(gpx)
assert b'<gpx' in gpx and b'<trkpt' in gpx, 'Logger import/conversion failed'
print('Logger upload and GPX conversion passed.', flush=True)
