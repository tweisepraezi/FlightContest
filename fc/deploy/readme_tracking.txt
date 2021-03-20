Flight Contest 3.2 Live Tracking Notes
======================================

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
- Endpoint "importnavigationtask": E-Mail addresses "...@anonymous.flightcontest.de" added to contestants if email does not exist in crews list.

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
- Crews-Command "Import Live Tracking Teams" added.
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
    server = "https://tracking.airsports.no"
    api = "/api/v1"
    token = "..."
    display {
        frontend = "/display/frontend/"
        frontendMap = "/map/"
    }
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

3.
Install FC with start of FCSetup-3.2.x.exe

Windows service will be installed and started.

4.
Take over of old data from previous 3.1.x installation
  5.1. Stop FC (Start menu -> Flight Contest -> Stop Flight Contest)
  5.2. Copy 'C:\Program Files (x86)\Flight Contest\fc\fcdb.h2.db' to 'C:\Program Files\Flight Contest\fc\fcdb.h2.db'
  5.3. Start FC (Start menu -> Flight Contest -> Start Flight Contest)


Load historical contests
------------------------

1. Copy database of the historical contest
  1.1. Stop FC (Start menu -> Flight Contest -> Stop Flight Contest)
  1.2. Copy 'HistoricalContest.fcdb.h2.db' to 'C:\Program Files\Flight Contest\fc\fcdb.h2.db'
  1.3. Start FC (Start menu -> Flight Contest -> Start Flight Contest)

2.
Open FC with http://localhost:8080/fc

3.
Create a new live tracking contest
-> Contest -> Settings -> Live tracking -> Create contest (at the bottom of the form)

4.
Create a new navigation task
-> Tasks -> <Task name> -> Settings -> Live tracking -> Create navigation task

5.
Add tracks
-> Tasks -> <Task name> -> Settings -> Live tracking -> Add navigation tracks


New contest with Live Tracking
------------------------------

Before the competition:
1. Teams are created in the tracking system, with very detailed information (first name, last name and pictures of pilot and co-pilot, country, club, ...)
   -> New contest
   -> Contest - Team-List -> Add team
2. Routes are created in Flight Contest
   -> Contest -> New Contest
   -> Routes -> Import route
   
Contest names must be identical in both systems (neccessary for connecting with each other).

On the day of the competition:
3. Flight Contest: Connect with a tracking contest
   -> Contest -> Settings -> Live tracking -> Connect contest
4. Flight Contest: Takeover of the crews from the tracking contest
   -> Crews -> Import Live Tracking Teams
5. Flight Contest: Create task with navigation test (generates flight plans)
   -> Tasks -> New task
   -> Tasks -> <Task name> -> Add navigation test
   -> Planning -> <Task name> -> Select all
   -> Planning -> <Task name> -> Assign wind
   -> Planning -> <Task name> -> Calculate timetable
6. Flight Contest: Creating a tracking navigation task
   -> Tasks -> <Task name> -> Settings -> Live tracking -> Create navigation task
   
With this the tracking system is ready for use and it can receive data from suitable tracking loggers and immediately calculate penalty points from it.
