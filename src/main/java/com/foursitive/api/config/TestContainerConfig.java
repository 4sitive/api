package com.foursitive.api.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.testcontainers.containers.MongoDBContainer;

@Configuration(proxyBeanMethods = false)
@Profile({"test"})
public class TestContainerConfig {
    @ConditionalOnClass(MongoDBContainer.class)
    @Configuration(proxyBeanMethods = false)
    @Profile({"test"})
    protected static class MongoDBContainerConfiguration {
        @Bean
        @Order
        public BeanPostProcessor mongoPropertiesBeanPostProcessor(MongoDBContainer mongoDBContainer) {
            return new BeanPostProcessor() {
                @Override
                public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                    if (bean instanceof MongoProperties) {
                        ((MongoProperties) bean).setUri(mongoDBContainer.getReplicaSetUrl());
                        ((MongoProperties) bean).setHost(null);
                        ((MongoProperties) bean).setPort(null);
                    }
                    return bean;
                }
            };
        }

        @Bean(initMethod = "start", destroyMethod = "stop")
        public MongoDBContainer mongoDBContainer() {
            return new MongoDBContainer("mongo:4.0.10");
        }
    }
}
