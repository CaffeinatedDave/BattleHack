# --- !Ups

CREATE TABLE tShelter(
    id            serial        primary key,
    name          varchar(128)  not null,
    contact_no    varchar(64)   default '', 
    contact_email varchar(128)  default '',
    addr_1        varchar(128)  not null,
    addr_2        varchar(128)  not null,
   	addr_3        varchar(128)  not null,
   	addr_4        varchar(128)  not null,
    info          text
);

CREATE UNIQUE INDEX iShelter1 ON tShelter (name);

INSERT INTO tShelter(name, contact_no, contact_email, addr_1, addr_2, addr_3, addr_4, info) values (
	'Battersea Dogs and Cats Home London',
	'0843 509 4444',
	'info@battersea.org.uk',
	'4 Battersea Park Road',
	'',
	'London',
	'SW8 4AA',
	'Info goes here later...');

CREATE TABLE tPet(
  id         serial      primary key,
  name       varchar(64) not null,
  image      varchar(64) not null,
  age        int         not null,
  profile    text        ,
  species    char(1)     not null,
  status     char(1)     not null default 'A',
  shelter_id int         not null references tShelter(id)
);

# --- !Downs

DROP TABLE tPet;
DROP TABLE tShelter;