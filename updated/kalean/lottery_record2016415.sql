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

/*Table structure for table `tmp_lottery_record` */

CREATE TABLE `tmp_lottery_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(60) NOT NULL,
  `createDate` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `ticket` varchar(60) DEFAULT NULL,
  `lotteryId` int(11) DEFAULT NULL,
  `awardId` int(11) DEFAULT 0,
  `amount` float DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `isActive` int(11) DEFAULT NULL,
  `mark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
