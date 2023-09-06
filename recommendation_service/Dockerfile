FROM openjdk:17

VOLUME [ "/tmp" ]

COPY target/recommendation_service.jar recommendation_service.jar

ENTRYPOINT [ "java", "-jar", "/recommendation_service.jar" ]

EXPOSE 7001