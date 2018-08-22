-- 36 Meistgenutze usergesetzte Tags (wordcloud)
select		l.name
,			count(*) as count
from		label l
left join	content_label cl on l.labelid = cl.labelid
where		namespace 	!= 'my'
and			name		!= 'documentation-space-sample'
and			namespace 	not like '%com.atlassian%'
and			name 		not like 'theme-%'
and			name		!= 'meeting-notes'
and			name		!= 'file-list'
group by 	l.name, l.labelid
order by 	count desc
limit 20
;