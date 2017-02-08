CREATE TABLE `offline_activity` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`uuid` VARCHAR(60) NULL DEFAULT NULL,
	`createDate` DATETIME NULL DEFAULT NULL,
	`modifyDate` DATETIME NULL DEFAULT NULL,
	`status` INT(11) NULL DEFAULT NULL,
	`isActive` INT(11) NULL DEFAULT NULL,
	`mark` VARCHAR(200) NULL DEFAULT NULL COMMENT '简介',
	`name` VARCHAR(100) NULL DEFAULT NULL,
	`pinyin` VARCHAR(200) NULL DEFAULT NULL,
	`description` TEXT NULL COMMENT '描述',
	`bookStartDate` DATETIME NULL DEFAULT NULL COMMENT '报名开始',
	`bookEndDate` DATETIME NULL DEFAULT NULL COMMENT '报名结束',
	`address` VARCHAR(150) NULL DEFAULT NULL,
	`enrollType` INT(11) NULL DEFAULT NULL COMMENT '报名类型',
	`limitNum` INT(11) NULL DEFAULT NULL COMMENT '报名上线',
	`cover` VARCHAR(200) NULL DEFAULT NULL,
	`price` TEXT NULL COMMENT '价格(包含价钱和积分)',
	`initiatorId` INT(11) NULL DEFAULT NULL COMMENT '线下活动的发起人',
	PRIMARY KEY (`id`),
	INDEX `FK_offline_activity_user` (`initiatorId`),
	CONSTRAINT `FK_offline_activity_user` FOREIGN KEY (`initiatorId`) REFERENCES `user` (`id`)
)
COMMENT='线下活动'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `project_code` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`uuid` VARCHAR(60) NULL DEFAULT NULL,
	`createDate` DATETIME NULL DEFAULT NULL,
	`modifyDate` DATETIME NULL DEFAULT NULL,
	`projectCode` VARCHAR(50) NULL DEFAULT NULL,
	`status` INT(11) NULL DEFAULT NULL,
	`isActive` INT(11) NULL DEFAULT NULL,
	`mark` VARCHAR(100) NULL DEFAULT NULL,
	`referId` INT(11) NULL DEFAULT NULL,
	`referType` INT(11) NULL DEFAULT NULL,
	`codeType` INT(11) NULL DEFAULT NULL,
	`userId` INT(11) NULL DEFAULT NULL,
	`activeTime` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_project_code_user` (`userId`),
	CONSTRAINT `FK_project_code_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
)
COMMENT='实体的邀请码,兑换码等等'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `signup_user` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`uuid` VARCHAR(60) NULL DEFAULT NULL,
	`createDate` DATETIME NULL DEFAULT NULL,
	`modifyDate` DATETIME NULL DEFAULT NULL,
	`status` INT(11) NULL DEFAULT NULL,
	`isActive` INT(11) NULL DEFAULT NULL,
	`mark` VARCHAR(200) NULL DEFAULT NULL,
	`referId` INT(11) NULL DEFAULT NULL,
	`referType` INT(11) NULL DEFAULT NULL,
	`userId` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_signup_user_user` (`userId`),
	CONSTRAINT `FK_signup_user_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
)
COMMENT='实体报名表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



