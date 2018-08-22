-- 59 Tag-Nachbarnetzwerkdiagramm(User, die die geichen Tag verwendet haben)
select				count(lb.name ) as occ
,					lb.name as tag 
,					spaceid
from				agg_fullchain fc
join				content_label cl on cl.contentid = fc.mainid
join				label lb on lb.labelid = cl.labelid
join				user_mapping um on um.user_key = fc.lastmodifier
join				cwd_user usr on usr.lower_user_name = um.lower_username	
and	namespace		= 'global'
and name			!= 'meeting-notes'
and name			!= 'favourite'
and name			!= 'blueprint-index-page'
and name			!= 'file-list'
and spaceid	 		!= ?
and display_name	not like '%Admin%'
group by spaceid, lb.name
order by spaceid, occ desc
;