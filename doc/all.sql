drop table if exists `test`;
create table `test`(
    `id` bigint not null comment 'id',
    `name` varchar(50) comment '名称',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';

insert into `test` (id, name) values (1, 'wang');

drop table if exists `demo`;
create table `demo`(
                       `id` bigint not null comment 'id',
                       `name` varchar(50) comment '名称',
                       `password` varchar(50) comment '密码',
                       primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='demo';

insert into `demo` (id, name, password) values (1, 'wang', 'aaaa');

# 团队成员信息表
drop table if exists `memberInfo`;
create table `memberInfo`(
                       `id` bigint not null comment 'id',
                       `name` varchar(50) comment '姓名',
                       `email` varchar(50) comment '邮箱',
                       `category1_id` bigint comment '信息所属类别',
                       `category2_id` bigint comment '人员所属类别',
                       `description` varchar(200) comment '简介',
                       `cover` varchar(200) comment '封面',
                       `view_count` int comment '阅览数',
                       `vote_count` int comment '点赞数',
                       primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='团队成员信息表';

insert into `memberInfo` (id, email, name, description) values (1, '745661616@qq.com', '李贝贝', '特聘副研究员，博/硕士生导师，四川省高层次引进人才');
insert into `memberInfo` (id, email, name, description) values (2, '745661616@qq.com', '欧阳远凯', '2019级硕士生');
insert into `memberInfo` (id, email, name, description) values (3, '745661616@qq.com','马小霞', '2019级硕士生');

# 主页分类
drop table if exists home_category;
create table `home_category` (
                            `id` bigint not null comment 'id',
                            `parent` bigint not null default 0 comment '父id',
                            `name` varchar(50) not null comment '名称',
                            `sort` int comment '顺序',
                            primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='主页分类';

insert into home_category (id, parent, name, sort) values (100, 000, '成员', 100);
insert into home_category (id, parent, name, sort) values (101, 100, '老师', 101);
insert into home_category (id, parent, name, sort) values (102, 100, '博士生', 102);
insert into home_category (id, parent, name, sort) values (103, 100, '硕士生', 103);
insert into home_category (id, parent, name, sort) values (104, 100, '本科生', 104);
insert into home_category (id, parent, name, sort) values (200, 000, '关于我们', 200);
insert into home_category (id, parent, name, sort) values (201, 200, '研究成果', 201);
insert into home_category (id, parent, name, sort) values (202, 200, '团队近况', 202);


-- 文档表
drop table if exists `doc`;
create table `doc` (
                       `id` bigint not null comment 'id',
                       `outCate_id` bigint not null default 0 comment '外部id',
                       `parent` bigint not null default 0 comment '父id',
                       `name` varchar(50) not null comment '名称',
                       `sort` int comment '顺序',
                       `view_count` int default 0 comment '阅读数',
                       `vote_count` int default 0 comment '点赞数',
                       primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文档';

insert into `doc` (id, outCate_id, parent, name, sort, view_count, vote_count) values (1, 1, 0, '文档1', 1, 0, 0);
insert into `doc` (id, outCate_id, parent, name, sort, view_count, vote_count) values (2, 1, 1, '文档1.1', 1, 0, 0);
insert into `doc` (id, outCate_id, parent, name, sort, view_count, vote_count) values (3, 1, 0, '文档2', 2, 0, 0);
insert into `doc` (id, outCate_id, parent, name, sort, view_count, vote_count) values (4, 1, 3, '文档2.1', 1, 0, 0);
insert into `doc` (id, outCate_id, parent, name, sort, view_count, vote_count) values (5, 1, 3, '文档2.2', 2, 0, 0);
insert into `doc` (id, outCate_id, parent, name, sort, view_count, vote_count) values (6, 1, 5, '文档2.2.1', 1, 0, 0);


-- 页面内容
drop table if exists `content`;
create table `content`(
                          `id` bigint not null comment 'id',
                          `content` mediumtext not null comment '内容',
                          `sort` int comment '顺序',
                          primary key (`id`)

)engine=innodb default charset=utf8mb4 comment='页面内容';

-- 其他内容：包括页脚信息，简介信息
drop table if exists `others`;
create table `others`(
                          `id` bigint not null comment 'id',
                          `content` mediumtext not null comment '简介信息',
                          `footer` mediumtext not null comment '页脚信息',
                          primary key (`id`)

)engine=innodb default charset=utf8mb4 comment='其他内容';

-- 用户表
drop table if exists `user`;
create table `user` (
                        `id` bigint not null comment 'ID',
                        `login_name` varchar(50) not null comment '登陆名',
                        `name` varchar(50) comment '昵称',
                        `password` char(32) not null comment '密码',
                        primary key (`id`),
                        unique key `login_name_unique` (`login_name`)
) engine=innodb default charset=utf8mb4 comment='用户';

insert into `user` (id, `login_name`, `name`, `password`) values (1, 'test', '测试', '7354a1d413535a6c0dc5c209e198d799');
