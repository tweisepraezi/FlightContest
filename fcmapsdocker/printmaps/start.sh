#!/bin/bash
echo "Hostname=$(hostname)"
echo "PostgreSQL-Server=$PGHOST"
id
#ip address show
apt list --installed
echo ""

# Show versions
lsb_release -a
psql --version
apt search libmapnik
echo ""

# Configure postgresql
echo "Configure postgresql..."
#   https://www.postgresql.org/docs/17/libpq-envars.html
#psql --dbname=postgres -c "SELECT pg_database.datname, pg_size_pretty(pg_database_size(pg_database.datname)) AS size FROM pg_database ORDER BY pg_database.datname;"
#   https://www.postgresql.org/docs/17/app-psql.html
echo "Done: postgresql configured."
echo ""

# Set dbid 
python3 /fcmaps/replace_dbid.py /fcmaps/otm/mapnik/region-id.xml /fcmaps/otm/mapnik/region-all.xml $dbid

# Start printmaps
echo "Start printmaps..."
cd /fcmaps
/fcmaps/printmaps_webservice   /fcmaps/printmaps_webservice.yaml   1>/fcmaps/logs/printmaps_webservice.out   2>&1 &
/fcmaps/printmaps_buildservice /fcmaps/printmaps_buildservice.yaml 1>/fcmaps/logs/printmaps_buildservice.out 2>&1 &
echo "Done: printmaps started."
echo ""

# Never ending process
echo "Log printmaps_buildservice..."
tail -F /fcmaps/logs/printmaps_buildservice.log
