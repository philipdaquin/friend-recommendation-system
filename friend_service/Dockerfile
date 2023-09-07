FROM openjdk:17

VOLUME [ "/tmp" ]

COPY target/friend_service.jar friend_service.jar

ENTRYPOINT [ "java", "-jar", "/friend_service.jar" ]

EXPOSE 7002