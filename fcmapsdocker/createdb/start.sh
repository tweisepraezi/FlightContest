#!/bin/bash
echo "Hostname=$(hostname)"
echo "PostgreSQL-Server=$PGHOST"
id
#ip address show
#apt list --installed
echo ""

# Show versions
osm2pgsql --version
pyhgtmap --version
echo ""

# Configure postgresql
echo "Configure postgresql..."
#   https://www.postgresql.org/docs/17/libpq-envars.html
#psql --dbname=postgres -c "SELECT pg_database.datname, pg_size_pretty(pg_database_size(pg_database.datname)) AS size FROM pg_database ORDER BY pg_database.datname;"
#   https://www.postgresql.org/docs/17/app-psql.html
echo "Done: postgresql configured."
echo ""

if [ $dbid ]; then
    echo "Remove database ready_$dbid..."
    dropdb ready_$dbid

    echo "Create database running_$dbid..."
    createdb running_$dbid
    
fi

# Generate osm databases
if [[ ! -z $PBFLINKS ]]; then
    echo "Generate osm databases..."
    cd /scripts-osm
    bash create_osm_db.sh
    echo "Done: osm databases generated."
    echo ""
fi

# Generate contour database
if [ $SRTMUSER ]; then
    echo "Generate contour database..."
    cd /scripts-contour
    bash create_contour_db.sh
    echo "Done: contour database generated."
    echo ""
fi

if [ $dbid ]; then
    echo "Remove database running_$dbid..."
    dropdb running_$dbid

    echo "Create database ready_$dbid..."
    createdb ready_$dbid
fi

# Show generated databases
psql --dbname=postgres -c "SELECT pg_database.datname, pg_size_pretty(pg_database_size(pg_database.datname)) AS size FROM pg_database ORDER BY pg_database.datname;"

# No stop
if [ $NOSTOP ]; then
    tail -F /open.txt
fi
