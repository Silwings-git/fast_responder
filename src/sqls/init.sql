-- 注意! MySql版本应不低于8.0
CREATE TABLE `responder_info_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(255) NOT NULL COMMENT '配置名称',
  `key_url` varchar(255) DEFAULT NULL COMMENT '关键地址(要模拟的接口地址)',
  `http_method` varchar(8) DEFAULT NULL COMMENT '请求方式.GET,POST,PUT,DELETE等',
  `category_name` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `enable_status` tinyint(1) DEFAULT '0' COMMENT '启用状态.0-禁用 1-启用',
  `logic_delete` tinyint(1) DEFAULT '0' COMMENT '删除状态.0-正常 1-删除',
  `data_json` json DEFAULT NULL COMMENT '配置信息数据',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_responderinfoconfig_keyurl` (`key_url`) USING BTREE,
  KEY `idx_responderinfoconfig_httpmethod` (`http_method`) USING BTREE
) ENGINE=InnoDB COMMENT='应答器配置表';

-- 示例应答器
INSERT INTO `responder_info_config`(`name`, `key_url`, `http_method`, `category_name`,`data_json`) VALUES
('Demo config', '/demo/url/{name}/{age}', 'GET', '示例配置', '{\"name\": \"Demo config\", \"tasks\": [{\"name\": \"My http task\", \"content\": {\"body\": {\"id\": \"-UUID()-\", \"name\": \"${name}\", \"timestemp\": \"-TSNow(ms)-\"}, \"params\": {\"keyA\": [\"key_Av1\", \"keyA_v2\"], \"keyB\": [\"keyB_v1\"]}, \"headers\": {\"authToken\": \"8888888888\"}, \"httpMethod\": \"POST\", \"requestUrl\": \"http://localhost:8088/hello/word\"}, \"delayTime\": 2000, \"conditions\": [\"1 == 1\", \"${age} >= 10\", \"name =IsNotBlank=\"]}], \"keyUrl\": \"/demo/url/{name}/{age}\", \"results\": [{\"msg\": \"Hello Word !\", \"headers\": {\"authToken\": \"-TSFNow(yyyy-MM-dd HH:mm:ss)-\"}, \"conditions\": [\"${age} == 18\"], \"resultName\": \"My Result A\"}, {\"body\": {\"fast\": \"responder\"}, \"headers\": {}, \"conditions\": [\"${age} == 15\"], \"resultName\": \"My Result B\"}], \"httpMethod\": \"GET\", \"categoryName\": \"示例配置\"}');
