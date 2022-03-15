# Source directory

Here you can find the docker_virtualentity SpringBoot source.

**NOTE:** do not touch section with _Required section_ tag

## PostgresSQL string Connection

The JDBC string connection is dynamically generated from the content of the AWS secret manager. Mandatory values are:

* **host**: postgreSQL hostname 
* **port**: RDBMS listening port
* **sid**: database name
* **username**: database username
* **password**: database password

Using AWS secrete manager you can configure different endpoints by environment. See application.properties and AwsSecret.java files.    