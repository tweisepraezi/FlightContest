enum EnrouteCanvasSign // DB-2.13
{
    None ('',''),
    S01 ('S01','images/enroute/s01.jpg'),
    S02 ('S02','images/enroute/s02.jpg'),
    S03 ('S03','images/enroute/s03.jpg'),
    S04 ('S04','images/enroute/s04.jpg'),
    S05 ('S05','images/enroute/s05.jpg'),
    S06 ('S06','images/enroute/s06.jpg'),
    S07 ('S07','images/enroute/s07.jpg'),
    S08 ('S08','images/enroute/s08.jpg'),
    S09 ('S09','images/enroute/s09.jpg'),
    S10 ('S10','images/enroute/s10.jpg'),
    S11 ('S11','images/enroute/s11.jpg'),
    S12 ('S12','images/enroute/s12.jpg'),
    S13 ('S13','images/enroute/s13.jpg'),
    S14 ('S14','images/enroute/s14.jpg'),
    S15 ('S15','images/enroute/s15.jpg')
    
    EnrouteCanvasSign(String canvasName, String imageName)
    {
        this.canvasName = canvasName
        this.imageName = imageName
    }
    
    final String canvasName
    final String imageName
}