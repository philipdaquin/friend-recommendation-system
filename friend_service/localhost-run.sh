
docker kill mongo-container 
docker rm mongo-container

docker run -d --name mongo-container --restart=always -e MONGO_INITDB_ROOT_USERNAME=username -e MONGO_INITDB_ROOT_PASSWORD=password -p 27017:27017 -v mongodb_data_container:/data/db mongo:latest