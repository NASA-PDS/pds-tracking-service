/*
**  Copyright 2016-2017, by the California Institute of Technology.
**  ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
**  Any commercial use must be negotiated with the Office of Technology
**  Transfer at the California Institute of Technology.
**
**  This software is subject to U. S. export control laws and regulations
**  (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
**  is subject to U.S. export control laws and regulations, the recipient has
**  the responsibility to obtain export licenses or other export authority as
**  may be required before exporting such information to foreign countries or
**  providing access to foreign nationals.
**
** $Id$
*/

/*
** This script creates the database objects for the Tracking Service schema.
*/

create table if not exists archive_status (
  logical_identifier varchar(255) not null,
  version_id varchar(255) not null,
  status_date_time datetime not null,
  status varchar(255) not null,
  electronic_mail_address varchar(255) not null,
  comment varchar(1024)
) ENGINE=InnoDB;

create table if not exists certification_status (
  logical_identifier varchar(255) not null,
  version_id varchar(255) not null,
  status_date_time datetime not null,
  status varchar(255) not null,
  electronic_mail_address varchar(255) not null,
  comment varchar(1024)
) ENGINE=InnoDB;

create table if not exists delivery (
  delivery_identifier int not null auto_increment,
  logical_identifier varchar(255) not null,
  version_id varchar(255) not null,
  name varchar(255) not null,
  start_date_time datetime not null,
  stop_date_time datetime not null,
  source varchar(255) not null,
  target varchar(255) not null,
  due_date date not null,
  primary key (delivery_identifier)
) ENGINE=InnoDB;

create table if not exists doi (
  logical_identifier varchar(255) not null,
  version_id varchar(255) not null,
  doi varchar(255) not null,
  registration_date datetime not null,
  site_url varchar(255) not null,
  electronic_mail_address varchar(255) not null,
  comment varchar(1024),
  primary key (logical_identifier, version_id)
) ENGINE=InnoDB;

create table if not exists instrument_reference (
  logical_identifier varchar(255) not null,
  reference varchar(255) not null,
  title varchar(255) not null,
  primary key (logical_identifier, reference)
) ENGINE=InnoDB;

create table if not exists investigation_reference (
  logical_identifier varchar(255) not null,
  reference varchar(255) not null,
  title varchar(255) not null,
  primary key (logical_identifier, reference)
) ENGINE=InnoDB;

create table if not exists node_reference (
  logical_identifier varchar(255) not null,
  reference varchar(255) not null,
  title varchar(255) not null,
  primary key (logical_identifier, reference)
) ENGINE=InnoDB;

create table if not exists nssdca_status (
  logical_identifier varchar(255) not null,
  version_id varchar(255) not null,
  status_date_time datetime not null,
  nssdca_identifier varchar(255) not null,
  electronic_mail_address varchar(255) not null,
  comment varchar(1024),
  primary key (logical_identifier, version_id)
) ENGINE=InnoDB;

create table if not exists product (
  logical_identifier varchar(255) not null,
  version_id varchar(255) not null,
  title varchar(255) not null,
  type varchar(255) not null,
  alternate_id varchar(255),
  primary key (logical_identifier, version_id)
) ENGINE=InnoDB;

create table if not exists releases (
  logical_identifier varchar(255) not null,
  version_id varchar(255) not null,
  release_date_time datetime not null,
  announcement_date_time datetime not null,
  name varchar(255) not null,
  description varchar(255) not null,
  electronic_mail_address varchar(255) not null,
  comment varchar(1024)
) ENGINE=InnoDB;

create table if not exists role (
  electronic_mail_address varchar(255) not null,
  reference varchar(255) not null
) ENGINE=InnoDB;

create table if not exists submission (
  delivery_identifier int not null,
  submission_date_time datetime not null,
  primary key (delivery_identifier, submission_date_time)
) ENGINE=InnoDB;

create table if not exists submission_status (
  delivery_identifier int not null,
  submission_date_time datetime not null,
  status_date_time datetime not null,
  status varchar(255) not null,
  electronic_mail_address varchar(255) not null,
  comment varchar(1024)
) ENGINE=InnoDB;

create table if not exists user (
  electronic_mail_address varchar(255) not null,
  name varchar(255) not null,
  primary key (electronic_mail_address)
  ) ENGINE=InnoDB;
