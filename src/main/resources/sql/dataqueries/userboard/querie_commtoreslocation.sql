-- 55 Anzahl der Co-Kommtoren pro Location
select	count(distinct user_key)
,		location
from	(
		select	distinct lastmodifier
		,	usr.display_name
		from	agg_fullchain
		join	user_mapping um on um.user_key = lastmodifier
		join	cwd_user usr on usr.lower_user_name = um.lower_username
		where	mainid in 
		(
		select	distinct mainid
		from	agg_fullchain
		where	creator is not null
		and		lastmodifier = ?
		)
		) users
join	(
		select	pe.string_val as location
		,		um.user_key 
		from	os_propertyentry pe
		join	content c on c.contentid = pe.entity_id
		join	user_mapping um on c.username = um.user_key
		where	c.contenttype = 'USERINFO'
		and		c.content_status = 'current'
		and		pe.entity_key = 'confluence.user.profile.location'
		) location
on location.user_key = users.lastmodifier
group by location
;