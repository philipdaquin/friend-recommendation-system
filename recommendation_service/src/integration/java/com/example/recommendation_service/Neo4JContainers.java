package com.example.recommendation_service;

import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.utility.DockerImageName;

public class Neo4JContainers {

	private static final String IMAGE_NAME = "neo4j:5";
	private static final String IMAGE_NAME_PROPERTY = "mongo.default.image.name";

	public static Neo4jContainer<?> getDefaultContainer() {
		return new Neo4jContainer<>(DockerImageName.parse(System.getProperty(IMAGE_NAME)))
				.withReuse(true);
	}
}