-- 44 Co-Space Netzwerkdiagramm (Space im Zentrum 1,2,3-Ebenen tief)
select	count(display_name) as occ
,		display_name
from	agg_fullchain fc
join	user_mapping um on um.user_key = fc.lastmodifier
join	cwd_user usr on usr.lower_user_name = um.lower_username
join	spaces s on s.spaceid = fc.spaceid
where	fc.spaceid = ?
and		s.spacetype != 'personal'
group by spacename, display_name
order by spacename, occ desc
;