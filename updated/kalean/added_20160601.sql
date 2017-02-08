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

/*Table structure for table `normal_collect` */

CREATE TABLE `normal_collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(60) NOT NULL,
  `createDate` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `name` varchar(60) DEFAULT NULL,
  `pinyin` varchar(100) DEFAULT NULL,
  `description` text,
  `level` int(11) DEFAULT NULL,
  `sign` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `speakerId` int(11) DEFAULT NULL COMMENT '讲者归属',
  `version` float DEFAULT NULL,
  `isActive` int(11) DEFAULT NULL,
  `mark` varchar(200) DEFAULT NULL COMMENT '备注',
  `passScore` int(11) DEFAULT NULL COMMENT '及格分数',
  `parentId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
