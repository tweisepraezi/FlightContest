"""Normalize legacy Windows-encoded Groovy sources in the build copy only."""
from pathlib import Path

for root in (Path('grails-app'), Path('src'), Path('scripts')):
    for path in root.rglob('*.groovy'):
        raw = path.read_bytes()
        try:
            raw.decode('utf-8')
        except UnicodeDecodeError:
            path.write_bytes(raw.decode('cp1252').encode('utf-8'))
