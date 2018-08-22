-- Top 10 Spaces mit den meisten Wörtern in den Pages
select		s.spacename
,			bc.body
from		spaces s
left join 	content c on c.spaceid = s.spaceid
left join	bodycontent bc on bc.contentid = c.contentid
where		c.contenttype = 'PAGE'
and			c.content_status = 'current'
and			c.prevver is null
order by	s.spacename