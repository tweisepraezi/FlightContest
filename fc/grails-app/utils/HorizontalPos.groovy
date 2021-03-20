enum HorizontalPos // saved in database. do not modify.
{
    None ('fc.pos.none'),
    Left ('fc.pos.left'),
    Center ('fc.pos.center'),
    Right ('fc.pos.right')
    
    HorizontalPos(String code)
    {
        this.code = code
    }
    
    static List GetValues()
    {
        List l = []
        l += HorizontalPos.Left
        l += HorizontalPos.Center
        l += HorizontalPos.Right
        return l
    }
    
    final String code
}
