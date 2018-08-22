select *
from (
select		count(spaceid) as spacecount
,			date_trunc('month', creationdate)::DATE as date
from		spaces
where		spacestatus = 'CURRENT'
and			spacetype != 'personal'
group by 	date
order by	date
) quarter
where date < DATE_TRUNC('quarter', CURRENT_TIMESTAMP )::DATE
;