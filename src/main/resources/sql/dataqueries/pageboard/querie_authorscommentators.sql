-- 48 Anzahl Authoren / Commentatoren
select	(
	select	count(distinct lastmodifier) as authors
	from	content c
	where	c.contentid = ?
	or	c.prevver = ?
	and	c.content_status = 'current'
	and	c.contenttype = 'PAGE'
),
(
	select	count(distinct lastmodifier) as commentators
	from	content c
	where	c.pageid = ?
	and		c.content_status = 'current'
	and		c.contenttype = 'COMMENT'
)
;
