package com.example.friend_service;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoContainers {

	private static final String IMAGE_NAME = "mongo:5.0";
	private static final String IMAGE_NAME_PROPERTY = "mongo.default.image.name";

	public static MongoDBContainer getDefaultContainer() {
		return new MongoDBContainer(DockerImageName.parse(System.getProperty(IMAGE_NAME_PROPERTY, IMAGE_NAME)))
				.withReuse(true);
	}
}