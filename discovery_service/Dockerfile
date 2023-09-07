FROM openjdk:17

VOLUME [ "/tmp" ]

COPY target/discovery_service.jar discovery_service.jar

ENTRYPOINT [ "java", "-jar", "/discovery_service.jar" ]

EXPOSE 7001
