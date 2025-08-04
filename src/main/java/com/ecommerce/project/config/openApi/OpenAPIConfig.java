//package com.ecommerce.project.config;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Collections;
//
//@Configuration
//@AllArgsConstructor
//public class OpenAPIConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI(){
//        return new OpenAPI()
//                .info(new Info().title("APIs for Ecommerce Backend")
//                        .description("This document provides testing and debugging of APIs of this ecommerce project")
//                )
//                .components(new Components()
//                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")
//                        )
//                ).addSecurityItem(
//                        new SecurityRequirement()
//                                .addList("bearerAuth", Collections.emptyList())
//
//                );
//
//    }
//
//}
