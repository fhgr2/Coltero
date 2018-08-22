--select	*
--from	(
--	select	title
--	,		count(distinct lastmodifier) as commentators
--	,		lastmoddate::DATE as date
--	from	agg_commentchain_coltero
--	where  	title is not null
----	and		lastmoddate  between ?::DATE and ?::DATE
--	group by pageid, title, lastmoddate
--	order by commentators, lastmoddate desc
--)sub
--where sub.commentators > 1
--order by date asc
--;
select *
from (
select	distinct mainid
,		fc.lastmodifier
,		date_trunc('quarter', lastmoddate)::DATE as date
from	agg_fullchain fc
where  	fc.contenttype = 'COMMENT'
group by mainid, fc.lastmodifier, fc.lastmoddate
order by date, lastmodifier
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
