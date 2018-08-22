
select		startspace
,			endspace
,			startname
from		(
select		t1.spacename as startspace
,			t1.display_name as startname
,			t2.spacename as endspace
,			t2.display_name as endname
,			lead(t1.spacename) over (order by t1.display_name, t2.display_name) as prevspace1
,			lead(t1.display_name) over (order by t1.display_name, t2.display_name) as prevname1
,			lead(t2.spacename) over (order by t1.display_name, t2.display_name) as prevspace2
,			lead(t2.display_name) over (order by t1.display_name, t2.display_name) as prevname2
from		agg_spacerelations t1
inner join	agg_spacerelations t2 on t1.display_name = t2.display_name
and			t2.spacename != t1.spacename
where		t1.spacename <> t2.spacename
order by 	t1.display_name, t2.display_name
) sub
where		not (startspace = prevspace2 and startname = prevname2)
and			not (endspace = prevspace1 and endname = prevname1)
or			prevspace1 is null
or			startspace = prevspace1
limit 100000
;
