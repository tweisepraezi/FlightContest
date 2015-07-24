class AflosTools
{
    static AflosRouteNames GetAflosRouteName(Contest contestInstance, String routeName)
    {
        AflosRouteNames aflosroutenames_instance = null
        if (contestInstance.aflosTest) {
            aflosroutenames_instance = AflosRouteNames.aflostest.findByName(routeName)
        } else if (contestInstance.aflosUpload) {
            aflosroutenames_instance = AflosRouteNames.aflosupload.findByName(routeName)
        } else {
            aflosroutenames_instance = AflosRouteNames.aflos.findByName(routeName)
        }
        return aflosroutenames_instance
    }
    
    static AflosCrewNames GetAflosCrewName(Contest contestInstance, int startNum)
    {
        AflosCrewNames afloscrewnames_instance = null
        if (contestInstance.aflosTest) {
            afloscrewnames_instance = AflosCrewNames.aflostest.findByStartnumAndPointsNotEqual(startNum,0)
        } else if (contestInstance.aflosUpload) {
            afloscrewnames_instance = AflosCrewNames.aflosupload.findByStartnumAndPointsNotEqual(startNum,0)
        } else {
            afloscrewnames_instance = AflosCrewNames.aflos.findByStartnumAndPointsNotEqual(startNum,0)
        }
        return afloscrewnames_instance
    }
    
    static List GetAflosCheckPoints(Contest contestInstance, String routeName, int startNum)
    {
        List afloscheckpoints_instances = null
        AflosRouteNames aflosroutenames_instance = AflosTools.GetAflosRouteName(contestInstance,routeName)
        if (aflosroutenames_instance) {
            if (contestInstance.aflosTest) {
                afloscheckpoints_instances = AflosCheckPoints.aflostest.findAllByStartnumAndRoutename(startNum,aflosroutenames_instance,[sort:"id"])
            } else if (contestInstance.aflosUpload) {
                afloscheckpoints_instances = AflosCheckPoints.aflosupload.findAllByStartnumAndRoutename(startNum,aflosroutenames_instance,[sort:"id"])
            } else {
                afloscheckpoints_instances = AflosCheckPoints.aflos.findAllByStartnumAndRoutename(startNum,aflosroutenames_instance,[sort:"id"])
            }
        }
        return afloscheckpoints_instances
    }
}
