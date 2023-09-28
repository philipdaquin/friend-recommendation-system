
# docker kill user_service_test
# docker rm user_service_test

# Build 

# ./mvnw clean compile 

# # Test 

# ./mvnw test 

# # Package 

# ./mvnw package 

# # Docker build image
# docker build -t user_service_test .

# Docker run 
docker run --name user_service_test -d -t --link postgresdb_test:postgres -p 7000:7000 user_service_user-service-network