import java.util.List;

enum EvaluationValue // DB-2.13
{
    Unevaluated ('fc.observation.evaluationvalue.enroute.noinput',  'fc.observation.resultvalue.enroute.unevaluated', 'fc.observation.evaluationvalue.turnpoint.noinput',  'fc.observation.resultvalue.turnpoint.unevaluated'),
    Correct     ('fc.observation.evaluationvalue.enroute.correct',  'fc.observation.resultvalue.enroute.correct',     'fc.observation.evaluationvalue.turnpoint.true',     'fc.observation.resultvalue.turnpoint.true'),
    Inexact     ('fc.observation.evaluationvalue.enroute.inexact',  'fc.observation.resultvalue.enroute.inexact',     'fc.observation.evaluationvalue.turnpoint.inexact',  'fc.observation.resultvalue.turnpoint.inexact'),
    NotFound    ('fc.observation.evaluationvalue.enroute.notfound', 'fc.observation.resultvalue.enroute.notfound',    'fc.observation.evaluationvalue.turnpoint.notfound', 'fc.observation.resultvalue.turnpoint.notfound'),
    False       ('fc.observation.evaluationvalue.enroute.false',    'fc.observation.resultvalue.enroute.false',       'fc.observation.evaluationvalue.turnpoint.false',    'fc.observation.resultvalue.turnpoint.false')
    
    EvaluationValue(String enrouteEvaluationCode, String enrouteResultCode, String turnpointEvaluationCode, String turnpointResultCode)
    {
        this.enrouteEvaluationCode = enrouteEvaluationCode
        this.enrouteResultCode = enrouteResultCode
        this.turnpointEvaluationCode = turnpointEvaluationCode
        this.turnpointResultCode = turnpointResultCode
    }
    
    final String enrouteEvaluationCode
    final String enrouteResultCode
    final String turnpointEvaluationCode
    final String turnpointResultCode
    
    static List GetEvaluationValues(boolean showInexact)
    {
        List ret = []
        ret += EvaluationValue.Correct
        if (showInexact) {
            ret += EvaluationValue.Inexact
        }
        ret += EvaluationValue.False
        ret += EvaluationValue.NotFound
        return ret
    }

}