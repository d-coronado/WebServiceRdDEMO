package org.dcoronado.WebServiceRdDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WebServiceRdDemo {
    static {
        // Forzar uso de Xerces como SAX parser
        System.setProperty(
                "javax.xml.parsers.SAXParserFactory",
                "org.apache.xerces.jaxp.SAXParserFactoryImpl"
        );
    }

    public static void main(String[] args) {
        SpringApplication.run(WebServiceRdDemo.class, args);
    }
}
