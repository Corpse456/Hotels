drop database if exists demo_hotels;
create database demo_hotels CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

grant all on demo_hotels.* to "demo"@"%" identified by "D1e2m3o4O1E3AE!3";
grant all on demo_hotels.* to "demo"@"localhost" identified by "D1e2m3o4O1E3AE!3";
grant all on demo_hotels.* to "demo"@"127.0.0.1" identified by "D1e2m3o4O1E3AE!3";

grant super on *.* to "demo"@"%" identified by "D1e2m3o4O1E3AE!3";
grant super on *.* to "demo"@"localhost" identified by "D1e2m3o4O1E3AE!3";
grant super on *.* to "demo"@"127.0.0.1" identified by "D1e2m3o4O1E3AE!3";

grant select on mysql.proc to "demo"@"%" identified by "D1e2m3o4O1E3AE!3";
grant select on mysql.proc to "demo"@"localhost" identified by "D1e2m3o4O1E3AE!3";
grant select on mysql.proc to "demo"@"127.0.0.1" identified by "D1e2m3o4O1E3AE!3";

flush privileges;
