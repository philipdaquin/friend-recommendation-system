
echo 'Deleting all external dependencies...'


docker kill  api-gateway
docker kill redis-db
docker rm redis-db
docker rm  api-gateway