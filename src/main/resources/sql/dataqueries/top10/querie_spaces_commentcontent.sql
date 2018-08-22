--Top 10 Spaces mit den meisten Wörtern in den Kommentaren (längste Kommentare)
with currentcomments as (
select		c.contentid as commentid
,			c.pageid as pageid
from		content c
where		c.prevver is null
and			c.content_status = 'current'
and			c.contenttype = 'COMMENT'
),
currentpages as (
select		c.contentid
,			c.spaceid
from		content c
where		c.prevver is null
and			c.contenttype = 'PAGE'
and			c.content_status = 'current'
)
select		s.spacename 
,			bc.body
from		currentcomments cc
left join 	currentpages cp on cp.contentid = cc.pageid
left join	bodycontent bc on bc.contentid = cc.commentid
left join 	spaces s on s.spaceid = cp.spaceid
where		s.spacename is not null
;