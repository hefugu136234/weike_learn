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

/*Table structure for table `media_central` */

CREATE TABLE `media_central` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `uuid` varchar(64) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `url` varchar(150) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `mediaType` int(11) DEFAULT NULL COMMENT '类型',
  `referType` int(11) DEFAULT NULL COMMENT '关联的表',
  `referId` int(11) DEFAULT NULL COMMENT '关联的Id',
  `sign` int(11) DEFAULT NULL COMMENT '业务值',
  `status` int(11) DEFAULT '1',
  `isActive` int(11) DEFAULT '1',
  `mark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `product_group` */

CREATE TABLE `product_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(60) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `name` varchar(150) DEFAULT NULL,
  `serialNum` varchar(4) DEFAULT NULL,
  `pinyin` varchar(200) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `isActive` int(1) DEFAULT NULL,
  `mark` varchar(200) DEFAULT NULL,
  `manufacturerId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_product_group_manufacturer` (`manufacturerId`),
  CONSTRAINT `FK_product_group_manufacturer` FOREIGN KEY (`manufacturerId`) REFERENCES `manufacturer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='产品组';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
