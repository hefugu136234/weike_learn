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

/*Table structure for table `notification` */

CREATE TABLE `notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(60) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `sendDate` datetime DEFAULT NULL,
  `body` varchar(250) DEFAULT NULL,
  `referType` int(11) DEFAULT NULL,
  `referId` int(11) DEFAULT NULL,
  `sign` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL COMMENT '创建的用户',
  `status` int(11) DEFAULT NULL,
  `isActive` int(11) DEFAULT NULL,
  `mark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_notification_user` (`userId`),
  CONSTRAINT `FK_notification_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
