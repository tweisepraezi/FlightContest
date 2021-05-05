Flight Contest 3.2
==================

Changes
-------
- New runtime environment
    Platform: x64
    Runtime environment: OpenJDK 8, Tomcat 9
    Framework: Grails 2.5.6
    Service will be installed with automatic start.
    Direct AFLOS connection deleted.
    Requires manual uninstallation of an older version (3.x). See below.

- Contest date and time zone added.

- Task date added.

- Live Tracking added.
    Configuration for Airsports Live Tracking see below.

    Full Live Tracking management by Flight Contest:
        1. Create contest (with crews and routes)
           -> Contest -> New Contest
           -> Routes -> Import route
           -> Crews -> Import Excel crew list

        2. Create live tracking contest
           -> Contest -> Settings -> Live tracking -> Create contest

        3. Create live tracking teams
           -> Crews -> Select all
           -> Crews -> Create and connect live tracking teams for selected crews
           
        4. Create task with navigation test (generates flight plans)
           -> Tasks -> New task
           -> Tasks -> <Task name> -> Add Navigation Test
           -> Planning -> <Task name> -> Select all
           -> Planning -> <Task name> -> Assign wind
           -> Planning -> <Task name> -> Calculate timetable
           
        5. Create live tracking navigation task
           -> Tasks -> <Task name> (Live tracking settings) -> Create navigation task
           
        6. Configuring the 'Results Service' for additional tests
           -> Tasks -> <Task name> (Live tracking settings) -> Create planning test (if exists)
           -> Tasks -> <Task name> (Live tracking settings) -> Create observation test (if exists)
           -> Tasks -> <Task name> (Live tracking settings) -> Create landing test (if exists)
           -> Tasks -> <Task name> (Live tracking settings) -> Create other test (if exists)
           -> Tasks -> <Task name> (Live tracking settings) -> Enable "Submit test results immediately"
           -> Tasks -> <Task name> (Live tracking settings) -> Save
           With these settings each with 'Result ready' approved test result will be submitted immediately.

        7. Enter test results
           -> Results
           
        8. Publish final navigation test results to the 'Results Service'
           -> Tasks -> <Task name> (Live tracking settings) -> Enable "Submit navigation test results"
           -> Tasks -> <Task name> (Live tracking settings) -> Save
           -> Results -> <Task name> -> Update live tracking results

- Special CSS properties moved to several dialogs
    --route --disable-procedureturn     -> Routes -> <Route name> -> Route settings -> Use procedure turns
    --route --show-curved-points        -> Routes -> <Route name> -> Route settings -> Show SCs of the curved route in route maps of this route
    --class --secret-gatewidth          -> Classes -> <Class name> -> Different gate width from UZK coordinates for this class
    --class --before-starttime          -> Classes -> <Class name> -> Forward the planning start time before the regular planning time for this class
    --class --add-submission            -> Classes -> <Class name> -> Extention of the latest submission time for submitting the solution sheet for this class
    --flightplan hide-distance          -> Tasks -> Navigation Test -> <Navigation test name> -> Edit -> Show values in flight plan's column 'Distance'
    --flightplan hide-truetrack         -> Tasks -> Navigation Test -> <Navigation test name> -> Edit -> Show values in flight plan's column 'True Track'
    --flightplan hide-trueheading       -> Tasks -> Navigation Test -> <Navigation test name> -> Edit -> Show values in flight plan's column 'True Heading'
    --flightplan hide-groundspeed       -> Tasks -> Navigation Test -> <Navigation test name> -> Edit -> Show values in flight plan's column 'Ground Speed'
    --flightplan disable-local-time     -> Tasks -> Navigation Test -> <Navigation test name> -> Edit -> Show flight plan's column 'Local Time'
    --flightplan show-elapsed-time      -> Tasks -> Navigation Test -> <Navigation test name> -> Edit -> Show elapsed time in flight plan's column 'Flight time'
    --submission                        -> Tasks -> Navigation Test -> <Navigation test name> -> Edit -> Latest submission time of the solution sheet after reaching the FP
    --route --start-tp --add-num        -> Tasks -> Navigation Test -> <Navigation test name> -> Edit -> Increase turnpoint numbers by the specified value from the specified turnpoint in the flight plans for crews
    --flightresults show-curved-points  -> Tasks -> Navigation Test -> <Navigation test name> -> Edit -> Always show SCs of curved legs in the navigation flight result
    --landingresults                    -> Evaluation -> Contest Evaluation -> Contest evaluation settings -> Reduce landing penalty points by specified factor

- The map scale is now set in the route settings.

- OSM Contest Map:
    All options will be saved in database.
    Curved legs can be disabled separately.
    Direct generation without route details added.
    A 2nd set of generation options added.
    Emailing of contest maps added.

- Judge timetable: 
    Print option 'Latest submission time' added
    Print option 'Landing field' added
    Configuration value 'flightcontest.landing.info' added

- New crew list template:
  -> Help -> Create crew list -> FC-CrewList-Sample.xlsx

- English help added

- OSM online map layer 'Flight Contest' added.
  Allows to display route and navigation flights interactively on the OSM contest map.

Live Tracking Configuration
---------------------------

Add following configuration data in "C:\FCSave\.fc\config.groovy"
Remove comment characters for optional configuration values

flightcontest {
  livetracking {
    server = "https://airsports.no"
    api = "/api/v1"
    token = "TODO"
    // showids = true // Default: false
    contest {
        showDelete = true // Default: false
        // createPublic = true // Default: false
        startLocalTimeOfDay = "08:00" // Default: 06:00
        endLocalTimeOfDay = "20:00" // Default: 22:00
        // timeZone = "Europe/Berlin" // for historical contests without time zone, Default: Europe/Oslo
    }
    navigationtask {
        showDelete = true // Default: false
        // createPublic = true // Default: false
    }
    contestant {
        minutesBetweenTrackerStartAndTakeoff = 10 // Default: 5
    }
  }
  wrlog {
    enable = true // Log will be availabe in C:\Program Files\Flight Contest\tomcat\logs\flightcontest-stdout.<date>.log
  }
}

Install Flight Contest
----------------------

1. 
Uninstall an older FC version (3.1.x) manually before you install the new version (3.2.x).
If you want to take over old data you can do it after installation of this new version.

2.
Install FC with start of FCSetup-3.2.x.exe

Windows service will be installed and started.

3.
Take over of old data from previous 3.1.x installation
  5.1. Stop FC (Start menu -> Flight Contest -> Stop Flight Contest)
  5.2. Copy 'C:\Program Files (x86)\Flight Contest\fc\fcdb.h2.db' to 'C:\Program Files\Flight Contest\fc\fcdb.h2.db'
  5.3. Start FC (Start menu -> Flight Contest -> Start Flight Contest)
