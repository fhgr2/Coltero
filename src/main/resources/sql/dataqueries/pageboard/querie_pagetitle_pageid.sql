-- resolves contentid to title
select	mainid
from	agg_fullchain
where 	title = ?
limit 1;