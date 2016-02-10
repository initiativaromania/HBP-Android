# ************************************************************
# Sequel Pro SQL dump
# Version 4499
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.9)
# Database: ir-investitii
# Generation Time: 2016-02-10 17:13:33 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table contracte
# ------------------------------------------------------------

CREATE TABLE `contracte` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `location_name` varchar(200) DEFAULT NULL,
  `location_lat` double NOT NULL,
  `location_lng` double NOT NULL,
  `company` varchar(200) NOT NULL DEFAULT '',
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `categories` varchar(200) DEFAULT NULL,
  `description` varchar(200) DEFAULT '',
  `price` int(11) NOT NULL,
  `currency` varchar(11) NOT NULL DEFAULT '',
  `justify` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `lat/lng` (`location_lat`,`location_lng`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
