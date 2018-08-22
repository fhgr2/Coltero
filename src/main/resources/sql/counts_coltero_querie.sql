insert into counts_coltero (
	pk_counts, 
	page_count, 
	comment_count, 
	user_count, 
	authors_count, 
	commentators_count, 
	uploaders_count, 
	slackers_count, 
	taggers_count, 
	likers_count
) 
values 
(
	(
		select pk_aggregation from agg_coltero
		order by pk_aggregation desc
		limit 1
	),
	(
		select count(*) as pagecount
		from content 
		where contenttype = 'PAGE'
	), 
	(
		select count(*) as commentcount
		from content 
		where contenttype = 'COMMENT'
	),
	(
		select count(*) as usercount 
		from cwd_user
	),
	(
		select count(distinct creator) as authorscount
		from content
		where contenttype = 'PAGE'
	),
	(
		select count(distinct creator) as commentcount
		from content
		where contenttype = 'COMMENT'
	),
	(
		select count(distinct creator) as uploadercount
		from content
		where contenttype = 'ATTACHMENT'
	),
	(
		select count(user_key)from (
			select user_key from user_mapping
			join cwd_user on cwd_user.lower_user_name = user_mapping.lower_username
			where cwd_user is not null
			except
			select content.creator
			from content
			where contenttype = 'PAGE'
			except
			select content.creator
			from content
			where contenttype = 'COMMENT'
			except
			select content.creator
			from content
			where contenttype = 'ATTACHMENT'
			except
			select content.creator from content
			full join content_label on content_label.contentid = content.contentid
			where labelid is not null
			except
			select username from likes
		) as subq),
	(
		select count(distinct content.creator) as slackerscount from content
		full join content_label on content_label.contentid = content.contentid
		where labelid is not null
	),
	(
		select count(distinct username) as likerscount
		from likes
	)
	);
