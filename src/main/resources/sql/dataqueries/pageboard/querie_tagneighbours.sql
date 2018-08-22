select	lb.name as tag
,		count(lb.name) as occurence
from	agg_fullchain fc
join	content_label cl on cl.contentid = fc.mainid
join	label lb on lb.labelid = cl.labelid
where 	mainid 		= ?
and 	namespace	= 'global'
and 	name		!= 'meeting-notes'
and 	name		!= 'favourite'
and 	name		!= 'blueprint-index-page'
and 	name		!= 'file-list'
group by tag, mainid
order by occurence desc
limit 3
;