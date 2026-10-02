package com.rental.config;

import java.net.URI;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@Profile("render")
public class RenderDataSourceConfiguration {

    @Bean
    DataSource dataSource(
            @Value("${DATABASE_URL}") String databaseUrl,
            @Value("${DATABASE_USERNAME}") String databaseUsername,
            @Value("${DATABASE_PASSWORD}") String databasePassword) {
        URI databaseUri = URI.create(databaseUrl);
        if (!"postgresql".equals(databaseUri.getScheme()) || databaseUri.getHost() == null) {
            throw new IllegalArgumentException("DATABASE_URL must be a PostgreSQL connection URL");
        }

        String port = databaseUri.getPort() > 0 ? ":" + databaseUri.getPort() : "";
        String jdbcUrl = "jdbc:postgresql://" + databaseUri.getHost() + port + databaseUri.getRawPath();
        if (databaseUri.getRawQuery() != null) {
            jdbcUrl += "?" + databaseUri.getRawQuery();
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(databaseUsername);
        config.setPassword(databasePassword);
        config.setMaximumPoolSize(4);
        config.addDataSourceProperty("sslmode", "require");
        return new HikariDataSource(config);
    }
}