"""Restore a trusted application backup only into an empty application store."""
from pathlib import Path, PurePosixPath
import sys
import tarfile
import tempfile

roots = [
    'var/lib/flightcontest',
    *['usr/local/tomcat/webapps/fc/' + name
      for name in ('gpxupload', 'jobs', 'map', 'live', 'live2')],
]
config = Path('/var/lib/flightcontest/save/.fc/config.groovy')
for root in roots:
    for path in Path('/' + root).rglob('*'):
        if path != config and (path.is_file() or path.is_symlink()):
            sys.exit('Refusing to overwrite existing application data. Use fresh volumes.')

# Validate the complete archive before modifying any files.
with tempfile.TemporaryFile() as stream:
    import shutil
    shutil.copyfileobj(sys.stdin.buffer, stream)
    stream.seek(0)
    with tarfile.open(fileobj=stream, mode='r:gz') as archive:
        for member in archive.getmembers():
            name = member.name.rstrip('/')
            path = PurePosixPath(name)
            if path.is_absolute() or '..' in path.parts:
                sys.exit('Invalid path in backup')
            if not any(name == root or name.startswith(root + '/') for root in roots):
                sys.exit('Unexpected path in backup')
            if not (member.isfile() or member.isdir()) or name == str(config).lstrip('/'):
                sys.exit('Unsupported backup entry')
        archive.extractall('/', filter='data')
print('Restored application storage.')
