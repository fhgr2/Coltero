-- Anzahl Pages mit 1,2,3,4>Autoren
select  *
from	(
select	title
,		count(distinct lastmodifier) as value
,		date
from	(
		select	spaceid
		,		contentid
		,		date_trunc('quarter', lastmoddate)::DATE as date
		,		lastmodifier
		,		title
		,		prevver
		from	content c
		where	c.prevver is null
		and		c.contenttype = 'PAGE'
		and		c.content_status = 'current'
		and		creator is not null
		and		spaceid = ?
		union
		select	subq.spaceid
		,		c2.contentid
		,		date_trunc('month', lastmoddate)::DATE as date
		,		c2.lastmodifier
		,		c2.title
		,		c2.prevver
		from 	(
				select		title
				,			version
				,			contentid
				,			spaceid
				,		title
				,		prevver
				from	content c
				where	c.prevver is null
				and		c.contenttype = 'PAGE'
				and		c.content_status = 'current'
				and		c.spaceid = ?
				) subq 
		join 	content c2 on subq.contentid = c2.prevver
		and		c2.contenttype = 'PAGE'
		and		c2.content_status = 'current'
	) subq2
group by date, title
order by date asc
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;