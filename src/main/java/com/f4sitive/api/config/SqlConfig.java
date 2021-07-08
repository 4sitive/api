package com.f4sitive.api.config;

import io.r2dbc.proxy.ProxyConnectionFactory;
import io.r2dbc.proxy.core.Bindings;
import io.r2dbc.proxy.core.QueryInfo;
import io.r2dbc.proxy.support.MethodExecutionInfoFormatter;
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter;
import io.r2dbc.spi.ConnectionFactory;
import lombok.Setter;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.log.LogMessage;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sql")
public class SqlConfig {
    private final Logger log = LoggerFactory.getLogger("SQL");
    @Setter
    private long threshold;

    @Bean
    @Order
    public BeanPostProcessor connectionFactoryBeanPostProcessor() {
        return new BeanPostProcessor() {
            String query(String query) {
                switch (query.replaceAll("--.*\n", "").replace("\n", "").replaceAll("/\\*.*\\*/", "").trim().split(" ", 2)[0].toUpperCase()) {
                    case "SELECT":
                    case "INSERT":
                    case "UPDATE":
                    case "DELETE":
                        return FormatStyle.BASIC.getFormatter().format(query);
                    case "CREATE":
                    case "ALTER":
                    case "DROP":
                        return FormatStyle.DDL.getFormatter().format(query);
                    default:
                        return FormatStyle.NONE.getFormatter().format(query);
                }
            }

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof ConnectionFactory) {
                    QueryExecutionInfoFormatter queryExecutionInfoFormatter = QueryExecutionInfoFormatter.showAll().delimiter(System.lineSeparator());
                    MethodExecutionInfoFormatter methodExecutionInfoFormatter = MethodExecutionInfoFormatter.withDefault();
                    return ProxyConnectionFactory.builder((ConnectionFactory) bean)
                            .onAfterMethod(execInfo -> log.trace("{}", LogMessage.of(() -> methodExecutionInfoFormatter.format(execInfo))))
                            .onAfterQuery(queryExecutionInfo -> {
                                if (threshold <= queryExecutionInfo.getExecuteDuration().toMillis()) {
                                    queryExecutionInfo.getQueries().replaceAll(queryInfo -> new QueryInfo(query(queryInfo.getQuery())) {
                                        @Override
                                        public List<Bindings> getBindingsList() {
                                            return queryInfo.getBindingsList();
                                        }
                                    });
                                    log.info(queryExecutionInfoFormatter.format(queryExecutionInfo));
                                }
                            })
                            .build();
                }
                return bean;
            }
        };
    }
}
