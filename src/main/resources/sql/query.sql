mysql -h localhost -u demo -p D1e2m3o4O1E3AE!3
use demo_hotels;
show tables;
desc HOTEL;
desc CATEGORY;
mvn liquibase:rollback -Dliquibase.rollbackCount=18