--select 	distinct agg.title as page
--,		count(*) over (partition by agg.title, agg.lastmoddate::DATE) as tags
--,		agg.lastmoddate::DATE as date
--from	label lb
--join	content_label cl on cl.labelid = lb.labelid
--join	agg_contentchain_coltero agg on agg.contentid = cl.contentid
--where 	namespace = 'global'
----and		agg.lastmoddate  between ?::DATE and ?::DATE
--and 	labelabletype = 'CONTENT'
--and		contenttype = 'PAGE'
--group by page, lb.labelid, agg.lastmoddate, lb.name
--order by date asc
--;
select *
from (
select 	c.contentid
,		lb.labelid as id
,		date_trunc('quarter', c.lastmoddate)::DATE as date
from	label lb
join	content_label cl on cl.labelid = lb.labelid
join	content c on c.contentid = cl.contentid
where 	namespace = 'global'
and 	labelabletype = 'CONTENT'
and	contenttype = 'PAGE'
order by date , contentid asc
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE

;