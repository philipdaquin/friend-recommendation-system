FROM openjdk:17

VOLUME [ "/tmp" ]

COPY target/friend_service-0.0.1-SNAPSHOT.jar friend_service.jar

ENTRYPOINT [ "java", "-jar", "/friend_service.jar" ]

EXPOSE 7002