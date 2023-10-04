
docker kill postgres
docker rm postgres
docker pull postgres

docker run -d --name postgres \                                                  
    --rm \
    -e POSTGRES_PASSWORD=password \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_DB=users \
    -p 5432:5432 \
    postgres


docker pull redis
docker run --name redis -p 6379:6379 -d redis
