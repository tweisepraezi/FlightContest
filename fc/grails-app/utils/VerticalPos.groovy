import java.util.List;

enum VerticalPos // saved in database. do not modify.
{
    None ('fc.pos.none'),
    Top ('fc.pos.top'),
    Center ('fc.pos.center'),
    Bottom ('fc.pos.bottom')
    
    VerticalPos(String code)
    {
        this.code = code
    }
    
    static List GetValues()
    {
        List l = []
        l += VerticalPos.Top
        l += VerticalPos.Center
        l += VerticalPos.Bottom
        return l
    }
    
    final String code
}
