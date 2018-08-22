---- 53 / 54 Anzahl der Kontakte mit anderen Usern (Autoren, Kommentatoren)
--select	distinct lastmodifier
--,		usr.display_name
--		from	agg_fullchain
--join	user_mapping um on um.user_key = lastmodifier
--join	cwd_user usr on usr.lower_user_name = um.lower_username
--where	mainid in 
--		(
--			select	distinct mainid
--			from	agg_fullchain
--			where	creator is not null
--			and	lastmodifier = ?
--		)
--and	lastmodifier != ?
-- other chain but without parentsite

-- 53 / 54 Anzahl der Kontakte mit anderen Usern (Autoren, Kommentatoren)
select	*
from (
select	lastmodifier
,	 	count(lastmodifier) as occ
,		usr.display_name
		from	agg_fullchain
join	user_mapping um on um.user_key = lastmodifier
join	cwd_user usr on usr.lower_user_name = um.lower_username
where	mainid in 
		(
			select	distinct mainid
			from	agg_fullchain
			where	creator is not null
			and	lastmodifier = ?
		)
and	lastmodifier != ?
group by lastmodifier, usr.display_name
) subq1
where occ > 5