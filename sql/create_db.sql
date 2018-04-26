create table client
(
  id serial not null
    constraint client_pkey
    primary key,
  email varchar(100) not null,
  name varchar(100) not null,
  password varchar(40) not null
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
  name varchar(20) not null
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

create table "like"
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

