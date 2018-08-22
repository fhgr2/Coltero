--select	title
--,		count(pageid) as comments
--,		lastmoddate::DATE as date
--from	agg_commentchain_coltero
--where  	title is not null
----and		lastmoddate  between ?::DATE and ?::DATE
--group by pageid, title, lastmoddate
--order by date asc
--;
select *
from (
select	mainid as contentid
,		contentid as id
,		date_trunc('quarter',lastmoddate)::DATE as date
from	agg_fullchain fc
where  	contenttype = 'COMMENT'
and		content_status = 'current'
order by date, mainid
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE

;