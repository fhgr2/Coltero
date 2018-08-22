-- 3739 Anzahl tags pro Zeitinterval (month)
select * 
from		(
select		date_trunc('quarter', cl.creationdate)::DATE as date
,			count(*) as count
from		content_label cl
left join 	label l on l.labelid = cl.labelid
where		cl.creationdate is not null
group by 	date
order by 	date 
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;

