package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;

import java.util.UUID;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.*;

@Getter
@EqualsAndHashCode
public class ConfiguracionBD {

    private final String nombreBD;
    private final String hostBD;
    private final String puertoBD;
    private final String usuario;
    private final String password;
    private final String urlConexion;

    private ConfiguracionBD (
            String nombreBD,
            String hostBD,
            String puertoBD,
            String usuario,
            String password,
            String urlConexion
    ) {
        this.nombreBD = trimOrNull(nombreBD);
        this.hostBD = trimOrNull(hostBD);
        this.puertoBD = trimOrNull(puertoBD);
        this.usuario = trimOrNull(usuario);
        this.password = trimOrNull(password);
        this.urlConexion = trimOrNull(urlConexion);
    }

    public static ConfiguracionBD create(RNC rnc, String host, String puerto) {
        required(rnc, "RNC requerido");
        notBlank(host, "Host requerido");
        notBlank(puerto, "Puerto requerido");
        final String nombreBD = "tennat_fe_rd_demo_" + rnc.getValor();
        final String usuario = "tennat_fe_rd_demo_user" + rnc.getValor();
        final String password = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String urlConexion = String.format("jdbc:mysql://%s:%s/%s", host, puerto, nombreBD);
        return new ConfiguracionBD(nombreBD, host, puerto, usuario, password, urlConexion);
    }

    public static ConfiguracionBD reconstructFromDatabase(
            String nombreBD,
            String hostBD,
            String puertoBD,
            String usuario,
            String password,
            String urlConexion
    ) {
        return new ConfiguracionBD(
                nombreBD,
                hostBD,
                puertoBD,
                usuario,
                password,
                urlConexion
        );
    }

}
