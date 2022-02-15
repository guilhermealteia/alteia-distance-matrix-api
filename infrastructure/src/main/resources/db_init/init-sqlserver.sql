drop table dbo.counter;

CREATE TABLE dbo.counter
(
	id VARCHAR (36) PRIMARY KEY NOT NULL DEFAULT newid(),
	name VARCHAR (100),
	value integer
);

USE master;
CREATE LOGIN cleanarch_user WITH PASSWORD = '!@12QWqw';
CREATE USER cleanarch_user FOR LOGIN cleanarch_user;
GRANT INSERT,SELECT,UPDATE,DELETE ON dbo.counter TO cleanarch_user;