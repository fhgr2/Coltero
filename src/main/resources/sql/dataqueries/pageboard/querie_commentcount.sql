-- 47 Anzahl Kommentare insgesamt
select	count(*) as sum
from	content c
where	contenttype = 'COMMENT'
and		content_status = 'current'
and		pageid = ?
;