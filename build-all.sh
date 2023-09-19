


echo 'Building All Services..'

echo 'Building User Service'
cd user_service \
    && ./mvnw clean install \ 
    && cd -
    
echo 'Building Api Gateway'
cd api_gateway \
    && ./mvnw clean install \ 
    && cd -

echo 'Building Discovery Service'
cd discovery_service \
    && ./mvnw clean install \ 
    && cd -

echo 'Building Friend Service'
cd friend_service \
    && ./mvnw clean install \ 
    && cd -

echo 'Building Recommendation Service'
cd recommendation_service \
    && ./mvnw clean install \ 
    && cd -
