--select		title
--,			count(distinct fc.lastmodifier) as authors
--,			mainid
--from		agg_fullchain fc
--left join 	spaces s on s.spaceid = fc.spaceid
--where		fc.contenttype = 'PAGE'
--and			fc.content_status = 'current'
--and			spacetype = 'global'
--group by 	title, mainid
--order by 	authors desc
--;

select *
from (
select		title
,			count(distinct fc.lastmodifier) as authors
,			mainid
,			date_trunc('quarter', fc.lastmoddate)::DATE as date
from		agg_fullchain fc
left join 	spaces s on s.spaceid = fc.spaceid
where		fc.contenttype = 'PAGE'
and			fc.content_status = 'current'
and			spacetype = 'global'
group by 	title, mainid, date
order by 	date asc, authors desc
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;