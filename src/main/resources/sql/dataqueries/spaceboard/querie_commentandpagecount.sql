select	(
	select		count(*) as pages
	from		content c
	where		c.spaceid = ?
	and			c.contenttype = 'PAGE'
	and			c.content_status = 'current'
	and			c.prevver is null
	)
, 	(
	select	count(*) as comments
	from	(
			select		contentid
			from		content c
			where		c.contenttype = 'PAGE'
			and		c.content_status = 'current'
			and		c.prevver is null
			and		c.spaceid = ?
			) subq1
	join	content c2 on c2.pageid = subq1.contentid
	and		c2.contenttype = 'COMMENT'
	and		c2.prevver is null
	and		c2.content_status = 'current'
	);
