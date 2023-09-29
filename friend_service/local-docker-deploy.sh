
docker network create friend-service-local


docker kill friend-service
docker rm friend-service

# Build 
./mvnw clean compile 

# # Test 
./mvnw test 

# Package 
./mvnw package -Dmaven.test.skip=true

# Docker build image
docker build -t friend-service .

docker tag friend-service philipasd/friend-service:v0.0.0

docker push philipasd/friend-service:v0.0.0

# Run the user_api container
# docker run --name user-service -d -t --link postgres-db:postgres -p 7000:7000 user-service