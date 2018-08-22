-- select key-value pairs space, username
select		spacename
,			display_name
from		agg_spacerelations
where		spacetype != 'personal'
order by 	spacename
;