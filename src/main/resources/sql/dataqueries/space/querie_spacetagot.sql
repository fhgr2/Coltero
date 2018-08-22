-- select result and calculate precentage
with spacetagproject as (
	select	count(*) as tagged
	,		date_trunc('month',cl.lastmoddate)::DATE as date
	from	label l
	left join	content_label cl on cl.labelid = l.labelid
	left join	content c on c.contentid = cl.contentid
	left join	spaces s on s.spaceid = c.spaceid
	where 	l.name = ?
group by date )
,	allspacecount as (
	select	count(spaceid) as spacecount
	,		date_trunc('month', creationdate)::DATE as date
	from	spaces
	group by date
	)
select *
from 	(
select	(tagged::float / spacecount) * 100 as precentage
,		spacecount - tagged as spacecount
,		tagged
,		a.date
from	allspacecount a
left join spacetagproject s on s.date = a.date 
where	tagged is not null
group by a.date, precentage, spacecount, tagged
order by a.date asc
) quarter
where date < DATE_TRUNC('quarter', CURRENT_TIMESTAMP)::DATE
;

