
DROP TABLE IF EXISTS vitrinis_x_locations;
DROP TABLE IF EXISTS places_x_locations;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS places;


create table locations (
	_id	text,
	_vitrini_id	text not null,
	FOREIGN KEY (_vitrini_id) REFERENCES vitrinis (_id)
);


UPDATE db_version SET version=3 where _id=1;