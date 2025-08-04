package com.ecommerce.project.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@PropertySource(value = "classpath/config/properties",  ignoreResourceNotFound = true)
public class ConfigProperties {
    @Value("${token.secret.key:akecom@11/12/2001}")
    String jwtSecretKey;

    @Value("${token.refreshExpirationms:86400000}")
    Long jwtRefreshExpirationMs;

    @Value("${token.expirationms:3600000}")
    Long jwtExpiration;

    @Value("${openapi.description:Ecommerce backend}")
    private String OpenApiDescription;

    @Value("${openapi.swaggerServerUrl:http://localhost:8080}")
    private String OpenApiSwaggerURL;

    @Value("${cors.allowUrls: *}")
    private String corsAllowedUrls;

    @Value("${cors.allowMethods: *}")
    private String corsAllowedMethods;

    @Value("${cors.allowHeaders: *}")
    private String corsAllowedHeaders;

    @Value("${cache_expiration_time_in_minutes:10}")
    private String cacheExpirationTimeInMinutes;

    @Value("${is-cache-enabled:true}")
    private boolean isCacheEnabled;

    @Value("${encryption.key}")
    private String EncryptionKey;

    @Value("${encryption.salt}")
    private String EncryptionSalt;
}
