FROM openjdk:17

VOLUME [ "/tmp" ]

# COPY target/user_service-0.0.1-SNAPSHOT.jar user_service.jar
COPY target/user_service.jar user_service.jar

ENTRYPOINT [ "java", "-jar", "/user_service.jar" ]

EXPOSE 7000