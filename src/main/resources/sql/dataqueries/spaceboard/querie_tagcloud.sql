-- 45 Tag Cloud (pages and attachments, just global tag-namespace)
select	lb.name as tag
,		count(*) as weight
from	
		(
		select		*
		from		content c
		where		c.prevver is null
		and			contenttype in ('PAGE', 'ATTACHMENT')
		and			content_status = 'current'
		and			spaceid = ?
		) subq1
join		content_label cl on cl.contentid = subq1.contentid
join		label lb on cl.labelid = lb.labelid
and			namespace		= 'global'
and			name			!= 'meeting-notes'
and			name			!= 'file-list'
group by 	lb.name
order by 	weight desc
;