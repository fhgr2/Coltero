-- get username from userkey
select	display_name
from	cwd_user usr
join	user_mapping um on um.lower_username = usr.lower_user_name
where	user_key = ?