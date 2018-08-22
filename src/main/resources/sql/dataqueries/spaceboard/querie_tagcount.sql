-- Anzahl Pages mit 1,2,3,4>Tags
select *
from
(select	distinct 	subq1.title
,					count(*) over (partition by subq1.title, date_trunc('month', cl.lastmoddate)::DATE) as value
,					date_trunc('quarter', cl.lastmoddate)::DATE as date
from	(
		select		*
		from		content c
		where		c.prevver is null
		and			contenttype = 'PAGE'
		and			content_status = 'current'
		) subq1
join		content_label cl on cl.contentid = subq1.contentid
join		label lb on cl.labelid = lb.labelid
where		subq1.spaceid = ?
and			namespace = 'global'
group by 	cl.lastmoddate, subq1.title
order by 	date asc
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;