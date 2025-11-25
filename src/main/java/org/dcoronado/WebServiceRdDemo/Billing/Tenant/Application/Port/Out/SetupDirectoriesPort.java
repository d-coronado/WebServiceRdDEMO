package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.TreeNodeDto;

public interface SetupDirectoriesPort {
    void createDirectory(TreeNodeDto estructura);
}
