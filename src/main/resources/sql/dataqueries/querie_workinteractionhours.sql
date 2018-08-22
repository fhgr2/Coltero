select	date_part('hour', lastmoddate) + 1 as hour
,		count(*)
from	content
where	content_status = 'current'
group by hour
order by hour
;