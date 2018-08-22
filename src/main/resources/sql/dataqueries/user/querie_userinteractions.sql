select *
from (
select	distinct displayname
,		spacename
,		interactions
,		date_trunc('quarter', date::DATE)::DATE as date
from	agg_userinteractions
order by date asc
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;