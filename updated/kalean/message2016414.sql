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

/*Table structure for table `message` */

CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) DEFAULT NULL,
  `body` varchar(250) DEFAULT NULL,
  `referId` int(11) DEFAULT NULL COMMENT '关联的id',
  `referType` int(11) DEFAULT NULL COMMENT '关联的表',
  `type` int(11) DEFAULT NULL COMMENT '消息类型',
  `uuid` varchar(64) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `mark` varchar(100) DEFAULT NULL,
  `isActive` int(1) DEFAULT '1' COMMENT '是否可用',
  `sign` int(11) DEFAULT NULL COMMENT '对应的类别代码',
  `praise` int(11) DEFAULT '0' COMMENT '点赞数',
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
