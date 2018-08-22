-- Top 10 Spaces based on usercount (modifiers)
select		count(distinct c.lastmodifier) as usercount
,			s.spacename
from 		spaces s
left join	content c on s.spaceid = c.spaceid
where		c.lastmodifier is not null
group by 	s.spacename
order by 	usercount desc
limit 10