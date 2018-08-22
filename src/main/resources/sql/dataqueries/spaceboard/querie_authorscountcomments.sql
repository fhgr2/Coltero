-- Anzahl Pages mit 1,2,3,4>Kommentatoren
select * from
(
	select	subq.title
	,		count(distinct lastmodifier) as value
	,		date_trunc('quarter',c2.lastmoddate)::DATE as date
	from	(
		select		title
		,			spaceid
		,			contentid
		from		content c
		where		prevver is null
		and			content_status = 'current'
		and			contenttype = 'PAGE'
		and			spaceid = ?
	) subq
	join		content c2 on subq.contentid = c2.pageid
	and			c2.contenttype = 'COMMENT'
	and			c2.content_status = 'current'
	group by 	subq.title, date
	order by 	date asc
	)quarter
where date < date_trunc('quarter',CURRENT_TIMESTAMP)::DATE
;