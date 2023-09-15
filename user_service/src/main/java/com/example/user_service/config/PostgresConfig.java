package com.example.user_service.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import jakarta.validation.constraints.NotNull;


@Configuration
@EnableR2dbcRepositories(basePackages = "com.example.user_service.repository")
@EnableR2dbcAuditing
@EnableTransactionManagement
@Profile({"dev, prod, test"})
public class PostgresConfig extends AbstractR2dbcConfiguration{


    @Value("${postgres.host}")
	private String postgresHost;

	@Value("${postgres.port}")
	private Integer postgresPort;

	@Value("${postgres.database-name}")
	private String databaseName;

	@Value("${spring.application.name}")
	private String applicationName;

	private DataSourceProperties dataSourceProperties;

	PostgresConfig(DataSourceProperties dataSourceProperties) {
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
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        // populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }
}
