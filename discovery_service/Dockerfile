FROM openjdk:17

VOLUME [ "/tmp" ]

# COPY target/discovery_service-0.0.1-SNAPSHOT.jar discovery_service.jar
COPY target/discovery_service.jar discovery_service.jar

ENTRYPOINT [ "java", "-jar", "/discovery_service.jar" ]

EXPOSE 8761
