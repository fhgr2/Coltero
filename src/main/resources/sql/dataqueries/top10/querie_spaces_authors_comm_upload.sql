--Top 10 Spaces mit Basis Co-Kommtoren (mit den meisten Autoren+Kommentatoren+Upload)
select		count(distinct c.creator) as usercount
,			s.spacename
from		spaces s
left join	content c on s.spaceid = c.spaceid
where		c.contenttype in ('PAGE', 'ATTACHMENT', 'COMMENT')
and			c.creator is not null
group by	s.spacename
order by	usercount desc
limit 10