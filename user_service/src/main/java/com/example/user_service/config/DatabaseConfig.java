package com.example.user_service.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import jakarta.validation.constraints.NotNull;

@Configuration(proxyBeanMethods = false)
@EnableR2dbcRepositories(basePackages = "com.example.repository")
@Profile({"dev, prod, test"})
public class DatabaseConfig extends AbstractR2dbcConfiguration{


    @Value("${postgres.host}")
	private String postgresHost;

	@Value("${postgres.port}")
	private Integer postgresPort;

	@Value("${postgres.database-name}")
	private String databaseName;

	@Value("${spring.application.name}")
	private String applicationName;

	private DataSourceProperties dataSourceProperties;

	DatabaseConfig(DataSourceProperties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}

	@Bean
	public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
		return new R2dbcEntityTemplate(connectionFactory);
	}

    @Override
    public ConnectionFactory connectionFactory() {
		return getPostgresqlConnectionFactory();
    }

    @NotNull
	private PostgresqlConnectionFactory  getPostgresqlConnectionFactory() {
		return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
				.applicationName(applicationName)
				.database(databaseName)
				.host(postgresHost)
				.port(postgresPort)
				.username(dataSourceProperties.getUsername())
				.password(dataSourceProperties.getPassword()).build());
	}

	@Bean
	@ConfigurationProperties("spring.datasource")
	@LiquibaseDataSource
	public DataSource dataSource(DataSourceProperties properties) {
		return new SimpleDriverDataSource(new org.postgresql.Driver(), properties.getUrl(),
				properties.getUsername(), properties.getPassword());
	}
}
