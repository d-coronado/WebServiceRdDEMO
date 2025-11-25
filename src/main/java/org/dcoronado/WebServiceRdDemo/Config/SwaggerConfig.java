package org.dcoronado.WebServiceRdDemo.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.dcoronado.WebServiceRdDemo.Config.Properties.SwaggerProperties;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi apiV1() {
        return GroupedOpenApi.builder()
                .group("version-2")
                .pathsToMatch("/api/v2/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI(SwaggerProperties swaggerProperties) {
        Contact contact = new Contact()
                .name(swaggerProperties.getContact().getName())
                .email(swaggerProperties.getContact().getEmail())
                .url(swaggerProperties.getContact().getUrl());

        Info info = new Info()
                .title(swaggerProperties.getInfo().getTitle())
                .description(swaggerProperties.getInfo().getDescription())
                .version(swaggerProperties.getInfo().getVersion())
                .contact(contact);

        return new OpenAPI().info(info);
    }
}