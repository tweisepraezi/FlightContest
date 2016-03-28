class LoggerResult // DB-2.12
{
    
    static belongsTo = [test:Test]
    
    static hasMany = [calcResults:CalcResult]
}
