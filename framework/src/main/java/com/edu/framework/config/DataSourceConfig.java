package com.edu.framework.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.edu.framework.annotation.MapperScanner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Configuration
@EnableTransactionManagement
@MapperScanner(basePackages = {"${mybatis-plus.basePackages.mapper}"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

    @Autowired
    DataSourceProperties dataSourceProperties;

    @Autowired
    MybatisPlusProperties mybatisPlusProperties;

    @Autowired
    DynamicDataSourceProperties dynamicDataSourceProperties;

    DynamicRoutingDataSource dynamicRouting = null;

//    public Log4j2Filter log4j2Filter() {
//        Log4j2Filter log4j2Filter = new Log4j2Filter();
//        log4j2Filter.setConnectionLogEnabled(false);
//        log4j2Filter.setStatementLogEnabled(true);
//        log4j2Filter.setResultSetLogEnabled(false);
//        log4j2Filter.setStatementExecutableSqlLogEnable(true);
//        log4j2Filter.setStatementSqlPrettyFormat(false);
//        log4j2Filter.setStatementExecuteAfterLogEnabled(false);
//        log4j2Filter.setStatementCloseAfterLogEnabled(false);
//        log4j2Filter.setStatementExecuteBatchAfterLogEnabled(false);
//        log4j2Filter.setStatementPrepareAfterLogEnabled(false);
//        log4j2Filter.setStatementPrepareCallAfterLogEnabled(false);
//        log4j2Filter.setStatementParameterClearLogEnable(false);
//        log4j2Filter.setStatementParameterSetLogEnabled(false);
//        log4j2Filter.setStatementLogErrorEnabled(true);
//        return log4j2Filter;
//    }

    public Slf4jLogFilter slf4jLogFilter() {
        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
        slf4jLogFilter.setConnectionLogEnabled(false);
        slf4jLogFilter.setStatementLogEnabled(true);
        slf4jLogFilter.setResultSetLogEnabled(false);
        slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
        slf4jLogFilter.setStatementSqlPrettyFormat(false);
        slf4jLogFilter.setStatementExecuteAfterLogEnabled(false);
        slf4jLogFilter.setStatementCloseAfterLogEnabled(false);
        slf4jLogFilter.setStatementExecuteBatchAfterLogEnabled(false);
        slf4jLogFilter.setStatementPrepareAfterLogEnabled(false);
        slf4jLogFilter.setStatementPrepareCallAfterLogEnabled(false);
        slf4jLogFilter.setStatementParameterClearLogEnable(false);
        slf4jLogFilter.setStatementParameterSetLogEnabled(false);
        slf4jLogFilter.setStatementLogErrorEnabled(true);
        return slf4jLogFilter;
    }

    public StatFilter statFilter() {
        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(500);
        statFilter.setLogSlowSql(true);
        statFilter.setMergeSql(true);
        return statFilter;
    }

    public DataSource dataSource(DataSourceProperty dataSourceProperty) {
        DruidDataSource druidDataSource = new DruidDataSource();
        BeanUtils.copyProperties(dataSourceProperties, druidDataSource);
        druidDataSource.setUrl(dataSourceProperty.getUrl());
        druidDataSource.setName(dataSourceProperty.getUsername());
        druidDataSource.setUsername(dataSourceProperty.getUsername());
        druidDataSource.setPassword(dataSourceProperty.getPassword());
        List<Filter> filters = new ArrayList<>();
//        filters.add(log4j2Filter());
        filters.add(slf4jLogFilter());
        filters.add(statFilter());
        druidDataSource.setProxyFilters(filters);
        return druidDataSource;
    }

    public DynamicRoutingDataSource dynamicRoutingDataSource() {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        dynamicRoutingDataSource.setPrimary(dynamicDataSourceProperties.getPrimary());
        Map<String, DataSourceProperty> map = dynamicDataSourceProperties.getDatasource();
        for (String key : map.keySet()) {
            dynamicRoutingDataSource.addDataSource(key, dataSource(map.get(key)));
        }

        dynamicRouting = dynamicRoutingDataSource;
        return dynamicRoutingDataSource;
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactory() {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        BeanUtils.copyProperties(mybatisPlusProperties, bean);
        bean.setMapperLocations(mybatisPlusProperties.resolveMapperLocations());
        bean.setDataSource(dynamicRoutingDataSource());
        bean.setPlugins(paginationInterceptor());
        return bean;
    }

    @Bean(name = "dataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dynamicRouting);
        return dataSourceTransactionManager;
    }

    /*
     * 分页插件，自动识别数据库类型
     * 多租户，请参考官网【插件扩展】
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


}
