class PlanningTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def parcourOverview = { attrs, body ->
        List task_list = []
        for (Task task_instance in Task.findAllByContest(attrs.t.contest,[sort:'idTitle'])) {
            if (task_instance?.flighttest?.route?.id == attrs.t?.flighttest?.route?.id) { // same parcour
                task_list += task_instance
            }
        }
        
        outln"""<table>"""
        outln"""    <thead>"""
        outln"""        <tr>"""
        outln"""            <th>${message(code:'fc.number')}</th>"""
        outln"""            <th>${message(code:'fc.crew')}</th>"""
        outln"""            <th>${message(code:'fc.aircraft')}</th>"""
        for (Task task_instance in task_list) {
            outln"""        <th>${task_instance.name()}</th>"""
        }
        outln"""        </tr>"""
        outln"""    </thead>"""
        outln"""    <tbody>"""
        int i = 0
        for (Test test_instance in Test.findAllByTask(attrs.t,[sort:'viewpos'])) {
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                outln"""<tr class="${(i % 2) == 0 ? 'odd' : ''}">"""
                outln"""    <td style="white-space:nowrap;">${test_instance.crew.startNum}</td>"""
                outln"""    <td style="white-space:nowrap;">${test_instance.crew.name}</td>"""
                if (test_instance.taskAircraft) {
                    outln"""<td style="white-space:nowrap;">${test_instance.taskAircraft.registration}</td>"""
                } else {
                    outln"""<td style="white-space:nowrap;">-</td>"""
                }
                List route_list = []
                for (Task task_instance in task_list) {
                    Test test_instance2 = Test.findByTaskAndCrew(task_instance, test_instance.crew)
                    if (test_instance2 && !test_instance2.disabledCrew && !test_instance2.crew.disabled) {
                        Route route_instance2 = test_instance2.flighttestwind?.GetRoute()
                        if (route_instance2) {
                            String route_class = ""
                            if (route_instance2.id in route_list) {
                                route_class = "errors"
                            }
                            String s = """<td class="${route_class}" style="white-space:nowrap;">"""
                            s += route_instance2.name()
                            if (route_instance2.id in route_list) {
                                s += " !"
                            }
                            s += """</td>"""
                            outln s
                            route_list += route_instance2.id
                        } else {
                            outln"""<td style="white-space:nowrap;">-</td>"""
                        }
                    } else {
                        outln"""<td style="white-space:nowrap;">-</td>"""
                    }
                }
                outln"""</tr>"""
                i++
            }
        }
        outln"""    </tbody>"""
        outln"""</table>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
