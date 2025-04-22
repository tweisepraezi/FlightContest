import java.util.List;

enum ContestRulesLanding 
{
    FAI_RF ([
            LongRuleTitle:"FAI GAC Landing appendix - Edition 2024 - Rally Flying", // FAI_RF
            ShortRuleTitle:"",                            // FAI_RF
            AirfieldImageNames:'images/landingfield/fai_rally.jpg', // FAI_RF
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
            PrintCalculatorValues:"Y,X,A,B,C,D,E,F,G,H",  // FAI_RF
    ]),

    DE_RF_ANR ([
            LongRuleTitle:"Regelwerk Landewertung Deutschland - Ausgabe 2025", // DE_RF_ANR
            ShortRuleTitle:"",                            // DE_RF_ANR
            AirfieldImageNames:'images/landingfield/de_nav.jpg', // DE_RF_ANR
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
            PrintCalculatorValues:"Y,X,A,B,C,D,E,F,G,H",  // DE_RF_ANR
    ]),

    AT_RF ([
            LongRuleTitle:"Navigationsflug-Reglement \u00D6sterreich - Ausgabe 2023", // AT_RF
            ShortRuleTitle:"",                            // AT_RF
            AirfieldImageNames:'images/landingfield/at_nav.jpg',  // AT_RF
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
            PrintCalculatorValues:"Y,X,A,B,C,D,E,F,G,H",  // AT_RF
    ]),

    FAI_PF_ANR_NORMAL ([
            LongRuleTitle:"FAI GAC Landing appendix - Edition 2024 - Precision Flying / Air Navigation Race - Normal", // FAI_PF_ANR_NORMAL
            ShortRuleTitle:"Normal",                      // FAI_PF_ANR_NORMAL
            AirfieldImageNames:'images/landingfield/fai_precision1.jpg,images/landingfield/fai_precision2.jpg', // FAI_PF_ANR_NORMAL
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
            PrintCalculatorValues:"A,-1,1,D,E,F,G,H",     // FAI_PF_ANR_NORMAL
    ]),
       
    FAI_PF_ANR_OBSTACLE ([
            LongRuleTitle:"FAI GAC Landing appendix - Edition 2024 - Precision Flying / Air Navigation Race - Obstacle", // FAI_PF_ANR_OBSTACLE
            ShortRuleTitle:"Obstacle",                    // FAI_PF_ANR_OBSTACLE
            AirfieldImageNames:'images/landingfield/fai_precision1.jpg,images/landingfield/fai_precision2.jpg', // FAI_PF_ANR_OBSTACLE
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
            PrintCalculatorValues:"A,-1,1,D,E,F,G,H",     // FAI_PF_ANR_OBSTACLE
    ]),
    
    FAI_PF_ANR_IDLE ([
            LongRuleTitle:"FAI GAC Landing appendix - Edition 2024 - Precision Flying / Air Navigation Race - Idle", // FAI_PF_ANR_IDLE
            ShortRuleTitle:"Idle",                        // FAI_PF_ANR_IDLE
            AirfieldImageNames:'images/landingfield/fai_precision1.jpg,images/landingfield/fai_precision2.jpg', // FAI_PF_ANR_IDLE
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
            PrintCalculatorValues:"A,-1,1,D,E,F,G,H",     // FAI_PF_ANR_IDLE
    ]),
        
    FAI_PF_ANR_IDLE_NO_FLAPS ([
            LongRuleTitle:"FAI GAC Landing appendix - Edition 2024 - Precision Flying / Air Navigation Race - Idle No Flaps", // FAI_PF_ANR_NORMAL
            ShortRuleTitle:"Idle No Flaps",               // FAI_PF_ANR_NORMAL
            AirfieldImageNames:'images/landingfield/fai_precision1.jpg,images/landingfield/fai_precision2.jpg', // FAI_PF_ANR_IDLE_NO_FLAPS
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
            PrintCalculatorValues:"A,-1,1,D,E,F,G,H",     // FAI_PF_ANR_IDLE_NO_FLAPS
    ]),
        
    DE_PF ([
            LongRuleTitle:"Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2017", // DE_PF
            ShortRuleTitle:"",                            // DE_PF
            AirfieldImageNames:'images/landingfield/de_nav2017*.jpg', // DE_PF
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
            PrintCalculatorValues:"F,E,A,B,C,D",          // DE_PF
    ]),

    CH_PF_NORMAL ([
            LongRuleTitle:"Wettkampfreglement Pr\u00E4zisionsflug-Schweizermeisterschaft - Ausgabe 2017 - Landung mit Motorhilfe", // CH_PF_NORMAL
            ShortRuleTitle:"Landung mit Motorhilfe",      // CH_PF_NORMAL
            AirfieldImageNames:'images/landingfield/ch_precision.jpg', // CH_PF_NORMAL
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
            PrintCalculatorValues:"A,-1,1,D,E,F,G,H",     // CH_PF_NORMAL
    ]),

    CH_PF_OBSTACLE ([        
            LongRuleTitle:"Wettkampfreglement Pr\u00E4zisionsflug-Schweizermeisterschaft - Ausgabe 2017 - Hindernislandung mit Motorhilfe", // CH_PF_OBSTACLE
            ShortRuleTitle:"Hindernislandung mit Motorhilfe", // CH_PF_OBSTACLE
            AirfieldImageNames:'images/landingfield/ch_precision.jpg', // CH_PF_OBSTACLE
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
            PrintCalculatorValues:"A,-1,1,D,E,F,G,H",     // CH_PF_OBSTACLE
    ]),

    CH_PF_IDLE ([    
            LongRuleTitle:"Wettkampfreglement Pr\u00E4zisionsflug-Schweizermeisterschaft - Ausgabe 2017 - Simulierte Notlandung", // CH_PF_IDLE
            ShortRuleTitle:"Simulierte Notlandung",       // CH_PF_IDLE
            AirfieldImageNames:'images/landingfield/ch_precision.jpg', // CH_PF_IDLE
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
            PrintCalculatorValues:"A,-1,1,D,E,F,G,H",     // CH_PF_IDLE
    ]),

    CH_PF_IDLE_NO_FLAPS ([
            LongRuleTitle:"Wettkampfreglement Pr\u00E4zisionsflug-Schweizermeisterschaft - Ausgabe 2017 - Simulierte Notlandung ohne Flaps", // CH_PF_IDLE_NO_FLAPS
            ShortRuleTitle:"Simulierte Notlandung ohne Flaps", // CH_PF_IDLE_NO_FLAPS
            AirfieldImageNames:'images/landingfield/ch_precision.jpg', // CH_PF_IDLE_NO_FLAPS
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
            PrintCalculatorValues:"A,-1,1,D,E,F,G,H",     // CH_PF_IDLE_NO_FLAPS
    ]),

    AT_PF1_NORMAL ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 1 - Normallandung", // AT_PF1_NORMAL
            ShortRuleTitle:"Normallandung",               // AT_PF1_NORMAL
            AirfieldImageNames:'images/landingfield/at_precision_landing1.jpg', // AT_PF1_NORMAL
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
            PrintCalculatorValues:"C,A,I,II,III",         // AT_PF1_NORMAL
    ]),
        
    AT_PF1_OBSTACLE ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 1 - Hindernislandung", // AT_PF1_OBSTACLE
            ShortRuleTitle:"Hindernislandung",            // AT_PF1_OBSTACLE
            AirfieldImageNames:'images/landingfield/at_precision_landing1.jpg', // AT_PF1_OBSTACLE
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
            PrintCalculatorValues:"C,A,I,II,III",         // AT_PF1_OBSTACLE
    ]),

    AT_PF1_IDLE ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 1 - Signallandung", // AT_PF1_IDLE
            ShortRuleTitle:"Signallandung",               // AT_PF1_IDLE
            AirfieldImageNames:'images/landingfield/at_precision_landing1.jpg', // AT_PF1_IDLE
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
            PrintCalculatorValues:"C,A,I,II,III",         // AT_PF1_IDLE
    ]),
        
    AT_PF1_IDLE_NO_FLAPS ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 1 - Signallandung ohne Klappen", // AT_PF1_IDLE_NO_FLAPS
            ShortRuleTitle:"Signallandung ohne Klappen",  // AT_PF1_IDLE_NO_FLAPS
            AirfieldImageNames:'images/landingfield/at_precision_landing1.jpg', // AT_PF1_IDLE_NO_FLAPS
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
            PrintCalculatorValues:"C,A,I,II,III",         // AT_PF1_IDLE_NO_FLAPS
    ]),
        
    AT_PF2_NORMAL ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 2 - Normallandung", // AT_PF2_NORMAL
            ShortRuleTitle:"Normallandung",               // AT_PF2_NORMAL
            AirfieldImageNames:'images/landingfield/at_precision_landing2.jpg', // AT_PF2_NORMAL
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
            PrintCalculatorValues:"C,B,A,-5,5,I,II,III,IV,V,VI", // AT_PF2_NORMAL
    ]),

    AT_PF2_OBSTACLE ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 2 - Hindernislandung", // AT_PF2_OBSTACLE
            ShortRuleTitle:"Hindernislandung",            // AT_PF2_OBSTACLE
            AirfieldImageNames:'images/landingfield/at_precision_landing2.jpg', // AT_PF2_OBSTACLE
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
            PrintCalculatorValues:"C,B,A,-5,5,I,II,III,IV,V,VI", // AT_PF2_OBSTACLE
    ]),

    AT_PF2_IDLE ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 2 - Signallandung", // AT_PF2_IDLE
            ShortRuleTitle:"Signallandung",               // AT_PF2_IDLE
            AirfieldImageNames:'images/landingfield/at_precision_landing2.jpg', // AT_PF2_IDLE
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
            PrintCalculatorValues:"C,B,A,-5,5,I,II,III,IV,V,VI", // AT_PF2_IDLE
    ]),

    AT_PF2_IDLE_NO_FLAPS ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 2 - Signallandung ohne Klappen", // AT_PF2_IDLE_NO_FLAPS
            ShortRuleTitle:"Signallandung ohne Klappen",  // AT_PF2_IDLE_NO_FLAPS
            AirfieldImageNames:'images/landingfield/at_precision_landing2.jpg', // AT_PF2_IDLE_NO_FLAPS
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
            PrintCalculatorValues:"C,B,A,-5,5,I,II,III,IV,V,VI", // AT_PF2_IDLE_NO_FLAPS
    ]),

    AT_PF3_NORMAL ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 3 - Normallandung", // AT_PF3_NORMAL
            ShortRuleTitle:"Normallandung",               // AT_PF3_NORMAL
            AirfieldImageNames:'images/landingfield/at_precision_landing3.jpg', // AT_PF3_NORMAL
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
            PrintCalculatorValues:"-20,-10,-1,1,10,20,30",// AT_PF3_NORMAL
    ]),

    AT_PF3_OBSTACLE ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 3 - Hindernislandung", // AT_PF3_OBSTACLE
            ShortRuleTitle:"Hindernislandung",            // AT_PF3_OBSTACLE
            AirfieldImageNames:'images/landingfield/at_precision_landing3.jpg', // AT_PF3_OBSTACLE
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
            PrintCalculatorValues:"-20,-10,-1,1,10,20,30",// AT_PF3_OBSTACLE
    ]),

    AT_PF3_IDLE ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 3 - Signallandung", // AT_PF3_IDLE
            ShortRuleTitle:"Signallandung",               // AT_PF3_IDLE
            AirfieldImageNames:'images/landingfield/at_precision_landing3.jpg', // AT_PF3_IDLE
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
            PrintCalculatorValues:"-20,-10,-1,1,10,20,30",// AT_PF3_IDLE
    ]),

    AT_PF3_IDLE_NO_FLAPS ([
            LongRuleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023 - Landefeld Typ 3 - Signallandung ohne Klappen", // AT_PF3_IDLE_NO_FLAPS
            ShortRuleTitle:"Signallandung ohne Klappen",  // AT_PF3_IDLE_NO_FLAPS
            AirfieldImageNames:'images/landingfield/at_precision_landing3.jpg', // AT_PF3_IDLE_NO_FLAPS
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
            PrintCalculatorValues:"-20,-10,-1,1,10,20,30",// AT_PF3_IDLE_NO_FLAPS
    ])

	ContestRulesLanding(Map data) 
	{
		this.data = data
	}
	
	final Map data
    
    static List GetLanding1Rules()
    {
        List ret = []
        ret += DE_RF_ANR
        //ret += DE_PF
        ret += FAI_RF
        ret += FAI_PF_ANR_NORMAL
        //ret += FAI_PF_ANR_IDLE
        //ret += FAI_PF_ANR_IDLE_NO_FLAPS
        //ret += FAI_PF_ANR_OBSTACLE
        ret += CH_PF_NORMAL
        //ret += CH_PF_IDLE
        //ret += CH_PF_IDLE_NO_FLAPS
        //ret += CH_PF_OBSTACLE
        ret += AT_RF
        ret += AT_PF1_NORMAL
        //ret += AT_PF1_IDLE
        //ret += AT_PF1_IDLE_NO_FLAPS
        //ret += AT_PF1_OBSTACLE
        ret += AT_PF2_NORMAL
        //ret += AT_PF2_IDLE
        //ret += AT_PF2_IDLE_NO_FLAPS
        //ret += AT_PF2_OBSTACLE
        ret += AT_PF3_NORMAL
        //ret += AT_PF3_IDLE
        //ret += AT_PF3_IDLE_NO_FLAPS        
        //ret += AT_PF3_OBSTACLE
        return ret
    }
    
    static List GetLanding2Rules()
    {
        List ret = []
        ret += DE_RF_ANR
        //ret += DE_PF
        ret += FAI_RF
        //ret += FAI_PF_ANR_NORMAL
        ret += FAI_PF_ANR_IDLE
        //ret += FAI_PF_ANR_IDLE_NO_FLAPS
        //ret += FAI_PF_ANR_OBSTACLE
        //ret += CH_PF_NORMAL
        ret += CH_PF_IDLE
        //ret += CH_PF_IDLE_NO_FLAPS
        //ret += CH_PF_OBSTACLE
        ret += AT_RF
        //ret += AT_PF1_NORMAL
        ret += AT_PF1_IDLE
        //ret += AT_PF1_IDLE_NO_FLAPS
        //ret += AT_PF1_OBSTACLE
        //ret += AT_PF2_NORMAL
        ret += AT_PF2_IDLE
        //ret += AT_PF2_IDLE_NO_FLAPS
        //ret += AT_PF2_OBSTACLE
        //ret += AT_PF3_NORMAL
        ret += AT_PF3_IDLE
        //ret += AT_PF3_IDLE_NO_FLAPS        
        //ret += AT_PF3_OBSTACLE
        return ret
    }
    
    static List GetLanding3Rules()
    {
        List ret = []
        ret += DE_RF_ANR
        //ret += DE_PF
        ret += FAI_RF
        //ret += FAI_PF_ANR_NORMAL
        //ret += FAI_PF_ANR_IDLE
        ret += FAI_PF_ANR_IDLE_NO_FLAPS
        //ret += FAI_PF_ANR_OBSTACLE
        //ret += CH_PF_NORMAL
        //ret += CH_PF_IDLE
        ret += CH_PF_IDLE_NO_FLAPS
        //ret += CH_PF_OBSTACLE
        ret += AT_RF
        //ret += AT_PF1_NORMAL
        //ret += AT_PF1_IDLE
        ret += AT_PF1_IDLE_NO_FLAPS
        //ret += AT_PF1_OBSTACLE
        //ret += AT_PF2_NORMAL
        //ret += AT_PF2_IDLE
        ret += AT_PF2_IDLE_NO_FLAPS
        //ret += AT_PF2_OBSTACLE
        //ret += AT_PF3_NORMAL
        //ret += AT_PF3_IDLE
        ret += AT_PF3_IDLE_NO_FLAPS        
        //ret += AT_PF3_OBSTACLE
        return ret
    }
    
    static List GetLanding4Rules()
    {
        List ret = []
        ret += DE_RF_ANR
        //ret += DE_PF
        ret += FAI_RF
        //ret += FAI_PF_ANR_NORMAL
        //ret += FAI_PF_ANR_IDLE
        //ret += FAI_PF_ANR_IDLE_NO_FLAPS
        ret += FAI_PF_ANR_OBSTACLE
        //ret += CH_PF_NORMAL
        //ret += CH_PF_IDLE
        //ret += CH_PF_IDLE_NO_FLAPS
        ret += CH_PF_OBSTACLE
        ret += AT_RF
        //ret += AT_PF1_NORMAL
        //ret += AT_PF1_IDLE
        //ret += AT_PF1_IDLE_NO_FLAPS
        ret += AT_PF1_OBSTACLE
        //ret += AT_PF2_NORMAL
        //ret += AT_PF2_IDLE
        //ret += AT_PF2_IDLE_NO_FLAPS
        ret += AT_PF2_OBSTACLE
        //ret += AT_PF3_NORMAL
        //ret += AT_PF3_IDLE
        //ret += AT_PF3_IDLE_NO_FLAPS        
        ret += AT_PF3_OBSTACLE
        return ret
    }
}