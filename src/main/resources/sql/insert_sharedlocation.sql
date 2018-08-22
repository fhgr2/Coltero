insert into 	AGG_SHAREDLOCATION (
	location
,	occ
,	spacename

)
(
select	location
,		count(location) as occ
,		spacename
from	agg_fullchain fc
join	(
			select	pe.string_val as location
			,	um.user_key 
			from	os_propertyentry pe
			join	content c on c.contentid = pe.entity_id
			join	user_mapping um on c.username = um.user_key
			where	c.contenttype = 'USERINFO'
			and	c.content_status = 'current'
			and	pe.entity_key = 'confluence.user.profile.location'
		) location
on 		location.user_key = fc.lastmodifier
join	user_mapping um on um.user_key = fc.lastmodifier
join	cwd_user usr on usr.lower_user_name = um.lower_username	
join	spaces s on s.spaceid = fc.spaceid
where	location in 
-- this subquerie ist just for normalizing locations
		(
			select location
			from	
			(
				select	pe.string_val as location
				,		count(pe.string_val) as occ
				from	os_propertyentry pe
				join	content c on c.contentid = pe.entity_id
				join	user_mapping um on c.username = um.user_key
				where	c.contenttype = 'USERINFO'
				and		c.content_status = 'current'
				and		pe.entity_key = 'confluence.user.profile.location'
				group by pe.string_val
				order by occ desc
			) subq1
			where	occ >= 5
		)
group by location, spacename
);