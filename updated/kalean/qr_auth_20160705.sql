/*
SQLyog Enterprise - MySQL GUI v7.15 
MySQL - 5.1.35-community : Database - zhiliao
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`zhiliao` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `zhiliao`;

/*Table structure for table `qr_auth` */

CREATE TABLE `qr_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(60) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `authDate` datetime DEFAULT NULL,
  `lastScanDate` datetime DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `key` varchar(100) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `qrUrl` varchar(100) DEFAULT NULL,
  `device` varchar(50) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `isActive` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
