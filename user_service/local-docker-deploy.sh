
docker network create user-service-local


docker kill user-api
docker rm user-api

# Build 
./mvnw clean compile 

# # Test 
./mvnw test 

# Package 
./mvnw package -Dmaven.test.skip=true

# Docker build image
docker build -t user-api .


# Run the user_api container
docker run --name user-api -d -t --link postgres-db:postgres -p 7000:7000 user-api