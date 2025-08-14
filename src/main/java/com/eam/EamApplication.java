package com.eam;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Enterprise Asset Management (EAM) API",
        version = "1.0.0",
        description = "Comprehensive API for managing enterprise assets, inventory, work orders, maintenance, and authentication",
        contact = @Contact(
            name = "EAM Development Team",
            email = "support@eam.com",
            url = "https://eam.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Development Server"
        ),
        @Server(
            url = "https://api.eam.com",
            description = "Production Server"
        )
    }
)
public class EamApplication {

	private static final Logger log = LoggerFactory.getLogger(EamApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EamApplication.class, args);
		log.info("EAM Service started...");
	}

}
