-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.18-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for potlocator
CREATE DATABASE IF NOT EXISTS `potlocator` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `potlocator`;

-- Dumping structure for table potlocator.potlocation
CREATE TABLE IF NOT EXISTS `potlocation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `latitude` decimal(10,8) NOT NULL,
  `longitude` decimal(11,8) NOT NULL,
  `reported_by` int(10) unsigned NOT NULL,
  `reported_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_repaired` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`),
  KEY `FK_portlocation_user` (`reported_by`),
  CONSTRAINT `FK_portlocation_user` FOREIGN KEY (`reported_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- Dumping data for table potlocator.potlocation: ~6 rows (approximately)
/*!40000 ALTER TABLE `potlocation` DISABLE KEYS */;
INSERT INTO `potlocation` (`id`, `latitude`, `longitude`, `reported_by`, `reported_on`, `is_repaired`) VALUES
	(1, 39.78113288, -86.17321544, 2, '2017-04-27 12:47:54', 'N'),
	(2, 39.78111659, -86.17312075, 2, '2017-04-27 13:27:51', 'N'),
	(3, 40.71278370, -74.00594130, 2, '2017-04-27 13:28:51', 'N'),
	(4, 36.99635200, -121.57527100, 2, '2017-04-27 13:31:29', 'N'),
	(5, 39.78113518, -86.17312863, 1, '2017-04-27 13:32:30', 'N'),
	(6, 18.52043030, 73.85674370, 1, '2017-04-27 13:33:31', 'N'),
	(7, 18.51763330, 73.86712320, 1, '2017-04-27 13:33:53', 'N'),
	(8, 39.78104284, -86.17309810, 1, '2017-04-27 20:20:04', 'N'),
	(9, 39.78121385, -86.17306394, 2, '2017-04-27 20:20:41', 'N');
/*!40000 ALTER TABLE `potlocation` ENABLE KEYS */;

-- Dumping structure for table potlocator.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(32) NOT NULL,
  `last_name` varchar(32) NOT NULL,
  `email` varchar(64) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `is_contractor` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- Dumping data for table potlocator.user: ~5 rows (approximately)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `first_name`, `last_name`, `email`, `username`, `password`, `is_contractor`) VALUES
	(1, 'Soham', 'Mehta', 'soham@test.com', 'soham', 'abc', 'N'),
	(2, 'Keyur', 'Mehta', 'keyur@gmail.com', 'keyur', 'abc', 'N'),
	(3, 'dewant', 'katare', 'kewant@gmail.com', 'dewant', 'ab123', 'Y'),
	(4, 'John', 'Smith', 'john@yahoo.com', 'john', 'abc', 'N'),
	(5, 'Raj', 'Shah', 'raj@rediff.com', 'raj', 'abc', 'N'),
	(6, 'Mihit', 'Joshi', 'Mihit@gmail.com', 'mihir', 'ab123', 'Y');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
