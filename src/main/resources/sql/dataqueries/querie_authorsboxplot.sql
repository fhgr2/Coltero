select *
from (
select		distinct lastmodifier
,			date_trunc('quarter', lastmoddate)::DATE as date
,			mainid
from		agg_fullchain fc
where 		contenttype = 'PAGE'
and			lastmodifier is not null
group by 	title, lastmodifier, lastmoddate, mainid
order by 	lastmodifier asc, date asc
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;
