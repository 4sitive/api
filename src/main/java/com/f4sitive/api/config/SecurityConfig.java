package com.f4sitive.api.config;

import lombok.Setter;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.server.BearerTokenServerAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@ConfigurationProperties("security")
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
    @Setter
    private String realm;

    @Setter
    private String key = UUID.randomUUID().toString();

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .cors(Customizer.withDefaults())
                .headers(headerSpec -> headerSpec
                        .contentTypeOptions(ServerHttpSecurity.HeaderSpec.ContentTypeOptionsSpec::disable)
                        .xssProtection(ServerHttpSecurity.HeaderSpec.XssProtectionSpec::disable)
                        .cache(ServerHttpSecurity.HeaderSpec.CacheSpec::disable)
                        .hsts(ServerHttpSecurity.HeaderSpec.HstsSpec::disable)
                        .frameOptions(frameOptionsSpec -> frameOptionsSpec.mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .anonymous(ServerHttpSecurity.AnonymousSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .requestCache(requestCacheSpec -> requestCacheSpec.requestCache(NoOpServerRequestCache.getInstance()))
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .authenticationEntryPoint(((Supplier<ServerAuthenticationEntryPoint>) () -> {
                            BearerTokenServerAuthenticationEntryPoint entryPoint = new BearerTokenServerAuthenticationEntryPoint();
                            entryPoint.setRealmName(realm);
                            return entryPoint;
                        }).get())
                        .bearerTokenConverter(((Supplier<ServerAuthenticationConverter>) () -> {
                            ServerBearerTokenAuthenticationConverter bearerTokenConverter = new ServerBearerTokenAuthenticationConverter();
                            bearerTokenConverter.setAllowUriQueryParameter(true);
                            return bearerTokenConverter;
                        }).get())
                        .jwt(Customizer.withDefaults()))
                .securityMatcher(new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.matchers(
                        exchange -> "http".equals(exchange.getRequest().getURI().getScheme()) ? ServerWebExchangeMatcher.MatchResult.match() : ServerWebExchangeMatcher.MatchResult.notMatch(),
                        ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/swagger-ui.html", "/swagger-ui/*", "/swagger-resources/**", "/v*/api-docs"),
                        EndpointRequest.toAnyEndpoint()
                )))
                .build();
    }

    @Bean
    ReactiveJwtDecoder jwtDecoder() {
        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withSecretKey(new SecretKeySpec(key.getBytes(), "HMACSHA256"))
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(Duration.of(1, ChronoUnit.DAYS))
        ));
        return jwtDecoder;
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("");
        return roleHierarchy;
    }
}
