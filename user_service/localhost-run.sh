
docker kill postgres
docker rm postgres
docker kill redis 
docker rm redis 

docker pull postgres
docker pull redis


docker run -d --name postgres --rm -e POSTGRES_PASSWORD=password -e POSTGRES_USER=postgres -e POSTGRES_DB=users -p 5432:5432 postgres


docker run --name redis -p 6379:6379 -d redis
