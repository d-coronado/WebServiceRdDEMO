package org.dcoronado.WebServiceRdDemo.Config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.dcoronado.WebServiceRdDemo.Config.Properties.DBProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DBProperties.class)
public class DBConfig {

    private final DBProperties dbProperties;

    @Bean
    public DataSource dataSource() {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(dbProperties.getUrl());
        dataSource.setUsername(dbProperties.getUsername());
        dataSource.setPassword(dbProperties.getPassword());
        dataSource.setDriverClassName(dbProperties.getDriverClassName());
        // Optimización del pool de conexiones
        dataSource.addDataSourceProperty("cachePrepStmts", "true");
        dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource.setMaximumPoolSize(10);      // máximo 10 conexiones simultáneas
        dataSource.setMinimumIdle(1);           // Siempre hay 1 conexión lista
        dataSource.setIdleTimeout(108000);      // Si hay más de 1 inactiva, se cierra a los 3 minutos
        dataSource.setMaxLifetime(600000);      // vida máxima de conexión: 10 minutos
        dataSource.setConnectionTimeout(30000); // esperar máximo 30 segundos para obtener conexión

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
