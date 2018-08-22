-- Anzahl Pages ohne att, likes, tags, comments
select *
from
(select		title
,			date
from (
	select		title
	,			contentid
	,			date_trunc('quarter', lastmoddate)::DATE as date
	from		content c
	where		contenttype = 'PAGE'
	and			content_status = 'current'
	and			prevver is null
	and			creator is not null
	and 		spaceid = ?
	EXCEPT
	select		subq.title
	,			subq.contentid
	,			subq.date
	from		(
	select		title
	,			spaceid
	,			contentid
	,			date_trunc('month', c.lastmoddate)::DATE as date
	from		content c
	where		prevver is null
	and			content_status = 'current'
	and			contenttype = 'PAGE'
	and			spaceid = ?
				) subq
	join		content c2 on subq.contentid = c2.pageid
	and			c2.contenttype = 'COMMENT'
	and			c2.content_status = 'current'
	EXCEPT
	select		subq1.title
	,			subq1.contentid
	,			date_trunc('month', cl.lastmoddate)::DATE as date
	from		(
				select		*
				from		content c
				where		c.prevver is null
				and		contenttype = 'PAGE'
				and		content_status = 'current'
				) subq1
	join		content_label cl on cl.contentid = subq1.contentid
	join		label lb on cl.labelid = lb.labelid
	where		subq1.spaceid = ?
	and			namespace = 'global'
	EXCEPT
	select		subq1.title
	,			subq1.contentid
	,			date_trunc('month',subq1.lastmoddate)::DATE as date
	from		(
				select		*
				from		content c
				where		prevver is null
				and		c.contenttype = 'PAGE'
				and		content_status = 'current'
				) subq1
	join		likes l on subq1.contentid = l.contentid
	where		subq1.spaceid = ?
	EXCEPT
	select		subq1.title
	,			subq1.contentid
	,			date_trunc('month', c2.lastmoddate)::DATE as date
	from		(
				select	*
				from	content c
				where	c.prevver is null
				and	c.contenttype = 'PAGE'
				and	c.content_status = 'current'
				and	c.spaceid = ?
				) subq1
	join 		content c2 on subq1.contentid = c2.pageid
	and			c2.prevver is null
	and			c2.contenttype = 'ATTACHMENT'
	and			c2.content_status = 'current'
	) subq2
group by date, title
order by date asc
)quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;