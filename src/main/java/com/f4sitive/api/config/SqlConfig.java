package com.f4sitive.api.config;

import io.r2dbc.proxy.ProxyConnectionFactory;
import io.r2dbc.proxy.core.Bindings;
import io.r2dbc.proxy.core.QueryInfo;
import io.r2dbc.proxy.support.MethodExecutionInfoFormatter;
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter;
import io.r2dbc.spi.ConnectionFactory;
import lombok.Setter;
import net.logstash.logback.argument.StructuredArguments;
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
import org.springframework.lang.NonNull;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "sql")
class SqlConfig {
    private final Logger log = LoggerFactory.getLogger("SQL");
    @Setter
    private long threshold;

    boolean isLogEnabled(long elapsedTime, boolean success, Throwable throwable) {
        return threshold <= elapsedTime || !success || throwable != null;
    }

    String format(String query) {
        switch (query.replaceAll("--.*\n", "").replace("\n", "").replaceAll("/\\*.*\\*/", "")
                .trim().split(" ", 2)[0].toUpperCase()) {
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

    @Bean
    @Order
    BeanPostProcessor connectionFactoryBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
                if (bean instanceof ConnectionFactory) {
                    QueryExecutionInfoFormatter queryInfo = QueryExecutionInfoFormatter
                            .showAll().delimiter(System.lineSeparator());
                    MethodExecutionInfoFormatter methodInfo = MethodExecutionInfoFormatter.withDefault();
                    return ProxyConnectionFactory.builder((ConnectionFactory) bean)
                            .onAfterMethod(method -> log.trace("{}", LogMessage.of(() -> methodInfo.format(method))))
                            .onAfterQuery(query -> {
                                long elapsedTime = query.getExecuteDuration().toMillis();
                                boolean success = query.isSuccess();
                                Throwable throwable = query.getThrowable();
                                if (isLogEnabled(elapsedTime, success, throwable)) {
                                    query.getQueries().replaceAll(q -> new QueryInfo(format(q.getQuery())) {
                                        @Override
                                        @NonNull
                                        public List<Bindings> getBindingsList() {
                                            return q.getBindingsList();
                                        }
                                    });
                                    log.info(queryInfo.format(query),
                                            StructuredArguments.keyValue("elapsed_time", elapsedTime),
                                            throwable);
                                }
                            })
                            .build();
                }
                return bean;
            }
        };
    }
}