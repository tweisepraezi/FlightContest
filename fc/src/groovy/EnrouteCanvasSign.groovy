enum EnrouteCanvasSign // DB-2.13
{
    None ('',''),
    S01 ('S01','GM_Utils/Icons/s01.png'),
    S02 ('S02','GM_Utils/Icons/s02.png'),
    S03 ('S03','GM_Utils/Icons/s03.png'),
    S04 ('S04','GM_Utils/Icons/s04.png'),
    S05 ('S05','GM_Utils/Icons/s05.png'),
    S06 ('S06','GM_Utils/Icons/s06.png'),
    S07 ('S07','GM_Utils/Icons/s07.png'),
    S08 ('S08','GM_Utils/Icons/s08.png'),
    S09 ('S09','GM_Utils/Icons/s09.png'),
    S10 ('S10','GM_Utils/Icons/s10.png'),
    S11 ('S11','GM_Utils/Icons/s11.png'),
    S12 ('S12','GM_Utils/Icons/s12.png'),
    S13 ('S13','GM_Utils/Icons/s13.png'),
    S14 ('S14','GM_Utils/Icons/s14.png'),
    S15 ('S15','GM_Utils/Icons/s15.png')
    
    EnrouteCanvasSign(String canvasName, String imageName)
    {
        this.canvasName = canvasName
        this.imageName = imageName
    }
    
    final String canvasName
    final String imageName
}