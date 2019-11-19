CREATE TABLE `nm_case_library_cur_val` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `case_library_field` varchar(50) DEFAULT NULL COMMENT '所属领域：专家池大类名-专家池小类名',
  `case_library_title` varchar(500) DEFAULT NULL COMMENT '帖子题目',
  `case_library_content` longtext COMMENT '帖子内容',
  `enclosure` varchar(2000) DEFAULT NULL COMMENT '附件',
  `publisher` varchar(30) DEFAULT NULL COMMENT '发布人员用户id',
  `publisher_cn` varchar(20) DEFAULT NULL COMMENT '发布人员中文名',
  `created` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(30) DEFAULT NULL COMMENT '修改人用户id',
  `update_by_cn` varchar(10) DEFAULT NULL COMMENT '修改人中文名',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `nm_case_library_his_val` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `case_library_id` int(11) NOT NULL COMMENT '关联id',
  `case_library_field` varchar(50) DEFAULT NULL COMMENT '所属领域：专家池大类名-专家池小类名',
  `case_library_title` varchar(500) DEFAULT NULL COMMENT '帖子题目',
  `case_library_content` longtext COMMENT '帖子内容',
  `enclosure` varchar(2000) DEFAULT NULL COMMENT '附件',
  `publisher` varchar(30) DEFAULT NULL COMMENT '发布人员用户id',
  `publisher_cn` varchar(20) DEFAULT NULL COMMENT '发布人员中文名',
  `created` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(30) DEFAULT NULL COMMENT '修改人用户id',
  `update_by_cn` varchar(10) DEFAULT NULL COMMENT '修改人中文名',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `nm_case_library_draft_val` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `case_library_field` varchar(50) DEFAULT NULL COMMENT '所属领域：专家池大类名-专家池小类名',
  `case_library_title` varchar(500) DEFAULT NULL COMMENT '帖子题目',
  `case_library_content` longtext COMMENT '帖子内容',
  `enclosure` varchar(2000) DEFAULT NULL COMMENT '附件',
  `publisher` varchar(30) DEFAULT NULL COMMENT '发布人员用户id',
  `publisher_cn` varchar(20) DEFAULT NULL COMMENT '发布人员中文名',
  `created` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(30) DEFAULT NULL COMMENT '修改人用户id',
  `update_by_cn` varchar(10) DEFAULT NULL COMMENT '修改人中文名',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `nm_case_library_collected_val` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `case_library_id` int(11) DEFAULT NULL COMMENT '案例库id',
  `created_by` varchar(30) DEFAULT NULL COMMENT '收藏人用户id',
  `created_by_cn` varchar(10) DEFAULT NULL COMMENT '收藏人中文名',
  `created_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `nm_case_library_comment_val` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `case_library_id` int(11) DEFAULT NULL COMMENT '案例库id',
  `case_id` int(11) DEFAULT NULL COMMENT '评论id(默认-1)',
  `case_content` text COMMENT '评论内容',
  `created_by` varchar(30) DEFAULT NULL COMMENT '评论人用户id',
  `created_by_cn` varchar(10) DEFAULT NULL COMMENT '评论人中文名',
  `created_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
