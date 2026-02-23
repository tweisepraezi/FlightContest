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

    Superuser password: ...
    Port: 5433
    Stack Builder: Spatial Extensions -> PostGIS 3.6.1

    In "C:\Program Files\PostgreSQL\17\data\pg_hba.conf" alle IP-Adressen für SQL-Abfragen zulassen
        # All ip connections (for access from WSL docker linux)
        host    all             all             0.0.0.0/0               scram-sha-256

Configure _env.bat
------------------
    view3:
        @echo off
        set dbid=1
        set PGHOST=172.20.144.1
        set PGPORT=5433
        set PGUSER=postgres
        set PGPASSWORD=...
        set CONTOURSOURCES=view3
    srtm3 && view3:
        @echo off
        set dbid=1
        set PGHOST=172.20.144.1
        set PGPORT=5433
        set PGUSER=postgres
        set PGPASSWORD=...
        set CONTOURSOURCES=srtm3,view3
        set SRTMUSER=...
        set SRTMPASSWORD=...

    In PGHOST die IP-Adresse von "Ethernet-Adapter vEthernet (WSL (Hyper-V firewall))" eintragen.
        Zu ermitteln mit: ipconfig
