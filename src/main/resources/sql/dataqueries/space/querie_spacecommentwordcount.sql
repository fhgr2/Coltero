--	Read preaggregated wordcount based on comments 
select		spacename
,			wordcount
from		AGG_SPACECOMMENTWORDCOUNT
order by 	wordcount desc
limit 10
;