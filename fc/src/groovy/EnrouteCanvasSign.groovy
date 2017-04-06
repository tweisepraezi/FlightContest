enum EnrouteCanvasSign // DB-2.13
{
    None ('',''),
    S01 ('S01','/fc/images/enroute/s01.jpg'),
    S02 ('S02','/fc/images/enroute/s02.jpg'),
    S03 ('S03','/fc/images/enroute/s03.jpg'),
    S04 ('S04','/fc/images/enroute/s04.jpg'),
    S05 ('S05','/fc/images/enroute/s05.jpg'),
    S06 ('S06','/fc/images/enroute/s06.jpg'),
    S07 ('S07','/fc/images/enroute/s07.jpg'),
    S08 ('S08','/fc/images/enroute/s08.jpg'),
    S09 ('S09','/fc/images/enroute/s09.jpg'),
    S10 ('S10','/fc/images/enroute/s10.jpg'),
    S11 ('S11','/fc/images/enroute/s11.jpg'),
    S12 ('S12','/fc/images/enroute/s12.jpg'),
    S13 ('S13','/fc/images/enroute/s13.jpg'),
    S14 ('S14','/fc/images/enroute/s14.jpg'),
    S15 ('S15','/fc/images/enroute/s15.jpg')
    
    EnrouteCanvasSign(String canvasName, String imageName)
    {
        this.canvasName = canvasName
        this.imageName = imageName
    }
    
    final String canvasName
    final String imageName
}