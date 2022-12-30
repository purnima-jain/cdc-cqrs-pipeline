# cdc-cqrs-pipeline
Complete CDC (MySQL) &amp; CQRS (MongoDB) pipeline via Kafka, Debezium &amp; Docker-Compose


To bring the entire application with all the services up:
> cd D:\Work\EclipseWorkspace\Github\cdc-cqrs-pipeline
> docker-compose up

To create the Connector, go to Postman and execute "Create-MySQL-Debezium-Connector" & "Create-MongoDB-Sink-Connector" request.

To check the status of Connector and ensure that it's running, go to Postman and execute "Get-MySQL-Connector-Status" & "Get-MongoDB-Connector-Status" request. Also, check out the link: http://localhost:8083/

Go to Adminer UI on http://localhost:8082/ and login using custom_mysql_user, custom_mysql_user_password and app-mysql-db.

Create any table and insert some values in it.

To list all the topics created, run:
> docker run -it --rm --name kafka_client_consumer --network app_network confluentinc/cp-kafka:7.0.1 /bin/kafka-topics --bootstrap-server kafka_server:29092 --list

To look at the messages published on Kafka by Debezium, run:
> docker run -it --rm --name kafka_client_consumer --network app_network confluentinc/cp-kafka:7.0.1 /bin/kafka-console-consumer --bootstrap-server kafka_server:29092 --topic app-mysql-server.app-mysql-db.customer --from-beginning

To check for messages in MongoDB, go to Mongo-Express UI running at http://localhost:8081/ and follow the UI to check the data in the database "customer_db" and the collection "customer". 

To bring the entire application down including all the services and erase the volumes, use:
> docker-compose down -v

