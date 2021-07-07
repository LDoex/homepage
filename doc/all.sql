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
                       `email` varchar(100) comment '邮箱',
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