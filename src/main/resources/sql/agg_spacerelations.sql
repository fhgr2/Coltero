-- 25 Anzahl isolierte Spaces
truncate table AGG_SPACERELATIONS;
with spaces as (
select		s.spacename
,			s.spaceid
,			s.spacetype
from		spaces s
where		s.spacestatus != 'DELETED'
-- and		s.creator is not null (avoid Demonstration Space in statistic->)
),
pagechain as (
select		c.spaceid
,			c2.contentid
,			c2.lastmoddate::DATE
,			c2.lastmodifier
from		content c	
left join	content c2 on c2.prevver = c.contentid
and			c.prevver is null
and			c.content_status = 'current'
and			c.contenttype = 'PAGE'
where		c2.prevver is not null
and			c2.content_status = 'current'
and			c2.contenttype = 'PAGE'
and			c2.lastmodifier is not null
union
select		c.spaceid
,			c.contentid
,			c.lastmoddate::DATE
,			c.lastmodifier
from		content c
where		c.prevver is null
and			c.contenttype = 'PAGE'
and			c.content_status = 'current'
and			c.lastmodifier is not null
),
comments as (
select		c.pageid
,			c2.contentid
,			c2.lastmoddate::DATE
,			c2.lastmodifier
from		content c
left join	content c2 on c.contentid = c2.prevver
where		c.prevver is null
and			c.contenttype = 'COMMENT'
and			c.content_status = 'current'
and			c2.prevver is not null
and			c2.contenttype = 'COMMENT'
and			c2.content_status = 'current'
union
select		c.pageid
,			c.contentid
,			c.lastmoddate::DATE
,			c.lastmodifier
from		content c
where		c.prevver is null
and			c.contenttype = 'COMMENT'
and			c.content_status = 'current'
),
uploaders as (
select		c.spaceid
,			c2.contentid
,			c2.lastmoddate::DATE
,			c2.lastmodifier
from		content c
left join	content c2 on c.contentid = c2.prevver
where		c.contenttype = 'ATTACHMENT'
and			c.content_status = 'current'
and			c.prevver is null
and			c2.prevver is not null
and			c2.content_status = 'current'
and			c2.contenttype = 'ATTACHMENT'
and			c2.title != 'user-avatar'
),
users as (
select		cu.display_name
,			um.user_key
,			um.username
from		cwd_user cu
left join	user_mapping um on um.lower_username = cu.lower_user_name  
),
unionized as (
select		*
from		(
	select		c.spaceid
	,			cm.contentid
	,			cm.lastmoddate
	,			cm.lastmodifier
	from		comments cm
	left join	content c on cm.pageid = c.contentid
	) commentchain
union
select		*
from		pagechain
union		
select		*
from		uploaders
)
insert into 	AGG_SPACERELATIONS (
	spaceid
,	spacetype
,	spacename
,	lastmodifier
,	display_name
,	contentid
,	date
)
(
select		uni.spaceid
,			spacetype
,			spacename
,			lastmodifier
,			case when u.display_name is null then 'deleted user' else u.display_name end
,			contentid
,			lastmoddate as date
from		unionized uni
left join	users u on lastmodifier = u.user_key
left join  	spaces s on uni.spaceid = s.spaceid 
where		uni.spaceid is not null
and			uni.lastmodifier is not null
and			s.spacename is not null
order by 	uni.lastmoddate
)
;