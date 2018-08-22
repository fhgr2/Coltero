select *
from (
select	count(*) as count
,		date_trunc('quarter', creationdate)::DATE as date
from	spaces
where 	spacestatus = 'CURRENT'
group by date
order by date asc
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;
