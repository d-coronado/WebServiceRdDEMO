package org.dcoronado.WebServiceRdDemo.Config.Properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    private Info info = new Info();
    private Contact contact = new Contact();
    private License license = new License();
    private Gateway gateway = new Gateway();

    @Getter @Setter
    public static class Info {
        private String title;
        private String description;
        private String version;
    }

    @Getter @Setter
    public static class Contact {
        private String name;
        private String email;
        private String url;
    }

    @Getter @Setter
    public static class License {
        private String name;
        private String url;
    }

    @Getter @Setter
    public static class Gateway {
        private String url;
    }
}
