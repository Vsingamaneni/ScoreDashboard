CREATE TABLE `PREDICTIONS` (
  `predictionId` int(11) NOT NULL AUTO_INCREMENT,
  `memberId` int(11) NOT NULL,
  `matchNumber` int(11) NOT NULL,
  `homeTeam` varchar(45) DEFAULT NULL,
  `awayTeam` varchar(45) DEFAULT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `selected` varchar(45) DEFAULT NULL,
  `predictedTime` varchar(45) DEFAULT NULL,
  `matchDay` int(11) DEFAULT NULL,
  PRIMARY KEY ( `memberId` ,  `matchNumber`),
  KEY `predictionId` (`predictionId`),
  KEY `memberId` (`memberId`),
  KEY `matchNumber` (`matchNumber`),
  CONSTRAINT `predictions_ibfk_1` FOREIGN KEY (`memberId`) REFERENCES `REGISTER` (`memberId`),
  CONSTRAINT `predictions_ibfk_2` FOREIGN KEY (`matchNumber`) REFERENCES `SCHEDULE` (`matchNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;