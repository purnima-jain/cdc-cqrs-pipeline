FROM confluentinc/cp-kafka-connect:7.0.1

USER root
RUN yum install unzip -y
RUN mkdir -p /usr/share/java/

WORKDIR /

# debezium-connector-mysql
RUN wget https://repo1.maven.org/maven2/io/debezium/debezium-connector-mysql/1.9.5.Final/debezium-connector-mysql-1.9.5.Final-plugin.tar.gz -O /tmp/debezium-connector-mysql.tar.gz \
&& tar -xvzf /tmp/debezium-connector-mysql.tar.gz --directory /usr/share/java/ \
&& rm /tmp/debezium-connector-mysql.tar.gz

# kafka-connector-mongodb
ADD mongodb-kafka-connect-mongodb-1.8.0.zip /tmp/mongodb-kafka-connect-mongodb.zip
RUN unzip /tmp/mongodb-kafka-connect-mongodb.zip -d /usr/share/java/ \
&& rm /tmp/mongodb-kafka-connect-mongodb.zip

