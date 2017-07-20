enum TurnpointSign // DB-2.13
{
    Unevaluated ('', true,''),
    None ('-', true,''),
    A ('A', true, 'images/turnpoint/a.jpg'),
    B ('B', false,''),
    C ('C', true, 'images/turnpoint/c.jpg'),
    D ('D', false,''),
    E ('E', true, 'images/turnpoint/e.jpg'),
    F ('F', true, 'images/turnpoint/f.jpg'),
    G ('G', true, 'images/turnpoint/g.jpg'),
    H ('H', false,''),
    I ('I', true, 'images/turnpoint/i.jpg'),
    J ('J', false,''),
    K ('K', true, 'images/turnpoint/k.jpg'),
    L ('L', true, 'images/turnpoint/l.jpg'),
    M ('M', false,''),
    N ('N', false,''),
    O ('O', true, 'images/turnpoint/o.jpg'),
    P ('P', true, 'images/turnpoint/p.jpg'),
    Q ('Q', false,''),
    R ('R', true, 'images/turnpoint/r.jpg'),
    S ('S', true, 'images/turnpoint/s.jpg'),
    T ('T', false,''),
    U ('U', false,''),
    V ('V', false,''),
    W ('W', false,''),
    X ('X', false,''),
    Y ('Y', false,''),
    Z ('Z', false,''),
    NoSign ('*', true, ''),
    N1 ('1', false, ''),
    N2 ('2', false, ''),
    N3 ('3', false, ''),
    N4 ('4', false, ''),
    N5 ('5', false, ''),
    N6 ('6', false, ''),
    N7 ('7', false, ''),
    N8 ('8', false, ''),
    N9 ('9', false, ''),
    N10 ('10', false, ''),
    N11 ('11', false, ''),
    N12 ('12', false, ''),
    N13 ('13', false, ''),
    N14 ('14', false, ''),
    N15 ('15', false, ''),
    N16 ('16', false, ''),
    N17 ('17', false, ''),
    N18 ('18', false, ''),
    N19 ('19', false, ''),
    N20 ('20', false, '')
    
    TurnpointSign(String title, boolean canvas, String imageName)
    {
        this.title = title
        this.canvas = canvas
        this.imageName = imageName
    }
    
    final String title
    final boolean canvas
    final String imageName
    
    static List GetTurnpointSigns(boolean isCanvas)
    {
        List ret = []
        for (def v in values()) {
            if (v == v.Unevaluated) {
                // nothing
            } else if (v == v.None) {
                ret += v
            } else if (!isCanvas) {
                ret += v
            } else if (v.canvas) {
                ret += v
            }
        }
        return ret
    }
    
    static List GetEvaluationSigns(boolean isCanvas)
    {
        List ret = []
        for (def v in values()) {
            if (v == v.NoSign) {
                // Nothing
            } else if (v == v.Unevaluated) {
                ret += v
            } else if (v == v.None) {
                ret += v
            } else if (!isCanvas) {
                ret += v
            } else if (v.canvas) {
                ret += v
            }
        }
        return ret
    }
    
    static TurnpointSign GetTurnpointSign(String title)
    {
        for (def v in values()) {
            if (v.title == title) {
                return v
            }
        }
        return TurnpointSign.Unevaluated
    }
}