-- insert into user-statistics (userkey, connections)
insert into agg_userglobalstatistics(user_key, userconnections, contentrange, reactions) values (?, ?, ?, ?);