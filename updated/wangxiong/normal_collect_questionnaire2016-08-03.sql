

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `normal_collect_questionnaire`
-- ----------------------------
DROP TABLE IF EXISTS `normal_collect_questionnaire`;
CREATE TABLE `normal_collect_questionnaire` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(80) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `mark` varchar(500) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `isActive` int(1) DEFAULT '1',
  `normalCollectId` int(11) DEFAULT NULL COMMENT '章节ID',
  `questionnaireId` int(11) DEFAULT NULL COMMENT '问卷ID',
  `type` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
