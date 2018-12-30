CREATE TABLE `SCHEDULE` (
  `matchNumber` int(11) NOT NULL AUTO_INCREMENT,
  `homeTeam` varchar(45) DEFAULT NULL,
  `awayTeam` varchar(45) DEFAULT NULL,
  `startDate` varchar(45) DEFAULT NULL,
  `deadline` varchar(45) DEFAULT NULL,
  `utcStartDate` varchar(45) DEFAULT NULL,
  `utcFormatDate` varchar(45) DEFAULT NULL,
  `winner` varchar(45) DEFAULT NULL,
  `possibleResult` int(3) DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT NULL,
  `matchFee` int(11) DEFAULT NULL,
  `matchDay` int(11) DEFAULT NULL,
  PRIMARY KEY (`matchNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

