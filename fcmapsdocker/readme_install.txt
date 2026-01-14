Install WSL2
------------
    wsl.2.6.3.0.x64.msi

    wsl --version

    WSL-Version: 2.6.3.0
    Kernelversion: 6.6.87.2-1
    WSLg-Version: 1.0.71
    MSRDC-Version: 1.2.6353
    Direct3D-Version: 1.611.1-81528511
    DXCore-Version: 10.0.26100.1-240331-1435.ge-release
    Windows-Version: 10.0.26200.7462

Install Docker Desktop
----------------------
    Docker Desktop Installer.exe

    Use WSL 2 instead of Hyper-V 
    
    Docker account anlegen

Install Postgresql 17.7.2
-------------------------
    postgresql-17.7-2-windows-x64.exe

    Superuser password: pwd
    Port: 5433
    Stack Builder: Spatial Extensions -> PostGIS 3.6.1

    Eigene IP-Adresse ermitteln: ipconfig
    
    Eigene IP-Adresse eintragen in "C:\Program Files\PostgreSQL\17\data\pg_hba.conf"
    host    all             all             192.168.178.22/32       scram-sha-256

Configure _env.bat
------------------
    @echo off
    set dbid=1
    set PGHOST=192.168.178.22
    set PGPORT=5433
    set PGUSER=postgres
    set PGPASSWORD=pwd
    set SRTMUSER=...
    set SRTMPASSWORD=...

