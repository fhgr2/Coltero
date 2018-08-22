---- 44 Co-Space Netzwerkdiagramm (Space im Zentrum 1,2,3-Ebenen tief)
--with layer1 as (
--select	spaceid
--,		display_name
--,		lastmodifier
--from	agg_fullchain fc
--join	user_mapping um on um.user_key = fc.lastmodifier
--join	cwd_user usr on usr.lower_user_name = um.lower_username	
--where	lastmodifier in 
--(
--		select	distinct lastmodifier
--		from	agg_fullchain fc
--		where	spaceid = ? --spaceid
--)
--),
--
--layer2 as (
--select	spaceid
--,		display_name
--,		lastmodifier
--from	agg_fullchain fc
--join	user_mapping um on um.user_key = fc.lastmodifier
--join	cwd_user usr on usr.lower_user_name = um.lower_username	
--where	lastmodifier in 
--(
--		select distinct lastmodifier
--		from layer1
--)
--),
--
--layer3 as (
--select	spaceid
--,		display_name
--,		lastmodifier
--from	agg_fullchain fc
--join	user_mapping um on um.user_key = fc.lastmodifier
--join	cwd_user usr on usr.lower_user_name = um.lower_username	
--where	lastmodifier in 
--(
--		select	distinct lastmodifier
--		from layer2
--)
--)
--select	spacename
--,		display_name
--from	$LAYER l -- layer1 - 3 
--join 	spaces s on s.spaceid = l.spaceid
--;
-- 44 Co-Space Netzwerkdiagramm (Space im Zentrum 1,2,3-Ebenen tief)
select	*
from	(
select	spacename
,		count(display_name) as occ
,		display_name
from	agg_fullchain fc
join	user_mapping um on um.user_key = fc.lastmodifier
join	cwd_user usr on usr.lower_user_name = um.lower_username
join	spaces s on s.spaceid = fc.spaceid
where	fc.lastmodifier in 
(
		select	distinct lastmodifier
		from	agg_fullchain fc
		where	spaceid = ? --spaceid
)
group by spacename, display_name
order by occ desc
) subq1
where occ >= 10