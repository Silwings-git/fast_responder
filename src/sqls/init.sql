-- 注意! 如果MySql版本低于8.0,需要将json类型修改为字符类型,建议给定足够长度或使用text类型
CREATE TABLE `request_config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `class_name` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `name` varchar(255) NOT NULL COMMENT '配置名称',
  `key_url` varchar(255) DEFAULT NULL COMMENT '关键地址(要模拟的接口地址)',
  `request_method` varchar(8) DEFAULT NULL COMMENT '请求方式.GET,POST,PUT,DELETE等',
  `enable_status` tinyint(1) DEFAULT '0' COMMENT '启用状态.0-禁用 1-启用',
  `logic_delete` tinyint(1) DEFAULT '0' COMMENT '删除状态.0-正常 1-删除',
  `data_json` json DEFAULT NULL COMMENT '配置信息数据',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_requestconfiginfo_keyurl_requestmethod` (`key_url`,`request_method`) USING BTREE COMMENT 'url与请求方式组合唯一'
) ENGINE=InnoDB COMMENT='请求信息配置表';