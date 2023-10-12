

docker kill config-service
docker rm config-service

# Build 
./mvnw clean compile 

# # Test 
./mvnw test 

# Package 
./mvnw package -Dmaven.test.skip=true

# Docker build image
docker build -t config-service .

docker tag config-service philipasd/config-service:0.0.0

# docker push philipasd/config-service:v0.0.0

# Run the config container
# docker run --name config-service -d -t --link postgres-db:postgres -p 7000:7000 config-service