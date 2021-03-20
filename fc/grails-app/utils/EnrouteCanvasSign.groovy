enum EnrouteCanvasSign // DB-2.13
{
    None ('','','','',''),
    S01 ('S01','GM_Utils/Icons/s01.png','s01.png','images/enroute/s01.jpg','s01.jpg'),
    S02 ('S02','GM_Utils/Icons/s02.png','s02.png','images/enroute/s02.jpg','s02.jpg'),
    S03 ('S03','GM_Utils/Icons/s03.png','s03.png','images/enroute/s03.jpg','s03.jpg'),
    S04 ('S04','GM_Utils/Icons/s04.png','s04.png','images/enroute/s04.jpg','s04.jpg'),
    S05 ('S05','GM_Utils/Icons/s05.png','s05.png','images/enroute/s05.jpg','s05.jpg'),
    S06 ('S06','GM_Utils/Icons/s06.png','s06.png','images/enroute/s06.jpg','s06.jpg'),
    S07 ('S07','GM_Utils/Icons/s07.png','s07.png','images/enroute/s07.jpg','s07.jpg'),
    S08 ('S08','GM_Utils/Icons/s08.png','s08.png','images/enroute/s08.jpg','s08.jpg'),
    S09 ('S09','GM_Utils/Icons/s09.png','s09.png','images/enroute/s09.jpg','s09.jpg'),
    S10 ('S10','GM_Utils/Icons/s10.png','s10.png','images/enroute/s10.jpg','s10.jpg'),
    S11 ('S11','GM_Utils/Icons/s11.png','s11.png','images/enroute/s11.jpg','s11.jpg'),
    S12 ('S12','GM_Utils/Icons/s12.png','s12.png','images/enroute/s12.jpg','s12.jpg'),
    S13 ('S13','GM_Utils/Icons/s13.png','s13.png','images/enroute/s13.jpg','s13.jpg'),
    S14 ('S14','GM_Utils/Icons/s14.png','s14.png','images/enroute/s14.jpg','s14.jpg'),
    S15 ('S15','GM_Utils/Icons/s15.png','s15.png','images/enroute/s15.jpg','s15.jpg')
    
    EnrouteCanvasSign(String canvasName, String imageName, String imageShortName, String imageJpgName, String imageJpgShortName)
    {
        this.canvasName = canvasName
        this.imageName = imageName
        this.imageShortName = imageShortName
        this.imageJpgName = imageJpgName
        this.imageJpgShortName = imageJpgShortName
    }
    
    final String canvasName
    final String imageName
    final String imageShortName
    final String imageJpgName
    final String imageJpgShortName
}