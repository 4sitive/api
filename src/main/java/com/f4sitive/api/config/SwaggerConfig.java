package com.f4sitive.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.OAuth2Scheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContextBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.Arrays;
import java.util.Collections;

@ConfigurationProperties("swagger")
@Configuration(proxyBeanMethods = false)
public class SwaggerConfig {
    @Bean
    SecurityConfiguration securityConfiguration() {
        return SecurityConfigurationBuilder.builder().enableCsrfSupport(true).useBasicAuthenticationWithAccessCodeGrant(true).build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .build()
                .securitySchemes(Collections.singletonList(OAuth2Scheme.OAUTH2_AUTHORIZATION_CODE_FLOW_BUILDER
                        .name("OAuth2")
                        .authorizationUrl("https://account.4sitive.com/oauth/authorize")
                        .tokenUrl("https://account.4sitive.com/oauth/token")
                        .scopes(Arrays.asList(new AuthorizationScope("profile", "")))
                        .build()))
                .securityContexts(Collections.singletonList(new SecurityContextBuilder()
                        .securityReferences(Arrays.asList(new SecurityReference("OAuth2", new AuthorizationScope[]{new AuthorizationScope("profile", "")})))
                        .build()));
    }

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Authorization", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                        )
                        .addSecuritySchemes("OAuth2", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .clientCredentials(new OAuthFlow()
                                                .tokenUrl("/v1/auth/oauth/token")
                                        )
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl("/v1/auth/oauth/authorize")
                                                .tokenUrl("/v1/auth/oauth/token")
                                        )
                                )
                        )
                        .addSecuritySchemes("Bearer", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList("Authorization")
                        .addList("OAuth2")
                        .addList("Bearer")
                );
    }
}
