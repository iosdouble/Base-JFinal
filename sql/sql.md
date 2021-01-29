## 默认数据库表
```sql
CREATE TABLE `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
```
## 用户数据表V1.0
```sql
CREATE TABLE `app_users` (
  `id` varchar(32) NOT NULL,
  `lt_nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `lt_user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `lt_sync_id` varchar(50) DEFAULT NULL COMMENT '同步id',
  `lt_real_name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `lt_org_name` varchar(50) DEFAULT NULL COMMENT '隶属党组织的名称',
  `lt_org_sync_id` varchar(50) DEFAULT NULL COMMENT '隶属党组织的同步ID',
  `community_id` varchar(50) DEFAULT NULL COMMENT '所属社区id',
  `community_name` varchar(50) DEFAULT NULL COMMENT '所属社区名称',
  `total_points` double DEFAULT NULL COMMENT '年度累计积分',
  `current_points` double DEFAULT NULL COMMENT '现有积分',
  `lt_account_type` varchar(20) DEFAULT NULL COMMENT '（0为党员，1为组织，2为群众）',
  `wx_union_id` varchar(80) DEFAULT NULL COMMENT '微信unionID',
  `wx_open_id` varchar(80) DEFAULT NULL COMMENT '微信openID',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `sex` varchar(10) DEFAULT NULL COMMENT '性别',
  `wx_nick_name` varchar(50) DEFAULT NULL COMMENT '微信昵称',
  `wx_avatarUrl` varchar(200) DEFAULT NULL COMMENT '微信头像url',
  `wx_country` varchar(50) DEFAULT NULL COMMENT '微信所在国家',
  `wx_province` varchar(50) DEFAULT NULL COMMENT '微信所在省',
  `wx_city` varchar(50) DEFAULT NULL COMMENT '微信所在城市',
  `wx_language` varchar(50) DEFAULT NULL COMMENT '微信所用语言',
  `last_login_time` varchar(20) DEFAULT NULL COMMENT '某天首次打开微信首页时记录，一天只记录一次',
  `login_count` varchar(20) DEFAULT NULL COMMENT '累计登录次数',
  `address_province` varchar(200) DEFAULT NULL COMMENT '省(住址)',
  `address_city` varchar(200) DEFAULT NULL COMMENT '市(住址)',
  `address_area` varchar(200) DEFAULT NULL COMMENT '区县(住址)',
  `address_street` varchar(200) DEFAULT NULL COMMENT '街道(住址)',
  `address_community` varchar(200) DEFAULT NULL COMMENT '社区(住址)',
  `address_quarters_id` varchar(200) DEFAULT NULL COMMENT '小区(住址)',
  `address_floor` varchar(200) DEFAULT NULL COMMENT '楼号(住址)',
  `address_unit` varchar(200) DEFAULT NULL COMMENT '单元号(住址)',
  `address_number` varchar(200) DEFAULT NULL COMMENT '门牌号(住址)',
  `is_resign` varchar(50) DEFAULT NULL COMMENT '是否注册',
  `is_grid_user` varchar(50) DEFAULT NULL COMMENT '是否为网格员',
  `spare1` varchar(200) DEFAULT NULL COMMENT '备用1',
  `spare2` varchar(200) DEFAULT NULL COMMENT '备用1',
  `spare4` varchar(200) DEFAULT NULL COMMENT '备用1',
  `spare3` varchar(200) DEFAULT NULL COMMENT '备用1',
  `spare5` varchar(200) DEFAULT NULL COMMENT '备用1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='移动端用户';
```

