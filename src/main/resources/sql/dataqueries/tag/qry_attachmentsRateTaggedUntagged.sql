-- 40 Verhältnis getaggte Attachements (Wie viele Attachements haben Tags?)
select	count(*) as taggedAttachments
,		(
		select	count(*) as allAttachments
		from	content c
		where	c.prevver is null
		and	c.contenttype = 'ATTACHMENT'
		)
from	content_label
where	labelabletype = 'ATTACHMENT'
;