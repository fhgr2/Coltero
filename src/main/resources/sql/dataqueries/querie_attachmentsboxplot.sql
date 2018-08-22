--select	distinct agg.title
--,		count(*) over (partition by nextval, agg.lastmoddate::DATE) as attachments
--,		date_trunc('quarter', agg.lastmoddate)::DATE as date
--from	agg_contentchain_coltero agg
--join	
--(
--	select	agg.pageid
--	,		agg.contentid
--	from	agg_contentchain_coltero agg
--	where	agg.contenttype = 'ATTACHMENT'
--	 
--) sub
--on sub.pageid = agg.contentid
--where	agg.title is not null
--order by date asc
select *
from (
select	mainid as contentid
,		contentid as id
,		date_trunc('quarter', lastmoddate)::DATE as date
from	agg_fullchain fc
where	contenttype = 'ATTACHMENT'
and		content_status = 'current'
order by date, mainid
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;