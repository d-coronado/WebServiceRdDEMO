package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.DbSetup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.DatabaseManagerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import static org.dcoronado.WebServiceRdDemo.Shared.Infraestructure.Util.FuncionesGenericasUtil.isLinux;

@Component
@RequiredArgsConstructor
@Slf4j
public class MySQLDatabaseManagerAdapter implements DatabaseManagerPort {

    private static final String SQL_CREATE_DB = "CREATE DATABASE IF NOT EXISTS `%s` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
    private static final String SQL_CREATE_USER = "CREATE USER IF NOT EXISTS '%s'@'%s' IDENTIFIED BY '%s';";
    private static final String SQL_GRANT_PRIVILEGES = "GRANT ALL PRIVILEGES ON `%s`.* TO '%s'@'%s';";
    private static final String SQL_DROP_USER_IF_EXISTS = "DROP USER IF EXISTS '%s'@'%s';";
    private static final String SQL_FLUSH_PRIVILEGES = "FLUSH PRIVILEGES;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createDatabase(String dbName) {
        String sql = String.format(SQL_CREATE_DB, dbName);
        jdbcTemplate.execute(sql);
        log.debug("Base de datos creada: {}", dbName);
    }

    @Override
    public void createUser(String username, String password, String host) {
        String effectiveHost = isLinux() ? "%" : host;
        String sql = String.format(SQL_CREATE_USER, username, effectiveHost, password);
        jdbcTemplate.execute(sql);
        log.debug("Usuario creado: {} para host: {}", username, effectiveHost);
    }

    @Override
    public void grantPrivileges(String dbName, String username, String host) {
        String effectiveHost = isLinux() ? "%" : host;
        String grantSql = String.format(SQL_GRANT_PRIVILEGES, dbName, username, effectiveHost);
        jdbcTemplate.execute(grantSql);
        jdbcTemplate.execute(SQL_FLUSH_PRIVILEGES);
        log.debug("Privilegios otorgados a usuario {} sobre {} para host: {}", username, dbName, effectiveHost);
    }

    @Override
    public void dropUserIfExists(String username, String host) {
        String effectiveHost = isLinux() ? "%" : host;
        String sql = String.format(SQL_DROP_USER_IF_EXISTS, username, effectiveHost);
        log.debug("Usuario eliminado {} para host: {}", username, effectiveHost);
        jdbcTemplate.execute(sql);
    }

}
