package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out;

public interface DatabaseManagerPort {

    /** Crea una base de datos si no existe */
    void createDatabase(String dbName);

    /** Crea un usuario si no existe */
    void createUser(String username, String password, String host);

    /** Otorga privilegios sobre una base de datos */
    void grantPrivileges(String dbName, String username, String host);

    /** Elimina un usuario si existe */
    void dropUserIfExists(String username, String host);
}
