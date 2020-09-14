CREATE TABLE `employee` (
  `emp_id` varchar(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `emp_name` varchar(32) DEFAULT NULL,
  `emp_address` varchar(100) DEFAULT NULL,
  `mob_num` int(11) DEFAULT NULL,
  `designation` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`emp_id`)
)