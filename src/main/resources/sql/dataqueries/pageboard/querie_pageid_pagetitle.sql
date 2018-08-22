-- resolves contentid to title
select	title
from	agg_fullchain
where 	mainid = ?
limit 1;