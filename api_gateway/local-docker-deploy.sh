
docker network create gateway-service-local


docker kill api-gateway
docker rm api-gateway

# Build 
./mvnw clean compile 

# # Test 
./mvnw test 

# Package 
./mvnw package -Dmaven.test.skip=true

# Docker build image
docker build -t api-gateway .

docker tag api-gateway philipasd/api-gateway:v0.0.0

docker push philipasd/api-gateway:v0.0.0

# Run the user_api container
# docker run --name user-service -d -t --link postgres-db:postgres -p 7000:7000 user-service