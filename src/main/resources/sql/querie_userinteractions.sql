-- 16 Boxplot von Kontakten der User (space)
select	display_name
,		s.spacename
,		date_trunc('quarter', fc.lastmoddate)::DATE as date
,		count(*)
from	agg_fullchain fc
join	spaces s on s.spaceid = fc.spaceid
join	user_mapping um on um.user_key = fc.lastmodifier
join	cwd_user usr on usr.lower_user_name = um.lower_username	
where	fc.content_status = 'current'
and		fc.lastmodifier is not null
and		date_trunc('quarter', fc.lastmoddate)::DATE < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
group by display_name, spacename, date
order by date 
;
