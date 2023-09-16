FROM openjdk:17

VOLUME [ "/tmp" ]

COPY target/recommendation_service-0.0.1-SNAPSHOT.jar recommendation_service.jar

ENTRYPOINT [ "java", "-jar", "/recommendation_service.jar" ]

EXPOSE 7002