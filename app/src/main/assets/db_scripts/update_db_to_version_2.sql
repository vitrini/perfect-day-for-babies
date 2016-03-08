
alter table vitrinis add favorite	integer	default	0;

UPDATE db_version SET version=2 where _id=1;