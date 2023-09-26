FROM openjdk:17

VOLUME [ "/tmp" ]

# COPY target/api_gateway-0.0.1-SNAPSHOT.jar api_gateway.jar
COPY target/api_gateway.jar api_gateway.jar

ENTRYPOINT [ "java", "-jar", "/api_gateway.jar" ]

EXPOSE 9090