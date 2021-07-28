package com.f4sitive.api.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.dialect.H2Dialect;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.data.relational.core.sql.IdentifierProcessing;
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.security.Principal;
import java.util.concurrent.TimeUnit;

@EnableR2dbcAuditing
@EnableMongoAuditing
@Configuration(proxyBeanMethods = false)
public class DataConfig {
    @Bean
    MongoClientSettingsBuilderCustomizer mongoClientSettingsBuilderCustomizer() {
        return clientSettingsBuilder -> clientSettingsBuilder
                .applyToClusterSettings(builder -> builder
                        .serverSelectionTimeout(2000, TimeUnit.MILLISECONDS)
                )
                .applyToSocketSettings(builder -> builder
                        .readTimeout(2000, TimeUnit.MILLISECONDS)
                        .connectTimeout(2000, TimeUnit.MILLISECONDS)
                );
    }

    @Bean
    BeanPostProcessor mappingMongoConverterBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof MappingMongoConverter) {
                    ((MappingMongoConverter) bean).setTypeMapper(new DefaultMongoTypeMapper(null, ((MappingMongoConverter) bean).getMappingContext()));
                }
                return bean;
            }
        };
    }

    @Bean
    ReactiveAuditorAware<String> reactiveAuditorAware() {
        return () -> Mono.deferContextual(context -> ServerWebExchangeContextFilter.get(Context.of(context))
                .map(exchange -> exchange.getPrincipal().map(Principal::getName))
                .orElseGet(Mono::empty));
    }

    @Bean
    @Order
    public BeanPostProcessor r2dbcMappingContextBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof R2dbcMappingContext) {
                    ((R2dbcMappingContext) bean).setForceQuote(true);
                }
                return bean;
            }
        };
    }

    @Configuration(proxyBeanMethods = false)
    protected static class R2dbcConfig extends AbstractR2dbcConfiguration {
        @Override
        public ConnectionFactory connectionFactory() {
            throw new IllegalStateException();
        }

        @Override
        public R2dbcDialect getDialect(ConnectionFactory connectionFactory) {
            return dialect(super.getDialect(connectionFactory));
        }

        R2dbcDialect dialect(R2dbcDialect dialect) {
            if (dialect instanceof org.springframework.data.r2dbc.dialect.MySqlDialect) {
                return MySqlIdDialect.INSTANCE;
            }
            if (dialect instanceof org.springframework.data.r2dbc.dialect.H2Dialect) {
                return H2IdDialect.INSTANCE;
            }
            return dialect;
        }

        static class H2IdDialect extends H2Dialect {
            IdentifierProcessing identifierProcessing = IdentifierProcessing.create(new IdentifierProcessing.Quoting("\"") {
                @Override
                public String apply(String identifier) {
                    return "id".equals(identifier) ? identifier : super.apply(identifier);
                }
            }, IdentifierProcessing.LetterCasing.LOWER_CASE);

            static final H2IdDialect INSTANCE = new H2IdDialect();

            @Override
            public IdentifierProcessing getIdentifierProcessing() {
                return identifierProcessing;
            }
        }

        static class MySqlIdDialect extends MySqlDialect {
            IdentifierProcessing identifierProcessing = IdentifierProcessing.create(new IdentifierProcessing.Quoting("`") {
                @Override
                public String apply(String identifier) {
                    return "id".equals(identifier) ? identifier : super.apply(identifier);
                }
            }, IdentifierProcessing.LetterCasing.LOWER_CASE);

            static final MySqlIdDialect INSTANCE = new MySqlIdDialect();

            @Override
            public IdentifierProcessing getIdentifierProcessing() {
                return identifierProcessing;
            }
        }
    }
}
