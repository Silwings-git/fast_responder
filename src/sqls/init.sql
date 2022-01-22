-- 注意! MySql版本应不低于8.0
CREATE TABLE `responder_info_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(255) NOT NULL COMMENT '配置名称',
  `key_url` varchar(255) DEFAULT NULL COMMENT '关键地址(要模拟的接口地址)',
  `http_method` varchar(8) DEFAULT NULL COMMENT '请求方式.GET,POST,PUT,DELETE等',
  `category_name` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `enable_status` tinyint(1) DEFAULT '0' COMMENT '启用状态.0-禁用 1-启用',
  `data_json` json DEFAULT NULL COMMENT '配置信息数据',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `logic_delete` tinyint(1) DEFAULT '0' COMMENT '删除状态.0-正常 1-删除',
  PRIMARY KEY (`id`),
  KEY `idx_responderinfoconfig_keyurl` (`key_url`) USING BTREE,
  KEY `idx_responderinfoconfig_httpmethod` (`http_method`) USING BTREE
) ENGINE=InnoDB COMMENT='应答器配置表';

-- 示例应答器
INSERT INTO `responder_info_config`(`name`, `key_url`, `http_method`, `category_name`,`data_json`) VALUES
('无任务无条件Get请求返回url中的name', '/do/{name}', 'GET', 'Test220119', '{\"name\": \"无任务无条件Get请求返回url中的name\", \"tasks\": [], \"keyUrl\": \"/do/{name}\", \"results\": [{\"body\": {\"name\": \"${name}\", \"timestemp\": \"-TSFNow()-\"}, \"resultName\": \"ResultA\"}], \"httpMethod\": \"GET\", \"categoryName\": \"Test220119\"}')
,('无任务无条件Post请求返回url中的name', '/do/{name}', 'POST', 'Test220119', '{\"name\": \"无任务无条件Post请求返回url中的name\", \"tasks\": [], \"keyUrl\": \"/do/{name}\", \"results\": [{\"body\": {\"name\": \"${name}\", \"timestemp\": \"-TSFNow(yyyy-MM-dd HH:mm:ss)-\"}, \"resultName\": \"ResultA\"}], \"httpMethod\": \"POST\", \"categoryName\": \"Test220119\"}')
,('无任务无条件Post请求返回请求体中的name', '/do', 'POST', 'Test220119','{\"name\": \"无任务无条件Post请求返回请求体中的name\", \"tasks\": [], \"keyUrl\": \"/do\", \"results\": [{\"body\": {\"name\": \"${name}\", \"timestemp\": \"-TSFNow(yyyy/MM/dd)-\"}, \"resultName\": \"ResultA\"}], \"httpMethod\": \"POST\", \"categoryName\": \"Test220119\"}')
,( '无任务无条件Post请求返回请求体中的指定层级的name', '/assign', 'POST', 'Test220119',  '{\"name\": \"无任务无条件Post请求返回请求体中的指定层级的name\", \"tasks\": [], \"keyUrl\": \"/assign\", \"results\": [{\"body\": {\"name\": \"${book.name}\", \"timestemp\": \"-TSNow(s)-\"}, \"resultName\": \"ResultA\"}], \"httpMethod\": \"POST\", \"categoryName\": \"Test220119\"}')
,('无任务无条件Get请求Params中的name', '/form', 'GET', 'Test220119', '{\"name\": \"无任务无条件Get请求Params中的name\", \"tasks\": [], \"keyUrl\": \"/form\", \"results\": [{\"body\": {\"name\": \"${name}\", \"timestemp\": \"-UUID()-\"}, \"resultName\": \"ResultA\"}], \"httpMethod\": \"GET\", \"categoryName\": \"Test220119\"}')
,( 'Get条件返回', '/get/cd', 'GET', 'Test220119','{\"name\": \"Get条件返回\", \"tasks\": [], \"keyUrl\": \"/get/cd\", \"results\": [{\"body\": {\"name\": \"${name}\", \"timestemp\": \"-TSFNow()-\"}, \"conditions\": [\"${name} == 小明\"], \"resultName\": \"ResultA\"}, {\"msg\": \"Hello Word !\", \"headers\": {\"authToken\": \"-RDInt()-\"}, \"conditions\": [\"${name} != 小明\"], \"resultName\": \"My Result B\"}], \"httpMethod\": \"GET\", \"categoryName\": \"Test220119\"}')
,( 'Post概率默认返回', '/post/cd', 'POST', 'Test220119','{\"name\": \"Post概率默认返回\", \"tasks\": [], \"keyUrl\": \"/post/cd\", \"results\": [{\"body\": {\"name\": \"${name}\", \"timestemp\": \"-TSFNow()-\"}, \"conditions\": [\"-RDBoolean()- == true\"], \"resultName\": \"ResultA\"}, {\"msg\": \"Hello Word !\", \"headers\": {\"authToken\": \"-RDInt()-\"}, \"conditions\": [], \"resultName\": \"default\"}], \"httpMethod\": \"POST\", \"categoryName\": \"Test220119\"}')
,( 'Post JsonPath测试A', '/post/jsonpath', 'POST', 'Test220119','{\"name\": \"Post JsonPath测试A\", \"tasks\": [], \"keyUrl\": \"/post/jsonpath\", \"results\": [{\"body\": {\"name\": \"${$.name}\", \"timestemp\": \"-TSFNow()-\"}, \"conditions\": [], \"resultName\": \"ResultA\"}], \"delayTime\": 0, \"httpMethod\": \"POST\", \"categoryName\": \"Test220122\"}')
;