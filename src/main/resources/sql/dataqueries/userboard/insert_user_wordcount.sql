-- updates agg_userglobalstatistics with writtenWords
update	 agg_userglobalstatistics
set		 writtenWords = ?
where 	 user_key = ?
;