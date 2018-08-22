-- 56 Reichweite meines Inhalts (Anzahl einmaliger User aller Spaces in dem der User ist)
select	count(distinct lastmodifier) as coverage
from 	agg_fullchain fc
where	spaceid in (
select	distinct spaceid
	from	
	(
	select	distinct mainid
	,		spaceid
	from	agg_fullchain
	where	lastmodifier = ?
	) as users
)
and	lastmodifier != ?