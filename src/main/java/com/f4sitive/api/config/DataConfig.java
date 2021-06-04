package com.f4sitive.api.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import java.util.concurrent.TimeUnit;

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
}
