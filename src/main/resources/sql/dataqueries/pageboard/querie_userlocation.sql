-- 51 Anzahl User pro Location
select	count(distinct subq1.user_key) as users
,		location
from	(
		select	usr.display_name
		,		pe.string_val as location
		,		um.user_key 
		from	os_propertyentry pe
		join	content c on c.contentid = pe.entity_id
		join	user_mapping um on c.username = um.user_key
		join	cwd_user usr on	usr.lower_user_name = um.lower_username
		where	c.contenttype = 'USERINFO'
		and		c.content_status = 'current'
		and		pe.entity_key = 'confluence.user.profile.location'
		) subq1
join 	(
		select	lastmodifier
		from	content c
		where	contentid = ?
		or		prevver = ?
		and		contenttype = 'PAGE'
		and		content_status = 'current'
		) subq2
on 	subq1.user_key = subq2.lastmodifier
group by location,  subq1.user_key
order by location
;