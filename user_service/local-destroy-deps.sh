
echo 'Deleting all external dependencies...'


docker kill redis 
docker kill postgres 

docker rm redis
docker rm postgres 