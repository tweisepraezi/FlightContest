import java.util.Map;

class DemoContestOtherService
{
	def fcService
	
	long CreateTest11(String testName, String printPrefix, boolean testExists)
	{
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,false,2,ContestRules.R1,true,testExists)
        
        // Tasks
        Map task1 = fcService.putTask(contest,"Task-1","09:00",3,"time:8min","time:10min",5,"wind+:2NM","wind+:2NM",false,false, true,false,false, false,true, true,false,false, false,false,false,false, false)
        Map task2 = fcService.putTask(contest,"Task-2","09:00",3,"time:8min","time:10min",5,"wind+:2NM","wind+:2NM",false,false, false,true,false, false,true, true,false,false, false,false,false,false, false)

        // Crews with Teams, ResultClasses and Aircrafts
        Map crew11 = fcService.putCrew(contest,11,"Crew 1-1","crew1.1.fc@localhost","Deutschland","","D-EAAA","","",85)
        Map crew12 = fcService.putCrew(contest,12,"Crew 1-2","crew1.2.fc@localhost","Deutschland","","D-EAAE","","",80)
        Map crew13 = fcService.putCrew(contest,13,"Crew 1-3","crew1.3.fc@localhost","Schweiz","","D-EAAC","","",70)
        Map crew21 = fcService.putCrew(contest,21,"Crew 2-1","crew2.1.fc@localhost","Deutschland","","D-EAAD","","",80)
        Map crew22 = fcService.putCrew(contest,22,"Crew 2-2","crew2.2.fc@localhost","Schweiz","","D-EAAB","","",70)
        Map crew31 = fcService.putCrew(contest,31,"Crew 3-1","crew3.1.fc@localhost","Deutschland","","D-EAAF","","",80)
        Map crew32 = fcService.putCrew(contest,32,"Crew 3-2","crew3.2.fc@localhost","Schweiz","","D-EAAG","","",70)

        fcService.putsequenceTask(task1,[crew11,crew12,crew13,crew21,crew22,crew31,crew32])
        fcService.putsequenceTask(task2,[crew11,crew12,crew13,crew21,crew22,crew31,crew32])
        
        fcService.putobservationresultsTask(task1, [
                                                    [crew:crew11,routePhotos: 20,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew12,routePhotos:  0,turnPointPhotos: 0,groundTargets:10,testComplete:true],
                                                    [crew:crew13,routePhotos:120,turnPointPhotos: 0,groundTargets:10,testComplete:true],
                                                    [crew:crew21,routePhotos:  0,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew22,routePhotos: 20,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew31,routePhotos: 40,turnPointPhotos:10,groundTargets:60,testComplete:true],
                                                    [crew:crew32,routePhotos: 30,turnPointPhotos:70,groundTargets:10,testComplete:true],
                                                   ])
        fcService.putobservationresultsTask(task2, [
                                                    [crew:crew11,routePhotos: 30,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew12,routePhotos:  0,turnPointPhotos: 0,groundTargets:90,testComplete:true],
                                                    [crew:crew13,routePhotos:130,turnPointPhotos: 0,groundTargets:80,testComplete:true],
                                                    [crew:crew21,routePhotos:  0,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew22,routePhotos: 30,turnPointPhotos: 0,groundTargets:60,testComplete:true],
                                                    [crew:crew31,routePhotos: 50,turnPointPhotos: 0,groundTargets:50,testComplete:true],
                                                    [crew:crew32,routePhotos: 40,turnPointPhotos:60,groundTargets: 0,testComplete:true],
                                                   ])
        fcService.putlandingresultsTask(task1, [
                                                [crew:crew11,landingPenalties:140,testComplete:true],
                                                [crew:crew12,landingPenalties:110,testComplete:true],
                                                [crew:crew13,landingPenalties: 80,testComplete:true],
                                                [crew:crew21,landingPenalties:130,testComplete:true],
                                                [crew:crew22,landingPenalties: 70,testComplete:true],
                                                [crew:crew31,landingPenalties: 90,testComplete:true],
                                                [crew:crew32,landingPenalties:170,testComplete:true],
                                               ])
        fcService.putlandingresultsTask(task2, [
                                                [crew:crew11,landingPenalties: 40,testComplete:true],
                                                [crew:crew12,landingPenalties: 10,testComplete:true],
                                                [crew:crew13,landingPenalties:180,testComplete:true],
                                                [crew:crew21,landingPenalties:230,testComplete:true],
                                                [crew:crew22,landingPenalties: 60,testComplete:true],
                                                [crew:crew31,landingPenalties:190,testComplete:true],
                                                [crew:crew32,landingPenalties:110,testComplete:true],
                                               ])
        
        fcService.putspecialresultsTask(task1, [
                                                [crew:crew11,specialPenalties: 80,testComplete:true],
                                                [crew:crew12,specialPenalties:150,testComplete:true],
                                                [crew:crew13,specialPenalties: 90,testComplete:true],
                                                [crew:crew21,specialPenalties:100,testComplete:true],
                                                [crew:crew22,specialPenalties:170,testComplete:true],
                                                [crew:crew31,specialPenalties: 50,testComplete:true],
                                                [crew:crew32,specialPenalties:120,testComplete:true],
                                               ])
        fcService.putspecialresultsTask(task2, [
                                                [crew:crew11,specialPenalties:280,testComplete:true],
                                                [crew:crew12,specialPenalties: 50,testComplete:true],
                                                [crew:crew13,specialPenalties: 30,testComplete:true],
                                                [crew:crew21,specialPenalties:120,testComplete:true],
                                                [crew:crew22,specialPenalties:220,testComplete:true],
                                                [crew:crew31,specialPenalties: 10,testComplete:true],
                                                [crew:crew32,specialPenalties: 90,testComplete:true],
                                               ])
        
		fcService.printdone ""
		
		return contest.instance.id
	}
	
	long CreateTest12(String testName, String printPrefix, boolean testExists)
	{
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,true,2,ContestRules.R1,true,testExists)
        
        // Tasks
        Map task1 = fcService.putTask(contest,"Task-1","09:00",3,"time:8min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,true,true,false, false,true, true,false,false, false,false,false,false, false)
        Map task2 = fcService.putTask(contest,"Task-2","09:00",3,"time:8min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,true,true,false, false,true, true,false,false, false,false,false,false, false)

        // Classes with properties
        Map resultclass1 = fcService.putResultClass(contest,"Class-1","Pr\u00E4zisionsflugmeisterschaft",ContestRules.R1)
        Map resultclass2 = fcService.putResultClass(contest,"Class-2","",ContestRules.R1)
        Map resultclass3 = fcService.putResultClass(contest,"Class-3","",ContestRules.R1)
        
        // Crews with Teams, ResultClasses and Aircrafts
        Map crew11 = fcService.putCrew(contest,11,"Crew 1-1","crew1.1.fc@localhost","Deutschland","Class-1","D-EAAA","","",85)
        Map crew12 = fcService.putCrew(contest,12,"Crew 1-2","crew1.2.fc@localhost","Deutschland","Class-1","D-EAAE","","",80)
        Map crew13 = fcService.putCrew(contest,13,"Crew 1-3","crew1.3.fc@localhost","Schweiz","Class-1","D-EAAC","","",70)
        Map crew21 = fcService.putCrew(contest,21,"Crew 2-1","crew2.1.fc@localhost","Deutschland","Class-2","D-EAAD","","",80)
        Map crew22 = fcService.putCrew(contest,22,"Crew 2-2","crew2.2.fc@localhost","Schweiz","Class-2","D-EAAB","","",70)
        Map crew31 = fcService.putCrew(contest,31,"Crew 3-1","crew3.1.fc@localhost","Deutschland","Class-3","D-EAAF","","",80)
        Map crew32 = fcService.putCrew(contest,32,"Crew 3-2","crew3.2.fc@localhost","Schweiz","Class-3","D-EAAG","","",70)

        // TaskClass properties
        fcService.puttaskclassTask(task1,resultclass1,false,false, true,true,true, false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task1,resultclass2,false,false, true,true,true, false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task1,resultclass3,false,false, true,true,true, false,true, true,false,false, false,false,false,false)
        
        // TaskClass properties
        fcService.puttaskclassTask(task2,resultclass1,false,false, true,false,false, false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task2,resultclass2,false,false, false,true,false, false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task2,resultclass3,false,false, false,false,true, false,true, true,false,false, false,false,false,false)
        
        fcService.putsequenceTask(task1,[crew11,crew12,crew13,crew21,crew22,crew31,crew32])
        fcService.putsequenceTask(task2,[crew11,crew12,crew13,crew21,crew22,crew31,crew32])
        
        fcService.putobservationresultsTask(task1, [
                                                    [crew:crew11,routePhotos: 20,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew12,routePhotos:  0,turnPointPhotos: 0,groundTargets:10,testComplete:true],
                                                    [crew:crew13,routePhotos:120,turnPointPhotos: 0,groundTargets:10,testComplete:true],
                                                    [crew:crew21,routePhotos:  0,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew22,routePhotos: 20,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew31,routePhotos: 40,turnPointPhotos:10,groundTargets:60,testComplete:true],
                                                    [crew:crew32,routePhotos: 30,turnPointPhotos:70,groundTargets:10,testComplete:true],
                                                   ])
        fcService.putobservationresultsTask(task2, [
                                                    [crew:crew11,routePhotos: 30,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew12,routePhotos:  0,turnPointPhotos: 0,groundTargets:90,testComplete:true],
                                                    [crew:crew13,routePhotos:130,turnPointPhotos: 0,groundTargets:80,testComplete:true],
                                                    [crew:crew21,routePhotos:  0,turnPointPhotos: 0,groundTargets: 0,testComplete:true],
                                                    [crew:crew22,routePhotos: 30,turnPointPhotos: 0,groundTargets:60,testComplete:true],
                                                    [crew:crew31,routePhotos: 50,turnPointPhotos: 0,groundTargets:50,testComplete:true],
                                                    [crew:crew32,routePhotos: 40,turnPointPhotos:60,groundTargets: 0,testComplete:true],
                                                   ])
        fcService.putlandingresultsTask(task1, [
                                                [crew:crew11,landingPenalties:140,testComplete:true],
                                                [crew:crew12,landingPenalties:110,testComplete:true],
                                                [crew:crew13,landingPenalties: 80,testComplete:true],
                                                [crew:crew21,landingPenalties:130,testComplete:true],
                                                [crew:crew22,landingPenalties: 70,testComplete:true],
                                                [crew:crew31,landingPenalties: 90,testComplete:true],
                                                [crew:crew32,landingPenalties:170,testComplete:true],
                                               ])
        fcService.putlandingresultsTask(task2, [
                                                [crew:crew11,landingPenalties: 40,testComplete:true],
                                                [crew:crew12,landingPenalties: 10,testComplete:true],
                                                [crew:crew13,landingPenalties:180,testComplete:true],
                                                [crew:crew21,landingPenalties:230,testComplete:true],
                                                [crew:crew22,landingPenalties: 60,testComplete:true],
                                                [crew:crew31,landingPenalties:190,testComplete:true],
                                                [crew:crew32,landingPenalties:110,testComplete:true],
                                               ])
        
        fcService.putspecialresultsTask(task1, [
                                                [crew:crew11,specialPenalties: 80,testComplete:true],
                                                [crew:crew12,specialPenalties:150,testComplete:true],
                                                [crew:crew13,specialPenalties: 90,testComplete:true],
                                                [crew:crew21,specialPenalties:100,testComplete:true],
                                                [crew:crew22,specialPenalties:170,testComplete:true],
                                                [crew:crew31,specialPenalties: 50,testComplete:true],
                                                [crew:crew32,specialPenalties:120,testComplete:true],
                                               ])
        fcService.putspecialresultsTask(task2, [
                                                [crew:crew11,specialPenalties:280,testComplete:true],
                                                [crew:crew12,specialPenalties: 50,testComplete:true],
                                                [crew:crew13,specialPenalties: 30,testComplete:true],
                                                [crew:crew21,specialPenalties:120,testComplete:true],
                                                [crew:crew22,specialPenalties:220,testComplete:true],
                                                [crew:crew31,specialPenalties: 10,testComplete:true],
                                                [crew:crew32,specialPenalties: 90,testComplete:true],
                                               ])
        
		fcService.printdone ""
		
		return contest.instance.id
	}
	
	long CreateTest13(String testName, String printPrefix, boolean testExists)
	{
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,false,0,ContestRules.R1,true,testExists)
        Map task1 = fcService.putTask(contest,"","11:00",3,"time:10min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,true,true,false, false,true, true,false,false, false,false,false,false, false)
    
        // Crews and Aircrafts
        (1..100).each {
            fcService.putCrew(contest,it,"Name-${it.toString()}","crew-${it.toString()}.fc@localhost","Deutschland","","D-${it.toString()}","C172","rot",110)
        }
        
        fcService.printdone ""
        
		return contest.instance.id
	}
	
	long CreateTest14(String testName, String printPrefix, boolean testExists)
	{
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,false,0,ContestRules.R1,true,testExists)
        Map task1 = fcService.putTask(contest,"","11:00",3,"time:10min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,true,true,false, false,true, true,false,false, false,false,false,false, false)
    
        // Crews and Aircrafts
        (1..20).each {
            fcService.putCrew(contest,it,"Name-${it.toString()}","crew-${it.toString()}.fc@localhost","Deutschland","","D-${it.toString()}","C172","rot",110)
        }
        
        fcService.printdone ""
        
		return contest.instance.id
	}
}
