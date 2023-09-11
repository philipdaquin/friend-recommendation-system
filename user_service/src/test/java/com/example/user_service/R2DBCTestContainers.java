package com.example.user_service;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

public class R2DBCTestContainers {
    private static final String IMAGE_NAME = "postgres:latest";

	public static PostgreSQLContainer getDefaultContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse(IMAGE_NAME))
            .withCopyFileToContainer(MountableFile.forClasspathResource("init.sql"), "/docker-entrypoint-initdb.d/init.sql")    
            .withReuse(true);
	}

}
