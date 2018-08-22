select		count(*) as isolated
,			quarter
from		(
			select		count(distinct lastmodifier) as contributors
			,			date_trunc('quarter', date)::DATE as quarter
			,			spacename
			from		AGG_SPACERELATIONS
			where		spacetype != 'personal'
			group by 	spacename, quarter
) sub
where		contributors = 1
and			quarter < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
group by	quarter
;