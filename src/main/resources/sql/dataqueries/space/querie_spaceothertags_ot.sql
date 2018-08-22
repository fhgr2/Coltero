-- Count all spaces with all other tags, and compare to all available tags at this date (rolled sum)
with allTaggedSpaces as (
	select	count(*) as allTaggedSpaces
,	subq.name
,	subq.date 
	from 	(
		select		l.name
		,			s.spaceid
		,			date_trunc('quarter', c.lastmoddate)::DATE date
		from		spaces s
		left join 	content c on c.spaceid = s.spaceid
		left join 	content_label cl on cl.contentid = c.contentid
		left join 	label l on l.labelid = cl.labelid
		where 		c.contenttype = 'SPACEDESCRIPTION'
		and 		c.creator is not null
		and			s.spacestatus = 'CURRENT'
		and			l.name 	in 
					(
						select	distinct l.name
						from	label l
						where 	l.name != 'documentation'
						and	l.name != 'development'
						and	l.name != 'collaboration'
						and	l.name != 'department'
						and	l.name != 'knowledge-bases'
						and	l.name != 'projects'
					)
		group by l.name, date, s.spaceid
		order by date
		) as subq
	group by subq.name, subq.date
	order by subq.date
),
allspacecount as (
select	sum(spacecount) over (order by date rows between unbounded preceding and current row) as spacesSummarized
,		spacecount
,		date
from 	(
		select	count(*) spacecount
		,		date_trunc('quarter', creationdate)::DATE as date
		from	spaces
		where	spacestatus = 'CURRENT'
		group by date
		order by date
		) as sumsubq
)
select *
from (
select	subq.name
,		sum(allTaggedSpaces) over (partition by name order by date rows between unbounded preceding and current row) as spaceTagsSumPartByName
,		spacessummarized
,		subq.date
from	(
		select	distinct s.name
		,			a.spacessummarized
		,			allTaggedSpaces
		,			a.date
		from		allspacecount a
		left join 	allTaggedSpaces s on s.date = a.date 
		where		allTaggedSpaces is not null
		group by 	s.name, a.date, spacecount, allTaggedSpaces, a.spacessummarized
		order by 	date
		)  as subq
group by subq.date, subq.name, subq.allTaggedSpaces, spacessummarized
order by date
) quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;

