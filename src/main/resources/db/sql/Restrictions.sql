CREATE TABLE `RESTRICTIONS` (
  `securityCode` varchar(45) DEFAULT NULL,
  `maxLimit` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO ipl.RESTRICTIONS
(securityCode, maxLimit)
VALUES('ROONEY', 2000);

