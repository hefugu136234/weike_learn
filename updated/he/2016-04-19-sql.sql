CREATE TABLE `qrscene` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`uuid` VARCHAR(60) NULL DEFAULT NULL,
	`createDate` DATETIME NULL DEFAULT NULL,
	`modifyDate` DATETIME NULL DEFAULT NULL,
	`name` VARCHAR(200) NULL DEFAULT NULL COMMENT '二维码场景值名称',
	`sceneid` BIGINT(20) NULL DEFAULT NULL COMMENT '微信临时二维码的场景id',
	`limitType` INT(1) NULL DEFAULT NULL COMMENT '临时永久类型 1代表永久 2代表临时',
	`type` INT(2) NULL DEFAULT NULL COMMENT '活动二维码的类型',
	`reflectId` INT(11) UNSIGNED NULL DEFAULT NULL,
	`businessId` INT(2) UNSIGNED NULL DEFAULT NULL COMMENT '默认业务为0',
	`mark` VARCHAR(250) NULL DEFAULT NULL COMMENT '描述',
	`status` INT(1) NULL DEFAULT NULL,
	`isActive` INT(1) NULL DEFAULT NULL,
	`message` TEXT NULL COMMENT 'json字符串',
	`pinyin` VARCHAR(200) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `sceneid` (`sceneid`)
)
COMMENT='二维码的场景'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `qrcode_scan_recode` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`uuid` VARCHAR(60) NULL DEFAULT NULL,
	`createDate` DATETIME NULL DEFAULT NULL,
	`modifyDate` DATETIME NULL DEFAULT NULL,
	`userId` INT(11) NULL DEFAULT NULL,
	`qrsceneId` INT(11) NULL DEFAULT NULL,
	`scancount` INT(11) NULL DEFAULT NULL COMMENT '扫描二维码的次数',
	`viewcount` INT(11) NULL DEFAULT NULL COMMENT '查看的次数',
	`status` INT(11) NULL DEFAULT NULL,
	`isActive` INT(11) NULL DEFAULT NULL,
	`mark` VARCHAR(200) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_qrcode_scan_recode_user` (`userId`),
	INDEX `FK_qrcode_scan_recode_qrscene` (`qrsceneId`),
	CONSTRAINT `FK_qrcode_scan_recode_qrscene` FOREIGN KEY (`qrsceneId`) REFERENCES `qrscene` (`id`),
	CONSTRAINT `FK_qrcode_scan_recode_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
)
COMMENT='二维码的扫描查看记录'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
