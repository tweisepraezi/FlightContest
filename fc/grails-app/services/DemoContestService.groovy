class DemoContestService 
{
	def fcService
	def demoContestStandardService
	def demoContestOtherService
	def demoContestRoutesService
	def demoContestIntermediateService
	def demoContestCurvedService
	def demoContestProcedureTurnService
	
	Map CreateTest(String demoContest)
	{
		Map ret = [:]
		switch (demoContest) {
			case '98':
                ret = create_test("2",ret,true)
                long contest_id = ret.contestid
				ret = create_test("1",ret,true)
				ret = create_test("3",ret,true)
				ret = create_test("11",ret,true)
				ret = create_test("12",ret,true)
				ret = create_test("13",ret,true)
				ret = create_test("14",ret,true)
				ret = create_test("21",ret,true)
				ret = create_test("22",ret,true)
				ret = create_test("23",ret,true)
				ret = create_test("24",ret,true)
                ret.contestid = contest_id
				break
			case '99':
                ret = create_test("2",ret,true)
                long contest_id = ret.contestid
				ret = create_test("1",ret,true)
				ret = create_test("21",ret,true)
				ret = create_test("22",ret,true)
				ret = create_test("23",ret,true)
				ret = create_test("24",ret,true)
                ret.contestid = contest_id
				break
			default:
				ret = create_test(demoContest,true)
				break
		}
		return ret
	}
	
	Map create_test(String demoContest, Map retValue, boolean runTest)
	{
		Map ret = create_test(demoContest, runTest)
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
	
	Map create_test(String demoContest, boolean runTest)
	{
		Map ret = [:]
		long contest_id = 0
		switch (demoContest) {
			case '1': 
		        contest_id = demoContestStandardService.CreateTest1("Demo Wettbewerb", "demo1", true)
				ret = run_test(contest_id, runTest)
				break
			case '2':
				contest_id = demoContestStandardService.CreateTest2("Demo Wettbewerb (mit Klassen)", "demo2", true)
				ret = run_test(contest_id, runTest)
				break
			case '3':
				contest_id = demoContestStandardService.CreateTest3("Demo Wettbewerb (kombinierter Wettbewerb)", "demo3", false)
				break
			case '11':
				contest_id = demoContestOtherService.CreateTest11("Demo Wettbewerb Auswertung ohne Klassen", "demo11", false)
				break
			case '12':
				contest_id = demoContestOtherService.CreateTest12("Demo Wettbewerb Auswertung mit Klassen", "demo12", false)
				break
			case '13':
		        contest_id = demoContestOtherService.CreateTest13("Demo Wettbewerb (100 Besatzungen)", "demo13", false)
				break
			case '14':
		        contest_id = demoContestOtherService.CreateTest14("Demo Wettbewerb (20 Besatzungen)", "demo14", false)
				break
			case '21':
		        contest_id = demoContestRoutesService.CreateTest("Demo Wettbewerb (Strecken)", "demo21", true)
				ret = run_test(contest_id, runTest)
				break
			case '22':
		        contest_id = demoContestIntermediateService.CreateTest("Demo Wettbewerb (Intermediate)", "demo22", true)
				ret = run_test(contest_id, runTest)
				break
			case '23':
		        contest_id = demoContestCurvedService.CreateTest("Demo Wettbewerb (Curved)", "demo23", true)
				ret = run_test(contest_id, runTest)
				break
			case '24':
		        contest_id = demoContestProcedureTurnService.CreateTest("Demo Wettbewerb (Procedure Turn)", "demo24", true)
				ret = run_test(contest_id, runTest)
				break
		}
		ret.contestid = contest_id
		return ret
	}
	
	Map run_test(long contest_id, boolean runTest)
	{
		Map ret = [:]
		if (runTest) {
			if (contest_id) {
				Contest contest = Contest.get(contest_id)
				if (contest) {
					ret = RunTest(contest)
				}
			}
		}
		return ret
	}
	
	Map RunTest(Contest lastContest)
	{
		Map ret_test = [:]
		if (lastContest) {
			switch (lastContest.title) {
				case "Demo Wettbewerb":
					ret_test = demoContestStandardService.RunTest1(lastContest, lastContest.title)
					break
				case "Demo Wettbewerb (mit Klassen)":
					ret_test = demoContestStandardService.RunTest2(lastContest, lastContest.title)
					break
				case "Demo Wettbewerb (kombinierter Wettbewerb)":
					ret_test = demoContestStandardService.RunTest3(lastContest, lastContest.title)
					break
				case "Demo Wettbewerb (Strecken)":
					ret_test = demoContestRoutesService.RunTest(lastContest, lastContest.title)
					break
				case "Demo Wettbewerb (Intermediate)":
					ret_test = demoContestIntermediateService.RunTest(lastContest, lastContest.title)
					break
				case "Demo Wettbewerb (Curved)":
					ret_test = demoContestCurvedService.RunTest(lastContest, lastContest.title)
					break
				case "Demo Wettbewerb (Procedure Turn)":
					ret_test = demoContestProcedureTurnService.RunTest(lastContest, lastContest.title)
					break
			}
		}
		return ret_test
	}
}
