-- Top 10 diskutierte Spaces (von unterschiedlichen Personen kommentiert) -> wie viele unterschiedliche User haben den Space kommentiert
select		count(*) as discussedCount
,			s.spacename
from		spaces s
left join 	content c on s.spaceid = c.spaceid
left join 	content c2 on c2.pageid = c.contentid
where		c2.contenttype = 'COMMENT'
and			c2.version = 1
and			c2.content_status = 'current'
group by 	s.spacename
order by 	discussedCount desc
limit 10