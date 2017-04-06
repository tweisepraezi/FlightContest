class TurnpointData // DB-2.13
{
    CoordType tpType = CoordType.TO
    int tpNumber = 1
    TurnpointSign tpSign = TurnpointSign.None
    TurnpointCorrect tpSignCorrect = TurnpointCorrect.Unassigned
    TurnpointSign evaluationSign = TurnpointSign.Unevaluated
    EvaluationValue evaluationValue = EvaluationValue.Unevaluated
    EvaluationValue resultValue = EvaluationValue.Unevaluated
    int penaltyCoord = 0           // Points
    Route route = null
    
	static belongsTo = [test:Test]

	static constraints = {
        tpType()
        tpNumber()
        tpSign()
        tpSignCorrect()
		evaluationSign()
		evaluationValue()
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
    
}
