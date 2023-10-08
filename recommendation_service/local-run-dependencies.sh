docker kill neo4j 
docker rm neo4j 
docker kill kafka_broker
docker rm kafka_broker

docker kill zookeeper
docker rm zookeeper

docker pull bitnami/neo4j:latest
docker pull confluentinc/cp-zookeeper:latest
docker pull confluentinc/cp-kafka:latest

docker run -d --name neo4j \
  -p 7475:7474 \
  -p 7687:7687 \
  -p 7473:7473 \
  -e NEO4J_PASSWORD=password \
  -e NEO4J_HOST=username \
  -e NEO4J_ALLOW_UPGRADE=true \
  bitnami/neo4j:latest


# Run Zookeeper
docker run -d --name zookeeper \
  -e ZOOKEEPER_CLIENT_PORT=2181 \
  -e ZOOKEEPER_TICK_TIME=2000 \
  confluentinc/cp-zookeeper:latest

# Run Kafka Broker
docker run -d --name kafka_broker -p 9191:9191 \
  -e KAFKA_BROKER_ID=1 \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka_broker:9191 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
  confluentinc/cp-kafka:latest
