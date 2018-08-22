--Top 10 kollaborativste Spaces (von unterschiedlichen Personen geschrieben) -> wie viele unterschiedliche User haben im Space geschrieben
select		count(distinct c.creator) as usercount
,			s.spacename
from		spaces s
left join	content c on s.spaceid = c.spaceid
where		c.contenttype = 'PAGE'
and			c.creator is not null
group by	s.spacename
order by	usercount desc
limit 10