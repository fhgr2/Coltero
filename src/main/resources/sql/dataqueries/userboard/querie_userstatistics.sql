select	userconnections 
,		contentrange 
,		reactions 
,		writtenwords
from 	agg_userglobalstatistics
where	user_key = ?
;