

DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS vitrinis;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS db_version;


create table events (
	_id text primary key, 
	name text not null, 
	description text, 
	iconPath text
);

create table vitrinis (
	_id text primary key,
	name text not null,
	smallImagePath text,
	largeImagePath text,
	description text,	
	segment text,
	site	text
);

create table products (
	_id text primary key,
	name text not null, 
	smallImagePath text,
	largeImagePath text,
	description text,
	price numeric not null,
	vitriniId text, 
	FOREIGN KEY (vitriniId) REFERENCES vitrinis (_id)
);

create table db_version (
	_id	int	primary key,
	version	int	not null
);

INSERT INTO db_version VALUES (1,1);