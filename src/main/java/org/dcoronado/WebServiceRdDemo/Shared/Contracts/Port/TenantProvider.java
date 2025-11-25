package org.dcoronado.WebServiceRdDemo.Shared.Contracts.Port;

import org.dcoronado.WebServiceRdDemo.Shared.Contracts.Dto.TenantInfoDto;

public interface TenantProvider {
    TenantInfoDto getTenantInfoByRnc(String rnc);
}
