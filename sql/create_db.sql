drop table if exists client cascade;
drop table if exists tag cascade;
drop table if exists status cascade;
drop table if exists report cascade;
drop table if exists report_tag cascade;
drop table if exists upvote cascade;


create table client
(
  id serial not null
    constraint client_pkey
    primary key,
  email varchar(100) not null,
  name varchar(100) not null,
  password varchar(40) not null,
  is_admin boolean not null,
  is_activated boolean not null,
  register_code integer not null
)
;

create unique index client_id_uindex
  on client (id)
;

create unique index client_email_uindex
  on client (email)
;

create table tag
(
  id serial not null
    constraint tag_pkey
    primary key,
  name varchar(30) not null,
  email varchar(100) not null
)
;

create unique index tag_id_uindex
  on tag (id)
;

create unique index tag_name_uindex
  on tag (name)
;

create table status
(
  name varchar(20) not null
    constraint status_pkey
    primary key
)
;

create table report
(
  id serial not null
    constraint report_pkey
    primary key,
  title varchar(100) not null,
  description varchar(500),
  client_id integer not null
    constraint report_client_id_fk
    references client,
  location varchar(20),
  status_name varchar(20) not null
    constraint report_status_name_fk
    references status
)
;

create unique index report_id_uindex
  on report (id)
;

create table report_tag
(
  report_id integer not null
    constraint report_tag_report_id_fk
    references report,
  tag_id integer not null
    constraint report_tag_tag_id_fk
    references tag,
  constraint report_tag_report_id_tag_id_pk
  primary key (report_id, tag_id)
)
;

create table upvote
(
  client_id integer not null
    constraint like_client_id_fk
    references client,
  report_id integer not null
    constraint like_report_id_fk
    references report
)
;

create unique index status_name_uindex
  on status (name)
;

insert into status values ('pending');
insert into status values ('accepted');
insert into status values ('fixed');

insert into tag values (nextval('tag_id_seq'), 'tagA', 'thewiztory@gmail.com');
insert into tag values (nextval('tag_id_seq'), 'tagB', 'thewiztory@gmail.com');
insert into tag values (nextval('tag_id_seq'), 'tagC', 'thewiztory@gmail.com');
insert into tag values (nextval('tag_id_seq'), 'tagD', 'thewiztory@gmail.com');
insert into tag values (nextval('tag_id_seq'), 'tagE', 'thewiztory@gmail.com');

insert into client values (nextval('client_id_seq'), 'peczar@cpp.pl', 'marcin peczarski', 'cppjestfajny', true, true, 0);
insert into client values (nextval('client_id_seq'), 'admin@admin.com', 'admin', 'admin', true, true, 0);
insert into client values (nextval('client_id_seq'), 'aw386379@students.mimuw.edu.pl', 'Adam Wiktor', 'admin', false, true, 0);
insert into client values (nextval('client_id_seq'), 'asdad@we.w', 'Radek Rowicki', 'admin', false, true, 0);
insert into client values (nextval('client_id_seq'), 'AWDWd3@wa.w', 'Łukasz Kondraciuk', 'admin', false, true, 0);
insert into client values (nextval('client_id_seq'), 'awqwq@op.pl', 'Szymon Zwara', 'admin', false, true, 0);

insert into tag values (nextval('tag_id_seq'), 'Zepsuty przedmiot', 'aw386379@students.mimuw.edu.pl');
insert into tag values (nextval('tag_id_seq'), 'Dyspozytor wody', 'lk385775@students.mimuw.edu.pl');
insert into tag values (nextval('tag_id_seq'), 'Hydraulika', 'aw386379@students.mimuw.edu.pl');
insert into tag values (nextval('tag_id_seq'), 'Okna/drzwi', 'aw386379@students.mimuw.edu.pl');
insert into tag values (nextval('tag_id_seq'), 'Students', 'rr386088@students.mimuw.edu.pl');
insert into tag values (nextval('tag_id_seq'), 'Sprzęt elektroniczny', 'rr386088@students.mimuw.edu.pl');
insert into tag values (nextval('tag_id_seq'), 'Lampy', 'aw386379@students.mimuw.edu.pl');
insert into tag values (nextval('tag_id_seq'), 'Bałagan', 'sz383558@students.mimuw.edu.pl');