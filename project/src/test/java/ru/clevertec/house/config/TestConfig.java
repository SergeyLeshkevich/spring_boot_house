package ru.clevertec.house.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;


@TestConfiguration
public class TestConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer(DynamicPropertyRegistry registry) {
        var container = new PostgreSQLContainer<>("postgres:13.3");
        registry.add("postgresql.driver", container::getDriverClassName);
        return container;
    }
}
