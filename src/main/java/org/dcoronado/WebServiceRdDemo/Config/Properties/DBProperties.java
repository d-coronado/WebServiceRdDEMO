package org.dcoronado.WebServiceRdDemo.Config.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DBProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
