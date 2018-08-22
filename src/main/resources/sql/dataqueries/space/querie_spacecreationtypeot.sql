select	*
from (
select	'global' as type
,		count(*) as total
,		date_trunc('quarter', creationdate)::DATE as date
from	spaces
where	spacetype != 'personal'
and		spacestatus = 'CURRENT'
group by date
union
select	'personal' as type
,		count(*) as total
,		date_trunc('quarter', creationdate)::DATE as date
from	spaces
where	spacetype = 'personal'
and		spacestatus = 'CURRENT'
group by date ) sub
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
order by sub.date