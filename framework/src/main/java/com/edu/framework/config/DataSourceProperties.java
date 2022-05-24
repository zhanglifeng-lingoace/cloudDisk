package com.edu.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource.druid")
@Data
public class DataSourceProperties {
    private int maxActive;
    private Integer initialSize;
    private Long maxWait;
    private Integer minIdle;
    private Long timeBetweenEvictionRunsMillis;
    private Long minEvictableIdleTimeMillis;
    private String validationQuery;
    private Integer maxOpenPreparedStatements;
    private String driverClassName;
}
