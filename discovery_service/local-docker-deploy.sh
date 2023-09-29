
docker network create discovery-service-local


docker kill discovery-service
docker rm discovery-service

# Build 
./mvnw clean compile 

# # Test 
./mvnw test 

# Package 
./mvnw package -Dmaven.test.skip=true

# Docker build image
docker build -t discovery-service .

docker tag discovery-service philipasd/discovery-service:v0.0.0

docker push philipasd/discovery-service:v0.0.0

# Run the user_api container
# docker run --name user-service -d -t --link postgres-db:postgres -p 7000:7000 user-service