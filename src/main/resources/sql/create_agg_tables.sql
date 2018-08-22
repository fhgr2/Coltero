-- Create main table
CREATE TABLE IF NOT EXISTS AGG_COLTERO (
  pk_aggregation SERIAL PRIMARY KEY,
  day DATE NOT NULL,
  time TIMESTAMP NOT NULL)
;

-- Create count Table
CREATE TABLE IF NOT EXISTS COUNTS_COLTERO (
  pk_counts SERIAL PRIMARY KEY,
  page_count INT NULL,
  comment_count INT NULL,
  user_count INT NULL,
  authors_count INT NULL,
  commentators_count INT NULL,
  uploaders_count INT NULL,
  slackers_count INT NULL,
  taggers_count INT NULL,
  likers_count INT NULL,
  CONSTRAINT fk_aggregation
    FOREIGN KEY (pk_counts)
    REFERENCES AGG_COLTERO (pk_aggregation)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
;
  CREATE UNIQUE INDEX IF NOT EXISTS idaggregation_UNIQUE on AGG_COLTERO (pk_aggregation ASC);
  CREATE UNIQUE INDEX IF NOT EXISTS pk_counts_UNIQUE on COUNTS_COLTERO (pk_counts ASC);
	
-- Create ContentChain Table (all contenttypes available)
create table if not exists agg_contentchain_coltero(
	id serial primary key
,	fk_aggregation bigint
,	nextval	bigint
,	title	varchar(255)
,	version	integer
,	lastmoddate date
,	fulllastmoddate timestamp
,	lastmodifier varchar(255)
,	creator varchar(255)
,	contenttype varchar(255)
,	pageid bigint
,	contentid bigint
,	spaceid bigint
,	CONSTRAINT fk_aggregation
	FOREIGN KEY (fk_aggregation)
	REFERENCES AGG_COLTERO (pk_aggregation)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

-- Create CommentChain from contentchain table (just comment types joined with title over pageid)
create table if not exists agg_commentchain_coltero(
	id serial primary key
,	fk_aggregation bigint
,	chaingroup	bigint
,	title	varchar(255)
,	version	integer
,	lastmoddate date
,	fulllastmoddate timestamp
,	lastmodifier varchar(255)
,	creator varchar(255)
,	contenttype varchar(255)
,	pageid bigint
,	spaceid bigint
,	CONSTRAINT fk_aggregation
	FOREIGN KEY (fk_aggregation)
	REFERENCES AGG_COLTERO (pk_aggregation)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

 -- create table for spacewordcount
CREATE TABLE IF NOT EXISTS AGG_SPACEWORDCOUNT (
 	id Serial primary key
 ,	spacename varchar(255) not null
 ,	wordcount int not null
 ,	time timestamp not null
 );

  -- create table for spacecommentwordcount
CREATE TABLE IF NOT EXISTS AGG_SPACECOMMENTWORDCOUNT (
 	id Serial primary key
 ,	spacename varchar(255) not null
 ,	wordcount int not null
 ,	time timestamp not null
 );
 
 -- create table for space-relations (pages, comments, uploads, users, spacetype...)
CREATE TABLE IF NOT EXISTS AGG_SPACERELATIONS (
	id Serial primary key
,	spaceid int not null
,	spacetype varchar(255) not null
,	spacename varchar(255) not null
,	lastmodifier varchar(255) not null
,	display_name varchar(255) not null
,	contentid int not null
,	date Date not null
 );
 
 -- create fullchain_table
 CREATE TABLE IF NOT EXISTS AGG_FULLCHAIN (
 	id Serial primary key
 ,	mainid int 
 ,	contentid int 
 ,	contenttype varchar(255) 
 ,	title varchar(255) 
 ,	version int not null
 ,	creator varchar(255)
 ,	creationdate timestamp
 ,	lastmodifier varchar(255)
 ,	lastmoddate timestamp
 ,	prevver int
 ,	content_status varchar(255)
 ,	pageid int
 ,	spaceid int
 );
 
 -- create user interactions table for preaggregating
CREATE TABLE IF NOT EXISTS agg_userinteractions (
 	id Serial primary key
,	displayName varchar(255)
,	spaceName varchar(255)
,	date varchar(255)
,	interactions int
);

-- create table for direct global user statistics
CREATE TABLE IF NOT EXISTS agg_userglobalstatistics (
	id Serial primary key
,	user_key varchar(255) not null
,	userconnections int
,	contentrange int
,	reactions int
,	writtenWords int
);

-- create mapping table for location to long and magnitude (World map)
CREATE TABLE IF NOT EXISTS COLTERO_LOCATION(
	id SERIAL PRIMARY KEY
,	location text
,	langitude text
,	latitude text
)
;

CREATE TABLE IF NOT EXISTS AGG_SHAREDLOCATION(
	id SERIAL PRIMARY KEY
,	location varchar(255)
,	occ int
,	spacename text
);

 CREATE INDEX IF NOT EXISTS spaceid_idx ON AGG_SPACERELATIONS (spaceid);
 CREATE INDEX IF NOT EXISTS spacetype_idx ON AGG_SPACERELATIONS (spacetype);
 CREATE INDEX IF NOT EXISTS display_name_idx ON AGG_SPACERELATIONS (display_name);
 CREATE INDEX IF NOT EXISTS date_idx ON AGG_SPACERELATIONS (date);
 CREATE INDEX IF NOT EXISTS lastmod_idx on AGG_FULLCHAIN (lastmodifier);
 CREATE INDEX IF NOT EXISTS userkey_idx on AGG_USERGLOBALSTATISTICS (user_key);
