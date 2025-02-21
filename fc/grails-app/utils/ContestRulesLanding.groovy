import java.util.List;

enum ContestRulesLanding 
{
    FAI_RF ([
            ruleTitle:"FAI GAC Landing appendix - Edition 2024",     // FAI_PF_ANR_NORMAL
            
            // General
            precisionFlying:false,                                   // FAI_RF
            landingFieldImageName:'images/landingfield/fai_rally.jpg', // FAI_RF
            printLandingCalculatorValues:"Y,X,A,B,C,D,E,F,G,H",      // FAI_RF
            
            // LandingTest
            MaxPoints:300,                                // FAI_RF A4.6 - Landing 6.1
            NoLandingPoints:300,                          // FAI_RF A4.6 - Landing 6.1
            OutsideLandingPoints:200,                     // FAI_RF A4.6 - Landing 6.1
            RollingOutsidePoints:200,                     // FAI_RF A4.6 - Landing 6.1
            PowerInBoxPoints:50,                          // FAI_RF A4.6 - Landing 6.1
            GoAroundWithoutTouchingPoints:200,            // FAI_RF A4.6 - Landing 6.1
            GoAroundInsteadStopPoints:200,                // FAI_RF A4.6 - Landing 6.1
            AbnormalLandingPoints:150,                    // FAI_RF A4.6 - Landing 6.1
            NotAllowedAerodynamicAuxiliariesPoints:0,     // FAI_RF
            TouchingObstaclePoints:0,                     // FAI_RF
            PowerInAirPoints:0,                           // FAI_RF
            FlapsInAirPoints:0,                           // FAI_RF
            PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // FAI_RF A4.6 - Landing 6.1
    ]),

    DE_RF_ANR ([
            ruleTitle:"Wettbewerbsordnung Landungen Deutschland - Ausgabe 2025", // DE_RF_ANR
            
            // General
            precisionFlying:false,                                   // DE_RF_ANR
            landingFieldImageName:'images/landingfield/de_nav.jpg',  // DE_RF_ANR
            printLandingCalculatorValues:"Y,X,A,B,C,D,E,F,G,H",      // DE_RF_ANR
            
            // LandingTest
            MaxPoints:300,                                // DE_RF_ANR 4
            NoLandingPoints:300,                          // DE_RF_ANR 4
            OutsideLandingPoints:200,                     // DE_RF_ANR 4
            RollingOutsidePoints:200,                     // DE_RF_ANR 4
            PowerInBoxPoints:50,                          // DE_RF_ANR 4
            GoAroundWithoutTouchingPoints:200,            // DE_RF_ANR 4
            GoAroundInsteadStopPoints:200,                // DE_RF_ANR 4
            AbnormalLandingPoints:150,                    // DE_RF_ANR 4
            NotAllowedAerodynamicAuxiliariesPoints:0,     // DE_RF_ANR
            TouchingObstaclePoints:0,                     // DE_RF_ANR
            PowerInAirPoints:0,                           // DE_RF_ANR
            FlapsInAirPoints:0,                           // DE_RF_ANR
            PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // DE_RF_ANR
    ]),

    AT_RF ([
            ruleTitle:"Navigationsflug-Reglement \u00D6sterreich - Ausgabe 2023", // AT_RF
            
            // General
            precisionFlying:false,                                   // AT_RF
            landingFieldImageName:'images/landingfield/at_nav.jpg',  // AT_RF
            printLandingCalculatorValues:"Y,X,A,B,C,D,E,F,G,H",      // AT_RF
            
            // LandingTest
            MaxPoints:300,                                // AT_RF 5.3e
            NoLandingPoints:300,                          // AT_RF 5.3e
            OutsideLandingPoints:200,                     // AT_RF 5.3e
            RollingOutsidePoints:200,                     // AT_RF 5.3e
            PowerInBoxPoints:50,                          // AT_RF 5.3e
            GoAroundWithoutTouchingPoints:200,            // AT_RF 5.3e
            GoAroundInsteadStopPoints:200,                // AT_RF 5.3e
            AbnormalLandingPoints:150,                    // AT_RF 5.3e
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_RF 
            TouchingObstaclePoints:0,                     // AT_RF 
            PowerInAirPoints:0,                           // AT_RF 
            FlapsInAirPoints:0,                           // AT_RF 
            PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // AT_RF 5.3e
    ]),

    FAI_PF_ANR_NORMAL ([
            ruleTitle:"FAI GAC Landing appendix - Edition 2024",     // FAI_PF_ANR_NORMAL
            
            // General
            precisionFlying:true,                                    // FAI_PF_ANR_NORMAL Precison landings
            landingFieldImageName:'images/landingfield/fai_precision1.jpg,images/landingfield/fai_precision2.jpg', // FAI_PF_ANR_NORMAL
            printLandingCalculatorValues:"A,-1,1,D,E,F,G,H",         // FAI_PF_ANR_NORMAL
            
            // LandingTest
            MaxPoints:400,                                // FAI_PF_ANR_NORMAL Landing 6.2.1
            NoLandingPoints:300,                          // FAI_PF_ANR_NORMAL Landing 6.2.1
            OutsideLandingPoints:300,                     // FAI_PF_ANR_NORMAL Landing 6.2.1
            RollingOutsidePoints:200,                     // FAI_PF_ANR_NORMAL Landing 6.2.1
            PowerInBoxPoints:50,                          // FAI_PF_ANR_NORMAL Landing 6.2.1
            GoAroundWithoutTouchingPoints:0,              // FAI_PF_ANR_NORMAL 
            GoAroundInsteadStopPoints:0,                  // FAI_PF_ANR_NORMAL 
            AbnormalLandingPoints:200,                    // FAI_PF_ANR_NORMAL Landing 6.2.1
            NotAllowedAerodynamicAuxiliariesPoints:0,     // FAI_PF_ANR_NORMAL 
            TouchingObstaclePoints:0,                     // FAI_PF_ANR_NORMAL 
            PowerInAirPoints:0,                           // FAI_PF_ANR_NORMAL 
            FlapsInAirPoints:0,                           // FAI_PF_ANR_NORMAL 
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 5*i}else{return -(10*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 250;case 'D':return 125;case 'E':return 150;case 'F':return 175;case 'G':return 200;case 'H':return 225;default:return null;}}}", // FAI_PF_ANR_NORMAL Landing 6.2.2
    ]),
       
    FAI_PF_ANR_OBSTACLE ([
            ruleTitle:"FAI GAC Landing appendix - Edition 2024",     // FAI_PF_ANR_OBSTACLE
            
            // General
            precisionFlying:true,                                    // FAI_PF_ANR_OBSTACLE Precison landings
            landingFieldImageName:'images/landingfield/fai_precision1.jpg,images/landingfield/fai_precision2.jpg', // FAI_PF_ANR_OBSTACLE
            printLandingCalculatorValues:"A,-1,1,D,E,F,G,H",         // FAI_PF_ANR_OBSTACLE
            
            // LandingTest
            MaxPoints:400,                                // FAI_PF_ANR_OBSTACLE Landing 6.2.1
            NoLandingPoints:300,                          // FAI_PF_ANR_OBSTACLE Landing 6.2.1
            OutsideLandingPoints:300,                     // FAI_PF_ANR_OBSTACLE Landing 6.2.1
            RollingOutsidePoints:200,                     // FAI_PF_ANR_OBSTACLE Landing 6.2.1
            PowerInBoxPoints:50,                          // FAI_PF_ANR_OBSTACLE Landing 6.2.1
            GoAroundWithoutTouchingPoints:0,              // FAI_PF_ANR_OBSTACLE  
            GoAroundInsteadStopPoints:0,                  // FAI_PF_ANR_OBSTACLE  
            AbnormalLandingPoints:200,                    // FAI_PF_ANR_OBSTACLE Landing 6.2.1
            NotAllowedAerodynamicAuxiliariesPoints:0,     // FAI_PF_ANR_OBSTACLE
            TouchingObstaclePoints:400,                   // FAI_PF_ANR_OBSTACLE Landing 6.2.1
            PowerInAirPoints:0,                           // FAI_PF_ANR_OBSTACLE
            FlapsInAirPoints:0,                           // FAI_PF_ANR_OBSTACLE
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 5*i}else{return -(10*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 250;case 'D':return 125;case 'E':return 150;case 'F':return 175;case 'G':return 200;case 'H':return 225;default:return null;}}}", // FAI_PF_ANR_OBSTACLE Landing 6.2.2
    ]),
    
    FAI_PF_ANR_IDLE ([
            ruleTitle:"FAI GAC Landing appendix - Edition 2024",     // FAI_PF_ANR_NORMAL
            
            // General
            precisionFlying:true,                                    // FAI_PF_ANR_NORMAL Precison landings
            landingFieldImageName:'images/landingfield/fai_precision1.jpg,images/landingfield/fai_precision2.jpg', // FAI_PF_ANR_NORMAL
            printLandingCalculatorValues:"A,-1,1,D,E,F,G,H",         // FAI_PF_ANR_NORMAL
            
            // LandingTest
            MaxPoints:200,                                // FAI_PF_ANR_IDLE Landing 6.2.1
            NoLandingPoints:200,                          // FAI_PF_ANR_IDLE Landing 6.2.1
            OutsideLandingPoints:200,                     // FAI_PF_ANR_IDLE Landing 6.2.1
            RollingOutsidePoints:150,                     // FAI_PF_ANR_IDLE Landing 6.2.1
            PowerInBoxPoints:50,                          // FAI_PF_ANR_IDLE Landing 6.2.1
            GoAroundWithoutTouchingPoints:0,              // FAI_PF_ANR_IDLE 
            GoAroundInsteadStopPoints:0,                  // FAI_PF_ANR_IDLE 
            AbnormalLandingPoints:200,                    // FAI_PF_ANR_IDLE Landing 6.2.1
            NotAllowedAerodynamicAuxiliariesPoints:0,     // FAI_PF_ANR_IDLE
            TouchingObstaclePoints:0,                     // FAI_PF_ANR_IDLE
            PowerInAirPoints:200,                         // FAI_PF_ANR_IDLE Landing 6.2.1
            FlapsInAirPoints:0,                           // FAI_PF_ANR_IDLE
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(6*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 150;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return null;}}}", // FAI_PF_ANR_IDLE Landing 6.2.2
    ]),
        
    FAI_PF_ANR_IDLE_NO_FLAPS ([
            ruleTitle:"FAI GAC Landing appendix - Edition 2024",     // FAI_PF_ANR_NORMAL
            
            // General
            precisionFlying:true,                                    // FAI_PF_ANR_NORMAL Precison landings
            landingFieldImageName:'images/landingfield/fai_precision1.jpg,images/landingfield/fai_precision2.jpg', // FAI_PF_ANR_NORMAL
            printLandingCalculatorValues:"A,-1,1,D,E,F,G,H",         // FAI_PF_ANR_NORMAL
            
            // LandingTest
            MaxPoints:200,                                // FAI_PF_ANR_IDLE_NO_FLAPS Landing 6.2.1
            NoLandingPoints:200,                          // FAI_PF_ANR_IDLE_NO_FLAPS Landing 6.2.1
            OutsideLandingPoints:200,                     // FAI_PF_ANR_IDLE_NO_FLAPS Landing 6.2.1
            RollingOutsidePoints:150,                     // FAI_PF_ANR_IDLE_NO_FLAPS Landing 6.2.1
            PowerInBoxPoints:50,                          // FAI_PF_ANR_IDLE_NO_FLAPS Landing 6.2.1
            GoAroundWithoutTouchingPoints:0,              // FAI_PF_ANR_IDLE_NO_FLAPS 
            GoAroundInsteadStopPoints:0,                  // FAI_PF_ANR_IDLE_NO_FLAPS 
            AbnormalLandingPoints:200,                    // FAI_PF_ANR_IDLE_NO_FLAPS Landing 6.2.1
            NotAllowedAerodynamicAuxiliariesPoints:0,     // FAI_PF_ANR_IDLE_NO_FLAPS
            TouchingObstaclePoints:0,                     // FAI_PF_ANR_IDLE_NO_FLAPS
            PowerInAirPoints:200,                         // FAI_PF_ANR_IDLE_NO_FLAPS Landing 6.2.1
            FlapsInAirPoints:200,                         // FAI_PF_ANR_IDLE_NO_FLAPS Landing 6.2.1
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(6*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 150;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return null;}}}", // FAI_PF_ANR_IDLE_NO_FLAPS Landing 6.2.2
    ]),
        
    DE_PF ([
            ruleTitle:"Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2017", // DE_PF
            
            // General
            precisionFlying:false,                                   // DE_PF
            landingFieldImageName:'images/landingfield/de_nav2017*.jpg', // DE_PF
            printLandingCalculatorValues:"F,E,A,B,C,D",              // DE_PF
            
            // LandingTest
            MaxPoints:300,                                // DE_PF 4.3
            NoLandingPoints:300,                          // DE_PF 4.3
            OutsideLandingPoints:200,                     // DE_PF 4.3
            RollingOutsidePoints:200,                     // DE_PF 4.3
            PowerInBoxPoints:100,                         // DE_PF 4.3
            GoAroundWithoutTouchingPoints:200,            // DE_PF 4.3
            GoAroundInsteadStopPoints:200,                // DE_PF 4.3
            AbnormalLandingPoints:200,                    // DE_PF 4.3
            NotAllowedAerodynamicAuxiliariesPoints:0,     // DE_PF
            TouchingObstaclePoints:0,                     // DE_PF
            PowerInAirPoints:0,                           // DE_PF
            FlapsInAirPoints:0,                           // DE_PF
            PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;default:return null;}}", // DE_PF 4.3
    ]),

    CH_PF_NORMAL ([
            ruleTitle:"Wettkampfreglement Pr\u00E4zisionsflug-Schweizermeisterschaft - Ausgabe 2017", // CH_PF_NORMAL
            
            // General
            precisionFlying:true,                                    // CH_PF_NORMAL Precison landings
            landingFieldImageName:'images/landingfield/ch_precision.jpg', // CH_PF_NORMAL
            printLandingCalculatorValues:"A,-1,1,D,E,F,G,H",         // CH_PF_NORMAL
            
            // LandingTest
            MaxPoints:400,                                // CH_PF_NORMAL Anhang 5.4
            NoLandingPoints:300,                          // CH_PF_NORMAL Anhang 5.4
            OutsideLandingPoints:300,                     // CH_PF_NORMAL Anhang 5.4
            RollingOutsidePoints:200,                     // CH_PF_NORMAL Anhang 5.4
            PowerInBoxPoints:50,                          // CH_PF_NORMAL Anhang 5.4
            GoAroundWithoutTouchingPoints:0,              // CH_PF_NORMAL
            GoAroundInsteadStopPoints:0,                  // CH_PF_NORMAL
            AbnormalLandingPoints:200,                    // CH_PF_NORMAL Anhang 5.4
            NotAllowedAerodynamicAuxiliariesPoints:200,   // CH_PF_NORMAL Anhang 5.4
            TouchingObstaclePoints:0,                     // CH_PF_NORMAL
            PowerInAirPoints:0,                           // CH_PF_NORMAL
            FlapsInAirPoints:0,                           // CH_PF_NORMAL
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 5*i}else{return -(10*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 250;case 'D':return 125;case 'E':return 150;case 'F':return 175;case 'G':return 200;case 'H':return 225;default:return null;}}}", // CH_PF_NORMAL Anhang 2
    ]),

    CH_PF_OBSTACLE ([        
            ruleTitle:"Wettkampfreglement Pr\u00E4zisionsflug-Schweizermeisterschaft - Ausgabe 2017", // CH_PF_OBSTACLE
            
            // General
            precisionFlying:true,                                    // CH_PF_OBSTACLE Precison landings
            landingFieldImageName:'images/landingfield/ch_precision.jpg', // CH_PF_OBSTACLE
            printLandingCalculatorValues:"A,-1,1,D,E,F,G,H",         // CH_PF_OBSTACLE
            
            // LandingTest
            MaxPoints:400,                                // CH_PF_OBSTACLE Anhang 5.4
            NoLandingPoints:300,                          // CH_PF_OBSTACLE Anhang 5.4
            OutsideLandingPoints:300,                     // CH_PF_OBSTACLE Anhang 5.4
            RollingOutsidePoints:200,                     // CH_PF_OBSTACLE Anhang 5.4
            PowerInBoxPoints:50,                          // CH_PF_OBSTACLE Anhang 5.4
            GoAroundWithoutTouchingPoints:0,              // CH_PF_OBSTACLE
            GoAroundInsteadStopPoints:0,                  // CH_PF_OBSTACLE
            AbnormalLandingPoints:200,                    // CH_PF_OBSTACLE Anhang 5.4
            NotAllowedAerodynamicAuxiliariesPoints:200,   // CH_PF_OBSTACLE Anhang 5.4
            TouchingObstaclePoints:400,                   // CH_PF_OBSTACLE Anhang 5.4
            PowerInAirPoints:0,                           // CH_PF_OBSTACLE
            FlapsInAirPoints:0,                           // CH_PF_OBSTACLE
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 5*i}else{return -(10*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 250;case 'D':return 125;case 'E':return 150;case 'F':return 175;case 'G':return 200;case 'H':return 225;default:return null;}}}", // CH_PF_OBSTACLE Anhang 2
    ]),

    CH_PF_IDLE ([    
            ruleTitle:"Wettkampfreglement Pr\u00E4zisionsflug-Schweizermeisterschaft - Ausgabe 2017", // CH_PF_IDLE
            
            // General
            precisionFlying:true,                                    // CH_PF_IDLE Precison landings
            landingFieldImageName:'images/landingfield/ch_precision.jpg', // CH_PF_IDLE
            printLandingCalculatorValues:"A,-1,1,D,E,F,G,H",         // CH_PF_IDLE
            
            // LandingTest
            MaxPoints:200,                                // CH_PF_IDLE Anhang 5.4
            NoLandingPoints:200,                          // CH_PF_IDLE Anhang 5.4
            OutsideLandingPoints:200,                     // CH_PF_IDLE Anhang 5.4
            RollingOutsidePoints:150,                     // CH_PF_IDLE Anhang 5.4
            PowerInBoxPoints:50,                          // CH_PF_IDLE Anhang 5.4
            GoAroundWithoutTouchingPoints:0,              // CH_PF_IDLE
            GoAroundInsteadStopPoints:0,                  // CH_PF_IDLE
            AbnormalLandingPoints:200,                    // CH_PF_IDLE Anhang 5.4
            NotAllowedAerodynamicAuxiliariesPoints:200,   // CH_PF_IDLE Anhang 5.4
            TouchingObstaclePoints:0,                     // CH_PF_IDLE
            PowerInAirPoints:200,                         // CH_PF_IDLE Anhang 5.4
            FlapsInAirPoints:0,                           // CH_PF_IDLE
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(6*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 150;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return null;}}}", // CH_PF_IDLE Anhang 2
    ]),

    CH_PF_IDLE_NO_FLAPS ([
            ruleTitle:"Wettkampfreglement Pr\u00E4zisionsflug-Schweizermeisterschaft - Ausgabe 2017", // CH_PF_IDLE_NO_FLAPS
            
            // General
            precisionFlying:true,                                    // CH_PF_IDLE_NO_FLAPS Precison landings
            landingFieldImageName:'images/landingfield/ch_precision.jpg', // CH_PF_IDLE_NO_FLAPS
            printLandingCalculatorValues:"A,-1,1,D,E,F,G,H",         // CH_PF_IDLE_NO_FLAPS
            
            // LandingTest
            MaxPoints:200,                                // CH_PF_IDLE_NO_FLAPS Anhang 5.4
            NoLandingPoints:200,                          // CH_PF_IDLE_NO_FLAPS Anhang 5.4
            OutsideLandingPoints:200,                     // CH_PF_IDLE_NO_FLAPS Anhang 5.4
            RollingOutsidePoints:150,                     // CH_PF_IDLE_NO_FLAPS Anhang 5.4
            PowerInBoxPoints:50,                          // CH_PF_IDLE_NO_FLAPS Anhang 5.4
            GoAroundWithoutTouchingPoints:0,              // CH_PF_IDLE_NO_FLAPS
            GoAroundInsteadStopPoints:0,                  // CH_PF_IDLE_NO_FLAPS
            AbnormalLandingPoints:200,                    // CH_PF_IDLE_NO_FLAPS Anhang 5.4
            NotAllowedAerodynamicAuxiliariesPoints:200,   // CH_PF_IDLE_NO_FLAPS Anhang 5.4            
            TouchingObstaclePoints:0,                     // CH_PF_IDLE_NO_FLAPS
            PowerInAirPoints:200,                         // CH_PF_IDLE_NO_FLAPS Anhang 5.4
            FlapsInAirPoints:200,                         // CH_PF_IDLE_NO_FLAPS Anhang 5.4
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(6*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 150;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return null;}}}", // CH_PF_IDLE_NO_FLAPS Anhang 2
    ]),

    AT_PF1_NORMAL ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 1 - Ausgabe 2023", // AT_PF1_NORMAL
            
            // General
            precisionFlying:true,                                    // AT_PF1_NORMAL Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing1.jpg', // AT_PF1_NORMAL
            printLandingCalculatorValues:"C,A,I,II,III",             // AT_PF1_NORMAL
            
            // LandingTest
            MaxPoints:100,                                // AT_PF1_NORMAL 4.5
            NoLandingPoints:100,                          // AT_PF1_NORMAL
            OutsideLandingPoints:100,                     // AT_PF1_NORMAL
            RollingOutsidePoints:0,                       // AT_PF1_NORMAL
            PowerInBoxPoints:50,                          // AT_PF1_NORMAL 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF1_NORMAL
            GoAroundInsteadStopPoints:0,                  // AT_PF1_NORMAL
            AbnormalLandingPoints:100,                    // AT_PF1_NORMAL 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF1_NORMAL
            TouchingObstaclePoints:0,                     // AT_PF1_NORMAL
            PowerInAirPoints:0,                           // AT_PF1_NORMAL
            FlapsInAirPoints:0,                           // AT_PF1_NORMAL
            PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'A':return 20;case 'I':return 10;case 'II':return 30;case 'III':return 50;default:return null;}}", // AT_PF1_NORMAL 4.5
    ]),
        
    AT_PF1_OBSTACLE ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 1 - Ausgabe 2023", // AT_PF1_OBSTACLE
            
            // General
            precisionFlying:true,                                    // AT_PF1_OBSTACLE Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing1.jpg', // AT_PF1_OBSTACLE
            printLandingCalculatorValues:"C,A,I,II,III",             // AT_PF1_OBSTACLE
            
            // LandingTest
            MaxPoints:200,                                // AT_PF1_OBSTACLE 4.5
            NoLandingPoints:200,                          // AT_PF1_OBSTACLE
            OutsideLandingPoints:200,                     // AT_PF1_OBSTACLE
            RollingOutsidePoints:0,                       // AT_PF1_OBSTACLE
            PowerInBoxPoints:50,                          // AT_PF1_OBSTACLE 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF1_OBSTACLE
            GoAroundInsteadStopPoints:0,                  // AT_PF1_OBSTACLE
            AbnormalLandingPoints:100,                    // AT_PF1_OBSTACLE 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF1_OBSTACLE
            TouchingObstaclePoints:100,                   // AT_PF1_OBSTACLE 4.5
            PowerInAirPoints:0,                           // AT_PF1_OBSTACLE
            FlapsInAirPoints:0,                           // AT_PF1_OBSTACLE
            PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'A':return 20;case 'I':return 10;case 'II':return 30;case 'III':return 50;default:return null;}}", // AT_PF1_OBSTACLE 4.5
    ]),

    AT_PF1_IDLE ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 1 - Ausgabe 2023", // AT_PF1_IDLE
            
            // General
            precisionFlying:true,                                    // AT_PF1_IDLE Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing1.jpg', // AT_PF1_IDLE
            printLandingCalculatorValues:"C,A,I,II,III",             // AT_PF1_IDLE
            
            // LandingTest
            MaxPoints:200,                                // AT_PF1_IDLE 4.5
            NoLandingPoints:200,                          // AT_PF1_IDLE
            OutsideLandingPoints:200,                     // AT_PF1_IDLE
            RollingOutsidePoints:0,                       // AT_PF1_IDLE
            PowerInBoxPoints:50,                          // AT_PF1_IDLE 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF1_IDLE
            GoAroundInsteadStopPoints:0,                  // AT_PF1_IDLE
            AbnormalLandingPoints:100,                    // AT_PF1_IDLE 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF1_IDLE
            TouchingObstaclePoints:0,                     // AT_PF1_IDLE
            PowerInAirPoints:100,                         // AT_PF1_IDLE 4.5
            FlapsInAirPoints:0,                           // AT_PF1_IDLE
            PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'A':return 20;case 'I':return 10;case 'II':return 30;case 'III':return 50;default:return null;}}", // AT_PF1_IDLE 4.5
    ]),
        
    AT_PF1_IDLE_NO_FLAPS ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 1 - Ausgabe 2023", // AT_PF1_IDLE_NO_FLAPS
            
            // General
            precisionFlying:true,                                    // AT_PF1_IDLE_NO_FLAPS Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing1.jpg', // AT_PF1_IDLE_NO_FLAPS
            printLandingCalculatorValues:"C,A,I,II,III",             // AT_PF1_IDLE_NO_FLAPS
            
            // LandingTest
            MaxPoints:200,                                // AT_PF1_IDLE_NO_FLAPS 4.5
            NoLandingPoints:200,                          // AT_PF1_IDLE_NO_FLAPS
            OutsideLandingPoints:200,                     // AT_PF1_IDLE_NO_FLAPS
            RollingOutsidePoints:0,                       // AT_PF1_IDLE_NO_FLAPS
            PowerInBoxPoints:50,                          // AT_PF1_IDLE_NO_FLAPS 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF1_IDLE_NO_FLAPS
            GoAroundInsteadStopPoints:0,                  // AT_PF1_IDLE_NO_FLAPS
            AbnormalLandingPoints:100,                    // AT_PF1_IDLE_NO_FLAPS 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF1_IDLE_NO_FLAPS
            TouchingObstaclePoints:0,                     // AT_PF1_IDLE_NO_FLAPS
            PowerInAirPoints:100,                         // AT_PF1_IDLE_NO_FLAPS 4.5
            FlapsInAirPoints:100,                         // AT_PF1_IDLE_NO_FLAPS 4.5
            PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'A':return 20;case 'I':return 10;case 'II':return 30;case 'III':return 50;default:return null;}}", // AT_PF1_IDLE_NO_FLAPS 4.5
    ]),
        
    AT_PF2_NORMAL ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 2 - Ausgabe 2023", // AT_PF2_NORMAL
            
            // General
            precisionFlying:true,                                    // AT_PF2_NORMAL Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing2.jpg', // AT_PF2_NORMAL
            printLandingCalculatorValues:"C,B,A,-5,5,I,II,III,IV,V,VI", // AT_PF2_NORMAL
            
            // LandingTest
            MaxPoints:100,                                // AT_PF2_NORMAL 4.5
            NoLandingPoints:100,                          // AT_PF2_NORMAL
            OutsideLandingPoints:100,                     // AT_PF2_NORMAL
            RollingOutsidePoints:0,                       // AT_PF2_NORMAL
            PowerInBoxPoints:50,                          // AT_PF2_NORMAL 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF2_NORMAL
            GoAroundInsteadStopPoints:0,                  // AT_PF2_NORMAL
            AbnormalLandingPoints:100,                    // AT_PF2_NORMAL 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF2_NORMAL
            TouchingObstaclePoints:0,                     // AT_PF2_NORMAL
            PowerInAirPoints:0,                           // AT_PF2_NORMAL
            FlapsInAirPoints:0,                           // AT_PF2_NORMAL
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'B':return 40;case 'A':return 20;case 'I':return 10;case 'II':return 20;case 'III':return 30;case 'IV':return 40;case 'V':return 50;case 'VI':return 60;default:return null;}}}", // AT_PF2_NORMAL 4.5
    ]),

    AT_PF2_OBSTACLE ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 2 - Ausgabe 2023", // AT_PF2_OBSTACLE
            
            // General
            precisionFlying:true,                                    // AT_PF2_OBSTACLE Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing2.jpg', // AT_PF2_OBSTACLE
            printLandingCalculatorValues:"C,B,A,-5,5,I,II,III,IV,V,VI", // AT_PF2_OBSTACLE
            
            // LandingTest
            MaxPoints:200,                                // AT_PF2_OBSTACLE 4.5
            NoLandingPoints:200,                          // AT_PF2_OBSTACLE
            OutsideLandingPoints:200,                     // AT_PF2_OBSTACLE
            RollingOutsidePoints:0,                       // AT_PF2_OBSTACLE
            PowerInBoxPoints:50,                          // AT_PF2_OBSTACLE 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF2_OBSTACLE
            GoAroundInsteadStopPoints:0,                  // AT_PF2_OBSTACLE
            AbnormalLandingPoints:100,                    // AT_PF2_OBSTACLE 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF2_OBSTACLE
            TouchingObstaclePoints:100,                   // AT_PF2_OBSTACLE 4.5
            PowerInAirPoints:0,                           // AT_PF2_OBSTACLE
            FlapsInAirPoints:0,                           // AT_PF2_OBSTACLE
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'B':return 40;case 'A':return 20;case 'I':return 10;case 'II':return 20;case 'III':return 30;case 'IV':return 40;case 'V':return 50;case 'VI':return 60;default:return null;}}}", // AT_PF2_OBSTACLE 4.5
    ]),

    AT_PF2_IDLE ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 2 - Ausgabe 2023", // AT_PF2_IDLE
            
            // General
            precisionFlying:true,                                    // AT_PF2_IDLE Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing2.jpg', // AT_PF2_IDLE
            printLandingCalculatorValues:"C,B,A,-5,5,I,II,III,IV,V,VI", // AT_PF2_IDLE
            
            // LandingTest
            MaxPoints:200,                                // AT_PF2_IDLE 4.5
            NoLandingPoints:200,                          // AT_PF2_IDLE
            OutsideLandingPoints:200,                     // AT_PF2_IDLE
            RollingOutsidePoints:0,                       // AT_PF2_IDLE
            PowerInBoxPoints:50,                          // AT_PF2_IDLE 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF2_IDLE
            GoAroundInsteadStopPoints:0,                  // AT_PF2_IDLE
            AbnormalLandingPoints:100,                    // AT_PF2_IDLE 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF2_IDLE
            TouchingObstaclePoints:0,                     // AT_PF2_IDLE
            PowerInAirPoints:100,                         // AT_PF2_IDLE 4.5
            FlapsInAirPoints:0,                           // AT_PF2_IDLE
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'B':return 40;case 'A':return 20;case 'I':return 10;case 'II':return 20;case 'III':return 30;case 'IV':return 40;case 'V':return 50;case 'VI':return 60;default:return null;}}}", // AT_PF2_IDLE 4.5
    ]),

    AT_PF2_IDLE_NO_FLAPS ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 2 - Ausgabe 2023", // AT_PF2_IDLE_NO_FLAPS
            
            // General
            precisionFlying:true,                                    // AT_PF2_IDLE_NO_FLAPS Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing2.jpg', // AT_PF2_IDLE_NO_FLAPS
            printLandingCalculatorValues:"C,B,A,-5,5,I,II,III,IV,V,VI", // AT_PF2_IDLE_NO_FLAPS
            
            // LandingTest
            MaxPoints:200,                                // AT_PF2_IDLE_NO_FLAPS 4.5
            NoLandingPoints:200,                          // AT_PF2_IDLE_NO_FLAPS
            OutsideLandingPoints:200,                     // AT_PF2_IDLE_NO_FLAPS
            RollingOutsidePoints:0,                       // AT_PF2_IDLE_NO_FLAPS
            PowerInBoxPoints:50,                          // AT_PF2_IDLE_NO_FLAPS 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF2_IDLE_NO_FLAPS
            GoAroundInsteadStopPoints:0,                  // AT_PF2_IDLE_NO_FLAPS
            AbnormalLandingPoints:100,                    // AT_PF2_IDLE_NO_FLAPS 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF2_IDLE_NO_FLAPS
            TouchingObstaclePoints:0,                     // AT_PF2_IDLE_NO_FLAPS
            PowerInAirPoints:100,                         // AT_PF2_IDLE_NO_FLAPS 4.5
            FlapsInAirPoints:100,                         // AT_PF2_IDLE_NO_FLAPS 4.5
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'B':return 40;case 'A':return 20;case 'I':return 10;case 'II':return 20;case 'III':return 30;case 'IV':return 40;case 'V':return 50;case 'VI':return 60;default:return null;}}}", // AT_PF2_IDLE_NO_FLAPS 4.5
    ]),

    AT_PF3_NORMAL ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 3 - Ausgabe 2023", // AT_PF3_NORMAL
            
            // General
            precisionFlying:true,                                    // AT_PF3_NORMAL Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing3.jpg', // AT_PF3_NORMAL
            printLandingCalculatorValues:"-20,-10,-1,1,10,20,30",    // AT_PF3_NORMAL
            
            // LandingTest
            MaxPoints:100,                                // AT_PF3_NORMAL 4.5
            NoLandingPoints:100,                          // AT_PF3_NORMAL
            OutsideLandingPoints:100,                     // AT_PF3_NORMAL
            RollingOutsidePoints:0,                       // AT_PF3_NORMAL
            PowerInBoxPoints:50,                          // AT_PF3_NORMAL 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF3_NORMAL
            GoAroundInsteadStopPoints:0,                  // AT_PF3_NORMAL
            AbnormalLandingPoints:100,                    // AT_PF3_NORMAL 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF3_NORMAL
            TouchingObstaclePoints:0,                     // AT_PF3_NORMAL
            PowerInAirPoints:0,                           // AT_PF3_NORMAL
            FlapsInAirPoints:0,                           // AT_PF3_NORMAL
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(3*i)}}else{return null;}}", // AT_PF3_NORMAL 4.5
    ]),

    AT_PF3_OBSTACLE ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 3 - Ausgabe 2023", // AT_PF3_OBSTACLE
            
            // General
            precisionFlying:true,                                    // AT_PF3_OBSTACLE Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing3.jpg', // AT_PF3_OBSTACLE
            printLandingCalculatorValues:"-20,-10,-1,1,10,20,30",    // AT_PF3_OBSTACLE
            
            // LandingTest
            MaxPoints:200,                                // AT_PF3_OBSTACLE 4.5
            NoLandingPoints:200,                          // AT_PF3_OBSTACLE
            OutsideLandingPoints:200,                     // AT_PF3_OBSTACLE
            RollingOutsidePoints:0,                       // AT_PF3_OBSTACLE
            PowerInBoxPoints:50,                          // AT_PF3_OBSTACLE 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF3_OBSTACLE
            GoAroundInsteadStopPoints:0,                  // AT_PF3_OBSTACLE
            AbnormalLandingPoints:100,                    // AT_PF3_OBSTACLE 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF3_OBSTACLE
            TouchingObstaclePoints:100,                   // AT_PF3_OBSTACLE 4.5
            PowerInAirPoints:0,                           // AT_PF3_OBSTACLE
            FlapsInAirPoints:0,                           // AT_PF3_OBSTACLE
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(3*i)}}else{return null;}}", // AT_PF3_OBSTACLE 4.5
    ]),

    AT_PF3_IDLE ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 3 - Ausgabe 2023", // AT_PF3_IDLE
            
            // General
            precisionFlying:true,                                    // AT_PF3_IDLE Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing3.jpg', // AT_PF3_IDLE
            printLandingCalculatorValues:"-20,-10,-1,1,10,20,30",    // AT_PF3_IDLE
            
            // LandingTest
            MaxPoints:200,                                // AT_PF3_IDLE 4.5
            NoLandingPoints:200,                          // AT_PF3_IDLE
            OutsideLandingPoints:200,                     // AT_PF3_IDLE
            RollingOutsidePoints:0,                       // AT_PF3_IDLE
            PowerInBoxPoints:50,                          // AT_PF3_IDLE 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF3_IDLE
            GoAroundInsteadStopPoints:0,                  // AT_PF3_IDLE
            AbnormalLandingPoints:100,                    // AT_PF3_IDLE 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF3_IDLE
            TouchingObstaclePoints:0,                     // AT_PF3_IDLE
            PowerInAirPoints:100,                         // AT_PF3_IDLE 4.5
            FlapsInAirPoints:0,                           // AT_PF3_IDLE
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(3*i)}}else{return null;}}", // AT_PF3_IDLE 4.5
    ]),

    AT_PF3_IDLE_NO_FLAPS ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 3 - Ausgabe 2023", // AT_PF3_IDLE_NO_FLAPS
            
            // General
            precisionFlying:true,                                    // AT_PF3_IDLE_NO_FLAPS Precison landings
            landingFieldImageName:'images/landingfield/at_precision_landing3.jpg', // AT_PF3_IDLE_NO_FLAPS
            printLandingCalculatorValues:"-20,-10,-1,1,10,20,30",    // AT_PF3_IDLE_NO_FLAPS
            
            // LandingTest
            MaxPoints:200,                                // AT_PF3_IDLE_NO_FLAPS 4.5
            NoLandingPoints:200,                          // AT_PF3_IDLE_NO_FLAPS
            OutsideLandingPoints:200,                     // AT_PF3_IDLE_NO_FLAPS
            RollingOutsidePoints:0,                       // AT_PF3_IDLE_NO_FLAPS
            PowerInBoxPoints:50,                          // AT_PF3_IDLE_NO_FLAPS 4.5
            GoAroundWithoutTouchingPoints:0,              // AT_PF3_IDLE_NO_FLAPS
            GoAroundInsteadStopPoints:0,                  // AT_PF3_IDLE_NO_FLAPS
            AbnormalLandingPoints:100,                    // AT_PF3_IDLE_NO_FLAPS 4.5
            NotAllowedAerodynamicAuxiliariesPoints:0,     // AT_PF3_IDLE_NO_FLAPS
            TouchingObstaclePoints:0,                     // AT_PF3_IDLE_NO_FLAPS
            PowerInAirPoints:100,                         // AT_PF3_IDLE_NO_FLAPS 4.5
            FlapsInAirPoints:100,                         // AT_PF3_IDLE_NO_FLAPS 4.5
            PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(3*i)}}else{return null;}}", // AT_PF3_IDLE_NO_FLAPS 4.5
    ])

	ContestRulesLanding(Map data) 
	{
		this.data = data
	}
	
	final Map data
}