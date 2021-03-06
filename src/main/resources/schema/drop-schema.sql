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
** This script drops all database objects from the Tracking Service schema.
*/

drop table archive_status;
drop table certification_status;
drop table delivery;
drop table doi;
drop table instrument_reference;
drop table investigation_reference;
drop table node_reference;
drop table nssdca_status;
drop table product;
drop table releases;
drop table role;
drop table submission;
drop table submission_status;
drop table user;
