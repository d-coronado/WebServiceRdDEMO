package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.Dto.DbConnectionInfo;

public interface ScriptDataBaseExecutorPort {
    void executeScript(DbConnectionInfo data);
}
