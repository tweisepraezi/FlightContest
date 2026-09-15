class EnrouteCanvasData extends EnrouteData // DB-2.13
{
    static belongsTo = [test:Test]
    
    String GetUniqueCanvasName(int canvasPos)
    {
        return "${canvasSign.canvasName}_${canvasPos}"
    }
    
}
