drop table if exists items;

create table items
(
 id int AUTO_INCREMENT PRIMARY KEY,
 item_id varchar(100) not null,
 item_desc varchar(1000) not null,
 item_price double,
 item_count int
);

drop table if exists visitors;

create table visitors
(
 visitor_id int AUTO_INCREMENT PRIMARY KEY,
 item_id varchar(100),
 ip_addr varchar(100),
 visit_time timestamp
);