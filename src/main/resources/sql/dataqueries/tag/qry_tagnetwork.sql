---- 37 Tag-Netzwerkdiagramm (just attachments and content / no templates)
--select		l.name
--,			l.labelid
--,			c.contentid
--from		label l
--left join	content_label cl on cl.labelid = l.labelid
--join		content c on c.contentid = cl.contentid
--and			c.prevver is null
--and			c.contenttype not in 	('SPACEDESCRIPTION', 'DRAFT')
--and			c.content_status 	!= 'deleted'
--where		namespace 		!= 'my'
--and			name			!= 'documentation-space-sample'
--and			namespace 		not like '%com.atlassian%'
--and			name 			not like 'theme-%'
--and			name			!= 'meeting-notes'
--and			name			!= 'file-list'
--order by	cl.labelid, c.contentid
--;

select		l.name
,			l.labelid
,			c.contentid
from		label l
left join	content_label cl on cl.labelid = l.labelid
join		content c on c.contentid = cl.contentid
and			c.prevver is null
and			c.contenttype not in 	('SPACEDESCRIPTION', 'DRAFT')
and			c.content_status 	= 'current'
where		namespace 		!= 'my'
and			name			!= 'documentation-space-sample'
and			namespace 		not like '%com.atlassian%'
and			name 			not like 'theme-%'
and			name			!= 'meeting-notes'
and			name			!= 'file-list'
and			l.labelid in 
(
			select	labelid
			from	
			(
				select	labelid
				,	count(labelid)
				from	content_label cl
				join	content c on c.contentid = cl.contentid
				group by labelid
				order by count desc
			) subq1
			where	count >= 10
);