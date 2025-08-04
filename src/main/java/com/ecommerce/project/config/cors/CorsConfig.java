package com.ecommerce.project.config.cors;

import com.ecommerce.project.config.ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {
    private final ConfigProperties configProperties;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(configProperties.getCorsAllowedUrls().split(","))
                .allowedMethods(configProperties.getCorsAllowedMethods().split(",")) // Allowing specific HTTP methods including preflight OPTIONS
                .allowedHeaders(configProperties.getCorsAllowedHeaders().split(",")) //allowing all headers from the client
                .exposedHeaders(configProperties.getCorsAllowedHeaders().split(","))// Exposing specific headers to the frontend (optional)
                .allowCredentials(true); // Allowing credentials (cookies, HTTP headers)
    }
}
