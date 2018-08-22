select	string_val as location
,		count(*) as count		
from	os_propertyentry
where	entity_key = 'confluence.user.profile.location'
group by location
order by count desc
;