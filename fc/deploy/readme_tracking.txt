Flight Contest Live Tracking Notes
==================================

Changes 4.1.2
-------------
- ANR Live Tracking does not work by following conditions:
    * ANR route contains semi-circles or scenic legs
    * Crews with deviated corridor width

Changes 4.1.1
-------------
- Bug "Created ANR navigation tasks don't work" fixed

Changes 4.1.0
-------------
- Adjustments to Live Tracking API changes (Parse and creation of contests)
- Creation of ANR navigation tasks added (not operable, some adjustments on the server needed)

Changes 3.3.0
-------------
- Live Tracking task settings for the 'Results Service':
  Each landing test has now it's own test column (max. 4: Landing1, Landing2, Landing3, Landing4)

Changes 3.2.1.1
---------------
- Adjustments to Live Tracking API changes (scorecard overrides)

Changes 3.2.1
-------------
- Adjustments to Live Tracking API changes

Changes 3.2.M25
---------------
- Command "Crews -> Create and connect live tracking teams for selected crews" runs "Import live tracking teams" for selected teams previously

Changes 3.2.M21
---------------
- Command "Add navigation tracks" added after reconnection of a navigation task

Changes 3.2.M17
---------------
- Tasks without navigation test can now be configured for 'Results Service':
    -> Tasks -> <Task name> (Live tracking settings) -> Create task: Create task to 'Results Service'
    -> Tasks -> <Task name> (Live tracking settings) -> Delete task: Delete task and it's tests from 'Results Service'
- Task date will be set to actual date if navigation task will be created:
    -> Tasks -> <Task name> (Live tracking settings) -> Set task date with current date when creating a navigation task
        If disabled task date won't be modified.
- Visibilities of contests and navigation tasks can now be set.
    -> Contest -> Settings -> Live tracking -> Set Public / Set Unlisted / Set Private
        Sets the visibility of navigation tasks lower if it is higher than the new contest visibility.
    -> Tasks -> <Task name> (Live tracking settings) -> Set Public / Set Unlisted / Set Private
        Sets the visiblity of the contest higher if it is lower than the new navaigation task visibility.
- When Live Tracking is connected, the crews are now indicated by a symbol behind the start number in the planning list.
    -> Planning
      If the symbol is crossed out, the crew is connected with the contest but not with the navigation task.
      To fix it you have to select the crew and call 'Update live tracking'.
- Difference detection bug for airspeed and other changes of contest teams fixed.

Changes 3.2.M16
---------------
- Bidirectional Live Tracking team management added to crew list
  * Allows to import crews from Live Tracking contest's team list
    -> Crews -> Import live tracking teams
    Existing teams will be identified by e-mail and not imported again.
    Existing disconnected teams will be connected.
    Name differences and other differences will be shown by a red symbol. Click on it for more information.
  * Allows to add crews to Live Tracking contest's team list
    -> Crews -> Create and connect live tracking teams for selected crews
    Non existing teams will be created.
    Live Tracking teams are an unique combination of Name/Email (Live Tracking crew), Team (Live Tracking club), and Aircraft (Live Tracking aeroplane).
    Aeroplane's colour and type will be updated if aeroplane's registration exists.
  * Allows to delete crews from Live Tracking contest's team list
    -> Crews -> Disconnect and delete live tracking teams for selected crews
    Teams will be deleted from Live Tracking contest's team list but not from the list of registered teams.
  * Differences between Live Tracking contest's team list and the crew list can be resolved by modifications on both sides.
    Call "-> Crews -> Import live tracking teams" again to check for remaining differences.
  * It is possible to use connected and non-connected crews at the same time for tasks.
    Connected crews will be shown by a connection symbol behind the start number.
    After the creation of a navigation task it is not possible to use afterwards connected crews for this navigation task.
- Endpoint POST "contests/{id}/importnavigationtask/" is no longer used and can be deleted
- Chapter "Full Live Tracking management by Flight Contest" added, see below
- Chapter "Team Management by Live Tracking" added, see below
- Chapter "Load historical contests" modified, see below

Changes 3.2.M15
---------------
- Live Tracking task settings have been moved to a separate dialog:
    -> Tasks -> <Task name> (Live tracking settings)
    -> Results -> <Task name> (Live tracking settings)
- Live Tracking task settings contains follow new controls for configuring of the 'Results Service':
    * Buttons "Create planning test" and "Delete planning test"
        Available if task setting 'Planning Test' has been enabled.
        Enable/Disable submitting of planning test results to 'Results Service'
        Enabled submitting will be shown by a column header symbol.
    * Buttons "Create observation test" and "Delete observation test"
        Available if task setting 'Observation Test' has been enabled.
        Enable/Disable submitting of observation test results to 'Results Service'
        Enabled submitting will be shown by a column header symbol.
    * Buttons "Create landing test" and "Delete landing test"
        Available if task setting 'Landing Test' has been enabled.
        Enable/Disable submitting of landing test results to 'Results Service'
        Enabled submitting will be shown by a column header symbol.
    * Buttons "Create other test" and "Delete other test"
        Available if task setting 'Other Test' has been enabled.
        Enable/Disable submitting of other test results to 'Results Service'
        Enabled submitting will be shown by a column header symbol.
    * Check box "Submit navigation test results"
        If enabled navigation test results will be submitted to 'Results Service'
        Enabled submitting will be shown by a column header symbol.
    * Check box "Submit test results immediately"
        If enabled each 'Result ready' submits the calculated or edited penalties to the associated team's test immediately.
        Immediate submit will be shown with a not crossed out header symbol.
- Publishing of results to the 'Results Service' added.
    Mode 1 - Immediate submit by 'Result ready' (shown by not crossed out header symbol):
        Each with 'Result ready' approved test result will be submitted immediately.
    Mode 2 - Send on command (shown by crossed out header symbol):
        -> Results -> <Task name> -> Update live tracking results
            Each not sent test results will be submitted.
            If there are submit errors you can sent affected test results with this command again.
    Successfully submitted test results will be shown by a green upload symbol.
    Test results with submit error will be shown by a red upload symbol.
    If there are differences between Flight Contest results and the 'Results Service':
      * Disconnect navigation task
      * Connect navigation task
      * Enable 'Submit navigation test results' and 'Save'
      * Update live tracking results: All approved test results will be submitted again

Changes 3.2.M14
---------------
- OSM online map layer 'Flight Contest' added.
  Allows to display route and navigation flights interactively on the OSM contest map.
- Configuration of a custom tile server
  -> Help -> Installation dependent configuration -> Configure OSM online map

Changes 3.2.M13
---------------
- English help added

Changes 3.2.M12
---------------
- Tracker points import improved (tolerates timestamps with fractions of a seconds, e.g. 2021-03-23T13:38:21.999000Z)
- Map configuration parameters display.frontend and display.frontendMap removed. They are now hard coded.
- Planning -> Update live tracking: Detailed error messages added.
- Using of endpoint /api/v1/contests/{contest_pk}/navigationtasks/{navigationtask_pk}/contestants/{id}/update_without_team/ for contests with teams managed by live tracking.

Changes 3.2.M11
---------------
- Crew list tracker id is now optional
- Missing tracker id no longer leads to crew exclusion in the navigation task
- Random tracker id for historical contests removed
- kml route import improved (ignore too much spaces in LineString coordinates)

Changes 3.2.M10
---------------
- Choice list for time zone added.
  -> Contest -> New Contest -> Time zone
  -> Contest -> Settings -> Time zone
- Date picker control for contest and task date added.
  -> Contest -> New Contest -> Contest date
  -> Contest -> Settings -> Contest date
  -> Tasks -> <Task name> -> Settings -> Live tracking -> Navigation task date

Changes 3.2.M9
--------------
- Contest latitude and longitude added. Calculated from the take-off coordinate of the first route.

Changes 3.2.M8
--------------
- Possibility to add incomplete navigation tracks added. Uploaded tracks end 2 minutes after the time the first team should have reached the finish point.
  The map shows teams that are still on the track.
  -> Tasks -> <Task name> -> Live tracking -> Add incomplete navigation tracks
- Problems with crew's tracker id initialization for new contests fixed
- Missing route's scorecard after route import fixed

Changes 3.2.M7
--------------
- Scorecard to route settings added
    Show value: -> Routes -> <Route name> -> Route details -> Live tracking scorecard
    Edit value, if route not used: -> Routes -> <Route name> -> Route settings -> Live tracking scorecard
    Value will be initialized by contest default settings.
    Configured value will be used for navigation task creation.
- Tracking navigation task parameter calculator_type removed

Changes 3.2.M6
--------------
- Calculation of the navigation flight with live tracking data (instead of logger data) added.
  -> Results -> <Task name> -> Navigation flight -> ... (crew) -> Import tracker data

Changes 3.2.M5.1
----------------
- Time zone configuration in "C:\FCSave\.fc\config.groovy" moved to contest (flightcontest.livetracking.contest.timeZone)

Changes 3.2.M5
--------------
- Updates of timetables for contestants added. The update runs with selected crews.
  -> Planning -> <Task name> -> Update live tracking
- Track upload for contestants added. The upload runs with selected crews.
  -> Planning -> <Task name> -> Add navigation tracks

Changes 3.2.M4.1
----------------
- Score overrides for check of latest landing time implemented

Changes 3.2.M4
--------------
- Scorecard configuration in "C:\FCSave\.fc\config.groovy" removed (flightcontest.livetracking.navigationtask.scoreCard)
- Scorecard to contest default settings added (-> Contest -> Defaults -> Live tracking scorecard)
- Usage of navigation task parameter use_procedure_turns removed
- Contestant parameter competition_class_... supported (for contests with classes)
- Score overrides implemented. Used by:
    -> Contest -> Points -> Navigation Test
    -> Classes -> <Class name> -> Points -> Navigation Test
    -> Tasks -> <Task name> -> Settings -> Navigation Test -> Time check of secret coordinates
    -> Tasks -> <Task name> -> Settings -> Navigation Test -> Check take-off time automatically by logger data 
    -> Tasks -> <Task name> -> Settings -> Navigation Test -> Check latest landing time automatically by logger data

Changes 3.2.M3.1
----------------
- Bug with availability of "Add navigation tracks" fixed
- Bug with multiple first names fixed

Changes 3.2.M3
--------------
- FCSetup: Check for manual uninstall of 3.1.x versions
- Crew print: Tracker id added
- Java heap max size changed to 2048 MB
- GPX upload bug for curved routes fixed
- Tracking navigation task parameter use_procedure_turns supported
- Contest settings: Crew delimiter for separation of pilot/navigator and first/last name added 
- Tracking contestant settings for club supported (will be set with team name)
- Bug with gpx route attribute "notimecheck" and "nogatecheck" fixed ("Results -> Disable check points" was not used).

Changes 3.2.M2
--------------
- Disconnect commands added.
    Allows to remove a saved connection to a contest or navigation task
- Connect commands added.
    Allows to etablish the connection to an existing contest or navigation task
    It parses the contest / navigation task titles.
    Connect command of a navigation task does not check the exact match of contestants.
- Crews-Command "Import live tracking teams" added.
- Process for a new Live Tracking contest implemented.
- A lot of new configuration settings added.

Install Flight Contest
----------------------

1. 
Uninstall an older FC version (3.1.x) manually before you install the new version (3.2.x).
If you want to take over old data you can do it after installation of this new version.

2.  
Set configuration for Live Tracking

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

3.
Install FC with start of FCSetup-3.2.x.exe

Windows service will be installed and started.

4.
Take over of old data from previous 3.1.x installation
  5.1. Stop FC (Start menu -> Flight Contest -> Stop Flight Contest)
  5.2. Copy 'C:\Program Files (x86)\Flight Contest\fc\fcdb.h2.db' to 'C:\Program Files\Flight Contest\fc\fcdb.h2.db'
  5.3. Start FC (Start menu -> Flight Contest -> Start Flight Contest)


Full Live Tracking management by Flight Contest
-----------------------------------------------
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
   

Team Management by Live Tracking
--------------------------------

1. Live Tracking: Create teams, with very detailed information (first name, last name of pilot and navigator, aeroplane, club, ...)
   -> New contest
   -> Contest - Team-List -> Add team

2. Flight Contest: Connect with a live tracking contest
   -> Contest -> Settings -> Live tracking -> Connect contest
   
3. Flight Contest: Live tracking teams take over
   -> Crews -> Import live tracking teams
   

Load historical contests
------------------------

1. Copy database of the historical contest
  1.1. Stop FC (Start menu -> Flight Contest -> Stop Flight Contest)
  1.2. Copy 'HistoricalContest.fcdb.h2.db' to 'C:\Program Files\Flight Contest\fc\fcdb.h2.db'
  1.3. Start FC (Start menu -> Flight Contest -> Start Flight Contest)

2. Open FC
   -> http://localhost:8080/fc -> <Contest name>

3. Create live tracking contest
   -> Contest -> Settings -> Live tracking -> Create contest

4. Set emails of crews you want to show
   -> Crews -> <Name> -> Email

5. Create live tracking teams
   -> Crews -> Select all
   -> Crews -> Create and connect live tracking teams for selected crews

6. Create navigation task
   -> Tasks -> <Task name> (Live tracking settings) -> Create navigation task

7. Add tracks
   -> Tasks -> <Task name> (Live tracking settings) -> Add navigation tracks
