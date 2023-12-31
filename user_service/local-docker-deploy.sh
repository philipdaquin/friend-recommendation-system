
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

docker tag user-service philipasd/user-service:0.0.0

# docker push philipasd/user-service:v0.0.0

# Run the user_api container
# docker run --name user-service -d -t --link postgres-db:postgres -p 7000:7000 user-service