-- queries userkey from username
select	user_key as userid
from	cwd_user usr
join	user_mapping um on usr.lower_user_name = um.lower_username
where	usr.display_name = ?
limit 1
;
