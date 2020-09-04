CREATE TABLE `STANDINGS` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `memberId` int(11) NOT NULL,
  `matchNumber` int(11) NOT NULL,
  `homeTeam` varchar(45) DEFAULT NULL,
  `awayTeam` varchar(45) DEFAULT NULL,
  `matchDate` varchar(45) DEFAULT NULL,
  `predictedDate` varchar(45) DEFAULT NULL,
  `selected` varchar(45) DEFAULT NULL,
  `winner` varchar(45) DEFAULT NULL,
  `wonAmount` decimal(10,2) DEFAULT NULL,
  `lostAmount` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`,`memberId`,`matchNumber`),
  KEY `memberId` (`memberId`),
  KEY `matchNumber` (`matchNumber`),
  CONSTRAINT `standings_ibfk_1` FOREIGN KEY (`memberId`) REFERENCES `REGISTER` (`memberId`),
  CONSTRAINT `standings_ibfk_2` FOREIGN KEY (`matchNumber`) REFERENCES `SCHEDULE` (`matchNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
