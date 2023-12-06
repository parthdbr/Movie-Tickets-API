package com.movie.ticket.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "BearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class swaggerConfig {



    @Bean
    public OpenAPI openAPI(){

        Contact contact = new Contact();
        contact.setEmail("movieticket@gmail.com");
        contact.setName("Online Movie Tickets");
        contact.setUrl("https://www.onlinemovietickets.com");
        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");



        Info info = new Info()
                .title("Movie Ticket Booking API")
                .version("0.1.1")
                .contact(contact)
                .description("This is an Movie ticket booking application")
                .license(mitLicense);

        return new OpenAPI().info(info);

    }


}
