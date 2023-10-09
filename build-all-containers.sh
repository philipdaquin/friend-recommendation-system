


echo 'Building All Services..'


echo 'Building User Service'
cd user_service && bash local-docker-deploy.sh && cd -
    
echo 'Building Api Gateway'
cd api_gateway && bash local-docker-deploy.sh && cd - 

echo 'Building Discovery Service'
cd discovery_service && bash local-docker-deploy.sh && cd -

echo 'Building Friend Service'
cd friend_service && bash local-docker-deploy.sh && cd -

echo 'Building Recommendation Service'
cd recommendation_service && bash local-docker-deploy.sh  && cd -
