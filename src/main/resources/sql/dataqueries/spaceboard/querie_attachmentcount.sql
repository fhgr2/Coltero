-- Anzahl Pages mit 1,2,3,4>Attachments (Verlauf verteilung)
select 	*
from	(
select	subq2.title
,		sum(subq2.attachments) over (partition by subq2.title order by date asc) as value
,		subq2.date
from 	(
		select	subq1.title
		,	count(*) over (partition by date_trunc('month', c2.lastmoddate)::DATE, subq1.title) as attachments
		,	date_trunc('quarter', c2.lastmoddate)::DATE as date
		from	(
				select	*
				from	content c
				where	c.prevver is null
				and	c.contenttype = 'PAGE'
				and	c.content_status = 'current'
				and	c.spaceid = ?
				) subq1
	join 	content c2 on subq1.contentid = c2.pageid
	and		c2.prevver is null
	and		c2.contenttype = 'ATTACHMENT'
	and		c2.content_status = 'current'
	group by subq1.title, c2.lastmoddate
	order by attachments desc
) subq2
group by title, subq2.date, subq2.attachments
order by date asc
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;