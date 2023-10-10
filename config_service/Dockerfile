FROM openjdk:17

VOLUME [ "/tmp" ]

# COPY target/user_service-0.0.1-SNAPSHOT.jar user_service.jar
COPY target/config_service.jar config_service.jar

ENTRYPOINT [ "java", "-jar", "/config_service.jar" ]

EXPOSE 9296