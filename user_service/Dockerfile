FROM openjdk:17

VOLUME [ "/tmp" ]

COPY target/user_service.jar user_service.jar

ENTRYPOINT [ "java", "-jar", "/user_service.jar" ]

EXPOSE 7000