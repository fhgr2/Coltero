select 	* 
from 	agg_coltero agg 
join	counts_coltero  on pk_counts = pk_aggregation  	
where	(? is null)
or	(day = ?::DATE)
order by pk_aggregation desc 
limit 1