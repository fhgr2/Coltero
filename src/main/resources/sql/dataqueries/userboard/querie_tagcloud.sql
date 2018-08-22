-- 58 Tag-Cloud
select	lb.name as tag
,		count(lb.name) as weight
from	content c
join	content_label cl on cl.contentid = c.contentid
join	label lb on cl.labelid = lb.labelid
where	c.lastmodifier = ?
and     namespace      = 'global'
and     name          != 'meeting-notes'
and     name          != 'file-list'
group by tag
order by weight desc
;