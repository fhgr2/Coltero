-- 49 Anzahl der User, die auf einen Inhalt reagiert haben (Kommentare, Likes)
select	(subq1.count + subq2.count) as reactions
from	(
		select	count(distinct l.username)
		from	content c
		join	likes l on l.contentid = c.contentid
		or		l.contentid = c.pageid
		where	(c.contentid = ?
		or		c.pageid = ?)
		and		c.contenttype = 'PAGE'
		) subq1
,
		(
		select	count(distinct c.lastmodifier)
		from	content c
		where	c.contenttype = 'COMMENT'
		and		c.prevver is null
		and		c.pageid = ?
		) subq2
;
