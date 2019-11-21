
-- 案例库表（）
CREATE TABLE `nm_case_library_cur_val` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `case_library_field` varchar(50) DEFAULT NULL COMMENT '所属领域：专家池大类名-专家池小类名',
  `case_library_title` varchar(500) DEFAULT NULL COMMENT '帖子题目',
  `case_library_content` longtext COMMENT '帖子内容',
  `enclosure` varchar(2000) DEFAULT NULL COMMENT '附件',
  `publisher` varchar(30) DEFAULT NULL COMMENT '发布人员用户id',
  `publisher_cn` varchar(20) DEFAULT NULL COMMENT '发布人员中文名',
  `update_by` varchar(30) DEFAULT NULL COMMENT '修改人用户id',
  `update_by_cn` varchar(10) DEFAULT NULL COMMENT '修改人中文名',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `created` datetime DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '案例库表';

-- 历史表
CREATE TABLE `nm_case_library_his_val` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `case_library_id` int(11) NOT NULL COMMENT '关联id',
  `state` varchar(30) DEFAULT NULL COMMENT '状态（保存，更新，删除）',
  `case_library_field` varchar(50) DEFAULT NULL COMMENT '所属领域：专家池大类名-专家池小类名',
  `case_library_title` varchar(500) DEFAULT NULL COMMENT '帖子题目',
  `case_library_content` longtext COMMENT '帖子内容',
  `enclosure` varchar(2000) DEFAULT NULL COMMENT '附件',
  `publisher` varchar(30) DEFAULT NULL COMMENT '发布人员用户id',
  `publisher_cn` varchar(20) DEFAULT NULL COMMENT '发布人员中文名',
  `update_by` varchar(30) DEFAULT NULL COMMENT '修改人用户id',
  `update_by_cn` varchar(10) DEFAULT NULL COMMENT '修改人中文名',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '历史表';

-- 草稿表
CREATE TABLE `nm_case_library_draft_val` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `case_library_field` varchar(50) DEFAULT NULL COMMENT '所属领域：专家池大类名-专家池小类名',
  `case_library_title` varchar(500) DEFAULT NULL COMMENT '帖子题目',
  `case_library_content` longtext COMMENT '帖子内容',
  `enclosure` varchar(2000) DEFAULT NULL COMMENT '附件',
  `publisher` varchar(30) DEFAULT NULL COMMENT '发布人员用户id',
  `publisher_cn` varchar(20) DEFAULT NULL COMMENT '发布人员中文名',
  `update_by` varchar(30) DEFAULT NULL COMMENT '修改人用户id',
  `update_by_cn` varchar(10) DEFAULT NULL COMMENT '修改人中文名',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '草稿表';

-- 收藏表
CREATE TABLE `nm_case_library_collected_val` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `case_library_id` int(11) DEFAULT NULL COMMENT '案例库id',
  `created_by` varchar(30) DEFAULT NULL COMMENT '收藏人用户id',
  `created_by_cn` varchar(10) DEFAULT NULL COMMENT '收藏人中文名',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '收藏表';

-- 评论表
CREATE TABLE `nm_case_library_comment_val` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `case_library_id` int(11) DEFAULT NULL COMMENT '案例库id',
  `case_id` int(11) DEFAULT NULL COMMENT '评论id(默认-1)',
  `case_content` text COMMENT '评论内容',
  `created_by` varchar(30) DEFAULT NULL COMMENT '评论人用户id',
  `created_by_cn` varchar(10) DEFAULT NULL COMMENT '评论人中文名',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '评论表';


-- sql 配置表
CREATE TABLE `case_base_sql` (
  `sql_id` varchar(255) NOT NULL COMMENT 'sqlId,',
  `sql_name` varchar(255) DEFAULT NULL COMMENT 'sql名称',
  `sql_desc` varchar(255) DEFAULT NULL COMMENT 'sql说明',
  `sql_type` varchar(255) DEFAULT NULL COMMENT 'sql类型',
  `sql` longtext NOT NULL COMMENT 'sql内容',
  `params` varchar(1000) DEFAULT NULL,
  `dbstr` varchar(64) DEFAULT NULL COMMENT '数据源配置名称',
  `status` varchar(16) DEFAULT NULL COMMENT '状态',
  `oper_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `oper_by` varchar(30) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`sql_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'sql配置表';