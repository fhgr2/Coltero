-- Verhältnis aktive User pro Location
with activeusersinlocation as (
select		location
,			active.date
,		count(active.lastmodifier) as activeUsersOnLocation
from	(
		select		date_trunc('quarter', lastmoddate)::DATE as date
		,			c.lastmodifier
		from		content c
		where		c.contenttype in ('PAGE', 'COMMENT', 'ATTACHMENT')
		and			lastmodifier is not null
		group by 	date, c.lastmodifier
		order by 	date 
		) as active
left join (
	select	pe.string_val as location
	,		c.username
	from	os_propertyentry pe
	join	content c on c.contentid = pe.entity_id
	join	user_mapping um on c.username = um.user_key
	join	cwd_user usr on	usr.lower_user_name = um.lower_username
	where	c.contenttype = 'USERINFO'
	and		c.content_status = 'current'
	and		pe.entity_key = 'confluence.user.profile.location'
) as location
on active.lastmodifier = location.username
where location is not null
group by location, date
order by date
) 
select	*
from	(
select		location
,			activeUsersOnLocation
,			allusers.totalActiveUsersOnDate 
,			active.date
from		activeusersinlocation active
left join (
	select	count(distinct lastmodifier) as totalActiveUsersOnDate
	,		date_trunc('quarter', lastmoddate)::DATE as date
	from	content c
	where	c.contenttype in ('PAGE', 'COMMENT', 'ATTACHMENT')
	and		lastmodifier is not null
	group by date
	order by date 
	) as allusers
on active.date = allusers.date 
group by active.date, location, activeUsersOnLocation, allusers.totalActiveUsersOnDate
) sub1
where	activeUsersOnLocation  >= 5
and     date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE

;