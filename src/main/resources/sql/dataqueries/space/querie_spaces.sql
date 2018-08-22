-- select all spaces 
select		distinct spacename
from		spaces
where		spacestatus = 'CURRENT'
and			spacetype != 'personal'
