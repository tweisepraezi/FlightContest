enum ObservationPrintStyle  // DB-2.28
{
    Portrait2x4  ('fc.observation.printstyle.portrait2x4',  2, 4, 300, 220, false, false),
    Landscape3x3 ('fc.observation.printstyle.landscape3x3', 3, 3, 270, 200, false, true),
    Portrait4x8  ('fc.observation.printstyle.portrait3x6',  3, 6, 300, 220, true,  false),
    Landscape6x6 ('fc.observation.printstyle.landscape5x4', 5, 4, 270, 200, true,  true)
    
    ObservationPrintStyle(String code, int columns, int rows, int width, int height, boolean a3, boolean landscape)
    {
        this.code = code
        this.columns = columns
        this.rows = rows
        this.width = width
        this.height = height
        this.a3 = a3
        this.landscape = landscape
    }
    
    final String code
    final int columns
    final int rows
    final int width
    final int height
    final boolean a3
    final boolean landscape
}