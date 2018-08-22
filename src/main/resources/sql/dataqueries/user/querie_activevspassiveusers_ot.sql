-- active vs inactive users
select *
from (
select	active.active
,		active.date
,		case when users.createdUsers is null then 0 else users.createdUsers end
from	(
	select	count(distinct lastmodifier) as active
	,		date_trunc('quarter', lastmoddate)::DATE as date
	from	content c
	where	c.contenttype in ('PAGE', 'COMMENT', 'ATTACHMENT')
	and		lastmodifier is not null
	group by date
	order by date 
) as active
left join (
	select	count(*) as createdUsers
	,		date_trunc('quarter', created_date)::DATE as date
	from	cwd_user
	where	active = 'T'
	group by date
	order by date
) as users 
on active.date = users.date
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;
