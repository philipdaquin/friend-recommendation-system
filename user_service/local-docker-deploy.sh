
docker kill user_api
docker rm user_api

# Build 

# ./mvnw clean compile 

# # Test 

# ./mvnw test 

# # Package 

./mvnw package 

# Docker build image
docker build -t user_api .

# Docker run 
docker run --name user_api -d -t --link postgreSql:postgres \n 
-p 7000:7000 user-service-local