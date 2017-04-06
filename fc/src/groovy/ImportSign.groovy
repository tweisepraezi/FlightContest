enum ImportSign
{
    None,
    RouteCoord,
    TurnpointPhoto,
    TurnpointCanvas,
    TurnpointTrueFalse,
    EnroutePhotoName,
    EnroutePhotoCoord,
    EnroutePhotoNMFromTP,
    EnroutePhotommFromTP,
    EnroutePhotoCoordmm,
    EnrouteCanvasName,
    EnrouteCanvasCoord,
    EnrouteCanvasNMFromTP,
    EnrouteCanvasmmFromTP,
    EnrouteCanvasCoordmm
    
    static Map GetRouteCoordData(Route routeInstance)
    {
        ImportSign import_sign = ImportSign.RouteCoord
        String line_content = "TO, ${GetImportTxtLineContent(routeInstance.contest)}, [Gate 240${RouteFileTools.UNIT_GRAD} 0.02${RouteFileTools.UNIT_NM},] [Dist 93.0${RouteFileTools.UNIT_mm},] [Track 001.00${RouteFileTools.UNIT_GRAD},] [Duration 2${RouteFileTools.UNIT_min},] [notime,] [nogate,] [noplan,] [endcurved]"
        return [importsign:import_sign, linecontent:line_content]
    }
    
    private static ImportSign GetTurnpointSign(Route routeInstance)
    {
        switch (routeInstance.turnpointRoute) {
            case TurnpointRoute.AssignPhoto:
                return ImportSign.TurnpointPhoto
            case TurnpointRoute.AssignCanvas:
                return ImportSign.TurnpointCanvas
            case TurnpointRoute.TrueFalsePhoto:
                return ImportSign.TurnpointTrueFalse
        }
        return ImportSign.None
    }
    
    static Map GetTurnpointSignData(Route routeInstance)
    {
        ImportSign import_sign = ImportSign.GetTurnpointSign(routeInstance)
        String line_content = "-"
        String title_code = ""
        
        switch (import_sign) {
            case ImportSign.TurnpointPhoto:
                line_content = "TP1, B"
                title_code = "fc.coordroute.turnpointphoto.import"
                break
            case ImportSign.TurnpointCanvas:
                line_content = "TP1, A"
                title_code = "fc.coordroute.turnpointcanvas.import"
                break
            case ImportSign.TurnpointTrueFalse:
                line_content = "TP1, yes"
                title_code = "fc.coordroute.turnpointtruefalse.import"
                break
        }
        return [importsign:import_sign, linecontent:line_content, titlecode:title_code]
    }
    
    private static ImportSign GetEnrouteSign(Route routeInstance, boolean enroutePhoto)
    {
        if (enroutePhoto) {
            switch (routeInstance.enroutePhotoRoute) {
                case EnrouteRoute.InputName:
                    return ImportSign.EnroutePhotoName
                case EnrouteRoute.InputCoord:
                    return ImportSign.EnroutePhotoCoord
                case EnrouteRoute.InputNMFromTP:
                    return ImportSign.EnroutePhotoNMFromTP
                case EnrouteRoute.InputmmFromTP:
                    return ImportSign.EnroutePhotommFromTP
                case EnrouteRoute.InputCoordmm:
                    return ImportSign.EnroutePhotoCoordmm
            }
        } else {
            switch (routeInstance.enrouteCanvasRoute) {
                case EnrouteRoute.InputName:
                    return ImportSign.EnrouteCanvasName
                case EnrouteRoute.InputCoord:
                    return ImportSign.EnrouteCanvasCoord
                case EnrouteRoute.InputNMFromTP:
                    return ImportSign.EnrouteCanvasNMFromTP
                case EnrouteRoute.InputmmFromTP:
                    return ImportSign.EnrouteCanvasmmFromTP
                case EnrouteRoute.InputCoordmm:
                    return ImportSign.EnrouteCanvasCoordmm
            }
        }
        return ImportSign.None
    }
    
    static String GetImportTxtLineContent(Contest contestInstance)
    {
        Coord coord_demo = new Coord()
        coord_demo.latGrad = 52
        coord_demo.latMinute = 12.1
        coord_demo.latDirection = CoordPresentation.NORTH
        coord_demo.lonGrad = 16
        coord_demo.lonMinute = 45.9
        coord_demo.lonDirection = CoordPresentation.EAST
        String coord_demo_lat = contestInstance.coordPresentation.GetCoordName(coord_demo, true).replaceAll(',', '.')
        String coord_demo_lon = contestInstance.coordPresentation.GetCoordName(coord_demo, false).replaceAll(',', '.')
        return "${coord_demo_lat}, ${coord_demo_lon}, ${RouteFileTools.ALT} 1243${RouteFileTools.UNIT_ft}"
    }
    
    static Map GetEnrouteSignData(Route routeInstance, boolean enroutePhoto)
    {
        ImportSign import_sign = ImportSign.GetEnrouteSign(routeInstance, enroutePhoto)
        String line_content = "-"
        
        Coord coord_demo = new Coord()
        coord_demo.latGrad = 52
        coord_demo.latMinute = 12.1
        coord_demo.latDirection = CoordPresentation.NORTH
        coord_demo.lonGrad = 16
        coord_demo.lonMinute = 45.9
        coord_demo.lonDirection = CoordPresentation.EAST
        String coord_demo_lat = routeInstance.contest.coordPresentation.GetCoordName(coord_demo, true).replaceAll(',', '.')
        String coord_demo_lon = routeInstance.contest.coordPresentation.GetCoordName(coord_demo, false).replaceAll(',', '.')
        
        switch (import_sign) {
            case ImportSign.TurnpointPhoto:
                line_content = "TP1, Z"
                break
            case ImportSign.TurnpointCanvas:
                line_content = "TP1, A"
                break
            case ImportSign.TurnpointTrueFalse:
                line_content = "TP1, yes"
                break
            case ImportSign.EnroutePhotoName:
                line_content = "1"
                break
            case ImportSign.EnroutePhotoCoord:
                line_content = "1, ${coord_demo_lat}, ${coord_demo_lon}"
                break
            case ImportSign.EnroutePhotoNMFromTP:
                line_content = "1, TP1, 12.43NM"
                break
            case ImportSign.EnroutePhotommFromTP:
                line_content = "1, TP1, 109.39mm"
                break
            case ImportSign.EnroutePhotoCoordmm:
                line_content = "1, ${coord_demo_lat}, ${coord_demo_lon}, 109.39mm"
                break
            case ImportSign.EnrouteCanvasName:
                line_content = "S01"
                break
            case ImportSign.EnrouteCanvasCoord:
                line_content = "S01, ${coord_demo_lat}, ${coord_demo_lon}"
                break
            case ImportSign.EnrouteCanvasNMFromTP:
                line_content = "S01, TP1, 2.00NM"
                break
            case ImportSign.EnrouteCanvasmmFromTP:
                line_content = "S01, SP, 18.52mm"
                break
            case ImportSign.EnrouteCanvasCoordmm:
                line_content = "S01, ${coord_demo_lat}, ${coord_demo_lon}, 109.39mm"
                break
        }
        return [importsign:import_sign, linecontent:line_content]
    }
    
    Map GetLineValues(String line)
    {
        Map ret = [value_num_ok:false, name:'', tpname:'', tpsign:'', tpcorrect:'', lat:'', lon:'', alt:'', nm:'', mm:'', other:[]]
        def line_values = line.split(',')
        switch (this) {
            case ImportSign.RouteCoord:
                if (line_values.size() >= 4) {
                    ret.value_num_ok = true
                    ret.tpname = line_values[0].trim()
                    ret.lat = line_values[1].trim()
                    ret.lon = line_values[2].trim()
                    ret.alt = line_values[3].trim()
                    int i = 0
                    for (l in line_values) {
                        i++
                        if (i > 4) {
                            String s = l.trim()
                            while (s.contains('  ')) {
                                s = s.replaceAll('  ', ' ')
                            }
                            ret.other += s
                        }
                    }
                }
                break
            case ImportSign.TurnpointPhoto:
                if (line_values.size() == 2) {
                    ret.value_num_ok = true
                    ret.tpname = line_values[0].trim()
                    ret.tpsign = line_values[1].trim()
                } 
                break
            case ImportSign.TurnpointCanvas:
                if (line_values.size() == 2) {
                    ret.value_num_ok = true
                    ret.tpname = line_values[0].trim()
                    ret.tpsign = line_values[1].trim()
                }
                break
            case ImportSign.TurnpointTrueFalse:
                if (line_values.size() == 2) {
                    ret.value_num_ok = true
                    ret.tpname = line_values[0].trim()
                    ret.tpcorrect = line_values[1].trim()
                }
                break
            case ImportSign.EnroutePhotoName:
                if (line_values.size() == 1) {
                    ret.value_num_ok = true
                    ret.name = line_values[0].trim()
                }
                break
            case ImportSign.EnroutePhotoCoord:
                if (line_values.size() == 3) {
                    ret.value_num_ok = true
                    ret.name = line_values[0].trim()
                    ret.lat = line_values[1].trim()
                    ret.lon = line_values[2].trim()
                }
                break
            case ImportSign.EnroutePhotoNMFromTP:
                if (line_values.size() == 3) {
                    ret.value_num_ok = true
                    ret.name = line_values[0].trim()
                    ret.tpname = line_values[1].trim()
                    ret.nm = line_values[2].trim()
                }
                break
            case ImportSign.EnroutePhotommFromTP:
                if (line_values.size() == 3) {
                    ret.value_num_ok = true
                    ret.name = line_values[0].trim()
                    ret.tpname = line_values[1].trim()
                    ret.mm = line_values[2].trim()
                }
                break
            case ImportSign.EnroutePhotoCoordmm:
                if (line_values.size() == 4) {
                    ret.value_num_ok = true
                    ret.name = line_values[0].trim()
                    ret.lat = line_values[1].trim()
                    ret.lon = line_values[2].trim()
                    ret.mm = line_values[3].trim()
                }
                break
            case ImportSign.EnrouteCanvasName:
                if (line_values.size() == 1) {
                    ret.value_num_ok = true
                    ret.name = line_values[0].trim()
                }
                break
            case ImportSign.EnrouteCanvasCoord:
                if (line_values.size() == 3) {
                    ret.value_num_ok = true
                    ret.name = line_values[0].trim()
                    ret.lat = line_values[1].trim()
                    ret.lon = line_values[2].trim()
                }
                break
            case ImportSign.EnrouteCanvasNMFromTP:
                if (line_values.size() == 3) {
                    ret.value_num_ok = true
                    ret.name = line_values[0].trim()
                    ret.tpname = line_values[1].trim()
                    ret.nm = line_values[2].trim()
                }
                break
            case ImportSign.EnrouteCanvasmmFromTP:
                if (line_values.size() == 3) {
                    ret.value_num_ok = true
                    ret.name = line_values[0].trim()
                    ret.tpname = line_values[1].trim()
                    ret.mm = line_values[2].trim()
                }
                break
            case ImportSign.EnrouteCanvasCoordmm:
                if (line_values.size() == 4) {
                    ret.value_num_ok = true
                    ret.name = line_values[0].trim()
                    ret.lat = line_values[1].trim()
                    ret.lon = line_values[2].trim()
                    ret.mm = line_values[3].trim()
                }
                break
        }
        return ret
    }
    
    boolean IsTurnpoint()
    {
        switch (this) {
            case ImportSign.TurnpointPhoto:
            case ImportSign.TurnpointCanvas:
            case ImportSign.TurnpointTrueFalse:
                return true
        }
        return false
    }
    
    boolean IsEnroutePhoto()
    {
        switch (this) {
            case ImportSign.EnroutePhotoName:
            case ImportSign.EnroutePhotoCoord:
            case ImportSign.EnroutePhotoNMFromTP:
            case ImportSign.EnroutePhotommFromTP:
            case ImportSign.EnroutePhotoCoordmm:
                return true
        }
        return false
    }

    boolean IsEnrouteCanvas()
    {
        switch (this) {
            case ImportSign.EnrouteCanvasName:
            case ImportSign.EnrouteCanvasCoord:
            case ImportSign.EnrouteCanvasNMFromTP:
            case ImportSign.EnrouteCanvasmmFromTP:
            case ImportSign.EnrouteCanvasCoordmm:
                return true
        }
        return false
    }
}
