
docker network create user-service-local


docker kill user-service
docker rm user-service

# Build 
./mvnw clean compile 

# # Test 
./mvnw test 

# Package 
./mvnw package -Dmaven.test.skip=true

# Docker build image
docker build -t user-service .


# Run the user_api container
docker run --name user-service -d -t --link postgres-db:postgres -p 7000:7000 user-service