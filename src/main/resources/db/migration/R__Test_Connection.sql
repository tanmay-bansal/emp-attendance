CREATE TABLE if not exists empattance.`employee` (
  `emp_id` varchar(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `emp_name` varchar(32) DEFAULT NULL,
  `emp_address` varchar(100) DEFAULT NULL,
  `mob_num` varchar(20) DEFAULT NULL,
  `designation` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`emp_id`)
);

CREATE TABLE if not exists empattance.`attendance` (
  `att_id` varchar(120) NOT NULL,
  `emp_id` varchar(50) DEFAULT NULL,
  `in_time` datetime DEFAULT NULL,
  `out_time` datetime DEFAULT NULL,
  PRIMARY KEY (`att_id`)
);