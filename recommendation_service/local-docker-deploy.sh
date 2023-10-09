
docker network create recommendation-service-local


docker kill recommendation-service
docker rm recommendation-service

# Build 
./mvnw clean compile 

# # Test 
./mvnw test 

# Package 
./mvnw package -Dmaven.test.skip=true

# Docker build image
docker build -t recommendation-service .

docker tag recommendation-service philipasd/recommendation-service:0.0.0

# docker push philipasd/recommendation-service:v0.0.0

# Run the user_api container
# docker run --name user-service -d -t --link postgres-db:postgres -p 7000:7000 user-service