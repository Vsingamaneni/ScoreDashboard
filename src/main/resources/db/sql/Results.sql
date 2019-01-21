CREATE TABLE `RESULTS` (
  `matchNumber` int(11) NOT NULL,
  `homeTeam` varchar(45) DEFAULT NULL,
  `awayTeam` varchar(45) DEFAULT NULL,
  `startDate` varchar(45) DEFAULT NULL,
  `winner` varchar(45) DEFAULT NULL,
  `winningAmount` decimal(10,2) DEFAULT NULL,
  `homeTeamCount` int(11) DEFAULT NULL,
  `awayTeamCount` int(11) DEFAULT NULL,
  `drawTeamCount` int(11) DEFAULT NULL,
  `notPredictedCount` int(11) DEFAULT NULL,
  `matchDay` int(11) DEFAULT NULL,
  PRIMARY KEY (`matchNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
