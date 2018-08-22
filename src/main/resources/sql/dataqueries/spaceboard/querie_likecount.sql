-- Anzahl Pages mit 1,2,3,4>Likes
select *
from	(
select	subq2.title
,		sum(subq2.likes) over (partition by subq2.title order by date asc) as value
,		subq2.date
from	(
		select	distinct subq1.title
		,		count(*) over (partition by date_trunc('month',l.creationdate)::DATE, subq1.title) as likes
		,		date_trunc('quarter',l.creationdate)::DATE as date
		from	(
				select		*
				from		content c
				where		prevver is null
				and			c.contenttype = 'PAGE'
				and			content_status = 'current'
				) subq1
		join		likes l on subq1.contentid = l.contentid
		where		subq1.spaceid = ?
		group by 	subq1.title, l.creationdate
		order by 	likes desc
		) subq2
order by date asc
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;