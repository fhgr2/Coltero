create sequence if not exists serial start 1;
ALTER SEQUENCE serial RESTART WITH 1;

drop table if exists parentsites;
create temporary table parentsites as (
	select	nextval('serial')
	,		*
	from	content
	where	prevver is null
	and		content_status = 'current'
	);


drop table if exists childrensites;
create temporary table childrensites as (
	select	nextval as latestversion_id
	,		c1.*
	from	parentsites 
	join 	content c1 on c1.prevver = parentsites.contentid
	where	c1.content_status = 'current'
	);

truncate agg_contentchain_coltero;

insert into agg_contentchain_coltero (
fk_aggregation,
nextval,
title,
version,
lastmoddate,
fulllastmoddate,
lastmodifier,
creator,
contenttype,
pageid,
contentid,
spaceid
)
	(select * from 
		(
			select pk_aggregation from agg_coltero
			order by pk_aggregation desc
			limit 1) as sub1
			,
			(select p.nextval
			,		p.title
			, 		p.version
			, 		p.lastmoddate::DATE
			,		p.lastmoddate
			, 		p.lastmodifier
			, 		p.creator
			,		p.contenttype
			,		p.pageid
			,		p.contentid
			,		p.spaceid
			from	parentsites p
			union
			select 	c.latestversion_id
			,		c.title
			, 		c.version
			, 		c.lastmoddate::DATE
			,		c.lastmoddate
			, 		c.lastmodifier
			, 		c.creator
			,		c.contenttype
			,		c.pageid
			,		c.contentid
			,		c.spaceid
			from	childrensites c
		) as sub2 
	order by sub2.nextval, sub2.version
	);
		
drop table childrensites;
drop table parentsites;

-- also calc agg_comments_chain
truncate agg_commentchain_coltero;

insert into agg_commentchain_coltero (
fk_aggregation,
chaingroup,
title,
version,
lastmoddate,
fulllastmoddate,
lastmodifier,
creator,
contenttype,
pageid,
spaceid
)
(select comments.fk_aggregation
,		comments.nextval as chaingroup
,		c1.title
,		comments.version
,		c1.lastmoddate
,		comments.fulllastmoddate
,		comments.lastmodifier
,		comments.creator
,		comments.contenttype
,		comments.pageid
,		c1.spaceid
from 
(
		select 	*
		from	agg_contentchain_coltero 
		where	contenttype = 'COMMENT'
) comments
left join content c1 on c1.contentid = comments.pageid and c1.contenttype = 'PAGE'
order by comments.pageid, chaingroup, comments.fulllastmoddate)
;


-- also calc agg_fullchain
truncate agg_fullchain;

-- pagechain
with parents as (
	select	contentid
	from	content c
	where	c.contenttype = 'PAGE'
	and	c.content_status = 'current'
	and	c.prevver is null
),
children as (
	select	*
	from	content c
	where	c.contenttype = 'PAGE'
	and		c.content_status = 'current'
	and		c.prevver is null	
	union 	
	select	*
	from	content c
	where	c.contenttype = 'PAGE'
	and		c.content_status = 'current'
	and		c.prevver is not null	
),
otherparents as (
	select	pageid
	,		contentid
	from	content c
	where	c.contenttype in ('ATTACHMENT', 'COMMENT')
	and		prevver is null
	and		c.content_status = 'current'
),
otherchildren as (
	select	*
	from	content c
	where	c.contenttype in ('ATTACHMENT', 'COMMENT')
	and		prevver is null
	and		c.content_status = 'current'
	union
	select	*
	from	content c
	where	c.contenttype in ('ATTACHMENT', 'COMMENT')
	and		prevver is not null
	and		c.content_status = 'current'
),
fullchain as (
select	p.contentid as mainid
,		c.*
from	parents p
join	children c on c.prevver = p.contentid
union
select	o.pageid as mainid
,		oc.*	
from	otherparents o
join	otherchildren oc on oc.prevver = o.contentid 
order by mainid
)
insert into agg_fullchain (
	mainid
,	contentid
,	contenttype
,	title
,	version
,	creator
,	creationdate
,	lastmodifier
,	lastmoddate
,	prevver
,	content_status
,	pageid
,	spaceid
)
select	mainid
,	fc.contentid
,	fc.contenttype
,	fc.title
,	fc.version
,	fc.creator
,	fc.creationdate
,	fc.lastmodifier
,	fc.lastmoddate
,	fc.prevver
,	fc.content_status
,	fc.pageid
,	c.spaceid
from	fullchain fc
join	content c on mainid = c.contentid
where	c.contenttype not in ('USERINFO', 'DRAFT', 'GLOBALDESCRIPTION')
and	fc.creator is not null
;
