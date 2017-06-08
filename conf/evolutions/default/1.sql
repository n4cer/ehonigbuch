# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bottling_record (
  id                        bigserial not null,
  number                    integer,
  amount                    varchar(255),
  date                      timestamp,
  label_number_from         integer,
  label_number_to           integer,
  harvest_record_id         bigint,
  constraint pk_bottling_record primary key (id))
;

create table harvest_record (
  id                        bigserial not null,
  number                    integer,
  date                      timestamp,
  description               varchar(255),
  weight                    float,
  user_id                   bigint,
  constraint pk_harvest_record primary key (id))
;

create table beekeepers (
  id                        bigserial not null,
  name                      varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  street                    varchar(255),
  zip_code                  varchar(255),
  city                      varchar(255),
  email                     varchar(255),
  telephone                 varchar(255),
  password_hash             varchar(255),
  visible                   boolean default false,
  active                    boolean default true,
  registration_number       varchar(255),
  constraint uq_beekeepers_name unique (name),
  constraint uq_beekeepers_email unique (email),
  constraint pk_beekeepers primary key (id))
;

alter table bottling_record add constraint fk_bottling_record_harvestReco_1 foreign key (harvest_record_id) references harvest_record (id);
create index ix_bottling_record_harvestReco_1 on bottling_record (harvest_record_id);
alter table harvest_record add constraint fk_harvest_record_user_2 foreign key (user_id) references beekeepers (id);
create index ix_harvest_record_user_2 on harvest_record (user_id);



# --- !Downs

drop table if exists bottling_record cascade;

drop table if exists harvest_record cascade;

drop table if exists beekeepers cascade;

