Flight Contest
==============

Changes 4.1.1
-------------
- Updating of the following sets of rules
    Wettbewerbsordnung Rallyeflug Deutschland 2025
    Wettbewerbsordnung Air Navigation Race Deutschland 2025
    Regelwerk Landewertung Deutschland 2025
- OSM Contest Map: Now all airspaces where the lower altitude limit is less than or equal to the set altitude are taken into account.
- ANR corridor evaluation: Only one second legs of logger measurement points lying outside the corridor are taken into account for calculating penalty points.
- Bug "Created live tracking ANR navigation tasks don't work" fixed

Changes 4.1.0
-------------
- Edit contest points: Possibility for take over of landing points of another ruleset added
- Edit contest points: "Recalculate all results" added
    Changes to the penalty points no longer result in the penalty points of all results being recalculated immediately.
    This function must be called up explicitly for this purpose.
- OSM Contest Map: Airfield OpenAIP queries are now saved in the route.
    Use "Search airfields around the airport" to determine the airfields before printing.
    Airfields starting with # are ignored during map generation.
- OSM Contest Map: "Generate (only T/O)" added.
    Prints map with T/O point but without further turning points.
- Internal Task Creator: Page guide added.
    The selected page size (A3, A4, ...) and orientation (Portrait or Landscape W) 
    is displayed around the center of existing check and plotting points.
    To define the page location before entering the route, first enter the takeoff and a plotting point.
- Option to launch the internal and external Task Creator in the map menu added.
- Bug "OSM Contest Map cannot be printed with ANR rules if only the T/O point is available" fixed
- Bug "Google Earth display in the internal Task Creator no longer works" fixed
- Bug "Live tracking contest cannot be created" fixed
- Bug "OpenAIP layer in OSM online map no longer works" fixed
- Help chapter "Restore 'Flight Contest' database damaged by installation" added

Changes 4.0.2
-------------
- OSM Contest Map: Airspace OpenAIP queries are now saved in the contest.
    This considerably speeds up the start of map generation for follow-up orders.
    The boundaries of retrieved airspaces can be displayed in the map menu under the 'Airspaces' entry.    
- Logger evaluation: When adjusting the runway with "Wind (Move runway)", the following actions are now available there:
    "Offline map (T/O)" - Display logger data with route at T/O point
    "Offline map (LDG)" - Display logger data with route at LDG point
    "Recalculate logger data again" - calculates the penalty points with the moved runway immediately
    The position deviation of the LDG point along the runway is now preset to -0.03NM to avoid an identical position with a T/O point.
- Bug "Penalty points for landings are not configured correctly in the German ANR rules" fixed
- Bug "Gate width of T/O and LDG of a used route cannot be changed with ANR rules" fixed
- Bug "When map regions overlap, the OSM Contest Map is not always printed completely" fixed
- Bug "Non-ANR formats can be selected for the route default print map" fixed
- Bug "OSM competition map cannot be printed if airspace name contains a quotation mark" fixed

Changes 4.0.1
-------------
- ANR competition map: The corridor width in NM is now printed in the bottom right corner.
- OSM Contest Map: Turnpoint circles are now printed with the diameter of the gate width (previously 1 NM).
- Offline viewer: Distance measurement added. Start with shift-click on a gate.
    With a subsequent shift-click, the measured distance can be drawn into the display with a line.
- Bug "Incorrect points calculation in scenic route legs" fixed

Changes 4.0.0
-------------
- Planning and evaluation of ANR competitions added

Changes 3.6.0
-------------
- Configuration management for Maps and OpenAIP server settings added
    A central server performs all the settings necessary for generating the maps.
      To do this, the client-id of the Flight Contest installation must be registered.
      The client-id is displayed in “Extras -> Get client-id”. The successful loading of the configuration is also displayed here.
      The client-id can also be determined using the Flight Contest Manager.
      Register your client here: https://flightcontest.de/register-client.
      The configuration is loaded when the Flight Contest service is started. If no internet connection was available at this point,
      you can use “Extras -> Get client-id -> Load configuration” to load the configuration after the Internet connection has been established.
    The use of the map generator is logged on the central server.
      This includes the IP address and time of access for a client-id.
      This information is stored for 400 days to enable an annual billing of the server usage.
    Previously used configuration settings in config.groovy are no longer effective.
      To use your own OpenAIP API key, the following new configuration settings are to be used:
        flightcontest {
          maps {
            openaip {
              server = "https://api.core.openaip.net/api"
              apikey = "TODO"
              ignoreAirspacesStartsWith = ""
            }  
          }
        }
- Bug “Map projection is lost when map generation is interrupted” fixed
- Updating of the following sets of rules
    FAI Precision Flying - Edition 2024

Changes 3.5.0
-------------
- OSM Contest Map: Integrated editing of additional map objects added
    Churches, castles, ruins, chateaus, mountain peaks, towers, communication towers, lighthouses, wind power stations,
    circle centers, airfields and own symbols can be added to the map for drawing.
    Each object can be provided with a subtitle.
    The additional map objects are included in a gpx and kml export.
    The map objects of a route can be used in other routes.
    For a GPX export with semi-circle gates, the semi-circle center points are added as a map object.
- OSM Contest Map: Style changes
    Road bridges and forest paths were removed
    Field paths can now be added in 5 gradations (1 = few to 5 = many).
- OSM Contest Map: Support for additional map regions added
    In addition to Central European countries, maps of Central Chile can now also be generated.
- OSM Contest Map: Option 'Shift of the map center' has been renamed to 'Shift of the map border'
- OSM Contest Map: 'KMZ export hidden airspaces' added
- Routes: iTO has been removed. Only iLDG is to be used for Touch&Go landings.
- Loggers with several measured values per second are accepted.
- The competition default "Allow multiple usage of enroute canvas" is now activated by default.
- Routes: The calculation of route errors has been revised.
    The display column 'Usable' now indicates with 'Yes' that a route can be used for navigation flight or planning test.
    Red markings indicate errors due to non-compliance with the rules and regulations. 
    A route can also be used if the number of images, canvas signs or route legs is exceeded or not reached.
    The canvas count check now includes turnpoint canvas signs.
- OSM Contest Map: Option 'Turnpoint canvas' added. Prints the configured turnpoint canvas to the right of the turning point circle.
- Updating of the following sets of rules
    FAI Air Rally Flying - Edition 2024
    GAC Landing appendix - Edition 2024

Changes 3.4.8
-------------
- Help "Manage photos and canvas signs" added
- Help for the Maps menu added
- Import enroute photo: Option "Set names automatically" added. Numbers the photos starting with 1.
- Import enroute canvas: Option "Set names automatically" added. Adds canvas placeholder * with given coordinate.
  The canvas placeholder * must be replaced by the desired canvas sign before use in a competition.
- New route: Button "Create without observations" added
- Overview Timetable: Planning, take-off and landing times are printed for each group when page breaks are made to form groups.
- Internal Task Creator: Bug "Magnetic declination swapped east with west" fixed.

Changes 3.4.7
-------------
- OpenAIP access restored
- Enroute photo/canvas import: The number of importable photos and canvas is no longer limited.
- OSM Contest Map improvements:
    Turning point names have been reduced in size (16 points) and written closer to the turning point (1.1 NM).
    Enroute photo name is printed larger.
    Bug "Enroute photo/canvas symbols were sometimes missing" fixed
    Airspaces: Areas and FIS are ignored by default. The configuration flightcontest.openaip.ignoreAirspacesStartsWith
      can be used to add further airspaces to be ignored.
- Bug "Crew list printing crashes if crew without team is present" fixed
- Bug "Overview timetable: Plan output time range incorrect for newcomers" fixed
- Bug "Semicircle calculation: Gate direction at the end sometimes incorrect" fixed
- Bug "Export semicircle gates from circle centers: Semicircle after iSP incorrect" fixed
- Bug "Wind assignment did not recalculate time table warnings" fixed

Changes 3.4.6
-------------
- Internal Task Creator: German and Spanish user interface added.
  The desired language can be set under "Extras -> Settings".
- Flight Contest Manager: Flight Contest starts with Firefox if it is available, even if it is not the default browser.
- Bug "PDF contest map cannot be created if the map title contains umlauts" fixed

Changes 3.4.5
-------------
- Added map menu item "Export all contest maps"
- Map import now displays an error message if the map(s) to be imported already exist in the map menu.
    An existing map must first be deleted before it can be imported again.
- Internal Task Creator: 'Sync map url' button added.
    After loading a task that has been created on another computer, the map URL points to maps of another contest.
    With this button, the map URL is changed to the current contest.
- The airspace altitude to be taken into account is now also transported during route export/import and route copy.
- The title defined in the task for "Other Results" is now displayed in the final evaluation instead of "Other".
- Bug "Crew list crashes if crew is entered without team" fixed
- Bug "Renaming aircraft crashes if an already existing registration is used" fixed
- Bug "Route setting 'Default online map' shows too many selection values" fixed
- Bug "OSM online map did not display the map set in route setting 'Default online map'" fixed
- Bug "OSM Contest Map does not use the set print language" fixed
- Bug "KMZ export does not use the set print language" fixed

Changes 3.4.4
-------------
- "Flight Contest Manager" added.
  Starts as a taskbar icon when logging in.
  Contains the following context menu commands, which were previously only available as Windows start menu commands:
    Evaluation commands -> Auto load logger data: Starts background program for importing newly saved logger data.
    Evaluation commands -> Auto load observation forms: Starts background program for importing scanned observation forms
    Evaluation commands -> Auto load planning task forms: Starts background program for importing scanned planning test forms
    Service commands -> Service Manager: Starts FlightContest Service Manager, where e.g. the Java memory can be increased (Java -> Maximum memory pool)
    Service commands -> Restart Flight Contest: Stops and starts Flight Contest service
    Service commands -> Start Flight Contest: Starts Flight Contest service
    Service commands -> Stop Flight Contest: Stops Flight Contest service
    Service commands -> Save database: Saves database in C:\FCSave
    Exit: Exit "Flight Contest Manager". Can be restarted with Windows start menu command.
  Click on the taskbar icon to open the Flight Contest user interface in the browser.

Changes 3.4.3
-------------
- Online/Offline viewer in the navigation flight result has been extended by buttons "T/O-SP" and "FP-LDG".
  Zooms to the logger data of the clicked area, whereby the two points determine the resolution.
- The settings for the flight time calculation of take-offs and landings have been moved from the task settings to the wind settings.
  This makes it possible to use different values when changing the direction of the runway,
  so that deviating approach and departure distances can be better taken into account.
- Enroute observations: Selection of the observation value ??? has been added.
  This must be selected if, for example, an unknown canvas sign was entered in the answer sheet. Will be penalized with "Incorrect".
- Planning: Arrangement of the action buttons has been organized more clearly.
- Planning: The entry of navigation test details has been simplified.
- Crew print: "Sorting help" option added.
    Displays an additional column with the start no. of the other aircraft.
    Displays an additional column with the position difference to the start list of the other aircraft.
    Marks TAS with !, which are higher than the predecessor.
    Marks teams with !! that are equal to the predecessor.
    Marks teams with ! that are equal to the predecessor's predecessor.
- Bug "Gate direction at the end of the semicircle in construction mode incorrect" fixed
- Language setting for Task Creator in "Extras -> Settings" added (German, English, Spanish)

Changes 3.4.2
-------------
- When configuring the external Task Creator, additional links are displayed next to the links to the internal Task Creator.
- Internal Task Creator: 'Addons RL' and 'SUAs Url' added
- OSM Contest Map: 'Determine airspaces around the airport' added
    Airspaces below 4000ft are taken into account by default (customizable).
    'KMZ export airspaces' now also contains the names of the airspaces.
- OSM Contest Map: Airfields from OpenAIP data added
    These are displayed as in ICAO maps.
    The airfield coordinates can be retrieved with 'CSV export airports'.

Changes 3.4.1
-------------
- Internal Task Creator: Error messages when entering the task name removed
- Routes: Improved error message for semi-circle center points incl. help text
- Map generation: AirportArea name extended by route title
- Maps: Bug "Import of a map zip crashes" fixed

Changes 3.4.0
-------------
- Menu item "Maps" added
    Lists all locally saved maps of the competition.
    Commands for creating PDF and PNG files, exporting, renaming, deleting and calling up the Task Creator are available here.
    PDF maps are intended for printing.
    New maps for PDF/PNG generation or for use in the Task Creator are created with "OSM Contest Map" of a route.
    A separate map must be created for PDF/PNG or for the Task Creator.
    With "Import map", a map export can be imported on another notebook or in another competition.
- OSM Contest Map extended:
    1. Button "Generate online map around airport" added,
       which creates a map with T/O in the center with 420mm distance to the edge for OSM online map display.
    2. Button "Generate Task Creator map around airport" added,
       which creates a map with T/O in the center with 420mm distance to the edge for use in the Task Creator.
    3. Button "Generate (for Task Creator)" in the 1./2./3./4. settings,
       which creates maps with T/O and possibly LDG but without further route details for use in the Task Creator.
    4. Generated OSM maps are now always saved locally and can then be accessed via the menu item "Maps",
       where further processing commands are available.
- OSM online map: Display of locally saved maps added
    It is now possible to switch seamlessly between the online map and the local map.
    If several local maps are available, you can switch between them.
    The default map can be defined in the 'Route settings'.
    Maps created for the Task Creator cannot be displayed here because these maps use a different map projection.
- Internal Task Creator for route construction added
    Based on https://www.airrats.cl/taskcreator?lang=en by Carlos Rocca.
    Start the Task-Creator via "..." of a saved map for a new task:
      Here, the "Map Url" is preset in the Task Creator and can be loaded with "Load".
      After activating "Turn Points -> Edit", turn points can be added to the map with a double-click.
      With "Save task data" the task can be saved as a CSV file in the download folder of the computer.
    Start via menu item "Maps -> Task Creator" to load previously saved tasks:
      "Load task data" can be used to load the saved CSV file of a task. 
      A preset "Map Url" must be loaded with "Load".
      After activating "Turn Points -> Edit", further processing of the task is now possible.
      With "Export FC kml" a route can be exported, which can be used for planning and evaluation with "Routes -> Import FC route".
- Semicircle gates from circle centers are now created with a 5° course change.
    This can be changed with the route setting "Course change per semicircle gate".
- The limit on the number of route points has been lifted.
- Route coordinate: "Ignore check point" option added for scenic route legs
    This means that this coordinate is not evaluated and cannot be seen in the online/offline viewer and various input masks.
- Results: Print options for pending crew result printing via ... with one click.
    This makes it possible to print corrections more quickly if changes are made to a crew result.

Changes 3.3.6
-------------
- Bug "SCs of the curved legs in the navigation flight result are always printed for curved legs one behind the other" fixed
- Result printing: Page layout improved to reduce print pages
- OSM contest map: ANR format adapted to ANR 2.2.0
- Bug "Import FC route: Enroute photos and canvas from iSP are not imported" fixed
- OSM contest map: Village roads, forest paths, and field paths extended
- OSM contest map: Help improved

Changes 3.3.5
-------------
- Route import: Option "Read placemarks instead of path in kml/kmz file" added
  By default "On", French kml/kmz files are automatically supported.
- Bug "When exporting semicircle gates from circle centers, curved route legs are exported incorrectly" fixed

Changes 3.3.4
-------------
- OSM contest map/Generate PNG contest map with world file: added pnginfo file containing detailed information about the generated map
- Bug "Import of coordinates from kml files does not work" fixed
- Bug "Importing degrees of TO/LDG coordinates from kml files does not work" fixed

Changes 3.3.3
-------------
- Updating of the following sets of rules
    FAI Precision Flying - Edition 2023
    FAI Air Rally Flying - Edition 2023
    GAC Landing appendix - Edition 2023
    Motorflug-Wettbewerbsordnung Österreich 2023
- Minimum altitude is now initialized at 400ft (500ft - 100ft tolerance) for new routes.
    Reason: Rule 'FAI Precision Flying - Edition 2023' defines a buffer of 100ft for altitude measurement.
    
Changes 3.3.2
-------------
- Accuracy of enroute photos and canvas signs reduced (one tenth NM, whole mm)
- Contest evaluation settings: Option "Live result crew count" added.
    Defines the max. number of crews displayed in the live result display on one page.
    If the set value is exceeded (default: 30), alternating page contents will be generated.
- Contest evaluation settings: Option "Live results newest crew count" added.
    Defines the number of crews that will be displayed in the live result display as the newest completed evaluation at the top of the page.
    Default: 5 crews
- OSM Online Map: Map "TopPlusOpen" from Bundesamt für Kartographie und Geodäsie added.
- OSM Contest Map:
    Generate: Specific error message for incorrect airspaces added.
    Deactivation of entries in the airspace list added: Entries starting with # are now ignored during map generation.
- Bug "Database is emptied during Flight Contest installation by another user" fixed
  The database can be restored from the installation backup copy:
    1. Stop Flight Contest
    2. Copy C:\Program Files\Flight Contest\fc-<date>-<time>-<FC version>\fcdb.h2.db -> C:\Program Files\Flight Contest\fc
       or
       Copy C:\Program Files\Flight Contest\fc-backup-<date>-<time>-<db version>\fcdb.h2.db -> C:\Program Files\Flight Contest\fc
    3. Start Flight Contest

Changes 3.3.1.1
---------------
- OSM Online Map: Map from Michelin added

Changes 3.3.1
-------------
- OpenAIP integration for airspace display and map printing added.
    OSM Contest Map: Airspace coordinates are now retrieved from OpenAIP.
    OSM Online Map: OpenAIP layer added. Displays airspaces and airfields into the map.
    OpenAIP configuration required, see 'Help -> Configure OpenAIP for airspace display and map printing'.

- OSM Contest Map: Support for multiple airspace files added
- OSM Online Map: OpenAIP layer added. Displays airspaces and airfields into the map.

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
  -> Help -> Route planning -> Create contest route with semicircles

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
