
alter table vitrinis add city text;
alter table vitrinis add estate text;

UPDATE db_version SET version=4 where _id=1;