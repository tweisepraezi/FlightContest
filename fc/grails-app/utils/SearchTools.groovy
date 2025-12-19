class SearchTools {

    // --------------------------------------------------------------------------------------------------------------------
    static List GetRouteTasks(Route routeInstance) {
        // TODOIF: look in Route.Used() for extensions
        List task_list = []
        for (FlightTest flighttest_instance in FlightTest.findAllByRoute(routeInstance,[sort:"id"])) {
            if (!(flighttest_instance.task in task_list)) {
                task_list += flighttest_instance.task
            }
        }
        for (FlightTestWind flighttestwind_instance in FlightTestWind.findAllByCorridorRouteID(routeInstance.id,[sort:"id"])) {
            if (!(flighttestwind_instance.flighttest.task in task_list)) {
                task_list += flighttestwind_instance.flighttest.task
            }
        }
        for (PlanningTestTask planningtesttask_instance in PlanningTestTask.findAllByRoute(routeInstance,[sort:"id"])) {
            if (!(planningtesttask_instance.planningtest.task in task_list)) {
                task_list += planningtesttask_instance.planningtest.task
            }
        }
		task_list = task_list.asImmutable().toSorted { a, b -> a.idTitle <=> b.idTitle }
        return task_list
    }
}