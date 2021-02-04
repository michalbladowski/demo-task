## Demo project
Repository containing a demo project imitating registry/account operations

### Goal
Project shows usage of Spring Boot, Spring Data with H2, REST services and tests.
Application has two profiles (default and test). 'Default' is based on stand-alone database (H2 Server)
while 'test' uses embedded H2 database to ensure results are idempotent.

### Database
By default it is expected that database server is running and already contains required initial data.
See 'How to run' section to learn more.

### Endpoints
Application officially exposes three endpoints:
```
GET /balance/all
PUT /registry/{id}/recharge
POST /transfer/from/{fromId}/to/{toId}
```

The first endpoint is used to show current balance of each registry.
The second endpoint is used to alter the value of the registry (account) with given amount.
The third endpoint is used to transfer given amount from one registry to another.

### Tests
For test purposes there are a couple of additional endpoints exposed:
```
GET /balance/cumulative
GET /registry/{id}/details
GET /registry/{id}/balance
GET /transfers
```

First shows cumulative balance of all registries.
Second shows details of specific registry.
Third shows just balance of one registry.
Fourth shows all transactions. Each transfer is stored in additional TRANSFERS table.

### How to start
Download or check-out the project.

First off, we need H2 database server up and running.
Download H2 platform-independent package from the official site and unpack it:

https://h2database.com/h2-2019-03-13.zip

Once unpacked, enter the 'h2' directory and run following command:
```
java -cp bin/h2-1.4.199.jar org.h2.tools.Server -ifNotExists
```

Once started, open the H2 UI (by default on http://localhost:8082) 
and execute SQL statements from following files:
```
/src/main/resources/schema.sql
/src/main/resources/data.sql
```
To make sure tables have been created and populated with initial data, run:
```
SELECT * FROM REGISTRY
```
Once database is set up and populated with data, you it's time to build the application. 
Run:
```
mvn clean install
```

Once build, run the application. Assuming you are in the main project directory, run:
```
java -jar target/task-0.0.1-SNAPSHOT.jar
```

Now you can use your favourite HTTP client to test all the endpoints.

Example REST calls:
```
PUT http://localhost:8080/registry/1/recharge?amount=300
POST http://localhost:8080/transfer/from/1/to/2?amount=1500
GET http://localhost:8080/balance/all
```
### Contact
```
michal.bladowski@gmail.com
https://github.com/michalbladowski
```