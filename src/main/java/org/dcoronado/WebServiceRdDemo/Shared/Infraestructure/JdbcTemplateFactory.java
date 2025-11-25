package org.dcoronado.WebServiceRdDemo.Shared.Infraestructure;

import lombok.RequiredArgsConstructor;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.Dto.DbConnectionInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class JdbcTemplateFactory {

    public JdbcTemplate create(DbConnectionInfo info) {
        DataSource dataSource = new DriverManagerDataSource(
                info.urlConexionBd(),
                info.usuarioBd(),
                info.passwordBd()
        );
        return new JdbcTemplate(dataSource);
    }
}
