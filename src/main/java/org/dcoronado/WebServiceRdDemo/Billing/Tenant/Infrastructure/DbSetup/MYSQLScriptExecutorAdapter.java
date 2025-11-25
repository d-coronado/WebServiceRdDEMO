package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.DbSetup;

import lombok.RequiredArgsConstructor;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.Dto.DbConnectionInfo;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.ScriptDataBaseExecutorPort;
import org.dcoronado.WebServiceRdDemo.Shared.Infraestructure.JdbcTemplateFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import static org.dcoronado.WebServiceRdDemo.Shared.Infraestructure.Util.FileSiytemDiscUtil.readFileFromResources;


@Component
@RequiredArgsConstructor
public class MYSQLScriptExecutorAdapter implements ScriptDataBaseExecutorPort {

    private static final String NAME_SCRIPT_SQL = "sql/schema_tenant_mysql.sql";
    private final JdbcTemplateFactory jdbcTemplateFactory;

    @Override
    public void executeScript(DbConnectionInfo data) {
        JdbcTemplate jdbcTemplate = jdbcTemplateFactory.create(data);
        String scriptSql = readFileFromResources(NAME_SCRIPT_SQL);

        for (String statement : scriptSql.split(";")) {
            String trimmed = statement.trim();
            if (!trimmed.isEmpty()) {
                jdbcTemplate.execute(trimmed);
            }
        }
    }
}
