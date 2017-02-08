CREATE TABLE `questionnaire` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`uuid` VARCHAR(60) NULL DEFAULT NULL,
	`createDate` DATETIME NULL DEFAULT NULL,
	`modifyDate` DATETIME NULL DEFAULT NULL,
	`name` VARCHAR(100) NULL DEFAULT NULL,
	`pinyin` VARCHAR(250) NULL DEFAULT NULL,
	`status` INT(1) NULL DEFAULT NULL,
	`isActive` INT(1) NULL DEFAULT NULL,
	`mark` VARCHAR(250) NULL DEFAULT NULL COMMENT '简介',
	`qProperty` TEXT NULL COMMENT '封面背景属性',
	`urlLink` VARCHAR(250) NULL DEFAULT NULL COMMENT '问卷网链接及问卷网的问卷id',
	PRIMARY KEY (`id`)
)
COMMENT='问卷网问卷'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `questionnaire_answer` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`uuid` VARCHAR(60) NULL DEFAULT NULL,
	`createDate` DATETIME NULL DEFAULT NULL,
	`modifyDate` DATETIME NULL DEFAULT NULL,
	`userId` INT(11) NULL DEFAULT NULL,
	`questionnaireId` INT(11) NULL DEFAULT NULL COMMENT '问卷id',
	`answer` TEXT NULL COMMENT '问卷网的答案',
	`verdict` VARCHAR(250) NULL DEFAULT NULL COMMENT '对答案的结论',
	`score` INT(11) NULL DEFAULT NULL COMMENT '对答案的分数',
	`status` INT(11) NULL DEFAULT NULL,
	`isActive` INT(11) NULL DEFAULT NULL,
	`mark` VARCHAR(150) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_questionnaire_answer_user` (`userId`),
	INDEX `FK_questionnaire_answer_questionnaire` (`questionnaireId`),
	CONSTRAINT `FK_questionnaire_answer_questionnaire` FOREIGN KEY (`questionnaireId`) REFERENCES `questionnaire` (`id`),
	CONSTRAINT `FK_questionnaire_answer_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
)
COMMENT='问卷网的答案'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
