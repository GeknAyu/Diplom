create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 1 );
create table users (id bigint not null, password varchar(255), role varchar(255), status varchar(255), user_name varchar(255), primary key (id));
