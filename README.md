# Virtual Entity Template

## Introduction

This repository describes a virtual entity template with the following components:
* Spring Boot: microservice engine
* PostgreSQL: external database 
* DynamoDB: optional database for CQRS
* AWS Secret Manager: for storing the postgres connection string

## Repository Structure

This directory contains the whole skeleton template. For specific folder's documentation see the inner README.md 

~~~c
├── containers/
├── hooks/
├── infrastructure/
├── src/
├── .gitignore
├── automation_conf.yaml
├── build_script.sh
└── README.md
~~~

* containers: contains the dockerfile which describes the container.
* hooks: contains bash scripts which can be run before and after the deployment phase.
* infrastructure: contains the cloudformation, the Helm description for the POD in kubernates and the ENEL platform metadata.
* src: contains microservice's codebase
* automation_conf.yaml: a descriptor for the pipeline
* build_script.sh: the build script for src directory codebase 

### src directory

#### pom.xml

The *pom.xml* file **must not be touched**.

#### src/main/resources

The instructions for the *resources* directory.

##### application.properties

Edit only the *SECRET MANAGER (Local)* section: add the localstack endpoints in the blank properties.

#### src/main/java

**Do not edit** the **Application** class.

Following the instructions for the classes inside packages.

##### conf package

Package containing the configuration classes:

* **AwsSecret**: class that represents an AWS Secret. **Do not touch**
* **DynamoDBConfig**: class for an example of connection to DynamoDB.
* **RdbmsConfig**: class for setting the datasource connection. **Do not touch**
  
##### controller package

Package containing the REST endpoints and application dto.

Change the following classes:

* **CustomerDto** with your applications domain dto
* **CustomerControllerReadOnly** and its implementation with your read-only endpoints
* **CustomerControllerReadWrite** and its implementation with your write endpoints

##### entity package

Package containing the entity classes, mapped through ORM to PostgreSQL.

Change the **Customer** entity with your entities.

##### mapper package

Package containing the `dto<-->entity` mappers.

Change the **CustomerMapper** with your application domain mappers.

##### repo package

Package containing the classes that implement the repository pattern.

Change the **CustomerRepository** and its implementation with your application own.\
Rely on this class for writing your own repositories.

##### service package

Package containing the service classes.

Change the **CustomerService** and its implementation with your application own.


##### javadoc

You can find more info looking at classes javadoc and comments.

## Testing locally

You must have [localstack](https://github.com/localstack/localstack) installed.\
If you have docker installed, you can start a container with:

```shell
docker run --rm -it -p 4566:4566 -p 4571:4571 localstack/localstack
```

Then, with the **awscli**, run this command (change `<your_local_ip>` with your machine local ip):

```shell
aws secretsmanager create-secret --name secret \
--secret-string '{"host": "<your_local_ip>", "port": "5432","sid": "dbtest","username":"admin","password":"password"}' \
--endpoint-url=http://<your_local_ip>:4566
```
in case you are using **awscli** on windows, the command becomes:

```shell
aws secretsmanager create-secret --name secret \
--secret-string "{\"host\": \"<your_local_ip>\", \"port\": \"5432\",\"sid\": \"dbtest\",\"username\":\"admin\",\"password\":\"password\"}" \
--endpoint-url=http://<your_local_ip>:4566
```

to create a secret for the PostgreSQL database connection.

Run an instance of PostgreSQL in a container with this command:

```shell
docker run --rm --name postgres -p 5432:5432 -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=password -d postgres
```

Enter the PostgreSQL container:

```shell
docker exec -it postgres psql -U admin
```

and create the database and table:

```shell
CREATE DATABASE dbtest;
\c dbtest
CREATE TABLE customer ( ID BIGINT PRIMARY KEY, name VARCHAR(100) NOT NULL, surname VARCHAR(100) NOT NULL );
```

After that, refer to [application.properties](#applicationproperties) paragraph for adding the localstack endpoints.

Set the following environment variables, required by the SDK and localstack:

```shell
## You can change the values with your own

# Case: Linux

PLATFORM_CONTAINER_NAME=my_microservice_name
PLATFORM_MODULE_ID=mod123
PLATFORM_MODULE_DOMAIN=my_domain
PLATFORM_MODULE_VERSION=1.0
# Can be ERROR,WARN,INFO,DEBUG,TRACE
PLATFORM_LOG_LEVEL=DEBUG
# Can be any values
AWS_ACCESS_KEY=access
AWS_SECRET_KEY=secret

:: Case: Windows

set PLATFORM_CONTAINER_NAME=my_microservice_name
set PLATFORM_MODULE_ID=mod123
set PLATFORM_MODULE_DOMAIN=my_domain
set PLATFORM_MODULE_VERSION=1.0
:: Can be ERROR,WARN,INFO,DEBUG,TRACE
set PLATFORM_LOG_LEVEL=DEBUG
:: Can be any values
set AWS_ACCESS_KEY=access
set AWS_SECRET_KEY=secret
```

Enter with `cd` command inside *src/docker_virtualentity* and start the application with:

```shell
java -Dspring.profiles.active=readwrite,readonly -jar
```

The `readonly` profile will activate the read-only endpoints; the `readwrite` profile will activate the read-write ones.

With `ENV_MODE=TEST` the logs will be printed in console and events will be mocked.

You can call an endpoint with a cURL:

```shell
curl --request GET -H "X-PLT-Solution-User: AX0000" localhost:8080/read-only
```

## Swagger

When running `mvn clean package`, if you specify `-Denv.ENV_READ=true`, the swagger for the read-only
endpoints will be generated (*readonly.yaml*); if you specify `-Denv.ENV_WRITE=true`, the swagger for the write endpoints will
be generated.
