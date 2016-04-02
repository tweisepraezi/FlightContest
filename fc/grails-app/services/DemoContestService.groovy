class DemoContestService 
{
	def fcService
	def demoContestStandardService
	def demoContestOtherService
	def demoContestRoutesService
	def demoContestIntermediateService
	def demoContestCurvedService
	def demoContestProcedureTurnService
	
	Map CreateTest(String demoContest, boolean aflosDB, String showLanguage)
	{
		Map ret = [:]
		switch (demoContest) {
			case '98':
                ret = create_test( "2",ret,true,aflosDB,showLanguage)
                long contest_id = ret.contestid
				ret = create_test( "1",ret,true,aflosDB,showLanguage)
				ret = create_test( "3",ret,true,aflosDB,showLanguage)
				ret = create_test("11",ret,true,aflosDB,showLanguage)
				ret = create_test("12",ret,true,aflosDB,showLanguage)
				ret = create_test("13",ret,true,aflosDB,showLanguage)
				ret = create_test("14",ret,true,aflosDB,showLanguage)
				ret = create_test("21",ret,true,aflosDB,showLanguage)
				ret = create_test("22",ret,true,aflosDB,showLanguage)
				ret = create_test("23",ret,true,aflosDB,showLanguage)
				ret = create_test("24",ret,true,aflosDB,showLanguage)
                ret.contestid = contest_id
				break
			case '99':
                ret = create_test( "2",ret,true,aflosDB,showLanguage)
                long contest_id = ret.contestid
				ret = create_test( "1",ret,true,aflosDB,showLanguage)
				ret = create_test("21",ret,true,aflosDB,showLanguage)
				ret = create_test("22",ret,true,aflosDB,showLanguage)
				ret = create_test("23",ret,true,aflosDB,showLanguage)
				ret = create_test("24",ret,true,aflosDB,showLanguage)
                ret.contestid = contest_id
				break
			default:
				ret = create_test(demoContest,true,aflosDB,showLanguage)
				break
		}
		return ret
	}
	
	Map create_test(String demoContest, Map retValue, boolean runTest, boolean aflosDB, String showLanguage)
	{
		Map ret = create_test(demoContest, runTest, aflosDB, showLanguage)
		if (ret.error) {
			retValue.error = true
		}
		if (ret.message) {
			if (retValue.message) {
				retValue.message += ", "
				retValue.message += ret.message
			} else {
				retValue.message = ret.message
			}
		}
		retValue.contestid = ret.contestid
		return retValue
	}
	
	Map create_test(String demoContest, boolean runTest, boolean aflosDB, String showLanguage)
	{
		Map ret = [:]
		long contest_id = 0
		switch (demoContest) {
			case '1': 
		        contest_id = demoContestStandardService.CreateTest1("Demo Wettbewerb", "demo1", true, aflosDB)
				ret = run_test(contest_id, runTest, aflosDB)
				break
			case '2':
				contest_id = demoContestStandardService.CreateTest2("Demo Wettbewerb (mit Klassen)", "demo2", true, aflosDB)
				ret = run_test(contest_id, runTest, aflosDB)
				break
			case '3':
				contest_id = demoContestStandardService.CreateTest3("Demo Wettbewerb (kombinierter Wettbewerb)", "demo3", false, aflosDB)
				break
			case '11':
				contest_id = demoContestOtherService.CreateTest11("Demo Wettbewerb Auswertung ohne Klassen", "demo11", false, aflosDB)
				break
			case '12':
				contest_id = demoContestOtherService.CreateTest12("Demo Wettbewerb Auswertung mit Klassen", "demo12", false, aflosDB)
				break
			case '13':
		        contest_id = demoContestOtherService.CreateTest13("Demo Wettbewerb (100 Besatzungen)", "demo13", false, aflosDB)
				break
			case '14':
		        contest_id = demoContestOtherService.CreateTest14("Demo Wettbewerb (20 Besatzungen)", "demo14", false, aflosDB)
				break
			case '21':
		        contest_id = demoContestRoutesService.CreateTest("Demo Wettbewerb (Strecken)", "demo21", true, aflosDB, showLanguage)
				ret = run_test(contest_id, runTest, aflosDB)
				break
			case '22':
		        contest_id = demoContestIntermediateService.CreateTest("Demo Wettbewerb (Intermediate)", "demo22", true, aflosDB)
				ret = run_test(contest_id, runTest, aflosDB)
				break
			case '23':
		        contest_id = demoContestCurvedService.CreateTest("Demo Wettbewerb (Curved)", "demo23", true, aflosDB)
				ret = run_test(contest_id, runTest, aflosDB)
				break
			case '24':
		        contest_id = demoContestProcedureTurnService.CreateTest("Demo Wettbewerb (Procedure Turn)", "demo24", true, aflosDB)
				ret = run_test(contest_id, runTest, aflosDB)
				break
		}
		ret.contestid = contest_id
		return ret
	}
	
	Map run_test(long contest_id, boolean runTest, boolean aflosDB)
	{
		Map ret = [:]
		if (runTest) {
			if (contest_id) {
				Contest contest = Contest.get(contest_id)
				if (contest) {
					ret = RunTest(contest, aflosDB)
				}
			}
		}
		return ret
	}
	
	Map RunTest(Contest lastContest, boolean aflosDB)
	{
		Map ret_test = [:]
		if (lastContest) {
			switch (lastContest.title) {
				case "Demo Wettbewerb":
					ret_test = demoContestStandardService.RunTest1(lastContest, lastContest.title, aflosDB)
					break
				case "Demo Wettbewerb (mit Klassen)":
					ret_test = demoContestStandardService.RunTest2(lastContest, lastContest.title, aflosDB)
					break
				case "Demo Wettbewerb (kombinierter Wettbewerb)":
					ret_test = demoContestStandardService.RunTest3(lastContest, lastContest.title, aflosDB)
					break
				case "Demo Wettbewerb (Strecken)":
					ret_test = demoContestRoutesService.RunTest(lastContest, lastContest.title, aflosDB)
					break
				case "Demo Wettbewerb (Intermediate)":
					ret_test = demoContestIntermediateService.RunTest(lastContest, lastContest.title, aflosDB)
					break
				case "Demo Wettbewerb (Curved)":
					ret_test = demoContestCurvedService.RunTest(lastContest, lastContest.title, aflosDB)
					break
				case "Demo Wettbewerb (Procedure Turn)":
					ret_test = demoContestProcedureTurnService.RunTest(lastContest, lastContest.title, aflosDB)
					break
			}
		}
		return ret_test
	}
}
