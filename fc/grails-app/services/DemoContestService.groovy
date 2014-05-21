class DemoContestService 
{
	def fcService
	def demoContestStandardService
	def demoContestOtherService
	def demoContestRoutesService
	def demoContestIntermediateService
	def demoContestCurvedService
	def demoContestProcedureTurnService
	
	int CreateTest(String demoContest)
	{
		int ret = 0
		switch (demoContest) {
			case '98':
				ret = create_test("1")
				ret = create_test("2")
				ret = create_test("3")
				ret = create_test("11")
				ret = create_test("12")
				ret = create_test("13")
				ret = create_test("14")
				ret = create_test("21")
				ret = create_test("22")
				ret = create_test("23")
				ret = create_test("24")
				break
			case '99':
				ret = create_test("1")
				ret = create_test("2")
				ret = create_test("21")
				ret = create_test("22")
				ret = create_test("23")
				ret = create_test("24")
				break
			default:
				ret = create_test(demoContest)
				break
		}
		return ret
	}
	
	int create_test(String demoContest)
	{
		switch (demoContest) {
			case '1': 
		        return demoContestStandardService.CreateTest1("Demo Wettbewerb ${Contest.DEMOCONTESTYEAR}", "demo1", true)
			case '2':
				return demoContestStandardService.CreateTest2("Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (mit Klassen)", "demo2", true)
			case '3':
				return demoContestStandardService.CreateTest3("Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (kombinierter Wettbewerb)", "demo3", false)
			case '11':
				return demoContestOtherService.CreateTest11("Demo Wettbewerb Auswertung ohne Klassen", "demo11", false)
			case '12':
				return demoContestOtherService.CreateTest12("Demo Wettbewerb Auswertung mit Klassen", "demo12", false)
			case '13':
		        return demoContestOtherService.CreateTest13("Demo Wettbewerb (100 Besatzungen)", "demo13", false)
			case '14':
		        return demoContestOtherService.CreateTest14("Demo Wettbewerb (20 Besatzungen)", "demo14", false)
			case '21':
		        return demoContestRoutesService.CreateTest("Demo Wettbewerb (Strecken)", "demo21", true)
			case '22':
		        return demoContestIntermediateService.CreateTest("Demo Wettbewerb (Intermediate)", "demo22", true)
			case '23':
		        return demoContestCurvedService.CreateTest("Demo Wettbewerb (Curved)", "demo23", true)
			case '24':
		        return demoContestProcedureTurnService.CreateTest("Demo Wettbewerb (Procedure Turn)", "demo24", true)
		}
		return 0
	}
	
	Map RunTest(Contest lastContest)
	{
		Map ret_test = [:]
		if (lastContest) {
			switch (lastContest.title) {
				case "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR}":
					ret_test = demoContestStandardService.RunTest1 lastContest, "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR}"
					break
				case "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (mit Klassen)":
					ret_test = demoContestStandardService.RunTest2 lastContest, "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (mit Klassen)"
					break
				case "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (kombinierter Wettbewerb)":
					ret_test = demoContestStandardService.RunTest3 lastContest, "Demo Wettbewerb ${Contest.DEMOCONTESTYEAR} (kombinierter Wettbewerb)"
					break
				case "Demo Wettbewerb (Strecken)":
					ret_test = demoContestRoutesService.RunTest lastContest, "Demo Wettbewerb (Strecken)"
					break
				case "Demo Wettbewerb (Intermediate)":
					ret_test = demoContestIntermediateService.RunTest lastContest, "Demo Wettbewerb (Intermediate)"
					break
				case "Demo Wettbewerb (Curved)":
					ret_test = demoContestCurvedService.RunTest lastContest, "Demo Wettbewerb (Curved)"
					break
				case "Demo Wettbewerb (Procedure Turn)":
					ret_test = demoContestProcedureTurnService.RunTest lastContest, "Demo Wettbewerb (Procedure Turn)"
					break
			}
		}
		return ret_test
	}
}
