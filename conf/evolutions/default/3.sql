# --- !Ups
ALTER TABLE harvest_record ADD COLUMN harvest_type_id bigint;
create table harvest_type (
  id                            bigserial not null,
  type_id                       varchar(255),
  name                          varchar(255),
  constraint pk_harvest_type primary key (id)
);
insert into harvest_type (id, type_id, name) values (1, 'L', 'Honig');
insert into harvest_type (id, type_id, name) values (2, 'M', 'Met - Honigwein');
update harvest_record set harvest_type_id = 1;
select 1;

# --- !Downs
drop table if exists harvest_type cascade;
ALTER TABLE harvest_record DROP COLUMN harvest_type_id;