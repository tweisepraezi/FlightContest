Flight Contest
==============

Changes 3.3.0
-------------
- The route list and route details now show the usage of tasks.
- Minimum altitude is now initialized at 300ft (500ft - 200ft tolerance) for new routes.
- Altitude values in dialogs and route printings now show altitude of the terrain (GND:), the minimum flight altitude (>) and the maximum flight altitude (<).
- Bug "Route altitude adjustments will not be applied to the next logger evaluation" fixed
- Support of numeric keyboards added
	When editing the planning given value, the Enter key can be used to navigate to the next input field.
	When editing measured values of checkpoints, the Enter key can be used to navigate to the next input field.
	When entering distances in observation results, the Enter key can be used to navigate to the next input field.
	When entering times, a ',' is also accepted instead of the ':', among others.
- Offline map for check points now shows flight from previous check point.
- Print footer for evaluation prints added.
- Task settings: Option "Lock planning" added. Disables all commands that can change the planning.
- Planning: Export timetable (Start list) added. For FFA_SkyTraq's start list.
- Route printing: Overview of coordinate map distances (in mm) added.
- Crew printing: Team printing and aircraft printing to subsequent pages added.
- Landing result input: Button 'Save and next result' added.
- Results: The result list now contains a separate column for each landing test (max. 4: Ldg1, Ldg2, Ldg3, Ldg4).
- Result input: Button 'Previous result' added.
- Observation result input has been optimized for touchscreen use:
    Route photo input has been changed from choice list to buttons.
	Radio buttons were enlarged and provided with frames.
- All list details: 'Previous item' button added.
- Planning: Landing test start list added. Allows printout of a start list without setting a schedule.
  Printout with grouping.
- Read logger: Option 'Read logger of a new port immediately' added.
- Planning, Results and Crews: Page breaks can be added to form groups.
    Page breaks are inserted before the configured crew when printing.
	The activated option "Show pages one by one" reduces the crew display to the selected page
	and limits navigation to the next or previous crew.
- Route import improved:
	Up to 3 coordinates can be declared as semicircle center points.
	The course change to be evaluated for automatic determination of UZK coordinates can be adjusted (default: 1.5°).
- Empty enroute canvas sign added.
    The input is made by selecting the *. Causes additional lines to be printed in the evaluation form.
	A correct result is then that no canvas sign was found in these lines.

Changes 3.2.4
-------------
- Export timetable (Data) added
  Returns a JSON file containing the takeoff time and all turnpoint overflight times for all crews.
- Delete command of turnpoint photos added
- Delete command of enroute photos added

Changes 3.2.3
-------------
- For each individual check point of a route, a separate minimum altitude above ground can now be specified
  (different from the definition in the track settings, which otherwise applies to all check points).
- For each individual check point of a route, a maximum altitude above ground can now also be specified.
  If the maximum altitude is exceeded, the user will be penalized for violating the correct altitude.
- When determining such altitudes, please note that the barometric altitude measurement during the flight 
  results in a different altitude than the GPS measurement, which is evaluated with these specifications.
  Therefore, reduce the actual minimum altitude to be evaluated, or increase the actual maximum altitude to be evaluated 
  by a tolerance value (e.g. 200ft) in order not to penalize cases near this specifications.

Changes 3.2.2.2
---------------
- Bug "Altitude evaluation delivers altitude error on flybys" fixed

Changes 3.2.2.1
---------------
- Update to GPSBabel 1.8.0

Changes 3.2.2
-------------
- Saving links to uploaded OSM maps has been extended to include "All route details".

Changes 3.2.1.1
---------------
- Adjustments to Live Tracking API changes (scorecard overrides)

Changes 3.2.1
-------------
- OSM Contest Map:
    Individual configuration of the appearance of each airspace added
    Horizontal and vertical shift of map center added

- Offline map for each individual turn point:
    Time offset to shift the shown logger recording section added

- Adjustments to Live Tracking API changes

Changes 3.2.0
-------------
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
           -> Contest -> Settings -> Live tracking -> Set Public

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
           -> Tasks -> <Task name> (Live tracking settings) -> Set Public
           
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

- The minimum altitude above ground is now defined in the route settings. Consequently, the terrain height above normal zero must be entered for the individual coordinates.
  The minimum altitude above normal zero is therefore the sum of the minimum altitude above ground and the terrain height.
  
- OSM Contest Map:
    All options will be saved in database.
    Curved legs can be disabled separately.
    Direct generation without route details added.
	Direct generation with all route details added.
    3 more sets of generation options added.
    Emailing of contest maps added.

- Judge timetable: 
    Print option 'Latest submission time' added.
    Print option 'Landing field' added.
    Configuration value 'flightcontest.landing.info' added.

- New crew list template:
  -> Help -> Create crew list -> FC-CrewList-Sample.xlsx

- English help added

- OSM online map layer 'Flight Contest' added.
  Allows to display route and navigation flights interactively on the OSM contest map.

- Route planning: 'Create semicircle by specifying a circle center' added.
  -> Help -> Route planning -> Create competition route with semicircles

- Photo management and printing added
    Enroute photo names can be assigned alphabetically or randomly. The first image determines whether letters or numbers are used.
      -> Routes -> <Route name> -> Assign enroute photo names alphabetically
      -> Routes -> <Route name> -> Assign enroute photo names randomly
    Turnpoint and enroute images can be imported individually.
      -> Routes -> <Route name> -> Turnpoint observations -> <No.> -> Import turnpoint photo image
      -> Routes -> <Route name> -> Enroute photos -> <No.> -> Import enroute photo image
    Turnpoint and enroute photos can be imported in a ZIP file.
      -> Routes -> <Route name> -> Import turnpoint photo images (SP.jpg, TP1.jpg, ..., FP.jpg, name must match check point).
      -> Routes -> <Route name> -> Import enroute photo images (1.jpg, 2.jpg, ..., name must match photo name)
    Photo marker can be moved with one click. If necessary, a page change can also be inserted in turnpoint observations for printing.
      -> Routes -> <Route name> -> Turnpoint observations -> <No.>
      -> Routes -> <Route name> -> Enroute photos -> <No.>
    Turnpoint and enroute photos can be printed alphabetically or sorted by their occurrence on the track.
      -> Routes -> <Route name> -> Print turnpoint photos
      -> Routes -> <Route name> -> Print turnpoint photos (alphabetical)
      -> Routes -> <Route name> -> Print turnpoint photos (Route)
      -> Routes -> <Route name> -> Print enroute photos (alphabetical)
      -> Routes -> <Route name> -> Print enroute photos (Route)
    Image Creation Notes:
      Create all images as JPG files in 4:3 format.
      Name turnpoint images with capitalized English check point names and put them into a ZIP file (e.g. SP.jpg, TP1.jpg, ..., FP.jpg)
      Name enroute images with numbers in the order of their occurrence and pack them into a ZIP file (e.g. 1.jpg, 2.jpg, ...)
      List enroute image coordinates in a UTF-8 text file, e.g.:
        1, Lat 52° 12.10000' N, Lon 016° 45.90000' E
        2, Lat 52° 16.80000' N, Lon 017° 23.10000' E
        ...
      These turnpoint images can also be added to the "turnpointphotos" folder of an already created kmz track file.
      These track images can also be added to the "photos" folder of an already created kmz track file.

- 'Show logger data' extended with 'Gpx download'

- Updating of the following sets of rules
    FAI Precision Flying - Edition 2022
    FAI Air Rally Flying - Edition 2022
    
- Logger evaluation: In the list of calculated points, the offline map is now quickly available for each individual turn point.

- The readout of the logger 'Renkforce GT-730FL-S', 'GlobalSat DG-100', and 'GlobalSat DG-200' can now be done directly from Flight Contest.
  Requirement: Installation of GPSBabel (http://www.gpsbabel.org/download.html)
    -> Help -> Read logger

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
        startLocalTimeOfDay = "08:00" // Default: 06:00
        endLocalTimeOfDay = "20:00" // Default: 22:00
        // timeZone = "Europe/Berlin" // for historical contests without time zone, Default: Europe/Oslo
    }
    navigationtask {
        showDelete = true // Default: false
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
