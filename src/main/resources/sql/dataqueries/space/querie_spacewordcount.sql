--	Read preaggregated wordcount 
select		spacename
,			wordcount
from		AGG_SPACEWORDCOUNT
order by 	wordcount desc
limit 10
;