--select	distinct title
--,		count(*) over (partition by nextval) as likes
--,		l.creationdate::DATE as date
--from	agg_contentchain_coltero c
--join 	likes l on c.contentid = l.contentid
----where	lastmoddate between ?::DATE and ?::DATE
--and		contenttype = 'PAGE'
--order by date asc
--;
select *
from (
select	l.id
,		l.contentid
,		date_trunc('quarter',l.creationdate)::DATE as date
from	likes l
order by date
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
