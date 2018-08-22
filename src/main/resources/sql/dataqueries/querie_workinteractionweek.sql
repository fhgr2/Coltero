select	case 	when date_part('dow', lastmoddate) = 0 then 'Sunday' 
			when date_part('dow', lastmoddate) = 1 then 'Monday'
			when date_part('dow', lastmoddate) = 2 then 'Tuesday'
			when date_part('dow', lastmoddate) = 3 then 'Wednesday'
			when date_part('dow', lastmoddate) = 4 then 'Thursday'
			when date_part('dow', lastmoddate) = 5 then 'Friday'
			when date_part('dow', lastmoddate) = 6 then 'Saturday'
			else	'not-defined'
		end	as weekday	
,		date_part('dow', lastmoddate) as dow
,		count(*)
from	content
where	content_status = 'current'
group by	weekday, dow
order by	dow
;