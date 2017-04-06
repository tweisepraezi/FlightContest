class EnrouteData // DB-2.13
{
    String photoName = ""
    EnrouteCanvasSign canvasSign = EnrouteCanvasSign.None
    BigDecimal distanceNM = 0.0  
    BigDecimal distancemm = 0.0
    CoordType tpType = CoordType.TO
    int tpNumber = 1
    CoordType evaluationType = CoordType.UNKNOWN
    int evaluationNumber = 0       // CoordType.UNKNOWN: 0 - Unevaluated, 1 - NotFound
    EvaluationValue evaluationValue = EvaluationValue.Unevaluated
    BigDecimal evaluationDistance = 0
    EvaluationValue resultValue = EvaluationValue.Unevaluated
    int penaltyCoord = 0           // Points
    Route route = null
    
	static belongsTo = [test:Test]

	static constraints = {
		photoName()
		canvasSign()
        distanceNM()
        distancemm()
        tpType()
        tpNumber()
        evaluationType()
        evaluationNumber()
		evaluationValue()
        evaluationDistance()
        resultValue()
        penaltyCoord()
        route()
	}
    
    String tpTitle()
    {
        switch (tpType) {
            case CoordType.TP:
            case CoordType.SECRET:
                return "${tpType.title}${tpNumber}"
            default:
                return tpType.title
        }
    }
    
    String tpName()
    {
        switch (tpType) {
            case CoordType.TP:
            case CoordType.SECRET:
                return "${getMsg(tpType.code)}${tpNumber}"
            default:
                return getMsg(tpType.code)
        }
    }
    
    String tpPrintName()
    {
        switch (tpType) {
            case CoordType.TP:
            case CoordType.SECRET:
                return "${getPrintMsg(tpType.code)}${tpNumber}"
            default:
                return getPrintMsg(tpType.code)
        }
    }
    
    String evaluationName()
    {
        switch (evaluationType) {
            case CoordType.TP:
            case CoordType.SECRET:
                return "${getMsg(evaluationType.code)}${evaluationNumber}"
            default:
                return getMsg(evaluationType.code)
        }
    }
    
    String evaluationPrintName()
    {
        switch (evaluationType) {
            case CoordType.TP:
            case CoordType.SECRET:
                return "${getPrintMsg(evaluationType.code)}${evaluationNumber}"
            default:
                return getPrintMsg(evaluationType.code)
        }
    }
    
    boolean IsEvaluationFromTPUnevaluated()
    {
        if ((evaluationType == CoordType.UNKNOWN) && (evaluationNumber == 0)) {
            return true
        }
        return false
    }
    
    boolean IsEvaluationFromTPNotFound()
    {
        if ((evaluationType == CoordType.UNKNOWN) && (evaluationNumber == 1)) {
            return true
        }
        return false
    }
}
