package com.foursitive.api.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration(proxyBeanMethods = false)
public class DataConfig {
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
