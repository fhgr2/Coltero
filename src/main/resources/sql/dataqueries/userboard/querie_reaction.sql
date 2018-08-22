-- 57 Anzahl der User, die auf meinen Beitrag reagiert haben (Likes)
select	distinct display_name
from	agg_fullchain fc
join	agg_fullchain fc2 on fc2.mainid = fc.mainid
join	likes l on l.contentid = fc2.mainid
join	user_mapping um on um.user_key = fc.lastmodifier
join	cwd_user usr on usr.lower_user_name = um.lower_username	
where	fc2.mainid in 
		(
		select	fc.mainid
		from	agg_fullchain fc
		where	fc.lastmodifier = ?
		)
and		fc2.lastmodifier != ?
group by display_name
union
-- 57 Anzahl der User, die auf meinen Beitrag reagiert haben (Comments)
select	distinct display_name
from	agg_fullchain fc
join	agg_fullchain fc2 on fc2.mainid = fc.mainid
join	user_mapping um on um.user_key = fc.lastmodifier
join	cwd_user usr on usr.lower_user_name = um.lower_username	
where	fc2.mainid in 
		(
		select	fc.mainid
		from	agg_fullchain fc
		where	fc.lastmodifier = ?
		)
and	fc2.lastmodifier != ?
and	fc2.contenttype = 'COMMENT'
;