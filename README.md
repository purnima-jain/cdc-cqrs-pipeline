# DDD Aggregates via CDC-CQRS Pipeline using Kafka &amp; Debezium
Spring Boot, Docker, Kafka, Kafka-Connect, Kafka-Streams, MySQL, MySQL CDC Source Connector Debezium, MongoDB &amp; MongoDB Sink Connector for Kafka. 

## Description
Refer to this Debezium blog-post [here]: [https://debezium.io/blog/2023/02/04/ddd-aggregates-via-cdc-cqrs-pipeline-using-kafka-and-debezium/]

## Execution Instructions
To bring up the entire Kafka infrastructure in Docker environment, cd to the directory containing docker-compose.yml and then execute the command:
```sh
docker-compose up
```
This starts the services for the following components:
- MySQL
- Adminer (formerly known as phpMinAdmin), to manage MySQL via browser
- MongoDB
- Mongo Express, to manage MongoDB via browser
- Zookeeper
- Confluent Kafka
- Kafka Connect

To access Mongo-Express (UI Client for MongoDB): http://localhost:8081

To access Adminer (MySQL Client): http://localhost:8082/
- username=custom_mysql_user
- password=custom_mysql_user_password
- database=app-mysql-db

Once all services have started, register an instance of the Debezium MySQL connector & MongoDB Connector by executing the `Create-MySQL-Debezium-Connector` and `Create-MongoDB-Sink-Connector` request respectively from `cdc-cqrs-pipeline.postman_collection.json`. Execute the request `Get-All-Connectors` to verify that the connectors have been properly created.

Change into the individual directories and spin-up the three Spring-Boot applications:
- order-write-service: runs on port no `8070`
- order-aggregation-service: runs on port no `8071`
- order-read-service: runs on port no `8072`

With this, our setup is complete.

To test the application, execute the request `Post-Shipping-Details` from the postman collection to insert shipping-details and `Post-Item-Details` to insert item-details for a particular order id.

Finally, execute the `Get-Order-By-Order-Id` request in the postman collection to retrieve the complete Order Aggregate.

Once we are done with the Kafka infrstructure, we can bring the entire environment down and erase all the volumes by executing the following command:
```sh
docker-compose down -v
```

> Note: With this setup in Docker, we do NOT need any Kafka infrastructure, including Kafka Client, installed locally on our machine.


[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

[here]: <https://dzone.com/articles/ports-and-adapters-architecture-with-kafka-avro-and-spring-boot>
[https://dzone.com/articles/ports-and-adapters-architecture-with-kafka-avro-and-spring-boot]: <https://dzone.com/articles/ports-and-adapters-architecture-with-kafka-avro-and-spring-boot>
