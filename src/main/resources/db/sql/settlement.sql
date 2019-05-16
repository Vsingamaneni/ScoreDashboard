CREATE TABLE `STANDINGS` (
  `memberId` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `net` decimal(10,2) DEFAULT NULL,
  `deduct` decimal(10,2) DEFAULT NULL,
  `add` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (memberId),
  KEY `memberId` (`memberId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
